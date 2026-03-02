/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.morbidities.beans;

import androidx.annotation.NonNull;

/**
 * @author alpeshkyada
 */
public class DataBeanToIdentifyChildCareMorbidities {

    private String beneficiaryName = "";
    private String beneficiaryType = "";
    //    (Is the child not able to drink or breast feeding
    private Boolean childNotAbleToDrinkOrBreastfeed;//(CCHV 5) change name in phase 2
    //    Is the child vomits everything
    private Boolean childVomitsEverything;//(CCHV 6)
    //    (Has the child had convulsion
    private Boolean childHadConvulsion;//(CCHV 7)
    //    Is the child lethargic or Unconscious?
    private Boolean isChildLethargicOrUnconscious;//(CCHV 8)
    //    lethargic or Unconscious?
    private Boolean lethargicOrUnconscious;//(CCHV 18)
    //    Does the child have Chest Indrawing?
    private Boolean childHaveChestIndrawing;//(CCHV 12)
    //    Does the child have cough or difficult Breathing?
    private Boolean doesChildHaveCoughOrDifficultBreathing;//(CCHV 9)
    //    Sunken eyes
    private Boolean sunkenEyes;//(CCHV 16)
    //    Is the neck stiff?
    private Boolean theNeckStiff;//(CCHV 25)
    //    Visible severe wasting
    private Boolean visibleSevereWasting;//(CCHV 27)
    //    Edema of both feet
    private Boolean edemaOfBothFeet;//(CCHV 28)
    //    Restless or irritable
    private Boolean restlessOrIrritable;//(CCHV 17)
    //    Blood in stools
    private Boolean bloodInStools;//(CCHV 15)
    // Does the child have fever?     This variable included on 11 july 2013 due to condition change.
    private Boolean feverFlag;//(CCHV 21)
    //    (If more than 7 days,has fever been present each day?
    private Boolean ifMoreThan7DaysHasFeverBeenPresentEachDay;//(CCHV 23)    
    //    discuss in days or in months
    private Long ageOfChild;//(POV )
    //    Respiratory rate
    private Integer respiratoryRate;//(CCHV 11) Both are same numberOfBreathsIn1Minute ..Dont confuse
    //    Since how many days (Does not getting exact wat is the parameter associated to this field)
    private Integer sinceHowManyDaysOfCough;//(CCHV 10)
    private Integer sinceHowManyDaysOfStools;//(CCHV 14)
    private Integer sinceHowManyDaysOfFever;//(CCHV 22)
    //    Count the number of breaths in one minute
    private Integer numberOfBreathsIn1Minute;//(CCHV 11)
    //    Measure axillary temperature Note:-(This value is in celcius.Also if temprature is not measure than null)
    private Float measureAxillaryTemperatureIfFeverNo;//(CCHV 26)
    //    Measure axillary temperature Note:-(This value is in celcius.Also if temprature is not measure than null)
    private Float measureAxillaryTemperatureIfFeverYes;//(CCHV 24)
    //    How does he/she drinks it?
    private String howChildDrinks;//(CCHV 20)
    //    Does skin goes back very slowly (longer than 2 seconds)?
    private String doesSkinGoesBackVerySlowly;//(CCHV 19)
    //    Malnutrition grade of the child as per the weight entered
    private String malnutritionGradeOfChild;//(CCHV 30)
    //    Does the child have palmer poller?
    private String doesTheChildHavePalmerPoller;//(CCHV 32)
    private Float weightOfChild;
    //Added in phase-II and (Very severe febrile illness and malaria is depended on this in new phase-II condition)
    private Boolean touchChildSkinOnAbdomenDoesItHaveFever;//CCHV 116

    public Boolean isTouchChildSkinOnAbdomenDoesItHaveFever() {
        return touchChildSkinOnAbdomenDoesItHaveFever;
    }

    public void setTouchChildSkinOnAbdomenDoesItHaveFever(Boolean touchChildSkinOnAbdomenDoesItHaveFever) {
        this.touchChildSkinOnAbdomenDoesItHaveFever = touchChildSkinOnAbdomenDoesItHaveFever;
    }

    public Float getWeightOfChild() {
        return weightOfChild;
    }

    public void setWeightOfChild(Float weightOfChild) {
        this.weightOfChild = weightOfChild;
    }

    public Boolean isIschildLethargicOrUnconscious() {
        return isChildLethargicOrUnconscious;
    }

    public void setIsChildLethargicOrUnconscious(Boolean isChildLethargicOrUnconscious) {
        this.isChildLethargicOrUnconscious = isChildLethargicOrUnconscious;
    }

    public Boolean isLethargicOrUnconscious() {
        return lethargicOrUnconscious;
    }

    public void setLethargicOrUnconscious(Boolean lethargicOrUnconscious) {
        this.lethargicOrUnconscious = lethargicOrUnconscious;
    }

    public Boolean isChildNOTAbleToDrinkOrBreastfeed() {
        return childNotAbleToDrinkOrBreastfeed;
    }

    public void setChildNotAbleToDrinkOrBreastfeed(Boolean childNotAbleToDrinkOrBreastfeed) {
        this.childNotAbleToDrinkOrBreastfeed = childNotAbleToDrinkOrBreastfeed;
    }

    public Boolean isChildVomitsEverything() {
        return childVomitsEverything;
    }

    public void setChildVomitsEverything(Boolean childVomitsEverything) {
        this.childVomitsEverything = childVomitsEverything;
    }

    public Boolean isChildHadConvulsion() {
        return childHadConvulsion;
    }

    public void setChildHadConvulsion(Boolean childHadConvulsion) {
        this.childHadConvulsion = childHadConvulsion;
    }

    public Boolean isChildHaveChestIndrawing() {
        return childHaveChestIndrawing;
    }

    public void setChildHaveChestIndrawing(Boolean childHaveChestIndrawing) {
        this.childHaveChestIndrawing = childHaveChestIndrawing;
    }

    public Boolean isDoeschildHaveCoughOrDifficultBreathing() {
        return doesChildHaveCoughOrDifficultBreathing;
    }

    public void setDoesChildHaveCoughOrDifficultBreathing(Boolean doesChildHaveCoughOrDifficultBreathing) {
        this.doesChildHaveCoughOrDifficultBreathing = doesChildHaveCoughOrDifficultBreathing;
    }

    public Boolean isSunkenEyes() {
        return sunkenEyes;
    }

    public void setSunkenEyes(Boolean sunkenEyes) {
        this.sunkenEyes = sunkenEyes;
    }

    public Boolean isTheNeckStiff() {
        return theNeckStiff;
    }

    public void setTheNeckStiff(Boolean theNeckStiff) {
        this.theNeckStiff = theNeckStiff;
    }

    public Boolean isVisibleSevereWasting() {
        return visibleSevereWasting;
    }

    public void setVisibleSevereWasting(Boolean visibleSevereWasting) {
        this.visibleSevereWasting = visibleSevereWasting;
    }

    public Boolean isEdemaOfBothFeet() {
        return edemaOfBothFeet;
    }

    public void setEdemaOfBothFeet(Boolean edemaOfBothFeet) {
        this.edemaOfBothFeet = edemaOfBothFeet;
    }

    public Boolean isRestlessOrIrritable() {
        return restlessOrIrritable;
    }

    public void setRestlessOrIrritable(Boolean restlessOrIrritable) {
        this.restlessOrIrritable = restlessOrIrritable;
    }

    public Boolean isBloodInStools() {
        return bloodInStools;
    }

    public void setBloodInStools(Boolean bloodInStools) {
        this.bloodInStools = bloodInStools;
    }

    public Boolean isIfMoreThan7DaysHasFeverBeenPresentEachDay() {
        return ifMoreThan7DaysHasFeverBeenPresentEachDay;
    }

    public void setIfMoreThan7DaysHasFeverBeenPresentEachDay(Boolean ifMoreThan7DaysHasFeverBeenPresentEachDay) {
        this.ifMoreThan7DaysHasFeverBeenPresentEachDay = ifMoreThan7DaysHasFeverBeenPresentEachDay;
    }

    public String getHowChildDrinks() {
        return howChildDrinks;
    }

    public void setHowChildDrinks(String howChildDrinks) {
        this.howChildDrinks = howChildDrinks;
    }

    public String getDoesSkinGoesBackVerySlowly() {
        return doesSkinGoesBackVerySlowly;
    }

    public void setDoesSkinGoesBackVerySlowly(String doesSkinGoesBackVerySlowly) {
        this.doesSkinGoesBackVerySlowly = doesSkinGoesBackVerySlowly;
    }

    public String getMalnutritionGradeOfChild() {
        return malnutritionGradeOfChild;
    }

    public void setMalnutritionGradeOfChild(String malnutritionGradeOfChild) {
        this.malnutritionGradeOfChild = malnutritionGradeOfChild;
    }

    public String getDoesTheChildHavePalmerPoller() {
        return doesTheChildHavePalmerPoller;
    }

    public void setDoesTheChildHavePalmerPoller(String doesTheChildHavePalmerPoller) {
        this.doesTheChildHavePalmerPoller = doesTheChildHavePalmerPoller;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getBeneficiaryType() {
        return beneficiaryType;
    }

    public void setBeneficiaryType(String beneficiaryType) {
        this.beneficiaryType = beneficiaryType;
    }

    public Long getAgeOfChild() {
        return ageOfChild;
    }

    public void setAgeOfChild(Long ageOfChild) {
        this.ageOfChild = ageOfChild;
    }

    public Integer getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(Integer respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    public Integer getSinceHowManyDaysOfCough() {
        return sinceHowManyDaysOfCough;
    }

    public void setSinceHowManyDaysOfCough(Integer sinceHowManyDaysOfCough) {
        this.sinceHowManyDaysOfCough = sinceHowManyDaysOfCough;
    }

    public Integer getSinceHowManyDaysOfStools() {
        return sinceHowManyDaysOfStools;
    }

    public void setSinceHowManyDaysOfStools(Integer sinceHowManyDaysOfStools) {
        this.sinceHowManyDaysOfStools = sinceHowManyDaysOfStools;
    }

    public Integer getNumberOfBreathsIn1Minute() {
        return numberOfBreathsIn1Minute;
    }

    public void setNumberOfBreathsIn1Minute(Integer numberOfBreathsIn1Minute) {
        this.numberOfBreathsIn1Minute = numberOfBreathsIn1Minute;
    }

    public Float getMeasureAxillaryTemperatureIfFeverNo() {
        return measureAxillaryTemperatureIfFeverNo;
    }

    public void setMeasureAxillaryTemperatureIfFeverNo(Float measureAxillaryTemperatureIfFeverNo) {
        this.measureAxillaryTemperatureIfFeverNo = measureAxillaryTemperatureIfFeverNo;
    }

    public Float getMeasureAxillaryTemperatureIfFeverYes() {
        return measureAxillaryTemperatureIfFeverYes;
    }

    public void setMeasureAxillaryTemperatureIfFeverYes(Float measureAxillaryTemperatureIfFeverYes) {
        this.measureAxillaryTemperatureIfFeverYes = measureAxillaryTemperatureIfFeverYes;
    }

    public Integer getSinceHowManyDaysOfFever() {
        return sinceHowManyDaysOfFever;
    }

    public void setSinceHowManyDaysOfFever(Integer sinceHowManyDaysOfFever) {
        this.sinceHowManyDaysOfFever = sinceHowManyDaysOfFever;
    }

    public Boolean getFeverFlag() {
        return feverFlag;
    }

    public void setFeverFlag(Boolean feverFlag) {
        this.feverFlag = feverFlag;
    }

    @NonNull
    @Override
    public String toString() {
        return "DataBeanToIdentifyChildCareMorbidities{" + "beneficiaryName=" + beneficiaryName + ", beneficiaryType=" + beneficiaryType + ", childAbleToDrinkOrBreastfeed=" + childNotAbleToDrinkOrBreastfeed + ", childVomitsEverything=" + childVomitsEverything + ", childHadConvulsion=" + childHadConvulsion + ", ischildLethargicOrUnconscious=" + isChildLethargicOrUnconscious + ", lethargicOrUnconscious=" + lethargicOrUnconscious + ", childHaveChestIndrawing=" + childHaveChestIndrawing + ", doeschildHaveCoughOrDifficultBreathing=" + doesChildHaveCoughOrDifficultBreathing + ", sunkenEyes=" + sunkenEyes + ", theNeckStiff=" + theNeckStiff + ", visibleSevereWasting=" + visibleSevereWasting + ", edemaOfBothFeet=" + edemaOfBothFeet + ", restlessOrIrritable=" + restlessOrIrritable + ", bloodInStools=" + bloodInStools + ", ifMoreThan7DaysHasFeverBeenPresentEachDay=" + ifMoreThan7DaysHasFeverBeenPresentEachDay + ", ageOfChild=" + ageOfChild + ", respiratoryRate=" + respiratoryRate + ", sinceHowManyDaysOfCough=" + sinceHowManyDaysOfCough + ", sinceHowManyDaysOfStools=" + sinceHowManyDaysOfStools + ", sinceHowManyDaysOfFever=" + sinceHowManyDaysOfFever + ", numberOfBreathsIn1Minute=" + numberOfBreathsIn1Minute + ", measureAxillaryTemperatureInVerySevereFebrileIllness=" + measureAxillaryTemperatureIfFeverNo + ", measureAxillaryTemperatureInMalaria=" + measureAxillaryTemperatureIfFeverYes + ", howChildDrinks=" + howChildDrinks + ", doesSkinGoesBackVerySlowly=" + doesSkinGoesBackVerySlowly + ", malnutritionGradeOfChild=" + malnutritionGradeOfChild + ", doestheChildHavePalmerPoller=" + doesTheChildHavePalmerPoller + ", weightOfChild=" + weightOfChild + '}';
    }
}
