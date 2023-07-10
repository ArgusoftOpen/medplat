package com.argusoft.sewa.android.app.morbidities.service;

import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.morbidities.beans.DataBeanToIdentifyANCMorbidities;
import com.argusoft.sewa.android.app.morbidities.beans.IdentifiedMorbidityDetails;
import com.argusoft.sewa.android.app.morbidities.constants.MorbiditiesConstant;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.UtilBean;

import java.util.List;

/**
 * @author alpeshkyada
 */
public class ANCMorbiditiesService {

    private DataBeanToIdentifyANCMorbidities dtim;
    private List<IdentifiedMorbidityDetails> identifiedMorbidities;

    public ANCMorbiditiesService(DataBeanToIdentifyANCMorbidities databeanToIdentifyANCMorbidities, List<IdentifiedMorbidityDetails> identifiedMorbiditiesList) {
        dtim = databeanToIdentifyANCMorbidities;
        identifiedMorbidities = identifiedMorbiditiesList;

        //In all the cases it is fixed that all method must be called so it is called in constructor.
        isAph();
        isEmesisOfPregnancy();
        isFever();
        isHepatitis();
        isMalariaInPregnancy();
        isMildPregnancyInducedHypertension();
        isModerateAnemia();
        isPossibleTB();
        isPrematureRuptureOfMembrane();
        isProbableSevereAnemia();
        isSevereAnemia();
        isSeverePregnancyInducedHypertension();
        isSickleCellCrisis();
        isUpperRespiratoryTractInfection();
        isUrinaryTractInfection();
        isVaginitis();
        //Check only once after First Anc home visit (It may be from unscheduled or alert or after registration).
        if (!dtim.isIsFirstAncHomeVisitDone()) {
            isBadObstetricHistory();
            isUnintendedPregnancy();
        }
    }

    //.........................................................This is for RED morbidities............................................
    //Severe pregnancy induced hypertension morbidity
    private void isSeverePregnancyInducedHypertension() {
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        boolean isIdentified = false;
        int forRepetitiveSymptoms = 0;
        StringBuilder sb = new StringBuilder();
        // 1. Systolic Blood Pressure > 160 or Diastolic Blood Pressure  is >110
        if ((dtim.getSystolicBP() != null)
                && (dtim.getSystolicBP() > 160)) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.SYSTOLIC_BP));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(dtim.getSystolicBP().intValue());
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            isIdentified = true;
            sb.delete(0, sb.length());
        }
        if ((dtim.getDiastolicBP() != null)
                && (dtim.getDiastolicBP() > 110)) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DIASTOLIC_BP));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(dtim.getDiastolicBP().intValue());
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            isIdentified = true;
            sb.delete(0, sb.length());
        }
        // 2. Systolic Blood Pressure between 140 to 159 or Diastolic Blood Pressure is between 90 to 110 &&  Headache = Yes
        if (Boolean.TRUE.equals(dtim.isHeadache())) {
            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HEADACHE));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            if ((dtim.getSystolicBP() != null) && (UtilBean.isNotInGivenRange(dtim.getSystolicBP(), 140, 159))) {
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                sb.delete(0, sb.length());
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.SYSTOLIC_BP));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(dtim.getSystolicBP().intValue());
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                isIdentified = true;
                forRepetitiveSymptoms = 1;
                sb.delete(0, sb.length());
            }
            if ((dtim.getDiastolicBP() != null) && (UtilBean.isNotInGivenRange(dtim.getDiastolicBP(), 90, 110))) {
                if (sb.length() > 0) {
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                }
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DIASTOLIC_BP));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(dtim.getDiastolicBP().intValue());
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                isIdentified = true;
                forRepetitiveSymptoms = 1;
                sb.delete(0, sb.length());
            }
        }
        // 3 . Systolic Blood Pressure between 140 to 159 or Diastolic Blood Pressure is between 90 to 110 && Vision disturbance = Yes
        if (Boolean.TRUE.equals(dtim.isVisionDisturbance())) {
            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.VISION_DISTURBANCE));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            if ((dtim.getSystolicBP() != null) && (UtilBean.isNotInGivenRange(dtim.getSystolicBP(), 140, 159))) {
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                sb.delete(0, sb.length());
                if (forRepetitiveSymptoms != 1) {
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.SYSTOLIC_BP));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    sb.append(dtim.getSystolicBP().intValue());
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                }
                isIdentified = true;
                if (forRepetitiveSymptoms == 1) {
                    forRepetitiveSymptoms = 2;
                }
                sb.delete(0, sb.length());
            }
            if ((dtim.getDiastolicBP() != null) && (UtilBean.isNotInGivenRange(dtim.getDiastolicBP(), 90, 110))) {
                if (sb.length() > 0) {
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                }
                if (forRepetitiveSymptoms != 1) {
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DIASTOLIC_BP));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    sb.append(dtim.getDiastolicBP().intValue());
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                }
                isIdentified = true;
                if (forRepetitiveSymptoms == 1) {
                    forRepetitiveSymptoms = 2;
                }
                sb.delete(0, sb.length());
            }
        }
        // 4 . Systolic Blood Pressure between 140 to 159 or Diastolic Blood Pressure is between 90 to 110 && urine protein > +2
        if ((dtim.getUrineProtein() != null) && (dtim.getUrineProtein() == 2)) {
            if ((dtim.getSystolicBP() != null) && (UtilBean.isNotInGivenRange(dtim.getSystolicBP(), 140, 159))) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.SYSTOLIC_BP));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(dtim.getSystolicBP().intValue());
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                isIdentified = true;
                sb.delete(0, sb.length());
            }
            if ((dtim.getDiastolicBP() != null) && (UtilBean.isNotInGivenRange(dtim.getDiastolicBP(), 90, 110))) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DIASTOLIC_BP));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(dtim.getDiastolicBP().intValue());
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                isIdentified = true;
                sb.delete(0, sb.length());
            }
        }
        // 5 . Headache = Yes && visual disturbances = Yes)
        if (Boolean.TRUE.equals(dtim.isHeadache()) && Boolean.TRUE.equals(dtim.isVisionDisturbance())) {
            if (forRepetitiveSymptoms != 2) {
                sb.delete(0, sb.length());
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HEADACHE));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                sb.delete(0, sb.length());
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.VISION_DISTURBANCE));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                sb.delete(0, sb.length());

            }
            isIdentified = true;
        }
        //  6 . Urine protein 3+ or 4+ Removed in phase 3
        sb.delete(0, sb.length());
        // 7.Has convulsion = yes
        if (Boolean.TRUE.equals(dtim.getHasConvulsion())) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.CONVULSION));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            isIdentified = true;
        }

        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.SEVERE_PREGNANCY_INDUCED_HYPERTENSION, identifiedMorbidityDetails);
        }
    }

    private void isAph() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        //If "Vaginal bleeding since last visit = Yes"
        if (Boolean.TRUE.equals(dtim.isVaginalBleedingSinceLastVisit())) {
            //Symptoms
            String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.VAGINAL_BLEEDING_SINCE_LAST_VISIT) + MorbiditiesConstant.EQUALS_SYMBOL +
                    UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES);
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);
            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.APH, identifiedMorbidityDetails);
        }
    }

    //Hepatitis
    private void isHepatitis() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        //If "Jaundice = Yes"
        if (Boolean.TRUE.equals(dtim.isJaundice())) {
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.JAUNDICE) + MorbiditiesConstant.EQUALS_SYMBOL + UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            isIdentified = true;
        }

        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.HEPATITIS, identifiedMorbidityDetails);
        }
    }

    //Sickle cell crisis
    private void isSickleCellCrisis() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        //If "Severe joint pain = Yes"
        if (Boolean.TRUE.equals(dtim.isSevereJointPain())) {
            String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.SEVERE_JOINT_PAIN) + MorbiditiesConstant.EQUALS_SYMBOL +
                    UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES);
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);
            isIdentified = true;
        }

        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.SICKLE_CELL_CRISIS, identifiedMorbidityDetails);
        }
    }

    //Premature rupture of membrane
    private void isPrematureRuptureOfMembrane() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        //If "Leaking per vaginally = yes" and gestational week is between 28 and 37 weeks.
        if (dtim.getGestationalWeek() != null
                && Boolean.TRUE.equals(dtim.isLeakingPerVaginally())
                && UtilBean.isNotInGivenRange(dtim.getGestationalWeek(), 28, 37)) {
            StringBuilder symptoms = new StringBuilder();
            symptoms.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.LEAKING_PER_VAGINALLY));
            symptoms.append(MorbiditiesConstant.EQUALS_SYMBOL);
            symptoms.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(symptoms.toString());
            symptoms.delete(0, symptoms.length());
            symptoms.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.GESTATIONAL_WEEK));
            symptoms.append(MorbiditiesConstant.EQUALS_SYMBOL);
            symptoms.append(dtim.getGestationalWeek().toString());
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(symptoms.toString());
            isIdentified = true;
        }

        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.PREMATURE_RUPTURE_OF_MEMBRANE, identifiedMorbidityDetails);
        }
    }

    //Severe anemia
    private void isSevereAnemia() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        //If "Haemoglobin is <= 7" [Hb is taken during mamta divas]
        if (dtim.getHaemoglobin() != null && dtim.getHaemoglobin() <= 7) {
            String symptoms = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HAEMOGLOBIN_IS) +
                    MorbiditiesConstant.EQUALS_SYMBOL +
                    dtim.getHaemoglobin().toString();
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(symptoms);
            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.SEVERE_ANEMIA, identifiedMorbidityDetails);
        }
    }

    //.........................................................This is for yellow morbidities............................................
    //Bad obstetric history
    private void isBadObstetricHistory() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();

        //If (age is > 35 or < 18 yrs)
        if (dtim.getAge() != null && (dtim.getAge() < 18 || dtim.getAge() > 35)) {
            String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.AGE_IS) +
                    MorbiditiesConstant.EQUALS_SYMBOL +
                    dtim.getAge();
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);
            isIdentified = true;
        }
        //(Gravida > 4
        if ((dtim.getGravida() != null) && (dtim.getGravida() > 4)) {
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.GRAVIDA_IS_MORE_THAN_4));
            isIdentified = true;
        }
        //(Complication present during previous pregnancy) is not equal to 'None' and 'Not known'
        if (dtim.getComplicationPresentDuringPreviousPregnancy() != null) {
            String object;
            for (int i = 0; i < dtim.getComplicationPresentDuringPreviousPregnancy().size(); i++) {
                object = dtim.getComplicationPresentDuringPreviousPregnancy().get(i);
                if (!(object.equals(MorbiditiesConstant.NONE) || object.equals(MorbiditiesConstant.NOT_KNOWN))) {
                    isIdentified = true;
                    StringBuilder sb = new StringBuilder();
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.COMPLICATION_PRESENT_DURING_PREVIOUS_PREGNANCY_IS));
                    String object1;
                    for (int j = 0; j < dtim.getComplicationPresentDuringPreviousPregnancy().size(); j++) {
                        object1 = dtim.getComplicationPresentDuringPreviousPregnancy().get(j);
                        sb.append(UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(object1)));
                        sb.append(GlobalTypes.COMMA);
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    break;
                }
            }
        }
        //Marital status is anything other than "Married"
        if ((dtim.getMaritalStatus() != null) && (!dtim.getMaritalStatus().equalsIgnoreCase(MorbiditiesConstant.MARRIED))) {
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.MARITAL_STATUS_IS) + UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(dtim.getMaritalStatus())));
            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.BAD_OBSTETRIC_HISTORY, identifiedMorbidityDetails);
        }
    }

    //Unintended pregnancy
    private void isUnintendedPregnancy() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        //If Marital status is anything other than "Married"
        if ((dtim.getMaritalStatus() != null) && (!dtim.getMaritalStatus().equalsIgnoreCase(MorbiditiesConstant.MARRIED))) {
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.MARITAL_STATUS_IS) + UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(dtim.getMaritalStatus())));
            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.UNINTENDED_PREGNANCY, identifiedMorbidityDetails);
        }
    }

    //Mild Pregnancy Induced Hypertension
    private void isMildPregnancyInducedHypertension() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        // If (systolic blood pressure > 140 or diastolic blood pressure is > 90) and
        // No headache and No vision disturbance and Urine protein anything other than "+++" or "++++"
        if (dtim.getUrineProtein() != null
                && Boolean.FALSE.equals(dtim.isHeadache())
                && Boolean.FALSE.equals(dtim.isVisionDisturbance())
                && (dtim.getSystolicBP() != null || dtim.getDiastolicBP() != null)
                && !(dtim.getUrineProtein() == 3 || dtim.getUrineProtein() == 4 || dtim.getUrineProtein() == -1)) {
            StringBuilder sb = new StringBuilder(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HEADACHE));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());

            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.VISION_DISTURBANCE));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());

            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.URINE_PROTEIN));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            if (dtim.getUrineProtein() == 1) {
                sb.append("+");
            } else if (dtim.getUrineProtein() == 2) {
                sb.append("++");
            } else {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(MorbiditiesConstant.ABSENT)));
            }
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());

            if ((dtim.getSystolicBP() > 140)) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.SYSTOLIC_BP));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(dtim.getSystolicBP().intValue());
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                isIdentified = true;
            }
            sb.delete(0, sb.length());

            if ((dtim.getDiastolicBP() > 90)) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DIASTOLIC_BP));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(dtim.getDiastolicBP().intValue());
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                isIdentified = true;
            }
        }

        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.MILD_PREGNANCY_INDUCED_HYPERTENSION, identifiedMorbidityDetails);
        }
    }

    //Malaria in pregnancy
    private void isMalariaInPregnancy() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        //If "Fever = Yes" and "Chills/Rigours = Yes" and "Burning urination = No"
        if (Boolean.TRUE.equals(dtim.isFever())
                && Boolean.TRUE.equals(dtim.isChillsOrRigours())
                && Boolean.FALSE.equals(dtim.isBurningUrination())) {
            StringBuilder sb = new StringBuilder(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.FEVER));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());

            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.CHILLS_RIGOURS));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());

            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.BURNING_URINATION));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());

            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.MALARIA_IN_PREGNANCY, identifiedMorbidityDetails);
        }
    }

    //Fever
    private void isFever() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        //If "Fever = Yes" and "Chills/Rigours = Yes" and "Burning urination = No" and "Presence of cough = No cough"
        if (dtim.getPresenceOfCough() != null
                && Boolean.TRUE.equals(dtim.isFever())
                && Boolean.TRUE.equals(dtim.isChillsOrRigours())
                && Boolean.FALSE.equals(dtim.isBurningUrination())
                && dtim.getPresenceOfCough().equalsIgnoreCase(MorbiditiesConstant.NO_COUGH)) {
            StringBuilder sb = new StringBuilder(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.FEVER));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());

            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.CHILLS_RIGOURS));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());

            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.BURNING_URINATION));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());

            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.PRESENCE_OF_COUGH));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel("No cough"));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());

            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.FEVER, identifiedMorbidityDetails);
        }
    }

    //Possible TB
    private void isPossibleTB() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        //If "Presence of cough = More than 15 days"
        if (MorbiditiesConstant.MORE_THAN_15_DAYS.equalsIgnoreCase(dtim.getPresenceOfCough())) {
            String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.PRESENCE_OF_COUGH) +
                    MorbiditiesConstant.EQUALS_SYMBOL +
                    UtilBean.getMyLabel(LabelConstants.MORE_THAN_15_DAYS);
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);
            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.POSSIBLE_TB, identifiedMorbidityDetails);
        }
    }

    //Urinary tract infection
    private void isUrinaryTractInfection() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        //If "Burning urination = Yes"
        if (Boolean.TRUE.equals(dtim.isBurningUrination())) {
            String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.BURNING_URINATION) +
                    MorbiditiesConstant.EQUALS_SYMBOL +
                    UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES);
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);

            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.URINARY_TRACT_INFECTION, identifiedMorbidityDetails);
        }
    }

    //Vaginitis
    private void isVaginitis() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        //If "Vaginal discharge is any value other than No/minimal discharge"
        if (dtim.getVaginalDischarge() != null
                && !dtim.getVaginalDischarge().equalsIgnoreCase(MorbiditiesConstant.NO_OR_MINIMAL_DISCHARGE)) {
            String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.VAGINAL_DISCHARGE) + MorbiditiesConstant.EQUALS_SYMBOL +
                    UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(dtim.getVaginalDischarge()));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);
            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.VAGINITIS, identifiedMorbidityDetails);
        }
    }

    //Probable severe anemia
    private void isProbableSevereAnemia() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        //1. [(Do you get tired easily? = Yes) and (Are you short of breath during routing household work? = Yes)]
        //or
        //2. (Is conjunctiva and palms pale? = Yes)
        StringBuilder sb = new StringBuilder();
        if ((Boolean.TRUE.equals(dtim.isDoYouGetTiredEasily())) && (Boolean.TRUE.equals(dtim.isShortOfBreathDuringRoutingHouseholdWork()))) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DO_YOU_GET_TIRED_EASILY));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.ARE_YOU_SHORT_OF_BREATH_DURING_ROUTING_HOUSEHOLD_WORK));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());
            isIdentified = true;
        }
        if ((Boolean.TRUE.equals(dtim.isConjunctivaAndPalmsPale()))) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.IS_CONJUNCTIVA_AND_PALMS_PALE));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());
            isIdentified = true;
        }
        //phase 3  //added in phase 3
        if ((Boolean.TRUE.equals(dtim.isSwellingOfFaceHandsOrFeet()))) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.SWELLING_OF_FACE_HANDS_OR_FEET));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.PROBABLE_SEVERE_ANEMIA, identifiedMorbidityDetails);
        }
    }

    //.........................................................This is for GREEN morbidities............................................
    //Emesis of pregnancy
    private void isEmesisOfPregnancy() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        //If "Vomiting = Yes"
        if (Boolean.TRUE.equals(dtim.isVomiting())) {
            String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.VOMITING) + MorbiditiesConstant.EQUALS_SYMBOL +
                    UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES);
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);
            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.EMESIS_OF_PREGNANCY, identifiedMorbidityDetails);
        }
    }

    //Upper respiratory tract infection
    private void isUpperRespiratoryTractInfection() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        //If "Presence of cough = Less than 15 days"
        if (MorbiditiesConstant.LESS_THAN_15_DAYS.equalsIgnoreCase(dtim.getPresenceOfCough())) {
            String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.PRESENCE_OF_COUGH) + MorbiditiesConstant.EQUALS_SYMBOL +
                    UtilBean.getMyLabel(LabelConstants.LESS_THAN_15_DAYS);
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);
            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.UPPER_RESPIRATORY_TRACT_INFECTION, identifiedMorbidityDetails);
        }
    }

    //Moderate anemia
    private void isModerateAnemia() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        //If "Haemoglobin is between 7 and 11" [Hb is taken during mamta divas]
        if ((dtim.getHaemoglobin() != null)
                && (UtilBean.isNotInGivenRange(dtim.getHaemoglobin(), 7, 11))) {
            String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HAEMOGLOBIN_IS)
                    + MorbiditiesConstant.EQUALS_SYMBOL
                    + dtim.getHaemoglobin();
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);
            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.MODERATE_ANEMIA, identifiedMorbidityDetails);
        }
    }

    private void addIdentifiedMorbidityIntoList(String morbidityCode, IdentifiedMorbidityDetails identifiedMorbidityDetails) {
        identifiedMorbidityDetails.setMorbidityCode(morbidityCode);
        identifiedMorbidityDetails.setRiskFactorOfIdentifiedMorbidities(MorbiditiesConstant.getMorbidityCodeAsKEYandRiskFactorAsVALUE(morbidityCode));
        identifiedMorbidities.add(identifiedMorbidityDetails);
    }
}
