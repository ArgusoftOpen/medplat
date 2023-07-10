/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.constants;

import com.argusoft.medplat.fhs.util.CroneService;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author prateek
 */
public class MobileConstantUtil {

    private MobileConstantUtil() {
        throw new IllegalStateException("Utility Class");
    }

    public static final String ANDROID = "ANDROID";
    public static final String J2ME = "J2ME";
    public static final String WEB = "WEB";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";
    public static final String CHILD_BENEFICIARY = "C";
    public static final String NEW_BORN_CHILD_BENEFICIARY = "N";
    public static final String MOTHER_BENEFICIARY = "M";
    public static final String FAMILY_FOLDER_MEMBER_UPDATE = "FAMILY_FOLDER_MEMBER_UPDATE";
    public static final String FAMILY_HEALTH_SURVEY = "FHS";
    public static final String FAMILY_FOLDER = "FAMILY_FOLDER";
    public static final String CFHC = "CFHC";
    public static final String LMP_FOLLOW_UP_VISIT = "LMPFU";
    public static final String ANC_VISIT = "FHW_ANC";
    public static final String WPD_VISIT = "FHW_WPD";
    public static final String PNC_VISIT = "FHW_PNC";
    public static final String CHILD_SERVICES_VISIT = "FHW_CS";
    public static final String REPRODUCTIVE_INFO_MODIFICATION_VISIT = "FHW_RIM";
    public static final String VACCINE_ADVERSE_EFFECT_VISIT = "FHW_VAE";
    public static final String FHS_MEMBER_UPDATE = "MEMBER_UPDATE";
    public static final String WPD_DISCHARGE_VISIT = "DISCHARGE";
    public static final String APPETITE_TEST_ALERT = "APPETITE";
    public static final String NCD_ASHA_CBAC = "NCD_ASHA_CBAC";
    public static final String NCD_FHW_HYPERTENSION = "NCD_FHW_HYPERTENSION";
    public static final String NCD_FHW_DIABETES = "NCD_FHW_DIABETES";
    public static final String NCD_FHW_ORAL = "NCD_FHW_ORAL";
    public static final String NCD_FHW_BREAST = "NCD_FHW_BREAST";
    public static final String NCD_FHW_CERVICAL = "NCD_FHW_CERVICAL";
    public static final String NCD_FHW_MENTAL_HEALTH = "NCD_FHW_MENTAL_HEALTH";
    public static final String NCD_FHW_HEALTH_SCREENING = "NCD_FHW_HEALTH_SCREENING";
    public static final String NCD_PERSONAL_HISTORY = "NCD_PERSONAL_HISTORY";

    public static final String NCD_FHW_DIABETES_CONFIRMATION = "NCD_FHW_DIABETES_CONFIRMATION";
    public static final String NCD_FHW_WEEKLY_CLINIC = "NCD_FHW_WEEKLY_CLINIC";
    public static final String NCD_FHW_WEEKLY_HOME = "NCD_FHW_WEEKLY_HOME";
    public static final String NOTIFICATION_NCD_CLINIC_VISIT = "NCD_CLINIC_VISIT";
    public static final String NOTIFICATION_NCD_HOME_VISIT = "NCD_HOME_VISIT";

    public static final String OFFLINE_ABHA_NUMBER_CREATIONS = "OFFLINE_ABHA_NUMBER_CREATIONS";

    public static final String ASHA_PNC_VISIT = "ASHA_PNC";
    public static final String ASHA_CS_VISIT = "ASHA_CS";
    public static final String ASHA_ANC_VISIT = "ASHA_ANC";
    public static final String ASHA_WPD_VISIT = "ASHA_WPD";
    public static final String ASHA_LMPFU_VISIT = "ASHA_LMPFU";
    public static final String ASHA_NPCB_VISIT = "ASHA_NPCB";

    //Static Forms
    public static final String AADHAR_UPDATION = "AADHAR_UPDATION";
    public static final String FHSR_PHONE_UPDATE = "FHSR_PHONE_UPDATE";
    public static final String TT2_ALERT = "TT2_ALERT";
    public static final String IRON_SUCROSE_ALERT = "IRON_SUCROSE_ALERT";
    public static final String SAM_SCREENING = "SAM_SCREENING";
    public static final String MOBILE_NUMBER_VERIFICATION = "MOBILE_NUMBER_VERIFICATION";

    //Migration Form Codes
    public static final String MIGRATION_IN = "MIGRATION_IN";
    public static final String MIGRATION_OUT = "MIGRATION_OUT";
    public static final String MIGRATION_IN_CONFIRMATION = "MIGRATION_IN_CONFIRMATION";
    public static final String MIGRATION_OUT_CONFIRMATION = "MIGRATION_OUT_CONFIRMATION";
    public static final String MIGRATION_REVERTED = "MIGRATION_REVERTED";
    public static final String FAMILY_MIGRATION_REVERTED = "FAMILY_MIGRATION_REVERTED";
    public static final String FAMILY_MIGRATION_OUT_FORM = "FAM_MIG_OUT";
    public static final String FAMILY_MIGRATION_IN_FORM = "FAM_MIG_IN";
    public static final String FAMILY_MIGRATION_OUT_CONFIRMATION_FORM = "FAM_MIG_OUT_CONF";
    public static final String FAMILY_MIGRATION_IN_CONFIRMATION_FORM = "FAM_MIG_IN_CONF";

    //Migration Notification Types
    public static final String MEMBER_MIGRATION_OUT_CONFIRMATION = "MO";
    public static final String MEMBER_MIGRATION_IN_CONFIRMATION = "MI";
    public static final String FAMILY_MIGRATION_OUT_CONFIRMATION = "FMO";
    public static final String FAMILY_MIGRATION_IN_CONFIRMATION = "FMI";

    //Read Only notification types
    public static final String FHW_READ_ONLY = "READ_ONLY";
    public static final String ASHA_READ_ONLY = "ASHA_READ_ONLY";

    //FHW Confirmation Notification of ASHA Reported Events
    public static final String NOTIFICATION_FHW_DELIVERY_CONF = "FHW_DELIVERY_CONF";
    public static final String NOTIFICATION_FHW_PREGNANCY_CONF = "FHW_PREG_CONF";
    public static final String NOTIFICATION_FHW_DEATH_CONF = "FHW_DEATH_CONF";
    public static final String NOTIFICATION_FHW_MEMBER_MIGRATION = "FHW_MEMBER_MIGRATION";
    public static final String NOTIFICATION_FHW_FAMILY_MIGRATION = "FHW_FAMILY_MIGRATION";
    public static final String NOTIFICATION_FHW_FAMILY_SPLIT = "FHW_FAMILY_SPLIT";

    public static final String NOTIFICATION_ASHA_READ_ONLY = "ASHA_READ_ONLY";
    // Asha Event Form
    public static final String ASHA_REPORTED_EVENT = "ASHA_REPORTED_EVENT";
    public static final String FHW_REPORTED_EVENT_REJECTION = "FHW_EVENT_REJECTION";
    //ASHA Reported Events
    public static final String ASHA_REPORT_FAMILY_MIGRATION = "REPORT_FAMILY_MIGRATION";
    public static final String ASHA_REPORT_FAMILY_SPLIT = "REPORT_FAMILY_SPLIT";
    public static final String ASHA_REPORT_MEMBER_MIGRATION = "REPORT_MIGRATION";
    public static final String ASHA_REPORT_MEMBER_DEATH = "REPORT_DEATH";
    public static final String ASHA_REPORT_MEMBER_DELIVERY = "REPORT_DELIVERY";

    //AWW 
    public static final String AWW_CHILD_SERVICE = "AWW_CS";
    public static final String AWW_TAKE_HOME_RATION = "AWW_THR";
    public static final String AWW_DAILY_NUTRITION = "AWW_DAILY_NUTRITION";

    public static final String GERIATRICS_MEDICATION_ALERT = "GMA";
    public static final String IDSP_MEMBER = "IDSP_MEMBER";

    //LMS Form
    public static final String LMS_TEST = "LMS_TEST";

    public static final String ABHA_NUMBER = "ABHA_NUMBER";

    public static final String SCREENED = "SCREENED";
    public static final String HOME_VISIT = "HOME";
    public static final String CLINIC_VISIT = "CLINIC";

    public static final String CHARDHAM_MEMBER_SCREENING = "CHARDHAM_MEMBER_SCREENING";
    public static final String CHARDHAM_EMERGENCY_NEW_REQUEST = "CHARDHAM_EMERGENCY_NEW_REQUEST";
    public static final String CHARDHAM_EMERGENCY_NEW_RESPONSE = "CHARDHAM_EMERGENCY_NEW_RESPONSE";
    public static final String CHARDHAM_EMERGENCY_REQUEST_COMPLETED = "CHARDHAM_EMERGENCY_REQUEST_COMPLETED";


    public static class Roles {
        private Roles() {

        }

        public static final String ASHA = "Asha";
        public static final String AWW = "AWW";
        public static final String FHW = "FHW";
        public static final String CHO_HWC = "CHO-HWC";
        public static final String MPHW = "MPHW";
        public static final String CC = "Care coordinator";
        public static final String KIOSK = "KIOSK";
        public static final String MRP = "MRP";
        public static final String FEMALE_HEALTH_SUPERVISOR = "Female Health Supervisor";
        public static final String RBSK_MO = "RBSK MO";
        public static final String USER_ROLE_SUPERVISOR_STAFF_NURSE_CORP = "Supervisor Staff Nurse Corporation";
        public static final String PROGRAM_MANAGER = "Program Manager";
        public static final String LOGISTICS_MANAGER = "Logistics Manager";
        public static final String ERT = "ERT";
        public static final String SETU = "Setu";
        public static final String SUPER_ADMIN = "Super Admin";
        public static final String ASHA_FACILITATOR = "Asha Facilitator";
        public static final String CLERK_ACCOUNTANT = "Clerk";
        public static final String MEDICAL_OFFICER = "Medical Officer";
        public static final String DATA_COLLECTOR = "Data Collector";
        public static final String PNC_DATA_COLLECTOR = "PNC Data Collector";
        public static final String CHILD_DATA_COLLECTOR = "Child Data Collector";
    }

    //Constant used in mobile methods and webservices
    public static final String CHECKSUM_AND_ENTITY_TYPE_SEPARATER = "|";
    public static final String ANSWER_STRING_FIRST_SEPARATER = "~";
    public static final String ANSWER_STRING_SECOND_SEPARATER = "!";

    //Morbidity Separator
    public static final String MORBIDITY_DASH_SEPERATOR = "%";
    public static final String MORBIDITY_TILD_SEPERATOR = "~";
    public static final String MORBIDITY_EXCLM_SEPERATOR = "!";
    public static final String MORBIDITY_HASH_SEPERATOR = "#";
    public static final String MORBIDITY_AT_SEPERATOR = "@";

    //SYSTEM CONFIGURATION CONSTANT FOR FAMILY HEALTH SURVEY
    public static final String LAST_MEMBER_HEALTH_ID = "LAST_MEMBER_HEALTH_ID";
    public static final String LAST_FAMILY_ID = "LAST_FAMILY_ID";

    //User Login Constants
    public static final String INVALID_USERNAME = "Invalid username";
    public static final String INVALID_USERNAME_OR_PASSWORD_STR = "Incorrect Username or Password";
    public static final String ACCOUNT_LOCKED = "Your account is locked for 15 minutes. Please try again later";
    public static final String ACCOUNT_DISABLED = "Your account is disabled by Administrator.";
    public static final String LOCATION_NOT_ASSIGNED = "Location is not assigned correctly to the user, please contact your co-ordinator.";
    public static final String MENU_NOT_FOUND = "User is not authorised to login.";

    //Use in move to production feature
    public static final String RESULT = "result";
    public static final String FORM_CODE = "form_code";

    //Immunisation Types For Mother
    public static final String IMMUNISATION_TT = "TT";
    public static final String IMMUNISATION_TT_1 = "TT1";
    public static final String IMMUNISATION_TT_2 = "TT2";
    public static final String IMMUNISATION_TT_BOOSTER = "TT_BOOSTER";
    public static final String IMMUNISATION_CORTICO_ASTEROID = "CORTICO_ASTEROID";
    //Immunisation Types For Children
    public static final String IMMUNISATION_HEPATITIS_B_0 = "HEPATITIS_B_0";
    public static final String IMMUNISATION_VITAMIN_K = "VITAMIN_K";
    public static final String IMMUNISATION_BCG = "BCG";
    public static final String IMMUNISATION_OPV_0 = "OPV_0";
    public static final String IMMUNISATION_OPV_1 = "OPV_1";
    public static final String IMMUNISATION_OPV_2 = "OPV_2";
    public static final String IMMUNISATION_OPV_3 = "OPV_3";
    public static final String IMMUNISATION_OPV_BOOSTER = "OPV_BOOSTER";
    public static final String IMMUNISATION_PENTA_1 = "PENTA_1";
    public static final String IMMUNISATION_PENTA_2 = "PENTA_2";
    public static final String IMMUNISATION_PENTA_3 = "PENTA_3";
    public static final String IMMUNISATION_DPT_1 = "DPT_1";
    public static final String IMMUNISATION_DPT_2 = "DPT_2";
    public static final String IMMUNISATION_DPT_3 = "DPT_3";
    public static final String IMMUNISATION_DPT_BOOSTER = "DPT_BOOSTER";
    public static final String IMMUNISATION_ROTA_VIRUS_1 = "ROTA_VIRUS_1";
    public static final String IMMUNISATION_ROTA_VIRUS_2 = "ROTA_VIRUS_2";
    public static final String IMMUNISATION_ROTA_VIRUS_3 = "ROTA_VIRUS_3";
    public static final String IMMUNISATION_MEASLES_1 = "MEASLES_1";
    public static final String IMMUNISATION_MEASLES_2 = "MEASLES_2";
    public static final String IMMUNISATION_MEASLES_RUBELLA_1 = "MEASLES_RUBELLA_1";
    public static final String IMMUNISATION_MEASLES_RUBELLA_2 = "MEASLES_RUBELLA_2";
    public static final String IMMUNISATION_F_IPV_1_01 = "F_IPV_1_01";
    public static final String IMMUNISATION_F_IPV_2_01 = "F_IPV_2_01";
    public static final String IMMUNISATION_F_IPV_2_05 = "F_IPV_2_05";
    public static final String IMMUNISATION_VITAMIN_A = "VITAMIN_A";
    //Immunisation Separators for Member Entity
    public static final String IMMUNISATION_DATE_SEPARATOR = "#";
    public static final String IMMUNISATION_NAME_SEPARATOR = ",";

    //Sync Status's status code
    public static final String SUCCESS_VALUE = "S";
    public static final String ERROR_VALUE = "E";
    public static final String PENDING_VALUE = "P";
    public static final String PROCESSING_VALUE = "PR";
    public static final String HANDLED_ERROR_VALUE = "HE";
    public static final String REL_LOCAL_PREFIX = "l";
    public static final String VERIFICATION_PENDING = "VP";
    public static final String VERIFICATION_PENDING_FOR_STILL_BIRTH = "SBVP";
    public static final String SUBMIT_PENDING = "SP";

    public static final JsonSerializer<Date> jsonDateSerializer = (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.getTime());

    public static final JsonDeserializer<Date> jsonDateDeserializer = (json, typeOfT, context) -> json == null ? null : new Date(json.getAsLong());

    public static final JsonDeserializer<Date> jsonDateDeserializerStringFormat = (json, typeOfT, context) -> {
        SimpleDateFormat fmt = new SimpleDateFormat("MMM d, yyyy HH:mm:ss");
        try {
            return json == null || json.getAsString().isEmpty() ? null : fmt.parse(json.getAsString());
        } catch (ParseException e) {
            Logger.getLogger(CroneService.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return new Date(json.getAsLong());
    };

    public static final JsonDeserializer<Date> jsonDateDeserializerToStringFormat = (json, typeOfT, context) -> {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            return json == null || json.getAsString().isEmpty() ? null : fmt.parse(json.getAsString());
        } catch (ParseException e) {
            Logger.getLogger(CroneService.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return new Date(json.getAsLong());
    };

    //This is only for IDSP. Do not use anywhere else
    public static final JsonDeserializer<Date> jsonDateDeserializerIDSP = (json, typeOfT, context) ->
            json == null || json.getAsString().isEmpty() ? null : new Date();

}
