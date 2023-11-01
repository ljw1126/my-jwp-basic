package next.service;

import com.google.common.collect.Lists;
import core.exception.CannotDeleteException;
import next.dao.MockAnswerDao;
import next.dao.MockQuestionDao;
import next.model.Question;
import next.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    void deleteQuestion_다른_사용자() {
        Question question = new Question(1L, "jinwoo");
        when(questionDao.findById(1L)).thenReturn(question);

        assertThatThrownBy(() -> questionService.delete(1L, new User("userId")))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사용자가 쓴 글을 삭제할 수 없습니다");
    }


    @Test
    void deleteQuestion_같은_사용자_답변없음() throws Exception {
        Question question = new Question(1L, "jinwoo");
        when(questionDao.findById(1L)).thenReturn(question);
        when(answerDao.findAllByQuestionId(1L)).thenReturn(Lists.newArrayList());

        questionService.delete(1L, new User("jinwoo"));
    }
}
