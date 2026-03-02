/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.ncddnhdd.enums.DiseaseCode;
import com.argusoft.medplat.ncddnhdd.enums.ReferralPlace;
import com.argusoft.medplat.ncddnhdd.enums.State;
import com.argusoft.medplat.ncddnhdd.enums.Status;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * <p>
 *     Define ncd_member_referral entity and its fields.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "ncd_member_referral")
public class MemberReferral extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "referred_by")
    private Integer referredBy;

    @Column(name = "referred_to")
    @Enumerated(EnumType.STRING)
    private ReferralPlace referredTo;

    @Column(name = "referred_from")
    @Enumerated(EnumType.STRING)
    private ReferralPlace referredFrom;

    @Column(name = "referred_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date referredOn;

    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "disease_code")
    @Enumerated(EnumType.STRING)
    private DiseaseCode diseaseCode;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "reason")
    private String reason;

    @Column(name = "health_infrastructure_id")
    private Integer healthInfraId;
    @Column(name = "mo_referred_health_infra_type")
    private String healthInfraType;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "follow_up_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date followUpDate;

    @Column(name = "referred_from_health_infrastructure_id")
    private Integer referredFromHealthInfrastructureId;

    @Column(name = "member_location")
    private Integer memberLocation;

    @Column(name = "pvt_health_infra_name")
    private String pvtHealthInfraName;

    public String getHealthInfraType() {
        return healthInfraType;
    }

    public void setHealthInfraType(String healthInfraType) {
        this.healthInfraType = healthInfraType;
    }


    public String getPvtHealthInfraName() {
        return pvtHealthInfraName;
    }

    public void setPvtHealthInfraName(String pvtHealthInfraName) {
        this.pvtHealthInfraName = pvtHealthInfraName;
    }

    public Integer getHealthInfraId() {
        return healthInfraId;
    }

    public void setHealthInfraId(Integer healthInfraId) {
        this.healthInfraId = healthInfraId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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

    public ReferralPlace getReferredFrom() {
        return referredFrom;
    }

    public void setReferredFrom(ReferralPlace referredFrom) {
        this.referredFrom = referredFrom;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(Date followUpDate) {
        this.followUpDate = followUpDate;
    }

    public Integer getReferredFromHealthInfrastructureId() {
        return referredFromHealthInfrastructureId;
    }

    public void setReferredFromHealthInfrastructureId(Integer referredFromHealthInfrastructureId) {
        this.referredFromHealthInfrastructureId = referredFromHealthInfrastructureId;
    }

    public Integer getMemberLocation() {
        return memberLocation;
    }

    public void setMemberLocation(Integer memberLocation) {
        this.memberLocation = memberLocation;
    }

    /**
     * Define fields name for ncd_member_referral.
     */
    public static class Fields {
        private Fields() {
            throw new IllegalStateException("Utility class");
        }
        public static final String ID = "id";
        public static final String LOCATION_ID = "locationId";
        public static final String DISEASE_CODE = "diseaseCode";
        public static final String STATE = "state";
        public static final String MEMBER_ID = "memberId";
        public static final String STATUS = "status";
        public static final String REFERRED_ON  = "referredOn";
        public static final String HEALTH_INFRA_ID  = "healthInfraId";
    }
}

