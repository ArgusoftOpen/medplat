package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.FullFormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceRestClientImpl;
import com.argusoft.sewa.android.app.databean.QueryMobDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.restclient.RestHttpException;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

@EActivity
public class CbacDetailsActivity extends MenuActivity implements View.OnClickListener {

    @Bean
    public SewaServiceRestClientImpl restClient;
    @Bean
    public SewaServiceImpl sewaService;

    private static final String MEMBER_DETAILS_SCREEN = "memberDetailsScreen";
    private static final String RISK_ASSESSMENT = "riskAssessment";
    private static final String EARLY_DETECTION_1 = "earlyDetection1";
    private static final String EARLY_DETECTION_2 = "earlyDetection2";
    private static final String EARLY_DETECTION_3 = "earlyDetection3";
    private static final String EARLY_DETECTION_4 = "earlyDetection4";
    private static final String EARLY_DETECTION_5 = "earlyDetection5";
    private static final String PERSONAL_HISTORY_1 = "personalHistory1";
    private static final String PERSONAL_HISTORY_2 = "personalHistory2";
    private static final String PERSONAL_HISTORY_3 = "personalHistory3";
    private static final String PERSONAL_EXAMINATION = "personalExamination";
    private static final String CBAC_EDIT_DETAILS = "cbacEditDetails";
    private static final Integer RADIO_BUTTON_ID_YES = 501;
    private static final Integer RADIO_BUTTON_ID_NO = 502;

    private static final String MEMBER_ID = "memberId";
    private static final String MEMBER_NAME = "memberName";
    private static final String UNIQUE_HEALTH_ID = "uniqueHealthId";
    private static final String ADDRESS = "address";
    private static final String MOBILE_NUMBER = "mobileNumber";
    private static final String WAIST = "waist";
    private static final String SMOKE_OR_CONSUME_GUTKA = "smoke_or_consume_gutka";
    private static final String DIAGNOSED_FOR_HYPERTENSION = "diagnosed_for_hypertension";
    private static final String DIAGNOSED_FOR_DIABETES = "diagnosed_for_diabetes";
    private static final String DIAGNOSED_FOR_HEART_DISEASES = "diagnosed_for_heart_diseases";
    private static final String DIAGNOSED_FOR_STROKE = "diagnosed_for_stroke";
    private static final String DIAGNOSED_FOR_KIDNEY_FAILURE = "diagnosed_for_kidney_failure";
    private static final String DIAGNOSED_FOR_NON_HEALING_WOUND = "diagnosed_for_non_healing_wound";
    private static final String DIAGNOSED_FOR_COPD = "diagnosed_for_copd";
    private static final String DIAGNOSED_FOR_ASTHAMA = "diagnosed_for_asthama";
    private static final String DIAGNOSED_FOR_ORAL_CANCER = "diagnosed_for_oral_cancer";
    private static final String DIAGNOSED_FOR_BREAST_CANCER = "diagnosed_for_breast_cancer";
    private static final String DIAGNOSED_FOR_CERVICAL_CANCER = "diagnosed_for_cervical_cancer";
    private static final String HEIGHT = "height";
    private static final String WEIGHT = "weight";
    private static final String GENDER = "gender";

    private LinearLayout bodyLayoutContainer;
    private RadioGroup radioGroup;
    private boolean isSelectedMemberMale;
    private boolean isSelectedMemberFemale;
    private String selectedScreen = MEMBER_DETAILS_SCREEN;
    private Long selectedMemberId;
    private LinkedHashMap<String, Object> selectedMemberCbacDetails;
    private LinearLayout globalPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        String memberId = getIntent().getStringExtra(MEMBER_ID);
        if (memberId != null) {
            selectedMemberId = Long.parseLong(memberId);
        }
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
        setTitle(UtilBean.getTitleText(LabelConstants.NCD_CBAC_DETAILS_ACTIVITY));
        setSubTitle(null);
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        setBodyDetail();
    }

    @Background
    public void setBodyDetail() {
        if (selectedMemberId != null) {
            if (sewaService.isOnline()) {
                retrieveCbacDetailFromServer();
            } else {
                showNotOnlineMessage();
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.ERROR_OCCURRED_SO_TRY_AGAIN));
            hideProcessDialog();
        }

    }

    private void showNotOnlineMessage() {
        if (!sewaService.isOnline()) {
            runOnUiThread(() -> {
                View.OnClickListener myListener = v -> {
                    alertDialog.dismiss();
                    setResult(RESULT_CANCELED);
                    finish();
                };
                alertDialog = new MyAlertDialog(context, false,
                        UtilBean.getMyLabel(LabelConstants.NETWORK_CONNECTIVITY_ALERT),
                        myListener, DynamicUtils.BUTTON_OK);
                alertDialog.show();
            });
        }
    }

    @Background
    public void retrieveCbacDetailFromServer() {
        LinkedHashMap<String, Object> parameter = new LinkedHashMap<>();
        parameter.put(MEMBER_ID, selectedMemberId);
        QueryMobDataBean queryMobDataBean = new QueryMobDataBean();
        queryMobDataBean.setCode("mob_ncd_cbac_details_by_member_id");
        queryMobDataBean.setParameters(parameter);
        try {
            List<LinkedHashMap<String, Object>> result = restClient.executeQuery(queryMobDataBean).getResult();
            if (result != null && !result.isEmpty()) {
                selectedMemberCbacDetails = result.get(0);
                setSubTitle(UtilBean.getNotAvailableIfNull(selectedMemberCbacDetails.get(MEMBER_NAME)));
                if (GlobalTypes.MALE.equals(selectedMemberCbacDetails.get(GENDER))) {
                    isSelectedMemberMale = true;
                } else if (GlobalTypes.FEMALE.equals(selectedMemberCbacDetails.get(GENDER))) {
                    isSelectedMemberFemale = true;
                }
                if (getIntent().getStringExtra("navigationTo") != null) {
                    switch (getIntent().getStringExtra("navigationTo")) {
                        case MEMBER_DETAILS_SCREEN:
                            setMemberDetailsScreen();
                            break;
                        case RISK_ASSESSMENT:
                            setRiskAssessmentScreen();
                            break;
                        case EARLY_DETECTION_1:
                            setEarlyDetection1Screen();
                            break;
                        case EARLY_DETECTION_2:
                            setEarlyDetection2Screen();
                            break;
                        case EARLY_DETECTION_3:
                            setEarlyDetection3Screen();
                            break;
                        case EARLY_DETECTION_4:
                            setEarlyDetection4Screen();
                            break;
                        case EARLY_DETECTION_5:
                            setEarlyDetection5Screen();
                            break;
//                        case PERSONAL_HISTORY_1:
//                            setPersonalHistory1Screen();
//                            break;
//                        case PERSONAL_HISTORY_2:
//                            setPersonalHistory2Screen();
//                            break;
//                        case PERSONAL_HISTORY_3:
//                            setPersonalHistory3Screen();
//                            break;
//                        case PERSONAL_EXAMINATION:
//                            setPersonalExaminationScreen();
//                            break;
                        case CBAC_EDIT_DETAILS:
                            setCbacEditDetailsScreen();
                            break;
                        default:
                    }
                } else {
                    setMemberDetailsScreen();
                }
            } else {
                runOnUiThread(() -> SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.DATA_NOT_SYNCED_ALERT)));

                setResult(RESULT_CANCELED);
                finish();
            }
        } catch (RestHttpException e) {
            Log.e("CbacDetailsActivity", null, e);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (selectedScreen) {
                case MEMBER_DETAILS_SCREEN:
                    setRiskAssessmentScreen();
                    break;
                case RISK_ASSESSMENT:
                    setEarlyDetection1Screen();
                    break;
                case EARLY_DETECTION_1:
                    setEarlyDetection2Screen();
                    break;
                case EARLY_DETECTION_2:
                    if (isSelectedMemberFemale) {
                        setEarlyDetection3Screen();
                    } else if (isSelectedMemberMale) {
                        setPersonalHistory1Screen();
                    }
                    break;
                case EARLY_DETECTION_3:
                    setEarlyDetection4Screen();
                    break;
                case EARLY_DETECTION_4:
                    if (isSelectedMemberFemale) {
                        setEarlyDetection5Screen();
                    } else if (isSelectedMemberMale) {
                        setCbacEditDetailsScreen();
                    }
                    break;
                case EARLY_DETECTION_5:
                    setCbacEditDetailsScreen();
                    break;
//                case PERSONAL_HISTORY_1:
//                    setPersonalHistory2Screen();
//                    break;
//                case PERSONAL_HISTORY_2:
//                    setPersonalHistory3Screen();
//                    break;
//                case PERSONAL_HISTORY_3:
//                    setPersonalExaminationScreen();
//                    break;
//                case PERSONAL_EXAMINATION:
//                    setCbacEditDetailsScreen();
//                    break;
                case CBAC_EDIT_DETAILS:
                    if (radioGroup.getCheckedRadioButtonId() != -1) {
                        if (radioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                            setSharedStructureData();
                        } else {
                            setResult(RESULT_OK);
                            this.finish();
                        }
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_AN_OPTION));
                    }
                    break;
                default:
            }
        }
    }

    @UiThread
    public void setMemberDetailsScreen() {
        selectedScreen = MEMBER_DETAILS_SCREEN;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(this, LabelConstants.PERSONAL_INFORMATION));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.UNIQUE_HEALTH_ID));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberCbacDetails.get(UNIQUE_HEALTH_ID))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.FAMILY_ID)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberCbacDetails.get("familyId"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.MEMBER_NAME)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberCbacDetails.get(MEMBER_NAME))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.AGE)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberCbacDetails.get("age"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.GENDER)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberCbacDetails.get(GENDER))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.ADDRESS)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberCbacDetails.get(ADDRESS))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.MOBILE_NUMBER)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberCbacDetails.get(MOBILE_NUMBER))));
        hideProcessDialog();
    }

    @UiThread
    public void setRiskAssessmentScreen() {
        selectedScreen = RISK_ASSESSMENT;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(this, LabelConstants.RISK_ASSESSMENT));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.HAVING_ANY_ADDICTION_OR_SMOKING)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                FullFormConstants.getFullFormOfCBACConstants(UtilBean.getNotAvailableIfNull(selectedMemberCbacDetails.get(SMOKE_OR_CONSUME_GUTKA)))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.CONSUME_ALCOHOL_DAILY)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("consume_alcohol_daily"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.MEASUREMENT_OF_WAIST)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                FullFormConstants.getFullFormOfCBACConstants(UtilBean.getNotAvailableIfNull(selectedMemberCbacDetails.get(WAIST)))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.TAKING_PART_IN_ANY_PHYSICAL_ACTIVITY_150_MINUTS_IN_A_WEEK)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                FullFormConstants.getFullFormOfCBACConstants(UtilBean.getNotAvailableIfNull(selectedMemberCbacDetails.get(SMOKE_OR_CONSUME_GUTKA)))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.HAVE_A_FAMILY_HISTORY_WITH_BP_DIABETES_OR_HEART_DISEASES)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("bp_diabetes_heart_history"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.CBAC_SCORE)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.getNotAvailableIfNull(selectedMemberCbacDetails.get("score"))));
        hideProcessDialog();
    }

    @UiThread
    public void setEarlyDetection1Screen() {
        selectedScreen = EARLY_DETECTION_1;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(this, LabelConstants.EARLY_DETECTION));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.RECURRENT_ULCERATION));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("recurrent_ulceration"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.RECURRENT_TINGLING)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("recurrent_tingling"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.CLOUDY_VISION)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("cloudy_vision"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.READING_DIFFICULTY)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("reading_difficluty"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.EYE_PAIN)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("eye_pain"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.EYE_REDNESS)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("eye_redness"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.HEARING_DIFFICULTY)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("hearing_difficulty"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.CHEWING_PAIN)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("chewing_pain"))));
        hideProcessDialog();
    }

    @UiThread
    public void setEarlyDetection2Screen() {
        selectedScreen = EARLY_DETECTION_2;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(this, LabelConstants.EARLY_DETECTION));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.SORTNESS_OF_BREATH));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("shortness_of_breath"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.COUGHING_MORE_THAN_2_WEEKS)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("two_weeks_coughing"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.BLOOD_IN_SPUTUM)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("blood_in_sputum"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.FEVER_GT_2_WEEKS)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("two_weeks_fever"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.LOSS_OF_WEIGHT)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("loss_of_weight"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.NIGHT_SWEETS)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("night_sweats"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.TAKING_ANY_ANTI_TB_DRUGS)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("taking_anti_tb_drugs"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.ANYONE_IN_FAMILY_SUFFERING_FROM_TB)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("family_member_suffering_from_tb"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.HISTORY_OF_TB)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("history_of_tb"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.HISTORY_OF_FITS)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("fits_history"))));

        hideProcessDialog();
    }

    @UiThread
    public void setEarlyDetection3Screen() {
        selectedScreen = EARLY_DETECTION_3;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(this, LabelConstants.EARLY_DETECTION));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.DIFFICULTY_IN_OPENING_MOUTH)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("mouth_opening_difficulty"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.ULCERS_IN_MOUTH_NOT_HEALDED_IN_2_WEEKS)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("mouth_ulcers"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.GROWTH_IN_MOUTH_NOT_HEALDED_IN_2_WEEKS)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("two_weeks_ulcers_in_mouth"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.PATCH_IN_MOUTH_NOT_HEALDED_IN_2_WEEKS)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("mouth_patch"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.ANY_CHANGES_IN_TONE_OF_VOICE)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("change_in_tone_of_voice"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.ANY_PATCH_ON_SKIN)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("patch_on_skin"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.DIFFICULTY_IN_HOLDING_OBJECTS_WITH_FINGURES)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("difficulty_holding_objects"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.LOSS_OF_SENSATION_IN_PALM)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("sensation_loss_palm"))));

        hideProcessDialog();
    }

    @UiThread
    public void setEarlyDetection4Screen() {

        selectedScreen = EARLY_DETECTION_4;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(this, LabelConstants.EARLY_DETECTION));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.THICKENED_SKIN));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("thick_skin"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.NODULES_ON_SKIN)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("nodules_on_skin"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.CLAWING_OF_FINGERS)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("clawing_of_fingers"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.TINGLING_IN_HANDS)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("tingling_in_hand"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.INABILITY_TO_CLOSE_EYELID)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("inability_close_eyelid"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.WEAK_FEET)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("feet_weakness"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.CROP_BURNING)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("crop_residue_burning"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.BURNING_OF_GARBAGE)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("garbage_burning"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.WORKING_IN_INDUSTRY_WITH_SMOKE_GAS)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("working_industry"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.INTEREST_OR_PLEASURE)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                FullFormConstants.getFullFormOfCBACConstants(
                        UtilBean.getNotAvailableIfNull(selectedMemberCbacDetails.get("interest_doing_things")))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.FEELING_DEPRESSED)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                FullFormConstants.getFullFormOfCBACConstants(
                        UtilBean.getNotAvailableIfNull(selectedMemberCbacDetails.get("feeling_down")))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.FEELING_UNSTEADY)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("feeling_unsteady"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.PHYSICAL_DISABILITY)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("physical_disability"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.NEED_HELP_FROM_OTHERS)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("need_help_from_others"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.FORGET_NAMES)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("forget_names"))));

        hideProcessDialog();
    }

    @UiThread
    public void setEarlyDetection5Screen() {
        selectedScreen = EARLY_DETECTION_5;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(this, LabelConstants.EARLY_DETECTION));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.LUMP_IN_BREAST));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("lump_in_breast"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.NIPPLE_BLOOD_STAINED_DISCHARGE)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("nipple_blood_stained_discharge"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.CHANGE_IN_SIZE_OF_BREAST)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("change_in_size_of_breast"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.BLEEDING_BETWEEN_PERIODS)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("bleeding_between_periods"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.BLEEDING_AFTER_MENOPAUSE)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("bleeding_after_menopause"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.BLEEDING_AFTER_INTERCOURSE)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("bleeding_after_intercourse"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.FOUL_VAGINAL_DISCHARGE)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("foul_vaginal_discharge"))));
        hideProcessDialog();
    }

    @UiThread
    public void setPersonalHistory1Screen() {
        selectedScreen = PERSONAL_HISTORY_1;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(this, LabelConstants.PERSONAL_HISTORY));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_HYPERTENSION));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_HYPERTENSION))));

        if (Boolean.TRUE.equals(selectedMemberCbacDetails.get(DIAGNOSED_FOR_HYPERTENSION))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.UNDER_TREATMENT_FOR_HYPERTENSION));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_hypertension"))));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_DIABETES));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_DIABETES))));

        if (Boolean.TRUE.equals(selectedMemberCbacDetails.get(DIAGNOSED_FOR_DIABETES))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.UNDER_TREATMENT_FOR_DIABETES));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_diabetes"))));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_HEART_DISEASES));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_HEART_DISEASES))));

        if (Boolean.TRUE.equals(selectedMemberCbacDetails.get(DIAGNOSED_FOR_HEART_DISEASES))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    LabelConstants.UNDER_TREATMENT_FOR_HEART_DISEASES));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_heart_diseases"))));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_STROKE));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_STROKE))));

        if (Boolean.TRUE.equals(selectedMemberCbacDetails.get(DIAGNOSED_FOR_STROKE))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    LabelConstants.UNDER_TREATMENT_FOR_STROKE));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_stroke"))));
        }
        hideProcessDialog();
    }

    @UiThread
    public void setPersonalHistory2Screen() {
        selectedScreen = PERSONAL_HISTORY_2;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(this, LabelConstants.PERSONAL_HISTORY));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_KIDNEY_FAILURE));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_KIDNEY_FAILURE))));

        if (Boolean.TRUE.equals(selectedMemberCbacDetails.get(DIAGNOSED_FOR_KIDNEY_FAILURE))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    LabelConstants.UNDER_TREATMENT_KIDNEY_FAILURE));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_kidney_failure"))));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_NON_HEALING_WOUND));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_NON_HEALING_WOUND))));

        if (Boolean.TRUE.equals(selectedMemberCbacDetails.get(DIAGNOSED_FOR_NON_HEALING_WOUND))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    LabelConstants.UNDER_TREATMENT_FOR_NON_HEALING_WOUND));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_non_healing_wound"))));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_COPD));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_COPD))));

        if (Boolean.TRUE.equals(selectedMemberCbacDetails.get(DIAGNOSED_FOR_COPD))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.UNDER_TREATMENT_FOR_COPD));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_copd"))));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_ASTHAMA));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_ASTHAMA))));

        if (Boolean.TRUE.equals(selectedMemberCbacDetails.get(DIAGNOSED_FOR_ASTHAMA))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    LabelConstants.UNDER_TREATMENT_FOR_ASTHAMA));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_asthama"))));
        }
        hideProcessDialog();
    }

    @UiThread
    public void setPersonalHistory3Screen() {
        selectedScreen = PERSONAL_HISTORY_3;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(this, LabelConstants.PERSONAL_HISTORY));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_ORAL_CANCER));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_ORAL_CANCER))));

        if (Boolean.TRUE.equals(selectedMemberCbacDetails.get(DIAGNOSED_FOR_ORAL_CANCER))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.UNDER_TREATMENT_FOR_ORAL_CANCER));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_oral_cancer"))));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_BREAST_CANCER));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_BREAST_CANCER))));

        if (Boolean.TRUE.equals(selectedMemberCbacDetails.get(DIAGNOSED_FOR_BREAST_CANCER))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    LabelConstants.UNDER_TREATMENT_FOR_BREAST_CANCER));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_breast_cancer"))));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_CERVICAL_CANCER));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_CERVICAL_CANCER))));

        if (Boolean.TRUE.equals(selectedMemberCbacDetails.get(DIAGNOSED_FOR_CERVICAL_CANCER))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    LabelConstants.UNDER_TREATMENT_FOR_CERVICAL_CANCER));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_cervical_cancer"))));
        }
        hideProcessDialog();
    }

    @UiThread
    public void setPersonalExaminationScreen() {
        selectedScreen = PERSONAL_EXAMINATION;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(this, LabelConstants.PERSONAL_EXAMINATION));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.HEIGHT_IN_CMS));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberCbacDetails.get(HEIGHT))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.WEIGHT_IN_KGS));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberCbacDetails.get(WEIGHT))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.BODY_MASS_INDEX));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberCbacDetails.get("bmi"))));
        hideProcessDialog();
    }

    @UiThread
    public void setCbacEditDetailsScreen() {
        bodyLayoutContainer.removeAllViews();
        selectedScreen = CBAC_EDIT_DETAILS;

        if (radioGroup != null) {
            radioGroup.removeAllViews();
            radioGroup.clearCheck();
        } else {
            radioGroup = new RadioGroup(this);
        }
        radioGroup.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.CONFORMATION_TO_EDIT_CBAC_DETAILS)));
        radioGroup.addView(MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel(LabelConstants.YES), RADIO_BUTTON_ID_YES));
        radioGroup.addView(MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel(LabelConstants.NO), RADIO_BUTTON_ID_NO));

        bodyLayoutContainer.addView(radioGroup);
        hideProcessDialog();
    }

    private void setSharedStructureData() {
        showProcessDialog();
        SharedStructureData.relatedPropertyHashTable.clear();
        Object dataObject = selectedMemberCbacDetails.get(MEMBER_ID);
        if (dataObject != null) {
            SharedStructureData.relatedPropertyHashTable.put(MEMBER_ID, dataObject.toString());
            SharedStructureData.relatedPropertyHashTable.put("memberActualId", dataObject.toString());
        }
        dataObject = selectedMemberCbacDetails.get(UNIQUE_HEALTH_ID);
        if (dataObject != null) {
            SharedStructureData.relatedPropertyHashTable.put(UNIQUE_HEALTH_ID, dataObject.toString());
        }
        dataObject = selectedMemberCbacDetails.get(MEMBER_NAME);
        if (dataObject != null) {
            SharedStructureData.relatedPropertyHashTable.put(MEMBER_NAME, dataObject.toString());
            SharedStructureData.relatedPropertyHashTable.put("nameOfBeneficiary", dataObject.toString());
        }
        if (isSelectedMemberFemale) {
            SharedStructureData.relatedPropertyHashTable.put(GENDER, LabelConstants.FEMALE);
        } else if (isSelectedMemberMale) {
            SharedStructureData.relatedPropertyHashTable.put(GENDER, LabelConstants.MALE);
        }
        setSharedStructureData(MOBILE_NUMBER, selectedMemberCbacDetails.get(MOBILE_NUMBER));
        dataObject = selectedMemberCbacDetails.get("dob");
        setSharedStructureData("dobDisplay", dataObject);
        if (dataObject != null) {
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dataObject.toString());
            } catch (ParseException e) {
                Log.e("CbacDetailsActivity", null, e);
            }
            if (date != null) {
                int[] yearMonthDayAge = UtilBean.calculateAgeYearMonthDay(date.getTime());
                setSharedStructureData("ageDisplay", UtilBean.getAgeDisplay(yearMonthDayAge[0], yearMonthDayAge[1], yearMonthDayAge[2]));
            }
        }

        setSharedStructureData("familyActualId", selectedMemberCbacDetails.get("fid"));

        setSharedStructureData("typeOfHouse", selectedMemberCbacDetails.get("typeOfHouse"));
        setSharedStructureData("toiletAvailability", selectedMemberCbacDetails.get("typeOfToilet"));
        setSharedStructureData("electricConnection", selectedMemberCbacDetails.get("electricityAvailability"));
        setSharedStructureData("drinkingWaterSource", selectedMemberCbacDetails.get("drinkingWaterSource"));
        setSharedStructureData("typeOfFuel", selectedMemberCbacDetails.get("fuelForCooking"));
        setSharedStructureData("houseOwnership", selectedMemberCbacDetails.get("houseOwnershipStatus"));
        setSharedStructureData("annualIncome", selectedMemberCbacDetails.get("annualIncome"));
        setSharedStructureData(ADDRESS, selectedMemberCbacDetails.get(ADDRESS));
        SharedStructureData.relatedPropertyHashTable.put("anyMemberCbacDone", "1");

        setSharedStructureData("smokeOrConsumeGutka", selectedMemberCbacDetails.get(SMOKE_OR_CONSUME_GUTKA));
        setSharedStructureData("consumeAlcoholDaily", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("consume_alcohol_daily")));
        setSharedStructureData(WAIST, selectedMemberCbacDetails.get(WAIST));
        setSharedStructureData("physicalActivity150Min", selectedMemberCbacDetails.get("physical_activity_150_min"));
        setSharedStructureData("bpDiabetesHeartHistory", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("bp_diabetes_heart_history")));
        setSharedStructureData("shortnessOfBreathe", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("shortness_of_breath")));
        setSharedStructureData("coughingMoreThan2Weeks", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("two_weeks_coughing")));
        setSharedStructureData("bloodInSputum", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("blood_in_sputum")));
        setSharedStructureData("fever2weeks", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("two_weeks_fever")));
        setSharedStructureData("weightLoss", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("loss_of_weight")));
        setSharedStructureData("nightSweats", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("night_sweats")));
        setSharedStructureData("currentlyTakingAntiTBDrugs", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("taking_anti_tb_drugs")));
        setSharedStructureData("AnyoneInFamilyCurrentTB", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("family_member_suffering_from_tb")));
        setSharedStructureData("historyOfTb", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("history_of_tb")));
        setSharedStructureData("fitsHistory", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("fits_history")));
        setSharedStructureData("difficultyInOpeningMouth", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("mouth_opening_difficulty")));
        setSharedStructureData("ulcerPatchGrowth", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("two_weeks_ulcers_in_mouth")));
        setSharedStructureData("voiceToneChange", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("change_in_tone_of_voice")));
        setSharedStructureData("skinDiscoloration", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("patch_on_skin")));
        setSharedStructureData("difficultyHoldingObjects", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("difficulty_holding_objects")));
        setSharedStructureData("sensationLossPalm", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("sensation_loss_palm")));
        setSharedStructureData("lumpInBreast", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("lump_in_breast")));
        setSharedStructureData("nippleBloodStainedDischarge", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("nipple_blood_stained_discharge")));
        setSharedStructureData("changeInSizeOfBreast", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("change_in_size_of_breast")));
        setSharedStructureData("bleedingBetweenPeriods", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("bleeding_between_periods")));
        setSharedStructureData("bleedingAfterMenopause", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("bleeding_after_menopause")));
        setSharedStructureData("bleedingAfterIntercourse", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("bleeding_after_intercourse")));
        setSharedStructureData("foulVaginalDischarge", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("foul_vaginal_discharge")));
        setSharedStructureData("occupationalExposure", selectedMemberCbacDetails.get("occupational_exposure"));
        setSharedStructureData("ageAtMenarche", selectedMemberCbacDetails.get("age_at_menarche"));
        setSharedStructureData("menopauseArrived", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("menopause_arrived")));
        setSharedStructureData("durationOfMenopause", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("duration_of_menopause")));
        setSharedStructureData("pregnant", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("pregnant")));
        setSharedStructureData("lactating", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("lactating")));
        setSharedStructureData("regularPeriods", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("regular_periods")));
        setSharedStructureData("bleeding", selectedMemberCbacDetails.get("bleeding"));
        setSharedStructureData("associatedWith", selectedMemberCbacDetails.get("associated_with"));
        setSharedStructureData("remarks", selectedMemberCbacDetails.get("remarks"));
        setSharedStructureData("diagnosedForHypertension", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_HYPERTENSION)));
        setSharedStructureData("underTreatmentForHypertension", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_hypertension")));
        setSharedStructureData("diagnosedForDiabetes", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_DIABETES)));
        setSharedStructureData("underTreatmentForDiabetes", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_diabetes")));
        setSharedStructureData("diagnosedForHeartDisease", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_HEART_DISEASES)));
        setSharedStructureData("underTreatmentForHeartDisease", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_heart_diseases")));
        setSharedStructureData("diagnosedForStroke", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_STROKE)));
        setSharedStructureData("underTreatmentForStroke", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_stroke")));
        setSharedStructureData("diagnosedForKidneyFailure", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_KIDNEY_FAILURE)));
        setSharedStructureData("underTreatmentForKidneyFailure", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_kidney_failure")));
        setSharedStructureData("diagnosedForNonHealingWound", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_NON_HEALING_WOUND)));
        setSharedStructureData("underTreatmentForNonHealingWound", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_non_healing_wound")));
        setSharedStructureData("diagnosedForCopd", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_COPD)));
        setSharedStructureData("underTreatmentForCopd", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_copd")));
        setSharedStructureData("diagnosedForAsthama", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_ASTHAMA)));
        setSharedStructureData("underTreatmentForAsthama", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_asthama")));
        setSharedStructureData("diagnosedForOralCancer", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_ORAL_CANCER)));
        setSharedStructureData("underTreatmentForOralCancer", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_oral_cancer")));
        setSharedStructureData("diagnosedForBreastCancer", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_BREAST_CANCER)));
        setSharedStructureData("underTreatmentForBreastCancer", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_breast_cancer")));
        setSharedStructureData("diagnosedForCervicalCancer", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get(DIAGNOSED_FOR_CERVICAL_CANCER)));
        setSharedStructureData("underTreatmentForCervicalCancer", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("under_treatement_for_cervical_cancer")));
        setSharedStructureData(HEIGHT, selectedMemberCbacDetails.get(HEIGHT));
        setSharedStructureData(WEIGHT, selectedMemberCbacDetails.get(WEIGHT));

        Object doneOn = selectedMemberCbacDetails.get("serviceDate");
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).parse(doneOn.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setSharedStructureData("serviceDate", date.getTime());
        setSharedStructureData("painInChewing", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("chewing_pain")));
        setSharedStructureData("difficultyInHearing", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("hearing_difficulty")));
        setSharedStructureData("eyeRedness", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("eye_redness")));
        setSharedStructureData("eyePain", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("eye_pain")));
        setSharedStructureData("difficultyInReading", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("reading_difficluty")));
        setSharedStructureData("cloudyVision", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("cloudy_vision")));
        setSharedStructureData("recurrentTingling", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("recurrent_tingling")));
        setSharedStructureData("recurrentUlceration", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("recurrent_ulceration")));
        setSharedStructureData("anyMouthUlcers", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("mouth_ulcers")));
        setSharedStructureData("anyPatch", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("mouth_patch")));
        setSharedStructureData("thickenedSkin", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("thick_skin")));
        setSharedStructureData("nodulesOnSkin", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("nodules_on_skin")));
        setSharedStructureData("clawingOfFingers", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("clawing_of_fingers")));
        setSharedStructureData("tinglingOrNumbness", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("tingling_in_hand")));
        setSharedStructureData("inabilityToCloseEyelid", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("inability_close_eyelid")));
        setSharedStructureData("difficultyWalking", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("feet_weakness")));
        setSharedStructureData("lumpInBreast", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("lump_in_breast")));
        setSharedStructureData("nippleBloodStainedDischarge", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("nipple_blood_stained_discharge")));
        setSharedStructureData("changeInSizeOfBreast", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("change_in_size_of_breast")));
        setSharedStructureData("bleedingBetweenPeriods", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("bleeding_between_periods")));
        setSharedStructureData("bleedingAfterMenopause", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("bleeding_after_menopause")));
        setSharedStructureData("bleedingAfterIntercourse", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("bleeding_after_intercourse")));
        setSharedStructureData("foulVaginalDischarge", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("foul_vaginal_discharge")));
        setSharedStructureData("cropResidue", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("crop_residue_burning")));
        setSharedStructureData("garbageLeaves", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("garbage_burning")));
        setSharedStructureData("workingInIndustry", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("working_industry")));
        setSharedStructureData("interestDoingThings", selectedMemberCbacDetails.get("interest_doing_things"));
        setSharedStructureData("feelingDepressed", selectedMemberCbacDetails.get("feeling_down"));
        setSharedStructureData("feelingUnsteady", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("feeling_unsteady")));
        setSharedStructureData("physicalDisability", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("physical_disability")));
        setSharedStructureData("needHelpFromOthers", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("need_help_from_others")));
        setSharedStructureData("forgetNames", UtilBean.returnKeyFromBoolean(selectedMemberCbacDetails.get("forget_names")));


        String formType = FormConstants.NCD_ASHA_CBAC;
        Intent myIntent = new Intent(this, DynamicFormActivity_.class);
        myIntent.putExtra(SewaConstants.ENTITY, formType);
        startActivityForResult(myIntent, 200);
        hideProcessDialog();
    }

    private void setSharedStructureData(String key, Object value) {
        if (value != null) {
            SharedStructureData.relatedPropertyHashTable.put(key, value.toString());
        }
    }

    @Override
    public void onBackPressed() {
        View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                alertDialog.dismiss();
                finish();
            } else {
                alertDialog.dismiss();
            }
        };

        alertDialog = new MyAlertDialog(this,
                LabelConstants.CONFORMATION_TO_CANCEL_VIEWING_CBAC_DETAILS,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (selectedScreen == null || selectedScreen.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            switch (selectedScreen) {
                case MEMBER_DETAILS_SCREEN:
                    setResult(RESULT_CANCELED);
                    this.finish();
                    break;
                case RISK_ASSESSMENT:
                    setMemberDetailsScreen();
                    break;
                case EARLY_DETECTION_1:
                    setRiskAssessmentScreen();
                    break;
                case EARLY_DETECTION_2:
                    setEarlyDetection1Screen();
                    break;
                case EARLY_DETECTION_3:
                    setEarlyDetection2Screen();
                    break;
                case EARLY_DETECTION_4:
                    setEarlyDetection3Screen();
                    break;
                case EARLY_DETECTION_5:
                    setEarlyDetection4Screen();
                    break;
//                case PERSONAL_HISTORY_1:
//                    if (isSelectedMemberFemale) {
//                        setEarlyDetection3Screen();
//                    } else {
//                        setEarlyDetection2Screen();
//                    }
//                    break;
//                case PERSONAL_HISTORY_2:
//                    setPersonalHistory1Screen();
//                    break;
//                case PERSONAL_HISTORY_3:
//                    setPersonalHistory2Screen();
//                    break;
//                case PERSONAL_EXAMINATION:
//                    setPersonalHistory3Screen();
//                    break;
                case CBAC_EDIT_DETAILS:
                    if (isSelectedMemberFemale) {
                        setEarlyDetection5Screen();
                    } else {
                        setEarlyDetection4Screen();
                    }
//                    setPersonalExaminationScreen();
                    break;
                default:
            }
            return true;
        }
        return false;
    }
}
