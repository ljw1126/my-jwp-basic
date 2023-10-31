package core.ref;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Junit4TestRunnger {

    @DisplayName("Junit4Test에서 @MyTest 애노테이션이 있는 모든 메서드를 실행한다")
    @Test
    void run() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Junit4Test object = new Junit4Test();

        Class<Junit4Test> clazz = (Class<Junit4Test>) Class.forName("core.ref.Junit4Test");
        Method[] declaredMethods = clazz.getDeclaredMethods();

        for(Method method : declaredMethods) {
            if(method.isAnnotationPresent(MyTest.class)) {
                method.invoke(object);
            }
        }
    }
}
