/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.morbidities.service;

import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.morbidities.beans.DataBeanToIdentifyChildCareMorbidities;
import com.argusoft.sewa.android.app.morbidities.beans.IdentifiedMorbidityDetails;
import com.argusoft.sewa.android.app.morbidities.constants.MorbiditiesConstant;
import com.argusoft.sewa.android.app.util.UtilBean;

import java.util.Date;
import java.util.List;

/**
 * @author alpeshkyada
 */
public class ChildCareMorbiditiesService {

    private DataBeanToIdentifyChildCareMorbidities dtim;
    private List<IdentifiedMorbidityDetails> identifiedMorbidities;

    public ChildCareMorbiditiesService(DataBeanToIdentifyChildCareMorbidities dtim, List<IdentifiedMorbidityDetails> identifiedMorbiditiesList) {
        this.dtim = dtim;
        this.identifiedMorbidities = identifiedMorbiditiesList;

        //In all the cases it is fixed that all method must be called so it is called in constructor.
        isAnemia();
        isChronicCoughOrCold();
        isColdOrCough();
        //Sequence of this morbidity method's matters a lot.
        isDiarrhoeaWithSevereDehydration();
        isDiarrhoeaWithSomeDehydration();
        isDiarrhoeaWithNoDehydration();
        isDysentery();
        isNotVeryLowWeight();
        isPneumonia();
        isSevereAnemia();
        isSevereMalnutrition();
        isSeverePersistentDiarrhoea();
        isSeverePneumoniaOrSeriousBacterialInfection();
        isVeryLowWeight();
        isVerySevereFebrileIllness();
        isMalaria();
    }

    //.........................................................RED MORBIDITIES....................................................................

    //Severe pneumonia or serious bacterial infection
    private void isSeverePneumoniaOrSeriousBacterialInfection() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        StringBuilder sb = new StringBuilder();

        if (Boolean.TRUE.equals(dtim.isDoeschildHaveCoughOrDifficultBreathing())) {
            //  If (Is the child not able to drink or breast feeding? = Yes) Or
            if (Boolean.FALSE.equals(dtim.isChildNOTAbleToDrinkOrBreastfeed())) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.IS_THE_CHILD_NOT_ABLE_TO_DRINK_OR_BREAST_FEEDING));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                isIdentified = true;
            }
            sb.delete(0, sb.length());
            //(Is the child vomits everything? = Yes) Or
            if (Boolean.TRUE.equals(dtim.isChildVomitsEverything())) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.VOMITING));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                isIdentified = true;
            }
            sb.delete(0, sb.length());
            //(Has the child had convulsion? = Yes) Or
            if (Boolean.TRUE.equals(dtim.isChildHadConvulsion())) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HAS_THE_CHILD_HAD_CONVULSION));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                isIdentified = true;
            }
            sb.delete(0, sb.length());
            //(Is the child lethargic or Unconscious? = Yes) Or
            if (Boolean.TRUE.equals(dtim.isIschildLethargicOrUnconscious())) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.IS_THE_CHILD_LETHARGIC_OR_UNCONSCIOUS));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                isIdentified = true;
            }
            sb.delete(0, sb.length());
            //(Does the child have Chest Indrawing? = Yes)
            if (Boolean.TRUE.equals(dtim.isChildHaveChestIndrawing())) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DOES_THE_CHILD_HAVE_CHEST_INDRAWING));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                isIdentified = true;
            }
        }
        sb.delete(0, sb.length());
        if (isIdentified) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DOES_THE_CHILD_HAVE_COUGH_OR_DIFFICULT_BREATHING));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.SEVERE_PNEUMONIA_OR_SERIOUS_BACTERIAL_INFECTION, identifiedMorbidityDetails);
        }
    }

    private void isColdOrCough() {

        boolean isIdentified = false;
        StringBuilder sb = new StringBuilder();
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();

        /*
         * (Does the child have cough or difficult Breathing? = Yes) and
         * (Since how many days >= 14 days) and
         * (Is the child not able to drink or breast feeding? = No) and
         * (Is the child vomits everything? = No) and
         * (Has the child had convulsion? = No) and
         * (Is the child lethargic or Unconscious? = No) and
         * [(Age of child 2 months to 12 months) and (Count the number of breaths in one minute < 50)] or
         * [(Age of child > 12 months)  && (Count the number of breaths in one minute < 40)]
         */
        if (Boolean.TRUE.equals(dtim.isDoeschildHaveCoughOrDifficultBreathing()) &&
                Boolean.FALSE.equals(dtim.isChildVomitsEverything()) &&
                Boolean.TRUE.equals(dtim.isChildNOTAbleToDrinkOrBreastfeed()) &&
                Boolean.FALSE.equals(dtim.isChildHadConvulsion()) &&
                Boolean.FALSE.equals(dtim.isIschildLethargicOrUnconscious()) &&
                dtim.getAgeOfChild() != null &&
                dtim.getRespiratoryRate() != null &&
                dtim.getSinceHowManyDaysOfCough() != null &&
                dtim.getSinceHowManyDaysOfCough() < 14 &&
                ((dtim.getRespiratoryRate() < 50 &&
                        dtim.getAgeOfChild() >= UtilBean.getMilliSeconds(0, 2, 0) &&
                        dtim.getAgeOfChild() <= UtilBean.getMilliSeconds(1, 0, 0)) ||
                        (dtim.getRespiratoryRate() < 40 &&
                                dtim.getAgeOfChild() > UtilBean.getMilliSeconds(1, 0, 0)))) {

            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.AGE_IS));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            Date date = new Date();
            long diff = date.getTime() - dtim.getAgeOfChild();
            int[] calculateAgeYearMonthDay = UtilBean.calculateAgeYearMonthDay(diff);
            sb.append(UtilBean.getAgeDisplay(calculateAgeYearMonthDay[0], calculateAgeYearMonthDay[1], calculateAgeYearMonthDay[2]));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.RESPIRATORY_RATE));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(dtim.getRespiratoryRate().toString());
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());
            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.IS_THE_CHILD_NOT_ABLE_TO_DRINK_OR_BREAST_FEEDING));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.VOMITING));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HAS_THE_CHILD_HAD_CONVULSION));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.IS_THE_CHILD_LETHARGIC_OR_UNCONSCIOUS));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DOES_THE_CHILD_HAVE_COUGH_OR_DIFFICULT_BREATHING));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(UtilBean.getMyLabel(LabelConstants.SINCE_LESS_THAN_14_DAYS));
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.COLD_OR_COUGH, identifiedMorbidityDetails);
        }
    }

    private void isChronicCoughOrCold() {
        //Changed in last week of launching
        /*
         * (Does the child have cough or difficult Breathing? = Yes) and
         * (Since how many days >= 14 days) and
         * (Is the child not able to drink or breast feeding? = No) and
         * (Is the child vomits everything? = No) and
         * (Has the child had convulsion? = No) and
         * (Is the child lethargic or Unconscious? = No) and
         * [(Age of child 2 months to 12 months) and (Count the number of breaths in one minute < 50)] or
         * [(Age of child > 12 months) and (Count the number of breaths in one minute < 40)]
         */

        boolean isIdentified = false;
        StringBuilder sb = new StringBuilder();
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();

        if (Boolean.TRUE.equals(dtim.isDoeschildHaveCoughOrDifficultBreathing()) &&
                Boolean.TRUE.equals(dtim.isChildNOTAbleToDrinkOrBreastfeed()) &&
                Boolean.FALSE.equals(dtim.isChildVomitsEverything()) &&
                Boolean.FALSE.equals(dtim.isChildHadConvulsion()) &&
                Boolean.FALSE.equals(dtim.isIschildLethargicOrUnconscious()) &&
                dtim.getAgeOfChild() != null &&
                dtim.getRespiratoryRate() != null &&
                dtim.getSinceHowManyDaysOfCough() != null &&
                dtim.getSinceHowManyDaysOfCough() >= 14 &&
                ((dtim.getAgeOfChild() >= UtilBean.getMilliSeconds(0, 2, 0) &&
                        dtim.getAgeOfChild() <= UtilBean.getMilliSeconds(1, 0, 0) &&
                        dtim.getRespiratoryRate() < 50) ||
                        (dtim.getAgeOfChild() > UtilBean.getMilliSeconds(1, 0, 0) &&
                                dtim.getRespiratoryRate() < 40))) {

            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.AGE_IS));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            Date date = new Date();
            long diff = date.getTime() - dtim.getAgeOfChild();
            int[] calculateAgeYearMonthDay = UtilBean.calculateAgeYearMonthDay(diff);
            sb.append(UtilBean.getAgeDisplay(calculateAgeYearMonthDay[0], calculateAgeYearMonthDay[1], calculateAgeYearMonthDay[2]));

            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.RESPIRATORY_RATE));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(dtim.getRespiratoryRate().toString());
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());
            isIdentified = true;
        }
        sb.delete(0, sb.length());
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.IS_THE_CHILD_NOT_ABLE_TO_DRINK_OR_BREAST_FEEDING));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.VOMITING));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HAS_THE_CHILD_HAD_CONVULSION));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.IS_THE_CHILD_LETHARGIC_OR_UNCONSCIOUS));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DOES_THE_CHILD_HAVE_COUGH_OR_DIFFICULT_BREATHING));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            sb.delete(0, sb.length());
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(UtilBean.getMyLabel(LabelConstants.SINCE_MORE_THAN_14_DAYS));
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.CHRONIC_COUGH_OR_COLD, identifiedMorbidityDetails);
        }
    }

    //Diarrhoea with severe dehydration (RED)
    private void isDiarrhoeaWithSevereDehydration() {
        /*
         [(Does the child have Diarrhoea? = Yes) and (Since how many days < 14)] and
         At least 2 of following: [(Sunken eyes?? = Yes)
         || (Lethargic or unconscious? = Yes)
         || (How does he/she drinks it? = Not able to drink or Drinking poorly)
         || (Does skin goes back very slowly (longer than 2 seconds)? = Very slowly)]
         */
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        int count = 0;
        if (dtim.getSinceHowManyDaysOfStools() != null && dtim.getSinceHowManyDaysOfStools() < 14) {
            StringBuilder sb = new StringBuilder();

            if (Boolean.TRUE.equals(dtim.isSunkenEyes())) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.SUNKEN_EYES));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                count++;
            }

            if (Boolean.TRUE.equals(dtim.isLethargicOrUnconscious())) {
                if (count == 1) {
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.LETHARGIC_OR_UNCONSCIOUS));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                } else {
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.LETHARGIC_OR_UNCONSCIOUS));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                }
                count++;
            }
            if (dtim.getDoesSkinGoesBackVerySlowly() != null &&
                    dtim.getDoesSkinGoesBackVerySlowly().equalsIgnoreCase(MorbiditiesConstant.VERY_SLOWLY)) {
                if (count == 1) {
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DOES_SKIN_GOES_BACK_VERY_SLOWLY));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    sb.append(UtilBean.getMyLabel(LabelConstants.VERY_SLOWLY));
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                } else if (count == 0) {
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DOES_SKIN_GOES_BACK_VERY_SLOWLY));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    sb.append(UtilBean.getMyLabel(LabelConstants.VERY_SLOWLY));
                } else {
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DOES_SKIN_GOES_BACK_VERY_SLOWLY));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    sb.append(UtilBean.getMyLabel(LabelConstants.VERY_SLOWLY));
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                }
                count++;
            }
            if (dtim.getHowChildDrinks() != null &&
                    (dtim.getHowChildDrinks().equalsIgnoreCase(MorbiditiesConstant.NOT_ABLE_TO_DRINK) ||
                            dtim.getHowChildDrinks().equalsIgnoreCase(MorbiditiesConstant.DRINKING_POORLY))) {
                if (count == 1) {
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HOW_DOES_HE_SHE_DRINKS_IT));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(dtim.getHowChildDrinks())));
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                } else if (count == 0) {
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HOW_DOES_HE_SHE_DRINKS_IT));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(dtim.getHowChildDrinks())));
                } else {
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HOW_DOES_HE_SHE_DRINKS_IT));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(dtim.getHowChildDrinks())));
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                }
                count++;
            }
        }

        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (count > 1) {
            String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DOES_THE_CHILD_HAVE_DIARRHOEA) +
                    MorbiditiesConstant.EQUALS_SYMBOL +
                    UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES);
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(UtilBean.getMyLabel(LabelConstants.SINCE_LESS_THAN_14_DAYS));
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.DIARRHOEA_WITH_SEVERE_DEHYDRATION, identifiedMorbidityDetails);
        }
    }

    //Diarrhoea with some dehydration (YELLOW)
    private void isDiarrhoeaWithSomeDehydration() {
        /* 1. [(Does the child have Diarrhoea? = Yes) and (Since how many days < 14)] and
         2. At least 2 of following:
         * [(Sunken eyes?? = Yes) or
         * (Restless or irritable? = Yes) or
         * (How does he/she drinks it? = Thirsty or Normally) or
         * (Does skin goes back very slowly (longer than 2 seconds)? = Slowly)]
         */
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        int count = 0;
        if (dtim.getSinceHowManyDaysOfStools() != null && dtim.getSinceHowManyDaysOfStools() < 14) {
            StringBuilder sb = new StringBuilder();

            if (Boolean.TRUE.equals(dtim.isRestlessOrIrritable())) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.RESTLESS_OR_IRRITABLE));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                count++;
            }

            if (Boolean.TRUE.equals(dtim.isSunkenEyes())) {
                if (count == 1) {
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.SUNKEN_EYES));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                } else {
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.SUNKEN_EYES));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                }
                count++;
            }

            if (dtim.getHowChildDrinks() != null &&
                    (dtim.getHowChildDrinks().equalsIgnoreCase(MorbiditiesConstant.THIRSTY) ||
                            dtim.getHowChildDrinks().equalsIgnoreCase(MorbiditiesConstant.NORMALLY))) {
                if (count == 1) {
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HOW_DOES_HE_SHE_DRINKS_IT));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(dtim.getHowChildDrinks())));
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                } else if (count == 0) {
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HOW_DOES_HE_SHE_DRINKS_IT));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(dtim.getHowChildDrinks())));
                } else {
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HOW_DOES_HE_SHE_DRINKS_IT));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.getStaticValueAndKeyMap().get(dtim.getHowChildDrinks())));
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                }
                count++;
            }

            if (dtim.getDoesSkinGoesBackVerySlowly() != null && dtim.getDoesSkinGoesBackVerySlowly().equalsIgnoreCase(MorbiditiesConstant.SLOWLY)) {
                if (count == 1) {
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DOES_SKIN_GOES_BACK_VERY_SLOWLY));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    sb.append(UtilBean.getMyLabel(LabelConstants.SLOWLY));
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                } else if (count == 0) {
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DOES_SKIN_GOES_BACK_VERY_SLOWLY));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    sb.append(UtilBean.getMyLabel(LabelConstants.SLOWLY));
                } else {
                    sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DOES_SKIN_GOES_BACK_VERY_SLOWLY));
                    sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                    sb.append(UtilBean.getMyLabel(LabelConstants.SLOWLY));
                    identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                    sb.delete(0, sb.length());
                }
                count++;
            }
        }

        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (count > 1) {
            String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DOES_THE_CHILD_HAVE_DIARRHOEA) +
                    MorbiditiesConstant.EQUALS_SYMBOL +
                    UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES);
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(UtilBean.getMyLabel(LabelConstants.SINCE_LESS_THAN_14_DAYS));
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.DIARRHOEA_WITH_SOME_DEHYDRATION, identifiedMorbidityDetails);
        }
    }

    //Diarrhoea with no dehydration (GREEN)
    private void isDiarrhoeaWithNoDehydration() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        /* 1. [(Does the child have Diarrhoea? = Yes) and (Since how many days < 14)] and
         2. Diarrhea with severe dehydration is NOT present and
         3. Diarrhea with some dehydration is NOT present
         */
        if (dtim.getSinceHowManyDaysOfStools() != null && dtim.getSinceHowManyDaysOfStools() < 14) {
            boolean isSevereOrSomeDiarrhoeaPresent = false;
            if (identifiedMorbidities != null && !(identifiedMorbidities.isEmpty())) {
                for (int i = 0; i < identifiedMorbidities.size(); i++) {
                    IdentifiedMorbidityDetails identifiedMorbidityDetails1 = identifiedMorbidities.get(i);
                    if (MorbiditiesConstant.DIARRHOEA_WITH_SEVERE_DEHYDRATION.equals(identifiedMorbidityDetails1.getMorbidityCode())
                            || MorbiditiesConstant.DIARRHOEA_WITH_SOME_DEHYDRATION.equals(identifiedMorbidityDetails1.getMorbidityCode())) {
                        isSevereOrSomeDiarrhoeaPresent = true;
                        break;
                    }
                }
            }
            if (!isSevereOrSomeDiarrhoeaPresent) {
                isIdentified = true;
            }

            //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
            if (isIdentified) {
                String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DOES_THE_CHILD_HAVE_DIARRHOEA) +
                        MorbiditiesConstant.EQUALS_SYMBOL +
                        UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES);
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(UtilBean.getMyLabel(LabelConstants.SINCE_LESS_THAN_14_DAYS));
                addIdentifiedMorbidityIntoList(MorbiditiesConstant.DIARRHOEA_WITH_NO_DEHYDRATION, identifiedMorbidityDetails);
            }
        }
    }

    //Severe persistent diarrhoea
    private void isSeverePersistentDiarrhoea() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        /*(Since how many days >= 14) of stools [This field comes in the checking for Diarrhoea section after
         * "Does the child have more stools/Diarrhoea than usual?"]*/
        if (dtim.getSinceHowManyDaysOfStools() != null && dtim.getSinceHowManyDaysOfStools() >= 14) {
            StringBuilder sb = new StringBuilder();
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DOES_THE_CHILD_HAVE_MORE_STOOLS_DIARRHOEA));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());

            identifiedMorbidityDetails.getIdentifiedSymptoms().add(UtilBean.getMyLabel(LabelConstants.SINCE_MORE_THAN_14_DAYS));
            isIdentified = true;
        }

        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.SEVERE_PERSISTENT_DIARRHOEA, identifiedMorbidityDetails);
        }
    }

    //Very severe febrile illness
    private void isVerySevereFebrileIllness() {
        boolean isVerySevereFebrileIllnessIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        /* If [
         * (Is the child not able to drink or breast feeding? = Yes)
         * Or (Is the child vomits everything? = Yes)
         * Or (Has the child had convulsion? = Yes)
         * Or (Is the child lethargic or Unconscious? = Yes)
         * Or (If more than 7 days,has fever been present each day? = Yes)
         * Or (Is the neck stiff? = Yes)
         * ]
         * && (Measure axillary temperature >= 99.5 F)
         */

        // phase-II Touch Child Skin On Abdomen Does It Have Fever added and temperature removed.
        StringBuilder sb = new StringBuilder();
        if (((dtim.getMeasureAxillaryTemperatureIfFeverNo() != null
                && dtim.getMeasureAxillaryTemperatureIfFeverNo() >= 99.5)
                || (dtim.getMeasureAxillaryTemperatureIfFeverYes() != null
                && dtim.getMeasureAxillaryTemperatureIfFeverYes() >= 99.5)
                || (dtim.getFeverFlag() != null && Boolean.TRUE.equals(dtim.getFeverFlag())))
                || (Boolean.TRUE.equals(dtim.getFeverFlag())
                || Boolean.TRUE.equals(dtim.isTouchChildSkinOnAbdomenDoesItHaveFever()))) {

            if (Boolean.FALSE.equals(dtim.isChildNOTAbleToDrinkOrBreastfeed())) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.IS_THE_CHILD_NOT_ABLE_TO_DRINK_OR_BREAST_FEEDING));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                isVerySevereFebrileIllnessIdentified = true;
            }
            sb.delete(0, sb.length());
            if (Boolean.TRUE.equals(dtim.isChildVomitsEverything())) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.VOMITING));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                isVerySevereFebrileIllnessIdentified = true;
            }
            sb.delete(0, sb.length());
            if (Boolean.TRUE.equals(dtim.isChildHadConvulsion())) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HAS_THE_CHILD_HAD_CONVULSION));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                isVerySevereFebrileIllnessIdentified = true;
            }
            sb.delete(0, sb.length());
            if (Boolean.TRUE.equals(dtim.isIschildLethargicOrUnconscious())) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.IS_THE_CHILD_LETHARGIC_OR_UNCONSCIOUS));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                isVerySevereFebrileIllnessIdentified = true;
            }
            sb.delete(0, sb.length());
            if (Boolean.TRUE.equals(dtim.isIfMoreThan7DaysHasFeverBeenPresentEachDay())) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.IF_MORE_THAN_7_DAYS_HAS_FEVER_BEEN_PRESENT_EACH_DAY));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                isVerySevereFebrileIllnessIdentified = true;
            }
            sb.delete(0, sb.length());
            if (Boolean.TRUE.equals(dtim.isTheNeckStiff())) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.IS_THE_NECK_STIFF));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                isVerySevereFebrileIllnessIdentified = true;
            }
            sb.delete(0, sb.length());
        }

        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isVerySevereFebrileIllnessIdentified) {
            sb.delete(0, sb.length());
            if ((dtim.getMeasureAxillaryTemperatureIfFeverNo() != null && dtim.getMeasureAxillaryTemperatureIfFeverNo() >= 99.5)) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.AXILLARY_TEMPERATURE));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(dtim.getMeasureAxillaryTemperatureIfFeverNo());
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                sb.delete(0, sb.length());
            }
            if (dtim.getMeasureAxillaryTemperatureIfFeverYes() != null && dtim.getMeasureAxillaryTemperatureIfFeverYes() >= 99.5) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.AXILLARY_TEMPERATURE));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(dtim.getMeasureAxillaryTemperatureIfFeverYes());
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                sb.delete(0, sb.length());
            }
            if (dtim.getFeverFlag() != null && Boolean.TRUE.equals(dtim.getFeverFlag())) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.FEVER));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                sb.delete(0, sb.length());
            }
            // added in phase 2
            if ((Boolean.TRUE.equals(dtim.isTouchChildSkinOnAbdomenDoesItHaveFever()))) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.TOUCH_CHILD_S_SKIN_ON_ABDOMEN_DOES_IT_HAVE_FEVER));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            }
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.VERY_SEVERE_FEBRILE_ILLNESS, identifiedMorbidityDetails);
        }
    }

    //Severe malnutrition
    private void isSevereMalnutrition() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        /* if Visible severe wasting = Yes
         * or Edema of both feet = Yes
         * or Malnutrition grade of the child as per the weight entered = Red
         */

        StringBuilder sb = new StringBuilder();
        if (Boolean.TRUE.equals(dtim.isVisibleSevereWasting())) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.VISIBLE_SEVERE_WASTING));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            isIdentified = true;
        }
        sb.delete(0, sb.length());
        if (Boolean.TRUE.equals(dtim.isEdemaOfBothFeet())) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.EDEMA_OF_BOTH_FEET));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            isIdentified = true;
        }
        sb.delete(0, sb.length());
        if (MorbiditiesConstant.RED_COLOR_FULL.equalsIgnoreCase(dtim.getMalnutritionGradeOfChild())) {
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.MALNUTRITION_GRADE));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel("Red"));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            isIdentified = true;
        }

        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.SEVERE_MALNUTRITION, identifiedMorbidityDetails);
        }
    }

    //Severe anemia
    private void isSevereAnemia() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        //If (Does the child have palmer poller? = Severe)
        if (dtim.getDoesTheChildHavePalmerPoller() != null &&
                dtim.getDoesTheChildHavePalmerPoller().equalsIgnoreCase(MorbiditiesConstant.SEVERE)) {
            String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DOES_THE_CHILD_HAVE_PALMER_POLLER) +
                    MorbiditiesConstant.EQUALS_SYMBOL +
                    UtilBean.getMyLabel("Severe");
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);
            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.SEVERE_ANEMIA_FOR_CHILD, identifiedMorbidityDetails);
        }
    }

    //.........................................................YELLOW MORBIDITIES....................................................................

    //Pneumonia
    private void isPneumonia() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        /*
         * 1. (Does the child have cough or difficult Breathing? = Yes)
         * && (Is the child not able to drink or breast feeding? = No)
         * && (Is the child vomits everything? = No)
         * && (Has the child had convulsion? = No)
         * && (Is the child lethargic or Unconscious? = No)
         * && (Age of child 2 months to 12 months)
         * && (Count the number of breaths in one minute > 50)
         * ||
         * 2. (Does the child have cough or difficult Breathing? = Yes)
         * && (Is the child not able to drink or breast feeding? = No)
         * && (Is the child vomits everything? = No)
         * && (Has the child had convulsion? = No)
         * && (Is the child lethargic or Unconscious? = No)
         * && (Age of child > 12 months)
         * && (Count the number of breaths in one minute > 40)
         */
        // Changed condition on 15 october 2013
        // 1. [(Does the child have cough or difficult Breathing? = Yes)
        // && (Age of child 2 months to 12 months)
        // && (Count the number of breaths in one minute > 50)
        // && (Is the child not able to drink or breast feeding? = No)
        // && (Is the child vomits everything? = No)
        // && (Has the child had convulsion? = No)
        // && (Is the child lethargic or Unconscious? = No)
        // && Chest indrawing? = no]
        // ||
        // 2. [(Does the child have cough or difficult Breathing? = Yes)
        // && (Age of child > 12 months)
        // && (Count the number of breaths in one minute > 40)
        // && (Is the child not able to drink or breast feeding? = No)
        // && (Is the child vomits everything? = No)
        // && (Has the child had convulsion? = No)
        // && (Is the child lethargic or Unconscious? = No)
        // && Chest indrawing? = no ]

        if (Boolean.TRUE.equals(dtim.isDoeschildHaveCoughOrDifficultBreathing()) &&
                Boolean.TRUE.equals(dtim.isChildNOTAbleToDrinkOrBreastfeed()) &&
                Boolean.FALSE.equals(dtim.isChildVomitsEverything()) &&
                Boolean.FALSE.equals(dtim.isChildHadConvulsion()) &&
                Boolean.FALSE.equals(dtim.isIschildLethargicOrUnconscious()) &&
                Boolean.FALSE.equals(dtim.isChildHaveChestIndrawing()) &&
                dtim.getNumberOfBreathsIn1Minute() != null &&
                dtim.getAgeOfChild() != null &&
                ((dtim.getNumberOfBreathsIn1Minute() > 50 &&
                        (dtim.getAgeOfChild() >= UtilBean.getMilliSeconds(0, 2, 0) &&
                                dtim.getAgeOfChild() <= UtilBean.getMilliSeconds(1, 0, 0))) ||
                        (dtim.getNumberOfBreathsIn1Minute() > 40 &&
                                dtim.getAgeOfChild() > UtilBean.getMilliSeconds(1, 0, 0)))) {
            StringBuilder sb = new StringBuilder();

            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DOES_THE_CHILD_HAVE_COUGH_OR_DIFFICULT_BREATHING));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());

            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.IS_THE_CHILD_NOT_ABLE_TO_DRINK_OR_BREAST_FEEDING));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());

            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DOES_THE_CHILD_HAVE_CHEST_INDRAWING));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());

            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.VOMITING));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());

            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HAS_THE_CHILD_HAD_CONVULSION));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());

            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.IS_THE_CHILD_LETHARGIC_OR_UNCONSCIOUS));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());

            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.AGE_IS));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            Date date = new Date();
            long diff = date.getTime() - dtim.getAgeOfChild();
            int[] calculateAgeYearMonthDay = UtilBean.calculateAgeYearMonthDay(diff);
            sb.append(UtilBean.getAgeDisplay(calculateAgeYearMonthDay[0], calculateAgeYearMonthDay[1], calculateAgeYearMonthDay[2]));

            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());

            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.RESPIRATORY_RATE));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(dtim.getNumberOfBreathsIn1Minute().toString());
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());

            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.PNEUMONIA_CHILD_CARE, identifiedMorbidityDetails);
        }
    }

    //Dysentery
    private void isDysentery() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        //If (Blood in stools? = Yes)
        if (Boolean.TRUE.equals(dtim.isBloodInStools())) {
            String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.BLOOD_IN_STOOLS) +
                    MorbiditiesConstant.EQUALS_SYMBOL +
                    UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES);
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);
            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.DYSENTERY, identifiedMorbidityDetails);
        }

    }

    //Malaria
    private void isMalaria() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        /*   The latest change requires it to be:
         1. [(Measure axillary temperature >=  99.5 degree ) or Fever is “yes”]
         and
         2. Absence of Severe febrile illness morbidity.
         */

        StringBuilder sb = new StringBuilder();
        if (!(Boolean.FALSE.equals(dtim.isChildNOTAbleToDrinkOrBreastfeed())
                || Boolean.TRUE.equals(dtim.isChildVomitsEverything())
                || Boolean.TRUE.equals(dtim.isChildHadConvulsion())
                || Boolean.TRUE.equals(dtim.isIschildLethargicOrUnconscious())
                || Boolean.TRUE.equals(dtim.isIfMoreThan7DaysHasFeverBeenPresentEachDay())
                || Boolean.TRUE.equals(dtim.isTheNeckStiff()))) {

            if ((dtim.getMeasureAxillaryTemperatureIfFeverNo() != null && dtim.getMeasureAxillaryTemperatureIfFeverNo() >= 99.5)) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.AXILLARY_TEMPERATURE));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(dtim.getMeasureAxillaryTemperatureIfFeverNo());
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                sb.delete(0, sb.length());
                isIdentified = true;
            }
            if (dtim.getMeasureAxillaryTemperatureIfFeverYes() != null && dtim.getMeasureAxillaryTemperatureIfFeverYes() >= 99.5) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.AXILLARY_TEMPERATURE));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(dtim.getMeasureAxillaryTemperatureIfFeverYes());
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                sb.delete(0, sb.length());
                isIdentified = true;
            }
            if (dtim.getFeverFlag() != null && Boolean.TRUE.equals(dtim.getFeverFlag())) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.FEVER));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                sb.delete(0, sb.length());
                isIdentified = true;
            }
            // added in phase 2
            sb.delete(0, sb.length());
            if ((Boolean.TRUE.equals(dtim.isTouchChildSkinOnAbdomenDoesItHaveFever()))) {
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.TOUCH_CHILD_S_SKIN_ON_ABDOMEN_DOES_IT_HAVE_FEVER));
                sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
                sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.YES));
                identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
                isIdentified = true;
            }
        }

        if (isIdentified) {
            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.IS_THE_CHILD_NOT_ABLE_TO_DRINK_OR_BREAST_FEEDING));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());

            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.VOMITING));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());

            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.HAS_THE_CHILD_HAD_CONVULSION));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());

            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.IS_THE_CHILD_LETHARGIC_OR_UNCONSCIOUS));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());

            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.IF_MORE_THAN_7_DAYS_HAS_FEVER_BEEN_PRESENT_EACH_DAY));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());

            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.IS_THE_NECK_STIFF));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
            sb.delete(0, sb.length());

            //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.MALARIA, identifiedMorbidityDetails);
        }
    }

    //Very low weight
    private void isVeryLowWeight() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        /*
         1. (Visible severe wasting = No) and
         2. (Edema of both feet = No) and
         3. (Malnutrition grade of the child as per the weight entered = Yellow)
         */
        if (dtim.isVisibleSevereWasting() != null &&
                Boolean.FALSE.equals(dtim.isVisibleSevereWasting()) &&
                dtim.isEdemaOfBothFeet() != null &&
                Boolean.FALSE.equals(dtim.isEdemaOfBothFeet()) &&
                dtim.getMalnutritionGradeOfChild() != null &&
                dtim.getMalnutritionGradeOfChild().equalsIgnoreCase(MorbiditiesConstant.YELLOW_COLOR_FULL)) {
            StringBuilder sb = new StringBuilder(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.MALNUTRITION_GRADE));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel("Yellow"));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());

            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.VISIBLE_SEVERE_WASTING));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());

            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.EDEMA_OF_BOTH_FEET));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());

            isIdentified = true;
        }

        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.VERY_LOW_WEIGHT, identifiedMorbidityDetails);
        }
    }

    //Anemia
    private void isAnemia() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        /* If (Does the child have palmer poller? = Some)   */
        if (dtim.getDoesTheChildHavePalmerPoller() != null &&
                dtim.getDoesTheChildHavePalmerPoller().equalsIgnoreCase(MorbiditiesConstant.SOME_MILD)) {
            String sb = UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.DOES_THE_CHILD_HAVE_PALMER_POLLER) + MorbiditiesConstant.EQUALS_SYMBOL +
                    UtilBean.getMyLabel("Some");
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb);
            isIdentified = true;
        }
        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.ANEMIA, identifiedMorbidityDetails);
        }
    }

    //.........................................................GREEN MORBIDITIES....................................................................

    //Not very low weight
    private void isNotVeryLowWeight() {
        boolean isIdentified = false;
        IdentifiedMorbidityDetails identifiedMorbidityDetails = new IdentifiedMorbidityDetails();
        //Changed in week of launching
        /*    
         1. (Visible severe wasting = No) and
         2. (Edema of both feet = No) and
         3. (Malnutrition grade of the child as per the weight entered = Green
         */
        if (dtim.isVisibleSevereWasting() != null &&
                Boolean.FALSE.equals(dtim.isVisibleSevereWasting()) &&
                dtim.isEdemaOfBothFeet() != null &&
                Boolean.FALSE.equals(dtim.isEdemaOfBothFeet()) &&
                dtim.getMalnutritionGradeOfChild() != null &&
                dtim.getMalnutritionGradeOfChild().equalsIgnoreCase(MorbiditiesConstant.GREEN_COLOR_FULL)) {
            isIdentified = true;
            StringBuilder sb = new StringBuilder(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.MALNUTRITION_GRADE));

            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel("Green"));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());

            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.VISIBLE_SEVERE_WASTING));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());

            sb.delete(0, sb.length());
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.EDEMA_OF_BOTH_FEET));
            sb.append(MorbiditiesConstant.EQUALS_SYMBOL);
            sb.append(UtilBean.getMyLabel(MorbiditiesConstant.MorbiditySymptoms.NO));
            identifiedMorbidityDetails.getIdentifiedSymptoms().add(sb.toString());
        }

        //If this morbidity identifies then details bean of this morbidity is adding to listOfIdentified morbidities
        if (isIdentified) {
            addIdentifiedMorbidityIntoList(MorbiditiesConstant.NOT_VERY_LOW_WEIGHT, identifiedMorbidityDetails);
        }
    }

    private void addIdentifiedMorbidityIntoList(String morbidityCode, IdentifiedMorbidityDetails identifiedMorbidityDetails) {
        identifiedMorbidityDetails.setMorbidityCode(morbidityCode);
        identifiedMorbidityDetails.setRiskFactorOfIdentifiedMorbidities(MorbiditiesConstant.getMorbidityCodeAsKEYandRiskFactorAsVALUE(morbidityCode));
        identifiedMorbidities.add(identifiedMorbidityDetails);
    }
}
