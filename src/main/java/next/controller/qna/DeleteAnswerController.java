package next.controller.qna;

import core.jdbc.DataAccessException;
import core.mvc.Controller;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import core.mvc.View;
import next.dao.AnswerDao;
import next.model.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteAnswerController implements Controller {

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long answerId = Long.parseLong(request.getParameter("answerId"));

        AnswerDao answerDao = new AnswerDao();
        ModelAndView modelAndView = new ModelAndView(null);

        try {
            answerDao.delete(answerId);
            modelAndView.addAttribute("data", Result.ok());
        } catch (DataAccessException e) {
            modelAndView.addAttribute("data", Result.fail("error message"));
        }

        return modelAndView;
    }
}
