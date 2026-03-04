package com.argusoft.sewa.android.app.databean;

public class LmsUserQuizMetaData {

    private Integer moduleId;
    private Integer lessonId;
    private Integer quizRefId;
    private String quizRefType;
    private Integer quizTypeId;
    private Integer quizAttempts;
    private Integer quizAttemptsToPass;
    private Integer latestScore;
    private Integer scoreWhenPassed;
    private Long lastQuizDate;
    private Boolean isLocked;
    private Integer questionSetId;

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public Integer getQuizRefId() {
        return quizRefId;
    }

    public void setQuizRefId(Integer quizRefId) {
        this.quizRefId = quizRefId;
    }

    public String getQuizRefType() {
        return quizRefType;
    }

    public void setQuizRefType(String quizRefType) {
        this.quizRefType = quizRefType;
    }

    public Integer getQuizTypeId() {
        return quizTypeId;
    }

    public void setQuizTypeId(Integer quizTypeId) {
        this.quizTypeId = quizTypeId;
    }

    public Integer getQuizAttempts() {
        return quizAttempts;
    }

    public void setQuizAttempts(Integer quizAttempts) {
        this.quizAttempts = quizAttempts;
    }

    public Integer getQuizAttemptsToPass() {
        return quizAttemptsToPass;
    }

    public void setQuizAttemptsToPass(Integer quizAttemptsToPass) {
        this.quizAttemptsToPass = quizAttemptsToPass;
    }

    public Integer getLatestScore() {
        return latestScore;
    }

    public void setLatestScore(Integer latestScore) {
        this.latestScore = latestScore;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Integer getScoreWhenPassed() {
        return scoreWhenPassed;
    }

    public void setScoreWhenPassed(Integer scoreWhenPassed) {
        this.scoreWhenPassed = scoreWhenPassed;
    }

    public Long getLastQuizDate() {
        return lastQuizDate;
    }

    public void setLastQuizDate(Long lastQuizDate) {
        this.lastQuizDate = lastQuizDate;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }

    public Integer getQuestionSetId() {
        return questionSetId;
    }

    public void setQuestionSetId(Integer questionSetId) {
        this.questionSetId = questionSetId;
    }
}
