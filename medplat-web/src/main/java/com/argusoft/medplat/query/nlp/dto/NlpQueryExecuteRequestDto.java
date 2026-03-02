package com.argusoft.medplat.query.nlp.dto;

public class NlpQueryExecuteRequestDto {

    private String previewId;
    private Boolean confirm;

    public String getPreviewId() {
        return previewId;
    }

    public void setPreviewId(String previewId) {
        this.previewId = previewId;
    }

    public Boolean getConfirm() {
        return confirm;
    }

    public void setConfirm(Boolean confirm) {
        this.confirm = confirm;
    }
}
