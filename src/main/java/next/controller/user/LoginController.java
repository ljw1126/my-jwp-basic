package next.controller.user;

import core.db.DataBase;
import core.mvc.Controller;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.View;
import next.model.User;
import next.utils.UserSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
       String userId = request.getParameter("userId");
       String password = request.getParameter("password");

       User user = DataBase.findUserById(userId);

       if(user == null || !user.matchPassword(password)) {
           request.setAttribute("loginFailed", true);
           return new ModelAndView("/user/login.jsp");
       } else {
           HttpSession session = request.getSession();
           session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
           return new ModelAndView("redirect:/");
       }
    }
}
