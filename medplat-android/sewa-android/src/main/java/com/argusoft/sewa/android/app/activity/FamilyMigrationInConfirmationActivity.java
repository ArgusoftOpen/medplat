package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.LocationMasterServiceImpl;
import com.argusoft.sewa.android.app.core.impl.MigrationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.FamilyMigrationDetailsDataBean;
import com.argusoft.sewa.android.app.databean.FamilyMigrationInConfirmationDataBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.gson.Gson;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by prateek on 8/20/19
 */
@EActivity
public class FamilyMigrationInConfirmationActivity extends MenuActivity implements View.OnClickListener {

    private static final String FAMILY_INFO_SCREEN = "familyInfoScreen";
    private static final String AREA_SELECTION_SCREEN = "areaSelectionScreen";
    private static final String CONFIRMATION_SCREEN = "confirmationScreen";
    private static final String FINAL_SCREEN = "finalScreen";
    private static final Integer RADIO_BUTTON_ID_YES = 1;
    private static final Integer RADIO_BUTTON_ID_NO = 2;
    private static final Integer RADIO_BUTTON_ID_NOT_FOUND = 3;
    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    LocationMasterServiceImpl locationMasterService;
    @Bean
    MigrationServiceImpl migrationService;
    private LinearLayout bodyLayoutContainer;
    private String screenName;
    private NotificationMobDataBean notificationMobDataBean;
    private FamilyMigrationDetailsDataBean familyMigrationDetailsDataBean;
    private RadioGroup radioGroupForFamilyQue;
    private RadioGroup radioGroupForConfirmation;
    private Spinner areaSpinner;

    private List<LocationBean> areas;
    private LocationBean selectedVillage;
    private LocationBean selectedArea;
    private LinearLayout globalPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String notificationBeanJson = extras.getString(GlobalTypes.NOTIFICATION_BEAN, null);
            notificationMobDataBean = new Gson().fromJson(notificationBeanJson, NotificationMobDataBean.class);
            selectedVillage = locationMasterService.getLocationBeanByActualId(notificationMobDataBean.getLocationId().toString());
            areas = fhsService.retrieveAshaAreaAssignedToUser(notificationMobDataBean.getLocationId().intValue());
            familyMigrationDetailsDataBean = new Gson().fromJson(notificationMobDataBean.getOtherDetails(), FamilyMigrationDetailsDataBean.class);
        }
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
        setTitle(UtilBean.getTitleText(LabelConstants.FAMILY_MIGRATION_IN_CONFIRMATION_TITLE));
        setSubTitle(null);
    }

    private void initView() {
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
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
                showProcessDialog();
                finish();
            } else {
                alertDialog.dismiss();
            }
        };

        alertDialog = new MyAlertDialog(context,
                LabelConstants.ON_MIGRATION_CLOSE_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screenName) {
                case FAMILY_INFO_SCREEN:
                    if (radioGroupForFamilyQue.getCheckedRadioButtonId() == -1) {
                        SewaUtil.generateToast(context, LabelConstants.PLEASE_SELECT_AN_OPTION);
                        break;
                    }

                    if (radioGroupForFamilyQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                        setAreaSelectionScreen();
                        break;
                    }

                    if (radioGroupForFamilyQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
                        setConfirmationScreen();
                        break;
                    }

                    if (radioGroupForFamilyQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NOT_FOUND) {
                        addFinalScreen();
                        break;
                    }
                    break;

                case AREA_SELECTION_SCREEN:
                    if (areaSpinner.getSelectedItemPosition() == 0) {
                        SewaUtil.generateToast(context, LabelConstants.AREA_SELECTION_REQUIRED_ALERT);
                    } else {
                        selectedArea = areas.get(areaSpinner.getSelectedItemPosition() - 1);
                        setConfirmationScreen();
                    }
                    break;

                case CONFIRMATION_SCREEN:
                    if (radioGroupForConfirmation.getCheckedRadioButtonId() == -1) {
                        SewaUtil.generateToast(context, LabelConstants.PLEASE_SELECT_AN_OPTION);
                    }
                    if (radioGroupForConfirmation.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                        addFinalScreen();
                    }
                    if (radioGroupForConfirmation.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
                        if (radioGroupForFamilyQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                            setAreaSelectionScreen();
                            break;
                        }
                        if (radioGroupForFamilyQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
                            setFamilyInfoScreen();
                            break;
                        }
                    }
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

        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this,
                UtilBean.getMyLabel(LabelConstants.HAVE_TO_FIND_FAMILY_IN_YOUR_VILLAGE)));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel(LabelConstants.FAMILY_ID)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                familyMigrationDetailsDataBean.getFamilyIdString()));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                UtilBean.getMyLabel(LabelConstants.MEMBER_DETAILS)));
        for (String str : familyMigrationDetailsDataBean.getMemberDetails()) {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                    str));
        }
        if (familyMigrationDetailsDataBean.getLocationDetails() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                    UtilBean.getMyLabel(LabelConstants.LOCATION_MIGRATED_FROM)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                    familyMigrationDetailsDataBean.getLocationDetails()));
        }
        if (familyMigrationDetailsDataBean.getAreaDetails() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                    UtilBean.getMyLabel(LabelConstants.AREA_MIGRATED_FROM)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                    familyMigrationDetailsDataBean.getAreaDetails()));
        }
        if (familyMigrationDetailsDataBean.getFhwDetails() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                    UtilBean.getMyLabel(LabelConstants.FHW_DETAILS)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                    familyMigrationDetailsDataBean.getFhwDetails()));
        }
        if (familyMigrationDetailsDataBean.getOtherInfo() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                    UtilBean.getMyLabel(LabelConstants.OTHER_INFO)));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                    familyMigrationDetailsDataBean.getOtherInfo()));
        }


        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                LabelConstants.CONFORMATION_TO_FAMILY_MIGRATION));

        if (radioGroupForFamilyQue == null) {
            HashMap<Integer, String> stringMap = new HashMap<>();
            stringMap.put(RADIO_BUTTON_ID_YES, LabelConstants.YES);
            stringMap.put(RADIO_BUTTON_ID_NO, LabelConstants.NO);
            stringMap.put(RADIO_BUTTON_ID_NOT_FOUND, LabelConstants.FAMILY_NOT_FOUND_YET);
            radioGroupForFamilyQue = MyStaticComponents.getRadioGroup(this, stringMap, false);
        }
        bodyLayoutContainer.addView(radioGroupForFamilyQue);
    }

    private void setAreaSelectionScreen() {
        screenName = AREA_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        if (areaSpinner == null) {
            String[] arrayOfOptions = new String[areas.size() + 1];
            arrayOfOptions[0] = GlobalTypes.SELECT;
            int i = 1;
            for (LocationBean locationBean : areas) {
                arrayOfOptions[i] = locationBean.getName();
                i++;
            }
            areaSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 3);
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.SELECT_AREA_FOR_FAMILY));
        bodyLayoutContainer.addView(areaSpinner);
    }

    private void setConfirmationScreen() {
        screenName = CONFIRMATION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        if (radioGroupForFamilyQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {

            if (selectedVillage != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                        UtilBean.getMyLabel(LabelConstants.VILLAGE_MIGRATED_TO)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, selectedVillage.getName()));
            }

            if (selectedArea != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                        UtilBean.getMyLabel(LabelConstants.AREA_MIGRATED_TO)));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, selectedArea.getName()));
            }

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel(LabelConstants.CONFORMATION_TO_FAMILY_MIGRATION_TO_SELECTED_AREA)));
        }

        if (radioGroupForFamilyQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel(LabelConstants.CONFORMATION_TO_FAMILY_NOT_MIGRATED_TO_YOUR_VILLAGE)));
        }

        if (radioGroupForConfirmation == null) {
            HashMap<Integer, String> stringMap = new HashMap<>();
            stringMap.put(RADIO_BUTTON_ID_YES, LabelConstants.YES);
            stringMap.put(RADIO_BUTTON_ID_NO, LabelConstants.NO);
            radioGroupForConfirmation = MyStaticComponents.getRadioGroup(this, stringMap, true);
        }

        bodyLayoutContainer.addView(radioGroupForConfirmation);
    }

    private void addFinalScreen() {
        screenName = FINAL_SCREEN;
        bodyLayoutContainer.removeAllViews();

        if (radioGroupForFamilyQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this,
                    UtilBean.getMyLabel(LabelConstants.SUCCESSFULLY_MIGRATED_IN_A_FAMILY)));
        }
        if (radioGroupForFamilyQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this,
                    UtilBean.getMyLabel(LabelConstants.SELECTED_NOT_TO_MIGRATE_IN_A_FAMILY)));
        }
        if (radioGroupForFamilyQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NOT_FOUND) {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this,
                    UtilBean.getMyLabel(LabelConstants.COME_BACK_WHEN_YOU_FIND_THE_FAMILY)));
        }
    }

    private void finishActivity() {
        if (radioGroupForFamilyQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NOT_FOUND) {
            finish();
        } else {
            View.OnClickListener myListener = v -> {
                if (v.getId() == BUTTON_POSITIVE) {
                    alertDialog.dismiss();
                    createFamilyMigrationInConfirmationBean();
                    finish();
                } else {
                    alertDialog.dismiss();
                }
            };

            alertDialog = new MyAlertDialog(context,
                    LabelConstants.SUBMIT_THIS_FAMILY_MIGRATION_CONFIRMATION_FORM,
                    myListener, DynamicUtils.BUTTON_YES_NO);
            alertDialog.show();
        }
    }

    private void createFamilyMigrationInConfirmationBean() {
        FamilyMigrationInConfirmationDataBean confirmationDataBean = new FamilyMigrationInConfirmationDataBean();
        confirmationDataBean.setNotificationId(notificationMobDataBean.getId());
        confirmationDataBean.setMigrationId(notificationMobDataBean.getMigrationId());
        confirmationDataBean.setFamilyId(notificationMobDataBean.getFamilyId());
        if (radioGroupForFamilyQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
            confirmationDataBean.setHasMigrationHappened(Boolean.TRUE);
            confirmationDataBean.setAreaMigratedTo(selectedArea.getActualID().longValue());
        }
        if (radioGroupForFamilyQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
            confirmationDataBean.setHasMigrationHappened(Boolean.FALSE);
        }

        migrationService.createFamilyMigrationInConfirmation(confirmationDataBean, familyMigrationDetailsDataBean);
        sewaService.deleteNotificationByNotificationId(notificationMobDataBean.getId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);if(screenName == null || screenName.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            switch (screenName) {
                case FAMILY_INFO_SCREEN:
                    showProcessDialog();
                    finish();
                    break;

                case AREA_SELECTION_SCREEN:
                    setFamilyInfoScreen();
                    break;

                case CONFIRMATION_SCREEN:
                    if (radioGroupForFamilyQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                        setAreaSelectionScreen();
                    }
                    if (radioGroupForFamilyQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
                        setFamilyInfoScreen();
                    }
                    break;

                case FINAL_SCREEN:
                    if (radioGroupForFamilyQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NOT_FOUND) {
                        setFamilyInfoScreen();
                    } else {
                        setConfirmationScreen();
                    }
                    break;

                default:
                    break;
            }
        }
        return true;
    }
}
