package core.di.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import core.annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {
    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private final Set<Class<?>> preInstantiatedBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstantiatedBeans) {
        this.preInstantiatedBeans = preInstantiatedBeans;
    }

    public void initialize() {
        for(Class<?> clazz : preInstantiatedBeans) {
            if(beans.get(clazz) == null) {
                instantiateClass(clazz);
            }
        }
    }

    // @Inject 있으면 instantiateConstructor()로 생성, 그외는 기본 생성자로 생성
    private Object instantiateClass(Class<?> clazz) {
        Object bean = beans.get(clazz);
        if(bean != null) {
            return bean;
        }

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if(injectedConstructor == null) {
            bean = BeanUtils.instantiate(clazz);
            beans.put(clazz, bean);
            return bean;
        }

        log.debug("Constructor : {}", injectedConstructor);
        bean = instantiateConstructor(injectedConstructor);
        beans.put(clazz, bean);
        return bean;
    }

    private Object instantiateConstructor(Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> args = Lists.newArrayList();

        for(Class<?> clazz : parameterTypes) {
            Class<?> concertedClass = BeanFactoryUtils.findConcreteClass(clazz, preInstantiatedBeans);

            if(!preInstantiatedBeans.contains(concertedClass)) {
                throw new IllegalStateException(clazz + "는 Bean 아닙니다");
            }

            Object bean = beans.get(concertedClass);
            if(bean == null) {
                bean = instantiateClass(concertedClass);
            }

            args.add(bean);
        }

        return BeanUtils.instantiateClass(constructor, args.toArray());
    }

    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void clear() {
        beans.clear();
    }

    public Map<Class<?>, Object> getControllers() {
        Map<Class<?>, Object> controllers = new HashMap<>();

        for(Class<?> clazz : preInstantiatedBeans) {
            if(clazz.isAnnotationPresent(Controller.class)) {
                controllers.put(clazz, beans.get(clazz));
            }
        }

        return controllers;
    }
}
