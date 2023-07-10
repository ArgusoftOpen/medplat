package com.argusoft.sewa.android.app.databean;


import java.util.List;
import java.util.UUID;


public class ChardhamTouristScreeningDataBean {

    private UUID uuid;
    private Integer id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String memberUniqueId;
    private String scanId;
    private float oxygenValue;
    private String screeningStatus;
    private List<String> symptoms;
    private List<String> treatment;
    private List<String> diagnosis;
    private Integer bloodSugarTest;
    private float temperature;
    private Integer systolicBp;
    private Integer diastolicBp;
    private Integer healthInfraId;
    private Integer age;
    private Float weight;
    private Boolean hasBreathlessness;
    private Boolean hasHighBloodPressure;
    private Boolean hasAsthma;
    private Boolean hasDiabetes;
    private Boolean hasHeartCondition;
    private Boolean isPregnant;
    private String gender;
    private String contactNumber;
    private String checkSumWhenScannedFromDevice;

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

    public float getOxygenValue() {
        return oxygenValue;
    }

    public void setOxygenValue(float oxygenValue) {
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

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
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

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Boolean getHasBreathlessness() {
        return hasBreathlessness;
    }

    public void setHasBreathlessness(Boolean hasBreathlessness) {
        this.hasBreathlessness = hasBreathlessness;
    }

    public Boolean getHasHighBloodPressure() {
        return hasHighBloodPressure;
    }

    public void setHasHighBloodPressure(Boolean hasHighBloodPressure) {
        this.hasHighBloodPressure = hasHighBloodPressure;
    }

    public Boolean getHasAsthma() {
        return hasAsthma;
    }

    public void setHasAsthma(Boolean hasAsthma) {
        this.hasAsthma = hasAsthma;
    }

    public Boolean getHasDiabetes() {
        return hasDiabetes;
    }

    public void setHasDiabetes(Boolean hasDiabetes) {
        this.hasDiabetes = hasDiabetes;
    }

    public Boolean getHasHeartCondition() {
        return hasHeartCondition;
    }

    public void setHasHeartCondition(Boolean hasHeartCondition) {
        this.hasHeartCondition = hasHeartCondition;
    }

    public Boolean getPregnant() {
        return isPregnant;
    }

    public void setPregnant(Boolean pregnant) {
        isPregnant = pregnant;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCheckSumWhenScannedFromDevice() {
        return checkSumWhenScannedFromDevice;
    }

    public void setCheckSumWhenScannedFromDevice(String checkSumWhenScannedFromDevice) {
        this.checkSumWhenScannedFromDevice = checkSumWhenScannedFromDevice;
    }
}
