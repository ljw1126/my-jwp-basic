package next.dao;

import core.di.factory.ApplicationContext;
import core.di.factory.BeanFactory;
import core.di.factory.ClasspathBeanDefinitionScanner;
import core.jdbc.ConnectionManager;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.predicate;

public class UserDaoTest {
    private UserDao userDao;

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        ApplicationContext ac = new ApplicationContext("core", "next");
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
