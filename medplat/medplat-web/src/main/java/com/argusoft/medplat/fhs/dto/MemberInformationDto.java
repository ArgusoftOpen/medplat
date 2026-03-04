/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fhs.dto;

import com.argusoft.medplat.rch.dto.*;

import java.util.List;

/**
 *
 * <p>
 *     Used for member information.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public class MemberInformationDto {

    private Integer memberId;
    private String familyId;
    private String memberName;
    private String gender;
    private String aadharAvailable;
    private String mobileNumber;
    private Boolean familyHeadFlag;
    private String isChild;
    private String dob;
    private String ifsc;
    private String accountNumber;
    private Boolean isPregnantFlag;
    private String memberState;
    private String motherName;
    private Float haemoglobin;
    private Float weight;
    private String childServiceVisitDatesList;
    private String ancVisitDatesList;
    private String immunisationGiven;
    private String familyState;
    private String fhwName;
    private String memberLocation;
    private String fhwUserName;
    private String fhwMobileNumber;
    private String isEligibleCouple;
    private String ashaName;
    private List<PregnancyRegistrationDetailDto> pregnancyRegistrationDetailDtos;
    private List<WpdMotherDto> wpdMotherDtos;
    private List<WpdChildDto> wpdChildDtos;
    private List<PncChildDto> pncChildDtos;
    private List<PncMotherDto> pncMotherDtos;
    private List<ChildServiceMasterDto> childServiceMasterDtos;
    private String createdOn;
    private String modifiedOn;
    private Integer createdBy;
    private Integer modifiedBy;

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
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

    public String getIsEligibleCouple() {
        return isEligibleCouple;
    }

    public void setIsEligibleCouple(String isEligibleCouple) {
        this.isEligibleCouple = isEligibleCouple;
    }

    public String getAshaName() {
        return ashaName;
    }

    public void setAshaName(String ashaName) {
        this.ashaName = ashaName;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAadharAvailable() {
        return aadharAvailable;
    }

    public void setAadharAvailable(String aadharAvailable) {
        this.aadharAvailable = aadharAvailable;
    }

    public String getImmunisationGiven() {
        return immunisationGiven;
    }

    public void setImmunisationGiven(String immunisationGiven) {
        this.immunisationGiven = immunisationGiven;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Boolean getFamilyHeadFlag() {
        return familyHeadFlag;
    }

    public void setFamilyHeadFlag(Boolean familyHeadFlag) {
        this.familyHeadFlag = familyHeadFlag;
    }

    public String getIsChild() {
        return isChild;
    }

    public void setIsChild(String isChild) {
        this.isChild = isChild;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Boolean getIsPregnantFlag() {
        return isPregnantFlag;
    }

    public void setIsPregnantFlag(Boolean isPregnantFlag) {
        this.isPregnantFlag = isPregnantFlag;
    }

    public String getMemberState() {
        return memberState;
    }

    public void setMemberState(String memberState) {
        this.memberState = memberState;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public Float getHaemoglobin() {
        return haemoglobin;
    }

    public void setHaemoglobin(Float haemoglobin) {
        this.haemoglobin = haemoglobin;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getChildServiceVisitDatesList() {
        return childServiceVisitDatesList;
    }

    public void setChildServiceVisitDatesList(String childServiceVisitDatesList) {
        this.childServiceVisitDatesList = childServiceVisitDatesList;
    }

    public String getAncVisitDatesList() {
        return ancVisitDatesList;
    }

    public void setAncVisitDatesList(String ancVisitDatesList) {
        this.ancVisitDatesList = ancVisitDatesList;
    }

    public String getFamilyState() {
        return familyState;
    }

    public void setFamilyState(String familyState) {
        this.familyState = familyState;
    }

    public String getFhwName() {
        return fhwName;
    }

    public void setFhwName(String fhwName) {
        this.fhwName = fhwName;
    }

    public String getMemberLocation() {
        return memberLocation;
    }

    public void setMemberLocation(String memberLocation) {
        this.memberLocation = memberLocation;
    }

    public String getFhwUserName() {
        return fhwUserName;
    }

    public void setFhwUserName(String fhwUserName) {
        this.fhwUserName = fhwUserName;
    }

    public String getFhwMobileNumber() {
        return fhwMobileNumber;
    }

    public void setFhwMobileNumber(String fhwMobileNumber) {
        this.fhwMobileNumber = fhwMobileNumber;
    }

    public List<PregnancyRegistrationDetailDto> getPregnancyRegistrationDetailDtos() {
        return pregnancyRegistrationDetailDtos;
    }

    public void setPregnancyRegistrationDetailDtos(List<PregnancyRegistrationDetailDto> pregnancyRegistrationDetailDtos) {
        this.pregnancyRegistrationDetailDtos = pregnancyRegistrationDetailDtos;
    }

    public List<WpdMotherDto> getWpdMotherDtos() {
        return wpdMotherDtos;
    }

    public void setWpdMotherDtos(List<WpdMotherDto> wpdMotherDtos) {
        this.wpdMotherDtos = wpdMotherDtos;
    }

    public List<WpdChildDto> getWpdChildDtos() {
        return wpdChildDtos;
    }

    public void setWpdChildDtos(List<WpdChildDto> wpdChildDtos) {
        this.wpdChildDtos = wpdChildDtos;
    }

    public List<PncChildDto> getPncChildDtos() {
        return pncChildDtos;
    }

    public void setPncChildDtos(List<PncChildDto> pncChildDtos) {
        this.pncChildDtos = pncChildDtos;
    }

    public List<PncMotherDto> getPncMotherDtos() {
        return pncMotherDtos;
    }

    public void setPncMotherDtos(List<PncMotherDto> pncMotherDtos) {
        this.pncMotherDtos = pncMotherDtos;
    }

    public List<ChildServiceMasterDto> getChildServiceMasterDtos() {
        return childServiceMasterDtos;
    }

    public void setChildServiceMasterDtos(List<ChildServiceMasterDto> childServiceMasterDtos) {
        this.childServiceMasterDtos = childServiceMasterDtos;
    }

}
