package core.di.factory;

import core.di.context.annotation.AnnotatedBeanDefinitionReader;
import core.di.factory.support.DefaultBeanFactory;
import next.config.MyConfigurationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AnnotatedBeanDefinitionReaderTest {
    @DisplayName("")
    @Test
    void register_simple() {
        DefaultBeanFactory beanFactory = new DefaultBeanFactory();
        AnnotatedBeanDefinitionReader annotatedBeanDefinitionReader
                = new AnnotatedBeanDefinitionReader(beanFactory);
        annotatedBeanDefinitionReader.loadBeanDefinitions(MyConfigurationTest.class);

        beanFactory.preInstantiateSingletons();

        assertThat(beanFactory.getBean(MyConfigurationTest.class)).isNotNull();
    }
}