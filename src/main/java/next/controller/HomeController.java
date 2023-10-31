package next.controller;

import core.annotation.Controller;
import core.annotation.RequestMapping;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import core.nmvc.AbstractNewController;
import next.dao.QuestionDao;
import next.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class HomeController extends AbstractNewController {
    private QuestionDao questionDao = QuestionDao.getInstance();

    @RequestMapping("/")
    public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return jspView("home.jsp")
                .addAttribute("questions", questionDao.findAll());
    }
}
