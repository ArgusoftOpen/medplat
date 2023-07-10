package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.MigrationServiceImpl;
import com.argusoft.sewa.android.app.databean.MigratedFamilyDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.MigratedFamilyBean;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.gson.Gson;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.HashMap;
import java.util.Objects;

import static android.content.DialogInterface.BUTTON_POSITIVE;

@EActivity
public class FamilyMigrationRevertActivity extends MenuActivity implements View.OnClickListener {

    @Bean
    MigrationServiceImpl migrationService;

    private static final String FAMILY_INFO_SCREEN = "familyInfoScreen";
    private static final String CONFIRMATION_SCREEN = "confirmationScreen";
    private static final String FINAL_SCREEN = "finalScreen";
    private static final String STATUS_OUT_OF_STATE = "outOfState";
    private static final String STATUS_CONFIRMATION_PENDING = "confirmationPending";
    private static final String STATUS_CONFIRMED = "confirmed";
    private static final String STATUS_LFU = "confirmed";
    private static final Integer RADIO_BUTTON_ID_YES = 1;
    private static final Integer RADIO_BUTTON_ID_NO = 2;

    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private String screenName;
    private MyAlertDialog myAlertDialog;

    private boolean isOut;
    private MigratedFamilyBean migratedFamilyBean;
    private RadioGroup revertRadioGroup;
    private RadioGroup confirmationRadioGroup;
    private LinearLayout globalPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isOut = extras.getBoolean("isOut");
            String string = extras.getString("migratedFamilyBean");
            if (string != null) {
                migratedFamilyBean = new Gson().fromJson(string, MigratedFamilyBean.class);
            }
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
        setTitle(UtilBean.getTitleText(LabelConstants.MANAGE_FAMILY_MIGRATIONS));
        setSubTitle(migratedFamilyBean.getFamilyIdString());
    }

    private void initView() {
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        setContentView(globalPanel);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        setFamilyDetailsScreen();
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
                LabelConstants.ON_MANAGE_FAMILY_MIGRATIONS_CLOSE_ALERT,
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
                case FAMILY_INFO_SCREEN:
                    if (revertRadioGroup.getCheckedRadioButtonId() == -1) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_AN_OPTION));
                    } else if (revertRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
                        finish();
                    } else if (revertRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                        setConfirmationScreen();
                    }
                    break;

                case CONFIRMATION_SCREEN:
                    if (confirmationRadioGroup.getCheckedRadioButtonId() == -1) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_AN_OPTION));
                    } else if (confirmationRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
                        setFamilyDetailsScreen();
                    } else if (confirmationRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                        setFinalScreen();
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

    private void setFamilyDetailsScreen() {
        screenName = FAMILY_INFO_SCREEN;
        String status;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context, LabelConstants.FAMILY_DETAILS));

        if (migratedFamilyBean.getFamilyIdString() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.FAMILY_ID));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, migratedFamilyBean.getFamilyIdString()));
        }

        if (migratedFamilyBean.getLocationMigratedFrom() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.LOCATION_MIGRATED_FROM));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, migratedFamilyBean.getLocationMigratedFrom()));
        }

        if (migratedFamilyBean.getLocationMigratedTo() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.LOCATION_MIGRATED_TO));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, migratedFamilyBean.getLocationMigratedTo()));
        }

        if (Boolean.TRUE.equals(migratedFamilyBean.getSplitFamily())) {
            if (Objects.equals(migratedFamilyBean.getToLocationId(), migratedFamilyBean.getFromLocationId())) {
                bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context, LabelConstants.SPLIT_FAMILY_INFO));
            } else {
                bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context, LabelConstants.SPLIT_WITH_MIGRATED_FAMILY_INFO));
            }
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.NEW_FAMILY_ID));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, migratedFamilyBean.getsFamilyIdString()));

            if (migratedFamilyBean.getSplitMembersDetail() != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.MEMBER_DETAILS));
                for (String member : migratedFamilyBean.getSplitMembersDetail().split(",")) {
                    bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, member));
                }
            }

        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.MIGRATION_STATUS));
        if (migratedFamilyBean.getLfu() != null && migratedFamilyBean.getLfu()) {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, LabelConstants.LOST_TO_FOLLOW_UP));
            status = STATUS_LFU;
        } else if (migratedFamilyBean.getOutOfState() != null && migratedFamilyBean.getOutOfState()) {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, LabelConstants.OUT_OF_STATE_TAG));
            status = STATUS_OUT_OF_STATE;
        } else if (migratedFamilyBean.getConfirmed() != null && !migratedFamilyBean.getConfirmed()) {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, LabelConstants.CONFIRMATION_PENDING));
            status = STATUS_CONFIRMATION_PENDING;
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, LabelConstants.CONFIRMED));
            status = STATUS_CONFIRMED;
        }

        if (revertRadioGroup == null) {
            revertRadioGroup = getRadioGroupYesNo();
        }

        if (!status.equals(STATUS_CONFIRMATION_PENDING)) {
            if (isOut) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.WANT_TO_BRING_BACK_FAMILY));
            } else {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.WANT_TO_RETURN_FAMILY_TO_NATIVE));
            }
            bodyLayoutContainer.addView(revertRadioGroup);
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context, LabelConstants.MIGRATION_REVRT_IMPOSSIBLE_ALERT));
            nextButton.setText(GlobalTypes.EVENT_OKAY);
            nextButton.setOnClickListener(v -> finish());
        }
    }

    private void setConfirmationScreen() {
        screenName = CONFIRMATION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        if (isOut) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.WANT_TO_BRING_BACK_FAMILY));
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.WANT_TO_RETURN_FAMILY_TO_NATIVE));
        }

        if (confirmationRadioGroup == null) {
            confirmationRadioGroup = getRadioGroupYesNo();
        }
        bodyLayoutContainer.addView(confirmationRadioGroup);
    }

    private void setFinalScreen() {
        screenName = FINAL_SCREEN;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context, LabelConstants.FORM_ENTRY_COMPLETED));
        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context, LabelConstants.FORM_ENTRY_COMPLETED_SUCCESSFULLY));
    }

    private void finishActivity() {
        View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                myAlertDialog.dismiss();
                createMigrationReturn(isOut);
                finish();
            } else {
                myAlertDialog.dismiss();
            }
        };

        myAlertDialog = new MyAlertDialog(context,
                LabelConstants.ON_MIGRATION_REVERT_SUBMISSION_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        myAlertDialog.show();
    }

    private void createMigrationReturn(Boolean isOut) {
        MigratedFamilyDataBean migratedFamilyDataBean = SewaTransformer.getInstance().convertMigratedFamilyBeanToDataBean(migratedFamilyBean);
        migratedFamilyDataBean.setOut(isOut);
        migrationService.storeRevertedFamilyMigrationBean(migratedFamilyDataBean);
        migrationService.deleteMigratedFamilyBean(migratedFamilyBean);
        finish();
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
                case FAMILY_INFO_SCREEN:
                    finish();
                    break;
                case CONFIRMATION_SCREEN:
                    setFamilyDetailsScreen();
                    break;
                case FINAL_SCREEN:
                    setConfirmationScreen();
                    break;
                default:
                    break;
            }
        }
        return true;
    }
}
