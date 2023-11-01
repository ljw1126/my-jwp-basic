package next.controller.qna;

import core.exception.DataAccessException;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.JdbcAnswerDao;
import next.model.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteAnswerController extends AbstractController {
    private JdbcAnswerDao answerDao = JdbcAnswerDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long answerId = Long.parseLong(request.getParameter("answerId"));

        ModelAndView modelAndView = jsonView();

        try {
            answerDao.delete(answerId);
            modelAndView.addAttribute("data", Result.ok());
        } catch (DataAccessException e) {
            modelAndView.addAttribute("data", Result.fail("error message"));
        }

        return modelAndView;
    }
}
