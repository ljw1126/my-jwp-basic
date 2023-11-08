package core.nmvc;

import core.di.factory.AnnotationConfigApplicationContext;
import core.di.factory.ApplicationContext;
import next.config.MyConfiguration;
import next.config.MyConfigurationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class AnnotationHandlerMappingTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MyConfigurationTest.class);
        handlerMapping = new AnnotationHandlerMapping(applicationContext);
        handlerMapping.initialize();
    }

    @DisplayName("")
    @Test
    void getHandler() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/user/findUserId");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }
}