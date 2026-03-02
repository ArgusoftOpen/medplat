package com.argusoft.sewa.android.app.activity;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.asynctask.ServerIsAliveAsyncTask;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.listeners.DateChangeListenerStatic;
import com.argusoft.sewa.android.app.constants.FormulaConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceRestClientImpl;
import com.argusoft.sewa.android.app.databean.QueryMobDataBean;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.restclient.RestHttpException;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@EActivity
public class WorkRegisterActivity extends MenuActivity implements View.OnClickListener {

    private static final String VILLAGE_SELECTION_SCREEN = "villageSelectionScreen";
    private static final String DATE_SELECTION_SCREEN = "dateSelectionScreen";
    private static final String TABLE_SCREEN = "tableScreen";
    private static final int LIMIT = 100;
    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaFhsServiceImpl fhsService;
    @Bean
    SewaServiceRestClientImpl sewaServiceRestClient;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private MyAlertDialog myAlertDialog;
    private TableLayout tableLayout;
    private Button loadMore;
    private LinearLayout fromDate;
    private DateChangeListenerStatic fromDateListener;
    private LinearLayout toDate;
    private DateChangeListenerStatic toDateListener;
    private Spinner villageSpinner;
    private String locationId;
    private String screen;
    private int numberOfRows = 0;
    private List<String> headNames = null;
    private List<LocationBean> locationBeans = new ArrayList<>();
    private LinearLayout globalPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        context = this;
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
        setTitle(UtilBean.getTitleText(LabelConstants.WORK_REGISTER_TITLE));
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        setContentView(globalPanel);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);

        if (!sewaService.isOnline()) {
            showNotOnlineMessage();
            return;
        }

        setBodyDetail();
    }

    @Override
    public void onClick(View v) {
        if (!sewaService.isOnline()) {
            showNotOnlineMessage();
            return;
        }

        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screen) {
                case VILLAGE_SELECTION_SCREEN:
                    addDateSelectionScreen();
                    String selectedVillage = villageSpinner.getSelectedItem().toString();
                    for (LocationBean locationBean : locationBeans) {
                        if (selectedVillage.equals(locationBean.getName())) {
                            locationId = locationBean.getActualID().toString();
                            break;
                        }
                    }
                    break;
                case DATE_SELECTION_SCREEN:
                    if (validateDate() && checkServerIsAlive()) {
                        setTableScreen();
                    }
                    break;
                case TABLE_SCREEN:
                    navigateToHomeScreen(false);
                    break;
                default:
                    break;
            }
        }
    }

    public boolean checkServerIsAlive() {
        try {
            Boolean serverIsAlive = new ServerIsAliveAsyncTask(sewaService).execute().get();
            if (Boolean.FALSE.equals(serverIsAlive)) {
                SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.SERVER_NOT_WORKING_ALERT));
            }
            return serverIsAlive;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public boolean validateDate() {
        if (fromDateListener.getDateSet() == null) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_FROM_DATE));
            return false;
        }
        if (toDateListener.getDateSet() == null) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.PLEASE_SELECT_TO_DATE));
            return false;
        }
        if (toDateListener.getDateSet().before(fromDateListener.getDateSet())) {
            SewaUtil.generateToast(context, UtilBean.getMyLabel(LabelConstants.TO_DATE_CANNOT_BE_BEFORE_FROM_DATE));
            return false;
        }
        return true;
    }

    @UiThread
    public void setTableScreen() {
        screen = TABLE_SCREEN;
        showProcessDialog();
        bodyLayoutContainer.removeAllViews();
        retrieveData(true, LIMIT, 0);
        nextButton.setText(GlobalTypes.EVENT_OKAY);
    }

    @UiThread
    public void setBodyDetail() {
        locationBeans = fhsService.getDistinctLocationsAssignedToUser();
        if (locationBeans.size() == 1) {
            addDateSelectionScreen();
            locationId = locationBeans.get(0).getActualID().toString();
        } else if (locationBeans.isEmpty()) {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(this,
                    UtilBean.getMyLabel(LabelConstants.DATA_NOT_SYNCED_ALERT)));
            View.OnClickListener listener = v -> navigateToHomeScreen(false);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
            nextButton.setOnClickListener(listener);
            screen = VILLAGE_SELECTION_SCREEN;
            hideProcessDialog();
        } else {
            addVillageSelectionSpinner();
        }
    }

    @UiThread
    public void showNotOnlineMessage() {
        hideProcessDialog();
        View.OnClickListener myListener = v -> {
            myAlertDialog.dismiss();
            navigateToHomeScreen(false);
        };
        myAlertDialog = new MyAlertDialog(context,
                UtilBean.getMyLabel(LabelConstants.NETWORK_CONNECTIVITY_ALERT),
                myListener, DynamicUtils.BUTTON_OK);
        myAlertDialog.show();
        myAlertDialog.setOnCancelListener(dialog -> {
            myAlertDialog.dismiss();
            navigateToHomeScreen(false);
        });
    }

    private void addVillageSelectionSpinner() {
        screen = VILLAGE_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();
        String[] arrayOfOptions = new String[locationBeans.size()];
        int i = 0;
        for (LocationBean locationBean : locationBeans) {
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }

        if (villageSpinner == null) {
            villageSpinner = MyStaticComponents.getSpinner(this, arrayOfOptions, 0, 2);
        }
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.SELECT_VILLAGE)));
        bodyLayoutContainer.addView(villageSpinner);
        hideProcessDialog();
    }

    private void addDateSelectionScreen() {
        bodyLayoutContainer.removeAllViews();
        screen = DATE_SELECTION_SCREEN;

        int fromDatePickerId = 1001;
        int toDatePickerId = 1002;
        List<ValidationTagBean> list = new ArrayList<>();
        list.add(new ValidationTagBean(FormulaConstants.VALIDATION_IS_FUTURE_DATE, LabelConstants.INVALID_DATE));

        if (fromDate == null) {
            fromDateListener = new DateChangeListenerStatic(context, list);
            fromDate = MyStaticComponents.getCustomDatePickerForStatic(context, fromDateListener, fromDatePickerId);
        }
        if (toDate == null) {
            toDateListener = new DateChangeListenerStatic(context, list);
            toDate = MyStaticComponents.getCustomDatePickerForStatic(context, toDateListener, toDatePickerId);
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.SELECT_FROM_DATE)));
        bodyLayoutContainer.addView(fromDate);
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, this, UtilBean.getMyLabel(LabelConstants.SELECT_TO_DATE)));
        bodyLayoutContainer.addView(toDate);
        hideProcessDialog();
    }

    @Background
    public void retrieveData(boolean createTable, int limit, int offset) {
        if (locationId == null) {
            hideProcessDialog();
            return;
        }
        if (fromDateListener.getDateSet() == null) {
            hideProcessDialog();
            return;
        }
        if (toDateListener.getDateSet() == null) {
            hideProcessDialog();
            return;
        }

        try {
            LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
            parameters.put("location_id", locationId);
            parameters.put("from_date", sdf.format(fromDateListener.getDateSet()));
            parameters.put("to_date", sdf.format(toDateListener.getDateSet()));
            parameters.put("limit", limit);
            parameters.put("offset", offset);

            QueryMobDataBean queryResponse = sewaServiceRestClient.executeQuery(
                    new QueryMobDataBean("mob_work_register_detail", null, parameters, 0));
            if (queryResponse != null && queryResponse.getResult() != null) {
                List<LinkedHashMap<String, Object>> resultList = queryResponse.getResult();
                if (resultList != null && !resultList.isEmpty()) {
                    Set<String> headers = resultList.get(0).keySet();
                    headNames = new ArrayList<>();
                    for (String head : headers) {
                        if (!head.startsWith("hidden")) {
                            headNames.add(head);
                        }
                    }
                    if (createTable) {
                        numberOfRows = 0;
                        createTableLayout(headNames, resultList);
                    } else {
                        addDataToTable(tableLayout, resultList);
                    }
                } else {
                    if (loadMore != null) {
                        runOnUiThread(() -> bodyLayoutContainer.removeView(loadMore));
                    }

                    if (createTable) {
                        numberOfRows = 0;
                        runOnUiThread(() -> bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context,
                                UtilBean.getMyLabel(LabelConstants.NO_SERVICES_PROVIDED_DURING_SELECTED_PERIOD))));
                    }
                    hideProcessDialog();
                }
            } else {
                hideProcessDialog();
            }
        } catch (RestHttpException e) {
            hideProcessDialog();
            Log.e(getClass().getName(), null, e);
        }
    }

    @UiThread
    public void createTableLayout(List<String> headNames, List<LinkedHashMap<String, Object>> resultList) {
        tableLayout = new TableLayout(this);
        tableLayout.setPadding(10, 10, 10, 10);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        TableRow row = new TableRow(this);
        row.setPadding(10, 15, 10, 15);
        if (SewaUtil.CURRENT_THEME == R.style.techo_training_app) {
            row.setBackgroundResource(R.drawable.table_row_selector);
        } else {
            row.setBackgroundResource(R.drawable.spinner_item_border);
        }
        row.setLayoutParams(new TableRow.LayoutParams(MATCH_PARENT));

        for (String string : headNames) {
            MaterialTextView textView = new MaterialTextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(10, 10, 10, 10);
            textView.setText(UtilBean.getMyLabel(string));
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            row.addView(textView);
        }
        tableLayout.addView(row, 0);

        HorizontalScrollView hsv = new HorizontalScrollView(this);
        hsv.addView(tableLayout);
        bodyLayoutContainer.addView(hsv);
        addDataToTable(tableLayout, resultList);
    }

    @UiThread
    public void addDataToTable(TableLayout tableLayout, List<LinkedHashMap<String, Object>> resultList) {
        bodyLayoutContainer.removeView(loadMore);
        for (LinkedHashMap<String, Object> map : resultList) {
            addTableRow(tableLayout, numberOfRows + 1, map);
            numberOfRows++;
        }
        if (resultList.size() == 100) {
            addLoadMoreButton();
        }
        hideProcessDialog();
    }

    private void addTableRow(TableLayout tableLayout, int index, final Map<String, Object> map) {
        TableRow.LayoutParams layoutParamsForRow = new TableRow.LayoutParams(MATCH_PARENT);

        TableRow row = new TableRow(this);
        row.setPadding(10, 15, 10, 15);
        if (SewaUtil.CURRENT_THEME == R.style.techo_training_app) {
            row.setBackgroundResource(R.drawable.table_row_selector);
        } else {
            row.setBackgroundResource(R.drawable.spinner_item_border);
        }
        row.setLayoutParams(layoutParamsForRow);

        Object tmpObj;
        for (String string : headNames) {
            MaterialTextView textView = new MaterialTextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(10, 10, 10, 10);
            tmpObj = map.get(string);
            if (tmpObj != null) {
                textView.setText(tmpObj.toString());
                textView.setOnClickListener(v -> {
                    Object hiddenVisitId = map.get("hiddenVisitId");
                    Object hiddenServiceType = map.get("hiddenServiceType");
                    if (hiddenVisitId != null && hiddenServiceType != null) {
                        Intent intent = new Intent(context, WorkRegisterLineListActivity_.class);
                        intent.putExtra("visitId", hiddenVisitId.toString());
                        intent.putExtra("serviceType", hiddenServiceType.toString());
                        startActivity(intent);
                    }
                });
                row.addView(textView);
            }
        }
        tableLayout.addView(row, index);
    }

    private void addLoadMoreButton() {
        bodyLayoutContainer.removeView(loadMore);

        if (loadMore == null) {
            loadMore = MyStaticComponents.getButton(context, UtilBean.getMyLabel(LabelConstants.LOAD_MORE), 101,
                    new LinearLayout.LayoutParams(MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            loadMore.setGravity(Gravity.CENTER);
            loadMore.setOnClickListener(
                    v -> {
                        showProcessDialog();
                        retrieveData(false, LIMIT, numberOfRows);
                    }
            );
        }

        bodyLayoutContainer.addView(loadMore);
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
                case VILLAGE_SELECTION_SCREEN:
                    navigateToHomeScreen(false);
                    break;
                case DATE_SELECTION_SCREEN:
                    if (locationBeans.size() == 1) {
                        navigateToHomeScreen(false);
                    } else if (locationBeans.isEmpty()) {
                        navigateToHomeScreen(false);
                    } else {
                        addVillageSelectionSpinner();
                        nextButton.setText(GlobalTypes.EVENT_NEXT);
                    }
                    break;
                case TABLE_SCREEN:
                    addDateSelectionScreen();
                    nextButton.setText(GlobalTypes.EVENT_NEXT);
                    numberOfRows = 0;
                    break;

                default:
            }
        }
        return true;
    }
}
