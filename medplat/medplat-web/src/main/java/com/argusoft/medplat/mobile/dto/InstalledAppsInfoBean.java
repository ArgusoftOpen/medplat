package com.argusoft.medplat.mobile.dto;

import java.util.Date;

/**
 *
 * @author prateek on 4 Feb, 2019
 */
public class InstalledAppsInfoBean {

    private Integer userId;

    private String imei;

    private String applicationName;

    private String packageName;

    private Integer uid;

    private String versionName;

    private Integer versionCode;

    private Long installedDate;

    private Long lastUpdateDate;

    private Date usedDate;

    private Integer receivedData;

    private Integer sentData;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public Long getInstalledDate() {
        return installedDate;
    }

    public void setInstalledDate(Long installedDate) {
        this.installedDate = installedDate;
    }

    public Long getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Long lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Date getUsedDate() {
        return usedDate;
    }

    public void setUsedDate(Date usedDate) {
        this.usedDate = usedDate;
    }

    public Integer getReceivedData() {
        return receivedData;
    }

    public void setReceivedData(Integer receivedData) {
        this.receivedData = receivedData;
    }

    public Integer getSentData() {
        return sentData;
    }

    public void setSentData(Integer sentData) {
        this.sentData = sentData;
    }
}
