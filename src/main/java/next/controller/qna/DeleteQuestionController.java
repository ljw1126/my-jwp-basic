package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.utils.UserSessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class DeleteQuestionController extends AbstractController {
    private QuestionDao questionDao = QuestionDao.getInstance();
    private AnswerDao answerDao = AnswerDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/login/loginForm");
        }

        long questionId = Long.parseLong(request.getParameter("questionId"));
        Question question = questionDao.findById(questionId);
        if(!question.isSameUser(UserSessionUtils.getUser(request.getSession()))) {
            throw new IllegalStateException("해당 질문의 작성자만 삭제 가능합니다");
        }

        List<Answer> answerList = answerDao.findAllByQuestionId(questionId);
        boolean checked = answerList.stream().map(Answer::getWriter).anyMatch(writer -> !writer.equals(question.getWriter()));
        if(checked) {
            throw new IllegalStateException("다른 사용자의 답변이 존재하여 질문을 삭제할 수 없습니다");
        }

        questionDao.delete(questionId);
        return jspView("redirect:/");
    }
}
