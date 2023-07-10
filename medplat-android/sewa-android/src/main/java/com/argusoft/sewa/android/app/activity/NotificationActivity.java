package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.argusoft.sewa.android.app.constants.NotificationConstants;
import com.argusoft.sewa.android.app.core.impl.ImmunisationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.NcdServiceImpl;
import com.argusoft.sewa.android.app.core.impl.NotificationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.FamilyMigrationDetailsDataBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.MemberMoConfirmedDataBean;
import com.argusoft.sewa.android.app.databean.MigrationDetailsDataBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.exception.DataException;
import com.argusoft.sewa.android.app.model.CovidTravellersInfoBean;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.MemberBean;
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

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

@EActivity
public class NotificationActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    private static final String VILLAGE_SELECTION_SCREEN = "villageSelectionScreen";
    private static final String NOTIFICATION_TYPE_SELECTION_SCREEN = "notificationTypeSelectionScreen";
    private static final String NOTIFICATION_SELECTION_SCREEN = "notificationSelectionScreen";
    private static final String MIGRATION_REVERT_NOTIFICATION_SCREEN = "migrationRevertNotificationScreen";
    private static final String READ_ONLY_NOTIFICATION_SCREEN = "readOnlyNotificationScreen";
    private static final Integer REQUEST_CODE_FOR_NOTIFICATION_ACTIVITY = 200;
    private static final Integer REQUEST_CODE_FOR_OTHER_NOTIFICATION_ACTIVITY = 201;
    private static final long LIMIT = 30;
    private static final long DELAY = 500;
    private static final String TAG = "NotificationActivity";

    @Bean
    public ImmunisationServiceImpl immunisationService;
    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    NotificationServiceImpl notificationService;
    @Bean
    FormMetaDataUtil formMetaDataUtil;
    @Bean
    public NcdServiceImpl ncdService;
    private long offset = 0;

    private boolean isFromLogin;
    private View.OnClickListener onClickListener = this;
    private List<NotificationMobDataBean> notificationBeanList = new LinkedList<>();
    private NotificationMobDataBean selectedNotification;
    private List<LocationBean> villageList = new ArrayList<>();
    private List<LocationBean> ashaAreaList = new ArrayList<>();
    private Integer selectedVillageId;

    private LinearLayout bodyLayoutContainer;
    private Button nextButton;

    private String screenName;
    private Spinner villageSpinner;
    private Spinner ashaAreaSpinner;
    private MaterialTextView selectAshaAreaTextView;
    private Map<Integer, String> notificationCodeWithRadioButtonIdMap = new HashMap<>();
    private String selectedNotificationCode = null;
    private TextInputLayout searchBox;
    private PagingListView notificationListView;
    private Timer timer = new Timer();
    private List<Integer> selectedVillageIds = new ArrayList<>();
    private List<Integer> selectedAreaIds = new ArrayList<>();
    private Map<String, Integer> notificationsCountMap;
    private LinearLayout globalPanel;
    private LinearLayout footerView;
    private MaterialTextView titleView;
    private CharSequence search;
    private LinkedHashMap<String, String> qrScanFilter = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFromLogin = getIntent().getBooleanExtra("isFromLogin", false);
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
            Intent intent = new Intent(this, LoginActivity_.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        setTitle(UtilBean.getTitleText(LabelConstants.NOTIFICATION_ACTIVITY_TITLE));
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        footerView = globalPanel.findViewById(DynamicUtils.ID_FOOTER);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        setBodyDetail();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screenName) {
                case VILLAGE_SELECTION_SCREEN:
                    showProcessDialog();
                    String selectedVillage;
                    if (villageList.size() == 1) {
                        selectedVillage = villageList.get(0).getName();
                    } else {
                        selectedVillage = villageSpinner.getSelectedItem().toString();
                    }

                    if (!selectedVillage.equals(LabelConstants.ALL) && !selectedVillage.equals(UtilBean.getMyLabel(LabelConstants.ALL))) {
                        for (LocationBean locationBean : villageList) {
                            if (selectedVillage.equals(locationBean.getName())) {
                                selectedVillageId = locationBean.getActualID();
                                if (!selectedVillageIds.contains(locationBean.getActualID())) {
                                    selectedVillageIds.add(locationBean.getActualID());
                                }
                                break;
                            }
                        }

                        if (ashaAreaSpinner != null) {
                            String selectedAshaArea = ashaAreaSpinner.getSelectedItem().toString();
                            if (!selectedAshaArea.equals(LabelConstants.ALL) && !selectedAshaArea.equals(UtilBean.getMyLabel(LabelConstants.ALL))) {
                                for (LocationBean locationBean : ashaAreaList) {
                                    if (selectedAshaArea.equals(locationBean.getName())) {
                                        selectedAreaIds.add(locationBean.getActualID());
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        for (LocationBean locationBean : villageList) {
                            if (!selectedVillageIds.contains(locationBean.getActualID())) {
                                selectedVillageIds.add(locationBean.getActualID());
                            }
                        }
                    }
                    retrieveCountsForEachNotificationType();
                    break;

                case NOTIFICATION_SELECTION_SCREEN:
                    Intent intent;
                    if (notificationBeanList == null || notificationBeanList.isEmpty()) {
                        addNotificationTypeScreen();
                        nextButton.setText(GlobalTypes.EVENT_NEXT);
                        selectedNotificationCode = null;
                        return;
                    }
                    if (selectedNotification == null) {
                        SewaUtil.generateToast(this, LabelConstants.TASK_SELECTION_ALERT);
                        return;
                    }
                    switch (selectedNotification.getTask()) {
                        case NotificationConstants.FHW_NOTIFICATION_MIGRATION_IN:
                            showProcessDialog();
                            intent = new Intent(this, MigrationInConfirmationActivity_.class);
                            intent.putExtra(GlobalTypes.NOTIFICATION_BEAN, new Gson().toJson(selectedNotification, NotificationMobDataBean.class));
                            startActivityForResult(intent, REQUEST_CODE_FOR_NOTIFICATION_ACTIVITY);
                            hideProcessDialog();
                            break;
                        case NotificationConstants.FHW_NOTIFICATION_MIGRATION_OUT:
                            showProcessDialog();
                            intent = new Intent(this, MigrationOutConfirmationActivity_.class);
                            intent.putExtra(GlobalTypes.NOTIFICATION_BEAN, new Gson().toJson(selectedNotification, NotificationMobDataBean.class));
                            startActivityForResult(intent, REQUEST_CODE_FOR_NOTIFICATION_ACTIVITY);
                            hideProcessDialog();
                            break;
                        case NotificationConstants.FHW_NOTIFICATION_FAMILY_MIGRATION_IN:
                            showProcessDialog();
                            intent = new Intent(this, FamilyMigrationInConfirmationActivity_.class);
                            intent.putExtra(GlobalTypes.NOTIFICATION_BEAN, new Gson().toJson(selectedNotification, NotificationMobDataBean.class));
                            startActivityForResult(intent, REQUEST_CODE_FOR_NOTIFICATION_ACTIVITY);
                            hideProcessDialog();
                            break;
                        case NotificationConstants.FHW_NOTIFICATION_READ_ONLY:
                            showReadOnlyNotification();
                            break;
                        case NotificationConstants.FHW_NOTIFICATION_WORK_PLAN_FOR_DELIVERY:
                            if (Boolean.TRUE.equals(notificationService.checkIfThereArePendingNotificationsForMember(selectedNotification.getMemberId(), NotificationConstants.FHW_NOTIFICATION_ANC))) {
                                View.OnClickListener myListener = v1 -> {
                                    if (v1.getId() == BUTTON_POSITIVE) {
                                        alertDialog.dismiss();
                                        startDynamicFormActivity();
                                    } else {
                                        alertDialog.dismiss();
                                    }
                                };

                                alertDialog = new MyAlertDialog(this,
                                        LabelConstants.PROCEED_FROM_WPD_WHILE_ANC_ALERTS_PENDING_ALERT,
                                        myListener, DynamicUtils.BUTTON_YES_NO);
                                alertDialog.show();
                            } else {
                                startDynamicFormActivity();
                            }
                            break;
                        default:
                            startDynamicFormActivity();
                            break;
                    }
                    break;
                case MIGRATION_REVERT_NOTIFICATION_SCREEN:
                case READ_ONLY_NOTIFICATION_SCREEN:
                    bodyLayoutContainer.removeAllViews();
                    showProcessDialog();
                    addLastUpdateLabel();
                    addSearchTextBox();
                    retrieveInitialNotificationsFromDB(selectedNotificationCode, null, LIMIT, offset, null);
                    break;
                default:
            }
        }
    }

    @Background
    public void setBodyDetail() {
        startLocationSelectionActivity();
    }

    private void startLocationSelectionActivity() {
        Intent myIntent = new Intent(context, LocationSelectionActivity_.class);
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.NOTIFICATION_ACTIVITY_TITLE);
        startActivityForResult(myIntent, ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE);
    }

    @UiThread
    public void addVillageSelectionSpinner() {
        screenName = VILLAGE_SELECTION_SCREEN;
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.SELECT_VILLAGE)));
        String[] arrayOfOptions = new String[villageList.size() + 1];
        int i = 0;
        arrayOfOptions[i++] = LabelConstants.ALL;
        for (LocationBean locationBean : villageList) {
            arrayOfOptions[i++] = locationBean.getName();
        }
        villageSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 2);
        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ashaAreaList = null;
                } else {
                    selectedVillageId = villageList.get(position - 1).getActualID();
                    ashaAreaList = fhsService.retrieveAshaAreaAssignedToUser(selectedVillageId);
                }
                addAshaAreaSelectionSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing to do here
            }
        });
        bodyLayoutContainer.addView(villageSpinner);
        hideProcessDialog();
    }

    @UiThread
    public void addAshaAreaSelectionSpinner() {
        screenName = VILLAGE_SELECTION_SCREEN;
        bodyLayoutContainer.removeView(selectAshaAreaTextView);
        bodyLayoutContainer.removeView(ashaAreaSpinner);
        selectAshaAreaTextView = MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.SELECT_ASHA_AREA));

        if (ashaAreaList != null && !ashaAreaList.isEmpty()) {
            String[] arrayOfOptions = new String[ashaAreaList.size() + 1];
            arrayOfOptions[0] = LabelConstants.ALL;
            int i = 1;
            for (LocationBean locationBean : ashaAreaList) {
                arrayOfOptions[i] = locationBean.getName();
                i++;
            }
            ashaAreaSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 3);
            bodyLayoutContainer.addView(selectAshaAreaTextView);
            bodyLayoutContainer.addView(ashaAreaSpinner);
        } else {
            ashaAreaSpinner = null;
        }
        hideProcessDialog();
    }

    @Background
    public void retrieveCountsForEachNotificationType() {
        selectedNotificationCode = null;
        notificationsCountMap = notificationService.retrieveCountForNotificationType(selectedVillageIds, selectedAreaIds);
        addNotificationTypeScreen();
    }

    private String setNotificationCountFromMap(Integer count) {
        return count != null ? count.toString() : null;
    }

    @UiThread
    public void addNotificationTypeScreen() {
        screenName = NOTIFICATION_TYPE_SELECTION_SCREEN;
        footerView.setVisibility(View.GONE);
        bodyLayoutContainer.removeAllViews();

        int count = 0;
        notificationCodeWithRadioButtonIdMap = new HashMap<>();

        List<ListItemDataBean> listItems = new ArrayList<>();

//        notificationCodeWithRadioButtonIdMap.put(count, NotificationConstants.FHW_NOTIFICATION_TRAVELLERS_SCREENING);
//        listItems.add(new ListItemDataBean(-1,
//                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_NOTIFICATION_TRAVELLERS_SCREENING)),
//                setNotificationCountFromMap(notificationsCountMap.get(NotificationConstants.FHW_NOTIFICATION_TRAVELLERS_SCREENING))));
//        count++;

        notificationCodeWithRadioButtonIdMap.put(count, NotificationConstants.FHW_NOTIFICATION_LMP_FOLLOW_UP);
        listItems.add(new ListItemDataBean(-1,
                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_NOTIFICATION_LMP_FOLLOW_UP)),
                setNotificationCountFromMap(notificationsCountMap.get(NotificationConstants.FHW_NOTIFICATION_LMP_FOLLOW_UP))));
        count++;

        notificationCodeWithRadioButtonIdMap.put(count, NotificationConstants.FHW_NOTIFICATION_ANC);
        listItems.add(new ListItemDataBean(-1,
                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_NOTIFICATION_ANC)),
                setNotificationCountFromMap(notificationsCountMap.get(NotificationConstants.FHW_NOTIFICATION_ANC))));
        count++;

        notificationCodeWithRadioButtonIdMap.put(count, NotificationConstants.FHW_NOTIFICATION_WORK_PLAN_FOR_DELIVERY);
        listItems.add(new ListItemDataBean(-1,
                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_NOTIFICATION_WORK_PLAN_FOR_DELIVERY)),
                setNotificationCountFromMap(notificationsCountMap.get(NotificationConstants.FHW_NOTIFICATION_WORK_PLAN_FOR_DELIVERY))));
        count++;

        notificationCodeWithRadioButtonIdMap.put(count, NotificationConstants.FHW_NOTIFICATION_PNC);
        listItems.add(new ListItemDataBean(-1,
                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_NOTIFICATION_PNC)),
                setNotificationCountFromMap(notificationsCountMap.get(NotificationConstants.FHW_NOTIFICATION_PNC))));
        count++;

        notificationCodeWithRadioButtonIdMap.put(count, NotificationConstants.FHW_NOTIFICATION_CHILD_SERVICES);
        listItems.add(new ListItemDataBean(-1,
                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_NOTIFICATION_CHILD_SERVICES)),
                setNotificationCountFromMap(notificationsCountMap.get(NotificationConstants.FHW_NOTIFICATION_CHILD_SERVICES))));
        count++;

//        notificationCodeWithRadioButtonIdMap.put(count, NotificationConstants.FHW_NOTIFICATION_MIGRATION_IN);
//        listItems.add(new ListItemDataBean(-1,
//                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_NOTIFICATION_MIGRATION_IN)),
//                setNotificationCountFromMap(notificationsCountMap.get(NotificationConstants.FHW_NOTIFICATION_MIGRATION_IN))));
//        count++;

//        notificationCodeWithRadioButtonIdMap.put(count, NotificationConstants.FHW_NOTIFICATION_MIGRATION_OUT);
//        listItems.add(new ListItemDataBean(-1,
//                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_NOTIFICATION_MIGRATION_OUT)),
//                setNotificationCountFromMap(notificationsCountMap.get(NotificationConstants.FHW_NOTIFICATION_MIGRATION_OUT))));
//        count++;

//        notificationCodeWithRadioButtonIdMap.put(count, NotificationConstants.FHW_NOTIFICATION_FAMILY_MIGRATION_IN);
//        listItems.add(new ListItemDataBean(-1,
//                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_NOTIFICATION_FAMILY_MIGRATION_IN)),
//                setNotificationCountFromMap(notificationsCountMap.get(NotificationConstants.FHW_NOTIFICATION_FAMILY_MIGRATION_IN))));
//        count++;

        notificationCodeWithRadioButtonIdMap.put(count, NotificationConstants.FHW_NOTIFICATION_DISCHARGE);
        listItems.add(new ListItemDataBean(-1,
                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_NOTIFICATION_DISCHARGE)),
                setNotificationCountFromMap(notificationsCountMap.get(NotificationConstants.FHW_NOTIFICATION_DISCHARGE))));
        count++;

//        notificationCodeWithRadioButtonIdMap.put(count, NotificationConstants.FHW_NOTIFICATION_SAM_SCREENING);
//        listItems.add(new ListItemDataBean(-1,
//                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_NOTIFICATION_SAM_SCREENING)),
//                setNotificationCountFromMap(notificationsCountMap.get(NotificationConstants.FHW_NOTIFICATION_SAM_SCREENING))));
//        count++;
//
//        notificationCodeWithRadioButtonIdMap.put(count, FormConstants.FHW_SAM_SCREENING_REF);
//        listItems.add(new ListItemDataBean(-1,
//                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(FormConstants.FHW_SAM_SCREENING_REF)),
//                setNotificationCountFromMap(notificationsCountMap.get(FormConstants.FHW_SAM_SCREENING_REF))));
//        count++;
//
//        notificationCodeWithRadioButtonIdMap.put(count, FormConstants.FHW_MONTHLY_SAM_SCREENING);
//        listItems.add(new ListItemDataBean(-1,
//                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(FormConstants.FHW_MONTHLY_SAM_SCREENING)),
//                setNotificationCountFromMap(notificationsCountMap.get(FormConstants.FHW_MONTHLY_SAM_SCREENING))));
//        count++;
//
//        notificationCodeWithRadioButtonIdMap.put(count, FormConstants.CMAM_FOLLOWUP);
//        listItems.add(new ListItemDataBean(-1,
//                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(FormConstants.CMAM_FOLLOWUP)),
//                setNotificationCountFromMap(notificationsCountMap.get(FormConstants.CMAM_FOLLOWUP))));
//        count++;
//
//        notificationCodeWithRadioButtonIdMap.put(count, NotificationConstants.FHW_NOTIFICATION_GERIATRICS_MEDICATION);
//        listItems.add(new ListItemDataBean(-1,
//                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_NOTIFICATION_GERIATRICS_MEDICATION)),
//                setNotificationCountFromMap(notificationsCountMap.get(NotificationConstants.FHW_NOTIFICATION_GERIATRICS_MEDICATION))));
//        count++;
//
//        notificationCodeWithRadioButtonIdMap.put(count, NotificationConstants.FHW_NOTIFICATION_APPETITE);
//        listItems.add(new ListItemDataBean(-1,
//                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_NOTIFICATION_APPETITE)),
//                setNotificationCountFromMap(notificationsCountMap.get(NotificationConstants.FHW_NOTIFICATION_APPETITE))));
//        count++;
//
//        notificationCodeWithRadioButtonIdMap.put(count, NotificationConstants.FHW_NOTIFICATION_READ_ONLY);
//        listItems.add(new ListItemDataBean(-1,
//                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_NOTIFICATION_READ_ONLY)),
//                setNotificationCountFromMap(notificationsCountMap.get(NotificationConstants.FHW_NOTIFICATION_READ_ONLY))));
//        count++;

        notificationCodeWithRadioButtonIdMap.put(count, NotificationConstants.FHW_WORK_PLAN_MAMTA_DAY);
        listItems.add(new ListItemDataBean(-1,
                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_WORK_PLAN_MAMTA_DAY)),
                null));
        count++;

        notificationCodeWithRadioButtonIdMap.put(count, NotificationConstants.FHW_WORK_PLAN_OTHER_SERVICES);
        listItems.add(new ListItemDataBean(-1,
                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_WORK_PLAN_OTHER_SERVICES)),
                null));
        count++;

//        notificationCodeWithRadioButtonIdMap.put(count, NotificationConstants.NOTIFICATION_NCD_CLINIC_VISIT);
//        listItems.add(new ListItemDataBean(-1,
//                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.NOTIFICATION_NCD_CLINIC_VISIT)),
//                setNotificationCountFromMap(notificationsCountMap.get(NotificationConstants.NOTIFICATION_NCD_CLINIC_VISIT))));
//        count++;
//
//        notificationCodeWithRadioButtonIdMap.put(count, NotificationConstants.NOTIFICATION_NCD_HOME_VISIT);
//        listItems.add(new ListItemDataBean(-1,
//                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.NOTIFICATION_NCD_HOME_VISIT)),
//                setNotificationCountFromMap(notificationsCountMap.get(NotificationConstants.NOTIFICATION_NCD_HOME_VISIT))));
//        count++;
//
//        notificationCodeWithRadioButtonIdMap.put(count, NotificationConstants.FHW_WORK_PLAN_ASHA_REPORTED_EVENT);
//        listItems.add(new ListItemDataBean(-1,
//                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_WORK_PLAN_ASHA_REPORTED_EVENT)),
//                null));

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            selectedNotificationCode = notificationCodeWithRadioButtonIdMap.get(position);
            offset = 0;
            Intent intent;
            if (selectedNotificationCode == null) {
                SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.SELECT_NOTIFICATION_TYPE));
                return;
            }
            switch (selectedNotificationCode) {
                case NotificationConstants.FHW_WORK_PLAN_MAMTA_DAY:
                    intent = new Intent(context, MamtaDayActivity_.class);
                    intent.putExtra(GlobalTypes.SELECTED_ASHA_AREAS, new Gson().toJson(selectedAreaIds));
                    intent.putExtra(GlobalTypes.SELECTED_VILLAGE_IDS, new Gson().toJson(selectedVillageIds));
                    startActivityForResult(intent, REQUEST_CODE_FOR_OTHER_NOTIFICATION_ACTIVITY);
                    break;

                case NotificationConstants.FHW_WORK_PLAN_OTHER_SERVICES:
                    intent = new Intent(context, NotificationOtherServicesActivity_.class);
                    intent.putExtra(GlobalTypes.SELECTED_ASHA_AREAS, new Gson().toJson(selectedAreaIds));
                    intent.putExtra(GlobalTypes.SELECTED_VILLAGE_IDS, new Gson().toJson(selectedVillageIds));
                    startActivityForResult(intent, REQUEST_CODE_FOR_OTHER_NOTIFICATION_ACTIVITY);
                    break;

                case NotificationConstants.FHW_WORK_PLAN_ASHA_REPORTED_EVENT:
                    intent = new Intent(context, ReportedEventConfirmationNotificationActivity_.class);
                    intent.putExtra(GlobalTypes.SELECTED_ASHA_AREAS, new Gson().toJson(selectedAreaIds));
                    intent.putExtra(GlobalTypes.SELECTED_VILLAGE_IDS, new Gson().toJson(selectedVillageIds));
                    startActivityForResult(intent, REQUEST_CODE_FOR_OTHER_NOTIFICATION_ACTIVITY);
                    break;

                default:
                    bodyLayoutContainer.removeAllViews();
                    showProcessDialog();
                    addLastUpdateLabel();
                    addSearchTextBox();
                    retrieveInitialNotificationsFromDB(selectedNotificationCode, null, LIMIT, offset, null);
                    footerView.setVisibility(View.VISIBLE);
                    break;
            }
        };

        PagingListView notificationTypeListView = MyStaticComponents.getPaginatedListViewWithItem(context, listItems, R.layout.listview_row_notification, onItemClickListener, null);
        bodyLayoutContainer.addView(notificationTypeListView);
        hideProcessDialog();
    }

    @UiThread
    public void addLastUpdateLabel() {
        bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
    }

    @UiThread
    public void addSearchTextBox() {
        if (selectedNotificationCode.equals(NotificationConstants.FHW_NOTIFICATION_MIGRATION_IN)
                || selectedNotificationCode.equals(NotificationConstants.FHW_NOTIFICATION_MIGRATION_OUT)
                || selectedNotificationCode.equals(NotificationConstants.FHW_NOTIFICATION_FAMILY_MIGRATION_IN)
                || selectedNotificationCode.equals(NotificationConstants.FHW_NOTIFICATION_READ_ONLY)) {
            return;
        }

        search = null;
        searchBox = MyStaticComponents.getEditTextWithQrScan(this, LabelConstants.MEMBER_ID_OR_NAME_OR_HEALTH_ID_TO_SEARCH, 1, 15, 1);
        bodyLayoutContainer.addView(searchBox);
        bodyLayoutContainer.addView(MyStaticComponents.getOrTextView(context));

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
                                        runOnUiThread(() -> retrieveInitialNotificationsFromDB(selectedNotificationCode, s, LIMIT, offset, null));
                                    } else if (s == null || s.length() == 0) {
                                        runOnUiThread(() -> {
                                            showProcessDialog();
                                            retrieveInitialNotificationsFromDB(selectedNotificationCode, null, LIMIT, offset, null);
                                            searchBox.clearFocus();
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
    public void retrieveInitialNotificationsFromDB(String notificationCode, CharSequence searchString, long limit, long offset, String qrData) {
        selectedNotification = null;
        search = searchString;
        qrScanFilter = SewaUtil.setQrScanFilterData(qrData);
        try {
            if (notificationCode.equalsIgnoreCase(NotificationConstants.FHW_NOTIFICATION_FAMILY_MIGRATION_IN) ||
                    selectedNotificationCode.equals(NotificationConstants.FHW_NOTIFICATION_MIGRATION_IN) ||
                    selectedNotificationCode.equals(NotificationConstants.FHW_NOTIFICATION_MIGRATION_OUT)) {
                Integer villageId = notificationService.getVillageIdFromAshaArea(selectedAreaIds.get(0));
                if (villageId != null) {
                    selectedVillageIds.add(villageId);
                }
                notificationBeanList = notificationService.retrieveNotificationsForUser(selectedVillageIds, null, notificationCode, searchString, limit, 0, qrScanFilter);
            } else {
                notificationBeanList = notificationService.retrieveNotificationsForUser(selectedVillageIds, selectedAreaIds, notificationCode, searchString, limit, 0, qrScanFilter);
            }
            this.offset = this.offset + LIMIT;
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }
        addNotificationSelectionScreen(searchString != null);
    }

    @UiThread
    public void addNotificationSelectionScreen(boolean isSearch) {
        screenName = NOTIFICATION_SELECTION_SCREEN;
        bodyLayoutContainer.removeView(notificationListView);
        bodyLayoutContainer.removeView(titleView);
        if (notificationBeanList == null || notificationBeanList.isEmpty()) {
            titleView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_NOTIFICATION_ALERT_WITH_GRATITUDE_FOR_WORK);
            bodyLayoutContainer.addView(titleView);
            nextButton.setText(GlobalTypes.EVENT_OKAY);
            nextButton.setOnClickListener(v -> addNotificationTypeScreen());

            if (!isSearch) {
                hideProcessDialog();
            }
            return;
        }

        if (selectedNotificationCode != null && (selectedNotificationCode.equals(NotificationConstants.FHW_NOTIFICATION_FAMILY_MIGRATION_IN))) {
            titleView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_FAMILY);
        } else {
            titleView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_BENEFICIARY);
        }
        bodyLayoutContainer.addView(titleView);

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            if (notificationBeanList.size() > position)
                selectedNotification = notificationBeanList.get(position);
        };

        List<ListItemDataBean> stringList = getListViewItemsForNotificationList(notificationBeanList);
        if (selectedNotificationCode != null && (selectedNotificationCode.equals(NotificationConstants.FHW_NOTIFICATION_FAMILY_MIGRATION_IN) ||
                selectedNotificationCode.equals(NotificationConstants.FHW_NOTIFICATION_MIGRATION_IN) ||
                selectedNotificationCode.equals(NotificationConstants.FHW_NOTIFICATION_MIGRATION_OUT))) {
            notificationListView = MyStaticComponents.getPaginatedListViewWithItem(context, stringList, R.layout.listview_row_with_info, onItemClickListener, this);
        } else {
            notificationListView = MyStaticComponents.getPaginatedListViewWithItem(context, stringList, R.layout.listview_row_with_date, onItemClickListener, this);
        }
        bodyLayoutContainer.addView(notificationListView);
        nextButton.setText(GlobalTypes.EVENT_NEXT);
        nextButton.setOnClickListener(this);

        if (!isSearch) {
            hideProcessDialog();
        }
    }

    @Override
    public void onLoadMoreItems() {
        onLoadMoreBackground();
    }

    @Background
    public void onLoadMoreBackground() {
        try {
            List<NotificationMobDataBean> notificationMobDataBeans = notificationService.retrieveNotificationsForUser(
                    selectedVillageIds, selectedAreaIds, selectedNotificationCode, search, LIMIT, offset, qrScanFilter);
            this.offset = this.offset + LIMIT;
            onLoadMoreUi(notificationMobDataBeans);
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }
    }

    @UiThread
    public void onLoadMoreUi(List<NotificationMobDataBean> notificationMobDataBeans) {
        if (notificationMobDataBeans != null && !notificationMobDataBeans.isEmpty()) {
            notificationBeanList.addAll(notificationMobDataBeans);
            List<ListItemDataBean> stringList = getListViewItemsForNotificationList(notificationMobDataBeans);
            notificationListView.onFinishLoadingWithItem(true, stringList);
        } else {
            notificationListView.onFinishLoadingWithItem(false, null);
        }
    }

    private List<ListItemDataBean> getListViewItemsForNotificationList(List<NotificationMobDataBean> notificationBeans) {
        List<ListItemDataBean> items = new ArrayList<>();
        StringBuilder text;
        for (NotificationMobDataBean notificationMobDataBean : notificationBeans) {
            notificationMobDataBean.setOverdueFlag(
                    notificationMobDataBean.getExpectedDueDate() != null && new Date(notificationMobDataBean.getExpectedDueDate()).before(new Date())
            );
            Gson gson = new Gson();
            String date = null;
            String visit = null;
            StringBuilder vaccines = new StringBuilder();
            text = new StringBuilder();

            MemberBean memberBean;
            switch (notificationMobDataBean.getTask()) {
                case NotificationConstants.FHW_NOTIFICATION_TRAVELLERS_SCREENING:
                    CovidTravellersInfoBean covidTravellersInfoBean = fhsService.retrieveTravellersInfoBeanById(notificationMobDataBean.getMemberId().intValue());
                    text = new StringBuilder(covidTravellersInfoBean.getName());
                    items.add(new ListItemDataBean(null, text.toString(), null, null, notificationMobDataBean.getOverdueFlag()));
                    break;
                case NotificationConstants.FHW_NOTIFICATION_FAMILY_MIGRATION_IN:
                    FamilyMigrationDetailsDataBean familyMigrationDetailsDataBean = gson.fromJson(notificationMobDataBean.getOtherDetails(), FamilyMigrationDetailsDataBean.class);
                    for (String string : familyMigrationDetailsDataBean.getMemberDetails()) {
                        text.append(string).append(LabelConstants.NEW_LINE);
                    }
                    items.add(new ListItemDataBean(LabelConstants.FAMILY_ID, familyMigrationDetailsDataBean.getFamilyIdString(), text.toString()));
                    break;
                case NotificationConstants.FHW_NOTIFICATION_MIGRATION_IN:
                case NotificationConstants.FHW_NOTIFICATION_MIGRATION_OUT:
                    MigrationDetailsDataBean memberDetails = gson.fromJson(notificationMobDataBean.getOtherDetails(), MigrationDetailsDataBean.class);
                    text = new StringBuilder(memberDetails.getFirstName() + " " + memberDetails.getMiddleName() + " " + memberDetails.getLastName());
                    items.add(new ListItemDataBean(memberDetails.getHealthId(), text.toString(), null));
                    break;
                case NotificationConstants.FHW_NOTIFICATION_READ_ONLY:
                    if (notificationMobDataBean.getHeader() != null) {
                        text = new StringBuilder(UtilBean.getMyLabel(notificationMobDataBean.getHeader()));
                    } else {
                        text = new StringBuilder(UtilBean.getMyLabel(LabelConstants.INFORMATION));
                    }
                    items.add(new ListItemDataBean(null, text.toString(), null, null, false));
                    break;
                case FormConstants.TECHO_FHW_CS:
                    memberBean = fhsService.retrieveMemberBeanByActualId(notificationMobDataBean.getMemberId());
                    if (notificationMobDataBean.getExpectedDueDate() != null) {
                        date = UtilBean.convertDateToString(notificationMobDataBean.getExpectedDueDate(), false, false, true);
                    }
                    if (notificationMobDataBean.getCustomField() != null && !notificationMobDataBean.getCustomField().equals("0")) {
                        visit = UtilBean.getMyLabel(LabelConstants.VISIT) + " " + notificationMobDataBean.getCustomField();
                    }
                    Set<String> dueImmunisationsForChild = immunisationService.getDueImmunisationsForChild(memberBean.getDob(), memberBean.getImmunisationGiven());
                    if (dueImmunisationsForChild != null && !dueImmunisationsForChild.isEmpty()) {
                        for (String anImmunisation : dueImmunisationsForChild) {
                            vaccines.append(UtilBean.getMyLabel(anImmunisation.trim())).append(", ");
                        }
                    } else {
                        vaccines = new StringBuilder(UtilBean.getMyLabel(LabelConstants.NO_DUE_VACCINES));
                    }
                    items.add(new ListItemDataBean(date, memberBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberBean), visit, LabelConstants.DUE_VACCINES, vaccines.substring(0, vaccines.length() - 2), notificationMobDataBean.getOverdueFlag()));
                    break;
                case NotificationConstants.NOTIFICATION_NCD_CLINIC_VISIT:
                case NotificationConstants.NOTIFICATION_NCD_HOME_VISIT:
                    Boolean referredBackFlag = null;
                    boolean displayEarly = false;
                    memberBean = fhsService.retrieveMemberBeanByActualId(notificationMobDataBean.getMemberId());
                    if (notificationMobDataBean.getOtherDetails() != null && notificationMobDataBean.getOtherDetails().equalsIgnoreCase("FOLLOWUP")) {
                        referredBackFlag = true;
                    }
                    if (new Date (notificationMobDataBean.getScheduledDate()).after(new Date())) {
                        displayEarly = true;
                    }
                    if (notificationMobDataBean.getExpectedDueDate() != null) {
                        date = UtilBean.convertDateToString(notificationMobDataBean.getExpectedDueDate(), false, false, true);
                    }
                    if (notificationMobDataBean.getCustomField() != null && !notificationMobDataBean.getCustomField().equals("0")) {
                        visit = UtilBean.getMyLabel(LabelConstants.VISIT) + " " + notificationMobDataBean.getCustomField();
                    }
                    items.add(new ListItemDataBean(date, memberBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberBean), visit, notificationMobDataBean.getOverdueFlag(), referredBackFlag, displayEarly));
                    break;
                default:
                    memberBean = fhsService.retrieveMemberBeanByActualId(notificationMobDataBean.getMemberId());
                    if (notificationMobDataBean.getExpectedDueDate() != null) {
                        date = UtilBean.convertDateToString(notificationMobDataBean.getExpectedDueDate(), false, false, true);
                    }
                    if (notificationMobDataBean.getCustomField() != null && !notificationMobDataBean.getCustomField().equals("0")) {
                        visit = UtilBean.getMyLabel(LabelConstants.VISIT) + " " + notificationMobDataBean.getCustomField();
                    }
                    items.add(new ListItemDataBean(date, memberBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberBean), visit, notificationMobDataBean.getOverdueFlag()));
                    break;
            }
        }
        return items;
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
                LabelConstants.ON_NOTIFICATION_ACTIVITY_CLOSE_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    private void startDynamicFormActivity() {
        showProcessDialog();
        String nextEntity;
        if (selectedNotificationCode.equalsIgnoreCase(FormConstants.FHW_MONTHLY_SAM_SCREENING)) {
            nextEntity = "SAM_SCREENING";
        } else if (selectedNotificationCode.equalsIgnoreCase(NotificationConstants.NOTIFICATION_NCD_CLINIC_VISIT)) {
            nextEntity = FormConstants.NCD_FHW_WEEKLY_CLINIC;
        } else if (selectedNotificationCode.equalsIgnoreCase(NotificationConstants.NOTIFICATION_NCD_HOME_VISIT)) {
            nextEntity = FormConstants.NCD_FHW_WEEKLY_HOME;
        } else {
            nextEntity = selectedNotification.getTask();
        }

        if (nextEntity != null && nextEntity.equalsIgnoreCase(NotificationConstants.FHW_NOTIFICATION_TRAVELLERS_SCREENING)) {
            CovidTravellersInfoBean covidTravellersInfoBean = fhsService.retrieveTravellersInfoBeanById(selectedNotification.getMemberId().intValue());
            if (covidTravellersInfoBean != null) {
                formMetaDataUtil.setMetaDataForCovidTravellersForm(covidTravellersInfoBean, selectedNotification);
                Intent intent = new Intent(NotificationActivity.this, DynamicFormActivity_.class);
                intent.putExtra(SewaConstants.ENTITY, nextEntity);
                startActivityForResult(intent, REQUEST_CODE_FOR_NOTIFICATION_ACTIVITY);
            }
        } else if (nextEntity != null && (nextEntity.equalsIgnoreCase(FormConstants.NCD_FHW_WEEKLY_CLINIC)
                || nextEntity.equalsIgnoreCase(FormConstants.NCD_FHW_WEEKLY_HOME))) {
            MemberBean memberBean = fhsService.retrieveMemberBeanByActualId(selectedNotification.getMemberId());
            setMetadataForNcdForm(memberBean);
            Intent intent = new Intent(NotificationActivity.this, DynamicFormActivity_.class);
            intent.putExtra(SewaConstants.ENTITY, nextEntity);
            startActivityForResult(intent, REQUEST_CODE_FOR_NOTIFICATION_ACTIVITY);
        } else if (nextEntity != null) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            try {
                MemberBean memberBean = fhsService.retrieveMemberBeanByActualId(selectedNotification.getMemberId());
                if (memberBean != null) {
                    formMetaDataUtil.setMetaDataForRchFormByFormType(nextEntity, memberBean.getActualId(), memberBean.getFamilyId(), selectedNotification, sharedPref);
                }
            } catch (DataException e) {
                View.OnClickListener listener = v -> {
                    alertDialog.dismiss();
                    navigateToHomeScreen(false);
                };
                alertDialog = new MyAlertDialog(this, false,
                        UtilBean.getMyLabel(LabelConstants.ERROR_TO_REFRESH_ALERT), listener, DynamicUtils.BUTTON_OK);
                alertDialog.show();
                return;
            }
            Intent intent = new Intent(NotificationActivity.this, DynamicFormActivity_.class);
            intent.putExtra(SewaConstants.ENTITY, nextEntity);
            startActivityForResult(intent, REQUEST_CODE_FOR_NOTIFICATION_ACTIVITY);
        }
        hideProcessDialog();
    }

    private void setMetadataForNcdForm(MemberBean memberBean) {
        MemberDataBean memberDataBean = new MemberDataBean(memberBean);
        FamilyDataBean familyDataBean = null;
        MemberMoConfirmedDataBean moConfirmedDataBean = null;
        if (memberDataBean != null) {
            familyDataBean = fhsService.retrieveFamilyDataBeanByFamilyId(memberDataBean.getFamilyId());
            moConfirmedDataBean = ncdService.retrieveMoConfirmedBeanByMemberId(Long.valueOf(memberDataBean.getId()));
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        List<String> basicMedicines = Arrays.asList("T Multivitamin", "T Paracetamol", "T Iron Folic acid", "T vitamin B12", "T Folic acid");
        if (memberDataBean != null) {
            formMetaDataUtil.setMetaDataForWeeklyVisitForms(memberDataBean, familyDataBean, moConfirmedDataBean, basicMedicines, "2", sharedPref);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult resultForQRScanner = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_NOTIFICATION_ACTIVITY) {
            showProcessDialog();
            offset = 0;
            onActivityResultBackgroundTask();
        } else if (requestCode == REQUEST_CODE_FOR_OTHER_NOTIFICATION_ACTIVITY) {
            addNotificationTypeScreen();
        } else if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Integer locationId = Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID));
                selectedAreaIds.clear();
                selectedAreaIds.add(locationId);
                showProcessDialog();
                retrieveCountsForEachNotificationType();
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
                retrieveInitialNotificationsFromDB(selectedNotificationCode, null, LIMIT, offset, scanningData);
            }
        } else {
            navigateToHomeScreen(false);
        }
    }

    private void onActivityResultBackgroundTask() {
        try {
            selectedNotification = null;
            this.offset = 0;
            notificationBeanList = notificationService.retrieveNotificationsForUser(selectedVillageIds, selectedAreaIds, selectedNotificationCode, null, LIMIT, 0, qrScanFilter);
            this.offset = this.offset + LIMIT;
            if (notificationBeanList != null && !notificationBeanList.isEmpty()) {
                bodyLayoutContainer.removeAllViews();
                addLastUpdateLabel();
                addSearchTextBox();
                addNotificationSelectionScreen(false);
            } else {
                notificationsCountMap = notificationService.retrieveCountForNotificationType(selectedVillageIds, selectedAreaIds);
                addNotificationTypeScreen();
            }
        } catch (SQLException e) {
            navigateToHomeScreen(false);
            Log.e(getClass().getName(), null, e);
        }
    }

    @UiThread
    public void showReadOnlyNotification() {
        screenName = READ_ONLY_NOTIFICATION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, selectedNotification.getOtherDetails()));
        nextButton.setText(UtilBean.getMyLabel(LabelConstants.MARK_AS_READ));
        offset = 0;

        final View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                alertDialog.dismiss();
                showProcessDialog();
                bodyLayoutContainer.removeAllViews();
                addLastUpdateLabel();
                addSearchTextBox();
                nextButton.setText(GlobalTypes.EVENT_NEXT);
                nextButton.setOnClickListener(onClickListener);
                markNotificationAsReadAndRetrieveData();
            } else {
                alertDialog.dismiss();
            }
        };

        nextButton.setOnClickListener(v -> {
            alertDialog = new MyAlertDialog(NotificationActivity.this,
                    LabelConstants.CONFORMATION_TO_MARK_NOTIFICATION_AS_READ,
                    myListener, DynamicUtils.BUTTON_YES_NO);
            alertDialog.show();
        });

    }

    @Background
    public void markNotificationAsReadAndRetrieveData() {
        fhsService.markNotificationAsRead(selectedNotification);
        notificationsCountMap = notificationService.retrieveCountForNotificationType(selectedVillageIds, selectedAreaIds);
        retrieveInitialNotificationsFromDB(selectedNotificationCode, null, LIMIT, offset, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (screenName == null || screenName.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            switch (screenName) {
                case VILLAGE_SELECTION_SCREEN:
                    finish();
                    break;
                case NOTIFICATION_TYPE_SELECTION_SCREEN:
                    startLocationSelectionActivity();
                    break;
                case NOTIFICATION_SELECTION_SCREEN:
                    offset = 0;
                    showProcessDialog();
                    retrieveCountsForEachNotificationType();
                    nextButton.setText(GlobalTypes.EVENT_NEXT);
                    break;
                case MIGRATION_REVERT_NOTIFICATION_SCREEN:
                case READ_ONLY_NOTIFICATION_SCREEN:
                    bodyLayoutContainer.removeAllViews();
                    showProcessDialog();
                    addLastUpdateLabel();
                    addSearchTextBox();
                    retrieveInitialNotificationsFromDB(selectedNotificationCode, null, LIMIT, offset, null);
                    nextButton.setText(GlobalTypes.EVENT_NEXT);
                    nextButton.setOnClickListener(this);
                    break;
                default:
            }
            footerView.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }
}
