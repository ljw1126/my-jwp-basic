package core.nmvc;

import com.google.common.collect.Maps;
import core.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {
    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getController() {
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(annotated);
    }

    public Map<Class<?>, Object> instantiateControllers(Set<Class<?>> annotated) {
        Map<Class<?>, Object> controllers = Maps.newHashMap();
        try {
            for (Class<?> clazz : annotated) {
                controllers.put(clazz, clazz.getDeclaredConstructor().newInstance());
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            log.error(e.getMessage());
        }

        return controllers;
    }
}
