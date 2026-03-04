
package com.argusoft.medplat.nutrition.dto;

import java.util.Date;
import java.util.Set;

/**
 * <p>
 *     Defines fields for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) admission
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
public class ChildCmtcNrcAdmissionDto {

    private Integer id;
    private Integer childId;
    private Integer caseId;
    private Integer screeningId;
    private Integer screeningCenter;
    private Integer referredBy;
    private Boolean medicalOfficerVisit;
    private Boolean specialistPediatricianVisit;
    private Date admissionDate;
    private String apetiteTest;
    private String bilateralPittingOedema;
    private String typeOfAdmission;
    private Float weightAtAdmission;
    private Integer height;
    private Float midUpperArmCircumference;
    private Set<String> illness;
    private String otherIllness;
    private String sdScore;
    private String state;
    private Date deathDate;
    private String deathReason;
    private String otherDeathReason;
    private String deathPlace;
    private String otherDeathPlace;
    private Date defaulterDate;
    private Boolean isDirectAdmission;
    private String locationId;
    private Boolean breastFeeding;
    private Boolean complementaryFeeding;
    private Boolean problemInBreastFeeding;
    private Boolean problemInMilkInjection;
    private Boolean visibleWasting;
    private Boolean kmcProvided;
    private Integer noOfTimesKmcDone;
    private Integer noOfTimesAmoxicillinGiven;
    private Boolean consecutive3DaysWeightGain;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(Integer screeningId) {
        this.screeningId = screeningId;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public Integer getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(Integer referredBy) {
        this.referredBy = referredBy;
    }

    public Boolean getMedicalOfficerVisit() {
        return medicalOfficerVisit;
    }

    public void setMedicalOfficerVisit(Boolean medicalOfficerVisit) {
        this.medicalOfficerVisit = medicalOfficerVisit;
    }

    public Boolean getSpecialistPediatricianVisit() {
        return specialistPediatricianVisit;
    }

    public void setSpecialistPediatricianVisit(Boolean specialistPediatricianVisit) {
        this.specialistPediatricianVisit = specialistPediatricianVisit;
    }

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getApetiteTest() {
        return apetiteTest;
    }

    public void setApetiteTest(String apetiteTest) {
        this.apetiteTest = apetiteTest;
    }

    public String getBilateralPittingOedema() {
        return bilateralPittingOedema;
    }

    public void setBilateralPittingOedema(String bilateralPittingOedema) {
        this.bilateralPittingOedema = bilateralPittingOedema;
    }

    public String getTypeOfAdmission() {
        return typeOfAdmission;
    }

    public void setTypeOfAdmission(String typeOfAdmission) {
        this.typeOfAdmission = typeOfAdmission;
    }

    public Float getWeightAtAdmission() {
        return weightAtAdmission;
    }

    public void setWeightAtAdmission(Float weightAtAdmission) {
        this.weightAtAdmission = weightAtAdmission;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Float getMidUpperArmCircumference() {
        return midUpperArmCircumference;
    }

    public void setMidUpperArmCircumference(Float midUpperArmCircumference) {
        this.midUpperArmCircumference = midUpperArmCircumference;
    }

    public String getSdScore() {
        return sdScore;
    }

    public void setSdScore(String sdScore) {
        this.sdScore = sdScore;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public String getDeathReason() {
        return deathReason;
    }

    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }

    public String getDeathPlace() {
        return deathPlace;
    }

    public void setDeathPlace(String deathPlace) {
        this.deathPlace = deathPlace;
    }

    public String getOtherDeathReason() {
        return otherDeathReason;
    }

    public void setOtherDeathReason(String otherDeathReason) {
        this.otherDeathReason = otherDeathReason;
    }

    public Date getDefaulterDate() {
        return defaulterDate;
    }

    public void setDefaulterDate(Date defaulterDate) {
        this.defaulterDate = defaulterDate;
    }

    public String getOtherIllness() {
        return otherIllness;
    }

    public void setOtherIllness(String otherIllness) {
        this.otherIllness = otherIllness;
    }

    public String getOtherDeathPlace() {
        return otherDeathPlace;
    }

    public void setOtherDeathPlace(String otherDeathPlace) {
        this.otherDeathPlace = otherDeathPlace;
    }

    public Set<String> getIllness() {
        return illness;
    }

    public void setIllness(Set<String> illness) {
        this.illness = illness;
    }

    public Boolean getIsDirectAdmission() {
        return isDirectAdmission;
    }

    public void setIsDirectAdmission(Boolean isDirectAdmission) {
        this.isDirectAdmission = isDirectAdmission;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public Integer getScreeningCenter() {
        return screeningCenter;
    }

    public void setScreeningCenter(Integer screeningCenter) {
        this.screeningCenter = screeningCenter;
    }

    public Boolean getBreastFeeding() {
        return breastFeeding;
    }

    public void setBreastFeeding(Boolean breastFeeding) {
        this.breastFeeding = breastFeeding;
    }

    public Boolean getComplementaryFeeding() {
        return complementaryFeeding;
    }

    public void setComplementaryFeeding(Boolean complementaryFeeding) {
        this.complementaryFeeding = complementaryFeeding;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public Boolean getProblemInBreastFeeding() {
        return problemInBreastFeeding;
    }

    public void setProblemInBreastFeeding(Boolean problemInBreastFeeding) {
        this.problemInBreastFeeding = problemInBreastFeeding;
    }

    public Boolean getProblemInMilkInjection() {
        return problemInMilkInjection;
    }

    public void setProblemInMilkInjection(Boolean problemInMilkInjection) {
        this.problemInMilkInjection = problemInMilkInjection;
    }

    public Boolean getVisibleWasting() {
        return visibleWasting;
    }

    public void setVisibleWasting(Boolean visibleWasting) {
        this.visibleWasting = visibleWasting;
    }

    public Boolean getKmcProvided() {
        return kmcProvided;
    }

    public void setKmcProvided(Boolean kmcProvided) {
        this.kmcProvided = kmcProvided;
    }

    public Integer getNoOfTimesKmcDone() {
        return noOfTimesKmcDone;
    }

    public void setNoOfTimesKmcDone(Integer noOfTimesKmcDone) {
        this.noOfTimesKmcDone = noOfTimesKmcDone;
    }

    public Integer getNoOfTimesAmoxicillinGiven() {
        return noOfTimesAmoxicillinGiven;
    }

    public void setNoOfTimesAmoxicillinGiven(Integer noOfTimesAmoxicillinGiven) {
        this.noOfTimesAmoxicillinGiven = noOfTimesAmoxicillinGiven;
    }

    public Boolean getConsecutive3DaysWeightGain() {
        return consecutive3DaysWeightGain;
    }

    public void setConsecutive3DaysWeightGain(Boolean consecutive3DaysWeightGain) {
        this.consecutive3DaysWeightGain = consecutive3DaysWeightGain;
    }
}
