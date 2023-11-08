package core.di.factory;

import core.di.factory.support.DefaultBeanDefinition;
import core.di.factory.example.JdbcUserRepository;
import core.di.factory.example.MyQnaService;
import core.di.factory.example.MyUserController;
import core.di.factory.support.InjectType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BeanDefinitionTest {
    private static final Logger log = LoggerFactory.getLogger(BeanScannerTest.class);

    @DisplayName("기본 생성자, 필드/setter, 생성자 주입")
    @Test
    void getResolvedAutowiredMode() {
        DefaultBeanDefinition definition = new DefaultBeanDefinition(JdbcUserRepository.class);
        assertThat(definition.getResolvedInjectMode()).isEqualTo(InjectType.INJECT_NO);

        definition = new DefaultBeanDefinition(MyUserController.class);
        assertThat(definition.getResolvedInjectMode()).isEqualTo(InjectType.INJECT_FIELD);

        definition = new DefaultBeanDefinition(MyQnaService.class);
        assertThat(definition.getResolvedInjectMode()).isEqualTo(InjectType.INJECT_CONSTRUCTOR);
    }

    @DisplayName("setter DI")
    @Test
    void getInjectProperties() {
        DefaultBeanDefinition definition = new DefaultBeanDefinition(MyUserController.class);
        Set<Field> injectFields = definition.getInjectFields();

        assertThat(injectFields.size()).isOne();

        for(Field field : injectFields) {
            log.debug("inject field : {}", field);
        }
    }

    @DisplayName("생성자 주입시 Field 사이즈는 0이다")
    @Test
    void getConstructor() {
        DefaultBeanDefinition definition = new DefaultBeanDefinition(MyQnaService.class);
        Set<Field> injectFields = definition.getInjectFields();

        assertThat(injectFields).isEmpty();

        Constructor<?> constructor = definition.getInjectConstructor();
        log.debug("inject constructor : {}", constructor);
    }
}