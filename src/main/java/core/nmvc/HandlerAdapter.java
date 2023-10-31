package core.nmvc;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {
    boolean support(Object handler);

    ModelAndView execute(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
