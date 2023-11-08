package core.di.factory;

import next.config.MyConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class BeanScannerTest {
    private static final Logger log = LoggerFactory.getLogger(BeanScannerTest.class);

    @Test
    void scan() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(MyConfiguration.class);

        Set<Class<?>> beanClasses = ac.getBeanClasses();
        for(Class<?> clazz : beanClasses) {
            log.debug("Bean : {}", clazz);
        }
    }
}
