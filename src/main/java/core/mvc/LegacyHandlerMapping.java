package core.mvc;

import core.nmvc.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class LegacyHandlerMapping implements HandlerMapping {
    private static final Logger log = LoggerFactory.getLogger(LegacyHandlerMapping.class);

    private Map<String, Controller> mapping = new HashMap<>();

    public LegacyHandlerMapping() {}

    public void initMapping() {
        log.info("Init Request Mapping!");
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return mapping.get(request.getRequestURI());
    }

    public void put(String url, Controller controller) {
        mapping.put(url, controller);
    }
}
