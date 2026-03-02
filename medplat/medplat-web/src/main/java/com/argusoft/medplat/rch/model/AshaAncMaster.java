/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Define rch_asha_anc_master entity and its fields.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_asha_anc_master")
public class AshaAncMaster extends VisitCommonFields implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "pregnancy_reg_det_id")
    private Integer pregnancyRegDetId;

    @Column(name = "service_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date serviceDate;

    @Column(name = "member_status")
    private String memberStatus;

    @Column(name = "is_alive")
    private Boolean isAlive;

    @Column(name = "death_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date deathDate;

    @Column(name = "death_place")
    private String deathPlace;

    @Column(name = "death_reason")
    private String deathReason;

    @Column(name = "other_death_reason")
    private String otherDeathReason;

    @Column(name = "family_understands")
    private Boolean familyUnderstands;

    @Column(name = "trained_dai_introduced")
    private Boolean trainedDaiIntroduced;

    @Column(name = "planned_delivery_at")
    private String plannedDeliveryAt;

    @Column(name = "expected_delivery_place")
    private String expectedDeliveryPlace;

    @Column(name = "family_understands_danger_signs")
    private Boolean familyUnderstandsDangerSigns;

    @Column(name = "money_arrangements")
    private Boolean moneyArrangements;

    @Column(name = "accompany_identified")
    private Boolean accompanyIdentified;

    @Column(name = "headache")
    private Boolean headache;

    @Column(name = "visual_disturbances")
    private Boolean visualDisturbances;

    @Column(name = "convulsions")
    private Boolean convulsions;

    @Column(name = "cough")
    private String cough;

    @Column(name = "fever")
    private Boolean fever;

    @Column(name = "chills_rigors")
    private Boolean chillsRigors;

    @Column(name = "jaundice")
    private Boolean jaundice;

    @Column(name = "vomitting")
    private Boolean vomitting;

    @Column(name = "vaginal_bleeding")
    private Boolean vaginalBleeding;

    @Column(name = "burning_urination")
    private Boolean burningUrination;

    @Column(name = "severe_joint_pain")
    private Boolean severeJointPain;

    @Column(name = "vaginal_discharge")
    private String vaginalDischarge;

    @Column(name = "leaking_per_vaginally")
    private Boolean leakingPerVaginally;

    @Column(name = "easily_tired")
    private Boolean easilyTired;

    @Column(name = "short_of_breath")
    private Boolean shortOfBreath;

    @Column(name = "swelling_feet_hands_face")
    private Boolean swellingFeetHandsFace;

    @Column(name = "visited_doctor_since_last_visit")
    private Boolean visitedDoctorSinceLastVisit;

    @Column(name = "doctor_visit_date")
    @Temporal(TemporalType.DATE)
    private Date doctorVisitDate;

    @Column(name = "ifa_tablets_taken")
    private Integer ifaTabletsTaken;

    @Column(name = "conjuctiva_and_palms_pale")
    private Boolean conjuctivaAndPalmsPale;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPregnancyRegDetId() {
        return pregnancyRegDetId;
    }

    public void setPregnancyRegDetId(Integer pregnancyRegDetId) {
        this.pregnancyRegDetId = pregnancyRegDetId;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
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

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public String getDeathPlace() {
        return deathPlace;
    }

    public void setDeathPlace(String deathPlace) {
        this.deathPlace = deathPlace;
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

    public Boolean getFamilyUnderstands() {
        return familyUnderstands;
    }

    public void setFamilyUnderstands(Boolean familyUnderstands) {
        this.familyUnderstands = familyUnderstands;
    }

    public Boolean getTrainedDaiIntroduced() {
        return trainedDaiIntroduced;
    }

    public void setTrainedDaiIntroduced(Boolean trainedDaiIntroduced) {
        this.trainedDaiIntroduced = trainedDaiIntroduced;
    }

    public String getPlannedDeliveryAt() {
        return plannedDeliveryAt;
    }

    public void setPlannedDeliveryAt(String plannedDeliveryAt) {
        this.plannedDeliveryAt = plannedDeliveryAt;
    }

    public String getExpectedDeliveryPlace() {
        return expectedDeliveryPlace;
    }

    public void setExpectedDeliveryPlace(String expectedDeliveryPlace) {
        this.expectedDeliveryPlace = expectedDeliveryPlace;
    }

    public Boolean getFamilyUnderstandsDangerSigns() {
        return familyUnderstandsDangerSigns;
    }

    public void setFamilyUnderstandsDangerSigns(Boolean familyUnderstandsDangerSigns) {
        this.familyUnderstandsDangerSigns = familyUnderstandsDangerSigns;
    }

    public Boolean getMoneyArrangements() {
        return moneyArrangements;
    }

    public void setMoneyArrangements(Boolean moneyArrangements) {
        this.moneyArrangements = moneyArrangements;
    }

    public Boolean getAccompanyIdentified() {
        return accompanyIdentified;
    }

    public void setAccompanyIdentified(Boolean accompanyIdentified) {
        this.accompanyIdentified = accompanyIdentified;
    }

    public Boolean getHeadache() {
        return headache;
    }

    public void setHeadache(Boolean headache) {
        this.headache = headache;
    }

    public Boolean getVisualDisturbances() {
        return visualDisturbances;
    }

    public void setVisualDisturbances(Boolean visualDisturbances) {
        this.visualDisturbances = visualDisturbances;
    }

    public Boolean getConvulsions() {
        return convulsions;
    }

    public void setConvulsions(Boolean convulsions) {
        this.convulsions = convulsions;
    }

    public String getCough() {
        return cough;
    }

    public void setCough(String cough) {
        this.cough = cough;
    }

    public Boolean getFever() {
        return fever;
    }

    public void setFever(Boolean fever) {
        this.fever = fever;
    }

    public Boolean getChillsRigors() {
        return chillsRigors;
    }

    public void setChillsRigors(Boolean chillsRigors) {
        this.chillsRigors = chillsRigors;
    }

    public Boolean getJaundice() {
        return jaundice;
    }

    public void setJaundice(Boolean jaundice) {
        this.jaundice = jaundice;
    }

    public Boolean getVomitting() {
        return vomitting;
    }

    public void setVomitting(Boolean vomitting) {
        this.vomitting = vomitting;
    }

    public Boolean getVaginalBleeding() {
        return vaginalBleeding;
    }

    public void setVaginalBleeding(Boolean vaginalBleeding) {
        this.vaginalBleeding = vaginalBleeding;
    }

    public Boolean getBurningUrination() {
        return burningUrination;
    }

    public void setBurningUrination(Boolean burningUrination) {
        this.burningUrination = burningUrination;
    }

    public Boolean getSevereJointPain() {
        return severeJointPain;
    }

    public void setSevereJointPain(Boolean severeJointPain) {
        this.severeJointPain = severeJointPain;
    }

    public String getVaginalDischarge() {
        return vaginalDischarge;
    }

    public void setVaginalDischarge(String vaginalDischarge) {
        this.vaginalDischarge = vaginalDischarge;
    }

    public Boolean getLeakingPerVaginally() {
        return leakingPerVaginally;
    }

    public void setLeakingPerVaginally(Boolean leakingPerVaginally) {
        this.leakingPerVaginally = leakingPerVaginally;
    }

    public Boolean getEasilyTired() {
        return easilyTired;
    }

    public void setEasilyTired(Boolean easilyTired) {
        this.easilyTired = easilyTired;
    }

    public Boolean getShortOfBreath() {
        return shortOfBreath;
    }

    public void setShortOfBreath(Boolean shortOfBreath) {
        this.shortOfBreath = shortOfBreath;
    }

    public Boolean getSwellingFeetHandsFace() {
        return swellingFeetHandsFace;
    }

    public void setSwellingFeetHandsFace(Boolean swellingFeetHandsFace) {
        this.swellingFeetHandsFace = swellingFeetHandsFace;
    }

    public Boolean getVisitedDoctorSinceLastVisit() {
        return visitedDoctorSinceLastVisit;
    }

    public void setVisitedDoctorSinceLastVisit(Boolean visitedDoctorSinceLastVisit) {
        this.visitedDoctorSinceLastVisit = visitedDoctorSinceLastVisit;
    }

    public Date getDoctorVisitDate() {
        return doctorVisitDate;
    }

    public void setDoctorVisitDate(Date doctorVisitDate) {
        this.doctorVisitDate = doctorVisitDate;
    }

    public Integer getIfaTabletsTaken() {
        return ifaTabletsTaken;
    }

    public void setIfaTabletsTaken(Integer ifaTabletsTaken) {
        this.ifaTabletsTaken = ifaTabletsTaken;
    }

    public Boolean getConjuctivaAndPalmsPale() {
        return conjuctivaAndPalmsPale;
    }

    public void setConjuctivaAndPalmsPale(Boolean conjuctivaAndPalmsPale) {
        this.conjuctivaAndPalmsPale = conjuctivaAndPalmsPale;
    }

}
