package com.argusoft.sewa.android.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class LmsViewedMediaBean extends BaseEntity {

    @DatabaseField
    private Integer courseId;
    @DatabaseField
    private Integer moduleId;
    @DatabaseField
    private Integer lessonId;
    @DatabaseField
    private Date startDate;
    @DatabaseField
    private Date endDate;
    @DatabaseField
    private Boolean isCompleted;
    @DatabaseField
    private Boolean isViewed;
    @DatabaseField
    private Integer lastPausedOn;
    @DatabaseField
    private Integer userFeedback;
    @DatabaseField
    private String sessions;
    @DatabaseField
    private String topics;
    @DatabaseField
    private String questionSet;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public Boolean getViewed() {
        return isViewed;
    }

    public void setViewed(Boolean viewed) {
        isViewed = viewed;
    }

    public Integer getLastPausedOn() {
        return lastPausedOn;
    }

    public void setLastPausedOn(Integer lastPausedOn) {
        this.lastPausedOn = lastPausedOn;
    }

    public Integer getUserFeedback() {
        return userFeedback;
    }

    public void setUserFeedback(Integer userFeedback) {
        this.userFeedback = userFeedback;
    }

    public String getSessions() {
        return sessions;
    }

    public void setSessions(String sessions) {
        this.sessions = sessions;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(String questionSet) {
        this.questionSet = questionSet;
    }

    @Override
    public String toString() {
        return "LmsViewedMediaBean{" +
                "courseId=" + courseId +
                ", moduleId=" + moduleId +
                ", lessonId=" + lessonId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", isCompleted=" + isCompleted +
                ", isViewed=" + isViewed +
                ", lastPausedOn=" + lastPausedOn +
                ", userFeedback=" + userFeedback +
                ", sessions='" + sessions + '\'' +
                '}';
    }
}
