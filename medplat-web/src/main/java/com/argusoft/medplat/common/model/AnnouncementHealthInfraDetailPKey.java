package com.argusoft.medplat.common.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AnnouncementHealthInfraDetailPKey implements Serializable {

    @Basic(optional = false)
    @Column(name = "announcement")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "health_infra_id")
    private Integer healthInfraId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHealthInfraId() {
        return healthInfraId;
    }

    public void setHealthInfraId(Integer healthInfraId) {
        this.healthInfraId = healthInfraId;
    }
}
