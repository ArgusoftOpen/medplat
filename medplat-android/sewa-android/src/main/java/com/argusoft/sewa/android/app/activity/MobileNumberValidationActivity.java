package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyDynamicComponents;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.FormulaConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceRestClientImpl;
import com.argusoft.sewa.android.app.core.impl.TechoServiceImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.MobileNumberUpdationBean;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

@EActivity
public class MobileNumberValidationActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    TechoServiceImpl techoService;
    @Bean
    SewaServiceRestClientImpl sewaServiceRestClient;

    private static final long DELAY = 500;
    private static final String MSG_MOBILE_NULL = "Please Enter Mobile Number";
    private static final String MSG_MOBILE_NOT_VALID = "Mobile number must contain 10 digits.";

    private static final String VILLAGE_SELECTION_SCREEN = "villageSelectionScreen";
    private static final String FAMILY_SELECTION_SCREEN = "familySelectionScreen";
    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";
    private static final String MEMBER_DETAILS_SCREEN = "memberDetailsScreen";
    private static final String MOBILE_NUMBER_VERIFICATION_SCREEN = "mobileNumberVerificationScreen";

    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private LinearLayout verificationLayoutContainer;
    private Button nextButton;
    private Spinner spinner;
    private TextView headerText;
    private TextInputLayout mobileNumberEditText;
    private QueFormBean otpQuestionBean;
    private Boolean isDefaultMobileNumber;

    private List<LocationBean> locationBeans = new ArrayList<>();
    private List<FamilyDataBean> familyDataBeans = null;
    private List<MemberDataBean> memberDataBeans = new ArrayList<>();
    private FamilyDataBean familyDataBean;
    private MemberDataBean memberDataBean;
    private String locationId;

    private MyAlertDialog myAlertDialog;
    private Timer timer = new Timer();
    private String screen;

    private boolean isYesChecked = false;
    private RadioGroup radioGroupForYesNo;
    private MaterialTextView pagingHeaderView;
    private int selectedFamilyIndex = -1;
    private PagingListView paginatedListView;
    private int offset = 0;
    private int limit = 30;
    private CharSequence searchString;
    private int selectedMemberIndex = -1;
    private MaterialTextView noFamilyAvailableView;

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
        setTitle(UtilBean.getTitleText(LabelConstants.MOBILE_NUMBER_VERIFICATION_TITLE));
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        setContentView(globalPanel);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        setContentView(globalPanel);

        if (!sewaService.isOnline()) {
            showNotOnlineMessage();
            return;
        }

        if (SharedStructureData.sewaServiceRestClient == null) {
            SharedStructureData.sewaServiceRestClient = sewaServiceRestClient;
        }
        setBodyDetail();
    }

    @Background
    public void setBodyDetail() {
        startLocationSelectionActivity();
    }

    private void startLocationSelectionActivity() {
        Intent myIntent = new Intent(context, LocationSelectionActivity_.class);
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.MOBILE_NUMBER_VERIFICATION_TITLE);
        startActivityForResult(myIntent, ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        if (!sewaService.isOnline()) {
            showNotOnlineMessage();
            return;
        }
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screen) {
                case VILLAGE_SELECTION_SCREEN:
                    String selectedVillage = spinner.getSelectedItem().toString();
                    for (LocationBean locationBean : locationBeans) {
                        if (selectedVillage.equals(locationBean.getName())) {
                            locationId = String.valueOf(locationBean.getActualID());
                            break;
                        }
                    }
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    addSearchTextBox();
                    retrieveFamilyListFromDB(null);
                    break;
                case FAMILY_SELECTION_SCREEN:
                    if (selectedFamilyIndex != -1) {
                        showProcessDialog();
                        familyDataBean = familyDataBeans.get(selectedFamilyIndex);
                        showProcessDialog();
                        retriveMemberListFromDB();
                    } else {
                        if (familyDataBeans != null && !familyDataBeans.isEmpty()) {
                            SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_FAMILY));
                        } else {
                            navigateToHomeScreen(false);
                        }
                    }
                    break;
                case MEMBER_SELECTION_SCREEN:
                    if (selectedMemberIndex != -1) {
                        showProcessDialog();
                        memberDataBean = memberDataBeans.get(selectedMemberIndex);
                        showProcessDialog();
                        mobileNumberEditText = null;
                        isDefaultMobileNumber = true;
                        addMemberDetailsScreen();
                    } else {
                        if (familyDataBeans != null && !familyDataBeans.isEmpty()) {
                            SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_MEMBER));
                        } else {
                            navigateToHomeScreen(false);
                        }
                    }
                    break;
                case MEMBER_DETAILS_SCREEN:
                    SharedStructureData.isMobileVerificationBlocked = false;
                    if (radioGroupForYesNo != null) {
                        if (radioGroupForYesNo.getCheckedRadioButtonId() != -1) {
                            showProcessDialog();
                            if (isYesChecked) {
                                addMobileVerificationScreen();
                            } else {
                                addMemberSelectionScreen();
                            }
                        } else {
                            SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_AN_OPTION));
                        }
                    } else {
                        addMobileVerificationScreen();
                    }
                    break;
                case MOBILE_NUMBER_VERIFICATION_SCREEN:
                    if (mobileNumberEditText.getEditText() == null
                            || mobileNumberEditText.getEditText().getText().toString().trim().isEmpty()) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(MSG_MOBILE_NULL));
                        return;
                    } else if (!Pattern.compile("^\\d{10}$").matcher(mobileNumberEditText.getEditText().getText().toString().trim()).matches()) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(MSG_MOBILE_NOT_VALID));
                        return;
                    }
                    if (Boolean.TRUE.equals(SharedStructureData.isMobileVerificationBlocked)) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_ENTER_VERIFICATION_CODE_TO_CONTINUE));
                    } else if (otpQuestionBean.getAnswer() == null) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.THE_MOBILE_NUMBER_IS_NOT_YET_VERIFIED + " " +
                                LabelConstants.PLEASE_VERIFY_THE_MOBILE_NUMBER_TO_CONTINUE));
                    } else if (otpQuestionBean.getAnswer().equals("F")) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.THE_MOBILE_NUMBER_IS_NOT_YET_VERIFIED + " " +
                                LabelConstants.PLEASE_CHANGE_THE_MOBILE_NUMBER_TO_CONTINUE));
                    } else {
                        showProcessDialog();
                        mobileNumberUpdationBean();
                        retriveMemberListFromDB();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                myAlertDialog.dismiss();
                navigateToHomeScreen(false);
            } else {
                myAlertDialog.dismiss();
            }
        };

        myAlertDialog = new MyAlertDialog(context,
                GlobalTypes.MSG_CANCEL_MOBILE_VERIFICATION,
                myListener, DynamicUtils.BUTTON_YES_NO);
        myAlertDialog.show();
    }

    @UiThread
    public void addSearchTextBox() {
        bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));

        final TextInputLayout editText = MyStaticComponents.getEditText(this, LabelConstants.SEARCH_FAMILY, 1, 20, 1);
        bodyLayoutContainer.addView(editText);
        bodyLayoutContainer.addView(MyStaticComponents.getOrTextView(context));

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
                                            retrieveFamilyListFromDB(s);
                                            editText.setFocusable(true);
                                        });
                                    } else if (s == null || s.length() == 0) {
                                        runOnUiThread(() -> {
                                            showProcessDialog();
                                            retrieveFamilyListFromDB(null);
                                            editText.clearFocus();
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
    public void addVillageSelectionSpinner() {
        screen = VILLAGE_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.SELECT_VILLAGE)));
        String[] arrayOfOptions = new String[locationBeans.size()];
        int i = 0;
        for (LocationBean locationBean : locationBeans) {
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }
        spinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 2);
        if (locationId != null) {
            int integerLocationId = Integer.parseInt(locationId);
            for (LocationBean locationBean : locationBeans) {
                String locationName;
                if (locationBean.getActualID() == integerLocationId) {
                    locationName = locationBean.getName();
                    spinner.setSelection(Arrays.asList(arrayOfOptions).indexOf(locationName));
                }
            }
        }
        bodyLayoutContainer.addView(spinner);
        hideProcessDialog();
    }

    @Background
    public void retrieveFamilyListFromDB(CharSequence s) {
        searchString = s;
        offset = 0;
        selectedFamilyIndex = -1;
        familyDataBeans = fhsService.retrieveAssignedFamilyDataBeansToUserForMobileNumberUpdationAndVerification(s, locationId, limit, offset);
        offset = offset + limit;
        addFamilySelectionScreen();
    }

    @UiThread
    public void addFamilySelectionScreen() {
        screen = FAMILY_SELECTION_SCREEN;
        bodyLayoutContainer.removeView(paginatedListView);
        bodyLayoutContainer.removeView(pagingHeaderView);
        bodyLayoutContainer.removeView(noFamilyAvailableView);

        if (!familyDataBeans.isEmpty()) {
            pagingHeaderView = MyStaticComponents.getListTitleView(this, UtilBean.getMyLabel(LabelConstants.SEARCH_FAMILY));
            bodyLayoutContainer.addView(pagingHeaderView);

            List<ListItemDataBean> list = getFamilyList(familyDataBeans);

            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedFamilyIndex = position;
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            noFamilyAvailableView = MyStaticComponents.generateInstructionView(this, UtilBean.getMyLabel(LabelConstants.NO_FAMILIES_FOUND));
            bodyLayoutContainer.addView(noFamilyAvailableView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }
        hideProcessDialog();
    }

    private List<ListItemDataBean> getFamilyList(List<FamilyDataBean> familyDataBeans) {
        List<String> familyIds = new ArrayList<>();
        String rbText;
        List<ListItemDataBean> list = new ArrayList<>();

        for (FamilyDataBean fDataBean : familyDataBeans) {
            familyIds.add(fDataBean.getFamilyId());
        }
        Map<String, MemberDataBean> headMembersMapWithFamilyIdAsKey = fhsService.retrieveHeadMemberDataBeansByFamilyId(familyIds);
        for (FamilyDataBean fDataBean : familyDataBeans) {
            MemberDataBean headMember = headMembersMapWithFamilyIdAsKey.get(fDataBean.getFamilyId());
            if (headMember != null) {
                rbText = headMember.getFirstName() + " " + headMember.getMiddleName() + " " + headMember.getGrandfatherName() + " " + headMember.getLastName();
                rbText = rbText.replace(" " + LabelConstants.NULL, "");
            } else {
                rbText = UtilBean.getMyLabel(LabelConstants.HEAD_NAME_NOT_AVAILABLE);
            }
            list.add(new ListItemDataBean(fDataBean.getFamilyId(), rbText));
        }
        return list;
    }

    @Background
    public void retriveMemberListFromDB() {
        memberDataBeans = fhsService.retrieveMemberDataBeanForMobileNumberUpdation(familyDataBean.getFamilyId());
        addMemberSelectionScreen();
    }

    @UiThread
    public void addMemberSelectionScreen() {
        bodyLayoutContainer.removeAllViews();
        screen = MEMBER_SELECTION_SCREEN;

        MaterialTextView titleView = MyStaticComponents.getListTitleView(this, UtilBean.getMyLabel(LabelConstants.SEARCH_MEMBER));
        bodyLayoutContainer.addView(titleView);

        List<ListItemDataBean> list = new ArrayList<>();
        String rbText;
        if (!memberDataBeans.isEmpty()) {
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedMemberIndex = position;
            for (MemberDataBean member : memberDataBeans) {
                rbText = member.getFirstName() + " " + member.getLastName();
                list.add(new ListItemDataBean(member.getUniqueHealthId(), rbText));
            }
            PagingListView memberList = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_item, onItemClickListener, null);
            bodyLayoutContainer.addView(memberList);
        } else {
            MaterialTextView instructionView = MyStaticComponents.generateInstructionView(this, UtilBean.getMyLabel(LabelConstants.MOBILE_NUMBER_VERIFIED_FOR_ALL_MEMBER));
            bodyLayoutContainer.addView(instructionView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }
        hideProcessDialog();
    }

    @UiThread
    public void addMemberDetailsScreen() {
        screen = MEMBER_DETAILS_SCREEN;
        bodyLayoutContainer.removeAllViews();
        setSubTitle(UtilBean.getMemberFullName(memberDataBean));

        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context,
                UtilBean.getMyLabel(LabelConstants.MEMBER_DETAILS)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.MEMBER_ID)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                memberDataBean.getUniqueHealthId()));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.MEMBER_NAME)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                memberDataBean.getFirstName() + " " + memberDataBean.getMiddleName() + " " + memberDataBean.getLastName()));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.DATE_OF_BIRTH)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                (new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault())).format(memberDataBean.getDob())));

        try {
            int[] yearMonthDayAge = UtilBean.calculateAgeYearMonthDay(memberDataBean.getDob());
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                    context, UtilBean.getMyLabel(LabelConstants.AGE)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                    UtilBean.getAgeDisplay(yearMonthDayAge[0], yearMonthDayAge[1], yearMonthDayAge[2])));
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.GENDER)));

        String gender = memberDataBean.getGender();
        switch (gender) {
            case GlobalTypes.MALE:
                gender = LabelConstants.MALE;
                break;
            case GlobalTypes.FEMALE:
                gender = LabelConstants.FEMALE;
                break;
            case GlobalTypes.TRANSGENDER:
                gender = LabelConstants.TRANSGENDER;
                break;
            default:
        }
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                UtilBean.getMyLabel(gender)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, UtilBean.getMyLabel(LabelConstants.MOBILE_NUMBER)));
        if (memberDataBean.getMobileNumber() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                    memberDataBean.getMobileNumber()));

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                    context, UtilBean.getMyLabel(LabelConstants.MOBILE_NUMBER_VERIFIED)));

            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                    UtilBean.getMyLabel(memberDataBean.getMobileNumberVerified() != null && memberDataBean.getMobileNumberVerified().equals(Boolean.TRUE) ? GlobalTypes.YES : GlobalTypes.NO)));

            if (memberDataBean.getMobileNumberVerified() != null && memberDataBean.getMobileNumberVerified().equals(Boolean.TRUE)) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                        LabelConstants.DO_YOU_WANT_TO_CHANGE_THE_MOBILE_NUMBER));

                radioGroupForYesNo = new RadioGroup(context);
                RadioButton yesRadioButton = MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel(LabelConstants.YES), 1);
                RadioButton noRadioButton = MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel(LabelConstants.NO), 2);
                radioGroupForYesNo.addView(yesRadioButton);
                radioGroupForYesNo.addView(noRadioButton);
                radioGroupForYesNo.setOnCheckedChangeListener((group, checkedId) -> isYesChecked = checkedId == 1);
                bodyLayoutContainer.addView(radioGroupForYesNo);
            } else {
                radioGroupForYesNo = null;
            }
        } else {
            radioGroupForYesNo = null;
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getMyLabel(GlobalTypes.NOT_AVAILABLE)));
        }

        hideProcessDialog();
    }

    @UiThread
    public void addMobileVerificationScreen() {
        screen = MOBILE_NUMBER_VERIFICATION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        if (otpQuestionBean == null) {
            otpQuestionBean = new QueFormBean();
            otpQuestionBean.setLoopCounter(0);
            otpQuestionBean.setRelatedpropertyname(RelatedPropertyNameConstants.MOBILE_NUMBER);
        }

        if (headerText == null) {
            headerText = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.MOBILE_NUMBER));
        }

        if (mobileNumberEditText == null) {
            mobileNumberEditText = MyStaticComponents.getEditText(context, LabelConstants.MOBILE_NUMBER, -1, 10, InputType.TYPE_CLASS_NUMBER);

            if (mobileNumberEditText.getEditText() != null) {
                mobileNumberEditText.getEditText().addTextChangedListener(new TextWatcher() {
                    private String before;

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        if (Pattern.compile("^([6-9]?|[6-9]\\d{0,9})$").matcher(s.toString()).matches()) {
                            before = s.toString();
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (Boolean.TRUE.equals(SharedStructureData.isMobileVerificationBlocked)) {
                            SewaUtil.generateToast(context, LabelConstants.MOBILE_VERIFICATION_SERVICE_IS_TEMPORARILY_BLOCKED);
                            return;
                        }
                        if (!Pattern.compile("^([6-9]?|[6-9]\\d{0,9})$").matcher(s.toString().trim()).matches()) {
                            mobileNumberEditText.getEditText().setText(before);
                            mobileNumberEditText.getEditText().setSelection(before.length());
                            return;
                        }

                        ValidationTagBean validationTagBean = new ValidationTagBean(FormulaConstants.VALIDATION_CHECK_MOBILE_NUMBER, "Please enter valid mobile number");
                        List<ValidationTagBean> validationTagBeans = new ArrayList<>();
                        validationTagBeans.add(validationTagBean);

                        String validation = DynamicUtils.checkValidation(s.toString(), 0, validationTagBeans);

                        if (Pattern.compile("^\\d{10}$").matcher(s).matches()) {
                            if (!s.toString().equals(memberDataBean.getMobileNumber()) || memberDataBean.getMobileNumberVerified() == null || Boolean.FALSE.equals(memberDataBean.getMobileNumberVerified())) {
                                if (validation != null) {
                                    SewaUtil.generateToast(context, validation);
                                    return;
                                }
                                SharedStructureData.isMobileVerificationBlocked = false;
                                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.MOBILE_NUMBER, s.toString());
                                otpQuestionBean.setAnswer(null);
                                verificationLayoutContainer = MyDynamicComponents.getOTPBasedVerificationComponent(context, otpQuestionBean);
                                bodyLayoutContainer.addView(verificationLayoutContainer);
                            } else {
                                otpQuestionBean.setAnswer("F");
                                bodyLayoutContainer.removeView(verificationLayoutContainer);
                            }
                        } else {
                            otpQuestionBean.setAnswer(null);
                            bodyLayoutContainer.removeView(verificationLayoutContainer);
                        }
                    }
                });
            }
        }
        bodyLayoutContainer.addView(headerText);
        bodyLayoutContainer.addView(mobileNumberEditText);

        if (mobileNumberEditText.getEditText() != null) {
            if (Boolean.TRUE.equals(isDefaultMobileNumber) && memberDataBean.getMobileNumber() != null) {
                mobileNumberEditText.getEditText().setText(memberDataBean.getMobileNumber());
            } else if (!mobileNumberEditText.getEditText().getText().toString().isEmpty()) {
                mobileNumberEditText.getEditText().setText(mobileNumberEditText.getEditText().getText());
            }
        }
        isDefaultMobileNumber = false;

        hideProcessDialog();
    }

    private void mobileNumberUpdationBean() {
        MobileNumberUpdationBean mobileNumberUpdationBean = new MobileNumberUpdationBean();
        mobileNumberUpdationBean.setMemberId(memberDataBean.getId());
        mobileNumberUpdationBean.setMobileNumber(Objects.requireNonNull(mobileNumberEditText.getEditText()).getText().toString());
        mobileNumberUpdationBean.setWhatsappSmsSchems(otpQuestionBean.getAnswer().toString().endsWith("T"));

        fhsService.mobileNumberVerificationAndUpdationOfFamilyMember(mobileNumberUpdationBean, Long.valueOf(memberDataBean.getId()));
        SewaUtil.generateToast(context,
                UtilBean.getMyLabel(LabelConstants.PHONE_NUMBER_UPDATED_SUCCESSFULLY));
    }

    private void showNotOnlineMessage() {
        hideProcessDialog();
        View.OnClickListener myListener = v -> {
            myAlertDialog.dismiss();
            navigateToHomeScreen(false);
        };
        myAlertDialog = new MyAlertDialog(context,
                UtilBean.getMyLabel(LabelConstants.NETWORK_CONNECTIVITY_ALERT),
                myListener, DynamicUtils.BUTTON_OK);
        myAlertDialog.show();
        myAlertDialog.setOnCancelListener(dialog -> {
            myAlertDialog.dismiss();
            navigateToHomeScreen(false);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                locationId = data.getStringExtra(FieldNameConstants.LOCATION_ID);
                showProcessDialog();
                bodyLayoutContainer.removeAllViews();
                addSearchTextBox();
                retrieveFamilyListFromDB(null);
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
                case VILLAGE_SELECTION_SCREEN:
                    navigateToHomeScreen(false);
                    break;
                case FAMILY_SELECTION_SCREEN:
                    startLocationSelectionActivity();
                    break;
                case MEMBER_SELECTION_SCREEN:
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    selectedFamilyIndex = -1;
                    addSearchTextBox();
                    retrieveFamilyListFromDB(null);
                    break;
                case MEMBER_DETAILS_SCREEN:
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    selectedMemberIndex = -1;
                    setSubTitle(null);
                    addMemberSelectionScreen();
                    break;
                case MOBILE_NUMBER_VERIFICATION_SCREEN:
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    addMemberDetailsScreen();
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
        List<FamilyDataBean> famDataBeans = fhsService.retrieveAssignedFamilyDataBeansToUserForMobileNumberUpdationAndVerification(searchString, locationId, limit, offset);
        offset = offset + limit;
        onLoadMoreUi(famDataBeans);
    }

    @UiThread
    public void onLoadMoreUi(List<FamilyDataBean> famDataBeans) {
        if (famDataBeans != null && !famDataBeans.isEmpty()) {
            familyDataBeans.addAll(famDataBeans);
            offset = offset + limit;
            List<ListItemDataBean> stringList = getFamilyList(famDataBeans);
            paginatedListView.onFinishLoadingWithItem(true, stringList);
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
        }
    }
}
