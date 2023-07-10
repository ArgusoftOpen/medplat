package com.argusoft.sewa.android.app.constants;

import com.argusoft.sewa.android.app.R;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RbskConstants {

    private RbskConstants() {
        throw new IllegalStateException("Utility Class");
    }

    public static final String BODY_PART_HEAD = "HEAD";
    public static final String BODY_PART_EYE = "EYES";
    public static final String BODY_PART_MOUTH = "MOUTH";
    public static final String BODY_PART_NOSE = "NOSE";
    public static final String BODY_PART_CHEEKS = "CHEEKS";
    public static final String BODY_PART_EAR = "EARS";
    public static final String BODY_PART_HAND = "HAND";
    public static final String BODY_PART_LEG = "LEG";
    public static final String BODY_PART_CHEST = "CHEST";
    public static final String BODY_PART_STOMACH = "STOMACH";
    public static final String BODY_PART_NECK = "NECK";
    public static final String BODY_PART_SKIN = "SKIN";
    public static final String BODY_PART_BACK = "BACK";
    public static final String BODY_PART_GENITALIA = "GENITALIA";

    public static final String FULL_NAME_HAND = "HANDS(UPPER LIMBS)";
    public static final String FULL_NAME_LEG = "LEGS(LOWER LIMBS)";
    public static final String FULL_NAME_STOMACH = "STOMACH/ABDOMEN";
    public static final String FULL_NAME_GENITALIA = "GENITALIA & ANUS";

    public static final String TONE_OF_BODY_DEFECT_HYPERTONIA = "HYPERTONIA";
    public static final String TONE_OF_BODY_DEFECT_HYPOTONIA = "HYPOTONIA";
    public static final String TONE_OF_BODY_DEFECT_NORMAL = "NORMAL";

    public static final String RADIO_BUTTON_SELECTION_VALUE_YES = "YES";
    public static final String RADIO_BUTTON_SELECTION_VALUE_NO = "NO";
    public static final String RADIO_BUTTON_SELECTION_VALUE_NOT_KNOWN = "NOT_KNOWN";

    public static final String APGAR_O_TO_3 = "0-3";
    public static final String APGAR_4_TO_7 = "4-7";
    public static final String APGAR_7_PLUS = "7+";

    public static final Map<Integer, String> APGARS = Collections.unmodifiableMap(getApgars());
    public static final Map<Integer, String> TONE_OF_BODY_DEFECT_VALUE = Collections.unmodifiableMap(getToneOfBodyDefectValue());
    public static final Map<Integer, String> COMMON_DEFECT_SELECTION_VALUES = Collections.unmodifiableMap(getCommonDefectSelectionValues());
    public static final Map<String, Integer> BODY_PART_IMAGE_IDS = Collections.unmodifiableMap(getBodyPartImageIds());
    public static final Map<String, String> BODY_PART_FULL_NAME = Collections.unmodifiableMap(getBodyPartFullName());

    private static Map<Integer, String> getApgars() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, APGAR_O_TO_3);
        map.put(2, APGAR_4_TO_7);
        map.put(3, APGAR_7_PLUS);
        return map;
    }

    private static Map<Integer, String> getToneOfBodyDefectValue() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, TONE_OF_BODY_DEFECT_HYPERTONIA);
        map.put(2, TONE_OF_BODY_DEFECT_HYPOTONIA);
        map.put(3, TONE_OF_BODY_DEFECT_NORMAL);
        return map;
    }

    private static Map<Integer, String> getCommonDefectSelectionValues() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, RADIO_BUTTON_SELECTION_VALUE_YES);
        map.put(2, RADIO_BUTTON_SELECTION_VALUE_NO);
        map.put(3, RADIO_BUTTON_SELECTION_VALUE_NOT_KNOWN);
        return map;
    }

    private static Map<String, Integer> getBodyPartImageIds() {
        Map<String, Integer> map = new HashMap<>();
        map.put(BODY_PART_HEAD, R.drawable.body_part_head);
        map.put(BODY_PART_EYE, R.drawable.body_part_eye);
        map.put(BODY_PART_MOUTH, R.drawable.body_part_mouth);
        map.put(BODY_PART_NOSE, R.drawable.body_part_nose);
        map.put(BODY_PART_CHEEKS, R.drawable.body_part_cheek);
        map.put(BODY_PART_EAR, R.drawable.body_part_ear);
        map.put(BODY_PART_HAND, R.drawable.body_part_hand);
        map.put(BODY_PART_LEG, R.drawable.body_part_leg);
        map.put(BODY_PART_CHEST, R.drawable.body_part_chest);
        map.put(BODY_PART_STOMACH, R.drawable.body_part_stomach);
        map.put(BODY_PART_NECK, R.drawable.body_part_neck);
        map.put(BODY_PART_BACK, R.drawable.body_part_back);
        map.put(BODY_PART_GENITALIA, R.drawable.body_part_genitalia);
        return map;
    }

    private static Map<String, String> getBodyPartFullName() {
        Map<String, String> map = new HashMap<>();
        map.put(BODY_PART_HEAD, BODY_PART_HEAD);
        map.put(BODY_PART_EYE, BODY_PART_EYE);
        map.put(BODY_PART_MOUTH, BODY_PART_MOUTH);
        map.put(BODY_PART_NOSE, BODY_PART_NOSE);
        map.put(BODY_PART_CHEEKS, BODY_PART_CHEEKS);
        map.put(BODY_PART_EAR, BODY_PART_EAR);
        map.put(BODY_PART_HAND, FULL_NAME_HAND);
        map.put(BODY_PART_LEG, FULL_NAME_LEG);
        map.put(BODY_PART_CHEST, BODY_PART_CHEST);
        map.put(BODY_PART_STOMACH, FULL_NAME_STOMACH);
        map.put(BODY_PART_NECK, BODY_PART_NECK);
        map.put(BODY_PART_BACK, BODY_PART_BACK);
        map.put(BODY_PART_GENITALIA, FULL_NAME_GENITALIA);
        return map;
    }
}
