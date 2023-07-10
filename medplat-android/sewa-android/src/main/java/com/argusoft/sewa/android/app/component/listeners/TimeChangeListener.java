package com.argusoft.sewa.android.app.component.listeners;

import android.app.TimePickerDialog;
import android.content.Context;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.util.Log;

import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;

import java.util.List;

public class TimeChangeListener implements TimePickerDialog.OnTimeSetListener, View.OnClickListener {

    private final QueFormBean queFormBean;
    private final Context context;
    private TextView txtDate;

    public TimeChangeListener(QueFormBean queFormBean, Context context) {
        this.queFormBean = queFormBean;
        this.context = context;
    }

    @Override
    public void onTimeSet(TimePicker dp, int hourOfDay, int minute) {
        String answer = null;
        if (txtDate != null) {
            String text = UtilBean.getFormattedTime(hourOfDay) + ":" + UtilBean.getFormattedTime(minute);
            txtDate.setText(text);
            Log.i(getClass().getSimpleName(), queFormBean.getQuestion() + " is set to : " + txtDate.getText());
        }
        queFormBean.setAnswer(hourOfDay + ":" + minute);

        if (queFormBean.getAnswer() != null) {
            answer = queFormBean.getAnswer().toString();
        }
        if (answer != null && answer.trim().length() > 0 && !answer.trim().equalsIgnoreCase("null")) {

            List<ValidationTagBean> validations = queFormBean.getValidations();
            int loopCounter = 0;
            if (!queFormBean.isIgnoreLoop()) {
                loopCounter = queFormBean.getLoopCounter();
            }

            String validation = DynamicUtils.checkValidation(answer, loopCounter, validations);
            if (validation != null) {
                String tmpDataObj = SharedStructureData.relatedPropertyHashTable.get(queFormBean.getRelatedpropertyname());
                if (tmpDataObj != null && !tmpDataObj.equalsIgnoreCase("null")) {
                    SewaUtil.generateToast(context, validation);
                    if (txtDate != null) {
                        txtDate.setText(String.format("%s:%s", UtilBean.getFormattedTime(hourOfDay), UtilBean.getFormattedTime(minute)));
                    }
                    if (loopCounter == 0) {
                        queFormBean.setAnswer(SharedStructureData.relatedPropertyHashTable.get(queFormBean.getRelatedpropertyname()));
                    } else {
                        queFormBean.setAnswer(SharedStructureData.relatedPropertyHashTable.get(queFormBean.getRelatedpropertyname() + loopCounter));
                    }
                } else {
                    SewaUtil.generateToast(context, validation);
                    if (txtDate != null) {
                        txtDate.setText(UtilBean.getMyLabel(GlobalTypes.SELECT_TIME_TEXT));
                    }
                    queFormBean.setAnswer(null);
                }
                DynamicUtils.applyFormula(queFormBean, false);
            } else {
                DynamicUtils.applyFormula(queFormBean, true);
            }
        }
    }

    @Override
    public void onClick(View view) {
        txtDate = view.findViewById(IdConstants.DATE_PICKER_TEXT_DATE_ID);
        TimePickerDialog tp;
        if (txtDate != null && txtDate.getText() != null && !txtDate.getText().equals(LabelConstants.SELECT_TIME) && !txtDate.getText().equals(UtilBean.getMyLabel(LabelConstants.SELECT_TIME))) {
            int hours = Integer.parseInt(txtDate.getText().toString().split(":")[0]);
            int minutes = Integer.parseInt(txtDate.getText().toString().split(":")[1]);
            tp = new TimePickerDialog(context, R.style.TimePicker, this, hours, minutes, true);
        } else {
            tp = new TimePickerDialog(context, R.style.TimePicker, this, 0, 0, true);
        }
        tp.show();
    }
}
