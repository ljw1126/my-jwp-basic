package next.dao;

import next.model.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MockQuestionDao implements QuestionDao {
    private Map<Long, Question> questions = new HashMap<>();

    @Override
    public List<Question> findAll() {
        return questions.values().stream().collect(Collectors.toList());
    }

    @Override
    public Question findById(long questionId) {
        return questions.get(questionId);
    }

    @Override
    public Question insert(Question question) {
        return questions.put(question.getQuestionId(), question);
    }

    @Override
    public void updateCountOfAnswer(long questionId) {
        Question question = findById(questionId);
        question.setCountOfAnswer(question.getCountOfAnswer() + 1);
    }

    @Override
    public void update(Question question) {
        findById(question.getQuestionId()).update(question.getTitle(), question.getContents());
    }

    @Override
    public void delete(long questionId) {
        questions.remove(questionId);
    }
}
