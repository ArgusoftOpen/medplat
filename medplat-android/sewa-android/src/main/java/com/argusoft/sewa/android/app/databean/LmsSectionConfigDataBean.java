package com.argusoft.sewa.android.app.databean;

import java.util.List;

public class LmsSectionConfigDataBean {

    String sectionTitle;
    String sectionDescription;
    List<LmsQuestionConfigDataBean> questions;

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public String getSectionDescription() {
        return sectionDescription;
    }

    public void setSectionDescription(String sectionDescription) {
        this.sectionDescription = sectionDescription;
    }

    public List<LmsQuestionConfigDataBean> getQuestions() {
        return questions;
    }

    public void setQuestions(List<LmsQuestionConfigDataBean> questions) {
        this.questions = questions;
    }
}
