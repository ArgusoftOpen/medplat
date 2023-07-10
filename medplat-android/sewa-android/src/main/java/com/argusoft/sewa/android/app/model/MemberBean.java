/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.model;

import androidx.annotation.NonNull;

import com.argusoft.sewa.android.app.databean.MemberAdditionalInfoDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * @author kunjan
 */
@DatabaseTable
public class MemberBean extends BaseEntity implements Serializable {

    @DatabaseField(index = true)
    private String actualId;

    @DatabaseField(index = true)
    private String familyId;

    @DatabaseField
    private String firstName;

    @DatabaseField
    private String middleName;

    @DatabaseField
    private String lastName;

    @DatabaseField
    private String gender;

    @DatabaseField
    private String maritalStatus;

    @DatabaseField
    private String aadharNumber;

    @DatabaseField
    private boolean isAadharUpdated;

    @DatabaseField
    private String mobileNumber;

    @DatabaseField(index = true)
    private Boolean familyHeadFlag;

    @DatabaseField
    private Date dob;

    @DatabaseField(index = true)
    private String uniqueHealthId;

    @DatabaseField
    private String ifsc;

    @DatabaseField
    private String accountNumber;

    @DatabaseField
    private String maaVatsalyaCardNumber;

    @DatabaseField
    private Boolean isPregnantFlag;

    @DatabaseField
    private Date lmpDate;

    @DatabaseField
    private Integer normalCycleDays;

    @DatabaseField
    private String familyPlanningMethod;

    @DatabaseField
    private Date fpInsertOperateDate;

    @DatabaseField
    private String type;

    @DatabaseField
    private String state;

    @DatabaseField
    private String createdBy;

    @DatabaseField
    private String updatedBy;

    @DatabaseField
    private Date createdOn;

    @DatabaseField
    private Date updatedOn;

    @DatabaseField
    private String grandfatherName;

    @DatabaseField
    private String nameBasedOnAadhar;

    @DatabaseField
    private String educationStatus;

    @DatabaseField//Comma Separated IDs
    private String congenitalAnomalyIds;

    @DatabaseField//Comma Separated IDs
    private String chronicDiseaseIds;

    @DatabaseField//Comma Separated IDs
    private String currentDiseaseIds;

    @DatabaseField//Comma Separated IDs
    private String eyeIssueIds;

    @DatabaseField
    private String lastMethodOfContraception;

    @DatabaseField
    private Integer yearOfWedding;

    @DatabaseField
    private Boolean jsyPaymentGiven;

    @DatabaseField
    private Boolean isEarlyRegistration;

    @DatabaseField
    private Boolean jsyBeneficiary;

    @DatabaseField
    private Boolean iayBeneficiary;

    @DatabaseField
    private Boolean kpsyBeneficiary;

    @DatabaseField
    private Boolean chiranjeeviYojnaBeneficiary;

    @DatabaseField
    private Float haemoglobin;

    @DatabaseField
    private Float weight;

    @DatabaseField
    private Date edd;

    @DatabaseField
    private String ancVisitDates;

    @DatabaseField
    private String immunisationGiven;

    @DatabaseField
    private String bloodGroup;

    @DatabaseField
    private String placeOfBirth;

    @DatabaseField
    private Float birthWeight;

    @DatabaseField
    private Long motherId;

    @DatabaseField
    private Boolean complementaryFeedingStarted;

    @DatabaseField
    private Boolean isHighRiskCase;

    @DatabaseField
    private Long curPregRegDetId;

    @DatabaseField
    private Date curPregRegDate;

    @DatabaseField
    private Boolean menopauseArrived;

    @DatabaseField
    private String syncStatus;

    @DatabaseField
    private Boolean isIucdRemoved;

    @DatabaseField
    private Date iucdRemovalDate;

    @DatabaseField
    private Date lastDeliveryDate;

    @DatabaseField
    private Boolean hysterectomyDone;

    @DatabaseField
    private String childNrcCmtcStatus;

    @DatabaseField
    private String lastDeliveryOutcome;

    //Comma Separated
    @DatabaseField
    private String previousPregnancyComplication;

    @DatabaseField
    private String additionalInfo;

    @DatabaseField
    private Date npcbScreeningDate;

    @DatabaseField
    private Boolean fhsrPhoneVerified;

    @DatabaseField
    private Short currentGravida;

    @DatabaseField
    private Short currentPara;

    @DatabaseField
    private Date dateOfWedding;

    @DatabaseField
    private Long husbandId;

    @DatabaseField
    private String npcbStatus;

    @DatabaseField
    private Boolean isMobileNumberVerified;

    @DatabaseField
    private String relationWithHof;

    @DatabaseField
    private Date cbacDate;

    @DatabaseField
    private String hypYear;         //Comma Separated financial year in which HYPERTENSION Screening is done

    @DatabaseField
    private String oralYear;        //Comma Separated financial year in which ORAL Screening is done

    @DatabaseField
    private String diabetesYear;    //Comma Separated financial year in which DIABETES Screening is done

    @DatabaseField
    private String breastYear;      //Comma Separated financial year in which BREAST Screening is done

    @DatabaseField
    private String cervicalYear;    //Comma Separated financial year in which CERVICAL Screening is done

    @DatabaseField
    private String mentalHealthYear;    //Comma Separated financial year in which Mental health Screening is done

    @DatabaseField
    private String healthYear;    //Comma Separated financial year in which health Screening is done

    @DatabaseField
    private String healthId;

    @DatabaseField
    private String healthIdNumber;

    @DatabaseField
    private Boolean vulnerableFlag;

    @DatabaseField
    private Boolean healthInsurance;

    @DatabaseField
    private String schemeDetail;

    @DatabaseField
    private Boolean isPersonalHistoryDone;

    @DatabaseField
    private Boolean confirmedDiabetes;

    @DatabaseField
    private Boolean suspectedForDiabetes;

    @DatabaseField
    private Integer cbacScore;

    @DatabaseField
    private Boolean sufferingDiabetes;

    @DatabaseField
    private Boolean sufferingHypertension;

    @DatabaseField
    private Boolean sufferingMentalHealth;

    @DatabaseField
    private String pmjayAvailability;

    @DatabaseField
    private String alcoholAddiction;

    @DatabaseField
    private String smokingAddiction;

    @DatabaseField
    private String tobaccoAddiction;

    public MemberBean() {
    }

    public MemberBean(MemberDataBean memberDataBean) {
        this.actualId = memberDataBean.getId();
        this.familyId = memberDataBean.getFamilyId();
        this.firstName = memberDataBean.getFirstName();
        this.middleName = memberDataBean.getMiddleName();
        this.lastName = memberDataBean.getLastName();
        this.gender = memberDataBean.getGender();
        this.maritalStatus = memberDataBean.getMaritalStatus();
        this.aadharNumber = memberDataBean.getAadharNumber();
        this.isAadharUpdated = memberDataBean.isAadharUpdated();
        this.mobileNumber = memberDataBean.getMobileNumber();
        this.familyHeadFlag = memberDataBean.getFamilyHeadFlag();
        this.uniqueHealthId = memberDataBean.getUniqueHealthId();
        this.ifsc = memberDataBean.getIfsc();
        this.accountNumber = memberDataBean.getAccountNumber();
        this.maaVatsalyaCardNumber = memberDataBean.getMaaVatsalyaCardNumber();
        this.isPregnantFlag = memberDataBean.getIsPregnantFlag();
        this.normalCycleDays = memberDataBean.getNormalCycleDays();
        this.familyPlanningMethod = memberDataBean.getFamilyPlanningMethod();
        this.type = memberDataBean.getType();
        this.state = memberDataBean.getState();
        this.createdBy = memberDataBean.getCreatedBy();
        this.updatedBy = memberDataBean.getUpdatedBy();
        this.grandfatherName = memberDataBean.getGrandfatherName();
        this.nameBasedOnAadhar = memberDataBean.getNameBasedOnAadhar();
        this.educationStatus = memberDataBean.getEducationStatus();
        this.congenitalAnomalyIds = memberDataBean.getCongenitalAnomalyIds();
        this.eyeIssueIds = memberDataBean.getEyeIssueIds();
        this.chronicDiseaseIds = memberDataBean.getChronicDiseaseIds();
        this.currentDiseaseIds = memberDataBean.getCurrentDiseaseIds();
        this.lastMethodOfContraception = memberDataBean.getLastMethodOfContraception();
        this.yearOfWedding = memberDataBean.getYearOfWedding();
        this.jsyPaymentGiven = memberDataBean.getJsyPaymentGiven();
        this.isEarlyRegistration = memberDataBean.getEarlyRegistration();
        this.jsyBeneficiary = memberDataBean.getJsyBeneficiary();
        this.iayBeneficiary = memberDataBean.getIayBeneficiary();
        this.kpsyBeneficiary = memberDataBean.getKpsyBeneficiary();
        this.chiranjeeviYojnaBeneficiary = memberDataBean.getChiranjeeviYojnaBeneficiary();
        this.haemoglobin = memberDataBean.getHaemoglobin();
        this.weight = memberDataBean.getWeight();
        this.ancVisitDates = memberDataBean.getAncVisitDates();
        this.immunisationGiven = memberDataBean.getImmunisationGiven();
        this.bloodGroup = memberDataBean.getBloodGroup();
        this.birthWeight = memberDataBean.getBirthWeight();
        this.placeOfBirth = memberDataBean.getPlaceOfBirth();
        this.motherId = memberDataBean.getMotherId();
        this.complementaryFeedingStarted = memberDataBean.getComplementaryFeedingStarted();
        this.isHighRiskCase = memberDataBean.getHighRiskCase();
        this.curPregRegDetId = memberDataBean.getCurPregRegDetId();
        this.menopauseArrived = memberDataBean.getMenopauseArrived();
        this.syncStatus = memberDataBean.getSyncStatus();
        this.isIucdRemoved = memberDataBean.getIucdRemoved();
        this.hysterectomyDone = memberDataBean.getHysterectomyDone();
        this.childNrcCmtcStatus = memberDataBean.getChildNrcCmtcStatus();
        this.lastDeliveryOutcome = memberDataBean.getLastDeliveryOutcome();
        this.previousPregnancyComplication = memberDataBean.getPreviousPregnancyComplication();
        this.additionalInfo = memberDataBean.getAdditionalInfo();
        this.fhsrPhoneVerified = memberDataBean.getFhsrPhoneVerified();
        this.currentGravida = memberDataBean.getCurrentGravida();
        this.currentPara = memberDataBean.getCurrentPara();
        this.husbandId = memberDataBean.getHusbandId();
        this.isMobileNumberVerified = memberDataBean.getMobileNumberVerified();
        this.relationWithHof = memberDataBean.getRelationWithHof();
        this.hypYear = memberDataBean.getHypYear();
        this.diabetesYear = memberDataBean.getDiabetesYear();
        this.oralYear = memberDataBean.getOralYear();
        this.breastYear = memberDataBean.getBreastYear();
        this.cervicalYear = memberDataBean.getCervicalYear();
        this.mentalHealthYear = memberDataBean.getMentalHealthYear();
        this.healthId = memberDataBean.getHealthId();
        this.healthIdNumber = memberDataBean.getHealthIdNumber();
        this.vulnerableFlag = memberDataBean.getVulnerableFlag();
        this.healthYear = memberDataBean.getHealthYear();
        this.confirmedDiabetes = memberDataBean.getConfirmedDiabetes();
        this.healthInsurance = memberDataBean.getHealthInsurance();
        this.schemeDetail = memberDataBean.getSchemeDetail();
        this.suspectedForDiabetes = memberDataBean.getSuspectedForDiabetes();
        this.isPersonalHistoryDone = memberDataBean.getPersonalHistoryDone();
        this.cbacScore = memberDataBean.getCbacScore();
        this.sufferingDiabetes = memberDataBean.getSufferingDiabetes();
        this.sufferingHypertension = memberDataBean.getSufferingHypertension();
        this.sufferingMentalHealth = memberDataBean.getSufferingMentalHealth();
        this.pmjayAvailability = memberDataBean.getPmjayAvailability();
        this.alcoholAddiction = memberDataBean.getAlcoholAddiction();
        this.smokingAddiction = memberDataBean.getSmokingAddiction();
        this.tobaccoAddiction = memberDataBean.getTobaccoAddiction();

        if (memberDataBean.getAdditionalInfo() != null) {
            this.npcbStatus = new Gson().fromJson(memberDataBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class).getNpcbStatus();
        }

        if (memberDataBean.getDateOfWedding() != null) {
            this.dateOfWedding = new Date(memberDataBean.getDateOfWedding());
        }

        if (memberDataBean.getLmpDate() != null) {
            this.lmpDate = new Date(memberDataBean.getLmpDate());
        }

        if (memberDataBean.getDob() != null) {
            this.dob = new Date(memberDataBean.getDob());
        }

        if (memberDataBean.getCreatedOn() != null) {
            this.createdOn = new Date(memberDataBean.getCreatedOn());
        }

        if (memberDataBean.getUpdatedOn() != null) {
            this.updatedOn = new Date(memberDataBean.getUpdatedOn());
        }

        if (memberDataBean.getEdd() != null) {
            this.edd = new Date(memberDataBean.getEdd());
        }

        if (memberDataBean.getFpInsertOperateDate() != null) {
            this.fpInsertOperateDate = new Date(memberDataBean.getFpInsertOperateDate());
        }

        if (memberDataBean.getIucdRemovalDate() != null) {
            this.iucdRemovalDate = new Date(memberDataBean.getIucdRemovalDate());
        }

        if (memberDataBean.getCurPregRegDate() != null) {
            this.curPregRegDate = new Date(memberDataBean.getCurPregRegDate());
        }

        if (memberDataBean.getLastDeliveryDate() != null) {
            this.lastDeliveryDate = new Date(memberDataBean.getLastDeliveryDate());
        }

        if (memberDataBean.getNpcbScreeningDate() != null) {
            this.npcbScreeningDate = new Date(memberDataBean.getNpcbScreeningDate());
        }

        if (memberDataBean.getCbacDate() != null) {
            this.cbacDate = new Date(memberDataBean.getCbacDate());
        }
    }

    public Date getDateOfWedding() {
        return dateOfWedding;
    }

    public void setDateOfWedding(Date dateOfWedding) {
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

    public String getActualId() {
        return actualId;
    }

    public void setActualId(String actualId) {
        this.actualId = actualId;
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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
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

    public Date getLmpDate() {
        return lmpDate;
    }

    public void setLmpDate(Date lmpDate) {
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

    public Date getFpInsertOperateDate() {
        return fpInsertOperateDate;
    }

    public void setFpInsertOperateDate(Date fpInsertOperateDate) {
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

    public Date getEdd() {
        return edd;
    }

    public void setEdd(Date edd) {
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

    public Date getCurPregRegDate() {
        return curPregRegDate;
    }

    public void setCurPregRegDate(Date curPregRegDate) {
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

    public Date getIucdRemovalDate() {
        return iucdRemovalDate;
    }

    public void setIucdRemovalDate(Date iucdRemovalDate) {
        this.iucdRemovalDate = iucdRemovalDate;
    }

    public Date getLastDeliveryDate() {
        return lastDeliveryDate;
    }

    public void setLastDeliveryDate(Date lastDeliveryDate) {
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

    public Date getNpcbScreeningDate() {
        return npcbScreeningDate;
    }

    public void setNpcbScreeningDate(Date npcbScreeningDate) {
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

    public Date getCbacDate() {
        return cbacDate;
    }

    public void setCbacDate(Date cbacDate) {
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
        return isPersonalHistoryDone;
    }

    public void setPersonalHistoryDone(Boolean personalHistoryDone) {
        isPersonalHistoryDone = personalHistoryDone;
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

    @NonNull
    @Override
    public String toString() {
        return "MemberBean{" + "actualId=" + actualId + ", familyId=" + familyId + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName + ", gender=" + gender + ", maritalStatus=" + maritalStatus + ", aadharNumber=" + aadharNumber + ", mobileNumber=" + mobileNumber + ", familyHeadFlag=" + familyHeadFlag + ", dob=" + dob + ", uniqueHealthId=" + uniqueHealthId + ", ifsc=" + ifsc + ", accountNumber=" + accountNumber + ", maaVatsalyaCardNumber=" + maaVatsalyaCardNumber + ", isPregnantFlag=" + isPregnantFlag + ", lmpDate=" + lmpDate + ", normalCycleDays=" + normalCycleDays + ", familyPlanningMethod=" + familyPlanningMethod + ", type=" + type + ", state=" + state + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", createdOn=" + createdOn + ", updatedOn=" + updatedOn + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof MemberBean)) return false;
        MemberBean that = (MemberBean) o;
        return this.actualId.equals(that.actualId);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
