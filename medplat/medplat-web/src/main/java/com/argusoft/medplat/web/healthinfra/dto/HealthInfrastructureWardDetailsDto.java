package com.argusoft.medplat.web.healthinfra.dto;

import java.util.Date;

/**
 *
 * <p>
 *     Used for health infrastructure ward.
 * </p>
 * @author dhaval
 * @since 26/08/20 11:00 AM
 *
 */
public class HealthInfrastructureWardDetailsDto {

    private Integer id;

    private Integer healthInfraId;

    private String wardName;

    private Integer wardType;

    private Integer numberOfBeds;

    private Integer numberOfVentilatorsType1;

    private Integer numberOfVentilatorsType2;

    private Integer numberOfO2;

    private String status;

    private Integer createdBy;

    private Date createdOn;

    private Integer modifiedBy;

    private Date modifiedOn;

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

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public Integer getWardType() {
        return wardType;
    }

    public void setWardType(Integer wardType) {
        this.wardType = wardType;
    }

    public Integer getNumberOfBeds() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(Integer numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public Integer getNumberOfVentilatorsType1() {
        return numberOfVentilatorsType1;
    }

    public void setNumberOfVentilatorsType1(Integer numberOfVentilatorsType1) {
        this.numberOfVentilatorsType1 = numberOfVentilatorsType1;
    }

    public Integer getNumberOfVentilatorsType2() {
        return numberOfVentilatorsType2;
    }

    public void setNumberOfVentilatorsType2(Integer numberOfVentilatorsType2) {
        this.numberOfVentilatorsType2 = numberOfVentilatorsType2;
    }

    public Integer getNumberOfO2() {
        return numberOfO2;
    }

    public void setNumberOfO2(Integer numberOfO2) {
        this.numberOfO2 = numberOfO2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
