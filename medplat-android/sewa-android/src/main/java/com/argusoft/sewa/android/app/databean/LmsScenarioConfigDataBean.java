package com.argusoft.sewa.android.app.databean;

import java.util.List;

public class LmsScenarioConfigDataBean {

    String scenarioTitle;
    String scenarioDescription;
    private Long mediaId;
    private String mediaName;
    private String mediaExtension;
    private String mediaType;
    List<LmsQuestionConfigDataBean> scenarioQuestions;
    LmsSimulationAudioDataBean audio;

    public String getScenarioTitle() {
        return scenarioTitle;
    }

    public void setScenarioTitle(String scenarioTitle) {
        this.scenarioTitle = scenarioTitle;
    }

    public String getScenarioDescription() {
        return scenarioDescription;
    }

    public void setScenarioDescription(String scenarioDescription) {
        this.scenarioDescription = scenarioDescription;
    }

    public List<LmsQuestionConfigDataBean> getScenarioQuestions() {
        return scenarioQuestions;
    }

    public void setScenarioQuestions(List<LmsQuestionConfigDataBean> scenarioQuestions) {
        this.scenarioQuestions = scenarioQuestions;
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

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public LmsSimulationAudioDataBean getAudio() {
        return audio;
    }

    public void setAudio(LmsSimulationAudioDataBean audio) {
        this.audio = audio;
    }
}
