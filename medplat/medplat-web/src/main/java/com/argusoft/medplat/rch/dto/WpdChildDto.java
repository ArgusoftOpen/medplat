/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Used for wpd child.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
public class WpdChildDto {

    private Integer id;
    private Integer memberId;
    private Integer familyId;
    private String latitude;
    private String longitude;
    private Date startDate;
    private Date endDate;
    private Integer locationId;
    private Integer wpdMotherId;
    private Integer motherId;
    private String pregnancyOutcome;
    private String childGender;
    private Float childBirthWeight;
    private Date deliveryDate;
    private Boolean childCry;
    private String memberStatus;
    private Boolean breastFeeding;
    private Boolean kangarooCare;
    private Boolean breastCrawl;
    private String congenitalDeformityOther;
    private String childOtherDangerSign;
    private String childReferralReason;
    private String childReferralTransport;
    private Integer childReferralPlace;
    private Set<Integer> congenitalDeformityPresent;
    private Set<Integer> childDangerSigns;
    private List<ImmunisationDto> immunisationDetails;
    private Boolean wasPremature;
    private String name;
    private String typeOfDelivery;
    private Date deathDate;
    private String deathReason;
    private Boolean isHighRiskCase;
    private String placeOfDeath;
    private Date createdOn;
    private Date modifiedOn;
    private Integer createdBy;
    private Integer modifiedBy;
    private Integer notificationId;
    private Integer childReferralInfraId;
    private String givenImmunisations;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getWpdMotherId() {
        return wpdMotherId;
    }

    public void setWpdMotherId(Integer wpdMotherId) {
        this.wpdMotherId = wpdMotherId;
    }

    public Integer getMotherId() {
        return motherId;
    }

    public void setMotherId(Integer motherId) {
        this.motherId = motherId;
    }

    public String getPregnancyOutcome() {
        return pregnancyOutcome;
    }

    public void setPregnancyOutcome(String pregnancyOutcome) {
        this.pregnancyOutcome = pregnancyOutcome;
    }

    public String getChildGender() {
        return childGender;
    }

    public void setChildGender(String childGender) {
        this.childGender = childGender;
    }

    public Float getChildBirthWeight() {
        return childBirthWeight;
    }

    public void setChildBirthWeight(Float childBirthWeight) {
        this.childBirthWeight = childBirthWeight;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Boolean getChildCry() {
        return childCry;
    }

    public void setChildCry(Boolean childCry) {
        this.childCry = childCry;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public Boolean getBreastFeeding() {
        return breastFeeding;
    }

    public void setBreastFeeding(Boolean breastFeeding) {
        this.breastFeeding = breastFeeding;
    }

    public Boolean getKangarooCare() {
        return kangarooCare;
    }

    public void setKangarooCare(Boolean kangarooCare) {
        this.kangarooCare = kangarooCare;
    }

    public Boolean getBreastCrawl() {
        return breastCrawl;
    }

    public void setBreastCrawl(Boolean breastCrawl) {
        this.breastCrawl = breastCrawl;
    }

    public String getCongenitalDeformityOther() {
        return congenitalDeformityOther;
    }

    public void setCongenitalDeformityOther(String congenitalDeformityOther) {
        this.congenitalDeformityOther = congenitalDeformityOther;
    }

    public String getChildReferralReason() {
        return childReferralReason;
    }

    public void setChildReferralReason(String childReferralReason) {
        this.childReferralReason = childReferralReason;
    }

    public String getChildReferralTransport() {
        return childReferralTransport;
    }

    public void setChildReferralTransport(String childReferralTransport) {
        this.childReferralTransport = childReferralTransport;
    }

    public Integer getChildReferralPlace() {
        return childReferralPlace;
    }

    public void setChildReferralPlace(Integer childReferralPlace) {
        this.childReferralPlace = childReferralPlace;
    }

    public Set<Integer> getCongenitalDeformityPresent() {
        return congenitalDeformityPresent;
    }

    public void setCongenitalDeformityPresent(Set<Integer> congenitalDeformityPresent) {
        this.congenitalDeformityPresent = congenitalDeformityPresent;
    }

    public Set<Integer> getChildDangerSigns() {
        return childDangerSigns;
    }

    public void setChildDangerSigns(Set<Integer> childDangerSigns) {
        this.childDangerSigns = childDangerSigns;
    }

    public List<ImmunisationDto> getImmunisationDetails() {
        return immunisationDetails;
    }

    public void setImmunisationDetails(List<ImmunisationDto> immunisationDetails) {
        this.immunisationDetails = immunisationDetails;
    }

    public Boolean getWasPremature() {
        return wasPremature;
    }

    public void setWasPremature(Boolean wasPremature) {
        this.wasPremature = wasPremature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChildOtherDangerSign() {
        return childOtherDangerSign;
    }

    public void setChildOtherDangerSign(String childOtherDangerSign) {
        this.childOtherDangerSign = childOtherDangerSign;
    }

    public String getTypeOfDelivery() {
        return typeOfDelivery;
    }

    public void setTypeOfDelivery(String typeOfDelivery) {
        this.typeOfDelivery = typeOfDelivery;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public String getDeathReason() {
        return deathReason;
    }

    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }

    public Boolean getIsHighRiskCase() {
        return isHighRiskCase;
    }

    public void setIsHighRiskCase(Boolean isHighRiskCase) {
        this.isHighRiskCase = isHighRiskCase;
    }

    public String getPlaceOfDeath() {
        return placeOfDeath;
    }

    public void setPlaceOfDeath(String placeOfDeath) {
        this.placeOfDeath = placeOfDeath;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public Integer getChildReferralInfraId() {
        return childReferralInfraId;
    }

    public void setChildReferralInfraId(Integer childReferralInfraId) {
        this.childReferralInfraId = childReferralInfraId;
    }

    public String getGivenImmunisations() {
        return givenImmunisations;
    }

    public void setGivenImmunisations(String givenImmunisations) {
        this.givenImmunisations = givenImmunisations;
    }

    @Override
    public String toString() {
        return "WpdChildDto{" + "id=" + id + "memberId=" + memberId + ", familyId=" + familyId + ", latitude=" + latitude + ", longitude=" + longitude + ", startDate=" + startDate + ", endDate=" + endDate + ", locationId=" + locationId + ", wpdMotherId=" + wpdMotherId + ", motherId=" + motherId + ", pregnancyOutcome=" + pregnancyOutcome + ", childGender=" + childGender + ", childBirthWeight=" + childBirthWeight + ", deliveryDate=" + deliveryDate + ", childCry=" + childCry + ", memberStatus=" + memberStatus + ", breastFeeding=" + breastFeeding + ", kangarooCare=" + kangarooCare + ", breastCrawl=" + breastCrawl + ", congenitalDeformityOther=" + congenitalDeformityOther + ", childOtherDangerSign=" + childOtherDangerSign + ", childReferralReason=" + childReferralReason + ", childReferralTransport=" + childReferralTransport + ", childReferralPlace=" + childReferralPlace + ", congenitalDeformityPresent=" + congenitalDeformityPresent + ", childDangerSigns=" + childDangerSigns + ", immunisationDetails=" + immunisationDetails + ", wasPremature=" + wasPremature + ", name=" + name + ", typeOfDelivery=" + typeOfDelivery + ", deathDate=" + deathDate + ", deathReason=" + deathReason + ", isHighRiskCase=" + isHighRiskCase + ", placeOfDeath=" + placeOfDeath + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", notificationId=" + notificationId + ", childReferralInfraId=" + childReferralInfraId + ", givenImmunisations=" + givenImmunisations + '}';
    }
}
