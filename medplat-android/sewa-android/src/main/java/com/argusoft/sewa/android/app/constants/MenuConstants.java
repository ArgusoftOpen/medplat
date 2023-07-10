package com.argusoft.sewa.android.app.constants;

import com.argusoft.sewa.android.app.R;

import java.util.HashMap;
import java.util.Map;

public class MenuConstants {

    private MenuConstants() {
        throw new IllegalStateException("Utility Class");
    }

    //FHW Role Menus
    public static final String FHW_CFHC = "FHW_CFHC";
    public static final String FHW_DATA_QUALITY = "FHW_DATA_QUALITY";
    public static final String FHW_SURVEILLANCE = "FHW_SURVEILLANCE";
    public static final String FHW_ASSIGN_FAMILY = "FHW_ASSIGN_FAMILY";
    public static final String FHW_MY_PEOPLE = "FHW_MY_PEOPLE";
    public static final String FHW_MOBILE_VERIFICATION = "FHW_MOBILE_VERIFICATION";
    public static final String FHW_NOTIFICATION = "FHW_NOTIFICATION";
    public static final String FHW_HIGH_RISK_WOMEN_AND_CHILD = "FHW_HIGH_RISK_WOMEN_AND_CHILD";
    public static final String FHW_NCD_SCREENING = "FHW_NCD_SCREENING";
    public static final String FHW_NCD_REGISTER = "FHW_NCD_REGISTER";
    public static final String FHW_WORK_REGISTER = "FHW_WORK_REGISTER";
    public static final String FHW_WORK_STATUS = "FHW_WORK_STATUS";
    public static final String LEARNING_MANAGEMENT_SYSTEM = "LEARNING_MANAGEMENT_SYSTEM";
    public static final String LMS_PROGRESS_REPORT = "LMS_PROGRESS_REPORT";
    public static final String FHW_NCD_CONFIRMATION = "FHW_NCD_CONFIRMATION";
    public static final String FHW_NCD_WEEKLY_VISIT = "FHW_NCD_WEEKLY_VISIT";

    //ASHA Role Menus
    public static final String ASHA_FHS = "ASHA_FHS";
    public static final String ASHA_MY_PEOPLE = "ASHA_MY_PEOPLE";
    public static final String ASHA_NOTIFICATION = "ASHA_NOTIFICATION";
    public static final String ASHA_HIGH_RISK_BENEFICIARIES = "ASHA_HIGH_RISK_BENEFICIARIES";
    public static final String ASHA_CBAC_ENTRY = "ASHA_CBAC_ENTRY";
    public static final String ASHA_NCD_REGISTER = "ASHA_NCD_REGISTER";
    public static final String ASHA_NPCB_SCREENING = "ASHA_NPCB_SCREENING";
    public static final String ASHA_WORK_REGISTER = "ASHA_WORK_REGISTER";

    //FHSR Role Menus
    public static final String FHSR_PHONE_NUMBER_VERIFICATION = "FHSR_PHONE_NUMBER_VERIFICATION";

    //CHO Role Menus

    //AWW Role Menus
    public static final String AWW_FHS = "AWW_FHS";
    public static final String AWW_MY_PEOPLE = "AWW_MY_PEOPLE";
    public static final String AWW_NOTIFICATION = "AWW_NOTIFICATION";
    public static final String DAILY_NUTRITION = "DAILY_NUTRITION";
    public static final String TAKE_HOME_RATION = "TAKE_HOME_RATION";

    //Lab Technician Role Menus
    public static final String OPD_FACILITY = "OPD_FACILITY";

    //RBSK Role Menus
    public static final String HEAD_TO_TOE_SCREENING = "HEAD_TO_TOE_SCREENING";

    //Common Menus
    public static final String REFRESH = "REFRESH";
    public static final String ANNOUNCEMENTS = "ANNOUNCEMENTS";
    public static final String LIBRARY = "LIBRARY";
    public static final String WORK_LOG = "WORK_LOG";
    public static final String ABHA_NUMBER = "ABHA_NUMBER";

    //KIOSK Menus
    public static final String KIOSK_LIST_OF_TOURISTS = "KIOSK_LIST_OF_TOURISTS";
    public static final String MEDICAL_RELIEF_POSTS = "MEDICAL_RELIEF_POSTS";
    public static final String REQUEST_EMERGENCY_SUPPORT = "REQUEST_EMERGENCY_SUPPORT";
    public static final String VIEW_EMERGENCY_REQUESTS = "VIEW_EMERGENCY_REQUESTS";
    public static final String GET_SCREENING_HISTORY = "GET_SCREENING_HISTORY";

    private static Map<String, Integer> menuIcons;

    public static Integer getMenuIcons(String constant) {
        if (constant == null)
            return null;

        if (menuIcons == null) {
            menuIcons = new HashMap<>();
            //FHW Role
            menuIcons.put(FHW_CFHC, R.drawable.fhs);
            menuIcons.put(FHW_DATA_QUALITY, R.drawable.reverification);
            menuIcons.put(FHW_SURVEILLANCE, R.drawable.idsp);
            menuIcons.put(FHW_ASSIGN_FAMILY, R.drawable.assign_family);
            menuIcons.put(FHW_MY_PEOPLE, R.drawable.my_people);
            menuIcons.put(FHW_MOBILE_VERIFICATION, R.drawable.aadhar);
            menuIcons.put(FHW_NOTIFICATION, R.drawable.schedule);
            menuIcons.put(FHW_HIGH_RISK_WOMEN_AND_CHILD, R.drawable.high_risk_mother_child);
            menuIcons.put(FHW_NCD_SCREENING, R.drawable.ncd_screening);
            menuIcons.put(FHW_NCD_REGISTER, R.drawable.ncd_reg);
            menuIcons.put(FHW_WORK_REGISTER, R.drawable.work_report);
            menuIcons.put(FHW_WORK_STATUS, R.drawable.work_status);
            menuIcons.put(LEARNING_MANAGEMENT_SYSTEM, R.drawable.menu_lms);
            menuIcons.put(LMS_PROGRESS_REPORT, R.drawable.menu_lms_report);
            menuIcons.put(FHW_NCD_CONFIRMATION, R.drawable.ncd_screening);
            menuIcons.put(FHW_NCD_WEEKLY_VISIT, R.drawable.ncd_screening);
            //ASHA Role
            menuIcons.put(ASHA_FHS, R.drawable.fhs);
            menuIcons.put(ASHA_MY_PEOPLE, R.drawable.my_people);
            menuIcons.put(ASHA_NOTIFICATION, R.drawable.schedule);
            menuIcons.put(ASHA_HIGH_RISK_BENEFICIARIES, R.drawable.high_risk_mother_child);
            menuIcons.put(ASHA_CBAC_ENTRY, R.drawable.ncd_screening);
            menuIcons.put(ASHA_NCD_REGISTER, R.drawable.ncd_reg);
            menuIcons.put(ASHA_NPCB_SCREENING, R.drawable.npcb);
            menuIcons.put(ASHA_WORK_REGISTER, R.drawable.work_report);

            //CHO Role

            //FHSR Role
            menuIcons.put(FHSR_PHONE_NUMBER_VERIFICATION, R.drawable.reverification);

            //AWW Role
            menuIcons.put(AWW_FHS, R.drawable.fhs);
            menuIcons.put(AWW_MY_PEOPLE, R.drawable.my_people);
            menuIcons.put(AWW_NOTIFICATION, R.drawable.schedule);
            menuIcons.put(DAILY_NUTRITION, R.drawable.ncd_reg);
            menuIcons.put(TAKE_HOME_RATION, R.drawable.assign_family);

            //Lab Technician Role
            menuIcons.put(OPD_FACILITY, R.drawable.labtest);

            //RBSK Role
            menuIcons.put(HEAD_TO_TOE_SCREENING, R.drawable.head_to_toe_screening);

            //Common
            menuIcons.put(LIBRARY, R.drawable.folder);
//            menuIcons.put(ANNOUNCEMENTS, R.drawable.announcement);
            menuIcons.put(WORK_LOG, R.drawable.work_status);
            menuIcons.put(ABHA_NUMBER, R.drawable.ic_medical_card);
            menuIcons.put(KIOSK_LIST_OF_TOURISTS, R.drawable.fhs);
            menuIcons.put(MEDICAL_RELIEF_POSTS, R.drawable.fhs);
            menuIcons.put(REQUEST_EMERGENCY_SUPPORT, R.drawable.emergency_icon);
            menuIcons.put(VIEW_EMERGENCY_REQUESTS, R.drawable.emergency_icon);
            menuIcons.put(GET_SCREENING_HISTORY, R.drawable.npcb);
        }

        if (menuIcons.containsKey(constant.trim())) {
            return menuIcons.get(constant.trim());
        }
        return null;
    }

    public static String getFormCodeFromMenuConstant(String menu) {
        if (menu == null) {
            return null;
        }

        switch (menu) {
            case MenuConstants.FHW_CFHC:
                return FormConstants.FORM_TYPE_FHS;
            case MenuConstants.ASHA_NOTIFICATION:
            case MenuConstants.FHW_NOTIFICATION:
            case MenuConstants.ASHA_HIGH_RISK_BENEFICIARIES:
            case MenuConstants.FHW_HIGH_RISK_WOMEN_AND_CHILD:
                return FormConstants.FORM_TYPE_RCH;
            case MenuConstants.ASHA_CBAC_ENTRY:
            case MenuConstants.FHW_NCD_SCREENING:
            case MenuConstants.FHW_NCD_REGISTER:
            case MenuConstants.ASHA_NCD_REGISTER:
            case MenuConstants.FHW_NCD_WEEKLY_VISIT:
            case MenuConstants.FHW_NCD_CONFIRMATION:
                return FormConstants.FORM_TYPE_NCD;
            default:
                return null;
        }
    }

}
