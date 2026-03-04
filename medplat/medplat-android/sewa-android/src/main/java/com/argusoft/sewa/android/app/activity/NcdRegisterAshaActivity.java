package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.NcdServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.MemberCbacDetailBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by prateek on 8/27/19
 */
@EActivity
public class NcdRegisterAshaActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    @Bean
    SewaServiceImpl sewaService;
    @Bean
    NcdServiceImpl ncdService;

    private static final long DELAY = 500;
    private static final String ASHA_AREA_SELECTION_SCREEN = "ashaAreaSelectionScreen";
    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";
    private static final String CBAC_SUMMARY_SCREEN = "cbacSummaryScreen";
    private static final long LIMIT = 30;
    private static final String TAG = "NcdRegisterAshaActivity";

    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private Spinner ashaAreaSpinner;
    private TextInputLayout searchText;
    private String screen;
    private Timer timer = new Timer();
    private List<LocationBean> ashaAreaList = new ArrayList<>();
    private Integer selectedAshaArea;
    private List<MemberDataBean> memberDataBeans;
    private MemberDataBean selectedMember;
    private PagingListView paginatedListView;
    private int selectedMemberIndex = -1;
    private long offset;
    private ArrayList<Integer> ashaArea;
    private String searchString;
    private MaterialTextView titleView;
    private MaterialTextView noMemberAvilableView;
    private LinearLayout globalPanel;
    private LinkedHashMap<String, String> qrScanFilter = new LinkedHashMap<>();

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
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        setTitle(UtilBean.getTitleText(LabelConstants.NCD_REGISTER_TITLE));
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        setBodyDetail();
    }

    @Background
    public void setBodyDetail() {
        startLocationSelectionActivity();
    }

    private void startLocationSelectionActivity() {
        Intent myIntent = new Intent(context, LocationSelectionActivity_.class);
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.NCD_REGISTER_TITLE);
        startActivityForResult(myIntent, ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onBackPressed() {
        View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                alertDialog.dismiss();
                navigateToHomeScreen(false);
                finish();
            } else {
                alertDialog.dismiss();
            }
        };

        alertDialog = new MyAlertDialog(this,
                LabelConstants.ON_NCD_REGISTER_CLOSE_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            setSubTitle(null);
            switch (screen) {
                case ASHA_AREA_SELECTION_SCREEN:
                    showProcessDialog();
                    String selectedArea = ashaAreaSpinner.getSelectedItem().toString();
                    for (LocationBean locationBean : ashaAreaList) {
                        if (selectedArea.equals(locationBean.getName())) {
                            selectedAshaArea = locationBean.getActualID();
                            break;
                        }
                    }
                    bodyLayoutContainer.removeAllViews();
                    addSearchTextBoxForMember();
                    retrieveMemberListFromDB(null, null);
                    break;
                case MEMBER_SELECTION_SCREEN:
                    if (selectedMemberIndex != -1) {
                        selectedMember = memberDataBeans.get(selectedMemberIndex);
                        setCBACSummaryScreen();
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.SELECT_MEMBER_OF_THE_FAMILY));
                    }
                    break;

                case CBAC_SUMMARY_SCREEN:
                    bodyLayoutContainer.removeAllViews();
                    addSearchTextBoxForMember();
                    retrieveMemberListFromDB(null, null);
                    break;
                default:
            }
        }
    }

    @UiThread
    public void addAshaAreaSelectionSpinner() {
        screen = ASHA_AREA_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        setSubTitle(null);
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.SELECT_ASHA_AREA));
        String[] arrayOfOptions = new String[ashaAreaList.size()];
        int i = 0;
        for (LocationBean locationBean : ashaAreaList) {
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }
        ashaAreaSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 2);
        if (selectedAshaArea != null) {
            for (LocationBean locationBean : ashaAreaList) {
                String locationName;
                if (locationBean.getActualID().intValue() == selectedAshaArea.intValue()) {
                    locationName = locationBean.getName();
                    ashaAreaSpinner.setSelection(Arrays.asList(arrayOfOptions).indexOf(locationName));
                }
            }
        }
        bodyLayoutContainer.addView(ashaAreaSpinner);
        hideProcessDialog();
    }

    @UiThread
    public void addSearchTextBoxForMember() {
        if (searchText == null) {
            searchText = MyStaticComponents.getEditTextWithQrScan(this, LabelConstants.MEMBER_ID_OR_NAME_OR_HEALTH_ID_TO_SEARCH, 1, 15, 1);
            if (searchText.getEditText() != null) {
                searchText.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //not implemented
                    }

                    @Override
                    public void onTextChanged(final CharSequence s, int start, int before, int count) {
                        timer.cancel();
                        timer = new Timer();
                        timer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        if (s != null && s.length() > 2) {
                                            runOnUiThread(() -> {
                                                retrieveMemberListFromDB(s.toString(), null);
                                                searchText.setFocusable(true);
                                            });
                                        } else if (s == null || s.length() == 0) {
                                            runOnUiThread(() -> {
                                                showProcessDialog();
                                                retrieveMemberListFromDB(null, null);
                                            });
                                        }
                                    }
                                },
                                DELAY
                        );
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        //not implemented
                    }
                });
            }
        }
        bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
        bodyLayoutContainer.addView(searchText);
        bodyLayoutContainer.addView(MyStaticComponents.getOrTextView(context));
    }

    @Background
    public void retrieveMemberListFromDB(String search, String qrData) {
        ashaArea = new ArrayList<>();
        searchString = search;
        qrScanFilter = SewaUtil.setQrScanFilterData(qrData);
        ashaArea.add(selectedAshaArea);
        offset = 0;
        selectedMemberIndex = -1;
        memberDataBeans = ncdService.retrieveMembersForNcdRegisterAsha(ashaArea, search, LIMIT, offset, qrScanFilter);
        offset = offset + LIMIT;
        setMemberSelectionScreen(search != null);
    }

    @UiThread
    public void setMemberSelectionScreen(boolean isSearch) {
        screen = MEMBER_SELECTION_SCREEN;
        setSubTitle(null);
        bodyLayoutContainer.removeView(paginatedListView);
        bodyLayoutContainer.removeView(titleView);
        bodyLayoutContainer.removeView(noMemberAvilableView);
        nextButton.setText(GlobalTypes.EVENT_NEXT);
        nextButton.setOnClickListener(this);

        if (!memberDataBeans.isEmpty()) {
            titleView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_MEMBER);
            bodyLayoutContainer.addView(titleView);

            List<ListItemDataBean> list = getMemberList(memberDataBeans);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedMemberIndex = position;
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
        } else {
            noMemberAvilableView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBERS_FOUND);
            bodyLayoutContainer.addView(noMemberAvilableView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
            nextButton.setOnClickListener(v -> navigateToHomeScreen(false));
        }
        hideProcessDialog();
    }

    private List<ListItemDataBean> getMemberList(List<MemberDataBean> memberDataBeans) {
        List<ListItemDataBean> list = new ArrayList<>();
        for (MemberDataBean memberDataBean : memberDataBeans) {
            list.add(new ListItemDataBean(memberDataBean.getFamilyId(), memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean), null, null));
        }
        return list;
    }

    @UiThread
    public void setCBACSummaryScreen() {
        screen = CBAC_SUMMARY_SCREEN;
        bodyLayoutContainer.removeAllViews();
        setSubTitle(UtilBean.getMemberFullName(selectedMember));

        nextButton.setText(GlobalTypes.EVENT_OKAY);
        selectedMemberIndex = -1;
        if (selectedMember != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.NAME));
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMemberFullName(selectedMember)));

            MemberCbacDetailBean memberCbacDetailBean = ncdService.retrieveMemberCbacDetailBean(Long.valueOf(selectedMember.getId()));

            if (memberCbacDetailBean != null) {
                List<String> items = new ArrayList<>();

                //Risk Assessment
                if (memberCbacDetailBean.getSmokeOrConsumeGutka() != null && memberCbacDetailBean.getSmokeOrConsumeGutka().equals("IN_PAST")) {
                    items.add(UtilBean.getMyLabel(LabelConstants.USED_TO_SMOKE_OR_CONSUME_GUTKA_IN_PAST));
                } else if (memberCbacDetailBean.getSmokeOrConsumeGutka() != null && memberCbacDetailBean.getSmokeOrConsumeGutka().equals("DAILY")) {
                    items.add(UtilBean.getMyLabel(LabelConstants.SMOKE_OR_CONSUME_GUTKA_DAILY));
                }

                if (memberCbacDetailBean.getConsumeAlcoholDaily() != null && memberCbacDetailBean.getConsumeAlcoholDaily()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.CONSUME_ALCOHOL_DAILY_TAG));
                }

                if (memberCbacDetailBean.getPhysicalActivity150Min() != null && memberCbacDetailBean.getPhysicalActivity150Min().equals("LESS_THAN_150")) {
                    items.add(UtilBean.getMyLabel(LabelConstants.DOESNT_TAKING_PART_IN_PHYSICAL_ACTIVITY_FOR_MINIMUM_150_MINUTES_IN_WEEK));
                }

                if (memberCbacDetailBean.getBpDiabetesHeartHistory() != null && memberCbacDetailBean.getBpDiabetesHeartHistory()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.FAMILY_HISTORY_WITH_BP_DIABETES_OR_HEART_DISEASE));
                }

                if (memberCbacDetailBean.getScore() != null) {
                    items.add(UtilBean.getMyLabel(LabelConstants.CBAC_SCORE) + " - " + memberCbacDetailBean.getScore() + ".");
                }

                if (!items.isEmpty()) {
                    bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.RISK_ASSESSMENT));
                    for (String item : items) {
                        MaterialTextView textView = MyStaticComponents.generateAnswerView(this, item);
                        textView.setText(UtilBean.addBullet(item));
                        bodyLayoutContainer.addView(textView);
                    }
                }

                //Early Detection
                items.clear();

                if (memberCbacDetailBean.getShortnessOfBreath() != null && memberCbacDetailBean.getShortnessOfBreath()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.SORTNESS_OF_BREATH));
                }

                if (memberCbacDetailBean.getFitsHistory() != null && memberCbacDetailBean.getFitsHistory()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.HISTORY_OF_FITS));
                }

                if (memberCbacDetailBean.getTwoWeeksCoughing() != null && memberCbacDetailBean.getTwoWeeksCoughing()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.COUGHING_MORE_THAN_2_WEEKS));
                }

                if (memberCbacDetailBean.getMouthOpeningDifficulty() != null && memberCbacDetailBean.getMouthOpeningDifficulty()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.DIFFICULTY_IN_OPENING_MOUTH));
                }

                if (memberCbacDetailBean.getBloodInSputum() != null && memberCbacDetailBean.getBloodInSputum()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.BLOOD_IN_SPUTUM));
                }

                if (memberCbacDetailBean.getTwoWeeksFever() != null && memberCbacDetailBean.getTwoWeeksFever()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.FEVER_MORE_THAN_2_WEEKS));
                }

                if (memberCbacDetailBean.getChangeInToneOfVoice() != null && memberCbacDetailBean.getChangeInToneOfVoice()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.CHANGE_IN_TONE_OF_VOICE));
                }

                if (memberCbacDetailBean.getPatchOnSkin() != null && memberCbacDetailBean.getPatchOnSkin()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.PATCH_ON_SKIN));
                }

                if (memberCbacDetailBean.getNightSweats() != null && memberCbacDetailBean.getNightSweats()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.NIGHT_SWEETS));
                }

                if (memberCbacDetailBean.getDifficultyHoldingObjects() != null && memberCbacDetailBean.getDifficultyHoldingObjects()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.DIFFICULTY_IN_HOLDING_OBJECTS_WITH_FINGURES));
                }

                if (memberCbacDetailBean.getSensationLossPalm() != null && memberCbacDetailBean.getSensationLossPalm()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.LOSS_OF_SENSATION_IN_PALM));
                }

                if (memberCbacDetailBean.getTakingAntiTbDrugs() != null && memberCbacDetailBean.getTakingAntiTbDrugs()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.CURRENTLY_TAKING_ANY_ANTI_TB_DRUGS));
                }

                if (memberCbacDetailBean.getFamilyMemberSufferingFromTb() != null && memberCbacDetailBean.getFamilyMemberSufferingFromTb()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.SOMEONE_IN_FAMILY_SUFFERING_FROM_TB));
                }

                if (memberCbacDetailBean.getHistoryOfTb() != null && memberCbacDetailBean.getHistoryOfTb()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.HISTORY_OF_TB));
                }

                if (memberCbacDetailBean.getLumpInBreast() != null && memberCbacDetailBean.getLumpInBreast()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.LUMP_IN_BREAST));
                }

                if (memberCbacDetailBean.getBleedingAfterMenopause() != null && memberCbacDetailBean.getBleedingAfterMenopause()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.BLEEDING_AFTER_MENOPAUSE));
                }

                if (memberCbacDetailBean.getNippleBloodStainedDischarge() != null && memberCbacDetailBean.getNippleBloodStainedDischarge()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.NIPPLE_BLOOD_STAINED_DISCHARGE));
                }

                if (memberCbacDetailBean.getBleedingAfterIntercourse() != null && memberCbacDetailBean.getBleedingAfterIntercourse()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.BLEEDING_AFTER_INTERCOURSE));
                }

                if (memberCbacDetailBean.getChangeInSizeOfBreast() != null && memberCbacDetailBean.getChangeInSizeOfBreast()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.CHANGE_IN_SIZE_OF_BREAST));
                }

                if (memberCbacDetailBean.getFoulVaginalDischarge() != null && memberCbacDetailBean.getFoulVaginalDischarge()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.FOUL_VAGINAL_DISCHARGE));
                }

                if (memberCbacDetailBean.getBleedingBetweenPeriods() != null && memberCbacDetailBean.getBleedingBetweenPeriods()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.BLEEDING_BETWEEN_PERIODS));
                }

                if (memberCbacDetailBean.getOccupationalExposure() != null && memberCbacDetailBean.getOccupationalExposure().equals("CROP_BURNING")) {
                    items.add(UtilBean.getMyLabel(LabelConstants.EXPOSURE_FROM_BURNING));
                } else if (memberCbacDetailBean.getOccupationalExposure() != null && memberCbacDetailBean.getOccupationalExposure().equals("INDUSTRY_WORK")) {
                    items.add(UtilBean.getMyLabel(LabelConstants.WORKING_WITH_INDUSTRIES));
                }

                if (!items.isEmpty()) {
                    bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.EARLY_DETECTION));

                    for (String item : items) {
                        MaterialTextView textView = MyStaticComponents.generateAnswerView(this, item);
                        textView.setText(UtilBean.addBullet(item));
                        bodyLayoutContainer.addView(textView);
                    }
                }

                //Menstrual History
                items.clear();

                if (selectedMember.getGender().equals(GlobalTypes.FEMALE)) {
                    if (memberCbacDetailBean.getMenopauseArrived() != null && memberCbacDetailBean.getMenopauseArrived()) {
                        items.add(UtilBean.getMyLabel(LabelConstants.MANOPAUSE_ARRIVED));
                    }

                    if (memberCbacDetailBean.getDurationOfMenoapuse() != null) {
                        items.add(UtilBean.getMyLabel(LabelConstants.DURATION_OF_MANOPAUSE) + " - " + memberCbacDetailBean.getDurationOfMenoapuse() + " " + LabelConstants.YEARS);
                    }

                    if (memberCbacDetailBean.getPregnant() != null && memberCbacDetailBean.getPregnant()) {
                        items.add(UtilBean.getMyLabel(LabelConstants.PREGNANT));
                    }

                    if (memberCbacDetailBean.getLactating() != null && memberCbacDetailBean.getLactating()) {
                        items.add(UtilBean.getMyLabel(LabelConstants.LACTATING));
                    }

                    if (memberCbacDetailBean.getRegularPeriods() != null && !memberCbacDetailBean.getRegularPeriods()) {
                        items.add(UtilBean.getMyLabel(LabelConstants.IRREGULAR_PERIOD));
                    }

                    if (memberCbacDetailBean.getBleeding() != null && memberCbacDetailBean.getBleeding().equals("HEAVY")) {
                        items.add(UtilBean.getMyLabel(LabelConstants.HEAVY_BLEEDING));
                    }

                    if (memberCbacDetailBean.getAssociatedWith() != null && memberCbacDetailBean.getAssociatedWith().equals("PAIN")) {
                        items.add(UtilBean.getMyLabel(LabelConstants.ASSOCIATED_WITH_PAIN));
                    }

                    if (!items.isEmpty()) {
                        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.MENSTRUAL_HISTORY));

                        for (String item : items) {
                            MaterialTextView textView = MyStaticComponents.generateAnswerView(this, item);
                            textView.setText(UtilBean.addBullet(item));
                            bodyLayoutContainer.addView(textView);
                        }
                    }
                }

                //Personal History
                items.clear();

                if (memberCbacDetailBean.getDiagnosedForHypertension() != null && memberCbacDetailBean.getDiagnosedForHypertension()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.DIAGNOSED_FOR_HYPERTENSION_TAG));
                }

                if (memberCbacDetailBean.getDiagnosedForDiabetes() != null && memberCbacDetailBean.getDiagnosedForDiabetes()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.DIAGNOSED_FOR_DIABETES_TAG));
                }

                if (memberCbacDetailBean.getDiagnosedForHeartDiseases() != null && memberCbacDetailBean.getDiagnosedForHeartDiseases()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.DIAGNOSED_FOR_HEART_DISEASES_TAG));
                }

                if (memberCbacDetailBean.getDiagnosedForStroke() != null && memberCbacDetailBean.getDiagnosedForStroke()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.DIAGNOSED_FOR_STROKE_TAG));
                }

                if (memberCbacDetailBean.getDiagnosedForKidneyFailure() != null && memberCbacDetailBean.getDiagnosedForKidneyFailure()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.DIAGNOSED_FOR_KIDNEY_FAILURE_TAG));
                }

                if (memberCbacDetailBean.getDiagnosedForNonHealingWound() != null && memberCbacDetailBean.getDiagnosedForNonHealingWound()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.DIAGNOSED_FOR_NON_HEALING_WOUND_TAG));
                }

                if (memberCbacDetailBean.getDiagnosedForCOPD() != null && memberCbacDetailBean.getDiagnosedForCOPD()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.DIAGNOSED_FOR_COPD_TAG));
                }

                if (memberCbacDetailBean.getDiagnosedForAsthama() != null && memberCbacDetailBean.getDiagnosedForAsthama()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.DIAGNOSED_FOR_ASTHAMA_TAG));
                }

                if (memberCbacDetailBean.getDiagnosedForOralCancer() != null && memberCbacDetailBean.getDiagnosedForOralCancer()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.DIAGNOSED_FOR_ORAL_CANCER_TAG));
                }

                if (memberCbacDetailBean.getDiagnosedForBreastCancer() != null && memberCbacDetailBean.getDiagnosedForBreastCancer()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.DIAGNOSED_FOR_BREAST_CANCER_TAG));
                }

                if (memberCbacDetailBean.getDiagnosedForCervicalCancer() != null && memberCbacDetailBean.getDiagnosedForCervicalCancer()) {
                    items.add(UtilBean.getMyLabel(LabelConstants.DIAGNOSED_FOR_CERVICAL_CANCER_TAG));
                }

                if (!items.isEmpty()) {
                    bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(this, LabelConstants.PERSONAL_HISTORY));

                    for (String item : items) {
                        MaterialTextView textView = MyStaticComponents.generateAnswerView(this, item);
                        textView.setText(UtilBean.addBullet(item));
                        bodyLayoutContainer.addView(textView);
                    }
                }
            } else {
                bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.REFRESH_TO_GET_MEMBER_CBAC_DETAILS));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        setSubTitle(null);
        if (item.getItemId() == android.R.id.home) {
            switch (screen) {
                case ASHA_AREA_SELECTION_SCREEN:
                    navigateToHomeScreen(false);
                    break;
                case MEMBER_SELECTION_SCREEN:
                    startLocationSelectionActivity();
                    break;
                case CBAC_SUMMARY_SCREEN:
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    addSearchTextBoxForMember();
                    retrieveMemberListFromDB(null, null);
                    nextButton.setText(GlobalTypes.EVENT_NEXT);
                    break;
                default:
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult resultForQRScanner = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                selectedAshaArea = Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID));
                bodyLayoutContainer.removeAllViews();
                addSearchTextBoxForMember();
                retrieveMemberListFromDB(null, null);
            } else {
                navigateToHomeScreen(false);
            }
        } else if (resultForQRScanner != null) {
            if (resultForQRScanner.getContents() == null) {
                SewaUtil.generateToast(this, LabelConstants.FAILED_TO_SCAN_QR);
            } else {
                //show dialogue with resultForQRScanner
                String scanningData = resultForQRScanner.getContents();
                Log.i(TAG, "QR Scanner Data : " + scanningData);
                retrieveMemberListFromDB(null, scanningData);
            }
        } else {
            navigateToHomeScreen(false);
        }
    }

    @Override
    public void onLoadMoreItems() {
        onLoadMoreBackground();
    }

    @Background
    public void onLoadMoreBackground() {
        List<MemberDataBean> membersDataBeanList = ncdService.retrieveMembersForNcdRegisterAsha(ashaArea, searchString, LIMIT, offset, qrScanFilter);
        offset = offset + LIMIT;
        onLoadMoreUi(membersDataBeanList);
    }

    @UiThread
    public void onLoadMoreUi(List<MemberDataBean> membersDataBeanList) {
        if (!membersDataBeanList.isEmpty()) {
            List<ListItemDataBean> list = getMemberList(membersDataBeanList);
            memberDataBeans.addAll(membersDataBeanList);
            paginatedListView.onFinishLoadingWithItem(true, list);
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
        }
    }
}
