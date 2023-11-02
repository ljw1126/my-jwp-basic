package next.model;

import core.exception.CannotDeleteException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuestionTest {

    public static Question createQuestion(String writer) {
        return new Question(1L, writer, "title", "contents", new Date(), 0);
    }

    public static Question createQuestion(long questionId, String writer) {
        return new Question(questionId, writer, "title", "contents", new Date(), 0);
    }

    public static Answer createAnswer(String writer) {
        return new Answer(99L, writer, "contents", new Date(), 1L);
    }

    @Test
    void canDelete_글쓴이_다르다() {
        User user = new User("tester");
        Question question = createQuestion("jinwoo");

        assertThatThrownBy(() -> question.canDelete(user, new ArrayList<>()))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사용자가 쓴 글을 삭제할 수 없습니다");
    }

    @Test
    void canDelete_글쓴이_같음() throws CannotDeleteException {
        User user = new User("jinwoo");
        Question question = createQuestion("jinwoo");

        assertThat(question.canDelete(user, new ArrayList<>())).isTrue();
    }

    @Test
    void canDelete_같은_사용자_답변() throws CannotDeleteException {
        User user = new User("jinwoo");
        Question question = createQuestion("jinwoo");
        List<Answer> answers = Arrays.asList(createAnswer("jinwoo"));

        assertThat(question.canDelete(user, answers)).isTrue();
    }

    @Test
    void canDelete_다른_사용자_답변() {
        User user = new User("jinwoo");
        Question question = createQuestion("jinwoo");
        List<Answer> answers = Arrays.asList(createAnswer("javajigi"));

        assertThatThrownBy(() -> question.canDelete(user, answers))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사용자의 답변이 존재하여 질문을 삭제할 수 없습니다");
    }
}