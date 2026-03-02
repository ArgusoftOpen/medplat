package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyDynamicComponents;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.component.listeners.FamilyIdTextWatcher;
import com.argusoft.sewa.android.app.component.listeners.MemberIdTextWatcher;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RbskConstants;
import com.argusoft.sewa.android.app.core.impl.HealthInfrastructureServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceRestClientImpl;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.QueryMobDataBean;
import com.argusoft.sewa.android.app.databean.RbskDefectDetailDto;
import com.argusoft.sewa.android.app.databean.RbskScreeningDetailDto;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.restclient.RestHttpException;
import com.argusoft.sewa.android.app.restclient.impl.ApiManager;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.argusoft.sewa.android.app.util.WSConstants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import okhttp3.ResponseBody;

@EActivity
public class HeadToToeScreeningActivity extends MenuActivity implements View.OnClickListener {

    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaServiceRestClientImpl sewaServiceRestClient;
    @Bean
    HealthInfrastructureServiceImpl healthInfrastructureService;
    @Bean
    ApiManager apiManager;

    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private LinearLayout memberDetailsLayout;
    private Button nextButton;

    private MyAlertDialog myAlertDialog;

    private static final String MEMBER_SEARCH_SCREEN = "memberSearchScreen";
    private static final String MEMBER_DETAILS_SCREEN = "memberDetailsScreen";
    private static final String CHILD_BODY_MEASUREMENT_SCREEN = "childBodyMeasurementScreen";
    private static final String APGAR_QUESTION_SCREEN = "apgarQuestionScreen";
    private static final String COMMON_DEFECTS_SCREEN_1 = "commonDefectsScreen1";
    private static final String COMMON_DEFECTS_SCREEN_2 = "commonDefectsScreen2";
    private static final String SCREENING_CONFIRMATION_SCREEN = "screeningConformationScreen";
    private static final String SCREENING_SCREEN = "screeningScreen";
    private static final String DISEASE_SELECTION_SCREEN = "diseaseSelectionScreen";
    private static final String STABILIZATION_INFO_SCREEN = "stabilizationInfoScreen";
    private static final String REFERRAL_CONFORMATION_SCREEN = "referralConformationScreen";
    private static final String HEALTH_INFRASTRUCTURE_SCREEN = "healthInfrastructureScreen";
    private static final String SCREENING_COMPLETION_SCREEN = "screeningCompletionScreen";
    private static final String SELECTED_DEFECT_LIST_SCREEN = "selected_defectListScreen";

    private LinkedHashMap<String, Object> memberDataBean = null;
    private LinkedHashMap<String, Object> familyDataBean = null;
    private List<LinkedHashMap<String, Object>> defects;
    private Map<Integer, byte[]> images = new HashMap<>();
    private String screen;
    private Set<Integer> selectedDefects = new HashSet<>();
    private int selectedDefect;
    private String bodyPart;
    private boolean isReferralRequired = false;
    private QueFormBean healthInfraQueFormBean;
    private boolean visibleDefectsPresent;

    private TextInputLayout familyEditText;
    private TextInputLayout childEditText;
    private TextInputLayout motherEditText;
    private RadioGroup searchSelectionRadioGroup;
    private Button searchButton;
    private RadioButton tmpRadioButton;
    private TextInputLayout heightText;
    private TextInputLayout headCircumferenceText;
    private RadioGroup apgar1MinRadioGroup;
    private RadioGroup apgar5MinRadioGroup;
    private RadioGroup toneOfTheBabyRadioGroup;
    private RadioGroup moroReflexRadioGroup;
    private RadioGroup stepReflexRadioGroup;
    private RadioGroup plantarReflexRadioGroup;
    private RadioGroup rootReflexRadioGroup;
    private RadioGroup asymmetricalTonicRadioGroup;
    private RadioGroup suckingReflexRadioGroup;
    private RadioGroup graspReflexRadioGroup;
    private RadioGroup referralConformationRadioGroup;
    private LinearLayout healthInfraComponent;
    private LinearLayout childBodyLayout;
    private RelativeLayout childFrontBodyLayout;
    private RelativeLayout childBackBodyLayout;
    private MaterialButton frontPartSelectionButton;
    private MaterialButton backPartSelectionButton;

    private static final int RADIOBUTTON_ID_CHILD = 101;
    private static final int RADIOBUTTON_ID_FAMILY = 102;
    private static final int RADIOBUTTON_ID_MOTHER = 103;

    private static final int RADIOBUTTON_ID_YES = 1;
    private static final int RADIOBUTTON_ID_NO = 2;
    private static final int RADIOBUTTON_ID_NOT_KNOWN = 3;

    private static final String NULL_QUERY_RESPONSE = "##### Query Response is null";
    private static final String FAMILY_ID_KEY = "familyId";
    private static final String MEMBERS_KEY = "members";
    private static final String CHILD_NAME = "child_name";
    private static final String DEFECT_NAME = "defect_name";
    private static final String DESCRIPTION = "description";

    private int selectedMemberIndex = -1;
    private NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //To change body of generated methods, choose Tools | Templates.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        context = this;
        setGps();
        initView();
    }

    private void setGps() {
        if (!SharedStructureData.gps.isLocationProviderEnabled()) {
            // Ask user to enable GPS/network in settings
            SharedStructureData.gps.showSettingsAlert(this);
        }
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
        setTitle(UtilBean.getTitleText(LabelConstants.HEAD_TO_TOE_SCREENING_TITLE));
        if (memberDataBean != null) {
            Object tmpObject = memberDataBean.get(CHILD_NAME);
            if (tmpObject != null) {
                setSubTitle(tmpObject.toString());
            }
        }
    }

    private void initView() {
        showNotOnlineMessage(true);
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        scrollView = globalPanel.findViewById(DynamicUtils.ID_BODY_SCROLL);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        if (SharedStructureData.healthInfrastructureService == null) {
            SharedStructureData.healthInfrastructureService = healthInfrastructureService;
        }
        addSearchScreen();
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        if (!sewaService.isOnline()) {
            showNotOnlineMessage(false);
            return;
        }

        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            scrollView.scrollTo(0, 0);
            switch (screen) {
                case MEMBER_SEARCH_SCREEN:
                    if (familyDataBean == null) {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.SEARCH_AND_SELECT_MEMBER_FOR_SCREENING));
                    } else if (selectedMemberIndex != -1) {
                        //noinspection unchecked
                        List<LinkedHashMap<String, Object>> members = (List<LinkedHashMap<String, Object>>) familyDataBean.get(MEMBERS_KEY);
                        memberDataBean = Objects.requireNonNull(members).get(selectedMemberIndex);
                        initializeQuestionView();
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_MEMBER));
                    }
                    break;

                case MEMBER_DETAILS_SCREEN:
                    setChildBodyMeasurementScreen();
                    break;

                case CHILD_BODY_MEASUREMENT_SCREEN:
                    if (checkBodyMeasurementScreenValidation()) {
                        setApgarQuestionView();
                    }
                    break;

                case APGAR_QUESTION_SCREEN:
                    if (checkApgarScreenValidation()) {
                        setCommonDefectsScreen1();
                    }
                    break;

                case COMMON_DEFECTS_SCREEN_1:
                    if (checkCommonDefectsScreen1Validation()) {
                        setCommonDefectsScreen2();
                    }
                    break;

                case COMMON_DEFECTS_SCREEN_2:
                    if (checkCommonDefectsScreen2Validation()) {
                        setScreeningConfirmationScreen();
                    }
                    break;

                case SCREENING_SCREEN:
                case DISEASE_SELECTION_SCREEN:
                    if (selectedDefects.isEmpty()) {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.BODY_PART_SELECTION_REQUIRED_TO_ADD_DEFECT_ALERT));
                    } else {
                        retrieveSelectedDefectDetails();
                    }
                    break;

                case SELECTED_DEFECT_LIST_SCREEN:
                    getStabilizationInfoForDefects();
                    break;

                case STABILIZATION_INFO_SCREEN:
                    if (isReferralRequired) {
                        viewReferralConformationScreen();
                    } else {
                        setScreeningCompletionScreen();
                    }
                    break;

                case REFERRAL_CONFORMATION_SCREEN:
                    if (referralConformationRadioGroup.getCheckedRadioButtonId() == -1) {
                        SewaUtil.generateToast(context, LabelConstants.PLEASE_SELECT_AN_OPTION);
                    } else if (referralConformationRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_YES) {
                        viewHealthInfrastructureScreen();
                    } else {
                        setScreeningCompletionScreen();
                    }
                    break;

                case HEALTH_INFRASTRUCTURE_SCREEN:
                    if (healthInfraQueFormBean.getAnswer() == null || healthInfraQueFormBean.getAnswer().toString().trim().isEmpty()) {
                        SewaUtil.generateToast(context, healthInfraQueFormBean.getMandatorymessage());
                        return;
                    }
                    setScreeningCompletionScreen();
                    break;

                case SCREENING_COMPLETION_SCREEN:
                    showProcessDialog();
                    storeScreeningDetails();
                    break;

                default:
                    break;
            }
        }
    }

    private boolean checkBodyMeasurementScreenValidation() {
        if (heightText.getEditText() == null || heightText.getEditText().getText().toString().trim().length() == 0) {
            SewaUtil.generateToast(context, LabelConstants.HEIGHT_OF_CHILD_REQUIRED_ALERT);
            return false;
        }

        if (Integer.parseInt(heightText.getEditText().getText().toString().trim()) < 40
                || Integer.parseInt(heightText.getEditText().getText().toString().trim()) > 70) {
            SewaUtil.generateToast(context, LabelConstants.ENTER_HEIGHT_BETWEEN_RANGE);
            return false;
        }

        if (headCircumferenceText.getEditText() == null || headCircumferenceText.getEditText().getText().toString().trim().length() == 0) {
            SewaUtil.generateToast(context, LabelConstants.HEAD_CIRCUMFERENCE_OF_CHILD_REQUIRED_ALERT);
            return false;
        }

        if (Integer.parseInt(headCircumferenceText.getEditText().getText().toString().trim()) < 30
                || Integer.parseInt(headCircumferenceText.getEditText().getText().toString().trim()) > 40) {
            SewaUtil.generateToast(context, LabelConstants.ENTER_HEAD_CIRCUMFERENCE_BETWEEN_RANGE);
            return false;
        }

        return true;
    }

    private boolean checkApgarScreenValidation() {
        if (apgar1MinRadioGroup.getCheckedRadioButtonId() == -1) {
            SewaUtil.generateToast(context, LabelConstants.VALUE_FOR_APGAR_1_MIN_REQUIRED);
            return false;
        }

        if (apgar5MinRadioGroup.getCheckedRadioButtonId() == -1) {
            SewaUtil.generateToast(context, LabelConstants.VALUE_FOR_APGAR_5_MIN_REQUIRED);
            return false;
        }

        return true;
    }

    private boolean checkCommonDefectsScreen1Validation() {
        if (toneOfTheBabyRadioGroup.getCheckedRadioButtonId() == -1) {
            SewaUtil.generateToast(context, LabelConstants.VALUE_FOR_TONE_OF_BABY_REQUIRED);
            return false;
        }
        if (moroReflexRadioGroup.getCheckedRadioButtonId() == -1) {
            SewaUtil.generateToast(context, LabelConstants.VALUE_FOR_MORO_REFLEX_REQUIRED);
            return false;
        }
        if (stepReflexRadioGroup.getCheckedRadioButtonId() == -1) {
            SewaUtil.generateToast(context, LabelConstants.VALUE_FOR_STEP_REFLEX_REQUIRED);
            return false;
        }
        if (plantarReflexRadioGroup.getCheckedRadioButtonId() == -1) {
            SewaUtil.generateToast(context, LabelConstants.VALUE_FOR_PLANTAR_REFLEX_REQUIRED);
            return false;
        }

        return true;
    }

    private boolean checkCommonDefectsScreen2Validation() {
        if (rootReflexRadioGroup.getCheckedRadioButtonId() == -1) {
            SewaUtil.generateToast(context, LabelConstants.VALUE_FOR_ROOT_REFLEX_REQUIRED);
            return false;
        }
        if (asymmetricalTonicRadioGroup.getCheckedRadioButtonId() == -1) {
            SewaUtil.generateToast(context, LabelConstants.VALUE_FOR_ASYMMETRICAL_TONIC_REQUIRED);
            return false;
        }
        if (suckingReflexRadioGroup.getCheckedRadioButtonId() == -1) {
            SewaUtil.generateToast(context, LabelConstants.VALUE_FOR_SUCKING_REFLEX_REQUIRED);
            return false;
        }
        if (graspReflexRadioGroup.getCheckedRadioButtonId() == -1) {
            SewaUtil.generateToast(context, LabelConstants.VALUE_FOR_GRASP_REFLEX_REQUIRED);
            return false;
        }

        return true;
    }

    private boolean checkForReferralRequirement() {
        return (apgar1MinRadioGroup.getCheckedRadioButtonId() != RADIOBUTTON_ID_NOT_KNOWN
                || apgar5MinRadioGroup.getCheckedRadioButtonId() != RADIOBUTTON_ID_NOT_KNOWN
                || toneOfTheBabyRadioGroup.getCheckedRadioButtonId() != RADIOBUTTON_ID_NOT_KNOWN
                || moroReflexRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_NO
                || stepReflexRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_NO
                || plantarReflexRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_NO
                || rootReflexRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_NO
                || asymmetricalTonicRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_NO
                || suckingReflexRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_NO
                || graspReflexRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_NO);
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

        myAlertDialog = new MyAlertDialog(this,
                LabelConstants.ON_HEAD_TO_TOE_SCREENING_CLOSE_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        myAlertDialog.show();
    }

    @UiThread
    public void addSearchScreen() {
        screen = MEMBER_SEARCH_SCREEN;
        nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));

        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.SEARCH_FOR_MEMBER)));

        if (searchSelectionRadioGroup == null) {
            createSearchSelectionRadioGroup();
        }
        bodyLayoutContainer.addView(searchSelectionRadioGroup);

        if (familyEditText == null) {
            createFamilyEditText();
        }

        if (childEditText == null) {
            childEditText = createMemberEditText(LabelConstants.CHILD_ID);
        }

        if (motherEditText == null) {
            motherEditText = createMemberEditText(LabelConstants.MOTHER_ID);
        }

        bodyLayoutContainer.addView(familyEditText);
        bodyLayoutContainer.addView(childEditText);
        bodyLayoutContainer.addView(motherEditText);

        setEditTextVisibility();

        if (searchButton == null) {
            createSearchButton();
        }

        bodyLayoutContainer.addView(searchButton);
        hideProcessDialog();
    }

    private void createSearchSelectionRadioGroup() {
        searchSelectionRadioGroup = new RadioGroup(this);
        RadioButton radioButton = MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel(LabelConstants.SEARCH_BY_FAMILY_ID), RADIOBUTTON_ID_FAMILY);
        radioButton.setChecked(true);
        searchSelectionRadioGroup.addView(radioButton);
        radioButton = MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel(LabelConstants.SEARCH_BY_CHILD_ID), RADIOBUTTON_ID_CHILD);
        searchSelectionRadioGroup.addView(radioButton);
        radioButton = MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel(LabelConstants.SEARCH_BY_MOTHER_ID), RADIOBUTTON_ID_MOTHER);
        searchSelectionRadioGroup.addView(radioButton);

        searchSelectionRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            setEditTextVisibility();
            clearMemberDetailsLayout();
        });
    }

    private void setEditTextVisibility() {
        if (searchSelectionRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_FAMILY) {
            familyEditText.setVisibility(View.VISIBLE);
            childEditText.setVisibility(View.GONE);
            motherEditText.setVisibility(View.GONE);
        } else if (searchSelectionRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_CHILD) {
            childEditText.setVisibility(View.VISIBLE);
            familyEditText.setVisibility(View.GONE);
            motherEditText.setVisibility(View.GONE);
        } else if (searchSelectionRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_MOTHER) {
            childEditText.setVisibility(View.GONE);
            familyEditText.setVisibility(View.GONE);
            motherEditText.setVisibility(View.VISIBLE);
        }
    }

    private void createFamilyEditText() {
        familyEditText = MyStaticComponents.getEditText(context, LabelConstants.FAMILY_ID, -1, 22, InputType.TYPE_CLASS_TEXT | TYPE_TEXT_FLAG_CAP_CHARACTERS);
        familyEditText.setVisibility(View.GONE);
        if (familyEditText.getEditText() != null) {
            familyEditText.getEditText().addTextChangedListener(new FamilyIdTextWatcher(familyEditText.getEditText()));
        }
    }

    private TextInputLayout createMemberEditText(String hint) {
        final TextInputLayout editText = MyStaticComponents.getEditText(context, hint, 103, 12, InputType.TYPE_CLASS_TEXT | TYPE_TEXT_FLAG_CAP_CHARACTERS);
        editText.setVisibility(View.GONE);
        if (editText.getEditText() != null) {
            editText.getEditText().addTextChangedListener(new MemberIdTextWatcher(editText.getEditText()));
        }
        return editText;
    }

    private void createSearchButton() {
        searchButton = MyStaticComponents.getCustomButton(context, LabelConstants.SEARCH, -1, null);
        searchButton.setOnClickListener(v -> {
            if (!sewaService.isOnline()) {
                showNotOnlineMessage(false);
                return;
            }

            showProcessDialog();
            clearMemberDetailsLayout();
            if (searchSelectionRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_CHILD
                    && childEditText.getEditText() != null) {
                String memberId = childEditText.getEditText().getText().toString().toUpperCase();
                if (memberId.trim().isEmpty() || memberId.trim().length() < 2) {
                    SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.CHILD_ID_REQUIRED_ALERT));
                    hideProcessDialog();
                } else {
                    getMemberDataBeanFromServer(memberId, "mob_rbsk_child_search_by_member_id");
                }
            } else if (searchSelectionRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_FAMILY
                    && familyEditText.getEditText() != null) {
                String familyId = familyEditText.getEditText().getText().toString().toUpperCase();
                if (familyId.trim().isEmpty() || familyId.trim().length() < 10) {
                    SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.FAMILY_ID_REQUIRED_ALERT));
                    hideProcessDialog();
                } else {
                    getFamilyDataBeanFromServer(familyId);
                }
            } else if (searchSelectionRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_MOTHER
                    && motherEditText.getEditText() != null) {
                String memberId = motherEditText.getEditText().getText().toString().toUpperCase();
                if (memberId.trim().isEmpty() || memberId.trim().length() < 2) {
                    SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.MOTHER_ID_REQUIRED_ALERT));
                    hideProcessDialog();
                } else {
                    getMemberDataBeanFromServer(memberId, "mob_rbsk_child_search_by_mother_id");
                }
            }
        });
    }

    private void clearMemberDetailsLayout() {
        if (memberDetailsLayout != null) {
            memberDetailsLayout.removeAllViews();
        }
        bodyLayoutContainer.removeView(memberDetailsLayout);
        memberDataBean = null;
        familyDataBean = null;
    }

    @Background
    public void getMemberDataBeanFromServer(String memberId, String queryCode) {
        try {
            LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
            parameters.put("health_id", memberId);
            parameters.put("userId", SewaTransformer.loginBean.getUserID());

            QueryMobDataBean queryResponse = sewaServiceRestClient.executeQuery(
                    new QueryMobDataBean(queryCode, null, parameters, 0));
            if (queryResponse != null && queryResponse.getResult() != null && !queryResponse.getResult().isEmpty()) {
                List<LinkedHashMap<String, Object>> resultList = queryResponse.getResult();

                familyDataBean = new LinkedHashMap<>();
                familyDataBean.put(FAMILY_ID_KEY, resultList.get(0).get("family_health_id"));
                familyDataBean.put(MEMBERS_KEY, resultList);
            } else {
                familyDataBean = null;
                Log.d(getClass().getSimpleName(), NULL_QUERY_RESPONSE);
            }
            showFamilyDetails();
        } catch (RestHttpException e) {
            hideProcessDialog();
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
    }

    @Background
    public void getFamilyDataBeanFromServer(String familyId) {
        try {
            LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
            parameters.put("family_id", familyId);
            parameters.put("userId", SewaTransformer.loginBean.getUserID());

            QueryMobDataBean queryResponse = sewaServiceRestClient.executeQuery(
                    new QueryMobDataBean("mob_rbsk_child_search_by_family_id", null, parameters, 0));
            if (queryResponse != null && queryResponse.getResult() != null && !queryResponse.getResult().isEmpty()) {
                List<LinkedHashMap<String, Object>> resultList = queryResponse.getResult();
                familyDataBean = new LinkedHashMap<>();
                familyDataBean.put(FAMILY_ID_KEY, familyId);
                familyDataBean.put(MEMBERS_KEY, resultList);
            } else {
                familyDataBean = null;
            }
            showFamilyDetails();
        } catch (RestHttpException e) {
            hideProcessDialog();
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @UiThread
    public void showFamilyDetails() {
        if (familyDataBean != null) {
            memberDetailsLayout = MyStaticComponents.getLinearLayout(context, 105,
                    LinearLayout.VERTICAL, new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            memberDetailsLayout.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel(LabelConstants.FAMILY_ID)));
            memberDetailsLayout.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.getNotAvailableIfNull(familyDataBean.get(FAMILY_ID_KEY))));
            memberDetailsLayout.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel(LabelConstants.MEMBER_LIST)));

            List<ListItemDataBean> list = new ArrayList<>();
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedMemberIndex = position;
            ArrayList<LinkedHashMap<String, Object>> tempMembersObj = (ArrayList<LinkedHashMap<String, Object>>) familyDataBean.get(MEMBERS_KEY);
            if (tempMembersObj != null) {
                Object uhi;
                Object cn;
                for (LinkedHashMap<String, Object> entry : tempMembersObj) {
                    uhi = entry.get("unique_health_id");
                    cn = entry.get(CHILD_NAME);
                    if (uhi != null && cn != null) {
                        list.add(new ListItemDataBean(uhi.toString(), cn.toString()));
                    }
                }
            }

            PagingListView paginatedListViewWithItem = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_item, onItemClickListener, null);
            memberDetailsLayout.addView(paginatedListViewWithItem);
            bodyLayoutContainer.addView(memberDetailsLayout);
        } else {
            SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.NO_MEMBER_FOUND_FOR_GIVEN_ID));
        }
        hideProcessDialog();
    }

    private void initializeQuestionView() {
        apgar1MinRadioGroup = initializeApgarRadioGroup();
        apgar5MinRadioGroup = initializeApgarRadioGroup();

        heightText = MyStaticComponents.getEditText(context, LabelConstants.CHILD_HEIGHT, -1, 3, InputType.TYPE_CLASS_NUMBER);

        headCircumferenceText = MyStaticComponents.getEditText(context, LabelConstants.HEAD_CIRCUMFERENCE, -1, 3, InputType.TYPE_CLASS_NUMBER);

        toneOfTheBabyRadioGroup = new RadioGroup(context);
        tmpRadioButton = MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel(LabelConstants.HYPERTONIA), RADIOBUTTON_ID_YES);
        toneOfTheBabyRadioGroup.addView(tmpRadioButton);
        tmpRadioButton = MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel(LabelConstants.HYPOTONIA), RADIOBUTTON_ID_NO);
        toneOfTheBabyRadioGroup.addView(tmpRadioButton);
        tmpRadioButton = MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel(LabelConstants.NORMAL), RADIOBUTTON_ID_NOT_KNOWN);
        toneOfTheBabyRadioGroup.addView(tmpRadioButton);

        moroReflexRadioGroup = initializeRadioGroup();
        stepReflexRadioGroup = initializeRadioGroup();
        plantarReflexRadioGroup = initializeRadioGroup();
        rootReflexRadioGroup = initializeRadioGroup();
        asymmetricalTonicRadioGroup = initializeRadioGroup();
        suckingReflexRadioGroup = initializeRadioGroup();
        graspReflexRadioGroup = initializeRadioGroup();

        referralConformationRadioGroup = new RadioGroup(context);
        addRadioButtonsYesAndNoToGroup(referralConformationRadioGroup);

        healthInfraQueFormBean = new QueFormBean();
        healthInfraQueFormBean.setIsmandatory(GlobalTypes.TRUE);
        healthInfraQueFormBean.setMandatorymessage(LabelConstants.SEARCH_AND_SELECT_HEALTH_INFRASTRUCTURE);
        setMemberDetailsView();
    }

    private RadioGroup initializeApgarRadioGroup() {
        final RadioGroup radioGroup = new RadioGroup(context);

        tmpRadioButton = MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel(RbskConstants.APGAR_O_TO_3), RADIOBUTTON_ID_YES);
        radioGroup.addView(tmpRadioButton);
        tmpRadioButton = MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel(RbskConstants.APGAR_4_TO_7), RADIOBUTTON_ID_NO);
        radioGroup.addView(tmpRadioButton);
        tmpRadioButton = MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel(RbskConstants.APGAR_7_PLUS), RADIOBUTTON_ID_NOT_KNOWN);
        radioGroup.addView(tmpRadioButton);

        return radioGroup;
    }

    private RadioGroup initializeRadioGroup() {
        final RadioGroup rGroup = new RadioGroup(context);

        addRadioButtonsYesAndNoToGroup(rGroup);
        tmpRadioButton = MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel(LabelConstants.NOT_KNOWN), RADIOBUTTON_ID_NOT_KNOWN);
        rGroup.addView(tmpRadioButton);

        return rGroup;
    }

    private void addRadioButtonsYesAndNoToGroup(RadioGroup radioGroup) {
        tmpRadioButton = MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel(LabelConstants.YES), RADIOBUTTON_ID_YES);
        radioGroup.addView(tmpRadioButton);
        tmpRadioButton = MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel(LabelConstants.NO), RADIOBUTTON_ID_NO);
        radioGroup.addView(tmpRadioButton);
    }

    @UiThread
    public void setMemberDetailsView() {
        screen = MEMBER_DETAILS_SCREEN;
        bodyLayoutContainer.removeAllViews();
        Object tmpObject = memberDataBean.get(CHILD_NAME);
        if (tmpObject != null) {
            setSubTitle(tmpObject.toString());
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context,
                UtilBean.getMyLabel(LabelConstants.MEMBER_DETAILS)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.CHILD_ID)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getNotAvailableIfNull(memberDataBean.get("unique_health_id"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.CHILD_NAME)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getNotAvailableIfNull(memberDataBean.get(CHILD_NAME))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.MOTHER_ID)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getNotAvailableIfNull(memberDataBean.get("mother_id"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.MOTHER_NAME)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getNotAvailableIfNull(memberDataBean.get("mother_name"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.MOTHER_MOBILE_NUMBER)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getNotAvailableIfNull(memberDataBean.get("mobile_number"))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.DATE_OF_BIRTH)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getNotAvailableIfNull(memberDataBean.get("date_of_birth"))));

        tmpObject = memberDataBean.get("date_of_birth");
        if (tmpObject != null) {
            try {
                Date dob = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(tmpObject.toString());
                int[] yearMonthDayAge = UtilBean.calculateAgeYearMonthDay(dob.getTime());
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                        context, UtilBean.getMyLabel(LabelConstants.AGE)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                        UtilBean.getAgeDisplay(yearMonthDayAge[0], yearMonthDayAge[1], yearMonthDayAge[2])));
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), null, e);
            }
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.GENDER)));

        tmpObject = memberDataBean.get("gender");
        String gender = tmpObject != null ? tmpObject.toString() : "";
        switch (gender) {
            case GlobalTypes.MALE:
                gender = LabelConstants.MALE;
                break;
            case GlobalTypes.FEMALE:
                gender = LabelConstants.FEMALE;
                break;
            case GlobalTypes.TRANSGENDER:
                gender = LabelConstants.TRANSGENDER;
                break;
            default:
                gender = LabelConstants.NOT_AVAILABLE;
        }
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, gender));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.CHILD_BIRTH_WEIGHT)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getNotAvailableIfNull(memberDataBean.get("birth_weight"))
                        + (UtilBean.getNotAvailableIfNull(memberDataBean.get("birth_weight")).equals(GlobalTypes.NOT_AVAILABLE) ? "" : " Kgs")));
    }

    @UiThread
    public void setChildBodyMeasurementScreen() {
        screen = CHILD_BODY_MEASUREMENT_SCREEN;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.CHILD_HEIGHT_IN_CMS)));
        bodyLayoutContainer.addView(heightText);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.HEAD_CIRCUMFERENCE_IN_CMS)));
        bodyLayoutContainer.addView(headCircumferenceText);
    }

    @UiThread
    public void setApgarQuestionView() {
        screen = APGAR_QUESTION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context,
                LabelConstants.APGAR_STANDS_FOR_APPROPRIATES));

        ImageView imageView = MyStaticComponents.getImageView(context, -1, R.drawable.apgar_scoring_system, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        imageView.setAdjustViewBounds(true);
        bodyLayoutContainer.addView(imageView);

        TextView apgrTextView = MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.APGAR_1_MIN));
        bodyLayoutContainer.addView(apgrTextView);
        bodyLayoutContainer.addView(apgar1MinRadioGroup);

        apgrTextView = MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.APGAR_5_MIN));
        bodyLayoutContainer.addView(apgrTextView);
        bodyLayoutContainer.addView(apgar5MinRadioGroup);
    }

    @UiThread
    public void setCommonDefectsScreen1() {
        screen = COMMON_DEFECTS_SCREEN_1;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.TONE_OF_BABY + ":")));
        bodyLayoutContainer.addView(toneOfTheBabyRadioGroup);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.MORO_REFLEX + ":")));
        bodyLayoutContainer.addView(moroReflexRadioGroup);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.STEP_REFLEX + ":")));
        bodyLayoutContainer.addView(stepReflexRadioGroup);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.PLANTAR_REFLEX + ":")));
        bodyLayoutContainer.addView(plantarReflexRadioGroup);
    }

    @UiThread
    public void setCommonDefectsScreen2() {
        screen = COMMON_DEFECTS_SCREEN_2;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.ROOT_REFLEX + ":")));
        bodyLayoutContainer.addView(rootReflexRadioGroup);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.ASYMMETRICAL_TONIC + ":")));
        bodyLayoutContainer.addView(asymmetricalTonicRadioGroup);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.SUCKING_REFLEX + ":")));
        bodyLayoutContainer.addView(suckingReflexRadioGroup);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.GRASP_REFLEX + ":")));
        bodyLayoutContainer.addView(graspReflexRadioGroup);
    }

    @UiThread
    public void setScreeningConfirmationScreen() {
        screen = SCREENING_CONFIRMATION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        isReferralRequired = checkForReferralRequirement();

        TextView textView = MyStaticComponents.generateInstructionView(context, LabelConstants.EXAMINE_BABY_AND_SCHOOSE_FOR_DEFECTS);
        textView.setPadding(0, 0, 0, 30);
        bodyLayoutContainer.addView(textView);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, LabelConstants.DOES_CHILD_HAVE_VISIBLE_DEFECTS));

        LinearLayout.LayoutParams buttonParam = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        buttonParam.setMargins(20, 20, 20, 20);
        Button enterVisualDefects = MyStaticComponents.getCustomButton(context, LabelConstants.ENTER_VISUAL_DEFECTS, -1, buttonParam);
        enterVisualDefects.setOnClickListener(v -> {
            nextButton.setVisibility(View.VISIBLE);
            visibleDefectsPresent = true;
            setScreeningScreen();
        });
        bodyLayoutContainer.addView(enterVisualDefects);

        Button noDefects = MyStaticComponents.getCustomButton(context, LabelConstants.NO_DEFECTS, -1, buttonParam);
        noDefects.setOnClickListener(v -> {
            nextButton.setVisibility(View.VISIBLE);
            visibleDefectsPresent = false;
            if (isReferralRequired) {
                viewReferralConformationScreen();
            } else {
                setScreeningCompletionScreen();
            }
        });
        bodyLayoutContainer.addView(noDefects);

        nextButton.setVisibility(View.GONE);
    }

    private void setScreeningScreen() {
        bodyLayoutContainer.removeAllViews();
        screen = SCREENING_SCREEN;
        if (childBodyLayout == null) {
            childBodyLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.child_body_layout, null);

            MaterialButtonToggleGroup bodyPartViewSelectionButton = childBodyLayout.findViewById(R.id.bodyPartViewSelector);
            bodyPartViewSelectionButton.addOnButtonCheckedListener((group, checkedId, isChecked) -> setPrimaryButton(checkedId));
            frontPartSelectionButton = bodyPartViewSelectionButton.findViewById(R.id.frontBody);
            backPartSelectionButton = bodyPartViewSelectionButton.findViewById(R.id.backBody);

            childFrontBodyLayout = childBodyLayout.findViewById(R.id.frontBodyLayout);
            addEventHandler(childFrontBodyLayout, null);

            childBackBodyLayout = childBodyLayout.findViewById(R.id.backBodyLayout);
            addEventHandler(null, childBackBodyLayout);
            setPrimaryButton(R.id.frontBody);
        }
        bodyLayoutContainer.addView(childBodyLayout);
    }

    private void setPrimaryButton(int checkedId) {
        frontPartSelectionButton.setTextColor(ContextCompat.getColorStateList(context, R.color.toggle_button_text_color_selector));
        frontPartSelectionButton.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimary)));
        frontPartSelectionButton.setStrokeWidth(2);
        backPartSelectionButton.setTextColor(ContextCompat.getColorStateList(context, R.color.toggle_button_text_color_selector));
        backPartSelectionButton.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimary)));
        backPartSelectionButton.setStrokeWidth(2);

        if (checkedId == R.id.frontBody) {
            childBackBodyLayout.setVisibility(View.GONE);
            childFrontBodyLayout.setVisibility(View.VISIBLE);
            frontPartSelectionButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorAccent));
            frontPartSelectionButton.setTextColor(ContextCompat.getColor(context, R.color.white));
            backPartSelectionButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.white));
        } else if (checkedId == R.id.backBody) {
            childBackBodyLayout.setVisibility(View.VISIBLE);
            childFrontBodyLayout.setVisibility(View.GONE);
            frontPartSelectionButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.white));
            backPartSelectionButton.setTextColor(ContextCompat.getColor(context, R.color.white));
            backPartSelectionButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorAccent));
        }
    }

    @UiThread
    public void addEventHandler(RelativeLayout childFrontBodyLayout, RelativeLayout childBackBodyLayout) {
        if (childFrontBodyLayout != null) {
            bindEventListenerOnClick(childFrontBodyLayout, R.id.head, RbskConstants.BODY_PART_HEAD);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.foreHead, RbskConstants.BODY_PART_HEAD);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.leftEye, RbskConstants.BODY_PART_EYE);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.rightEye, RbskConstants.BODY_PART_EYE);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.leftEar, RbskConstants.BODY_PART_EAR);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.rightEar, RbskConstants.BODY_PART_EAR);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.leftCheek, RbskConstants.BODY_PART_CHEEKS);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.rightCheek, RbskConstants.BODY_PART_CHEEKS);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.nose, RbskConstants.BODY_PART_NOSE);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.mouth, RbskConstants.BODY_PART_MOUTH);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.neck, RbskConstants.BODY_PART_NECK);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.leftHand, RbskConstants.BODY_PART_HAND);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.leftPalm, RbskConstants.BODY_PART_HAND);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.rightHand, RbskConstants.BODY_PART_HAND);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.rightPalm, RbskConstants.BODY_PART_HAND);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.chest, RbskConstants.BODY_PART_CHEST);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.stomach, RbskConstants.BODY_PART_STOMACH);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.leftThigh, RbskConstants.BODY_PART_LEG);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.leftFibula, RbskConstants.BODY_PART_LEG);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.leftFoot, RbskConstants.BODY_PART_LEG);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.rightThigh, RbskConstants.BODY_PART_LEG);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.rightFibula, RbskConstants.BODY_PART_LEG);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.rightFoot, RbskConstants.BODY_PART_LEG);
            bindEventListenerOnClick(childFrontBodyLayout, R.id.genitaliaFront, RbskConstants.BODY_PART_GENITALIA);
        } else if (childBackBodyLayout != null) {
            bindEventListenerOnClick(childBackBodyLayout, R.id.back, RbskConstants.BODY_PART_BACK);
            bindEventListenerOnClick(childBackBodyLayout, R.id.genitaliaBack, RbskConstants.BODY_PART_GENITALIA);
        }
    }

    private void bindEventListenerOnClick(RelativeLayout childBodyLayout, int id, final String bodyPartString) {
        final Button button = childBodyLayout.findViewById(id);
        button.setOnClickListener(v -> {
            showProcessDialog();
            bodyPart = bodyPartString;
            setDefectSelectionScreen();
        });
    }

    @Background
    public void setDefectSelectionScreen() {
        if (bodyPart != null) {
            try {
                LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
                parameters.put("bodyPart", bodyPart + "','" + RbskConstants.BODY_PART_SKIN);

                QueryMobDataBean queryResponse = sewaServiceRestClient.executeQuery(
                        new QueryMobDataBean("mob_rbsk_defect_list_by_body_part", null, parameters, 0));

                if (queryResponse != null && queryResponse.getResult() != null && !queryResponse.getResult().isEmpty()) {
                    defects = queryResponse.getResult();
                    viewDefectSelectionScreen();
                } else {
                    runOnUiThread(() -> SewaUtil.generateToast(context, LabelConstants.NO_DEFECTS_FOUND_FOR_THE_BODY_PART));
                    hideProcessDialog();
                }
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), e.getMessage());
                hideProcessDialog();
            }
        }
    }

    @UiThread
    public void viewDefectSelectionScreen() {
        bodyLayoutContainer.removeAllViews();
        screen = DISEASE_SELECTION_SCREEN;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        layoutParams.setMargins(150, 20, 150, 20);
        ImageView bodyPartImage = MyStaticComponents.getImageView(context, -1, -1, layoutParams);
        bodyPartImage.setAdjustViewBounds(true);
        Integer tmpImgId = RbskConstants.BODY_PART_IMAGE_IDS.get(bodyPart);
        if (tmpImgId != null) {
            bodyPartImage.setImageResource(tmpImgId);
        }
        bodyLayoutContainer.addView(bodyPartImage);

        TextView title = MyStaticComponents.generateTitleView(context, UtilBean.getMyLabel(RbskConstants.BODY_PART_FULL_NAME.get(bodyPart)) + " Defects");
        title.setPadding(0, 0, 0, 30);
        bodyLayoutContainer.addView(title);

        Object tmpDataObj;
        for (LinkedHashMap<String, Object> defect : defects) {
            LinearLayout defectLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.rbsk_defect_list_layout, null);

            tmpDataObj = defect.get("id");
            if (tmpDataObj != null) {
                int defectId = Double.valueOf(tmpDataObj.toString()).intValue();

                TextView defectName = defectLayout.findViewById(R.id.defect_name);
                defectName.setText(UtilBean.getNotAvailableIfNull(defect.get(DEFECT_NAME)));

                TextView defectDescription = defectLayout.findViewById(R.id.defect_desc);
                defectDescription.setText(UtilBean.getNotAvailableIfNull(defect.get(DESCRIPTION)));

                tmpDataObj = defect.get("photo_required");
                if (tmpDataObj != null && Boolean.valueOf(tmpDataObj.toString()).equals(Boolean.TRUE)) {
                    ImageView imageView = defectLayout.findViewById(R.id.capture_pic);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setLabelFor(defectId);

                    imageView.setOnClickListener((view -> {
                        if (selectedDefects.contains(view.getLabelFor())) {
                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            selectedDefect = view.getLabelFor();
                            startActivityForResult(intent, GlobalTypes.PHOTO_CAPTURE_ACTIVITY);
                        } else {
                            SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.DEFECT_SELECTION_REQUIRED_TO_CAPTURE_IMAGE));
                        }
                    }));
                }

                if (defect.get("defect_image") != null) {
                    getImageFromServer(defect, defectLayout);
                } else {
                    setDefaultImage(defectLayout);
                    addDefectDescriptionPopUp(defectLayout, null, defect);
                }

                CheckBox checkBox = defectLayout.findViewById(R.id.defect_checkbox);
                checkBox.setId(defectId);
                checkBox.setChecked(selectedDefects.contains(defectId));
                checkBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                    if (isChecked) {
                        selectedDefects.add(buttonView.getId());
                    } else {
                        selectedDefects.remove(buttonView.getId());
                    }
                }));
                bodyLayoutContainer.addView(defectLayout);
            }
        }

        Button moreDefects = MyStaticComponents.getCustomButton(context, LabelConstants.MORE_DEFECTS, -1, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        moreDefects.setOnClickListener(v -> {
            setPrimaryButton(R.id.frontBody);
            setScreeningScreen();
        });
        bodyLayoutContainer.addView(moreDefects);

        nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        hideProcessDialog();
    }

    @Background
    public void getImageFromServer(Map<String, Object> defect, LinearLayout defectLayout) {
        try {
            Object tmpObj = defect.get("defect_image");
            if (tmpObj != null) {
                long imageId = ((Double) tmpObj).longValue();
                String url = WSConstants.REST_TECHO_SERVICE_URL + "getfileById?id=" + imageId;
                ResponseBody responseBody = apiManager.execute(apiManager.getInstance().getFile(url));
                if (responseBody == null)
                    return;

                if (responseBody.bytes().length == 0) {
                    byte[] bytes = responseBody.bytes();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    setImage(defectLayout, bitmap);
                    addDefectDescriptionPopUp(defectLayout, bitmap, defect);
                } else {
                    setDefaultImage(defectLayout);
                    addDefectDescriptionPopUp(defectLayout, null, defect);
                }

            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), null, e);
            setDefaultImage(defectLayout);
            addDefectDescriptionPopUp(defectLayout, null, defect);
        }
    }

    @UiThread
    public void addDefectDescriptionPopUp(LinearLayout defectLayout, Bitmap bitmap, Map<String, Object> defect) {
        final PopupWindow popupWindow = new PopupWindow(defectLayout, MATCH_PARENT, MATCH_PARENT, true);

        final LinearLayout popUpLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.defect_pop_window, null);
        MaterialTextView textView = popUpLayout.findViewById(R.id.toolbar_title);
        textView.setTextAppearance(context, R.style.ToolbarTitle);
        textView.setText(UtilBean.getTitleText(LabelConstants.HEAD_TO_TOE_SCREENING_TITLE));

        Object tmpObject = memberDataBean.get(CHILD_NAME);
        MaterialTextView subTitleTextView = popUpLayout.findViewById(R.id.toolbar_subtitle);
        if (tmpObject != null) {
            subTitleTextView.setVisibility(View.VISIBLE);
            subTitleTextView.setText(UtilBean.getMyLabel(tmpObject.toString()));
            subTitleTextView.setTextAppearance(context, R.style.ToolbarSubtitle);
        } else {
            subTitleTextView.setVisibility(View.GONE);
        }

        TextView defectName = popUpLayout.findViewById(R.id.defect_title);
        defectName.setText((UtilBean.getNotAvailableIfNull(defect.get(DEFECT_NAME))));

        TextView description = popUpLayout.findViewById(R.id.defect_description);
        description.setText(UtilBean.getNotAvailableIfNull(defect.get(DESCRIPTION)));

        ImageView imageView = popUpLayout.findViewById(R.id.defect_image);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.no_image_available);
        }

        Button closeButton = popUpLayout.findViewById(R.id.defect_close_button);
        closeButton.setTypeface(SharedStructureData.typeface, Typeface.BOLD);
        closeButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_BACK));
        closeButton.setOnClickListener(v -> popupWindow.dismiss());

        popupWindow.setContentView(popUpLayout);

        TextView title = defectLayout.findViewById(R.id.defect_name);
        title.setOnClickListener(v -> popupWindow.showAtLocation(popUpLayout, Gravity.CENTER, 0, 0));
        ImageView defectInfo = defectLayout.findViewById(R.id.defect_info);
        defectInfo.setVisibility(View.VISIBLE);
        defectInfo.setOnClickListener(v -> popupWindow.showAtLocation(popUpLayout, Gravity.CENTER, 0, 0));
    }

    @UiThread
    public void setImage(LinearLayout defectLayout, Bitmap bitmap) {
        ImageView defectImage = defectLayout.findViewById(R.id.defect_image);
        defectImage.setImageBitmap(bitmap);
    }

    @UiThread
    public void setDefaultImage(LinearLayout defectLayout) {
        ImageView defectImage = defectLayout.findViewById(R.id.defect_image);
        defectImage.setImageResource(R.drawable.no_image_available);
    }

    @Background
    public void retrieveSelectedDefectDetails() {
        if (selectedDefects != null && !selectedDefects.isEmpty()) {
            showProcessDialog();
            try {
                LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
                String param = "(" + (selectedDefects.isEmpty() ? null : selectedDefects.toString().substring(1, selectedDefects.toString().length() - 1)) + ")";
                parameters.put("ids", param);
                QueryMobDataBean queryResponse = sewaServiceRestClient.executeQuery(
                        new QueryMobDataBean("mob_rbsk_defect_by_id", null, parameters, 0));

                List<LinkedHashMap<String, Object>> allSelectedDefects = queryResponse.getResult();
                setSelectedDefectsScreen(allSelectedDefects);
            } catch (RestHttpException e) {
                Log.e(getClass().getSimpleName(), null, e);
                generateToaster(LabelConstants.ERROR_OCCURRED_SO_TRY_AGAIN);
            }
        } else {
            generateToaster(LabelConstants.DEFECT_SELECTION_REQUIRED);
        }
    }

    @UiThread
    public void setSelectedDefectsScreen(List<LinkedHashMap<String, Object>> defects) {
        screen = SELECTED_DEFECT_LIST_SCREEN;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context, UtilBean.getMyLabel(LabelConstants.SELECTED_DEFECT_FOR_CHILD)));

        Object tmpObj;
        for (LinkedHashMap<String, Object> defect : defects) {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.rbsk_selected_defect_list_layout, null);
            TextView defectName = layout.findViewById(R.id.defect_name);
            tmpObj = defect.get(DEFECT_NAME);
            if (tmpObj != null) {
                defectName.setText(tmpObj.toString());
            }

            tmpObj = defect.get("id");
            int defectId = -1;
            if (tmpObj != null) {
                defectId = Double.valueOf(tmpObj.toString()).intValue();
            }
            byte[] bytes = images.get(defectId);
            ImageView imageView = layout.findViewById(R.id.defect_image);
            if (bytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(images.get(defectId), 0, bytes.length);
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.no_image_available);
            }

            tmpObj = defect.get("referral_required");
            if (tmpObj != null && Boolean.valueOf(tmpObj.toString()).equals(Boolean.TRUE)) {
                isReferralRequired = true;
            }

            bodyLayoutContainer.addView(layout);
        }
        hideProcessDialog();
    }

    @Background
    public void getStabilizationInfoForDefects() {
        try {
            LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
            String param = "(" + (selectedDefects.isEmpty() ? null : selectedDefects.toString().substring(1, selectedDefects.toString().length() - 1)) + ")";
            parameters.put("defectIds", param);

            QueryMobDataBean queryResponse = sewaServiceRestClient.executeQuery(
                    new QueryMobDataBean("mob_rbsk_defect_stabilization_info", null, parameters, 0));

            if (queryResponse != null && queryResponse.getResult() != null) {
                List<LinkedHashMap<String, Object>> resultList = queryResponse.getResult();
                viewStabilizationInfoForDefects(resultList);
            } else {
                Log.d(getClass().getSimpleName(), NULL_QUERY_RESPONSE);
                hideProcessDialog();
            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
    }

    @UiThread
    public void viewStabilizationInfoForDefects(List<LinkedHashMap<String, Object>> result) {
        if (!result.isEmpty()) {
            screen = STABILIZATION_INFO_SCREEN;
            bodyLayoutContainer.removeAllViews();

            bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context,
                    UtilBean.getMyLabel(LabelConstants.INSTRUCTIONS_TO_STABILIZE_DEFECTS + ":")));
            int counter = 1;
            Object tmpObj;
            for (LinkedHashMap<String, Object> entry : result) {
                tmpObj = entry.get(DESCRIPTION);
                if (tmpObj != null) {
                    bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                            (counter++) + ". " + tmpObj));
                }
            }
        } else {
            if (screen.equals(SCREENING_COMPLETION_SCREEN) || screen.equals(REFERRAL_CONFORMATION_SCREEN)) {
                retrieveSelectedDefectDetails();
            } else {
                if (isReferralRequired) {
                    viewReferralConformationScreen();
                } else {
                    setScreeningCompletionScreen();
                }
            }
        }
        hideProcessDialog();
    }

    @UiThread
    public void viewReferralConformationScreen() {
        screen = REFERRAL_CONFORMATION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.BENEFICIARY_READY_FOR_REFERRAL)));
        bodyLayoutContainer.addView(referralConformationRadioGroup);
    }

    @UiThread
    public void viewHealthInfrastructureScreen() {
        screen = HEALTH_INFRASTRUCTURE_SCREEN;
        bodyLayoutContainer.removeAllViews();
        if (healthInfraComponent == null) {
            healthInfraComponent = MyDynamicComponents.getHealthInfrastructureComponent(context, healthInfraQueFormBean);
        }
        bodyLayoutContainer.addView(healthInfraComponent);
    }

    @Background
    public void storeScreeningDetails() {
        RbskScreeningDetailDto screeningDetail = new RbskScreeningDetailDto();
        screeningDetail.setToken(SewaTransformer.loginBean.getUserToken());
        screeningDetail.setMemberId(Double.valueOf((Objects.requireNonNull(memberDataBean.get("child_id")).toString())).intValue());
        screeningDetail.setFamilyId(Double.valueOf(Objects.requireNonNull(memberDataBean.get("family_id")).toString()).intValue());
        screeningDetail.setLocationId(Double.valueOf(Objects.requireNonNull(memberDataBean.get("location_id")).toString()).intValue());
        screeningDetail.setScreeningDate(new Date());
        screeningDetail.setDoneFrom(LabelConstants.DONE_FROM_SCREENING);

        if (selectedDefects != null) {
            for (int i : selectedDefects) {
                RbskDefectDetailDto defectDetail = new RbskDefectDetailDto();
                defectDetail.setDefectId(i);
                defectDetail.setMemberId(Double.valueOf(Objects.requireNonNull(memberDataBean.get("child_id")).toString()).intValue());

                if (images.get(i) != null) {
                    defectDetail.setPhotoData(images.get(i));
                }

                if (screeningDetail.getRbskScreeningDefectDto() == null) {
                    ArrayList<RbskDefectDetailDto> rbskDefectDetailDtos = new ArrayList<>();
                    screeningDetail.setRbskScreeningDefectDto(rbskDefectDetailDtos);
                }
                screeningDetail.getRbskScreeningDefectDto().add(defectDetail);
            }
        }

        if (heightText.getEditText() != null) {
            screeningDetail.setLength(Integer.valueOf(heightText.getEditText().getText().toString()));
        }
        if (headCircumferenceText.getEditText() != null) {
            screeningDetail.setHeadCircumference(Integer.valueOf(headCircumferenceText.getEditText().getText().toString()));
        }
        screeningDetail.setApgar1(RbskConstants.APGARS.get(apgar1MinRadioGroup.getCheckedRadioButtonId()));
        screeningDetail.setApgar5(RbskConstants.APGARS.get(apgar5MinRadioGroup.getCheckedRadioButtonId()));
        screeningDetail.setBabyTone(RbskConstants.TONE_OF_BODY_DEFECT_VALUE.get(toneOfTheBabyRadioGroup.getCheckedRadioButtonId()));
        screeningDetail.setMoroReflex(RbskConstants.COMMON_DEFECT_SELECTION_VALUES.get(moroReflexRadioGroup.getCheckedRadioButtonId()));
        screeningDetail.setStepReflex(RbskConstants.COMMON_DEFECT_SELECTION_VALUES.get(stepReflexRadioGroup.getCheckedRadioButtonId()));
        screeningDetail.setPlantarReflex(RbskConstants.COMMON_DEFECT_SELECTION_VALUES.get(plantarReflexRadioGroup.getCheckedRadioButtonId()));
        screeningDetail.setRootReflex(RbskConstants.COMMON_DEFECT_SELECTION_VALUES.get(rootReflexRadioGroup.getCheckedRadioButtonId()));
        screeningDetail.setAssymetricalTonic(RbskConstants.COMMON_DEFECT_SELECTION_VALUES.get(asymmetricalTonicRadioGroup.getCheckedRadioButtonId()));
        screeningDetail.setSuckingReflex(RbskConstants.COMMON_DEFECT_SELECTION_VALUES.get(suckingReflexRadioGroup.getCheckedRadioButtonId()));
        screeningDetail.setGraspReflex(RbskConstants.COMMON_DEFECT_SELECTION_VALUES.get(graspReflexRadioGroup.getCheckedRadioButtonId()));

        screeningDetail.setVisibleDefects(visibleDefectsPresent);

        if (referralConformationRadioGroup != null && referralConformationRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_YES) {
            screeningDetail.setReferralDone(true);
            screeningDetail.setReferralPlace(Integer.parseInt(healthInfraQueFormBean.getAnswer().toString()));
        } else {
            screeningDetail.setReferralDone(false);
        }

        try {
            sewaServiceRestClient.storeRbskScreeningDetails(screeningDetail);
            runOnUiThread(() -> SewaUtil.generateToast(context, LabelConstants.HEAD_TO_TOE_SCREENING_FORM_SUBMITTED_SUCCESSFULLY));
            navigateToHomeScreen(false);
        } catch (Exception e) {
            generateToaster(LabelConstants.ERROR_OCCURRED_SO_TRY_AFTER_SOME_TIME);
        }

        hideProcessDialog();
    }

    @UiThread
    public void setScreeningCompletionScreen() {
        screen = SCREENING_COMPLETION_SCREEN;
        nextButton.setText(GlobalTypes.EVENT_SUBMIT);
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context,
                UtilBean.getMyLabel(LabelConstants.YOUR_FORM_IS_COMPLETE)));

        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context,
                UtilBean.getMyLabel(LabelConstants.SUCCESSFULLY_COMPLTED_SCREENING_FOR_CHILD)));
    }

    private void showNotOnlineMessage(final boolean navigateToHomeScreen) {
        if (!sewaService.isOnline()) {
            View.OnClickListener myListener = v -> {
                myAlertDialog.dismiss();
                if (navigateToHomeScreen)
                    navigateToHomeScreen(false);
            };
            myAlertDialog = new MyAlertDialog(context,
                    UtilBean.getMyLabel(LabelConstants.NETWORK_CONNECTIVITY_ALERT),
                    myListener, DynamicUtils.BUTTON_OK);
            myAlertDialog.show();
            myAlertDialog.setOnCancelListener(dialog -> {
                myAlertDialog.dismiss();
                if (navigateToHomeScreen)
                    navigateToHomeScreen(false);
            });
        }
    }

    @UiThread
    public void generateToaster(String title) {
        SewaUtil.generateToast(this, title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GlobalTypes.PHOTO_CAPTURE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                SewaUtil.generateToast(this, LabelConstants.PHOTO_CAPTURED);
                if (data.getExtras() != null) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    if (photo != null) {
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        images.put(selectedDefect, stream.toByteArray());
                    }
                }
            } else {
                SewaUtil.generateToast(this, LabelConstants.FAILED_TO_TAKE_PHOTO);
            }
        } else if (requestCode == GlobalTypes.LOCATION_SERVICE_ACTIVITY) {
            setGps();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (screen == null || screen.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }

        scrollView.scrollTo(0, 0);
        switch (screen) {
            case MEMBER_SEARCH_SCREEN:
                navigateToHomeScreen(false);
                break;

            case MEMBER_DETAILS_SCREEN:
                setSubTitle(null);
                selectedMemberIndex = -1;
                memberDataBean = null;
                familyDataBean = null;
                addSearchScreen();
                break;

            case CHILD_BODY_MEASUREMENT_SCREEN:
                setMemberDetailsView();
                break;

            case APGAR_QUESTION_SCREEN:
                setChildBodyMeasurementScreen();
                break;

            case COMMON_DEFECTS_SCREEN_1:
                setApgarQuestionView();
                break;

            case COMMON_DEFECTS_SCREEN_2:
                setCommonDefectsScreen1();
                break;

            case SCREENING_CONFIRMATION_SCREEN:
                nextButton.setVisibility(View.VISIBLE);
                setCommonDefectsScreen2();
                break;

            case SCREENING_SCREEN:
                bodyLayoutContainer.removeAllViews();
                screen = MEMBER_DETAILS_SCREEN;
                nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                setScreeningConfirmationScreen();
                break;

            case DISEASE_SELECTION_SCREEN:
            case SELECTED_DEFECT_LIST_SCREEN:
                setScreeningScreen();
                break;

            case STABILIZATION_INFO_SCREEN:
                retrieveSelectedDefectDetails();
                break;

            case REFERRAL_CONFORMATION_SCREEN:
                if (!visibleDefectsPresent) {
                    setScreeningConfirmationScreen();
                } else {
                    getStabilizationInfoForDefects();
                }
                break;

            case HEALTH_INFRASTRUCTURE_SCREEN:
                viewReferralConformationScreen();
                break;

            case SCREENING_COMPLETION_SCREEN:
                nextButton.setText(GlobalTypes.EVENT_NEXT);
                if (isReferralRequired) {
                    if (referralConformationRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_NO) {
                        viewReferralConformationScreen();
                    } else {
                        viewHealthInfrastructureScreen();
                    }
                } else {
                    if (!visibleDefectsPresent) {
                        setScreeningConfirmationScreen();
                    } else {
                        getStabilizationInfoForDefects();
                    }
                }
                break;

            default:
                break;
        }
        return true;
    }
}
