package core.mvc;

import com.google.common.collect.Lists;
import core.nmvc.HandlerAdapter;
import core.nmvc.HandlerExecutionHandlerAdapter;
import core.nmvc.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DispatcherServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMapping handlerMapping;
    private List<HandlerMapping> mappings = Lists.newArrayList();

    private List<HandlerAdapter> adapters = Lists.newArrayList();

    public DispatcherServlet(HandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @Override
    public void init() throws ServletException {
        mappings.add(handlerMapping);
        adapters.add(new HandlerExecutionHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object handler = getHandler(req);
        if(handler == null) {
            throw new IllegalArgumentException("존재하지 않는 URL입니다");
        }

        try {
            ModelAndView mav = execute(handler, req, resp);
            View view = mav.getView();
            view.render(mav.getModel(), req, resp);
        } catch (Exception e) {
            log.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView execute(Object handler, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        for(HandlerAdapter adapter : adapters) {
            if(adapter.support(handler)) {
                return adapter.execute(handler, req, resp);
            }
        }

        return null;
    }

    private Object getHandler(HttpServletRequest request) {
        for(HandlerMapping hm : mappings) {
            Object handler = hm.getHandler(request);
            if(handler != null) {
                return handler;
            }
        }

        return null;
    }
}
