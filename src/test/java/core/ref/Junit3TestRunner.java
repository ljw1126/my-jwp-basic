package core.ref;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Junit3TestRunner {
    @DisplayName("Junit3Test에서 test로 시작하는 모든 메서드를 실행한다")
    @Test
    void run() throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<Junit3Test> clazz = Junit3Test.class;
        Method[] methods = clazz.getDeclaredMethods();

        Junit3Test object = clazz.newInstance();

        for(Method method : methods) {
            if(method.getName().startsWith("test")) {
                method.invoke(object);
            }
        }
    }
}
