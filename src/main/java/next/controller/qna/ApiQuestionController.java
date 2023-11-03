package next.controller.qna;

import core.annotation.Controller;
import core.annotation.Inject;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import core.exception.CannotDeleteException;
import core.exception.DataAccessException;
import core.mvc.ModelAndView;
import core.nmvc.AbstractNewController;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Result;
import next.model.User;
import next.service.QuestionService;
import next.utils.UserSessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ApiQuestionController extends AbstractNewController {
    private final QuestionService questionService;
    private final AnswerDao answerDao;
    private final QuestionDao questionDao;

    @Inject
    public ApiQuestionController(QuestionService questionService, AnswerDao answerDao, QuestionDao questionDao) {
        this.questionService = questionService;
        this.answerDao = answerDao;
        this.questionDao = questionDao;
    }

    @RequestMapping(value = "/api/qna/list", method = RequestMethod.GET)
    public ModelAndView questionList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return jsonView().addAttribute("data", questionDao.findAll())
                .addAttribute("status", Result.ok());
    }

    @RequestMapping(value = "/api/qna/deleteQuestion", method = RequestMethod.GET)
    public ModelAndView deleteQuestion(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!UserSessionUtils.isLogined(request.getSession())) {
            return jsonView().addAttribute("data", Result.fail("로그인이 필요합니다"));
        }

        try {
            long questionId = Long.parseLong(request.getParameter("questionId"));
            questionService.delete(questionId, UserSessionUtils.getUser(request.getSession()));

            return jsonView().addAttribute("data", Result.ok());
        } catch(CannotDeleteException e) {
            return jsonView().addAttribute("data", Result.fail(e.getMessage()));
        }
    }

    @RequestMapping(value = "/api/qna/addAnswer", method = RequestMethod.GET)
    public ModelAndView addAnswer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(!UserSessionUtils.isLogined(request.getSession())) {
            return jsonView().addAttribute("result", Result.fail("Login is required"));
        }

        User user = UserSessionUtils.getUser(request.getSession());
        Answer answer = new Answer(user.getUserId(),
                request.getParameter("contents"),
                Long.parseLong(request.getParameter("questionId")));

        Answer savedAnswer = answerDao.insert(answer);
        questionDao.updateCountOfAnswer(savedAnswer.getQuestionId());
        int count = answerDao.count(savedAnswer.getQuestionId());

        return jsonView()
                .addAttribute("result", Result.ok())
                .addAttribute("data", savedAnswer)
                .addAttribute("count", count);
    }

    @RequestMapping(value = "/api/qna/deleteAnswer", method = RequestMethod.GET)
    public ModelAndView deleteAnswer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long answerId = Long.parseLong(request.getParameter("answerId"));

        ModelAndView modelAndView = jsonView();

        try {
            answerDao.delete(answerId);
            modelAndView.addAttribute("data", Result.ok());
        } catch (DataAccessException e) {
            modelAndView.addAttribute("data", Result.fail("error message"));
        }

        return modelAndView;
    }

}
