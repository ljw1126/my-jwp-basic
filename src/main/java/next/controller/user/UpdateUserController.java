package next.controller.user;

import core.db.DataBase;
import core.mvc.AbstractController;
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

public class UpdateUserController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(UpdateUserController.class);

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userId = request.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if(!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalArgumentException("다른 사용자의 정보를 수정할 수 없습니다");
        }

        User updateUser = new User(request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email"));
        log.debug("Update user : {}", updateUser);
        user.update(updateUser);

        return jspView("redirect:/");
    }
}
