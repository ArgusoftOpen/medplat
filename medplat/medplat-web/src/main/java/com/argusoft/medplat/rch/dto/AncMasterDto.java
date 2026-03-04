package com.argusoft.medplat.rch.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Used for anc master.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
public class AncMasterDto {

    private Integer id;
    private Integer memberId;
    private Integer familyId;
    private String latitude;
    private String longitude;
    private Date mobileStartDate;
    private Date mobileEndDate;
    private Integer locationId;
    private Integer notificationId;
    private String memberStatus;
    private Date lmp;
    private String bloodGroup;
    private String lastDeliveryOutcome;
    private Set<String> previousPregnancyComplication;
    private String otherPreviousPregnancyComplication;
    private Boolean jsyBeneficiary;
    private Boolean jsyPaymentDone;
    private Boolean kpsyBeneficiary;
    private Boolean iayBeneficiary;
    private Boolean chiranjeeviYojnaBeneficiary;
    private Integer ancPlace;
    private Float weight;
    private Float haemoglobinCount;
    private Integer systolicBp;
    private Integer diastolicBp;
    private Integer memberHeight;
    private String foetalMovement;
    private Integer foetalHeight;
    private Boolean foetalHeartSound;
    private String foetalPosition;
    private Integer ifaTabletsGiven;
    private Integer faTabletsGiven;
    private Integer calciumTabletsGiven;
    private String hbsagTest;
    private String bloodSugarTest;
    private Integer sugarTestAfterFoodValue;
    private Integer sugarTestBeforeFoodValue;
    private Boolean urineTestDone;
    private String urineAlbumin;
    private String urineSugar;
    private String vdrlTest;
    private String sickleCellTest;
    private String hivTest;
    private Boolean albendazoleGiven;
    private Set<Integer> dangerousSignIds;
    private String otherDangerousSign;
    private String referralDone;
    private Integer referralPlace;
    private String expectedDeliveryPlace;
    private String familyPlanningMethod;
    private Boolean deadFlag;
    private Date deathDate;
    private String placeOfDeath;
    private String deathReason;
    private Date edd;
    private Boolean isHighRiskCase;
    private Integer pregnancyRegDetId;
    private Date serviceDate;
    private Integer createdBy;
    private Date createdOn;
    private Integer modifiedBy;
    private Date modifiedOn;
    private List<ImmunisationDto> immunisationDetails;
    private Boolean isFromWeb;
    private String deliveryPlace;
    private Integer typeOfHospital;
    private Integer healthInfrastructureId;
    private String healthInfraName;
    private String deliveryDoneBy;
    private Integer deliveryPerson;
    private String deliveryPersonName;
    private String uniqueHealthID;
    private String accountNumber;
    private String ifsc;
    private String mobileNumber;
    private Boolean bloodTransfusion;
    private String ironDefAnemiaInj;
    private Date ironDefAnemiaInjDueDate;
    private Boolean examinedByGynecologist;
    private Boolean isInjCorticosteroidGiven;
    private Integer referralInfraId;
    private Boolean isLinkedToAbha;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Date getMobileStartDate() {
        return mobileStartDate;
    }

    public void setMobileStartDate(Date mobileStartDate) {
        this.mobileStartDate = mobileStartDate;
    }

    public Date getMobileEndDate() {
        return mobileEndDate;
    }

    public void setMobileEndDate(Date mobileEndDate) {
        this.mobileEndDate = mobileEndDate;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public Date getLmp() {
        return lmp;
    }

    public void setLmp(Date lmp) {
        this.lmp = lmp;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
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
    }

    public String getOtherPreviousPregnancyComplication() {
        return otherPreviousPregnancyComplication;
    }

    public void setOtherPreviousPregnancyComplication(String otherPreviousPregnancyComplication) {
        this.otherPreviousPregnancyComplication = otherPreviousPregnancyComplication;
    }

    public Boolean getJsyBeneficiary() {
        return jsyBeneficiary;
    }

    public void setJsyBeneficiary(Boolean jsyBeneficiary) {
        this.jsyBeneficiary = jsyBeneficiary;
    }

    public Boolean getJsyPaymentDone() {
        return jsyPaymentDone;
    }

    public void setJsyPaymentDone(Boolean jsyPaymentDone) {
        this.jsyPaymentDone = jsyPaymentDone;
    }

    public Boolean getKpsyBeneficiary() {
        return kpsyBeneficiary;
    }

    public void setKpsyBeneficiary(Boolean kpsyBeneficiary) {
        this.kpsyBeneficiary = kpsyBeneficiary;
    }

    public Boolean getIayBeneficiary() {
        return iayBeneficiary;
    }

    public void setIayBeneficiary(Boolean iayBeneficiary) {
        this.iayBeneficiary = iayBeneficiary;
    }

    public Boolean getChiranjeeviYojnaBeneficiary() {
        return chiranjeeviYojnaBeneficiary;
    }

    public void setChiranjeeviYojnaBeneficiary(Boolean chiranjeeviYojnaBeneficiary) {
        this.chiranjeeviYojnaBeneficiary = chiranjeeviYojnaBeneficiary;
    }

    public Integer getAncPlace() {
        return ancPlace;
    }

    public void setAncPlace(Integer ancPlace) {
        this.ancPlace = ancPlace;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getHaemoglobinCount() {
        return haemoglobinCount;
    }

    public void setHaemoglobinCount(Float haemoglobinCount) {
        this.haemoglobinCount = haemoglobinCount;
    }

    public Integer getSystolicBp() {
        return systolicBp;
    }

    public void setSystolicBp(Integer systolicBp) {
        this.systolicBp = systolicBp;
    }

    public Integer getDiastolicBp() {
        return diastolicBp;
    }

    public void setDiastolicBp(Integer diastolicBp) {
        this.diastolicBp = diastolicBp;
    }

    public Integer getMemberHeight() {
        return memberHeight;
    }

    public void setMemberHeight(Integer memberHeight) {
        this.memberHeight = memberHeight;
    }

    public String getFoetalMovement() {
        return foetalMovement;
    }

    public void setFoetalMovement(String foetalMovement) {
        this.foetalMovement = foetalMovement;
    }

    public Integer getFoetalHeight() {
        return foetalHeight;
    }

    public void setFoetalHeight(Integer foetalHeight) {
        this.foetalHeight = foetalHeight;
    }

    public Boolean getFoetalHeartSound() {
        return foetalHeartSound;
    }

    public void setFoetalHeartSound(Boolean foetalHeartSound) {
        this.foetalHeartSound = foetalHeartSound;
    }

    public String getFoetalPosition() {
        return foetalPosition;
    }

    public void setFoetalPosition(String foetalPosition) {
        this.foetalPosition = foetalPosition;
    }

    public Integer getIfaTabletsGiven() {
        return ifaTabletsGiven;
    }

    public void setIfaTabletsGiven(Integer ifaTabletsGiven) {
        this.ifaTabletsGiven = ifaTabletsGiven;
    }

    public Integer getFaTabletsGiven() {
        return faTabletsGiven;
    }

    public void setFaTabletsGiven(Integer faTabletsGiven) {
        this.faTabletsGiven = faTabletsGiven;
    }

    public Integer getCalciumTabletsGiven() {
        return calciumTabletsGiven;
    }

    public void setCalciumTabletsGiven(Integer calciumTabletsGiven) {
        this.calciumTabletsGiven = calciumTabletsGiven;
    }

    public String getHbsagTest() {
        return hbsagTest;
    }

    public void setHbsagTest(String hbsagTest) {
        this.hbsagTest = hbsagTest;
    }

    public String getBloodSugarTest() {
        return bloodSugarTest;
    }

    public void setBloodSugarTest(String bloodSugarTest) {
        this.bloodSugarTest = bloodSugarTest;
    }

    public Integer getSugarTestAfterFoodValue() {
        return sugarTestAfterFoodValue;
    }

    public void setSugarTestAfterFoodValue(Integer sugarTestAfterFoodValue) {
        this.sugarTestAfterFoodValue = sugarTestAfterFoodValue;
    }

    public Integer getSugarTestBeforeFoodValue() {
        return sugarTestBeforeFoodValue;
    }

    public void setSugarTestBeforeFoodValue(Integer sugarTestBeforeFoodValue) {
        this.sugarTestBeforeFoodValue = sugarTestBeforeFoodValue;
    }

    public Boolean getUrineTestDone() {
        return urineTestDone;
    }

    public void setUrineTestDone(Boolean urineTestDone) {
        this.urineTestDone = urineTestDone;
    }

    public String getUrineAlbumin() {
        return urineAlbumin;
    }

    public void setUrineAlbumin(String urineAlbumin) {
        this.urineAlbumin = urineAlbumin;
    }

    public String getUrineSugar() {
        return urineSugar;
    }

    public void setUrineSugar(String urineSugar) {
        this.urineSugar = urineSugar;
    }

    public String getVdrlTest() {
        return vdrlTest;
    }

    public void setVdrlTest(String vdrlTest) {
        this.vdrlTest = vdrlTest;
    }

    public String getSickleCellTest() {
        return sickleCellTest;
    }

    public void setSickleCellTest(String sickleCellTest) {
        this.sickleCellTest = sickleCellTest;
    }

    public String getHivTest() {
        return hivTest;
    }

    public void setHivTest(String hivTest) {
        this.hivTest = hivTest;
    }

    public Boolean getAlbendazoleGiven() {
        return albendazoleGiven;
    }

    public void setAlbendazoleGiven(Boolean albendazoleGiven) {
        this.albendazoleGiven = albendazoleGiven;
    }

    public Set<Integer> getDangerousSignIds() {
        return dangerousSignIds;
    }

    public void setDangerousSignIds(Set<Integer> dangerousSignIds) {
        this.dangerousSignIds = dangerousSignIds;
    }

    public String getOtherDangerousSign() {
        return otherDangerousSign;
    }

    public void setOtherDangerousSign(String otherDangerousSign) {
        this.otherDangerousSign = otherDangerousSign;
    }

    public String getReferralDone() {
        return referralDone;
    }

    public void setReferralDone(String referralDone) {
        this.referralDone = referralDone;
    }

    public Integer getReferralPlace() {
        return referralPlace;
    }

    public void setReferralPlace(Integer referralPlace) {
        this.referralPlace = referralPlace;
    }

    public String getExpectedDeliveryPlace() {
        return expectedDeliveryPlace;
    }

    public void setExpectedDeliveryPlace(String expectedDeliveryPlace) {
        this.expectedDeliveryPlace = expectedDeliveryPlace;
    }

    public String getFamilyPlanningMethod() {
        return familyPlanningMethod;
    }

    public void setFamilyPlanningMethod(String familyPlanningMethod) {
        this.familyPlanningMethod = familyPlanningMethod;
    }

    public Boolean getDeadFlag() {
        return deadFlag;
    }

    public void setDeadFlag(Boolean deadFlag) {
        this.deadFlag = deadFlag;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public String getPlaceOfDeath() {
        return placeOfDeath;
    }

    public void setPlaceOfDeath(String placeOfDeath) {
        this.placeOfDeath = placeOfDeath;
    }

    public String getDeathReason() {
        return deathReason;
    }

    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }

    public Date getEdd() {
        return edd;
    }

    public void setEdd(Date edd) {
        this.edd = edd;
    }

    public Boolean getIsHighRiskCase() {
        return isHighRiskCase;
    }

    public void setIsHighRiskCase(Boolean isHighRiskCase) {
        this.isHighRiskCase = isHighRiskCase;
    }

    public Integer getPregnancyRegDetId() {
        return pregnancyRegDetId;
    }

    public void setPregnancyRegDetId(Integer pregnancyRegDetId) {
        this.pregnancyRegDetId = pregnancyRegDetId;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public List<ImmunisationDto> getImmunisationDetails() {
        return immunisationDetails;
    }

    public void setImmunisationDetails(List<ImmunisationDto> immunisationDetails) {
        this.immunisationDetails = immunisationDetails;
    }

    public Boolean getIsFromWeb() {
        return isFromWeb;
    }

    public void setIsFromWeb(Boolean isFromWeb) {
        this.isFromWeb = isFromWeb;
    }

    public String getDeliveryPlace() {
        return deliveryPlace;
    }

    public void setDeliveryPlace(String deliveryPlace) {
        this.deliveryPlace = deliveryPlace;
    }

    public Integer getTypeOfHospital() {
        return typeOfHospital;
    }

    public void setTypeOfHospital(Integer typeOfHospital) {
        this.typeOfHospital = typeOfHospital;
    }

    public Integer getHealthInfrastructureId() {
        return healthInfrastructureId;
    }

    public void setHealthInfrastructureId(Integer healthInfrastructureId) {
        this.healthInfrastructureId = healthInfrastructureId;
    }

    public String getDeliveryDoneBy() {
        return deliveryDoneBy;
    }

    public void setDeliveryDoneBy(String deliveryDoneBy) {
        this.deliveryDoneBy = deliveryDoneBy;
    }

    public Integer getDeliveryPerson() {
        return deliveryPerson;
    }

    public void setDeliveryPerson(Integer deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }

    public String getDeliveryPersonName() {
        return deliveryPersonName;
    }

    public void setDeliveryPersonName(String deliveryPersonName) {
        this.deliveryPersonName = deliveryPersonName;
    }

    public String getUniqueHealthID() {
        return uniqueHealthID;
    }

    public void setUniqueHealthID(String uniqueHealthID) {
        this.uniqueHealthID = uniqueHealthID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Boolean getBloodTransfusion() {
        return bloodTransfusion;
    }

    public void setBloodTransfusion(Boolean bloodTransfusion) {
        this.bloodTransfusion = bloodTransfusion;
    }

    public String getIronDefAnemiaInj() {
        return ironDefAnemiaInj;
    }

    public void setIronDefAnemiaInj(String ironDefAnemiaInj) {
        this.ironDefAnemiaInj = ironDefAnemiaInj;
    }

    public Date getIronDefAnemiaInjDueDate() {
        return ironDefAnemiaInjDueDate;
    }

    public void setIronDefAnemiaInjDueDate(Date ironDefAnemiaInjDueDate) {
        this.ironDefAnemiaInjDueDate = ironDefAnemiaInjDueDate;
    }

    public Boolean getExaminedByGynecologist() {
        return examinedByGynecologist;
    }

    public void setExaminedByGynecologist(Boolean examinedByGynecologist) {
        this.examinedByGynecologist = examinedByGynecologist;
    }

    public Boolean isInjCorticosteroidGiven() {
        return isInjCorticosteroidGiven;
    }

    public void setIsInjCorticosteroidGiven(Boolean isInjCorticosteroidGiven) {
        this.isInjCorticosteroidGiven = isInjCorticosteroidGiven;
    }

    public Integer getReferralInfraId() {
        return referralInfraId;
    }

    public void setReferralInfraId(Integer referralInfraId) {
        this.referralInfraId = referralInfraId;
    }

    public String getHealthInfraName() {
        return healthInfraName;
    }

    public void setHealthInfraName(String healthInfraName) {
        this.healthInfraName = healthInfraName;
    }

    public Boolean getIsLinkedToAbha() {
        return isLinkedToAbha;
    }

    public void setIsLinkedToAbha(Boolean isLinkedToAbha) {
        this.isLinkedToAbha = isLinkedToAbha;
    }
}
