package com.argusoft.sewa.android.app.core;

import java.util.Map;

public interface RchHighRiskService {

    String identifyHighRiskForRchAnc(Map<String, Object> mapOfAnswers);

    String identifyHighRiskForChildRchPnc(Object dangerousSignAnswer, Object otherDangerousSignAnswer);

    String identifyHighRiskForMotherRchPnc(Object dangerousSignAnswer, Object otherDangerousSignAnswer);

    String identifyHighRiskForChildRchWpd(Object weightAnswer);

    String identifyHighRiskForChardhamTourist(Map<String, Object> mapOfAnswers);

}
