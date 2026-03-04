/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.dto;

/**
 *
 * @author rahul
 */
public class MobileNumberUpdationBean {

    private Integer memberId; //memberId

    private String mobileNumber;

    private Boolean whatsappSmsSchems;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Boolean getWhatsappSmsSchems() {
        return whatsappSmsSchems;
    }

    public void setWhatsappSmsSchems(Boolean whatsappSmsSchems) {
        this.whatsappSmsSchems = whatsappSmsSchems;
    }
}
