package com.argusoft.medplat.config.security;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author dax
 */
public class ImtechoSecurityUser implements Serializable {

    private Integer id;
    private String name;
    private String userName;
    private Integer roleId;
    private String roleName;
    private Integer minLocationId;
    private String minLocationName;
    private Integer minLocationLevel;
    private String roleCode;
    private Integer rchInstitutionId;
    private String languagePreference;
    private String mobileNumber;
    private String gender;
    private String address;
    private Date dob;

    public ImtechoSecurityUser(Integer id, String name, String username, Integer roleId, String roleName,
            Integer minLocationId, String minLocationName, String roleCode, Integer rchInstitutionId, String languagePreference, String mobileNumber,
            String gender, String address, Date dob, Integer minLocationLevel) {
        this.id = id;
        this.name = name;
        this.userName = username;
        this.roleId = roleId;
        this.roleName = roleName;
        this.minLocationId = minLocationId;
        this.minLocationName = minLocationName;
        this.roleCode = roleCode;
        this.rchInstitutionId = rchInstitutionId;
        this.languagePreference = languagePreference;
        this.mobileNumber = mobileNumber;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.minLocationLevel = minLocationLevel;
    }

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

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
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

    public Integer getMinLocationLevel() {
        return minLocationLevel;
    }

    public void setMinLocationLevel(Integer minLocationLevel) {
        this.minLocationLevel = minLocationLevel;
    }
}
