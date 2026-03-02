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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyArrayAdapter;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.listeners.DateChangeListenerStatic;
import com.argusoft.sewa.android.app.constants.FormulaConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.LocationMasterServiceImpl;
import com.argusoft.sewa.android.app.core.impl.MigrationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.MigrationInDataBean;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.LocationMasterBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

/**
 * Created by prateek on 13/3/19.
 */

@EActivity
public class MigrateInActivity extends MenuActivity implements View.OnClickListener {

    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    MigrationServiceImpl migrationService;
    @Bean
    LocationMasterServiceImpl locationMasterService;

    private static final Integer RADIO_BUTTON_ID_YES = 1;
    private static final Integer RADIO_BUTTON_ID_NO = 2;
    private static final Integer RADIO_BUTTON_ID_NILL = 0;
    private static final String FIRST_QUESTION_SCREEN = "firstQuestionScreen";
    private static final String LOCATION_QUESTION_SCREEN = "locationQuestionScreen";
    private static final String MEMBER_QUESTION_SCREEN = "memberQuestionScreen";
    private static final String FAMILY_QUESTION_SCREEN = "familyQuestionScreen";
    private static final String FAMILY_SELECTION_SCREEN = "familySelectionScreen";
    private static final String VILLAGE_SELECTION_SCREEN = "villageSelectionScreen";
    private static final String FINAL_SCREEN = "finalScreen";
    private static final long DELAY = 1000;

    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private String screenName;
    private MyAlertDialog myAlertDialog;

    private RadioGroup migratedFromGujaratRadioGroup;

    private List<LocationMasterBean> regions;
    private Spinner regionSpinner;
    private Long selectedRegion = null;
    private List<LocationMasterBean> districts;
    private Spinner districtSpinner;
    private Long selectedDistrict = null;
    private List<LocationMasterBean> blocks;
    private Spinner blockSpinner;
    private TextInputLayout villageNameEditText;
    private TextInputLayout fhwNameEditText;
    private TextInputLayout fhwPhoneEditText;

    private TextInputLayout firstNameEditText;
    private TextInputLayout middleNameEditText;
    private TextInputLayout lastNameEditText;
    private RadioGroup genderRadioGroup;
    private DateChangeListenerStatic dobDateListener;
    private LinearLayout dobDatePicker;
    private TextInputLayout healthIdEditText;
    private TextInputLayout phoneEditText;
    private MaterialTextView pregnantQuestionView;
    private RadioGroup pregnantRadioGroup;
    private MaterialTextView lmpQuestionView;
    private DateChangeListenerStatic lmpDateListener;
    private LinearLayout lmpDatePicker;
    private TextInputLayout bankAccountEditText;
    private TextInputLayout ifscEditText;

    private RadioGroup stayingWithFamilyRadioGroup;
    private TextInputLayout searchFamilyEditText;
    private List<FamilyDataBean> searchedFamily;
    private List<LocationBean> villageList;
    private TextView villageTextView;
    private Spinner villageSpinner;
    private List<LocationBean> ashaAreaList;
    private TextView ashaAreaTextView;
    private Spinner ashaAreaSpinner;

    private Timer timer = new Timer();
    private ListView listView;
    private int selectedFamilyindex = -1;
    private int selectedAshaAreaIndex = -1;
    private MaterialTextView noFamilyTextView;
    private MaterialTextView listTitleView;
    private MaterialButton nextButton;

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
        setTitle(UtilBean.getTitleText(LabelConstants.MIGRATION_TITLE));
        setSubTitle(null);
    }

    private void initView() {
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        setContentView(globalPanel);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        setBodyDetail();

    }

    private void setBodyDetail() {
        try {
            setMemberQuestionsScreen();
        } finally {
            setContentView(globalPanel);
        }
    }

    @Override
    public void onBackPressed() {
        View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                myAlertDialog.dismiss();
                finish();
            } else {
                myAlertDialog.dismiss();
            }
        };

        myAlertDialog = new MyAlertDialog(this,
                LabelConstants.ON_MIGRATION_CLOSE_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        myAlertDialog.show();
    }

    private RadioGroup getRadioGroupYesNo() {
        HashMap<Integer, String> stringMap = new HashMap<>();
        stringMap.put(RADIO_BUTTON_ID_YES, LabelConstants.YES);
        stringMap.put(RADIO_BUTTON_ID_NO, LabelConstants.NO);
        return MyStaticComponents.getRadioGroup(this, stringMap, true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screenName) {
                case MEMBER_QUESTION_SCREEN:
                    if (Boolean.TRUE.equals(checkValidationForMemberQuestionScreen())) {
                        setFirstQuestionScreen();
                    }
                    break;

                case FIRST_QUESTION_SCREEN:
                    if (migratedFromGujaratRadioGroup.getCheckedRadioButtonId() != -1) {
                        if (migratedFromGujaratRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                            setLocationQuestionScreen();
                        } else {
                            setFamilyQuestionScreen();
                        }
                    } else {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_AN_OPTION));
                    }
                    break;

                case LOCATION_QUESTION_SCREEN:
                    if (regionSpinner.getSelectedItemPosition() == 0) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.REGION_SELECTION_REQUIRED_ALERT));
                        break;
                    }
                    if (districtSpinner.getSelectedItemPosition() == 0) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.DISTRICT_SELECTION_REQUIRED_ALERT));
                        break;
                    }
                    if (blockSpinner.getSelectedItemPosition() == 0) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.BLOCK_SELECTION_REQUIRED_ALERT));
                        break;
                    }
                    if (villageNameEditText.getEditText() != null
                            && villageNameEditText.getEditText().getText().toString().trim().length() == 0) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.VILLAGE_NAME_REQUIRED_ALERT));
                        break;
                    }
                    if (fhwPhoneEditText.getEditText() != null
                            && fhwPhoneEditText.getEditText().getText().toString().trim().length() > 0
                            && fhwPhoneEditText.getEditText().getText().toString().trim().length() != 10) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.INCORRECT_FHW_PHONE_NUMBER_ALERT));
                        break;
                    }
                    setFamilyQuestionScreen();
                    break;

                case FAMILY_QUESTION_SCREEN:
                    if (stayingWithFamilyRadioGroup.getCheckedRadioButtonId() != -1) {
                        if (stayingWithFamilyRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                            setFamilySelectionScreen();
                        } else {
                            setVillageSelectionScreen();
                        }
                    } else {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_AN_OPTION));
                    }
                    break;

                case FAMILY_SELECTION_SCREEN:
                    if (searchedFamily == null || searchedFamily.isEmpty()) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.SEARCH_FOR_FAMILY_REQUIRED_ALERT));
                        break;
                    }
                    if (selectedFamilyindex != -1) {
                        setFinalScreen();
                    } else {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.FAMILY_SELECTION_REQUIRED_ALERT_FROM_LIST));
                    }
                    break;

                case VILLAGE_SELECTION_SCREEN:
                    if (villageSpinner.getSelectedItemPosition() == 0) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.VILLAGE_SELECTION_REQUIRED_ALERT_FOR_BENEFICIARY));
                        break;
                    }
                    if (ashaAreaSpinner.getSelectedItemPosition() == 0) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.ASHA_AREA_SELECTION_REQUIRED_ALERT_FOR_BENEFICIARY));
                        break;
                    }
                    setFinalScreen();
                    break;

                case FINAL_SCREEN:
                    finishActivity();
                    break;

                default:
            }
        } else {
            switch (screenName) {
                case MEMBER_QUESTION_SCREEN:
                    onBackPressed();
                    break;

                case FIRST_QUESTION_SCREEN:
                    setMemberQuestionsScreen();
                    break;


                case LOCATION_QUESTION_SCREEN:
                    setFirstQuestionScreen();
                    break;

                case FAMILY_QUESTION_SCREEN:
                    if (migratedFromGujaratRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                        setLocationQuestionScreen();
                    } else {
                        setFirstQuestionScreen();
                    }
                    break;

                case FAMILY_SELECTION_SCREEN:
                case VILLAGE_SELECTION_SCREEN:
                    setFamilyQuestionScreen();
                    break;

                case FINAL_SCREEN:
                    nextButton.setText(GlobalTypes.EVENT_NEXT);
                    if (stayingWithFamilyRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                        setFamilySelectionScreen();
                    } else {
                        setVillageSelectionScreen();
                    }
                    break;

                default:
            }
        }
    }

    private void setFirstQuestionScreen() {
        bodyLayoutContainer.removeAllViews();
        screenName = FIRST_QUESTION_SCREEN;
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.CONFORMATION_TO_MIGRATION_OUT_OF_GUJARAT_FOR_BENEFICIARY));

        if (migratedFromGujaratRadioGroup == null) {
            migratedFromGujaratRadioGroup = getRadioGroupYesNo();
        }
        bodyLayoutContainer.addView(migratedFromGujaratRadioGroup);
    }

    private void setLocationQuestionScreen() {
        bodyLayoutContainer.removeAllViews();
        screenName = LOCATION_QUESTION_SCREEN;

        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context, LabelConstants.ENTER_LOCATION_DETAILS_FOR_MIGRATED_BENEFICIARY));

        addRegionSpinner();
        addDistrictSpinner(selectedRegion);
        addBlockSpinner(selectedDistrict);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.VILLAGE_NAME));
        if (villageNameEditText == null) {
            villageNameEditText = MyStaticComponents.getEditText(context, LabelConstants.VILLAGE_NAME, 1001, 50, InputType.TYPE_CLASS_TEXT);
        }
        bodyLayoutContainer.addView(villageNameEditText);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.FHW_OR_ASHA_NAME));
        if (fhwNameEditText == null) {
            fhwNameEditText = MyStaticComponents.getEditText(context, LabelConstants.FHW_OR_ASHA_NAME, 1002, 100, InputType.TYPE_CLASS_TEXT);
        }
        bodyLayoutContainer.addView(fhwNameEditText);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.FHW_OR_ASHA_PHONE_NUMBER));
        if (fhwPhoneEditText == null) {
            fhwPhoneEditText = MyStaticComponents.getEditText(context, LabelConstants.FHW_OR_ASHA_PHONE_NUMBER, 1003, 10, InputType.TYPE_CLASS_NUMBER);
        }
        bodyLayoutContainer.addView(fhwPhoneEditText);
    }

    private void addRegionSpinner() {
        if (regionSpinner == null) {
            regions = locationMasterService.retrieveLocationMasterBeansByLevelAndParent(1, 2L);
            String[] arrayOfOptions = new String[regions.size() + 1];
            if (!regions.isEmpty()) {
                int i = 1;
                arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
                for (LocationMasterBean locationMasterBean : regions) {
                    arrayOfOptions[i] = locationMasterBean.getName();
                    i++;
                }
            } else {
                arrayOfOptions[0] = UtilBean.getMyLabel(LabelConstants.NO_LOCATION_AVAILABLE);
            }
            regionSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 1);

            regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String[] array;
                    if (position != 0) {
                        selectedRegion = regions.get(position - 1).getActualID();
                        districts = locationMasterService.retrieveLocationMasterBeansByLevelAndParent(2, selectedRegion);
                        array = new String[districts.size() + 1];
                        array[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
                        int count = 1;
                        for (LocationMasterBean locationMasterBean : districts) {
                            array[count] = locationMasterBean.getName();
                            count++;
                        }
                    } else {
                        selectedRegion = null;
                        array = new String[1];
                        array[0] = UtilBean.getMyLabel(LabelConstants.SELECT_REGION);
                    }
                    MyArrayAdapter adapter = new MyArrayAdapter(context, R.layout.spinner_item_top, array);
                    adapter.setDropDownViewResource((R.layout.spinner_item_dropdown));
                    adapter.notifyDataSetChanged();
                    districtSpinner.setAdapter(adapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //do something
                }
            });
        }
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.REGION));
        bodyLayoutContainer.addView(regionSpinner);
    }

    private void addDistrictSpinner(Long parent) {
        if (districtSpinner == null) {
            districts = locationMasterService.retrieveLocationMasterBeansByLevelAndParent(2, parent);
            String[] arrayOfOptions = new String[districts.size() + 1];
            if (!districts.isEmpty()) {
                int i = 1;
                arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
                for (LocationMasterBean locationMasterBean : districts) {
                    arrayOfOptions[i] = locationMasterBean.getName();
                    i++;
                }
            } else {
                arrayOfOptions[0] = UtilBean.getMyLabel(LabelConstants.NO_LOCATION_AVAILABLE);
            }
            districtSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 1);

            districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String[] array;
                    if (position != 0) {
                        selectedDistrict = districts.get(position - 1).getActualID();
                        blocks = locationMasterService.retrieveLocationMasterBeansByLevelAndParent(3, selectedDistrict);
                        array = new String[blocks.size() + 1];
                        array[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
                        int count = 1;
                        for (LocationMasterBean locationMasterBean : blocks) {
                            array[count] = locationMasterBean.getName();
                            count++;
                        }
                    } else {
                        selectedDistrict = null;
                        array = new String[1];
                        array[0] = UtilBean.getMyLabel(LabelConstants.SELECT_DISTRICT);
                    }
                    MyArrayAdapter adapter = new MyArrayAdapter(context, R.layout.spinner_item_top, array);
                    adapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
                    adapter.notifyDataSetChanged();
                    blockSpinner.setAdapter(adapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //do something
                }
            });
        }
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.DISTRICT));
        bodyLayoutContainer.addView(districtSpinner);
    }

    private void addBlockSpinner(Long parent) {
        if (blockSpinner == null) {
            blocks = locationMasterService.retrieveLocationMasterBeansByLevelAndParent(3, parent);
            String[] arrayOfOptions = new String[blocks.size() + 1];
            if (!blocks.isEmpty()) {
                int i = 1;
                arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
                for (LocationMasterBean locationMasterBean : blocks) {
                    arrayOfOptions[i] = locationMasterBean.getName();
                    i++;
                }
            } else {
                arrayOfOptions[0] = UtilBean.getMyLabel(LabelConstants.SELECT_DISTRICT);
            }
            blockSpinner = MyStaticComponents.getSpinner(context, arrayOfOptions, 0, 2);

        }
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.BLOCK));
        bodyLayoutContainer.addView(blockSpinner);
    }

    private void setMemberQuestionsScreen() {
        bodyLayoutContainer.removeAllViews();
        screenName = MEMBER_QUESTION_SCREEN;

        bodyLayoutContainer.addView(MyStaticComponents.generateTitleView(context,
                UtilBean.getMyLabel(LabelConstants.MIGRATED_IN_BENEFICIARY)));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.ENTER + " " + LabelConstants.FIRST_NAME.toLowerCase()));
        if (firstNameEditText == null) {
            firstNameEditText = MyStaticComponents.getEditText(context, LabelConstants.FIRST_NAME, 1004, 50, InputType.TYPE_CLASS_TEXT);
        }
        bodyLayoutContainer.addView(firstNameEditText);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                UtilBean.getMyLabel(LabelConstants.ENTER + " " + LabelConstants.MIDDLE_NAME.toLowerCase())));
        if (middleNameEditText == null) {
            middleNameEditText = MyStaticComponents.getEditText(context, LabelConstants.MIDDLE_NAME, 1005, 50, InputType.TYPE_CLASS_TEXT);
        }
        bodyLayoutContainer.addView(middleNameEditText);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                UtilBean.getMyLabel(LabelConstants.ENTER + " " + LabelConstants.LAST_NAME.toLowerCase())));
        if (lastNameEditText == null) {
            lastNameEditText = MyStaticComponents.getEditText(context, LabelConstants.LAST_NAME, 1006, 50, InputType.TYPE_CLASS_TEXT);
        }
        bodyLayoutContainer.addView(lastNameEditText);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.ENTER + " " + LabelConstants.GENDER.toLowerCase()));
        if (genderRadioGroup == null) {
            genderRadioGroup = new RadioGroup(context);
            genderRadioGroup.addView(MyStaticComponents.getRadioButton(context, UtilBean.getMyLabel(LabelConstants.MALE), RADIO_BUTTON_ID_YES));
            genderRadioGroup.addView(MyStaticComponents.getRadioButton(context, UtilBean.getMyLabel(LabelConstants.FEMALE), RADIO_BUTTON_ID_NO));
            genderRadioGroup.addView(MyStaticComponents.getRadioButton(context, UtilBean.getMyLabel(LabelConstants.TRANSGENDER), RADIO_BUTTON_ID_NILL));

            genderRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == RADIO_BUTTON_ID_YES || checkedId == RADIO_BUTTON_ID_NILL) {
                    bodyLayoutContainer.removeView(pregnantQuestionView);
                    bodyLayoutContainer.removeView(pregnantRadioGroup);
                    bodyLayoutContainer.removeView(lmpQuestionView);
                    bodyLayoutContainer.removeView(lmpDatePicker);
                }
                if (checkedId == RADIO_BUTTON_ID_NO) {
                    bodyLayoutContainer.removeView(pregnantQuestionView);
                    bodyLayoutContainer.removeView(pregnantRadioGroup);
                    bodyLayoutContainer.addView(pregnantQuestionView);
                    bodyLayoutContainer.addView(pregnantRadioGroup);
                    if (pregnantRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                        bodyLayoutContainer.removeView(lmpQuestionView);
                        bodyLayoutContainer.removeView(lmpDatePicker);
                        bodyLayoutContainer.addView(lmpQuestionView);
                        bodyLayoutContainer.addView(lmpDatePicker);
                    }
                }
            });
        }
        bodyLayoutContainer.addView(genderRadioGroup);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null,
                context, LabelConstants.ENTER + " " + LabelConstants.DATE_OF_BIRTH.toLowerCase()));
        if (dobDateListener == null) {
            List<ValidationTagBean> validationsForDob = new LinkedList<>();
            validationsForDob.add(new ValidationTagBean(FormulaConstants.VALIDATION_IS_FUTURE_DATE,
                    LabelConstants.DATE_OF_BIRTH + " " + LabelConstants.IS_NOT_CORRECT));
            dobDateListener = new DateChangeListenerStatic(context, validationsForDob);
            dobDatePicker = MyStaticComponents.getCustomDatePickerForStatic(this, dobDateListener, 1010);
        }
        bodyLayoutContainer.addView(dobDatePicker);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                UtilBean.getMyLabel(LabelConstants.ENTER + " " + LabelConstants.HEALTH_ID)));
        if (healthIdEditText == null) {
            healthIdEditText = MyStaticComponents.getEditText(context, LabelConstants.HEALTH_ID,
                    1007, 11, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        }
        bodyLayoutContainer.addView(healthIdEditText);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                UtilBean.getMyLabel(LabelConstants.ENTER + " " + LabelConstants.PHONE_NUMBER)));
        if (phoneEditText == null) {
            phoneEditText = MyStaticComponents.getEditText(context, LabelConstants.PHONE_NUMBER, 1009, 10, InputType.TYPE_CLASS_NUMBER);
        }
        bodyLayoutContainer.addView(phoneEditText);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                UtilBean.getMyLabel(LabelConstants.ENTER + " " + LabelConstants.BANK_ACCOUNT_NUMBER)));
        if (bankAccountEditText == null) {
            bankAccountEditText = MyStaticComponents.getEditText(context, LabelConstants.BANK_ACCOUNT_NUMBER, 1012, 16, InputType.TYPE_CLASS_NUMBER);
        }
        bodyLayoutContainer.addView(bankAccountEditText);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                UtilBean.getMyLabel(LabelConstants.ENTER + " " + LabelConstants.IFSC_CODE)));
        if (ifscEditText == null) {
            ifscEditText = MyStaticComponents.getEditText(context, LabelConstants.IFSC_CODE, 1013, 11,
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        }
        bodyLayoutContainer.addView(ifscEditText);

        if (pregnantQuestionView == null) {
            pregnantQuestionView = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.CONFORMATION_TO_PREGNANCY_OF_BENEFICIARY);
        }
        if (pregnantRadioGroup == null) {
            pregnantRadioGroup = getRadioGroupYesNo();
            pregnantRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == RADIO_BUTTON_ID_YES) {
                    bodyLayoutContainer.removeView(lmpQuestionView);
                    bodyLayoutContainer.removeView(lmpDatePicker);
                    bodyLayoutContainer.addView(lmpQuestionView);
                    bodyLayoutContainer.addView(lmpDatePicker);
                }
                if (checkedId == RADIO_BUTTON_ID_NO) {
                    bodyLayoutContainer.removeView(lmpQuestionView);
                    bodyLayoutContainer.removeView(lmpDatePicker);
                }
            });
        }

        if (lmpQuestionView == null) {
            lmpQuestionView = MyStaticComponents.generateQuestionView(null, null, context,
                    UtilBean.getMyLabel(LabelConstants.ENTER + " " + LabelConstants.LMP_DATE));
        }
        if (lmpDateListener == null) {
            List<ValidationTagBean> validationsForLmp = new LinkedList<>();
            validationsForLmp.add(new ValidationTagBean(FormulaConstants.VALIDATION_IS_FUTURE_DATE, LabelConstants.LMP_DATE + " " + LabelConstants.IS_NOT_CORRECT));
            validationsForLmp.add(new ValidationTagBean("isDateIn-Sub-0-9-0", LabelConstants.LMP_DATE + " " + LabelConstants.IS_NOT_CORRECT));
            validationsForLmp.add(new ValidationTagBean("isDateOut-Sub-0-0-28", LabelConstants.LMP_DATE_MUST_BE_WITHIN_28_DAYS_TO_9_MONTHS_OF_A_DAY));
            lmpDateListener = new DateChangeListenerStatic(context, validationsForLmp);
            lmpDatePicker = MyStaticComponents.getCustomDatePickerForStatic(this, lmpDateListener, 1011);
        }

        setMemberDetails();
    }

    private void setMemberDetails() {
        int count = genderRadioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            RadioButton radioButton = (RadioButton) genderRadioGroup.getChildAt(i);
            radioButton.setEnabled(true);
        }

        dobDatePicker.setClickable(true);

        if (genderRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
            bodyLayoutContainer.removeView(pregnantQuestionView);
            bodyLayoutContainer.removeView(pregnantRadioGroup);
            bodyLayoutContainer.addView(pregnantQuestionView);
            bodyLayoutContainer.addView(pregnantRadioGroup);

            if (pregnantRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                bodyLayoutContainer.removeView(lmpQuestionView);
                bodyLayoutContainer.removeView(lmpDatePicker);
                bodyLayoutContainer.addView(lmpQuestionView);
                bodyLayoutContainer.addView(lmpDatePicker);
            }
        }
    }

    private Boolean checkValidationForMemberQuestionScreen() {

        String phoneNumber = null;
        if (phoneEditText.getEditText() != null) {
            phoneNumber = phoneEditText.getEditText().getText().toString().trim();
        }
        ValidationTagBean validationTagBean = new ValidationTagBean(FormulaConstants.VALIDATION_CHECK_MOBILE_NUMBER, null);
        List<ValidationTagBean> validationTagBeans = new ArrayList<>();
        validationTagBeans.add(validationTagBean);
        String validation = DynamicUtils.checkValidation(phoneNumber, 0, validationTagBeans);

        if (firstNameEditText.getEditText() != null
                && firstNameEditText.getEditText().getText().toString().trim().length() == 0) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.FIRST_NAME_REQUIRED_ALERT));
            return false;
        }

        if (lastNameEditText.getEditText() != null
                && lastNameEditText.getEditText().getText().toString().trim().length() == 0) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.LAST_NAME_REQUIRED_ALERT));
            return false;
        }

        if (genderRadioGroup.getCheckedRadioButtonId() == -1) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.GENDER_REQUIRED_ALERT));
            return false;
        }

        if (dobDateListener.getDateSet() == null) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.DOB_REQUIRED_ALERT));
            return false;
        }

        if (bankAccountEditText.getEditText() != null
                && bankAccountEditText.getEditText().getText().length() != 0
                && !Pattern.compile("^\\d{16}$").matcher(bankAccountEditText.getEditText().getText().toString().trim()).matches()) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.BANK_ACCOUNT_NOT_VALID));
            return false;
        }

        if (healthIdEditText.getEditText() != null
                && healthIdEditText.getEditText().getText().toString().trim().length() > 0
                && !healthIdEditText.getEditText().getText().toString().trim().startsWith("A")) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.HEALTH_ID_MUST_START_WITH_A));
            return false;
        }

        if (healthIdEditText.getEditText() != null
                && healthIdEditText.getEditText().getText().toString().trim().length() > 0
                && (healthIdEditText.getEditText().getText().toString().trim().length() < 10
                || healthIdEditText.getEditText().getText().toString().trim().length() > 11)) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.HEALTH_ID_DIGIT_VALIDATION));
            return false;
        }

        if (validation != null) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel(validation));
            return false;
        }

        if (ifscEditText.getEditText() != null
                && ifscEditText.getEditText().getText().toString().trim().length() > 0
                && ifscEditText.getEditText().getText().toString().trim().length() < 11) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.IFSC_CODE + " " + LabelConstants.IS_NOT_CORRECT));
            return false;
        }

        if (genderRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.YEAR, -10);
            if (dobDateListener.getDateSet().before(instance.getTime())) {
                if (pregnantRadioGroup.getCheckedRadioButtonId() == -1) {
                    SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.CONFORMATION_TO_PREGNANCY_OF_BENEFICIARY));
                    return false;
                }

                if (pregnantRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES && lmpDateListener.getDateSet() == null) {
                    SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.LMP_DATE_REQUIRED_ALERT));
                    return false;
                }
            } else {
                if (pregnantRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                    SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.GIRL_CAN_NOT_BE_MARKED_AS_PREGNANT_ALERT));
                    return false;
                }
            }
        }

        return true;
    }

    private void setFamilyQuestionScreen() {
        bodyLayoutContainer.removeAllViews();
        screenName = FAMILY_QUESTION_SCREEN;

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.CONFORMATION_TO_BENEFICIARY_STAYING_WITH_FAMILY_IN_VILLAGE));
        if (stayingWithFamilyRadioGroup == null) {
            stayingWithFamilyRadioGroup = getRadioGroupYesNo();
        }
        bodyLayoutContainer.addView(stayingWithFamilyRadioGroup);
    }

    private void setFamilySelectionScreen() {
        bodyLayoutContainer.removeAllViews();
        screenName = FAMILY_SELECTION_SCREEN;

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.SEARCH_AND_SELECT_FAMILY_FOR_BENEFICIARY));
        addSearchTextBoxForFamily();
        bodyLayoutContainer.addView(searchFamilyEditText);
        if (searchedFamily != null && !searchedFamily.isEmpty()) {
            bodyLayoutContainer.addView(listTitleView);
            bodyLayoutContainer.addView(listView);
        }
    }

    private void addSearchTextBoxForFamily() {
        if (searchFamilyEditText == null) {
            searchFamilyEditText = MyStaticComponents.getEditText(this, LabelConstants.SEARCH_FAMILY, 1, RESULT_OK, 1);
        }
        if (searchFamilyEditText.getEditText() != null) {
            searchFamilyEditText.getEditText().addTextChangedListener(new TextWatcher() {
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
                                            showProcessDialog();
                                            bodyLayoutContainer.removeView(listView);
                                            retrieveFamilyListFromDB(s.toString());
                                            searchFamilyEditText.setFocusable(true);
                                        });
                                    } else if (s != null && s.length() == 0) {
                                        runOnUiThread(() -> {
                                            if (listView != null) {
                                                searchedFamily.clear();
                                                bodyLayoutContainer.removeView(listView);
                                            }
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
    public void retrieveFamilyListFromDB(String search) {
        searchedFamily = migrationService.retrieveFamilyListBySearchForMigration(search);
        runOnUiThread(() -> {
            bodyLayoutContainer.removeView(listView);
            bodyLayoutContainer.removeView(noFamilyTextView);
            bodyLayoutContainer.removeView(listTitleView);

            List<String> familyIds = new ArrayList<>();
            String rbText;
            List<String> list = new ArrayList<>();
            if (searchedFamily != null && !searchedFamily.isEmpty()) {
                listTitleView = MyStaticComponents.getListTitleView(context, LabelConstants.SELECT_FROM_LIST.toLowerCase());
                bodyLayoutContainer.addView(listTitleView);
                for (FamilyDataBean familyDataBean : searchedFamily) {
                    familyIds.add(familyDataBean.getFamilyId());
                }
                Map<String, MemberDataBean> headMembersMapWithFamilyIdAsKey = fhsService.retrieveHeadMemberDataBeansByFamilyId(familyIds);

                for (FamilyDataBean familyDataBean : searchedFamily) {
                    MemberDataBean headMember = headMembersMapWithFamilyIdAsKey.get(familyDataBean.getFamilyId());
                    if (headMember != null) {
                        familyDataBean.setHeadMemberName(headMember.getFirstName() + " " + headMember.getMiddleName() + " " + headMember.getGrandfatherName() + " " + headMember.getLastName());
                        familyDataBean.setHeadMemberName(familyDataBean.getHeadMemberName().replace(LabelConstants.NULL, ""));
                        rbText = familyDataBean.getFamilyId() + " - " + headMember.getFirstName() + " " + headMember.getMiddleName() + " " + headMember.getGrandfatherName() + " " + headMember.getLastName();
                        rbText = rbText.replace(" " + LabelConstants.NULL, "");
                        rbText = rbText.replace(LabelConstants.NULL + " ", "");
                    } else {
                        rbText = familyDataBean.getFamilyId() + " - " + UtilBean.getMyLabel(LabelConstants.HEAD_NAME_NOT_AVAILABLE);
                    }
                    list.add(rbText);
                }

                AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedFamilyindex = position;
                listView = MyStaticComponents.getListView(context, list, onItemClickListener, -1);
                bodyLayoutContainer.addView(listView);
            } else {
                noFamilyTextView = MyStaticComponents.generateInstructionView(context, LabelConstants.NO_FAMILIES_FOUND);
                bodyLayoutContainer.addView(noFamilyTextView);
            }
            hideProcessDialog();
        });
    }

    private void setVillageSelectionScreen() {
        bodyLayoutContainer.removeAllViews();
        screenName = VILLAGE_SELECTION_SCREEN;
        addVillageSelectionSpinner();
    }

    private void addVillageSelectionSpinner() {
        if (villageTextView == null) {
            villageTextView = MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel(LabelConstants.SELECT_VILLAGE + " " + LabelConstants.FOR_BENEFICIARY));
        }
        bodyLayoutContainer.addView(villageTextView);

        if (villageSpinner == null) {
            villageList = fhsService.getDistinctLocationsAssignedToUser();
            String[] arrayOfOptions = new String[villageList.size() + 1];
            arrayOfOptions[0] = GlobalTypes.SELECT;
            int i = 1;
            for (LocationBean locationBean : villageList) {
                arrayOfOptions[i] = locationBean.getName();
                i++;
            }

            villageSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 2);
            villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        bodyLayoutContainer.removeView(ashaAreaTextView);
                        bodyLayoutContainer.removeView(ashaAreaSpinner);
                    } else {
                        bodyLayoutContainer.removeView(ashaAreaTextView);
                        bodyLayoutContainer.removeView(ashaAreaSpinner);
                        ashaAreaList = fhsService.retrieveAshaAreaAssignedToUser(villageList.get(position - 1).getActualID());
                        addAshaAreaSelectionSpinner();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //do something
                }
            });

            selectedAshaAreaIndex = 0;
        }
        bodyLayoutContainer.addView(villageSpinner);
        if (villageSpinner.getSelectedItemPosition() != 0) {
            addAshaAreaSelectionSpinner();
        }
    }

    private void addAshaAreaSelectionSpinner() {
        if (ashaAreaTextView == null) {
            ashaAreaTextView = MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel(LabelConstants.SELECT_ASHA_AREA + " " + LabelConstants.FOR_BENEFICIARY));
        }
        bodyLayoutContainer.addView(ashaAreaTextView);

        String[] arrayOfOptions = new String[ashaAreaList.size() + 1];
        arrayOfOptions[0] = GlobalTypes.SELECT;
        int i = 1;
        for (LocationBean locationBean : ashaAreaList) {
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }
        ashaAreaSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, selectedAshaAreaIndex, 3);
        ashaAreaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAshaAreaIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
        bodyLayoutContainer.addView(ashaAreaSpinner);
    }

    private void setFinalScreen() {
        screenName = FINAL_SCREEN;
        bodyLayoutContainer.removeAllViews();
        nextButton.setText(GlobalTypes.EVENT_SUBMIT);

        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context,
                UtilBean.getMyLabel(LabelConstants.FORM_ENTRY_COMPLETED)));
        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context,
                UtilBean.getMyLabel(LabelConstants.FORM_ENTRY_COMPLETED_SUCCESSFULLY)));
    }

    private void finishActivity() {
        View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                myAlertDialog.dismiss();
                saveMigrationIn();
                finish();
            } else {
                myAlertDialog.dismiss();
            }
        };

        myAlertDialog = new MyAlertDialog(context,
                LabelConstants.ON_MIGRATION_FORM_SUBMISSION_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        myAlertDialog.show();
    }

    private void saveMigrationIn() {
        MigrationInDataBean migration = new MigrationInDataBean();

        migration.setFirstname(Objects.requireNonNull(firstNameEditText.getEditText()).getText().toString().trim());
        if (middleNameEditText.getEditText() != null
                && middleNameEditText.getEditText().getText().toString().trim().length() > 0) {
            migration.setMiddleName(middleNameEditText.getEditText().getText().toString().trim());
        }
        migration.setLastName(Objects.requireNonNull(lastNameEditText.getEditText()).getText().toString().trim());
        if (genderRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
            migration.setGender(GlobalTypes.FEMALE);
            if (pregnantRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                migration.setPregnant(Boolean.TRUE);
                migration.setLmp(lmpDateListener.getDateSet().getTime());
            } else {
                migration.setPregnant(Boolean.FALSE);
            }
        } else {
            migration.setGender(GlobalTypes.MALE);
        }
        migration.setDob(dobDateListener.getDateSet().getTime());
        if (healthIdEditText.getEditText() != null
                && healthIdEditText.getEditText().getText().toString().trim().length() > 0) {
            migration.setHealthId(healthIdEditText.getEditText().getText().toString().trim().toUpperCase());
        }
        if (phoneEditText.getEditText() != null
                && phoneEditText.getEditText().getText().toString().trim().length() > 0) {
            migration.setPhoneNumber(phoneEditText.getEditText().getText().toString().trim());
        }
        if (bankAccountEditText.getEditText() != null
                && bankAccountEditText.getEditText().getText().toString().trim().length() > 0) {
            migration.setBankAccountNumber(bankAccountEditText.getEditText().getText().toString().trim());
        }
        if (ifscEditText.getEditText() != null
                && ifscEditText.getEditText().getText().toString().trim().length() > 0) {
            migration.setIfscCode(ifscEditText.getEditText().getText().toString().trim());
        }

        if (migratedFromGujaratRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
            migration.setOutOfState(false);
            migration.setNearestLocId(blocks.get(blockSpinner.getSelectedItemPosition() - 1).getActualID());
            migration.setVillageName(Objects.requireNonNull(villageNameEditText.getEditText()).getText().toString().trim());
            migration.setFhwOrAshaName(Objects.requireNonNull(fhwNameEditText.getEditText()).getText().toString().trim());
            if (fhwPhoneEditText.getEditText() != null) {
                migration.setFhwOrAshaPhone(fhwPhoneEditText.getEditText().getText().toString().trim());
            }
        } else {
            migration.setOutOfState(true);
        }

        if (stayingWithFamilyRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
            migration.setStayingWithFamily(true);
            migration.setFamilyId(Long.valueOf(searchedFamily.get(selectedFamilyindex).getId()));
        } else {
            migration.setStayingWithFamily(false);
            migration.setVillageId(Long.valueOf(villageList.get(villageSpinner.getSelectedItemPosition() - 1).getActualID()));
            migration.setAreaId(Long.valueOf(ashaAreaList.get(ashaAreaSpinner.getSelectedItemPosition() - 1).getActualID()));
        }

        migration.setReportedOn(new Date().getTime());

        migrationService.createMigrationIn(migration);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (screenName == null || screenName.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            switch (screenName) {
                case MEMBER_QUESTION_SCREEN:
                    onBackPressed();
                    break;

                case FIRST_QUESTION_SCREEN:
                    setMemberQuestionsScreen();
                    break;


                case LOCATION_QUESTION_SCREEN:
                    setFirstQuestionScreen();
                    break;

                case FAMILY_QUESTION_SCREEN:
                    if (migratedFromGujaratRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                        setLocationQuestionScreen();
                    } else {
                        setFirstQuestionScreen();
                    }
                    break;

                case FAMILY_SELECTION_SCREEN:
                case VILLAGE_SELECTION_SCREEN:
                    setFamilyQuestionScreen();
                    break;

                case FINAL_SCREEN:
                    nextButton.setText(GlobalTypes.EVENT_NEXT);
                    if (stayingWithFamilyRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                        setFamilySelectionScreen();
                    } else {
                        setVillageSelectionScreen();
                    }
                    break;

                default:
                    break;
            }
        }
        return true;
    }

}
