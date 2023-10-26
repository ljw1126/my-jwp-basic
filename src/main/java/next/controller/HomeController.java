package next.controller;

import core.mvc.Controller;
import next.dao.QuestionDao;
import next.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class HomeController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        QuestionDao questionDao = new QuestionDao();
        List<Question> questions = questionDao.findAll();

        request.setAttribute("questions", questions);

        return "home.jsp";
    }
}
