package com.argusoft.sewa.android.app.component.listeners;


import com.argusoft.sewa.android.app.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * @author alpeshkyada
 */
public class CheckBoxWithTextBoxListener implements CompoundButton.OnCheckedChangeListener, View.OnFocusChangeListener {

    private final QueFormBean queFormBean;
    private String answer;
    private boolean isChecked;
    private LinearLayout inputLayout;
    private TextInputLayout inputTextView;
    private TextInputEditText inputTextEditText;
    private boolean isMorbidityQuestion;

    public CheckBoxWithTextBoxListener(QueFormBean queFormBean) {
        this.queFormBean = queFormBean;
        answer = (String) queFormBean.getAnswer();
        isChecked = false;
        if (queFormBean.getSubform() != null && queFormBean.getSubform().toLowerCase().contains(GlobalTypes.DATA_MAP_MORBIDITY_CONDITION.toLowerCase())) {
            isMorbidityQuestion = true;
        }
        if (queFormBean.getQuestionTypeView() != null) {
            inputLayout = ((LinearLayout) queFormBean.getQuestionTypeView()).findViewById(IdConstants.CHECKBOX_WITH_TEXT_BOX_INPUT_LAYOUT_ID);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton cb, boolean bln) {
        if (inputLayout == null) {
            inputLayout = ((LinearLayout) queFormBean.getQuestionTypeView()).findViewById(IdConstants.CHECKBOX_WITH_TEXT_BOX_INPUT_LAYOUT_ID);
            inputTextView = (TextInputLayout) inputLayout.getChildAt(0);
            inputTextEditText = (TextInputEditText) inputTextView.getEditText();
        }
        isChecked = bln;
        if (isChecked) {
            reset();
            inputLayout.setVisibility(View.GONE);
        } else {
            inputLayout.setVisibility(View.VISIBLE);
        }
        setAnswer();
    }

    @Override
    public void onFocusChange(View view, boolean bln) {
        inputTextEditText = (TextInputEditText) view;
        if (!bln && inputTextEditText.getText() != null) {
            answer = inputTextEditText.getText().toString();
        }
        setAnswer();
    }

    private void setAnswer() {
        String ans;
        if (isChecked) {
            ans = GlobalTypes.TRUE;
        } else {

            if (inputTextEditText != null && inputTextEditText.getText() != null) {
                answer = inputTextEditText.getText().toString();
            }
            if (answer != null && answer.trim().length() > 0 && !answer.trim().equalsIgnoreCase("null")) {
                ans = GlobalTypes.FALSE + GlobalTypes.DATE_STRING_SEPARATOR + answer;
            } else {
                ans = null;
            }
        }
        queFormBean.setAnswer(ans);
        if (isMorbidityQuestion) {
            String result = DynamicUtils.checkForMorbidity(queFormBean.getQuestion(), answer, queFormBean.getSubform(), queFormBean.getLoopCounter() + "", null, null);
            if (inputTextView.getEditText() != null) {
                inputTextView.getEditText().setTextColor(SewaUtil.getColorForMorbiditySims(result));
            }
        }
        Log.d("" + queFormBean.getQuestion(), "" + queFormBean.getAnswer());
    }

    private void reset() {
        answer = null;
    }

}
