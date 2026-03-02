package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.LocationMasterServiceImpl;
import com.argusoft.sewa.android.app.core.impl.MigrationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.FamilyMigrationOutDataBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LocationMasterBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prateek on 8/9/19
 */
@EActivity
public class FamilyMigrationOutActivity extends MenuActivity implements View.OnClickListener {

    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    MigrationServiceImpl migrationService;
    @Bean
    LocationMasterServiceImpl locationMasterService;

    private static final String FAMILY_INFO_SCREEN = "familyInfoScreen";
    private static final String SEARCH_OPTION_SCREEN = "searchOptionScreen";
    private static final String HIERARCHY_SCREEN = "hierarchyScreen";
    private static final String VILLAGE_SEARCH_SCREEN = "villageSearchScreen";
    private static final String HIERARCHY_SEARCH_SCREEN = "hierarchySearchScreen";
    private static final String CONFIRMATION_SCREEN = "confirmationScreen";
    private static final String OTHER_INFO_SCREEN = "otherInfoScreen";
    private static final String FINAL_SCREEN = "finalScreen";
    private static final Integer RADIO_BUTTON_ID_YES = 1;
    private static final Integer RADIO_BUTTON_ID_NO = 2;
    private static final Integer RADIO_BUTTON_ID_NOT_KNOWN = 3;

    private LinearLayout bodyLayoutContainer;
    private String screenName;
    private TextView selectDistrictQues;
    private Spinner districtSpinner;
    private TextView selectBlockQues;
    private Spinner blockSpinner;
    private TextView selectPhcQues;
    private Spinner phcSpinner;
    private TextView selectSubCenterQues;
    private Spinner subCenterSpinner;
    private TextView selectVillageQues;
    private Spinner villageSpinner;
    private CheckBox outOfStateCheckBox;
    private TextView searchOptionTextView;
    private RadioGroup searchOptionRadioGroup;
    private RadioGroup splitRadioGroup;
    private TextInputLayout searchVillageEditText;
    private Button searchVillageButton;
    private List<LocationMasterBean> searchedVillageList = new ArrayList<>();
    private Map<Integer, String> hierarcyMapWithLevelAndName;
    private Map<String, Long> selectionMap = new HashMap<>();
    private LocationMasterBean selectedLocation = null;
    private TextInputLayout otherInfoEditText;
    private RadioGroup confirmationRadioGroup;
    private NotificationMobDataBean selectedNotification;
    private FamilyDataBean familyDataBean;
    private List<Long> memberIds = new ArrayList<>();
    private LinearLayout globalPanel;
    private ListView listView;
    private int selectedVillageIndex = -1;
    private MaterialTextView listTitleView;
    private MaterialTextView noVillageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String family = extras.getString("familyToMigrate", null);
            familyDataBean = new Gson().fromJson(family, FamilyDataBean.class);
            String notificationString = extras.getString(GlobalTypes.NOTIFICATION, null);
            if (notificationString != null) {
                selectedNotification = new Gson().fromJson(notificationString, NotificationMobDataBean.class);
            }
        }
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
            Intent myIntent = new Intent(context, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        setTitle(UtilBean.getTitleText(LabelConstants.FAMILY_MIGRATION_OUT_TITLE));
        setSubTitle(familyDataBean.getFamilyId());
    }

    private void initView() {
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(context, this);
        setContentView(globalPanel);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        setFamilyInfoScreen();
    }

    @Override
    public void onBackPressed() {
        View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                alertDialog.dismiss();
                setResult(RESULT_CANCELED);
                finish();
            } else {
                alertDialog.dismiss();
            }
        };

        alertDialog = new MyAlertDialog(context, false,
                LabelConstants.ON_MIGRATION_CLOSE_ALERT,
                myListener,
                DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityConstants.FAMILY_SPLIT_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                boolean isCurrentLocation = data.getBooleanExtra("isCurrentLocation", false);
                if (isCurrentLocation) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    String memberIdsString = data.getStringExtra("memberIds");
                    if (memberIdsString != null) {
                        memberIds = new Gson().fromJson(memberIdsString, new TypeToken<List<Long>>() {
                        }.getType());
                        setSearchOptionScreen(true);
                    } else {
                        memberIds = new ArrayList<>();
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                setFamilyInfoScreen();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screenName) {
                case FAMILY_INFO_SCREEN:
                    if (splitRadioGroup.getCheckedRadioButtonId() == -1) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_AN_OPTION));
                    } else if (splitRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                        setSearchOptionScreen(false);
                    } else if (splitRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
                        Intent intent = new Intent(context, FamilySplitActivity_.class);
                        intent.putExtra("familyToSplit", new Gson().toJson(familyDataBean));
                        if (selectedNotification != null) {
                            intent.putExtra(GlobalTypes.NOTIFICATION, new Gson().toJson(selectedNotification));
                        }
                        startActivityForResult(intent, ActivityConstants.FAMILY_SPLIT_ACTIVITY_REQUEST_CODE);
                    }
                    break;

                case SEARCH_OPTION_SCREEN:
                    if (outOfStateCheckBox != null && outOfStateCheckBox.isChecked()) {
                        selectedLocation = null;
                        setConfirmationScreen(false);
                    } else {
                        if (searchOptionRadioGroup.getCheckedRadioButtonId() != -1) {
                            if (searchOptionRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                                bodyLayoutContainer.removeAllViews();
                                screenName = HIERARCHY_SCREEN;
                                if (selectedLocation != null) {
                                    addSearchedVillageHierarchyScreen(selectedLocation);
                                } else {
                                    addRegionSpinner(false);
                                }
                            }
                            if (searchOptionRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
                                bodyLayoutContainer.removeAllViews();
                                setVillageSearchScreen();
                                EditText searchVillage = searchVillageEditText.getEditText();
                                if (searchVillage != null && searchVillage.getText().toString().trim().length() > 0) {
                                    retrieveSearchedVillageListFromDB(searchVillage.getText().toString().trim());
                                }
                            }
                            if (searchOptionRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NOT_KNOWN) {
                                selectedLocation = null;
                                bodyLayoutContainer.removeAllViews();
                                setConfirmationScreen(false);
                            }
                        } else {
                            SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_AN_OPTION));
                        }
                    }
                    break;

                case HIERARCHY_SCREEN:
                    Long sixthStepOfHierarchy = selectionMap.get("6");
                    if (sixthStepOfHierarchy != null) {
                        String locId = familyDataBean.getLocationId();
                        if (locId != null && locId.equals(sixthStepOfHierarchy.toString())) {
                            SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.MIGRATED_FROM_VILLAGE_SELECTED_AS_MIGRATION_IN_ALERT));
                        } else {
                            setOtherInfoScreen();
                        }
                    } else {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.VILLAGE_OR_ANGANVADI_SELECTION_REQUIRED_ALERT));
                    }
                    break;

                case VILLAGE_SEARCH_SCREEN:
                    if (searchedVillageList != null && !searchedVillageList.isEmpty()) {
                        if (selectedVillageIndex != -1) {
                            selectedLocation = searchedVillageList.get(selectedVillageIndex);
                            if (familyDataBean.getLocationId().equals(selectedLocation.getActualID().toString())) {
                                SewaUtil.generateToast(context, LabelConstants.MIGRATED_FROM_VILLAGE_SELECTED_AS_MIGRATION_IN_ALERT);
                                return;
                            }
                            bodyLayoutContainer.removeAllViews();
                            showProcessDialog();
                            screenName = HIERARCHY_SEARCH_SCREEN;
                            addSearchedVillageHierarchyScreen(selectedLocation);
                            hideProcessDialog();
                        } else {
                            SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.VILLAGE_OR_ANGANVADI_SELECTION_REQUIRED_ALERT_FOR_SEARCH));
                        }
                    } else {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.SEARCH_FOR_VILLAGE_OR_ANGANVADI_ALERT));
                    }
                    break;

                case HIERARCHY_SEARCH_SCREEN:
                    if (selectionMap.get("6") != null) {
                        setOtherInfoScreen();
                    } else {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.VILLAGE_OR_ANGANVADI_SELECTION_REQUIRED_ALERT));
                    }
                    break;

                case CONFIRMATION_SCREEN:
                    if (confirmationRadioGroup.getCheckedRadioButtonId() != -1) {
                        if (confirmationRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                            setOtherInfoScreen();
                        }
                        if (confirmationRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
                            setSearchOptionScreen(false);
                        }
                    } else {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_AN_OPTION));
                    }
                    break;

                case OTHER_INFO_SCREEN:
                    setFinalScreen();
                    break;

                case FINAL_SCREEN:
                    finishActivity();
                    break;

                default:
                    break;
            }
        }
    }

    private void setFamilyInfoScreen() {
        screenName = FAMILY_INFO_SCREEN;
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateTitleView(this, LabelConstants.FAMILY_DETAILS));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.FAMILY_ID));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this, familyDataBean.getFamilyId()));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.MEMBERS_INFO));
        bodyLayoutContainer.addView(UtilBean.getMembersListForDisplay(this, familyDataBean));

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                LabelConstants.DO_YOU_WANT_TO_MIGRATE_OR_SPLIT_THIS_FAMILY));
        if (splitRadioGroup == null) {
            splitRadioGroup = new RadioGroup(context);
            splitRadioGroup.addView(
                    MyStaticComponents.getRadioButton(context,
                            UtilBean.getMyLabel(LabelConstants.MIGRATE_THE_FAMILY),
                            RADIO_BUTTON_ID_YES));
            splitRadioGroup.addView(
                    MyStaticComponents.getRadioButton(context,
                            UtilBean.getMyLabel(LabelConstants.SPLIT_THE_FAMILY),
                            RADIO_BUTTON_ID_NO));
        }
        bodyLayoutContainer.addView(splitRadioGroup);
    }


    private void setSearchOptionScreen(boolean isSplitFamily) {
        screenName = SEARCH_OPTION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                LabelConstants.CHECK_IF_THE_FAMILY_HAS_BEEN_MIGRATED_TO_OUT_OF_STATE));

        if (searchOptionTextView == null) {
            searchOptionTextView = MyStaticComponents.generateQuestionView(null, null, context,
                    LabelConstants.CHOOSE_OPTION_TO_SEARCH);
        }

        if (searchOptionRadioGroup == null) {

            HashMap<Integer, String> stringMap = new HashMap<>();
            stringMap.put(RADIO_BUTTON_ID_YES, LabelConstants.SELECT_LOCATION);
            stringMap.put(RADIO_BUTTON_ID_NO, LabelConstants.TYPE_VILLAGE_NAME);

            if (!isSplitFamily) {
                stringMap.put(RADIO_BUTTON_ID_NOT_KNOWN, LabelConstants.LOCATION_NOT_KNOWN);
            }
            searchOptionRadioGroup = MyStaticComponents.getRadioGroup(this, stringMap, false);
        }

        if (outOfStateCheckBox == null) {
            outOfStateCheckBox = MyStaticComponents.getCheckBox(context, LabelConstants.OUT_OF_STATE, 255, false);
            outOfStateCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    bodyLayoutContainer.removeView(searchOptionTextView);
                    bodyLayoutContainer.removeView(searchOptionRadioGroup);
                } else {
                    bodyLayoutContainer.addView(searchOptionTextView);
                    bodyLayoutContainer.addView(searchOptionRadioGroup);
                }
            });
        }
        bodyLayoutContainer.addView(outOfStateCheckBox);

        if (!outOfStateCheckBox.isChecked()) {
            bodyLayoutContainer.addView(searchOptionTextView);
            bodyLayoutContainer.addView(searchOptionRadioGroup);
        }
    }


    private void setVillageSearchScreen() {
        bodyLayoutContainer.removeAllViews();
        screenName = VILLAGE_SEARCH_SCREEN;

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                LabelConstants.VILLAGE_OR_ANGANVADI_TO_BE_ENTERED_ALERT));

        if (searchVillageEditText == null) {
            searchVillageEditText = MyStaticComponents.getEditText(context, LabelConstants.VILLAGE_OR_ANGANVADI, 1001, 50, -1);
        }
        bodyLayoutContainer.addView(searchVillageEditText);

        if (searchVillageButton == null) {
            searchVillageButton = MyStaticComponents.getButton(context, UtilBean.getMyLabel(LabelConstants.SEARCH),
                    104, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            searchVillageButton.setOnClickListener(v -> {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                bodyLayoutContainer.removeView(listView);
                bodyLayoutContainer.removeView(listTitleView);
                selectedVillageIndex = -1;
                EditText searchVillage = searchVillageEditText.getEditText();
                if (searchVillage != null && searchVillage.getText().toString().trim().length() > 0) {
                    showProcessDialog();
                    retrieveSearchedVillageListFromDB(searchVillageEditText.getEditText().getText().toString().trim());
                } else {
                    searchedVillageList.clear();
                    selectedLocation = null;
                    SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.VILLAGE_OR_ANGANVADI_SELECTION_REQUIRED_ALERT_FOR_SEARCH));
                }
            });
        }
        bodyLayoutContainer.addView(searchVillageButton);
    }

    @Background
    public void retrieveSearchedVillageListFromDB(CharSequence s) {
        searchedVillageList = migrationService.retrieveLocationMasterBeansBySearch(s, 6);
        addSearchedVillageList();
    }

    @UiThread
    public void addSearchedVillageList() {
        bodyLayoutContainer.removeView(listView);
        bodyLayoutContainer.removeView(listTitleView);
        bodyLayoutContainer.removeView(noVillageTextView);

        listTitleView = MyStaticComponents.getListTitleView(context, LabelConstants.SELECT_FROM_LIST);
        bodyLayoutContainer.addView(listTitleView);

        if (searchedVillageList != null && !searchedVillageList.isEmpty()) {
            String rbText;
            List<String> list = new ArrayList<>();
            for (LocationMasterBean locationMasterBean : searchedVillageList) {
                Map<Integer, String> hierarchyOfVillage = migrationService.retrieveHierarchyOfVillage(locationMasterBean);
                rbText = String.format("%s > %s > %s > %s > %s > %s",
                        hierarchyOfVillage.get(1),
                        hierarchyOfVillage.get(2),
                        hierarchyOfVillage.get(3),
                        hierarchyOfVillage.get(4),
                        hierarchyOfVillage.get(5),
                        hierarchyOfVillage.get(6));
                list.add(rbText);
            }
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedVillageIndex = position;
            listView = MyStaticComponents.getListView(context, list, onItemClickListener, -1);
            bodyLayoutContainer.addView(listView);
        } else {
            noVillageTextView = MyStaticComponents.generateInstructionView(context, LabelConstants.NO_VILLAGE_OR_ANGANVADI_FOUND_WITH_THE_GIVEN_NAME);
            bodyLayoutContainer.addView(noVillageTextView);
        }
        hideProcessDialog();
    }

    private void addSearchedVillageHierarchyScreen(LocationMasterBean locationMasterBean) {
        hierarcyMapWithLevelAndName = migrationService.retrieveHierarchyOfVillage(locationMasterBean);
        addRegionSpinner(true);
    }

    private void setConfirmationScreen(boolean isBack) {
        screenName = CONFIRMATION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        if (outOfStateCheckBox.isChecked()) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                    LabelConstants.SURE_THIS_FAMILY_HAS_MIGRATED_OUT_OF_STATE));
        } else if (searchOptionRadioGroup != null && searchOptionRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NOT_KNOWN) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                    LabelConstants.SURE_YOU_DON_T_KNOW_WHERE_THIS_FAMILY_HAS_BEEN_MIGRATED));
        }

        if (confirmationRadioGroup == null) {
            HashMap<Integer, String> stringMap = new HashMap<>();
            stringMap.put(RADIO_BUTTON_ID_YES, LabelConstants.YES);
            stringMap.put(RADIO_BUTTON_ID_NO, LabelConstants.NO);
            confirmationRadioGroup = MyStaticComponents.getRadioGroup(this, stringMap, true);
        }
        if (!isBack) {
            confirmationRadioGroup.clearCheck();
        }
        bodyLayoutContainer.addView(confirmationRadioGroup);
    }

    private void setOtherInfoScreen() {
        screenName = OTHER_INFO_SCREEN;
        bodyLayoutContainer.removeAllViews();

        if (selectedLocation != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.LOCATION_MIGRATED_TO));

            Map<Integer, String> hierarchyOfVillage = migrationService.retrieveHierarchyOfVillage(selectedLocation);
            String villageHierarchy = String.format("%s > %s > %s > %s > %s > %s",
                    hierarchyOfVillage.get(1),
                    hierarchyOfVillage.get(2),
                    hierarchyOfVillage.get(3),
                    hierarchyOfVillage.get(4),
                    hierarchyOfVillage.get(5),
                    hierarchyOfVillage.get(6));

            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, villageHierarchy));

            String fhwDetailString = selectedLocation.getFhwDetailString();
            if (fhwDetailString != null) {
                Type type = new TypeToken<List<Map<String, String>>>() {
                }.getType();
                List<Map<String, String>> fhwDetailMapList = new Gson().fromJson(fhwDetailString, type);
                Map<String, String> fhwDetailMap = fhwDetailMapList.get(0);

                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.FHW_NAME));

                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, fhwDetailMap.get("name")));

                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.FHW_MOBILE_NUMBER));

                String mobNumber = fhwDetailMap.get("mobileNumber");
                if (mobNumber != null && !mobNumber.trim().isEmpty()) {
                    bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, fhwDetailMap.get("mobileNumber")));
                } else {
                    bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, LabelConstants.NOT_AVAILABLE));
                }
            }
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.OTHER_INFORMATION));
        if (otherInfoEditText == null) {
            otherInfoEditText = MyStaticComponents.getEditText(context, LabelConstants.OTHER_INFORMATION, 1000, 500, -1);
        }
        bodyLayoutContainer.addView(otherInfoEditText);
    }

    private void setFinalScreen() {
        screenName = FINAL_SCREEN;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context,
                UtilBean.getMyLabel(LabelConstants.FORM_ENTRY_COMPLETED)));
        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context,
                UtilBean.getMyLabel(LabelConstants.FORM_ENTRY_COMPLETED_SUCCESSFULLY)));
    }

    private void finishActivity() {
        View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                alertDialog.dismiss();
                saveFamilyMigrationOut();
                setResult(RESULT_OK);
                finish();
            } else {
                alertDialog.dismiss();
            }
        };

        alertDialog = new MyAlertDialog(context,
                LabelConstants.ON_MIGRATION_FORM_SUBMISSION_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    private void saveFamilyMigrationOut() {
        FamilyMigrationOutDataBean migration = new FamilyMigrationOutDataBean();
        migration.setFamilyId(Long.valueOf(familyDataBean.getId()));
        migration.setFromLocationId(Long.valueOf(familyDataBean.getLocationId()));

        if (memberIds != null && !memberIds.isEmpty()) {
            migration.setSplit(true);
            migration.setMemberIds(memberIds);
        }

        if (outOfStateCheckBox.isChecked()) {
            migration.setOutOfState(true);
        } else {
            migration.setOutOfState(false);

            if (searchOptionRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NOT_KNOWN) {
                migration.setLocationKnown(false);
            } else {
                migration.setLocationKnown(true);
                migration.setToLocationId(selectionMap.get("6"));
            }
        }

        EditText otherInfo = otherInfoEditText.getEditText();
        if (otherInfo != null && otherInfo.getText().toString().trim().length() > 0) {
            migration.setOtherInfo(otherInfo.getText().toString().trim());
        }
        migration.setReportedOn(new Date().getTime());
        migrationService.createFamilyMigrationOut(migration, familyDataBean, selectedNotification);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////                                       Methods to show Hierarchy                                       //////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void addRegionSpinner(final boolean preSelected) {
        final List<LocationMasterBean> locationMasterBeans =
                locationMasterService.retrieveLocationMasterBeansByLevelAndParent(1, 2L);
        String[] arrayOfOptions = new String[locationMasterBeans.size() + 1];
        int defaultIndex = 0;
        if (!locationMasterBeans.isEmpty()) {
            int i = 1;
            arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
            for (LocationMasterBean locationMasterBean : locationMasterBeans) {
                arrayOfOptions[i] = locationMasterBean.getName();
                if (preSelected && locationMasterBean.getName().equals(hierarcyMapWithLevelAndName.get(1))) {
                    defaultIndex = i;
                }
                i++;
            }
        } else {
            arrayOfOptions[0] = UtilBean.getMyLabel(LabelConstants.NO_LOCATION_AVAILABLE);
        }

        Spinner regionSpinner = MyStaticComponents.getSpinner(context, arrayOfOptions, defaultIndex, 1);
        regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectionMap.remove("2");
                selectionMap.remove("3");
                selectionMap.remove("4");
                selectionMap.remove("5");
                selectionMap.remove("6");
                bodyLayoutContainer.removeView(selectDistrictQues);
                bodyLayoutContainer.removeView(districtSpinner);
                bodyLayoutContainer.removeView(selectBlockQues);
                bodyLayoutContainer.removeView(blockSpinner);
                bodyLayoutContainer.removeView(selectPhcQues);
                bodyLayoutContainer.removeView(phcSpinner);
                bodyLayoutContainer.removeView(selectSubCenterQues);
                bodyLayoutContainer.removeView(subCenterSpinner);
                bodyLayoutContainer.removeView(selectVillageQues);
                bodyLayoutContainer.removeView(villageSpinner);
                if (position != 0) {
                    LocationMasterBean selectedLocationMaster = locationMasterBeans.get(position - 1);
                    selectionMap.put("1", selectedLocationMaster.getActualID());
                    addDistrictSpinner(selectedLocationMaster.getActualID(), preSelected);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do something
            }
        });

        TextView selectRegionQues = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.SELECT_REGION);
        bodyLayoutContainer.addView(selectRegionQues);
        bodyLayoutContainer.addView(regionSpinner);
    }

    private void addDistrictSpinner(Long parent, final boolean isSearch) {
        final List<LocationMasterBean> locationMasterBeans = locationMasterService.retrieveLocationMasterBeansByLevelAndParent(2, parent);
        String[] arrayOfOptions = new String[locationMasterBeans.size() + 1];
        int defaultIndex = 0;
        if (!locationMasterBeans.isEmpty()) {
            int i = 1;
            arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
            for (LocationMasterBean locationMasterBean : locationMasterBeans) {
                arrayOfOptions[i] = locationMasterBean.getName();
                if (isSearch && locationMasterBean.getName().equals(hierarcyMapWithLevelAndName.get(2))) {
                    defaultIndex = i;
                }
                i++;
            }
        } else {
            arrayOfOptions[0] = UtilBean.getMyLabel(LabelConstants.NO_LOCATION_AVAILABLE);
        }

        districtSpinner = MyStaticComponents.getSpinner(context, arrayOfOptions, defaultIndex, 2);
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectionMap.remove("3");
                selectionMap.remove("4");
                selectionMap.remove("5");
                selectionMap.remove("6");
                bodyLayoutContainer.removeView(selectBlockQues);
                bodyLayoutContainer.removeView(blockSpinner);
                bodyLayoutContainer.removeView(selectPhcQues);
                bodyLayoutContainer.removeView(phcSpinner);
                bodyLayoutContainer.removeView(selectSubCenterQues);
                bodyLayoutContainer.removeView(subCenterSpinner);
                bodyLayoutContainer.removeView(selectVillageQues);
                bodyLayoutContainer.removeView(villageSpinner);
                if (position != 0) {
                    LocationMasterBean selectedLocationMaster = locationMasterBeans.get(position - 1);
                    selectionMap.put("2", selectedLocationMaster.getActualID());
                    addBlockSpinner(selectedLocationMaster.getActualID(), isSearch);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do something
            }
        });

        selectDistrictQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT_DISTRICT_OR_CORPORATION));
        bodyLayoutContainer.addView(selectDistrictQues);
        bodyLayoutContainer.addView(districtSpinner);
    }

    private void addBlockSpinner(Long parent, final boolean isSearch) {
        final List<LocationMasterBean> locationMasterBeans = locationMasterService.retrieveLocationMasterBeansByLevelAndParent(3, parent);
        List<String> locationTypes = new ArrayList<>();
        String[] arrayOfOptions = new String[locationMasterBeans.size() + 1];
        int defaultIndex = 0;
        if (!locationMasterBeans.isEmpty()) {
            int i = 1;
            arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
            for (LocationMasterBean locationMasterBean : locationMasterBeans) {
                arrayOfOptions[i] = locationMasterBean.getName();
                if (!locationTypes.contains(locationMasterBean.getType())) {
                    locationTypes.add(locationMasterBean.getType());
                }
                if (isSearch && locationMasterBean.getName().equals(hierarcyMapWithLevelAndName.get(3))) {
                    defaultIndex = i;
                }
                i++;
            }
        } else {
            arrayOfOptions[0] = UtilBean.getMyLabel(LabelConstants.NO_LOCATION_AVAILABLE);
        }

        blockSpinner = MyStaticComponents.getSpinner(context, arrayOfOptions, defaultIndex, 3);
        blockSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectionMap.remove("4");
                selectionMap.remove("5");
                selectionMap.remove("6");
                bodyLayoutContainer.removeView(selectPhcQues);
                bodyLayoutContainer.removeView(phcSpinner);
                bodyLayoutContainer.removeView(selectSubCenterQues);
                bodyLayoutContainer.removeView(subCenterSpinner);
                bodyLayoutContainer.removeView(selectVillageQues);
                bodyLayoutContainer.removeView(villageSpinner);
                if (position != 0) {
                    LocationMasterBean selectedLocationMaster = locationMasterBeans.get(position - 1);
                    selectionMap.put("3", selectedLocationMaster.getActualID());
                    addPhcSpinner(selectedLocationMaster.getActualID(), isSearch);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do something
            }
        });

        if (locationTypes.size() > 1) {
            StringBuilder sb = new StringBuilder();
            int counter = 1;
            for (String locationType : locationTypes) {
                if (locationType.equals("B")) {
                    sb.append(LabelConstants.BLOCK);
                }
                if (locationType.equals("Z")) {
                    sb.append(LabelConstants.ZONE);
                }
                if (counter != locationTypes.size()) {
                    sb.append("/");
                }
                counter++;
            }
            selectBlockQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT + " " + sb));
        } else if (locationTypes.size() == 1) {
            if (locationTypes.get(0).equals("B")) {
                selectBlockQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT_BLOCK));
            }
            if (locationTypes.get(0).equals("Z")) {
                selectBlockQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT_ZONE));
            }
        } else {
            selectBlockQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT_BLOCK));
        }
        bodyLayoutContainer.addView(selectBlockQues);
        bodyLayoutContainer.addView(blockSpinner);
    }

    private void addPhcSpinner(Long parent, final boolean isSearch) {
        final List<LocationMasterBean> locationMasterBeans = locationMasterService.retrieveLocationMasterBeansByLevelAndParent(4, parent);
        List<String> locationTypes = new ArrayList<>();
        String[] arrayOfOptions = new String[locationMasterBeans.size() + 1];
        int defaultIndex = 0;
        if (!locationMasterBeans.isEmpty()) {
            int i = 1;
            arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
            for (LocationMasterBean locationMasterBean : locationMasterBeans) {
                arrayOfOptions[i] = locationMasterBean.getName();
                if (!locationTypes.contains(locationMasterBean.getType())) {
                    locationTypes.add(locationMasterBean.getType());
                }
                if (isSearch && locationMasterBean.getName().equals(hierarcyMapWithLevelAndName.get(4))) {
                    defaultIndex = i;
                }
                i++;
            }
        } else {
            arrayOfOptions[0] = UtilBean.getMyLabel(LabelConstants.NO_LOCATION_AVAILABLE);
        }

        phcSpinner = MyStaticComponents.getSpinner(context, arrayOfOptions, defaultIndex, 4);
        phcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectionMap.remove("5");
                selectionMap.remove("6");
                bodyLayoutContainer.removeView(selectSubCenterQues);
                bodyLayoutContainer.removeView(subCenterSpinner);
                bodyLayoutContainer.removeView(selectVillageQues);
                bodyLayoutContainer.removeView(villageSpinner);
                if (position != 0) {
                    LocationMasterBean selectedLocationMaster = locationMasterBeans.get(position - 1);
                    selectionMap.put("4", selectedLocationMaster.getActualID());
                    addSubCenterSpinner(selectedLocationMaster.getActualID(), isSearch);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do something
            }
        });

        if (locationTypes.size() > 1) {
            StringBuilder sb = new StringBuilder();
            int counter = 1;
            for (String locationType : locationTypes) {
                if (locationType.equals("U")) {
                    sb.append(LabelConstants.UHC);
                }
                if (locationType.equals("P")) {
                    sb.append(LabelConstants.PHC);
                }
                if (counter != locationTypes.size()) {
                    sb.append("/");
                }
                counter++;
            }
            selectPhcQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT + " " + sb));
        } else if (locationTypes.size() == 1) {
            if (locationTypes.get(0).equals("U")) {
                selectPhcQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT + " " + LabelConstants.UHC));
            }
            if (locationTypes.get(0).equals("P")) {
                selectPhcQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT + " " + LabelConstants.PHC));
            }
        } else {
            selectPhcQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT + " " + LabelConstants.PHC));
        }
        bodyLayoutContainer.addView(selectPhcQues);
        bodyLayoutContainer.addView(phcSpinner);
    }

    private void addSubCenterSpinner(Long parent, final boolean isSearch) {
        final List<LocationMasterBean> locationMasterBeans = locationMasterService.retrieveLocationMasterBeansByLevelAndParent(5, parent);
        List<String> locationTypes = new ArrayList<>();
        String[] arrayOfOptions = new String[locationMasterBeans.size() + 1];
        int defaultIndex = 0;
        if (!locationMasterBeans.isEmpty()) {
            int i = 1;
            arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
            for (LocationMasterBean locationMasterBean : locationMasterBeans) {
                arrayOfOptions[i] = locationMasterBean.getName();
                if (!locationTypes.contains(locationMasterBean.getType())) {
                    locationTypes.add(locationMasterBean.getType());
                }
                if (isSearch && locationMasterBean.getName().equals(hierarcyMapWithLevelAndName.get(5))) {
                    defaultIndex = i;
                }
                i++;
            }
        } else {
            arrayOfOptions[0] = UtilBean.getMyLabel(LabelConstants.NO_LOCATION_AVAILABLE);
        }

        subCenterSpinner = MyStaticComponents.getSpinner(context, arrayOfOptions, defaultIndex, 5);
        subCenterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectionMap.remove("6");
                bodyLayoutContainer.removeView(selectVillageQues);
                bodyLayoutContainer.removeView(villageSpinner);
                if (position != 0) {
                    LocationMasterBean selectedLocationMaster = locationMasterBeans.get(position - 1);
                    selectionMap.put("5", selectedLocationMaster.getActualID());
                    addVillageSpinner(selectedLocationMaster.getActualID(), isSearch);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do something
            }
        });

        if (locationTypes.size() > 1) {
            StringBuilder sb = new StringBuilder();
            int counter = 1;
            for (String locationType : locationTypes) {
                if (locationType.equals(LabelConstants.ANM)) {
                    sb.append(LabelConstants.ANM_AREA);
                }
                if (locationType.equals(LabelConstants.SC)) {
                    sb.append(LabelConstants.SUB_CENTER);
                }
                if (counter != locationTypes.size()) {
                    sb.append("/");
                }
                counter++;
            }
            selectSubCenterQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT + " " + sb));
        } else if (locationTypes.size() == 1) {
            if (locationTypes.get(0).equals(LabelConstants.ANM)) {
                selectSubCenterQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT + " " + LabelConstants.ANM_AREA));
            }
            if (locationTypes.get(0).equals(LabelConstants.SC)) {
                selectSubCenterQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT + " " + LabelConstants.SUB_CENTER));
            }
        } else {
            selectSubCenterQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT + " " + LabelConstants.SUB_CENTER));
        }
        bodyLayoutContainer.addView(selectSubCenterQues);
        bodyLayoutContainer.addView(subCenterSpinner);
    }

    private void addVillageSpinner(Long parent, boolean isSearch) {
        final List<LocationMasterBean> locationMasterBeans = locationMasterService.retrieveLocationMasterBeansByLevelAndParent(6, parent);
        List<String> locationTypes = new ArrayList<>();
        String[] arrayOfOptions = new String[locationMasterBeans.size() + 1];
        int defaultIndex = 0;
        if (!locationMasterBeans.isEmpty()) {
            int i = 1;
            arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
            for (LocationMasterBean locationMasterBean : locationMasterBeans) {
                arrayOfOptions[i] = locationMasterBean.getName();
                if (!locationTypes.contains(locationMasterBean.getType())) {
                    locationTypes.add(locationMasterBean.getType());
                }
                if (isSearch && locationMasterBean.getName().equals(hierarcyMapWithLevelAndName.get(6))) {
                    defaultIndex = i;
                }
                i++;
            }
        } else {
            arrayOfOptions[0] = UtilBean.getMyLabel(LabelConstants.NO_LOCATION_AVAILABLE);
        }

        villageSpinner = MyStaticComponents.getSpinner(context, arrayOfOptions, defaultIndex, 6);
        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    selectedLocation = locationMasterBeans.get(position - 1);
                    selectionMap.put("6", selectedLocation.getActualID());
                } else {
                    selectionMap.remove("6");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do something
            }
        });

        if (locationTypes.size() > 1) {
            StringBuilder sb = new StringBuilder();
            int counter = 1;
            for (String locationType : locationTypes) {
                if (locationType.equals(LabelConstants.UA)) {
                    sb.append(LabelConstants.URBAN_AREA);
                }
                if (locationType.equals("V")) {
                    sb.append(LabelConstants.VILLAGE);
                }
                if (locationType.equals(LabelConstants.ANG)) {
                    sb.append(LabelConstants.ANGANWADI_AREA);
                }
                if (counter != locationTypes.size()) {
                    sb.append("/");
                }
                counter++;
            }
            selectVillageQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT + " " + sb));
        } else if (locationTypes.size() == 1) {
            if (locationTypes.get(0).equals(LabelConstants.UA)) {
                selectVillageQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT + " " + LabelConstants.URBAN_AREA));
            }
            if (locationTypes.get(0).equals("V")) {
                selectVillageQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT_VILLAGE));
            }
            if (locationTypes.get(0).equals(LabelConstants.ANG)) {
                selectVillageQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT + " " + LabelConstants.ANGANWADI_AREA));
            }
        } else {
            selectVillageQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SELECT_VILLAGE));
        }
        bodyLayoutContainer.addView(selectVillageQues);
        bodyLayoutContainer.addView(villageSpinner);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (screenName == null || screenName.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            selectedVillageIndex = -1;
            switch (screenName) {
                case FAMILY_INFO_SCREEN:
                    setResult(RESULT_CANCELED);
                    finish();
                    break;

                case SEARCH_OPTION_SCREEN:
                    if (memberIds != null && !memberIds.isEmpty()) {
                        Intent intent = new Intent(context, FamilySplitActivity_.class);
                        intent.putExtra("familyToSplit", new Gson().toJson(familyDataBean));
                        intent.putExtra("memberIds", new Gson().toJson(memberIds));
                        if (selectedNotification != null) {
                            intent.putExtra(GlobalTypes.NOTIFICATION, new Gson().toJson(selectedNotification));
                        }
                        startActivityForResult(intent, ActivityConstants.FAMILY_SPLIT_ACTIVITY_REQUEST_CODE);
                    } else {
                        setFamilyInfoScreen();
                    }
                    break;

                case HIERARCHY_SCREEN:
                    bodyLayoutContainer.removeAllViews();
                    setSearchOptionScreen(false);
                    break;

                case HIERARCHY_SEARCH_SCREEN:
                    bodyLayoutContainer.removeAllViews();
                    showProcessDialog();
                    setVillageSearchScreen();
                    EditText searchVillage = searchVillageEditText.getEditText();
                    if (searchVillage != null && searchVillage.getText().toString().trim().length() > 0) {
                        retrieveSearchedVillageListFromDB(searchVillage.getText().toString().trim());
                    }
                    break;

                case VILLAGE_SEARCH_SCREEN:
                    bodyLayoutContainer.removeAllViews();
                    setSearchOptionScreen(false);
                    if (searchedVillageList != null && !searchedVillageList.isEmpty()) {
                        searchedVillageList.clear();
                    }
                    break;

                case CONFIRMATION_SCREEN:
                    setSearchOptionScreen(false);
                    break;

                case OTHER_INFO_SCREEN:
                    bodyLayoutContainer.removeAllViews();
                    if (outOfStateCheckBox != null && outOfStateCheckBox.isChecked()) {
                        setConfirmationScreen(true);
                    } else {
                        if (searchOptionRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO && selectedLocation != null) {
                            screenName = HIERARCHY_SEARCH_SCREEN;
                            addSearchedVillageHierarchyScreen(selectedLocation);
                            break;
                        }
                        if (searchOptionRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                            screenName = HIERARCHY_SCREEN;
                            if (selectedLocation != null) {
                                addSearchedVillageHierarchyScreen(selectedLocation);
                            } else {
                                addRegionSpinner(false);
                            }
                            break;
                        }
                        if (searchOptionRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NOT_KNOWN) {
                            setConfirmationScreen(true);
                            break;
                        }
                    }
                    break;

                case FINAL_SCREEN:
                    setOtherInfoScreen();
                    break;

                default:
                    onBackPressed();
            }
        }
        return true;
    }
}
