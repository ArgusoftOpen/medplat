package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceRestClientImpl;
import com.argusoft.sewa.android.app.databean.QueryMobDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.restclient.RestHttpException;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.LinkedHashMap;
import java.util.List;

@EActivity
public class PersonalHistoryDetailsActivity extends MenuActivity implements View.OnClickListener {

    @Bean
    public SewaServiceRestClientImpl restClient;
    @Bean
    public SewaServiceImpl sewaService;

    private static final String MEMBER_DETAILS_SCREEN = "memberDetailsScreen";
    private static final String PERSONAL_HISTORY_1 = "personalHistory1";
    private static final String PERSONAL_HISTORY_2 = "personalHistory2";
    private static final String PERSONAL_HISTORY_3 = "personalHistory3";
    private static final String MENSTRUAL_HISTORY = "menstrualHistory";
    private static final String PERSONAL_EXAMINATION = "personalExamination";

    private static final String MEMBER_ID = "memberId";
    private static final String MEMBER_NAME = "memberName";
    private static final String UNIQUE_HEALTH_ID = "uniqueHealthId";
    private static final String ADDRESS = "address";
    private static final String MOBILE_NUMBER = "mobileNumber";
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
    private boolean isSelectedMemberMale;
    private boolean isSelectedMemberFemale;
    private String selectedScreen = MEMBER_DETAILS_SCREEN;
    private Long selectedMemberId;
    private LinkedHashMap<String, Object> selectedMemberPersonalHistoryDetails;
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
        setTitle(UtilBean.getTitleText(LabelConstants.NCD_PERSONAL_HISTORY_DETAILS_ACTIVITY));
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
                retrievePersonalHistoryDetailFromServer();
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
            runOnUiThread(new Runnable() {
                public void run() {
                    View.OnClickListener myListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            setResult(RESULT_CANCELED);
                            finish();
                        }
                    };
                    alertDialog = new MyAlertDialog(context, false,
                            UtilBean.getMyLabel(LabelConstants.NETWORK_CONNECTIVITY_ALERT),
                            myListener, DynamicUtils.BUTTON_OK);
                    alertDialog.show();
                }
            });
        }
    }

    @Background
    public void retrievePersonalHistoryDetailFromServer() {
        LinkedHashMap<String, Object> parameter = new LinkedHashMap<>();
        parameter.put(MEMBER_ID, selectedMemberId);
        QueryMobDataBean queryMobDataBean = new QueryMobDataBean();
        queryMobDataBean.setCode("mob_ncd_personal_history_details_by_member_id");
        queryMobDataBean.setParameters(parameter);
        try {
            List<LinkedHashMap<String, Object>> result = restClient.executeQuery(queryMobDataBean).getResult();
            if (result != null && !result.isEmpty()) {
                selectedMemberPersonalHistoryDetails = result.get(0);
                setSubTitle(UtilBean.getNotAvailableIfNull(selectedMemberPersonalHistoryDetails.get(MEMBER_NAME)));
                if (GlobalTypes.MALE.equals(selectedMemberPersonalHistoryDetails.get(GENDER))) {
                    isSelectedMemberMale = true;
                } else if (GlobalTypes.FEMALE.equals(selectedMemberPersonalHistoryDetails.get(GENDER))) {
                    isSelectedMemberFemale = true;
                }
                if (getIntent().getStringExtra("navigationTo") != null) {
                    switch (getIntent().getStringExtra("navigationTo")) {
                        case MEMBER_DETAILS_SCREEN:
                            setMemberDetailsScreen();
                            break;
                        case PERSONAL_HISTORY_1:
                            setPersonalHistory1Screen();
                            break;
                        case PERSONAL_HISTORY_2:
                            setPersonalHistory2Screen();
                            break;
                        case PERSONAL_HISTORY_3:
                            setPersonalHistory3Screen();
                            break;
//                        case PERSONAL_EXAMINATION:
//                            setPersonalExaminationScreen();
//                            break;
                        case MENSTRUAL_HISTORY:
                            setMenstrualHistoryScreen();
                            break;
                        default:
                    }
                } else {
                    setMemberDetailsScreen();
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel("Data not found in the server. Please refresh and try again."));
                    }
                });

                setResult(RESULT_CANCELED);
                finish();
            }
        } catch (RestHttpException e) {
            Log.e("PersonalHistoryDetails", null, e);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (selectedScreen) {
                case MEMBER_DETAILS_SCREEN:
                    setPersonalHistory1Screen();
//                    setPersonalExaminationScreen();
                    break;
//                case PERSONAL_EXAMINATION:
//                    setPersonalHistory1Screen();
//                    break;
                case PERSONAL_HISTORY_1:
                    setPersonalHistory2Screen();
                    break;
                case PERSONAL_HISTORY_2:
                    setPersonalHistory3Screen();
                    break;
                case PERSONAL_HISTORY_3:
                    if (isSelectedMemberMale) {
                        setResult(RESULT_OK);
                        this.finish();
                    } else if (isSelectedMemberFemale) {
                        setMenstrualHistoryScreen();
                    }
                    break;
                case MENSTRUAL_HISTORY:
                    setResult(RESULT_OK);
                    this.finish();
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
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberPersonalHistoryDetails.get(UNIQUE_HEALTH_ID))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.FAMILY_ID)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberPersonalHistoryDetails.get("familyId"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.MEMBER_NAME)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberPersonalHistoryDetails.get(MEMBER_NAME))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.AGE)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberPersonalHistoryDetails.get("age"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.GENDER)));
        if (selectedMemberPersonalHistoryDetails.get(GENDER).equals(GlobalTypes.MALE)) {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,UtilBean.getMyLabel("Male")));
        } else if (selectedMemberPersonalHistoryDetails.get(GENDER).equals(GlobalTypes.FEMALE)) {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,UtilBean.getMyLabel("Female")));
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberPersonalHistoryDetails.get(GENDER))));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.ADDRESS)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberPersonalHistoryDetails.get(ADDRESS))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.MOBILE_NUMBER)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberPersonalHistoryDetails.get(MOBILE_NUMBER))));
//
//        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.MOBILE_OWNER)));
//        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
//                FullFormConstants.getFullFormOfCBACConstants(UtilBean.getNotAvailableIfNull(selectedMemberPersonalHistoryDetails.get("mobileOwner")))));

//        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.SERVICE_DATE)));
//        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberCbacDetails.get("serviceDate"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.EDUCATION_STATUS)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberPersonalHistoryDetails.get("educationStatusValue"))));

//        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.PROBLEMS)));
//        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
//                FullFormConstants.getFullFormOfCBACConstants(UtilBean.getNotAvailableIfNull(selectedMemberPersonalHistoryDetails.get("anyProblem")))));
//
//        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.HEALTH_INSURANCE)));
//        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get("healthInsurance"))));
//
//        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.SCHEME_DETAILS)));
//        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
//                FullFormConstants.getFullFormOfCBACConstants(UtilBean.getNotAvailableIfNull(selectedMemberPersonalHistoryDetails.get("schemeDetail")))));
        hideProcessDialog();
    }

    @UiThread
    public void setPersonalHistory1Screen() {
        selectedScreen = PERSONAL_HISTORY_1;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(this, LabelConstants.PERSONAL_HISTORY));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_HYPERTENSION));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_HYPERTENSION))));

        if (Boolean.TRUE.equals(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_HYPERTENSION))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.UNDER_TREATMENT_FOR_HYPERTENSION));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get("under_treatement_for_hypertension"))));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_DIABETES));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_DIABETES))));

        if (Boolean.TRUE.equals(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_DIABETES))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.UNDER_TREATMENT_FOR_DIABETES));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get("under_treatement_for_diabetes"))));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_HEART_DISEASES));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_HEART_DISEASES))));

        if (Boolean.TRUE.equals(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_HEART_DISEASES))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    LabelConstants.UNDER_TREATMENT_FOR_HEART_DISEASES));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get("under_treatement_for_heart_diseases"))));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_STROKE));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_STROKE))));

        if (Boolean.TRUE.equals(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_STROKE))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    LabelConstants.UNDER_TREATMENT_FOR_STROKE));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get("under_treatement_for_stroke"))));
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
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_KIDNEY_FAILURE))));

        if (Boolean.TRUE.equals(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_KIDNEY_FAILURE))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    LabelConstants.UNDER_TREATMENT_KIDNEY_FAILURE));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get("under_treatement_for_kidney_failure"))));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_NON_HEALING_WOUND));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_NON_HEALING_WOUND))));

        if (Boolean.TRUE.equals(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_NON_HEALING_WOUND))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    LabelConstants.UNDER_TREATMENT_FOR_NON_HEALING_WOUND));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get("under_treatement_for_non_healing_wound"))));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_COPD));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_COPD))));

        if (Boolean.TRUE.equals(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_COPD))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.UNDER_TREATMENT_FOR_COPD));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get("under_treatement_for_copd"))));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_ASTHAMA));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_ASTHAMA))));

        if (Boolean.TRUE.equals(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_ASTHAMA))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    LabelConstants.UNDER_TREATMENT_FOR_ASTHAMA));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get("under_treatement_for_asthama"))));
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
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_ORAL_CANCER))));

        if (Boolean.TRUE.equals(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_ORAL_CANCER))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.UNDER_TREATMENT_FOR_ORAL_CANCER));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get("under_treatement_for_oral_cancer"))));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_BREAST_CANCER));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_BREAST_CANCER))));

        if (Boolean.TRUE.equals(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_BREAST_CANCER))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    LabelConstants.UNDER_TREATMENT_FOR_BREAST_CANCER));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get("under_treatement_for_breast_cancer"))));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DIAGNOSED_FOR_CERVICAL_CANCER));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_CERVICAL_CANCER))));

        if (Boolean.TRUE.equals(selectedMemberPersonalHistoryDetails.get(DIAGNOSED_FOR_CERVICAL_CANCER))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    LabelConstants.UNDER_TREATMENT_FOR_CERVICAL_CANCER));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get("under_treatement_for_cervical_cancer"))));
        }
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.OTHER));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get("any_other_examination"))));

        if (Boolean.TRUE.equals(selectedMemberPersonalHistoryDetails.get("any_other_examination"))) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    LabelConstants.SPECIFY_OTHER_DIAGNOSIS));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.getNotAvailableIfNull(selectedMemberPersonalHistoryDetails.get("specify_other_examination"))));
        }
        hideProcessDialog();
    }

    @UiThread
    public void setPersonalExaminationScreen() {
        selectedScreen = PERSONAL_EXAMINATION;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(this, LabelConstants.PERSONAL_EXAMINATION));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.HEIGHT_IN_CMS));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberPersonalHistoryDetails.get(HEIGHT))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.WEIGHT_IN_KGS));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberPersonalHistoryDetails.get(WEIGHT))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.BODY_MASS_INDEX));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberPersonalHistoryDetails.get("bmi"))));
        hideProcessDialog();
    }

    @UiThread
    public void setMenstrualHistoryScreen() {
        selectedScreen = MENSTRUAL_HISTORY;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(this, LabelConstants.MENSTRUAL_HISTORY));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.AGE_AT_MENARCHE));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberPersonalHistoryDetails.get("age_at_menarche"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.MANOPAUSE_ARRIVED));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get("menopause_arrived"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DURATION_OF_MANOPAUSE));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberPersonalHistoryDetails.get("duration_of_menopause"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.PREGNANT));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get("pregnant"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                LabelConstants.LACTATING));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get("lactating"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.REGULAR_PERIOD));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get("regular_periods"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.HEAVY_BLEEDING));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get("bleeding"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                LabelConstants.ASSOCIATED_WITH_PAIN));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.returnYesNoNotAvailableFromBoolean(selectedMemberPersonalHistoryDetails.get("associated_with"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.REMARKS));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMemberPersonalHistoryDetails.get("remarks"))));

        hideProcessDialog();
    }

    @Override
    public void onBackPressed() {
        View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == BUTTON_POSITIVE) {
                    alertDialog.dismiss();
                    finish();
                } else {
                    alertDialog.dismiss();
                }
            }
        };

        alertDialog = new MyAlertDialog(this,
                LabelConstants.CONFORMATION_TO_CANCEL_VIEWING_PERSONAL_HISTORY_DETAILS,
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
//                case PERSONAL_EXAMINATION:
//                    setMemberDetailsScreen();
//                    break;
                case PERSONAL_HISTORY_1:
                    setMemberDetailsScreen();
//                    setPersonalExaminationScreen();
                    break;
                case PERSONAL_HISTORY_2:
                    setPersonalHistory1Screen();
                    break;
                case PERSONAL_HISTORY_3:
                    setPersonalHistory2Screen();
                    break;
                case MENSTRUAL_HISTORY:
                    setPersonalHistory3Screen();
                default:
            }
            return true;
        }
        return false;
    }
}
