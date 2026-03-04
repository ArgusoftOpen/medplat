/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author kunjan
 */
@Entity
@Table(name = "uncaught_exception_mobile")
public class UncaughtExceptionMobile implements Serializable,Comparable<UncaughtExceptionMobile>{
    private static final int serialVersionUID = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Column(name = "user_name",nullable = false)
    private String userName;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "stack_trace",nullable = false,columnDefinition = "text")
    private String stackTrace;
    @Column(name = "android_version")
    private String androidVersion;
    @Column
    private String manufacturer;
    @Column
    private String model;
    @Column(name = "on_date_mobile", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date onDate;
    @Column(name="revision_number")
    private Integer revisionNumber;
    @Column(name="is_active")
    private Boolean isActive;
    @Column(name="exception_type")
    private String exceptionType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getOnDate() {
        return onDate;
    }

    public void setOnDate(Date onDate) {
        this.onDate = onDate;
    }

    public Integer getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(Integer revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public int compareTo(UncaughtExceptionMobile o) {
        return onDate.compareTo(o.onDate);
    }
    
}
