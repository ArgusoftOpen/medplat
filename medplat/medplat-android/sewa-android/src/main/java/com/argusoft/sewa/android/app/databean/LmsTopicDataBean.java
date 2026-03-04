package com.argusoft.sewa.android.app.databean;

import com.argusoft.sewa.android.app.util.MyComparatorUtil;

import java.util.Collections;
import java.util.List;

public class LmsTopicDataBean {

    private Integer topicId;
    private String topicName;
    private String topicDescription;
    private String topicOrder;
    private String topicState;
    private Integer courseId;
    private List<LmsLessonDataBean> topicMedias;
    private List<LmsQuestionSetDataBean> questionSet;

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
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

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public List<LmsLessonDataBean> getTopicMedias() {
        Collections.sort(topicMedias, MyComparatorUtil.LMS_TOPIC_MEDIA_ORDER_COMPARATOR);
        return topicMedias;
    }

    public void setTopicMedias(List<LmsLessonDataBean> topicMedias) {
        this.topicMedias = topicMedias;
    }

    public List<LmsQuestionSetDataBean> getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(List<LmsQuestionSetDataBean> questionSet) {
        this.questionSet = questionSet;
    }

    @Override
    public String toString() {
        return "LmsTopicDataBean{" +
                "topicId=" + topicId +
                ", topicName='" + topicName + '\'' +
                ", topicDescription='" + topicDescription + '\'' +
                ", topicOrder='" + topicOrder + '\'' +
                ", topicState='" + topicState + '\'' +
                ", courseId=" + courseId +
                '}';
    }
}
