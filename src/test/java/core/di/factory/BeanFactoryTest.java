package core.di.factory;

import core.di.factory.example.MyQnaService;
import core.di.factory.example.QnaController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanFactoryTest {
    private Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    private BeanFactory beanFactory;

    @BeforeEach
    void setUp() {
        BeanScanner scanner = new BeanScanner("core.di.factory.example");
        Set<Class<?>> preInstantiatedBeans = scanner.scan();
        beanFactory = new BeanFactory(preInstantiatedBeans);
        beanFactory.initialize();
    }

    @DisplayName("Controller - Service - Repository Bean 생성 및 DI 확인한다")
    @Test
    public void constructorDI() {
        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertThat(qnaController).isNotNull();
        assertThat(qnaController.getQnaService()).isNotNull();

        MyQnaService qnaService = qnaController.getQnaService();
        assertThat(qnaService.getUserRepository()).isNotNull();
        assertThat(qnaService.getQuestionRepository()).isNotNull();
    }

    @DisplayName("")
    @Test
    void getControllers() {
        Map<Class<?>, Object> controllers = beanFactory.getControllers();
        for(Class<?> key : controllers.keySet()) {
            log.debug("Bean : {}", key);
        }
    }

    @AfterEach
    void tearDown() {
        beanFactory.clear();
    }
}
