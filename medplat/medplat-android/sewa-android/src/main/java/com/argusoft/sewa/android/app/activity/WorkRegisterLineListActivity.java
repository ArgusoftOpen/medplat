package com.argusoft.sewa.android.app.activity;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceRestClientImpl;
import com.argusoft.sewa.android.app.databean.QueryMobDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.restclient.RestHttpException;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@EActivity
public class WorkRegisterLineListActivity extends MenuActivity {

    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaServiceRestClientImpl sewaServiceRestClient;

    private String visitId = null;
    private String serviceType = null;

    private LinearLayout bodyLayoutContainer;
    private MyAlertDialog myAlertDialog;
    private LinearLayout globalPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            visitId = extras.getString("visitId");
            serviceType = extras.getString("serviceType");
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
        setTitle(UtilBean.getTitleText(LabelConstants.WORK_REGISTER_TITLE));
        setSubTitle(null);
    }

    private void initView() {
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, null);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        LinearLayout footer = globalPanel.findViewById(DynamicUtils.ID_FOOTER);
        globalPanel.removeView(footer);
        setBodyDetail();
    }

    private void setBodyDetail() {
        showNotOnlineMessage();
        retrieveData();
    }

    @UiThread
    public void showNotOnlineMessage() {
        if (!sewaService.isOnline()) {
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
    }

    @Background
    public void retrieveData() {
        try {
            LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
            parameters.put("visitId", visitId);

            List<QueryMobDataBean> queryMobDataBeans = new ArrayList<>();
            List<String> titleTexts = new ArrayList<>();
            List<String> instructionTexts = new ArrayList<>();

            switch (serviceType) {
                case "FHW_LMP":
                    queryMobDataBeans.add(new QueryMobDataBean("mob_work_register_detail_lmp", null, parameters, 0));
                    titleTexts.add(UtilBean.getMyLabel(LabelConstants.LMP_FOLLOW_UP_SERVICE_DETAILS));
                    break;
                case "ASHA_LMP":
                    queryMobDataBeans.add(new QueryMobDataBean("mob_asha_work_register_detail_lmp", null, parameters, 0));
                    titleTexts.add(UtilBean.getMyLabel(LabelConstants.LMP_FOLLOW_UP_SERVICE_DETAILS));
                    break;
                case "FHW_ANC":
                    queryMobDataBeans.add(new QueryMobDataBean("mob_work_register_detail_anc", null, parameters, 0));
                    titleTexts.add(UtilBean.getMyLabel(LabelConstants.ANC_SERVICE_DETAILS));
                    break;
                case "ASHA_ANC":
                    queryMobDataBeans.add(new QueryMobDataBean("mob_asha_work_register_detail_anc", null, parameters, 0));
                    titleTexts.add(UtilBean.getMyLabel(LabelConstants.ANC_SERVICE_DETAILS));
                    break;
                case "FHW_MOTHER_WPD":
                    queryMobDataBeans.add(new QueryMobDataBean("mob_work_register_detail_wpd_mother", null, parameters, 0));
                    titleTexts.add(UtilBean.getMyLabel("Delivery Registration Details"));
                    instructionTexts.add(UtilBean.getMyLabel("Mother Details"));
                    queryMobDataBeans.add(new QueryMobDataBean("mob_work_register_detail_wpd_child", null, parameters, 0));
                    instructionTexts.add(UtilBean.getMyLabel(LabelConstants.CHILD_DETAILS));
                    break;
                case "FHW_PNC":
                    queryMobDataBeans.add(new QueryMobDataBean("mob_work_register_detail_pnc_mother", null, parameters, 0));
                    titleTexts.add(UtilBean.getMyLabel(LabelConstants.PNC_SERVICE_DETAILS));
                    instructionTexts.add(UtilBean.getMyLabel(LabelConstants.MOTHER_DETAILS));
                    queryMobDataBeans.add(new QueryMobDataBean("mob_work_register_detail_pnc_child", null, parameters, 0));
                    instructionTexts.add(UtilBean.getMyLabel(LabelConstants.CHILD_DETAILS));
                    break;
                case "ASHA_PNC":
                    queryMobDataBeans.add(new QueryMobDataBean("mob_asha_work_register_detail_pnc_mother", null, parameters, 0));
                    titleTexts.add(UtilBean.getMyLabel(LabelConstants.PNC_SERVICE_DETAILS));
                    instructionTexts.add(UtilBean.getMyLabel(LabelConstants.MOTHER_DETAILS));
                    queryMobDataBeans.add(new QueryMobDataBean("mob_asha_work_register_detail_pnc_child", null, parameters, 0));
                    instructionTexts.add(UtilBean.getMyLabel(LabelConstants.CHILD_DETAILS));
                    break;
                case "FHW_CS":
                    queryMobDataBeans.add(new QueryMobDataBean("mob_work_register_detail_cs", null, parameters, 0));
                    titleTexts.add(UtilBean.getMyLabel(LabelConstants.CHILD_SERVICE_DETAILS));
                    break;
                case "ASHA_CS":
                    queryMobDataBeans.add(new QueryMobDataBean("mob_asha_work_register_detail_cs", null, parameters, 0));
                    titleTexts.add(UtilBean.getMyLabel(LabelConstants.CHILD_SERVICE_DETAILS));
                    break;
                case "NCD_CBAC":
                    queryMobDataBeans.add(new QueryMobDataBean("mob_asha_work_register_detail_cbac", null, parameters, 0));
                    titleTexts.add(UtilBean.getMyLabel(LabelConstants.CBAC_SCREENING_DETAILS));
                    break;
                case "NCD_HYPERTENSION":
                    queryMobDataBeans.add(new QueryMobDataBean("mob_work_register_detail_hypertension", null, parameters, 0));
                    titleTexts.add(UtilBean.getMyLabel(LabelConstants.HYPERTENSION_SCREENING_DETAILS));
                    break;
                case "NCD_DIABETES":
                    queryMobDataBeans.add(new QueryMobDataBean("mob_work_register_detail_diabetes", null, parameters, 0));
                    titleTexts.add(UtilBean.getMyLabel(LabelConstants.DIABETES_SCREENING_DETAILS));
                    break;
                case "NCD_ORAL":
                    queryMobDataBeans.add(new QueryMobDataBean("mob_work_register_detail_oral", null, parameters, 0));
                    titleTexts.add(UtilBean.getMyLabel(LabelConstants.ORAL_CANCER_SCREENING_DETAILS));
                    break;
                case "NCD_BREAST":
                    queryMobDataBeans.add(new QueryMobDataBean("mob_work_register_detail_breast", null, parameters, 0));
                    titleTexts.add(UtilBean.getMyLabel(LabelConstants.BREAST_CANCER_SCREENING_DETAILS));
                    break;
                case "NCD_CERVICAL":
                    queryMobDataBeans.add(new QueryMobDataBean("mob_work_register_detail_cervical", null, parameters, 0));
                    titleTexts.add(UtilBean.getMyLabel(LabelConstants.CERVICAL_CANCER_SCREENING_DETAILS));
                    break;
                case "ASHA_NPCB":
                    queryMobDataBeans.add(new QueryMobDataBean("mob_asha_work_register_detail_npcb", null, parameters, 0));
                    titleTexts.add(UtilBean.getMyLabel(LabelConstants.NPCB_SCREENING_DETAILS));
                    break;
                default:
            }

            if (queryMobDataBeans.isEmpty()) {
                hideProcessDialog();
                return;
            }

            int count = 0;
            for (QueryMobDataBean queryMobDataBean : queryMobDataBeans) {
                count++;
                QueryMobDataBean queryResponse = sewaServiceRestClient.executeQuery(queryMobDataBean);

                if (queryResponse != null && queryResponse.getResult() != null) {
                    List<LinkedHashMap<String, Object>> resultList = queryResponse.getResult();
                    if (resultList != null && !resultList.isEmpty()) {
                        String title = null;
                        String instruction = null;
                        if (titleTexts.size() >= count) {
                            title = titleTexts.get(count - 1);
                        }
                        if (instructionTexts.size() >= count) {
                            instruction = instructionTexts.get(count - 1);
                        }
                        addLineListDetails(resultList, title, instruction);
                    } else {
                        if (queryMobDataBeans.size() == 1) {
                            runOnUiThread(() -> bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context,
                                    UtilBean.getMyLabel(LabelConstants.ERROR_OCCURRED_SO_TRY_AGAIN))));
                        }
                    }
                } else {
                    if (count == queryMobDataBeans.size()) {
                        runOnUiThread(() -> bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context,
                                UtilBean.getMyLabel(LabelConstants.ERROR_OCCURRED_SO_TRY_AGAIN))));
                    }
                }
            }
            hideProcessDialog();
        } catch (RestHttpException e) {
            runOnUiThread(() -> {
                bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context,
                        UtilBean.getMyLabel(LabelConstants.ERROR_OCCURRED_SO_TRY_AGAIN)));
                hideProcessDialog();
            });
            Log.e(getClass().getName(), null, e);
        }
    }

    @UiThread
    public void addLineListDetails(List<LinkedHashMap<String, Object>> results, String title, String instruction) {
        if (title != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateTitleView(context, title));
        }
        if (instruction != null) {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context, instruction));
        }

        for (LinkedHashMap<String, Object> map : results) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey() != null && !entry.getKey().isEmpty() &&
                        entry.getValue() != null && !entry.getValue().toString().isEmpty()) {
                    bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context,
                            UtilBean.getMyLabel(entry.getKey())));
                    bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(context,
                            UtilBean.getMyLabel(entry.getValue().toString())));
                }
            }
        }
    }

    @UiThread
    public void addTable(List<LinkedHashMap<String, Object>> results) {
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setPadding(10, 10, 10, 10);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        tableLayout.addView(createTableRow(UtilBean.getMyLabel(LabelConstants.FIELD), UtilBean.getMyLabel(LabelConstants.VALUE), true), 0);

        int count = 1;
        for (LinkedHashMap<String, Object> map : results) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                tableLayout.addView(createTableRow(entry.getKey(), entry.getValue(), false), count);
                count++;
            }
        }
        bodyLayoutContainer.addView(tableLayout);
        hideProcessDialog();
    }

    private TableRow createTableRow(String field, Object value, boolean isHeader) {
        TableRow.LayoutParams layoutParamsForRow = new TableRow.LayoutParams(MATCH_PARENT);
        TableRow row = new TableRow(this);
        row.setPadding(10, 15, 10, 15);
        if (SewaUtil.CURRENT_THEME == R.style.techo_training_app) {
            row.setBackgroundResource(R.drawable.table_row_selector);
        } else {
            row.setBackgroundResource(R.drawable.spinner_item_border);
        }
        row.setLayoutParams(layoutParamsForRow);

        MaterialTextView textView1 = new MaterialTextView(context);
        textView1.setGravity(Gravity.CENTER);
        textView1.setPadding(10, 10, 10, 10);
        textView1.setText(field);
        if (isHeader) {
            textView1.setTypeface(Typeface.DEFAULT_BOLD);
        }
        row.addView(textView1, new TableRow.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 2));

        MaterialTextView textView2 = new MaterialTextView(context);
        textView2.setGravity(Gravity.CENTER);
        textView2.setPadding(10, 10, 10, 10);
        if (value != null) {
            textView2.setText(value.toString());
        } else {
            textView2.setText(LabelConstants.N_A);
        }
        if (isHeader) {
            textView2.setTypeface(Typeface.DEFAULT_BOLD);
        }
        row.addView(textView2, new TableRow.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1));

        return row;
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
