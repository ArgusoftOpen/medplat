package com.argusoft.sewa.android.app.databean;

import com.argusoft.sewa.android.app.model.MemberBean;
import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;

/**
 * @author kunjan
 */
public class MemberDataBean {

    private String id;

    private String familyId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String gender;

    private String maritalStatus;

    private String aadharNumber;

    private boolean isAadharUpdated;

    private String mobileNumber;

    private Boolean familyHeadFlag;

    private Long dob;

    private String uniqueHealthId;

    private String ifsc;

    private String accountNumber;

    private String maaVatsalyaCardNumber;

    private Boolean isPregnantFlag;

    private Long lmpDate;

    private Integer normalCycleDays;

    private String familyPlanningMethod;

    private Long fpInsertOperateDate;

    private String type;

    private String state;

    private String createdBy;

    private String updatedBy;

    private Long createdOn;

    private Long updatedOn;

    private String grandfatherName;

    private String nameBasedOnAadhar;

    private String educationStatus;
    //Comma Separated IDs
    private String congenitalAnomalyIds;

    //Comma Separated IDs
    private String chronicDiseaseIds;

    //Comma Separated IDs
    private String currentDiseaseIds;

    //Comma Separated IDs
    private String eyeIssueIds;

    private String lastMethodOfContraception;

    private Integer yearOfWedding;

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

    private Long motherId;

    private Boolean complementaryFeedingStarted;

    private Boolean isHighRiskCase;

    private Long curPregRegDetId;

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

    private Short currentGravida;

    private Short currentPara;

    private Long dateOfWedding;

    private Long husbandId;

    private String npcbStatus;

    private Boolean isMobileNumberVerified;

    private String relationWithHof;

    private Long cbacDate;

    private String hypYear;         //Comma Separated financial year in which HYPERTENSION Screening is done

    private String oralYear;        //Comma Separated financial year in which ORAL Screening is done

    private String diabetesYear;    //Comma Separated financial year in which DIABETES Screening is done

    private String breastYear;      //Comma Separated financial year in which BREAST Screening is done

    private String cervicalYear;    //Comma Separated financial year in which CERVICAL Screening is done

    private String mentalHealthYear;    //Comma Separated financial year in which CERVICAL Screening is done

    private String healthYear;      //Comma Separated financial year in which health Screening is done

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

    private Boolean eveningAvailability;

    private String pmjayAvailability;

    private String alcoholAddiction;

    private String smokingAddiction;

    private String tobaccoAddiction;

    public MemberDataBean() {
    }

    public MemberDataBean(MemberBean memberBean) {
        this.id = memberBean.getActualId();
        this.familyId = memberBean.getFamilyId();
        this.firstName = memberBean.getFirstName();
        this.middleName = memberBean.getMiddleName();
        this.lastName = memberBean.getLastName();
        this.gender = memberBean.getGender();
        this.maritalStatus = memberBean.getMaritalStatus();
        this.aadharNumber = memberBean.getAadharNumber();
        this.isAadharUpdated = memberBean.isAadharUpdated();
        this.mobileNumber = memberBean.getMobileNumber();
        this.familyHeadFlag = memberBean.getFamilyHeadFlag();
        this.uniqueHealthId = memberBean.getUniqueHealthId();
        this.ifsc = memberBean.getIfsc();
        this.accountNumber = memberBean.getAccountNumber();
        this.maaVatsalyaCardNumber = memberBean.getMaaVatsalyaCardNumber();
        this.isPregnantFlag = memberBean.getIsPregnantFlag();
        this.normalCycleDays = memberBean.getNormalCycleDays();
        this.familyPlanningMethod = memberBean.getFamilyPlanningMethod();
        this.type = memberBean.getType();
        this.state = memberBean.getState();
        this.createdBy = memberBean.getCreatedBy();
        this.updatedBy = memberBean.getUpdatedBy();
        this.grandfatherName = memberBean.getGrandfatherName();
        this.nameBasedOnAadhar = memberBean.getNameBasedOnAadhar();
        this.educationStatus = memberBean.getEducationStatus();
        this.congenitalAnomalyIds = memberBean.getCongenitalAnomalyIds();
        this.eyeIssueIds = memberBean.getEyeIssueIds();
        this.chronicDiseaseIds = memberBean.getChronicDiseaseIds();
        this.currentDiseaseIds = memberBean.getCurrentDiseaseIds();
        this.lastMethodOfContraception = memberBean.getLastMethodOfContraception();
        this.yearOfWedding = memberBean.getYearOfWedding();
        this.jsyPaymentGiven = memberBean.getJsyPaymentGiven();
        this.isEarlyRegistration = memberBean.getEarlyRegistration();
        this.jsyBeneficiary = memberBean.getJsyBeneficiary();
        this.iayBeneficiary = memberBean.getIayBeneficiary();
        this.kpsyBeneficiary = memberBean.getKpsyBeneficiary();
        this.chiranjeeviYojnaBeneficiary = memberBean.getChiranjeeviYojnaBeneficiary();
        this.haemoglobin = memberBean.getHaemoglobin();
        this.weight = memberBean.getWeight();
        this.ancVisitDates = memberBean.getAncVisitDates();
        this.immunisationGiven = memberBean.getImmunisationGiven();
        this.bloodGroup = memberBean.getBloodGroup();
        this.motherId = memberBean.getMotherId();
        this.complementaryFeedingStarted = memberBean.getComplementaryFeedingStarted();
        this.isHighRiskCase = memberBean.getHighRiskCase();
        this.curPregRegDetId = memberBean.getCurPregRegDetId();
        this.menopauseArrived = memberBean.getMenopauseArrived();
        this.syncStatus = memberBean.getSyncStatus();
        this.isIucdRemoved = memberBean.getIucdRemoved();
        this.hysterectomyDone = memberBean.getHysterectomyDone();
        this.childNrcCmtcStatus = memberBean.getChildNrcCmtcStatus();
        this.lastDeliveryOutcome = memberBean.getLastDeliveryOutcome();
        this.previousPregnancyComplication = memberBean.getPreviousPregnancyComplication();
        this.additionalInfo = memberBean.getAdditionalInfo();
        this.fhsrPhoneVerified = memberBean.getFhsrPhoneVerified();
        this.currentGravida = memberBean.getCurrentGravida();
        this.currentPara = memberBean.getCurrentPara();
        this.husbandId = memberBean.getHusbandId();
        this.isMobileNumberVerified = memberBean.getMobileNumberVerified();
        this.relationWithHof = memberBean.getRelationWithHof();
        this.birthWeight = memberBean.getBirthWeight();
        this.placeOfBirth = memberBean.getPlaceOfBirth();
        this.hypYear = memberBean.getHypYear();
        this.diabetesYear = memberBean.getDiabetesYear();
        this.oralYear = memberBean.getOralYear();
        this.breastYear = memberBean.getBreastYear();
        this.cervicalYear = memberBean.getCervicalYear();
        this.mentalHealthYear = memberBean.getMentalHealthYear();
        this.healthId = memberBean.getHealthId();
        this.healthIdNumber = memberBean.getHealthIdNumber();
        this.vulnerableFlag = memberBean.getVulnerableFlag();
        this.healthYear = memberBean.getHealthYear();
        this.confirmedDiabetes = memberBean.getConfirmedDiabetes();
        this.healthInsurance = memberBean.getHealthInsurance();
        this.schemeDetail = memberBean.getSchemeDetail();
        this.suspectedForDiabetes = memberBean.getSuspectedForDiabetes();
        this.personalHistoryDone = memberBean.getPersonalHistoryDone();
        this.cbacScore = memberBean.getCbacScore();
        this.sufferingDiabetes = memberBean.getSufferingDiabetes();
        this.sufferingHypertension = memberBean.getSufferingHypertension();
        this.sufferingMentalHealth = memberBean.getSufferingMentalHealth();
        this.pmjayAvailability = memberBean.getPmjayAvailability();
        this.alcoholAddiction = memberBean.getAlcoholAddiction();
        this.smokingAddiction = memberBean.getSmokingAddiction();
        this.tobaccoAddiction = memberBean.getTobaccoAddiction();

        if (memberBean.getAdditionalInfo() != null) {
            this.npcbStatus = new Gson().fromJson(memberBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class).getNpcbStatus();
        }

        if (memberBean.getLmpDate() != null) {
            this.lmpDate = memberBean.getLmpDate().getTime();
        }

        if (memberBean.getDob() != null) {
            this.dob = memberBean.getDob().getTime();
        }

        if (memberBean.getCreatedOn() != null) {
            this.createdOn = memberBean.getCreatedOn().getTime();
        }

        if (memberBean.getUpdatedOn() != null) {
            this.updatedOn = memberBean.getUpdatedOn().getTime();
        }

        if (memberBean.getEdd() != null) {
            this.edd = memberBean.getEdd().getTime();
        }

        if (memberBean.getFpInsertOperateDate() != null) {
            this.fpInsertOperateDate = memberBean.getFpInsertOperateDate().getTime();
        }

        if (memberBean.getIucdRemovalDate() != null) {
            this.iucdRemovalDate = memberBean.getIucdRemovalDate().getTime();
        }

        if (memberBean.getCurPregRegDate() != null) {
            this.curPregRegDate = memberBean.getCurPregRegDate().getTime();
        }

        if (memberBean.getLastDeliveryDate() != null) {
            this.lastDeliveryDate = memberBean.getLastDeliveryDate().getTime();
        }

        if (memberBean.getNpcbScreeningDate() != null) {
            this.npcbScreeningDate = memberBean.getNpcbScreeningDate().getTime();
        }

        if (memberBean.getDateOfWedding() != null) {
            this.dateOfWedding = memberBean.getDateOfWedding().getTime();
        }

        if (memberBean.getCbacDate() != null) {
            this.cbacDate = memberBean.getCbacDate().getTime();
        }
    }

    public Long getDateOfWedding() {
        return dateOfWedding;
    }

    public void setDateOfWedding(Long dateOfWedding) {
        this.dateOfWedding = dateOfWedding;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public boolean isAadharUpdated() {
        return isAadharUpdated;
    }

    public void setAadharUpdated(boolean aadharUpdated) {
        isAadharUpdated = aadharUpdated;
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

    public Integer getNormalCycleDays() {
        return normalCycleDays;
    }

    public void setNormalCycleDays(Integer normalCycleDays) {
        this.normalCycleDays = normalCycleDays;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Long updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getGrandfatherName() {
        return grandfatherName;
    }

    public void setGrandfatherName(String grandfatherName) {
        this.grandfatherName = grandfatherName;
    }

    public String getNameBasedOnAadhar() {
        return nameBasedOnAadhar;
    }

    public void setNameBasedOnAadhar(String nameBasedOnAadhar) {
        this.nameBasedOnAadhar = nameBasedOnAadhar;
    }

    public String getEducationStatus() {
        return educationStatus;
    }

    public void setEducationStatus(String educationStatus) {
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

    public String getLastMethodOfContraception() {
        return lastMethodOfContraception;
    }

    public void setLastMethodOfContraception(String lastMethodOfContraception) {
        this.lastMethodOfContraception = lastMethodOfContraception;
    }

    public Integer getYearOfWedding() {
        return yearOfWedding;
    }

    public void setYearOfWedding(Integer yearOfWedding) {
        this.yearOfWedding = yearOfWedding;
    }

    public Boolean getJsyPaymentGiven() {
        return jsyPaymentGiven;
    }

    public void setJsyPaymentGiven(Boolean jsyPaymentGiven) {
        this.jsyPaymentGiven = jsyPaymentGiven;
    }

    public Boolean getEarlyRegistration() {
        return isEarlyRegistration;
    }

    public void setEarlyRegistration(Boolean earlyRegistration) {
        isEarlyRegistration = earlyRegistration;
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

    public Long getMotherId() {
        return motherId;
    }

    public void setMotherId(Long motherId) {
        this.motherId = motherId;
    }

    public Boolean getComplementaryFeedingStarted() {
        return complementaryFeedingStarted;
    }

    public void setComplementaryFeedingStarted(Boolean complementaryFeedingStarted) {
        this.complementaryFeedingStarted = complementaryFeedingStarted;
    }

    public Boolean getHighRiskCase() {
        return isHighRiskCase;
    }

    public void setHighRiskCase(Boolean highRiskCase) {
        isHighRiskCase = highRiskCase;
    }

    public Long getCurPregRegDetId() {
        return curPregRegDetId;
    }

    public void setCurPregRegDetId(Long curPregRegDetId) {
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

    public Boolean getIucdRemoved() {
        return isIucdRemoved;
    }

    public void setIucdRemoved(Boolean iucdRemoved) {
        isIucdRemoved = iucdRemoved;
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
        if (additionalInfo != null) {
            this.npcbStatus = new Gson().fromJson(additionalInfo, MemberAdditionalInfoDataBean.class).getNpcbStatus();
        }
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

    public Long getHusbandId() {
        return husbandId;
    }

    public void setHusbandId(Long husbandId) {
        this.husbandId = husbandId;
    }

    public String getNpcbStatus() {
        return npcbStatus;
    }

    public void setNpcbStatus(String npcbStatus) {
        this.npcbStatus = npcbStatus;
    }

    public Boolean getMobileNumberVerified() {
        return isMobileNumberVerified;
    }

    public void setMobileNumberVerified(Boolean mobileNumberVerified) {
        isMobileNumberVerified = mobileNumberVerified;
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

    public String getMentalHealthYear() {
        return mentalHealthYear;
    }

    public void setMentalHealthYear(String mentalHealthYear) {
        this.mentalHealthYear = mentalHealthYear;
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

    public Boolean getEveningAvailability() {
        return eveningAvailability;
    }

    public void setEveningAvailability(Boolean eveningAvailability) {
        this.eveningAvailability = eveningAvailability;
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
