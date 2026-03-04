package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
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
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberAdditionalInfoDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.exception.DataException;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.FormMetaDataUtil;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by prateek on 30/7/18.
 */

@EActivity
public class HighRiskPregnancyActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    private static final Integer REQUEST_CODE_FOR_HIGH_RISK_PREGNANCY_ACTIVITY = 200;
    private static final String VILLAGE_SELECTION_SCREEN = "villageSelectionScreen";
    private static final String HIGH_RISK_WOMEN_AND_CHILD_SELECTION_SCREEN = "highRiskWomenAndChildSelectionScreen";
    private static final String VISIT_SELECTION_SCREEN = "visitSelectionScreen";
    private static final String TYPE_SELECTION_SCREEN = "typeSelectionScreen";
    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    FormMetaDataUtil formMetaDataUtil;
    private List<Integer> selectedVillages = new LinkedList<>();
    private LinearLayout bodyLayoutContainer;
    private LinearLayout footerLayout;
    private Button nextButton;
    private Spinner villageSpinner;
    private Spinner ashaAreaSpinner;
    private String screen;
    private MemberDataBean memberSelected = null;
    private List<LocationBean> villageList = new ArrayList<>();
    private List<LocationBean> ashaAreaList = new ArrayList<>();
    private List<Integer> selectedAshaAreas = new ArrayList<>();
    private Integer selectedVillage;
    private List<MemberDataBean> highRiskPregnantWomen = new ArrayList<>();
    private List<MemberDataBean> highRiskChildren = new ArrayList<>();
    private List<String> visitForms;
    private MyAlertDialog myAlertDialog;
    private int selectedWomanIndex = -1;
    private int selectedChildIndex = -1;
    private int selectedTypeIndex = -1;
    private int selectedVisitIndex = -1;
    private long limit = 30;
    private long offset;
    private PagingListView womenListView;
    private PagingListView childrenListView;
    private LinearLayout globalPanel;
    private List<String> visits;
    private LinkedHashMap<String, String> qrScanFilter = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setTitle(UtilBean.getTitleText(LabelConstants.HIGH_RISK_WOMEN_AND_CHILD_TITLE));
    }

    private void initView() {
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        footerLayout = globalPanel.findViewById(DynamicUtils.ID_FOOTER);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        setBodyDetail();
    }

    @Background
    public void setBodyDetail() {
        startLocationSelectionActivity();
    }

    private void startLocationSelectionActivity() {
        Intent myIntent = new Intent(context, LocationSelectionActivity_.class);
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.HIGH_RISK_WOMEN_AND_CHILD_TITLE);
        startActivityForResult(myIntent, ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screen) {
                case VILLAGE_SELECTION_SCREEN:
                    setSubTitle(null);
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
                    setSelectionType();
                    break;
                case HIGH_RISK_WOMEN_AND_CHILD_SELECTION_SCREEN:
                    visits = new ArrayList<>();
                    setSubTitle(null);
                    if (selectedChildIndex != -1) {
                        bodyLayoutContainer.removeAllViews();
                        visits.add(FormConstants.TECHO_FHW_CS);
                        memberSelected = highRiskChildren.get(selectedChildIndex);
                        setVisitSelectionScreen(visits);
                    } else if (selectedWomanIndex != -1) {
                        bodyLayoutContainer.removeAllViews();
                        visits.add(FormConstants.TECHO_FHW_ANC);
                        memberSelected = highRiskPregnantWomen.get(selectedWomanIndex);
                        setVisitSelectionScreen(visits);
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_MEMBER));
                    }
                    break;

                case VISIT_SELECTION_SCREEN:
                    if (selectedVisitIndex != -1) {
                        startDynamicFormActivity(visitForms.get(selectedVisitIndex), memberSelected);
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.VISIT_SELECTION_REQUIRED_ALERT));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @UiThread
    public void addVillageSelectionSpinner() {
        setSubTitle(null);
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.SELECT_VILLAGE)));
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
                //add something
            }
        });
        bodyLayoutContainer.addView(villageSpinner);
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.SELECT_ASHA_AREA)));
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

    @Background
    public void retrieveHighRiskWomenAndChildList() {
        selectedVillages.add(selectedVillage);
        offset = 0;
        if (selectedTypeIndex == 0) {
            highRiskPregnantWomen = fhsService.retrievePregnantWomenByAshaArea(selectedAshaAreas, true, selectedVillages, null, limit, offset, qrScanFilter);
        } else {
            highRiskChildren = fhsService.retrieveChildsBelow5YearsByAshaArea(selectedAshaAreas, true, selectedVillages, null, limit, offset, qrScanFilter);
        }
        offset = offset + limit;
        setHighRiskWomenAndChildSelectionScreen();
    }

    @UiThread
    public void setSelectionType() {
        bodyLayoutContainer.removeAllViews();

        screen = TYPE_SELECTION_SCREEN;
        List<ListItemDataBean> items = new ArrayList<>();
        items.add(new ListItemDataBean(LabelConstants.HIGH_RISK_WOMEN));
        items.add(new ListItemDataBean(LabelConstants.HIGH_RISK_CHILDREN));

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            selectedTypeIndex = position;
            footerLayout.setVisibility(View.VISIBLE);
            retrieveHighRiskWomenAndChildList();
        };
        PagingListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, items, R.layout.listview_row_type, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
        footerLayout.setVisibility(View.GONE);
    }

    @UiThread
    public void setHighRiskWomenAndChildSelectionScreen() {
        screen = HIGH_RISK_WOMEN_AND_CHILD_SELECTION_SCREEN;
        setSubTitle(null);
        bodyLayoutContainer.removeAllViews();

        switch (selectedTypeIndex) {
            case 0:
                List<ListItemDataBean> highRiskPregnantWomenList = addHighRiskPregnantWomenList(highRiskPregnantWomen);
                if (highRiskPregnantWomenList.isEmpty()) {
                    nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
                    nextButton.setOnClickListener(v -> navigateToHomeScreen(false));
                } else {
                    bodyLayoutContainer.addView(MyStaticComponents.getListTitleView(context, LabelConstants.SELECT_FROM_LIST));
                    AdapterView.OnItemClickListener onWomanItemClickListener = (parent, view, position, id) -> selectedWomanIndex = position;
                    womenListView = MyStaticComponents.getPaginatedListViewWithItem(context, highRiskPregnantWomenList, R.layout.listview_row_with_item, onWomanItemClickListener, this);
                    bodyLayoutContainer.addView(womenListView);
                }
                break;
            case 1:
                List<ListItemDataBean> highRiskChildrenList = addHighRiskChildrenList(highRiskChildren);
                if (highRiskChildrenList.isEmpty()) {
                    nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
                    nextButton.setOnClickListener(v -> navigateToHomeScreen(false));
                } else {
                    bodyLayoutContainer.addView(MyStaticComponents.getListTitleView(context, LabelConstants.SELECT_FROM_LIST));
                    AdapterView.OnItemClickListener onChildItemClickListener = (parent, view, position, id) -> selectedChildIndex = position;
                    childrenListView = MyStaticComponents.getPaginatedListViewWithItem(context, highRiskChildrenList, R.layout.listview_row_with_item, onChildItemClickListener, this);
                    bodyLayoutContainer.addView(childrenListView);
                }
                break;
            default:
                break;
        }
        hideProcessDialog();
    }

    private List<ListItemDataBean> addHighRiskPregnantWomenList(List<MemberDataBean> highRiskPregnantWomen) {
        List<ListItemDataBean> list = new ArrayList<>();
        if (highRiskPregnantWomen != null && !highRiskPregnantWomen.isEmpty()) {
            for (MemberDataBean memberDataBean : highRiskPregnantWomen) {
                list.add(new ListItemDataBean(memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean)));
            }
        } else {
            MaterialTextView noWomenAvailableTextView = MyStaticComponents.generateInstructionView(this, UtilBean.getMyLabel(LabelConstants.NO_HIGH_RISK_PREGNANT_WOMEN_IN_AREA));
            bodyLayoutContainer.addView(noWomenAvailableTextView);
        }
        return list;
    }

    private List<ListItemDataBean> addHighRiskChildrenList(List<MemberDataBean> highRiskChildren) {
        List<ListItemDataBean> list = new ArrayList<>();
        if (highRiskChildren != null && !highRiskChildren.isEmpty()) {
            for (MemberDataBean memberDataBean : highRiskChildren) {
                list.add(new ListItemDataBean(memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean)));
            }
        } else {
            MaterialTextView noChildAvailableTextView = MyStaticComponents.generateInstructionView(this, UtilBean.getMyLabel(LabelConstants.NO_HIGH_RISK_CHILDREN_IN_AREA));
            bodyLayoutContainer.addView(noChildAvailableTextView);
        }
        return list;
    }

    private void setVisitSelectionScreen(List<String> visits) {
        screen = VISIT_SELECTION_SCREEN;
        setSubTitle(UtilBean.getMemberFullName(memberSelected));

        List<ListItemDataBean> items = new ArrayList<>();
        visitForms = new ArrayList<>();
        LinearLayout detailsLayout = MyStaticComponents.getDetailsLayout(context, -1, LinearLayout.VERTICAL, bodyLayoutContainer.getPaddingTop());

        detailsLayout.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.MEMBER_NAME));
        detailsLayout.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMemberFullName(memberSelected)));
        detailsLayout.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.HIGH_RISK_REASON));

        Gson gson = new Gson();
        MemberAdditionalInfoDataBean memberAdditionalInfo = gson.fromJson(memberSelected.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
        if (memberAdditionalInfo != null && memberAdditionalInfo.getHighRiskReasons() != null && !memberAdditionalInfo.getHighRiskReasons().isEmpty()) {
            String[] split = memberAdditionalInfo.getHighRiskReasons().split(GlobalTypes.COMMA);

            int count = 1;
            for (String s : split) {
                detailsLayout.addView(MyStaticComponents.generateAnswerView(this, count + ". " + UtilBean.getMyLabel(s.trim())));
                count++;
            }
        } else {
            detailsLayout.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMyLabel(LabelConstants.NOT_AVAILABLE)));
        }

        bodyLayoutContainer.addView(detailsLayout);
        bodyLayoutContainer.addView(MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_VISIT));

        for (String visit : visits) {
            items.add(new ListItemDataBean(UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(visit)), null));
            visitForms.add(visit);
        }
        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedVisitIndex = position;
        PagingListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, items, R.layout.listview_row_with_item, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
    }

    private void startDynamicFormActivity(String formType, MemberDataBean memberDataBean) {
        setSubTitle(UtilBean.getMemberFullName(memberSelected));
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            formMetaDataUtil.setMetaDataForRchFormByFormType(formType, memberDataBean.getId(), memberDataBean.getFamilyId(), null, sharedPref);
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
        Intent myIntent = new Intent(this, DynamicFormActivity_.class);
        myIntent.putExtra(SewaConstants.ENTITY, visitForms.get(selectedVisitIndex));
        startActivityForResult(myIntent, REQUEST_CODE_FOR_HIGH_RISK_PREGNANCY_ACTIVITY);
        selectedVisitIndex = -1;
        hideProcessDialog();
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
                LabelConstants.WANT_TO_CLOSE_HIGH_RISK_PREGNANCY,
                myListener, DynamicUtils.BUTTON_YES_NO);
        myAlertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_HIGH_RISK_PREGNANCY_ACTIVITY) {
            showProcessDialog();
            bodyLayoutContainer.removeAllViews();
            setVisitSelectionScreen(visits);
            hideProcessDialog();
        } else if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Integer locationId = Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID));
                selectedAshaAreas.clear();
                selectedAshaAreas.add(locationId);
                setSubTitle(null);
                setSelectionType();
            } else {
                navigateToHomeScreen(false);
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
                case VISIT_SELECTION_SCREEN:
                    showProcessDialog();
                    selectedWomanIndex = -1;
                    selectedChildIndex = -1;
                    retrieveHighRiskWomenAndChildList();
                    break;

                case TYPE_SELECTION_SCREEN:
                    startLocationSelectionActivity();
                    break;

                case VILLAGE_SELECTION_SCREEN:
                    navigateToHomeScreen(false);
                    break;

                case HIGH_RISK_WOMEN_AND_CHILD_SELECTION_SCREEN:
                    setSelectionType();
                    selectedTypeIndex = -1;
                    nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
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
        List<MemberDataBean> membersDataBeanList;
        switch (selectedTypeIndex) {
            case 0:
                membersDataBeanList = fhsService.retrievePregnantWomenByAshaArea(selectedAshaAreas, true, selectedVillages, null, limit, offset, qrScanFilter);
                offset = offset + limit;
                onLoadMoreUi(membersDataBeanList);
                break;
            case 1:
                membersDataBeanList = fhsService.retrieveChildsBelow5YearsByAshaArea(selectedAshaAreas, true, selectedVillages, null, limit, offset, qrScanFilter);
                offset = offset + limit;
                onLoadMoreUi(membersDataBeanList);
                break;
            default:
        }
    }

    @UiThread
    public void onLoadMoreUi(List<MemberDataBean> membersDataBeanList) {
        switch (selectedTypeIndex) {
            case 0:
                if (!membersDataBeanList.isEmpty()) {
                    highRiskPregnantWomen.addAll(membersDataBeanList);
                    List<ListItemDataBean> list = addHighRiskPregnantWomenList(membersDataBeanList);
                    womenListView.onFinishLoadingWithItem(true, list);
                } else {
                    womenListView.onFinishLoadingWithItem(false, null);
                }
                break;
            case 1:
                if (!membersDataBeanList.isEmpty()) {
                    highRiskChildren.addAll(membersDataBeanList);
                    List<ListItemDataBean> list = addHighRiskChildrenList(membersDataBeanList);
                    childrenListView.onFinishLoadingWithItem(true, list);
                } else {
                    childrenListView.onFinishLoadingWithItem(false, null);
                }
                break;
            default:
        }
    }
}
