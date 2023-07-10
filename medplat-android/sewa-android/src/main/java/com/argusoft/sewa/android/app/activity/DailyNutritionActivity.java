package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.FhsConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.core.impl.DailyNutritionLogServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.OptionDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.DailyNutritionLogBean;
import com.argusoft.sewa.android.app.model.FamilyBean;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@EActivity
public class DailyNutritionActivity extends MenuActivity implements View.OnClickListener {

    @OrmLiteDao(helper = DBConnection.class)
    Dao<MemberBean, Integer> memberBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<FamilyBean, Integer> familyBeanDao;
    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    DailyNutritionLogServiceImpl dailyNutritionLogService;

    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private Spinner ashaAreaSpinner;
    private Integer selectedAshaArea;
    private List<LocationBean> ashaAreaList = new ArrayList<>();
    private LinearLayout globalPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (globalPanel != null) {
            setContentView(globalPanel);
        }
        setTitle(UtilBean.getTitleText(UtilBean.getFullFormOfEntity().get(FormConstants.TECHO_AWW_DAILY_NUTRITION)));
    }

    @UiThread
    public void checkIfAlreadyFilled() {
        DailyNutritionLogBean dailyNutritionLogBean = null;
        try {
            dailyNutritionLogBean = dailyNutritionLogService.retrieveDailyNutritionLogByLocationId(selectedAshaArea);
        } catch (Exception e) {
            Log.e(getClass().getName(), null, e);
        }
        if (dailyNutritionLogBean != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            if (dailyNutritionLogBean.getSeviceDate() > calendar.getTime().getTime()) {
                View.OnClickListener onClickListener = v -> {
                    alertDialog.dismiss();
                    if (ashaAreaList.size() == 1) {
                        navigateToHomeScreen(false);
                    } else {
                        startLocationSelectionActivity();
                    }
                };

                alertDialog = new MyAlertDialog(this, false,
                        LabelConstants.FORM_FILLED_FOR_THE_DAY_ALERT,
                        onClickListener, DynamicUtils.BUTTON_OK);
                alertDialog.show();
            } else {
                addChildData();
            }
        } else {
            addChildData();
        }
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
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
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.DAILY_NUTRITION_TITLE);
        startActivityForResult(myIntent, ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            nextButtonPressed();
        }
    }

    private void nextButtonPressed() {
        showProcessDialog();
        String selectedArea = ashaAreaSpinner.getSelectedItem().toString();
        for (LocationBean locationBean : ashaAreaList) {
            if (selectedArea.equals(locationBean.getName())) {
                selectedAshaArea = locationBean.getActualID();
                break;
            }
        }
        bodyLayoutContainer.removeAllViews();
        checkIfAlreadyFilled();
    }

    @Background
    public void addChildData() {
        setSubTitle(null);
        try {
            List<FamilyBean> familyBeans = familyBeanDao.queryBuilder().selectColumns("familyId").where()
                    .eq("areaId", selectedAshaArea)
                    .and().in("state", FhsConstants.FHS_ACTIVE_CRITERIA_FAMILY_STATES).query();

            List<String> familyIds = new ArrayList<>();
            for (FamilyBean familyBean : familyBeans) {
                familyIds.add(familyBean.getFamilyId());
            }

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -3);
            Date date3 = calendar.getTime();
            calendar.add(Calendar.YEAR, -3);
            Date date6 = calendar.getTime();
            List<MemberBean> children = memberBeanDao.queryBuilder().where()
                    .in("familyId", familyIds)
                    .and().between("dob", date6, date3)
                    .and().notIn("state", FhsConstants.FHS_INACTIVE_CRITERIA_MEMBER_STATES)
                    .query();

            List<OptionDataBean> optionDataBeans = new ArrayList<>();
            for (MemberBean bean : children) {
                OptionDataBean optionDataBean = new OptionDataBean();
                optionDataBean.setKey(bean.getActualId());
                optionDataBean.setValue(bean.getFirstName() + " " + bean.getMiddleName() + " " + bean.getLastName());
                optionDataBeans.add(optionDataBean);
            }
            SharedStructureData.relatedPropertyHashTable.clear();
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.LOCATION_ID, selectedAshaArea.toString());
            SharedStructureData.childrenFrom3To6Years = optionDataBeans;
            startDynamicFormActivity();
        } catch (SQLException e) {
            Log.e(getClass().getName(), null, e);
        }
    }

    @UiThread
    public void addAshaAreaSelectionSpinner() {
        bodyLayoutContainer.removeAllViews();
        setSubTitle(null);
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.SELECT_ASHA_AREA));
        String[] arrayOfOptions = new String[ashaAreaList.size()];
        int i = 0;
        for (LocationBean locationBean : ashaAreaList) {
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }
        ashaAreaSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 2);
        if (selectedAshaArea != null) {
            for (LocationBean locationBean : ashaAreaList) {
                String locationName;
                if (locationBean.getActualID().intValue() == selectedAshaArea.intValue()) {
                    locationName = locationBean.getName();
                    ashaAreaSpinner.setSelection(Arrays.asList(arrayOfOptions).indexOf(locationName));
                }
            }
        }
        bodyLayoutContainer.addView(ashaAreaSpinner);
        hideProcessDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        navigateToHomeScreen(false);
        return true;
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
                LabelConstants.ON_DAILY_NUTRITION_CLOSE_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    private void startDynamicFormActivity() {
        showProcessDialog();
        Intent intent = new Intent(this, DynamicFormActivity_.class);
        SharedStructureData.relatedPropertyHashTable.put("nameOfBeneficiary", UtilBean.getMyLabel(LabelConstants.DAILY_NUTRITION_FORM));
        intent.putExtra(SewaConstants.ENTITY, FormConstants.TECHO_AWW_DAILY_NUTRITION);
        startActivityForResult(intent, ActivityConstants.DAILY_NUTRITION_ACTIVITY_REQUEST_CODE);
        hideProcessDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityConstants.DAILY_NUTRITION_ACTIVITY_REQUEST_CODE) {
            startLocationSelectionActivity();
        } else if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                selectedAshaArea = Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID));
                showProcessDialog();
                bodyLayoutContainer.removeAllViews();
                checkIfAlreadyFilled();
            } else {
                navigateToHomeScreen(false);
            }
        } else {
            navigateToHomeScreen(false);
        }
    }
}
