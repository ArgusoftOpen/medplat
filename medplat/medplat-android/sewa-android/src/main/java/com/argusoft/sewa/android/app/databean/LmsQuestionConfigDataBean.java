package com.argusoft.sewa.android.app.databean;

import com.google.gson.JsonObject;

import java.util.List;

public class LmsQuestionConfigDataBean {

    private Integer id;
    private String questionTitle;
    private String questionType;
    private List<LmsQuestionOptionDataBean> options;
    private List<LmsQuestionOptionDataBean> lhs;
    private List<LmsQuestionOptionDataBean> rhs;
    private List<LmsFIBQuestionAnswersDataBean> answers;
    private JsonObject answerPairs;
    private Long mediaId;
    private String mediaName;
    private String mediaExtension;
    private String correctAnswerDescription;
    private String incorrectAnswerDescription;
    private String interactiveFeedbackContent;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public List<LmsQuestionOptionDataBean> getOptions() {
        return options;
    }

    public void setOptions(List<LmsQuestionOptionDataBean> options) {
        this.options = options;
    }

    public List<LmsQuestionOptionDataBean> getLhs() {
        return lhs;
    }

    public void setLhs(List<LmsQuestionOptionDataBean> lhs) {
        this.lhs = lhs;
    }

    public List<LmsQuestionOptionDataBean> getRhs() {
        return rhs;
    }

    public void setRhs(List<LmsQuestionOptionDataBean> rhs) {
        this.rhs = rhs;
    }

    public List<LmsFIBQuestionAnswersDataBean> getAnswers() {
        return answers;
    }

    public void setAnswers(List<LmsFIBQuestionAnswersDataBean> answers) {
        this.answers = answers;
    }

    public JsonObject getAnswerPairs() {
        return answerPairs;
    }

    public void setAnswerPairs(JsonObject answerPairs) {
        this.answerPairs = answerPairs;
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

    public String getCorrectAnswerDescription() {
        return correctAnswerDescription;
    }

    public void setCorrectAnswerDescription(String correctAnswerDescription) {
        this.correctAnswerDescription = correctAnswerDescription;
    }

    public String getIncorrectAnswerDescription() {
        return incorrectAnswerDescription;
    }

    public void setIncorrectAnswerDescription(String incorrectAnswerDescription) {
        this.incorrectAnswerDescription = incorrectAnswerDescription;
    }

    public String getInteractiveFeedbackContent() {
        return interactiveFeedbackContent;
    }

    public void setInteractiveFeedbackContent(String interactiveFeedbackContent) {
        this.interactiveFeedbackContent = interactiveFeedbackContent;
    }

    @Override
    public String toString() {
        return "LmsQuestionConfigDataBean{" +
                "id=" + id +
                ", questionTitle='" + questionTitle + '\'' +
                ", questionType='" + questionType + '\'' +
                ", options=" + options +
                ", answers=" + answerPairs +
                ", mediaId=" + mediaId +
                ", mediaName='" + mediaName + '\'' +
                ", mediaExtension='" + mediaExtension + '\'' +
                '}';
    }
}
