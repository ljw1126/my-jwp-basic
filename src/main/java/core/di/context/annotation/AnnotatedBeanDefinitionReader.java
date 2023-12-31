package core.di.context.annotation;

import core.annotation.Bean;
import core.di.factory.support.DefaultBeanDefinition;
import core.di.factory.support.BeanDefinitionReader;
import core.di.factory.support.BeanDefinitionRegistry;
import core.di.factory.support.BeanFactoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Set;

// @Configuration + @Bean 읽기 전용
public class AnnotatedBeanDefinitionReader implements BeanDefinitionReader {
    private static final Logger log = LoggerFactory.getLogger(AnnotatedBeanDefinitionReader.class);

    private BeanDefinitionRegistry beanDefinitionRegistry;

    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    @Override
    public void loadBeanDefinitions(Class<?>... annotatedClasses) {
        for(Class<?> clazz: annotatedClasses) {
            registerBean(clazz);
        }
    }

    public void registerBean(Class<?> annotatedClazz) {
        // 1. BeanDefinition 타입 저장 (@Configuration 클래스 저장)
        beanDefinitionRegistry.registerBeanDefinition(annotatedClazz, new DefaultBeanDefinition(annotatedClazz));

        // 2. @Configuration 클래스 안 @Bean Method 저장 - 부모 클래스와 Method 정보 정의 후 @Bean 리턴타입으로 저장
        Set<Method> beanMethods = BeanFactoryUtils.getBeanMethods(annotatedClazz, Bean.class);
        for(Method beanMethod : beanMethods) {
            log.debug("@Bean Method : {}", beanMethod);
            log.debug("DeclargingClass : {}", beanMethod.getDeclaringClass());
            log.debug("return type : {}", beanMethod.getReturnType());
            AnnotatedBeanDefinition abd = new AnnotatedBeanDefinition(beanMethod.getReturnType(), beanMethod);
            beanDefinitionRegistry.registerBeanDefinition(beanMethod.getReturnType(), abd);
        }
    }
}
