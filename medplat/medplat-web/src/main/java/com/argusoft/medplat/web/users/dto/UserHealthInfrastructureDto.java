/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.users.dto;

/**
 * <p>Defines fields related to user health infrastructure</p>
 * @author vaishali
 * @since 26/08/2020 5:30
 */
public class UserHealthInfrastructureDto {

    private Integer id;

    private Integer userId;

    private Integer healthInfrastructureId;

    private String healthInfrastructureName;

    private String healthInfrastructurePincode;

    private String healthInfrastructureRegNo;

    private String healthInfrastructureAddress;

    private Integer createdBy;

    private Integer createdOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getHealthInfrastructureId() {
        return healthInfrastructureId;
    }

    public void setHealthInfrastructureId(Integer healthInfrastructureId) {
        this.healthInfrastructureId = healthInfrastructureId;
    }

    public String getHealthInfrastructureName() {
        return healthInfrastructureName;
    }

    public void setHealthInfrastructureName(String healthInfrastructureName) {
        this.healthInfrastructureName = healthInfrastructureName;
    }

    public String getHealthInfrastructurePincode() {
        return healthInfrastructurePincode;
    }

    public void setHealthInfrastructurePincode(String healthInfrastructurePincode) {
        this.healthInfrastructurePincode = healthInfrastructurePincode;
    }

    public String getHealthInfrastructureRegNo() {
        return healthInfrastructureRegNo;
    }

    public void setHealthInfrastructureRegNo(String healthInfrastructureRegNo) {
        this.healthInfrastructureRegNo = healthInfrastructureRegNo;
    }

    public String getHealthInfrastructureAddress() {
        return healthInfrastructureAddress;
    }

    public void setHealthInfrastructureAddress(String healthInfrastructureAddress) {
        this.healthInfrastructureAddress = healthInfrastructureAddress;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Integer createdOn) {
        this.createdOn = createdOn;
    }

}
