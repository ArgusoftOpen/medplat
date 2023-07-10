package com.argusoft.sewa.android.app.databean;

import com.argusoft.sewa.android.app.model.CovidTravellersInfoBean;
import com.argusoft.sewa.android.app.model.DataQualityBean;
import com.argusoft.sewa.android.app.model.DrugInventoryBean;
import com.argusoft.sewa.android.app.model.FhwServiceDetailBean;
import com.argusoft.sewa.android.app.model.HealthInfrastructureBean;
import com.argusoft.sewa.android.app.model.LmsUserMetaDataBean;
import com.argusoft.sewa.android.app.model.LocationMasterBean;
import com.argusoft.sewa.android.app.model.LocationTypeBean;
import com.argusoft.sewa.android.app.model.MemberCbacDetailBean;
import com.argusoft.sewa.android.app.model.MemberPregnancyStatusBean;
import com.argusoft.sewa.android.app.model.MenuBean;
import com.argusoft.sewa.android.app.model.SchoolBean;
import com.argusoft.sewa.android.app.model.UserHealthInfraBean;

import java.util.List;
import java.util.Map;

/**
 * Created by prateek on 18/5/18.
 */

public class LoggedInUserPrincipleDataBean {

    private List<String> deletedFamilies;

    private List<FamilyDataBean> assignedFamilies;

    private Map<Long, List<SurveyLocationMobDataBean>> retrievedVillageAndChildLocations;

    private List<FamilyDataBean> orphanedAndReverificationFamilies;

    private List<FhwServiceDetailBean> fhwServiceStatusDtos;

    private List<FamilyDataBean> updatedFamilyByDate;

    private List<NotificationMobDataBean> notifications;

    private List<Long> deletedNotifications;

    private QueryMobDataBean retrievedLabels;

    private Map<String, List<ComponentTagBean>> retrievedXlsData;

    private Map<String, Integer> currentSheetVersion;

    private Integer currentMobileFormVersion;

    private List<FieldValueMobDataBean> retrievedListValues;

    private List<DataQualityBean> dataQualityBeans;

    private List<AnnouncementMobDataBean> retrievedAnnouncements;

    private List<RchVillageProfileDataBean> rchVillageProfileDataBeans;

    private List<LocationMasterBean> locationMasterBeans;

    public List<String> getDeletedFamilies() {
        return deletedFamilies;
    }

    private List<Long> locationsForFamilyDataDeletion;

    private String newToken;

    private List<MemberCbacDetailBean> memberCbacDetails;

    private List<String> features;

    private List<LibraryDataBean> mobileLibraryDataBeans;

    private List<MigratedMembersDataBean> migratedMembersDataBeans;

    private List<HealthInfrastructureBean> healthInfrastructures;

    private List<SchoolBean> schools;

    private QueryMobDataBean csvCoordinates;

    private List<MemberPregnancyStatusBean> pregnancyStatus;

    private Long lastPregnancyStatusDate;

    private List<CovidTravellersInfoBean> covidTravellersInfos;

    private List<LocationTypeBean> locationTypeMasters;

    private List<MenuBean> mobileMenus;

    private List<MigratedFamilyDataBean> migratedFamilyDataBeans;

    private List<LmsCourseDataBean> courseDataBeans;

    private List<LmsUserMetaDataBean> lmsUserMetaData;

    private String lmsFileDownloadUrl;

    private List<MemberMoConfirmedDataBean> moConfirmedDataBeans;

    private List<DrugInventoryBean> drugInventoryBeans;

    private List<UserHealthInfraBean> userHealthInfrastructures;

    private Map<String, String> systemConfigurations;

    public void setDeletedFamilies(List<String> deletedFamilies) {
        this.deletedFamilies = deletedFamilies;
    }

    public List<FamilyDataBean> getAssignedFamilies() {
        return assignedFamilies;
    }

    public void setAssignedFamilies(List<FamilyDataBean> assignedFamilies) {
        this.assignedFamilies = assignedFamilies;
    }

    public Map<Long, List<SurveyLocationMobDataBean>> getRetrievedVillageAndChildLocations() {
        return retrievedVillageAndChildLocations;
    }

    public void setRetrievedVillageAndChildLocations(Map<Long, List<SurveyLocationMobDataBean>> retrievedVillageAndChildLocations) {
        this.retrievedVillageAndChildLocations = retrievedVillageAndChildLocations;
    }

    public List<FamilyDataBean> getOrphanedAndReverificationFamilies() {
        return orphanedAndReverificationFamilies;
    }

    public void setOrphanedAndReverificationFamilies(List<FamilyDataBean> orphanedAndReverificationFamilies) {
        this.orphanedAndReverificationFamilies = orphanedAndReverificationFamilies;
    }

    public List<FhwServiceDetailBean> getFhwServiceStatusDtos() {
        return fhwServiceStatusDtos;
    }

    public void setFhwServiceStatusDtos(List<FhwServiceDetailBean> fhwServiceStatusDtos) {
        this.fhwServiceStatusDtos = fhwServiceStatusDtos;
    }

    public List<FamilyDataBean> getUpdatedFamilyByDate() {
        return updatedFamilyByDate;
    }

    public void setUpdatedFamilyByDate(List<FamilyDataBean> updatedFamilyByDate) {
        this.updatedFamilyByDate = updatedFamilyByDate;
    }

    public List<NotificationMobDataBean> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationMobDataBean> notifications) {
        this.notifications = notifications;
    }

    public List<Long> getDeletedNotifications() {
        return deletedNotifications;
    }

    public void setDeletedNotifications(List<Long> deletedNotifications) {
        this.deletedNotifications = deletedNotifications;
    }

    public QueryMobDataBean getRetrievedLabels() {
        return retrievedLabels;
    }

    public void setRetrievedLabels(QueryMobDataBean retrievedLabels) {
        this.retrievedLabels = retrievedLabels;
    }

    public Map<String, List<ComponentTagBean>> getRetrievedXlsData() {
        return retrievedXlsData;
    }

    public void setRetrievedXlsData(Map<String, List<ComponentTagBean>> retrievedXlsData) {
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

    public List<FieldValueMobDataBean> getRetrievedListValues() {
        return retrievedListValues;
    }

    public void setRetrievedListValues(List<FieldValueMobDataBean> retrievedListValues) {
        this.retrievedListValues = retrievedListValues;
    }

    public List<DataQualityBean> getDataQualityBeans() {
        return dataQualityBeans;
    }

    public void setDataQualityBeans(List<DataQualityBean> dataQualityBeans) {
        this.dataQualityBeans = dataQualityBeans;
    }

    public List<AnnouncementMobDataBean> getRetrievedAnnouncements() {
        return retrievedAnnouncements;
    }

    public void setRetrievedAnnouncements(List<AnnouncementMobDataBean> retrievedAnnouncements) {
        this.retrievedAnnouncements = retrievedAnnouncements;
    }

    public List<RchVillageProfileDataBean> getRchVillageProfileDataBeans() {
        return rchVillageProfileDataBeans;
    }

    public void setRchVillageProfileDataBeans(List<RchVillageProfileDataBean> rchVillageProfileDataBeans) {
        this.rchVillageProfileDataBeans = rchVillageProfileDataBeans;
    }

    public List<LocationMasterBean> getLocationMasterBeans() {
        return locationMasterBeans;
    }

    public void setLocationMasterBeans(List<LocationMasterBean> locationMasterBeans) {
        this.locationMasterBeans = locationMasterBeans;
    }

    public List<Long> getLocationsForFamilyDataDeletion() {
        return locationsForFamilyDataDeletion;
    }

    public void setLocationsForFamilyDataDeletion(List<Long> locationsForFamilyDataDeletion) {
        this.locationsForFamilyDataDeletion = locationsForFamilyDataDeletion;
    }

    public String getNewToken() {
        return newToken;
    }

    public void setNewToken(String newToken) {
        this.newToken = newToken;
    }

    public List<MemberCbacDetailBean> getMemberCbacDetails() {
        return memberCbacDetails;
    }

    public void setMemberCbacDetails(List<MemberCbacDetailBean> memberCbacDetails) {
        this.memberCbacDetails = memberCbacDetails;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public List<LibraryDataBean> getMobileLibraryDataBeans() {
        return mobileLibraryDataBeans;
    }

    public void setMobileLibraryDataBeans(List<LibraryDataBean> mobileLibraryDataBeans) {
        this.mobileLibraryDataBeans = mobileLibraryDataBeans;
    }

    public List<MigratedMembersDataBean> getMigratedMembersDataBeans() {
        return migratedMembersDataBeans;
    }

    public void setMigratedMembersDataBeans(List<MigratedMembersDataBean> migratedMembersDataBeans) {
        this.migratedMembersDataBeans = migratedMembersDataBeans;
    }

    public List<HealthInfrastructureBean> getHealthInfrastructures() {
        return healthInfrastructures;
    }

    public void setHealthInfrastructures(List<HealthInfrastructureBean> healthInfrastructures) {
        this.healthInfrastructures = healthInfrastructures;
    }

    public List<SchoolBean> getSchools() {
        return schools;
    }

    public void setSchools(List<SchoolBean> schools) {
        this.schools = schools;
    }

    public QueryMobDataBean getCsvCoordinates() {
        return csvCoordinates;
    }

    public void setCsvCoordinates(QueryMobDataBean csvCoordinates) {
        this.csvCoordinates = csvCoordinates;
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

    public List<CovidTravellersInfoBean> getCovidTravellersInfos() {
        return covidTravellersInfos;
    }

    public void setCovidTravellersInfos(List<CovidTravellersInfoBean> covidTravellersInfos) {
        this.covidTravellersInfos = covidTravellersInfos;
    }

    public List<LocationTypeBean> getLocationTypeMasters() {
        return locationTypeMasters;
    }

    public void setLocationTypeMasters(List<LocationTypeBean> locationTypeMasters) {
        this.locationTypeMasters = locationTypeMasters;
    }

    public List<MenuBean> getMobileMenus() {
        return mobileMenus;
    }

    public void setMobileMenus(List<MenuBean> mobileMenus) {
        this.mobileMenus = mobileMenus;
    }

    public List<MigratedFamilyDataBean> getMigratedFamilyDataBeans() {
        return migratedFamilyDataBeans;
    }

    public void setMigratedFamilyDataBeans(List<MigratedFamilyDataBean> migratedFamilyDataBeans) {
        this.migratedFamilyDataBeans = migratedFamilyDataBeans;
    }

    public List<LmsCourseDataBean> getCourseDataBeans() {
        return courseDataBeans;
    }

    public void setCourseDataBeans(List<LmsCourseDataBean> courseDataBeans) {
        this.courseDataBeans = courseDataBeans;
    }

    public List<LmsUserMetaDataBean> getLmsUserMetaData() {
        return lmsUserMetaData;
    }

    public void setLmsUserMetaData(List<LmsUserMetaDataBean> lmsUserMetaData) {
        this.lmsUserMetaData = lmsUserMetaData;
    }

    public String getLmsFileDownloadUrl() {
        return lmsFileDownloadUrl;
    }

    public void setLmsFileDownloadUrl(String lmsFileDownloadUrl) {
        this.lmsFileDownloadUrl = lmsFileDownloadUrl;
    }

    public List<MemberMoConfirmedDataBean> getMoConfirmedDataBeans() {
        return moConfirmedDataBeans;
    }

    public void setMoConfirmedDataBeans(List<MemberMoConfirmedDataBean> moConfirmedDataBeans) {
        this.moConfirmedDataBeans = moConfirmedDataBeans;
    }

    public List<DrugInventoryBean> getDrugInventoryBeans() {
        return drugInventoryBeans;
    }

    public void setDrugInventoryBeans(List<DrugInventoryBean> drugInventoryBeans) {
        this.drugInventoryBeans = drugInventoryBeans;
    }

    public List<UserHealthInfraBean> getUserHealthInfrastructures() {
        return userHealthInfrastructures;
    }

    public void setUserHealthInfrastructures(List<UserHealthInfraBean> userHealthInfrastructures) {
        this.userHealthInfrastructures = userHealthInfrastructures;
    }

    public Map<String, String> getSystemConfigurations() {
        return systemConfigurations;
    }

    public void setSystemConfigurations(Map<String, String> systemConfigurations) {
        this.systemConfigurations = systemConfigurations;
    }
}
