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
import com.argusoft.sewa.android.app.constants.FhsConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.core.impl.AbhaCreationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.LocationMasterServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.OfflineHealthIdBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.FamilyBean;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.model.StoreAnswerBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@EActivity
public class AbhaNumberActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    AbhaCreationServiceImpl abhaCreationService;
    @Bean
    LocationMasterServiceImpl locationService;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<MemberBean, Integer> memberBeansDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<FamilyBean, Integer> familyBeansDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<StoreAnswerBean, Integer> storeAnswerBeanDao;

    private static final String FAMILY_SELECTION_SCREEN = "familySelectionScreen";
    private static final String VILLAGE_SELECTION_SCREEN = "villageSelectionScreen";
    private static final String OPTION_SELECTION_SCREEN = "optionSelectionScreen";
    private static final long DELAY = 500;
    private static final int LIMIT = 30;

    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private Spinner spinner;
    private Timer timer = new Timer();
    private Intent myIntent;
    private String screen;
    private String locationId;
    private List<LocationBean> locationBeans = new ArrayList<>();
    private List<FamilyDataBean> familyDataBeans = null;
    private FamilyDataBean selectedFamily;
    private MemberDataBean selectedMember;
    private MaterialTextView pagingHeaderView;
    private MaterialTextView noFamilyAvailableView;
    private int selectedFamilyIndex = -1;
    private int selectedMemberIndex = -1;
    private PagingListView paginatedListView;
    private int offset = 0;
    private LinearLayout footerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        setTitle(UtilBean.getTitleText(LabelConstants.HEALTH_ID_MANAGEMENT));
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        footerLayout = globalPanel.findViewById(DynamicUtils.ID_FOOTER);
        setBodyDetail();
    }

    @Background
    public void setBodyDetail() {
        screen = VILLAGE_SELECTION_SCREEN;
        startLocationSelectionActivity();
    }

    private void startLocationSelectionActivity() {
        myIntent = new Intent(context, LocationSelectionActivity_.class);
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.HEALTH_ID_MANAGEMENT);
        startActivityForResult(myIntent, ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screen) {
                case VILLAGE_SELECTION_SCREEN:
                    String selectedVillage = spinner.getSelectedItem().toString();
                    for (LocationBean locationBean : locationBeans) {
                        if (selectedVillage.equals(locationBean.getName())) {
                            locationId = String.valueOf(locationBean.getActualID());
                            break;
                        }
                    }
                    bodyLayoutContainer.removeAllViews();
                    addSearchTextBox();
                    addFamilyList();
                    screen = FAMILY_SELECTION_SCREEN;
                    break;
                case FAMILY_SELECTION_SCREEN:
                    if (selectedFamilyIndex != -1) {
                        bodyLayoutContainer.removeAllViews();
                        FamilyDataBean familyDataBean = familyDataBeans.get(selectedFamilyIndex);
                        selectedFamily = fhsService.retrieveFamilyDataBeanByFamilyId(familyDataBean.getFamilyId());
                        addSelectedFamilyDetails(selectedFamily);
                        screen = OPTION_SELECTION_SCREEN;
                    } else {
                        SewaUtil.generateToast(this, LabelConstants.PLEASE_SELECT_A_FAMILY);
                    }
                    break;
                case OPTION_SELECTION_SCREEN:
                    if (selectedMemberIndex != -1) {
                        try {
                            List<StoreAnswerBean> storeAnswerBeans = storeAnswerBeanDao.queryForEq("memberId", selectedMember.getId());
                            if (storeAnswerBeans.size() > 0) {
                                SewaUtil.generateToast(this, LabelConstants.ABHA_OFFLINE_PROCESS_INITIATED);
                                return;
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (selectedMember == null) return;
                        if (selectedMember.getHealthIdNumber() != null && !selectedMember.getHealthIdNumber().isEmpty()) {
                            SewaUtil.generateToast(this, LabelConstants.ABHA_NUMBER_IS_ALREADY_LINKED_MSG);
                            return;
                        }

                        Intent intent;
                        int requestCode = 0;
                        intent = new Intent(this, NDHMHealthIdManagementActivity_.class);
                        intent.putExtra("featureName", "ABHA_NUMBER");
                        requestCode = ActivityConstants.HEALTH_ID_MANAGEMENT_REQUEST_CODE;

                        String textDate = "";
                        if (selectedMember.getDob() != null) {
                            textDate = new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault()).format(new Date(selectedMember.getDob()));
                        }
                        String gender = selectedMember.getGender();
                        String mobileNumber = selectedMember.getMobileNumber();
                        if (mobileNumber != null && mobileNumber.contains("/")) {
                            mobileNumber = mobileNumber.substring(mobileNumber.indexOf("/") + 1);
                        }
                        intent.putExtra(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY, String.format("%s %s", selectedMember.getFirstName(), selectedMember.getLastName()));
                        intent.putExtra(RelatedPropertyNameConstants.DOB, textDate);
                        intent.putExtra(RelatedPropertyNameConstants.GENDER, gender);
                        intent.putExtra(RelatedPropertyNameConstants.MOBILE_NUMBER, mobileNumber);
                        String memberId = selectedMember.getId();
                        if (memberId != null) {
                            intent.putExtra(RelatedPropertyNameConstants.MEMBER_ID, Integer.parseInt(memberId));
                        }
                        startActivityForResult(intent, requestCode);
                    } else {
                        SewaUtil.generateToast(this, LabelConstants.PLEASE_SELECT_A_MEMBER);
                    }
                    break;
                default:
                    navigateToHomeScreen(false);
            }
        }
    }

    @UiThread
    public void addSearchTextBox() {
        bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));

        final TextInputLayout editText = MyStaticComponents.getEditText(this, LabelConstants.SEARCH_FAMILY, 1, 20, 10);
        bodyLayoutContainer.addView(editText);
        bodyLayoutContainer.addView(MyStaticComponents.getOrTextView(context));

        if (editText.getEditText() != null) {
            editText.getEditText().addTextChangedListener(new TextWatcher() {
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
                                            retrieveFamilyListFromDB(s.toString());
                                            editText.setFocusable(true);
                                        });
                                    } else if (s == null || s.length() == 0) {
                                        runOnUiThread(() -> {
                                            showProcessDialog();
                                            retrieveFamilyListFromDB(null);
                                            editText.clearFocus();
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

    private void addFamilyList() {
        retrieveFamilyListFromDB(null);
    }

    @UiThread
    public void addVillageSelectionSpinner() {
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.SELECT_VILLAGE)));
        String[] arrayOfOptions = new String[locationBeans.size()];
        int i = 0;
        for (LocationBean locationBean : locationBeans) {
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }
        spinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 2);
        if (locationId != null) {
            int integerLocationId = Integer.parseInt(locationId);
            for (LocationBean locationBean : locationBeans) {
                String locationName;
                if (locationBean.getActualID() == integerLocationId) {
                    locationName = locationBean.getName();
                    spinner.setSelection(Arrays.asList(arrayOfOptions).indexOf(locationName));
                }
            }
        }
        bodyLayoutContainer.addView(spinner);
        hideProcessDialog();
    }


    @Background
    public void retrieveFamilyListFromDB(String search) {
        offset = 0;
        selectedFamilyIndex = -1;
        if (search == null) {
            familyDataBeans = fhsService.retrieveFamilyDataBeansForAbhaNumberByVillage(locationId, LIMIT, offset);
            offset = offset + LIMIT;
        } else {
            familyDataBeans = fhsService.searchFamilyDataBeansForAbhaNumberByVillage(search, locationId);
        }
        setFamilySelectionScreen(search != null);
    }

    @UiThread
    public void setFamilySelectionScreen(boolean isSearch) {
        bodyLayoutContainer.removeView(pagingHeaderView);
        bodyLayoutContainer.removeView(noFamilyAvailableView);
        bodyLayoutContainer.removeView(paginatedListView);

        if (familyDataBeans != null && !familyDataBeans.isEmpty()) {
            pagingHeaderView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_FAMILY);
            bodyLayoutContainer.addView(pagingHeaderView);
            List<ListItemDataBean> list = getFamilyList(familyDataBeans);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedFamilyIndex = position;

            if (isSearch) {
                paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_three_item, onItemClickListener, null);
            } else {
                paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_three_item, onItemClickListener, this);
            }
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
            nextButton.setOnClickListener(this);
        } else {
            noFamilyAvailableView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_FAMILIES_FOUND);
            bodyLayoutContainer.addView(noFamilyAvailableView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
            nextButton.setOnClickListener(v -> navigateToHomeScreen(false));
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
        Map<String, MemberDataBean> headMembersMapWithFamilyIdAsKey = fhsService.retrieveHeadMemberDataBeansByFamilyId(familyIds);

        for (FamilyDataBean familyDataBean : familyDataBeans) {
            MemberDataBean headMember = headMembersMapWithFamilyIdAsKey.get(familyDataBean.getFamilyId());
            if (headMember != null) {
                familyDataBean.setHeadMemberName(headMember.getFirstName() + " " + headMember.getMiddleName() + " " + headMember.getGrandfatherName() + " " + headMember.getLastName());
                familyDataBean.setHeadMemberName(familyDataBean.getHeadMemberName().replace(" " + LabelConstants.NULL, ""));
                rbText = headMember.getFirstName() + " " + headMember.getMiddleName() + " " + headMember.getGrandfatherName() + " " + headMember.getLastName();
                rbText = rbText.replace(" " + LabelConstants.NULL, "");
            } else {
                rbText = UtilBean.getMyLabel(LabelConstants.HEAD_NAME_NOT_AVAILABLE);
            }
            if (FhsConstants.CFHC_VERIFIED_FAMILY_STATES.contains(familyDataBean.getState())) {
                list.add(new ListItemDataBean(null, familyDataBean.getFamilyId(), null, null, rbText, true, familyDataBean.getPendingAbhaCount()));
            } else {
                list.add(new ListItemDataBean(null, familyDataBean.getFamilyId(), null, null, rbText, false, familyDataBean.getPendingAbhaCount()));
            }
        }
        return list;
    }


    private List<ListItemDataBean> getMemberList(List<MemberDataBean> familyDataBeans) {
        List<ListItemDataBean> list = new ArrayList<>();
        String rbText;
        for (MemberDataBean memberDataBean : familyDataBeans) {
            rbText = memberDataBean.getFirstName() + " " + memberDataBean.getMiddleName() + " " + memberDataBean.getLastName();
            String replace = rbText.replace(" null", "");
            replace = replace.replace("null ", "");
            replace = replace.trim();
            boolean isHealthID = memberDataBean.getHealthIdNumber() != null && !memberDataBean.getHealthIdNumber().isEmpty();
            if (FhsConstants.CFHC_VERIFIED_FAMILY_STATES.contains(memberDataBean.getState())) {
                list.add(new ListItemDataBean(memberDataBean.getUniqueHealthId(), replace, true, isHealthID));
            } else {
                list.add(new ListItemDataBean(memberDataBean.getUniqueHealthId(), replace, false, isHealthID));
            }
        }
        return list;
    }

    private void addSelectedFamilyDetails(FamilyDataBean familyDataBean) {
        List<String> states = new ArrayList<>();
        selectedMemberIndex = -1;
        setSubTitle(UtilBean.getMyLabel(LabelConstants.FAMILY_DETAILS));
        states.addAll(FhsConstants.FHS_VERIFIED_CRITERIA_FAMILY_STATES);
        states.addAll(FhsConstants.FHS_NEW_CRITERIA_FAMILY_STATES);
        if (states.contains(familyDataBean.getState())) {
            familyDataBean.setMembers(fhsService.retrieveMemberDataBeansExceptArchivedAndDeadByFamilyId(familyDataBean.getFamilyId()));
        } else {
            familyDataBean.setMembers(fhsService.retrieveMemberDataBeansByFamily(familyDataBean.getFamilyId()));
        }

        LinearLayout familyDetailsLinearLayout = MyStaticComponents.getDetailsLayout(context, -1, LinearLayout.VERTICAL, bodyLayoutContainer.getPaddingTop());
        familyDetailsLinearLayout.addView(MyStaticComponents.generateLabelView(this, UtilBean.getMyLabel(LabelConstants.FAMILY_ID)));
        familyDetailsLinearLayout.addView(MyStaticComponents.generateBoldAnswerView(this, familyDataBean.getFamilyId()));
        bodyLayoutContainer.addView(familyDetailsLinearLayout);

        if (familyDataBean.getMembers() != null && !familyDataBean.getMembers().isEmpty()) {
            pagingHeaderView = MyStaticComponents.getListTitleView(this, LabelConstants.MEMBERS_INFO);
            bodyLayoutContainer.addView(pagingHeaderView);
            List<ListItemDataBean> list = getMemberList(familyDataBean.getMembers());
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
                selectedMemberIndex = position;
                selectedMember = selectedFamily.getMembers().get(selectedMemberIndex);
            };
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_abha_number, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
            nextButton.setOnClickListener(this);
        } else {
            noFamilyAvailableView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBERS_FOUND);
            bodyLayoutContainer.addView(noFamilyAvailableView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
            nextButton.setOnClickListener(v -> onBackPressed());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        footerLayout.setVisibility(View.VISIBLE);
        if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            selectedFamilyIndex = -1;
            if (resultCode == RESULT_OK) {
                locationId = data.getStringExtra(FieldNameConstants.LOCATION_ID);
                showProcessDialog();
                screen = FAMILY_SELECTION_SCREEN;
                bodyLayoutContainer.removeAllViews();
                addSearchTextBox();
                addFamilyList();
            } else {
                finish();
            }
        } else if (requestCode == ActivityConstants.HEALTH_ID_MANAGEMENT_REQUEST_CODE) {
            if (selectedMember == null) return;
            if (data != null && data.getStringExtra(RelatedPropertyNameConstants.HEALTH_ID_NUMBER) != null) {
                if (data.getBooleanExtra("isFromOffline", false)) {
                    try {
                        MemberBean memberBean = memberBeansDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_I_D, selectedMember.getId()).queryForFirst();
                        abhaCreationService.createMigrationForOfflineAbha(data.getStringExtra(RelatedPropertyNameConstants.HEALTH_ID_NUMBER), memberBean);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                } else {
                    try {
                        MemberBean memberBean = memberBeansDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_I_D, selectedMember.getId()).queryForFirst();
                        memberBean.setHealthIdNumber(data.getStringExtra(RelatedPropertyNameConstants.HEALTH_ID_NUMBER));
                        memberBean.setHealthId(data.getStringExtra(RelatedPropertyNameConstants.HEALTH_ID));
                        memberBean.setUpdatedOn(new Date());
                        memberBeansDao.update(memberBean);

                        FamilyBean familyBean = familyBeansDao.queryBuilder().where().eq(FieldNameConstants.FAMILY_ID, selectedFamily.getFamilyId()).queryForFirst();
                        int count = 0;
                        count = familyBean.getPendingAbhaCount();
                        if (count > 0) {
                            familyBean.setPendingAbhaCount(count - 1);
                        }
                        familyBeansDao.update(familyBean);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
            handleBackClick();
        } else if (requestCode == ActivityConstants.OFFLINE_HEALTH_ID_MANAGEMENT_REQUEST_CODE) {
            if (data != null && data.getStringExtra(OfflineHealthIdBean.class.getSimpleName()) != null) {
                MemberBean memberBean = null;
                if (selectedMember != null) {
                    try {
                        memberBean = memberBeansDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_I_D, selectedMember.getId()).queryForFirst();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                abhaCreationService.createMigrationForOfflineAbha(data.getStringExtra(OfflineHealthIdBean.class.getSimpleName()), memberBean);
            }
            handleBackClick();
        }
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
                LabelConstants.ARE_YOU_SURE_WANT_TO_CLOSE_HEALTH_ID_CREATION,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    @Override
    public void onLoadMoreItems() {
        if (screen.equals(FAMILY_SELECTION_SCREEN)) {
            onLoadMoreBackground();
        }
    }

    @Background
    public void onLoadMoreBackground() {
        List<FamilyDataBean> famDataBeans = fhsService.retrieveFamilyDataBeansForAbhaNumberByVillage(locationId, LIMIT, offset);
        offset = offset + LIMIT;
        onLoadMoreUi(famDataBeans);
    }

    @UiThread
    public void onLoadMoreUi(List<FamilyDataBean> familyDataBeansList) {
        if (familyDataBeansList != null && !familyDataBeansList.isEmpty()) {
            List<ListItemDataBean> stringList = getFamilyList(familyDataBeansList);
            familyDataBeans.addAll(familyDataBeansList);
            paginatedListView.onFinishLoadingWithItem(true, stringList);
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
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
            footerLayout.setVisibility(View.VISIBLE);
            handleBackClick();
        }
        return true;
    }

    private void handleBackClick() {
        switch (screen) {
            case FAMILY_SELECTION_SCREEN:
                startLocationSelectionActivity();
                break;
            case OPTION_SELECTION_SCREEN:
                bodyLayoutContainer.removeAllViews();
                addSearchTextBox();
                addFamilyList();
                screen = FAMILY_SELECTION_SCREEN;
                break;
            default:
                navigateToHomeScreen(false);
                finish();
        }
    }
}
