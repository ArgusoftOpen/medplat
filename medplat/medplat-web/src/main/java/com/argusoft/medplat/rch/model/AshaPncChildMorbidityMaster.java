package com.argusoft.medplat.rch.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Define rch_asha_pnc_child_morbidity_master entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_asha_pnc_child_morbidity_master")
public class AshaPncChildMorbidityMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Column(name = "member_id")
    private Integer memberId;
    @Column(name = "family_id")
    private Integer familyId;
    @Column(name = "location_id")
    private Integer locationId;
    @Column(name = "pnc_id")
    private Integer pncId;
    @Column(name = "latitude")
    private String latitude;
    @Column(name = "longitude")
    private String longitude;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "mobile_start_date")
    private Date mobileStartDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "mobile_end_date")
    private Date mobileEndDate;
    @Column(name = "family_understand")
    private Boolean familyUnderstand;
    @Column(name = "morbidity_found")
    private Boolean morbidityFound;

    // 53
    @Column(name = "ready_for_referral")
    private Boolean readyForReferral;
    // 54

    @Column(name = "able_to_call_108")
    private Boolean ableToCall108;
    // 1000
    @Column(name = "call_log")
    private String callLog;
    // 56, 60, 64
    @Column(name = "accompany_child")
    private Boolean accompanyChild;
    // 57, 61, 65
    @Column(name = "referral_slip_given")
    private Boolean referralSlipGiven;
    // 58, 62, 66
    @Column(name = "referral_place")
    private String referralPlace;
    // 59, 63, 67
    @Column(name = "referral_vehicle")
    private String referralVehicle;

    // 309
    @Column(name = "sepsis_cotri_understand")
    private Boolean sepsisCotriUnderstand;
    // 300, 310
    @Column(name = "sepsis_cotri_given")
    private String sepsisCotriGiven;
    // 308, 314
    @Column(name = "sepsis_warm_understand")
    private Boolean sepsisWarmUnderstand;
    // 305, 316
    @Column(name = "sepsis_breast_feeding_understand")
    private Boolean sepsisBreastFeedingUnderstand;
    // 303, 320
    @Column(name = "sepsis_syrup_pcm_given")
    private String sepsisSyrupPcmGiven;
    // 323
    @Column(name = "sepsis_gv_lotion")
    private String sepsisGvLotion;
    // 328, 339, 639
    @Column(name = "vlbw_breast_feeding_understand")
    private Boolean vlbwBreastFeedingUnderstand;
    // 331. 351
    @Column(name = "vlbw_warm_understand")
    private Boolean vlbwWarmUnderstand;
    // 640
    @Column(name = "vlbw_kmc_understand")
    private Boolean vlbwKmcUnderstand;
    // 352
    @Column(name = "vlbw_call_understand")
    private Boolean vlbwCallUnderstand;
    // 357
    @Column(name = "bleeding_warm_understand")
    private Boolean bleedingWarmUnderstand;
    // 359
    @Column(name = "bleeding_breast_feeding_understand")
    private Boolean bleedingBreastFeedingUnderstand;
    // 361, 363
    @Column(name = "jaundice_breast_feeding_understand")
    private Boolean jaundiceBreastFeedingUnderstand;
    // 366
    @Column(name = "jaundice_warm_understand")
    private Boolean jaundiceWarmUnderstand;
    // 381, 372
    @Column(name = "diarrhoea_ors_understand")
    private Boolean diarrhoeaOrsUnderstand;
    // 384, 375
    @Column(name = "diarrhoea_breast_feeding_understand")
    private Boolean diarrhoeaBreastFeedingUnderstand;
    // 385
    @Column(name = "diarrhoea_call_understand")
    private Boolean diarrhoeaCallUnderstand;
    // 502
    @Column(name = "pneumonia_cotri_understand")
    private Boolean pneumoniaCotriUnderstand;
    // 503
    @Column(name = "pneumonia_cotri_given")
    private String pneumoniaCotriGiven;
    // 504
    @Column(name = "pneumonia_call_understand")
    private Boolean pneumoniaCallUnderstand;
    // 509
    @Column(name = "local_infection_gv_lotion_understand")
    private Boolean localInfectionGvLotionUnderstand;
    // 510
    @Column(name = "local_infection_gv_lotion_given")
    private String localInfectionGvLotionGiven;
    // 511
    @Column(name = "local_infection_call_understand")
    private Boolean localInfectionCallUnderstand;
    // 702
    @Column(name = "hypothermia_risk_understand")
    private Boolean hypothermiaRiskUnderstand;
    // 709
    @Column(name = "hypothermia_warm_understand")
    private Boolean hypothermiaWarmUnderstand;
    // 712
    @Column(name = "hypothermia_kmc_understand")
    private Boolean hypothermiaKmcUnderstand;
    // 713
    @Column(name = "hypothermia_kmc_help")
    private Boolean hypothermiaKmcHelp;
    // 527
    @Column(name = "high_risk_lbw_breast_feeding_understand")
    private Boolean highRiskLbwBreastFeedingUnderstand;
    // 530
    @Column(name = "high_risk_lbw_warm_understand")
    private Boolean highRiskLbwWarmUnderstand;
    // 5271
    @Column(name = "high_risk_lbw_kmc_understand")
    private Boolean highRiskLbwKmcUnderstand;
    // 531
    @Column(name = "high_risk_lbw_kmc_help")
    private Boolean highRiskLbwKmcHelp;
    // 532
    @Column(name = "high_risk_lbw_call_understand")
    private Boolean highRiskLbwCallUnderstand;
    // 561
    @Column(name = "lbw_breast_feeding_understand")
    private Boolean lbwBreastFeedingUnderstand;
    // 562
    @Column(name = "lbw_kmc_understand")
    private Boolean lbwKmcUnderstand;
    // 565
    @Column(name = "lbw_warm_understand")
    private Boolean lbwWarmUnderstand;
    // 566
    @Column(name = "lbw_kmc_help")
    private Boolean lbwKmcHelp;
    // 567
    @Column(name = "lbw_call_understand")
    private Boolean lbwCallUnderstand;

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

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getPncId() {
        return pncId;
    }

    public void setPncId(Integer pncId) {
        this.pncId = pncId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Date getMobileStartDate() {
        return mobileStartDate;
    }

    public void setMobileStartDate(Date mobileStartDate) {
        this.mobileStartDate = mobileStartDate;
    }

    public Date getMobileEndDate() {
        return mobileEndDate;
    }

    public void setMobileEndDate(Date mobileEndDate) {
        this.mobileEndDate = mobileEndDate;
    }

    public Boolean getFamilyUnderstand() {
        return familyUnderstand;
    }

    public void setFamilyUnderstand(Boolean familyUnderstand) {
        this.familyUnderstand = familyUnderstand;
    }

    public Boolean getMorbidityFound() {
        return morbidityFound;
    }

    public void setMorbidityFound(Boolean morbidityFound) {
        this.morbidityFound = morbidityFound;
    }

    public Boolean getReadyForReferral() {
        return readyForReferral;
    }

    public void setReadyForReferral(Boolean readyForReferral) {
        this.readyForReferral = readyForReferral;
    }

    public Boolean getAbleToCall108() {
        return ableToCall108;
    }

    public void setAbleToCall108(Boolean ableToCall108) {
        this.ableToCall108 = ableToCall108;
    }

    public String getCallLog() {
        return callLog;
    }

    public void setCallLog(String callLog) {
        this.callLog = callLog;
    }

    public Boolean getAccompanyChild() {
        return accompanyChild;
    }

    public void setAccompanyChild(Boolean accompanyChild) {
        this.accompanyChild = accompanyChild;
    }

    public Boolean getReferralSlipGiven() {
        return referralSlipGiven;
    }

    public void setReferralSlipGiven(Boolean referralSlipGiven) {
        this.referralSlipGiven = referralSlipGiven;
    }

    public String getReferralPlace() {
        return referralPlace;
    }

    public void setReferralPlace(String referralPlace) {
        this.referralPlace = referralPlace;
    }

    public String getReferralVehicle() {
        return referralVehicle;
    }

    public void setReferralVehicle(String referralVehicle) {
        this.referralVehicle = referralVehicle;
    }

    public Boolean getSepsisCotriUnderstand() {
        return sepsisCotriUnderstand;
    }

    public void setSepsisCotriUnderstand(Boolean sepsisCotriUnderstand) {
        this.sepsisCotriUnderstand = sepsisCotriUnderstand;
    }

    public String getSepsisCotriGiven() {
        return sepsisCotriGiven;
    }

    public void setSepsisCotriGiven(String sepsisCotriGiven) {
        this.sepsisCotriGiven = sepsisCotriGiven;
    }

    public Boolean getSepsisWarmUnderstand() {
        return sepsisWarmUnderstand;
    }

    public void setSepsisWarmUnderstand(Boolean sepsisWarmUnderstand) {
        this.sepsisWarmUnderstand = sepsisWarmUnderstand;
    }

    public Boolean getSepsisBreastFeedingUnderstand() {
        return sepsisBreastFeedingUnderstand;
    }

    public void setSepsisBreastFeedingUnderstand(Boolean sepsisBreastFeedingUnderstand) {
        this.sepsisBreastFeedingUnderstand = sepsisBreastFeedingUnderstand;
    }

    public String getSepsisSyrupPcmGiven() {
        return sepsisSyrupPcmGiven;
    }

    public void setSepsisSyrupPcmGiven(String sepsisSyrupPcmGiven) {
        this.sepsisSyrupPcmGiven = sepsisSyrupPcmGiven;
    }

    public String getSepsisGvLotion() {
        return sepsisGvLotion;
    }

    public void setSepsisGvLotion(String sepsisGvLotion) {
        this.sepsisGvLotion = sepsisGvLotion;
    }

    public Boolean getVlbwBreastFeedingUnderstand() {
        return vlbwBreastFeedingUnderstand;
    }

    public void setVlbwBreastFeedingUnderstand(Boolean vlbwBreastFeedingUnderstand) {
        this.vlbwBreastFeedingUnderstand = vlbwBreastFeedingUnderstand;
    }

    public Boolean getVlbwWarmUnderstand() {
        return vlbwWarmUnderstand;
    }

    public void setVlbwWarmUnderstand(Boolean vlbwWarmUnderstand) {
        this.vlbwWarmUnderstand = vlbwWarmUnderstand;
    }

    public Boolean getVlbwKmcUnderstand() {
        return vlbwKmcUnderstand;
    }

    public void setVlbwKmcUnderstand(Boolean vlbwKmcUnderstand) {
        this.vlbwKmcUnderstand = vlbwKmcUnderstand;
    }

    public Boolean getVlbwCallUnderstand() {
        return vlbwCallUnderstand;
    }

    public void setVlbwCallUnderstand(Boolean vlbwCallUnderstand) {
        this.vlbwCallUnderstand = vlbwCallUnderstand;
    }

    public Boolean getBleedingWarmUnderstand() {
        return bleedingWarmUnderstand;
    }

    public void setBleedingWarmUnderstand(Boolean bleedingWarmUnderstand) {
        this.bleedingWarmUnderstand = bleedingWarmUnderstand;
    }

    public Boolean getBleedingBreastFeedingUnderstand() {
        return bleedingBreastFeedingUnderstand;
    }

    public void setBleedingBreastFeedingUnderstand(Boolean bleedingBreastFeedingUnderstand) {
        this.bleedingBreastFeedingUnderstand = bleedingBreastFeedingUnderstand;
    }

    public Boolean getJaundiceBreastFeedingUnderstand() {
        return jaundiceBreastFeedingUnderstand;
    }

    public void setJaundiceBreastFeedingUnderstand(Boolean jaundiceBreastFeedingUnderstand) {
        this.jaundiceBreastFeedingUnderstand = jaundiceBreastFeedingUnderstand;
    }

    public Boolean getJaundiceWarmUnderstand() {
        return jaundiceWarmUnderstand;
    }

    public void setJaundiceWarmUnderstand(Boolean jaundiceWarmUnderstand) {
        this.jaundiceWarmUnderstand = jaundiceWarmUnderstand;
    }

    public Boolean getDiarrhoeaOrsUnderstand() {
        return diarrhoeaOrsUnderstand;
    }

    public void setDiarrhoeaOrsUnderstand(Boolean diarrhoeaOrsUnderstand) {
        this.diarrhoeaOrsUnderstand = diarrhoeaOrsUnderstand;
    }

    public Boolean getDiarrhoeaBreastFeedingUnderstand() {
        return diarrhoeaBreastFeedingUnderstand;
    }

    public void setDiarrhoeaBreastFeedingUnderstand(Boolean diarrhoeaBreastFeedingUnderstand) {
        this.diarrhoeaBreastFeedingUnderstand = diarrhoeaBreastFeedingUnderstand;
    }

    public Boolean getDiarrhoeaCallUnderstand() {
        return diarrhoeaCallUnderstand;
    }

    public void setDiarrhoeaCallUnderstand(Boolean diarrhoeaCallUnderstand) {
        this.diarrhoeaCallUnderstand = diarrhoeaCallUnderstand;
    }

    public Boolean getPneumoniaCotriUnderstand() {
        return pneumoniaCotriUnderstand;
    }

    public void setPneumoniaCotriUnderstand(Boolean pneumoniaCotriUnderstand) {
        this.pneumoniaCotriUnderstand = pneumoniaCotriUnderstand;
    }

    public String getPneumoniaCotriGiven() {
        return pneumoniaCotriGiven;
    }

    public void setPneumoniaCotriGiven(String pneumoniaCotriGiven) {
        this.pneumoniaCotriGiven = pneumoniaCotriGiven;
    }

    public Boolean getPneumoniaCallUnderstand() {
        return pneumoniaCallUnderstand;
    }

    public void setPneumoniaCallUnderstand(Boolean pneumoniaCallUnderstand) {
        this.pneumoniaCallUnderstand = pneumoniaCallUnderstand;
    }

    public Boolean getLocalInfectionGvLotionUnderstand() {
        return localInfectionGvLotionUnderstand;
    }

    public void setLocalInfectionGvLotionUnderstand(Boolean localInfectionGvLotionUnderstand) {
        this.localInfectionGvLotionUnderstand = localInfectionGvLotionUnderstand;
    }

    public String getLocalInfectionGvLotionGiven() {
        return localInfectionGvLotionGiven;
    }

    public void setLocalInfectionGvLotionGiven(String localInfectionGvLotionGiven) {
        this.localInfectionGvLotionGiven = localInfectionGvLotionGiven;
    }

    public Boolean getLocalInfectionCallUnderstand() {
        return localInfectionCallUnderstand;
    }

    public void setLocalInfectionCallUnderstand(Boolean localInfectionCallUnderstand) {
        this.localInfectionCallUnderstand = localInfectionCallUnderstand;
    }

    public Boolean getHypothermiaRiskUnderstand() {
        return hypothermiaRiskUnderstand;
    }

    public void setHypothermiaRiskUnderstand(Boolean hypothermiaRiskUnderstand) {
        this.hypothermiaRiskUnderstand = hypothermiaRiskUnderstand;
    }

    public Boolean getHypothermiaWarmUnderstand() {
        return hypothermiaWarmUnderstand;
    }

    public void setHypothermiaWarmUnderstand(Boolean hypothermiaWarmUnderstand) {
        this.hypothermiaWarmUnderstand = hypothermiaWarmUnderstand;
    }

    public Boolean getHypothermiaKmcUnderstand() {
        return hypothermiaKmcUnderstand;
    }

    public void setHypothermiaKmcUnderstand(Boolean hypothermiaKmcUnderstand) {
        this.hypothermiaKmcUnderstand = hypothermiaKmcUnderstand;
    }

    public Boolean getHypothermiaKmcHelp() {
        return hypothermiaKmcHelp;
    }

    public void setHypothermiaKmcHelp(Boolean hypothermiaKmcHelp) {
        this.hypothermiaKmcHelp = hypothermiaKmcHelp;
    }

    public Boolean getHighRiskLbwBreastFeedingUnderstand() {
        return highRiskLbwBreastFeedingUnderstand;
    }

    public void setHighRiskLbwBreastFeedingUnderstand(Boolean highRiskLbwBreastFeedingUnderstand) {
        this.highRiskLbwBreastFeedingUnderstand = highRiskLbwBreastFeedingUnderstand;
    }

    public Boolean getHighRiskLbwWarmUnderstand() {
        return highRiskLbwWarmUnderstand;
    }

    public void setHighRiskLbwWarmUnderstand(Boolean highRiskLbwWarmUnderstand) {
        this.highRiskLbwWarmUnderstand = highRiskLbwWarmUnderstand;
    }

    public Boolean getHighRiskLbwKmcUnderstand() {
        return highRiskLbwKmcUnderstand;
    }

    public void setHighRiskLbwKmcUnderstand(Boolean highRiskLbwKmcUnderstand) {
        this.highRiskLbwKmcUnderstand = highRiskLbwKmcUnderstand;
    }

    public Boolean getHighRiskLbwKmcHelp() {
        return highRiskLbwKmcHelp;
    }

    public void setHighRiskLbwKmcHelp(Boolean highRiskLbwKmcHelp) {
        this.highRiskLbwKmcHelp = highRiskLbwKmcHelp;
    }

    public Boolean getHighRiskLbwCallUnderstand() {
        return highRiskLbwCallUnderstand;
    }

    public void setHighRiskLbwCallUnderstand(Boolean highRiskLbwCallUnderstand) {
        this.highRiskLbwCallUnderstand = highRiskLbwCallUnderstand;
    }

    public Boolean getLbwBreastFeedingUnderstand() {
        return lbwBreastFeedingUnderstand;
    }

    public void setLbwBreastFeedingUnderstand(Boolean lbwBreastFeedingUnderstand) {
        this.lbwBreastFeedingUnderstand = lbwBreastFeedingUnderstand;
    }

    public Boolean getLbwKmcUnderstand() {
        return lbwKmcUnderstand;
    }

    public void setLbwKmcUnderstand(Boolean lbwKmcUnderstand) {
        this.lbwKmcUnderstand = lbwKmcUnderstand;
    }

    public Boolean getLbwWarmUnderstand() {
        return lbwWarmUnderstand;
    }

    public void setLbwWarmUnderstand(Boolean lbwWarmUnderstand) {
        this.lbwWarmUnderstand = lbwWarmUnderstand;
    }

    public Boolean getLbwKmcHelp() {
        return lbwKmcHelp;
    }

    public void setLbwKmcHelp(Boolean lbwKmcHelp) {
        this.lbwKmcHelp = lbwKmcHelp;
    }

    public Boolean getLbwCallUnderstand() {
        return lbwCallUnderstand;
    }

    public void setLbwCallUnderstand(Boolean lbwCallUnderstand) {
        this.lbwCallUnderstand = lbwCallUnderstand;
    }
}
