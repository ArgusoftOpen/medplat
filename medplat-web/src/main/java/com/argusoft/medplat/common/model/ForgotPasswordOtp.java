/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.model;

import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.LastModifiedDate;

/**
 *<p>Defines fields related to user</p>
 * @author akshar
 * @since 26/08/2020 5:30
 */
@Entity
@Table(name = "forgot_password_otp")
public class ForgotPasswordOtp implements Serializable {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "forgot_password_otp", nullable = false, length = 4)
    private String otp;

    @LastModifiedDate
    @Column(name = "modified_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;
    @Column(name = "is_verified")
    private Boolean isVerified;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    /**
     * An util class defines string constant
     */
    public static class Fields {

        private Fields() {

        }

        public static final String USER_ID = "userId";
        public static final String OTP = "otp";
        public static final String MODIFIED_ON = "modifiedOn";
    }

}
