package com.argusoft.medplat.nutrition.dto;

import com.argusoft.medplat.nutrition.enums.Identification;
import com.argusoft.medplat.nutrition.enums.State;

import java.util.Date;

/**
 * <p>
 *     Defines fields for Child Nutrition CMAM
 * </p>
 * @author smeet
 * @since 09/09/2020 5:30
 */
public class ChildNutritionCmamMasterDto {

    private Integer id;
    private Integer childId;
    private Integer locationId;
    private State state;
    private Date serviceDate;
    private Identification identifiedFrom;
    private Integer referenceId;
    private Boolean isCaseCompleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public Identification getIdentifiedFrom() {
        return identifiedFrom;
    }

    public void setIdentifiedFrom(Identification identifiedFrom) {
        this.identifiedFrom = identifiedFrom;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public Boolean getCaseCompleted() {
        return isCaseCompleted;
    }

    public void setCaseCompleted(Boolean caseCompleted) {
        isCaseCompleted = caseCompleted;
    }
}
