package com.argusoft.sewa.android.app.util;

import static com.argusoft.sewa.android.app.datastructure.SharedStructureData.context;
import static com.argusoft.sewa.android.app.util.UtilBean.createAdapter;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.FormulaConstants;
import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.databean.FieldValueMobDataBean;
import com.argusoft.sewa.android.app.databean.OptionDataBean;
import com.argusoft.sewa.android.app.databean.OptionTagBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.morbidities.constants.MorbiditiesConstant;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class FormulaUtil {

    private static final String TAG = "FormulaUtil";

    private FormulaUtil() {
        throw new IllegalStateException("Utility Class");
    }

    public static void addPeriod(String[] split, QueFormBean queFormBean, boolean isValid) {
        if (queFormBean.getAnswer() != null && queFormBean.getAnswer().toString().trim().length() > 0) {
            Calendar dateSubmitted = Calendar.getInstance();
            try {
                dateSubmitted.setTimeInMillis(Long.parseLong(queFormBean.getAnswer().toString()));
            } catch (NumberFormatException e) {
                Log.e(TAG, null, e);
            }
            UtilBean.clearTimeFromDate(dateSubmitted);

            int countGestationalWeek = UtilBean.noOfDayFromDate(dateSubmitted.getTimeInMillis(), 7);
            SharedStructureData.relatedPropertyHashTable.put("gestationalWeek", String.valueOf(countGestationalWeek));

            int queCode;
            try {
                queCode = Integer.parseInt(split[1]);
                if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                    queCode = DynamicUtils.getLoopId(queCode, queFormBean.getLoopCounter());
                }
            } catch (Exception e) {
                queCode = 0;
            }

            QueFormBean ques = SharedStructureData.mapIndexQuestion.get(queCode);
            if (ques != null) {
                TextView textField = (TextView) ques.getQuestionTypeView();
                if (isValid) {
                    int year;
                    int month;
                    int day;
                    if (split.length >= 5) {
                        try {
                            year = Integer.parseInt(split[2]);
                            month = Integer.parseInt(split[3]);
                            day = Integer.parseInt(split[4]);
                        } catch (Exception e) {
                            year = 0;
                            month = 0;
                            day = 0;
                        }

                        long futureDate = UtilBean.addYearsMonthsDays(dateSubmitted.getTimeInMillis(), year, month, day);
                        String convertDateToString = UtilBean.convertDateToString(futureDate, false, false, true);
                        textField.setText(convertDateToString);
                        ques.setAnswer(convertDateToString);
                    }
                } else {
                    textField.setText(UtilBean.getMyLabel(GlobalTypes.NOT_AVAILABLE));
                    ques.setAnswer(null);
                }
            }
        }
    }

    public static void setAnswer(QueFormBean queFormBean) {
        if (queFormBean.getRelatedpropertyname() != null && SharedStructureData.relatedPropertyHashTable.containsKey(queFormBean.getRelatedpropertyname())) {
            queFormBean.setAnswer(SharedStructureData.relatedPropertyHashTable.get(queFormBean.getRelatedpropertyname()));
        }
    }
    @SuppressLint("SetTextI18n")
    public static void setDnhddHypertensionStatus(String[] split, QueFormBean queFormBean) {
        QueFormBean queFormBean1 = SharedStructureData.mapIndexQuestion.get(19);
        MaterialTextView materialTextView = (MaterialTextView) queFormBean1.getQuestionTypeView();
        String pastStatus = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.IS_EARLIER_DIAGNOSED_HYPERTENSION);

        if (queFormBean.getAnswer() != null) {
            String[] subString = queFormBean.getAnswer().toString().split("-");
            if (subString.length > 2) {
                int systolicAns = Integer.parseInt(subString[1]);
                int diastolicAns = Integer.parseInt(subString[2]);
                if (pastStatus != null && pastStatus.equals("1")) {
                    if ((systolicAns >= 140) || (diastolicAns >= 90)) {
                        materialTextView.setText("Uncontrolled Hypertension");
                        queFormBean1.setAnswer("uncontrolled");
                        materialTextView.setTextColor(ContextCompat.getColor(context, R.color.pregnantWomenTextColor));
                    } else {
                        materialTextView.setText("Controlled Hypertension");
                        queFormBean1.setAnswer("controlled");
                        materialTextView.setTextColor(ContextCompat.getColor(context, R.color.hofTextColor));
                    }
                } else {
                    if ((systolicAns >= 140) || (diastolicAns >= 90)) {
                        materialTextView.setText("Suspected Hypertension");
                        queFormBean1.setAnswer("suspected");
                        materialTextView.setTextColor(ContextCompat.getColor(context, R.color.pregnantWomenTextColor));
                    } else {
                        materialTextView.setText("NORMAL");
                        queFormBean1.setAnswer("normal");
                        materialTextView.setTextColor(ContextCompat.getColor(context, R.color.hofTextColor));
                    }
                }
            }
        }
    }
    @SuppressLint("SetTextI18n")
    public static void setDnhddDiabetesStatus(String[] split, QueFormBean queFormBean) {
        QueFormBean queFormBean1 = SharedStructureData.mapIndexQuestion.get(15);
        MaterialTextView materialTextView = (MaterialTextView) queFormBean1.getQuestionTypeView();
        String pastStatus = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.IS_EARLIER_DIAGNOSED_DIABETES);

        if (split.length == 5) {
            String measurementType = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            if (measurementType != null) {
                if (measurementType.equals("FBS")) {
                    String answerString = queFormBean != null && queFormBean.getAnswer() != null ? queFormBean.getAnswer().toString() : "";
                    String splitValue = split[2];
                    if (!answerString.isEmpty() && !splitValue.isEmpty()) {
                        if (Integer.parseInt(answerString) > Integer.parseInt(splitValue)) {
                            if (pastStatus != null && pastStatus.equals("1")) {
                                materialTextView.setText("Uncontrolled Diabetes");
                                queFormBean1.setAnswer("uncontrolled");
                                if (queFormBean1.getRelatedpropertyname() != null && !queFormBean1.getRelatedpropertyname().isEmpty()) {
                                    SharedStructureData.relatedPropertyHashTable.put(queFormBean1.getRelatedpropertyname(), "uncontrolled");
                                }
                            } else {
                                materialTextView.setText("Suspected Diabetes");
                                queFormBean1.setAnswer("suspected");
                                if (queFormBean1.getRelatedpropertyname() != null && !queFormBean1.getRelatedpropertyname().isEmpty()) {
                                    SharedStructureData.relatedPropertyHashTable.put(queFormBean1.getRelatedpropertyname(), "suspected");
                                }
                            }

                        } else {
                            if (pastStatus != null && pastStatus.equals("1")) {
                                materialTextView.setText("Controlled Diabetes");
                                queFormBean1.setAnswer("controlled");
                                if (queFormBean1.getRelatedpropertyname() != null && !queFormBean1.getRelatedpropertyname().isEmpty()) {
                                    SharedStructureData.relatedPropertyHashTable.put(queFormBean1.getRelatedpropertyname(), "controlled");
                                }
                            } else {
                                materialTextView.setText("Normal");
                                queFormBean1.setAnswer("normal");
                                if (queFormBean1.getRelatedpropertyname() != null && !queFormBean1.getRelatedpropertyname().isEmpty()) {
                                    SharedStructureData.relatedPropertyHashTable.put(queFormBean1.getRelatedpropertyname(), "normal");
                                }
                            }
                        }
                    }
                } else if (measurementType.equals("PP2BS")) {
                    String answerString = queFormBean != null && queFormBean.getAnswer() != null ? queFormBean.getAnswer().toString() : "";
                    String splitValue = split[3];
                    if (!answerString.isEmpty() && !splitValue.isEmpty()) {
                        if (Integer.parseInt(answerString) > Integer.parseInt(splitValue)) {
                            if (pastStatus != null && pastStatus.equals("1")) {
                                materialTextView.setText("Uncontrolled Diabetes");
                                queFormBean1.setAnswer("uncontrolled");
                                if (queFormBean1.getRelatedpropertyname() != null && !queFormBean1.getRelatedpropertyname().isEmpty()) {
                                    SharedStructureData.relatedPropertyHashTable.put(queFormBean1.getRelatedpropertyname(), "uncontrolled");
                                }
                            } else {
                                materialTextView.setText("Suspected Diabetes");
                                queFormBean1.setAnswer("suspected");
                                if (queFormBean1.getRelatedpropertyname() != null && !queFormBean1.getRelatedpropertyname().isEmpty()) {
                                    SharedStructureData.relatedPropertyHashTable.put(queFormBean1.getRelatedpropertyname(), "suspected");
                                }
                            }

                        } else {
                            if (pastStatus != null && pastStatus.equals("1")) {
                                materialTextView.setText("Controlled Diabetes");
                                queFormBean1.setAnswer("controlled");
                                if (queFormBean1.getRelatedpropertyname() != null && !queFormBean1.getRelatedpropertyname().isEmpty()) {
                                    SharedStructureData.relatedPropertyHashTable.put(queFormBean1.getRelatedpropertyname(), "controlled");
                                }
                            } else {
                                materialTextView.setText("Normal");
                                queFormBean1.setAnswer("normal");
                                if (queFormBean1.getRelatedpropertyname() != null && !queFormBean1.getRelatedpropertyname().isEmpty()) {
                                    SharedStructureData.relatedPropertyHashTable.put(queFormBean1.getRelatedpropertyname(), "normal");
                                }
                            }
                        }
                    }
                } else {
                    String answerString = queFormBean != null && queFormBean.getAnswer() != null ? queFormBean.getAnswer().toString() : "";
                    String splitValue = split[4];
                    if (!answerString.isEmpty() && !splitValue.isEmpty()) {
                        if (Integer.parseInt(answerString) > Integer.parseInt(splitValue)) {
                            if (pastStatus != null && pastStatus.equals("1")) {
                                materialTextView.setText("Uncontrolled Diabetes");
                                queFormBean1.setAnswer("uncontrolled");
                                if (queFormBean1.getRelatedpropertyname() != null && !queFormBean1.getRelatedpropertyname().isEmpty()) {
                                    SharedStructureData.relatedPropertyHashTable.put(queFormBean1.getRelatedpropertyname(), "uncontrolled");
                                }
                            } else {
                                materialTextView.setText("Suspected Diabetes");
                                queFormBean1.setAnswer("suspected");
                                if (queFormBean1.getRelatedpropertyname() != null && !queFormBean1.getRelatedpropertyname().isEmpty()) {
                                    SharedStructureData.relatedPropertyHashTable.put(queFormBean1.getRelatedpropertyname(), "suspected");
                                }
                            }

                        } else {
                            if (pastStatus != null && pastStatus.equals("1")) {
                                materialTextView.setText("Controlled Diabetes");
                                queFormBean1.setAnswer("controlled");
                                if (queFormBean1.getRelatedpropertyname() != null && !queFormBean1.getRelatedpropertyname().isEmpty()) {
                                    SharedStructureData.relatedPropertyHashTable.put(queFormBean1.getRelatedpropertyname(), "controlled");
                                }
                            } else {
                                materialTextView.setText("Normal");
                                queFormBean1.setAnswer("normal");
                                if (queFormBean1.getRelatedpropertyname() != null && !queFormBean1.getRelatedpropertyname().isEmpty()) {
                                    SharedStructureData.relatedPropertyHashTable.put(queFormBean1.getRelatedpropertyname(), "normal");
                                }
                            }

                        }
                    }
                }
            }
        }
    }
    public static void calculateOutComeDate(QueFormBean queFormBean, boolean isValid) {
        if (queFormBean.getAnswer() != null && queFormBean.getAnswer().toString().trim().length() > 0) {
            Calendar dateSubmitted = Calendar.getInstance();
            try {
                dateSubmitted.setTimeInMillis(Long.parseLong(queFormBean.getAnswer().toString()));
            } catch (NumberFormatException e) {
                Log.e(TAG, null, e);
            }
            UtilBean.clearTimeFromDate(dateSubmitted);

            if (isValid) {
                int[] ageArray = UtilBean.calculateAgeYearMonthDay(dateSubmitted.getTime().getTime());
                SharedStructureData.relatedPropertyHashTable.put("dobOfChild", String.valueOf(dateSubmitted.getTime().getTime()));
                if (ageArray.length == 3) {
                    SharedStructureData.relatedPropertyHashTable.put("childDOB", ageArray[0] + GlobalTypes.KEY_VALUE_SEPARATOR + ageArray[1] + GlobalTypes.KEY_VALUE_SEPARATOR + ageArray[2]);
                }
            } else {
                SharedStructureData.relatedPropertyHashTable.put("dobOfChild", String.valueOf(0));
            }
        }
    }

    public static void setSubtitleColor(String[] split, QueFormBean queFormBean) {
        try {
            String color = split[split.length - 1];
            if (queFormBean.getSubTitleView() != null) {
                if (color.equalsIgnoreCase(MorbiditiesConstant.RED_COLOR)) {
                    queFormBean.getSubTitleView().setTextColor(SewaUtil.COLOR_RED);
                } else if (color.equalsIgnoreCase(MorbiditiesConstant.YELLOW_COLOR)) {
                    queFormBean.getSubTitleView().setTextColor(SewaUtil.COLOR_YELLOW);
                } else if (color.equalsIgnoreCase(MorbiditiesConstant.GREEN_COLOR)) {
                    queFormBean.getSubTitleView().setTextColor(SewaUtil.COLOR_GREEN);
                }
            }
        } catch (Exception e) {
            Log.e("Error", "At Question id : " + queFormBean.getId() + " \n Sub title : " + queFormBean.getSubtitle() + "\n SubTitle View : " + queFormBean.getSubTitleView());
        }
    }

    public static void setColor(String[] split, QueFormBean queFormBean) {
        // like  ::: setColor-A-propertyName-op1=R+op2=Y+op3=G
        // e.g   ::: formulaName - whereToApply - value or property (possibility value)
        TextView targetView = null;
        if (split.length >= 2) {
            if (split[1].trim().equalsIgnoreCase("T")) {   // if color for Title View
                targetView = queFormBean.getTitleView();
            } else if (split[1].trim().equalsIgnoreCase("S")) { // if color for Sub Title View
                targetView = queFormBean.getSubTitleView();
            } else if (split[1].trim().equalsIgnoreCase("I")) { // if color for Instruction View
                targetView = queFormBean.getInstructionsView();
            } else if (split[1].trim().equalsIgnoreCase("Q")) { // if color for Question View
                targetView = queFormBean.getQuestionView();
            } else if (split[1].trim().equalsIgnoreCase("A")) { // if color for Answer View
                targetView = (queFormBean.getQuestionTypeView() instanceof TextView ? (TextView) queFormBean.getQuestionTypeView() : null);
            }
            if (targetView != null) {
                if (split.length == 3) { // direct value given to color
                    SewaUtil.setColor(targetView, split[2].trim());
                } else if (split.length == 4) { // related property given
                    String defaultValue = GlobalTypes.NOT_AVAILABLE;
                    if (split[2] != null) {
                        String relatedPropertyName = split[2].trim();
                        if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                            relatedPropertyName += queFormBean.getLoopCounter();
                        }
                        defaultValue = SharedStructureData.relatedPropertyHashTable.get(relatedPropertyName);
                        if (defaultValue == null || defaultValue.trim().length() == 0 || defaultValue.trim().equalsIgnoreCase("null")) {
                            defaultValue = GlobalTypes.NOT_AVAILABLE;
                        } else {
                            queFormBean.setAnswer(defaultValue);
                        }
                    }
                    if (split[3] != null) {
                        List<String> options = Arrays.asList(UtilBean.split(split[3].trim().toLowerCase(), "+"));
                        if (!options.isEmpty() && !defaultValue.equalsIgnoreCase(GlobalTypes.NOT_AVAILABLE)) {
                            targetView.setText(UtilBean.getMyLabel(defaultValue));
                            for (String string : options) {
                                if (string.contains(defaultValue.trim().toLowerCase() + "=")) {
                                    String[] split1 = UtilBean.split(string, "=");
                                    if (split1.length > 2) {
                                        SewaUtil.setColor(targetView, split1[1]);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void setDateSet(String[] split, QueFormBean queFormBean) {
        if (queFormBean.getAnswer() != null && queFormBean.getAnswer().toString().trim().length() > 0 && split.length >= 2) {
            SharedStructureData.relatedPropertyHashTable.put(split[1].trim(), queFormBean.getAnswer().toString());
        }
    }

    public static void setHint(String[] split, QueFormBean queFormBean) {
        if (split.length > 1) {
            EditText editText;
            try {
                editText = (EditText) queFormBean.getQuestionTypeView();
                String hintText = split[2];
                if (split[1].equalsIgnoreCase("P")) { // set hint from property (it will find value of property)
                    if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                        hintText += queFormBean.getLoopCounter();
                    }
                    hintText = SharedStructureData.relatedPropertyHashTable.get(hintText);
                } else if (split[1].equalsIgnoreCase("D")) {
                    hintText = split[2];  // // set hint Direct  value
                }
                if (hintText != null && hintText.trim().length() > 0 && !hintText.equalsIgnoreCase("null")) {
                    editText.setHint(hintText);
                    if (queFormBean.getAnswer() == null || queFormBean.getAnswer().toString().trim().length() == 0
                            && split.length >= 9 && split[3] != null && split[3].trim().equalsIgnoreCase("startswith")) {
                        if (hintText.trim().toLowerCase().startsWith(split[4].toLowerCase())) { // if true
                            if (split[5] != null && split[5].trim().equalsIgnoreCase("editable")) {
                                editText.setEnabled(split[6] != null && split[6].trim().equalsIgnoreCase(GlobalTypes.TRUE));
                            }
                        } else {
                            if (split[7] != null && split[7].trim().equalsIgnoreCase("editable")) {
                                editText.setEnabled(split[8] != null && split[8].trim().equalsIgnoreCase(GlobalTypes.TRUE));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, null, e);
            }
        }
    }

    public static void setValueAsProperty(QueFormBean queFormBean) {
        if (queFormBean.getAnswer() != null
                && queFormBean.getAnswer().toString().trim().length() > 0
                && !queFormBean.getAnswer().toString().trim().equalsIgnoreCase("null")) {
            String property = queFormBean.getRelatedpropertyname().trim();
            if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                property += queFormBean.getLoopCounter();
            }

            List<OptionDataBean> optionsOrDataMap = UtilBean.getOptionsOrDataMap(queFormBean, false);
            OptionDataBean optionDataBean = new OptionDataBean();
            optionDataBean.setKey(queFormBean.getAnswer().toString().trim());
            int indexOf = optionsOrDataMap.indexOf(optionDataBean);
            if (indexOf > -1) {
                optionDataBean = optionsOrDataMap.get(indexOf);
            }
            SharedStructureData.relatedPropertyHashTable.put(property, optionDataBean.getValue());
        }
    }

    public static void setProperty(String[] split, QueFormBean queFormBean) {

        if (queFormBean.getAnswer() != null
                && queFormBean.getAnswer().toString().trim().length() > 0
                && !queFormBean.getAnswer().toString().trim().equalsIgnoreCase("null")) {

            boolean flag = queFormBean.getLoopCounter() <= 0 || queFormBean.isIgnoreLoop();
            String answer;
            Long weightDate = null;
            String dataMap = queFormBean.getDatamap();
            if (dataMap != null) {
                String[] dataMapSplit = UtilBean.split(dataMap, GlobalTypes.KEY_VALUE_SEPARATOR);
                if (dataMapSplit.length > 3) {
                    int index;
                    try {
                        index = Integer.parseInt(dataMapSplit[3].trim());
                    } catch (Exception e) {
                        index = 0;
                    }
                    String[] temp = UtilBean.split(queFormBean.getAnswer().toString().trim(), dataMapSplit[2].trim());
                    if (temp.length > index) {
                        answer = temp[index];
                    } else {
                        answer = queFormBean.getAnswer().toString().trim();
                    }
                    if (temp.length > 1 && temp[1] != null && !temp[1].equalsIgnoreCase("null")) {
                        weightDate = Long.parseLong(temp[1]);
                    }
                } else {
                    answer = queFormBean.getAnswer().toString().trim();
                }
            } else {
                answer = queFormBean.getAnswer().toString().trim();
            }

            if (answer != null) {
                String property = null;
                if (queFormBean.getRelatedpropertyname() != null && queFormBean.getRelatedpropertyname().trim().length() > 0) {
                    property = queFormBean.getRelatedpropertyname().trim();

                } else if (split.length > 1 && split[1] != null && !split[1].equalsIgnoreCase(FormulaConstants.FORMULA_SET_DEFAULT_PROPERTY)) {
                    property = split[1];
                }
                if (property != null) {
                    SharedStructureData.relatedPropertyHashTable.put(flag ? property : property + queFormBean.getLoopCounter(), answer);
                }

                if (split.length > 2 && split[2].equalsIgnoreCase("CheckWeight")) {
                    String checkWeight;
                    checkWeight = UtilBean.checkWeightForPNCMorbidity(queFormBean.getQuestion(), answer, queFormBean.getLoopCounter() + "");
                    if (checkWeight != null) {
                        if (!checkWeight.equalsIgnoreCase(GlobalTypes.PRETERM_DELIVERY)) {
                            SharedStructureData.addItemInLICList(queFormBean.getQuestion(), queFormBean.getAnswer().toString(), queFormBean.getLoopCounter() + "");
                        }
                        if (checkWeight.equalsIgnoreCase(GlobalTypes.PRETERM_DELIVERY_N_WEIGHT) || checkWeight.equalsIgnoreCase(GlobalTypes.PRETERM_DELIVERY)) {
                            SharedStructureData.addItemInLICList("Delivery", GlobalTypes.PRETERM_DELIVERY, queFormBean.getLoopCounter() + "");
                        }
                        if (checkWeight.equalsIgnoreCase(GlobalTypes.PRETERM_DELIVERY_N_WEIGHT) || checkWeight.equalsIgnoreCase(answer)) {
                            LinearLayout mainLayout = (LinearLayout) queFormBean.getQuestionTypeView();
                            Spinner weightSpinner = mainLayout.findViewById(IdConstants.WEIGHT_BOX_SPINNER_KILO_ID);
                            SewaUtil.setColor((TextView) weightSpinner.getSelectedView(), MorbiditiesConstant.GREEN_COLOR);
                            weightSpinner = mainLayout.findViewById(IdConstants.WEIGHT_BOX_SPINNER_GRAM_ID);
                            SewaUtil.setColor((TextView) weightSpinner.getSelectedView(), MorbiditiesConstant.GREEN_COLOR);
                        }
                        if (checkWeight.equalsIgnoreCase(GlobalTypes.PRETERM_DELIVERY)) {
                            SharedStructureData.removeItemFromLICList(queFormBean.getQuestion(), queFormBean.getLoopCounter() + "");
                            LinearLayout mainLayout = (LinearLayout) queFormBean.getQuestionTypeView();
                            Spinner weightSpinner = mainLayout.findViewById(IdConstants.WEIGHT_BOX_SPINNER_KILO_ID);
                            SewaUtil.setColor((TextView) weightSpinner.getSelectedView(), MorbiditiesConstant.NO_COLOR_FULL);
                            weightSpinner = mainLayout.findViewById(IdConstants.WEIGHT_BOX_SPINNER_GRAM_ID);
                            SewaUtil.setColor((TextView) weightSpinner.getSelectedView(), MorbiditiesConstant.NO_COLOR_FULL);
                        }
                    } else {
                        UtilBean.addLBWorAsphyxiaIntoLICForFirstPNC(queFormBean.getLoopCounter() + "");
                        SharedStructureData.removeItemFromLICList(queFormBean.getQuestion(), queFormBean.getLoopCounter() + "");
                        SharedStructureData.removeItemFromLICList("Delivery", queFormBean.getLoopCounter() + "");
                        LinearLayout mainLayout = (LinearLayout) queFormBean.getQuestionTypeView();
                        Spinner weightSpinner = mainLayout.findViewById(IdConstants.WEIGHT_BOX_SPINNER_KILO_ID);
                        SewaUtil.setColor((TextView) weightSpinner.getSelectedView(), MorbiditiesConstant.NO_COLOR_FULL);
                        weightSpinner = mainLayout.findViewById(IdConstants.WEIGHT_BOX_SPINNER_GRAM_ID);
                        SewaUtil.setColor((TextView) weightSpinner.getSelectedView(), MorbiditiesConstant.NO_COLOR_FULL);
                    }
                }

                if (split.length > 2 && split[2].equalsIgnoreCase("Malnutrition")) {

                    String latestWeight = null;
                    if (!answer.equalsIgnoreCase(GlobalTypes.NO_WEIGHT)) {
                        latestWeight = answer;
                    }
                    String gender = SharedStructureData.relatedPropertyHashTable.get(flag ? "gender" : "gender" + queFormBean.getLoopCounter());
                    String age1 = SharedStructureData.relatedPropertyHashTable.get(flag ? "age" : "age" + queFormBean.getLoopCounter());
                    Integer age = null;
                    if (age1 != null && !age1.equalsIgnoreCase("null")) {
                        try {
                            age = Integer.valueOf(age1);
                        } catch (Exception e) {
                            Log.e(TAG, null, e);
                        }
                    }
                    if (weightDate != null) {
                        Calendar maxPrevDate = Calendar.getInstance();
                        maxPrevDate.add(Calendar.MONTH, -1);
                        UtilBean.clearTimeFromDate(maxPrevDate);
                        Calendar weightTaken = Calendar.getInstance();
                        weightTaken.setTimeInMillis(weightDate);
                        UtilBean.clearTimeFromDate(weightTaken);
                        if (maxPrevDate.equals(weightTaken) && age != null) {
                            age--;
                        }
                    }
                    if (age == null) {
                        String ageFromECR = SharedStructureData.relatedPropertyHashTable.get(flag ? "ageFromECR" : "ageFromECR" + queFormBean.getLoopCounter());
                        if (ageFromECR != null && !ageFromECR.trim().equalsIgnoreCase("null")) {
                            String[] split1 = UtilBean.split(ageFromECR, GlobalTypes.KEY_VALUE_SEPARATOR);
                            if (split1.length > 1) {
                                try {
                                    age = Integer.parseInt(split1[0]) * 12 + Integer.parseInt(split1[1]);
                                } catch (NumberFormatException e) {
                                    Log.e(TAG, null, e);
                                }
                            }
                        }
                    }
                    String setValue = null;
                    if (age != null && gender != null
                            && latestWeight != null && latestWeight.trim().length() > 0 && !latestWeight.trim().equalsIgnoreCase("null")) {
                        setValue = UtilBean.findMalnutritionGrade(gender, age, Float.parseFloat(latestWeight));
                    }
                    if (setValue == null) {
                        setValue = GlobalTypes.MALNUTRITION_GRADE_NOT_AVAILABLE;
                    }
                    SharedStructureData.relatedPropertyHashTable.put("malnutritionGrade", setValue);
                    int updateQuestion;
                    try {
                        updateQuestion = Integer.parseInt(split[3]);
                    } catch (Exception e) {
                        updateQuestion = 0;
                    }
                    if (!flag) {
                        updateQuestion = DynamicUtils.getLoopId(updateQuestion, queFormBean.getLoopCounter());
                    }
                    QueFormBean setterQuestion = SharedStructureData.mapIndexQuestion.get(updateQuestion);
                    if (setterQuestion != null) {
                        TextView textView = (TextView) setterQuestion.getQuestionTypeView();
                        textView.setText(UtilBean.getMyLabel(setValue));
                        SewaUtil.setColor(textView, setValue);
                        if (!setValue.equalsIgnoreCase(GlobalTypes.MALNUTRITION_GRADE_NOT_AVAILABLE)) {
                            textView.setTypeface(Typeface.DEFAULT_BOLD);
                            setterQuestion.setAnswer(setValue);
                        } else {
                            textView.setTypeface(Typeface.DEFAULT);
                            setterQuestion.setAnswer(null);
                        }
                    }
                }
                if (split.length == 8 && split[5].equalsIgnoreCase("improved")) {
                    String latestWeight = SharedStructureData.relatedPropertyHashTable.get(flag ? split[1] : split[1] + queFormBean.getLoopCounter());
                    String setValue;

                    String prevWeight = SharedStructureData.relatedPropertyHashTable.get(flag ? split[7] : split[7] + queFormBean.getLoopCounter());
                    if (latestWeight != null
                            && prevWeight != null
                            && latestWeight.trim().length() > 0
                            && prevWeight.trim().length() > 0
                            && !latestWeight.trim().equalsIgnoreCase("null")
                            && !prevWeight.trim().equalsIgnoreCase("null")) {
                        float diff;
                        try {
                            diff = Float.parseFloat(latestWeight) - Float.parseFloat(prevWeight);
                        } catch (Exception e) {
                            diff = -111;
                        }
                        if (diff > 0) {
                            setValue = "Yes";
                        } else if (diff == -111) {
                            setValue = UtilBean.getMyLabel(GlobalTypes.NOT_AVAILABLE);
                        } else {
                            setValue = "No";
                        }
                    } else {
                        setValue = UtilBean.getMyLabel(GlobalTypes.NOT_AVAILABLE);
                    }
                    SharedStructureData.relatedPropertyHashTable.put("isChildWeightImprove", setValue);
                    int updateQuestion = Integer.parseInt(split[6]);
                    if (!flag) {
                        updateQuestion = DynamicUtils.getLoopId(updateQuestion, queFormBean.getLoopCounter());
                    }
                    QueFormBean setterQuestion = SharedStructureData.mapIndexQuestion.get(updateQuestion);
                    if (setterQuestion != null) {
                        TextView textView = (TextView) setterQuestion.getQuestionTypeView();
                        textView.setText(UtilBean.getMyLabel(setValue));
                        setterQuestion.setAnswer(setValue);
                    }
                }
            }
        }
    }

    public static void setDefaultProperty(String[] split, QueFormBean queFormBean) {
        boolean flag = queFormBean.getLoopCounter() <= 0 || queFormBean.isIgnoreLoop();
        if (queFormBean.getAnswer() != null) {
            String property = null;
            if (queFormBean.getRelatedpropertyname() != null && queFormBean.getRelatedpropertyname().trim().length() > 0) {
                property = queFormBean.getRelatedpropertyname().trim();

            } else if (split.length > 1 && split[1] != null) {
                property = split[1];
            }
            if (property != null) {
                SharedStructureData.relatedPropertyHashTable.put(flag ? property : property + queFormBean.getLoopCounter(), queFormBean.getAnswer().toString());
            }
        }
    }

    public static void getTime(String[] split) {
        if (split.length > 1 && split[1] != null) {
            QueFormBean question = SharedStructureData.mapIndexQuestion.get(Integer.parseInt(split[1]));
            if (question != null) {
                Date interviewEndDuration = new Date();
                question.setAnswer(interviewEndDuration.getTime());
                SimpleDateFormat sdf = new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT + " hh:mm:ss", Locale.getDefault());
                TextView label = (TextView) question.getQuestionTypeView();
                if (label != null) {
                    label.setText(sdf.format(interviewEndDuration));
                }
            }
        }
    }

    public static void fillLocation(String[] split, QueFormBean queFormBean) {
        if (split.length > 1 && split[1] != null && queFormBean.getAnswer() != null) {
            Integer answer = Integer.parseInt((String) queFormBean.getAnswer());
            SharedStructureData.sewaService.retrieveLocationByParent(answer);
            List<FieldValueMobDataBean> allLocationByParent = SharedStructureData.mapDataMapLabelBean.get(String.valueOf(answer));
            List<String> stringOptions = new ArrayList<>();
            List<OptionTagBean> allLocOptions = new ArrayList<>();
            if (allLocationByParent != null) {
                stringOptions.add(UtilBean.getMyLabel(GlobalTypes.SELECT));
                for (FieldValueMobDataBean locBean : allLocationByParent) {
                    OptionTagBean option = new OptionTagBean();
                    option.setKey("" + locBean.getIdOfValue());
                    option.setValue(locBean.getValue());
                    allLocOptions.add(option);
                    stringOptions.add(UtilBean.getMyLabel(locBean.getValue()));
                }
            } else {
                OptionTagBean option = new OptionTagBean();
                option.setKey("" + -1);
                option.setValue("No option available");
                allLocOptions.add(option);
                stringOptions.add(UtilBean.getMyLabel("No option available"));
            }

            QueFormBean nextQuestion = SharedStructureData.mapIndexQuestion.get(Integer.parseInt(split[1]));
            if (nextQuestion != null) {
                Spinner spin = (Spinner) nextQuestion.getQuestionTypeView();
                if (nextQuestion.getAnswer() != null) {
                    boolean adapterValueChanged = true;
                    for (OptionTagBean newOption : allLocOptions) {
                        if (newOption.getKey().equals(nextQuestion.getAnswer())) {
                            adapterValueChanged = false;
                            break;
                        }
                    }
                    if (adapterValueChanged) {
                        spin.setAdapter(createAdapter(stringOptions));
                        nextQuestion.setOptions(allLocOptions);
                    }
                } else {
                    spin.setAdapter(createAdapter(stringOptions));
                    nextQuestion.setOptions(allLocOptions);
                }
            }
        }
    }

    public static void displayAge(String[] split, QueFormBean queFormBean) {
        String selectedDate;
        int questionIdForUpdate;
        if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
            questionIdForUpdate = DynamicUtils.getLoopId(Integer.parseInt(split[1]), queFormBean.getLoopCounter());
            selectedDate = SharedStructureData.relatedPropertyHashTable.get("dob" + queFormBean.getLoopCounter());
        } else {
            questionIdForUpdate = Integer.parseInt(split[1]);
            selectedDate = SharedStructureData.relatedPropertyHashTable.get("dob");
        }
        QueFormBean setterQuestion = SharedStructureData.mapIndexQuestion.get(questionIdForUpdate);

        if (setterQuestion != null) {
            TextView textView = (TextView) setterQuestion.getQuestionTypeView();
            if (textView != null) {
                if (selectedDate != null) {
                    int[] ageYearMonthDayArray = UtilBean.calculateAgeYearMonthDayOnGivenDate(new Date(Long.parseLong(selectedDate)).getTime(), new Date().getTime());
                    String ageDisplay = UtilBean.getAgeDisplay(ageYearMonthDayArray[0], ageYearMonthDayArray[1], ageYearMonthDayArray[2]);
                    textView.setText(UtilBean.getMyLabel(ageDisplay));
                    setterQuestion.setAnswer(ageDisplay);
                } else {
                    textView.setText(UtilBean.getMyLabel(LabelConstants.DATE_NOT_YET_SELECTED));
                    setterQuestion.setAnswer(LabelConstants.DATE_NOT_YET_SELECTED);
                }
            }
        }
        SharedStructureData.relatedPropertyHashTable.put("ageAsPerDate", "Age here");
    }

    public static void setPropertyFromScannedAadhar(String[] split, QueFormBean queFormBean) {
        SharedStructureData.isAadharNumberScanned = Boolean.FALSE;
        SharedStructureData.isDobScannedFromAadhar = Boolean.FALSE;

        if (queFormBean.getAnswer() == null) {
            return;
        }

        Map<String, String> aadharDataMap = new HashMap<>();
        try {
            aadharDataMap = new Gson().fromJson(queFormBean.getAnswer().toString(), new TypeToken<Map<String, String>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (aadharDataMap.isEmpty()) {
            return;
        }

        setAadharScannedDataInRelatedPropertyHashtable(aadharDataMap, queFormBean.getLoopCounter());

        //Both values are static values, need to be changed if queId is changed in the Xls file - just for FHS Qr Code Scanning
        int questionIdForAadhar = 97;
        int questionIdForDob = 38;
        int questionIdForAgeDisplay = 39;
        int questionIdForFirstName = 32;
        int questionIdForMiddleName = 33;
        int questionIdForLastName = 34;
        int questionIdForGender = 36;

        if (split.length > 1) {
            questionIdForAadhar = Integer.parseInt(split[1]);
            questionIdForDob = Integer.parseInt(split[2]);
            questionIdForAgeDisplay = Integer.parseInt(split[3]);
            questionIdForFirstName = Integer.parseInt(split[4]);
            questionIdForMiddleName = Integer.parseInt(split[5]);
            questionIdForLastName = Integer.parseInt(split[6]);
            questionIdForGender = Integer.parseInt(split[7]);
        }

        questionIdForAadhar = DynamicUtils.getLoopId(questionIdForAadhar, queFormBean.getLoopCounter());
        questionIdForDob = DynamicUtils.getLoopId(questionIdForDob, queFormBean.getLoopCounter());
        questionIdForAgeDisplay = DynamicUtils.getLoopId(questionIdForAgeDisplay, queFormBean.getLoopCounter());

        System.out.println("??????????? questionIdForAadhar : " + questionIdForAadhar);
        System.out.println("??????????? questionIdForDob : " + questionIdForDob);
        System.out.println("??????????? questionIdForAgeDisplay : " + questionIdForAgeDisplay);
        System.out.println("??????????? questionIdForFirstName : " + questionIdForFirstName);
        System.out.println("??????????? questionIdForMiddleName : " + questionIdForMiddleName);
        System.out.println("??????????? questionIdForLastName : " + questionIdForLastName);
        System.out.println("??????????? questionIdForGender : " + questionIdForGender);

        setAadharNumberFromAadharScanned(questionIdForAadhar, aadharDataMap);
        setDobDataFromAadharScanned(questionIdForDob, questionIdForAgeDisplay, aadharDataMap);
        setNameFromAadharScanned(questionIdForFirstName, questionIdForMiddleName, questionIdForLastName, aadharDataMap);
        setGenderFromAadharScanned(questionIdForGender, aadharDataMap);
    }

    private static void setAadharScannedDataInRelatedPropertyHashtable(Map<String, String> aadharDataMap, int loopCounter) {
        SharedStructureData.relatedPropertyHashTable.put(
                UtilBean.getRelatedPropertyNameWithLoopCounter("aadharScannedData", loopCounter),
                new Gson().toJson(aadharDataMap)
        );

        String uid = aadharDataMap.get("uid");
        if (uid != null && !uid.isEmpty()) {
            SharedStructureData.relatedPropertyHashTable.put(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.AADHAR_NUMBER, loopCounter),
                    uid
            );
        }

        String dobString = aadharDataMap.get("dob");
        Date dob = AadharScanUtil.formatDate(dobString);
        if (dob != null) {
            SharedStructureData.relatedPropertyHashTable.put(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.DOB, loopCounter),
                    Long.toString(dob.getTime())
            );

            int[] ageYearMonthDayArray = UtilBean.calculateAgeYearMonthDayOnGivenDate(dob.getTime(), new Date().getTime());
            String ageDisplay = UtilBean.getAgeDisplay(ageYearMonthDayArray[0], ageYearMonthDayArray[1], ageYearMonthDayArray[2]);
            SharedStructureData.relatedPropertyHashTable.put(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.AGE_AS_PER_DATE, loopCounter),
                    Objects.requireNonNull(ageDisplay)
            );
        }

        String gender = aadharDataMap.get("gender");
        if (gender != null && !gender.isEmpty()) {
            SharedStructureData.relatedPropertyHashTable.put(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.ANS_12, loopCounter),
                    AadharScanUtil.formatGender(gender)
            );
        }

        String name = aadharDataMap.get("name");
        if (name != null) {
            String[] splitName = name.split(" ");
            if (splitName.length > 2) {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.FIRST_NAME, loopCounter),
                        splitName[0]
                );
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.MIDDLE_NAME, loopCounter),
                        splitName[1]
                );
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.LAST_NAME, loopCounter),
                        splitName[2]
                );
            } else if (splitName.length >1) {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.FIRST_NAME, loopCounter),
                        splitName[0]
                );
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.LAST_NAME, loopCounter),
                        splitName[1]
                );
            } else {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.FIRST_NAME, loopCounter),
                        splitName[0]
                );
            }
        }
    }

    private static void setAadharNumberFromAadharScanned(int questionIdForAadhar, Map<String, String> aadharDataMap) {
        String uid = aadharDataMap.get("uid");
        if (uid == null || uid.isEmpty()) {
            return;
        }

        SharedStructureData.isAadharNumberScanned = Boolean.TRUE;
        QueFormBean aadharQuestion = SharedStructureData.mapIndexQuestion.get(questionIdForAadhar);

        if (aadharQuestion == null) {
            return;
        }

        LinearLayout mainLayout = (LinearLayout) aadharQuestion.getQuestionTypeView();
        CheckBox checkBox = (CheckBox) mainLayout.getChildAt(0);
        checkBox.setChecked(false);
        checkBox.setClickable(false);
        LinearLayout inputLayout = (LinearLayout) mainLayout.getChildAt(1);
        TextInputLayout textInputLayout = (TextInputLayout) inputLayout.getChildAt(0);
        textInputLayout.setFocusable(false);
        EditText editText = textInputLayout.getEditText();
        if (editText != null) {
            editText.setText(uid);
        }
        aadharQuestion.setAnswer(uid);
    }

    private static void setDobDataFromAadharScanned(int questionIdForDob, int questionIdForAgeDisplay, Map<String, String> aadharDataMap) {
        if (!aadharDataMap.containsKey("dob")) {
            return;
        }

        String dobString = aadharDataMap.get("dob");
        if (dobString == null || dobString.isEmpty()) {
            return;
        }

        Date dob = AadharScanUtil.formatDate(dobString);
        if (dob == null) {
            return;
        }

        SharedStructureData.isDobScannedFromAadhar = Boolean.TRUE;

        QueFormBean dobQuestion = SharedStructureData.mapIndexQuestion.get(questionIdForDob);
        if (dobQuestion == null) {
            return;
        }

        LinearLayout dobQuestionTypeView = (LinearLayout) Objects.requireNonNull(dobQuestion).getQuestionTypeView();
        TextView textViewDob = (TextView) dobQuestionTypeView.getChildAt(0);
        textViewDob.setText(new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault()).format(dob.getTime()));
        dobQuestionTypeView.setClickable(false);
        dobQuestionTypeView.setOnClickListener(null);
        dobQuestion.setAnswer(dob.getTime());

        QueFormBean ageDisplayQuestion = SharedStructureData.mapIndexQuestion.get(questionIdForAgeDisplay);
        if (ageDisplayQuestion == null) {
            return;
        }

        TextView ageDisplayTextView = (TextView) ageDisplayQuestion.getQuestionTypeView();
        int[] ageYearMonthDayArray = UtilBean.calculateAgeYearMonthDayOnGivenDate(dob.getTime(), new Date().getTime());
        String ageDisplay = UtilBean.getAgeDisplay(ageYearMonthDayArray[0], ageYearMonthDayArray[1], ageYearMonthDayArray[2]);
        ageDisplayTextView.setText(UtilBean.getMyLabel(ageDisplay));
    }

    private static void setNameFromAadharScanned(int questionIdForFirstName, int questionIdForMiddleName, int questionIdForLastName, Map<String, String> aadharDataMap) {
        String name = aadharDataMap.get("name");
        if (name == null || name.isEmpty()) {
            return;
        }

        String[] splitName = name.split(" ");
        String firstName, middleName = null, lastName = null;

        if (splitName.length > 2) {
            firstName = splitName[0];
            middleName = splitName[1];
            lastName = splitName[2];
        } else if (splitName.length > 1) {
            firstName = splitName[0];
            lastName = splitName[1];
        } else {
            firstName = splitName[0];
        }

        QueFormBean firstNameQuestion = SharedStructureData.mapIndexQuestion.get(questionIdForFirstName);
        if (firstNameQuestion == null) {
            return;
        }
        TextInputLayout textInputLayout = (TextInputLayout) firstNameQuestion.getQuestionTypeView();
        textInputLayout.setFocusable(false);
        EditText editText = textInputLayout.getEditText();
        if (editText != null) {
            editText.setText(firstName);
        }
        firstNameQuestion.setAnswer(firstName);

        QueFormBean middleNameQuestion = SharedStructureData.mapIndexQuestion.get(questionIdForMiddleName);
        if (middleNameQuestion == null) {
            return;
        }
        TextInputLayout textInputLayout1 = (TextInputLayout) middleNameQuestion.getQuestionTypeView();
        textInputLayout1.setFocusable(false);
        EditText editText1 = textInputLayout1.getEditText();
        if (editText1 != null) {
            editText1.setText(middleName);
        }
        middleNameQuestion.setAnswer(middleName);

        QueFormBean lastNameQuestion = SharedStructureData.mapIndexQuestion.get(questionIdForLastName);
        if (lastNameQuestion == null) {
            return;
        }
        TextInputLayout textInputLayout2 = (TextInputLayout) lastNameQuestion.getQuestionTypeView();
        textInputLayout.setFocusable(false);
        EditText editText2 = textInputLayout2.getEditText();
        if (editText2 != null) {
            editText2.setText(lastName);
        }
        lastNameQuestion.setAnswer(lastName);
    }

    private static void setGenderFromAadharScanned(int questionIdForGender, Map<String, String> aadharDataMap) {
        String gender = aadharDataMap.get("gender");
        if (gender == null || gender.isEmpty()) {
            return;
        }

        QueFormBean genderQuestion = SharedStructureData.mapIndexQuestion.get(questionIdForGender);

        if (genderQuestion == null) {
            return;
        }

        LinearLayout mainLayout = (LinearLayout) genderQuestion.getQuestionTypeView();
        int index = 0;

        if (gender.equals("M")) {
            index = 0;
        } else if (gender.equals("F")) {
            index = 1;
        } else {
            index = 2;
        }
        RadioButton radioButton = (RadioButton) mainLayout.getChildAt(index);
        radioButton.setChecked(true);
    }

    public static void setAanganwadiIdFromPhcId(QueFormBean queFormBean) {
        if (queFormBean.getAnswer() != null) {
            Integer subCenterSelected = Integer.parseInt(queFormBean.getAnswer().toString());
            List<FieldValueMobDataBean> allLocationByParent;
            if (subCenterSelected == 0) {
                allLocationByParent = SharedStructureData.mapDataMapLabelBean.get("anganwadiList");
            } else {
                allLocationByParent = SharedStructureData.sewaFhsService.retrieveAnganwadiListFromSubcentreId(subCenterSelected);
            }
            SharedStructureData.relatedPropertyHashTable.put("defaultPhc", String.valueOf(subCenterSelected));

            List<String> stringOptions = new ArrayList<>();
            List<OptionTagBean> allLocOptions = new ArrayList<>();
            if (allLocationByParent != null && !allLocationByParent.isEmpty()) {
                stringOptions.add(UtilBean.getMyLabel(GlobalTypes.SELECT));
                for (FieldValueMobDataBean locBean : allLocationByParent) {
                    OptionTagBean option = new OptionTagBean();
                    option.setKey("" + locBean.getIdOfValue());
                    option.setValue(locBean.getValue());
                    allLocOptions.add(option);
                    stringOptions.add(UtilBean.getMyLabel(locBean.getValue()));
                }
                OptionTagBean option = new OptionTagBean();
                option.setKey("0");
                option.setValue(LabelConstants.DOES_NOT_BELONG_TO_ANGANWADI);
                allLocOptions.add(option);
            } else {
                stringOptions.add(UtilBean.getMyLabel(LabelConstants.NO_ANGANWADI_AVAILABLE));

                OptionTagBean option1 = new OptionTagBean();
                option1.setKey("0");
                option1.setValue(LabelConstants.DOES_NOT_BELONG_TO_ANGANWADI);
                allLocOptions.add(option1);
            }
            stringOptions.add(UtilBean.getMyLabel(LabelConstants.DOES_NOT_BELONG_TO_ANGANWADI));

            QueFormBean nextQuestion = SharedStructureData.mapIndexQuestion.get(Integer.parseInt(queFormBean.getNext()));
            if (nextQuestion != null) {
                Spinner spin = (Spinner) nextQuestion.getQuestionTypeView();
                spin.setAdapter(createAdapter(stringOptions));
                if (SharedStructureData.relatedPropertyHashTable.get(nextQuestion.getRelatedpropertyname()) != null) {
                    for (OptionTagBean optionTagBean : allLocOptions) {
                        if (optionTagBean.getKey().equals(SharedStructureData.relatedPropertyHashTable.get(nextQuestion.getRelatedpropertyname()))) {
                            spin.setSelection(stringOptions.indexOf(optionTagBean.getValue()));
                        }
                    }
                }
                nextQuestion.setOptions(allLocOptions);
                nextQuestion.setDatamap(null);
            }
        }
    }

    public static void displayEarlyRegistration(String[] split, QueFormBean queFormBean, boolean isValid) {
        if (queFormBean != null && queFormBean.getQuestionTypeView() != null && isValid) {

            if (split.length != 3) {
                return;
            }

            int questionIdForEarlyRegistration = Integer.parseInt(split[1]);
            int earlyRegistrationParam = Integer.parseInt(split[2]);

            String lmpDateString = SharedStructureData.relatedPropertyHashTable.get("lmpDate");
            String pregnancyRegDateString = SharedStructureData.relatedPropertyHashTable.get("curPregRegDate");

            if (pregnancyRegDateString == null) {
                return;
            }

            Date lmpDate = new Date(Long.parseLong(Objects.requireNonNull(lmpDateString)));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(lmpDate);
            calendar.add(Calendar.DATE, earlyRegistrationParam);

            if (calendar.getTime().getTime() > new Date(Long.parseLong(pregnancyRegDateString)).getTime()) {
                QueFormBean queFormBean1 = SharedStructureData.mapIndexQuestion.get(questionIdForEarlyRegistration);
                TextView textView = (TextView) Objects.requireNonNull(queFormBean1).getQuestionTypeView();
                textView.setText(UtilBean.getMyLabel("Yes"));
                queFormBean1.setAnswer("Yes");
                queFormBean1.setQuestionUIFrame(queFormBean1);
            } else {
                QueFormBean queFormBean1 = SharedStructureData.mapIndexQuestion.get(questionIdForEarlyRegistration);
                TextView textView = (TextView) Objects.requireNonNull(queFormBean1).getQuestionTypeView();
                textView.setText(UtilBean.getMyLabel("No"));
                queFormBean1.setAnswer("No");
                queFormBean1.setQuestionUIFrame(queFormBean1);
            }

        }
    }

    public static void resetLoopParams() {
        SharedStructureData.isAadharNumberScanned = Boolean.FALSE;
        SharedStructureData.isDobScannedFromAadhar = Boolean.FALSE;
    }

    public static void updateLoopCount(String[] split, QueFormBean queFormBean) {
        if (queFormBean.getAnswer() != null && queFormBean.getType().equals(GlobalTypes.RADIOBUTTON)) {
            SharedStructureData.relatedPropertyHashTable.put(split[1], String.valueOf(queFormBean.getLoopCounter() + 1));
        }
    }

    public static void clearScannedAadharDetails(String[] split, QueFormBean queFormBean) {
        if (queFormBean.getAnswer() != null) {
            String relatedPropertyAnswer;
            if (queFormBean.getLoopCounter() > 0) {
                relatedPropertyAnswer = SharedStructureData.relatedPropertyHashTable.get(queFormBean.getRelatedpropertyname() + queFormBean.getLoopCounter());
            } else {
                relatedPropertyAnswer = SharedStructureData.relatedPropertyHashTable.get(queFormBean.getRelatedpropertyname());
            }

            if (!queFormBean.getAnswer().toString().equals(relatedPropertyAnswer)) {
                String aadharScanned;
                String aadharAgreement;
                if (queFormBean.getLoopCounter() > 0) {
                    aadharScanned = SharedStructureData.relatedPropertyHashTable.get("aadharScanned" + queFormBean.getLoopCounter());
                    aadharAgreement = SharedStructureData.relatedPropertyHashTable.get("aadharAgreement" + queFormBean.getLoopCounter());
                } else {
                    aadharScanned = SharedStructureData.relatedPropertyHashTable.get("aadharScanned");
                    aadharAgreement = SharedStructureData.relatedPropertyHashTable.get("aadharAgreement");
                }

                if ((aadharScanned != null && aadharScanned.equals("2"))
                        || (aadharAgreement != null && aadharAgreement.equals("2"))) {

                    //Both values are static values, need to be changed if queId is changed in the Xls file - just for FHS Qr Code Scanning
                    int questionIdForAadhar = 14;
                    int questionIdForDob = 17;
                    int questionIdForAgeDisplay = 1701;

                    if (split.length > 1) {
                        String[] stringQuestionIds = split[1].split(",");
                        if (stringQuestionIds.length == 3) {
                            questionIdForAadhar = Integer.parseInt(stringQuestionIds[0]);
                            questionIdForDob = Integer.parseInt(stringQuestionIds[1]);
                            questionIdForAgeDisplay = Integer.parseInt(stringQuestionIds[2]);
                        }
                    }

                    if (queFormBean.getLoopCounter() > 0) {
                        SharedStructureData.relatedPropertyHashTable.remove(RelatedPropertyNameConstants.AADHAR_NUMBER + queFormBean.getLoopCounter());
                        SharedStructureData.relatedPropertyHashTable.remove("dob" + queFormBean.getLoopCounter());

                        questionIdForAadhar = DynamicUtils.getLoopId(questionIdForAadhar, queFormBean.getLoopCounter());
                        questionIdForDob = DynamicUtils.getLoopId(questionIdForDob, queFormBean.getLoopCounter());
                        questionIdForAgeDisplay = DynamicUtils.getLoopId(questionIdForAgeDisplay, queFormBean.getLoopCounter());

                        QueFormBean aadharQuestion = SharedStructureData.mapIndexQuestion.get(questionIdForAadhar);
                        QueFormBean dobQuestion = SharedStructureData.mapIndexQuestion.get(questionIdForDob);
                        QueFormBean ageDisplayQuestion = SharedStructureData.mapIndexQuestion.get(questionIdForAgeDisplay);

                        if (aadharQuestion != null) {
                            LinearLayout mainLayout = (LinearLayout) aadharQuestion.getQuestionTypeView();
                            CheckBox checkBox = (CheckBox) mainLayout.getChildAt(0);
                            LinearLayout inputLayout = (LinearLayout) mainLayout.getChildAt(1);
                            EditText textView = (EditText) inputLayout.getChildAt(0);
                            View.OnFocusChangeListener onFocusChangeListener = textView.getOnFocusChangeListener();
                            inputLayout.removeView(textView);
                            TextInputLayout aadharNumberView = MyStaticComponents.getEditText(SharedStructureData.context, "Enter Aadhar Number", 1000, 12, -1);
                            Objects.requireNonNull(aadharNumberView.getEditText()).setInputType(InputType.TYPE_CLASS_NUMBER);
                            aadharNumberView.setOnFocusChangeListener(onFocusChangeListener);
                            inputLayout.addView(aadharNumberView, 0);
                            checkBox.setChecked(false);
                            checkBox.setClickable(true);
                            aadharQuestion.setAnswer(null);
                        }

                        if (dobQuestion != null) {
                            LinearLayout questionTypeViewForDobQuestion = (LinearLayout) dobQuestion.getQuestionTypeView();
                            TextView textViewDob = (TextView) questionTypeViewForDobQuestion.getChildAt(0);
                            questionTypeViewForDobQuestion.setClickable(true);
                            textViewDob.setText(UtilBean.getMyLabel("Select Date"));
                        }

                        if (ageDisplayQuestion != null) {
                            TextView ageDisplayTextView = (TextView) ageDisplayQuestion.getQuestionTypeView();
                            ageDisplayTextView.setText(UtilBean.getMyLabel(LabelConstants.DATE_NOT_YET_SELECTED));
                            Objects.requireNonNull(dobQuestion).setAnswer(null);
                        }
                    } else {
                        SharedStructureData.relatedPropertyHashTable.remove(RelatedPropertyNameConstants.AADHAR_NUMBER);
                        SharedStructureData.relatedPropertyHashTable.remove("dob");

                        questionIdForAadhar = DynamicUtils.getLoopId(questionIdForAadhar, queFormBean.getLoopCounter());
                        questionIdForDob = DynamicUtils.getLoopId(questionIdForDob, queFormBean.getLoopCounter());
                        questionIdForAgeDisplay = DynamicUtils.getLoopId(questionIdForAgeDisplay, queFormBean.getLoopCounter());

                        QueFormBean aadharQuestion = SharedStructureData.mapIndexQuestion.get(questionIdForAadhar);
                        QueFormBean dobQuestion = SharedStructureData.mapIndexQuestion.get(questionIdForDob);
                        QueFormBean ageDisplayQuestion = SharedStructureData.mapIndexQuestion.get(questionIdForAgeDisplay);

                        if (aadharQuestion != null) {
                            LinearLayout mainLayout = (LinearLayout) aadharQuestion.getQuestionTypeView();
                            CheckBox checkBox = (CheckBox) mainLayout.getChildAt(0);
                            LinearLayout inputLayout = (LinearLayout) mainLayout.getChildAt(1);
                            EditText textView = (EditText) inputLayout.getChildAt(0);
                            View.OnFocusChangeListener onFocusChangeListener = textView.getOnFocusChangeListener();
                            inputLayout.removeView(textView);
                            TextInputLayout aadharNumberView = MyStaticComponents.getEditText(SharedStructureData.context, UtilBean.getMyLabel("Enter Aadhar Number"), 1000, 12, -1);
                            Objects.requireNonNull(aadharNumberView.getEditText()).setInputType(InputType.TYPE_CLASS_NUMBER);
                            aadharNumberView.setOnFocusChangeListener(onFocusChangeListener);
                            inputLayout.addView(aadharNumberView, 0);
                            checkBox.setChecked(false);
                            checkBox.setClickable(true);
                            aadharQuestion.setAnswer(null);
                        }

                        if (dobQuestion != null) {
                            dobQuestion.setAnswer(null);
                            LinearLayout questionTypeViewForDobQuestion = (LinearLayout) dobQuestion.getQuestionTypeView();
                            if (questionTypeViewForDobQuestion != null) {
                                questionTypeViewForDobQuestion.setClickable(true);
                                TextView textViewDob = (TextView) questionTypeViewForDobQuestion.getChildAt(0);
                                if (textViewDob != null) {
                                    textViewDob.setText(UtilBean.getMyLabel("Select Date"));
                                }
                            }
                        }

                        if (ageDisplayQuestion != null) {
                            TextView ageDisplayTextView = (TextView) ageDisplayQuestion.getQuestionTypeView();
                            if (ageDisplayTextView != null) {
                                ageDisplayTextView.setText(UtilBean.getMyLabel("Not Available"));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void setImmunisationWpd(QueFormBean queFormBean) {
        String deliveryDate = SharedStructureData.relatedPropertyHashTable.get("deliveryDate");
        if (deliveryDate == null ||
                (queFormBean.getAnswer() != null && !queFormBean.getAnswer().toString().trim().equals(deliveryDate.trim()))) {
            SharedStructureData.relatedPropertyHashTable.remove(
                    UtilBean.getRelatedPropertyNameWithLoopCounter("remainingVaccines", queFormBean.getLoopCounter()));
        }
    }

    public static void setPreFilledAsPerRelationWithHOF(String[] split, QueFormBean queFormBean) {
        if (split.length == 2 && queFormBean.getAnswer() != null && queFormBean.getAnswer().equals("1")) {
            SharedStructureData.relatedPropertyHashTable.put(split[1], String.valueOf(queFormBean.getLoopCounter()));
        }
    }

    @SuppressLint("SetTextI18n")
    public static void setDiabetesStatus(String[] split, QueFormBean queFormBean) {
        if (split.length == 4) {
            QueFormBean queFormBean1 = SharedStructureData.mapIndexQuestion.get(Integer.valueOf(split[3]));
            MaterialTextView materialTextView = (MaterialTextView) queFormBean1.getQuestionTypeView();
            if (split[1].equalsIgnoreCase("RBS") && queFormBean.getAnswer() != null && split[2] != null && !queFormBean.getAnswer().toString().isEmpty()) {
                if (Integer.parseInt(queFormBean.getAnswer().toString()) > Integer.parseInt(split[2])) {
                    materialTextView.setText("Uncontrolled");
                    queFormBean1.setAnswer("uncontrolled");
                } else {
                    materialTextView.setText("Controlled");
                    queFormBean1.setAnswer("controlled");
                }
            }
        } else {
            QueFormBean queFormBean1 = SharedStructureData.mapIndexQuestion.get(15);
            MaterialTextView materialTextView = (MaterialTextView) queFormBean1.getQuestionTypeView();
            String pastStatus = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.IS_EARLIER_DIAGNOSED_DIABETES);

            if (pastStatus != null && pastStatus.equals("1")) {
                materialTextView.setText("Suspected Diabetes");
                queFormBean1.setAnswer("suspected");
            } else {
                if (split.length == 5) {
                    String measurementType = SharedStructureData.relatedPropertyHashTable.get(split[1]);
                    if (measurementType != null) {
                        if (measurementType.equals("FBS")) {
                            if (Integer.parseInt(queFormBean.getAnswer().toString()) > Integer.parseInt(split[2])) {
                                materialTextView.setText("Suspected Diabetes");
                                queFormBean1.setAnswer("suspected");
                            } else {
                                materialTextView.setText("Normal");
                                queFormBean1.setAnswer("normal");
                            }
                        } else if (measurementType.equals("PP2BS")) {
                            if (Integer.parseInt(queFormBean.getAnswer().toString()) > Integer.parseInt(split[3])) {
                                materialTextView.setText("Suspected Diabetes");
                                queFormBean1.setAnswer("suspected");
                            } else {
                                materialTextView.setText("Normal");
                                queFormBean1.setAnswer("normal");
                            }
                        } else {
                            if (Integer.parseInt(queFormBean.getAnswer().toString()) > Integer.parseInt(split[4])) {
                                materialTextView.setText("Suspected Diabetes");
                                queFormBean1.setAnswer("suspected");
                            } else {
                                materialTextView.setText("Normal");
                                queFormBean1.setAnswer("normal");
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public static void setHypertensionStatus(String[] split, QueFormBean queFormBean) {
        if (split.length == 4) {
            QueFormBean queFormBean1 = SharedStructureData.mapIndexQuestion.get(Integer.valueOf(split[3]));
            MaterialTextView materialTextView = (MaterialTextView) queFormBean1.getQuestionTypeView();
            if (queFormBean.getAnswer() != null) {
                int systolicBp = Integer.parseInt(split[1]);
                int diastolicBp = Integer.parseInt(split[2]);
                String[] subString = queFormBean.getAnswer().toString().split("-");
                if (subString.length > 2) {
                    if (Integer.parseInt(subString[1]) > systolicBp || Integer.parseInt(subString[2]) > diastolicBp) {
                        queFormBean1.setAnswer("uncontrolled");
                        materialTextView.setText("Uncontrolled");
                    } else {
                        materialTextView.setText("Controlled");
                        queFormBean1.setAnswer("controlled");
                    }
                }
            }
        } else {
            QueFormBean queFormBean1 = SharedStructureData.mapIndexQuestion.get(19);
            MaterialTextView materialTextView = (MaterialTextView) queFormBean1.getQuestionTypeView();
            String pastStatus = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.IS_EARLIER_DIAGNOSED_HYPERTENSION);

            if (pastStatus != null && pastStatus.equals("1")) {
                materialTextView.setText("Suspected Hypertension");
                queFormBean1.setAnswer("suspected");
            } else {
                if (split.length == 3) {
                    if (queFormBean.getAnswer() != null) {
                        int systolicBp = Integer.parseInt(split[1]);
                        int diastolicBp = Integer.parseInt(split[2]);
                        String[] subString = queFormBean.getAnswer().toString().split("-");
                        if (subString.length > 2) {
                            if (Integer.parseInt(subString[1]) > systolicBp || Integer.parseInt(subString[2]) > diastolicBp) {
                                queFormBean1.setAnswer("suspected");
                                materialTextView.setText("Suspected Hypertension");
                            } else {
                                materialTextView.setText("Normal");
                                queFormBean1.setAnswer("normal");
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public static void setMentalHealthStatus(String[] split, QueFormBean queFormBean) {
        if (split.length == 2) {
            QueFormBean queFormBean1 = SharedStructureData.mapIndexQuestion.get(15);
            QueFormBean queFormBean2 = SharedStructureData.mapIndexQuestion.get(16);
            MaterialTextView materialTextView1 = (MaterialTextView) queFormBean1.getQuestionTypeView();
            MaterialTextView materialTextView2 = (MaterialTextView) queFormBean2.getQuestionTypeView();
            String pastStatus = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            if (queFormBean.getAnswer() != null) {
                FieldValueMobDataBean observation = null;
                List<FieldValueMobDataBean> labelDataBeans = SharedStructureData.sewaService.getFieldValueMobDataBeanByDataMap("mentalHealthObservationList");
                for (FieldValueMobDataBean data: labelDataBeans) {
                    if (data.getIdOfValue() == Integer.parseInt(queFormBean.getAnswer().toString())) {
                        observation = data;
                    }
                }
                if (observation != null) {
                    if (observation.getValue().equals("No mental health issues")) {
                        materialTextView1.setText("No mental health issues");
                        queFormBean1.setAnswer("normal");
                    } else if (observation.getValue().equals("Presence of behaviour related symptoms") ||
                            observation.getValue().equals("Presence of communication related symptoms") ||
                            observation.getValue().equals("Presence of feelings related symptoms")) {
                        materialTextView1.setText("Suspected mild mental health issues");
                        queFormBean1.setAnswer("mild");
                    } else {
                        materialTextView1.setText("Suspected severe mental health issues");
                        queFormBean1.setAnswer("severe");
                    }
                }

                if (pastStatus != null && pastStatus.equals("1")) {
                    queFormBean2.setAnswer("1");
                    materialTextView2.setText("Yes");
                } else if (queFormBean1.getAnswer().equals("mild") || queFormBean1.getAnswer().equals("severe")) {
                    queFormBean2.setAnswer("1");
                    materialTextView2.setText("Yes");
                } else {
                    queFormBean2.setAnswer("2");
                    materialTextView2.setText("No");
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public static void calculateMentalHealthStatus(String[] split, QueFormBean queFormBean) {
        if (split.length == 4) {
            QueFormBean queFormBean1 = SharedStructureData.mapIndexQuestion.get(15);
            QueFormBean queFormBean2 = SharedStructureData.mapIndexQuestion.get(16);
            MaterialTextView materialTextView1 = (MaterialTextView) queFormBean1.getQuestionTypeView();
            MaterialTextView materialTextView2 = (MaterialTextView) queFormBean2.getQuestionTypeView();
            String observationList = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            String otherProblemList = SharedStructureData.relatedPropertyHashTable.get(split[2]);
            String pastStatus = SharedStructureData.relatedPropertyHashTable.get(split[3]);
            if (queFormBean.getAnswer() != null) {

                if (otherProblemList != null && otherProblemList.split(",").length >= 4) {
                    materialTextView1.setText("Suspected severe mental health issues");
                    queFormBean1.setAnswer("severe");
                } else if (observationList != null && observationList.split(",").length >= 4) {
                    materialTextView1.setText("Suspected mild mental health issues");
                    queFormBean1.setAnswer("mild");
                } else {
                    materialTextView1.setText("No mental health issues");
                    queFormBean1.setAnswer("normal");
                }

                if (pastStatus != null && pastStatus.equals("1")) {
                    queFormBean2.setAnswer("1");
                    materialTextView2.setText("Yes");
                } else if (queFormBean1.getAnswer().equals("mild") || queFormBean1.getAnswer().equals("severe")) {
                    queFormBean2.setAnswer("1");
                    materialTextView2.setText("Yes");
                } else {
                    queFormBean2.setAnswer("2");
                    materialTextView2.setText("No");
                }
            }
        } else if (split.length == 6) {
            QueFormBean queFormBean1 = SharedStructureData.mapIndexQuestion.get(Integer.valueOf(split[5]));
            MaterialTextView materialTextView1 = (MaterialTextView) queFormBean1.getQuestionTypeView();
            if (materialTextView1 != null) {
                String talkValue = SharedStructureData.relatedPropertyHashTable.get(split[1]);
                String ownDailyWorkValue = SharedStructureData.relatedPropertyHashTable.get(split[2]);
                String socialWorkValue = SharedStructureData.relatedPropertyHashTable.get(split[3]);
                String understandingValue = SharedStructureData.relatedPropertyHashTable.get(split[4]);
                Integer talk = talkValue != null ? Integer.parseInt(talkValue) : 0;
                Integer ownDailyWork = ownDailyWorkValue != null ? Integer.parseInt(ownDailyWorkValue) : 0;
                Integer socialWork = socialWorkValue != null ? Integer.parseInt(socialWorkValue) : 0;
                Integer understanding = understandingValue != null ? Integer.parseInt(understandingValue) : 0;

                int result = talk + ownDailyWork + socialWork + understanding;
                if (result > 4) {
                    queFormBean1.setAnswer("uncontrolled");
                    materialTextView1.setText("Uncontrolled");
                } else {
                    queFormBean1.setAnswer("controlled");
                    materialTextView1.setText("Controlled");
                }
            }
        }
    }

    public static void setMedicineId(String[] split, QueFormBean queFormBean) {
        QueFormBean queFormBean1 = SharedStructureData.mapIndexQuestion.get(Integer.valueOf(split[1]));
        if (queFormBean.getAnswer() != null && queFormBean1 != null) {
            String medicineId = SharedStructureData.relatedPropertyHashTable.get(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.MEDICINE_ID, queFormBean.getLoopCounter()));
            queFormBean1.setAnswer(medicineId);
        }
    }

    public static void setDefaultPropertyLoop(String[] split, QueFormBean queFormBean) {
        String property = null;
        String value = null;
        if (queFormBean.getRelatedpropertyname() != null && queFormBean.getRelatedpropertyname().trim().length() > 0) {
            property = queFormBean.getRelatedpropertyname().trim();
        }
        if (property != null) {
            value = SharedStructureData.relatedPropertyHashTable.get(UtilBean.getRelatedPropertyNameWithLoopCounter(property, queFormBean.getLoopCounter()));
        }
        if (queFormBean.getAnswer() == null && property != null && value == null && split.length > 1) {
            SharedStructureData.relatedPropertyHashTable.put(UtilBean.
                    getRelatedPropertyNameWithLoopCounter(property,
                            queFormBean.getLoopCounter()), split[1]);
        }
    }
}
