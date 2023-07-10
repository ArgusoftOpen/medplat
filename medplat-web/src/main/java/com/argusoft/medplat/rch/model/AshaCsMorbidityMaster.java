package com.argusoft.medplat.rch.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Define rch_asha_cs_morbidity_master entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_asha_cs_morbidity_master")
public class AshaCsMorbidityMaster extends EntityAuditInfo implements Serializable {

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
    @Column(name = "cs_id")
    private Integer csId;
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
    // 56
    @Column(name = "accompany_child")
    private Boolean accompanyChild;
    // 57
    @Column(name = "referral_slip_given")
    private Boolean referralSlipGiven;
    // 58
    @Column(name = "referral_place")
    private String referralPlace;
    // 59
    @Column(name = "referral_vehicle")
    private String referralVehicle;
    // 300, 3001
    @Column(name = "severe_pneumonia_oral_food")
    private Boolean severePneumoniaOralFood;
    // 301, 3011
    @Column(name = "severe_pneumonia_cotri_given")
    private String severePneumoniaCotriGiven;
    // 302, 3021
    @Column(name = "chronic_cough_family_understand")
    private Boolean chronicCoughFamilyUnderstand;
    // 303, 3031
    @Column(name = "diarrhoea_severe_dehydration_oral_food")
    private Boolean diarrhoeaSevereDehydrationOralFood;
    // 304, 3041
    @Column(name = "diarrhoea_severe_dehydration_ors_given")
    private String diarrhoeaSevereDehydrationOrsGiven;
    // 306, 3061
    @Column(name = "febrile_illness_cotri_given")
    private String febrileIllnessCotriGiven;
    // 307, 3071
    @Column(name = "febrile_illness_slides_taken")
    private Boolean febrileIllnessSlidesTaken;
    // 310, 3101
    @Column(name = "febrile_illness_chloroquine_given")
    private String febrileIllnessChloroquineGiven;
    // 312, 3121
    @Column(name = "febrile_illness_pcm_given")
    private String febrileIllnessPcmGiven;
    // 315, 3151
    @Column(name = "severe_malnutrition_vitamin_a_given")
    private String severeMalnutritionVitaminAGiven;
    // 319, 3191
    @Column(name = "severe_malnutrition_breast_feeding_understand")
    private Boolean severeMalnutritionBreastFeedingUnderstand;
    // 322, 3221
    @Column(name = "severe_anemia_folic_given")
    private String severeAnemiaFolicGiven;
    // 323, 3231
    @Column(name = "severe_persistent_diarrioea_oral_food")
    private Boolean severePersistentDiarrioeaOralFood;
    // 324, 3241
    @Column(name = "severe_persistent_diarrioea_ors_given")
    private String severePersistentDiarrioeaOrsGiven;
    // 999
    @Column(name = "pneumonia_cotri_given")
    private String pneumoniaCotriGiven;
    // 403
    @Column(name = "pneumonia_cotrimoxazole_syrup")
    private Boolean pneumoniaCotrimoxazoleSyrup;
    // 404
    @Column(name = "pneumonia_family_understand")
    private Boolean pneumoniaFamilyUnderstand;
    // 409
    @Column(name = "diarrhoea_some_dehydration_ors_given")
    private String diarrhoeaSomeDehydrationOrsGiven;
    // 413
    @Column(name = "diarrhoea_some_dehydration_ors_understand")
    private Boolean diarrhoeaSomeDehydrationOrsUnderstand;
    // 416
    @Column(name = "diarrhoea_some_dehydration_family_understand")
    private Boolean diarrhoeaSomeDehydrationFamilyUnderstand;
    // 418
    @Column(name = "diarrhoea_some_dehydration_dehydration_understand")
    private Boolean diarrhoeaSomeDehydrationDehydrationUnderstand;
    // 420
    @Column(name = "dysentry_cotri_given")
    private String dysentryCotriGiven;
    // 422
    @Column(name = "dysentry_cotrimoxazole_syrup")
    private Boolean dysentryCotrimoxazoleSyrup;
    // 423
    @Column(name = "dysentry_return_immediately")
    private Boolean dysentryReturnImmediately;
    // 425
    @Column(name = "malaria_slides_taken")
    private Boolean malariaSlidesTaken;
    // 428
    @Column(name = "malaria_chloroquine_given")
    private String malariaChloroquineGiven;
    // 430
    @Column(name = "malaria_pcm_given")
    private String malariaPcmGiven;
    // 432
    @Column(name = "malaria_fever_persist_family_understand")
    private Boolean malariaFeverPersistFamilyUnderstand;
    // 450
    @Column(name = "anemia_folic_given")
    private String anemiaFolicGiven;
    // 501
    @Column(name = "cold_cough_family_understand")
    private Boolean coldCoughFamilyUnderstand;
    // 506
    @Column(name = "diarrhoea_no_dehydration_ors_given")
    private String diarrhoeaNoDehydrationOrsGiven;
    // 510
    @Column(name = "diarrhoea_no_dehydration_ors_understand")
    private Boolean diarrhoeaNoDehydrationOrsUnderstand;
    // 511
    @Column(name = "diarrhoea_no_dehydration_family_understand")
    private Boolean diarrhoeaNoDehydrationFamilyUnderstand;
    // 526
    @Column(name = "no_anemia_folic_given")
    private String noAnemiaFolicGiven;

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

    public Integer getCsId() {
        return csId;
    }

    public void setCsId(Integer csId) {
        this.csId = csId;
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

    public Boolean getSeverePneumoniaOralFood() {
        return severePneumoniaOralFood;
    }

    public void setSeverePneumoniaOralFood(Boolean severePneumoniaOralFood) {
        this.severePneumoniaOralFood = severePneumoniaOralFood;
    }

    public String getSeverePneumoniaCotriGiven() {
        return severePneumoniaCotriGiven;
    }

    public void setSeverePneumoniaCotriGiven(String severePneumoniaCotriGiven) {
        this.severePneumoniaCotriGiven = severePneumoniaCotriGiven;
    }

    public Boolean getChronicCoughFamilyUnderstand() {
        return chronicCoughFamilyUnderstand;
    }

    public void setChronicCoughFamilyUnderstand(Boolean chronicCoughFamilyUnderstand) {
        this.chronicCoughFamilyUnderstand = chronicCoughFamilyUnderstand;
    }

    public Boolean getDiarrhoeaSevereDehydrationOralFood() {
        return diarrhoeaSevereDehydrationOralFood;
    }

    public void setDiarrhoeaSevereDehydrationOralFood(Boolean diarrhoeaSevereDehydrationOralFood) {
        this.diarrhoeaSevereDehydrationOralFood = diarrhoeaSevereDehydrationOralFood;
    }

    public String getDiarrhoeaSevereDehydrationOrsGiven() {
        return diarrhoeaSevereDehydrationOrsGiven;
    }

    public void setDiarrhoeaSevereDehydrationOrsGiven(String diarrhoeaSevereDehydrationOrsGiven) {
        this.diarrhoeaSevereDehydrationOrsGiven = diarrhoeaSevereDehydrationOrsGiven;
    }

    public String getFebrileIllnessCotriGiven() {
        return febrileIllnessCotriGiven;
    }

    public void setFebrileIllnessCotriGiven(String febrileIllnessCotriGiven) {
        this.febrileIllnessCotriGiven = febrileIllnessCotriGiven;
    }

    public Boolean getFebrileIllnessSlidesTaken() {
        return febrileIllnessSlidesTaken;
    }

    public void setFebrileIllnessSlidesTaken(Boolean febrileIllnessSlidesTaken) {
        this.febrileIllnessSlidesTaken = febrileIllnessSlidesTaken;
    }

    public String getFebrileIllnessChloroquineGiven() {
        return febrileIllnessChloroquineGiven;
    }

    public void setFebrileIllnessChloroquineGiven(String febrileIllnessChloroquineGiven) {
        this.febrileIllnessChloroquineGiven = febrileIllnessChloroquineGiven;
    }

    public String getFebrileIllnessPcmGiven() {
        return febrileIllnessPcmGiven;
    }

    public void setFebrileIllnessPcmGiven(String febrileIllnessPcmGiven) {
        this.febrileIllnessPcmGiven = febrileIllnessPcmGiven;
    }

    public String getSevereMalnutritionVitaminAGiven() {
        return severeMalnutritionVitaminAGiven;
    }

    public void setSevereMalnutritionVitaminAGiven(String severeMalnutritionVitaminAGiven) {
        this.severeMalnutritionVitaminAGiven = severeMalnutritionVitaminAGiven;
    }

    public Boolean getSevereMalnutritionBreastFeedingUnderstand() {
        return severeMalnutritionBreastFeedingUnderstand;
    }

    public void setSevereMalnutritionBreastFeedingUnderstand(Boolean severeMalnutritionBreastFeedingUnderstand) {
        this.severeMalnutritionBreastFeedingUnderstand = severeMalnutritionBreastFeedingUnderstand;
    }

    public String getSevereAnemiaFolicGiven() {
        return severeAnemiaFolicGiven;
    }

    public void setSevereAnemiaFolicGiven(String severeAnemiaFolicGiven) {
        this.severeAnemiaFolicGiven = severeAnemiaFolicGiven;
    }

    public Boolean getSeverePersistentDiarrioeaOralFood() {
        return severePersistentDiarrioeaOralFood;
    }

    public void setSeverePersistentDiarrioeaOralFood(Boolean severePersistentDiarrioeaOralFood) {
        this.severePersistentDiarrioeaOralFood = severePersistentDiarrioeaOralFood;
    }

    public String getSeverePersistentDiarrioeaOrsGiven() {
        return severePersistentDiarrioeaOrsGiven;
    }

    public void setSeverePersistentDiarrioeaOrsGiven(String severePersistentDiarrioeaOrsGiven) {
        this.severePersistentDiarrioeaOrsGiven = severePersistentDiarrioeaOrsGiven;
    }

    public String getPneumoniaCotriGiven() {
        return pneumoniaCotriGiven;
    }

    public void setPneumoniaCotriGiven(String pneumoniaCotriGiven) {
        this.pneumoniaCotriGiven = pneumoniaCotriGiven;
    }

    public Boolean getPneumoniaCotrimoxazoleSyrup() {
        return pneumoniaCotrimoxazoleSyrup;
    }

    public void setPneumoniaCotrimoxazoleSyrup(Boolean pneumoniaCotrimoxazoleSyrup) {
        this.pneumoniaCotrimoxazoleSyrup = pneumoniaCotrimoxazoleSyrup;
    }

    public Boolean getPneumoniaFamilyUnderstand() {
        return pneumoniaFamilyUnderstand;
    }

    public void setPneumoniaFamilyUnderstand(Boolean pneumoniaFamilyUnderstand) {
        this.pneumoniaFamilyUnderstand = pneumoniaFamilyUnderstand;
    }

    public String getDiarrhoeaSomeDehydrationOrsGiven() {
        return diarrhoeaSomeDehydrationOrsGiven;
    }

    public void setDiarrhoeaSomeDehydrationOrsGiven(String diarrhoeaSomeDehydrationOrsGiven) {
        this.diarrhoeaSomeDehydrationOrsGiven = diarrhoeaSomeDehydrationOrsGiven;
    }

    public Boolean getDiarrhoeaSomeDehydrationOrsUnderstand() {
        return diarrhoeaSomeDehydrationOrsUnderstand;
    }

    public void setDiarrhoeaSomeDehydrationOrsUnderstand(Boolean diarrhoeaSomeDehydrationOrsUnderstand) {
        this.diarrhoeaSomeDehydrationOrsUnderstand = diarrhoeaSomeDehydrationOrsUnderstand;
    }

    public Boolean getDiarrhoeaSomeDehydrationFamilyUnderstand() {
        return diarrhoeaSomeDehydrationFamilyUnderstand;
    }

    public void setDiarrhoeaSomeDehydrationFamilyUnderstand(Boolean diarrhoeaSomeDehydrationFamilyUnderstand) {
        this.diarrhoeaSomeDehydrationFamilyUnderstand = diarrhoeaSomeDehydrationFamilyUnderstand;
    }

    public Boolean getDiarrhoeaSomeDehydrationDehydrationUnderstand() {
        return diarrhoeaSomeDehydrationDehydrationUnderstand;
    }

    public void setDiarrhoeaSomeDehydrationDehydrationUnderstand(Boolean diarrhoeaSomeDehydrationDehydrationUnderstand) {
        this.diarrhoeaSomeDehydrationDehydrationUnderstand = diarrhoeaSomeDehydrationDehydrationUnderstand;
    }

    public String getDysentryCotriGiven() {
        return dysentryCotriGiven;
    }

    public void setDysentryCotriGiven(String dysentryCotriGiven) {
        this.dysentryCotriGiven = dysentryCotriGiven;
    }

    public Boolean getDysentryCotrimoxazoleSyrup() {
        return dysentryCotrimoxazoleSyrup;
    }

    public void setDysentryCotrimoxazoleSyrup(Boolean dysentryCotrimoxazoleSyrup) {
        this.dysentryCotrimoxazoleSyrup = dysentryCotrimoxazoleSyrup;
    }

    public Boolean getDysentryReturnImmediately() {
        return dysentryReturnImmediately;
    }

    public void setDysentryReturnImmediately(Boolean dysentryReturnImmediately) {
        this.dysentryReturnImmediately = dysentryReturnImmediately;
    }

    public Boolean getMalariaSlidesTaken() {
        return malariaSlidesTaken;
    }

    public void setMalariaSlidesTaken(Boolean malariaSlidesTaken) {
        this.malariaSlidesTaken = malariaSlidesTaken;
    }

    public String getMalariaChloroquineGiven() {
        return malariaChloroquineGiven;
    }

    public void setMalariaChloroquineGiven(String malariaChloroquineGiven) {
        this.malariaChloroquineGiven = malariaChloroquineGiven;
    }

    public String getMalariaPcmGiven() {
        return malariaPcmGiven;
    }

    public void setMalariaPcmGiven(String malariaPcmGiven) {
        this.malariaPcmGiven = malariaPcmGiven;
    }

    public Boolean getMalariaFeverPersistFamilyUnderstand() {
        return malariaFeverPersistFamilyUnderstand;
    }

    public void setMalariaFeverPersistFamilyUnderstand(Boolean malariaFeverPersistFamilyUnderstand) {
        this.malariaFeverPersistFamilyUnderstand = malariaFeverPersistFamilyUnderstand;
    }

    public String getAnemiaFolicGiven() {
        return anemiaFolicGiven;
    }

    public void setAnemiaFolicGiven(String anemiaFolicGiven) {
        this.anemiaFolicGiven = anemiaFolicGiven;
    }

    public Boolean getColdCoughFamilyUnderstand() {
        return coldCoughFamilyUnderstand;
    }

    public void setColdCoughFamilyUnderstand(Boolean coldCoughFamilyUnderstand) {
        this.coldCoughFamilyUnderstand = coldCoughFamilyUnderstand;
    }

    public String getDiarrhoeaNoDehydrationOrsGiven() {
        return diarrhoeaNoDehydrationOrsGiven;
    }

    public void setDiarrhoeaNoDehydrationOrsGiven(String diarrhoeaNoDehydrationOrsGiven) {
        this.diarrhoeaNoDehydrationOrsGiven = diarrhoeaNoDehydrationOrsGiven;
    }

    public Boolean getDiarrhoeaNoDehydrationOrsUnderstand() {
        return diarrhoeaNoDehydrationOrsUnderstand;
    }

    public void setDiarrhoeaNoDehydrationOrsUnderstand(Boolean diarrhoeaNoDehydrationOrsUnderstand) {
        this.diarrhoeaNoDehydrationOrsUnderstand = diarrhoeaNoDehydrationOrsUnderstand;
    }

    public Boolean getDiarrhoeaNoDehydrationFamilyUnderstand() {
        return diarrhoeaNoDehydrationFamilyUnderstand;
    }

    public void setDiarrhoeaNoDehydrationFamilyUnderstand(Boolean diarrhoeaNoDehydrationFamilyUnderstand) {
        this.diarrhoeaNoDehydrationFamilyUnderstand = diarrhoeaNoDehydrationFamilyUnderstand;
    }

    public String getNoAnemiaFolicGiven() {
        return noAnemiaFolicGiven;
    }

    public void setNoAnemiaFolicGiven(String noAnemiaFolicGiven) {
        this.noAnemiaFolicGiven = noAnemiaFolicGiven;
    }

}
