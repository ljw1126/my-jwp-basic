package next.dao;

import core.di.factory.ApplicationContext;
import core.jdbc.ConnectionManager;
import next.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JdbcQuestionDaoTest {
    private QuestionDao questionDao;
    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        ApplicationContext ac = new ApplicationContext("core", "next");
        questionDao = ac.getBean(QuestionDao.class);
    }

    @Test
    void findAll() {
        //given
        //when
        List<Question> result = questionDao.findAll();

        //then
        assertThat(result).hasSize(8);
    }

    @Test
    void findById() {
        //given
        long questionId = 1L;

        //when
        Question question = questionDao.findById(questionId);

        //then
        assertThat(question)
                .isNotNull()
                .extracting("questionId", "writer", "title")
                .containsExactly(questionId, "자바지기", "국내에서 Ruby on Rails와 Play가 활성화되기 힘든 이유는 뭘까?");
    }

    @Test
    void insert() {
        // given
        Question given = new Question("jinwoo", "제목없음", "내용없음");

        // when
        Question result = questionDao.insert(given);

        // then
        assertThat(result)
                .isNotNull()
                .extracting("writer", "title", "contents")
                .containsExactly("jinwoo", "제목없음", "내용없음");
    }

    @Test
    void updateCountOfAnswer() {
        //given
        Question given = new Question("jinwoo", "제목없음", "내용없음");
        Question saverdQuestion = questionDao.insert(given);

        //when
        questionDao.updateCountOfAnswer(saverdQuestion.getQuestionId());

        //then
        assertThat(questionDao.findById(saverdQuestion.getQuestionId()).getCountOfAnswer())
                .isEqualTo(1);
    }

    @Test
    void update() {
        //given
        long questionId = 7L;
        Question question = questionDao.findById(questionId);

        //when
        question.setTitle("제목없음");
        question.setContents("내용없음");
        questionDao.update(question);

        //then
        assertThat(questionDao.findById(questionId)).extracting("title", "contents")
                .containsExactly("제목없음", "내용없음");
    }

    @Test
    void delete() {
        //given
        long questionId = 7L;

        //when
        questionDao.delete(questionId);

        //then
        assertThat(questionDao.findById(questionId)).isNull();
    }
}
