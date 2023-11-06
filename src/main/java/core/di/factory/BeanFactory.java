package core.di.factory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import core.annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
// 빈을 추가하고 조회하는 역할
public class BeanFactory implements BeanDefinitionRegistry {
    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Class<?>, BeanDefinition> beanDefinitionMap = Maps.newHashMap();

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory() {
    }

    public void initialize() {
        for(Class<?> clazz : getBeanClasses()) {
            getBean(clazz);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) { // 재귀 호출
        Object bean = beans.get(requiredType);
        if(bean != null) {
            return (T) bean;
        }

        Class<?> concreteClass = findConcreteClass(requiredType); // 구현 클래스 타입 가져옴
        BeanDefinition beanDefinition = beanDefinitionMap.get(concreteClass);
        bean = inject(beanDefinition);
        registerBean(concreteClass, bean);
        return (T) bean;
    }

    // Bean 대상 여부
    private Class<?> findConcreteClass(Class<?> clazz) {
        Set<Class<?>> beanClasses = getBeanClasses();
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, beanClasses);
        if (!beanClasses.contains(concreteClass)) {
            throw new IllegalStateException(clazz + "는 Bean이 아닙니다");
        }

        return concreteClass;
    }

    private Object inject(BeanDefinition beanDefinition) {
        InjectType resolvedInjectMode = beanDefinition.getResolvedInjectMode();
        if(resolvedInjectMode == InjectType.INJECT_CONSTRUCTOR) { // 생성자 DI
            return injectConstructor(beanDefinition);
        } else if(resolvedInjectMode == InjectType.INJECT_FIELD) { // setter, field DI
            return injectFields(beanDefinition);
        } else { // 기본 생성자
            return BeanUtils.instantiate(beanDefinition.getBeanClazz());
        }
    }

    private Object injectConstructor(BeanDefinition beanDefinition) {
        Constructor<?> injectConstructor = beanDefinition.getInjectConstructor(); // 생성자 정보 가져옴

        List<Object> args = new ArrayList<>();
        for(Class<?> clazz : injectConstructor.getParameterTypes()) {
            args.add(getBean(clazz));
        }

        return BeanUtils.instantiateClass(injectConstructor, args.toArray());
    }

    private Object injectFields(BeanDefinition beanDefinition) {
        Object bean = BeanUtils.instantiate(beanDefinition.getBeanClazz()); // 기본 객체 생성
        Set<Field> injectFields = beanDefinition.getInjectFields(); // 주입할 파라미터 정보
        for(Field field : injectFields) {
            injectField(bean, field);
        }

        return bean;
    }

    private void injectField(Object bean, Field field) {
        log.debug("Inject Bean : {}, Field : {}", bean, field);
        try {
            field.setAccessible(true);
            field.set(bean, getBean(field.getType())); // bean 객체의 field에 getBean(field.getType())을 주입
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
        }
    }

    public void clear() {
        beanDefinitionMap.clear();
        beans.clear();
    }

    public void registerBean(Class<?> clazz, Object instance) {
        beans.put(clazz, instance);
    }

    @Override
    public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        log.debug("register bean definition : {}", clazz);
        beanDefinitionMap.put(clazz, beanDefinition);
    }

    public Set<Class<?>> getBeanClasses() {
        return beanDefinitionMap.keySet();
    }
}
