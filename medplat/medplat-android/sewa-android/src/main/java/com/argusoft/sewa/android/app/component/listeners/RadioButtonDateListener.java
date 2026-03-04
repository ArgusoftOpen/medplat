/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.component.listeners;

import android.app.DatePickerDialog;
import android.content.Context;
import com.argusoft.sewa.android.app.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.databean.OptionDataBean;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author alpesh
 */
public class RadioButtonDateListener implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, DatePickerDialog.OnDateSetListener {

    public static final int DATE_VIEW_ID = 151;
    private final Context context;
    private final QueFormBean queFormBean;
    private final List<OptionDataBean> optionsOrDatamap;
    private final String[] mandatoryMsg;
    private final List<ValidationTagBean> validations;
    private MaterialTextView txtDate;
    private String ansRadio;
    private long ansDate;
    private View datePickerView;

    public RadioButtonDateListener(Context context, QueFormBean queFormBean) {
        this.context = context;
        this.queFormBean = queFormBean;
        // get option from question 
        optionsOrDatamap = UtilBean.getOptionsOrDataMap(queFormBean, false);
        // get mandatory message from question
        if (queFormBean.getMandatorymessage() != null) {
            String[] temp = UtilBean.split(queFormBean.getMandatorymessage(), GlobalTypes.DATE_STRING_SEPARATOR);
            if (temp.length > 1) {
                queFormBean.setMandatorymessage(temp[0]);
                mandatoryMsg = temp;
            } else {
                mandatoryMsg = new String[]{queFormBean.getMandatorymessage(), queFormBean.getMandatorymessage()};
            }
        } else {
            mandatoryMsg = new String[]{LabelConstants.VALUE_IS_REQUIRED, LabelConstants.VALUE_IS_REQUIRED};
        }
        // get Validation from question 
        if (queFormBean.getValidations() != null) {
            validations = queFormBean.getValidations();
        } else {
            validations = null;
        }
        // date picker layout
        if (queFormBean.getQuestionTypeView() != null) {
            datePickerView = ((View) queFormBean.getQuestionTypeView()).findViewById(DATE_VIEW_ID);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup parent, int position) {
        if (datePickerView == null) {
            datePickerView = ((View) queFormBean.getQuestionTypeView()).findViewById(DATE_VIEW_ID);
        }
        //set key as answer of radio selection
        ansRadio = optionsOrDatamap.get(position).getKey();
        String next = optionsOrDatamap.get(position).getNext();
        if (next != null && !next.equalsIgnoreCase("0") && !next.equalsIgnoreCase("null")) {
            try {
                queFormBean.setNext(next);
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), null, e);
            }
        }
        if (position == 0) // yes pressed  or zero index options checked
        {
            setDate(0); // clear the date 
            showDatePicker(true);
        } else {
            setDate(0); // clear the date 
            showDatePicker(false);
        }
        setAnswer();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == DynamicUtils.ID_OF_CUSTOM_DATE_PICKER) {  // if date picker clicked
            txtDate = v.findViewById(IdConstants.DATE_PICKER_TEXT_DATE_ID); // get Text View from Custome Date Picker ......

            DatePickerDialog dp;
            if (txtDate != null) {
                String[] ddmmyyy = UtilBean.split(txtDate.getText().toString().trim(), GlobalTypes.DATE_STRING_SEPARATOR);
                if (ddmmyyy.length == 3) {
                    dp = new DatePickerDialog(context, this, Integer.parseInt(ddmmyyy[2]), Integer.parseInt(ddmmyyy[1]) - 1, Integer.parseInt(ddmmyyy[0]));
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

    @Override
    public void onDateSet(DatePicker dp, int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);
        UtilBean.clearTimeFromDate(calendar);
        long dateLong = calendar.getTimeInMillis();
        setDate(dateLong);
        // fire any validation 
        int loopcounter = 0;
        if (!queFormBean.isIgnoreLoop()) {
            loopcounter = queFormBean.getLoopCounter();
        }
        String checkValidation = DynamicUtils.checkValidation(ansRadio + GlobalTypes.DATE_STRING_SEPARATOR + dateLong, loopcounter, validations);
        if (checkValidation == null) {
            setDate(dateLong);
        } else {
            setDate(0); // clear the date
            SewaUtil.generateToast(context, checkValidation);
        }
        setAnswer();
    }

    private String isValid() {
        if (ansRadio == null || ansRadio.trim().length() == 0 || ansRadio.trim().equalsIgnoreCase("null")) {
            queFormBean.setMandatorymessage(mandatoryMsg[0]);
            return mandatoryMsg[0];
        } else if (ansRadio.trim().equalsIgnoreCase(optionsOrDatamap.get(0).getKey()) && ansDate < 1) {
            queFormBean.setMandatorymessage(mandatoryMsg[1]);
            return mandatoryMsg[1];
        } else {
            queFormBean.setMandatorymessage(mandatoryMsg[0]);
            return null;
        }
    }

    private void setAnswer() {
        String valid = isValid();
        if (valid == null) {
            String ans = ansRadio;
            if (ansDate > 0) {
                ans += GlobalTypes.DATE_STRING_SEPARATOR + ansDate;
            }
            queFormBean.setAnswer(ans);
        } else {
            queFormBean.setAnswer(null);
        }
        Log.i(getClass().getSimpleName(), "Answer is : " + queFormBean.getAnswer());
    }

    private void setDate(long dateOfTaken) {

        this.ansDate = dateOfTaken;
        if (txtDate != null) {
            if (dateOfTaken > 0) {
                txtDate.setText(UtilBean.getMyLabel(new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault()).format(new Date(ansDate))));
            } else {
                txtDate.setText(UtilBean.getMyLabel(GlobalTypes.SELECT_DATE_TEXT));
            }
        }
    }

    private void showDatePicker(boolean flag) {
        if (datePickerView != null) {
            if (flag) {
                datePickerView.setVisibility(View.VISIBLE);
            } else {
                datePickerView.setVisibility(View.GONE);
            }
        }
    }
}
