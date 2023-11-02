package core.di.factory;

import com.google.common.collect.Sets;
import core.annotation.Inject;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import static org.reflections.ReflectionUtils.*;

public class BeanFactoryUtils {

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
     * 인자로 전달되는 클래스의 구현 클래스. 만약 인자로 전달되는 Class가 인터페이스가 아니면 전달되는 인자가 구현 클래스,
     * 인터페이스인 경우 BeanFactory가 관리하는 모든 클래스 중에 인터페이스를 구현하는 클래스를 찾아 반환
     *
     * @param injectedClazz
     * @param preInstantiatedBeans
     * @return
     */
    public static Class<?> findConcreteClass(Class<?> injectedClazz, Set<Class<?>> preInstantiatedBeans){
        if(!injectedClazz.isInterface()) { // 인터페이스가 아닌 일반 클래스의 경우
            return injectedClazz;
        }

        for(Class<?> clazz : preInstantiatedBeans) {
            Set<Class<?>> interfaces = Sets.newHashSet(clazz.getInterfaces()); // bean 클래스가 가진 인터페이스 목록 가져옴
            if(interfaces.contains(injectedClazz)) { // 인터페이스 구현체가 있다면
                return clazz;
            }
        }

        throw new IllegalStateException(injectedClazz + "인터페이스를 구현하는 Bean이 존재하지 않습니다");
    }
}
