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
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceRestClientImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberAdditionalInfoDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.QueryMobDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.exception.DataException;
import com.argusoft.sewa.android.app.model.LocationBean;
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

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by prateek on 27/11/18.
 */

@EActivity
public class MyPeopleAshaActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    private static final Integer REQUEST_CODE_FOR_MY_PEOPLE_ASHA_ACTIVITY = 300;
    private static final String ASHA_AREA_SELECTION_SCREEN = "ashaAreaSelectionScreen";
    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";
    private static final String FAMILY_SELECTION_SCREEN = "familySelectionScreen";
    private static final String SERVICE_SELECTION_SCREEN = "serviceSelectionScreen";
    private static final String MOTHER_DETAILS_SCREEN = "motherDetailsScreen";
    private static final String RCH_REGISTER_TABLE_SCREEN = "rchRegisterTableScreen";
    private static final String VISIT_SELECTION_SCREEN = "visitSelectionScreen";
    private static final String CHART_SELECTION_SCREEN = "chartSelectionScreen";
    private static final long DELAY = 500;
    private static final String TAG = "MyPeopleAshaActivity";
    @Bean
    public SewaServiceImpl sewaService;
    @Bean
    public SewaFhsServiceImpl fhsService;
    @Bean
    public FormMetaDataUtil formMetaDataUtil;
    @Bean
    public SewaServiceRestClientImpl restClient;
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private String screen;
    private Spinner spinner;

    private TextInputLayout searchText;
    private List<LocationBean> ashaAreaList = new ArrayList<>();
    private Integer selectedAshaArea;
    private List<String> mapOfVisitFormWithRadioButtonId;
    private Integer selectedService;
    private MemberDataBean selectedMember;
    private List<MemberDataBean> memberDataBeans = new ArrayList<>();
    private Timer timer = new Timer();
    private TableRow.LayoutParams layoutParams;
    private ArrayList<Integer> ashaArea;
    private List<String> formTypes = new ArrayList<>();
    private long limit = 30;
    private long offset = 0;
    private int selectedServiceIndex = -1;
    private int selectedPeopleIndex = -1;
    private String searchString;
    private PagingListView paginatedListView;
    private LinearLayout globalPanel;
    private LinearLayout footerView;
    private int selectedVisitIndex = -1;
    private MaterialTextView headerTitle;
    private MaterialTextView noMemberAvailableView;
    private LinkedHashMap<String, String> qrScanFilter = new LinkedHashMap<>();
    private int selectedMemberToUpdateIndex;
    private int selectedFamilyIndex;
    private List<FamilyDataBean> familyList = new ArrayList<>();
    private FamilyDataBean familySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        context = this;
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
        setTitle(UtilBean.getTitleText(LabelConstants.MY_PEOPLE_ASHA_TITLE));
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
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.MY_PEOPLE_ASHA_TITLE);
        startActivityForResult(myIntent, ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE);
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
                LabelConstants.ON_MY_PEOPLE_ASHA_ACTIVITY_CLOSE_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        setSubTitle(null);
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screen) {
                case ASHA_AREA_SELECTION_SCREEN:
                    String selectedArea = spinner.getSelectedItem().toString();
                    for (LocationBean locationBean : ashaAreaList) {
                        if (selectedArea.equals(locationBean.getName())) {
                            selectedAshaArea = locationBean.getActualID();
                            break;
                        }
                    }
                    setServiceSelectionScreen();
                    break;

                case CHART_SELECTION_SCREEN:
                    selectedService = 4;
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                    addSearchTextBoxForMember();
                    retrieveMemberListFromDB(null, null);
                    break;

                case SERVICE_SELECTION_SCREEN:
                    if (selectedServiceIndex != -1) {
                        selectedService = selectedServiceIndex;
                        bodyLayoutContainer.removeAllViews();
                        bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                        addSearchTextBoxForMember();
                        if (selectedServiceIndex == 8) {
                            screen = FAMILY_SELECTION_SCREEN;
                        }
                        if (selectedServiceIndex == 7) {
                            screen = MEMBER_SELECTION_SCREEN;
                        }
                        if (selectedServiceIndex != 7 && selectedServiceIndex != 8) {
                            showProcessDialog();
                            retrieveMemberListFromDB(null, null);
                        }
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.SERVICE_SELECTION_REQUIRED_ALERT));
                    }
                    break;

                case MEMBER_SELECTION_SCREEN:
                    if (selectedService == 6) {
                        navigateToHomeScreen(false);
                        finish();
                    } else if (selectedMemberToUpdateIndex != -1) {
                        selectedMember = memberDataBeans.get(selectedMemberToUpdateIndex);
                        startDynamicFormActivity(FormConstants.FHS_MEMBER_UPDATE, selectedMember, null);
                    } else if (memberDataBeans.isEmpty()) {
                        nextButton.setText(GlobalTypes.EVENT_NEXT);
                        setServiceSelectionScreen();
                    } else {
                        if (selectedPeopleIndex != -1) {
                            showProcessDialog();
                            selectedMember = memberDataBeans.get(selectedPeopleIndex);
                            switch (selectedService) {
                                case 4:
                                    setVisits();
                                    break;
                                case 5:
                                    showNotOnlineMessage();
                                    retrieveRCHServicesProvidedToMember();
                                    break;
                                default:
                                    startDynamicFormActivity(formTypes.get(selectedServiceIndex), selectedMember, null);
                                    break;
                            }
                        } else {
                            SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_MEMBER));
                        }
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

                case VISIT_SELECTION_SCREEN:
                    setSubTitle(UtilBean.getMemberFullName(selectedMember));
                    final String formType = mapOfVisitFormWithRadioButtonId.get(selectedVisitIndex);
                    showProcessDialog();
                    if (selectedVisitIndex == -1) {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.VISIT_SELECTION_REQUIRED_ALERT));
                    } else if (formType.equals(FormConstants.TECHO_AWW_WEIGHT_GROWTH_GRAPH)) {
                        addGraph();
                    } else {
                        startDynamicFormActivity(formTypes.get(selectedServiceIndex), selectedMember, null);
                    }
                    break;
                case MOTHER_DETAILS_SCREEN:
                case RCH_REGISTER_TABLE_SCREEN:
                    navigateToHomeScreen(false);
                    break;

                default:
                    break;
            }
        }
    }

    @UiThread
    public void setVisits() {
        bodyLayoutContainer.removeAllViews();
        screen = VISIT_SELECTION_SCREEN;
        setSubTitle(UtilBean.getMemberFullName(selectedMember));
        List<String> visits = new ArrayList<>();
        visits.add(FormConstants.ASHA_CS);
        visits.add(FormConstants.TECHO_AWW_WEIGHT_GROWTH_GRAPH);

        List<ListItemDataBean> list = new ArrayList<>();
        mapOfVisitFormWithRadioButtonId = new ArrayList<>();

        for (String visit : visits) {
            list.add(new ListItemDataBean(UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(visit))));
            mapOfVisitFormWithRadioButtonId.add(visit);
        }
        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            selectedVisitIndex = position;
            onClick(nextButton);
        };
        ListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_type, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
        footerView.setVisibility(View.GONE);
        hideProcessDialog();
    }

    @UiThread
    public void addGraph() {
        setSubTitle(UtilBean.getMemberFullName(selectedMember));
        List<PointValue> yAxisValues = new ArrayList<>();
        Line line = new Line(yAxisValues).setColor(Color.BLACK);

        Gson gson = new Gson();
        MemberAdditionalInfoDataBean additionalInfo = null;
        if (selectedMember.getAdditionalInfo() != null) {
            additionalInfo = gson.fromJson(selectedMember.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
        }

        if (additionalInfo != null && additionalInfo.getWeightMap() != null && !additionalInfo.getWeightMap().isEmpty()) {
            for (Map.Entry<Long, Float> entry : additionalInfo.getWeightMap().entrySet()) {
                int[] ageYearMonthDayArray = UtilBean.calculateAgeYearMonthDayOnGivenDate(selectedMember.getDob(), entry.getKey());
                yAxisValues.add(new PointValue(ageYearMonthDayArray[1], entry.getValue()));
            }
        } else {
            View.OnClickListener listener = v -> alertDialog.dismiss();
            alertDialog = new MyAlertDialog(this,
                    LabelConstants.NO_DATA_FOUND_FOR_CHILD_GROWTH_CHART,
                    listener, DynamicUtils.BUTTON_OK);
            alertDialog.show();
            hideProcessDialog();
            return;
        }

        screen = CHART_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        LinearLayout childGrowthChart = MyDynamicComponents.getChildGrowthChart(selectedMember.getGender().equals(GlobalTypes.MALE), this);
        LineChartView lineChartView = childGrowthChart.findViewById(R.id.lineChart);

        LineChartData data = lineChartView.getLineChartData();
        List<Line> lines = data.getLines();
        lines.set(0, line);

        bodyLayoutContainer.addView(childGrowthChart);
        hideProcessDialog();
    }

    @UiThread
    public void addAshaAreaSelectionSpinner() {
        screen = ASHA_AREA_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        MaterialTextView titleTextView = MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.SELECT_ASHA_AREA);
        bodyLayoutContainer.addView(titleTextView);
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
    public void setServiceSelectionScreen() {
        screen = SERVICE_SELECTION_SCREEN;
        nextButton.setText(GlobalTypes.EVENT_NEXT);
        bodyLayoutContainer.removeAllViews();
        MaterialTextView textView = MyStaticComponents.getListTitleView(this, LabelConstants.SERVICE_TYPE_SELECTION);
        bodyLayoutContainer.addView(textView);

        formTypes.add(FormConstants.ASHA_LMPFU);
        formTypes.add(FormConstants.ASHA_ANC);
        formTypes.add(FormConstants.ASHA_WPD);
        formTypes.add(FormConstants.ASHA_PNC);
        formTypes.add(FormConstants.ASHA_CS);

        List<ListItemDataBean> serviceList = new ArrayList<>();
        serviceList.add(new ListItemDataBean(LabelConstants.SERVICE_ELIGIBLE_COUPLES));
        serviceList.add(new ListItemDataBean(LabelConstants.SERVICE_PREGNANT_WOMEN));
        serviceList.add(new ListItemDataBean(LabelConstants.SERVICE_WPD_WOMEN));
        serviceList.add(new ListItemDataBean(LabelConstants.SERVICE_PNC_WOMEN));
        serviceList.add(new ListItemDataBean(LabelConstants.SERVICE_CHILDREN));
        serviceList.add(new ListItemDataBean(LabelConstants.SERVICE_RCH_REGISTER));
        //serviceList.add(new ListItemDataBean(LabelConstants.SERVICE_NPCB_REFERRED_PATIENTS));
        //serviceList.add(new ListItemDataBean(LabelConstants.SERVICE_UPDATE_MEMBER));
        //serviceList.add(new ListItemDataBean(LabelConstants.SERVICE_ADD_MEMBER));

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            selectedServiceIndex = position;
            onClick(nextButton);
            footerView.setVisibility(View.VISIBLE);
        };
        PagingListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, serviceList, R.layout.listview_row_type, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
        footerView.setVisibility(View.GONE);
        nextButton.setOnClickListener(this);
        hideProcessDialog();
    }

    @UiThread
    public void addSearchTextBoxForMember() {
        if (selectedService == 8) {
            searchText = MyStaticComponents.getEditText(this, LabelConstants.FAMILY_ID_TO_SEARCH, 1, 15, 1);
        } else {
            searchText = MyStaticComponents.getEditTextWithQrScan(this, LabelConstants.MEMBER_ID_OR_NAME_OR_HEALTH_ID_TO_SEARCH, 1, 15, 1);
        }
        if (searchText.getEditText() != null) {
            searchText.getEditText().addTextChangedListener(new TextWatcher() {
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
                                            if (selectedService == 7
                                                    || selectedService == 8) {
                                                retrieveMemberListForUpdateBySearch(s, null);
                                            } else {
                                                retrieveMemberListFromDB(s.toString(), null);
                                            }
                                            searchText.setFocusable(true);
                                        });
                                    } else if (s == null || s.length() == 0) {
                                        runOnUiThread(() -> {
                                            showProcessDialog();
                                            if (selectedService == 7 || selectedService == 8) {
                                                bodyLayoutContainer.removeView(headerTitle);
                                                bodyLayoutContainer.removeView(noMemberAvailableView);
                                                bodyLayoutContainer.removeView(paginatedListView);
                                                hideProcessDialog();
                                            } else {
                                                retrieveMemberListFromDB(null, null);
                                            }
                                        });
                                    }
                                }
                            }, DELAY
                    );
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //not implemented
                }
            });
        }
        bodyLayoutContainer.addView(searchText);
        if (selectedService != 7 && selectedService != 8) {
            bodyLayoutContainer.addView(MyStaticComponents.getOrTextView(context));
        }
    }

    @Background
    public void retrieveMemberListForUpdateBySearch(CharSequence s, String qrData) {
        searchString = s.toString();
        qrScanFilter = SewaUtil.setQrScanFilterData(qrData);
        selectedPeopleIndex = -1;
        selectedFamilyIndex = -1;
        ashaArea = new ArrayList<>();
        ashaArea.add(selectedAshaArea);
        if (selectedService == 7) {
            offset = 0;
            memberDataBeans = fhsService.retrieveMembersByAshaArea(ashaArea, null, s, limit, offset, qrScanFilter);
            offset = offset + limit;
            addMemberListScreenForUpdateInfo();
        }
        if (selectedService == 8) {
            offset = 0;
            familyList = fhsService.retrieveFamilyDataBeansByAshaArea(ashaArea, null, s, limit, offset);
            offset = offset + limit;
            addFamilyListScreenForNewMember();
        }
    }

    @UiThread
    public void addMemberListScreenForUpdateInfo() {
        bodyLayoutContainer.removeView(noMemberAvailableView);
        bodyLayoutContainer.removeView(headerTitle);
        bodyLayoutContainer.removeView(paginatedListView);
        selectedMemberToUpdateIndex = -1;

        if (memberDataBeans != null && !memberDataBeans.isEmpty()) {
            headerTitle = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_MEMBER);
            headerTitle.setPadding(0, 50, 0, 0);
            bodyLayoutContainer.addView(headerTitle);
            List<ListItemDataBean> membersList = new ArrayList<>();
            for (MemberDataBean memberDataBean : memberDataBeans) {
                membersList.add(new ListItemDataBean(memberDataBean.getFamilyId(), memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean), null, null));
            }
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedMemberToUpdateIndex = position;

            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, membersList, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
        } else {
            bodyLayoutContainer.removeView(headerTitle);
            noMemberAvailableView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBERS_FOUND);
            bodyLayoutContainer.addView(noMemberAvailableView);
        }
    }

    @UiThread
    public void addFamilyListScreenForNewMember() {
        selectedFamilyIndex = -1;
        bodyLayoutContainer.removeView(headerTitle);
        bodyLayoutContainer.removeView(noMemberAvailableView);
        bodyLayoutContainer.removeView(paginatedListView);
        if (!familyList.isEmpty()) {
            headerTitle = MyStaticComponents.getListTitleView(this, LabelConstants.SERVICE_ADD_MEMBER);
            headerTitle.setPadding(0, 50, 0, 0);
            bodyLayoutContainer.addView(headerTitle);

            List<ListItemDataBean> famListItemDataBeans = getFamilyList(familyList);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedFamilyIndex = position;
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, famListItemDataBeans, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            noMemberAvailableView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_FAMILIES_FOUND);
            bodyLayoutContainer.addView(noMemberAvailableView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }
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

    @Background
    public void retrieveMemberListFromDB(String search, String qrData) {
        searchString = search;
        qrScanFilter = SewaUtil.setQrScanFilterData(qrData);
        ashaArea = new ArrayList<>();
        ashaArea.add(selectedAshaArea);
        offset = 0;
        selectedPeopleIndex = -1;
        switch (selectedServiceIndex) {
            case 0:
                memberDataBeans = fhsService.retrieveEligibleCouplesByAshaArea(ashaArea, null, search, limit, offset, qrScanFilter);
                break;
            case 1:
                memberDataBeans = fhsService.retrievePregnantWomenByAshaArea(ashaArea, false, null, search, limit, offset, qrScanFilter);
                break;
            case 2:
                memberDataBeans = fhsService.retrieveWPDWomenByAshaArea(ashaArea, null, search, limit, offset, qrScanFilter);
                break;
            case 3:
                memberDataBeans = fhsService.retrievePncMothersByAshaArea(ashaArea, null, search, limit, offset, qrScanFilter);
                break;
            case 4:
                memberDataBeans = fhsService.retrieveChildsBelow5YearsByAshaArea(ashaArea, false, null, search, limit, offset, qrScanFilter);
                break;
            case 5:
                memberDataBeans = fhsService.retrieveMemberListForRchRegister(ashaArea, null, search, limit, offset, qrScanFilter);
                break;
            case 6:
                memberDataBeans = fhsService.retrieveMemberListForNPCB(ashaArea, search, qrScanFilter);
                setNpcbMemberListScreen();
                return;
            default:
                break;
        }
        offset = offset + limit;
        boolean isSearch = search != null;
        setMemberSelectionScreen(isSearch);
    }

    @Override
    public void onLoadMoreItems() {
        onLoadMoreBackground();
    }

    @Background
    public void onLoadMoreBackground() {
        List<MemberDataBean> memberDataBeanList = new ArrayList<>();
        List<FamilyDataBean> familyDataBeanList = new ArrayList<>();
        switch (selectedServiceIndex) {
            case 0:
                memberDataBeanList = fhsService.retrieveEligibleCouplesByAshaArea(ashaArea, null, searchString, limit, offset, qrScanFilter);
                break;
            case 1:
                memberDataBeanList = fhsService.retrievePregnantWomenByAshaArea(ashaArea, false, null, searchString, limit, offset, qrScanFilter);
                break;
            case 2:
                memberDataBeanList = fhsService.retrieveWPDWomenByAshaArea(ashaArea, null, searchString, limit, offset, qrScanFilter);
                break;
            case 3:
                memberDataBeanList = fhsService.retrievePncMothersByAshaArea(ashaArea, null, searchString, limit, offset, qrScanFilter);
                break;
            case 4:
                memberDataBeanList = fhsService.retrieveChildsBelow5YearsByAshaArea(ashaArea, false, null, searchString, limit, offset, qrScanFilter);
                break;
            case 5:
                memberDataBeanList = fhsService.retrieveMemberListForRchRegister(ashaArea, null, searchString, limit, offset, qrScanFilter);
                break;
            case 7:
                memberDataBeanList = fhsService.retrieveMembersByAshaArea(ashaArea, null, searchString, limit, offset, qrScanFilter);
                break;
            case 8:
                familyDataBeanList = fhsService.retrieveFamilyDataBeansByAshaArea(ashaArea, null, searchString, limit, offset);
                break;
            default:
                break;
        }
        offset = offset + limit;
        if (selectedService == 8) {
            onLoadMoreUiFamilies(familyDataBeanList);
        } else {
            onLoadMoreUi(memberDataBeanList);
        }
    }

    @UiThread
    public void onLoadMoreUiFamilies(List<FamilyDataBean> familyDataBeanList) {
        if (familyDataBeanList != null && !familyDataBeanList.isEmpty()) {
            List<ListItemDataBean> familiesList = getFamilyList(familyDataBeanList);
            familyList.addAll(familyDataBeanList);
            paginatedListView.onFinishLoadingWithItem(true, familiesList);
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
        }
    }

    @UiThread
    public void onLoadMoreUi(List<MemberDataBean> memberDataBeanList) {
        List<ListItemDataBean> membersList = new ArrayList<>();

        if (memberDataBeanList != null && !memberDataBeanList.isEmpty()) {
            for (MemberDataBean memberDataBean : memberDataBeanList) {
                membersList.add(new ListItemDataBean(memberDataBean.getFamilyId(), memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean), null, null));
            }
            memberDataBeans.addAll(memberDataBeanList);
            paginatedListView.onFinishLoadingWithItem(true, membersList);
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
        }
    }

    @UiThread
    public void setNpcbMemberListScreen() {
        bodyLayoutContainer.removeAllViews();
        screen = MEMBER_SELECTION_SCREEN;
        List<ListItemDataBean> list = new ArrayList<>();

        if (memberDataBeans != null && !memberDataBeans.isEmpty()) {
            bodyLayoutContainer.addView(MyStaticComponents.getListTitleView(context, LabelConstants.SERVICE_NPCB_REFERRED_PATIENTS));
            for (MemberDataBean memberBean : memberDataBeans) {
                MemberAdditionalInfoDataBean memberAdditionalInfoDataBean = new Gson().fromJson(memberBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
                list.add(new ListItemDataBean(memberBean.getFamilyId(), memberBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberBean),
                        UtilBean.getMyLabel(LabelConstants.STATUS), memberAdditionalInfoDataBean.getNpcbStatus()));
            }
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_two_item, null, null);
            bodyLayoutContainer.addView(paginatedListView);
        } else {
            MaterialTextView noMemberTextView = MyStaticComponents.generateInstructionView(this, UtilBean.getMyLabel(LabelConstants.NO_MEMBERS_REFFERD_FOR_NPCB));
            bodyLayoutContainer.addView(noMemberTextView);
        }
        nextButton.setText(UtilBean.getMyLabel(GlobalTypes.MAIN_MENU));
        nextButton.setOnClickListener(v -> navigateToHomeScreen(false));
        hideProcessDialog();
    }

    @UiThread
    public void setMemberSelectionScreen(boolean isSearch) {
        screen = MEMBER_SELECTION_SCREEN;
        selectedMemberToUpdateIndex = -1;
        bodyLayoutContainer.removeView(headerTitle);
        bodyLayoutContainer.removeView(paginatedListView);
        bodyLayoutContainer.removeView(noMemberAvailableView);
        nextButton.setText(GlobalTypes.EVENT_NEXT);
        List<ListItemDataBean> memberList = new ArrayList<>();
        if (!memberDataBeans.isEmpty()) {
            headerTitle = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_MEMBER);
            bodyLayoutContainer.addView(headerTitle);

            for (MemberDataBean memberDataBean : memberDataBeans) {
                memberList.add(new ListItemDataBean(memberDataBean.getFamilyId(), memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean), null, null));
            }

            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedPeopleIndex = position;

            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, memberList, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            noMemberAvailableView = MyStaticComponents.generateInstructionView(context, LabelConstants.NO_MEMBERS_FOUND);
            bodyLayoutContainer.addView(noMemberAvailableView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }

        hideProcessDialog();
    }

    private void startDynamicFormActivity(final String formType, MemberDataBean memberDataBean, FamilyDataBean familyDataBean) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (!formType.equals(FormConstants.FHS_MEMBER_UPDATE)) {
            try {
                formMetaDataUtil.setMetaDataForRchFormByFormType(formType,
                        memberDataBean.getId(), memberDataBean.getFamilyId(), null, sharedPref);
            } catch(DataException e){
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
        Intent intent = new Intent(this, DynamicFormActivity_.class);
        intent.putExtra(SewaConstants.ENTITY, formType);
        startActivityForResult(intent, REQUEST_CODE_FOR_MY_PEOPLE_ASHA_ACTIVITY);
        hideProcessDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult resultForQRScanner = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        footerView.setVisibility(View.VISIBLE);
        if (requestCode == REQUEST_CODE_FOR_MY_PEOPLE_ASHA_ACTIVITY) {
            selectedPeopleIndex = -1;
            showProcessDialog();
            bodyLayoutContainer.removeAllViews();
            familyList.clear();
            bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
            if (selectedServiceIndex == 4) {
                setVisits();
            } else {
                bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                addSearchTextBoxForMember();
                if (selectedServiceIndex == 8) {
                    screen = FAMILY_SELECTION_SCREEN;
                }
                if (selectedServiceIndex == 7) {
                    screen = MEMBER_SELECTION_SCREEN;
                }
                if (selectedServiceIndex != 7 && selectedServiceIndex != 8) {
                    retrieveMemberListFromDB(null, null);
                }
            }
            hideProcessDialog();
        } else if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                selectedAshaArea = Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID));
                setServiceSelectionScreen();
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
                retrieveMemberListFromDB(null, scanningData);
            }
        } else {
            navigateToHomeScreen(false);
        }
    }

    @UiThread
    public void setMotherDetailsScreen() {
        screen = MOTHER_DETAILS_SCREEN;
        bodyLayoutContainer.removeAllViews();
        nextButton.setText(GlobalTypes.EVENT_OKAY);
        SimpleDateFormat sdf = new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault());

        bodyLayoutContainer.addView(MyStaticComponents.generateTitleView(this, UtilBean.getMyLabel(LabelConstants.MEMBERS_DETAILS)));
        if (selectedMember != null) {
            FamilyDataBean familyDataBean = fhsService.retrieveFamilyDataBeanByFamilyId(selectedMember.getFamilyId());

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel(LabelConstants.MEMBER_NAME)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMemberFullName(selectedMember)));

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel(LabelConstants.HEALTH_ID)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, selectedMember.getUniqueHealthId()));

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel(LabelConstants.FAMILY_ID)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, selectedMember.getFamilyId()));

            if (selectedMember.getMobileNumber() != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                        UtilBean.getMyLabel(LabelConstants.MOBILE_NUMBER)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, selectedMember.getMobileNumber()));
            }

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel(LabelConstants.DATE_OF_BIRTH)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, sdf.format(selectedMember.getDob())));

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel(LabelConstants.AGE)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.getAgeDisplayOnGivenDate(new Date(selectedMember.getDob()), new Date())));

            if (selectedMember.getYearOfWedding() != null) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(selectedMember.getDob());
                Integer yearOfBirth = c.get(Calendar.YEAR);
                Integer yearOfWedding = selectedMember.getYearOfWedding();
                int ageAtWedding = yearOfWedding - yearOfBirth;
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                        UtilBean.getMyLabel(LabelConstants.AGE_AT_WEDDING)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, Integer.toString(ageAtWedding)));
            }

            if (selectedMember.getLmpDate() != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                        UtilBean.getMyLabel(LabelConstants.LMP_DATE)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, sdf.format(selectedMember.getLmpDate())));
            }

            if (familyDataBean.getReligion() != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                        UtilBean.getMyLabel(LabelConstants.RELIGION)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                        fhsService.getValueOfListValuesById(familyDataBean.getReligion())));
            }

            if (familyDataBean.getCaste() != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                        UtilBean.getMyLabel(LabelConstants.CASTE)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                        fhsService.getValueOfListValuesById(familyDataBean.getCaste())));
            }

            if (familyDataBean.getBplFlag() != null && familyDataBean.getBplFlag()) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                        UtilBean.getMyLabel(LabelConstants.BPL)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, LabelConstants.FAMILY_IS_BPL));
            } else {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                        UtilBean.getMyLabel(LabelConstants.BPL)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, LabelConstants.FAMILY_IS_NOT_BPL));
            }

            String totalChildren = fhsService.getChildrenCount(Long.valueOf(selectedMember.getId()), true, false, false);
            if (totalChildren != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                        UtilBean.getMyLabel(LabelConstants.TOTAL_LIVING_CHILDREN)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, totalChildren));
            }

            String totalMaleChildren = fhsService.getChildrenCount(Long.valueOf(selectedMember.getId()), false, true, false);
            if (totalMaleChildren != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                        UtilBean.getMyLabel(LabelConstants.TOTAL_MALE_CHILDREN)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, totalMaleChildren));
            }


            String totalFemaleChildren = fhsService.getChildrenCount(Long.valueOf(selectedMember.getId()), false, false, true);
            if (totalFemaleChildren != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                        UtilBean.getMyLabel(LabelConstants.TOTAL_FEMALE_CHILDREN)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, totalFemaleChildren));
            }

            MemberDataBean latestChild = fhsService.getLatestChildByMotherId(Long.valueOf(selectedMember.getId()));
            if (latestChild != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                        UtilBean.getMyLabel(LabelConstants.LATEST_CHILD_NAME)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMemberFullName(latestChild)));

                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                        UtilBean.getMyLabel(LabelConstants.LATEST_CHILD_GENDER)));
                if (latestChild.getGender().equals(GlobalTypes.MALE)) {
                    bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, LabelConstants.MALE));
                } else {
                    bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, LabelConstants.FEMALE));
                }

                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                        UtilBean.getMyLabel(LabelConstants.LATEST_CHILD_AGE)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                        UtilBean.getAgeDisplayOnGivenDate(new Date(latestChild.getDob()), new Date())));
            }

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel(LabelConstants.ADDRESS)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.getFamilyFullAddress(familyDataBean)));
        }
    }

    @UiThread
    public void showNotOnlineMessage() {
        if (!sewaService.isOnline()) {
            hideProcessDialog();
            View.OnClickListener myListener = v -> {
                alertDialog.dismiss();
                hideProcessDialog();
                bodyLayoutContainer.removeAllViews();
                memberDataBeans.clear();
                setServiceSelectionScreen();
                nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                nextButton.setOnClickListener(MyPeopleAshaActivity.this);
            };
            alertDialog = new MyAlertDialog(this, false,
                    UtilBean.getMyLabel(LabelConstants.NETWORK_CONNECTIVITY_ALERT),
                    myListener, DynamicUtils.BUTTON_OK);
            alertDialog.show();
        }
    }

    @Background
    public void retrieveRCHServicesProvidedToMember() {
        screen = RCH_REGISTER_TABLE_SCREEN;
        setSubTitle(UtilBean.getMemberFullName(selectedMember));
        LinkedHashMap<String, List<LinkedHashMap<String, Object>>> resultMap = new LinkedHashMap<>();
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
            parameter.put("memberId", selectedMember.getId());

            if (new Date(selectedMember.getDob()).after(before5Years)) {
                // Member is a child
                request.put("Child Services Provided", new QueryMobDataBean("mob_asha_child_services_provided", null, parameter, 0));
            } else if (new Date(selectedMember.getDob()).before(before5Years)
                    && new Date(selectedMember.getDob()).after(before15Years)) {
                setServicesProvidedScreenForRchRegister(null);
            } else if (new Date(selectedMember.getDob()).before(before15Years)
                    && new Date(selectedMember.getDob()).after(before49Years)
                    && selectedMember.getGender().equalsIgnoreCase("F")
                    && selectedMember.getMaritalStatus().equalsIgnoreCase("629")) {
                // Member is an Eligible couple
                request.put("LMP Follow Up Services Provided", new QueryMobDataBean("mob_asha_lmp_services_provided", null, parameter, 0));
                request.put("ANC Services Provided", new QueryMobDataBean("mob_asha_anc_services_provided", null, parameter, 0));
                request.put("WPD Services Provided", new QueryMobDataBean("mob_asha_wpd_services_provided", null, parameter, 0));
                request.put("PNC Services Provided", new QueryMobDataBean("mob_asha_pnc_services_provided", null, parameter, 0));
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
        setServicesProvidedScreenForRchRegister(resultMap);
    }

    @UiThread
    public void setServicesProvidedScreenForRchRegister(Map<String, List<LinkedHashMap<String, Object>>> resultMap) {
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.MEMBER_NAME));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMemberFullName(selectedMember)));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.HEALTH_ID));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, selectedMember.getUniqueHealthId()));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.FAMILY_ID));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, selectedMember.getFamilyId()));
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
        }
        hideProcessDialog();
    }

    private void createTableLayout(List<String> headNames, List<LinkedHashMap<String, Object>> resultList) {
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setPadding(10, 10, 10, 10);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        layoutParams = new TableRow.LayoutParams(0, MATCH_PARENT, 1);

        TableRow row = new TableRow(this);
        row.setPadding(10, 15, 10, 15);
        row.setLayoutParams(new TableRow.LayoutParams(MATCH_PARENT));

        for (String string : headNames) {
            MaterialTextView textView = new MaterialTextView(this);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(10, 10, 10, 10);
            textView.setText(UtilBean.getMyLabel(string));
            textView.setTypeface(Typeface.DEFAULT_BOLD);
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
        layoutParams = new TableRow.LayoutParams(0, MATCH_PARENT, 1);

        TableRow row = new TableRow(this);
        row.setPadding(10, 15, 10, 15);
        if (SewaUtil.CURRENT_THEME == R.style.techo_training_app) {
            row.setBackgroundResource(R.drawable.table_row_selector);
        } else {
            row.setBackgroundResource(R.drawable.spinner_item_border);
        }
        row.setLayoutParams(layoutParamsForRow);

        for (String string : headNames) {
            MaterialTextView textView = new MaterialTextView(this);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(10, 10, 10, 10);
            Object tmpObject = map.get(string);
            if (tmpObject != null) {
                textView.setText(tmpObject.toString());
            } else {
                textView.setText(LabelConstants.N_A);
            }
            row.addView(textView, layoutParams);
        }
        TableRow.LayoutParams layoutParamsForIcon = new TableRow.LayoutParams(0, MATCH_PARENT, 0.5f);
        ImageView imageView = MyStaticComponents.getImageView(context, -1, R.drawable.ic_chevron_right, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setMaxWidth(30);
        imageView.setMaxHeight(30);
        imageView.setImageTintList(ContextCompat.getColorStateList(context, R.color.listview_text_color_selector));
        row.addView(imageView, layoutParamsForIcon);
        row.setOnClickListener(v -> {
            Object visitId = map.get("hiddenVisitId");
            Object serviceType = map.get("hiddenServiceType");
            if (visitId != null && serviceType != null) {
                Intent intent = new Intent(context, WorkRegisterLineListActivity_.class);
                intent.putExtra("visitId", visitId.toString());
                intent.putExtra("serviceType", serviceType.toString());
                startActivity(intent);
            }
        });

        tableLayout.addView(row, index);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (screen == null || screen.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            bodyLayoutContainer.removeAllViews();
            switch (screen) {
                case MOTHER_DETAILS_SCREEN:
                case RCH_REGISTER_TABLE_SCREEN:
                    showProcessDialog();
                    setSubTitle(null);
                    bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                    addSearchTextBoxForMember();
                    selectedPeopleIndex = -1;
                    retrieveMemberListFromDB(null, null);
                    break;

                case MEMBER_SELECTION_SCREEN:
                case FAMILY_SELECTION_SCREEN:
                    selectedServiceIndex = -1;
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    memberDataBeans.clear();
                    familyList.clear();
                    setServiceSelectionScreen();
                    break;

                case SERVICE_SELECTION_SCREEN:
                    startLocationSelectionActivity();
                    break;

                case ASHA_AREA_SELECTION_SCREEN:
                    navigateToHomeScreen(false);
                    break;

                case VISIT_SELECTION_SCREEN:
                    selectedService = 4;
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    setSubTitle(null);
                    bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                    addSearchTextBoxForMember();
                    retrieveMemberListFromDB(null, null);
                    footerView.setVisibility(View.VISIBLE);
                    break;

                case CHART_SELECTION_SCREEN:
                    setVisits();
                    break;

                default:
            }
            return true;
        }
        return false;
    }

}
