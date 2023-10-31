package core.mvc;

import next.controller.qna.AddAnswerController;
import next.controller.HomeController;
import next.controller.qna.ApiDeleteQuestionController;
import next.controller.qna.CreateFormQuestionController;
import next.controller.qna.CreateQuestionController;
import next.controller.qna.DeleteAnswerController;
import next.controller.qna.DeleteQuestionController;
import next.controller.qna.ShowController;
import next.controller.qna.UpdateFormQuestionController;
import next.controller.qna.UpdateQuestionController;
import next.controller.user.ListUserController;
import next.controller.user.LoginController;
import next.controller.user.LogoutController;
import next.controller.user.ProfileController;
import next.controller.user.UpdateFormUserController;
import next.controller.user.UpdateUserController;
import next.controller.user.CreateUserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class LegacyHandlerMapping {
    private static final Logger log = LoggerFactory.getLogger(LegacyHandlerMapping.class);

    private Map<String, Controller> mapping = new HashMap<>();

    public LegacyHandlerMapping() {}

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

        mapping.put("/qna/show", new ShowController());
        mapping.put("/qna/form", new CreateFormQuestionController());
        mapping.put("/qna/create", new CreateQuestionController());
        mapping.put("/qna/updateForm", new UpdateFormQuestionController());
        mapping.put("/qna/update", new UpdateQuestionController());
        mapping.put("/qna/delete", new DeleteQuestionController());

        mapping.put("/api/qna/addAnswer", new AddAnswerController());
        mapping.put("/api/qna/deleteAnswer", new DeleteAnswerController());
        mapping.put("/api/qna/deleteQuestion", new ApiDeleteQuestionController());

        log.info("Init Request Mapping!");
    }

    public Controller get(String requestUrl) {
        return mapping.get(requestUrl);
    }

    public void put(String url, Controller controller) {
        mapping.put(url, controller);
    }
}
