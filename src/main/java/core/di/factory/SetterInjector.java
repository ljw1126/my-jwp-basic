package core.di.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class SetterInjector extends AbstractInjector {

    private static final Logger log = LoggerFactory.getLogger(SetterInjector.class);

    public SetterInjector(BeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    Set<?> getInjectedBeans(Class<?> clazz) {
        return BeanFactoryUtils.getInjectedMethods(clazz);
    }

    @Override
    Class<?> getBeanClass(Object injectedBean) {
        Class<?>[] parameterTypes = ((Method) injectedBean).getParameterTypes();
        if(parameterTypes.length != 1) {
            throw new IllegalStateException("DI할 메소드 인자는 하나여야 합니다");
        }

        return parameterTypes[0];
    }

    // injectedBean 메소드, bean 주입 대상, beanFactory
    @Override
    void inject(Object injectedBean, Object bean, BeanFactory beanFactory) {
        Method method = (Method) injectedBean;
        try {
            method.invoke(beanFactory.getBean(method.getDeclaringClass()), bean);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage());
        }
    }
}
