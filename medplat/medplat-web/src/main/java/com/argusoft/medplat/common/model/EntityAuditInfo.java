/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.model;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 *<p>Defines fields related to user</p>
 * @author vaishali
 * @since 26/08/2020 5:30
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class EntityAuditInfo {

    @CreatedBy
    @Basic(optional = false)
    @Column(name = "created_by", nullable = false)
    private Integer createdBy;
    @CreatedDate
    @Basic(optional = false)
    @Column(name = "created_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @LastModifiedBy
    @Column(name = "modified_by")
    private Integer modifiedBy;
    @LastModifiedDate
    @Column(name = "modified_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;

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

    /**
     * An util class defines string constant
     */
    public static class Fields {

        private Fields() {
            
        }

        public static final String CREATED_BY = "createdBy";
        public static final String CREATED_ON = "createdOn";
        public static final String MODIFIED_BY = "modifiedBy";
        public static final String MODIFIED_ON = "modifiedOn";

    }

}
