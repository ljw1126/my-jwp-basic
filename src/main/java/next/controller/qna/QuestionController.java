package next.controller.qna;

import core.annotation.Controller;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import core.exception.CannotDeleteException;
import core.mvc.ModelAndView;
import core.nmvc.AbstractNewController;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Question;
import next.model.User;
import next.service.QuestionService;
import next.utils.UserSessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class QuestionController extends AbstractNewController {
    private final QuestionService questionService;
    private final QuestionDao questionDao;
    private final AnswerDao answerDao;

    public QuestionController(QuestionService questionService, QuestionDao questionDao, AnswerDao answerDao) {
        this.questionService = questionService;
        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }

    @RequestMapping(value = "/qna/show", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long questionId = Long.parseLong(request.getParameter("questionId"));

        return jspView("/qna/show.jsp")
                .addAttribute("question", questionDao.findById(questionId))
                .addAttribute("answerList", answerDao.findAllByQuestionId(questionId));
    }

    @RequestMapping(value = "/qna/form", method = RequestMethod.GET)
    public ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/user/loginForm");
        }

        return jspView("/qna/form.jsp");
    }

    @RequestMapping(value = "/qna/create", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/user/loginForm");
        }

        User user = UserSessionUtils.getUser(request.getSession());
        Question question = new Question(user.getUserId(), request.getParameter("title"), request.getParameter("contents"));
        questionDao.insert(question);

        return jspView("redirect:/");
    }

    @RequestMapping(value = "/qna/updateForm", method = RequestMethod.GET)
    public ModelAndView updateForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/user/loginForm");
        }

        long questionId = Long.parseLong(request.getParameter("questionId"));
        Question question = questionDao.findById(questionId);
        if(!question.isSameUser(UserSessionUtils.getUser(request.getSession()))) {
            throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다");
        }

        return jspView("/qna/updateForm.jsp")
                .addAttribute("question", question);
    }

    @RequestMapping(value = "/qna/update", method = RequestMethod.POST)
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/login/loginForm");
        }

        long questionId = Long.parseLong(request.getParameter("questionId"));
        Question question = questionDao.findById(questionId);
        if(!question.isSameUser(UserSessionUtils.getUser(request.getSession()))) {
            throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다");
        }

        question.update(request.getParameter("title"), request.getParameter("contents"));
        questionDao.update(question);

        return jspView("redirect:/");
    }

    @RequestMapping(value = "/qna/delete", method = RequestMethod.DELETE)
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/login/loginForm");
        }

        long questionId = Long.parseLong(request.getParameter("questionId"));
        try {
            questionService.delete(questionId, UserSessionUtils.getUser(request.getSession()));
            return jspView("redirect:/");
        } catch (CannotDeleteException e) {
            return jspView("/qna/show.jsp")
                    .addAttribute("question", questionService.findById(questionId))
                    .addAttribute("answerList", questionService.findAllByQuestionId(questionId))
                    .addAttribute("errorMessage", e.getMessage());
        }
    }
}
