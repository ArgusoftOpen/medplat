package com.argusoft.sewa.android.app.databean;

import com.argusoft.sewa.android.app.model.LmsCourseBean;
import com.argusoft.sewa.android.app.util.MyComparatorUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LmsCourseDataBean {

    private Integer courseId;
    private String courseName;
    private String courseDescription;
    private String courseState;
    private String courseType;
    private List<LmsTopicDataBean> topics;
    private List<LmsQuestionSetDataBean> questionSet;
    private Map<Integer, LmsQuizConfigDataBean> testConfigJson;
    private LmsCourseImageDataBean courseImage;
    private Integer completionStatus;
    private Boolean isArchived;
    private Boolean isAllowedToSkipLessons;
    private Date scheduleDate;
    private Boolean isMediaDownloaded;
    List<LmsExpandableListDataBean> listDataBeans;

    public LmsCourseDataBean() {
    }

    public LmsCourseDataBean(LmsCourseBean lmsCourseBean) {
        this.courseId = lmsCourseBean.getCourseId();
        this.courseName = lmsCourseBean.getCourseName();
        this.courseDescription = lmsCourseBean.getCourseDescription();
        this.courseState = lmsCourseBean.getCourseState();
        this.courseType = lmsCourseBean.getCourseType();
        this.isAllowedToSkipLessons = lmsCourseBean.getAllowedToSkipLessons();
        if(lmsCourseBean.getArchived() != null)
            this.isArchived = lmsCourseBean.getArchived();
        this.scheduleDate = lmsCourseBean.getScheduleDate();
//        this.isMediaDownloaded = lmsCourseBean.getMediaDownloaded();
        if (lmsCourseBean.getTopics() != null && !lmsCourseBean.getTopics().isEmpty()) {
            this.topics = new Gson().fromJson(lmsCourseBean.getTopics(), new TypeToken<List<LmsTopicDataBean>>() {
            }.getType());
            Collections.sort(topics, MyComparatorUtil.LMS_TOPIC_ORDER_COMPARATOR);
        }
        if (lmsCourseBean.getQuestionSet() != null && !lmsCourseBean.getQuestionSet().isEmpty()) {
            this.questionSet = new Gson().fromJson(lmsCourseBean.getQuestionSet(), new TypeToken<List<LmsQuestionSetDataBean>>() {
            }.getType());
        }
        if (lmsCourseBean.getTestConfigJson() != null && !lmsCourseBean.getTestConfigJson().isEmpty()) {
            this.testConfigJson = new Gson().fromJson(lmsCourseBean.getTestConfigJson(), new TypeToken<Map<Integer, LmsQuizConfigDataBean>>() {
            }.getType());
        }
        if (lmsCourseBean.getCourseImage() != null) {
            this.courseImage = new Gson().fromJson(lmsCourseBean.getCourseImage(), LmsCourseImageDataBean.class);
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

    public Map<Integer, LmsQuizConfigDataBean> getTestConfigJson() {
        return testConfigJson;
    }

    public void setTestConfigJson(Map<Integer, LmsQuizConfigDataBean> testConfigJson) {
        this.testConfigJson = testConfigJson;
    }

    public LmsCourseImageDataBean getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(LmsCourseImageDataBean courseImage) {
        this.courseImage = courseImage;
    }

    public Integer getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(Integer completionStatus) {
        this.completionStatus = completionStatus;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
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

    public List<LmsExpandableListDataBean> getListDataBeans() {
        return listDataBeans;
    }

    public void setListDataBeans(List<LmsExpandableListDataBean> listDataBeans) {
        this.listDataBeans = listDataBeans;
    }

    public Boolean getMediaDownloaded() {
        return isMediaDownloaded;
    }

    public void setMediaDownloaded(Boolean mediaDownloaded) {
        isMediaDownloaded = mediaDownloaded;
    }

}
