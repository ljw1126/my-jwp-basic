package next.service;

import core.exception.CannotDeleteException;
import next.dao.MockAnswerDao;
import next.dao.MockQuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    private MockQuestionDao questionDao;

    @Mock
    private MockAnswerDao answerDao;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void deleteQuestion_없는질문() {
        when(questionDao.findById(1L)).thenReturn(null);

        assertThatThrownBy(() -> questionService.delete(1L, new User("userId")))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("존재하지 않는 글입니다");
    }

    @Test
    void deleteQuestion_삭제할수없음() {
        Question question = new Question(1L, "jinwoo") {
            @Override
            public boolean canDelete(User user, List<Answer> answers) throws CannotDeleteException {
                throw new CannotDeleteException("다른 사용자가 쓴 글을 삭제할 수 없습니다");
            }
        };
        when(questionDao.findById(1L)).thenReturn(question);

        assertThatThrownBy(() -> questionService.delete(1L, new User("javajigi")))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사용자가 쓴 글을 삭제할 수 없습니다");
    }


    @Test
    void deleteQuestion_삭제가능() throws Exception {
        Question question = new Question(1L, "jinwoo") {
            @Override
            public boolean canDelete(User user, List<Answer> answers) throws CannotDeleteException {
                return true;
            }
        };

        when(questionDao.findById(1L)).thenReturn(question);

        questionService.delete(1L, new User("jinwoo"));

        verify(questionDao).delete(question.getQuestionId());
    }
}
