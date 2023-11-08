package core.di.factory.support;

import com.google.common.collect.Sets;
import core.di.factory.config.BeanDefinition;
import core.di.factory.support.BeanFactoryUtils;
import core.di.factory.support.InjectType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

public class DefaultBeanDefinition implements BeanDefinition {
    private Class<?> beanClazz;
    private Constructor<?> injectConstructor;
    private Set<Field> injectFields;

    public DefaultBeanDefinition(Class<?> clazz) {
        this.beanClazz = clazz;
        this.injectConstructor = getInjectConstructor(clazz);
        this.injectFields = getInjectFields(clazz, injectConstructor);
    }

    private static Constructor<?> getInjectConstructor(Class<?> clazz) {
        return BeanFactoryUtils.getInjectedConstructor(clazz);
    }

    private Set<Field> getInjectFields(Class<?> clazz, Constructor<?> constructor) {
        if(constructor != null) {
            return Sets.newHashSet();
        }

        Set<Field> injectFields = Sets.newHashSet();

        Set<Class<?>> injectProperties = getInjectPropertiesType(clazz);
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields) {
            if(injectProperties.contains(field.getType())) {
                injectFields.add(field);
            }
        }

        return injectFields;
    }

    private static Set<Class<?>> getInjectPropertiesType(Class<?> clazz) {
        Set<Class<?>> injectProperties = Sets.newHashSet();

        // setter injection
        Set<Method> injectMethod = BeanFactoryUtils.getInjectedMethods(clazz);
        for(Method method : injectMethod) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if(parameterTypes.length != 1) {
                throw new IllegalStateException("DI할 메소드 인자는 하나여야 합니다");
            }

            injectProperties.add(parameterTypes[0]);
        }

        // field injection
        Set<Field> fields = BeanFactoryUtils.getInjectFields(clazz);
        for(Field field : fields) {
            injectProperties.add(field.getType());
        }

        return injectProperties;
    }

    @Override
    public Class<?> getBeanClazz() {
        return beanClazz;
    }

    @Override
    public Constructor<?> getInjectConstructor() {
        return injectConstructor;
    }

    @Override
    public Set<Field> getInjectFields() {
        return injectFields;
    }

    @Override
    public InjectType getResolvedInjectMode() {
        if(injectConstructor != null) {
            return InjectType.INJECT_CONSTRUCTOR;
        }
        if(!injectFields.isEmpty()) {
            return InjectType.INJECT_FIELD;
        }

        return InjectType.INJECT_NO;
    }
}
