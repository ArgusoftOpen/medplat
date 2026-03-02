package com.argusoft.sewa.android.app.databean;

public class LmsQuizCompletionMessage {

    private String message;
    private Long mediaId;
    private String mediaExtension;
    private String mediaName;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaExtension() {
        return mediaExtension;
    }

    public void setMediaExtension(String mediaExtension) {
        this.mediaExtension = mediaExtension;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }
}
