package next.controller.qna;

import core.jdbc.DataAccessException;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.AnswerDao;
import next.model.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteAnswerController extends AbstractController {

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long answerId = Long.parseLong(request.getParameter("answerId"));

        AnswerDao answerDao = new AnswerDao();
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
