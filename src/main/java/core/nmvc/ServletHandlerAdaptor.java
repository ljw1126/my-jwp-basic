package core.nmvc;

import core.mvc.ModelAndView;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletHandlerAdaptor implements HandlerAdapter {
    @Override
    public boolean support(Object handler) {
        return handler instanceof Servlet;
    }

    @Override
    public ModelAndView execute(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ((Servlet)handler).service(request, response);
        return null;
    }
}
