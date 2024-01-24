/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dto;

import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.enums.ReferralPlace;
import com.argusoft.medplat.ncd.enums.Status;

import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 *     Used for member oral.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class MemberOralDto {

    private Integer id;
    private Integer cbacId;
    private Long hmisId;
    private Integer memberId;
    private Integer locationId;
    private Integer familyId;
    private String restrictedMouthOpening;
    private String submucousFibrosis;
    private String smokersPalate;
    private String lichenPlanus;
    private String whitePatches;
    private String redPatches;
    private String nonHealingUlcers;
    private String growthOfRecentOrigins;
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
    private Boolean anyIssuesInMouth;
    private Boolean whiteRedPatchOralCavity;
    private Boolean difficultyInSpicyFood;
    private Boolean voiceChange;
    private Boolean difficultyInOpeningMouth;
    private Boolean threeWeeksMouthUlcer;
    private Boolean flag;
    private String remarks;
    private String otherSymptoms;
    private String refferalPlace;
    private ReferralPlace refTo;
    private ReferralPlace refFrom;
    private String ulcer;
    private Boolean doesSuffering;
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

    public String getSubmucousFibrosis() {
        return submucousFibrosis;
    }

    public void setSubmucousFibrosis(String submucousFibrosis) {
        this.submucousFibrosis = submucousFibrosis;
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

    public String getRestrictedMouthOpening() {
        return restrictedMouthOpening;
    }

    public void setRestrictedMouthOpening(String restrictedMouthOpening) {
        this.restrictedMouthOpening = restrictedMouthOpening;
    }

    public String getWhitePatches() {
        return whitePatches;
    }

    public void setWhitePatches(String whitePatches) {
        this.whitePatches = whitePatches;
    }

    public String getRedPatches() {
        return redPatches;
    }

    public void setRedPatches(String redPatches) {
        this.redPatches = redPatches;
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

    public Date getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(Date screeningDate) {
        this.screeningDate = screeningDate;
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

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Boolean getThreeWeeksMouthUlcer() {
        return threeWeeksMouthUlcer;
    }

    public void setThreeWeeksMouthUlcer(Boolean threeWeeksMouthUlcer) {
        this.threeWeeksMouthUlcer = threeWeeksMouthUlcer;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getOtherSymptoms() {
        return otherSymptoms;
    }

    public void setOtherSymptoms(String otherSymptoms) {
        this.otherSymptoms = otherSymptoms;
    }

    public String getRefferalPlace() {
        return refferalPlace;
    }

    public void setRefferalPlace(String refferalPlace) {
        this.refferalPlace = refferalPlace;
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

    public String getUlcer() {
        return ulcer;
    }

    public void setUlcer(String ulcer) {
        this.ulcer = ulcer;
    }

    public Boolean getDoesSuffering() {
        return doesSuffering;
    }

    public void setDoesSuffering(Boolean doesSuffering) {
        this.doesSuffering = doesSuffering;
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
