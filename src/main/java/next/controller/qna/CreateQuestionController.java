package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.JdbcQuestionDao;
import next.model.Question;
import next.model.User;
import next.utils.UserSessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateQuestionController extends AbstractController {
    private JdbcQuestionDao questionDao = JdbcQuestionDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/user/loginForm");
        }

        User user = UserSessionUtils.getUser(request.getSession());
        Question question = new Question(user.getUserId(), request.getParameter("title"), request.getParameter("contents"));
        questionDao.insert(question);

        return jspView("redirect:/");
    }
}
