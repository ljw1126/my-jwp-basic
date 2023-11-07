package core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@HandlesTypes(WebApplicationInitializer.class)
public class MyServletContainerInitializer implements ServletContainerInitializer {
    private final static Logger log = LoggerFactory.getLogger(MyServletContainerInitializer.class);

    @Override
    public void onStartup(Set<Class<?>> webappInitializerClasses, ServletContext servletContext) throws ServletException {
        List<WebApplicationInitializer> initializers = new LinkedList<>();

        if(webappInitializerClasses != null) {
            for(Class<?> clazz : webappInitializerClasses) {
                try {
                    initializers.add((WebApplicationInitializer) clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error(e.getMessage());
                }
            }
        }

        if(initializers.isEmpty()) {
            servletContext.log("No Spring WebApplicationInitializer types detected on classpath");
            return;
        }

        for(WebApplicationInitializer initializer : initializers) {
            initializer.onStartup(servletContext);
        }
    }
}
