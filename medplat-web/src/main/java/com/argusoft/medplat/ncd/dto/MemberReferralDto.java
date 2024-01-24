/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dto;

import com.argusoft.medplat.ncd.enums.DiseaseCode;
import com.argusoft.medplat.ncd.enums.ReferralPlace;
import com.argusoft.medplat.ncd.enums.State;
import java.util.Date;

/**
 *
 * <p>
 *     Used for member referral.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class MemberReferralDto {
    
    private Integer id;

    private Integer referredBy;

    private ReferralPlace referredTo;

    private ReferralPlace referredFrom;

    private Date referredOn;

    private Integer locationId;

    private DiseaseCode diseaseCode;
    
    private String diseaseName;

    private Integer memberId;
    
    private String reason;
    
    private State state;
    
    private Integer previousRefferalId;
    
    private Integer healthInfraId;

    private String healthInfraName;
    public Integer getHealthInfraId() {
        return healthInfraId;
    }

    public void setHealthInfraId(Integer healthInfraId) {
        this.healthInfraId = healthInfraId;
    }
    
    public Integer getPreviousRefferalId() {
        return previousRefferalId;
    }

    public void setPreviousRefferalId(Integer previousRefferalId) {
        this.previousRefferalId = previousRefferalId;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(Integer referredBy) {
        this.referredBy = referredBy;
    }

    public ReferralPlace getReferredTo() {
        return referredTo;
    }

    public void setReferredTo(ReferralPlace referredTo) {
        this.referredTo = referredTo;
    }

    public ReferralPlace getReferredFrom() {
        return referredFrom;
    }

    public void setReferredFrom(ReferralPlace referredFrom) {
        this.referredFrom = referredFrom;
    }

    public Date getReferredOn() {
        return referredOn;
    }

    public void setReferredOn(Date referredOn) {
        this.referredOn = referredOn;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public DiseaseCode getDiseaseCode() {
        return diseaseCode;
    }

    public void setDiseaseCode(DiseaseCode diseaseCode) {
        this.diseaseCode = diseaseCode;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getHealthInfraName() {
        return healthInfraName;
    }

    public void setHealthInfraName(String healthInfraName) {
        this.healthInfraName = healthInfraName;
    }
    
    
    
    
}
