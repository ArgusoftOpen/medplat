/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>Defines fields related to announcement</p>
 *
 * @author smeet
 * @since 26/08/2020 5:30
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnnouncementMasterDto {

    private Integer id;
    private String subject;
    private Integer createdBy;
    private Date createdOn;
    private Integer modifiedBy;
    private Date modifiedOn;
    private Date fromDate;
    private Boolean isActive;
    private String defaultLanguage;
    private Boolean containsMultimedia;

    private byte[] content;
    private String fileExtension;
    private String mediaPath;

    private Integer location;
    private List<String> announcementForArray;
    private String announcementFor;
    private String locationType;
    private List<LocationDto> locations;
    private boolean mediaExists;
    private List<Integer> healthInfras;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public List<String> getAnnouncementForArray() {
        return announcementForArray;
    }

    public void setAnnouncementForArray(List<String> announcementForArray) {
        this.announcementForArray = announcementForArray;
    }

    public String getAnnouncementFor() {
        return announcementFor;
    }

    public void setAnnouncementFor(String announcementFor) {
        this.announcementFor = announcementFor;
    }

    public List<LocationDto> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationDto> locations) {
        this.locations = locations;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public Boolean getContainsMultimedia() {
        return containsMultimedia;
    }

    public void setContainsMultimedia(Boolean containsMultimedia) {
        this.containsMultimedia = containsMultimedia;
    }

    public boolean isMediaExists() {
        return mediaExists;
    }

    public void setMediaExists(boolean mediaExists) {
        this.mediaExists = mediaExists;
    }

    public List<Integer> getHealthInfras() {
        return healthInfras;
    }

    public void setHealthInfras(List<Integer> healthInfras) {
        this.healthInfras = healthInfras;
    }

    @Override
    public String toString() {
        return "AnnouncementMasterDto{" + "id=" + id + ", subject=" + subject + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", modifiedBy=" + modifiedBy + ", modifiedOn=" + modifiedOn + ", fromDate=" + fromDate + ", isActive=" + isActive + ", defaultLanguage=" + defaultLanguage + ", containsMultimedia=" + containsMultimedia + ", content=" + Arrays.toString(content) + ", fileExtension=" + fileExtension + ", mediaPath=" + mediaPath + ", location=" + location + ", announcementForArray=" + announcementForArray + ", announcementFor=" + announcementFor + ", locationType=" + locationType + ", locations=" + locations + '}';
    }

}
