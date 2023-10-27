package next.dao;

import core.jdbc.ConnectionManager;
import next.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionDaoTest {
    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    void findAll() {
        //given
        //when
        QuestionDao questionDao = QuestionDao.getInstance();
        List<Question> result = questionDao.findAll();

        //then
        assertThat(result).hasSize(8);
    }

    @Test
    void findById() {
        //given
        long questionId = 1L;

        //when
        QuestionDao questionDao = QuestionDao.getInstance();
        Question question = questionDao.findById(questionId);

        //then
        assertThat(question)
                .isNotNull()
                .extracting("questionId", "writer", "title")
                .containsExactly(questionId, "자바지기", "국내에서 Ruby on Rails와 Play가 활성화되기 힘든 이유는 뭘까?");
    }
}
