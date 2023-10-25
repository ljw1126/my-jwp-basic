package next.model;

import java.util.Date;
import java.util.Objects;

public class Answer {
    private long answerId;
    private String writer;
    private String contents;
    private Date createdDate;
    private long questionId;

    public Answer(String writer, String contents, long questionId) {
        this(0, writer, contents, new Date(), questionId);
    }

    public Answer(long answerId, String writer, String contents, Date createdDate, long questionId) {
        this.answerId = answerId;
        this.writer = writer;
        this.contents = contents;
        this.createdDate = createdDate;
        this.questionId = questionId;
    }

    public long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public long getTimeFromCreateDate() {
        return createdDate.getTime();
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return answerId == answer.answerId && questionId == answer.questionId && Objects.equals(writer, answer.writer) && Objects.equals(contents, answer.contents) && Objects.equals(createdDate, answer.createdDate);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (answerId ^ (answerId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answerId=" + answerId +
                ", writer='" + writer + '\'' +
                ", contents='" + contents + '\'' +
                ", createdDate=" + createdDate +
                ", questionId=" + questionId +
                '}';
    }
}
