/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p>Defines fields related to user</p>
 *
 * @author smeet
 * @since 26/08/2020 5:30
 */
@Entity
@Table(name = "announcement_location_detail")
public class AnnouncementLocationDetail {

    @EmbeddedId
    private AnnouncementLocationDetailPKey announcementLocationDetailPKey;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "location_type")
    private String locationType;

    public AnnouncementLocationDetailPKey getAnnouncementLocationDetailPKey() {
        return announcementLocationDetailPKey;
    }

    public void setAnnouncementLocationDetailPKey(AnnouncementLocationDetailPKey announcementLocationDetailPKey) {
        this.announcementLocationDetailPKey = announcementLocationDetailPKey;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    @Override
    public String toString() {
        return "AnnouncementLocationDetail{" +
                "announcementLocationDetailPKey=" + announcementLocationDetailPKey +
                ", isActive=" + isActive +
                ", locationType='" + locationType + '\'' +
                '}';
    }
}
