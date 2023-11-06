package next.dao;

import core.annotation.Inject;
import core.annotation.Repository;
import core.exception.DataAccessException;
import core.jdbc.MyJdbcTemplate;
import next.model.User;

import java.util.List;

@Repository
public class JdbcUserDao implements UserDao {

    @Inject
    private MyJdbcTemplate jdbcTemplate;

    public JdbcUserDao() {
    }

    @Override
    public void insert(User user) throws DataAccessException {
        String sql = "INSERT INTO USERS(userId, password, name, email) VALUES(?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }


    @Override
    public void update(User user) throws DataAccessException {
        String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }


    @Override
    public List<User> findAll() throws DataAccessException {
        return jdbcTemplate.query("SELECT * FROM USERS",
                (rs) -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email")));
    };

    @Override
    public User findByUserId(String userId) throws DataAccessException {
        return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE userId = ?",
                (rs) -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email")),
                userId
        );
    }
}
