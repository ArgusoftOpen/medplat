package com.argusoft.medplat.systemfunction;

/**
 * <p>System function parameter dto</p>
 * @author ketul
 * @since 08/09/20 04:00 PM
 * 
 */
public class FunctionParametersDto {
    private String parameterName;
    private String parameterType;

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }
}
