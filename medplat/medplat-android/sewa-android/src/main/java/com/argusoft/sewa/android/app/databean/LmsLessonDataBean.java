package com.argusoft.sewa.android.app.databean;

import java.util.List;

public class LmsLessonDataBean {

    private Integer actualId;
    private Integer topicId;
    private Long mediaId;
    private String mediaFileName;
    private String mediaExtension;
    private Integer mediaOrder;
    private String mediaType;
    private Long transcriptFileId;
    private String transcriptFileName;
    private String transcriptFileExtension;
    private String url;
    private String title;
    private String description;
    private Boolean isUserFeedbackRequired;
    private List<LmsQuestionSetDataBean> questionSet;
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

    public String getMediaExtension() {
        return mediaExtension;
    }

    public void setMediaExtension(String mediaExtension) {
        this.mediaExtension = mediaExtension;
    }

    public String getTranscriptFileExtension() {
        return transcriptFileExtension;
    }

    public void setTranscriptFileExtension(String transcriptFileExtension) {
        this.transcriptFileExtension = transcriptFileExtension;
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

    public Boolean getUserFeedbackRequired() {
        return isUserFeedbackRequired;
    }

    public void setUserFeedbackRequired(Boolean userFeedbackRequired) {
        isUserFeedbackRequired = userFeedbackRequired;
    }

    public List<LmsQuestionSetDataBean> getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(List<LmsQuestionSetDataBean> questionSet) {
        this.questionSet = questionSet;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "LmsLessonDataBean{" +
                "actualId=" + actualId +
                ", topicId=" + topicId +
                ", mediaId=" + mediaId +
                ", mediaFileName='" + mediaFileName + '\'' +
                ", mediaExtension='" + mediaExtension + '\'' +
                ", mediaOrder=" + mediaOrder +
                ", mediaType='" + mediaType + '\'' +
                ", transcriptFileId=" + transcriptFileId +
                ", transcriptFileName='" + transcriptFileName + '\'' +
                ", transcriptFileExtension='" + transcriptFileExtension + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", isUserFeedbackRequired=" + isUserFeedbackRequired +
                ", questionSet=" + questionSet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof LmsLessonDataBean))
            return false;
        LmsLessonDataBean that = (LmsLessonDataBean) o;
        return this.actualId.equals(that.actualId);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
