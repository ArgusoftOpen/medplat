package com.argusoft.sewa.android.app.component.listeners;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateChangeListenerStatic implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private final Context context;
    private TextView txtDate;
    private Date dateSet;
    private List<ValidationTagBean> validations;

    public DateChangeListenerStatic(Context context, List<ValidationTagBean> validations) {
        this.context = context;
        this.validations = validations;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, 0, 0, 0);
        String validationMessage = DynamicUtils.checkValidation(String.valueOf(calendar.getTime().getTime()), 0, validations);
        if (validationMessage != null) {
            txtDate.setText(UtilBean.getMyLabel(GlobalTypes.SELECT_DATE_TEXT));
            dateSet = null;
            SewaUtil.generateToast(context, UtilBean.getMyLabel(validationMessage));
        } else {
            dateSet = calendar.getTime();
            if (txtDate != null) {
                txtDate.setText(new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault()).format(calendar.getTime()));
            }
        }
    }

    @Override
    public void onClick(View view) {
        txtDate = view.findViewById(IdConstants.DATE_PICKER_TEXT_DATE_ID);
        DatePickerDialog dp;

        if (txtDate.getText() != null) {
            String[] ddmmyyy = UtilBean.split(txtDate.getText().toString(), GlobalTypes.DATE_STRING_SEPARATOR);
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

    public Date getDateSet() {
        return dateSet;
    }

    public void setDateSet(Date dateSet) {
        this.dateSet = dateSet;
    }
}
