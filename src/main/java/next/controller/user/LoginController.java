package next.controller.user;

import core.db.DataBase;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.model.User;
import next.utils.UserSessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginController extends AbstractController {

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
       String userId = request.getParameter("userId");
       String password = request.getParameter("password");

       User user = DataBase.findUserById(userId);

       if(user == null || !user.matchPassword(password)) {
           request.setAttribute("loginFailed", true);
           return jspView("/user/login.jsp");
       } else {
           HttpSession session = request.getSession();
           session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
           return jspView("redirect:/");
       }
    }
}
