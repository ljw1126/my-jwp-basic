package next.dao;

import core.jdbc.DataAccessException;
import core.jdbc.MyJdbcTemplate;
import next.model.User;

import java.util.List;

public class UserDao {
    public void insert(User user) throws DataAccessException {
        MyJdbcTemplate<User> jdbcTemplate = new MyJdbcTemplate();
        String sql = "INSERT INTO USERS VALUES(?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }


    public void update(User user) throws DataAccessException {
        MyJdbcTemplate<User> jdbcTemplate = new MyJdbcTemplate();
        String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }


    public List<User> findAll() throws DataAccessException {
        MyJdbcTemplate<User> jdbcTemplate = new MyJdbcTemplate();
        return jdbcTemplate.query("SELECT * FROM USERS",
                (rs) -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email")));
    };

    public User findByUserId(String userId) throws DataAccessException {
        MyJdbcTemplate<User> jdbcTemplate = new MyJdbcTemplate();
        return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE userId = ?",
                (rs) -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email")),
                userId
        );
    }
}
