package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import com.argusoft.sewa.android.app.util.Log;
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
import com.argusoft.sewa.android.app.core.impl.ImmunisationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.NotificationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.FormMetaDataUtil;
import com.argusoft.sewa.android.app.util.GlobalTypes;
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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * Created by prateek on 8/5/2019.
 */
@EActivity
public class NotificationAwwActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    private static final long DELAY = 500;
    private static final String AREA_SELECTION_SCREEN = "AreaSelectionScreen";
    private static final String NOTIFICATION_TYPE_SELECTION_SCREEN = "NotificationTypeSelectionScreen";
    private static final String NOTIFICATION_SELECTION_SCREEN = "NotificationSelectionScreen";
    private static final Integer REQUEST_CODE_FOR_NOTIFICATION_ACTIVITY = 200;
    private static final String TAG = "NotificationAwwActivity";

    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    NotificationServiceImpl notificationService;
    @Bean
    ImmunisationServiceImpl immunisationService;
    @Bean
    FormMetaDataUtil formMetaDataUtil;
    private long offset;
    private long limit = 30;
    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private String screenName;
    private List<LocationBean> ashaAreaList = new ArrayList<>();
    private List<Integer> selectedAreaIds = new ArrayList<>();
    private Map<String, Integer> notificationsCountMap;
    private List<String> notificationCodeWithRadioButtonIdMap = new ArrayList<>();
    private List<NotificationMobDataBean> notificationBeanList = new LinkedList<>();
    private Map<Long, MemberBean> memberMapWithActualIdAsKey = new HashMap<>();
    private String selectedNotificationCode = null;
    private NotificationMobDataBean selectedNotification;
    private Spinner ashaAreaSpinner;
    private List<ListItemDataBean> serviceList;
    private TextInputLayout searchBox;
    private Timer timer = new Timer();
    private int selectedVisitIndex = -1;
    private PagingListView paginatedListView;
    private int selectedNotificationIndex = -1;
    private CharSequence searchtring;
    private String notificationCode;
    private LinearLayout footerView;
    private MaterialTextView listTitle;
    private MaterialTextView noNotificationTextView;
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
        if (!SharedStructureData.isLogin) {
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        if (globalPanel != null) {
            setContentView(globalPanel);
        }
        setTitle(UtilBean.getTitleText(LabelConstants.NOTIFICATION_ACTIVITY_TITLE));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screenName) {
                case AREA_SELECTION_SCREEN:
                    String selectedArea;
                    if (ashaAreaList.size() == 1) {
                        selectedArea = ashaAreaList.get(0).getName();
                    } else {
                        selectedArea = ashaAreaSpinner.getSelectedItem().toString();
                    }

                            if (!selectedArea.equals(LabelConstants.ALL) && !selectedArea.equals(UtilBean.getMyLabel(LabelConstants.ALL))) {
                        for (LocationBean locationBean : ashaAreaList) {
                            if (selectedArea.equals(locationBean.getName())) {
                                selectedAreaIds.add(locationBean.getActualID());
                                break;
                            }
                        }
                    } else {
                        for (LocationBean locationBean : ashaAreaList) {
                            selectedAreaIds.add(locationBean.getActualID());
                        }
                    }
                    showProcessDialog();
                    retrieveCountsForEachNotificationType();
                    break;
                case NOTIFICATION_SELECTION_SCREEN:

                    if (notificationBeanList == null || notificationBeanList.isEmpty()) {
                        navigateToHomeScreen(false);
                        return;
                    }
                    if (selectedNotificationIndex == -1) {
                        SewaUtil.generateToast(this, LabelConstants.TASK_SELECTION_ALERT);
                        return;
                    }
                    selectedNotification = notificationBeanList.get(selectedNotificationIndex);
                    startDynamicFormActivity();
                    break;

                default:
            }
        }
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
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.NOTIFICATION_ACTIVITY_TITLE);
        startActivityForResult(myIntent, ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE);
    }

    @UiThread
    public void addAshaAreaSelectionSpinner() {
        screenName = AREA_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        if (ashaAreaSpinner == null) {
            String[] arrayOfOptions = new String[ashaAreaList.size() + 1];
            arrayOfOptions[0] = LabelConstants.ALL;
            int i = 1;
            for (LocationBean locationBean : ashaAreaList) {
                arrayOfOptions[i] = locationBean.getName();
                i++;
            }
            ashaAreaSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 3);
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.SELECT_ASHA_AREA));
        bodyLayoutContainer.addView(ashaAreaSpinner);
        hideProcessDialog();
    }

    @Background
    public void retrieveCountsForEachNotificationType() {
        notificationsCountMap = notificationService.retrieveCountForNotificationType(null, selectedAreaIds);
        addNotificationTypeScreen();
    }


    @UiThread
    public void addSearchTextBox() {
        bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
        if (searchBox == null) {
            searchBox = MyStaticComponents.getEditTextWithQrScan(this, LabelConstants.MEMBER_ID_OR_NAME_OR_HEALTH_ID_TO_SEARCH, 1, 15, 1);
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
                                            runOnUiThread(() -> retrieveNotificationListFromDB(selectedNotificationCode, s, null));
                                        } else if (s == null || s.length() == 0) {
                                            runOnUiThread(() -> {
                                                showProcessDialog();
                                                retrieveNotificationListFromDB(selectedNotificationCode, null, null);
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

        bodyLayoutContainer.addView(searchBox);
        bodyLayoutContainer.addView(MyStaticComponents.getOrTextView(context));
    }

    private String setNotificationCountFromMap(Integer count) {
        return count != null ? count.toString() : null;
    }

    @UiThread
    public void addNotificationTypeScreen() {
        screenName = NOTIFICATION_TYPE_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        footerView.setVisibility(View.GONE);

        if (serviceList == null) {
            serviceList = new ArrayList<>();
            serviceList.add(new ListItemDataBean(-1,
                    UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(FormConstants.TECHO_AWW_CS)),
                    setNotificationCountFromMap(notificationsCountMap.get(FormConstants.TECHO_AWW_CS))));
            notificationCodeWithRadioButtonIdMap.add(FormConstants.TECHO_AWW_CS);
        }

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            selectedVisitIndex = position;
            offset = 0;
            if (selectedVisitIndex != -1) {
                selectedNotificationCode = notificationCodeWithRadioButtonIdMap.get(selectedVisitIndex);
                bodyLayoutContainer.removeAllViews();
                showProcessDialog();
                addSearchTextBox();
                retrieveNotificationListFromDB(selectedNotificationCode, null, null);
            } else {
                SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.SELECT_NOTIFICATION_TYPE));
            }
        };

        PagingListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, serviceList, R.layout.listview_row_notification, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
        hideProcessDialog();
    }

    @Background
    public void retrieveNotificationListFromDB(String notificationCode, CharSequence searchString, String qrData) {
        searchtring = searchString;
        selectedNotificationIndex = -1;
        qrScanFilter = SewaUtil.setQrScanFilterData(qrData);
        this.notificationCode = notificationCode;
        try {
            offset = 0;
            notificationBeanList = notificationService.retrieveNotificationsForUser(null, selectedAreaIds, notificationCode, searchString, limit, offset, qrScanFilter);
            offset = offset + limit;
        } catch (SQLException e) {
            Logger.getLogger(NotificationAshaActivity.class.getName()).log(Level.SEVERE, null, e);
        }

        boolean isSearch = searchString != null;
        addNotificationScreen(isSearch);
    }

    @UiThread
    public void addNotificationScreen(boolean isSearch) {
        screenName = NOTIFICATION_SELECTION_SCREEN;
        bodyLayoutContainer.removeView(paginatedListView);
        bodyLayoutContainer.removeView(listTitle);
        bodyLayoutContainer.removeView(noNotificationTextView);
        footerView.setVisibility(View.VISIBLE);

        if (notificationBeanList != null && !notificationBeanList.isEmpty()) {
            listTitle = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_NOTIFICATION);
            bodyLayoutContainer.addView(listTitle);

            List<ListItemDataBean> list = addNotificationForLoop(notificationBeanList);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedNotificationIndex = position;
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_date, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            noNotificationTextView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_NOTIFICATION_ALERT_WITH_GRATITUDE_FOR_WORK);
            bodyLayoutContainer.addView(noNotificationTextView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }
        hideProcessDialog();
    }

    private List<ListItemDataBean> addNotificationForLoop(List<NotificationMobDataBean> notificationBeans) {

        List<Long> memberActualIds = new ArrayList<>();
        for (NotificationMobDataBean notificationMobDataBean : notificationBeans) {
            if (notificationMobDataBean.getMemberId() != null) {
                memberActualIds.add(notificationMobDataBean.getMemberId());
            }
        }

        memberMapWithActualIdAsKey.putAll(fhsService.retrieveMemberBeansMapByActualIds(memberActualIds));

        String date = null;
        String visit = null;
        List<ListItemDataBean> list = new ArrayList<>();
        for (NotificationMobDataBean notificationMobDataBean : notificationBeans) {
            notificationMobDataBean.setOverdueFlag(notificationMobDataBean.getExpectedDueDate() != null && new Date(notificationMobDataBean.getExpectedDueDate()).before(new Date()));
            MemberBean memberBean = memberMapWithActualIdAsKey.get(notificationMobDataBean.getMemberId());
            if (memberBean == null) {
                continue;
            }

            if (notificationMobDataBean.getExpectedDueDate() != null) {
                date = UtilBean.convertDateToString(notificationMobDataBean.getExpectedDueDate(), false, false, true);
            }
            if (notificationMobDataBean.getCustomField() != null && !notificationMobDataBean.getCustomField().equals("0")) {
                visit = UtilBean.getMyLabel(LabelConstants.VISIT) + " " + notificationMobDataBean.getCustomField();
            }
            list.add(new ListItemDataBean(date, memberBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberBean), visit, notificationMobDataBean.getOverdueFlag()));
        }
        return list;
    }

    private void setMetadataForFormByFormType(String formType) {
        MemberBean memberBean = memberMapWithActualIdAsKey.get(selectedNotification.getMemberId());
        if (memberBean != null) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            formMetaDataUtil.setMetaDataForRchFormByFormType(formType, memberBean.getActualId(), memberBean.getFamilyId(), selectedNotification, sharedPref);
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
                LabelConstants.ON_NOTIFICATION_ACTIVITY_CLOSE_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    private void startDynamicFormActivity() {
        showProcessDialog();
        final Intent myIntent = new Intent(context, DynamicFormActivity_.class);
        new Thread() {
            @Override
            public void run() {
                String nextEntity = selectedNotification.getTask();
                setMetadataForFormByFormType(nextEntity);
                if (nextEntity != null) {
                    myIntent.putExtra(SewaConstants.ENTITY, nextEntity);
                    startActivityForResult(myIntent, REQUEST_CODE_FOR_NOTIFICATION_ACTIVITY);
                }
                hideProcessDialog();
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult resultForQRScanner = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        offset = 0;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_NOTIFICATION_ACTIVITY) {
            try {
                notificationBeanList = notificationService.retrieveNotificationsForUser(null, selectedAreaIds, selectedNotificationCode,
                        null, limit, offset, qrScanFilter);
                offset = offset + limit;
                selectedNotificationIndex = -1;
                if (notificationBeanList != null && !notificationBeanList.isEmpty()) {
                    bodyLayoutContainer.removeAllViews();
                    showProcessDialog();
                    addSearchTextBox();
                    addNotificationScreen(false);
                } else {
                    showProcessDialog();
                    retrieveCountsForEachNotificationType();
                }
            } catch (SQLException e) {
                navigateToHomeScreen(false);
                Logger.getLogger(NotificationAshaActivity.class.getName()).log(Level.SEVERE, null, e);
            }
        } else if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                selectedAreaIds.clear();
                selectedAreaIds.add(Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID)));
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
                retrieveNotificationListFromDB(selectedNotificationCode, null, scanningData);
            }
        } else {
            navigateToHomeScreen(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(screenName == null || screenName.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }
        footerView.setVisibility(View.VISIBLE);
        if (item.getItemId() == android.R.id.home) {
            switch (screenName) {
                case AREA_SELECTION_SCREEN:
                    finish();
                    break;
                case NOTIFICATION_TYPE_SELECTION_SCREEN:
                    startLocationSelectionActivity();
                    break;
                case NOTIFICATION_SELECTION_SCREEN:
                    offset = 0;
                    selectedVisitIndex = -1;
                    showProcessDialog();
                    retrieveCountsForEachNotificationType();
                    nextButton.setText(GlobalTypes.EVENT_NEXT);
                    break;

                default:
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
        List<NotificationMobDataBean> notificationDataBeanList = null;
        try {
            notificationDataBeanList = notificationService.retrieveNotificationsForUser(null, selectedAreaIds, notificationCode, searchtring, limit, offset, qrScanFilter);
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }
        offset = offset + limit;
        onLoadMoreUi(notificationDataBeanList);
    }

    @UiThread
    public void onLoadMoreUi(List<NotificationMobDataBean> notificationDataBeanList) {
        if (notificationDataBeanList != null && !notificationDataBeanList.isEmpty()) {
            notificationBeanList.addAll(notificationDataBeanList);
            List<ListItemDataBean> stringList = addNotificationForLoop(notificationDataBeanList);
            paginatedListView.onFinishLoadingWithItem(true, stringList);
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
        }
    }
}
