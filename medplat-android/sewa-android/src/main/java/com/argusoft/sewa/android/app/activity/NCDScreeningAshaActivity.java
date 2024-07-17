package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.argusoft.sewa.android.app.core.impl.NcdServiceImpl;
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
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
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
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by prateek on 14/11/18.
 */

@EActivity
public class NCDScreeningAshaActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    @Bean
    public SewaServiceImpl sewaService;
    @Bean
    public SewaFhsServiceImpl fhsService;
    @Bean
    public FormMetaDataUtil formMetaDataUtil;
    @Bean
    public NcdServiceImpl ncdService;

    private static final String ASHA_AREA_SELECTION_SCREEN = "ashaAreaSelectionScreen";
    private static final String FAMILY_SELECTION_SCREEN = "familySelectionScreen";
    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";
    private static final String SERVICE_SELECTION_SCREEN = "serviceSelectionScreen";
    private static final Integer REQUEST_CODE_FOR_NCD_SCREENING_ACTIVITY = 200;
    private static final long DELAY = 500;
    private static final long LIMIT = 30;

    private Timer timer = new Timer();
    private long offset;
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private String screen;
    private Spinner spinner;
    private List<LocationBean> ashaAreaList = new ArrayList<>();
    private Integer selectedAshaArea;
    private FamilyDataBean selectedFamily;
    private List<FamilyDataBean> familyDataBeans = new ArrayList<>();
    private List<MemberDataBean> memberDataBeans = new ArrayList<>();
    private LinearLayout globalPanel;
    private PagingListView paginatedListView;
    private int selectedMemberIndex = -1;
    private int selectedFamilyIndex = -1;
    private String rbText;
    private String searchString;
    private MaterialTextView titleView;
    private MaterialTextView noFamilyAvailableTextView;
    private LinearLayout footerView;
    private int selectedServiceIndex;
    private MemberDataBean selectedMember;
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
        setTitle(UtilBean.getTitleText(LabelConstants.CBAC_SCREENING_ACTIVITY_TITLE));
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
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.CBAC_SCREENING_ACTIVITY_TITLE);
        startActivityForResult(myIntent, ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != DynamicUtils.ID_NEXT_BUTTON) {
            return;
        }

        switch (screen) {
            case ASHA_AREA_SELECTION_SCREEN:
                String selectedArea = spinner.getSelectedItem().toString();
                for (LocationBean locationBean : ashaAreaList) {
                    if (selectedArea.equals(locationBean.getName())) {
                        selectedAshaArea = locationBean.getActualID();
                        break;
                    }
                }
                showProcessDialog();
                addSearchTextBox();
                retrieveFamilyListFromDB(null, null);
                break;

            case FAMILY_SELECTION_SCREEN:
                if (selectedFamilyIndex != 0 && (familyDataBeans == null || familyDataBeans.isEmpty())) {
                    navigateToHomeScreen(false);
                    finish();
                } else {
                    if (selectedFamilyIndex != -1) {
                        selectedFamily = familyDataBeans.get(selectedFamilyIndex);
                        retrieveMemberListFromDB(selectedFamily.getFamilyId());
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_FAMILY));
                    }
                }
                break;

            case MEMBER_SELECTION_SCREEN:
                if (selectedMemberIndex != -1) {
                    selectedMember = memberDataBeans.get(selectedMemberIndex);
                    setServiceSelectionScreen();
                    if (selectedMember.getCbacDate() != null && new Date(selectedMember.getCbacDate()).after(UtilBean.getStartOfFinancialYear(null))) {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.CBAC_ENTRY_FOR_SELECTED_MEMBER_IS_ALREADY_DONE));
                        return;
                    }
                    showProcessDialog();
                    Intent myIntent = new Intent(this, DynamicFormActivity_.class);
                    myIntent.putExtra(SewaConstants.ENTITY, FormConstants.NCD_ASHA_CBAC);
                    setMetadataForFormByFormType(selectedMember);
                    startActivityForResult(myIntent, REQUEST_CODE_FOR_NCD_SCREENING_ACTIVITY);
                    hideProcessDialog();
                } else {
                    SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.MEMBER_SELECTION_REQUIRED_FROM_FAMILY_ALERT));
                }
                break;
            case SERVICE_SELECTION_SCREEN:
                if (selectedServiceIndex != -1) {
                    if (selectedServiceIndex == 0) {
                        if (selectedMember.getCbacDate() != null && new Date(selectedMember.getCbacDate()).after(UtilBean.getStartOfFinancialYear(null))) {
                            SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.CBAC_ENTRY_FOR_SELECTED_MEMBER_IS_ALREADY_DONE));
                            return;
                        }
                        showProcessDialog();
                        Intent myIntent = new Intent(this, DynamicFormActivity_.class);
                        myIntent.putExtra(SewaConstants.ENTITY, FormConstants.NCD_ASHA_CBAC);
                        setMetadataForFormByFormType(selectedMember);
                        startActivityForResult(myIntent, REQUEST_CODE_FOR_NCD_SCREENING_ACTIVITY);
                        hideProcessDialog();
                    } else if (selectedServiceIndex == 1) {
                        if (Boolean.TRUE.equals(selectedMember.getPersonalHistoryDone())) {
                            SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PH_ENTRY_FOR_SELECTED_MEMBER_IS_ALREADY_DONE));
                            return;
                        }
                        showProcessDialog();
                        Intent myIntent = new Intent(this, DynamicFormActivity_.class);
                        myIntent.putExtra(SewaConstants.ENTITY, FormConstants.NCD_PERSONAL_HISTORY);
                        setMetadataForFormByFormType(selectedMember);
                        startActivityForResult(myIntent, REQUEST_CODE_FOR_NCD_SCREENING_ACTIVITY);
                        hideProcessDialog();
                    }
                } else {
                    SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_TYPE_OF_SERVICE));
                }
                break;
            default:
        }
    }

    private void addLastUpdateLabel() {
        bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
    }

    @UiThread
    public void addAshaAreaSelectionSpinner() {
        screen = ASHA_AREA_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.SELECT_ASHA_AREA));
        String[] arrayOfOptions = new String[ashaAreaList.size()];
        int i = 0;
        for (LocationBean locationBean : ashaAreaList) {
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }
        spinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 2);
        if (selectedAshaArea != null) {
            for (LocationBean locationBean : ashaAreaList) {
                String locationName;
                if (locationBean.getActualID().intValue() == selectedAshaArea.intValue()) {
                    locationName = locationBean.getName();
                    spinner.setSelection(Arrays.asList(arrayOfOptions).indexOf(locationName));
                }
            }
        }
        bodyLayoutContainer.addView(spinner);
        hideProcessDialog();
    }

    @UiThread
    public void addSearchTextBox() {
        bodyLayoutContainer.removeAllViews();
        addLastUpdateLabel();
        final TextInputLayout editText;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            editText = MyStaticComponents.getEditTextWithQrScan(this, LabelConstants.SEARCH_FAMILY, 1, 20, 1);
        }else {
            editText = MyStaticComponents.getEditText(this, LabelConstants.SEARCH_FAMILY, 1, 20, 1);
        }
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
                                            retrieveFamilyListFromDB(s.toString(),null);
                                            editText.setFocusable(true);
                                        });
                                    } else if (s == null || s.length() == 0) {
                                        runOnUiThread(() -> {
                                            showProcessDialog();
                                            retrieveFamilyListFromDB(null, null);
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

    @Background
    public void retrieveFamilyListFromDB(String familyId, String qrData) {
        offset = 0;
        searchString = familyId;
        qrScanFilter = SewaUtil.setQrScanFilterData(qrData);
        selectedFamilyIndex = -1;
        familyDataBeans = ncdService.retrieveFamiliesForCbacEntry(familyId, selectedAshaArea, LIMIT, offset, qrScanFilter);
        offset = offset + LIMIT;
        setFamilySelectionScreen(familyId != null);
    }

    @UiThread
    public void setFamilySelectionScreen(boolean isSearch) {
        screen = FAMILY_SELECTION_SCREEN;
        bodyLayoutContainer.removeView(paginatedListView);
        bodyLayoutContainer.removeView(titleView);
        bodyLayoutContainer.removeView(noFamilyAvailableTextView);

        List<ListItemDataBean> list;
        rbText = "";

        if (!familyDataBeans.isEmpty()) {
            titleView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_FAMILY);
            bodyLayoutContainer.addView(titleView);

            list = getFamilyList(familyDataBeans);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedFamilyIndex = position;
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            noFamilyAvailableTextView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_FAMILIES_FOUND);
            bodyLayoutContainer.addView(noFamilyAvailableTextView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }

        if (!isSearch) {
            hideProcessDialog();
        }
    }

    private List<ListItemDataBean> getFamilyList(List<FamilyDataBean> familyDataBeans) {
        List<String> familyIds = new ArrayList<>();
        List<ListItemDataBean> list = new ArrayList<>();

        for (FamilyDataBean familyDataBean : familyDataBeans) {
            familyIds.add(familyDataBean.getFamilyId());
        }
        Map<String, MemberDataBean> headMembersMapWithFamilyIdAsKey = fhsService.retrieveHeadMemberDataBeansByFamilyId(familyIds);

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

    @Background
    public void retrieveMemberListFromDB(String familyId) {
        selectedMemberIndex = -1;
        if (familyId != null) {
            memberDataBeans = ncdService.retrieveMembersListForCbacEntry(familyId);
            setMemberSelectionScreen();
        }
    }

    @UiThread
    public void setMemberSelectionScreen() {
        screen = MEMBER_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        StringBuilder memberId;
        List<ListItemDataBean> list = new ArrayList<>();

        if (!memberDataBeans.isEmpty()) {
            bodyLayoutContainer.addView(MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_A_MEMBER_FROM_LIST));

            for (MemberDataBean memberDataBean : memberDataBeans) {
                int[] ageArray = UtilBean.calculateAgeYearMonthDay(memberDataBean.getDob());
                int ageInYears = ageArray[0];
                int ageInMonth = ageArray[1];
                int ageInDays = ageArray[2];
                Long cbacDate = memberDataBean.getCbacDate();
                if ((ageInYears > 6) || (ageInYears == 6 && (ageInMonth > 0 || ageInDays > 0))) {
                    Calendar oneYearBack = Calendar.getInstance();
                    oneYearBack.add(Calendar.YEAR, -1);
                    if (cbacDate != null && new Date(cbacDate).after(oneYearBack.getTime())) {
                        memberId = new StringBuilder(" ✔️️");
                        memberId.append(memberDataBean.getUniqueHealthId());
                    } else {
                        memberId = new StringBuilder(memberDataBean.getUniqueHealthId());
                    }
                } else {
                    Calendar fifteenDaysBack = Calendar.getInstance();
                    fifteenDaysBack.add(Calendar.DAY_OF_YEAR, -15);

                    Calendar threeMonthBack = Calendar.getInstance();
                    threeMonthBack.add(Calendar.MONTH, -3);


                    if (memberDataBean.getDob() != null && ageInYears == 0 && ageInMonth < 6) {
                        if (cbacDate != null && new Date(cbacDate).after(fifteenDaysBack.getTime())) {
                            memberId = new StringBuilder(" ✔️️");
                            memberId.append(memberDataBean.getUniqueHealthId());
                        } else {
                            memberId = new StringBuilder(memberDataBean.getUniqueHealthId());
                        }
                    } else {
                        if (cbacDate != null && new Date(cbacDate).after(threeMonthBack.getTime())) {
                            memberId = new StringBuilder(" ✔️️");
                            memberId.append(memberDataBean.getUniqueHealthId());
                        } else {
                            memberId = new StringBuilder(memberDataBean.getUniqueHealthId());
                        }
                    }
                }

                list.add(new ListItemDataBean(memberId.toString(), UtilBean.getMemberFullName(memberDataBean) + " (" + UtilBean.getAgeDisplayOnGivenDate(new Date(memberDataBean.getDob()), new Date()) + ")"));
            }
            PagingListView listView = MyStaticComponents.getPaginatedListViewWithItem(
                    context, list, R.layout.listview_row_with_item,
                    (parent, view, position, id) -> selectedMember = memberDataBeans.get(position),
                    null);
            bodyLayoutContainer.addView(listView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, UtilBean.getMyLabel(LabelConstants.NO_MEMBERS_HAVING_AGE_MORE_THAN_30_YEARS_IN_FAMILY)));
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
            nextButton.setOnClickListener(v -> navigateToHomeScreen(false));
        }
    }

    private void setMetadataForFormByFormType(MemberDataBean memberDataBean) {
        FamilyDataBean familyDataBean = null;
        if (memberDataBean != null) {
            familyDataBean = fhsService.retrieveFamilyDataBeanByFamilyId(memberDataBean.getFamilyId());
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        if (memberDataBean != null) {
            formMetaDataUtil.setMetaDataForNCDForms(memberDataBean, familyDataBean, sharedPref);
        }
    }

    private void setServiceSelectionScreen() {
        bodyLayoutContainer.removeAllViews();
        screen = SERVICE_SELECTION_SCREEN;
        setSubTitle(UtilBean.getMemberFullName(selectedMember));

        MaterialTextView serviceType = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_SERVICE_TYPE);
        bodyLayoutContainer.addView(serviceType);

        selectedServiceIndex = -1;
        List<ListItemDataBean> list = new ArrayList<>();

        list.add(new ListItemDataBean(LabelConstants.CBAC_SCREENING_ACTIVITY_TITLE));
        list.add(new ListItemDataBean(LabelConstants.PERSONAL_HISTORY));

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            selectedServiceIndex = position;
            onClick(nextButton);
        };
        ListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_type, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
        footerView.setVisibility(View.GONE);
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
                LabelConstants.ON_NCD_SCREENING_CLOSE_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult resultForQRScanner = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        selectedMemberIndex = -1;
        setSubTitle(null);
        if (requestCode == REQUEST_CODE_FOR_NCD_SCREENING_ACTIVITY) {
            int count = 0;
            memberDataBeans = ncdService.retrieveMembersListForCbacEntry(selectedFamily.getFamilyId());
            for (MemberDataBean memberDataBean : memberDataBeans) {
                if (Boolean.TRUE.equals(selectedMember.getPersonalHistoryDone()) && memberDataBean.getCbacDate() != null && new Date(memberDataBean.getCbacDate()).after(UtilBean.getStartOfFinancialYear(null))) {
                    count++;
                }
            }

            if (count == memberDataBeans.size()) {
                showProcessDialog();
                addSearchTextBox();
                retrieveFamilyListFromDB(null, null);
            } else {
                retrieveMemberListFromDB(selectedFamily.getFamilyId());
            }
            footerView.setVisibility(View.VISIBLE);
        } else if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                selectedAshaArea = Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID));
                showProcessDialog();
                addSearchTextBox();
                retrieveFamilyListFromDB(null, null);
            } else {
                navigateToHomeScreen(false);
            }
        } else if (resultForQRScanner != null) {
            if (resultForQRScanner.getContents() == null) {
                SewaUtil.generateToast(this, LabelConstants.FAILED_TO_SCAN_QR);
            } else {
                //show dialogue with resultForQRScanner
                String scanningData = resultForQRScanner.getContents();
                Log.i(getClass().getSimpleName(), "QR Scanner Data : " + scanningData);
                retrieveFamilyListFromDB(null, scanningData);
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
        }

        if (item.getItemId() == android.R.id.home) {
            footerView.setVisibility(View.VISIBLE);
            setSubTitle(null);
            switch (screen) {
                case SERVICE_SELECTION_SCREEN:
                    selectedMemberIndex = -1;
                    nextButton.setOnClickListener(this);
                    setMemberSelectionScreen();
                    break;

                case MEMBER_SELECTION_SCREEN:
                    showProcessDialog();
                    selectedFamilyIndex = -1;
                    addSearchTextBox();
                    nextButton.setOnClickListener(this);
                    retrieveFamilyListFromDB(null, null);
                    break;

                case FAMILY_SELECTION_SCREEN:
                    startLocationSelectionActivity();
                    break;

                case ASHA_AREA_SELECTION_SCREEN:
                    navigateToHomeScreen(false);
                    break;
                default:
                    finish();
            }
        }
        return true;
    }

    @Override
    public void onLoadMoreItems() {
        onLoadMoreBackground();
    }

    @Background
    public void onLoadMoreBackground() {
        List<FamilyDataBean> familyDataBeanList = ncdService.retrieveFamiliesForCbacEntry(searchString, selectedAshaArea, LIMIT, offset, qrScanFilter);
        offset = offset + LIMIT;
        onLoadMoreUi(familyDataBeanList);
    }

    @UiThread
    public void onLoadMoreUi(List<FamilyDataBean> familyDataBeanList) {
        if (familyDataBeanList != null && !familyDataBeanList.isEmpty()) {
            familyDataBeans.addAll(familyDataBeanList);
            paginatedListView.onFinishLoadingWithItem(true, getFamilyList(familyDataBeanList));
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
        }
    }
}
