package com.argusoft.sewa.android.app.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.argusoft.sewa.android.app.constants.FhsConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.FormulaConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RchConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.databean.FieldValueMobDataBean;
import com.argusoft.sewa.android.app.databean.MedicineListItemDataBean;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.argusoft.sewa.android.app.util.UtilBean.clearTimeFromDate;

/**
 * Created by prateek on 10/8/19
 */
public class ValidationFormulaUtil {

    private ValidationFormulaUtil() {
        throw new IllegalStateException("Utility Class");
    }

    public static String alphanumericWithSpace(String answer, ValidationTagBean validation) {
        String validationMessage = null;

        if (!UtilBean.isAlphaNumericWithSpace(answer)) {
            if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                validationMessage = validation.getMessage();
            } else {
                validationMessage = "Must alphanumeric and space";
            }
        }
        return validationMessage;
    }

    public static String isFutureDate(String[] split, String answer, ValidationTagBean validation) {
        try {
            long date;
            try {
                if (split.length > 2) {
                    if (split[1] != null && split[2] != null) {
                        String[] answer1 = UtilBean.split(answer.trim(), split[1].trim());
                        int index;
                        try {
                            index = Integer.parseInt(split[2].trim());
                        } catch (NumberFormatException e) {
                            index = 1;
                        }
                        if (answer1.length > index) {
                            date = Long.parseLong(answer1[index].trim());
                        } else {
                            date = Long.parseLong(answer.trim());
                        }
                    } else {
                        date = Long.parseLong(answer.trim());
                    }
                } else {
                    date = Long.parseLong(answer.trim());
                }
            } catch (NumberFormatException e) {
                date = 0L;
            }
            if (UtilBean.isFutureDate(date)) {
                if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                    return validation.getMessage();
                } else {
                    return GlobalTypes.MSG_VALIDATION_INVALID_DATE;
                }
            }
        } catch (Exception e) {
            return GlobalTypes.MSG_VALIDATION_INVALID_DATE;
        }

        return null;
    }

    public static String isPastDate(String[] split, String answer, ValidationTagBean validation) {
        try {
            long date;
            try {
                if (split.length > 2) {
                    if (split[1] != null && split[2] != null) {
                        String[] answer1 = UtilBean.split(answer.trim(), split[1].trim());
                        int index;
                        try {
                            index = Integer.parseInt(split[2].trim());
                        } catch (Exception e) {
                            index = 1;
                        }
                        if (answer1.length > index) {
                            date = Long.parseLong(answer1[index].trim());
                        } else {
                            date = Long.parseLong(answer.trim());
                        }
                    } else {
                        date = Long.parseLong(answer.trim());
                    }
                } else {
                    date = Long.parseLong(answer.trim());
                }
            } catch (Exception e) {
                date = 0L;
            }
            if (UtilBean.isPastDate(date)) {
                if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                    return validation.getMessage();

                } else {
                    return GlobalTypes.MSG_VALIDATION_INVALID_DATE;
                }
            }
        } catch (Exception e) {
            return GlobalTypes.MSG_VALIDATION_INVALID_DATE;
        }

        return null;
    }

    public static String isNotToday(String[] split, String answer, ValidationTagBean validation) {
        try {
            long date;
            try {
                if (split.length > 2) {
                    if (split[1] != null && split[2] != null) {
                        String[] answer1 = UtilBean.split(answer.trim(), split[1].trim());
                        int index;
                        try {
                            index = Integer.parseInt(split[2].trim());
                        } catch (Exception e) {
                            index = 1;
                        }
                        if (answer1.length > index) {
                            date = Long.parseLong(answer1[index].trim());
                        } else {
                            date = Long.parseLong(answer.trim());
                        }
                    } else {
                        date = Long.parseLong(answer.trim());
                    }
                } else {
                    date = Long.parseLong(answer.trim());
                }
            } catch (Exception e) {
                date = 0L;
            }
            if (UtilBean.isToday(date)) {
                if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                    return validation.getMessage();

                } else {
                    return GlobalTypes.MSG_VALIDATION_INVALID_DATE;
                }
            }
        } catch (Exception e) {
            return GlobalTypes.MSG_VALIDATION_INVALID_DATE;
        }
        return null;
    }

    public static String isDateIn(String[] split, String answer, ValidationTagBean validation) {
        try {
            long date;
            try {
                if (split.length > 6) {
                    if (split[5] != null && split[6] != null) {
                        String[] answer1 = UtilBean.split(answer.trim(), split[5].trim());
                        int index;
                        try {
                            index = Integer.parseInt(split[6].trim());
                        } catch (Exception e) {
                            index = 1;
                        }
                        if (answer1.length > index) {
                            date = Long.parseLong(answer1[index].trim());
                        } else {
                            date = Long.parseLong(answer.trim());
                        }
                    } else {
                        date = Long.parseLong(answer.trim());
                    }
                } else {
                    date = Long.parseLong(answer.trim());
                }
            } catch (Exception e) {
                date = 0L;
            }
            long customTodayDate = 0;
            if (split.length == 6) {
                String tmpObj = SharedStructureData.relatedPropertyHashTable.get(split[5]);
                if (tmpObj != null) {
                    customTodayDate = Long.parseLong(tmpObj);
                }
            }

            if (!UtilBean.isDateIn(date, split, customTodayDate)) { // check if submitted date is in between calculated date from given parameters. (here range is given in months. EG : isDateIn-Sub-1-2-3 means is submitted date is between today and date before 1 year 2 months 3 days?), returns true if in range
                if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                    return validation.getMessage();

                } else {
                    return GlobalTypes.MSG_VALIDATION_INVALID_DATE;
                }
            }
        } catch (Exception e) {
            return GlobalTypes.MSG_VALIDATION_INVALID_DATE;
        }
        return null;
    }

    public static String isDateOut(String[] split, String answer, ValidationTagBean validation) {
        try {
            long date;
            try {
                if (split.length > 6) {
                    if (split[5] != null && split[6] != null) {
                        String[] answer1 = UtilBean.split(answer.trim(), split[5].trim());
                        int index;
                        try {
                            index = Integer.parseInt(split[6].trim());
                        } catch (Exception e) {
                            index = 1;
                        }
                        if (answer1.length > index) {
                            date = Long.parseLong(answer1[index].trim());
                        } else {
                            date = Long.parseLong(answer.trim());
                        }
                    } else {
                        date = Long.parseLong(answer.trim());
                    }
                } else {
                    date = Long.parseLong(answer.trim());
                }
            } catch (Exception e) {
                date = 0L;
            }

            long customTodayDate = 0;
            if (split.length == 6) {
                String tmpData = SharedStructureData.relatedPropertyHashTable.get(split[5]);
                if (tmpData != null) {
                    customTodayDate = Long.parseLong(tmpData);
                }
            }

            // check if submitted date is not in between calculated date from given parameters. (here range is given in months. EG : isDateIn-Sub-1-2-3 means is submitted date is not between today and date before 1 year 2 months 3 days?), returns true if in range
            if (!UtilBean.isDateOut(date, split, customTodayDate)) {
                if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                    return validation.getMessage();

                } else {
                    return GlobalTypes.MSG_VALIDATION_INVALID_DATE;
                }
            }
        } catch (Exception e) {
            return GlobalTypes.MSG_VALIDATION_INVALID_DATE;
        }

        return null;
    }

    public static String comapreDateWithGivenDate(String[] split, String answer, int counter, ValidationTagBean validation) {
        if (split[1] != null && split[1].trim().length() > 0) {
            if (counter > 0) {
                split[1] += counter;
            }
            String getValue = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            if (getValue != null && getValue.trim().length() > 0) {
                try {
                    long enterDate;
                    long comparedDate = Long.parseLong(getValue);
                    if (split.length == 4 && split[2] != null && split[2].trim().length() > 0 && split[3] != null && split[3].trim().length() > 0) {
                        String[] splitAnswer = answer.split(split[2]);
                        int index = Integer.parseInt(split[3]);
                        if (splitAnswer.length > index) {
                            answer = splitAnswer[index];
                        }
                    }
                    enterDate = Long.parseLong(answer);

                    Calendar enterDateCal = Calendar.getInstance();
                    enterDateCal.setTimeInMillis(enterDate);
                    enterDate = UtilBean.clearTimeFromDate(enterDateCal).getTimeInMillis();

                    Calendar clearTimeFromDate = Calendar.getInstance();
                    clearTimeFromDate.setTimeInMillis(comparedDate);
                    comparedDate = UtilBean.clearTimeFromDate(clearTimeFromDate).getTimeInMillis();
                    if (enterDate < comparedDate) {
                        if (validation.getMessage().contains("$s")) {
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            return String.format(validation.getMessage(), format.format(comparedDate));
                        }
                        return validation.getMessage();
                    }
                } catch (Exception e) {
                    return GlobalTypes.MSG_VALIDATION_INVALID_DATE;
                }
            }
        }
        return null;
    }

    public static String comapreDateWithGivenDateAfter(String[] split, String answer, int counter, ValidationTagBean validation) {
        if (split[1] != null && split[1].trim().length() > 0) {
            if (counter > 0) {
                split[1] += counter;
            }
            String getValue = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            if (getValue != null && getValue.trim().length() > 0) {
                try {
                    long enterDate;
                    long comparedDate = Long.parseLong(getValue);
                    if (split.length == 4 && split[2] != null && split[2].trim().length() > 0 && split[3] != null && split[3].trim().length() > 0) {
                        String[] splitAnswer = answer.split(split[2]);
                        int index = Integer.parseInt(split[3]);
                        if (splitAnswer.length > index) {
                            answer = splitAnswer[index];
                        }
                    }
                    enterDate = Long.parseLong(answer);

                    Calendar enterDateCal = Calendar.getInstance();
                    enterDateCal.setTimeInMillis(enterDate);
                    enterDate = UtilBean.clearTimeFromDate(enterDateCal).getTimeInMillis();

                    Calendar comparedDateCal = Calendar.getInstance();
                    comparedDateCal.setTimeInMillis(comparedDate);
                    comparedDate = UtilBean.clearTimeFromDate(comparedDateCal).getTimeInMillis();
                    if (enterDate > comparedDate) {
                        return validation.getMessage();
                    }
                } catch (Exception e) {
                    return GlobalTypes.MSG_VALIDATION_INVALID_DATE;
                }
            }
        }
        return null;
    }

    public static String comapreDateBefore(String[] split, String answer, int counter, ValidationTagBean validation) {
        if (split[1] != null && split[1].trim().length() > 0) {
            if (counter > 0) {
                split[1] += counter;
            }
            String getValue = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            if (getValue != null && getValue.trim().length() > 0) {
                try {
                    long enterDate;
                    long comparedDate = Long.parseLong(getValue);
                    if (split.length == 4 && split[2] != null && split[2].trim().length() > 0 && split[3] != null && split[3].trim().length() > 0) {
                        String[] splitAnswer = answer.split(split[2]);
                        int index = Integer.parseInt(split[3]);
                        if (splitAnswer.length > index) {
                            answer = splitAnswer[index];
                        }
                    }
                    //for SRDB
                    if (answer.indexOf('T') > -1) {
                        enterDate = Long.parseLong(answer.substring(2));
                    } else {
                        enterDate = Long.parseLong(answer);
                    }

                    Calendar enterDateCal = Calendar.getInstance();
                    enterDateCal.setTimeInMillis(enterDate);
                    enterDate = UtilBean.clearTimeFromDate(enterDateCal).getTimeInMillis();

                    Calendar clearTimeFromDate = Calendar.getInstance();
                    clearTimeFromDate.setTimeInMillis(comparedDate);
                    comparedDate = UtilBean.clearTimeFromDate(clearTimeFromDate).getTimeInMillis();
                    if (enterDate <= comparedDate) {
                        return validation.getMessage();
                    }
                } catch (Exception e) {
                    return GlobalTypes.MSG_VALIDATION_INVALID_DATE;
                }
            }
        }
        return null;
    }

    public static String comapreDateAfter(String[] split, String answer, int counter, ValidationTagBean validation) {
        if (split[1] != null && split[1].trim().length() > 0) {
            if (counter > 0) {
                split[1] += counter;
            }
            String getValue = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            if (getValue != null && getValue.trim().length() > 0) {
                try {
                    long enterDate;
                    long comparedDate = Long.parseLong(getValue);
                    if (split.length == 4 && split[2] != null && split[2].trim().length() > 0 && split[3] != null && split[3].trim().length() > 0) {
                        String[] splitAnswer = answer.split(split[2]);
                        int index = Integer.parseInt(split[3]);
                        if (splitAnswer.length > index) {
                            answer = splitAnswer[index];
                        }
                    }
                    enterDate = Long.parseLong(answer);

                    Calendar enterDateCal = Calendar.getInstance();
                    enterDateCal.setTimeInMillis(enterDate);
                    enterDate = UtilBean.clearTimeFromDate(enterDateCal).getTimeInMillis();

                    Calendar comparedDateCal = Calendar.getInstance();
                    comparedDateCal.setTimeInMillis(comparedDate);
                    comparedDate = UtilBean.clearTimeFromDate(comparedDateCal).getTimeInMillis();
                    if (enterDate >= comparedDate) {
                        return validation.getMessage();
                    }
                } catch (Exception e) {
                    return GlobalTypes.MSG_VALIDATION_INVALID_DATE;
                }
            }
        }
        return null;
    }

    public static String containsCharacterPipeline(String answer, ValidationTagBean validation) {
        // check if text contains |, returns true if contains
        if (answer != null && answer.contains("|")) {
            if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                return validation.getMessage();
            } else {
                return GlobalTypes.MSG_VALIDATION_PIPELINE_MSG;
            }
        }
        return null;
    }

    public static String greaterThen0(String answer, ValidationTagBean validation) {
        String validationMessage = null;

        try {
            float number;
            try {
                number = Float.parseFloat(answer.trim());
            } catch (NumberFormatException e) {
                number = 0;
            }
            if (!UtilBean.isValidNumber(FormulaConstants.VALIDATION_GREATER_THAN, number, 0)) {
                if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                    validationMessage = validation.getMessage();
                } else {
                    validationMessage = "Number is not greater than 0";
                }
            }
        } catch (Exception e) {
            validationMessage = "Number is not greater than 0";
        }

        return validationMessage;
    }

    public static String greaterThan(String[] split, String answer, ValidationTagBean validation) {
        String validationMessage = null;

        try {
            float number;
            try {
                number = Float.parseFloat(answer.trim());
            } catch (NumberFormatException e) {
                number = 0;
            }
            float from = 0.0f;
            if (split[1] != null && split[1].length() > 0) {
                try {
                    from = Float.parseFloat(split[1].trim());
                } catch (NumberFormatException e) {
                    from = 0;
                }
            }
            if (!UtilBean.isValidNumber(FormulaConstants.VALIDATION_GREATER_THAN, number, from)) {
                if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                    validationMessage = validation.getMessage();
                } else {
                    validationMessage = LabelConstants.INVALID_VALUE;
                }
            }
        } catch (Exception e) {
            return LabelConstants.INVALID_VALUE;
        }
        return validationMessage;
    }

    public static String greaterThanEq(String[] split, String answer, ValidationTagBean validation) {
        String validationMessage = null;

        try {
            float number;
            try {
                number = Float.parseFloat(answer.trim());
            } catch (Exception e) {
                number = 0;
            }
            float from = 0.0f;
            if (split[1] != null && split[1].length() > 0) {
                try {
                    from = Float.parseFloat(split[1].trim());
                } catch (Exception e) {
                    from = 0;
                }
            }
            if (!UtilBean.isValidNumber(FormulaConstants.VALIDATION_GREATER_THAN_EQUAL, number, from)) {
                if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                    validationMessage = validation.getMessage();
                } else {
                    validationMessage = LabelConstants.INVALID_VALUE;
                }
            }
        } catch (Exception e) {
            return LabelConstants.INVALID_VALUE;
        }
        return validationMessage;
    }

    public static String lessThan(String[] split, String answer, ValidationTagBean validation) {
        String validationMessage = null;

        try {
            float number;
            try {
                number = Float.parseFloat(answer.trim());
            } catch (Exception e) {
                number = 0;
            }
            float from = 0.0f;
            if (split[1] != null && split[1].length() > 0) {
                try {
                    from = Float.parseFloat(split[1].trim());
                } catch (Exception e) {
                    from = 0;
                }
            }
            if (!UtilBean.isValidNumber(FormulaConstants.VALIDATION_LESS_THAN, number, from)) {
                if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                    validationMessage = validation.getMessage();
                } else {
                    validationMessage = LabelConstants.INVALID_VALUE;
                }
            }
        } catch (Exception e) {
            return LabelConstants.INVALID_VALUE;
        }
        return validationMessage;
    }

    public static String lessThanEq(String[] split, String answer, ValidationTagBean validation) {
        String validationMessage = null;
        try {
            float number;
            try {
                number = Float.parseFloat(answer.trim());
            } catch (Exception e) {
                number = 0;
            }
            float from = 0.0f;
            if (split[1] != null && split[1].length() > 0) {
                try {
                    from = Float.parseFloat(split[1].trim());
                } catch (Exception e) {
                    from = 0;
                }
            }
            if (!UtilBean.isValidNumber(FormulaConstants.VALIDATION_LESS_THAN_EQUAL, number, from)) {
                if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                    validationMessage = validation.getMessage();
                } else {
                    validationMessage = LabelConstants.INVALID_VALUE;
                }
            }
        } catch (Exception e) {
            return LabelConstants.INVALID_VALUE;
        }

        return validationMessage;
    }

    public static String lessThanEqRelatedProperty(String[] split, String answer, ValidationTagBean validation) {
        String validationMessage = null;
        try {
            float number;
            try {
                number = Float.parseFloat(answer.trim());
            } catch (Exception e) {
                number = 0;
            }
            float from = 0.0f;
            if (split[1] != null && split[1].length() > 0) {
                String tmpData = SharedStructureData.relatedPropertyHashTable.get(split[1]);
                if (tmpData != null) {
                    try {
                        from = Float.parseFloat(tmpData);
                    } catch (Exception e) {
                        from = 0;
                    }
                }
            }
            if (!UtilBean.isValidNumber(FormulaConstants.VALIDATION_LESS_THAN_EQUAL, number, from)) {
                if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                    validationMessage = validation.getMessage();
                } else {
                    validationMessage = LabelConstants.INVALID_VALUE;
                }
            }
        } catch (Exception e) {
            return LabelConstants.INVALID_VALUE;
        }

        return validationMessage;
    }


    public static String between(String[] split, String answer, ValidationTagBean validation) {
        String validationMessage = null;
        try {
            float number;
            try {
                number = Float.parseFloat(answer.trim());
            } catch (Exception e) {
                number = 0;
            }
            float from = 0.0f;
            float to = 0.0f;
            String[] range = UtilBean.split(split[1], GlobalTypes.COMMA);
            if (range.length == 2) {
                try {
                    from = Float.parseFloat(range[0].trim());
                    to = Float.parseFloat(range[1].trim());
                } catch (Exception e) {
                    from = 0;
                    to = 0;
                }
            }
            if (!UtilBean.isValidNumber(FormulaConstants.VALIDATION_GREATER_THAN_EQUAL, number, from) || !UtilBean.isValidNumber(FormulaConstants.VALIDATION_LESS_THAN_EQUAL, number, to)) {
                if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                    validationMessage = validation.getMessage();
                } else {
                    validationMessage = LabelConstants.INVALID_VALUE;
                }
            }
        } catch (Exception e) {
            return LabelConstants.INVALID_VALUE;
        }

        return validationMessage;
    }

    public static String checkInputLength(String[] split, String answer, ValidationTagBean validation) {
        String validationMessage = null;
        if (split.length > 1 && split[1] != null) {
            String[] answerArray = answer.split("");
            if (answerArray.length - 1 < Integer.parseInt(split[1])) {
                if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                    return validation.getMessage();
                } else {
                    validationMessage = LabelConstants.INVALID_VALUE;
                }
            }
        }
        return validationMessage;
    }

    public static String mobileNumber(String[] split, String answer, ValidationTagBean validation) {
        String validationMessage = null;
        if (split.length > 1 && !split[1].equals("10")) {
            int length = Integer.parseInt(split[1]);
            if (answer.trim().length() != length) {
                if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                    return validation.getMessage();
                } else {
                    validationMessage = LabelConstants.INVALID_VALUE;
                }
            }
        } else {
            if (answer.trim().length() != 10) {
                validationMessage = "Mobile number must contain 10 digits.";
                return validationMessage;
            }

            char c = answer.charAt(0);
            if (String.valueOf(c).matches("[012345]")) {
                if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                    return validation.getMessage();
                } else {
                    validationMessage = "Mobile number can't start from 0 to 5";
                }
            }

            long count = SharedStructureData.sewaFhsService.getMobileNumberCount(answer);
            if (count > 3) {
                validationMessage = "Mobile number is already registered with other family. Please enter another number.";
            }

            if (UtilBean.getBlockedMobileNumbers().contains(answer)) {
                validationMessage = "The mobile number is blocked. Please enter another number";
            }
        }
        return validationMessage;
    }

    public static String aadhaarNumber(String answer) {
        String validationMessage = null;
        if (answer == null) {
            validationMessage = "Please enter aadhaar number.";
            return validationMessage;
        }
        validationMessage = UtilBean.aadhaarNumber(answer);
        return validationMessage;
    }

    public static String inputLengthRange(String[] split, String answer, ValidationTagBean validation) {
        String validationMessage = null;
        if (split.length > 1) {
            String[] inputRange = split[1].split(",");
            if (inputRange.length == 2) {
                int from = Integer.parseInt(inputRange[0]);
                int to = Integer.parseInt(inputRange[1]);
                if (answer.trim().length() < from || answer.trim().length() > to) {
                    if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                        return validation.getMessage();
                    } else {
                        validationMessage = LabelConstants.INVALID_VALUE;
                    }
                }
            }
        }
        return validationMessage;
    }

    public static String maxLength(String[] split, String answer, ValidationTagBean validation) {
        String validationMessage = null;
        if (answer.trim().length() > Integer.parseInt(split[1])) {
            if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                return validation.getMessage();
            } else {
                validationMessage = LabelConstants.INVALID_VALUE;
            }
        }
        return validationMessage;
    }

    public static String length(String[] split, String answer, ValidationTagBean validation) {
        String validationMessage = null;
        if (answer.trim().length() != Integer.parseInt(split[1])) {
            if (validation.getMessage() != null && validation.getMessage().trim().length() > 0) {
                return validation.getMessage();
            } else {
                validationMessage = LabelConstants.INVALID_VALUE;
            }
        }

        return validationMessage;
    }

    public static String onlyOneHead(ValidationTagBean validation) {
        String familyFound = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.FAMILY_FOUND);
        if (familyFound != null && familyFound.equals("1")) {
            if (SharedStructureData.loopBakCounter == 0) {
                String headAnswer = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.FAMILY_HEAD_FLAG);
                if (headAnswer != null && headAnswer.equals("2")) {
                    return validation.getMessage();
                }
            } else {
                int headsDeclared = 0;
                for (int i = 0; i < SharedStructureData.loopBakCounter + 1; i++) {
                    String headAnswer;
                    String statusAnswer;
                    if (i == 0) {
                        headAnswer = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.FAMILY_HEAD_FLAG);
                        statusAnswer = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.MEMBER_STATUS);
                    } else {
                        headAnswer = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.FAMILY_HEAD_FLAG + i);
                        statusAnswer = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.MEMBER_STATUS + i);
                    }
                    if ((statusAnswer == null && headAnswer != null && headAnswer.equals("1"))
                            || (headAnswer != null && statusAnswer != null && headAnswer.equals("1") && statusAnswer.equals("1"))) {
                        headsDeclared++;
                    }

                }
                if (headsDeclared != 1) {
                    return validation.getMessage();
                }
            }
        }
        return null;
    }

    public static String checkHeadMemberAge(ValidationTagBean validation) {
        String familyFound = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.FAMILY_FOUND);
        if (familyFound != null && familyFound.equals("1")) {
            String dob = null;
            for (int i = 0; i < SharedStructureData.loopBakCounter + 1; i++) {
                String headAnswer;
                String statusAnswer;
                String dob1;
                if (i == 0) {
                    headAnswer = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.FAMILY_HEAD_FLAG);
                    statusAnswer = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.MEMBER_STATUS);
                    dob1 = SharedStructureData.relatedPropertyHashTable.get("dob");
                } else {
                    headAnswer = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.FAMILY_HEAD_FLAG + i);
                    statusAnswer = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.MEMBER_STATUS + i);
                    dob1 = SharedStructureData.relatedPropertyHashTable.get("dob" + i);
                }
                if ((statusAnswer == null && headAnswer != null && headAnswer.equals("1"))
                        || (headAnswer != null && statusAnswer != null && headAnswer.equals("1") && statusAnswer.equals("1"))) {
                    dob = dob1;
                }
            }
            if (dob != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR, -18);
                if (new Date(Long.parseLong(dob)).after(calendar.getTime())) {
                    return validation.getMessage();
                }
            }
        }
        return null;
    }

    public static String maritalStatusValidation(String answer, int counter, ValidationTagBean validation) {
        String tempObj;
        if (counter == 0) {
            tempObj = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.ANS_12);
        } else {
            tempObj = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.ANS_12 + counter);
        }
        if (tempObj != null && ((tempObj.equals("1") && answer.equals("641"))
                || (tempObj.equals("2") && answer.equals("643")))) {
            return validation.getMessage();
        }

        return null;
    }

    public static String checkHofRelationWithMaritalStatus(String[] split, String answer, int counter, ValidationTagBean validation) {
        if (split.length > 1) {
            String property = UtilBean.getRelatedPropertyNameWithLoopCounter(split[1], counter);
            property = SharedStructureData.relatedPropertyHashTable.get(property);

            if (property == null) {
                return null;
            }

            switch (split[1]) {
                case RelatedPropertyNameConstants.ANS_12:
                    if ((property.equals("1") && answer.equals("641"))
                            || (property.equals("2") && answer.equals("643"))) {
                        return validation.getMessage();
                    }
                    break;
                case RelatedPropertyNameConstants.RELATION_WITH_HOF:
                    if (!answer.equals("629") && (FhsConstants.RELATION_WIFE.equals(property)
                            || FhsConstants.RELATION_HUSBAND.contains(property))) {
                        return validation.getMessage();
                    }
                    break;
                default:
            }
        }

        return null;
    }


    public static String villageSelectionCheck(String answer, ValidationTagBean validation) {
        String locationId = SharedStructureData.relatedPropertyHashTable.get("locationId");
        if (locationId != null && locationId.equalsIgnoreCase(answer)) {
            return validation.getMessage();
        }
        return null;
    }

    public static String motherChildComponentValidation(String answer, ValidationTagBean validation) {
        String tmpObj = SharedStructureData.relatedPropertyHashTable.get("numberOfMemberForMotherSelection");
        if (tmpObj != null) {
            int size = Integer.parseInt(tmpObj);
            Gson gson = new Gson();
            Map<String, String> answerMap = gson.fromJson(answer, new TypeToken<HashMap<String, String>>() {
            }.getType());
            if (size != answerMap.size()) {
                return validation.getMessage();
            }
            for (Map.Entry<String, String> entry : answerMap.entrySet()) {
                if (entry.getKey().equals(entry.getValue())) {
                    return UtilBean.getMyLabel("Mother and Child selection for a child is not valid.");
                }
            }
        }
        return null;
    }

    public static String vaccinationValidationChild(String answer) {
        Date dob = null;
        String dDateObj = SharedStructureData.relatedPropertyHashTable.get("deliveryDate");
        String dobObj = SharedStructureData.relatedPropertyHashTable.get("dob");
        if (SharedStructureData.formType != null && (SharedStructureData.formType.equals(FormConstants.TECHO_FHW_WPD)
                || SharedStructureData.formType.equals(FormConstants.TECHO_FHW_PNC)) && dDateObj != null) {
            dob = new Date(Long.parseLong(dDateObj));
        } else if (dobObj != null) {
            dob = new Date(Long.parseLong(dobObj));
        }

        if (dob != null) {
            Date givenDate = new Date(Long.parseLong(answer));
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(SharedStructureData.context);
            String currentVaccine = sharedPref.getString("currentVaccine", null);
            if (currentVaccine != null) {
                return SharedStructureData.immunisationService.vaccinationValidationForChild(
                        dob, givenDate, currentVaccine.trim(), SharedStructureData.vaccineGivenDateMap);
            }
        }
        return null;
    }

    public static String checkNumberFormatException(String answer) {
        try {
            Long.parseLong(answer);
        } catch (NumberFormatException ex) {
            return answer + UtilBean.getMyLabel(" is not a number.");
        }
        return null;
    }

    public static String checkSingleECMemberFamily() {
        String familyStatus = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.FAMILY_FOUND);
        if (SharedStructureData.loopBakCounter == 0 && "1".equals(familyStatus)) {
            String dob = SharedStructureData.relatedPropertyHashTable.get("dob");
            String gender = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.ANS_12);
            String status = SharedStructureData.relatedPropertyHashTable.get("defaultMaritalStatus");

            if (dob != null) {
                Date dobDate = new Date(Long.parseLong(dob));
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR, -15);
                Date before15Years = calendar.getTime();
                calendar.add(Calendar.YEAR, -34);
                Date before49Years = calendar.getTime();
                if (gender != null && gender.equals("2")
                        && status != null && status.equals("629")
                        && dobDate.after(before49Years) && dobDate.before(before15Years)) {
                    return UtilBean.getMyLabel("Single Eligible Couple Member cannot be added as a Family. "
                            + "Please enter the full family details. "
                            + "If this member belongs to another family, use the family update feature.");
                }
            }
        }
        return null;
    }

    public static String checkAgeForNotUnmarried(String answer, int counter, ValidationTagBean validation) {
        String status;
        if (counter == 0) {
            status = SharedStructureData.relatedPropertyHashTable.get("defaultMaritalStatus");
        } else {
            status = SharedStructureData.relatedPropertyHashTable.get("defaultMaritalStatus" + counter);
        }
        List<FieldValueMobDataBean> labelDataBeans = SharedStructureData.sewaService.getFieldValueMobDataBeanByDataMap("maritalStatusList");
        FieldValueMobDataBean selectedStatus = null;
        for (FieldValueMobDataBean data: labelDataBeans) {
            if (data.getIdOfValue() == Integer.parseInt(status)) {
                selectedStatus = data;
            }
        }

        if (answer != null) {
            Date dobDate = new Date(Long.parseLong(answer));
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -15);
            Date before15Years = calendar.getTime();
            if (status != null && !status.equals("630")
                    && dobDate.after(before15Years)) {
                return UtilBean.getMyLabel(validation.getMessage() + " for " + selectedStatus.getValue().toLowerCase() + " person.");
            }
        }
        return null;
    }

    public static String checkDaysGapDeliveryDate(String[] split, ValidationTagBean validation) {
        int numberOfDays = 2;
        if (split.length == 2) {
            numberOfDays = Integer.parseInt(split[1]);
        }

        if (SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.HEALTH_INFRASTRUCTURE_TYPE_ID) == null) {
            return null;
        }

        if (RchConstants.PHI_INSTITUTIONS_TYPE_ID_FOR_2_DAYS_DEL_GAP.contains(
                SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.HEALTH_INFRASTRUCTURE_TYPE_ID))) {
            String deliveryDate = SharedStructureData.relatedPropertyHashTable.get("deliveryDate");
            Calendar todayCalendar = Calendar.getInstance();
            clearTimeFromDate(todayCalendar);
            todayCalendar.add(Calendar.DATE, -numberOfDays);
            if (deliveryDate != null && todayCalendar.getTime().getTime() < Long.parseLong(deliveryDate)) {
                return validation.getMessage();
            }
        }

        return null;
    }

    // checkServiceDateForHealthInfra-serviceDate-govtHospDayValidation-pvtHospDayValidation
    public static String checkServiceDateForHealthInfra(String[] split, ValidationTagBean validation) {
        String healthInfrastructureTypeId = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.HEALTH_INFRASTRUCTURE_TYPE_ID);
        if (healthInfrastructureTypeId != null) {
            Date serviceDate = new Date();
            Date lastValidDate;
            if (split.length > 1) {
                String serviceDateString = SharedStructureData.relatedPropertyHashTable.get(split[1]);
                if (serviceDateString != null) {
                    serviceDate = new Date(Long.parseLong(serviceDateString));
                }
            }

            int govtHospDayValidation = 15;
            int pvtHospDayValidation = 30;
            if (split.length > 2) {
                govtHospDayValidation = Integer.parseInt(split[2]);
            }
            if (split.length > 3) {
                pvtHospDayValidation = Integer.parseInt(split[3]);
            }

            if (RchConstants.GOVERNMENT_INSTITUTIONS.contains(Long.valueOf(healthInfrastructureTypeId))) {
                Calendar instance = Calendar.getInstance();
                instance.add(Calendar.DATE, govtHospDayValidation * -1);
                lastValidDate = instance.getTime();
            } else {
                Calendar instance = Calendar.getInstance();
                instance.add(Calendar.DATE, pvtHospDayValidation * -1);
                lastValidDate = instance.getTime();
            }

            if (serviceDate.before(lastValidDate)) {
                return validation.getMessage();
            }
        }
        return null;
    }

    public static String checkServiceDateForHomeVisit(String[] split, String answer, ValidationTagBean validation) {
        if (!answer.equalsIgnoreCase("HOSP")) {
            Date serviceDate;
            Date lastValidDate;
            if (split.length > 1) {
                String serviceDateString = SharedStructureData.relatedPropertyHashTable.get(split[1]);
                if (serviceDateString != null) {
                    serviceDate = new Date(Long.parseLong(serviceDateString));
                } else {
                    serviceDate = new Date();
                }
            } else {
                serviceDate = new Date();
            }

            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.DATE, -15);
            lastValidDate = instance.getTime();

            if (serviceDate.before(lastValidDate)) {
                return validation.getMessage();
            }
        }
        return null;
    }

    public static String checkIntegerPlus(String[] split, String answer, ValidationTagBean validation) {
        if (split.length > 1) {
            String relatedProperyName = split[1];
            String previousValue = SharedStructureData.relatedPropertyHashTable.get(relatedProperyName);
            if (previousValue != null) {
                int previous = Integer.parseInt(previousValue);
                int current = Integer.parseInt(answer);
                if (current < previous) {
                    return validation.getMessage();
                }
            }
        }
        return null;
    }

    public static String familyPlanningDateValidation(String[] split, String answer, ValidationTagBean validation) {
        // familyPlanningDateValidation-lastMethodOfContraception-serviceDate-lastDeliveryDate
        if (split.length > 1) {
            String familyPlanningRelatedPropertyName = split[1];
            String familyPlanning = SharedStructureData.relatedPropertyHashTable.get(familyPlanningRelatedPropertyName);
            if (familyPlanning != null) {
                Calendar instance = Calendar.getInstance();
                if (split.length > 2) {
                    String serviceDate = SharedStructureData.relatedPropertyHashTable.get(split[2]);
                    if (serviceDate != null && !serviceDate.equalsIgnoreCase("null")) {
                        instance.setTimeInMillis(Long.parseLong(serviceDate));
                    }
                }

                Date serviceDate = instance.getTime();
                Date insertionDate = new Date(Long.parseLong(answer));
                Date validationDate;

                switch (familyPlanning) {
                    case RchConstants.IUCD_10_YEARS:
                        instance.add(Calendar.YEAR, -10);
                        validationDate = instance.getTime();

                        if (insertionDate.before(validationDate) || insertionDate.after(serviceDate)) {
                            return validation.getMessage();
                        }
                        break;

                    case RchConstants.IUCD_5_YEARS:
                        instance.add(Calendar.YEAR, -5);
                        validationDate = instance.getTime();

                        if (insertionDate.before(validationDate) || insertionDate.after(serviceDate)) {
                            return validation.getMessage();
                        }
                        break;

                    case RchConstants.ANTARA:
                        instance.add(Calendar.MONTH, -3);
                        validationDate = instance.getTime();

                        if (insertionDate.after(serviceDate) || insertionDate.before(validationDate)) {
                            return validation.getMessage();
                        }
                        break;

                    case RchConstants.PPIUCD:
                        if (split.length > 3) {
                            String lastDeliveryDateString = SharedStructureData.relatedPropertyHashTable.get(split[3]);
                            if (lastDeliveryDateString != null && !lastDeliveryDateString.equalsIgnoreCase("null")) {
                                instance.setTimeInMillis(Long.parseLong(lastDeliveryDateString));
                                Date lastDeliveryDate = instance.getTime();
                                instance.add(Calendar.DATE, 2);
                                validationDate = instance.getTime();

                                if (insertionDate.after(validationDate) || insertionDate.before(lastDeliveryDate)) {
                                    return validation.getMessage();
                                }
                            } else {
                                return validation.getMessage();
                            }
                        }
                        break;

                    case RchConstants.PAIUCD:
                        if (split.length > 3) {
                            String lastDeliveryDateString = SharedStructureData.relatedPropertyHashTable.get(split[3]);
                            if (lastDeliveryDateString != null && !lastDeliveryDateString.equalsIgnoreCase("null")) {
                                instance.setTimeInMillis(Long.parseLong(lastDeliveryDateString));
                                Date lastDeliveryDate = instance.getTime();
                                instance.add(Calendar.DATE, 12);
                                validationDate = instance.getTime();

                                if (insertionDate.after(validationDate) || insertionDate.before(lastDeliveryDate)) {
                                    return validation.getMessage();
                                }
                            } else {
                                return validation.getMessage();
                            }
                        }
                        break;

                    default:
                        return null;
                }
            }
        }
        return null;
    }

    public static String checkGivenSachets(String[] split, String answer, ValidationTagBean validation) {
        if (split.length == 2 && answer != null) {
            String tmpObj = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            if (tmpObj != null) {
                int toBeGiven = Integer.parseInt(tmpObj);
                if (Integer.parseInt(answer) > toBeGiven) {
                    return validation.getMessage();
                }
            }
        } else {
            return UtilBean.getMyLabel("Please enter valid given sachets.");
        }
        return null;
    }

    public static String checkConsumedSachets(String[] split, String answer, ValidationTagBean validation) {
        if (split.length == 2 && answer != null) {
            String tmpObj = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            if (tmpObj != null && Integer.parseInt(tmpObj) < Integer.parseInt(answer)) {
                return validation.getMessage();
            }
        }
        return null;
    }

    public static String checkHofRelationWithGender(String[] split, String answer, int counter, ValidationTagBean validation) {
        // checkHofRelationWithGender-relatedPropertyToTestWith

        if (split.length > 1) {
            String property = UtilBean.getRelatedPropertyNameWithLoopCounter(split[1], counter);
            property = SharedStructureData.relatedPropertyHashTable.get(property);

            if (property == null) {
                return null;
            }

            switch (split[1]) {
                case RelatedPropertyNameConstants.ANS_12:
                    if ((property.equals("1") && FhsConstants.FEMALE_RELATION.contains(answer))
                            || (property.equals("2") && FhsConstants.MALE_RELATION.contains(answer))) {
                        return validation.getMessage();
                    }
                    break;
                case RelatedPropertyNameConstants.RELATION_WITH_HOF:
                    if ((answer.equals("1") && FhsConstants.FEMALE_RELATION.contains(property))
                            || (answer.equals("2") && FhsConstants.MALE_RELATION.contains(property))) {
                        return validation.getMessage();
                    }
                    break;
                default:
            }
        }

        return null;
    }

    public static String validateMedicineDetail(String answer, ValidationTagBean validation) {

        if (answer != null) {
            List<MedicineListItemDataBean> medicineDetails = new Gson().fromJson(answer, new TypeToken<List<MedicineListItemDataBean>>() {
            }.getType());
            if (medicineDetails != null && !medicineDetails.isEmpty()) {
                for (MedicineListItemDataBean medicine : medicineDetails) {
                    if (medicine.getDuration() == null || medicine.getFrequency() == null) {
                        return validation.getMessage();
                    }
                    if (medicine.getDuration() == 0 || medicine.getFrequency() == 0) {
                        return UtilBean.getMyLabel(LabelConstants.INVALID_VALUE);
                    }
                }
            }
        }
        return null;
    }

    public static String validateWeddingDate(String[] split, String answer, int counter, ValidationTagBean validation) {

        if (split.length > 1) {
            String property = UtilBean.getRelatedPropertyNameWithLoopCounter(split[1], counter);
            property = SharedStructureData.relatedPropertyHashTable.get(property);

            if (property == null) {
                return null;
            }

            if (answer != null) {
                Date dobDate = new Date(Long.parseLong(property));
                Date weddingDate = new Date(Long.parseLong(answer));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dobDate);
                calendar.add(Calendar.YEAR, 15);
                Date after15Years = calendar.getTime();
                if (weddingDate.before(after15Years)) {
                    return UtilBean.getMyLabel(validation.getMessage());
                }
            }
        }
        return null;
    }

    public static String validateDiabetesValue(String[] split, String answer, ValidationTagBean validation) {
        if (split.length > 1) {
            String property = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            if ((answer == null || answer.isEmpty()) && (property == null || property.isEmpty())) {
                return UtilBean.getMyLabel(validation.getMessage());
            }
        }
        return null;
    }

    public static String checkChardhamIsPregnantWithGender(String[] split, String answer, int counter, ValidationTagBean validation) {
        // checkHofRelationWithGender-relatedPropertyToTestWith

        if (split.length > 1) {
            String property = UtilBean.getRelatedPropertyNameWithLoopCounter(split[1], counter);
            String isPregnant = UtilBean.getRelatedPropertyNameWithLoopCounter(split[2], counter);
            property = SharedStructureData.relatedPropertyHashTable.get(property);
            isPregnant = SharedStructureData.relatedPropertyHashTable.get(isPregnant);

            if (property == null || isPregnant == null) {
                return null;
            }

            if (RelatedPropertyNameConstants.CHARDHAM_MEMBER_GENDER.equals(split[1])) {
                if (((property.equals("1") || property.equals("3"))) && isPregnant.equals("1")) {
                    return validation.getMessage();
                }
            }
        }

        return null;
    }
}
