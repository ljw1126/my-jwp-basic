package core.mvc;

import next.controller.HomeController;
import next.controller.ListUserController;
import next.controller.LoginController;
import next.controller.LogoutController;
import next.controller.ProfileController;
import next.controller.UpdateFormUserController;
import next.controller.UpdateUserController;
import next.controller.CreateUserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {
    private static final Logger log = LoggerFactory.getLogger(RequestMapping.class);

    private Map<String, Controller> mapping = new HashMap<>();


    public RequestMapping() {}

    public void initMapping() {
        mapping.put("/", new HomeController());
        mapping.put("/user/form", new ForwardController("/user/form.jsp"));
        mapping.put("/user/loginForm", new ForwardController("/user/login.jsp"));
        mapping.put("/user/list", new ListUserController());
        mapping.put("/user/profile", new ProfileController());
        mapping.put("/user/login", new LoginController());
        mapping.put("/user/logout", new LogoutController());
        mapping.put("/user/create", new CreateUserController());
        mapping.put("/user/updateForm", new UpdateFormUserController());
        mapping.put("/user/update", new UpdateUserController());

        log.info("Init Request Mapping!");
    }

    public Controller get(String requestUrl) {
        return mapping.get(requestUrl);
    }

    public void put(String url, Controller controller) {
        mapping.put(url, controller);
    }
}
