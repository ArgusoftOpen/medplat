/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fhs.dto;

import java.util.Map;
import java.util.Set;

/**
 *
 * <p>
 *     Used for member additional info.
 * </p>
 * @author kunjan
 * @since 26/08/20 11:00 AM
 *
 */
public class MemberAdditionalInfo {

    private Integer height;

    private Float weight;

    private Float haemoglobin;

    private Integer systolicBp;

    private Integer diastolicBp;

    private String ancBloodSugarTest;

    private Integer sugarTestAfterFoodValue;

    private Integer sugarTestBeforeFoodValue;

    private String hbsagTest;

    private String hivTest;

    private String vdrlTest;

    private String sickleCellTest;

    private Boolean albendanzoleGiven;

    private Integer ancIfa;

    private Integer ancFa;

    private Integer ancCalcium;

    private Integer pncIfa;

    private Integer pncCalcium;

    private Integer bloodSugar;

    private String expectedDeliveryPlace;

    private Set<String> previousPregnancyComplication;

    //NCD Related
    private Boolean suspectedForOralCancer;
    //NCD Related
    private Boolean suspectedForCervicalCancer;
    //NCD Related
    private Boolean suspectedForBreastCancer;
    //NCD Related
    private Boolean suspectedForHypertension;
    //NCD Related
    private Boolean suspectedForDiabetes;
    //NCD Related
    private Boolean suspectedForMentalHealth;
    //NCD Related
    private String ncdConfFor;
    //NCD Related
    private Long cbacDate;
    //NCD Related
    private String iaYear;
    private String hypYear;         //Comma Separated financial year in which HYPERTENSION Screening is done
    //NCD Related
    private String oralYear;        //Comma Separated financial year in which ORAL Screening is done
    //NCD Related
    private String diabetesYear;    //Comma Separated financial year in which DIABETES Screening is done
    //NCD Related
    private String breastYear;      //Comma Separated financial year in which BREAST Screening is done
    //NCD Related
    private String cervicalYear;    //Comma Separated financial year in which CERVICAL Screening is done
    //NCD Related
    private String mentalHealthYear;    //Comma Separated financial year in which MENTAL HEALTH Screening is done

    private String healthYear;    //Comma Separated financial year in which HEALTH Screening is done

    private Set<String> cpNegativeQues; //Que Ids Of Cerebral Palsy Questions Where The Answer Was 'No'

    private Long lastServiceLongDate;

    private String cpState;

    private Boolean phoneVerified;

    private Long lastSamScreeningDate;

    private String ancAshaMorbidity;

    private String wtGainStatus;

    private Map<Long, Float> weightMap;

    private String npcbStatus;

    private Long whatsappConsentOn;

    private Integer givenRUTF;

    private String highRiskReasons;

    private Long lastTHRServiceDate;

    private Float bmi;

    private String diseaseHistory;

    private String otherDiseaseHistory;

    private String riskFactor;

    private String mentalHealthObservation;

    private Boolean confirmedDiabetes;

    private Integer cbacScore;

    private Boolean sufferingDiabetes;

    private Boolean sufferingHypertension;

    private Boolean sufferingMentalHealth;


    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getHaemoglobin() {
        return haemoglobin;
    }

    public void setHaemoglobin(Float haemoglobin) {
        this.haemoglobin = haemoglobin;
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

    public String getAncBloodSugarTest() {
        return ancBloodSugarTest;
    }

    public void setAncBloodSugarTest(String ancBloodSugarTest) {
        this.ancBloodSugarTest = ancBloodSugarTest;
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

    public String getHbsagTest() {
        return hbsagTest;
    }

    public void setHbsagTest(String hbsagTest) {
        this.hbsagTest = hbsagTest;
    }

    public String getHivTest() {
        return hivTest;
    }

    public void setHivTest(String hivTest) {
        this.hivTest = hivTest;
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

    public Boolean getAlbendanzoleGiven() {
        return albendanzoleGiven;
    }

    public void setAlbendanzoleGiven(Boolean albendanzoleGiven) {
        this.albendanzoleGiven = albendanzoleGiven;
    }

    public Integer getAncIfa() {
        return ancIfa;
    }

    public void setAncIfa(Integer ancIfa) {
        this.ancIfa = ancIfa;
    }

    public Integer getAncFa() {
        return ancFa;
    }

    public void setAncFa(Integer ancFa) {
        this.ancFa = ancFa;
    }

    public Integer getAncCalcium() {
        return ancCalcium;
    }

    public void setAncCalcium(Integer ancCalcium) {
        this.ancCalcium = ancCalcium;
    }

    public Integer getPncIfa() {
        return pncIfa;
    }

    public void setPncIfa(Integer pncIfa) {
        this.pncIfa = pncIfa;
    }

    public Integer getPncCalcium() {
        return pncCalcium;
    }

    public void setPncCalcium(Integer pncCalcium) {
        this.pncCalcium = pncCalcium;
    }

    public Integer getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(Integer bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public String getExpectedDeliveryPlace() {
        return expectedDeliveryPlace;
    }

    public void setExpectedDeliveryPlace(String expectedDeliveryPlace) {
        this.expectedDeliveryPlace = expectedDeliveryPlace;
    }

    public String getIaYear() {
        return iaYear;
    }

    public void setIaYear(String iaYear) {
        this.iaYear = iaYear;
    }

    public Set<String> getPreviousPregnancyComplication() {
        return previousPregnancyComplication;
    }

    public void setPreviousPregnancyComplication(Set<String> previousPregnancyComplication) {
        this.previousPregnancyComplication = previousPregnancyComplication;
    }

    public Boolean getSuspectedForOralCancer() {
        return suspectedForOralCancer;
    }

    public void setSuspectedForOralCancer(Boolean suspectedForOralCancer) {
        this.suspectedForOralCancer = suspectedForOralCancer;
    }

    public Boolean getSuspectedForCervicalCancer() {
        return suspectedForCervicalCancer;
    }

    public void setSuspectedForCervicalCancer(Boolean suspectedForCervicalCancer) {
        this.suspectedForCervicalCancer = suspectedForCervicalCancer;
    }

    public Boolean getSuspectedForBreastCancer() {
        return suspectedForBreastCancer;
    }

    public void setSuspectedForBreastCancer(Boolean suspectedForBreastCancer) {
        this.suspectedForBreastCancer = suspectedForBreastCancer;
    }

    public Long getCbacDate() {
        return cbacDate;
    }

    public void setCbacDate(Long cbacDate) {
        this.cbacDate = cbacDate;
    }

    public Set<String> getCpNegativeQues() {
        return cpNegativeQues;
    }

    public void setCpNegativeQues(Set<String> cpNegativeQues) {
        this.cpNegativeQues = cpNegativeQues;
    }

    public Long getLastServiceLongDate() {
        return lastServiceLongDate;
    }

    public void setLastServiceLongDate(Long lastServiceLongDate) {
        this.lastServiceLongDate = lastServiceLongDate;
    }

    public String getCpState() {
        return cpState;
    }

    public void setCpState(String cpState) {
        this.cpState = cpState;
    }

    public Boolean getPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(Boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public Long getLastSamScreeningDate() {
        return lastSamScreeningDate;
    }

    public void setLastSamScreeningDate(Long lastSamScreeningDate) {
        this.lastSamScreeningDate = lastSamScreeningDate;
    }

    public String getAncAshaMorbidity() {
        return ancAshaMorbidity;
    }

    public void setAncAshaMorbidity(String ancAshaMorbidity) {
        this.ancAshaMorbidity = ancAshaMorbidity;
    }

    public String getNcdConfFor() {
        return ncdConfFor;
    }

    public void setNcdConfFor(String ncdConfFor) {
        this.ncdConfFor = ncdConfFor;
    }

    public Map<Long, Float> getWeightMap() {
        return weightMap;
    }

    public void setWeightMap(Map<Long, Float> weightMap) {
        this.weightMap = weightMap;
    }

    public String getNpcbStatus() {
        return npcbStatus;
    }

    public void setNpcbStatus(String npcbStatus) {
        this.npcbStatus = npcbStatus;
    }

    public Long getWhatsappConsentOn() {
        return whatsappConsentOn;
    }

    public void setWhatsappConsentOn(Long whatsappConsentOn) {
        this.whatsappConsentOn = whatsappConsentOn;
    }

    public String getWtGainStatus() {
        return wtGainStatus;
    }

    public void setWtGainStatus(String wtGainStatus) {
        this.wtGainStatus = wtGainStatus;
    }

    public Integer getGivenRUTF() {
        return givenRUTF;
    }

    public void setGivenRUTF(Integer givenRUTF) {
        this.givenRUTF = givenRUTF;
    }

    public String getHighRiskReasons() {
        return highRiskReasons;
    }

    public void setHighRiskReasons(String highRiskReasons) {
        this.highRiskReasons = highRiskReasons;
    }

    public Long getLastTHRServiceDate() {
        return lastTHRServiceDate;
    }

    public void setLastTHRServiceDate(Long lastTHRServiceDate) {
        this.lastTHRServiceDate = lastTHRServiceDate;
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

    public Float getBmi() {
        return bmi;
    }

    public void setBmi(Float bmi) {
        this.bmi = bmi;
    }

    public String getDiseaseHistory() {
        return diseaseHistory;
    }

    public void setDiseaseHistory(String diseaseHistory) {
        this.diseaseHistory = diseaseHistory;
    }

    public String getOtherDiseaseHistory() {
        return otherDiseaseHistory;
    }

    public void setOtherDiseaseHistory(String otherDiseaseHistory) {
        this.otherDiseaseHistory = otherDiseaseHistory;
    }

    public String getRiskFactor() {
        return riskFactor;
    }

    public void setRiskFactor(String riskFactor) {
        this.riskFactor = riskFactor;
    }

    public Boolean getSuspectedForHypertension() {
        return suspectedForHypertension;
    }

    public void setSuspectedForHypertension(Boolean suspectedForHypertension) {
        this.suspectedForHypertension = suspectedForHypertension;
    }

    public Boolean getSuspectedForDiabetes() {
        return suspectedForDiabetes;
    }

    public void setSuspectedForDiabetes(Boolean suspectedForDiabetes) {
        this.suspectedForDiabetes = suspectedForDiabetes;
    }

    public String getMentalHealthYear() {
        return mentalHealthYear;
    }

    public void setMentalHealthYear(String mentalHealthYear) {
        this.mentalHealthYear = mentalHealthYear;
    }

    public Boolean getSuspectedForMentalHealth() {
        return suspectedForMentalHealth;
    }

    public void setSuspectedForMentalHealth(Boolean suspectedForMentalHealth) {
        this.suspectedForMentalHealth = suspectedForMentalHealth;
    }

    public String getMentalHealthObservation() {
        return mentalHealthObservation;
    }

    public void setMentalHealthObservation(String mentalHealthObservation) {
        this.mentalHealthObservation = mentalHealthObservation;
    }

    public String getHealthYear() {
        return healthYear;
    }

    public void setHealthYear(String healthYear) {
        this.healthYear = healthYear;
    }

    public Boolean getConfirmedDiabetes() {
        return confirmedDiabetes;
    }

    public void setConfirmedDiabetes(Boolean confirmedDiabetes) {
        this.confirmedDiabetes = confirmedDiabetes;
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
}
