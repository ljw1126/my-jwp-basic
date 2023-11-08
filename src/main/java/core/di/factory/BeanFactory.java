package core.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

// 빈을 추가하고 조회하는 역할
public class BeanFactory implements BeanDefinitionRegistry {
    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Class<?>, BeanDefinition> beanDefinitionMap = Maps.newHashMap();

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory() {
    }

    public void initialize() {
        for (Class<?> clazz : getBeanClasses()) {
            getBean(clazz);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) { // 재귀 호출
        Object bean = beans.get(clazz);
        if (bean != null) {
            return (T) bean;
        }

        BeanDefinition beanDefinition = beanDefinitionMap.get(clazz);

        // @Configuration + @Bean의 경우
        if (beanDefinition != null && beanDefinition instanceof AnnotatedBeanDefinition) {
            Optional<Object> optionalBean = createAnnotatedBean(beanDefinition);
            optionalBean.ifPresent(b -> beans.put(clazz, b));
            return (T) optionalBean.orElse(null);
        }

        Optional<Class<?>> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, getBeanClasses()); // 구현 클래스 타입 가져옴
        if (!concreteClass.isPresent())
            return null;

        beanDefinition = beanDefinitionMap.get(concreteClass.get());
        bean = inject(beanDefinition);
        registerBean(concreteClass.get(), bean);
        return (T) bean;
    }

    private Optional<Object> createAnnotatedBean(BeanDefinition beanDefinition) {
        AnnotatedBeanDefinition abd = (AnnotatedBeanDefinition) beanDefinition;
        Method method = abd.getMethod();
        Object[] args = populateArguments(method);

        return BeanFactoryUtils.invokeMethod(method, getBean(method.getDeclaringClass()), args);
    }

    private Object[] populateArguments(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        List<Object> args = new ArrayList<>();
        for (Class<?> parameter : parameterTypes) {
            Object bean = getBean(parameter);
            if (bean == null) {
                throw new NullPointerException(parameter + "에 해당하는 Bean이 존재하지 않습니다");
            }
            args.add(bean); // TODO. getBean(param) 하는 이유가?
        }
        return args.toArray();
    }

    private Object inject(BeanDefinition beanDefinition) {
        InjectType resolvedInjectMode = beanDefinition.getResolvedInjectMode();
        if (resolvedInjectMode == InjectType.INJECT_CONSTRUCTOR) { // 생성자 DI
            return injectConstructor(beanDefinition);
        } else if (resolvedInjectMode == InjectType.INJECT_FIELD) { // setter, field DI
            return injectFields(beanDefinition);
        } else { // 기본 생성자
            return BeanUtils.instantiate(beanDefinition.getBeanClazz());
        }
    }

    private Object injectConstructor(BeanDefinition beanDefinition) {
        Constructor<?> injectConstructor = beanDefinition.getInjectConstructor(); // 생성자 정보 가져옴

        List<Object> args = new ArrayList<>();
        for (Class<?> clazz : injectConstructor.getParameterTypes()) {
            args.add(getBean(clazz));
        }

        return BeanUtils.instantiateClass(injectConstructor, args.toArray());
    }

    private Object injectFields(BeanDefinition beanDefinition) {
        Object bean = BeanUtils.instantiate(beanDefinition.getBeanClazz()); // 기본 객체 생성
        Set<Field> injectFields = beanDefinition.getInjectFields(); // 주입할 파라미터 정보
        for (Field field : injectFields) {
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
