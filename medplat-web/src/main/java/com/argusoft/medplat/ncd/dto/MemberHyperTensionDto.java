/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dto;

import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.enums.ReferralPlace;
import com.argusoft.medplat.ncd.enums.Status;
import com.argusoft.medplat.ncd.enums.SubType;
import com.argusoft.medplat.ncd.model.MedicineMaster;
import lombok.Getter;

import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 *     Used for member hypertension.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class MemberHyperTensionDto {

    private Integer id;
    private Integer cbacId;
    private Long hmisId;
    private Integer memberId;
    private Integer locationId;
    private Integer familyId;
    private Boolean flag;
    private Integer systolicBloodPressure;
    private Integer diastolicBloodPressure;
    private Integer heartRate;
    private Date screeningDate;
    private DoneBy doneBy;
    private Date createdOn;
    private Integer createdBy;
    private Integer referralId;
    private Status status;
    private Date followUpDate;
    private String readings;
    private SubType subType;
    private String reason;
    private Integer healthInfraId;
    private List<Integer> medicines;
    private List<MedicineMaster> medicineMasters;
    private Integer referredFromHealthInfrastructureId;
    private Boolean isRegularRythm;
    private Boolean murmur;
    private Boolean bilateralClear;
    private Boolean bilateralBasalCrepitation;
    private Boolean rhonchi;
    private Boolean doesSuffering;
    private String refferalplace;
    private String remarks;
    private ReferralPlace refTo;
    private ReferralPlace refFrom;
    private List<GeneralDetailMedicineDto> medicineDetail;
    private Boolean diagnosedEarlier;
    private Boolean takeMedicine;
    private Boolean htn;
    private Boolean pedalOedema;
    private Integer hmisHealthInfraId;
    @Getter
    private Boolean currentlyUnderTreatment;
    private String pvtHealthInfraName;

    public String getPvtHealthInfraName() {
        return pvtHealthInfraName;
    }

    public void setPvtHealthInfraName(String pvtHealthInfraName) {
        this.pvtHealthInfraName = pvtHealthInfraName;
    }

    public Integer getHmisHealthInfraId() {
        return hmisHealthInfraId;
    }

    public void setHmisHealthInfraId(Integer hmisHealthInfraId) {
        this.hmisHealthInfraId = hmisHealthInfraId;
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

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Boolean getRegularRythm() {
        return isRegularRythm;
    }

    public void setRegularRythm(Boolean regularRythm) {
        isRegularRythm = regularRythm;
    }

    public void setRefFrom(ReferralPlace refFrom) {
        this.refFrom = refFrom;
    }


    public Integer getReferralId() {
        return referralId;
    }

    public void setReferralId(Integer referralId) {
        this.referralId = referralId;
    }

    public Integer getSystolicBloodPressure() {
        return systolicBloodPressure;
    }

    public void setSystolicBloodPressure(Integer systolicBloodPressure) {
        this.systolicBloodPressure = systolicBloodPressure;
    }

    public Integer getDiastolicBloodPressure() {
        return diastolicBloodPressure;
    }

    public void setDiastolicBloodPressure(Integer diastolicBloodPressure) {
        this.diastolicBloodPressure = diastolicBloodPressure;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public Date getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(Date screeningDate) {
        this.screeningDate = screeningDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public Boolean getIsRegularRythm() {
        return isRegularRythm;
    }

    public void setIsRegularRythm(Boolean isRegularRythm) {
        this.isRegularRythm = isRegularRythm;
    }

    public Boolean getMurmur() {
        return murmur;
    }

    public void setMurmur(Boolean murmur) {
        this.murmur = murmur;
    }

    public Boolean getBilateralClear() {
        return bilateralClear;
    }

    public void setBilateralClear(Boolean bilateralClear) {
        this.bilateralClear = bilateralClear;
    }

    public Boolean getBilateralBasalCrepitation() {
        return bilateralBasalCrepitation;
    }

    public void setBilateralBasalCrepitation(Boolean bilateralBasalCrepitation) {
        this.bilateralBasalCrepitation = bilateralBasalCrepitation;
    }

    public Boolean getRhonchi() {
        return rhonchi;
    }

    public void setRhonchi(Boolean rhonchi) {
        this.rhonchi = rhonchi;
    }

    public String getRefferalplace() {
        return refferalplace;
    }

    public void setRefferalplace(String refferalplace) {
        this.refferalplace = refferalplace;
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

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getReadings() {
        return readings;
    }

    public void setReadings(String readings) {
        this.readings = readings;
    }

    public SubType getSubType() {
        return subType;
    }

    public void setSubType(SubType subType) {
        this.subType = subType;
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

    public Boolean getDoesSuffering() {
        return doesSuffering;
    }

    public void setDoesSuffering(Boolean doesSuffering) {
        this.doesSuffering = doesSuffering;
    }

    public List<Integer> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<Integer> medicines) {
        this.medicines = medicines;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public Integer getReferredFromHealthInfrastructureId() {
        return referredFromHealthInfrastructureId;
    }

    public void setReferredFromHealthInfrastructureId(Integer referredFromHealthInfrastructureId) {
        this.referredFromHealthInfrastructureId = referredFromHealthInfrastructureId;
    }

    public List<MedicineMaster> getMedicineMasters() {
        return medicineMasters;
    }

    public void setMedicineMasters(List<MedicineMaster> medicineMasters) {
        this.medicineMasters = medicineMasters;
    }

    public List<GeneralDetailMedicineDto> getMedicineDetail() {
        return medicineDetail;
    }

    public void setMedicineDetail(List<GeneralDetailMedicineDto> medicineDetail) {
        this.medicineDetail = medicineDetail;
    }

    public Boolean getDiagnosedEarlier() {
        return diagnosedEarlier;
    }

    public void setDiagnosedEarlier(Boolean diagnosedEarlier) {
        this.diagnosedEarlier = diagnosedEarlier;
    }

    public Boolean getTakeMedicine() {
        return takeMedicine;
    }

    public void setTakeMedicine(Boolean takeMedicine) {
        this.takeMedicine = takeMedicine;
    }

    public Boolean getHTN() {
        return htn;
    }

    public void setHTN(Boolean HTN) {
        htn = HTN;
    }

    public Boolean getPedalOedema() {
        return pedalOedema;
    }

    public void setPedalOedema(Boolean pedalOedema) {
        this.pedalOedema = pedalOedema;
    }

    public void setCurrentlyUnderTreatment(Boolean currentlyUnderTreatment) {
        this.currentlyUnderTreatment = currentlyUnderTreatment;
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

    @Override
    public String toString() {
        return "\nMemberHyperTensionDto{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", locationId=" + locationId +
                ", familyId=" + familyId +
                ", flag=" + flag +
                ", systolicBloodPressure=" + systolicBloodPressure +
                ", diastolicBloodPressure=" + diastolicBloodPressure +
                ", heartRate=" + heartRate +
                ", screeningDate=" + screeningDate +
                ", doneBy=" + doneBy +
                ",\n createdOn=" + createdOn +
                ", \ncreatedBy=" + createdBy +
                ", referralId=" + referralId +
                ", status=" + status +
                ", followUpDate=" + followUpDate +
                ", readings='" + readings + '\'' +
                ", subType=" + subType +
                ", reason='" + reason + '\'' +
                ", healthInfraId=" + healthInfraId +
                ", medicines=" + medicines +
                ", medicineMasters=" + medicineMasters +
                ", referredFromHealthInfrastructureId=" + referredFromHealthInfrastructureId +
                ", isRegularRythm=" + isRegularRythm +
                ", murmur=" + murmur +
                ", bilateralClear=" + bilateralClear +
                ", bilateralBasalCrepitation=" + bilateralBasalCrepitation +
                ", rhonchi=" + rhonchi +
                ", refferalplace='" + refferalplace + '\'' +
                ", remarks='" + remarks + '\'' +
                ", refTo=" + refTo +
                ", refFrom=" + refFrom +
                '}';
    }
}
