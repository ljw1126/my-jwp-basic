package next.dao;

import core.di.context.support.AnnotationConfigApplicationContext;
import core.di.context.ApplicationContext;
import core.jdbc.ConnectionManager;
import next.config.MyConfiguration;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoTest {
    private UserDao userDao;

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        ApplicationContext ac = new AnnotationConfigApplicationContext(MyConfiguration.class);
        userDao = ac.getBean(UserDao.class);
    }

    @Test
    void crud() throws Exception {
        // 생성
        User expected = new User("userId", "password", "name", "jinwoo@email.com");

        userDao.insert(expected);

        User actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);

        // 수정
        expected.update(new User("userId", "password2", "name2", "jinwoo@email.com"));
        userDao.update(expected);
        actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).extracting("password", "name")
                .containsExactlyInAnyOrder("password2", "name2");
    }

    @Test
    void findAll() throws Exception {
        List<User> userList = userDao.findAll();
        assertThat(userList).hasSize(4);
    }
}
