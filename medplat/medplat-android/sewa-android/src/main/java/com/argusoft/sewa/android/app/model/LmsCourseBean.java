package com.argusoft.sewa.android.app.model;

import com.argusoft.sewa.android.app.databean.LmsCourseDataBean;
import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable
public class LmsCourseBean extends BaseEntity implements Serializable {

    @DatabaseField
    private Integer courseId;
    @DatabaseField
    private String courseName;
    @DatabaseField
    private String courseDescription;
    @DatabaseField
    private String courseState;
    @DatabaseField
    private String courseType;
    @DatabaseField
    private String topics;
    @DatabaseField
    private String questionSet;
    @DatabaseField
    private String testConfigJson;
    @DatabaseField
    private String courseImage;
    @DatabaseField
    private Boolean isArchived;
    @DatabaseField
    private Boolean isAllowedToSkipLessons;
    @DatabaseField
    private Date scheduleDate;
    @DatabaseField
    private Boolean isMediaDownloaded;

    public LmsCourseBean() {
    }

    public LmsCourseBean(LmsCourseDataBean lmsCourseDataBean) {
        this.courseId = lmsCourseDataBean.getCourseId();
        this.courseName = lmsCourseDataBean.getCourseName();
        this.courseDescription = lmsCourseDataBean.getCourseDescription();
        this.courseState = lmsCourseDataBean.getCourseState();
        this.courseType = lmsCourseDataBean.getCourseType();
        this.isAllowedToSkipLessons = lmsCourseDataBean.getAllowedToSkipLessons();
        this.scheduleDate = lmsCourseDataBean.getScheduleDate();
        this.isMediaDownloaded = lmsCourseDataBean.getMediaDownloaded();
        if (lmsCourseDataBean.getTopics() != null && !lmsCourseDataBean.getTopics().isEmpty()) {
            this.topics = new Gson().toJson(lmsCourseDataBean.getTopics());
        }
        if (lmsCourseDataBean.getQuestionSet() != null && !lmsCourseDataBean.getQuestionSet().isEmpty()) {
            this.questionSet = new Gson().toJson(lmsCourseDataBean.getQuestionSet());
        }
        if (lmsCourseDataBean.getTestConfigJson() != null && !lmsCourseDataBean.getTestConfigJson().isEmpty()) {
            this.testConfigJson = new Gson().toJson(lmsCourseDataBean.getTestConfigJson());
        }
        if (lmsCourseDataBean.getCourseImage() != null) {
            this.courseImage = new Gson().toJson(lmsCourseDataBean.getCourseImage());
        }
        if(lmsCourseDataBean.getArchived() != null) {
            this.isArchived = lmsCourseDataBean.getArchived();
        }
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseState() {
        return courseState;
    }

    public void setCourseState(String courseState) {
        this.courseState = courseState;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
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

    public String getTestConfigJson() {
        return testConfigJson;
    }

    public void setTestConfigJson(String testConfigJson) {
        this.testConfigJson = testConfigJson;
    }

    public String getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(String courseImage) {
        this.courseImage = courseImage;
    }

    public Boolean getAllowedToSkipLessons() {
        return isAllowedToSkipLessons;
    }

    public void setAllowedToSkipLessons(Boolean allowedToSkipLessons) {
        isAllowedToSkipLessons = allowedToSkipLessons;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }

    public Boolean getMediaDownloaded() {
        return isMediaDownloaded;
    }

    public void setMediaDownloaded(Boolean mediaDownloaded) {
        isMediaDownloaded = mediaDownloaded;
    }
}
