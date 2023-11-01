package next.service;

import core.exception.CannotDeleteException;
import next.dao.MockAnswerDao;
import next.dao.MockQuestionDao;
import next.model.Question;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class QuestionServiceTest {
    private QuestionService questionService;
    private MockQuestionDao questionDao;
    private MockAnswerDao answerDao;

    @BeforeEach
    void setUp() {
        questionDao = new MockQuestionDao();
        answerDao = new MockAnswerDao();
        questionService = QuestionService.getInstance(questionDao, answerDao);
    }

    @Test
    void deleteQuestion_없는질문() {
        assertThatThrownBy(() -> questionService.delete(1L, new User("userId")))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("존재하지 않는 글입니다");
    }

    @Test
    void deleteQuestion_다른_사용자() {
        Question question = new Question(1L, "jinwoo");
        questionDao.insert(question);
        assertThatThrownBy(() -> questionService.delete(1L, new User("userId")))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사용자가 쓴 글을 삭제할 수 없습니다");
    }


    @Test
    void deleteQuestion_같은_사용자_답변없음() throws Exception {
        Question question = new Question(1L, "jinwoo");
        questionDao.insert(question);
        questionService.delete(1L, new User("jinwoo"));
    }
}
