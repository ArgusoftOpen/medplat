/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.morbidities.beans;

import com.argusoft.sewa.android.app.staticform.DisplayMorbiditiesFormUtil;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.UtilBean;

import java.util.Collections;
import java.util.List;

/**
 * @author jemish
 */
public class MorbidityAnswerStringBean {

    private List<BeneficiaryMorbidityDetails> beneficiaryMorbidityDetailses;
    private String familyUnderstand;
    private String morbidityAnswerString;

    public MorbidityAnswerStringBean(List<BeneficiaryMorbidityDetails> beneficiaryMorbidityDetailses, String familyUnderstand, String morbidityAnswerString) {
        this.beneficiaryMorbidityDetailses = beneficiaryMorbidityDetailses;
        this.familyUnderstand = familyUnderstand;
        this.morbidityAnswerString = morbidityAnswerString;
    }

    public MorbidityAnswerStringBean() {
    }

    public List<BeneficiaryMorbidityDetails> getBeneficiaryMorbidityDetailses() {
        return beneficiaryMorbidityDetailses;
    }

    public void setBeneficiaryMorbidityDetailses(List<BeneficiaryMorbidityDetails> beneficiaryMorbidityDetailses) {
        this.beneficiaryMorbidityDetailses = beneficiaryMorbidityDetailses;
    }

    public String getFamilyUnderstand() {
        return familyUnderstand;
    }

    public void setFamilyUnderstand(String familyUnderstand) {
        this.familyUnderstand = familyUnderstand;
    }

    public String getMorbidityAnswerString() {
        return morbidityAnswerString;
    }

    public void setMorbidityAnswerString(String morbidityAnswerString) {
        this.morbidityAnswerString = morbidityAnswerString;
    }

    public String calculateMorbidityString() {

        if (beneficiaryMorbidityDetailses != null && !beneficiaryMorbidityDetailses.isEmpty()) {
            Collections.sort(beneficiaryMorbidityDetailses, DisplayMorbiditiesFormUtil.sortBeneficiaryMorbidityDetailsByBeneficiaryType);
            StringBuilder finalMorbidity = new StringBuilder();

            for (BeneficiaryMorbidityDetails beneficiaryMorbidityDetails : beneficiaryMorbidityDetailses) {

                StringBuilder beneMorbidity = new StringBuilder();
                beneMorbidity.append("0");
                beneMorbidity.append(GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR);
                beneMorbidity.append(beneficiaryMorbidityDetails.getBeneficiaryId());
                beneMorbidity.append(GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR);
                beneMorbidity.append("1");
                beneMorbidity.append(GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR);
                beneMorbidity.append(beneficiaryMorbidityDetails.getBeneficiaryName());
                beneMorbidity.append(GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR);
                beneMorbidity.append("2");
                beneMorbidity.append(GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR);
                beneMorbidity.append(beneficiaryMorbidityDetails.getBeneficiaryType());
                beneMorbidity.append(GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR);
                beneMorbidity.append("3");
                beneMorbidity.append(GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR);
                beneMorbidity.append(familyUnderstand);
                beneMorbidity.append(GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR);
                List<IdentifiedMorbidityDetails> identifiedMorbidities = beneficiaryMorbidityDetails.getIdentifiedMorbidities();

                int noOfMorbidity = identifiedMorbidities.size();

                beneMorbidity.append("4");
                beneMorbidity.append(GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR);
                if (noOfMorbidity == 0) {
                    beneMorbidity.append(GlobalTypes.FALSE);
                    beneMorbidity.append(GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR);
                } else {
                    beneMorbidity.append(GlobalTypes.TRUE);
                    beneMorbidity.append(GlobalTypes.MULTI_VALUE_BEAN_SEPARATOR);
                    beneMorbidity.append("5");
                    beneMorbidity.append(GlobalTypes.ANSWER_STRING_FIRST_SEPARATOR);
                }


                for (int j = 0; j < noOfMorbidity; j++) {
                    IdentifiedMorbidityDetails elementAt = identifiedMorbidities.get(j);
                    String[] split = UtilBean.split(elementAt.getMorbidityCode(), GlobalTypes.KEY_VALUE_SEPARATOR);
                    String appendCode = split[0];
                    beneMorbidity.append(appendCode);
                    beneMorbidity.append(GlobalTypes.MORBIDITY_DETAILS_SEPARATOR);
                    beneMorbidity.append(elementAt.getRiskFactorOfIdentifiedMorbidities());
                    beneMorbidity.append(GlobalTypes.MORBIDITY_DETAILS_SEPARATOR);
                    List<String> identifiedSymptoms = elementAt.getIdentifiedSymptoms();
                    for (String symptom : identifiedSymptoms) {
                        beneMorbidity.append(symptom);
                        beneMorbidity.append(GlobalTypes.COMMA);
                    }
                    beneMorbidity.append(GlobalTypes.MORBIDITY_SEPARATOR_HASH);
                }

                finalMorbidity.append(beneMorbidity);
                finalMorbidity.append(GlobalTypes.BENEFICIARY_SEPARATOR);
            }

            return finalMorbidity.toString();
        }
        return null;
    }
}
