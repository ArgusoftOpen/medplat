/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.users.dto;

import com.argusoft.medplat.web.users.model.UserMaster;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>Defines fields related to user</p>
 *
 * @author vaishali
 * @since 26/08/2020 5:30
 */
@JsonInclude(Include.NON_NULL)
public class UserMasterDto {

    private Integer id;
    private String fullName;
    private String userName;
    private String emailId;
    private String contactNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private String address;
    private String prefferedLanguage;
    private UserMaster.State state;
    private String code;
    private String aadharNumber;
    private Integer roleId;
    private Date dateOfBirth;
    private Integer createdBy;
    private Date createdOn;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String roleName;
    private String roleCode;
    private Integer minLocationId;
    private Integer minLocationLevel;
    private String minLocationName;
    private List<UserLocationDto> addedLocations;
    private List<UserLocationDto> deletedLocations;
    private List<String> locationName;
    private String displayState;
    private Integer maxPositions;
    private String title;
    private String areaOfIntervention;
    private String imeiNumber;
    private String techoPhoneNumber;
    private Integer noOfAttempts;
    private Integer rchInstitutionId;
    private Integer infrastructureId;
    private List<Integer> infrastructureIds;
    private String reportPreferredLanguage;
    private String loginCode;
    private String convoxId;
    private String mobileNumber;
    private String uniqueHealthId;
    private Integer memberId;
    private Integer locationId;
    private String latitude;
    private String longitude;
    private Integer pinCode;
    private String registrationNumber;
    private String medicalCouncil;
    private Integer healthFacilityId;
    private String healthFacilityName;
    private String healthFacilityPincode;
    private String healthFacilityRegNo;
    private String healthFacilityAddress;
    private List<UserHealthInfrastructureDto> healthInfrastructureDetails;
    private String pin;
    private String documentTypes;
    private String documentDetails;
    private String remarks;
    private Date actionOn;
    private String actionBy;
    private String otp;
    private Integer defaultHealthInfrastructure;
    private Date userLastLogin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrefferedLanguage() {
        return prefferedLanguage;
    }

    public void setPrefferedLanguage(String prefferedLanguage) {
        this.prefferedLanguage = prefferedLanguage;
    }

    public UserMaster.State getState() {
        return state;
    }

    public void setState(UserMaster.State state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<UserLocationDto> getAddedLocations() {
        return addedLocations;
    }

    public void setAddedLocations(List<UserLocationDto> addedLocations) {
        this.addedLocations = addedLocations;
    }

    public List<UserLocationDto> getDeletedLocations() {
        return deletedLocations;
    }

    public void setDeletedLocations(List<UserLocationDto> deletedLocations) {
        this.deletedLocations = deletedLocations;
    }

    public Integer getMinLocationId() {
        return minLocationId;
    }

    public void setMinLocationId(Integer minLocationId) {
        this.minLocationId = minLocationId;
    }

    public String getMinLocationName() {
        return minLocationName;
    }

    public void setMinLocationName(String minLocationName) {
        this.minLocationName = minLocationName;
    }

    public String getDisplayState() {
        return displayState;
    }

    public void setDisplayState(String displayState) {
        this.displayState = displayState;
    }

    public Integer getMaxPositions() {
        return maxPositions;
    }

    public void setMaxPositions(Integer maxPositions) {
        this.maxPositions = maxPositions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getLocationName() {
        return locationName;
    }

    public void setLocationName(List<String> locationName) {
        this.locationName = locationName;
    }

    public String getAreaOfIntervention() {
        return areaOfIntervention;
    }

    public void setAreaOfIntervention(String areaOfIntervention) {
        this.areaOfIntervention = areaOfIntervention;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public String getTechoPhoneNumber() {
        return techoPhoneNumber;
    }

    public void setTechoPhoneNumber(String techoPhoneNumber) {
        this.techoPhoneNumber = techoPhoneNumber;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Integer getNoOfAttempts() {
        return noOfAttempts;
    }

    public void setNoOfAttempts(Integer noOfAttempts) {
        this.noOfAttempts = noOfAttempts;
    }

    public Integer getRchInstitutionId() {
        return rchInstitutionId;
    }

    public void setRchInstitutionId(Integer rchInstitutionId) {
        this.rchInstitutionId = rchInstitutionId;
    }

    public Integer getInfrastructureId() {
        return infrastructureId;
    }

    public void setInfrastructureId(Integer infrastructureId) {
        this.infrastructureId = infrastructureId;
    }

    public List<Integer> getInfrastructureIds() {
        return infrastructureIds;
    }

    public void setInfrastructureIds(List<Integer> infrastructureIds) {
        this.infrastructureIds = infrastructureIds;
    }

    public String getReportPreferredLanguage() {
        return reportPreferredLanguage;
    }

    public void setReportPreferredLanguage(String reportPreferredLanguage) {
        this.reportPreferredLanguage = reportPreferredLanguage;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public String getLoginCode() {
        return loginCode;
    }

    public String getConvoxId() {
        return convoxId;
    }

    public void setConvoxId(String convoxId) {
        this.convoxId = convoxId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUniqueHealthId() {
        return uniqueHealthId;
    }

    public void setUniqueHealthId(String uniqueHealthId) {
        this.uniqueHealthId = uniqueHealthId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
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

    public Integer getPinCode() {
        return pinCode;
    }

    public void setPinCode(Integer pinCode) {
        this.pinCode = pinCode;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getMedicalCouncil() {
        return medicalCouncil;
    }

    public void setMedicalCouncil(String medicalCouncil) {
        this.medicalCouncil = medicalCouncil;
    }

    public Integer getHealthFacilityId() {
        return healthFacilityId;
    }

    public void setHealthFacilityId(Integer healthFacilityId) {
        this.healthFacilityId = healthFacilityId;
    }

    public String getHealthFacilityName() {
        return healthFacilityName;
    }

    public void setHealthFacilityName(String healthFacilityName) {
        this.healthFacilityName = healthFacilityName;
    }

    public String getHealthFacilityPincode() {
        return healthFacilityPincode;
    }

    public void setHealthFacilityPincode(String healthFacilityPincode) {
        this.healthFacilityPincode = healthFacilityPincode;
    }

    public String getHealthFacilityRegNo() {
        return healthFacilityRegNo;
    }

    public void setHealthFacilityRegNo(String healthFacilityRegNo) {
        this.healthFacilityRegNo = healthFacilityRegNo;
    }

    public String getHealthFacilityAddress() {
        return healthFacilityAddress;
    }

    public void setHealthFacilityAddress(String healthFacilityAddress) {
        this.healthFacilityAddress = healthFacilityAddress;
    }

    public List<UserHealthInfrastructureDto> getHealthInfrastructureDetails() {
        return healthInfrastructureDetails;
    }

    public void setHealthInfrastructureDetails(List<UserHealthInfrastructureDto> healthInfrastructureDetails) {
        this.healthInfrastructureDetails = healthInfrastructureDetails;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(String documentTypes) {
        this.documentTypes = documentTypes;
    }

    public String getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(String documentDetails) {
        this.documentDetails = documentDetails;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getActionOn() {
        return actionOn;
    }

    public void setActionOn(Date actionOn) {
        this.actionOn = actionOn;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Integer getMinLocationLevel() {
        return minLocationLevel;
    }

    public void setMinLocationLevel(Integer minLocationLevel) {
        this.minLocationLevel = minLocationLevel;
    }

    public Integer getDefaultHealthInfrastructure() {
        return defaultHealthInfrastructure;
    }

    public void setDefaultHealthInfrastructure(Integer defaultHealthInfrastructure) {
        this.defaultHealthInfrastructure = defaultHealthInfrastructure;
    }

    public Date getUserLastLogin() {
        return userLastLogin;
    }

    public void setUserLastLogin(Date userLastLogin) {
        this.userLastLogin = userLastLogin;
    }

    @Override
    public String toString() {
        return "UserMasterDto{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", userName='" + userName + '\'' +
                ", emailId='" + emailId + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", prefferedLanguage='" + prefferedLanguage + '\'' +
                ", state=" + state +
                ", code='" + code + '\'' +
                ", aadharNumber='" + aadharNumber + '\'' +
                ", roleId=" + roleId +
                ", dateOfBirth=" + dateOfBirth +
                ", createdBy=" + createdBy +
                ", createdOn=" + createdOn +
                ", password='" + password + '\'' +
                ", roleName='" + roleName + '\'' +
                ", roleCode='" + roleCode + '\'' +
                ", minLocationId=" + minLocationId +
                ", minLocationLevel=" + minLocationLevel +
                ", minLocationName='" + minLocationName + '\'' +
                ", addedLocations=" + addedLocations +
                ", deletedLocations=" + deletedLocations +
                ", locationName=" + locationName +
                ", displayState='" + displayState + '\'' +
                ", maxPositions=" + maxPositions +
                ", title='" + title + '\'' +
                ", areaOfIntervention='" + areaOfIntervention + '\'' +
                ", imeiNumber='" + imeiNumber + '\'' +
                ", techoPhoneNumber='" + techoPhoneNumber + '\'' +
                ", noOfAttempts=" + noOfAttempts +
                ", rchInstitutionId=" + rchInstitutionId +
                ", infrastructureId=" + infrastructureId +
                ", infrastructureIds=" + infrastructureIds +
                ", reportPreferredLanguage='" + reportPreferredLanguage + '\'' +
                ", loginCode='" + loginCode + '\'' +
                ", convoxId='" + convoxId + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", uniqueHealthId='" + uniqueHealthId + '\'' +
                ", memberId=" + memberId +
                ", locationId=" + locationId +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", pinCode=" + pinCode +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", medicalCouncil='" + medicalCouncil + '\'' +
                ", healthFacilityId=" + healthFacilityId +
                ", healthFacilityName='" + healthFacilityName + '\'' +
                ", healthFacilityPincode='" + healthFacilityPincode + '\'' +
                ", healthFacilityRegNo='" + healthFacilityRegNo + '\'' +
                ", healthFacilityAddress='" + healthFacilityAddress + '\'' +
                ", healthInfrastructureDetails=" + healthInfrastructureDetails +
                ", pin='" + pin + '\'' +
                ", documentTypes='" + documentTypes + '\'' +
                ", documentDetails='" + documentDetails + '\'' +
                ", remarks='" + remarks + '\'' +
                ", actionOn=" + actionOn +
                ", actionBy='" + actionBy + '\'' +
                ", otp='" + otp + '\'' +
                ", defaultHealthInfrastructure=" + defaultHealthInfrastructure +
                '}';
    }

}
