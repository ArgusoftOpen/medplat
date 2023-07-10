package com.argusoft.sewa.android.app.component.listeners;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import com.argusoft.sewa.android.app.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.argusoft.sewa.android.app.component.MyProcessDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.FormulaConstants;
import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RchConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.HealthInfrastructureBean;
import com.argusoft.sewa.android.app.model.LocationMasterBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class HealthInfrastructureListener implements AdapterView.OnItemSelectedListener {

    private static final long DELAY = 1000L;
    private Context context;
    private QueFormBean queFormBean;
    private LinearLayout linearLayout;
    private Spinner typeSpinner;
    private Map<Long, String> infraTypeMap;
    private MyProcessDialog processDialog;
    private Long selectedInfraTypeId;
    private RadioGroup searchTypeRadioGroup;
    private TextView searchTypeTextView;
    private List<HealthInfrastructureBean> infrastructureBeans = new ArrayList<>();
    private TextView infraSelectionTextView;
    private Spinner infraSelectionSpinner;
    private MaterialTextView noInfraTextView;
    private TextView searchInfraTextView;
    private TextInputLayout searchInfraEditText;
    private Timer timer = new Timer();
    private TextView hierarchyTextView;
    private LinearLayout hierarchyLayout;

    private Map<Integer, List<LocationMasterBean>> locationMap = new HashMap<>();
    private Map<Integer, MaterialTextView> selectLocationQueMap = new HashMap<>();
    private Map<Integer, Spinner> spinnerMap = new HashMap<>();

    private TextView selectDistrictQues;
    private Spinner districtSpinner;
    private TextView selectBlockQues;
    private Spinner blockSpinner;
    private TextView selectPhcQues;
    private Spinner phcSpinner;
    private TextView selectSubCenterQues;
    private Spinner subCenterSpinner;
    private TextView selectVillageQues;
    private Spinner villageSpinner;

    private Map<String, Long> selectionMap = new HashMap<>();
    private Long selectedLocationId = null;

    public HealthInfrastructureListener(Context context, QueFormBean queFormBean, LinearLayout linearLayout, Spinner typeSpinner, Map<Long, String> infraType) {
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
                setSearchTypeView();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do Nothing
    }

    private void setSearchTypeView() {
        queFormBean.setAnswer(null);
        linearLayout.removeView(infraSelectionTextView);
        linearLayout.removeView(infraSelectionSpinner);
        linearLayout.removeView(noInfraTextView);
        linearLayout.removeView(searchTypeTextView);
        linearLayout.removeView(searchTypeRadioGroup);

        if (searchTypeTextView == null) {
            searchTypeTextView = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.CHOOSE_OPTION_TO_SEARCH);
        }

        if (searchTypeRadioGroup == null) {
            searchTypeRadioGroup = new RadioGroup(context);
            searchTypeRadioGroup.setPadding(0, 0, 0, 50);
            RadioButton searchByName = MyStaticComponents.getRadioButton(context, LabelConstants.SEARCH_BY_NAME,
                    IdConstants.HEALTH_INFRA_SEARCH_BY_NAME_RADIO_BUTTON_ID);
            searchTypeRadioGroup.addView(searchByName);
            RadioButton searchByHierarchy = MyStaticComponents.getRadioButton(context, LabelConstants.SEARCH_BY_HIERARCHY,
                    IdConstants.HEALTH_INFRA_SEARCH_BY_HIERARCHY_RADIO_BUTTON_ID);
            searchTypeRadioGroup.addView(searchByHierarchy);

            searchTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                queFormBean.setAnswer(null);
                linearLayout.removeView(infraSelectionTextView);
                linearLayout.removeView(infraSelectionSpinner);
                linearLayout.removeView(noInfraTextView);
                if (checkedId == IdConstants.HEALTH_INFRA_SEARCH_BY_NAME_RADIO_BUTTON_ID) {
                    addSearchLayoutForInfrastructure();
                    if (searchInfraEditText.getEditText() != null) {
                        retrieveInfrastructuresBySearchFromDB(searchInfraEditText.getEditText().getText().toString());
                    }
                } else if (checkedId == IdConstants.HEALTH_INFRA_SEARCH_BY_HIERARCHY_RADIO_BUTTON_ID) {
                    addHierarchyLayoutForInfrastructure();
                    retrieveInfrastructureByLocationFromDB();
                }
            });
        }

        linearLayout.addView(searchTypeTextView);
        linearLayout.addView(searchTypeRadioGroup);

        if (searchTypeRadioGroup.getCheckedRadioButtonId() == IdConstants.HEALTH_INFRA_SEARCH_BY_NAME_RADIO_BUTTON_ID) {
            addSearchLayoutForInfrastructure();
            if (searchInfraEditText.getEditText() != null) {
                retrieveInfrastructuresBySearchFromDB(searchInfraEditText.getEditText().getText().toString());
            }
        } else if (searchTypeRadioGroup.getCheckedRadioButtonId() == IdConstants.HEALTH_INFRA_SEARCH_BY_HIERARCHY_RADIO_BUTTON_ID) {
            addHierarchyLayoutForInfrastructure();
            retrieveInfrastructureByLocationFromDB();
        }
    }

    private void addSearchLayoutForInfrastructure() {
        linearLayout.removeView(hierarchyTextView);
        linearLayout.removeView(hierarchyLayout);
        linearLayout.removeView(searchInfraTextView);
        linearLayout.removeView(searchInfraEditText);

        if (searchInfraTextView == null) {
            searchInfraTextView = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.SEARCH_HEALTH_INFRASTRUCTURE);
        }

        if (searchInfraEditText == null) {
            searchInfraEditText = MyStaticComponents.getEditText(context,
                    UtilBean.getMyLabel(LabelConstants.SEARCH_HEALTH_INFRASTRUCTURE),
                    IdConstants.HEALTH_INFRA_SEARCH_BY_NAME_EDIT_TEXT_ID,
                    100, -1);
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

        linearLayout.addView(searchInfraTextView);
        linearLayout.addView(searchInfraEditText);
    }

    private void addHierarchyLayoutForInfrastructure() {
        linearLayout.removeView(hierarchyTextView);
        linearLayout.removeView(hierarchyLayout);
        linearLayout.removeView(searchInfraTextView);
        linearLayout.removeView(searchInfraEditText);

        if (hierarchyTextView == null) {
            hierarchyTextView = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.SELECT_HIERARCHY_FOR_HEALTH_INFRASTRUCTURE);
        }

        if (hierarchyLayout == null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            hierarchyLayout = MyStaticComponents.getLinearLayout(context,
                    IdConstants.HEALTH_INFRA_SEARCH_BY_HIERARCHY_LAYOUT_ID,
                    LinearLayout.VERTICAL, layoutParams);
            addSpinnersForLocationHierarchy();
        }

        linearLayout.addView(hierarchyTextView);
        linearLayout.addView(hierarchyLayout);
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

    private void retrieveInfrastructureByLocationFromDB() {
        queFormBean.setAnswer(null);
        if (selectedLocationId != null) {
            processDialog = new MyProcessDialog(context, GlobalTypes.MSG_PROCESSING);
            processDialog.show();
            infrastructureBeans = SharedStructureData.healthInfrastructureService.retrieveHealthInfraByLocationId(selectedLocationId, selectedInfraTypeId, queFormBean.getDatamap());
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

                if (selectedInfraTypeId.equals(RchConstants.INFRA_PRIVATE_HOSPITAL)) {
                    HealthInfrastructureBean infrastructureBean = new HealthInfrastructureBean();
                    infrastructureBean.setActualId(-1L);
                    infrastructureBean.setTypeId(RchConstants.INFRA_PRIVATE_HOSPITAL);
                    infrastructureBean.setName(UtilBean.getMyLabel(LabelConstants.OTHER));
                    infrastructureBeans.add(infrastructureBean);
                }

                if (selectedInfraTypeId.equals(RchConstants.INFRA_TRUST_HOSPITAL)) {
                    HealthInfrastructureBean infrastructureBean = new HealthInfrastructureBean();
                    infrastructureBean.setActualId(-1L);
                    infrastructureBean.setTypeId(RchConstants.INFRA_TRUST_HOSPITAL);
                    infrastructureBean.setName(UtilBean.getMyLabel(LabelConstants.OTHER));
                    infrastructureBeans.add(infrastructureBean);
                }

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

                infraSelectionSpinner = MyStaticComponents.getSpinner(context, arrayOfOptions, defaultIndex, 1);
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

    private void addSpinnersForLocationHierarchy() {
        locationMap.clear();
        spinnerMap.clear();
        selectLocationQueMap.clear();
        infrastructureBeans.clear();

        List<LocationMasterBean> mainLocations = SharedStructureData.locationMasterService.getLocationWithNoParent();

        List<LocationMasterBean> locationMasterBeans = mainLocations;

        if (mainLocations.size() == 1) {
            locationMap.put(mainLocations.get(0).getLevel(), mainLocations);
            locationMasterBeans = SharedStructureData.locationMasterService.retrieveLocationMasterBeansByLevelAndParent(
                    mainLocations.get(0).getLevel() + 1,
                    mainLocations.get(0).getActualID());
        }

        String locationType = null;
        String[] arrayOfOptions = new String[locationMasterBeans.size() + 1];
        arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
        int i = 1;
        for (LocationMasterBean locationBean : locationMasterBeans) {
            locationType = locationBean.getType();
            arrayOfOptions[i] = locationBean.getName();
            i++;
        }

        Integer level = locationMasterBeans.get(0).getLevel();
        locationMap.put(level, locationMasterBeans);

        Spinner spinner = MyStaticComponents.getSpinner(context, arrayOfOptions, 0, level);
        spinner.setOnItemSelectedListener(getOnItemSelectedListenerForHierarchy());

        String name = SharedStructureData.locationMasterService.getLocationTypeNameByType(locationType);
        MaterialTextView selectTextView = MyStaticComponents.generateQuestionView(null, null, context, "Select " + name);
        hierarchyLayout.addView(selectTextView);
        hierarchyLayout.addView(spinner);
        spinnerMap.put(level, spinner);
        selectLocationQueMap.put(level, selectTextView);
    }

    private AdapterView.OnItemSelectedListener getOnItemSelectedListenerForHierarchy() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int level = parent.getId();
                if (position != 0) {
                    List<LocationMasterBean> locationMasterBeans = locationMap.get(level);
                    if (locationMasterBeans != null && !locationMasterBeans.isEmpty()) {
                        LocationMasterBean locationBean = locationMasterBeans.get(position - 1);
                        selectionMap.put(locationBean.getLevel().toString(), locationBean.getActualID());
                        selectedLocationId = locationBean.getActualID();
                        addSpinners(locationBean);
                    } else {
                        removeSpinners(level);
                        selectedLocationId = selectionMap.get(String.valueOf(level));
                    }
                } else {
                    removeSpinners(level);
                    selectedLocationId = selectionMap.get(String.valueOf(level));
                }

                hierarchyLayout.removeView(infraSelectionTextView);
                hierarchyLayout.removeView(infraSelectionSpinner);
                hierarchyLayout.removeView(noInfraTextView);
                retrieveInfrastructureByLocationFromDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private void addSpinners(LocationMasterBean parent) {
        List<LocationMasterBean> locationBeans = SharedStructureData.locationMasterService.retrieveLocationMasterBeansByLevelAndParent(
                parent.getLevel() + 1,
                parent.getActualID());
        if (locationBeans != null && !locationBeans.isEmpty()) {
            locationMap.put(parent.getLevel() + 1, locationBeans);
            removeSpinners(parent.getLevel());

            String locationType = null;
            String[] arrayOfOptions = new String[locationBeans.size() + 1];
            arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
            int i = 1;
            for (LocationMasterBean location : locationBeans) {
                locationType = location.getType();
                arrayOfOptions[i] = location.getName();
                i++;
            }

            Spinner spinner = MyStaticComponents.getSpinner(context, arrayOfOptions, 0, parent.getLevel() + 1);
            spinner.setOnItemSelectedListener(getOnItemSelectedListenerForHierarchy());

            String name = SharedStructureData.locationMasterService.getLocationTypeNameByType(locationType);
            MaterialTextView selectTextView = MyStaticComponents.generateQuestionView(null, null, context, "Select " + name);
            hierarchyLayout.addView(selectTextView);
            hierarchyLayout.addView(spinner);
            spinnerMap.put(parent.getLevel() + 1, spinner);
            selectLocationQueMap.put(parent.getLevel() + 1, selectTextView);
        } else {
            removeSpinners(parent.getLevel());
            selectedLocationId = selectionMap.get(String.valueOf(parent.getLevel()));
        }
    }

    private void removeSpinners(Integer level) {
        for (Map.Entry<Integer, List<LocationMasterBean>> entry : locationMap.entrySet()) {
            if (entry.getKey() > level) {
                hierarchyLayout.removeView(selectLocationQueMap.get(entry.getKey()));
                hierarchyLayout.removeView(spinnerMap.get(entry.getKey()));
                selectionMap.remove(entry.getKey().toString());
            }
        }
    }

    private void addRegionSpinner() {
        final List<LocationMasterBean> locationMasterBeans = SharedStructureData.locationMasterService.retrieveLocationMasterBeansByLevelAndParent(1, 2L);
        String[] arrayOfOptions = new String[locationMasterBeans.size() + 1];
        int defaultIndex = 0;
        if (!locationMasterBeans.isEmpty()) {
            int i = 1;
            arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
            for (LocationMasterBean locationMasterBean : locationMasterBeans) {
                arrayOfOptions[i] = locationMasterBean.getName();
                i++;
            }
        } else {
            arrayOfOptions[0] = UtilBean.getMyLabel(LabelConstants.NO_LOCATION_AVAILABLE);
        }

        Spinner regionSpinner = MyStaticComponents.getSpinner(context, arrayOfOptions, defaultIndex, 1);
        regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectionMap.remove("2");
                selectionMap.remove("3");
                selectionMap.remove("4");
                selectionMap.remove("5");
                selectionMap.remove("6");
                hierarchyLayout.removeView(selectDistrictQues);
                hierarchyLayout.removeView(districtSpinner);
                hierarchyLayout.removeView(selectBlockQues);
                hierarchyLayout.removeView(blockSpinner);
                hierarchyLayout.removeView(selectPhcQues);
                hierarchyLayout.removeView(phcSpinner);
                hierarchyLayout.removeView(selectSubCenterQues);
                hierarchyLayout.removeView(subCenterSpinner);
                hierarchyLayout.removeView(selectVillageQues);
                hierarchyLayout.removeView(villageSpinner);
                if (position != 0) {
                    LocationMasterBean selectedLocation = locationMasterBeans.get(position - 1);
                    selectionMap.put("1", selectedLocation.getActualID());
                    selectedLocationId = selectedLocation.getActualID();
                    addDistrictSpinner(selectedLocation.getActualID());
                } else {
                    selectedLocationId = null;
                    infrastructureBeans.clear();
                }

                linearLayout.removeView(infraSelectionTextView);
                linearLayout.removeView(infraSelectionSpinner);
                linearLayout.removeView(noInfraTextView);
                retrieveInfrastructureByLocationFromDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Nothing
            }
        });

        TextView selectRegionQues = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.REGION);
        hierarchyLayout.addView(selectRegionQues);
        hierarchyLayout.addView(regionSpinner);
    }

    private void addDistrictSpinner(Long parent) {
        final List<LocationMasterBean> locationMasterBeans = SharedStructureData.locationMasterService.retrieveLocationMasterBeansByLevelAndParent(2, parent);
        String[] arrayOfOptions = new String[locationMasterBeans.size() + 1];
        int defaultIndex = 0;
        if (!locationMasterBeans.isEmpty()) {
            int i = 1;
            arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
            for (LocationMasterBean locationMasterBean : locationMasterBeans) {
                arrayOfOptions[i] = locationMasterBean.getName();
                i++;
            }
        } else {
            arrayOfOptions[0] = UtilBean.getMyLabel(LabelConstants.NO_LOCATION_AVAILABLE);
        }

        districtSpinner = MyStaticComponents.getSpinner(context, arrayOfOptions, defaultIndex, 2);
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectionMap.remove("3");
                selectionMap.remove("4");
                selectionMap.remove("5");
                selectionMap.remove("6");
                hierarchyLayout.removeView(selectBlockQues);
                hierarchyLayout.removeView(blockSpinner);
                hierarchyLayout.removeView(selectPhcQues);
                hierarchyLayout.removeView(phcSpinner);
                hierarchyLayout.removeView(selectSubCenterQues);
                hierarchyLayout.removeView(subCenterSpinner);
                hierarchyLayout.removeView(selectVillageQues);
                hierarchyLayout.removeView(villageSpinner);
                if (position != 0) {
                    LocationMasterBean selectedLocation = locationMasterBeans.get(position - 1);
                    selectionMap.put("2", selectedLocation.getActualID());
                    selectedLocationId = selectedLocation.getActualID();
                    addBlockSpinner(selectedLocation.getActualID());
                } else {
                    selectedLocationId = selectionMap.get("1");
                }

                linearLayout.removeView(infraSelectionTextView);
                linearLayout.removeView(infraSelectionSpinner);
                linearLayout.removeView(noInfraTextView);
                retrieveInfrastructureByLocationFromDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Nothing
            }
        });

        selectDistrictQues = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.DISTRICT_OR_CORPORATION);
        hierarchyLayout.addView(selectDistrictQues);
        hierarchyLayout.addView(districtSpinner);
    }

    private void addBlockSpinner(Long parent) {
        final List<LocationMasterBean> locationMasterBeans =
                SharedStructureData.locationMasterService.retrieveLocationMasterBeansByLevelAndParent(3, parent);
        List<String> locationTypes = new ArrayList<>();
        String[] arrayOfOptions = new String[locationMasterBeans.size() + 1];
        int defaultIndex = 0;
        if (!locationMasterBeans.isEmpty()) {
            int i = 1;
            arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
            for (LocationMasterBean locationMasterBean : locationMasterBeans) {
                arrayOfOptions[i] = locationMasterBean.getName();
                if (!locationTypes.contains(locationMasterBean.getType())) {
                    locationTypes.add(locationMasterBean.getType());
                }
                i++;
            }
        } else {
            arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
        }

        blockSpinner = MyStaticComponents.getSpinner(context, arrayOfOptions, defaultIndex, 3);
        blockSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectionMap.remove("4");
                selectionMap.remove("5");
                selectionMap.remove("6");
                hierarchyLayout.removeView(selectPhcQues);
                hierarchyLayout.removeView(phcSpinner);
                hierarchyLayout.removeView(selectSubCenterQues);
                hierarchyLayout.removeView(subCenterSpinner);
                hierarchyLayout.removeView(selectVillageQues);
                hierarchyLayout.removeView(villageSpinner);
                if (position != 0) {
                    LocationMasterBean selectedLocation = locationMasterBeans.get(position - 1);
                    selectionMap.put("3", selectedLocation.getActualID());
                    selectedLocationId = selectedLocation.getActualID();
                    addPhcSpinner(selectedLocation.getActualID());
                } else {
                    selectedLocationId = selectionMap.get("2");
                }

                linearLayout.removeView(infraSelectionTextView);
                linearLayout.removeView(infraSelectionSpinner);
                linearLayout.removeView(noInfraTextView);
                retrieveInfrastructureByLocationFromDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Nothing
            }
        });

        if (locationTypes.size() > 1) {
            StringBuilder sb = new StringBuilder();
            int counter = 1;
            for (String locationType : locationTypes) {
                if (locationType.equals("B")) {
                    sb.append(LabelConstants.BLOCK);
                }
                if (locationType.equals("Z")) {
                    sb.append(LabelConstants.ZONE);
                }
                if (counter != locationTypes.size()) {
                    sb.append("/");
                }
                counter++;
            }
            selectBlockQues = MyStaticComponents.generateQuestionView(null, null, context, sb.toString());
        } else if (locationTypes.size() == 1) {
            if (locationTypes.get(0).equals("B")) {
                selectBlockQues = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.BLOCK);
            }
            if (locationTypes.get(0).equals("Z")) {
                selectBlockQues = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.ZONE);
            }
        } else {
            selectBlockQues = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.BLOCK);
        }
        hierarchyLayout.addView(selectBlockQues);
        hierarchyLayout.addView(blockSpinner);
    }

    private void addPhcSpinner(Long parent) {
        final List<LocationMasterBean> locationMasterBeans =
                SharedStructureData.locationMasterService.retrieveLocationMasterBeansByLevelAndParent(4, parent);
        List<String> locationTypes = new ArrayList<>();
        String[] arrayOfOptions = new String[locationMasterBeans.size() + 1];
        int defaultIndex = 0;
        if (!locationMasterBeans.isEmpty()) {
            int i = 1;
            arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
            for (LocationMasterBean locationMasterBean : locationMasterBeans) {
                arrayOfOptions[i] = locationMasterBean.getName();
                if (!locationTypes.contains(locationMasterBean.getType())) {
                    locationTypes.add(locationMasterBean.getType());
                }
                i++;
            }
        } else {
            arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
        }

        phcSpinner = MyStaticComponents.getSpinner(context, arrayOfOptions, defaultIndex, 3);
        phcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectionMap.remove("5");
                selectionMap.remove("6");
                hierarchyLayout.removeView(selectSubCenterQues);
                hierarchyLayout.removeView(subCenterSpinner);
                hierarchyLayout.removeView(selectVillageQues);
                hierarchyLayout.removeView(villageSpinner);
                if (position != 0) {
                    LocationMasterBean selectedLocation = locationMasterBeans.get(position - 1);
                    selectionMap.put("4", selectedLocation.getActualID());
                    selectedLocationId = selectedLocation.getActualID();
                    addSubCenterSpinner(selectedLocation.getActualID());
                } else {
                    selectedLocationId = selectionMap.get("3");
                }

                linearLayout.removeView(infraSelectionTextView);
                linearLayout.removeView(infraSelectionSpinner);
                linearLayout.removeView(noInfraTextView);
                retrieveInfrastructureByLocationFromDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Nothing
            }
        });

        if (locationTypes.size() > 1) {
            StringBuilder sb = new StringBuilder();
            int counter = 1;
            for (String locationType : locationTypes) {
                if (locationType.equals("U")) {
                    sb.append(LabelConstants.UHC);
                }
                if (locationType.equals("P")) {
                    sb.append(LabelConstants.PHC);
                }
                if (counter != locationTypes.size()) {
                    sb.append("/");
                }
                counter++;
            }
            selectPhcQues = MyStaticComponents.generateQuestionView(null, null, context, sb.toString());
        } else if (locationTypes.size() == 1) {
            if (locationTypes.get(0).equals("U")) {
                selectPhcQues = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.UHC);
            }
            if (locationTypes.get(0).equals("P")) {
                selectPhcQues = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.PHC);
            }
        } else {
            selectPhcQues = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.PHC);
        }
        hierarchyLayout.addView(selectPhcQues);
        hierarchyLayout.addView(phcSpinner);
    }

    private void addSubCenterSpinner(Long parent) {
        final List<LocationMasterBean> locationMasterBeans =
                SharedStructureData.locationMasterService.retrieveLocationMasterBeansByLevelAndParent(5, parent);
        List<String> locationTypes = new ArrayList<>();
        String[] arrayOfOptions = new String[locationMasterBeans.size() + 1];
        int defaultIndex = 0;
        if (!locationMasterBeans.isEmpty()) {
            int i = 1;
            arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
            for (LocationMasterBean locationMasterBean : locationMasterBeans) {
                arrayOfOptions[i] = locationMasterBean.getName();
                if (!locationTypes.contains(locationMasterBean.getType())) {
                    locationTypes.add(locationMasterBean.getType());
                }
                i++;
            }
        } else {
            arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
        }

        subCenterSpinner = MyStaticComponents.getSpinner(context, arrayOfOptions, defaultIndex, 5);
        subCenterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectionMap.remove("5");
                hierarchyLayout.removeView(selectVillageQues);
                hierarchyLayout.removeView(villageSpinner);
                if (position != 0) {
                    LocationMasterBean selectedLocation = locationMasterBeans.get(position - 1);
                    selectionMap.put("5", selectedLocation.getActualID());
                    selectedLocationId = selectedLocation.getActualID();
                    addVillageSpinner(selectedLocation.getActualID());
                } else {
                    selectedLocationId = selectionMap.get("4");
                }

                linearLayout.removeView(infraSelectionTextView);
                linearLayout.removeView(infraSelectionSpinner);
                linearLayout.removeView(noInfraTextView);
                retrieveInfrastructureByLocationFromDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Nothing
            }
        });

        if (locationTypes.size() > 1) {
            StringBuilder sb = new StringBuilder();
            int counter = 1;
            for (String locationType : locationTypes) {
                if (locationType.equals(LabelConstants.ANM)) {
                    sb.append(LabelConstants.ANM_AREA);
                }
                if (locationType.equals(LabelConstants.SC)) {
                    sb.append(LabelConstants.SUB_CENTER);
                }
                if (counter != locationTypes.size()) {
                    sb.append("/");
                }
                counter++;
            }
            selectSubCenterQues = MyStaticComponents.generateQuestionView(null, null, context, sb.toString());
        } else if (locationTypes.size() == 1) {
            if (locationTypes.get(0).equals(LabelConstants.ANM)) {
                selectSubCenterQues = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.ANM_AREA);
            }
            if (locationTypes.get(0).equals(LabelConstants.SC)) {
                selectSubCenterQues = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.SUB_CENTER);
            }
        } else {
            selectSubCenterQues = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.SUB_CENTER);
        }
        hierarchyLayout.addView(selectSubCenterQues);
        hierarchyLayout.addView(subCenterSpinner);
    }

    private void addVillageSpinner(Long parent) {
        final List<LocationMasterBean> locationMasterBeans =
                SharedStructureData.locationMasterService.retrieveLocationMasterBeansByLevelAndParent(6, parent);
        List<String> locationTypes = new ArrayList<>();
        String[] arrayOfOptions = new String[locationMasterBeans.size() + 1];
        int defaultIndex = 0;
        if (!locationMasterBeans.isEmpty()) {
            int i = 1;
            arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
            for (LocationMasterBean locationMasterBean : locationMasterBeans) {
                arrayOfOptions[i] = locationMasterBean.getName();
                if (!locationTypes.contains(locationMasterBean.getType())) {
                    locationTypes.add(locationMasterBean.getType());
                }
                i++;
            }
        } else {
            arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
        }

        villageSpinner = MyStaticComponents.getSpinner(context, arrayOfOptions, defaultIndex, 6);
        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    LocationMasterBean selectedLocation = locationMasterBeans.get(position - 1);
                    selectionMap.put("6", selectedLocation.getActualID());
                    selectedLocationId = selectedLocation.getActualID();
                } else {
                    selectionMap.remove("6");
                    selectedLocationId = selectionMap.get("5");
                }
                linearLayout.removeView(infraSelectionTextView);
                linearLayout.removeView(infraSelectionSpinner);
                linearLayout.removeView(noInfraTextView);
                retrieveInfrastructureByLocationFromDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Nothing
            }
        });

        if (locationTypes.size() > 1) {
            StringBuilder sb = new StringBuilder();
            int counter = 1;
            for (String locationType : locationTypes) {
                if (locationType.equals(LabelConstants.UA)) {
                    sb.append(LabelConstants.URBAN_AREA);
                }
                if (locationType.equals("V")) {
                    sb.append(LabelConstants.VILLAGE);
                }
                if (locationType.equals(LabelConstants.ANG)) {
                    sb.append(LabelConstants.ANGANWADI_AREA);
                }
                if (counter != locationTypes.size()) {
                    sb.append("/");
                }
                counter++;
            }
            selectVillageQues = MyStaticComponents.generateQuestionView(null, null, context, sb.toString());
        } else if (locationTypes.size() == 1) {
            if (locationTypes.get(0).equals(LabelConstants.UA)) {
                selectVillageQues = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.URBAN_AREA);
            }
            if (locationTypes.get(0).equals("V")) {
                selectVillageQues = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.VILLAGE);
            }
            if (locationTypes.get(0).equals(LabelConstants.ANG)) {
                selectVillageQues = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.ANGANWADI_AREA);
            }
        } else {
            selectVillageQues = MyStaticComponents.generateQuestionView(null, null, context, LabelConstants.VILLAGE);
        }
        hierarchyLayout.addView(selectVillageQues);
        hierarchyLayout.addView(villageSpinner);
    }

}
