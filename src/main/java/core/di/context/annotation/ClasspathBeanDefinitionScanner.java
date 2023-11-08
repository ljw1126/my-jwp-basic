package core.di.context.annotation;

import com.google.common.collect.Sets;
import core.annotation.Component;
import core.annotation.Controller;
import core.annotation.Repository;
import core.annotation.Service;
import core.di.factory.support.DefaultBeanFactory;
import core.di.factory.support.DefaultBeanDefinition;
import core.di.factory.support.BeanDefinitionRegistry;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ClasspathBeanDefinitionScanner {
    private static final Logger log = LoggerFactory.getLogger(DefaultBeanFactory.class);

    private BeanDefinitionRegistry beanDefinitionRegistry;

    public ClasspathBeanDefinitionScanner(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    public void doScan(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> preInstantiateBeans = getTypesAnnotatedWith(reflections, Controller.class, Service.class, Repository.class, Component.class);
        for(Class<?> candidateBeanType : preInstantiateBeans) {
            log.debug("do scan : {}", candidateBeanType);
            beanDefinitionRegistry.registerBeanDefinition(candidateBeanType, new DefaultBeanDefinition(candidateBeanType));
        }
    }

    private Set<Class<?>> getTypesAnnotatedWith(Reflections reflections, Class<? extends Annotation>... annotations) {
        Set<Class<?>> preInstantiateBeans = Sets.newHashSet();
        for(Class<? extends Annotation> annotation: annotations) {
            preInstantiateBeans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }

        return preInstantiateBeans;
    }
}
