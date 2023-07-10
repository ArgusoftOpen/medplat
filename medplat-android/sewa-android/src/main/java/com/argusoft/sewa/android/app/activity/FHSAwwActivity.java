package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.argusoft.sewa.android.app.core.impl.LocationMasterServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.FormMetaDataUtil;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by prateek on 17/5/19.
 */
@EActivity
public class FHSAwwActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    private static final String ASHA_AREA_SELECTION_SCREEN = "ashaAreaSelectionScreen";
    private static final String FAMILY_SELECTION_SCREEN = "familySelectionScreen";
    private static final String FAMILY_OPTION_SCREEN = "familyOptionScreen";
    private static final String FAMILY_DETAILS_SCREEN = "familyDetailsScreen";
    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";
    private static final String MEMBER_DETAILS_SCREEN = "memberDetailsScreen";
    private static final long DELAY = 500;
    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    FormMetaDataUtil formMetaDataUtil;
    @Bean
    LocationMasterServiceImpl locationMasterService;
    private Map<String, MemberDataBean> headMembersMapWithFamilyIdAsKey;
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private Spinner areaSpinner;
    private TextInputLayout searchFamilyEditText;
    private MyAlertDialog myAlertDialog;
    private Timer timer = new Timer();
    private List<LocationBean> ashaAreaList = new ArrayList<>();
    private Integer selectedAshaArea;
    private String screen;
    private List<FamilyDataBean> familyDataBeans = new ArrayList<>();
    private FamilyDataBean selectedFamily;
    private MemberDataBean selectedMember;
    private LinearLayout globalPanel;
    private long offset;
    private long limit = 30;
    private String searchString;
    private PagingListView paginatedListView;
    private int selectedFamilyIndex = -1;
    private MaterialTextView familyTextView;
    private int selectedMemberIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //To change body of generated methods, choose Tools | Templates.
        context = this;
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
        setTitle(UtilBean.getTitleText(LabelConstants.FAMILY_HEALTH_SURVEY_TITLE));
    }

    @Override
    public void onClick(View v) {
        setSubTitle(null);
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screen) {
                case ASHA_AREA_SELECTION_SCREEN:
                    showProcessDialog();
                    String selectedVillage = areaSpinner.getSelectedItem().toString();
                    for (LocationBean locationBean : ashaAreaList) {
                        if (selectedVillage.equals(locationBean.getName())) {
                            selectedAshaArea = locationBean.getActualID();
                            break;
                        }
                    }
                    bodyLayoutContainer.removeAllViews();
                    bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                    addSearchTextBox();
                    retrieveFamilyListFromDB(null);
                    break;
                case FAMILY_SELECTION_SCREEN:
                    if (familyDataBeans == null || familyDataBeans.isEmpty()) {
                        navigateToHomeScreen(false);
                        finish();
                    } else {
                        if (selectedFamilyIndex != -1) {
                            selectedFamily = familyDataBeans.get(selectedFamilyIndex);
                            addFamilyOptionSelectionScreen();
                        } else {
                            SewaUtil.generateToast(this, LabelConstants.PLEASE_SELECT_A_FAMILY);
                        }
                    }
                    break;

                case FAMILY_OPTION_SCREEN:
                    addSelectedFamilyDetails();
                    break;

                case FAMILY_DETAILS_SCREEN:
                    addMemberSelectionScreen();
                    break;

                case MEMBER_DETAILS_SCREEN:
                    navigateToHomeScreen(false);
                    break;

                case MEMBER_SELECTION_SCREEN:
                    if (selectedMemberIndex != -1) {
                        selectedMember = selectedFamily.getMembers().get(selectedMemberIndex);
                        addMemberDetailsScreen();
                    } else {
                        SewaUtil.generateToast(this, LabelConstants.PLEASE_SELECT_A_MEMBER);
                    }
                    break;

                default:
            }
        }
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
                LabelConstants.ON_FAMILY_HEALTH_SURVEY_CLOSE_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        myAlertDialog.show();
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
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.FAMILY_HEALTH_SURVEY_TITLE);
        startActivityForResult(myIntent, ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE);
    }

    @UiThread
    public void addAshaAreaSelectionSpinner() {
        screen = ASHA_AREA_SELECTION_SCREEN;
        setSubTitle(null);
        bodyLayoutContainer.removeAllViews();

        if (areaSpinner == null) {
            String[] arrayOfOptions = new String[ashaAreaList.size()];
            int i = 0;
            for (LocationBean locationBean : ashaAreaList) {
                arrayOfOptions[i] = locationBean.getName();
                i++;
            }
            areaSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 2);
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.SELECT_ASHA_AREA));
        bodyLayoutContainer.addView(areaSpinner);
        hideProcessDialog();
    }

    @UiThread
    public void addSearchTextBox() {
        searchFamilyEditText = MyStaticComponents.getEditText(this, LabelConstants.SEARCH_FAMILY, 1, 20, 1);
        if (searchFamilyEditText.getEditText() != null) {
            searchFamilyEditText.getEditText().addTextChangedListener(new TextWatcher() {
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
                                            retrieveFamilyListFromDB(s.toString().trim());
                                            searchFamilyEditText.setFocusable(true);
                                        });
                                    } else if (s == null || s.toString().trim().length() == 0) {
                                        runOnUiThread(() -> {
                                            showProcessDialog();
                                            retrieveFamilyListFromDB(null);
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
        bodyLayoutContainer.addView(searchFamilyEditText);
        bodyLayoutContainer.addView(MyStaticComponents.getOrTextView(context));
    }

    @Background
    public void retrieveFamilyListFromDB(String search) {
        offset = 0;
        selectedFamilyIndex = -1;
        searchString = search;
        if (search == null) {
            familyDataBeans = fhsService.retrieveFamilyDataBeansForFHSByArea(selectedAshaArea.longValue(), limit, offset);
            offset = offset + limit;
        } else {
            familyDataBeans = fhsService.searchFamilyDataBeansForFHSByArea(selectedAshaArea.longValue(), search);
        }
        setFamilySelectionScreen(search != null);
    }

    @UiThread
    public void setFamilySelectionScreen(Boolean isSearch) {
        bodyLayoutContainer.removeView(paginatedListView);
        bodyLayoutContainer.removeView(familyTextView);
        screen = FAMILY_SELECTION_SCREEN;

        if (!familyDataBeans.isEmpty()) {
            familyTextView = MyStaticComponents.getListTitleView(context, LabelConstants.SELECT_FAMILY);
            bodyLayoutContainer.addView(familyTextView);
            List<ListItemDataBean> list = getFamilyList(familyDataBeans);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedFamilyIndex = position;
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            familyTextView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_FAMILIES_FOUND);
            bodyLayoutContainer.addView(familyTextView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }

        hideProcessDialog();
    }

    private List<ListItemDataBean> getFamilyList(List<FamilyDataBean> familyDataBeans) {
        List<String> familyIds = new ArrayList<>();
        List<ListItemDataBean> list = new ArrayList<>();
        String rbText;

        for (FamilyDataBean familyDataBean : familyDataBeans) {
            familyIds.add(familyDataBean.getFamilyId());
        }
        headMembersMapWithFamilyIdAsKey = fhsService.retrieveHeadMemberDataBeansByFamilyId(familyIds);

        for (FamilyDataBean familyDataBean : familyDataBeans) {
            MemberDataBean headMember = headMembersMapWithFamilyIdAsKey.get(familyDataBean.getFamilyId());
            if (headMember != null) {
                familyDataBean.setHeadMemberName(headMember.getFirstName() + " " + headMember.getMiddleName() + " " + headMember.getGrandfatherName() + " " + headMember.getLastName());
                familyDataBean.setHeadMemberName(familyDataBean.getHeadMemberName().replace(LabelConstants.NULL, ""));
                rbText = headMember.getFirstName() + " " + headMember.getMiddleName() + " " + headMember.getGrandfatherName() + " " + headMember.getLastName();
                rbText = rbText.replace(" " + LabelConstants.NULL, "");
            } else {
                rbText = UtilBean.getMyLabel(LabelConstants.HEAD_NAME_NOT_AVAILABLE);
            }
            list.add(new ListItemDataBean(null, familyDataBean.getFamilyId(), null, null, rbText));
        }
        return list;
    }

    @UiThread
    public void addFamilyOptionSelectionScreen() {
        bodyLayoutContainer.removeAllViews();
        screen = FAMILY_OPTION_SCREEN;
        setSubTitle(LabelConstants.FAMILY_DETAILS);

        selectedFamily = fhsService.retrieveFamilyDataBeanByFamilyId(selectedFamily.getFamilyId());
        selectedFamily.setMembers(fhsService.retrieveMemberDataBeansByFamily(selectedFamily.getFamilyId()));

        LinearLayout familyDetailsLinearLayout = MyStaticComponents.getDetailsLayout(context, -1, LinearLayout.VERTICAL, bodyLayoutContainer.getPaddingTop());
        familyDetailsLinearLayout.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.FAMILY_ID));
        familyDetailsLinearLayout.addView(MyStaticComponents.generateBoldAnswerView(this, selectedFamily.getFamilyId()));
        familyDetailsLinearLayout.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.MEMBERS_INFO));
        familyDetailsLinearLayout.addView(UtilBean.getMembersListForDisplay(this, selectedFamily));
        bodyLayoutContainer.addView(familyDetailsLinearLayout);
    }


    @UiThread
    public void addSelectedFamilyDetails() {
        screen = FAMILY_DETAILS_SCREEN;
        setSubTitle(LabelConstants.FAMILY_DETAILS);
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context, LabelConstants.FAMILY_DETAILS));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.FAMILY_ID));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, selectedFamily.getFamilyId()));
        MemberDataBean tmpMemberHeadDataBean = headMembersMapWithFamilyIdAsKey.get(selectedFamily.getFamilyId());
        if (tmpMemberHeadDataBean != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.HEAD_OF_THE_FAMILY));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                    UtilBean.getMemberFullName(tmpMemberHeadDataBean)));
        }
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.ADDRESS_LINE_1));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getNotAvailableIfNull(selectedFamily.getAddress1())));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.ADDRESS_LINE_2));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getNotAvailableIfNull(selectedFamily.getAddress2())));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.ASHA_AREA));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getNotAvailableIfNull(locationMasterService.retrieveLocationHierarchyByLocationId(selectedAshaArea.longValue()))));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.ANGANWADI_NAME));
        String anganwadiName = null;
        if (selectedFamily.getAnganwadiId() != null && !selectedFamily.getAnganwadiId().isEmpty()) {
            LocationBean anganwadi = locationMasterService.getLocationBeanByActualId(selectedFamily.getAnganwadiId());
            if (anganwadi != null) {
                anganwadiName = anganwadi.getName();
            }
        }
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getNotAvailableIfNull(anganwadiName)));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.REGION));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(selectedFamily.getReligion()))));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.CASTE));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(selectedFamily.getCaste()))));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.SEASONAL_MIGRANT));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getNotAvailableIfNull(UtilBean.returnYesNoNotAvailableFromBoolean(selectedFamily.getSeasonalMigrantFlag()))));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.VULNERABLE_FAMILY));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getNotAvailableIfNull(UtilBean.returnYesNoNotAvailableFromBoolean(selectedFamily.getVulnerableFlag()))));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.BPL_FAMILY));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getNotAvailableIfNull(UtilBean.returnYesNoNotAvailableFromBoolean(selectedFamily.getBplFlag()))));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.TOILET_AVAILABLE));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getNotAvailableIfNull(UtilBean.returnYesNoNotAvailableFromBoolean(selectedFamily.getToiletAvailableFlag()))));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.MAA_CARD_NUMBER));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getNotAvailableIfNull(selectedFamily.getMaaVatsalyaNumber())));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.RSBY_CARD_NUMBER));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getNotAvailableIfNull(selectedFamily.getRsbyCardNumber())));
    }

    @UiThread
    public void addMemberSelectionScreen() {
        screen = MEMBER_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        List<ListItemDataBean> list = new ArrayList<>();

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.SELECT_MEMBER_OF_THE_FAMILY));

        for (MemberDataBean memberDataBean : selectedFamily.getMembers()) {
            list.add(new ListItemDataBean(memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean)));
        }

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedMemberIndex = position;
        PagingListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_item, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
    }

    private void addMemberDetailsScreen() {
        screen = MEMBER_DETAILS_SCREEN;
        setSubTitle(UtilBean.getMemberFullName(selectedMember));
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context, LabelConstants.MEMBER_DETAILS));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.MEMBER_NAME));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getMemberFullName(selectedMember)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.HEALTH_ID));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, selectedMember.getUniqueHealthId()));

        String gender = LabelConstants.NOT_AVAILABLE;
        if (selectedMember.getGender() != null && selectedMember.getGender().equalsIgnoreCase(GlobalTypes.FEMALE)) {
            gender = LabelConstants.FEMALE;
        } else if (selectedMember.getGender() != null && selectedMember.getGender().equalsIgnoreCase(GlobalTypes.MALE)) {
            gender = LabelConstants.MALE;
        }
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.GENDER));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, gender));

        String age = LabelConstants.NOT_AVAILABLE;
        if (selectedMember.getDob() != null) {
            age = UtilBean.getAgeDisplayOnGivenDate(new Date(selectedMember.getDob()), new Date());
        }
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.AGE));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, age));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.MARITAL_STATUS));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(selectedMember.getMaritalStatus()))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.MOBILE_NUMBER));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getNotAvailableIfNull(selectedMember.getMobileNumber())));

        boolean bankDetails = selectedMember.getAccountNumber() != null && !selectedMember.getAccountNumber().isEmpty()
                && selectedMember.getIfsc() != null && !selectedMember.getIfsc().isEmpty();
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.BANK_DETAILS));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.returnYesNoNotAvailableFromBoolean(bankDetails)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.EDUCATION_STATUS));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(selectedMember.getEducationStatus()))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.CONGENITAL_ANOMALIES_PRESENT));
        if (selectedMember.getCongenitalAnomalyIds() != null && !selectedMember.getCongenitalAnomalyIds().isEmpty()) {
            if (selectedMember.getCongenitalAnomalyIds().contains(",")) {
                String[] split = selectedMember.getCongenitalAnomalyIds().split(",");
                for (String string : split) {
                    bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                            UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(string))));
                }
            } else {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                        UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(selectedMember.getCongenitalAnomalyIds()))));
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, LabelConstants.NOT_AVAILABLE));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.CHRONIC_DISEASES_PRESENT));
        if (selectedMember.getChronicDiseaseIds() != null && !selectedMember.getChronicDiseaseIds().isEmpty()) {
            if (selectedMember.getChronicDiseaseIds().contains(",")) {
                String[] split = selectedMember.getChronicDiseaseIds().split(",");
                for (String string : split) {
                    bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                            UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(string))));
                }
            } else {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                        UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(selectedMember.getChronicDiseaseIds()))));
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, LabelConstants.NOT_AVAILABLE));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.CURRENT_DISEASES_PRESENT)));
        if (selectedMember.getCurrentDiseaseIds() != null && !selectedMember.getCurrentDiseaseIds().isEmpty()) {
            if (selectedMember.getCurrentDiseaseIds().contains(",")) {
                String[] split = selectedMember.getCurrentDiseaseIds().split(",");
                for (String string : split) {
                    bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                            UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(string))));
                }
            } else {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                        UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(selectedMember.getCurrentDiseaseIds()))));
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, LabelConstants.NOT_AVAILABLE));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.EYE_DISEASES_PRESENT)));
        if (selectedMember.getEyeIssueIds() != null && !selectedMember.getEyeIssueIds().isEmpty()) {
            if (selectedMember.getEyeIssueIds().contains(",")) {
                String[] split = selectedMember.getEyeIssueIds().split(",");
                for (String string : split) {
                    bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                            UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(string))));
                }
            } else {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                        UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(selectedMember.getEyeIssueIds()))));
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, LabelConstants.NOT_AVAILABLE));
        }
        nextButton.setText(GlobalTypes.MAIN_MENU);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                selectedAshaArea = Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID));
                bodyLayoutContainer.removeAllViews();
                bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                addSearchTextBox();
                retrieveFamilyListFromDB(null);
            } else {
                navigateToHomeScreen(false);
            }
        } else {
            navigateToHomeScreen(false);
        }
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
                case ASHA_AREA_SELECTION_SCREEN:
                    navigateToHomeScreen(false);
                    break;

                case FAMILY_SELECTION_SCREEN:
                    startLocationSelectionActivity();
                    break;

                case FAMILY_OPTION_SCREEN:
                    bodyLayoutContainer.removeAllViews();
                    selectedFamilyIndex = -1;
                    bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                    addSearchTextBox();
                    retrieveFamilyListFromDB(null);
                    screen = FAMILY_SELECTION_SCREEN;
                    break;

                case FAMILY_DETAILS_SCREEN:
                    setSubTitle(null);
                    addFamilyOptionSelectionScreen();
                    break;

                case MEMBER_SELECTION_SCREEN:
                    addSelectedFamilyDetails();
                    break;

                case MEMBER_DETAILS_SCREEN:
                    nextButton.setText(GlobalTypes.EVENT_NEXT);
                    setSubTitle(null);
                    selectedMemberIndex = -1;
                    addMemberSelectionScreen();
                    break;

                default:
            }
            return true;
        }
        return false;
    }

    @Override
    public void onLoadMoreItems() {
        if (searchString == null) {
            onLoadMoreBackground();
        }
    }

    @Background
    public void onLoadMoreBackground() {
        List<FamilyDataBean> famDataBeans = fhsService.retrieveFamilyDataBeansForFHSByArea(selectedAshaArea.longValue(), limit, offset);
        offset = offset + limit;
        onLoadMoreUi(famDataBeans);
    }

    @UiThread
    public void onLoadMoreUi(List<FamilyDataBean> familyDataBeansList) {
        if (familyDataBeansList != null && !familyDataBeansList.isEmpty()) {
            familyDataBeans.addAll(familyDataBeansList);
            List<ListItemDataBean> stringList = getFamilyList(familyDataBeansList);
            paginatedListView.onFinishLoadingWithItem(true, stringList);
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
        }
    }

}
