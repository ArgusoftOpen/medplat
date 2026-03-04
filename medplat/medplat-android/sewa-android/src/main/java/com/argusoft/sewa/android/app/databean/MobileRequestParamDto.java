package com.argusoft.sewa.android.app.databean;

import com.argusoft.sewa.android.app.model.LmsEventBean;
import com.argusoft.sewa.android.app.model.MergedFamiliesBean;
import com.argusoft.sewa.android.app.model.UncaughtExceptionBean;

import java.util.List;

/**
 * Created by prateek on 6/9/18.
 */
public class MobileRequestParamDto {

    // Common
    private String token;
    private Long userId;

    // For Language Change
    private String languageCode;

    // For Validate User
    private String userName;
    private String password;
    private String firebaseToken;
    private Boolean isFirstTimeLogin;

    // For Upload Uncaught Exception
    private List<UncaughtExceptionBean> uncaughtExceptionBeans;

    //For Search For Family to Assign
    private String searchString;
    private Boolean isSearchByFamilyId;
    //For Assign Family
    private String locationId;
    private String familyId;

    //For Merged Family Information
    private List<MergedFamiliesBean> mergedFamiliesBeans;

    // For Move to Production &&
    private String formCode;

    //For Record Entry
    private String[] records;

    //For re-validating token
    private String[] userPassMap;

    // For Updating answerBeanTokens for user
    private List<String> userTokens;

    // For OTP Mobile Verification
    private String mobileNumber;
    private String otp;

    // For CHO role Attendance mark in HWC
    private String gpsRecords;
    private Integer attendanceId;

    // For OPD Lab Test
    private String answerString;
    private Integer labTestDetId;
    private String labTestVersion;

    //For Lms Mobile Event
    private List<LmsEventBean> mobileEvents;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

    public Boolean getFirstTimeLogin() {
        return isFirstTimeLogin;
    }

    public void setFirstTimeLogin(Boolean firstTimeLogin) {
        isFirstTimeLogin = firstTimeLogin;
    }

    public List<UncaughtExceptionBean> getUncaughtExceptionBeans() {
        return uncaughtExceptionBeans;
    }

    public void setUncaughtExceptionBeans(List<UncaughtExceptionBean> uncaughtExceptionBeans) {
        this.uncaughtExceptionBeans = uncaughtExceptionBeans;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public Boolean getSearchByFamilyId() {
        return isSearchByFamilyId;
    }

    public void setSearchByFamilyId(Boolean searchByFamilyId) {
        isSearchByFamilyId = searchByFamilyId;
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

    public List<LmsEventBean> getMobileEvents() {
        return mobileEvents;
    }

    public void setMobileEvents(List<LmsEventBean> mobileEvents) {
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
}
