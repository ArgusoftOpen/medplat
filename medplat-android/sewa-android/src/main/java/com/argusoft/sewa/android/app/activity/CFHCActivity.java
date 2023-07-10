package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.FhsConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.core.impl.LocationMasterServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberAdditionalInfoDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.OptionDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.model.VersionBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

@EActivity
public class CFHCActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    LocationMasterServiceImpl locationService;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<VersionBean, Integer> versionBeanDao;

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
    private boolean isReverification;
    private boolean isAshaNotAssigned;

    private String locationId;
    private List<LocationBean> locationBeans = new ArrayList<>();
    private List<FamilyDataBean> familyDataBeans = null;
    private FamilyDataBean selectedFamily;
    private MaterialTextView pagingHeaderView;
    private MaterialTextView noFamilyAvailableView;
    private int selectedFamilyIndex = -1;
    private PagingListView paginatedListView;
    private int offset = 0;
    private LinearLayout footerLayout;
    private LinkedHashMap<String, String> qrScanFilter = new LinkedHashMap<>();

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
        isReverification = getIntent().getBooleanExtra("reverification", false);
        setTitle(UtilBean.getTitleText(LabelConstants.COMPREHENSIVE_FAMILY_HEALTH_CENSUS_TITLE));
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
        startLocationSelectionActivity();
    }

    private void startLocationSelectionActivity() {
        myIntent = new Intent(context, LocationSelectionActivity_.class);
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.CFHC_TITLE);
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
                        addOptionForFamily();
                        screen = OPTION_SELECTION_SCREEN;
                    } else {
                        SewaUtil.generateToast(this, LabelConstants.SELECT_A_FAMILY_FOR_VERIFICATION);
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

        List<String> options = new ArrayList<>();
        if (!isReverification) {
            options.add(UtilBean.getMyLabel(LabelConstants.ADD_NEW_FAMILY));

            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
                if (position == 0) {
                    startFamilyHealthSurvey(null);
                }
            };

            ListView buttonList = MyStaticComponents.getButtonList(context, options, onItemClickListener);
            buttonList.setPadding(0, 0, 0, 30);
            bodyLayoutContainer.addView(buttonList);
        }

        final TextInputLayout editText;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            editText = MyStaticComponents.getEditTextWithQrScan(this, LabelConstants.SEARCH_FAMILY, 1, 20, 10);
        }else {
            editText = MyStaticComponents.getEditText(this, LabelConstants.SEARCH_FAMILY, 1, 20, 10);
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
                                            retrieveFamilyListFromDB(s.toString(), null);
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

    private void addFamilyList() {
//        addAshaNotAssignedMessage();
//        if (!isAshaNotAssigned) {
            retrieveFamilyListFromDB(null, null);
//        }
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
    public void retrieveFamilyListFromDB(String search, String qrData) {
        offset = 0;
        selectedFamilyIndex = -1;
        qrScanFilter = SewaUtil.setQrScanFilterData(qrData);
        if (search == null && qrData == null) {
            familyDataBeans = fhsService.retrieveFamilyDataBeansForCFHCByVillage(locationId, isReverification, LIMIT, offset);
            offset = offset + LIMIT;
        } else {
            familyDataBeans = fhsService.searchFamilyDataBeansForCFHCByVillage(search, locationId, isReverification, qrScanFilter);
        }
        setFamilySelectionScreen(search != null || qrData != null);
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
                paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_two_item, onItemClickListener, null);
            } else {
                paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_two_item, onItemClickListener, this);
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
                list.add(new ListItemDataBean(null, familyDataBean.getFamilyId(), null, null, rbText, true));
            } else {
                list.add(new ListItemDataBean(null, familyDataBean.getFamilyId(), null, null, rbText, false));
            }
        }
        return list;
    }

    private static String getReverificationComment(FamilyDataBean familyDataBean) {
        StringBuilder comment = new StringBuilder();
        comment.append(UtilBean.getMyLabel(LabelConstants.FAMILY_ID));
        comment.append(": ");
        comment.append(familyDataBean.getFamilyId());
        comment.append("\n");
        comment.append(UtilBean.getMyLabel(LabelConstants.HEAD_OF_THE_FAMILY));
        comment.append(": ");
        if (familyDataBean.getHeadMemberName() == null) {
            for (MemberDataBean memberDataBean : familyDataBean.getMembers()) {
                if (memberDataBean.getFamilyHeadFlag() != null && memberDataBean.getFamilyHeadFlag()) {
                    comment.append(UtilBean.getMemberFullName(memberDataBean));
                }
            }
        } else {
            comment.append(familyDataBean.getHeadMemberName());
        }
        if (familyDataBean.getComment() != null) {
            comment.append("\n");
            comment.append(familyDataBean.getComment());
        }
        return comment.toString();
    }

    private void addAshaNotAssignedMessage() {
        List<LocationBean> areas = locationService.retrieveLocationBeansByLevelAndParent(7, Long.valueOf(locationId));
        if (areas != null && !areas.isEmpty()) {
            for (LocationBean locationBean : areas) {
                String[] locationName = locationBean.getName().split("-");
                if (locationName.length <= 1) {
                    isAshaNotAssigned = true;
                    break;
                }
            }

            if (isAshaNotAssigned) {
                runOnUiThread(() -> {
                    View.OnClickListener myListener = v -> navigateToHomeScreen(false);
                    alertDialog = new MyAlertDialog(context, false,
                            GlobalTypes.MSG_ASHA_NOT_ASSIGNED,
                            myListener, DynamicUtils.BUTTON_OK);
                    alertDialog.show();
                });
            }
        }
    }

    private void addSelectedFamilyDetails(FamilyDataBean familyDataBean) {
        List<String> states = new ArrayList<>();
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
        familyDetailsLinearLayout.addView(MyStaticComponents.generateLabelView(this, UtilBean.getMyLabel(LabelConstants.MEMBERS_INFO)));
        familyDetailsLinearLayout.addView(UtilBean.getMembersListForDisplay(this, familyDataBean));

        TextView infoColorCodes = new MaterialTextView(context);
        infoColorCodes.setText(UtilBean.getMyLabel("Click here to see color codes"));
        infoColorCodes.setTextColor(ContextCompat.getColor(context, R.color.black));
        infoColorCodes.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_info_black), null, null, null);
        infoColorCodes.setCompoundDrawablePadding(20);
        infoColorCodes.setTextSize(14);
        infoColorCodes.setGravity(Gravity.CENTER_VERTICAL);
        familyDetailsLinearLayout.addView(infoColorCodes);
        bodyLayoutContainer.addView(familyDetailsLinearLayout);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialogCustom = builder.create();
        View alertView = LayoutInflater.from(this).inflate(R.layout.color_code_alert, null);
        alertDialogCustom.setView(alertView);
        alertDialogCustom.setCancelable(true);

        infoColorCodes.setOnClickListener(v -> {
            alertView.findViewById(R.id.buttonAlert).setOnClickListener(v2 -> {
                alertDialogCustom.dismiss();
            });
            alertDialogCustom.show();
        });

    }

    private void startFamilyHealthSurvey(FamilyDataBean familyDataBean) {
        showProcessDialog();
        myIntent = new Intent(this, DynamicFormActivity_.class);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().clear().apply();
        SharedStructureData.relatedPropertyHashTable.clear();
        setSharedPropertiesAndDataMaps(familyDataBean);

        String nextEntity = FormConstants.CFHC;
        myIntent.putExtra(SewaConstants.ENTITY, nextEntity);
        startActivityForResult(myIntent, ActivityConstants.FHS_ACTIVITY_REQUEST_CODE);
        showProcessDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult resultForQRScanner = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        footerLayout.setVisibility(View.VISIBLE);
        selectedFamilyIndex = -1;
        if (requestCode == ActivityConstants.FHS_ACTIVITY_REQUEST_CODE) {
            bodyLayoutContainer.removeAllViews();
            addSearchTextBox();
            addFamilyList();
            screen = FAMILY_SELECTION_SCREEN;
            hideProcessDialog();
        } else if (requestCode == ActivityConstants.FAMILY_MERGE_ACTIVITY_REQUEST_CODE) {
            boolean isMerged = data.getBooleanExtra("isMerged", false);
            if (isMerged) {
                String familyIdToExpand = data.getStringExtra("familyToExpand");
                String familyIdToMerge = data.getStringExtra("familyToMerge");

                FamilyDataBean familyDataBean = fhsService.retrieveFamilyDataBeanByFamilyId(familyIdToExpand);
                List<String> states = new ArrayList<>();
                states.addAll(FhsConstants.FHS_VERIFIED_CRITERIA_FAMILY_STATES);
                states.addAll(FhsConstants.FHS_NEW_CRITERIA_FAMILY_STATES);
                List<MemberDataBean> memberDataBeans = new ArrayList<>();
                if (states.contains(familyDataBean.getState())) {
                    familyDataBean.setMembers(memberDataBeans);
                    familyDataBean.getMembers().addAll(fhsService.retrieveMemberDataBeansExceptArchivedAndDeadByFamilyId(familyIdToExpand));
                    familyDataBean.getMembers().addAll(fhsService.retrieveMemberDataBeansExceptArchivedAndDeadByFamilyId(familyIdToMerge));
                } else {
                    familyDataBean.setMembers(memberDataBeans);
                    familyDataBean.getMembers().addAll(fhsService.retrieveMemberDataBeansByFamily(familyIdToExpand));
                    familyDataBean.getMembers().addAll(fhsService.retrieveMemberDataBeansByFamily(familyIdToMerge));
                }
                startFamilyHealthSurvey(familyDataBean);
            } else {
                bodyLayoutContainer.removeAllViews();
                addSelectedFamilyDetails(selectedFamily);
                addOptionForFamily();
                screen = OPTION_SELECTION_SCREEN;
            }
        } else if (requestCode == ActivityConstants.FAMILY_MIGRATION_OUT_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                bodyLayoutContainer.removeAllViews();
                addSearchTextBox();
                addFamilyList();
                screen = FAMILY_SELECTION_SCREEN;
            } else if (resultCode == RESULT_CANCELED) {
                bodyLayoutContainer.removeAllViews();
                addSelectedFamilyDetails(selectedFamily);
                addOptionForFamily();
                screen = OPTION_SELECTION_SCREEN;
            }
        } else if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
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
        }  else if (resultForQRScanner != null) {
            if (resultForQRScanner.getContents() == null) {
                SewaUtil.generateToast(this, LabelConstants.FAILED_TO_SCAN_QR);
            } else {
                //show dialogue with resultForQRScanner
                String scanningData = resultForQRScanner.getContents();
                com.argusoft.sewa.android.app.util.Log.i(getClass().getSimpleName(), "QR Scanner Data : " + scanningData);
                retrieveFamilyListFromDB(null, scanningData);
            }
        }
    }

    private void setSharedPropertiesAndDataMaps(FamilyDataBean familyDataBean) {
        try {
            List<VersionBean> featureVersionBeans = versionBeanDao.queryForEq("key", GlobalTypes.VERSION_FEATURES_LIST);
            if (featureVersionBeans != null && !featureVersionBeans.isEmpty()) {
                VersionBean versionBean = featureVersionBeans.get(0);
                if (versionBean.getValue().contains(GlobalTypes.MOB_FEATURE_ABDM_HEALTH_ID)) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.GENERATE_ABDM_HEALTH_ID, GlobalTypes.MOB_FEATURE_ABDM_HEALTH_ID);
                }
            }
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }

        if (familyDataBean == null) {
            SharedStructureData.currentFamilyDataBean = null;
            SharedStructureData.familyDataBeanToBeMerged = null;
            SharedStructureData.totalFamilyMembersCount = -1;
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FAMILY_ID, LabelConstants.NOT_AVAILABLE);
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.HEAD_OF_FAMILY, LabelConstants.NOT_AVAILABLE);
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.AREA_ID, locationId);
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FAMILY_FOUND, "1");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault());

        SharedStructureData.totalFamilyMembersCount = familyDataBean.getMembers().size();
        SharedStructureData.currentFamilyDataBean = familyDataBean;
        SharedStructureData.isMobileVerificationBlocked = false;
        SharedStructureData.isAnyMemberMobileVerificationDone = false;
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FAMILY_ID, familyDataBean.getFamilyId());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.REVERIFICATION_COMMENT, getReverificationComment(familyDataBean));
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.HOUSE_NUMBER, familyDataBean.getHouseNumber());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ADDRESS_1, familyDataBean.getAddress1());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ADDRESS_2, familyDataBean.getAddress2());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MAA_VATSALYA_NUMBER, familyDataBean.getMaaVatsalyaNumber());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.RSBY_CARD_NUMBER, familyDataBean.getRsbyCardNumber());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.RATION_CARD_NUMBER, familyDataBean.getRationCardNumber());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.BPL_CARD_NUMBER, familyDataBean.getBplCardNumber());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LOCATION_ID, familyDataBean.getLocationId());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.STATE, familyDataBean.getState());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DEFAULT_CASTE, familyDataBean.getCaste());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DEFAULT_RELIGION, familyDataBean.getReligion());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.HEAD_OF_FAMILY, LabelConstants.NOT_AVAILABLE);
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.VULNERABILITY_CRITERIA, familyDataBean.getVulnerabilityCriteria());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TYPE_OF_HOUSE, familyDataBean.getTypeOfHouse());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DRINKING_WATER_SOURCE, familyDataBean.getDrinkingWaterSource());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.TOILET_AVAILABLE_FLAG, familyDataBean.getTypeOfToilet());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.COOKING_GAS, familyDataBean.getFuelForCooking());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MOTORIZED_VEHICLE, familyDataBean.getVehicleDetails());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ELECTRIC_CONNECTION, familyDataBean.getElectricityAvailability());
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.BENEFICIARY_NAME_FOR_LOG,
                familyDataBean.getFamilyId() + "-" + familyDataBean.getHeadMemberName());


        if (familyDataBean.getAreaId() != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.AREA_ID, familyDataBean.getAreaId());
        }

        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.BPL_FLAG, "2");
        if (familyDataBean.getBplFlag() != null && familyDataBean.getBplFlag()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.BPL_FLAG, "1");
        }

        if (familyDataBean.getVulnerableFlag() != null && familyDataBean.getVulnerableFlag()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.VULNERABLE_FLAG, "1");
        } else if (familyDataBean.getVulnerableFlag() != null && !familyDataBean.getVulnerableFlag()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.VULNERABLE_FLAG, "2");
        }

        if (familyDataBean.getSeasonalMigrantFlag() != null && familyDataBean.getSeasonalMigrantFlag()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.SEASONAL_MIGRANT_FAMILY, "1");
        } else if (familyDataBean.getSeasonalMigrantFlag() != null && !familyDataBean.getSeasonalMigrantFlag()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.SEASONAL_MIGRANT_FAMILY, "2");
        }

        if (familyDataBean.getAnganwadiId() != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DEFAULT_PHC,
                    String.valueOf(fhsService.retrieveSubcenterIdFromAnganwadiId(Integer.valueOf(familyDataBean.getAnganwadiId()))));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DEFAULT_ANGANWADI, familyDataBean.getAnganwadiId());
        }

        if (familyDataBean.getProvidingConsent() != null && familyDataBean.getProvidingConsent()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IS_PROVIDING_CONSENT, "1");
        } else if (familyDataBean.getProvidingConsent() != null && !familyDataBean.getProvidingConsent()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.IS_PROVIDING_CONSENT, "2");
        }

        if (familyDataBean.getEligibleForPmjay() != null && familyDataBean.getEligibleForPmjay()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ELIGIBLE_FOR_PMJAY, "1");
        } else if (familyDataBean.getEligibleForPmjay() != null && !familyDataBean.getEligibleForPmjay()) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.ELIGIBLE_FOR_PMJAY, "2");
        }

        if (isReverification) {
            if (familyDataBean.getState().equals(FhsConstants.FHS_FAMILY_STATE_ARCHIVED)
                    || familyDataBean.getState().equals(FhsConstants.FHS_FAMILY_STATE_ARCHIVED_FHSR_REVERIFICATION)
                    || familyDataBean.getState().equals(FhsConstants.FHS_FAMILY_STATE_ARCHIVED_MO_REVERIFICATION)) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FAMILY_FOUND, "3");
            } else if (familyDataBean.getState().equals(FhsConstants.FHS_FAMILY_STATE_NEW_FHSR_REVERIFICATION)
                    || familyDataBean.getState().equals(FhsConstants.FHS_FAMILY_STATE_NEW_MO_REVERIFICATION)) {
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.FAMILY_FOUND, "1");
            }
        }

        int count = 0;
        Set<String> middleNamesSet = new HashSet<>();
        for (MemberDataBean memberDataBean : familyDataBean.getMembers()) {
            SharedStructureData.relatedPropertyHashTable.put(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.MEMBER_ID, count),
                    memberDataBean.getId());
            SharedStructureData.relatedPropertyHashTable.put(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.MEMBER_FULL_NAME, count),
                    UtilBean.getMemberFullName(memberDataBean));
            SharedStructureData.relatedPropertyHashTable.put(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.FIRST_NAME, count),
                    memberDataBean.getFirstName());
            SharedStructureData.relatedPropertyHashTable.put(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.MIDDLE_NAME, count),
                    memberDataBean.getMiddleName());
            SharedStructureData.relatedPropertyHashTable.put(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.LAST_NAME, count),
                    memberDataBean.getLastName());
            SharedStructureData.relatedPropertyHashTable.put(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.MOBILE_NUMBER, count),
                    memberDataBean.getMobileNumber());
            SharedStructureData.relatedPropertyHashTable.put(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.AADHAR_NUMBER, count),
                    memberDataBean.getAadharNumber());
            SharedStructureData.relatedPropertyHashTable.put(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.BANK_ACCOUNT_NUMBER, count),
                    memberDataBean.getAccountNumber());
            SharedStructureData.relatedPropertyHashTable.put(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.IFSC, count),
                    memberDataBean.getIfsc());
            SharedStructureData.relatedPropertyHashTable.put(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.UNIQUE_HEALTH_ID, count),
                    memberDataBean.getUniqueHealthId());
            SharedStructureData.relatedPropertyHashTable.put(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.DEFAULT_MARITAL_STATUS, count),
                    memberDataBean.getMaritalStatus());
            SharedStructureData.relatedPropertyHashTable.put(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.ANS_12, count),
                    "1");
            SharedStructureData.relatedPropertyHashTable.put(
                    UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.MEMBER_STATE, count),
                    memberDataBean.getState());

            if (memberDataBean.getDob() != null) {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.DOB, count),
                        memberDataBean.getDob().toString());
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.DOB_DISPLAY, count),
                        sdf.format(memberDataBean.getDob()));

                int[] ageYearMonthDayArray = UtilBean.calculateAgeYearMonthDayOnGivenDate(memberDataBean.getDob(), new Date().getTime());
                String ageDisplay = UtilBean.getAgeDisplay(ageYearMonthDayArray[0], ageYearMonthDayArray[1], ageYearMonthDayArray[2]);
                if (ageDisplay != null) {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.AGE_AS_PER_DATE, count),
                            ageDisplay);
                }
            }

            if (memberDataBean.getDateOfWedding() != null) {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.DATE_OF_WEDDING, count),
                        memberDataBean.getDateOfWedding().toString());
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.DATE_OF_WEDDING_DISPLAY, count),
                        sdf.format(memberDataBean.getDateOfWedding()));
            }

                if (memberDataBean.getGender() != null) {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.GENDER, count),
                        UtilBean.getGenderLabelFromValue(memberDataBean.getGender()));
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.ANS_12, count),
                        UtilBean.getGenderValueAs123FromGender(memberDataBean.getGender()));
            }

            if (memberDataBean.getVulnerableFlag() != null && memberDataBean.getVulnerableFlag()) {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.IS_VULNERABLE, count),
                        "1");
            } else {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.IS_VULNERABLE, count),
                        "2");
            }

            if (memberDataBean.getPmjayAvailability() != null) {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.HAVE_PMJAY, count),
                        memberDataBean.getPmjayAvailability());
            }
            if (memberDataBean.getAlcoholAddiction() != null) {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.ALCOHOL_ADDICTION, count),
                        memberDataBean.getAlcoholAddiction());
            }
            if (memberDataBean.getSmokingAddiction() != null) {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.SMOKING_ADDICTION, count),
                        memberDataBean.getSmokingAddiction());
            }
            if (memberDataBean.getTobaccoAddiction() != null) {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.TOBACCO_ADDICTION, count),
                        memberDataBean.getTobaccoAddiction());
            }

            if (memberDataBean.getFamilyHeadFlag() != null && memberDataBean.getFamilyHeadFlag()) {
                SharedStructureData.relatedPropertyHashTable.put(
                        RelatedPropertyNameConstants.HEAD_OF_FAMILY, UtilBean.getMemberFullName(memberDataBean));

                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.FAMILY_HEAD_FLAG, count),
                        "1");
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.GRANDFATHER_NAME, count),
                        memberDataBean.getGrandfatherName());
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.HEAD_COUNT, count),
                        String.valueOf(count));
            } else {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.FAMILY_HEAD_FLAG, count),
                        "2");
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.RELATION_WITH_HOF, count),
                        memberDataBean.getRelationWithHof());
            }

            if (memberDataBean.getMotherId() != null) {
                MemberBean mother = fhsService.retrieveMemberBeanByActualId(memberDataBean.getMotherId());
                if (mother != null) {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.MOTHER_ID, count),
                            memberDataBean.getMotherId().toString());
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.MOTHER_NAME, count),
                            UtilBean.getMemberFullName(mother));
                }
            }

            if (memberDataBean.getHusbandId() != null) {
                MemberBean husband = fhsService.retrieveMemberBeanByActualId(memberDataBean.getHusbandId());
                if (husband != null) {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.HUSBAND_ID, count),
                            memberDataBean.getHusbandId().toString());
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.HUSBAND_NAME, count),
                            UtilBean.getMemberFullName(husband));
                }
            }

            if (memberDataBean.isAadharUpdated()) {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.AADHAR_AVAILABLE, count),
                        "1");
            } else {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.AADHAR_AVAILABLE, count),
                        "2");
            }

            if (memberDataBean.getNormalCycleDays() != null) {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.DEFAULT_NORMAL_CYCLE_DAYS, count),
                        memberDataBean.getNormalCycleDays().toString());
            }


            if (memberDataBean.getMenopauseArrived() != null && memberDataBean.getMenopauseArrived()) {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.MENOPAUSE_ARRIVED, count),
                        "1");
            } else {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.MENOPAUSE_ARRIVED, count),
                        "2");
            }

            if (memberDataBean.getFamilyPlanningMethod() != null) {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.DEFAULT_FAMILY_PLANNING_METHOD, count),
                        memberDataBean.getFamilyPlanningMethod());
            }

            if (memberDataBean.getGender() != null && memberDataBean.getGender().equals(GlobalTypes.FEMALE)) {
                if (memberDataBean.getIsPregnantFlag() != null && memberDataBean.getIsPregnantFlag()) {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.DEFAULT_IS_PREGNANT, count),
                            "1");
                } else {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.DEFAULT_IS_PREGNANT, count),
                            "2");
                }
                if (memberDataBean.getLmpDate() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.DEFAULT_LMP, count),
                            memberDataBean.getLmpDate().toString());
                }
                if (memberDataBean.getLastDeliveryDate() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.LAST_DELIVERY_DATE, count),
                            memberDataBean.getLastDeliveryDate().toString());
                }
            }

            if (memberDataBean.getLastMethodOfContraception() != null) {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.LAST_METHOD_OF_CONTRACEPTION, count),
                        memberDataBean.getLastMethodOfContraception());
            }
            if (memberDataBean.getMobileNumberVerified() != null && memberDataBean.getMobileNumberVerified()) {
                SharedStructureData.isAnyMemberMobileVerificationDone = true;
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.MOBILE_NUMBER_VERIFIED, count),
                        "1");
            } else {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.MOBILE_NUMBER_VERIFIED, count),
                        "2");
            }

            if (memberDataBean.getEducationStatus() != null) {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.EDUCATION_STATUS, count),
                        memberDataBean.getEducationStatus());
            }

            if (memberDataBean.getHealthInsurance() != null && memberDataBean.getHealthInsurance()) {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.ANY_INSURANCE_SCHEME, count),
                        "1");
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.SCHEME_DETAILS, count),
                        memberDataBean.getSchemeDetail());
            } else if (memberDataBean.getHealthInsurance() != null && !memberDataBean.getHealthInsurance()) {
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.ANY_INSURANCE_SCHEME, count),
                        "2");
            }

            if (memberDataBean.getAdditionalInfo() != null && !memberDataBean.getAdditionalInfo().isEmpty()) {
                MemberAdditionalInfoDataBean memberAdditionalInfoDataBean = new Gson().fromJson(memberDataBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
                if (memberAdditionalInfoDataBean.getHeight() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.HEIGHT, count),
                            memberAdditionalInfoDataBean.getHeight().toString()
                    );
                }
                if (memberAdditionalInfoDataBean.getWeight() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.WEIGHT, count),
                            memberAdditionalInfoDataBean.getWeight().toString()
                    );
                }
                if (memberAdditionalInfoDataBean.getBmi() != null) {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.BMI, count),
                            memberAdditionalInfoDataBean.getBmi().toString()
                    );
                }
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.RISK_FACTOR, count),
                        memberAdditionalInfoDataBean.getRiskFactor()
                );
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.DISEASE_HISTORY, count),
                        memberAdditionalInfoDataBean.getDiseaseHistory()
                );
                SharedStructureData.relatedPropertyHashTable.put(
                        UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.OTHER_DISEASE_HISTORY, count),
                        memberAdditionalInfoDataBean.getOtherDiseaseHistory()
                );
            }

            if (isReverification) {
                if (FhsConstants.FHS_DEAD_CRITERIA_MEMBER_STATES.contains(memberDataBean.getState())) {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.MEMBER_STATUS, count),
                            "2");
                } else if (FhsConstants.FHS_ARCHIVED_CRITERIA_MEMBER_STATES.contains(memberDataBean.getState())) {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.MEMBER_STATUS, count),
                            "3");
                } else {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.MEMBER_STATUS, count),
                            "1");
                }

                if (memberDataBean.getCongenitalAnomalyIds() != null && !memberDataBean.getCongenitalAnomalyIds().isEmpty()) {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.CONGENITAL_STATUS, count),
                            "1");
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.CONGENITAL_ANOMALY_IDS, count),
                            memberDataBean.getCongenitalAnomalyIds());
                } else {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.CONGENITAL_STATUS, count),
                            "2");
                }

                if (memberDataBean.getChronicDiseaseIds() != null && !memberDataBean.getChronicDiseaseIds().isEmpty()) {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.CHRONIC_DISEASE_STATUS, count),
                            "1");
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.CHRONIC_DISEASE_IDS, count),
                            memberDataBean.getChronicDiseaseIds());
                } else {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.CHRONIC_DISEASE_STATUS, count),
                            "2");
                }

                if (memberDataBean.getCurrentDiseaseIds() != null && !memberDataBean.getCurrentDiseaseIds().isEmpty()) {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.CURRENT_DISEASE_STATUS, count),
                            "1");
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.CURRENT_DISEASE_IDS, count),
                            memberDataBean.getCurrentDiseaseIds());
                } else {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.CURRENT_DISEASE_STATUS, count),
                            "2");
                }

                if (memberDataBean.getEyeIssueIds() != null && !memberDataBean.getEyeIssueIds().isEmpty()) {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.EYE_ISSUE_STATUS, count),
                            "1");
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.EYE_ISSUE_IDS, count),
                            memberDataBean.getEyeIssueIds());
                } else {
                    SharedStructureData.relatedPropertyHashTable.put(
                            UtilBean.getRelatedPropertyNameWithLoopCounter(RelatedPropertyNameConstants.EYE_ISSUE_STATUS, count),
                            "2");
                }
            }

            if (memberDataBean.getMiddleName() != null && !memberDataBean.getMiddleName().isEmpty()) {
                middleNamesSet.add(memberDataBean.getMiddleName());
            }

            count++;
        }

        List<OptionDataBean> middleNameOptions = new ArrayList<>();
        for (String middleName : middleNamesSet) {
            middleNameOptions.add(new OptionDataBean(middleName, middleName, null));
        }
        middleNameOptions.add(new OptionDataBean("1", LabelConstants.ADD_NEW, null));
        SharedStructureData.middleNameList = middleNameOptions;
    }

    private void addOptionForFamily() {
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(
                null, null, this, UtilBean.getMyLabel(LabelConstants.WHAT_DO_YOU_WANT_TO_DO_WITH_THIS_FAMILY)));
        List<String> options = new ArrayList<>();

        if (isReverification) {
            options.add(UtilBean.getMyLabel(LabelConstants.RE_VERIFY_THIS_FAMILY));
        } else {
            options.add(UtilBean.getMyLabel(LabelConstants.VERIFY_THIS_FAMILY));
            options.add(UtilBean.getMyLabel(LabelConstants.MERGE_THIS_FAMILY_WITH_ANOTHER_FAMILY));
            options.add(UtilBean.getMyLabel(LabelConstants.MIGRATE_THIS_FAMILY_TO_ANOTHER_LOCATION));
            options.add(UtilBean.getMyLabel(LabelConstants.VIEW_QR_CODE));
        }

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            if (position == 0) {
                List<String> states = new ArrayList<>();
                states.addAll(FhsConstants.FHS_VERIFIED_CRITERIA_FAMILY_STATES);
                states.addAll(FhsConstants.FHS_NEW_CRITERIA_FAMILY_STATES);
                if (states.contains(selectedFamily.getState())) {
                    selectedFamily.setMembers(fhsService.retrieveMemberDataBeansExceptArchivedAndDeadByFamilyId(selectedFamily.getFamilyId()));
                } else {
                    selectedFamily.setMembers(fhsService.retrieveMemberDataBeansByFamily(selectedFamily.getFamilyId()));
                }
                startFamilyHealthSurvey(selectedFamily);
            } else if (position == 1) {
                myIntent = new Intent(context, FamilyMergeActivity_.class);
                myIntent.putExtra("familyToExpand", new Gson().toJson(selectedFamily));
                myIntent.putExtra("locationId", locationId);
                startActivityForResult(myIntent, ActivityConstants.FAMILY_MERGE_ACTIVITY_REQUEST_CODE);
            } else if (position == 2) {
                myIntent = new Intent(context, FamilyMigrationOutActivity_.class);
                selectedFamily.setMembers(fhsService.retrieveMemberDataBeansExceptArchivedAndDeadByFamilyId(selectedFamily.getFamilyId()));
                myIntent.putExtra("familyToMigrate", new Gson().toJson(selectedFamily));
                myIntent.putExtra("locationId", locationId);
                myIntent.putExtra("isFamilyMigration", true);
                startActivityForResult(myIntent, ActivityConstants.FAMILY_MIGRATION_OUT_ACTIVITY_REQUEST_CODE);
            } else if (position == 3) {
                myIntent = new Intent(context, QRCodeActivity_.class);
                myIntent.putExtra(FieldNameConstants.QR_DATA, selectedFamily.getFamilyId());
                startActivity(myIntent);
            }
        };

        ListView buttonList = MyStaticComponents.getButtonList(context, options, onItemClickListener);
        bodyLayoutContainer.addView(buttonList);
        footerLayout.setVisibility(View.GONE);
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
                LabelConstants.CLOSE_COMPREHENSIVE_FAMILY_HEALTH_CENSUS,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    @Override
    public void onLoadMoreItems() {
        onLoadMoreBackground();
    }

    @Background
    public void onLoadMoreBackground() {
        List<FamilyDataBean> famDataBeans = fhsService.retrieveFamilyDataBeansForCFHCByVillage(locationId, isReverification, LIMIT, offset);
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
                    finish();
            }
        }
        return true;
    }
}
