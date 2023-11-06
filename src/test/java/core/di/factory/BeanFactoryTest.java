package core.di.factory;

import core.di.factory.example.MyQnaService;
import core.di.factory.example.MyUserController;
import core.di.factory.example.MyUserService;
import core.di.factory.example.QnaController;
import next.controller.user.UserController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanFactoryTest {
    private Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    private static final String basePackage = "core.di.factory.example";
    private BeanFactory beanFactory;

    @BeforeEach
    void setUp() {
        beanFactory = new BeanFactory();
        ClasspathBeanDefinitionScanner scanner = new ClasspathBeanDefinitionScanner(beanFactory);
        scanner.doScan(basePackage);
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
    void fieldDI() {
        MyUserService userService = beanFactory.getBean(MyUserService.class);

        assertThat(userService).isNotNull();
        assertThat(userService.getUserRepository()).isNotNull();
    }

    @DisplayName("")
    @Test
    void setterDI() {
        MyUserController userController = beanFactory.getBean(MyUserController.class);

        assertThat(userController).isNotNull();
        assertThat(userController.getMyUserService()).isNotNull();
    }

    @AfterEach
    void tearDown() {
        beanFactory.clear();
    }
}
