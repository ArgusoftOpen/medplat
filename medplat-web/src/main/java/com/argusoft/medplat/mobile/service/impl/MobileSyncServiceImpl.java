package com.argusoft.medplat.mobile.service.impl;

//import com.argusoft.medplat.cfhc.dto.FamilyAvailabilityDataBean;
//import com.argusoft.medplat.cfhc.service.FamilyAvailabilityService;
//import com.argusoft.medplat.chardham.model.ChardhamTouristMasterEntity;
//import com.argusoft.medplat.chardham.service.ChardhamMemberScreeningService;
import com.argusoft.medplat.infrastructure.dto.SchoolDataBean;
import com.argusoft.medplat.mobile.service.*;
import com.argusoft.medplat.web.users.dao.RoleDao;
import com.argusoft.medplat.web.users.dao.UserDao;
import com.argusoft.medplat.web.users.dao.UserLocationDao;
import com.argusoft.medplat.web.users.dao.UserTokenDao;
import com.argusoft.medplat.web.users.dto.UserTokenDto;
import com.argusoft.medplat.common.model.SystemConfiguration;
import com.argusoft.medplat.web.users.model.UserHealthInfrastructure;
import com.argusoft.medplat.web.users.model.UserLocation;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.common.service.SystemConfigurationService;
import com.argusoft.medplat.web.users.service.UserHealthInfrastructureService;
import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.common.util.TechoCompletableFuture;
import com.argusoft.medplat.course.model.LmsUserMetaData;
import com.argusoft.medplat.course.service.CourseMasterService;
//import com.argusoft.medplat.covid.dto.CovidTravellersInfoMobileDto;
//import com.argusoft.medplat.covid.service.CovidTravellersInfoService;
import com.argusoft.medplat.dashboard.fhs.constants.FamilyHealthSurveyServiceConstants;
import com.argusoft.medplat.exception.ImtechoMobileException;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.dto.FhwServiceStatusDto;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.fhs.service.FamilyHealthSurveyService;
//import com.argusoft.medplat.infrastructure.dto.SchoolDataBean;
import com.argusoft.medplat.web.location.dao.LocationHierchyCloserDetailDao;
import com.argusoft.medplat.web.location.model.LocationTypeMaster;
import com.argusoft.medplat.web.location.service.LocationTypeService;
//import com.argusoft.medplat.migration.dto.MigratedFamilyDataBean;
//import com.argusoft.medplat.migration.dto.MigratedMembersDataBean;
//import com.argusoft.medplat.migration.service.FamilyMigrationService;
//import com.argusoft.medplat.migration.service.MigrationService;
import com.argusoft.medplat.mobile.constants.SyncConstant;
import com.argusoft.medplat.mobile.dao.MobileBeanMasterDao;
import com.argusoft.medplat.mobile.dto.*;
import com.argusoft.medplat.mobile.mapper.FamilyDataBeanMapper;
import com.argusoft.medplat.mobile.mapper.MemberDataBeanMapper;
//import com.argusoft.medplat.ncd.dao.MemberCbacDetailDao;
//import com.argusoft.medplat.ncd.dto.DrugInventoryDetailDataBean;
//import com.argusoft.medplat.ncd.dto.MemberCbacDetailDataBean;
//import com.argusoft.medplat.ncd.dto.MemberMoConfirmedDetailDataBean;
//import com.argusoft.medplat.ncd.mapper.MemberCbacDetailMapper;
//import com.argusoft.medplat.ncd.model.MemberCbacDetail;
//import com.argusoft.medplat.ncd.service.NcdDrugService;
//import com.argusoft.medplat.ncd.service.NcdService;
//import com.argusoft.medplat.ndhmmobile.healthid.service.HealthIdCommonUtilService;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.notification.service.TechoNotificationService;
import com.argusoft.medplat.query.dto.QueryDto;
import com.argusoft.medplat.query.service.QueryMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * Implements methods for MobileSyncService
 * </p>
 *
 * @author rahul
 * @since 21/05/21 4:36 PM
 */
@Service
@Transactional
public class MobileSyncServiceImpl extends GenericSessionUtilService implements MobileSyncService {

    static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
    private static final String SESSION_EXPIRED_MSG = "Your session is expired, Please login again";

    @Autowired
    private UserTokenDao userTokenDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MobileBeanMasterDao mobileBeanMasterDao;
    @Autowired
    private MobileFhsService mobileFhsService;
    @Autowired
    private MobileUtilService mobileUtilService;
    @Autowired
    private LocationTypeService locationTypeService;
    @Autowired
    private TechoNotificationService techoNotificationService;
    @Autowired
    private TechoNotificationMasterDao techoNotificationMasterDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserLocationDao userLocationDao;
    @Autowired
    private FamilyDao familyDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private LocationHierchyCloserDetailDao locationHierchyCloserDetailDao;
    @Autowired
    private QueryMasterService queryMasterService;
//    @Autowired
//    private HealthIdCommonUtilService healthIdCommonUtilService;
    @Autowired
    private FamilyHealthSurveyService familyHealthSurveyService;
    @Autowired
    private MobileLibraryService mobileLibraryService;
//    @Autowired
//    private MigrationService migrationService;
//    @Autowired
//    private FamilyMigrationService familyMigrationService;
//    @Autowired
//    private MemberCbacDetailDao memberCbacDetailDao;
//    @Autowired
//    private CovidTravellersInfoService covidTravellersInfoService;
    @Autowired
    private CourseMasterService courseMasterService;
    @Autowired
    private SystemConfigurationService systemConfigurationService;
//    @Autowired
//    private NcdService ncdService;
//    @Autowired
//    private NcdDrugService ncdDrugService;
//    @Autowired
//    private ChardhamMemberScreeningService chardhamMemberScreeningService;
    @Autowired
    private UserHealthInfrastructureService userHealthInfrastructureService;
//    @Autowired
//    private FamilyAvailabilityService familyAvailabilityService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Boolean> getMetaData(MobileRequestParamDto mobileRequestParamDto) {
        if (mobileRequestParamDto.getToken() != null) {
            UserMaster userMaster = mobileFhsService.isUserTokenValid(mobileRequestParamDto.getToken());
            if (userMaster == null) {
                throw new ImtechoMobileException(SESSION_EXPIRED_MSG, 1);
            }
            Map<String, Boolean> metadata = new HashMap<>(SyncConstant.SYSTEM_BEANS);
            for (LinkedHashMap<String, Object> row : mobileBeanMasterDao.retrieveAllBeansForUserRole(userMaster.getRoleId(), null)) {
                metadata.put(String.valueOf(row.get("bean")), Boolean.valueOf(row.get("depends_on_last_sync").toString()));
            }
            return metadata;
        } else {
            throw new ImtechoMobileException(SESSION_EXPIRED_MSG, 1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoggedInUserPrincipleDto getDetails(LogInRequestParamDetailDto paramDetailDto, Integer apkVersion) throws Exception {
        UserTokenDto userTokenDto = userTokenDao.retrieveDtoByUserToken(paramDetailDto.getToken());
        if (userTokenDto == null) {
            return null;
        }

        UserMaster user = mobileFhsService.isUserTokenValid(userTokenDto.getUserToken());
        if (user == null) {
            return null;
        }

        if (ConstantUtil.DROP_TYPE == null) {
            Properties props = new Properties();
            try {
                props.load(getClass().getResourceAsStream("/build.properties"));
                ConstantUtil.DROP_TYPE = props.getProperty("deployType").trim();
            } catch (IOException ex) {
                Logger.getLogger(MobileFhsServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        user.setTechoPhoneNumber(paramDetailDto.getPhoneNumber());
        user.setImeiNumber(paramDetailDto.getImeiNumber());
        user.setSdkVersion(paramDetailDto.getSdkVersion());
        user.setFreeSpaceMB(paramDetailDto.getFreeSpaceMB());
        if (paramDetailDto.getLatitude() != null && paramDetailDto.getLongitude() != null) {
            user.setLatitude(paramDetailDto.getLatitude());
            user.setLongitude(paramDetailDto.getLongitude());
        }
        userDao.update(user);

        List<String> beans = mobileBeanMasterDao.retrieveBeansListForUserRole(user.getRoleId(), null);

        LoggedInUserPrincipleDto loggedInUserPrincipleDto = new LoggedInUserPrincipleDto();

        // Location master
        CompletableFuture<List<LocationMasterDataBean>> locationMasterCF = CompletableFuture.supplyAsync(()
                        -> mobileFhsService.getLocationMasterDetails(paramDetailDto),
                TechoCompletableFuture.mobileGetDetailThreadPool);
        loggedInUserPrincipleDto.setLocationMasterBeans(locationMasterCF.get());

        // Health Infra structure
        CompletableFuture<List<HealthInfrastructureBean>> healthInfraCF = CompletableFuture.supplyAsync(()
                        -> mobileFhsService.getHealthInfrastructureDetails(paramDetailDto),
                TechoCompletableFuture.mobileGetDetailThreadPool);
        loggedInUserPrincipleDto.setHealthInfrastructures(healthInfraCF.get());

         //School
        CompletableFuture<List<SchoolDataBean>> schoolsCF = CompletableFuture.supplyAsync(()
                        -> mobileFhsService.getSchoolDetails(paramDetailDto),
                TechoCompletableFuture.mobileGetDetailThreadPool);
        loggedInUserPrincipleDto.setSchools(schoolsCF.get());

        // Feature
        CompletableFuture<List<String>> featuresAssignedCF = CompletableFuture.supplyAsync(()
                        -> mobileUtilService.retrieveFeaturesAssignedToTheAoi(user.getId()),
                TechoCompletableFuture.mobileGetDetailThreadPool);
        loggedInUserPrincipleDto.setFeatures(featuresAssignedCF.get());

        // Location Type
        CompletableFuture<List<LocationTypeMaster>> locationTypeMasterCF = CompletableFuture.supplyAsync(()
                        -> locationTypeService.retrieveLocationTypeMasterByModifiedOn(paramDetailDto.getLastUpdateDateForLocationTypeMaster()),
                TechoCompletableFuture.mobileGetDetailThreadPool);
        loggedInUserPrincipleDto.setLocationTypeMasters(locationTypeMasterCF.get());

        // Menu
        CompletableFuture<List<MenuDataBean>> menuCF = CompletableFuture.supplyAsync(()
                        -> mobileFhsService.getMenuDetails(paramDetailDto, user.getRoleId()),
                TechoCompletableFuture.mobileGetDetailThreadPool);
        loggedInUserPrincipleDto.setMobileMenus(menuCF.get());

        // Location
        CompletableFuture<Map<Integer, List<SurveyLocationMobDataBean>>> villagesAndChildLocationsCF =
                CompletableFuture.supplyAsync(() -> mobileFhsService.retrieveVillagesAndChildLocations(
                                paramDetailDto.getUserId(), user.getRoleId()),
                        TechoCompletableFuture.mobileGetDetailThreadPool);
        loggedInUserPrincipleDto.setRetrievedVillageAndChildLocations(villagesAndChildLocationsCF.get());

        // Label
        if (user.getPrefferedLanguage() != null) {
            CompletableFuture<QueryDto> languageLabelsCF = CompletableFuture.supplyAsync(()
                            -> mobileFhsService.retrieveLabels(paramDetailDto.getCreatedOnDateForLabel(),
                            user.getPrefferedLanguage()),
                    TechoCompletableFuture.mobileGetDetailThreadPool);
            loggedInUserPrincipleDto.setRetrievedLabels(languageLabelsCF.get());
        }

        //Xls Sheets
        CompletableFuture<Void> mobileFormsCF = CompletableFuture.runAsync(()
                        -> mobileFhsService.getXlsFormDataForMobile(paramDetailDto, user, loggedInUserPrincipleDto),
                TechoCompletableFuture.mobileGetDetailThreadPool);
        mobileFormsCF.get();

        //List Value
        CompletableFuture<List<LinkedHashMap<String, Object>>> listValuesCF = CompletableFuture.supplyAsync(()
                        -> mobileFhsService.retrieveListValues(paramDetailDto.getLastUpdateDateForListValue(), user),
                TechoCompletableFuture.mobileGetDetailThreadPool);
        loggedInUserPrincipleDto.setRetrievedListValues(listValuesCF.get());

        if (beans == null || beans.isEmpty()) {
            return loggedInUserPrincipleDto;
        }

        // Notification
        if (beans.contains(SyncConstant.NOTIFICATION_BEAN)) {
            if (paramDetailDto.getReadNotifications() != null && !paramDetailDto.getReadNotifications().isEmpty()) {
                CompletableFuture<Void> markNotificationAsReadCF = CompletableFuture.runAsync(() ->
                                techoNotificationService.markNotificationAsRead(paramDetailDto.getReadNotifications(), user.getId()),
                        TechoCompletableFuture.mobileGetDetailThreadPool);
                markNotificationAsReadCF.get();
            }

            CompletableFuture<Void> notificationsCF = CompletableFuture.runAsync(()
                            -> this.getNotificationsForMobile(loggedInUserPrincipleDto, paramDetailDto, userTokenDto, user),
                    TechoCompletableFuture.mobileGetDetailThreadPool);
            notificationsCF.get();
        }

        if (beans.contains(SyncConstant.FAMILY_BEAN)) {
            CompletableFuture<Void> getUpdatedFamiliesCF;
            CompletableFuture<List<String>> deletedFamiliesCF = null;
            if (paramDetailDto.getLastUpdateDateForFamily() != null) {
                getUpdatedFamiliesCF = CompletableFuture.runAsync(() -> this.getUpdatedFamilyDataByDate(
                        user,
                        paramDetailDto.getLastUpdateDateForFamily(),
                        loggedInUserPrincipleDto), TechoCompletableFuture.mobileGetDetailThreadPool);

                deletedFamiliesCF = CompletableFuture.supplyAsync(() -> this.retrieveDeletedFamiliesList(
                                user,
                                paramDetailDto.getLastUpdateDateForFamily()),
                        TechoCompletableFuture.mobileGetDetailThreadPool);
            } else {
                getUpdatedFamiliesCF = CompletableFuture.runAsync(() ->
                                this.retrieveAssignedFamiliesForFHS(user, loggedInUserPrincipleDto),
                        TechoCompletableFuture.mobileGetDetailThreadPool);
            }

            if (deletedFamiliesCF != null) {
                loggedInUserPrincipleDto.setDeletedFamilies(deletedFamiliesCF.get());
            }
            getUpdatedFamiliesCF.get();
        }

        if (beans.contains(SyncConstant.LIBRARY_BEAN)) {
            CompletableFuture<List<MobileLibraryDataBean>> mobileLibrararyCF = CompletableFuture.supplyAsync(()
                            -> mobileLibraryService.retrieveMobileLibraryDataBeans(user.getRoleId(),
                            paramDetailDto.getLastUpdateDateForLibrary()),
                    TechoCompletableFuture.mobileGetDetailThreadPool);

            loggedInUserPrincipleDto.setMobileLibraryDataBeans(mobileLibrararyCF.get());
        }

        if (beans.contains(SyncConstant.ANNOUNCEMENT_BEAN)) {
            CompletableFuture<List<AnnouncementMobDataBean>> announcementsCF = CompletableFuture.supplyAsync(()
                            -> mobileFhsService.retrieveAnnouncements(user, paramDetailDto.getRoleType(),
                            paramDetailDto.getLastUpdateOfAnnouncements(),
                            user.getPrefferedLanguage()),
                    TechoCompletableFuture.mobileGetDetailThreadPool);
            loggedInUserPrincipleDto.setRetrievedAnnouncements(announcementsCF.get());
        }

//        if (beans.contains(SyncConstant.DATA_QUALITY_BEAN)) {
//            CompletableFuture<List<LinkedHashMap<String, Object>>> dataQualityBeansCF = CompletableFuture.supplyAsync(()
//                            -> mobileFhsService.retrieveDataQualityValues(paramDetailDto.getLastUpdateDateForDataQualityValue(), user),
//                    TechoCompletableFuture.mobileGetDetailThreadPool);
//            loggedInUserPrincipleDto.setDataQualityBeans(dataQualityBeansCF.get());
//        }

//        if (beans.contains(SyncConstant.MIGRATED_MEMBERS_BEAN)) {
//            CompletableFuture<List<MigratedMembersDataBean>> migratedMembersCF = CompletableFuture.supplyAsync(()
//                            -> migrationService.retrieveMigrationDetailsDataBean(user.getId()),
//                    TechoCompletableFuture.mobileGetDetailThreadPool);
//            loggedInUserPrincipleDto.setMigratedMembersDataBeans(migratedMembersCF.get());
//        }
//
//        if (beans.contains(SyncConstant.MIGRATED_FAMILY_BEAN)) {
//            CompletableFuture<List<MigratedFamilyDataBean>> migratedFamilyCF = CompletableFuture.supplyAsync(()
//                            -> familyMigrationService.retrieveMigrationByLocation(user.getId()),
//                    TechoCompletableFuture.mobileGetDetailThreadPool);
//            loggedInUserPrincipleDto.setMigratedFamilyDataBeans(migratedFamilyCF.get());
//        }

        if (beans.contains(SyncConstant.FHW_SERVICE_DETAIL_BEAN) && ConstantUtil.DROP_TYPE.equals("P")) {
            CompletableFuture<List<FhwServiceStatusDto>> fhwServiceDetailDtoCF = CompletableFuture.supplyAsync(()
                    -> mobileFhsService.retrieveFhwServiceDetailBean(user.getId()), TechoCompletableFuture.mobileGetDetailThreadPool);
            loggedInUserPrincipleDto.setFhwServiceStatusDtos(fhwServiceDetailDtoCF.get());
        }

//        if (beans.contains(SyncConstant.MEMBER_CBAC_DETAIL_BEAN)) {
//            CompletableFuture<List<MemberCbacDetailDataBean>> cbacDetailsCF = CompletableFuture.supplyAsync(()
//                            -> this.retrieveMemberCbacDetails(user.getId(), paramDetailDto.getLastUpdateDateForCbacDetails()),
//                    TechoCompletableFuture.mobileGetDetailAshaThreadPool);
//            loggedInUserPrincipleDto.setMemberCbacDetails(cbacDetailsCF.get());
//        }

//        if (beans.contains(SyncConstant.COVID_TRAVELLERS_INFO_BEAN)) {
//            CompletableFuture<List<CovidTravellersInfoMobileDto>> covidTravellersInfoCF = CompletableFuture.supplyAsync(()
//                            -> covidTravellersInfoService.retrieveCovidTravellersInfoMobileDtoList(paramDetailDto.getLastUpdateDateForCovidTravellersInfo(), user),
//                    TechoCompletableFuture.mobileGetDetailThreadPool);
//            loggedInUserPrincipleDto.setCovidTravellersInfos(covidTravellersInfoCF.get());
//        }

        if (beans.contains(SyncConstant.COURSE_BEAN)) {
            CompletableFuture<List<CourseDataBean>> courseCF = CompletableFuture.supplyAsync(()
                            -> courseMasterService.getCourseDataBeanByUserId(user.getId()),
                    TechoCompletableFuture.mobileGetDetailThreadPool);
            loggedInUserPrincipleDto.setCourseDataBeans(courseCF.get());
        }

        if (beans.contains(SyncConstant.LMS_USER_METADATA_BEAN)) {
            CompletableFuture<List<LmsUserMetaData>> lmsUserMetaData = CompletableFuture.supplyAsync(()
                            -> courseMasterService.getLmsUserMetaData(user.getId()),
                    TechoCompletableFuture.mobileGetDetailThreadPool);
            loggedInUserPrincipleDto.setLmsUserMetaData(lmsUserMetaData.get());

            // LMS File Download Url
            CompletableFuture<SystemConfiguration> lmsFileDownloadUrl = CompletableFuture.supplyAsync(()
                            -> systemConfigurationService.retrieveSystemConfigurationByKey("LMS_FILE_DOWNLOAD_URL"),
                    TechoCompletableFuture.mobileGetDetailThreadPool);
            loggedInUserPrincipleDto.setLmsFileDownloadUrl(lmsFileDownloadUrl.get().getKeyValue());
        }

//        if (beans.contains(SyncConstant.CHARDHAM_TOURIST_BEAN)) {
//            CompletableFuture<List<ChardhamTouristMasterEntity>> chardhamTouristsData = CompletableFuture.supplyAsync(()
//                            -> chardhamMemberScreeningService.retrieveChardhamTourists(paramDetailDto.getLastUpdateDateForChardhamTouristMaster()),
//                    TechoCompletableFuture.mobileGetDetailThreadPool);
//            loggedInUserPrincipleDto.setChardhamTourists(chardhamTouristsData.get());
//        }

//        if (beans.contains(SyncConstant.USER_HEALTH_INFRA_BEAN)) {
//            CompletableFuture<List<UserHealthInfrastructure>> userHealthInfraBeans = CompletableFuture.supplyAsync(()
//                            -> userHealthInfrastructureService.retrieveByUserId(user.getId(), paramDetailDto.getLastUpdateDateForUserHealthInfra()),
//                    TechoCompletableFuture.mobileGetDetailThreadPool);
//            loggedInUserPrincipleDto.setUserHealthInfrastructures(userHealthInfraBeans.get());
//        }

//        if (beans.contains(SyncConstant.MO_CONFIRMED_BEAN)) {
//            CompletableFuture<List<MemberMoConfirmedDetailDataBean>> moConfirmedBean = CompletableFuture.supplyAsync(()
//                            -> this.retrieveMoConfirmedMemberDetails(user.getId()),
//                    TechoCompletableFuture.mobileGetDetailThreadPool);
//            loggedInUserPrincipleDto.setMoConfirmedDataBeans(moConfirmedBean.get());
//        }

//        if (beans.contains(SyncConstant.DRUG_INVENTORY_BEAN)) {
//            CompletableFuture<List<DrugInventoryDetailDataBean>> drugInventoryBean = CompletableFuture.supplyAsync(()
//                            -> this.retrieveDrugInventoryDetails(user.getId()),
//                    TechoCompletableFuture.mobileGetDetailThreadPool);
//            loggedInUserPrincipleDto.setDrugInventoryBeans(drugInventoryBean.get());
//        }

//        if (beans.contains(SyncConstant.FAMILY_AVAILABILITY_BEAN)) {
//            CompletableFuture<List<FamilyAvailabilityDataBean>> familyAvailabilities = CompletableFuture.supplyAsync(()
//                            -> familyAvailabilityService.getFamilyAvailabilityByModifiedOn(user.getId(), paramDetailDto.getLastUpdateDateForFamilyAvailabilities()),
//                    TechoCompletableFuture.mobileGetDetailThreadPool);
//            loggedInUserPrincipleDto.setFamilyAvailabilities(familyAvailabilities.get());
//        }


//        if (ConstantUtil.IMPLEMENTATION_TYPE.equals("uttarakhand")) {
//            Map<String, String> systemConfigMap = new HashMap<>();
//            systemConfigMap.put(ConstantUtil.SCREENING_STATUS_RED, systemConfigurationService.retrieveSystemConfigurationByKey(ConstantUtil.SCREENING_STATUS_RED).getKeyValue());
//            systemConfigMap.put(ConstantUtil.SCREENING_STATUS_YELLOW, systemConfigurationService.retrieveSystemConfigurationByKey(ConstantUtil.SCREENING_STATUS_YELLOW).getKeyValue());
//            systemConfigMap.put(ConstantUtil.SCREENING_STATUS_GREEN, systemConfigurationService.retrieveSystemConfigurationByKey(ConstantUtil.SCREENING_STATUS_GREEN).getKeyValue());
//            loggedInUserPrincipleDto.setSystemConfigurations(systemConfigMap);
//        }

        mobileFhsService.userDataRequestInsertion(user.getId(), apkVersion, paramDetailDto.getImeiNumber(), paramDetailDto.getMobileFormVersion());
        return loggedInUserPrincipleDto;
    }

    private void getNotificationsForMobile(LoggedInUserPrincipleDto loggedInUserPrincipleDto,
                                           LogInRequestParamDetailDto paramDetailDto,
                                           UserTokenDto userTokenDto,
                                           UserMaster user) {
        if (paramDetailDto.getLastUpdateDateForNotifications() != null) {
            List<TechoNotificationDataBean> toBeRemovedNotificationDataBeans = new LinkedList<>();
            List<TechoNotificationDataBean> updatedNotificationByUser;
            List<Integer> deletedNotificationIds;

            deletedNotificationIds = techoNotificationMasterDao.getDeletedNotificationsForUserByLastModifiedOn(
                    userTokenDto.getUserId(),
                    new Date(paramDetailDto.getLastUpdateDateForNotifications()));

            if (user.getRole().getName().equals("MPHW")) {
                updatedNotificationByUser = techoNotificationMasterDao.retrieveNotificationsForUserByLastModifiedOn(
                        userTokenDto.getUserId(), roleDao.retrieveByCode("FHW").getId(),
                        new Date(paramDetailDto.getLastUpdateDateForNotifications()));
            } else {
                updatedNotificationByUser = techoNotificationMasterDao.retrieveNotificationsForUserByLastModifiedOn(
                        userTokenDto.getUserId(), userTokenDto.getRoleId(),
                        new Date(paramDetailDto.getLastUpdateDateForNotifications()));
            }
            for (TechoNotificationDataBean techoNotificationDataBean : updatedNotificationByUser) {
                if (!techoNotificationDataBean.getState().equals("PENDING")
                        && !techoNotificationDataBean.getState().equals("RESCHEDULE")) {
                    deletedNotificationIds.add(techoNotificationDataBean.getId());
                    toBeRemovedNotificationDataBeans.add(techoNotificationDataBean);
                }
            }
            updatedNotificationByUser.removeAll(toBeRemovedNotificationDataBeans);
            loggedInUserPrincipleDto.setNotifications(updatedNotificationByUser);
            loggedInUserPrincipleDto.setDeletedNotifications(deletedNotificationIds);
        } else {
            if (user.getRole().getName().equals("MPHW")) {
                loggedInUserPrincipleDto.setNotifications(
                        techoNotificationMasterDao.retrieveAllNotificationsForUser(userTokenDto.getUserId(), roleDao.retrieveByCode("FHW").getId()));
            } else {
                loggedInUserPrincipleDto.setNotifications(
                        techoNotificationMasterDao.retrieveAllNotificationsForUser(userTokenDto.getUserId(), userTokenDto.getRoleId()));
            }
        }
    }

    private void retrieveAssignedFamiliesForFHS(UserMaster user, LoggedInUserPrincipleDto loggedInUserPrincipleDto) {
        if (user != null) {
            List<FamilyDataBean> familyDataBeans = new LinkedList<>();
            List<FamilyDataBean> reverificationFamilyDataBeans = new LinkedList<>();

            Map<Integer, FamilyEntity> familyMapWithFamilyIdAsKey = new HashMap<>();
            Map<String, List<MemberDataBean>> membersListMapWithFamilyIdAsKey = new HashMap<>();
            Map<String, List<Integer>> locationMap = this.retrieveVillageOrAreaLocationByUserId(user.getId(), loggedInUserPrincipleDto, null);
            List<Integer> assignedLocationIds = new LinkedList<>();
            List<String> familyIds = new LinkedList<>();

            if (locationMap.get("OLD") != null) {
                assignedLocationIds.addAll(locationMap.get("OLD"));
            }

            if (CollectionUtils.isEmpty(assignedLocationIds)) {
                loggedInUserPrincipleDto.setAssignedFamilies(null);
                loggedInUserPrincipleDto.setOrphanedAndReverificationFamilies(null);
                return;
            }

            for (FamilyEntity family : familyDao.retrieveFamiliesByLocationIdsAndState(assignedLocationIds, null)) {
                familyMapWithFamilyIdAsKey.put(family.getId(), family);
                familyIds.add(family.getFamilyId());
            }

            List<MemberEntity> members = familyHealthSurveyService.getMembers(null, familyIds, null, null);
            Set<String> familyIdsWithMembersInReverification = new HashSet<>();
            for (MemberEntity member : members) {
                List<MemberDataBean> membersList = membersListMapWithFamilyIdAsKey.get(member.getFamilyId());
                if (membersList == null) {
                    membersList = new ArrayList<>();
                }
                membersList.add(MemberDataBeanMapper.convertMemberEntityToMemberDataBean(member));
                membersListMapWithFamilyIdAsKey.put(member.getFamilyId(), membersList);

                if (FamilyHealthSurveyServiceConstants.FHS_IN_REVERIFICATION_MEMBER_STATES.contains(member.getState())) {
                    familyIdsWithMembersInReverification.add(member.getFamilyId());
                }
            }

            for (Map.Entry<Integer, FamilyEntity> entry : familyMapWithFamilyIdAsKey.entrySet()) {
                if (!FamilyHealthSurveyServiceConstants.FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES.contains(entry.getValue().getState())
                        && !familyIdsWithMembersInReverification.contains(entry.getValue().getFamilyId())) {
                    familyDataBeans.add(FamilyDataBeanMapper.convertFamilyEntityToFamilyDataBean(entry.getValue(),
                            membersListMapWithFamilyIdAsKey.get(entry.getValue().getFamilyId())));
                } else {
                    reverificationFamilyDataBeans.add(FamilyDataBeanMapper.convertFamilyEntityToFamilyDataBean(entry.getValue(),
                            membersListMapWithFamilyIdAsKey.get(entry.getValue().getFamilyId())));
                }
            }

            loggedInUserPrincipleDto.setAssignedFamilies(familyDataBeans);
            loggedInUserPrincipleDto.setOrphanedAndReverificationFamilies(reverificationFamilyDataBeans);
        }
    }

    private List<String> retrieveDeletedFamiliesList(UserMaster user, String lastUpdatedDate) {
        QueryDto queryDto = new QueryDto();
        queryDto.setCode("retrieval_deleted_families");
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("user_id", user.getId());
        parameters.put("modified_on", new SimpleDateFormat(DATE_TIME_FORMAT).format(new Date(Long.parseLong(lastUpdatedDate))));
        queryDto.setParameters(parameters);
        List<QueryDto> queryDtos = new LinkedList<>();
        queryDtos.add(queryDto);
        List<QueryDto> resultQueryDto = queryMasterService.executeQuery(queryDtos, true);
        List<LinkedHashMap<String, Object>> results = resultQueryDto.get(0).getResult();

        List<String> familyIds = new ArrayList<>();
        for (LinkedHashMap<String, Object> linkedHashMap : results) {
            familyIds.add(String.valueOf(linkedHashMap.get("family_id")));
        }
        return familyIds;
    }

    private void getUpdatedFamilyDataByDate(UserMaster user, String lastUpdatedDate, LoggedInUserPrincipleDto loggedInUserPrincipleDto) {

        if (user != null) {
            List<FamilyDataBean> familyDataBeans = new LinkedList<>();
            List<FamilyDataBean> reverificationFamilyDataBeans = new LinkedList<>();
            Date lastUpdatedOn = new Date(Long.parseLong(lastUpdatedDate));
            Map<String, FamilyEntity> familyMapWithFamilyIdAsKey = new HashMap<>();
            Map<String, List<MemberDataBean>> membersListMapWithFamilyIdAsKey = new HashMap<>();

            Map<String, List<Integer>> locationsMap = this.retrieveVillageOrAreaLocationByUserId(user.getId(), loggedInUserPrincipleDto, lastUpdatedOn);

            List<Integer> newLocationsAssignedToUser = locationsMap.get("NEW");
            List<Integer> assignedLocationsToUser = locationsMap.get("OLD");

            if (CollectionUtils.isEmpty(assignedLocationsToUser) && CollectionUtils.isEmpty(newLocationsAssignedToUser)) {
                loggedInUserPrincipleDto.setUpdatedFamilyByDate(familyDataBeans);
                loggedInUserPrincipleDto.setOrphanedAndReverificationFamilies(reverificationFamilyDataBeans);
                return;
            }

            if (!CollectionUtils.isEmpty(assignedLocationsToUser)) {
                for (FamilyEntity family : familyDao.getFamilies(null, null, assignedLocationsToUser, null, lastUpdatedOn)) {
                    familyMapWithFamilyIdAsKey.put(family.getFamilyId(), family);
                }
            }

            if (!CollectionUtils.isEmpty(newLocationsAssignedToUser)) {
                for (FamilyEntity family : familyDao.retrieveFamiliesByLocationIdsAndState(newLocationsAssignedToUser, null)) {
                    familyMapWithFamilyIdAsKey.put(family.getFamilyId(), family);
                }
            }

            if (CollectionUtils.isEmpty(familyMapWithFamilyIdAsKey.keySet())) {
                loggedInUserPrincipleDto.setUpdatedFamilyByDate(familyDataBeans);
                loggedInUserPrincipleDto.setOrphanedAndReverificationFamilies(reverificationFamilyDataBeans);
                return;
            }

            Set<String> familyIdsWithMembersInReverification = new HashSet<>();
            for (MemberEntity member : memberDao.getMembers(null, null, null, new ArrayList<>(familyMapWithFamilyIdAsKey.keySet()), null, null)) {
                List<MemberDataBean> membersList = membersListMapWithFamilyIdAsKey.get(member.getFamilyId());
                if (membersList == null) {
                    membersList = new ArrayList<>();
                }
                membersList.add(MemberDataBeanMapper.convertMemberEntityToMemberDataBean(member));
                membersListMapWithFamilyIdAsKey.put(member.getFamilyId(), membersList);

                if (FamilyHealthSurveyServiceConstants.FHS_IN_REVERIFICATION_MEMBER_STATES.contains(member.getState())) {
                    familyIdsWithMembersInReverification.add(member.getFamilyId());
                }
            }

            for (Map.Entry<String, FamilyEntity> entry : familyMapWithFamilyIdAsKey.entrySet()) {
                if (!FamilyHealthSurveyServiceConstants.FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES.contains(entry.getValue().getState())
                        && !familyIdsWithMembersInReverification.contains(entry.getValue().getFamilyId())) {
                    familyDataBeans.add(FamilyDataBeanMapper.convertFamilyEntityToFamilyDataBean(entry.getValue(), membersListMapWithFamilyIdAsKey.get(entry.getValue().getFamilyId())));
                } else {
                    reverificationFamilyDataBeans.add(
                            FamilyDataBeanMapper.convertFamilyEntityToFamilyDataBean(entry.getValue(), membersListMapWithFamilyIdAsKey.get(entry.getValue().getFamilyId()))
                    );
                }
            }

            loggedInUserPrincipleDto.setUpdatedFamilyByDate(familyDataBeans);
            loggedInUserPrincipleDto.setOrphanedAndReverificationFamilies(reverificationFamilyDataBeans);
        } else {
            loggedInUserPrincipleDto.setUpdatedFamilyByDate(new ArrayList<>());
        }
    }

    private Map<String, List<Integer>> retrieveVillageOrAreaLocationByUserId(Integer userId, LoggedInUserPrincipleDto loggedInUserPrincipleDto, Date lastUpdatedOn) {
        Map<String, List<Integer>> mapOfLocations = new HashMap<>();
        List<UserLocation> userLocations = userLocationDao.retrieveAllLocationsByUserId(userId);
        List<Integer> alreadyAssignedUserLocations = new LinkedList<>();
        List<Integer> newlyAssignedUserLocations = new LinkedList<>();
        List<Integer> newlyRemovedUserLocations = new LinkedList<>();
        List<String> childLocationTypes = new LinkedList<>();

        if (lastUpdatedOn != null) {
            for (UserLocation userLocation : userLocations) {
                if (userLocation.getModifiedOn().after(lastUpdatedOn) && userLocation.getState().equals(UserLocation.State.ACTIVE)) {
                    newlyAssignedUserLocations.add(userLocation.getLocationId());
                } else if (userLocation.getModifiedOn().after(lastUpdatedOn) && userLocation.getState().equals(UserLocation.State.INACTIVE)) {
                    newlyRemovedUserLocations.add(userLocation.getLocationId());
                } else if (userLocation.getState().equals(UserLocation.State.ACTIVE)) {
                    alreadyAssignedUserLocations.add(userLocation.getLocationId());
                }
            }
        } else {
            for (UserLocation userLocation : userLocations) {
                if (userLocation.getState().equals(UserLocation.State.ACTIVE)) {
                    alreadyAssignedUserLocations.add(userLocation.getLocationId());
                }
            }
        }

        if (!CollectionUtils.isEmpty(newlyAssignedUserLocations)) {
            mapOfLocations.put("NEW", locationHierchyCloserDetailDao.retrieveChildLocationIdsFromParentList(newlyAssignedUserLocations, childLocationTypes));
        } else if (!CollectionUtils.isEmpty(alreadyAssignedUserLocations)) {
            mapOfLocations.put("OLD", locationHierchyCloserDetailDao.retrieveChildLocationIdsFromParentList(alreadyAssignedUserLocations, childLocationTypes));
        } else if (!CollectionUtils.isEmpty(newlyRemovedUserLocations)) {
            loggedInUserPrincipleDto.setLocationsForFamilyDataDeletion(
                    locationHierchyCloserDetailDao.retrieveChildLocationIdsFromParentList(newlyRemovedUserLocations, childLocationTypes));
        }
        return mapOfLocations;
    }

//    private List<MemberCbacDetailDataBean> retrieveMemberCbacDetails(Integer userId, Long lastModifiedOn) {
//        if (userId != null) {
//            Date lastUpdateDate;
//            lastUpdateDate = new Date(ImtechoUtil.getValueOrDefault(lastModifiedOn, 0L));
//
//            List<MemberCbacDetailDataBean> memberCbacDetailDataBeans = new ArrayList<>();
//            List<MemberCbacDetail> cbacDetails = memberCbacDetailDao.retrieveCbacDetailsForAsha(userId, lastUpdateDate);
//            if (cbacDetails != null) {
//                for (MemberCbacDetail memberCbacDetail : cbacDetails) {
//                    memberCbacDetailDataBeans.add(MemberCbacDetailMapper.convertMemberCbacDetailEntityToDataBean(memberCbacDetail));
//                }
//            }
//            return memberCbacDetailDataBeans;
//        }
//        return new ArrayList<>();
//    }

//    private List<MemberMoConfirmedDetailDataBean> retrieveMoConfirmedMemberDetails(Integer userId) {
//        if (userId != null) {
//            List<MemberMoConfirmedDetailDataBean> moConfirmedBeanList = new ArrayList<>();
//            List<MemberMoConfirmedDetailDataBean> beanList = memberDao.retrieveMoConfirmedMembers(userId);
//            if (beanList != null && !beanList.isEmpty()) {
//                for (MemberMoConfirmedDetailDataBean bean : beanList) {
//                    moConfirmedBeanList.add(ncdService.retrieveMoConfirmedDetail(bean));
//                }
//            }
//            return moConfirmedBeanList;
//        }
//        return new ArrayList<>();
//    }

//    private List<DrugInventoryDetailDataBean> retrieveDrugInventoryDetails(Integer userId) {
//        if (userId != null) {
//            return ncdDrugService.retrieveDrugInventoryDetails(userId);
//        }
//        return new ArrayList<>();
//    }

}
