package next.dao;

import core.jdbc.KeyHolder;
import core.jdbc.MyJdbcTemplate;
import core.jdbc.PreparedStatementCreator;
import core.jdbc.RowMapper;
import next.model.Question;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

public class QuestionDao {
    private static QuestionDao questionDao;
    private MyJdbcTemplate jdbcTemplate = MyJdbcTemplate.getInstance();

    private QuestionDao() {
    }

    public static QuestionDao getInstance() {
        if(questionDao == null) {
            questionDao = new QuestionDao();
        }

        return questionDao;
    }


    // 전체 목록 (이때 content = null)
    public List<Question> findAll() {
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

    public Question insert(Question question) {
        String sql = "INSERT INTO QUESTIONS (writer, title, contents, createdDate, countOfAnswer) " +
                "VALUES(?, ?, ?, ?, ?)";

        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, question.getWriter());
            ps.setString(2, question.getTitle());
            ps.setString(3, question.getContents());
            ps.setTimestamp(4, new Timestamp(question.getCreatedDateToTime()));
            ps.setInt(5, 0);
            return ps;
        };

        KeyHolder keyHolder = new KeyHolder();
        jdbcTemplate.update(psc, keyHolder);
        return findById(keyHolder.getId());
    }

    public void updateCountOfAnswer(long questionId) {
        String sql = "UPDATE QUESTIONS SET countOfAnswer = countOfAnswer + 1 WHERE questionId = ?";
        jdbcTemplate.update(sql, questionId);
    }
}
