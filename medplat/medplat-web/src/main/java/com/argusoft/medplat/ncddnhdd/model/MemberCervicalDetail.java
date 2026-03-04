package com.argusoft.medplat.ncddnhdd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Define ncd_member_cervical_detail entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "ncd_member_cervical_detail")
public class MemberCervicalDetail extends EntityAuditInfo implements Serializable {

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

    @Column(name = "cervical_related_symptoms")
    private Boolean cervicalRelatedSymptoms;

    @Column(name = "excessive_bleeding_during_periods")
    private Boolean excessiveBleedingDuringPeriods;

    @Column(name = "bleeding_between_periods")
    private Boolean bleedingBetweenPeriods;

    @Column(name = "bleeding_after_intercourse")
    private Boolean bleedingAfterIntercourse;

    @Column(name = "excessive_smelling_vaginal_discharge")
    private Boolean excessiveSmellingVaginalDischarge;

    @Column(name = "postmenopausal_bleeding")
    private Boolean postmenopausalBleeding;

    @Column(name = "refferal_done")
    private Boolean refferalDone;

    @Column(name = "refferal_place")
    private String refferalPlace;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "polyp")
    private Boolean polyp;

    @Column(name = "ectopy")
    private Boolean ectopy;

    @Column(name = "hypertrophy")
    private Boolean hypertrophy;

    @Column(name = "prolapse_uterus")
    private Boolean prolapseUterus;

    @Column(name = "bleeds_on_touch")
    private Boolean bleedsOnTouch;

    @Column(name = "unhealthy_cervix")
    private Boolean unhealthyCervix;

    @Column(name = "suspicious_looking_cervix")
    private Boolean suspiciousLookingCervix;

    @Column(name = "frank_malignancy")
    private Boolean frankMalignancy;

    @Column(name = "other_symptoms")
    private Boolean otherSymptoms;

    @Column(name = "other_desc")
    private String otherDesc;

    @Column(name = "via_exam")
    private String viaExam;

    @Column(name = "done_by", length = 200)
    @Enumerated(EnumType.STRING)
    private com.argusoft.medplat.ncddnhdd.enums.DoneBy doneBy;

    @Column(name = "done_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doneOn;

    @Column(name = "other_clinical_examination")
    private String otherClinicalExamination;

    @Column(name = "via_exam_points")
    private String viaExamPoints;

    @Column(name = "referral_id")
    private Integer referralId;

    @Column(name = "papsmear_test")
    private Boolean papsmearTest;

    @Column(name = "via_test")
    private String viaTest;

    @Column(name = "health_infra_id")
    private Integer healthInfraId;

    @Column(name = "trained_via_examination")
    private Boolean trainedViaExamination;

    @Column(name = "excessive_discharge")
    private Boolean excessiveDischarge;

    @Column(name = "visual_polyp")
    private String visualPolyp;

    @Column(name = "visual_ectopy")
    private String visualEctopy;

    @Column(name = "visual_hypertrophy")
    private String visualHypertrophy;

    @Column(name = "visual_bleeds_on_touch")
    private String visualBleedsOnTouch;

    @Column(name = "visual_unhealthy_cervix")
    private String visualUnhealthyCervix;

    @Column(name = "visual_suspicious_looking")
    private String visualSuspiciousLooking;

    @Column(name = "visual_frank_growth")
    private String visualFrankGrowth;

    @Column(name = "visual_prolapse_uterus")
    private String visualProlapseUterus;

    @Column(name = "other_symptoms_description")
    private String otherSymptomsDescription;

    @Column(name = "bimanual_examination")
    private String bimanualExamination;

    @Column(name = "other_finding")
    private Boolean otherFinding;

    @Column(name = "other_finding_description")
    private String otherFindingDescription;

    @Column(name = "does_suffering")
    private Boolean doesSuffering;

    @Column(name = "external_genitalia_healthy")
    private Boolean externalGenitaliaHealthy;

    @Column(name = "consultant_flag")
    private Boolean consultantFlag;

    @Column(name = "status")
    private String status;

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

    @Column(name = "future_screening_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date futureScreeningDate;

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

    public Boolean getCervicalRelatedSymptoms() {
        return cervicalRelatedSymptoms;
    }

    public void setCervicalRelatedSymptoms(Boolean cervicalRelatedSymptoms) {
        this.cervicalRelatedSymptoms = cervicalRelatedSymptoms;
    }

    public Boolean getExcessiveBleedingDuringPeriods() {
        return excessiveBleedingDuringPeriods;
    }

    public void setExcessiveBleedingDuringPeriods(Boolean excessiveBleedingDuringPeriods) {
        this.excessiveBleedingDuringPeriods = excessiveBleedingDuringPeriods;
    }

    public Boolean getBleedingBetweenPeriods() {
        return bleedingBetweenPeriods;
    }

    public void setBleedingBetweenPeriods(Boolean bleedingBetweenPeriods) {
        this.bleedingBetweenPeriods = bleedingBetweenPeriods;
    }

    public Boolean getBleedingAfterIntercourse() {
        return bleedingAfterIntercourse;
    }

    public void setBleedingAfterIntercourse(Boolean bleedingAfterIntercourse) {
        this.bleedingAfterIntercourse = bleedingAfterIntercourse;
    }

    public Boolean getExcessiveSmellingVaginalDischarge() {
        return excessiveSmellingVaginalDischarge;
    }

    public void setExcessiveSmellingVaginalDischarge(Boolean excessiveSmellingVaginalDischarge) {
        this.excessiveSmellingVaginalDischarge = excessiveSmellingVaginalDischarge;
    }

    public Boolean getPostmenopausalBleeding() {
        return postmenopausalBleeding;
    }

    public void setPostmenopausalBleeding(Boolean postmenopausalBleeding) {
        this.postmenopausalBleeding = postmenopausalBleeding;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getPolyp() {
        return polyp;
    }

    public void setPolyp(Boolean polyp) {
        this.polyp = polyp;
    }

    public Boolean getEctopy() {
        return ectopy;
    }

    public void setEctopy(Boolean ectopy) {
        this.ectopy = ectopy;
    }

    public Boolean getHypertrophy() {
        return hypertrophy;
    }

    public void setHypertrophy(Boolean hypertrophy) {
        this.hypertrophy = hypertrophy;
    }

    public Boolean getProlapseUterus() {
        return prolapseUterus;
    }

    public void setProlapseUterus(Boolean prolapseUterus) {
        this.prolapseUterus = prolapseUterus;
    }

    public Boolean getBleedsOnTouch() {
        return bleedsOnTouch;
    }

    public void setBleedsOnTouch(Boolean bleedsOnTouch) {
        this.bleedsOnTouch = bleedsOnTouch;
    }

    public Boolean getUnhealthyCervix() {
        return unhealthyCervix;
    }

    public void setUnhealthyCervix(Boolean unhealthyCervix) {
        this.unhealthyCervix = unhealthyCervix;
    }

    public Boolean getSuspiciousLookingCervix() {
        return suspiciousLookingCervix;
    }

    public void setSuspiciousLookingCervix(Boolean suspiciousLookingCervix) {
        this.suspiciousLookingCervix = suspiciousLookingCervix;
    }

    public Boolean getFrankMalignancy() {
        return frankMalignancy;
    }

    public void setFrankMalignancy(Boolean frankMalignancy) {
        this.frankMalignancy = frankMalignancy;
    }

    public String getOtherDesc() {
        return otherDesc;
    }

    public void setOtherDesc(String otherDesc) {
        this.otherDesc = otherDesc;
    }

    public String getViaExam() {
        return viaExam;
    }

    public void setViaExam(String viaExam) {
        this.viaExam = viaExam;
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

    public String getOtherClinicalExamination() {
        return otherClinicalExamination;
    }

    public void setOtherClinicalExamination(String otherClinicalExamination) {
        this.otherClinicalExamination = otherClinicalExamination;
    }

    public String getViaExamPoints() {
        return viaExamPoints;
    }

    public void setViaExamPoints(String viaExamPoints) {
        this.viaExamPoints = viaExamPoints;
    }

    public Integer getReferralId() {
        return referralId;
    }

    public void setReferralId(Integer referralId) {
        this.referralId = referralId;
    }

    public Boolean getPapsmearTest() {
        return papsmearTest;
    }

    public void setPapsmearTest(Boolean papsmearTest) {
        this.papsmearTest = papsmearTest;
    }

    public String getViaTest() {
        return viaTest;
    }

    public void setViaTest(String viaTest) {
        this.viaTest = viaTest;
    }

    public Integer getHealthInfraId() {
        return healthInfraId;
    }

    public void setHealthInfraId(Integer healthInfraId) {
        this.healthInfraId = healthInfraId;
    }

    public Boolean getTrainedViaExamination() {
        return trainedViaExamination;
    }

    public void setTrainedViaExamination(Boolean trainedViaExamination) {
        this.trainedViaExamination = trainedViaExamination;
    }

    public Boolean getExcessiveDischarge() {
        return excessiveDischarge;
    }

    public void setExcessiveDischarge(Boolean excessiveDischarge) {
        this.excessiveDischarge = excessiveDischarge;
    }

    public String getVisualPolyp() {
        return visualPolyp;
    }

    public void setVisualPolyp(String visualPolyp) {
        this.visualPolyp = visualPolyp;
    }

    public String getVisualEctopy() {
        return visualEctopy;
    }

    public void setVisualEctopy(String visualEctopy) {
        this.visualEctopy = visualEctopy;
    }

    public String getVisualHypertrophy() {
        return visualHypertrophy;
    }

    public void setVisualHypertrophy(String visualHypertrophy) {
        this.visualHypertrophy = visualHypertrophy;
    }

    public String getVisualBleedsOnTouch() {
        return visualBleedsOnTouch;
    }

    public void setVisualBleedsOnTouch(String visualBleedsOnTouch) {
        this.visualBleedsOnTouch = visualBleedsOnTouch;
    }

    public String getVisualUnhealthyCervix() {
        return visualUnhealthyCervix;
    }

    public void setVisualUnhealthyCervix(String visualUnhealthyCervix) {
        this.visualUnhealthyCervix = visualUnhealthyCervix;
    }

    public String getVisualSuspiciousLooking() {
        return visualSuspiciousLooking;
    }

    public void setVisualSuspiciousLooking(String visualSuspiciousLooking) {
        this.visualSuspiciousLooking = visualSuspiciousLooking;
    }

    public String getVisualFrankGrowth() {
        return visualFrankGrowth;
    }

    public void setVisualFrankGrowth(String visualFrankGrowth) {
        this.visualFrankGrowth = visualFrankGrowth;
    }

    public String getVisualProlapseUterus() {
        return visualProlapseUterus;
    }

    public void setVisualProlapseUterus(String visualProlapseUterus) {
        this.visualProlapseUterus = visualProlapseUterus;
    }

    public String getBimanualExamination() {
        return bimanualExamination;
    }

    public void setBimanualExamination(String bimanualExamination) {
        this.bimanualExamination = bimanualExamination;
    }

    public Boolean getOtherSymptoms() {
        return otherSymptoms;
    }

    public void setOtherSymptoms(Boolean otherSymptoms) {
        this.otherSymptoms = otherSymptoms;
    }

    public String getOtherSymptomsDescription() {
        return otherSymptomsDescription;
    }

    public void setOtherSymptomsDescription(String otherSymptomsDescription) {
        this.otherSymptomsDescription = otherSymptomsDescription;
    }

    public Boolean getOtherFinding() {
        return otherFinding;
    }

    public void setOtherFinding(Boolean otherFinding) {
        this.otherFinding = otherFinding;
    }

    public String getOtherFindingDescription() {
        return otherFindingDescription;
    }

    public void setOtherFindingDescription(String otherFindingDescription) {
        this.otherFindingDescription = otherFindingDescription;
    }

    public Boolean getDoesSuffering() {
        return doesSuffering;
    }

    public void setDoesSuffering(Boolean doesSuffering) {
        this.doesSuffering = doesSuffering;
    }

    public Boolean getExternalGenitaliaHealthy() {
        return externalGenitaliaHealthy;
    }

    public void setExternalGenitaliaHealthy(Boolean externalGenitaliaHealthy) {
        this.externalGenitaliaHealthy = externalGenitaliaHealthy;
    }

    public Boolean getConsultantFlag() {
        return consultantFlag;
    }

    public void setConsultantFlag(Boolean consultantFlag) {
        this.consultantFlag = consultantFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Date getFutureScreeningDate() {
        return futureScreeningDate;
    }

    public void setFutureScreeningDate(Date futureScreeningDate) {
        this.futureScreeningDate = futureScreeningDate;
    }

    /**
     * Define fields name for ncd_member_cervical_detail.
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
        public static final String REFERRAL_PLACE = "refferalPlace";
        public static final String REMARKS = "remarks";
        public static final String OTHER = "other";
        public static final String DONE_BY = "doneBy";
    }

}
