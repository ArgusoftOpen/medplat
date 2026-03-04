/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.infrastructure.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * <p>
 *     Define school_master entity and its fields.
 * </p>
 * @author rahul
 * @since 26/08/20 11:00 AM
 *
 */
@javax.persistence.Entity
@Table(name = "school_master")
public class SchoolEntity extends EntityAuditInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    
    @Column
    private String name;
    
    @Column
    private String code;
    
    @Column(name = "grant_type")
    private Integer grantType;
    
    @Column(name = "school_type")
    private Integer schoolType;
    
    @Column(name = "no_of_teachers")
    private Integer noOfTeachers;
    
    @Column(name = "principal_name")
    private String principalName;
    
    @Column(name = "contact_number")
    private String contactNumber;
    
    @Column(name = "child_male_1_to_5")
    private Integer childMale1To5;
    
    @Column(name = "child_female_1_to_5")
    private Integer childFemale1To5;
    
    @Column(name = "child_male_6_to_8")
    private Integer childMale6To8;
    
    @Column(name = "child_female_6_to_8")
    private Integer childFemale6To8;
    
    @Column(name = "child_male_9_to_10")
    private Integer childMale9To10;
    
    @Column(name = "child_female_9_to_10")
    private Integer childFemale9To10;
    
    @Column(name = "child_male_11_to_12")
    private Integer childMale11To12;
    
    @Column(name = "child_female_11_to_12")
    private Integer childFemale11To12;
    
    @Column(name = "rbsk_team_id")
    private String rbskTeamId;
    
    @Column(name = "location_id")
    private Integer locationId;
    
    @Column(name = "is_primary_school")
    private Boolean isPrimarySchool;

    @Column(name = "is_higher_secondary_school")
    private Boolean isHigherSecondarySchool;
    
    @Column(name = "is_madresa")
    private Boolean isMadresa;
    
    @Column(name = "is_gurukul")
    private Boolean isGurukul;
    
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getGrantType() {
        return grantType;
    }

    public void setGrantType(Integer grantType) {
        this.grantType = grantType;
    }

    public Integer getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(Integer schoolType) {
        this.schoolType = schoolType;
    }

    public Integer getNoOfTeachers() {
        return noOfTeachers;
    }

    public void setNoOfTeachers(Integer noOfTeachers) {
        this.noOfTeachers = noOfTeachers;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Integer getChildMale1To5() {
        return childMale1To5;
    }

    public void setChildMale1To5(Integer childMale1To5) {
        this.childMale1To5 = childMale1To5;
    }

    public Integer getChildFemale1To5() {
        return childFemale1To5;
    }

    public void setChildFemale1To5(Integer childFemale1To5) {
        this.childFemale1To5 = childFemale1To5;
    }

    public Integer getChildMale6To8() {
        return childMale6To8;
    }

    public void setChildMale6To8(Integer childMale6To8) {
        this.childMale6To8 = childMale6To8;
    }

    public Integer getChildFemale6To8() {
        return childFemale6To8;
    }

    public void setChildFemale6To8(Integer childFemale6To8) {
        this.childFemale6To8 = childFemale6To8;
    }

    public Integer getChildMale9To10() {
        return childMale9To10;
    }

    public void setChildMale9To10(Integer childMale9To10) {
        this.childMale9To10 = childMale9To10;
    }

    public Integer getChildFemale9To10() {
        return childFemale9To10;
    }

    public void setChildFemale9To10(Integer childFemale9To10) {
        this.childFemale9To10 = childFemale9To10;
    }

    public Integer getChildMale11To12() {
        return childMale11To12;
    }

    public void setChildMale11To12(Integer childMale11To12) {
        this.childMale11To12 = childMale11To12;
    }

    public Integer getChildFemale11To12() {
        return childFemale11To12;
    }

    public void setChildFemale11To12(Integer childFemale11To12) {
        this.childFemale11To12 = childFemale11To12;
    }

    public String getRbskTeamId() {
        return rbskTeamId;
    }

    public void setRbskTeamId(String rbskTeamId) {
        this.rbskTeamId = rbskTeamId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Boolean getIsPrimarySchool() {
        return isPrimarySchool;
    }

    public void setIsPrimarySchool(Boolean isPrimarySchool) {
        this.isPrimarySchool = isPrimarySchool;
    }

    public Boolean getIsHigherSecondarySchool() {
        return isHigherSecondarySchool;
    }

    public void setIsHigherSecondarySchool(Boolean isHigherSecondarySchool) {
        this.isHigherSecondarySchool = isHigherSecondarySchool;
    }

    public Boolean getIsMadresa() {
        return isMadresa;
    }

    public void setIsMadresa(Boolean isMadresa) {
        this.isMadresa = isMadresa;
    }

    public Boolean getIsGurukul() {
        return isGurukul;
    }

    public void setIsGurukul(Boolean isGurukul) {
        this.isGurukul = isGurukul;
    }
    
}
