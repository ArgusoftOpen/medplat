package com.argusoft.medplat.ncd.model;

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
 *     Define ncd_member_breast_detail entity and its fields.
 * </p>
 * @author prateek
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "ncd_member_breast_detail")
public class MemberBreastDetail extends EntityAuditInfo implements Serializable {

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

    @Column(name = "any_breast_related_symptoms")
    private Boolean anyBreastRelatedSymptoms;

    @Column(name = "lump_in_breast")
    private Boolean lumpInBreast;

    @Column(name = "size_change")
    private Boolean sizeChange;

    @Column(name = "nipple_shape_and_position_change")
    private Boolean nippleShapeAndPositionChange;

    @Column(name = "any_retraction_of_nipple")
    private Boolean anyRetractionOfNipple;


    @Column(name = "discharge_from_nipple")
    private Boolean dischargeFromNipple;
    @Column(name = "consultant_flag")
    private Boolean consultantFlag;

    @Column(name = "redness_of_skin_over_nipple")
    private Boolean rednessOfSkinOverNipple;

    @Column(name = "skin_edema")
    private Boolean skinEdema;
    @Column(name = "erosions_of_nipple")
    private Boolean erosionsOfNipple;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "agreed_for_self_breast_exam")
    private Boolean agreedForSelfBreastExam;

    @Column(name = "swelling_in_armpit_flag")
    private String swellingInArmpitFlag;
    @Column(name = "visual_swelling_in_armpit")
    private String visualSwellingInArmpit;

    @Column(name = "visual_lump_in_breast")
    private String visualLumpInBreast;

    @Column(name = "visual_nipple_retraction_distortion")
    private String visualNippleRetractionDistortion;

    @Column(name = "visual_skin_retraction")
    private String visualSkinRetraction;
    @Column(name = "visual_skin_dimpling_retraction")
    private String visualSkinDimplingRetraction;

    @Column(name = "visual_ulceration")
    private String visualUlceration;

    @Column(name = "discharge_from_nipple_flag")
    private Boolean dischargeFromNippleFlag;

    @Column(name = "visual_discharge_from_nipple")
    private String visualDischargeFromNipple;

    @Column(name = "visual_remarks")
    private String visualRemarks;

    @Column(name = "lymphadenopathy")
    private Boolean lymphadenopathy;

    @Column(name = "done_by", length = 200)
    @Enumerated(EnumType.STRING)
    private com.argusoft.medplat.ncd.enums.DoneBy doneBy;

    @Column(name = "done_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doneOn;

    @Column(name = "refferal_done")
    private Boolean refferalDone;

    @Column(name = "refferal_place")
    private String refferalPlace;

    @Column(name = "ulceration")
    private Boolean ulceration;

    @Column(name = "nipples_not_on_same_level")
    private Boolean nippleNotOnSameLevel;

    @Column(name = "is_axillary")
    private Boolean isAxillary;

    @Column(name = "is_super_clavicular_area")
    private Boolean isSuperClavicularArea;

    @Column(name = "is_infra_clavicular_area")
    private Boolean isInfraClavicularArea;

    @Column(name = "referral_id")
    private Integer referralId;

    @Column(name = "health_infra_id")
    private Integer healthInfraId;

    @Column(name = "swelling_or_lump")
    private Boolean swellingOrLump;

    @Column(name = "puckering_or_dimpling")
    private Boolean puckeringOrDimpling;

    @Column(name = "constant_pain_in_breast")
    private Boolean constantPainInBreast;

    @Column(name = "skin_dimpling_retraction_flag")
    private Boolean skinDimplingRetractionFlag;

    @Column(name = "nipple_retraction_distortion_flag")
    private Boolean nippleRetractionDistortionFlag;

    @Column(name = "lump_in_breast_flag")
    private Boolean lumpInBreastFlag;

    @Column(name = "retraction_of_skin")
    private String retractionOfSkin;

    @Column(name = "status")
    private String status;

    @Column(name = "does_suffering")
    private Boolean doesSuffering;

    @Column(name = "retraction_of_skin_flag")
    private Boolean retractionOfSkinFlag;

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

    public Boolean getAnyBreastRelatedSymptoms() {
        return anyBreastRelatedSymptoms;
    }

    public void setAnyBreastRelatedSymptoms(Boolean anyBreastRelatedSymptoms) {
        this.anyBreastRelatedSymptoms = anyBreastRelatedSymptoms;
    }

    public Boolean getSizeChange() {
        return sizeChange;
    }

    public void setSizeChange(Boolean sizeChange) {
        this.sizeChange = sizeChange;
    }

    public Boolean getNippleShapeAndPositionChange() {
        return nippleShapeAndPositionChange;
    }

    public void setNippleShapeAndPositionChange(Boolean nippleShapeAndPositionChange) {
        this.nippleShapeAndPositionChange = nippleShapeAndPositionChange;
    }

    public Boolean getConsultantFlag() {
        return consultantFlag;
    }

    public void setConsultantFlag(Boolean consultantFlag) {
        this.consultantFlag = consultantFlag;
    }

    public Boolean getRednessOfSkinOverNipple() {
        return rednessOfSkinOverNipple;
    }

    public void setRednessOfSkinOverNipple(Boolean rednessOfSkinOverNipple) {
        this.rednessOfSkinOverNipple = rednessOfSkinOverNipple;
    }

    public Boolean getSkinEdema() {
        return skinEdema;
    }

    public void setSkinEdema(Boolean skinEdema) {
        this.skinEdema = skinEdema;
    }

    public Boolean getErosionsOfNipple() {
        return erosionsOfNipple;
    }

    public void setErosionsOfNipple(Boolean erosionsOfNipple) {
        this.erosionsOfNipple = erosionsOfNipple;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getAgreedForSelfBreastExam() {
        return agreedForSelfBreastExam;
    }

    public void setAgreedForSelfBreastExam(Boolean agreedForSelfBreastExam) {
        this.agreedForSelfBreastExam = agreedForSelfBreastExam;
    }

    public String getSwellingInArmpitFlag() {
        return swellingInArmpitFlag;
    }

    public void setSwellingInArmpitFlag(String swellingInArmpitFlag) {
        this.swellingInArmpitFlag = swellingInArmpitFlag;
    }

    public String getVisualSwellingInArmpit() {
        return visualSwellingInArmpit;
    }

    public void setVisualSwellingInArmpit(String visualSwellingInArmpit) {
        this.visualSwellingInArmpit = visualSwellingInArmpit;
    }

    public String getVisualLumpInBreast() {
        return visualLumpInBreast;
    }

    public void setVisualLumpInBreast(String visualLumpInBreast) {
        this.visualLumpInBreast = visualLumpInBreast;
    }

    public String getVisualNippleRetractionDistortion() {
        return visualNippleRetractionDistortion;
    }

    public void setVisualNippleRetractionDistortion(String visualNippleRetractionDistortion) {
        this.visualNippleRetractionDistortion = visualNippleRetractionDistortion;
    }

    public String getVisualSkinRetraction() {
        return visualSkinRetraction;
    }

    public void setVisualSkinRetraction(String visualSkinRetraction) {
        this.visualSkinRetraction = visualSkinRetraction;
    }

    public String getVisualSkinDimplingRetraction() {
        return visualSkinDimplingRetraction;
    }

    public void setVisualSkinDimplingRetraction(String visualSkinDimplingRetraction) {
        this.visualSkinDimplingRetraction = visualSkinDimplingRetraction;
    }

    public String getVisualUlceration() {
        return visualUlceration;
    }

    public void setVisualUlceration(String visualUlceration) {
        this.visualUlceration = visualUlceration;
    }

    public Boolean getDischargeFromNippleFlag() {
        return dischargeFromNippleFlag;
    }

    public void setDischargeFromNippleFlag(Boolean dischargeFromNippleFlag) {
        this.dischargeFromNippleFlag = dischargeFromNippleFlag;
    }

    public String getVisualDischargeFromNipple() {
        return visualDischargeFromNipple;
    }

    public void setVisualDischargeFromNipple(String visualDischargeFromNipple) {
        this.visualDischargeFromNipple = visualDischargeFromNipple;
    }

    public String getVisualRemarks() {
        return visualRemarks;
    }

    public void setVisualRemarks(String visualRemarks) {
        this.visualRemarks = visualRemarks;
    }

    public Boolean getLymphadenopathy() {
        return lymphadenopathy;
    }

    public void setLymphadenopathy(Boolean lymphadenopathy) {
        this.lymphadenopathy = lymphadenopathy;
    }

    public com.argusoft.medplat.ncd.enums.DoneBy getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(com.argusoft.medplat.ncd.enums.DoneBy doneBy) {
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

    public Boolean getUlceration() {
        return ulceration;
    }

    public void setUlceration(Boolean ulceration) {
        this.ulceration = ulceration;
    }

    public Boolean getNippleNotOnSameLevel() {
        return nippleNotOnSameLevel;
    }

    public void setNippleNotOnSameLevel(Boolean nippleNotOnSameLevel) {
        this.nippleNotOnSameLevel = nippleNotOnSameLevel;
    }

    public Boolean getAxillary() {
        return isAxillary;
    }

    public void setAxillary(Boolean axillary) {
        isAxillary = axillary;
    }

    public Boolean getSuperClavicularArea() {
        return isSuperClavicularArea;
    }

    public void setSuperClavicularArea(Boolean superClavicularArea) {
        isSuperClavicularArea = superClavicularArea;
    }

    public Boolean getInfraClavicularArea() {
        return isInfraClavicularArea;
    }

    public void setInfraClavicularArea(Boolean infraClavicularArea) {
        isInfraClavicularArea = infraClavicularArea;
    }

    public Integer getReferralId() {
        return referralId;
    }

    public void setReferralId(Integer referralId) {
        this.referralId = referralId;
    }

    public Integer getHealthInfraId() {
        return healthInfraId;
    }

    public void setHealthInfraId(Integer healthInfraId) {
        this.healthInfraId = healthInfraId;
    }

    public Boolean getSwellingOrLump() {
        return swellingOrLump;
    }

    public void setSwellingOrLump(Boolean swellingOrLump) {
        this.swellingOrLump = swellingOrLump;
    }

    public Boolean getPuckeringOrDimpling() {
        return puckeringOrDimpling;
    }

    public void setPuckeringOrDimpling(Boolean puckeringOrDimpling) {
        this.puckeringOrDimpling = puckeringOrDimpling;
    }

    public Boolean getConstantPainInBreast() {
        return constantPainInBreast;
    }

    public void setConstantPainInBreast(Boolean constantPainInBreast) {
        this.constantPainInBreast = constantPainInBreast;
    }

    public Boolean getSkinDimplingRetractionFlag() {
        return skinDimplingRetractionFlag;
    }

    public void setSkinDimplingRetractionFlag(Boolean skinDimplingRetractionFlag) {
        this.skinDimplingRetractionFlag = skinDimplingRetractionFlag;
    }

    public Boolean getNippleRetractionDistortionFlag() {
        return nippleRetractionDistortionFlag;
    }

    public void setNippleRetractionDistortionFlag(Boolean nippleRetractionDistortionFlag) {
        this.nippleRetractionDistortionFlag = nippleRetractionDistortionFlag;
    }

    public Boolean getLumpInBreastFlag() {
        return lumpInBreastFlag;
    }

    public void setLumpInBreastFlag(Boolean lumpInBreastFlag) {
        this.lumpInBreastFlag = lumpInBreastFlag;
    }

    public String getRetractionOfSkin() {
        return retractionOfSkin;
    }

    public void setRetractionOfSkin(String retractionOfSkin) {
        this.retractionOfSkin = retractionOfSkin;
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

    public Boolean getRetractionOfSkinFlag() {
        return retractionOfSkinFlag;
    }

    public void setRetractionOfSkinFlag(Boolean retractionOfSkinFlag) {
        this.retractionOfSkinFlag = retractionOfSkinFlag;
    }

    public Boolean getAnyRetractionOfNipple() {
        return anyRetractionOfNipple;
    }

    public void setAnyRetractionOfNipple(Boolean anyRetractionOfNipple) {
        this.anyRetractionOfNipple = anyRetractionOfNipple;
    }

    public Boolean getDischargeFromNipple() {
        return dischargeFromNipple;
    }

    public void setDischargeFromNipple(Boolean dischargeFromNipple) {
        this.dischargeFromNipple = dischargeFromNipple;
    }

    public Boolean getLumpInBreast() {
        return lumpInBreast;
    }

    public void setLumpInBreast(Boolean lumpInBreast) {
        this.lumpInBreast = lumpInBreast;
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

    public static class Fields {
        private Fields(){
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
