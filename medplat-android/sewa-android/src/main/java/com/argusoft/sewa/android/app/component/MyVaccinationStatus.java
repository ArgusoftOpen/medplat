package com.argusoft.sewa.android.app.component;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.argusoft.sewa.android.app.constants.FullFormConstants;
import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RchConstants;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;

import java.util.HashMap;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author alpesh
 */
public class MyVaccinationStatus {

    private final Context context;
    private final MyVaccination parent;
    private String name;
    private GivenStatus isTaken;
    private long dateOfTaken;
    private List<ValidationTagBean> validation;
    private LinearLayout mainLayout;
    private LinearLayout customDatePicker;
    private MaterialTextView txtDate;
    private static final Integer RADIO_BUTTON_ID_YES = 0;
    private static final Integer RADIO_BUTTON_ID_NO = 1;

    public MyVaccinationStatus(Context context, MyVaccination parent, String name, GivenStatus isTaken, long dateOfTaken, List<ValidationTagBean> validation) {
        this.context = context;
        this.parent = parent;
        this.name = name;
        this.isTaken = isTaken;
        this.dateOfTaken = dateOfTaken;
        this.validation = validation;
        // generate Layout
        setLayout();
    }

    private void setLayout() {
        // generate Layout
        mainLayout = MyStaticComponents.getLinearLayout(context, name.hashCode(), LinearLayout.VERTICAL, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        //generate question View
        mainLayout.addView(MyStaticComponents.generateSubTitleView(context, UtilBean.getMyLabel(FullFormConstants.getFullFormOfVaccines(name.trim()))));

        //generate answer View
        if (name.trim().equals(RchConstants.VITAMIN_A)) {
            mainLayout.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.IS_IT_GIVEN));
        } else {
            mainLayout.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.IS_VACCINATION_GIVEN));
        }

        HashMap<Integer, String> stringMap = new HashMap<>();
        stringMap.put(RADIO_BUTTON_ID_YES, LabelConstants.YES);
        stringMap.put(RADIO_BUTTON_ID_NO, LabelConstants.NO);
        RadioGroup radioGroup = MyStaticComponents.getRadioGroup(context, stringMap, true);
        radioGroup.setOnCheckedChangeListener(parent);
        mainLayout.addView(radioGroup);

        //generate Dependency date view
        customDatePicker = MyStaticComponents.getLinearLayout(context, name.hashCode(), LinearLayout.VERTICAL, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        if (name.trim().equals(RchConstants.VITAMIN_A)) {
            customDatePicker.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.DATE));
        } else {
            customDatePicker.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.DATE_OF_VACCINATION));
        }

        LinearLayout myDatePicker = MyStaticComponents.getCustomDatePickerForStatic(context, parent, DynamicUtils.ID_OF_CUSTOM_DATE_PICKER);
        txtDate = myDatePicker.findViewById(IdConstants.DATE_PICKER_TEXT_DATE_ID); // get Text View from Custom Date Picker ......
        customDatePicker.addView(myDatePicker);

        customDatePicker.setVisibility(View.GONE);
        mainLayout.addView(customDatePicker);
        setVisibility(false);
    }

    public LinearLayout getVaccinationView() {
        if (mainLayout == null) {
            setLayout();
        }
        return mainLayout;
    }

    public void setVisibility(boolean flag) {
        if (mainLayout != null) {
            if (flag) {
                mainLayout.setVisibility(View.VISIBLE);
            } else {
                mainLayout.setVisibility(View.GONE);
            }
        }
    }

    public String isValid() {
        if (isTaken.equals(GivenStatus.NULL)) {
            return LabelConstants.IS_VACCINATION_GIVEN;
        } else if (isTaken.equals(GivenStatus.YES) && dateOfTaken == 0) {
            return LabelConstants.DATE_OF_VACCINATION_REQUIRED_ALERT;
        }
        return null;
    }

    public String getAnswerString() {
        String valid = isValid();
        if (valid == null) {
            if (isTaken.equals(GivenStatus.YES)) {
                return name + GlobalTypes.DATE_STRING_SEPARATOR + isTaken.getValue() + GlobalTypes.DATE_STRING_SEPARATOR + dateOfTaken;
            } else if (isTaken.equals(GivenStatus.NO)) {
                return name + GlobalTypes.DATE_STRING_SEPARATOR + isTaken.getValue();
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GivenStatus getIsTaken() {
        return isTaken;
    }

    public void setIsTaken(GivenStatus isTaken) {
        this.isTaken = isTaken;
    }

    public long getDateOfTaken() {
        return dateOfTaken;
    }

    public void setDateOfTaken(long dateOfTaken) {
        this.dateOfTaken = dateOfTaken;
        if (txtDate != null && dateOfTaken == 0) {
            txtDate.setText(UtilBean.getMyLabel(GlobalTypes.SELECT_DATE_TEXT));
        }
    }

    public String getTxtDate() {
        if (txtDate != null) {
            return txtDate.getText().toString();
        }
        return UtilBean.getMyLabel(GlobalTypes.SELECT_DATE_TEXT);
    }

    public void setTextDate(String dateOfTaken) {
        if (txtDate != null) {
            txtDate.setText(UtilBean.getMyLabel(dateOfTaken));
        }
    }

    public void showDatePicker(boolean flag) {
        if (customDatePicker != null) {
            if (flag) {
                customDatePicker.setVisibility(View.VISIBLE);
            } else {
                customDatePicker.setVisibility(View.GONE);
            }
        }
    }

    public List<ValidationTagBean> getValidation() {
        return validation;
    }

    public void setValidation(List<ValidationTagBean> validation) {
        this.validation = validation;
    }

    @NonNull
    @Override
    public String toString() {
        return "VaccinationStatus{" + "name=" + name + ", isTaken=" + isTaken + ", dateOfTaken=" + dateOfTaken + ", validation=" + validation + '}';
    }

    public enum GivenStatus {

        YES('T'), NO('F'), SKIP('S'), NULL(' ');
        private char status;

        GivenStatus(char status) {
            this.status = status;
        }

        public char getValue() {
            return status;
        }

        @Override
        public String toString() {
            return "GivenStatus{" + "status=" + status + '}';
        }
    }
}
