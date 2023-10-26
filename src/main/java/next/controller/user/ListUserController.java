package next.controller.user;

import core.db.DataBase;
import core.mvc.Controller;
import core.mvc.JspView;
import core.mvc.View;
import next.utils.UserSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListUserController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(ListUserController.class);

    @Override
    public View execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!UserSessionUtils.isLogined(request.getSession())) {
            return new JspView("redirect:/user/loginForm.jsp");
        }

        request.setAttribute("users", DataBase.findAll());
        return new JspView("/user/list.jsp");
    }
}
