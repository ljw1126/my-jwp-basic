package core.di.factory;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;

public abstract class AbstractInjector implements Injector {

    private static final Logger log = LoggerFactory.getLogger(AbstractInjector.class);

    private BeanFactory beanFactory;

    public AbstractInjector(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void inject(Class<?> clazz) {
        instantiateClass(clazz);
        Set<?> injectedBeans = getInjectedBeans(clazz);
        for(Object injectedBean : injectedBeans) {
            Class<?> beanClass = getBeanClass(injectedBean);
            inject(injectedBean, instantiateClass(beanClass), beanFactory);
        }
    }

    abstract Set<?> getInjectedBeans(Class<?> clazz);

    abstract Class<?> getBeanClass(Object injectedBean);

    abstract void inject(Object injectedBean, Object bean, BeanFactory beanFactory);

    private Object instantiateClass(Class<?> clazz) {
        Object bean = beanFactory.getBean(clazz);
        if(bean != null) {
            return bean;
        }

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if(injectedConstructor == null) {
            bean = BeanUtils.instantiate(clazz);
            beanFactory.registerBean(clazz, bean);
            return bean;
        }

        log.debug("Constructor : {}", injectedConstructor);
        bean = instantiateConstructor(injectedConstructor);
        beanFactory.registerBean(clazz, bean);
        return bean;
    }

    private Object instantiateConstructor(Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> args = Lists.newArrayList();

        Set<Class<?>> preInstantiatedBeans = beanFactory.getPreInstantiatedBeans();
        for(Class<?> clazz : parameterTypes) {
            Class<?> concertedClass = BeanFactoryUtils.findConcreteClass(clazz, preInstantiatedBeans);

            if(!preInstantiatedBeans.contains(concertedClass)) {
                throw new IllegalStateException(clazz + "는 Bean 아닙니다");
            }

            Object bean = beanFactory.getBean(concertedClass);
            if(bean == null) {
                bean = instantiateClass(concertedClass);
            }

            args.add(bean);
        }

        return BeanUtils.instantiateClass(constructor, args.toArray());
    }
}
