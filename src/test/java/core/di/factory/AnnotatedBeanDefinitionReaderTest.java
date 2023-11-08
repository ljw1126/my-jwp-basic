package core.di.factory;

import next.config.MyConfigurationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AnnotatedBeanDefinitionReaderTest {
    @DisplayName("")
    @Test
    void register_simple() {
        BeanFactory beanFactory = new BeanFactory();
        AnnotatedBeanDefinitionReader annotatedBeanDefinitionReader
                = new AnnotatedBeanDefinitionReader(beanFactory);
        annotatedBeanDefinitionReader.register(MyConfigurationTest.class);

        beanFactory.initialize();

        assertThat(beanFactory.getBean(MyConfigurationTest.class)).isNotNull();
    }
}