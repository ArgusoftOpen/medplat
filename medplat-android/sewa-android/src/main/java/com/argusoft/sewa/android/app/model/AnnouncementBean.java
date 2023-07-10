/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.model;

import androidx.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * @author kelvin
 */
@DatabaseTable
public class AnnouncementBean extends BaseEntity {

    @DatabaseField
    private Long announcementId;
    @DatabaseField
    private String defaultLanguage;
    @DatabaseField
    private String subject;
    @DatabaseField
    private Long publishedOn;
    @DatabaseField
    private String isDownloaded;
    @DatabaseField
    private String fileName;
    @DatabaseField
    private int isPlayedAnnouncement;//0 for new announcement and 1 for old ones
    @DatabaseField
    private Date modifiedOn;

    public AnnouncementBean() {
    }

    public AnnouncementBean(Long announcementId, String subject, Long publishedOn, String fileName) {
        this.announcementId = announcementId;
        this.subject = subject;
        this.publishedOn = publishedOn;
        this.fileName = fileName;
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

    public String getIsDownloaded() {
        return isDownloaded;
    }

    public void setIsDownloaded(String isDownloaded) {
        this.isDownloaded = isDownloaded;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getIsPlayedAnnouncement() {
        return isPlayedAnnouncement;
    }

    public void setIsPlayedAnnouncement(int isPlayedAnnouncement) {
        this.isPlayedAnnouncement = isPlayedAnnouncement;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @NonNull
    @Override
    public String toString() {
        return "AnnouncementBean{" + "announcementId=" + announcementId + ", defaultLanguage=" + defaultLanguage + ", subject=" + subject + ", publishedOn=" + publishedOn + ", isDownloaded=" + isDownloaded + ", fileName=" + fileName + ", isPlayedAnnouncement=" + isPlayedAnnouncement + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof AnnouncementBean))
            return false;
        AnnouncementBean that = (AnnouncementBean) o;
        return this.announcementId.equals(that.announcementId);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
