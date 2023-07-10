/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dto;

import java.util.Date;

/**
 * <p>
 * Used for facility performance master.
 * </p>
 *
 * @author vivek
 * @since 26/08/20 11:00 AM
 */
public class FacilityPerformanceMasterDto {

    private Integer id;

    private Date performanceDate;

    private Integer noOfOpdAttended;

    private Integer noOfIpdAttended;

    private Integer noOfDeliveresConducted;

    private Integer noOfSectionConducted;

    private Integer noOfMajorOperationConducted;

    private Integer noOfMinorOperationConducted;

    private Integer noOfLaboratoryTestConducted;

    private Integer healthInfrastructureId;

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

    public Date getPerformanceDate() {
        return performanceDate;
    }

    public void setPerformanceDate(Date performanceDate) {
        this.performanceDate = performanceDate;
    }

    public Integer getNoOfOpdAttended() {
        return noOfOpdAttended;
    }

    public void setNoOfOpdAttended(Integer noOfOpdAttended) {
        this.noOfOpdAttended = noOfOpdAttended;
    }

    public Integer getNoOfIpdAttended() {
        return noOfIpdAttended;
    }

    public void setNoOfIpdAttended(Integer noOfIpdAttended) {
        this.noOfIpdAttended = noOfIpdAttended;
    }

    public Integer getNoOfDeliveresConducted() {
        return noOfDeliveresConducted;
    }

    public void setNoOfDeliveresConducted(Integer noOfDeliveresConducted) {
        this.noOfDeliveresConducted = noOfDeliveresConducted;
    }

    public Integer getNoOfSectionConducted() {
        return noOfSectionConducted;
    }

    public void setNoOfSectionConducted(Integer noOfSectionConducted) {
        this.noOfSectionConducted = noOfSectionConducted;
    }

    public Integer getNoOfMajorOperationConducted() {
        return noOfMajorOperationConducted;
    }

    public void setNoOfMajorOperationConducted(Integer noOfMajorOperationConducted) {
        this.noOfMajorOperationConducted = noOfMajorOperationConducted;
    }

    public Integer getNoOfMinorOperationConducted() {
        return noOfMinorOperationConducted;
    }

    public void setNoOfMinorOperationConducted(Integer noOfMinorOperationConducted) {
        this.noOfMinorOperationConducted = noOfMinorOperationConducted;
    }

    public Integer getNoOfLaboratoryTestConducted() {
        return noOfLaboratoryTestConducted;
    }

    public void setNoOfLaboratoryTestConducted(Integer noOfLaboratoryTestConducted) {
        this.noOfLaboratoryTestConducted = noOfLaboratoryTestConducted;
    }

    public Integer getHealthInfrastructureId() {
        return healthInfrastructureId;
    }

    public void setHealthInfrastructureId(Integer healthInfrastructureId) {
        this.healthInfrastructureId = healthInfrastructureId;
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
