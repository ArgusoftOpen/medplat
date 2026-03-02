package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.LocationMasterServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.model.LocationMasterBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity
public class LocationSelectionActivity extends MenuActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    LocationMasterServiceImpl locationService;

    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private MaterialButton nextButton;
    private List<LocationMasterBean> locationBeans;
    private LocationMasterBean selectedLocation;
    private Map<Integer, List<LocationMasterBean>> locationMap = new HashMap<>();
    private Map<Integer, Spinner> spinnerMap = new HashMap<>();
    private Map<Integer, MaterialTextView> selectTextViewMap = new HashMap<>();
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //To change body of generated methods, choose Tools | Templates.
        context = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title = extras.getString(FieldNameConstants.TITLE, LabelConstants.SELECT_LOCATION);
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (globalPanel != null) {
            setContentView(globalPanel);
        }
        setTitle(UtilBean.getTitleText(title));
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
                UtilBean.getMyLabel("Are you sure you want to close " + title + "?"),
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return true;
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
        locationBeans = locationService.retrieveLocationMastersAssignedToUser();
        if (locationBeans.isEmpty()) {
            addDataNotSyncedMsg(bodyLayoutContainer, nextButton);
        } else {
            addLocationSpinner();
        }
    }

    @UiThread
    public void addLocationSpinner() {
        String locationType = null;
        String[] arrayOfOptions = new String[locationBeans.size() + 1];
        arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
        int i = 1;
        for (LocationMasterBean locationBean : locationBeans) {
            locationType = locationBean.getType();
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }

        Integer level = locationBeans.get(0).getLevel();
        locationMap.put(level, locationBeans);

        Spinner spinner = MyStaticComponents.getSpinner(context, arrayOfOptions, 0, level);
        spinner.setOnItemSelectedListener(this);

        String name = locationService.getLocationTypeNameByType(locationType);
        MaterialTextView selectTextView = MyStaticComponents.generateSubTitleView(this, "Select " + name);
        bodyLayoutContainer.addView(selectTextView);
        bodyLayoutContainer.addView(spinner);
        spinnerMap.put(level, spinner);
        selectTextViewMap.put(level, selectTextView);
        hideProcessDialog();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == nextButton.getId()) {
            if (selectedLocation != null) {
                Intent intent = new Intent();
                intent.putExtra(FieldNameConstants.LOCATION_ID, selectedLocation.getActualID().toString());
                intent.putExtra(FieldNameConstants.LOCATION, new Gson().toJson(selectedLocation));
                setResult(RESULT_OK, intent);
                finish();
            } else {
                SewaUtil.generateToast(context, LabelConstants.SELECT_LOCATION);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int level = parent.getId();
        if (position != 0) {
            List<LocationMasterBean> locationMasterBeans = locationMap.get(level);
            if (locationMasterBeans != null && !locationMasterBeans.isEmpty()) {
                LocationMasterBean locationBean = locationMasterBeans.get(position - 1);
                List<LocationMasterBean> locationMasterBeanList = locationService.retrieveLocationMasterBeansByLevelAndParent(level + 1, locationBean.getActualID());
                if (locationMasterBeanList != null && !locationMasterBeanList.isEmpty()) {
                    selectedLocation = null;
                    locationMap.put(level + 1, locationMasterBeanList);
                    removeSpinners(level);

                    String locationType = null;
                    String[] arrayOfOptions = new String[locationMasterBeanList.size() + 1];
                    arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
                    int i = 1;
                    for (LocationMasterBean location : locationMasterBeanList) {
                        locationType = location.getType();
                        arrayOfOptions[i] = location.getName();
                        i++;
                    }
                    Spinner spinner = MyStaticComponents.getSpinner(context, arrayOfOptions, 0, level + 1);
                    spinner.setOnItemSelectedListener(this);

                    String name = locationService.getLocationTypeNameByType(locationType);
                    MaterialTextView selectTextView = MyStaticComponents.generateSubTitleView(this, "Select " + name);
                    bodyLayoutContainer.addView(selectTextView);
                    bodyLayoutContainer.addView(spinner);
                    spinnerMap.put(level + 1, spinner);
                    selectTextViewMap.put(level + 1, selectTextView);
                } else {
                    selectedLocation = locationBean;
                }
            }
        } else {
            selectedLocation = null;
            removeSpinners(level);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //
    }

    private void removeSpinners(Integer level) {
        for (Map.Entry<Integer, List<LocationMasterBean>> entry : locationMap.entrySet()) {
            if (entry.getKey() > level) {
                bodyLayoutContainer.removeView(selectTextViewMap.get(entry.getKey()));
                bodyLayoutContainer.removeView(spinnerMap.get(entry.getKey()));
            }
        }
    }

}
