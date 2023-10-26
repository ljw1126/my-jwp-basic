package next.controller.user;

import core.db.DataBase;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.utils.UserSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListUserController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(ListUserController.class);

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/user/loginForm.jsp");
        }

        request.setAttribute("users", DataBase.findAll());
        return jspView("/user/list.jsp");
    }
}
