package next.dao;

import core.jdbc.MyJdbcTemplate;
import core.jdbc.MySelectJdbcTemplate;
import next.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {
    public void insert(User user) throws SQLException {
        MyJdbcTemplate jdbcTemplate = new MyJdbcTemplate() {
            @Override
            protected void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, user.getUserId());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getName());
                ps.setString(4, user.getEmail());
            }
        };
        jdbcTemplate.update("INSERT INTO USERS VALUES(?, ?, ?, ?)");
    }


    public void update(User user) throws SQLException {
        MyJdbcTemplate jdbcTemplate = new MyJdbcTemplate() {
            @Override
            protected void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getPassword());
                pstmt.setString(2, user.getName());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getUserId());
            }
        };

        jdbcTemplate.update("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?");
    }


    public List<User> findAll() throws SQLException {
        MySelectJdbcTemplate<User> jdbcTemplate = new MySelectJdbcTemplate() {
            @Override
            protected void setValues(PreparedStatement ps) throws SQLException {
            }

            @Override
            protected User mapRow(ResultSet rs) throws SQLException {
                return new User(rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"));
            }
        };
        return jdbcTemplate.query("SELECT * FROM USERS");
    }

    public User findByUserId(String userId) throws SQLException {
        MySelectJdbcTemplate<User> jdbcTemplate = new MySelectJdbcTemplate() {
            @Override
            protected void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, userId);
            }

            @Override
            protected User mapRow(ResultSet rs) throws SQLException {
                return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"));
            }
        };

        return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE userId = ?");
    }
}
