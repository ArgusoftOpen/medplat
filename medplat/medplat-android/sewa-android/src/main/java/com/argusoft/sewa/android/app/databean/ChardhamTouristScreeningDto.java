package com.argusoft.sewa.android.app.databean;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ChardhamTouristScreeningDto {
    private UUID uuid;
    private Integer id;
    private String memberUniqueId;
    private String scanId;
    private Float oxygenValue;
    private String screeningStatus;
    private List<String> symptoms;
    private List<String> treatment;
    private List<String> diagnosis;
    private Integer bloodSugarTest;
    private Float temperature;
    private Integer systolicBp;
    private Integer diastolicBp;
    private Integer healthInfraId;
    private String mmId;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date createdOn;
    private String healthInfraName;
    private Integer age;
    private String otherSymptoms;
    private String otherDiagnosis;
    private String otherMedicines;
    private String treatmentStatus;
    private String courseOfTreatment;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMemberUniqueId() {
        return memberUniqueId;
    }

    public void setMemberUniqueId(String memberUniqueId) {
        this.memberUniqueId = memberUniqueId;
    }

    public String getScanId() {
        return scanId;
    }

    public void setScanId(String scanId) {
        this.scanId = scanId;
    }

    public Float getOxygenValue() {
        return oxygenValue;
    }

    public void setOxygenValue(Float oxygenValue) {
        this.oxygenValue = oxygenValue;
    }

    public String getScreeningStatus() {
        return screeningStatus;
    }

    public void setScreeningStatus(String screeningStatus) {
        this.screeningStatus = screeningStatus;
    }

    public List<String> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<String> symptoms) {
        this.symptoms = symptoms;
    }

    public List<String> getTreatment() {
        return treatment;
    }

    public void setTreatment(List<String> treatment) {
        this.treatment = treatment;
    }

    public Integer getBloodSugarTest() {
        return bloodSugarTest;
    }

    public void setBloodSugarTest(Integer bloodSugarTest) {
        this.bloodSugarTest = bloodSugarTest;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
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

    public Integer getHealthInfraId() {
        return healthInfraId;
    }

    public void setHealthInfraId(Integer healthInfraId) {
        this.healthInfraId = healthInfraId;
    }

    public String getMmId() {
        return mmId;
    }

    public void setMmId(String mmId) {
        this.mmId = mmId;
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

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getHealthInfraName() {
        return healthInfraName;
    }

    public void setHealthInfraName(String healthInfraName) {
        this.healthInfraName = healthInfraName;
    }

    public List<String> getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(List<String> diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getOtherSymptoms() {
        return otherSymptoms;
    }

    public void setOtherSymptoms(String otherSymptoms) {
        this.otherSymptoms = otherSymptoms;
    }

    public String getOtherDiagnosis() {
        return otherDiagnosis;
    }

    public void setOtherDiagnosis(String otherDiagnosis) {
        this.otherDiagnosis = otherDiagnosis;
    }

    public String getOtherMedicines() {
        return otherMedicines;
    }

    public void setOtherMedicines(String otherMedicines) {
        this.otherMedicines = otherMedicines;
    }

    public String getTreatmentStatus() {
        return treatmentStatus;
    }

    public void setTreatmentStatus(String treatmentStatus) {
        this.treatmentStatus = treatmentStatus;
    }

    public String getCourseOfTreatment() {
        return courseOfTreatment;
    }

    public void setCourseOfTreatment(String courseOfTreatment) {
        this.courseOfTreatment = courseOfTreatment;
    }
}
