package com.argusoft.sewa.android.app.lms;

import android.graphics.Bitmap;

public class LmsCourseScreenDataBean {

    private Bitmap image;
    private Integer courseId;
    private String courseTitle;
    private String courseDesc;
    private int completionStatus;
    private int questionSetSize;
    private int topicSize;
    private Boolean isMediaDownloaded;
    private Long courseSize;

    public Long getCourseSize() {
        return courseSize;
    }

    public void setCourseSize(Long courseSize) {
        this.courseSize = courseSize;
    }

    public LmsCourseScreenDataBean(Bitmap image, Integer courseId, String courseTitle, String courseDesc, int completionStatus, int questionSetSize, int topicSize, Boolean isMediaDownloaded, Long courseSize) {
        this.image = image;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDesc = courseDesc;
        this.completionStatus = completionStatus;
        this.questionSetSize = questionSetSize;
        this.topicSize = topicSize;
        this.isMediaDownloaded = isMediaDownloaded;
        this.courseSize = courseSize;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    public int getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(int completionStatus) {
        this.completionStatus = completionStatus;
    }

    public int getQuestionSetSize() {
        return questionSetSize;
    }

    public void setQuestionSetSize(int questionSetSize) {
        this.questionSetSize = questionSetSize;
    }

    public int getTopicSize() {
        return topicSize;
    }

    public void setTopicSize(int topicSize) {
        this.topicSize = topicSize;
    }

    public Boolean isMediaDownloaded() {
        return isMediaDownloaded;
    }

    public void setMediaDownloaded(Boolean mediaDownloaded) {
        isMediaDownloaded = mediaDownloaded;
    }

}
