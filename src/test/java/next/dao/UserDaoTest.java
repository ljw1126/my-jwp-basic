package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoTest {

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    void crud() throws Exception {
        // 생성
        User expected = new User("userId", "password", "name", "jinwoo@email.com");

        UserDao dao = UserDao.getInstance();
        dao.insert(expected);

        User actual = dao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);

        // 수정
        expected.update(new User("userId", "password2", "name2", "jinwoo@email.com"));
        dao.update(expected);
        actual = dao.findByUserId(expected.getUserId());
        assertThat(actual).extracting("password", "name")
                .containsExactlyInAnyOrder("password2", "name2");
    }

    @Test
    void findAll() throws Exception {
        UserDao dao = UserDao.getInstance();
        List<User> userList = dao.findAll();
        assertThat(userList).hasSize(1);
    }
}
