package com.argusoft.sewa.android.app.databean;

import androidx.annotation.NonNull;

public class MobileNumberUpdationBean {

    private String memberId; //memberId

    private String mobileNumber;

    private Boolean whatsappSmsSchems;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
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

    @NonNull
    @Override
    public String toString() {
        return "MobileNumberUpdationBean{" +
                "memberId='" + memberId + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", whatsappSmsSchems=" + whatsappSmsSchems +
                '}';
    }
}
