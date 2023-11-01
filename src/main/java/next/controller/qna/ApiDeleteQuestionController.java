package next.controller.qna;

import core.exception.CannotDeleteException;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.JdbcAnswerDao;
import next.dao.JdbcQuestionDao;
import next.model.Result;
import next.service.QuestionService;
import next.utils.UserSessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiDeleteQuestionController extends AbstractController {
    private final QuestionService questionService;

    public ApiDeleteQuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!UserSessionUtils.isLogined(request.getSession())) {
            return jsonView().addAttribute("data", Result.fail("로그인이 필요합니다"));
        }

        try {
            long questionId = Long.parseLong(request.getParameter("questionId"));
            questionService.delete(questionId, UserSessionUtils.getUser(request.getSession()));

            return jsonView().addAttribute("data", Result.ok());
        } catch(CannotDeleteException e) {
            return jsonView().addAttribute("data", Result.fail(e.getMessage()));
        }
    }
}
