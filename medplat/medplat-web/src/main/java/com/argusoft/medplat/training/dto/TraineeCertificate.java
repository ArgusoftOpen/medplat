/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.dto;

/**
 *
 * <p>
 *     Used for trainee certificate.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public class TraineeCertificate {
    
    private Integer userId;
    private Boolean trained;
    private String remark;
    private Integer daysAttendedInPercentage;
    private String userName;
    private Integer certificateId;
    private String certificateType;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getTrained() {
        return trained;
    }

    public void setTrained(Boolean trained) {
        this.trained = trained;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDaysAttendedInPercentage() {
        return daysAttendedInPercentage;
    }

    public void setDaysAttendedInPercentage(Integer daysAttendedInPercentage) {
        this.daysAttendedInPercentage = daysAttendedInPercentage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(Integer certificateId) {
        this.certificateId = certificateId;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }
       
}
