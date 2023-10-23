package core.jdbc;

import next.model.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class MyJdbcTemplate {

    public void update(String query) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(query);

            setValues(ps);

            ps.executeUpdate();
        } finally {
            if(ps != null) ps.close();
            if(con != null) con.close();
        }
    }

    abstract protected void setValues(PreparedStatement ps) throws SQLException;
}
