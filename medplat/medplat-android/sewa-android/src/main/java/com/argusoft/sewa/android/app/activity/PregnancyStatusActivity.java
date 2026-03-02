package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.model.MemberPregnancyStatusBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.UtilBean;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by prateek on 12/25/19
 */
@EActivity
public class PregnancyStatusActivity extends MenuActivity {

    @Bean
    SewaFhsServiceImpl fhsService;

    private MemberPregnancyStatusBean memberPregnancyStatusBean;
    private MemberBean memberBean;
    private Map<Integer, Integer> regDateTextViewIds;
    private Map<Integer, Integer> ancDateTextViewIds;
    private Map<Integer, Integer> bpTextViewIds;
    private Map<Integer, Integer> hbTextViewIds;
    private Map<Integer, Integer> urineTextViewIds;
    private Map<Integer, Integer> weightTextViewIds;
    private Map<Integer, Integer> vaccineTextViewIds;
    private Map<Integer, Integer> faTabTextViewIds;
    private Map<Integer, Integer> ifaTabTextViewIds;
    private Map<Integer, Integer> calciumTabTextViewIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Long memberId = getIntent().getExtras() != null ? getIntent().getExtras().getLong("memberId") : -1;
        memberPregnancyStatusBean = fhsService.retrievePregnancyStatusBeanByMemberId(memberId);
        memberBean = fhsService.retrieveMemberBeanByActualId(memberId);
        if (memberPregnancyStatusBean == null) {
            View.OnClickListener myListener = v -> {
                alertDialog.dismiss();
                navigateToHomeScreen(false);
            };
            alertDialog = new MyAlertDialog(this, false,
                    UtilBean.getMyLabel(LabelConstants.NO_PREGNANCY_DETAILS_FOUND),
                    myListener, DynamicUtils.BUTTON_OK);
            alertDialog.show();
        } else {
            initView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SharedStructureData.isLogin) {
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        setTitle(UtilBean.getTitleText(LabelConstants.PREGNANCY_STATUS_TITLE));
    }

    @UiThread
    public void initView() {
        setContentView(R.layout.activity_pregnancy_status);
        initMaps();
        setData();
    }

    private void initMaps() {
        regDateTextViewIds = new HashMap<>();
        regDateTextViewIds.put(1, R.id.pregStatusRegDate1);
        regDateTextViewIds.put(2, R.id.pregStatusRegDate2);
        regDateTextViewIds.put(3, R.id.pregStatusRegDate3);
        regDateTextViewIds.put(4, R.id.pregStatusRegDate4);
        regDateTextViewIds.put(5, R.id.pregStatusRegDate5);
        regDateTextViewIds.put(6, R.id.pregStatusRegDate6);
        regDateTextViewIds.put(7, R.id.pregStatusRegDate7);
        regDateTextViewIds.put(8, R.id.pregStatusRegDate8);
        regDateTextViewIds.put(9, R.id.pregStatusRegDate9);

        ancDateTextViewIds = new HashMap<>();
        ancDateTextViewIds.put(1, R.id.pregStatusAncDate1);
        ancDateTextViewIds.put(2, R.id.pregStatusAncDate2);
        ancDateTextViewIds.put(3, R.id.pregStatusAncDate3);
        ancDateTextViewIds.put(4, R.id.pregStatusAncDate4);
        ancDateTextViewIds.put(5, R.id.pregStatusAncDate5);
        ancDateTextViewIds.put(6, R.id.pregStatusAncDate6);
        ancDateTextViewIds.put(7, R.id.pregStatusAncDate7);
        ancDateTextViewIds.put(8, R.id.pregStatusAncDate8);
        ancDateTextViewIds.put(9, R.id.pregStatusAncDate9);

        bpTextViewIds = new HashMap<>();
        bpTextViewIds.put(1, R.id.pregStatusBp1);
        bpTextViewIds.put(2, R.id.pregStatusBp2);
        bpTextViewIds.put(3, R.id.pregStatusBp3);
        bpTextViewIds.put(4, R.id.pregStatusBp4);
        bpTextViewIds.put(5, R.id.pregStatusBp5);
        bpTextViewIds.put(6, R.id.pregStatusBp6);
        bpTextViewIds.put(7, R.id.pregStatusBp7);
        bpTextViewIds.put(8, R.id.pregStatusBp8);
        bpTextViewIds.put(9, R.id.pregStatusBp9);

        hbTextViewIds = new HashMap<>();
        hbTextViewIds.put(1, R.id.pregStatusHb1);
        hbTextViewIds.put(2, R.id.pregStatusHb2);
        hbTextViewIds.put(3, R.id.pregStatusHb3);
        hbTextViewIds.put(4, R.id.pregStatusHb4);
        hbTextViewIds.put(5, R.id.pregStatusHb5);
        hbTextViewIds.put(6, R.id.pregStatusHb6);
        hbTextViewIds.put(7, R.id.pregStatusHb7);
        hbTextViewIds.put(8, R.id.pregStatusHb8);
        hbTextViewIds.put(9, R.id.pregStatusHb9);

        urineTextViewIds = new HashMap<>();
        urineTextViewIds.put(1, R.id.pregStatusUrine1);
        urineTextViewIds.put(2, R.id.pregStatusUrine2);
        urineTextViewIds.put(3, R.id.pregStatusUrine3);
        urineTextViewIds.put(4, R.id.pregStatusUrine4);
        urineTextViewIds.put(5, R.id.pregStatusUrine5);
        urineTextViewIds.put(6, R.id.pregStatusUrine6);
        urineTextViewIds.put(7, R.id.pregStatusUrine7);
        urineTextViewIds.put(8, R.id.pregStatusUrine8);
        urineTextViewIds.put(9, R.id.pregStatusUrine9);

        weightTextViewIds = new HashMap<>();
        weightTextViewIds.put(1, R.id.pregStatusWeight1);
        weightTextViewIds.put(2, R.id.pregStatusWeight2);
        weightTextViewIds.put(3, R.id.pregStatusWeight3);
        weightTextViewIds.put(4, R.id.pregStatusWeight4);
        weightTextViewIds.put(5, R.id.pregStatusWeight5);
        weightTextViewIds.put(6, R.id.pregStatusWeight6);
        weightTextViewIds.put(7, R.id.pregStatusWeight7);
        weightTextViewIds.put(8, R.id.pregStatusWeight8);
        weightTextViewIds.put(9, R.id.pregStatusWeight9);

        vaccineTextViewIds = new HashMap<>();
        vaccineTextViewIds.put(1, R.id.pregStatusVaccine1);
        vaccineTextViewIds.put(2, R.id.pregStatusVaccine2);
        vaccineTextViewIds.put(3, R.id.pregStatusVaccine3);
        vaccineTextViewIds.put(4, R.id.pregStatusVaccine4);
        vaccineTextViewIds.put(5, R.id.pregStatusVaccine5);
        vaccineTextViewIds.put(6, R.id.pregStatusVaccine6);
        vaccineTextViewIds.put(7, R.id.pregStatusVaccine7);
        vaccineTextViewIds.put(8, R.id.pregStatusVaccine8);
        vaccineTextViewIds.put(9, R.id.pregStatusVaccine9);

        faTabTextViewIds = new HashMap<>();
        faTabTextViewIds.put(1, R.id.pregStatusFaTab1);
        faTabTextViewIds.put(2, R.id.pregStatusFaTab2);
        faTabTextViewIds.put(3, R.id.pregStatusFaTab3);

        ifaTabTextViewIds = new HashMap<>();
        ifaTabTextViewIds.put(4, R.id.pregStatusIfaTab1);
        ifaTabTextViewIds.put(5, R.id.pregStatusIfaTab2);
        ifaTabTextViewIds.put(6, R.id.pregStatusIfaTab3);
        ifaTabTextViewIds.put(7, R.id.pregStatusIfaTab4);
        ifaTabTextViewIds.put(8, R.id.pregStatusIfaTab5);
        ifaTabTextViewIds.put(9, R.id.pregStatusIfaTab6);


        calciumTabTextViewIds = new HashMap<>();
        calciumTabTextViewIds.put(4, R.id.pregStatusCalciumTab1);
        calciumTabTextViewIds.put(5, R.id.pregStatusCalciumTab2);
        calciumTabTextViewIds.put(6, R.id.pregStatusCalciumTab3);
        calciumTabTextViewIds.put(7, R.id.pregStatusCalciumTab4);
        calciumTabTextViewIds.put(8, R.id.pregStatusCalciumTab5);
        calciumTabTextViewIds.put(9, R.id.pregStatusCalciumTab6);
    }

    private void setData() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd\nMMM", Locale.getDefault());
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        if (memberPregnancyStatusBean.getRegDate() != null) {
            int month = UtilBean.calculateMonthsBetweenDates(memberBean.getLmpDate(), memberPregnancyStatusBean.getRegDate());
            Integer textViewId = regDateTextViewIds.get(month);
            if (textViewId != null) {
                TextView textView = findViewById(textViewId);
                textView.setText(sdf.format(memberPregnancyStatusBean.getRegDate()).toUpperCase());
                textView.setBackgroundColor(ContextCompat.getColor(context, R.color.pregnancyStatusTextSelected));
            }
        }

        if (memberPregnancyStatusBean.getAncDate() != null) {
            String[] ancDates = memberPregnancyStatusBean.getAncDate().split(",");
            for (String ancDate : ancDates) {
                try {
                    Date date = sdf1.parse(ancDate);
                    int month = UtilBean.calculateMonthsBetweenDates(memberBean.getLmpDate(), date);
                    Integer textViewId = ancDateTextViewIds.get(month);
                    if (textViewId != null) {
                        TextView textView = findViewById(textViewId);
                        textView.setText(sdf.format(date).toUpperCase());
                        textView.setBackground(ContextCompat.getDrawable(context, R.drawable.pregnancy_status_textview_background_green));
                    }
                } catch (ParseException e) {
                    Log.e(getClass().getName(), null, e);
                }
            }
        }


        if (memberPregnancyStatusBean.getBp() != null) {
            String[] bps = memberPregnancyStatusBean.getBp().split(",");
            for (String bp : bps) {
                String[] split = bp.split("#");
                try {
                    Date date = sdf1.parse(split[0]);
                    int month = UtilBean.calculateMonthsBetweenDates(memberBean.getLmpDate(), date);
                    Integer textViewId = bpTextViewIds.get(month);
                    if (textViewId != null) {
                        TextView textView = findViewById(textViewId);
                        String text = split[1] + "\nmmHg";
                        textView.setText(text);
                        textView.setTextColor(ContextCompat.getColor(context, R.color.pregnancyStatusTextSelected));
                        textView.setBackground(ContextCompat.getDrawable(context, R.drawable.pregnancy_status_textview_background_white));
                    }
                } catch (ParseException e) {
                    Log.e(getClass().getName(), null, e);
                }
            }
        }

        if (memberPregnancyStatusBean.getHaemoglobin() != null) {
            String[] hbs = memberPregnancyStatusBean.getHaemoglobin().split(",");
            for (String hb : hbs) {
                String[] split = hb.split("#");
                try {
                    Date date = sdf1.parse(split[0]);
                    int month = UtilBean.calculateMonthsBetweenDates(memberBean.getLmpDate(), date);
                    Integer textViewId = hbTextViewIds.get(month);
                    if (textViewId != null) {
                        TextView textView = findViewById(textViewId);
                        String text = split[1] + "\ng/dl";
                        textView.setText(text);
                        textView.setTextColor(ContextCompat.getColor(context, R.color.pregnancyStatusTextSelected));
                        textView.setBackground(ContextCompat.getDrawable(context, R.drawable.pregnancy_status_textview_background_white));
                    }
                } catch (ParseException e) {
                    Log.e(getClass().getName(), null, e);
                }
            }
        }

        if (memberPregnancyStatusBean.getUrineTest() != null) {
            String[] urineTests = memberPregnancyStatusBean.getUrineTest().split(",");
            for (String ur : urineTests) {
                String[] split = ur.split("#");
                try {
                    Date date = sdf1.parse(split[0]);
                    int month = UtilBean.calculateMonthsBetweenDates(memberBean.getLmpDate(), date);
                    Integer textViewId = urineTextViewIds.get(month);
                    if (textViewId != null) {
                        TextView textView = findViewById(textViewId);
                        textView.setText(split[1]);
                        textView.setTextColor(ContextCompat.getColor(context, R.color.pregnancyStatusTextSelected));
                        textView.setBackground(ContextCompat.getDrawable(context, R.drawable.pregnancy_status_textview_background_white));
                    }
                } catch (ParseException e) {
                    Log.e(getClass().getName(), null, e);
                }
            }
        }

        if (memberPregnancyStatusBean.getWeight() != null) {
            String[] weights = memberPregnancyStatusBean.getWeight().split(",");
            for (String weight : weights) {
                String[] split = weight.split("#");
                try {
                    Date date = sdf1.parse(split[0]);
                    int month = UtilBean.calculateMonthsBetweenDates(memberBean.getLmpDate(), date);
                    Integer textViewId = weightTextViewIds.get(month);
                    if (textViewId != null) {
                        TextView textView = findViewById(textViewId);
                        String text = split[1] + "\nKgs";
                        textView.setText(text);
                        textView.setTextColor(ContextCompat.getColor(context, R.color.pregnancyStatusTextSelected));
                        textView.setBackground(ContextCompat.getDrawable(context, R.drawable.pregnancy_status_textview_background_white));
                    }
                } catch (ParseException e) {
                    Log.e(getClass().getName(), null, e);
                }
            }
        }

        if (memberPregnancyStatusBean.getImmunisation() != null) {
            String[] vaccines = memberPregnancyStatusBean.getImmunisation().split(",");
            for (String vaccine : vaccines) {
                String[] split = vaccine.split("#");
                try {
                    Date date = sdf1.parse(split[0]);
                    int month = UtilBean.calculateMonthsBetweenDates(memberBean.getLmpDate(), date);
                    Integer textViewId = vaccineTextViewIds.get(month);
                    if (textViewId != null) {
                        TextView textView = findViewById(textViewId);
                        textView.setText(sdf.format(date).toUpperCase());
                        textView.setBackground(ContextCompat.getDrawable(context, R.drawable.pregnancy_status_textview_background_green));
                    }
                } catch (ParseException e) {
                    Log.e(getClass().getName(), null, e);
                }
            }
        }

        if (memberPregnancyStatusBean.getFaTablets() != null) {
            String[] faTabs = memberPregnancyStatusBean.getFaTablets().split(",");
            for (String faTab : faTabs) {
                String[] split = faTab.split("#");
                try {
                    Date date = sdf1.parse(split[0]);
                    int month = UtilBean.calculateMonthsBetweenDates(memberBean.getLmpDate(), date);
                    Integer textViewId = faTabTextViewIds.get(month);
                    if (textViewId != null) {
                        TextView textView = findViewById(textViewId);
                        textView.setText(split[1]);
                    }
                } catch (ParseException e) {
                    Log.e(getClass().getName(), null, e);
                }
            }
        }

        if (memberPregnancyStatusBean.getIfaTablets() != null) {
            String[] ifaTabs = memberPregnancyStatusBean.getIfaTablets().split(",");
            for (String ifaTab : ifaTabs) {
                String[] split = ifaTab.split("#");
                try {
                    Date date = sdf1.parse(split[0]);
                    int month = UtilBean.calculateMonthsBetweenDates(memberBean.getLmpDate(), date);
                    Integer textViewId = ifaTabTextViewIds.get(month);
                    if (textViewId != null) {
                        TextView textView = findViewById(textViewId);
                        textView.setText(split[1]);
                    }
                } catch (ParseException e) {
                    Log.e(getClass().getName(), null, e);
                }
            }
        }

        if (memberPregnancyStatusBean.getCalciumTablets() != null) {
            String[] calciumTabs = memberPregnancyStatusBean.getCalciumTablets().split(",");
            for (String calciumTab : calciumTabs) {
                String[] split = calciumTab.split("#");
                try {
                    Date date = sdf1.parse(split[0]);
                    int month = UtilBean.calculateMonthsBetweenDates(memberBean.getLmpDate(), date);
                    Integer textViewId = calciumTabTextViewIds.get(month);
                    if (textViewId != null) {
                        TextView textView = findViewById(textViewId);
                        textView.setText(split[1]);
                    }
                } catch (ParseException e) {
                    Log.e(getClass().getName(), null, e);
                }
            }
        }
    }
}
