package com.argusoft.medplat.rch.dto;

/**
 * <p>
 * Used for morbidity.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
public class MorbidityDto {

    private String code;
    private String status;
    private String symptoms;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
}
