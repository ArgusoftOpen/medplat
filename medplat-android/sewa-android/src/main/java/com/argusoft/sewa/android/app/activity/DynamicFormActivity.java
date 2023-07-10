package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyDynamicComponents;
import com.argusoft.sewa.android.app.component.MyProcessDialog;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.NotificationConstants;
import com.argusoft.sewa.android.app.constants.RchConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.core.impl.DailyNutritionLogServiceImpl;
import com.argusoft.sewa.android.app.core.impl.HealthInfrastructureServiceImpl;
import com.argusoft.sewa.android.app.core.impl.ImmunisationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.LocationMasterServiceImpl;
import com.argusoft.sewa.android.app.core.impl.MoveToProductionServiceImpl;
import com.argusoft.sewa.android.app.core.impl.NPCBServiceImpl;
import com.argusoft.sewa.android.app.core.impl.NcdScoreServiceImpl;
import com.argusoft.sewa.android.app.core.impl.NcdServiceImpl;
import com.argusoft.sewa.android.app.core.impl.RchHighRiskServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SchoolServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceRestClientImpl;
import com.argusoft.sewa.android.app.core.impl.TechoServiceImpl;
import com.argusoft.sewa.android.app.core.impl.VersionServiceImpl;
import com.argusoft.sewa.android.app.databean.OfflineHealthIdBean;
import com.argusoft.sewa.android.app.datastructure.FormEngine;
import com.argusoft.sewa.android.app.datastructure.PageFormBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.exception.DataException;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.morbidities.constants.MorbiditiesConstant;
import com.argusoft.sewa.android.app.restclient.RestHttpException;
import com.argusoft.sewa.android.app.service.GPSTracker;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.AadharScanUtil;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.FormMetaDataUtil;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.PointInPolygon;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author alpeshkyada
 */
@EActivity
public class DynamicFormActivity extends MenuActivity implements View.OnClickListener {

    public static FormEngine formEngine;

    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl sewaFhsService;
    @Bean
    RchHighRiskServiceImpl rchHighRiskService;
    @Bean
    ImmunisationServiceImpl immunisationService;
    @Bean
    TechoServiceImpl techoService;
    @Bean
    NcdScoreServiceImpl ncdScoreService;
    @Bean
    NcdServiceImpl ncdService;
    @Bean
    FormMetaDataUtil formMetaDataUtil;
    @Bean
    MoveToProductionServiceImpl moveToProductionService;
    @Bean
    HealthInfrastructureServiceImpl healthInfrastructureService;
    @Bean
    LocationMasterServiceImpl locationMasterService;
    @Bean
    NPCBServiceImpl npcbService;
    @Bean
    SchoolServiceImpl schoolService;
    @Bean
    SewaServiceRestClientImpl serviceRestClient;
    @Bean
    DailyNutritionLogServiceImpl dailyNutritionLogService;
    @Bean
    VersionServiceImpl versionService;

    private static final String TAG = "DynamicFormActivity";
    private SharedPreferences sharedPref;
    //  Required Fields
    private String formType;
    private boolean allowForm;
    private boolean isLabTestForm = false;
    private Integer labTestFormDetId;
    private String labTestVersion;
    private MyAlertDialog myDialog;
    private String answerString;
    private boolean isProperFinish = true;
    private String currentLatitude;
    private String currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        showProcessDialog();
        getLgdWiseCoordinates();
        context = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        try {
            formType = getIntent().getStringExtra(SewaConstants.ENTITY);
            allowForm = getIntent().getBooleanExtra(SewaConstants.ALLOW_SECOND_FORM_SAME_MEMBER_OFFLINE, Boolean.FALSE);
            isLabTestForm = getIntent().getBooleanExtra("isOPDLabTestForm", Boolean.FALSE);
            if (isLabTestForm) {
                labTestFormDetId = Integer.parseInt(getIntent().getStringExtra("labTestDetId"));
                labTestVersion = getIntent().getStringExtra("version");
            }
            initView();
        } catch (Exception e) {
            Log.e(getClass().getName(), null, e);
            sewaService.storeException(e, GlobalTypes.EXCEPTION_TYPE_DYNAMIC_FORM);
            showToaster(LabelConstants.ERROR_OCCURRED_SO_TRY_AGAIN_LATER);
            formType = "DynamicForm";
        }
    }

    @Background
    public void getLgdWiseCoordinates() {
        techoService.getLgdCodeWiseCoordinates();
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
        retrieveSheet();
    }

    private void initView() {
        checkIfAnyFormSyncIsPending();
    }

    private void checkIfAnyFormSyncIsPending() {
        if (allowForm) {
            return;
        }
        if (SewaTransformer.loginBean.getUserRole().equals(GlobalTypes.USER_ROLE_ASHA)) {
            return;
        }
        if (formType != null && (FormConstants.NCD_SHEETS.contains(formType)
                || FormConstants.IDSP_MEMBER.equals(formType)
                || FormConstants.IDSP_MEMBER_2.equals(formType))) {
            return;
        }

        String memberId = SharedStructureData.relatedPropertyHashTable.get("memberId");
        if (memberId != null) {
            boolean aBoolean = techoService.checkIfOfflineAnyFormFilledForMember(Long.valueOf(memberId));
            if (aBoolean) {
                showAlertAndFinish(LabelConstants.MSG_REFRESH_AND_TRY_AGAIN);
            }
        }
    }

    @Background
    public void retrieveSheet() {
        try {
            if (formEngine == null && formType != null) {
                SharedStructureData.formFillUpTime = new Date().getTime(); // set form start tym on first retrieval of question
                sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                sharedPref.edit().putString(RelatedPropertyNameConstants.FORM_START_DATE, String.valueOf(new Date().getTime())).apply();
                loadQuestionsAndQuestion();
                checkForGpsEnabledForms();
                runOnUiThread(() -> {
                    formEngine = FormEngine.generateForm(DynamicFormActivity.this, formType);
                    setContentView();
                });
            } else {
                setContentView();
            }
        } catch (Exception e) {
            hideProcessDialog();
            sewaService.storeException(e, GlobalTypes.EXCEPTION_TYPE_DYNAMIC_FORM);
            showToaster(LabelConstants.ERROR_OCCURRED_SO_TRY_AGAIN_LATER);
            Log.e(getClass().getName(), null, e);
            finish();
        }
    }

    @UiThread
    public void setContentView() {
        if (formEngine != null) {
            setContentView(formEngine.getPageView());
            String formName = DynamicUtils.getFullFormName(formType);

            if (isLabTestForm) {
                setTitle(UtilBean.getMyLabel(LabelConstants.OPD_LAB_TEST));
                if (SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY) != null) {
                    setSubTitle(SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY));
                }
            } else if (SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY) != null) {
                setTitle(formName);
                setSubTitle(SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY));
            } else {
                if (formType.equals(FormConstants.FHS_MEMBER_UPDATE)) {
                    formName = UtilBean.getFullFormOfEntity().get(FormConstants.FHS_ADD_MEMBER);
                }
                setTitle(formName);
            }
            hideProcessDialog();
        }
    }

    private void loadQuestionsAndQuestion() {
        Log.d(TAG, "Form Title : " + formType + " is Loaded ...................");
        SharedStructureData.initIndexQuestionMap(this,
                sewaService,
                sewaFhsService,
                rchHighRiskService,
                immunisationService,
                ncdService,
                ncdScoreService,
                healthInfrastructureService,
                locationMasterService,
                schoolService,
                serviceRestClient,
                dailyNutritionLogService,
                npcbService,
                versionService);

        sewaService.loadQuestionsBySheet(formType);
    }

    @UiThread
    public void checkForGpsEnabledForms() {
        if (isLabTestForm || SharedStructureData.gpsEnabledForms.contains(formType)) {
            // fetch current location either by gps or network if gps is disabled
            if (!SharedStructureData.gps.isLocationProviderEnabled()) {
                // Ask user to enable GPS/network in settings
                SharedStructureData.gps.showSettingsAlert(context);
            } else {
                SharedStructureData.gps.getLocation();
                currentLatitude = String.valueOf(GPSTracker.latitude);
                currentLongitude = String.valueOf(GPSTracker.longitude);
                SharedStructureData.relatedPropertyHashTable.put("currentLatitude", currentLatitude);
                SharedStructureData.relatedPropertyHashTable.put("currentLongitude", currentLongitude);

                if (!PointInPolygon.coordinateInsidePolygon()) {
                    UtilBean.showAlertAndExit(GlobalTypes.MSG_GEO_FENCING_VIOLATION, this);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (formType != null && !formType.equalsIgnoreCase("DynamicForm")) {
            myDialog = new MyAlertDialog(DynamicFormActivity.this, LabelConstants.CLOSE_THE_REGISTRATION, this, DynamicUtils.BUTTON_YES_NO);
            myDialog.show();
        } else {
            processDialog = new MyProcessDialog(DynamicFormActivity.this, GlobalTypes.MSG_PROCESSING);
            processDialog.show();
            changeActivity();
            processDialog.dismiss();
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == BUTTON_POSITIVE) {
            if (SharedStructureData.ordtTimer != null) {
                SharedStructureData.ordtTimer.cancel();
            }
            isProperFinish = false;
            myDialog.dismiss();
            changeActivity();
            finish();
        } else if (id == BUTTON_NEGATIVE) {
            myDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult resultForQRScanner = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // Check first for location service
        if (requestCode == GlobalTypes.LOCATION_SERVICE_ACTIVITY) {
            if (!SharedStructureData.gps.isLocationProviderEnabled()) {
                runOnUiThread(() -> SharedStructureData.gps.showSettingsAlert(context));
            } else {
                SharedStructureData.gps.getLocation();
                currentLatitude = String.valueOf(GPSTracker.latitude);
                currentLongitude = String.valueOf(GPSTracker.longitude);
                SharedStructureData.relatedPropertyHashTable.put("currentLatitude", currentLatitude);
                SharedStructureData.relatedPropertyHashTable.put("currentLongitude", currentLongitude);

                if (!PointInPolygon.coordinateInsidePolygon()) {
                    UtilBean.showAlertAndExit(GlobalTypes.MSG_GEO_FENCING_VIOLATION, this);
                }
            }
            // Then check for Photo capture
        } else if (requestCode == GlobalTypes.PHOTO_CAPTURE_ACTIVITY) {
            try {
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap photo = (Bitmap) extras.get("data");
                        QueFormBean queFormBean = SharedStructureData.mapIndexQuestion.get(SharedStructureData.currentQuestion);
                        if (queFormBean != null) {

                            LinearLayout parentLayout = (LinearLayout) queFormBean.getQuestionTypeView();
                            ImageView takenImage = parentLayout.findViewById(GlobalTypes.PHOTO_CAPTURE_ACTIVITY);
                            takenImage.setVisibility(View.VISIBLE);
                            takenImage.setImageBitmap(photo);
                            String photoName = SewaTransformer.loginBean.getUsername() + "_" + SharedStructureData.currentQuestion + "_" + new Date().getTime() + GlobalTypes.IMAGE_CAPTURE_FORMAT;
                            queFormBean.setAnswer(photoName);
                            SewaUtil.generateToast(this, LabelConstants.PHOTO_CAPTURED);
                        }
                    }
                } else {
                    QueFormBean queFormBean = SharedStructureData.mapIndexQuestion.get(SharedStructureData.currentQuestion);
                    if (queFormBean != null) {
                        LinearLayout parentLayout = (LinearLayout) queFormBean.getQuestionTypeView();
                        ImageView takenImage = parentLayout.findViewById(GlobalTypes.PHOTO_CAPTURE_ACTIVITY);
                        takenImage.setVisibility(View.GONE);
                        queFormBean.setAnswer(null);
                    }
                    SewaUtil.generateToast(this, LabelConstants.FAILED_TO_TAKE_PHOTO);
                }
            } catch (Exception e) {
                Log.e(TAG, null, e);
            }
            // Then Check for QR scanner
        } else if (resultForQRScanner != null) {
            if (resultForQRScanner.getContents() == null) {
                SewaUtil.generateToast(this, LabelConstants.FAILED_TO_SCAN_QR);
            } else {
                //show dialogue with resultForQRScanner
                String scanningData = resultForQRScanner.getContents();
                QueFormBean queFormBean = SharedStructureData.mapIndexQuestion.get(SharedStructureData.currentQuestion);
                if (queFormBean != null) {
                    LinearLayout parentLayout = (LinearLayout) queFormBean.getQuestionTypeView();
                    TextView answerView = parentLayout.findViewById(GlobalTypes.QR_SCAN_ACTIVITY);

                    Log.i(TAG, "QR Scanner Data : " + scanningData);
                    try {
                        Map<String, String> aadharDetailsMap = AadharScanUtil.getAadharScanDataMap(scanningData);
                        answerView.setText(AadharScanUtil.getAadharTextToBeDisplayedAfterScan(aadharDetailsMap));
                        queFormBean.setAnswer(new Gson().toJson(aadharDetailsMap));
                        answerView.setVisibility(View.VISIBLE);
                    } catch (DataException e) {
                        Log.e(TAG, null, e);
                        answerView.setVisibility(View.GONE);
                        queFormBean.setAnswer(null);
                        // If Scanned data are not in xml format
                        SewaUtil.generateToast(this, LabelConstants.PLEASE_SCAN_AADHAAR);
                    }

                } else {
                    Log.e(TAG, "No Question found from structure having id " + SharedStructureData.currentQuestion);
                }
            }
        } else if (requestCode == ActivityConstants.OPENRDTREADER_TRAINING_ACTIVITY_REQUEST_CODE) {
            if (SharedStructureData.ordtTimer != null) {
                SharedStructureData.ordtTimer.cancel();
            }
            SharedStructureData.ordtTimer = new Timer();
            SharedStructureData.ordtTimer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            if (SharedStructureData.ordtIntent.hasExtra("trainingMode")) {
                                SharedStructureData.ordtIntent.removeExtra("trainingMode");
                            }
                            startActivityForResult(SharedStructureData.ordtIntent, ActivityConstants.OPENRDTREADER_ACTIVITY_REQUEST_CODE);
                        }
                    }, 30000
            );
        } else if (requestCode == ActivityConstants.OPENRDTREADER_ACTIVITY_REQUEST_CODE) {
            QueFormBean queFormBean = SharedStructureData.mapIndexQuestion.get(SharedStructureData.currentQuestion);
            if (queFormBean != null) {
                if (data != null) {
                    Map<String, String> result = new HashMap<>();
                    result.put("requestId", data.getStringExtra("requestId"));
                    result.put("timestamp", data.getStringExtra("timestamp"));
                    result.put("userObservedResult", data.getStringExtra("userObservedResult"));
                    result.put("capturedImageUri", data.getStringExtra("capturedImageUri"));
                    queFormBean.setAnswer(new Gson().toJson(result));
                    MyDynamicComponents.setORDTTestResult(context, queFormBean, result);
                } else {
                    MyDynamicComponents.resetORDPTestClick(queFormBean);
                }
            }
        } else if (requestCode == ActivityConstants.HEALTH_ID_MANAGEMENT_REQUEST_CODE) {
            QueFormBean queFormBean = SharedStructureData.mapIndexQuestion.get(SharedStructureData.currentQuestion);
            if (queFormBean != null) {
                if (data != null && data.getStringExtra(RelatedPropertyNameConstants.HEALTH_ID_NUMBER) != null) {
                    queFormBean.setAnswer(data.getStringExtra(RelatedPropertyNameConstants.HEALTH_ID_NUMBER));
                }
            }
        } else if (requestCode == ActivityConstants.OFFLINE_HEALTH_ID_MANAGEMENT_REQUEST_CODE) {
            QueFormBean queFormBean = SharedStructureData.mapIndexQuestion.get(SharedStructureData.currentQuestion);
            if (queFormBean != null) {
                if (data != null && data.getStringExtra(OfflineHealthIdBean.class.getSimpleName()) != null) {
                    queFormBean.setAnswer(data.getStringExtra(OfflineHealthIdBean.class.getSimpleName()));
                }
            }
        }
        // This is important, otherwise the resultForQRScanner will not be passed to the fragment
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // for reset the content view
        setContentView(new TextView(this));
    }

    @Background
    public void storeOpdLabTestForm(String answerStr) {
        runOnUiThread(() -> {
            processDialog = new MyProcessDialog(DynamicFormActivity.this, LabelConstants.SAVING_DATA);
            processDialog.show();
        });

        answerString = answerStr +
                "-1" +
                GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR +
                currentLongitude +
                GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR +
                "-2" +
                GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR +
                currentLatitude +
                GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR +
                "-8" +
                GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR +
                sharedPref.getString(RelatedPropertyNameConstants.FORM_START_DATE, null) +
                GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR +
                "-9" + GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR +
                new Date().getTime() +
                GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR;

        try {
            final String result = serviceRestClient.storeOpdLabFormDetails(answerString, labTestFormDetId, labTestVersion);
            if (result != null) {
                runOnUiThread(() -> SewaUtil.generateToast(DynamicFormActivity.this, LabelConstants.FORM_HAS_BEEN_SAVED_SUCCESSFULLY));
            }
            finishOpdLabTestForm(true);
        } catch (RestHttpException e) {
            hideProcessDialog();
            Log.e(TAG, null, e);

            runOnUiThread(() -> {
                View.OnClickListener listener = v -> {
                    if (v.getId() == BUTTON_POSITIVE) {
                        storeOpdLabTestForm(answerString);
                    } else {
                        finishOpdLabTestForm(false);
                    }
                };

                alertDialog = new MyAlertDialog(DynamicFormActivity.this, false,
                        LabelConstants.ERROR_OCCURRED_DURING_FORM_SUBMISSION,
                        listener,
                        DynamicUtils.BUTTON_RETRY_CANCEL);
                alertDialog.show();
            });
        }
    }

    private void finishOpdLabTestForm(boolean formSubmitted) {
        try {
            formEngine = null;
            PageFormBean.context = null;
            SharedStructureData.resetSharedStructureData();

            techoService.deleteQuestionAndAnswersByFormCode(formType);

            if (formSubmitted) {
                setResult(RESULT_OK);
            } else {
                setResult(RESULT_CANCELED);
            }
        } finally {
            super.finish();
        }
    }

    @UiThread
    public void onFormFillFinish(String answerString1, boolean isNavigate) {
        this.answerString = answerString1;
        this.isProperFinish = isNavigate;
        processDialog = new MyProcessDialog(this, LabelConstants.MSG_SAVING_DATA);
        processDialog.show();
        new Thread() {
            @Override
            public void run() {
                Calendar cal = Calendar.getInstance();
                Date endDate = new Date();
                Calendar durationInCal = Calendar.getInstance();
                durationInCal.setTimeInMillis(cal.getTimeInMillis() - SharedStructureData.formFillUpTime);
                SharedStructureData.formFillUpTime = endDate.getTime() - SharedStructureData.formFillUpTime;

                answerString = answerString
                        + "-8"
                        + GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR
                        + sharedPref.getString(RelatedPropertyNameConstants.FORM_START_DATE, null)
                        + GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR
                        + "-9"
                        + GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR
                        + new Date().getTime()
                        + GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR;

                DynamicUtils.storeForm(formType, answerString);

                try {
                    if (SharedStructureData.fhwSheets.contains(formType)) {
                        changeActivity();
                    } else {
                        finish();
                    }
                } catch (Exception e) {
                    Log.e(TAG, null, e);
                } finally {
                    hideProcessDialog();
                }
            }
        }.start();
    }

    @Override
    public void finish() {
        formEngine = null;
        PageFormBean.context = null;
        SharedStructureData.resetSharedStructureData();
        try {
            if (isProperFinish) {
                navigationFlowForActivity();
            }

            if (answerString != null) {
                setResult(RESULT_OK);
            } else {
                setResult(RESULT_CANCELED);
            }

            hideProcessDialog();
        } finally {
            super.finish();
        }
    }

    private void navigationFlowForActivity() {
        try {
            if (formType != null) {
                showMorbidity(formType);
            }
        } catch (Exception e) {
            SharedStructureData.sewaService.storeException(e, GlobalTypes.EXCEPTION_TYPE_DYNAMIC_FORM);
            Log.e(TAG, null, e);
        }
    }

    private void showMorbidity(String entity) {
        MorbiditiesConstant.morbidities = null;
        MorbiditiesConstant.nextEntity = null;

        String isPresent = SharedStructureData.relatedPropertyHashTable.get("isPresent");
        String status = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.MEMBER_STATUS);

        if (isPresent != null && isPresent.equalsIgnoreCase(GlobalTypes.TRUE)
                && status != null && !status.equals(RchConstants.MEMBER_STATUS_DEATH)) {
            if (entity.equalsIgnoreCase(FormConstants.ASHA_ANC)) {
                MorbiditiesConstant.morbidities = SewaUtil.showANCMorbidity(answerString);
                MorbiditiesConstant.nextEntity = FormConstants.ANC_MORBIDITY;
                startActivity(new Intent(this, DisplayMorbiditiesActivity.class));
            } else if (entity.equalsIgnoreCase(FormConstants.ASHA_CS)) {
                MorbiditiesConstant.morbidities = SewaUtil.showCCMorbidity(answerString);
                MorbiditiesConstant.nextEntity = FormConstants.CHILD_CARE_MORBIDITY;
                startActivity(new Intent(this, DisplayMorbiditiesActivity.class));
            } else if (entity.equalsIgnoreCase(FormConstants.ASHA_PNC)) {
                MorbiditiesConstant.morbidities = SewaUtil.showPNCMorbidity(answerString);
                MorbiditiesConstant.nextEntity = FormConstants.PNC_MORBIDITY;
                startActivity(new Intent(this, DisplayMorbiditiesActivity.class));
            }
        }
    }

    private void changeActivity() {
        String nextEntity = SharedStructureData.relatedPropertyHashTable.get("nextEntity");
        if (formType != null
                && (formType.equals(NotificationConstants.FHW_NOTIFICATION_LMP_FOLLOW_UP) || formType.equals(FormConstants.FHW_PREGNANCY_CONFIRMATION))
                && nextEntity != null && !nextEntity.equals("NO") && isProperFinish) {
            SharedStructureData.relatedPropertyHashTable.clear();
            sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            try {
                formMetaDataUtil.setMetaDataForRchFormByFormType(nextEntity, SharedStructureData.currentRchMemberBean.getActualId(),
                        SharedStructureData.currentRchMemberBean.getFamilyId(), null, sharedPref);
            } catch (DataException e) {
                View.OnClickListener listener = v -> {
                    alertDialog.dismiss();
                    navigateToHomeScreen(false);
                };
                alertDialog = new MyAlertDialog(this, false,
                        UtilBean.getMyLabel(LabelConstants.ERROR_TO_REFRESH_ALERT), listener, DynamicUtils.BUTTON_OK);
                alertDialog.show();
                return;
            }
            Intent myIntent = new Intent(this, DynamicFormActivity_.class);
            myIntent.putExtra(SewaConstants.ENTITY, nextEntity);
            myIntent.putExtra(SewaConstants.ALLOW_SECOND_FORM_SAME_MEMBER_OFFLINE, true);
            startActivity(myIntent);
            finish();
        } else if (formType != null && SharedStructureData.fhwSheets.contains(formType)) {
            if (formType.equalsIgnoreCase(FormConstants.LMP_FOLLOW_UP)
                    || formType.equalsIgnoreCase(FormConstants.TECHO_FHW_ANC)
                    || formType.equalsIgnoreCase(FormConstants.TECHO_FHW_PNC)
                    || formType.equalsIgnoreCase(FormConstants.TECHO_FHW_WPD)
                    || formType.equalsIgnoreCase(FormConstants.TECHO_FHW_CI)
                    || formType.equalsIgnoreCase(FormConstants.TECHO_FHW_CS)
                    || formType.equalsIgnoreCase(FormConstants.NCD_FHW_WEEKLY_CLINIC)
                    || formType.equalsIgnoreCase(FormConstants.NCD_FHW_WEEKLY_HOME)) {
                String memberStatus = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.MEMBER_STATUS);
                if (memberStatus != null && memberStatus.equals("MIGRATED")) {
                    sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor edit = sharedPref.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(SharedStructureData.currentRchMemberBean);
                    edit.putString("memberBean", json);
                    json = gson.toJson(SharedStructureData.currentRchFamilyDataBean);
                    edit.putString("familyDataBean", json);
                    edit.apply();
                    Intent myIntent = new Intent(this, MigrateOutActivity_.class);
                    startActivity(myIntent);
                }
            } else if (formType.equalsIgnoreCase(FormConstants.FHS_MEMBER_UPDATE)) {
                String memberStatus = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.MEMBER_STATUS);
                if (memberStatus != null && memberStatus.equals("MIGRATED")) {
                    sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor edit = sharedPref.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(new MemberBean(SharedStructureData.currentMemberDataBean));
                    edit.putString("memberBean", json);
                    json = gson.toJson(SharedStructureData.currentFamilyDataBean);
                    edit.putString("familyDataBean", json);
                    edit.apply();
                    Intent myIntent = new Intent(this, MigrateOutActivity_.class);
                    startActivity(myIntent);
                }
            }
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (formEngine != null) {
                formEngine.backButtonClicked();
            }
        } else if (item.getItemId() == R.id.menu_home) {
            View.OnClickListener myListener = v -> {
                if (v.getId() == BUTTON_POSITIVE) {
                    alertDialog.dismiss();
                    isProperFinish = false;
                    navigateToHomeScreen(false);
                    finish();
                } else {
                    alertDialog.dismiss();
                }
            };

            alertDialog = new MyAlertDialog(this,
                    LabelConstants.WANT_TO_GO_BACK_TO_HOME_SCREEN,
                    myListener, DynamicUtils.BUTTON_YES_NO);
            alertDialog.show();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public FormEngine getFormEngine() {
        return formEngine;
    }

    @UiThread
    public void showToaster(String msg) {
        SewaUtil.generateToast(context, msg);
    }
}
