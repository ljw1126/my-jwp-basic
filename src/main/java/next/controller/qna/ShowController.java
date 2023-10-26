package next.controller.qna;

import core.mvc.Controller;
import core.mvc.JspView;
import core.mvc.View;
import next.dao.AnswerDao;
import next.dao.QuestionDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowController implements Controller {
    @Override
    public View execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long questionId = Long.parseLong(request.getParameter("questionId"));
        QuestionDao questionDao = new QuestionDao();
        AnswerDao answerDao = new AnswerDao();

        request.setAttribute("question", questionDao.findById(questionId));
        request.setAttribute("answerList", answerDao.findAllByQuestionId(questionId));

        return new JspView("/qna/show.jsp");
    }
}
