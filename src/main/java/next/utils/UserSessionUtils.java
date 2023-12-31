package next.utils;

import next.model.User;

import javax.servlet.http.HttpSession;

public class UserSessionUtils {
    public static final String USER_SESSION_KEY = "user";

    public static User getUser(HttpSession session) {
        Object user = session.getAttribute(USER_SESSION_KEY);
        if(user == null) {
            return null;
        }

        return (User) user;
    }

    public static boolean isLogined(HttpSession session) {
       return getUser(session) != null;
    }

    public static boolean isSameUser(HttpSession session, User user) {
        if(!isLogined(session) || user == null) return false;

        return user.isSameUser(getUser(session));
    }

}
