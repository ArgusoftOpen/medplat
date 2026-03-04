/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.dto;

import com.argusoft.medplat.course.dto.LmsMobileEventDto;

import java.util.Arrays;
import java.util.List;

/**
 * @author prateek
 */
public class MobileRequestParamDto {

    private String token;
    private Integer userId;
    // For language change
    private String languageCode;
    // For Validate User
    private String userName;
    private String password;
    private Boolean isFirstTimeLogin;
    // For Upload Uncaught Excption
    private List<UncaughtExceptionBean> uncaughtExceptionBeans;
    //For Aadhar Update Details
    private List<AadharUpdationBean> aadharUpdationBeans;
    //For Search For Family to Assign
    private String searchString;
    private Boolean isSearchByFamilyId;
    //For Assign Family
    private String locationId;
    private String familyId;
    //For Merged Family Information
    private List<MergedFamiliesBean> mergedFamiliesBeans;
    //For Move to Production
    private String formCode;
    //For Record Entry
    private String[] records;
    private String[] userPassMap;
    List<String> userTokens;
    private String mobileNumber;
    private String otp;
    //For User's Attendance Entry
    private String gpsRecords;
    private Integer attendanceId;

    //For OPD Lab Test forms
    private String answerString;
    private Integer labTestDetId;
    private String labTestVersion;
    private String firebaseToken;
//    For Lms Mobile Event
    private List<LmsMobileEventDto> mobileEvents;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsFirstTimeLogin() {
        return isFirstTimeLogin;
    }

    public void setIsFirstTimeLogin(Boolean isFirstTimeLogin) {
        this.isFirstTimeLogin = isFirstTimeLogin;
    }

    public List<UncaughtExceptionBean> getUncaughtExceptionBeans() {
        return uncaughtExceptionBeans;
    }

    public void setUncaughtExceptionBeans(List<UncaughtExceptionBean> uncaughtExceptionBeans) {
        this.uncaughtExceptionBeans = uncaughtExceptionBeans;
    }

    public List<AadharUpdationBean> getAadharUpdationBeans() {
        return aadharUpdationBeans;
    }

    public void setAadharUpdationBeans(List<AadharUpdationBean> aadharUpdationBeans) {
        this.aadharUpdationBeans = aadharUpdationBeans;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public Boolean getIsSearchByFamilyId() {
        return isSearchByFamilyId;
    }

    public void setIsSearchByFamilyId(Boolean isSearchByFamilyId) {
        this.isSearchByFamilyId = isSearchByFamilyId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public List<MergedFamiliesBean> getMergedFamiliesBeans() {
        return mergedFamiliesBeans;
    }

    public void setMergedFamiliesBeans(List<MergedFamiliesBean> mergedFamiliesBeans) {
        this.mergedFamiliesBeans = mergedFamiliesBeans;
    }

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public String[] getRecords() {
        return records;
    }

    public void setRecords(String[] records) {
        this.records = records;
    }

    public String[] getUserPassMap() {
        return userPassMap;
    }

    public void setUserPassMap(String[] userPassMap) {
        this.userPassMap = userPassMap;
    }

    public List<String> getUserTokens() {
        return userTokens;
    }

    public void setUserTokens(List<String> userTokens) {
        this.userTokens = userTokens;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getGpsRecords() {
        return gpsRecords;
    }

    public void setGpsRecords(String gpsRecords) {
        this.gpsRecords = gpsRecords;
    }

    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getAnswerString() {
        return answerString;
    }

    public void setAnswerString(String answerString) {
        this.answerString = answerString;
    }

    public Integer getLabTestDetId() {
        return labTestDetId;
    }

    public void setLabTestDetId(Integer labTestDetId) {
        this.labTestDetId = labTestDetId;
    }

    public String getLabTestVersion() {
        return labTestVersion;
    }

    public void setLabTestVersion(String labTestVersion) {
        this.labTestVersion = labTestVersion;
    }

    public List<LmsMobileEventDto> getMobileEvents() {
        return mobileEvents;
    }

    public void setMobileEvents(List<LmsMobileEventDto> mobileEvents) {
        this.mobileEvents = mobileEvents;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    @Override
    public String toString() {
        return "MobileRequestParamDto{" + "token=" + token + ", userId=" + userId + ", userName=" + userName + ", password=" + password + ", isFirstTimeLogin=" + isFirstTimeLogin + ", aadharUpdationBeans=" + aadharUpdationBeans + ", searchString=" + searchString + ", isSearchByFamilyId=" + isSearchByFamilyId + ", locationId=" + locationId + ", familyId=" + familyId + ", mergedFamiliesBeans=" + mergedFamiliesBeans + ", formCode=" + formCode + ", records=" + Arrays.toString(records) + ", userPassMap=" + Arrays.toString(userPassMap) + ", userTokens=" + userTokens + '}';
    }
}
