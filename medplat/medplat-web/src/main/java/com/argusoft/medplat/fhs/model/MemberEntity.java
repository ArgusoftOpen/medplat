/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fhs.model;

import com.argusoft.medplat.cfhc.model.MemberCFHCEntity;
import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.common.util.AESEncryption;
import com.argusoft.medplat.mobile.mapper.MemberDataBeanMapper;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * <p>
 * Define imt_member entity and its fields.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
@javax.persistence.Entity
@Table(name = "imt_member")
public class MemberEntity extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "family_id")
    private String familyId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "husband_name")
    private String husbandName;
    @Column(name = "husband_id")
    private Integer husbandId;
    @Column(name = "gender")
    private String gender;
    @Column(name = "marital_status")
    private Integer maritalStatus;
    @Column(name = "aadhar_number_encrypted")
    private String aadharNumber;
    @Column(name = "mobile_number")
    private String mobileNumber;
    @Column(name = "family_head")
    private Boolean familyHeadFlag;
    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Column(name = "unique_health_id")
    private String uniqueHealthId;
    @Column(name = "ifsc")
    private String ifsc;
    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "is_pregnant")
    private Boolean isPregnantFlag;
    @Column(name = "lmp")
    @Temporal(TemporalType.DATE)
    private Date lmpDate;
    @Column(name = "normal_cycle_days")
    private Short normalCycleDays;
    @Column(name = "family_planning_method")
    private String familyPlanningMethod;
    @Column(name = "fp_insert_operate_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fpInsertOperateDate;
    @Column(name = "state")
    private String state;
    @Column(name = "grandfather_name")
    private String grandfatherName;
    @Column(name = "emamta_health_id")
    private String emamtaHealthId;
    @Column(name = "is_aadhar_verified")
    private Boolean isAadharVerified;
    @Column(name = "is_mobile_verified")
    private Boolean isMobileNumberVerified;
    @Column(name = "is_native")
    private Boolean isNativeFlag;
    @Column(name = "education_status")
    private Integer educationStatus;
    @Column(name = "is_report")
    private Boolean isReport;
    @Column(name = "name_as_per_aadhar")
    private String nameAsPerAadhar;
    @Column(name = "aadhaar_reference_key")
    @Type(type = "uuid-char")
    private UUID aadhaarReferenceKey;
//    @Column(name = "occupation")
//    private String occupation;
//    @Column(name = "under_treatment_chronic")
//    private Boolean underTreatmentChronic;
//    @Column(name = "other_chronic")
//    private String otherChronic;
//    @Column(name = "other_eye_issue")
//    private String otherEyeIssue;

    private transient Set<Integer> chronicDiseaseDetails;
    private transient Set<Integer> congenitalAnomalyDetails;
    private transient Set<Integer> currentDiseaseDetails;
    private transient Set<Integer> eyeIssueDetails;

    @Column(name = "merged_from_family_id")
    private String mergedFromFamilyId;
    @Column(name = "agreed_to_share_aadhar")
    private Boolean agreedToShareAadhar;
//    @Column(name = "aadhar_status")
//    private String aadharStatus;
    @Column(name = "aadhar_number_available")
    private Boolean aadharNumberAvailable;
    @Column(name = "year_of_wedding")
    private Short yearOfWedding;
    @Column(name = "date_of_wedding")
    @Temporal(TemporalType.DATE)
    private Date dateOfWedding;
    @Column(name = "jsy_payment_given")
    private Boolean jsyPaymentGiven;
    @Column(name = "early_registration")
    private Boolean isEarlyRegistration;
    @Column(name = "mother_id")
    private Integer motherId;
    @Column(name = "jsy_beneficiary")
    private Boolean jsyBeneficiary;
    @Column(name = "iay_beneficiary")
    private Boolean iayBeneficiary;
    @Column(name = "kpsy_beneficiary")
    private Boolean kpsyBeneficiary;
    @Column(name = "chiranjeevi_yojna_beneficiary")
    private Boolean chiranjeeviYojnaBeneficiary;
    @Column(name = "haemoglobin", columnDefinition = "numeric", precision = 6, scale = 2)
    private Float haemoglobin;
    @Column(name = "weight", columnDefinition = "numeric", precision = 7, scale = 3)
    private Float weight;
    @Column(name = "edd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date edd;
    @Column(name = "anc_visit_dates")
    private String ancVisitDates;
    @Column(name = "immunisation_given")
    private String immunisationGiven;
    @Column(name = "blood_group")
    private String bloodGroup;
    @Column(name = "place_of_birth")
    private String placeOfBirth;
    @Column(name = "birth_weight", columnDefinition = "numeric", precision = 7, scale = 3)
    private Float birthWeight;
    @Column(name = "complementary_feeding_started")
    private Boolean complementaryFeedingStarted;
    @Column(name = "last_method_of_contraception")
    private String lastMethodOfContraception;
    @Column(name = "is_high_risk_case")
    private Boolean isHighRiskCase;
    @Column(name = "cur_preg_reg_det_id")
    private Integer curPregRegDetId;
    @Column(name = "cur_preg_reg_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date curPregRegDate;
    @Column(name = "menopause_arrived")
    private Boolean menopauseArrived;
    @Column(name = "sync_status")
    private String syncStatus;
    @Column(name = "is_iucd_removed")
    private Boolean isIucdRemoved;
    @Column(name = "iucd_removal_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date iucdRemovalDate;
    @Column(name = "congenital_anomaly")
    private String congenitalAnomaly;
    @Column(name = "chronic_disease")
    private String chronicDisease;
    @Column(name = "current_disease")
    private String currentDisease;
    @Column(name = "eye_issue")
    private String eyeIssue;
    @Column(name = "last_delivery_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastDeliveryDate;
    @Column(name = "hysterectomy_done")
    private Boolean hysterectomyDone;
    @Column(name = "child_nrc_cmtc_status")
    private String childNrcCmtcStatus;
    @Column(name = "last_delivery_outcome")
    private String lastDeliveryOutcome;
    //    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "imt_member_previous_pregnancy_complication_rel", joinColumns = @JoinColumn(name = "member_id"))
//    @Column(name = "previous_pregnancy_complication")
    private transient Set<String> previousPregnancyComplication;
    @Column(name = "previous_pregnancy_complication")
    private String previousPregnancyComplicationCsv;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "additional_info")
    private String additionalInfo;
    @Column(name = "suspected_cp")
    private Boolean suspectedCp;
    @Column(name = "npcb_screening_date")
    @Temporal(TemporalType.DATE)
    private Date npcbScreeningDate;
    @Column(name = "fhsr_phone_verified")
    private Boolean fhsrPhoneVerified;
    @Column(name = "basic_state")
    private String basicState;
    @Column(name = "eligible_couple_date")
    private Date eligibleCoupleDate;
    @Column(name = "current_gravida")
    private Short currentGravida;
    @Column(name = "current_para")
    private Short currentPara;
    @Column(name = "family_planning_health_infra")
    private Integer familyPlanningHealthInfrastructure;
    @Column(name = "relation_with_hof")
    private String relationWithHof;
    @Column(name = "member_uuid")
    private String memberUUId; //This column hold Unique UUID for member. While using it check if value not exist then generate and persist in DB
    @Column(name = "vulnerable_flag")
    private Boolean vulnerableFlag;
//    @Column(name = "abha_status")
//    private String abhaStatus;
    @Column(name = "health_id")
    private String healthId;
    @Column(name = "health_id_number")
    private String healthIdNumber;
    @Column(name = "health_insurance")
    private Boolean healthInsurance;
    @Column(name = "scheme_detail")
    private String schemeDetail;
    @Column(name = "is_personal_history_done")
    private Boolean isPersonalHistoryDone;
    @Column(name = "pmjay_availability")
    private String pmjayAvailability;
    @Column(name = "alcohol_addiction")
    private String alcoholAddiction;
    @Column(name = "smoking_addiction")
    private String smokingAddiction;
    @Column(name = "tobacco_addiction")
    private String tobaccoAddiction;
    @Column(name = "rch_id")
    private String rchId;
    @Column(name = "hospitalized_earlier")
    private Boolean hospitalizedEarlier;
    @Column(name = "alternate_number")
    private String alternateNumber;
    @Column(name = "physical_disability")
    private String physicalDisability;
//    @Column(name = "other_disability")
//    private String otherDisability;
    @Column(name = "cataract_surgery")
    private String cataractSurgery;
    @Column(name = "sickle_cell_status")
    private String sickleCellStatus;
    @Column(name = "pension_scheme")
    private String pensionScheme;

//    @Column(name = "other_hof_relation")
//    private String otherHofRelation;

    private transient String ndhmHealthId;
    private transient MemberCFHCEntity memberCFHCEntity;
    private transient  boolean markedPregnant;

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

    public Integer getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Integer maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getAadharNumber() {
        return this.aadharNumber != null ? AESEncryption.getInstance().decrypt(aadharNumber) : null;
    }

    public void setAadharNumber(String aadharNumber) {
        if (aadharNumber != null) {
            this.aadharNumber = AESEncryption.getInstance().encrypt(aadharNumber);
        } else {
            this.aadharNumber = null;
        }
    }

    public UUID getAadhaarReferenceKey() {
        return aadhaarReferenceKey;
    }

    public void setAadhaarReferenceKey(UUID aadhaarReferenceKey) {
        this.aadhaarReferenceKey = aadhaarReferenceKey;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Boolean getFamilyHeadFlag() {
        return familyHeadFlag != null ? familyHeadFlag : Boolean.FALSE;
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

    public Boolean getIsPregnantFlag() {
        return isPregnantFlag == null ? Boolean.FALSE : isPregnantFlag;
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

    public Short getNormalCycleDays() {
        return normalCycleDays;
    }

    public void setNormalCycleDays(Short normalCycleDays) {
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGrandfatherName() {
        return grandfatherName;
    }

    public void setGrandfatherName(String grandfatherName) {
        this.grandfatherName = grandfatherName;
    }

    public String getEmamtaHealthId() {
        return emamtaHealthId;
    }

    public void setEmamtaHealthId(String emamtaHealthId) {
        this.emamtaHealthId = emamtaHealthId;
    }

    public Boolean getIsAadharVerified() {
        return isAadharVerified;
    }

    public void setIsAadharVerified(Boolean isAadharVerified) {
        this.isAadharVerified = isAadharVerified;
    }

    public Boolean getIsMobileNumberVerified() {
        return isMobileNumberVerified;
    }

    public void setIsMobileNumberVerified(Boolean isMobileNumberVerified) {
        this.isMobileNumberVerified = isMobileNumberVerified;
    }

    public Boolean getIsNativeFlag() {
        return isNativeFlag;
    }

    public void setIsNativeFlag(Boolean isNativeFlag) {
        this.isNativeFlag = isNativeFlag;
    }

    public Integer getEducationStatus() {
        return educationStatus;
    }

    public void setEducationStatus(Integer educationStatus) {
        this.educationStatus = educationStatus;
    }

    public Boolean getIsReport() {
        return isReport;
    }

    public void setIsReport(Boolean isReport) {
        this.isReport = isReport;
    }

    public String getNameAsPerAadhar() {
        return nameAsPerAadhar;
    }

    public void setNameAsPerAadhar(String nameAsPerAadhar) {
        this.nameAsPerAadhar = nameAsPerAadhar;
    }

    public Set<Integer> getChronicDiseaseDetails() {
        return chronicDiseaseDetails;
    }

    public void setChronicDiseaseDetails(Set<Integer> chronicDiseaseDetails) {
        this.chronicDiseaseDetails = chronicDiseaseDetails;
        setChronicDisease(MemberDataBeanMapper.convertSetToCommaSeparatedString(this.chronicDiseaseDetails, ","));
    }

    public Set<Integer> getCongenitalAnomalyDetails() {
        return congenitalAnomalyDetails;
    }

    public void setCongenitalAnomalyDetails(Set<Integer> congenitalAnomalyDetails) {
        this.congenitalAnomalyDetails = congenitalAnomalyDetails;
        setCongenitalAnomaly(MemberDataBeanMapper.convertSetToCommaSeparatedString(this.congenitalAnomalyDetails, ","));
    }

    public Set<Integer> getCurrentDiseaseDetails() {
        return currentDiseaseDetails;
    }

    public void setCurrentDiseaseDetails(Set<Integer> currentDiseaseDetails) {
        this.currentDiseaseDetails = currentDiseaseDetails;
        setCurrentDisease(MemberDataBeanMapper.convertSetToCommaSeparatedString(this.currentDiseaseDetails, ","));
    }

    public Set<Integer> getEyeIssueDetails() {
        return eyeIssueDetails;
    }

    public void setEyeIssueDetails(Set<Integer> eyeIssueDetails) {
        this.eyeIssueDetails = eyeIssueDetails;
        setEyeIssue(MemberDataBeanMapper.convertSetToCommaSeparatedString(this.eyeIssueDetails, ","));
    }

    public String getMergedFromFamilyId() {
        return mergedFromFamilyId;
    }

    public void setMergedFromFamilyId(String mergedFromFamilyId) {
        this.mergedFromFamilyId = mergedFromFamilyId;
    }

    public Boolean getAgreedToShareAadhar() {
        return agreedToShareAadhar;
    }

    public void setAgreedToShareAadhar(Boolean agreedToShareAadhar) {
        this.agreedToShareAadhar = agreedToShareAadhar;
    }

//    public String getAadharStatus() {
//        return aadharStatus;
//    }
//
//    public void setAadharStatus(String aadharStatus) {
//        this.aadharStatus = aadharStatus;
//    }

    public Boolean getAadharNumberAvailable() {
        return aadharNumberAvailable;
    }

    public void setAadharNumberAvailable(Boolean aadharNumberAvailable) {
        this.aadharNumberAvailable = aadharNumberAvailable;
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

    public Integer getMotherId() {
        return motherId;
    }

    public void setMotherId(Integer motherId) {
        this.motherId = motherId;
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

    public Boolean getIsIucdRemoved() {
        return isIucdRemoved;
    }

    public void setIsIucdRemoved(Boolean isIucdRemoved) {
        this.isIucdRemoved = isIucdRemoved;
    }

    public Date getIucdRemovalDate() {
        return iucdRemovalDate;
    }

    public void setIucdRemovalDate(Date iucdRemovalDate) {
        this.iucdRemovalDate = iucdRemovalDate;
    }

    public String getCongenitalAnomaly() {
        return congenitalAnomaly;
    }

    private void setCongenitalAnomaly(String congenitalAnomaly) {
        this.congenitalAnomaly = congenitalAnomaly;
    }

    public String getChronicDisease() {
        return chronicDisease;
    }

    private void setChronicDisease(String chronicDisease) {
        this.chronicDisease = chronicDisease;
    }

    public String getCurrentDisease() {
        return currentDisease;
    }

    private void setCurrentDisease(String currentDisease) {
        this.currentDisease = currentDisease;
    }

    public String getEyeIssue() {
        return eyeIssue;
    }

    private void setEyeIssue(String eyeIssue) {
        this.eyeIssue = eyeIssue;
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

    public Set<String> getPreviousPregnancyComplication() {
        return previousPregnancyComplication;
    }

    public void setPreviousPregnancyComplication(Set<String> previousPregnancyComplication) {
        this.previousPregnancyComplication = previousPregnancyComplication;
        setPreviousPregnancyComplicationCsv(MemberDataBeanMapper.convertStringSetToCommaSeparatedString(this.previousPregnancyComplication, ","));
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Boolean getSuspectedCp() {
        return suspectedCp;
    }

    public void setSuspectedCp(Boolean suspectedCp) {
        this.suspectedCp = suspectedCp;
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

    public String getBasicState() {
        return basicState;
    }

    public void setBasicState(String basicState) {
        this.basicState = basicState;
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

    public Short getCurrentPara() {
        return currentPara;
    }

    public void setCurrentPara(Short currentPara) {
        this.currentPara = currentPara;
    }

    public Date getDateOfWedding() {
        return dateOfWedding;
    }

    public void setDateOfWedding(Date dateOfWedding) {
        this.dateOfWedding = dateOfWedding;
    }

    public Integer getHusbandId() {
        return husbandId;
    }

    public void setHusbandId(Integer husbandId) {
        this.husbandId = husbandId;
    }

    public Integer getFamilyPlanningHealthInfrastructure() {
        return familyPlanningHealthInfrastructure;
    }

    public void setFamilyPlanningHealthInfrastructure(Integer familyPlanningHealthInfrastructure) {
        this.familyPlanningHealthInfrastructure = familyPlanningHealthInfrastructure;
    }

    public MemberCFHCEntity getMemberCFHCEntity() {
        return memberCFHCEntity;
    }

    public void setMemberCFHCEntity(MemberCFHCEntity memberCFHCEntity) {
        this.memberCFHCEntity = memberCFHCEntity;
    }

    public String getRelationWithHof() {
        return relationWithHof;
    }

    public void setRelationWithHof(String relationWithHof) {
        this.relationWithHof = relationWithHof;
    }

    public String getPreviousPregnancyComplicationCsv() {
        return previousPregnancyComplicationCsv;
    }

    public void setPreviousPregnancyComplicationCsv(String previousPregnancyComplicationCsv) {
        this.previousPregnancyComplicationCsv = previousPregnancyComplicationCsv;
    }

    public String getMemberUUId() {
        return memberUUId;
    }

    public void setMemberUUId(String memberUUId) {
        this.memberUUId = memberUUId;
    }

    public String getNdhmHealthId() {
        return ndhmHealthId;
    }

    public void setNdhmHealthId(String ndhmHealthId) {
        this.ndhmHealthId = ndhmHealthId;
    }

    public Boolean getVulnerableFlag() {
        return vulnerableFlag;
    }

    public void setVulnerableFlag(Boolean vulnerableFlag) {
        this.vulnerableFlag = vulnerableFlag;
    }

//    public String getAbhaStatus() {
//        return abhaStatus;
//    }
//
//    public void setAbhaStatus(String abhaStatus) {
//        this.abhaStatus = abhaStatus;
//    }

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

    public Boolean getPersonalHistoryDone() {
        return isPersonalHistoryDone;
    }

    public void setPersonalHistoryDone(Boolean personalHistoryDone) {
        isPersonalHistoryDone = personalHistoryDone;
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

    public String getRchId() {
        return rchId;
    }

    public void setRchId(String rchId) {
        this.rchId = rchId;
    }

    public Boolean getHospitalizedEarlier() {
        return hospitalizedEarlier;
    }

    public void setHospitalizedEarlier(Boolean hospitalizedEarlier) {
        this.hospitalizedEarlier = hospitalizedEarlier;
    }

    public String getAlternateNumber() {
        return alternateNumber;
    }

    public void setAlternateNumber(String alternateNumber) {
        this.alternateNumber = alternateNumber;
    }

    public String getPhysicalDisability() {
        return physicalDisability;
    }

    public void setPhysicalDisability(String physicalDisability) {
        this.physicalDisability = physicalDisability;
    }

//    public String getOtherDisability() {
//        return otherDisability;
//    }
//
//    public void setOtherDisabilit y(String otherDisability) {
//        this.otherDisability = otherDisability;
//    }

    public String getCataractSurgery() {
        return cataractSurgery;
    }

    public void setCataractSurgery(String cataractSurgery) {
        this.cataractSurgery = cataractSurgery;
    }

    public String getSickleCellStatus() {
        return sickleCellStatus;
    }

    public void setSickleCellStatus(String sickleCellStatus) {
        this.sickleCellStatus = sickleCellStatus;
    }

    public String getPensionScheme() {
        return pensionScheme;
    }

    public void setPensionScheme(String pensionScheme) {
        this.pensionScheme = pensionScheme;
    }

//    public String getOccupation() {
//        return occupation;
//    }
//
//    public void setOccupation(String occupation) {
//        this.occupation = occupation;
//    }

//    public Boolean getUnderTreatmentChronic() {
//        return underTreatmentChronic;
//    }
//
//    public void setUnderTreatmentChronic(Boolean underTreatmentChronic) {
//        this.underTreatmentChronic = underTreatmentChronic;
//    }

//    public String getOtherChronic() {
//        return otherChronic;
//    }

//    public void setOtherChronic(String otherChronic) {
//        this.otherChronic = otherChronic;
//    }

//    public String getOtherEyeIssue() {
//        return otherEyeIssue;
//    }
//
//    public void setOtherEyeIssue(String otherEyeIssue) {
//        this.otherEyeIssue = otherEyeIssue;
//    }

//    public String getOtherHofRelation() {
//        return otherHofRelation;
//    }
//
//    public void setOtherHofRelation(String otherHofRelation) {
//        this.otherHofRelation = otherHofRelation;
//    }


    public boolean isMarkedPregnant() {
        return markedPregnant;
    }

    public void setMarkedPregnant(boolean markedPregnant) {
        this.markedPregnant = markedPregnant;
    }

    public static class Fields {

        private Fields() {
            throw new IllegalStateException("Utility Class");
        }

        public static final String ID = "id";
        public static final String FAMILY_ID = "familyId";
        public static final String FIRST_NAME = "firstName";
        public static final String MIDDLE_NAME = "middleName";
        public static final String LAST_NAME = "lastName";
        public static final String GENDER = "gender";
        public static final String MARITAL_STATUS = "maritalStatus";
        public static final String AADHAR_NUMBER = "aadharNumber";
        public static final String MOBILE_NUMBER = "mobileNumber";
        public static final String FAMILY_HEAD_FLAG = "familyHeadFlag";
        public static final String DOB = "dob";
        public static final String UNIQUE_HEALTH_ID = "uniqueHealthId";
        public static final String IFSC = "ifsc";
        public static final String ACCOUNT_NUMBER = "accountNumber";
        public static final String MAA_VATSALYA_CARD_NUMBER = "maaVatsalyaCardNumber";
        public static final String IS_PREGNANT_FLAG = "isPregnantFlag";
        public static final String LMP_DATE = "lmpDate";
        public static final String NORMAL_CYCLE_DAYS = "normalCycleDays";
        public static final String FAMILY_PLANNING_METHOD = "familyPlanningMethod";
        public static final String FP_INSERT_OPERATE_DATE = "fpInsertOperateDate";
        public static final String STATE = "state";
        public static final String GRANDFATHER_NAME = "grandfatherName";
        public static final String EMAMTA_HEALTH_ID = "emamtaHealthId";
        public static final String IS_AADHAR_VERIFIED = "isAadharVerified";
        public static final String IS_MOBILE_NUMBER_VERIFIED = "isMobileNumberVerified";
        public static final String IS_NATIVE_FLAG = "isNativeFlag";
        public static final String EDUCATION_STATUS = "educationStatus";
        public static final String CURRENT_STATE = "currentState";
        public static final String IS_REPORT = "isReport";
        public static final String NAME_AS_PER_AADHAR = "nameAsPerAadhar";
        public static final String CURRENT_STATE_DETAIL = "currentStateDetail";
        public static final String CHRONIC_DISEASE_DETAILS = "chronicDiseaseDetails";
        public static final String CONGENITAL_ANOMALY_DETAILS = "congenitalAnomalyDetails";
        public static final String CURRENT_DISEASE_DETAILS = "currentDiseaseDetails";
        public static final String EYE_ISSUE_DETAILS = "eyeIssueDetails";
        public static final String MERGED_FROM_FAMILY_ID = "mergedFromFamilyId";
        public static final String AGREED_TO_SHARE_AADHAR = "agreedToShareAadhar";
        public static final String AADHAR_NUMBER_AVAILABLE = "aadharNumberAvailable";
        public static final String YEAR_OF_WEDDING = "yearOfWedding";
        public static final String DATE_OF_WEDDING = "dateOfWedding";
        public static final String MOTHER_ID = "motherId";
        public static final String LAST_METHOD_OF_CONTRACEPTION = "lastMethodOfContraception";
        public static final String CUR_PREG_REG_DET_ID = "curPregRegDetId";
        public static final String CUR_PREG_REG_DATE = "curPregRegDate";
        public static final String MENOPAUSE_ARRIVED = "menopauseArrived";
        public static final String SYNC_STATUS = "syncStatus";
        public static final String IS_IUCD_REMOVED = "isIucdRemoved";
        public static final String IUCD_REMOVAL_DATE = "iucdRemovalDate";
        public static final String LAST_DELIVERY_DATE = "lastDeliveryDate";
        public static final String HYSTERECTOMY_DONE = "hysterectomyDone";
        public static final String CHILD_NRC_CMTC_STATUS = "childNrcCmtcStatus";
        public static final String LAST_DELIVERY_OUTCOME = "lastDeliveryOutcome";
        public static final String PREVIOUS_PREGNANCY_COMPLICATION = "previousPregnancyComplication";
        public static final String REMARKS = "remarks";
        public static final String NPCB_SCREENING_DATE = "npcbScreeningDate";
        public static final String FHSR_PHONE_VERIFIED = "fhsrPhoneVerified";
        public static final String BASIC_STATE = "basicState";
        public static final String CURRENT_GRAVIDA = "currentGravida";
        public static final String CURRENT_PARA = "currentPara";
        public static final String RELATION_WITH_HOF = "relationWithHof";
    }

    @Override
    public String toString() {
        return "MemberEntity{" + "id=" + id + ", familyId='" + familyId + '\'' + ", firstName='" + firstName + '\'' + ", middleName='" + middleName + '\'' + ", lastName='" + lastName + '\'' + ", gender='" + gender + '\'' + ", maritalStatus=" + maritalStatus + ", mobileNumber='" + mobileNumber + '\'' + ", dob=" + dob + ", uniqueHealthId='" + uniqueHealthId + '\'' + ", isPregnantFlag=" + isPregnantFlag + ", state='" + state + '\'' + '}';
    }

    public String getFullName() {
        return this.getFirstName() + " " + this.getMiddleName() + " " + this.getLastName();
    }
}
