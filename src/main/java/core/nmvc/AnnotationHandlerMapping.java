package core.nmvc;

import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnnotationHandlerMapping {
    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    public AnnotationHandlerMapping() {}

    public void initialize() {

    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        return null;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
