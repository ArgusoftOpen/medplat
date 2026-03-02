/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.dto;

import java.util.Date;

/**
 *
 * <p>
 *     Used for member complaints details.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class MemberComplaintsDto {
  
    private Integer id;
        
    private Integer memberId;
    
    private String complaint;
    
    private Integer createdBy;
    
    private Date createdOn;
    
    private Integer modifiedBy;
    
    private Date modifiedOn;
    
    private Date doneOn;
    
    private String doneBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
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

    public Date getDoneOn() {
        return doneOn;
    }

    public void setDoneOn(Date doneOn) {
        this.doneOn = doneOn;
    }

    public String getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(String doneBy) {
        this.doneBy = doneBy;
    }

    
    
}
