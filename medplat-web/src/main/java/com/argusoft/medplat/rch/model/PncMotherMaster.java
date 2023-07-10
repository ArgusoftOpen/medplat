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
 * Define rch_pnc_mother_master entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_pnc_mother_master")
public class PncMotherMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "pnc_master_id")
    private Integer pncMasterId;

    @Column(name = "mother_id")
    private Integer motherId;

    @Column(name = "date_of_delivery")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfDelivery;

    @Column(name = "service_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceDate;

    @Column(name = "member_status")
    private String memberStatus;

    @Column(name = "is_alive")
    private Boolean isAlive;

    @Column(name = "ifa_tablets_given")
    private Integer ifaTabletsGiven;

    @Column(name = "calcium_tablets_given")
    private Integer calciumTabletsGiven;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "rch_pnc_mother_danger_signs_rel", joinColumns = @JoinColumn(name = "mother_pnc_id"))
    @Column(name = "mother_danger_signs")
    private Set<Integer> motherDangerSigns;

    @Column(name = "other_danger_sign")
    private String otherDangerSign;

    @Column(name = "mother_referral_done")
    private String motherReferralDone;

    @Column(name = "referral_place")
    private Integer referralPlace;

    @Column(name = "family_planning_method")
    private String familyPlanningMethod;

    @Column(name = "fp_insert_operate_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fpInsertOperateDate;

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

    @Column(name = "blood_transfusion")
    private Boolean bloodTransfusion;

    @Column(name = "iron_def_anemia_inj")
    private String ironDefAnemiaInj;

    @Column(name = "iron_def_anemia_inj_due_date")
    private Date ironDefAnemiaInjDueDate;

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

    public Integer getMotherId() {
        return motherId;
    }

    public void setMotherId(Integer motherId) {
        this.motherId = motherId;
    }

    public Date getDateOfDelivery() {
        return dateOfDelivery;
    }

    public void setDateOfDelivery(Date dateOfDelivery) {
        if (dateOfDelivery != null && dateOfDelivery.after(new Date())) {
            throw new ImtechoMobileException("Delivery date cannot be future", 100);
        }
        this.dateOfDelivery = dateOfDelivery;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        if (serviceDate != null && ImtechoUtil.clearTimeFromDate(serviceDate).after(new Date())) {
            throw new ImtechoMobileException("Service date cannot be future", 100);
        }
        this.serviceDate = ImtechoUtil.clearTimeFromDate(serviceDate);
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public Boolean getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(Boolean isAlive) {
        this.isAlive = isAlive;
    }

    public Integer getIfaTabletsGiven() {
        return ifaTabletsGiven;
    }

    public void setIfaTabletsGiven(Integer ifaTabletsGiven) {
        this.ifaTabletsGiven = ifaTabletsGiven;
    }

    public Integer getCalciumTabletsGiven() {
        return calciumTabletsGiven;
    }

    public void setCalciumTabletsGiven(Integer calciumTabletsGiven) {
        this.calciumTabletsGiven = calciumTabletsGiven;
    }


    public Set<Integer> getMotherDangerSigns() {
        return motherDangerSigns;
    }

    public void setMotherDangerSigns(Set<Integer> motherDangerSigns) {
        this.motherDangerSigns = motherDangerSigns;
    }

    public String getOtherDangerSign() {
        return otherDangerSign;
    }

    public void setOtherDangerSign(String otherDangerSign) {
        this.otherDangerSign = otherDangerSign;
    }

    public String getMotherReferralDone() {
        return motherReferralDone;
    }

    public void setMotherReferralDone(String motherReferralDone) {
        this.motherReferralDone = motherReferralDone;
    }

    public Integer getReferralPlace() {
        return referralPlace;
    }

    public void setReferralPlace(Integer referralPlace) {
        this.referralPlace = referralPlace;
    }

    public String getFamilyPlanningMethod() {
        return familyPlanningMethod;
    }

    public void setFamilyPlanningMethod(String familyPlanningMethod) {
        this.familyPlanningMethod = familyPlanningMethod;
    }

    public Date getFpInsertOperateDate() {
        return fpInsertOperateDate;
    }

    public void setFpInsertOperateDate(Date fpInsertOperateDate) {
        this.fpInsertOperateDate = fpInsertOperateDate;
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

    public Boolean getBloodTransfusion() {
        return bloodTransfusion;
    }

    public void setBloodTransfusion(Boolean bloodTransfusion) {
        this.bloodTransfusion = bloodTransfusion;
    }

    public String getIronDefAnemiaInj() {
        return ironDefAnemiaInj;
    }

    public void setIronDefAnemiaInj(String ironDefAnemiaInj) {
        this.ironDefAnemiaInj = ironDefAnemiaInj;
    }

    public Date getIronDefAnemiaInjDueDate() {
        return ironDefAnemiaInjDueDate;
    }

    public void setIronDefAnemiaInjDueDate(Date ironDefAnemiaInjDueDate) {
        this.ironDefAnemiaInjDueDate = ironDefAnemiaInjDueDate;
    }

    public Integer getReferralInfraId() {
        return referralInfraId;
    }

    public void setReferralInfraId(Integer referralInfraId) {
        this.referralInfraId = referralInfraId;
    }

    /**
     * Define fields name for rch_pnc_mother_master entity.
     */
    public static class Fields {
        private Fields() {
        }

        public static final String ID = "id";
        public static final String MOTHER_ID = "motherId";
        public static final String SERVICE_DATE = "serviceDate";
        public static final String REFERRAL_PLACE = "referralPlace";
        public static final String DEATH_DATE = "deathDate";
        public static final String DEATH_REASON = "deathReason";
    }

    @Override
    public String toString() {
        return "PncMotherMaster{" +
                "id=" + id +
                ", pncMasterId=" + pncMasterId +
                ", motherId=" + motherId +
                ", dateOfDelivery=" + dateOfDelivery +
                ", serviceDate=" + serviceDate +
                ", memberStatus='" + memberStatus + '\'' +
                ", isAlive=" + isAlive +
                ", ifaTabletsGiven=" + ifaTabletsGiven +
                ", calciumTabletsGiven=" + calciumTabletsGiven +
                ", motherDangerSigns=" + motherDangerSigns +
                ", otherDangerSign='" + otherDangerSign + '\'' +
                ", motherReferralDone='" + motherReferralDone + '\'' +
                ", referralPlace=" + referralPlace +
                ", familyPlanningMethod='" + familyPlanningMethod + '\'' +
                ", fpInsertOperateDate=" + fpInsertOperateDate +
                ", deathDate=" + deathDate +
                ", placeOfDeath='" + placeOfDeath + '\'' +
                ", deathReason='" + deathReason + '\'' +
                ", otherDeathReason='" + otherDeathReason + '\'' +
                ", isHighRiskCase=" + isHighRiskCase +
                ", deathInfrastructureId=" + deathInfrastructureId +
                ", bloodTransfusion=" + bloodTransfusion +
                ", ironDefAnemiaInj='" + ironDefAnemiaInj + '\'' +
                ", ironDefAnemiaInjDueDate=" + ironDefAnemiaInjDueDate +
                ", referralInfraId=" + referralInfraId +
                '}';
    }

}
