package next.dao;

import next.model.Question;

import java.util.List;

public interface QuestionDao {
    // 전체 목록 (이때 content = null)
    List<Question> findAll();

    // 상세보기
    Question findById(long questionId);

    Question insert(Question question);

    void updateCountOfAnswer(long questionId);

    void update(Question question);

    void delete(long questionId);
}
