/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * <p>
 *     Define location_master for mobile entity and its fields.
 * </p>
 * @author kunjan
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "location_master")
public class LocationMasterMobile implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 4000)
    private String name;
    
    @Column(name = "parent",insertable = false,updatable = false)
    private Integer parent;
    
    @Column(name = "type",insertable = false,updatable = false)
    private String type;
    
    @Basic(optional = false)
    @Column(name = "created_by", nullable = false, length = 50)
    private String createdBy;
    
    @Basic(optional = false)
    @Column(name = "created_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    
    @Column(name = "modified_by", length = 50)
    private String modifiedBy;
    
    @Column(name = "modified_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private LocationMasterMobile.State state;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public LocationMasterMobile.State getState() {
        return state;
    }

    public void setState(LocationMasterMobile.State state) {
        this.state = state;
    }

    /**
     * Define fields name for location master mobile.
     */
    public static class Fields {

        private Fields(){
        }

        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String CREATED_BY = "createdBy";
        public static final String CREATED_ON = "createdOn";
        public static final String MODIFIED_BY = "modifiedBy";
        public static final String MODIFIED_ON = "modifiedOn";
        public static final String PARENT = "parent";
        public static final String STATE = "state";
    }
    
    public enum State {
        ACTIVE,
        INACTIVE
    }
}
