package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import static com.argusoft.sewa.android.app.util.UtilBean.getMemberFullName;

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
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.component.SearchComponent;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.NcdServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberAdditionalInfoDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.FormMetaDataUtil;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@EActivity
public class DnhddNCDScreeningFHWActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    private static final String FAMILY_SELECTION_SCREEN = "familySelectionScreen";
    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";
    private static final String CBAC_QUESTION_SCREEN = "cbacQuestionScreen";
    private static final String SERVICE_SELECTION_SCREEN = "serviceSelectionScreen";
    private static final String VILLAGE_SELECTION_SCREEN = "villageSelectionScreen";
    private static final String PERSONAL_HISTORY_SCREEN = "personalHistoryScreen";
    private static final Integer ACTIVITY_CODE_FOR_CBAC_DETAILS_ACTIVITY = 300;
    private static final Integer ACTIVITY_CODE_FOR_CBAC_FORM_ACTIVITY = 400;
    private static final Integer ACTIVITY_CODE_FOR_DNHDD_NCD_SCREENING_ACTIVITY = 206;

    private static final long DELAY = 500;
    private static final long LIMIT = 30;
    @Bean
    public SewaServiceImpl sewaService;
    @Bean
    public SewaFhsServiceImpl fhsService;
    @Bean
    public NcdServiceImpl ncdService;
    @Bean
    public FormMetaDataUtil formMetaDataUtil;
    private LinearLayout bodyLayoutContainer;
    private LinearLayout footerLayout;
    private Button nextButton;
    private String screen;
    private Spinner ashaAreaSpinner;
    private List<LocationBean> ashaAreaList;
    private FamilyDataBean selectedFamily;
    private MemberDataBean selectedMember;
    private List<String> selectedMemberCompletedScreenings;
    private List<FamilyDataBean> familyDataBeans;
    private List<MemberDataBean> memberDataBeans;
    private List<LocationBean> villageList;
    private List<Integer> selectedAshaAreas = new ArrayList<>();
    private Timer timer = new Timer();
    private MyAlertDialog myAlertDialog;
    private long offset;
    private int selectedFamilyIndex = -1;
    private int selectedMemberIndex = -1;
    private int selectedServiceIndex = -1;
    private PagingListView paginatedListView;
    private List<String> familyIds;
    private List<String> screeningForms;
    private String searchString;
    private LinearLayout globalPanel;
    private MaterialTextView textView;
    private MaterialTextView noFamilyTextView;
    private boolean isHealthScreeningDone;
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
        setTitle(UtilBean.getTitleText(LabelConstants.NCD_SCREENING_ACTIVITY_TITLE));
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        setContentView(globalPanel);
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
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.NCD_SCREENING_ACTIVITY_TITLE);
        startActivityForResult(myIntent, ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != DynamicUtils.ID_NEXT_BUTTON) {
            return;
        }
        setSubTitle(null);
        switch (screen) {
            case VILLAGE_SELECTION_SCREEN:
                showProcessDialog();
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
                bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                addSearchTextBox();
                retrieveFamilyListFromDB(null);
                break;

            case FAMILY_SELECTION_SCREEN:
                if (familyDataBeans == null || familyDataBeans.isEmpty()) {
                    navigateToHomeScreen(false);
                    return;
                }
                if (selectedFamilyIndex == -1) {
                    SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_FAMILY));
                    return;
                }
                selectedFamily = familyDataBeans.get(selectedFamilyIndex);
                retrieveMemberListFromDB(selectedFamily.getFamilyId());
                break;

            case MEMBER_SELECTION_SCREEN:
                Calendar oneYearBack = Calendar.getInstance();
                oneYearBack.add(Calendar.YEAR, -1);

                if (selectedMemberIndex != -1) {
                    selectedMember = memberDataBeans.get(selectedMemberIndex);
                    if (isMemberAllScreeningDone(selectedMember)) {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.ALL_SCREENING_IS_DONE_FOR_SELECTED_MEMBER));
                        return;
                    }

                    setServiceSelectionScreen();
                } else {
                    SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.MEMBER_SELECTION_REQUIRED_FROM_FAMILY_ALERT));
                }
                break;

//            case CBAC_QUESTION_SCREEN:
//                if (radioGroup.getCheckedRadioButtonId() == -1) {
//                    SewaUtil.generateToast(this, LabelConstants.PLEASE_SELECT_AN_OPTION);
//                } else if (radioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
//                    if (selectedMemberCbacDone) {
//                        if (sewaService.isOnline()) {
//                            showProcessDialog();
//                            Intent myIntent = new Intent(this, CbacDetailsActivity_.class);
//                            myIntent.putExtra("memberId", selectedMember.getId());
//                            startActivityForResult(myIntent, ACTIVITY_CODE_FOR_CBAC_DETAILS_ACTIVITY);
//                            showProcessDialog();
//                        } else {
//                            showNotOnlineMessage();
//                        }
//                    } else {
//                        String formType = FormConstants.NCD_ASHA_CBAC;
//                        showProcessDialog();
//                        Intent myIntent = new Intent(this, DynamicFormActivity_.class);
//                        myIntent.putExtra(SewaConstants.ENTITY, formType);
//                        setMetadataForFormByFormType(selectedMember);
//                        startActivityForResult(myIntent, ACTIVITY_CODE_FOR_CBAC_FORM_ACTIVITY);
//                        showProcessDialog();
//                    }
//                } else {
//                    setServiceSelectionScreen();
//                }
//                break;

            case SERVICE_SELECTION_SCREEN:
                if (selectedServiceIndex != -1) {
                    String formType = screeningForms.get(selectedServiceIndex);
                    if (formType != null) {
                        if (selectedMemberCompletedScreenings.contains(formType)) {
                            SewaUtil.generateToast(this, UtilBean.getMyLabel(
                                    LabelConstants.FORM_FOR_SELECTED_SERVICE_IS_ALREADY_FILLED_SO_SELECT_ANOTHER_SERVICE));
                        } else {
//                            showProcessDialog();
                            Intent myIntent = new Intent(this, DynamicFormActivity_.class);
                            myIntent.putExtra(SewaConstants.ENTITY, formType);
                            setMetadataForFormByFormType(selectedMember);
                            startActivityForResult(myIntent, ACTIVITY_CODE_FOR_DNHDD_NCD_SCREENING_ACTIVITY);
                            selectedServiceIndex = -1;
                        }
                    }
                } else {
                    SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_TYPE_OF_SERVICE));
                }
                break;
            default:
        }
    }

    private void showNotOnlineMessage() {
        if (!sewaService.isOnline()) {
            runOnUiThread(new Runnable() {
                public void run() {
                    View.OnClickListener myListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    };
                    alertDialog = new MyAlertDialog(context, false,
                            UtilBean.getMyLabel(LabelConstants.NETWORK_CONNECTIVITY_ALERT),
                            myListener, DynamicUtils.BUTTON_OK);
                    alertDialog.show();
                }
            });
        }
    }


    private void addSearchTextBox() {
        SearchComponent searchBox = new SearchComponent(context, null, LabelConstants.SEARCH_FAMILY_BY_NAME_AND_ADDRESS, null, null, null, null);
        searchBox.addTextWatcherInEditText(getSearchClickOffline());
        bodyLayoutContainer.addView(searchBox);
        bodyLayoutContainer.addView(MyStaticComponents.getOrTextView(context));
    }

    private TextWatcher getSearchClickOffline() {
        return new TextWatcher() {
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
                                        retrieveFamilyListFromDB(s.toString());
//                                        editText.setFocusable(true);
                                    });
                                } else if (s == null || s.length() == 0) {
                                    runOnUiThread(() -> {
                                        showProcessDialog();
                                        retrieveFamilyListFromDB(null);
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
        };
    }

    @Background
    public void retrieveFamilyListFromDB(String familyId) {
        searchString = familyId;
        offset = 0;
        selectedFamilyIndex = -1;
        familyDataBeans = ncdService.retrieveFamiliesForDnhddNcdScreening(familyId, selectedAshaAreas, LIMIT, offset, qrScanFilter);
        offset = offset + LIMIT;
        if (!familyDataBeans.isEmpty()) {
            setFamilySelectionScreen(familyId != null, getFamilyList(familyDataBeans));
        } else {

            setFamilySelectionScreen(familyId != null, Collections.emptyList());
        }
    }

    @UiThread
    public void setFamilySelectionScreen(boolean isSearch, List<ListItemDataBean> listItems) {
        screen = FAMILY_SELECTION_SCREEN;
        bodyLayoutContainer.removeView(textView);
        bodyLayoutContainer.removeView(paginatedListView);
        bodyLayoutContainer.removeView(noFamilyTextView);

        familyIds = new ArrayList<>();
        if (!familyDataBeans.isEmpty()) {
            textView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_FAMILY);
            bodyLayoutContainer.addView(textView);

            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedFamilyIndex = position;

            if (isSearch) {
                paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, listItems, R.layout.listview_row_family_list, onItemClickListener, null);
            } else {
                paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, listItems, R.layout.listview_row_family_list, onItemClickListener, this);
            }
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            noFamilyTextView = MyStaticComponents.generateInstructionView(this, UtilBean.getMyLabel(LabelConstants.NO_FAMILIES_FOUND));
            bodyLayoutContainer.addView(noFamilyTextView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }

        if (!isSearch) {
            showProcessDialog();
        }
        hideProcessDialog();
    }

    private List<ListItemDataBean> getFamilyList(List<FamilyDataBean> familyList) {
        if (familyList == null || familyList.isEmpty()) {
            return Collections.emptyList();
        }
        List<ListItemDataBean> list = new ArrayList<>();
        ListItemDataBean item;
        String headName = null;
        for (FamilyDataBean familyDataBean : familyList) {

            familyDataBean.setMembers(fhsService.retrieveMemberDataBeansForDnhddNcdByFamily(familyDataBean.getFamilyId()));
            int hypDiaMenScreenCount = 0;
            int cancerScreeningCount = 0;
            int referredMemberCount = 0;
            int totalMemberCount = 0;
            Calendar oneYearBack = Calendar.getInstance();
            oneYearBack.add(Calendar.YEAR, -1);
            for (MemberDataBean member : familyDataBean.getMembers()) {
                if (Boolean.TRUE.equals(member.getFamilyHeadFlag())) {
                    headName = UtilBean.getMemberFullName(member);
                    familyDataBean.setHeadMemberName(headName);
                }
                totalMemberCount++;
                if (member.getHypDiaMentalServiceDate() != null && new Date(member.getHypDiaMentalServiceDate()).after(oneYearBack.getTime())) {
                    hypDiaMenScreenCount++;
                }
                if (member.getCancerServiceDate() != null && new Date(member.getCancerServiceDate()).after(oneYearBack.getTime())) {
                    cancerScreeningCount++;
                }
                if (isMemberSuspectedInAny(member)) {
                    referredMemberCount++;
                }
            }
            if (headName == null) {
                headName = UtilBean.getMyLabel(LabelConstants.HEAD_NAME_NOT_AVAILABLE);
            }

            String color = "";
            if (hypDiaMenScreenCount == totalMemberCount && cancerScreeningCount == totalMemberCount) {
                color = "GREEN";
            } else if (referredMemberCount > 0) {
                color = "BLUE";
            } else if (hypDiaMenScreenCount > 0 || cancerScreeningCount > 0) {
                color = "ORANGE";
            } else if (hypDiaMenScreenCount == 0 && cancerScreeningCount == 0) {
                color = "RED";
            }

            item = new ListItemDataBean();
            item.setColor(color);
            item.setFamilyId(familyDataBean.getFamilyId());
            item.setMemberName(headName);
            list.add(item);

        }
        return list;
    }

    @Background
    public void retrieveMemberListFromDB(String familyId) {
        if (familyId != null) {
            selectedMemberIndex = -1;
            memberDataBeans = ncdService.retrieveMembersListForDnhddNcdScreening(familyId);
            setMemberSelectionScreen();
        }
    }

    @UiThread
    public void setMemberSelectionScreen() {
        screen = MEMBER_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        footerLayout.setVisibility(View.VISIBLE);
        List<ListItemDataBean> list = new ArrayList<>();
        StringBuilder text;

        if (!memberDataBeans.isEmpty()) {
            bodyLayoutContainer.addView(MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_A_MEMBER_FROM_LIST));

            Calendar oneYearBack = Calendar.getInstance();
            oneYearBack.add(Calendar.YEAR, -1);

            for (MemberDataBean memberDataBean : memberDataBeans) {
                if (isMemberAllScreeningDone(memberDataBean)) {
                    text = new StringBuilder(" ✔️️");
                    text.append(memberDataBean.getUniqueHealthId());
                } else {
                    text = new StringBuilder(memberDataBean.getUniqueHealthId());
                }

                list.add(new ListItemDataBean(text.toString(), getMemberFullName(memberDataBean), " (" + UtilBean.getAgeDisplayOnGivenDate(new Date(memberDataBean.getDob()), new Date()) + ")"));
            }
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedMemberIndex = position;
            PagingListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_info, onItemClickListener, null);
            bodyLayoutContainer.addView(listView);
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBERS_HAVING_AGE_MORE_THAN_30_YEARS_IN_FAMILY));
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }
    }

    private boolean isMemberAllScreeningDone(MemberDataBean memberDataBean) {
        Calendar oneYearBack = Calendar.getInstance();
        oneYearBack.add(Calendar.YEAR, -1);

        if (memberDataBean.getCancerServiceDate() != null && memberDataBean.getHypDiaMentalServiceDate() != null) {
            return (new Date(memberDataBean.getHypDiaMentalServiceDate()).after(oneYearBack.getTime())
                    && new Date(memberDataBean.getCancerServiceDate()).after(oneYearBack.getTime()));
        }
        return false;
    }

    private boolean isMemberSuspectedInAny(MemberDataBean memberDataBean) {

        if (memberDataBean.getGender() != null && memberDataBean.getAdditionalInfo() != null && !memberDataBean.getAdditionalInfo().isEmpty()) {
            MemberAdditionalInfoDataBean additionalInfoDataBean = new Gson().fromJson(memberDataBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);

            if (memberDataBean.getGender().equals(GlobalTypes.MALE) || memberDataBean.getGender().equals(GlobalTypes.TRANSGENDER)) {
                return additionalInfoDataBean.getSuspectedForHypertension() != null && additionalInfoDataBean.getSuspectedForHypertension() ||
                        additionalInfoDataBean.getSuspectedForDiabetes() != null && additionalInfoDataBean.getSuspectedForDiabetes() ||
                        additionalInfoDataBean.getSuspectedForMentalHealth() != null && additionalInfoDataBean.getSuspectedForMentalHealth() ||
                        additionalInfoDataBean.getSuspectedForOralCancer() != null && additionalInfoDataBean.getSuspectedForOralCancer();
            } else if (memberDataBean.getGender().equals(GlobalTypes.FEMALE)) {
                return additionalInfoDataBean.getSuspectedForHypertension() != null && additionalInfoDataBean.getSuspectedForHypertension() ||
                        additionalInfoDataBean.getSuspectedForDiabetes() != null && additionalInfoDataBean.getSuspectedForDiabetes() ||
                        additionalInfoDataBean.getSuspectedForMentalHealth() != null && additionalInfoDataBean.getSuspectedForMentalHealth() ||
                        additionalInfoDataBean.getSuspectedForOralCancer() != null && additionalInfoDataBean.getSuspectedForOralCancer() ||
                        additionalInfoDataBean.getSuspectedForBreastCancer() != null && additionalInfoDataBean.getSuspectedForBreastCancer();
            }
        }
        return false;
    }


    private boolean getMemberScreeningStatus(MemberDataBean memberDataBean,
                                             boolean checkAllScreeningDone, boolean neverScreenedBefore) {
        String financialYear = UtilBean.getFinancialYearFromDate(new Date());

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.YEAR, -40);
        Date dobDate = null;

        if (memberDataBean.getDob() != null) {
            dobDate = new Date(Long.parseLong(memberDataBean.getDob().toString()));
        }

        if (checkAllScreeningDone) {
            if (dobDate != null && dobDate.before(instance.getTime())) {
                return memberDataBean.getHypYear() != null && memberDataBean.getHypYear().contains(financialYear)
                        && memberDataBean.getDiabetesYear() != null && memberDataBean.getDiabetesYear().contains(financialYear)
                        && memberDataBean.getMentalHealthYear() != null && memberDataBean.getMentalHealthYear().contains(financialYear);
            } else {
                return memberDataBean.getMentalHealthYear() != null && memberDataBean.getMentalHealthYear().contains(financialYear);
            }
        } else {
            if (dobDate != null && dobDate.before(instance.getTime()) && !neverScreenedBefore) {
                return (memberDataBean.getHypYear() != null && memberDataBean.getHypYear().contains(financialYear))
                        || (memberDataBean.getDiabetesYear() != null && memberDataBean.getDiabetesYear().contains(financialYear))
                        || (memberDataBean.getMentalHealthYear() != null && memberDataBean.getMentalHealthYear().contains(financialYear));
            } else if (dobDate != null && dobDate.before(instance.getTime())) {
                return (memberDataBean.getHypYear() == null && memberDataBean.getDiabetesYear() == null);
            } else {
                return memberDataBean.getMentalHealthYear() != null && memberDataBean.getMentalHealthYear().contains(financialYear);
            }
        }
    }


    private void setServiceSelectionScreen() {
        bodyLayoutContainer.removeAllViews();
        screen = SERVICE_SELECTION_SCREEN;
        setSubTitle(UtilBean.getMemberFullName(selectedMember));

        String rbText;
        MaterialTextView serviceType = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_SERVICE_TYPE);
        bodyLayoutContainer.addView(serviceType);

        selectedServiceIndex = -1;
        screeningForms = new ArrayList<>();
        selectedMemberCompletedScreenings = new ArrayList<>();

        List<ListItemDataBean> list = new ArrayList<>();

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.YEAR, -30);

        Calendar oneYearBack = Calendar.getInstance();
        oneYearBack.add(Calendar.YEAR, -1);

        isHealthScreeningDone = false;

        if (selectedMember.getDob() != null) {
            Date dobDate = new Date(Long.parseLong(selectedMember.getDob().toString()));


            if (dobDate.before(instance.getTime())) {
                //Hypertension,Diabetes and Mental health screening
                rbText = UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(FormConstants.DNHDD_NCD_HYPERTENSION_DIABETES_AND_MENTAL_HEALTH));
                screeningForms.add(FormConstants.DNHDD_NCD_HYPERTENSION_DIABETES_AND_MENTAL_HEALTH);
                if (selectedMember.getHypDiaMentalServiceDate() != null &&
                        new Date(selectedMember.getHypDiaMentalServiceDate()).after(oneYearBack.getTime())) {
                    rbText = rbText + " ✔️️";
                    selectedMemberCompletedScreenings.add(FormConstants.DNHDD_NCD_HYPERTENSION_DIABETES_AND_MENTAL_HEALTH);
                }
                list.add(new ListItemDataBean(rbText));
                //cancer screening
                rbText = UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(FormConstants.CANCER_SCREENING));
                screeningForms.add(FormConstants.CANCER_SCREENING);
                if (selectedMember.getCancerServiceDate() != null &&
                        new Date(selectedMember.getCancerServiceDate()).after(oneYearBack.getTime())) {
                    rbText = rbText + " ✔️️";
                    selectedMemberCompletedScreenings.add(FormConstants.CANCER_SCREENING);
                }

                list.add(new ListItemDataBean(rbText));
            }
        }

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            selectedServiceIndex = position;
            onClick(nextButton);
        };
        ListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_type, onItemClickListener, null);
        bodyLayoutContainer.addView(listView);
        footerLayout.setVisibility(View.GONE);
    }

    private void setMetadataForFormByFormType(MemberDataBean memberDataBean) {
        FamilyDataBean familyDataBean = null;
        if (memberDataBean != null) {
            familyDataBean = fhsService.retrieveFamilyDataBeanByFamilyId(memberDataBean.getFamilyId());
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        if (memberDataBean != null) {
            formMetaDataUtil.setMetaDataForNCDForms(memberDataBean, familyDataBean, sharedPref);
        }
    }

    @UiThread
    public void addVillageSelectionSpinner() {
        setSubTitle(null);
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.SELECT_VILLAGE));
        String[] arrayOfOptions = new String[villageList.size()];
        int i = 0;
        for (LocationBean locationBean : villageList) {
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }

        Spinner villageSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 2);
        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bodyLayoutContainer.removeView(ashaAreaSpinner);
                ashaAreaList = fhsService.retrieveAshaAreaAssignedToUser(villageList.get(position).getActualID());
                addAshaAreaSelectionSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // not doing anything here
            }
        });
        bodyLayoutContainer.addView(villageSpinner);
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.SELECT_ASHA_AREA)));
        addAshaAreaSelectionSpinner();
    }

    @UiThread
    public void addAshaAreaSelectionSpinner() {
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
                LabelConstants.ON_NCD_SCREENING_CLOSE_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        myAlertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult resultForQRScanner = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Integer locationId = Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID));
                selectedAshaAreas.clear();
                selectedAshaAreas.add(locationId);
                showProcessDialog();
                bodyLayoutContainer.removeAllViews();
                bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                addSearchTextBox();
                qrScanFilter.put(FieldNameConstants.IS_QR_SCAN, "false");
                retrieveFamilyListFromDB(null);
            } else {
                navigateToHomeScreen(false);
            }
            return;
        } else if (requestCode == ACTIVITY_CODE_FOR_CBAC_FORM_ACTIVITY) {
            showProcessDialog();
            retrieveMemberListFromDB(selectedFamily.getFamilyId());
            hideProcessDialog();
            return;
        } else if (resultForQRScanner != null) {
            if (resultForQRScanner.getContents() == null) {
                SewaUtil.generateToast(this, LabelConstants.FAILED_TO_SCAN_QR);
            } else {
                //show dialogue with resultForQRScanner
                qrScanFilter = SewaUtil.setQrScanFilterData(resultForQRScanner.getContents());
                retrieveFamilyListFromDB(null);
            }
        } else if (requestCode == ACTIVITY_CODE_FOR_DNHDD_NCD_SCREENING_ACTIVITY) {
            retrieveMemberListFromDB(selectedFamily.getFamilyId());
        }

        if (selectedMember != null) {
            selectedMember = new MemberDataBean(fhsService.retrieveMemberBeanByActualId(Long.valueOf(selectedMember.getId())));
            if (isMemberAllScreeningDone(selectedMember)) {
                showProcessDialog();
                retrieveMemberListFromDB(selectedFamily.getFamilyId());
                selectedMemberIndex = -1;
                footerLayout.setVisibility(View.VISIBLE);
            } else {
                showProcessDialog();
                setServiceSelectionScreen();
            }
        }
        hideProcessDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        selectedServiceIndex = -1;
        if (screen == null || screen.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            setSubTitle(null);
            switch (screen) {
                case SERVICE_SELECTION_SCREEN:
                    footerLayout.setVisibility(View.VISIBLE);
                    selectedMemberIndex = -1;
                    retrieveMemberListFromDB(selectedFamily.getFamilyId());
                    nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
//                    setCbacQuestionScreen();
                    break;
//                case CBAC_QUESTION_SCREEN:
//                    selectedMemberIndex = -1;
//                    retrieveMemberListFromDB(selectedFamily.getFamilyId());
//                    nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
//                    break;
                case MEMBER_SELECTION_SCREEN:
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    selectedFamilyIndex = -1;
                    bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                    addSearchTextBox();
                    retrieveFamilyListFromDB(null);
                    nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                    break;

                case FAMILY_SELECTION_SCREEN:
                    startLocationSelectionActivity();
                    break;

                case VILLAGE_SELECTION_SCREEN:
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
        List<FamilyDataBean> familyDataBeansList = ncdService.retrieveFamiliesForDnhddNcdScreening(searchString, selectedAshaAreas, LIMIT, offset, qrScanFilter);
        offset = offset + LIMIT;
        onLoadMoreUi(familyDataBeansList);
    }

    @UiThread
    public void onLoadMoreUi(List<FamilyDataBean> familyDataBeansList) {
        if (!familyDataBeansList.isEmpty()) {
            List<ListItemDataBean> familyList = getFamilyList(familyDataBeansList);
            familyDataBeans.addAll(familyDataBeansList);
            paginatedListView.onFinishLoadingWithItem(true, familyList);
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
        }
    }
}