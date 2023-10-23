package core.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class MySelectJdbcTemplate<T> {
    private static final Logger log = LoggerFactory.getLogger(MySelectJdbcTemplate.class);
    public List<T> query(String query) throws SQLException {
        List<T> result = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(query);

            rs = ps.executeQuery();

            while(rs.next()) {
                result.add(mapRow(rs));
            }

            return result;
        } finally {
            if(rs != null) rs.close();
            if(ps != null) ps.close();
            if(con != null) con.close();
        }
    }

    public T queryForObject(String query) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(query);

            setValues(ps);

            rs = ps.executeQuery();

            T result = null;
            while(rs.next()) {
                result = mapRow(rs);
            }

            return result;
        } finally {
            if(rs != null) rs.close();
            if(ps != null) ps.close();
            if(con != null) con.close();
        }
    }

    abstract protected void setValues(PreparedStatement ps) throws SQLException;

    abstract protected T mapRow(ResultSet rs) throws SQLException;
}
