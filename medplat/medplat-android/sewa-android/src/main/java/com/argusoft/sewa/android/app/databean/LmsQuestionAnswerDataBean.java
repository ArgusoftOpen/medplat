package com.argusoft.sewa.android.app.databean;

public class LmsQuestionAnswerDataBean {

    private Integer queId;
    private String queTitle;
    private String answer;
    private String correctAnswer;

    public Integer getQueId() {
        return queId;
    }

    public void setQueId(Integer queId) {
        this.queId = queId;
    }

    public String getQueTitle() {
        return queTitle;
    }

    public void setQueTitle(String queTitle) {
        this.queTitle = queTitle;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String toString() {
        return "LmsQuestionAnswerDataBean{" +
                "queId=" + queId +
                ", queTitle='" + queTitle + '\'' +
                ", answer='" + answer + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                '}';
    }
}
