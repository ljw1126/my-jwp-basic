package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.JdbcQuestionDao;
import next.dao.QuestionDao;
import next.model.Question;
import next.utils.UserSessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateFormQuestionController extends AbstractController {
    private final QuestionDao questionDao;

    public UpdateFormQuestionController(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/user/loginForm");
        }

        long questionId = Long.parseLong(request.getParameter("questionId"));
        Question question = questionDao.findById(questionId);
        if(!question.isSameUser(UserSessionUtils.getUser(request.getSession()))) {
            throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다");
        }

        return jspView("/qna/updateForm.jsp")
                .addAttribute("question", question);
    }
}
