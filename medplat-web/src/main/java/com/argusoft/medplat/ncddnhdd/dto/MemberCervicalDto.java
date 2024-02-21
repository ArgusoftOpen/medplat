/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.dto;

import com.argusoft.medplat.ncddnhdd.enums.DoneBy;
import com.argusoft.medplat.ncddnhdd.enums.ReferralPlace;
import com.argusoft.medplat.ncddnhdd.enums.Status;

import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 *     Used for member cervical.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class MemberCervicalDto {

    private Integer id;
    private Integer cbacId;
    private Long hmisId;
    private Integer memberId;
    private Integer locationId;
    private Integer familyId;
    private Boolean papsmearTest;
    private String viaTest;
    private Boolean excessiveDischarge;
    private Date screeningDate;
    private DoneBy doneBy;
    private Date createdOn;
    private Integer createdBy;
    private Integer referralId;
    private Status status;
    private Date followUpDate;
    private String reason;
    private Integer healthInfraId;
    private Integer referredFromHealthInfrastructureId;
    private Boolean cervicalRelatedSymptoms;
    private Boolean externalGenitaliaHealthy;
    private Boolean excessiveBleedingDuringPeriods;
    private Boolean bleedingBetweenPeriods;
    private Boolean bleedingDfterIntercourse;
    private Boolean excessiveSmellingVaginalDischarge;
    private Boolean postmenopausalBleeding;
    private Boolean polyp;
    private Boolean ectopy;
    private Boolean hypertrophy;
    private Boolean prolapseUterus;
    private Boolean bleedsOnTouch;
    private Boolean unhealthyCervix;
    private Boolean suspiciousLookingCervix;
    private Boolean frankMalignancy;
    private Boolean otherSymptoms;
    private Boolean otherFinding;
    private String otherSymptomsDescription;
    private String otherDesc;
    private Boolean consultantFlag;
    private String bimanualExamination;
    private String otherFindingDescription;
    private Boolean doesSuffering;
    private String viaExam;
    private String otherClinicalExamination;
    private String viaExamPoints;
    private String refferralPlace;
    private ReferralPlace refTo;
    private ReferralPlace refFrom;
    private List<GeneralDetailMedicineDto> medicineDetail;
    private Boolean takeMedicine;
    private Boolean htn;
    private String pvtHealthInfraName;

    public String getPvtHealthInfraName() {
        return pvtHealthInfraName;
    }

    public void setPvtHealthInfraName(String pvtHealthInfraName) {
        this.pvtHealthInfraName = pvtHealthInfraName;
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

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
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

    public Date getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(Date screeningDate) {
        this.screeningDate = screeningDate;
    }

    public Boolean getExternalGenitaliaHealthy() {
        return externalGenitaliaHealthy;
    }

    public void setExternalGenitaliaHealthy(Boolean externalGenitaliaHealthy) {
        this.externalGenitaliaHealthy = externalGenitaliaHealthy;
    }

    public DoneBy getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(DoneBy doneBy) {
        this.doneBy = doneBy;
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

    public Integer getReferralId() {
        return referralId;
    }

    public Boolean getExcessiveDischarge() {
        return excessiveDischarge;
    }

    public void setExcessiveDischarge(Boolean excessiveDischarge) {
        this.excessiveDischarge = excessiveDischarge;
    }

    public void setReferralId(Integer referralId) {
        this.referralId = referralId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(Date followUpDate) {
        this.followUpDate = followUpDate;
    }

    public Boolean getConsultantFlag() {
        return consultantFlag;
    }

    public void setConsultantFlag(Boolean consultantFlag) {
        this.consultantFlag = consultantFlag;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getHealthInfraId() {
        return healthInfraId;
    }

    public void setHealthInfraId(Integer healthInfraId) {
        this.healthInfraId = healthInfraId;
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

    public String getBimanualExamination() {
        return bimanualExamination;
    }

    public void setBimanualExamination(String bimanualExamination) {
        this.bimanualExamination = bimanualExamination;
    }



    public Boolean getDoesSuffering() {
        return doesSuffering;
    }

    public void setDoesSuffering(Boolean doesSuffering) {
        this.doesSuffering = doesSuffering;
    }

    public void setBleedingBetweenPeriods(Boolean bleedingBetweenPeriods) {
        this.bleedingBetweenPeriods = bleedingBetweenPeriods;
    }

    public Boolean getBleedingDfterIntercourse() {
        return bleedingDfterIntercourse;
    }

    public void setBleedingDfterIntercourse(Boolean bleedingDfterIntercourse) {
        this.bleedingDfterIntercourse = bleedingDfterIntercourse;
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

    public String getRefferralPlace() {
        return refferralPlace;
    }

    public void setRefferralPlace(String refferralPlace) {
        this.refferralPlace = refferralPlace;
    }

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

    public Integer getReferredFromHealthInfrastructureId() {
        return referredFromHealthInfrastructureId;
    }

    public void setReferredFromHealthInfrastructureId(Integer referredFromHealthInfrastructureId) {
        this.referredFromHealthInfrastructureId = referredFromHealthInfrastructureId;
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

    public List<GeneralDetailMedicineDto> getMedicineDetail() {
        return medicineDetail;
    }

    public void setMedicineDetail(List<GeneralDetailMedicineDto> medicineDetail) {
        this.medicineDetail = medicineDetail;
    }

    public Boolean getTakeMedicine() {
        return takeMedicine;
    }

    public void setTakeMedicine(Boolean takeMedicine) {
        this.takeMedicine = takeMedicine;
    }

    public Boolean getHtn() {
        return htn;
    }

    public void setHtn(Boolean htn) {
        this.htn = htn;
    }

    public Integer getCbacId() {
        return cbacId;
    }

    public void setCbacId(Integer cbacId) {
        this.cbacId = cbacId;
    }

    public Long getHmisId() {
        return hmisId;
    }

    public void setHmisId(Long hmisId) {
        this.hmisId = hmisId;
    }
}
