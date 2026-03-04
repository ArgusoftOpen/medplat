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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.LocationMasterServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.IDSP2FamilyDataBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.FamilyBean;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.LoggerBean;
import com.argusoft.sewa.android.app.model.StoreAnswerBean;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.FormMetaDataUtil;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.argusoft.sewa.android.app.util.WSConstants;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@EActivity
public class IDSP2Activity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    LocationMasterServiceImpl locationService;
    @Bean
    FormMetaDataUtil formMetaDataUtil;

    private static final String FAMILY_SELECTION_SCREEN = "familySelectionScreen";
    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";
    private static final String VILLAGE_SELECTION_SCREEN = "villageSelectionScreen";
    private static final String OPTION_SELECTION_SCREEN = "optionSelectionScreen";
    private static final String NEXT_MEMBER_SELECTION_SCREEN = "newMemberSelectionScreen";
    private static final String FORM_SUBMISSION_SCREEN = "formSubmissionScreen";
    private static final String FINAL_SUBMISSION_SCREEN = "finalSubmissionScreen";
    private static final long DELAY = 500;

    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private TextInputLayout searchText;
    private Button nextButton;

    private RadioGroup nextMemberGroup;
    private Spinner villageSpinner;
    private Spinner ashaAreaSpinner;

    private Timer timer = new Timer();
    private Intent myIntent;
    private String screenName;
    private String familyHeadText;
    private RadioGroup anyIllnessRadioGroup;
    private RadioGroup anyOneTravelFromFamilyRadioGroup;
    private RadioGroup anyOneCovidContactRadioGroup;

    private String selectedVillage;
    private List<Integer> selectedAshaAreas = new ArrayList<>();
    private List<LocationBean> villageList = new ArrayList<>();
    private List<LocationBean> ashaAreaList = new ArrayList<>();
    private List<FamilyDataBean> familyDataBeans = null;
    private List<String> verifiedMemberList = null;
    private List<Integer> verifiedMemberCounters = null;
    private Map<String, MemberDataBean> headMembersMapWithFamilyIdAsKey;
    private FamilyDataBean selectedFamily;
    private MemberDataBean selectedMember;
    private boolean isAshaNotAssigned;
    private boolean isAshaLogin = false;

    private static final int RADIOBUTTON_ID_YES = 1;
    private static final int RADIOBUTTON_ID_NO = 2;

    private TextView selectAshaAreaTextView;
    private MaterialTextView textView;
    private int selectedFamilyIndex = -1;
    private PagingListView paginatedListView;
    private int selectedMemberIndex = -1;
    private long limit = 30;
    private long offset;
    private boolean canMoveBack = true;
    private MaterialTextView noFamilyAvailableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //To change body of generated methods, choose Tools | Templates.
        context = this;
        isAshaLogin = SewaTransformer.loginBean.getUserRole().equalsIgnoreCase(GlobalTypes.USER_ROLE_ASHA);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SharedStructureData.isLogin) {
            myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        if (globalPanel != null) {
            setContentView(globalPanel);
        }
        setTitle(UtilBean.getTitleText(LabelConstants.SURVEILLANCE_TITLE));
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        setContentView(globalPanel);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        startLocationSelectionActivity();
    }

    private void startLocationSelectionActivity() {
        myIntent = new Intent(context, LocationSelectionActivity_.class);
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.SURVEILLANCE_TITLE);
        startActivityForResult(myIntent, ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE);
    }

    @Background
    public void getVillageList() {
        screenName = VILLAGE_SELECTION_SCREEN;
        if (isAshaLogin) {
            ashaAreaList = fhsService.retrieveAshaAreaAssignedToUser(Integer.valueOf(selectedVillage));
            if (ashaAreaList == null || ashaAreaList.isEmpty()) {
                addDataNotSyncedScreen();
                return;
            }
            addAshaAreaSelectionSpinner();
            return;
        }
        villageList = fhsService.getDistinctLocationsAssignedToUser();

        if (villageList == null || villageList.isEmpty()) {
            addDataNotSyncedScreen();
            return;
        }

        selectedVillage = villageList.get(0).getActualID().toString();
        ashaAreaList = fhsService.retrieveAshaAreaAssignedToUser(Integer.valueOf(selectedVillage));
        addVillageSelectionSpinner();
    }

    @UiThread
    public void addVillageSelectionSpinner() {
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.SELECT_VILLAGE));
        nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        nextButton.setOnClickListener(this);
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
                ashaAreaList = fhsService.retrieveAshaAreaAssignedToUser(villageList.get(position).getActualID());
                addAshaAreaSelectionSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing to be done here
            }
        });
        bodyLayoutContainer.addView(villageSpinner);
        addAshaAreaSelectionSpinner();
    }

    @UiThread
    public void addAshaAreaSelectionSpinner() {
        if (selectAshaAreaTextView == null) {
            selectAshaAreaTextView = MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.SELECT_ASHA_AREA);
        } else {
            bodyLayoutContainer.removeView(selectAshaAreaTextView);
        }

        if (ashaAreaSpinner != null) {
            bodyLayoutContainer.removeView(ashaAreaSpinner);
        }

        String[] arrayOfOptions = new String[ashaAreaList.size() + 1];
        arrayOfOptions[0] = LabelConstants.ALL;
        int i = 1;
        for (LocationBean locationBean : ashaAreaList) {
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }
        ashaAreaSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 3);
        bodyLayoutContainer.addView(selectAshaAreaTextView);
        bodyLayoutContainer.addView(ashaAreaSpinner);
        hideProcessDialog();
    }

    @UiThread
    public void addDataNotSyncedScreen() {
        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context, LabelConstants.DATA_NOT_SYNCED_ALERT));
        View.OnClickListener listener = v -> navigateToHomeScreen(false);
        nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        nextButton.setOnClickListener(listener);
        hideProcessDialog();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screenName) {
                case VILLAGE_SELECTION_SCREEN:
                    selectedAshaAreas.clear();
                    if (!isAshaLogin) {
                        String village = villageSpinner.getSelectedItem().toString();
                        for (LocationBean locationBean : villageList) {
                            if (village.equals(locationBean.getName())) {
                                selectedVillage = String.valueOf(locationBean.getActualID());
                                break;
                            }
                        }
                    }
                    if (ashaAreaList != null && !ashaAreaList.isEmpty()) {
                        int position = ashaAreaSpinner.getSelectedItemPosition();
                        if (position == 0) {
                            for (LocationBean locationBean : ashaAreaList) {
                                selectedAshaAreas.add(locationBean.getActualID());
                            }
                        } else {
                            LocationBean area = ashaAreaList.get(position - 1);
                            selectedAshaAreas.add(area.getActualID());
                        }
                    }
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    addSearchTextBox();
                    addFamilyList();
                    break;
                case FAMILY_SELECTION_SCREEN:
                    if (selectedFamilyIndex != -1) {
                        bodyLayoutContainer.removeAllViews();
                        FamilyDataBean familyDataBean = familyDataBeans.get(selectedFamilyIndex);
                        selectedFamily = fhsService.retrieveFamilyDataBeanByFamilyId(familyDataBean.getFamilyId());
                        addOptionSelectionScreenForFamily();
                    } else {
                        SewaUtil.generateToast(this, LabelConstants.SELECT_A_FAMILY_FOR_SURVEILLANCE);
                    }
                    break;
                case OPTION_SELECTION_SCREEN:
                    Boolean validation = checkForBasicOptionsAnswer();
                    if (validation == null) {
                        SewaUtil.generateToast(this, LabelConstants.PLEASE_ANSWER_ALL_THE_QUESTIONS_TO_CONTINUE);
                    } else if (Boolean.TRUE.equals(validation)) {
                        showProcessDialog();
                        addSelectedFamilyDetails();
                    } else {
                        screenName = FORM_SUBMISSION_SCREEN;
                        setFormSubmissionScreen();
                    }
                    break;
                case MEMBER_SELECTION_SCREEN:
                    if (selectedMemberIndex != -1) {
                        if (verifiedMemberCounters.contains(selectedMemberIndex)) {
                            SewaUtil.generateToast(this, LabelConstants.ALREADY_BEEN_SCREENED_PLEASE_SELECT_ANOTHER_MEMBER);
                        } else {
                            selectedMember = selectedFamily.getMembers().get(selectedMemberIndex);
                            startDynamicFormActivity();
                            selectedMemberIndex = -1;
                        }
                    } else {
                        SewaUtil.generateToast(this, LabelConstants.PLEASE_SELECT_A_MEMBER_FOR_SCREENING);
                    }
                    break;
                case NEXT_MEMBER_SELECTION_SCREEN:
                    if (nextMemberGroup.getCheckedRadioButtonId() == -1) {
                        SewaUtil.generateToast(this, LabelConstants.PLEASE_SELECT_AN_OPTION);
                    } else if (nextMemberGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_YES) {
                        memberSelectionScreen();
                    } else {
                        screenName = FINAL_SUBMISSION_SCREEN;
                        setFormSubmissionScreen();
                    }
                    break;
                case FORM_SUBMISSION_SCREEN:
                    storeIDSPForm();
                    break;
                case FINAL_SUBMISSION_SCREEN:
                    screenName = FAMILY_SELECTION_SCREEN;
                    bodyLayoutContainer.removeAllViews();
                    addSearchTextBox();
                    addFamilyList();
                    break;
                default:
                    navigateToHomeScreen(false);
            }
        }
    }

    @UiThread
    public void addNewFamily() {
        showProcessDialog();

        myIntent = new Intent(this, DynamicFormActivity_.class);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().clear().apply();
        SharedStructureData.relatedPropertyHashTable.clear();

        SharedStructureData.currentFamilyDataBean = null;
        SharedStructureData.familyDataBeanToBeMerged = null;
        SharedStructureData.totalFamilyMembersCount = -1;

        SharedStructureData.relatedPropertyHashTable.put("familyId", LabelConstants.NOT_AVAILABLE);
        SharedStructureData.relatedPropertyHashTable.put("headOfFamily", LabelConstants.NOT_AVAILABLE);
        SharedStructureData.relatedPropertyHashTable.put("familyFound", "1");

        if (selectedVillage != null) {
            SharedStructureData.relatedPropertyHashTable.put("locationId", selectedVillage);
        }
        if (selectedAshaAreas != null && selectedAshaAreas.size() == 1) {
            SharedStructureData.relatedPropertyHashTable.put("areaId", selectedAshaAreas.get(0).toString());
        }

        String nextEntity = FormConstants.IDSP_NEW_FAMILY;
        myIntent.putExtra(SewaConstants.ENTITY, nextEntity);
        startActivityForResult(myIntent, ActivityConstants.IDSP_ACTIVITY_NEW_FAMILY_REQUEST_CODE);
        hideProcessDialog();
    }

    private void addSearchTextBox() {
        bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));

        List<String> options = new ArrayList<>();
        options.add(UtilBean.getMyLabel(LabelConstants.ADD_NEW_FAMILY));

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            if (position == 0) {
                addNewFamily();
            }
        };

        ListView buttonList = MyStaticComponents.getButtonList(context, options, onItemClickListener);
        buttonList.setPadding(0, 0, 0, 30);
        bodyLayoutContainer.addView(buttonList);

        searchText = MyStaticComponents.getEditText(this, LabelConstants.SEARCH_FAMILY, 1, 20, 1);
        bodyLayoutContainer.addView(searchText);
        bodyLayoutContainer.addView(MyStaticComponents.getOrTextView(context));

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
                                            retrieveFamilyListFromDB(s.toString());
                                            searchText.setFocusable(true);
                                        });
                                    } else if (s == null || s.length() == 0) {
                                        runOnUiThread(() -> {
                                            showProcessDialog();
                                            retrieveFamilyListFromDB(null);
                                            searchText.clearFocus();
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

    @UiThread
    public void addFamilyList() {
        screenName = FAMILY_SELECTION_SCREEN;
//        addAshaNotAssignedMessage();
//        if (!isAshaNotAssigned) {
//            showProcessDialog();
            retrieveFamilyListFromDB(null);
//        }
    }

    @Background
    public void retrieveFamilyListFromDB(String search) {
        offset = 0;
        selectedFamilyIndex = -1;
        if (search == null) {
            familyDataBeans = fhsService.retrieveFamilyDataBeansForIDSPByVillage(selectedVillage, selectedAshaAreas, limit, offset);
            offset = offset + limit;
        } else {
            familyDataBeans = fhsService.searchFamilyDataBeansForIDSPByVillage(search, selectedVillage, selectedAshaAreas);
        }
        setFamilySelectionScreen(search != null);
    }

    @UiThread
    public void setFamilySelectionScreen(boolean isSearch) {
        nextMemberGroup = null;
        bodyLayoutContainer.removeView(textView);
        bodyLayoutContainer.removeView(paginatedListView);
        bodyLayoutContainer.removeView(noFamilyAvailableView);
        if (familyDataBeans != null && !familyDataBeans.isEmpty()) {
            textView = MyStaticComponents.getListTitleView(context, LabelConstants.SELECT_FAMILY);
            bodyLayoutContainer.addView(textView);
            List<ListItemDataBean> list = new ArrayList<>(getFamilyList(familyDataBeans));
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedFamilyIndex = position;

            if (isSearch) {
                paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_two_item, onItemClickListener, null);
            } else {
                paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_two_item, onItemClickListener, this);
            }
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
            nextButton.setOnClickListener(this);
        } else {
            noFamilyAvailableView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_FAMILIES_FOUND);
            bodyLayoutContainer.addView(noFamilyAvailableView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
            nextButton.setOnClickListener(v -> navigateToHomeScreen(false));
        }
        hideProcessDialog();
    }

    private List<ListItemDataBean> getFamilyList(List<FamilyDataBean> familyDataBeans) {
        List<String> familyIds = new ArrayList<>();
        List<ListItemDataBean> list = new ArrayList<>();
        String rbText;
        for (FamilyDataBean familyDataBean : familyDataBeans) {
            familyIds.add(familyDataBean.getFamilyId());
        }
        headMembersMapWithFamilyIdAsKey = fhsService.retrieveHeadMemberDataBeansByFamilyId(familyIds);
        for (FamilyDataBean familyDataBean : familyDataBeans) {
            MemberDataBean headMember = headMembersMapWithFamilyIdAsKey.get(familyDataBean.getFamilyId());
            if (headMember != null) {
                familyDataBean.setHeadMemberName(headMember.getFirstName() + " " + headMember.getMiddleName() + " " + headMember.getGrandfatherName() + " " + headMember.getLastName());
                familyDataBean.setHeadMemberName(familyDataBean.getHeadMemberName().replace(" " + LabelConstants.NULL, ""));
                rbText = headMember.getFirstName() + " " + headMember.getMiddleName() + " " + headMember.getGrandfatherName() + " " + headMember.getLastName();
                rbText = rbText.replace(" " + LabelConstants.NULL, "");
            } else {
                rbText = UtilBean.getMyLabel(LabelConstants.HEAD_NAME_NOT_AVAILABLE);
            }

            if (familyDataBean.getLastIdsp2ScreeningDate() != null &&
                    new Date(familyDataBean.getLastIdsp2ScreeningDate())
                            .after(UtilBean.getDateAfterNoOfDays(new Date(), -15))) {
                list.add(new ListItemDataBean(null, familyDataBean.getFamilyId(), null, null, rbText, true));
            } else {
                list.add(new ListItemDataBean(null, familyDataBean.getFamilyId(), null, null, rbText, false));
            }
        }
        return list;
    }

    private void addAshaNotAssignedMessage() {
        if (isAshaLogin) {
            return;
        }

        List<LocationBean> areas = locationService.retrieveLocationBeansByLevelAndParent(7, Long.valueOf(selectedVillage));
        if (areas != null && !areas.isEmpty()) {
            for (LocationBean locationBean : areas) {
                String[] locationName = locationBean.getName().split("-");
                if (locationName.length <= 1) {
                    isAshaNotAssigned = true;
                    break;
                }
            }

            if (isAshaNotAssigned) {
                runOnUiThread(() -> {
                    View.OnClickListener myListener = v -> navigateToHomeScreen(false);
                    alertDialog = new MyAlertDialog(context, false,
                            GlobalTypes.MSG_ASHA_NOT_ASSIGNED,
                            myListener, DynamicUtils.BUTTON_OK);
                    alertDialog.show();
                });
            }
        }
    }

    @UiThread
    public void addOptionSelectionScreenForFamily() {
        verifiedMemberList = new ArrayList<>();
        anyIllnessRadioGroup = initializeRadioGroup();
        anyOneTravelFromFamilyRadioGroup = initializeRadioGroup();
        anyOneCovidContactRadioGroup = initializeRadioGroup();
        setOptionSelectionScreen();
    }

    private RadioGroup initializeRadioGroup() {
        HashMap<Integer, String> stringMap = new HashMap<>();
        stringMap.put(RADIOBUTTON_ID_YES, LabelConstants.YES);
        stringMap.put(RADIOBUTTON_ID_NO, LabelConstants.NO);
        return MyStaticComponents.getRadioGroup(this, stringMap, true);
    }

    private void setOptionSelectionScreen() {
        screenName = OPTION_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        MemberDataBean headMember = headMembersMapWithFamilyIdAsKey.get(selectedFamily.getFamilyId());
        if (headMember != null) {
            familyHeadText = headMember.getFirstName() + " " + headMember.getMiddleName() + " " + headMember.getGrandfatherName() + " " + headMember.getLastName();
            familyHeadText = familyHeadText.replace(" " + LabelConstants.NULL, "");
        } else {
            familyHeadText = UtilBean.getMyLabel(LabelConstants.HEAD_NAME_NOT_AVAILABLE);
        }

        LinearLayout detailsLayout = MyStaticComponents.getDetailsLayout(context, -1, LinearLayout.VERTICAL, bodyLayoutContainer.getPaddingTop());
        detailsLayout.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.FAMILY_ID + ":"));
        detailsLayout.addView(MyStaticComponents.generateBoldAnswerView(context, UtilBean.getMyLabel(selectedFamily.getFamilyId())));
        detailsLayout.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.FAMILY_HEAD + ":"));
        detailsLayout.addView(MyStaticComponents.generateAnswerView(context, familyHeadText));
        bodyLayoutContainer.addView(detailsLayout);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                LabelConstants.DOES_ANYONE_IN_THE_FAMILY_HAS_ANY_ILLNESS_OR_DISCOMFORT));
        bodyLayoutContainer.addView(anyIllnessRadioGroup);
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                LabelConstants.ANYONE_FROM_THE_FAMILY_TRAVEL_IN_THE_PAST_WEEK));
        bodyLayoutContainer.addView(anyOneTravelFromFamilyRadioGroup);
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                LabelConstants.CONTACT_WITH_ANY_SUSPECTED_OR_POSITIVE_CORONA_CASES));
        bodyLayoutContainer.addView(anyOneCovidContactRadioGroup);
    }

    private Boolean checkForBasicOptionsAnswer() {
        if (anyIllnessRadioGroup.getCheckedRadioButtonId() != -1
                && anyOneTravelFromFamilyRadioGroup.getCheckedRadioButtonId() != -1
                && anyOneCovidContactRadioGroup.getCheckedRadioButtonId() != -1) {
            return anyIllnessRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_YES
                    || anyOneTravelFromFamilyRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_YES
                    || anyOneCovidContactRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_YES;
        }

        return null;
    }

    @Background
    public void addSelectedFamilyDetails() {
        selectedFamily.setMembers(fhsService.retrieveActiveMemberByFamilyIdForIdsp(selectedFamily.getFamilyId()));
        memberSelectionScreen();
    }

    @UiThread
    public void memberSelectionScreen() {
        screenName = MEMBER_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        LinearLayout detailsLayout = MyStaticComponents.getDetailsLayout(context, -1, LinearLayout.VERTICAL, bodyLayoutContainer.getPaddingTop());
        detailsLayout.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.FAMILY_ID + ":"));
        detailsLayout.addView(MyStaticComponents.generateBoldAnswerView(context, UtilBean.getMyLabel(selectedFamily.getFamilyId())));
        detailsLayout.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.FAMILY_HEAD + ":"));
        detailsLayout.addView(MyStaticComponents.generateAnswerView(context, familyHeadText));
        bodyLayoutContainer.addView(detailsLayout);

        bodyLayoutContainer.addView(MyStaticComponents.getListTitleView(context, LabelConstants.SEARCH_MEMBER));

        String rbText;
        List<ListItemDataBean> list = new ArrayList<>();

        if (selectedFamily.getMembers() != null && !selectedFamily.getMembers().isEmpty()) {
            verifiedMemberCounters = new ArrayList<>();
            int count = 0;
            for (MemberDataBean memberDataBean : selectedFamily.getMembers()) {
                rbText = memberDataBean.getFirstName() + " " + memberDataBean.getMiddleName() + " " + memberDataBean.getGrandfatherName() + " " + memberDataBean.getLastName();
                rbText = rbText.replace(" " + LabelConstants.NULL, "");
                if (verifiedMemberList.contains(memberDataBean.getId())) {
                    verifiedMemberCounters.add(count);
                    list.add(new ListItemDataBean(null, memberDataBean.getUniqueHealthId(), null, null, rbText, true));
                } else {
                    list.add(new ListItemDataBean(null, memberDataBean.getUniqueHealthId(), null, null, rbText, false));
                }
                count++;
            }
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedMemberIndex = position;

            PagingListView paginatedListViewWithItem = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_two_item, onItemClickListener, null);
            bodyLayoutContainer.addView(paginatedListViewWithItem);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBERS_FOUND));
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }
        hideProcessDialog();
    }

    @UiThread
    public void setFormSubmissionScreen() {
        nextButton.setText(GlobalTypes.EVENT_OKAY);
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context, LabelConstants.YOUR_FORM_IS_COMPLETE));
        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context, LabelConstants.COMPLETED_SURVEILLANCE_FORM_FOR_THE_FAMILY));
    }

    @Background
    public void storeIDSPForm() {
        final IDSP2FamilyDataBean idspFamilyBean = new IDSP2FamilyDataBean();
        idspFamilyBean.setFamilyId(selectedFamily.getId());
        idspFamilyBean.setAnyIllness(anyIllnessRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_YES);
        idspFamilyBean.setAnyOneTravelFromFamily(anyOneTravelFromFamilyRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_YES);
        idspFamilyBean.setAnyOneCovidContact(anyOneCovidContactRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_YES);
        idspFamilyBean.setServiceDate(new Date().getTime());

        StringBuilder checkSum = new StringBuilder(SewaTransformer.loginBean.getUsername());
        checkSum.append(Calendar.getInstance().getTimeInMillis());

        StoreAnswerBean storeAnswerBean = new StoreAnswerBean();
        storeAnswerBean.setToken(SewaTransformer.loginBean.getUserToken());
        storeAnswerBean.setUserId(SewaTransformer.loginBean.getUserID());
        storeAnswerBean.setChecksum(checkSum.toString());
        storeAnswerBean.setDateOfMobile(Calendar.getInstance().getTimeInMillis());
        storeAnswerBean.setFormFilledUpTime(0L);
        storeAnswerBean.setMorbidityAnswer("-1");
        storeAnswerBean.setNotificationId(-1L);
        storeAnswerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO);
        storeAnswerBean.setRelatedInstance("-1");
        storeAnswerBean.setAnswerEntity(FormConstants.IDSP_FAMILY_2);

        LoggerBean loggerBean = new LoggerBean();
        loggerBean.setCheckSum(checkSum.toString());
        loggerBean.setBeneficiaryName(selectedFamily.getFamilyId() + " - " + familyHeadText);
        loggerBean.setDate(Calendar.getInstance().getTimeInMillis());
        loggerBean.setStatus(GlobalTypes.STATUS_PENDING);
        loggerBean.setRecordUrl(WSConstants.CONTEXT_URL_TECHO.trim());
        loggerBean.setNoOfAttempt(0);
        loggerBean.setTaskName(UtilBean.getMyLabel(LabelConstants.SURVEILLANCE_TITLE));
        loggerBean.setFormType(FormConstants.IDSP_FAMILY_2);

        storeAnswerBean.setAnswer(new Gson().toJson(idspFamilyBean));
        sewaService.createStoreAnswerBean(storeAnswerBean);
        sewaService.createLoggerBean(loggerBean);

        //DATE IS NOT BEING STORED HERE, NEED TO CHECK
        FamilyBean familyBean = fhsService.retrieveFamilyBeanByActualId(Long.valueOf(selectedFamily.getId()));
        familyBean.setLastIdsp2ScreeningDate(idspFamilyBean.getServiceDate());
        fhsService.updateFamily(familyBean);
        runOnUiThread(() -> {
            SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.SURVEILLANCE_FORM_SUBMITTED_SUCCESSFULLY));
            bodyLayoutContainer.removeAllViews();
            screenName = FAMILY_SELECTION_SCREEN;
            addSearchTextBox();
            addFamilyList();
        });

    }

    @UiThread
    public void setScreenTOAskForNextMemberVerfication() {
        screenName = NEXT_MEMBER_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.PROCEED_FOR_NEXT_MEMBER));
        bodyLayoutContainer.addView(nextMemberGroup);
    }

    private void startDynamicFormActivity() {
        showProcessDialog();
        Intent intent = new Intent(this, DynamicFormActivity_.class);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        formMetaDataUtil.setMetaDataForRchFormByFormType(FormConstants.IDSP_MEMBER_2, selectedMember.getId(), selectedMember.getFamilyId(), null, sharedPref);

        String sb = "-11" +
                GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR +
                (anyIllnessRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_YES ? "T" : "F") +
                GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR +
                "-12" +
                GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR +
                (anyOneTravelFromFamilyRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_YES ? "T" : "F") +
                GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR +
                "-13" +
                GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR +
                (anyOneCovidContactRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_YES ? "T" : "F") +
                GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR;
        SharedStructureData.relatedPropertyHashTable.put("familyQuestions", sb);

        SharedStructureData.relatedPropertyHashTable.put("anyMemberTravelled", anyOneTravelFromFamilyRadioGroup.getCheckedRadioButtonId() == RADIOBUTTON_ID_YES ? "T" : "F");

        intent.putExtra(SewaConstants.ENTITY, FormConstants.IDSP_MEMBER_2);
        startActivityForResult(intent, ActivityConstants.IDSP_ACTIVITY_REQUEST_CODE);
        hideProcessDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityConstants.IDSP_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                verifiedMemberList.add(selectedMember.getId());
                if (verifiedMemberList.size() == selectedFamily.getMembers().size()) {
                    runOnUiThread(() -> {
                        canMoveBack = false;
                        screenName = FINAL_SUBMISSION_SCREEN;
                        setFormSubmissionScreen();
                    });
                } else {
                    runOnUiThread(() -> {
                        nextMemberGroup = initializeRadioGroup();
                        setScreenTOAskForNextMemberVerfication();
                    });
                }
            } else {
                memberSelectionScreen();
            }
        } else if (requestCode == ActivityConstants.IDSP_ACTIVITY_NEW_FAMILY_REQUEST_CODE) {
            bodyLayoutContainer.removeAllViews();
            addSearchTextBox();
            addFamilyList();
            screenName = FAMILY_SELECTION_SCREEN;
        } else if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Integer locationId = Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID));
                selectedAshaAreas.clear();
                selectedAshaAreas.add(locationId);
                showProcessDialog();
                bodyLayoutContainer.removeAllViews();
                addSearchTextBox();
                addFamilyList();
            } else {
                navigateToHomeScreen(false);
            }
        } else {
            navigateToHomeScreen(false);
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
                LabelConstants.WANT_TO_CLOSE_SURVEILLANCE,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (screenName == null || screenName.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            setSubTitle(null);
            switch (screenName) {
                case FAMILY_SELECTION_SCREEN:
                    startLocationSelectionActivity();
                    break;
                case OPTION_SELECTION_SCREEN:
                    selectedFamilyIndex = -1;
                    bodyLayoutContainer.removeAllViews();
                    addSearchTextBox();
                    addFamilyList();
                    break;
                case MEMBER_SELECTION_SCREEN:
                    if (nextMemberGroup != null) {
                        setScreenTOAskForNextMemberVerfication();
                    } else {
                        setOptionSelectionScreen();
                    }
                    break;
                case FORM_SUBMISSION_SCREEN:
                    nextButton.setText(GlobalTypes.EVENT_NEXT);
                    setOptionSelectionScreen();
                    break;
                case NEXT_MEMBER_SELECTION_SCREEN:
                    SewaUtil.generateToast(this, LabelConstants.FORM_FOR_A_MEMBER_IS_ALREADY_STORED);
                    break;
                case FINAL_SUBMISSION_SCREEN:
                    if (!canMoveBack) {
                        SewaUtil.generateToast(IDSP2Activity.this, LabelConstants.FORM_FOR_ALL_MEMBERS_HAS_BEEN_FILLED);
                        break;
                    }
                    setScreenTOAskForNextMemberVerfication();
                    canMoveBack = true;
                    break;
                default:
                    finish();
            }
        }
        return true;
    }

    @Override
    public void onLoadMoreItems() {
        if (FAMILY_SELECTION_SCREEN.equals(screenName)) {
            onLoadMoreBackground();
        }
    }

    @Background
    public void onLoadMoreBackground() {
        List<FamilyDataBean> familyDataBeansList = fhsService.retrieveFamilyDataBeansForIDSPByVillage(selectedVillage, selectedAshaAreas, limit, offset);
        offset = offset + limit;
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
