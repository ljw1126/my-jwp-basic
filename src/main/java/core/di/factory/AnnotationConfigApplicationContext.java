package core.di.factory;

import java.util.Set;

/**
 * 객체간의 의존관계 연결을 담당하도록 구현
 */
public class ApplicationContext {
    private BeanFactory beanFactory;

    public ApplicationContext(Object... basePackage) {
        beanFactory = new BeanFactory();
        ClasspathBeanDefinitionScanner beanScanner = new ClasspathBeanDefinitionScanner(beanFactory);
        beanScanner.doScan(basePackage);
        beanFactory.initialize();
    }

    public <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    public Set<Class<?>> getBeanClasses() {
        return beanFactory.getBeanClasses();
    }

    public void clear() {
        beanFactory.clear();
    }
}
