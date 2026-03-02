/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.util;

import com.argusoft.medplat.training.validation.ValidationResult;
import com.argusoft.medplat.training.validation.impl.ValidationResultInfo;

/**
 *
 * <p>
 * Define utility methods for validations.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 10:19 AM
 */
public class ValidationUtils {
    private ValidationUtils(){}
    /**
     * Make validation result.
     * @param errorMessage Error message.
     * @param element Element.
     * @param errorLevel Error level.
     * @return Returns validation result.
     */
    public static ValidationResultInfo makeValidationResultInfo(
            String errorMessage,
            String element,
            ValidationResult.ErrorLevel errorLevel) {

        ValidationResultInfo validationResultInfo = new ValidationResultInfo();
        validationResultInfo.setError(errorMessage);
        validationResultInfo.setElement(element);
        validationResultInfo.setLevel(errorLevel);
        return validationResultInfo;
    }
}
