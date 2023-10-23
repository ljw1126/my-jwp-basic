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

    public void update(String query, Object... parameters) {
        update(query, createPreparedStatementSetter(parameters));
    }

    private PreparedStatementSetter createPreparedStatementSetter(Object... parameters) {
        return (ps) -> {
            for(int i = 1; i <= parameters.length; i++) {
                ps.setString(i, String.valueOf(parameters[i - 1]));
            }
        };
    }

    public T queryForObject(String query, RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) throws DataAccessException {
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

    public T queryForObject(String query, RowMapper<T> rowMapper, Object... parameters) throws DataAccessException {
        return queryForObject(query, rowMapper, createPreparedStatementSetter(parameters));
    }

    public List<T> query(String query, RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) throws DataAccessException {
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

    public List<T> query(String query, RowMapper<T> rowMapper, Object... parameters) throws DataAccessException {
        return query(query, rowMapper, createPreparedStatementSetter(parameters));
    }
}
