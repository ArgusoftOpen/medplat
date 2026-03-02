package com.argusoft.sewa.android.app.component.listeners;

import android.content.Context;
import com.argusoft.sewa.android.app.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.argusoft.sewa.android.app.datastructure.PageFormBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.google.android.material.textfield.TextInputEditText;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * @author alpeshkyada
 */
public class TextFocusChangeListener implements View.OnFocusChangeListener {

    private Context context;
    private QueFormBean queFormBean;
    private View nextView;
    private View hiddenView;
    //added in phase 3
    private boolean isMorbiQuestion;

    public TextFocusChangeListener(Context context, QueFormBean queFormBean) {
        this.context = context;
        this.queFormBean = queFormBean;
        if (queFormBean.getSubform() != null && queFormBean.getSubform().toLowerCase().contains(GlobalTypes.DATA_MAP_MORBIDITY_CONDITION.toLowerCase())) {
            isMorbiQuestion = true;
        }
    }

    @Override
    public void onFocusChange(View view, boolean bln) {
        if (!bln) {
            TextInputEditText editText = (TextInputEditText) view;
            if (editText.getText() != null) {
                queFormBean.setAnswer(editText.getText().toString());
            }
            DynamicUtils.applyFormula(queFormBean, true);
            //next visible question will show portion
            QueFormBean setNextVisibleQuestion = DynamicUtils.setNextVisibleQuestionInSamePage(queFormBean);
            if (setNextVisibleQuestion != null) {
                Log.i(getClass().getSimpleName(), "Next Question is :" + setNextVisibleQuestion.getId());
                if (hiddenView != null) {
                    hiddenView.setVisibility(View.GONE);
                }
                if (nextView != null) {
                    nextView.setVisibility(View.GONE);
                }

                PageFormBean pageFromNext = DynamicUtils.getPageFromNext(DynamicUtils.getLoopId(queFormBean.getId(), setNextVisibleQuestion.getLoopCounter()));
                if (pageFromNext != null) {
                    LinearLayout pageLayout = pageFromNext.getPageLayout(true, 0);
                    if (pageLayout != null) {
                        hiddenView = pageLayout.findViewById(DynamicUtils.HIDDEN_LAYOUT_ID);
                    }

                    if (hiddenView != null) {
                        hiddenView.setVisibility(View.VISIBLE);
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
            } else {
                if (hiddenView != null) {
                    hiddenView.setVisibility(View.GONE);
                }
                if (nextView != null) {
                    nextView.setVisibility(View.GONE);
                }
            }
            //  morbidity portions
            if (isMorbiQuestion) {
                String answer = editText.getText().toString();
                String result = DynamicUtils.checkForMorbidity(queFormBean.getQuestion(), answer, queFormBean.getSubform(), queFormBean.getLoopCounter() + "", null, null);
                editText.setTextColor(SewaUtil.getColorForMorbiditySims(result));
            }
            Log.i(getClass().getSimpleName(), queFormBean.getQuestion() + " = " + queFormBean.getAnswer());

            InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
