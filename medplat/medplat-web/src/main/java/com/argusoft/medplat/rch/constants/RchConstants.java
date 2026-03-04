/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.constants;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * Define constants for rch.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 10:19 AM
 */
public class RchConstants {
    private RchConstants() {
    }

    public static final String TRUE = "1";
    public static final String FALSE = "2";

    public static final String DATA_INDICATION_FLAG = "DATA";
    public static final String MEMBER_ID = "member_id";
    public static final String PREGNANCY_REGISTRATION_ID = "preg_reg_id";
    public static final String SELECTED_SERVICE_TYPE = "serviceType";
    public static final String SELECTED_LANGUAGE_CODE = "languageCode";
    public static final String FROM_DATE = "from_date";
    public static final String TO_DATE = "to_date";
    public static final String LOCATION_ID = "location_id";

    public static final String SR_NUMBER = "Sr no.";
    public static final String CONST_HUSBAND_NAME = "Husband Name";
    public static final String FULL_ADDRESS = "Address";
    public static final String REASON_FOR_DEATH = "Reason of Death";
    public static final String CONST_RELIGION = "Religion";
    public static final String CONST_CASTE = "Caste";
    public static final String ABOVE_POVERTY_LINE = "Above Poverty Line";

    public static final String MEMBER_STATUS_DEATH = "DEATH";
    public static final String MEMBER_STATUS_MIGRATED = "MIGRATED";
    public static final String MEMBER_STATUS_AVAILABLE = "AVAILABLE";
    public static final String MEMBER_STATUS_WRONGLY_REGISTERED = "WRONGLY_REGISTERED";

    public static final String PREGNANCY_OUTCOME_MTP = "MTP";
    public static final String PREGNANCY_OUTCOME_LIVE_BIRTH = "LBIRTH";
    public static final String PREGNANCY_OUTCOME_STILL_BIRTH = "SBIRTH";
    public static final String PREGNANCY_OUTCOME_ABORTION = "ABORTION";
    public static final String PREGNANCY_OUTCOME_SPONTANEOUS_ABORTION = "SPONT_ABORTION";

    public static final String DELIVERY_PLACE_HOSPITAL = "HOSP";
    public static final String DELIVERY_PLACE_HOME = "HOME";
    public static final String DELIVERY_PLACE_ON_THE_WAY = "ON_THE_WAY";
    public static final String DELIVERY_PLACE_PRIVATE_HOSPITAL = "PVT_HOSP";

    public static final String REFFERAL_DONE_YES = "YES";
    public static final String REFFERAL_DONE_NO = "NO";
    public static final String REFFERAL_DONE_NOT_REQUIRED = "NOT_REQUIRED";

    public static final String DEATH_REASON_OTHER = "OTHER";
    public static final String DEATH_REASON_NONE = "NONE";

    public static final String DANGEROUS_SIGN_OTHER = "OTHER";
    public static final String SYMPTOMS_OTHER = "OTHER";
    public static final String TREATMENT_OTHER = "OTHER";
    public static final String DANGEROUS_SIGN_NONE = "NONE";

    public static final String PREVIOUS_PREGNANCY_COMPLICATION_OTHER = "OTHER";
    public static final String PREVIOUS_PREGNANCY_COMPLICATION_NONE = "NONE";

    public static final String BLOOD_SUGAR_TEST_EMPTY = "EMPTY";
    public static final String BLOOD_SUGAR_TEST_NON_EMPTY = "NON_EMPTY";
    public static final String BLOOD_SUGAR_TEST_NOT_DONE = "NOT_DONE";

    public static final String NO_RISK_FOUND = "No risks found";

    public static final String PREGNANCY_STATUS_APPROVED = "APPROVED";
    public static final String PREGNANCY_STATUS_REJECTED = "REJECTED";
    public static final String PREGNANCY_STATUS_PENDING = "PENDING";

    /**
     * Define constants for vaccination type.
     */
    public static class VaccinationType {
        private VaccinationType() {
        }

        public static final String HEPATITIS_B_0 = "HEPATITIS_B_0";
        public static final String BCG = "BCG";

        public static final String OPV_0 = "OPV_0";
        public static final String OPV_1 = "OPV_1";
        public static final String OPV_2 = "OPV_2";
        public static final String OPV_3 = "OPV_3";
        public static final String OPV_BOOSTER = "OPV_BOOSTER";

        public static final String PENTA_1 = "PENTA_1";
        public static final String PENTA_2 = "PENTA_2";
        public static final String PENTA_3 = "PENTA_3";

        public static final String DPT_1 = "DPT_1";
        public static final String DPT_2 = "DPT_2";
        public static final String DPT_3 = "DPT_3";
        public static final String DPT_BOOSTER = "DPT_BOOSTER";

        public static final String MEASLES_1 = "MEASLES_1";
        public static final String MEASLES_2 = "MEASLES_2";
        public static final String MEASLES_RUBELLA_1 = "MEASLES_RUBELLA_1";
        public static final String MEASLES_RUBELLA_2 = "MEASLES_RUBELLA_2";

        public static final String F_IPV_1_01 = "F_IPV_1_01";
        public static final String F_IPV_2_01 = "F_IPV_2_01";
        public static final String F_IPV_2_05 = "F_IPV_2_05";

        public static final String VITAMIN_A = "VITAMIN_A";
        public static final String VITAMIN_K = "VITAMIN_K";

        public static final String ROTA_VIRUS_1 = "ROTA_VIRUS_1";
        public static final String ROTA_VIRUS_2 = "ROTA_VIRUS_2";
        public static final String ROTA_VIRUS_3 = "ROTA_VIRUS_3";

        public static final String TT1 = "TT1";
        public static final String TT2 = "TT2";
        public static final String TT_BOOSTER = "TT_BOOSTER";
    }

    /**
     * Define constants for migration.
     */
    public static class MIGRATION {
        private MIGRATION() {
        }

        public static final String MIGRATION_TYPE_OUT = "OUT";
        public static final String MIGRATION_TYPE_IN = "IN";
        public static final String MIGRATION_STATE_REPORTED = "REPORTED";
        public static final String MIGRATION_STATE_CONFIRMED = "CONFIRMED";
        public static final String MIGRATION_STATE_NOT_HAPPENED = "NOT_HAPPEN";
        public static final String MIGRATION_STATE_REVERTED = "REVERTED";
        public static final String MIGRATION_STATE_NO_RESPONSE = "NO_RESPONSE";
        public static final String MIGRATION_STATE_NOT_YET_RESOLVED = "NOT_RESOLVED";
    }

    public static final String ASHA_REPORTED_EVENT_REJECTED = "REJECTED";
    public static final String ASHA_REPORTED_EVENT_CONFIRMED = "CONFIRMED";

    //print rch-village-profile keys

    // general information
    public static final String UNIQUE_HEALTH_ID = "unique_health_id";
    public static final String REG_SERVICE_DATE = "reg_service_date";
    public static final String MEMBER_NAME = "member_name";
    public static final String HUSBAND_NAME = "husband_name";
    public static final String AGE_DURING_DELIVERY = "age_during_delivery";
    public static final String BLOOD_GROUP = "blood_group";
    public static final String ADDRESS = "address";
    public static final String MOBILE_NUMBER = "mobile_number";
    public static final String RELIGION = "religion";
    public static final String BPL_FLAG = "bpl_flag";
    public static final String LMP_DATE = "lmp_date";
    public static final String PREGNANCY_WEEK_NUMBER = "pregnancy_week_number";
    public static final String TWELVE_PREGNANCY_WEEK_NUMBER = "twelve_pregnancy_week_number";
    public static final String EDD = "edd";
    public static final String EXPECTED_DELIVERY_PLACE = "expected_delivery_place";
    public static final String PREVIOUS_COMPLICATION = "previous_complication";
    public static final String SR_NO = "sr_no";

    public static final Map<String, String> GENERAL_INFORMATION_KEYS = new LinkedHashMap<>();

    // previous pregnancy registraion details
    public static final String PREVIOUS_PREGNANCY_DETAILS_JSON = "previous_pregnancy_details_json";
    public static final String PREG_COMPLICATION = "preg_complication";
    public static final String PREG_GENDER = "preg_gender";

    public static final Map<String, String> PREVIOUS_PREGNANCY_DETAILS_KEYS = new LinkedHashMap<>();

    // Pre-Delivery Care info
    public static final String PRE_DELIVERY_CARE_JSON = "pre_delivery_care_json";

    public static final String INSPECTION_DATE = "inspection_date";
    public static final String INSPECTION_PLACE = "inspection_place";
    public static final String INSPECTION_PLACE_TYPE = "inspection_place_type";
    public static final String PREGNANCY_WEEK = "pregnancy_week";
    public static final String PREG_WOMEN_WEIGHT = "preg_women_weight";
    public static final String HIV_TEST = "hiv_test";
    public static final String VDRL_TEST = "vdrl_test";
    public static final String SYSTOLIC_BP = "systolic_bp";
    public static final String DIASTOLIC_BP = "diastolic_bp";
    public static final String HAEMOGLOBIN_COUNT = "haemoglobin_count";
    public static final String FALIC_ACID_TABLETS = "falic_acid_tablets";
    public static final String IFA_TABLET = "ifa_tablet";
    public static final String URINE_ALBUMIN = "urine_albumin";
    public static final String URINE_SUGAR = "urine_sugar";
    public static final String SUGAR_TEST_BEFORE_FOOD_VAL = "sugar_test_before_food_val";
    public static final String SUGAR_TEST_AFTER_FOOD_VAL = "sugar_test_after_food_val";
    public static final String TT1 = "tt1";
    public static final String TT2 = "tt2";
    public static final String TT_BOOSTER = "ttb";
    public static final String FOETAL_HEIGHT = "foetal_height";
    public static final String FOETAL_HEART_SOUND = "foetal_heart_sound";
    public static final String FOETAL_POSITION = "foetal_position";
    public static final String FOETAL_MOVEMENT = "foetal_movement";
    public static final String REFERRAL_DONE = "referral_done";
    public static final String FAMILY_PLANNING_METHOD = "family_planning_method";
    public static final String ALIVE_FLAG = "alive_flag";
    public static final String DEATH_REASON = "death_reason";
    public static final String DEATH_DATE = "death_date";
    public static final String DEATH_PLACE = "death_place";
    public static final String TITLE = "";

    protected static final Map<String, String> PRE_DELIVERY_CARE_INFO_KEY_ONE = new LinkedHashMap<>();

    protected static final Map<String, String> PRE_DELIVERY_CARE_INFO_KEY_TWO = new LinkedHashMap<>();

    protected static final Map<String, String> PRE_DELIVERY_CARE_INFO_KEY_THREE = new LinkedHashMap<>();

    public static final Map<String, String> PRE_DELIVERY_CARE_INFO_KEY = new LinkedHashMap<>();

    //Delivery Result info
    public static final String DELIVERY_RESULT_JSON = "delivery_result_json";

    public static final String DATE_OF_DELIVERY = "date_of_delivery";
    public static final String DELIVERY_PLACE = "delivery_place";
    public static final String DELIVERY_PLACE_TYPE = "delivery_place_type";
    public static final String DELIVERY_DONE_BY = "delivery_done_by";
    public static final String TYPE_OF_DELIVERY = "type_of_delivery";
    public static final String DISCHARGE_DATE = "discharge_date";
    public static final String ABORTION_PLACE = "abortion_place";
    public static final String IS_ALIVE_DEAD = "is_alive_dead";
    public static final String WAS_PREMATURE = "was_premature";
    public static final String GENDER = "gender";
    public static final String BABY_CRIED_AT_BIRTH = "baby_cried_at_birth";
    public static final String BIRTH_WEIGHT = "birth_weight";
    public static final String BREAST_FEEDING_IN_ONE_HOUR = "breast_feeding_in_one_hour";
    public static final String OPV_GIVEN = "opv_given";
    public static final String BCG_GIVEN = "bcg_given";
    public static final String HEP_B_GIVEN = "hep_b_given";
    public static final String VIT_K_GIVEN = "vit_k_given";

    public static final Map<String, String> DELIVERY_RESULT_BASIC_INFO_KEY = new LinkedHashMap<>();

    public static final Map<String, String> DELIVERY_RESULT_INFANT_INFO_KEY = new LinkedHashMap<>();

    //pnc visit mother info
    public static final String PNC_VISIT_JSON = "pnc_visit_json";

    public static final String SERVICE_DATE = "service_date";
    public static final String IFA_TABLETS_GIVEN = "ifa_tablets_given";
    public static final String OTHER_DANGER_SIGN = "other_danger_sign";
    public static final String REFERRAL_PLACE = "referral_place";
    public static final String FAMILY_PLANNING_METHOD_PNC = "family_planning_method";
    public static final String IS_ALIVE_PNC_MOTHER = "is_alive";
    public static final String DEATH_DATE_PNC_MOTHER = "death_date";
    public static final String DEATH_REASON_PNC_MOTHER = "death_reason";
    public static final String PLACE_OF_DEATH_PNC_MOTHER = "place_of_death";

    public static final Map<String, String> PNC_VISIT_MOTHER_KEY = new LinkedHashMap<>();

    //pnc visit childrend info
    public static final String CHILD_PNC_DTO = "child_pnc_dto";

    public static final String CHILD_WEIGHT = "pnc_child_weight";
    public static final String CHILD_REFERRAL_DONE = "child_referral_done";
    public static final String IS_ALIVE_PNC_CHILD = "is_child_alive";
    public static final String DEATH_DATE_PNC_CHILD = "parsed_death_date";
    public static final String DEATH_REASON_PNC_CHILD = "child_death_reason";
    public static final String PLACE_OF_DEATH_PNC_CHILD = "child_place_of_death";

    public static final Map<String, String> PNC_VISIT_CHILD_KEY = new LinkedHashMap<>();

    public static final String MOTHER_NAME = "mother_name";
    public static final String MOTHER_UNIQUE_HEALTH_ID = "mother_unique_health_id";
    public static final String LAST_GIVEN_CHILD_SERVICE_DATE = "last_child_service_date";
    public static final String WEIGHT = "birth_weight";
    public static final String BIRTH_DATE = "dob";
    public static final String BIRTH_LOCATION = "birth_location";
    public static final String CASTE = "cast";

    public static final Map<String, String> CHILD_GENERAL_INFORMATION_ONE = new LinkedHashMap<>();

    public static final Map<String, String> CHILD_GENERAL_INFORMATION_TWO = new LinkedHashMap<>();

    public static final String BCG = "bcg";
    public static final String OPV_1 = "opv1";
    public static final String OPV_2 = "opv2";
    public static final String OPV_3 = "opv3";
    public static final String PENTAVALENT_1 = "penta1";
    public static final String PENTAVALENT_2 = "penta2";
    public static final String PENTAVALENT_3 = "penta3";
    public static final String ROTA_VIRUS_1 = "rota_virus_1";
    public static final String ROTA_VIRUS_2 = "rota_virus_2";
    public static final String ROTA_VIRUS_3 = "rota_virus_3";
    public static final String MEASLES = "measles";
    public static final String F_IPV_1 = "f_ipv_1";
    public static final String F_IPV_2 = "f_ipv_2";
    public static final String MEASLES_RUBELLA = "measles_rubella";
    public static final String MEASLES_RUBELLA_2 = "measles_rubella_2";
    public static final String FULLY_IMMUN_12_MNTH = "fully_immunization_in_twelve_of_month";
    public static final String OPV_BOOSTER = "opv_booster";
    public static final String DPT_BOOSTER = "dpt_booster";
    public static final String MEASLES_2 = "measles_2";
    public static final String FULLY_IMMUN_2_YR = "fully_immunization_in_two_year";
    public static final String IS_BREASTFED_TILL_SIX_MONTH = "is_breastfed_till_six_month";
    public static final String COMPLEMENTARY_FEEDING_START_PERIOD = "complementary_feeding_start_period";
    public static final String VIT_A_DOSE = "vitamin_a_dose";

    public static final Map<String, String> VACCINATION_ONE = new LinkedHashMap<>();

    public static final Map<String, String> VACCINATION_TWO = new LinkedHashMap<>();

    public static final String DPT_BSTR_DATE_1 = "dpt_booster_given_date";
    public static final String IS_DIARRHEA = "is_diarrhea";
    public static final String PHUEMONIA = "is_pnuemonia";
    public static final String ADVERSE_EFFECT = "dpt_booster_2_adverse_effect";
    public static final String DPT_BSTR_DATE_2 = "dpt_booster_2_given_on";
    public static final String VACC_DETAIL = "vaccination_details";
    public static final String BSTR_CHILD_WEIGHT = "dpt_booster_child_weight";

    public static final Map<String, String> SIDE_EEFECT_OF_VACC_ONE = new LinkedHashMap<>();

    public static final Map<String, String> SIDE_EEFECT_OF_VACC_TWO = new LinkedHashMap<>();

    public static final Map<String, String> VIATMIN_A_DOSE = new LinkedHashMap<>();

    public static final Integer FIRST = 1;
    public static final Integer SECOND = 2;
    public static final Integer THIRD = 3;
    public static final Integer FOURTH = 4;
    public static final Integer FIFTH = 5;
    public static final Integer SIXTH = 6;
    public static final Integer SEVENTH = 7;
    public static final Integer EIGHTH = 8;
    public static final Integer NINTH = 9;
    public static final Integer TENTH = 10;
    public static final Integer ELEVENTH = 11;
    public static final Integer TWELFTH = 12;
    public static final Integer THIRTEENTH = 13;
    public static final Integer FOURTEENTH = 14;
    public static final Integer FIFTEENTH = 15;

    public static final Map<Integer, String> NUBER_TO_STRING_FORMATE_VALUE = new LinkedHashMap<>();

    // rch service type
    public static final String RCH_ELIGIBLE_COUPLE_SERVICE = "rch_eligible_couple_service";
    public static final String RCH_MOTHER_SERVICE = "rch_mother_service";
    public static final String RCH_CHILD_SERVICE = "rch_child_service";

    public static final String REGISTRATION_DATE = "registration_date";
    public static final String MEMBER_MRG_AGE = "member_marriage_age";
    public static final String ELIGIBLE_COUPLE_DATE = "eligible_couple_date";
    public static final String MEMBER_CURRENT_AGE = "member_current_age";
    public static final String MEMBER_AADHAR_NUMBER_AVAILABLE = "member_aadhar_number_available";
    public static final String MEMBER_BANK_ACCOUNT_NUMBER_AVAILABLE = "member_bank_account_number_available";
    public static final String HUSBAND_CURRENT_AGE = "husband_current_age";
    public static final String HUSBAND_MRG_AGE = "husband_marriage_age";
    public static final String BPL_APL = "bpl_apl";
    public static final String TOTAL_GIVEN_MALE_BIRTH = "total_given_male_birth";
    public static final String TOTAL_GIVEN_FEMALE_BIRTH = "total_given_female_birth";
    public static final String LIVE_MALE_BIRTH = "live_male_birth";
    public static final String LIVE_FEMALE_BIRTH = "live_female_birth";
    public static final String SML_CHILD_AGE = "smallest_child_age";
    public static final String SML_CHILD_GENDER = "smallest_child_gender";

    protected static final Map<String, String> ELIGIBLE_COUPLE_GENERAL_INFORMATION_ONE = new LinkedHashMap<>();

    protected static final Map<String, String> ELIGIBLE_COUPLE_GENERAL_INFORMATION_TWO = new LinkedHashMap<>();

    protected static final Map<String, String> ELIGIBLE_COUPLE_GENERAL_INFORMATION_THREE = new LinkedHashMap<>();

    public static final String DATE = "date";
    public static final String CONTRACEPTION_METHOD = "contraception_method";
    public static final String LMP_VISIT_INFO = "lmp_visit_info";

    protected static final Map<String, String> LMP_VISIT_INFORMATION = new LinkedHashMap<>();

    // rch register mother service constancts
    // different section of infomation for mother service
    public static final String PREVIOUS_PREGNANCY_DETAILS = "PREVIOUS PREGNANCY DETAILS";
    public static final String DELIVERY_RESULT_INFORMATION = "Delivery Result Information";
    public static final String PNC_VISIT = "PNC Visit";
    public static final String PRE_DELIVERY_CARE_INFORMATION = "Pre Delivery Care Information";

    public static final Map<String, Integer> RCH_REGISTER_MOTHER_SERVICE_TITLE = new LinkedHashMap<>();

    // full list of key constant of mother service
    public static final Map<String, String> RCH_REGISTER_MOTHER_SERVICE_KEY_LIST = new LinkedHashMap<>();

    // rch register child service constancts
    // different section of infomation for child service
    public static final String GENERAL_INFORMATION = "GENERAL INFORMATION";
    public static final String FOR_INDEX_NUMBER = "FOR_INDEX_NUMBER";
    public static final String VACCINATION = "VACCINATION";
    public static final String SIDE_EFFECTS_OF_VACCINATION = "SIDE EFFECTS OF VACCINATION";
    public static final String VITAMIN_A_DOSE = "VITAMIN A DOSE";

    public static final Map<String, Integer> RCH_REGISTER_CHILD_SERVICE_TITLE = new LinkedHashMap<>();

    // full list of key constant of mother service
    public static final Map<String, String> RCH_REGISTER_CHILD_SERVICE_KEY_LIST = new LinkedHashMap<>();

    // rch register elgible couple service constancts
    // different section of infomation for child service
    public static final String MEMBER_INFO = "Member Information";
    public static final String HUSBAND_INFO = "Husdand Information";
    public static final String FAMILY_INFO = "Family Information";
    public static final String TOTAL_CHILD_BIRTH = "Total Number Of Child Birth";
    public static final String LIVE_CHILDREN_COUNT = "Live Children Count";
    public static final String SMALLEST_CHILDREN_COUNT = "Smallest Child Information";
    public static final String LMP_VISIT_INFO_TITLE = "LMP VISIT INFO(VISIT DATE/CONTRACEPTION METHOD)";

    public static final Map<String, String> ELIGIBLE_COUPLE_MEMBER_INFO_KEY = new LinkedHashMap<>();

    public static final Map<String, String> ELIGIBLE_COUPLE_HUSBAND_INFO_KEY = new LinkedHashMap<>();

    public static final Map<String, String> ELIGIBLE_COUPLE_FAMILY_INFO_KEY = new LinkedHashMap<>();

    public static final Map<String, String> ELIGIBLE_COUPLE_TOTAL_CHILD_BIRTH_KEY = new LinkedHashMap<>();

    public static final Map<String, String> ELIGIBLE_COUPLE_LIVE_CHILDREN_COUNT_KEY = new LinkedHashMap<>();

    public static final Map<String, String> ELIGIBLE_COUPLE_SMALLEST_CHILDREN_COUNT_KEY = new LinkedHashMap<>();

    public static final Map<String, String> ELIGIBLE_COUPLE_LMP_VISIT = new LinkedHashMap<>();

    public static final Map<String, Integer> RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_TITLE = new LinkedHashMap<>();

    // full list of key constant of mother service
    public static final Map<String, String> RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_KEY_LIST = new LinkedHashMap<>();

    public static final Map<String, String> RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_VALUE_KEY_TO_LIST = new LinkedHashMap<>();

    // Abbreviation and full form map for appendix
    public static final Map<String, String> ELIGIBLE_COUPLE_SERVICE_APPENDIX = new LinkedHashMap<>();
    public static final Map<String, String> CHILD_SERVICE_APPENDIX = new LinkedHashMap<>();
    public static final Map<String, String> MOTHER_SERVICE_APPENDIX = new LinkedHashMap<>();

    static {

        //GENERAL_INFORMATION_KEYS
        GENERAL_INFORMATION_KEYS.put(SR_NO, SR_NUMBER);
        GENERAL_INFORMATION_KEYS.put(UNIQUE_HEALTH_ID, "Health Id");
        GENERAL_INFORMATION_KEYS.put(REG_SERVICE_DATE, "Date of Registration");
        GENERAL_INFORMATION_KEYS.put(MEMBER_NAME, "Pregnant Woman Name");
        GENERAL_INFORMATION_KEYS.put(HUSBAND_NAME, CONST_HUSBAND_NAME);
        GENERAL_INFORMATION_KEYS.put(AGE_DURING_DELIVERY, "P.W. Age (D.O.B.)");
        GENERAL_INFORMATION_KEYS.put(BLOOD_GROUP, "Blood Group of P.W.");
        GENERAL_INFORMATION_KEYS.put(ADDRESS, FULL_ADDRESS);
        GENERAL_INFORMATION_KEYS.put(MOBILE_NUMBER, "Mobile Number");
        GENERAL_INFORMATION_KEYS.put(RELIGION, "Religion / Caste");
        GENERAL_INFORMATION_KEYS.put(BPL_FLAG, "BPL / APL");
        GENERAL_INFORMATION_KEYS.put(LMP_DATE, "Last Period Date");
        GENERAL_INFORMATION_KEYS.put(PREGNANCY_WEEK_NUMBER, "Pregnancy Week Number");
        GENERAL_INFORMATION_KEYS.put(TWELVE_PREGNANCY_WEEK_NUMBER, "Registered in 12 Week of Pregnancy");
        GENERAL_INFORMATION_KEYS.put(EDD, "Expected Delivery Date");
        GENERAL_INFORMATION_KEYS.put(EXPECTED_DELIVERY_PLACE, "Expected Place of Delivery");
        GENERAL_INFORMATION_KEYS.put(PREVIOUS_COMPLICATION, "Previous Complication");

        //PREVIOUS_PREGNANCY_DETAILS_KEYS
        PREVIOUS_PREGNANCY_DETAILS_KEYS.put(PREG_COMPLICATION, "Complication in Pregnancy");
        PREVIOUS_PREGNANCY_DETAILS_KEYS.put(PREG_GENDER, "Result of First Pregnancy");

        //PRE_DELIVERY_CARE_INFO_KEY_ONE
        PRE_DELIVERY_CARE_INFO_KEY_ONE.put(INSPECTION_DATE, "Date of Inspection");
        PRE_DELIVERY_CARE_INFO_KEY_ONE.put(INSPECTION_PLACE, "Place Name");
        PRE_DELIVERY_CARE_INFO_KEY_ONE.put(INSPECTION_PLACE_TYPE, "Place Type");
        PRE_DELIVERY_CARE_INFO_KEY_ONE.put(PREGNANCY_WEEK, "Pregnancy Week");
        PRE_DELIVERY_CARE_INFO_KEY_ONE.put(PREG_WOMEN_WEIGHT, "P.W. Weight");
        PRE_DELIVERY_CARE_INFO_KEY_ONE.put(HIV_TEST, "HIV Test");
        PRE_DELIVERY_CARE_INFO_KEY_ONE.put(VDRL_TEST, "VDRL Test");
        PRE_DELIVERY_CARE_INFO_KEY_ONE.put(SYSTOLIC_BP, "Systolic BP");
        PRE_DELIVERY_CARE_INFO_KEY_ONE.put(DIASTOLIC_BP, "Diastolic BP");
        PRE_DELIVERY_CARE_INFO_KEY_ONE.put(HAEMOGLOBIN_COUNT, "HB gm");
        PRE_DELIVERY_CARE_INFO_KEY_ONE.put(FALIC_ACID_TABLETS, "Folic Acid Tablet");

        //PRE_DELIVERY_CARE_INFO_KEY_TWO
        PRE_DELIVERY_CARE_INFO_KEY_TWO.put(IFA_TABLET, "IFA Tablet");
        PRE_DELIVERY_CARE_INFO_KEY_TWO.put(URINE_ALBUMIN, "Albumin");
        PRE_DELIVERY_CARE_INFO_KEY_TWO.put(URINE_SUGAR, "Sugar");
        PRE_DELIVERY_CARE_INFO_KEY_TWO.put(SUGAR_TEST_BEFORE_FOOD_VAL, "Before Lunch");
        PRE_DELIVERY_CARE_INFO_KEY_TWO.put(SUGAR_TEST_AFTER_FOOD_VAL, "After Lunch");
        PRE_DELIVERY_CARE_INFO_KEY_TWO.put(TT1, "T.T. Fist Dose");
        PRE_DELIVERY_CARE_INFO_KEY_TWO.put(TT2, "T.T. Second Dose");
        PRE_DELIVERY_CARE_INFO_KEY_TWO.put(TT_BOOSTER, "T. T. Booster");
        PRE_DELIVERY_CARE_INFO_KEY_TWO.put(FOETAL_HEIGHT, "Height of Foeta(cm)");
        PRE_DELIVERY_CARE_INFO_KEY_TWO.put(FOETAL_HEART_SOUND, "FHS");

        //PRE_DELIVERY_CARE_INFO_KEY_THREE
        PRE_DELIVERY_CARE_INFO_KEY_THREE.put(FOETAL_POSITION, "FP");
        PRE_DELIVERY_CARE_INFO_KEY_THREE.put(FOETAL_MOVEMENT, "Movement of Fetus");
        PRE_DELIVERY_CARE_INFO_KEY_THREE.put(REFERRAL_DONE, "Refer Facility");
        PRE_DELIVERY_CARE_INFO_KEY_THREE.put(FAMILY_PLANNING_METHOD, "Any Contraceptive Method Post Pregnancy");
        PRE_DELIVERY_CARE_INFO_KEY_THREE.put(ALIVE_FLAG, "Is Mother Alive");
        PRE_DELIVERY_CARE_INFO_KEY_THREE.put(DEATH_DATE, "Death Date");
        PRE_DELIVERY_CARE_INFO_KEY_THREE.put(DEATH_PLACE, "Death Place");
        PRE_DELIVERY_CARE_INFO_KEY_THREE.put(DEATH_REASON, REASON_FOR_DEATH);

        //PRE_DELIVERY_CARE_INFO_KEY
        PRE_DELIVERY_CARE_INFO_KEY.putAll(PRE_DELIVERY_CARE_INFO_KEY_ONE);
        PRE_DELIVERY_CARE_INFO_KEY.putAll(PRE_DELIVERY_CARE_INFO_KEY_TWO);
        PRE_DELIVERY_CARE_INFO_KEY.putAll(PRE_DELIVERY_CARE_INFO_KEY_THREE);

        //DELIVERY_RESULT_BASIC_INFO_KEY
        DELIVERY_RESULT_BASIC_INFO_KEY.put(DATE_OF_DELIVERY, "Date and Time of Delivery");
        DELIVERY_RESULT_BASIC_INFO_KEY.put(DELIVERY_PLACE, "Place Name of Delivery");
        DELIVERY_RESULT_BASIC_INFO_KEY.put(DELIVERY_PLACE_TYPE, "Place Type of Delivery");
        DELIVERY_RESULT_BASIC_INFO_KEY.put(DELIVERY_DONE_BY, "Role");
        DELIVERY_RESULT_BASIC_INFO_KEY.put(TYPE_OF_DELIVERY, "Type of Delivery");
        DELIVERY_RESULT_BASIC_INFO_KEY.put(DISCHARGE_DATE, "Date and Time of Discharge");
        DELIVERY_RESULT_BASIC_INFO_KEY.put(ABORTION_PLACE, "Abortion place(Hospital)");

        //DELIVERY_RESULT_INFANT_INFO_KEY
        DELIVERY_RESULT_INFANT_INFO_KEY.put(IS_ALIVE_DEAD, "Alive or Dead");
        DELIVERY_RESULT_INFANT_INFO_KEY.put(WAS_PREMATURE, "Full Month or Half Month");
        DELIVERY_RESULT_INFANT_INFO_KEY.put(GENDER, "Infant Gender");
        DELIVERY_RESULT_INFANT_INFO_KEY.put(BABY_CRIED_AT_BIRTH, "Infant Cried");
        DELIVERY_RESULT_INFANT_INFO_KEY.put(BIRTH_WEIGHT, "Infant Weight(in Kg)");
        DELIVERY_RESULT_INFANT_INFO_KEY.put(BREAST_FEEDING_IN_ONE_HOUR, "Infant Breastfeeding started in 1 hour of birth?");
        DELIVERY_RESULT_INFANT_INFO_KEY.put(OPV_GIVEN, "OPV");
        DELIVERY_RESULT_INFANT_INFO_KEY.put(BCG_GIVEN, "BCG");
        DELIVERY_RESULT_INFANT_INFO_KEY.put(HEP_B_GIVEN, "HEP B");
        DELIVERY_RESULT_INFANT_INFO_KEY.put(VIT_K_GIVEN, "VIT K");

        //PNC_VISIT_MOTHER_KEY
        PNC_VISIT_MOTHER_KEY.put(SERVICE_DATE, "Service Date");
        PNC_VISIT_MOTHER_KEY.put(IFA_TABLETS_GIVEN, "Number of IFA Pills Given");
        PNC_VISIT_MOTHER_KEY.put(OTHER_DANGER_SIGN, "Any Complication");
        PNC_VISIT_MOTHER_KEY.put(REFERRAL_PLACE, "Refer for Advanced Treatment");
        PNC_VISIT_MOTHER_KEY.put(FAMILY_PLANNING_METHOD_PNC, "Any Contraceptive Used");
        PNC_VISIT_MOTHER_KEY.put(IS_ALIVE_PNC_MOTHER, "Is mother alive?");
        PNC_VISIT_MOTHER_KEY.put(DEATH_DATE_PNC_MOTHER, "Date of Death");
        PNC_VISIT_MOTHER_KEY.put(DEATH_REASON_PNC_MOTHER, REASON_FOR_DEATH);
        PNC_VISIT_MOTHER_KEY.put(PLACE_OF_DEATH_PNC_MOTHER, "Place of Death");

        //PNC_VISIT_CHILD_KEY
        PNC_VISIT_CHILD_KEY.put(CHILD_WEIGHT, "Weight(in kg)");
        PNC_VISIT_CHILD_KEY.put(CHILD_REFERRAL_DONE, "Refer for Advanced Treatment");
        PNC_VISIT_CHILD_KEY.put(IS_ALIVE_PNC_CHILD, "Is infant alive?");
        PNC_VISIT_CHILD_KEY.put(DEATH_DATE_PNC_CHILD, "Date of Death");
        PNC_VISIT_CHILD_KEY.put(DEATH_REASON_PNC_CHILD, REASON_FOR_DEATH);
        PNC_VISIT_CHILD_KEY.put(PLACE_OF_DEATH_PNC_CHILD, "Place of Death");

        //CHILD_GENERAL_INFORMATION_ONE
        CHILD_GENERAL_INFORMATION_ONE.put(SR_NO, SR_NUMBER);
        CHILD_GENERAL_INFORMATION_ONE.put(MEMBER_NAME, "Child Name");
        CHILD_GENERAL_INFORMATION_ONE.put(GENDER, "Gender");
        CHILD_GENERAL_INFORMATION_ONE.put(WEIGHT, "Weight");
        CHILD_GENERAL_INFORMATION_ONE.put(ADDRESS, FULL_ADDRESS);
        CHILD_GENERAL_INFORMATION_ONE.put(MOTHER_NAME, "Mother name");
        CHILD_GENERAL_INFORMATION_ONE.put(LAST_GIVEN_CHILD_SERVICE_DATE, "Last given child service date");

        //CHILD_GENERAL_INFORMATION_TWO
        CHILD_GENERAL_INFORMATION_TWO.put(BIRTH_DATE, "Birth date");
        CHILD_GENERAL_INFORMATION_TWO.put(BIRTH_LOCATION, "Birth location");
        CHILD_GENERAL_INFORMATION_TWO.put(RELIGION, CONST_RELIGION);
        CHILD_GENERAL_INFORMATION_TWO.put(CASTE, CONST_CASTE);

        //VACCINATION_ONE
        VACCINATION_ONE.put(BCG, "BCG");
        VACCINATION_ONE.put(OPV_1, "OPV 1");
        VACCINATION_ONE.put(PENTAVALENT_1, "Pentavalent 1");
        VACCINATION_ONE.put(OPV_2, "OPV 2");
        VACCINATION_ONE.put(PENTAVALENT_2, "Pentavalent 2");
        VACCINATION_ONE.put(OPV_3, "OPV 3");
        VACCINATION_ONE.put(PENTAVALENT_3, "Pentavalent 3");
        VACCINATION_ONE.put(MEASLES, "Measles");
        VACCINATION_ONE.put(F_IPV_1, "F_IPV 1");
        VACCINATION_ONE.put(F_IPV_2, "F_IPV 2");
        VACCINATION_ONE.put(MEASLES_RUBELLA, "Measles Rubella");
        VACCINATION_ONE.put(MEASLES_RUBELLA_2, "Measles Rubella 2");
        VACCINATION_ONE.put(FULLY_IMMUN_12_MNTH, "Fully immunization in twelve Month?");


        //VACCINATION_TWO
        VACCINATION_TWO.put(OPV_BOOSTER, "OPV booster");
        VACCINATION_TWO.put(DPT_BOOSTER, "DPT booster 1");
        VACCINATION_TWO.put(MEASLES_2, "Measles 2");
        VACCINATION_TWO.put(FULLY_IMMUN_2_YR, "Fully immunization in two year?");
        VACCINATION_TWO.put(IS_BREASTFED_TILL_SIX_MONTH, "Is Breastfed till six month?");
        VACCINATION_TWO.put(COMPLEMENTARY_FEEDING_START_PERIOD, "Complementary Feeding Start Period");

        //SIDE_EEFECT_OF_VACC_ONE
        SIDE_EEFECT_OF_VACC_ONE.put(DPT_BSTR_DATE_1, "DTP booster Given Date");
        SIDE_EEFECT_OF_VACC_ONE.put(BSTR_CHILD_WEIGHT, "Child Weight");
        SIDE_EEFECT_OF_VACC_ONE.put(IS_DIARRHEA, "Is Diarrhea");
        SIDE_EEFECT_OF_VACC_ONE.put(PHUEMONIA, "Is Pneumonia");

        //SIDE_EEFECT_OF_VACC_TWO
        SIDE_EEFECT_OF_VACC_TWO.put(DPT_BSTR_DATE_2, "DTP booster2 given date");
        SIDE_EEFECT_OF_VACC_TWO.put(ADVERSE_EFFECT, "Adverse effect");
        SIDE_EEFECT_OF_VACC_TWO.put(VACC_DETAIL, "Vaccination details");

        //VIATMIN_A_DOSE
        VIATMIN_A_DOSE.put("FIRST", "FIRST Dose");
        VIATMIN_A_DOSE.put("SECOND", "SECOND Dose");
        VIATMIN_A_DOSE.put("THIRD", "THIRD Dose");
        VIATMIN_A_DOSE.put("FOURTH", "FOURTH Dose");
        VIATMIN_A_DOSE.put("FIFTH", "FIFTH Dose");
        VIATMIN_A_DOSE.put("SIXTH", "SIXTH Dose");
        VIATMIN_A_DOSE.put("SEVENTH", "SEVENTH Dose");
        VIATMIN_A_DOSE.put("EIGHTH", "EIGHTH Dose");
        VIATMIN_A_DOSE.put("NINTH", "NINTH Dose");
        VIATMIN_A_DOSE.put("TENTH", "TENTH Dose");

        //NUBER_TO_STRING_FORMATE_VALUE
        NUBER_TO_STRING_FORMATE_VALUE.put(FIRST, "First");
        NUBER_TO_STRING_FORMATE_VALUE.put(SECOND, "Second");
        NUBER_TO_STRING_FORMATE_VALUE.put(THIRD, "Third");
        NUBER_TO_STRING_FORMATE_VALUE.put(FOURTH, "Fourth");
        NUBER_TO_STRING_FORMATE_VALUE.put(FIFTH, "Fifth");
        NUBER_TO_STRING_FORMATE_VALUE.put(SIXTH, "Sixth");
        NUBER_TO_STRING_FORMATE_VALUE.put(SEVENTH, "Seventh");
        NUBER_TO_STRING_FORMATE_VALUE.put(EIGHTH, "Eighth");
        NUBER_TO_STRING_FORMATE_VALUE.put(NINTH, "Ninth");
        NUBER_TO_STRING_FORMATE_VALUE.put(TENTH, "Tenth");
        NUBER_TO_STRING_FORMATE_VALUE.put(ELEVENTH, "Eleventh");
        NUBER_TO_STRING_FORMATE_VALUE.put(TWELFTH, "Twelfth");
        NUBER_TO_STRING_FORMATE_VALUE.put(THIRTEENTH, "Thirteenth");
        NUBER_TO_STRING_FORMATE_VALUE.put(FOURTEENTH, "Fourteenth");
        NUBER_TO_STRING_FORMATE_VALUE.put(FIFTEENTH, "Fifteenth");

        //ELIGIBLE_COUPLE_GENERAL_INFORMATION_ONE
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_ONE.put(REGISTRATION_DATE, "Registration date");
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_ONE.put(MEMBER_NAME, "Member Name");
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_ONE.put(UNIQUE_HEALTH_ID, "Unique Health Id");
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_ONE.put(MEMBER_CURRENT_AGE, "Age(at present)");
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_ONE.put(MEMBER_MRG_AGE, "Age(at the marriage time)");
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_ONE.put(ELIGIBLE_COUPLE_DATE, "Eligible couple date");
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_ONE.put(MEMBER_AADHAR_NUMBER_AVAILABLE, "Aadhar Number available");
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_ONE.put(MEMBER_BANK_ACCOUNT_NUMBER_AVAILABLE, "Bank Account Number available");

        //ELIGIBLE_COUPLE_GENERAL_INFORMATION_TWO
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_TWO.put(HUSBAND_NAME, CONST_HUSBAND_NAME);
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_TWO.put(HUSBAND_CURRENT_AGE, "Husband Age(at present)");
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_TWO.put(HUSBAND_MRG_AGE, "Husband Age(at the marriage time)");
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_TWO.put(ADDRESS, FULL_ADDRESS);
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_TWO.put(RELIGION, CONST_RELIGION);
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_TWO.put(CASTE, CONST_CASTE);
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_TWO.put(BPL_APL, "APL / BPL");

        //ELIGIBLE_COUPLE_GENERAL_INFORMATION_THREE
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_THREE.put(TOTAL_GIVEN_MALE_BIRTH, "Total Number Of Child Birth Male");
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_THREE.put(TOTAL_GIVEN_FEMALE_BIRTH, "Total Number Of Child Birth Female");
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_THREE.put(LIVE_MALE_BIRTH, "Live Children Count Male");
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_THREE.put(LIVE_FEMALE_BIRTH, "Live Children Count Female");
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_THREE.put(SML_CHILD_AGE, "Smallest Child Age");
        ELIGIBLE_COUPLE_GENERAL_INFORMATION_THREE.put(SML_CHILD_GENDER, "Smallest Child Gender");

        //LMP_VISIT_INFORMATION
        LMP_VISIT_INFORMATION.put(DATE, "Date");
        LMP_VISIT_INFORMATION.put(CONTRACEPTION_METHOD, "Contraception method");

        //RCH_REGISTER_MOTHER_SERVICE_TITLE
        RCH_REGISTER_MOTHER_SERVICE_TITLE.put(GENERAL_INFORMATION, GENERAL_INFORMATION_KEYS.size());
        RCH_REGISTER_MOTHER_SERVICE_TITLE.put(PREVIOUS_PREGNANCY_DETAILS, PREVIOUS_PREGNANCY_DETAILS_KEYS.size());
        RCH_REGISTER_MOTHER_SERVICE_TITLE.put(PRE_DELIVERY_CARE_INFORMATION, PRE_DELIVERY_CARE_INFO_KEY.size());
        RCH_REGISTER_MOTHER_SERVICE_TITLE.put(DELIVERY_RESULT_INFORMATION, DELIVERY_RESULT_BASIC_INFO_KEY.size() + DELIVERY_RESULT_INFANT_INFO_KEY.size());
        RCH_REGISTER_MOTHER_SERVICE_TITLE.put(PNC_VISIT, PNC_VISIT_MOTHER_KEY.size() + PNC_VISIT_CHILD_KEY.size());

        //RCH_REGISTER_MOTHER_SERVICE_KEY_LIST
        RCH_REGISTER_MOTHER_SERVICE_KEY_LIST.putAll(GENERAL_INFORMATION_KEYS); //15
        RCH_REGISTER_MOTHER_SERVICE_KEY_LIST.putAll(PREVIOUS_PREGNANCY_DETAILS_KEYS); //2
        RCH_REGISTER_MOTHER_SERVICE_KEY_LIST.putAll(PRE_DELIVERY_CARE_INFO_KEY_ONE);//8
        RCH_REGISTER_MOTHER_SERVICE_KEY_LIST.putAll(PRE_DELIVERY_CARE_INFO_KEY_TWO);//8
        RCH_REGISTER_MOTHER_SERVICE_KEY_LIST.putAll(PRE_DELIVERY_CARE_INFO_KEY_THREE);//6

        RCH_REGISTER_MOTHER_SERVICE_KEY_LIST.putAll(DELIVERY_RESULT_BASIC_INFO_KEY); //5
        RCH_REGISTER_MOTHER_SERVICE_KEY_LIST.putAll(DELIVERY_RESULT_INFANT_INFO_KEY);  //10

        //RCH_REGISTER_CHILD_SERVICE_TITLE
        RCH_REGISTER_CHILD_SERVICE_TITLE.put(GENERAL_INFORMATION, CHILD_GENERAL_INFORMATION_ONE.size() + CHILD_GENERAL_INFORMATION_TWO.size());
        RCH_REGISTER_CHILD_SERVICE_TITLE.put(VACCINATION, VACCINATION_ONE.size() + VACCINATION_TWO.size() + 1);
        RCH_REGISTER_CHILD_SERVICE_TITLE.put(SIDE_EFFECTS_OF_VACCINATION, SIDE_EEFECT_OF_VACC_ONE.size() + SIDE_EEFECT_OF_VACC_TWO.size());
        RCH_REGISTER_CHILD_SERVICE_TITLE.put(VITAMIN_A_DOSE, VIATMIN_A_DOSE.size());

        //RCH_REGISTER_CHILD_SERVICE_KEY_LIST
        RCH_REGISTER_CHILD_SERVICE_KEY_LIST.putAll(CHILD_GENERAL_INFORMATION_ONE); //5
        RCH_REGISTER_CHILD_SERVICE_KEY_LIST.putAll(CHILD_GENERAL_INFORMATION_TWO); //4

        RCH_REGISTER_CHILD_SERVICE_KEY_LIST.putAll(VACCINATION_ONE);//8
        RCH_REGISTER_CHILD_SERVICE_KEY_LIST.putAll(VACCINATION_TWO);//3
        RCH_REGISTER_CHILD_SERVICE_KEY_LIST.putAll(SIDE_EEFECT_OF_VACC_ONE); //4
        RCH_REGISTER_CHILD_SERVICE_KEY_LIST.putAll(SIDE_EEFECT_OF_VACC_TWO);  //3
        RCH_REGISTER_CHILD_SERVICE_KEY_LIST.putAll(VIATMIN_A_DOSE);

        //ELIGIBLE_COUPLE_MEMBER_INFO_KEY
        ELIGIBLE_COUPLE_MEMBER_INFO_KEY.put(SR_NO, SR_NUMBER);
        ELIGIBLE_COUPLE_MEMBER_INFO_KEY.put(REGISTRATION_DATE, "Registration date");
        ELIGIBLE_COUPLE_MEMBER_INFO_KEY.put(MEMBER_NAME, "Member Name");
        ELIGIBLE_COUPLE_MEMBER_INFO_KEY.put(UNIQUE_HEALTH_ID, "Unique Health Id");
        ELIGIBLE_COUPLE_MEMBER_INFO_KEY.put(MEMBER_CURRENT_AGE, "Age(at present)");
        ELIGIBLE_COUPLE_MEMBER_INFO_KEY.put(MEMBER_MRG_AGE, "Age(at the marriage time)");
        ELIGIBLE_COUPLE_MEMBER_INFO_KEY.put(ELIGIBLE_COUPLE_DATE, "Eligible couple date");
        ELIGIBLE_COUPLE_MEMBER_INFO_KEY.put(MEMBER_AADHAR_NUMBER_AVAILABLE, "Aadhar Number available");
        ELIGIBLE_COUPLE_MEMBER_INFO_KEY.put(MEMBER_BANK_ACCOUNT_NUMBER_AVAILABLE, "Bank Account Number available");

        //ELIGIBLE_COUPLE_HUSBAND_INFO_KEY
        ELIGIBLE_COUPLE_HUSBAND_INFO_KEY.put(HUSBAND_NAME, CONST_HUSBAND_NAME);
        ELIGIBLE_COUPLE_HUSBAND_INFO_KEY.put(HUSBAND_CURRENT_AGE, "Husband Age(at present)");
        ELIGIBLE_COUPLE_HUSBAND_INFO_KEY.put(HUSBAND_MRG_AGE, "Husband Age(at the marriage time)");

        //ELIGIBLE_COUPLE_FAMILY_INFO_KEY
        ELIGIBLE_COUPLE_FAMILY_INFO_KEY.put(ADDRESS, FULL_ADDRESS);
        ELIGIBLE_COUPLE_FAMILY_INFO_KEY.put(RELIGION, CONST_RELIGION);
        ELIGIBLE_COUPLE_FAMILY_INFO_KEY.put(CASTE, CONST_CASTE);
        ELIGIBLE_COUPLE_FAMILY_INFO_KEY.put(BPL_APL, "AVL / BPL");

        //ELIGIBLE_COUPLE_TOTAL_CHILD_BIRTH_KEY
        ELIGIBLE_COUPLE_TOTAL_CHILD_BIRTH_KEY.put(TOTAL_GIVEN_MALE_BIRTH, "Total Number Of Child Birth Male");
        ELIGIBLE_COUPLE_TOTAL_CHILD_BIRTH_KEY.put(TOTAL_GIVEN_FEMALE_BIRTH, "Total Number Of Child Birth Female");

        //ELIGIBLE_COUPLE_LIVE_CHILDREN_COUNT_KEY
        ELIGIBLE_COUPLE_LIVE_CHILDREN_COUNT_KEY.put(LIVE_MALE_BIRTH, "Live Children Count Male");
        ELIGIBLE_COUPLE_LIVE_CHILDREN_COUNT_KEY.put(LIVE_FEMALE_BIRTH, "Live Children Count Female");

        //ELIGIBLE_COUPLE_SMALLEST_CHILDREN_COUNT_KEY
        ELIGIBLE_COUPLE_SMALLEST_CHILDREN_COUNT_KEY.put(SML_CHILD_AGE, "Smallest Child Age");
        ELIGIBLE_COUPLE_SMALLEST_CHILDREN_COUNT_KEY.put(SML_CHILD_GENDER, "Smallest Child Gender");

        //ELIGIBLE_COUPLE_LMP_VISIT
        ELIGIBLE_COUPLE_LMP_VISIT.put("FIRST", "First Visit");
        ELIGIBLE_COUPLE_LMP_VISIT.put("SECOND", "Second Visit");
        ELIGIBLE_COUPLE_LMP_VISIT.put("THIRD", "Third Visit");
        ELIGIBLE_COUPLE_LMP_VISIT.put("FOURTH", "Fourth Visit");
        ELIGIBLE_COUPLE_LMP_VISIT.put("FIFTH", "Fifth Visit");
        ELIGIBLE_COUPLE_LMP_VISIT.put("SIXTH", "Sixth Visit");
        ELIGIBLE_COUPLE_LMP_VISIT.put("SEVENTH", "Seventh Visit");
        ELIGIBLE_COUPLE_LMP_VISIT.put("EIGHTH", "Eighth Visit");

        //RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_TITLE
        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_TITLE.put(MEMBER_INFO, ELIGIBLE_COUPLE_MEMBER_INFO_KEY.size());
        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_TITLE.put(HUSBAND_INFO, ELIGIBLE_COUPLE_HUSBAND_INFO_KEY.size());
        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_TITLE.put(FAMILY_INFO, ELIGIBLE_COUPLE_FAMILY_INFO_KEY.size());
        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_TITLE.put(TOTAL_CHILD_BIRTH, ELIGIBLE_COUPLE_TOTAL_CHILD_BIRTH_KEY.size());
        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_TITLE.put(LIVE_CHILDREN_COUNT, ELIGIBLE_COUPLE_LIVE_CHILDREN_COUNT_KEY.size());
        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_TITLE.put(SMALLEST_CHILDREN_COUNT, ELIGIBLE_COUPLE_SMALLEST_CHILDREN_COUNT_KEY.size());
        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_TITLE.put(LMP_VISIT_INFO_TITLE, ELIGIBLE_COUPLE_LMP_VISIT.size());

        //RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_KEY_LIST
        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_KEY_LIST.putAll(ELIGIBLE_COUPLE_MEMBER_INFO_KEY); //5
        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_KEY_LIST.putAll(ELIGIBLE_COUPLE_HUSBAND_INFO_KEY); //4

        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_KEY_LIST.putAll(ELIGIBLE_COUPLE_FAMILY_INFO_KEY);//8
        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_KEY_LIST.putAll(ELIGIBLE_COUPLE_TOTAL_CHILD_BIRTH_KEY);//3

        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_KEY_LIST.putAll(ELIGIBLE_COUPLE_LIVE_CHILDREN_COUNT_KEY); //4
        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_KEY_LIST.putAll(ELIGIBLE_COUPLE_SMALLEST_CHILDREN_COUNT_KEY);  //3
        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_KEY_LIST.putAll(ELIGIBLE_COUPLE_LMP_VISIT);  //3

        //RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_VALUE_KEY_TO_LIST
        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_VALUE_KEY_TO_LIST.putAll(ELIGIBLE_COUPLE_MEMBER_INFO_KEY); //5
        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_VALUE_KEY_TO_LIST.putAll(ELIGIBLE_COUPLE_HUSBAND_INFO_KEY); //4

        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_VALUE_KEY_TO_LIST.putAll(ELIGIBLE_COUPLE_FAMILY_INFO_KEY);//8
        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_VALUE_KEY_TO_LIST.putAll(ELIGIBLE_COUPLE_TOTAL_CHILD_BIRTH_KEY);//3

        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_VALUE_KEY_TO_LIST.putAll(ELIGIBLE_COUPLE_LIVE_CHILDREN_COUNT_KEY); //4
        RCH_REGISTER_ELIGIBLE_COUPLE_SERVICE_VALUE_KEY_TO_LIST.putAll(ELIGIBLE_COUPLE_SMALLEST_CHILDREN_COUNT_KEY);  //3


        // ELIGIBLE_COUPLE_SERVICE_APPENDIX
        ELIGIBLE_COUPLE_SERVICE_APPENDIX.put("APL", ABOVE_POVERTY_LINE);
        ELIGIBLE_COUPLE_SERVICE_APPENDIX.put("BPL", ABOVE_POVERTY_LINE);

        // CHILD_SERVICE_APPENDIX
        CHILD_SERVICE_APPENDIX.put("TD", "Tetanus, Diphtheria");
        CHILD_SERVICE_APPENDIX.put("DPT", "Diphtheria, Pertussis(Whooping Cough) and Tetanus");
        CHILD_SERVICE_APPENDIX.put("OPV", "Oral Polio Vaccine");
        CHILD_SERVICE_APPENDIX.put("F IPV", "Fractional Inactivated Poliovirus Vaccine");
        CHILD_SERVICE_APPENDIX.put("BCG", "Bacille Calmette Guerin");
        CHILD_SERVICE_APPENDIX.put("HEP B", "Hepatitis B");
        CHILD_SERVICE_APPENDIX.put("VIT K", "Vitamin K");

        // MOTHER_SERVICE_APPENDIX
        MOTHER_SERVICE_APPENDIX.put("LMP", "Last Menstrual Period");
        MOTHER_SERVICE_APPENDIX.put("APL", ABOVE_POVERTY_LINE);
        MOTHER_SERVICE_APPENDIX.put("BPL", "Below Poverty Line");
        MOTHER_SERVICE_APPENDIX.put("PW", "Pregnant Woman");
        MOTHER_SERVICE_APPENDIX.put("D.O.B.", "Date Of Birth");
        MOTHER_SERVICE_APPENDIX.put("PHC", "Primary Healthcare Center");
        MOTHER_SERVICE_APPENDIX.put("HIV", "Human Immunodeficiency Virus");
        MOTHER_SERVICE_APPENDIX.put("VDRL", "Venereal Disease Research Laboratory");
        MOTHER_SERVICE_APPENDIX.put("BP", "Blood Pressure");
        MOTHER_SERVICE_APPENDIX.put("IFA", "Iron Folic Acid");
        MOTHER_SERVICE_APPENDIX.put("T.T.", "Tetanus Toxoid");
        MOTHER_SERVICE_APPENDIX.put("HB", "Hemoglobin");
    }
}
