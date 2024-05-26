package com.argusoft.medplat.mobile.dto;

import java.util.Date;

/**
 * @author prateek
 */
public class MemberDataBean {

    private Integer id;

    private Integer locationId;

    private String familyId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String gender;

    private Integer maritalStatus;

    private String aadharNumber;

    private boolean isAadharUpdated;

    private String nameBasedOnAadhar;

    private String mobileNumber;

    private Boolean familyHeadFlag;

    private Long dob;

    private Date dateOfBirth;

    private String uniqueHealthId;

    private String ifsc;

    private String accountNumber;

    private String maaVatsalyaCardNumber;

    private Boolean isPregnantFlag;

    private Long lmpDate;

    private Short normalCycleDays;

    private String familyPlanningMethod;

    private Long fpInsertOperateDate;

    private String healthIssueIds;

    private String state;

    private Integer createdBy;

    private Integer updatedBy;

    private Date createdOn;

    private Date updatedOn;

    private String grandfatherName;

    private Integer educationStatus;

    //Comma Separated IDs
    private String congenitalAnomalyIds;

    //Comma Separated IDs
    private String chronicDiseaseIds;

    //Comma Separated IDs
    private String currentDiseaseIds;

    //Comma Separated IDs
    private String eyeIssueIds;

    private Short yearOfWedding;

    private Long dateOfWedding;

    private Boolean jsyPaymentGiven;

    private Boolean isEarlyRegistration;

    private Boolean jsyBeneficiary;

    private Boolean iayBeneficiary;

    private Boolean kpsyBeneficiary;

    private Boolean chiranjeeviYojnaBeneficiary;

    private Float haemoglobin;

    private Float weight;

    private Long edd;

    private String ancVisitDates;

    private String immunisationGiven;

    private String bloodGroup;

    private String placeOfBirth;

    private Float birthWeight;

    private Integer motherId;

    private Boolean complementaryFeedingStarted;

    private String lastMethodOfContraception;

    private Boolean isHighRiskCase;

    private Integer curPregRegDetId;

    private Long curPregRegDate;

    private Boolean menopauseArrived;

    private String syncStatus;

    private Boolean isIucdRemoved;

    private Long iucdRemovalDate;

    private Long lastDeliveryDate;

    private Boolean hysterectomyDone;

    private String childNrcCmtcStatus;

    private String lastDeliveryOutcome;

    //Comma Separated
    private String previousPregnancyComplication;

    private String additionalInfo;

    private Long npcbScreeningDate;

    private Boolean fhsrPhoneVerified;

    private String address;

    private Date lmp;

    private Short menstruationDays;

    private Integer mytechoUserId;

    private Short currentGravida;

    private Short currentPara;

    private Integer husbandId;

    private Boolean isMobileNumberVerified;

    private String relationWithHof;
    //NCD Related
    private Long cbacDate;
    //NCD Related
    private String hypYear;         //Comma Separated financial year in which HYPERTENSION Screening is done
    //NCD Related
    private String oralYear;        //Comma Separated financial year in which ORAL Screening is done
    //NCD Related
    private String diabetesYear;    //Comma Separated financial year in which DIABETES Screening is done
    //NCD Related
    private String breastYear;      //Comma Separated financial year in which BREAST Screening is done
    //NCD Related
    private String cervicalYear;    //Comma Separated financial year in which CERVICAL Screening is done
    //NCD Related
    private String mentalHealthYear;    //Comma Separated financial year in which MENTAL HEALTH Screening is done

    private String healthYear;    //Comma Separated financial year in which HEALTH Screening is done

    private String healthId;

    private String healthIdNumber;

    private Boolean vulnerableFlag;

    private Boolean healthInsurance;

    private String schemeDetail;
    private Boolean personalHistoryDone;

    private Boolean confirmedDiabetes;

    private Boolean suspectedForDiabetes;

    private Integer cbacScore;

    private Boolean sufferingDiabetes;

    private Boolean sufferingHypertension;

    private Boolean sufferingMentalHealth;

    private String pmjayAvailability;
    private String alcoholAddiction;
    private String smokingAddiction;
    private String tobaccoAddiction;
    private Long hypDiaMentalServiceDate;

    public Long getHypDiaMentalServiceDate() {
        return hypDiaMentalServiceDate;
    }

    public void setHypDiaMentalServiceDate(Long hypDiaMentalServiceDate) {
        this.hypDiaMentalServiceDate = hypDiaMentalServiceDate;
    }

    public Long getCancerServiceDate() {
        return cancerServiceDate;
    }

    public void setCancerServiceDate(Long cancerServiceDate) {
        this.cancerServiceDate = cancerServiceDate;
    }

    private Long cancerServiceDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public boolean isIsAadharUpdated() {
        return isAadharUpdated;
    }

    public void setIsAadharUpdated(boolean isAadharUpdated) {
        this.isAadharUpdated = isAadharUpdated;
    }

    public String getNameBasedOnAadhar() {
        return nameBasedOnAadhar;
    }

    public void setNameBasedOnAadhar(String nameBasedOnAadhar) {
        this.nameBasedOnAadhar = nameBasedOnAadhar;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Boolean getFamilyHeadFlag() {
        return familyHeadFlag;
    }

    public void setFamilyHeadFlag(Boolean familyHeadFlag) {
        this.familyHeadFlag = familyHeadFlag;
    }

    public Long getDob() {
        return dob;
    }

    public void setDob(Long dob) {
        this.dob = dob;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUniqueHealthId() {
        return uniqueHealthId;
    }

    public void setUniqueHealthId(String uniqueHealthId) {
        this.uniqueHealthId = uniqueHealthId;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getMaaVatsalyaCardNumber() {
        return maaVatsalyaCardNumber;
    }

    public void setMaaVatsalyaCardNumber(String maaVatsalyaCardNumber) {
        this.maaVatsalyaCardNumber = maaVatsalyaCardNumber;
    }

    public Boolean getIsPregnantFlag() {
        return isPregnantFlag;
    }

    public void setIsPregnantFlag(Boolean isPregnantFlag) {
        this.isPregnantFlag = isPregnantFlag;
    }

    public Long getLmpDate() {
        return lmpDate;
    }

    public void setLmpDate(Long lmpDate) {
        this.lmpDate = lmpDate;
    }

    public String getFamilyPlanningMethod() {
        return familyPlanningMethod;
    }

    public void setFamilyPlanningMethod(String familyPlanningMethod) {
        this.familyPlanningMethod = familyPlanningMethod;
    }

    public Long getFpInsertOperateDate() {
        return fpInsertOperateDate;
    }

    public void setFpInsertOperateDate(Long fpInsertOperateDate) {
        this.fpInsertOperateDate = fpInsertOperateDate;
    }

    public String getHealthIssueIds() {
        return healthIssueIds;
    }

    public void setHealthIssueIds(String healthIssueIds) {
        this.healthIssueIds = healthIssueIds;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getGrandfatherName() {
        return grandfatherName;
    }

    public void setGrandfatherName(String grandfatherName) {
        this.grandfatherName = grandfatherName;
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

    public String getCongenitalAnomalyIds() {
        return congenitalAnomalyIds;
    }

    public void setCongenitalAnomalyIds(String congenitalAnomalyIds) {
        this.congenitalAnomalyIds = congenitalAnomalyIds;
    }

    public String getChronicDiseaseIds() {
        return chronicDiseaseIds;
    }

    public void setChronicDiseaseIds(String chronicDiseaseIds) {
        this.chronicDiseaseIds = chronicDiseaseIds;
    }

    public String getCurrentDiseaseIds() {
        return currentDiseaseIds;
    }

    public void setCurrentDiseaseIds(String currentDiseaseIds) {
        this.currentDiseaseIds = currentDiseaseIds;
    }

    public String getEyeIssueIds() {
        return eyeIssueIds;
    }

    public void setEyeIssueIds(String eyeIssueIds) {
        this.eyeIssueIds = eyeIssueIds;
    }

    public Short getYearOfWedding() {
        return yearOfWedding;
    }

    public void setYearOfWedding(Short yearOfWedding) {
        this.yearOfWedding = yearOfWedding;
    }

    public Boolean getJsyPaymentGiven() {
        return jsyPaymentGiven;
    }

    public void setJsyPaymentGiven(Boolean jsyPaymentGiven) {
        this.jsyPaymentGiven = jsyPaymentGiven;
    }

    public Boolean getIsEarlyRegistration() {
        return isEarlyRegistration;
    }

    public void setIsEarlyRegistration(Boolean isEarlyRegistration) {
        this.isEarlyRegistration = isEarlyRegistration;
    }

    public Boolean getJsyBeneficiary() {
        return jsyBeneficiary;
    }

    public void setJsyBeneficiary(Boolean jsyBeneficiary) {
        this.jsyBeneficiary = jsyBeneficiary;
    }

    public Boolean getIayBeneficiary() {
        return iayBeneficiary;
    }

    public void setIayBeneficiary(Boolean iayBeneficiary) {
        this.iayBeneficiary = iayBeneficiary;
    }

    public Boolean getKpsyBeneficiary() {
        return kpsyBeneficiary;
    }

    public void setKpsyBeneficiary(Boolean kpsyBeneficiary) {
        this.kpsyBeneficiary = kpsyBeneficiary;
    }

    public Boolean getChiranjeeviYojnaBeneficiary() {
        return chiranjeeviYojnaBeneficiary;
    }

    public void setChiranjeeviYojnaBeneficiary(Boolean chiranjeeviYojnaBeneficiary) {
        this.chiranjeeviYojnaBeneficiary = chiranjeeviYojnaBeneficiary;
    }

    public Float getHaemoglobin() {
        return haemoglobin;
    }

    public void setHaemoglobin(Float haemoglobin) {
        this.haemoglobin = haemoglobin;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Long getEdd() {
        return edd;
    }

    public void setEdd(Long edd) {
        this.edd = edd;
    }

    public String getAncVisitDates() {
        return ancVisitDates;
    }

    public void setAncVisitDates(String ancVisitDates) {
        this.ancVisitDates = ancVisitDates;
    }

    public String getImmunisationGiven() {
        return immunisationGiven;
    }

    public void setImmunisationGiven(String immunisationGiven) {
        this.immunisationGiven = immunisationGiven;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public Float getBirthWeight() {
        return birthWeight;
    }

    public void setBirthWeight(Float birthWeight) {
        this.birthWeight = birthWeight;
    }

    public Integer getMotherId() {
        return motherId;
    }

    public void setMotherId(Integer motherId) {
        this.motherId = motherId;
    }

    public Boolean getComplementaryFeedingStarted() {
        return complementaryFeedingStarted;
    }

    public void setComplementaryFeedingStarted(Boolean complementaryFeedingStarted) {
        this.complementaryFeedingStarted = complementaryFeedingStarted;
    }

    public String getLastMethodOfContraception() {
        return lastMethodOfContraception;
    }

    public void setLastMethodOfContraception(String lastMethodOfContraception) {
        this.lastMethodOfContraception = lastMethodOfContraception;
    }

    public Boolean getIsHighRiskCase() {
        return isHighRiskCase;
    }

    public void setIsHighRiskCase(Boolean isHighRiskCase) {
        this.isHighRiskCase = isHighRiskCase;
    }

    public Integer getCurPregRegDetId() {
        return curPregRegDetId;
    }

    public void setCurPregRegDetId(Integer curPregRegDetId) {
        this.curPregRegDetId = curPregRegDetId;
    }

    public Long getCurPregRegDate() {
        return curPregRegDate;
    }

    public void setCurPregRegDate(Long curPregRegDate) {
        this.curPregRegDate = curPregRegDate;
    }

    public Boolean getMenopauseArrived() {
        return menopauseArrived;
    }

    public void setMenopauseArrived(Boolean menopauseArrived) {
        this.menopauseArrived = menopauseArrived;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Boolean getIsIucdRemoved() {
        return isIucdRemoved;
    }

    public void setIsIucdRemoved(Boolean isIucdRemoved) {
        this.isIucdRemoved = isIucdRemoved;
    }

    public Long getIucdRemovalDate() {
        return iucdRemovalDate;
    }

    public void setIucdRemovalDate(Long iucdRemovalDate) {
        this.iucdRemovalDate = iucdRemovalDate;
    }

    public Long getLastDeliveryDate() {
        return lastDeliveryDate;
    }

    public void setLastDeliveryDate(Long lastDeliveryDate) {
        this.lastDeliveryDate = lastDeliveryDate;
    }

    public Boolean getHysterectomyDone() {
        return hysterectomyDone;
    }

    public void setHysterectomyDone(Boolean hysterectomyDone) {
        this.hysterectomyDone = hysterectomyDone;
    }

    public String getChildNrcCmtcStatus() {
        return childNrcCmtcStatus;
    }

    public void setChildNrcCmtcStatus(String childNrcCmtcStatus) {
        this.childNrcCmtcStatus = childNrcCmtcStatus;
    }

    public String getLastDeliveryOutcome() {
        return lastDeliveryOutcome;
    }

    public void setLastDeliveryOutcome(String lastDeliveryOutcome) {
        this.lastDeliveryOutcome = lastDeliveryOutcome;
    }

    public String getPreviousPregnancyComplication() {
        return previousPregnancyComplication;
    }

    public void setPreviousPregnancyComplication(String previousPregnancyComplication) {
        this.previousPregnancyComplication = previousPregnancyComplication;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Long getNpcbScreeningDate() {
        return npcbScreeningDate;
    }

    public void setNpcbScreeningDate(Long npcbScreeningDate) {
        this.npcbScreeningDate = npcbScreeningDate;
    }

    public Boolean getFhsrPhoneVerified() {
        return fhsrPhoneVerified;
    }

    public void setFhsrPhoneVerified(Boolean fhsrPhoneVerified) {
        this.fhsrPhoneVerified = fhsrPhoneVerified;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Date getLmp() {
        return lmp;
    }

    public void setLmp(Date lmp) {
        this.lmp = lmp;
    }

    public Short getMenstruationDays() {
        return menstruationDays;
    }

    public void setMenstruationDays(Short menstruationDays) {
        this.menstruationDays = menstruationDays;
    }

    public Integer getMytechoUserId() {
        return mytechoUserId;
    }

    public void setMytechoUserId(Integer mytechoUserId) {
        this.mytechoUserId = mytechoUserId;
    }

    public Short getCurrentGravida() {
        return currentGravida;
    }

    public void setCurrentGravida(Short currentGravida) {
        this.currentGravida = currentGravida;
    }

    public Short getCurrentPara() {
        return currentPara;
    }

    public void setCurrentPara(Short currentPara) {
        this.currentPara = currentPara;
    }

    public Long getDateOfWedding() {
        return dateOfWedding;
    }

    public void setDateOfWedding(Long dateOfWedding) {
        this.dateOfWedding = dateOfWedding;
    }

    public Boolean getIsMobileNumberVerified() {
        return isMobileNumberVerified;
    }

    public void setIsMobileNumberVerified(Boolean isMobileNumberVerified) {
        this.isMobileNumberVerified = isMobileNumberVerified;
    }

    public Integer getHusbandId() {
        return husbandId;
    }

    public void setHusbandId(Integer husbandId) {
        this.husbandId = husbandId;
    }

    public String getRelationWithHof() {
        return relationWithHof;
    }

    public void setRelationWithHof(String relationWithHof) {
        this.relationWithHof = relationWithHof;
    }

    public Long getCbacDate() {
        return cbacDate;
    }

    public void setCbacDate(Long cbacDate) {
        this.cbacDate = cbacDate;
    }

    public String getHypYear() {
        return hypYear;
    }

    public void setHypYear(String hypYear) {
        this.hypYear = hypYear;
    }

    public String getOralYear() {
        return oralYear;
    }

    public void setOralYear(String oralYear) {
        this.oralYear = oralYear;
    }

    public String getDiabetesYear() {
        return diabetesYear;
    }

    public void setDiabetesYear(String diabetesYear) {
        this.diabetesYear = diabetesYear;
    }

    public String getBreastYear() {
        return breastYear;
    }

    public void setBreastYear(String breastYear) {
        this.breastYear = breastYear;
    }

    public String getCervicalYear() {
        return cervicalYear;
    }

    public void setCervicalYear(String cervicalYear) {
        this.cervicalYear = cervicalYear;
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

    public Boolean getVulnerableFlag() {
        return vulnerableFlag;
    }

    public void setVulnerableFlag(Boolean vulnerableFlag) {
        this.vulnerableFlag = vulnerableFlag;
    }

    public String getMentalHealthYear() {
        return mentalHealthYear;
    }

    public void setMentalHealthYear(String mentalHealthYear) {
        this.mentalHealthYear = mentalHealthYear;
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

    public String getHealthYear() {
        return healthYear;
    }

    public void setHealthYear(String healthYear) {
        this.healthYear = healthYear;
    }

    public Boolean getPersonalHistoryDone() {
        return personalHistoryDone;
    }

    public void setPersonalHistoryDone(Boolean personalHistoryDone) {
        this.personalHistoryDone = personalHistoryDone;
    }

    public Boolean getConfirmedDiabetes() {
        return confirmedDiabetes;
    }

    public void setConfirmedDiabetes(Boolean confirmedDiabetes) {
        this.confirmedDiabetes = confirmedDiabetes;
    }

    public Boolean getSuspectedForDiabetes() {
        return suspectedForDiabetes;
    }

    public void setSuspectedForDiabetes(Boolean suspectedForDiabetes) {
        this.suspectedForDiabetes = suspectedForDiabetes;
    }

    public Integer getCbacScore() {
        return cbacScore;
    }

    public void setCbacScore(Integer cbacScore) {
        this.cbacScore = cbacScore;
    }

    public Boolean getSufferingDiabetes() {
        return sufferingDiabetes;
    }

    public void setSufferingDiabetes(Boolean sufferingDiabetes) {
        this.sufferingDiabetes = sufferingDiabetes;
    }

    public Boolean getSufferingHypertension() {
        return sufferingHypertension;
    }

    public void setSufferingHypertension(Boolean sufferingHypertension) {
        this.sufferingHypertension = sufferingHypertension;
    }

    public Boolean getSufferingMentalHealth() {
        return sufferingMentalHealth;
    }

    public void setSufferingMentalHealth(Boolean sufferingMentalHealth) {
        this.sufferingMentalHealth = sufferingMentalHealth;
    }

    public String getPmjayAvailability() {
        return pmjayAvailability;
    }

    public void setPmjayAvailability(String pmjayAvailability) {
        this.pmjayAvailability = pmjayAvailability;
    }

    public String getAlcoholAddiction() {
        return alcoholAddiction;
    }

    public void setAlcoholAddiction(String alcoholAddiction) {
        this.alcoholAddiction = alcoholAddiction;
    }

    public String getSmokingAddiction() {
        return smokingAddiction;
    }

    public void setSmokingAddiction(String smokingAddiction) {
        this.smokingAddiction = smokingAddiction;
    }

    public String getTobaccoAddiction() {
        return tobaccoAddiction;
    }

    public void setTobaccoAddiction(String tobaccoAddiction) {
        this.tobaccoAddiction = tobaccoAddiction;
    }
}
