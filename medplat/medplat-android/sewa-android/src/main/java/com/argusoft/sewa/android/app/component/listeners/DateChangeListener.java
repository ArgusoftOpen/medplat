package com.argusoft.sewa.android.app.component.listeners;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateChangeListener implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private final QueFormBean queFormBean;
    private final Context context;
    private TextView txtDate;
    private TextView ageDisplay;

    private boolean isFutureDate;

    public DateChangeListener(QueFormBean queFormBean, Context context, boolean isFutureDate) {
        this.queFormBean = queFormBean;
        this.context = context;
        this.isFutureDate = isFutureDate;
    }

    public DateChangeListener(QueFormBean queFormBean, Context context, TextView ageDisplay, boolean isFutureDate) {
        this.queFormBean = queFormBean;
        this.context = context;
        this.ageDisplay = ageDisplay;
        this.isFutureDate = isFutureDate;
    }

    @Override
    public void onDateSet(DatePicker dp, int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);

        if (txtDate != null) {
            txtDate.setText(new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault()).format(calendar.getTime()));
            Log.i(getClass().getSimpleName(), queFormBean.getQuestion() + " is set to : " + txtDate.getText());
        }

        if (ageDisplay != null) {
            int[] ageYearMonthDayArray = UtilBean.calculateAgeYearMonthDayOnGivenDate(calendar.getTime().getTime(), new Date().getTime());
            String age = UtilBean.getAgeDisplay(ageYearMonthDayArray[0], ageYearMonthDayArray[1], ageYearMonthDayArray[2]);
            ageDisplay.setText(UtilBean.getMyLabel(age));
        }

        queFormBean.setAnswer(calendar.getTimeInMillis());
        String answer = null;
        if (queFormBean.getAnswer() != null) {
            answer = queFormBean.getAnswer().toString();
        }

        if (answer == null || answer.trim().length() == 0 || answer.trim().equalsIgnoreCase("null")) {
            return;
        }

        List<ValidationTagBean> validations = queFormBean.getValidations();
        int loopCounter = 0;
        if (!queFormBean.isIgnoreLoop()) {
            loopCounter = queFormBean.getLoopCounter();
        }

        String validation = DynamicUtils.checkValidation(answer, loopCounter, validations);
        if (validation == null) {
            DynamicUtils.applyFormula(queFormBean, true);
            return;
        }

        String relatedPropertyName = queFormBean.getRelatedpropertyname();
        if (loopCounter != 0) {
            relatedPropertyName = queFormBean.getRelatedpropertyname() + loopCounter;
        }

        String tmpDataObj = SharedStructureData.relatedPropertyHashTable.get(relatedPropertyName);
        SewaUtil.generateToast(context, validation);
        if (tmpDataObj != null && !tmpDataObj.equalsIgnoreCase("null")) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(Long.parseLong(tmpDataObj));
            if (txtDate != null) {
                txtDate.setText(new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault()).format(calendar1.getTime()));
            }
            if (ageDisplay != null) {
                int[] ageYearMonthDayArray = UtilBean.calculateAgeYearMonthDayOnGivenDate(calendar1.getTime().getTime(), new Date().getTime());
                String age = UtilBean.getAgeDisplay(ageYearMonthDayArray[0], ageYearMonthDayArray[1], ageYearMonthDayArray[2]);
                ageDisplay.setText(UtilBean.getMyLabel(age));
            }
            queFormBean.setAnswer(SharedStructureData.relatedPropertyHashTable.get(relatedPropertyName));
        } else {
            if (txtDate != null) {
                txtDate.setText(UtilBean.getMyLabel(GlobalTypes.SELECT_DATE_TEXT));
            }
            if (ageDisplay != null) {
                ageDisplay.setText(UtilBean.getMyLabel("Age as per date selected"));
            }
            queFormBean.setAnswer(null);
        }
        DynamicUtils.applyFormula(queFormBean, false);
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
        if (isFutureDate) {
            dp.getDatePicker().setMaxDate(new Date().getTime());
        }
        dp.show();
    }
}