package com.argusoft.sewa.android.app.databean;

public class LmsQuestionOptionDataBean {

    private String optionTitle;
    private String optionValue;
    private Boolean isCorrect;
    private LmsOptionFeedbackDataBean optionFeedback;
    private Long mediaId;
    private String mediaName;
    private String mediaExtension;

    public String getOptionTitle() {
        return optionTitle;
    }

    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getMediaExtension() {
        return mediaExtension;
    }

    public void setMediaExtension(String mediaExtension) {
        this.mediaExtension = mediaExtension;
    }

    public LmsOptionFeedbackDataBean getOptionFeedback() {
        return optionFeedback;
    }

    public void setOptionFeedback(LmsOptionFeedbackDataBean optionFeedback) {
        this.optionFeedback = optionFeedback;
    }
}
