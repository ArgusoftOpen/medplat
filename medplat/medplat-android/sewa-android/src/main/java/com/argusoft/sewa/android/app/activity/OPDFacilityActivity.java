package com.argusoft.sewa.android.app.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.component.PagingListView;
import com.argusoft.sewa.android.app.constants.ActivityConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceRestClientImpl;
import com.argusoft.sewa.android.app.databean.ComponentTagBean;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;
import com.argusoft.sewa.android.app.databean.QueryMobDataBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.AnswerBean;
import com.argusoft.sewa.android.app.model.QuestionBean;
import com.argusoft.sewa.android.app.restclient.RestHttpException;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Created by prateek on 3/14/20
 */
@EActivity
public class OPDFacilityActivity extends MenuActivity implements View.OnClickListener {

    @Bean
    SewaServiceImpl sewaService;
    @Bean
    SewaServiceRestClientImpl sewaServiceRestClient;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<QuestionBean, Integer> questionBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<AnswerBean, Integer> answerBeanDao;

    private static final String TAG = "OPDFacilityActivity";
    private final SewaTransformer sewaTransformer = SewaTransformer.getInstance();

    private static final String HEALTH_INFRA_SELECTION_SCREEN = "healthInfraSelectionScreen";
    private static final String MEMBER_SELECTION_SCREEN = "memberSelectionScreen";
    private static final String FORM_CODE = "formCode";

    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private Button nextButton;
    private String screen;

    private List<LinkedHashMap<String, Object>> healthInfraDetailsList;
    private LinkedHashMap<String, Object> selectedHealthInfraDetails;
    private List<LinkedHashMap<String, Object>> memberDetailsList;
    private LinkedHashMap<String, Object> selectedMemberDetails;
    private LinkedHashMap<String, Object> selectedFormConfig;

    private Spinner healthInfraSpinner;
    private int selectedMemberIndex = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
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
        setTitle(UtilBean.getTitleText(LabelConstants.OPD_LAB_TEST));
    }

    private void initView() {
        SharedStructureData.sewaService = sewaService;
        showProcessDialog();
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        setContentView(globalPanel);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);

        if (checkIfOnline(true)) {
            retrieveHealthInfraAssignedToUser();
        }
    }

    private boolean checkIfOnline(boolean navigateToHomescreen) {
        boolean result = false;
        try {
            result = new OnlineStatusChecker().execute(null, null, null).get();
        } catch (ExecutionException e) {
            Log.e(getClass().getSimpleName(), null, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (result) {
            return true;
        }
        hideProcessDialog();
        showOfflineMessage(navigateToHomescreen);
        return false;
    }

    @UiThread
    public void showOfflineMessage(final boolean navigateToHomescreen) {
        View.OnClickListener myListener = v -> {
            alertDialog.dismiss();
            if (navigateToHomescreen)
                navigateToHomeScreen(false);
        };
        alertDialog = new MyAlertDialog(context, false,
                UtilBean.getMyLabel(LabelConstants.NETWORK_CONNECTIVITY_ALERT),
                myListener, DynamicUtils.BUTTON_OK);
        alertDialog.show();
    }

    @Background
    public void retrieveHealthInfraAssignedToUser() {
        try {
            LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
            parameters.put("userId", SewaTransformer.loginBean.getUserID());

            String queryCode = "retrieve_health_infra_for_opd_lab_test";

            QueryMobDataBean queryResponse = sewaServiceRestClient.executeQuery(
                    new QueryMobDataBean(queryCode, null, parameters, 0));

            if (queryResponse != null && queryResponse.getResult() != null && !queryResponse.getResult().isEmpty()) {
                healthInfraDetailsList = queryResponse.getResult();
            } else {
                healthInfraDetailsList = new ArrayList<>();
            }
            showHealthInfraDetails();
        } catch (RestHttpException e) {
            hideProcessDialog();
            generateToast(LabelConstants.ERROR_OCCURRED_SO_TRY_AGAIN);
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @UiThread
    public void showHealthInfraDetails() {
        screen = HEALTH_INFRA_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        if (healthInfraDetailsList == null || healthInfraDetailsList.isEmpty()) {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context,
                    LabelConstants.NO_HEALTH_INFRASTRUCTURE_ASSIGNED));
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
            hideProcessDialog();
            return;
        }

        if (healthInfraDetailsList.size() == 1) {
            selectedHealthInfraDetails = healthInfraDetailsList.get(0);
            retrieveMemberDetails();
            return;
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.SELECT_HEALTH_INFRASTRUCTURE));

        String[] options = new String[healthInfraDetailsList.size()];
        int count = 0;
        Object tmpObj;
        for (LinkedHashMap<String, Object> healthInfra : healthInfraDetailsList) {
            tmpObj = healthInfra.get("healthInfraName");
            if (tmpObj != null) {
                options[count] = tmpObj.toString();
                count++;
            }
        }

        healthInfraSpinner = MyStaticComponents.getSpinner(context, options, 0, -1);
        bodyLayoutContainer.addView(healthInfraSpinner);
        hideProcessDialog();
    }

    @Background
    public void retrieveMemberDetails() {
        try {
            LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
            parameters.put("healthInfraId", selectedHealthInfraDetails.get("id"));

            String queryCode = "retrieve_member_details_for_opd_lab_test";

            QueryMobDataBean queryResponse = sewaServiceRestClient.executeQuery(
                    new QueryMobDataBean(queryCode, null, parameters, 0));
            if (queryResponse != null && queryResponse.getResult() != null && !queryResponse.getResult().isEmpty()) {
                memberDetailsList = queryResponse.getResult();
            } else {
                memberDetailsList = new ArrayList<>();
            }
            showMemberDetails();
        } catch (RestHttpException e) {
            hideProcessDialog();
            generateToast(LabelConstants.ERROR_OCCURRED_SO_TRY_AGAIN);
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @UiThread
    public void generateToast(String message) {
        SewaUtil.generateToast(context, message);
    }

    @UiThread
    public void showMemberDetails() {
        screen = MEMBER_SELECTION_SCREEN;
        bodyLayoutContainer.removeAllViews();

        if (memberDetailsList == null || memberDetailsList.isEmpty()) {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context,
                    UtilBean.getMyLabel(LabelConstants.NO_MEMBER_REGISTER_FOR_LAB_TEST_IN_YOUR_HEALTH_INFRASTRUCTURE)));
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
            hideProcessDialog();
            return;
        }

        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.PLEASE_SELECT_A_MEMBER));

        StringBuilder rbText;
        List<ListItemDataBean> list = new ArrayList<>();

        for (LinkedHashMap<String, Object> memberDetails : memberDetailsList) {
            rbText = new StringBuilder();
            rbText.append(Objects.requireNonNull(memberDetails.get("firstName")));
            Object tmpObj = memberDetails.get("middleName");
            if (tmpObj != null) {
                rbText.append(" ")
                        .append(tmpObj);
            }
            tmpObj = memberDetails.get("lastName");
            if (tmpObj != null) {
                rbText.append(" ")
                        .append(tmpObj);
            }

            list.add(new ListItemDataBean(Objects.requireNonNull(memberDetails.get("healthId")).toString(), rbText.toString(), Objects.requireNonNull(memberDetails.get("testName")).toString()));
        }

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
            if (memberDetailsList.size() > position)
                selectedMemberIndex = position;
        };

        PagingListView notificationListView = MyStaticComponents.getPaginatedListViewWithItem(context, list, R.layout.listview_row_with_info, onItemClickListener, null);
        bodyLayoutContainer.addView(notificationListView);
        hideProcessDialog();
    }

    @Background
    public void openForm() {
        showProcessDialog();
        Object formCode = selectedMemberDetails.get(FORM_CODE);
        Object form = retrieveLabTestForm(formCode);
        if (form != null) {
            Type type = new TypeToken<List<ComponentTagBean>>() {
            }.getType();

            StringBuilder memberName = new StringBuilder();
            memberName.append(Objects.requireNonNull(selectedMemberDetails.get("firstName")));
            Object tmpObj = selectedMemberDetails.get("middleName");
            if (tmpObj != null) {
                memberName.append(" ")
                        .append(tmpObj);
            }
            tmpObj = selectedMemberDetails.get("lastName");
            if (tmpObj != null) {
                memberName.append(" ")
                        .append(tmpObj);
            }

            SharedStructureData.relatedPropertyHashTable.put("nameOfBeneficiary", memberName.toString());

            List<ComponentTagBean> componentTagBeans = new Gson().fromJson(form.toString(), type);
            storeFormDataToDatabase(Objects.requireNonNull(selectedMemberDetails.get(FORM_CODE)).toString(), componentTagBeans);
            Intent intent = new Intent(this, DynamicFormActivity_.class);
            intent.putExtra(SewaConstants.ENTITY, Objects.requireNonNull(selectedMemberDetails.get(FORM_CODE)).toString());
            intent.putExtra("isOPDLabTestForm", true);

            double id = Double.parseDouble(Objects.requireNonNull(selectedMemberDetails.get("labTestDetId")).toString());
            intent.putExtra("labTestDetId", Integer.toString((int) id));
            intent.putExtra("version", Objects.requireNonNull(selectedFormConfig.get("version")).toString());
            startActivityForResult(intent, ActivityConstants.OPD_FACILITY_ACTIVITY_REQUEST_CODE);
        } else {
            generateToast(LabelConstants.FORM_NOT_FOUND);
            hideProcessDialog();
        }
    }

    private Object retrieveLabTestForm(Object formCode) {

        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put(FORM_CODE, formCode);

        String queryCode = "retrieve_lab_test_form_opd_lab_test";

        try {
            QueryMobDataBean queryMobDataBean = sewaServiceRestClient.executeQuery(
                    new QueryMobDataBean(queryCode, null, parameters, 0));
            if (queryMobDataBean != null && queryMobDataBean.getResult() != null && !queryMobDataBean.getResult().isEmpty()) {
                List<LinkedHashMap<String, Object>> result = queryMobDataBean.getResult();
                selectedFormConfig = result.get(0);
                return selectedFormConfig.get("formConfigJson");
            }
        } catch (RestHttpException e) {
            Log.e(TAG, null, e);
        }
        return null;
    }

    private void storeFormDataToDatabase(String sheetName, List<ComponentTagBean> componentTagBeans) {
        if (componentTagBeans != null && !componentTagBeans.isEmpty()) {
            List<AnswerBean> answerBeans;
            List<QuestionBean> questionBeans;
            questionBeans = sewaTransformer.convertComponentDataBeanToQuestionBeanModel(null, componentTagBeans, sheetName);
            answerBeans = sewaTransformer.convertComponentDataBeanToAnswerBeanModel(null, componentTagBeans, sheetName);

            try {
                DeleteBuilder<QuestionBean, Integer> deleteQueBuilder = questionBeanDao.deleteBuilder();
                deleteQueBuilder.where().eq(SewaConstants.QUESTION_BEAN_ENTITY, sheetName);
                PreparedDelete<QuestionBean> prepare = deleteQueBuilder.prepare();
                if (prepare != null) {
                    questionBeanDao.delete(prepare);
                }
                for (QuestionBean questionBean : questionBeans) {
                    questionBeanDao.createOrUpdate(questionBean);
                }

                DeleteBuilder<AnswerBean, Integer> deleteAnsBuilder = answerBeanDao.deleteBuilder();
                deleteAnsBuilder.where().eq(SewaConstants.QUESTION_BEAN_ENTITY, sheetName);
                PreparedDelete<AnswerBean> prepare1 = deleteAnsBuilder.prepare();
                if (prepare1 != null) {
                    answerBeanDao.delete(prepare1);
                }
                for (AnswerBean answerBean : answerBeans) {
                    answerBeanDao.createOrUpdate(answerBean);
                }
            } catch (SQLException ex) {
                Log.e(getClass().getSimpleName(), null, ex);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityConstants.OPD_FACILITY_ACTIVITY_REQUEST_CODE) {
            showProcessDialog();
            selectedMemberIndex = -1;
            if (resultCode == RESULT_OK) {
                retrieveMemberDetails();
            } else {
                showMemberDetails();
            }
        }
        hideProcessDialog();
    }

    @Override
    public void onClick(View v) {
        if (screen == null) {
            navigateToHomeScreen(false);
            return;
        }

        if (!checkIfOnline(false)) {
            return;
        }

        if (v.getId() == DynamicUtils.ID_NEXT_BUTTON) {
            switch (screen) {
                case HEALTH_INFRA_SELECTION_SCREEN:
                    if (healthInfraDetailsList == null || healthInfraDetailsList.isEmpty()) {
                        navigateToHomeScreen(false);
                        finish();
                        return;
                    }

                    if (healthInfraSpinner.getSelectedItemPosition() != -1) {
                        selectedHealthInfraDetails = healthInfraDetailsList.get(healthInfraSpinner.getSelectedItemPosition());
                        retrieveMemberDetails();
                    } else {
                        SewaUtil.generateToast(context, LabelConstants.HEALTH_INFRASTRUCTURE_SELECTION_REQUIRED_ALERT);
                    }
                    break;
                case MEMBER_SELECTION_SCREEN:
                    if (memberDetailsList == null || memberDetailsList.isEmpty()) {
                        navigateToHomeScreen(false);
                        finish();
                        return;
                    }

                    if (selectedMemberIndex == -1) {
                        SewaUtil.generateToast(context, LabelConstants.MEMBER_SELECTION_REQUIRED_TO_CONTINUE);
                        return;
                    }

                    selectedMemberDetails = memberDetailsList.get(selectedMemberIndex);
                    openForm();
                    break;
                default:
            }
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
                case HEALTH_INFRA_SELECTION_SCREEN:
                    navigateToHomeScreen(false);
                    break;
                case MEMBER_SELECTION_SCREEN:
                    if (healthInfraDetailsList.size() == 1) {
                        navigateToHomeScreen(false);
                    } else {
                        nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_NEXT));
                        showHealthInfraDetails();
                    }
                    break;
                default:
            }
            return true;
        }
        return false;
    }

    private static class OnlineStatusChecker extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... data) {
            return SharedStructureData.sewaService.isOnline();
        }
    }
}
