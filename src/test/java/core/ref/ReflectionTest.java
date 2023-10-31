package core.ref;

import next.model.Question;
import next.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

import static org.assertj.core.api.Assertions.*;

public class ReflectionTest {
    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void showClass() {
        Class<Question> clazz = Question.class;
        log.debug(clazz.getName());
    }

    @DisplayName("10.1.2.4 생성자가 있는 클래스의 인스턴스 생성하기")
    @Test
    void createInstance() throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<User> clazz = (Class<User>) Class.forName("next.model.User");
        Constructor<User>[] constructors = (Constructor<User>[]) clazz.getDeclaredConstructors();

        User user = newInstanceWithConstructorArgs(constructors[0]);

        assertThat(user).isNotNull();
        assertThat(user).extracting("userId", "password", "name", "email")
                .containsExactly("jinwoo3", "1234", "jinwoo", "jinwoo@gmail.com");
    }

    private User newInstanceWithConstructorArgs(Constructor constructor) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return (User) constructor.newInstance("jinwoo3", "1234", "jinwoo", "jinwoo@gmail.com");
    }

    @DisplayName("10.1.2.5 private 필드에 접근하기")
    @Test
    void accessPrivateField() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;

        Student student = new Student();

        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(student, "진우");

        Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);
        age.set(student, 30);

        assertThat(student).extracting("name", "age")
                .containsExactly("진우", 30);
    }
}
