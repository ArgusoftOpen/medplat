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
import android.widget.ListView;
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
import com.argusoft.sewa.android.app.core.impl.NcdServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.MemberMoConfirmedDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.FormMetaDataUtil;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

@EActivity
public class NcdWeeklyVisitFhwActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";
    private static final String MEMBER_DETAILS_SCREEN = "memberDetailsScreen";
    private static final String SERVICE_SELECTION_SCREEN = "serviceSelectionScreen";
    private static final String VILLAGE_SELECTION_SCREEN = "villageSelectionScreen";
    private static final Integer ACTIVITY_CODE_FOR_WEEKLY_FORM_ACTIVITY = 400;
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
    private MemberDataBean selectedMember;
    private List<LocationBean> villageList;
    private List<Integer> selectedAshaAreas = new ArrayList<>();
    private Timer timer = new Timer();
    private MyAlertDialog myAlertDialog;
    private long offset;
    private int selectedFamilyIndex = -1;
    private int selectedMemberIndex = -1;
    private int selectedServiceIndex = -1;
    private PagingListView paginatedListView;
    private String searchString;
    private LinearLayout globalPanel;
    private int selectedPeopleIndex;
    private List<MemberDataBean> memberList;
    private boolean referenceDue = false;
    private PagingListView listView;
    private MaterialTextView listTitleView;
    private MaterialTextView noMemberAvailableView;

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
        setTitle(UtilBean.getTitleText(LabelConstants.NCD_WEEKLY_CLINIC_ACTIVITY));
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
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.NCD_WEEKLY_CLINIC_ACTIVITY);
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
                setServiceSelectionScreen();
                break;

            case SERVICE_SELECTION_SCREEN:
                if (selectedServiceIndex != -1) {
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    addSearchTextBox();
                    retrieveMemberForWeeklyClinicVisit(null);
                } else {
                    SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_TYPE_OF_SERVICE));
                }
                break;

            case MEMBER_SELECTION_SCREEN:
                if (selectedMemberIndex != -1) {
                    selectedMember = memberList.get(selectedMemberIndex);
                    if (selectedServiceIndex == 0) {
                        String formType = FormConstants.NCD_FHW_WEEKLY_CLINIC;
                        showProcessDialog();
                        Intent myIntent = new Intent(this, DynamicFormActivity_.class);
                        myIntent.putExtra(SewaConstants.ENTITY, formType);
                        setMetadataForFormByFormType(selectedMember);
                        startActivityForResult(myIntent, ACTIVITY_CODE_FOR_WEEKLY_FORM_ACTIVITY);
                        showProcessDialog();
                    } else if (selectedServiceIndex == 1) {
                        String formType = FormConstants.NCD_FHW_WEEKLY_HOME;
                        showProcessDialog();
                        Intent myIntent = new Intent(this, DynamicFormActivity_.class);
                        myIntent.putExtra(SewaConstants.ENTITY, formType);
                        setMetadataForFormByFormType(selectedMember);
                        startActivityForResult(myIntent, ACTIVITY_CODE_FOR_WEEKLY_FORM_ACTIVITY);
                        showProcessDialog();
                    } else if (selectedServiceIndex == 2) {
                        MemberMoConfirmedDataBean moConfirmedDataBean = ncdService.retrieveMoConfirmedBeanByMemberId(Long.valueOf(selectedMember.getId()));
                        setMemberDetailsScreen(selectedMember, moConfirmedDataBean);
                    }
                } else if (memberList != null && !memberList.isEmpty()) {
                    SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.MEMBER_SELECTION_REQUIRED_TO_CONTINUE));
                } else {
                    navigateToHomeScreen(false);
                }
                break;

            case MEMBER_DETAILS_SCREEN:
                navigateToHomeScreen(false);
                break;

            default:
        }
    }

    private void setMemberDetailsScreen(MemberDataBean selectedMember, MemberMoConfirmedDataBean moConfirmedDataBean) {
        if (selectedMember != null) {
            screen = MEMBER_DETAILS_SCREEN;
            String diseaseStatus = "";
            SimpleDateFormat sdf = new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault());
            bodyLayoutContainer.removeAllViews();
            bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(this, LabelConstants.MEMBER_DETAILS));

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.UNIQUE_HEALTH_ID));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMember.getUniqueHealthId())));

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.FAMILY_ID));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, selectedMember.getFamilyId()));

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.NAME));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getMemberFullName(selectedMember)));

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.GENDER));
            if (selectedMember.getGender().equals(GlobalTypes.MALE)) {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, LabelConstants.MALE));
            } else {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, LabelConstants.FEMALE));
            }
            if (selectedMember.getDob() != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.DATE_OF_BIRTH));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, sdf.format(selectedMember.getDob())));

                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.AGE));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getAgeDisplayOnGivenDate(new Date(selectedMember.getDob()), new Date())));
            }
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.MOBILE_NUMBER)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, UtilBean.getNotAvailableIfNull(selectedMember.getMobileNumber())));

            if (moConfirmedDataBean != null) {
                if (moConfirmedDataBean.getConfirmedForDiabetes() != null && moConfirmedDataBean.getConfirmedForDiabetes()) {
                    diseaseStatus += "Diabetes - " + UtilBean.getTreatmentStatus(moConfirmedDataBean.getDiabetesTreatmentStatus());
                }
                if (moConfirmedDataBean.getConfirmedForHypertension() != null && moConfirmedDataBean.getConfirmedForHypertension()) {
                    String hypertensionStatus = "Hypertension - " + UtilBean.getTreatmentStatus(moConfirmedDataBean.getHypertensionTreatmentStatus());
                    diseaseStatus += diseaseStatus.isEmpty() ? hypertensionStatus : "\n" + hypertensionStatus;
                }
                if (moConfirmedDataBean.getConfirmedForMentalHealth() != null && moConfirmedDataBean.getConfirmedForMentalHealth()) {
                    String mentalHealthStatus = "Mental Health - " + UtilBean.getTreatmentStatus(moConfirmedDataBean.getMentalHealthTreatmentStatus());
                    diseaseStatus += diseaseStatus.isEmpty() ? mentalHealthStatus : "\n" + mentalHealthStatus;
                }
            }
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.CONFIRMED_DISEASE)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, diseaseStatus.isEmpty() ? LabelConstants.NOT_AVAILABLE : diseaseStatus));

            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.MAIN_MENU));
            footerLayout.setVisibility(View.VISIBLE);
            hideProcessDialog();
        }
    }

    @Background
    public void retrieveMemberForWeeklyClinicVisit(String search) {
        offset = 0;
        selectedPeopleIndex = -1;
        searchString = search;
        referenceDue = selectedServiceIndex == 2;
        memberList = ncdService.retrieveMembersListForWeeklyClinic(search, referenceDue, selectedAshaAreas, LIMIT, offset);
        offset = offset + LIMIT;
        setMemberScreenForWeeklyClinic();
    }

    @UiThread
    public void setMemberScreenForWeeklyClinic() {
        screen = MEMBER_SELECTION_SCREEN;
        bodyLayoutContainer.removeView(listView);
        bodyLayoutContainer.removeView(listTitleView);
        bodyLayoutContainer.removeView(noMemberAvailableView);
        List<ListItemDataBean> list = new ArrayList<>();

        if (memberList != null && !memberList.isEmpty()) {
            listTitleView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_A_MEMBER_FROM_LIST);
            bodyLayoutContainer.addView(listTitleView);

            for (MemberDataBean memberDataBean : memberList) {
                list.add(new ListItemDataBean(memberDataBean.getFamilyId(), memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean), null, null));
            }
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
                selectedMemberIndex = position;
            };
            listView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_two_item, onItemClickListener, null);
            bodyLayoutContainer.addView(listView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            noMemberAvailableView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBERS_FOUND);
            bodyLayoutContainer.addView(noMemberAvailableView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }
        footerLayout.setVisibility(View.VISIBLE);
        hideProcessDialog();
    }

    private void addSearchTextBox() {
        final TextInputLayout editText = MyStaticComponents.getEditText(this, LabelConstants.SEARCH_MEMBER, 1, 20, 1);

        if (editText.getEditText() != null) {
            editText.getEditText().addTextChangedListener(new TextWatcher() {
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
                                            retrieveMemberForWeeklyClinicVisit(s.toString());
                                            editText.setFocusable(true);
                                        });
                                    } else if (s == null || s.length() == 0) {
                                        runOnUiThread(() -> {
                                            showProcessDialog();
                                            retrieveMemberForWeeklyClinicVisit(null);
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
        bodyLayoutContainer.addView(editText);
        bodyLayoutContainer.addView(MyStaticComponents.getOrTextView(context));
    }


    private void setServiceSelectionScreen() {
        bodyLayoutContainer.removeAllViews();
        screen = SERVICE_SELECTION_SCREEN;

        MaterialTextView optionType = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_AN_OPTION);
        bodyLayoutContainer.addView(optionType);

        List<String> options = new ArrayList<>();
        options.add(UtilBean.getMyLabel(LabelConstants.CLINIC_VISIT));
        options.add(UtilBean.getMyLabel(LabelConstants.HOME_VISIT));
        options.add(UtilBean.getMyLabel(LabelConstants.REFERENCE_DUE));

        selectedServiceIndex = -1;
        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            selectedServiceIndex = position;
            onClick(nextButton);
        };
        ListView buttonList = MyStaticComponents.getButtonList(context, options, onItemClickListener);
        bodyLayoutContainer.addView(buttonList);
        hideProcessDialog();
        footerLayout.setVisibility(View.GONE);
    }

    private void setMetadataForFormByFormType(MemberDataBean memberDataBean) {
        FamilyDataBean familyDataBean = null;
        MemberMoConfirmedDataBean moConfirmedDataBean = null;
        if (memberDataBean != null) {
            familyDataBean = fhsService.retrieveFamilyDataBeanByFamilyId(memberDataBean.getFamilyId());
            moConfirmedDataBean = ncdService.retrieveMoConfirmedBeanByMemberId(Long.valueOf(memberDataBean.getId()));
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        List<String> basicMedicines = Arrays.asList("T Multivitamin", "T Paracetamol", "T Iron Folic acid", "T vitamin B12", "T Folic acid");
        if (memberDataBean != null) {
            formMetaDataUtil.setMetaDataForWeeklyVisitForms(memberDataBean, familyDataBean, moConfirmedDataBean, basicMedicines, "1", sharedPref);
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
                LabelConstants.ON_NCD_WEEKLY_VISIT_CLOSE_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        myAlertDialog.show();
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
                setServiceSelectionScreen();
            } else {
                navigateToHomeScreen(false);
            }
            return;
        }
        if (requestCode == ACTIVITY_CODE_FOR_WEEKLY_FORM_ACTIVITY) {
            if (resultCode == RESULT_CANCELED) {
                showProcessDialog();
                selectedMemberIndex = -1;
                bodyLayoutContainer.removeAllViews();
                addSearchTextBox();
                retrieveMemberForWeeklyClinicVisit(null);
                nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                hideProcessDialog();
                return;
            } else if (resultCode == RESULT_OK) {
                showProcessDialog();
                setServiceSelectionScreen();
                hideProcessDialog();
                return;
            }
        }
        hideProcessDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        selectedMemberIndex = -1;
        if (screen == null || screen.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            setSubTitle(null);
            switch (screen) {
                case MEMBER_DETAILS_SCREEN:
                    bodyLayoutContainer.removeAllViews();
                    addSearchTextBox();
                    retrieveMemberForWeeklyClinicVisit(null);
                    break;
                case SERVICE_SELECTION_SCREEN:
                    selectedServiceIndex = -1;
                    startLocationSelectionActivity();
                    break;
                case MEMBER_SELECTION_SCREEN:
                    showProcessDialog();
                    selectedServiceIndex = -1;
                    setServiceSelectionScreen();
                    nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                    footerLayout.setVisibility(View.VISIBLE);
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
        List<MemberDataBean> memberDataBeans = ncdService.retrieveMembersListForWeeklyClinic(null, referenceDue, selectedAshaAreas, LIMIT, offset);
        offset = offset + LIMIT;
        onLoadMoreUi(memberDataBeans);
    }

    @UiThread
    public void onLoadMoreUi(List<MemberDataBean> memberDataBeans) {
        List<ListItemDataBean> memberList = new ArrayList<>();
        if (!memberDataBeans.isEmpty()) {
            for (MemberDataBean memberDataBean : memberDataBeans) {
                memberList.add(new ListItemDataBean(memberDataBean.getFamilyId(), memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean), null, null));
            }
            this.memberList.addAll(memberDataBeans);
            paginatedListView.onFinishLoadingWithItem(true, memberList);
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
        }
    }
}
