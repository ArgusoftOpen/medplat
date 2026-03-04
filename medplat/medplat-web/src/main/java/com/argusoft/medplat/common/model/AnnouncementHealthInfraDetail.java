package com.argusoft.medplat.common.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "announcement_health_infra_detail")
public class AnnouncementHealthInfraDetail {

    @EmbeddedId
    private AnnouncementHealthInfraDetailPKey announcementHealthInfraDetailPKey;

    @Column(name = "has_seen")
    private Boolean hasSeen;

    public AnnouncementHealthInfraDetailPKey getAnnouncementHealthInfraDetailPKey() {
        return announcementHealthInfraDetailPKey;
    }

    public void setAnnouncementHealthInfraDetailPKey(AnnouncementHealthInfraDetailPKey announcementHealthInfraDetailPKey) {
        this.announcementHealthInfraDetailPKey = announcementHealthInfraDetailPKey;
    }

    public Boolean getHasSeen() {
        return hasSeen;
    }

    public void setHasSeen(Boolean hasSeen) {
        this.hasSeen = hasSeen;
    }
}
