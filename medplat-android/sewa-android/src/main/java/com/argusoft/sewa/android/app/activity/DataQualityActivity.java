package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.DataQualityConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.DataQualityServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.DataQualityMemberDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.DataQualityBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity
public class DataQualityActivity extends MenuActivity implements View.OnClickListener {

    static class CountData {
        private String type;
        private int total;
        private int successful;
        private int failed;

        public CountData(String type, int total, int successful, int failed) {
            this.type = type;
            this.total = total;
            this.successful = successful;
            this.failed = failed;
        }
    }

    @Bean
    DataQualityServiceImpl dataQualityService;

    @Bean
    SewaServiceImpl sewaService;

    @Bean
    SewaFhsServiceImpl fhsService;

    private LinearLayout bodyLayoutContainer;
    private MyAlertDialog myAlertDialog;
    private TableLayout tableLayout;
    private String screenName;
    private LinearLayout globalPanel;

    private static final String DATA_TABLE_SCREEN = "dataTableScreen";
    private static final String DATA_QUALITY_DETAILS_SCREEN = "dataQualityDetailsScreen";

    private Button nextButton;
    private Map<String, String> map;
    private List<DataQualityBean> allDataQualityBeans;
    private boolean allVerified = false;
    private List<DataQualityMemberDataBean> dataQualityMemberDataBeans = new ArrayList<>();
    private TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, MATCH_PARENT, 2);
    private TableRow.LayoutParams layoutParams1 = new TableRow.LayoutParams(0, MATCH_PARENT, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //To change body of generated methods, choose Tools | Templates.
        context = this;
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (globalPanel != null) {
            setContentView(globalPanel);
        }
        if (!SharedStructureData.isLogin) {
            Intent myIntent = new Intent(context, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        setTitle(UtilBean.getTitleText(LabelConstants.DATA_QUALITY_TITLE));
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        setContentView(globalPanel);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        setBodyDetail();
    }

    @Override
    public void onBackPressed() {
        View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                myAlertDialog.dismiss();
                navigateToHomeScreen(false);
                finish();
            } else {
                myAlertDialog.dismiss();
            }
        };

        myAlertDialog = new MyAlertDialog(context,
                LabelConstants.WANT_TO_CLOSE_DATA_QUALITY_PROCESS,
                myListener, DynamicUtils.BUTTON_YES_NO);
        myAlertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screenName) {
                case DATA_TABLE_SCREEN:
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    setDataQualityDetailsScreen();
                    break;
                case DATA_QUALITY_DETAILS_SCREEN:
                    navigateToHomeScreen(false);
                    break;
                default:
            }
        }
    }

    private void setBodyDetail() {
        screenName = DATA_TABLE_SCREEN;
        allDataQualityBeans = dataQualityService.getAllDataQualityBeans();
        createTableLayout();
        hideProcessDialog();
    }

    private void createTableLayout() {
        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context, UtilBean.getMyLabel(LabelConstants.DATA_QUALITY_VERIFICATION_REPORT)));
        tableLayout = new TableLayout(this);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        List<String> headerNames = new ArrayList<>();
        headerNames.add(LabelConstants.SERVICE_TYPE);
        headerNames.add(LabelConstants.TOTAL_CALLS);
        headerNames.add(LabelConstants.VERIFICATION_SUCCESSFUL);
        headerNames.add(LabelConstants.VERIFICATION_FAILED);
        headerNames.add("%");

        map = new HashMap<>();
        map.put(DataQualityConstants.FHW_CH_SER_VERI, LabelConstants.CHILD_VACCINATION_VERIFICATION);
        map.put(DataQualityConstants.FHW_CH_SER_PREG_VERI, LabelConstants.DELIVERY_AND_CHILD_VERIFICATION);
        map.put(DataQualityConstants.FHW_DEL_VERI, LabelConstants.DELIVERY_VERIFICATION);
        map.put(DataQualityConstants.PREG_REGI_VERI, LabelConstants.PREGNANCY_REGISTRATION_VERIFICATION);
        map.put(DataQualityConstants.FHW_TT_VERI, LabelConstants.TT_INJECTION_VERIFICATION);

        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        row.setPadding(10, 15, 10, 15);
        if (SewaUtil.CURRENT_THEME == R.style.techo_training_app) {
            row.setBackgroundResource(R.drawable.table_row_selector);
        } else {
            row.setBackgroundResource(R.drawable.spinner_item_border);
        }
        int count = 0;
        row.setWeightSum(6);

        for (String string : headerNames) {
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(10, 10, 10, 10);
            textView.setText(UtilBean.getMyLabel(string));
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            if (count == 0) {
                row.addView(textView, layoutParams);
            } else {
                row.addView(textView, layoutParams1);
            }
            count++;
        }
        tableLayout.addView(row, 0);
        bodyLayoutContainer.addView(tableLayout);
        if (allDataQualityBeans != null && !allDataQualityBeans.isEmpty())
            setRowData(allDataQualityBeans);
    }

    private void setRowData(List<DataQualityBean> beans) {
        CountData childServiceVerificationBean = new CountData(map.get(DataQualityConstants.FHW_CH_SER_VERI), 0, 0, 0);
        CountData childServicePregnancyVerificationBean = new CountData(map.get(DataQualityConstants.FHW_CH_SER_PREG_VERI), 0, 0, 0);
        CountData deliveryVerificationBean = new CountData(map.get(DataQualityConstants.FHW_DEL_VERI), 0, 0, 0);
        CountData pregnancyRegisterVerificationBean = new CountData(map.get(DataQualityConstants.PREG_REGI_VERI), 0, 0, 0);
        CountData ttVerificationBean = new CountData(map.get(DataQualityConstants.FHW_TT_VERI), 0, 0, 0);

        for (DataQualityBean bean : beans) {
            DataQualityMemberDataBean dataQualityDataBean;

            MemberBean memberBean = fhsService.retrieveMemberBeanByActualId(bean.getMemberId());
            if (memberBean != null) {
                dataQualityDataBean = new DataQualityMemberDataBean();
                dataQualityDataBean.setHealthId(memberBean.getUniqueHealthId());
                dataQualityDataBean.setName(memberBean.getFirstName() + " " + memberBean.getLastName());
                dataQualityDataBean.setMemberId(bean.getMemberId());
                dataQualityDataBean.setServiceType(bean.getServiceType());
                dataQualityDataBean.setDeliveryHappened(bean.isDeliveryHappened());
                dataQualityDataBean.setDeliveryPlaceVerification(bean.isDeliveryPlaceVerificationDone());
                dataQualityDataBean.setNoOfChildGenderVerification(bean.isNoOfChildGenderVerification());
                dataQualityDataBean.setChildServiceVaccinationStatus(bean.isChildServiceVaccinationStatus());
                dataQualityDataBean.setPregnant(bean.isPregnant());
                dataQualityDataBean.setTtInjectionReceivedStatus(bean.getTtInjectionReceivedStatus());
                dataQualityMemberDataBeans.add(dataQualityDataBean);
            }

            switch (bean.getServiceType()) {
                case DataQualityConstants.FHW_CH_SER_VERI:
                    childServiceVerificationBean.total++;
                    childServiceVerificationBean.successful += getCallCount(bean);
                    break;

                case DataQualityConstants.FHW_CH_SER_PREG_VERI:
                    childServicePregnancyVerificationBean.total = childServicePregnancyVerificationBean.total + 4;
                    childServicePregnancyVerificationBean.successful += getCallCount(bean);
                    break;

                case DataQualityConstants.FHW_DEL_VERI:
                    deliveryVerificationBean.total = deliveryVerificationBean.total + 4;
                    deliveryVerificationBean.successful += getCallCount(bean);
                    break;

                case DataQualityConstants.PREG_REGI_VERI:
                    pregnancyRegisterVerificationBean.total++;
                    pregnancyRegisterVerificationBean.successful += getCallCount(bean);
                    break;

                case DataQualityConstants.FHW_TT_VERI:
                    ttVerificationBean.total++;
                    ttVerificationBean.successful += getCallCount(bean);
                    break;

                default:
            }
        }
        childServiceVerificationBean.failed = childServiceVerificationBean.total - childServiceVerificationBean.successful;
        childServicePregnancyVerificationBean.failed = childServicePregnancyVerificationBean.total - childServicePregnancyVerificationBean.successful;
        deliveryVerificationBean.failed = deliveryVerificationBean.total - deliveryVerificationBean.successful;
        pregnancyRegisterVerificationBean.failed = pregnancyRegisterVerificationBean.total - pregnancyRegisterVerificationBean.successful;
        ttVerificationBean.failed = ttVerificationBean.total - ttVerificationBean.successful;

        List<List<String>> objects = new ArrayList<>();
        objects.add(convertToList(childServiceVerificationBean));
        objects.add(convertToList(childServicePregnancyVerificationBean));
        objects.add(convertToList(deliveryVerificationBean));
        objects.add(convertToList(pregnancyRegisterVerificationBean));
        objects.add(convertToList(ttVerificationBean));
        for (List<String> obj : objects) {
            addRowToTable(obj);
        }
    }

    private List<String> convertToList(CountData obj) {
        DecimalFormat df = new DecimalFormat("0.##");
        List<String> list = new ArrayList<>();
        list.add(obj.type);
        list.add(String.valueOf(obj.total));
        list.add(String.valueOf(obj.successful));
        list.add(String.valueOf(obj.failed));
        if (obj.total != 0) {
            double i = (double) obj.successful / obj.total * 100;
            list.add(df.format(i) + " %");
        } else {
            list.add("0 %");
        }
        return list;
    }

    private int getCallCount(DataQualityBean bean) {
        int count = 0;
        if (bean.isDeliveryHappened()) {
            count++;
        }
        if (bean.isDeliveryPlaceVerificationDone()) {
            count++;
        }
        if (bean.isNoOfChildGenderVerification()) {
            count++;
        }
        if (bean.isChildServiceVaccinationStatus()) {
            count++;
        }
        if (bean.isPregnant()) {
            count++;
        }
        if (bean.getTtInjectionReceivedStatus() != null && (bean.getTtInjectionReceivedStatus().equals("YES") ||
                bean.getTtInjectionReceivedStatus().equals("CANNOT_DETERMINE"))) {
            count++;
        }
        return count;
    }

    private void addRowToTable(List<String> list) {
        TableRow row = new TableRow(this);
        row.setPadding(10, 15, 10, 15);
        if (SewaUtil.CURRENT_THEME == R.style.techo_training_app) {
            row.setBackgroundResource(R.drawable.table_row_selector);
        } else {
            row.setBackgroundResource(R.drawable.spinner_item_border);
        }
        row.setLayoutParams(new TableRow.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        row.setWeightSum(6);

        int count = 0;
        for (String c : list) {
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(10, 10, 10, 10);
            textView.setText(c);
            if (count == 0) {
                row.addView(textView, layoutParams);
            } else {
                row.addView(textView, layoutParams1);
            }
            count++;
        }
        int rowIndex = 1;
        tableLayout.addView(row, rowIndex);
    }

    private void setDataQualityDetailsScreen() {
        screenName = DATA_QUALITY_DETAILS_SCREEN;

        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context, UtilBean.getMyLabel(LabelConstants.DATA_QUALITY_VERIFICATION_DETAILS)));
        LinearLayout parentLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL,
                new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        LinearLayout childLayout;
        LinearLayout leftPanelLayout;
        LinearLayout rightPanelLayout;

        if (dataQualityMemberDataBeans != null && !dataQualityMemberDataBeans.isEmpty()) {
            for (DataQualityMemberDataBean bean : dataQualityMemberDataBeans) {
                String rbText;

                childLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.HORIZONTAL,
                        new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                childLayout.setPadding(0, 0, 0, 20);
                childLayout.setWeightSum(1);

                leftPanelLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL,
                        new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, 0.2f));

                rightPanelLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL,
                        new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, 0.8f));
                rightPanelLayout.setGravity(Gravity.CENTER);

                TextView nameTextView = MyStaticComponents.generateQuestionView(null, null, context, bean.getName() + " (" + bean.getHealthId() + ")");
                if (nameTextView != null) {
                    nameTextView.setTypeface(Typeface.DEFAULT_BOLD);
                    nameTextView.setTextSize(16f);
                }
                TextView serviceTextView = new TextView(context);
                rbText = map.get(bean.getServiceType()) + " call";
                serviceTextView.setText(rbText);
                serviceTextView.setTextSize(16f);
                serviceTextView.setTypeface(Typeface.DEFAULT_BOLD);

                TextView statusTextView = null;
                ImageView imageView = new ImageView(context);

                switch (bean.getServiceType()) {
                    case DataQualityConstants.FHW_CH_SER_PREG_VERI:
                        statusTextView = getServiceCallStatus(bean.isDeliveryHappened(), bean.isDeliveryPlaceVerification(), bean.isNoOfChildGenderVerification(),
                                bean.isChildServiceVaccinationStatus(), null, null);
                        break;
                    case DataQualityConstants.FHW_CH_SER_VERI:
                        statusTextView = getServiceCallStatus(null, null, null,
                                bean.isChildServiceVaccinationStatus(), null, null);
                        break;
                    case DataQualityConstants.FHW_DEL_VERI:
                        statusTextView = getServiceCallStatus(bean.isDeliveryHappened(), bean.isDeliveryPlaceVerification(), bean.isNoOfChildGenderVerification(),
                                null, null, null);
                        break;
                    case DataQualityConstants.FHW_TT_VERI:
                        statusTextView = getServiceCallStatus(null, null, null,
                                null, null, bean.getTtInjectionReceivedStatus());
                        break;
                    case DataQualityConstants.PREG_REGI_VERI:
                        statusTextView = getServiceCallStatus(null, null, null, null, bean.isPregnant(), null);
                        break;
                    default:
                }
                leftPanelLayout.addView(nameTextView);
                leftPanelLayout.addView(serviceTextView);
                if (statusTextView != null) {
                    leftPanelLayout.addView(statusTextView);
                }
                if (allVerified) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.round_checkmark));
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.round_cross));
                }
                imageView.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
                rightPanelLayout.addView(imageView);

                childLayout.addView(leftPanelLayout);
                childLayout.addView(rightPanelLayout);
                parentLayout.addView(childLayout);
                parentLayout.addView(MyStaticComponents.getSeparator(this));
            }
            bodyLayoutContainer.addView(parentLayout);
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, UtilBean.getMyLabel(LabelConstants.NO_MEMBERS_FOUND)));
        }
        nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        hideProcessDialog();
    }

    private TextView getServiceCallStatus(Boolean deliveryHappened, Boolean placeVerification, Boolean noGenderVerification, Boolean vaccinationStatus,
                                          Boolean isPregnant, String ttReceived) {
        TextView textView = new TextView(context);
        textView.setTextSize(16f);
        textView.setText("");

        String str;
        allVerified = true;
        if (deliveryHappened != null) {
            str = LabelConstants.DELIVERY_HAPPENED + LabelConstants.SPACE_NEW_LINE;
            textView.append(getStatus(deliveryHappened, str));
        }
        if (placeVerification != null) {
            str = LabelConstants.DELIVERY_PLACE_VERIFICATION + LabelConstants.SPACE_NEW_LINE;
            textView.append(getStatus(placeVerification, str));
        }
        if (noGenderVerification != null) {
            str = LabelConstants.NO_OF_CHILD_AND_GENDER_VERIFICATION + LabelConstants.SPACE_NEW_LINE;
            textView.append(getStatus(noGenderVerification, str));
        }
        if (vaccinationStatus != null) {
            str = LabelConstants.CHILD_VACCINATION_STATUS + LabelConstants.SPACE_NEW_LINE;
            textView.append(getStatus(vaccinationStatus, str));
        }
        if (isPregnant != null) {
            str = LabelConstants.PREGNANCY_REGISTRATION + LabelConstants.SPACE_NEW_LINE;
            textView.append(getStatus(isPregnant, str));
        }
        if (ttReceived != null) {
            str = LabelConstants.TT_INJECTION_RECEIVED + LabelConstants.SPACE_NEW_LINE;
            if ((ttReceived.equals("YES") || ttReceived.equals("CANNOT_DETERMINE"))) {
                textView.append(getStatus(true, str));
            } else {
                textView.append(getStatus(false, str));
            }
        }
        if (textView.getText().toString().lastIndexOf(LabelConstants.NEW_LINE) > 0) {
            textView.setText(textView.getText().subSequence(0, textView.getText().toString().length() - 1));
        }
        return textView;
    }

    private SpannableString getStatus(boolean status, String text) {
        SpannableString spannableString = new SpannableString(text);
        if (status) {
            ImageSpan imageSpan = new ImageSpan(this, R.drawable.checkmark);
            spannableString.setSpan(imageSpan, text.length() - 2, text.length() - 1, 0);
            return spannableString;
        }
        allVerified = false;
        ImageSpan imageSpan = new ImageSpan(this, R.drawable.cross);
        spannableString.setSpan(imageSpan, text.length() - 2, text.length() - 1, 0);
        return spannableString;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (screenName == null || screenName.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            switch (screenName) {
                case DATA_TABLE_SCREEN:
                    navigateToHomeScreen(false);
                    break;
                case DATA_QUALITY_DETAILS_SCREEN:
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                    setBodyDetail();
                default:
            }
        }
        return true;
    }
}
