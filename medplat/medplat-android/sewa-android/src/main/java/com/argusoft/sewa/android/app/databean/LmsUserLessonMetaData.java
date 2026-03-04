package com.argusoft.sewa.android.app.databean;

import java.util.List;

public class LmsUserLessonMetaData {

    private Integer moduleId;
    private Integer lessonId;
    private Long mediaId;
    private Long startDate;
    private Long endDate;
    private Boolean isCompleted;
    private Integer lastPausedOn;
    private Integer userFeedback;
    private List<LmsUserLessonSessionMetaData> sessions;

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

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
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

    public List<LmsUserLessonSessionMetaData> getSessions() {
        return sessions;
    }

    public void setSessions(List<LmsUserLessonSessionMetaData> sessions) {
        this.sessions = sessions;
    }

    @Override
    public String toString() {
        return "LmsUserLessonMetaData{" +
                "moduleId=" + moduleId +
                ", lessonId=" + lessonId +
                ", mediaId=" + mediaId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", isCompleted=" + isCompleted +
                ", lastPausedOn=" + lastPausedOn +
                ", userFeedback=" + userFeedback +
                ", sessions=" + sessions +
                '}';
    }
}
