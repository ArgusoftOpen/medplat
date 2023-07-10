
package com.argusoft.medplat.nutrition.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * <p>
 *     Defines fields for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) discharge
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
@Entity
@Table(name = "child_cmtc_nrc_discharge_detail")
public class ChildCmtcNrcDischarge extends EntityAuditInfo implements Serializable {

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

    @Column(name = "higher_facility_referral")
    private String higherFacilityReferral;

    @Column(name = "referral_reason")
    private String referralReason;

    @Column(name = "discharge_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dischargeDate;

    @Column(name = "bilateral_pitting_oedema")
    private String bilateralPittingOedema;

    @Column(name = "weight", columnDefinition = "numeric", precision = 7, scale = 3)
    private Float weight;

    @Column(name = "height")
    private Integer height;

    @Column(name = "mid_upper_arm_circumference", columnDefinition = "numeric", precision = 6, scale = 2)
    private Float midUpperArmCircumference;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "child_cmtc_nrc_discharge_illness_detail", joinColumns = @JoinColumn(name = "discharge_id"))
    @Column(name = "illness")
    private Set<String> illness;

    @Column(name = "other_illness")
    private String otherIllness;

    @Column(name = "sd_score")
    private String sdScore;

    @Column(name = "discharge_status")
    private String dischargeStatus;

    @Column(name = "higher_facility_referral_place")
    private Integer higherFacilityReferralPlace;

    @Column(name = "kmc_provided")
    private Boolean kmcProvided;

    @Column(name = "no_of_times_kmc_done")
    private Integer noOfTimesKmcDone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChildId() {
        return childId;
    }

    public Integer getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(Integer admissionId) {
        this.admissionId = admissionId;
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

    public String getHigherFacilityReferral() {
        return higherFacilityReferral;
    }

    public void setHigherFacilityReferral(String higherFacilityReferral) {
        this.higherFacilityReferral = higherFacilityReferral;
    }

    public String getReferralReason() {
        return referralReason;
    }

    public void setReferralReason(String referralReason) {
        this.referralReason = referralReason;
    }

    public Date getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(Date dischargeDate) {
        this.dischargeDate = dischargeDate;
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

    public String getDischargeStatus() {
        return dischargeStatus;
    }

    public void setDischargeStatus(String dischargeStatus) {
        this.dischargeStatus = dischargeStatus;
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

    public Integer getHigherFacilityReferralPlace() {
        return higherFacilityReferralPlace;
    }

    public void setHigherFacilityReferralPlace(Integer higherFacilityReferralPlace) {
        this.higherFacilityReferralPlace = higherFacilityReferralPlace;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public Boolean getKmcProvided() {
        return kmcProvided;
    }

    public void setKmcProvided(Boolean kmcProvided) {
        this.kmcProvided = kmcProvided;
    }

    public Integer getNoOfTimesKmcDone() {
        return noOfTimesKmcDone;
    }

    public void setNoOfTimesKmcDone(Integer noOfTimesKmcDone) {
        this.noOfTimesKmcDone = noOfTimesKmcDone;
    }

    /**
     * An util class defines string constant
     */
    public static class Fields {

        private Fields() {

        }

        public static final String ADMISSION_ID = "admissionId";

    }
}
