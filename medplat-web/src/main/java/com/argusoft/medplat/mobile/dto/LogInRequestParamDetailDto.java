/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.dto;

import java.util.List;
import java.util.Map;

/**
 * @author vaishali
 */
public class LogInRequestParamDetailDto {

    private String token;
    private Integer userId;
    private String roleType;
    private String villageCode;
    private String phoneNumber;
    private String imeiNumber;
    private Integer sdkVersion;
    private Integer freeSpaceMB;
    private String longitude;
    private String latitude;
    private Integer locationId;
    private List<InstalledAppsInfoBean> installedApps;
    private String lastUpdateDateForLocation;
    private String lastUpdateDateForFamily;
    private Long lastUpdateDateForNotifications;
    private Long lastUpdateDateForCbacDetails;
    private Long lastUpdateDateForListValue;
    private Long lastUpdateDateForDataQualityValue;
    private Long lastUpdateOfAnnouncements;
    private Long createdOnDateForLabel;
    private Map<String, Integer> sheetNameVersionMap;
    private Integer mobileFormVersion;
    private Long lastUpdateDateForLibrary;
    private Long lastUpdateDateForMigrationDetails;
    private List<Integer> readNotifications;
    private Long lastUpdateDateForHealthInfrastructure;
    private Long lastUpdateDateForAwwChildService;
    private Long lastUpdateDateForSchoolMaster;
    private Long lastUpdateDateForPregnancyStatus;
    private Long lastUpdateDateForCovidTravellersInfo;
    private Long lastUpdateDateForLocationTypeMaster;
    private Long lastUpdateDateForMenuMaster;
    private Long lastUpdateDateForChardhamTouristMaster;
    private Long lastUpdateDateForUserHealthInfra;
    private Long lastUpdateDateForFamilyAvailabilities;

    public Long getLastUpdateDateForSchoolMaster() {
        return lastUpdateDateForSchoolMaster;
    }

    public void setLastUpdateDateForSchoolMaster(Long lastUpdateDateForSchoolMaster) {
        this.lastUpdateDateForSchoolMaster = lastUpdateDateForSchoolMaster;
    }

    public String getToken() {
        return token;
    }

    public Long getLastUpdateDateForAwwChildService() {
        return lastUpdateDateForAwwChildService;
    }

    public void setLastUpdateDateForAwwChildService(Long lastUpdateDateForAwwChildService) {
        this.lastUpdateDateForAwwChildService = lastUpdateDateForAwwChildService;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public Integer getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(Integer sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public Integer getFreeSpaceMB() {
        return freeSpaceMB;
    }

    public void setFreeSpaceMB(Integer freeSpaceMB) {
        this.freeSpaceMB = freeSpaceMB;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public List<InstalledAppsInfoBean> getInstalledApps() {
        return installedApps;
    }

    public void setInstalledApps(List<InstalledAppsInfoBean> installedApps) {
        this.installedApps = installedApps;
    }

    public String getLastUpdateDateForLocation() {
        return lastUpdateDateForLocation;
    }

    public void setLastUpdateDateForLocation(String lastUpdateDateForLocation) {
        this.lastUpdateDateForLocation = lastUpdateDateForLocation;
    }

    public String getLastUpdateDateForFamily() {
        return lastUpdateDateForFamily;
    }

    public void setLastUpdateDateForFamily(String lastUpdateDateForFamily) {
        this.lastUpdateDateForFamily = lastUpdateDateForFamily;
    }

    public Long getLastUpdateDateForNotifications() {
        return lastUpdateDateForNotifications;
    }

    public void setLastUpdateDateForNotifications(Long lastUpdateDateForNotifications) {
        this.lastUpdateDateForNotifications = lastUpdateDateForNotifications;
    }

    public Long getLastUpdateDateForCbacDetails() {
        return lastUpdateDateForCbacDetails;
    }

    public void setLastUpdateDateForCbacDetails(Long lastUpdateDateForCbacDetails) {
        this.lastUpdateDateForCbacDetails = lastUpdateDateForCbacDetails;
    }

    public Long getLastUpdateDateForListValue() {
        return lastUpdateDateForListValue;
    }

    public void setLastUpdateDateForListValue(Long lastUpdateDateForListValue) {
        this.lastUpdateDateForListValue = lastUpdateDateForListValue;
    }

    public Long getLastUpdateDateForDataQualityValue() {
        return lastUpdateDateForDataQualityValue;
    }

    public void setLastUpdateDateForDataQualityValue(Long lastUpdateDateForDataQualityValue) {
        this.lastUpdateDateForDataQualityValue = lastUpdateDateForDataQualityValue;
    }

    public Long getLastUpdateOfAnnouncements() {
        return lastUpdateOfAnnouncements;
    }

    public void setLastUpdateOfAnnouncements(Long lastUpdateOfAnnouncements) {
        this.lastUpdateOfAnnouncements = lastUpdateOfAnnouncements;
    }

    public Long getCreatedOnDateForLabel() {
        return createdOnDateForLabel;
    }

    public void setCreatedOnDateForLabel(Long createdOnDateForLabel) {
        this.createdOnDateForLabel = createdOnDateForLabel;
    }

    public Map<String, Integer> getSheetNameVersionMap() {
        return sheetNameVersionMap;
    }

    public void setSheetNameVersionMap(Map<String, Integer> sheetNameVersionMap) {
        this.sheetNameVersionMap = sheetNameVersionMap;
    }

    public Integer getMobileFormVersion() {
        return mobileFormVersion;
    }

    public void setMobileFormVersion(Integer mobileFormVersion) {
        this.mobileFormVersion = mobileFormVersion;
    }

    public Long getLastUpdateDateForLibrary() {
        return lastUpdateDateForLibrary;
    }

    public void setLastUpdateDateForLibrary(Long lastUpdateDateForLibrary) {
        this.lastUpdateDateForLibrary = lastUpdateDateForLibrary;
    }

    public Long getLastUpdateDateForMigrationDetails() {
        return lastUpdateDateForMigrationDetails;
    }

    public void setLastUpdateDateForMigrationDetails(Long lastUpdateDateForMigrationDetails) {
        this.lastUpdateDateForMigrationDetails = lastUpdateDateForMigrationDetails;
    }

    public List<Integer> getReadNotifications() {
        return readNotifications;
    }

    public void setReadNotifications(List<Integer> readNotifications) {
        this.readNotifications = readNotifications;
    }

    public Long getLastUpdateDateForHealthInfrastructure() {
        return lastUpdateDateForHealthInfrastructure;
    }

    public void setLastUpdateDateForHealthInfrastructure(Long lastUpdateDateForHealthInfrastructure) {
        this.lastUpdateDateForHealthInfrastructure = lastUpdateDateForHealthInfrastructure;
    }

    public Long getLastUpdateDateForPregnancyStatus() {
        return lastUpdateDateForPregnancyStatus;
    }

    public void setLastUpdateDateForPregnancyStatus(Long lastUpdateDateForPregnancyStatus) {
        this.lastUpdateDateForPregnancyStatus = lastUpdateDateForPregnancyStatus;
    }

    public Long getLastUpdateDateForCovidTravellersInfo() {
        return lastUpdateDateForCovidTravellersInfo;
    }

    public void setLastUpdateDateForCovidTravellersInfo(Long lastUpdateDateForCovidTravellersInfo) {
        this.lastUpdateDateForCovidTravellersInfo = lastUpdateDateForCovidTravellersInfo;
    }

    public Long getLastUpdateDateForLocationTypeMaster() {
        return lastUpdateDateForLocationTypeMaster;
    }

    public void setLastUpdateDateForLocationTypeMaster(Long lastUpdateDateForLocationTypeMaster) {
        this.lastUpdateDateForLocationTypeMaster = lastUpdateDateForLocationTypeMaster;
    }

    public Long getLastUpdateDateForMenuMaster() {
        return lastUpdateDateForMenuMaster;
    }

    public void setLastUpdateDateForMenuMaster(Long lastUpdateDateForMenuMaster) {
        this.lastUpdateDateForMenuMaster = lastUpdateDateForMenuMaster;
    }

    public Long getLastUpdateDateForChardhamTouristMaster() {
        return lastUpdateDateForChardhamTouristMaster;
    }

    public void setLastUpdateDateForChardhamTouristMaster(Long lastUpdateDateForChardhamTouristMaster) {
        this.lastUpdateDateForChardhamTouristMaster = lastUpdateDateForChardhamTouristMaster;
    }

    public Long getLastUpdateDateForUserHealthInfra() {
        return lastUpdateDateForUserHealthInfra;
    }

    public void setLastUpdateDateForUserHealthInfra(Long lastUpdateDateForUserHealthInfra) {
        this.lastUpdateDateForUserHealthInfra = lastUpdateDateForUserHealthInfra;
    }

    public Long getLastUpdateDateForFamilyAvailabilities() {
        return lastUpdateDateForFamilyAvailabilities;
    }

    public void setLastUpdateDateForFamilyAvailabilities(Long lastUpdateDateForFamilyAvailabilities) {
        this.lastUpdateDateForFamilyAvailabilities = lastUpdateDateForFamilyAvailabilities;
    }

    @Override
    public String toString() {
        return "LogInRequestParamDetailDto{" +
                "token='" + token + '\'' +
                ", userId=" + userId +
                ", roleType='" + roleType + '\'' +
                ", villageCode='" + villageCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", imeiNumber='" + imeiNumber + '\'' +
                ", sdkVersion=" + sdkVersion +
                ", freeSpaceMB=" + freeSpaceMB +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", locationId=" + locationId +
                ", installedApps=" + installedApps +
                ", lastUpdateDateForLocation='" + lastUpdateDateForLocation + '\'' +
                ", lastUpdateDateForFamily='" + lastUpdateDateForFamily + '\'' +
                ", lastUpdateDateForNotifications=" + lastUpdateDateForNotifications +
                ", lastUpdateDateForCbacDetails=" + lastUpdateDateForCbacDetails +
                ", lastUpdateDateForListValue=" + lastUpdateDateForListValue +
                ", lastUpdateDateForDataQualityValue=" + lastUpdateDateForDataQualityValue +
                ", lastUpdateOfAnnouncements='" + lastUpdateOfAnnouncements + '\'' +
                ", createdOnDateForLabel=" + createdOnDateForLabel +
                ", sheetNameVersionMap=" + sheetNameVersionMap +
                ", lastUpdateDateForLibrary=" + lastUpdateDateForLibrary +
                ", lastUpdateDateForMigrationDetails=" + lastUpdateDateForMigrationDetails +
                ", readNotifications=" + readNotifications +
                ", lastUpdateDateForHealthInfrastructure=" + lastUpdateDateForHealthInfrastructure +
                ", lastUpdateDateForAwwChildService=" + lastUpdateDateForAwwChildService +
                ", lastUpdateDateForSchoolMaster=" + lastUpdateDateForSchoolMaster +
                ", lastUpdateDateForChardhamTouristMaster=" + lastUpdateDateForChardhamTouristMaster +
                ", lastUpdateDateForUserHealthInfra=" + lastUpdateDateForUserHealthInfra +
                '}';
    }
}
