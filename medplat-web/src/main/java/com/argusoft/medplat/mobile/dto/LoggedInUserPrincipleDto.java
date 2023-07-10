/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.dto;


import com.argusoft.medplat.course.model.LmsUserMetaData;
import com.argusoft.medplat.fhs.dto.FhwServiceStatusDto;
import com.argusoft.medplat.infrastructure.dto.SchoolDataBean;
import com.argusoft.medplat.query.dto.QueryDto;
import com.argusoft.medplat.rch.dto.MemberPregnancyStatusBean;
import com.argusoft.medplat.rch.dto.RchVillageProfileDto;
import com.argusoft.medplat.web.location.model.LocationTypeMaster;
import com.argusoft.medplat.web.users.model.UserHealthInfrastructure;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vaishali
 */
public class LoggedInUserPrincipleDto {

    private List<FamilyDataBean> assignedFamilies;
    private Map<Integer, List<SurveyLocationMobDataBean>> retrievedVillageAndChildLocations;
    private List<FamilyDataBean> orphanedAndReverificationFamilies;
    private List<FhwServiceStatusDto> fhwServiceStatusDtos;
    private List<FamilyDataBean> updatedFamilyByDate;
    private List<TechoNotificationDataBean> notifications;
    private List<Integer> deletedNotifications;
    private List<SchoolDataBean> schools;
    private QueryDto retrievedLabels;
    private QueryDto csvCoordinates;
    private Map<String, List<ComponentTagDto>> retrievedXlsData;
    private Map<String, Integer> currentSheetVersion;
    private Integer currentMobileFormVersion;
    private List<LinkedHashMap<String, Object>> retrievedListValues;
    private List<LinkedHashMap<String, Object>> dataQualityBeans;
    //    private List<AnnouncementMobDataBean> retrievedAnnouncements;
    private List<String> deletedFamilies;
    private List<RchVillageProfileDto> rchVillageProfileDtos;
    private List<LocationMasterDataBean> locationMasterBeans;
    private List<Integer> locationsForFamilyDataDeletion;
    private String newToken;

    private List<String> features;
    private List<MobileLibraryDataBean> mobileLibraryDataBeans;
    private List<HealthInfrastructureBean> healthInfrastructures;
    private List<MemberPregnancyStatusBean> pregnancyStatus;
    private Long lastPregnancyStatusDate;
    private List<LocationTypeMaster> locationTypeMasters;
    private List<MenuDataBean> mobileMenus;
//    private List<CourseDataBean> courseDataBeans;

    private String lmsFileDownloadUrl;

//    private List<DrugInventoryDetailDataBean> drugInventoryBeans;

    private List<UserHealthInfrastructure> userHealthInfrastructures;
    private Map<String, String> systemConfigurations;
    private List<LmsUserMetaData> lmsUserMetaData;
    private List<CourseDataBean> courseDataBeans;
    private List<AnnouncementMobDataBean> retrievedAnnouncements;

    public List<LmsUserMetaData> getLmsUserMetaData() {
        return lmsUserMetaData;
    }

    public void setLmsUserMetaData(List<LmsUserMetaData> lmsUserMetaData) {
        this.lmsUserMetaData = lmsUserMetaData;
    }

    public List<CourseDataBean> getCourseDataBeans() {
        return courseDataBeans;
    }

    public void setCourseDataBeans(List<CourseDataBean> courseDataBeans) {
        this.courseDataBeans = courseDataBeans;
    }

    public List<AnnouncementMobDataBean> getRetrievedAnnouncements() {
        return retrievedAnnouncements;
    }

    public void setRetrievedAnnouncements(List<AnnouncementMobDataBean> retrievedAnnouncements) {
        this.retrievedAnnouncements = retrievedAnnouncements;
    }

    public List<FamilyDataBean> getAssignedFamilies() {
        return assignedFamilies;
    }

    public void setAssignedFamilies(List<FamilyDataBean> assignedFamilies) {
        this.assignedFamilies = assignedFamilies;
    }

    public Map<Integer, List<SurveyLocationMobDataBean>> getRetrievedVillageAndChildLocations() {
        return retrievedVillageAndChildLocations;
    }

    public void setRetrievedVillageAndChildLocations(Map<Integer, List<SurveyLocationMobDataBean>> retrievedVillageAndChildLocations) {
        this.retrievedVillageAndChildLocations = retrievedVillageAndChildLocations;
    }

    public List<FamilyDataBean> getOrphanedAndReverificationFamilies() {
        return orphanedAndReverificationFamilies;
    }

    public void setOrphanedAndReverificationFamilies(List<FamilyDataBean> orphanedAndReverificationFamilies) {
        this.orphanedAndReverificationFamilies = orphanedAndReverificationFamilies;
    }

    public List<FhwServiceStatusDto> getFhwServiceStatusDtos() {
        return fhwServiceStatusDtos;
    }

    public void setFhwServiceStatusDtos(List<FhwServiceStatusDto> fhwServiceStatusDtos) {
        this.fhwServiceStatusDtos = fhwServiceStatusDtos;
    }

    public List<FamilyDataBean> getUpdatedFamilyByDate() {
        return updatedFamilyByDate;
    }

    public void setUpdatedFamilyByDate(List<FamilyDataBean> updatedFamilyByDate) {
        this.updatedFamilyByDate = updatedFamilyByDate;
    }

    public List<TechoNotificationDataBean> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<TechoNotificationDataBean> notifications) {
        this.notifications = notifications;
    }

    public List<Integer> getDeletedNotifications() {
        return deletedNotifications;
    }

    public void setDeletedNotifications(List<Integer> deletedNotifications) {
        this.deletedNotifications = deletedNotifications;
    }

    public QueryDto getRetrievedLabels() {
        return retrievedLabels;
    }

    public void setRetrievedLabels(QueryDto retrievedLabels) {
        this.retrievedLabels = retrievedLabels;
    }

    public QueryDto getCsvCoordinates() {
        return csvCoordinates;
    }

    public void setCsvCoordinates(QueryDto csvCoordinates) {
        this.csvCoordinates = csvCoordinates;
    }

    public Map<String, List<ComponentTagDto>> getRetrievedXlsData() {
        return retrievedXlsData;
    }

    public void setRetrievedXlsData(Map<String, List<ComponentTagDto>> retrievedXlsData) {
        this.retrievedXlsData = retrievedXlsData;
    }

    public Map<String, Integer> getCurrentSheetVersion() {
        return currentSheetVersion;
    }

    public void setCurrentSheetVersion(Map<String, Integer> currentSheetVersion) {
        this.currentSheetVersion = currentSheetVersion;
    }

    public Integer getCurrentMobileFormVersion() {
        return currentMobileFormVersion;
    }

    public void setCurrentMobileFormVersion(Integer currentMobileFormVersion) {
        this.currentMobileFormVersion = currentMobileFormVersion;
    }

    public List<LinkedHashMap<String, Object>> getRetrievedListValues() {
        return retrievedListValues;
    }

    public void setRetrievedListValues(List<LinkedHashMap<String, Object>> retrievedListValues) {
        this.retrievedListValues = retrievedListValues;
    }

    public List<LinkedHashMap<String, Object>> getDataQualityBeans() {
        return dataQualityBeans;
    }

    public void setDataQualityBeans(List<LinkedHashMap<String, Object>> dataQualityBeans) {
        this.dataQualityBeans = dataQualityBeans;
    }

//    public List<AnnouncementMobDataBean> getRetrievedAnnouncements() {
//        return retrievedAnnouncements;
//    }
//
//    public void setRetrievedAnnouncements(List<AnnouncementMobDataBean> retrievedAnnouncements) {
//        this.retrievedAnnouncements = retrievedAnnouncements;
//    }

    public List<String> getDeletedFamilies() {
        return deletedFamilies;
    }

    public void setDeletedFamilies(List<String> deletedFamilies) {
        this.deletedFamilies = deletedFamilies;
    }

    public List<RchVillageProfileDto> getRchVillageProfileDtos() {
        return rchVillageProfileDtos;
    }

    public void setRchVillageProfileDtos(List<RchVillageProfileDto> rchVillageProfileDtos) {
        this.rchVillageProfileDtos = rchVillageProfileDtos;
    }

    public List<LocationMasterDataBean> getLocationMasterBeans() {
        return locationMasterBeans;
    }

    public void setLocationMasterBeans(List<LocationMasterDataBean> locationMasterBeans) {
        this.locationMasterBeans = locationMasterBeans;
    }

    public List<Integer> getLocationsForFamilyDataDeletion() {
        return locationsForFamilyDataDeletion;
    }

    public void setLocationsForFamilyDataDeletion(List<Integer> locationsForFamilyDataDeletion) {
        this.locationsForFamilyDataDeletion = locationsForFamilyDataDeletion;
    }

    public String getNewToken() {
        return newToken;
    }

    public void setNewToken(String newToken) {
        this.newToken = newToken;
    }


    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public List<MobileLibraryDataBean> getMobileLibraryDataBeans() {
        return mobileLibraryDataBeans;
    }

    public void setMobileLibraryDataBeans(List<MobileLibraryDataBean> mobileLibraryDataBeans) {
        this.mobileLibraryDataBeans = mobileLibraryDataBeans;
    }

    public List<HealthInfrastructureBean> getHealthInfrastructures() {
        return healthInfrastructures;
    }

    public void setHealthInfrastructures(List<HealthInfrastructureBean> healthInfrastructures) {
        this.healthInfrastructures = healthInfrastructures;
    }

    public List<MemberPregnancyStatusBean> getPregnancyStatus() {
        return pregnancyStatus;
    }

    public void setPregnancyStatus(List<MemberPregnancyStatusBean> pregnancyStatus) {
        this.pregnancyStatus = pregnancyStatus;
    }

    public Long getLastPregnancyStatusDate() {
        return lastPregnancyStatusDate;
    }

    public void setLastPregnancyStatusDate(Long lastPregnancyStatusDate) {
        this.lastPregnancyStatusDate = lastPregnancyStatusDate;
    }


    public List<LocationTypeMaster> getLocationTypeMasters() {
        return locationTypeMasters;
    }

    public void setLocationTypeMasters(List<LocationTypeMaster> locationTypeMasters) {
        this.locationTypeMasters = locationTypeMasters;
    }

    public List<MenuDataBean> getMobileMenus() {
        return mobileMenus;
    }

    public void setMobileMenus(List<MenuDataBean> mobileMenus) {
        this.mobileMenus = mobileMenus;
    }


//    public List<CourseDataBean> getCourseDataBeans() {
//        return courseDataBeans;
//    }
//
//    public void setCourseDataBeans(List<CourseDataBean> courseDataBeans) {
//        this.courseDataBeans = courseDataBeans;
//    }

    public String getLmsFileDownloadUrl() {
        return lmsFileDownloadUrl;
    }

    public void setLmsFileDownloadUrl(String lmsFileDownloadUrl) {
        this.lmsFileDownloadUrl = lmsFileDownloadUrl;
    }


    public List<UserHealthInfrastructure> getUserHealthInfrastructures() {
        return userHealthInfrastructures;
    }

    public void setUserHealthInfrastructures(List<UserHealthInfrastructure> userHealthInfrastructures) {
        this.userHealthInfrastructures = userHealthInfrastructures;
    }

    public Map<String, String> getSystemConfigurations() {
        return systemConfigurations;
    }

    public void setSystemConfigurations(Map<String, String> systemConfigurations) {
        this.systemConfigurations = systemConfigurations;
    }

    public List<SchoolDataBean> getSchools() {
        return schools;
    }

    public void setSchools(List<SchoolDataBean> schools) {
        this.schools = schools;
    }
}
