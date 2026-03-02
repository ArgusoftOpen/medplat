/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.dto;

import java.util.Date;

/**
 * @author prateek
 */
public class AnnouncementMobDataBean {

    private String subject;
    private Long publishedOn;
    private Integer announcementId;
    private String defaultLanguage;
    private boolean isActive;
    private Long lastUpdateOfAnnouncement;
    private String fileName;
    private Date modifiedOn;
    private Boolean hasSeen;
    private String fromDate;
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

    public Integer getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(Integer announcementId) {
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

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Boolean getHasSeen() {
        return hasSeen;
    }

    public void setHasSeen(Boolean hasSeen) {
        this.hasSeen = hasSeen;
    }

    @Override
    public String toString() {
        return "AnnouncementMobDataBean{" + "subject=" + subject + ", publishedOn=" + publishedOn + ", announcementId=" + announcementId + ", defaultLanguage=" + defaultLanguage + ", isActive=" + isActive + ", lastUpdateOfAnnouncement=" + lastUpdateOfAnnouncement + ", fileName=" + fileName + '}';
    }

}