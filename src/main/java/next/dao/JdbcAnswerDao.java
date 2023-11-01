package next.dao;

import core.jdbc.KeyHolder;
import core.jdbc.MyJdbcTemplate;
import core.jdbc.PreparedStatementCreator;
import core.jdbc.RowMapper;
import next.model.Answer;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

public class AnswerDao {

    private static AnswerDao answerDao;
    private MyJdbcTemplate jdbcTemplate = MyJdbcTemplate.getInstance();

    private AnswerDao() {}

    public static AnswerDao getInstance() {
        if(answerDao == null) {
            answerDao = new AnswerDao();
        }

        return answerDao;
    }

    public Answer insert(Answer answer) {
        String sql = "INSERT INTO ANSWERS (writer, contents, createdDate, questionId) VALUES(?, ?, ?, ?)";
        PreparedStatementCreator psc = (con) -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, answer.getWriter());
            ps.setString(2, answer.getContents());
            ps.setTimestamp(3, new Timestamp(answer.getTimeFromCreateDate()));
            ps.setLong(4, answer.getQuestionId());
            return ps;
        };

        KeyHolder keyHolder = new KeyHolder();
        jdbcTemplate.update(psc, keyHolder);
        return findById(keyHolder.getId());
    }

    public Answer findById(long answerId) {
        String sql = "SELECT answerId, writer, contents, createdDate, questionId FROM ANSWERS WHERE answerId = ?";
        RowMapper<Answer> rowMapper = (rs) -> new Answer(
                            rs.getLong("answerId"),
                            rs.getString("writer"),
                            rs.getString("contents"),
                            rs.getTimestamp("createdDate"),
                            rs.getLong("questionId"));

        return jdbcTemplate.queryForObject(sql, rowMapper, answerId);
    }

    public List<Answer> findAllByQuestionId(long questionId) {
        String sql = "SELECT answerId, writer, contents, createdDate, questionId FROM ANSWERS WHERE questionId = ?";
        RowMapper<Answer> rowMapper = (rs) -> new Answer(
                rs.getLong("answerId"),
                rs.getString("writer"),
                rs.getString("contents"),
                rs.getTimestamp("createdDate"),
                rs.getLong("questionId"));

        return jdbcTemplate.query(sql, rowMapper, questionId);
    }

    public void delete(long answerId) {
        String sql = "DELETE FROM ANSWERS WHERE answerId = ?";
        jdbcTemplate.update(sql, answerId);
    }

    public int count(long questionId) {
        String sql = "SELECT count(*) as cnt FROM ANSWERS WHERE questionId = ?";
        RowMapper<Integer> rowMapper = (rs) -> rs.getInt(1);
        return jdbcTemplate.queryForObject(sql, rowMapper, questionId);
    }
}
