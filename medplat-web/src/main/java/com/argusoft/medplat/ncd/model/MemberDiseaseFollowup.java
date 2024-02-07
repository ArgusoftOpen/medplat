/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.ncd.enums.DiseaseCode;
import com.argusoft.medplat.ncd.enums.ReferralPlace;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * <p>
 *     Define ncd_member_disesase_followup entity and its fields.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "ncd_member_disesase_followup")
public class MemberDiseaseFollowup extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "health_infrastructure_id")
    private Integer healthInfraId;

    @Column(name = "followup_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date followupDate;

    @Column(name = "disease_code")
    @Enumerated(EnumType.STRING)
    private DiseaseCode diseaseCode;

    @Column(name = "referral_from")
    @Enumerated(EnumType.STRING)
    private ReferralPlace referralFrom;

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
