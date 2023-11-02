package core.di.factory;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class BeanScannerTest {
    private static final Logger log = LoggerFactory.getLogger(BeanScannerTest.class);

    @Test
    void scan() {
        BeanScanner scanner = new BeanScanner("core.di.factory.example");
        Set<Class<?>> beans = scanner.scan();
        for(Class<?> clazz : beans) {
            log.debug("Bean : {}", clazz);
        }
    }
}
