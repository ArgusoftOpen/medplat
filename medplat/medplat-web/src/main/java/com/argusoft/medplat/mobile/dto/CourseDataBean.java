package com.argusoft.medplat.mobile.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class CourseDataBean {

    private Integer courseId;
    private String courseName;
    private String courseDescription;
    private String courseType;
    private String courseState;
    private Date scheduleDate;
    private List<TopicDataBean> topics;
    private List<QuestionSetBean> questionSet;
    private String testConfigJsonString;
    private Map<Integer, LmsQuizConfigDataBean> testConfigJson;
    private String courseImageJsonString;
    private CourseImageDataBean courseImage;
    private Boolean isAllowedToSkipLessons;

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

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCourseState() {
        return courseState;
    }

    public void setCourseState(String courseState) {
        this.courseState = courseState;
    }

    public List<TopicDataBean> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicDataBean> topics) {
        this.topics = topics;
    }

    public List<QuestionSetBean> getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(List<QuestionSetBean> questionSet) {
        this.questionSet = questionSet;
    }

    public String getTestConfigJsonString() {
        return testConfigJsonString;
    }

    public void setTestConfigJsonString(String testConfigJsonString) {
        this.testConfigJsonString = testConfigJsonString;
    }

    public Map<Integer, LmsQuizConfigDataBean> getTestConfigJson() {
        return testConfigJson;
    }

    public void setTestConfigJson(Map<Integer, LmsQuizConfigDataBean> testConfigJson) {
        this.testConfigJson = testConfigJson;
    }

    public String getCourseImageJsonString() {
        return courseImageJsonString;
    }

    public void setCourseImageJsonString(String courseImageJsonString) {
        this.courseImageJsonString = courseImageJsonString;
    }

    public CourseImageDataBean getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(CourseImageDataBean courseImage) {
        this.courseImage = courseImage;
    }

    public Boolean getIsAllowedToSkipLessons() {
        return isAllowedToSkipLessons;
    }

    public void setIsAllowedToSkipLessons(Boolean isAllowedToSkipLessons) {
        this.isAllowedToSkipLessons = isAllowedToSkipLessons;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }
}
