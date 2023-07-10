package com.argusoft.sewa.android.app.databean;

import java.util.Map;
import java.util.Set;

/**
 * Created by kunjan on 06-01-2019.
 */

public class MemberAdditionalInfoDataBean {

    private Integer height;

    private String hbsagTest;

    private String hivTest;

    private Integer ancIfa;

    private Integer pncIfa;

    private Integer pncCalcium;

    private String vdrlTest;

    private String sickleCellTest;

    private Integer bloodSugar;

    private Integer systolicBp;

    private Integer diastolicBp;

    private Boolean suspectedForOralCancer;

    private Boolean suspectedForCervicalCancer;

    private Boolean suspectedForBreastCancer;

    private Boolean suspectedForDiabetes;

    private Boolean suspectedForHypertension;

    private Boolean suspectedForMentalHealth;

    private Set<String> cpNegativeQues; //Que Ids Of Cerebral Palsy Questions Where The Answer Was 'No'

    private Long lastServiceLongDate;

    private String cpState;

    private Boolean phoneVerified;

    private Long lastSamScreeningDate;

    private String ancAshaMorbidity;

    private Map<Long, Float> weightMap;

    private String ncdConfFor;

    private String npcbStatus;

    private String wtGainStatus;

    private Integer givenRUTF;

    private String highRiskReasons;

    private Long lastTHRServiceDate;

    private Float bmi;

    private Float weight;

    private String diseaseHistory;

    private String otherDiseaseHistory;

    private String riskFactor;

    private String mentalHealthObservation;

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
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

    public Integer getAncIfa() {
        return ancIfa;
    }

    public void setAncIfa(Integer ancIfa) {
        this.ancIfa = ancIfa;
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

    public Integer getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(Integer bloodSugar) {
        this.bloodSugar = bloodSugar;
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

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Boolean getSuspectedForDiabetes() {
        return suspectedForDiabetes;
    }

    public void setSuspectedForDiabetes(Boolean suspectedForDiabetes) {
        this.suspectedForDiabetes = suspectedForDiabetes;
    }

    public Boolean getSuspectedForHypertension() {
        return suspectedForHypertension;
    }

    public void setSuspectedForHypertension(Boolean suspectedForHypertension) {
        this.suspectedForHypertension = suspectedForHypertension;
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
}
