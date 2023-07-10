/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.util;

import com.argusoft.medplat.mobile.dto.ComponentTagDto;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * An util class for system constants
 * @author harsh
 * @since 31/08/2020 4:30
 */
public class SystemConstantUtil {

    private SystemConstantUtil() {
            
    }

    public static final String CRONE_FLAG = "CRONE_FLAG";
    public static final String HASH = "#";
    public static final String USER_ID_IMPLICIT_PARAMETER = "#loggedInUserId#";
    public static final String USER_CREATED = "userCreated";
    public static final String FHW_LMPFU = "FHW_LMPFU";
    public static final String FHW_ANC = "FHW_ANC";
    public static final String FHW_WPD = "FHW_WPD";
    public static final String FHW_PNC = "FHW_PNC";
    public static final String FHW_CHILD_SERVICE = "FHW_CS";
    public static final String FHW_VAE = "FHW_VAE";
    public static final String FHW_RIM = "FHW_RIM";
    public static final String FSAM_TO_CMAM = "FSAM_TO_CMAM";
    public static final String COVID_LAB_TEST_CASE_HISTORY = "COVID_LAB_TEST_CASE_HISTORY";
    //Old Migration
    public static final String MIGRATION_IN_REQUEST = "MIGRATION_IN_REQ";
    public static final String MIGRATION_OUT_REQUEST = "MIGRATION_OUT_REQ";
    public static final String MIGRATION_IN_RESPONSE = "MIGRATION_IN_RESP";
    public static final String MIGRATION_OUT_RESPONSE = "MIGRATION_OUT_RESP";
    //New Migration
    public static final String MIGRATION_IN = "MIG_IN";
    public static final String MIGRATION_OUT = "MIG_OUT";
    public static final String MIGRATION_IN_CONFIRMATION = "MIG_IN_CONF";
    public static final String MIGRATION_OUT_CONFIRMATION = "MIG_OUT_CONF";
    public static final String MIGRATION_REVERTED = "MIG_REVERT";
    public static final String FAMILY_MIGRATION_REVERTED = "FAM_MIG_REVERT";
    // Family Migration
    public static final String FAMILY_MIGRATION_OUT = "FAMILY_MIGRATION_OUT";
    public static final String FAMILY_MIGRATION_IN_CONFIRMATION = "FAMILY_MIGRATION_IN_CONFIRMATION";
    //
    public static final String ARGUS_ADMIN_ROLE = "argusadmin";
    public static final String[] ALLOWED_USERS_TO_DOWNLOAD_DB = {"hshah", "slamba", "kkpatel", "harsh"};
    public static final String[] SMS_TO_BE_SENT_ON_MOBILE_NO_FOR_DB_DOWNLOAD_REQUEST = {"9429282218", "9727227889", "8866885883", "9974666602"};

    public static final String FHS_NEW_MEMBER = "FHS_NEW_CHILD";

    //TeCHO FHW SHEETS
    public static final String FAMILY_HEALTH_SURVEY = "FHS";
    public static final String CFHC = "CFHC";
    public static final String FHS_MEMBER_UPDATE = "MEMBER_UPDATE";
    public static final String FHW_PREG_CONF = "FHW_PREG_CONF";
    public static final String FHW_DEATH_CONF = "FHW_DEATH_CONF";
    public static final String LMP_FOLLOW_UP = "LMPFU";
    public static final String TECHO_FHW_ANC = "FHW_ANC";
    public static final String TECHO_FHW_WPD = "FHW_WPD";
    public static final String TECHO_FHW_PNC = "FHW_PNC";
    public static final String TECHO_FHW_CI = "FHW_CI";
    public static final String TECHO_FHW_CS = "FHW_CS";
    public static final String TECHO_FHW_VAE = "FHW_VAE";
    public static final String TECHO_FHW_RIM = "FHW_RIM";
    public static final String TECHO_WPD_DISCHARGE = "DISCHARGE";
    public static final String TECHO_CS_APPETITE_TEST = "APPETITE";
    public static final String SAM_SCREENING = "SAM_SCREENING";
    public static final String FHW_SAM_SCREENING_REF = "FHW_SAM_SCREENING_REF";
    public static final String TRAVELLERS_SCREENING = "TRAVELLERS_SCREENING";
    public static final String GERIATRICS_MEDICATION_ALERT = "GMA";
    public static final String IDSP_MEMBER = "IDSP_MEMBER";
    public static final String IDSP_FAMILY = "IDSP_FAMILY";
    public static final String IDSP_NEW_FAMILY = "IDSP_NEW_FAMILY";
    public static final String IDSP_MEMBER_2 = "IDSP_MEMBER_2";
    public static final String IDSP_FAMILY_2 = "IDSP_FAMILY_2";
    //NCD FHW SHEETS
    public static final String NCD_FHW_HYPERTENSION = "NCD_FHW_HYPERTENSION";
    public static final String NCD_FHW_DIABETES = "NCD_FHW_DIABETES";
    public static final String NCD_FHW_ORAL = "NCD_FHW_ORAL";
    public static final String NCD_FHW_BREAST = "NCD_FHW_BREAST";
    public static final String NCD_FHW_CERVICAL = "NCD_FHW_CERVICAL";
    //TECHO Other Notifications Sheets
    public static final String TT2_ALERT = "TT2_ALERT";
    public static final String IRON_SUCROSE_ALERT = "IRON_SUCROSE_ALERT";
    //TECHO ASHA SHEETS
    public static final String ASHA_LMPFU = "ASHA_LMPFU";
    public static final String ASHA_ANC = "ASHA_ANC";
    public static final String ASHA_WPD = "ASHA_WPD";
    public static final String ASHA_PNC = "ASHA_PNC";
    public static final String ASHA_CS = "ASHA_CS";
    public static final String ASHA_SAM_SCREENING = "ASHA_SAM_SCREENING";
    public static final String CMAM_FOLLOWUP = "CMAM_FOLLOWUP";
    public static final String ASHA_TRAVELLERS_SCREENING = "TRAVELLERS_SCREENING";
    // ASHA Morbidity Sheets
    public static final String ASHA_ANC_MORBIDITY = "ANCMORB";
    public static final String ASHA_PNC_MORBIDITY = "PNCMORB";
    public static final String ASHA_CS_MORBIDITY = "CCMORB";
    //NCD ASHA SHEETS
    public static final String NCD_ASHA_CBAC = "NCD_ASHA_CBAC";
    //NPCB ASHA SHEET
    public static final String ASHA_NPCB = "ASHA_NPCB";
    //AWW Sheets
    public static final String AWW_CS = "AWW_CS";
    public static final String AWW_THR = "AWW_THR";
    public static final String AWW_DAILY_NUTRITION = "AWW_DAILY_NUTRITION";
    // States
    public static final String STATE_ACTIVE = "ACTIVE";
    public static final String STATE_INACTIVE = "INACTIVE";
    //Sheets for mobile form
    public static final List<String> FHW_SHEETS = new LinkedList<>();
    public static final List<String> ASHA_SHEETS = new LinkedList<>();
    public static final List<String> AWW_SHEETS = new LinkedList<>();

    //Sheet Version System Key in System Configuration
    public static final String FHW_SHEET_VERSION = "FHW SHEET VERSION";
    public static final String ASHA_SHEET_VERSION = "ASHA SHEET VERSION";
    public static final String AWW_SHEET_VERSION = "AWW SHEET VERSION";

    public static final String MOBILE_FORM_VERSION = "MOBILE_FORM_VERSION";

    public static final Map<String, String> IMPLICIT_PARAMETERS_MAP = new HashMap<>();

    public static final Map<String, String> MANUAL_EVENTS_MAP = new HashMap<>();

    public static final String DATABASE_PATH_DEFAULT = "/home/database-backup";

    static {
        //fhwSheets
        //FHW_SHEETS.add(FAMILY_HEALTH_SURVEY);
        FHW_SHEETS.add(CFHC);
        //FHW_SHEETS.add(FHW_PREG_CONF);
        //FHW_SHEETS.add(FHW_DEATH_CONF);
        FHW_SHEETS.add(LMP_FOLLOW_UP);
        FHW_SHEETS.add(TECHO_FHW_ANC);
        FHW_SHEETS.add(TECHO_FHW_WPD);
        FHW_SHEETS.add(TECHO_FHW_PNC);
        FHW_SHEETS.add(TECHO_FHW_CI);
        FHW_SHEETS.add(TECHO_FHW_CS);
        FHW_SHEETS.add(TECHO_FHW_VAE);
        FHW_SHEETS.add(TECHO_FHW_RIM);
        FHW_SHEETS.add(TECHO_WPD_DISCHARGE);
        //FHW_SHEETS.add(FHS_MEMBER_UPDATE);
//        FHW_SHEETS.add(TECHO_CS_APPETITE_TEST);
//        FHW_SHEETS.add(NCD_ASHA_CBAC);
//        FHW_SHEETS.add(NCD_FHW_HYPERTENSION);
//        FHW_SHEETS.add(NCD_FHW_DIABETES);
//        FHW_SHEETS.add(NCD_FHW_ORAL);
//        FHW_SHEETS.add(NCD_FHW_BREAST);
//        FHW_SHEETS.add(NCD_FHW_CERVICAL);
        FHW_SHEETS.add(TT2_ALERT);
        FHW_SHEETS.add(IRON_SUCROSE_ALERT);
//        FHW_SHEETS.add(SAM_SCREENING);
//        FHW_SHEETS.add(FHW_SAM_SCREENING_REF);
//        FHW_SHEETS.add(CMAM_FOLLOWUP);
//        FHW_SHEETS.add(TRAVELLERS_SCREENING);
//        FHW_SHEETS.add(GERIATRICS_MEDICATION_ALERT);
//        FHW_SHEETS.add(IDSP_MEMBER);
//        FHW_SHEETS.add(IDSP_NEW_FAMILY);
//        FHW_SHEETS.add(IDSP_MEMBER_2);

        //ashaSheets
        //ASHA_SHEETS.add(NCD_ASHA_CBAC);
        ASHA_SHEETS.add(ASHA_LMPFU);
        ASHA_SHEETS.add(ASHA_PNC);
        ASHA_SHEETS.add(ASHA_CS);
        //ASHA_SHEETS.add(ASHA_NPCB);
        //ASHA_SHEETS.add(FAMILY_HEALTH_SURVEY);
        ASHA_SHEETS.add(ASHA_ANC);
        ASHA_SHEETS.add(ASHA_WPD);
        ASHA_SHEETS.add(ASHA_ANC_MORBIDITY);
        ASHA_SHEETS.add(ASHA_PNC_MORBIDITY);
        ASHA_SHEETS.add(ASHA_CS_MORBIDITY);
//        ASHA_SHEETS.add(ASHA_SAM_SCREENING);
//        ASHA_SHEETS.add(CMAM_FOLLOWUP);
//        ASHA_SHEETS.add(ASHA_TRAVELLERS_SCREENING);
//        ASHA_SHEETS.add(IDSP_MEMBER);
//        ASHA_SHEETS.add(IDSP_NEW_FAMILY);
//        ASHA_SHEETS.add(IDSP_MEMBER_2);

        //awwSheets
        AWW_SHEETS.add(AWW_CS);
        AWW_SHEETS.add(AWW_THR);
        AWW_SHEETS.add(AWW_DAILY_NUTRITION);

        //implicitParametersMap
        IMPLICIT_PARAMETERS_MAP.put(USER_ID_IMPLICIT_PARAMETER, USER_ID_IMPLICIT_PARAMETER);

        //manualEventsMap
        MANUAL_EVENTS_MAP.put(USER_CREATED, "User Created");
    }

    public static  Map<String, List<ComponentTagDto>> retrievedXlsData;
}
