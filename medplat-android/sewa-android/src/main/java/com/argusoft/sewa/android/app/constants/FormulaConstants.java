package com.argusoft.sewa.android.app.constants;

public class FormulaConstants {

    private FormulaConstants() {
        throw new IllegalStateException("Utility Class");
    }

    // Formula for Validations
    public static final String ALPHA_NUMERIC_WITH_CAPS = "AlphanumericWithCaps";
    public static final String GREATER_THAN_0 = "GreaterThen0";
    public static final String NUMERIC = "Numeric";
    public static final String ONLY_NUMBERS = "onlyNumbers";
    public static final String PERSON_NAME = "personName";
    public static final String MULTI_OPTION_UNCHECK = "multiOptionUncheck";

    //Validation Formula
    public static final String VALIDATION_IS_FUTURE_DATE = "isFutureDate";
    public static final String VALIDATION_IS_PAST_DATE = "PastDate";
    public static final String VALIDATION_IS_NOT_TODAY = "isNotToday";
    public static final String VALIDATION_IS_DATE_IN = "isDateIn";
    public static final String VALIDATION_IS_DATE_OUT = "isDateOut";
    public static final String VALIDATION_CONTAINS_CHARACTER_PIPELINE = "containsCharacterPipeline";
    public static final String VALIDATION_ALPHABETIC_NUMERIC_WITH_SPACE = "AlphanumericWithSpace";
    public static final String VALIDATION_GREATER_THEN_0 = "GreaterThen0";
    public static final String VALIDATION_CHECK_INPUT_LENGTH = "checkInputLength";
    public static final String VALIDATION_CHECK_MOBILE_NUMBER = "mobileNumber";
    public static final String VALIDATION_CHECK_AADHAAR_NUMBER = "aadhaarNumber";
    public static final String VALIDATION_CHECK_INPUT_LENGTH_RANGE = "inputLengthRange";
    public static final String VALIDATION_GREATER_THAN = "GreaterThan";
    public static final String VALIDATION_GREATER_THAN_EQUAL = "GreaterThanEq";
    public static final String VALIDATION_LESS_THAN = "LessThan";
    public static final String VALIDATION_LESS_THAN_EQUAL = "LessThanEq";
    public static final String VALIDATION_LESS_THAN_EQUAL_RELATED_PROPERTY = "lessThanEqRelatedProperty";
    public static final String VALIDATION_IN_BETWEEN_RANGE = "Between";
    public static final String VALIDATION_COMPARE_DATE_WITH_GIVEN_DATE = "comapreDateWithGivenDate";
    public static final String VALIDATION_COMPARE_DATE_WITH_GIVEN_DATE_AFTER = "ComapreDateWithGivenDateAfter";
    public static final String VALIDATION_MAX_LENGTH = "maxLength";
    public static final String VALIDATION_LENGTH = "length";
    public static final String VALIDATION_ONLY_ONE_HEAD = "onlyOneHead";
    public static final String VALIDATION_CHECK_HEAD_MEMBER_AGE = "checkHeadMemberAge";
    public static final String VALIDATION_MARITAL_STATUS = "maritalStatusValidation";
    public static final String VALIDATION_CHECK_VILLAGE_SELECTION = "villageSelectionCheck";
    public static final String VALIDATION_MOTHER_CHILD_COMPONENT = "motherChildComponentValidation";
    public static final String VALIDATION_CHILD_VACCINATION = "vaccinationValidationChild";
    public static final String VALIDATION_CHECK_NUMBER_FORMAT = "checkNumberFormatException";
    public static final String VALIDATION_CHECK_SINGLE_E_C_MEMBER_FAMILY = "checkSingleECMemberFamily";
    public static final String VALIDATION_CHECK_2_DAYS_GAP_DELIVERY_DATE = "checkDaysGapDeliveryDate";
    public static final String VALIDATION_CHECK_SERVICE_DATE_FOR_HEALTH_INFRA = "checkServiceDateForHealthInfra";
    public static final String VALIDATION_CHECK_SERVICE_DATE_FOR_HOME_VISIT = "checkServiceDateForHomeVisit";
    public static final String VALIDATION_CHECK_AGE_FOR_NOT_UNMARRIED = "checkAgeForNotUnmarried";
    public static final String VALIDATION_INTEGER_ONLY_PLUS = "integerOnlyPlus";
    public static final String VALIDATION_FAMILY_PLANNING_DATE = "familyPlanningDateValidation";
    public static final String VALIDATION_CHECK_SERVICE_DATE_FOR_SCHOOL = "checkServiceDateForSchool";
    public static final String VALIDATION_COMPARE_DATE_BEFORE = "comapreDateBefore";
    public static final String VALIDATION_COMPARE_DATE_AFTER = "ComapreDateAfter";
    public static final String VALIDATION_CHECK_FOR_GIVEN_SACHETS = "checkGivenSachets";
    public static final String VALIDATION_CHECK_FOR_CONSUMED_SACHETS = "checkConsumedSachets";
    public static final String VALIDATION_CHECK_HOF_RELATION_OR_GENDER_WITH_EACH_OTHER = "checkHofRelationWithGender";
    public static final String VALIDATION_CHECK_HOF_RELATION_MARITAL_STATUS_WITH_EACH_OTHER = "checkHofRelationWithMaritalStatus";
    public static final String VALIDATION_CHECK_CHARDHAM_IS_PREGNANT_REL_WITH_GENDER = "checkIsPregnantRelWithGender";
    public static final String VALIDATION_MEDICINE_DETAIL = "medicineDetailValidation";
    public static final String VALIDATION_WEDDING_DATE = "weddingDateValidation";
    public static final String VALIDATION_DIABETES_CONFIRMATION = "diabetesConfirmationValidation";

    //Formula Hidden Questions
    public static final String FORMULA_IS_DATE_IN = "isdatein"; // isDateIn
    public static final String FORMULA_IS_DATE_OUT = "isdateout"; // isDateOut
    public static final String FORMULA_IS_DATE_BETWEEN = "isdatebetween"; // isDateBetween
    public static final String FORMULA_CHECK_PRETERM_BIRTH = "checkpretermbirth"; // checkPretermBirth
    public static final String FORMULA_MEMBERS_COUNT_CHECK = "memberscountcheck"; // membersCountCheck
    public static final String FORMULA_LOOP_CONTAINS = "loopcontains"; // loopContains
    public static final String FORMULA_LOOP_CHECK_AGE_BETWEEN = "loopcheckagebetween"; // loopCheckAgeBetween
    public static final String FORMULA_CHECK_DATE_BETWEEN = "checkdatebetween"; // checkDateBetween
    public static final String FORMULA_SET_AREA_QUESTION_VISIBILITY = "setareaquestionvisibility"; // setAreaQuestionVisibility
    public static final String FORMULA_CHECK_FAMILY_REVERIFICATION = "checkfamilyreverification"; // checkFamilyReverification
    public static final String FORMULA_IS_DATE_WITHIN = "isdatewithin"; // isDateWithin
    public static final String FORMULA_IS_DATE_OUTSIDE = "isdateoutside"; // isDateOutside
    public static final String FORMULA_IDENTIFY_HIGH_RISKS_ANC = "identifyhighriskanc"; // identifyHighRiskAnc
    public static final String FORMULA_IDENTIFY_HIGH_RISKS_CHARDHAM_TOURIST = "identifyhighriskchardhamtourist"; // identifyHighRiskAnc
    public static final String FORMULA_IDENTIFY_HIGH_RISKS_PNC_CHILD = "identifyhighriskpncchild"; // identifyHighRiskPncChild
    public static final String FORMULA_IDENTIFY_HIGH_RISKS_PNC_MOTHER = "identifyhighriskpncmother"; // identifyHighRiskPncMother
    public static final String FORMULA_IDENTIFY_HIGH_RISKS_WPD_CHILD = "identifyhighriskwpdchild"; // identifyHighRiskWpdChild
    public static final String FORMULA_CHECK_CONTAINS_LOOP = "checkcontainsloop"; // checkContainsLoop
    public static final String FORMULA_CHECK_IF_LMP_AVAILABLE = "checkiflmpisavailable"; // checkIfLmpIsAvailable
    public static final String FORMULA_PUT_REMAINING_VACCINES = "putremainingvaccines"; // putRemainingVaccines
    public static final String FORMULA_CHECK_IF_THIRD_TRIMESTER = "checkifthirdtrimester"; // checkIfThirdTrimester
    public static final String FORMULA_SET_PROPERTY_ADD_ALL_OPTION_IN_SUBCENTRE_LIST = "addalloptioninsubcentrelist"; // addAllOptionInSubcentreList
    public static final String FORMULA_CHECK_AGE_IF_WRONGLY_REGISTERED_AS_PREGNANT = "checkageifwronglyregistered"; // checkAgeIfWronglyRegistered
    public static final String FORMULA_CHECK_AGE_LESS_THAN_20 = "checkagelessthan20"; // checkAgeLessThan20
    public static final String FORMULA_CHECK_IF_ANY_CHILD_EXISIS = "checkifanychildexisits"; // checkIfAnyChildExisits
    public static final String FORMULA_CHECK_IF_ANY_FEMALE_MARRIED_MEMBER_EXISIS = "checkifanyfemalemarriedmembersexists"; // checkIfAnyFemaleMarriedMembersExists
    public static final String FORMULA_CHECK_FAMILY_STATE = "checkfamilystate"; // checkFamilyState
    public static final String FORMULA_CHECK_NEW_FAMILY = "checknewfamily"; // checkNewFamily
    public static final String FORMULA_CHECK_MEMBER_STATE = "checkmemberstate"; // checkMemberState
    public static final String FORMULA_CONTACT_GYNECOLOGIST = "contactgynecologist"; // contactGynecologist
    public static final String FORMULA_ADD_MOTHER_LIST = "addmotherlist"; // addMotherList
    public static final String FORMULA_CALCULATE_CBAC_SCORE = "calculatecbacscore"; // calculateCbacScore
    public static final String FORMULA_CHECK_MISOPROSTOL = "checkmisoprostol"; // checkMisoprostol
    public static final String FORMULA_RESET_PROPERTY = "resetproperty"; // resetProperty
    public static final String FORMULA_CHECK_CONTAINS_MULTIPLE = "checkcontainsmultiple"; // checkContainsMultiple
    public static final String FORMULA_CHECK_CHIRANJEEVI_ELIGIBILITY = "checkchiranjeevieligibility"; // checkChiranjeeviEligibility
    public static final String FORMULA_ADD_ADDITIONAL_DISCHARGE_QUESTION = "addadditiondischargequestion"; // addAdditionDischargeQuestion
    public static final String FORMULA_CALCULATE_SD_SCORE = "calculatesdscore"; // calculateSDScore
    public static final String FORMULA_SHOW_ALERT_IF_YES_IN_ANY = "showalertifyesinany"; // showAlertIfYesInAny
    public static final String FORMULA_SET_CHILD_GROWTH_CHART_DATA = "setchildgrowthchartdata"; // setChildGrowthChartData
    public static final String FORMULA_IS_FAMILY_HEAD_IDENTIFIED = "isfamilyheadidentified"; // isFamilyHeadIdentified
    public static final String FORMULA_IS_USER_ONLINE = "isuseronline"; // isUserOnline
    public static final String FORMULA_IS_MOBILE_NUMBER_VERFICATION_BLOCKED = "ismobilenumberverificationblocked"; // isMobileNumberVerificationBlocked
    public static final String FORMULA_IS_MOBILE_NUMBER_VERFIED = "ismobilenumberverfied"; // isMobileNumberVerfied
    public static final String FORMULA_SET_OTP_BASED_VERIFICATION_COMPONENT = "setotpbasedverificationcomponent"; // setOTPBasedVerificationComponent
    public static final String FORMULA_SET_CHECK_ANY_MEMBER_OTP_VERIFICATION_DONE = "checkanymemberotpverificationdone"; // checkAnyMemberOtpVerificationDone
    public static final String FORMULA_APPETITE_TEST_ELIGIBILITY_CHECK = "appetitetesteligibilitycheck"; // appetiteTestEligibilityCheck
    public static final String FORMULA_IS_CMAM_FOLLOWUPS_PROBLEM = "iscmamfollowupsproblem"; // isCMAMFollowupsProblem
    public static final String FORMULA_CALCULATE_TO_BE_GIVEN_SACHETS = "calculaterutfsachets"; // calculateRUTFSachets
    public static final String FORMULA_CHECK_CONTAINS_ANY_LOOP = "checkcontainsanyloop"; // checkContainsAnyLoop
    public static final String FORMULA_CHECK_FOR_URINE_TEST = "urinetest"; // UrineTest
    public static final String FORMULA_CHECK_CONTAINS = "checkcontains"; // checkContains
    public static final String FORMULA_PNC_CHECK_TEMP = "pncchecktemp"; // pncCheckTemp
    public static final String FORMULA_CHECK_LESS_THAN = "lessthan"; // lessThan
    public static final String FORMULA_CHECK_LENGTH = "checklength"; // checkLength
    public static final String FORMULA_SET_REVERSE_FLAG = "setreverseflag"; // setReverseFlag
    public static final String FORMULA_COMPARE_DISEASE_COUNT = "comparediseasecount"; // CompareDiseaseCount
    public static final String FORMULA_CHECK_LOOP = "checkloop"; // CheckLoop
    public static final String FORMULA_CHECK_LOOP_BAK_COUNT = "checkloopbakcount"; // CheckLoopBakCount
    public static final String FORMULA_COMPARE_DATE = "comparedate"; // CompareDate
    public static final String FORMULA_SET_TRUE = "settrue"; // setTrue
    public static final String FORMULA_SET_MOBIDITY_LIST = "setmobiditylist";
    public static final String FORMULA_SET_ANSWER = "setanswer"; // setAnswer
    public static final String FORMULA_CONTAINS = "contains";
    public static final String FORMULA_DATE_BETWEEN = "date_between";
    public static final String FORMULA_CHECK_PROPERTY = "checkproperty"; // CheckProperty
    public static final String FORMULA_DISPLAY_AGE = "displayage"; // displayAge
    public static final String FORMULA_CHECK_STRING = "checkstring"; // checkString
    public static final String FORMULA_CHECK_WEIGHT = "checkweight"; // checkWeight
    public static final String FORMULA_IS_NULL = "isnull"; // isNull
    public static final String FORMULA_UPDATE_CURRENT_GRAVIDA = "updatecurrentgravida"; // updateCurrentGravida
    public static final String FORMULA_SET_SCHOOL_TYPE = "setschooltype"; // setSchoolType
    public static final String FORMULA_CHECK_IS_ANY_VACCINE_GIVEN = "isanyvaccinegiven"; // isAnyVaccineGiven
    public static final String FORMULA_CHECK_IF_CONTAIN_OTHER = "checkifcontainother"; // checkIfContainOther
    public static final String FORMULA_MEDICINE_COUNT_CHECK = "medicinecountcheck"; // medicineCountCheck

    // Formulas
    public static final String FORMULA_SET_PROPERTY = "setproperty"; // setProperty
    public static final String FORMULA_SET_DEFAULT_PROPERTY = "setdefaultproperty";  // setDefaultProperty
    public static final String FORMULA_ADD_PERIOD = "addperiod"; // addPeriod
    public static final String FORMULA_CALCULATE_OUT_COME_DATE = "calculateoutcomedate"; // calculateOutComeDate
    public static final String FORMULA_SET_SUBTITLE_COLOR = "setsubtitlecolor"; // setSubtitleColor
    public static final String FORMULA_SET_COLOR = "setcolor"; // setColor
    public static final String FORMULA_SET_DATE_SET = "setdateset"; // setDateSet
    public static final String FORMULA_SET_HINT = "sethint"; // setHint
    public static final String FORMULA_SET_VALUE_AS_PROPERTY = "setvalueasproperty"; // setValueAsProperty
    public static final String FORMULA_GET_TIME = "gettime"; // getTime
    public static final String FORMULA_FILL_LOCATION = "filllocation"; // fillLocation
    public static final String FORMULA_SET_PROPERTY_CLEAR_SCANNED_AADHAR_DETAILS = "clearscannedaadhardetails"; // clearScannedAadharDetails
    public static final String FORMULA_SET_IMMUNISATION_WPD = "setimmunisationwpd";
    public static final String FORMULA_SET_PROPERTY_FROM_SCANNED_AADHAR = "setpropertyfromscannedaadhar";
    public static final String FORMULA_SET_PROPERTY_AANGANWADI_ID_FROM_PHC_ID = "setaanganwadiidfromphcid";
    public static final String FORMULA_DISPLAY_EARLY_REGISTRATION = "displayearlyregistration";
    public static final String FORMULA_RESET_LOOP_PARAMS = "resetloopparams";
    public static final String FORMULA_UPDATE_LOOP_COUNT = "updateloopcount";
    public static final String FORMULA_SET_PRE_FILLED_AS_PER_RELATION_WITH_HOF = "setprefilledasperrelationwithhof";

    // This formula is used in BMI Component
    public static final String FORMULA_SET_BMI_HEIGHT_WEIGHT = "setbmiheightweight";

    // This formula is used for ncd hypertension
    public static final String FORMULA_TO_CHECK_BP = "checkbpvalue";

    // This formula is used for ncd hypertension
    public static final String FORMULA_SET_HYPERTENSION_STATUS = "sethypertensionstatus";

    // This formula is used for ncd diabetes
    public static final String FORMULA_SET_DIABETES_STATUS = "setdiabetesstatus";

    // This formula is used for ncd mental health
    public static final String FORMULA_SET_MENTAL_HEALTH_STATUS = "setmentalhealthstatus";
    public static final String FORMULA_CALCULATE_MENTAL_HEALTH_STATUS = "calculatementalhealthstatus";

    // Formula for Ncd Medicine Details
    public static final String FORMULA_SET_MEDICINE_ID = "setmedicineid"; //setMedicineId

    // This formula is used in School Component for setting the standard for the child to get school according to school type
    public static final String FORMULA_SET_STANDARD = "setstandard";

    //Formula used for getting options in question
    public static final String FORMULA_SET_DEFAULT_MIDDLE_NAMES_CBDS = "setDefaultMiddleNames";
    public static final String FORMULA_SET_DEFAULT_MEMBERS_UNDER_20_MS = "setDefaultMembersUnder20";
    public static final String FORMULA_SET_GIVEN_VACCINES_TO_CHILD = "setGivenVaccinesToChild";
    public static final String FORMULA_SET_MEMBERS_FROM_3_TO_6_YEARS = "setMembersFrom3To6Years";
    public static final String FORMULA_SET_BASIC_MEDICINE_LIST = "setBasicMedicineList";
    public static final String FORMULA_SET_GENDER_BASED_ON_RELATION_WITH_HOF = "setgenderbasedonrelationwithhof"; // setGenderBasedOnRlationWithHOF

    public static final String FORMULA_SET_DEFAULT_PROPERTY_LOOP = "setdefaultpropertyloop"; // setDefaultPropertyLoop

}
