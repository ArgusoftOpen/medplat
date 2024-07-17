package com.argusoft.sewa.android.app.util;

import static com.argusoft.sewa.android.app.util.DynamicUtils.getLoopId;
import static com.argusoft.sewa.android.app.util.UtilBean.createAdapter;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyDynamicComponents;
import com.argusoft.sewa.android.app.component.MyListInColorComponent;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.MyVaccination;
import com.argusoft.sewa.android.app.component.MyVaccinationStatus;
import com.argusoft.sewa.android.app.constants.FhsConstants;
import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.NotificationConstants;
import com.argusoft.sewa.android.app.constants.RchConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.databean.FieldValueMobDataBean;
import com.argusoft.sewa.android.app.databean.MemberAdditionalInfoDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.OptionDataBean;
import com.argusoft.sewa.android.app.databean.OptionTagBean;
import com.argusoft.sewa.android.app.datastructure.PageFormBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.model.NotificationBean;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.j256.ormlite.stmt.query.In;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by prateek on 10/9/19
 */
public class HiddenQuestionFormulaUtil {

    private static final String TAG = "HiddenQuestionFormula";
    private static MyAlertDialog myAlertDialog;

    private HiddenQuestionFormulaUtil() {
        throw new IllegalStateException("Utility Class");
    }

    public static void urineTest(String[] split, QueFormBean queFormBean) {
        String propertyValue = SharedStructureData.relatedPropertyHashTable.get(split[1]);
        if (propertyValue != null && propertyValue.trim().length() > 0) {
            String[] bloodPresser = UtilBean.split(propertyValue, GlobalTypes.KEY_VALUE_SEPARATOR);
            // format is F-140-51 or T

            queFormBean.setNext(queFormBean.getOptions().get(0).getNext()); // set default True
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            if (bloodPresser.length > 2) {
                try {
                    // entered value split1[0] and split1[1], compare with value split[2] and split[3], if both are greater than corresponding value, key=false
                    if (Integer.parseInt(bloodPresser[1]) > Integer.parseInt(split[2])
                            || Integer.parseInt(bloodPresser[2]) > Integer.parseInt(split[3])) {
                        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                    }
                } catch (Exception e) {
                    Log.e(TAG, null, e);
                }
            }
        }
    }

    public static void checkContains(String[] split, QueFormBean queFormBean) {
        String mainAnswer = SharedStructureData.relatedPropertyHashTable.get(split[1]); // get answer of property
        queFormBean.setNext(queFormBean.getOptions().get(1).getNext()); // set default false
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        if (mainAnswer != null) {
            String[] split1 = UtilBean.split(mainAnswer, GlobalTypes.COMMA);
            List<String> asList = Arrays.asList(split1);
            if (!asList.isEmpty()) {
                for (int i = 2; i < split.length; i++) {
                    if (asList.contains(split[i])) {
                        queFormBean.setNext(queFormBean.getOptions().get(0).getNext()); // set default true
                        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                        break;
                    }
                }
            }
        }
    }

    public static void pncCheckTemp(String[] split, QueFormBean queFormBean) {
        String mainAnswer = SharedStructureData.relatedPropertyHashTable.get(split[1]);
        if (split[2] != null) {
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext()); // set default false
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            if (mainAnswer != null) {
                String[] split1 = UtilBean.split(mainAnswer, GlobalTypes.KEY_VALUE_SEPARATOR);
                if (split1.length > 0 && split1[0].equalsIgnoreCase(GlobalTypes.FALSE)) {
                    // if true temperature structure is true-104-1
                    // else convert in celsius and compare with provided value
                    try {
                        String fahrenheitString = split1[1] + GlobalTypes.DOT_SEPARATOR + split1[2];
                        float fahrenheit = Float.parseFloat(fahrenheitString);
                        float fahrenheitToCelsius = UtilBean.fahrenheitToCelsius(fahrenheit);
                        float compareWithCelsius = Float.parseFloat(split[2]);
                        if (UtilBean.isValidNumber(split[3], fahrenheitToCelsius, compareWithCelsius)) {
                            queFormBean.setNext(queFormBean.getOptions().get(0).getNext()); // set default true
                            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, null, e);
                    }
                }
            }
        }
    }
    public static void checkIfYesInAny(String[] split, QueFormBean queFormBean, Boolean isFirstCall) {
//        if (Boolean.TRUE.equals(isFirstCall))
//            return;

        if (split.length > 1) {
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext()); // set default false
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            for (int i = 1; i < split.length; i++) {
                if (SharedStructureData.relatedPropertyHashTable.containsKey(split[i])
                        && ("T".equalsIgnoreCase(SharedStructureData.relatedPropertyHashTable.get(split[i]))
                        || "1".equalsIgnoreCase(SharedStructureData.relatedPropertyHashTable.get(split[i])))) {
                    queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                    queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                    break;
                }
            }
        }
    }

    public static void lessThan(String[] split, QueFormBean queFormBean) {
        if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
            split[1] += queFormBean.getLoopCounter();
        }
        String propertyValue = SharedStructureData.relatedPropertyHashTable.get(split[1]); // get value of property
        if (propertyValue != null) {
            if (propertyValue.equalsIgnoreCase("")) {
                propertyValue = "0";
            }
            int parseInt;
            try {
                parseInt = Integer.parseInt(propertyValue);
            } catch (Exception e) {
                parseInt = 0;
            }
            try {
                if (parseInt <= Integer.parseInt(split[2])) {
                    queFormBean.setNext(queFormBean.getOptions().get(0).getNext()); // set is true
                    queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                } else {
                    queFormBean.setNext(queFormBean.getOptions().get(1).getNext()); // set is true
                    queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                }
            } catch (Exception e) {
                Log.e(TAG, null, e);
            }
        }
    }

    public static void checkLength(String[] split, QueFormBean queFormBean) {
        if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
            split[1] += queFormBean.getLoopCounter();
        }
        String propertyValue = SharedStructureData.relatedPropertyHashTable.get(split[1]); // get value of property
        if (propertyValue != null) {
            if (propertyValue.equalsIgnoreCase("")) {
                propertyValue = "0";
            }
            int length = propertyValue.length();
            try {
                if (length <= Integer.parseInt(split[2])) {
                    queFormBean.setNext(queFormBean.getOptions().get(0).getNext()); // set is true
                    queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                } else {
                    queFormBean.setNext(queFormBean.getOptions().get(1).getNext()); // set is true
                    queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                }
            } catch (Exception e) {
                Log.e(TAG, null, e);
            }
        }
    }

    public static void setReverseFlag(String[] split, QueFormBean queFormBean) {
        String split1 = SharedStructureData.relatedPropertyHashTable.get(split[1]);
        if (split1 != null && split1.trim().length() > 0 && !split1.trim().equalsIgnoreCase("null")) {
            String setFlag = GlobalTypes.FALSE;
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext()); // set is true
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            if (split1.equalsIgnoreCase(GlobalTypes.FALSE)) {
                setFlag = GlobalTypes.TRUE;
                queFormBean.setNext(queFormBean.getOptions().get(1).getNext()); // set is false
                queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            }
            SharedStructureData.relatedPropertyHashTable.put(split[2], setFlag);
        }
    }

    public static void appetiteTestEligibilityCheck(String[] split, QueFormBean queFormBean) {
        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());

        if (split.length == 5) {
            String muac = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            String sdScore = SharedStructureData.relatedPropertyHashTable.get(split[2]);
            String pedalEdema = SharedStructureData.relatedPropertyHashTable.get(split[3]);
            String medicalComplication = SharedStructureData.relatedPropertyHashTable.get(split[4]);
            if ((muac != null && !muac.isEmpty() && Float.parseFloat(muac) < 11.5) ||
                    (sdScore != null && !sdScore.isEmpty() && (sdScore.equals(RchConstants.SD_SCORE_SD4) || sdScore.equals(RchConstants.SD_SCORE_SD3))) ||
                    (pedalEdema != null && pedalEdema.equals("1")) ||
                    (medicalComplication != null && medicalComplication.equals("1"))) {
                queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            }
        }
    }

    public static void compareDiseaseCount(QueFormBean queFormBean) {
        queFormBean.setEvent(GlobalTypes.EVENT_DEFAULT_LOOP);
        queFormBean.setIgnoreLoop(true);
        queFormBean.setIgnoreNextQueLoop(false);
        String relatedPropertyName = queFormBean.getRelatedpropertyname();

        if (relatedPropertyName == null || relatedPropertyName.trim().length() == 0) {
            relatedPropertyName = queFormBean.getOptions().get(0).getRelatedpropertyname();
        }
        String relatedPropertyValue = SharedStructureData.relatedPropertyHashTable.get(relatedPropertyName);

        if (relatedPropertyValue == null || relatedPropertyValue.trim().length() == 0 || relatedPropertyValue.trim().equalsIgnoreCase("null")) {
            relatedPropertyValue = "1";
        }
        queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
        if (queFormBean.getLoopCounter() >= (Integer.parseInt(relatedPropertyValue) - 1)) {
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        }

    }

    public static void checkLoop(QueFormBean queFormBean) {

        String relatedPropertyName = queFormBean.getRelatedpropertyname();

        if (relatedPropertyName == null || relatedPropertyName.trim().length() == 0) {
            relatedPropertyName = queFormBean.getOptions().get(0).getRelatedpropertyname();
        }
        String relatedPropertyValue = SharedStructureData.relatedPropertyHashTable.get(relatedPropertyName);

        if (relatedPropertyValue == null || relatedPropertyValue.trim().length() == 0 || relatedPropertyValue.trim().equalsIgnoreCase("null")) {
            relatedPropertyValue = "0";
        }

        queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
        if (queFormBean.getLoopCounter() >= (Integer.parseInt(relatedPropertyValue))) {
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        }
    }

    public static void checkLoopBakCount(String[] split, QueFormBean queFormBean) {
        if (split.length > 1) {
            if (queFormBean.getLoopCounter() == Integer.parseInt(split[1])) {
                queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            } else {
                queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            }
        } else {
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            if (SharedStructureData.totalFamilyMembersCount == -1 || queFormBean.getLoopCounter() >= SharedStructureData.totalFamilyMembersCount) {
                queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            }
        }

    }

    public static void compareDate(String[] split, QueFormBean queFormBean) {
        String date1 = SharedStructureData.relatedPropertyHashTable.get(split[1]);
        String date2 = SharedStructureData.relatedPropertyHashTable.get(split[2]);
        if (date1 != null && date1.trim().length() > 0 && date2 != null && date2.trim().length() > 0) {
            Calendar cDate1 = Calendar.getInstance();
            cDate1.setTimeInMillis(Long.parseLong(date1));
            UtilBean.clearTimeFromDate(cDate1);
            Calendar cDate2 = Calendar.getInstance();
            cDate2.setTimeInMillis(Long.parseLong(date2));
            UtilBean.clearTimeFromDate(cDate2);
            if (cDate1.compareTo(cDate2) == 0) {
                queFormBean.setNext(UtilBean.getOptionsOrDataMap(queFormBean, false).get(0).getNext());
                queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            } else {
                queFormBean.setNext(UtilBean.getOptionsOrDataMap(queFormBean, false).get(1).getNext());
                queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            }
        }
    }
    public static void setReferralReason(String[] split) {

        String hypStatus = SharedStructureData.relatedPropertyHashTable.get(split[1]);
        String diabetesStatus = SharedStructureData.relatedPropertyHashTable.get(split[2]);
        String mentalObservations = SharedStructureData.relatedPropertyHashTable.get(split[3]);
        String bpValue1 = SharedStructureData.relatedPropertyHashTable.get(split[4]);
        String bpValue2 = SharedStructureData.relatedPropertyHashTable.get(split[5]);
        int sugarValue = Integer.parseInt(SharedStructureData.relatedPropertyHashTable.get(split[7]));

        int systolicBp;
        int diastolicBp;
        if (bpValue2 != null) {
            String[] substring = bpValue2.split("-");
            systolicBp = Integer.parseInt(substring[1]);
            diastolicBp = Integer.parseInt(substring[2]);
        } else {
            String[] substring = bpValue1.split("-");
            systolicBp = Integer.parseInt(substring[1]);
            diastolicBp = Integer.parseInt(substring[2]);
        }

        StringBuilder riskFound = new StringBuilder();
        if (hypStatus != null && !hypStatus.isEmpty() && (hypStatus.equalsIgnoreCase("suspected")
                || hypStatus.equalsIgnoreCase("uncontrolled"))) {
            riskFound.append(UtilBean.getMyLabel("Suspected Hypertension  (BP : " + systolicBp + "/" + diastolicBp + ")"));
            riskFound.append("\n");
        }
        if (diabetesStatus != null && !diabetesStatus.isEmpty() && (diabetesStatus.equalsIgnoreCase("suspected")
                || diabetesStatus.equalsIgnoreCase("uncontrolled"))) {
            riskFound.append(UtilBean.getMyLabel("Suspected Diabetes (Sugar Value : " + sugarValue + ")"));
            riskFound.append("\n");
        }
        if (mentalObservations != null && !mentalObservations.isEmpty()
                && !mentalObservations.equalsIgnoreCase("NONE")) {
            riskFound.append(UtilBean.getMyLabel("Mental Health issues"));
            riskFound.append("\n");
        }

        String referralReason = riskFound.toString().length() > 0 ? riskFound.toString() : UtilBean.getMyLabel(RchConstants.NO_RISK_FOUND);

        QueFormBean displayReasonQue = SharedStructureData.mapIndexQuestion.get(Integer.parseInt(split[8]));
        if (displayReasonQue != null) {
            TextView textView = (TextView) displayReasonQue.getQuestionTypeView();
            if (textView != null) {
                textView.setText(referralReason);
            }
            displayReasonQue.setAnswer(referralReason);
        }
    }   

    public static void setTrue(String[] split, QueFormBean queFormBean) {
        if (split.length == 5) {
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());  // default true
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            boolean flag = true;
            String tt1Given = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            String tt2Given = SharedStructureData.relatedPropertyHashTable.get(split[2]);
            String tt2NGiven = SharedStructureData.relatedPropertyHashTable.get(split[3]);

            if (tt1Given == null || tt1Given.equalsIgnoreCase(GlobalTypes.FALSE)
                    || tt2Given == null || tt2Given.equalsIgnoreCase(GlobalTypes.FALSE)
                    || tt2NGiven == null || tt2NGiven.equalsIgnoreCase(GlobalTypes.FALSE)) {
                flag = false;
            }
            if (flag && split[4] != null) {
                String[] split1 = UtilBean.split(tt1Given, GlobalTypes.DATE_STRING_SEPARATOR);
                long dateLastTT1 = Long.parseLong(split1[1]);
                long fourWeekAgoLong = Calendar.getInstance().getTimeInMillis() - (UtilBean.DAY_LONG_VALUE * Integer.parseInt(split[4]) * 7);
                if (dateLastTT1 == 0 || dateLastTT1 > fourWeekAgoLong) {
                    flag = false;
                }
            }
            if (!flag) {
                queFormBean.setNext(queFormBean.getOptions().get(1).getNext());  // default false
                queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            }
        } else if (split.length == 4) {
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());  // default true
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            boolean flag = true;
            String tt1Given = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            String currentGravida = SharedStructureData.relatedPropertyHashTable.get(split[2]);
            if (tt1Given == null || tt1Given.equalsIgnoreCase(GlobalTypes.FALSE)
                    || currentGravida == null || !currentGravida.equalsIgnoreCase("1")) {
                flag = false;
            }
            if (flag && split[3] != null) {
                String[] split1 = UtilBean.split(tt1Given, GlobalTypes.DATE_STRING_SEPARATOR);
                long dateLastTT1 = Long.parseLong(split1[1]);
                long fourWeekAgoLong = Calendar.getInstance().getTimeInMillis() - (UtilBean.DAY_LONG_VALUE * Integer.parseInt(split[3]) * 7);
                if (dateLastTT1 == 0 || dateLastTT1 > fourWeekAgoLong) {
                    flag = false;
                }
            }
            if (!flag) {
                queFormBean.setNext(queFormBean.getOptions().get(1).getNext());  // default false
                queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            }
        }
    }

    public static void setmobiditylist(String[] split, QueFormBean queFormBean) {

        if (queFormBean.getNext() == null) {
            List<OptionDataBean> optionsOrDataMap = UtilBean.getOptionsOrDataMap(queFormBean, false);
            if (!optionsOrDataMap.isEmpty()) {
                queFormBean.setNext(optionsOrDataMap.get(0).getNext());
            }
        }
        if (SharedStructureData.ismapUpdate) {
            int nextQue = Integer.parseInt(split[1]);
            QueFormBean nextLICQuest = SharedStructureData.mapIndexQuestion.get(nextQue);
            if (nextLICQuest != null && nextLICQuest.getType().equalsIgnoreCase(GlobalTypes.LIST_IN_COLOR)) {
                MyListInColorComponent colorComponent = (MyListInColorComponent) nextLICQuest.getQuestionTypeView();
                colorComponent.resetList(SharedStructureData.getListOfLIC());
            }
        }
    }

    public static void setAnswer(QueFormBean queFormBean) {
        if (queFormBean.getRelatedpropertyname() != null
                && SharedStructureData.relatedPropertyHashTable.containsKey(queFormBean.getRelatedpropertyname())) {
            queFormBean.setAnswer(SharedStructureData.relatedPropertyHashTable.get(queFormBean.getRelatedpropertyname()));
        }
    }

    public static void contains(String[] split, QueFormBean queFormBean) {
        queFormBean.setNext(queFormBean.getOptions().get(1).getKey());
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        if (split[1].contains(GlobalTypes.MORBIDITY_SEPARATOR_HASH)) {
            //split-1 contains hash
            String[] relatedPropertyKey = split[1].split(GlobalTypes.MORBIDITY_SEPARATOR_HASH);
            List<String> relatedPropertyValue = new ArrayList<>();
            for (String s : relatedPropertyKey) {
                relatedPropertyValue.add(
                        SharedStructureData.relatedPropertyHashTable.get(s) == null ? "" : SharedStructureData.relatedPropertyHashTable.get(s));
            }
            if (relatedPropertyValue.contains(split[2])) {
                queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
            } else {
                queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
            }
        } else if (split[1].contains(GlobalTypes.MULTI_SELECT_SEPARATOR)) {
            List<String> relatedPropertyValue = new ArrayList<>();
            /*You may change the double slash i.e. "\\" appended to the separator if your separator is other than "*" */
            String[] relatedPropertyKey = split[1].split("\\" + GlobalTypes.MULTI_SELECT_SEPARATOR);
            for (String s : relatedPropertyKey) {
                relatedPropertyValue.add(
                        SharedStructureData.relatedPropertyHashTable.get(s) == null ? "" : SharedStructureData.relatedPropertyHashTable.get(s));
            }
            boolean temp = false;
            if (split[2].contains("\\+")) {
                String[] ans1 = split[2].split("\\+");
                List<String> ans = new ArrayList<>(Arrays.asList(ans1).subList(0, (ans1.length)));
                String multiSelectAnswer = relatedPropertyValue.get(0);
                List<String> selectedOptions = Arrays.asList(multiSelectAnswer.split(GlobalTypes.COMMA));
                if (!selectedOptions.isEmpty()) {
                    for (String anAns : ans) {
                        if (selectedOptions.contains(anAns)) {
                            temp = true;
                            break;
                        }
                    }
                    if (temp) {
                        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                        queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                    } else {
                        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                    }
                } else {
                    queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                    queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                }
            } else {
                String multiSelectAnswer = relatedPropertyValue.get(0);
                List<String> selectedOptions = Arrays.asList(multiSelectAnswer.split(GlobalTypes.COMMA));
                if (!selectedOptions.isEmpty()) {
                    if (selectedOptions.contains(split[2])) {
                        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                        queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                    } else {
                        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                    }
                } else {
                    queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                    queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                }
            }
        } else if (split[1].contains(GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR)) {
            //split-1 contains ~
            String[] relatedPropertyKey = split[1].split(GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR);
            boolean b = true;
            if (split.length > 2) {
                for (String s : relatedPropertyKey) {
                    if (!split[2].equals(SharedStructureData.relatedPropertyHashTable.get(s))) {
                        b = false;
                    }
                }
            }
            if (b) {
                queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
            } else {
                queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
            }
        } else if (split[1].contains(GlobalTypes.MORBIDITY_DETAILS_SEPARATOR)) {
            //split-1 contains @
            String[] relatedPropertyKey = split[1].split(GlobalTypes.MORBIDITY_DETAILS_SEPARATOR);
            String givenAnswer;
            if (SharedStructureData.relatedPropertyHashTable.get(relatedPropertyKey[1]) != null &&
                    !("3".equals(SharedStructureData.relatedPropertyHashTable.get(relatedPropertyKey[0])))) {
                givenAnswer = SharedStructureData.relatedPropertyHashTable.get(relatedPropertyKey[1]);
            } else {
                givenAnswer = SharedStructureData.relatedPropertyHashTable.get(
                        relatedPropertyKey[0]) == null ? "" : SharedStructureData.relatedPropertyHashTable.get(relatedPropertyKey[0]);
            }
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
            if (split[2].contains(GlobalTypes.ADD_SEPARATOR)) {
                String[] ans1 = split[2].split("\\+");
                List<String> ans = new ArrayList<>(Arrays.asList(ans1));
                if (givenAnswer != null && ans.contains(givenAnswer)) {
                    queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                    queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                } else {
                    queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                    queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                }
            }
        } else {
            //split-2 contains comma
            String givenAnswer = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            if (split[2].contains(GlobalTypes.ADD_SEPARATOR)) {
                String[] ans1 = split[2].split("\\+");
                List<String> ans = new ArrayList<>(Arrays.asList(ans1).subList(0, (ans1.length)));
                if (givenAnswer != null && ans.contains(givenAnswer)) {
                    queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                    queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                } else {
                    queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                    queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                }
            } else {
                //user given answer contains comma
                if (givenAnswer != null && givenAnswer.contains(GlobalTypes.COMMA)) {
                    String[] splitGivenAnswer = givenAnswer.split(GlobalTypes.COMMA);
                    List<String> ansSplitGivenAnswer = new ArrayList<>(Arrays.asList(splitGivenAnswer).subList(0, (splitGivenAnswer.length)));
                    if (ansSplitGivenAnswer.contains(split[2])) {
                        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                        queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                    } else {
                        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                    }
                } else {
                    if (givenAnswer != null && givenAnswer.equals(split[2])) {
                        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                        queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                    } else {
                        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                    }

                }
            }

        }
    }

    public static void dateBetween(String[] split, QueFormBean queFormBean) {
        queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
        if (split.length == 3 && split[1] != null && split[2] != null) {
            String from = split[1];
            String to = split[2];
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date fromDate = null;
            Date toDate = null;
            try {
                fromDate = formatter.parse(from);
                toDate = formatter.parse(to);
            } catch (ParseException e) {
                // execution will come here if the String that is given
                // does not match the expected format.
                Log.e(TAG, null, e);
            }
            String temp = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.DELIVERY_DATE);
            if (temp != null) {
                long outcomeDt = Long.parseLong(temp);
                Calendar calDob = Calendar.getInstance();
                calDob.setTimeInMillis(outcomeDt);
                UtilBean.clearTimeFromDate(calDob);

                if (!((calDob.getTime().after(fromDate) && calDob.getTime().before(toDate)) || calDob.getTime().equals(fromDate) || calDob.getTime().equals(toDate))) {
                    queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                    queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                }
            }
        }
    }

    public static void setAgePerDateSelected(String[] split, QueFormBean queFormBean) {
        String selectedDate;
        String ageDisplay = null;
        int questionIdForUpdate;
        if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
            questionIdForUpdate = getLoopId(Integer.parseInt(split[1]), queFormBean.getLoopCounter());
            selectedDate = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.DOB + queFormBean.getLoopCounter());
        } else {
            questionIdForUpdate = Integer.parseInt(split[1]);
            selectedDate = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.DOB);
        }
        QueFormBean setterQuestion = SharedStructureData.mapIndexQuestion.get(questionIdForUpdate);
        if (setterQuestion != null) {
            TextView textView = (TextView) setterQuestion.getQuestionTypeView();
            if (textView != null) {
                if (selectedDate != null) {
                    int[] ageYearMonthDayArray = UtilBean.calculateAgeYearMonthDayOnGivenDate(new Date(Long.parseLong(selectedDate)).getTime(), new Date().getTime());
                    ageDisplay = UtilBean.getAgeDisplay(ageYearMonthDayArray[0], ageYearMonthDayArray[1], ageYearMonthDayArray[2]);
                    textView.setText(UtilBean.getMyLabel(ageDisplay));
                    setterQuestion.setAnswer(ageDisplay);
                } else {
                    textView.setText(UtilBean.getMyLabel(LabelConstants.DATE_NOT_YET_SELECTED));
                    setterQuestion.setAnswer(LabelConstants.DATE_NOT_YET_SELECTED);
                }
            }
        }
        if (ageDisplay != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.AGE_AS_PER_DATE, ageDisplay);
        }
    }

    public static void checkProperty(String[] split, QueFormBean queFormBean) {
        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        if (split.length > 1 && split[1] != null && split[1].length() > 0) {
            String relatedPropertyValue = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            if (relatedPropertyValue != null && relatedPropertyValue.length() > 0
                    && (!relatedPropertyValue.equalsIgnoreCase(LabelConstants.NULL)
                    && !relatedPropertyValue.equalsIgnoreCase(LabelConstants.N_A)
                    && !relatedPropertyValue.equalsIgnoreCase(LabelConstants.NOT_APPLICABLE))) {
                if (split.length == 3 && split[2] != null && split[2].length() > 0) {
                    int checkValue = Integer.parseInt(split[2]);
                    int answer = Integer.parseInt(relatedPropertyValue);
                    if (answer >= checkValue) { // if greater then option yes
                        queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                    }
                } else {
                    queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                    queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                }
            }
        }
    }

    public static void checkString(String[] split, QueFormBean queFormBean) {
        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        if (split.length > 1 && split[1] != null && split[1].length() > 0) {
            //retrieve the check string value
            String checkValue = split[1];
            //Check the mentioned property's value
            String relatedPropertyValue = null;
            if (split.length == 3 && split[2].length() > 0) {
                relatedPropertyValue = SharedStructureData.relatedPropertyHashTable.get(split[2]);
            }
            if (relatedPropertyValue == null && queFormBean.getRelatedpropertyname() != null) {
                relatedPropertyValue = SharedStructureData.relatedPropertyHashTable.get(queFormBean.getRelatedpropertyname());
            }
            if (relatedPropertyValue != null && relatedPropertyValue.equalsIgnoreCase(checkValue)) {
                queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            }
        }
    }

    public static void checkWeight(String[] split, QueFormBean queFormBean) {
        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        if (split[1].contains("+")) {
            String[] relatedProperties = split[1].split("\\+");
            String kg;
            String gm;
            kg = SharedStructureData.relatedPropertyHashTable.get(relatedProperties[0]);
            gm = SharedStructureData.relatedPropertyHashTable.get(relatedProperties[1]);
            String totalWeight = kg + "." + gm;
            float finalWeight = Float.parseFloat(totalWeight);

            if (finalWeight < Float.parseFloat(split[2])) {
                queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            } else {
                queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            }
        } else {
            if (split[1].length() > 0) {
                //retrieve the check string value
                String relatedProperty = split[1];
                //Check the mentioned property's value
                String relatedPropertyValue = null;
                if (split.length == 3 && split[2].length() > 0) {
                    relatedPropertyValue = SharedStructureData.relatedPropertyHashTable.get(relatedProperty);
                }

                if (relatedPropertyValue != null) {
                    if (Float.parseFloat(relatedPropertyValue) < Float.parseFloat(split[2])) {
                        queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                    } else {
                        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                    }
                }
            }
        }
    }

    public static void membersCountCheck(QueFormBean queFormBean) {
        if (SharedStructureData.totalFamilyMembersCount == -1 || SharedStructureData.totalFamilyMembersCount <= queFormBean.getLoopCounter() + 1) {
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        } else {
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
        }
    }

    public static void loopContains(String[] split, QueFormBean queFormBean) {
        String givenAnswer = SharedStructureData.relatedPropertyHashTable.get(split[1]);
        if (queFormBean.getLoopCounter() > 0) {
            givenAnswer = SharedStructureData.relatedPropertyHashTable.get(split[1] + queFormBean.getLoopCounter());
        }
        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        if (givenAnswer != null && givenAnswer.equals(split[2])) {
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
        }
    }

    public static void loopCheckAgeBetween(String[] split, QueFormBean queFormBean) {
        if (split.length >= 3) {
            int minAge = Integer.parseInt(split[1]);
            int maxAge = Integer.parseInt(split[2]);
            String dateParam = null;
            if (split.length >= 4) {
                dateParam = split[3];
            }
            String dobDate = SharedStructureData.relatedPropertyHashTable.get(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.DOB, queFormBean.getLoopCounter()));
            String givenDateString = null;
            Date givenDate;
            if (split.length == 5) {
                givenDateString = SharedStructureData.relatedPropertyHashTable.get(split[4]);
            }
            if (givenDateString != null) {
                givenDate = new Date(Long.parseLong(givenDateString));
            } else {
                givenDate = new Date();
            }

            if (dobDate != null) {
                Date dob = UtilBean.clearTimeFromDate(new Date(Long.parseLong(dobDate)));
                if (dob != null) {
                    Calendar instance = Calendar.getInstance();
                    instance.setTime(UtilBean.clearTimeFromDate(givenDate));
                    queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                    queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                    if (dateParam == null || dateParam.equals("Y")) {
                        instance.add(Calendar.YEAR, -minAge);
                        Date maxDate = instance.getTime();
                        instance.add(Calendar.YEAR, minAge);
                        instance.add(Calendar.YEAR, -maxAge);
                        Date minDate = instance.getTime();
                        if (dob.before(minDate) || dob.after(maxDate)) {
                            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                        }
                    } else if (dateParam.equals("M")) {
                        instance.add(Calendar.MONTH, -minAge);
                        Date maxDate = instance.getTime();
                        instance.add(Calendar.MONTH, minAge);
                        instance.add(Calendar.MONTH, -maxAge);
                        Date minDate = instance.getTime();
                        if (dob.before(minDate) || dob.after(maxDate)) {
                            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                        }
                    } else if (dateParam.equals("D")) {
                        instance.add(Calendar.DAY_OF_MONTH, -minAge);
                        Date maxDate = instance.getTime();
                        instance.add(Calendar.DAY_OF_MONTH, minAge);
                        instance.add(Calendar.DAY_OF_MONTH, -maxAge);
                        Date minDate = instance.getTime();
                        if (dob.before(minDate) || dob.after(maxDate)) {
                            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                        }
                    }
                } else {
                    queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                    queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                }
            } else {
                queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            }

        }
    }

    public static void checkDateBetween(String[] split, QueFormBean queFormBean) {
        if (split.length >= 5) {
            int minDate = Integer.parseInt(split[1]);
            int maxDate = Integer.parseInt(split[2]);
            String dateParam = split[3];
            String givenDate1param = split[4];

            if (givenDate1param != null) {
                String givenDate1String = SharedStructureData.relatedPropertyHashTable.get(givenDate1param);
                if (queFormBean.getLoopCounter() > 0) {
                    givenDate1String = SharedStructureData.relatedPropertyHashTable.get(givenDate1param + queFormBean.getLoopCounter());
                }
                String givenDate2String = null;
                if (givenDate1String != null) {
                    Date givenDate1;
                    Date givenDate2;

                    if (split.length == 6) {
                        givenDate2String = SharedStructureData.relatedPropertyHashTable.get(split[5]);
                        if (queFormBean.getLoopCounter() > 0) {
                            givenDate2String = SharedStructureData.relatedPropertyHashTable.get(split[5] + queFormBean.getLoopCounter());
                        }
                    }

                    if (givenDate2String != null) {
                        givenDate2 = new Date(Long.parseLong(givenDate2String));
                    } else {
                        givenDate2 = new Date();
                    }

                    givenDate1 = new Date(Long.parseLong(givenDate1String));

                    int[] ageYearMonthDay = UtilBean.calculateAgeYearMonthDayOnGivenDate(givenDate1.getTime(), givenDate2.getTime());
                    queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                    queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());

                    if (dateParam == null || dateParam.equals("Y")) {
                        if (ageYearMonthDay[0] > maxDate || ageYearMonthDay[0] < minDate) {
                            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                        }
                    } else if (dateParam.equals("M")) {
                        int totalMonths = ageYearMonthDay[1] + (ageYearMonthDay[0] * 12);
                        if (totalMonths > maxDate || totalMonths < minDate) {
                            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                        }
                    } else if (dateParam.equals("D")) {
                        int totalDays = ageYearMonthDay[2] + (ageYearMonthDay[0] * 365) + (ageYearMonthDay[1] * 30);
                        if (totalDays > maxDate || totalDays < minDate) {
                            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                        }
                    }
                } else {
                    queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                    queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                }
            } else {
                queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            }
        } else {
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        }
    }

    public static void setAreaQuestionVisibility(String[] split, QueFormBean queFormBean) {
        if (split[1] != null) {
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            List<FieldValueMobDataBean> dataMapValues = SharedStructureData.mapDataMapLabelBean.get(split[1]);
            if (dataMapValues != null && !dataMapValues.isEmpty()) {
                List<LocationBean> locationsToBeRemoved = new ArrayList<>();
                List<FieldValueMobDataBean> allLocationOptionByLevel = new ArrayList<>();
                Integer level = SewaConstants.getLocationLevel().get(split[1]);
                List<LocationBean> retrieveLocationByLevel = new ArrayList<>(SharedStructureData.sewaService.retrieveLocationByLevel(level));
                if (split.length > 2 && split[2] != null && split[2].equalsIgnoreCase("filterRequired")) {
                    String locationId = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.LOCATION_ID);
                    if (locationId != null && !locationId.equals("null")) {
                        for (LocationBean locationBean : retrieveLocationByLevel) {
                            if (locationBean.getParent() != Integer.parseInt(locationId)) {
                                locationsToBeRemoved.add(locationBean);
                            }
                        }
                    }
                    retrieveLocationByLevel.removeAll(locationsToBeRemoved);
                }
                for (LocationBean loc : retrieveLocationByLevel) {
                    FieldValueMobDataBean locationOptionValue = new FieldValueMobDataBean();
                    locationOptionValue.setIdOfValue(loc.getActualID());
                    locationOptionValue.setValue(loc.getName());
                    allLocationOptionByLevel.add(locationOptionValue);
                }
                SharedStructureData.mapDataMapLabelBean.put(split[1], allLocationOptionByLevel);
                queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            }
        } else {
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
        }
    }

    public static void checkFamilyReverification(QueFormBean queFormBean) {
        if (SharedStructureData.currentFamilyDataBean == null ||
                (SharedStructureData.currentFamilyDataBean.getState().equals(FhsConstants.FHS_FAMILY_STATE_UNVERIFIED) ||
                        SharedStructureData.currentFamilyDataBean.getState().equals(FhsConstants.FHS_FAMILY_STATE_ORPHAN)) ||
                FhsConstants.FHS_NEW_CRITERIA_FAMILY_STATES.contains(SharedStructureData.currentFamilyDataBean.getState()) ||
                FhsConstants.FHS_VERIFIED_CRITERIA_FAMILY_STATES.contains(SharedStructureData.currentFamilyDataBean.getState())) {
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
        } else {
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        }

        if (SharedStructureData.currentFamilyDataBean != null) {
            List<MemberDataBean> memberDataBeans = SharedStructureData.currentFamilyDataBean.getMembers();
            if (memberDataBeans != null && !memberDataBeans.isEmpty()) {
                for (MemberDataBean memberDataBean : memberDataBeans) {
                    if (FhsConstants.FHS_IN_REVERIFICATION_MEMBER_STATES.contains(memberDataBean.getState())) {
                        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                        break;
                    }
                }
            }
        }
    }

    public static void isNull(String[] split, QueFormBean queFormBean) {
        if (split.length == 1) {
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        } else {
            boolean isNull = false;
            for (String propertyName : split[1].split(",")) {

                if (queFormBean.getLoopCounter() > 0) {
                    propertyName = propertyName + queFormBean.getLoopCounter();
                }

                isNull = SharedStructureData.relatedPropertyHashTable.get(propertyName) == null;
            }

            if (isNull) {
                queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            } else {
                queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            }
        }
    }

    public static void addAllOptionInSubcentreList(QueFormBean queFormBean) {
        QueFormBean nextQuestion = SharedStructureData.mapIndexQuestion.get(Integer.parseInt(queFormBean.getNext()));
        if (nextQuestion != null && nextQuestion.getQuestionTypeView() != null) {
            List<FieldValueMobDataBean> allLocationByParent = SharedStructureData.mapDataMapLabelBean.get("Level-5");
            boolean allOptionAdded = Boolean.FALSE;
            if (allLocationByParent != null) {
                for (FieldValueMobDataBean fieldValueMobDataBean : allLocationByParent) {
                    if (fieldValueMobDataBean.getIdOfValue() == 0) {
                        allOptionAdded = Boolean.TRUE;
                        break;
                    }
                }
            } else {
                allLocationByParent = new ArrayList<>();
            }

            if (!allOptionAdded) {
                FieldValueMobDataBean allOption = new FieldValueMobDataBean();
                allOption.setIdOfValue(0);
                allOption.setValue("All");
                allLocationByParent.add(0, allOption);
            }

            List<String> stringOptions = new ArrayList<>();
            List<OptionTagBean> allLocOptions = new ArrayList<>();
            stringOptions.add(UtilBean.getMyLabel(GlobalTypes.SELECT));
            for (FieldValueMobDataBean locBean : allLocationByParent) {
                OptionTagBean option = new OptionTagBean();
                option.setKey("" + locBean.getIdOfValue());
                option.setValue(locBean.getValue());
                allLocOptions.add(option);
                stringOptions.add(UtilBean.getMyLabel(locBean.getValue()));
            }

            try {
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
            } catch (NullPointerException ex) {
                Log.e(TAG, null, ex);
            }
        }
    }

    public static void clearScannedAadharDetails(String[] split, QueFormBean queFormBean) {
        String aadharScanned = SharedStructureData.relatedPropertyHashTable.get(
                UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.AADHAR_SCANNED, queFormBean.getLoopCounter()));
        String aadharAgreement = SharedStructureData.relatedPropertyHashTable.get(
                UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.AADHAR_AGREEMENT, queFormBean.getLoopCounter()));

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
                SharedStructureData.relatedPropertyHashTable.remove(RelatedPropertyNameConstants.DOB + queFormBean.getLoopCounter());

                questionIdForAadhar = getLoopId(questionIdForAadhar, queFormBean.getLoopCounter());
                questionIdForDob = getLoopId(questionIdForDob, queFormBean.getLoopCounter());
                questionIdForAgeDisplay = getLoopId(questionIdForAgeDisplay, queFormBean.getLoopCounter());

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
                    TextInputLayout aadharNumberView = MyStaticComponents.getEditText(
                            SharedStructureData.context, LabelConstants.ENTER_AADHAR_NUMBER, 1000, 12, InputType.TYPE_CLASS_NUMBER);
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
                    textViewDob.setText(UtilBean.getMyLabel(LabelConstants.SELECT_DATE));
                }

                if (ageDisplayQuestion != null) {
                    TextView ageDisplayTextView = (TextView) ageDisplayQuestion.getQuestionTypeView();
                    ageDisplayTextView.setText(UtilBean.getMyLabel(LabelConstants.DATE_NOT_YET_SELECTED));
                    if (dobQuestion != null) {
                        dobQuestion.setAnswer(null);
                    }
                }
            } else {
                SharedStructureData.relatedPropertyHashTable.remove(RelatedPropertyNameConstants.AADHAR_NUMBER);
                SharedStructureData.relatedPropertyHashTable.remove(RelatedPropertyNameConstants.DOB);

                questionIdForAadhar = getLoopId(questionIdForAadhar, queFormBean.getLoopCounter());
                questionIdForDob = getLoopId(questionIdForDob, queFormBean.getLoopCounter());
                questionIdForAgeDisplay = getLoopId(questionIdForAgeDisplay, queFormBean.getLoopCounter());

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
                    TextInputLayout aadharNumberView = MyStaticComponents.getEditText(SharedStructureData.context,
                            UtilBean.getMyLabel(LabelConstants.ENTER_AADHAR_NUMBER), 1000, 12, InputType.TYPE_CLASS_NUMBER);
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
                    textViewDob.setText(UtilBean.getMyLabel(LabelConstants.SELECT_DATE));
                }

                if (ageDisplayQuestion != null) {
                    TextView ageDisplayTextView = (TextView) ageDisplayQuestion.getQuestionTypeView();
                    ageDisplayTextView.setText(UtilBean.getMyLabel(LabelConstants.NOT_AVAILABLE));
                    if (dobQuestion != null) {
                        dobQuestion.setAnswer(null);
                    }
                }
            }
        }
    }

    public static void isDateWithin(String[] split, QueFormBean queFormBean) {
        try {
            long baseDateLong;
            long comparisonDateLong;
            String comparisonDateString = SharedStructureData.relatedPropertyHashTable.get(queFormBean.getRelatedpropertyname());

            if (split.length == 6) {
                String baseDateString = SharedStructureData.relatedPropertyHashTable.get(split[5]);

                if (baseDateString == null) {
                    queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                    queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                } else {
                    baseDateLong = Long.parseLong(baseDateString);
                    if (comparisonDateString != null) {
                        comparisonDateLong = Long.parseLong(comparisonDateString);
                    } else {
                        comparisonDateLong = new Date().getTime();
                    }

                    int years = Integer.parseInt(split[2]);
                    int months = Integer.parseInt(split[3]);
                    int days = Integer.parseInt(split[4]);

                    queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                    queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());

                    if (split[1].equalsIgnoreCase("Sub")) {
                        long actualDate = UtilBean.calculateDateMinus(baseDateLong, years, months, days);
                        if (actualDate < comparisonDateLong) {
                            queFormBean.setNext(queFormBean.getOptions().get(0).getNext()); //0
                            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());//0
                        }
                    } else if (split[1].equalsIgnoreCase("Add")) {
                        long actualDate = UtilBean.calculateDatePlus(baseDateLong, years, months, days);
                        if (actualDate > comparisonDateLong) {
                            queFormBean.setNext(queFormBean.getOptions().get(0).getNext()); //0
                            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey()); //0
                        }
                    }
                }
            } else {
                queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            }

        } catch (Exception e) {
            Log.e(TAG, null, e);
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        }
    }

    public static void isDateOutside(String[] split, QueFormBean queFormBean) {
        long baseDateLong;
        long comparisonDateLong;
        String comparisonDateString = SharedStructureData.relatedPropertyHashTable.get(queFormBean.getRelatedpropertyname());
        if (split.length == 6) {
            String baseDateString = SharedStructureData.relatedPropertyHashTable.get(split[5]);

            if (baseDateString == null) {
                queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            } else {
                baseDateLong = Long.parseLong(baseDateString);
                if (comparisonDateString != null) {
                    comparisonDateLong = Long.parseLong(comparisonDateString);
                } else {
                    comparisonDateLong = new Date().getTime();
                }

                int years = Integer.parseInt(split[2]);
                int months = Integer.parseInt(split[3]);
                int days = Integer.parseInt(split[4]);

                queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());

                if (split[1].equalsIgnoreCase("Sub")) {
                    long actualDate = UtilBean.calculateDateMinus(baseDateLong, years, months, days);
                    if (actualDate > comparisonDateLong || comparisonDateLong > baseDateLong) {
                        queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                    }
                } else if (split[1].equalsIgnoreCase("Add")) {
                    long actualDate = UtilBean.calculateDatePlus(baseDateLong, years, months, days);
                    if (actualDate < comparisonDateLong || comparisonDateLong < baseDateLong) {
                        queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                    }
                }
            }
        } else {
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
        }

    }

    public static void isDateBetween(String[] split, QueFormBean queFormBean) {
        long baseDate = 0;
        String baseDateValue = SharedStructureData.relatedPropertyHashTable.get(queFormBean.getRelatedpropertyname());
        if (baseDateValue != null) {
            baseDate = Long.parseLong(baseDateValue);
        }
        long compareDate = 0;
        if (split.length > 1) {
            String compareDateValue = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            if (compareDateValue != null) {
                compareDate = Long.parseLong(compareDateValue);
            }
        }

        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());

        if (split.length > 3) {
            String compareBy = split[2];
            int unit = 0;
            if (split[3] != null) {
                unit = Integer.parseInt(split[3]);
            }

            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(compareDate);
            switch (compareBy) {
                case "D":
                    instance.add(Calendar.DATE, unit);
                    if (instance.getTimeInMillis() > baseDate) {
                        queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                    }
                    break;
                case "M":
                    instance.add(Calendar.MONTH, unit);
                    if (instance.getTimeInMillis() > baseDate) {
                        queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                    }
                    break;
                case "Y":
                    instance.add(Calendar.YEAR, unit);
                    if (instance.getTimeInMillis() > baseDate) {
                        queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                    }
                    break;
                default:
            }
        }
    }

    public static void isDateIn(String[] split, QueFormBean queFormBean) {
        String answer = SharedStructureData.relatedPropertyHashTable.get(queFormBean.getRelatedpropertyname());
        if (answer == null) {
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            return;
        }

        long customCompareDate = 0;
        if (split.length > 1) {
            String compareDateValue = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            if (compareDateValue != null) {
                customCompareDate = Long.parseLong(compareDateValue);
            }
        }

        long date;
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

        // check if submitted date is in between calculated date from given parameters.
        // (here range is given in months. EG : isDateIn-Sub-1-2-3 means is submitted date is between today and date before 1 year 2 months 3 days?),
        // returns true if in range
        if (!UtilBean.isDateIn(date, split, customCompareDate)) {
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
        } else {
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        }
    }

    public static void isDateOut(String[] split, QueFormBean queFormBean) {
        String answer = SharedStructureData.relatedPropertyHashTable.get(queFormBean.getRelatedpropertyname());
        if (answer == null) {
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            return;
        }

        long customCompareDate = 0;
        if (split.length > 1) {
            String compareDateValue = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            if (compareDateValue != null) {
                customCompareDate = Long.parseLong(compareDateValue);
            }
        }

        long date;
        try {
            if (split.length > 6) {
                if (split[5] != null && split[6] != null) {
                    String[] answer1 = UtilBean.split(answer.trim(), split[5].trim());
                    int index = getIndex(split[6].trim());
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
            Log.e(TAG, null, e);
            date = 0L;
        }

        if (!UtilBean.isDateOut(date, split, customCompareDate)) {
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
        } else {
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        }
    }

    private static int getIndex(String split) {
        int index;
        try {
            index = Integer.parseInt(split.trim());
        } catch (NumberFormatException e) {
            index = 1;
        }
        return index;
    }

    public static void checkPretermBirth(QueFormBean queFormBean) {
        String deliveryDateString = SharedStructureData.relatedPropertyHashTable.get("deliveryDate");
        String lmpDateString = SharedStructureData.relatedPropertyHashTable.get("lmpDate");

        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        if (deliveryDateString == null || lmpDateString == null) {
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            return;
        }

        long lmpDateLong = Long.parseLong(lmpDateString);
        calendar.setTime(new Date(lmpDateLong));
        calendar.add(Calendar.DATE, 238);

        calendar2.setTime(new Date(lmpDateLong));
        calendar2.add(Calendar.DATE, 168);
        long deliveryDateLong = Long.parseLong(deliveryDateString);

        if (calendar.getTime().getTime() > deliveryDateLong && calendar2.getTime().getTime() < deliveryDateLong) {
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
        } else {
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        }
    }

    public static void updateCurrentGravida(String[] split) {
        QueFormBean gravidaConfirmationQuestion = SharedStructureData.mapIndexQuestion.get(Integer.valueOf(split[1]));
        QueFormBean memberSelectQuestion = SharedStructureData.mapIndexQuestion.get(Integer.valueOf(split[2]));
        if (gravidaConfirmationQuestion != null && gravidaConfirmationQuestion.getAnswer() != null
                && gravidaConfirmationQuestion.getAnswer().equals("2")) {
            int updatedCurrentGravida = 0;
            int loopCount = 0;
            String loopCountString = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.LOOP_COUNT);
            if (loopCountString != null) {
                loopCount = Integer.parseInt(loopCountString);
            }

            if (memberSelectQuestion != null && memberSelectQuestion.getAnswer() != null) {
                for (String newMemberId : memberSelectQuestion.getAnswer().toString().split(",")) {
                    if (!newMemberId.equals("ADDNEW")) {
                        updatedCurrentGravida++;
                    }
                }
            }

            updatedCurrentGravida = updatedCurrentGravida + loopCount;
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CURRENT_GRAVIDA, Integer.toString(updatedCurrentGravida));
            QueFormBean gravidaDisplayQuestion = SharedStructureData.mapIndexQuestion.get(Integer.valueOf(split[3]));
            if (gravidaDisplayQuestion != null) {
                TextView questionTypeView = (TextView) gravidaDisplayQuestion.getQuestionTypeView();
                questionTypeView.setText(String.format(Locale.getDefault(), "%d", updatedCurrentGravida));
                gravidaDisplayQuestion.setAnswer(Integer.toString(updatedCurrentGravida));
            }
        }
    }

    public static void identifyHighRiskAnc(String[] split) {
        int weightQueId = Integer.parseInt(split[1]);
        int haemoglobinQueId = Integer.parseInt(split[2]);
        int bpQueId = Integer.parseInt(split[3]);
        int dangerousSignQueId = Integer.parseInt(split[4]);
        int otherDangerousSignQueId = Integer.parseInt(split[5]);
        int displayMorbidityQueId = Integer.parseInt(split[6]);
        int urineAlbuminQueId = Integer.parseInt(split[7]);
        int urineSugarQueId = Integer.parseInt(split[8]);
        int sickleCellTestQueId = Integer.parseInt(split[9]);

        QueFormBean weightQue = SharedStructureData.mapIndexQuestion.get(weightQueId);
        QueFormBean haemoglobinQue = SharedStructureData.mapIndexQuestion.get(haemoglobinQueId);
        QueFormBean bpQue = SharedStructureData.mapIndexQuestion.get(bpQueId);
        QueFormBean dangerousSignQue = SharedStructureData.mapIndexQuestion.get(dangerousSignQueId);
        QueFormBean otherDangerousSignQue = SharedStructureData.mapIndexQuestion.get(otherDangerousSignQueId);
        QueFormBean displayMorbidityQue = SharedStructureData.mapIndexQuestion.get(displayMorbidityQueId);
        QueFormBean urineAlbuminQue = SharedStructureData.mapIndexQuestion.get(urineAlbuminQueId);
        QueFormBean urineSugarQue = SharedStructureData.mapIndexQuestion.get(urineSugarQueId);
        QueFormBean sickleCellTestQue = SharedStructureData.mapIndexQuestion.get(sickleCellTestQueId);

        Map<String, Object> mapOfAnswers = new HashMap<>();
        if (weightQue != null) {
            mapOfAnswers.put("weightAnswer", weightQue.getAnswer());
        }
        if (haemoglobinQue != null) {
            mapOfAnswers.put("haemoglobinAnswer", haemoglobinQue.getAnswer());
        }
        if (bpQue != null) {
            mapOfAnswers.put("bpAnswer", bpQue.getAnswer());
        }
        if (dangerousSignQue != null) {
            mapOfAnswers.put("dangerousSignAnswer", dangerousSignQue.getAnswer());
        }
        if (urineAlbuminQue != null) {
            mapOfAnswers.put("urineAlbuminAnswer", urineAlbuminQue.getAnswer());
        }
        if (urineSugarQue != null) {
            mapOfAnswers.put("urineSugarAnswer", urineSugarQue.getAnswer());
        }
        if (sickleCellTestQue != null) {
            mapOfAnswers.put("sickleCellTestAnswer", sickleCellTestQue.getAnswer());
        }
        if (otherDangerousSignQue != null) {
            mapOfAnswers.put("otherDangerousSignAnswer", otherDangerousSignQue.getAnswer());
        }

        if (split.length > 10) {
            int previousComplicationsQueId = Integer.parseInt(split[10]);
            int otherPreviousComplicationsQueId = Integer.parseInt(split[11]);

            QueFormBean previousComplicationsQue = SharedStructureData.mapIndexQuestion.get(previousComplicationsQueId);
            QueFormBean otherPreviousComplicationsQue = SharedStructureData.mapIndexQuestion.get(otherPreviousComplicationsQueId);

            if (previousComplicationsQue != null) {
                mapOfAnswers.put("previousComplicationsAnswer", previousComplicationsQue.getAnswer());
            }

            if (otherPreviousComplicationsQue != null) {
                mapOfAnswers.put("otherPreviousComplicationsAnswer", otherPreviousComplicationsQue.getAnswer());
            }
        }

        String identifyHighRiskForRchAnc = SharedStructureData.rchHighRiskService.identifyHighRiskForRchAnc(mapOfAnswers);

        if (displayMorbidityQue != null) {
            TextView textView = (TextView) displayMorbidityQue.getQuestionTypeView();
            if (textView != null) {
                if (identifyHighRiskForRchAnc.equals(RchConstants.NO_RISK_FOUND)) {
                    TextView instructionsView = displayMorbidityQue.getInstructionsView();
                    if (instructionsView != null) {
                        instructionsView.setText(RchConstants.NO_RISK_IDENTIFIED_IN_THIS_VISIT);
                    } else {
                        instructionsView = MyStaticComponents.generateInstructionView(textView.getContext(), RchConstants.NO_RISK_IDENTIFIED_IN_THIS_VISIT);
                        displayMorbidityQue.setInstructionsView(instructionsView);
                    }
                    textView.setText("");
                } else {
                    textView.setText(identifyHighRiskForRchAnc);
                }
            }
            displayMorbidityQue.setAnswer(identifyHighRiskForRchAnc);
        }
    }

    public static void identifyHighRiskPncChild(String[] split, QueFormBean queFormBean) {
        int dangerousSignQueId = Integer.parseInt(split[2]);
        int otherDangerousSignQueId = Integer.parseInt(split[3]);
        int displayMorbidityQueId = Integer.parseInt(split[4]);

        if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
            dangerousSignQueId = getLoopId(dangerousSignQueId, queFormBean.getLoopCounter());
            otherDangerousSignQueId = getLoopId(otherDangerousSignQueId, queFormBean.getLoopCounter());
            displayMorbidityQueId = getLoopId(displayMorbidityQueId, queFormBean.getLoopCounter());
        }

        QueFormBean dangerousSignQue = SharedStructureData.mapIndexQuestion.get(dangerousSignQueId);
        QueFormBean otherDangerousSignQue = SharedStructureData.mapIndexQuestion.get(otherDangerousSignQueId);
        QueFormBean displayMorbidityQue = SharedStructureData.mapIndexQuestion.get(displayMorbidityQueId);

        String identifyHighRiskForChildRchPnc = null;
        if (dangerousSignQue != null) {
            if (otherDangerousSignQue != null) {
                identifyHighRiskForChildRchPnc = SharedStructureData.rchHighRiskService.identifyHighRiskForChildRchPnc(dangerousSignQue.getAnswer(), otherDangerousSignQue.getAnswer());
            } else {
                identifyHighRiskForChildRchPnc = SharedStructureData.rchHighRiskService.identifyHighRiskForChildRchPnc(dangerousSignQue.getAnswer(), null);
            }
        }

        if (identifyHighRiskForChildRchPnc == null) {
            return;
        }

        if (displayMorbidityQue == null) {
            SharedStructureData.relatedPropertyHashTable.put(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.DISPLAY_MORBIDITY_CHILD, queFormBean.getLoopCounter()),
                    identifyHighRiskForChildRchPnc);
        } else {
            TextView textView = (TextView) displayMorbidityQue.getQuestionTypeView();
            if (textView != null) {
                if (identifyHighRiskForChildRchPnc.equals(RchConstants.NO_RISK_FOUND)) {
                    TextView instructionsView = displayMorbidityQue.getInstructionsView();
                    if (instructionsView != null) {
                        instructionsView.setText(RchConstants.NO_RISK_IDENTIFIED_IN_THIS_VISIT);
                    } else {
                        instructionsView = MyStaticComponents.generateInstructionView(textView.getContext(), RchConstants.NO_RISK_IDENTIFIED_IN_THIS_VISIT);
                        displayMorbidityQue.setInstructionsView(instructionsView);
                    }
                    textView.setText("");
                } else {
                    textView.setText(identifyHighRiskForChildRchPnc);
                }
            }
        }
    }

    public static void identifyHighRiskPncMother(String[] split) {
        int dangerousSignQueId = Integer.parseInt(split[1]);
        int otherDangerousSignQueId = Integer.parseInt(split[2]);
        int displayMorbidityQueId = Integer.parseInt(split[3]);

        QueFormBean dangerousSignQue = SharedStructureData.mapIndexQuestion.get(dangerousSignQueId);
        QueFormBean otherDangerousSignQue = SharedStructureData.mapIndexQuestion.get(otherDangerousSignQueId);
        QueFormBean displayMorbidityQue = SharedStructureData.mapIndexQuestion.get(displayMorbidityQueId);

        String identifyHighRiskForMotherRchPnc = null;
        if (dangerousSignQue != null) {
            if (otherDangerousSignQue != null) {
                identifyHighRiskForMotherRchPnc = SharedStructureData.rchHighRiskService.identifyHighRiskForMotherRchPnc(dangerousSignQue.getAnswer(), otherDangerousSignQue.getAnswer());
            } else {
                identifyHighRiskForMotherRchPnc = SharedStructureData.rchHighRiskService.identifyHighRiskForMotherRchPnc(dangerousSignQue.getAnswer(), null);
            }
        }

        if (identifyHighRiskForMotherRchPnc == null) {
            return;
        }

        if (displayMorbidityQue != null) {
            TextView textView = (TextView) displayMorbidityQue.getQuestionTypeView();
            if (textView != null) {
                if (identifyHighRiskForMotherRchPnc.equals(RchConstants.NO_RISK_FOUND)) {
                    TextView instructionsView = displayMorbidityQue.getInstructionsView();
                    if (instructionsView != null) {
                        instructionsView.setText(RchConstants.NO_RISK_IDENTIFIED_IN_THIS_VISIT);
                    } else {
                        instructionsView = MyStaticComponents.generateInstructionView(textView.getContext(), RchConstants.NO_RISK_IDENTIFIED_IN_THIS_VISIT);
                        displayMorbidityQue.setInstructionsView(instructionsView);
                    }
                    textView.setText("");
                } else {
                    textView.setText(identifyHighRiskForMotherRchPnc);
                }
            }
            displayMorbidityQue.setAnswer(identifyHighRiskForMotherRchPnc);
        }
    }

    public static void identifyHighRiskWpdChild(String[] split, QueFormBean queFormBean) {
        int weightQueId = Integer.parseInt(split[1]);
        int displayMorbidityQueId = Integer.parseInt(split[2]);
        int congenitalDeformityQueId = Integer.parseInt(split[3]);

        if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
            weightQueId = getLoopId(weightQueId, queFormBean.getLoopCounter());
            congenitalDeformityQueId = getLoopId(congenitalDeformityQueId, queFormBean.getLoopCounter());
            displayMorbidityQueId = getLoopId(displayMorbidityQueId, queFormBean.getLoopCounter());
        }

        QueFormBean weightQue = SharedStructureData.mapIndexQuestion.get(weightQueId);
        QueFormBean displayMorbidityQue = SharedStructureData.mapIndexQuestion.get(displayMorbidityQueId);
        QueFormBean congenitalDeformityQue = SharedStructureData.mapIndexQuestion.get(congenitalDeformityQueId);

        String identifyHighRiskForChildRchWpd = null;
        if (weightQue != null) {
            identifyHighRiskForChildRchWpd = SharedStructureData.rchHighRiskService.identifyHighRiskForChildRchWpd(weightQue.getAnswer(), congenitalDeformityQue.getAnswer());
        }

        if (identifyHighRiskForChildRchWpd == null) {
            return;
        }

        if (displayMorbidityQue != null) {
            TextView textView = (TextView) displayMorbidityQue.getQuestionTypeView();
            if (textView != null) {
                if (identifyHighRiskForChildRchWpd.equals(RchConstants.NO_RISK_FOUND)) {
                    TextView instructionsView = displayMorbidityQue.getInstructionsView();
                    if (instructionsView != null) {
                        instructionsView.setText(RchConstants.NO_RISK_IDENTIFIED_IN_THIS_VISIT);
                    } else {
                        instructionsView = MyStaticComponents.generateInstructionView(textView.getContext(), RchConstants.NO_RISK_IDENTIFIED_IN_THIS_VISIT);
                        displayMorbidityQue.setInstructionsView(instructionsView);
                    }
                    textView.setText("");
                } else {
                    textView.setText(identifyHighRiskForChildRchWpd);
                }
            }
            displayMorbidityQue.setAnswer(identifyHighRiskForChildRchWpd);
        } else {
            if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                SharedStructureData.relatedPropertyHashTable.put(
                        RelatedPropertyNameConstants.DISPLAY_MORBIDITY_CHILD + queFormBean.getLoopCounter(), identifyHighRiskForChildRchWpd);
            } else {
                SharedStructureData.relatedPropertyHashTable.put(
                        RelatedPropertyNameConstants.DISPLAY_MORBIDITY_CHILD, identifyHighRiskForChildRchWpd);
            }
        }
    }

    public static void checkContainsLoop(String[] split, QueFormBean queFormBean) {
        String mainAnswer;
        if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
            mainAnswer = SharedStructureData.relatedPropertyHashTable.get(split[1] + queFormBean.getLoopCounter()); // get answer of property
        } else {
            mainAnswer = SharedStructureData.relatedPropertyHashTable.get(split[1]); // get answer of property
        }
        queFormBean.setNext(queFormBean.getOptions().get(1).getNext()); // set default false
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());

        if (mainAnswer != null) {
            List<String> asList = Arrays.asList(UtilBean.split(mainAnswer, GlobalTypes.COMMA));
            if (!asList.isEmpty()) {
                for (int i = 2; i < split.length; i++) {
                    if (asList.contains(split[i])) {
                        queFormBean.setNext(queFormBean.getOptions().get(0).getNext()); // set default true
                        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                        break;
                    }
                }
            }
        }
    }

    public static void checkIfLmpIsAvailable(QueFormBean queFormBean) {
        String lastLmp = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.LAST_LMP);
        if (lastLmp == null) {
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
        } else {
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
        }
    }

    public static void putRemainingVaccines(QueFormBean queFormBean) {
        String remainingVaccines;
        if (queFormBean.getLoopCounter() > 0) {
            remainingVaccines = SharedStructureData.relatedPropertyHashTable.get(
                    RelatedPropertyNameConstants.REMAINING_VACCINES + queFormBean.getLoopCounter());
        } else {
            remainingVaccines = SharedStructureData.relatedPropertyHashTable.get(
                    RelatedPropertyNameConstants.REMAINING_VACCINES);
        }

        String deliveryDateString = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.DELIVERY_DATE);
        if (remainingVaccines == null && deliveryDateString != null && !deliveryDateString.isEmpty() && !deliveryDateString.equals("null")) {
            Date deliveryDate = new Date(Long.parseLong(deliveryDateString));
            Set<String> dueImmunisationsForChild = SharedStructureData.immunisationService.getDueImmunisationsForChild(deliveryDate, null);
            if (dueImmunisationsForChild != null && !dueImmunisationsForChild.isEmpty()) {
                if (dueImmunisationsForChild.contains(RchConstants.MEASLES_RUBELLA_1)) {
                    dueImmunisationsForChild.remove(RchConstants.MEASLES_RUBELLA_1);
                    SharedStructureData.relatedPropertyHashTable.put("measlesRubella1Due", "Yes");
                }

                if (dueImmunisationsForChild.contains(RchConstants.MEASLES_RUBELLA_2)) {
                    dueImmunisationsForChild.remove(RchConstants.MEASLES_RUBELLA_2);
                    SharedStructureData.relatedPropertyHashTable.put("measlesRubella2Due", "Yes");
                }

                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.REMAINING_VACCINES, queFormBean.getLoopCounter()),
                        dueImmunisationsForChild.toString().replace("[", "").replace("]", ""));
                if (queFormBean.getLoopCounter() == 0) {
                    Integer queId = Integer.valueOf(queFormBean.getNext());
                    QueFormBean nextQue = SharedStructureData.mapIndexQuestion.get(queId);
                    PageFormBean pageFromNext = DynamicUtils.getPageFromNext(getLoopId(nextQue));
                    if (pageFromNext != null) {
                        MyVaccination vaccination = pageFromNext.getMyVaccination();
                        vaccination.reSet(new LinkedList<>(dueImmunisationsForChild));
                    }
                }
            }
        }
    }

    public static void checkIfThirdTrimester(QueFormBean queFormBean) {
        String lmpDateString = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.LMP_DATE);
        if (lmpDateString != null && lmpDateString.equals("null")) {
            Date lmpDate = new Date(Long.parseLong(lmpDateString));
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -6);
            if (calendar.getTime().after(lmpDate)) {
                queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
            } else {
                queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
            }
        } else {
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
        }
    }

    public static void checkAgeIfWronglyRegistered(QueFormBean queFormBean) {
        Date dob = new Date();
        String dobString = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.DOB);
        if (dobString != null) {
            dob = new Date(Long.parseLong(dobString));
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -18);
        Date dateBefore18Years = calendar.getTime();
        calendar.add(Calendar.YEAR, -32);
        Date dateBefore50Years = calendar.getTime();
        if (dob.after(dateBefore50Years) && dob.before(dateBefore18Years)) {
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
        } else {
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
        }
    }

    public static void checkAgeLessThan20(QueFormBean queFormBean) {
        String loopCounter = String.valueOf(queFormBean.getLoopCounter());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SharedStructureData.context);
        SharedPreferences.Editor edit = sharedPreferences.edit();

        HashSet<String> strings = new HashSet<>();
        Set<String> aliveMembers = sharedPreferences.getStringSet(RelatedPropertyNameConstants.LOOP_COUNTER_FOR_ALIVE_MEMBERS, strings);
        Set<String> femaleMarriedMembers = sharedPreferences.getStringSet(RelatedPropertyNameConstants.LOOP_COUNTER_FOR_FEMALE_MARRIED_MEMBERS, strings);
        Set<String> wifeMembers = sharedPreferences.getStringSet(RelatedPropertyNameConstants.LOOP_COUNTER_FOR_WIFE_MEMBERS, strings);
        Set<String> maleMarriedMembers = sharedPreferences.getStringSet(RelatedPropertyNameConstants.LOOP_COUNTER_FOR_MALE_MARRIED_MEMBERS, strings);

        String motherId = SharedStructureData.relatedPropertyHashTable.get(
                UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.MOTHER_ID, queFormBean.getLoopCounter()));

        if (motherId == null) {
            Objects.requireNonNull(aliveMembers).add(loopCounter);
        }

        String gender = SharedStructureData.relatedPropertyHashTable.get(
                UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.ANS_12, queFormBean.getLoopCounter()));
        String maritalStatus = SharedStructureData.relatedPropertyHashTable.get(
                UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.DEFAULT_MARITAL_STATUS, queFormBean.getLoopCounter()));

        //For adding female married members
        // Marital Status code for MARRIED is 629 and for WIDOW is 641 in DB. Needs to change if changed in DB
        if (gender != null && gender.equals("2")) {
            if (maritalStatus != null && (maritalStatus.equals("629") || maritalStatus.equals("641"))) {
                Objects.requireNonNull(femaleMarriedMembers).add(loopCounter);

                if (SharedStructureData.relatedPropertyHashTable.get(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.HUSBAND_ID, queFormBean.getLoopCounter())) == null) {
                    Objects.requireNonNull(wifeMembers).add(loopCounter);
                }
            } else if (Objects.requireNonNull(femaleMarriedMembers).contains(loopCounter)) {
                femaleMarriedMembers.remove(loopCounter);
            }

            if (Objects.requireNonNull(maleMarriedMembers).contains(loopCounter)) {
                maleMarriedMembers.remove(loopCounter);
            }
        }

        //For adding male married members
        // Marital Status code for MARRIED is 629 in DB. Needs to change if changed in DB
        if (gender != null && gender.equals("1")) {
            if (maritalStatus != null && (maritalStatus.equals("629"))) {
                Objects.requireNonNull(maleMarriedMembers).add(loopCounter);
            } else if (Objects.requireNonNull(maleMarriedMembers).contains(loopCounter)) {
                maleMarriedMembers.remove(loopCounter);
            }

            if (Objects.requireNonNull(femaleMarriedMembers).contains(loopCounter)) {
                femaleMarriedMembers.remove(loopCounter);
            }
            if (Objects.requireNonNull(wifeMembers).contains(loopCounter)) {
                wifeMembers.remove(loopCounter);
            }
        }

        edit.putStringSet(RelatedPropertyNameConstants.LOOP_COUNTER_FOR_ALIVE_MEMBERS, aliveMembers);
        edit.putStringSet(RelatedPropertyNameConstants.LOOP_COUNTER_FOR_FEMALE_MARRIED_MEMBERS, femaleMarriedMembers);
        edit.putStringSet(RelatedPropertyNameConstants.LOOP_COUNTER_FOR_WIFE_MEMBERS, wifeMembers);
        edit.putStringSet(RelatedPropertyNameConstants.LOOP_COUNTER_FOR_MALE_MARRIED_MEMBERS, maleMarriedMembers);
        edit.apply();
    }

    public static void removeMemberCountForRemovedMember() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SharedStructureData.context);
        int loopCounter = SharedStructureData.loopBakCounter;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        updateMemberCountSetTillLastLoopCount(loopCounter, RelatedPropertyNameConstants.LOOP_COUNTER_FOR_ALIVE_MEMBERS, sharedPreferences, edit);
        updateMemberCountSetTillLastLoopCount(loopCounter, RelatedPropertyNameConstants.LOOP_COUNTER_FOR_FEMALE_MARRIED_MEMBERS, sharedPreferences, edit);
        updateMemberCountSetTillLastLoopCount(loopCounter, RelatedPropertyNameConstants.LOOP_COUNTER_FOR_WIFE_MEMBERS, sharedPreferences, edit);
        updateMemberCountSetTillLastLoopCount(loopCounter, RelatedPropertyNameConstants.LOOP_COUNTER_FOR_MALE_MARRIED_MEMBERS, sharedPreferences, edit);
        edit.apply();
    }

    public static void updateMemberCountSetTillLastLoopCount(int loopCount, String key, SharedPreferences sharedPreferences, SharedPreferences.Editor editor) {
        Set<String> members = new HashSet<>();
        HashSet<String> strings = new HashSet<>();
        Set<String> stringSet = sharedPreferences.getStringSet(key, strings);
        if (stringSet != null) {
            for (String memberCount : stringSet) {
                if (Integer.parseInt(memberCount) <= loopCount) {
                    members.add(memberCount);
                }
            }
        }
        editor.putStringSet(key, members);
    }

    public static void checkIfAnyChildExisits(QueFormBean queFormBean) {
        removeMemberCountForRemovedMember();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SharedStructureData.context);
        Set<String> femaleMarriedMembers = sharedPreferences.getStringSet(RelatedPropertyNameConstants.LOOP_COUNTER_FOR_FEMALE_MARRIED_MEMBERS, null);
        Set<String> membersUnderTwenty = sharedPreferences.getStringSet(RelatedPropertyNameConstants.LOOP_COUNTER_FOR_ALIVE_MEMBERS, null);

        if (membersUnderTwenty != null && !membersUnderTwenty.isEmpty()
                && femaleMarriedMembers != null && !femaleMarriedMembers.isEmpty()) {

            String counter;
            String motherCounter;
            for (String loopCounter : membersUnderTwenty) {
                counter = loopCounter;
                if (counter.equals("0")) {
                    counter = "";
                }

                String dob = SharedStructureData.relatedPropertyHashTable.get("dob" + counter);

                if (dob != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(Long.parseLong(dob));
                    calendar.add(Calendar.YEAR, -12);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    long dateBefore12years = calendar.getTime().getTime();

                    for (String motherLoopCounter : femaleMarriedMembers) {
                        if (loopCounter.equals(motherLoopCounter)) {
                            continue;
                        }

                        motherCounter = motherLoopCounter;
                        if (motherLoopCounter.equals("0")) {
                            motherCounter = "";
                        }

                        dob = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.DOB + motherCounter);
                        if (dob != null) {
                            calendar.setTimeInMillis(Long.parseLong(dob));
                            calendar.set(Calendar.HOUR_OF_DAY, 0);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MILLISECOND, 0);
                            if (calendar.getTimeInMillis() <= dateBefore12years) {
                                queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                                queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                                QueFormBean next = SharedStructureData.mapIndexQuestion.get(Integer.valueOf(queFormBean.getNext()));
                                if (next != null) {
                                    LinearLayout motherChildRelationshipView = MyStaticComponents.getLinearLayout(SharedStructureData.context, 10001, LinearLayout.VERTICAL, null);
                                    motherChildRelationshipView.addView(MyStaticComponents.generateQuestionView(null, null, SharedStructureData.context, next.getQuestion()));
                                    motherChildRelationshipView.addView(MyDynamicComponents.getMotherChildRelationshipView(SharedStructureData.context, next));
                                    next.setQuestionTypeView(motherChildRelationshipView);
                                    next.setQuestionUIFrame(motherChildRelationshipView);
                                    PageFormBean pageFormBean = SharedStructureData.mapIndexPage.get(Integer.valueOf(next.getPage()));
                                    if (pageFormBean != null) {
                                        LinearLayout pageLayout = pageFormBean.getPageLayout(true, 0);
                                        if (pageLayout != null) {
                                            pageLayout.removeAllViews();
                                            pageLayout.addView(motherChildRelationshipView);
                                        }
                                    }
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }

        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
    }

    public static void checkIfAnyFemaleMarriedMembersExists(QueFormBean queFormBean) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SharedStructureData.context);
        Set<String> femaleMarriedMembers = sharedPreferences.getStringSet(RelatedPropertyNameConstants.LOOP_COUNTER_FOR_WIFE_MEMBERS, null);
        if (femaleMarriedMembers == null || femaleMarriedMembers.isEmpty()) {
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
        } else {
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
            QueFormBean next = SharedStructureData.mapIndexQuestion.get(Integer.valueOf(queFormBean.getNext()));
            if (next != null) {
                LinearLayout husbandWifeRelationshipView = MyStaticComponents.getLinearLayout(SharedStructureData.context, 10001, LinearLayout.VERTICAL, null);
                husbandWifeRelationshipView.addView(MyStaticComponents.generateQuestionView(null, null, SharedStructureData.context, next.getQuestion()));
                husbandWifeRelationshipView.addView(MyDynamicComponents.getHusbandWifeRelationshipView(SharedStructureData.context, next));
                next.setQuestionTypeView(husbandWifeRelationshipView);
                next.setQuestionUIFrame(husbandWifeRelationshipView);
                PageFormBean pageFormBean = SharedStructureData.mapIndexPage.get(Integer.valueOf(next.getPage()));
                if (pageFormBean != null) {
                    LinearLayout pageLayout = pageFormBean.getPageLayout(true, -1);
                    if (pageLayout != null) {
                        pageLayout.removeAllViews();
                        pageLayout.addView(husbandWifeRelationshipView);
                    }
                }
            }
        }
    }

    public static void checkFamilyState(String[] split, QueFormBean queFormBean) {
        if (split.length > 1) {
            if (SharedStructureData.currentFamilyDataBean != null) {
                String state = SharedStructureData.currentFamilyDataBean.getState();
                switch (split[1]) {
                    case "U":
                        if (state.equals(FhsConstants.FHS_FAMILY_STATE_UNVERIFIED)) {
                            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                        } else {
                            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                        }
                        break;
                    case "V":
                        if (FhsConstants.FHS_VERIFIED_CRITERIA_FAMILY_STATES.contains(state)) {
                            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                        } else {
                            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                        }
                        break;
                    case "A":
                        if (FhsConstants.FHS_ARCHIVED_CRITERIA_FAMILY_STATES.contains(state)) {
                            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                        } else {
                            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                        }
                        break;
                    case "N":
                        if (FhsConstants.FHS_NEW_CRITERIA_FAMILY_STATES.contains(state)) {
                            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                        } else {
                            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                        }
                        break;
                    case "R":
                        if (FhsConstants.FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES.contains(state)) {
                            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                        } else {
                            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                        }
                        break;
                    case "VN":
                        if (FhsConstants.FHS_NEW_CRITERIA_FAMILY_STATES.contains(state)
                                || FhsConstants.FHS_VERIFIED_CRITERIA_FAMILY_STATES.contains(state)) {
                            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                        } else {
                            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                        }
                        break;
                    default:
                        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                        break;
                }
            } else {
                if ("U".equals(split[1])) {
                    queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                    queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                } else {
                    queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                    queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                }
            }
        } else {
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
        }
    }

    public static void checkBpValue(String[] split, QueFormBean queFormBean) {
        if (split.length == 4) {
            String answer = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            if (answer != null) {
                int systolicBp = Integer.parseInt(split[2]);
                int diastolicBp = Integer.parseInt(split[3]);
                String[] subString = answer.split("-");
                if (subString.length > 2) {
                    if (Integer.parseInt(subString[1]) >= systolicBp || Integer.parseInt(subString[2]) >= diastolicBp) {
                        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                        queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                    } else {
                        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                    }
                }
            }
        }
    }

    public static void checkIfContainOther(String[] split, QueFormBean queFormBean) {
        if (split.length == 2) {
            String answer;
            if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                answer = SharedStructureData.relatedPropertyHashTable.get(split[1] + queFormBean.getLoopCounter());
            } else {
                answer = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            }
            if (answer != null) {
                String[] answerSplit = answer.split(GlobalTypes.COMMA);
                List<FieldValueMobDataBean> labelDataBeans = SharedStructureData.sewaService.getFieldValueMobDataBeanByDataMap("diseaseHistoryList");
                List<Integer> diseaseIndexList = new ArrayList<>();
                boolean isContainOther = false;
                for (String disease : answerSplit) {
                    diseaseIndexList.add(Integer.valueOf(disease));
                }
                for (FieldValueMobDataBean data : labelDataBeans) {
                    if (data.getValue().equals("Other")) {
                        int index = diseaseIndexList.indexOf(data.getIdOfValue());
                        if (index != -1) {
                            isContainOther = true;
                        }
                    }
                }
                if (isContainOther) {
                    queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                    queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                } else {
                    queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                    queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                }
            }
        }
    }

    public static void checkMemberState(String[] split, QueFormBean queFormBean) {
        if (split.length > 2) {
            String relatedProperty = split[1];
            String state;
            if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                state = SharedStructureData.relatedPropertyHashTable.get(relatedProperty + queFormBean.getLoopCounter());
            } else {
                state = SharedStructureData.relatedPropertyHashTable.get(relatedProperty);
            }
            if (state != null) {
                switch (split[2]) {
                    case "U":
                        if (state.equals(FhsConstants.FHS_MEMBER_STATE_UNVERIFIED)) {
                            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                        } else {
                            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                        }
                        break;
                    case "V":
                        if (FhsConstants.FHS_VERIFIED_CRITERIA_MEMBER_STATES.contains(state)) {
                            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                        } else {
                            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                        }
                        break;
                    case "N":
                        if (FhsConstants.FHS_NEW_CRITERIA_MEMBER_STATES.contains(state)) {
                            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                        } else {
                            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                        }
                        break;
                    case "VN":
                        if (FhsConstants.FHS_ACTIVE_CRITERIA_MEMBER_STATES.contains(state)) {
                            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                        } else {
                            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                        }
                        break;
                    default:
                        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                        break;
                }
            } else {
                if ("U".equals(split[1])) {
                    queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                    queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                } else {
                    queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
                    queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
                }
            }
        } else {
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
        }
    }

    public static void checkNewFamily(QueFormBean queFormBean) {
        if (SharedStructureData.currentFamilyDataBean != null) {
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
        } else {
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
        }
    }

    public static void contactGynecologist() {
        if (RchConstants.MEMBER_STATUS_AVAILABLE.equals(SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.MEMBER_STATUS))) {
            String notificationId = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.NOTIFICATION_ID);
            if (notificationId != null) {
                NotificationBean notificationBean = SharedStructureData.sewaService.retrieveNotificationById(Long.valueOf(notificationId));
                if (notificationBean.getCustomField().equals("1")
                        && notificationBean.getState().equals(NotificationConstants.NOTIFICATION_STATE_RESCHEDULE)) {
                    View.OnClickListener listener = v -> myAlertDialog.dismiss();
                    myAlertDialog = new MyAlertDialog(SharedStructureData.context,
                            LabelConstants.REFERRED_TO_GYNECOLOGIST,
                            listener, DynamicUtils.BUTTON_OK);
                    myAlertDialog.show();
                }
            }
        }
    }

    public static void addMotherList(QueFormBean queFormBean) {
        List<MemberDataBean> mothers = new ArrayList<>();
        String nextQuestionId = queFormBean.getNext();
        if (nextQuestionId != null) {
            List<MemberDataBean> memberDataBeans = SharedStructureData.sewaFhsService.retrieveMemberDataBeansExceptArchivedAndDeadByFamilyId(SharedStructureData.currentFamilyDataBean.getFamilyId());
            for (MemberDataBean memberDataBean : memberDataBeans) {
                if (memberDataBean.getGender() != null && memberDataBean.getGender().equalsIgnoreCase("F")
                        && memberDataBean.getMaritalStatus() != null
                        && (memberDataBean.getMaritalStatus().equals("629") || memberDataBean.getMaritalStatus().equals("641"))) {
                    mothers.add(memberDataBean);
                }
            }

            List<String> stringOptions = new ArrayList<>();
            List<OptionTagBean> allMotherOptions = new ArrayList<>();
            OptionTagBean selectOption = new OptionTagBean();
            selectOption.setKey("-1");
            selectOption.setValue(UtilBean.getMyLabel(GlobalTypes.SELECT));
            allMotherOptions.add(selectOption);
            stringOptions.add(UtilBean.getMyLabel(GlobalTypes.SELECT));
            if (!mothers.isEmpty()) {
                for (MemberDataBean mother : mothers) {
                    OptionTagBean option = new OptionTagBean();
                    option.setKey("" + mother.getId());
                    option.setValue(mother.getFirstName() + " " + mother.getLastName());
                    allMotherOptions.add(option);
                    stringOptions.add(mother.getFirstName() + " " + mother.getLastName());
                }
            }

            OptionTagBean option1 = new OptionTagBean();
            option1.setKey("0");
            option1.setValue(UtilBean.getMyLabel(GlobalTypes.NOT_AVAILABLE));
            allMotherOptions.add(option1);
            stringOptions.add(UtilBean.getMyLabel(GlobalTypes.NOT_AVAILABLE));

            QueFormBean next = SharedStructureData.mapIndexQuestion.get(Integer.valueOf(queFormBean.getNext()));
            if (next != null) {
                next.setOptions(allMotherOptions);
                Spinner spin = (Spinner) next.getQuestionTypeView();
                if (spin != null) {
                    spin.setAdapter(createAdapter(stringOptions));
                    if (SharedStructureData.relatedPropertyHashTable.get(next.getRelatedpropertyname()) != null) {
                        for (OptionTagBean optionTagBean : allMotherOptions) {
                            if (optionTagBean.getKey().equals(SharedStructureData.relatedPropertyHashTable.get(next.getRelatedpropertyname()))) {
                                spin.setSelection(stringOptions.indexOf(optionTagBean.getValue()));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void calculateCbacScore() {
        Integer score = SharedStructureData.ncdScoreService.calculateCbacScoreForMember();
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CBAC_SCORE, score.toString());

        //Currently here is static Question Id for CBAC Score Questions & needs to be changes if changed in Sheet
        int nextQueId;
        if (score <= 4) {
            nextQueId = 50;
        } else {
            nextQueId = 51;
        }

        QueFormBean nextQue = SharedStructureData.mapIndexQuestion.get(nextQueId);
        if (nextQue != null) {
            TextView textView = (TextView) nextQue.getQuestionTypeView();
            if (textView != null)
                textView.setText(String.format(Locale.getDefault(), "%d", score));
            nextQue.setAnswer(score);
        }
    }

    public static void checkMisoprostol(QueFormBean queFormBean) {
        String deliveryPlace = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.DELIVERY_PLACE);
        String healthInfrastructureType = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.HEALTH_INFRASTRUCTURE_TYPE);
        if ((deliveryPlace != null && deliveryPlace.equals(RchConstants.HOME))
                || (healthInfrastructureType != null && healthInfrastructureType.equals(RchConstants.INFRA_TYPE_SC))) {
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
        } else {
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
        }
    }

    public static void resetProperty(String[] split, QueFormBean queFormBean) {
        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
        queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
        if (split.length > 1 && split[1] != null) {
            SharedStructureData.relatedPropertyHashTable.remove(split[1]);
        }
    }

    public static void checkContainsMultiple(String[] split, QueFormBean queFormBean) {
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
        if (split.length > 1) {
            String answer = split[1];
            for (int i = 2; i < split.length; i++) {
                if (answer.equals(SharedStructureData.relatedPropertyHashTable.get(split[i]))) {
                    queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                    queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                    break;
                }
            }
        }
    }

    public static void checkChiranjeeviEligibility(QueFormBean queFormBean) {
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());

        if (SharedStructureData.selectedHealthInfra != null
                && SharedStructureData.selectedHealthInfra.getTypeId().equals(RchConstants.INFRA_PRIVATE_HOSPITAL)
                && Boolean.TRUE.equals(SharedStructureData.selectedHealthInfra.getChiranjeevi())
                && SharedStructureData.currentRchFamilyDataBean != null
                && (SharedStructureData.currentRchFamilyDataBean.getBplFlag() || SharedStructureData.currentRchFamilyDataBean.getCaste().equals("625"))) {
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
        }
    }

    public static void addAdditionDischargeQuestion(QueFormBean queFormBean) {
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());

        if (SharedStructureData.selectedHealthInfra != null
                && RchConstants.GOVERNMENT_INSTITUTIONS.contains(SharedStructureData.selectedHealthInfra.getTypeId())) {
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
        }
    }

    public static void calculateSDScore(String[] split, QueFormBean queFormBean) {
        if (split.length >= 4) {
            String relatedProperty = queFormBean.getRelatedpropertyname();
            String strHeight = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            String strWeight = SharedStructureData.relatedPropertyHashTable.get(split[2]);
            String gender = SharedStructureData.relatedPropertyHashTable.get(split[3]);

            int height = 0;
            if (strHeight != null) {
                height = Integer.parseInt(strHeight);
            }

            float weight = 0f;
            if (strWeight != null && !strWeight.equals(GlobalTypes.NO_WEIGHT)) {
                weight = Float.parseFloat(strWeight);
            }

            String sdScore = SDScoreUtil.calculateSDScore(height, weight, Objects.requireNonNull(gender));
            if (sdScore != null) {
                SharedStructureData.relatedPropertyHashTable.put(relatedProperty, sdScore);
                String sdScoreForDisplay = SDScoreUtil.getSDScoreForDisplay(sdScore);
                if (split.length == 5) {
                    QueFormBean sdScoreQue = SharedStructureData.mapIndexQuestion.get(Integer.valueOf(split[4]));
                    if (sdScoreQue != null) {
                        if (!sdScore.equals(RchConstants.SD_SCORE_CANNOT_BE_CALCULATED)) {
                            sdScoreQue.setAnswer(sdScore);
                        } else {
                            sdScoreQue.setAnswer(null);
                        }
                        TextView textView = (TextView) sdScoreQue.getQuestionTypeView();
                        textView.setText(sdScoreForDisplay);
                    }
                }
            }
        }
    }

    public static void showAlertIfYesInAny(String[] split, QueFormBean queFormBean, Boolean isFirstCall) {
        if (Boolean.TRUE.equals(isFirstCall))
            return;

        if (split.length > 1) {
            for (int i = 1; i < split.length; i++) {
                if (SharedStructureData.relatedPropertyHashTable.containsKey(split[i])
                        && ("T".equalsIgnoreCase(SharedStructureData.relatedPropertyHashTable.get(split[i]))
                        || "1".equalsIgnoreCase(SharedStructureData.relatedPropertyHashTable.get(split[i])))) {
                    View.OnClickListener listener = v -> myAlertDialog.dismiss();
                    if (myAlertDialog == null || !myAlertDialog.isShowing()) {
                        myAlertDialog = new MyAlertDialog(SharedStructureData.context, false,
                                queFormBean.getDatamap(), listener, DynamicUtils.BUTTON_OK);
                        myAlertDialog.show();
                    }
                    return;
                }
            }
        }
    }

    public static void isCMAMFollowupsProblem(String[] split, QueFormBean queFormBean) {
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
        String complication = SharedStructureData.relatedPropertyHashTable.get(split[1]);
        String currentWeight = SharedStructureData.relatedPropertyHashTable.get(split[2]);
        Float lastWeight = SharedStructureData.currentRchMemberBean.getWeight();

        Gson gson = new Gson();
        MemberAdditionalInfoDataBean additionalInfo;

        if (!"NONE".equals(complication)) {
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
        } else if (SharedStructureData.currentRchMemberBean.getAdditionalInfo() != null) {
            additionalInfo = gson.fromJson(SharedStructureData.currentRchMemberBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
            if (additionalInfo.getWtGainStatus() != null && additionalInfo.getWtGainStatus().length() > 2) {
                String str = additionalInfo.getWtGainStatus().substring(additionalInfo.getWtGainStatus().length() - 1);
                if (lastWeight != null && currentWeight != null && ((lastWeight > Float.parseFloat(currentWeight) && str.equals("-")) ||
                        (lastWeight == Float.parseFloat(currentWeight) && str.equals("=")))) {
                    queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                    queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                }
            }
        }
    }

    public static void setChildGrowthChartData(String[] split, QueFormBean queFormBean) {
        // setChildGrowthChartData-memberId-currentWeight-serviceDate
        String next = queFormBean.getNext();
        if (next != null) {
            QueFormBean nextQuestion = SharedStructureData.mapIndexQuestion.get(Integer.parseInt(next));

            if (nextQuestion == null) {
                return;
            }

            LinearLayout linearLayout = (LinearLayout) nextQuestion.getQuestionTypeView();
            LineChartView lineChart = linearLayout.findViewById(R.id.lineChart);
            MemberBean child = null;

            String currentWeight = null;
            List<PointValue> yAxisValues = new ArrayList<>();
            Line line = new Line(yAxisValues).setColor(Color.BLACK);

            if (split.length > 1) {
                String childId = SharedStructureData.relatedPropertyHashTable.get(split[1]);
                if (childId != null) {
                    child = SharedStructureData.sewaFhsService.retrieveMemberBeanByActualId(Long.parseLong(childId));
                    Gson gson = new Gson();
                    MemberAdditionalInfoDataBean additionalInfo = null;
                    if (child.getAdditionalInfo() != null) {
                        additionalInfo = gson.fromJson(child.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
                    }

                    if (additionalInfo != null && additionalInfo.getWeightMap() != null && !additionalInfo.getWeightMap().isEmpty()) {
                        for (Map.Entry<Long, Float> entry : additionalInfo.getWeightMap().entrySet()) {
                            int[] ageYearMonthDayArray = UtilBean.calculateAgeYearMonthDayOnGivenDate(child.getDob().getTime(), entry.getKey());
                            yAxisValues.add(new PointValue(ageYearMonthDayArray[1], entry.getValue()));
                        }
                    }
                }
            }

            if (split.length > 2) {
                currentWeight = SharedStructureData.relatedPropertyHashTable.get(split[2]);
            }

            Date currentDate = new Date();
            if (split.length > 3) {
                String s = SharedStructureData.relatedPropertyHashTable.get(split[3]);
                if (s != null) {
                    currentDate = new Date(Long.parseLong(s));
                }
            }

            if (currentWeight != null && child != null) {
                int[] ageYearMonthDayArray = UtilBean.calculateAgeYearMonthDayOnGivenDate(child.getDob().getTime(), currentDate.getTime());
                yAxisValues.add(new PointValue(ageYearMonthDayArray[1], Float.parseFloat(currentWeight)));
            }

            LineChartData data = lineChart.getLineChartData();
            List<Line> lines = data.getLines();
            lines.set(0, line);
        }
    }

    public static void isFamilyHeadIdentified(QueFormBean queFormBean) {
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());

        String familyFound = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.FAMILY_FOUND);
        if (familyFound != null && familyFound.equals("1")) {
            if (SharedStructureData.loopBakCounter == 0) {
                queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
            } else {
                String isHead = SharedStructureData.relatedPropertyHashTable.get(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.FAMILY_HEAD_FLAG, queFormBean.getLoopCounter()));

                if (isHead != null && isHead.equals("1")) {
                    queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                    queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                } else {
                    boolean isHeadDeclared = false;
                    for (int i = 0; i < SharedStructureData.loopBakCounter + 1; i++) {
                        String headAnswer = SharedStructureData.relatedPropertyHashTable.get(
                                UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.FAMILY_HEAD_FLAG, i));
                        String statusAnswer = SharedStructureData.relatedPropertyHashTable.get(
                                UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.MEMBER_STATUS, i));

                        if ((headAnswer != null && statusAnswer != null && headAnswer.equals("1") && statusAnswer.equals("1"))
                                || (statusAnswer == null && headAnswer != null && headAnswer.equals("1"))) {
                            isHeadDeclared = true;
                            break;
                        }
                    }
                    if (!isHeadDeclared) {
                        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                        queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                    }
                }
            }
        }
    }

    public static void setDefaultGenderBasedOnRelationWithHOF(QueFormBean queFormBean) {
        String property = UtilBean.getRelatedPropertyNameWithLoopCounter(
                RelatedPropertyNameConstants.RELATION_WITH_HOF, queFormBean.getLoopCounter());
        property = SharedStructureData.relatedPropertyHashTable.get(property);
        if (property != null) {
            if (FhsConstants.MALE_RELATION.contains(property)) {
                SharedStructureData.relatedPropertyHashTable.put(UtilBean.
                                getRelatedPropertyNameWithLoopCounter(
                                        RelatedPropertyNameConstants.ANS_12,
                                        queFormBean.getLoopCounter()),
                        queFormBean.getOptions().get(0).getKey());
            } else if (FhsConstants.FEMALE_RELATION.contains(property)) {
                SharedStructureData.relatedPropertyHashTable.put(UtilBean.
                                getRelatedPropertyNameWithLoopCounter(
                                        RelatedPropertyNameConstants.ANS_12,
                                        queFormBean.getLoopCounter()),
                        queFormBean.getOptions().get(1).getKey());
            }
        }
    }

    public static void isUserOnline(QueFormBean queFormBean) {
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());

        if (SharedStructureData.sewaService.isOnline()) {
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
        }
    }

    public static void isMobileNumberVerfied(String[] split, QueFormBean queFormBean) {
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());

        if (split.length > 2) {
            try {
                int loopCounter = queFormBean.getLoopCounter();
                String memberId = split[1];
                String mobileNumber = split[2];
                if (loopCounter > 0) {
                    memberId += loopCounter;
                    mobileNumber += loopCounter;
                }
                memberId = SharedStructureData.relatedPropertyHashTable.get(memberId);
                mobileNumber = SharedStructureData.relatedPropertyHashTable.get(mobileNumber);
                if (mobileNumber != null) {
                    mobileNumber = mobileNumber.replace("F/", "");
                }
                if (memberId != null) {
                    MemberBean memberBean = SharedStructureData.sewaFhsService.retrieveMemberBeanByActualId(Long.parseLong(memberId));
                    if (memberBean != null && memberBean.getMobileNumber() != null
                            && memberBean.getMobileNumber().equals(mobileNumber)
                            && Boolean.TRUE.equals(memberBean.getMobileNumberVerified())) {
                        queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                        queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, null, e);
            }
        }
    }

    public static void isMobileNumberVerificationBlocked(QueFormBean queFormBean) {
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());

        if (Boolean.TRUE.equals(SharedStructureData.isMobileVerificationBlocked)) {
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
        }
    }

    public static void setOTPBasedVerificationComponent(QueFormBean queFormBean) {
        String next = queFormBean.getNext();
        if (next != null) {
            QueFormBean nextQuestion = SharedStructureData.mapIndexQuestion.get(DynamicUtils.getLoopId(Integer.parseInt(next), queFormBean.getLoopCounter()));

            if (nextQuestion == null) {
                return;
            }

            LinearLayout linearLayout = (LinearLayout) nextQuestion.getQuestionUIFrame();
            if (linearLayout != null) {
                TextView mobileText = linearLayout.findViewById(R.id.mobileText);

                String key = queFormBean.getRelatedpropertyname();
                if (queFormBean.getLoopCounter() > 0) {
                    key += queFormBean.getLoopCounter();
                }

                String mob = mobileText.getText().toString().trim();

                String mobileNumber = SharedStructureData.relatedPropertyHashTable.get(key);
                if (mobileNumber != null) {
                    mobileNumber = mobileNumber.replace("F/", "");

                    if (!mobileNumber.equalsIgnoreCase(mob)) {
                        if (mob.length() != 0) {
                            Button resetButton = linearLayout.findViewById(R.id.resetBtn);
                            resetButton.performClick();
                            resetButton.setPressed(true);
                            resetButton.invalidate();
                            resetButton.setPressed(false);
                            resetButton.invalidate();
                        }
                        mobileText.setText(mobileNumber);
                        linearLayout.requestLayout();
                        nextQuestion.setAnswer(null);
                    }
                }
            }
        }
    }

    public static void checkAnyMemberOtpVerificationDone(QueFormBean queFormBean) {
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());

        if (Boolean.TRUE.equals(SharedStructureData.isAnyMemberMobileVerificationDone)) {
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
        }
    }

    public static void calculateRUTFSachets(String[] split, QueFormBean queFormBean) {
        if (split.length >= 3) {
            String relatedProperty = queFormBean.getRelatedpropertyname();
            String strWeight = SharedStructureData.relatedPropertyHashTable.get(split[1]);
            String consumed = SharedStructureData.relatedPropertyHashTable.get(split[2]);
            String visit = SharedStructureData.relatedPropertyHashTable.get(split[3]);

            float weight = 0f;
            String sachets;
            if (strWeight != null && !strWeight.equals(GlobalTypes.NO_WEIGHT)) {
                weight = Float.parseFloat(strWeight);
            }
            if (consumed != null) {
                sachets = countSachets(weight, Integer.parseInt(consumed), visit);
            } else {
                sachets = countSachets(weight, 0, visit);
            }
            SharedStructureData.relatedPropertyHashTable.put(relatedProperty, sachets);
            QueFormBean sachetQue = SharedStructureData.mapIndexQuestion.get(Integer.valueOf(split[4]));
            if (sachetQue != null) {
                TextView textView = (TextView) sachetQue.getQuestionTypeView();
                if (textView != null) {
                    textView.setText(sachets);
                }
            }
        }
    }

    private static String countSachets(Float weight, int consumed, String visit) {

        MemberAdditionalInfoDataBean additionalInfo;
        Gson gson = new Gson();
        int lastGiven;
        int toGive = 0;
        int remaining = 0;

        if (SharedStructureData.currentRchMemberBean.getAdditionalInfo() != null) {
            additionalInfo = gson.fromJson(SharedStructureData.currentRchMemberBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
            if (additionalInfo.getGivenRUTF() != null && !visit.equals("1")) {
                lastGiven = additionalInfo.getGivenRUTF();
                remaining = lastGiven - consumed;
            }
        }
        if (weight >= 3.5 && weight <= 3.9) {
            toGive = 11 - remaining;
        } else if (weight >= 4 && weight <= 5.4) {
            toGive = 14 - remaining;
        } else if (weight >= 5.5 && weight <= 6.9) {
            toGive = 18 - remaining;
        } else if (weight >= 7 && weight <= 8.4) {
            toGive = 21 - remaining;
        } else if (weight >= 8.5 && weight <= 9.4) {
            toGive = 25 - remaining;
        } else if (weight >= 9.5 && weight <= 10.4) {
            toGive = 28 - remaining;
        } else if (weight >= 10.5 && weight <= 11.9) {
            toGive = 32 - remaining;
        } else if (weight >= 12) {
            toGive = 35 - remaining;
        }
        if (toGive < 0) {
            return "0";
        } else if (toGive != 0) {
            return Integer.toString(toGive);
        }

        return LabelConstants.NOT_AVAILABLE;
    }

    public static void setSchoolType(QueFormBean queFormBean) {
        String next = queFormBean.getNext();
        if (next != null) {
            QueFormBean nextQuestion = SharedStructureData.mapIndexQuestion.get(DynamicUtils.getLoopId(Integer.parseInt(next), queFormBean.getLoopCounter()));
            if (nextQuestion != null && nextQuestion.getQuestionUIFrame() != null) {
                LinearLayout linearLayout = (LinearLayout) nextQuestion.getQuestionUIFrame();
                View view = linearLayout.findViewById(IdConstants.SCHOOL_COMPONENT_RESET_BUTTON_ID);
                if (view != null) {
                    view.performClick();
                }
            }
        }
    }

    public static void checkContainsAnyLoop(String[] split, QueFormBean queFormBean) {
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());

        if (split.length > 2) {
            String relatedProperty;

            if (split[2].trim().isEmpty()) {
                return;
            }

            for (int counter = 0; counter <= SharedStructureData.loopBakCounter; counter++) {
                relatedProperty = SharedStructureData.relatedPropertyHashTable.get(UtilBean.getRelatedPropertyNameWithLoopCounter(split[2], counter));

                if (relatedProperty != null && relatedProperty.equals(split[1])) {
                    queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                    queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                    return;
                }
            }
        }
    }

    public static void isAnyVaccineGiven(QueFormBean queFormBean) {
        queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
        if (SharedStructureData.vaccinations != null) {
            for (MyVaccinationStatus vaccinationStatus : SharedStructureData.vaccinations.values()) {
                if (vaccinationStatus.getIsTaken().getValue() == 'T') {
                    queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
                    queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
                    return;
                }
            }
        }
    }

    public static void memberMedicineCountCheck(QueFormBean queFormBean) {
        if (SharedStructureData.totalMemberMedicineCount == -1 || SharedStructureData.totalMemberMedicineCount == queFormBean.getLoopCounter() + 1) {
            queFormBean.setNext(queFormBean.getOptions().get(1).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(1).getKey());
        } else {
            queFormBean.setNext(queFormBean.getOptions().get(0).getNext());
            queFormBean.setAnswer(queFormBean.getOptions().get(0).getKey());
        }
    }

    public static void identifyHighRiskChardhamTourist(String[] split) {
        int oxygenQueId = Integer.parseInt(split[1]);
        int bloodPressureQueId = Integer.parseInt(split[2]);
        int bloodSugarQueId = Integer.parseInt(split[3]);
        int temperatureQueId = Integer.parseInt(split[4]);

        QueFormBean oxygenQue = SharedStructureData.mapIndexQuestion.get(oxygenQueId);
        QueFormBean bloodPressureQue = SharedStructureData.mapIndexQuestion.get(bloodPressureQueId);
        QueFormBean bloodSugarQue = SharedStructureData.mapIndexQuestion.get(bloodSugarQueId);
        QueFormBean temperatureQue = SharedStructureData.mapIndexQuestion.get(temperatureQueId);

        Map<String, Object> mapOfAnswers = new HashMap<>();
        if (oxygenQue != null) {
            mapOfAnswers.put("oxygenAnswer", oxygenQue.getAnswer());
        }
        if (bloodPressureQue != null) {
            mapOfAnswers.put("bloodPressureAnswer", bloodPressureQue.getAnswer());
        }
        if (bloodSugarQue != null) {
            mapOfAnswers.put("bloodSugarAnswer", bloodSugarQue.getAnswer());
        }
        if (temperatureQue != null) {
            mapOfAnswers.put("temperatureAnswer", temperatureQue.getAnswer());
        }

        String identifyHighRiskForRchAnc = SharedStructureData.rchHighRiskService.identifyHighRiskForChardhamTourist(mapOfAnswers);

        String mrpFlow = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.MRP_TREATMENT_FLOW);
        if (mrpFlow == null || mrpFlow.equalsIgnoreCase("0")) {
            switch (identifyHighRiskForRchAnc) {
                case LabelConstants.SCREENING_STATUS_GREEN:
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.SCREENING_STATUS, LabelConstants.SCREENING_STATUS_GREEN);
                    break;
                case LabelConstants.SCREENING_STATUS_YELLOW:
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.SCREENING_STATUS, LabelConstants.SCREENING_STATUS_YELLOW);
                    break;
                case LabelConstants.SCREENING_STATUS_RED:
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.SCREENING_STATUS, LabelConstants.SCREENING_STATUS_RED);
                    break;
                default:
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.SCREENING_STATUS, LabelConstants.CH_MEMBER_REGISTERED);
            }
        } else {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.SCREENING_STATUS, LabelConstants.SCREENING_STATUS_TREATMENT);
        }
    }
}
