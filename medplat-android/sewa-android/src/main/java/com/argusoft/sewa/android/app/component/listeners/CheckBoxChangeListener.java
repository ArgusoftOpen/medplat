package com.argusoft.sewa.android.app.component.listeners;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.argusoft.sewa.android.app.constants.FormulaConstants;
import com.argusoft.sewa.android.app.databean.FormulaTagBean;
import com.argusoft.sewa.android.app.databean.OptionDataBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author alpeshkyada
 */
public class CheckBoxChangeListener implements CompoundButton.OnCheckedChangeListener {

    private final QueFormBean queFormBean;
    private List<OptionDataBean> optionBeans;
    private final boolean isMulti;
    private List<String> answer;
    private List<String> answerOptionsValue;
    private boolean noneCheck;
    private String noneKey;
    private int noneIndex;
    private List<String> uncheckedOption;
    //added in phase 3 
    private boolean isMorbidityQuestion;
    private String[] morbidityOptions;
    private LinearLayout layout;

    public CheckBoxChangeListener(QueFormBean queFormBean, boolean isMulti, boolean isChecked) {
        this.queFormBean = queFormBean;
        this.isMulti = isMulti;
        if (queFormBean.getSubform() != null && queFormBean.getSubform().toLowerCase().contains(GlobalTypes.DATA_MAP_MORBIDITY_CONDITION.toLowerCase())) {
            isMorbidityQuestion = true;
            String[] split = UtilBean.split(queFormBean.getSubform().trim(), GlobalTypes.COMMA);
            if (isMulti) {
                for (String option : split) {
                    if (option.toLowerCase().contains(GlobalTypes.DATA_MAP_MORBIDITY_CONDITION.toLowerCase())) {
                        morbidityOptions = UtilBean.split(option, GlobalTypes.DATE_STRING_SEPARATOR);
                        break;
                    }
                }
                answerOptionsValue = new ArrayList<>();
            }
        }
        this.optionBeans = UtilBean.getOptionsOrDataMap(queFormBean, false);
        if (optionBeans.size() == 1 && optionBeans.get(0).getKey() != null && optionBeans.get(0).getKey().equalsIgnoreCase(GlobalTypes.NO_OPTION_AVAILABLE)) {
            List<String> defaultValues;
            if (queFormBean.getRelatedpropertyname() != null && queFormBean.getRelatedpropertyname().trim().length() > 0) {
                String propertyName = queFormBean.getRelatedpropertyname().trim();
                if (queFormBean.getLoopCounter() > 0 && !queFormBean.isIgnoreLoop()) {
                    propertyName += queFormBean.getLoopCounter();
                }
                String defaultValue = SharedStructureData.relatedPropertyHashTable.get(propertyName);
                if (defaultValue != null) {
                    defaultValues = UtilBean.getListFromStringBySeparator(defaultValue, GlobalTypes.COMMA);
                    if (defaultValues != null) {
                        optionBeans.clear();
                        for (String ss : defaultValues) {
                            OptionDataBean option = new OptionDataBean();
                            option.setKey(ss);
                            option.setValue(ss);
                            optionBeans.add(option);
                        }
                    }
                }
            }
        }
        if (isMulti) {
            answer = new ArrayList<>();
            List<FormulaTagBean> formulas = queFormBean.getFormulas();
            if (formulas != null && !formulas.isEmpty()) {
                FormulaTagBean formula = formulas.get(0);
                String myFormula = formula.getFormulavalue();
                if (myFormula != null && myFormula.length() > 0 && myFormula.toLowerCase().contains(FormulaConstants.MULTI_OPTION_UNCHECK.toLowerCase())) {
                    String[] split = UtilBean.split(formula.getFormulavalue().toLowerCase(), FormulaConstants.MULTI_OPTION_UNCHECK.toLowerCase() + "-");
                    if (split.length > 1) {
                        uncheckedOption = Arrays.asList(UtilBean.split(split[split.length - 1].toLowerCase(), GlobalTypes.KEY_VALUE_SEPARATOR));
                    }
                }
            }
        } else {
            this.singleCheckChange(isChecked, null);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton cb, boolean bln) {
        if (isMulti) {
            int optionIndex = cb.getId();
            if (optionBeans != null && optionBeans.size() > optionIndex) {
                OptionDataBean option = optionBeans.get(optionIndex);

                if (queFormBean.getAnswer() != null) {
                    List<String> strings = Arrays.asList(queFormBean.getAnswer().toString().split(","));
                    answer = new ArrayList<>(strings);
                }

                if (bln) {  // if Checked 
                    if (answer != null && !answer.contains(option.getKey())) {
                        if (uncheckedOption != null && uncheckedOption.contains(option.getValue().trim().toLowerCase())) {
                            noneCheck = true;
                            noneKey = option.getKey();
                            noneIndex = optionIndex;
                            answer.clear();
                            if (layout == null) {
                                layout = (LinearLayout) queFormBean.getQuestionTypeView();
                            }
                            for (int i = 0; i < optionBeans.size(); i++) {
                                if (i != optionIndex) {
                                    CheckBox checkBox = layout.findViewById(i);
                                    checkBox.setChecked(false);
                                    checkBox.setTextColor(SewaUtil.COLOR_MORBIDITY_NORMAL);
                                }
                            }
                            answer.add(option.getKey());
                            if (isMorbidityQuestion) {
                                // here we have remove the morbidity
                                answerOptionsValue.clear();
                                SharedStructureData.removeItemFromLICList(queFormBean.getQuestion(), queFormBean.getLoopCounter() + "");
                            }
                        } else {
                            if (noneCheck) {
                                answer.remove(noneKey);
                                noneCheck = false;
                                LinearLayout layout = (LinearLayout) queFormBean.getQuestionTypeView();
                                CheckBox checkBox = layout.findViewById(noneIndex);
                                checkBox.setChecked(false);
                            }
                            answer.add(option.getKey());
                            if (isMorbidityQuestion) {
                                if (isMorbidity(option.getKey())) {
                                    answerOptionsValue.add(option.getValue());
                                    cb.setTextColor(SewaUtil.COLOR_MORBIDITY_GREEN);
                                } else {
                                    answerOptionsValue.remove(option.getValue());
                                    cb.setTextColor(SewaUtil.COLOR_MORBIDITY_NORMAL);
                                }
                            }
                        }
                    }
                } else {
                    answer.remove(option.getKey());
                    if (isMorbidityQuestion) {
                        answerOptionsValue.remove(option.getValue());
                        cb.setTextColor(SewaUtil.COLOR_MORBIDITY_NORMAL);
                    }
                }
                queFormBean.setAnswer(UtilBean.stringListJoin(answer, GlobalTypes.COMMA));
                if (isMorbidityQuestion) {
                    if (answerOptionsValue != null && !answerOptionsValue.isEmpty()) {
                        SharedStructureData.addItemInLICList(queFormBean.getQuestion(),
                                UtilBean.stringListJoin(answerOptionsValue, GlobalTypes.COMMA, true),
                                queFormBean.getLoopCounter() + "");
                    } else {
                        SharedStructureData.removeItemFromLICList(queFormBean.getQuestion(), queFormBean.getLoopCounter() + "");
                    }
                }
            }  //this is for dataMap multi value

        } else {
            singleCheckChange(bln, cb);
        }
    }

    private void singleCheckChange(boolean isChecked, CompoundButton cb) {
        if (optionBeans != null && optionBeans.size() == 2) {
            OptionDataBean option;
            if (isChecked) {
                option = optionBeans.get(0);
            } else {
                option = optionBeans.get(1);
            }
            if (option.getNext() != null) {
                queFormBean.setNext(option.getNext().trim());
            }
            queFormBean.setAnswer(option.getKey());
            if (isMorbidityQuestion) {
                String result = DynamicUtils.checkForMorbidity(option.getValue(), option.getKey(), queFormBean.getSubform(), queFormBean.getLoopCounter() + "", null, null);
                if (cb != null) {
                    cb.setTextColor(SewaUtil.getColorForMorbiditySims(result));
                }
            }
        } else {
            if (isChecked) {
                queFormBean.setAnswer("T");
            } else {
                queFormBean.setAnswer("F");
            }
        }
    }

    private boolean isMorbidity(String key) {
        if (morbidityOptions != null && morbidityOptions.length > 0) {
            for (String string : morbidityOptions) {
                if (string.contains(key)) {
                    String[] option = UtilBean.split(string, GlobalTypes.KEY_VALUE_SEPARATOR);
                    if (option.length == 3) {
                        return option[1].equalsIgnoreCase(SewaUtil.COLOR_MORBIDITY_GREEN_CODE);
                    }
                    break;
                }
            }
        }
        return false;
    }

    public void changeOptions(List<OptionDataBean> options) {
        this.optionBeans = options;
    }

    public void setLayout(LinearLayout linearLayout) {
        layout = linearLayout;
    }
}
