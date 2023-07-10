/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 *<p>Defines fields related to user</p>
 * @author ashish
 * @since 26/08/2020 5:30
 */

@Entity
@Table(name = "sync_system_configuration_master")
public class SystemConfigSync implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    
    @Column(name = "feature_uuid")
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID featureUUID;
    
    @Column(name = "feature_name")
    private String  featureName;    
    
    @Column (name = "feature_type")
    private String featureType;
    
    @Column (name = "config_json")
    private String configJson;
    
    @Column(name = "created_by", nullable = false)
    private Integer createdBy;
    
    @Column(name = "created_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    public SystemConfigSync() {
    
    }   
    
    public SystemConfigSync(Integer id, UUID featureUUID, String featureName, String featureType, String configJson, Integer createdBy, Date createdOn) {
        this.id = id;
        this.featureUUID = featureUUID;
        this.featureName = featureName;
        this.featureType = featureType;
        this.configJson = configJson;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
    }

    public UUID getFeatureUUID() {
        return featureUUID;
    }

    public void setFeatureUUID(UUID featureUUID) {
        this.featureUUID = featureUUID;
    }    

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
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
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFeatureType() {
        return featureType;
    }

    public void setFeatureType(String featureType) {
        this.featureType = featureType;
    }

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson;
    }


    /**
     * An util class defines string constant
     */
    public static class Fields {

        private Fields() {
            
        }

        public static final String ID = "id";
        public static final String FEATURE_TYPE = "featureType";
        public static final String CONFIG_JSON = "configJson";
        public static final String CREATED_BY = "createdBy";
        public static final String CREATED_ON = "createdOn";
    }

}
