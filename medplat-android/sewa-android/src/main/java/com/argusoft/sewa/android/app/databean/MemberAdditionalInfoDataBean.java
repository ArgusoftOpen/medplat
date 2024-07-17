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

    private Float haemoglobin;

    private String serviceLocation;

    private Boolean isPeriodStarted;

    private String counsellingDone;
    private Long schoolActualId;

    private Long adolescentScreeningDate;

    private Boolean isHaemoglobinMeasured;

    private Integer ifaTabTakenLastMonth;

    private Integer ifaTabTakenNow;

    private String absorbentMaterialUsed;

    private Boolean isSanitaryPadGiven;

    private Integer numberOfSanitaryPadsGiven;

    private Boolean isHavingMenstrualProblem;

    private String issueWithMenstruation;

    private Boolean isTDInjectionGiven;

    private Long tdInjectionDate;

    private Boolean isAlbandazoleGivenInLastSixMonths;

    private String currentStudyingStandard;

    private Long hypDiaMentalServiceDate;

    private Long cancerServiceDate;

    private String nutritionStatus;

    private String sdScore;

    private Boolean isTbSuspected;

    private Boolean isTbCured;

    private Boolean indexCase;
    private String rdtStatus;
    private Long fpServiceDate;
    private Boolean personallyUsingFp;
    private Long anemiaServiceDate;
    private String memberAnemiaStatus;
    private Boolean bcgSurveyStatus;
    private Boolean sickleCellSurveyCompleted; // used for UT survey form related entry check
    private Boolean bcgEligible;
    private Boolean bcgWilling;
    private Boolean bcgEligibleFilled;
    private Boolean havingBirthPlan;
    private Boolean developmentDelays;

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

    public Long getSchoolActualId() {
        return schoolActualId;
    }


    public void setSchoolActualId(Long schoolActualId) {
        this.schoolActualId = schoolActualId;
    }


    public Boolean getHaemoglobinMeasured() {
        return isHaemoglobinMeasured;
    }

    public void setHaemoglobinMeasured(Boolean haemoglobinMeasured) {
        isHaemoglobinMeasured = haemoglobinMeasured;
    }

    public Integer getIfaTabTakenLastMonth() {
        return ifaTabTakenLastMonth;
    }

    public void setIfaTabTakenLastMonth(Integer ifaTabTakenLastMonth) {
        this.ifaTabTakenLastMonth = ifaTabTakenLastMonth;
    }

    public Integer getIfaTabTakenNow() {
        return ifaTabTakenNow;
    }

    public void setIfaTabTakenNow(Integer ifaTabTakenNow) {
        this.ifaTabTakenNow = ifaTabTakenNow;
    }

    public String getAbsorbentMaterialUsed() {
        return absorbentMaterialUsed;
    }

    public void setAbsorbentMaterialUsed(String absorbentMaterialUsed) {
        this.absorbentMaterialUsed = absorbentMaterialUsed;
    }

    public Boolean getSanitaryPadGiven() {
        return isSanitaryPadGiven;
    }

    public void setSanitaryPadGiven(Boolean sanitaryPadGiven) {
        isSanitaryPadGiven = sanitaryPadGiven;
    }

    public Integer getNumberOfSanitaryPadsGiven() {
        return numberOfSanitaryPadsGiven;
    }

    public void setNumberOfSanitaryPadsGiven(Integer numberOfSanitaryPadsGiven) {
        this.numberOfSanitaryPadsGiven = numberOfSanitaryPadsGiven;
    }

    public Boolean getHavingMenstrualProblem() {
        return isHavingMenstrualProblem;
    }

    public void setHavingMenstrualProblem(Boolean havingMenstrualProblem) {
        isHavingMenstrualProblem = havingMenstrualProblem;
    }

    public String getIssueWithMenstruation() {
        return issueWithMenstruation;
    }

    public void setIssueWithMenstruation(String issueWithMenstruation) {
        this.issueWithMenstruation = issueWithMenstruation;
    }

    public Boolean getTDInjectionGiven() {
        return isTDInjectionGiven;
    }

    public void setTDInjectionGiven(Boolean TDInjectionGiven) {
        isTDInjectionGiven = TDInjectionGiven;
    }

    public Long getAdolescentScreeningDate() {
        return adolescentScreeningDate;
    }

    public void setAdolescentScreeningDate(Long adolescentScreeningDate) {
        this.adolescentScreeningDate = adolescentScreeningDate;
    }

    public Long getTdInjectionDate() {
        return tdInjectionDate;
    }

    public void setTdInjectionDate(Long tdInjectionDate) {
        this.tdInjectionDate = tdInjectionDate;
    }

    public Boolean getAlbandazoleGivenInLastSixMonths() {
        return isAlbandazoleGivenInLastSixMonths;
    }

    public void setAlbandazoleGivenInLastSixMonths(Boolean albandazoleGivenInLastSixMonths) {
        isAlbandazoleGivenInLastSixMonths = albandazoleGivenInLastSixMonths;
    }

    public Float getHaemoglobin() {
        return haemoglobin;
    }

    public void setHaemoglobin(Float haemoglobin) {
        this.haemoglobin = haemoglobin;
    }

    public String getServiceLocation() {
        return serviceLocation;
    }

    public void setServiceLocation(String serviceLocation) {
        this.serviceLocation = serviceLocation;
    }

    public Boolean getPeriodStarted() {
        return isPeriodStarted;
    }

    public void setPeriodStarted(Boolean periodStarted) {
        isPeriodStarted = periodStarted;
    }

    public String getCounsellingDone() {
        return counsellingDone;
    }

    public void setCounsellingDone(String counsellingDone) {
        this.counsellingDone = counsellingDone;
    }

    public String getCurrentStudyingStandard() {
        return currentStudyingStandard;
    }

    public void setCurrentStudyingStandard(String currentStudyingStandard) {
        this.currentStudyingStandard = currentStudyingStandard;
    }

    public Long getHypDiaMentalServiceDate() {
        return hypDiaMentalServiceDate;
    }

    public void setHypDiaMentalServiceDate(Long hypDiaMentalServiceDate) {
        this.hypDiaMentalServiceDate = hypDiaMentalServiceDate;
    }

    public Long getCancerServiceDate() {
        return cancerServiceDate;
    }

    public void setCancerServiceDate(Long cancerServiceDate) {
        this.cancerServiceDate = cancerServiceDate;
    }

    public String getNutritionStatus() {
        return nutritionStatus;
    }

    public void setNutritionStatus(String nutritionStatus) {
        this.nutritionStatus = nutritionStatus;
    }

    public String getSdScore() {
        return sdScore;
    }

    public void setSdScore(String sdScore) {
        this.sdScore = sdScore;
    }

    public Boolean getTbSuspected() {
        return isTbSuspected;
    }

    public void setTbSuspected(Boolean tbSuspected) {
        isTbSuspected = tbSuspected;
    }

    public Boolean getTbCured() {
        return isTbCured;
    }

    public void setTbCured(Boolean tbCured) {
        isTbCured = tbCured;
    }

    public Boolean getIndexCase() {
        return indexCase;
    }

    public void setIndexCase(Boolean indexCase) {
        this.indexCase = indexCase;
    }

    public Long getFpServiceDate() {
        return fpServiceDate;
    }

    public void setFpServiceDate(Long fpServiceDate) {
        this.fpServiceDate = fpServiceDate;
    }

    public Boolean getPersonallyUsingFp() {
        return personallyUsingFp;
    }

    public void setPersonallyUsingFp(Boolean personallyUsingFp) {
        this.personallyUsingFp = personallyUsingFp;
    }

    public Long getAnemiaServiceDate() {
        return anemiaServiceDate;
    }

    public void setAnemiaServiceDate(Long anemiaServiceDate) {
        this.anemiaServiceDate = anemiaServiceDate;
    }

    public String getMemberAnemiaStatus() {
        return memberAnemiaStatus;
    }

    public void setMemberAnemiaStatus(String memberAnemiaStatus) {
        this.memberAnemiaStatus = memberAnemiaStatus;
    }

    public Boolean getBcgSurveyStatus() {
        return bcgSurveyStatus;
    }

    public void setBcgSurveyStatus(Boolean bcgSurveyStatus) {
        this.bcgSurveyStatus = bcgSurveyStatus;
    }

    public Boolean getSickleCellSurveyCompleted() {
        return sickleCellSurveyCompleted;
    }

    public void setSickleCellSurveyCompleted(Boolean sickleCellSurveyCompleted) {
        this.sickleCellSurveyCompleted = sickleCellSurveyCompleted;
    }

    public Boolean getBcgEligible() {
        return bcgEligible;
    }

    public void setBcgEligible(Boolean bcgEligible) {
        this.bcgEligible = bcgEligible;
    }

    public Boolean getBcgWilling() {
        return bcgWilling;
    }

    public void setBcgWilling(Boolean bcgWilling) {
        this.bcgWilling = bcgWilling;
    }

    public String getRdtStatus() {
        return rdtStatus;
    }

    public void setRdtStatus(String rdtStatus) {
        this.rdtStatus = rdtStatus;
    }

    public Boolean getBcgEligibleFilled() {
        return bcgEligibleFilled;
    }

    public void setBcgEligibleFilled(Boolean bcgEligibleFilled) {
        this.bcgEligibleFilled = bcgEligibleFilled;
    }

    public Boolean getHavingBirthPlan() {
        return havingBirthPlan;
    }

    public void setHavingBirthPlan(Boolean havingBirthPlan) {
        this.havingBirthPlan = havingBirthPlan;
    }

    public Boolean getDevelopmentDelays() {
        return developmentDelays;
    }

    public void setDevelopmentDelays(Boolean developmentDelays) {
        this.developmentDelays = developmentDelays;
    }
}
