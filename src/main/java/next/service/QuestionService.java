package next.service;

import core.annotation.Inject;
import core.annotation.Service;
import next.dao.AnswerDao;
import next.dao.JdbcAnswerDao;
import next.dao.JdbcQuestionDao;
import core.exception.CannotDeleteException;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.User;

import java.util.List;

@Service
public class QuestionService {
    private final QuestionDao questionDao;
    private final AnswerDao answerDao;

    @Inject
    public QuestionService(QuestionDao questionDao, AnswerDao answerDao) {
        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }

    public void delete(long questionId, User user) throws CannotDeleteException{
        Question question = questionDao.findById(questionId);
        if(question == null) {
            throw new CannotDeleteException("존재하지 않는 글입니다");
        }

        List<Answer> answers = answerDao.findAllByQuestionId(questionId);
        if(question.canDelete(user, answers)) {
            questionDao.delete(questionId);
        }
    }

    public Question findById(long questionId) {
        return questionDao.findById(questionId);
    }

    public List<Answer> findAllByQuestionId(long questionId) {
        return answerDao.findAllByQuestionId(questionId);
    }
}
