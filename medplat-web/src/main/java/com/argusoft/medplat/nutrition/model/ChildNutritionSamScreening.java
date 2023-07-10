package com.argusoft.medplat.nutrition.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
/**
 * <p>
 *     Defines fields for child nutrition sam Screening
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
@Entity
@Table(name = "child_nutrition_sam_screening_master")
public class ChildNutritionSamScreening extends EntityAuditInfo implements Serializable {

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

    @Column(name = "notification_id")
    private Integer notificationId;

    @Column(name = "service_date")
    @Temporal(TemporalType.DATE)
    private Date serviceDate;

    @Column(name = "height")
    private Integer height;

    @Column(name = "weight", columnDefinition = "numeric", precision = 7, scale = 3)
    private Float weight;

    @Column(name = "muac", columnDefinition = "numeric", precision = 6, scale = 2)
    private Float muac;

    @Column(name = "have_pedal_edema")
    private Boolean havePedalEdema;

    @Column(name = "sd_score")
    private String sdScore;

    @Column(name = "appetite_test")
    private Boolean appetiteTest;

    @Column(name = "referral_place")
    private Integer referralPlace;

    @Column(name = "referral_done")
    private Boolean referralDone;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "breast_feeding_done")
    private Boolean breastFeedingDone;

    @Column(name = "breast_sucking_problems")
    private Boolean breastSuckingProblems;

    @Column(name = "reference_id")
    private Integer referenceId;

    @Column(name = "medical_complications_present")
    private Boolean medicalComplicationsPresent;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "child_nutrition_sam_screening_diseases_rel", joinColumns = @JoinColumn(name = "sam_screening_id"))
    @Column(name = "diseases")
    private Set<Integer> diseases;

    @Column(name = "other_diseases")
    private String otherDiseases;

    @Column(name = "done_from")
    private String doneFrom;

    @Column(name = "done_by")
    private Integer doneBy;

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

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getMuac() {
        return muac;
    }

    public void setMuac(Float muac) {
        this.muac = muac;
    }

    public Boolean getHavePedalEdema() {
        return havePedalEdema;
    }

    public void setHavePedalEdema(Boolean havePedalEdema) {
        this.havePedalEdema = havePedalEdema;
    }

    public String getSdScore() {
        return sdScore;
    }

    public void setSdScore(String sdScore) {
        this.sdScore = sdScore;
    }

    public Boolean getAppetiteTest() {
        return appetiteTest;
    }

    public void setAppetiteTest(Boolean appetiteTest) {
        this.appetiteTest = appetiteTest;
    }

    public Integer getReferralPlace() {
        return referralPlace;
    }

    public void setReferralPlace(Integer referralPlace) {
        this.referralPlace = referralPlace;
    }

    public Boolean getReferralDone() {
        return referralDone;
    }

    public void setReferralDone(Boolean referralDone) {
        this.referralDone = referralDone;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }


    public Boolean getBreastFeedingDone() {
        return breastFeedingDone;
    }

    public void setBreastFeedingDone(Boolean breastFeedingDone) {
        this.breastFeedingDone = breastFeedingDone;
    }

    public Boolean getBreastSuckingProblems() {
        return breastSuckingProblems;
    }

    public void setBreastSuckingProblems(Boolean breastSuckingProblems) {
        this.breastSuckingProblems = breastSuckingProblems;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public Boolean getMedicalComplicationsPresent() {
        return medicalComplicationsPresent;
    }

    public void setMedicalComplicationsPresent(Boolean medicalComplicationsPresent) {
        this.medicalComplicationsPresent = medicalComplicationsPresent;
    }

    public Set<Integer> getDiseases() {
        return diseases;
    }

    public void setDiseases(Set<Integer> diseases) {
        this.diseases = diseases;
    }

    public String getOtherDiseases() {
        return otherDiseases;
    }

    public void setOtherDiseases(String otherDiseases) {
        this.otherDiseases = otherDiseases;
    }

    public String getDoneFrom() {
        return doneFrom;
    }

    public void setDoneFrom(String doneFrom) {
        this.doneFrom = doneFrom;
    }

    public Integer getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(Integer doneBy) {
        this.doneBy = doneBy;
    }
}
