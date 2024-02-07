package com.argusoft.medplat.ncd.dto;

import com.argusoft.medplat.ncd.enums.*;
import com.argusoft.medplat.ncd.model.MemberGeneralDetail;
import com.argusoft.medplat.ncd.model.MemberInitialAssessmentDetail;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

public class MemberGeneralDto {

    private Integer id;
    private Integer memberId;
    private Integer locationId;
    private Integer familyId;
    private Date screeningDate;
    private DoneBy doneBy;
    private Date createdOn;
    private Integer createdBy;
    private Integer referralId;
    private Integer healthInfraId;
    private Date followUpDate;
    private String reason;
    private Integer referredFromHealthInfrastructureId;
    private Status status;
    private ReferralPlace refTo;
    private ReferralPlace refFrom;
    private String refferralPlace;
    private List<Integer> medicines;
    private String readings;
    private SubType subType;
    private String symptoms;
    private String otherDetails;
    private Boolean consultantFlag;
    private String clinicalObservation;
    private String diagnosis;
    private String remarks;
    private String comment;
    private Boolean markReview;
    private Boolean doesRequiredRef;
    private String  refferralReason;
    private List<GeneralDetailMedicineDto> medicineDetail;
    private String followupPlace;
    private MemberGeneralDetail.Category category;
    private List<String> followUpDisease;
    private String commentBy;
    private Boolean takeMedicine;
    private List<GeneralDetailMedicineDto> editedMedicineDetail;
    private List<GeneralDetailMedicineDto> deletedMedicineDetail;

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

    public Integer getHealthInfraId() {
        return healthInfraId;
    }

    public void setHealthInfraId(Integer healthInfraId) {
        this.healthInfraId = healthInfraId;
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

    public Integer getReferredFromHealthInfrastructureId() {
        return referredFromHealthInfrastructureId;
    }

    public void setReferredFromHealthInfrastructureId(Integer referredFromHealthInfrastructureId) {
        this.referredFromHealthInfrastructureId = referredFromHealthInfrastructureId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public String getRefferralPlace() {
        return refferralPlace;
    }

    public void setRefferralPlace(String refferralPlace) {
        this.refferralPlace = refferralPlace;
    }

    public List<Integer> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<Integer> medicines) {
        this.medicines = medicines;
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

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    public Boolean getConsultantFlag() {
        return consultantFlag;
    }

    public void setConsultantFlag(Boolean consultantFlag) {
        this.consultantFlag = consultantFlag;
    }

    public String getClinicalObservation() {
        return clinicalObservation;
    }

    public void setClinicalObservation(String clinicalObservation) {
        this.clinicalObservation = clinicalObservation;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getMarkReview() {
        return markReview;
    }

    public void setMarkReview(Boolean markReview) {
        this.markReview = markReview;
    }

    public Boolean getDoesRequiredRef() {
        return doesRequiredRef;
    }

    public void setDoesRequiredRef(Boolean doesRequiredRef) {
        this.doesRequiredRef = doesRequiredRef;
    }

    public String getRefferralReason() {
        return refferralReason;
    }

    public void setRefferralReason(String refferralReason) {
        this.refferralReason = refferralReason;
    }

    public List<GeneralDetailMedicineDto> getMedicineDetail() {
        return medicineDetail;
    }

    public void setMedicineDetail(List<GeneralDetailMedicineDto> medicineDetail) {
        this.medicineDetail = medicineDetail;
    }

    public String getFollowupPlace() {
        return followupPlace;
    }

    public void setFollowupPlace(String followupPlace) {
        this.followupPlace = followupPlace;
    }

    public MemberGeneralDetail.Category getCategory() {
        return category;
    }

    public void setCategory(MemberGeneralDetail.Category category) {
        this.category = category;
    }

    public List<String> getFollowUpDisease() {
        return followUpDisease;
    }

    public void setFollowUpDisease(List<String> followUpDisease) {
        this.followUpDisease = followUpDisease;
    }

    public String getCommentBy() {
        return commentBy;
    }

    public void setCommentBy(String commentBy) {
        this.commentBy = commentBy;
    }

    public Boolean getTakeMedicine() {
        return takeMedicine;
    }

    public void setTakeMedicine(Boolean takeMedicine) {
        this.takeMedicine = takeMedicine;
    }

    public List<GeneralDetailMedicineDto> getEditedMedicineDetail() {
        return editedMedicineDetail;
    }

    public void setEditedMedicineDetail(List<GeneralDetailMedicineDto> editedMedicineDetail) {
        this.editedMedicineDetail = editedMedicineDetail;
    }

    public List<GeneralDetailMedicineDto> getDeletedMedicineDetail() {
        return deletedMedicineDetail;
    }

    public void setDeletedMedicineDetail(List<GeneralDetailMedicineDto> deletedMedicineDetail) {
        this.deletedMedicineDetail = deletedMedicineDetail;
    }
}
