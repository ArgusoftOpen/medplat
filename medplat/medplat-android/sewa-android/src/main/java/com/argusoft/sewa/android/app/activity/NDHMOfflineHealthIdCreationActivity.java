package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;
import static com.argusoft.sewa.android.app.component.MyStaticComponents.getLinearLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.listeners.DateChangeListenerStatic;
import com.argusoft.sewa.android.app.constants.FormulaConstants;
import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.core.impl.SewaServiceRestClientImpl;
import com.argusoft.sewa.android.app.databean.OfflineHealthIdBean;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.AadharScanUtil;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

@EActivity
public class NDHMOfflineHealthIdCreationActivity extends MenuActivity implements View.OnClickListener {

    private static final String TAG = "NDHMHealthIdCreation";
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private static final SimpleDateFormat displaySDF = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private static final String ADD_DETAILS = "add_details";
    private static final String CONFIRMATION_MESSAGE = "CONFIRMATION_MESSAGE";
    private static final String OFFLINE_MESSAGE = "OFFLINE_MESSAGE";

    @Bean
    SewaServiceRestClientImpl restClient;

    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private LinearLayout detailsLayout;
    private Button nextButton;
    private MyAlertDialog myAlertDialog;
    private TextInputLayout aadhaarTB;
    private TextInputLayout mobileNumberTB;
    private TextInputLayout nameTB;
    private RadioGroup genderRG;
    private LinearLayout dobDP;
    private DateChangeListenerStatic dobChangeListener;
    private Integer memberId;
    private String screen;
    private String abhaConsentCheckboxModel;
    MaterialCheckBox checkBox;

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
        abhaConsentCheckboxModel = getIntent().getStringExtra(RelatedPropertyNameConstants.ABHA_CONSENT_CHECKBOX_MODEL);
        setTitle(UtilBean.getTitleText(LabelConstants.CREATE_HEALTH_ID));
    }

    private void initView() {
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(context, this);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        nextButton.setOnClickListener(this);
        detailsLayout = getLinearLayout(
                context, -1, VERTICAL,
                new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        setComponent();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screen) {
                case ADD_DETAILS:
                    if (aadhaarValidation() && dobValidation() && genderValidation() && mobileNumberValidation() && nameValidation()) {
                        setMessageConformationScreen();
                    }
                    break;
                case CONFIRMATION_MESSAGE:
                    sendResponse();
                    setMessageOfflineScreen();
                    break;
                case OFFLINE_MESSAGE:
                    finish();
                default:
            }
        }
    }

    private void setComponent() {
        screen = ADD_DETAILS;

        String aadharNumber = "";
        int gender = -1;
        aadharNumber = getIntent().getStringExtra(RelatedPropertyNameConstants.AADHAR_NUMBER);
        String dob = getIntent().getStringExtra(RelatedPropertyNameConstants.DOB);
        String genderString = getIntent().getStringExtra(RelatedPropertyNameConstants.GENDER);
        String name = getIntent().getStringExtra(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY);
        String aadharScanData = getIntent().getStringExtra(RelatedPropertyNameConstants.AADHAR_SCANNED);
        String mobileNumber = getIntent().getStringExtra(RelatedPropertyNameConstants.MOBILE_NUMBER);
        if (aadharScanData != null && !aadharScanData.isEmpty()) {
            Map<String, String> aadharData = new Gson().fromJson(SharedStructureData.relatedPropertyHashTable.get("aadharScannedData"), Map.class);
            if (aadharData != null) {
                if (aadharData.containsKey(AadharScanUtil.NAME)) {
                    name = aadharData.get(AadharScanUtil.NAME);
                }
                if (aadharData.containsKey(AadharScanUtil.DOB)) {
                    dob = aadharData.get(AadharScanUtil.DOB);
                }
                if (aadharData.containsKey(AadharScanUtil.UID)) {
                    aadharNumber = aadharData.get(AadharScanUtil.UID);
                }
                if (aadharData.containsKey(AadharScanUtil.GENDER)) {
                    genderString = aadharData.get(AadharScanUtil.GENDER);
                }
            }
        }
        if (genderString != null) {
            if (genderString.matches("\\d+(?:\\.\\d+)?")) {
                int genderAns = Integer.parseInt(getIntent().getStringExtra(RelatedPropertyNameConstants.GENDER));
                if (genderAns == 1) {
                    gender = IdConstants.RADIO_BUTTON_ID_YES;
                } else if (genderAns == 2) {
                    gender = IdConstants.RADIO_BUTTON_ID_NO;
                } else {
                    gender = IdConstants.RADIO_BUTTON_ID_UNKNOWN;
                }
            } else {
                if (genderString.equalsIgnoreCase("M")) {
                    gender = IdConstants.RADIO_BUTTON_ID_YES;
                } else if (genderString.equalsIgnoreCase("F")) {
                    gender = IdConstants.RADIO_BUTTON_ID_NO;
                } else {
                    gender = IdConstants.RADIO_BUTTON_ID_UNKNOWN;
                }
            }
        }

        aadhaarTB = MyStaticComponents.getEditText(context, LabelConstants.AADHAR_NUMBER, -1, 12, InputType.TYPE_CLASS_NUMBER);
        if (aadharNumber != null && !aadharNumber.isEmpty()) {
            aadhaarTB.getEditText().setText(aadharNumber);
        }
        mobileNumberTB = MyStaticComponents.getEditText(context, LabelConstants.MOBILE_NUMBER, -1, 10, InputType.TYPE_CLASS_PHONE);
        if (mobileNumber != null && !mobileNumber.isEmpty()) {
            mobileNumberTB.getEditText().setText(mobileNumber);
        }
        nameTB = MyStaticComponents.getEditText(context, LabelConstants.NAME, -1, 99, InputType.TYPE_CLASS_TEXT);
        if (name != null && !name.isEmpty() && !name.toLowerCase(Locale.ROOT).equals("beneficiary")) {
            nameTB.getEditText().setText(name);
        }
        genderRG = new RadioGroup(context);
        genderRG.addView(MyStaticComponents.getRadioButton(context, UtilBean.getMyLabel(LabelConstants.MALE), IdConstants.RADIO_BUTTON_ID_YES));
        genderRG.addView(MyStaticComponents.getRadioButton(context, UtilBean.getMyLabel(LabelConstants.FEMALE), IdConstants.RADIO_BUTTON_ID_NO));
        genderRG.addView(MyStaticComponents.getRadioButton(context, UtilBean.getMyLabel(LabelConstants.OTHER), IdConstants.RADIO_BUTTON_ID_UNKNOWN));
        genderRG.check(gender);

        List<ValidationTagBean> list = new ArrayList<>();
        list.add(new ValidationTagBean(FormulaConstants.VALIDATION_IS_FUTURE_DATE, UtilBean.getMyLabel("Date of birth is can’t be in future")));
        list.add(new ValidationTagBean("isDateIn-Sub-110-0-0", UtilBean.getMyLabel("Date of birth can’t be before 110 years")));

        dobChangeListener = new DateChangeListenerStatic(context, list);
        dobDP = MyStaticComponents.getCustomDatePickerForStatic(context, dobChangeListener, -1);
        for (int i = 0; i < dobDP.getChildCount(); i++) {
            if (dobDP.getChildAt(i) instanceof MaterialTextView && (dob != null && !dob.isEmpty())) {
                ((MaterialTextView) dobDP.getChildAt(i)).setText(dob);
                try {
                    dobChangeListener.setDateSet(displaySDF.parse(dob));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        screen = ADD_DETAILS;
        bodyLayoutContainer.removeAllViews();

        detailsLayout.addView(MyStaticComponents.generateTitleView(context, UtilBean.getMyLabel(LabelConstants.CREATE_HEALTH_ID)));

        detailsLayout.addView(MyStaticComponents.generateInstructionView(context,
                UtilBean.getMyLabel(LabelConstants.PLEASE_ENTER_ALL_DETAILS_AS_GIVEN_IN_THE_AADHAAR_CARD)));

        detailsLayout.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.AADHAR_NUMBER)));
        detailsLayout.addView(aadhaarTB);

        detailsLayout.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.BIRTH_DATE)));
        detailsLayout.addView(dobDP);

        detailsLayout.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.GENDER)));
        detailsLayout.addView(genderRG);

        detailsLayout.addView(MyStaticComponents.generateQuestionViewWithTooltip(null, UtilBean.getMyLabel(LabelConstants.MOBILE_NUMBER_HINT_FOR_ABHA), context, UtilBean.getMyLabel(LabelConstants.MOBILE_NUMBER)));
        detailsLayout.addView(mobileNumberTB);

        detailsLayout.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.NAME)));
        detailsLayout.addView(nameTB);

        bodyLayoutContainer.addView(detailsLayout);
        nextButton.setVisibility(View.VISIBLE);
    }

    @UiThread
    public void setMessageConformationScreen() {
        screen = CONFIRMATION_MESSAGE;
        bodyLayoutContainer.removeAllViews();
        nextButton.setVisibility(View.GONE);
        nextButton.setText(UtilBean.getMyLabel(LabelConstants.REGISTER));
        checkBox = MyStaticComponents.getCheckBox(context, UtilBean.getMyLabel(LabelConstants.ABHA_OFFLINE_CONSENT), -1, false);
        checkBox.setGravity(Gravity.TOP);
        TextViewCompat.setTextAppearance(checkBox, R.style.CustomSubtitleView);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Boolean.TRUE.equals(isChecked)) {
                nextButton.setVisibility(View.VISIBLE);
            } else {
                nextButton.setVisibility(View.GONE);
            }
        });
        bodyLayoutContainer.addView(checkBox);

    }

    @UiThread
    public void setMessageOfflineScreen(){

        screen = OFFLINE_MESSAGE;
        bodyLayoutContainer.removeAllViews();
        nextButton.setText(UtilBean.getMyLabel(LabelConstants.OKAY));
        nextButton.setVisibility(View.VISIBLE);
        MaterialTextView textBox = MyStaticComponents.generateTitleView(context, UtilBean.getMyLabel(LabelConstants.OFFLINE_ABHA_NUMBER_CREATION_MESSAGE));
        textBox.setGravity(Gravity.TOP);
        bodyLayoutContainer.addView(textBox);
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
        String gender;
        if (genderRG.getCheckedRadioButtonId() == IdConstants.RADIO_BUTTON_ID_YES) {
            gender = GlobalTypes.MALE;
        } else {
            if (genderRG.getCheckedRadioButtonId() == IdConstants.RADIO_BUTTON_ID_NO)
                gender = GlobalTypes.FEMALE;
            else gender = "O";
        }
        Intent intent = new Intent();
        String offlineData = new Gson().toJson(new OfflineHealthIdBean(aadhaarTB.getEditText().getText().toString().trim(),
                SDF.format(dobChangeListener.getDateSet()),
                gender,
                mobileNumberTB.getEditText().getText().toString().trim(),
                nameTB.getEditText().getText().toString().trim(),
                getIntent().getStringExtra(RelatedPropertyNameConstants.AUDIO_FILE_NAME),
                memberId,
                abhaConsentCheckboxModel,
                checkBox.isChecked()
        ));
        intent.putExtra(OfflineHealthIdBean.class.getSimpleName(), offlineData);
        setResult(RESULT_OK, intent);
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
                case CONFIRMATION_MESSAGE:
                    screen = ADD_DETAILS;
                    bodyLayoutContainer.removeAllViews();
                    bodyLayoutContainer.addView(detailsLayout);
                    nextButton.setVisibility(View.VISIBLE);
                    break;
                case ADD_DETAILS:
                    setResult(RESULT_CANCELED);
                    finish();
                    break;
                default:
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (screen == null
                || !Collections.singletonList(ADD_DETAILS).contains(screen)) {
            super.onBackPressed();
        } else {
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
                    LabelConstants.ARE_YOU_SURE_WANT_TO_CLOSE_HEALTH_ID_CREATION,
                    myListener, DynamicUtils.BUTTON_YES_NO);
            myAlertDialog.show();
        }
    }
}
