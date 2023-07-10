package com.argusoft.sewa.android.app.activity;


import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.RadioButton;
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
import com.argusoft.sewa.android.app.constants.RchConstants;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.MemberAdditionalInfoDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.LocationBean;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@EActivity
public class TakeHomeRation extends MenuActivity implements View.OnClickListener, PagingListView.PagingListener {

    private static final long DELAY = 500;
    private static final String ASHA_AREA_SELECTION_SCREEN = "ashaAreaSelectionScreen";
    private static final String CHILD_SELECTION_SCREEN = "childSelectionScreen";
    private static final Integer REQUEST_CODE_FOR_MY_PEOPLE_ACTIVITY = 200;
    private static final String TAG = "TakeHomeRation";
    @OrmLiteDao(helper = DBConnection.class)
    Dao<MemberBean, Integer> memberBeanDao;
    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    FormMetaDataUtil formMetaDataUtil;
    private MemberDataBean memberSelected = null;

    private LinearLayout bodyLayoutContainer;
    private Button nextButton;

    private Spinner ashaAreaSpinner;
    private Integer selectedAshaArea;
    private String screen;
    private TextInputLayout searchBox;

    private Timer timer = new Timer();

    private List<LocationBean> ashaAreaList = new ArrayList<>();
    private List<MemberDataBean> memberList = new ArrayList<>();
    private LinearLayout globalPanel;
    private CharSequence searchstring;
    private int offset;
    private int limit = 30;
    private MaterialTextView titleTextView;
    private MaterialTextView noTextView;
    private PagingListView paginatedListView;
    private int selectedChildIndex = -1;
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
        setTitle(UtilBean.getTitleText(UtilBean.getFullFormOfEntity().get((FormConstants.TECHO_AWW_THR))));
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
        myIntent.putExtra(FieldNameConstants.TITLE, UtilBean.getTitleText(UtilBean.getFullFormOfEntity().get((FormConstants.TECHO_AWW_THR))));
        startActivityForResult(myIntent, ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            nextButtonPressed();
        }
    }

    private void nextButtonPressed() {
        switch (screen) {
            case ASHA_AREA_SELECTION_SCREEN:
                showProcessDialog();
                String selectedArea = ashaAreaSpinner.getSelectedItem().toString();
                for (LocationBean locationBean : ashaAreaList) {
                    if (selectedArea.equals(locationBean.getName())) {
                        selectedAshaArea = locationBean.getActualID();
                        break;
                    }
                }
                bodyLayoutContainer.removeAllViews();
                bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                addSearchTextBox();
                retrieveChildrens(null, false, null);
                break;
            case CHILD_SELECTION_SCREEN:
                if (memberList == null || memberList.isEmpty()) {
                    navigateToHomeScreen(false);
                    return;
                }
                if (selectedChildIndex != -1) {
                    memberSelected = memberList.get(selectedChildIndex);
                    startDynamicFormActivity();
                } else {
                    SewaUtil.generateToast(this, LabelConstants.CHILD_SELECTION_REQUIRED);
                }
                break;
            default:

        }
    }

    @UiThread
    public void addSearchTextBox() {
        searchBox = MyStaticComponents.getEditTextWithQrScan(this, LabelConstants.MEMBER_ID_OR_NAME_OR_HEALTH_ID_TO_SEARCH, 1, 10, 1);
        bodyLayoutContainer.addView(searchBox);
        bodyLayoutContainer.addView(MyStaticComponents.getOrTextView(context));

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
                                        runOnUiThread(() -> retrieveChildrens(s, true, null));
                                    } else if (s == null || s.length() == 0) {
                                        runOnUiThread(() -> {
                                            showProcessDialog();
                                            retrieveChildrens(null, false, null);
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
    }


    @UiThread
    public void addAshaAreaSelectionSpinner() {
        screen = ASHA_AREA_SELECTION_SCREEN;
        setSubTitle(null);
        bodyLayoutContainer.removeAllViews();
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, LabelConstants.SELECT_ASHA_AREA));
        String[] arrayOfOptions = new String[ashaAreaList.size()];
        int i = 0;
        for (LocationBean locationBean : ashaAreaList) {
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }
        ashaAreaSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 2);
        if (selectedAshaArea != null) {
            for (LocationBean locationBean : ashaAreaList) {
                String locationName;
                if (locationBean.getActualID().intValue() == selectedAshaArea.intValue()) {
                    locationName = locationBean.getName();
                    ashaAreaSpinner.setSelection(Arrays.asList(arrayOfOptions).indexOf(locationName));
                }
            }
        }
        bodyLayoutContainer.addView(ashaAreaSpinner);
        hideProcessDialog();
    }

    @Background
    public void retrieveChildrens(CharSequence s, Boolean isSearch, String qrData) {
        searchstring = s;
        offset = 0;
        qrScanFilter = SewaUtil.setQrScanFilterData(qrData);
        selectedChildIndex = -1;
        memberList = fhsService.retrieveChildsBetween6MonthsTo3YearsByAshaArea(selectedAshaArea, s, limit, offset, qrScanFilter);
        offset = offset + limit;
        addChildsOfAgeBetween6MonthsTo3YearsList(isSearch);
    }

    @UiThread
    public void addChildsOfAgeBetween6MonthsTo3YearsList(boolean isSearch) {
        screen = CHILD_SELECTION_SCREEN;
        setSubTitle(null);
        bodyLayoutContainer.removeView(paginatedListView);
        bodyLayoutContainer.removeView(noTextView);
        bodyLayoutContainer.removeView(titleTextView);

        if (memberList != null && !memberList.isEmpty()) {
            titleTextView = MyStaticComponents.getListTitleView(this, LabelConstants.SELECT_CHILD);
            bodyLayoutContainer.addView(titleTextView);

            List<ListItemDataBean> list = setTextInList(memberList);
            AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> selectedChildIndex = position;
            paginatedListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_two_item, onItemClickListener, this);
            bodyLayoutContainer.addView(paginatedListView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
        } else {
            noTextView = MyStaticComponents.generateInstructionView(this, LabelConstants.NO_CHILDREN_OF_AGE_BETWEEN_6_MONTHS_TO_3_YEARS_ALERT);
            bodyLayoutContainer.addView(noTextView);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
        }
        if (!isSearch) {
            hideProcessDialog();
        }
    }

    private List<ListItemDataBean> setTextInList(List<MemberDataBean> memberDataBeans) {
        MemberBean mother = null;
        boolean isFilledToday;
        Gson gson = new Gson();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        List<MemberDataBean> removedChildren = new ArrayList<>();
        List<ListItemDataBean> list = new ArrayList<>();
        for (MemberDataBean memberDataBean : memberDataBeans) {
            if (memberDataBean.getUniqueHealthId() != null) {
                if (memberDataBean.getMotherId() != null) {
                    try {
                        mother = memberBeanDao.queryBuilder().where().eq("actualId", memberDataBean.getMotherId()).queryForFirst();
                    } catch (SQLException e) {
                        Log.e(getClass().getName(), null, e);
                    }
                }

                isFilledToday = false;
                if (memberDataBean.getAdditionalInfo() != null) {
                    MemberAdditionalInfoDataBean memberAdditionalInfoDataBean = gson.fromJson(memberDataBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
                    if (memberAdditionalInfoDataBean.getLastTHRServiceDate() != null && calendar.getTimeInMillis() <= memberAdditionalInfoDataBean.getLastTHRServiceDate()) {
                        isFilledToday = true;
                    }
                }
                if (mother == null) {
                    list.add(new ListItemDataBean(null, memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean),
                            UtilBean.getMyLabel(LabelConstants.NOT_AVAILABLE), UtilBean.getMyLabel(LabelConstants.NOT_AVAILABLE), isFilledToday));
                } else {
                    list.add(new ListItemDataBean(null, memberDataBean.getUniqueHealthId(), UtilBean.getMemberFullName(memberDataBean),
                            UtilBean.getMyLabel(LabelConstants.MOTHER), UtilBean.getMemberFullName(mother), isFilledToday));
                }
            } else {
                removedChildren.add(memberDataBean);
            }
        }
        memberList.removeAll(removedChildren);
        return list;
    }

    @UiThread
    public void setRadioButtonColorFromSyncStatus(RadioButton radioButton, MemberDataBean memberDataBean) {
        if (memberDataBean.getSyncStatus() != null) {
            switch (memberDataBean.getSyncStatus()) {
                case "B":
                    radioButton.setTextColor(Color.BLUE);
                    break;
                case "R":
                    radioButton.setTextColor(Color.RED);
                    break;
                default:
                    radioButton.setTextColor(Color.BLACK);
            }
        } else if (memberDataBean.getAdditionalInfo() != null) {
            Gson gson = new Gson();
            MemberAdditionalInfoDataBean memberAdditionalInfo = gson.fromJson(memberDataBean.getAdditionalInfo(), MemberAdditionalInfoDataBean.class);
            if (memberAdditionalInfo.getCpNegativeQues() != null && memberAdditionalInfo.getCpState() != null && memberAdditionalInfo.getCpState().equals(RchConstants.CP_DELAYED_DEVELOPMENT)) {
                radioButton.setTextColor(Color.YELLOW);
            } else if (memberAdditionalInfo.getCpNegativeQues() != null && memberAdditionalInfo.getCpState() != null && memberAdditionalInfo.getCpState().equals(RchConstants.CP_TREATMENT_COMMENCED)) {
                radioButton.setTextColor(Color.parseColor("#20aa0b"));//dark green
            } else {
                radioButton.setTextColor(Color.BLACK);
            }
        }
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
                LabelConstants.ON_TAKE_HOME_RATION_CLOSE_ALERT,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    private void startDynamicFormActivity() {
        showProcessDialog();
        Intent intent = new Intent(this, DynamicFormActivity_.class);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        formMetaDataUtil.setMetaDataForRchFormByFormType(FormConstants.TECHO_AWW_THR, memberSelected.getId(), memberSelected.getFamilyId(), null, sharedPref);
        intent.putExtra(SewaConstants.ENTITY, FormConstants.TECHO_AWW_THR);
        startActivityForResult(intent, REQUEST_CODE_FOR_MY_PEOPLE_ACTIVITY);
        hideProcessDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult resultForQRScanner = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_MY_PEOPLE_ACTIVITY) {
            showProcessDialog();
            bodyLayoutContainer.removeAllViews();
            bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
            selectedChildIndex = -1;
            addSearchTextBox();
            retrieveChildrens(null, false, null);
        } else if (requestCode == ActivityConstants.LOCATION_SELECTION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                selectedAshaArea = Integer.parseInt(data.getStringExtra(FieldNameConstants.LOCATION_ID));
                showProcessDialog();
                bodyLayoutContainer.removeAllViews();
                bodyLayoutContainer.addView(lastUpdateLabelView(sewaService, bodyLayoutContainer));
                selectedChildIndex = -1;
                addSearchTextBox();
                retrieveChildrens(null, false, null);
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
                retrieveChildrens(null, false, scanningData);
            }
        } else {
            navigateToHomeScreen(false);
        }
    }

    @Override
    public void onLoadMoreItems() {
        onLoadMoreBackground();
    }

    @Background
    public void onLoadMoreBackground() {
        List<MemberDataBean> memberDataBeanList = fhsService.retrieveChildsBetween6MonthsTo3YearsByAshaArea(selectedAshaArea, searchstring, limit, offset, qrScanFilter);
        offset = offset + limit;
        onLoadMoreUi(memberDataBeanList);
    }

    @UiThread
    public void onLoadMoreUi(List<MemberDataBean> memberDataBeanList) {
        if (memberDataBeanList != null && !memberDataBeanList.isEmpty()) {
            memberList.addAll(memberDataBeanList);
            List<ListItemDataBean> stringList = setTextInList(memberDataBeanList);
            paginatedListView.onFinishLoadingWithItem(true, stringList);
        } else {
            paginatedListView.onFinishLoadingWithItem(false, null);
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
            switch (screen) {
                case ASHA_AREA_SELECTION_SCREEN:
                    navigateToHomeScreen(false);
                    break;
                case CHILD_SELECTION_SCREEN:
                    startLocationSelectionActivity();
                    break;
                default:
            }
            return true;
        }
        return false;
    }
}
