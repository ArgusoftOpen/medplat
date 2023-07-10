package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyArrayAdapter;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.listeners.DateChangeListenerStatic;
import com.argusoft.sewa.android.app.constants.FormulaConstants;
import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.NDHMConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.core.impl.SewaServiceRestClientImpl;
import com.argusoft.sewa.android.app.databean.HealthIdSearchResponse;
import com.argusoft.sewa.android.app.databean.LinkBenefitIdResponse;
import com.argusoft.sewa.android.app.databean.OptionDataBean;
import com.argusoft.sewa.android.app.databean.StateResponse;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

@EActivity
public class NDHMHealthIdCaptureActivity extends MenuActivity implements View.OnClickListener {

    private static final String TAG = "NDHMHealthIdCapture";
    private static final String HEALTH_ID_VERIFICATION_SCREEN = "healthIdVerificationScreen";
    private static final String AUTH_MODE_SELECTION_SCREEN = "authModeSelectionScreen";
    private static final String AUTH_MODE_DEMOGRAPHIC_SCREEN = "authModeDemographicScreen";
    private static final String OTP_VALIDATION_SCREEN = "otpValidationScreen";
    private static final String STATE_SELECTION_SCREEN = "stateSelectionScreen";
    private static final String DISPLAY_HEALTH_ID_SCREEN = "displayHealthIdScreen";

    private final SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy", Locale.getDefault());

    @Bean
    SewaServiceRestClientImpl restClient;

    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private LinearLayout footerLayout;
    private MyAlertDialog myAlertDialog;

    private TextInputLayout healthIdTB;
    private Spinner authModeSelection;
    private TextInputLayout nameTB;
    private RadioGroup genderRG;
    private LinearLayout dobDP;
    private DateChangeListenerStatic dobChangeListener;
    private TextInputLayout otpTB;
    private MaterialButton verificationBtn;
    private Spinner stateSelection;
    private MaterialTextView timer;
    private MaterialTextView resendBtn;

    private final Timer[] timerCounter = new Timer[1];
    private final int[] counter = {30};

    private Integer memberId;
    private String beneficiaryName;
    private String screen;
    private String selectedAuthMode;
    private String txnId;
    private String token;
    private final Map<String, String> states = new HashMap<>();
    private LinkBenefitIdResponse linkBenefitIdResponse;
    private String featureName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        beneficiaryName = this.getIntent().getStringExtra(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY);
        featureName = this.getIntent().getStringExtra("featureName");
        setTitle(UtilBean.getTitleText(LabelConstants.LINK_HEALTH_ID));
    }

    private void initView() {
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(context, this);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        footerLayout = globalPanel.findViewById(DynamicUtils.ID_FOOTER);
        setHealthIdVerificationScreen();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screen) {
                case HEALTH_ID_VERIFICATION_SCREEN:
                    if (healthIdValidation()) {
                        showProcessDialog();
                        fetchAuthMode();
                    }
                    break;
                case AUTH_MODE_SELECTION_SCREEN:
                    if (authModeSelection.getSelectedItem() != null) {
                        showProcessDialog();
                        selectedAuthMode = NDHMConstants.getAuthModeKey(authModeSelection.getSelectedItem().toString());
                        validateAuthMode();
                    }
                    break;
                case AUTH_MODE_DEMOGRAPHIC_SCREEN:
                    if (dobValidation() && genderValidation() && nameValidation()) {
                        showProcessDialog();
                        getTokenForDemographics();
                    }
                    break;
                case STATE_SELECTION_SCREEN:
                    if (stateSelection.getSelectedItem() != null) {
                        showProcessDialog();
                        linkHealthId(states.get(stateSelection.getSelectedItem().toString()));
                    }
                    break;
                default:
            }
        }
    }

    @UiThread
    public void setHealthIdVerificationScreen() {
        screen = HEALTH_ID_VERIFICATION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        footerLayout.setVisibility(View.VISIBLE);
        nextButton.setText(UtilBean.getMyLabel(LabelConstants.VALIDATE));

        healthIdTB = MyStaticComponents.getEditText(context, "XX-XXXX-XXXX-XXXX", -1, 17, InputType.TYPE_CLASS_NUMBER);
        healthIdTB.getEditText().setKeyListener(DigitsKeyListener.getInstance("1234567890-"));
        healthIdTB.getEditText().addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                len = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not required
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > len) {
                    //User has added a character
                    if (s.length() == 2 || s.length() == 7 || s.length() == 12) {
                        s.append("-");
                    }
                } else {
                    // User has deleted a character
                    if (s.length() == 2 || s.length() == 7 || s.length() == 12) {
                        s.delete(s.length() - 1, s.length());
                    }
                }
            }
        });

        bodyLayoutContainer.addView(MyStaticComponents.generateTitleView(context, LabelConstants.LINK_HEALTH_ID));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.HEALTH_ID_NUMBER));
        bodyLayoutContainer.addView(healthIdTB);
    }

    @Background
    public void fetchAuthMode() {
        try {
            HealthIdSearchResponse response = restClient.searchUserByHealthId(healthIdTB.getEditText().getText().toString());
            if (response == null || response.getAuthMethods().isEmpty()) {
                runOnUiThread(() -> {
                    hideProcessDialog();
                    SewaUtil.generateToast(context, "No auth modes found");
                });
            } else {
                setComponentForAuthMode(response.getAuthMethods());
            }
        } catch (final Exception e) {
            runOnUiThread(() -> {
                hideProcessDialog();
                SewaUtil.generateToast(context, e.getMessage());
            });
        }
    }

    private void setComponentForAuthMode(List<String> authModes) {
        List<OptionDataBean> options = new ArrayList<>();
        OptionDataBean option;

        for (String authMode : authModes) {
            if (NDHMConstants.AUTH_MODE_AADHAAR_BIO.equals(authMode) || NDHMConstants.AUTH_MODE_PASSWORD.equals(authMode)
                    || NDHMConstants.AUTH_MODE_DEMOGRAPHICS.equals(authMode)) {
                continue;
            }
            option = new OptionDataBean();
            option.setKey(authMode);
            option.setValue(NDHMConstants.getAuthModeFullName(authMode));
            options.add(option);
        }

        authModeSelection = new Spinner(context);
        MyArrayAdapter myAdapter = new MyArrayAdapter(context, R.layout.spinner_item_top, UtilBean.getListOfOptions(options));
        myAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        authModeSelection.setAdapter(myAdapter);
        authModeSelection.setSelection(-1);

        setAuthModeSelectionScreen();
    }

    @UiThread
    public void setAuthModeSelectionScreen() {
        screen = AUTH_MODE_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        footerLayout.setVisibility(View.VISIBLE);
        nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));

        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context, UtilBean.getMyLabel(LabelConstants.SELECT_AUTH_MODE_TO_LINK_HEALTH_ID)));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel("Select auth mode")));
        bodyLayoutContainer.addView(authModeSelection);
        hideProcessDialog();
    }

    @Background
    public void validateAuthMode() {
        final String[] message = {"Issue with auth method validation or selected auth mode is not allowed for you"};
        try {
            Map<String, String> result = restClient.healthIdVerificationAuthInit(healthIdTB.getEditText().getText().toString(), selectedAuthMode);
            if (result != null) {
                txnId = result.get("txnId");
                setScreenForSelectedAuthMode();
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

    private void setScreenForSelectedAuthMode() {
        switch (selectedAuthMode) {
            case NDHMConstants.AUTH_MODE_DEMOGRAPHICS:
                setComponentForDemographic();
                break;
            case NDHMConstants.AUTH_MODE_AADHAAR_OTP:
            case NDHMConstants.AUTH_MODE_MOBILE_OTP:
            case NDHMConstants.AUTH_MODE_PASSWORD:
                createOtpValidationScreen();
                break;
            default:
                runOnUiThread(this::hideProcessDialog);
        }
    }

    private void setComponentForDemographic() {
        nameTB = MyStaticComponents.getEditText(context, "Name", -1, 99, InputType.TYPE_CLASS_TEXT);

        genderRG = new RadioGroup(context);
        genderRG.addView(MyStaticComponents.getRadioButton(context, UtilBean.getMyLabel(LabelConstants.MALE), IdConstants.RADIO_BUTTON_ID_YES));
        genderRG.addView(MyStaticComponents.getRadioButton(context, UtilBean.getMyLabel(LabelConstants.FEMALE), IdConstants.RADIO_BUTTON_ID_NO));
        genderRG.addView(MyStaticComponents.getRadioButton(context, UtilBean.getMyLabel(LabelConstants.OTHER), IdConstants.RADIO_BUTTON_ID_UNKNOWN));

        List<ValidationTagBean> list = new ArrayList<>();
        list.add(new ValidationTagBean(FormulaConstants.VALIDATION_IS_FUTURE_DATE, UtilBean.getMyLabel("Date of birth is can’t be in future")));
        list.add(new ValidationTagBean("isDateIn-Sub-110-0-0", UtilBean.getMyLabel("Date of birth can’t be before 110 years")));

        dobChangeListener = new DateChangeListenerStatic(context, list);
        dobDP = MyStaticComponents.getCustomDatePickerForStatic(context, dobChangeListener, -1);
        setDemographicScreen();
    }

    @UiThread
    public void setDemographicScreen() {
        bodyLayoutContainer.removeAllViews();
        footerLayout.setVisibility(View.VISIBLE);
        nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        screen = AUTH_MODE_DEMOGRAPHIC_SCREEN;

        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context,
                UtilBean.getMyLabel(LabelConstants.PLEASE_ENTER_ALL_DETAILS_AS_GIVEN_IN_THE_AADHAAR_CARD)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.BIRTH_DATE)));
        bodyLayoutContainer.addView(dobDP);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.GENDER)));
        bodyLayoutContainer.addView(genderRG);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.NAME)));
        bodyLayoutContainer.addView(nameTB);
        hideProcessDialog();
    }

    @Background
    public void getTokenForDemographics() {
        final String[] message = {"Issue for generating token"};
        try {
            String gender;
            if (genderRG.getCheckedRadioButtonId() == IdConstants.RADIO_BUTTON_ID_YES) {
                gender = GlobalTypes.MALE;
            } else {
                if (genderRG.getCheckedRadioButtonId() == IdConstants.RADIO_BUTTON_ID_NO)
                    gender = GlobalTypes.FEMALE;
                else gender = "O";
            }
            Map<String, String> result = restClient.confirmAadharDemo(txnId, gender, nameTB.getEditText().getText().toString(), sdfYear.format(dobChangeListener.getDateSet()));
            if (result != null && result.get("token") != null) {
                token = result.get("token");
                getStateList();
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
    public void createOtpValidationScreen() {
        bodyLayoutContainer.removeAllViews();
        footerLayout.setVisibility(View.GONE);
        screen = OTP_VALIDATION_SCREEN;

        final LinearLayout otpLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.aadhaar_otp_validation_layout, null);
        if (NDHMConstants.AUTH_MODE_PASSWORD.equals(selectedAuthMode)) {
            ((MaterialTextView) otpLayout.findViewById(R.id.description)).setText(UtilBean.getMyLabel(LabelConstants.ENTER_PASSWORD_TO_VERIFY_HEALTH_ID));
        } else if (NDHMConstants.AUTH_MODE_MOBILE_OTP.equals(selectedAuthMode)) {
            ((MaterialTextView) otpLayout.findViewById(R.id.description)).setText(UtilBean.getMyLabel(LabelConstants.AN_OTP_WILL_BE_SENT_TO_THE_MOBILE_PHONE_NUMBER_LINKED_TO_THIS_HEALTH_ID));
        } else {
            ((MaterialTextView) otpLayout.findViewById(R.id.description)).setText(UtilBean.getMyLabel(LabelConstants.AN_OTP_WILL_BE_SENT_TO_THE_MOBILE_PHONE_NUMBER_LINKED_TO_THIS_AADHAAR_NUMBER));
        }

        otpTB = otpLayout.findViewById(R.id.otpText);
        if (otpTB.getEditText() != null) {
            if (NDHMConstants.AUTH_MODE_PASSWORD.equals(selectedAuthMode)) {
                otpTB.getEditText().setHint(LabelConstants.ENTER_PASSWORD);
                otpTB.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                otpTB.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
            } else {
                otpTB.getEditText().setHint(LabelConstants.ENTER_OTP);
                otpTB.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
                otpTB.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
            }
        }

        verificationBtn = otpLayout.findViewById(R.id.verifyOtpBtn);
        verificationBtn.setText(UtilBean.getMyLabel(LabelConstants.VALIDATE));
        verificationBtn.setEnabled(false);

        otpLayout.findViewById(R.id.skip).setVisibility(View.INVISIBLE);
        otpLayout.findViewById(R.id.or).setVisibility(View.INVISIBLE);

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
                if (NDHMConstants.AUTH_MODE_PASSWORD.equals(selectedAuthMode)) {
                    verificationBtn.setEnabled(s != null && s.toString().length() >= 8);
                } else {
                    verificationBtn.setEnabled(Pattern.compile("^\\d{6}$").matcher(s).matches());
                }
            }
        });

        verificationBtn.setOnClickListener(v -> {
            verificationBtn.setEnabled(false);
            otpTB.setEnabled(false);
            showProcessDialog();
            validateOtpAndGenerateToken();
        });

        if (NDHMConstants.AUTH_MODE_MOBILE_OTP.equals(selectedAuthMode) || NDHMConstants.AUTH_MODE_AADHAAR_OTP.equals(selectedAuthMode)) {
            otpLayout.findViewById(R.id.timer).setVisibility(View.VISIBLE);
            otpLayout.findViewById(R.id.resend).setVisibility(View.VISIBLE);

            timer = otpLayout.findViewById(R.id.timer);
            resendBtn = otpLayout.findViewById(R.id.resend);
            resendBtn.setEnabled(false);

            resendBtn.setOnClickListener(v -> {
                showProcessDialog();
                resendOTP();
            });

            startTimer();
        } else {
            otpLayout.findViewById(R.id.timer).setVisibility(View.GONE);
            otpLayout.findViewById(R.id.resend).setVisibility(View.GONE);
            hideProcessDialog();
        }

        bodyLayoutContainer.addView(otpLayout);
    }

    @Background
    public void resendOTP() {
        final String[] message = {"Issue for sending OTP again"};
        try {
            Boolean result = restClient.resendOtp(txnId, selectedAuthMode);
            if (Boolean.TRUE.equals(result)) {
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

    private void startTimer() {
        counter[0] = 30;
        timer.setVisibility(View.VISIBLE);
        resendBtn.setEnabled(false);
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
                            });
                            this.cancel();
                        }
                    }
                },
                50, 1500
        );
        hideProcessDialog();
    }

    @Background
    public void validateOtpAndGenerateToken() {
        final String[] message = {"Issue for generating token"};
        try {
            Map<String, String> result = null;

            if (NDHMConstants.AUTH_MODE_AADHAAR_OTP.equals(selectedAuthMode)) {
                result = restClient.confirmAadharOtp(txnId, otpTB.getEditText().getText().toString());
            } else if (NDHMConstants.AUTH_MODE_MOBILE_OTP.equals(selectedAuthMode)) {
                result = restClient.confirmMobileOtp(txnId, otpTB.getEditText().getText().toString());
            } else if (NDHMConstants.AUTH_MODE_PASSWORD.equals(selectedAuthMode)) {
                result = restClient.confirmPassword(txnId, otpTB.getEditText().getText().toString());
            }

            if (result != null) {
                token = result.get("token");
                if (NDHMConstants.AUTH_MODE_MOBILE_OTP.equals(selectedAuthMode) || NDHMConstants.AUTH_MODE_AADHAAR_OTP.equals(selectedAuthMode)) {
                    runOnUiThread(() -> {
                        if (timerCounter[0] != null) {
                            timerCounter[0].cancel();
                        }
                        timer.setVisibility(View.GONE);
                        resendBtn.setEnabled(false);
                        verificationBtn.setEnabled(false);
                    });
                }
                getStateList();
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            message[0] = e.getMessage();
        }
        runOnUiThread(() -> {
            verificationBtn.setEnabled(true);
            otpTB.setEnabled(true);
            hideProcessDialog();
            SewaUtil.generateToast(context, message[0]);
        });
    }

    @Background
    public void getStateList() {
        final String[] message = {"Issue for fetching state"};
        try {
            StateResponse[] stateList = restClient.getStates();
            if (stateList != null && stateList.length > 0) {
                setStateSelectionScreen(stateList);
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            message[0] = e.getMessage();
        }
        runOnUiThread(() -> {
            if (!NDHMConstants.AUTH_MODE_DEMOGRAPHICS.equals(selectedAuthMode)) {
                verificationBtn.setEnabled(true);
            }
            hideProcessDialog();
            SewaUtil.generateToast(context, message[0]);
        });
    }

    @UiThread
    public void setStateSelectionScreen(StateResponse[] stateList) {
        bodyLayoutContainer.removeAllViews();
        footerLayout.setVisibility(View.VISIBLE);
        nextButton.setText(UtilBean.getMyLabel(LabelConstants.LINK));
        screen = STATE_SELECTION_SCREEN;

        List<OptionDataBean> options = new ArrayList<>();
        OptionDataBean option;

        states.clear();
        for (StateResponse state : stateList) {
            states.put(state.getName(), state.getCode());

            option = new OptionDataBean();
            option.setKey(state.getCode());
            option.setValue(state.getName());
            options.add(option);
        }

        stateSelection = new Spinner(context);
        MyArrayAdapter myAdapter = new MyArrayAdapter(context, R.layout.spinner_item_top, UtilBean.getListOfOptions(options));
        myAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        stateSelection.setAdapter(myAdapter);
        stateSelection.setSelection(-1);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT_STATE)));
        bodyLayoutContainer.addView(stateSelection);
        hideProcessDialog();
    }

    @Background
    public void linkHealthId(String stateCode) {
        final String[] message = {"Issue with link healthId"};
        try {
            linkBenefitIdResponse = restClient.linkBenefitId(memberId, token, stateCode, featureName, "LINK_" + selectedAuthMode);
            if (linkBenefitIdResponse != null && linkBenefitIdResponse.getStatus() != null && linkBenefitIdResponse.getStatus().equals("LINKED")) {
                displayHealthIdScreen();
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
    public void displayHealthIdScreen() {
        bodyLayoutContainer.removeAllViews();
        screen = DISPLAY_HEALTH_ID_SCREEN;

        bodyLayoutContainer.addView(MyStaticComponents.generateTitleView(context, UtilBean.getMyLabel(LabelConstants.HEALTH_ID_LINKED_SUCCESSFULLY)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.NAME)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, beneficiaryName));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.HEALTH_ID)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, linkBenefitIdResponse.getHealthId()));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel("Program Name")));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, linkBenefitIdResponse.getBenefitName()));

        footerLayout.setVisibility(View.VISIBLE);
        nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        nextButton.setOnClickListener(v -> sendResponse());

        hideProcessDialog();
    }

    private void sendResponse() {
        Intent intent = new Intent();
        intent.putExtra("HealthId", linkBenefitIdResponse.getHealthId());
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean healthIdValidation() {
        if (healthIdTB.getEditText() == null || healthIdTB.getEditText().getText() == null
                || healthIdTB.getEditText().getText().toString().trim().isEmpty()) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_ENTER_HEALTH_ID));
            return false;
        } else if (!Pattern.compile("^\\d{2}(-)\\d{4}(-)\\d{4}(-)\\d{4}$").matcher(healthIdTB.getEditText().getText().toString()).matches()) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_ENTER_HEALTH_ID_IN_PROPER_FORMAT));
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

    private boolean genderValidation() {
        if (genderRG == null || genderRG.getCheckedRadioButtonId() == -1) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel("Please enter gender"));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (screen == null || screen.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            switch (screen) {
                case HEALTH_ID_VERIFICATION_SCREEN:
                    setResult(RESULT_CANCELED);
                    finish();
                    break;
                case AUTH_MODE_SELECTION_SCREEN:
                    setHealthIdVerificationScreen();
                    break;
                case AUTH_MODE_DEMOGRAPHIC_SCREEN:
                case OTP_VALIDATION_SCREEN:
                    if (timerCounter[0] != null) {
                        timerCounter[0].cancel();
                    }
                    setAuthModeSelectionScreen();
                    break;
                case STATE_SELECTION_SCREEN:
                    if (NDHMConstants.AUTH_MODE_DEMOGRAPHICS.equals(selectedAuthMode)) {
                        setDemographicScreen();
                    } else {
                        validateAuthMode();
                    }
                    break;
                case DISPLAY_HEALTH_ID_SCREEN:
                    sendResponse();
                    break;
                default:
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (screen != null
                && !Arrays.asList(HEALTH_ID_VERIFICATION_SCREEN, DISPLAY_HEALTH_ID_SCREEN).contains(screen)) {
            View.OnClickListener myListener = v -> {
                if (v.getId() == BUTTON_POSITIVE) {
                    myAlertDialog.dismiss();
                    setResult(RESULT_CANCELED);
                    finish();
                } else {
                    myAlertDialog.dismiss();
                }
            };

            myAlertDialog = new MyAlertDialog(this,
                    LabelConstants.ARE_YOU_SURE_WANT_TO_CLOSE_HEALTH_ID_LINK,
                    myListener, DynamicUtils.BUTTON_YES_NO);
            myAlertDialog.show();
        } else if (DISPLAY_HEALTH_ID_SCREEN.equals(screen)) {
            sendResponse();
        } else {
            super.onBackPressed();
        }
    }
}

