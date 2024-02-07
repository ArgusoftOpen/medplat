/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dto;

import com.argusoft.medplat.ncd.enums.DiseaseCode;
import com.argusoft.medplat.ncd.enums.ReferralPlace;
import java.util.Date;

/**
 *
 * <p>
 *     Used for member disease follow up.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class MemberDiseaseFollowupDto {
    private Integer id;

    private Integer memberId;

    private Date followupDate;

    private DiseaseCode diseaseCode;
    
    private Integer createdBy;
    
    private Date createdOn;
    
    private ReferralPlace referralFrom;
    
    private Integer healthInfraId;

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

    public Date getFollowupDate() {
        return followupDate;
    }

    public void setFollowupDate(Date followupDate) {
        this.followupDate = followupDate;
    }

    public DiseaseCode getDiseaseCode() {
        return diseaseCode;
    }

    public void setDiseaseCode(DiseaseCode diseaseCode) {
        this.diseaseCode = diseaseCode;
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

    public ReferralPlace getReferralFrom() {
        return referralFrom;
    }

    public void setReferralFrom(ReferralPlace referralFrom) {
        this.referralFrom = referralFrom;
    }

    public Integer getHealthInfraId() {
        return healthInfraId;
    }

    public void setHealthInfraId(Integer healthInfraId) {
        this.healthInfraId = healthInfraId;
    }
    
    
    
}
