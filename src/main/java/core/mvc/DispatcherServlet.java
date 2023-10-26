package core.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private RequestMapping rm;

    @Override
    public void init() throws ServletException {
        rm = new RequestMapping();
        rm.initMapping();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUrl = req.getRequestURI();
        log.debug("Method : {}, Request URI : {}", req.getMethod(), requestUrl);

        Controller controller = rm.get(requestUrl);
        try {
            View view = controller.execute(req, resp);
            view.render(req, resp);
        } catch (Exception e) {
            log.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }
}
