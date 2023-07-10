package com.argusoft.sewa.android.app.databean;

import androidx.annotation.NonNull;

/**
 * Created by prateek on 23/6/18.
 */

public class WorkerDetailDataBean {

    private Long userId;
    private String name;
    private String mobileNumber;
    private String aadharNumber;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return "WorkerDetailDataBean{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", aadharNumber='" + aadharNumber + '\'' +
                '}';
    }
}
