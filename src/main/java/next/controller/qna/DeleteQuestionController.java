package next.controller.qna;

import core.exception.CannotDeleteException;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.JdbcAnswerDao;
import next.dao.JdbcQuestionDao;
import next.service.QuestionService;
import next.utils.UserSessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteQuestionController extends AbstractController {
    private final QuestionService questionService;

    public DeleteQuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/login/loginForm");
        }

        long questionId = Long.parseLong(request.getParameter("questionId"));
        try {
            questionService.delete(questionId, UserSessionUtils.getUser(request.getSession()));
            return jspView("redirect:/");
        } catch (CannotDeleteException e) {
            return jspView("/qna/show.jsp")
                    .addAttribute("question", questionService.findById(questionId))
                    .addAttribute("answerList", questionService.findAllByQuestionId(questionId))
                    .addAttribute("errorMessage", e.getMessage());
        }
    }
}
