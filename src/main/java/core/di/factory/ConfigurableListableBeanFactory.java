package core.di.factory;

public interface ConfigurableListableBeanFactory extends BeanFactory {
    void preInstantiateSingletons();
}
