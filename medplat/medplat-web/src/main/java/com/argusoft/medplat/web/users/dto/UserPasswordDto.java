/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.users.dto;

/**
 * <p>Defines fields related to uesr password</p>
 * @author kunjan
 * @since 26/08/2020 5:30
 */
public class UserPasswordDto {
    Integer userId;
    String oldPassword;
    String newPassword;
    String username;
    String otp;
    String password;
    String mobileNumber;
    Boolean isWithoutLogin;
    Integer noOfAttempts;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Boolean getIsWithoutLogin() {
        return isWithoutLogin;
    }

    public void setIsWithoutLogin(Boolean isWithoutLogin) {
        this.isWithoutLogin = isWithoutLogin;
    }

    public Integer getNoOfAttempts() {
        return noOfAttempts;
    }

    public void setNoOfAttempts(Integer noOfAttempts) {
        this.noOfAttempts = noOfAttempts;
    }

    @Override
    public String toString() {
        return "UserPasswordDto{" + "userId=" + userId + ", oldPassword=" + oldPassword + ", newPassword=" + newPassword + ", username=" + username + ", otp=" + otp + ", password=" + password + '}';
    }
}
