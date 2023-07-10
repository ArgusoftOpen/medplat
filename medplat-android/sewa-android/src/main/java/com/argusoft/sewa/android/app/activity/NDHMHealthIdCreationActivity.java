package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.listeners.DateChangeListenerStatic;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.FormulaConstants;
import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.core.impl.SewaServiceRestClientImpl;
import com.argusoft.sewa.android.app.databean.HealthIdAccountProfileResponse;
import com.argusoft.sewa.android.app.databean.HealthIdAccountProfileResponseForNonGov;
import com.argusoft.sewa.android.app.databean.OfflineHealthIdBean;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.VersionBean;
import com.argusoft.sewa.android.app.restclient.impl.ApiManager;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.argusoft.sewa.android.app.util.WSConstants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;

@EActivity
public class NDHMHealthIdCreationActivity extends MenuActivity implements View.OnClickListener {

    private static final String TAG = "NDHMHealthIdCreation";
    private static final String OPTION_SELECTION_FOR_HEALTH_ID_CREATION_SCREEN = "optionSelectionForHealthIdCreationScreen";
    private static final String CREATE_HEALTH_ID_VIA_AADHAAR_OTP_SCREEN = "createHealthIdViaAadhaarOTPScreen";
    private static final String CREATE_HEALTH_ID_VIA_AADHAAR_FINGERPRINT_SCREEN = "createHealthIdViaAadhaarFingerPrintScreen";
    private static final String CREATE_HEALTH_ID_VIA_AADHAAR_DEMOGRAPHY_SCREEN = "createHealthIdViaAadhaarDemographyScreen";
    private static final String OTP_VERIFICATION_SCREEN = "otpVerificationScreen";
    private static final String DISPLAY_HEALTH_ID_SCREEN = "displayHealthIdScreen";
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private static final int NDHM_HEALTH_ID_MANAGEMENT_REQUEST_CODE = 1000;
    private static final int FINGER_PRINT_REQUEST_CODE = 1001;
    private static final int NDHM_HEALTH_ID_SKIP_REQUEST_CODE = 1002;

    public static final String NETWORK_FAILURE = "Network Unavailable";

    @Bean
    SewaServiceRestClientImpl restClient;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<VersionBean, Integer> versionBeanDao;

    @Bean
    ApiManager apiManager;

    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private LinearLayout footerLayout;
    private MyAlertDialog myAlertDialog;

    private TextInputLayout aadhaarTB;
    private TextInputLayout mobileNumberTB;
    private TextInputLayout nameTB;
    private RadioGroup genderRG;
    private LinearLayout dobDP;
    private DateChangeListenerStatic dobChangeListener;
    private LinearLayout otpLayout;
    private TextInputLayout otpTB;
    private TextInputLayout mobileOtpTB;
    private MaterialButton verificationBtn;
    private MaterialTextView timer;
    private MaterialTextView resendBtn;
    private MaterialTextView cancelAction;
    private MaterialTextView skipBtn;
    private String featureName;

    private final Timer[] timerCounter = new Timer[1];
    private final int[] counter = {30, 0};

    private Integer memberId;
    private String screen;
    private String txnId;
    public String abhaConsentCheckboxModel;
    private Boolean isNonGov = false;

    private boolean isAudioRecording = false;
    private MediaRecorder recorder;
    private String fileName = "";
    private String healthIdNumber;
    private String healthId;

    MaterialCheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            isNonGov = getIsNonGov();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (globalPanel != null) {
            setContentView(globalPanel);
        }
        if (!SharedStructureData.isLogin) {
            Intent intent = new Intent(context, LoginActivity_.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        memberId = this.getIntent().getIntExtra(RelatedPropertyNameConstants.MEMBER_ID, -1);
        featureName = this.getIntent().getStringExtra("featureName");
        abhaConsentCheckboxModel = getIntent().getStringExtra(RelatedPropertyNameConstants.ABHA_CONSENT_CHECKBOX_MODEL);
        setTitle(UtilBean.getTitleText(LabelConstants.CREATE_HEALTH_ID));
    }

    private void initView() {
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(context, this);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        footerLayout = globalPanel.findViewById(DynamicUtils.ID_FOOTER);
        setOptionSelectionForHealthIdCreationScreenV2();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screen) {
                case CREATE_HEALTH_ID_VIA_AADHAAR_OTP_SCREEN:
                case CREATE_HEALTH_ID_VIA_AADHAAR_FINGERPRINT_SCREEN:
                    if (aadhaarValidation() && mobileNumberValidation()) {
                        checkNetworkConnection();
                    }
                    break;
                default:
            }
        }
    }

    private void alertToAskForConcern(final int position) {
        if (position > 1) {
            return;
        }
        String message = LabelConstants.READY_FOR_GIVE_CONCERN_TO_SHARE_AADHAAR_DETAILS_FOR_CREATE_HEALTH_ID;

        View.OnClickListener myListener = v -> {
            myAlertDialog.dismiss();
            if (position == 0) {
                setComponentForCreateHealthIdViaAadhaarOTP();
            } else if (position == 1) {
                setComponentForCreateHealthIdViaAadhaarDemography();
            }
        };
        myAlertDialog = new MyAlertDialog(context,
                UtilBean.getMyLabel(message),
                myListener, DynamicUtils.BUTTON_OK);
        myAlertDialog.show();
        myAlertDialog.setOnCancelListener(dialog -> myAlertDialog.dismiss());
    }

    private void showPopUpWindow(View view) {
        final View inflate = LayoutInflater.from(this).inflate(R.layout.ndhm_record_consent_popup_window, null);
        boolean focusable = true;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        final PopupWindow popupWindow = new PopupWindow(inflate, displayMetrics.widthPixels * 8 / 10, ViewGroup.LayoutParams.MATCH_PARENT, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button addBookmark = inflate.findViewById(R.id.popup_button);
        final ImageButton micButton = inflate.findViewById(R.id.mic_button);
        addBookmark.setOnClickListener(v -> {
            if (isAudioRecording)
                micButton.performClick();
            popupWindow.dismiss();
            Intent intent = new Intent(context, NDHMOfflineHealthIdCreationActivity_.class);
            intent.putExtra(RelatedPropertyNameConstants.MEMBER_ID, memberId);
            intent.putExtra(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY, getIntent().getStringExtra(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY));
            intent.putExtra(RelatedPropertyNameConstants.AADHAR_SCANNED, getIntent().getStringExtra(RelatedPropertyNameConstants.AADHAR_SCANNED));
            intent.putExtra(RelatedPropertyNameConstants.GENDER, getIntent().getStringExtra(RelatedPropertyNameConstants.GENDER));
            intent.putExtra(RelatedPropertyNameConstants.MOBILE_NUMBER, getIntent().getStringExtra(RelatedPropertyNameConstants.MOBILE_NUMBER));
            intent.putExtra(RelatedPropertyNameConstants.DOB, getIntent().getStringExtra(RelatedPropertyNameConstants.DOB));
            intent.putExtra(RelatedPropertyNameConstants.ABHA_CONSENT_CHECKBOX_MODEL, abhaConsentCheckboxModel);
            startActivityForResult(intent, NDHM_HEALTH_ID_MANAGEMENT_REQUEST_CODE);
        });
        micButton.setOnClickListener(v -> {
            if (!isAudioRecording) {
                try {
                    isAudioRecording = true;
                    if (recorder == null) {
                        recorder = new MediaRecorder();
                    }
                    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.getDefault()).format(new Date());
                    fileName = "Record_ABHA_CONSENT_" + timeStamp + ".amr";
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    recorder.setOutputFile(SewaConstants.DIR_AUDIO + fileName);
                    recorder.prepare();
                    recorder.start();
                    micButton.setImageResource(R.drawable.stop_audio);
                    SewaUtil.generateToast(this, "Recording started");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                addBookmark.setText(R.string.next);
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder = null;
                micButton.setImageResource(R.drawable.record_audio);
                SewaUtil.generateToast(this, "Audio Recorded successfully");
                isAudioRecording = false;
            }
        });
    }

    @UiThread
    public void setOptionSelectionForHealthIdCreationScreenV2() {
        screen = OPTION_SELECTION_FOR_HEALTH_ID_CREATION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        footerLayout.setVisibility(View.GONE);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(
                null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT_METHOD_FOR_ABHA_CREATION)));
        List<String> options = new ArrayList<>();

        options.add(UtilBean.getMyLabel(LabelConstants.AADHAAR_OTP_AUTHENTICATION));
        options.add(UtilBean.getMyLabel(LabelConstants.AADHAAR_FINGERPRINT_AUTHENTICATION));
        if(!isNonGov)
            options.add(UtilBean.getMyLabel(LabelConstants.AADHAAR_DEMOGRAPHIC_AUTHENTICATION));

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if(SharedStructureData.sewaService.isOnline()) {
                        setComponentForCreateHealthIdViaAadhaarOTP();
                    } else {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.NETWORK_CONNECTIVITY_ALERT));
                        setOptionSelectionForHealthIdCreationScreenV2();
                    }
                } else if (position == 1) {
                    if(SharedStructureData.sewaService.isOnline()) {
                        setComponentForCreateHealthIdViaFingerprint();
                    }else{SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.NETWORK_CONNECTIVITY_ALERT));
                        setOptionSelectionForHealthIdCreationScreenV2();
                    }
                } else if (position == 2) {
                    showPopUpWindow(view);
                }
            }
        };

        ListView buttonList = MyStaticComponents.getButtonList(context, options, onItemClickListener);
        bodyLayoutContainer.addView(buttonList);
    }

    private void setComponentForCreateHealthIdViaFingerprint() {
        aadhaarTB = MyStaticComponents.getEditText(context, LabelConstants.AADHAAR_NUMBER, -1, 12, InputType.TYPE_CLASS_NUMBER);
        mobileNumberTB = MyStaticComponents.getEditText(context, LabelConstants.MOBILE_NUMBER, -1, 10, InputType.TYPE_CLASS_PHONE);
        setCreateHealthIdViaFingerprintScreen();
    }

    @UiThread
    public void setCreateHealthIdViaFingerprintScreen() {
        screen = CREATE_HEALTH_ID_VIA_AADHAAR_FINGERPRINT_SCREEN;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateTitleView(context, UtilBean.getMyLabel(LabelConstants.CREATE_HEALTH_ID)));
        MaterialTextView subtitle = MyStaticComponents.generateSubTitleView(context,
                UtilBean.getMyLabel(String.format("(%s)", UtilBean.getMyLabel(LabelConstants.AADHAAR_FINGERPRINT_AUTHENTICATION))));
        subtitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        bodyLayoutContainer.addView(subtitle);

        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context,
                UtilBean.getMyLabel(LabelConstants.PLEASE_ENTER_ALL_DETAILS_AS_GIVEN_IN_THE_AADHAAR_CARD)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.AADHAAR_NUMBER)));
        bodyLayoutContainer.addView(aadhaarTB);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionViewWithTooltip(null, UtilBean.getMyLabel(LabelConstants.MOBILE_NUMBER_HINT_FOR_ABHA),
                context, UtilBean.getMyLabel(LabelConstants.MOBILE_NUMBER)));
        bodyLayoutContainer.addView(mobileNumberTB);

        LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewLayoutParams.setMargins(0, 40, 0, 0);
        checkBox = MyStaticComponents.getCheckBox(context, UtilBean.getMyLabel(LabelConstants.ABHA_OFFLINE_CONSENT), -1, false);
        checkBox.setGravity(Gravity.TOP);
        checkBox.setLayoutParams(textViewLayoutParams);
        TextViewCompat.setTextAppearance(checkBox, R.style.CustomSubtitleView);
        bodyLayoutContainer.addView(checkBox);

        footerLayout.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.GONE);
        nextButton.setText(UtilBean.getMyLabel(LabelConstants.REGISTER));
        nextButton.setOnClickListener(this);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Boolean.TRUE.equals(isChecked)) {
                nextButton.setVisibility(View.VISIBLE);
            } else {
                nextButton.setVisibility(View.GONE);
            }
        });
    }

    @UiThread
    public void checkNetworkConnection() {
        if (SharedStructureData.sewaService.isOnline()) {
            switch (screen) {
                case CREATE_HEALTH_ID_VIA_AADHAAR_OTP_SCREEN:
                    showProcessDialog();
                    generateOTP();
                    break;

                case CREATE_HEALTH_ID_VIA_AADHAAR_FINGERPRINT_SCREEN:
                    Intent intent = new Intent(context, FingerPrintScannerActivity_.class);
                    intent.putExtra("aadhaar", aadhaarTB.getEditText().getText().toString());
                    intent.putExtra("mobile", mobileNumberTB.getEditText().getText().toString());
                    intent.putExtra("memberId", memberId);
                    intent.putExtra("featureName", featureName);
                    intent.putExtra(RelatedPropertyNameConstants.ABHA_CONSENT_CHECKBOX_MODEL, abhaConsentCheckboxModel);
                    intent.putExtra("isNonGov", isNonGov);
                    intent.putExtra(RelatedPropertyNameConstants.AADHAAR_MATCH_WITH_BENEFICIARY_CONSENT, checkBox.isChecked());
                    startActivityForResult(intent, FINGER_PRINT_REQUEST_CODE);
                    break;
                default:
            }
        } else {
            SewaUtil.generateToast(context, UtilBean.getMyLabel(NETWORK_FAILURE));
        }
    }

    @UiThread
    public void setOptionSelectionForHealthIdCreationScreen() {
        screen = OPTION_SELECTION_FOR_HEALTH_ID_CREATION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        footerLayout.setVisibility(View.GONE);

        bodyLayoutContainer.addView(MyStaticComponents.generateTitleView(context, UtilBean.getMyLabel(LabelConstants.CREATE_HEALTH_ID)));
        MaterialTextView subtitle = MyStaticComponents.generateSubTitleView(context, UtilBean.getMyLabel("(Aadhaar based authentication)"));
        subtitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        bodyLayoutContainer.addView(subtitle);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(
                null, null, context, UtilBean.getMyLabel("Select method for health id creation")));
        List<String> options = new ArrayList<>();

        options.add(UtilBean.getMyLabel(String.format("Via %s", LabelConstants.AADHAAR_OTP_AUTHENTICATION)));
        options.add(UtilBean.getMyLabel(String.format("Via %s", LabelConstants.AADHAAR_DEMOGRAPHIC_AUTHENTICATION)));

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> alertToAskForConcern(position);

        ListView buttonList = MyStaticComponents.getButtonList(context, options, onItemClickListener);
        bodyLayoutContainer.addView(buttonList);
    }

    private void setComponentForCreateHealthIdViaAadhaarOTP() {
        aadhaarTB = MyStaticComponents.getEditText(context, LabelConstants.AADHAR_NUMBER, -1, 12, InputType.TYPE_CLASS_NUMBER);
        mobileNumberTB = MyStaticComponents.getEditText(context, LabelConstants.MOBILE_NUMBER, -1, 10, InputType.TYPE_CLASS_PHONE);
        setCreateHealthIdViaAadhaarOTPScreen();
    }

    @UiThread
    public void setCreateHealthIdViaAadhaarOTPScreen() {
        screen = CREATE_HEALTH_ID_VIA_AADHAAR_OTP_SCREEN;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateTitleView(context, UtilBean.getMyLabel(LabelConstants.CREATE_HEALTH_ID)));
        MaterialTextView subtitle = MyStaticComponents.generateSubTitleView(context,
                UtilBean.getMyLabel(String.format("(%s)", UtilBean.getMyLabel(LabelConstants.AADHAAR_OTP_AUTHENTICATION))));
        subtitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        bodyLayoutContainer.addView(subtitle);

        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context,
                UtilBean.getMyLabel(LabelConstants.AN_OTP_WILL_BE_SENT_TO_THE_MOBILE_PHONE_NUMBER_LINKED_TO_THIS_AADHAAR_NUMBER)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.AADHAR_NUMBER)));
        bodyLayoutContainer.addView(aadhaarTB);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionViewWithTooltip(null, UtilBean.getMyLabel(LabelConstants.MOBILE_NUMBER_HINT_FOR_ABHA),
                context, UtilBean.getMyLabel(LabelConstants.MOBILE_NUMBER)));
        bodyLayoutContainer.addView(mobileNumberTB);

        LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewLayoutParams.setMargins(0, 40, 0, 0);
        checkBox = MyStaticComponents.getCheckBox(context, UtilBean.getMyLabel(LabelConstants.ABHA_OFFLINE_CONSENT), -1, false);
        checkBox.setGravity(Gravity.TOP);
        checkBox.setLayoutParams(textViewLayoutParams);
        TextViewCompat.setTextAppearance(checkBox, R.style.CustomSubtitleView);
        bodyLayoutContainer.addView(checkBox);

        footerLayout.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.GONE);
        nextButton.setText(UtilBean.getMyLabel(LabelConstants.SEND_OTP));

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Boolean.TRUE.equals(isChecked)) {
                nextButton.setVisibility(View.VISIBLE);
            } else {
                nextButton.setVisibility(View.GONE);
            }
        });
    }

    private void setComponentForCreateHealthIdViaAadhaarDemography() {
        aadhaarTB = MyStaticComponents.getEditText(context, LabelConstants.AADHAR_NUMBER, -1, 12, InputType.TYPE_CLASS_NUMBER);
        mobileNumberTB = MyStaticComponents.getEditText(context, LabelConstants.MOBILE_NUMBER, -1, 10, InputType.TYPE_CLASS_PHONE);
        nameTB = MyStaticComponents.getEditText(context, LabelConstants.NAME, -1, 99, InputType.TYPE_CLASS_TEXT);

        genderRG = new RadioGroup(context);
        genderRG.addView(MyStaticComponents.getRadioButton(context, UtilBean.getMyLabel(LabelConstants.MALE), IdConstants.RADIO_BUTTON_ID_YES));
        genderRG.addView(MyStaticComponents.getRadioButton(context, UtilBean.getMyLabel(LabelConstants.FEMALE), IdConstants.RADIO_BUTTON_ID_NO));
        genderRG.addView(MyStaticComponents.getRadioButton(context, UtilBean.getMyLabel(LabelConstants.OTHER), IdConstants.RADIO_BUTTON_ID_UNKNOWN));

        List<ValidationTagBean> list = new ArrayList<>();
        list.add(new ValidationTagBean(FormulaConstants.VALIDATION_IS_FUTURE_DATE, UtilBean.getMyLabel("Date of birth is can’t be in future")));
        list.add(new ValidationTagBean("isDateIn-Sub-110-0-0", UtilBean.getMyLabel("Date of birth can’t be before 110 years")));

        dobChangeListener = new DateChangeListenerStatic(context, list);
        dobDP = MyStaticComponents.getCustomDatePickerForStatic(context, dobChangeListener, -1);
        setCreateHealthIdViaAadhaarDemographyScreen();
    }

    @UiThread
    public void setCreateHealthIdViaAadhaarDemographyScreen() {
        screen = CREATE_HEALTH_ID_VIA_AADHAAR_DEMOGRAPHY_SCREEN;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateTitleView(context, UtilBean.getMyLabel(LabelConstants.CREATE_HEALTH_ID)));
        MaterialTextView subtitle = MyStaticComponents.generateSubTitleView(context,
                UtilBean.getMyLabel(String.format("(%s)", LabelConstants.AADHAAR_DEMOGRAPHIC_AUTHENTICATION)));
        subtitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        bodyLayoutContainer.addView(subtitle);

        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context,
                UtilBean.getMyLabel(LabelConstants.PLEASE_ENTER_ALL_DETAILS_AS_GIVEN_IN_THE_AADHAAR_CARD)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.AADHAR_NUMBER)));
        bodyLayoutContainer.addView(aadhaarTB);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.BIRTH_DATE)));
        bodyLayoutContainer.addView(dobDP);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.GENDER)));
        bodyLayoutContainer.addView(genderRG);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.MOBILE_NUMBER)));
        bodyLayoutContainer.addView(mobileNumberTB);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.NAME)));
        bodyLayoutContainer.addView(nameTB);

        footerLayout.setVisibility(View.VISIBLE);
        nextButton.setText(UtilBean.getMyLabel(LabelConstants.REGISTER));
        nextButton.setOnClickListener(this);
    }

    @Background
    public void generateOTP() {
        final String[] message = {"Issue for sending otp"};
        try {
            Map<String, String> result = restClient.generateAadhaarOTP(aadhaarTB.getEditText().getText().toString().trim(), isNonGov, memberId);
            if (result != null) {
                txnId = result.get("txnId");
                if (isNonGov)
                    setOTPVerificationScreenForNonGov(mobileNumberTB.getEditText().getText().toString().trim());
                else
                    setOTPVerificationScreen(result.get("mobileNumber"));
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            message[0] = e.getMessage();
        }
        runOnUiThread(() -> {
            hideProcessDialog();
            SewaUtil.generateToast(context, message[0]);
        });
    }

//    @Background
    public boolean getIsNonGov() throws SQLException {
        /*try {
            isNonGov = restClient.getIsNonGov();
            System.out.println(isNonGov);
        }catch (Exception e){
            System.out.println(e);
        }*/
        List<VersionBean> versionBeans = versionBeanDao.queryForEq(FieldNameConstants.KEY, "IS_NON_GOV_VAR");
        if (versionBeans != null && !versionBeans.isEmpty()) {
            return Boolean.parseBoolean(versionBeans.get(0).getValue());
        }
        return false;
    }

    @Background
    public void resendOTP() {
        final String[] message = {"Issue for sending otp again"};
        try {
            Map<String, String> result = restClient.generateAadhaarOTP(aadhaarTB.getEditText().getText().toString().trim(), isNonGov, memberId);
            if (result != null) {
                txnId = result.get("txnId");
                runOnUiThread(() -> {
                    timerCounter[0] = new Timer();
                    startTimer();
                });
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            message[0] = e.getMessage();
        }
        runOnUiThread(() -> {
            hideProcessDialog();
            SewaUtil.generateToast(context, message[0]);
        });
    }

    @Background
    public void resendOtpNonGov(String mobileNumber) {
        final String[] message = {"Issue for sending otp again"};
        try {
            Map<String, String> result = restClient.resendOtpNonGov(txnId, mobileNumber);
            if (result != null) {
                txnId = result.get("txnId");
                runOnUiThread(() -> {
                    timerCounter[0] = new Timer();
                    startTimer();
                });
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            message[0] = e.getMessage();
        }
        runOnUiThread(() -> {
            hideProcessDialog();
            SewaUtil.generateToast(context, message[0]);
        });
    }

    @Background
    public void resendMobileOtpNonGov(String mobileNumber) {
        final String[] message = {"Issue for sending otp again"};
        try {
            Map<String, String> result = restClient.resendMobileOtpNonGov(txnId, mobileNumber);
            if (result != null) {
                txnId = result.get("txnId");
                runOnUiThread(() -> {
                    timerCounter[0] = new Timer();
                    startTimer();
                });
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            message[0] = e.getMessage();
        }
        runOnUiThread(() -> {
            hideProcessDialog();
            SewaUtil.generateToast(context, message[0]);
        });
    }

    @UiThread
    public void setOTPVerificationScreen(String mobileNumber) {
        screen = OTP_VERIFICATION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        footerLayout.setVisibility(View.GONE);

        String label = UtilBean.getMyLabel(LabelConstants.PLEASE_ENTER_THE_OTP_RECEIVED_ON_THE_AADHAAR_LINKED_MOBILE_NUMBER);
        if (mobileNumber != null && !isNonGov) {
            label = label + " " + mobileNumber;
        }
        otpLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.aadhaar_otp_validation_layout, null);
        ((MaterialTextView) otpLayout.findViewById(R.id.description))
                .setText(label);

        otpTB = otpLayout.findViewById(R.id.otpText);
        if (otpTB.getEditText() != null) {
            otpTB.getEditText().setHint(LabelConstants.ENTER_OTP);
            otpTB.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        verificationBtn = otpLayout.findViewById(R.id.verifyOtpBtn);
        verificationBtn.setText(UtilBean.getMyLabel(LabelConstants.VALIDATE));
        verificationBtn.setEnabled(false);

        timer = otpLayout.findViewById(R.id.timer);
        resendBtn = otpLayout.findViewById(R.id.resend);
        skipBtn = ((MaterialTextView) otpLayout.findViewById(R.id.skip));
        resendBtn.setEnabled(false);
        skipBtn.setEnabled(false);

        otpTB.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                verificationBtn.setEnabled(Pattern.compile("^\\d{6}$").matcher(s).matches());
            }
        });

        verificationBtn.setOnClickListener(v -> {
            showProcessDialog();
            generateHealthIdUsingAadhaarOTP();
        });

        resendBtn.setOnClickListener(v -> {
            showProcessDialog();
            resendOTP();

        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NDHMOfflineHealthIdCreationActivity_.class);
                intent.putExtra(RelatedPropertyNameConstants.MEMBER_ID, memberId);
                intent.putExtra(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY, getIntent().getStringExtra(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY));
                intent.putExtra(RelatedPropertyNameConstants.AADHAR_SCANNED, getIntent().getStringExtra(RelatedPropertyNameConstants.AADHAR_SCANNED));
                intent.putExtra(RelatedPropertyNameConstants.GENDER, getIntent().getStringExtra(RelatedPropertyNameConstants.GENDER));
                intent.putExtra(RelatedPropertyNameConstants.MOBILE_NUMBER, mobileNumberTB.getEditText().getText().toString().trim());
                intent.putExtra(RelatedPropertyNameConstants.DOB, getIntent().getStringExtra(RelatedPropertyNameConstants.DOB));
                intent.putExtra(RelatedPropertyNameConstants.AADHAR_NUMBER, aadhaarTB.getEditText().getText().toString().trim());
                intent.putExtra(RelatedPropertyNameConstants.ABHA_CONSENT_CHECKBOX_MODEL, abhaConsentCheckboxModel);
                startActivityForResult(intent, NDHM_HEALTH_ID_SKIP_REQUEST_CODE);
            }
        });

        bodyLayoutContainer.addView(otpLayout);
        counter[1] = 0;
        startTimer();
    }

    @UiThread
    public void setOTPVerificationScreenForNonGov(String mobileNumber) {
        screen = OTP_VERIFICATION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        footerLayout.setVisibility(View.GONE);

        String label = UtilBean.getMyLabel(LabelConstants.PLEASE_ENTER_THE_OTP_RECEIVED_ON_THE_AADHAAR_LINKED_MOBILE_NUMBER);
        otpLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.aadhaar_otp_validation_without_skipotp_layout, null);
        ((MaterialTextView) otpLayout.findViewById(R.id.description))
                .setText(label);

        otpTB = otpLayout.findViewById(R.id.otpText);
        if (otpTB.getEditText() != null) {
            otpTB.getEditText().setHint(LabelConstants.ENTER_OTP);
            otpTB.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        verificationBtn = otpLayout.findViewById(R.id.verifyOtpBtn);
        verificationBtn.setText(UtilBean.getMyLabel(LabelConstants.VALIDATE));
        verificationBtn.setEnabled(false);

        timer = otpLayout.findViewById(R.id.timer);
        resendBtn = otpLayout.findViewById(R.id.resend);
        resendBtn.setEnabled(false);

        otpTB.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                verificationBtn.setEnabled(Pattern.compile("^\\d{6}$").matcher(s).matches());
            }
        });

        verificationBtn.setOnClickListener(v -> {
            showProcessDialog();
            verifyAadhaarOTPForNonGov();
        });

        resendBtn.setOnClickListener(v -> {
            showProcessDialog();
            resendOtpNonGov(mobileNumber);
        });

        bodyLayoutContainer.addView(otpLayout);
        counter[1] = 0;
        startTimer();
    }

    @UiThread
    public void setOTPVerificationForMobileScreen(String mobileNumber) {
        screen = OTP_VERIFICATION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        footerLayout.setVisibility(View.GONE);

        String label = UtilBean.getMyLabel(LabelConstants.PLEASE_ENTER_THE_OTP_RECEIVED_ON_THE_MOBILE_NUMBER);
        if (mobileNumber != null) {
            label = label + " " + mobileNumber;
        }
        otpLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.aadhaar_otp_validation_without_skipotp_layout, null);
        ((MaterialTextView) otpLayout.findViewById(R.id.description))
                .setText(label);

        mobileOtpTB = otpLayout.findViewById(R.id.otpText);
        if (mobileOtpTB.getEditText() != null) {
            mobileOtpTB.getEditText().setHint(LabelConstants.ENTER_OTP);
            mobileOtpTB.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        verificationBtn = otpLayout.findViewById(R.id.verifyOtpBtn);
        verificationBtn.setText(UtilBean.getMyLabel(LabelConstants.VALIDATE));
        verificationBtn.setEnabled(false);

        timer = otpLayout.findViewById(R.id.timer);
        resendBtn = otpLayout.findViewById(R.id.resend);
        resendBtn.setEnabled(false);

        mobileOtpTB.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                verificationBtn.setEnabled(Pattern.compile("^\\d{6}$").matcher(s).matches());
            }
        });

        verificationBtn.setOnClickListener(v -> {
            showProcessDialog();
            verifyMobileOTPForNonGov();
        });

        resendBtn.setOnClickListener(v -> {
            showProcessDialog();
            resendMobileOtpNonGov(mobileNumber);
        });

        bodyLayoutContainer.addView(otpLayout);
        counter[1] = 0;
        startTimer();
    }

    private void startTimer() {
        counter[0] = 30;
        counter[1]++;
        timer.setVisibility(View.VISIBLE);
        resendBtn.setEnabled(false);
        if(!isNonGov) {
            skipBtn.setEnabled(false);
        }
        if (timerCounter[0] != null) {
            timerCounter[0].cancel();
        }
        timerCounter[0] = new Timer();
        timerCounter[0].schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        if (counter[0] > 0) {
                            runOnUiThread(() -> {
                                timer.setText(String.format(Locale.getDefault(), "in %ds", counter[0]));
                                counter[0]--;
                            });
                        } else {
                            runOnUiThread(() -> {
                                timer.setVisibility(View.GONE);
                                resendBtn.setEnabled(true);
                                resendBtn.setTextColor(Color.parseColor("#245eae"));
                                if(!isNonGov) {
                                    skipBtn.setEnabled(true);
                                    skipBtn.setTextColor(Color.parseColor("#245eae"));
                                }
                            });
                            this.cancel();
                        }
                    }
                },
                50, 1500
        );
        /*if (counter[1] > 2) {
            setOTPVerificationCancel();
        }*/
        hideProcessDialog();
    }

    private void setOTPVerificationCancel() {
        cancelAction = otpLayout.findViewById(R.id.cancel_action);
        cancelAction.setVisibility(View.VISIBLE);
        cancelAction.setText(UtilBean.getMyLabel("OTP not received? Tap here to go back and try with demographics option or try again later"));
        cancelAction.setOnClickListener(v -> {
            if (timerCounter[0] != null) {
                timerCounter[0].cancel();
            }
            setOptionSelectionForHealthIdCreationScreenV2();
        });
    }

    @Background
    public void verifyAadhaarOTPForNonGov() {
        final String[] message = {"Invalid OTP"};
        try {
            Map<String, String> result = restClient.verifyAadhaarOTPForNonGov(otpTB.getEditText().getText().toString(), txnId);
            if (result != null) {
                txnId = result.get("txnId");
                Map<String, String> isLinked = restClient.aadhaarLinkedMobileOtpForNonGov(txnId, mobileNumberTB.getEditText().getText().toString());

                if (isLinked.get("mobileLinked").compareTo("false")==0) {
                    runOnUiThread(() -> {
                        if (timerCounter[0] != null) {
                            timerCounter[0].cancel();
                        }
                        hideProcessDialog();
                        timer.setVisibility(View.GONE);
                        resendBtn.setEnabled(false);
                        verificationBtn.setEnabled(false);
                        otpTB.setEnabled(false);
                    });
                    setOTPVerificationForMobileScreen(mobileNumberTB.getEditText().getText().toString());
                }

                if(isLinked.get("mobileLinked").compareTo("true")==0) {
                    getABHAcard("AADHAAR_OTP");
                }
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            message[0] = e.getMessage();
        }
        runOnUiThread(() -> {
            hideProcessDialog();
            SewaUtil.generateToast(context, message[0]);
        });
    }

    @Background
    public void getABHAcard(String authMethodType){
        final String[] message = {"Invalid OTP"};
        try{
            HealthIdAccountProfileResponseForNonGov accountProfileResponseForNonGov = restClient.createHealthIdForNonGov(txnId, memberId, mobileNumberTB.getEditText().getText().toString(), featureName, abhaConsentCheckboxModel, checkBox.isChecked(), authMethodType);
            byte[] abhaCard = null;
            if (accountProfileResponseForNonGov != null) {
                healthIdNumber = accountProfileResponseForNonGov.getHealthIdNumber();
                healthId = accountProfileResponseForNonGov.getHealthId();
                try {
                    if (accountProfileResponseForNonGov.getToken() != null && accountProfileResponseForNonGov.getHealthIdNumber() != null) {
                        abhaCard = restClient.saveHealthIdCardV2(accountProfileResponseForNonGov.getToken(), accountProfileResponseForNonGov.getHealthIdNumber());
                    }
                } catch (Exception e) {
                    abhaCard = null;
                }
                if (!isNonGov) {
                    runOnUiThread(() -> {
                        if (timerCounter[0] != null) {
                            timerCounter[0].cancel();
                        }
                        hideProcessDialog();
                        timer.setVisibility(View.GONE);
                        resendBtn.setEnabled(false);
                        verificationBtn.setEnabled(false);
                        otpTB.setEnabled(false);
                    });
                }
                if (abhaCard != null && abhaCard.length > 0) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(abhaCard, 0, abhaCard.length);
                    displayHealthIdScreen(bitmap);
                } else {
                    displayHealthIdScreen(null);
                }

                return;
            } else {
                message[0] = "Couldn't fetch health Id details";
            }
        }catch (Exception e) {
            Log.e(TAG, e.getMessage());
            message[0] = e.getMessage();
        }
        runOnUiThread(() -> {
            hideProcessDialog();
            SewaUtil.generateToast(context, message[0]);
        });
    }

    @Background
    public void verifyMobileOTPForNonGov() {
        final String[] message = {"Invalid OTP"};
        try {
            Map<String, String> result = restClient.verifyMobileOtpForNonGov(txnId, mobileOtpTB.getEditText().getText().toString());
            if (result != null) {
                txnId = result.get("txnId");
                getABHAcard("AADHAAR_OTP");
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            message[0] = e.getMessage();
        }
        runOnUiThread(() -> {
            hideProcessDialog();
            SewaUtil.generateToast(context, message[0]);
        });
    }

    @Background
    public void generateHealthIdUsingAadhaarOTP() {
        final String[] message = {"Issue for creating Health Id"};
        try {
            HealthIdAccountProfileResponse accountProfileResponse = restClient.createUsingAadhaarOTP(memberId, mobileNumberTB.getEditText().getText().toString(),
                    otpTB.getEditText().getText().toString(), txnId, featureName, abhaConsentCheckboxModel, checkBox.isChecked());
            byte[] abhaCard = null;
            if (accountProfileResponse != null) {
                try {
                    if (accountProfileResponse.getJwtResponse() != null && accountProfileResponse.getJwtResponse().getToken() != null && accountProfileResponse.getHealthIdNumber() != null) {
                        abhaCard = restClient.saveHealthIdCardV2(accountProfileResponse.getJwtResponse().getToken(), accountProfileResponse.getHealthIdNumber());
                    } else {
                        abhaCard = restClient.saveHealthIdCardV2(accountProfileResponse.getToken(), accountProfileResponse.getHealthIdNumber());
                    }
                    healthIdNumber = accountProfileResponse.getHealthIdNumber();
                    healthId = accountProfileResponse.getHealthId();
                }catch (Exception e){
                    abhaCard = null;
                }
                runOnUiThread(() -> {
                    if (timerCounter[0] != null) {
                        timerCounter[0].cancel();
                    }
                    timer.setVisibility(View.GONE);
                    resendBtn.setEnabled(false);
                    verificationBtn.setEnabled(false);
                    otpTB.setEnabled(false);
                });
                Bitmap bitmap = BitmapFactory.decodeByteArray(abhaCard, 0, abhaCard.length);
                displayHealthIdScreen(bitmap);
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            message[0] = e.getMessage();
        }
        runOnUiThread(() -> {
            hideProcessDialog();
            SewaUtil.generateToast(context, message[0]);
        });
    }

    @UiThread
    public void displayHealthIdScreen(Bitmap bitmap) {
        bodyLayoutContainer.removeAllViews();
        screen = DISPLAY_HEALTH_ID_SCREEN;

        bodyLayoutContainer.addView(MyStaticComponents.generateTitleView(context, UtilBean.getMyLabel(LabelConstants.HEALTH_ID_GENERATED_SUCCESSFULLY)));
        ImageView imageView = MyStaticComponents.getImageView(context, -1, -1, null);
        imageView.setImageBitmap(bitmap);
        bodyLayoutContainer.addView(imageView);
        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context,
                UtilBean.getMyLabel(LabelConstants.HEALTH_ID_CARD_MESSAGE)));

        footerLayout.setVisibility(View.VISIBLE);
        nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        nextButton.setOnClickListener(v -> sendResponse());

        hideProcessDialog();
    }

    @Background
    public void downloadAbhaCard(Long abhaCardId) {
        try {
            if (abhaCardId != null) {
                String url = WSConstants.REST_TECHO_SERVICE_URL + "getfileById?id=" + abhaCardId;
                ResponseBody responseBody = apiManager.execute(apiManager.getInstance().getFile(url));
                if (responseBody == null)
                    return;
                byte[] bytes = responseBody.bytes();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                displayHealthIdScreen(bitmap);
            }
        } catch (Exception e) {
            android.util.Log.e(getClass().getSimpleName(), null, e);
        }
    }

    private boolean isValidContactNumber(String contactNumber) {
        return Pattern.compile("^[9876]{1}[0-9]{9}$").matcher(contactNumber).matches();
    }

    private boolean aadhaarValidation() {
        if (aadhaarTB.getEditText() == null || aadhaarTB.getEditText().getText() == null
                || aadhaarTB.getEditText().getText().toString().trim().isEmpty()) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel("Please enter aadhaar number"));
            return false;
        } else {
            String message = UtilBean.aadhaarNumber(aadhaarTB.getEditText().getText().toString().trim());
            if (message != null) {
                SewaUtil.generateToast(context, UtilBean.getMyLabel(message));
                return false;
            }
        }
        return true;
    }

    private boolean mobileNumberValidation() {
        if (mobileNumberTB.getEditText() == null || mobileNumberTB.getEditText().getText() == null
                || mobileNumberTB.getEditText().getText().toString().trim().isEmpty()) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel("Please enter mobile number"));
            return false;
        } else if (mobileNumberTB.getEditText().getText().toString().trim().length() < 10) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel("Mobile number must contains 10 digit numbers only"));
            return false;
        } else if (!isValidContactNumber(mobileNumberTB.getEditText().getText().toString().trim())) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel("Please enter valid mobile number"));
            return false;
        }
        return true;
    }

    private boolean genderValidation() {
        if (genderRG == null || genderRG.getCheckedRadioButtonId() == -1) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel("Please enter gender"));
            return false;
        }
        return true;
    }

    private boolean dobValidation() {
        if (dobChangeListener == null || dobChangeListener.getDateSet() == null) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel("Please enter birth date"));
            return false;
        }
        return true;
    }

    private boolean nameValidation() {
        if (nameTB.getEditText() == null || nameTB.getEditText().getText() == null
                || nameTB.getEditText().getText().toString().trim().isEmpty()) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel("Please enter name"));
            return false;
        }
        return true;
    }

    private void sendResponse() {
        Intent intent = new Intent();
        intent.putExtra(RelatedPropertyNameConstants.HEALTH_ID_NUMBER, healthIdNumber);
        intent.putExtra(RelatedPropertyNameConstants.HEALTH_ID, healthId);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (screen == null || screen.isEmpty()) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            switch (screen) {
                case OPTION_SELECTION_FOR_HEALTH_ID_CREATION_SCREEN:
                    setResult(RESULT_CANCELED);
                    finish();
                    break;
                case CREATE_HEALTH_ID_VIA_AADHAAR_OTP_SCREEN:
                case CREATE_HEALTH_ID_VIA_AADHAAR_FINGERPRINT_SCREEN:
                case CREATE_HEALTH_ID_VIA_AADHAAR_DEMOGRAPHY_SCREEN:
                    setResult(RESULT_CANCELED);
                    setOptionSelectionForHealthIdCreationScreenV2();
                    break;
                case DISPLAY_HEALTH_ID_SCREEN:
                    sendResponse();
                    break;
                case OTP_VERIFICATION_SCREEN:
                    if (timerCounter[0] != null) {
                        timerCounter[0].cancel();
                    }
                    setCreateHealthIdViaAadhaarOTPScreen();
                    break;
                default:
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NDHM_HEALTH_ID_MANAGEMENT_REQUEST_CODE) {
            if (data == null && resultCode == RESULT_CANCELED) {
                setOptionSelectionForHealthIdCreationScreenV2();
            } else if (data != null && data.getStringExtra(OfflineHealthIdBean.class.getSimpleName()) != null) {
                Intent intent = new Intent();
                intent.putExtra(RelatedPropertyNameConstants.HEALTH_ID_NUMBER, data.getStringExtra(OfflineHealthIdBean.class.getSimpleName()));
                intent.putExtra("isFromOffline", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        } else if (requestCode == FINGER_PRINT_REQUEST_CODE) {
            if (isNonGov) {
                if (data != null && data.hasExtra("txnId")) {
                    txnId = data.getStringExtra("txnId");
                    getABHAcard("BIOMETRICS");
                }
            } else {
                if (data != null && data.hasExtra(RelatedPropertyNameConstants.HEALTH_ID_NUMBER) && data.hasExtra("token")) {
                    healthIdNumber = data.getStringExtra(RelatedPropertyNameConstants.HEALTH_ID_NUMBER);
                    healthId = data.getStringExtra(RelatedPropertyNameConstants.HEALTH_ID);
                    getAbhaCard(data.getStringExtra("token"), healthIdNumber);
                }
            }
        } else if (requestCode == NDHM_HEALTH_ID_SKIP_REQUEST_CODE) {
            if (data != null && data.getStringExtra(OfflineHealthIdBean.class.getSimpleName()) != null) {
                Intent intent = new Intent();
                intent.putExtra(RelatedPropertyNameConstants.HEALTH_ID_NUMBER, data.getStringExtra(OfflineHealthIdBean.class.getSimpleName()));
                intent.putExtra("isFromOffline", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @Background
    public void getAbhaCard(String token, String healthIdNumber) {
        final String[] message = {"Issue for retrieving Health Id"};
        try {
            byte[] bytes = restClient.saveHealthIdCardV2(token, healthIdNumber);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            displayHealthIdScreen(bitmap);
        } catch (Exception e) {
            android.util.Log.e(TAG, e.getMessage());
            message[0] = e.getMessage();
        }
    }

    @Override
    public void onBackPressed() {
        if (screen != null
                && Arrays.asList(CREATE_HEALTH_ID_VIA_AADHAAR_DEMOGRAPHY_SCREEN, OTP_VERIFICATION_SCREEN).contains(screen)) {
            View.OnClickListener myListener = v -> {
                if (v.getId() == BUTTON_POSITIVE) {
                    myAlertDialog.dismiss();
                    setResult(RESULT_CANCELED);
                    setOptionSelectionForHealthIdCreationScreenV2();
                } else {
                    myAlertDialog.dismiss();
                }
            };

            myAlertDialog = new MyAlertDialog(this,
                    LabelConstants.ARE_YOU_SURE_WANT_TO_CLOSE_HEALTH_ID_CREATION,
                    myListener, DynamicUtils.BUTTON_YES_NO);
            myAlertDialog.show();
        } else if (DISPLAY_HEALTH_ID_SCREEN.equals(screen)) {
            sendResponse();
        } else {
            super.onBackPressed();
        }
    }
}
