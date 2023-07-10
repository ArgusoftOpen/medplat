/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.service;

import com.argusoft.medplat.fhs.dto.FhwServiceStatusDto;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.infrastructure.dto.SchoolDataBean;
import com.argusoft.medplat.mobile.dto.*;
import com.argusoft.medplat.query.dto.QueryDto;
import com.argusoft.medplat.web.users.model.UserMaster;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author prateek
 */
public interface MobileFhsService {

    Map<Integer, List<SurveyLocationMobDataBean>> retrieveVillagesAndChildLocations(Integer userId, Integer roleId);

//    List<FamilyDataBean> retrieveOrphanedAndReverificationFamiliesForFHS(UserMaster user, String villageCode);

    List<AreaAshaMappingBean> retrieveAreaAshaMapping(String locationIds);

    List<FhwServiceStatusDto> retrieveFhwServiceDetailBean(Integer userId);

    Integer saveAadharUpdateDetails(AadharUpdationBean aadharUpdationBean);

    Boolean syncMergedFamiliesInformationWithDb(String userToken, List<MergedFamiliesBean> mergedFamiliesList);

    Boolean validateUser(String userToken);

    Map<String, FamilyDataBean> getFamiliesToBeAssignedBySearchString(String token, String searchString, Boolean searchByFamilyId);

    FamilyDataBean assignFamilyToUser(String token, String locationId, String familyId);

    List<UserFormAccessBean> getUserFormAccessDetail(String token);

    void userReadyToMoveProduction(String token, String formCode, String userId);

//    @Deprecated
//    LoggedInUserPrincipleDto getDetailsArchive(LogInRequestParamDetailDto paramDetailDto, Integer apkVersion);

    LoggedInUserPrincipleDto getDetails(LogInRequestParamDetailDto paramDetailDto, Integer apkVersion) throws Exception;

    String retrieveAndroidVersion(int oldVersion);

    String retrieveFontSize(String fontType);

    Boolean isUserInProduction(String userName);

    UserMaster isUserTokenValid(String token);

    void updateMemberDetailsFromRchForms(String mobileNumber, String aadharMapString, String aadharNumber, String accountNumber, String ifsc, MemberEntity memberEntity);

//    List<RchVillageProfileDto> getRchVillageProfileDto(Integer userId);

    List<LocationMasterDataBean> getLocationMasterDetails(LogInRequestParamDetailDto paramDetailDto);

    List<LinkedHashMap<String, Object>> retrieveListValues(Long updatedOnDate, UserMaster user);
    
    List<LinkedHashMap<String, Object>> retrieveDataQualityValues(Long updatedOnDate, UserMaster user);

    List<AnnouncementMobDataBean> retrieveAnnouncements(UserMaster user, String userType, Long lastUpdateOn, String languageCode);

    QueryDto retrieveLabels(Long createdOnDate, String prefferedLanguage);

    void userDataRequestInsertion(Integer userId, Integer apkVersion, String imeiNumber, Integer sheetVersion);

    void runPatch();

//    void storeUserInstalledAppsInfo(Integer userId, String imei, List<InstalledAppsInfoBean> appsInfoBeans);

    List<HealthInfrastructureBean> getHealthInfrastructureDetails(LogInRequestParamDetailDto paramDetailDto);
    
    List<SchoolDataBean> getSchoolDetails(LogInRequestParamDetailDto paramDetailDto);

    void updateAllSchoolDetailsForMobile();

//    Map<Integer, String> getNameBasedOnAadharForAllMembersByUserId(MobileRequestParamDto mobileRequestParamDto);
    
    void setXlsSheetsAsComponentTagsInMemory();

    void getXlsFormDataForMobile(LogInRequestParamDetailDto paramDetailDto, UserMaster user, LoggedInUserPrincipleDto loggedInUserPrincipleDto);

//    void getXlsSheetDataForMobile(Map<String, Integer> sheetNameVersionMap, LoggedInUserPrincipleDto loggedInUserPrincipleDto);

    Integer saveMobileNumberUpdateDetails(MobileNumberUpdationBean mobileNumberUpdationBean);

    void checkIfMemberDeathEntryExists(Integer memberId);

    /**
     * Retrieves all mobile menu for user by it's role
     * @param paramDetailDto A request body dto for Mobile
     * @param roleId A roleId of user
     * @return fetch menu by user role
     */
    public List<MenuDataBean> getMenuDetails(LogInRequestParamDetailDto paramDetailDto, int roleId);
}
