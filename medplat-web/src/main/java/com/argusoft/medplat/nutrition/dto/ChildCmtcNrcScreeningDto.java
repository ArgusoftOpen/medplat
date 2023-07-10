
package com.argusoft.medplat.nutrition.dto;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * <p>
 *     Defines fields for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) screening
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
public class ChildCmtcNrcScreeningDto {

    private Integer id;

    private Integer childId;

    private Integer admissionId;

    private Integer dischargeId;

    private Date screenedOn;

    private Integer locationId;

    private String state;

    private Date serverDate;

    private Integer screeningCenter;

    private Boolean isCaseCompleted;

    private LinkedHashMap<String, Object> result;

    private Integer referredFrom;

    private Integer referredTo;

    private Date referredDate;

    private Boolean isArchive;

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

    public Date getScreenedOn() {
        return screenedOn;
    }

    public void setScreenedOn(Date screenedOn) {
        this.screenedOn = screenedOn;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LinkedHashMap<String, Object> getResult() {
        return result;
    }

    public void setResult(LinkedHashMap<String, Object> result) {
        this.result = result;
    }

    public Integer getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(Integer admissionId) {
        this.admissionId = admissionId;
    }

    public Date getServerDate() {
        return serverDate;
    }

    public void setServerDate(Date serverDate) {
        this.serverDate = serverDate;
    }

    public Integer getDischargeId() {
        return dischargeId;
    }

    public void setDischargeId(Integer dischargeId) {
        this.dischargeId = dischargeId;
    }

    public Integer getScreeningCenter() {
        return screeningCenter;
    }

    public void setScreeningCenter(Integer screeningCenter) {
        this.screeningCenter = screeningCenter;
    }

    public Boolean getIsCaseCompleted() {
        return isCaseCompleted;
    }

    public void setIsCaseCompleted(Boolean isCaseCompleted) {
        this.isCaseCompleted = isCaseCompleted;
    }

    public Integer getReferredFrom() {
        return referredFrom;
    }

    public void setReferredFrom(Integer referredFrom) {
        this.referredFrom = referredFrom;
    }

    public Date getReferredDate() {
        return referredDate;
    }

    public void setReferredDate(Date referredDate) {
        this.referredDate = referredDate;
    }

    public Integer getReferredTo() {
        return referredTo;
    }

    public void setReferredTo(Integer referredTo) {
        this.referredTo = referredTo;
    }

    public Boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }
}
