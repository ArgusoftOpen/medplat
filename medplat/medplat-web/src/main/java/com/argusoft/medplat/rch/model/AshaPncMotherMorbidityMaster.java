package com.argusoft.medplat.rch.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Define rch_asha_pnc_mother_morbidity_master entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_asha_pnc_mother_morbidity_master")
public class AshaPncMotherMorbidityMaster extends EntityAuditInfo implements Serializable {

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
    @Column(name = "accompany_women")
    private Boolean accompanyWomen;
    // 57, 61, 65
    @Column(name = "referral_slip_given")
    private Boolean referralSlipGiven;
    // 58, 62, 66
    @Column(name = "referral_place")
    private String referralPlace;
    // 59, 63, 67
    @Column(name = "referral_vehicle")
    private String referralVehicle;

    // 394, 398
    @Column(name = "mastitis_pcm_given")
    private String mastitisPcmGiven;
    // 396, 400
    @Column(name = "mastitis_breast_feeding_understand")
    private Boolean mastitisBreastFeedingUnderstand;
    // 401
    @Column(name = "mastitis_warm_water_understand")
    private Boolean mastitisWarmWaterUnderstand;
    // 535
    @Column(name = "feeding_problem_breast_feeding_understand")
    private Boolean feedingProblemBreastFeedingUnderstand;
    // 540
    @Column(name = "feeding_problem_engorged_breast_understand")
    private Boolean feedingProblemEngorgedBreastUnderstand;
    // 544
    @Column(name = "feeding_problem_crakd_nipple_understand")
    private Boolean feedingProblemCrakdNippleUnderstand;
    // 547
    @Column(name = "feeding_problem_retrctd_nipple_understand")
    private Boolean feedingProblemRetrctdNippleUnderstand;
    // 548
    @Column(name = "feeding_problem_family_understand")
    private Boolean feedingProblemFamilyUnderstand;

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

    public String getMastitisPcmGiven() {
        return mastitisPcmGiven;
    }

    public void setMastitisPcmGiven(String mastitisPcmGiven) {
        this.mastitisPcmGiven = mastitisPcmGiven;
    }

    public Boolean getMastitisBreastFeedingUnderstand() {
        return mastitisBreastFeedingUnderstand;
    }

    public void setMastitisBreastFeedingUnderstand(Boolean mastitisBreastFeedingUnderstand) {
        this.mastitisBreastFeedingUnderstand = mastitisBreastFeedingUnderstand;
    }

    public Boolean getMastitisWarmWaterUnderstand() {
        return mastitisWarmWaterUnderstand;
    }

    public void setMastitisWarmWaterUnderstand(Boolean mastitisWarmWaterUnderstand) {
        this.mastitisWarmWaterUnderstand = mastitisWarmWaterUnderstand;
    }

    public Boolean getFeedingProblemBreastFeedingUnderstand() {
        return feedingProblemBreastFeedingUnderstand;
    }

    public void setFeedingProblemBreastFeedingUnderstand(Boolean feedingProblemBreastFeedingUnderstand) {
        this.feedingProblemBreastFeedingUnderstand = feedingProblemBreastFeedingUnderstand;
    }

    public Boolean getFeedingProblemEngorgedBreastUnderstand() {
        return feedingProblemEngorgedBreastUnderstand;
    }

    public void setFeedingProblemEngorgedBreastUnderstand(Boolean feedingProblemEngorgedBreastUnderstand) {
        this.feedingProblemEngorgedBreastUnderstand = feedingProblemEngorgedBreastUnderstand;
    }

    public Boolean getFeedingProblemCrakdNippleUnderstand() {
        return feedingProblemCrakdNippleUnderstand;
    }

    public void setFeedingProblemCrakdNippleUnderstand(Boolean feedingProblemCrakdNippleUnderstand) {
        this.feedingProblemCrakdNippleUnderstand = feedingProblemCrakdNippleUnderstand;
    }

    public Boolean getFeedingProblemRetrctdNippleUnderstand() {
        return feedingProblemRetrctdNippleUnderstand;
    }

    public void setFeedingProblemRetrctdNippleUnderstand(Boolean feedingProblemRetrctdNippleUnderstand) {
        this.feedingProblemRetrctdNippleUnderstand = feedingProblemRetrctdNippleUnderstand;
    }

    public Boolean getFeedingProblemFamilyUnderstand() {
        return feedingProblemFamilyUnderstand;
    }

    public void setFeedingProblemFamilyUnderstand(Boolean feedingProblemFamilyUnderstand) {
        this.feedingProblemFamilyUnderstand = feedingProblemFamilyUnderstand;
    }


}
