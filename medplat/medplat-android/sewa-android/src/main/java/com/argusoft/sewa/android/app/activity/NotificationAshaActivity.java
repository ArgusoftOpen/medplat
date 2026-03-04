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
import com.argusoft.sewa.android.app.core.impl.NotificationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.exception.DataException;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by prateek on 8/5/2019.
 */
@EActivity
public class NotificationAshaActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    private static final String AREA_SELECTION_SCREEN = "AreaSelectionScreen";
    private static final String NOTIFICATION_TYPE_SELECTION_SCREEN = "NotificationTypeSelectionScreen";
    private static final String NOTIFICATION_SELECTION_SCREEN = "NotificationSelectionScreen";
    private static final String READ_ONLY_NOTIFICATION_SCREEN = "ReadOnlyNotificationScreen";
    private static final Integer REQUEST_CODE_FOR_NOTIFICATION_ACTIVITY = 200;
    private static final Integer REQUEST_CODE_FOR_OTHER_NOTIFICATION_ACTIVITY = 201;
    private static final long DELAY = 500;
    private static final long LIMIT = 30;
    private static final String TAG = "NotificationAshActivity";

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

    private View.OnClickListener onClickListener = this;
    private long offset = 0;

    private boolean isFromLogin;
    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private String screenName;
    private List<LocationBean> ashaAreaList = new ArrayList<>();
    private List<Integer> selectedAreaIds = new ArrayList<>();
    private Map<String, Integer> notificationsCountMap;
    private List<NotificationMobDataBean> notificationBeanList = new LinkedList<>();
    private Map<Long, MemberBean> memberMapWithActualIdAsKey = new HashMap<>();
    private String selectedNotificationCode = null;
    private NotificationMobDataBean selectedNotification;
    private PagingListView notificationListView;
    private CharSequence searchString;
    private Spinner ashaAreaSpinner;
    private Timer timer = new Timer();
    private int selectedNotificationTypeIndex = -1;
    private List<String> notificationGroupCodes = new ArrayList<>();
    private MaterialTextView noNotificationAvailabelView;
    private LinearLayout footerView;
    private MaterialTextView listTitle;
    private LinkedHashMap<String, String> qrScanFilter = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
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
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        setTitle(UtilBean.getTitleText(LabelConstants.NOTIFICATION_ACTIVITY_TITLE));
    }

    @Override
    public void onClick(View v) {
        setSubTitle(null);
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screenName) {
                case AREA_SELECTION_SCREEN:
                    String selectedArea;
                    if (ashaAreaList.size() == 1) {
                        selectedArea = ashaAreaList.get(0).getName();
                    } else {
                        selectedArea = ashaAreaSpinner.getSelectedItem().toString();
                    }

                    if (!selectedArea.equals(LabelConstants.ALL) && !selectedArea.equals(UtilBean.getMyLabel("All"))) {
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

                    if (selectedNotification == null) {
                        SewaUtil.generateToast(context, LabelConstants.NOTIFICATION_SELECTION_REQUIRED_ALERT);
                        return;
                    }

                    if (selectedNotification.getTask().equals(NotificationConstants.ASHA_NOTIFICATION_READ_ONLY)) {
                        showReadOnlyNotification();
                    } else {
                        startDynamicFormActivity();
                    }
                    break;

                case READ_ONLY_NOTIFICATION_SCREEN:
                    bodyLayoutContainer.removeAllViews();
                    showProcessDialog();
                    bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                    addSearchTextBox();
                    retrieveNotificationListFromDB(selectedNotificationCode, null, null);
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
        footerView = globalPanel.findViewById(DynamicUtils.ID_FOOTER);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
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
        setSubTitle(null);

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

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.SELECT_ASHA_AREA));
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
        if (selectedNotificationCode.equals(NotificationConstants.ASHA_NOTIFICATION_READ_ONLY)) {
            return;
        }

        TextInputLayout searchBox = MyStaticComponents.getEditTextWithQrScan(this, LabelConstants.MEMBER_ID_OR_NAME_OR_HEALTH_ID_TO_SEARCH, 1, 10, 1);
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
        setSubTitle(null);

        notificationGroupCodes.add(FormConstants.ASHA_LMPFU);
        notificationGroupCodes.add(FormConstants.ASHA_ANC);
        notificationGroupCodes.add(FormConstants.ASHA_PNC);
        notificationGroupCodes.add(FormConstants.ASHA_CS);
//        notificationGroupCodes.add(FormConstants.ASHA_SAM_SCREENING);
//        notificationGroupCodes.add(FormConstants.CMAM_FOLLOWUP);
//        notificationGroupCodes.add(NotificationConstants.ASHA_NOTIFICATION_READ_ONLY);
        notificationGroupCodes.add(NotificationConstants.FHW_WORK_PLAN_MAMTA_DAY);

        List<ListItemDataBean> listItems = new ArrayList<>();

        listItems.add(new ListItemDataBean(-1,
                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(FormConstants.ASHA_LMPFU)),
                setNotificationCountFromMap(notificationsCountMap.get(FormConstants.ASHA_LMPFU))));

        listItems.add(new ListItemDataBean(-1,
                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(FormConstants.ASHA_ANC)),
                setNotificationCountFromMap(notificationsCountMap.get(FormConstants.ASHA_ANC))));

        listItems.add(new ListItemDataBean(-1,
                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(FormConstants.ASHA_PNC)),
                setNotificationCountFromMap(notificationsCountMap.get(FormConstants.ASHA_PNC))));

        listItems.add(new ListItemDataBean(-1,
                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(FormConstants.ASHA_CS)),
                setNotificationCountFromMap(notificationsCountMap.get(FormConstants.ASHA_CS))));

       /* listItems.add(new ListItemDataBean(-1,
                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(FormConstants.ASHA_SAM_SCREENING)),
                setNotificationCountFromMap(notificationsCountMap.get(FormConstants.ASHA_SAM_SCREENING))));

        listItems.add(new ListItemDataBean(-1,
                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(FormConstants.CMAM_FOLLOWUP)),
                setNotificationCountFromMap(notificationsCountMap.get(FormConstants.CMAM_FOLLOWUP))));

        listItems.add(new ListItemDataBean(-1,
                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.ASHA_NOTIFICATION_READ_ONLY)),
                setNotificationCountFromMap(notificationsCountMap.get(NotificationConstants.ASHA_NOTIFICATION_READ_ONLY))));
*/
        listItems.add(new ListItemDataBean(-1,
                UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(NotificationConstants.FHW_WORK_PLAN_MAMTA_DAY)), null));

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            offset = 0;
            selectedNotificationTypeIndex = position;
            if (selectedNotificationTypeIndex != -1) {
                selectedNotificationCode = notificationGroupCodes.get(selectedNotificationTypeIndex);
                if (selectedNotificationCode.equals(NotificationConstants.FHW_WORK_PLAN_MAMTA_DAY)) {
                    Intent intent = new Intent(context, MamtaDayActivity_.class);
                    intent.putExtra(GlobalTypes.SELECTED_ASHA_AREAS, new Gson().toJson(selectedAreaIds));
                    intent.putExtra(GlobalTypes.SELECTED_VILLAGE_IDS, new Gson().toJson(new ArrayList<Integer>()));
                    intent.putExtra("role", 24L);
                    startActivityForResult(intent, REQUEST_CODE_FOR_OTHER_NOTIFICATION_ACTIVITY);
                } else {
                    bodyLayoutContainer.removeAllViews();
                    showProcessDialog();
                    bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                    addSearchTextBox();
                    retrieveNotificationListFromDB(selectedNotificationCode, null, null);
                    footerView.setVisibility(View.VISIBLE);
                }
            } else {
                SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.SELECT_NOTIFICATION_TYPE));
            }
        };

        PagingListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, listItems, R.layout.listview_row_notification, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
        hideProcessDialog();
    }

    @Background
    public void retrieveNotificationListFromDB(String notificationCode, CharSequence searchString, String qrData) {
        offset = 0;
        this.searchString = searchString;
        qrScanFilter = SewaUtil.setQrScanFilterData(qrData);
        try {
            notificationBeanList = notificationService.retrieveNotificationsForUser(null, selectedAreaIds, notificationCode, searchString, LIMIT, offset, qrScanFilter);
            offset = offset + LIMIT;
        } catch (SQLException e) {
            Logger.getLogger(NotificationAshaActivity.class.getName()).log(Level.SEVERE, null, e);
        }

        boolean isSearch = searchString != null;
        addNotificationScreen(isSearch);
    }

    @UiThread
    public void addNotificationScreen(boolean isSearch) {
        screenName = NOTIFICATION_SELECTION_SCREEN;
        selectedNotification = null;
        bodyLayoutContainer.removeView(listTitle);
        bodyLayoutContainer.removeView(notificationListView);
        bodyLayoutContainer.removeView(noNotificationAvailabelView);
        if (notificationBeanList == null || notificationBeanList.isEmpty()) {
            noNotificationAvailabelView = MyStaticComponents.generateInstructionView(context, LabelConstants.NO_NOTIFICATION_ALERT_WITH_GRATITUDE_FOR_WORK);
            bodyLayoutContainer.addView(noNotificationAvailabelView);
            nextButton.setText(GlobalTypes.EVENT_OKAY);
            nextButton.setOnClickListener(v -> addNotificationTypeScreen());
        } else {
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
                if (notificationBeanList.size() > position)
                    selectedNotification = notificationBeanList.get(position);
            };

            List<ListItemDataBean> stringList = addNotificationForLoop(notificationBeanList);
            listTitle = MyStaticComponents.getListTitleView(context, LabelConstants.SELECT_FROM_LIST);
            bodyLayoutContainer.addView(listTitle);

            if (selectedNotificationCode.equals(NotificationConstants.ASHA_NOTIFICATION_READ_ONLY)) {
                notificationListView = MyStaticComponents.getPaginatedListViewWithItem(context, stringList, R.layout.listview_row_with_item, onItemClickListener, this);
            } else {
                notificationListView = MyStaticComponents.getPaginatedListViewWithItem(context, stringList, R.layout.listview_row_with_date, onItemClickListener, this);
            }
            bodyLayoutContainer.addView(notificationListView);
            nextButton.setText(GlobalTypes.EVENT_NEXT);
            nextButton.setOnClickListener(this);

        }
        if (!isSearch) {
            hideProcessDialog();
        }
    }

    private List<ListItemDataBean> addNotificationForLoop(List<NotificationMobDataBean> notificationBeans) {
        List<ListItemDataBean> items = new ArrayList<>();
        List<Long> memberActualIds = new ArrayList<>();
        for (NotificationMobDataBean notificationMobDataBean : notificationBeans) {
            if (notificationMobDataBean.getMemberId() != null) {
                memberActualIds.add(notificationMobDataBean.getMemberId());
            }
        }
        memberMapWithActualIdAsKey.putAll(fhsService.retrieveMemberBeansMapByActualIds(memberActualIds));

        List<NotificationMobDataBean> notificationsToRemove = new ArrayList<>();

        for (NotificationMobDataBean notificationMobDataBean : notificationBeans) {
            notificationMobDataBean.setOverdueFlag(notificationMobDataBean.getExpectedDueDate() != null && new Date(notificationMobDataBean.getExpectedDueDate()).before(new Date()));
            MemberBean memberBean = memberMapWithActualIdAsKey.get(notificationMobDataBean.getMemberId());
            String date = "";
            String visit = null;
            StringBuilder vaccines = new StringBuilder();
            switch (notificationMobDataBean.getTask()) {
                case FormConstants.ASHA_CS:
                    if (memberBean == null) {
                        notificationsToRemove.add(notificationMobDataBean);
                        continue;
                    }
                    if (memberBean.getDob() == null) {
                        notificationsToRemove.add(notificationMobDataBean);
                        continue;
                    }

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.YEAR, -5);

                    if (memberBean.getDob().before(cal.getTime())) {
                        notificationsToRemove.add(notificationMobDataBean);
                        continue;
                    }

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
                    items.add(new ListItemDataBean(date.isEmpty() ? null : date, memberBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberBean), visit, LabelConstants.DUE_VACCINES,
                            vaccines.substring(0, vaccines.length() - 2), notificationMobDataBean.getOverdueFlag()));
                    break;

                case FormConstants.ASHA_PNC:
                    if (memberBean == null) {
                        notificationsToRemove.add(notificationMobDataBean);
                        continue;
                    }
                    if (memberBean.getIsPregnantFlag() != null && memberBean.getIsPregnantFlag()) {
                        notificationsToRemove.add(notificationMobDataBean);
                        continue;
                    }

                    if (notificationMobDataBean.getExpectedDueDate() != null) {
                        date = UtilBean.convertDateToString(notificationMobDataBean.getExpectedDueDate(), false, false, true);
                    }
                    if (notificationMobDataBean.getCustomField() != null && !notificationMobDataBean.getCustomField().equals("0")) {
                        visit = UtilBean.getMyLabel(LabelConstants.VISIT) + " " + notificationMobDataBean.getCustomField();
                    }
                    items.add(new ListItemDataBean(date.isEmpty() ? null : date, memberBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberBean), visit,
                            notificationMobDataBean.getOverdueFlag()));
                    break;
                case NotificationConstants.ASHA_NOTIFICATION_READ_ONLY:
                    String info;
                    if (notificationMobDataBean.getHeader() != null) {
                        info = UtilBean.getMyLabel(notificationMobDataBean.getHeader());
                    } else {
                        info = UtilBean.getMyLabel(LabelConstants.INFORMATION);
                    }
                    items.add(new ListItemDataBean(info, null));
                    break;
                default:
                    if (memberBean == null) {
                        notificationsToRemove.add(notificationMobDataBean);
                        continue;
                    }

                    if (notificationMobDataBean.getExpectedDueDate() != null) {
                        date = UtilBean.convertDateToString(notificationMobDataBean.getExpectedDueDate(), false, false, true);
                    }
                    if (notificationMobDataBean.getCustomField() != null && !notificationMobDataBean.getCustomField().equals("0")) {
                        visit = UtilBean.getMyLabel(LabelConstants.VISIT) + " " + notificationMobDataBean.getCustomField();
                    }
                    items.add(new ListItemDataBean(date.isEmpty() ? null : date,
                            memberBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberBean), visit, notificationMobDataBean.getOverdueFlag()));
                    break;
            }
        }
        notificationBeanList.removeAll(notificationsToRemove);
        return items;
    }

    @UiThread
    public void showReadOnlyNotification() {
        screenName = READ_ONLY_NOTIFICATION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, selectedNotification.getOtherDetails()));
        nextButton.setText(UtilBean.getMyLabel(LabelConstants.MARK_AS_READ));

        final View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                alertDialog.dismiss();
                fhsService.markNotificationAsRead(selectedNotification);
                bodyLayoutContainer.removeAllViews();
                showProcessDialog();
                bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                addSearchTextBox();
                retrieveNotificationListFromDB(selectedNotificationCode, null, null);
                nextButton.setText(GlobalTypes.EVENT_NEXT);
                nextButton.setOnClickListener(onClickListener);
            } else {
                alertDialog.dismiss();
            }
        };

        nextButton.setOnClickListener(v -> {
            alertDialog = new MyAlertDialog(context,
                    LabelConstants.CONFORMATION_TO_MARK_NOTIFICATION_AS_READ,
                    myListener, DynamicUtils.BUTTON_YES_NO);
            alertDialog.show();
        });

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
        String nextEntity = selectedNotification.getTask();
        try {
            MemberBean memberBean = memberMapWithActualIdAsKey.get(selectedNotification.getMemberId());
            if (memberBean != null) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                formMetaDataUtil.setMetaDataForRchFormByFormType(nextEntity, memberBean.getActualId(), memberBean.getFamilyId(), selectedNotification, sharedPref);
            }
        } catch (DataException e) {
            hideProcessDialog();
            View.OnClickListener listener = v -> {
                alertDialog.dismiss();
                navigateToHomeScreen(false);
            };
            alertDialog = new MyAlertDialog(this, false,
                    UtilBean.getMyLabel(LabelConstants.ERROR_TO_REFRESH_ALERT), listener, DynamicUtils.BUTTON_OK);
            alertDialog.show();
            return;
        }
        if (nextEntity != null) {
            Intent myIntent = new Intent(context, DynamicFormActivity_.class);
            myIntent.putExtra(SewaConstants.ENTITY, nextEntity);
            startActivityForResult(myIntent, REQUEST_CODE_FOR_NOTIFICATION_ACTIVITY);
        }
        hideProcessDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult resultForQRScanner = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        offset = 0;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_NOTIFICATION_ACTIVITY) {
            try {
                notificationBeanList = notificationService.retrieveNotificationsForUser(null, selectedAreaIds, selectedNotificationCode, null, LIMIT, offset, qrScanFilter);
                offset = offset + LIMIT;
                if (notificationBeanList != null && !notificationBeanList.isEmpty()) {
                    bodyLayoutContainer.removeAllViews();
                    showProcessDialog();
                    bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                    addSearchTextBox();
                    selectedNotification = null;
                    addNotificationScreen(false);
                } else {
                    showProcessDialog();
                    retrieveCountsForEachNotificationType();
                }
            } catch (SQLException e) {
                navigateToHomeScreen(false);
                Logger.getLogger(NotificationAshaActivity.class.getName()).log(Level.SEVERE, null, e);
            }
        } else if (requestCode == REQUEST_CODE_FOR_OTHER_NOTIFICATION_ACTIVITY) {
            addNotificationTypeScreen();
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
        selectedNotificationTypeIndex = -1;
        if (screenName == null || screenName.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            footerView.setVisibility(View.VISIBLE);
            switch (screenName) {
                case AREA_SELECTION_SCREEN:
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
                case READ_ONLY_NOTIFICATION_SCREEN:
                    bodyLayoutContainer.removeAllViews();
                    showProcessDialog();
                    bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                    addSearchTextBox();
                    retrieveNotificationListFromDB(selectedNotificationCode, null, null);
                    nextButton.setText(GlobalTypes.EVENT_NEXT);
                    nextButton.setOnClickListener(this);
                    break;
                default:
                    break;
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
        List<NotificationMobDataBean> notificationMobDataBeans = null;
        try {
            notificationMobDataBeans = notificationService.retrieveNotificationsForUser(
                    null, selectedAreaIds, selectedNotificationCode, searchString, LIMIT, offset, qrScanFilter);
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }
        offset = offset + LIMIT;
        onLoadMoreUi(notificationMobDataBeans);
    }

    @UiThread
    public void onLoadMoreUi(List<NotificationMobDataBean> notificationMobDataBeans) {
        if (notificationMobDataBeans != null && !notificationMobDataBeans.isEmpty()) {
            notificationBeanList.addAll(notificationMobDataBeans);
            offset = offset + LIMIT;
            List<ListItemDataBean> stringList = addNotificationForLoop(notificationMobDataBeans);
            notificationListView.onFinishLoadingWithItem(true, stringList);
        } else {
            notificationListView.onFinishLoadingWithItem(false, null);
        }
    }
}
