package core.nmvc;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import core.annotation.Controller;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import core.di.factory.ApplicationContext;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handler = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        ApplicationContext ac = new ApplicationContext(basePackage);
        Map<Class<?>, Object> controllerMap = getControllers(ac);

        Set<Method> methods = getRequestMappingMethod(controllerMap);

        for(Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            log.debug("register handlerExecution : url is {}, method is {}", requestMapping.value(), requestMapping.method());
            handler.put(createHandlerKey(requestMapping),
                    new HandlerExecution(controllerMap.get(method.getDeclaringClass()), method));
        }

        log.debug("initialized AnnotationHandlerMapping");
    }

    private static Set<Method> getRequestMappingMethod(Map<Class<?>, Object> controllerMap) {
        Set<Method> requestMappingMethod = Sets.newHashSet();
        for(Class<?> clazz : controllerMap.keySet()) {
            requestMappingMethod.addAll(ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class)));
        }
        return requestMappingMethod;
    }

    private HandlerKey createHandlerKey(RequestMapping requestMapping) {
        return new HandlerKey(requestMapping.value(), requestMapping.method());
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());

        log.debug("requestUri : {}, requestMethod : {}", requestURI, requestMethod);
        return handler.get(new HandlerKey(requestURI, requestMethod));
    }


    public Map<Class<?>, Object> getControllers(ApplicationContext ac) {
        Map<Class<?>, Object> controllers = new HashMap<>();

        for(Class<?> clazz : ac.getBeanClasses()) {
            if(clazz.isAnnotationPresent(Controller.class)) {
                controllers.put(clazz, ac.getBean(clazz));
            }
        }

        return controllers;
    }

}
