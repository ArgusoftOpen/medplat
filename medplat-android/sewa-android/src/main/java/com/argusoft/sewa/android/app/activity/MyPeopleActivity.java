package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyDynamicComponents;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.FhsConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.FullFormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.NotificationConstants;
import com.argusoft.sewa.android.app.constants.RchConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.core.impl.ImmunisationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.MigrationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.NotificationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceRestClientImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberAdditionalInfoDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.QueryMobDataBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.exception.DataException;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.LocationMasterBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.model.MigratedFamilyBean;
import com.argusoft.sewa.android.app.model.MigratedMembersBean;
import com.argusoft.sewa.android.app.restclient.RestHttpException;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.FormMetaDataUtil;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
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
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by prateek on 28/6/18.
 */

@EActivity
public class MyPeopleActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    @Bean
    SewaServiceRestClientImpl restClient;
    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    NotificationServiceImpl notificationService;
    @Bean
    FormMetaDataUtil formMetaDataUtil;
    @Bean
    MigrationServiceImpl migrationService;
    @Bean
    ImmunisationServiceImpl immunisationService;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<MemberBean, Integer> memberBeanDao;

    private static final String TAG = "MyPeopleActivity";
    private static final Integer REQUEST_CODE_FOR_MY_PEOPLE_ACTIVITY = 200;
    private static final long DELAY = 500;
    private static final String VILLAGE_SELECTION_SCREEN = "villageSelectionScreen";
    private static final String SERVICE_SELECTION_SCREEN = "serviceSelectionScreen";
    private static final String PEOPLE_SELECTION_SCREEN = "peopleSelectionScreen";
    private static final String VISIT_SELECTION_SCREEN = "visitSelectionScreen";
    private static final String CHART_SELECTION_SCREEN = "chartSelectionScreen";
    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";
    private static final String FAMILY_SELECTION_SCREEN = "familySelectionScreen";
    private static final String MIGRATED_MEMBERS_SCREEN = "migratedMembersScreen";
    private static final String RCH_REGISTER_MEMBER_SCREEN = "rchRegisterMemberScreen";
    private static final String RCH_REGISTER_TABLE_SCREEN = "rchRegisterTableScreen";
    private static final String MANAGE_FAMILY_MIGRATIONS_SCREEN = "manageFamilyMigrationsScreen";
    private static final String SERVICE_ELIGIBLE_COUPLES = "eligibleCouples";
    private static final String SERVICE_ADOLESCENT = "adolscent";
    private static final String SERVICE_PREGNANT_WOMEN = "pregnantWomen";
    private static final String SERVICE_PNC_MOTHERS = "pncMothers";
    private static final String SERVICE_CHILDREN = "children";
    private static final String SERVICE_TEMP_REGISTRATION = "tempRegistration";
    private static final String SERVICE_UPDATE_MEMBER = "updateMember";
    private static final String SERVICE_ADD_NEW_MEMBER = "addNewMember";
    private static final String SERVICE_MIGRATED_OUT_MEMBERS = "migratedOutMembers";
    private static final String SERVICE_MIGRATED_IN_MEMBERS = "migratedInMembers";
    private static final String SERVICE_RCH_REGISTER = "rchRegister";
    private static final String SERVICE_GERIATRIC_MEMBERS = "geriatricMembers";
    private static final String SERVICE_TRAVELLERS_SCREENING = "travellersScreening";
    private static final String MIGRATED_FAMILY_SCREEN = "migratedFamilyScreen";
    private static final String SERVICE_MIGRATED_OUT_FAMILY = "migratedOutFamily";
    private static final String SERVICE_MIGRATED_IN_FAMILY = "migratedInFamily";
    private static final String SERVICE_MANAGE_FAMILY_MIGRATIONS = "manageFamilyMigrations";

    private List<MigratedMembersBean> migratedMembersBeans;
    private List<MigratedFamilyBean> migratedFamilyBeans;
    private List<MemberDataBean> removedChildren;
    private SharedPreferences sharedPref;
    private LinearLayout bodyLayoutContainer;
    private LinearLayout footerLayout;
    private Button nextButton;
    private Intent myIntent;
    private Spinner villageSpinner;
    private Spinner ashaAreaSpinner;
    private String screen;
    private String selectedService;
    private List<LocationBean> villageList = new ArrayList<>();
    private List<LocationBean> ashaAreaList = new ArrayList<>();
    private Integer selectedVillage;
    private List<Integer> selectedAshaAreas = new ArrayList<>();
    private List<MemberDataBean> memberList = new ArrayList<>();
    private MemberDataBean memberSelected = null;
    private List<FamilyDataBean> familyList = new ArrayList<>();
    private FamilyDataBean familySelected = null;
    private Timer timer = new Timer();
    private MyAlertDialog myAlertDialog;
    private String rbText;
    private PagingListView pagingListView;
    private MaterialTextView pagingHeaderView;
    private MaterialTextView noMemberAvailableView;
    private int selectedServiceIndex = -1;
    private int selectedPeopleIndex = -1;
    private int selectedFamilyIndex = -1;
    private int selectedVisitIndex = -1;
    private boolean isMigratedOut;
    private long limit = 30;
    private long offset = 0;
    private List<Integer> villageIds;
    private List<String> visits;
    private CharSequence searchString;
    private LinearLayout globalPanel;
    private int selectedMemberToUpdateIndex = -1;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
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
        if (Objects.equals(selectedService, SERVICE_TRAVELLERS_SCREENING)) {
            footerLayout.setVisibility(View.GONE);
        }
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
        setTitle(UtilBean.getTitleText(LabelConstants.MY_PEOPLE_TITLE));
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        setContentView(globalPanel);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        footerLayout = globalPanel.findViewById(DynamicUtils.ID_FOOTER);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        setContentView(globalPanel);
        setBodyDetail();
    }

    @Background
    public void setBodyDetail() {
        startLocationSelectionActivity();
    }

    private void startLocationSelectionActivity() {
        myIntent = new Intent(context, LocationSelectionActivity_.class);
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.MY_PEOPLE_TITLE);
        startActivityForResult(myIntent, ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            setSubTitle(null);
            switch (screen) {
                case VILLAGE_SELECTION_SCREEN:
                    showProcessDialog();
                    String selectedVillageName = villageSpinner.getSelectedItem().toString();
                    for (LocationBean locationBean : villageList) {
                        if (selectedVillageName.equals(locationBean.getName())) {
                            selectedVillage = locationBean.getActualID();
                            break;
                        }
                    }
                    String selectedAshaArea = ashaAreaSpinner.getSelectedItem().toString();
                    if (selectedAshaArea.equals(LabelConstants.ALL)) {
                        for (LocationBean locationBean : ashaAreaList) {
                            selectedAshaAreas.add(locationBean.getActualID());
                        }
                    } else {
                        for (LocationBean locationBean : ashaAreaList) {
                            if (selectedAshaArea.equals(locationBean.getName())) {
                                selectedAshaAreas.add(locationBean.getActualID());
                                break;
                            }
                        }
                    }
                    bodyLayoutContainer.removeAllViews();
                    setServiceSelectionScreen();
                    break;

                case SERVICE_SELECTION_SCREEN:
                    if (selectedServiceIndex != -1) {
                        switch (selectedServiceIndex) {
                            case 0:
                                selectedService = SERVICE_ELIGIBLE_COUPLES;
                                break;
                            case 1:
                                selectedService = SERVICE_PREGNANT_WOMEN;
                                break;
                            case 2:
                                selectedService = SERVICE_PNC_MOTHERS;
                                break;
                            case 3:
                                selectedService = SERVICE_CHILDREN;
                                break;
                            case 4:
                                showProcessDialog();
                                selectedService = SERVICE_RCH_REGISTER;
                                bodyLayoutContainer.removeAllViews();
                                bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                                addSearchTextBox();
                                retrieveMemberListForRchRegister(null, null);
                                break;
                            case 5 :
                                selectedService = SERVICE_ADOLESCENT;
                                break;
//                            case 4:
//                                selectedService = SERVICE_TEMP_REGISTRATION;
//                                showProcessDialog();
//                                sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//                                sharedPref.edit().clear().apply();
//                                myIntent = new Intent(this, MigrateInActivity_.class);
//                                startActivityForResult(myIntent, REQUEST_CODE_FOR_MY_PEOPLE_ACTIVITY);
//                                hideProcessDialog();
//                                break;
//                            case 5:
//                                selectedService = SERVICE_UPDATE_MEMBER;
//                                screen = MEMBER_SELECTION_SCREEN;
//                                bodyLayoutContainer.removeAllViews();
//                                addSearchTextBox();
//                                break;
//                            case 6:
//                                selectedService = SERVICE_ADD_NEW_MEMBER;
//                                screen = FAMILY_SELECTION_SCREEN;
//                                bodyLayoutContainer.removeAllViews();
//                                addSearchTextBox();
//                                break;
//                            case 7:
//                                selectedService = SERVICE_MIGRATED_IN_MEMBERS;
//                                showProcessDialog();
//                                retrieveMigratedMembersList(false);
//                                break;
//                            case 8:
//                                selectedService = SERVICE_MIGRATED_OUT_MEMBERS;
//                                showProcessDialog();
//                                retrieveMigratedMembersList(true);
//                                break;
//                            case 9:
//                                showProcessDialog();
//                                selectedService = SERVICE_RCH_REGISTER;
//                                bodyLayoutContainer.removeAllViews();
//                                bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
//                                addSearchTextBox();
//                                retrieveMemberListForRchRegister(null, null);
//                                break;
//                            case 10:
//                                selectedService = SERVICE_GERIATRIC_MEMBERS;
//                                Intent geriatricActivityIntent = new Intent(this, MemberGeriatricListActivity_.class);
//                                geriatricActivityIntent.putExtra("locationId", new Gson().toJson(selectedAshaAreas));
//                                startActivityForResult(geriatricActivityIntent, REQUEST_CODE_FOR_MY_PEOPLE_ACTIVITY);
//                                break;
//                            case 11:
//                                selectedService = SERVICE_TRAVELLERS_SCREENING;
//                                Intent travellersScreeningActivityIntent = new Intent(this, TravellersScreeningActivity_.class);
//                                travellersScreeningActivityIntent.putExtra("locationId", new Gson().toJson(selectedAshaAreas));
//                                startActivityForResult(travellersScreeningActivityIntent, REQUEST_CODE_FOR_MY_PEOPLE_ACTIVITY);
//                                break;
//                            case 12:
//                                selectedService = SERVICE_MANAGE_FAMILY_MIGRATIONS;
//                                showProcessDialog();
//                                setManageFamilyMigrationsScreen();
//                                break;
                            default:
                        }
                        if (!SERVICE_MANAGE_FAMILY_MIGRATIONS.equals(selectedService)) {
                            footerLayout.setVisibility(View.VISIBLE);
                        }
                        if (selectedService.equals(SERVICE_ELIGIBLE_COUPLES)
                                || selectedService.equals(SERVICE_PREGNANT_WOMEN)
                                || selectedService.equals(SERVICE_PNC_MOTHERS)
                                || selectedService.equals(SERVICE_ADOLESCENT)
                                || selectedService.equals(SERVICE_CHILDREN)) {
                            screen = PEOPLE_SELECTION_SCREEN;
                            showProcessDialog();
                            bodyLayoutContainer.removeAllViews();
                            bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                            addSearchTextBox();
                            retrieveMemberListByServiceType(selectedService, null, false, null);
                        }
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.SERVICE_SELECTION_REQUIRED_ALERT));
                    }
                    break;

                case MANAGE_FAMILY_MIGRATIONS_SCREEN:
                    if (selectedVisitIndex == 0) {
                        selectedService = SERVICE_MIGRATED_IN_FAMILY;
                        showProcessDialog();
                        retrieveMigratedFamilyList(false);
                        footerLayout.setVisibility(View.VISIBLE);
                    } else if (selectedVisitIndex == 1) {
                        selectedService = SERVICE_MIGRATED_OUT_FAMILY;
                        showProcessDialog();
                        retrieveMigratedFamilyList(true);
                        footerLayout.setVisibility(View.VISIBLE);
                    }
                    break;
                case CHART_SELECTION_SCREEN:
                    screen = PEOPLE_SELECTION_SCREEN;
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                    addSearchTextBox();
                    retrieveMemberListByServiceType(selectedService, null, false, null);
                    break;

                case PEOPLE_SELECTION_SCREEN:
                    if (selectedPeopleIndex != -1) {
                        showProcessDialog();
                        memberSelected = memberList.get(selectedPeopleIndex);
                        setSubTitle(UtilBean.getMemberFullName(memberSelected));
                        setVisits();
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_MEMBER));
                    }
                    break;

                case VISIT_SELECTION_SCREEN:
                    if (selectedVisitIndex != -1) {
                        showProcessDialog();
                        footerLayout.setVisibility(View.VISIBLE);
                        setSubTitle(UtilBean.getMemberFullName(memberSelected));
                        final String formType = visits.get(selectedVisitIndex);
                        if (formType != null) {
                            switch (formType) {
                                case NotificationConstants.FHW_NOTIFICATION_WORK_PLAN_FOR_DELIVERY:
                                    if (Boolean.TRUE.equals(notificationService.checkIfThereArePendingNotificationsForMember(
                                            Long.valueOf(memberSelected.getId()), NotificationConstants.FHW_NOTIFICATION_ANC))) {
                                        View.OnClickListener myListener = v1 -> {
                                            if (v1.getId() == BUTTON_POSITIVE) {
                                                myAlertDialog.dismiss();
                                                startDynamicFormActivity(formType, memberSelected, null);
                                            } else {
                                                myAlertDialog.dismiss();
                                            }
                                        };

                                        myAlertDialog = new MyAlertDialog(this,
                                                LabelConstants.PROCEED_FROM_WPD_WHILE_ANC_ALERTS_PENDING_ALERT,
                                                myListener, DynamicUtils.BUTTON_YES_NO);
                                        myAlertDialog.show();
                                    } else {
                                        startDynamicFormActivity(formType, memberSelected, null);
                                    }
                                    break;
                                case FormConstants.PREGNANCY_STATUS:
                                    Intent intent = new Intent(this, PregnancyStatusActivity_.class);
                                    intent.putExtra(RelatedPropertyNameConstants.MEMBER_ID, Long.parseLong(memberSelected.getId()));
                                    startActivity(intent);
                                    break;
                                case FormConstants.TECHO_AWW_WEIGHT_GROWTH_GRAPH:
                                    footerLayout.setVisibility(View.GONE);
                                    addGraph();
                                    break;
                                default:
                                    startDynamicFormActivity(formType, memberSelected, null);
                                    break;
                            }
                        }
                        selectedVisitIndex = -1;
                        hideProcessDialog();
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.VISIT_SELECTION_REQUIRED_ALERT));
                    }
                    break;

                case MEMBER_SELECTION_SCREEN:
                    if (memberList != null && !memberList.isEmpty()) {
                        if (selectedMemberToUpdateIndex != -1) {
                            memberSelected = memberList.get(selectedMemberToUpdateIndex);
                            startDynamicFormActivity(FormConstants.FHS_MEMBER_UPDATE, memberSelected, null);
                        } else {
                            SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_MEMBER));
                        }
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.MEMBER_SEARCH_REQUIRED_ALERT));
                    }
                    break;

                case FAMILY_SELECTION_SCREEN:
                    if (familyList != null && !familyList.isEmpty()) {
                        if (selectedFamilyIndex != -1) {
                            familySelected = familyList.get(selectedFamilyIndex);
                            startDynamicFormActivity(FormConstants.FHS_MEMBER_UPDATE, null, familySelected);
                        } else {
                            SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_FAMILY));
                        }
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.FAMILY_SEARCH_REQUIRED_ALERT));
                    }
                    break;

                case MIGRATED_MEMBERS_SCREEN:
                    if (migratedMembersBeans == null || migratedMembersBeans.isEmpty()) {
                        navigateToHomeScreen(false);
                        break;
                    }
                    if (selectedPeopleIndex != -1) {
                        MigratedMembersBean selectedMigratedMembersBean = migratedMembersBeans.get(selectedPeopleIndex);
                        Intent intent = new Intent(MyPeopleActivity.this, MigrationRevertActivity_.class);
                        intent.putExtra("migratedMemberBean", new Gson().toJson(selectedMigratedMembersBean));
                        if (selectedService.equals(SERVICE_MIGRATED_OUT_MEMBERS)) {
                            intent.putExtra("isOut", true);
                        } else if (selectedService.equals(SERVICE_MIGRATED_IN_MEMBERS)) {
                            intent.putExtra("isOut", false);
                        }
                        startActivityForResult(intent, REQUEST_CODE_FOR_MY_PEOPLE_ACTIVITY);
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.MIGRATED_MEMBER_SELECTION_REQUIRED_ALERT));
                    }
                    break;

                case MIGRATED_FAMILY_SCREEN:
                    if (migratedFamilyBeans == null || migratedFamilyBeans.isEmpty()) {
                        navigateToHomeScreen(false);
                        break;
                    }
                    if (selectedPeopleIndex != -1) {
                        MigratedFamilyBean selectedMigratedFamilyBean = migratedFamilyBeans.get(selectedPeopleIndex);
                        Intent intent = new Intent(MyPeopleActivity.this, FamilyMigrationRevertActivity_.class);
                        intent.putExtra("migratedFamilyBean", new Gson().toJson(selectedMigratedFamilyBean));
                        if (selectedService.equals(SERVICE_MIGRATED_OUT_FAMILY)) {
                            intent.putExtra("isOut", true);
                        } else if (selectedService.equals(SERVICE_MIGRATED_IN_FAMILY)) {
                            intent.putExtra("isOut", false);
                        }
                        startActivityForResult(intent, REQUEST_CODE_FOR_MY_PEOPLE_ACTIVITY);
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.MIGRATED_FAMILY_SELECTION_REQUIRED_ALERT));
                    }
                    break;

                case RCH_REGISTER_MEMBER_SCREEN:
                    if (selectedPeopleIndex == -1) {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_MEMBER));
                        return;
                    }
                    showProcessDialog();
                    memberSelected = memberList.get(selectedPeopleIndex);
                    showNotOnlineMessage();
                    bodyLayoutContainer.removeAllViews();
                    retrieveRCHServicesProvidedToMember();
                    break;

                case RCH_REGISTER_TABLE_SCREEN:
                    navigateToHomeScreen(false);
                    break;

                default:
                    break;
            }
        }
    }

    @UiThread
    public void setManageFamilyMigrationsScreen() {
        screen = MANAGE_FAMILY_MIGRATIONS_SCREEN;
        setTitle(LabelConstants.MANAGE_FAMILY_MIGRATIONS);
        footerLayout.setVisibility(View.GONE);
        bodyLayoutContainer.removeAllViews();
        List<ListItemDataBean> items = new ArrayList<>();
        items.add(new ListItemDataBean(UtilBean.getMyLabel(LabelConstants.SERVICE_MIGRATED_IN_FAMILY)));
        items.add(new ListItemDataBean(UtilBean.getMyLabel(LabelConstants.SERVICE_MIGRATED_OUT_FAMILY)));
        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            selectedVisitIndex = position;
            onClick(nextButton);
        };
        ListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, items, R.layout.listview_row_type, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
        hideProcessDialog();
    }

    @UiThread
    public void setVisits() {
        visits = new ArrayList<>();
        switch (selectedService) {
            case SERVICE_ELIGIBLE_COUPLES:
                visits.add(FormConstants.TECHO_FHW_RIM);
                visits.add(FormConstants.LMP_FOLLOW_UP);
                bodyLayoutContainer.removeAllViews();
                setVisitSelectionScreen(visits);
                break;

            case SERVICE_PREGNANT_WOMEN:
                visits.add(FormConstants.TECHO_FHW_ANC);
                visits.add(FormConstants.TECHO_FHW_WPD);
                bodyLayoutContainer.removeAllViews();
                setVisitSelectionScreen(visits);
                break;

            case SERVICE_PNC_MOTHERS:
                visits.add(FormConstants.TECHO_FHW_PNC);
                bodyLayoutContainer.removeAllViews();
                setVisitSelectionScreen(visits);
                break;

            case SERVICE_CHILDREN:
                visits.add(FormConstants.TECHO_FHW_CS);
                visits.add(FormConstants.TECHO_FHW_VAE);
                visits.add(FormConstants.TECHO_AWW_WEIGHT_GROWTH_GRAPH);
                bodyLayoutContainer.removeAllViews();
                setVisitSelectionScreen(visits);
                break;

            case SERVICE_ADOLESCENT:
                startDynamicFormActivity(FormConstants.ADOLESCENT_SCREENING, memberList.get(selectedPeopleIndex), null);
                break;
            default:
        }
        hideProcessDialog();
    }

    @UiThread
    public void addGraph() {

        List<PointValue> yAxisValues = new ArrayList<>();
        Line line = new Line(yAxisValues).setColor(Color.BLACK);

        Gson gson = new Gson();
        MemberAdditionalInfoDataBean additionalInfo = null;
        if (memberSelected.getAdditionalInfo() != null) {
            additionalInfo = gson.fromJson(memberSelected.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
        }

        if (additionalInfo != null && additionalInfo.getWeightMap() != null && !additionalInfo.getWeightMap().isEmpty()) {
            for (Map.Entry<Long, Float> entry : additionalInfo.getWeightMap().entrySet()) {
                int[] ageYearMonthDayArray = UtilBean.calculateAgeYearMonthDayOnGivenDate(memberSelected.getDob(), entry.getKey());
                yAxisValues.add(new PointValue(ageYearMonthDayArray[1], entry.getValue()));
            }
            footerLayout.setVisibility(View.VISIBLE);
        } else {
            View.OnClickListener listener = v -> alertDialog.dismiss();
            alertDialog = new MyAlertDialog(this,
                    LabelConstants.NO_DATA_FOUND_FOR_CHILD_GROWTH_CHART,
                    listener, DynamicUtils.BUTTON_OK);
            alertDialog.show();
            return;
        }

        screen = CHART_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        LinearLayout childGrowthChart = MyDynamicComponents.getChildGrowthChart(memberSelected.getGender().equals(GlobalTypes.MALE), this);
        LineChartView lineChartView = childGrowthChart.findViewById(R.id.lineChart);

        LineChartData data = lineChartView.getLineChartData();
        List<Line> lines = data.getLines();
        lines.set(0, line);

        bodyLayoutContainer.addView(childGrowthChart);
    }

    @UiThread
    public void addVillageSelectionSpinner() {
        setSubTitle(null);
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.SELECT_VILLAGE));
        String[] arrayOfOptions = new String[villageList.size()];
        int i = 0;
        for (LocationBean locationBean : villageList) {
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }

        villageSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 2);
        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bodyLayoutContainer.removeView(ashaAreaSpinner);
                ashaAreaList = fhsService.retrieveAshaAreaAssignedToUser(villageList.get(position).getActualID());
                addAshaAreaSelectionSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do something
            }
        });
        bodyLayoutContainer.addView(villageSpinner);
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.SELECT_ASHA_AREA));
        addAshaAreaSelectionSpinner();
    }

    @UiThread
    public void addAshaAreaSelectionSpinner() {
        String[] arrayOfOptions = new String[ashaAreaList.size() + 1];
        arrayOfOptions[0] = LabelConstants.ALL;
        int i = 1;
        for (LocationBean locationBean : ashaAreaList) {
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }
        ashaAreaSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 3);
        bodyLayoutContainer.addView(ashaAreaSpinner);
        hideProcessDialog();
    }

    @UiThread
    public void setServiceSelectionScreen() {
        screen = SERVICE_SELECTION_SCREEN;

        MaterialTextView textView = MyStaticComponents.getListTitleView(this, LabelConstants.SERVICE_TYPE_SELECTION);
        bodyLayoutContainer.addView(textView);

        List<ListItemDataBean> items = new ArrayList<>();
        items.add(new ListItemDataBean(UtilBean.getMyLabel(LabelConstants.SERVICE_ELIGIBLE_COUPLES)));
        items.add(new ListItemDataBean(UtilBean.getMyLabel(LabelConstants.SERVICE_PREGNANT_WOMEN)));
        items.add(new ListItemDataBean(UtilBean.getMyLabel(LabelConstants.SERVICE_PNC_WOMEN)));
        items.add(new ListItemDataBean(UtilBean.getMyLabel(LabelConstants.SERVICE_CHILDREN)));
        //items.add(new ListItemDataBean(UtilBean.getMyLabel(LabelConstants.SERVICE_TEMPORARY_REGISTRATION)));
        //items.add(new ListItemDataBean(UtilBean.getMyLabel(LabelConstants.SERVICE_UPDATE_MEMBER)));
        //items.add(new ListItemDataBean(UtilBean.getMyLabel(LabelConstants.SERVICE_ADD_MEMBER)));
        //items.add(new ListItemDataBean(UtilBean.getMyLabel(LabelConstants.SERVICE_MIGRATED_IN_MEMBERS)));
        //items.add(new ListItemDataBean(UtilBean.getMyLabel(LabelConstants.SERVICE_MIGRATED_OUT_MEMBERS)));
        items.add(new ListItemDataBean(UtilBean.getMyLabel(LabelConstants.SERVICE_RCH_REGISTER)));
        items.add(new ListItemDataBean(UtilBean.getMyLabel(LabelConstants.ADOLESCENT_SCREENING)));
        //items.add(new ListItemDataBean(UtilBean.getMyLabel(LabelConstants.SERVICE_GERIATRIC_MEMBERS)));
        //items.add(new ListItemDataBean(UtilBean.getMyLabel(LabelConstants.SERVICE_TRAVELLERS_SCREENING)));
        //items.add(new ListItemDataBean(UtilBean.getMyLabel(LabelConstants.MANAGE_FAMILY_MIGRATIONS)));

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            selectedServiceIndex = position;
            onClick(nextButton);
        };
        ListView listView = MyStaticComponents.getPaginatedListViewWithItem(this, items, R.layout.listview_row_type, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
        nextButton.setText(GlobalTypes.EVENT_NEXT);
        nextButton.setOnClickListener(this);
        footerLayout.setVisibility(View.GONE);
        hideProcessDialog();
    }

    @UiThread
    public void addSearchTextBox() {
        TextInputLayout searchBox;
        if (selectedService.equals(SERVICE_TEMP_REGISTRATION)
                || selectedService.equals(SERVICE_MIGRATED_IN_MEMBERS)
                || selectedService.equals(SERVICE_MIGRATED_OUT_MEMBERS)) {
            return;
        }

        if (selectedService.equals(SERVICE_ADD_NEW_MEMBER)) {
            searchBox = MyStaticComponents.getEditText(this, LabelConstants.FAMILY_ID_TO_SEARCH, 1, 15, 1);
        } else {
            searchBox = MyStaticComponents.getEditTextWithQrScan(this, LabelConstants.MEMBER_ID_OR_NAME_OR_HEALTH_ID_TO_SEARCH, 1, 15, 1);
        }
        bodyLayoutContainer.addView(searchBox);

        if (!selectedService.equals(SERVICE_ADD_NEW_MEMBER) && !selectedService.equals(SERVICE_UPDATE_MEMBER)) {
            bodyLayoutContainer.addView(MyStaticComponents.getOrTextView(context));
        }

        if (searchBox.getEditText() != null) {
            searchBox.getEditText().addTextChangedListener(new TextWatcher() {

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
                                            if (selectedService.equals(SERVICE_ADD_NEW_MEMBER)
                                                    || selectedService.equals(SERVICE_UPDATE_MEMBER)) {
                                                retrieveMemberListForUpdateBySearch(s, null);
                                            } else if (selectedService.equals(SERVICE_RCH_REGISTER)) {
                                                retrieveMemberListForRchRegister(s, null);
                                            } else {
                                                retrieveMemberListByServiceType(selectedService, s, true, null);
                                            }
                                        });
                                    } else if (s == null || s.length() == 0) {
                                        runOnUiThread(() -> {
                                            showProcessDialog();
                                            if (selectedService.equals(SERVICE_ADD_NEW_MEMBER)
                                                    || selectedService.equals(SERVICE_UPDATE_MEMBER)) {
                                                bodyLayoutContainer.removeView(pagingHeaderView);
                                                bodyLayoutContainer.removeView(noMemberAvailableView);
                                                bodyLayoutContainer.removeView(pagingListView);
                                                hideProcessDialog();
                                            } else if (selectedService.equals(SERVICE_RCH_REGISTER)) {
                                                retrieveMemberListForRchRegister(s, null);
                                            } else {
                                                retrieveMemberListByServiceType(selectedService, null, false, null);
                                            }
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
    public void retrieveMemberListByServiceType(String selectedService, CharSequence s, boolean isSearch, String qrData) {
        searchString = s;
        offset = 0;
        selectedPeopleIndex = -1;
        villageIds = new LinkedList<>();
        villageIds.add(selectedVillage);
        qrScanFilter = SewaUtil.setQrScanFilterData(qrData);
        switch (selectedService) {
            case SERVICE_ELIGIBLE_COUPLES:
                memberList = fhsService.retrieveEligibleCouplesByAshaArea(selectedAshaAreas, selectedVillage, s, limit, offset, qrScanFilter);
                break;
            case SERVICE_PREGNANT_WOMEN:
                memberList = fhsService.retrievePregnantWomenByAshaArea(selectedAshaAreas, false, villageIds, s, limit, offset, qrScanFilter);
                break;
            case SERVICE_PNC_MOTHERS:
                memberList = fhsService.retrievePncMothersByAshaArea(selectedAshaAreas, selectedVillage, s, limit, offset, qrScanFilter);
                break;
            case SERVICE_CHILDREN:
                memberList = fhsService.retrieveChildsBelow5YearsByAshaArea(selectedAshaAreas, false, villageIds, s, limit, offset, qrScanFilter);
                break;
            case SERVICE_ADOLESCENT:
                memberList = fhsService.retrieveAdolescentChildren(selectedAshaAreas, false, villageIds, s, limit, offset, qrScanFilter);
                break;
            default:
                break;
        }
        offset = offset + limit;
        setMemberSelectionScreen(selectedService, isSearch);
    }

    @UiThread
    public void setMemberSelectionScreen(String selectedService, boolean isSearch) {
        bodyLayoutContainer.removeView(pagingListView);
        bodyLayoutContainer.removeView(pagingHeaderView);
        bodyLayoutContainer.removeView(noMemberAvailableView);
        switch (selectedService) {
            case SERVICE_ELIGIBLE_COUPLES:
                addEligibleCoupleList();
                break;
            case SERVICE_PREGNANT_WOMEN:
                addPregnantWomenList();
                break;
            case SERVICE_PNC_MOTHERS:
                addPncMothersList();
                break;
            case SERVICE_CHILDREN:
                addChildsBelow5YearsList();
                break;
            case SERVICE_ADOLESCENT:
                adoloscentMemberList();
                break;
            default:
                break;
        }

        if (memberList == null || memberList.isEmpty()) {
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
            nextButton.setOnClickListener(v -> navigateToHomeScreen(false));
        } else {
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
            nextButton.setOnClickListener(this);
        }

        if (!isSearch) {
            showProcessDialog();
        }
    }

    @Override
    public void onLoadMoreItems() {
        switch (selectedService) {
            case SERVICE_ELIGIBLE_COUPLES:
                loadMoreEligibleCouples();
                break;
            case SERVICE_PREGNANT_WOMEN:
                loadMorePregnantWomenList();
                break;
            case SERVICE_PNC_MOTHERS:
                loadMorePNCMothersList();
                break;
            case SERVICE_CHILDREN:
                loadMoreChildrenList();
                break;
            case SERVICE_RCH_REGISTER:
                loadMoreRCH();
                break;
            case SERVICE_UPDATE_MEMBER:
                loadMoreMembers();
                break;
            case SERVICE_ADD_NEW_MEMBER:
                loadMoreFamilies();
                break;
            case SERVICE_MIGRATED_IN_MEMBERS:
            case SERVICE_MIGRATED_OUT_MEMBERS:
                loadMigratedMembers();
                break;
            case SERVICE_MIGRATED_IN_FAMILY:
            case SERVICE_MIGRATED_OUT_FAMILY:
                loadMigratedFamily();
                break;
            default:
                break;
        }
    }

    private List<ListItemDataBean> getMembersList(List<MemberDataBean> memberDataBeanList) {
        List<ListItemDataBean> list = new ArrayList<>();
        rbText = "";
        switch (selectedService) {
            case SERVICE_ELIGIBLE_COUPLES:
            case SERVICE_PREGNANT_WOMEN:
            case SERVICE_PNC_MOTHERS:
            case SERVICE_RCH_REGISTER:
            case SERVICE_UPDATE_MEMBER:
            case SERVICE_ADOLESCENT:
                for (MemberDataBean memberDataBean : memberDataBeanList) {
                    list.add(new ListItemDataBean(memberDataBean.getFamilyId(), memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean), null, null));
                }
                break;
            case SERVICE_CHILDREN:
                removedChildren = new ArrayList<>();
                for (MemberDataBean memberDataBean : memberDataBeanList) {
                    if (memberDataBean.getUniqueHealthId() != null) {
                        MemberBean mother = null;
                        if (memberDataBean.getMotherId() != null) {
                            try {
                                mother = memberBeanDao.queryBuilder().where().eq("actualId", memberDataBean.getMotherId()).queryForFirst();
                            } catch (SQLException e) {
                                Log.e(getClass().getName(), null, e);
                            }
                        }
                        if (mother == null) {
                            rbText = UtilBean.getMyLabel(LabelConstants.NOT_AVAILABLE);
                        } else {
                            rbText = UtilBean.getMemberFullName(mother);
                        }
                        list.add(new ListItemDataBean(memberDataBean.getFamilyId(), memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean), LabelConstants.MOTHER, rbText));
                    } else {
                        removedChildren.add(memberDataBean);
                    }
                }
                break;
            default:
                break;
        }
        return list;
    }

    private List<ListItemDataBean> getFamilyList(List<FamilyDataBean> familyDataBeans) {
        List<String> familyIds = new ArrayList<>();
        List<ListItemDataBean> list = new ArrayList<>();
        String text;
        for (FamilyDataBean familyDataBean : familyDataBeans) {
            familyIds.add(familyDataBean.getFamilyId());
        }
        Map<String, MemberDataBean> headMembersMapWithFamilyIdAsKey = fhsService.retrieveHeadMemberDataBeansByFamilyId(familyIds);

        for (FamilyDataBean familyDataBean : familyDataBeans) {
            MemberDataBean headMember = headMembersMapWithFamilyIdAsKey.get(familyDataBean.getFamilyId());
            if (headMember != null) {
                familyDataBean.setHeadMemberName(headMember.getFirstName() + " " + headMember.getMiddleName() + " " + headMember.getGrandfatherName() + " " + headMember.getLastName());
                familyDataBean.setHeadMemberName(familyDataBean.getHeadMemberName().replace(" " + LabelConstants.NULL, ""));
                text = headMember.getFirstName() + " " + headMember.getMiddleName() + " " + headMember.getGrandfatherName() + " " + headMember.getLastName();
                text = text.replace(" " + LabelConstants.NULL, "");
            } else {
                text = UtilBean.getMyLabel(LabelConstants.HEAD_NAME_NOT_AVAILABLE);
            }
            if (FhsConstants.CFHC_VERIFIED_FAMILY_STATES.contains(familyDataBean.getState())) {
                list.add(new ListItemDataBean(null, familyDataBean.getFamilyId(), null, null, text, true));
            } else {
                list.add(new ListItemDataBean(null, familyDataBean.getFamilyId(), null, null, text, false));
            }
        }
        return list;
    }

    private List<ListItemDataBean> getMigratedMembersList(List<MigratedMembersBean> migratedMembersBeans) {
        List<ListItemDataBean> list = new ArrayList<>();
        rbText = "";
        for (MigratedMembersBean migratedMembersBean : migratedMembersBeans) {
            if (migratedMembersBean.getLfu() != null && migratedMembersBean.getLfu()) {
                rbText = UtilBean.getMyLabel(LabelConstants.LOST_TO_FOLLOW_UP);
            } else if (migratedMembersBean.getOutOfState() != null && migratedMembersBean.getOutOfState()) {
                rbText = UtilBean.getMyLabel(LabelConstants.OUT_OF_STATE_TAG);
            } else if (migratedMembersBean.getConfirmed() != null && !migratedMembersBean.getConfirmed()) {
                rbText = UtilBean.getMyLabel(LabelConstants.CONFIRMATION_PENDING);
            } else {
                rbText = UtilBean.getMyLabel(LabelConstants.CONFIRMED);
            }

            list.add(new ListItemDataBean(null, migratedMembersBean.getHealthId(), migratedMembersBean.getName(), rbText, null));
        }
        return list;
    }

    private List<ListItemDataBean> getMigratedFamilyList(List<MigratedFamilyBean> migratedFamilyBeans) {
        List<ListItemDataBean> list = new ArrayList<>();
        rbText = "";
        for (MigratedFamilyBean migratedFamilyBean : migratedFamilyBeans) {
            if (migratedFamilyBean.getLfu() != null && migratedFamilyBean.getLfu()) {
                rbText = UtilBean.getMyLabel(LabelConstants.LOST_TO_FOLLOW_UP);
            } else if (migratedFamilyBean.getOutOfState() != null && migratedFamilyBean.getOutOfState()) {
                rbText = UtilBean.getMyLabel(LabelConstants.OUT_OF_STATE_TAG);
            } else if (migratedFamilyBean.getConfirmed() != null && !migratedFamilyBean.getConfirmed()) {
                rbText = UtilBean.getMyLabel(LabelConstants.CONFIRMATION_PENDING);
            } else {
                rbText = UtilBean.getMyLabel(LabelConstants.CONFIRMED);
            }

            list.add(new ListItemDataBean(null, LabelConstants.FAMILY_ID, migratedFamilyBean.getFamilyIdString(), rbText, null));
        }
        return list;
    }

    @Background
    public void loadMoreEligibleCouples() {
        List<MemberDataBean> memberDataBeanList = fhsService.retrieveEligibleCouplesByAshaArea(selectedAshaAreas, selectedVillage, searchString, limit, offset, qrScanFilter);
        offset = offset + limit;
        onLoadMoreUiMember(memberDataBeanList);
    }

    @Background
    public void loadMorePregnantWomenList() {
        List<MemberDataBean> memberDataBeanList = fhsService.retrievePregnantWomenByAshaArea(selectedAshaAreas, false, villageIds, searchString, limit, offset, qrScanFilter);
        offset = offset + limit;
        onLoadMoreUiMember(memberDataBeanList);
    }

    @Background
    public void loadMorePNCMothersList() {
        List<MemberDataBean> memberDataBeanList = fhsService.retrievePncMothersByAshaArea(selectedAshaAreas, selectedVillage, searchString, limit, offset, qrScanFilter);
        offset = offset + limit;
        onLoadMoreUiMember(memberDataBeanList);
    }

    @Background
    public void loadMoreRCH() {
        List<MemberDataBean> memberDataBeanList = fhsService.retrieveMemberListForRchRegister(selectedAshaAreas, selectedVillage, searchString, limit, offset, qrScanFilter);
        offset = offset + limit;

        onLoadMoreUiMember(memberDataBeanList);
    }

    @Background
    public void loadMoreMembers() {
        List<MemberDataBean> memberDataBeanList = fhsService.retrieveMembersByAshaArea(selectedAshaAreas, selectedVillage, searchString, limit, offset, qrScanFilter);
        offset = offset + limit;
        onLoadMoreUiMember(memberDataBeanList);
    }

    @UiThread
    public void onLoadMoreUiMember(List<MemberDataBean> memberDataBeanList) {
        if (memberDataBeanList != null && !memberDataBeanList.isEmpty()) {
            List<ListItemDataBean> membersList = getMembersList(memberDataBeanList);
            memberList.addAll(memberDataBeanList);
            pagingListView.onFinishLoadingWithItem(true, membersList);
        } else {
            pagingListView.onFinishLoadingWithItem(false, null);
        }
    }

    @Background
    public void loadMoreChildrenList() {
        List<MemberDataBean> memberDataBeanList = fhsService.retrieveChildsBelow5YearsByAshaArea(selectedAshaAreas, false, villageIds, searchString, limit, offset, qrScanFilter);
        offset = offset + limit;
        onLoadMoreUiChildren(memberDataBeanList);
    }

    @UiThread
    public void onLoadMoreUiChildren(List<MemberDataBean> memberDataBeanList) {
        if (memberDataBeanList != null && !memberDataBeanList.isEmpty()) {
            List<ListItemDataBean> membersList = getMembersList(memberDataBeanList);
            memberList.addAll(memberDataBeanList);
            memberList.removeAll(removedChildren);
            pagingListView.onFinishLoadingWithItem(true, membersList);
        } else {
            pagingListView.onFinishLoadingWithItem(false, null);
        }
    }

    @Background
    public void loadMoreFamilies() {
        List<FamilyDataBean> familyDataBeanList = fhsService.retrieveFamilyDataBeansByAshaArea(selectedAshaAreas, selectedVillage, searchString, limit, offset);
        offset = offset + limit;
        onLoadMoreUiFamilies(familyDataBeanList);
    }

    @UiThread
    public void onLoadMoreUiFamilies(List<FamilyDataBean> familyDataBeanList) {
        if (familyDataBeanList != null && !familyDataBeanList.isEmpty()) {
            List<ListItemDataBean> familiesList = getFamilyList(familyDataBeanList);
            familyList.addAll(familyDataBeanList);
            pagingListView.onFinishLoadingWithItem(true, familiesList);
        } else {
            pagingListView.onFinishLoadingWithItem(false, null);
        }
    }

    @Background
    public void loadMigratedMembers() {
        List<MigratedMembersBean> migratedMembersBeanList =
                migrationService.retrieveMigrationDetailsForMigratedMembers(selectedVillage, selectedAshaAreas, isMigratedOut, limit, offset);
        offset = offset + limit;
        onLoadMoreUiMigratedMembers(migratedMembersBeanList);
    }

    @UiThread
    public void onLoadMoreUiMigratedMembers(List<MigratedMembersBean> migratedMembersBeans) {
        if (migratedMembersBeans != null && !migratedMembersBeans.isEmpty()) {
            List<ListItemDataBean> membersList = getMigratedMembersList(migratedMembersBeans);
            this.migratedMembersBeans.addAll(migratedMembersBeans);
            pagingListView.onFinishLoadingWithItem(true, membersList);
        } else {
            pagingListView.onFinishLoadingWithItem(false, null);
        }
    }

    @Background
    public void loadMigratedFamily() {
        List<MigratedFamilyBean> migratedFamilyBeanList =
                migrationService.retrieveMigrationDetailsForMigratedFamily(selectedVillage, selectedAshaAreas, isMigratedOut, limit, offset);
        offset = offset + limit;
        onLoadMoreUiMigratedFamily(migratedFamilyBeanList);
    }

    @UiThread
    public void onLoadMoreUiMigratedFamily(List<MigratedFamilyBean> migratedFamilyBeans) {
        if (migratedFamilyBeans != null && !migratedFamilyBeans.isEmpty()) {
            List<ListItemDataBean> familyList = getMigratedFamilyList(migratedFamilyBeans);
            this.migratedFamilyBeans.addAll(migratedFamilyBeans);
            pagingListView.onFinishLoadingWithItem(true, familyList);
        } else {
            pagingListView.onFinishLoadingWithItem(false, null);
        }
    }

    @UiThread
    public void addEligibleCoupleList() {
        if (memberList != null && !memberList.isEmpty()) {
            pagingHeaderView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT + " an " + LabelConstants.ELIGIBLE_COUPLE);
            bodyLayoutContainer.addView(pagingHeaderView);

            List<ListItemDataBean> membersList = getMembersList(memberList);

            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedPeopleIndex = position;

            pagingListView = MyStaticComponents.getPaginatedListViewWithItem(context, membersList, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(pagingListView);
        } else {
            bodyLayoutContainer.removeView(pagingHeaderView);
            noMemberAvailableView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_ELIGIBLE_COUPLE_IN_AREA);
            bodyLayoutContainer.addView(noMemberAvailableView);
        }
        hideProcessDialog();
    }

    @UiThread
    public void addPregnantWomenList() {
        if (memberList != null && !memberList.isEmpty()) {
            pagingHeaderView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT + " a " + LabelConstants.SERVICE_PREGNANT_WOMEN);
            bodyLayoutContainer.addView(pagingHeaderView);
            List<ListItemDataBean> membersList = getMembersList(memberList);

            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedPeopleIndex = position;

            pagingListView = MyStaticComponents.getPaginatedListViewWithItem(context, membersList, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(pagingListView);
        } else {
            bodyLayoutContainer.removeView(pagingHeaderView);
            noMemberAvailableView = MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.NO_PREGNANT_WOMEN_IN_AREA);
            bodyLayoutContainer.addView(noMemberAvailableView);
        }
        hideProcessDialog();
    }

    @UiThread
    public void addPncMothersList() {
        if (memberList != null && !memberList.isEmpty()) {
            pagingHeaderView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT + " a " + LabelConstants.PNC_MOTHER);
            bodyLayoutContainer.addView(pagingHeaderView);
            List<ListItemDataBean> membersList = getMembersList(memberList);

            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedPeopleIndex = position;
            pagingListView = MyStaticComponents.getPaginatedListViewWithItem(context, membersList, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(pagingListView);
        } else {
            bodyLayoutContainer.removeView(pagingHeaderView);
            noMemberAvailableView = MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.NO_PNC_WOMEN_IN_AREA);
            bodyLayoutContainer.addView(noMemberAvailableView);
        }
        hideProcessDialog();
    }

    @UiThread
    public void addChildsBelow5YearsList() {
        if (memberList != null && !memberList.isEmpty()) {
            pagingHeaderView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_CHILD);
            bodyLayoutContainer.addView(pagingHeaderView);

            List<ListItemDataBean> membersList = getMembersList(memberList);
            memberList.removeAll(removedChildren);

            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedPeopleIndex = position;
            pagingListView = MyStaticComponents.getPaginatedListViewWithItem(context, membersList, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(pagingListView);
        } else {
            bodyLayoutContainer.removeView(pagingHeaderView);
            noMemberAvailableView = MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.NO_CHILD_MEMBER_IN_AREA);
            bodyLayoutContainer.addView(noMemberAvailableView);
        }
        hideProcessDialog();
    }

    @UiThread
    public void adoloscentMemberList() {
        if (memberList != null && !memberList.isEmpty()) {
            pagingHeaderView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_ADOLESCENT_MEMBER);
            bodyLayoutContainer.addView(pagingHeaderView);

            List<ListItemDataBean> membersList = getMembersList(memberList);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedPeopleIndex = position;
            pagingListView = MyStaticComponents.getPaginatedListViewWithItem(context, membersList, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(pagingListView);
        } else {
            bodyLayoutContainer.removeView(pagingHeaderView);
            noMemberAvailableView = MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.NO_ADOLESCENT_MEMBER_FOUND);
            bodyLayoutContainer.addView(noMemberAvailableView);
        }
        hideProcessDialog();
    }

    @Background
    public void retrieveMemberListForUpdateBySearch(CharSequence s, String qrData) {
        searchString = s;
        qrScanFilter = SewaUtil.setQrScanFilterData(qrData);
        selectedPeopleIndex = -1;
        selectedFamilyIndex = -1;
        if (selectedService.equals(SERVICE_UPDATE_MEMBER)) {
            offset = 0;
            memberList = fhsService.retrieveMembersByAshaArea(selectedAshaAreas, selectedVillage, s, limit, offset, qrScanFilter);
            offset = offset + limit;
            addMemberListScreenForUpdateInfo();
        }
        if (selectedService.equals(SERVICE_ADD_NEW_MEMBER)) {
            offset = 0;
            familyList = fhsService.retrieveFamilyDataBeansByAshaArea(selectedAshaAreas, selectedVillage, s, limit, offset);
            offset = offset + limit;
            addFamilyListScreenForNewMember();
        }
    }

    @UiThread
    public void addMemberListScreenForUpdateInfo() {
        bodyLayoutContainer.removeView(noMemberAvailableView);
        bodyLayoutContainer.removeView(pagingHeaderView);
        bodyLayoutContainer.removeView(pagingListView);
        selectedMemberToUpdateIndex = -1;

        if (memberList != null && !memberList.isEmpty()) {
            pagingHeaderView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_MEMBER);
            pagingHeaderView.setPadding(0, 50, 0, 0);
            bodyLayoutContainer.addView(pagingHeaderView);
            List<ListItemDataBean> membersList = getMembersList(memberList);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedMemberToUpdateIndex = position;

            pagingListView = MyStaticComponents.getPaginatedListViewWithItem(context, membersList, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(pagingListView);
        } else {
            bodyLayoutContainer.removeView(pagingHeaderView);
            noMemberAvailableView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBERS_FOUND);
            bodyLayoutContainer.addView(noMemberAvailableView);
        }
    }

    @UiThread
    public void addFamilyListScreenForNewMember() {
        selectedFamilyIndex = -1;
        bodyLayoutContainer.removeView(pagingHeaderView);
        bodyLayoutContainer.removeView(noMemberAvailableView);
        bodyLayoutContainer.removeView(pagingListView);
        if (!familyList.isEmpty()) {
            pagingHeaderView = MyStaticComponents.getListTitleView(this, LabelConstants.SERVICE_ADD_MEMBER);
            pagingHeaderView.setPadding(0, 50, 0, 0);
            bodyLayoutContainer.addView(pagingHeaderView);

            List<ListItemDataBean> famListItemDataBeans = getFamilyList(familyList);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedFamilyIndex = position;
            pagingListView = MyStaticComponents.getPaginatedListViewWithItem(context, famListItemDataBeans, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(pagingListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            noMemberAvailableView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_FAMILIES_FOUND);
            bodyLayoutContainer.addView(noMemberAvailableView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }
    }

    @UiThread
    public void setVisitSelectionScreen(List<String> visits) {
        screen = VISIT_SELECTION_SCREEN;
        setSubTitle(UtilBean.getMemberFullName(memberSelected));
        List<ListItemDataBean> items = new ArrayList<>();
        for (String visit : visits) {
            items.add(new ListItemDataBean(UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(visit))));
        }
        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            selectedVisitIndex = position;
            onClick(nextButton);
        };
        ListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, items, R.layout.listview_row_type, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
        footerLayout.setVisibility(View.GONE);
        hideProcessDialog();
    }

    @Background
    public void retrieveMigratedFamilyList(Boolean isOut) {
        isMigratedOut = isOut;
        offset = 0;
        selectedPeopleIndex = -1;
        migratedFamilyBeans = migrationService.retrieveMigrationDetailsForMigratedFamily(selectedVillage, selectedAshaAreas, isOut, limit, offset);
        offset = offset + limit;
        setMigratedFamilyScreen(isOut);
    }

    @UiThread
    public void setMigratedFamilyScreen(final boolean isOut) {
        if (selectedService.equals(SERVICE_MIGRATED_IN_FAMILY)) {
            setTitle(LabelConstants.SERVICE_MIGRATED_IN_FAMILY);
        } else if (selectedService.equals(SERVICE_MIGRATED_OUT_FAMILY)) {
            setTitle(LabelConstants.SERVICE_MIGRATED_OUT_FAMILY);
        }
        bodyLayoutContainer.removeAllViews();
        screen = MIGRATED_FAMILY_SCREEN;
        nextButton.setText(GlobalTypes.EVENT_OKAY);

        pagingHeaderView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_FAMILY);
        pagingHeaderView.setPadding(0, 0, 0, 20);
        bodyLayoutContainer.addView(pagingHeaderView);

        if (migratedFamilyBeans != null && !migratedFamilyBeans.isEmpty()) {
            List<ListItemDataBean> migratedMembersList = getMigratedFamilyList(migratedFamilyBeans);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedPeopleIndex = position;
            pagingListView = MyStaticComponents.getPaginatedListViewWithItem(context, migratedMembersList, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(pagingListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            bodyLayoutContainer.removeView(pagingHeaderView);
            noMemberAvailableView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MIGRATED_FAMILY_FOUND);
            bodyLayoutContainer.addView(noMemberAvailableView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }

        hideProcessDialog();
    }

    @Background
    public void retrieveMigratedMembersList(Boolean isOut) {
        isMigratedOut = isOut;
        offset = 0;
        selectedPeopleIndex = -1;
        migratedMembersBeans = migrationService.retrieveMigrationDetailsForMigratedMembers(selectedVillage, selectedAshaAreas, isOut, limit, offset);
        offset = offset + limit;
        setMigratedMembersScreen(isOut);
    }

    @UiThread
    public void setMigratedMembersScreen(final boolean isOut) {
        bodyLayoutContainer.removeAllViews();
        screen = MIGRATED_MEMBERS_SCREEN;
        nextButton.setText(GlobalTypes.EVENT_OKAY);

        if (isOut) {
            pagingHeaderView = MyStaticComponents.getListTitleView(this, LabelConstants.SERVICE_MIGRATED_OUT_MEMBERS);
        } else {
            pagingHeaderView = MyStaticComponents.getListTitleView(this, LabelConstants.SERVICE_MIGRATED_IN_MEMBERS);
        }
        pagingHeaderView.setPadding(0, 0, 0, 20);
        bodyLayoutContainer.addView(pagingHeaderView);

        if (migratedMembersBeans != null && !migratedMembersBeans.isEmpty()) {
            List<ListItemDataBean> migratedMembersList = getMigratedMembersList(migratedMembersBeans);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedPeopleIndex = position;
            pagingListView = MyStaticComponents.getPaginatedListViewWithItem(context, migratedMembersList, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(pagingListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            bodyLayoutContainer.removeView(pagingHeaderView);
            noMemberAvailableView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MIGRATED_MEMBER_FOUND);
            bodyLayoutContainer.addView(noMemberAvailableView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }

        hideProcessDialog();
    }

    @Background
    public void retrieveMemberListForRchRegister(CharSequence search, String qrData) {
        offset = 0;
        selectedPeopleIndex = -1;
        searchString = search;
        qrScanFilter = SewaUtil.setQrScanFilterData(qrData);
        memberList = fhsService.retrieveMemberListForRchRegister(selectedAshaAreas, selectedVillage, search, limit, offset, qrScanFilter);
        offset = offset + limit;
        setMemberScreenForRchRegister();
    }

    @UiThread
    public void setMemberScreenForRchRegister() {
        bodyLayoutContainer.removeView(pagingHeaderView);
        bodyLayoutContainer.removeView(noMemberAvailableView);
        bodyLayoutContainer.removeView(pagingListView);
        screen = RCH_REGISTER_MEMBER_SCREEN;

        pagingHeaderView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_MEMBER);
        bodyLayoutContainer.addView(pagingHeaderView);

        if (memberList != null && !memberList.isEmpty()) {
            List<ListItemDataBean> membersList = getMembersList(memberList);

            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedPeopleIndex = position;
            pagingListView = MyStaticComponents.getPaginatedListViewWithItem(context, membersList, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(pagingListView);
        } else {
            bodyLayoutContainer.removeView(pagingHeaderView);
            noMemberAvailableView = MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.NO_ELIGIBLE_MEMBER_FOUND_FOR_RCH);
            bodyLayoutContainer.addView(noMemberAvailableView);
        }
        hideProcessDialog();
    }

    @UiThread
    public void showNotOnlineMessage() {
        if (!sewaService.isOnline()) {
            hideProcessDialog();
            View.OnClickListener myListener = v -> {
                myAlertDialog.dismiss();
                showProcessDialog();
                bodyLayoutContainer.removeAllViews();
                memberList.clear();
                familyList.clear();
                setServiceSelectionScreen();
                nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                nextButton.setOnClickListener(MyPeopleActivity.this);
            };
            myAlertDialog = new MyAlertDialog(this, false,
                    UtilBean.getMyLabel(LabelConstants.NETWORK_CONNECTIVITY_ALERT),
                    myListener, DynamicUtils.BUTTON_OK);
            myAlertDialog.show();
            myAlertDialog.setOnCancelListener(dialog -> {
                myAlertDialog.dismiss();
                showProcessDialog();
                bodyLayoutContainer.removeAllViews();
                memberList.clear();
                familyList.clear();
                setServiceSelectionScreen();
                nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                nextButton.setOnClickListener(MyPeopleActivity.this);
            });
        }
    }

    @Background
    public void retrieveRCHServicesProvidedToMember() {
        screen = RCH_REGISTER_TABLE_SCREEN;
        setSubTitle(UtilBean.getMemberFullName(memberSelected));
        LinkedHashMap<String, List<LinkedHashMap<String, Object>>> resultMap = new LinkedHashMap<>();

        Boolean isChild = null;
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -5);
            Date before5Years = cal.getTime();
            cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -15);
            Date before15Years = cal.getTime();
            cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -49);
            Date before49Years = cal.getTime();

            LinkedHashMap<String, QueryMobDataBean> request = new LinkedHashMap<>();
            LinkedHashMap<String, Object> parameter = new LinkedHashMap<>();
            parameter.put(RelatedPropertyNameConstants.MEMBER_ID, memberSelected.getId());

            if (new Date(memberSelected.getDob()).after(before5Years)) {
                // Member is a child
                request.put("Child Service", new QueryMobDataBean("mob_child_services_provided", null, parameter, 0));
                isChild = true;
            }

            if (new Date(memberSelected.getDob()).before(before15Years)
                    && new Date(memberSelected.getDob()).after(before49Years)
                    && memberSelected.getGender().equalsIgnoreCase(GlobalTypes.FEMALE)
                    && memberSelected.getMaritalStatus().equalsIgnoreCase("629")) {
                // Member is an Eligible couple
                request.put("LMP Follow Up Service", new QueryMobDataBean("mob_lmp_services_provided", null, parameter, 0));
                request.put("ANC Service", new QueryMobDataBean("mob_anc_services_provided", null, parameter, 0));
                request.put("WPD Service", new QueryMobDataBean("mob_wpd_services_provided", null, parameter, 0));
                request.put("PNC Service", new QueryMobDataBean("mob_pnc_services_provided", null, parameter, 0));
                isChild = false;
            }

            for (Map.Entry<String, QueryMobDataBean> entry : request.entrySet()) {
                QueryMobDataBean response = restClient.executeQuery(entry.getValue());
                if (response != null) {
                    List<LinkedHashMap<String, Object>> result = response.getResult();
                    if (result != null && !result.isEmpty()) {
                        resultMap.put(entry.getKey(), result);
                    }
                }
            }
        } catch (RestHttpException e) {
            Log.e(getClass().getName(), null, e);
        }
        setServicesProvidedScreenForRchRegister(resultMap, isChild);
    }

    @UiThread
    public void setServicesProvidedScreenForRchRegister(Map<String, List<LinkedHashMap<String, Object>>> resultMap, Boolean isChild) {
        if (isChild != null) {
            if (Boolean.TRUE.equals(isChild)) {
                setMemberDetailsForChildRchRegister();
            } else {
                setMemberDetailsForEligibleCouplesRchRegister();
                if (memberSelected.getIsPregnantFlag() != null && memberSelected.getIsPregnantFlag()) {
                    setMemberDetailsForPregnantWomenRchRegister();
                }
                addPreviousPregnancyDetailsRchRegister();
            }
        }
        if (resultMap != null && !resultMap.isEmpty()) {
            for (Map.Entry<String, List<LinkedHashMap<String, Object>>> entry : resultMap.entrySet()) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, entry.getKey()));

                Set<String> headers = entry.getValue().get(0).keySet();
                List<String> headNames = new ArrayList<>();
                for (String head : headers) {
                    if (!head.startsWith("hidden")) {
                        headNames.add(head);
                    }
                }
                createTableLayout(headNames, entry.getValue());
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this,
                    UtilBean.getMyLabel(LabelConstants.NO_SERVICE_PROVIDED_TO_MEMBER)));
            nextButton.setText(GlobalTypes.MAIN_MENU);
        }
        hideProcessDialog();
    }

    private void setMemberDetailsForEligibleCouplesRchRegister() {
        FamilyDataBean familyDataBean = fhsService.retrieveFamilyDataBeanByFamilyId(memberSelected.getFamilyId());

        bodyLayoutContainer.addView(MyStaticComponents.generateTitleView(this, UtilBean.getMyLabel(LabelConstants.GENERAL_INFORMATION)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.TECHO_ID));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, memberSelected.getUniqueHealthId()));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.FAMILY_ID));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, memberSelected.getFamilyId()));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.MEMBER_NAME));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMemberFullName(memberSelected)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.HUSBAND_NAME));
        if (memberSelected.getHusbandId() != null) {
            MemberBean memberBean = fhsService.retrieveMemberBeanByActualId(memberSelected.getHusbandId());
            if (memberBean != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMemberFullName(memberBean)));
            } else {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(GlobalTypes.NOT_AVAILABLE)));
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(GlobalTypes.NOT_AVAILABLE)));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.MARRIAGE_YEAR));
        if (memberSelected.getYearOfWedding() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, memberSelected.getYearOfWedding().toString()));
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(GlobalTypes.NOT_AVAILABLE)));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.AGE_WITH_DOB));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.getAgeDisplayOnGivenDate(new Date(memberSelected.getDob()), new Date()) + " (" + sdf.format(memberSelected.getDob()) + ")"));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.MOBILE_NUMBER));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.getMyLabel(UtilBean.getNotAvailableIfNull(memberSelected.getMobileNumber()))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.BLOOD_GROUP));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.getMyLabel(UtilBean.getNotAvailableIfNull(memberSelected.getBloodGroup()))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.ADDRESS));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getFamilyFullAddress(familyDataBean)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.RELIGION_WITH_CASTE));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.getMyLabel(fhsService.getValueOfListValuesById(familyDataBean.getReligion()))
                        + " / "
                        + UtilBean.getMyLabel(fhsService.getValueOfListValuesById(familyDataBean.getCaste()))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.BPL_OR_APL));
        if (familyDataBean.getBplFlag() != null) {
            if (Boolean.TRUE.equals(familyDataBean.getBplFlag())) {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(LabelConstants.BPL)));
            } else {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(LabelConstants.APL)));
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(GlobalTypes.NOT_AVAILABLE)));
        }
    }

    private void setMemberDetailsForPregnantWomenRchRegister() {
        if (memberSelected.getCurPregRegDate() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.DATE_OF_REGISTRATION));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, sdf.format(memberSelected.getCurPregRegDate())));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.LMP));
        if (memberSelected.getLmpDate() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, sdf.format(memberSelected.getLmpDate())));
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(GlobalTypes.NOT_AVAILABLE)));
        }

        Integer weeks = null;
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.PREGNANCY_WEEK_NUMBER_AT_TIME_OF_REGISTRATION));
        if (memberSelected.getLmpDate() != null && memberSelected.getCurPregRegDate() != null) {
            weeks = UtilBean.getNumberOfWeeks(new Date(memberSelected.getLmpDate()), new Date(memberSelected.getCurPregRegDate()));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, weeks.toString()));
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(GlobalTypes.NOT_AVAILABLE)));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.REGISTERED_WITHIN_12_WEEKS_OF_PREGNANCY));
        if (weeks != null) {
            if (weeks < 12) {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(LabelConstants.YES)));
            } else {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(LabelConstants.NO)));
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(GlobalTypes.NOT_AVAILABLE)));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.EXPECTED_DELIVERY_DATE));
        if (memberSelected.getEdd() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, sdf.format(memberSelected.getEdd())));
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(GlobalTypes.NOT_AVAILABLE)));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.PREVIOUS_PREGNANCY_COMPLICATION));
        if (memberSelected.getPreviousPregnancyComplication() != null) {
            String[] split = memberSelected.getPreviousPregnancyComplication().split(",");
            for (String str : split) {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(str)));
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(GlobalTypes.NOT_AVAILABLE)));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.JSY_BENEFICIARY));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.getMyLabel(UtilBean.returnYesNoNotAvailableFromBoolean(memberSelected.getJsyBeneficiary()))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.KPSY_BENEFICIARY));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.getMyLabel(UtilBean.returnYesNoNotAvailableFromBoolean(memberSelected.getKpsyBeneficiary()))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.IAY_BENEFICIARY));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.getMyLabel(UtilBean.returnYesNoNotAvailableFromBoolean(memberSelected.getIayBeneficiary()))));
    }

    private void addPreviousPregnancyDetailsRchRegister() {
        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(this, UtilBean.getMyLabel(LabelConstants.PREVIOUS_PREGNANCY_DETAILS)));

        String totalMaleChildren = null;
        String totalFemaleChildren = null;
        String totalLivingMaleChildren = null;
        String totalLivingFemaleChildren = null;
        MemberBean latestChild = null;

        List<String> invalidStates = new ArrayList<>();
        invalidStates.addAll(FhsConstants.FHS_ARCHIVED_CRITERIA_MEMBER_STATES);
        invalidStates.addAll(FhsConstants.FHS_DEAD_CRITERIA_MEMBER_STATES);

        try {
            totalMaleChildren = String.valueOf(memberBeanDao.queryBuilder().where()
                    .eq(FieldNameConstants.MOTHER_ID, memberSelected.getId())
                    .and().eq(FieldNameConstants.GENDER, GlobalTypes.MALE)
                    .and().notIn(FieldNameConstants.STATE, FhsConstants.FHS_ARCHIVED_CRITERIA_MEMBER_STATES).countOf());
            totalFemaleChildren = String.valueOf(memberBeanDao.queryBuilder().where()
                    .eq(FieldNameConstants.MOTHER_ID, memberSelected.getId())
                    .and().eq(FieldNameConstants.GENDER, GlobalTypes.FEMALE)
                    .and().notIn(FieldNameConstants.STATE, FhsConstants.FHS_ARCHIVED_CRITERIA_MEMBER_STATES).countOf());
            totalLivingMaleChildren = String.valueOf(memberBeanDao.queryBuilder().where()
                    .eq(FieldNameConstants.MOTHER_ID, memberSelected.getId())
                    .and().eq(FieldNameConstants.GENDER, GlobalTypes.MALE)
                    .and().notIn(FieldNameConstants.STATE, invalidStates).countOf());
            totalLivingFemaleChildren = String.valueOf(memberBeanDao.queryBuilder().where()
                    .eq(FieldNameConstants.MOTHER_ID, memberSelected.getId())
                    .and().eq(FieldNameConstants.GENDER, GlobalTypes.FEMALE)
                    .and().notIn(FieldNameConstants.STATE, invalidStates).countOf());

            latestChild = memberBeanDao.queryBuilder().orderBy(FieldNameConstants.DOB, false)
                    .where().eq(FieldNameConstants.MOTHER_ID, memberSelected.getId())
                    .and().notIn(FieldNameConstants.STATE, invalidStates).queryForFirst();
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.TOTAL_CHILD_BIRTH));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.getMyLabel(LabelConstants.MALE) + ": " + UtilBean.getNotAvailableIfNull(totalMaleChildren)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.getMyLabel(LabelConstants.FEMALE) + ": " + UtilBean.getNotAvailableIfNull(totalFemaleChildren)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.LIVE_CHILD_COUNT));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.getMyLabel(LabelConstants.MALE) + ": " + UtilBean.getNotAvailableIfNull(totalLivingMaleChildren)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.getMyLabel(LabelConstants.FEMALE) + ": " + UtilBean.getNotAvailableIfNull(totalLivingFemaleChildren)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.YOUNGEST_CHILD_INFORMATION));
        if (latestChild != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.getMyLabel(LabelConstants.AGE) + ": " + UtilBean.getAgeDisplayOnGivenDate(latestChild.getDob(), new Date())));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.getMyLabel(LabelConstants.GENDER) + ": " + FullFormConstants.getFullFormOfGender(latestChild.getGender())));
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(LabelConstants.NOT_AVAILABLE)));
        }
    }

    private void setMemberDetailsForChildRchRegister() {
        FamilyDataBean familyDataBean = fhsService.retrieveFamilyDataBeanByFamilyId(memberSelected.getFamilyId());

        bodyLayoutContainer.addView(MyStaticComponents.generateTitleView(this, UtilBean.getMyLabel(LabelConstants.GENERAL_INFORMATION)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.TECHO_ID));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, memberSelected.getUniqueHealthId()));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.FAMILY_ID));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, memberSelected.getFamilyId()));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.CHILD_NAME));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMemberFullName(memberSelected)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.MOTHER_NAME));
        MemberBean mother = null;
        if (memberSelected.getMotherId() != null) {
            mother = fhsService.retrieveMemberBeanByActualId(memberSelected.getMotherId());
        }
        if (mother != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.getMemberFullName(mother) + " (" + mother.getUniqueHealthId() + ")"));
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(LabelConstants.NOT_AVAILABLE)));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.GENDER));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, FullFormConstants.getFullFormOfGender(memberSelected.getGender())));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.AGE_WITH_DOB));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.getAgeDisplayOnGivenDate(new Date(memberSelected.getDob()), new Date()) + " (" + sdf.format(memberSelected.getDob()) + ")"));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.BIRTH_WEIGHT));
        if (memberSelected.getBirthWeight() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, memberSelected.getBirthWeight() + " " + LabelConstants.UNIT_OF_MASS_IN_KG));
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(LabelConstants.NOT_AVAILABLE)));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.BIRTH_PLACE));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.getNotAvailableIfNull(FullFormConstants.getFullFormsOfPlace(memberSelected.getPlaceOfBirth()))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.ADDRESS));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getFamilyFullAddress(familyDataBean)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.RELIGION_WITH_CASTE));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                UtilBean.getMyLabel(fhsService.getValueOfListValuesById(familyDataBean.getReligion()))
                        + " / "
                        + UtilBean.getMyLabel(fhsService.getValueOfListValuesById(familyDataBean.getCaste()))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.LAST_CHILD_SERVICE_DATE));
        if (memberSelected.getAdditionalInfo() != null) {
            MemberAdditionalInfoDataBean additionalInfo = new Gson().fromJson(memberSelected.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
            if (additionalInfo != null && additionalInfo.getLastServiceLongDate() != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, sdf.format(additionalInfo.getLastServiceLongDate())));
            } else {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(GlobalTypes.NOT_AVAILABLE)));
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(GlobalTypes.NOT_AVAILABLE)));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.FULLY_IMMUNISED_IN_1_YEAR));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, formMetaDataUtil.isChildImmunisedInOneYear(new MemberBean(memberSelected))));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.FULLY_IMMUNISED_IN_2_YEAR));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, formMetaDataUtil.isChildImmunisedInTwoYear(new MemberBean(memberSelected))));

        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MEMBER_ID, memberSelected.getId());
        SharedStructureData.immunisationService = immunisationService;
        SharedStructureData.sewaFhsService = fhsService;
        QueFormBean queFormBean = new QueFormBean();
        queFormBean.setRelatedpropertyname(RelatedPropertyNameConstants.MEMBER_ID);
        bodyLayoutContainer.addView(MyDynamicComponents.getImmunisationGivenComponent(this, queFormBean));
    }

    private void createTableLayout(List<String> headNames, List<LinkedHashMap<String, Object>> resultList) {
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setPadding(10, 10, 10, 10);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, MATCH_PARENT, 1);

        TableRow row = new TableRow(this);
        row.setPadding(10, 15, 10, 15);
        row.setLayoutParams(new TableRow.LayoutParams(MATCH_PARENT));

        for (String string : headNames) {
            MaterialTextView textView = new MaterialTextView(this);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setPadding(10, 10, 10, 10);
            textView.setText(UtilBean.getMyLabel(string));
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setTextColor(ContextCompat.getColor(context, R.color.textColor));
            row.addView(textView, layoutParams);
        }

        MaterialTextView textView = new MaterialTextView(this);
        textView.setPadding(10, 10, 10, 10);
        TableRow.LayoutParams layoutParamsForIcon = new TableRow.LayoutParams(0, MATCH_PARENT, 0.5f);
        row.addView(textView, layoutParamsForIcon);

        tableLayout.addView(row, 0);

        bodyLayoutContainer.addView(tableLayout);

        int count = 1;
        for (LinkedHashMap<String, Object> map : resultList) {
            addTableRow(tableLayout, count, map, headNames);
            count++;
        }

        nextButton.setText(GlobalTypes.EVENT_OKAY);
    }

    private void addTableRow(TableLayout tableLayout, int index, final LinkedHashMap<String, Object> map, List<String> headNames) {
        TableRow.LayoutParams layoutParamsForRow = new TableRow.LayoutParams(MATCH_PARENT);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, MATCH_PARENT, 1);

        TableRow row = new TableRow(this);
        if (SewaUtil.CURRENT_THEME == R.style.techo_training_app) {
            row.setBackgroundResource(R.drawable.table_row_selector);
        } else {
            row.setBackgroundResource(R.drawable.spinner_item_border);
        }
        row.setLayoutParams(layoutParamsForRow);
        row.setPadding(10, 15, 10, 15);

        Object tmpObj;
        for (String string : headNames) {
            MaterialTextView textView = new MaterialTextView(this);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setPadding(10, 10, 10, 10);
            textView.setTextColor(ContextCompat.getColorStateList(context, R.color.listview_text_color_selector));
            tmpObj = map.get(string);
            if (tmpObj != null) {
                textView.setText(tmpObj.toString());
            } else {
                textView.setText(LabelConstants.N_A);
            }
            row.addView(textView, layoutParams);
        }

        row.setOnClickListener(v -> {
            Object visitId = map.get("hiddenVisitId");
            Object serviceType = map.get("hiddenServiceType");
            if (visitId != null && serviceType != null) {
                Intent intent = new Intent(MyPeopleActivity.this, WorkRegisterLineListActivity_.class);
                intent.putExtra("visitId", visitId.toString());
                intent.putExtra("serviceType", serviceType.toString());
                startActivity(intent);
            }
        });

        TableRow.LayoutParams layoutParamsForIcon = new TableRow.LayoutParams(0, MATCH_PARENT, 0.5f);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;

        LinearLayout layout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.HORIZONTAL, params);
        ImageView imageView = MyStaticComponents.getImageView(context, -1, R.drawable.ic_chevron_right, params);
        imageView.setMaxWidth(30);
        imageView.setMaxHeight(30);
        imageView.setImageTintList(ContextCompat.getColorStateList(context, R.color.listview_text_color_selector));
        layout.addView(imageView);

        row.addView(layout, layoutParamsForIcon);
        tableLayout.addView(row, index);
    }

    @UiThread
    public void setRadioButtonColorFromSyncStatus(RadioButton radioButton, MemberDataBean memberDataBean) {
        if (memberDataBean.getSyncStatus() != null) {
            switch (memberDataBean.getSyncStatus()) {
                case "B":
                    radioButton.setTextColor(Color.BLUE);
                    break;
                case "R":
                    radioButton.setTextColor(Color.RED);
                    break;
                default:
                    radioButton.setTextColor(Color.BLACK);
            }
        } else if (memberDataBean.getAdditionalInfo() != null) {
            Gson gson = new Gson();
            MemberAdditionalInfoDataBean memberAdditionalInfo = gson.fromJson(memberDataBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
            if (memberAdditionalInfo.getCpNegativeQues() != null && memberAdditionalInfo.getCpState() != null && memberAdditionalInfo.getCpState().equals(RchConstants.CP_DELAYED_DEVELOPMENT)) {
                radioButton.setTextColor(Color.YELLOW);
            } else if (memberAdditionalInfo.getCpNegativeQues() != null && memberAdditionalInfo.getCpState() != null && memberAdditionalInfo.getCpState().equals(RchConstants.CP_TREATMENT_COMMENCED)) {
                radioButton.setTextColor(Color.parseColor("#20aa0b"));//dark green
            } else {
                radioButton.setTextColor(Color.BLACK);
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
                LabelConstants.ON_MY_PEOPLE_ACTIVITY_CLOSE_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        myAlertDialog.show();
    }

    private void startDynamicFormActivity(final String formType, MemberDataBean memberDataBean, FamilyDataBean familyDataBean) {
        if (formType.equals((FormConstants.TECHO_FHW_VAE)) && memberSelected.getImmunisationGiven() == null) {
            footerLayout.setVisibility(View.GONE);
            View.OnClickListener listener = v -> alertDialog.dismiss();
            alertDialog = new MyAlertDialog(this,
                    LabelConstants.NO_IMMUNISATIONS_GIVEN_TO_MEMBER_ALERT,
                    listener, DynamicUtils.BUTTON_OK);
            alertDialog.show();
            return;
        }

        showProcessDialog();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (!formType.equals(FormConstants.FHS_MEMBER_UPDATE)) {
            try {
                formMetaDataUtil.setMetaDataForRchFormByFormType(formType, memberDataBean.getId(), memberDataBean.getFamilyId(), null, sharedPref);
            } catch (DataException e) {
                showProcessDialog();
                View.OnClickListener listener = v -> {
                    alertDialog.dismiss();
                    navigateToHomeScreen(false);
                };
                alertDialog = new MyAlertDialog(this, false,
                        UtilBean.getMyLabel(LabelConstants.ERROR_TO_REFRESH_ALERT), listener, DynamicUtils.BUTTON_OK);
                alertDialog.show();
                return;
            }
        } else {
            if (memberDataBean != null) {
                familyDataBean = fhsService.retrieveFamilyDataBeanByFamilyId(memberDataBean.getFamilyId());
            }
            formMetaDataUtil.setMetaDataForMemberUpdateForm(memberDataBean, familyDataBean, sharedPref);
        }
        myIntent = new Intent(this, DynamicFormActivity_.class);
        myIntent.putExtra(SewaConstants.ENTITY, formType);
        startActivityForResult(myIntent, REQUEST_CODE_FOR_MY_PEOPLE_ACTIVITY);
        hideProcessDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult resultForQRScanner = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_MY_PEOPLE_ACTIVITY) {
            setSubTitle(null);
            memberList.clear();
            familyList.clear();
            familySelected = null;
            switch (selectedService) {
                case SERVICE_TEMP_REGISTRATION:
                case SERVICE_GERIATRIC_MEMBERS:
                case SERVICE_TRAVELLERS_SCREENING:
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    setServiceSelectionScreen();
                    break;
                case SERVICE_UPDATE_MEMBER:
                    screen = MEMBER_SELECTION_SCREEN;
                    bodyLayoutContainer.removeAllViews();
                    addSearchTextBox();
                    break;
                case SERVICE_ADD_NEW_MEMBER:
                    screen = FAMILY_SELECTION_SCREEN;
                    bodyLayoutContainer.removeAllViews();
                    addSearchTextBox();
                    break;
                case SERVICE_MIGRATED_OUT_MEMBERS:
                    screen = MIGRATED_MEMBERS_SCREEN;
                    showProcessDialog();
                    retrieveMigratedMembersList(true);
                    break;
                case SERVICE_MIGRATED_IN_MEMBERS:
                    screen = MIGRATED_MEMBERS_SCREEN;
                    showProcessDialog();
                    retrieveMigratedMembersList(false);
                    break;
                case SERVICE_MIGRATED_OUT_FAMILY:
                    screen = MIGRATED_FAMILY_SCREEN;
                    showProcessDialog();
                    retrieveMigratedFamilyList(true);
                    break;
                case SERVICE_MIGRATED_IN_FAMILY:
                    screen = MIGRATED_FAMILY_SCREEN;
                    showProcessDialog();
                    retrieveMigratedFamilyList(false);
                    break;
                default:
                    screen = PEOPLE_SELECTION_SCREEN;
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    setSubTitle(UtilBean.getMemberFullName(memberSelected));
                    setVisitSelectionScreen(visits);
                    break;
            }
        } else if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Integer locationId = Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID));
                selectedVillage = new Gson().fromJson(data.getStringExtra(FieldNameConstants.LOCATION), LocationMasterBean.class).getParent();
                selectedAshaAreas.clear();
                selectedAshaAreas.add(locationId);
                showProcessDialog();
                bodyLayoutContainer.removeAllViews();
                setServiceSelectionScreen();
            } else {
                finish();
            }
        } else if (resultForQRScanner != null) {
            if (resultForQRScanner.getContents() == null) {
                SewaUtil.generateToast(this, LabelConstants.FAILED_TO_SCAN_QR);
            } else {
                //show dialogue with resultForQRScanner
                String scanningData = resultForQRScanner.getContents();
                Log.i(TAG, "QR Scanner Data : " + scanningData);
                if (selectedService.equals(SERVICE_UPDATE_MEMBER)) {
                    retrieveMemberListForUpdateBySearch(null, scanningData);
                } else if (selectedService.equals(SERVICE_RCH_REGISTER)) {
                    retrieveMemberListForRchRegister(null, scanningData);
                } else {
                    retrieveMemberListByServiceType(selectedService, null, true, scanningData);
                }
            }
        } else {
            navigateToHomeScreen(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            setSubTitle(null);
            selectedFamilyIndex = -1;
            switch (screen) {
                case VISIT_SELECTION_SCREEN:
                    footerLayout.setVisibility(View.VISIBLE);
                    screen = PEOPLE_SELECTION_SCREEN;
                    selectedPeopleIndex = -1;
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                    addSearchTextBox();
                    retrieveMemberListByServiceType(selectedService, null, false, null);
                    memberSelected = null;
                    break;

                case MANAGE_FAMILY_MIGRATIONS_SCREEN:
                    showProcessDialog();
                    setTitle(LabelConstants.MY_PEOPLE_TITLE);
                    bodyLayoutContainer.removeAllViews();
                    setServiceSelectionScreen();
                    nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                    nextButton.setOnClickListener(this);
                    break;

                case MIGRATED_FAMILY_SCREEN:
                    selectedServiceIndex = -1;
                    showProcessDialog();
                    memberList.clear();
                    familyList.clear();
                    setManageFamilyMigrationsScreen();
                    break;

                case PEOPLE_SELECTION_SCREEN:
                case FAMILY_SELECTION_SCREEN:
                case MEMBER_SELECTION_SCREEN:
                case MIGRATED_MEMBERS_SCREEN:
                case RCH_REGISTER_MEMBER_SCREEN:
                    selectedServiceIndex = -1;
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    memberList.clear();
                    familyList.clear();
                    setServiceSelectionScreen();
                    nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                    nextButton.setOnClickListener(this);
                    break;

                case SERVICE_SELECTION_SCREEN:
                    startLocationSelectionActivity();
                    break;

                case VILLAGE_SELECTION_SCREEN:
                    navigateToHomeScreen(false);
                    break;

                case RCH_REGISTER_TABLE_SCREEN:
                    showProcessDialog();
                    selectedService = SERVICE_RCH_REGISTER;
                    bodyLayoutContainer.removeAllViews();
                    bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                    addSearchTextBox();
                    retrieveMemberListForRchRegister(null, null);
                    nextButton.setText(GlobalTypes.EVENT_NEXT);
                    break;

                case CHART_SELECTION_SCREEN:
                    showProcessDialog();
                    setVisits();
                    break;

                default:
                    break;
            }
        }
        return true;
    }
}
