package core.di.factory.example;

import core.annotation.Controller;
import core.annotation.Inject;
import core.annotation.RequestMapping;
import core.mvc.ModelAndView;
import core.nmvc.AbstractNewController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class QnaController extends AbstractNewController {
    private final MyQnaService myQnaService;

    @Inject
    public QnaController(MyQnaService myQnaService) {
        this.myQnaService = myQnaService;
    }

    @RequestMapping("/questions")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return jspView("/qna/list.jsp");
    }

    public MyQnaService getQnaService() {
        return myQnaService;
    }
}