package com.argusoft.medplat.mobile.dto;

import java.util.List;

public class TopicDataBean {

    private Integer topicId;
    private Integer courseId;
    private String topicName;
    private String topicDescription;
    private String topicOrder;
    private String topicState;
    private List<TopicMediaDataBean> topicMedias;
    private List<QuestionSetBean> questionSet;

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }

    public String getTopicOrder() {
        return topicOrder;
    }

    public void setTopicOrder(String topicOrder) {
        this.topicOrder = topicOrder;
    }

    public String getTopicState() {
        return topicState;
    }

    public void setTopicState(String topicState) {
        this.topicState = topicState;
    }

    public List<TopicMediaDataBean> getTopicMedias() {
        return topicMedias;
    }

    public void setTopicMedias(List<TopicMediaDataBean> topicMedias) {
        this.topicMedias = topicMedias;
    }

    public List<QuestionSetBean> getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(List<QuestionSetBean> questionSet) {
        this.questionSet = questionSet;
    }
}
