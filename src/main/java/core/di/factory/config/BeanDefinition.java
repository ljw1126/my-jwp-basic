package core.di.factory.config;

import core.di.factory.support.InjectType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;

public interface BeanDefinition {
    Class<?> getBeanClazz();

    Constructor<?> getInjectConstructor();

    Set<Field> getInjectFields();

    InjectType getResolvedInjectMode();
}
