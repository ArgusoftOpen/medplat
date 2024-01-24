/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dto;

import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.enums.ReferralPlace;
import com.argusoft.medplat.ncd.enums.Status;
import com.argusoft.medplat.ncd.enums.SubType;
import com.argusoft.medplat.ncd.model.MedicineMaster;
import com.argusoft.medplat.ncd.model.MemberDiabetesDetail;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 *     Used for member diabetes details.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class MemberDiabetesDto {

    private Integer id;
    private Integer memberId;
    private Integer locationId;
    private Integer familyId;
    private Integer fastingBloodSugar;
    private Integer postPrandialBloodSugar;
    private Integer bloodSugar;
    private Boolean dka;
    private BigDecimal hba1c;
    private Date screeningDate;
    private DoneBy doneBy;
    private Date createdOn;
    private Integer createdBy;
    private Integer referralId;
    private Status status;
    private Date followUpDate;
    private String readings;
    private SubType subType;
    private String reason;
    private Integer healthInfraId;
    private Boolean flag;
    private Integer urineSugar;
    private List<Integer> medicines;
    private List<MedicineMaster> medicineMasters;
    private Integer referredFromHealthInfrastructureId;

    private Boolean earlierDiabetesDiagnosis;
    private Boolean currentlyUnderTreatment;
    private Boolean peripheralPulses;
    private Boolean callusesFeet;
    private Boolean ulcersFeet;
    private Boolean gangreneFeet;
    private Boolean prominentVeins;
    private Boolean edema;
    private Boolean anyInjuries;
    private Boolean regularRythmCardio;
    private Boolean sensoryLoss;
    private MemberDiagnosisDto diagnosisDto;
    private String refferralPlace;
    private ReferralPlace refTo;
    private ReferralPlace refFrom;
    private Boolean doesSuffering;
    private List<GeneralDetailMedicineDto> medicineDetail;
    private String measurementType;
    private Boolean takeMedicine;
    private Boolean htn;
    private Float weight;
    private Integer height;
    private Float bmi;
    private String pvtHealthInfraName;

    public String getPvtHealthInfraName() {
        return pvtHealthInfraName;
    }

    public void setPvtHealthInfraName(String pvtHealthInfraName) {
        this.pvtHealthInfraName = pvtHealthInfraName;
    }

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

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public Integer getFastingBloodSugar() {
        return fastingBloodSugar;
    }

    public void setFastingBloodSugar(Integer fastingBloodSugar) {
        this.fastingBloodSugar = fastingBloodSugar;
    }

    public Integer getPostPrandialBloodSugar() {
        return postPrandialBloodSugar;
    }

    public void setPostPrandialBloodSugar(Integer postPrandialBloodSugar) {
        this.postPrandialBloodSugar = postPrandialBloodSugar;
    }

    public Integer getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(Integer bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public Boolean getDka() {
        return dka;
    }

    public void setDka(Boolean dka) {
        this.dka = dka;
    }

    public BigDecimal getHba1c() {
        return hba1c;
    }

    public void setHba1c(BigDecimal hba1c) {
        this.hba1c = hba1c;
    }

    public Date getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(Date screeningDate) {
        this.screeningDate = screeningDate;
    }

    public DoneBy getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(DoneBy doneBy) {
        this.doneBy = doneBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getReferralId() {
        return referralId;
    }

    public void setReferralId(Integer referralId) {
        this.referralId = referralId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(Date followUpDate) {
        this.followUpDate = followUpDate;
    }

    public String getReadings() {
        return readings;
    }

    public void setReadings(String readings) {
        this.readings = readings;
    }

    public SubType getSubType() {
        return subType;
    }

    public void setSubType(SubType subType) {
        this.subType = subType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getHealthInfraId() {
        return healthInfraId;
    }

    public Integer getUrineSugar() {
        return urineSugar;
    }

    public void setUrineSugar(Integer urineSugar) {
        this.urineSugar = urineSugar;
    }

    public void setHealthInfraId(Integer healthInfraId) {
        this.healthInfraId = healthInfraId;
    }

    public List<Integer> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<Integer> medicines) {
        this.medicines = medicines;
    }

    public Boolean getEarlierDiabetesDiagnosis() {
        return earlierDiabetesDiagnosis;
    }

    public void setEarlierDiabetesDiagnosis(Boolean earlierDiabetesDiagnosis) {
        this.earlierDiabetesDiagnosis = earlierDiabetesDiagnosis;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Boolean getCurrentlyUnderTreatment() {
        return currentlyUnderTreatment;
    }

    public void setCurrentlyUnderTreatment(Boolean currentlyUnderTreatment) {
        this.currentlyUnderTreatment = currentlyUnderTreatment;
    }

    public Boolean getPeripheralPulses() {
        return peripheralPulses;
    }

    public void setPeripheralPulses(Boolean peripheralPulses) {
        this.peripheralPulses = peripheralPulses;
    }

    public Boolean getCallusesFeet() {
        return callusesFeet;
    }

    public void setCallusesFeet(Boolean callusesFeet) {
        this.callusesFeet = callusesFeet;
    }

    public Boolean getUlcersFeet() {
        return ulcersFeet;
    }

    public void setUlcersFeet(Boolean ulcersFeet) {
        this.ulcersFeet = ulcersFeet;
    }

    public Boolean getGangreneFeet() {
        return gangreneFeet;
    }

    public void setGangreneFeet(Boolean gangreneFeet) {
        this.gangreneFeet = gangreneFeet;
    }

    public Boolean getProminentVeins() {
        return prominentVeins;
    }

    public void setProminentVeins(Boolean prominentVeins) {
        this.prominentVeins = prominentVeins;
    }

    public Boolean getEdema() {
        return edema;
    }

    public void setEdema(Boolean edema) {
        this.edema = edema;
    }

    public Boolean getAnyInjuries() {
        return anyInjuries;
    }

    public void setAnyInjuries(Boolean anyInjuries) {
        this.anyInjuries = anyInjuries;
    }

    public Boolean getRegularRythmCardio() {
        return regularRythmCardio;
    }

    public void setRegularRythmCardio(Boolean regularRythmCardio) {
        this.regularRythmCardio = regularRythmCardio;
    }

    public Boolean getSensoryLoss() {
        return sensoryLoss;
    }

    public void setSensoryLoss(Boolean sensoryLoss) {
        this.sensoryLoss = sensoryLoss;
    }

    public MemberDiagnosisDto getDiagnosisDto() {
        return diagnosisDto;
    }

    public void setDiagnosisDto(MemberDiagnosisDto diagnosisDto) {
        this.diagnosisDto = diagnosisDto;
    }

    public String getRefferralPlace() {
        return refferralPlace;
    }

    public void setRefferralPlace(String refferralPlace) {
        this.refferralPlace = refferralPlace;
    }

    public ReferralPlace getRefTo() {
        return refTo;
    }

    public void setRefTo(ReferralPlace refTo) {
        this.refTo = refTo;
    }

    public ReferralPlace getRefFrom() {
        return refFrom;
    }

    public void setRefFrom(ReferralPlace refFrom) {
        this.refFrom = refFrom;
    }

    public Integer getReferredFromHealthInfrastructureId() {
        return referredFromHealthInfrastructureId;
    }

    public void setReferredFromHealthInfrastructureId(Integer referredFromHealthInfrastructureId) {
        this.referredFromHealthInfrastructureId = referredFromHealthInfrastructureId;
    }

    public List<MedicineMaster> getMedicineMasters() {
        return medicineMasters;
    }

    public void setMedicineMasters(List<MedicineMaster> medicineMasters) {
        this.medicineMasters = medicineMasters;
    }

    public Boolean getDoesSuffering() {
        return doesSuffering;
    }

    public void setDoesSuffering(Boolean doesSuffering) {
        this.doesSuffering = doesSuffering;
    }

    public List<GeneralDetailMedicineDto> getMedicineDetail() {
        return medicineDetail;
    }

    public void setMedicineDetail(List<GeneralDetailMedicineDto> medicineDetail) {
        this.medicineDetail = medicineDetail;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    public Boolean getTakeMedicine() {
        return takeMedicine;
    }

    public void setTakeMedicine(Boolean takeMedicine) {
        this.takeMedicine = takeMedicine;
    }

    public Boolean getHtn() {
        return htn;
    }

    public void setHtn(Boolean htn) {
        this.htn = htn;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Float getBmi() {
        return bmi;
    }

    public void setBmi(Float bmi) {
        this.bmi = bmi;
    }
}
