package core.jdbc;

import core.annotation.Component;
import core.annotation.Inject;
import core.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MyJdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(MyJdbcTemplate.class);

    private DataSource dataSource;

    public MyJdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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

    // AnswerDao에서 PreparedStatementCreator 구현해서 콜백 함수 실행
    public void update(PreparedStatementCreator psc, KeyHolder holder) throws DataAccessException {
        try (Connection con = ConnectionManager.getConnection()){
            PreparedStatement ps = psc.createPreparedStatement(con);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys(); // 자동 생성키를 찾아오거나, null 리턴
            if(rs.next()) {
                holder.setId(rs.getLong(1)); // 1번 인덱스는 answerId, AnswerDao에서 사용
            }

            rs.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }


    private PreparedStatementSetter createPreparedStatementSetter(Object... parameters) {
        return (ps) -> {
            for(int i = 1; i <= parameters.length; i++) {
                ps.setString(i, String.valueOf(parameters[i - 1]));
            }
        };
    }

    public <T> T queryForObject(String query, RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) throws DataAccessException {
        List<T> list = query(query, rowMapper, preparedStatementSetter);
        if(list.isEmpty()) return null;

        return list.get(0);
    }

    public <T> T queryForObject(String query, RowMapper<T> rowMapper, Object... parameters) throws DataAccessException {
        return queryForObject(query, rowMapper, createPreparedStatementSetter(parameters));
    }

    public <T> List<T> query(String query, RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) throws DataAccessException {
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

    public <T> List<T> query(String query, RowMapper<T> rowMapper, Object... parameters) throws DataAccessException {
        return query(query, rowMapper, createPreparedStatementSetter(parameters));
    }
}
