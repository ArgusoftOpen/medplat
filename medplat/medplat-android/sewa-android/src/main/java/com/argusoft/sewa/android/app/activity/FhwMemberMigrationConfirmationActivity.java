package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.AshaEventRejectionDataBean;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LoggerBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.model.NotificationBean;
import com.argusoft.sewa.android.app.model.StoreAnswerBean;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.FormMetaDataUtil;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.argusoft.sewa.android.app.util.WSConstants;
import com.google.gson.Gson;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by prateek on 9/24/19
 */
@EActivity
public class FhwMemberMigrationConfirmationActivity extends MenuActivity implements View.OnClickListener {

    private static final Integer RADIO_BUTTON_ID_YES = 1;
    private static final Integer RADIO_BUTTON_ID_NO = 2;
    private static final String MEMBER_DETAILS_SCREEN = "memberDetailsScreen";
    private static final String REJECT_CONFIRMATION_SCREEN = "rejectConfirmationScreen";
    private static final String FINAL_SCREEN = "finalScreen";
    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    FormMetaDataUtil formMetaDataUtil;
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private RadioGroup confirmationRadioGroup;
    private RadioGroup rejectConfirmationRadioGroup;
    private String screen;

    private MemberBean selectedMember;
    private FamilyDataBean selectedFamily;
    private NotificationMobDataBean selectedNotification;
    private SharedPreferences sharedPref;
    private LinearLayout globalPanel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        String notification = getIntent().getStringExtra(GlobalTypes.NOTIFICATION);
        selectedNotification = new Gson().fromJson(notification, NotificationMobDataBean.class);
        selectedMember = fhsService.retrieveMemberBeanByActualId(selectedNotification.getMemberId());
        selectedFamily = fhsService.retrieveFamilyDataBeanByFamilyId(selectedMember.getFamilyId());
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (globalPanel != null) {
            setContentView(globalPanel);
        }
        if (!SharedStructureData.isLogin) {
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        setTitle(UtilBean.getTitleText(LabelConstants.MEMBER_MIGRATION_CONFORMATION));
        setSubTitle(UtilBean.getMemberFullName(selectedMember));
    }

    @UiThread
    @Override
    public void onBackPressed() {

        alertDialog = new MyAlertDialog(this,
                LabelConstants.ON_MEMBER_MIGRATION_CONFORMATION_CLOSE_ALERT,
                this, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    private void initView() {
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        showMemberDetails();
    }

    private RadioGroup getRadioGroupYesNo() {
        Map<Integer, String> stringMap = new HashMap<>();
        stringMap.put(RADIO_BUTTON_ID_YES, LabelConstants.YES);
        stringMap.put(RADIO_BUTTON_ID_NO, LabelConstants.NO);
        return MyStaticComponents.getRadioGroup(this, stringMap, true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == BUTTON_POSITIVE) {
            alertDialog.dismiss();
            finish();
        } else if (v.getId() == BUTTON_NEGATIVE) {
            alertDialog.dismiss();
        } else if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screen) {
                case MEMBER_DETAILS_SCREEN:
                    if (confirmationRadioGroup.getCheckedRadioButtonId() == -1) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_AN_OPTION));
                    } else if (confirmationRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                        SharedPreferences.Editor edit = sharedPref.edit();
                        Gson gson = new Gson();

                        if (selectedNotification != null) {
                            NotificationBean notificationBean = new NotificationBean();
                            notificationBean.setId(selectedNotification.getId().intValue());
                            String json = gson.toJson(notificationBean);
                            edit.putString(GlobalTypes.NOTIFICATION_BEAN, json);
                        }

                        String json = gson.toJson(selectedMember);
                        edit.putString("memberBean", json);
                        json = gson.toJson(selectedFamily);
                        edit.putString("familyDataBean", json);
                        edit.apply();
                        Intent myIntent = new Intent(this, MigrateOutActivity_.class);
                        startActivityForResult(myIntent, ActivityConstants.MEMBER_MIGRATION_OUT_ACTIVITY_REQUEST_CODE);
                    } else if (confirmationRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
                        setRejectConfirmationScreen();
                    }
                    break;

                case REJECT_CONFIRMATION_SCREEN:
                    if (rejectConfirmationRadioGroup.getCheckedRadioButtonId() == -1) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_AN_OPTION));
                    } else if (rejectConfirmationRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                        setFinalScreen();
                    } else if (rejectConfirmationRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
                        showMemberDetails();
                    }
                    break;

                case FINAL_SCREEN:
                    storeAshaEventRejectionForm();
                    break;
                default:
            }
        }
    }

    private void showMemberDetails() {
        showProcessDialog();
        screen = MEMBER_DETAILS_SCREEN;
        SimpleDateFormat sdf = new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault());

        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context, LabelConstants.MEMBER_DETAILS));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.MEMBER_ID));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, selectedMember.getUniqueHealthId()));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.FAMILY_ID));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, selectedMember.getFamilyId()));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.NAME));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getMemberFullName(selectedMember)));

        if (selectedMember.getDob() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.DATE_OF_BIRTH));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, sdf.format(selectedMember.getDob())));

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.AGE));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getAgeDisplayOnGivenDate(selectedMember.getDob(), new Date())));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.CONFORMATION_TO_MEMBER_MIGRATION));

        if (confirmationRadioGroup == null) {
            confirmationRadioGroup = getRadioGroupYesNo();
        }
        bodyLayoutContainer.addView(confirmationRadioGroup);
        hideProcessDialog();
    }

    private void setRejectConfirmationScreen() {
        screen = REJECT_CONFIRMATION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.CONFORMATION_TO_MEMBER_MIGRATION_REJECTION));
        if (rejectConfirmationRadioGroup == null) {
            rejectConfirmationRadioGroup = getRadioGroupYesNo();
        }
        bodyLayoutContainer.addView(rejectConfirmationRadioGroup);
    }

    private void setFinalScreen() {
        screen = FINAL_SCREEN;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context, LabelConstants.FORM_ENTRY_COMPLETED));
        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context, LabelConstants.FORM_ENTRY_COMPLETED_SUCCESSFULLY));
        nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_SUBMIT));
    }

    private void storeAshaEventRejectionForm() {
        AshaEventRejectionDataBean rejectionDataBean = new AshaEventRejectionDataBean();
        rejectionDataBean.setNotificationId(selectedNotification.getId());
        rejectionDataBean.setMemberId(selectedNotification.getMemberId());
        rejectionDataBean.setFamilyId(selectedNotification.getFamilyId());
        rejectionDataBean.setEventType(selectedNotification.getTask());
        rejectionDataBean.setRejectedOn(new Date().getTime());

        //Preparing Checksum
        StringBuilder checkSum = new StringBuilder(SewaTransformer.loginBean.getUsername());
        checkSum.append(Calendar.getInstance().getTimeInMillis());

        StoreAnswerBean storeAnswerBean = new StoreAnswerBean();
        storeAnswerBean.setToken(SewaTransformer.loginBean.getUserToken());
        storeAnswerBean.setUserId(SewaTransformer.loginBean.getUserID());
        storeAnswerBean.setChecksum(checkSum.toString());
        storeAnswerBean.setDateOfMobile(Calendar.getInstance().getTimeInMillis());
        storeAnswerBean.setFormFilledUpTime(0L);
        storeAnswerBean.setMorbidityAnswer("-1");
        storeAnswerBean.setNotificationId(-1L);
        storeAnswerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO);
        storeAnswerBean.setRelatedInstance("-1");
        storeAnswerBean.setAnswerEntity(FormConstants.FHW_REPORTED_EVENT_REJECTION);

        LoggerBean loggerBean = new LoggerBean();
        loggerBean.setCheckSum(checkSum.toString());
        loggerBean.setDate(Calendar.getInstance().getTimeInMillis());
        loggerBean.setStatus(GlobalTypes.STATUS_PENDING);
        loggerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO.trim());
        loggerBean.setNoOfAttempt(0);
        loggerBean.setBeneficiaryName(selectedMember.getUniqueHealthId() + " - " + UtilBean.getMemberFullName(selectedMember));
        loggerBean.setTaskName(UtilBean.getMyLabel(LabelConstants.MEMBER_MIGRATION_REJECTION));
        loggerBean.setFormType(FormConstants.FHW_REPORTED_EVENT_REJECTION);

        storeAnswerBean.setAnswer(new Gson().toJson(rejectionDataBean));
        sewaService.createStoreAnswerBean(storeAnswerBean);
        sewaService.createLoggerBean(loggerBean);

        sewaService.deleteNotificationByNotificationId(selectedNotification.getId());
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (screen == null || screen.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            switch (screen) {
                case MEMBER_DETAILS_SCREEN:
                    finish();
                    break;

                case REJECT_CONFIRMATION_SCREEN:
                    showMemberDetails();
                    break;

                case FINAL_SCREEN:
                    setRejectConfirmationScreen();
                    break;
                default:
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityConstants.MEMBER_MIGRATION_OUT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            finish();
        }
    }
}
