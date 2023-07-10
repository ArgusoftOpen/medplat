/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.users.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *<p>Defines fields related to user</p>
 * @author vaishali
 * @since 26/08/2020 5:30
 */
@Entity
@Table(name = "um_user_login_det")
public class UserLoginDetailMaster implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    @Column(name = "no_of_attempts")
    private Integer noOfAttempts;
    @Column(name = "logging_from_web", nullable = false)
    private boolean loggingFromWeb;
    @Column(name = "imei_number")
    private String imeiNumber;
    @Column(name = "apk_version")
    private Integer apkVersion;
    @Column(name = "created_on")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdOn = new Date();
    @Column(name = "mobile_form_version")
    private Integer mobileFormVersion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getNoOfAttempts() {
        return noOfAttempts;
    }

    public void setNoOfAttempts(Integer noOfAttempts) {
        this.noOfAttempts = noOfAttempts;
    }

    public boolean isLoggingFromWeb() {
        return loggingFromWeb;
    }

    public void setLoggingFromWeb(boolean loggingFromWeb) {
        this.loggingFromWeb = loggingFromWeb;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public Integer getApkVersion() {
        return apkVersion;
    }

    public void setApkVersion(Integer apkVersion) {
        this.apkVersion = apkVersion;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getMobileFormVersion() {
        return mobileFormVersion;
    }

    public void setMobileFormVersion(Integer mobileFormVersion) {
        this.mobileFormVersion = mobileFormVersion;
    }

    /**
     * An util class defines string constant
     */
    public static class Fields {

        private Fields() {
            
        }

        public static final String ID = "id";
        public static final String USER_ID = "userId";
        public static final String NO_OF_ATTEMPTS = "noOfAttempts";
    }

}
