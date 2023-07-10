/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.service.impl;

import com.argusoft.medplat.web.users.dao.UserDao;
import com.argusoft.medplat.web.users.dao.UserLocationDao;
import com.argusoft.medplat.web.users.dao.UserTokenDao;
import com.argusoft.medplat.web.users.dto.UserTokenDto;
import com.argusoft.medplat.web.users.model.UserLocation;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.web.users.service.UserService;
import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.common.util.TechoCompletableFuture;
import com.argusoft.medplat.dashboard.fhs.constants.FamilyHealthSurveyServiceConstants;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.fhs.service.FamilyHealthSurveyService;
import com.argusoft.medplat.web.location.constants.LocationConstants;
import com.argusoft.medplat.web.location.dao.LocationHierchyCloserDetailDao;
import com.argusoft.medplat.web.location.dao.LocationMasterDao;
import com.argusoft.medplat.mobile.dto.*;
import com.argusoft.medplat.mobile.mapper.FamilyDataBeanMapper;
import com.argusoft.medplat.mobile.mapper.MemberDataBeanMapper;
import com.argusoft.medplat.mobile.service.MobileAshaService;
import com.argusoft.medplat.mobile.service.MobileFhsService;
import com.argusoft.medplat.mobile.service.MobileLibraryService;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.notification.service.TechoNotificationService;
import com.argusoft.medplat.query.dto.QueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author kunjan
 */
@Service
@Transactional
public class MobileAshaServiceImpl implements MobileAshaService {

    @Autowired
    private MobileFhsService mobileFhsService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserTokenDao userTokenDao;

    @Autowired
    private FamilyHealthSurveyService familyHealthSurveyService;

    @Autowired
    private FamilyDao familyDao;

    @Autowired
    private LocationHierchyCloserDetailDao locationHierchyCloserDetailDao;

    @Autowired
    private UserLocationDao userLocationDao;

    @Autowired
    private LocationMasterDao locationMasterDao;

//    @Autowired
//    private MemberCbacDetailDao memberCbacDetailDao;

    @Autowired
    private MobileLibraryService mobileLibraryService;

    @Autowired
    private TechoNotificationMasterDao techoNotificationMasterDao;

    @Autowired
    private TechoNotificationService techoNotificationService;

    @Override
    public LoggedInUserPrincipleDto getDataForAsha(LogInRequestParamDetailDto paramDetailDto, Integer apkVersion) throws ExecutionException, InterruptedException {
        UserTokenDto userTokenDto = userTokenDao.retrieveDtoByUserToken(paramDetailDto.getToken());
        if (userTokenDto != null) {
            UserMaster user = userService.getUserByValidToken(userTokenDto.getUserToken());
            if (user != null) {
                Integer sheetVersion = null;
                if (ConstantUtil.DROP_TYPE == null) {
                    Properties props = new Properties();
                    try {
                        props.load(getClass().getResourceAsStream("/build.properties"));
                        ConstantUtil.DROP_TYPE = props.getProperty("deployType").trim();
                    } catch (IOException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
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


                CompletableFuture<Void> markNotificationAsReadCF = CompletableFuture.runAsync(()
                                -> techoNotificationService.markNotificationAsRead(paramDetailDto.getReadNotifications(), user.getId()),
                        TechoCompletableFuture.mobileGetDetailAshaThreadPool);

                LoggedInUserPrincipleDto loggedInUserPrincipleDto = new LoggedInUserPrincipleDto();

                CompletableFuture<Void> setAssignedFamiliesAshaCF = CompletableFuture.runAsync(()
                                -> this.setAssignedFamiliesForAsha(user, loggedInUserPrincipleDto, paramDetailDto),
                        TechoCompletableFuture.mobileGetDetailAshaThreadPool);

                CompletableFuture<Void> notificationsCF = CompletableFuture.runAsync(()
                                -> this.setMobileNotificationsForAsha(paramDetailDto, loggedInUserPrincipleDto, userTokenDto),
                        TechoCompletableFuture.mobileGetDetailAshaThreadPool);

//                CompletableFuture<List<MemberCbacDetailDataBean>> cbacDetailsCF = CompletableFuture.supplyAsync(()
//                                -> this.retrieveMemberCbacDetails(user.getId(), paramDetailDto.getLastUpdateDateForCbacDetails()),
//                        TechoCompletableFuture.mobileGetDetailAshaThreadPool);

                CompletableFuture<Map<Integer, List<SurveyLocationMobDataBean>>> villagesAndChildLocationsCF = CompletableFuture.supplyAsync(()
                                -> this.retrieveVillagesAndChildLocations(paramDetailDto.getUserId()),
                        TechoCompletableFuture.mobileGetDetailAshaThreadPool);

                CompletableFuture<List<LocationMasterDataBean>> locationMasterCF = CompletableFuture.supplyAsync(()
                                -> mobileFhsService.getLocationMasterDetails(paramDetailDto),
                        TechoCompletableFuture.mobileGetDetailAshaThreadPool);

                CompletableFuture<List<HealthInfrastructureBean>> healthInfraCF = CompletableFuture.supplyAsync(()
                                -> mobileFhsService.getHealthInfrastructureDetails(paramDetailDto),
                        TechoCompletableFuture.mobileGetDetailAshaThreadPool);

                CompletableFuture<List<LinkedHashMap<String, Object>>> listValuesCF = CompletableFuture.supplyAsync(()
                                -> mobileFhsService.retrieveListValues(paramDetailDto.getLastUpdateDateForListValue(), user),
                        TechoCompletableFuture.mobileGetDetailAshaThreadPool);

                CompletableFuture<QueryDto> languageLabelsCF = CompletableFuture.supplyAsync(()
                                -> mobileFhsService.retrieveLabels(paramDetailDto.getCreatedOnDateForLabel(), user.getPrefferedLanguage()),
                        TechoCompletableFuture.mobileGetDetailAshaThreadPool);

                CompletableFuture<List<AnnouncementMobDataBean>> announcementsCF = CompletableFuture.supplyAsync(()
                                -> mobileFhsService.retrieveAnnouncements(
                                user, paramDetailDto.getRoleType(), paramDetailDto.getLastUpdateOfAnnouncements(), user.getPrefferedLanguage()),
                        TechoCompletableFuture.mobileGetDetailAshaThreadPool);

                CompletableFuture<List<MobileLibraryDataBean>> mobileLibraryCF = CompletableFuture.supplyAsync(()
                                -> mobileLibraryService.retrieveMobileLibraryDataBeans(user.getRoleId(), paramDetailDto.getLastUpdateDateForLibrary()),
                        TechoCompletableFuture.mobileGetDetailAshaThreadPool);

                markNotificationAsReadCF.get();
                setAssignedFamiliesAshaCF.get();
                notificationsCF.get();
//                loggedInUserPrincipleDto.setMemberCbacDetails(cbacDetailsCF.get());
                loggedInUserPrincipleDto.setRetrievedVillageAndChildLocations(villagesAndChildLocationsCF.get());
                loggedInUserPrincipleDto.setLocationMasterBeans(locationMasterCF.get());
                loggedInUserPrincipleDto.setHealthInfrastructures(healthInfraCF.get());
                loggedInUserPrincipleDto.setRetrievedListValues(listValuesCF.get());
                loggedInUserPrincipleDto.setRetrievedLabels(languageLabelsCF.get());
                loggedInUserPrincipleDto.setRetrievedAnnouncements(announcementsCF.get());
                loggedInUserPrincipleDto.setMobileLibraryDataBeans(mobileLibraryCF.get());

                //Fetching data from cache. Not firing any query. So fetching directly
                //mobileFhsService.getXlsSheetDataForMobile(paramDetailDto.getSheetNameVersionMap(), loggedInUserPrincipleDto);
                if (paramDetailDto.getMobileFormVersion() != null) {
                    sheetVersion = paramDetailDto.getMobileFormVersion();
                }
                //Making entry in user data access request table
                mobileFhsService.userDataRequestInsertion(user.getId(), apkVersion, paramDetailDto.getImeiNumber(), sheetVersion);
                return loggedInUserPrincipleDto;
            }
        }
        return null;
    }

    private void setAssignedFamiliesForAsha(UserMaster user, LoggedInUserPrincipleDto loggedInUserPrincipleDto, LogInRequestParamDetailDto paramDetailDto) {
        if (user == null) {
            return;
        }

        Date lastUpdateDate = null;
        if (paramDetailDto.getLastUpdateDateForFamily() != null && !paramDetailDto.getLastUpdateDateForFamily().equals("0")) {
            lastUpdateDate = new Date(Long.parseLong(paramDetailDto.getLastUpdateDateForFamily()));
        }

        Map<String, List<Integer>> locationMap = this.retrieveAreaLocationByUserId(user.getId(), loggedInUserPrincipleDto, lastUpdateDate);
        List<Integer> assignedLocationIds = new LinkedList<>();

        if (locationMap.get("OLD") != null) {
            assignedLocationIds.addAll(locationMap.get("OLD"));
        }

        if (CollectionUtils.isEmpty(assignedLocationIds)) {
            loggedInUserPrincipleDto.setAssignedFamilies(null);
            loggedInUserPrincipleDto.setOrphanedAndReverificationFamilies(null);
            return;
        }

        List<String> validStates = new LinkedList<>();
        validStates.addAll(FamilyHealthSurveyServiceConstants.FHS_VERIFIED_CRITERIA_FAMILY_STATES);
        validStates.addAll(FamilyHealthSurveyServiceConstants.FHS_NEW_CRITERIA_FAMILY_STATES);
        validStates.add(FamilyHealthSurveyServiceConstants.IDSP_FAMILY_STATE_IDSP_TEMP);

        List<FamilyEntity> families = familyDao.retrieveFamiliesByAreaIds(assignedLocationIds, validStates, lastUpdateDate);
        Map<Integer, FamilyEntity> familyMapWithFamilyIdAsKey = new HashMap<>();
        Map<String, List<MemberDataBean>> membersListMapWithFamilyIdAsKey = new HashMap<>();

        for (FamilyEntity family : families) {
            familyMapWithFamilyIdAsKey.put(family.getId(), family);
        }

        for (MemberEntity member : familyHealthSurveyService.getMembersForAsha(null, null, assignedLocationIds)) {
            List<MemberDataBean> membersList = membersListMapWithFamilyIdAsKey.get(member.getFamilyId());
            if (membersList == null) {
                membersList = new ArrayList<>();
            }
            membersList.add(MemberDataBeanMapper.convertMemberEntityToMemberDataBean(member));
            membersListMapWithFamilyIdAsKey.put(member.getFamilyId(), membersList);
        }

        List<FamilyDataBean> familyDataBeans = new LinkedList<>();
        for (Map.Entry<Integer, FamilyEntity> entry : familyMapWithFamilyIdAsKey.entrySet()) {
            familyDataBeans.add(FamilyDataBeanMapper.convertFamilyEntityToFamilyDataBean(entry.getValue(), membersListMapWithFamilyIdAsKey.get(entry.getValue().getFamilyId())));
        }

        if (lastUpdateDate != null) {
            loggedInUserPrincipleDto.setUpdatedFamilyByDate(familyDataBeans);
        } else {
            loggedInUserPrincipleDto.setAssignedFamilies(familyDataBeans);
        }
    }

    private Map<String, List<Integer>> retrieveAreaLocationByUserId(Integer userId, LoggedInUserPrincipleDto loggedInUserPrincipleDto, Date lastUpdatedOn) {
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

        childLocationTypes.add(LocationConstants.LocationType.AREA);
        childLocationTypes.add(LocationConstants.LocationType.ASHA_AREA);
        if (!CollectionUtils.isEmpty(newlyAssignedUserLocations)) {
            mapOfLocations.put("NEW", locationHierchyCloserDetailDao.retrieveChildLocationIdsFromParentList(newlyAssignedUserLocations, childLocationTypes));
        }

        if (!CollectionUtils.isEmpty(alreadyAssignedUserLocations)) {
            mapOfLocations.put("OLD", locationHierchyCloserDetailDao.retrieveChildLocationIdsFromParentList(alreadyAssignedUserLocations, childLocationTypes));
        }

        if (!CollectionUtils.isEmpty(newlyRemovedUserLocations)) {
            loggedInUserPrincipleDto.setLocationsForFamilyDataDeletion(locationHierchyCloserDetailDao.retrieveChildLocationIdsFromParentList(newlyRemovedUserLocations, childLocationTypes));
        }
        return mapOfLocations;
    }

    private Map<Integer, List<SurveyLocationMobDataBean>> retrieveVillagesAndChildLocations(Integer userId) {
        if (userId != null) {
            Map<Integer, List<SurveyLocationMobDataBean>> mapOfLocationsWithParentIdAsKey = new HashMap<>();
            List<SurveyLocationMobDataBean> surveyLocationMobDataBeans =
                    new ArrayList<>(locationMasterDao.retrieveAllAreasAssignedToAshaForMobileByUserId(userId));

            for (SurveyLocationMobDataBean surveyLocationMobDataBean : surveyLocationMobDataBeans) {
                List<SurveyLocationMobDataBean> areas = mapOfLocationsWithParentIdAsKey.get(surveyLocationMobDataBean.getParent());
                if (areas == null) {
                    areas = new ArrayList<>();
                }
                areas.add(surveyLocationMobDataBean);
                mapOfLocationsWithParentIdAsKey.put(surveyLocationMobDataBean.getParent(), areas);
            }
            return mapOfLocationsWithParentIdAsKey;
        }
        return null;
    }

//    private List<MemberCbacDetailDataBean> retrieveMemberCbacDetails(Integer userId, Long lastModifiedOn) {
//        if (userId != null) {
//            Date lastUpdateDate;
//            lastUpdateDate = new Date(Objects.requireNonNullElse(lastModifiedOn, 0L));
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

    private void setMobileNotificationsForAsha(LogInRequestParamDetailDto paramDetailDto, LoggedInUserPrincipleDto loggedInUserPrincipleDto, UserTokenDto userTokenDto) {
        if (paramDetailDto.getLastUpdateDateForNotifications() != null) {
            List<TechoNotificationDataBean> toBeRemovedNotificationDataBeans = new LinkedList<>();
            List<TechoNotificationDataBean> updatedNotificationByUser;
            List<Integer> deletedNotificationIds;
            deletedNotificationIds = techoNotificationMasterDao.getDeletedNotificationsForUserByLastModifiedOn(
                    userTokenDto.getUserId(),
                    new Date(paramDetailDto.getLastUpdateDateForNotifications()));
            updatedNotificationByUser = techoNotificationMasterDao.retrieveNotificationsForUserByLastModifiedOn(
                    userTokenDto.getUserId(), userTokenDto.getRoleId(),
                    new Date(paramDetailDto.getLastUpdateDateForNotifications()));
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
            loggedInUserPrincipleDto.setNotifications(
                    techoNotificationMasterDao.retrieveAllNotificationsForUser(userTokenDto.getUserId(), userTokenDto.getRoleId()));
        }
    }
}
