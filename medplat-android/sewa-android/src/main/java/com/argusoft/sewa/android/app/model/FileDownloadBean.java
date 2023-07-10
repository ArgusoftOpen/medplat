package com.argusoft.sewa.android.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class FileDownloadBean extends BaseEntity {

    @DatabaseField
    private Long mediaId;
    @DatabaseField
    private String fileName;
    @DatabaseField
    private String extension;
    @DatabaseField
    private Integer noOfAttempt;
    @DatabaseField
    private Boolean isDownloaded;

    public FileDownloadBean() {
    }

    public FileDownloadBean(Long mediaId, String fileName, String extension, Integer noOfAttempt, Boolean isDownloaded) {
        this.mediaId = mediaId;
        this.fileName = fileName;
        this.extension = extension;
        this.noOfAttempt = noOfAttempt;
        this.isDownloaded = isDownloaded;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Integer getNoOfAttempt() {
        return noOfAttempt;
    }

    public void setNoOfAttempt(Integer noOfAttempt) {
        this.noOfAttempt = noOfAttempt;
    }

    public Boolean getDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(Boolean downloaded) {
        isDownloaded = downloaded;
    }

    @Override
    public String toString() {
        return "FileDownloadBean{" +
                "mediaId=" + mediaId +
                ", fileName='" + fileName + '\'' +
                ", extension='" + extension + '\'' +
                ", noOfAttempt=" + noOfAttempt +
                ", isDownloaded=" + isDownloaded +
                '}';
    }
}
