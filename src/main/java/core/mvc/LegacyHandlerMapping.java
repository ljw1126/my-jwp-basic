package core.mvc;

import core.nmvc.HandlerMapping;
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

    @Override
    public Object getHandler(HttpServletRequest request) {
        return mapping.get(request.getRequestURI());
    }

    public void put(String url, Controller controller) {
        mapping.put(url, controller);
    }
}
