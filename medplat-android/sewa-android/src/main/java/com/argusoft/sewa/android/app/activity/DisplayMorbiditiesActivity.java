package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.FormConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.morbidities.beans.BeneficiaryMorbidityDetails;
import com.argusoft.sewa.android.app.morbidities.beans.IdentifiedMorbidityDetails;
import com.argusoft.sewa.android.app.morbidities.constants.MorbiditiesConstant;
import com.argusoft.sewa.android.app.staticform.DisplayMorbiditiesFormUtil;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author alpeshkyada
 */
public class DisplayMorbiditiesActivity extends MenuActivity implements View.OnClickListener {

    private String morbidityType = MorbiditiesConstant.nextEntity;
    private MyAlertDialog myDialog;

    private Context activity = this;
    private LinearLayout globalPanel;
    private LinearLayout bodyLayoutContainer;
    private MaterialButton nextButton;

    private int noOfChild = 0;
    private String overAllStatus;
    private String overAllStatusForPNC;
    private String beneficiaryWithRed;
    private boolean displayNextScreen;
    private String familyUnderstandMorbidity;
    private boolean isMorbidityFound;
    private List<BeneficiaryMorbidityDetails> beneficiaryMorbidityDetailsList = MorbiditiesConstant.morbidities;

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        if (!SharedStructureData.isLogin) {
            Intent myIntent = new Intent(this, LoginActivity_.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
            finish();
        }
    }

    private void initView() {
        globalPanel = DynamicUtils.generateDynamicScreenTemplate(this, this);
        setContentView(globalPanel);
        Toolbar toolbar = globalPanel.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bodyLayoutContainer = globalPanel.findViewById(DynamicUtils.ID_BODY_LAYOUT);
        nextButton = globalPanel.findViewById(DynamicUtils.ID_NEXT_BUTTON);
        setViewAsMorbidityScreen();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            setViewAsMorbidityScreen();
        }
        return true;
    }

    private void setTitle(boolean showBackButton) {
        MaterialTextView textView = globalPanel.findViewById(R.id.toolbar_title);
        if (textView != null) {
            textView.setText(UtilBean.getMyLabel(LabelConstants.DIAGNOSIS));
            textView.setTextAppearance(activity, R.style.ToolbarTitle);
        }
        setSubTitle(null);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            if (showBackButton) {
                supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
                supportActionBar.setDisplayHomeAsUpEnabled(true);
            } else {
                supportActionBar.setDisplayHomeAsUpEnabled(false);
            }
            supportActionBar.setDisplayShowCustomEnabled(false); //show custom title
            supportActionBar.setDisplayShowTitleEnabled(false); //hide the default title
        }
    }

    @Override
    public void onBackPressed() {
        myDialog = new MyAlertDialog(this, GlobalTypes.MSG_CANCEL_FORM, this, DynamicUtils.BUTTON_YES_NO);
        myDialog.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == BUTTON_POSITIVE) {
            myDialog.dismiss();
            finish();
        } else if (id == BUTTON_NEGATIVE) {
            myDialog.dismiss();
        }
    }

    private void loadDynamicForm() {
        Intent myIntent = new Intent(this, DynamicFormActivity_.class);
        myIntent.putExtra(SewaConstants.ALLOW_SECOND_FORM_SAME_MEMBER_OFFLINE, true);

        if (morbidityType.equalsIgnoreCase(FormConstants.ANC_MORBIDITY)) {
            prepareHashTableForANCMorbidity();
            myIntent.putExtra(SewaConstants.ENTITY, FormConstants.ANC_MORBIDITY);
            myIntent.putExtra(SewaConstants.ALLOW_SECOND_FORM_SAME_MEMBER_OFFLINE, true);
            startActivity(myIntent);
            finish();
        } else if (morbidityType.equalsIgnoreCase(FormConstants.CHILD_CARE_MORBIDITY)) {
            prepareHashTableForCCMorbidity();
            myIntent.putExtra(SewaConstants.ENTITY, FormConstants.CHILD_CARE_MORBIDITY);
            myIntent.putExtra(SewaConstants.ALLOW_SECOND_FORM_SAME_MEMBER_OFFLINE, true);
            startActivity(myIntent);
            finish();
        } else if (morbidityType.equalsIgnoreCase(FormConstants.PNC_MORBIDITY)) {
            prepareHashTableForPNCHVMorbidity();
            myIntent.putExtra(SewaConstants.ENTITY, FormConstants.PNC_MORBIDITY);
            myIntent.putExtra(SewaConstants.ALLOW_SECOND_FORM_SAME_MEMBER_OFFLINE, true);
            startActivity(myIntent);
            finish();
        }
    }

    private void setViewAsMorbidityScreen() {
        showProcessDialog();
        buildMorbidityScreen();
    }

    private void setViewAsDiagnosticScreen() {
        showProcessDialog();
        buildDiseasesExplanationScreen();
    }

    private void buildMorbidityScreen() {
        bodyLayoutContainer.removeAllViews();
        setTitle(false);
        isMorbidityFound = false;
        if (beneficiaryMorbidityDetailsList != null && !beneficiaryMorbidityDetailsList.isEmpty()) {
            Log.i(getClass().getSimpleName(), "We got beneficiary morbidity detail");
            Collections.sort(beneficiaryMorbidityDetailsList, DisplayMorbiditiesFormUtil.sortBeneficiaryMorbidityDetailsByBeneficiaryType);
            for (BeneficiaryMorbidityDetails beneficiaryMorbidityDetails : beneficiaryMorbidityDetailsList) {
                bodyLayoutContainer.addView(MyStaticComponents.generateLabelView(activity, LabelConstants.BENEFICIARY_NAME + ":"));
                bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(activity, UtilBean.getMyLabel(beneficiaryMorbidityDetails.getBeneficiaryName())));
                if (this.beneficiaryMorbidityDetailsList.size() > 1) {
                    if (beneficiaryMorbidityDetails.getBeneficiaryType().equalsIgnoreCase(GlobalTypes.CLIENT_IS_MOTHER)) {
                        bodyLayoutContainer.addView(MyStaticComponents.generateLabelView(activity, LabelConstants.MOTHER + ":"));
                    } else if (beneficiaryMorbidityDetails.getBeneficiaryType().equalsIgnoreCase(GlobalTypes.CLIENT_IS_CHILD)) {
                        noOfChild++;
                        bodyLayoutContainer.addView(MyStaticComponents.generateLabelView(activity, LabelConstants.CHILD + ":"));
                    }
                }
                bodyLayoutContainer.addView(MyStaticComponents.generateLabelView(activity, LabelConstants.OVERALL_STATUS + ":"));

                List<IdentifiedMorbidityDetails> identifiedMorbidity = beneficiaryMorbidityDetails.getIdentifiedMorbidities();

                if (identifiedMorbidity != null) {
                    Collections.sort(identifiedMorbidity, DisplayMorbiditiesFormUtil.sortIdentifiedMorbiditiesByRiskFactor);

                    String status = UtilBean.getMyLabel(LabelConstants.NO_MORBIDITY_FOUND);
                    overAllStatus = "NMF";
                    if (!identifiedMorbidity.isEmpty()) {
                        if (identifiedMorbidity.get(0).getRiskFactorOfIdentifiedMorbidities().equalsIgnoreCase(MorbiditiesConstant.RED_COLOR)) {
                            status = LabelConstants.RED;
                            overAllStatus = MorbiditiesConstant.RED_COLOR;
                            displayNextScreen = true;
                        } else if (identifiedMorbidity.get(0).getRiskFactorOfIdentifiedMorbidities().equalsIgnoreCase(MorbiditiesConstant.YELLOW_COLOR)) {
                            status = LabelConstants.YELLOW;
                            overAllStatus = MorbiditiesConstant.YELLOW_COLOR;
                        } else if (identifiedMorbidity.get(0).getRiskFactorOfIdentifiedMorbidities().equalsIgnoreCase(MorbiditiesConstant.GREEN_COLOR)) {
                            status = LabelConstants.GREEN;
                            overAllStatus = MorbiditiesConstant.GREEN_COLOR;
                        }
                    }
                    TextView textview = MyStaticComponents.generateAnswerView(activity, status);
                    SewaUtil.setColor(textview, status);
                    bodyLayoutContainer.addView(textview);

                    if (overAllStatusForPNC == null) {
                        overAllStatusForPNC = overAllStatus;
                    } else {
                        if (DisplayMorbiditiesFormUtil.riskValueOf(overAllStatus) < DisplayMorbiditiesFormUtil.riskValueOf(overAllStatusForPNC)) {
                            overAllStatusForPNC = overAllStatus;
                        }
                    }

                    if (!identifiedMorbidity.isEmpty()) {
                        isMorbidityFound = true;
                        bodyLayoutContainer.addView(MyStaticComponents.generateLabelView(activity, LabelConstants.EXHIBITS_SIGN_OF));
                        int i = 1;
                        for (IdentifiedMorbidityDetails identifiedMorbidityDetails : identifiedMorbidity) {
                            StringBuilder morbidityName = new StringBuilder(String.valueOf(i));
                            morbidityName.append(". ");
                            String[] split = UtilBean.split(identifiedMorbidityDetails.getMorbidityCode(), GlobalTypes.KEY_VALUE_SEPARATOR);
                            if (split.length > 0 && split[0] != null) {
                                identifiedMorbidityDetails.setMorbidityCode(split[0]);
                            }
                            String name = MorbiditiesConstant.getMorbidityCodeAsKEYandMorbidityNameAsVALUE(identifiedMorbidityDetails.getMorbidityCode());
                            morbidityName.append(UtilBean.getMyLabel(name));

                            TextView morbidityTextView = MyStaticComponents.generateAnswerView(activity, morbidityName.toString());
                            SewaUtil.setColor(morbidityTextView, identifiedMorbidityDetails.getRiskFactorOfIdentifiedMorbidities());
                            bodyLayoutContainer.addView(morbidityTextView);
                            i++;
                        } // end of for loop identify morbidity

                        bodyLayoutContainer.addView(MyStaticComponents.generateLabelView(activity, LabelConstants.MAIN_SYMPTOMS));

                        StringBuilder mainSym = new StringBuilder();
                        ArrayList<String> list = new ArrayList<>();
                        i = 1;
                        for (IdentifiedMorbidityDetails identifiedMorbidityDetails : identifiedMorbidity) {
                            List<String> identifiedSymptoms = identifiedMorbidityDetails.getIdentifiedSymptoms();
                            for (String symptom : identifiedSymptoms) {
                                if (!list.contains(symptom)) {
                                    list.add(symptom);
                                    mainSym.append(i);
                                    mainSym.append(". ");
                                    mainSym.append(UtilBean.getMyLabel(symptom)).append(LabelConstants.NEW_LINE);
                                    i++;
                                }
                            }
                        }
                        bodyLayoutContainer.addView(MyStaticComponents.generateAnswerView(activity, mainSym.toString()));
                    } // if of identify morbidity
                }// if of identify morbidity
            }// end for loop

            nextButton.setOnClickListener(v -> {
                if (!isMorbidityFound) {
                    finish();
                } else if (displayNextScreen) {
                    setViewAsDiagnosticScreen();
                } else {
                    SewaUtil.familyUnderstandInMorbidity = familyUnderstandMorbidity;
                    showProcessDialog();
                    new Thread() {
                        @Override
                        public void run() {
                            loadDynamicForm();
                            hideProcessDialog();
                        }
                    }.start();
                }
            });
        }// if no morbidity
        else {
            bodyLayoutContainer.addView(MyStaticComponents.generateLabelView(activity, LabelConstants.NO_MORBIDITY_FOUND));
            nextButton.setText(GlobalTypes.EVENT_OKAY);
            nextButton.setOnClickListener(v -> finish());
        }
        hideProcessDialog();
    }

    private void buildDiseasesExplanationScreen() {
        bodyLayoutContainer.removeAllViews();
        setTitle(true);
        for (BeneficiaryMorbidityDetails beneficiaryMorbidityDetails : beneficiaryMorbidityDetailsList) {

            List<IdentifiedMorbidityDetails> identifiedMorbidity = beneficiaryMorbidityDetails.getIdentifiedMorbidities();

            if (identifiedMorbidity != null) {
                Collections.sort(identifiedMorbidity, DisplayMorbiditiesFormUtil.sortIdentifiedMorbiditiesByRiskFactor);

                if (!identifiedMorbidity.isEmpty()) {
                    if (identifiedMorbidity.get(0).getRiskFactorOfIdentifiedMorbidities().equalsIgnoreCase(MorbiditiesConstant.RED_COLOR)) {
                        if (beneficiaryMorbidityDetails.getBeneficiaryType().equalsIgnoreCase(GlobalTypes.CLIENT_IS_MOTHER)) {
                            String label = UtilBean.getMyLabel(LabelConstants.EXPLAIN_THE_FAMILY_THAT_MOTHER_IS_AT_HIGH_RISK_DUE_TO);
                            bodyLayoutContainer.addView(MyStaticComponents.generateLabelView(activity, label));
                        } else if (beneficiaryMorbidityDetails.getBeneficiaryType().equalsIgnoreCase(GlobalTypes.CLIENT_IS_CHILD)) {
                            String label = UtilBean.getMyLabel(LabelConstants.EXPLAIN_THE_FAMILY_THAT_CHILD_IS_AT_HIGH_RISK_DUE_TO);
                            bodyLayoutContainer.addView(MyStaticComponents.generateLabelView(activity, label));
                        }
                    }
                    for (IdentifiedMorbidityDetails identifiedMorbidityDetails : identifiedMorbidity) {
                        if (identifiedMorbidityDetails.getRiskFactorOfIdentifiedMorbidities().equalsIgnoreCase(MorbiditiesConstant.RED_COLOR)) {
                            if (beneficiaryWithRed == null) {
                                if (beneficiaryMorbidityDetails.getBeneficiaryType().equalsIgnoreCase(GlobalTypes.CLIENT_IS_MOTHER)) {
                                    beneficiaryWithRed = MorbiditiesConstant.ONLY_MOTHER_RED;
                                } else {
                                    beneficiaryWithRed = MorbiditiesConstant.ONLY_CHILD_RED;
                                }
                            } else {
                                if (beneficiaryWithRed.equalsIgnoreCase(MorbiditiesConstant.ONLY_MOTHER_RED) && beneficiaryMorbidityDetails.getBeneficiaryType().equalsIgnoreCase(GlobalTypes.CLIENT_IS_CHILD)) {
                                    beneficiaryWithRed = MorbiditiesConstant.MOTHER_AND_CHILD_RED;
                                }
                            }

                            String diseaseName = MorbiditiesConstant.getMorbidityCodeAsKEYandMorbidityNameAsVALUE(identifiedMorbidityDetails.getMorbidityCode());
                            TextView diseasesTextView = MyStaticComponents.generateAnswerView(activity, diseaseName);
                            SewaUtil.setColor(diseasesTextView, MorbiditiesConstant.RED_COLOR);
                            bodyLayoutContainer.addView(diseasesTextView);
                        }
                    }
                }
            }
        }
        bodyLayoutContainer.addView(MyStaticComponents.generateQuestionView(null, null, activity, LabelConstants.DOES_MOTHER_OR_FAMILY_UNDERSTAND));
        RadioGroup myRadioGroup = new RadioGroup(activity);
        myRadioGroup.setOrientation(VERTICAL);

        RadioGroup.LayoutParams myLayoutParam = new RadioGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1);

        RadioButton radioButton = MyStaticComponents.getRadioButton(activity, UtilBean.getMyLabel(LabelConstants.YES), 1);
        myRadioGroup.addView(radioButton, myLayoutParam);

        radioButton = MyStaticComponents.getRadioButton(activity, UtilBean.getMyLabel(LabelConstants.NO), 2);
        myRadioGroup.addView(radioButton, myLayoutParam);
        myRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == 1) {   //yes
                familyUnderstandMorbidity = GlobalTypes.TRUE;
            } else {   // no
                familyUnderstandMorbidity = GlobalTypes.FALSE;
            }
        });
        bodyLayoutContainer.addView(myRadioGroup);

        View.OnClickListener myListener = v -> {
            int id = v.getId();
            if (id == DynamicUtils.ID_NEXT_BUTTON) { // Next
                if (familyUnderstandMorbidity != null) {
                    SewaUtil.familyUnderstandInMorbidity = familyUnderstandMorbidity;
                    showProcessDialog();
                    new Thread() {
                        @Override
                        public void run() {
                            loadDynamicForm();
                            hideProcessDialog();
                        }
                    }.start();
                } else {
                    SewaUtil.generateToast(activity, LabelConstants.DOES_MOTHER_OR_FAMILY_UNDERSTAND);
                }
            }
        };
        nextButton.setOnClickListener(myListener);
        hideProcessDialog();
    }

    private void prepareHashTableForANCMorbidity() {
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.OVERALL_STATUS, overAllStatus);
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CALL_HOSPITAL, "108");
        for (BeneficiaryMorbidityDetails beneficiaryMorbidityDetails : beneficiaryMorbidityDetailsList) {
            List<IdentifiedMorbidityDetails> identifiedMorbidity = beneficiaryMorbidityDetails.getIdentifiedMorbidities();

            if (identifiedMorbidity != null) {
                Collections.sort(identifiedMorbidity, DisplayMorbiditiesFormUtil.sortIdentifiedMorbiditiesByRiskFactor);

                int redCount = 0;
                int j = 0;
                for (IdentifiedMorbidityDetails identifiedMorbidityDetails : identifiedMorbidity) {
                    String morbidityName = identifiedMorbidityDetails.getMorbidityCode();
                    if (j == 0) {
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DISEASE_NAME, morbidityName);
                    } else {
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DISEASE_NAME + j, morbidityName);
                    }
                    if (identifiedMorbidityDetails.getRiskFactorOfIdentifiedMorbidities().equalsIgnoreCase(MorbiditiesConstant.RED_COLOR)) {
                        redCount++;
                    }
                    j++;
                }
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.REMAINING_DISEASE, String.valueOf(identifiedMorbidity.size()));
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.REMAINING_RED_DISEASE, String.valueOf(redCount));
            }
        }
    }

    private void prepareHashTableForCCMorbidity() {

        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.OVERALL_STATUS, overAllStatus);
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CALL_HOSPITAL, "108");
        for (BeneficiaryMorbidityDetails beneficiaryMorbidityDetails : beneficiaryMorbidityDetailsList) {
            List<IdentifiedMorbidityDetails> identifiedMorbidity = beneficiaryMorbidityDetails.getIdentifiedMorbidities();

            if (identifiedMorbidity != null) {
                int redCount = 0;
                Collections.sort(identifiedMorbidity, DisplayMorbiditiesFormUtil.sortIdentifiedMorbiditiesByRiskFactor);

                int j = 0;
                for (IdentifiedMorbidityDetails identifiedMorbidityDetails : identifiedMorbidity) {
                    String morbidityName = identifiedMorbidityDetails.getMorbidityCode();
                    if (j == 0) {
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DISEASE_NAME, morbidityName);
                    } else {
                        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DISEASE_NAME + j, morbidityName);
                    }
                    if (identifiedMorbidityDetails.getRiskFactorOfIdentifiedMorbidities().equalsIgnoreCase(MorbiditiesConstant.RED_COLOR)) {
                        redCount++;
                    }
                    j++;
                }
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.REMAINING_DISEASE, String.valueOf(identifiedMorbidity.size()));
                SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.REMAINING_RED_DISEASE, String.valueOf(redCount));

                Integer age = null;
                String tmpDataObj = SharedStructureData.relatedPropertyHashTable.get("age");
                if (tmpDataObj != null) {
                    age = Integer.parseInt(tmpDataObj);
                }
                String ageFromECR = SharedStructureData.relatedPropertyHashTable.get("ageFromECR");
                tmpDataObj = SharedStructureData.relatedPropertyHashTable.get("dob");
                if (ageFromECR != null) {
                    SharedStructureData.relatedPropertyHashTable.put("childCurrentAge", ageFromECR);
                } else if (tmpDataObj != null) {
                    long dob = Long.parseLong(tmpDataObj);
                    int[] ageArray = UtilBean.calculateAgeYearMonthDay(dob);
                    ageFromECR = ageArray[0] + GlobalTypes.KEY_VALUE_SEPARATOR + ageArray[1] + GlobalTypes.KEY_VALUE_SEPARATOR + ageArray[2];
                    SharedStructureData.relatedPropertyHashTable.put("childCurrentAge", ageFromECR);
                }
                int ageInMonths = 0;
                if (ageFromECR != null) {
                    String[] split = UtilBean.split(ageFromECR, GlobalTypes.KEY_VALUE_SEPARATOR);
                    if (split.length > 1) {
                        ageInMonths = Integer.parseInt(split[0]) * 12 + Integer.parseInt(split[1]);
                    }
                }
                if (age != null) {
                    ageInMonths = age;
                }
                String ironFolicAcidTablet = UtilBean.getIronFolicAcidTablet(ageInMonths);
                if (ironFolicAcidTablet != null) {
                    SharedStructureData.relatedPropertyHashTable.put("ironFolicAcidTabletDosage", ironFolicAcidTablet);
                }
                String chloroquineTabletDosage = UtilBean.getChloroquineTabletDosage(ageInMonths);
                SharedStructureData.relatedPropertyHashTable.put("chloroquineDosage", chloroquineTabletDosage);

                String pcmTabletDosage = UtilBean.getPCMTabletDosage(ageInMonths);
                if (pcmTabletDosage != null) {
                    SharedStructureData.relatedPropertyHashTable.put("pcmDosage", pcmTabletDosage);
                }

                String vitaminADosage = UtilBean.getVitaminADosage(ageInMonths);
                if (vitaminADosage != null) {
                    SharedStructureData.relatedPropertyHashTable.put("vitaminADosage", vitaminADosage);
                }

                String dosageForDiarrhoeaWithoutDehydration = UtilBean.getDosageForDiarrhoeaWithoutDehydration(ageInMonths);
                if (dosageForDiarrhoeaWithoutDehydration != null) {
                    SharedStructureData.relatedPropertyHashTable.put("orsnoDosage", dosageForDiarrhoeaWithoutDehydration);
                }

                String dosageForDiarrhoeaWithDehydration = UtilBean.getDosageForDiarrhoeaWithDehydration(ageInMonths);
                if (dosageForDiarrhoeaWithDehydration != null) {
                    SharedStructureData.relatedPropertyHashTable.put("orssomeDosage", dosageForDiarrhoeaWithDehydration);
                }
            }
        }
    }

    private void prepareHashTableForPNCHVMorbidity() {
        if (overAllStatusForPNC != null) {
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.OVERALL_STATUS, overAllStatusForPNC);
        }
        SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.CALL_HOSPITAL, "108");
        List<IdentifiedMorbidityDetails> allMorbidity = new ArrayList<>();
        int childCounter = 0;
        for (BeneficiaryMorbidityDetails beneficiaryMorbidityDetails : beneficiaryMorbidityDetailsList) {
            if (beneficiaryMorbidityDetails != null && beneficiaryMorbidityDetails.getIdentifiedMorbidities() != null
                    && !beneficiaryMorbidityDetails.getIdentifiedMorbidities().isEmpty()) {

                List<IdentifiedMorbidityDetails> identifiedMorbidity = beneficiaryMorbidityDetails.getIdentifiedMorbidities();
                if (beneficiaryMorbidityDetails.getBeneficiaryType().equalsIgnoreCase(GlobalTypes.CLIENT_IS_CHILD) && noOfChild > 1) {
                    childCounter++;
                    for (IdentifiedMorbidityDetails identifiedMorbidityDetails : identifiedMorbidity) {
                        identifiedMorbidityDetails.setMorbidityCode(identifiedMorbidityDetails.getMorbidityCode() + GlobalTypes.KEY_VALUE_SEPARATOR + childCounter);
                        allMorbidity.add(identifiedMorbidityDetails);
                    }
                } else {
                    allMorbidity.addAll(identifiedMorbidity);
                }
            }
        }

        if (!allMorbidity.isEmpty()) {
            Collections.sort(allMorbidity, (idm1, idm2) -> (idm1.getMorbidityCode().compareToIgnoreCase(idm2.getMorbidityCode()) * -1));
            Collections.sort(allMorbidity, DisplayMorbiditiesFormUtil.sortIdentifiedMorbiditiesByRiskFactor);
            int j = 0;
            int redCount = 0;
            for (IdentifiedMorbidityDetails identifiedMorbidityDetails : allMorbidity) {
                if (identifiedMorbidityDetails.getRiskFactorOfIdentifiedMorbidities().equalsIgnoreCase(MorbiditiesConstant.RED_COLOR)) {
                    redCount++;
                }
                String morbidityName = identifiedMorbidityDetails.getMorbidityCode();
                if (j == 0) {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DISEASE_NAME, morbidityName);
                } else {
                    SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.DISEASE_NAME + j, morbidityName);
                }
                j++;
            }
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.REMAINING_DISEASE, String.valueOf(allMorbidity.size()));
            SharedStructureData.relatedPropertyHashTable.put(RelatedPropertyNameConstants.REMAINING_RED_DISEASE, String.valueOf(redCount));
            if (beneficiaryWithRed != null) {
                SharedStructureData.relatedPropertyHashTable.put("beneficiaryWithRed", beneficiaryWithRed);
            }
        }
    }
}
