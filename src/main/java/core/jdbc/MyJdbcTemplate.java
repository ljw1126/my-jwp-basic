package core.jdbc;

import next.model.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class MyJdbcTemplate {

    public void update(User user) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(createQuery());

            setValues(user, ps);

            ps.executeUpdate();
        } finally {
            if(ps != null) ps.close();
            if(con != null) con.close();
        }
    }

    abstract protected void setValues(User user, PreparedStatement ps) throws SQLException;

    abstract protected String createQuery();
}
