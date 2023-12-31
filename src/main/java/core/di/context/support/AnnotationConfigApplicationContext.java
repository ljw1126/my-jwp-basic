package core.di.context.support;

import core.annotation.ComponentScan;
import core.di.context.ApplicationContext;
import core.di.context.annotation.AnnotatedBeanDefinitionReader;
import core.di.context.annotation.ClasspathBeanDefinitionScanner;
import core.di.factory.support.DefaultBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 객체간의 의존관계 연결을 담당하도록 구현
 */
public class AnnotationConfigApplicationContext implements ApplicationContext {
    private static final Logger log = LoggerFactory.getLogger(AnnotatedBeanDefinitionReader.class);
    private DefaultBeanFactory beanFactory;

    public AnnotationConfigApplicationContext(Class<?>... clazz) {
        // 1. @Configuration 의 경우 읽어서 beanFactory 저장
        Object[] basePackages = findBasePackage(clazz);
        beanFactory = new DefaultBeanFactory();
        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(clazz);

        // 2. 그외
        if (basePackages.length > 0) {
            ClasspathBeanDefinitionScanner beanScanner = new ClasspathBeanDefinitionScanner(beanFactory);
            beanScanner.doScan(basePackages);
        }

        beanFactory.preInstantiateSingletons();
    }

    // @ComponentScan value 값을 읽음
    private Object[] findBasePackage(Class<?>[] annotatedClasses) {
        List<Object> basePackages = new ArrayList<>();
        for (Class<?> clazz : annotatedClasses) {
            ComponentScan componentScan = clazz.getAnnotation(ComponentScan.class);

            if (componentScan == null)
                continue;

            for (String basePackage : componentScan.value()) {
                log.debug("Component Scan basePackage : {}", basePackage);
            }
            basePackages.addAll(Arrays.asList(componentScan.value()));
        }

        return basePackages.toArray();
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    @Override
    public Set<Class<?>> getBeanClasses() {
        return beanFactory.getBeanClasses();
    }
}
