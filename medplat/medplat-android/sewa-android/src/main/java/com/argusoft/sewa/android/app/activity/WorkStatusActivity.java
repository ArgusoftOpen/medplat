package com.argusoft.sewa.android.app.activity;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyProcessDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.MyWorkLogAdapter;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.LocationMasterServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.WorkLogScreenDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.FhwServiceDetailBean;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.LocationMasterBean;
import com.argusoft.sewa.android.app.model.LoggerBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@EActivity
public class WorkStatusActivity extends MenuActivity implements View.OnClickListener {

    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    LocationMasterServiceImpl locationMasterService;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<LocationBean, Integer> locationBeanDao;


    private List<LocationMasterBean> locationBeans = new ArrayList<>();
    private Map<String, Long> mapOfStatistics = new LinkedHashMap<>();
    private Map<String, Long> mapOfExpectedValues = new LinkedHashMap<>();
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private Spinner spinner;
    private boolean isVillageSelectionScreen = Boolean.FALSE;
    private Integer locationId;
    private Integer pageCounter = 0;
    private LinearLayout globalPanel;

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
        setTitle(UtilBean.getTitleText(LabelConstants.WORK_STATUS_TITLE));
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

    private void setBodyDetail() {
        locationBeans = locationMasterService.retrieveLocationMastersAssignedToUser();
        if (locationBeans.size() == 1) {
            locationId = locationBeans.get(0).getActualID().intValue();
            isVillageSelectionScreen = Boolean.FALSE;
            pageCounter++;
            addTable(Long.valueOf(locationId));
        } else if (locationBeans.isEmpty()) {
            addDataNotSyncedMsg(bodyLayoutContainer, nextButton);
        } else {
            isVillageSelectionScreen = Boolean.TRUE;
            addVillageSelectionSpinner();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            if (isVillageSelectionScreen) {
                String selectedVillage = spinner.getSelectedItem().toString();
                if (spinner.getSelectedItemPosition() == 0) {
                    locationId = null;
                } else {
                    for (LocationMasterBean locationBean : locationBeans) {
                        if (selectedVillage.equals(locationBean.getName())) {
                            locationId = locationBean.getActualID().intValue();
                            break;
                        }
                    }
                }
                bodyLayoutContainer.removeAllViews();
                pageCounter++;
                if (locationId != null)
                    addTable(Long.valueOf(locationId));
                else
                    addTable(null);
                isVillageSelectionScreen = Boolean.FALSE;
            } else if (pageCounter == 4) {
                bodyLayoutContainer.removeAllViews();
                processDialog = new MyProcessDialog(this, GlobalTypes.MSG_PROCESSING);
                processDialog.show();
                pageCounter++;
                retrieveWorkLog();
                processDialog.dismiss();
                nextButton.setText(UtilBean.getMyLabel(GlobalTypes.MAIN_MENU));
            } else {
                if (pageCounter == 5) {
                    navigateToHomeScreen(false);
                    return;
                }
                bodyLayoutContainer.removeAllViews();
                pageCounter++;
                if (locationId != null) {
                    addTable(Long.valueOf(locationId));
                } else {
                    addTable(null);
                }

            }
        }
    }

    private void addTable(Long locationId) {
        LocationBean locationBean = new LocationBean();
        List<FhwServiceDetailBean> fhwServiceDetailBeans;
        if (locationId != null) {
            try {
                locationBean = locationBeanDao.queryBuilder().where().eq("actualID", Integer.valueOf(locationId.toString())).query().get(0);
                addVillageHeading(locationBean);
                addStatisticTypeHeading();
            } catch (SQLException e) {
                Log.e(getClass().getName(), null, e);
            }
            fhwServiceDetailBeans = fhsService.retrieveFhwServiceDetailBeansByVillageId(locationBean.getActualID());
        } else {
            addVillageHeading(null);
            addStatisticTypeHeading();
            fhwServiceDetailBeans = fhsService.retrieveFhwServiceDetailBeansByVillageId(null);
        }

        populateMapOfStatistics(fhwServiceDetailBeans);

        //Setting Up UI Table
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setPadding(10, 10, 10, 10);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        TableRow.LayoutParams layoutParamsForRow = new TableRow.LayoutParams(MATCH_PARENT);
        TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(0, WRAP_CONTENT, 2);
        TableRow.LayoutParams layoutParams1 = new TableRow.LayoutParams(0, MATCH_PARENT, 1);

        int i = 0;
        int countForStatistics = 0;

        TableRow row = new TableRow(this);
        row.setPadding(10, 15, 10, 15);
        if (SewaUtil.CURRENT_THEME == R.style.techo_training_app) {
            row.setBackgroundResource(R.drawable.table_row_selector);
        } else {
            row.setBackgroundResource(R.drawable.spinner_item_border);
        }
        row.setLayoutParams(layoutParamsForRow);
        MaterialTextView performanceItem = new MaterialTextView(this);
        MaterialTextView performanceValue = new MaterialTextView(this);
        MaterialTextView expectedValue = new MaterialTextView(this);
        performanceItem.setGravity(Gravity.CENTER);
        performanceValue.setGravity(Gravity.CENTER);
        expectedValue.setGravity(Gravity.CENTER);
        performanceItem.setText(UtilBean.getMyLabel(LabelConstants.PERFORMANCE_ITEM));
        performanceValue.setText(UtilBean.getMyLabel(LabelConstants.VALUE));
        expectedValue.setText(UtilBean.getMyLabel(LabelConstants.EXPECTED));
        performanceItem.setTypeface(Typeface.DEFAULT_BOLD);
        performanceValue.setTypeface(Typeface.DEFAULT_BOLD);
        expectedValue.setTypeface(Typeface.DEFAULT_BOLD);
        row.addView(performanceItem, layoutParams2);
        row.addView(performanceValue, layoutParams1);
        row.addView(expectedValue, layoutParams1);
        tableLayout.addView(row, i);
        i++;

        for (Map.Entry<String, Long> aStatistic : mapOfStatistics.entrySet()) {
            countForStatistics++;
            if ((pageCounter == 1 && (countForStatistics == 1 || countForStatistics > 7))
                    || (pageCounter == 2 && (countForStatistics < 8 || countForStatistics > 10))
                    || (pageCounter == 3 && (countForStatistics < 11 || countForStatistics > 14))
                    || (pageCounter == 4 && (countForStatistics < 15))) {
                continue;
            }

            row = new TableRow(this);
            row.setPadding(10, 15, 10, 15);
            if (SewaUtil.CURRENT_THEME == R.style.techo_training_app) {
                row.setBackgroundResource(R.drawable.table_row_selector);
            } else {
                row.setBackgroundResource(R.drawable.spinner_item_border);
            }
            row.setLayoutParams(layoutParamsForRow);
            performanceItem = new MaterialTextView(this);
            performanceValue = new MaterialTextView(this);
            expectedValue = new MaterialTextView(this);
            performanceItem.setGravity(Gravity.CENTER);
            performanceValue.setGravity(Gravity.CENTER);
            expectedValue.setGravity(Gravity.CENTER);
            if (i == 0) {
                performanceItem.setText(UtilBean.getMyLabel(LabelConstants.PERFORMANCE_ITEM));
                performanceValue.setText(UtilBean.getMyLabel(LabelConstants.VALUE));
                expectedValue.setText(UtilBean.getMyLabel(LabelConstants.EXPECTED));
                performanceItem.setTypeface(Typeface.DEFAULT_BOLD);
                performanceValue.setTypeface(Typeface.DEFAULT_BOLD);
                expectedValue.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                performanceItem.setText(UtilBean.getMyLabel(aStatistic.getKey()));

                if (pageCounter == 2) {
                    performanceValue.setText(String.format("%s%s", aStatistic.getValue().toString(),
                            getProportionForTrueValidationsStatistics(aStatistic.getKey())));
                } else {
                    performanceValue.setText(String.format("%s", aStatistic.getValue()));
                }
                Long tmpObj = mapOfExpectedValues.get(aStatistic.getKey());
                if (tmpObj == null || tmpObj == 0L) {
                    expectedValue.setText(LabelConstants.N_A);
                } else {
                    expectedValue.setText(String.format("%s", tmpObj));
                }

            }
            row.addView(performanceItem, layoutParams2);
            row.addView(performanceValue, layoutParams1);
            row.addView(expectedValue, layoutParams1);
            tableLayout.addView(row, i);
            i++;
        }
        bodyLayoutContainer.addView(tableLayout);
        hideProcessDialog();
    }

    private void addVillageSelectionSpinner() {
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.SELECT_VILLAGE)));
        String[] arrayOfOptions = new String[locationBeans.size() + 1];
        arrayOfOptions[0] = UtilBean.getMyLabel(LabelConstants.ALL);
        int i = 1;
        for (LocationMasterBean locationBean : locationBeans) {
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }
        spinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 2);
        bodyLayoutContainer.addView(spinner);
        hideProcessDialog();
    }

    private void addVillageHeading(LocationBean locationBean) {
        String labelValue;
        if (locationBean != null) {
            labelValue = UtilBean.getMyLabel(LabelConstants.VILLAGE) + ":" + UtilBean.getMyLabel(locationBean.getName());
        } else {
            labelValue = UtilBean.getMyLabel(LabelConstants.ALL + " " + LabelConstants.VILLAGE);
        }

        MaterialTextView villageHeading = MyStaticComponents.generateSubTitleView(this, labelValue);
        bodyLayoutContainer.addView(villageHeading);
    }

    private void addStatisticTypeHeading() {
        String labelValue = "";
        if (pageCounter == 1) {
            labelValue = LabelConstants.SPEED_OF_FHS_VERIFICATION;
        } else if (pageCounter == 2) {
            labelValue = LabelConstants.QUALITY_OF_FHS_VERIFICATION;
        } else if (pageCounter == 3) {
            labelValue = LabelConstants.INFORMATION_ABOUT_EXISTING_BENEFICIARIES;
        } else if (pageCounter == 4) {
            labelValue = LabelConstants.NCD_SCREENING;
        }

        if (!labelValue.trim().isEmpty()) {
            MaterialTextView villageHeading = MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(labelValue));
            bodyLayoutContainer.addView(villageHeading);
        }
    }

    private void addValueInMap(Map<String, Long> map, String key, Long newValue) {
        Long oldValue = map.get(key);
        if (oldValue != null) {
            map.put(key, oldValue + newValue);
        } else {
            map.put(key, newValue);
        }
    }

    private void populateMapOfStatistics(List<FhwServiceDetailBean> fhwServiceDetailBeans) {

        mapOfStatistics.put(LabelConstants.NUMBER_OF_FAMILIES_IMPORTED_FROM_EMAMTA_TARGET, 0L);
        mapOfStatistics.put(LabelConstants.NUMBER_OF_FAMILIES_VERIFIED_BY_YOU_TILL_NOW, 0L);
        mapOfStatistics.put(LabelConstants.NUMBER_OF_FAMILIES_VERIFIED_BY_YOU_IN_LAST_3_DAYS, 0L);
        mapOfStatistics.put(LabelConstants.NUMBER_OF_FAMILIES_ARCHIVED_BY_YOU_TILL_NOW, 0L);
        mapOfStatistics.put(LabelConstants.NUMBER_OF_FAMILIES_ADDED_BY_YOU_TILL_NOW, 0L);
        mapOfStatistics.put(LabelConstants.TOTAL_NUMBER_OF_FAMILIES_IN_TECHO_AS_OF_TODAY, 0L);
        mapOfStatistics.put(LabelConstants.TOTAL_NUMBER_OF_FAMILY_MEMBERS_IN_TECHO_AS_OF_TODAY, 0L);
        mapOfStatistics.put(LabelConstants.NUMBER_AND_PROPORTION_OF_TRUE_VALIDATION_BY_GVK_EMRI_CALL_CENTER, 0L);
        mapOfStatistics.put(LabelConstants.NUMBER_AND_PROPORTION_OF_FAMILY_MEMBERS_FOR_WHOM_YOU_ENTERED_AADHAR_NUMBER, 0L);
        mapOfStatistics.put(LabelConstants.NUMBER_AND_PROPORTION_OF_FAMILY_MEMBERS_FOR_WHOM_YOU_ENTERED_PHONE_NUMBER, 0L);
        mapOfStatistics.put(LabelConstants.NUMBER_OF_ELIGIBLE_COUPLES_IN_TECHO_TODAY, 0L);
        mapOfStatistics.put(LabelConstants.NUMBER_OF_PREGNANT_WOMEN_IN_TECHO_TODAY, 0L);
        mapOfStatistics.put(LabelConstants.NUMBER_OF_UNDER_5_CHILDREN_IN_TECHO_TODAY, 0L);
        mapOfStatistics.put(LabelConstants.NUMBER_OF_SEASONAL_MIGRANT_FAMILIES, 0L);
        mapOfStatistics.put(LabelConstants.NUMBER_OF_MEMBERS_SCREENED, 0L);
        mapOfStatistics.put(LabelConstants.NUMBER_OF_MALE_MEMBERS_SCREENED, 0L);
        mapOfStatistics.put(LabelConstants.NUMBER_OF_FEMALE_MEMBERS_SCREENED, 0L);
        mapOfStatistics.put(LabelConstants.NUMBER_OF_MEMBERS_SCREENED_FOR_HYPERTENSION, 0L);
        mapOfStatistics.put(LabelConstants.NUMBER_OF_MEMBERS_SCREENED_FOR_DIABETES, 0L);
        mapOfStatistics.put(LabelConstants.NUMBER_OF_MEMBERS_SCREENED_FOR_ORAL_CANCER, 0L);
        mapOfStatistics.put(LabelConstants.NUMBER_OF_FEMALE_MEMBERS_SCREENED_FOR_BREAST_CANCER, 0L);
        mapOfStatistics.put(LabelConstants.NUMBER_OF_FEMALE_MEMBERS_SCREENED_FOR_CERVICAL_CANCER, 0L);

        mapOfExpectedValues.put(LabelConstants.NUMBER_OF_FAMILIES_IMPORTED_FROM_EMAMTA_TARGET, 0L);
        mapOfExpectedValues.put(LabelConstants.NUMBER_OF_FAMILIES_VERIFIED_BY_YOU_TILL_NOW, 0L);
        mapOfExpectedValues.put(LabelConstants.NUMBER_OF_FAMILIES_VERIFIED_BY_YOU_IN_LAST_3_DAYS, 0L);
        mapOfExpectedValues.put(LabelConstants.NUMBER_OF_FAMILIES_ARCHIVED_BY_YOU_TILL_NOW, 0L);
        mapOfExpectedValues.put(LabelConstants.NUMBER_OF_FAMILIES_ADDED_BY_YOU_TILL_NOW, 0L);
        mapOfExpectedValues.put(LabelConstants.TOTAL_NUMBER_OF_FAMILIES_IN_TECHO_AS_OF_TODAY, 0L);
        mapOfExpectedValues.put(LabelConstants.TOTAL_NUMBER_OF_FAMILY_MEMBERS_IN_TECHO_AS_OF_TODAY, 0L);
        mapOfExpectedValues.put(LabelConstants.NUMBER_AND_PROPORTION_OF_TRUE_VALIDATION_BY_GVK_EMRI_CALL_CENTER, 0L);
        mapOfExpectedValues.put(LabelConstants.NUMBER_AND_PROPORTION_OF_FAMILY_MEMBERS_FOR_WHOM_YOU_ENTERED_AADHAR_NUMBER, 0L);
        mapOfExpectedValues.put(LabelConstants.NUMBER_AND_PROPORTION_OF_FAMILY_MEMBERS_FOR_WHOM_YOU_ENTERED_PHONE_NUMBER, 0L);
        mapOfExpectedValues.put(LabelConstants.NUMBER_OF_ELIGIBLE_COUPLES_IN_TECHO_TODAY, 0L);
        mapOfExpectedValues.put(LabelConstants.NUMBER_OF_PREGNANT_WOMEN_IN_TECHO_TODAY, 0L);
        mapOfExpectedValues.put(LabelConstants.NUMBER_OF_UNDER_5_CHILDREN_IN_TECHO_TODAY, 0L);
        mapOfExpectedValues.put(LabelConstants.NUMBER_OF_SEASONAL_MIGRANT_FAMILIES, 0L);
        mapOfExpectedValues.put(LabelConstants.NUMBER_OF_MEMBERS_SCREENED, 0L);
        mapOfExpectedValues.put(LabelConstants.NUMBER_OF_MALE_MEMBERS_SCREENED, 0L);
        mapOfExpectedValues.put(LabelConstants.NUMBER_OF_FEMALE_MEMBERS_SCREENED, 0L);
        mapOfExpectedValues.put(LabelConstants.NUMBER_OF_MEMBERS_SCREENED_FOR_HYPERTENSION, 0L);
        mapOfExpectedValues.put(LabelConstants.NUMBER_OF_MEMBERS_SCREENED_FOR_DIABETES, 0L);
        mapOfExpectedValues.put(LabelConstants.NUMBER_OF_MEMBERS_SCREENED_FOR_ORAL_CANCER, 0L);
        mapOfExpectedValues.put(LabelConstants.NUMBER_OF_FEMALE_MEMBERS_SCREENED_FOR_BREAST_CANCER, 0L);
        mapOfExpectedValues.put(LabelConstants.NUMBER_OF_FEMALE_MEMBERS_SCREENED_FOR_CERVICAL_CANCER, 0L);

        for (FhwServiceDetailBean fhwServiceDetailBean : fhwServiceDetailBeans) {
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_OF_FAMILIES_IMPORTED_FROM_EMAMTA_TARGET, fhwServiceDetailBean.getFamiliesImportedFromEMamta());
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_OF_FAMILIES_VERIFIED_BY_YOU_TILL_NOW, fhwServiceDetailBean.getFamiliesVerifiedTillNow());
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_OF_FAMILIES_VERIFIED_BY_YOU_IN_LAST_3_DAYS, fhwServiceDetailBean.getFamiliesVerifiedLast3Days());
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_OF_FAMILIES_ARCHIVED_BY_YOU_TILL_NOW, fhwServiceDetailBean.getFamiliesArchivedTillNow());
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_OF_FAMILIES_ADDED_BY_YOU_TILL_NOW, fhwServiceDetailBean.getNewFamiliesAddedTillNow());
            addValueInMap(mapOfStatistics, LabelConstants.TOTAL_NUMBER_OF_FAMILIES_IN_TECHO_AS_OF_TODAY, fhwServiceDetailBean.getTotalFamiliesInIMTTillNow());
            addValueInMap(mapOfStatistics, LabelConstants.TOTAL_NUMBER_OF_FAMILY_MEMBERS_IN_TECHO_AS_OF_TODAY, fhwServiceDetailBean.getTotalMembersInIMTTillNow());
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_AND_PROPORTION_OF_TRUE_VALIDATION_BY_GVK_EMRI_CALL_CENTER, fhwServiceDetailBean.getNumberOfTrueValidationsByGvkEmri());
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_AND_PROPORTION_OF_FAMILY_MEMBERS_FOR_WHOM_YOU_ENTERED_AADHAR_NUMBER, fhwServiceDetailBean.getNumberOfMembersWithAadharNumberEntered());
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_AND_PROPORTION_OF_FAMILY_MEMBERS_FOR_WHOM_YOU_ENTERED_PHONE_NUMBER, fhwServiceDetailBean.getNumberOfMembersWithMobileNumberEntered());
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_OF_ELIGIBLE_COUPLES_IN_TECHO_TODAY, fhwServiceDetailBean.getTotalEligibleCouplesInTeCHO());
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_OF_PREGNANT_WOMEN_IN_TECHO_TODAY, fhwServiceDetailBean.getTotalPregnantWomenInTeCHO());
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_OF_UNDER_5_CHILDREN_IN_TECHO_TODAY, fhwServiceDetailBean.getUnder5ChildrenTillNow());
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_OF_SEASONAL_MIGRANT_FAMILIES, fhwServiceDetailBean.getTotalNumberOfSeasonalMigrantFamilies());
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_OF_MEMBERS_SCREENED, fhwServiceDetailBean.getNcdTotalMembersScreened());
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_OF_MALE_MEMBERS_SCREENED, fhwServiceDetailBean.getNcdTotalMaleMembersScreened());
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_OF_FEMALE_MEMBERS_SCREENED, fhwServiceDetailBean.getNcdTotalFemaleMembersScreened());
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_OF_MEMBERS_SCREENED_FOR_HYPERTENSION, fhwServiceDetailBean.getNcdTotalMembersScreenedForHypertension());
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_OF_MEMBERS_SCREENED_FOR_DIABETES, fhwServiceDetailBean.getNcdTotalMembersScreenedForDiabetes());
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_OF_MEMBERS_SCREENED_FOR_ORAL_CANCER, fhwServiceDetailBean.getNcdTotalMembersScreenedForOral());
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_OF_FEMALE_MEMBERS_SCREENED_FOR_BREAST_CANCER, fhwServiceDetailBean.getNcdTotalFemaleMembersScreenedForBreast());
            addValueInMap(mapOfStatistics, LabelConstants.NUMBER_OF_FEMALE_MEMBERS_SCREENED_FOR_CERVICAL_CANCER, fhwServiceDetailBean.getNcdTotalFemaleMembersScreenedForCervical());

            //expectedValues
            addValueInMap(mapOfExpectedValues, LabelConstants.NUMBER_OF_FAMILIES_IMPORTED_FROM_EMAMTA_TARGET, fhwServiceDetailBean.getFamiliesImportedFromEMamtaExpectedValue());
            addValueInMap(mapOfExpectedValues, LabelConstants.NUMBER_OF_FAMILIES_VERIFIED_BY_YOU_TILL_NOW, fhwServiceDetailBean.getFamiliesVerifiedTillNowExpectedValue());
            addValueInMap(mapOfExpectedValues, LabelConstants.NUMBER_OF_FAMILIES_VERIFIED_BY_YOU_IN_LAST_3_DAYS, fhwServiceDetailBean.getFamiliesVerifiedLast3DaysExpectedValue());
            addValueInMap(mapOfExpectedValues, LabelConstants.NUMBER_OF_FAMILIES_ARCHIVED_BY_YOU_TILL_NOW, fhwServiceDetailBean.getFamiliesArchivedTillNowExpectedValue());
            addValueInMap(mapOfExpectedValues, LabelConstants.NUMBER_OF_FAMILIES_ADDED_BY_YOU_TILL_NOW, fhwServiceDetailBean.getNewFamiliesAddedTillNowExpectedValue());
            addValueInMap(mapOfExpectedValues, LabelConstants.TOTAL_NUMBER_OF_FAMILIES_IN_TECHO_AS_OF_TODAY, fhwServiceDetailBean.getNewFamiliesAddedTillNowExpectedValue());
            addValueInMap(mapOfExpectedValues, LabelConstants.TOTAL_NUMBER_OF_FAMILY_MEMBERS_IN_TECHO_AS_OF_TODAY, fhwServiceDetailBean.getTotalMembersInIMTTillNowExpectedValue());
            addValueInMap(mapOfExpectedValues, LabelConstants.NUMBER_AND_PROPORTION_OF_TRUE_VALIDATION_BY_GVK_EMRI_CALL_CENTER, fhwServiceDetailBean.getNumberOfTrueValidationsByGvkEmriExpectedValue());
            addValueInMap(mapOfExpectedValues, LabelConstants.NUMBER_AND_PROPORTION_OF_FAMILY_MEMBERS_FOR_WHOM_YOU_ENTERED_AADHAR_NUMBER,
                    fhwServiceDetailBean.getNumberOfMembersWithAadharNumberEnteredExpectedValue());
            addValueInMap(mapOfExpectedValues, LabelConstants.NUMBER_AND_PROPORTION_OF_FAMILY_MEMBERS_FOR_WHOM_YOU_ENTERED_PHONE_NUMBER,
                    fhwServiceDetailBean.getNumberOfMembersWithMobileNumberEnteredExpectedValue());
            addValueInMap(mapOfExpectedValues, LabelConstants.NUMBER_OF_ELIGIBLE_COUPLES_IN_TECHO_TODAY, fhwServiceDetailBean.getTotalEligibleCouplesInTeCHOExpectedValue());
            addValueInMap(mapOfExpectedValues, LabelConstants.NUMBER_OF_PREGNANT_WOMEN_IN_TECHO_TODAY, fhwServiceDetailBean.getTotalPregnantWomenInTeCHOExpectedValue());
            addValueInMap(mapOfExpectedValues, LabelConstants.NUMBER_OF_UNDER_5_CHILDREN_IN_TECHO_TODAY, fhwServiceDetailBean.getUnder5ChildrenTillNowExpectedValue());
            addValueInMap(mapOfExpectedValues, LabelConstants.NUMBER_OF_SEASONAL_MIGRANT_FAMILIES, fhwServiceDetailBean.getTotalNumberOfSeasonalMigrantFamiliesExpectedValue());
        }
    }

    private String getProportionForTrueValidationsStatistics(String key) {
        StringBuilder stringBuilder = new StringBuilder(" ");
        Long statValue = mapOfStatistics.get(key);
        if (statValue == null) {
            statValue = 0L;
        }
        Long totalValue;
        stringBuilder.append("(");
        switch (key) {
            case LabelConstants.NUMBER_AND_PROPORTION_OF_TRUE_VALIDATION_BY_GVK_EMRI_CALL_CENTER:
                totalValue = mapOfStatistics.get(LabelConstants.TOTAL_NUMBER_OF_FAMILIES_IN_TECHO_AS_OF_TODAY);
                if (totalValue == null || totalValue == 0) {
                    return "";
                }
                stringBuilder.append(String.format(Locale.getDefault(), "%.2f",
                        (float) statValue / totalValue * 100));
                break;
            case LabelConstants.NUMBER_AND_PROPORTION_OF_FAMILY_MEMBERS_FOR_WHOM_YOU_ENTERED_AADHAR_NUMBER:
            case LabelConstants.NUMBER_AND_PROPORTION_OF_FAMILY_MEMBERS_FOR_WHOM_YOU_ENTERED_PHONE_NUMBER:
                totalValue = mapOfStatistics.get(LabelConstants.TOTAL_NUMBER_OF_FAMILY_MEMBERS_IN_TECHO_AS_OF_TODAY);
                if (totalValue == null || totalValue == 0) {
                    return "";
                }
                stringBuilder.append(String.format(Locale.getDefault(), "%.2f",
                        (float) statValue / totalValue * 100));
                break;
            default:
        }
        stringBuilder.append("%)");
        return stringBuilder.toString();
    }

    @UiThread
    public void retrieveWorkLog() {
        List<LoggerBean> loggerBeans = sewaService.getWorkLog();
        List<WorkLogScreenDataBean> lst = new ArrayList<>();
        WorkLogScreenDataBean log;
        if (loggerBeans != null && !loggerBeans.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat(GlobalTypes.DATE_DD_MM_YYYY_FORMAT + " hh:mm:ss", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);

            for (LoggerBean loggerBean : loggerBeans) {
                if (loggerBean.getStatus().equalsIgnoreCase(GlobalTypes.STATUS_SUCCESS)) {
                    log = new WorkLogScreenDataBean(sdf.format(new Date(loggerBean.getDate())), R.drawable.success,
                            loggerBean.getBeneficiaryName(), loggerBean.getTaskName(), loggerBean.getMessage());
                    lst.add(log);
                } else if (loggerBean.getStatus().equalsIgnoreCase(GlobalTypes.STATUS_PENDING)) {
                    log = new WorkLogScreenDataBean(sdf.format(new Date(loggerBean.getDate())), R.drawable.pending,
                            loggerBean.getBeneficiaryName(), loggerBean.getTaskName(), loggerBean.getMessage());
                    lst.add(log);
                } else if (loggerBean.getStatus().equalsIgnoreCase(GlobalTypes.STATUS_ERROR)
                        && loggerBean.getModifiedOn() != null && loggerBean.getModifiedOn().after(calendar.getTime())) {
                    log = new WorkLogScreenDataBean(sdf.format(new Date(loggerBean.getDate())), R.drawable.error,
                            loggerBean.getBeneficiaryName(), loggerBean.getTaskName(), loggerBean.getMessage());
                    lst.add(log);
                } else if (loggerBean.getStatus().equalsIgnoreCase(GlobalTypes.STATUS_HANDLED_ERROR)
                        && loggerBean.getModifiedOn() != null && loggerBean.getModifiedOn().after(calendar.getTime())) {
                    log = new WorkLogScreenDataBean(sdf.format(new Date(loggerBean.getDate())), R.drawable.warning,
                            loggerBean.getBeneficiaryName(), loggerBean.getTaskName(), loggerBean.getMessage());
                    lst.add(log);
                }
            }
        } else {
            log = new WorkLogScreenDataBean(null, -1, LabelConstants.THERE_IS_NO_WORKLOG_TO_DISPLAY, null, null);
            lst.add(log);
        }

        MyWorkLogAdapter adapter = new MyWorkLogAdapter(this, lst);
        bodyLayoutContainer.addView(MyStaticComponents.getListView(this, adapter, null));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            if (isVillageSelectionScreen) {
                navigateToHomeScreen(false);
            } else if (pageCounter == 1 && locationBeans.size() == 1) {
                navigateToHomeScreen(false);
            } else if (pageCounter == 1) {
                bodyLayoutContainer.removeAllViews();
                isVillageSelectionScreen = Boolean.TRUE;
                pageCounter--;
                addVillageSelectionSpinner();
            } else {
                nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                bodyLayoutContainer.removeAllViews();
                if (pageCounter == 3) {
                    nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                }
                pageCounter--;
                if (locationId != null) {
                    addTable(Long.valueOf(locationId));
                } else {
                    addTable(null);
                }
            }
        }
        return true;
    }
}
