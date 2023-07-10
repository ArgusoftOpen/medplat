package com.argusoft.sewa.android.app.component.listeners;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author alpeshkyada
 */
public class AgeBoxChangeListener implements View.OnFocusChangeListener {

    private int year;
    private int month;
    private int day;
    private QueFormBean queFormBean;
    private final Context context;
    private TextView textDate;

    public AgeBoxChangeListener(Context context, QueFormBean queFormBean) {
        this.context = context;
        this.queFormBean = queFormBean;
        year = -1;
        month = 0;
    }

    public void setTextDate(TextView textDate) {
        this.textDate = textDate;
    }

    @Override
    public void onFocusChange(View view, boolean bln) {
        EditText editText = (EditText) view;
        if (!bln) {
            int textId = editText.getId();
            if (textId == IdConstants.AGE_BOX_YEAR_EDIT_TEXT_ID) {  //that is year
                try {
                    year = Integer.parseInt(editText.getText().toString());
                    editText.setHint(LabelConstants.ENTER + " " + GlobalTypes.YEAR);
                } catch (Exception e) {
                    editText.setHint(LabelConstants.ENTER_VALID + " " + GlobalTypes.YEAR);
                    editText.setText("");
                    SewaUtil.generateToast(view.getContext(), "Enter valid years");
                    year = -1;
                } finally {
                    setAnswer();
                }
            } else if (textId == IdConstants.AGE_BOX_MONTH_EDIT_TEXT_ID) { //that is month
                try {
                    month = Integer.parseInt(editText.getText().toString());
                    editText.setHint(LabelConstants.ENTER + " " + GlobalTypes.MONTH);
                    setAnswer();
                } catch (Exception e) {
                    editText.setHint(LabelConstants.ENTER_VALID + " " + GlobalTypes.MONTH);
                    editText.setText("0");
                    SewaUtil.generateToast(view.getContext(), "Enter valid months");
                    month = 0;
                } finally {
                    setAnswer();
                }
//            } else if (textId == IdConstants.AGE_BOX_DAY_EDIT_TEXT_ID) {
//                try {
//                    day = Integer.parseInt(editText.getText().toString());
//                    editText.setHint(LabelConstants.ENTER + " " + GlobalTypes.DAY);
//                    setAnswer();
//                } catch (Exception e) {
//                    editText.setHint(LabelConstants.ENTER_VALID + " " + GlobalTypes.DAY);
//                    editText.setText("");
//                    day = -1;
//                } finally {
//                    setAnswer();
//                }
            }
        }
    }

    private void setAnswer() {
//        String ageFormat = null;
//        if (year != -1 && month != -1 && day != -1) {
//            ageFormat = year + GlobalTypes.DATE_STRING_SEPARATOR + month + GlobalTypes.DATE_STRING_SEPARATOR + day;
        if (year != -1 && month != -1) {

            if (month < 0 || month > 11) {
                queFormBean.setAnswer(null);
                SewaUtil.generateToast(context, "Months should be between 0 to 11");
                return;
            }

            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.YEAR, (-1) * year);
            instance.add(Calendar.MONTH, (-1) * month);
            instance.set(Calendar.DATE, 1);
            instance.set(Calendar.HOUR_OF_DAY, 0);
            instance.set(Calendar.MINUTE, 0);
            instance.set(Calendar.SECOND, 0);
            instance.set(Calendar.MILLISECOND, 0);
//            if ()
//            SharedStructureData.relatedPropertyHashTable.put(dobRelatedPropertyName, String.valueOf(instance.getTime().getTime()));
            queFormBean.setAnswer(instance.getTime().getTime());
            Log.i(getClass().getSimpleName(), "The Age in year/month/day wise : " + instance.getTime());

            if (textDate != null) {
                String calculatedDate = new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault()).format(instance.getTime());
                textDate.setText(calculatedDate);
            }
        } else {
            queFormBean.setAnswer(null);
            if (textDate != null) {
                textDate.setText(UtilBean.getMyLabel(LabelConstants.YEAR_AND_MONTH_NOT_YET_ENTERED));
            }
        }
    }
}
