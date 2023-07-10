package com.argusoft.sewa.android.app.constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RchConstants {

    private RchConstants() {
        throw new IllegalStateException("Utility Class");
    }

    //Member Status
    public static final String MEMBER_STATUS_AVAILABLE = "AVAILABLE";
    public static final String MEMBER_STATUS_DEATH = "DEATH";

    //
    public static final String OTHER = "OTHER";
    public static final String NONE = "NONE";
    public static final String NOT_KNOWN = "NK";

    //
    public static final String NOT_DONE = "NOT_DONE";

    //Family Planning Methods
    public static final String FEMALE_STERILIZATION = "FMLSTR";
    public static final String MALE_STERILIZATION = "MLSTR";
    public static final String IUCD_5_YEARS = "IUCD5";
    public static final String IUCD_10_YEARS = "IUCD10";
    public static final String CONDOM = "CONDOM";
    public static final String ORAL_PILLS = "ORALPILLS";
    public static final String CHHAYA = "CHHAYA";
    public static final String ANTARA = "ANTARA";
    public static final String EMERGENCY_CONTRACEPTIVE_PILLS = "CONTRA";
    public static final String PPIUCD = "PPIUCD";
    public static final String PAIUCD = "PAIUCD";

    //Vaccinations
    public static final String HEPATITIS_B_0 = "HEPATITIS_B_0";
    public static final String BCG = "BCG";
    public static final String VITAMIN_K = "VITAMIN_K";
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
    public static final String MEASLES_RUBELLA_1 = "MEASLES_RUBELLA_1";
    public static final String MEASLES_1 = "MEASLES_1";
    public static final String DPT_BOOSTER = "DPT_BOOSTER";
    public static final String F_IPV_1_01 = "F_IPV_1_01";
    public static final String F_IPV_2_01 = "F_IPV_2_01";
    public static final String F_IPV_2_05 = "F_IPV_2_05";
    public static final String MEASLES_RUBELLA_2 = "MEASLES_RUBELLA_2";
    public static final String MEASLES_2 = "MEASLES_2";
    public static final String VITAMIN_A = "VITAMIN_A";
    public static final String ROTA_VIRUS_1 = "ROTA_VIRUS_1";
    public static final String ROTA_VIRUS_2 = "ROTA_VIRUS_2";
    public static final String ROTA_VIRUS_3 = "ROTA_VIRUS_3";
    public static final String TT1 = "TT1";
    public static final String TT2 = "TT2";
    public static final String TT_BOOSTER = "TT_BOOSTER";

    // Health Infrastructure Type IDs
    public static final Long INFRA_DISTRICT_HOSPITAL = 1007L;
    public static final Long INFRA_SUB_DISTRICT_HOSPITAL = 1008L;
    public static final Long INFRA_COMMUNITY_HEALTH_CENTER = 1009L;
    public static final Long INFRA_TRUST_HOSPITAL = 1010L;
    public static final Long INFRA_MEDICAL_COLLEGE_HOSPITAL = 1012L;
    public static final Long INFRA_PRIVATE_HOSPITAL = 1013L;
    public static final Long INFRA_PHC = 1061L;
    public static final Long INFRA_SC = 1062L;
    public static final Long INFRA_UPHC = 1063L;
    public static final Long INFRA_URBAN_COMMUNITY_HEALTH_CENTER = 1084L;

    //Health Infrastructure Types
    public static final String INFRA_TYPE_SC = "Sub Center";

    //SD Score Constants
    public static final String SD_SCORE_SD4 = "SD4";
    public static final String SD_SCORE_SD3 = "SD3";
    public static final String SD_SCORE_SD2 = "SD2";
    public static final String SD_SCORE_SD1 = "SD1";
    public static final String SD_SCORE_MEDIAN = "MEDIAN";
    public static final String SD_SCORE_NONE = "NONE";
    public static final String SD_SCORE_CANNOT_BE_CALCULATED = "Cannot be calculated";

    //Place of birth
    public static final String HOME = "HOME";
    public static final String HOSP = "HOSP";
    public static final String ON_THE_WAY = "ON_THE_WAY";
    public static final String AMBULANCE_108 = "108_AMBULANCE";
    public static final String OUT_OF_STATE_GOVT = "OUT_OF_STATE_GOVT";
    public static final String OUT_OF_STATE_PVT = "OUT_OF_STATE_PVT";

    //RCH HIGH RISK
    public static final String HIGH_RISK_HIGH_BLOOD_PRESSURE = "High Blood Pressure";
    public static final String HIGH_RISK_LOW_HAEMOGLOBIN = "Low Haemoglobin";
    public static final String HIGH_RISK_LOW_OXYGEN = "Low Oxygen";
    public static final String HIGH_TEMP = "High Temperature";
    public static final String HIGH_BLOOD_SUGAR_LEVEL = "High Blood Sugar";
    public static final String HIGH_RISK_VERY_LOW_WEIGHT = "Very Low Weight";
    public static final String HIGH_RISK_URINE_ALBUMIN = "Urine Albumin";
    public static final String HIGH_RISK_URINE_SUGAR = "Urine Sugar";
    public static final String HIGH_RISK_SICKLE_CELL = "Sickle Cell Test Positive";
    public static final String NO_RISK_FOUND = "No risks found";
    public static final String NO_RISK_IDENTIFIED_IN_THIS_VISIT = "No high risks were identified in this visit";
    public static final String HEALTH_ADVISORY_FOR = "Health Advisory For";

    //CEREBRAL PALSY CONSTANTS
    public static final String CP_DELAYED_DEVELOPMENT = "DD";
    public static final String CP_TREATMENT_COMMENCED = "TC";

    public static final List<String> PHI_INSTITUTIONS_TYPE_ID_FOR_2_DAYS_DEL_GAP = Collections.unmodifiableList(getPhiInstitutionsTypeIdFor2DaysDelGap());
    public static final List<Long> GOVERNMENT_INSTITUTIONS = Collections.unmodifiableList(getGovernmentInstitutions());

    private static List<String> getPhiInstitutionsTypeIdFor2DaysDelGap() {
        List<String> stringList = new ArrayList<>();
        stringList.add(RchConstants.INFRA_DISTRICT_HOSPITAL.toString());
        stringList.add(RchConstants.INFRA_SUB_DISTRICT_HOSPITAL.toString());
        stringList.add(RchConstants.INFRA_COMMUNITY_HEALTH_CENTER.toString());
        stringList.add(RchConstants.INFRA_MEDICAL_COLLEGE_HOSPITAL.toString());
        stringList.add(RchConstants.INFRA_PHC.toString());
        stringList.add(RchConstants.INFRA_UPHC.toString());
        stringList.add(RchConstants.INFRA_URBAN_COMMUNITY_HEALTH_CENTER.toString());
        return stringList;
    }

    private static List<Long> getGovernmentInstitutions() {
        List<Long> longList = new ArrayList<>();
        longList.add(RchConstants.INFRA_DISTRICT_HOSPITAL);
        longList.add(RchConstants.INFRA_SUB_DISTRICT_HOSPITAL);
        longList.add(RchConstants.INFRA_COMMUNITY_HEALTH_CENTER);
        longList.add(RchConstants.INFRA_MEDICAL_COLLEGE_HOSPITAL);
        longList.add(RchConstants.INFRA_PHC);
        longList.add(RchConstants.INFRA_SC);
        longList.add(RchConstants.INFRA_UPHC);
        longList.add(RchConstants.INFRA_URBAN_COMMUNITY_HEALTH_CENTER);
        return longList;
    }

}
