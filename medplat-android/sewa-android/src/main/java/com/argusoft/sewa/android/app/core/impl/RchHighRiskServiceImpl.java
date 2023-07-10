package com.argusoft.sewa.android.app.core.impl;

import androidx.core.content.ContextCompat;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RchConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.core.RchHighRiskService;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.ListValueBean;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.Map;

@EBean(scope = EBean.Scope.Singleton)
public class RchHighRiskServiceImpl implements RchHighRiskService {

    @OrmLiteDao(helper = DBConnection.class)
    public Dao<ListValueBean, Integer> listValueBeanDao;

    @Override
    public String identifyHighRiskForRchAnc(Map<String, Object> mapOfAnswers) {
        Object bpAnswer = mapOfAnswers.get("bpAnswer");
        Object weightAnswer = mapOfAnswers.get("weightAnswer");
        Object haemoglobinAnswer = mapOfAnswers.get("haemoglobinAnswer");
        Object dangerousSignAnswer = mapOfAnswers.get("dangerousSignAnswer");
        Object otherDangerousSignAnswer = mapOfAnswers.get("otherDangerousSignAnswer");
        Object urineAlbuminAnswer = mapOfAnswers.get("urineAlbuminAnswer");
        Object urineSugarAnswer = mapOfAnswers.get("urineSugarAnswer");
        Object sickleCellTestAnswer = mapOfAnswers.get("sickleCellTestAnswer");
        Object previousComplicationsAnswer = mapOfAnswers.get("previousComplicationsAnswer");
        Object otherPreviousComplicationsAnswer = mapOfAnswers.get("otherPreviousComplicationsAnswer");

        StringBuilder highRiskFound = new StringBuilder();

        if (bpAnswer != null && bpAnswer.toString().contains("F")) {
            String[] bpAnswerArray = bpAnswer.toString().split("-");
            if (bpAnswerArray.length == 3) {
                int systolic = Integer.parseInt(bpAnswerArray[1]);
                int diastolic = Integer.parseInt(bpAnswerArray[2]);
                if (systolic > 139 || diastolic > 89) {
                    highRiskFound.append(UtilBean.getMyLabel(RchConstants.HIGH_RISK_HIGH_BLOOD_PRESSURE));
                    highRiskFound.append("\n");
                }
            }
        }

        if (haemoglobinAnswer != null && haemoglobinAnswer.toString().length() > 0 && Float.parseFloat(haemoglobinAnswer.toString()) < 7) {
            highRiskFound.append(UtilBean.getMyLabel(RchConstants.HIGH_RISK_LOW_HAEMOGLOBIN));
            highRiskFound.append("\n");
        }

        if (weightAnswer != null && weightAnswer.toString().length() > 0 && Float.parseFloat(weightAnswer.toString()) < 45f) {
            highRiskFound.append(UtilBean.getMyLabel(RchConstants.HIGH_RISK_VERY_LOW_WEIGHT));
            highRiskFound.append("\n");
        }

        if (urineAlbuminAnswer != null && !urineAlbuminAnswer.toString().equals("0")) {
            highRiskFound.append(UtilBean.getMyLabel(RchConstants.HIGH_RISK_URINE_ALBUMIN));
            highRiskFound.append("\n");
        }

        if (urineSugarAnswer != null && !urineSugarAnswer.toString().equals("0")) {
            highRiskFound.append(UtilBean.getMyLabel(RchConstants.HIGH_RISK_URINE_SUGAR));
            highRiskFound.append("\n");
        }

        if (dangerousSignAnswer != null) {
            for (String risk : dangerousSignAnswer.toString().split(",")) {
                if (!risk.contains(RchConstants.OTHER) && !risk.contains(RchConstants.NONE)) {
                    try {
                        highRiskFound.append(UtilBean.getMyLabel(
                                listValueBeanDao.queryBuilder().where().eq(FieldNameConstants.ID_OF_VALUE, risk).queryForFirst().getValue()));
                        highRiskFound.append("\n");
                    } catch (SQLException e) {
                        Log.e(getClass().getSimpleName(), null, e);
                    }
                } else if (risk.contains(RchConstants.OTHER) && otherDangerousSignAnswer != null) {
                    highRiskFound.append(otherDangerousSignAnswer);
                    highRiskFound.append("\n");
                }
            }
        }

        if (sickleCellTestAnswer != null && sickleCellTestAnswer.toString().equals("POSITIVE")) {
            highRiskFound.append(UtilBean.getMyLabel(RchConstants.HIGH_RISK_SICKLE_CELL));
            highRiskFound.append("\n");
        }

        if (previousComplicationsAnswer != null) {
            for (String risk : previousComplicationsAnswer.toString().split(",")) {
                if (!risk.contains(RchConstants.OTHER) && !risk.contains(RchConstants.NONE) && !risk.contains(RchConstants.NOT_KNOWN)) {
                    //to display values instead of keys
                    switch (risk) {
                        case "PLPRE":
                            highRiskFound.append("Placenta previa");
                            break;
                        case "PRETRM":
                            highRiskFound.append("Pre term");
                            break;
                        case "CONVLS":
                            highRiskFound.append("Convulsion");
                            break;
                        case "MLPRST":
                            highRiskFound.append("Malpresentation");
                            break;
                        case "PRELS":
                            highRiskFound.append("Previous LSCS");
                            break;
                        case "TWINS":
                            highRiskFound.append("Twins");
                            break;
                        case "SBRTH":
                            highRiskFound.append("Still birth");
                            break;
                        case "P2ABO":
                            highRiskFound.append("Previous 2 abortions");
                            break;
                        case "KCOSCD":
                            highRiskFound.append("Known case of sickle cell disease");
                            break;
                        case "CONGDEF":
                            highRiskFound.append("Congenital Defects");
                            break;
                        case "SEVANM":
                            highRiskFound.append("Severe Anemia");
                            break;
                        case "OBSLBR":
                            highRiskFound.append("Obstructed Labour");
                            break;
                        case "CAESARIAN":
                            highRiskFound.append("Caesarian Section");
                            break;
                        default:
                            highRiskFound.append(risk);
                            break;
                    }
                    highRiskFound.append("\n");
                } else if (risk.contains(RchConstants.OTHER) && otherPreviousComplicationsAnswer != null) {
                    highRiskFound.append(otherPreviousComplicationsAnswer);
                    highRiskFound.append("\n");
                }
            }
        }

        if (highRiskFound.length() > 0) {
            return highRiskFound.toString();
        }

        return UtilBean.getMyLabel(RchConstants.NO_RISK_FOUND);
    }

    @Override
    public String identifyHighRiskForChildRchPnc(Object dangerousSignAnswer, Object otherDangerousSignAnswer) {

        StringBuilder highRiskFound = new StringBuilder();

        if (dangerousSignAnswer != null) {
            for (String risk : dangerousSignAnswer.toString().split(",")) {
                if (!risk.contains(RchConstants.OTHER) && !risk.contains(RchConstants.NONE)) {
                    try {
                        highRiskFound.append(UtilBean.getMyLabel(listValueBeanDao.queryBuilder()
                                .where().eq("idOfValue", risk).queryForFirst().getValue()));
                        highRiskFound.append("\n");
                    } catch (SQLException e) {
                        Log.e(getClass().getSimpleName(), null, e);
                    }
                } else if (risk.contains(RchConstants.OTHER) && otherDangerousSignAnswer != null) {
                    highRiskFound.append(otherDangerousSignAnswer);
                    highRiskFound.append("\n");
                }
            }
        }

        if (highRiskFound.length() > 0) {
            return highRiskFound.toString();
        }

        return UtilBean.getMyLabel(RchConstants.NO_RISK_FOUND);
    }

    @Override
    public String identifyHighRiskForMotherRchPnc(Object dangerousSignAnswer, Object otherDangerousSignAnswer) {

        StringBuilder highRiskFound = new StringBuilder();

        if (dangerousSignAnswer != null) {
            for (String risk : dangerousSignAnswer.toString().split(",")) {
                if (!risk.contains(RchConstants.OTHER) && !risk.contains(RchConstants.NONE)) {
                    try {
                        highRiskFound.append(UtilBean.getMyLabel(
                                listValueBeanDao.queryBuilder().where().eq(FieldNameConstants.ID_OF_VALUE, risk).queryForFirst().getValue()));
                        highRiskFound.append("\n");
                    } catch (SQLException e) {
                        Log.e(getClass().getSimpleName(), null, e);
                    }
                } else if (risk.contains(RchConstants.OTHER) && otherDangerousSignAnswer != null) {
                    highRiskFound.append(otherDangerousSignAnswer);
                    highRiskFound.append("\n");
                }
            }
        }

        if (highRiskFound.length() > 0) {
            return highRiskFound.toString();
        }

        return UtilBean.getMyLabel(RchConstants.NO_RISK_FOUND);
    }

    @Override
    public String identifyHighRiskForChildRchWpd(Object weightAnswer) {

        StringBuilder highRiskFound = new StringBuilder();

        if (weightAnswer != null && Float.parseFloat(weightAnswer.toString()) < 2.5f && Float.parseFloat(weightAnswer.toString()) > 0) {
            highRiskFound.append(UtilBean.getMyLabel(RchConstants.HIGH_RISK_VERY_LOW_WEIGHT));
            highRiskFound.append("\n");
        }

        if (highRiskFound.length() > 0) {
            return highRiskFound.toString();
        }

        return UtilBean.getMyLabel(RchConstants.NO_RISK_FOUND);
    }

    @Override
    public String identifyHighRiskForChardhamTourist(Map<String, Object> mapOfAnswers) {
        Object oxygenAnswer = mapOfAnswers.get("oxygenAnswer");
        Object bloodPressureAnswer = mapOfAnswers.get("bloodPressureAnswer");
        Object bloodSugarAnswer = mapOfAnswers.get("bloodSugarAnswer");
        Object temperatureAnswer = mapOfAnswers.get("temperatureAnswer");

        String age = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.CHARDHAM_MEMBER_AGE);
        String weight = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.CHARDHAM_MEMBER_WEIGHT);
        String hasBreathlessness = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.HAS_BREATHLESSNESS);
        String hasHighBp = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.HAS_HIGH_BP);
        String hasAsthma = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.HAS_ASTHMA);
        String hasDiabetes = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.HAS_DIABETES);
        String hasHeartConditions = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.HAS_HEART_CONDITIONS);
        String isChMemberPregnant = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.IS_CH_MEMBER_PREGNANT);

        String highRiskStatusOxygen = "";
        String highRiskStatusBP = "";
        String highRiskStatusBS = "";
        String highRiskStatusTemp = "";

        if (oxygenAnswer != null && oxygenAnswer.toString().length() > 0 && Integer.parseInt(oxygenAnswer.toString()) > 95) {
            highRiskStatusOxygen = LabelConstants.SCREENING_STATUS_GREEN;
        } else if (oxygenAnswer != null
                && oxygenAnswer.toString().length() > 0
                && Integer.parseInt(oxygenAnswer.toString()) <= 95
                && Integer.parseInt(oxygenAnswer.toString()) >= 90) {
            highRiskStatusOxygen = LabelConstants.SCREENING_STATUS_YELLOW;
        } else if (oxygenAnswer != null
                && oxygenAnswer.toString().length() > 0
                && Integer.parseInt(oxygenAnswer.toString()) < 90) {
            highRiskStatusOxygen = LabelConstants.SCREENING_STATUS_RED;
        }

        if (bloodPressureAnswer != null && bloodPressureAnswer.toString().contains("F")) {
            String[] bpAnswerArray = bloodPressureAnswer.toString().split("-");
            if (bpAnswerArray.length == 3) {
                int systolic = Integer.parseInt(bpAnswerArray[1]);
                int diastolic = Integer.parseInt(bpAnswerArray[2]);

                if (systolic < 140 && diastolic < 90) {
                    highRiskStatusBP = LabelConstants.SCREENING_STATUS_GREEN;
                } else if ((systolic > 170 || diastolic > 110)) {
                    highRiskStatusBP = LabelConstants.SCREENING_STATUS_RED;
                } else {
                    highRiskStatusBP = LabelConstants.SCREENING_STATUS_YELLOW;
                }
            }
        }

        if (bloodSugarAnswer != null && bloodSugarAnswer.toString().length() > 0 && Integer.parseInt(bloodSugarAnswer.toString()) <= 99) {
            highRiskStatusBS = LabelConstants.SCREENING_STATUS_GREEN;
        } else if (bloodSugarAnswer != null && bloodSugarAnswer.toString().length() > 0
                && Integer.parseInt(bloodSugarAnswer.toString()) >= 100
                && Integer.parseInt(bloodSugarAnswer.toString()) <= 125) {
            highRiskStatusBS = LabelConstants.SCREENING_STATUS_YELLOW;
        } else if (bloodSugarAnswer != null && bloodSugarAnswer.toString().length() > 0
                && Integer.parseInt(bloodSugarAnswer.toString()) >= 126) {
            highRiskStatusBS = LabelConstants.SCREENING_STATUS_RED;
        }

        if (temperatureAnswer != null && temperatureAnswer.toString().length() > 0 && Integer.parseInt(temperatureAnswer.toString()) < 100) {
            highRiskStatusTemp = LabelConstants.SCREENING_STATUS_GREEN;
        } else if (temperatureAnswer != null
                && temperatureAnswer.toString().length() > 0
                && Integer.parseInt(temperatureAnswer.toString()) >= 100
                && Integer.parseInt(temperatureAnswer.toString()) < 103) {
            highRiskStatusTemp = LabelConstants.SCREENING_STATUS_YELLOW;
        } else if (temperatureAnswer != null
                && temperatureAnswer.toString().length() > 0
                && Integer.parseInt(temperatureAnswer.toString()) >= 103) {
            highRiskStatusTemp = LabelConstants.SCREENING_STATUS_RED;
        }


        if (highRiskStatusOxygen.equalsIgnoreCase(LabelConstants.SCREENING_STATUS_RED)
                || highRiskStatusBP.equalsIgnoreCase(LabelConstants.SCREENING_STATUS_RED)
                || highRiskStatusBS.equalsIgnoreCase(LabelConstants.SCREENING_STATUS_RED)
                || highRiskStatusTemp.equalsIgnoreCase(LabelConstants.SCREENING_STATUS_RED)) {
            return LabelConstants.SCREENING_STATUS_RED;
        }

        if (highRiskStatusOxygen.equalsIgnoreCase(LabelConstants.SCREENING_STATUS_YELLOW)
                || highRiskStatusBP.equalsIgnoreCase(LabelConstants.SCREENING_STATUS_YELLOW)
                || highRiskStatusBS.equalsIgnoreCase(LabelConstants.SCREENING_STATUS_YELLOW)
                || highRiskStatusTemp.equalsIgnoreCase(LabelConstants.SCREENING_STATUS_YELLOW)
                || (age != null && Integer.parseInt(age) > 55)
                || (weight != null && Float.parseFloat(weight) > 100.00)
                || "1".equalsIgnoreCase(hasBreathlessness)
                || "1".equalsIgnoreCase(hasHighBp)
                || "1".equalsIgnoreCase(hasAsthma)
                || "1".equalsIgnoreCase(hasDiabetes)
                || "1".equalsIgnoreCase(hasHeartConditions)
                || "1".equalsIgnoreCase(isChMemberPregnant)) {
            return LabelConstants.SCREENING_STATUS_YELLOW;
        }

        if (highRiskStatusOxygen.equalsIgnoreCase(LabelConstants.SCREENING_STATUS_GREEN)
                || highRiskStatusBP.equalsIgnoreCase(LabelConstants.SCREENING_STATUS_GREEN)
                || highRiskStatusBS.equalsIgnoreCase(LabelConstants.SCREENING_STATUS_GREEN)
                || highRiskStatusTemp.equalsIgnoreCase(LabelConstants.SCREENING_STATUS_GREEN)
                || age != null && Integer.parseInt(age) <= 55) {
            return LabelConstants.SCREENING_STATUS_GREEN;
        }

        return UtilBean.getMyLabel(RchConstants.NO_RISK_FOUND);
    }
}
