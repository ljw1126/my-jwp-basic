package core.di.factory.support;

public interface BeanDefinitionRegistry {
    void registerBeanDefinition(Class<?> clazz, DefaultBeanDefinition beanDefinition);
}
