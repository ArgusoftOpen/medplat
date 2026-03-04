package com.argusoft.sewa.android.app.databean;

public class LmsSimulationAudioDataBean {
    private Boolean isAudioWithControls;
    private Long mediaId;
    private String mediaName;
    private String mediaExtension;

    public Boolean getAudioWithControls() {
        return isAudioWithControls;
    }

    public void setAudioWithControls(Boolean audioWithControls) {
        isAudioWithControls = audioWithControls;
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
}
