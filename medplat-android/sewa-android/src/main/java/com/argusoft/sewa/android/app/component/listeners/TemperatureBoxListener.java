/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.component.listeners;

import com.argusoft.sewa.android.app.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.morbidities.constants.MorbiditiesConstant;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;

/**
 * @author alpeshkyada
 */
public class TemperatureBoxListener implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    private final QueFormBean queFormBean;
    private boolean isChecked;
    private String degree;
    private String floating;
    private View inputLayout;
    private View nextView;
    private Spinner comboDegree;
    private Spinner comboFloating;
    //added in phase 3 
    private boolean isMorbidityQuestion;

    public TemperatureBoxListener(QueFormBean queFormBean) {
        this.queFormBean = queFormBean;
        degree = floating = "";
        isChecked = false;
        if (queFormBean.getSubform() != null && queFormBean.getSubform().toLowerCase().contains(GlobalTypes.DATA_MAP_MORBIDITY_CONDITION.toLowerCase())) {
            isMorbidityQuestion = true;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton cb, boolean bln) {
        isChecked = bln;
        if (inputLayout == null) {
            inputLayout = ((View) queFormBean.getQuestionTypeView()).findViewById(IdConstants.TEMPERATURE_BOX_INPUT_LAYOUT_ID);
        }
        if (isChecked) {
            inputLayout.setVisibility(View.GONE);
            resetComponent();
        } else {
            inputLayout.setVisibility(View.VISIBLE);
        }
        setAnswer();
    }

    @Override
    public void onItemSelected(AdapterView<?> av, View view, int i, long l) {
        int id = av.getId();
        if (id == IdConstants.TEMPERATURE_BOX_SPINNER_DEGREE_ID) {
            try {
                comboDegree = (Spinner) av;
                degree = comboDegree.getSelectedItem().toString();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), null, e);
                degree = av.getSelectedItem().toString();
            }
        } else if (id == IdConstants.TEMPERATURE_BOX_SPINNER_FLOATING_ID) {
            try {
                comboFloating = (Spinner) av;
                floating = comboFloating.getSelectedItem().toString();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), null, e);
                floating = av.getSelectedItem().toString();
            }
        }
        setAnswer();
    }

    @Override
    public void onNothingSelected(AdapterView<?> av) {
        // Do nothing
    }

    private void setAnswer() {
        String answer;
        if (isChecked) {
            answer = GlobalTypes.TRUE;
        } else {
            if (!degree.equalsIgnoreCase(GlobalTypes.SELECT) && !floating.equalsIgnoreCase(GlobalTypes.SELECT)) {
                answer = GlobalTypes.FALSE + GlobalTypes.KEY_VALUE_SEPARATOR + degree + GlobalTypes.KEY_VALUE_SEPARATOR + floating;
            } else {
                answer = "";
            }
        }
        queFormBean.setAnswer(answer);
        Log.i(getClass().getSimpleName(), queFormBean.getQuestion() + "=" + queFormBean.getAnswer());

        // morbidity section
        if (isMorbidityQuestion) {
            String ans = null;
            if (!degree.equalsIgnoreCase(GlobalTypes.SELECT) && !floating.equalsIgnoreCase(GlobalTypes.SELECT)) {
                ans = degree + "." + floating;
            }
            String result = DynamicUtils.checkForMorbidity(MorbiditiesConstant.MorbiditySymptoms.AXILLARY_TEMPERATURE, ans, queFormBean.getSubform(), queFormBean.getLoopCounter() + "", null, null);
            int colorForMorbiditySims = SewaUtil.getColorForMorbiditySims(result);
            try {
                if (comboDegree != null) {
                    ((TextView) comboDegree.getSelectedView()).setTextColor(colorForMorbiditySims);
                }
                if (comboFloating != null) {
                    ((TextView) comboFloating.getSelectedView()).setTextColor(colorForMorbiditySims);
                }
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), null, e);
            }
        }

        // show current page question if any hide
        QueFormBean setNextVisibleQuestion = DynamicUtils.setNextVisibleQuestionInSamePage(queFormBean);
        if (setNextVisibleQuestion != null) {
            Log.i(getClass().getSimpleName(), "Next Question is :" + setNextVisibleQuestion.getId());

            if (nextView != null) {
                nextView.setVisibility(View.GONE);
            }
            nextView = (View) setNextVisibleQuestion.getQuestionUIFrame();
            nextView.setVisibility(View.VISIBLE);
        } else {

            if (nextView != null) {
                nextView.setVisibility(View.GONE);
            }
        }
    }

    private void resetComponent() {
        if (comboDegree != null) {
            try {
                comboDegree.setSelection(0);
                degree = GlobalTypes.SELECT;
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), null, e);
                degree = GlobalTypes.SELECT;
            }
        }
        if (comboFloating != null) {
            try {
                comboFloating.setSelection(0);
                floating = GlobalTypes.SELECT;
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), null, e);
                floating = GlobalTypes.SELECT;
            }
        }
    }
}
