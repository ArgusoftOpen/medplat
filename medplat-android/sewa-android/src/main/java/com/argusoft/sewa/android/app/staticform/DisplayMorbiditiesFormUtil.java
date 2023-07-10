package com.argusoft.sewa.android.app.staticform;

import com.argusoft.sewa.android.app.morbidities.beans.BeneficiaryMorbidityDetails;
import com.argusoft.sewa.android.app.morbidities.beans.IdentifiedMorbidityDetails;
import com.argusoft.sewa.android.app.morbidities.constants.MorbiditiesConstant;

import java.util.Comparator;

/**
 * @author alpeshkyada
 */
public class DisplayMorbiditiesFormUtil {

    private DisplayMorbiditiesFormUtil() {
        throw new IllegalStateException("Utility Class");
    }

    public static final Comparator<BeneficiaryMorbidityDetails> sortBeneficiaryMorbidityDetailsByBeneficiaryType = (o1, o2) -> (o1.getBeneficiaryType().toLowerCase().compareTo(o2.getBeneficiaryType().toLowerCase()) * -1);
    public static final Comparator<IdentifiedMorbidityDetails> sortIdentifiedMorbiditiesByRiskFactor = (o1, o2) -> Integer.compare(riskValueOf(o1.getRiskFactorOfIdentifiedMorbidities()), riskValueOf(o2.getRiskFactorOfIdentifiedMorbidities()));

    public static int riskValueOf(String riskStatus) {
        if (riskStatus.equalsIgnoreCase(MorbiditiesConstant.RED_COLOR)) {
            return 1;
        } else if (riskStatus.equalsIgnoreCase(MorbiditiesConstant.YELLOW_COLOR)) {
            return 2;
        } else if (riskStatus.equalsIgnoreCase(MorbiditiesConstant.GREEN_COLOR)) {
            return 3;
        } else {
            return 4;
        }
    }
}
