package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.JdbcAnswerDao;
import next.dao.JdbcQuestionDao;
import next.model.Answer;
import next.model.Result;
import next.model.User;
import next.utils.UserSessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddAnswerController extends AbstractController {
    private JdbcAnswerDao answerDao = JdbcAnswerDao.getInstance();
    private JdbcQuestionDao questionDao = JdbcQuestionDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!UserSessionUtils.isLogined(request.getSession())) {
            return jsonView().addAttribute("result", Result.fail("Login is required"));
        }

        User user = UserSessionUtils.getUser(request.getSession());
        Answer answer = new Answer(user.getUserId(),
                request.getParameter("contents"),
                Long.parseLong(request.getParameter("questionId")));

        Answer savedAnswer = answerDao.insert(answer);
        questionDao.updateCountOfAnswer(savedAnswer.getQuestionId());
        int count = answerDao.count(savedAnswer.getQuestionId());

        return jsonView()
                .addAttribute("result", Result.ok())
                .addAttribute("data", savedAnswer)
                .addAttribute("count", count);
    }
}
