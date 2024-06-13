/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.dto;

import com.argusoft.medplat.fhs.dto.MemberDto;
import com.argusoft.medplat.ncddnhdd.model.MemberReferral;

import java.util.Date;

/**
 * @author vaishali
 */
public class MemberNcdDetailDto {

    private Integer id;
    private String uniqueHealthId;
    private String referredForHypertension;
    private String referredForDiabetes;
    private String referredForBreast;
    private String referredForOral;
    private String referredForCervical;
    private String name;
    private Integer familyId;
    private Integer locationId;
    private String locationHierarchy;
    private String locationName;
    private String followUpDate;
    private MemberReferral memberHypertensionDto;
    private MemberReferral memberDiabetesDto;
    private MemberReferral memberOralDto;
    private MemberReferral memberBreastDto;
    private MemberReferral memberCervicalDto;
    private MemberDto basicDetails;
    private String mobileNumber;
    private String additionalInfo;
    private Boolean caseOfHypertension;
    private Boolean caseOfDiabetes;
    private Boolean caseOfBreastCancer;
    private Boolean caseOfOralCancer;
    private Boolean caseOfCervicalCancer;
    private Date serverDate;

    public Date getServerDate() {
        return serverDate;
    }

    public void setServerDate(Date serverDate) {
        this.serverDate = serverDate;
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

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Boolean getCaseOfHypertension() {
        return caseOfHypertension;
    }

    public void setCaseOfHypertension(Boolean caseOfHypertension) {
        this.caseOfHypertension = caseOfHypertension;
    }

    public Boolean getCaseOfDiabetes() {
        return caseOfDiabetes;
    }

    public void setCaseOfDiabetes(Boolean caseOfDiabetes) {
        this.caseOfDiabetes = caseOfDiabetes;
    }

    public Boolean getCaseOfBreastCancer() {
        return caseOfBreastCancer;
    }

    public void setCaseOfBreastCancer(Boolean caseOfBreastCancer) {
        this.caseOfBreastCancer = caseOfBreastCancer;
    }

    public Boolean getCaseOfOralCancer() {
        return caseOfOralCancer;
    }

    public void setCaseOfOralCancer(Boolean caseOfOralCancer) {
        this.caseOfOralCancer = caseOfOralCancer;
    }

    public Boolean getCaseOfCervicalCancer() {
        return caseOfCervicalCancer;
    }

    public void setCaseOfCervicalCancer(Boolean caseOfCervicalCancer) {
        this.caseOfCervicalCancer = caseOfCervicalCancer;
    }
}
