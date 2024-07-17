package com.argusoft.sewa.android.app.datastructure;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyVaccinationStatus;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.core.DailyNutritionLogService;
import com.argusoft.sewa.android.app.core.HealthInfrastructureService;
import com.argusoft.sewa.android.app.core.ImmunisationService;
import com.argusoft.sewa.android.app.core.LocationMasterService;
import com.argusoft.sewa.android.app.core.NPCBService;
import com.argusoft.sewa.android.app.core.NcdScoreService;
import com.argusoft.sewa.android.app.core.NcdService;
import com.argusoft.sewa.android.app.core.RchHighRiskService;
import com.argusoft.sewa.android.app.core.SchoolService;
import com.argusoft.sewa.android.app.core.SewaFhsService;
import com.argusoft.sewa.android.app.core.SewaService;
import com.argusoft.sewa.android.app.core.SewaServiceRestClient;
import com.argusoft.sewa.android.app.core.TechoService;
import com.argusoft.sewa.android.app.core.VersionService;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.FieldValueMobDataBean;
import com.argusoft.sewa.android.app.databean.MedicineListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.NcdMemberMedicineDataBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.databean.OptionDataBean;
import com.argusoft.sewa.android.app.model.HealthInfrastructureBean;
import com.argusoft.sewa.android.app.model.LabelBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.model.SchoolBean;
import com.argusoft.sewa.android.app.morbidities.constants.MorbiditiesConstant;
import com.argusoft.sewa.android.app.service.GPSTracker;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.UtilBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TreeMap;

/**
 * @author alpeshkyada
 */
public class SharedStructureData {

    private SharedStructureData() {
        throw new IllegalStateException("Utility Class");
    }

    public static boolean dbDowngraded = false;
    //  Shared Data For Dynamic Form Generation
    public static final Map<String, String> relatedPropertyHashTable = new HashMap<>();
    public static final List<String> ashaSheets = new ArrayList<>();
    public static final List<String> awwSheets = new ArrayList<>();
    public static final List<String> fhwSheets = new ArrayList<>();
    public static final List<String> gpsEnabledForms = new ArrayList<>();
    public static Map<String, Map<String, List<Double>>> mapOfLatLongWithLGDCode = new HashMap<>();

    // Service Classes
    public static SewaService sewaService;
    public static TechoService techoService;
    public static SewaFhsService sewaFhsService;
    public static RchHighRiskService rchHighRiskService;
    public static ImmunisationService immunisationService;
    public static NcdScoreService ncdScoreService;
    public static NcdService ncdService;
    public static HealthInfrastructureService healthInfrastructureService;
    public static LocationMasterService locationMasterService;
    public static NPCBService npcbService;
    public static SchoolService schoolService;
    public static SewaServiceRestClient sewaServiceRestClient;
    public static DailyNutritionLogService dailyNutritionLogService;
    public static VersionService versionService;

    public static Context context;
    public static Context welcomeContext;

    public static int currentQuestion;  // question id for external activity called like camera or video etc.
    public static int loopBakCounter; // that is used in repetitive question
    public static int newBindingForMorbidity;

    public static Map<Integer, QueFormBean> mapIndexQuestion;
    public static Map<Integer, PageFormBean> mapIndexPage;
    public static Map<String, List<FieldValueMobDataBean>> mapDataMapLabelBean;
    public static Map<String, LabelBean> listLabelBeans;
    public static Map<String, Date> vaccineGivenDateMap = new HashMap<>();

    public static boolean ismapUpdate = false;
    public static boolean isLogin = false;

    public static GPSTracker gps;
    public static Map<Integer, Boolean> videoShownMap = new HashMap<>();

    public static List<OptionDataBean> middleNameList = new ArrayList<>();
    public static List<OptionDataBean> membersUnderTwenty = new ArrayList<>();
    public static List<OptionDataBean> givenVaccinesToChild = new ArrayList<>();
    public static List<OptionDataBean> childrenFrom3To6Years = new ArrayList<>();
    public static List<OptionDataBean> basicMedicineList = new ArrayList<>();
    public static List<MedicineListItemDataBean> prescribedMedicineList = new ArrayList<>();
    public static Map<Integer, NcdMemberMedicineDataBean> prescribedMedicineMap = new HashMap<>();
    public static List<MedicineListItemDataBean> additionalMedicineList = new ArrayList<>();
    public static Integer totalFamilyMembersCount = 0;
    public static FamilyDataBean currentFamilyDataBean;
    public static MemberDataBean currentMemberDataBean;
    public static FamilyDataBean familyDataBeanToBeMerged = null;
    public static String currentMemberUHId;
    public static long formFillUpTime;
    public static NotificationMobDataBean notificationMobDataBean;
    public static String NETWORK_MESSAGE = SewaConstants.NETWORK_AVAILABLE;
    //MORBIDITY MAP [For FHW]
    public static HashMap<String, String> MORBIDITY_CODE_MAP = new HashMap<>();
    public static Typeface typeface = Typeface.DEFAULT;
    //for audio multiple files
    public static Map<String, String> audioFilesToUpload = new HashMap<>();
    public static Boolean isAadharNumberScanned = Boolean.FALSE;
    public static Boolean isDobScannedFromAadhar = Boolean.FALSE;
    public static String formType = null;
    public static MemberBean currentRchMemberBean;
    public static FamilyDataBean currentRchFamilyDataBean;
    private static Map<String, String> mapAllMobidites;
    private static Map<String, List<String>> mapOfListOfQuestionLIC;
    public static HealthInfrastructureBean selectedHealthInfra;
    public static Boolean currentlyDownloadingFamilyData = false;
    public static SchoolBean selectedSchool;
    public static Boolean isMobileVerificationBlocked = false;
    public static Boolean isAnyMemberMobileVerificationDone = false;
    public static MyAlertDialog workLogAlert;

    public static boolean CHO_ATTENDANCE_IS_MARKED_PRESENCE = false;
    public static Timer choAttendanceTimer;

    public static Timer ordtTimer;
    public static Intent ordtIntent;

    public static Map<String, MyVaccinationStatus> vaccinations;

    public static Integer totalMemberMedicineCount = 0;

    public static void setVaccinations(Map<String, MyVaccinationStatus> vaccination) {
        vaccinations = vaccination;
    }

    public static Boolean isNDHMHealthIdVerificationBlocked;

    public static void setIsNDHMHealthIdVerificationBlocked(Boolean flag) {
        isNDHMHealthIdVerificationBlocked = flag;
    }

    static {
        groupAshaSheets();
        groupAwwSheets();
        groupFHWSheets();
        gpsFeatureEnabledForms();
        includeMorbidityCodeNNamesMap();
    }

    public static boolean isEmptyMapAllMorbidities(String question, String forWhome) {
        return mapAllMobidites == null || mapAllMobidites.isEmpty() || mapAllMorbiditiesOnlyRelatedToMother(question, forWhome) || (mapAllMobidites.keySet().size() == 1 && (mapAllMobidites.containsKey(question + GlobalTypes.KEY_VALUE_SEPARATOR + forWhome) || mapAllMobidites.containsKey("Delivery" + GlobalTypes.KEY_VALUE_SEPARATOR + forWhome)));
    }

    private static boolean mapAllMorbiditiesOnlyRelatedToMother(String question, String forWhome) {
        int counter = 0;
        for (Map.Entry<String, String> entry : mapAllMobidites.entrySet()) {
            String[] split = entry.getKey().split(GlobalTypes.KEY_VALUE_SEPARATOR);
            if (split.length > 1 && MorbiditiesConstant.getOnlyMotherRelatedPNCMorbidities().contains(split[0])) {
                counter++;
            }
        }
        if (mapAllMobidites.containsKey(question + GlobalTypes.KEY_VALUE_SEPARATOR + forWhome)) {
            counter++;
        }
        if (mapAllMobidites.containsKey("Delivery" + GlobalTypes.KEY_VALUE_SEPARATOR + forWhome)) {
            counter++;
        }
        return counter == mapAllMobidites.keySet().size();
    }

    public static void addItemInLICList(String question, String answer, String forWhome) {
        if (mapAllMobidites == null) {
            mapAllMobidites = new HashMap<>();
            mapOfListOfQuestionLIC = new HashMap<>();
        }
        List<String> listQuestion = mapOfListOfQuestionLIC.get(forWhome);
        if (listQuestion == null) {
            listQuestion = new ArrayList<>();
            mapOfListOfQuestionLIC.put(forWhome, listQuestion);
        }
        if (!listQuestion.contains(question)) {
            listQuestion.add(question);
        }
        mapAllMobidites.put(question + GlobalTypes.KEY_VALUE_SEPARATOR + forWhome, answer);
        ismapUpdate = true;
    }

    public static void removeItemFromLICList(String question, String forWhome) {
        if (mapAllMobidites != null) {
            mapAllMobidites.remove(question + GlobalTypes.KEY_VALUE_SEPARATOR + forWhome);
        }
        ismapUpdate = true;
    }

    public static Map<String, List<String>> getListOfLIC() {
        if (mapAllMobidites != null && mapOfListOfQuestionLIC != null) {
            TreeMap<String, List<String>> data = new TreeMap<>();
            for (Map.Entry<String, List<String>> entry : mapOfListOfQuestionLIC.entrySet()) {
                String key = entry.getKey();
                List<String> listValue = entry.getValue();
                List<String> finalAnswer = new ArrayList<>();
                for (String question : listValue) {
                    String answer = mapAllMobidites.get(question + GlobalTypes.KEY_VALUE_SEPARATOR + key);
                    if (answer != null) {
                        finalAnswer.add(UtilBean.getMyLabel(question) + "=" + UtilBean.getMyLabel(answer));
                    }
                }
                data.put(key, finalAnswer);
            }
            return data;
        }
        return null;
    }

    public static void initIndexQuestionMap(Context c,
                                            SewaService service,
                                            SewaFhsService fhsService,
                                            RchHighRiskService rchRiskService,
                                            ImmunisationService immunisation,
                                            NcdService ncdService1,
                                            NcdScoreService scoreService,
                                            HealthInfrastructureService infrastructureService,
                                            LocationMasterService locationService,
                                            SchoolService schoolListService,
                                            SewaServiceRestClient restClient,
                                            DailyNutritionLogService nutritionLogService,
                                            NPCBService npcbService1,
                                            VersionService versionService1) {
        context = c;
        sewaService = service;
        sewaFhsService = fhsService;
        rchHighRiskService = rchRiskService;
        immunisationService = immunisation;
        ncdService = ncdService1;
        ncdScoreService = scoreService;
        healthInfrastructureService = infrastructureService;
        locationMasterService = locationService;
        npcbService = npcbService1;
        schoolService = schoolListService;
        sewaServiceRestClient = restClient;
        dailyNutritionLogService = nutritionLogService;
        loopBakCounter = 0;
        newBindingForMorbidity = 0;
        versionService = versionService1;
    }

    public static void resetSharedStructureData() {
        currentQuestion = -1;
        loopBakCounter = 0;
        newBindingForMorbidity = 0;
        mapIndexQuestion = null;
        mapIndexPage = null;
        mapDataMapLabelBean = null;
        mapAllMobidites = null;
        mapOfListOfQuestionLIC = null;
        ismapUpdate = false;
    }

    private static void groupFHWSheets() {
        fhwSheets.clear();
        fhwSheets.add(FormConstants.FAMILY_HEALTH_SURVEY);
        fhwSheets.add(FormConstants.CFHC);
        fhwSheets.add(FormConstants.FHS_MEMBER_UPDATE);
        fhwSheets.add(FormConstants.LMP_FOLLOW_UP);
        fhwSheets.add(FormConstants.TECHO_FHW_ANC);
        fhwSheets.add(FormConstants.TECHO_FHW_PNC);
        fhwSheets.add(FormConstants.TECHO_FHW_WPD);
        fhwSheets.add(FormConstants.TECHO_FHW_CI);
        fhwSheets.add(FormConstants.TECHO_FHW_CS);
        fhwSheets.add(FormConstants.TECHO_FHW_VAE);
        fhwSheets.add(FormConstants.TECHO_FHW_RIM);
        fhwSheets.add(FormConstants.TECHO_WPD_DISCHARGE);
        fhwSheets.add(FormConstants.TECHO_CS_APPETITE_TEST);
        fhwSheets.add(FormConstants.NCD_FHW_HYPERTENSION);
        fhwSheets.add(FormConstants.NCD_FHW_DIABETES);
        fhwSheets.add(FormConstants.NCD_FHW_ORAL);
        fhwSheets.add(FormConstants.NCD_FHW_BREAST);
        fhwSheets.add(FormConstants.NCD_FHW_CERVICAL);
        fhwSheets.add(FormConstants.NCD_FHW_MENTAL_HEALTH);
        fhwSheets.add(FormConstants.NCD_FHW_HEALTH_SCREENING);
        fhwSheets.add(FormConstants.NCD_PERSONAL_HISTORY);
        fhwSheets.add(FormConstants.NCD_FHW_DIABETES_CONFIRMATION);
        fhwSheets.add(FormConstants.NCD_FHW_WEEKLY_CLINIC);
        fhwSheets.add(FormConstants.NCD_FHW_WEEKLY_HOME);
        fhwSheets.add(FormConstants.TT2_ALERT);
        fhwSheets.add(FormConstants.IRON_SUCROSE_ALERT);
        fhwSheets.add(FormConstants.FHW_PREGNANCY_CONFIRMATION);
        fhwSheets.add(FormConstants.FHW_DEATH_CONFIRMATION);
        fhwSheets.add(FormConstants.FHW_SAM_SCREENING_REF);
        fhwSheets.add(FormConstants.CMAM_FOLLOWUP);
        fhwSheets.add(FormConstants.GERIATRICS_MEDICATION_ALERT);
        fhwSheets.add(FormConstants.IDSP_MEMBER);
        fhwSheets.add(FormConstants.IDSP_MEMBER_2);
        fhwSheets.add(FormConstants.IDSP_NEW_FAMILY);
        fhwSheets.add(FormConstants.TRAVELLERS_SCREENING);
        fhwSheets.add(FormConstants.DNHDD_NCD_HYPERTENSION_DIABETES_AND_MENTAL_HEALTH);
        fhwSheets.add(FormConstants.CANCER_SCREENING);
    }

    private static void groupAshaSheets() {
        ashaSheets.clear();
        ashaSheets.add(FormConstants.FAMILY_HEALTH_SURVEY);
        ashaSheets.add(FormConstants.ASHA_LMPFU);
        ashaSheets.add(FormConstants.ASHA_PNC);
        ashaSheets.add(FormConstants.ASHA_ANC);
        ashaSheets.add(FormConstants.ASHA_WPD);
        ashaSheets.add(FormConstants.ASHA_CS);
        ashaSheets.add(FormConstants.NCD_ASHA_CBAC);
        ashaSheets.add(FormConstants.ASHA_NPCB);
        ashaSheets.add(FormConstants.ANC_MORBIDITY);
        ashaSheets.add(FormConstants.PNC_MORBIDITY);
        ashaSheets.add(FormConstants.CHILD_CARE_MORBIDITY);
        ashaSheets.add(FormConstants.ASHA_SAM_SCREENING);
        ashaSheets.add(FormConstants.CMAM_FOLLOWUP);
        ashaSheets.add(FormConstants.IDSP_MEMBER);
        ashaSheets.add(FormConstants.IDSP_MEMBER_2);
        ashaSheets.add(FormConstants.DNHDD_NCD_CBAC_AND_NUTRITION);

    }

    private static void groupAwwSheets() {
        awwSheets.clear();
        awwSheets.add(FormConstants.TECHO_AWW_CS);
        awwSheets.add(FormConstants.TECHO_AWW_THR);
        awwSheets.add(FormConstants.TECHO_AWW_DAILY_NUTRITION);
    }

    private static void gpsFeatureEnabledForms() {
        gpsEnabledForms.clear();
        gpsEnabledForms.addAll(ashaSheets);
        gpsEnabledForms.addAll(fhwSheets);
        gpsEnabledForms.addAll(awwSheets);
    }

    private static void includeMorbidityCodeNNamesMap() {
        MORBIDITY_CODE_MAP.clear();
        MORBIDITY_CODE_MAP.put(MorbidityCode.APH, "APH");
        MORBIDITY_CODE_MAP.put(MorbidityCode.SEVERE_ABDOMINAL_PAIN, "Severe abdominal pain");
        MORBIDITY_CODE_MAP.put(MorbidityCode.HEPATITIS, "Hepatitis");
        MORBIDITY_CODE_MAP.put(MorbidityCode.SICKLE_CELL_CRISIS, "Sickle cell crisis");
        MORBIDITY_CODE_MAP.put(MorbidityCode.PREMATURE_RUPTURE_OF_MEMBRANE, "Premature rupture of membrane");
        MORBIDITY_CODE_MAP.put(MorbidityCode.SEVERE_ANEMIA, "Severe anemia");
        MORBIDITY_CODE_MAP.put(MorbidityCode.MALPRESENTATION, "Malpresentation");
        MORBIDITY_CODE_MAP.put(MorbidityCode.DECREASED_FOETAL_MOVEMENT, "Decreased foetal movement");

        //CHILD CARE MORBIDITIES
        MORBIDITY_CODE_MAP.put(MorbidityCode.SEVERE_PNEUMONIA_OR_SERIOUS_BACTERIAL_INFECTION, "Severe pneumonia or serious bacterial infection");
        MORBIDITY_CODE_MAP.put(MorbidityCode.CHRONIC_COUGH_OR_COLD, "Chronic cough or cold");
        MORBIDITY_CODE_MAP.put(MorbidityCode.DIARRHOEA_WITH_SEVERE_DEHYDRATION, "Diarrhoea with severe dehydration");
        MORBIDITY_CODE_MAP.put(MorbidityCode.SEVERE_PERSISTENT_DIARRHOEA, "Severe persistent diarrhoea");
        MORBIDITY_CODE_MAP.put(MorbidityCode.VERY_SEVERE_FEBRILE_ILLNESS, "Very severe febrile illness");
        MORBIDITY_CODE_MAP.put(MorbidityCode.SEVERE_MALNUTRITION, "Severe malnutrition");
        MORBIDITY_CODE_MAP.put(MorbidityCode.SEVERE_ANEMIA_FOR_CHILD, "Severe anemia for child");

        //PNC MORBIDITIES
        MORBIDITY_CODE_MAP.put(MorbidityCode.SEPSIS, "Sepsis");
        MORBIDITY_CODE_MAP.put(MorbidityCode.VERY_LOW_BIRTH_WEIGHT, "Very low birth weight");
        MORBIDITY_CODE_MAP.put(MorbidityCode.DIARRHOEA, "Diarrhoea");
        MORBIDITY_CODE_MAP.put(MorbidityCode.JAUNDICE, "Jaundice");
        MORBIDITY_CODE_MAP.put(MorbidityCode.BLEEDING_FROM_ANY_PART_OF_THE_BODY, "Bleeding from any part of the body(nose, mouth, anus, umbilicus)");

        MORBIDITY_CODE_MAP.put(MorbidityCode.PPH, "PPH");
        MORBIDITY_CODE_MAP.put(MorbidityCode.POST_PARTUM_INFECTION, "Post partum infection");
        MORBIDITY_CODE_MAP.put(MorbidityCode.MASTITIS, "Mastitis");
        MORBIDITY_CODE_MAP.put(MorbidityCode.POST_PARTUM_MOOD_DISORDER, "Post partum mood disorder");
        MORBIDITY_CODE_MAP.put(MorbidityCode.PIH, "PIH");

        //Yellow Morbidities.............................................................................................
        //ANC_MORBIDITIES
        MORBIDITY_CODE_MAP.put(MorbidityCode.BAD_OBSTETRIC_HISTORY, "Bad obstetric history");
        MORBIDITY_CODE_MAP.put(MorbidityCode.UNINTENTED_PREGNANCY, "Unintented pregnancy");
        MORBIDITY_CODE_MAP.put(MorbidityCode.MILD_PREGNANCY_INDUCED_HYPERTENSION, "Mild pregnancy induced hypertension");
        MORBIDITY_CODE_MAP.put(MorbidityCode.MALARIA_IN_PREGNANCY, "Malaria in pregnancy");
        MORBIDITY_CODE_MAP.put(MorbidityCode.FEVER, "Fever");
        MORBIDITY_CODE_MAP.put(MorbidityCode.POSSIBLE_TB, "Possible TB");
        MORBIDITY_CODE_MAP.put(MorbidityCode.URINARY_TRACT_INFECTION, "Urinary tract infection");
        MORBIDITY_CODE_MAP.put(MorbidityCode.VAGINITIS, "Vaginitis");
        MORBIDITY_CODE_MAP.put(MorbidityCode.NIGHT_BLINDNESS, "Night blindness");
        MORBIDITY_CODE_MAP.put(MorbidityCode.PROBABLE_SEVERE_ANEMIA, "Probable severe anemia");
        MORBIDITY_CODE_MAP.put(MorbidityCode.SEVERE_PREGNANCY_INDUCED_HYPERTENSION, "Severe Pregnancy induced hypertension");
        MORBIDITY_CODE_MAP.put(MorbidityCode.FEVER_PNC, "Fever");
        MORBIDITY_CODE_MAP.put(MorbidityCode.LOW_BIRTH_WEIGHT, "Low Birth Weight");
        MORBIDITY_CODE_MAP.put(MorbidityCode.CONJUNCTIVITIS, "Conjunctivitis");

        //CHILD CARE MORBIDITIES
        MORBIDITY_CODE_MAP.put(MorbidityCode.PNEUMONIA_CHILD_CARE, "Pneumonia");
        MORBIDITY_CODE_MAP.put(MorbidityCode.DIARRHOEA_WITH_SOME_DEHYDRATION, "Diarrhoea with some dehydration");
        MORBIDITY_CODE_MAP.put(MorbidityCode.DYSENTERY, "Dysentry");
        MORBIDITY_CODE_MAP.put(MorbidityCode.MALARIA, "Malaria");
        MORBIDITY_CODE_MAP.put(MorbidityCode.VERY_LOW_WEIGHT, "Very low weight");
        MORBIDITY_CODE_MAP.put(MorbidityCode.ANEMIA, "Anemia");

        //PNC MORBIDITIES
        MORBIDITY_CODE_MAP.put(MorbidityCode.HYPOTHERMIA, "Hypothermia");
        MORBIDITY_CODE_MAP.put(MorbidityCode.PNEUMONIA, "Pneumonia");
        MORBIDITY_CODE_MAP.put(MorbidityCode.LOCAL_INFECTION, "Local infection");
        MORBIDITY_CODE_MAP.put(MorbidityCode.HIGH_RISK_LOW_BIRTH_WEIGHT_PRE_TERM, "High risk Low Birth Weight or Pre-term");
        MORBIDITY_CODE_MAP.put(MorbidityCode.BIRTH_ASPHYXIA, "Birth asphyxia");

        //Green Morbidities........................................................
        //ANC_MORBIDITIES
        MORBIDITY_CODE_MAP.put(MorbidityCode.EMESIS_OF_PREGNANCY, "Emesis of pregnancy");
        MORBIDITY_CODE_MAP.put(MorbidityCode.UPPER_RESPIRATORY_TRACT_INFACTION, "Upper respiratory tract infection");
        MORBIDITY_CODE_MAP.put(MorbidityCode.MODERATE_ANEMIA, "Moderate anemia");
        MORBIDITY_CODE_MAP.put(MorbidityCode.BREAST_PROBLEMS, "Breast problems");

        //CHILD CARE MORBIDITIES
        MORBIDITY_CODE_MAP.put(MorbidityCode.COLD_COUGH, "Cold or Cough");
        MORBIDITY_CODE_MAP.put(MorbidityCode.DIARRHOEA_WITH_NO_DEHYDRATION, "Diarrhoea with no dehydration");
        MORBIDITY_CODE_MAP.put(MorbidityCode.NOT_VERY_LOW_WEIGHT, "Not very low weight");
        MORBIDITY_CODE_MAP.put(MorbidityCode.NO_ANEMIA, "No anemia");

        //PNC MORBIDITIES
        MORBIDITY_CODE_MAP.put(MorbidityCode.FEEDING_PROBLEMS, "Feeding problem");
    }

    public static class MorbidityCode {

        private MorbidityCode() {
            throw new IllegalStateException("Utility class");
        }

        public static final String CONJUNCTIVITIS = "CONJUC";
        public static final String LOW_BIRTH_WEIGHT = "BWLPC";
        public static final String FEVER_PNC = "FVRIP";
        public static final String SEVERE_PREGNANCY_INDUCED_HYPERTENSION = "SPIH";
        public static final String APH = "APH";
        public static final String SEVERE_ABDOMINAL_PAIN = "SAP";
        public static final String HEPATITIS = "HEP";
        public static final String SICKLE_CELL_CRISIS = "SCC";
        public static final String PREMATURE_RUPTURE_OF_MEMBRANE = "PRM";
        public static final String SEVERE_ANEMIA = "SEAM";
        public static final String MALPRESENTATION = "MAL";
        public static final String DECREASED_FOETAL_MOVEMENT = "DFM";
        public static final String BAD_OBSTETRIC_HISTORY = "BOH";
        public static final String UNINTENTED_PREGNANCY = "UP";
        public static final String MILD_PREGNANCY_INDUCED_HYPERTENSION = "MPIH";
        public static final String MALARIA_IN_PREGNANCY = "MIP";
        public static final String FEVER = "FEVER";
        public static final String POSSIBLE_TB = "PT";
        public static final String URINARY_TRACT_INFECTION = "UTI";
        public static final String VAGINITIS = "VAGIN";
        public static final String NIGHT_BLINDNESS = "NB";
        public static final String PROBABLE_SEVERE_ANEMIA = "PSA";
        public static final String EMESIS_OF_PREGNANCY = "EOP";
        public static final String UPPER_RESPIRATORY_TRACT_INFACTION = "URTI";
        public static final String MODERATE_ANEMIA = "MDAN";
        public static final String BREAST_PROBLEMS = "BP";
        //PNC MORBIDITY
        public static final String SEPSIS = "SEP";
        public static final String VERY_LOW_BIRTH_WEIGHT = "LBWSH";
        public static final String DIARRHOEA = "DIAH";
        public static final String JAUNDICE = "JAUND";
        public static final String BLEEDING_FROM_ANY_PART_OF_THE_BODY = "BFAP";
        public static final String PPH = "PPH";
        public static final String POST_PARTUM_INFECTION = "PPI";
        public static final String MASTITIS = "MASTI";
        public static final String POST_PARTUM_MOOD_DISORDER = "PPMD";
        public static final String PIH = "PIHH";
        public static final String HYPOTHERMIA = "HYTH";
        public static final String PNEUMONIA = "PNEUM";
        public static final String LOCAL_INFECTION = "LI";
        public static final String HIGH_RISK_LOW_BIRTH_WEIGHT_PRE_TERM = "HRLB";
        public static final String BIRTH_ASPHYXIA = "BASP";
        public static final String FEEDING_PROBLEMS = "FPRO";
        //CHILD MORBIDITY
        public static final String SEVERE_PNEUMONIA_OR_SERIOUS_BACTERIAL_INFECTION = "SPSBI";
        public static final String CHRONIC_COUGH_OR_COLD = "CCC";
        public static final String DIARRHOEA_WITH_SEVERE_DEHYDRATION = "DWSD";
        public static final String SEVERE_PERSISTENT_DIARRHOEA = "SPD";
        public static final String VERY_SEVERE_FEBRILE_ILLNESS = "VSFI";
        public static final String SEVERE_MALNUTRITION = "SMAL";
        public static final String DIARRHOEA_WITH_SOME_DEHYDRATION = "DSD";
        public static final String DYSENTERY = "DYSEN";
        public static final String MALARIA = "MALAR";
        public static final String VERY_LOW_WEIGHT = "VLW";
        public static final String ANEMIA = "ANEMA";
        public static final String COLD_COUGH = "COCOU";
        public static final String DIARRHOEA_WITH_NO_DEHYDRATION = "DWND";
        public static final String NOT_VERY_LOW_WEIGHT = "NVW";
        public static final String NO_ANEMIA = "NANEM";
        public static final String SEVERE_ANEMIA_FOR_CHILD = "SAFC";
        public static final String PNEUMONIA_CHILD_CARE = "PNEUFC";
    }
}
