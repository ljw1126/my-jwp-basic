package core.di.factory;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class BeanScannerTest {
    private static final Logger log = LoggerFactory.getLogger(BeanScannerTest.class);

    @Test
    void scan() {
        String basePackage = "core.di.factory.example";
        ApplicationContext ac = new ApplicationContext(basePackage);

        Set<Class<?>> beanClasses = ac.getBeanClasses();
        for(Class<?> clazz : beanClasses) {
            log.debug("Bean : {}", clazz);
        }
    }
}
