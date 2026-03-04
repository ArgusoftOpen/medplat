package com.argusoft.sewa.android.app.constants;

import com.argusoft.sewa.android.app.util.GlobalTypes;

import java.util.HashMap;
import java.util.Map;

public class FullFormConstants {

    private FullFormConstants() {
        throw new IllegalStateException("Utility Class");
    }

    private static Map<String, String> genderFullNames;
    private static Map<String, String> placeFullNames;
    private static Map<String, String> cbacDetails;
    private static Map<String, String> familyPlanningFullNames;
    private static Map<String, String> vaccinesFullNameMap;


    public static String getFullFormOfGender(String constant) {
        if (constant == null)
            return null;

        if (genderFullNames == null) {
            genderFullNames = new HashMap<>();
            genderFullNames.put(GlobalTypes.MALE, "Male");
            genderFullNames.put(GlobalTypes.FEMALE, "Female");
            genderFullNames.put(GlobalTypes.TRANSGENDER, "Transgender");
        }

        if (genderFullNames.containsKey(constant.trim())) {
            return genderFullNames.get(constant.trim());
        }
        return constant;
    }

    public static String getFullFormsOfPlace(String constant) {
        if (constant == null)
            return null;

        if (placeFullNames == null) {
            placeFullNames = new HashMap<>();
            placeFullNames.put(RchConstants.HOME, "Home");
            placeFullNames.put(RchConstants.HOSP, "Hospital");
            placeFullNames.put(RchConstants.ON_THE_WAY, "On the way");
            placeFullNames.put(RchConstants.AMBULANCE_108, "108 Ambulance");
            placeFullNames.put(RchConstants.OUT_OF_STATE_GOVT, "Out of State - Govt. Hospital");
            placeFullNames.put(RchConstants.OUT_OF_STATE_PVT, "Out of State - Private Hospital");
        }

        if (placeFullNames.containsKey(constant.trim())) {
            return placeFullNames.get(constant.trim());
        }
        return constant;
    }

    public static String getFullFormOfFamilyPlanningMethods(String constant) {
        if (constant == null) {
            return null;
        }

        if (familyPlanningFullNames == null) {
            familyPlanningFullNames = new HashMap<>();
            familyPlanningFullNames.put(RchConstants.FEMALE_STERILIZATION, "Female Sterilization");
            familyPlanningFullNames.put(RchConstants.MALE_STERILIZATION, "Male Sterilization");
            familyPlanningFullNames.put(RchConstants.IUCD_5_YEARS, "IUCD - 5 years");
            familyPlanningFullNames.put(RchConstants.IUCD_10_YEARS, "IUCD - 10 years");
            familyPlanningFullNames.put(RchConstants.CONDOM, "Condom");
            familyPlanningFullNames.put(RchConstants.ORAL_PILLS, "Oral Pills");
            familyPlanningFullNames.put(RchConstants.CHHAYA, "Chaya");
            familyPlanningFullNames.put(RchConstants.ANTARA, "Antara");
            familyPlanningFullNames.put(RchConstants.EMERGENCY_CONTRACEPTIVE_PILLS, "Emergency Contraceptive Pills");
            familyPlanningFullNames.put(RchConstants.PPIUCD, "Post Partum IUCD");
            familyPlanningFullNames.put(RchConstants.PAIUCD, "Post Abortion IUCD");
        }

        if (familyPlanningFullNames.containsKey(constant.trim())) {
            return familyPlanningFullNames.get(constant.trim());
        }

        return constant;
    }

    public static String getFullFormOfCBACConstants(String constant) {
        if (constant == null) {
            return null;
        }

        if (cbacDetails == null) {
            cbacDetails = new HashMap<>();
            cbacDetails.put("KUCCHA", "Kuccha");
            cbacDetails.put("PUCCA_STONE_MORTAS", "Pucca with stone and mortas");
            cbacDetails.put("PUCCA_BRICKS_CONCRETE", "Pucca with bricks and concrete");
            cbacDetails.put("FLUSH_WITH_WATER", "Flush toilet with running watter supply");
            cbacDetails.put("FLUSH_WITHOUT_WATER", "Flush toilet without running water supply");
            cbacDetails.put("PIT_WITH_WATER", "Pit toilet with running water supply");
            cbacDetails.put("PIT_WITHOUT_WATER", "Pit toilet without running water supply");
            cbacDetails.put("ELECTRICITY_SUPPLY", "Electricity supply");
            cbacDetails.put("GENERATOR", "Generator");
            cbacDetails.put("SOLAR_POWER", "Solar power");
            cbacDetails.put("KEROSENE_LAMP", "Kerosene lamp");
            cbacDetails.put("TAP", "Tap water");
            cbacDetails.put("HAND_PUMP_INSIDE", "Hand pump within house");
            cbacDetails.put("HAND_PUMP_OUTSIDE", "Hand pump outside of house");
            cbacDetails.put("WELL", "Well");
            cbacDetails.put("TANK", "Tank");
            cbacDetails.put("RIVER", "River");
            cbacDetails.put("POND", "Pond");
            cbacDetails.put("FIREWOOD", "Firewood");
            cbacDetails.put("CROP_RESIDUE", "Crop Residue");
            cbacDetails.put("COW_DUNG_CAKE", "Cow Dung Cake");
            cbacDetails.put("COAL", "Coal");
            cbacDetails.put("KEROSENE", "Kerosene");
            cbacDetails.put("MOTOR_BIKE", "Motor Bike");
            cbacDetails.put("CAR", "Car");
            cbacDetails.put("TRACTOR", "Tractor");
            cbacDetails.put("RENT", "Rent");
            cbacDetails.put("OWN", "Own");
            cbacDetails.put("0_TO_10K", "0 to 10000");
            cbacDetails.put("10K_TO_20K", "10000 to 20000");
            cbacDetails.put("20K_TO_30K", "20000 to 30000");
            cbacDetails.put("30K_TO_50K", "30000 to 50000");
            cbacDetails.put("50K_TO_100K", "50000 to 100000");
            cbacDetails.put("100K_TO_200K", "100000 to 200000");
            cbacDetails.put("GT_200K", "More than 200000");
            cbacDetails.put("DONT_KNOW", "Don’t know");
            cbacDetails.put("NEVER", "Never");
            cbacDetails.put("IN_PAST", "Used to consume in Past/ Sometimes now");
            cbacDetails.put("DAILY", "Daily");
            cbacDetails.put("LT80", "80 cm or less");
            cbacDetails.put("81TO90", "81-90 cm");
            cbacDetails.put("GT90", "More than 90 cm");
            cbacDetails.put("LT90", "90 cm or less");
            cbacDetails.put("91TO100", "91-100 cm");
            cbacDetails.put("GT100", "More than 100 cm");
            cbacDetails.put("ATLEAST_150", "Atleast 150 minutes in a week");
            cbacDetails.put("LESS_THAN_150", "Less than 150 minutes in a week");
            cbacDetails.put("CROP_BURNING", "Crop residue burning / burning of garbage");
            cbacDetails.put("INDUSTRY_WORK", "Leaves / Working in industries with smoke, gas and dust exposure such as brick kilns and glass factories etc.");
            cbacDetails.put("NONE", "None");
            cbacDetails.put("OTHER", "Other");
            cbacDetails.put("NORMAL", "Normal");
            cbacDetails.put("HEAVY", "Heavy");
            cbacDetails.put("CLOTS", "Clots");
            cbacDetails.put("PAIN", "Pain");
            cbacDetails.put("GOVT", "Government");
            cbacDetails.put("PRIVATE", "Private");
            cbacDetails.put("SELF", "Self");
            cbacDetails.put("FAMILY_MEMBER", "Family member");
            cbacDetails.put("VISUALLY_IMPAIRED", "Visually impaired");
            cbacDetails.put("KNOWN_DISABILITY", "Known disability");
            cbacDetails.put("BEDRIDDEN", "Bedridden");
            cbacDetails.put("DEPENDENT_ON_OTHERS", "Dependent on others for daily activities");
            cbacDetails.put("NOT_AT_ALL", "Not at all");
            cbacDetails.put("SEVERAL", "Several days");
            cbacDetails.put("MORE_THAN_HALF", "More than half the days");
            cbacDetails.put("EVERYDAY", "Nearly everyday");
        }

        if (cbacDetails.containsKey(constant.trim())) {
            return cbacDetails.get(constant.trim());
        }
        return constant;
    }

    public static String getFullFormOfVaccines(String constant) {
        if (constant == null) {
            return null;
        }

        if (vaccinesFullNameMap == null) {
            vaccinesFullNameMap = new HashMap<>();
            vaccinesFullNameMap.put("HEPATITIS_B_0", "Hepatitis B 0");
            vaccinesFullNameMap.put("BCG", "BCG");
            vaccinesFullNameMap.put("OPV_0", "OPV 0");
            vaccinesFullNameMap.put("OPV_1", "OPV 1");
            vaccinesFullNameMap.put("OPV_2", "OPV 2");
            vaccinesFullNameMap.put("OPV_3", "OPV 3");
            vaccinesFullNameMap.put("OPV_BOOSTER", "OPV Booster");
            vaccinesFullNameMap.put("PENTA_1", "Penta 1");
            vaccinesFullNameMap.put("PENTA_2", "Penta 2");
            vaccinesFullNameMap.put("PENTA_3", "Penta 3");
            vaccinesFullNameMap.put("DPT_1", "DPT 1");
            vaccinesFullNameMap.put("DPT_2", "DPT 2");
            vaccinesFullNameMap.put("DPT_3", "DPT 3");
            vaccinesFullNameMap.put("DPT_BOOSTER", "DPT Booster");
            vaccinesFullNameMap.put("MEASLES_1", "Measles 1");
            vaccinesFullNameMap.put("MEASLES_2", "Measles 2");
            vaccinesFullNameMap.put("F_IPV_1_01", "F IPV 1 01");
            vaccinesFullNameMap.put("F_IPV_2_01", "F IPV 2 01");
            vaccinesFullNameMap.put("F_IPV_2_05", "F IPV 2 05");
            vaccinesFullNameMap.put("VITAMIN_A", "Vitamin A");
            vaccinesFullNameMap.put("VITAMIN_K", "Vitamin K");
            vaccinesFullNameMap.put("ROTA_VIRUS_1", "Rota Virus 1");
            vaccinesFullNameMap.put("ROTA_VIRUS_2", "Rota Virus 2");
            vaccinesFullNameMap.put("ROTA_VIRUS_3", "Rota Virus 3");
            vaccinesFullNameMap.put("MEASLES_RUBELLA_1", "Measles Rubella 1");
            vaccinesFullNameMap.put("MEASLES_RUBELLA_2", "Measles Rubella 2");
            vaccinesFullNameMap.put("TT1", "TT 1");
            vaccinesFullNameMap.put("TT2", "TT 1");
            vaccinesFullNameMap.put("TT_BOOSTER", "TT Booster");
            vaccinesFullNameMap.put("MMR", "MMR");
            vaccinesFullNameMap.put("HIB_1", "HIB 1");
            vaccinesFullNameMap.put("HIB_2", "HIB 2");
            vaccinesFullNameMap.put("HIB_3", "HIB 3");
            vaccinesFullNameMap.put("HEPATITIS_B_1", "Hepatitis B 1");
            vaccinesFullNameMap.put("HEPATITIS_B_2", "Hepatitis B 2");
            vaccinesFullNameMap.put("HEPATITIS_B_3", "Hepatitis B 3");
            vaccinesFullNameMap.put("HEPATITIS_A_1", "Hepatitis A 1");
            vaccinesFullNameMap.put("HEPATITIS_A_2", "Hepatitis A 2");
            vaccinesFullNameMap.put("CHICKEN_POX", "Chicken Pox");
            vaccinesFullNameMap.put("JE", "JE");
            vaccinesFullNameMap.put("DPT_BOOSTER_2", "DPT Booster 2");
            vaccinesFullNameMap.put("TT_10", "TT 10");
            vaccinesFullNameMap.put("TT_16", "TT 16");
            vaccinesFullNameMap.put("TYPHOID", "Typhoid");
        }

        if (vaccinesFullNameMap.containsKey(constant.trim())) {
            return vaccinesFullNameMap.get(constant.trim());
        }

        return constant;
    }
}
