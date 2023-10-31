package next.controller.user;

import core.annotation.Controller;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import core.mvc.ModelAndView;
import core.nmvc.AbstractNewController;
import next.dao.UserDao;
import next.model.User;
import next.utils.UserSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UserController extends AbstractNewController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private UserDao userDao = UserDao.getInstance();

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest request, HttpServletResponse response) {
        User user = new User(request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email"));
        log.debug("create user : {}", user);
        userDao.insert(user);
        return jspView("redirect:/");
    }

    @RequestMapping("/user/list")
    public ModelAndView userList(HttpServletRequest request, HttpServletResponse response) {
        if(!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/user/loginForm.jsp");
        }

        request.setAttribute("users", userDao.findAll());
        return jspView("/user/list.jsp");
    }

    @RequestMapping("/user/form")
    public ModelAndView userForm(HttpServletRequest request, HttpServletResponse response) {
        return jspView("/user/form.jsp");
    }

    @RequestMapping("/user/loginForm")
    public ModelAndView userLoginForm(HttpServletRequest request, HttpServletResponse response) {
        return jspView("/user/login.jsp");
    }

    @RequestMapping("/user/profile")
    public ModelAndView profile(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        User user = userDao.findByUserId(userId);
        if(user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다");
        }

        request.setAttribute("user", user);
        return jspView("/user/profile.jsp");
    }

    @RequestMapping("/user/updateForm")
    public ModelAndView updateFormUser(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");

        User user = userDao.findByUserId(userId);
        if(!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalArgumentException("다른 사용자의 정보를 수정할 수 없습니다");
        }

        request.setAttribute("user", user);
        return jspView("/user/updateForm.jsp");
    }

    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public ModelAndView updateUser(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        User user = userDao.findByUserId(userId);
        if(!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalArgumentException("다른 사용자의 정보를 수정할 수 없습니다");
        }

        User updateUser = new User(request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email"));
        log.debug("Update user : {}", updateUser);
        userDao.update(updateUser);

        return jspView("redirect:/");
    }



    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        User user = userDao.findByUserId(userId);

        if(user == null || !user.matchPassword(password)) {
            request.setAttribute("loginFailed", true);
            return jspView("/user/login.jsp");
        } else {
            HttpSession session = request.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return jspView("redirect:/");
        }
    }

    @RequestMapping("/user/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);

        return jspView("redirect:/");
    }

}
