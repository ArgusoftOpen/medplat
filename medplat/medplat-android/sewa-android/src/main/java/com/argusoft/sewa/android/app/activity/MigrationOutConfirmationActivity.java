package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.MigrationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.MigrationDetailsDataBean;
import com.argusoft.sewa.android.app.databean.MigrationOutConfirmationDataBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.gson.Gson;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.HashMap;

import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * Created by prateek on 18/3/19.
 */

@EActivity
public class MigrationOutConfirmationActivity extends MenuActivity implements View.OnClickListener {

    private static final String MEMBER_INFO_SCREEN = "memberInfoScreen";
    private static final String CONFIRMATION_SCREEN = "confirmationScreen";
    private static final String FINAL_SCREEN = "finalScreen";
    private static final Integer RADIO_BUTTON_ID_YES = 1;
    private static final Integer RADIO_BUTTON_ID_NO = 2;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    SewaServiceImpl sewaService;
    @Bean
    MigrationServiceImpl migrationService;
    private LinearLayout bodyLayoutContainer;
    private MyAlertDialog myAlertDialog;

    private NotificationMobDataBean notificationMobDataBean;
    private MigrationDetailsDataBean migrationDetailsDataBean;

    private String screenName = null;
    private RadioGroup radioGroupForMemberQue;
    private RadioGroup radioGroupForConfirmation;
    private LinearLayout globalPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String notificationBeanJson = extras.getString(GlobalTypes.NOTIFICATION_BEAN, null);
            notificationMobDataBean = new Gson().fromJson(notificationBeanJson, NotificationMobDataBean.class);
            migrationDetailsDataBean = new Gson().fromJson(notificationMobDataBean.getOtherDetails(), MigrationDetailsDataBean.class);
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
        setTitle(UtilBean.getTitleText(LabelConstants.MIGRATION_CONFIRMATION_TITLE));
    }

    private void initView() {
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        setContentView(globalPanel);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        setMemberInfoScreen();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screenName) {
                case MEMBER_INFO_SCREEN:
                    if (radioGroupForMemberQue.getCheckedRadioButtonId() == -1) {
                        SewaUtil.generateToast(context, "Please select an option");
                        break;
                    }
                    setConfirmationScreen();
                    break;

                case CONFIRMATION_SCREEN:
                    if (radioGroupForConfirmation.getCheckedRadioButtonId() == -1) {
                        SewaUtil.generateToast(context, "Please select an option");
                        break;
                    }

                    if (radioGroupForConfirmation.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                        setFinalScreen();
                        break;
                    }

                    if (radioGroupForConfirmation.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
                        setMemberInfoScreen();
                        break;
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

        myAlertDialog = new MyAlertDialog(context,
                GlobalTypes.MSG_CANCEL_MIGRATION,
                myListener, DynamicUtils.BUTTON_YES_NO);
        myAlertDialog.show();
    }

    private void setMemberInfoScreen() {
        bodyLayoutContainer.removeAllViews();
        screenName = MEMBER_INFO_SCREEN;
        setSubTitle(migrationDetailsDataBean.getFirstName() + " " + migrationDetailsDataBean.getLastName());

        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, "You have to migrate out this person"));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, "Member name"));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                migrationDetailsDataBean.getFirstName() + " " + migrationDetailsDataBean.getMiddleName() + " " + migrationDetailsDataBean.getLastName()));
        if (migrationDetailsDataBean.getHealthId() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, "Member Health Id"));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    migrationDetailsDataBean.getHealthId()));
        }
        if (migrationDetailsDataBean.getFamilyId() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, "Family Migrated to"));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    migrationDetailsDataBean.getFamilyId()));
        }
        if (migrationDetailsDataBean.getLocationDetails() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, "Location Migrated To"));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    migrationDetailsDataBean.getLocationDetails()));
        }
        if (migrationDetailsDataBean.getFhwName() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, "FHW Name"));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    migrationDetailsDataBean.getFhwName()));
        }
        if (migrationDetailsDataBean.getFhwPhoneNumber() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, "FHW Contact Number"));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    migrationDetailsDataBean.getFhwPhoneNumber()));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, "Do you want to migrate out this member?"));

        if (radioGroupForMemberQue == null) {
            radioGroupForMemberQue = getRadioGroupYesNo();
        }
        bodyLayoutContainer.addView(radioGroupForMemberQue);
    }

    private void setConfirmationScreen() {
        screenName = CONFIRMATION_SCREEN;

        if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, "Are you sure this beneficiary has been migrated?"));
        }

        if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this, "Are you sure this beneficiary has not been migrated?"));
        }

        if (radioGroupForConfirmation == null) {
            radioGroupForConfirmation = getRadioGroupYesNo();
        }
        bodyLayoutContainer.addView(radioGroupForConfirmation);
    }

    private RadioGroup getRadioGroupYesNo() {
        HashMap<Integer, String> stringMap = new HashMap<>();
        stringMap.put(RADIO_BUTTON_ID_YES, "Yes");
        stringMap.put(RADIO_BUTTON_ID_NO, "No");
        return MyStaticComponents.getRadioGroup(this, stringMap, true);
    }

    private void setFinalScreen() {
        bodyLayoutContainer.removeAllViews();
        screenName = FINAL_SCREEN;

        if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this,
                    UtilBean.getMyLabel("You have successfully migrated out a beneficiary. Thank you, form is complete.")));
        }

        if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this,
                    UtilBean.getMyLabel("You have selected not to migrate out a beneficiary. Thank you, form is complete.")));
        }
    }

    private void finishActivity() {
        View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                myAlertDialog.dismiss();
                createMigrationOutConfirmation();
                finish();
            } else {
                myAlertDialog.dismiss();
            }
        };

        myAlertDialog = new MyAlertDialog(context,
                "Are you sure want to submit this migration confirmation form?",
                myListener, DynamicUtils.BUTTON_YES_NO);
        myAlertDialog.show();
    }

    private void createMigrationOutConfirmation() {
        MigrationOutConfirmationDataBean migrationOutConfirmation = new MigrationOutConfirmationDataBean();
        migrationOutConfirmation.setNotificationId(notificationMobDataBean.getId());
        migrationOutConfirmation.setMigrationId(notificationMobDataBean.getMigrationId());
        migrationOutConfirmation.setMemberId(notificationMobDataBean.getMemberId());
        if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
            migrationOutConfirmation.setHasMigrationHappened(Boolean.TRUE);
        }
        if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
            migrationOutConfirmation.setHasMigrationHappened(Boolean.FALSE);
        }

        migrationService.createMigrationOutConfirmation(migrationOutConfirmation, migrationDetailsDataBean);
        sewaService.deleteNotificationByNotificationId(notificationMobDataBean.getId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(screenName == null || screenName.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            switch (screenName) {
                case MEMBER_INFO_SCREEN:
                    onBackPressed();
                    break;

                case CONFIRMATION_SCREEN:
                    setMemberInfoScreen();
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
