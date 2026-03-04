package com.argusoft.medplat.rch.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Define rch_asha_anc_morbidity_master entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_asha_anc_morbidity_master")
public class AshaAncMorbidityMaster extends EntityAuditInfo implements Serializable {

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
    @Column(name = "anc_id")
    private Integer ancId;
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
    // 103
    @Column(name = "family_understands_big_hospital")
    private Boolean familyUnderstandsBigHospital;
    // 104
    @Column(name = "ready_for_referral")
    private Boolean readyForReferral;
    // 106
    @Column(name = "able_to_call_108")
    private Boolean ableToCall108;
    // 50
    @Column(name = "call_log")
    private String callLog;
    // 107
    @Column(name = "accompany_women")
    private Boolean accompanyWomen;
    // 108
    @Column(name = "referral_slip_given")
    private Boolean referralSlipGiven;
    // 109
    @Column(name = "referral_place")
    private String referralPlace;
    // 110
    @Column(name = "referral_vehicle")
    private String referralVehicle;
    // 222
    @Column(name = "understand_hospital_delivery")
    private Boolean understandHospitalDelivery;
    // 307
    @Column(name = "severe_anemia_ifa_understand")
    private Boolean severeAnemiaIfaUnderstand;
    // 401 && 404
    @Column(name = "sickle_cell_pcm_given")
    private Boolean sickleCellPcmGiven;
    // 501
    @Column(name = "bad_obstetric_doctor_visit")
    private Boolean badObstetricDoctorVisit;
    // 502
    @Column(name = "bad_obstetric_hospital_visit")
    private Boolean badObstetricHospitalVisit;
    // 503
    @Column(name = "bad_obstetric_hospital_delivery")
    private Boolean badObstetricHospitalDelivery;
    // 504
    @Column(name = "unintended_preg_continue_pregnancy")
    private Boolean unintendedPregContinuePregnancy;
    // 505
    @Column(name = "unintended_preg_arrange_marriage")
    private Boolean unintendedPregArrangeMarriage;
    // 508
    @Column(name = "unintended_preg_termination_understand")
    private Boolean unintendedPregTerminationUnderstand;
    // 511
    @Column(name = "unintended_preg_help")
    private Boolean unintendedPregHelp;
    // 514
    @Column(name = "mild_hypertension_hospital_delivery")
    private Boolean mildHypertensionHospitalDelivery;
    // 518
    @Column(name = "malaria_chloroquine_given")
    private Boolean malariaChloroquineGiven;
    // 519
    @Column(name = "malaria_pcm_given")
    private Boolean malariaPcmGiven;
    // 520
    @Column(name = "malaria_chloroquine_understand")
    private Boolean malariaChloroquineUnderstand;
    // 521
    @Column(name = "malaria_family_understand")
    private Boolean malariaFamilyUnderstand;
    // 522
    @Column(name = "fever_pcm_given")
    private Boolean feverPcmGiven;
    // 523
    @Column(name = "fever_phc_visit")
    private Boolean feverPhcVisit;
    // 524
    @Column(name = "fever_family_understand")
    private Boolean feverFamilyUnderstand;
    // 529
    @Column(name = "urinary_tract_hospital_visit")
    private Boolean urinaryTractHospitalVisit;
    // 530
    @Column(name = "urinary_tract_family_understand")
    private Boolean urinaryTractFamilyUnderstand;
    // 532
    @Column(name = "vaginitis_hospital_visit")
    private Boolean vaginitisHospitalVisit;
    // 533
    @Column(name = "vaginitis_family_understand")
    private Boolean vaginitisFamilyUnderstand;
    // 535
    @Column(name = "vaginitis_bathe_daily")
    private Boolean vaginitisBatheDaily;
    // 537
    @Column(name = "night_blindness_vhnd_visit")
    private Boolean nightBlindnessVhndVisit;
    // 544
    @Column(name = "probable_anemia_ifa_understand")
    private Boolean probableAnemiaIfaUnderstand;
    // 546
    @Column(name = "probable_anemia_hospital_delivery")
    private Boolean probableAnemiaHospitalDelivery;
    // 605
    @Column(name = "emesis_pregnancy_family_understand")
    private Boolean emesisPregnancyFamilyUnderstand;
    // 610
    @Column(name = "respiratory_tract_drink_water")
    private Boolean respiratoryTractDrinkWater;
    // 612
    @Column(name = "moderate_anemia_ifa_given")
    private Boolean moderateAnemiaIfaGiven;
    // 620
    @Column(name = "moderate_anemia_ifa_understand")
    private Boolean moderateAnemiaIfaUnderstand;
    // 621
    @Column(name = "moderate_anemia_hospital_delivery")
    private Boolean moderateAnemiaHospitalDelivery;
    // 625
    @Column(name = "breast_problem_demonstration")
    private Boolean breastProblemDemonstration;
    // 626
    @Column(name = "breast_problem_syringe_given")
    private Boolean breastProblemSyringeGiven;

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

    public Integer getAncId() {
        return ancId;
    }

    public void setAncId(Integer ancId) {
        this.ancId = ancId;
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

    public Boolean getFamilyUnderstandsBigHospital() {
        return familyUnderstandsBigHospital;
    }

    public void setFamilyUnderstandsBigHospital(Boolean familyUnderstandsBigHospital) {
        this.familyUnderstandsBigHospital = familyUnderstandsBigHospital;
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

    public Boolean getAccompanyWomen() {
        return accompanyWomen;
    }

    public void setAccompanyWomen(Boolean accompanyWomen) {
        this.accompanyWomen = accompanyWomen;
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

    public Boolean getUnderstandHospitalDelivery() {
        return understandHospitalDelivery;
    }

    public void setUnderstandHospitalDelivery(Boolean understandHospitalDelivery) {
        this.understandHospitalDelivery = understandHospitalDelivery;
    }

    public Boolean getSevereAnemiaIfaUnderstand() {
        return severeAnemiaIfaUnderstand;
    }

    public void setSevereAnemiaIfaUnderstand(Boolean severeAnemiaIfaUnderstand) {
        this.severeAnemiaIfaUnderstand = severeAnemiaIfaUnderstand;
    }

    public Boolean getSickleCellPcmGiven() {
        return sickleCellPcmGiven;
    }

    public void setSickleCellPcmGiven(Boolean sickleCellPcmGiven) {
        this.sickleCellPcmGiven = sickleCellPcmGiven;
    }

    public Boolean getBadObstetricDoctorVisit() {
        return badObstetricDoctorVisit;
    }

    public void setBadObstetricDoctorVisit(Boolean badObstetricDoctorVisit) {
        this.badObstetricDoctorVisit = badObstetricDoctorVisit;
    }

    public Boolean getBadObstetricHospitalVisit() {
        return badObstetricHospitalVisit;
    }

    public void setBadObstetricHospitalVisit(Boolean badObstetricHospitalVisit) {
        this.badObstetricHospitalVisit = badObstetricHospitalVisit;
    }

    public Boolean getBadObstetricHospitalDelivery() {
        return badObstetricHospitalDelivery;
    }

    public void setBadObstetricHospitalDelivery(Boolean badObstetricHospitalDelivery) {
        this.badObstetricHospitalDelivery = badObstetricHospitalDelivery;
    }

    public Boolean getUnintendedPregContinuePregnancy() {
        return unintendedPregContinuePregnancy;
    }

    public void setUnintendedPregContinuePregnancy(Boolean unintendedPregContinuePregnancy) {
        this.unintendedPregContinuePregnancy = unintendedPregContinuePregnancy;
    }

    public Boolean getUnintendedPregArrangeMarriage() {
        return unintendedPregArrangeMarriage;
    }

    public void setUnintendedPregArrangeMarriage(Boolean unintendedPregArrangeMarriage) {
        this.unintendedPregArrangeMarriage = unintendedPregArrangeMarriage;
    }

    public Boolean getUnintendedPregTerminationUnderstand() {
        return unintendedPregTerminationUnderstand;
    }

    public void setUnintendedPregTerminationUnderstand(Boolean unintendedPregTerminationUnderstand) {
        this.unintendedPregTerminationUnderstand = unintendedPregTerminationUnderstand;
    }

    public Boolean getUnintendedPregHelp() {
        return unintendedPregHelp;
    }

    public void setUnintendedPregHelp(Boolean unintendedPregHelp) {
        this.unintendedPregHelp = unintendedPregHelp;
    }

    public Boolean getMildHypertensionHospitalDelivery() {
        return mildHypertensionHospitalDelivery;
    }

    public void setMildHypertensionHospitalDelivery(Boolean mildHypertensionHospitalDelivery) {
        this.mildHypertensionHospitalDelivery = mildHypertensionHospitalDelivery;
    }

    public Boolean getMalariaChloroquineGiven() {
        return malariaChloroquineGiven;
    }

    public void setMalariaChloroquineGiven(Boolean malariaChloroquineGiven) {
        this.malariaChloroquineGiven = malariaChloroquineGiven;
    }

    public Boolean getMalariaPcmGiven() {
        return malariaPcmGiven;
    }

    public void setMalariaPcmGiven(Boolean malariaPcmGiven) {
        this.malariaPcmGiven = malariaPcmGiven;
    }

    public Boolean getMalariaChloroquineUnderstand() {
        return malariaChloroquineUnderstand;
    }

    public void setMalariaChloroquineUnderstand(Boolean malariaChloroquineUnderstand) {
        this.malariaChloroquineUnderstand = malariaChloroquineUnderstand;
    }

    public Boolean getMalariaFamilyUnderstand() {
        return malariaFamilyUnderstand;
    }

    public void setMalariaFamilyUnderstand(Boolean malariaFamilyUnderstand) {
        this.malariaFamilyUnderstand = malariaFamilyUnderstand;
    }

    public Boolean getFeverPcmGiven() {
        return feverPcmGiven;
    }

    public void setFeverPcmGiven(Boolean feverPcmGiven) {
        this.feverPcmGiven = feverPcmGiven;
    }

    public Boolean getFeverPhcVisit() {
        return feverPhcVisit;
    }

    public void setFeverPhcVisit(Boolean feverPhcVisit) {
        this.feverPhcVisit = feverPhcVisit;
    }

    public Boolean getFeverFamilyUnderstand() {
        return feverFamilyUnderstand;
    }

    public void setFeverFamilyUnderstand(Boolean feverFamilyUnderstand) {
        this.feverFamilyUnderstand = feverFamilyUnderstand;
    }

    public Boolean getUrinaryTractHospitalVisit() {
        return urinaryTractHospitalVisit;
    }

    public void setUrinaryTractHospitalVisit(Boolean urinaryTractHospitalVisit) {
        this.urinaryTractHospitalVisit = urinaryTractHospitalVisit;
    }

    public Boolean getUrinaryTractFamilyUnderstand() {
        return urinaryTractFamilyUnderstand;
    }

    public void setUrinaryTractFamilyUnderstand(Boolean urinaryTractFamilyUnderstand) {
        this.urinaryTractFamilyUnderstand = urinaryTractFamilyUnderstand;
    }

    public Boolean getVaginitisHospitalVisit() {
        return vaginitisHospitalVisit;
    }

    public void setVaginitisHospitalVisit(Boolean vaginitisHospitalVisit) {
        this.vaginitisHospitalVisit = vaginitisHospitalVisit;
    }

    public Boolean getVaginitisFamilyUnderstand() {
        return vaginitisFamilyUnderstand;
    }

    public void setVaginitisFamilyUnderstand(Boolean vaginitisFamilyUnderstand) {
        this.vaginitisFamilyUnderstand = vaginitisFamilyUnderstand;
    }

    public Boolean getVaginitisBatheDaily() {
        return vaginitisBatheDaily;
    }

    public void setVaginitisBatheDaily(Boolean vaginitisBatheDaily) {
        this.vaginitisBatheDaily = vaginitisBatheDaily;
    }

    public Boolean getNightBlindnessVhndVisit() {
        return nightBlindnessVhndVisit;
    }

    public void setNightBlindnessVhndVisit(Boolean nightBlindnessVhndVisit) {
        this.nightBlindnessVhndVisit = nightBlindnessVhndVisit;
    }

    public Boolean getProbableAnemiaIfaUnderstand() {
        return probableAnemiaIfaUnderstand;
    }

    public void setProbableAnemiaIfaUnderstand(Boolean probableAnemiaIfaUnderstand) {
        this.probableAnemiaIfaUnderstand = probableAnemiaIfaUnderstand;
    }

    public Boolean getProbableAnemiaHospitalDelivery() {
        return probableAnemiaHospitalDelivery;
    }

    public void setProbableAnemiaHospitalDelivery(Boolean probableAnemiaHospitalDelivery) {
        this.probableAnemiaHospitalDelivery = probableAnemiaHospitalDelivery;
    }

    public Boolean getEmesisPregnancyFamilyUnderstand() {
        return emesisPregnancyFamilyUnderstand;
    }

    public void setEmesisPregnancyFamilyUnderstand(Boolean emesisPregnancyFamilyUnderstand) {
        this.emesisPregnancyFamilyUnderstand = emesisPregnancyFamilyUnderstand;
    }

    public Boolean getRespiratoryTractDrinkWater() {
        return respiratoryTractDrinkWater;
    }

    public void setRespiratoryTractDrinkWater(Boolean respiratoryTractDrinkWater) {
        this.respiratoryTractDrinkWater = respiratoryTractDrinkWater;
    }

    public Boolean getModerateAnemiaIfaGiven() {
        return moderateAnemiaIfaGiven;
    }

    public void setModerateAnemiaIfaGiven(Boolean moderateAnemiaIfaGiven) {
        this.moderateAnemiaIfaGiven = moderateAnemiaIfaGiven;
    }

    public Boolean getModerateAnemiaIfaUnderstand() {
        return moderateAnemiaIfaUnderstand;
    }

    public void setModerateAnemiaIfaUnderstand(Boolean moderateAnemiaIfaUnderstand) {
        this.moderateAnemiaIfaUnderstand = moderateAnemiaIfaUnderstand;
    }

    public Boolean getModerateAnemiaHospitalDelivery() {
        return moderateAnemiaHospitalDelivery;
    }

    public void setModerateAnemiaHospitalDelivery(Boolean moderateAnemiaHospitalDelivery) {
        this.moderateAnemiaHospitalDelivery = moderateAnemiaHospitalDelivery;
    }

    public Boolean getBreastProblemDemonstration() {
        return breastProblemDemonstration;
    }

    public void setBreastProblemDemonstration(Boolean breastProblemDemonstration) {
        this.breastProblemDemonstration = breastProblemDemonstration;
    }

    public Boolean getBreastProblemSyringeGiven() {
        return breastProblemSyringeGiven;
    }

    public void setBreastProblemSyringeGiven(Boolean breastProblemSyringeGiven) {
        this.breastProblemSyringeGiven = breastProblemSyringeGiven;
    }
}
