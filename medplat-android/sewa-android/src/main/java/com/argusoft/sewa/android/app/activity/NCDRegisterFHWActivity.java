package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.NcdServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.databean.FieldValueMobDataBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberAdditionalInfoDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prateek on 16/1/19.
 */

@EActivity
public class NCDRegisterFHWActivity extends MenuActivity implements View.OnClickListener {

    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    NcdServiceImpl ncdService;

    private static final String VILLAGE_SELECTION_SCREEN = "villageSelectionScreen";
    private static final String SCREENING_SELECTION_SCREEN = "screeningSelectionScreen";
    private static final String MEMBER_LIST_SCREEN = "memberListScreen";
    private static final String SCREENING_DIABETES = "diabetes";
    private static final String SCREENING_HYPERTENSION = "hypertension";
    private static final String SCREENING_ORAL = "oral";
    private static final String SCREENING_CERVICAL = "cervical";
    private static final String SCREENING_BREAST = "breast";
    private static final String SCREENING_MENTAL_HEALTH = "mentalHealth";

    private List<MemberBean> memberBeans;
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private String screen;
    private String selectedScreening;
    private List<LocationBean> villageList = new ArrayList<>();
    private List<LocationBean> ashaAreaList = new ArrayList<>();
    private Integer selectedVillage;
    private List<Integer> selectedAshaAreas = new ArrayList<>();
    private Spinner villageSpinner;
    private Spinner ashaAreaSpinner;
    private ArrayList<ListItemDataBean> memberList;
    private ListView memberListView;
    private LinearLayout globalPanel;
    private LinearLayout footerView;

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
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        setTitle(UtilBean.getTitleText(LabelConstants.NCD_REGISTER_TITLE));
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        setContentView(globalPanel);
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
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.NCD_REGISTER_TITLE);
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
                    selectedAshaAreas = new ArrayList<>();
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
                    setScreeningSelectionScreen();
                    break;

                case MEMBER_LIST_SCREEN:
                    bodyLayoutContainer.removeAllViews();
                    setScreeningSelectionScreen();
                    nextButton.setText(GlobalTypes.EVENT_NEXT);
                    break;

                default:
                    break;
            }
        }
    }

    @UiThread
    public void addVillageSelectionSpinner() {
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
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.SELECT_ASHA_AREA)));
        addAshaAreaSelectionSpinner();
    }

    private void addAshaAreaSelectionSpinner() {
        String[] arrayOfOptions = new String[ashaAreaList.size() + 1];
        arrayOfOptions[0] = "All";
        int i = 1;
        for (LocationBean locationBean : ashaAreaList) {
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }
        ashaAreaSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 3);
        bodyLayoutContainer.addView(ashaAreaSpinner);
        hideProcessDialog();
    }

    private void setScreeningSelectionScreen() {
        screen = SCREENING_SELECTION_SCREEN;
        memberList = new ArrayList<>();
        bodyLayoutContainer.removeAllViews();

        MaterialTextView textView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_TYPE_OF_DISEASE);
        bodyLayoutContainer.addView(textView);

        ArrayList<ListItemDataBean> list = new ArrayList<>();
        list.add(new ListItemDataBean(LabelConstants.SUSPECTED_FOR_DIABETES));
        list.add(new ListItemDataBean(LabelConstants.SUSPECTED_FOR_HYPERTENSION));
        list.add(new ListItemDataBean(LabelConstants.SUSPECTED_FOR_MENTAL_HEALTH_ISSUES));
//        list.add(new ListItemDataBean(LabelConstants.SUSPECTED_FOR_ORAL_CANCER));
//        list.add(new ListItemDataBean(LabelConstants.SUSPECTED_FOR_CERVICAL_CANCER));
//        list.add(new ListItemDataBean(LabelConstants.SUSPECTED_FOR_BREAST_CANCER));

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            if (position != -1) {
                switch (position) {
                    case 0:
                        selectedScreening = SCREENING_DIABETES;
                        break;
                    case 1:
                        selectedScreening = SCREENING_HYPERTENSION;
                        break;
                    case 2:
                        selectedScreening = SCREENING_MENTAL_HEALTH;
                        break;
                    case 3:
                        selectedScreening = SCREENING_ORAL;
                        break;
                    case 4:
                        selectedScreening = SCREENING_CERVICAL;
                        break;
                    case 5:
                        selectedScreening = SCREENING_BREAST;
                        break;
                    default:
                        break;
                }
                retrieveMemberList();
                nextButton.setText(GlobalTypes.EVENT_OKAY);
                footerView.setVisibility(View.VISIBLE);
            }
        };

        PagingListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_type, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
        footerView.setVisibility(View.GONE);
        hideProcessDialog();
    }

    @Background
    public void retrieveMemberList() {
        showProcessDialog();
        memberBeans = ncdService.retrieveMembersForNcdRegisterFhw(selectedVillage, selectedAshaAreas, selectedScreening);
        setMemberListScreen();
    }

    @UiThread
    public void setMemberListScreen() {
        screen = MEMBER_LIST_SCREEN;
        bodyLayoutContainer.removeAllViews();
        switch (selectedScreening) {
            case SCREENING_DIABETES:
                addSuspectedDiabetesScreenedMemberList();
                break;
            case SCREENING_HYPERTENSION:
                addHypertensionScreenedMemberList();
                break;
            case SCREENING_ORAL:
                addOralScreenedMemberList();
                break;
            case SCREENING_CERVICAL:
                addCervicalScreenedMemberList();
                break;
            case SCREENING_BREAST:
                addBreastScreenedMemberList();
                break;
            case SCREENING_MENTAL_HEALTH:
                addMentalHealthScreenedMemberList();
                break;
            default:
                break;
        }
        hideProcessDialog();
    }

    private void addSuspectedDiabetesScreenedMemberList() {
        bodyLayoutContainer.removeAllViews();

        if (memberBeans != null && !memberBeans.isEmpty()) {
            for (MemberBean memberBean : memberBeans) {
                if (memberBean.getAdditionalInfo() != null && !memberBean.getAdditionalInfo().isEmpty()) {
                    MemberAdditionalInfoDataBean memberAdditionalInfoDataBean = new Gson().fromJson(memberBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
                    if (memberAdditionalInfoDataBean.getSuspectedForDiabetes() != null && memberAdditionalInfoDataBean.getSuspectedForDiabetes()) {
                        memberList.add(new ListItemDataBean(memberBean.getFamilyId(), memberBean.getUniqueHealthId(),
                                UtilBean.getMemberFullName(memberBean), UtilBean.getMyLabel(LabelConstants.BLOOD_SUGAR),
                                memberAdditionalInfoDataBean.getBloodSugar() == null ? UtilBean.getMyLabel(LabelConstants.NOT_AVAILABLE) : memberAdditionalInfoDataBean.getBloodSugar() + " mg/dl"));
                    }
                }
            }
            if (memberList.isEmpty()) {
                bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBER_SUSPECTED_FOR_DIABETES_IN_AREA));
            } else {
                MaterialTextView textView = MyStaticComponents.getListTitleView(this, LabelConstants.SUSPECTED_FOR_DIABETES);
                bodyLayoutContainer.addView(textView);
                memberListView = MyStaticComponents.getPaginatedListViewWithItem(context, memberList, R.layout.listview_row_with_two_item, null, null);
                bodyLayoutContainer.addView(memberListView);
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBER_IN_YOUR_AREA));
        }
    }

    private void addHypertensionScreenedMemberList() {
        bodyLayoutContainer.removeAllViews();

        if (memberBeans != null && !memberBeans.isEmpty()) {
            memberList.clear();
            for (MemberBean memberBean : memberBeans) {
                if (memberBean.getAdditionalInfo() != null && !memberBean.getAdditionalInfo().isEmpty()) {
                    MemberAdditionalInfoDataBean memberAdditionalInfoDataBean = new Gson().fromJson(memberBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
                    if (memberAdditionalInfoDataBean.getSuspectedForHypertension() != null && memberAdditionalInfoDataBean.getSuspectedForHypertension()) {
                        memberList.add(new ListItemDataBean(memberBean.getFamilyId(), memberBean.getUniqueHealthId(),
                                UtilBean.getMemberFullName(memberBean), UtilBean.getMyLabel(LabelConstants.BLOOD_PRESSURE),
                                (memberAdditionalInfoDataBean.getSystolicBp() != null && memberAdditionalInfoDataBean.getDiastolicBp() != null) ?
                                        memberAdditionalInfoDataBean.getSystolicBp() + "/" + memberAdditionalInfoDataBean.getDiastolicBp() + " " + LabelConstants.UNIT_OF_PRESSURE :
                                        UtilBean.getMyLabel(LabelConstants.NOT_AVAILABLE)));
                    }
                }
            }
            if (memberList.isEmpty()) {
                bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBER_SUSPECTED_FOR_HYPERTENSION_IN_AREA));
            } else {
                MaterialTextView textView = MyStaticComponents.getListTitleView(this, LabelConstants.SUSPECTED_FOR_HYPERTENSION);
                bodyLayoutContainer.addView(textView);
                memberListView = MyStaticComponents.getPaginatedListViewWithItem(context, memberList, R.layout.listview_row_with_two_item, null, null);
                bodyLayoutContainer.addView(memberListView);
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBER_IN_YOUR_AREA));
        }
    }

    private void addOralScreenedMemberList() {
        bodyLayoutContainer.removeAllViews();

        if (memberBeans != null && !memberBeans.isEmpty()) {
            memberList.clear();
            for (MemberBean memberBean : memberBeans) {
                if (memberBean.getAdditionalInfo() != null && !memberBean.getAdditionalInfo().isEmpty()) {
                    MemberAdditionalInfoDataBean memberAdditionalInfoDataBean = new Gson().fromJson(memberBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
                    if (memberAdditionalInfoDataBean.getSuspectedForOralCancer() != null && memberAdditionalInfoDataBean.getSuspectedForOralCancer()) {
                        memberList.add(new ListItemDataBean(memberBean.getFamilyId(), memberBean.getUniqueHealthId(),
                                UtilBean.getMemberFullName(memberBean), null, null));
                    }
                }
            }
            if (memberList.isEmpty()) {
                bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBER_SUSPECTED_FOR_ORAL_CANCER_IN_AREA));
            } else {
                MaterialTextView textView = MyStaticComponents.getListTitleView(this, LabelConstants.SUSPECTED_FOR_ORAL_CANCER);
                bodyLayoutContainer.addView(textView);

                memberListView = MyStaticComponents.getPaginatedListViewWithItem(context, memberList, R.layout.listview_row_with_two_item, null, null);
                bodyLayoutContainer.addView(memberListView);
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBER_IN_YOUR_AREA));
        }
    }

    private void addCervicalScreenedMemberList() {
        bodyLayoutContainer.removeAllViews();

        if (memberBeans != null && !memberBeans.isEmpty()) {
            memberList.clear();
            for (MemberBean memberBean : memberBeans) {
                if (memberBean.getAdditionalInfo() != null && !memberBean.getAdditionalInfo().isEmpty()) {
                    MemberAdditionalInfoDataBean memberAdditionalInfoDataBean = new Gson().fromJson(memberBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
                    if (memberAdditionalInfoDataBean.getSuspectedForCervicalCancer() != null && memberAdditionalInfoDataBean.getSuspectedForCervicalCancer()) {
                        memberList.add(new ListItemDataBean(memberBean.getFamilyId(), memberBean.getUniqueHealthId(),
                                UtilBean.getMemberFullName(memberBean), null, null));
                    }
                }
            }
            if (memberList.isEmpty()) {
                bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBER_SUSPECTED_FOR_CERVICAL_CANCER_IN_AREA));
            } else {
                MaterialTextView textView = MyStaticComponents.getListTitleView(this, LabelConstants.SUSPECTED_FOR_CERVICAL_CANCER);
                bodyLayoutContainer.addView(textView);

                memberListView = MyStaticComponents.getPaginatedListViewWithItem(context, memberList, R.layout.listview_row_with_two_item, null, null);
                bodyLayoutContainer.addView(memberListView);
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBER_IN_YOUR_AREA));
        }
    }

    private void addBreastScreenedMemberList() {
        bodyLayoutContainer.removeAllViews();

        if (memberBeans != null && !memberBeans.isEmpty()) {
            memberList.clear();
            for (MemberBean memberBean : memberBeans) {
                if (memberBean.getAdditionalInfo() != null && !memberBean.getAdditionalInfo().isEmpty()) {
                    MemberAdditionalInfoDataBean memberAdditionalInfoDataBean = new Gson().fromJson(memberBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
                    if (memberAdditionalInfoDataBean.getSuspectedForBreastCancer() != null && memberAdditionalInfoDataBean.getSuspectedForBreastCancer()) {
                        memberList.add(new ListItemDataBean(memberBean.getFamilyId(), memberBean.getUniqueHealthId(),
                                UtilBean.getMemberFullName(memberBean), null, null));
                    }
                }
            }

            if (memberList.isEmpty()) {
                bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBER_SUSPECTED_FOR_BREAST_CANCER_IN_AREA));
            } else {
                MaterialTextView textView = MyStaticComponents.getListTitleView(this, LabelConstants.SUSPECTED_FOR_BREAST_CANCER);
                bodyLayoutContainer.addView(textView);

                memberListView = MyStaticComponents.getPaginatedListViewWithItem(context, memberList, R.layout.listview_row_with_two_item, null, null);
                bodyLayoutContainer.addView(memberListView);
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBER_IN_YOUR_AREA));
        }
    }

    private void addMentalHealthScreenedMemberList() {
        bodyLayoutContainer.removeAllViews();

        if (memberBeans != null && !memberBeans.isEmpty()) {
            memberList.clear();
            for (MemberBean memberBean : memberBeans) {
                if (memberBean.getAdditionalInfo() != null && !memberBean.getAdditionalInfo().isEmpty()) {
                    MemberAdditionalInfoDataBean memberAdditionalInfoDataBean = new Gson().fromJson(memberBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
                    if (memberAdditionalInfoDataBean.getSuspectedForMentalHealth() != null && memberAdditionalInfoDataBean.getSuspectedForMentalHealth()) {
                        FieldValueMobDataBean observation = null;
                        if (memberAdditionalInfoDataBean.getMentalHealthObservation() != null) {
                            List<FieldValueMobDataBean> labelDataBeans = SharedStructureData.sewaService.getFieldValueMobDataBeanByDataMap("mentalHealthObservationList");
                            for (FieldValueMobDataBean data: labelDataBeans) {
                                if (data.getIdOfValue() == Integer.parseInt(memberAdditionalInfoDataBean.getMentalHealthObservation())) {
                                    observation = data;
                                }
                            }
                        }
                        memberList.add(new ListItemDataBean(memberBean.getFamilyId(), memberBean.getUniqueHealthId(),
                                UtilBean.getMemberFullName(memberBean), UtilBean.getMyLabel(LabelConstants.OBSERVATION),
                                observation != null ? observation.getValue() : UtilBean.getMyLabel(LabelConstants.NOT_AVAILABLE)));
                    }
                }
            }
            if (memberList.isEmpty()) {
                bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBER_SUSPECTED_FOR_MENTAL_HEALTH_IN_AREA));
            } else {
                MaterialTextView textView = MyStaticComponents.getListTitleView(this, LabelConstants.SUSPECTED_FOR_MENTAL_HEALTH_ISSUES);
                bodyLayoutContainer.addView(textView);
                memberListView = MyStaticComponents.getPaginatedListViewWithItem(context, memberList, R.layout.listview_row_with_two_item, null, null);
                bodyLayoutContainer.addView(memberListView);
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBER_IN_YOUR_AREA));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Integer locationId = Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID));
                selectedAshaAreas.clear();
                selectedAshaAreas.add(locationId);
                showProcessDialog();
                bodyLayoutContainer.removeAllViews();
                setScreeningSelectionScreen();
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
                case MEMBER_LIST_SCREEN:
                    footerView.setVisibility(View.GONE);
                    showProcessDialog();
                    nextButton.setText(GlobalTypes.EVENT_NEXT);
                    bodyLayoutContainer.removeAllViews();
                    setScreeningSelectionScreen();
                    break;

                case SCREENING_SELECTION_SCREEN:
                    startLocationSelectionActivity();
                    break;

                case VILLAGE_SELECTION_SCREEN:
                    navigateToHomeScreen(false);
                    break;

                default:
                    break;
            }
            return true;
        }
        return false;
    }

}
