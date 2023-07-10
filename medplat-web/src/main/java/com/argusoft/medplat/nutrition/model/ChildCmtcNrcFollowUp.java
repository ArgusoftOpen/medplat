
package com.argusoft.medplat.nutrition.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * <p>
 *     Defines fields for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) followup
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
@Entity
@Table(name = "child_cmtc_nrc_follow_up")
public class ChildCmtcNrcFollowUp extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "child_id")
    private Integer childId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "case_id")
    private Integer caseId;

    @Column(name = "referred_by")
    private Integer referredBy;

    @Column(name = "follow_up_visit")
    private Integer followUpVisit;

    @Column(name = "follow_up_date")
    private Date followUpDate;

    @Column(name = "bilateral_pitting_oedema")
    private String bilateralPittingOedema;

    @Column(name = "weight", columnDefinition = "numeric", precision = 7, scale = 3)
    private Float weight;

    @Column(name = "height")
    private Integer height;

    @Column(name = "mid_upper_arm_circumference", columnDefinition = "numeric", precision = 6, scale = 2)
    private Float midUpperArmCircumference;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "child_cmtc_nrc_follow_up_illness_detail", joinColumns = @JoinColumn(name = "follow_up_id"))
    @Column(name = "illness")
    private Set<String> illness;

    @Column(name = "other_illness")
    private String otherIllness;

    @Column(name = "sd_score")
    private String sdScore;

    @Column(name = "program_output")
    private String programOutput;

    @Column(name = "discharge_from_program")
    private Date dischargeFromProgram;

    @Column(name = "other_cmtc_center_follow_up")
    private Boolean otherCmtcCenterFollowUp;

    @Column(name = "follow_up_other_center")
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

    /**
//     * An util class for string constants of Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC)
     */
    public static class Fields {
        private Fields(){
        }
        public static final String ID = "id";
        public static final String CHILD_ID = "childId";
        public static final String ADMISSION_ID = "admissionId";
        public static final String FOLLOW_UP_VISIT = "followUpVisit";
    }
}
