package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.NPCBServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberAdditionalInfoDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.FormMetaDataUtil;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by prateek on 3/6/19.
 */

@EActivity
public class NPCBScreening extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    private static final Integer REQUEST_CODE_FOR_NPCB_SCREENING_ACTIVITY = 300;
    private static final String ASHA_AREA_SELECTION_SCREEN = "ashaAreaSelectionScreen";
    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";
    private static final String MEMBER_DETAILS_SCREEN = "memberDetailsScreen";
    private static final long DELAY = 500;
    private static final String TAG = "NPCBScreening";
    @Bean
    public SewaFhsServiceImpl fhsService;
    @Bean
    public FormMetaDataUtil formMetaDataUtil;
    @Bean
    public NPCBServiceImpl npcbService;
    @Bean
    public SewaServiceImpl sewaService;
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private String screen;
    private Spinner spinner;
    private TextInputLayout searchText;
    private List<LocationBean> ashaAreaList = new ArrayList<>();
    private Integer selectedAshaArea;
    private MemberDataBean selectedMember;
    private List<MemberDataBean> memberDataBeans = new ArrayList<>();
    private Timer timer = new Timer();
    private MyAlertDialog myAlertDialog;
    private long limit = 30;
    private long offset;
    private MaterialTextView noMemberTextView;
    private MaterialTextView titleTextView;
    private MaterialTextView questionTextView;
    private String searchString;
    private int selectedMemberIndex = -1;
    private PagingListView paginatedListView;
    private LinearLayout globalPanel;
    private LinkedHashMap<String, String> qrScanFilter = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setTitle(UtilBean.getTitleText(LabelConstants.NPCB_SCREENING_TITLE));
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
        myIntent.putExtra(FieldNameConstants.TITLE, LabelConstants.NPCB_SCREENING_TITLE);
        startActivityForResult(myIntent, ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE);
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
                LabelConstants.ON_NPCB_SCREENING_CLOSE_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        myAlertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            setSubTitle(null);
            switch (screen) {
                case ASHA_AREA_SELECTION_SCREEN:
                    showProcessDialog();
                    String selectedArea = spinner.getSelectedItem().toString();
                    for (LocationBean locationBean : ashaAreaList) {
                        if (selectedArea.equals(locationBean.getName())) {
                            selectedAshaArea = locationBean.getActualID();
                            break;
                        }
                    }
                    bodyLayoutContainer.removeAllViews();
                    addSearchTextBoxForMember();
                    if (searchText != null && searchText.getEditText() != null
                            && searchText.getEditText().getText().toString().trim().length() > 0) {
                        retrieveMemberListFromDB(searchText.getEditText().getText().toString().trim(), null);
                    } else {
                        retrieveMemberListFromDB(null, null);
                    }
                    break;
                case MEMBER_SELECTION_SCREEN:
                    if (selectedMemberIndex != -1) {
                        selectedMember = memberDataBeans.get(selectedMemberIndex);
                        startDynamicFormActivity();
                    } else {
                        SewaUtil.generateToast(this, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_A_MEMBER));
                    }
                    selectedMemberIndex = -1;
                    break;
                default:
                    break;
            }
        }
    }

    @UiThread
    public void addAshaAreaSelectionSpinner() {
        screen = ASHA_AREA_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        setSubTitle(null);
        titleTextView = MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.SELECT_ASHA_AREA);
        bodyLayoutContainer.addView(titleTextView);
        String[] arrayOfOptions = new String[ashaAreaList.size()];
        int i = 0;
        for (LocationBean locationBean : ashaAreaList) {
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }
        spinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 2);
        if (selectedAshaArea != null) {
            for (LocationBean locationBean : ashaAreaList) {
                String locationName;
                if (locationBean.getActualID().intValue() == selectedAshaArea.intValue()) {
                    locationName = locationBean.getName();
                    spinner.setSelection(Arrays.asList(arrayOfOptions).indexOf(locationName));
                }
            }
        }
        bodyLayoutContainer.addView(spinner);
        nextButton.setOnClickListener(this);
        hideProcessDialog();
    }

    @UiThread
    public void addSearchTextBoxForMember() {
        if (searchText == null) {
            searchText = MyStaticComponents.getEditTextWithQrScan(this, LabelConstants.MEMBER_ID_OR_NAME_OR_HEALTH_ID_TO_SEARCH, 1, 10, 1);
            if (searchText.getEditText() != null) {
                searchText.getEditText().addTextChangedListener(new TextWatcher() {
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
                                                retrieveMemberListFromDB(s.toString(), null);
                                                searchText.setFocusable(true);
                                            });
                                        } else if (s == null || s.length() == 0) {
                                            runOnUiThread(() -> {
                                                showProcessDialog();
                                                retrieveMemberListFromDB(null, null);
                                                searchText.clearFocus();
                                            });
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
        bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
        bodyLayoutContainer.addView(searchText);
        bodyLayoutContainer.addView(MyStaticComponents.getOrTextView(context));
    }

    @Background
    public void retrieveMemberListFromDB(String search, String qrData) {
        offset = 0;
        searchString = search;
        qrScanFilter = SewaUtil.setQrScanFilterData(qrData);
        selectedMemberIndex = -1;
        memberDataBeans = npcbService.retrieveMembersForNPCBScreening(selectedAshaArea, search, limit, offset, qrScanFilter);
        offset = offset + limit;
        setMemberSelectionScreen(search != null);
    }

    @UiThread
    public void setMemberSelectionScreen(Boolean isSearch) {
        bodyLayoutContainer.removeView(titleTextView);
        bodyLayoutContainer.removeView(spinner);
        bodyLayoutContainer.removeView(paginatedListView);
        bodyLayoutContainer.removeView(questionTextView);
        bodyLayoutContainer.removeView(noMemberTextView);

        screen = MEMBER_SELECTION_SCREEN;
        nextButton.setText(GlobalTypes.EVENT_NEXT);
        if (!memberDataBeans.isEmpty()) {
            questionTextView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_MEMBER);
            bodyLayoutContainer.addView(questionTextView);

            List<ListItemDataBean> list = getList(memberDataBeans);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedMemberIndex = position;
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
        } else {
            noMemberTextView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBERS_FOUND);
            bodyLayoutContainer.addView(noMemberTextView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
            nextButton.setOnClickListener(v -> addAshaAreaSelectionSpinner());
        }
        hideProcessDialog();
    }

    private List<ListItemDataBean> getList(List<MemberDataBean> memberDataBeanList) {
        List<ListItemDataBean> list = new ArrayList<>();
        List<MemberDataBean> diabeticMembers = new ArrayList<>();
        for (MemberDataBean memberDataBean : memberDataBeanList) {
            if (memberDataBean.getAdditionalInfo() != null) {
                MemberAdditionalInfoDataBean additionalInfo = new Gson().fromJson(memberDataBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
                if (additionalInfo.getNcdConfFor() != null && additionalInfo.getNcdConfFor().contains("D")) {
                    diabeticMembers.add(memberDataBean);
                    continue;
                }
            }
            list.add(new ListItemDataBean(memberDataBean.getFamilyId(), memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean), null, null));
        }
        memberDataBeans.removeAll(diabeticMembers);
        return list;
    }

    private void startDynamicFormActivity() {
        screen = MEMBER_DETAILS_SCREEN;
        formMetaDataUtil.setMetaDataForNPCBForms(Long.valueOf(selectedMember.getId()));
        Intent intent = new Intent(this, DynamicFormActivity_.class);
        intent.putExtra(SewaConstants.ENTITY, FormConstants.ASHA_NPCB);
        startActivityForResult(intent, REQUEST_CODE_FOR_NPCB_SCREENING_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult resultForQRScanner = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        bodyLayoutContainer.removeAllViews();
        if (requestCode == REQUEST_CODE_FOR_NPCB_SCREENING_ACTIVITY) {
            showProcessDialog();
            addSearchTextBoxForMember();
            setMemberSelectionScreen(null);
        } else if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                selectedAshaArea = Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID));
                showProcessDialog();
                bodyLayoutContainer.removeAllViews();
                addSearchTextBoxForMember();
                if (searchText != null && searchText.getEditText() != null
                        && searchText.getEditText().getText().toString().trim().length() > 0) {
                    searchText.getEditText().setText("");
                } else {
                    retrieveMemberListFromDB(null, null);
                }
            } else {
                navigateToHomeScreen(false);
            }
        } else if (resultForQRScanner != null) {
            if (resultForQRScanner.getContents() == null) {
                SewaUtil.generateToast(this, LabelConstants.FAILED_TO_SCAN_QR);
            } else {
                //show dialogue with resultForQRScanner
                String scanningData = resultForQRScanner.getContents();
                Log.i(TAG, "QR Scanner Data : " + scanningData);
                retrieveMemberListFromDB(null, scanningData);
            }
        } else {
            navigateToHomeScreen(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (screen == null || screen.isEmpty()) {
            navigateToHomeScreen(false);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            setSubTitle(null);
            switch (screen) {
                case MEMBER_SELECTION_SCREEN:
                    startLocationSelectionActivity();
                    break;
                case ASHA_AREA_SELECTION_SCREEN:
                    navigateToHomeScreen(false);
                    break;
                default:
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
        List<MemberDataBean> membersDataBeanList = npcbService.retrieveMembersForNPCBScreening(selectedAshaArea, searchString, limit, offset, qrScanFilter);
        offset = offset + limit;
        onLoadMoreUi(membersDataBeanList);
    }

    @UiThread
    public void onLoadMoreUi(List<MemberDataBean> membersDataBeanList) {
        if (membersDataBeanList != null && !membersDataBeanList.isEmpty()) {
            List<ListItemDataBean> list = getList(membersDataBeanList);
            memberDataBeans.addAll(membersDataBeanList);
            paginatedListView.onFinishLoadingWithItem(true, list);
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
        }
    }
}
