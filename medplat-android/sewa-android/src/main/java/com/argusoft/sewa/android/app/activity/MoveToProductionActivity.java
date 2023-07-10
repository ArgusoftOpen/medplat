package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.MoveToProductionServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.FormAccessibilityBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.button.MaterialButtonToggleGroup;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.List;

@EActivity
public class MoveToProductionActivity extends MenuActivity implements View.OnClickListener {

    @Bean
    SewaServiceImpl sewaService;
    @Bean
    MoveToProductionServiceImpl moveToProductoinService;

    private LinearLayout bodyLayoutContainer;
    private int counter = 0;
    private List<FormAccessibilityBean> formsTrainingCompleted = new ArrayList<>();
    private List<FormAccessibilityBean> formsToMoveToProductions = new ArrayList<>();
    private LinearLayout globalPanel;
    private MaterialButtonToggleGroup yesNoToggleButton;

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
        if (!SharedStructureData.isLogin) {
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
        setTitle(UtilBean.getTitleText(LabelConstants.MOVE_TO_PRODUCTION_ACTIVITY_TITLE));
        setSubTitle(null);
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        formsTrainingCompleted = moveToProductoinService.isAnyFormTrainingCompleted();
        addFormList();
    }

    private void addFormList() {
        if (!formsTrainingCompleted.isEmpty()) {
            bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(this, LabelConstants.CONGRATULATIONS));
            bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(this,
                    UtilBean.getMyLabel(LabelConstants.COMPLETED_TRAINING_FROM + " : " + formsTrainingCompleted.get(counter).getForm())));
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    LabelConstants.CONFIRMATION_TO_MOVE_PRODUCTION));

            yesNoToggleButton = MyStaticComponents.getYesNoToggleButton(context);
            bodyLayoutContainer.addView(yesNoToggleButton);
        }
        hideProcessDialog();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            List<Integer> checkedButton = yesNoToggleButton.getCheckedButtonIds();
            if (!checkedButton.isEmpty()) {
                if (checkedButton.get(0) == 10002) {
                    if (!formsToMoveToProductions.contains(formsTrainingCompleted.get(counter))) {
                        formsToMoveToProductions.add(formsTrainingCompleted.get(counter));
                    }
                } else if (checkedButton.get(0) == 10003) {
                    formsToMoveToProductions.remove(formsTrainingCompleted.get(counter));
                }
                counter++;
                if (counter < formsTrainingCompleted.size()) {
                    bodyLayoutContainer.removeAllViews();
                    addFormList();
                } else {
                    moveToProductoinService.saveFormsToMoveToProduction(formsToMoveToProductions);
                    sendDataToServerIfOnline();
                    navigateToHomeScreen(false);
                }
            } else {
                SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_AN_OPTION));
            }
        }
    }

    @Background
    public void sendDataToServerIfOnline() {
        if (sewaService.isOnline()) {
            moveToProductoinService.postUserReadyToMoveProduction();
            moveToProductoinService.saveFormAccessibilityBeansFromServer();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            if (counter != 0) {
                counter--;
                bodyLayoutContainer.removeAllViews();
                addFormList();
            } else {
                navigateToHomeScreen(false);
            }
        }
        return true;
    }
}
