package com.argusoft.sewa.android.app.util;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyArrayAdapter;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.FormulaConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.NotificationConstants;
import com.argusoft.sewa.android.app.constants.RchConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.databean.ChardhamSupportUserBean;
import com.argusoft.sewa.android.app.databean.ChardhamTouristScreeningDataBean;
import com.argusoft.sewa.android.app.databean.ChardhamTouristScreeningDto;
import com.argusoft.sewa.android.app.databean.ChardhamTouristsDataBean;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.FieldValueMobDataBean;
import com.argusoft.sewa.android.app.databean.FormulaTagBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.OptionDataBean;
import com.argusoft.sewa.android.app.databean.OptionTagBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LabelBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.morbidities.constants.MorbiditiesConstant;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.Weeks;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author kelvin
 */
public class UtilBean {

    private UtilBean() {
        throw new IllegalStateException("Utility Class");
    }

    public static MyAlertDialog dialogForExit;

    // Morbidity UtilBean is moved here.
    public static final long MINUTE_LONG_VALUE = (60 * 1000L);
    public static final long HOUR_LONG_VALUE = (MINUTE_LONG_VALUE * 60);
    public static final long DAY_LONG_VALUE = (HOUR_LONG_VALUE * 24);
    public static final long YEAR_LONG_VALUE = (DAY_LONG_VALUE * 365);

    public static final JsonSerializer<Date> JSON_DATE_SERIALIZER = (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.getTime());

    private static final Map<String, Integer> VACCINATION_SORT_MAP = new HashMap<>();

    static {
        VACCINATION_SORT_MAP.put(RchConstants.HEPATITIS_B_0, 1);
        VACCINATION_SORT_MAP.put(RchConstants.VITAMIN_K, 2);
        VACCINATION_SORT_MAP.put(RchConstants.BCG, 3);
        VACCINATION_SORT_MAP.put(RchConstants.OPV_0, 4);
        VACCINATION_SORT_MAP.put(RchConstants.OPV_1, 5);
        VACCINATION_SORT_MAP.put(RchConstants.ROTA_VIRUS_1, 6);
        VACCINATION_SORT_MAP.put(RchConstants.PENTA_1, 7);
        VACCINATION_SORT_MAP.put(RchConstants.DPT_1, 8);
        VACCINATION_SORT_MAP.put(RchConstants.F_IPV_1_01, 9);
        VACCINATION_SORT_MAP.put(RchConstants.OPV_2, 10);
        VACCINATION_SORT_MAP.put(RchConstants.ROTA_VIRUS_2, 11);
        VACCINATION_SORT_MAP.put(RchConstants.PENTA_2, 12);
        VACCINATION_SORT_MAP.put(RchConstants.DPT_2, 13);
        VACCINATION_SORT_MAP.put(RchConstants.OPV_3, 14);
        VACCINATION_SORT_MAP.put(RchConstants.ROTA_VIRUS_3, 15);
        VACCINATION_SORT_MAP.put(RchConstants.PENTA_3, 16);
        VACCINATION_SORT_MAP.put(RchConstants.DPT_3, 17);
        VACCINATION_SORT_MAP.put(RchConstants.F_IPV_2_01, 18);
        VACCINATION_SORT_MAP.put(RchConstants.F_IPV_2_05, 19);
        VACCINATION_SORT_MAP.put(RchConstants.MEASLES_RUBELLA_1, 20);
        VACCINATION_SORT_MAP.put(RchConstants.MEASLES_RUBELLA_2, 21);
        VACCINATION_SORT_MAP.put(RchConstants.OPV_BOOSTER, 22);
        VACCINATION_SORT_MAP.put(RchConstants.DPT_BOOSTER, 23);
        VACCINATION_SORT_MAP.put(RchConstants.VITAMIN_A, 24);
    }

    public static final Comparator<String> VACCINATION_COMPARATOR = (o1, o2) -> {
        Integer one = VACCINATION_SORT_MAP.get(o1);
        Integer two = VACCINATION_SORT_MAP.get(o2);
        if (one != null && two != null) {
            return one.compareTo(two);
        }
        return 0;
    };

    private static Map<String, String> malnutritionGradeMapBoy;
    private static Map<String, String> malnutritionGradeMapGirl;
    private static Map<String, String> dosageHashTable;
    private static Map<String, String> entityFullFormNames;

    public static boolean isNotInGivenRange(int givenValue, int lowerBoundary, int upperBoundary) {
        return givenValue > lowerBoundary && givenValue < upperBoundary;
    }

    public static Map<String, String> getMalnutritionGradeMapForBOYS() {
        if (malnutritionGradeMapBoy == null) {
            malnutritionGradeMapBoy = new HashMap<>();
            malnutritionGradeMapBoy.put("0", "2.1~2.5");
            malnutritionGradeMapBoy.put("1", "3.0~3.5");
            malnutritionGradeMapBoy.put("2", "3.8~4.3");
            malnutritionGradeMapBoy.put("3", "4.5~5.0");
            malnutritionGradeMapBoy.put("4", "4.9~5.6");
            malnutritionGradeMapBoy.put("5", "5.3~6.0");
            malnutritionGradeMapBoy.put("6", "5.7~6.4");
            malnutritionGradeMapBoy.put("7", "5.9~6.7");
            malnutritionGradeMapBoy.put("8", "6.2~6.9");
            malnutritionGradeMapBoy.put("9", "6.4~7.1");
            malnutritionGradeMapBoy.put("10", "6.6~7.3");
            malnutritionGradeMapBoy.put("11", "6.8~7.5");
            malnutritionGradeMapBoy.put("12", "6.9~7.7");
            malnutritionGradeMapBoy.put("13", "7.1~7.9");
            malnutritionGradeMapBoy.put("14", "7.2~8.1");
            malnutritionGradeMapBoy.put("15", "7.4~8.2");
            malnutritionGradeMapBoy.put("16", "7.5~8.4");
            malnutritionGradeMapBoy.put("17", "7.6~8.6");
            malnutritionGradeMapBoy.put("18", "7.8~8.8");
            malnutritionGradeMapBoy.put("19", "8.0~8.9");
            malnutritionGradeMapBoy.put("20", "8.1~9.1");
            malnutritionGradeMapBoy.put("21", "8.3~9.2");
            malnutritionGradeMapBoy.put("22", "8.3~9.4");
            malnutritionGradeMapBoy.put("23", "8.4~9.5");
            malnutritionGradeMapBoy.put("24", "8.5~9.7");
            malnutritionGradeMapBoy.put("25", "8.6~9.8");
            malnutritionGradeMapBoy.put("26", "8.8~10.0");
            malnutritionGradeMapBoy.put("27", "9.0~10.1");
            malnutritionGradeMapBoy.put("28", "9.1~10.2");
            malnutritionGradeMapBoy.put("29", "9.2~10.3");
            malnutritionGradeMapBoy.put("30", "9.3~10.5");
            malnutritionGradeMapBoy.put("31", "9.4~10.6");
            malnutritionGradeMapBoy.put("32", "9.5~10.7");
            malnutritionGradeMapBoy.put("33", "9.7~10.9");
            malnutritionGradeMapBoy.put("34", "9.8~11.0");
            malnutritionGradeMapBoy.put("35", "9.9~11.1");
            malnutritionGradeMapBoy.put("36", "10.0~11.3");
            malnutritionGradeMapBoy.put("37", "10.1~11.4");
            malnutritionGradeMapBoy.put("38", "10.2~11.5");
            malnutritionGradeMapBoy.put("39", "10.3~11.6");
            malnutritionGradeMapBoy.put("40", "10.4~11.7");
            malnutritionGradeMapBoy.put("41", "10.5~11.9");
            malnutritionGradeMapBoy.put("42", "10.6~12.0");
            malnutritionGradeMapBoy.put("43", "10.7~12.1");
            malnutritionGradeMapBoy.put("44", "10.8~12.2");
            malnutritionGradeMapBoy.put("45", "10.9~12.3");
            malnutritionGradeMapBoy.put("46", "11.0~12.5");
            malnutritionGradeMapBoy.put("47", "11.1~12.6");
            malnutritionGradeMapBoy.put("48", "11.2~12.7");
            malnutritionGradeMapBoy.put("49", "11.3~12.8");
            malnutritionGradeMapBoy.put("50", "11.4~12.9");
            malnutritionGradeMapBoy.put("51", "11.5~13.0");
            malnutritionGradeMapBoy.put("52", "11.6~13.1");
            malnutritionGradeMapBoy.put("53", "11.7~13.3");
            malnutritionGradeMapBoy.put("54", "11.8~13.4");
            malnutritionGradeMapBoy.put("55", "11.9~13.5");
            malnutritionGradeMapBoy.put("56", "12.0~13.6");
            malnutritionGradeMapBoy.put("57", "12.1~13.7");
            malnutritionGradeMapBoy.put("58", "12.2~13.8");
            malnutritionGradeMapBoy.put("59", "12.3~13.9");
            malnutritionGradeMapBoy.put("60", "12.4~14.1");
        }
        return malnutritionGradeMapBoy;
    }

    public static Map<String, String> getMalnutritionGradeMapForGIRLS() {
        if (malnutritionGradeMapGirl == null) {
            malnutritionGradeMapGirl = new HashMap<>();
            malnutritionGradeMapGirl.put("0", "2.0~2.4");
            malnutritionGradeMapGirl.put("1", "2.7~3.1");
            malnutritionGradeMapGirl.put("2", "3.4~3.9");
            malnutritionGradeMapGirl.put("3", "4.0~4.5");
            malnutritionGradeMapGirl.put("4", "4.4~5.0");
            malnutritionGradeMapGirl.put("5", "4.8~5.4");
            malnutritionGradeMapGirl.put("6", "5.1~5.7");
            malnutritionGradeMapGirl.put("7", "5.3~6.0");
            malnutritionGradeMapGirl.put("8", "5.6~6.4");
            malnutritionGradeMapGirl.put("9", "5.8~6.5");
            malnutritionGradeMapGirl.put("10", "5.9~6.7");
            malnutritionGradeMapGirl.put("11", "6.1~6.9");
            malnutritionGradeMapGirl.put("12", "6.2~7.0");
            malnutritionGradeMapGirl.put("13", "6.4~7.2");
            malnutritionGradeMapGirl.put("14", "6.5~7.4");
            malnutritionGradeMapGirl.put("15", "6.7~7.6");
            malnutritionGradeMapGirl.put("16", "6.8~7.8");
            malnutritionGradeMapGirl.put("17", "7.0~7.9");
            malnutritionGradeMapGirl.put("18", "7.2~8.1");
            malnutritionGradeMapGirl.put("19", "7.3~8.2");
            malnutritionGradeMapGirl.put("20", "7.5~8.4");
            malnutritionGradeMapGirl.put("21", "7.6~8.6");
            malnutritionGradeMapGirl.put("22", "7.7~8.7");
            malnutritionGradeMapGirl.put("23", "7.9~8.9");
            malnutritionGradeMapGirl.put("24", "8.0~9.0");
            malnutritionGradeMapGirl.put("25", "8.2~9.2");
            malnutritionGradeMapGirl.put("26", "8.3~9.4");
            malnutritionGradeMapGirl.put("27", "8.5~9.5");
            malnutritionGradeMapGirl.put("28", "8.6~9.7");
            malnutritionGradeMapGirl.put("29", "8.8~9.8");
            malnutritionGradeMapGirl.put("30", "8.9~10.0");
            malnutritionGradeMapGirl.put("31", "9.0~10.1");
            malnutritionGradeMapGirl.put("32", "9.2~10.3");
            malnutritionGradeMapGirl.put("33", "9.3~10.4");
            malnutritionGradeMapGirl.put("34", "9.4~10.5");
            malnutritionGradeMapGirl.put("35", "9.5~10.7");
            malnutritionGradeMapGirl.put("36", "9.6~10.8");
            malnutritionGradeMapGirl.put("37", "9.7~11.0");
            malnutritionGradeMapGirl.put("38", "9.8~11.1");
            malnutritionGradeMapGirl.put("39", "9.9~11.2");
            malnutritionGradeMapGirl.put("40", "10.0~11.4");
            malnutritionGradeMapGirl.put("41", "10.2~11.5");
            malnutritionGradeMapGirl.put("42", "10.3~11.6");
            malnutritionGradeMapGirl.put("43", "10.4~11.8");
            malnutritionGradeMapGirl.put("44", "10.5~11.9");
            malnutritionGradeMapGirl.put("45", "10.6~12.0");
            malnutritionGradeMapGirl.put("46", "10.7~12.1");
            malnutritionGradeMapGirl.put("47", "10.8~12.2");
            malnutritionGradeMapGirl.put("48", "10.9~12.4");
            malnutritionGradeMapGirl.put("49", "11.0~12.5");
            malnutritionGradeMapGirl.put("50", "11.1~12.6");
            malnutritionGradeMapGirl.put("51", "11.2~12.7");
            malnutritionGradeMapGirl.put("52", "11.3~12.8");
            malnutritionGradeMapGirl.put("53", "11.4~12.9");
            malnutritionGradeMapGirl.put("54", "11.5~13.1");
            malnutritionGradeMapGirl.put("55", "11.6~13.2");
            malnutritionGradeMapGirl.put("56", "11.7~13.3");
            malnutritionGradeMapGirl.put("57", "11.8~13.4");
            malnutritionGradeMapGirl.put("58", "11.9~13.5");
            malnutritionGradeMapGirl.put("59", "12.0~13.1");
            malnutritionGradeMapGirl.put("60", "12.1~13.2");
        }
        return malnutritionGradeMapGirl;
    }

    public static String findMalnutritionGrade(String gender, int age, float weight) {
        String malnutritionGrade = GlobalTypes.LOWER_MALNUTRITION_GRADE;

        float lowerBoundaryOfWeight = 0f;
        float upperBoundaryOfWeight = 0f;

        if (gender.equalsIgnoreCase(GlobalTypes.MALE)) {
            String boundaryString = getMalnutritionGradeMapForBOYS().get(String.valueOf(age));
            if (boundaryString != null) {
                int indexOfSeparator = boundaryString.indexOf('~');
                lowerBoundaryOfWeight = Float.parseFloat(boundaryString.substring(0, indexOfSeparator));
                upperBoundaryOfWeight = Float.parseFloat(boundaryString.substring(indexOfSeparator + 1));
            }
        } else {
            String boundaryString = getMalnutritionGradeMapForGIRLS().get(String.valueOf(age));
            if (boundaryString != null) {
                int indexOfSeparator = boundaryString.indexOf('~');
                lowerBoundaryOfWeight = Float.parseFloat(boundaryString.substring(0, indexOfSeparator));
                upperBoundaryOfWeight = Float.parseFloat(boundaryString.substring(indexOfSeparator + 1));
            }
        }
        if (weight >= lowerBoundaryOfWeight) {
            if (weight <= upperBoundaryOfWeight) {
                malnutritionGrade = GlobalTypes.MIDDLE_MALNUTRITION_GRADE;
            } else {
                malnutritionGrade = GlobalTypes.UPPER_MALNUTRITION_GRADE;
            }
        }
        return malnutritionGrade;
    }

    /* For displaying weight on diagnosis screen during PNC visit */
    public static String checkWeightForPNCMorbidity(String question, String weight, String loop) {
        if (weight != null && !weight.equals("") && !weight.equals(GlobalTypes.NO_WEIGHT)) {
            if (Float.parseFloat(weight) > 0.0 && Float.parseFloat(weight) < 1.5) {
                return weight;//
            } else {
                /* Consider if no other morbidity of child is detect during this visit*/
                String tmpDataObj = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.IS_CHILD_FIRST_PNC_DONE);
                if (tmpDataObj != null && tmpDataObj.equalsIgnoreCase("false")) {
                    Float newBornWeight = null;
                    String cryStatus;
                    if (Integer.parseInt(loop) == 0) {
                        String prevWeight = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.CHILD_LAST_WEIGHT);
                        if (prevWeight != null) {
                            newBornWeight = Float.parseFloat(prevWeight);
                        }
                        cryStatus = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.WHEN_DID_BABY_CRY);
                    } else {
                        cryStatus = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.WHEN_DID_BABY_CRY + loop);
                        String prevWeight = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.CHILD_LAST_WEIGHT + loop);
                        if (prevWeight != null) {
                            newBornWeight = Float.parseFloat(prevWeight);
                        }
                    }
                    if ((newBornWeight != null && newBornWeight >= 2.0 && newBornWeight < 2.5)
                            || (cryStatus != null && cryStatus.equalsIgnoreCase(MorbiditiesConstant.CRY_AFTER_EFFORTS))) {
                        return null;
                    }
                }
                if (SharedStructureData.isEmptyMapAllMorbidities(question, loop)) {
                    return pncWeightCheck(weight, loop);
                }
            }

        }
        return null;
    }

    public static String pncWeightCheck(String weight, String loop) {
        String age;
        Boolean includeWeight = Boolean.FALSE;

        if (Integer.parseInt(loop) == 0) {
            age = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.CHILD_DOB);
        } else {
            age = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.CHILD_DOB + loop);
        }

        if (age != null) {
            String[] split = UtilBean.split(age, GlobalTypes.KEY_VALUE_SEPARATOR);
            Long ageOfChild = null;

            if (split.length == 3 && split[0] != null && !split[0].trim().equalsIgnoreCase("") && split[1] != null && !split[1].trim().equalsIgnoreCase("") && split[2] != null && !split[2].trim().equalsIgnoreCase("")) {
                ageOfChild = UtilBean.getMilliSeconds(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            }
            if (Float.parseFloat(weight) >= 1.5 && Float.parseFloat(weight) < 1.999 && ageOfChild != null && ageOfChild <= getMilliSeconds(0, 0, 15)) {
                includeWeight = Boolean.TRUE;
            }
            if (Float.parseFloat(weight) > 0.0 && Float.parseFloat(weight) < 2.1 && ageOfChild != null && ageOfChild >= getMilliSeconds(0, 0, 15) && ageOfChild <= getMilliSeconds(0, 0, 21)) {
                includeWeight = Boolean.TRUE;
            }
            if (Float.parseFloat(weight) > 0.0 && Float.parseFloat(weight) < 2.2 && ageOfChild != null && ageOfChild >= getMilliSeconds(0, 0, 22) && ageOfChild <= getMilliSeconds(0, 0, 27)) {
                includeWeight = Boolean.TRUE;
            }
            if (Float.parseFloat(weight) > 0.0 && Float.parseFloat(weight) < 2.3 && ageOfChild != null && ageOfChild > getMilliSeconds(0, 0, 27)) { /* irrespective of year n month */
                includeWeight = Boolean.TRUE;
            }
        }
        if (Boolean.TRUE.equals(includeWeight)) {
            return weight;
        }
        return null;
    }

    public static void addLBWorAsphyxiaIntoLICForFirstPNC(String loop) {
        String tempDataObj = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.IS_CHILD_FIRST_PNC_DONE);
        if (tempDataObj != null && tempDataObj.equalsIgnoreCase("false")) {
            Float newBornWeight = null;
            String cryStatus;
            if (Integer.parseInt(loop) == 0) {
                String prevWeight = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.CHILD_LAST_WEIGHT);
                if (prevWeight != null) {
                    newBornWeight = Float.parseFloat(prevWeight);
                }
                cryStatus = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.WHEN_DID_BABY_CRY);
            } else {
                cryStatus = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.WHEN_DID_BABY_CRY + loop);
                String prevWeight = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.CHILD_LAST_WEIGHT + loop);
                if (prevWeight != null) {
                    newBornWeight = Float.parseFloat(prevWeight);
                }
            }
            if ((newBornWeight != null && newBornWeight >= 2.0 && newBornWeight < 2.5)
                    || (cryStatus != null && cryStatus.equalsIgnoreCase(MorbiditiesConstant.CRY_AFTER_EFFORTS))) {
                if (newBornWeight != null && newBornWeight >= 2.0 && newBornWeight < 2.5) {
                    SharedStructureData.addItemInLICList("Birth weight:", newBornWeight.toString(), loop);
                }
                if (cryStatus != null && cryStatus.equalsIgnoreCase(MorbiditiesConstant.CRY_AFTER_EFFORTS)) {
                    SharedStructureData.addItemInLICList("When did the baby cry?", MorbiditiesConstant.getStaticValueAndKeyMap().get(MorbiditiesConstant.CRY_AFTER_EFFORTS), loop);
                }
            }
        }
    }

    public static List<String> getListFromStringBySeparator(String value, String separator) {
        if (value != null && value.trim().length() > 0 && separator != null && separator.trim().length() > 0) {
            return Arrays.asList(split(value.trim(), separator.trim()));
        }
        return new ArrayList<>();
    }

    public static Boolean getBooleanValue(String tOrF) {
        if (tOrF != null) {
            if (tOrF.trim().equalsIgnoreCase(MorbiditiesConstant.TRUE) || tOrF.trim().equalsIgnoreCase("true")) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } else {
            return null;
        }
    }

    public static float fahrenheitToCelsius(float fahrenheit) {
        return ((fahrenheit - 32) * 5) / 9;
    }

    public static List<String> getListOfOptions(List<OptionDataBean> options) {
        List<String> stringOptions = new ArrayList<>();
        if (options != null && !options.isEmpty()) {
            for (OptionDataBean optionTagBean : options) {
                stringOptions.add(optionTagBean.getValue());
            }
        }
        return stringOptions;
    }

    public static List<OptionDataBean> getOptionsOrDataMap(QueFormBean queFormBean, boolean isSpinner) {
        List<OptionDataBean> options = new ArrayList<>();
        OptionDataBean firstOption = new OptionDataBean();
        if (isSpinner) {
            firstOption.setKey("-1");
            firstOption.setValue(GlobalTypes.SELECT);
            options.add(firstOption);
        }

        if (queFormBean.getDatamap() != null && queFormBean.getDatamap().length() > 0) {
            if (queFormBean.getDatamap().equalsIgnoreCase("Countries list")) {
                OptionDataBean withinIndia = new OptionDataBean();
                withinIndia.setKey("0");
                withinIndia.setValue("Within India");
                options.add(withinIndia);
            }

            List<FieldValueMobDataBean> labelDataBeans = UtilBean.getDataMapValues(queFormBean.getDatamap());
            if (labelDataBeans != null && !labelDataBeans.isEmpty()) {
                for (FieldValueMobDataBean labelDataBean : labelDataBeans) {
                    OptionDataBean option = new OptionDataBean();
                    option.setKey(String.valueOf(labelDataBean.getIdOfValue()));
                    option.setValue(labelDataBean.getValue());
                    option.setNext(null);
                    option.setRelatedProperty(null);
                    options.add(option);
                }
            }
        }

        if (queFormBean.getOptions() != null && !queFormBean.getOptions().isEmpty()) {
            for (OptionTagBean optionTagBean : queFormBean.getOptions()) {
                if (optionTagBean.getKey() != null && optionTagBean.getValue() != null) {
                    OptionDataBean option = new OptionDataBean();
                    option.setKey(optionTagBean.getKey());
                    option.setValue(optionTagBean.getValue());
                    option.setNext(optionTagBean.getNext());
                    option.setRelatedProperty(optionTagBean.getRelatedpropertyname());
                    options.add(option);
                    firstOption.setRelatedProperty(option.getRelatedProperty());
                }
            }
        }

        List<FormulaTagBean> formulas = queFormBean.getFormulas();
        if (formulas != null && !formulas.isEmpty()) {
            String formulaValue = formulas.get(0).getFormulavalue();
            String[] split = UtilBean.split(formulaValue, GlobalTypes.KEY_VALUE_SEPARATOR);
            if (Arrays.toString(split).contains(FormulaConstants.FORMULA_SET_DEFAULT_MIDDLE_NAMES_CBDS)) {
                options.addAll(SharedStructureData.middleNameList);
            } else if (Arrays.toString(split).contains(FormulaConstants.FORMULA_SET_DEFAULT_MEMBERS_UNDER_20_MS)) {
                options.addAll(SharedStructureData.membersUnderTwenty);
            } else if (Arrays.toString(split).contains(FormulaConstants.FORMULA_SET_GIVEN_VACCINES_TO_CHILD)) {
                options.addAll(SharedStructureData.givenVaccinesToChild);
            } else if (Arrays.toString(split).contains(FormulaConstants.FORMULA_SET_MEMBERS_FROM_3_TO_6_YEARS)) {
                options.addAll(SharedStructureData.childrenFrom3To6Years);
            } else if (Arrays.toString(split).contains(FormulaConstants.FORMULA_SET_BASIC_MEDICINE_LIST)) {
                options.addAll(SharedStructureData.basicMedicineList);
            }
        }
        return options;
    }

    public static ArrayAdapter<String> createAdapter(List<String> options) {
        MyArrayAdapter myArrayAdapter = new MyArrayAdapter(SharedStructureData.context, R.layout.spinner_item_top, options);
        myArrayAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        return myArrayAdapter;
    }

    public static List<FieldValueMobDataBean> getDataMapValues(String dataMap) {
        if (dataMap != null && dataMap.trim().length() > 0) {
            List<FieldValueMobDataBean> labelDataBeans = SharedStructureData.mapDataMapLabelBean.get(dataMap);
            if (labelDataBeans == null || labelDataBeans.isEmpty()) {
                labelDataBeans = SharedStructureData.sewaService.getFieldValueMobDataBeanByDataMap(dataMap);
            }
            return labelDataBeans;
        }
        return new ArrayList<>();
    }

    public static String getIronFolicAcidTablet(int ageMonths) {
        String dosage = null;
        if (ageMonths > 6 && ageMonths <= 24) {
            dosage = getDosageHashTable().get(GlobalTypes.IRON_FOLIC_ACID_TABLET_DOSAGE_GT_6_MONTHS);
        }
        return dosage;
    }

    private static Map<String, String> getDosageHashTable() {
        if (dosageHashTable == null) {
            dosageHashTable = new HashMap<>();
            dosageHashTable.put(GlobalTypes.DIARRHOEA_WITH_DEHYDRATION_LT_4_MONTHS, "200 - 400 ml (2 cups)");
            dosageHashTable.put(GlobalTypes.DIARRHOEA_WITH_DEHYDRATION_LT_4_TO_12_MONTHS, "400 - 700 ml (3 cups)");
            dosageHashTable.put(GlobalTypes.DIARRHOEA_WITH_DEHYDRATION_GT_12_MONTHS, "700 - 900 ml (5 cups)");
            dosageHashTable.put(GlobalTypes.DIARRHOEA_WO_DEHYDRATION_LT_2_MONTHS, "5 teaspoon of ORS for every episode of diarrohea");
            dosageHashTable.put(GlobalTypes.DIARRHOEA_WO_DEHYDRATION_GT_2_MONTHS, "200 ml of ORS for every episode of diarrohea (1/2 cup)");
            dosageHashTable.put(GlobalTypes.CHLOROQUINE_TABLET_DOSAGE_0_TO_1_YEAR, "1/2 tablet");
            dosageHashTable.put(GlobalTypes.CHLOROQUINE_TABLET_DOSAGE_GT_1_YEAR, "1 tablet");
            dosageHashTable.put(GlobalTypes.PCM_TABLET_DOSAGE_GT_2_MONTHS, "1/4 tablet");
            dosageHashTable.put(GlobalTypes.VITAMIN_A_DOSAGE_6_TO_12_MONTHS, "Vitamin A 1  lac IU");
            dosageHashTable.put(GlobalTypes.VITAMIN_A_DOSAGE_GT_12_MONTHS, "Vitamin A 2 lac IU");
            dosageHashTable.put(GlobalTypes.IRON_FOLIC_ACID_TABLET_DOSAGE_GT_6_MONTHS, "1 tablet daily for 14 days daily");
        }
        return dosageHashTable;
    }

    public static String getPCMTabletDosage(int ageMonths) {
        String dosage = null;
        if (ageMonths > 2 && ageMonths <= 24) {
            dosage = getDosageHashTable().get(GlobalTypes.PCM_TABLET_DOSAGE_GT_2_MONTHS);
        }
        return dosage;
    }

    public static String getDosageForDiarrhoeaWithDehydration(int ageMonths) {
        String dosage = null;
        if (ageMonths <= 4) {
            dosage = getDosageHashTable().get(GlobalTypes.DIARRHOEA_WITH_DEHYDRATION_LT_4_MONTHS);
        } else if (ageMonths > 12 && ageMonths <= 24) {
            dosage = getDosageHashTable().get(GlobalTypes.DIARRHOEA_WITH_DEHYDRATION_GT_12_MONTHS);
        } else if (ageMonths <= 12) {
            dosage = getDosageHashTable().get(GlobalTypes.DIARRHOEA_WITH_DEHYDRATION_LT_4_TO_12_MONTHS);
        }
        return dosage;
    }

    public static String getDosageForDiarrhoeaWithoutDehydration(int ageMonths) {
        String dosage = null;
        if (ageMonths <= 2) {
            dosage = getDosageHashTable().get(GlobalTypes.DIARRHOEA_WO_DEHYDRATION_LT_2_MONTHS);
        } else if (ageMonths <= 24) {
            dosage = getDosageHashTable().get(GlobalTypes.DIARRHOEA_WO_DEHYDRATION_GT_2_MONTHS);
        }
        return dosage;
    }

    public static String getVitaminADosage(int ageMonths) {
        String dosage = null;
        if (ageMonths > 6 && ageMonths <= 12) {
            dosage = getDosageHashTable().get(GlobalTypes.VITAMIN_A_DOSAGE_6_TO_12_MONTHS);
        } else if (ageMonths > 12) {
            dosage = getDosageHashTable().get(GlobalTypes.VITAMIN_A_DOSAGE_GT_12_MONTHS);
        }
        return dosage;
    }

    public static String getChloroquineTabletDosage(int ageMonths) {
        String dosage;
        if (ageMonths <= 12) {
            dosage = getDosageHashTable().get(GlobalTypes.CHLOROQUINE_TABLET_DOSAGE_0_TO_1_YEAR);
        } else {
            dosage = getDosageHashTable().get(GlobalTypes.CHLOROQUINE_TABLET_DOSAGE_GT_1_YEAR);
        }
        return dosage;
    }

    public static float calculateYearsBetweenDates(long startDate, long endDate) {
        long difference = startDate - endDate;
        return ((float) difference / YEAR_LONG_VALUE);
    }

    public static int calculateMonthsBetweenDates(Date a, Date b) {
        Calendar cal = Calendar.getInstance();
        if (a.before(b)) {
            cal.setTime(a);
        } else {
            cal.setTime(b);
            b = a;
        }
        int c = 0;
        while (cal.getTime().before(b)) {
            cal.add(Calendar.MONTH, 1);
            c++;
        }
        return c - 1;
    }

    public static String setWeightDisplay(String stringToDisplay) {
        if (stringToDisplay != null && stringToDisplay.length() > 0 && stringToDisplay.contains(".")) {
            String[] displayData;
            displayData = split(stringToDisplay, ".");
            if (displayData.length == 2) {
                return displayData[0] + "  Kgs  " + displayData[1] + "00  Gms  ";
            }
        }
        return getMyLabel(GlobalTypes.NOT_AVAILABLE);
    }

    public static String getMyLabel(String labelString) {
        if (labelString == null || labelString.trim().length() == 0) {
            return labelString;
        }
        String convertedStr = replace(labelString, "[ \n]", "");

        LabelBean labelBean = new LabelBean();
        labelBean.setLabelKey(convertedStr);
        if (SewaTransformer.loginBean != null) {
            labelBean.setLanguage(SewaTransformer.loginBean.getLanguageCode());
        } else {
            labelBean.setLanguage("GU"); // default language
        }

        if (SharedStructureData.listLabelBeans != null) {
            labelBean = SharedStructureData.listLabelBeans.get(labelBean.getMapIndex());
            if (labelBean != null) {
                return labelBean.getLabelValue();
            }
        }
        return labelString;
    }

    public static boolean isFileExists(String path) {
        if (path != null) {
            try {
                File file = new File(path);
                return file.exists();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static String replace(String actualString, String searchString, String replacementString) {
        return actualString.replaceAll(searchString, replacementString);
    }

    public static String stringListJoin(Collection<String> list, String separator) {
        if (list != null && !list.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (String item : list) {
                if (first) {
                    first = false;
                } else {
                    sb.append(separator);
                }
                sb.append(item);
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    public static String stringListJoin(List<String> list, String separator, boolean isInternationalise) {
        if (list != null && !list.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (String item : list) {
                if (first) {
                    first = false;
                } else {
                    sb.append(separator);
                }
                if (isInternationalise) {
                    sb.append(getMyLabel(item));
                } else {
                    sb.append(item);
                }
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    public static String arrayJoinToString(String[] list, String separator) {
        if (list != null && list.length > 0) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (String item : list) {
                if (first) {
                    first = false;
                } else {
                    sb.append(separator);
                }
                sb.append(item);
            }
            return sb.toString();
        }
        return null;
    }

    public static String arrayJoinToString(int[] list, String separator) {
        if (list != null && list.length > 0) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (long item : list) {
                if (first) {
                    first = false;
                } else {
                    sb.append(separator);
                }
                sb.append(item);
            }
            return sb.toString();
        }
        return null;
    }

    public static Map<String, String> getFullFormOfEntity() {
        if (entityFullFormNames == null) {
            entityFullFormNames = new HashMap<>();
            entityFullFormNames.put(FormConstants.ANC_MORBIDITY, "ANC Morbidity");
            entityFullFormNames.put(FormConstants.CHILD_CARE_MORBIDITY, "Child Care Morbidity");
            entityFullFormNames.put(FormConstants.PNC_MORBIDITY, "PNC Morbidity");
            //TeCHO FHW FHS
            entityFullFormNames.put(FormConstants.FAMILY_HEALTH_SURVEY, "Family Health Survey");
            entityFullFormNames.put(FormConstants.CFHC, "Comprehensive Family Health Census");
            entityFullFormNames.put(FormConstants.AADHAR_UPDATION, "Aadhar Updation");
            entityFullFormNames.put(FormConstants.AADHAR_PHONE_UPDATION, "Aadhar and Phone Updation");
            entityFullFormNames.put(FormConstants.PHONE_UPDATION, "Phone Updation");
            entityFullFormNames.put(FormConstants.FHSR_PHONE_UPDATE, "Phone Number Verification");
            entityFullFormNames.put(FormConstants.FHS_ADD_MEMBER, "Add New Member");
            entityFullFormNames.put(FormConstants.FHS_MEMBER_UPDATE, "Update Member Information");
            entityFullFormNames.put(FormConstants.MOBILE_NUMBER_VERIFICATION, "Mobile Number Verification");
            //TeCHO FHW RCH
            entityFullFormNames.put(FormConstants.LMP_FOLLOW_UP, "LMP Follow Up Visit");
            entityFullFormNames.put(FormConstants.TECHO_FHW_RIM, "Reproductive Info Modification Visit");
            entityFullFormNames.put(FormConstants.TECHO_FHW_ANC, "Ante Natal Care Visit");
            entityFullFormNames.put(FormConstants.TECHO_FHW_WPD, "Pregnancy Outcome Visit (WPD)");
            entityFullFormNames.put(FormConstants.TECHO_WPD_DISCHARGE, "Discharge Date Entry for WPD");
            entityFullFormNames.put(FormConstants.TECHO_FHW_PNC, "Post Natal Care Visit");
            entityFullFormNames.put(FormConstants.TECHO_FHW_CI, "Child Immunisation Visit");
            entityFullFormNames.put(FormConstants.TECHO_FHW_CS, "Child Services Visit");
            entityFullFormNames.put(FormConstants.TECHO_CS_APPETITE_TEST, "Appetite Test Alert For Child");
            entityFullFormNames.put(FormConstants.TECHO_FHW_VAE, "Vaccine Adverse Effect Visit");
            entityFullFormNames.put(FormConstants.TECHO_MIGRATION_IN, "Migration In");
            entityFullFormNames.put(FormConstants.TECHO_MIGRATION_OUT, "Migration Out");
            entityFullFormNames.put(FormConstants.TECHO_MIGRATION_IN_CONFIRMATION, "Migration In Confirmation");
            entityFullFormNames.put(FormConstants.TECHO_MIGRATION_OUT_CONFIRMATION, "Migration Out Confirmation");
            entityFullFormNames.put(FormConstants.TECHO_MIGRATION_REVERTED, "Reverted Migration");
            entityFullFormNames.put(FormConstants.TECHO_FAMILY_MIGRATION_REVERTED, "Reverted Family Migration");
            entityFullFormNames.put(NotificationConstants.FHW_NOTIFICATION_MIGRATION_IN, "Migration-in Alert");
            entityFullFormNames.put(NotificationConstants.FHW_NOTIFICATION_MIGRATION_OUT, "Migration-out Alert");
            entityFullFormNames.put(NotificationConstants.FHW_NOTIFICATION_FAMILY_MIGRATION_IN, "Family Migration-in Alert");
            entityFullFormNames.put(NotificationConstants.FHW_NOTIFICATION_FAMILY_MIGRATION_OUT, "Family Migration-out Alert");
            entityFullFormNames.put(NotificationConstants.FHW_NOTIFICATION_READ_ONLY, "Read Only Alert");
            entityFullFormNames.put(NotificationConstants.FHW_NOTIFICATION_TT2, "TT2 Alert");
            entityFullFormNames.put(NotificationConstants.FHW_NOTIFICATION_IRON_SUCROSE, "Iron Sucrose Alert");
            entityFullFormNames.put(NotificationConstants.FHW_NOTIFICATION_SAM_SCREENING, "SAM Screening");
            entityFullFormNames.put(NotificationConstants.FHW_WORK_PLAN_MAMTA_DAY, "Mamta Day Workplan");
            entityFullFormNames.put(NotificationConstants.FHW_WORK_PLAN_OTHER_SERVICES, "Other Services Workplan");
            entityFullFormNames.put(NotificationConstants.FHW_WORK_PLAN_ASHA_REPORTED_EVENT, "Confirmation of ASHA reported Events");
            //TeCHO FHW NCD
            entityFullFormNames.put(FormConstants.NCD_FHW_HYPERTENSION, "Hypertension Screening");
            entityFullFormNames.put(FormConstants.NCD_FHW_DIABETES, "Diabetes Screening");
            entityFullFormNames.put(FormConstants.NCD_FHW_ORAL, "Oral Cancer Screening");
            entityFullFormNames.put(FormConstants.NCD_FHW_BREAST, "Breast Cancer Screening");
            entityFullFormNames.put(FormConstants.NCD_FHW_CERVICAL, "Cervical Cancer Screening");
            entityFullFormNames.put(FormConstants.NCD_FHW_MENTAL_HEALTH, "Mental Health Screening");
            entityFullFormNames.put(FormConstants.NCD_FHW_HEALTH_SCREENING, "Health Screening");
            entityFullFormNames.put(FormConstants.NCD_PERSONAL_HISTORY, "Personal History");
            entityFullFormNames.put(FormConstants.NCD_FHW_DIABETES_CONFIRMATION, "NCD Diabetes Confirmation");
            entityFullFormNames.put(FormConstants.NCD_FHW_WEEKLY_CLINIC, "NCD Weekly Clinic Visit");
            entityFullFormNames.put(FormConstants.NCD_FHW_WEEKLY_HOME, "NCD Weekly Home Visit");
            //TeCHO ASHA NCD
            entityFullFormNames.put(FormConstants.NCD_ASHA_CBAC, "NCD CBAC Screening");
            //Techo ASHA RCH
            entityFullFormNames.put(FormConstants.ASHA_LMPFU, "LMP Follow Up Visit");
            entityFullFormNames.put(FormConstants.ASHA_PNC, "Post Natal Care Visit");
            entityFullFormNames.put(FormConstants.ASHA_CS, "Child Services Visit");
            entityFullFormNames.put(FormConstants.ASHA_NPCB, "NPCB Screening");
            entityFullFormNames.put(FormConstants.ASHA_ANC, "Ante Natal Care Visit");
            entityFullFormNames.put(FormConstants.ASHA_WPD, "Work Plan Delivery Visit");
            // Techo ASHA FHS
            entityFullFormNames.put(FormConstants.ASHA_REPORT_FAMILY_MIGRATION, "Report Family Migration");
            entityFullFormNames.put(FormConstants.ASHA_REPORT_FAMILY_SPLIT, "Report Family Split");
            entityFullFormNames.put(FormConstants.ASHA_REPORT_MEMBER_MIGRATION, "Report Member Migration");
            entityFullFormNames.put(FormConstants.ASHA_REPORT_MEMBER_DEATH, "Report Member Death");
            entityFullFormNames.put(FormConstants.ASHA_REPORT_MEMBER_DELIVERY, "Report Member Delivery");

            entityFullFormNames.put(NotificationConstants.NOTIFICATION_FHW_PREGNANCY_CONF, "Pregnancy Confirmation");
            entityFullFormNames.put(NotificationConstants.NOTIFICATION_FHW_DEATH_CONF, "Death Confirmation");
            entityFullFormNames.put(NotificationConstants.NOTIFICATION_FHW_DELIVERY_CONF, "Delivery Confirmation");
            entityFullFormNames.put(NotificationConstants.NOTIFICATION_FHW_MEMBER_MIGRATION, "Process Member Migration");
            entityFullFormNames.put(NotificationConstants.NOTIFICATION_FHW_FAMILY_MIGRATION, "Process Family Migration");
            entityFullFormNames.put(NotificationConstants.NOTIFICATION_FHW_FAMILY_SPLIT, "Process Family Split");

            entityFullFormNames.put(NotificationConstants.ASHA_NOTIFICATION_READ_ONLY, "Read Only Alert");

            entityFullFormNames.put(FormConstants.FAMILY_MIGRATION_OUT, "Family Migration OUT");
            entityFullFormNames.put(FormConstants.FAMILY_MIGRATION_IN_CONFIRMATION, "Family Migration IN Confirmation");
            entityFullFormNames.put(FormConstants.TECHO_AWW_CS, "Child Service Visit");
            entityFullFormNames.put(FormConstants.TECHO_AWW_THR, "Take Home Ration");
            entityFullFormNames.put(FormConstants.TECHO_AWW_HEIGHT_GROWTH_GRAPH, "Child Height growth chart");
            entityFullFormNames.put(FormConstants.TECHO_AWW_WEIGHT_GROWTH_GRAPH, "Child Weight growth chart");
            entityFullFormNames.put(FormConstants.TECHO_AWW_DAILY_NUTRITION, "Daily Nutrition");
            entityFullFormNames.put(FormConstants.FHW_SAM_SCREENING_REF, "SAM Screening Referral");
            entityFullFormNames.put(FormConstants.ASHA_SAM_SCREENING, "SAM Screening");
            entityFullFormNames.put(FormConstants.CMAM_FOLLOWUP, "CMAM followup");
            entityFullFormNames.put(FormConstants.FHW_MONTHLY_SAM_SCREENING, "Monthly SAM Screening");
            entityFullFormNames.put(FormConstants.PREGNANCY_STATUS, "Pregnancy Status");
            entityFullFormNames.put(FormConstants.GERIATRICS_MEDICATION_ALERT, "Geriatrics Medication");
            entityFullFormNames.put(FormConstants.TRAVELLERS_SCREENING, "Travellers' Screening");
            entityFullFormNames.put(FormConstants.IDSP_MEMBER, "IDSP MEMBER");
            entityFullFormNames.put(FormConstants.IDSP_MEMBER_2, "Member Surveillance");
            entityFullFormNames.put(FormConstants.IDSP_FAMILY_2, "Family Surveillance");
            entityFullFormNames.put(FormConstants.IDSP_NEW_FAMILY, "Surveillance New Family");
            entityFullFormNames.put(FormConstants.OFFLINE_ABHA_NUMBER_CREATIONS, "Offline ABHA Number Creation");
            entityFullFormNames.put(NotificationConstants.NOTIFICATION_NCD_HOME_VISIT, "NCD Home Visit");
            entityFullFormNames.put(NotificationConstants.NOTIFICATION_NCD_CLINIC_VISIT, "NCD Clinic Visit");
            entityFullFormNames.put(FormConstants.CHARDHAM_MEMBER_SCREENING, "Chardham Member Screening");
        }
        return entityFullFormNames;
    }

    /**
     * isAlphaNumericWithSpace method checks Whether the String contains
     * ALPHANUMERIC value with space or not.
     *
     * @param string The String which needs to be checked
     * @return true if Given String only contains AlphaNumeric value and space
     * else return false.
     */
    public static boolean isAlphaNumericWithSpace(String string) {
        if (string != null && string.trim().length() > 0) {
            String specialChars = "!~.;-^*:_|@#%+";
            String pattern = ".*[" + Pattern.quote(specialChars) + "].*";
            return !string.matches(pattern);
        }
        return true;
    }

    public static boolean isValidNumber(String validation, float number, float from) {
        if ((validation != null)) {
            if (validation.equalsIgnoreCase(FormulaConstants.VALIDATION_GREATER_THAN)) {
                return (number > from);
            } else if (validation.equalsIgnoreCase(FormulaConstants.VALIDATION_LESS_THAN)) {
                return (number < from);
            } else if (validation.equalsIgnoreCase(FormulaConstants.VALIDATION_GREATER_THAN_EQUAL)) {
                return (number >= from);
            } else if (validation.equalsIgnoreCase(FormulaConstants.VALIDATION_LESS_THAN_EQUAL)) {
                return (number <= from);
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean isFutureDate(long date) {
        if (date > 0) {
            Calendar cal = Calendar.getInstance();
            clearTimeFromDate(cal);
            long today = cal.getTimeInMillis();

            cal.setTimeInMillis(date);
            clearTimeFromDate(cal);
            long enterDate = cal.getTimeInMillis();

            return today < enterDate;
        }
        return false;
    }

    public static boolean isPastDate(long date) {
        if (date > 0) {
            Calendar cal = Calendar.getInstance();
            clearTimeFromDate(cal);
            long today = cal.getTimeInMillis();

            cal.setTimeInMillis(date);
            clearTimeFromDate(cal);
            long enterDate = cal.getTimeInMillis();

            return today > enterDate;
        }
        return false;
    }

    public static boolean isToday(long date) {
        if (date > 0) {
            Calendar cal = Calendar.getInstance();
            clearTimeFromDate(cal);
            long today = cal.getTimeInMillis();

            cal.setTimeInMillis(date);
            clearTimeFromDate(cal);
            long enterDate = cal.getTimeInMillis();

            return today == enterDate;
        }
        return false;
    }

    public static Calendar clearTimeFromDate(Calendar today) {
        today.set(Calendar.MILLISECOND, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.HOUR_OF_DAY, 0);
        return today;
    }

    public static Date clearTimeFromDate(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 0);

            return calendar.getTime();
        }
        return null;
    }

    public static boolean isDateIn(long date, String[] validationMethod, long customTodayDate) {
        if (date != 0) {
            Calendar enterDate = Calendar.getInstance();
            enterDate.setTimeInMillis(date);
            clearTimeFromDate(enterDate);
            long checkThisDate = enterDate.getTimeInMillis();

            Calendar today = Calendar.getInstance();
            if (customTodayDate > 0) {
                today.setTimeInMillis(customTodayDate);
            }

            clearTimeFromDate(today);
            long todayTime = today.getTimeInMillis();

            int year;
            int month;
            int day;

            if (validationMethod != null && validationMethod.length >= 5) {
                try {
                    year = Integer.parseInt(validationMethod[2]);
                    month = Integer.parseInt(validationMethod[3]);
                    day = Integer.parseInt(validationMethod[4]);
                } catch (Exception e) {
                    year = 0;
                    month = 0;
                    day = 0;
                }
                if (validationMethod[1].equalsIgnoreCase("Sub")) {
                    long minRange = calculateDateMinus(todayTime, year, month, day);
                    return minRange <= checkThisDate && checkThisDate <= todayTime;
                } else if (validationMethod[1].equalsIgnoreCase("Add")) {
                    long maxRange = calculateDatePlus(todayTime, year, month, day);
                    return todayTime <= checkThisDate && checkThisDate <= maxRange;
                }
            }
        }
        return false;
    }

    public static boolean isDateOut(long date, String[] validationMethod, long customTodayDate) {
        if (date != 0) {
            Calendar enterDate = Calendar.getInstance();
            enterDate.setTimeInMillis(date);
            clearTimeFromDate(enterDate);
            long checkThisDate = enterDate.getTimeInMillis();

            Calendar today = Calendar.getInstance();
            if (customTodayDate > 0) {
                today.setTimeInMillis(customTodayDate);
            }

            clearTimeFromDate(today);
            long todayTime = today.getTimeInMillis();

            int year;
            int month;
            int day;
            if (validationMethod != null && validationMethod.length >= 5) {
                try {
                    year = Integer.parseInt(validationMethod[2]);
                    month = Integer.parseInt(validationMethod[3]);
                    day = Integer.parseInt(validationMethod[4]);
                } catch (Exception e) {
                    year = 0;
                    month = 0;
                    day = 0;
                }

                if (validationMethod[1].equalsIgnoreCase("Sub")) {
                    long minRange = calculateDateMinus(todayTime, year, month, day);
                    return minRange >= checkThisDate || checkThisDate > todayTime;
                } else if (validationMethod[1].equalsIgnoreCase("Add")) {
                    long maxRange = calculateDatePlus(todayTime, year, month, day);
                    return todayTime > checkThisDate || checkThisDate >= maxRange;
                }
            }
        }
        return false;
    }

    public static String[] split(String original, String separator) {
        if (original != null) {
            if (separator != null && separator.length() == 1) {
                return original.split("[" + separator + "]");
            } else if (separator != null) {
                return original.split(separator);
            }
        }
        return new String[0];
    }

    //as suggested by mayank sir month is taken as 30 and year is taken as 365 days
    public static long getMilliSeconds(int yearsToMinus, int monthsToMinus, int daysToMinus) {
        return (DAY_LONG_VALUE * ((365L * yearsToMinus) + (30L * monthsToMinus) + (daysToMinus)));
    }

    public static long calculateDatePlus(long enterDate, int years, int months, int days) {
        return addYearsMonthsDays(enterDate, years, months, days);
    }

    public static long calculateDateMinus(long enterDate, int years, int months, int days) {
        return addYearsMonthsDays(enterDate, (-1 * years), (-1 * months), (-1 * days));
    }

    public static long addYearsMonthsDays(long enterDate, int years, int months, int days) {
        Calendar cal = Calendar.getInstance();
        if (enterDate > 0) {
            cal.setTimeInMillis(enterDate);
        }
        clearTimeFromDate(cal);
        if (days != 0) {
            cal.add(Calendar.DAY_OF_MONTH, (days));
        }
        if (months != 0) {
            cal.add(Calendar.MONTH, (months));
        }
        if (years != 0) {
            cal.add(Calendar.YEAR, (years));
        }
        return cal.getTimeInMillis();
    }

    // for gestationalWeek no=7 (it will return week)
    // else no=1 (it will return no of day)
    public static int noOfDayFromDate(long date, int no) {
        Calendar calendar = Calendar.getInstance();
        clearTimeFromDate(calendar);
        long difference = calendar.getTimeInMillis() - date;
        return (int) (difference / (DAY_LONG_VALUE * no));
    }

    /**
     * function to convert date to string
     *
     * @param date             Date which is to be converted in string
     * @param displayTime      If displayTime is true, time is merged to return
     * @param displayMonthName If displayMonthName is true, month name is
     *                         displayed instead of number string. time is in 12 hour format
     * @param displayYear      If displayYear is true, Year will be displayed
     * @return returns the date in format mm/dd/yyyy hh:mm [am/pm]
     */
    public static String convertDateToString(long date, boolean displayTime, boolean displayMonthName, boolean displayYear) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        if (day.length() == 1) {
            day = "0" + day;
        }
        StringBuilder dateString = new StringBuilder(day);
        if (displayMonthName) {
            dateString.append(" ");
        } else {
            dateString.append(GlobalTypes.DATE_STRING_SEPARATOR);
        }

        String month = Integer.toString((cal.get(Calendar.MONTH) + 1));
        if (month.length() == 1) {
            month = "0" + month;
        }
        if (displayMonthName) {
            switch (month) {
                case "01":
                    month = GlobalTypes.MONTH_JANUARY;
                    break;
                case "02":
                    month = GlobalTypes.MONTH_FEBRUARY;
                    break;
                case "03":
                    month = GlobalTypes.MONTH_MARCH;
                    break;
                case "04":
                    month = GlobalTypes.MONTH_APRIL;
                    break;
                case "05":
                    month = GlobalTypes.MONTH_MAY;
                    break;
                case "06":
                    month = GlobalTypes.MONTH_JUNE;
                    break;
                case "07":
                    month = GlobalTypes.MONTH_JULY;
                    break;
                case "08":
                    month = GlobalTypes.MONTH_AUGUST;
                    break;
                case "09":
                    month = GlobalTypes.MONTH_SEPTEMBER;
                    break;
                case "10":
                    month = GlobalTypes.MONTH_OCTOBER;
                    break;
                case "11":
                    month = GlobalTypes.MONTH_NOVEMBER;
                    break;
                case "12":
                    month = GlobalTypes.MONTH_DECEMBER;
                    break;
                default:
            }
        }
        if (displayMonthName) {
            dateString.append(getMyLabel(month.substring(0, 3)));
            dateString.append(", ");
        } else {
            dateString.append(getMyLabel(month));
            dateString.append(GlobalTypes.DATE_STRING_SEPARATOR);
        }
        if (displayYear) {
            dateString.append(cal.get(Calendar.YEAR));
        }
        if (displayTime) {
            String min = Integer.toString(cal.get(Calendar.MINUTE));
            if (min.length() == 1) {
                min = "0" + min;
            }

            String hour = Integer.toString(cal.get(Calendar.HOUR));
            if (hour.length() == 1) {
                if (hour.equals("0") && cal.get(Calendar.AM_PM) == Calendar.PM) {
                    hour = "12";
                } else {
                    hour = "0" + hour;
                }
            }
            dateString.append(" ").append(hour).append(":").append(min).append(" ").append((cal.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM"));
        }
        return dateString.toString();
    }

    public static int[] calculateAgeYearMonthDay(long dateDiff) {
        LocalDate dob = new LocalDate(dateDiff);
        LocalDate date = new LocalDate();

        Period period = new Period(dob, date, PeriodType.yearMonthDay());
        return new int[]{period.getYears(), period.getMonths(), period.getDays()};
    }

    //This method calculate age up to mentioned date
    public static int[] calculateAgeYearMonthDayOnGivenDate(Long dobDate, Long givenDate) {
        if (dobDate != null && givenDate != null && dobDate.compareTo(givenDate) <= 0) {
            LocalDate dob = new LocalDate(dobDate);
            LocalDate mentionedDate = new LocalDate(givenDate);

            Period period = new Period(dob, mentionedDate, PeriodType.yearMonthDay());
            return new int[]{period.getYears(), period.getMonths(), period.getDays()};
        }
        return new int[]{0, 0, 0};
    }

    public static String getAgeDisplay(int yr, int month, int day) {
        StringBuilder str = new StringBuilder();
        if (yr == 1) {
            str.append(yr).append(" ").append(getMyLabel(GlobalTypes.YEAR)).append(" ");
        }
        if (yr > 1) {
            str.append(yr).append(" ").append(getMyLabel(GlobalTypes.YEAR)).append("s ");
        }
        if (month == 1) {
            str.append(month).append(" ").append(getMyLabel(GlobalTypes.MONTH)).append(" ");
        }
        if (month > 1) {
            str.append(month).append(" ").append(getMyLabel(GlobalTypes.MONTH)).append("s ");
        }
        if (day == 1) {
            str.append(day).append(" ").append(getMyLabel(GlobalTypes.DAY)).append(" ");
        }
        if (day > 1) {
            str.append(day).append(" ").append(getMyLabel(GlobalTypes.DAY)).append("s ");
        }
        if (day == 0 && month == 0 && yr == 0) {
            str.append(getMyLabel("Born today"));
        }
        if (str.length() > 0) {
            return str.toString();
        }
        return null;
    }

    public static String getAgeDisplayOnGivenDate(Date dobDate, Date givenDate) {
        if (dobDate != null && givenDate != null && dobDate.compareTo(givenDate) <= 0) {
            LocalDate dob = new LocalDate(dobDate);
            LocalDate mentionedDate = new LocalDate(givenDate);

            Period period = new Period(dob, mentionedDate, PeriodType.yearMonthDay());

            StringBuilder str = new StringBuilder();
            if (period.getYears() == 1) {
                str.append(period.getYears()).append(" ").append(getMyLabel(GlobalTypes.YEAR)).append(" ");
            }
            if (period.getYears() > 1) {
                str.append(period.getYears()).append(" ").append(getMyLabel(GlobalTypes.YEAR)).append("s ");
            }
            if (period.getMonths() == 1) {
                str.append(period.getMonths()).append(" ").append(getMyLabel(GlobalTypes.MONTH)).append(" ");
            }
            if (period.getMonths() > 1) {
                str.append(period.getMonths()).append(" ").append(getMyLabel(GlobalTypes.MONTH)).append("s ");
            }
            if (period.getDays() == 1) {
                str.append(period.getDays()).append(" ").append(getMyLabel(GlobalTypes.DAY)).append(" ");
            }
            if (period.getDays() > 1) {
                str.append(period.getDays()).append(" ").append(getMyLabel(GlobalTypes.DAY)).append("s ");
            }
            if (period.getDays() == 0 && period.getMonths() == 0 && period.getYears() == 0) {
                str.append(getMyLabel("Born today"));
            }
            if (str.length() > 0) {
                return str.toString();
            }
        }
        return null;
    }

    public static int getDpsAccordingScreenHeight(Context context, int percentage) {
        if (context != null) {
            return (int) ((context.getResources().getDisplayMetrics().heightPixels) * (percentage / 100.0));
        }
        return percentage;
    }

    public static int getDpsAccordingScreenWidthHeight(Context context, float factor) {
        if (context != null) {
            return (int) ((context.getResources().getDisplayMetrics().xdpi + context.getResources().getDisplayMetrics().ydpi) * (factor / 100));
        }
        return (int) factor;
    }

    public static String getLastUpdatedTime(long aLong) {
        Calendar lastUpdate = Calendar.getInstance();
        lastUpdate.setTimeInMillis(aLong);
        clearTimeFromDate(lastUpdate);
        Calendar currentDay = Calendar.getInstance();
        clearTimeFromDate(currentDay);
        String returnValue = " " + getMyLabel("Last Updated Time :") + " ";
        if (lastUpdate.getTimeInMillis() == currentDay.getTimeInMillis()) {
            lastUpdate.setTimeInMillis(aLong);
            return returnValue + getMyLabel("Today") + new SimpleDateFormat("(hh:mm a)", Locale.getDefault()).format(lastUpdate.getTime());
        }
        currentDay.add(Calendar.DAY_OF_MONTH, -1);
        if (lastUpdate.getTimeInMillis() == currentDay.getTimeInMillis()) {
            lastUpdate.setTimeInMillis(aLong);
            return returnValue + getMyLabel("Yesterday") + new SimpleDateFormat("(hh:mm a)", Locale.getDefault()).format(lastUpdate.getTime());
        }
        lastUpdate.setTimeInMillis(aLong);
        if (aLong > 0) {
            return returnValue + new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT + " hh:mm a", Locale.getDefault()).format(lastUpdate.getTime());
        } else {
            return returnValue + UtilBean.getMyLabel("Not updated till now");
        }
    }

    public static void deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            file.delete();
        } catch (Exception e) {
            Log.e("UtilBean", "File " + filePath + " is not deleted", e);
        }
    }

    public static Date getDateAfterNoOfDays(Date date, int noOfDays) {
        Calendar dateCal = Calendar.getInstance();
        if (date != null) {
            dateCal.setTime(date);
            dateCal.set(Calendar.DAY_OF_MONTH, dateCal.get(Calendar.DAY_OF_MONTH) + noOfDays);
        }
        return dateCal.getTime();
    }

    public static int getNumberOfMonths(Date fromDate, Date toDate) {
        DateTime dateTime1 = new DateTime(fromDate);
        DateTime dateTime2 = new DateTime(toDate);

        return Months.monthsBetween(dateTime1, dateTime2).getMonths();
    }

    public static int getNumberOfWeeks(Date fromDate, Date toDate) {
        DateTime dateTime1 = new DateTime(fromDate);
        DateTime dateTime2 = new DateTime(toDate);

        return Weeks.weeksBetween(dateTime1, dateTime2).getWeeks();
    }

    public static int getNumberOfDays(Date fromDate, Date toDate) {
        DateTime dateTime1 = new DateTime(fromDate);
        DateTime dateTime2 = new DateTime(toDate);

        return Days.daysBetween(dateTime1, dateTime2).getDays() + 1;
    }

    public static TextView getMembersListForDisplay(Context context, FamilyDataBean familyDataBeanDefault) {
        FamilyDataBean familyDataBean = SharedStructureData.currentFamilyDataBean;
        if (familyDataBeanDefault != null) {
            familyDataBean = familyDataBeanDefault;
        }

        MaterialTextView textView = new MaterialTextView(context, null, R.style.CustomAnswerView);
        textView.setPadding(0, 0, 0, 15);

        if (familyDataBean == null || familyDataBean.getMembers() == null || familyDataBean.getMembers().isEmpty()) {
            textView.setText(String.format("%s!", UtilBean.getMyLabel("No members registered in the family")));
            return textView;
        }

        int count = familyDataBean.getMembers().size();
        for (MemberDataBean memberDataBean : familyDataBean.getMembers()) {
            String stringBuilder = memberDataBean.getUniqueHealthId() + " - " + UtilBean.getMemberFullName(memberDataBean);

            String replace = stringBuilder.replace(" null", "");
            replace = replace.replace("null ", "");
            replace = replace.trim();

            Spannable word = new SpannableString(replace);
            word.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.detailsTextColor)), 0, word.length(), SPAN_EXCLUSIVE_EXCLUSIVE);

            Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_bold);
            if (typeface != null) {
                word.setSpan(new StyleSpan(typeface.getStyle()), 0, word.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (memberDataBean.getFamilyHeadFlag() != null && memberDataBean.getFamilyHeadFlag()) {
                word.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.hofTextColor)), 0, word.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (memberDataBean.getGender() != null && memberDataBean.getGender().equals(GlobalTypes.FEMALE)
                    && Boolean.TRUE.equals(memberDataBean.getIsPregnantFlag())) {
                word.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.pregnantWomenTextColor)), 0, word.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (memberDataBean.getDob() != null && calculateAgeYearMonthDay(memberDataBean.getDob())[0] < 5) {
                word.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.childrenTextColor)), 0, word.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            count--;
            textView.append(word);
            if (count != 0) {
                textView.append("\n");
            }
        }
        return textView;
    }

    public static TextView getMemberFullNameForDisplay(Context context) {
        FamilyDataBean familyDataBean = SharedStructureData.currentFamilyDataBean;

        MaterialTextView textView = new MaterialTextView(context);
        textView.setTextAppearance(context, R.style.CustomAnswerView);

        if (familyDataBean != null) {
            for (MemberDataBean memberDataBean : familyDataBean.getMembers()) {

                if (memberDataBean.getUniqueHealthId().equals(SharedStructureData.currentMemberUHId)) {
                    String stringBuilder = UtilBean.getMemberFullName(memberDataBean);

                    String replace = stringBuilder.replace(" null", "");
                    replace = replace.replace("null ", "");
                    replace = replace.trim();

                    Spannable word = new SpannableString(replace);

                    if (memberDataBean.getFamilyHeadFlag() != null && memberDataBean.getFamilyHeadFlag()) {
                        word.setSpan(new ForegroundColorSpan(Color.rgb(48, 112, 6)), 0, word.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (memberDataBean.getGender() != null && memberDataBean.getGender().equals(GlobalTypes.FEMALE)
                            && (memberDataBean.getIsPregnantFlag() != null && memberDataBean.getIsPregnantFlag())) {
                        word.setSpan(new ForegroundColorSpan(Color.RED), 0, word.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (memberDataBean.getDob() != null && calculateAgeYearMonthDay(memberDataBean.getDob())[0] < 5) {
                        word.setSpan(new ForegroundColorSpan(Color.BLUE), 0, word.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    textView.append(word);
                }
            }
        }
        return textView;
    }

    public static String getMemberFullName(MemberBean memberBean) {
        StringBuilder sb = new StringBuilder();
        if (memberBean.getFirstName() != null) {
            sb.append(memberBean.getFirstName());
        }
        if (memberBean.getMiddleName() != null) {
            sb.append(" ");
            sb.append(memberBean.getMiddleName());
        }
        if (memberBean.getLastName() != null) {
            sb.append(" ");
            sb.append(memberBean.getLastName());
        }
        if (sb.length() > 0) {
            return sb.toString();
        } else {
            return "";
        }
    }

    public static String getMemberFullName(MemberDataBean memberBean) {
        StringBuilder sb = new StringBuilder();
        if (memberBean.getFirstName() != null) {
            sb.append(memberBean.getFirstName());
        }
        if (memberBean.getMiddleName() != null) {
            sb.append(" ");
            sb.append(memberBean.getMiddleName());
        }
        if (memberBean.getLastName() != null) {
            sb.append(" ");
            sb.append(memberBean.getLastName());
        }
        if (sb.length() > 0) {
            return sb.toString();
        } else {
            return "";
        }
    }

    public static String getTouristFullName(ChardhamTouristsDataBean memberBean) {
        StringBuilder sb = new StringBuilder();
        if (memberBean.getFirstName() != null) {
            sb.append(memberBean.getFirstName());
        }
        if (memberBean.getMiddleName() != null) {
            sb.append(" ");
            sb.append(memberBean.getMiddleName());
        }
        if (memberBean.getLastName() != null) {
            sb.append(" ");
            sb.append(memberBean.getLastName());
        }
        if (sb.length() > 0) {
            return sb.toString();
        } else {
            return "Name not available";
        }
    }

    public static String getTouristFullName(ChardhamTouristScreeningDto memberBean) {
        StringBuilder sb = new StringBuilder();
        if (memberBean.getFirstName() != null) {
            sb.append(memberBean.getFirstName());
        }
        if (memberBean.getMiddleName() != null) {
            sb.append(" ");
            sb.append(memberBean.getMiddleName());
        }
        if (memberBean.getLastName() != null) {
            sb.append(" ");
            sb.append(memberBean.getLastName());
        }
        if (sb.length() > 0) {
            return sb.toString();
        } else {
            return "Name not available";
        }
    }

    public static String getTouristFullName(ChardhamTouristScreeningDataBean memberBean) {
        StringBuilder sb = new StringBuilder();
        if (memberBean.getFirstName() != null) {
            sb.append(memberBean.getFirstName());
        }
        if (memberBean.getMiddleName() != null) {
            sb.append(" ");
            sb.append(memberBean.getMiddleName());
        }
        if (memberBean.getLastName() != null) {
            sb.append(" ");
            sb.append(memberBean.getLastName());
        }
        if (sb.length() > 0) {
            return sb.toString();
        } else {
            return "Name not available";
        }
    }

    public static String getTouristFullName(ChardhamSupportUserBean memberBean) {
        StringBuilder sb = new StringBuilder();
        if (memberBean.getFirstName() != null) {
            sb.append(memberBean.getFirstName());
        }
        if (memberBean.getLastName() != null) {
            sb.append(" ");
            sb.append(memberBean.getLastName());
        }
        if (sb.length() > 0) {
            return sb.toString();
        } else {
            return "Name not available";
        }
    }

    public static String getFamilyFullAddress(FamilyDataBean familyDataBean) {
        StringBuilder stringBuilder = new StringBuilder();
        if (familyDataBean.getAddress1() != null || familyDataBean.getAddress2() != null) {
            if (familyDataBean.getAddress1() != null) {
                stringBuilder.append(familyDataBean.getAddress1());
                if (familyDataBean.getAddress2() != null) {
                    stringBuilder.append(" ");
                    stringBuilder.append(familyDataBean.getAddress2());
                }
            } else {
                if (familyDataBean.getAddress2() != null) {
                    stringBuilder.append(familyDataBean.getAddress2());
                }
            }
        } else {
            stringBuilder.append(UtilBean.getMyLabel(GlobalTypes.NOT_AVAILABLE));
        }
        return stringBuilder.toString();
    }

    public static String getTitleText(String title) {
        return UtilBean.getMyLabel(title);
    }

    public static String calculateBmi(Integer height, Float weight) {
        if (height != null && height > 0 && weight != null && weight > 0) {
            float heightInMetres = height / 100f;

            float bmi = weight / heightInMetres;
            bmi = bmi / heightInMetres;
            return Float.toString(bmi);
        }
        return null;
    }

    public static SpannableString addBullet(String text) {
        SpannableString spannable = new SpannableString(text);
        spannable.setSpan(new BulletSpan(15), 0, text.length(), 0);
        return spannable;
    }


    public static void showAlertAndExit(final String msg, final Context context) {
        final Activity activity = (Activity) context;
        activity.runOnUiThread(() -> {
            View.OnClickListener listener = v -> {
                dialogForExit.dismiss();
                activity.finish();
            };
            dialogForExit = new MyAlertDialog(context, false, msg, listener, DynamicUtils.BUTTON_OK);
            dialogForExit.show();
        });
    }

    public static String getNotAvailableIfNull(String text) {
        if (text != null && !text.isEmpty()) {
            return text;
        }
        return GlobalTypes.NOT_AVAILABLE;
    }

    public static String getNotAvailableIfNull(Object object) {
        if (object != null && !object.toString().isEmpty()) {
            return object.toString();
        }
        return GlobalTypes.NOT_AVAILABLE;
    }

    public static String returnYesNoNotAvailableFromBoolean(Boolean bool) {
        if (Boolean.TRUE.equals(bool)) {
            return LabelConstants.YES;
        } else if (Boolean.FALSE.equals(bool)) {
            return LabelConstants.NO;
        } else {
            return LabelConstants.NOT_AVAILABLE;
        }
    }

    public static String returnYesNoNotAvailableFromBoolean(Object object) {
        if (object != null) {
            if (Boolean.parseBoolean(object.toString())) {
                return LabelConstants.YES;
            } else {
                return LabelConstants.NO;
            }
        } else {
            return LabelConstants.NOT_AVAILABLE;
        }
    }

    public static String returnKeyFromBoolean(Object object) {
        if (object != null) {
            if (Boolean.parseBoolean(object.toString())) {
                return "1";
            } else {
                return "2";
            }
        } else {
            return null;
        }
    }

    public static List<String> getBlockedMobileNumbers() {
        List<String> blockedMobileNumbers = new ArrayList<>();
        blockedMobileNumbers.add("9999999999");
        blockedMobileNumbers.add("8888888888");
        blockedMobileNumbers.add("7777777777");
        blockedMobileNumbers.add("6666666666");
        blockedMobileNumbers.add("5555555555");
        return blockedMobileNumbers;
    }

    public static List<String> getSupportedExtensions() {
        List<String> supportedExtensions = new ArrayList<>();
        supportedExtensions.add("3gp");
        supportedExtensions.add("mp4");
        supportedExtensions.add("jpg");
        supportedExtensions.add("png");
        supportedExtensions.add("mp3");
        supportedExtensions.add("pdf");
        return supportedExtensions;
    }

    public static String getFormattedTime(int time) {
        if (time < 10) {
            return String.format(Locale.getDefault(), "%02d", time);
        } else {
            return Integer.toString(time);
        }
    }

    public static String getRelatedPropertyNameWithLoopCounter(String relatedPropertyName, int loopCounter) {
        if (loopCounter > 0) {
            return relatedPropertyName + loopCounter;
        } else {
            return relatedPropertyName;
        }
    }

    public static void restartApplication(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        if (intent != null) {
            ComponentName componentName = intent.getComponent();
            Intent mainIntent = Intent.makeRestartActivityTask(componentName);
            context.startActivity(mainIntent);
        }
        Runtime.getRuntime().exit(0);
    }

    public static String getGenderLabelFromValue(String gender) {
        if (gender == null) {
            return null;
        }
        switch (gender) {
            case GlobalTypes.MALE:
                return LabelConstants.MALE;
            case GlobalTypes.FEMALE:
                return LabelConstants.FEMALE;
            case GlobalTypes.TRANSGENDER:
                return LabelConstants.TRANSGENDER;
            case GlobalTypes.OTHER:
                return LabelConstants.OTHER;
            default:
                return gender;
        }
    }

    public static String getGenderValueAs123FromGender(String gender) {
        if (gender == null) {
            return null;
        }
        switch (gender) {
            case GlobalTypes.MALE:
                return "1";
            case GlobalTypes.FEMALE:
                return "2";
            case GlobalTypes.TRANSGENDER:
                return "3";
            default:
                return "0";
        }
    }

    public static String getDifferenceBetweenTwoDates(Date from, Date to) {
        if (from != null && to != null && from.compareTo(to) <= 0) {
            LocalDateTime fromDate = new LocalDateTime(from);
            LocalDateTime toDate = new LocalDateTime(to);

            Period period = new Period(fromDate, toDate, PeriodType.yearMonthDayTime());
            PeriodFormatter formatter = PeriodFormat.getDefault();

            return formatter.print(period);
        }
        return null;
    }

    public static String getFinancialYearFromDate(Date date) {
        Calendar instance = Calendar.getInstance();

        if (date != null) {
            instance.setTime(date);
        }

        int year = instance.get(Calendar.MONTH) < 3 ? instance.get(Calendar.YEAR) - 1 : instance.get(Calendar.YEAR);
        return year + "-" + (year + 1);
    }

    public static Date getStartOfFinancialYear(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }

        int year = cal.get(Calendar.MONTH) < 3 ? cal.get(Calendar.YEAR) - 1 : cal.get(Calendar.YEAR);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, 3);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static String addCommaSeparatedStringIfNotExists(String previousString, String stringToAdd) {
        if (previousString == null || previousString.isEmpty()) {
            return stringToAdd;
        } else if (previousString.contains(stringToAdd)) {
            return previousString;
        } else {
            return previousString + "," + stringToAdd;
        }
    }

    public static Date endOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        return cal.getTime();
    }

    public static String aadhaarNumber(String answer) {
        if (answer == null) {
            return null;
        }
        answer = answer.trim();

        if (!Pattern.compile("^[0-9]{12}$").matcher(answer).matches()) {
            return "Aadhaar number must contains 12 digit numbers only";
        }

        int[][] d = {
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                {1, 2, 3, 4, 0, 6, 7, 8, 9, 5},
                {2, 3, 4, 0, 1, 7, 8, 9, 5, 6},
                {3, 4, 0, 1, 2, 8, 9, 5, 6, 7},
                {4, 0, 1, 2, 3, 9, 5, 6, 7, 8},
                {5, 9, 8, 7, 6, 0, 4, 3, 2, 1},
                {6, 5, 9, 8, 7, 1, 0, 4, 3, 2},
                {7, 6, 5, 9, 8, 2, 1, 0, 4, 3},
                {8, 7, 6, 5, 9, 3, 2, 1, 0, 4},
                {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}
        };

        int[][] p = {
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                {1, 5, 7, 6, 2, 8, 3, 0, 9, 4},
                {5, 8, 0, 3, 7, 9, 6, 1, 4, 2},
                {8, 9, 1, 6, 0, 4, 3, 5, 2, 7},
                {9, 4, 5, 3, 1, 2, 6, 8, 7, 0},
                {4, 2, 8, 6, 5, 7, 3, 9, 0, 1},
                {2, 7, 9, 3, 8, 0, 6, 4, 1, 5},
                {7, 0, 4, 6, 9, 1, 3, 2, 5, 8}
        };

        int c = 0;
        int l = answer.length();
        for (int t = 0; t<l; t++) {
            c = d[c][p[(t % 8)][Integer.parseInt(answer.charAt(l-t-1)+"")]];
        }

        if (c!=0) {
            return "Please enter valid aadhaar number";
        }

        return null;
    }
    public static String getLMSFileName(Long mediaId, String fileName) {
        return mediaId == null ? null : String.format("%s_%s%s", "LMS_MEDIA", mediaId, fileName.substring(fileName.lastIndexOf(".")));
    }
    public static String getTimeSpentFromMillis(Long timeSpentInMillis) {
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = timeSpentInMillis / daysInMilli;
        timeSpentInMillis = timeSpentInMillis % daysInMilli;

        long elapsedHours = timeSpentInMillis / hoursInMilli;
        timeSpentInMillis = timeSpentInMillis % hoursInMilli;

        long elapsedMinutes = timeSpentInMillis / minutesInMilli;
        timeSpentInMillis = timeSpentInMillis % minutesInMilli;

        long elapsedSeconds = timeSpentInMillis / secondsInMilli;

        String duration = "";
        if (elapsedDays != 0) {
            duration = String.format("%sd", elapsedDays);
        }

        if (elapsedHours != 0) {
            duration = String.format("%s %sh", duration, elapsedHours);
        }

        if (elapsedMinutes != 0) {
            duration = String.format("%s %sm", duration, elapsedMinutes);
        }

        duration = String.format("%s %ss", duration, elapsedSeconds);
        return duration.trim();
    }

    public static boolean videoFileIsCorrupted(Context myContext, String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(myContext, Uri.parse(path));
            String hasVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
            retriever.release();
            return "yes".equals(hasVideo);
        } catch (Exception e) {
            e.printStackTrace();
            retriever.release();
            return false;
        }
    }

    public static String getTreatmentStatus(String status) {
        if (status != null) {
            if (status.equalsIgnoreCase(LabelConstants.CPHC)) {
                return "CPHC treatment";
            } else if (status.equalsIgnoreCase(LabelConstants.OUTSIDE)) {
                return "Outside treamnet";
            }
        }
        return LabelConstants.NOT_AVAILABLE;
    }
}
