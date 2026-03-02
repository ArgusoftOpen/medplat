/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.event.dto;

/**
 *
 * <p>
 *     Used for query master parameter.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class QueryMasterParamterDto {

    private String parameterName;
    private String mappingValue;
    private Boolean isFixed;

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getMappingValue() {
        return mappingValue;
    }

    public void setMappingValue(String mappingValue) {
        this.mappingValue = mappingValue;
    }

    public Boolean getIsFixed() {
        return isFixed != null && isFixed;
    }

    public void setIsFixed(Boolean isFixed) {
        this.isFixed = isFixed;
    }

}
