package core.nmvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    private static final Logger log = LoggerFactory.getLogger(ControllerScannerTest.class);

    private ControllerScanner cs;

    @BeforeEach
    public void setup() {
        cs = new ControllerScanner("core.nmvc");
    }

    @Test
    void init() throws ClassNotFoundException {
        Map<Class<?>, Object> controllerMap = cs.getController();

        assertThat(controllerMap.get(Class.forName("core.nmvc.MyController")))
                .isNotNull()
                .isInstanceOf(MyController.class);
    }

    @Test
    void printScannedController() {
        Map<Class<?>, Object> classObjectMap = cs.getController();

        for(Class<?> controller : classObjectMap.keySet()) {
            log.debug("controller : {}", controller);
        }
    }
}