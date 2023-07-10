package com.argusoft.sewa.android.app.component.listeners;

import com.argusoft.sewa.android.app.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
public class ComboChangeListener implements AdapterView.OnItemSelectedListener {

    private final QueFormBean queFormBean;
    private List<OptionDataBean> options;
    private View hiddenView;
    private View nextView;
    private boolean isMorbidityQuestion;
    private String[] morbidityOptions;
    private boolean isCombo = false;

    public ComboChangeListener(QueFormBean queFormBean) {
        this.queFormBean = queFormBean;
        if (queFormBean.getSubform() != null && queFormBean.getSubform().toLowerCase().contains(GlobalTypes.DATA_MAP_MORBIDITY_CONDITION.toLowerCase())) {
            isMorbidityQuestion = true;
            String subForm = queFormBean.getSubform();
            String[] split = UtilBean.split(subForm, GlobalTypes.COMMA);
            for (String string : split) {
                if (string.toLowerCase().contains(GlobalTypes.DATA_MAP_MORBIDITY_CONDITION.toLowerCase())) {
                    morbidityOptions = UtilBean.split(string, GlobalTypes.DATE_STRING_SEPARATOR);
                }
            }
        }
        String typeOfQue = queFormBean.getType();

        if (typeOfQue != null && typeOfQue.equalsIgnoreCase(GlobalTypes.COMBO_BOX_DYNAMIC_SELECT)) {
            isCombo = true;
        }
        options = UtilBean.getOptionsOrDataMap(queFormBean, isCombo);
    }

    @Override
    public void onItemSelected(AdapterView<?> av, View view, int position, long l) {
        options = UtilBean.getOptionsOrDataMap(queFormBean, isCombo);
        if (!options.isEmpty()) {
            OptionDataBean selectOption = options.get(position);
            if (!selectOption.getKey().trim().equalsIgnoreCase("-1")) {
                if (selectOption.getNext() != null && selectOption.getNext().trim().length() > 0) {
                    queFormBean.setNext(selectOption.getNext());
                }
                queFormBean.setAnswer(selectOption.getKey());
                // on page hide question will show if any
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
                // check if morbidity have
                if (isMorbidityQuestion && morbidityOptions != null && morbidityOptions.length > 1) {
                    for (int counter = 1; counter < morbidityOptions.length; counter++) {
                        if (morbidityOptions[counter].toLowerCase().contains(selectOption.getKey().toLowerCase() + GlobalTypes.KEY_VALUE_SEPARATOR)) {
                            String[] optionsValue = UtilBean.split(morbidityOptions[counter], GlobalTypes.KEY_VALUE_SEPARATOR);
                            if (optionsValue.length > 1 && optionsValue[1].equalsIgnoreCase(SewaUtil.COLOR_MORBIDITY_GREEN_CODE)) {
                                SharedStructureData.addItemInLICList(queFormBean.getQuestion(), selectOption.getValue(), queFormBean.getLoopCounter() + "");
                                try {
                                    ((TextView) view).setTextColor(SewaUtil.getColorForMorbiditySims(optionsValue[1]));
                                } catch (Exception e) {
                                    Log.e(getClass().getSimpleName(), null, e);
                                }
                            } else {
                                SharedStructureData.removeItemFromLICList(queFormBean.getQuestion(), queFormBean.getLoopCounter() + "");
                            }
                            break;
                        }
                    }
                }
            } else {
                queFormBean.setAnswer(null);
                if (isMorbidityQuestion) {
                    SharedStructureData.removeItemFromLICList(queFormBean.getQuestion(), queFormBean.getLoopCounter() + "");
                    try {
                        ((TextView) view).setTextColor(SewaUtil.COLOR_MORBIDITY_NORMAL);
                    } catch (Exception e) {
                        Log.e(getClass().getSimpleName(), null, e);
                    }
                }
            }
        } else {
            queFormBean.setAnswer(av.getSelectedItem());
        }
        if (queFormBean.getFormulas() != null &&
                !queFormBean.getFormulas().isEmpty() &&
                queFormBean.getFormulas().toString().toLowerCase().contains(FormulaConstants.FORMULA_SET_PROPERTY.toLowerCase())) {
            if (queFormBean.getAnswer() != null) {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(queFormBean.getRelatedpropertyname(), queFormBean.getLoopCounter()),
                        queFormBean.getAnswer().toString());
            } else {
                SharedStructureData.relatedPropertyHashTable.remove(queFormBean.getRelatedpropertyname());
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> av) {
        //No need to do anything
    }
}
