package com.argusoft.medplat.nutrition.dto;

import java.util.Date;
import java.util.Set;

public class ChildNutritionSamScreeningMasterDto {
    private Integer id;
    private Integer memberId;
    private Integer familyId;
    private Integer locationId;
    private Integer notificationId;
    private Integer height;
    private Float weight;
    private Float muac;
    private String sdScore;
    private Boolean havePedalEdema;
    private Boolean appetiteTest;
    private Boolean referralDone;
    private Integer roleId;
    private Date serviceDate;
    private Boolean breastSuckingProblems;
    private Integer referralPlace;
    private Boolean medicalComplicationsPresent;
    private String childState;
    private Integer doneBy;
    private String doneFrom;
    private Boolean initTreatmentStarted;
    private Set<Integer> illness;

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

    public String getSdScore() {
        return sdScore;
    }

    public void setSdScore(String sdScore) {
        this.sdScore = sdScore;
    }

    public Boolean getHavePedalEdema() {
        return havePedalEdema;
    }

    public void setHavePedalEdema(Boolean havePedalEdema) {
        this.havePedalEdema = havePedalEdema;
    }

    public Boolean getAppetiteTest() {
        return appetiteTest;
    }

    public void setAppetiteTest(Boolean appetiteTest) {
        this.appetiteTest = appetiteTest;
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

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public Boolean getBreastSuckingProblems() {
        return breastSuckingProblems;
    }

    public void setBreastSuckingProblems(Boolean breastSuckingProblems) {
        this.breastSuckingProblems = breastSuckingProblems;
    }

    public Integer getReferralPlace() {
        return referralPlace;
    }

    public void setReferralPlace(Integer referralPlace) {
        this.referralPlace = referralPlace;
    }

    public Boolean getMedicalComplicationsPresent() {
        return medicalComplicationsPresent;
    }

    public void setMedicalComplicationsPresent(Boolean medicalComplicationsPresent) {
        this.medicalComplicationsPresent = medicalComplicationsPresent;
    }

    public String getChildState() {
        return childState;
    }

    public void setChildState(String childState) {
        this.childState = childState;
    }

    public Integer getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(Integer doneBy) {
        this.doneBy = doneBy;
    }

    public String getDoneFrom() {
        return doneFrom;
    }

    public void setDoneFrom(String doneFrom) {
        this.doneFrom = doneFrom;
    }

    public Boolean getInitTreatmentStarted() {
        return initTreatmentStarted;
    }

    public void setInitTreatmentStarted(Boolean initTreatmentStarted) {
        this.initTreatmentStarted = initTreatmentStarted;
    }

    public Set<Integer> getIllness() {
        return illness;
    }

    public void setIllness(Set<Integer> illness) {
        this.illness = illness;
    }
}
