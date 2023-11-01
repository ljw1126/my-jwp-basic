package next.controller;

import core.annotation.Controller;
import core.annotation.RequestMapping;
import core.mvc.ModelAndView;
import core.nmvc.AbstractNewController;
import next.dao.JdbcQuestionDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController extends AbstractNewController {
    private JdbcQuestionDao questionDao = JdbcQuestionDao.getInstance();

    @RequestMapping("/")
    public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return jspView("home.jsp")
                .addAttribute("questions", questionDao.findAll());
    }
}
