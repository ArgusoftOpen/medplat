package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.MigrationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.FamilyMigrationOutDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by prateek on 8/13/19
 */
@EActivity
public class FamilySplitActivity extends MenuActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";
    private static final String HEAD_MEMBER_SELECTION_SCREEN = "headMemberSelectionScreen";
    private static final String CONFIRMATION_SCREEN = "confirmationScreen";
    private static final String MIGRATE_QUE_SCREEN = "migrateQueScreen";
    private static final String OTHER_INFO_SCREEN = "otherInfoScreen";
    private static final String FINAL_SCREEN = "finalScreen";
    private static final int RADIO_BUTTON_ID_YES = 5001;
    private static final int RADIO_BUTTON_ID_NO = 5002;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    MigrationServiceImpl migrationService;
    private LinearLayout bodyLayoutContainer;
    private RadioGroup headRadioGroup;
    private RadioGroup confirmationRadioGroup;
    private RadioGroup migrateQueRadioGroup;
    private String screenName;
    private TextInputLayout otherInfoEditText;
    private FamilyDataBean familyToSplit;
    private List<MemberDataBean> allMembers;
    private List<MemberDataBean> selectedMembers = new ArrayList<>();
    private List<MemberDataBean> unselectedMembers = new ArrayList<>();
    private boolean headSelected;
    private MemberDataBean selectedHeadMember;
    private NotificationMobDataBean selectedNotification;
    private LinearLayout globalPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String notification = extras.getString(GlobalTypes.NOTIFICATION, null);
            if (notification != null) {
                selectedNotification = new Gson().fromJson(notification, NotificationMobDataBean.class);
            }
            String family = extras.getString("familyToSplit", null);
            familyToSplit = new Gson().fromJson(family, FamilyDataBean.class);
            allMembers = familyToSplit.getMembers();
            String memberIdsString = extras.getString("memberIds", null);
            if (memberIdsString != null) {
                List<Long> memberIds = new Gson().fromJson(memberIdsString, new TypeToken<List<Long>>() {
                }.getType());

                if (memberIds != null && !memberIds.isEmpty()) {
                    for (MemberDataBean memberDataBean : familyToSplit.getMembers()) {
                        if (memberIds.contains(Long.valueOf(memberDataBean.getId()))) {
                            selectedMembers.add(memberDataBean);
                        } else {
                            unselectedMembers.add(memberDataBean);
                        }
                    }
                }
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
        setTitle(UtilBean.getTitleText(LabelConstants.FAMILY_SPLIT_TITLE));
        setSubTitle(null);
    }

    private void initView() {
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(context, this);
        setContentView(globalPanel);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        setBodyDetail();
    }

    private void setBodyDetail() {
        if (selectedMembers != null && !selectedMembers.isEmpty()) {
            setMigrateQueScreen();
        } else {
            setMemberSelectionScreen();
        }
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
                LabelConstants.ARE_YOU_SURE_WANT_TO_CLOSE_SPLIT_FAMILY,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screenName) {
                case MEMBER_SELECTION_SCREEN:
                    if (selectedMembers.isEmpty()) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_SOME_MEMBERS_OF_THE_FAMILY));
                        return;
                    } else if (selectedMembers.size() == allMembers.size()) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.YOU_HAVE_SELECTED_ALL_THE_MEMBERS_OF_THE_FAMILY));
                        return;
                    }

                    headSelected = false;
                    for (MemberDataBean memberDataBean : selectedMembers) {
                        if (memberDataBean.getFamilyHeadFlag() != null && memberDataBean.getFamilyHeadFlag()) {
                            headSelected = true;
                            break;
                        }
                    }
                    if (headSelected) {
                        setHeadMemberSelectionScreen();
                    } else {
                        setConfirmationScreen();
                    }
                    break;

                case HEAD_MEMBER_SELECTION_SCREEN:
                    if (headRadioGroup.getCheckedRadioButtonId() == -1) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_HEAD_FOR_THE_FAMILY));
                        return;
                    }

                    selectedHeadMember = unselectedMembers.get(headRadioGroup.getCheckedRadioButtonId());
                    setConfirmationScreen();
                    break;

                case CONFIRMATION_SCREEN:
                    if (confirmationRadioGroup.getCheckedRadioButtonId() == -1) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_AN_OPTION));
                    } else if (confirmationRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
                        setMemberSelectionScreen();
                    } else if (confirmationRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                        setMigrateQueScreen();
                    }
                    break;

                case MIGRATE_QUE_SCREEN:
                    if (migrateQueRadioGroup.getCheckedRadioButtonId() == -1) {
                        SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_AN_OPTION));
                    } else if (migrateQueRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                        setOtherInfoScreen();
                    } else if (migrateQueRadioGroup.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
                        List<Long> memberIds = new ArrayList<>();
                        for (MemberDataBean memberDataBean : selectedMembers) {
                            memberIds.add(Long.valueOf(memberDataBean.getId()));
                        }
                        Intent intent = new Intent();
                        intent.putExtra("isCurrentLocation", false);
                        intent.putExtra("memberIds", new Gson().toJson(memberIds));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    break;

                case OTHER_INFO_SCREEN:
                    setFinalScreen();
                    break;

                case FINAL_SCREEN:
                    saveSplitFamily();
                    Intent intent = new Intent();
                    intent.putExtra("isCurrentLocation", true);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            selectedMembers.add(allMembers.get(buttonView.getId()));
        } else {
            selectedMembers.remove(allMembers.get(buttonView.getId()));
        }
    }

    private void setMemberSelectionScreen() {
        screenName = MEMBER_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(
                null, null, context,
                LabelConstants.SELECT_THE_MEMBERS_WHOM_YOU_WANT_TO_MIGRATE));

        int count = 0;
        if (allMembers != null && !allMembers.isEmpty()) {
            for (final MemberDataBean memberDataBean : allMembers) {
                CheckBox checkBox = MyStaticComponents.getCheckBox(context,
                        memberDataBean.getUniqueHealthId() + " - " + UtilBean.getMemberFullName(memberDataBean),
                        count, false);

                if (selectedMembers.contains(memberDataBean)) {
                    checkBox.setChecked(true);
                } else {
                    if (!unselectedMembers.contains(memberDataBean)) {
                        unselectedMembers.add(memberDataBean);
                    }
                }

                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        unselectedMembers.remove(memberDataBean);
                        if (!selectedMembers.contains(memberDataBean)) {
                            selectedMembers.add(memberDataBean);
                        }
                    } else {
                        selectedMembers.remove(memberDataBean);
                        if (!unselectedMembers.contains(memberDataBean)) {
                            unselectedMembers.add(memberDataBean);
                        }
                    }
                });

                bodyLayoutContainer.addView(checkBox);
            }
        } else {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context,
                    LabelConstants.THERE_ARE_NO_MEMBERS_IN_THIS_FAMILY));
        }
    }

    private void setHeadMemberSelectionScreen() {
        screenName = HEAD_MEMBER_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context,
                LabelConstants.SELECTED_TO_MOVE_THE_HEAD_FROM_THE_FAMILY_TO_NEW_SPLIT_FAMILY + familyToSplit.getFamilyId()));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                LabelConstants.SELECT_A_NEW_HEAD_FOR_THE_FAMILY));

        headRadioGroup = new RadioGroup(context);
        int counter = 0;
        for (MemberDataBean memberDataBean : unselectedMembers) {
            RadioButton radioButton = MyStaticComponents.getRadioButton(context,
                    memberDataBean.getUniqueHealthId() + " - " + UtilBean.getMemberFullName(memberDataBean),
                    counter);
            if (headSelected && selectedHeadMember != null && selectedHeadMember.getId().equals(memberDataBean.getId())) {
                radioButton.setChecked(true);
            }
            headRadioGroup.addView(radioButton);
            counter++;
        }

        bodyLayoutContainer.addView(headRadioGroup);
    }

    private void setConfirmationScreen() {
        screenName = CONFIRMATION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context, UtilBean.getMyLabel(LabelConstants.FAMILY_DETAILS)));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.FAMILY_ID)));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, familyToSplit.getFamilyId()));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.MEMBER_DETAILS)));
        for (MemberDataBean memberDataBean : unselectedMembers) {
            MaterialTextView textView = MyStaticComponents.generateAnswerView(
                    context, memberDataBean.getUniqueHealthId() + " - " + UtilBean.getMemberFullName(memberDataBean));
            if (headSelected && memberDataBean.getId().equals(selectedHeadMember.getId())) {
                textView.setTextColor(Color.rgb(48, 112, 6));
            }
            bodyLayoutContainer.addView(textView);
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context, UtilBean.getMyLabel(LabelConstants.NEW_SPLIT_FAMILY_WILL_BE_CREATED_WITH_THESE_MEMBERS)));
        for (MemberDataBean memberDataBean : selectedMembers) {
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(
                    context, memberDataBean.getUniqueHealthId() + " - " + UtilBean.getMemberFullName(memberDataBean)));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(
                null, null, context, UtilBean.getMyLabel(LabelConstants.SURE_WANT_TO_CONTINUE_SPLITTING_THIS_FAMILY)));

        if (confirmationRadioGroup == null) {
            HashMap<Integer, String> stringMap = new HashMap<>();
            stringMap.put(RADIO_BUTTON_ID_YES, LabelConstants.YES);
            stringMap.put(RADIO_BUTTON_ID_NO, LabelConstants.NO);
            confirmationRadioGroup = MyStaticComponents.getRadioGroup(this, stringMap, true);
        }

        bodyLayoutContainer.addView(confirmationRadioGroup);

    }

    private void setMigrateQueScreen() {
        screenName = MIGRATE_QUE_SCREEN;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(
                null, null, context, UtilBean.getMyLabel(LabelConstants.WHAT_DO_YOU_WANT_TO_DO_WITH_THIS_NEW_FAMILY)
        ));

        if (migrateQueRadioGroup == null) {
            HashMap<Integer, String> stringMap = new HashMap<>();
            stringMap.put(RADIO_BUTTON_ID_YES, LabelConstants.KEEP_THIS_FAMILY_IN_THE_CURRENT_LOCATION);
            stringMap.put(RADIO_BUTTON_ID_NO, LabelConstants.MIGRATE_THIS_FAMILY_TO_A_NEW_LOCATION);
            migrateQueRadioGroup = MyStaticComponents.getRadioGroup(this, stringMap, false);
        }

        bodyLayoutContainer.addView(migrateQueRadioGroup);
    }

    private void setOtherInfoScreen() {
        screenName = OTHER_INFO_SCREEN;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.OTHER_INFORMATION)));
        if (otherInfoEditText == null) {
            otherInfoEditText = MyStaticComponents.getEditText(context, LabelConstants.OTHER_INFORMATION, 1000, 500, -1);
        }
        bodyLayoutContainer.addView(otherInfoEditText);
    }

    private void setFinalScreen() {
        screenName = FINAL_SCREEN;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateSubTitleView(context, LabelConstants.FORM_ENTRY_COMPLETED));
        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context, LabelConstants.FORM_ENTRY_COMPLETED_SUCCESSFULLY));
    }

    private void saveSplitFamily() {
        List<Long> memberIds = new ArrayList<>();

        for (MemberDataBean memberDataBean : selectedMembers) {
            memberIds.add(Long.valueOf(memberDataBean.getId()));
        }

        FamilyMigrationOutDataBean migration = new FamilyMigrationOutDataBean();
        if (familyToSplit.getId() != null) {
            migration.setFamilyId(Long.valueOf(familyToSplit.getId()));
        }
        migration.setCurrentLocation(true);
        migration.setSplit(true);
        migration.setMemberIds(memberIds);
        migration.setReportedOn(new Date().getTime());

        if (headSelected && selectedHeadMember != null) {
            migration.setNewHead(Long.parseLong(selectedHeadMember.getId()));
        }

        migrationService.createFamilyMigrationOut(migration, familyToSplit, selectedNotification);
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
                case MEMBER_SELECTION_SCREEN:
                    setResult(RESULT_CANCELED);
                    finish();
                    break;

                case HEAD_MEMBER_SELECTION_SCREEN:
                    selectedHeadMember = null;
                    headSelected = false;
                    setMemberSelectionScreen();
                    break;

                case CONFIRMATION_SCREEN:
                    if (headSelected) {
                        setHeadMemberSelectionScreen();
                    } else {
                        setMemberSelectionScreen();
                    }
                    break;

                case MIGRATE_QUE_SCREEN:
                    setConfirmationScreen();
                    break;

                case OTHER_INFO_SCREEN:
                    setMigrateQueScreen();
                    break;

                case FINAL_SCREEN:
                    setOtherInfoScreen();
                    break;

                default:
                    break;
            }
        }
        return true;
    }
}
