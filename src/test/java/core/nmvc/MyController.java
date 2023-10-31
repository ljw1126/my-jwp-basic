package core.nmvc;

import core.annotation.Controller;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MyController {
    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @RequestMapping("/user")
    public ModelAndView list() {
        logger.debug("user list");
        return new ModelAndView(new JspView("/user/list.jsp"));
    }

    @RequestMapping(value = "/user/show", method = RequestMethod.GET)
    public ModelAndView show() {
        logger.debug("/user/show");
        return new ModelAndView(new JspView("/user/show.jsp"));
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ModelAndView create() {
        logger.debug("user create");
        return new ModelAndView(new JspView("redirect:/user"));
    }

    @RequestMapping("/user/findUserId")
    public ModelAndView findUserId(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("findUserId");
        return null;
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ModelAndView save() {
        logger.debug("user save");
        return null;
    }
}
