package core.ref;

import next.model.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionTest {
    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void showClass() {
        Class<Question> clazz = Question.class;
        log.debug(clazz.getName());
    }

    @DisplayName("10.1.2.4 생성자가 있는 클래스의 인스턴스 생성하기")
    @Test
    void createInstance() {

    }

    @DisplayName("10.1.2.5 private 필드에 접근하기")
    @Test
    void accessPrivateField() {

    }
}
