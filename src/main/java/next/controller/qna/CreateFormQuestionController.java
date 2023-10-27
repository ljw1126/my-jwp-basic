package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.utils.UserSessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class CreateFormQuestionController extends AbstractController  {
    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/user/loginForm");
        }

        return jspView("/qna/form.jsp");
    }
}
