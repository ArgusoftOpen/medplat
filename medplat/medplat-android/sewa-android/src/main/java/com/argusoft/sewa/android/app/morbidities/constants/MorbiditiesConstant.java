package com.argusoft.sewa.android.app.morbidities.constants;

import com.argusoft.sewa.android.app.morbidities.beans.BeneficiaryMorbidityDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author alpeshkyada
 */
public class MorbiditiesConstant {

    private MorbiditiesConstant() {
        throw new IllegalStateException("MorbiditiesConstant Utility Class");
    }

    public static final String RED_COLOR = "R";
    public static final String RED_COLOR_FULL = "Red";
    public static final String YELLOW_COLOR = "Y";
    public static final String GREEN_COLOR = "G";
    public static final String YELLOW_COLOR_FULL = "Yellow";
    public static final String GREEN_COLOR_FULL = "Green";
    public static final String NO_COLOR_FULL = "no";
    public static final String SEVERE_PREGNANCY_INDUCED_HYPERTENSION = "SPIH";
    public static final String APH = "APH";
    public static final String SEVERE_ABDOMINAL_PAIN = "SAP";
    public static final String HEPATITIS = "HEP";
    public static final String SICKLE_CELL_CRISIS = "SCC";
    public static final String PREMATURE_RUPTURE_OF_MEMBRANE = "PRM";
    public static final String SEVERE_ANEMIA = "SEAM";
    public static final String MAL_PRESENTATION = "MAL";
    public static final String DECREASED_FOETAL_MOVEMENT = "DFM";
    public static final String BAD_OBSTETRIC_HISTORY = "BOH";
    public static final String UNINTENDED_PREGNANCY = "UP";
    public static final String MILD_PREGNANCY_INDUCED_HYPERTENSION = "MPIH";
    public static final String MALARIA_IN_PREGNANCY = "MIP";
    public static final String FEVER = "FEVER";
    public static final String POSSIBLE_TB = "PT";
    public static final String URINARY_TRACT_INFECTION = "UTI";
    public static final String VAGINITIS = "VAGIN";
    public static final String NIGHT_BLINDNESS = "NB";
    public static final String PROBABLE_SEVERE_ANEMIA = "PSA";
    public static final String EMESIS_OF_PREGNANCY = "EOP";
    public static final String UPPER_RESPIRATORY_TRACT_INFECTION = "URTI";
    public static final String MODERATE_ANEMIA = "MDAN";
    public static final String BREAST_PROBLEMS = "BP";
    public static final String SEVERE_PNEUMONIA_OR_SERIOUS_BACTERIAL_INFECTION = "SPSBI";
    public static final String CHRONIC_COUGH_OR_COLD = "CCC";
    public static final String DIARRHOEA_WITH_SEVERE_DEHYDRATION = "DWSD";
    public static final String SEVERE_PERSISTENT_DIARRHOEA = "SPD";
    public static final String VERY_SEVERE_FEBRILE_ILLNESS = "VSFI";
    public static final String SEVERE_MALNUTRITION = "SMAL";
    public static final String SEVERE_ANEMIA_FOR_CHILD = "SAFC";
    public static final String PNEUMONIA_CHILD_CARE = "PNEUFC";
    public static final String DIARRHOEA_WITH_SOME_DEHYDRATION = "DSD";
    public static final String DYSENTERY = "DYSEN";
    public static final String MALARIA = "MALAR";
    public static final String VERY_LOW_WEIGHT = "VLW";
    public static final String ANEMIA = "ANEMA";
    public static final String COLD_OR_COUGH = "COCOU";
    public static final String DIARRHOEA_WITH_NO_DEHYDRATION = "DWND";
    public static final String NOT_VERY_LOW_WEIGHT = "NVW";
    public static final String NO_ANEMIA = "NANEM";
    public static final String SEPSIS = "SEP";
    public static final String VERY_LOW_BIRTH_WEIGHT = "LBWSH";
    public static final String DIARRHOEA = "DIAH";
    public static final String JAUNDICE = "JAUND";
    public static final String BLEEDING_FROM_ANY_PART_OF_THE_BODY = "BFAP";
    public static final String PPH = "PPH";
    public static final String POST_PARTUM_INFECTION = "PPI";
    public static final String MASTITIS = "MASTI";
    public static final String POST_PARTUM_MOOD_DISORDER = "PPMD";
    public static final String PIH = "PIH";
    public static final String PIHH = "PIHH";
    public static final String HYPOTHERMIA = "HYTH";
    public static final String PNEUMONIA_PNC = "PNEUM";
    public static final String LOCAL_INFECTION = "LI";
    public static final String HIGH_RISK_LOW_BIRTH_WEIGHT_PRE_TERM = "HRLB";
    public static final String BIRTH_ASPHYXIA = "BASP";
    public static final String FEEDING_PROBLEM = "FPRO";
    public static final String TRUE = "T";
    public static final String FALSE = "F";
    public static final String ONLY_MOTHER_RED = "MR";
    public static final String ONLY_CHILD_RED = "CR";
    public static final String MOTHER_AND_CHILD_RED = "MCR";
    public static final String UNMARRIED = "UNMRID";
    public static final String SEPARATED = "SEPRTD";
    public static final String DIVORCED = "DIVRSD";
    public static final String WIDOW = "WIDOW";
    public static final String PLACENTA_PREVIA = "PLPRE";
    public static final String PRE_TERM = "PRETRM";
    public static final String CONVULSION = "CONVLS";
    public static final String PREVIOUS_LSCS = "PRELS";
    public static final String TWINS = "TWINS";
    public static final String STILL_BIRTH = "SBRTH";
    public static final String PREVIOUS_2_ABORTIONS = "P2ABO";
    public static final String KNOWN_CASE_OF_SICKLE_CELL_DISEASE = "KCOSCD";
    public static final String MAL_PRESENTATION_PREGNANCY_COMPLICATION = "MLPRST";
    public static final String NOT_IN_STOCK = "NS";
    public static final String ABORTION = "ABRSN";
    public static final String OK = "OK";
    public static final String DIFFERENT = "DIFF";
    public static final String NOSE = "NOSE";
    public static final String MOUTH = "MOUTH";
    public static final String ANUS = "ANUS";
    public static final String URINE = "URINE";
    public static final String SKIN = "SKIN";
    public static final String GRAVIDA_MORE_THAN_4 = "4+";
    public static final String EQUALS_SYMBOL = " = ";
    //This constant is used in DataBeanToIdentifyPNCMorbidities
    public static final String SKIN_YELLOW = "YLLW";
    public static final String NO_BLEEDING = "NBLDNG";
    public static final String NONE = "NONE";
    public static final String CRY_AFTER_EFFORTS = "CRYAE";
    public static final String NO_PROBLEM = "NOPROB";
    public static final String DROWSY = "DROWSY";
    public static final String LESS_THAN_NORMAL = "LTUSL";
    public static final String NO_SUCKLING = "NSUCK";
    public static final String WEAK = "WEAK";
    public static final String STOP = "STOP";
    public static final String DISTENDED = "DSTND";
    public static final String PUS = "PUS";
    public static final String ABSCESS = "ABSC";
    public static final String ERUPTION = "ERUPT";
    //This constant is used in DataBeanToIdentifyChildCareMorbidities
    public static final String VERY_SLOWLY = "VSWLY";
    public static final String NOT_ABLE_TO_DRINK = "NABTDR";
    public static final String DRINKING_POORLY = "DRNKP";
    public static final String SEVERE = "SEVR";
    public static final String SOME_MILD = "SMMLD";
    public static final String NORMALLY = "NRMLY";
    public static final String THIRSTY = "THRSTY";
    //This constant is used in DataBeanToIdentifyANCMorbidities
    public static final String MARRIED = "MERID";
    public static final String USUAL = "USUAL";
    public static final String NOT_APPLICABLE = "NAPP";    //Added in enhancement
    public static final String NON_VERTEX = "NVRTX";
    public static final String VERTEX = "VRTX";
    public static final String NOT_KNOWN = "NK";
    public static final String NO_COUGH = "NCOU";
    public static final String MORE_THAN_15_DAYS = "GT15D";
    public static final String NO = FALSE;
    public static final String YES = TRUE;
    public static final String NO_OR_MINIMAL_DISCHARGE = "MINDIS";
    public static final String CURDY_WHITE = "CWHIT";
    public static final String FOUL_SMELLING = "FOSML";
    public static final String YELLOWISH_GREEN_DISCHARGE = "YGDIS";
    public static final String LESS_THAN_15_DAYS = "LT15D";
    public static final String NORMAL = "NRML";
    public static final String ABSENT = "ABSNT";
    public static final String LESS_THAN_USUAL = "LTUSL";
    public static final String INVERTED_NIPPLE = "INNIP";
    public static final String OTHER_ISSUES = "OTHIS";
    public static final String ENGORGED_BREAST = "ENBRST";
    public static final String CRACKED_NIPPLE = "CRCKNP";
    public static final String YELLOW = "YLLW";
    public static final String PALE = "PALE";
    public static final String BLEEDING = "BLDNG";
    public static final String RED_ERUPTIONS = "REDER";
    public static final String NOT_PRESENT = "NTPRE";
    public static final String LESS_THAN_EQ_10 = "LTE10";
    public static final String GREATER_THAN_10 = "GT10";
    public static final String GOOD = "GOOD";
    public static final String UNCONSCIOUS = "UNCON";
    public static final String CONSCIOUS = "CONS";
    public static final String CAN_BE_AWAKENED = "CBA";
    public static final String FORCEFUL = "FRCFL";
    public static final String STOPPED = "STOPD";
    public static final String SLOWLY = "SWLY";
    public static final String DRINKING_EAGARLY = "DRNKE";
    public static final String SOME = "SMMLD";
    public static final String IMMEDIATELY_AFTER_BIRTH = "IMMAB";
    public static final String DID_NOT_CRY = "DNTCRY";
    public static final String ONE_PLUS = "1";
    public static final String TWO_PLUS = "2";
    public static final String THREE_PLUS = "3";
    public static final String FOUR_PLUS = "4";
    public static final String NO_CHANGE = "NCHNG";
    public static final String WORSE = "WORS";
    public static final String BETTER = "BTTR";
    //added in phase 2
    public static final String CONJUNCTIVITIS = "CONJUC"; //---PHASE-II---
    public static final String LOW_BIRTH_WEIGHT = "BWLPC"; //---PHASE-II---
    public static final String FEVER_IN_PNC = "FVRIP"; //---PHASE-II---
    public static final String PUS_PRESENT = "PUSP";//---PHASE-II---
    public static final String SWOLLEN = "SWLN";//---PHASE-II---
    public static final String LIMP = "LIMP";//---PHASE-II---
    public static List<BeneficiaryMorbidityDetails> morbidities;
    public static String nextEntity;
    private static Map<String, String> morbidityCodeAsKEYandRiskFactorAsVALUE = null;
    private static Map<String, String> morbidityCodeAsKEYandMorbidityNameAsVALUE = null;
    private static Map<String, String> staticValueAndKeyMap = null;
    private static List<String> motherRelatedMorbidityQuestions = null;

    static {
        fillStaticValueAndKeyMap();
        fillMorbidityCodeAsKEYandMorbidityNameAsVALUE();
        fillMorbidityCodeAsKEYandRiskFactorAsVALUE();
        fillMotherRelatedPNCMorbidityQuestions();
    }

    public static Map<String, String> getStaticValueAndKeyMap() {
        return staticValueAndKeyMap;
    }

    public static String getMorbidityCodeAsKEYandRiskFactorAsVALUE(String key) {
        return morbidityCodeAsKEYandRiskFactorAsVALUE.get(key);
    }

    public static String getMorbidityCodeAsKEYandMorbidityNameAsVALUE(String key) {
        return morbidityCodeAsKEYandMorbidityNameAsVALUE.get(key);
    }

    private static void fillStaticValueAndKeyMap() {
        staticValueAndKeyMap = new HashMap<>();
        staticValueAndKeyMap.put(USUAL, "Usual");
        staticValueAndKeyMap.put(NOT_APPLICABLE, "Not applicable");
        staticValueAndKeyMap.put(ABSENT, "Absent");
        staticValueAndKeyMap.put(NON_VERTEX, "Non-vertex");
        staticValueAndKeyMap.put(VERTEX, "Vertex");
        staticValueAndKeyMap.put(MARRIED, "Married");
        staticValueAndKeyMap.put(UNMARRIED, "Unmarried");
        staticValueAndKeyMap.put(SEPARATED, "Separated");
        staticValueAndKeyMap.put(DIVORCED, "Divorced");
        staticValueAndKeyMap.put(WIDOW, "Widow");
        staticValueAndKeyMap.put(NO_OR_MINIMAL_DISCHARGE, "No or minimal discharge");
        staticValueAndKeyMap.put(CURDY_WHITE, "Curdy white");
        staticValueAndKeyMap.put(FOUL_SMELLING, "Foul smelling");
        staticValueAndKeyMap.put(YELLOWISH_GREEN_DISCHARGE, "Yellowish green discharge");
        staticValueAndKeyMap.put(NO, "No");
        staticValueAndKeyMap.put(NO_COUGH, "No cough");
        staticValueAndKeyMap.put(LESS_THAN_15_DAYS, "Less than 15 days");
        staticValueAndKeyMap.put(MORE_THAN_15_DAYS, "More than 15 days");
        staticValueAndKeyMap.put(YES, "Yes");
        staticValueAndKeyMap.put(LESS_THAN_NORMAL, "Less than usual");
        staticValueAndKeyMap.put(NORMAL, "Normal");
        staticValueAndKeyMap.put(INVERTED_NIPPLE, "Inverted Nipple");
        staticValueAndKeyMap.put(OTHER_ISSUES, "Other issues");
        staticValueAndKeyMap.put(NONE, "None");
        staticValueAndKeyMap.put(NOT_KNOWN, "Not known");
        staticValueAndKeyMap.put(ENGORGED_BREAST, "Engorged breast");
        staticValueAndKeyMap.put(CRACKED_NIPPLE, "Cracked nipple");
        staticValueAndKeyMap.put(PUS, "Pus");
        staticValueAndKeyMap.put(ABSCESS, "Abscess");
        staticValueAndKeyMap.put(ERUPTION, "Eruptions");

        staticValueAndKeyMap.put(APH, "APH");
        staticValueAndKeyMap.put(PPH, "PPH");
        staticValueAndKeyMap.put(PLACENTA_PREVIA, "Placenta previa");
        staticValueAndKeyMap.put(PRE_TERM, "Pre term");
        staticValueAndKeyMap.put(PIH, "PIH");
        staticValueAndKeyMap.put(PIHH, "PIH");
        staticValueAndKeyMap.put(CONVULSION, MorbiditySymptoms.CONVULSION);
        staticValueAndKeyMap.put(MAL_PRESENTATION_PREGNANCY_COMPLICATION, "Malpresentation");
        staticValueAndKeyMap.put(PREVIOUS_LSCS, "Previous LSCS");
        staticValueAndKeyMap.put(TWINS, "Twins");
        staticValueAndKeyMap.put(STILL_BIRTH, "Still birth");
        staticValueAndKeyMap.put(PREVIOUS_2_ABORTIONS, "Previous 2 abortions");
        staticValueAndKeyMap.put(KNOWN_CASE_OF_SICKLE_CELL_DISEASE, "Known case of sickle cell disease");
        staticValueAndKeyMap.put(NOT_IN_STOCK, "Not in stock");
        staticValueAndKeyMap.put(ABORTION, "Abortion");
        staticValueAndKeyMap.put(NO_PROBLEM, "No problem");
        staticValueAndKeyMap.put(OK, "Ok ");
        staticValueAndKeyMap.put(DIFFERENT, "Different");
        staticValueAndKeyMap.put(WEAK, "Weak");
        staticValueAndKeyMap.put(STOP, "Stop");
        staticValueAndKeyMap.put(NO_BLEEDING, "No bleeding");
        staticValueAndKeyMap.put(NOSE, "Nose");
        staticValueAndKeyMap.put(MOUTH, "Mouth");
        staticValueAndKeyMap.put(ANUS, "Anus");
        staticValueAndKeyMap.put(URINE, "Urine");
        staticValueAndKeyMap.put(SKIN, "Skin");
        staticValueAndKeyMap.put(YELLOW, YELLOW_COLOR_FULL);
        staticValueAndKeyMap.put(PALE, "Pale");
        staticValueAndKeyMap.put(BLEEDING, "Bleeding");
        staticValueAndKeyMap.put(RED_ERUPTIONS, "Red eruptions");
        staticValueAndKeyMap.put(NOT_PRESENT, "Not present");
        staticValueAndKeyMap.put(LESS_THAN_EQ_10, "Less than equal 10");
        staticValueAndKeyMap.put(GREATER_THAN_10, "More than 10");
        staticValueAndKeyMap.put(DISTENDED, "Distended");
        staticValueAndKeyMap.put(GOOD, "Good");
        staticValueAndKeyMap.put(DROWSY, "Drowsy");
        staticValueAndKeyMap.put(UNCONSCIOUS, "Unconsious");
        staticValueAndKeyMap.put(CONSCIOUS, "Consious");
        staticValueAndKeyMap.put(CAN_BE_AWAKENED, "Can be awakend");
        staticValueAndKeyMap.put(FORCEFUL, "Forceful");
        staticValueAndKeyMap.put(STOPPED, "Stopped");
        staticValueAndKeyMap.put(SLOWLY, "Slowly");
        staticValueAndKeyMap.put(VERY_SLOWLY, "Very slowly");
        staticValueAndKeyMap.put(NORMALLY, "Normally");
        staticValueAndKeyMap.put(NOT_ABLE_TO_DRINK, "Not able to drink");
        staticValueAndKeyMap.put(DRINKING_POORLY, "Drinks poorly");
        staticValueAndKeyMap.put(DRINKING_EAGARLY, "Drinks eagarly");
        staticValueAndKeyMap.put(THIRSTY, "Thirsty");
        staticValueAndKeyMap.put(SEVERE, "Severe");
        staticValueAndKeyMap.put(SOME, "Some or Mild");
        staticValueAndKeyMap.put(IMMEDIATELY_AFTER_BIRTH, "Immediately after birth");
        staticValueAndKeyMap.put(CRY_AFTER_EFFORTS, "Cry after efforts");
        staticValueAndKeyMap.put(DID_NOT_CRY, "Did not cry");
        staticValueAndKeyMap.put(NO_SUCKLING, "No suckling");
        staticValueAndKeyMap.put(ONE_PLUS, "+");
        staticValueAndKeyMap.put(TWO_PLUS, "++");
        staticValueAndKeyMap.put(THREE_PLUS, "+++");
        staticValueAndKeyMap.put(FOUR_PLUS, "++++");
        staticValueAndKeyMap.put(NO_CHANGE, "No change");
        staticValueAndKeyMap.put(WORSE, "Worse");
        staticValueAndKeyMap.put(BETTER, "Better");

        //added in phase 2
        staticValueAndKeyMap.put(SWOLLEN, "Swollen");
        staticValueAndKeyMap.put(PUS_PRESENT, "Pus present");
        staticValueAndKeyMap.put(LIMP, "Limp");

    }

    private static void fillMorbidityCodeAsKEYandRiskFactorAsVALUE() {
        morbidityCodeAsKEYandRiskFactorAsVALUE = new HashMap<>();
        //Red Risk Morbidities ..............................................................

        //ANC_MORBIDITIES
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(SEVERE_PREGNANCY_INDUCED_HYPERTENSION, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(APH, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(SEVERE_ABDOMINAL_PAIN, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(HEPATITIS, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(SICKLE_CELL_CRISIS, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(PREMATURE_RUPTURE_OF_MEMBRANE, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(SEVERE_ANEMIA, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(MAL_PRESENTATION, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(DECREASED_FOETAL_MOVEMENT, RED_COLOR);

        //CHILD CARE MORBIDITIES
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(SEVERE_PNEUMONIA_OR_SERIOUS_BACTERIAL_INFECTION, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(CHRONIC_COUGH_OR_COLD, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(DIARRHOEA_WITH_SEVERE_DEHYDRATION, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(SEVERE_PERSISTENT_DIARRHOEA, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(VERY_SEVERE_FEBRILE_ILLNESS, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(SEVERE_MALNUTRITION, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(SEVERE_ANEMIA_FOR_CHILD, RED_COLOR);

        //PNC MORBIDITIES
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(SEPSIS, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(VERY_LOW_BIRTH_WEIGHT, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(DIARRHOEA, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(JAUNDICE, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(BLEEDING_FROM_ANY_PART_OF_THE_BODY, RED_COLOR);

        morbidityCodeAsKEYandRiskFactorAsVALUE.put(PPH, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(POST_PARTUM_INFECTION, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(MASTITIS, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(POST_PARTUM_MOOD_DISORDER, RED_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(PIHH, RED_COLOR);

        //Yellow Morbidities.............................................................................................
        //ANC_MORBIDITIES
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(BAD_OBSTETRIC_HISTORY, YELLOW_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(UNINTENDED_PREGNANCY, YELLOW_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(MILD_PREGNANCY_INDUCED_HYPERTENSION, YELLOW_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(MALARIA_IN_PREGNANCY, YELLOW_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(FEVER, YELLOW_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(POSSIBLE_TB, YELLOW_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(URINARY_TRACT_INFECTION, YELLOW_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(VAGINITIS, YELLOW_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(NIGHT_BLINDNESS, YELLOW_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(PROBABLE_SEVERE_ANEMIA, YELLOW_COLOR);

        //CHILD CARE MORBIDITIES
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(PNEUMONIA_CHILD_CARE, YELLOW_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(DIARRHOEA_WITH_SOME_DEHYDRATION, YELLOW_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(DYSENTERY, YELLOW_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(MALARIA, YELLOW_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(VERY_LOW_WEIGHT, YELLOW_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(ANEMIA, YELLOW_COLOR);

        //PNC MORBIDITIES
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(HYPOTHERMIA, YELLOW_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(PNEUMONIA_PNC, YELLOW_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(LOCAL_INFECTION, YELLOW_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(HIGH_RISK_LOW_BIRTH_WEIGHT_PRE_TERM, YELLOW_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(BIRTH_ASPHYXIA, YELLOW_COLOR);
//added in phase 2
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(CONJUNCTIVITIS, YELLOW_COLOR); //---PHASE-II---
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(LOW_BIRTH_WEIGHT, YELLOW_COLOR);//---PHASE-II---
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(FEVER_IN_PNC, YELLOW_COLOR);//---PHASE-II---

        //Green Morbidities........................................................
        //ANC_MORBIDITIES
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(EMESIS_OF_PREGNANCY, GREEN_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(UPPER_RESPIRATORY_TRACT_INFECTION, GREEN_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(MODERATE_ANEMIA, GREEN_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(BREAST_PROBLEMS, GREEN_COLOR);

        //CHILD CARE MORBIDITIES
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(COLD_OR_COUGH, GREEN_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(DIARRHOEA_WITH_NO_DEHYDRATION, GREEN_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(NOT_VERY_LOW_WEIGHT, GREEN_COLOR);
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(NO_ANEMIA, GREEN_COLOR);

        //PNC MORBIDITIES
        morbidityCodeAsKEYandRiskFactorAsVALUE.put(FEEDING_PROBLEM, GREEN_COLOR);
    }

    private static void fillMorbidityCodeAsKEYandMorbidityNameAsVALUE() {
        morbidityCodeAsKEYandMorbidityNameAsVALUE = new HashMap<>();
        //Red Risk Morbidities ..............................................................

        //ANC_MORBIDITIES
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(SEVERE_PREGNANCY_INDUCED_HYPERTENSION, "Severe pregnancy induced hypertension");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(APH, "APH");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(SEVERE_ABDOMINAL_PAIN, "Severe abdominal pain");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(HEPATITIS, "Hepatitis");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(SICKLE_CELL_CRISIS, "Sickle cell crisis");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(PREMATURE_RUPTURE_OF_MEMBRANE, "Premature rupture of membrane");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(SEVERE_ANEMIA, "Severe anemia");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(MAL_PRESENTATION, "Malpresentation");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(DECREASED_FOETAL_MOVEMENT, "Decreased foetal movement");

        //CHILD CARE MORBIDITIES
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(SEVERE_PNEUMONIA_OR_SERIOUS_BACTERIAL_INFECTION, "Severe pneumonia or serious bacterial infection");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(CHRONIC_COUGH_OR_COLD, "Chronic cough or cold");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(DIARRHOEA_WITH_SEVERE_DEHYDRATION, "Diarrhoea with severe dehydration");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(SEVERE_PERSISTENT_DIARRHOEA, "Severe persistent diarrhoea");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(VERY_SEVERE_FEBRILE_ILLNESS, "Very severe febrile illness");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(SEVERE_MALNUTRITION, "Severe malnutrition");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(SEVERE_ANEMIA_FOR_CHILD, "Severe anemia for child");

        //PNC MORBIDITIES
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(SEPSIS, "Sepsis");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(VERY_LOW_BIRTH_WEIGHT, "Very low birth weight");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(DIARRHOEA, "Diarrhoea");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(JAUNDICE, MorbiditySymptoms.JAUNDICE);
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(BLEEDING_FROM_ANY_PART_OF_THE_BODY, "Bleeding from any part of the body(nose, mouth, anus, umbilicus)");

        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(PPH, "PPH");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(POST_PARTUM_INFECTION, "Post partum infection");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(MASTITIS, "Mastitis");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(POST_PARTUM_MOOD_DISORDER, "Post partum mood disorder");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(PIHH, "PIH");

        //Yellow Morbidities.............................................................................................
        //ANC_MORBIDITIES
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(BAD_OBSTETRIC_HISTORY, "Bad obstetric history");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(UNINTENDED_PREGNANCY, "Unintented pregnancy");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(MILD_PREGNANCY_INDUCED_HYPERTENSION, "Mild pregnancy induced hypertension");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(MALARIA_IN_PREGNANCY, "Malaria in pregnancy");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(FEVER, MorbiditySymptoms.FEVER);
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(POSSIBLE_TB, "Possible TB");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(URINARY_TRACT_INFECTION, "Urinary tract infection");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(VAGINITIS, "Vaginitis");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(NIGHT_BLINDNESS, "Night blindness");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(PROBABLE_SEVERE_ANEMIA, "Probable severe anemia");

        //CHILD CARE MORBIDITIES
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(PNEUMONIA_CHILD_CARE, "Pneumonia");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(DIARRHOEA_WITH_SOME_DEHYDRATION, "Diarrhoea with some dehydration");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(DYSENTERY, "Dysentry");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(MALARIA, "Malaria");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(VERY_LOW_WEIGHT, "Very low weight");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(ANEMIA, "Anemia");

        //PNC MORBIDITIES
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(HYPOTHERMIA, "Hypothermia");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(PNEUMONIA_PNC, "Pneumonia");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(LOCAL_INFECTION, "Local infection");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(HIGH_RISK_LOW_BIRTH_WEIGHT_PRE_TERM, "High risk Low Birth Weight or Pre-term");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(BIRTH_ASPHYXIA, "Birth asphyxia");

        //Green Morbidities........................................................
        //ANC_MORBIDITIES
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(EMESIS_OF_PREGNANCY, "Emesis of pregnancy");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(UPPER_RESPIRATORY_TRACT_INFECTION, "Upper respiratory tract infection");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(MODERATE_ANEMIA, "Moderate anemia");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(BREAST_PROBLEMS, "Breast problems");

        //CHILD CARE MORBIDITIES
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(COLD_OR_COUGH, "Cold or Cough");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(DIARRHOEA_WITH_NO_DEHYDRATION, "Diarrhoea with no dehydration");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(NOT_VERY_LOW_WEIGHT, "Not very low weight");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(NO_ANEMIA, "No anemia");

        //PNC MORBIDITIES
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(FEEDING_PROBLEM, "Feeding problem");
        //added in phase 2
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(CONJUNCTIVITIS, "Conjuctivitis");
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(FEVER_IN_PNC, MorbiditySymptoms.FEVER);
        morbidityCodeAsKEYandMorbidityNameAsVALUE.put(LOW_BIRTH_WEIGHT, "Low birth weight");
    }

    private static void fillMotherRelatedPNCMorbidityQuestions() {
        motherRelatedMorbidityQuestions = new ArrayList<>();
        motherRelatedMorbidityQuestions.add(PNCMorbidityQuestionsForMother.PADS_CHANGED);
        motherRelatedMorbidityQuestions.add(PNCMorbidityQuestionsForMother.IF_FOUL_SMELL);
        motherRelatedMorbidityQuestions.add(PNCMorbidityQuestionsForMother.ABNORMAL_TALK);
        motherRelatedMorbidityQuestions.add(PNCMorbidityQuestionsForMother.FEVER);
        motherRelatedMorbidityQuestions.add(PNCMorbidityQuestionsForMother.HEADACHE_N_VISUAL);
        motherRelatedMorbidityQuestions.add(PNCMorbidityQuestionsForMother.BREAST_FEEDING);
        motherRelatedMorbidityQuestions.add(PNCMorbidityQuestionsForMother.BREAST_FEEDING_PROBLEM);
    }

    public static List<String> getOnlyMotherRelatedPNCMorbidities() {
        return motherRelatedMorbidityQuestions;
    }

    public static class MorbiditySymptoms {

        private MorbiditySymptoms() {
            throw new IllegalStateException("MorbiditySymptoms Utility Class");
        }

        public static final String VAGINAL_BLEEDING_SINCE_LAST_VISIT = "Vaginal bleeding since last visit";
        public static final String YES = "Yes";
        public static final String JAUNDICE = "Jaundice";
        public static final String SEVERE_JOINT_PAIN = "Severe joint pain";
        public static final String LEAKING_PER_VAGINALLY = "Leaking per vaginally";
        public static final String GESTATIONAL_WEEK = "Gestational week";
        public static final String HAEMOGLOBIN_IS = "Haemoglobin is";
        public static final String AGE_IS = "Age is";
        public static final String GRAVIDA_IS_MORE_THAN_4 = "Gravida is more than 4";
        public static final String COMPLICATION_PRESENT_DURING_PREVIOUS_PREGNANCY_IS = "Complication present during previous pregnancy is ";
        public static final String MARITAL_STATUS_IS = "Marital status is ";
        public static final String HEADACHE = "Headache";
        public static final String NO = "No";
        public static final String VISION_DISTURBANCE = "Vision disturbance";
        public static final String URINE_PROTEIN = "Urine protein";
        public static final String SYSTOLIC_BP = "Systolic blood pressure";
        public static final String DIASTOLIC_BP = "Diastolic blood pressure";
        public static final String CHILLS_RIGOURS = "Chills or Rigours";
        public static final String BURNING_URINATION = "Burning urination";
        public static final String FEVER = "Fever";
        public static final String PRESENCE_OF_COUGH = "Presence of cough";
        public static final String VAGINAL_DISCHARGE = "Vaginal discharge";
        public static final String VOMITING = "Vomiting";
        public static final String DOES_THE_CHILD_HAVE_PALMER_POLLER = "Has palmer poller";
        public static final String IS_THE_CHILD_NOT_ABLE_TO_DRINK_OR_BREAST_FEEDING = "Child is not able to drink or breast feeding";
        public static final String HAS_THE_CHILD_HAD_CONVULSION = "Had convulsion";
        public static final String IS_THE_CHILD_LETHARGIC_OR_UNCONSCIOUS = "Is the child lethargic or Unconscious?";
        public static final String DOES_THE_CHILD_HAVE_CHEST_INDRAWING = "Chest Indrawing";
        public static final String DOES_THE_CHILD_HAVE_COUGH_OR_DIFFICULT_BREATHING = "Has cough or difficult Breathing";
        public static final String DOES_THE_CHILD_HAVE_DIARRHOEA = "Has diarrhoea";
        public static final String RESPIRATORY_RATE = "Respiratory rate";
        public static final String SUNKEN_EYES = "Sunken eyes";
        public static final String LETHARGIC_OR_UNCONSCIOUS = "Lethargic or unconscious";
        public static final String DOES_SKIN_GOES_BACK_VERY_SLOWLY = "Skin goes back very slowly (longer than 2 seconds)";
        public static final String HOW_DOES_HE_SHE_DRINKS_IT = "Drinking";
        public static final String DOES_THE_CHILD_HAVE_MORE_STOOLS_DIARRHOEA = "Child has more stools or Diarrhoea";
        public static final String IF_MORE_THAN_7_DAYS_HAS_FEVER_BEEN_PRESENT_EACH_DAY = "Fever has been present each day, for more than 7 days";
        public static final String IS_THE_NECK_STIFF = "Neck stiff";
        public static final String AXILLARY_TEMPERATURE = "Measure axillary temperature";
        public static final String VISIBLE_SEVERE_WASTING = "Visible severe wasting";
        public static final String EDEMA_OF_BOTH_FEET = "Edema of both feet";
        public static final String MALNUTRITION_GRADE = "Malnutrition grade";
        public static final String RESTLESS_OR_IRRITABLE = "Restless or irritable";
        public static final String BLOOD_IN_STOOLS = "Blood in stools";
        public static final String WEIGHT_IS = "Weight is ";
        public static final String HOW_IS_THE_SKIN_OF_THE_BABY = "How is the skin of the baby";
        public static final String PADS_ARE_CHANGED_IN_24_HOURS = "Pads are changed in 24 hours";
        public static final String FOUL_SMELLING_DISCHARGE = "Foul smelling discharge";
        public static final String ABNORMAL_TALK_BEHAVIOURS_OR_MOOD_CHANGE = "Abnormal talk or behaviour or mood changes";
        public static final String HEADACHE_WITH_VISION_DISTURBANCE = "Has headache with visual disturbances";
        public static final String BABY_HAND_AND_FEET_COLD_TO_TOUCH = "Baby's hands and feet cold to touch";
        public static final String TEMPERATURE = "Temperature";
        public static final String UMBILICUS = "Umbilicus";
        public static final String WHEN_DID_BABY_CRY = "When did baby cry?";
        public static final String ANY_DIFFICULTY_IN_BREASTFEEDING = "Any difficulty in breastfeeding";
        public static final String DOES_MOTHER_HAVE_ANY_OF_THE_FOLLOWING = "Mother has following problems";
        public static final String DO_YOU_GET_TIRED_EASILY = "Gets tired easily";
        public static final String ARE_YOU_SHORT_OF_BREATH_DURING_ROUTING_HOUSEHOLD_WORK = "Experience shortness of breath during routine household work";
        public static final String IS_CONJUNCTIVA_AND_PALMS_PALE = "Conjunctiva and palms pale";
        public static final String CHEST_INDRAWING = "Chest indrawing";
        public static final String ABDOMEN = "Abdomen";
        public static final String WHETHER_BABY_VOMITS = "Whether baby vomits";
        public static final String HOW_IS_BABY_CRY = "Baby's cry ";
        public static final String SKIN_PUSTULES = "Skin pustules";
        public static final String CONVULSION = "Convulsion";
        public static final String HOW_IS_BABY_SUCKLING = "Baby's sucking";
        public static final String WAS_BABY_FED_LESS_THAN_USUAL = "Baby's feeding";
        //added in phase 2
        public static final String HOW_ARE_BABYS_EYES = "Baby's eyes";
        public static final String WHETHER_BABYS_LIMBS_AND_NECK_MORE_LIMP_THAN_BEFORE = "Baby's limbs and neck";
        public static final String TOUCH_CHILD_S_SKIN_ON_ABDOMEN_DOES_IT_HAVE_FEVER = "Touch child's skin on abdomen.Does it have fever?";
        public static final String SWELLING_OF_FACE_HANDS_OR_FEET = "Swelling of feet, hands or face";
    }

    public static class PNCMorbidityQuestionsForMother {

        private PNCMorbidityQuestionsForMother() {
            throw new IllegalStateException("PNCMorbidityQuestionsForMother Utility Class");
        }

        public static final String PADS_CHANGED = "No. of pads changed in last 24 hours?";
        public static final String IF_FOUL_SMELL = "If there is any foul smell discharge?";
        public static final String ABNORMAL_TALK = "Abnormal talks or behaviour observed?";
        public static final String FEVER = "Has fever?";
        public static final String HEADACHE_N_VISUAL = "Has headache with visual disturbances?";
        public static final String BREAST_FEEDING = "Difficulty in breastfeeding?";
        public static final String BREAST_FEEDING_PROBLEM = "Does mother have any of following problems?(Tick all that apply)";
    }
}
