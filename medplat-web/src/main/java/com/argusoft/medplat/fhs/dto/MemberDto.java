/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fhs.dto;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import java.util.Date;
import java.util.Set;

/**
 * <p>
 * Used for member.
 * </p>
 *
 * @author harsh
 * @since 26/08/20 11:00 AM
 */
public class MemberDto extends EntityAuditInfo {

    private Integer id;
    private Integer fid;       //Family
    private String familyId;
    private String uniqueHealthId;
    private String emamtaHealthId;
    private Integer motherId;
    private String areaId;      //Family
    private Integer locationId;    //Family
    private String locationHierarchy;   //Family
    private String firstName;
    private String middleName;
    private String lastName;
    private String healthId;
    private String healthIdNumber;
    private String husbandName;
    private String grandfatherName;
    private String nameAsPerAadhar;
    private String gender;
    private Date dob;
    private Integer age;
    private Integer maritalStatus;
    private String yearOfWedding;
    private String aadharNumber;
    private String mobileNumber;
    private String ifsc;
    private String accountNumber;
    private Boolean familyHeadFlag;
    private Boolean bplFlag;    //Family
    private String caste;       //Family
    private String maaVatsalyaCardNumber;       //Family
    private Boolean isPregnantFlag;
    private Date lmpDate;
    private Date edd;
    private Short normalCycleDays;
    private String familyPlanningMethod;
    private Integer gravida;
    private String state;
    private Boolean isAadharVerified;
    private Boolean isMobileNumberVerified;
    private Boolean isNativeFlag;
    private Integer educationStatus;
    private Integer currentState;
    private Boolean isReport;
    private Set<Integer> chronicDiseaseDetails;
    private Set<Integer> congenitalAnomalyDetails;
    private Set<Integer> currentDiseaseDetails;
    private Set<Integer> eyeIssueDetails;
    private Boolean isEarlyRegistration;
    private Boolean isJsyBeneficiary;
    private Boolean isKpsyBeneficiary;
    private Boolean isIayBeneficiary;
    private Boolean isChiranjeeviYojnaBeneficiary;
    private Boolean isJsyPaymentDone;
    private String wpdState;
    private String immunisationGiven;
    private String bloodGroup;
    private Float weight;
    private Float haemoglobin;
    private String ancVisitDates;
    private Integer curPregRegDetId;
    private Date curPregRegDate;
    private Date lastDeliveryDate;
    private String lastDeliveryOutcome;
    private String additionalInfo;
    private String ashaInfo;
    private String anmInfo;
    private Date eligibleCoupleDate;
    private Short currentGravida;
    private Integer familyPlanningHealthInfrastructure;
    private String basicState;
    private String lastMethodOfContraception;
    private String relationWithHof;
    private String chronicDiseases; // comma separated
    private String hofMobileNumber;
    private Boolean isHighRiskCase;
    private Boolean healthInsurance;
    private String schemeDetail;

    public MemberDto() {
    }

    public MemberDto(Integer id, Integer fid, String familyId, String uniqueHealthId, String emamtaHealthId, Integer motherId, String areaId, Integer locationId, String locationHierarchy, String firstName, String middleName, String lastName, String husbandName, String grandfatherName, String nameAsPerAadhar, String gender, Date dob, Integer age, Integer maritalStatus, String yearOfWedding, String aadharNumber, String mobileNumber, String ifsc, String accountNumber, Boolean familyHeadFlag, Boolean bplFlag, String caste, String maaVatsalyaCardNumber, Boolean isPregnantFlag, Date lmpDate, Date edd, Short normalCycleDays, String familyPlanningMethod, Integer gravida, String state, Boolean isAadharVerified, Boolean isMobileNumberVerified, Boolean isNativeFlag, Integer educationStatus, Integer currentState, Boolean isReport, Set<Integer> chronicDiseaseDetails, Set<Integer> congenitalAnomalyDetails, Set<Integer> currentDiseaseDetails, Set<Integer> eyeIssueDetails, Boolean isEarlyRegistration, Boolean isJsyBeneficiary, Boolean isKpsyBeneficiary, Boolean isIayBeneficiary, Boolean isChiranjeeviYojnaBeneficiary, Boolean isJsyPaymentDone, String wpdState, String immunisationGiven, String bloodGroup, Float weight, Float haemoglobin, String ancVisitDates, Integer curPregRegDetId, Date curPregRegDate, Date lastDeliveryDate, String lastDeliveryOutcome, String additionalInfo, String ashaInfo, String anmInfo, Date eligibleCoupleDate, Short currentGravida, Integer familyPlanningHealthInfrastructure, String basicState, String lastMethodOfContraception, String relationWithHof, String chronicDiseases) {
        this.id = id;
        this.fid = fid;
        this.familyId = familyId;
        this.uniqueHealthId = uniqueHealthId;
        this.emamtaHealthId = emamtaHealthId;
        this.motherId = motherId;
        this.areaId = areaId;
        this.locationId = locationId;
        this.locationHierarchy = locationHierarchy;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.husbandName = husbandName;
        this.grandfatherName = grandfatherName;
        this.nameAsPerAadhar = nameAsPerAadhar;
        this.gender = gender;
        this.dob = dob;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.yearOfWedding = yearOfWedding;
        this.aadharNumber = aadharNumber;
        this.mobileNumber = mobileNumber;
        this.ifsc = ifsc;
        this.accountNumber = accountNumber;
        this.familyHeadFlag = familyHeadFlag;
        this.bplFlag = bplFlag;
        this.caste = caste;
        this.maaVatsalyaCardNumber = maaVatsalyaCardNumber;
        this.isPregnantFlag = isPregnantFlag;
        this.lmpDate = lmpDate;
        this.edd = edd;
        this.normalCycleDays = normalCycleDays;
        this.familyPlanningMethod = familyPlanningMethod;
        this.gravida = gravida;
        this.state = state;
        this.isAadharVerified = isAadharVerified;
        this.isMobileNumberVerified = isMobileNumberVerified;
        this.isNativeFlag = isNativeFlag;
        this.educationStatus = educationStatus;
        this.currentState = currentState;
        this.isReport = isReport;
        this.chronicDiseaseDetails = chronicDiseaseDetails;
        this.congenitalAnomalyDetails = congenitalAnomalyDetails;
        this.currentDiseaseDetails = currentDiseaseDetails;
        this.eyeIssueDetails = eyeIssueDetails;
        this.isEarlyRegistration = isEarlyRegistration;
        this.isJsyBeneficiary = isJsyBeneficiary;
        this.isKpsyBeneficiary = isKpsyBeneficiary;
        this.isIayBeneficiary = isIayBeneficiary;
        this.isChiranjeeviYojnaBeneficiary = isChiranjeeviYojnaBeneficiary;
        this.isJsyPaymentDone = isJsyPaymentDone;
        this.wpdState = wpdState;
        this.immunisationGiven = immunisationGiven;
        this.bloodGroup = bloodGroup;
        this.weight = weight;
        this.haemoglobin = haemoglobin;
        this.ancVisitDates = ancVisitDates;
        this.curPregRegDetId = curPregRegDetId;
        this.curPregRegDate = curPregRegDate;
        this.lastDeliveryDate = lastDeliveryDate;
        this.lastDeliveryOutcome = lastDeliveryOutcome;
        this.additionalInfo = additionalInfo;
        this.ashaInfo = ashaInfo;
        this.anmInfo = anmInfo;
        this.eligibleCoupleDate = eligibleCoupleDate;
        this.currentGravida = currentGravida;
        this.familyPlanningHealthInfrastructure = familyPlanningHealthInfrastructure;
        this.basicState = basicState;
        this.lastMethodOfContraception = lastMethodOfContraception;
        this.relationWithHof = relationWithHof;
        this.chronicDiseases = chronicDiseases;
    }

    public String getFamilyId() {
        return familyId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public Boolean getFamilyHeadFlag() {
        return familyHeadFlag;
    }

    public Date getDob() {
        return dob;
    }

    public String getUniqueHealthId() {
        return uniqueHealthId;
    }

    public String getIfsc() {
        return ifsc;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getMaaVatsalyaCardNumber() {
        return maaVatsalyaCardNumber;
    }

    public Boolean getIsPregnantFlag() {
        return isPregnantFlag;
    }

    public Date getLmpDate() {
        return lmpDate;
    }

    public String getFamilyPlanningMethod() {
        return familyPlanningMethod;
    }

    public String getGrandfatherName() {
        return grandfatherName;
    }

    public String getEmamtaHealthId() {
        return emamtaHealthId;
    }

    public Boolean getIsAadharVerified() {
        return isAadharVerified;
    }

    public Boolean getIsMobileNumberVerified() {
        return isMobileNumberVerified;
    }

    public Boolean getIsNativeFlag() {
        return isNativeFlag;
    }

    public String getNameAsPerAadhar() {
        return nameAsPerAadhar;
    }

    public Boolean getIsReport() {
        return isReport;
    }

    public Integer getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public Integer getCurrentState() {
        return currentState;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setFamilyHeadFlag(Boolean familyHeadFlag) {
        this.familyHeadFlag = familyHeadFlag;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setUniqueHealthId(String uniqueHealthId) {
        this.uniqueHealthId = uniqueHealthId;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setMaaVatsalyaCardNumber(String maaVatsalyaCardNumber) {
        this.maaVatsalyaCardNumber = maaVatsalyaCardNumber;
    }

    public void setIsPregnantFlag(Boolean isPregnantFlag) {
        this.isPregnantFlag = isPregnantFlag;
    }

    public void setLmpDate(Date lmpDate) {
        this.lmpDate = lmpDate;
    }

    public void setFamilyPlanningMethod(String familyPlanningMethod) {
        this.familyPlanningMethod = familyPlanningMethod;
    }

    public void setGrandfatherName(String grandfatherName) {
        this.grandfatherName = grandfatherName;
    }

    public void setEmamtaHealthId(String emamtaHealthId) {
        this.emamtaHealthId = emamtaHealthId;
    }

    public void setIsAadharVerified(Boolean isAadharVerified) {
        this.isAadharVerified = isAadharVerified;
    }

    public void setIsMobileNumberVerified(Boolean isMobileNumberVerified) {
        this.isMobileNumberVerified = isMobileNumberVerified;
    }

    public void setIsNativeFlag(Boolean isNativeFlag) {
        this.isNativeFlag = isNativeFlag;
    }

    public Integer getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Integer maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Short getNormalCycleDays() {
        return normalCycleDays;
    }

    public void setNormalCycleDays(Short normalCycleDays) {
        this.normalCycleDays = normalCycleDays;
    }

    public Integer getEducationStatus() {
        return educationStatus;
    }

    public void setEducationStatus(Integer educationStatus) {
        this.educationStatus = educationStatus;
    }

    public void setIsReport(Boolean isReport) {
        this.isReport = isReport;
    }

    public void setNameAsPerAadhar(String nameAsPerAadhar) {
        this.nameAsPerAadhar = nameAsPerAadhar;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCurrentState(Integer currentState) {
        this.currentState = currentState;
    }

    public Set<Integer> getChronicDiseaseDetails() {
        return chronicDiseaseDetails;
    }

    public void setChronicDiseaseDetails(Set<Integer> chronicDiseaseDetails) {
        this.chronicDiseaseDetails = chronicDiseaseDetails;
    }

    public Set<Integer> getCongenitalAnomalyDetails() {
        return congenitalAnomalyDetails;
    }

    public void setCongenitalAnomalyDetails(Set<Integer> congenitalAnomalyDetails) {
        this.congenitalAnomalyDetails = congenitalAnomalyDetails;
    }

    public Set<Integer> getCurrentDiseaseDetails() {
        return currentDiseaseDetails;
    }

    public void setCurrentDiseaseDetails(Set<Integer> currentDiseaseDetails) {
        this.currentDiseaseDetails = currentDiseaseDetails;
    }

    public Set<Integer> getEyeIssueDetails() {
        return eyeIssueDetails;
    }

    public void setEyeIssueDetails(Set<Integer> eyeIssueDetails) {
        this.eyeIssueDetails = eyeIssueDetails;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getYearOfWedding() {
        return yearOfWedding;
    }

    public void setYearOfWedding(String yearOfWedding) {
        this.yearOfWedding = yearOfWedding;
    }

    public Boolean getIsEarlyRegistration() {
        return isEarlyRegistration;
    }

    public void setIsEarlyRegistration(Boolean isEarlyRegistration) {
        this.isEarlyRegistration = isEarlyRegistration;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Date getEdd() {
        return edd;
    }

    public void setEdd(Date edd) {
        this.edd = edd;
    }

    public Boolean getIsJsyBeneficiary() {
        return isJsyBeneficiary;
    }

    public void setIsJsyBeneficiary(Boolean isJsyBeneficiary) {
        this.isJsyBeneficiary = isJsyBeneficiary;
    }

    public Boolean getIsJsyPaymentDone() {
        return isJsyPaymentDone;
    }

    public void setIsJsyPaymentDone(Boolean isJsyPaymentDone) {
        this.isJsyPaymentDone = isJsyPaymentDone;
    }

    public String getImmunisationGiven() {
        return immunisationGiven;
    }

    public void setImmunisationGiven(String immunisationGiven) {
        this.immunisationGiven = immunisationGiven;
    }

    public Boolean getIsKpsyBeneficiary() {
        return isKpsyBeneficiary;
    }

    public void setIsKpsyBeneficiary(Boolean isKpsyBeneficiary) {
        this.isKpsyBeneficiary = isKpsyBeneficiary;
    }

    public Boolean getIsIayBeneficiary() {
        return isIayBeneficiary;
    }

    public void setIsIayBeneficiary(Boolean isIayBeneficiary) {
        this.isIayBeneficiary = isIayBeneficiary;
    }

    public Boolean getIsChiranjeeviYojnaBeneficiary() {
        return isChiranjeeviYojnaBeneficiary;
    }

    public void setIsChiranjeeviYojnaBeneficiary(Boolean isChiranjeeviYojnaBeneficiary) {
        this.isChiranjeeviYojnaBeneficiary = isChiranjeeviYojnaBeneficiary;
    }

    public Boolean getBplFlag() {
        return bplFlag;
    }

    public void setBplFlag(Boolean bplFlag) {
        this.bplFlag = bplFlag;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getLocationHierarchy() {
        return locationHierarchy;
    }

    public void setLocationHierarchy(String locationHierarchy) {
        this.locationHierarchy = locationHierarchy;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getMotherId() {
        return motherId;
    }

    public void setMotherId(Integer motherId) {
        this.motherId = motherId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getWpdState() {
        return wpdState;
    }

    public void setWpdState(String wpdState) {
        this.wpdState = wpdState;
    }

    public Float getHaemoglobin() {
        return haemoglobin;
    }

    public void setHaemoglobin(Float haemoglobin) {
        this.haemoglobin = haemoglobin;
    }

    public String getAncVisitDates() {
        return ancVisitDates;
    }

    public void setAncVisitDates(String ancVisitDates) {
        this.ancVisitDates = ancVisitDates;
    }

    public Integer getCurPregRegDetId() {
        return curPregRegDetId;
    }

    public void setCurPregRegDetId(Integer curPregRegDetId) {
        this.curPregRegDetId = curPregRegDetId;
    }

    public Date getCurPregRegDate() {
        return curPregRegDate;
    }

    public void setCurPregRegDate(Date curPregRegDate) {
        this.curPregRegDate = curPregRegDate;
    }

    public Date getLastDeliveryDate() {
        return lastDeliveryDate;
    }

    public void setLastDeliveryDate(Date lastDeliveryDate) {
        this.lastDeliveryDate = lastDeliveryDate;
    }

    public String getLastDeliveryOutcome() {
        return lastDeliveryOutcome;
    }

    public void setLastDeliveryOutcome(String lastDeliveryOutcome) {
        this.lastDeliveryOutcome = lastDeliveryOutcome;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getAshaInfo() {
        return ashaInfo;
    }

    public void setAshaInfo(String ashaInfo) {
        this.ashaInfo = ashaInfo;
    }

    public String getAnmInfo() {
        return anmInfo;
    }

    public void setAnmInfo(String anmInfo) {
        this.anmInfo = anmInfo;
    }

    public Integer getGravida() {
        return gravida;
    }

    public void setGravida(Integer gravida) {
        this.gravida = gravida;
    }

    public Date getEligibleCoupleDate() {
        return eligibleCoupleDate;
    }

    public void setEligibleCoupleDate(Date eligibleCoupleDate) {
        this.eligibleCoupleDate = eligibleCoupleDate;
    }

    public String getHusbandName() {
        return husbandName;
    }

    public void setHusbandName(String husbandName) {
        this.husbandName = husbandName;
    }

    public Short getCurrentGravida() {
        return currentGravida;
    }

    public void setCurrentGravida(Short currentGravida) {
        this.currentGravida = currentGravida;
    }

    public Integer getFamilyPlanningHealthInfrastructure() {
        return familyPlanningHealthInfrastructure;
    }

    public void setFamilyPlanningHealthInfrastructure(Integer familyPlanningHealthInfrastructure) {
        this.familyPlanningHealthInfrastructure = familyPlanningHealthInfrastructure;
    }

    public String getBasicState() {
        return basicState;
    }

    public void setBasicState(String basicState) {
        this.basicState = basicState;
    }

    public String getLastMethodOfContraception() {
        return lastMethodOfContraception;
    }

    public void setLastMethodOfContraception(String lastMethodOfContraception) {
        this.lastMethodOfContraception = lastMethodOfContraception;
    }

    public String getRelationWithHof() {
        return relationWithHof;
    }

    public void setRelationWithHof(String relationWithHof) {
        this.relationWithHof = relationWithHof;
    }

    public String getChronicDiseases() {
        return chronicDiseases;
    }

    public void setChronicDiseases(String chronicDiseases) {
        this.chronicDiseases = chronicDiseases;
    }

    public String getHofMobileNumber() {
        return hofMobileNumber;
    }

    public void setHofMobileNumber(String hofMobileNumber) {
        this.hofMobileNumber = hofMobileNumber;
    }

    public Boolean getIsHighRiskCase() {
        return isHighRiskCase;
    }

    public void setIsHighRiskCase(Boolean isHighRiskCase) {
        this.isHighRiskCase = isHighRiskCase;
    }

    public String getHealthId() {
        return healthId;
    }

    public void setHealthId(String healthId) {
        this.healthId = healthId;
    }

    public String getHealthIdNumber() {
        return healthIdNumber;
    }

    public void setHealthIdNumber(String healthIdNumber) {
        this.healthIdNumber = healthIdNumber;
    }

    public Boolean getHealthInsurance() {
        return healthInsurance;
    }

    public void setHealthInsurance(Boolean healthInsurance) {
        this.healthInsurance = healthInsurance;
    }

    public String getSchemeDetail() {
        return schemeDetail;
    }

    public void setSchemeDetail(String schemeDetail) {
        this.schemeDetail = schemeDetail;
    }

    @Override
    public String toString() {
        return "MemberDto{" +
                "id=" + id +
                ", fid=" + fid +
                ", familyId='" + familyId + '\'' +
                ", uniqueHealthId='" + uniqueHealthId + '\'' +
                ", emamtaHealthId='" + emamtaHealthId + '\'' +
                ", motherId=" + motherId +
                ", areaId='" + areaId + '\'' +
                ", locationId=" + locationId +
                ", locationHierarchy='" + locationHierarchy + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", husbandName='" + husbandName + '\'' +
                ", grandfatherName='" + grandfatherName + '\'' +
                ", nameAsPerAadhar='" + nameAsPerAadhar + '\'' +
                ", gender='" + gender + '\'' +
                ", dob=" + dob +
                ", age=" + age +
                ", maritalStatus=" + maritalStatus +
                ", yearOfWedding='" + yearOfWedding + '\'' +
                ", aadharNumber='" + aadharNumber + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", ifsc='" + ifsc + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", familyHeadFlag=" + familyHeadFlag +
                ", bplFlag=" + bplFlag +
                ", caste='" + caste + '\'' +
                ", maaVatsalyaCardNumber='" + maaVatsalyaCardNumber + '\'' +
                ", isPregnantFlag=" + isPregnantFlag +
                ", lmpDate=" + lmpDate +
                ", edd=" + edd +
                ", normalCycleDays=" + normalCycleDays +
                ", familyPlanningMethod='" + familyPlanningMethod + '\'' +
                ", gravida=" + gravida +
                ", state='" + state + '\'' +
                ", isAadharVerified=" + isAadharVerified +
                ", isMobileNumberVerified=" + isMobileNumberVerified +
                ", isNativeFlag=" + isNativeFlag +
                ", educationStatus=" + educationStatus +
                ", currentState=" + currentState +
                ", isReport=" + isReport +
                ", chronicDiseaseDetails=" + chronicDiseaseDetails +
                ", congenitalAnomalyDetails=" + congenitalAnomalyDetails +
                ", currentDiseaseDetails=" + currentDiseaseDetails +
                ", eyeIssueDetails=" + eyeIssueDetails +
                ", isEarlyRegistration=" + isEarlyRegistration +
                ", isJsyBeneficiary=" + isJsyBeneficiary +
                ", isKpsyBeneficiary=" + isKpsyBeneficiary +
                ", isIayBeneficiary=" + isIayBeneficiary +
                ", isChiranjeeviYojnaBeneficiary=" + isChiranjeeviYojnaBeneficiary +
                ", isJsyPaymentDone=" + isJsyPaymentDone +
                ", wpdState='" + wpdState + '\'' +
                ", immunisationGiven='" + immunisationGiven + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", weight=" + weight +
                ", haemoglobin=" + haemoglobin +
                ", ancVisitDates='" + ancVisitDates + '\'' +
                ", curPregRegDetId=" + curPregRegDetId +
                ", curPregRegDate=" + curPregRegDate +
                ", lastDeliveryDate=" + lastDeliveryDate +
                ", lastDeliveryOutcome='" + lastDeliveryOutcome + '\'' +
                ", additionalInfo='" + additionalInfo + '\'' +
                ", ashaInfo='" + ashaInfo + '\'' +
                ", anmInfo='" + anmInfo + '\'' +
                ", eligibleCoupleDate=" + eligibleCoupleDate +
                ", currentGravida=" + currentGravida +
                ", familyPlanningHealthInfrastructure=" + familyPlanningHealthInfrastructure +
                ", basicState='" + basicState + '\'' +
                ", lastMethodOfContraception='" + lastMethodOfContraception + '\'' +
                ", relationWithHof='" + relationWithHof + '\'' +
                ", chronicDiseases='" + chronicDiseases + '\'' +
                '}';
    }

}
