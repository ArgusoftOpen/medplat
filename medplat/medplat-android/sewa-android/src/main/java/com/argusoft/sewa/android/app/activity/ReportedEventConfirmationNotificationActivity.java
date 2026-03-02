package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.NotificationConstants;
import com.argusoft.sewa.android.app.core.impl.NotificationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.exception.DataException;
import com.argusoft.sewa.android.app.model.FamilyBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.FormMetaDataUtil;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prateek on 9/23/19
 */
@EActivity
public class ReportedEventConfirmationNotificationActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    private static final Integer REQUEST_CODE_FOR_REPORTED_EVENT_CONF_ACTIVITY = 200;
    private static final String SET_SERVICE_TYPE = "setServiceType";
    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    NotificationServiceImpl notificationService;
    @Bean
    FormMetaDataUtil formMetaDataUtil;
    private SharedPreferences sharedPref;
    private LinearLayout bodyLayoutContainer;
    private List<Integer> selectedAshaAreas = new ArrayList<>();
    private List<Integer> selectedVillageIds = new ArrayList<>();
    private NotificationMobDataBean selectedNotification;
    private List<NotificationMobDataBean> notificationMapWithCounter;
    private Gson gson = new Gson();
    private LinearLayout globalPanel;
    private int selectedServiceIndex = -1;
    private String screen;
    private List<String> servicesList = NotificationConstants.ASHA_REPORTED_EVENT_NOTIFICATIONS;
    private Map<String, Integer> notificationCountMap;
    private List<NotificationMobDataBean> notificationMobDataBeans;
    private PagingListView paginatedListView;
    private int selectedMemeberIndex = -1;
    private Button nextButton;
    private long offset;
    private long limit = 30;
    private LinearLayout footerView;
    private LinkedHashMap<String, String> qrScanFilter = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        String selectedAshaAreasString = getIntent().getStringExtra(GlobalTypes.SELECTED_ASHA_AREAS);
        selectedAshaAreas = gson.fromJson(selectedAshaAreasString, new TypeToken<List<Integer>>() {
        }.getType());
        String selectedVillageString = getIntent().getStringExtra(GlobalTypes.SELECTED_VILLAGE_IDS);
        selectedVillageIds = gson.fromJson(selectedVillageString, new TypeToken<List<Integer>>() {
        }.getType());
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
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
        setTitle(UtilBean.getTitleText(LabelConstants.REPORTED_EVENT_CONF_ACTIVITY_TITLE));
    }

    private void initView() {
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        footerView = globalPanel.findViewById(DynamicUtils.ID_FOOTER);
        showProcessDialog();
        setServiceTypeScreen();
    }

    @Override
    public void onClick(View v) {
        setSubTitle(null);
        switch (screen) {
            case SET_SERVICE_TYPE:
                if (selectedServiceIndex != -1) {
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    retrieveNotifications();
                } else {
                    SewaUtil.generateToast(context, LabelConstants.TASK_SELECTION_ALERT);
                }
                break;
            case MEMBER_SELECTION_SCREEN:
                if (selectedMemeberIndex != -1) {
                    bodyLayoutContainer.removeAllViews();
                    selectedNotification = notificationMapWithCounter.get(selectedMemeberIndex);
                    startConfirmationActivity();
                } else if (notificationMobDataBeans.isEmpty()) {
                    setServiceTypeScreen();
                } else {
                    SewaUtil.generateToast(context, LabelConstants.PLEASE_SELECT_A_MEMBER);
                }
            default:
        }
    }

    @Background
    public void setServiceTypeScreen() {
        notificationCountMap = notificationService.retrieveCountForAshaReportedEventNotificationType(selectedVillageIds, selectedAshaAreas);
        viewServiceTypeScreen();
    }

    private String setNotificationCountFromMap(Integer count) {
        return count != null ? count.toString() : null;
    }

    @UiThread
    public void viewServiceTypeScreen() {
        screen = SET_SERVICE_TYPE;
        bodyLayoutContainer.removeAllViews();

        List<ListItemDataBean> confirmationlist = new ArrayList<>();
        confirmationlist.add(new ListItemDataBean(-1, UtilBean.getFullFormOfEntity().get(NotificationConstants.NOTIFICATION_FHW_PREGNANCY_CONF),
                setNotificationCountFromMap(notificationCountMap.get(NotificationConstants.NOTIFICATION_FHW_PREGNANCY_CONF))));
        confirmationlist.add(new ListItemDataBean(-1, UtilBean.getFullFormOfEntity().get(NotificationConstants.NOTIFICATION_FHW_DELIVERY_CONF),
                setNotificationCountFromMap(notificationCountMap.get(NotificationConstants.NOTIFICATION_FHW_DELIVERY_CONF))));
        confirmationlist.add(new ListItemDataBean(-1, UtilBean.getFullFormOfEntity().get(NotificationConstants.NOTIFICATION_FHW_DEATH_CONF),
                setNotificationCountFromMap(notificationCountMap.get(NotificationConstants.NOTIFICATION_FHW_DEATH_CONF))));
        confirmationlist.add(new ListItemDataBean(-1, UtilBean.getFullFormOfEntity().get(NotificationConstants.NOTIFICATION_FHW_MEMBER_MIGRATION),
                setNotificationCountFromMap(notificationCountMap.get(NotificationConstants.NOTIFICATION_FHW_MEMBER_MIGRATION))));
        confirmationlist.add(new ListItemDataBean(-1, UtilBean.getFullFormOfEntity().get(NotificationConstants.NOTIFICATION_FHW_FAMILY_MIGRATION),
                setNotificationCountFromMap(notificationCountMap.get(NotificationConstants.NOTIFICATION_FHW_FAMILY_MIGRATION))));
        confirmationlist.add(new ListItemDataBean(-1, UtilBean.getFullFormOfEntity().get(NotificationConstants.NOTIFICATION_FHW_FAMILY_SPLIT),
                setNotificationCountFromMap(notificationCountMap.get(NotificationConstants.NOTIFICATION_FHW_FAMILY_SPLIT))));

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            selectedServiceIndex = position;
            onClick(nextButton);
            footerView.setVisibility(View.VISIBLE);
        };

        PagingListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, confirmationlist, R.layout.listview_row_notification, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
        footerView.setVisibility(View.GONE);
        hideProcessDialog();
    }

    @Background
    public void retrieveNotifications() {
        try {
            offset = 0;
            notificationMobDataBeans = notificationService.retrieveNotificationsForUser(selectedVillageIds, selectedAshaAreas,
                    servicesList.get(selectedServiceIndex), null, limit, offset, qrScanFilter);
            offset = offset + limit;
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }
        setMemberSelectionScreen();
        hideProcessDialog();
    }

    @UiThread
    public void setMemberSelectionScreen() {
        screen = MEMBER_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        if (notificationMobDataBeans != null && !notificationMobDataBeans.isEmpty()) {
            MaterialTextView textView = MyStaticComponents.getListTitleView(context, UtilBean.getFullFormOfEntity().get(servicesList.get(selectedServiceIndex)));
            bodyLayoutContainer.addView(textView);

            List<ListItemDataBean> list = getList(notificationMobDataBeans);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedMemeberIndex = position;
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_date, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(GlobalTypes.EVENT_NEXT);
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context, LabelConstants.NO_NOTIFICATIONS));
            nextButton.setText(GlobalTypes.EVENT_OKAY);
        }

        hideProcessDialog();
    }

    private List<ListItemDataBean> getList(List<NotificationMobDataBean> notificationMobDataBeans) {
        String date = "";
        String id;
        String name;
        List<ListItemDataBean> memberList = new ArrayList<>();
        notificationMapWithCounter = new ArrayList<>();
        for (NotificationMobDataBean notificationMobDataBean : notificationMobDataBeans) {
            if (notificationMobDataBean.getExpectedDueDate() != null) {
                date = UtilBean.convertDateToString(notificationMobDataBean.getExpectedDueDate(), false, false, true);
                if (new Date(notificationMobDataBean.getExpectedDueDate()).before(new Date())) {
                    notificationMobDataBean.setOverdueFlag(true);
                }
            } else {
                notificationMobDataBean.setOverdueFlag(false);
            }

            if (notificationMobDataBean.getMemberId() != null) {
                MemberBean memberBean = fhsService.retrieveMemberBeanByActualId(notificationMobDataBean.getMemberId());
                if (memberBean == null) {
                    continue;
                }
                id = memberBean.getUniqueHealthId();
                name = UtilBean.getMemberFullName(memberBean);
            } else if (notificationMobDataBean.getFamilyId() != null) {
                FamilyBean familyBean = fhsService.retrieveFamilyBeanByActualId(notificationMobDataBean.getFamilyId());
                if (familyBean == null) {
                    continue;
                }
                ArrayList<String> list = new ArrayList<>();
                list.add(familyBean.getFamilyId());
                Map<String, MemberDataBean> headMemberDataBeansByFamilyId = fhsService.retrieveHeadMemberDataBeansByFamilyId(list);
                MemberDataBean head = headMemberDataBeansByFamilyId.get(familyBean.getFamilyId());
                id = familyBean.getFamilyId();
                if (head != null) {
                    name = UtilBean.getMemberFullName(head);
                } else {
                    name = "";
                }
            } else {
                continue;
            }

            notificationMapWithCounter.add(notificationMobDataBean);
            memberList.add(new ListItemDataBean(date, id, name, null, notificationMobDataBean.getOverdueFlag()));
        }
        return memberList;
    }

    private void startConfirmationActivity() {
        Intent intent;
        switch (selectedNotification.getTask()) {
            case NotificationConstants.NOTIFICATION_FHW_DEATH_CONF:
                intent = new Intent(context, FhwDeathConfirmationActivity_.class);
                intent.putExtra(GlobalTypes.NOTIFICATION, new Gson().toJson(selectedNotification));
                startActivityForResult(intent, REQUEST_CODE_FOR_REPORTED_EVENT_CONF_ACTIVITY);
                break;
            case NotificationConstants.NOTIFICATION_FHW_DELIVERY_CONF:
                intent = new Intent(context, FhwDeliveryConfirmationActivity_.class);
                intent.putExtra(GlobalTypes.NOTIFICATION, new Gson().toJson(selectedNotification));
                startActivityForResult(intent, REQUEST_CODE_FOR_REPORTED_EVENT_CONF_ACTIVITY);
                break;
            case NotificationConstants.NOTIFICATION_FHW_PREGNANCY_CONF:
                try {
                    formMetaDataUtil.setMetaDataForRchFormByFormType(selectedNotification.getTask(),
                            selectedNotification.getMemberId().toString(), null, selectedNotification, sharedPref);
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
                intent = new Intent(context, DynamicFormActivity_.class);
                String nextEntity = selectedNotification.getTask();
                intent.putExtra(SewaConstants.ENTITY, nextEntity);
                startActivityForResult(intent, REQUEST_CODE_FOR_REPORTED_EVENT_CONF_ACTIVITY);
                break;
            case NotificationConstants.NOTIFICATION_FHW_MEMBER_MIGRATION:
                intent = new Intent(context, FhwMemberMigrationConfirmationActivity_.class);
                intent.putExtra(GlobalTypes.NOTIFICATION, new Gson().toJson(selectedNotification));
                startActivityForResult(intent, REQUEST_CODE_FOR_REPORTED_EVENT_CONF_ACTIVITY);
                break;
            case NotificationConstants.NOTIFICATION_FHW_FAMILY_MIGRATION:
            case NotificationConstants.NOTIFICATION_FHW_FAMILY_SPLIT:
                intent = new Intent(context, FhwFamilyMigrationConfirmationActivity_.class);
                intent.putExtra(GlobalTypes.NOTIFICATION, new Gson().toJson(selectedNotification));
                startActivityForResult(intent, REQUEST_CODE_FOR_REPORTED_EVENT_CONF_ACTIVITY);
                break;
            default:
                break;
        }
        selectedMemeberIndex = -1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_REPORTED_EVENT_CONF_ACTIVITY) {
            showProcessDialog();
            retrieveNotifications();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        selectedMemeberIndex = -1;
        if(screen == null || screen.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            if (MEMBER_SELECTION_SCREEN.equals(screen)) {
                showProcessDialog();
                bodyLayoutContainer.removeAllViews();
                selectedServiceIndex = -1;
                setServiceTypeScreen();
            } else if (SET_SERVICE_TYPE.equals(screen)) {
                finish();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onLoadMoreItems() {
        onLoadMoreBackground();
    }

    @Background
    public void onLoadMoreBackground() {
        List<NotificationMobDataBean> notificationMobDataBeansList = null;
        try {
            notificationMobDataBeansList = notificationService.retrieveNotificationsForUser(selectedVillageIds, selectedAshaAreas,
                    servicesList.get(selectedServiceIndex), null, limit, offset, qrScanFilter);
            offset = offset + limit;
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }
        onLoadMoreUi(notificationMobDataBeansList);
    }

    @UiThread
    public void onLoadMoreUi(List<NotificationMobDataBean> notificationMobDataBeansList) {
        if (notificationMobDataBeansList != null && !notificationMobDataBeansList.isEmpty()) {
            notificationMobDataBeans.addAll(notificationMobDataBeansList);
            List<ListItemDataBean> stringList = getList(notificationMobDataBeansList);
            paginatedListView.onFinishLoadingWithItem(true, stringList);
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
        }
    }
}
