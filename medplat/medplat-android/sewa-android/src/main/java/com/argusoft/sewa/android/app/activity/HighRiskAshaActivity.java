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
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by prateek on 8/23/19
 */
@EActivity
public class HighRiskAshaActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    private static final Integer REQUEST_CODE_FOR_HIGH_RISK_PREGNANCY_ACTIVITY = 200;
    private static final String ASHA_AREA_SELECTION_SCREEN = "ashaAreaSelectionScreen";
    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";
    private static final String VISIT_SELECTION_SCREEN = "visitSelectionScreen";
    private static final String MEMBER_DETAILS_SCREEN = "memberDetailsScreen";
    private static final String TYPE_SELECTION_SCREEN = "typeSelectionScree";
    @Bean
    public SewaServiceImpl sewaService;
    @Bean
    public SewaFhsServiceImpl fhsService;
    @Bean
    public FormMetaDataUtil formMetaDataUtil;
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private String screen;
    private Spinner spinner;
    private List<LocationBean> ashaAreaList = new ArrayList<>();
    private Integer selectedAshaArea;
    private MemberDataBean memberSelected = null;
    private List<MemberDataBean> highRiskPregnantWomen;
    private List<MemberDataBean> highRiskChildren;
    private long limit = 30;
    private long offset;
    private List<Integer> selectedAreas;
    private int selectedTypeIndex = -1;
    private int selectedWomanIndex = -1;
    private int selectedChildIndex = -1;
    private int selectedVisitIndex = -1;
    private PagingListView pagingListView;
    private List<String> visitForms;
    private LinearLayout globalPanel;
    private LinearLayout footerView;
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
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        setTitle(UtilBean.getTitleText(LabelConstants.HIGH_RISK_BENEFICIARIES_TITLE));
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
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.HIGH_RISK_BENEFICIARIES_TITLE);
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
                LabelConstants.WANT_TO_CLOSE_HIGH_RISK_BENEFICIARIES,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        setSubTitle(null);
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screen) {
                case ASHA_AREA_SELECTION_SCREEN:
                    String selectedAreaName = spinner.getSelectedItem().toString();
                    for (LocationBean locationBean : ashaAreaList) {
                        if (selectedAreaName.equals(locationBean.getName())) {
                            selectedAshaArea = locationBean.getActualID();
                            break;
                        }
                    }
                    setSelectionType();
                    break;
                case TYPE_SELECTION_SCREEN:
                    if (selectedTypeIndex != -1) {
                        footerView.setVisibility(View.VISIBLE);
                        retrieveHighRiskWomenAndChildList();
                    }
                    break;
                case MEMBER_SELECTION_SCREEN:
                    visits = new ArrayList<>();
                    if (selectedChildIndex != -1) {
                        bodyLayoutContainer.removeAllViews();
                        visits.add(FormConstants.ASHA_CS);
                        memberSelected = highRiskChildren.get(selectedChildIndex);
                        setVisitSelectionScreen(visits);
                    } else if (selectedWomanIndex != -1) {
                        bodyLayoutContainer.removeAllViews();
                        visits.add(FormConstants.ASHA_ANC);
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
                case MEMBER_DETAILS_SCREEN:
                    navigateToHomeScreen(false);
                    break;
                default:
                    break;
            }
        }
    }

    @UiThread
    public void setSelectionType() {
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.getListTitleView(context, LabelConstants.SELECT_TYPE));
        screen = TYPE_SELECTION_SCREEN;
        List<ListItemDataBean> list = new ArrayList<>();
        list.add(new ListItemDataBean(LabelConstants.HIGH_RISK_WOMEN));
        list.add(new ListItemDataBean(LabelConstants.HIGH_RISK_CHILDREN));

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            selectedTypeIndex = position;
            onClick(nextButton);
        };
        PagingListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_type, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
        footerView.setVisibility(View.GONE);
    }

    @UiThread
    public void addAshaAreaSelectionSpinner() {
        screen = ASHA_AREA_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        setSubTitle(null);
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.SELECT_ASHA_AREA));
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
        nextButton.setText(GlobalTypes.EVENT_NEXT);
        nextButton.setOnClickListener(this);
        hideProcessDialog();
    }

    @Background
    public void retrieveHighRiskWomenAndChildList() {
        selectedAreas = new LinkedList<>();
        selectedAreas.add(selectedAshaArea);
        offset = 0;
        selectedChildIndex = -1;
        selectedWomanIndex = -1;
        if (selectedTypeIndex == 0) {
            highRiskPregnantWomen = fhsService.retrievePregnantWomenByAshaArea(selectedAreas, true, null, null, limit, offset, qrScanFilter);
        } else {
            highRiskChildren = fhsService.retrieveChildsBelow5YearsByAshaArea(selectedAreas, true, null, null, limit, offset, qrScanFilter);
        }
        offset = offset + limit;
        setMemberSelectionScreen();
    }

    @UiThread
    public void setMemberSelectionScreen() {
        screen = MEMBER_SELECTION_SCREEN;
        setSubTitle(null);
        bodyLayoutContainer.removeAllViews();
        List<ListItemDataBean> highRiskPregnantWomenList;
        List<ListItemDataBean> highRiskChildrenList;
        switch (selectedTypeIndex) {
            case 0:
                highRiskPregnantWomenList = addHighRiskPregnantWomenList(highRiskPregnantWomen);
                if (highRiskPregnantWomenList.isEmpty()) {
                    nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
                    nextButton.setOnClickListener(v -> addAshaAreaSelectionSpinner());
                } else {
                    AdapterView.OnItemClickListener onWomanItemClickListener = (parent, view, position, id) -> selectedWomanIndex = position;
                    pagingListView = MyStaticComponents.getPaginatedListViewWithItem(context, highRiskPregnantWomenList, R.layout.listview_row_with_two_item, onWomanItemClickListener, this);
                    MaterialTextView pregnantWomantextView = MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.HIGH_RISK_PREGNANT_WOMEN);
                    bodyLayoutContainer.addView(pregnantWomantextView);
                    bodyLayoutContainer.addView(pagingListView);
                }
                break;
            case 1:
                highRiskChildrenList = addHighRiskChildrenList(highRiskChildren);
                if (highRiskChildrenList.isEmpty()) {
                    nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
                    nextButton.setOnClickListener(v -> addAshaAreaSelectionSpinner());
                } else {
                    AdapterView.OnItemClickListener onChildItemClickListener = (parent, view, position, id) -> selectedChildIndex = position;
                    pagingListView = MyStaticComponents.getPaginatedListViewWithItem(context, highRiskChildrenList, R.layout.listview_row_with_two_item, onChildItemClickListener, this);
                    MaterialTextView childrenTextView = MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.HIGH_RISK_CHILDREN);
                    bodyLayoutContainer.addView(childrenTextView);
                    bodyLayoutContainer.addView(pagingListView);
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
                list.add(new ListItemDataBean(memberDataBean.getFamilyId(), memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean), null, null));
            }
        } else {
            bodyLayoutContainer.removeAllViews();
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.NO_HIGH_RISK_PREGNANT_WOMEN_IN_AREA));
        }
        return list;
    }

    private List<ListItemDataBean> addHighRiskChildrenList(List<MemberDataBean> highRiskChildren) {
        List<ListItemDataBean> list = new ArrayList<>();
        if (highRiskChildren != null && !highRiskChildren.isEmpty()) {
            for (MemberDataBean memberDataBean : highRiskChildren) {
                list.add(new ListItemDataBean(memberDataBean.getFamilyId(), memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean), null, null));
            }
        } else {
            bodyLayoutContainer.removeAllViews();
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.NO_HIGH_RISK_CHILDREN_IN_AREA));
        }
        return list;
    }

    private void setVisitSelectionScreen(List<String> visits) {
        screen = VISIT_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        setSubTitle(UtilBean.getMemberFullName(memberSelected));
        List<ListItemDataBean> list = new ArrayList<>();
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

        if (visits == null || visits.isEmpty()) {
            showMemberDetails();
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_VISIT));
            for (String visit : visits) {
                list.add(new ListItemDataBean(UtilBean.getFullFormOfEntity().get(visit), null));
                visitForms.add(visit);
            }
        }
        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedVisitIndex = position;
        PagingListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_item, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
    }

    private void showMemberDetails() {
        screen = MEMBER_DETAILS_SCREEN;
        bodyLayoutContainer.removeAllViews();
        nextButton.setText(GlobalTypes.EVENT_OKAY);
        SimpleDateFormat sdf = new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault());

        bodyLayoutContainer.addView(MyStaticComponents.generateTitleView(this, UtilBean.getMyLabel(LabelConstants.MEMBER_DETAILS)));
        if (this.memberSelected != null) {
            FamilyDataBean familyDataBean = fhsService.retrieveFamilyDataBeanByFamilyId(memberSelected.getFamilyId());

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel(LabelConstants.MEMBER_NAME)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getMemberFullName(memberSelected)));

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel(LabelConstants.HEALTH_ID)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, memberSelected.getUniqueHealthId()));

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel(LabelConstants.FAMILY_ID)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, memberSelected.getFamilyId()));

            if (memberSelected.getMobileNumber() != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                        UtilBean.getMyLabel(LabelConstants.MOBILE_NUMBER)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, memberSelected.getMobileNumber()));
            }

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel(LabelConstants.DATE_OF_BIRTH)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, sdf.format(memberSelected.getDob())));

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel(LabelConstants.AGE)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    UtilBean.getAgeDisplayOnGivenDate(new Date(memberSelected.getDob()), new Date())));

            if (memberSelected.getYearOfWedding() != null) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(memberSelected.getDob());
                Integer yearOfBirth = c.get(Calendar.YEAR);
                Integer yearOfWedding = memberSelected.getYearOfWedding();
                int ageAtWedding = yearOfWedding - yearOfBirth;
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                        UtilBean.getMyLabel(LabelConstants.AGE_AT_WEDDING)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, Integer.toString(ageAtWedding)));
            }

            if (memberSelected.getLmpDate() != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                        UtilBean.getMyLabel(LabelConstants.LMP_DATE)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, sdf.format(memberSelected.getLmpDate())));
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

            String totalChildren = fhsService.getChildrenCount(Long.valueOf(memberSelected.getId()), true, false, false);
            if (totalChildren != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                        UtilBean.getMyLabel(LabelConstants.TOTAL_LIVING_CHILDREN)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, totalChildren));
            }

            String totalMaleChildren = fhsService.getChildrenCount(Long.valueOf(memberSelected.getId()), false, true, false);
            if (totalMaleChildren != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                        UtilBean.getMyLabel(LabelConstants.TOTAL_MALE_CHILDREN)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, totalMaleChildren));
            }


            String totalFemaleChildren = fhsService.getChildrenCount(Long.valueOf(memberSelected.getId()), false, false, true);
            if (totalFemaleChildren != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                        UtilBean.getMyLabel(LabelConstants.TOTAL_FEMALE_CHILDREN)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, totalFemaleChildren));
            }

            MemberDataBean latestChild = fhsService.getLatestChildByMotherId(Long.valueOf(memberSelected.getId()));
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

    private void startDynamicFormActivity(String formType, MemberDataBean memberDataBean) {
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
        hideProcessDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_HIGH_RISK_PREGNANCY_ACTIVITY) {
            showProcessDialog();
            selectedVisitIndex = -1;
            selectedWomanIndex = -1;
            selectedChildIndex = -1;
            setVisitSelectionScreen(visits);
            hideProcessDialog();
        } else if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                selectedAshaArea = Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID));
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
                case MEMBER_DETAILS_SCREEN:
                    showProcessDialog();
                    selectedChildIndex = -1;
                    selectedWomanIndex = -1;
                    retrieveHighRiskWomenAndChildList();
                    break;
                case TYPE_SELECTION_SCREEN:
                    startLocationSelectionActivity();
                    break;
                case MEMBER_SELECTION_SCREEN:
                    selectedTypeIndex = -1;
                    setSelectionType();
                    nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                    nextButton.setOnClickListener(this);
                    break;
                case ASHA_AREA_SELECTION_SCREEN:
                    navigateToHomeScreen(false);
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
        List<MemberDataBean> membersDataBeanList = null;
        switch (selectedTypeIndex) {
            case 0:
                membersDataBeanList = fhsService.retrievePregnantWomenByAshaArea(selectedAreas, true, null, null, limit, offset, qrScanFilter);
                break;
            case 1:
                membersDataBeanList = fhsService.retrieveChildsBelow5YearsByAshaArea(selectedAreas, true, null, null, limit, offset, qrScanFilter);
                break;
            default:
        }
        offset = offset + limit;
        onLoadMoreUi(membersDataBeanList);
    }

    @UiThread
    public void onLoadMoreUi(List<MemberDataBean> membersDataBeanList) {
        List<ListItemDataBean> list = new ArrayList<>();
        if (membersDataBeanList != null && !membersDataBeanList.isEmpty()) {
            for (MemberDataBean memberDataBean : membersDataBeanList) {
                list.add(new ListItemDataBean(memberDataBean.getFamilyId(), memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean), null, null));
            }
            highRiskPregnantWomen.addAll(membersDataBeanList);
            pagingListView.onFinishLoadingWithItem(true, list);
        } else {
            pagingListView.onFinishLoadingWithItem(false, null);
        }
    }

}

