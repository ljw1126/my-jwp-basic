package core.nmvc;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
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
        ControllerScanner cs = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllerMap = cs.getController();

        Set<Method> methods = getRequestMappingMethod(controllerMap);

        for(Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            log.debug("register handlerExecution : url is {}, method is {}", requestMapping.value(), requestMapping.method());
            handler.put(createHandlerKey(requestMapping),
                    new HandlerExecution(controllerMap.get(method.getDeclaringClass()), method));
        }
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
}
