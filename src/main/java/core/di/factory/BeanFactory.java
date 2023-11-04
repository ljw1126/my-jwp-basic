package core.di.factory;

import com.google.common.collect.Maps;
import core.annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
// 빈을 추가하고 조회하는 역할
public class BeanFactory {
    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private final Set<Class<?>> preInstantiatedBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    private List<Injector> injectors;

    public BeanFactory(Set<Class<?>> preInstantiatedBeans) {
        this.preInstantiatedBeans = preInstantiatedBeans;
        this.injectors = Arrays.asList(
                new ConstructorInjector(this),
                new FieldInjector(this),
                new SetterInjector(this)
        );
    }

    public void initialize() {
        for(Class<?> clazz : preInstantiatedBeans) {
            if(beans.get(clazz) == null) {
                inject(clazz);
            }
        }
    }

    private void inject(Class<?> clazz) {
        for(Injector injector : injectors) {
            injector.inject(clazz);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void clear() {
        preInstantiatedBeans.clear();
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

    public Set<Class<?>> getPreInstantiatedBeans() {
        return preInstantiatedBeans;
    }

    public void registerBean(Class<?> clazz, Object instance) {
        beans.put(clazz, instance);
    }
}
