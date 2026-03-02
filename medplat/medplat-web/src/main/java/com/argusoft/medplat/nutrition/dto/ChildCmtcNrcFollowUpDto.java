
package com.argusoft.medplat.nutrition.dto;

import java.util.Date;
import java.util.Set;

/**
 * <p>
 *     Defines fields for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) followup
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
public class ChildCmtcNrcFollowUpDto {

    private Integer id;
    private Integer childId;
    private Integer admissionId;
    private Integer caseId;
    private Integer referredBy;
    private Integer followUpVisit;
    private Date followUpDate;
    private String bilateralPittingOedema;
    private Float weight;
    private Integer height;
    private Float midUpperArmCircumference;
    private Set<String> illness;
    private String otherIllness;
    private String sdScore;
    private String programOutput;
    private Date dischargeFromProgram;
    private Boolean otherCmtcCenterFollowUp;
    private Integer followupOtherCenter;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public Integer getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(Integer referredBy) {
        this.referredBy = referredBy;
    }

    public Integer getFollowUpVisit() {
        return followUpVisit;
    }

    public void setFollowUpVisit(Integer followUpVisit) {
        this.followUpVisit = followUpVisit;
    }

    public Date getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(Date followUpDate) {
        this.followUpDate = followUpDate;
    }

    public String getBilateralPittingOedema() {
        return bilateralPittingOedema;
    }

    public void setBilateralPittingOedema(String bilateralPittingOedema) {
        this.bilateralPittingOedema = bilateralPittingOedema;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Float getMidUpperArmCircumference() {
        return midUpperArmCircumference;
    }

    public void setMidUpperArmCircumference(Float midUpperArmCircumference) {
        this.midUpperArmCircumference = midUpperArmCircumference;
    }

    public String getSdScore() {
        return sdScore;
    }

    public void setSdScore(String sdScore) {
        this.sdScore = sdScore;
    }

    public String getProgramOutput() {
        return programOutput;
    }

    public void setProgramOutput(String programOutput) {
        this.programOutput = programOutput;
    }

    public Date getDischargeFromProgram() {
        return dischargeFromProgram;
    }

    public void setDischargeFromProgram(Date dischargeFromProgram) {
        this.dischargeFromProgram = dischargeFromProgram;
    }

    public String getOtherIllness() {
        return otherIllness;
    }

    public void setOtherIllness(String otherIllness) {
        this.otherIllness = otherIllness;
    }

    public Set<String> getIllness() {
        return illness;
    }

    public void setIllness(Set<String> illness) {
        this.illness = illness;
    }

    public Boolean getOtherCmtcCenterFollowUp() {
        return otherCmtcCenterFollowUp;
    }

    public void setOtherCmtcCenterFollowUp(Boolean otherCmtcCenterFollowUp) {
        this.otherCmtcCenterFollowUp = otherCmtcCenterFollowUp;
    }

    public Integer getFollowupOtherCenter() {
        return followupOtherCenter;
    }

    public void setFollowupOtherCenter(Integer followupOtherCenter) {
        this.followupOtherCenter = followupOtherCenter;
    }

    public Integer getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(Integer admissionId) {
        this.admissionId = admissionId;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }
}
