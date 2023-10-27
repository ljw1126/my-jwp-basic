package next.dao;

import core.jdbc.ConnectionManager;
import next.model.Answer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerDaoTest {

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    void insert() {
        //given
        Date now = new Date();
        Answer givenAnswer = new Answer(0, "jinwoo", "내용없음체", now, Long.valueOf(1));

        //when
        AnswerDao answerDao = AnswerDao.getInstance();
        Answer result = answerDao.insert(givenAnswer);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getTimeFromCreateDate()).isEqualTo(now.getTime());
        assertThat(result).extracting("writer", "contents", "questionId")
                .containsExactlyInAnyOrder("jinwoo", "내용없음체", Long.valueOf(1));
    }

    @DisplayName("answerId 값으로 답변 Answer를 가져온다")
    @Test
    void findById() {
        //given
        long answerId = 1L;

        //when
        AnswerDao answerDao = AnswerDao.getInstance();
        Answer result = answerDao.findById(answerId);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getAnswerId()).isEqualTo(answerId);
    }

    @Test
    void findAllByQuestionId() {
        //given
        long questionId = 7L;

        //when
        AnswerDao answerDao = AnswerDao.getInstance();
        List<Answer> result = answerDao.findAllByQuestionId(questionId);

        //then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("questionId")
                .containsOnly(questionId);
    }

    @Test
    void delete() {
        // given
        long answerId = 1L;

        //when
        AnswerDao answerDao = AnswerDao.getInstance();
        Assertions.assertThat(answerDao.findById(answerId)).isNotNull();

        answerDao.delete(answerId);

        assertThat(answerDao.findById(answerId)).isNull();
    }

}
