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
 *     Define ncd_member_oral_detail entity and its fields.
 * </p>
 * @author prateek
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "ncd_member_oral_detail")
public class MemberOralDetail extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "family_id")
    private Integer familyId;

    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "mobile_start_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date mobileStartDate;

    @Column(name = "mobile_end_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date mobileEndDate;

    @Column(name = "screening_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date screeningDate;

    @Column(name = "any_issues_in_mouth")
    private Boolean anyIssuesInMouth;

    @Column(name = "white_red_patch_oral_cavity")
    private Boolean whiteRedPatchOralCavity;

    @Column(name = "difficulty_in_spicy_food")
    private Boolean difficultyInSpicyFood;

    @Column(name = "voice_change")
    private Boolean voiceChange;

    @Column(name = "difficulty_in_opening_mouth")
    private Boolean difficultyInOpeningMouth;

    @Column(name = "three_weeks_mouth_ulcer")
    private Boolean threeWeeksMouthUlcer;

    @Column(name = "growth_of_recent_origin_flag")
    private Boolean growthOfRecentOriginFlag;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "non_healing_ulcers")
    private String nonHealingUlcers;

    @Column(name = "growth_of_recent_origins")
    private String growthOfRecentOrigins;

    @Column(name = "submucous_fibrosis")
    private String submucousFibrosis;

    @Column(name = "flag")
    private Boolean flag;

    @Column(name = "smokers_palate")
    private String smokersPalate;

    @Column(name = "lichen_planus")
    private String lichenPlanus;

    @Column(name = "red_patches")
    private String redPatches;

    @Column(name = "white_patches")
    private String whitePatches;

    @Column(name = "restricted_mouth_opening")
    private String restrictedMouthOpening;

    @Column(name = "done_by", length = 200)
    @Enumerated(EnumType.STRING)
    private com.argusoft.medplat.ncddnhdd.enums.DoneBy doneBy;

    @Column(name = "done_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doneOn;

    @Column(name = "refferal_done")
    private Boolean refferalDone;

    @Column(name = "refferal_place")
    private String refferalPlace;

    @Column(name = "other_symptoms")
    private String otherSymptoms;

    @Column(name = "referral_id")
    private Integer referralId;

    @Column(name = "health_infra_id")
    private Integer healthInfraId;

    @Column(name = "is_red_patch")
    private Boolean isRedPatch;

    @Column(name = "symptoms_remarks")
    private String symptomsRemarks;

    @Column(name = "white_or_red_patch")
    private Boolean whiteOrRedPatch;

    @Column(name = "Ulceration_roughened_areas")
    private Boolean UlcerationRoughenedAreas;

    @Column(name = "ulcer")
    private String ulcer;

    @Column(name = "status")
    private String status;

    @Column(name = "does_suffering")
    private Boolean doesSuffering;

    @Column(name = "master_id")
    private Integer masterId;

    @Column(name = "is_suspected")
    private Boolean isSuspected;
    @Column(name = "take_medicine")
    private Boolean takeMedicine;
    @Column(name = "treatment_status")
    private String treatmentStatus;

    @Column(name = "cancer_screening_master_id")
    private Integer cancerScreeningMasterId;

    @Column(name = "diagnosed_earlier")
    private Boolean diagnosedEarlier;

    @Column(name = "currently_under_treatment")
    private Boolean currentlyUnderTreatment;

    @Column(name = "current_treatment_place")
    private String currentTreatmentPlace;

    @Column(name = "govt_facility_id")
    private Integer govtFacilityId;

    @Column(name = "private_facility")
    private String privateFacility;

    @Column(name = "out_of_territory_facility")
    private String outOfTerritoryFacility;

    public String getSubmucousFibrosis() {
        return submucousFibrosis;
    }

    public void setSubmucousFibrosis(String submucousFibrosis) {
        this.submucousFibrosis = submucousFibrosis;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getSmokersPalate() {
        return smokersPalate;
    }

    public void setSmokersPalate(String smokersPalate) {
        this.smokersPalate = smokersPalate;
    }

    public String getLichenPlanus() {
        return lichenPlanus;
    }

    public void setLichenPlanus(String lichenPlanus) {
        this.lichenPlanus = lichenPlanus;
    }

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

    public Date getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(Date screeningDate) {
        this.screeningDate = screeningDate;
    }

    public Boolean getAnyIssuesInMouth() {
        return anyIssuesInMouth;
    }

    public void setAnyIssuesInMouth(Boolean anyIssuesInMouth) {
        this.anyIssuesInMouth = anyIssuesInMouth;
    }

    public Boolean getWhiteRedPatchOralCavity() {
        return whiteRedPatchOralCavity;
    }

    public void setWhiteRedPatchOralCavity(Boolean whiteRedPatchOralCavity) {
        this.whiteRedPatchOralCavity = whiteRedPatchOralCavity;
    }

    public Boolean getDifficultyInSpicyFood() {
        return difficultyInSpicyFood;
    }

    public void setDifficultyInSpicyFood(Boolean difficultyInSpicyFood) {
        this.difficultyInSpicyFood = difficultyInSpicyFood;
    }

    public Boolean getVoiceChange() {
        return voiceChange;
    }

    public void setVoiceChange(Boolean voiceChange) {
        this.voiceChange = voiceChange;
    }

    public Boolean getDifficultyInOpeningMouth() {
        return difficultyInOpeningMouth;
    }

    public void setDifficultyInOpeningMouth(Boolean difficultyInOpeningMouth) {
        this.difficultyInOpeningMouth = difficultyInOpeningMouth;
    }

    public Boolean getThreeWeeksMouthUlcer() {
        return threeWeeksMouthUlcer;
    }

    public void setThreeWeeksMouthUlcer(Boolean threeWeeksMouthUlcer) {
        this.threeWeeksMouthUlcer = threeWeeksMouthUlcer;
    }

    public Boolean getGrowthOfRecentOriginFlag() {
        return growthOfRecentOriginFlag;
    }

    public void setGrowthOfRecentOriginFlag(Boolean growthOfRecentOriginFlag) {
        this.growthOfRecentOriginFlag = growthOfRecentOriginFlag;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getNonHealingUlcers() {
        return nonHealingUlcers;
    }

    public void setNonHealingUlcers(String nonHealingUlcers) {
        this.nonHealingUlcers = nonHealingUlcers;
    }

    public String getGrowthOfRecentOrigins() {
        return growthOfRecentOrigins;
    }

    public void setGrowthOfRecentOrigins(String growthOfRecentOrigins) {
        this.growthOfRecentOrigins = growthOfRecentOrigins;
    }

    public String getRedPatches() {
        return redPatches;
    }

    public void setRedPatches(String redPatches) {
        this.redPatches = redPatches;
    }

    public String getWhitePatches() {
        return whitePatches;
    }

    public void setWhitePatches(String whitePatches) {
        this.whitePatches = whitePatches;
    }

    public String getRestrictedMouthOpening() {
        return restrictedMouthOpening;
    }

    public void setRestrictedMouthOpening(String restrictedMouthOpening) {
        this.restrictedMouthOpening = restrictedMouthOpening;
    }

    public com.argusoft.medplat.ncddnhdd.enums.DoneBy getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(com.argusoft.medplat.ncddnhdd.enums.DoneBy doneBy) {
        this.doneBy = doneBy;
    }

    public Date getDoneOn() {
        return doneOn;
    }

    public void setDoneOn(Date doneOn) {
        this.doneOn = doneOn;
    }

    public Boolean getRefferalDone() {
        return refferalDone;
    }

    public void setRefferalDone(Boolean refferalDone) {
        this.refferalDone = refferalDone;
    }

    public String getRefferalPlace() {
        return refferalPlace;
    }

    public void setRefferalPlace(String refferalPlace) {
        this.refferalPlace = refferalPlace;
    }

    public String getOtherSymptoms() {
        return otherSymptoms;
    }

    public void setOtherSymptoms(String otherSymptoms) {
        this.otherSymptoms = otherSymptoms;
    }

    public Integer getHealthInfraId() {
        return healthInfraId;
    }

    public void setHealthInfraId(Integer healthInfraId) {
        this.healthInfraId = healthInfraId;
    }

    public Boolean getRedPatch() {
        return isRedPatch;
    }

    public void setRedPatch(Boolean redPatch) {
        isRedPatch = redPatch;
    }

    public String getSymptomsRemarks() {
        return symptomsRemarks;
    }

    public void setSymptomsRemarks(String symptomsRemarks) {
        this.symptomsRemarks = symptomsRemarks;
    }

    public Boolean getWhiteOrRedPatch() {
        return whiteOrRedPatch;
    }

    public void setWhiteOrRedPatch(Boolean whiteOrRedPatch) {
        this.whiteOrRedPatch = whiteOrRedPatch;
    }

    public Boolean getUlcerationRoughenedAreas() {
        return UlcerationRoughenedAreas;
    }

    public void setUlcerationRoughenedAreas(Boolean ulcerationRoughenedAreas) {
        UlcerationRoughenedAreas = ulcerationRoughenedAreas;
    }

    public String getUlcer() {
        return ulcer;
    }

    public void setUlcer(String ulcer) {
        this.ulcer = ulcer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDoesSuffering() {
        return doesSuffering;
    }

    public void setDoesSuffering(Boolean doesSuffering) {
        this.doesSuffering = doesSuffering;
    }

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    public Boolean getSuspected() {
        return isSuspected;
    }

    public void setSuspected(Boolean suspected) {
        isSuspected = suspected;
    }

    public Boolean getTakeMedicine() {
        return takeMedicine;
    }

    public void setTakeMedicine(Boolean takeMedicine) {
        this.takeMedicine = takeMedicine;
    }

    public String getTreatmentStatus() {
        return treatmentStatus;
    }

    public void setTreatmentStatus(String treatmentStatus) {
        this.treatmentStatus = treatmentStatus;
    }

    public Integer getCancerScreeningMasterId() {
        return cancerScreeningMasterId;
    }

    public void setCancerScreeningMasterId(Integer cancerScreeningMasterId) {
        this.cancerScreeningMasterId = cancerScreeningMasterId;
    }

    public Boolean getDiagnosedEarlier() {
        return diagnosedEarlier;
    }

    public void setDiagnosedEarlier(Boolean diagnosedEarlier) {
        this.diagnosedEarlier = diagnosedEarlier;
    }

    public Boolean getCurrentlyUnderTreatment() {
        return currentlyUnderTreatment;
    }

    public void setCurrentlyUnderTreatment(Boolean currentlyUnderTreatment) {
        this.currentlyUnderTreatment = currentlyUnderTreatment;
    }

    public String getCurrentTreatmentPlace() {
        return currentTreatmentPlace;
    }

    public void setCurrentTreatmentPlace(String currentTreatmentPlace) {
        this.currentTreatmentPlace = currentTreatmentPlace;
    }

    public Integer getGovtFacilityId() {
        return govtFacilityId;
    }

    public void setGovtFacilityId(Integer govtFacilityId) {
        this.govtFacilityId = govtFacilityId;
    }

    public String getPrivateFacility() {
        return privateFacility;
    }

    public void setPrivateFacility(String privateFacility) {
        this.privateFacility = privateFacility;
    }

    public String getOutOfTerritoryFacility() {
        return outOfTerritoryFacility;
    }

    public void setOutOfTerritoryFacility(String outOfTerritoryFacility) {
        this.outOfTerritoryFacility = outOfTerritoryFacility;
    }

    /**
     * Define fields name for ncd_member_oral_detail.
     */
    public static class Fields {
        private Fields() {
            throw new IllegalStateException("Utility class");
        }
        public static final String ID = "id";
        public static final String MEMBER_ID = "memberId";
        public static final String FAMILY_ID = "familyId";
        public static final String LOCATION_ID = "locationId";
        public static final String SCREENING_DATE = "screeningDate";
        public static final String REMARKS = "remarks";
        public static final String DONE_BY = "doneBy";
        public static final String REFERRAL_PLACE = "refferalPlace";
    }

}
