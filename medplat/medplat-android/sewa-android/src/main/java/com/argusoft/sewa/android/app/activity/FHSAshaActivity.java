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
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.LocationMasterServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.AshaReportedEventDataBean;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.LoggerBean;
import com.argusoft.sewa.android.app.model.StoreAnswerBean;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.FormMetaDataUtil;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.argusoft.sewa.android.app.util.WSConstants;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by prateek on 17/5/19.
 */
@EActivity
public class FHSAshaActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    private static final String ASHA_AREA_SELECTION_SCREEN = "ashaAreaSelectionScreen";
    private static final String FAMILY_SELECTION_SCREEN = "familySelectionScreen";
    private static final String FAMILY_OPTION_SCREEN = "familyOptionScreen";
    private static final String FAMILY_DETAILS_SCREEN = "familyDetailsScreen";
    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";
    private static final String MEMBER_OPTION_SCREEN = "memberOptionScreen";
    private static final String MEMBER_DETAILS_SCREEN = "memberDetailsScreen";
    private static final String REPORT_FAMILY_MIGRATION_SCREEN = "reportFamilyMigrationScreen";
    private static final String REPORT_FAMILY_SPLIT_SCREEN = "reportFamilySplitScreen";
    private static final String REPORT_MEMBER_MIGRATION_SCREEN = "reportMemberMigrationScreen";
    private static final String REPORT_MEMBER_DEATH_SCREEN = "reportMemberDeathScreen";
    private static final String FINAL_SCREEN = "finalScreen";
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
    private String lastScreen;
    private String searchString;
    private MaterialTextView noMemberTextView;
    private PagingListView paginatedListView;
    private long limit = 30;
    private long offset;
    private int selectedFamilyIndex = -1;
    private MaterialTextView titleTextView;
    private LinearLayout globalPanel;
    private LinearLayout footerView;
    private MaterialButtonToggleGroup yesNoToggleButton;

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
            List<Integer> checkedButton;
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
                    screen = FAMILY_SELECTION_SCREEN;
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

                case FAMILY_DETAILS_SCREEN:
                case MEMBER_DETAILS_SCREEN:
                    selectedFamilyIndex = -1;
                    nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                    footerView.setVisibility(View.VISIBLE);
                    addMemberSelectionScreen();
                    break;

                case MEMBER_SELECTION_SCREEN:
                    if (selectedFamilyIndex != -1) {
                        selectedMember = selectedFamily.getMembers().get(selectedFamilyIndex);
                        addMemberOptionScreen();
                    } else {
                        SewaUtil.generateToast(this, LabelConstants.PLEASE_SELECT_A_MEMBER);
                    }
                    break;

                case REPORT_FAMILY_MIGRATION_SCREEN:
                case REPORT_FAMILY_SPLIT_SCREEN:
                    checkedButton = yesNoToggleButton.getCheckedButtonIds();
                    if (!checkedButton.isEmpty()) {
                        bodyLayoutContainer.removeAllViews();
                        if (checkedButton.get(0) == 10002) {
                            addFinalScreen(screen);
                        } else if (checkedButton.get(0) == 10003) {
                            addFamilyOptionSelectionScreen();
                        }
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_AN_OPTION));
                    }
                    break;

                case REPORT_MEMBER_MIGRATION_SCREEN:
                case REPORT_MEMBER_DEATH_SCREEN:
                    checkedButton = yesNoToggleButton.getCheckedButtonIds();
                    if (!checkedButton.isEmpty()) {
                        bodyLayoutContainer.removeAllViews();
                        if (checkedButton.get(0) == 10002) {
                            addFinalScreen(screen);
                        } else if (checkedButton.get(0) == 10003) {
                            addMemberOptionScreen();
                        }
                    } else {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_AN_OPTION));
                    }
                    break;

                case FINAL_SCREEN:
                    storeAshaReportedEvent();
                    navigateToHomeScreen(false);
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
        footerView = globalPanel.findViewById(DynamicUtils.ID_FOOTER);
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
        bodyLayoutContainer.removeAllViews();
        screen = ASHA_AREA_SELECTION_SCREEN;

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
        if (!familyDataBeansList.isEmpty()) {
            List<ListItemDataBean> items = getFamilyList(familyDataBeansList);
            familyDataBeans.addAll(familyDataBeansList);
            paginatedListView.onFinishLoadingWithItem(true, items);
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
        }
    }

    @UiThread
    public void setFamilySelectionScreen(Boolean isSearch) {
        screen = FAMILY_SELECTION_SCREEN;
        bodyLayoutContainer.removeView(paginatedListView);
        bodyLayoutContainer.removeView(titleTextView);
        bodyLayoutContainer.removeView(noMemberTextView);

        if (!familyDataBeans.isEmpty()) {
            titleTextView = MyStaticComponents.getListTitleView(context, LabelConstants.SELECT_FAMILY);
            bodyLayoutContainer.addView(titleTextView);

            List<ListItemDataBean> familyList = getFamilyList(familyDataBeans);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedFamilyIndex = position;
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, familyList, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            bodyLayoutContainer.removeView(titleTextView);
            noMemberTextView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_FAMILIES_FOUND);
            bodyLayoutContainer.addView(noMemberTextView);
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

        List<String> options = new ArrayList<>();
        options.add(UtilBean.getMyLabel(LabelConstants.VIEW_FAMILY_DETAILS));
        options.add(UtilBean.getMyLabel(LabelConstants.REPORT_FAMILY_MIGRATION));
        int noOfMembersOfSelectedFamily = selectedFamily.getMembers().size();
        if (noOfMembersOfSelectedFamily > 1) {
            options.add(UtilBean.getMyLabel(LabelConstants.REPORT_FAMILY_SPLIT));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.WHAT_DO_YOU_WANT_TO_DO_WITH_THIS_FAMILY));

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            switch (position) {
                case 0:
                    setSubTitle(LabelConstants.FAMILY_DETAILS);
                    addSelectedFamilyDetails();
                    break;
                case 1:
                    addReportEventScreen(REPORT_FAMILY_MIGRATION_SCREEN);
                    break;
                case 2:
                    addReportEventScreen(REPORT_FAMILY_SPLIT_SCREEN);
                    break;
                default:
            }
            footerView.setVisibility(View.VISIBLE);
        };

        ListView buttonList = MyStaticComponents.getButtonList(context, options, onItemClickListener);
        bodyLayoutContainer.addView(buttonList);
        footerView.setVisibility(View.GONE);
    }


    @UiThread
    public void addSelectedFamilyDetails() {
        screen = FAMILY_DETAILS_SCREEN;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.FAMILY_ID));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, selectedFamily.getFamilyId()));
        MemberDataBean tmpMemberDataBean = headMembersMapWithFamilyIdAsKey.get(selectedFamily.getFamilyId());
        if (tmpMemberDataBean != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.HEAD_OF_THE_FAMILY));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getMemberFullName(tmpMemberDataBean)));
        }
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.ADDRESS_LINE_1));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getNotAvailableIfNull(selectedFamily.getAddress1())));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.ADDRESS_LINE_2));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getNotAvailableIfNull(selectedFamily.getAddress2())));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.ASHA_AREA));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getNotAvailableIfNull(locationMasterService.retrieveLocationHierarchyByLocationId(selectedAshaArea.longValue()))));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.ANGANWADI_NAME));
        String anganwadiName = null;
        if (selectedFamily.getAnganwadiId() != null && !selectedFamily.getAnganwadiId().isEmpty()) {
            LocationBean anganwadi = locationMasterService.getLocationBeanByActualId(selectedFamily.getAnganwadiId());
            if (anganwadi != null) {
                anganwadiName = anganwadi.getName();
            }
        }
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getNotAvailableIfNull(anganwadiName)));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.RELIGION));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(selectedFamily.getReligion()))));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.CASTE));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(selectedFamily.getCaste()))));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.SEASONAL_MIGRANT));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getNotAvailableIfNull(UtilBean.returnYesNoNotAvailableFromBoolean(selectedFamily.getSeasonalMigrantFlag()))));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.VULNERABLE_FAMILY));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getNotAvailableIfNull(UtilBean.returnYesNoNotAvailableFromBoolean(selectedFamily.getVulnerableFlag()))));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.BPL_FAMILY));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getNotAvailableIfNull(UtilBean.returnYesNoNotAvailableFromBoolean(selectedFamily.getBplFlag()))));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.TOILET_AVAILABLE));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getNotAvailableIfNull(UtilBean.returnYesNoNotAvailableFromBoolean(selectedFamily.getToiletAvailableFlag()))));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.MAA_CARD_NUMBER));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getNotAvailableIfNull(selectedFamily.getMaaVatsalyaNumber())));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.RSBY_CARD_NUMBER));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getNotAvailableIfNull(selectedFamily.getRsbyCardNumber())));
    }

    @UiThread
    public void addMemberSelectionScreen() {
        screen = MEMBER_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        List<ListItemDataBean> list = new ArrayList<>();
        bodyLayoutContainer.addView(MyStaticComponents.getListTitleView(context, LabelConstants.SELECT_A_MEMBER_FROM_LIST));

        for (MemberDataBean memberDataBean : selectedFamily.getMembers()) {
            list.add(new ListItemDataBean(memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean)));
        }

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedFamilyIndex = position;
        PagingListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_item, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
    }

    private void addMemberOptionScreen() {
        bodyLayoutContainer.removeAllViews();
        screen = MEMBER_OPTION_SCREEN;
        setSubTitle(UtilBean.getMemberFullName(selectedMember));

        LinearLayout familyDetailsLinearLayout = MyStaticComponents.getDetailsLayout(context, -1, LinearLayout.VERTICAL, bodyLayoutContainer.getPaddingTop());
        familyDetailsLinearLayout.addView(MyStaticComponents.generateLabelView(this, LabelConstants.MEMBER_NAME));
        familyDetailsLinearLayout.addView(MyStaticComponents.generateBoldAnswerView(this, UtilBean.getMemberFullName(selectedMember)));
        familyDetailsLinearLayout.addView(MyStaticComponents.generateLabelView(this, LabelConstants.HEALTH_ID));
        familyDetailsLinearLayout.addView(MyStaticComponents.generateAnswerView(this, selectedMember.getUniqueHealthId()));
        bodyLayoutContainer.addView(familyDetailsLinearLayout);
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.SELECT_OPTION));

        List<String> options = new ArrayList<>();
        options.add(UtilBean.getMyLabel(LabelConstants.VIEW_MEMBER_DETAILS));
        options.add(UtilBean.getMyLabel(LabelConstants.REPORT_MEMBER_MIGRATION));
        options.add(UtilBean.getMyLabel(LabelConstants.REPORT_MEMBER_DEATH));

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            setSubTitle(UtilBean.getMemberFullName(selectedMember));
            switch (position) {
                case 0:
                    addMemberDetailsScreen();
                    break;
                case 1:
                    addReportEventScreen(REPORT_MEMBER_MIGRATION_SCREEN);
                    break;
                case 2:
                    addReportEventScreen(REPORT_MEMBER_DEATH_SCREEN);
                    break;
                default:
            }
            footerView.setVisibility(View.VISIBLE);
        };
        ListView buttonList = MyStaticComponents.getButtonList(context, options, onItemClickListener);
        bodyLayoutContainer.addView(buttonList);
        footerView.setVisibility(View.GONE);
    }

    private void addMemberDetailsScreen() {
        screen = MEMBER_DETAILS_SCREEN;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context, LabelConstants.MEMBER_DETAILS));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.MEMBER_NAME));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getMemberFullName(selectedMember)));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.HEALTH_ID));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, selectedMember.getUniqueHealthId()));

        String gender = GlobalTypes.NOT_AVAILABLE;
        if (selectedMember.getGender() != null && selectedMember.getGender().equalsIgnoreCase(GlobalTypes.FEMALE)) {
            gender = LabelConstants.FEMALE;
        } else if (selectedMember.getGender() != null && selectedMember.getGender().equalsIgnoreCase(GlobalTypes.MALE)) {
            gender = LabelConstants.MALE;
        }
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.GENDER));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, gender));

        String age = GlobalTypes.NOT_AVAILABLE;
        if (selectedMember.getDob() != null) {
            age = UtilBean.getAgeDisplayOnGivenDate(new Date(selectedMember.getDob()), new Date());
        }
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.AGE));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, age));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.MARITAL_STATUS)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getMyLabel(UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(selectedMember.getMaritalStatus())))));

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
                            UtilBean.getMyLabel(UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(string)))));
                }
            } else {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                        UtilBean.getMyLabel(UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(selectedMember.getCongenitalAnomalyIds())))));
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
                            UtilBean.getMyLabel(UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(string)))));
                }
            } else {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                        UtilBean.getMyLabel(UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(selectedMember.getChronicDiseaseIds())))));
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, LabelConstants.NOT_AVAILABLE));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.CURRENT_DISEASES_PRESENT));
        if (selectedMember.getCurrentDiseaseIds() != null && !selectedMember.getCurrentDiseaseIds().isEmpty()) {
            if (selectedMember.getCurrentDiseaseIds().contains(",")) {
                String[] split = selectedMember.getCurrentDiseaseIds().split(",");
                for (String string : split) {
                    bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                            UtilBean.getMyLabel(UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(string)))));
                }
            } else {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                        UtilBean.getMyLabel(UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(selectedMember.getCurrentDiseaseIds())))));
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, LabelConstants.NOT_AVAILABLE));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.EYE_DISEASES_PRESENT));
        if (selectedMember.getEyeIssueIds() != null && !selectedMember.getEyeIssueIds().isEmpty()) {
            if (selectedMember.getEyeIssueIds().contains(",")) {
                String[] split = selectedMember.getEyeIssueIds().split(",");
                for (String string : split) {
                    bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                            UtilBean.getMyLabel(UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(string)))));
                }
            } else {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                        UtilBean.getMyLabel(UtilBean.getNotAvailableIfNull(fhsService.getValueOfListValuesById(selectedMember.getEyeIssueIds())))));
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, LabelConstants.NOT_AVAILABLE));
        }
        nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
    }

    private void addReportEventScreen(String selectedScreen) {
        screen = selectedScreen;
        bodyLayoutContainer.removeAllViews();

        switch (selectedScreen) {
            case REPORT_FAMILY_MIGRATION_SCREEN:
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.WANT_REPORT_FAMILY_MIGRATION));
                break;

            case REPORT_FAMILY_SPLIT_SCREEN:
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.WANT_REPORT_FAMILY_SPLIT));
                break;

            case REPORT_MEMBER_MIGRATION_SCREEN:
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.WANT_REPORT_MEMBER_MIGRATION));
                break;

            case REPORT_MEMBER_DEATH_SCREEN:
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.WANT_REPORT_MEMBER_DEATH));
                break;
            default:
        }

        yesNoToggleButton = MyStaticComponents.getYesNoToggleButton(context);
        bodyLayoutContainer.addView(yesNoToggleButton);
    }

    private void addFinalScreen(String fromScreen) {
        lastScreen = fromScreen;
        screen = FINAL_SCREEN;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context, LabelConstants.SUCCESSFULLY_REPORTED_EVENT));
    }

    private void storeAshaReportedEvent() {
        //Preparing Checksum
        StringBuilder checkSum = new StringBuilder(SewaTransformer.loginBean.getUsername());
        checkSum.append(Calendar.getInstance().getTimeInMillis());

        StoreAnswerBean storeAnswerBean = new StoreAnswerBean();
        storeAnswerBean.setToken(SewaTransformer.loginBean.getUserToken());
        storeAnswerBean.setUserId(SewaTransformer.loginBean.getUserID());
        storeAnswerBean.setChecksum(checkSum.toString());
        storeAnswerBean.setDateOfMobile(Calendar.getInstance().getTimeInMillis());
        storeAnswerBean.setFormFilledUpTime(0L);
        storeAnswerBean.setMorbidityAnswer("-1");
        storeAnswerBean.setNotificationId(-1L);
        storeAnswerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO);
        storeAnswerBean.setRelatedInstance("-1");

        LoggerBean loggerBean = new LoggerBean();
        loggerBean.setCheckSum(checkSum.toString());
        loggerBean.setDate(Calendar.getInstance().getTimeInMillis());
        loggerBean.setStatus(GlobalTypes.STATUS_PENDING);
        loggerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO.trim());
        loggerBean.setNoOfAttempt(0);

        AshaReportedEventDataBean ashaReportedEventDataBean = new AshaReportedEventDataBean();
        ashaReportedEventDataBean.setReportedOn(new Date().getTime());
        ashaReportedEventDataBean.setLocationId(selectedAshaArea.longValue());
        ashaReportedEventDataBean.setFamilyId(Long.valueOf(selectedFamily.getId()));

        MemberDataBean tmpMemberDataBean = headMembersMapWithFamilyIdAsKey.get(selectedFamily.getFamilyId());
        switch (lastScreen) {
            case REPORT_FAMILY_MIGRATION_SCREEN:
                ashaReportedEventDataBean.setEventType(FormConstants.ASHA_REPORT_FAMILY_MIGRATION);
                storeAnswerBean.setAnswerEntity(FormConstants.ASHA_REPORTED_EVENT);
                if (tmpMemberDataBean != null) {
                    loggerBean.setBeneficiaryName(selectedFamily.getFamilyId() + " - "
                            + UtilBean.getMemberFullName(tmpMemberDataBean));
                } else {
                    loggerBean.setBeneficiaryName(selectedFamily.getFamilyId());
                }
                loggerBean.setTaskName(UtilBean.getFullFormOfEntity().get(FormConstants.ASHA_REPORT_FAMILY_MIGRATION));
                loggerBean.setFormType(FormConstants.ASHA_REPORT_FAMILY_MIGRATION);
                fhsService.markFamilyAsMigrated(selectedFamily.getFamilyId());
                break;
            case REPORT_FAMILY_SPLIT_SCREEN:
                ashaReportedEventDataBean.setEventType(FormConstants.ASHA_REPORT_FAMILY_SPLIT);
                storeAnswerBean.setAnswerEntity(FormConstants.ASHA_REPORTED_EVENT);
                if (tmpMemberDataBean != null) {
                    loggerBean.setBeneficiaryName(selectedFamily.getFamilyId() + " - "
                            + UtilBean.getMemberFullName(tmpMemberDataBean));
                } else {
                    loggerBean.setBeneficiaryName(selectedFamily.getFamilyId());
                }
                loggerBean.setTaskName(UtilBean.getFullFormOfEntity().get(FormConstants.ASHA_REPORT_FAMILY_SPLIT));
                loggerBean.setFormType(FormConstants.ASHA_REPORT_FAMILY_SPLIT);
                break;
            case REPORT_MEMBER_MIGRATION_SCREEN:
                ashaReportedEventDataBean.setMemberId(Long.valueOf(selectedMember.getId()));
                ashaReportedEventDataBean.setEventType(FormConstants.ASHA_REPORT_MEMBER_MIGRATION);
                storeAnswerBean.setAnswerEntity(FormConstants.ASHA_REPORTED_EVENT);
                loggerBean.setBeneficiaryName(selectedMember.getUniqueHealthId() + " - " + UtilBean.getMemberFullName(selectedMember));
                loggerBean.setTaskName(UtilBean.getFullFormOfEntity().get(FormConstants.ASHA_REPORT_MEMBER_MIGRATION));
                loggerBean.setFormType(FormConstants.ASHA_REPORT_MEMBER_MIGRATION);
                fhsService.markMemberAsMigrated(Long.valueOf(selectedMember.getId()));
                break;
            case REPORT_MEMBER_DEATH_SCREEN:
                ashaReportedEventDataBean.setMemberId(Long.valueOf(selectedMember.getId()));
                ashaReportedEventDataBean.setEventType(FormConstants.ASHA_REPORT_MEMBER_DEATH);
                storeAnswerBean.setAnswerEntity(FormConstants.ASHA_REPORTED_EVENT);
                loggerBean.setBeneficiaryName(selectedMember.getUniqueHealthId() + " - " + UtilBean.getMemberFullName(selectedMember));
                loggerBean.setTaskName(UtilBean.getFullFormOfEntity().get(FormConstants.ASHA_REPORT_MEMBER_DEATH));
                loggerBean.setFormType(FormConstants.ASHA_REPORT_MEMBER_DEATH);
                fhsService.markMemberAsDead(Long.valueOf(selectedMember.getId()));
                break;
            default:
        }

        storeAnswerBean.setAnswer(new Gson().toJson(ashaReportedEventDataBean));
        sewaService.createStoreAnswerBean(storeAnswerBean);
        sewaService.createLoggerBean(loggerBean);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                selectedAshaArea = Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID));
                bodyLayoutContainer.removeAllViews();
                selectedFamilyIndex = -1;
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
        setSubTitle(null);
        if(screen == null || screen.isEmpty()) {
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
                    footerView.setVisibility(View.VISIBLE);
                    selectedFamilyIndex = -1;
                    bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                    addSearchTextBox();
                    retrieveFamilyListFromDB(null);
                    break;

                case FAMILY_DETAILS_SCREEN:
                case REPORT_FAMILY_MIGRATION_SCREEN:
                case REPORT_FAMILY_SPLIT_SCREEN:
                    addFamilyOptionSelectionScreen();
                    break;

                case MEMBER_SELECTION_SCREEN:
                    setSubTitle(LabelConstants.FAMILY_DETAILS);
                    addSelectedFamilyDetails();
                    break;

                case MEMBER_OPTION_SCREEN:
                    selectedFamilyIndex = -1;
                    footerView.setVisibility(View.VISIBLE);
                    addMemberSelectionScreen();
                    break;

                case MEMBER_DETAILS_SCREEN:
                case REPORT_MEMBER_MIGRATION_SCREEN:
                case REPORT_MEMBER_DEATH_SCREEN:
                    selectedFamilyIndex = -1;
                    addMemberOptionScreen();
                    break;

                case FINAL_SCREEN:
                    addReportEventScreen(lastScreen);
                    break;
                default:
            }
        }
        return true;
    }
}
