package com.argusoft.sewa.android.app.databean;

public class LmsExpandableListDataBean {

    private Integer groupIndex;

    private LmsQuestionSetDataBean questionSet;

    private LmsTopicDataBean module;

    private String groupType;

    public Integer getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(Integer groupIndex) {
        this.groupIndex = groupIndex;
    }

    public LmsQuestionSetDataBean getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(LmsQuestionSetDataBean questionSet) {
        this.questionSet = questionSet;
    }

    public LmsTopicDataBean getModule() {
        return module;
    }

    public void setModule(LmsTopicDataBean module) {
        this.module = module;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }
}
