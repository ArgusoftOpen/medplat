/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dto;

import com.argusoft.medplat.ncd.enums.ReferralPlace;
import com.argusoft.medplat.ncd.model.MemberOtherInfo;
import java.util.Date;

/**
 *
 * <p>
 *     Used for member other info.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class MemberOtherInfoDto {

    private Integer memberId;

    private Integer id;

    private Boolean excessThirst;

    private Boolean excessUrination;

    private Boolean excessHunger;

    private Boolean recurrentSkin;

    private Boolean delayInHealing;

    private Boolean changeInDieteryHabits;

    private Boolean visualDisturbancesHistoryOrPresent;

    private Boolean significantEdema;

    private Boolean angina;

    private Boolean intermittentClaudication;

    private Boolean limpness;

    private Boolean isMoScreeningDone;

    private MemberOtherInfo.DoneBy doneBy;

    private Boolean confirmedCaseOfDiabetes;

    private Boolean confirmedCaseOfHyperTension;

    private Boolean confirmedCaseOfCopd;

    private Boolean historyOfHeartAttack;

    private Boolean historyOfStroke;

    private Boolean familyHistoryOfDiabetes;

    private Boolean familyHistoryOfPrematureMi;

    private Boolean familyHistoryOfStroke;

    private Boolean isReportVerified;

    private Boolean breathlessness;

    private Boolean lossOfWeight;

    private Date createdOn;

    private Integer createdBy;

    private Date doneOn;

    private Boolean smokeTobacco;

    private Boolean smokelessTobacco;

    private String alcoholConsumption;

    private Integer referralId;
    
    
    private ReferralPlace refTo;
    private ReferralPlace refFrom;

    public ReferralPlace getRefTo() {
        return refTo;
    }

    public void setRefTo(ReferralPlace refTo) {
        this.refTo = refTo;
    }

    public ReferralPlace getRefFrom() {
        return refFrom;
    }

    public void setRefFrom(ReferralPlace refFrom) {
        this.refFrom = refFrom;
    }
    


    public Integer getReferralId() {
        return referralId;
    }

    public void setReferralId(Integer referralId) {
        this.referralId = referralId;
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

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MemberOtherInfo.DoneBy getDoneBy() {
        return doneBy;
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

    public void setDoneBy(MemberOtherInfo.DoneBy doneBy) {
        this.doneBy = doneBy;
    }

    public Boolean getBreathlessness() {
        return breathlessness;
    }

    public void setBreathlessness(Boolean breathlessness) {
        this.breathlessness = breathlessness;
    }

    public Boolean getLossOfWeight() {
        return lossOfWeight;
    }

    public void setLossOfWeight(Boolean lossOfWeight) {
        this.lossOfWeight = lossOfWeight;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getDoneOn() {
        return doneOn;
    }

    public void setDoneOn(Date doneOn) {
        this.doneOn = doneOn;
    }

}
