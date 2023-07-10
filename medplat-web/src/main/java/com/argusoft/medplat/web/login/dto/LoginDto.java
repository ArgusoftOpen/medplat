/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.login.dto;

import java.util.Date;

/**
 * <p>Defines fields related to login</p>
 * @author Harshit
 * @since 26/08/2020 5:30
 */
public class LoginDto {

    private Integer id;
    private String name;
    private String userName;
    private Integer roleId;
    private String roleName;
    private Integer minLocationId;
    private String minLocationName;
    private Integer minLocationLevel;
    private Integer rchInstitutionId;
    private String languagePreference;
    private String mobileNumber;
    private String gender;
    private String address;
    private Date dob;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public Integer getRchInstitutionId() {
        return rchInstitutionId;
    }

    public void setRchInstitutionId(Integer rchInstitutionId) {
        this.rchInstitutionId = rchInstitutionId;
    }

    public String getLanguagePreference() {
        return languagePreference;
    }

    public void setLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setMinLocationLevel(Integer minLocationLevel) {
        this.minLocationLevel = minLocationLevel;
    }

    public Integer getMinLocationLevel() {
        return minLocationLevel;
    }
}
