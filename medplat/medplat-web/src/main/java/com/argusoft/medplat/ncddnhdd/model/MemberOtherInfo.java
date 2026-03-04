/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
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
 *     Define ncd_member_other_information entity and its fields.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "ncd_member_other_information")
public class MemberOtherInfo extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "excess_thirst")
    private Boolean excessThirst;

    @Column(name = "excess_urination")
    private Boolean excessUrination;

    @Column(name = "excess_hunger")
    private Boolean excessHunger;

    @Column(name = "recurrent_skin")
    private Boolean recurrentSkin;

    @Column(name = "delay_in_healing")
    private Boolean delayInHealing;

    @Column(name = "change_in_dietery_habits")
    private Boolean changeInDieteryHabits;

    @Column(name = "visual_disturbances_history_or_present")
    private Boolean visualDisturbancesHistoryOrPresent;

    @Column(name = "significant_edema")
    private Boolean significantEdema;

    @Column(name = "angina")
    private Boolean angina;

    @Column(name = "intermittent_claudication")
    private Boolean intermittentClaudication;

    @Column(name = "limpness")
    private Boolean limpness;

    @Column(name = "mo_screening_done")
    private Boolean isMoScreeningDone;

    @Column(name = "done_by", length = 200)
    @Enumerated(EnumType.STRING)
    private DoneBy doneBy;

    @Column(name = "done_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doneOn;
    
    @Column(name="confirmed_case_of_diabetes")
    private Boolean confirmedCaseOfDiabetes;
    
    
    @Column(name="confirmed_case_of_hypertension")
    private Boolean confirmedCaseOfHyperTension;
    
    @Column(name="confirmed_case_of_copd")
    private Boolean confirmedCaseOfCopd;

    @Column(name="history_of_heart_attack")
    private Boolean historyOfHeartAttack;
    
    @Column(name="history_of_stroke")
    private Boolean historyOfStroke;
    
    @Column(name="family_history_of_diabetes")
    private Boolean familyHistoryOfDiabetes;
    
    
    @Column(name="family_history_of_premature_mi")
    private Boolean familyHistoryOfPrematureMi;
    
    
    @Column(name="family_history_of_stroke")
    private Boolean familyHistoryOfStroke;
    
    @Column(name="is_report_verified")
    private Boolean isReportVerified;
    
    @Column(name="smoke_tobacco")
    private Boolean smokeTobacco;
    
    @Column(name="smokeless_tobacco")
    private Boolean smokelessTobacco;
    
    @Column(name="alcohol_consumption")
    private String alcoholConsumption;
    
    @Column(name="referral_id")
    private Integer referralId;

    public Integer getReferralId() {
        return referralId;
    }

    public void setReferralId(Integer referralId) {
        this.referralId = referralId;
    }
    
    
    
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

    public Boolean getExcessThirst() {
        return excessThirst;
    }

    public void setExcessThirst(Boolean excessThirst) {
        this.excessThirst = excessThirst;
    }

    public Boolean getExcessUrination() {
        return excessUrination;
    }

    public void setExcessUrination(Boolean excessUrination) {
        this.excessUrination = excessUrination;
    }

    public Boolean getExcessHunger() {
        return excessHunger;
    }

    public void setExcessHunger(Boolean excessHunger) {
        this.excessHunger = excessHunger;
    }

    public Boolean getRecurrentSkin() {
        return recurrentSkin;
    }

    public void setRecurrentSkin(Boolean recurrentSkin) {
        this.recurrentSkin = recurrentSkin;
    }

    public Boolean getDelayInHealing() {
        return delayInHealing;
    }

    public void setDelayInHealing(Boolean delayInHealing) {
        this.delayInHealing = delayInHealing;
    }

    public Boolean getChangeInDieteryHabits() {
        return changeInDieteryHabits;
    }

    public void setChangeInDieteryHabits(Boolean changeInDieteryHabits) {
        this.changeInDieteryHabits = changeInDieteryHabits;
    }

    public Boolean getVisualDisturbancesHistoryOrPresent() {
        return visualDisturbancesHistoryOrPresent;
    }

    public void setVisualDisturbancesHistoryOrPresent(Boolean visualDisturbancesHistoryOrPresent) {
        this.visualDisturbancesHistoryOrPresent = visualDisturbancesHistoryOrPresent;
    }

    public Boolean getSignificantEdema() {
        return significantEdema;
    }

    public void setSignificantEdema(Boolean significantEdema) {
        this.significantEdema = significantEdema;
    }

    public Boolean getAngina() {
        return angina;
    }

    public void setAngina(Boolean angina) {
        this.angina = angina;
    }

    public Boolean getIntermittentClaudication() {
        return intermittentClaudication;
    }

    public void setIntermittentClaudication(Boolean intermittentClaudication) {
        this.intermittentClaudication = intermittentClaudication;
    }

    public Boolean getLimpness() {
        return limpness;
    }

    public void setLimpness(Boolean limpness) {
        this.limpness = limpness;
    }

    public Boolean getIsMoScreeningDone() {
        return isMoScreeningDone;
    }

    public void setIsMoScreeningDone(Boolean isMoScreeningDone) {
        this.isMoScreeningDone = isMoScreeningDone;
    }

    public DoneBy getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(DoneBy doneBy) {
        this.doneBy = doneBy;
    }

    public Date getDoneOn() {
        return doneOn;
    }

    public void setDoneOn(Date doneOn) {
        this.doneOn = doneOn;
    }

    public Boolean getConfirmedCaseOfDiabetes() {
        return confirmedCaseOfDiabetes;
    }

    public void setConfirmedCaseOfDiabetes(Boolean confirmedCaseOfDiabetes) {
        this.confirmedCaseOfDiabetes = confirmedCaseOfDiabetes;
    }

    public Boolean getConfirmedCaseOfHyperTension() {
        return confirmedCaseOfHyperTension;
    }

    public void setConfirmedCaseOfHyperTension(Boolean confirmedCaseOfHyperTension) {
        this.confirmedCaseOfHyperTension = confirmedCaseOfHyperTension;
    }

    public Boolean getConfirmedCaseOfCopd() {
        return confirmedCaseOfCopd;
    }

    public void setConfirmedCaseOfCopd(Boolean confirmedCaseOfCopd) {
        this.confirmedCaseOfCopd = confirmedCaseOfCopd;
    }

    public Boolean getHistoryOfHeartAttack() {
        return historyOfHeartAttack;
    }

    public void setHistoryOfHeartAttack(Boolean historyOfHeartAttack) {
        this.historyOfHeartAttack = historyOfHeartAttack;
    }

    public Boolean getHistoryOfStroke() {
        return historyOfStroke;
    }

    public void setHistoryOfStroke(Boolean historyOfStroke) {
        this.historyOfStroke = historyOfStroke;
    }


    public Boolean getFamilyHistoryOfDiabetes() {
        return familyHistoryOfDiabetes;
    }

    public void setFamilyHistoryOfDiabetes(Boolean familyHistoryOfDiabetes) {
        this.familyHistoryOfDiabetes = familyHistoryOfDiabetes;
    }

    public Boolean getFamilyHistoryOfPrematureMi() {
        return familyHistoryOfPrematureMi;
    }

    public void setFamilyHistoryOfPrematureMi(Boolean familyHistoryOfPrematureMi) {
        this.familyHistoryOfPrematureMi = familyHistoryOfPrematureMi;
    }

    public Boolean getFamilyHistoryOfStroke() {
        return familyHistoryOfStroke;
    }

    public void setFamilyHistoryOfStroke(Boolean familyHistoryOfStroke) {
        this.familyHistoryOfStroke = familyHistoryOfStroke;
    }

    public Boolean getIsReportVerified() {
        return isReportVerified;
    }

    public void setIsReportVerified(Boolean isReportVerified) {
        this.isReportVerified = isReportVerified;
    }

    public Boolean getSmokeTobacco() {
        return smokeTobacco;
    }

    public void setSmokeTobacco(Boolean smokeTobacco) {
        this.smokeTobacco = smokeTobacco;
    }

    public Boolean getSmokelessTobacco() {
        return smokelessTobacco;
    }

    public void setSmokelessTobacco(Boolean smokelessTobacco) {
        this.smokelessTobacco = smokelessTobacco;
    }

    public String getAlcoholConsumption() {
        return alcoholConsumption;
    }

    public void setAlcoholConsumption(String alcoholConsumption) {
        this.alcoholConsumption = alcoholConsumption;
    }

    
    public enum DoneBy{
        MO, FHW
    }
}
