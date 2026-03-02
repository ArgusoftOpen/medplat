package com.argusoft.sewa.android.app.activity;

import static com.argusoft.sewa.android.app.component.MyAlertDialog.BUTTON_POSITIVE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyDynamicComponents;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.NotificationConstants;
import com.argusoft.sewa.android.app.constants.RchConstants;
import com.argusoft.sewa.android.app.core.impl.NotificationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberAdditionalInfoDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
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
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

@EActivity
public class MyPeopleAwwActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    private static final long DELAY = 500;
    private static final String ASHA_AREA_SELECTION_SCREEN = "ashaAreaSelectionScreen";
    private static final String NO_DATA_SCREEN = "noDataScreen";
    private static final String SERVICE_SELECTION_SCREEN = "serviceSelectionScreen";
    private static final String PEOPLE_SELECTION_SCREEN = "peopleSelectionScreen";
    private static final String VISIT_SELECTION_SCREEN = "visitSelectionScreen";
    private static final String CHART_SELECTION_SCREEN = "chartSelectionScreen";
    private static final String SERVICE_ELIGIBLE_MOTHERS = "mothersWithLastDeliveryLessThan6Months";
    private static final String SERVICE_PREGNANT_WOMEN = "pregnantWomen";
    private static final String SERVICE_CHILDREN = "childrenOf0to6years";
    private static final String SERVICE_ADOLESCENT_GIRLS = "adolescentGirls";
    private static final Integer REQUEST_CODE_FOR_MY_PEOPLE_ACTIVITY = 200;
    private static final String MOTHER_OBJECT = "Mother";
    private static final String TAG = "MyPeopleAwwActivity";
    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    NotificationServiceImpl notificationService;
    @Bean
    FormMetaDataUtil formMetaDataUtil;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<MemberBean, Integer> memberBeanDao;
    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private Intent myIntent;
    private Spinner ashaAreaSpinner;
    private String screen;
    private String selectedService;
    private List<LocationBean> ashaAreaList = new ArrayList<>();
    private Integer selectedAshaArea;
    private List<MemberDataBean> memberList = new ArrayList<>();
    private MemberDataBean memberSelected = null;
    private List<String> mapOfVisitFormWithRadioButtonId;
    private Timer timer = new Timer();
    private MyAlertDialog myAlertDialog;
    private int selectedServiceIndex = -1;
    private long limit = 30;
    private long offset;
    private MaterialTextView subtitleTextView;
    private PagingListView paginatedListView;
    private MaterialTextView noMemberTextView;
    private int selectedMemberIndex = -1;
    private int selectedVisitIndex = -1;
    private CharSequence searchString;
    private List<Integer> areas;
    private List<String> visits;
    private LinearLayout footerView;
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
            myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        setTitle(UtilBean.getTitleText(LabelConstants.MY_PEOPLE_TITLE));
    }

    private void initView() {
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
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.MY_PEOPLE_TITLE);
        startActivityForResult(myIntent, ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            setSubTitle(null);
            switch (screen) {
                case ASHA_AREA_SELECTION_SCREEN:
                    String selectedArea = ashaAreaSpinner.getSelectedItem().toString();
                    for (LocationBean locationBean : ashaAreaList) {
                        if (selectedArea.equals(locationBean.getName())) {
                            selectedAshaArea = locationBean.getActualID();
                            break;
                        }
                    }
                    bodyLayoutContainer.removeAllViews();
                    setServiceSelectionScreen();
                    break;

                case SERVICE_SELECTION_SCREEN:
                    if (selectedServiceIndex != -1) {
                        switch (selectedServiceIndex) {
                            case 0:
                                selectedService = SERVICE_CHILDREN;
                                break;
                            case 1:
                                selectedService = SERVICE_PREGNANT_WOMEN;
                                break;
                            case 2:
                                selectedService = SERVICE_ELIGIBLE_MOTHERS;
                                break;
                            case 3:
                                selectedService = SERVICE_ADOLESCENT_GIRLS;
                                break;
                            default:
                        }

                        screen = PEOPLE_SELECTION_SCREEN;
                        showProcessDialog();
                        bodyLayoutContainer.removeAllViews();
                        addSearchTextBox();
                        retrieveMemberListByServiceType(selectedService, null, false, null);
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.SERVICE_SELECTION_REQUIRED_ALERT));
                    }
                    break;

                case PEOPLE_SELECTION_SCREEN:
                    if (memberList == null || memberList.isEmpty()) {
                        navigateToHomeScreen(false);
                        return;
                    }
                    if (selectedMemberIndex != -1) {
                        memberSelected = memberList.get(selectedMemberIndex);
                        addVisitSelectionScreen();
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_MEMBER));
                    }
                    break;

                case VISIT_SELECTION_SCREEN:
                    setSubTitle(UtilBean.getMemberFullName(memberSelected));
                    if (selectedVisitIndex != -1) {
                        final String formType = mapOfVisitFormWithRadioButtonId.get(selectedVisitIndex);
                        if (formType != null) {
                            switch (formType) {
                                case NotificationConstants.FHW_NOTIFICATION_WORK_PLAN_FOR_DELIVERY:
                                    if (Boolean.TRUE.equals(notificationService.checkIfThereArePendingNotificationsForMember(Long.valueOf(memberSelected.getId()), NotificationConstants.FHW_NOTIFICATION_ANC))) {
                                        View.OnClickListener myListener = v1 -> {
                                            if (v1.getId() == BUTTON_POSITIVE) {
                                                myAlertDialog.dismiss();
                                                bodyLayoutContainer.removeAllViews();
                                                startDynamicFormActivity(formType);
                                            } else {
                                                myAlertDialog.dismiss();
                                            }
                                        };

                                        myAlertDialog = new MyAlertDialog(this,
                                                LabelConstants.PROCEED_FROM_WPD_WHILE_ANC_ALERTS_PENDING_ALERT,
                                                myListener, DynamicUtils.BUTTON_YES_NO);
                                        myAlertDialog.show();
                                    } else {
                                        bodyLayoutContainer.removeAllViews();
                                        startDynamicFormActivity(formType);
                                    }
                                    break;
                                case FormConstants.TECHO_AWW_HEIGHT_GROWTH_GRAPH:
                                    addGraph(FormConstants.TECHO_AWW_HEIGHT_GROWTH_GRAPH);
                                    break;
                                case FormConstants.TECHO_AWW_WEIGHT_GROWTH_GRAPH:
                                    addGraph(FormConstants.TECHO_AWW_WEIGHT_GROWTH_GRAPH);
                                    break;
                                default:
                                    bodyLayoutContainer.removeAllViews();
                                    startDynamicFormActivity(formType);
                                    break;
                            }
                        }
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.VISIT_SELECTION_REQUIRED_ALERT));
                    }
                    selectedVisitIndex = -1;
                    break;
                case CHART_SELECTION_SCREEN:
                    screen = PEOPLE_SELECTION_SCREEN;
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    addSearchTextBox();
                    retrieveMemberListByServiceType(selectedService, null, false, null);
                    memberSelected = null;
                    break;
                case NO_DATA_SCREEN:
                    navigateToHomeScreen(false);
                    finish();
                    break;
                default:
            }
        }
    }

    @UiThread
    public void addVisitSelectionScreen() {
        visits = new ArrayList<>();
        if (SERVICE_CHILDREN.equals(selectedService)) {
            visits.add(FormConstants.TECHO_AWW_CS);
            visits.add(FormConstants.TECHO_AWW_WEIGHT_GROWTH_GRAPH);
            bodyLayoutContainer.removeAllViews();
            setSubTitle(UtilBean.getMemberFullName(memberSelected));
            setVisitSelectionScreen();
        } else {
            View.OnClickListener listener = v -> alertDialog.dismiss();
            alertDialog = new MyAlertDialog(this,
                    LabelConstants.SERVICE_IS_TEMPORARY_BLOCKED,
                    listener, DynamicUtils.BUTTON_OK);
            alertDialog.show();
        }
    }

    @UiThread
    public void addGraph(String flag) {
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
        ashaAreaSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 2);
        if (selectedAshaArea != null) {
            for (LocationBean locationBean : ashaAreaList) {
                String locationName;
                if (locationBean.getActualID().intValue() == selectedAshaArea.intValue()) {
                    locationName = locationBean.getName();
                    ashaAreaSpinner.setSelection(Arrays.asList(arrayOfOptions).indexOf(locationName));
                }
            }
        }
        bodyLayoutContainer.addView(ashaAreaSpinner);
        hideProcessDialog();
    }

    @UiThread
    public void setServiceSelectionScreen() {
        screen = SERVICE_SELECTION_SCREEN;
        List<ListItemDataBean> list = new ArrayList<>();
        MaterialTextView textView = MyStaticComponents.getListTitleView(this, LabelConstants.SERVICE_TYPE_SELECTION);
        bodyLayoutContainer.addView(textView);

        list.add(new ListItemDataBean(LabelConstants.SERVICE_CHILDREN_OF_AGE_0_TO_6_YEARS));
        list.add(new ListItemDataBean(LabelConstants.SERVICE_PREGNANT_WOMEN));
        list.add(new ListItemDataBean(LabelConstants.SERVICE_MOTHERS_WITH_LAST_DELIVERY_BEFORE_6_MONTHS));
        list.add(new ListItemDataBean(LabelConstants.SERVICE_ADOLESCENT_GIRLS));

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            selectedServiceIndex = position;
            onClick(nextButton);
            footerView.setVisibility(View.VISIBLE);
        };
        PagingListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_type, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
        footerView.setVisibility(View.GONE);
        hideProcessDialog();
    }

    @UiThread
    public void addSearchTextBox() {
        bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
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

    @Background
    public void retrieveMemberListByServiceType(String selectedService, CharSequence s, Boolean isSearch, String qrData) {
        offset = 0;
        searchString = s;
        selectedMemberIndex = -1;
        qrScanFilter = SewaUtil.setQrScanFilterData(qrData);
        switch (selectedService) {
            case SERVICE_ELIGIBLE_MOTHERS:
                memberList = fhsService.retrieveMothersWithLastDeliveryLessThan6MonthsByAshaArea(selectedAshaArea, s, limit, offset, qrScanFilter);
                offset = offset + limit;
                break;
            case SERVICE_PREGNANT_WOMEN:
                areas = new LinkedList<>();
                areas.add(selectedAshaArea);
                LinkedList<Integer> integers = new LinkedList<>();
                memberList = fhsService.retrievePregnantWomenByAshaArea(areas, false, integers, s, limit, offset, qrScanFilter);
                offset = offset + limit;
                break;
            case SERVICE_ADOLESCENT_GIRLS:
                memberList = fhsService.retrieveAdolescentGirlsByAshaArea(selectedAshaArea, s, limit, offset, qrScanFilter);
                offset = offset + limit;
                break;
            case SERVICE_CHILDREN:
                memberList = fhsService.retrieveChildsBelow6YearsByAshaArea(selectedAshaArea, s, limit, offset, qrScanFilter);
                offset = offset + limit;
                break;
            default:
        }
        setMemberSelectionScreen(selectedService, isSearch);
    }

    @UiThread
    public void setMemberSelectionScreen(String selectedService, Boolean isSearch) {
        switch (selectedService) {
            case SERVICE_ELIGIBLE_MOTHERS:
                addEligibleMothersList();
                break;
            case SERVICE_PREGNANT_WOMEN:
                addPregnantWomenList();
                break;
            case SERVICE_ADOLESCENT_GIRLS:
                addAdolescentGirlsList();
                break;
            case SERVICE_CHILDREN:
                addChildsBelow6YearsList();
                break;
            default:
        }
        hideProcessDialog();
    }

    private void addEligibleMothersList() {
        bodyLayoutContainer.removeView(subtitleTextView);
        bodyLayoutContainer.removeView(noMemberTextView);
        bodyLayoutContainer.removeView(paginatedListView);

        if (memberList != null && !memberList.isEmpty()) {
            subtitleTextView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_MOTHER);
            bodyLayoutContainer.addView(subtitleTextView);
            List<ListItemDataBean> list = getMothersList(memberList);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedMemberIndex = position;
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            noMemberTextView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MOTHERS_WITH_LAST_DELIVERY_BEFORE_6_MONTHS_ALERT);
            bodyLayoutContainer.addView(noMemberTextView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }
    }

    private void addPregnantWomenList() {
        bodyLayoutContainer.removeView(subtitleTextView);
        bodyLayoutContainer.removeView(noMemberTextView);
        bodyLayoutContainer.removeView(paginatedListView);

        if (memberList != null && !memberList.isEmpty()) {
            subtitleTextView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT + " a " + LabelConstants.SERVICE_PREGNANT_WOMEN);
            bodyLayoutContainer.addView(subtitleTextView);

            List<ListItemDataBean> list = getMothersList(memberList);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedMemberIndex = position;
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            noMemberTextView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_PREGNANT_WOMEN_IN_AREA);
            bodyLayoutContainer.addView(noMemberTextView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }
    }

    private List<ListItemDataBean> getMothersList(List<MemberDataBean> memberList) {
        List<ListItemDataBean> list = new ArrayList<>();
        for (MemberDataBean memberDataBean : memberList) {
            list.add(new ListItemDataBean(memberDataBean.getFamilyId(), memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean), null, null));
        }
        return list;
    }

    private void addAdolescentGirlsList() {
        bodyLayoutContainer.removeView(subtitleTextView);
        bodyLayoutContainer.removeView(noMemberTextView);
        bodyLayoutContainer.removeView(paginatedListView);

        if (memberList != null && !memberList.isEmpty()) {
            subtitleTextView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_ADOLESCENT_GIRL);
            bodyLayoutContainer.addView(subtitleTextView);

            List<ListItemDataBean> list = getAdolescentGirlsList(memberList);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedMemberIndex = position;
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            noMemberTextView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_ADOLESCENT_GIRLS_IN_YOUR_AREA);
            bodyLayoutContainer.addView(noMemberTextView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }
    }

    private List<ListItemDataBean> getAdolescentGirlsList(List<MemberDataBean> memberList) {
        String rbText;
        List<ListItemDataBean> list = new ArrayList<>();
        List<MemberDataBean> removedChildren = new ArrayList<>();

        for (MemberDataBean memberDataBean : memberList) {
            if (memberDataBean.getUniqueHealthId() != null) {
                MemberBean mother = null;
                if (memberDataBean.getMotherId() != null) {
                    try {
                        mother = memberBeanDao.queryBuilder().where().eq("actualId", memberDataBean.getMotherId()).queryForFirst();
                    } catch (SQLException e) {
                        Log.d("+++++++++++++", e.getMessage());
                    }
                }

                if (mother == null) {
                    rbText = UtilBean.getMyLabel(LabelConstants.NOT_AVAILABLE);
                } else {
                    rbText = UtilBean.getMemberFullName(mother);
                }
                list.add(new ListItemDataBean(null, memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean), UtilBean.getMyLabel(MOTHER_OBJECT), rbText));
            } else {
                removedChildren.add(memberDataBean);
            }
        }
        this.memberList.removeAll(removedChildren);
        return list;
    }

    private void addChildsBelow6YearsList() {
        bodyLayoutContainer.removeView(subtitleTextView);
        bodyLayoutContainer.removeView(noMemberTextView);
        bodyLayoutContainer.removeView(paginatedListView);

        if (memberList != null && !memberList.isEmpty()) {
            subtitleTextView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_CHILD);
            bodyLayoutContainer.addView(subtitleTextView);
            List<ListItemDataBean> list = getChildrenList(memberList);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedMemberIndex = position;
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            noMemberTextView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_CHILDREN_OF_AGE_0_TO_6_YEARS_ALERT);
            bodyLayoutContainer.addView(noMemberTextView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }
    }

    private List<ListItemDataBean> getChildrenList(List<MemberDataBean> memberList) {
        String rbText;
        List<MemberDataBean> removedChildren = new ArrayList<>();

        List<ListItemDataBean> list = new ArrayList<>();
        for (MemberDataBean memberDataBean : memberList) {
            if (memberDataBean.getUniqueHealthId() != null) {
                MemberBean mother = null;
                if (memberDataBean.getMotherId() != null) {
                    try {
                        mother = memberBeanDao.queryBuilder().where().eq("actualId", memberDataBean.getMotherId()).queryForFirst();
                    } catch (SQLException e) {
                        Log.d("++++++++++++++++++", e.getMessage());
                    }
                }

                if (mother == null) {
                    rbText = UtilBean.getMyLabel(LabelConstants.NOT_AVAILABLE);
                } else {
                    rbText = UtilBean.getMemberFullName(mother);
                }
                list.add(new ListItemDataBean(null, memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean), UtilBean.getMyLabel(MOTHER_OBJECT), rbText));
            } else {
                removedChildren.add(memberDataBean);
            }
        }
        this.memberList.removeAll(removedChildren);
        return list;
    }

    @UiThread
    public void setVisitSelectionScreen() {
        screen = VISIT_SELECTION_SCREEN;
        setSubTitle(UtilBean.getMemberFullName(memberSelected));
        mapOfVisitFormWithRadioButtonId = new ArrayList<>();
        List<ListItemDataBean> list = new ArrayList<>();
        for (String visit : visits) {
            list.add(new ListItemDataBean(UtilBean.getFullFormOfEntity().get(visit)));
            mapOfVisitFormWithRadioButtonId.add(visit);
        }

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            selectedVisitIndex = position;
            onClick(nextButton);
        };
        PagingListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_type, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
        footerView.setVisibility(View.GONE);
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
                setServiceSelectionScreen();
                nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                nextButton.setOnClickListener(MyPeopleAwwActivity.this);
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
                setServiceSelectionScreen();
                nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                nextButton.setOnClickListener(MyPeopleAwwActivity.this);
            });
        }
    }

    private void setMetadataForFormByFormType(String formType, MemberDataBean memberDataBean) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (!formType.equals(FormConstants.FHS_MEMBER_UPDATE)) {
            formMetaDataUtil.setMetaDataForRchFormByFormType(formType, memberDataBean.getId(), memberDataBean.getFamilyId(), null, sharedPref);
        } else {
            FamilyDataBean familyDataBean = fhsService.retrieveFamilyDataBeanByFamilyId(memberDataBean.getFamilyId());
            formMetaDataUtil.setMetaDataForMemberUpdateForm(memberDataBean, familyDataBean, sharedPref);
        }
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

    private void startDynamicFormActivity(final String formType) {
        showProcessDialog();
        myIntent = new Intent(this, DynamicFormActivity_.class);
        new Thread() {
            @Override
            public void run() {
                setMetadataForFormByFormType(formType, memberSelected);
                myIntent.putExtra(SewaConstants.ENTITY, formType);
                startActivityForResult(myIntent, REQUEST_CODE_FOR_MY_PEOPLE_ACTIVITY);
                showProcessDialog();
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult resultForQRScanner = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_MY_PEOPLE_ACTIVITY) {
            memberList.clear();
            screen = PEOPLE_SELECTION_SCREEN;
            showProcessDialog();
            bodyLayoutContainer.removeAllViews();
            setVisitSelectionScreen();
            hideProcessDialog();
        } else if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                selectedAshaArea = Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID));
                bodyLayoutContainer.removeAllViews();
                setServiceSelectionScreen();
            } else {
                navigateToHomeScreen(false);
            }
        }  else if (resultForQRScanner != null) {
            if (resultForQRScanner.getContents() == null) {
                SewaUtil.generateToast(this, LabelConstants.FAILED_TO_SCAN_QR);
            } else {
                //show dialogue with resultForQRScanner
                String scanningData = resultForQRScanner.getContents();
                Log.i(TAG, "QR Scanner Data : " + scanningData);
                retrieveMemberListByServiceType(selectedService,null, false, scanningData);
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
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            switch (screen) {
                case CHART_SELECTION_SCREEN:
                    addVisitSelectionScreen();
                    break;
                case VISIT_SELECTION_SCREEN:
                    screen = PEOPLE_SELECTION_SCREEN;
                    showProcessDialog();
                    footerView.setVisibility(View.VISIBLE);
                    bodyLayoutContainer.removeAllViews();
                    selectedMemberIndex = -1;
                    setSubTitle(null);
                    addSearchTextBox();
                    retrieveMemberListByServiceType(selectedService, null, false, null);
                    memberSelected = null;
                    break;

                case PEOPLE_SELECTION_SCREEN:
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    memberList.clear();
                    selectedServiceIndex = -1;
                    setServiceSelectionScreen();
                    nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                    nextButton.setOnClickListener(this);
                    break;

                case SERVICE_SELECTION_SCREEN:
                    startLocationSelectionActivity();
                    break;

                case ASHA_AREA_SELECTION_SCREEN:
                    navigateToHomeScreen(false);
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
        List<MemberDataBean> memberDataBeanList = null;
        switch (selectedService) {
            case SERVICE_ELIGIBLE_MOTHERS:
                memberDataBeanList = fhsService.retrieveMothersWithLastDeliveryLessThan6MonthsByAshaArea(selectedAshaArea, searchString, limit, offset, qrScanFilter);
                offset = offset + limit;
                break;
            case SERVICE_PREGNANT_WOMEN:
                LinkedList<Integer> integers = new LinkedList<>();
                memberDataBeanList = fhsService.retrievePregnantWomenByAshaArea(areas, false, integers, searchString, limit, offset, qrScanFilter);
                offset = offset + limit;
                break;
            case SERVICE_ADOLESCENT_GIRLS:
                memberDataBeanList = fhsService.retrieveAdolescentGirlsByAshaArea(selectedAshaArea, searchString, limit, offset, qrScanFilter);
                offset = offset + limit;
                break;
            case SERVICE_CHILDREN:
                memberDataBeanList = fhsService.retrieveChildsBelow6YearsByAshaArea(selectedAshaArea, searchString, limit, offset, qrScanFilter);
                offset = offset + limit;
                break;
            default:
        }
        onLoadMoreUi(memberDataBeanList);
    }

    @UiThread
    public void onLoadMoreUi(List<MemberDataBean> memberDataBeanList) {
        if (memberDataBeanList != null && !memberDataBeanList.isEmpty()) {
            memberList.addAll(memberDataBeanList);
            List<ListItemDataBean> stringList = new ArrayList<>();
            switch (selectedService) {
                case SERVICE_ELIGIBLE_MOTHERS:
                case SERVICE_PREGNANT_WOMEN:
                    stringList = getMothersList(memberDataBeanList);
                    break;
                case SERVICE_ADOLESCENT_GIRLS:
                    stringList = getAdolescentGirlsList(memberDataBeanList);
                    break;
                case SERVICE_CHILDREN:
                    stringList = getChildrenList(memberDataBeanList);
                    break;
                default:
            }
            paginatedListView.onFinishLoadingWithItem(true, stringList);
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
        }

    }
}
