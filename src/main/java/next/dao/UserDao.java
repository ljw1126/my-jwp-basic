package next.dao;

import core.jdbc.DataAccessException;
import core.jdbc.MyJdbcTemplate;
import next.model.User;

import java.util.List;

public class UserDao {
    public void insert(User user) throws DataAccessException {
        MyJdbcTemplate<User> jdbcTemplate = new MyJdbcTemplate();
        jdbcTemplate.update("INSERT INTO USERS VALUES(?, ?, ?, ?)", (ps) -> {
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getEmail());
        });
    }


    public void update(User user) throws DataAccessException {
        MyJdbcTemplate<User> jdbcTemplate = new MyJdbcTemplate();
        jdbcTemplate.update("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?", ps -> {
            ps.setString(1, user.getPassword());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getUserId());
        });
    }


    public List<User> findAll() throws DataAccessException {
        MyJdbcTemplate<User> jdbcTemplate = new MyJdbcTemplate();
        return jdbcTemplate.query("SELECT * FROM USERS",
                (ps) -> {},
                (rs) -> new User(rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")));
    }

    public User findByUserId(String userId) throws DataAccessException {
        MyJdbcTemplate<User> jdbcTemplate = new MyJdbcTemplate();
        return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE userId = ?",
                (ps) -> {ps.setString(1, userId);},
                (rs) -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"))
        );

    }
}
