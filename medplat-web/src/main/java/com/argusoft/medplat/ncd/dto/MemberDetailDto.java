/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dto;

import com.argusoft.medplat.fhs.dto.MemberDto;
import com.argusoft.medplat.ncd.model.MemberReferral;

import java.sql.Date;

/**
 *
 * <p>
 *     Used for member details.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class MemberDetailDto {

    private Integer id;
    private String famId;
    private String uniqueHealthId;
    private String referredForHypertension;
    private String referredForDiabetes;
    private String referredForBreast;
    private String referredForOral;
    private String referredForCervical;
    private String name;
    private String gender;
    private java.sql.Date dob;
    private String status;
    private Integer familyId;
    private String subCenter;
    private Integer locationId;
    private String locationHierarchy;
    private String locationName;
    private String followUpDate;
    private MemberReferral memberHypertensionDto;
    private MemberReferral memberDiabetesDto;
    private MemberReferral memberOralDto;
    private MemberReferral memberBreastDto;
    private MemberReferral memberCervicalDto;
    private MemberReferral memberGeneralDto;
    private MemberReferral memberInitialAssessmentDto;
    private MemberReferral memberMentalHealth;
    private MemberDto basicDetails;
    private String mobileNumber;
    private Integer memberId;
    private String diseases;
    private String subStatus;
    private String village;
    private Integer age;

    public String getFamId() {
        return famId;
    }

    public void setFamId(String famId) {
        this.famId = famId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocationHierarchy() {
        return locationHierarchy;
    }

    public void setLocationHierarchy(String locationHierarchy) {
        this.locationHierarchy = locationHierarchy;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getSubCenter() {
        return subCenter;
    }

    public void setSubCenter(String subCenter) {
        this.subCenter = subCenter;
    }

    public MemberReferral getMemberGeneralDto() {
        return memberGeneralDto;
    }

    public void setMemberGeneralDto(MemberReferral memberGeneralDto) {
        this.memberGeneralDto = memberGeneralDto;
    }

    public MemberReferral getMemberInitialAssessmentDto() {
        return memberInitialAssessmentDto;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMemberInitialAssessmentDto(MemberReferral memberInitialAssessmentDto) {
        this.memberInitialAssessmentDto = memberInitialAssessmentDto;
    }

    public MemberReferral getMemberMentalHealth() {
        return memberMentalHealth;
    }

    public void setMemberMentalHealth(MemberReferral memberMentalHealth) {
        this.memberMentalHealth = memberMentalHealth;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public MemberReferral getMemberHypertensionDto() {
        return memberHypertensionDto;
    }

    public void setMemberHypertensionDto(MemberReferral memberHypertensionDto) {
        this.memberHypertensionDto = memberHypertensionDto;
    }

    public MemberReferral getMemberDiabetesDto() {
        return memberDiabetesDto;
    }

    public void setMemberDiabetesDto(MemberReferral memberDiabetesDto) {
        this.memberDiabetesDto = memberDiabetesDto;
    }

    public MemberReferral getMemberOralDto() {
        return memberOralDto;
    }

    public void setMemberOralDto(MemberReferral memberOralDto) {
        this.memberOralDto = memberOralDto;
    }

    public MemberReferral getMemberBreastDto() {
        return memberBreastDto;
    }

    public void setMemberBreastDto(MemberReferral memberBreastDto) {
        this.memberBreastDto = memberBreastDto;
    }

    public MemberReferral getMemberCervicalDto() {
        return memberCervicalDto;
    }

    public void setMemberCervicalDto(MemberReferral memberCervicalDto) {
        this.memberCervicalDto = memberCervicalDto;
    }

    public MemberDto getBasicDetails() {
        return basicDetails;
    }

    public void setBasicDetails(MemberDto basicDetails) {
        this.basicDetails = basicDetails;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUniqueHealthId() {
        return uniqueHealthId;
    }

    public void setUniqueHealthId(String uniqueHealthId) {
        this.uniqueHealthId = uniqueHealthId;
    }

    public String getReferredForHypertension() {
        return referredForHypertension;
    }

    public void setReferredForHypertension(String referredForHypertension) {
        this.referredForHypertension = referredForHypertension;
    }

    public String getReferredForDiabetes() {
        return referredForDiabetes;
    }

    public void setReferredForDiabetes(String referredForDiabetes) {
        this.referredForDiabetes = referredForDiabetes;
    }

    public String getReferredForBreast() {
        return referredForBreast;
    }

    public void setReferredForBreast(String referredForBreast) {
        this.referredForBreast = referredForBreast;
    }

    public String getReferredForOral() {
        return referredForOral;
    }

    public void setReferredForOral(String referredForOral) {
        this.referredForOral = referredForOral;
    }

    public String getReferredForCervical() {
        return referredForCervical;
    }

    public void setReferredForCervical(String referredForCervical) {
        this.referredForCervical = referredForCervical;
    }

    public String getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(String followUpDate) {
        this.followUpDate = followUpDate;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getDiseases() {
        return diseases;
    }

    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "MemberDetailDto{" +
                "id=" + id +
                ", uniqueHealthId='" + uniqueHealthId + '\'' +
                ", referredForHypertension='" + referredForHypertension + '\'' +
                ", referredForDiabetes='" + referredForDiabetes + '\'' +
                ", referredForBreast='" + referredForBreast + '\'' +
                ", referredForOral='" + referredForOral + '\'' +
                ", referredForCervical='" + referredForCervical + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", dob=" + dob +
                ", status='" + status + '\'' +
                ", familyId=" + familyId +
                ", subCenter='" + subCenter + '\'' +
                ", locationId=" + locationId +
                ", locationHierarchy='" + locationHierarchy + '\'' +
                ", locationName='" + locationName + '\'' +
                ", followUpDate='" + followUpDate + '\'' +
                ", memberHypertensionDto=" + memberHypertensionDto +
                ", memberDiabetesDto=" + memberDiabetesDto +
                ", memberOralDto=" + memberOralDto +
                ", memberBreastDto=" + memberBreastDto +
                ", memberCervicalDto=" + memberCervicalDto +
                ", memberGeneralDto=" + memberGeneralDto +
                ", memberInitialAssessmentDto=" + memberInitialAssessmentDto +
                ", memberMentalHealth=" + memberMentalHealth +
                ", basicDetails=" + basicDetails +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", memberId=" + memberId +
                ", diseases='" + diseases + '\'' +
                ", subStatus='" + subStatus + '\'' +
                ", village='" + village + '\'' +
                ", age=" + age +
                '}';
    }
}
