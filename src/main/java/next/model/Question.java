package next.model;

import core.exception.CannotDeleteException;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Question {
    private long questionId;
    private String writer;
    private String title;
    private String contents;
    private Date createdDate;
    private int countOfAnswer;

    public Question(long questionId, String writer) {
        this.questionId = questionId;
        this.writer = writer;
    }

    public Question(String writer, String title, String contents) {
        this(0, writer, title, contents, new Date(), 0);
    }

    public Question(long questionId, String writer, String title, String contents, Date createdDate, int countOfAnswer) {
        this.questionId = questionId;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createdDate = createdDate;
        this.countOfAnswer = countOfAnswer;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public long getCreatedDateToTime() {
        return createdDate.getTime();
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getCountOfAnswer() {
        return countOfAnswer;
    }

    public void setCountOfAnswer(int countOfAnswer) {
        this.countOfAnswer = countOfAnswer;
    }

    public boolean isSameUser(User user) {
        return this.writer.equals(user.getUserId());
    }

    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public boolean canDelete(User user, List<Answer> answers) throws CannotDeleteException {
        if(!this.isSameUser(user)) {
            throw new CannotDeleteException("다른 사용자가 쓴 글을 삭제할 수 없습니다");
        }

        for(Answer answer : answers) {
            if(!answer.canDelete(user)) {
                throw new CannotDeleteException("다른 사용자의 답변이 존재하여 질문을 삭제할 수 없습니다");
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return questionId == question.questionId && countOfAnswer == question.countOfAnswer && Objects.equals(writer, question.writer) && Objects.equals(title, question.title) && Objects.equals(contents, question.contents) && Objects.equals(createdDate, question.createdDate);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (questionId ^ (questionId >>> 32));
        return result;
    }
}
