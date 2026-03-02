/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.component.listeners;

import com.argusoft.sewa.android.app.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.argusoft.sewa.android.app.constants.FormulaConstants;
import com.argusoft.sewa.android.app.databean.OptionDataBean;
import com.argusoft.sewa.android.app.datastructure.PageFormBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;

import java.util.List;

/**
 * @author alpeshkyada
 */
public class RadioButtonCheckedListener implements RadioGroup.OnCheckedChangeListener {

    private final QueFormBean queFormBean;
    private final List<OptionDataBean> options;
    private View nextView;
    private View hiddenView;
    //added in phase 3 
    private boolean isMorbiQuestion;

    public RadioButtonCheckedListener(QueFormBean queFormBean, List<OptionDataBean> options) {
        this.queFormBean = queFormBean;
        this.options = options;
        if (queFormBean.getSubform() != null && queFormBean.getSubform().toLowerCase().contains(GlobalTypes.DATA_MAP_MORBIDITY_CONDITION.toLowerCase())) {
            isMorbiQuestion = true;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radiogroup, int position) {
        RadioButton radioButton = radiogroup.findViewById(position);

        if (options != null && !options.isEmpty()) {
            OptionDataBean selectOption = options.get(position);
            if (selectOption.getNext() != null && selectOption.getNext().trim().length() > 0 && !selectOption.getNext().trim().equalsIgnoreCase("null")) {
                queFormBean.setNext(selectOption.getNext());
            } else if (queFormBean.getId() == GlobalTypes.SUBMIT_QUESTION_ID || queFormBean.getId() == GlobalTypes.SUBMIT_QUESTION_ID_FOR_FP) {
                queFormBean.setNext("0");
            }
            queFormBean.setAnswer(selectOption.getKey());
            // in page there is any hidden question than show
            QueFormBean setNextVisibleQuestion = DynamicUtils.setNextVisibleQuestionInSamePage(queFormBean);
            if (hiddenView != null) {
                hiddenView.setVisibility(View.GONE);
            }
            if (nextView != null) {
                nextView.setVisibility(View.GONE);
            }
            if (setNextVisibleQuestion != null) {
                PageFormBean pageFromNext = DynamicUtils.getPageFromNext(DynamicUtils.getLoopId(queFormBean.getId(), setNextVisibleQuestion.getLoopCounter()));
                if (pageFromNext != null) {
                    LinearLayout pageLayout = pageFromNext.getPageLayout(true, 0);
                    if (pageLayout != null) {
                        hiddenView = pageLayout.findViewById(DynamicUtils.HIDDEN_LAYOUT_ID);
                    }
                    if (hiddenView != null) {
                        Log.i(getClass().getSimpleName(), "Hidden View found ......");
                        hiddenView.setVisibility(View.VISIBLE);
                    } else {
                        Log.i(getClass().getSimpleName(), "Hidden View Not found ......");
                    }
                }
                nextView = (View) setNextVisibleQuestion.getQuestionUIFrame();
                if (nextView != null) {
                    nextView.setVisibility(View.VISIBLE);
                }
                QueFormBean temp = setNextVisibleQuestion;
                // direct chaining will visible 
                do {
                    temp = DynamicUtils.setNextVisibleQuestionInSamePage(temp);
                    if (temp != null) {
                        View tempView = (View) temp.getQuestionUIFrame();
                        if (tempView != null) {
                            tempView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        break;
                    }
                } while (true);
            }
            // if morbidity than 
            if (isMorbiQuestion) {
                for (int counter = 0; counter < radiogroup.getChildCount(); counter++) {
                    RadioButton rButton = radiogroup.findViewById(counter);
                    if (rButton != null) {
                        rButton.setTextColor(SewaUtil.COLOR_MORBIDITY_NORMAL);
                    }
                }
                String result;
                if (selectOption.getKey().equalsIgnoreCase("T") || selectOption.getKey().equalsIgnoreCase("F")) {
                    result = DynamicUtils.checkForMorbidity(
                            queFormBean.getQuestion(), selectOption.getKey(), queFormBean.getSubform(),
                            queFormBean.getLoopCounter() + "", null, null);
                } else {
                    result = DynamicUtils.checkForMorbidity(
                            queFormBean.getQuestion(), selectOption.getKey(), queFormBean.getSubform(),
                            queFormBean.getLoopCounter() + "", null, selectOption.getValue());
                }
                if (radioButton != null && result != null && result.equalsIgnoreCase(SewaUtil.COLOR_MORBIDITY_GREEN_CODE)) {
                    radioButton.setTextColor(SewaUtil.COLOR_MORBIDITY_GREEN);
                }
            }
        } else {
            queFormBean.setAnswer(radioButton.getText().toString());
        }

        if (queFormBean.getRelatedpropertyname() != null && queFormBean.getFormulas() != null && !queFormBean.getFormulas().isEmpty()
                && queFormBean.getFormulas().toString().toLowerCase().contains(FormulaConstants.FORMULA_SET_PROPERTY.toLowerCase())) {
            if (queFormBean.getAnswer() != null) {
                SharedStructureData.relatedPropertyHashTable.put(UtilBean.getRelatedPropertyNameWithLoopCounter(
                        queFormBean.getRelatedpropertyname(), queFormBean.getLoopCounter()), queFormBean.getAnswer().toString());
            } else {
                SharedStructureData.relatedPropertyHashTable.remove(UtilBean.getRelatedPropertyNameWithLoopCounter(
                        queFormBean.getRelatedpropertyname(), queFormBean.getLoopCounter()));
            }
        }
    }
}
