package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceRestClientImpl;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.FormMetaDataUtil;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@EActivity
public class MemberGeriatricListActivity extends MenuActivity implements View.OnClickListener {

    private static final String TAG = "MemberGeriatricList";

    @Bean
    SewaServiceRestClientImpl sewaServiceRestClient;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    FormMetaDataUtil formMetaDataUtil;

    private static final long DELAY = 500;
    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private TextInputLayout searchBox;

    private Timer timer = new Timer();

    private MemberDataBean selectedMember = null;
    private List<String> areaIds;
    List<String[]> geriatricList;
    private MaterialTextView textView;
    private PagingListView paginatedListViewWithItem;
    private int selectedMemberIndex = -1;
    private LinkedHashMap<String, String> qrScanFilter = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        areaIds = new Gson().fromJson(this.getIntent().getStringExtra("locationId"),
                new TypeToken<List<String>>() {
                }.getType());
        if (areaIds.isEmpty()) {
            Log.i(TAG, "Location ID is not valid. Finishing activity.");
            finish();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.initView();
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
        setTitle(UtilBean.getTitleText(LabelConstants.GERIATRIC_ACTIVITY_TITLE));
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        setContentView(globalPanel);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        setBodyDetail();
    }

    private void setBodyDetail() {
        addSearchTextBox();
        this.fetchGeriatricListFromDB(null, null);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            //retrieve contact details of the member and show it in display mode.
            if (geriatricList.isEmpty()) {
                finish();
                return;
            }
            if (selectedMemberIndex != -1) {
                String[] member = geriatricList.get(selectedMemberIndex);
                MemberBean memberBean = fhsService.retrieveMemberBeanByActualId(Long.parseLong(member[0]));
                selectedMember = new MemberDataBean(memberBean);
                startDynamicFormActivity(FormConstants.GERIATRICS_MEDICATION_ALERT);
            } else {
                SewaUtil.generateToast(this, LabelConstants.PLEASE_SELECT_A_MEMBER);
            }

        }
    }

    @UiThread
    public void addSearchTextBox() {
        searchBox = MyStaticComponents.getEditTextWithQrScan(this, LabelConstants.MEMBER_ID_OR_NAME_OR_HEALTH_ID_TO_SEARCH, 1, 10, 1);
        if (searchBox.getEditText() != null) {
            searchBox.getEditText().addTextChangedListener(new TextWatcher() {
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
                                        runOnUiThread(() -> fetchGeriatricListFromDB(s, null));
                                    } else if (s == null || s.length() == 0) {
                                        runOnUiThread(() -> {
                                            showProcessDialog();
                                            fetchGeriatricListFromDB(null, null);
                                            searchBox.clearFocus();
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

        bodyLayoutContainer.addView(searchBox);
        bodyLayoutContainer.addView(MyStaticComponents.getOrTextView(context));
    }

    @UiThread
    public void setGeriatricListViews(boolean isSearch) {
        bodyLayoutContainer.removeView(textView);
        bodyLayoutContainer.removeView(paginatedListViewWithItem);

        if (!geriatricList.isEmpty()) {
            textView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_A_MEMBER_FROM_LIST);
            bodyLayoutContainer.addView(textView);
            List<ListItemDataBean> list = getList(geriatricList);

            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedMemberIndex = position;
            paginatedListViewWithItem = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_item, onItemClickListener, null);
            bodyLayoutContainer.addView(paginatedListViewWithItem);
            nextButton.setText(GlobalTypes.EVENT_NEXT);
        } else {
            textView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_MEMBER_FOUND_IN_YOUR_AREA);
            bodyLayoutContainer.addView(textView);
            nextButton.setText(GlobalTypes.MAIN_MENU);
        }
        hideProcessDialog();
    }

    private List<ListItemDataBean> getList(List<String[]> geriatricList) {
        List<ListItemDataBean> list = new ArrayList<>();
        for (String[] aRow : geriatricList) {
            list.add(new ListItemDataBean(aRow[5], aRow[2] + " " + aRow[3] + " " + aRow[4]));
        }
        return list;
    }

    @Background
    public void fetchGeriatricListFromDB(CharSequence s, String qrData) {
        qrScanFilter = SewaUtil.setQrScanFilterData(qrData);
        geriatricList = fhsService.retrieveGeriatricMembers(areaIds, s, qrScanFilter);
        setGeriatricListViews(s != null && s.toString().trim().length() > 0);
    }

    private void startDynamicFormActivity(String formType) {
        showProcessDialog();
        Intent intent = new Intent(this, DynamicFormActivity_.class);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        formMetaDataUtil.setMetaDataForRchFormByFormType(formType, selectedMember.getId(), selectedMember.getFamilyId(), null, sharedPref);
        intent.putExtra(SewaConstants.ENTITY, formType);
        startActivityForResult(intent, ActivityConstants.GERIATRICS_ACTIVITY_REQUEST_CODE);
        hideProcessDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult resultForQRScanner = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityConstants.GERIATRICS_ACTIVITY_REQUEST_CODE) {
            showProcessDialog();
            selectedMemberIndex = -1;
            bodyLayoutContainer.removeAllViews();
            addSearchTextBox();
            fetchGeriatricListFromDB(null, null);
        } else if (resultForQRScanner != null) {
            if (resultForQRScanner.getContents() == null) {
                SewaUtil.generateToast(this, LabelConstants.FAILED_TO_SCAN_QR);
            } else {
                //show dialogue with resultForQRScanner
                String scanningData = resultForQRScanner.getContents();
                Log.i(TAG, "QR Scanner Data : " + scanningData);
                fetchGeriatricListFromDB(null, scanningData);
            }
        } else {
            navigateToHomeScreen(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
