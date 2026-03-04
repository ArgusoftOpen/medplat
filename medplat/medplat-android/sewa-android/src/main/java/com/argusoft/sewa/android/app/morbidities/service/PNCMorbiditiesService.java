package com.argusoft.sewa.android.app.morbidities.service;

import com.argusoft.sewa.android.app.morbidities.beans.DataBeanToIdentifyPNCMorbidities;
import com.argusoft.sewa.android.app.morbidities.beans.IdentifiedMorbidityDetails;
import com.argusoft.sewa.android.app.morbidities.constants.MorbiditiesConstant;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.UtilBean;

import java.util.Calendar;
import java.util.List;

/**
 * @author alpeshkyada
 */
public final class PNCMorbiditiesService {

    private DataBeanToIdentifyPNCMorbidities dtim;
    private List<IdentifiedMorbidityDetails> identifiedMorbidities;
    //added in phase 2
    private boolean isSepsisFound;

    public PNCMorbiditiesService(DataBeanToIdentifyPNCMorbidities dtim, List<IdentifiedMorbidityDetails> identifiedMorbiditiesList) {
        this.dtim = dtim;
        this.identifiedMorbidities = identifiedMorbiditiesList;

        isHypothermia();
        isJaundice();
        isPIH();
        isPPH();
        isPostPartumInfection();
        isPostpartumMoodDisorder();
        //Check only once after First PNC home visit(It may be from unscheduled or alert or after registration).
        isSepsisFound = isSepsisP2();
        if (!isSepsisFound) {
            isPneumonia();
            isFever();
        }
        //Must be call after sepsis
        isLocalInfection();
        if (GlobalTypes.CLIENT_IS_MOTHER.equalsIgnoreCase(dtim.getBeneficiaryType())) {
            //One condition is dependent on sepsis found or not so It is always come after sepsis checking.
            isFeedingProblems();
        }
        if (!dtim.getIsChildFirstPncDone()) {
            isBirthAsphyxia();
            isLowBirthWeight();
            isVeryLowBirthWeight();
        }
        isConjunctivitis();
    }

    //Sepsis
    private boolean isSepsisP2() {
        boolean isIdentified = false;
        boolean tempCheck = false;
        int count = 0;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        /*
         (1)  
         1. (Whether baby's limbs and neck more limp than before? = Limp)
         */
        StringBuilder sb = new StringBuilder();
        //Write first changed condition
        if (!dtim.getWhetherBabyLimbsNeckLastState()
                && MorbiditiesConstant.LIMP.equals(dtim.getWhetherBabyLimbsAndNeckMoreLimpThanBefore())) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.WHETHER_BABYS_LIMBS_AND_NECK_MORE_LIMP_THAN_BEFORE));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(dtim.getWhetherBabyLimbsAndNeckMoreLimpThanBefore())));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());
            tempCheck = true;
        }
        if (tempCheck) {
            count++;
        }

        tempCheck = false;
        /*(2)
         Any difficulty in breastfeeding (If mother is having any problem section) = Yes 
         OR 
         Whether baby fed less than usual (Ask mother about baby) = Less than normal 
         OR
         How is baby suckling = No suckling or Weak */

        if (((!dtim.getAnyDifficultyInBreastfeedingLastStatus()) && (Boolean.TRUE.equals(dtim.getAnyDifficultyInBreastfeeding())))
                || ((!dtim.getWhetherBabyFedLessLastStatus()) && MorbiditiesConstant.LESS_THAN_NORMAL.equalsIgnoreCase(dtim.getWasBabyFedLessThanUsual()))
                || ((!dtim.getHowIsBabySucklingLastStatus()) && MorbiditiesConstant.NO_SUCKLING.equalsIgnoreCase(dtim.getHowIsBabySuckling())
                || ((!dtim.getHowIsBabySucklingLastStatus()) && MorbiditiesConstant.WEAK.equalsIgnoreCase(dtim.getHowIsBabySuckling())))) {
            sb.delete(0, sb.length());
            if (Boolean.TRUE.equals(dtim.getAnyDifficultyInBreastfeeding())) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.ANY_DIFFICULTY_IN_BREASTFEEDING));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                sb.delete(0, sb.length());
            }

            if ((MorbiditiesConstant.LESS_THAN_NORMAL.equalsIgnoreCase(dtim.getWasBabyFedLessThanUsual()))) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.WAS_BABY_FED_LESS_THAN_USUAL));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(dtim.getWasBabyFedLessThanUsual())));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                sb.delete(0, sb.length());
            }

            if ((MorbiditiesConstant.NO_SUCKLING.equalsIgnoreCase(dtim.getHowIsBabySuckling())
                    || MorbiditiesConstant.WEAK.equalsIgnoreCase(dtim.getHowIsBabySuckling()))) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HOW_IS_BABY_SUCKLING));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(dtim.getHowIsBabySuckling())));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            }
            tempCheck = true;
        }
        if (tempCheck) {
            count++;
        }
        sb.delete(0, sb.length());
        tempCheck = false;
        /*
         (3) 
         How is baby's cry (Ask mother about baby section) = Weak or stopped          
         */
        if (!dtim.getHowIsBabyCryLastStatus()
                && (MorbiditiesConstant.WEAK.equalsIgnoreCase(dtim.getHowIsBabyCry()) || MorbiditiesConstant.STOP.equalsIgnoreCase(dtim.getHowIsBabyCry()))) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HOW_IS_BABY_CRY));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(dtim.getHowIsBabyCry())));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            tempCheck = true;
        }
        sb.delete(0, sb.length());
        if (tempCheck) {
            count++;
        }
        tempCheck = false;
        /*
         (4)
         Baby's hand and feet cold to touch = Yes 
         OR 
         Temperature (Examination section) > 99 */
        if (Boolean.TRUE.equals(dtim.getAreBabyHandsAndFeetColdToTouch())) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.BABY_HAND_AND_FEET_COLD_TO_TOUCH));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            tempCheck = true;
        }
        sb.delete(0, sb.length());
        if (dtim.getTemperature() != null && dtim.getTemperature() > 99.0) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.TEMPERATURE));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(dtim.getTemperature().toString());
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            tempCheck = true;
        }
        sb.delete(0, sb.length());
        if (tempCheck) {
            count++;
        }
        tempCheck = false;
        /*
         (5)
         Abdomen = Distended OR Whether baby vomits = Yes */
        if (Boolean.TRUE.equals(dtim.getWhetherBabyVomits())) {
            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.WHETHER_BABY_VOMITS));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            tempCheck = true;
        }
        sb.delete(0, sb.length());
        if (MorbiditiesConstant.DISTENDED.equalsIgnoreCase(dtim.getAbdomen())) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.ABDOMEN));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel("Distended"));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            tempCheck = true;
        }
        sb.delete(0, sb.length());
        if (tempCheck) {
            count++;
        }
        tempCheck = false;
        /*
         (6)
         Chest indrawing = Yes */
        if (Boolean.TRUE.equals(dtim.getChestIndrawing())) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.CHEST_INDRAWING));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            tempCheck = true;
        }
        sb.delete(0, sb.length());
        if (tempCheck) {
            count++;
        }
        tempCheck = false;
        /*
         (7)
         Umbilicus = Pus          
         */
        if (MorbiditiesConstant.PUS.equalsIgnoreCase(dtim.getUmbilicus())) {
            tempCheck = true;
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.UMBILICUS));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel("Pus"));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
        }
        sb.delete(0, sb.length());
        if (tempCheck) {
            count++;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (count > 1) {
            isIdentified = true;
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.SEPSIS, identifiedMorbidityDetails);
        }
        return isIdentified;
    }

    //PPH
    private void isPPH() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        /* If (Pads are changed in 24 hours > 5) */
        if (dtim.getNoOfPadsChangedIn24Hours() != null && dtim.getNoOfPadsChangedIn24Hours() > 5) {
            String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.PADS_ARE_CHANGED_IN_24_HOURS) + MorbiditiesConstant.EQUALS_SYMBOL +
                    dtim.getNoOfPadsChangedIn24Hours();
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);
            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.PPH, identifiedMorbidityDetails);
        }
    }

    //Jaundice
    private void isJaundice() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        /* If (How is the skin of the baby = Yellow)*/
        if (dtim.getHowIsTheSkinOfTheBaby() != null) {
            for (int i = 0; i < dtim.getHowIsTheSkinOfTheBaby().size(); i++) {
                String option = dtim.getHowIsTheSkinOfTheBaby().get(i);
                if (option.equalsIgnoreCase(MorbiditiesConstant.SKIN_YELLOW)) {
                    String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HOW_IS_THE_SKIN_OF_THE_BABY) + MorbiditiesConstant.EQUALS_SYMBOL +
                            UtilBean.getMyLabel("Yellow");
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);
                    isIdentified = true;
                }
            }

        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.JAUNDICE, identifiedMorbidityDetails);
        }
    }

    //Post-partum infection
    private void isPostPartumInfection() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        /* If (Foul smelling discharge = Yes)
         * or (Fever = Yes) */
        StringBuilder sb = new StringBuilder();
        if (Boolean.TRUE.equals(dtim.getFoulSmellingDischarge()) && (Boolean.TRUE.equals(dtim.getFeverForMother()))) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.FOUL_SMELLING_DISCHARGE));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.FEVER));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            isIdentified = true;
        }

        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.POST_PARTUM_INFECTION, identifiedMorbidityDetails);
        }
    }

    //Post-partum mood disorder
    private void isPostpartumMoodDisorder() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        /* If (Abnormal talk or behaviour or mood changes? = Yes) */
        if (Boolean.TRUE.equals(dtim.getAbnormalTalkOrBehaviourOrMoodChanges())) {
            String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.ABNORMAL_TALK_BEHAVIOURS_OR_MOOD_CHANGE) +
                    MorbiditiesConstant.EQUALS_SYMBOL +
                    UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES);
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);
            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.POST_PARTUM_MOOD_DISORDER, identifiedMorbidityDetails);
        }
    }

    //PIH
    private void isPIH() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        /* If (Has headache with visual disturbances? = Yes) */
        if (Boolean.TRUE.equals(dtim.getHasHeadacheWithVisualDisturbances())) {
            String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HEADACHE_WITH_VISION_DISTURBANCE) +
                    MorbiditiesConstant.EQUALS_SYMBOL +
                    UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES);
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);
            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.PIHH, identifiedMorbidityDetails);
        }
    }

    //..............................................................................YELLOW FOR CHILD.............................................

    //Hypothermia
    private void isHypothermia() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        /* If (Baby's hands and feet cold to touch = Yes)
         * or (Temperature < 35) */
        StringBuilder sb = new StringBuilder();
        if (Boolean.TRUE.equals(dtim.getAreBabyHandsAndFeetColdToTouch())) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.BABY_HAND_AND_FEET_COLD_TO_TOUCH));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            isIdentified = true;
        }
        sb.delete(0, sb.length());
        if (dtim.getTemperature() != null && dtim.getTemperature() < 97.0) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.TEMPERATURE));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(dtim.getTemperature().toString());
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.HYPOTHERMIA, identifiedMorbidityDetails);
        }
    }

    //Pneumonia
    private void isPneumonia() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        //1. If (Sepsis diagnosed = No) && (Chest indrawing = Yes)
        //Sepsis is first checked from constructor.If sepsis not identified then and then this method will be called and Pneumonia will check.

        StringBuilder sb = new StringBuilder();
        if (Boolean.TRUE.equals(dtim.getChestIndrawing())) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.CHEST_INDRAWING));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            isIdentified = true;
        }
        sb.delete(0, sb.length());
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.PNEUMONIA_PNC, identifiedMorbidityDetails);
        }
    }

    //Local infection
    private void isLocalInfection() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        /*
         1. (Skin pustules < 10)
         or
         2. [(Umbilicus = 'Pus') && NO Sepsis]
         or
         3. [(Skin pustules > 10 && NO Sepsis]
         */

        StringBuilder sb = new StringBuilder();
        boolean isSepsis = false;
        if (identifiedMorbidities != null && !(identifiedMorbidities.isEmpty())) {
            for (int i = 0; i < identifiedMorbidities.size(); i++) {
                IdentifiedMorbidityDetails identifiedMorbidityDetails1 = identifiedMorbidities.get(i);
                if (MorbiditiesConstant.SEPSIS.equals(identifiedMorbidityDetails1.getMorbidityCode())) {
                    isSepsis = true;
                    break;
                }
            }
        }
        sb.delete(0, sb.length());
        if (!isSepsis) {
            if (dtim.getUmbilicus() != null && dtim.getUmbilicus().equalsIgnoreCase(MorbiditiesConstant.PUS)) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.UMBILICUS));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel("Pus"));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                isIdentified = true;
            }
            sb.delete(0, sb.length());
            if (dtim.getSkinPustules() != null && Boolean.TRUE.equals(dtim.getSkinPustules())) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.SKIN_PUSTULES));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(MorbiditiesConstant.MorbiditySymptoms.YES);
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(UtilBean.getMyLabel(sb.toString()));
                isIdentified = true;
            }
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.LOCAL_INFECTION, identifiedMorbidityDetails);
        }
    }

    private void isVeryLowBirthWeight() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        StringBuilder sb = new StringBuilder();
        if (dtim.getNewBornWeight() != null && dtim.getNewBornWeight() < 2.0 && dtim.getNewBornWeight() > 0.0) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.WEIGHT_IS));
            sb.append(dtim.getNewBornWeight());
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            isIdentified = true;
        }

        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.VERY_LOW_BIRTH_WEIGHT, identifiedMorbidityDetails);
        }
    }

    //High risk Low Birth Weight/Pre-term
    public void isHighRiskLowBirthWeightOrPreterm(List<IdentifiedMorbidityDetails> identifiedVector) {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        /*
         Weight between 1500 to 1999 gm at anytime during first 15 days 
         Weight less than 2.1 kg at anytime during 15 to 21 days  
         Weight less than 2.2 kg at anytime during 22 to 27 days 
         Weight less than 2.3 kg at anytime on 28th day
         Preterm delivery [Definition of preterm delivery is delivery 
         happened in 8 months and 14 days or less AND delivery date will be obtained from Pregnancy outcome visit]
         */
        StringBuilder sb = new StringBuilder();
        if (identifiedVector != null && identifiedVector.isEmpty()) {
            if (dtim.getWeightOfChild() != null && dtim.getAgeOfChild() != null) {
                if ((dtim.getWeightOfChild() < 1.999 && dtim.getWeightOfChild() >= 1.5) && (dtim.getAgeOfChild() <= UtilBean.getMilliSeconds(0, 0, 15))) {
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.WEIGHT_IS));
                    sb.append(dtim.getWeightOfChild());
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.AGE_IS));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    long diff = Calendar.getInstance().getTimeInMillis() - dtim.getAgeOfChild();
                    int[] calculateAgeYearMonthDay = UtilBean.calculateAgeYearMonthDay(diff);
                    sb.append(calculateAgeYearMonthDay[2]);
                    sb.append(UtilBean.getMyLabel(" Days"));
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                    isIdentified = true;
                }
                if ((dtim.getWeightOfChild() > 0.0) && (dtim.getWeightOfChild() < 2.1) && (dtim.getAgeOfChild() >= UtilBean.getMilliSeconds(0, 0, 15)) && (dtim.getAgeOfChild() <= UtilBean.getMilliSeconds(0, 0, 21))) {
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.WEIGHT_IS));
                    sb.append(dtim.getWeightOfChild());
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.AGE_IS));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    long diff = Calendar.getInstance().getTimeInMillis() - dtim.getAgeOfChild();
                    int[] calculateAgeYearMonthDay = UtilBean.calculateAgeYearMonthDay(diff);
                    sb.append(calculateAgeYearMonthDay[2]);
                    sb.append(UtilBean.getMyLabel(" Days"));
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                    isIdentified = true;
                }
                if ((dtim.getWeightOfChild() > 0.0) && (dtim.getWeightOfChild() < 2.2) && (dtim.getAgeOfChild() >= UtilBean.getMilliSeconds(0, 0, 22)) && (dtim.getAgeOfChild() <= UtilBean.getMilliSeconds(0, 0, 27))) {
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.WEIGHT_IS));
                    sb.append(dtim.getWeightOfChild());
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.AGE_IS));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    long diff = Calendar.getInstance().getTimeInMillis() - dtim.getAgeOfChild();
                    int[] calculateAgeYearMonthDay = UtilBean.calculateAgeYearMonthDay(diff);
                    sb.append(calculateAgeYearMonthDay[1]);
                    sb.append(UtilBean.getMyLabel(" Months "));
                    sb.append(calculateAgeYearMonthDay[2]);
                    sb.append(UtilBean.getMyLabel(" Days "));
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                    isIdentified = true;
                }
                //In last condition for "Weight less than 2.3 kg at anytime on or after 28th day"" so we can say that "Weight less than 2.3 kg after 27th day"
                if ((dtim.getWeightOfChild() > 0.0) && (dtim.getWeightOfChild() < 2.3) && (dtim.getAgeOfChild() > UtilBean.getMilliSeconds(0, 0, 27))) {
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.WEIGHT_IS));
                    sb.append(dtim.getWeightOfChild());
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.AGE_IS));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    sb.append(UtilBean.getMyLabel(" 28 Days "));
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                    isIdentified = true;
                }
            }
            sb.delete(0, sb.length());
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            identifiedMorbidities = identifiedVector;
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.HIGH_RISK_LOW_BIRTH_WEIGHT_PRE_TERM, identifiedMorbidityDetails);
        }
    }

    //Birth asphyxia
    private void isBirthAsphyxia() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        /*  If (When did baby cry? = Cry after efforts) */
        if ((dtim.getWhenDidBabyCry() != null)
                && (dtim.getWhenDidBabyCry().equalsIgnoreCase(MorbiditiesConstant.CRY_AFTER_EFFORTS))) {
            String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.WHEN_DID_BABY_CRY) +
                    MorbiditiesConstant.EQUALS_SYMBOL +
                    UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(dtim.getWhenDidBabyCry()));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);
            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.BIRTH_ASPHYXIA, identifiedMorbidityDetails);
        }
    }

    //..............................................................................GREEN FOR MOTHER.............................................

    //Feeding problems
    private void isFeedingProblems() {
        boolean isIdentified = false;
        boolean temp = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        /*1. Any difficulty in breastfeeding = yes) ||
         2. Does mother have any of following problems? = anything other than no problem ||
         3. Does baby's chin touch the breast? = No
         4. Is mouth widely open? = No
         5. Is lower lip turned outward? = No
         6. Is areola more above the mouth and less beneath the mouth? = No
        
         Ph-2 Revised criteria:
        
         1. (Any difficulty in breastfeeding = yes) ||
         2. (Does mother have any of following problems? = anything other than no problem) ||
         3. [Sepsis diagnosed = No] && [(Was baby fed less than usual?  = Yes/Less than usual)]
         */

        StringBuilder sb = new StringBuilder();
        if (Boolean.TRUE.equals(dtim.getAnyDifficultyInBreastfeeding())) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.ANY_DIFFICULTY_IN_BREASTFEEDING));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            isIdentified = true;
        }
        sb.delete(0, sb.length());
        sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DOES_MOTHER_HAVE_ANY_OF_THE_FOLLOWING));
        sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
        if (dtim.getDoesMotherHaveAnyOfFollowingProblems() != null) {
            String options;
            for (int i = 0; i < dtim.getDoesMotherHaveAnyOfFollowingProblems().size(); i++) {
                options = dtim.getDoesMotherHaveAnyOfFollowingProblems().get(i);
                if (!(MorbiditiesConstant.NO_PROBLEM.equalsIgnoreCase(options))) {
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(options)));
                    sb.append(",");
                    isIdentified = true;
                    temp = true;
                } else {
                    break;
                }
            }
            if (temp) {
                sb.deleteCharAt(sb.length() - 1);
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            }
        }

        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        sb.delete(0, sb.length());
        if ((!isSepsisFound) && (MorbiditiesConstant.LESS_THAN_USUAL.equalsIgnoreCase(dtim.getWasBabyFedLessThanUsual()))) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.WAS_BABY_FED_LESS_THAN_USUAL));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(dtim.getWasBabyFedLessThanUsual())));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());
            isIdentified = true;
        }

        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.FEEDING_PROBLEM, identifiedMorbidityDetails);
        }
    }

    //Conjunctivitis
    private void isConjunctivitis() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        StringBuilder sb = new StringBuilder();

        //If (How are baby's eyes? = 'Pus present' or 'Swollen')
        if (dtim.getHowAreBabyEyes() != null
                && (MorbiditiesConstant.PUS_PRESENT.equals(dtim.getHowAreBabyEyes()) || MorbiditiesConstant.SWOLLEN.equals(dtim.getHowAreBabyEyes()))) {
            isIdentified = true;
            sb.append(UtilBean.getMyLabel(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HOW_ARE_BABYS_EYES)));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(dtim.getHowAreBabyEyes())));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.CONJUNCTIVITIS, identifiedMorbidityDetails);
        }
    }

    //Low birth weight
    private void isLowBirthWeight() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        StringBuilder sb = new StringBuilder();
        if (dtim.getNewBornWeight() != null && (dtim.getNewBornWeight() >= 2.0) && (dtim.getNewBornWeight() < 2.5)) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.WEIGHT_IS)).append(": ");
            sb.append(dtim.getNewBornWeight());
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            isIdentified = true;
        }
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.LOW_BIRTH_WEIGHT, identifiedMorbidityDetails);
        }

    }

    //Fever
    private void isFever() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();

        StringBuilder sb = new StringBuilder();
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        //If (Sepsis diagnosed = No) && (Temperature > 99F)
        //Sepsis is first checked from constructor.If sepsis not identified then and then this method will be called and Pneumonia will check.
        if (dtim.getTemperature() != null && dtim.getTemperature() > 99.0) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.TEMPERATURE));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(dtim.getTemperature().toString());
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            isIdentified = true;
        }
        sb.delete(0, sb.length());
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.FEVER_IN_PNC, identifiedMorbidityDetails);
        }
    }

    private void addIdentifiedMorbidityIntoList(String morbidityCode, IdentifiedMorbidityDetails identifiedMorbidityDetails) {
        identifiedMorbidityDetails.setMorbidityCode(morbidityCode);
        identifiedMorbidityDetails.setRiskFactorOfIdentifiedMorbidities(MorbiditiesConstant.getMorbidityCodeAsKEYandRiskFactorAsVALUE(morbidityCode));
        identifiedMorbidities.add(identifiedMorbidityDetails);
    }
}
