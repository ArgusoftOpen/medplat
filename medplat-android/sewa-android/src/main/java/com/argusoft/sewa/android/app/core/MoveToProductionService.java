package com.argusoft.sewa.android.app.core;

import com.argusoft.sewa.android.app.model.FormAccessibilityBean;

import java.util.List;

public interface MoveToProductionService {

    FormAccessibilityBean retrieveFormAccessibilityBeanByFormType(String formType);

    List<FormAccessibilityBean> isAnyFormTrainingCompleted();

    void saveFormsToMoveToProduction(List<FormAccessibilityBean> formAccessibilityBeans);

    void saveFormAccessibilityBeansFromServer();

    void postUserReadyToMoveProduction();

}
