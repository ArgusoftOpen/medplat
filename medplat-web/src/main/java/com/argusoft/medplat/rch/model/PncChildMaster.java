/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.exception.ImtechoMobileException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * <p>
 * Define rch_pnc_child_master entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_pnc_child_master")
public class PncChildMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "pnc_master_id")
    private Integer pncMasterId;

    @Column(name = "child_id")
    private Integer childId;

    @Column(name = "is_alive")
    private Boolean isAlive;

    @Column(name = "member_status")
    private String memberStatus;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "rch_pnc_child_danger_signs_rel", joinColumns = @JoinColumn(name = "child_pnc_id"))
    @Column(name = "child_danger_signs")
    private Set<Integer> childDangerSigns;

    @Column(name = "other_danger_sign")
    private String otherDangerSign;

    @Column(name = "child_referral_done")
    private String childReferralDone;

    @Column(name = "referral_place")
    private Integer referralPlace;

    @Column(name = "child_weight", columnDefinition = "numeric", precision = 7, scale = 3)
    private Float childWeight;

    @Column(name = "death_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deathDate;

    @Column(name = "place_of_death")
    private String placeOfDeath;

    @Column(name = "death_reason")
    private String deathReason;

    @Column(name = "other_death_reason")
    private String otherDeathReason;

    @Column(name = "is_high_risk_case")
    private Boolean isHighRiskCase;

    @Column(name = "death_infra_id")
    private Integer deathInfrastructureId;

    @Column(name = "referral_infra_id")
    private Integer referralInfraId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPncMasterId() {
        return pncMasterId;
    }

    public void setPncMasterId(Integer pncMasterId) {
        this.pncMasterId = pncMasterId;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public Boolean getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(Boolean isAlive) {
        this.isAlive = isAlive;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public Set<Integer> getChildDangerSigns() {
        return childDangerSigns;
    }

    public void setChildDangerSigns(Set<Integer> childDangerSigns) {
        this.childDangerSigns = childDangerSigns;
    }

    public String getOtherDangerSign() {
        return otherDangerSign;
    }

    public void setOtherDangerSign(String otherDangerSign) {
        this.otherDangerSign = otherDangerSign;
    }

    public String getChildReferralDone() {
        return childReferralDone;
    }

    public void setChildReferralDone(String childReferralDone) {
        this.childReferralDone = childReferralDone;
    }

    public Integer getReferralPlace() {
        return referralPlace;
    }

    public void setReferralPlace(Integer referralPlace) {
        this.referralPlace = referralPlace;
    }

    public Float getChildWeight() {
        return childWeight;
    }

    public void setChildWeight(Float childWeight) {
        this.childWeight = childWeight;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        if (deathDate != null && ImtechoUtil.clearTimeFromDate(deathDate).after(new Date())) {
            throw new ImtechoMobileException("Death date cannot be future", 100);
        }
        this.deathDate = ImtechoUtil.clearTimeFromDate(deathDate);
    }

    public String getPlaceOfDeath() {
        return placeOfDeath;
    }

    public void setPlaceOfDeath(String placeOfDeath) {
        this.placeOfDeath = placeOfDeath;
    }

    public String getDeathReason() {
        return deathReason;
    }

    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }

    public String getOtherDeathReason() {
        return otherDeathReason;
    }

    public void setOtherDeathReason(String otherDeathReason) {
        this.otherDeathReason = otherDeathReason;
    }

    public Boolean getIsHighRiskCase() {
        return isHighRiskCase;
    }

    public void setIsHighRiskCase(Boolean isHighRiskCase) {
        this.isHighRiskCase = isHighRiskCase;
    }

    public Integer getDeathInfrastructureId() {
        return deathInfrastructureId;
    }

    public void setDeathInfrastructureId(Integer deathInfrastructureId) {
        this.deathInfrastructureId = deathInfrastructureId;
    }

    public Integer getReferralInfraId() {
        return referralInfraId;
    }

    public void setReferralInfraId(Integer referralInfraId) {
        this.referralInfraId = referralInfraId;
    }

    /**
     * Define fields name for rch_pnc_child_master entity.
     */
    public static class Fields {
        private Fields() {
        }

        public static final String ID = "id";
        public static final String CHILD_ID = "childId";
        public static final String REFERRAL_PLACE = "referralPlace";
        public static final String DEATH_DATE = "deathDate";
        public static final String DEATH_REASON = "deathReason";
    }

    @Override
    public String toString() {
        return "PncChildMaster{" +
                "id=" + id +
                ", pncMasterId=" + pncMasterId +
                ", childId=" + childId +
                ", isAlive=" + isAlive +
                ", memberStatus='" + memberStatus + '\'' +
                ", childDangerSigns=" + childDangerSigns +
                ", otherDangerSign='" + otherDangerSign + '\'' +
                ", childReferralDone='" + childReferralDone + '\'' +
                ", referralPlace=" + referralPlace +
                ", childWeight=" + childWeight +
                ", deathDate=" + deathDate +
                ", placeOfDeath='" + placeOfDeath + '\'' +
                ", deathReason='" + deathReason + '\'' +
                ", otherDeathReason='" + otherDeathReason + '\'' +
                ", isHighRiskCase=" + isHighRiskCase +
                ", deathInfrastructureId=" + deathInfrastructureId +
                ", referralInfraId=" + referralInfraId +
                '}';
    }
}
