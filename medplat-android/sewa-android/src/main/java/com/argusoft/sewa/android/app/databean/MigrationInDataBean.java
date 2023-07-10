package com.argusoft.sewa.android.app.databean;

import java.util.List;
import java.util.Map;

/**
 * Created by prateek on 15/3/19.
 */
public class MigrationInDataBean {

    private Boolean outOfState;
    private Long nearestLocId;
    private String villageName;
    private String fhwOrAshaName;
    private String fhwOrAshaPhone;
    private String firstname;
    private String middleName;
    private String lastName;
    private String gender;
    private Long dob;
    private String healthId;
    private String aadharNumber;
    private Map<String, String> aadharMap;
    private String phoneNumber;
    private Boolean isPregnant;
    private Long lmp;
    private String bankAccountNumber;
    private String ifscCode;
    private String hofFullName;
    private String maaVatsalyaNumber;
    private String rsbyNumber;
    private Integer totalLiveChildren;
    private List<MemberDataBean> childrensUnder5;
    private Boolean stayingWithFamily;
    private Long familyId;
    private Long villageId;
    private Long areaId;
    private Long reportedOn;

    public Boolean getOutOfState() {
        return outOfState;
    }

    public void setOutOfState(Boolean outOfState) {
        this.outOfState = outOfState;
    }

    public Long getNearestLocId() {
        return nearestLocId;
    }

    public void setNearestLocId(Long nearestLocId) {
        this.nearestLocId = nearestLocId;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getFhwOrAshaName() {
        return fhwOrAshaName;
    }

    public void setFhwOrAshaName(String fhwOrAshaName) {
        this.fhwOrAshaName = fhwOrAshaName;
    }

    public String getFhwOrAshaPhone() {
        return fhwOrAshaPhone;
    }

    public void setFhwOrAshaPhone(String fhwOrAshaPhone) {
        this.fhwOrAshaPhone = fhwOrAshaPhone;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
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

    public Long getDob() {
        return dob;
    }

    public void setDob(Long dob) {
        this.dob = dob;
    }

    public String getHealthId() {
        return healthId;
    }

    public void setHealthId(String healthId) {
        this.healthId = healthId;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public Map<String, String> getAadharMap() {
        return aadharMap;
    }

    public void setAadharMap(Map<String, String> aadharMap) {
        this.aadharMap = aadharMap;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getPregnant() {
        return isPregnant;
    }

    public void setPregnant(Boolean pregnant) {
        isPregnant = pregnant;
    }

    public Long getLmp() {
        return lmp;
    }

    public void setLmp(Long lmp) {
        this.lmp = lmp;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getHofFullName() {
        return hofFullName;
    }

    public void setHofFullName(String hofFullName) {
        this.hofFullName = hofFullName;
    }

    public String getMaaVatsalyaNumber() {
        return maaVatsalyaNumber;
    }

    public void setMaaVatsalyaNumber(String maaVatsalyaNumber) {
        this.maaVatsalyaNumber = maaVatsalyaNumber;
    }

    public String getRsbyNumber() {
        return rsbyNumber;
    }

    public void setRsbyNumber(String rsbyNumber) {
        this.rsbyNumber = rsbyNumber;
    }

    public Integer getTotalLiveChildren() {
        return totalLiveChildren;
    }

    public void setTotalLiveChildren(Integer totalLiveChildren) {
        this.totalLiveChildren = totalLiveChildren;
    }

    public List<MemberDataBean> getChildrensUnder5() {
        return childrensUnder5;
    }

    public void setChildrensUnder5(List<MemberDataBean> childrensUnder5) {
        this.childrensUnder5 = childrensUnder5;
    }

    public Boolean getStayingWithFamily() {
        return stayingWithFamily;
    }

    public void setStayingWithFamily(Boolean stayingWithFamily) {
        this.stayingWithFamily = stayingWithFamily;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public Long getVillageId() {
        return villageId;
    }

    public void setVillageId(Long villageId) {
        this.villageId = villageId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getReportedOn() {
        return reportedOn;
    }

    public void setReportedOn(Long reportedOn) {
        this.reportedOn = reportedOn;
    }
}
