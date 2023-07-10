package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.MigrationServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.MigrationDetailsDataBean;
import com.argusoft.sewa.android.app.databean.MigrationInConfirmationDataBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * Created by prateek on 18/3/19.
 */

@EActivity
public class MigrationInConfirmationActivity extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    SewaServiceImpl sewaService;
    @Bean
    MigrationServiceImpl migrationService;

    private static final String MEMBER_INFO_SCREEN = "memberInfoScreen";
    private static final String FAMILY_SELECTION_SCREEN = "familySelectionScreen";
    private static final String CONFIRMATION_SCREEN = "confirmationScreen";
    private static final String FINAL_SCREEN = "finalScreen";
    private static final Integer RADIO_BUTTON_ID_YES = 1;
    private static final Integer RADIO_BUTTON_ID_NO = 2;
    private static final Integer RADIO_BUTTON_ID_NOT_FOUND = 3;
    private static final long DELAY = 500;

    private Timer timer = new Timer();

    private LinearLayout bodyLayoutContainer;
    private MyAlertDialog myAlertDialog;

    private NotificationMobDataBean notificationMobDataBean;
    private MigrationDetailsDataBean migrationDetailsDataBean;

    private String screenName = null;
    private RadioGroup radioGroupForMemberQue;
    private TextInputLayout searchFamilyEditText;
    private List<FamilyDataBean> familyDataBeans = null;
    private Map<String, MemberDataBean> headMembersMapWithFamilyIdAsKey;
    private FamilyDataBean selectedFamily;
    private RadioGroup radioGroupForConfirmation;
    private LinearLayout globalPanel;
    private int selectedFamilyIndex = -1;
    private PagingListView paginatedListView;
    private MaterialTextView noFamilyTextView;
    private MaterialButton nextButton;
    private long offset;
    private long limit = 30;
    private String searchString;

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
        setTitle(UtilBean.getTitleText(LabelConstants.MIGRATION_CONFIRMATION_TITLE));
    }

    private void initView() {
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        setContentView(globalPanel);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
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

                    if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                        bodyLayoutContainer.removeAllViews();
                        addSearchTextBoxForFamily();
                        if (searchFamilyEditText.getEditText() != null && searchFamilyEditText.getEditText().getText().length() > 0
                                && familyDataBeans != null && !familyDataBeans.isEmpty()) {
                            addRadioGroupForFamilySelection();
                        }
                        break;
                    }

                    if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
                        setConfirmationScreen();
                        break;
                    }

                    if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NOT_FOUND) {
                        addFinalScreen();
                        break;
                    }
                    break;

                case FAMILY_SELECTION_SCREEN:
                    if (searchFamilyEditText.getEditText() == null || searchFamilyEditText.getEditText().getText().toString().trim().length() == 0) {
                        SewaUtil.generateToast(context, "Please search for a family");
                        break;
                    }
                    if (familyDataBeans == null || familyDataBeans.isEmpty()) {
                        finish();
                        return;
                    } else if (selectedFamilyIndex == -1) {
                        SewaUtil.generateToast(context, "Please select a family");
                        break;
                    }

                    selectedFamily = familyDataBeans.get(selectedFamilyIndex);
                    setConfirmationScreen();
                    break;

                case CONFIRMATION_SCREEN:
                    if (radioGroupForConfirmation.getCheckedRadioButtonId() == -1) {
                        SewaUtil.generateToast(context, "Please select an option");
                        break;
                    }

                    if (radioGroupForConfirmation.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                        addFinalScreen();
                        break;
                    }

                    if (radioGroupForConfirmation.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
                        if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                            bodyLayoutContainer.removeAllViews();
                            addSearchTextBoxForFamily();
                            if (searchFamilyEditText.getEditText() != null && searchFamilyEditText.getEditText().getText().length() > 0
                                    && familyDataBeans != null && !familyDataBeans.isEmpty()) {
                                addRadioGroupForFamilySelection();
                            }
                            break;
                        }
                        if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
                            setMemberInfoScreen();
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
        screenName = MEMBER_INFO_SCREEN;
        bodyLayoutContainer.removeAllViews();

        bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this,
                UtilBean.getMyLabel("You have to identify this person in your village and select a family for the same")));
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel("Member name")));
        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                migrationDetailsDataBean.getFirstName() + " " + migrationDetailsDataBean.getMiddleName() + " " + migrationDetailsDataBean.getLastName()));
        if (migrationDetailsDataBean.getHealthId() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel("Member Health Id")));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    migrationDetailsDataBean.getHealthId()));
        }
        if (migrationDetailsDataBean.getChildDetails() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel("Children migrated with this member")));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    migrationDetailsDataBean.getChildDetails()));
        }
        if (migrationDetailsDataBean.getFamilyId() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel("Family Migrated From")));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    migrationDetailsDataBean.getFamilyId()));
        }
        if (migrationDetailsDataBean.getLocationDetails() != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel("Location Migrated From")));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    migrationDetailsDataBean.getLocationDetails()));
        }
        if (migrationDetailsDataBean.getFhwName() != null && !migrationDetailsDataBean.getFhwName().isEmpty()) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel("FHW Name")));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    migrationDetailsDataBean.getFhwName()));
        }
        if (migrationDetailsDataBean.getFhwPhoneNumber() != null && !migrationDetailsDataBean.getFhwPhoneNumber().isEmpty()) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel("FHW Contact Number")));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    migrationDetailsDataBean.getFhwPhoneNumber()));
        }
        if (migrationDetailsDataBean.getOtherInfo() != null && !migrationDetailsDataBean.getOtherInfo().isEmpty()) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel("Other Info")));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(this,
                    migrationDetailsDataBean.getOtherInfo()));
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                UtilBean.getMyLabel("Do you want to migrate in this member") + "?"));

        if (radioGroupForMemberQue == null) {
            radioGroupForMemberQue = new RadioGroup(this);
            radioGroupForMemberQue.addView(MyStaticComponents.getRadioButton(this,
                    UtilBean.getMyLabel("Yes"), RADIO_BUTTON_ID_YES));
            radioGroupForMemberQue.addView(MyStaticComponents.getRadioButton(this,
                    UtilBean.getMyLabel("No"), RADIO_BUTTON_ID_NO));
            radioGroupForMemberQue.addView(MyStaticComponents.getRadioButton(this,
                    UtilBean.getMyLabel("Member not found yet"), RADIO_BUTTON_ID_NOT_FOUND));
        }
        bodyLayoutContainer.addView(radioGroupForMemberQue);
    }

    private void addSearchTextBoxForFamily() {
        screenName = FAMILY_SELECTION_SCREEN;
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, "Search Family in which the beneficiary has migrated"));

        if (searchFamilyEditText == null) {
            searchFamilyEditText = MyStaticComponents.getEditText(this, "Search Family", 1, RESULT_OK, 1);

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
                                                retrieveFamilyListFromDB(s.toString());
                                                searchFamilyEditText.setFocusable(true);
                                            });
                                        } else if (s != null && s.length() == 0) {
                                            runOnUiThread(() -> bodyLayoutContainer.removeView(paginatedListView));
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

        bodyLayoutContainer.addView(searchFamilyEditText);
    }

    @Background
    public void retrieveFamilyListFromDB(String search) {
        searchString = search;
        selectedFamilyIndex = -1;
        offset = 0;
        familyDataBeans = migrationService.searchFamilyByLocation(search, notificationMobDataBean.getLocationId(), limit, offset);
        addRadioGroupForFamilySelection();
    }

    @UiThread
    public void addRadioGroupForFamilySelection() {
        bodyLayoutContainer.removeView(paginatedListView);
        bodyLayoutContainer.removeView(noFamilyTextView);

        if (familyDataBeans != null && !familyDataBeans.isEmpty()) {
            List<ListItemDataBean> familyList = getList(familyDataBeans);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedFamilyIndex = position;
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, familyList, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            noFamilyTextView = MyStaticComponents.generateInstructionView(this, "There are no families.");
            bodyLayoutContainer.addView(noFamilyTextView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }
    }

    private List<ListItemDataBean> getList(List<FamilyDataBean> familyDataBeans) {
        List<String> familyIds = new ArrayList<>();
        List<ListItemDataBean> list = new ArrayList<>();
        String rbText;

        for (FamilyDataBean familyDataBean : familyDataBeans) {
            familyIds.add(familyDataBean.getFamilyId());
        }
        headMembersMapWithFamilyIdAsKey = fhsService.retrieveHeadMemberDataBeansByFamilyId(familyIds);

        for (FamilyDataBean familyDataBean : familyDataBeans) {
            MemberDataBean headMember = headMembersMapWithFamilyIdAsKey.get(familyDataBean.getFamilyId());
            if (headMember != null) {
                familyDataBean.setHeadMemberName(headMember.getFirstName() + " " + headMember.getMiddleName() + " " + headMember.getGrandfatherName() + " " + headMember.getLastName());
                familyDataBean.setHeadMemberName(familyDataBean.getHeadMemberName().replace("null", ""));
                rbText = headMember.getFirstName() + " " + headMember.getMiddleName() + " " + headMember.getGrandfatherName() + " " + headMember.getLastName();
                rbText = rbText.replace(" null", "");
            } else {
                rbText = familyDataBean.getFamilyId() + " - " + UtilBean.getMyLabel("Head name not available");
            }
            list.add(new ListItemDataBean(null, familyDataBean.getFamilyId(), null, null, rbText));
        }
        return list;
    }

    private void setConfirmationScreen() {
        screenName = CONFIRMATION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                    UtilBean.getMyLabel("Family ID")));
            bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context, selectedFamily.getFamilyId()));

            MemberDataBean tmpMemberDataBean = headMembersMapWithFamilyIdAsKey.get(selectedFamily.getFamilyId());
            if (tmpMemberDataBean != null) {
                bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                        UtilBean.getMyLabel("Head of the family")));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                        UtilBean.getMemberFullName(tmpMemberDataBean)));
            }

            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel("Are you sure want to migrate the beneficiary to this family" + "?")));
        }

        if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
            bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel("Are you sure this member has not been migrated in to your village")));
        }

        if (radioGroupForConfirmation == null) {
            radioGroupForConfirmation = new RadioGroup(this);
            radioGroupForConfirmation.addView(MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel("Yes"), RADIO_BUTTON_ID_YES));
            radioGroupForConfirmation.addView(MyStaticComponents.getRadioButton(this, UtilBean.getMyLabel("No"), RADIO_BUTTON_ID_NO));
        }

        bodyLayoutContainer.addView(radioGroupForConfirmation);
    }


    private void addFinalScreen() {
        screenName = FINAL_SCREEN;
        bodyLayoutContainer.removeAllViews();

        if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this,
                    UtilBean.getMyLabel("You have successfully migrated in a beneficiary. Thank you, form is complete.")));
        }
        if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this,
                    UtilBean.getMyLabel("You have selected not to migrate in a beneficiary. Thank you, form is complete.")));
        }
        if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NOT_FOUND) {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this,
                    UtilBean.getMyLabel("Please come back when you find the beneficiary. Form is complete.")));
        }
    }

    private void finishActivity() {
        if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NOT_FOUND) {
            finish();
        } else {
            View.OnClickListener myListener = v -> {
                if (v.getId() == BUTTON_POSITIVE) {
                    myAlertDialog.dismiss();
                    createMigrationConfirmationBean();
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
    }

    private void createMigrationConfirmationBean() {
        MigrationInConfirmationDataBean migrationInConfirmationDataBean = new MigrationInConfirmationDataBean();
        migrationInConfirmationDataBean.setNotificationId(notificationMobDataBean.getId());
        migrationInConfirmationDataBean.setMigrationId(notificationMobDataBean.getMigrationId());
        migrationInConfirmationDataBean.setMemberId(notificationMobDataBean.getMemberId());
        if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
            migrationInConfirmationDataBean.setHasMigrationHappened(Boolean.TRUE);
            migrationInConfirmationDataBean.setFamilyMigratedTo(Long.valueOf(selectedFamily.getId()));
        }
        if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
            migrationInConfirmationDataBean.setHasMigrationHappened(Boolean.FALSE);
        }

        migrationService.createMigrationInConfirmation(migrationInConfirmationDataBean, migrationDetailsDataBean);
        sewaService.deleteNotificationByNotificationId(notificationMobDataBean.getId());
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
                case MEMBER_INFO_SCREEN:
                    onBackPressed();
                    break;

                case FAMILY_SELECTION_SCREEN:
                    setMemberInfoScreen();
                    break;

                case CONFIRMATION_SCREEN:
                    selectedFamilyIndex = -1;
                    if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_YES) {
                        bodyLayoutContainer.removeAllViews();
                        addSearchTextBoxForFamily();
                        if (searchFamilyEditText.getEditText() != null && searchFamilyEditText.getEditText().getText().length() > 0
                                && familyDataBeans != null && !familyDataBeans.isEmpty()) {
                            addRadioGroupForFamilySelection();
                        }
                        break;
                    }
                    if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NO) {
                        setMemberInfoScreen();
                        break;
                    }
                    break;
                case FINAL_SCREEN:
                    if (radioGroupForMemberQue.getCheckedRadioButtonId() == RADIO_BUTTON_ID_NOT_FOUND) {
                        setMemberInfoScreen();
                    } else {
                        setConfirmationScreen();
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public void onLoadMoreItems() {
        onLoadMoreBackground();
    }

    @Background
    public void onLoadMoreBackground() {
        List<FamilyDataBean> famDataBeans = migrationService.searchFamilyByLocation(searchString, notificationMobDataBean.getLocationId(), limit, offset);
        offset = offset + limit;
        onLoadMoreUi(famDataBeans);
    }

    @UiThread
    public void onLoadMoreUi(List<FamilyDataBean> familyDataBeansList) {
        if (!familyDataBeansList.isEmpty()) {
            List<ListItemDataBean> items = getList(familyDataBeansList);
            familyDataBeans.addAll(familyDataBeansList);
            paginatedListView.onFinishLoadingWithItem(true, items);
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
        }
    }
}
