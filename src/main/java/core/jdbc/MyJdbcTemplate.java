package core.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class MyJdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(MyJdbcTemplate.class);
    public void update(String query){
        try (
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
        ){
            setValues(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    abstract protected void setValues(PreparedStatement ps) throws SQLException;
}
