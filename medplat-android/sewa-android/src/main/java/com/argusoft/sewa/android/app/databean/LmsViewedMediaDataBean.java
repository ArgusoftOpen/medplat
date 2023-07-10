package com.argusoft.sewa.android.app.databean;

import com.argusoft.sewa.android.app.model.LmsViewedMediaBean;
import com.argusoft.sewa.android.app.util.MyComparatorUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class LmsViewedMediaDataBean {

    private Integer courseId;
    private Integer moduleId;
    private Integer lessonId;
    private Date startDate;
    private Date endDate;
    private Boolean isCompleted;
    private Boolean isViewed;
    private Integer lastPausedOn;
    private Integer userFeedback;
    private String sessions;
    private List<LmsTopicDataBean> topics;
    private List<LmsQuestionSetDataBean> questionSet;

    public LmsViewedMediaDataBean(LmsViewedMediaBean lmsViewedMediaBean) {
        this.courseId = lmsViewedMediaBean.getCourseId();
        this.moduleId = lmsViewedMediaBean.getModuleId();
        this.lessonId = lmsViewedMediaBean.getLessonId();
        this.startDate = lmsViewedMediaBean.getStartDate();
        this.endDate = lmsViewedMediaBean.getEndDate();
        this.isCompleted = lmsViewedMediaBean.getCompleted();
        this.isViewed = lmsViewedMediaBean.getViewed();
        this.lastPausedOn = lmsViewedMediaBean.getLastPausedOn();
        this.userFeedback = lmsViewedMediaBean.getUserFeedback();
        this.sessions = lmsViewedMediaBean.getSessions();
        if (lmsViewedMediaBean.getTopics() != null && !lmsViewedMediaBean.getTopics().isEmpty()) {
            this.topics = new Gson().fromJson(lmsViewedMediaBean.getTopics(), new TypeToken<List<LmsTopicDataBean>>() {
            }.getType());
            Collections.sort(topics, MyComparatorUtil.LMS_TOPIC_ORDER_COMPARATOR);
        }
        if (lmsViewedMediaBean.getQuestionSet() != null && !lmsViewedMediaBean.getQuestionSet().isEmpty()) {
            this.questionSet = new Gson().fromJson(lmsViewedMediaBean.getQuestionSet(), new TypeToken<List<LmsQuestionSetDataBean>>() {
            }.getType());
        }
    }

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

    public List<LmsTopicDataBean> getTopics() {
        return topics;
    }

    public void setTopics(List<LmsTopicDataBean> topics) {
        this.topics = topics;
    }

    public List<LmsQuestionSetDataBean> getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(List<LmsQuestionSetDataBean> questionSet) {
        this.questionSet = questionSet;
    }
}
