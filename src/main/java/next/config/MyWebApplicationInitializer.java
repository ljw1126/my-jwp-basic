package next.config;

import core.WebApplicationInitializer;
import core.mvc.DispatcherServlet;
import core.nmvc.AnnotationHandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class MyWebApplicationInitializer implements WebApplicationInitializer {
    private static final Logger log = LoggerFactory.getLogger(MyWebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationHandlerMapping hm = new AnnotationHandlerMapping("next", "core");
        hm.initialize();

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(hm));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}
