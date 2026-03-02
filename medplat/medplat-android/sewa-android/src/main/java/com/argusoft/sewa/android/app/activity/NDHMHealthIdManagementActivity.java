package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.widget.TextViewCompat;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyAudioComponent;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.databean.AbhaConsentCheckboxModel;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.ListValueBean;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@EActivity
public class NDHMHealthIdManagementActivity extends MenuActivity implements MyAudioComponent.MediaPlayerListener {

    private static final int NDHM_HEALTH_ID_MANAGEMENT_REQUEST_CODE = 1000;
    private static final String HEALTH_ID = "HealthId";
    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private LinearLayout footerLayout;
    private Button nextButton;
    private MyAlertDialog myAlertDialog;

    private Integer memberId;
    private String beneficiaryName;
    private MediaPlayer audioMediaPlayer;
    private String featureName;
    private String screen;
    private static final String CREATE_ABHA_NUMBER = "createAbhaNumber";
    private static final String SEED_ABHA_NUMBER = "seedAbhaNumber";

    @OrmLiteDao(helper = DBConnection.class)
    public Dao<ListValueBean, Integer> listValueBeanDao;

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
        String name = this.getIntent().getStringExtra(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY);
        beneficiaryName = name != null ? name : "beneficiary";
        featureName = this.getIntent().getStringExtra("featureName");
        setTitle(UtilBean.getTitleText(LabelConstants.HEALTH_ID_MANAGEMENT));
    }

    private void initView() {
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(context, null);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        footerLayout = globalPanel.findViewById(DynamicUtils.ID_FOOTER);
        setActionForHealthIdScreen();
    }

    @UiThread
    public void setConsentMessageScreen(int position) {
        bodyLayoutContainer.removeAllViews();
        footerLayout.setVisibility(View.GONE);

        String language = "EN"; // default language
        if (SewaTransformer.loginBean != null) {
            language = SewaTransformer.loginBean.getLanguageCode();
        }

        String message;
        String heading;
        String textOtherThanAadhaar;
        String textAbhaUsage;
        String textAbhaSharing;
        String textAnonymization;
        String textI;
        String textHealthWorker;
        String textBeneficiary;

        if (language.equals("EN")) {
            message = LabelConstants.INFORMED_BENEFICIARY_ABOUT_ABDM;
            heading = LabelConstants.CONSENT_HEADING;
            textOtherThanAadhaar = LabelConstants.CONSENT_OTHER_THAN_AADHAAR;
            textAbhaUsage = LabelConstants.CONSENT_FOR_ABHA_USAGE;
            textAbhaSharing = LabelConstants.CONSENT_FOR_SHARING_HEALTH_RECORDS;
            textAnonymization = LabelConstants.CONSENT_ANONYMIZATION;
            textI = LabelConstants.I;
            textHealthWorker = LabelConstants.CONSENT_HEALTH_WORKER;
            textBeneficiary = LabelConstants.CONSENT_BENEFICIARY;
        } else {
            message = LabelConstants.INFORMED_BENEFICIARY_ABOUT_ABDM_GU;
            heading = LabelConstants.CONSENT_HEADING_GU;
            textOtherThanAadhaar = LabelConstants.CONSENT_OTHER_THAN_AADHAAR_GU;
            textAbhaUsage = LabelConstants.CONSENT_FOR_ABHA_USAGE_GU;
            textAbhaSharing = LabelConstants.CONSENT_FOR_SHARING_HEALTH_RECORDS_GU;
            textAnonymization = LabelConstants.CONSENT_ANONYMIZATION_GU;
            textI = LabelConstants.I;
            textHealthWorker = LabelConstants.CONSENT_HEALTH_WORKER_GU;
            textBeneficiary = LabelConstants.CONSENT_BENEFICIARY_GU;
        }


        MaterialTextView headingText = MyStaticComponents.generateInstructionView(context, heading);

        LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewLayoutParams.setMargins(0, 40, 0, 0);

        AbhaConsentCheckboxModel abhaConsentCheckboxModel = new AbhaConsentCheckboxModel();
        Gson gson = new Gson();

        MaterialCheckBox checkBox = MyStaticComponents.getCheckBox(context, message, -1, false);
        checkBox.setGravity(Gravity.TOP);
        checkBox.setChecked(true);
        footerLayout.setVisibility(View.VISIBLE);
        TextViewCompat.setTextAppearance(checkBox, R.style.CustomSubtitleViewCheckBoxText);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Boolean.TRUE.equals(isChecked)) {
                footerLayout.setVisibility(View.VISIBLE);
                checkBox.setChecked(true);
            } else {
                footerLayout.setVisibility(View.GONE);
                checkBox.setChecked(false);
            }
        });

        MaterialCheckBox checkBoxOtherThanAadhaar = MyStaticComponents.getCheckBox(context, textOtherThanAadhaar, -1, false);
        checkBoxOtherThanAadhaar.setGravity(Gravity.TOP);
        checkBoxOtherThanAadhaar.setChecked(true);
        checkBoxOtherThanAadhaar.setLayoutParams(textViewLayoutParams);
        TextViewCompat.setTextAppearance(checkBoxOtherThanAadhaar, R.style.CustomSubtitleViewCheckBoxText);
        checkBoxOtherThanAadhaar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Boolean.FALSE.equals(isChecked)) {
                checkBoxOtherThanAadhaar.setChecked(false);
            }
        });

        MaterialCheckBox checkBoxForABHAUsage = MyStaticComponents.getCheckBox(context, textAbhaUsage, -1, false);
        checkBoxForABHAUsage.setGravity(Gravity.TOP);
        checkBoxForABHAUsage.setChecked(true);
        checkBoxForABHAUsage.setLayoutParams(textViewLayoutParams);
        TextViewCompat.setTextAppearance(checkBoxForABHAUsage, R.style.CustomSubtitleViewCheckBoxText);
        checkBoxForABHAUsage.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Boolean.FALSE.equals(isChecked)) {
                checkBoxForABHAUsage.setChecked(false);
            }
        });

        MaterialCheckBox checkBoxForSharing = MyStaticComponents.getCheckBox(context, textAbhaSharing, -1, false);
        checkBoxForSharing.setGravity(Gravity.TOP);
        checkBoxForSharing.setChecked(true);
        checkBoxForSharing.setLayoutParams(textViewLayoutParams);
        TextViewCompat.setTextAppearance(checkBoxForSharing, R.style.CustomSubtitleViewCheckBoxText);
        checkBoxForSharing.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Boolean.FALSE.equals(isChecked)) {
                checkBoxForSharing.setChecked(false);
            }
        });

        MaterialCheckBox checkBoxAnonymization = MyStaticComponents.getCheckBox(context, textAnonymization, -1, false);
        checkBoxAnonymization.setGravity(Gravity.TOP);
        checkBoxAnonymization.setChecked(true);
        checkBoxAnonymization.setLayoutParams(textViewLayoutParams);
        TextViewCompat.setTextAppearance(checkBoxAnonymization, R.style.CustomSubtitleViewCheckBoxText);
        checkBoxAnonymization.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Boolean.FALSE.equals(isChecked)) {
                checkBoxAnonymization.setChecked(false);
            }
        });

        String healthWorkerConsent = String.format("%s, %s , %s", textI, SewaTransformer.loginBean.getFirstName(), textHealthWorker);
        MaterialCheckBox checkBoxHealthWorker = MyStaticComponents.getCheckBox(context, healthWorkerConsent, -1, false);
        checkBoxHealthWorker.setGravity(Gravity.TOP);
        checkBoxHealthWorker.setChecked(true);
        checkBoxHealthWorker.setPadding(60, 0, 0, 0);
        checkBoxHealthWorker.setLayoutParams(textViewLayoutParams);
        TextViewCompat.setTextAppearance(checkBoxHealthWorker, R.style.CustomSubtitleViewCheckBoxText);
        checkBoxHealthWorker.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Boolean.FALSE.equals(isChecked)) {
                checkBoxHealthWorker.setChecked(false);
            }
        });

        String beneficiaryConsent = String.format("%s, %s , %s", textI, beneficiaryName, textBeneficiary);
        MaterialCheckBox checkBoxBeneficiary = MyStaticComponents.getCheckBox(context, beneficiaryConsent, -1, false);
        checkBoxBeneficiary.setGravity(Gravity.TOP);
        checkBoxBeneficiary.setChecked(true);
        checkBoxBeneficiary.setPadding(60, 0, 0, 0);
        checkBoxBeneficiary.setLayoutParams(textViewLayoutParams);
        TextViewCompat.setTextAppearance(checkBoxBeneficiary, R.style.CustomSubtitleViewCheckBoxText);
        checkBoxBeneficiary.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Boolean.FALSE.equals(isChecked)) {
                checkBoxBeneficiary.setChecked(false);
            }
        });

        bodyLayoutContainer.addView(headingText);
        bodyLayoutContainer.addView(checkBox);
        bodyLayoutContainer.addView(checkBoxOtherThanAadhaar);
        bodyLayoutContainer.addView(checkBoxForABHAUsage);
        bodyLayoutContainer.addView(checkBoxForSharing);
        bodyLayoutContainer.addView(checkBoxAnonymization);
        bodyLayoutContainer.addView(checkBoxHealthWorker);
        bodyLayoutContainer.addView(checkBoxBeneficiary);
        addAudioView();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked() && checkBoxHealthWorker.isChecked() && checkBoxBeneficiary.isChecked()) {

                    if (checkBox.isChecked()) {
                        abhaConsentCheckboxModel.setAadhaarSharingConsentGiven(Boolean.TRUE);
                    } else {
                        abhaConsentCheckboxModel.setAadhaarSharingConsentGiven(Boolean.FALSE);
                    }

                    if (checkBoxOtherThanAadhaar.isChecked()) {
                        abhaConsentCheckboxModel.setDocOtherThanAadhaarConsentGiven(Boolean.TRUE);
                    } else {
                        abhaConsentCheckboxModel.setDocOtherThanAadhaarConsentGiven(Boolean.FALSE);
                    }

                    if (checkBoxForABHAUsage.isChecked()) {
                        abhaConsentCheckboxModel.setAbhaUsageConsentGiven(Boolean.TRUE);
                    } else {
                        abhaConsentCheckboxModel.setAbhaUsageConsentGiven(Boolean.FALSE);
                    }

                    if (checkBoxForSharing.isChecked()) {
                        abhaConsentCheckboxModel.setSharingHealthRecordsConsentGiven(Boolean.TRUE);
                    } else {
                        abhaConsentCheckboxModel.setSharingHealthRecordsConsentGiven(Boolean.FALSE);
                    }

                    if (checkBoxAnonymization.isChecked()) {
                        abhaConsentCheckboxModel.setAnonymizationConsentGiven(Boolean.TRUE);
                    } else {
                        abhaConsentCheckboxModel.setAnonymizationConsentGiven(Boolean.FALSE);
                    }
                    if (checkBoxHealthWorker.isChecked()) {
                        abhaConsentCheckboxModel.setHealthWorkerConsentGiven(Boolean.TRUE);
                    } else {
                        abhaConsentCheckboxModel.setHealthWorkerConsentGiven(Boolean.FALSE);
                    }

                    if (checkBoxBeneficiary.isChecked()) {
                        abhaConsentCheckboxModel.setBeneficiaryConsentGiven(Boolean.TRUE);
                    } else {
                        abhaConsentCheckboxModel.setBeneficiaryConsentGiven(Boolean.FALSE);
                    }
                    String myJson = gson.toJson(abhaConsentCheckboxModel);
                    Intent intent;
                    if (position == 0) {
                        intent = new Intent(context, NDHMHealthIdCreationActivity_.class);
                        intent.putExtra("featureName", featureName);
                        intent.putExtra(RelatedPropertyNameConstants.MEMBER_ID, memberId);
                        intent.putExtra(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY, getIntent().getStringExtra(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY));
                        intent.putExtra(RelatedPropertyNameConstants.AADHAR_SCANNED, getIntent().getStringExtra(RelatedPropertyNameConstants.AADHAR_SCANNED));
                        intent.putExtra(RelatedPropertyNameConstants.GENDER, getIntent().getStringExtra(RelatedPropertyNameConstants.GENDER));
                        intent.putExtra(RelatedPropertyNameConstants.MOBILE_NUMBER, getIntent().getStringExtra(RelatedPropertyNameConstants.MOBILE_NUMBER));
                        intent.putExtra(RelatedPropertyNameConstants.DOB, getIntent().getStringExtra(RelatedPropertyNameConstants.DOB));
                        intent.putExtra(RelatedPropertyNameConstants.ABHA_CONSENT_CHECKBOX_MODEL, myJson);
                        startActivityForResult(intent, NDHM_HEALTH_ID_MANAGEMENT_REQUEST_CODE);
                    } else {
                        intent = new Intent(context, NDHMHealthIdCaptureActivity_.class);
                        intent.putExtra("featureName", featureName);
                        intent.putExtra(RelatedPropertyNameConstants.MEMBER_ID, memberId);
                        intent.putExtra(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY, getIntent().getStringExtra(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY));
                        intent.putExtra(RelatedPropertyNameConstants.AADHAR_SCANNED, getIntent().getStringExtra(RelatedPropertyNameConstants.AADHAR_SCANNED));
                        intent.putExtra(RelatedPropertyNameConstants.GENDER, getIntent().getStringExtra(RelatedPropertyNameConstants.GENDER));
                        intent.putExtra(RelatedPropertyNameConstants.MOBILE_NUMBER, getIntent().getStringExtra(RelatedPropertyNameConstants.MOBILE_NUMBER));
                        intent.putExtra(RelatedPropertyNameConstants.DOB, getIntent().getStringExtra(RelatedPropertyNameConstants.DOB));
                        intent.putExtra(RelatedPropertyNameConstants.ABHA_CONSENT_CHECKBOX_MODEL, myJson);
                        startActivityForResult(intent, NDHM_HEALTH_ID_MANAGEMENT_REQUEST_CODE);
                    }
                } else {
                    SewaUtil.generateToast(context, "Please check to aadhaar sharing and confirmation consents to continue");
                }
            }
        });
    }

    private void addAudioView() {
        List<ListValueBean> listValueBean = null;
        try {
            listValueBean = listValueBeanDao.queryForEq(FieldNameConstants.Field, "offline_health_id_consent");
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        if (listValueBean != null && !listValueBean.isEmpty()) {
            String fileName = listValueBean.get(0).getValue();
            fileName = SewaConstants.DIR_DOWNLOADED + fileName;
            File file = new File(fileName);
            if (!file.exists()) {
                footerLayout.setVisibility(View.VISIBLE);
                return;
            }
            Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
            if (audioMediaPlayer == null) {
                audioMediaPlayer = MediaPlayer.create(this, uri);
            }
            MyAudioComponent myAudioComponent = new MyAudioComponent(this, R.layout.health_id_consent_audio_player_layout, audioMediaPlayer, this);
            myAudioComponent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            bodyLayoutContainer.addView(myAudioComponent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.audioMediaPlayer != null) {
            audioMediaPlayer.pause();
        }
    }

    @UiThread
    public void setActionForHealthIdScreen() {
        screen = null;
        bodyLayoutContainer.removeAllViews();
        footerLayout.setVisibility(View.GONE);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(
                null, null, context, String.format("%s %s", UtilBean.getMyLabel("Please choose action to perform for"), beneficiaryName)));
        List<String> options = new ArrayList<>();

        options.add(UtilBean.getMyLabel(LabelConstants.CREATE_HEALTH_ID));

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    screen = CREATE_ABHA_NUMBER;
                } else {
                    screen = SEED_ABHA_NUMBER;
                }
                setConsentMessageScreen(position);
            }
        };

        ListView buttonList = MyStaticComponents.getButtonList(context, options, onItemClickListener);
        bodyLayoutContainer.addView(buttonList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getStringExtra(RelatedPropertyNameConstants.HEALTH_ID_NUMBER) != null) {
            Intent intent = new Intent();
            intent.putExtra(RelatedPropertyNameConstants.HEALTH_ID_NUMBER, data.getStringExtra(RelatedPropertyNameConstants.HEALTH_ID_NUMBER));
            intent.putExtra(RelatedPropertyNameConstants.HEALTH_ID, data.getStringExtra(RelatedPropertyNameConstants.HEALTH_ID));
            intent.putExtra("isFromOffline", data.getBooleanExtra("isFromOffline", false));
            setResult(RESULT_OK, intent);
            finish();
            return;
        }
        bodyLayoutContainer.removeAllViews();
        setActionForHealthIdScreen();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        if (screen == null || screen.isEmpty()) {
            finish();
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            switch (screen) {
                case CREATE_ABHA_NUMBER:
                case SEED_ABHA_NUMBER:
                    setActionForHealthIdScreen();
                    break;
                default:
                    finish();
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if ((CREATE_ABHA_NUMBER.equals(screen) || SEED_ABHA_NUMBER.equals(screen))) {
            setActionForHealthIdScreen();
        }
    }

    @Override
    public void onCompletion() {
        footerLayout.setVisibility(View.VISIBLE);
    }
}
