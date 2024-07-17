package com.argusoft.sewa.android.app.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author satyam
 */
public class GlobalTypes {

    private GlobalTypes() {
        throw new IllegalStateException("Utility class");
    }

    //Sync Status's status code
    public static final String STATUS_PENDING = "P";
    public static final String STATUS_SUCCESS = "S";
    public static final String STATUS_ERROR = "E";
    public static final String FLAVOUR_CHIP = "chip";

    public static final String STATUS_HANDLED_ERROR = "HE";

    //All the role names which are used at login time
    public static final String USER_ROLE_FHW = "FHW";
    public static final String USER_ROLE_AWW = "AWW";
    public static final String USER_ROLE_MPHW = "MPHW";
    public static final String USER_ROLE_KIOSK = "Kiosk User";
    public static final String USER_ROLE_MRP = "MRP User";
    public static final String USER_ROLE_FMR = "Travelling Paramedics";
    public static final String USER_ROLE_SDRF = "SDRF";
    public static final String USER_ROLE_MULE = "Mule User";
    public static final String USER_ROLE_FIXED_SDRF = "Fixed SDRF";
    public static final String USER_ROLE_FIXED_FMR = "Fixed Travelling Paramedics";
    public static final String USER_ROLE_FIXED_MULE = "Fixed Mule";
    public static final String VALUE_MRP = "MRP";
    public static final String VALUE_KIOSK = "KIOSK";
    public static final String VALUE_FMR = "FMR";
    public static final String VALUE_SDRF = "SDRF";
    public static final String VALUE_MULE = "MULE";
    public static final String USER_ROLE_ASHA = "Asha";
    public static final String USER_ROLE_CHO_HWC = "CHO-HWC";
    public static final String USER_ROLE_FHSR = "Female Health Supervisor";
    public static final String USER_ROLE_SUPERVISOR_STAFF_NURSE_CORP = "Supervisor Staff Nurse Corporation";
    public static final String USER_ROLE_RBSK_MO = "RBSK MO";
    public static final String USER_ROLE_LAB_TECHNICIAN = "LAB Technician";
    public static final String USER_ROLE_DATA_COLLECTOR = "Data collector";

    public static final String USER_ROLE_CODE_ASHA = "A";
    public static final String USER_ROLE_CODE_FHW = "F";
    public static final String USER_ROLE_CODE_KIOSK = "K";

    //All role names used for announcement retrieval
    public static final String ANNOUNCE_FOR_FHW = "F";
    public static final String ANNOUNCE_FOR_ASHA = "H";

    // Component type
    public static final String CUSTOM_TIME_PICKER = "CTP";
    public static final String COMBO = "CB";
    public static final String RADIOBUTTON = "RB";
    public static final String CUSTOM_DATE_BOX = "CDB";
    public static final String AGE_BOX = "AB";
    public static final String AGE_BOX_DISPLAY = "AD";
    public static final String TEXT_BOX_WITH_AUDIO = "TBWA";
    public static final String TEXT_AREA = "TA";
    public static final String CHARDHAM_TEXT_AREA = "CHTA";
    public static final String TEXT_BOX = "TB";
    public static final String CHARDHAM_TEXT_BOX = "CHTB";
    public static final String TEXT_BOX_CHANGE_LISTENER = "TBCL";
    public static final String PHOTO_PICKER = "PP";
    public static final String SHOW_VIDEO = "SV";
    public static final String SHOW_VIDEO_MANDATORY = "SVM";
    public static final String COMBO_BOX_DYNAMIC_SELECT = "CBDS";
    public static final String WEIGHT_BOX = "WB";
    public static final String WEIGHT_BOX_DISPLAY = "WD";
    public static final String TEMPERATURE_BOX = "TEMB";
    public static final String LABEL = "L";
    public static final String LABEL_FORMULA = "LF";
    public static final String MULTI_SELECT = "MS";
    public static final String SEARCHABLE_MULTI_SELECT = "SRMS";
    public static final String CHARDHAM_CHIP_GROUP = "CCG";
    public static final String DISTRICT_HEALTH_INFRASTRUCTURE_COMPONENT = "DHIC";
    public static final String SINGLE_CHECK_BOX = "SCB";
    public static final String BLOOD_PRESSURE_MEASUREMENT = "BPM";
    public static final String CHARDHAM_BLOOD_PRESSURE_MEASUREMENT = "CD_BPM";
    public static final String PLAY_AUDIO = "PA";
    public static final String CALL = "CALL";
    public static final String MEMBERS_LIST_COMPONENT = "ML"; //Only for FHS, not used anywhere else currently
    public static final String MEMBER_FULL_NAME_COMPONENT = "MFN"; //Only for FHS, not used anywhere else currently
    public static final String MOTHER_CHILD_RELATIONSHIP = "MCR";
    public static final String HUSBAND_WIFE_RELATIONSHIP = "HWR";
    public static final String ORAL_SCREENING_COMPONENT = "OS";
    public static final String BREAST_SCREENING_COMPONENT = "BS";
    public static final String CERVICAL_SCREENING_COMPONENT = "CS";
    public static final String BMI_COMPONENT = "BMI";
    public static final String HEALTH_INFRASTRUCTURE_COMPONENT = "HIC";
    public static final String CHARDHAM_HEALTH_INFRASTRUCTURE_COMPONENT = "CHARDHAM_HIC";
    public static final String CHILD_GROWTH_CHART_COMPONENT = "CGCC";
    public static final String IMMUNISATION_GIVEN_COMPONENT = "IGC";
    public static final String SCHOOL_COMPONENT = "SC";
    public static final String OTP_BASED_VERIFICATION_COMPONENT = "OBVC";
    public static final String NDHM_DATA_PUSH_CONF_COMPONENT = "NDPC";
    public static final String FORM_SUBMISSION_COMPONENT = "FSC";
    public static final String NUMBER_TEXT_BOX_COMPONENT = "NUB";
    public static final String OPENRDTREADER_COMPONENT = "ORDT";
    public static final String HEALTH_ID_MANAGEMENT_COMPONENT = "HMC";
    public static final String HEALTH_ADVISORY_CHARDHAM_COMPONENT = "HACC";
    public static final String LIST_IN_COLOR = "LIC";
    public static final String VACCINATIONS_TYPE = "RBD";
    public static final String MEMBER_DETAILS_COMPONENT = "MDC";
    public static final String SIMPLE_RADIO_DATE = "SRBD";
    public static final String CHECKBOX_TEXT_BOX = "CTB";
    public static final String QR_SCAN = "QRS";
    public static final String SET_DRUG_COMPONENT = "SDC";

    //extra constant
    public static final String NOT_AVAILABLE = "Not available";
    public static final String NO_ISSUES_FOUND = "No issues found";
    public static final String MALNUTRITION_GRADE_NOT_AVAILABLE = "Malnutrition grade could not be calculated, because weight is not available.";
    public static final String RELATED_PROPERTY_FOR_MALNUTRITION_GRADE = "malnutritionGrade";
    public static final String MOB_FEATURE_ABDM_HEALTH_ID = "ABDM_HEALTH_ID";

    //Separator
    public static final String PATH_SEPARATOR = "/";
    public static final String DATE_STRING_SEPARATOR = "/";
    public static final String DOT_SEPARATOR = ".";
    public static final String BEAN_SEPARATOR = "|";
    public static final String MULTI_VALUE_BEAN_SEPARATOR = "~";
    public static final String ANSWER_STRING_FIRST_SEPARATOR = "!";
    public static final String KEY_VALUE_SEPARATOR = "-";
    public static final String MORBIDITY_DETAILS_SEPARATOR = "@";
    public static final String BENEFICIARY_SEPARATOR = "%";
    public static final String MORBIDITY_SEPARATOR_HASH = "#";
    public static final String COMMA = ",";
    public static final String STRING_LIST_SEPARATOR = ":";
    public static final String ADD_SEPARATOR = "+";
    public static final String MULTI_SELECT_SEPARATOR = "*";

    //list
    public static final String ASHA_VILLAGES = "ashaVillages";

    //All month names used to display dates
    public static final String MONTH_JANUARY = "January";
    public static final String MONTH_FEBRUARY = "February";
    public static final String MONTH_MARCH = "March";
    public static final String MONTH_APRIL = "April";
    public static final String MONTH_MAY = "May";
    public static final String MONTH_JUNE = "June";
    public static final String MONTH_JULY = "July";
    public static final String MONTH_AUGUST = "August";
    public static final String MONTH_SEPTEMBER = "September";
    public static final String MONTH_OCTOBER = "October";
    public static final String MONTH_NOVEMBER = "November";
    public static final String MONTH_DECEMBER = "December";

    //Event
    public static final String EVENT_NEXT = "Next";
    public static final String EVENT_BACK = "Back";
    public static final String EVENT_CANCEL = "Cancel";
    public static final String EVENT_MERGE = "Merge";
    public static final String EVENT_LOOP = "Loop";
    public static final String EVENT_DEFAULT_LOOP = "DefaultLoop";
    public static final String EVENT_SAVE_FORM = "Save Form";
    public static final String EVENT_SUBMIT = "Submit";
    public static final String EVENT_PLAY = "Play";
    public static final String EVENT_PAUSE = "Pause";
    public static final String EVENT_OKAY = "Okay";
    public static final String EVENT_DONE = "Done";
    public static final String EVENT_COMPLETED = "Mark as completed";
    public static final String EVENT_SEND_REQUEST = "Send Request";

    //Gender Constants
    public static final String MALE = "M";
    public static final String FEMALE = "F";
    public static final String TRANSGENDER = "T";
    public static final String OTHER = "O";

    //Malnutrition Grade
    public static final String LOWER_MALNUTRITION_GRADE = "Red";
    public static final String MIDDLE_MALNUTRITION_GRADE = "Yellow";
    public static final String UPPER_MALNUTRITION_GRADE = "Green";

    //constants used in MyClient RMS
    public static final String CLIENT_IS_MOTHER = "M";
    public static final String CLIENT_IS_CHILD = "C";
    public static final String TRUE = "T";
    public static final String FALSE = "F";
    public static final String YES = "Yes";
    public static final String NO = "No";

    //Dosage constant
    public static final String DIARRHOEA_WITH_DEHYDRATION_LT_4_MONTHS = "Diarrhoea_WD_<4_M";
    public static final String DIARRHOEA_WITH_DEHYDRATION_LT_4_TO_12_MONTHS = "Diarrhoea_WD_4-12_M";
    public static final String DIARRHOEA_WITH_DEHYDRATION_GT_12_MONTHS = "Diarrhoea_WD_>12_M";
    public static final String DIARRHOEA_WO_DEHYDRATION_LT_2_MONTHS = "Diarrhoea_WOD_<2_M";
    public static final String DIARRHOEA_WO_DEHYDRATION_GT_2_MONTHS = "Diarrhoea_WOD_>2M";
    public static final String CHLOROQUINE_TABLET_DOSAGE_0_TO_1_YEAR = "Chlorquine_0-1_Y";
    public static final String CHLOROQUINE_TABLET_DOSAGE_GT_1_YEAR = "Chlorquine_0>1sewa_Y";
    public static final String PCM_TABLET_DOSAGE_GT_2_MONTHS = "PCM_>2_M";
    public static final String VITAMIN_A_DOSAGE_6_TO_12_MONTHS = "Vitamin_A_6-12_M";
    public static final String VITAMIN_A_DOSAGE_GT_12_MONTHS = "Vitamin_A_>12_M";
    public static final String IRON_FOLIC_ACID_TABLET_DOSAGE_GT_6_MONTHS = "Iron_Folic_>6_M";

    //Audio/ Image Constant
    public static final String FILE_TYPE_AUDIO_DISTRICT = "AUD_D";
    public static final String FILE_TYPE_AUDIO_BLOCK = "AUD_B";
    public static final String FILE_TYPE_AUDIO_VILLAGE = "AUD_V";

    //Default Application Directory Path To Save ALL Types of Media Content....
    public static final String AUDIO_RECORD_FORMAT = ".amr";
    public static final String IMAGE_CAPTURE_FORMAT = ".jpg";

    public static final String YEAR = "Year";
    public static final String MONTH = "Month";
    public static final String DAY = "Day";

    //Validation Message
    public static final String MSG_VALIDATION_INVALID_DATE = "Invalid Date";
    public static final String MSG_VALIDATION_PIPELINE_MSG = "Address cannot contain 'sewa|'";

    //Constants for Combo
    public static final String SELECT = "-Select-";
    public static final String NO_OPTION_AVAILABLE = "No options available";
    public static final List<String> KILO_LIST = Collections.unmodifiableList(getKiloList());
    public static final List<String> GRAM_LIST = Collections.unmodifiableList(getGramList());
    public static final List<String> TEMPERATURE_DEGREE_VALUES = Collections.unmodifiableList(getTemperatureDegreeValues());
    public static final List<String> TEMPERATURE_FLOATING_VALUES = Collections.unmodifiableList(getTemperatureFloatingValues());

    private static List<String> getKiloList() {
        List<String> stringList = new LinkedList<>();
        stringList.add(GlobalTypes.SELECT);
        stringList.add("0");
        stringList.add("1");
        stringList.add("2");
        stringList.add("3");
        stringList.add("4");
        stringList.add("5");
        stringList.add("6");
        stringList.add("7");
        stringList.add("8");
        stringList.add("9");
        stringList.add("10");
        stringList.add("11");
        stringList.add("12");
        stringList.add("13");
        stringList.add("14");
        stringList.add("15");
        stringList.add("16");
        stringList.add("17");
        stringList.add("18");
        stringList.add("19");
        stringList.add("20");
        stringList.add("21");
        stringList.add("22");
        stringList.add("23");
        stringList.add("24");
        return stringList;
    }

    private static List<String> getGramList() {
        List<String> stringList = new LinkedList<>();
        stringList.add(GlobalTypes.SELECT);
        stringList.add("0 to 99");
        stringList.add("100 to 199");
        stringList.add("200 to 299");
        stringList.add("300 to 399");
        stringList.add("400 to 499");
        stringList.add("500 to 599");
        stringList.add("600 to 699");
        stringList.add("700 to 799");
        stringList.add("800 to 899");
        stringList.add("900 to 999");
        return stringList;
    }

    private static List<String> getTemperatureDegreeValues() {
        List<String> stringList = new LinkedList<>();
        stringList.add(GlobalTypes.SELECT);
        stringList.add("92");
        stringList.add("93");
        stringList.add("94");
        stringList.add("95");
        stringList.add("96");
        stringList.add("97");
        stringList.add("98");
        stringList.add("99");
        stringList.add("100");
        stringList.add("101");
        stringList.add("102");
        stringList.add("103");
        stringList.add("104");
        stringList.add("105");
        stringList.add("106");
        return stringList;
    }

    private static List<String> getTemperatureFloatingValues() {
        List<String> stringList = new LinkedList<>();
        stringList.add(GlobalTypes.SELECT);
        stringList.add("0");
        stringList.add("1");
        stringList.add("2");
        stringList.add("3");
        stringList.add("4");
        stringList.add("5");
        stringList.add("6");
        stringList.add("7");
        stringList.add("8");
        stringList.add("9");
        return stringList;
    }

    public static final int PHOTO_CAPTURE_ACTIVITY = 15555;
    public static final int QR_SCAN_ACTIVITY = 16666;
    public static final int LOCATION_SERVICE_ACTIVITY = 111;

    //EXCEPTION TYPES
    public static final String EXCEPTION_TYPE_UNHANDLED = "UNHANDLED";
    public static final String EXCEPTION_TYPE_DYNAMIC_FORM = "DYNAMIC_FORM";
    public static final String EXCEPTION_TYPE_SQL = "SQL";
    public static final String EXCEPTION_TYPE_SHOW_VIDEO = "SHOW_VIDEO";
    public static final String EXCEPTION_TYPE_CAMERA = "CAMERA";

    //School Type
    public static final String SCHOOL_TYPE_PRIMARY = "isPrimarySchool";
    public static final String SCHOOL_TYPE_HIGHER_SECONDARY = "isHigherSecondarySchool";
    public static final String SCHOOL_TYPE_MADRESA = "isMadresa";
    public static final String SCHOOL_TYPE_GURUKUL = "isGurukul";

    // extra that needed
    public static final int LOOP_FACTOR = 10000;
    public static final int SUBMIT_QUESTION_ID = 9997;
    public static final int SUBMIT_QUESTION_ID_FOR_FP = 99977;
    public static final int LAST_QUESTION_ID = 9999;
    public static final String NO_WEIGHT = "-1111";

    public static final String MAIN_MENU = "Main Menu";

    // Date Formats
    public static final String DATE_DD_MM_YYYY_FORMAT = "dd/MM/yyyy";

    // Messages
    public static final String MSG_CANCEL_FORM = "Are you sure to close the form?";
    public static final String MSG_CANCEL_MOBILE_VERIFICATION = "Are you sure to close Mobile verification?";
    public static final String MSG_CANCEL_MIGRATION = "Are you sure want to close Migration?";
    public static final String MSG_CANCEL_APPLICATION = "Are you sure to exit from application?";
    public static final String MSG_PROCESSING = "Processing....";
    public static final String MSG_LOGIN_SCREEN = "Preparing the system for the first time may take a few minutes. Please wait.";
    public static final String MSG_LOGGING_IN = "Please wait while we are logging you into the app!";
    public static final String MSG_STARTUP_APPLICATION = "Setting up the Application. Please wait.";
    public static final String MSG_ASHA_NOT_ASSIGNED = "ASHA worker not assigned for the areas in this village. Please contact Call Center before starting the Survey.";
    public static final String MOBILE_DATE_NOT_SAME_SERVER = "Mobile and server dates are different, server date is:";
    public static final String MSG_LOAD_DATA_FROM_SERVER = "Data sync in progress";
    public static final String PLEASE_WAIT = "Please wait.... ";
    public static final String MSG_GEO_FENCING_VIOLATION = "You are filling this form from a region out of your area of intervention. Please be present in your AOI to fill the form.";
    public static final String MSG_NO_MENU = "No feature is assigned to you.";

    // added new fields
    public static final String TEXTUAL_LIST_VALUE = "T";
    public static final String MULTIMEDIA_LIST_VALUE = "M";
    public static final String DATA_MAP_MORBIDITY_CONDITION = "morbidityCondition";
    public static final String NO_MEDIA_FOUND = "No Media found";
    public static final String SELECT_DATE_TEXT = "Select date";
    public static final String SELECT_TIME_TEXT = "Select time";

    // Move To Production States
    public static final String MOVE_TO_PRODUCTION = "MOVE_TO_PRODUCTION";
    public static final String MOVE_TO_PRODUCTION_RESPONSE_PENDING = "MOVE_TO_PRODUCTION_RESPONSE_PENDING";

    public static final String PRETERM_DELIVERY = "Preterm Delivery";
    public static final String PRETERM_DELIVERY_N_WEIGHT = "Preterm Delivery - Weight";

    //Version Bean Key Constants
    public static final String VERSION_LAST_UPDATED_LOCATION_MASTER = "lastUpdateDateForLocationMaster";
    public static final String VERSION_LAST_UPDATED_HEALTH_INFRASTRUCTURE = "lastUpdateDateForHealthInfrastructure";
    public static final String VERSION_LAST_UPDATED_PREGNANCY_STATUS = "lastUpdateDateForPregnancyStatus";
    public static final String VERSION_SSL_FLAG = "sslFlag";
    public static final String VERSION_FEATURES_LIST = "featuresList";
    public static final String VERSION_IMEI_BLOCKED = "imeiBlocked";
    public static final String VERSION_LMS_FILE_DOWNLOAD_URL = "lmsFileDownloadUrl";
    public static final String VERSION_SCREENING_STATUS_RED = "SCREENING_STATUS_RED";
    public static final String VERSION_SCREENING_STATUS_YELLOW = "SCREENING_STATUS_YELLOW";
    public static final String VERSION_SCREENING_STATUS_GREEN = "SCREENING_STATUS_GREEN";

    //FEATURES LIST for Version Bean
    public static final String MOB_FEATURE_CEREBRAL_PALSY_SCREENING = "CEREBRAL_PALSY_SCREENING";
    public static final String MOB_FEATURE_GEO_FENCING = "GEO_FENCING";

    //Sheet Versions
    public static final String MOBILE_FORM_VERSION = "MOBILE_FORM_VERSION";
    public static final String ASHA_SHEET_VERSION = "ASHA SHEET VERSION";
    public static final String FHW_SHEET_VERSION = "FHW SHEET VERSION";
    public static final String AWW_SHEET_VERSION = "AWW SHEET VERSION";

    public static final Map<String, String> CEREBRAL_PALSY_QUESTION_IDS_MAP = Collections.unmodifiableMap(getCerebralPalsyQuestionIdsMap());

    private static Map<String, String> getCerebralPalsyQuestionIdsMap() {
        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("705", "cp1");
        stringStringMap.put("707", "cp2");
        stringStringMap.put("709", "cp3");
        stringStringMap.put("711", "cp4");
        stringStringMap.put("713", "cp5");
        stringStringMap.put("715", "cp6");
        stringStringMap.put("717", "cp7");
        stringStringMap.put("719", "cp8");
        stringStringMap.put("721", "cp9");
        stringStringMap.put("723", "cp10");
        stringStringMap.put("725", "cp11");
        stringStringMap.put("727", "cp12");
        stringStringMap.put("729", "cp13");
        stringStringMap.put("731", "cp14");
        stringStringMap.put("733", "cp15");
        stringStringMap.put("735", "cp16");
        stringStringMap.put("737", "cp17");
        stringStringMap.put("739", "cp18");
        stringStringMap.put("741", "cp19");
        stringStringMap.put("743", "cp20");
        stringStringMap.put("745", "cp21");
        stringStringMap.put("747", "cp22");
        stringStringMap.put("749", "cp23");
        stringStringMap.put("751", "cp24");
        stringStringMap.put("753", "cp25");
        return stringStringMap;
    }

    //Supported Files
    public static final List<String> SUPPORTED_EXTENSIONS = Collections.unmodifiableList(getSupportedExtensions());

    private static List<String> getSupportedExtensions() {
        List<String> stringList = new ArrayList<>();
        stringList.add("3gp");
        stringList.add("mp4");
        stringList.add("jpg");
        stringList.add("png");
        stringList.add("mp3");
        stringList.add("pdf");
        return stringList;
    }

    public static final String BLOCKED_DEVICE_IS_BLOCK_DEVICE = "isBlockDevice";
    public static final String BLOCKED_DEVICE_IS_DELETE_DATABASE = "isDeleteDatabase";

    //Intent Extras for Different Activities
    public static final String DATA_MAP = "dataMap";
    public static final String NOTIFICATION = "notification";
    public static final String NOTIFICATION_BEAN = "notificationBean";
    public static final String SELECTED_VILLAGE_IDS = "selectedVillageIds";
    public static final String SELECTED_ASHA_AREAS = "selectedAshaAreas";

    //Flavor constant
    public static final String UTTARAKHAND_FLAVOR = "uttarakhand";

    public static final String LAST_MEMBER_SCREENED = "LAST_MEMBER_SCREENED";
}
