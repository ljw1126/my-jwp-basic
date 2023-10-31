package core.nmvc;

import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);

    private Object declearObject;
    private Method method;

    public HandlerExecution(Object declearObject, Method method) {
        this.declearObject = declearObject;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(declearObject, request, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("{} method invoke fail, error message : {}", method, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
