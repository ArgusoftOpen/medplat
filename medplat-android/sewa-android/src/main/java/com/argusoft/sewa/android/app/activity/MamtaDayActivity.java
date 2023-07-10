/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.NotificationConstants;
import com.argusoft.sewa.android.app.core.impl.NotificationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.exception.DataException;
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
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author alpeshkyada
 */

@EActivity
public class MamtaDayActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    private static final Integer REQUEST_CODE_FOR_MAMTA_DAY_ACTIVITY = 200;
    private static final String SERVICE_SELECTION_SCREEN = "serviceSelectionScreen";
    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";
    private static final String VISIT_SELECTION_SCREEN = "visitSelectionScreen";
    private static final String SERVICE_PREGNANT_WOMEN = "pregnantWomen";
    private static final String SERVICE_CHILDREN = "children";
    private static final long DELAY = 500;
    private static final String TAG = "MamtaDayActivity";

    @OrmLiteDao(helper = DBConnection.class)
    Dao<MemberBean, Integer> memberBeanDao;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    NotificationServiceImpl notificationService;
    @Bean
    FormMetaDataUtil formMetaDataUtil;
    private MyAlertDialog myDialog;
    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private String screen;
    private List<Integer> selectedAshaAreas = new ArrayList<>();
    private List<Integer> villageIds = new ArrayList<>();
    private String selectedService;
    private MemberDataBean memberSelected = null;
    private Map<Long, MemberBean> memberMapWithActualIdAsKey;
    private List<NotificationMobDataBean> notificationList = new ArrayList<>();
    private Gson gson = new Gson();
    private List<String> mapOfVisitFormWithRadioButtonId;
    private MyAlertDialog myAlertDialog;
    private Intent myIntent;
    private Timer timer = new Timer();
    private Long role = -1L;
    private int selectedTypeIndex = -1;
    private int selectedMemberIndex = -1;
    private long limit = 30;
    private long offset;
    private PagingListView paginatedListView;
    private MaterialTextView noWomanView;
    private MaterialTextView textView;
    private CharSequence searchString;
    private String notificationCodePregnantWomen;
    private String notificationCodeChild;
    private List<String> visits;
    private LinearLayout footerView;
    private LinkedHashMap<String, String> qrScanFilter = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String selectedAshaAreasString = getIntent().getStringExtra(GlobalTypes.SELECTED_ASHA_AREAS);
        selectedAshaAreas = gson.fromJson(selectedAshaAreasString, new TypeToken<List<Integer>>() {
        }.getType());
        String selectedVillageString = getIntent().getStringExtra(GlobalTypes.SELECTED_VILLAGE_IDS);
        role = getIntent().getLongExtra("role", -1L);
        villageIds = gson.fromJson(selectedVillageString, new TypeToken<List<Integer>>() {
        }.getType());
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
        setTitle(UtilBean.getTitleText(LabelConstants.MAMTA_DAY_TITLE));
    }

    @UiThread
    @Override
    public void onBackPressed() {
        myDialog = new MyAlertDialog(this, LabelConstants.WANT_TO_CLOSE_MAMTA_DAY_WORKPLAN, this, DynamicUtils.BUTTON_YES_NO);
        myDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == BUTTON_POSITIVE) {
            finish();
        } else if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screen) {
                case SERVICE_SELECTION_SCREEN:
                    if (selectedTypeIndex != -1) {
                        if (selectedTypeIndex == 0) {
                            selectedService = SERVICE_PREGNANT_WOMEN;
                        } else if (selectedTypeIndex == 1) {
                            selectedService = SERVICE_CHILDREN;
                        }
                        showProcessDialog();
                        bodyLayoutContainer.removeAllViews();
                        addSearchTextBox();
                        retrieveMemberListByServiceType(selectedService, null, false, null);
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_TYPE_OF_SERVICE));
                    }
                    break;
                case MEMBER_SELECTION_SCREEN:
                    if (selectedMemberIndex != -1) {
                        NotificationMobDataBean selectedNotification = notificationList.get(selectedMemberIndex);
                        memberSelected = new MemberDataBean(Objects.requireNonNull(memberMapWithActualIdAsKey.get(selectedNotification.getMemberId())));
                        visits = new ArrayList<>();
                        if (SERVICE_PREGNANT_WOMEN.equals(selectedService)) {
                            if (role == 24) {
                                visits.add(FormConstants.ASHA_ANC);
                            } else {
                                visits.add(FormConstants.TECHO_FHW_ANC);
                                visits.add(FormConstants.TECHO_FHW_WPD);
                            }
                        } else if (SERVICE_CHILDREN.equals(selectedService)) {
                            if (role == 24) {
                                visits.add(FormConstants.ASHA_CS);
                            } else {
                                visits.add(FormConstants.TECHO_FHW_CS);
                                visits.add(FormConstants.TECHO_FHW_VAE);
                            }
                        }
                        bodyLayoutContainer.removeAllViews();
                        showProcessDialog();
                        setVisitSelectionScreen(visits);
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_MEMBER));
                    }
                    break;

                default:
            }
        } else if (v.getId() == BUTTON_NEGATIVE) {
            myDialog.dismiss();
        }
    }

    @Background
    public void retrieveMemberListByServiceType(String selectedService, CharSequence s, boolean isSearch, String qrData) {
        searchString = s;
        selectedMemberIndex = -1;
        qrScanFilter = SewaUtil.setQrScanFilterData(qrData);
        try {
            if (role == 24) {
                notificationCodePregnantWomen = FormConstants.ASHA_ANC;
                notificationCodeChild = FormConstants.ASHA_CS;
            } else {
                notificationCodePregnantWomen = NotificationConstants.FHW_NOTIFICATION_ANC;
                notificationCodeChild = NotificationConstants.FHW_NOTIFICATION_CHILD_SERVICES;
            }
            offset = 0;
            if (SERVICE_PREGNANT_WOMEN.equals(selectedService)) {
                notificationList = notificationService.retrieveNotificationsForUser(villageIds, selectedAshaAreas, notificationCodePregnantWomen, s, limit, offset, qrScanFilter);
            } else if (SERVICE_CHILDREN.equals(selectedService)) {
                notificationList = notificationService.retrieveNotificationsForUser(villageIds, selectedAshaAreas, notificationCodeChild, s, limit, offset, qrScanFilter);
            }
            offset = offset + limit;
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }
        setMemberSelectionScreen(selectedService, isSearch);
    }

    @UiThread
    public void setMemberSelectionScreen(String selectedService, boolean isSearch) {
        screen = MEMBER_SELECTION_SCREEN;

        if (SERVICE_PREGNANT_WOMEN.equals(selectedService)) {
            addAncDueNotificationList();
        } else if (SERVICE_CHILDREN.equals(selectedService)) {
            addChildServicesDueNotificationList();
        }

        if (notificationList.isEmpty()) {
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
            nextButton.setOnClickListener(v -> navigateToHomeScreen(false));
        } else {
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
            nextButton.setOnClickListener(this);
        }
        hideProcessDialog();
    }

    private void addAncDueNotificationList() {
        bodyLayoutContainer.removeView(paginatedListView);
        bodyLayoutContainer.removeView(textView);
        bodyLayoutContainer.removeView(noWomanView);

        List<Long> memberActualIds = new ArrayList<>();
        for (NotificationMobDataBean notificationMobDataBean : notificationList) {
            if (notificationMobDataBean.getMemberId() != null) {
                memberActualIds.add(notificationMobDataBean.getMemberId());
            }
        }

        memberMapWithActualIdAsKey = fhsService.retrieveMemberBeansMapByActualIds(memberActualIds);

        if (notificationList != null && !notificationList.isEmpty()) {
            textView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_A_PREGNANT_WOMEN);
            bodyLayoutContainer.addView(textView);

            List<ListItemDataBean> memberList = getWomenList(notificationList);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedMemberIndex = position;
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, memberList, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
        } else {
            noWomanView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_PREGNANT_WOMEN_IN_AREA);
            bodyLayoutContainer.addView(noWomanView);
        }
    }

    private List<ListItemDataBean> getWomenList(List<NotificationMobDataBean> notificationMobDataBeans) {
        String rbText = null;
        List<ListItemDataBean> list = new ArrayList<>();

        for (NotificationMobDataBean notificationMobDataBean : notificationMobDataBeans) {
            MemberBean memberBean = memberMapWithActualIdAsKey.get(notificationMobDataBean.getMemberId());
            if (memberBean != null) {
                if (memberBean.getImmunisationGiven() == null || memberBean.getImmunisationGiven().isEmpty()) {
                    rbText = LabelConstants.NO_TT_GIVEN;
                }
                list.add(new ListItemDataBean(memberBean.getFamilyId(), memberBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberBean), rbText, null));
            }
        }
        return list;
    }

    private void addChildServicesDueNotificationList() {
        bodyLayoutContainer.removeView(paginatedListView);
        bodyLayoutContainer.removeView(textView);
        bodyLayoutContainer.removeView(noWomanView);


        List<Long> memberActualIds = new ArrayList<>();
        for (NotificationMobDataBean notificationMobDataBean : notificationList) {
            if (notificationMobDataBean.getMemberId() != null) {
                memberActualIds.add(notificationMobDataBean.getMemberId());
            }
        }

        memberMapWithActualIdAsKey = fhsService.retrieveMemberBeansMapByActualIds(memberActualIds);

        if (notificationList != null && !notificationList.isEmpty()) {
            textView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_CHILD);
            bodyLayoutContainer.addView(textView);
            List<ListItemDataBean> childrenList = getChildrenList(notificationList);

            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedMemberIndex = position;
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, childrenList, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
        } else {
            noWomanView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_CHILDREN_IN_YOUR_AREA);
            bodyLayoutContainer.addView(noWomanView);
        }
    }

    private List<ListItemDataBean> getChildrenList(List<NotificationMobDataBean> notificationMobDataBeans) {
        String rbText;
        List<ListItemDataBean> list = new ArrayList<>();
        for (NotificationMobDataBean notificationMobDataBean : notificationMobDataBeans) {
            MemberBean memberBean = memberMapWithActualIdAsKey.get(notificationMobDataBean.getMemberId());
            MemberBean mother = null;
            if (memberBean != null && memberBean.getMotherId() != null) {
                try {
                    mother = memberBeanDao.queryBuilder().where().eq("actualId", memberBean.getMotherId()).queryForFirst();
                } catch (SQLException e) {
                    Log.e(getClass().getName(), null, e);
                }
                if (mother == null) {
                    rbText = UtilBean.getMyLabel(LabelConstants.NOT_AVAILABLE);
                } else {
                    rbText = UtilBean.getMemberFullName(mother);
                }
                list.add(new ListItemDataBean(memberBean.getFamilyId(), memberBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberBean), LabelConstants.MOTHER, rbText));
            }
        }
        return list;
    }

    @UiThread
    public void setVisitSelectionScreen(List<String> visits) {
        screen = VISIT_SELECTION_SCREEN;
        setSubTitle(UtilBean.getMemberFullName(memberSelected));
        mapOfVisitFormWithRadioButtonId = new ArrayList<>();
        List<ListItemDataBean> list = new ArrayList<>();
        for (String visit : visits) {
            list.add(new ListItemDataBean(UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(visit))));
            mapOfVisitFormWithRadioButtonId.add(visit);
        }
        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            if (position != -1) {
                final String formType = mapOfVisitFormWithRadioButtonId.get(position);
                if (formType != null) {
                    if (formType.equals(NotificationConstants.FHW_NOTIFICATION_WORK_PLAN_FOR_DELIVERY)) {
                        if (Boolean.TRUE.equals(notificationService.checkIfThereArePendingNotificationsForMember(Long.valueOf(memberSelected.getId()), NotificationConstants.FHW_NOTIFICATION_ANC))) {
                            View.OnClickListener myListener = v -> {
                                if (v.getId() == BUTTON_POSITIVE) {
                                    myAlertDialog.dismiss();
                                    startDynamicFormActivity(formType);
                                } else {
                                    myAlertDialog.dismiss();
                                }
                            };

                            myAlertDialog = new MyAlertDialog(context,
                                    LabelConstants.PROCEED_FROM_WPD_WHILE_ANC_ALERTS_PENDING_ALERT,
                                    myListener, DynamicUtils.BUTTON_YES_NO);
                            myAlertDialog.show();
                        } else {
                            startDynamicFormActivity(formType);
                        }
                    } else {
                        startDynamicFormActivity(formType);
                    }
                }
            }
            footerView.setVisibility(View.VISIBLE);
        };
        PagingListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_type, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
        footerView.setVisibility(View.GONE);
        hideProcessDialog();
    }

    private void startDynamicFormActivity(final String formType) {
        showProcessDialog();
        try {
            setMetadataForFormByFormType(formType, memberSelected);
        } catch (DataException e) {
            View.OnClickListener listener = v -> {
                alertDialog.dismiss();
                navigateToHomeScreen(false);
                finish();
            };
            alertDialog = new MyAlertDialog(this, false,
                    UtilBean.getMyLabel(LabelConstants.ERROR_TO_REFRESH_ALERT), listener, DynamicUtils.BUTTON_OK);
            alertDialog.show();
            return;
        }
        myIntent = new Intent(this, DynamicFormActivity_.class);
        myIntent.putExtra(SewaConstants.ENTITY, formType);
        startActivityForResult(myIntent, REQUEST_CODE_FOR_MAMTA_DAY_ACTIVITY);
        hideProcessDialog();
    }

    private void setMetadataForFormByFormType(String formType, MemberDataBean memberDataBean) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        formMetaDataUtil.setMetaDataForRchFormByFormType(formType, memberDataBean.getId(), memberDataBean.getFamilyId(), null, sharedPref);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        setContentView(new TextView(this));
        super.onSaveInstanceState(outState);
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        footerView = globalPanel.findViewById(DynamicUtils.ID_FOOTER);
        addServiceSelectionScreen();
    }

    private void addServiceSelectionScreen() {
        screen = SERVICE_SELECTION_SCREEN;
        List<ListItemDataBean> list = new ArrayList<>();
        list.add(new ListItemDataBean(LabelConstants.PREGNANT_WOMEN));
        list.add(new ListItemDataBean(LabelConstants.CHILDREN));

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            selectedTypeIndex = position;
            onClick(nextButton);
            footerView.setVisibility(View.VISIBLE);
        };
        PagingListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_type, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
        footerView.setVisibility(View.GONE);
        hideProcessDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult resultForQRScanner = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_MAMTA_DAY_ACTIVITY) {
            showProcessDialog();
            bodyLayoutContainer.removeAllViews();
            setVisitSelectionScreen(visits);
            hideProcessDialog();
        } else if (resultForQRScanner != null) {
            if (resultForQRScanner.getContents() == null) {
                SewaUtil.generateToast(this, LabelConstants.FAILED_TO_SCAN_QR);
            } else {
                //show dialogue with resultForQRScanner
                String scanningData = resultForQRScanner.getContents();
                Log.i(TAG, "QR Scanner Data : " + scanningData);
                retrieveMemberListByServiceType(selectedService, null, false, scanningData);
            }
        } else {
            finish();
        }
    }

    private void addSearchTextBox() {
        TextInputLayout searchBox = MyStaticComponents.getEditTextWithQrScan(this, LabelConstants.MEMBER_ID_OR_NAME_OR_HEALTH_ID_TO_SEARCH, 1, 15, 1);
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
                                        runOnUiThread(() -> retrieveMemberListByServiceType(selectedService, s, true, null));
                                    } else if (s == null || s.length() == 0) {
                                        runOnUiThread(() -> {
                                            showProcessDialog();
                                            retrieveMemberListByServiceType(selectedService, null, false, null);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (screen == null || screen.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            switch (screen) {
                case SERVICE_SELECTION_SCREEN:
                    finish();
                    break;
                case MEMBER_SELECTION_SCREEN:
                    showProcessDialog();
                    selectedTypeIndex = -1;
                    bodyLayoutContainer.removeAllViews();
                    addServiceSelectionScreen();
                    nextButton.setText(GlobalTypes.EVENT_NEXT);
                    nextButton.setOnClickListener(this);
                    break;
                case VISIT_SELECTION_SCREEN:
                    showProcessDialog();
                    footerView.setVisibility(View.VISIBLE);
                    setSubTitle(null);
                    selectedMemberIndex = -1;
                    bodyLayoutContainer.removeAllViews();
                    addSearchTextBox();
                    retrieveMemberListByServiceType(selectedService, null, false, null);
                    break;
                default:
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
        try {
            if (SERVICE_PREGNANT_WOMEN.equals(selectedService)) {
                List<NotificationMobDataBean> notificationMobDataBeans;
                notificationMobDataBeans = notificationService.retrieveNotificationsForUser(villageIds, selectedAshaAreas, notificationCodePregnantWomen,
                        searchString, limit, offset, qrScanFilter);
                offset = offset + limit;
                onLoadMoreUi(notificationMobDataBeans);
            } else if (SERVICE_CHILDREN.equals(selectedService)) {
                List<NotificationMobDataBean> notificationMobDataBeans = notificationService.retrieveNotificationsForUser(villageIds, selectedAshaAreas,
                        notificationCodeChild, searchString, limit, offset, qrScanFilter);
                offset = offset + limit;
                onLoadMoreUi(notificationMobDataBeans);
            }
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }
    }

    @UiThread
    public void onLoadMoreUi(List<NotificationMobDataBean> notificationMobDataBeans) {
        notificationList.addAll(notificationMobDataBeans);
        List<Long> memberActualIds = new ArrayList<>();
        for (NotificationMobDataBean notificationMobDataBean : notificationMobDataBeans) {
            if (notificationMobDataBean.getMemberId() != null) {
                memberActualIds.add(notificationMobDataBean.getMemberId());
            }
        }
        memberMapWithActualIdAsKey.putAll(fhsService.retrieveMemberBeansMapByActualIds(memberActualIds));
        if (!notificationMobDataBeans.isEmpty()) {
            if (SERVICE_PREGNANT_WOMEN.equals(selectedService)) {
                List<ListItemDataBean> stringList = getWomenList(notificationMobDataBeans);
                paginatedListView.onFinishLoadingWithItem(true, stringList);
            } else if (SERVICE_CHILDREN.equals(selectedService)) {
                List<ListItemDataBean> stringList = getChildrenList(notificationMobDataBeans);
                paginatedListView.onFinishLoadingWithItem(true, stringList);
            }
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
        }
    }
}
