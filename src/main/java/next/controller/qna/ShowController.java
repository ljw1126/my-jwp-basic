package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.JdbcAnswerDao;
import next.dao.JdbcQuestionDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowController extends AbstractController {
    private JdbcQuestionDao questionDao = JdbcQuestionDao.getInstance();
    private JdbcAnswerDao answerDao = JdbcAnswerDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long questionId = Long.parseLong(request.getParameter("questionId"));

        return jspView("/qna/show.jsp")
                .addAttribute("question", questionDao.findById(questionId))
                .addAttribute("answerList", answerDao.findAllByQuestionId(questionId));
    }
}
