package com.argusoft.sewa.android.app.component.listeners;

import android.app.Activity;
import android.content.Context;
import com.argusoft.sewa.android.app.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.morbidities.constants.MorbiditiesConstant;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;

/**
 * @author alpeshkyada
 */
public class BloodPresserChangedListener implements View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener {

    private static final int SYS_MIN = 60;
    private static final int SYS_MAX = 260;
    private static final int DIA_MIN = 40;
    private static final int DIA_MAX = 160;
    private static final String VALID_MSG_SYS = "Systolic BP should be between " + SYS_MIN + " and " + SYS_MAX;
    private static final String VALID_MSG_DIA = "Diastolic BP should be between " + DIA_MIN + " and " + DIA_MAX;

    private final QueFormBean queFormBean;
    private final Context context;
    private final int[] bloodPresser;
    private final String mandatoryMsg;
    private View inputLayout;
    private boolean isChecked;
    private EditText sysEditText;
    private EditText diaEditText;
    private boolean isMorbidityQuestion;

    public BloodPresserChangedListener(Context context, QueFormBean queFormBean) {
        this.context = context;
        this.queFormBean = queFormBean;
        mandatoryMsg = queFormBean.getMandatorymessage();
        // get input layout
        if (queFormBean.getQuestionTypeView() != null) {
            inputLayout = ((View) queFormBean.getQuestionTypeView()).findViewById(IdConstants.BLOOD_PRESSURE_BOX_INPUT_LAYOUT_ID);

        }
        bloodPresser = new int[2];
        bloodPresser[0] = -1;
        bloodPresser[1] = -1;
        isChecked = false;
        if (queFormBean.getSubform() != null && queFormBean.getSubform().toLowerCase().contains(GlobalTypes.DATA_MAP_MORBIDITY_CONDITION.toLowerCase())) {
            isMorbidityQuestion = true;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton cb, boolean bln) {
        if (inputLayout == null) {
            inputLayout = ((View) queFormBean.getQuestionTypeView()).findViewById(IdConstants.BLOOD_PRESSURE_BOX_INPUT_LAYOUT_ID);
        }
        isChecked = bln;
        if (isChecked) {
            if (inputLayout != null) {
                inputLayout.setVisibility(View.GONE);
            }
            reset();
        } else {
            if (inputLayout != null) {
                inputLayout.setVisibility(View.VISIBLE);
            }
            setAnswer();
        }
    }

    @Override
    public void onFocusChange(View view, boolean bln) {
        if (view.getId() == IdConstants.BLOOD_PRESSURE_BOX_SYSTOLIC_ID) {
            sysEditText = (EditText) view;
            if (!bln) {
                try {
                    bloodPresser[0] = Integer.parseInt(sysEditText.getText().toString());
                    if (isNotValidSys()) {
                        generateToast(VALID_MSG_SYS);
                    } else if (isMorbidityQuestion) {
                        String result = DynamicUtils.checkForMorbidity(
                                MorbiditiesConstant.MorbiditySymptoms.SYSTOLIC_BP, "" + bloodPresser[0], queFormBean.getSubform(),
                                queFormBean.getLoopCounter() + "", "sys", null);
                        sysEditText.setTextColor(SewaUtil.getColorForMorbiditySims(result));
                    }
                } catch (Exception e) {
                    bloodPresser[0] = -1;
                } finally {
                    setAnswer();
                }
            }
        } else if (view.getId() == IdConstants.BLOOD_PRESSURE_BOX_DIASTOLIC_ID) {
            diaEditText = (EditText) view;
            if (!bln) {
                try {
                    bloodPresser[1] = Integer.parseInt(diaEditText.getText().toString());
                    if (isNotValidDia()) {
                        generateToast(VALID_MSG_DIA);
                    } else if (isMorbidityQuestion) {
                        String result = DynamicUtils.checkForMorbidity(MorbiditiesConstant.MorbiditySymptoms.DIASTOLIC_BP, "" + bloodPresser[1], queFormBean.getSubform(), queFormBean.getLoopCounter() + "", "dia", null);
                        diaEditText.setTextColor(SewaUtil.getColorForMorbiditySims(result));
                    }
                } catch (Exception e) {
                    bloodPresser[1] = -1;
                } finally {
                    setAnswer();
                }
            }
        }
    }

    private boolean isValid() {
        if (bloodPresser[0] != -1 && bloodPresser[1] != -1) {
            if (isNotValidSys()) {
                queFormBean.setMandatorymessage(VALID_MSG_SYS);
                return false;
            } else if (isNotValidDia()) {
                queFormBean.setMandatorymessage(VALID_MSG_DIA);
                return false;
            } else {
                queFormBean.setMandatorymessage(mandatoryMsg);
                return true;
            }
        } else {
            queFormBean.setMandatorymessage(mandatoryMsg);
            return false;
        }

    }

    private void reset() {
        if (sysEditText != null) {
            sysEditText.setText("");
            if (isMorbidityQuestion) {
                sysEditText.setTextColor(SewaUtil.COLOR_MORBIDITY_NORMAL);
                SharedStructureData.removeItemFromLICList(MorbiditiesConstant.MorbiditySymptoms.SYSTOLIC_BP, queFormBean.getLoopCounter() + "");
            }
        }
        if (diaEditText != null) {
            diaEditText.setText("");
            if (isMorbidityQuestion) {
                diaEditText.setTextColor(SewaUtil.COLOR_MORBIDITY_NORMAL);
                SharedStructureData.removeItemFromLICList(MorbiditiesConstant.MorbiditySymptoms.DIASTOLIC_BP, queFormBean.getLoopCounter() + "");
            }
        }
        bloodPresser[0] = -1;
        bloodPresser[1] = -1;
        setAnswer();
    }

    private void setAnswer() {
        if (!isChecked) {
            if (isValid()) {
                queFormBean.setAnswer(GlobalTypes.FALSE + GlobalTypes.KEY_VALUE_SEPARATOR + bloodPresser[0] + GlobalTypes.KEY_VALUE_SEPARATOR + bloodPresser[1]);
            } else {
                queFormBean.setAnswer(null);
            }
        } else {
            queFormBean.setAnswer(GlobalTypes.TRUE);
        }
        Log.i(getClass().getSimpleName(), queFormBean.getQuestion() + " = " + queFormBean.getAnswer());
    }

    private void generateToast(String validationMessage) {
        ((Activity) context).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        SewaUtil.generateToast(context, validationMessage);
    }

    private boolean isNotValidSys() {
        return bloodPresser[0] < SYS_MIN || bloodPresser[0] > SYS_MAX;
    }

    private boolean isNotValidDia() {
        return bloodPresser[1] < DIA_MIN || bloodPresser[1] > DIA_MAX;
    }
}
