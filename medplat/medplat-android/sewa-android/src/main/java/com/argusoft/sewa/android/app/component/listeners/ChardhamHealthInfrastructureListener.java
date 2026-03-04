package com.argusoft.sewa.android.app.component.listeners;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyProcessDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.FormulaConstants;
import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.HealthInfrastructureBean;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ChardhamHealthInfrastructureListener implements AdapterView.OnItemSelectedListener {

    private static final long DELAY = 1000L;
    private Context context;
    private QueFormBean queFormBean;
    private LinearLayout linearLayout;
    private Spinner typeSpinner;
    private Map<Long, String> infraTypeMap;
    private MyProcessDialog processDialog;
    private Long selectedInfraTypeId;
    private List<HealthInfrastructureBean> infrastructureBeans = new ArrayList<>();
    private HealthInfrastructureBean userAssignedHealthInfraBean = new HealthInfrastructureBean();
    private TextView infraSelectionTextView;
    private Spinner infraSelectionSpinner;
    private MaterialTextView noInfraTextView;
    private TextInputLayout searchInfraEditText;
    private Timer timer = new Timer();

    public ChardhamHealthInfrastructureListener(Context context, QueFormBean queFormBean, LinearLayout linearLayout, Spinner typeSpinner, Map<Long, String> infraType) {
        this.context = context;
        this.queFormBean = queFormBean;
        this.linearLayout = linearLayout;
        this.typeSpinner = typeSpinner;
        this.infraTypeMap = infraType;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        queFormBean.setAnswer(null);

        linearLayout.removeAllViews();
        linearLayout.addView(MyStaticComponents.generateQuestionView(
                null, null, context, LabelConstants.SELECT_HEALTH_INFRASTRUCTURE_TYPE));
        linearLayout.addView(typeSpinner);

        if (!typeSpinner.getSelectedItem().equals(UtilBean.getMyLabel(GlobalTypes.SELECT))
                && !typeSpinner.getSelectedItem().equals(GlobalTypes.SELECT)) {
            for (Map.Entry<Long, String> entry : infraTypeMap.entrySet()) {
                if (entry.getValue().equals(typeSpinner.getSelectedItem())) {
                    selectedInfraTypeId = entry.getKey();
                    Log.i(getClass().getSimpleName(), "Selected Health Infrastructure Type : " + entry.getKey() + " : : " + entry.getValue());
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.HEALTH_INFRASTRUCTURE_TYPE, entry.getValue());
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.HEALTH_INFRASTRUCTURE_TYPE_ID, entry.getKey().toString());
                    break;
                }
            }
            String validationMessage = null;
            if (queFormBean.getValidations() != null) {
                for (ValidationTagBean validation : queFormBean.getValidations()) {
                    if (validation.getMethod().contains("-")) {
                        String[] split = validation.getMethod().split("-");
                        if (split[0].equalsIgnoreCase(FormulaConstants.VALIDATION_CHECK_SERVICE_DATE_FOR_HEALTH_INFRA)) {
                            ArrayList<ValidationTagBean> validationTagBeans = new ArrayList<>();
                            validationTagBeans.add(validation);
                            validationMessage = DynamicUtils.checkValidation(selectedInfraTypeId.toString(), 0, validationTagBeans);
                        }
                    } else if (validation.getMethod().equalsIgnoreCase(FormulaConstants.VALIDATION_CHECK_SERVICE_DATE_FOR_HEALTH_INFRA)) {
                        ArrayList<ValidationTagBean> validationTagBeans = new ArrayList<>();
                        validationTagBeans.add(validation);
                        validationMessage = DynamicUtils.checkValidation(selectedInfraTypeId.toString(), 0, validationTagBeans);
                    }
                }
            }
            if (validationMessage != null) {
                SewaUtil.generateToast(SharedStructureData.context, validationMessage);
            } else {
                if (infraTypeMap.containsValue("Kiosk")) {
                    retrieveInfrastructuresFromDB();
                } else {
                    addSearchLayoutForInfrastructure();
                    if (searchInfraEditText.getEditText() != null) {
                        retrieveInfrastructuresBySearchFromDB(searchInfraEditText.getEditText().getText().toString());
                    }
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do Nothing
    }

    private void addSearchLayoutForInfrastructure() {
        linearLayout.removeView(searchInfraEditText);

        if (searchInfraEditText == null) {
            searchInfraEditText = MyStaticComponents.getChardhamEditText(context,
                    UtilBean.getMyLabel(LabelConstants.SEARCH_HEALTH_INFRASTRUCTURE),
                    IdConstants.HEALTH_INFRA_SEARCH_BY_NAME_EDIT_TEXT_ID,
                    100, -1);
            searchInfraEditText.setStartIconDrawable(R.drawable.ic_baseline_search_24);

            if (searchInfraEditText.getEditText() != null) {
                searchInfraEditText.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //Do Nothing
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
                                            ((Activity) context).runOnUiThread(() -> {
                                                linearLayout.removeView(infraSelectionTextView);
                                                linearLayout.removeView(infraSelectionSpinner);
                                                linearLayout.removeView(noInfraTextView);
                                                retrieveInfrastructuresBySearchFromDB(s.toString());
                                                searchInfraEditText.setFocusable(true);
                                            });
                                        } else if (s != null && s.length() == 0) {
                                            ((Activity) context).runOnUiThread(() -> {
                                                linearLayout.removeView(infraSelectionTextView);
                                                linearLayout.removeView(infraSelectionSpinner);
                                                linearLayout.removeView(noInfraTextView);
                                                infrastructureBeans.clear();
                                            });
                                        }
                                    }
                                },
                                DELAY
                        );
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        //Do Nothing
                    }
                });
            }
        }
        searchInfraEditText.setPadding(0, 20, 0, 0);
        linearLayout.addView(searchInfraEditText);
    }

    private void retrieveInfrastructuresFromDB() {
        queFormBean.setAnswer(null);
        processDialog = new MyProcessDialog(context, GlobalTypes.MSG_PROCESSING);
        processDialog.show();
        infrastructureBeans = SharedStructureData.healthInfrastructureService.retrieveHealthInfrastructures(selectedInfraTypeId, queFormBean.getDatamap());
        addInfrastructureSelectionLayout();
    }

    private void retrieveInfrastructuresBySearchFromDB(final String search) {
        queFormBean.setAnswer(null);
        if (search != null && search.length() > 2) {
            processDialog = new MyProcessDialog(context, GlobalTypes.MSG_PROCESSING);
            processDialog.show();
            infrastructureBeans = SharedStructureData.healthInfrastructureService.retrieveHealthInfraBySearch(search, selectedInfraTypeId, queFormBean.getDatamap());
            addInfrastructureSelectionLayout();
        }
    }

    private void addInfrastructureSelectionLayout() {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                linearLayout.removeView(infraSelectionTextView);
                linearLayout.removeView(infraSelectionSpinner);
                linearLayout.removeView(noInfraTextView);

                if (infrastructureBeans.isEmpty()) {
                    noInfraTextView = MyStaticComponents.generateInstructionView(context, LabelConstants.NO_HEALTH_INFRASTRUCTURE_AVAILABLE_ON_SELECTED_LOCATION);
                    linearLayout.addView(noInfraTextView);
                    if (processDialog != null && processDialog.isShowing()) {
                        processDialog.dismiss();
                    }
                    return;
                }

                if (infraSelectionTextView == null) {
                    infraSelectionTextView = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.SELECT_HEALTH_INFRASTRUCTURE);
                }

                String[] arrayOfOptions = new String[infrastructureBeans.size() + 1];
                int defaultIndex = 0;
                if (!infrastructureBeans.isEmpty()) {
                    int i = 1;
                    arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
                    for (HealthInfrastructureBean infrastructureBean : infrastructureBeans) {
                        arrayOfOptions[i] = infrastructureBean.getName();
                        i++;
                    }
                } else {
                    arrayOfOptions[0] = UtilBean.getMyLabel(LabelConstants.NO_HEALTH_INFRASTRUCTURE_AVAILABLE);
                }

                userAssignedHealthInfraBean = SharedStructureData.healthInfrastructureService.retrieveHealthInfrastructureAssignedToUser(SewaTransformer.loginBean.getUserID());
                infraSelectionSpinner = MyStaticComponents.getSpinner(context, arrayOfOptions, defaultIndex, 1);

                if (userAssignedHealthInfraBean != null) {
                    infraSelectionSpinner.setSelection(Arrays.asList(arrayOfOptions).indexOf(userAssignedHealthInfraBean.getName()));
                }

                infraSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            HealthInfrastructureBean selectedInfra = infrastructureBeans.get(position - 1);
                            Log.i(getClass().getSimpleName(), "Selected Health Infrastructure : " + selectedInfra);
                            queFormBean.setAnswer(selectedInfra.getActualId());
                            SharedStructureData.selectedHealthInfra = selectedInfra;
                        } else {
                            queFormBean.setAnswer(null);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        //Do Nothing
                    }
                });

                linearLayout.addView(infraSelectionTextView);
                linearLayout.addView(infraSelectionSpinner);

                if (processDialog != null && processDialog.isShowing()) {
                    processDialog.dismiss();
                }
            }
        });
    }
}
