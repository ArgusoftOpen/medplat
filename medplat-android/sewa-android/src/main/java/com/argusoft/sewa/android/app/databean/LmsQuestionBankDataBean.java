package com.argusoft.sewa.android.app.databean;

public class LmsQuestionBankDataBean {

    private Integer actualId;
    private Integer questionSetId;
    private String configJson;

    public Integer getActualId() {
        return actualId;
    }

    public void setActualId(Integer actualId) {
        this.actualId = actualId;
    }

    public Integer getQuestionSetId() {
        return questionSetId;
    }

    public void setQuestionSetId(Integer questionSetId) {
        this.questionSetId = questionSetId;
    }

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson;
    }

    @Override
    public String toString() {
        return "LmsQuestionBankDataBean{" +
                "actualId=" + actualId +
                ", questionSetId=" + questionSetId +
                ", configJson='" + configJson + '\'' +
                '}';
    }
}
