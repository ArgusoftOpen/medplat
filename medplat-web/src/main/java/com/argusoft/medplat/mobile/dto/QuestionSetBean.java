package com.argusoft.medplat.mobile.dto;


import java.util.List;

public class QuestionSetBean {

    private Integer actualId;
    private Integer refId;
    private String refType;
    private String questionSetName;
    private String status;
    private Integer minimumMarks;
    private Integer courseId;
    private Integer questionSetType;
    private Integer quizAtSecond;
    private List<QuestionBankBean> questionBank;

    public Integer getActualId() {
        return actualId;
    }

    public void setActualId(Integer actualId) {
        this.actualId = actualId;
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public String getQuestionSetName() {
        return questionSetName;
    }

    public void setQuestionSetName(String questionSetName) {
        this.questionSetName = questionSetName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getMinimumMarks() {
        return minimumMarks;
    }

    public void setMinimumMarks(Integer minimumMarks) {
        this.minimumMarks = minimumMarks;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getQuestionSetType() {
        return questionSetType;
    }

    public void setQuestionSetType(Integer questionSetType) {
        this.questionSetType = questionSetType;
    }

    public Integer getQuizAtSecond() {
        return quizAtSecond;
    }

    public void setQuizAtSecond(Integer quizAtSecond) {
        this.quizAtSecond = quizAtSecond;
    }

    public List<QuestionBankBean> getQuestionBank() {
        return questionBank;
    }

    public void setQuestionBank(List<QuestionBankBean> questionBank) {
        this.questionBank = questionBank;
    }
}
