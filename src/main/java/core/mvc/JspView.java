package core.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

public class JspView implements View {
    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private String viewName;

    public JspView(String viewName) {
        log.debug("JspView path : {}", viewName);
        if(viewName == null) {
            throw new NullPointerException("viewName is null. 이동할 URL을 추가해 주세요");
        }

        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        Set<String> keys = model.keySet();
        for(String key : keys) {
            request.setAttribute(key, model.get(key));
        }

        RequestDispatcher rd = request.getRequestDispatcher(viewName);
        rd.forward(request, response);
    }
}
