package next.dao;

import core.jdbc.MyJdbcTemplate;
import core.jdbc.RowMapper;
import next.model.Question;

import java.util.List;

public class QuestionDao {

    // 전체 목록 (이때 content = null)
    public List<Question> findAll() {
        MyJdbcTemplate jdbcTemplate = new MyJdbcTemplate();
        String sql = "SELECT questionId, writer, title, createdDate, countOfAnswer " +
                "FROM QUESTIONS ORDER BY questionId desc";


        RowMapper<Question> rm = (rs) ->
             new Question(
                    rs.getLong("questionId"),
                    rs.getString("writer"),
                    rs.getString("title"),
                    null,
                    rs.getTimestamp("createdDate"),
                    rs.getInt("countOfAnswer")
            );

        return jdbcTemplate.query(sql, rm);
    }

    // 상세보기
    public Question findById(long questionId) {
        MyJdbcTemplate<Question> jdbcTemplate = new MyJdbcTemplate();
        String sql = "SELECT questionId, writer, title, contents, createdDate, countOfAnswer " +
                "FROM QUESTIONS WHERE questionId = ?";
        RowMapper<Question> rm = (rs) ->
                new Question(
                        rs.getLong("questionId"),
                        rs.getString("writer"),
                        rs.getString("title"),
                        rs.getString("contents"),
                        rs.getTimestamp("createdDate"),
                        rs.getInt("countOfAnswer")
                );

        return jdbcTemplate.queryForObject(sql, rm, questionId);
    }
}
