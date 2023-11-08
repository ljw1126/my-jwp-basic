package core.di.factory;

import com.google.common.collect.Sets;
import core.annotation.Bean;
import core.annotation.Inject;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.reflections.ReflectionUtils.*;

public class BeanFactoryUtils {

    private static final Logger log = LoggerFactory.getLogger(BeanFactoryUtils.class);

    @SuppressWarnings({"unchecked"})
    public static Set<Method> getInjectedMethods(Class<?> clazz) {
        return getAllMethods(clazz, withAnnotation(Inject.class), withReturnType(void.class));
    }

    @SuppressWarnings({"unchecked"})
    public static Set<Field> getInjectFields(Class<?> clazz) {
        return getAllFields(clazz, withAnnotation(Inject.class));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Set<Constructor> getInjectedConstructors(Class<?> clazz) {
        return getAllConstructors(clazz, withAnnotation(Inject.class));
    }

    public static Set<Method> getBeanMethods(Class<?> clazz) {
        return getAllMethods(clazz, withAnnotation(Bean.class));
    }

    /**
     * 인자로 전달하는 클래스의 생성자 중 @Inject 애노테이션이 설정되어 있는 생성자를 반환
     *
     * @Inject 애노테이션이 설정되어 있는 생성자는 클래스당 하나로 가정한다.
     * @param clazz
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Constructor<?> getInjectedConstructor(Class<?> clazz) {
        Set<Constructor> injectedConstructors = getInjectedConstructors(clazz);
        if(injectedConstructors.isEmpty()) return null;

        return injectedConstructors.iterator().next();
    }

    /**
     * 만약 인자로 전달되는 injectedClazz가 일반/구현 클래스라면 바로 반환
     * 인터페이스인 경우 BeanFactory가 관리하는 모든 클래스 중에 해당 injectedClazz인터페이스를 구현하는 클래스를 찾아 반환
     *
     * @param injectedClazz
     * @param preInstantiatedBeans
     * @return
     */
    public static Optional<Class<?>> findConcreteClass(Class<?> injectedClazz, Set<Class<?>> preInstantiatedBeans){
        if(!injectedClazz.isInterface()) { // 인터페이스가 아닌 일반 클래스의 경우
            return Optional.of(injectedClazz);
        }

        for(Class<?> clazz : preInstantiatedBeans) {
            Set<Class<?>> interfaces = Sets.newHashSet(clazz.getInterfaces()); // clazz 클래스 타입이 가지는 인터페이스 목록 가져옴
            if(interfaces.contains(injectedClazz)) { // 인터페이스 목록에 injectedClazz 인터페이스가 있다면 해당 clazz 반환
                return Optional.of(clazz);
            }
        }

        throw new IllegalStateException(injectedClazz + "인터페이스를 구현하는 Bean이 존재하지 않습니다");
    }

    public static Optional<Object> invokeMethod(Method method, Object bean, Object[] args) {
        try {
            return Optional.ofNullable(method.invoke(bean, args));
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }
}
