package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.JdbcQuestionDao;
import next.model.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class ApiListQuestionController extends AbstractController {
    private JdbcQuestionDao questionDao = JdbcQuestionDao.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return jsonView().addAttribute("data", questionDao.findAll())
                .addAttribute("status", Result.ok());
    }
}
