/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.databean;

import androidx.annotation.NonNull;

import java.util.Date;

/**
 * @author kelvin
 */
public class AnnouncementMobDataBean {

    private String subject;
    private Long publishedOn;
    private Long announcementId;
    private String defaultLanguage;
    private boolean isActive;
    private Long lastUpdateOfAnnouncement;
    private String fileName;
    private Date modifiedOn;
    private boolean hasSeen;
    private String fileExtension;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(Long publishedOn) {
        this.publishedOn = publishedOn;
    }

    public Long getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(Long announcementId) {
        this.announcementId = announcementId;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public Long getLastUpdateOfAnnouncement() {
        return lastUpdateOfAnnouncement;
    }

    public void setLastUpdateOfAnnouncement(Long lastUpdateOfAnnouncement) {
        this.lastUpdateOfAnnouncement = lastUpdateOfAnnouncement;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public boolean isHasSeen() {
        return hasSeen;
    }

    public void setHasSeen(boolean hasSeen) {
        this.hasSeen = hasSeen;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @NonNull
    @Override
    public String toString() {
        return "AnnouncementMobDataBean{" +
                "subject='" + subject + '\'' +
                ", publishedOn=" + publishedOn +
                ", announcementId=" + announcementId +
                ", defaultLanguage='" + defaultLanguage + '\'' +
                ", isActive=" + isActive +
                ", lastUpdateOfAnnouncement=" + lastUpdateOfAnnouncement +
                ", fileName='" + fileName + '\'' +
                ", hasSeen='" + hasSeen + '\'' +
                ", fileExtension='" + fileExtension + '\'' +
                '}';
    }
}
