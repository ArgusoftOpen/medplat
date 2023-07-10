/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.dto;

import com.argusoft.medplat.notification.model.FormMaster;

import java.util.Date;

/**
 *
 * <p>
 *     Used for form master.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class FormMasterDto {

    private Integer id;
    private String code;
    private String name;
    private FormMaster.State state;
    private Integer createdBy;
    private Date createdOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FormMaster.State getState() {
        return state;
    }

    public void setState(FormMaster.State state) {
        this.state = state;
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

    
}
