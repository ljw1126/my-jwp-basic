package next.service;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import core.exception.CannotDeleteException;
import next.model.Answer;
import next.model.Question;
import next.model.User;

import java.util.List;

public class QuestionService {
    private static QuestionService questionService = new QuestionService();
    private QuestionDao questionDao = QuestionDao.getInstance();
    private AnswerDao answerDao = AnswerDao.getInstance();

    public static QuestionService getInstance() {
        return questionService;
    }

    public void delete(long questionId, User user) throws CannotDeleteException{
        Question question = questionDao.findById(questionId);
        if(question == null) {
            throw new CannotDeleteException("존재하지 않는 글입니다");
        }

        if(!question.isSameUser(user)) {
            throw new CannotDeleteException("다른 사용자가 쓴 글을 삭제할 수 없습니다");
        }

        List<Answer> answerList = answerDao.findAllByQuestionId(questionId);
        boolean cantDelete = answerList.stream().map(Answer::getWriter).anyMatch(writer -> !writer.equals(question.getWriter()));
        if(cantDelete) {
            throw new CannotDeleteException("다른 사용자의 답변이 존재하여 질문을 삭제할 수 없습니다");
        }

        questionDao.delete(questionId);
    }

    public Question findById(long questionId) {
        return questionDao.findById(questionId);
    }

    public List<Answer> findAllByQuestionId(long questionId) {
        return answerDao.findAllByQuestionId(questionId);
    }
}