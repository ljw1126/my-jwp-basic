package core.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyJdbcTemplate<T> {
    private static final Logger log = LoggerFactory.getLogger(MyJdbcTemplate.class);
    public void update(String query, PreparedStatementSetter preparedStatementSetter) throws DataAccessException {
        try (
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
        ){
            preparedStatementSetter.values(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public List<T> query(String query, PreparedStatementSetter preparedStatementSetter, RowMapper<T> rowMapper) throws DataAccessException {
        List<T> result = new ArrayList<>();
        ResultSet rs = null;

        try (
             Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
        ) {
            preparedStatementSetter.values(ps);
            rs = ps.executeQuery();

            while(rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }

            return result;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    public T queryForObject(String query, PreparedStatementSetter preparedStatementSetter, RowMapper<T> rowMapper) throws DataAccessException {
        ResultSet rs = null;

        try (
             Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
        ){
           preparedStatementSetter.values(ps);
           rs = ps.executeQuery();

            T result = null;
            while(rs.next()) {
                result = rowMapper.mapRow(rs);
            }

            return result;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
