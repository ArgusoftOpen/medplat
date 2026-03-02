/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fhs.dto;

/**
 *
 * <p>
 *     Used for pregnancy registration.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public class PregnancyRegistrationDetailDto {

    private String motherRegNo;
    private String lmpDate;
    private String expectedDeliveryDate;
    private String registrationDate;
    private String pregnancyState;

    public String getMotherRegNo() {
        return motherRegNo;
    }

    public void setMotherRegNo(String motherRegNo) {
        this.motherRegNo = motherRegNo;
    }

    public String getLmpDate() {
        return lmpDate;
    }

    public void setLmpDate(String lmpDate) {
        this.lmpDate = lmpDate;
    }

    public String getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(String expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getPregnancyState() {
        return pregnancyState;
    }

    public void setPregnancyState(String pregnancyState) {
        this.pregnancyState = pregnancyState;
    }

}
