/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.dto;

import java.util.HashMap;

/**
 *
 * @author kunjan
 */
public class MobUserInfoDataBean {

    private boolean authenticated;
    private String contactNumber;
    private Long dob;
    private boolean firstTimeLogin;
    private String languageCode;
    private String loginFailureMsg;
    private String password;
    private Integer userContactId;
    private String userRole;
    private String username;
    private String fName;
    private String phNoOfPM;
    private Integer id;
    private String userToken;
    private Long serverDate;
    private int[] currentVillageCode;
    private String[] currentAssignedVillagesName;
    private HashMap<Integer, String> phcNames;
    private HashMap<Integer, String> subcenterNames;
    private HashMap<Integer, String> villageNames;
    private boolean isNagarPalikaUser;
    private boolean firstTimePasswordChanged;
    private String firstName;
    private String middleName;
    private String lastName;

    public MobUserInfoDataBean(boolean authenticated, String contactNumber, Long dob, boolean firstTimeLogin, String languageCode, String loginFailureMsg, String password, Integer userContactId, String userRole, String username, String fName, String phNoOfPM, Integer id, String userToken, boolean isNagarPalikaUser) {
        this.authenticated = authenticated;
        this.contactNumber = contactNumber;
        this.dob = dob;
        this.firstTimeLogin = firstTimeLogin;
        this.languageCode = languageCode;
        this.loginFailureMsg = loginFailureMsg;
        this.password = password;
        this.userContactId = userContactId;
        this.userRole = userRole;
        this.username = username;
        this.fName = fName;
        this.phNoOfPM = phNoOfPM;
        this.id = id;
        this.userToken = userToken;
        this.isNagarPalikaUser = isNagarPalikaUser;
    }

    public MobUserInfoDataBean() {
    }

    public HashMap<Integer, String> getPhcNames() {
        return phcNames;
    }

    public void setPhcNames(HashMap<Integer, String> phcNames) {
        this.phcNames = phcNames;
    }

    public HashMap<Integer, String> getSubcenterNames() {
        return subcenterNames;
    }

    public void setSubcenterNames(HashMap<Integer, String> subcenterNames) {
        this.subcenterNames = subcenterNames;
    }

    public HashMap<Integer, String> getVillageNames() {
        return villageNames;
    }

    public void setVillageNames(HashMap<Integer, String> villageNames) {
        this.villageNames = villageNames;
    }

    public Long getServerDate() {
        return serverDate;
    }

    public void setServerDate(Long serverDate) {
        this.serverDate = serverDate;
    }

    public int[] getCurrentVillageCode() {
        return currentVillageCode;
    }

    public void setCurrentVillageCode(int[] currentVillageCode) {
        this.currentVillageCode = currentVillageCode;
    }

    public String[] getCurrentAssignedVillagesName() {
        return currentAssignedVillagesName;
    }

    public void setCurrentAssignedVillagesName(String[] currentAssignedVillagesName) {
        this.currentAssignedVillagesName = currentAssignedVillagesName;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Long getDob() {
        return dob;
    }

    public void setDob(Long dob) {
        this.dob = dob;
    }

    public boolean isFirstTimeLogin() {
        return firstTimeLogin;
    }

    public void setFirstTimeLogin(boolean firstTimeLogin) {
        this.firstTimeLogin = firstTimeLogin;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLoginFailureMsg() {
        return loginFailureMsg;
    }

    public void setLoginFailureMsg(String loginFailureMsg) {
        this.loginFailureMsg = loginFailureMsg;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserContactId() {
        return userContactId;
    }

    public void setUserContactId(Integer userContactId) {
        this.userContactId = userContactId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userType) {
        this.userRole = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getPhNoOfPM() {
        return phNoOfPM;
    }

    public void setPhNoOfPM(String phNoOfPM) {
        this.phNoOfPM = phNoOfPM;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isIsNagarPalikaUser() {
        return isNagarPalikaUser;
    }

    public void setIsNagarPalikaUser(boolean isNagarPalikaUser) {
        this.isNagarPalikaUser = isNagarPalikaUser;
    }

    public boolean isFirstTimePasswordChanged() {
        return firstTimePasswordChanged;
    }

    public void setFirstTimePasswordChanged(boolean firstTimePasswordChanged) {
        this.firstTimePasswordChanged = firstTimePasswordChanged;
    }

    public boolean isNagarPalikaUser() {
        return isNagarPalikaUser;
    }

    public void setNagarPalikaUser(boolean nagarPalikaUser) {
        isNagarPalikaUser = nagarPalikaUser;
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
}

