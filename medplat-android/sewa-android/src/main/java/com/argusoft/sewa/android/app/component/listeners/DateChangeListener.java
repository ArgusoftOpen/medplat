package com.argusoft.sewa.android.app.component.listeners;

import android.app.DatePickerDialog;
import android.content.Context;
import com.argusoft.sewa.android.app.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DateChangeListener implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private final QueFormBean queFormBean;
    private final Context context;
    private TextView txtDate;

    public DateChangeListener(QueFormBean queFormBean, Context context) {
        this.queFormBean = queFormBean;
        this.context = context;
    }

    @Override
    public void onDateSet(DatePicker dp, int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);

        if (txtDate != null) {
            txtDate.setText(new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault()).format(calendar.getTime()));
            Log.i(getClass().getSimpleName(), queFormBean.getQuestion() + " is set to : " + txtDate.getText());
        }
        queFormBean.setAnswer(calendar.getTimeInMillis());
        String answer = null;
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
            String tmpDataObj;
            if (validation != null) {
                Calendar calendar1 = Calendar.getInstance();
                if (loopCounter == 0) {
                    tmpDataObj = SharedStructureData.relatedPropertyHashTable.get(queFormBean.getRelatedpropertyname());
                    if (tmpDataObj != null
                            && !tmpDataObj.equalsIgnoreCase("null")) {
                        calendar1.setTimeInMillis(Long.parseLong(tmpDataObj));
                        SewaUtil.generateToast(context, validation);
                        if (txtDate != null) {
                            txtDate.setText(new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault()).format(calendar1.getTime()));
                        }
                        queFormBean.setAnswer(SharedStructureData.relatedPropertyHashTable.get(queFormBean.getRelatedpropertyname()));
                    } else {
                        SewaUtil.generateToast(context, validation);
                        if (txtDate != null) {
                            txtDate.setText(UtilBean.getMyLabel(GlobalTypes.SELECT_DATE_TEXT));
                        }
                        queFormBean.setAnswer(null);
                    }
                } else {
                    tmpDataObj = SharedStructureData.relatedPropertyHashTable.get(queFormBean.getRelatedpropertyname() + loopCounter);
                    if (tmpDataObj != null
                            && !tmpDataObj.equalsIgnoreCase("null")) {
                        calendar1.setTimeInMillis(Long.parseLong(tmpDataObj));
                        SewaUtil.generateToast(context, validation);
                        if (txtDate != null) {
                            txtDate.setText(new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault()).format(calendar1.getTime()));
                        }
                        queFormBean.setAnswer(SharedStructureData.relatedPropertyHashTable.get(queFormBean.getRelatedpropertyname() + loopCounter));
                    } else {
                        SewaUtil.generateToast(context, validation);
                        if (txtDate != null) {
                            txtDate.setText(UtilBean.getMyLabel(GlobalTypes.SELECT_DATE_TEXT));
                        }
                        queFormBean.setAnswer(null);
                    }
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
        DatePickerDialog dp;
        if (txtDate.getText() != null) {
            String[] dateArray = UtilBean.split(txtDate.getText().toString(), GlobalTypes.DATE_STRING_SEPARATOR);
            if (dateArray.length == 3) {
                dp = new DatePickerDialog(context, this, Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[1]) - 1, Integer.parseInt(dateArray[0]));
            } else {
                Calendar calendar = Calendar.getInstance();
                dp = new DatePickerDialog(context, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            }
        } else {
            Calendar calendar = Calendar.getInstance();
            dp = new DatePickerDialog(context, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }
        dp.show();
    }
}