package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.ListPopupWindow.MATCH_PARENT;

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

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.FormulaConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.LocationMasterServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberAdditionalInfoDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.service.GPSTracker;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.PointInPolygon;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by prateek on 7/11/19
 */

@EActivity
public class PhoneNumberVerificationActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    private static final String VILLAGE_SELECTION_SCREEN = "villageSelectionScreen";
    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";
    private static final String MEMBER_DETAILS_SCREEN = "memberDetailsScreen";
    private static final String PHONE_UPDATE_SCREEN = "phoneUpdateScreen";
    private static final String VERIFY_SCREEN = "verifyScreen";
    private static final String FINAL_SCREEN = "finalScreen";
    private static final long DELAY = 500;
    private static final int RADIO_BUTTON_SUBMIT = 501;
    private static final int RADIO_BUTTON_VERIFY = 502;

    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    SewaServiceImpl sewaService;
    @Bean
    LocationMasterServiceImpl locationService;

    private String currentLatitude;
    private String currentLongitude;
    private Date mobileStartDate;

    private Timer timer = new Timer();
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private MyAlertDialog myAlertDialog;
    private String screen;
    private Spinner subCenterSpinner;
    private MaterialTextView subCenterQue;
    private Spinner villageSpinner;
    private MaterialTextView villageQue;
    private List<LocationBean> subCenterList = new ArrayList<>();
    private Integer selectedSubCenter;
    private List<LocationBean> villageList = new ArrayList<>();
    private Integer selectedVillage;
    private List<MemberDataBean> memberDataBeans = new ArrayList<>();
    private MemberDataBean selectedMember;
    private TextInputLayout phoneNumberEditText;
    private RadioGroup verifyRadioGroup;
    private LinearLayout globalPanel;
    private String searchString;
    private long offset;
    private long limit = 30;
    private PagingListView paginatedListView;
    private int selectedMemberIndex = -1;
    private MaterialTextView memberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setGps();
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
        setTitle(UtilBean.getTitleText(LabelConstants.PHONE_NUMBER_VERIFICATION_TITLE));
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        setContentView(globalPanel);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        setBodyDetail();
    }

    @Background
    public void setBodyDetail() {
        startLocationSelectionActivity();
    }

    private void startLocationSelectionActivity() {
        Intent myIntent = new Intent(context, LocationSelectionActivity_.class);
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.PHONE_NUMBER_VERIFICATION_TITLE);
        startActivityForResult(myIntent, ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE);
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
                LabelConstants.ON_PHONE_NUMBER_VERIFICATION_CLOSE_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        myAlertDialog.show();
    }

    private void showAlertAndExit(final String msg) {
        runOnUiThread(() -> {
            View.OnClickListener listener = v -> {
                myAlertDialog.dismiss();
                navigateToHomeScreen(false);
            };
            myAlertDialog = new MyAlertDialog(context, false, msg, listener, DynamicUtils.BUTTON_OK);
            myAlertDialog.show();
        });
    }

    private void setGps() {
        if (!SharedStructureData.gps.isLocationProviderEnabled()) {
            // Ask user to enable GPS/network in settings
            SharedStructureData.gps.showSettingsAlert(this);
        } else {
            SharedStructureData.gps.getLocation();
            currentLatitude = String.valueOf(GPSTracker.latitude);
            currentLongitude = String.valueOf(GPSTracker.longitude);
            if (!PointInPolygon.coordinateInsidePolygon()) {
                UtilBean.showAlertAndExit(GlobalTypes.MSG_GEO_FENCING_VIOLATION, context);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalTypes.LOCATION_SERVICE_ACTIVITY) {
            selectedMemberIndex = -1;
            setGps();
        } else if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                selectedVillage = Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID));
                showProcessDialog();
                bodyLayoutContainer.removeAllViews();
                bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                addSearchTextBox();
                retrieveMemberListFromDB(null);
            } else {
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            setSubTitle(null);
            switch (screen) {
                case VILLAGE_SELECTION_SCREEN:
                    if (Boolean.TRUE.equals(SharedStructureData.currentlyDownloadingFamilyData)) {
                        showAlertAndExit(UtilBean.getMyLabel(LabelConstants.WAIT_AND_DO_NOT_DISCONNECT_INTERNET_TO_DOWNLOAD_FAMILY_DATA));
                        return;
                    }
                    showProcessDialog();
                    selectedVillage = villageList.get(villageSpinner.getSelectedItemPosition()).getActualID();
                    bodyLayoutContainer.removeAllViews();
                    bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                    addSearchTextBox();
                    retrieveMemberListFromDB(null);
                    break;

                case MEMBER_SELECTION_SCREEN:
                    if (selectedMemberIndex == -1) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_MEMBER));
                        return;
                    }
                    mobileStartDate = new Date();
                    selectedMember = memberDataBeans.get(selectedMemberIndex);
                    phoneNumberEditText = null;
                    setMemberDetailsScreen();
                    break;

                case MEMBER_DETAILS_SCREEN:
                    setPhoneNumberUpdateScreen();
                    break;

                case PHONE_UPDATE_SCREEN:
                    String phoneNumber = "";
                    if (phoneNumberEditText.getEditText() != null) {
                        phoneNumber = phoneNumberEditText.getEditText().getText().toString().trim();
                    }
                    String validation;
                    if (phoneNumber.trim().length() > 0) {
                        List<ValidationTagBean> validationTagBeans = new ArrayList<>();
                        validationTagBeans.add(new ValidationTagBean(FormulaConstants.VALIDATION_CHECK_MOBILE_NUMBER, null));
                        validationTagBeans.add(new ValidationTagBean(FormulaConstants.VALIDATION_CHECK_NUMBER_FORMAT, null));
                        validation = DynamicUtils.checkValidation(phoneNumber, 0, validationTagBeans);
                    } else {
                        validation = LabelConstants.MOBILE_NUMBER_REQUIRED_ALERT;
                    }
                    if (validation != null) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(validation));
                    } else {
                        setVerifyScreen();
                    }
                    break;

                case VERIFY_SCREEN:
                    if (verifyRadioGroup.getCheckedRadioButtonId() == -1) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_AN_OPTION));
                        return;
                    }
                    if (verifyRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_SUBMIT) {
                        setFinalScreen();
                    }
                    if (verifyRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_VERIFY) {
                        setMemberDetailsScreen();
                    }
                    break;

                case FINAL_SCREEN:
                    showProcessDialog();
                    if (phoneNumberEditText.getEditText() != null) {
                        fhsService.storeFhsrPhoneVerificationForm(Long.valueOf(selectedMember.getId()),
                                phoneNumberEditText.getEditText().getText().toString().replace(" ", ""),
                                generateAnswerString());
                        bodyLayoutContainer.removeAllViews();
                        bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                        addSearchTextBox();
                        retrieveMemberListFromDB(null);
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @UiThread
    public void addSubCenterSelectionSpinner() {
        setSubTitle(null);
        if (subCenterQue == null) {
            subCenterQue = MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.SELECT_SUB_CENTER_OR_ANM_AREA);
        }

        if (subCenterSpinner == null) {
            String[] arrayOfOptions = new String[subCenterList.size()];
            int i = 0;
            for (LocationBean locationBean : subCenterList) {
                arrayOfOptions[i] = locationBean.getName();
                i++;
            }

            subCenterSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 2);
            subCenterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    bodyLayoutContainer.removeView(villageSpinner);
                    selectedSubCenter = subCenterList.get(position).getActualID();
                    villageList = locationService.retrieveLocationBeansByLevelAndParent(6, selectedSubCenter.longValue());
                    addVillageSelectionSpinner();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //do something
                }
            });
        }

        bodyLayoutContainer.addView(subCenterQue);
        bodyLayoutContainer.addView(subCenterSpinner);
        addVillageSelectionSpinner();
    }

    @UiThread
    public void addVillageSelectionSpinner() {
        setSubTitle(null);
        bodyLayoutContainer.removeView(villageQue);
        bodyLayoutContainer.removeView(villageSpinner);

        String[] arrayOfOptions = new String[villageList.size()];
        int i = 0;
        for (LocationBean locationBean : villageList) {
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }

        if (villageQue == null) {
            villageQue = MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.SELECT_VILLAGE));
        }

        villageSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 3);

        bodyLayoutContainer.addView(villageQue);
        bodyLayoutContainer.addView(villageSpinner);
        hideProcessDialog();
    }

    private void addSearchTextBox() {
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
                                            retrieveMemberListFromDB(s.toString());
                                            editText.setFocusable(true);
                                        });
                                    } else if (s == null || s.length() == 0) {
                                        runOnUiThread(() -> {
                                            showProcessDialog();
                                            retrieveMemberListFromDB(null);
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
    public void retrieveMemberListFromDB(String search) {
        searchString = search;
        offset = 0;
        selectedMemberIndex = -1;
        memberDataBeans = fhsService.retrieveMembersForPhoneVerificationByFhsr(selectedVillage.longValue(), search, limit, offset);
        offset = offset + limit;
        setMemberSelectionScreen(search != null);
    }

    @UiThread
    public void setMemberSelectionScreen(boolean isSearch) {
        screen = MEMBER_SELECTION_SCREEN;
        bodyLayoutContainer.removeView(paginatedListView);
        bodyLayoutContainer.removeView(memberTextView);

        List<ListItemDataBean> list = new ArrayList<>();
        if (memberDataBeans != null && !memberDataBeans.isEmpty()) {
            memberTextView = MyStaticComponents.getListTitleView(context, LabelConstants.SELECT_FAMILY);
            bodyLayoutContainer.addView(memberTextView);
            for (MemberDataBean memberDataBean : memberDataBeans) {
                list.add(new ListItemDataBean(memberDataBean.getFamilyId(), memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean), null, null));
            }
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedMemberIndex = position;
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            memberTextView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBERS_FOUND_IN_VILLAGE);
            bodyLayoutContainer.addView(memberTextView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }

        if (!isSearch) {
            hideProcessDialog();
        }
    }

    private void setMemberDetailsScreen() {
        showProcessDialog();
        bodyLayoutContainer.removeAllViews();
        screen = MEMBER_DETAILS_SCREEN;
        setSubTitle(UtilBean.getMemberFullName(selectedMember));

        FamilyDataBean familyDataBean = fhsService.retrieveFamilyDataBeanByFamilyId(selectedMember.getFamilyId());
        SimpleDateFormat sdf = new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT, Locale.getDefault());
        bodyLayoutContainer.addView(MyStaticComponents.generateTitleView(context, UtilBean.getMyLabel(LabelConstants.MEMBER_DETAILS)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.NAME)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getMemberFullName(selectedMember)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.HEALTH_ID)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, selectedMember.getUniqueHealthId()));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.FAMILY_ID)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, selectedMember.getFamilyId()));

        if (selectedMember.getDob() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.DATE_OF_BIRTH)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, sdf.format(new Date(selectedMember.getDob()))));

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.AGE)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getAgeDisplayOnGivenDate(new Date(selectedMember.getDob()), new Date())));

            if (selectedMember.getYearOfWedding() != null) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(selectedMember.getDob());
                Integer yearOfBirth = c.get(Calendar.YEAR);
                Integer yearOfWedding = selectedMember.getYearOfWedding();
                int ageAtWedding = yearOfWedding - yearOfBirth;
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.AGE_AT_WEDDING)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, ageAtWedding + " " + LabelConstants.YEARS));
            }
        }

        if (familyDataBean != null) {
            if (familyDataBean.getAddress1() != null || familyDataBean.getAddress2() != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.ADDRESS)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getFamilyFullAddress(familyDataBean)));
            }

            if (familyDataBean.getReligion() != null) {
                String religion = fhsService.getValueOfListValuesById(familyDataBean.getReligion());
                if (religion != null) {
                    bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.RELIGION)));
                    bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, religion));
                }
            }

            if (familyDataBean.getCaste() != null) {
                String caste = fhsService.getValueOfListValuesById(familyDataBean.getCaste());
                if (caste != null) {
                    bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.CASTE)));
                    bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, caste));
                }
            }

            if (familyDataBean.getBplFlag() != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.BPL)));
                if (Boolean.TRUE.equals(familyDataBean.getBplFlag())) {
                    bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getMyLabel(LabelConstants.FAMILY_IS_BPL)));
                } else {
                    bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getMyLabel(LabelConstants.FAMILY_IS_NOT_BPL)));
                }
            }

            if (familyDataBean.getAreaId() != null) {
                Map<String, String> ashaInfo = fhsService.retrieveAshaInfoByAreaId(familyDataBean.getAreaId());
                if (ashaInfo != null) {
                    String ashaName = ashaInfo.get("name");
                    String mobileNumber = ashaInfo.get("mobileNumber");
                    bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.ASHA_NAME_AND_PHONE)));
                    bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, ashaName + " (" + mobileNumber + ")"));
                }
            }
        }

        String totalChildren = fhsService.getChildrenCount(Long.valueOf(selectedMember.getId()), true, false, false);
        if (totalChildren != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.NUMBER_OF_LIVING_CHILDRENS)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, totalChildren));
        }

        String maleChildren = fhsService.getChildrenCount(Long.valueOf(selectedMember.getId()), false, true, false);
        if (maleChildren != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.NUMBER_OF_BOYS)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, maleChildren));
        }

        String femaleChildren = fhsService.getChildrenCount(Long.valueOf(selectedMember.getId()), false, false, true);
        if (femaleChildren != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.NUMBER_OF_GIRLS)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, femaleChildren));
        }

        if (selectedMember.getLmpDate() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.LMP_DATE)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, sdf.format(new Date(selectedMember.getLmpDate()))));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selectedMember.getLmpDate());
            calendar.add(Calendar.DATE, 281);
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.EDD)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, sdf.format(calendar.getTime())));
        }

        if (selectedMember.getCurPregRegDate() != null && selectedMember.getLmpDate() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.PREGNANCY_REGISTRATION_DATE)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, sdf.format(new Date(selectedMember.getCurPregRegDate()))));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selectedMember.getLmpDate());
            calendar.add(Calendar.DATE, 90);
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.EARLY_REGISTRATION)));
            if (calendar.getTime().after(new Date(selectedMember.getCurPregRegDate()))) {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getMyLabel(LabelConstants.YES)));
            } else {
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, UtilBean.getMyLabel(LabelConstants.NO)));
            }
        }

        if (selectedMember.getBloodGroup() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.BLOOD_GROUP)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, selectedMember.getBloodGroup()));
        }

        if (selectedMember.getWeight() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.LAST_WEIGHT_RECORDED)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, selectedMember.getWeight() + " " + LabelConstants.UNIT_OF_MASS_IN_KG));
        }
        hideProcessDialog();
    }

    private void setPhoneNumberUpdateScreen() {
        bodyLayoutContainer.removeAllViews();
        screen = PHONE_UPDATE_SCREEN;
        setSubTitle(UtilBean.getMemberFullName(selectedMember));

        String phoneLabel = LabelConstants.ENTER_PHONE_NUMBER;
        if (selectedMember.getAdditionalInfo() != null && !selectedMember.getAdditionalInfo().isEmpty()) {
            MemberAdditionalInfoDataBean additionalInfo = new Gson().fromJson(selectedMember.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
            if (additionalInfo.getPhoneVerified() != null && additionalInfo.getPhoneVerified()) {
                phoneLabel = phoneLabel + " ✔️";
            }
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(phoneLabel)));

        if (phoneNumberEditText == null) {
            phoneNumberEditText = MyStaticComponents.getEditText(context, LabelConstants.PHONE_NUMBER, 1001, 10, InputType.TYPE_CLASS_NUMBER);

            if (phoneNumberEditText.getEditText() != null && selectedMember.getMobileNumber() != null && !selectedMember.getMobileNumber().isEmpty()) {
                phoneNumberEditText.getEditText().setText(selectedMember.getMobileNumber());
            }
        }

        bodyLayoutContainer.addView(phoneNumberEditText);
    }

    private void setVerifyScreen() {
        bodyLayoutContainer.removeAllViews();
        screen = VERIFY_SCREEN;
        setSubTitle(UtilBean.getMemberFullName(selectedMember));

        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context, LabelConstants.FORM_ENTRY_COMPLETED));
        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context, LabelConstants.FORM_ENTRY_COMPLETED_SUCCESSFULLY));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.WANT_TO_REVIEW_OR_SUBMIT_THE_DATA));

        if (verifyRadioGroup == null) {
            verifyRadioGroup = new RadioGroup(context);
            RadioButton radioButton = MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel(LabelConstants.WANT_TO_SUBMIT_THE_DATA), RADIO_BUTTON_SUBMIT);
            verifyRadioGroup.addView(radioButton, new RadioGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            radioButton = MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel(LabelConstants.WANT_TO_VERIFY_THE_DATA), RADIO_BUTTON_VERIFY);
            verifyRadioGroup.addView(radioButton, new RadioGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }

        bodyLayoutContainer.addView(verifyRadioGroup);
    }

    private void setFinalScreen() {
        screen = FINAL_SCREEN;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context, UtilBean.getMyLabel(LabelConstants.YOUR_FORM_IS_COMPLETE)));
        nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
    }

    private String generateAnswerString() {
        StringBuilder answerString = new StringBuilder();
        answerString.append("latitude")
                .append(GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR)
                .append(currentLatitude)
                .append(GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR);
        answerString.append("longitude")
                .append(GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR)
                .append(currentLongitude)
                .append(GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR);
        answerString.append("mobileStartDate")
                .append(GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR)
                .append(mobileStartDate.getTime())
                .append(GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR);
        answerString.append("mobileEndDate")
                .append(GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR)
                .append(new Date().getTime())
                .append(GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR);
        answerString.append("memberId")
                .append(GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR)
                .append(selectedMember.getId())
                .append(GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR);
        String tmpObj = "";
        if (phoneNumberEditText.getEditText() != null) {
            tmpObj = phoneNumberEditText.getEditText().getText().toString();
        }
        answerString.append("phoneNumber")
                .append(GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR)
                .append(tmpObj.replace(" ", ""))
                .append(GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR);

        return answerString.toString();
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

                case MEMBER_SELECTION_SCREEN:
                    bodyLayoutContainer.removeAllViews();
                    if (subCenterList.size() == 1) {
                        navigateToHomeScreen(false);
                    } else if (subCenterList.isEmpty()) {
                        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this,
                                UtilBean.getMyLabel(LabelConstants.DATA_NOT_SYNCED_ALERT)));
                        nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
                        nextButton.setOnClickListener(v -> navigateToHomeScreen(false));
                    } else {
                        screen = VILLAGE_SELECTION_SCREEN;
                        villageList = locationService.retrieveLocationBeansByLevelAndParent(6, selectedSubCenter.longValue());
                        addSubCenterSelectionSpinner();
                    }
                    break;

                case MEMBER_DETAILS_SCREEN:
                    showProcessDialog();
                    bodyLayoutContainer.removeAllViews();
                    setSubTitle(null);
                    bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                    addSearchTextBox();
                    retrieveMemberListFromDB(null);
                    break;

                case PHONE_UPDATE_SCREEN:
                    setMemberDetailsScreen();
                    break;

                case VERIFY_SCREEN:
                    setPhoneNumberUpdateScreen();
                    break;

                case FINAL_SCREEN:
                    setVerifyScreen();
                    break;

                default:
                    break;
            }
        }
        return true;
    }

    @Override
    public void onLoadMoreItems() {
        onLoadMoreBackground();
    }

    @Background
    public void onLoadMoreBackground() {
        List<MemberDataBean> verificationDataBeans = fhsService.retrieveMembersForPhoneVerificationByFhsr(selectedVillage.longValue(), searchString, limit, offset);
        offset = offset + limit;
        onLoadMoreUi(verificationDataBeans);
    }

    @UiThread
    public void onLoadMoreUi(List<MemberDataBean> verificationDataBeans) {
        List<ListItemDataBean> list = new ArrayList<>();
        if (verificationDataBeans != null && !verificationDataBeans.isEmpty()) {
            memberDataBeans.addAll(verificationDataBeans);
            for (MemberDataBean memberDataBean : verificationDataBeans) {
                list.add(new ListItemDataBean(memberDataBean.getFamilyId(), memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean), null, null));
            }
            paginatedListView.onFinishLoadingWithItem(true, list);
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
        }
    }
}
