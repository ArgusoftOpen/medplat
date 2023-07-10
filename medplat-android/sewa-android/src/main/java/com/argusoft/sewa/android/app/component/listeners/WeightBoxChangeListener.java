package com.argusoft.sewa.android.app.component.listeners;

import android.content.Context;
import com.argusoft.sewa.android.app.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author alpeshkyada
 */
public class WeightBoxChangeListener implements View.OnFocusChangeListener,
        CompoundButton.OnCheckedChangeListener,
        AdapterView.OnItemSelectedListener,
        View.OnClickListener,
        MaterialPickerOnPositiveButtonClickListener<Long> {

    private final Context context;
    private final QueFormBean queFormBean;
    private final boolean isShowDate;
    private final String dateMandatoryMessage;
    private int kg;
    private int gm;
    private Long date;
    private boolean isChecked;
    private View mainLayout;
    private TextView txtDate;
    private String weightMandatoryMessage;
    private EditText txtKg;
    private Spinner comboGram;
    private Spinner comboKilo;

    public WeightBoxChangeListener(Context context, QueFormBean queFormBean, boolean isShowDate) {
        this.context = context;
        this.queFormBean = queFormBean;
        this.isShowDate = isShowDate;
        this.dateMandatoryMessage = LabelConstants.WHEN_WAS_WEIGHT_TAKEN;
        kg = gm = -1;
        date = 0L;
        isChecked = false;
    }

    @Override  // if checked changed
    public void onCheckedChanged(CompoundButton cb, boolean bln) {
        isChecked = bln;
        if (mainLayout == null) {
            mainLayout = (((View) queFormBean.getQuestionTypeView()).findViewById(IdConstants.WEIGHT_BOX_INPUT_LAYOUT_ID));
        }
        if (isChecked) {
            mainLayout.setVisibility(View.GONE);
            resetComponent();
        } else {
            mainLayout.setVisibility(View.VISIBLE);
        }
        setAnswer();
    }

    @Override  // if kilogram entered
    public void onFocusChange(View view, boolean bln) {
        if (!bln) {
            txtKg = (EditText) view;
            try {
                kg = Integer.parseInt(txtKg.getText().toString());
            } catch (Exception e) {
                kg = -1;
            } finally {
                setAnswer();
            }
        }
    }

    @Override  // if gram is selected 
    public void onItemSelected(AdapterView<?> av, View view, int i, long l) {
        try {
            if (av.getId() == IdConstants.WEIGHT_BOX_SPINNER_GRAM_ID) {
                gm = i - 1;
                comboGram = (Spinner) av;
            } else if (av.getId() == IdConstants.WEIGHT_BOX_SPINNER_KILO_ID) {
                kg = i - 1;
                comboKilo = (Spinner) av;
            }
        } catch (Exception e) {
            gm = i - 1;
            Log.e(getClass().getSimpleName(), null, e);
        }
        setAnswer();
    }

    @Override
    public void onNothingSelected(AdapterView<?> av) {
        // Do nothing
    }

    @Override  // if click on date selection
    public void onClick(View view) {
        txtDate = view.findViewById(IdConstants.DATE_PICKER_TEXT_DATE_ID);

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        if (date != null && date != 0L) {
            builder.setSelection(date);
        }
        MaterialDatePicker<Long> datePicker = builder.build();
        datePicker.addOnPositiveButtonClickListener(this);
        datePicker.show(((AppCompatActivity) context).getSupportFragmentManager(), LabelConstants.MY_DATE_PICKER);
    }

    @Override
    public void onPositiveButtonClick(Long selection) {
        if (txtDate != null) {
            txtDate.setText(new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault()).format(selection));
        }
        this.date = selection;
        setAnswer();
    }

    private void setAnswer() {
        String answer = null;
        if (isChecked) {
            answer = GlobalTypes.NO_WEIGHT;
        } else {
            if (isShowDate) {
                if (date > 0) {
                    if (kg != -1) {
                        if (gm != -1) {
                            answer = kg + GlobalTypes.DOT_SEPARATOR + gm + GlobalTypes.DATE_STRING_SEPARATOR + date;
                        } else {
                            answer = kg + GlobalTypes.DOT_SEPARATOR + 0 + GlobalTypes.DATE_STRING_SEPARATOR + date;
                        }
                    } else if (gm != -1) {
                        answer = 0 + GlobalTypes.DOT_SEPARATOR + gm + GlobalTypes.DATE_STRING_SEPARATOR + date;
                    } else if (weightMandatoryMessage != null) {
                        queFormBean.setMandatorymessage(weightMandatoryMessage);
                    }
                } else {
                    weightMandatoryMessage = queFormBean.getMandatorymessage();
                    queFormBean.setMandatorymessage(dateMandatoryMessage);
                }
            } else {
                if (kg != -1) {
                    if (gm != -1) {
                        answer = kg + GlobalTypes.DOT_SEPARATOR + gm;
                    } else {
                        answer = kg + GlobalTypes.DOT_SEPARATOR + 0;
                    }
                } else if (gm != -1) {
                    answer = 0 + GlobalTypes.DOT_SEPARATOR + gm;
                }
            }
        }
        queFormBean.setAnswer(answer);
        Log.i(getClass().getSimpleName(), queFormBean.getQuestion() + "=" + queFormBean.getAnswer());
        DynamicUtils.applyFormula(queFormBean, true);
    }

    private void resetComponent() {
        if (txtKg != null) {
            txtKg.setText("");
            kg = -1;
        } else if (comboKilo != null) {
            try {
                comboKilo.setSelection(0);
                kg = -1;
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), null, e);
                kg = -1;
            }
        }
        if (comboGram != null) {
            try {
                comboGram.setSelection(0);
                gm = -1;
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), null, e);
                gm = -1;
            }
        }
        if (txtDate != null) {
            txtDate.setText(UtilBean.getMyLabel(LabelConstants.SELECT_DATE));
            date = 0L;
        }
    }
}
