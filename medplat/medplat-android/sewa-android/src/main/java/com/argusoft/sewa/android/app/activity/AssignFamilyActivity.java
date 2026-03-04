package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.listeners.FamilyIdTextWatcher;
import com.argusoft.sewa.android.app.component.listeners.MemberIdTextWatcher;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceRestClientImpl;
import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.restclient.RestHttpException;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EActivity
public class AssignFamilyActivity extends MenuActivity implements View.OnClickListener {

    private List<LocationBean> locationBeans = new ArrayList<>();
    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    SewaServiceRestClientImpl sewaServiceRestClient;
    private FamilyDataBean familyDataBean;
    private LinearLayout familyDetailsLayout;
    private MyAlertDialog myAlertDialog;
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private Spinner spinner;
    private boolean isVillageSelectionScreen = Boolean.FALSE;
    private boolean isFamilySelectionScreen = Boolean.FALSE;
    private boolean isFamilyAssignedScreen = Boolean.FALSE;
    private String locationId;
    private String response;
    private int checkedRadioButtonId;
    private String errorString;
    private LinearLayout globalPanel;
    private LinearLayout footerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        initView();
    }

    private void initView() {
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        footerView = globalPanel.findViewById(DynamicUtils.ID_FOOTER);

        if (!sewaService.isOnline()) {
            showNotOnlineMessage();
            return;
        }

        setBodyDetail();
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
        setTitle(LabelConstants.ASSIGN_FAMILY_TITLE);
    }

    @Background
    public void setBodyDetail() {
        locationBeans = fhsService.getDistinctLocationsAssignedToUser();
        addSearchScreen();
        isFamilySelectionScreen = Boolean.TRUE;
    }

    @Override
    public void onClick(View v) {
        if (!sewaService.isOnline()) {
            showNotOnlineMessage();
            return;
        }
        setSubTitle(null);
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            if (isVillageSelectionScreen) {
                String selectedVillage = spinner.getSelectedItem().toString();
                for (LocationBean locationBean : locationBeans) {
                    if (selectedVillage.equals(locationBean.getName())) {
                        locationId = String.valueOf(locationBean.getActualID());
                        break;
                    }
                }
                bodyLayoutContainer.removeAllViews();
                assignFamilyToUserFromNetwork();
                isVillageSelectionScreen = Boolean.FALSE;
            } else if (isFamilySelectionScreen) {
                if (familyDataBean == null) {
                    SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.SEARCH_FOR_FAMILY_REQUIRED_ALERT));
                } else {
                    bodyLayoutContainer.removeAllViews();
                    isFamilySelectionScreen = Boolean.FALSE;
                    isVillageSelectionScreen = Boolean.TRUE;
                    addVillageSelectionSpinner();
                }
            } else {
                navigateToHomeScreen(false);
            }
        }
    }

    @Background
    public void assignFamilyToUserFromNetwork() {
        showProcessDialog();
        response = fhsService.assignFamilyToUser(locationId, familyDataBean);
        runOnUiThread(() -> {
            isFamilySelectionScreen = Boolean.FALSE;
            isFamilyAssignedScreen = Boolean.TRUE;
            bodyLayoutContainer.removeAllViews();
            MaterialTextView textView = MyStaticComponents.generateInstructionView(AssignFamilyActivity.this, response);
            textView.setGravity(Gravity.CENTER);
            bodyLayoutContainer.addView(textView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
            nextButton.setOnClickListener(v -> navigateToHomeScreen(false));
            hideProcessDialog();
        });
    }

    @UiThread
    public void addSearchScreen() {
        footerView.setVisibility(View.GONE);
        final TextInputLayout editText = MyStaticComponents.getEditText(this, LabelConstants.FAMILY_ID, 102, 22, InputType.TYPE_CLASS_TEXT | TYPE_TEXT_FLAG_CAP_CHARACTERS);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                dismissKeyboard();
            }
        });

        if (editText.getEditText() != null) {
            editText.getEditText().addTextChangedListener(new FamilyIdTextWatcher(editText.getEditText()));
        }

        final TextInputLayout editText1 = MyStaticComponents.getEditText(AssignFamilyActivity.this, LabelConstants.MEMBER_ID, 103, RESULT_OK, InputType.TYPE_CLASS_TEXT | TYPE_TEXT_FLAG_CAP_CHARACTERS);
        editText1.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                dismissKeyboard();
            }
        });

        if (editText1.getEditText() != null) {
            editText1.getEditText().addTextChangedListener(new MemberIdTextWatcher(editText1.getEditText()));
        }

        final MaterialButton searchButton = MyStaticComponents.getCustomButton(AssignFamilyActivity.this, UtilBean.getMyLabel(LabelConstants.SEARCH),
                104, null);

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.SEARCH_FOR_FAMILY)));

        final RadioGroup radioGroup = new RadioGroup(this);
        RadioButton radioButton = MyStaticComponents.getRadioButton(this, LabelConstants.SEARCH_WITH_FAMILY_ID, 10);
        radioButton.setChecked(true);
        RadioButton radioButton1 = MyStaticComponents.getRadioButton(this, LabelConstants.SEARCH_WITH_MEMBER_ID, 11);
        radioGroup.addView(radioButton);
        radioGroup.addView(radioButton1);
        bodyLayoutContainer.addView(radioGroup);
        bodyLayoutContainer.addView(editText);
        bodyLayoutContainer.addView(searchButton);
        checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            familyDataBean = null;
            checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            bodyLayoutContainer.removeView(editText);
            bodyLayoutContainer.removeView(editText1);
            bodyLayoutContainer.removeView(searchButton);
            bodyLayoutContainer.removeView(familyDetailsLayout);

            if (checkedRadioButtonId == 10) {
                bodyLayoutContainer.addView(editText);
                bodyLayoutContainer.addView(searchButton);
            } else if (checkedRadioButtonId == 11) {
                bodyLayoutContainer.addView(editText1);
                bodyLayoutContainer.addView(searchButton);
            }
        });

        searchButton.setOnClickListener(v -> {
            if (!sewaService.isOnline()) {
                showNotOnlineMessage();
                return;
            }
            showProcessDialog();
            bodyLayoutContainer.removeView(familyDetailsLayout);
            familyDataBean = null;
            if (checkedRadioButtonId == 10) {
                if (editText.getEditText().getText().toString().trim().length() > 3) {
                    String familyId = editText.getEditText().getText().toString().toUpperCase();
                    getFamilyDataBeanFromServer(familyId, true);
                } else {
                    dismissKeyboard();
                    SewaUtil.generateToast(AssignFamilyActivity.this, UtilBean.getMyLabel(LabelConstants.FAMILY_ID_REQUIRED_ALERT));
                }
            } else if (checkedRadioButtonId == 11) {
                if (editText1.getEditText().getText().toString().trim().length() > 1) {
                    String memberId = editText1.getEditText().getText().toString().toUpperCase();
                    getFamilyDataBeanFromServer(memberId, false);
                } else {
                    dismissKeyboard();
                    SewaUtil.generateToast(AssignFamilyActivity.this, UtilBean.getMyLabel(LabelConstants.MEMBER_ID_REQUIRED_ALERT));
                }
            } else {
                SewaUtil.generateToast(AssignFamilyActivity.this, UtilBean.getMyLabel(LabelConstants.OPTION_SELECTION_REQUIRED_TO_SEARCH_ALERT));
            }
            hideProcessDialog();
        });
    }

    @Background
    public void getFamilyDataBeanFromServer(String searchString, Boolean searchByFamilyId) {
        try {
            Map<String, FamilyDataBean> map = sewaServiceRestClient.getFamilyToBeAssignedBySearchString(searchString, searchByFamilyId);
            if (map != null && !map.isEmpty()) {
                for (Map.Entry<String, FamilyDataBean> entry : map.entrySet()) {
                    errorString = entry.getKey();
                    familyDataBean = entry.getValue();
                }
                showFamilyDetails();
            } else {
                dismissKeyboard();
                runOnUiThread(() -> {
                    SewaUtil.generateToast(AssignFamilyActivity.this, UtilBean.getMyLabel(LabelConstants.FAMILY_NOT_FOUND));
                    processDialog.dismiss();
                });
            }
        } catch (RestHttpException e) {
            Log.e(getClass().getName(), null, e);
            dismissKeyboard();
            runOnUiThread(() -> {
                SewaUtil.generateToast(AssignFamilyActivity.this, UtilBean.getMyLabel(LabelConstants.FAMILY_NOT_FOUND));
                processDialog.dismiss();
            });
        }
    }

    @UiThread
    public void showFamilyDetails() {
        dismissKeyboard();
        if (familyDataBean != null) {
            familyDetailsLayout = MyStaticComponents.getLinearLayout(AssignFamilyActivity.this, 105,
                    LinearLayout.VERTICAL, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            familyDetailsLayout.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel(LabelConstants.FAMILY_ID)));
            familyDetailsLayout.addView(MyStaticComponents.generateAnswerView(this, familyDataBean.getFamilyId()));
            familyDetailsLayout.addView(MyStaticComponents.generateQuestionView(null, null, this,
                    UtilBean.getMyLabel(LabelConstants.MEMBERS_INFO)));
            familyDetailsLayout.addView(UtilBean.getMembersListForDisplay(this, familyDataBean));
            bodyLayoutContainer.addView(familyDetailsLayout);
            footerView.setVisibility(View.VISIBLE);
        } else {
            SewaUtil.generateToast(this, UtilBean.getMyLabel(errorString));
        }
        processDialog.dismiss();
    }

    private void dismissKeyboard() {
        View view = getWindow().getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void addVillageSelectionSpinner() {
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.SELECT_VILLAGE)));
        String[] arrayOfOptions = new String[locationBeans.size()];
        int i = 0;
        for (LocationBean locationBean : locationBeans) {
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }
        spinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 2);
        bodyLayoutContainer.addView(spinner);
    }

    @UiThread
    public void showNotOnlineMessage() {
        View.OnClickListener myListener = v -> {
            myAlertDialog.dismiss();
            navigateToHomeScreen(false);
        };
        myAlertDialog = new MyAlertDialog(AssignFamilyActivity.this,
                UtilBean.getMyLabel(LabelConstants.NETWORK_CONNECTIVITY_ALERT),
                myListener, DynamicUtils.BUTTON_OK);
        myAlertDialog.show();
        myAlertDialog.setOnCancelListener(dialog -> {
            myAlertDialog.dismiss();
            navigateToHomeScreen(false);
        });
    }

    @Override
    public void onBackPressed() {
        View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                myAlertDialog.dismiss();
                navigateToHomeScreen(false);
                finish();
            } else {
                myAlertDialog.dismiss();
            }
        };

        myAlertDialog = new MyAlertDialog(this,
                LabelConstants.ON_ASSIGN_FAMILY_CLOSE_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        myAlertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            if (isFamilySelectionScreen) {
                navigateToHomeScreen(false);
            } else if (isFamilyAssignedScreen) {
                familyDataBean = null;
                bodyLayoutContainer.removeAllViews();
                nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                nextButton.setOnClickListener(this);
                addSearchScreen();
                isFamilySelectionScreen = Boolean.TRUE;
                isFamilyAssignedScreen = Boolean.FALSE;
            } else if (isVillageSelectionScreen) {
                bodyLayoutContainer.removeAllViews();
                familyDataBean = null;
                addSearchScreen();
                isFamilySelectionScreen = Boolean.TRUE;
                isVillageSelectionScreen = Boolean.FALSE;
            } else {
                navigateToHomeScreen(false);
            }
        }
        return true;
    }
}
