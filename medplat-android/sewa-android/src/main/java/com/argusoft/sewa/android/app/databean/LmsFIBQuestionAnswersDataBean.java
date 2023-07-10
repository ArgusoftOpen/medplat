package com.argusoft.sewa.android.app.databean;

public class LmsFIBQuestionAnswersDataBean {

    private Integer blankNumber;
    private String blankValue;

    public Integer getBlankNumber() {
        return blankNumber;
    }

    public void setBlankNumber(Integer blankNumber) {
        this.blankNumber = blankNumber;
    }

    public String getBlankValue() {
        return blankValue;
    }

    public void setBlankValue(String blankValue) {
        this.blankValue = blankValue;
    }

    @Override
    public String toString() {
        return "LmsQuestionAnswersDataBean{" +
                "blankNumber=" + blankNumber +
                ", blankValue='" + blankValue + '\'' +
                '}';
    }
}
