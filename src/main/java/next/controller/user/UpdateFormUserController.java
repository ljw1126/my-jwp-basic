package next.controller.user;

import core.db.DataBase;
import core.mvc.Controller;
import next.model.User;
import next.utils.UserSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateFormUserController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(UpdateFormUserController.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userId = request.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if(!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalArgumentException("다른 사용자의 정보를 수정할 수 없습니다");
        }

        request.setAttribute("user", user);
        return "/user/updateForm.jsp";
    }
}
