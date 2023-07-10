/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.databean;

import java.util.Date;
import java.util.Objects;

public class OfflineHealthIdBean  {

    private String aadhar;

    private String dob;

    private String gender;

    private String mobile;

    private String name;

    private String audiFileName;

    private Long userId;

    private String familyId;

    private Integer memberId;

    private String errorMessage;

    private Integer errorCode;

    private Long createdBy;

    private Date createdOn;

    private Long modifiedBy;

    private Date modifiedOn;

    private String consentValues;

    private Boolean isAadhaarDetailMatchConsentGiven;

    public OfflineHealthIdBean() {
    }

    public OfflineHealthIdBean(String aadhar, String dob, String gender, String mobile, String name, String audiFileName,Integer memberId, String consentValues, Boolean isAadhaarDetailMatchConsentGiven) {
        this.aadhar = aadhar;
        this.dob = dob;
        this.gender = gender;
        this.mobile = mobile;
        this.name = name;
        this.audiFileName = audiFileName;
        this.memberId = memberId;
        this.consentValues = consentValues;
        this.isAadhaarDetailMatchConsentGiven = isAadhaarDetailMatchConsentGiven;
    }

    public String getAudiFileName() {
        return audiFileName;
    }

    public void setAudiFileName(String audiFileName) {
        this.audiFileName = audiFileName;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getConsentValues() {
        return consentValues;
    }

    public void setConsentValues(String consentValues) {
        this.consentValues = consentValues;
    }

    public Boolean getAadhaarDetailMatchConsentGiven() {
        return isAadhaarDetailMatchConsentGiven;
    }

    public void setAadhaarDetailMatchConsentGiven(Boolean aadhaarDetailMatchConsentGiven) {
        isAadhaarDetailMatchConsentGiven = aadhaarDetailMatchConsentGiven;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OfflineHealthIdBean that = (OfflineHealthIdBean) o;
        return Objects.equals(aadhar, that.aadhar) && Objects.equals(dob, that.dob) && Objects.equals(gender, that.gender) && Objects.equals(mobile, that.mobile) && Objects.equals(name, that.name) && Objects.equals(audiFileName, that.audiFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aadhar, dob, gender, mobile, name, audiFileName);
    }

    @Override
    public String toString() {
        return "OfflineHealthIdBean{" +
                "aadhaarNo='" + aadhar + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", mobileNumber='" + mobile + '\'' +
                ", name='" + name + '\'' +
                ", audiFileName='" + audiFileName + '\'' +
                '}';
    }

}
