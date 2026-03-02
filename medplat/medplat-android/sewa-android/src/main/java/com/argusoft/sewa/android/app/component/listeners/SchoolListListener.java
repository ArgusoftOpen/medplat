package com.argusoft.sewa.android.app.component.listeners;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import com.argusoft.sewa.android.app.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.argusoft.sewa.android.app.component.MyProcessDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.FormulaConstants;
import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.databean.FormulaTagBean;
import com.argusoft.sewa.android.app.databean.ValidationTagBean;
import com.argusoft.sewa.android.app.datastructure.QueFormBean;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.LocationMasterBean;
import com.argusoft.sewa.android.app.model.SchoolBean;
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

public class SchoolListListener implements RadioGroup.OnCheckedChangeListener {
    private Context context;
    private QueFormBean queFormBean;
    private LinearLayout linearLayout;
    private RadioGroup searchTypeRadioGroup;
    private MyProcessDialog processDialog;
    private Integer standard;

    private List<SchoolBean> schoolBeans = new ArrayList<>();
    private MaterialTextView schoolSelectionTextView;
    private Spinner schoolSelectionSpinner;
    private MaterialTextView noSchoolTextView;

    private MaterialTextView searchSchoolTextView;
    private TextInputLayout searchSchoolEditText;
    private Timer timer = new Timer();
    private static final long DELAY = 1000;

    private MaterialTextView hierarchyTextView;
    private LinearLayout hierarchyLayout;

    private MaterialTextView selectDistrictQues;
    private Spinner districtSpinner;
    private MaterialTextView selectBlockQues;
    private Spinner blockSpinner;
    private MaterialTextView selectPhcQues;
    private Spinner phcSpinner;
    private MaterialTextView selectSubcenterQues;
    private Spinner subcenterSpinner;
    private MaterialTextView selectVillageQues;
    private Spinner villageSpinner;

    private Map<String, Long> selectionMap = new HashMap<>();
    private Long selectedLocationId = null;

    public SchoolListListener(Context context, QueFormBean queFormBean, LinearLayout linearLayout, RadioGroup searchTypeRadioGroup) {
        this.context = context;
        this.queFormBean = queFormBean;
        this.linearLayout = linearLayout;
        this.searchTypeRadioGroup = searchTypeRadioGroup;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (searchTypeRadioGroup.getCheckedRadioButtonId() == -1) {
            return;
        }

        queFormBean.setAnswer(null);
        if (queFormBean.getValidations() != null) {
            String validationMessage = null;
            for (ValidationTagBean validation : queFormBean.getValidations()) {
                if (validation.getMethod().contains("-")) {
                    String[] split = validation.getMethod().split("-");
                    if (split[0].equalsIgnoreCase(FormulaConstants.VALIDATION_CHECK_SERVICE_DATE_FOR_SCHOOL)) {
                        ArrayList<ValidationTagBean> validationTagBeans = new ArrayList<>();
                        validationTagBeans.add(validation);
                        validationMessage = DynamicUtils.checkValidation(GlobalTypes.SCHOOL_TYPE_PRIMARY, 0, validationTagBeans);
                    }
                } else if (validation.getMethod().equalsIgnoreCase(FormulaConstants.VALIDATION_CHECK_SERVICE_DATE_FOR_SCHOOL)) {
                    ArrayList<ValidationTagBean> validationTagBeans = new ArrayList<>();
                    validationTagBeans.add(validation);
                    validationMessage = DynamicUtils.checkValidation(GlobalTypes.SCHOOL_TYPE_HIGHER_SECONDARY, 0, validationTagBeans);
                }
            }
            if (validationMessage != null) {
                SewaUtil.generateToast(SharedStructureData.context, validationMessage);
                return;
            }
        }

        if (searchTypeRadioGroup.getCheckedRadioButtonId() == IdConstants.SCHOOL_COMPONENT_SEARCH_BY_NAME_RB_ID) {
            addSearchLayoutForSchool();
            if (searchSchoolEditText.getEditText() != null) {
                retrieveSchoolsBySearchFromDB(searchSchoolEditText.getEditText().getText().toString());
            }
        } else if (searchTypeRadioGroup.getCheckedRadioButtonId() == IdConstants.SCHOOL_COMPONENT_SEARCH_BY_HIERARCHY_RB_ID) {
            addHierarchyLayoutForSchool();
            retrieveSchoolsByLocationFromDB();
        }
    }

    private void addSearchLayoutForSchool() {
        linearLayout.removeView(hierarchyTextView);
        linearLayout.removeView(hierarchyLayout);
        linearLayout.removeView(searchSchoolTextView);
        linearLayout.removeView(searchSchoolEditText);
        linearLayout.removeView(schoolSelectionTextView);
        linearLayout.removeView(schoolSelectionSpinner);
        linearLayout.removeView(noSchoolTextView);

        if (searchSchoolTextView == null) {
            searchSchoolTextView = MyStaticComponents.generateQuestionView(null, null, context,
                    UtilBean.getMyLabel(LabelConstants.SEARCH_SCHOOL));
        }

        if (searchSchoolEditText == null) {

            searchSchoolEditText = MyStaticComponents.getEditText(context, UtilBean.getMyLabel(LabelConstants.SEARCH_SCHOOL), 1101, 100, -1);
            if (searchSchoolEditText.getEditText() != null) {
                searchSchoolEditText.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //
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
                                                linearLayout.removeView(schoolSelectionTextView);
                                                linearLayout.removeView(schoolSelectionSpinner);
                                                linearLayout.removeView(noSchoolTextView);
                                                retrieveSchoolsBySearchFromDB(s.toString());
                                                searchSchoolEditText.setFocusable(true);
                                            });
                                        } else if (s != null && s.length() == 0) {
                                            ((Activity) context).runOnUiThread(() -> {
                                                linearLayout.removeView(schoolSelectionTextView);
                                                linearLayout.removeView(schoolSelectionSpinner);
                                                linearLayout.removeView(noSchoolTextView);
                                                schoolBeans.clear();
                                            });
                                        }
                                    }
                                },
                                DELAY
                        );
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        //
                    }
                });
            }
        }

        linearLayout.addView(searchSchoolTextView);
        linearLayout.addView(searchSchoolEditText);
    }

    private void addHierarchyLayoutForSchool() {
        linearLayout.removeView(hierarchyTextView);
        linearLayout.removeView(hierarchyLayout);
        linearLayout.removeView(searchSchoolTextView);
        linearLayout.removeView(searchSchoolEditText);
        linearLayout.removeView(schoolSelectionTextView);
        linearLayout.removeView(schoolSelectionSpinner);
        linearLayout.removeView(noSchoolTextView);

        if (hierarchyTextView == null) {
            hierarchyTextView = MyStaticComponents.generateQuestionView(null, null, context,
                    UtilBean.getMyLabel(LabelConstants.SELECT_HIERARCHY_FOR_SCHOOL));
        }

        if (hierarchyLayout == null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            hierarchyLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL, layoutParams);
            addRegionSpinner();
        }

        linearLayout.addView(hierarchyTextView);
        linearLayout.addView(hierarchyLayout);
    }

    private void retrieveSchoolsBySearchFromDB(final String search) {
        queFormBean.setAnswer(null);
        if (search != null && search.length() > 2) {
            processDialog = new MyProcessDialog(context, GlobalTypes.MSG_PROCESSING);
            processDialog.show();
            setStandard();
            schoolBeans = SharedStructureData.schoolService.retrieveSchoolsListBySearch(search, standard);
            addSchoolSelectionLayout();
        }
    }

    private void retrieveSchoolsByLocationFromDB() {
        queFormBean.setAnswer(null);
        if (selectedLocationId != null) {
            processDialog = new MyProcessDialog(context, GlobalTypes.MSG_PROCESSING);
            processDialog.show();
            setStandard();
            schoolBeans = SharedStructureData.schoolService.retrieveSchoolsListByLocationId(selectedLocationId, standard);
            addSchoolSelectionLayout();
        }
    }

    public Integer getStandard() {
        return standard;
    }

    private void setStandard() {
        if (queFormBean.getFormulas() != null && !queFormBean.getFormulas().isEmpty()) {
            for(FormulaTagBean formula : queFormBean.getFormulas()) {
                String[] split = formula.getFormulavalue().split("-");
                if(split.length > 1 && split[0].equalsIgnoreCase(FormulaConstants.FORMULA_SET_STANDARD)) {
                    String property = UtilBean.getRelatedPropertyNameWithLoopCounter(split[1], queFormBean.getLoopCounter());
                    String std = SharedStructureData.relatedPropertyHashTable.get(property);
                    if (std != null) {
                        standard = Integer.parseInt(std);
                    }
                    break;
                }
            }
        }
    }

    private void addSchoolSelectionLayout() {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                linearLayout.removeView(schoolSelectionTextView);
                linearLayout.removeView(schoolSelectionSpinner);
                linearLayout.removeView(noSchoolTextView);

                if (schoolBeans.isEmpty()) {
                    noSchoolTextView = MyStaticComponents.generateInstructionView(context,
                            UtilBean.getMyLabel(LabelConstants.NO_SCHOOL_AVAILABLE_FOR_SELECTED_LOCATION));
                    linearLayout.addView(noSchoolTextView);
                    if (processDialog != null && processDialog.isShowing()) {
                        processDialog.dismiss();
                    }
                    return;
                }

                if (schoolSelectionTextView == null) {
                    schoolSelectionTextView = MyStaticComponents.generateQuestionView(null, null, context,
                            UtilBean.getMyLabel(LabelConstants.SELECT_SCHOOL));
                }

                String[] arrayOfOptions = new String[schoolBeans.size() + 1];
                int defaultIndex = 0;
                int i = 1;
                arrayOfOptions[0] = UtilBean.getMyLabel(GlobalTypes.SELECT);
                for (SchoolBean schoolBean : schoolBeans) {
                    arrayOfOptions[i] = schoolBean.getName();
                    i++;
                }

                schoolSelectionSpinner = MyStaticComponents.getSpinner(
                        context, arrayOfOptions, defaultIndex, 1);
                schoolSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            SchoolBean selectedSchool = schoolBeans.get(position - 1);
                            Log.i(getClass().getSimpleName(), LabelConstants.SELECT_SCHOOL + " : " + selectedSchool.getActualId() + " : " + selectedSchool.getName());
                            queFormBean.setAnswer(selectedSchool.getActualId());
                            SharedStructureData.selectedSchool = selectedSchool;
                        } else {
                            queFormBean.setAnswer(null);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        //
                    }
                });

                linearLayout.addView(schoolSelectionTextView);
                linearLayout.addView(schoolSelectionSpinner);

                if (processDialog != null && processDialog.isShowing()) {
                    processDialog.dismiss();
                }
            }
        });
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

        Spinner regionSpinner = MyStaticComponents.getSpinner(
                context, arrayOfOptions, defaultIndex, 1);
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
                hierarchyLayout.removeView(selectSubcenterQues);
                hierarchyLayout.removeView(subcenterSpinner);
                hierarchyLayout.removeView(selectVillageQues);
                hierarchyLayout.removeView(villageSpinner);
                if (position != 0) {
                    LocationMasterBean selectedLocation = locationMasterBeans.get(position - 1);
                    selectionMap.put("1", selectedLocation.getActualID());
                    selectedLocationId = selectedLocation.getActualID();
                    addDistrictSpinner(selectedLocation.getActualID());
                } else {
                    selectedLocationId = null;
                    schoolBeans.clear();
                }

                linearLayout.removeView(schoolSelectionTextView);
                linearLayout.removeView(schoolSelectionSpinner);
                linearLayout.removeView(noSchoolTextView);
                retrieveSchoolsByLocationFromDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        MaterialTextView selectRegionQues = MyStaticComponents.generateQuestionView(
                null, null, context, UtilBean.getMyLabel(LabelConstants.REGION));
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

        districtSpinner = MyStaticComponents.getSpinner(
                context, arrayOfOptions, defaultIndex, 2);
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
                hierarchyLayout.removeView(selectSubcenterQues);
                hierarchyLayout.removeView(subcenterSpinner);
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

                linearLayout.removeView(schoolSelectionTextView);
                linearLayout.removeView(schoolSelectionSpinner);
                linearLayout.removeView(noSchoolTextView);
                retrieveSchoolsByLocationFromDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        selectDistrictQues = MyStaticComponents.generateQuestionView(
                null, null, context, UtilBean.getMyLabel(LabelConstants.DISTRICT_OR_CORPORATION));
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

        blockSpinner = MyStaticComponents.getSpinner(
                context, arrayOfOptions, defaultIndex, 3);
        blockSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectionMap.remove("4");
                selectionMap.remove("5");
                selectionMap.remove("6");
                hierarchyLayout.removeView(selectPhcQues);
                hierarchyLayout.removeView(phcSpinner);
                hierarchyLayout.removeView(selectSubcenterQues);
                hierarchyLayout.removeView(subcenterSpinner);
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

                linearLayout.removeView(schoolSelectionTextView);
                linearLayout.removeView(schoolSelectionSpinner);
                linearLayout.removeView(noSchoolTextView);
                retrieveSchoolsByLocationFromDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
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
            selectBlockQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(sb.toString()));
        } else if (locationTypes.size() == 1) {
            if (locationTypes.get(0).equals("B")) {
                selectBlockQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.BLOCK));
            }
            if (locationTypes.get(0).equals("Z")) {
                selectBlockQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.ZONE));
            }
        } else {
            selectBlockQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.BLOCK));
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

        phcSpinner = MyStaticComponents.getSpinner(
                context, arrayOfOptions, defaultIndex, 3);
        phcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectionMap.remove("5");
                selectionMap.remove("6");
                hierarchyLayout.removeView(selectSubcenterQues);
                hierarchyLayout.removeView(subcenterSpinner);
                hierarchyLayout.removeView(selectVillageQues);
                hierarchyLayout.removeView(villageSpinner);
                if (position != 0) {
                    LocationMasterBean selectedLocation = locationMasterBeans.get(position - 1);
                    selectionMap.put("4", selectedLocation.getActualID());
                    selectedLocationId = selectedLocation.getActualID();
                    addSubcenterSpinner(selectedLocation.getActualID());
                } else {
                    selectedLocationId = selectionMap.get("3");
                }

                linearLayout.removeView(schoolSelectionTextView);
                linearLayout.removeView(schoolSelectionSpinner);
                linearLayout.removeView(noSchoolTextView);
                retrieveSchoolsByLocationFromDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
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
            selectPhcQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(sb.toString()));
        } else if (locationTypes.size() == 1) {
            if (locationTypes.get(0).equals("U")) {
                selectPhcQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.UHC));
            }
            if (locationTypes.get(0).equals("P")) {
                selectPhcQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.PHC));
            }
        } else {
            selectPhcQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.PHC));
        }
        hierarchyLayout.addView(selectPhcQues);
        hierarchyLayout.addView(phcSpinner);
    }

    private void addSubcenterSpinner(Long parent) {
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

        subcenterSpinner = MyStaticComponents.getSpinner(
                context, arrayOfOptions, defaultIndex, 5);
        subcenterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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

                linearLayout.removeView(schoolSelectionTextView);
                linearLayout.removeView(schoolSelectionSpinner);
                linearLayout.removeView(noSchoolTextView);
                retrieveSchoolsByLocationFromDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
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
            selectSubcenterQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(sb.toString()));
        } else if (locationTypes.size() == 1) {
            if (locationTypes.get(0).equals(LabelConstants.ANM)) {
                selectSubcenterQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.ANM_AREA));
            }
            if (locationTypes.get(0).equals(LabelConstants.SC)) {
                selectSubcenterQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SUB_CENTER));
            }
        } else {
            selectSubcenterQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.SUB_CENTER));
        }
        hierarchyLayout.addView(selectSubcenterQues);
        hierarchyLayout.addView(subcenterSpinner);
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

        villageSpinner = MyStaticComponents.getSpinner(
                context, arrayOfOptions, defaultIndex, 6);
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
                linearLayout.removeView(schoolSelectionTextView);
                linearLayout.removeView(schoolSelectionSpinner);
                linearLayout.removeView(noSchoolTextView);
                retrieveSchoolsByLocationFromDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
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
            selectVillageQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(sb.toString()));
        } else if (locationTypes.size() == 1) {
            if (locationTypes.get(0).equals(LabelConstants.UA)) {
                selectVillageQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.URBAN_AREA));
            }
            if (locationTypes.get(0).equals("V")) {
                selectVillageQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.VILLAGE));
            }
            if (locationTypes.get(0).equals(LabelConstants.ANG)) {
                selectVillageQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.ANGANWADI_AREA));
            }
        } else {
            selectVillageQues = MyStaticComponents.generateQuestionView(null, null, context, UtilBean.getMyLabel(LabelConstants.VILLAGE));
        }
        hierarchyLayout.addView(selectVillageQues);
        hierarchyLayout.addView(villageSpinner);
    }
}
