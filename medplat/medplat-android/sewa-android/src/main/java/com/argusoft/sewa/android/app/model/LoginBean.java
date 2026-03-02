/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.model;

import androidx.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author kelvin
 */
@DatabaseTable
public class LoginBean extends BaseEntity {

    @DatabaseField
    private String username;
    @DatabaseField
    private String password;
    @DatabaseField
    private String userToken;
    @DatabaseField
    private String userRole;
    @DatabaseField
    private String userRoleCode;
    @DatabaseField
    private boolean loggedIn;
    @DatabaseField
    private boolean firstLogin;
    @DatabaseField
    private String languageCode;
    @DatabaseField
    private String villageCode;
    @DatabaseField
    private String villageName;
    @DatabaseField
    private Long serverDate;
    @DatabaseField
    private Long lastUpdateOfLabels;
    @DatabaseField
    private Long lastUpdateOfListValues;
    @DatabaseField
    private Long lastUpdateOfAnnouncements;
    @DatabaseField
    private Long lastUpdateOfInventory;
    @DatabaseField
    private String firstName;
    @DatabaseField
    private String phoneNoOfPM;
    @DatabaseField
    private Long userID;
    @DatabaseField
    private boolean hidden;
    @DatabaseField
    private int updateCounter;
    @DatabaseField
    private boolean isNagarpalikaUser;
    @DatabaseField
    private boolean isRemember;
    @DatabaseField
    private boolean isTrainingUser;
    @DatabaseField
    private String passwordPlain;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserRoleCode() {
        return userRoleCode;
    }

    public void setUserRoleCode(String userRoleCode) {
        this.userRoleCode = userRoleCode;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public Long getServerDate() {
        return serverDate;
    }

    public void setServerDate(Long serverDate) {
        this.serverDate = serverDate;
    }

    public Long getLastUpdateOfLabels() {
        return lastUpdateOfLabels;
    }

    public void setLastUpdateOfLabels(Long lastUpdateOfLabels) {
        this.lastUpdateOfLabels = lastUpdateOfLabels;
    }

    public Long getLastUpdateOfListValues() {
        return lastUpdateOfListValues;
    }

    public void setLastUpdateOfListValues(Long lastUpdateOfListValues) {
        this.lastUpdateOfListValues = lastUpdateOfListValues;
    }

    public Long getLastUpdateOfAnnouncements() {
        return lastUpdateOfAnnouncements;
    }

    public void setLastUpdateOfAnnouncements(Long lastUpdateOfAnnouncements) {
        this.lastUpdateOfAnnouncements = lastUpdateOfAnnouncements;
    }

    public Long getLastUpdateOfInventory() {
        return lastUpdateOfInventory;
    }

    public void setLastUpdateOfInventory(Long lastUpdateOfInventory) {
        this.lastUpdateOfInventory = lastUpdateOfInventory;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNoOfPM() {
        return phoneNoOfPM;
    }

    public void setPhoneNoOfPM(String phoneNoOfPM) {
        this.phoneNoOfPM = phoneNoOfPM;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public int getUpdateCounter() {
        return updateCounter;
    }

    public void setUpdateCounter(int updateCounter) {
        this.updateCounter = updateCounter;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isNagarpalikaUser() {
        return isNagarpalikaUser;
    }

    public void setNagarpalikaUser(boolean nagarpalikaUser) {
        isNagarpalikaUser = nagarpalikaUser;
    }

    public boolean isTrainingUser() {
        return isTrainingUser;
    }

    public void setTrainingUser(boolean trainingUser) {
        isTrainingUser = trainingUser;
    }

    public String getPasswordPlain() {
        return passwordPlain;
    }

    public void setPasswordPlain(String passwordPlain) {
        this.passwordPlain = passwordPlain;
    }

    public boolean isRemember() {
        return isRemember;
    }

    public void setRemember(boolean remember) {
        isRemember = remember;
    }

    @NonNull
    @Override
    public String toString() {
        return "LoginBean{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userToken='" + userToken + '\'' +
                ", userRole='" + userRole + '\'' +
                ", userRoleCode='" + userRoleCode + '\'' +
                ", loggedIn=" + loggedIn +
                ", firstLogin=" + firstLogin +
                ", languageCode='" + languageCode + '\'' +
                ", villageCode='" + villageCode + '\'' +
                ", villageName='" + villageName + '\'' +
                ", serverDate=" + serverDate +
                ", lastUpdateOfLabels=" + lastUpdateOfLabels +
                ", lastUpdateOfListValues=" + lastUpdateOfListValues +
                ", lastUpdateOfAnnouncements=" + lastUpdateOfAnnouncements +
                ", lastUpdateOfInventory=" + lastUpdateOfInventory +
                ", firstName='" + firstName + '\'' +
                ", phoneNoOfPM='" + phoneNoOfPM + '\'' +
                ", userID=" + userID +
                ", hidden=" + hidden +
                ", updateCounter=" + updateCounter +
                ", isNagarpalikaUser=" + isNagarpalikaUser +
                ", isTrainingUser=" + isTrainingUser +
                ", passwordPlain='" + passwordPlain + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
