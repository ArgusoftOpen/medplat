package com.argusoft.medplat.mobile.dto;

import java.util.List;

public class TopicMediaDataBean {

    private Integer actualId;
    private Integer topicId;
    private Long mediaId;
    private String mediaFileName;
    private Integer mediaOrder;
    private String mediaType;
    private Long transcriptFileId;
    private String transcriptFileName;
    private String url;
    private String title;
    private String description;
    private String mediaState;
    private Boolean isUserFeedbackRequired;
    private List<QuestionSetBean> questionSet;
    private Long size;

    public Integer getActualId() {
        return actualId;
    }

    public void setActualId(Integer actualId) {
        this.actualId = actualId;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaFileName() {
        return mediaFileName;
    }

    public void setMediaFileName(String mediaFileName) {
        this.mediaFileName = mediaFileName;
    }

    public Integer getMediaOrder() {
        return mediaOrder;
    }

    public void setMediaOrder(Integer mediaOrder) {
        this.mediaOrder = mediaOrder;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Long getTranscriptFileId() {
        return transcriptFileId;
    }

    public void setTranscriptFileId(Long transcriptFileId) {
        this.transcriptFileId = transcriptFileId;
    }

    public String getTranscriptFileName() {
        return transcriptFileName;
    }

    public void setTranscriptFileName(String transcriptFileName) {
        this.transcriptFileName = transcriptFileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMediaState() {
        return mediaState;
    }

    public void setMediaState(String mediaState) {
        this.mediaState = mediaState;
    }

    public Boolean getIsUserFeedbackRequired() {
        return isUserFeedbackRequired;
    }

    public void setIsUserFeedbackRequired(Boolean isUserFeedbackRequired) {
        this.isUserFeedbackRequired = isUserFeedbackRequired;
    }

    public List<QuestionSetBean> getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(List<QuestionSetBean> questionSet) {
        this.questionSet = questionSet;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
