package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.NotificationConstants;
import com.argusoft.sewa.android.app.core.impl.NotificationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.FormMetaDataUtil;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.button.MaterialButton;
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
 * Created by prateek on 7/26/19
 */
@EActivity
public class NotificationOtherServicesActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    private static final Integer REQUEST_CODE_FOR_OTHER_NOTIFICATIONS_ACTIVITY = 200;
    private static final String TYPE_SELECTION_SCREEN = "typeSelectionScreen";
    private static final String ALERT_SCREEN = "alertScreen";
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    NotificationServiceImpl notificationService;
    @Bean
    FormMetaDataUtil formMetaDataUtil;
    private LinearLayout bodyLayoutContainer;
    private List<Integer> selectedAshaAreas = new ArrayList<>();
    private List<Integer> selectedVillageIds = new ArrayList<>();
    private Gson gson = new Gson();
    private LinearLayout globalPanel;
    private ArrayList<String> serviceList;
    private int selectedTypeIndex = -1;
    private String screen;
    private long limit = 30;
    private long offset;
    private List<NotificationMobDataBean> notificationMobDataBeans;
    private List<NotificationMobDataBean> notificationMapWithCounter = new ArrayList<>();
    private Map<String, Integer> notificationCountMap;
    private PagingListView paginatedListView;
    private int selectedNotificationIndex = -1;
    private MaterialButton nextButton;
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
        setTitle(UtilBean.getTitleText(LabelConstants.OTHER_NOTIFICATIONS_ACTIVITY_TITLE));
    }

    private void initView() {
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        footerView = globalPanel.findViewById(DynamicUtils.ID_FOOTER);
        showProcessDialog();
        setTypeScreen();
    }

    @Background
    public void setTypeScreen() {
        notificationCountMap = notificationService.retrieveCountForOtherServicesNotificationType(selectedVillageIds, selectedAshaAreas);
        viewTypeScreen();
    }

    private String setNotificationCountFromMap(Integer count) {
        return count != null ? count.toString() : null;
    }

    @UiThread
    public void viewTypeScreen() {
        screen = TYPE_SELECTION_SCREEN;

        serviceList = new ArrayList<>();
        serviceList.add(NotificationConstants.FHW_NOTIFICATION_TT2);
        serviceList.add(NotificationConstants.FHW_NOTIFICATION_IRON_SUCROSE);

        List<ListItemDataBean> list = new ArrayList<>();
        list.add(new ListItemDataBean(-1, UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_NOTIFICATION_TT2),
                setNotificationCountFromMap(notificationCountMap.get(NotificationConstants.FHW_NOTIFICATION_TT2))));
        list.add(new ListItemDataBean(-1, UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_NOTIFICATION_IRON_SUCROSE),
                setNotificationCountFromMap(notificationCountMap.get(NotificationConstants.FHW_NOTIFICATION_IRON_SUCROSE))));

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            selectedTypeIndex = position;
            onClick(nextButton);
            footerView.setVisibility(View.VISIBLE);
        };
        PagingListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_notification, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
        footerView.setVisibility(View.GONE);
        hideProcessDialog();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            setSubTitle(null);
            switch (screen) {
                case TYPE_SELECTION_SCREEN:
                    showProcessDialog();
                    footerView.setVisibility(View.VISIBLE);
                    retrieveNotifications();
                    break;
                case ALERT_SCREEN:
                    if(notificationMobDataBeans == null || notificationMobDataBeans.isEmpty()){
                        bodyLayoutContainer.removeAllViews();
                        viewTypeScreen();
                        return;
                    }
                    if (selectedNotificationIndex != -1) {
                        bodyLayoutContainer.removeAllViews();
                        NotificationMobDataBean selectedNotification = notificationMapWithCounter.get(selectedNotificationIndex);
                        formMetaDataUtil.setMetaDataForOtherNotificationsForm(selectedNotification);
                        Intent intent = new Intent(context, DynamicFormActivity_.class);
                        intent.putExtra(SewaConstants.ENTITY, selectedNotification.getTask());
                        startActivityForResult(intent, REQUEST_CODE_FOR_OTHER_NOTIFICATIONS_ACTIVITY);
                        selectedNotificationIndex = -1;
                    } else {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.TASK_SELECTION_ALERT));
                    }
                    break;
                default:
            }
        }
    }

    @Background
    public void retrieveNotifications() {
        try {
            offset = 0;
            if (selectedTypeIndex == 0) {
                notificationMobDataBeans = notificationService.retrieveNotificationsForUser(selectedVillageIds, selectedAshaAreas,
                        serviceList.get(0), null, limit, offset, qrScanFilter);
            } else if (selectedTypeIndex == 1) {
                notificationMobDataBeans = notificationService.retrieveNotificationsForUser(selectedVillageIds, selectedAshaAreas,
                        serviceList.get(1), null, limit, offset, qrScanFilter);
            }
            offset = offset + limit;
            setAlertScreen();
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }
    }

    @UiThread
    public void setAlertScreen() {
        screen = ALERT_SCREEN;
        bodyLayoutContainer.removeAllViews();
        MaterialTextView textView;

        if (notificationMobDataBeans != null && !notificationMobDataBeans.isEmpty()) {
            textView = MyStaticComponents.getListTitleView(context, LabelConstants.SELECT_ALERT);
            bodyLayoutContainer.addView(textView);

            List<ListItemDataBean> list = getNotificationList(notificationMobDataBeans);

            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedNotificationIndex = position;
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_date, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            textView = MyStaticComponents.generateInstructionView(context, LabelConstants.NO_NOTIFICATION_ALERT_WITH_GRATITUDE_FOR_WORK);
            bodyLayoutContainer.addView(textView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }
        hideProcessDialog();
    }

    private List<ListItemDataBean> getNotificationList(List<NotificationMobDataBean> notificationMobDataBeans) {
        List<Long> memberActualIds = new ArrayList<>();
        for (NotificationMobDataBean notificationMobDataBean : notificationMobDataBeans) {
            if (notificationMobDataBean.getMemberId() != null) {
                memberActualIds.add(notificationMobDataBean.getMemberId());
            }
        }

        Map<Long, MemberBean> memberMapWithActualIdAsKey = fhsService.retrieveMemberBeansMapByActualIds(memberActualIds);
        List<ListItemDataBean> list = new ArrayList<>();
        String date = "";
        for (NotificationMobDataBean notificationMobDataBean : notificationMobDataBeans) {
            if (notificationMobDataBean.getExpectedDueDate() != null) {
                date = UtilBean.convertDateToString(notificationMobDataBean.getExpectedDueDate(), false, false, true);
                if (new Date(notificationMobDataBean.getExpectedDueDate()).before(new Date())) {
                    notificationMobDataBean.setOverdueFlag(true);
                }
            } else {
                notificationMobDataBean.setOverdueFlag(false);
            }

            MemberBean memberBean = memberMapWithActualIdAsKey.get(notificationMobDataBean.getMemberId());
            if (memberBean != null) {
                list.add(new ListItemDataBean(date, memberBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberBean), null, notificationMobDataBean.getOverdueFlag()));
                notificationMapWithCounter.add(notificationMobDataBean);
            }
        }
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_OTHER_NOTIFICATIONS_ACTIVITY) {
            showProcessDialog();
            setAlertScreen();
            retrieveNotifications();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(screen == null || screen.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            selectedTypeIndex = -1;
            selectedNotificationIndex = -1;
            if (screen.equals(ALERT_SCREEN)) {
                bodyLayoutContainer.removeAllViews();
                setTypeScreen();
            } else {
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
        List<NotificationMobDataBean> notificationDataBeans = null;
        try {
            notificationDataBeans = notificationService.retrieveNotificationsForUser(selectedVillageIds, selectedAshaAreas,
                    serviceList.get(selectedTypeIndex), null, limit, offset, qrScanFilter);
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }
        offset = offset + limit;
        onLoadMoreUi(notificationDataBeans);
    }

    @UiThread
    public void onLoadMoreUi(List<NotificationMobDataBean> notificationDataBeans) {
        if (notificationDataBeans != null && !notificationDataBeans.isEmpty()) {
            notificationMobDataBeans.addAll(notificationDataBeans);
            offset = offset + limit;
            List<ListItemDataBean> stringList = getNotificationList(notificationDataBeans);
            paginatedListView.onFinishLoadingWithItem(true, stringList);
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
        }
    }
}
