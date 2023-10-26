package next.controller.user;

import core.db.DataBase;
import core.mvc.Controller;
import core.mvc.JspView;
import core.mvc.View;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProfileController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    @Override
    public View execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userId = request.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if(user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다");
        }

        request.setAttribute("user", user);
        return new JspView("/user/profile.jsp");
    }
}
