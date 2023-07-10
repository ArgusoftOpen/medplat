package com.argusoft.medplat.rch.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Define rch_asha_child_service_master entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_asha_child_service_master")
public class AshaChildServiceMaster extends VisitCommonFields implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "service_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date serviceDate;

    @Column(name = "member_status")
    private String memberStatus;

    @Column(name = "service_place")
    private String servicePlace;

    @Column(name = "type_of_hospital")
    private Integer typeOfHospital;

    @Column(name = "health_infrastructure_id")
    private Integer healthInfrastructureId;

    @Column(name = "death_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date deathDate;

    @Column(name = "death_place")
    private String deathPlace;

    @Column(name = "death_reason")
    private String deathReason;

    @Column(name = "other_death_reason")
    private String otherDeathReason;

    @Column(name = "unable_to_drink_breastfeed")
    private Boolean unableToDrinkBreastfeed;

    @Column(name = "vomit_everything")
    private Boolean vomitEverything;

    @Column(name = "have_convulsions")
    private Boolean haveConvulsions;

    @Column(name = "have_cough_or_fast_breathing")
    private Boolean haveCoughOrFastBreathing;

    @Column(name = "cough_days")
    private Integer coughDays;

    @Column(name = "breath_in_one_minute")
    private Integer breathInOneMinute;

    @Column(name = "have_chest_indrawing")
    private Boolean haveChestIndrawing;

    @Column(name = "have_more_stools_diarrhea")
    private Boolean haveMoreStoolsDiarrhea;

    @Column(name = "diarrhea_days")
    private Integer diarrheaDays;

    @Column(name = "blood_in_stools")
    private Boolean bloodInStools;

    @Column(name = "sunken_eyes")
    private Boolean sunkenEyes;

    @Column(name = "irritable_or_restless")
    private Boolean irritableOrRestless;

    @Column(name = "lethargic_or_unconsious")
    private Boolean lethargicOrUnconsious;

    @Column(name = "skin_behaviour_after_pinching")
    private String skinBehaviourAfterPinching;

    @Column(name = "drinking_behaviour")
    private String drinkingBehaviour;

    @Column(name = "have_fever")
    private Boolean haveFever;

    @Column(name = "fever_days")
    private Integer feverDays;

    @Column(name = "fever_present_each_day")
    private Boolean feverPresentEachDay;

    @Column(name = "is_neck_stiff")
    private Boolean isNeckStiff;

    @Column(name = "have_fever_on_service")
    private Boolean haveFeverOnService;

    @Column(name = "have_palmer_poller")
    private String havePalmerPoller;

    @Column(name = "have_severe_wasting")
    private Boolean haveSevereWasting;

    @Column(name = "have_edema_both_feet")
    private Boolean haveEdemaBothFeet;

    @Column(name = "weight", columnDefinition = "numeric", precision = 7, scale = 3)
    private Float weight;

    @Column(name = "date_of_weight")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateOfWeight;

    @Column(name = "mother_informed_about_grade")
    private Boolean motherInformedAboutGrade;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getServicePlace() {
        return servicePlace;
    }

    public void setServicePlace(String servicePlace) {
        this.servicePlace = servicePlace;
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

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public String getDeathPlace() {
        return deathPlace;
    }

    public void setDeathPlace(String deathPlace) {
        this.deathPlace = deathPlace;
    }

    public String getDeathReason() {
        return deathReason;
    }

    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }

    public String getOtherDeathReason() {
        return otherDeathReason;
    }

    public void setOtherDeathReason(String otherDeathReason) {
        this.otherDeathReason = otherDeathReason;
    }

    public Boolean getUnableToDrinkBreastfeed() {
        return unableToDrinkBreastfeed;
    }

    public void setUnableToDrinkBreastfeed(Boolean unableToDrinkBreastfeed) {
        this.unableToDrinkBreastfeed = unableToDrinkBreastfeed;
    }

    public Boolean getVomitEverything() {
        return vomitEverything;
    }

    public void setVomitEverything(Boolean vomitEverything) {
        this.vomitEverything = vomitEverything;
    }

    public Boolean getHaveConvulsions() {
        return haveConvulsions;
    }

    public void setHaveConvulsions(Boolean haveConvulsions) {
        this.haveConvulsions = haveConvulsions;
    }

    public Boolean getHaveCoughOrFastBreathing() {
        return haveCoughOrFastBreathing;
    }

    public void setHaveCoughOrFastBreathing(Boolean haveCoughOrFastBreathing) {
        this.haveCoughOrFastBreathing = haveCoughOrFastBreathing;
    }

    public Integer getCoughDays() {
        return coughDays;
    }

    public void setCoughDays(Integer coughDays) {
        this.coughDays = coughDays;
    }

    public Integer getBreathInOneMinute() {
        return breathInOneMinute;
    }

    public void setBreathInOneMinute(Integer breathInOneMinute) {
        this.breathInOneMinute = breathInOneMinute;
    }

    public Boolean getHaveChestIndrawing() {
        return haveChestIndrawing;
    }

    public void setHaveChestIndrawing(Boolean haveChestIndrawing) {
        this.haveChestIndrawing = haveChestIndrawing;
    }

    public Boolean getHaveMoreStoolsDiarrhea() {
        return haveMoreStoolsDiarrhea;
    }

    public void setHaveMoreStoolsDiarrhea(Boolean haveMoreStoolsDiarrhea) {
        this.haveMoreStoolsDiarrhea = haveMoreStoolsDiarrhea;
    }

    public Integer getDiarrheaDays() {
        return diarrheaDays;
    }

    public void setDiarrheaDays(Integer diarrheaDays) {
        this.diarrheaDays = diarrheaDays;
    }

    public Boolean getBloodInStools() {
        return bloodInStools;
    }

    public void setBloodInStools(Boolean bloodInStools) {
        this.bloodInStools = bloodInStools;
    }

    public Boolean getSunkenEyes() {
        return sunkenEyes;
    }

    public void setSunkenEyes(Boolean sunkenEyes) {
        this.sunkenEyes = sunkenEyes;
    }

    public Boolean getIrritableOrRestless() {
        return irritableOrRestless;
    }

    public void setIrritableOrRestless(Boolean irritableOrRestless) {
        this.irritableOrRestless = irritableOrRestless;
    }

    public Boolean getLethargicOrUnconsious() {
        return lethargicOrUnconsious;
    }

    public void setLethargicOrUnconsious(Boolean lethargicOrUnconsious) {
        this.lethargicOrUnconsious = lethargicOrUnconsious;
    }

    public String getSkinBehaviourAfterPinching() {
        return skinBehaviourAfterPinching;
    }

    public void setSkinBehaviourAfterPinching(String skinBehaviourAfterPinching) {
        this.skinBehaviourAfterPinching = skinBehaviourAfterPinching;
    }

    public String getDrinkingBehaviour() {
        return drinkingBehaviour;
    }

    public void setDrinkingBehaviour(String drinkingBehaviour) {
        this.drinkingBehaviour = drinkingBehaviour;
    }

    public Boolean getHaveFever() {
        return haveFever;
    }

    public void setHaveFever(Boolean haveFever) {
        this.haveFever = haveFever;
    }

    public Integer getFeverDays() {
        return feverDays;
    }

    public void setFeverDays(Integer feverDays) {
        this.feverDays = feverDays;
    }

    public Boolean getFeverPresentEachDay() {
        return feverPresentEachDay;
    }

    public void setFeverPresentEachDay(Boolean feverPresentEachDay) {
        this.feverPresentEachDay = feverPresentEachDay;
    }

    public Boolean getIsNeckStiff() {
        return isNeckStiff;
    }

    public void setIsNeckStiff(Boolean isNeckStiff) {
        this.isNeckStiff = isNeckStiff;
    }

    public Boolean getHaveFeverOnService() {
        return haveFeverOnService;
    }

    public void setHaveFeverOnService(Boolean haveFeverOnService) {
        this.haveFeverOnService = haveFeverOnService;
    }

    public String getHavePalmerPoller() {
        return havePalmerPoller;
    }

    public void setHavePalmerPoller(String havePalmerPoller) {
        this.havePalmerPoller = havePalmerPoller;
    }

    public Boolean getHaveSevereWasting() {
        return haveSevereWasting;
    }

    public void setHaveSevereWasting(Boolean haveSevereWasting) {
        this.haveSevereWasting = haveSevereWasting;
    }

    public Boolean getHaveEdemaBothFeet() {
        return haveEdemaBothFeet;
    }

    public void setHaveEdemaBothFeet(Boolean haveEdemaBothFeet) {
        this.haveEdemaBothFeet = haveEdemaBothFeet;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Date getDateOfWeight() {
        return dateOfWeight;
    }

    public void setDateOfWeight(Date dateOfWeight) {
        this.dateOfWeight = dateOfWeight;
    }

    public Boolean getMotherInformedAboutGrade() {
        return motherInformedAboutGrade;
    }

    public void setMotherInformedAboutGrade(Boolean motherInformedAboutGrade) {
        this.motherInformedAboutGrade = motherInformedAboutGrade;
    }

}
