package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.FhsConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.NcdServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.FormMetaDataUtil;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@EActivity
public class NcdScreeningFhwActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    private static final String FAMILY_SELECTION_SCREEN = "familySelectionScreen";
    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";
    private static final String CBAC_QUESTION_SCREEN = "cbacQuestionScreen";
    private static final String SERVICE_SELECTION_SCREEN = "serviceSelectionScreen";
    private static final String VILLAGE_SELECTION_SCREEN = "villageSelectionScreen";
    private static final String PERSONAL_HISTORY_SCREEN = "personalHistoryScreen";
    private static final Integer ACTIVITY_CODE_FOR_CBAC_DETAILS_ACTIVITY = 300;
    private static final Integer ACTIVITY_CODE_FOR_CBAC_FORM_ACTIVITY = 400;
    private static final Integer ACTIVITY_CODE_FOR_PERSONAL_HISTORY_DETAILS_ACTIVITY = 500;
    private static final Integer ACTIVITY_CODE_FOR_PERSONAL_HISTORY_FORM_ACTIVITY = 600;
    private static final Integer ACTIVITY_CODE_FOR_SCREENING_FORM_ACTIVITY = 200;
    private static final Integer RADIO_BUTTON_ID_YES = 501;
    private static final Integer RADIO_BUTTON_ID_NO = 502;
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
    private boolean selectedMemberCbacDone;
    private boolean selectedMemberPersonalHistoryDone;
    private LinearLayout bodyLayoutContainer;
    private LinearLayout footerLayout;
    private Button nextButton;
    private String screen;
    private Spinner ashaAreaSpinner;
    private RadioGroup cbacRadioGroup;
    private RadioGroup personalHistoryRadioGroup;
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
                retrieveFamilyListFromDB(null,null);
                break;

            case FAMILY_SELECTION_SCREEN:
                if (familyDataBeans == null || familyDataBeans.isEmpty()) {
                    navigateToHomeScreen(false);
                    return;
                }
                if (selectedFamilyIndex != -1) {
                    selectedFamily = familyDataBeans.get(selectedFamilyIndex);
                    retrieveMemberListFromDB(selectedFamily.getFamilyId());
                } else {
                    SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_FAMILY));
                }
                break;

            case MEMBER_SELECTION_SCREEN:
                if (selectedMemberIndex != -1) {
                    selectedMember = memberDataBeans.get(selectedMemberIndex);
//                    if (isMemberAllScreeningDone(selectedMember)) {
//                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.ALL_SCREENING_IS_DONE_FOR_SELECTED_MEMBER));
//                    } else {
//                        setCbacQuestionScreen();
                    setServiceSelectionScreen();
//                    }
                } else {
                    SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.MEMBER_SELECTION_REQUIRED_FROM_FAMILY_ALERT));
                }
                break;

            case CBAC_QUESTION_SCREEN:
                if (cbacRadioGroup.getCheckedRadioButtonId() == -1) {
                    SewaUtil.generateToast(this, LabelConstants.PLEASE_SELECT_AN_OPTION);
                } else if (cbacRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                    if (selectedMemberCbacDone) {
                        if (sewaService.isOnline()) {
                            showProcessDialog();
                            Intent myIntent = new Intent(this, CbacDetailsActivity_.class);
                            myIntent.putExtra("memberId", selectedMember.getId());
                            startActivityForResult(myIntent, ACTIVITY_CODE_FOR_CBAC_DETAILS_ACTIVITY);
                            showProcessDialog();
                        } else {
                            showNotOnlineMessage();
                        }
                    } else {
                        String formType = FormConstants.NCD_ASHA_CBAC;
                        showProcessDialog();
                        Intent myIntent = new Intent(this, DynamicFormActivity_.class);
                        myIntent.putExtra(SewaConstants.ENTITY, formType);
                        setMetadataForFormByFormType(selectedMember);
                        startActivityForResult(myIntent, ACTIVITY_CODE_FOR_CBAC_FORM_ACTIVITY);
                        showProcessDialog();
                    }
                } else {
                    setPersonalHistoryScreen();
                }
                break;

            case PERSONAL_HISTORY_SCREEN:
                if (personalHistoryRadioGroup.getCheckedRadioButtonId() == -1) {
                    SewaUtil.generateToast(this, LabelConstants.PLEASE_SELECT_AN_OPTION);
                } else if (personalHistoryRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                    if (selectedMemberPersonalHistoryDone) {
                        if (sewaService.isOnline()) {
                            showProcessDialog();
                            Intent myIntent = new Intent(this, PersonalHistoryDetailsActivity_.class);
                            myIntent.putExtra("memberId", selectedMember.getId());
                            startActivityForResult(myIntent, ACTIVITY_CODE_FOR_PERSONAL_HISTORY_DETAILS_ACTIVITY);
                            showProcessDialog();
                        } else {
                            showNotOnlineMessage();
                        }
                    } else {
                        String formType = FormConstants.NCD_PERSONAL_HISTORY;
                        showProcessDialog();
                        Intent myIntent = new Intent(this, DynamicFormActivity_.class);
                        myIntent.putExtra(SewaConstants.ENTITY, formType);
                        setMetadataForFormByFormType(selectedMember);
                        startActivityForResult(myIntent, ACTIVITY_CODE_FOR_PERSONAL_HISTORY_FORM_ACTIVITY);
                        showProcessDialog();
                    }
                } else {
                    setServiceSelectionScreen();
                }
                break;

            case SERVICE_SELECTION_SCREEN:
                if (selectedServiceIndex != -1) {
                    String formType = screeningForms.get(selectedServiceIndex);
                    if (formType != null) {
//                        if (selectedMemberCompletedScreenings.contains(formType)) {
//                            SewaUtil.generateToast(this, UtilBean.getMyLabel(
//                                    LabelConstants.FORM_FOR_SELECTED_SERVICE_IS_ALREADY_FILLED_SO_SELECT_ANOTHER_SERVICE));
//                        } else {
                        showProcessDialog();
                        Intent myIntent = new Intent(this, DynamicFormActivity_.class);
                        myIntent.putExtra(SewaConstants.ENTITY, formType);
                        setMetadataForFormByFormType(selectedMember);
                        startActivityForResult(myIntent, ACTIVITY_CODE_FOR_SCREENING_FORM_ACTIVITY);
                        selectedServiceIndex = -1;
                        showProcessDialog();
//                        }
                    }
                } else {
                    SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_TYPE_OF_SERVICE));
                }
                break;
            default:
        }
    }

    private void addSearchTextBox() {
        final TextInputLayout editText;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            editText = MyStaticComponents.getEditTextWithQrScan(this, LabelConstants.SEARCH_FAMILY, 1, 20, 1);
        }else {
            editText = MyStaticComponents.getEditText(this, LabelConstants.SEARCH_FAMILY, 1, 20, 1);
        }

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
                                            retrieveFamilyListFromDB(s.toString(), null);
                                            editText.setFocusable(true);
                                        });
                                    } else if (s == null || s.length() == 0) {
                                        runOnUiThread(() -> {
                                            showProcessDialog();
                                            retrieveFamilyListFromDB(null, null);
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

    @Background
    public void retrieveFamilyListFromDB(String familyId, String qrData) {
        searchString = familyId;
        qrScanFilter = SewaUtil.setQrScanFilterData(qrData);
        offset = 0;
        selectedFamilyIndex = -1;
        familyDataBeans = ncdService.retrieveFamiliesForNcdScreening(familyId, selectedAshaAreas, LIMIT, offset, qrScanFilter);
        offset = offset + LIMIT;
        setFamilySelectionScreen(familyId != null);
    }

    @UiThread
    public void setFamilySelectionScreen(boolean isSearch) {
        screen = FAMILY_SELECTION_SCREEN;
        bodyLayoutContainer.removeView(textView);
        bodyLayoutContainer.removeView(paginatedListView);
        bodyLayoutContainer.removeView(noFamilyTextView);

        familyIds = new ArrayList<>();
        if (!familyDataBeans.isEmpty()) {
            textView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_FAMILY);
            bodyLayoutContainer.addView(textView);
            List<ListItemDataBean> familyList = getFamilyList(familyDataBeans);

            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedFamilyIndex = position;

            if (isSearch) {
                paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, familyList, R.layout.listview_row_with_two_item, onItemClickListener, null);
            } else {
                paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, familyList, R.layout.listview_row_with_two_item, onItemClickListener, this);
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

    private List<ListItemDataBean> getFamilyList(List<FamilyDataBean> familyDataBeans) {
        List<ListItemDataBean> list = new ArrayList<>();
        String rbText;
        for (FamilyDataBean familyDataBean : familyDataBeans) {
            familyIds.add(familyDataBean.getFamilyId());
        }

        Map<String, MemberDataBean> headMembersMapWithFamilyIdAsKey = fhsService.retrieveHeadMemberDataBeansByFamilyId(familyIds);

        for (FamilyDataBean familyDataBean : familyDataBeans) {
            MemberDataBean headMember = headMembersMapWithFamilyIdAsKey.get(familyDataBean.getFamilyId());
            if (headMember != null) {
                familyDataBean.setHeadMemberName(headMember.getFirstName() + " " + headMember.getMiddleName() + " " + headMember.getGrandfatherName() + " " + headMember.getLastName());
                familyDataBean.setHeadMemberName(familyDataBean.getHeadMemberName().replace(LabelConstants.NULL, ""));
                rbText = headMember.getFirstName() + " " + headMember.getMiddleName() + " " + headMember.getGrandfatherName() + " " + headMember.getLastName();
                rbText = rbText.replace(" " + LabelConstants.NULL, "");
            } else {
                rbText = UtilBean.getMyLabel(LabelConstants.HEAD_NAME_NOT_AVAILABLE);
            }

            if (familyDataBean.getLastMemberNcdScreeningDate() != null && new Date(familyDataBean.getLastMemberNcdScreeningDate()).after(UtilBean.getStartOfFinancialYear(null))) {
                list.add(new ListItemDataBean(null, familyDataBean.getFamilyId(), null, null, rbText, true, familyDataBean.getEveningAvailability()));
            } else {
                list.add(new ListItemDataBean(null, familyDataBean.getFamilyId(), null, null, rbText, false, familyDataBean.getEveningAvailability()));
            }
        }
        return list;
    }

    @Background
    public void retrieveMemberListFromDB(String familyId) {
        if (familyId != null) {
            selectedMemberIndex = -1;
            memberDataBeans = ncdService.retrieveMembersListForNcdScreening(familyId);
            setMemberSelectionScreen();
        }
    }

    @UiThread
    public void setMemberSelectionScreen() {
        screen = MEMBER_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        List<ListItemDataBean> list = new ArrayList<>();
        StringBuilder text;

        if (!memberDataBeans.isEmpty()) {
            bodyLayoutContainer.addView(MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_A_MEMBER_FROM_LIST));

            for (MemberDataBean memberDataBean : memberDataBeans) {
                text = new StringBuilder(UtilBean.getMemberFullName(memberDataBean));
                if (getMemberScreeningStatus(memberDataBean, true, false)) {
                    text.append(" ✔️️");
                    list.add(new ListItemDataBean(null, FhsConstants.COMPLETED_STATE, memberDataBean.getUniqueHealthId(), text.toString()));
                } else if (getMemberScreeningStatus(memberDataBean, false, false)) {
                    list.add(new ListItemDataBean(memberDataBean.getEveningAvailability(), FhsConstants.INCOMPLETE_STATE, memberDataBean.getUniqueHealthId(), text.toString()));
                } else if (getMemberScreeningStatus(memberDataBean, false, true)) {
                    list.add(new ListItemDataBean(memberDataBean.getEveningAvailability(), FhsConstants.NOT_STARTED_STATE, memberDataBean.getUniqueHealthId(), text.toString()));
                } else {
                    list.add(new ListItemDataBean(memberDataBean.getEveningAvailability(), FhsConstants.STARTED_STATE, memberDataBean.getUniqueHealthId(), text.toString()));
                }
            }
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedMemberIndex = position;
            PagingListView listView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_item, onItemClickListener, null);
            bodyLayoutContainer.addView(listView);
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBERS_HAVING_AGE_MORE_THAN_15_YEARS_IN_FAMILY));
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }
    }

    private boolean isMemberAllScreeningDone(MemberDataBean memberDataBean) {
        String financialYear = UtilBean.getFinancialYearFromDate(new Date());
        if (memberDataBean.getGender() != null && memberDataBean.getAdditionalInfo() != null) {
            if (memberDataBean.getGender().equals(GlobalTypes.MALE) || memberDataBean.getGender().equals(GlobalTypes.TRANSGENDER)) {
                return memberDataBean.getHypYear() != null && memberDataBean.getHypYear().contains(financialYear)
                        && memberDataBean.getDiabetesYear() != null && memberDataBean.getDiabetesYear().contains(financialYear)
//                        && memberDataBean.getOralYear() != null && memberDataBean.getOralYear().contains(financialYear)
                        && memberDataBean.getMentalHealthYear() != null && memberDataBean.getMentalHealthYear().contains(financialYear);
            } else if (memberDataBean.getGender().equals(GlobalTypes.FEMALE)) {
                return memberDataBean.getHypYear() != null && memberDataBean.getHypYear().contains(financialYear)
                        && memberDataBean.getDiabetesYear() != null && memberDataBean.getDiabetesYear().contains(financialYear)
//                        && memberDataBean.getOralYear() != null && memberDataBean.getOralYear().contains(financialYear)
//                        && memberDataBean.getBreastYear() != null && memberDataBean.getBreastYear().contains(financialYear)
//                        && memberDataBean.getCervicalYear() != null && memberDataBean.getCervicalYear().contains(financialYear)
                        && memberDataBean.getMentalHealthYear() != null && memberDataBean.getMentalHealthYear().contains(financialYear);
            }
        }
        return false;
    }

    private boolean isMemberAnyScreeningDone(MemberDataBean memberDataBean) {
        String financialYear = UtilBean.getFinancialYearFromDate(new Date());
        if (memberDataBean.getGender() != null && memberDataBean.getAdditionalInfo() != null) {
            if (memberDataBean.getGender().equals(GlobalTypes.MALE) || memberDataBean.getGender().equals(GlobalTypes.TRANSGENDER)) {
                return (memberDataBean.getHypYear() != null && memberDataBean.getHypYear().contains(financialYear))
                        || (memberDataBean.getDiabetesYear() != null && memberDataBean.getDiabetesYear().contains(financialYear))
//                        || (memberDataBean.getOralYear() != null && memberDataBean.getOralYear().contains(financialYear))
                        || (memberDataBean.getMentalHealthYear() != null && memberDataBean.getMentalHealthYear().contains(financialYear));
            } else if (memberDataBean.getGender().equals(GlobalTypes.FEMALE)) {
                return (memberDataBean.getHypYear() != null && memberDataBean.getHypYear().contains(financialYear))
                        || (memberDataBean.getDiabetesYear() != null && memberDataBean.getDiabetesYear().contains(financialYear))
//                        || (memberDataBean.getOralYear() != null && memberDataBean.getOralYear().contains(financialYear))
//                        || (memberDataBean.getBreastYear() != null && memberDataBean.getBreastYear().contains(financialYear))
//                        || (memberDataBean.getCervicalYear() != null && memberDataBean.getCervicalYear().contains(financialYear))
                        || (memberDataBean.getMentalHealthYear() != null && memberDataBean.getMentalHealthYear().contains(financialYear));
            }
        }
        return false;
    }


    private boolean getMemberScreeningStatus(MemberDataBean memberDataBean, boolean checkAllScreeningDone, boolean neverScreenedBefore) {
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

    private void setCbacQuestionScreen() {
        bodyLayoutContainer.removeAllViews();
        screen = CBAC_QUESTION_SCREEN;
        setSubTitle(UtilBean.getMemberFullName(selectedMember));

        if (selectedMember.getCbacDate() != null && new Date(selectedMember.getCbacDate()).after(UtilBean.getStartOfFinancialYear(null))) {
            selectedMemberCbacDone = true;
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.CBAC_FORM_IS_ALREADY_FILLED));
        } else {
            selectedMemberCbacDone = false;
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.WANT_TO_FILL_CBAC_FORM));
        }

        Map<Integer, String> stringMap = new HashMap<>();
        stringMap.put(RADIO_BUTTON_ID_YES, LabelConstants.YES);
        stringMap.put(RADIO_BUTTON_ID_NO, LabelConstants.NO);
        cbacRadioGroup = MyStaticComponents.getRadioGroup(this, stringMap, true);
        bodyLayoutContainer.addView(cbacRadioGroup);
        hideProcessDialog();
    }

    private void setPersonalHistoryScreen() {
        bodyLayoutContainer.removeAllViews();
        screen = PERSONAL_HISTORY_SCREEN;
        setSubTitle(UtilBean.getMemberFullName(selectedMember));

        selectedMember = new MemberDataBean(fhsService.retrieveMemberBeanByActualId(Long.parseLong(selectedMember.getId())));
        if (Boolean.TRUE.equals(selectedMember.getPersonalHistoryDone())) {
            selectedMemberPersonalHistoryDone = true;
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                    UtilBean.getMyLabel("Personal History form for this member is already filled. Do you want to view the personal history details form details for this member?")));

        } else {
            selectedMemberPersonalHistoryDone = false;
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.WANT_TO_FILL_PERSONAL_HISTORY_FORM));
        }
        Map<Integer, String> stringMap = new HashMap<>();
        stringMap.put(RADIO_BUTTON_ID_YES, LabelConstants.YES);
        stringMap.put(RADIO_BUTTON_ID_NO, LabelConstants.NO);
        personalHistoryRadioGroup = MyStaticComponents.getRadioGroup(this, stringMap, true);

        bodyLayoutContainer.addView(personalHistoryRadioGroup);
    }

    private void setServiceSelectionScreen() {
        bodyLayoutContainer.removeAllViews();
        screen = SERVICE_SELECTION_SCREEN;
        setSubTitle(UtilBean.getMemberFullName(selectedMember));
        boolean eveningAvailability = false;

        if (selectedMember.getEveningAvailability() != null && selectedMember.getEveningAvailability()) {
            eveningAvailability = true;
        }
        MaterialCheckBox eveningCheckbox = MyStaticComponents.getCheckBox(context, UtilBean.getMyLabel(LabelConstants.EVENING), -1, eveningAvailability);
        eveningCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectedMember.setEveningAvailability(isChecked);
            selectedFamily.setEveningAvailability(isChecked);
            ncdService.markMemberEveningAvailability(Long.valueOf(selectedMember.getId()), selectedMember.getFamilyId(), isChecked);
            SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.AVAILABLE_EVENING));
        });
        bodyLayoutContainer.addView(eveningCheckbox);

        String rbText;
        MaterialTextView serviceType = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_SERVICE_TYPE);
        bodyLayoutContainer.addView(serviceType);

        selectedServiceIndex = -1;
        screeningForms = new ArrayList<>();
        selectedMemberCompletedScreenings = new ArrayList<>();

        String financialYear = UtilBean.getFinancialYearFromDate(new Date());
        List<ListItemDataBean> list = new ArrayList<>();

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.YEAR, -40);

        isHealthScreeningDone = false;

        //NCD Health Screening
//        rbText = UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(FormConstants.NCD_FHW_HEALTH_SCREENING));
//        screeningForms.add(FormConstants.NCD_FHW_HEALTH_SCREENING);
//        if (selectedMember.getHealthYear() != null && selectedMember.getHealthYear().contains(financialYear)) {
//            isHealthScreeningDone = true;
//            rbText = rbText + " ✔️️";
//            selectedMemberCompletedScreenings.add(FormConstants.NCD_FHW_HEALTH_SCREENING);
//        }
//        list.add(new ListItemDataBean(rbText));

        //NCD Mental Health Screening
        rbText = UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(FormConstants.NCD_FHW_MENTAL_HEALTH));
        screeningForms.add(FormConstants.NCD_FHW_MENTAL_HEALTH);
        if (selectedMember.getMentalHealthYear() != null && selectedMember.getMentalHealthYear().contains(financialYear)) {
            rbText = rbText + " ✔️️";
            selectedMemberCompletedScreenings.add(FormConstants.NCD_FHW_MENTAL_HEALTH);
        }
        list.add(new ListItemDataBean(rbText));

        if (selectedMember.getDob() != null) {
            Date dobDate = new Date(Long.parseLong(selectedMember.getDob().toString()));

            if (dobDate.before(instance.getTime())) {
                //NCD Hypertension Screening
                rbText = UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(FormConstants.NCD_FHW_HYPERTENSION));
                screeningForms.add(FormConstants.NCD_FHW_HYPERTENSION);
                if (selectedMember.getHypYear() != null && selectedMember.getHypYear().contains(financialYear)) {
                    rbText = rbText + " ✔️️";
                    selectedMemberCompletedScreenings.add(FormConstants.NCD_FHW_HYPERTENSION);
                }
                list.add(new ListItemDataBean(rbText));

                //NCD Diabetes Screening
                rbText = UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(FormConstants.NCD_FHW_DIABETES));
                screeningForms.add(FormConstants.NCD_FHW_DIABETES);
                if (selectedMember.getDiabetesYear() != null && selectedMember.getDiabetesYear().contains(financialYear)) {
                    rbText = rbText + " ✔️️";
                    selectedMemberCompletedScreenings.add(FormConstants.NCD_FHW_DIABETES);
                }
                list.add(new ListItemDataBean(rbText));

                //NCD Oral Screening
//                rbText = UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(FormConstants.NCD_FHW_ORAL));
//                screeningForms.add(FormConstants.NCD_FHW_ORAL);
//                if (selectedMember.getOralYear() != null && selectedMember.getOralYear().contains(financialYear)) {
//                    rbText = rbText + " ✔️️";
//                    selectedMemberCompletedScreenings.add(FormConstants.NCD_FHW_ORAL);
//                }
//                list.add(new ListItemDataBean(rbText));
//
//                if (selectedMember.getGender() != null && selectedMember.getGender().equals(GlobalTypes.FEMALE)) {
//                    //NCD Breast Screening
//                    rbText = UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(FormConstants.NCD_FHW_BREAST));
//                    screeningForms.add(FormConstants.NCD_FHW_BREAST);
//                    if (selectedMember.getBreastYear() != null && selectedMember.getBreastYear().contains(financialYear)) {
//                        rbText = rbText + " ✔️️";
//                        selectedMemberCompletedScreenings.add(FormConstants.NCD_FHW_BREAST);
//                    }
//                    list.add(new ListItemDataBean(rbText));
//
//                    //NCD Cervical Screening
//                    rbText = UtilBean.getMyLabel(UtilBean.getFullFormOfEntity().get(FormConstants.NCD_FHW_CERVICAL));
//                    screeningForms.add(FormConstants.NCD_FHW_CERVICAL);
//                    if (selectedMember.getCervicalYear() != null && selectedMember.getCervicalYear().contains(financialYear)) {
//                        rbText = rbText + " ✔️️";
//                        selectedMemberCompletedScreenings.add(FormConstants.NCD_FHW_CERVICAL);
//                    }
//                    list.add(new ListItemDataBean(rbText));
//                }
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
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult resultForQRScanner = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Integer locationId = Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID));
                selectedAshaAreas.clear();
                selectedAshaAreas.add(locationId);
                showProcessDialog();
                bodyLayoutContainer.removeAllViews();
                bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                addSearchTextBox();
                retrieveFamilyListFromDB(null, null);
            } else {
                navigateToHomeScreen(false);
            }
            return;
        } else if (resultForQRScanner != null) {
            if (resultForQRScanner.getContents() == null) {
                SewaUtil.generateToast(this, LabelConstants.FAILED_TO_SCAN_QR);
            } else {
                //show dialogue with resultForQRScanner
                String scanningData = resultForQRScanner.getContents();
                Log.i(getClass().getSimpleName(), "QR Scanner Data : " + scanningData);
                retrieveFamilyListFromDB(null, scanningData);
            }
        }

        if (requestCode == ACTIVITY_CODE_FOR_CBAC_FORM_ACTIVITY) {
            showProcessDialog();
            retrieveMemberListFromDB(selectedFamily.getFamilyId());
            hideProcessDialog();
            return;
        }

        if (requestCode == ACTIVITY_CODE_FOR_CBAC_DETAILS_ACTIVITY) {
            if (resultCode == RESULT_CANCELED) {
                showProcessDialog();
                setCbacQuestionScreen();
                hideProcessDialog();
                return;
            } else if (resultCode == RESULT_OK) {
                showProcessDialog();
                setPersonalHistoryScreen();
                hideProcessDialog();
                return;
            }
        }

        if (requestCode == ACTIVITY_CODE_FOR_PERSONAL_HISTORY_FORM_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                showProcessDialog();
                setServiceSelectionScreen();
                hideProcessDialog();
                return;
            } else {
                showProcessDialog();
                setCbacQuestionScreen();
                nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                hideProcessDialog();
                return;
            }
        }

        if (requestCode == ACTIVITY_CODE_FOR_PERSONAL_HISTORY_DETAILS_ACTIVITY) {
            if (resultCode == RESULT_CANCELED) {
                showProcessDialog();
                setPersonalHistoryScreen();
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
        if (requestCode == ACTIVITY_CODE_FOR_SCREENING_FORM_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                selectedMember.setEveningAvailability(false);
                selectedFamily.setEveningAvailability(false);
                ncdService.markMemberEveningAvailability(Long.valueOf(selectedMember.getId()), selectedMember.getFamilyId(), false);
            }
        }
        if(selectedMember != null) {
            selectedMember = new MemberDataBean(fhsService.retrieveMemberBeanByActualId(Long.valueOf(selectedMember.getId())));
            if (getMemberScreeningStatus(selectedMember, true, false)) {
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

    private void showNotOnlineMessage() {
        if (!sewaService.isOnline()) {
            runOnUiThread(() -> {
                View.OnClickListener myListener = v -> alertDialog.dismiss();
                alertDialog = new MyAlertDialog(context, false,
                        UtilBean.getMyLabel(LabelConstants.NETWORK_CONNECTIVITY_ALERT),
                        myListener, DynamicUtils.BUTTON_OK);
                alertDialog.show();
            });
        }
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
//                    setPersonalHistoryScreen();
//                    break;
//                case PERSONAL_HISTORY_SCREEN:
//                    footerLayout.setVisibility(View.VISIBLE);
//                    setCbacQuestionScreen();
//                    break;
//                case CBAC_QUESTION_SCREEN:
                    selectedMemberIndex = -1;
                    retrieveMemberListFromDB(selectedFamily.getFamilyId());
                    nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                    break;
                case MEMBER_SELECTION_SCREEN:
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    selectedFamilyIndex = -1;
                    bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                    addSearchTextBox();
                    retrieveFamilyListFromDB(null, null);
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
        List<FamilyDataBean> familyDataBeansList = ncdService.retrieveFamiliesForNcdScreening(searchString, selectedAshaAreas, LIMIT, offset, qrScanFilter);
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
