/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.service.impl;

import com.argusoft.medplat.common.dao.SequenceDao;
import com.argusoft.medplat.common.dao.SystemConfigurationDao;
import com.argusoft.medplat.common.model.SystemConfiguration;
import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.common.util.SystemConstantUtil;
import com.argusoft.medplat.common.util.TechoCompletableFuture;
import com.argusoft.medplat.dashboard.fhs.constants.FamilyHealthSurveyServiceConstants;
import com.argusoft.medplat.exception.ImtechoMobileException;
import com.argusoft.medplat.fhs.dao.AnganwadiDao;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.dto.FhwServiceStatusDto;
import com.argusoft.medplat.fhs.dto.MemberAdditionalInfo;
import com.argusoft.medplat.fhs.model.Anganwadi;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.fhs.service.FamilyHealthSurveyService;
import com.argusoft.medplat.infrastructure.dao.SchoolMasterDao;
import com.argusoft.medplat.infrastructure.dto.SchoolDataBean;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.mobile.dao.MobileFormDao;
import com.argusoft.medplat.mobile.dao.MobileMenuMasterDao;
import com.argusoft.medplat.mobile.dto.*;
import com.argusoft.medplat.mobile.mapper.FamilyDataBeanMapper;
import com.argusoft.medplat.mobile.mapper.MemberDataBeanMapper;
import com.argusoft.medplat.mobile.model.SyncStatus;
import com.argusoft.medplat.mobile.service.GenericSessionUtilService;
import com.argusoft.medplat.mobile.service.MobileFhsService;
import com.argusoft.medplat.mobile.service.MobileUtilService;
import com.argusoft.medplat.mobile.service.MoveToProductionService;
import com.argusoft.medplat.mobile.util.XlsToDtoConversion;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.notification.service.TechoNotificationService;
import com.argusoft.medplat.query.dto.QueryDto;
import com.argusoft.medplat.query.service.QueryMasterService;
import com.argusoft.medplat.rch.service.MemberPregnancyStatusService;
import com.argusoft.medplat.systemconstraint.mapper.SystemConstraintComponentTagDtoMapper;
import com.argusoft.medplat.systemconstraint.service.SystemConstraintService;
import com.argusoft.medplat.web.healthinfra.dao.HealthInfrastructureDetailsDao;
import com.argusoft.medplat.web.location.constants.LocationConstants;
import com.argusoft.medplat.web.location.dao.LocationHierchyCloserDetailDao;
import com.argusoft.medplat.web.location.dao.LocationMasterDao;
import com.argusoft.medplat.web.location.model.LocationMaster;
import com.argusoft.medplat.web.location.model.LocationTypeMaster;
import com.argusoft.medplat.web.location.service.LocationTypeService;
import com.argusoft.medplat.web.users.dao.*;
import com.argusoft.medplat.web.users.dto.UserTokenDto;
import com.argusoft.medplat.web.users.model.UserLocation;
import com.argusoft.medplat.web.users.model.UserLoginDetailMaster;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.web.users.model.UserToken;
import com.argusoft.medplat.web.users.service.UserService;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.argusoft.medplat.rch.constants.RchConstants.SECOND;
import static java.util.Calendar.*;

/**
 * @author prateek
 */
@Service
@Transactional
public class MobileFhsServiceImpl extends GenericSessionUtilService implements MobileFhsService {

    public static final ExecutorService  mobileGetDetailThreadPool = Executors.newFixedThreadPool(25);

    static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.S";

    @Autowired
    private UserTokenDao userTokenDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private FamilyDao familyDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private FamilyHealthSurveyService familyHealthSurveyService;
    @Autowired
    private UserLocationDao userLocationDao;
    @Autowired
    private LocationMasterDao locationMasterDao;
    @Autowired
    private AnganwadiDao anganwadiDao;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleDao roleDao;

    @Autowired
    private LocationHierchyCloserDetailDao locationHierchyCloserDetailDao;
    @Autowired
    private MoveToProductionService moveToProductionService;
    @Autowired
    private TechoNotificationService techoNotificationService;
    @Autowired
    private TechoNotificationMasterDao techoNotificationMasterDao;
    @Autowired
    private QueryMasterService queryMasterService;
    @Autowired
    private XlsToDtoConversion xlsToDtoConversion;
    @Autowired
    private SystemConfigurationDao systemConfigurationDao;
    @Autowired
    private SequenceDao sequenceDao;
    @Autowired
    private MobileUtilService mobileUtilService;
    @Autowired
    private UserLoginDetailMasterDao userLoginDetailMasterDao;
//    @Autowired
//    private UserInstalledAppsMasterDao userInstalledAppsMasterDao;
//    @Autowired
//    private MobileLibraryService mobileLibraryService;
//    @Autowired
//    private MigrationService migrationService;
    @Autowired
    private HealthInfrastructureDetailsDao healthInfrastructureDao;
    @Autowired
    private SchoolMasterDao schoolMasterDao;
    @Autowired
    private MemberPregnancyStatusService memberPregnancyStatusService;
//    @Autowired
//    private CovidTravellersInfoService covidTravellersInfoService;
//    @Autowired
//    private MobileFormMasterService mobileFormMasterService;
//    @Autowired
//    private FamilyMigrationService familyMigrationService;
    @Autowired
    private MobileFormDao mobileFormDao;
    @Autowired
    private SystemConstraintService systemConstraintService;

//    @Autowired
//    @Qualifier(value = "allLocations")
//    private TenantCacheProvider<List<LocationMasterDataBean>> tenantCacheProviderForAllLocations;
//
//    @Autowired
//    @Qualifier(value = "allHealthInfrastructures")
//    private TenantCacheProvider<List<HealthInfrastructureBean>> tenantCacheProviderForAllHealthInfrastructures;
//    @Autowired
//    @Qualifier(value = "xlsDataFhw")
//    private TenantCacheProvider<Map<String, List<ComponentTagDto>>> tenantCacheProviderForXlsDataFhw;
//    @Autowired
//    @Qualifier(value = "xlsDataAsha")
//    private TenantCacheProvider<Map<String, List<ComponentTagDto>>> tenantCacheProviderForXlsDataAsha;
//    @Autowired
//    @Qualifier(value = "xlsDataAww")
//    private TenantCacheProvider<Map<String, List<ComponentTagDto>>> tenantCacheProviderForXlsDataAww;
//    @Autowired
//    @Qualifier(value = "xlsAllData")
//    private TenantCacheProvider<Map<String, List<ComponentTagDto>>> tenantCacheProviderForXlsAllData;
//    @Autowired
//    @Qualifier(value = "allSchools")
//    private TenantCacheProvider<List<SchoolDataBean>> tenantCacheProviderForAllSchools;
    @Autowired
    private MobileMenuMasterDao mobileMenuMasterDao;
    @Autowired
    private LocationTypeService locationTypeService;

    private void retrieveAssignedFamiliesForFHS(UserMaster user, LoggedInUserPrincipleDto loggedInUserPrincipleDto) {
        if (user != null) {
            List<FamilyDataBean> familyDataBeans = new LinkedList<>();
            List<FamilyDataBean> reverificationFamilyDataBeans = new LinkedList<>();

            Map<Integer, FamilyEntity> familyMapWithFamilyIdAsKey = new HashMap<>();
            List<String> familyIds = new ArrayList<>();
            Map<String, List<MemberDataBean>> membersListMapWithFamilyIdAsKey = new HashMap<>();
            Map<String, List<Integer>> locationMap = this.retrieveVillageOrAreaLocationByUserId(user.getId(), loggedInUserPrincipleDto, null);
            List<Integer> assignedLocationIds = new LinkedList<>();

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

    @Override
    public Map<Integer, List<SurveyLocationMobDataBean>> retrieveVillagesAndChildLocations(Integer userId, Integer roleId) {
        if (userId != null) {
            Map<Integer, List<SurveyLocationMobDataBean>> mapOfLocationsWithParentIdAsKey = new HashMap<>();
            List<SurveyLocationMobDataBean> surveyLocationMobDataBeans = new ArrayList<>();
            surveyLocationMobDataBeans.addAll(locationMasterDao.retrieveAllLocationForMobileByUserId(userId, roleId));
            surveyLocationMobDataBeans.addAll(anganwadiDao.retrieveAllAnganwadiForMobileByUserId(userId));

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

    private QueryDto retrieveCoordinatesForAssignedLocations(Integer userId) {
        QueryDto queryDto = new QueryDto();
        queryDto.setCode("retrieve_coordinates_assigned_location");
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("userId", userId);
        queryDto.setParameters(parameters);
        List<QueryDto> queryDtos = new LinkedList<>();
        queryDtos.add(queryDto);
        List<QueryDto> resultQueryDto = queryMasterService.executeQuery(queryDtos, true);
        if (resultQueryDto != null && !resultQueryDto.isEmpty()) {
            return (resultQueryDto.get(0));
        } else {
            return null;
        }
    }

//    @Override
//    public List<FamilyDataBean> retrieveOrphanedAndReverificationFamiliesForFHS(UserMaster user, String villageCode) {
//        if (user != null) {
//            List<FamilyDataBean> dataPool;
//            try {
//                List<Integer> assignedLocations = userLocationDao.retrieveLocationIdsByUserId(user.getId());
//                List<Integer> locations = new LinkedList<>();
//                for (Integer id : assignedLocations) {
//                    List<Integer> retrieveChildLocationIds = locationHierchyCloserDetailDao.retrieveChildLocationIds(id);
//                    locations.addAll(retrieveChildLocationIds);
//                }
//
//                List<FamilyDataBean> familyDataBeans = new ArrayList<>();
//                for (FamilyEntity family : familyHealthSurveyService.getOrphanedOrReverificationFamiliesAssignedToUser(locations)) {
//                    List<MemberDataBean> memberDataBeans = new ArrayList<>();
//                    List<MemberEntity> membersByFamilyId = memberDao.retrieveMemberEntitiesByFamilyId(family.getFamilyId());
//                    for (MemberEntity member : membersByFamilyId) {
//                        memberDataBeans.add(MemberDataBeanMapper.convertMemberEntityToMemberDataBean(member));
//                    }
//                    familyDataBeans.add(FamilyDataBeanMapper.convertFamilyEntityToFamilyDataBean(family, memberDataBeans));
//                    if (family.getState().equals(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_ORPHAN)) {
//                        family.setModifiedBy(user.getId());
//                        family.setModifiedOn(new Date());
//                        familyHealthSurveyService.updateFamily(family, family.getState(), FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_UNVERIFIED);
//                        for (MemberEntity memberEntity : membersByFamilyId) {
//                            memberEntity.setModifiedBy(user.getId());
//                            memberEntity.setModifiedOn(new Date());
//                            familyHealthSurveyService.updateMember(memberEntity, memberEntity.getState(), FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_UNVERIFIED);
//                        }
//                    }
//                }
//                dataPool = familyDataBeans;
//            } catch (ImtechoUserException e) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
//                return new ArrayList<>();
//            }
//            return dataPool;
//        }
//        return new ArrayList<>();
//    }

    @Override
    public List<AreaAshaMappingBean> retrieveAreaAshaMapping(String locationIds) {
        if (locationIds == null || locationIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<AreaAshaMappingBean> areaAshaMappingList = new ArrayList<>();
        List<String> locationIdStrings = Arrays.asList(locationIds.split(":"));
        List<Integer> longLocationIds = new ArrayList<>();
        if (locationIdStrings.isEmpty()) {
            return new ArrayList<>();
        }

        for (String locationIdString : locationIdStrings) {
            longLocationIds.add(Integer.valueOf(locationIdString));
        }

        List<Integer> areas = locationMasterDao.getAllLocationIdsByParentList(longLocationIds);

        if (areas != null && !areas.isEmpty()) {
            List<UserLocation> userLocations = userLocationDao.getUserLocationByLocationId(areas);
            if (!CollectionUtils.isEmpty(userLocations)) {
                for (UserLocation userLocation : userLocations) {
                    UserMaster user = userDao.retrieveById(userLocation.getUserId());
                    AreaAshaMappingBean areaAshaMappingBean = new AreaAshaMappingBean();
                    areaAshaMappingBean.setAreaId(userLocation.getLocationMaster().getId());
                    areaAshaMappingBean.setAshaName(user.getFirstName() + " " + user.getLastName());
                    areaAshaMappingList.add(areaAshaMappingBean);
                }
            }
            return areaAshaMappingList;
        }
        return new ArrayList<>();
    }

    @Override
    public List<FhwServiceStatusDto> retrieveFhwServiceDetailBean(Integer userId) {
        List<FhwServiceStatusDto> fhwServiceStatusDtos = new ArrayList<>();
        if (userId != null) {
            fhwServiceStatusDtos = familyDao.getFhwServiceReportByUserId(userId);
            List<FhwServiceStatusDto> fhwServiceStatusDtosForNcd = familyDao.getFhwServiceReportForNcdByUserId(userId);
            Map<Integer, FhwServiceStatusDto> fhwServiceStatusDtoMapWithLocationIdAsKey = new HashMap<>();
            fhwServiceStatusDtosForNcd.forEach(fhwServiceStatusDto
                    -> fhwServiceStatusDtoMapWithLocationIdAsKey.put(fhwServiceStatusDto.getLocationId(), fhwServiceStatusDto));

            for (FhwServiceStatusDto fhwServiceStatusDto : fhwServiceStatusDtos) {

                FhwServiceStatusDto get = fhwServiceStatusDtoMapWithLocationIdAsKey.get(fhwServiceStatusDto.getLocationId());
                if (get != null) {
                    fhwServiceStatusDto.setNcdTotalMembersScreened(get.getNcdTotalMembersScreened());
                    fhwServiceStatusDto.setNcdTotalMaleMembersScreened(get.getNcdTotalMaleMembersScreened());
                    fhwServiceStatusDto.setNcdTotalFemaleMembersScreened(get.getNcdTotalFemaleMembersScreened());
                    fhwServiceStatusDto.setNcdTotalMembersScreenedForHypertension(get.getNcdTotalMembersScreenedForHypertension());
                    fhwServiceStatusDto.setNcdTotalMembersScreenedForDiabetes(get.getNcdTotalMembersScreenedForDiabetes());
                    fhwServiceStatusDto.setNcdTotalMembersScreenedForOral(get.getNcdTotalMembersScreenedForOral());
                    fhwServiceStatusDto.setNcdTotalFemaleMembersScreenedForBreast(get.getNcdTotalFemaleMembersScreenedForBreast());
                    fhwServiceStatusDto.setNcdTotalFemaleMembersScreenedForCervical(get.getNcdTotalFemaleMembersScreenedForCervical());
                }
                if (fhwServiceStatusDto.getFamiliesImportedFromEMamta() == null) {
                    fhwServiceStatusDto.setFamiliesImportedFromEMamta(0);
                }

                if (fhwServiceStatusDto.getFamiliesImportedFromEMamtaExpectedValue() == null) {
                    fhwServiceStatusDto.setFamiliesImportedFromEMamtaExpectedValue(0);
                }

                if (fhwServiceStatusDto.getFamiliesArchivedTillNow() == null) {
                    fhwServiceStatusDto.setFamiliesArchivedTillNow(0);
                }

                if (fhwServiceStatusDto.getFamiliesArchivedTillNowExpectedValue() == null) {
                    fhwServiceStatusDto.setFamiliesArchivedTillNowExpectedValue(0);
                }

                if (fhwServiceStatusDto.getFamiliesVerifiedLast3Days() == null) {
                    fhwServiceStatusDto.setFamiliesVerifiedLast3Days(0);
                }

                if (fhwServiceStatusDto.getFamiliesVerifiedLast3DaysExpectedValue() == null) {
                    fhwServiceStatusDto.setFamiliesVerifiedLast3DaysExpectedValue(0);
                }

                if (fhwServiceStatusDto.getNewFamiliesAddedTillNow() == null) {
                    fhwServiceStatusDto.setNewFamiliesAddedTillNow(0);
                }

                if (fhwServiceStatusDto.getNewFamiliesAddedTillNowExpectedValue() == null) {
                    fhwServiceStatusDto.setNewFamiliesAddedTillNowExpectedValue(0);
                }

                if (fhwServiceStatusDto.getTotalFamiliesInIMTTillNow() == null) {
                    fhwServiceStatusDto.setTotalFamiliesInIMTTillNow(0);
                }

                if (fhwServiceStatusDto.getTotalFamiliesInIMTTillNowExpectedValue() == null) {
                    fhwServiceStatusDto.setTotalFamiliesInIMTTillNowExpectedValue(0);
                }

                if (fhwServiceStatusDto.getTotalNumberOfSeasonalMigrantFamilies() == null) {
                    fhwServiceStatusDto.setTotalNumberOfSeasonalMigrantFamilies(0);
                }

                if (fhwServiceStatusDto.getTotalNumberOfSeasonalMigrantFamiliesExpectedValue() == null) {
                    fhwServiceStatusDto.setTotalNumberOfSeasonalMigrantFamiliesExpectedValue(0);
                }

                if (fhwServiceStatusDto.getTotalMembersInIMTTillNow() == null) {
                    fhwServiceStatusDto.setTotalMembersInIMTTillNow(0);
                }

                if (fhwServiceStatusDto.getTotalMembersInIMTTillNowExpectedValue() == null) {
                    fhwServiceStatusDto.setTotalMembersInIMTTillNowExpectedValue(0);
                }

                if (fhwServiceStatusDto.getTotalEligibleCouplesInTeCHO() == null) {
                    fhwServiceStatusDto.setTotalEligibleCouplesInTeCHO(0);
                }

                if (fhwServiceStatusDto.getTotalEligibleCouplesInTeCHOExpectedValue() == null) {
                    fhwServiceStatusDto.setTotalEligibleCouplesInTeCHOExpectedValue(0);
                }

                if (fhwServiceStatusDto.getTotalPregnantWomenInTeCHO() == null) {
                    fhwServiceStatusDto.setTotalPregnantWomenInTeCHO(0);
                }

                if (fhwServiceStatusDto.getTotalPregnantWomenInTeCHOExpectedValue() == null) {
                    fhwServiceStatusDto.setTotalPregnantWomenInTeCHOExpectedValue(0);
                }

                if (fhwServiceStatusDto.getNumberOfMembersWithAadharNumberEntered() == null) {
                    fhwServiceStatusDto.setNumberOfMembersWithAadharNumberEntered(0);
                }

                if (fhwServiceStatusDto.getNumberOfMembersWithAadharNumberEnteredExpectedValue() == null) {
                    fhwServiceStatusDto.setNumberOfMembersWithAadharNumberEnteredExpectedValue(0);
                }

                if (fhwServiceStatusDto.getUnder5ChildrenTillNow() == null) {
                    fhwServiceStatusDto.setUnder5ChildrenTillNow(0);
                }

                if (fhwServiceStatusDto.getUnder5ChildrenTillNowExpectedValue() == null) {
                    fhwServiceStatusDto.setUnder5ChildrenTillNowExpectedValue(0);
                }

                if (fhwServiceStatusDto.getNumberOfMembersWithMobileNumberEntered() == null) {
                    fhwServiceStatusDto.setNumberOfMembersWithMobileNumberEntered(0);
                }

                if (fhwServiceStatusDto.getNumberOfMembersWithMobileNumberEnteredExpectedValue() == null) {
                    fhwServiceStatusDto.setNumberOfMembersWithMobileNumberEnteredExpectedValue(0);
                }

                if (fhwServiceStatusDto.getNumberOfTrueValidationsByGvkEmri() == null) {
                    fhwServiceStatusDto.setNumberOfTrueValidationsByGvkEmri(0);
                }

                if (fhwServiceStatusDto.getNumberOfTrueValidationsByGvkEmriExpectedValue() == null) {
                    fhwServiceStatusDto.setNumberOfTrueValidationsByGvkEmriExpectedValue(0);
                }

                if (fhwServiceStatusDto.getFamiliesVerifiedTillNow() == null) {
                    fhwServiceStatusDto.setFamiliesVerifiedTillNow(0);
                }

                if (fhwServiceStatusDto.getFamiliesVerifiedTillNowExpectedValue() == null) {
                    fhwServiceStatusDto.setFamiliesVerifiedTillNowExpectedValue(0);
                }

                if (fhwServiceStatusDto.getNcdTotalMembersScreened() == null) {
                    fhwServiceStatusDto.setNcdTotalMembersScreened(0);
                }

                if (fhwServiceStatusDto.getNcdTotalMaleMembersScreened() == null) {
                    fhwServiceStatusDto.setNcdTotalMaleMembersScreened(0);
                }

                if (fhwServiceStatusDto.getNcdTotalFemaleMembersScreened() == null) {
                    fhwServiceStatusDto.setNcdTotalFemaleMembersScreened(0);
                }

                if (fhwServiceStatusDto.getNcdTotalMembersScreenedForHypertension() == null) {
                    fhwServiceStatusDto.setNcdTotalMembersScreenedForHypertension(0);
                }

                if (fhwServiceStatusDto.getNcdTotalMembersScreenedForDiabetes() == null) {
                    fhwServiceStatusDto.setNcdTotalMembersScreenedForDiabetes(0);
                }

                if (fhwServiceStatusDto.getNcdTotalMembersScreenedForOral() == null) {
                    fhwServiceStatusDto.setNcdTotalMembersScreenedForOral(0);
                }

                if (fhwServiceStatusDto.getNcdTotalFemaleMembersScreenedForBreast() == null) {
                    fhwServiceStatusDto.setNcdTotalFemaleMembersScreenedForBreast(0);
                }

                if (fhwServiceStatusDto.getNcdTotalFemaleMembersScreenedForCervical() == null) {
                    fhwServiceStatusDto.setNcdTotalFemaleMembersScreenedForCervical(0);
                }
            }
        }
        return fhwServiceStatusDtos;
    }

//    @Override
//    public Integer saveAadharUpdateDetails(AadharUpdationBean aadharUpdationBean) {
//        boolean toBeUpdated = false;
//        MemberEntity memberByUniqueHealthIdAndFamilyId = memberDao.getMemberByUniqueHealthIdAndFamilyId(aadharUpdationBean.getMemberId(), null);
//
//        if (memberByUniqueHealthIdAndFamilyId == null) {
//            Logger.getLogger(getClass().getName()).log(Level.SEVERE,
//                    "Member Not Found For Member ID:" + aadharUpdationBean.getMemberId() + " and FamilyID:" + aadharUpdationBean.getFamilyId());
//            return null;
//        }
//
//        if (aadharUpdationBean.getAadharNumber() != null) {
//            if (memberByUniqueHealthIdAndFamilyId.getNameAsPerAadhar() == null || memberByUniqueHealthIdAndFamilyId.getNameAsPerAadhar().trim().isEmpty()) {
//                memberByUniqueHealthIdAndFamilyId.setAadharNumber(aadharUpdationBean.getAadharNumber());
//                memberByUniqueHealthIdAndFamilyId.setAadharNumberAvailable(Boolean.TRUE);
//                if (aadharUpdationBean.getNameBasedOnAadhar() != null && !aadharUpdationBean.getNameBasedOnAadhar().isEmpty()) {
//                    memberByUniqueHealthIdAndFamilyId.setNameAsPerAadhar(aadharUpdationBean.getNameBasedOnAadhar());
//                }
//                toBeUpdated = true;
//            }
//        }
//
//        if (aadharUpdationBean.getMobileNumber() != null && !aadharUpdationBean.getMobileNumber().isEmpty()) {
//            memberByUniqueHealthIdAndFamilyId.setMobileNumber(aadharUpdationBean.getMobileNumber());
//            toBeUpdated = true;
//        }
//
//        if (aadharUpdationBean.getDob() != null) {
//            memberByUniqueHealthIdAndFamilyId.setDob(aadharUpdationBean.getDob());
//            toBeUpdated = true;
//        }
//
//        if (toBeUpdated) {
//            familyHealthSurveyService.updateMember(memberByUniqueHealthIdAndFamilyId, null, null);
//        }
//        return memberByUniqueHealthIdAndFamilyId.getId();
//    }


    public void getUpdatedFamilyDataByDate(UserMaster user, String lastUpdatedDate, LoggedInUserPrincipleDto loggedInUserPrincipleDto) {

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
                    FamilyDataBean familyDataBean = FamilyDataBeanMapper.convertFamilyEntityToFamilyDataBean(entry.getValue(), membersListMapWithFamilyIdAsKey.get(entry.getValue().getFamilyId()));
                    reverificationFamilyDataBeans.add(familyDataBean);
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

//        childLocationTypes.add(LocationConstants.LocationType.VILLAGE);
//        childLocationTypes.add(LocationConstants.LocationType.ANGANWADI_AREA);

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

    @Override
    public UserMaster isUserTokenValid(String token) {
        UserToken userToken = userTokenDao.retrieveByUserToken(token);
        UserMaster user = null;
        if (userToken != null) {
            user = userDao.retrieveById(userToken.getUserId());
        }
        return user;
    }

    @Override
    public Boolean syncMergedFamiliesInformationWithDb(String token, List<MergedFamiliesBean> mergedFamiliesList) {
        try {
            UserToken userToken = userTokenDao.retrieveByUserToken(token);
            if (userToken != null) {
                for (MergedFamiliesBean mergedFamiliesBean : mergedFamiliesList) {
                    FamilyEntity familyMerged = familyDao.retrieveFamilyByFamilyId(mergedFamiliesBean.getMergedFamilyId());
                    familyMerged.setMergedWith(mergedFamiliesBean.getExpandedFamilyId());
                    familyMerged.setState(FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_MERGED);
                    familyHealthSurveyService.updateFamily(familyMerged, familyMerged.getState(), FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_MERGED);
                    List<MemberEntity> memberEntities = memberDao.retrieveMemberEntitiesByFamilyId(mergedFamiliesBean.getMergedFamilyId());
                    for (MemberEntity memberEntity : memberEntities) {
                        memberEntity.setMergedFromFamilyId(memberEntity.getFamilyId());
                        memberEntity.setFamilyId(mergedFamiliesBean.getExpandedFamilyId());
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    @Override
    public Boolean validateUser(String token) {
        UserToken userToken = userTokenDao.retrieveByUserToken(token);
        if (userToken != null) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public Map<String, FamilyDataBean> getFamiliesToBeAssignedBySearchString(String token, String searchString, Boolean searchByFamilyId) {
        UserToken userToken = userTokenDao.retrieveByUserToken(token);
        if (userToken != null) {
            if (searchByFamilyId != null && searchByFamilyId) {
                return this.getFamilyToBeAssignedByFamilyId(userToken.getUserId(), searchString);
            } else {
                String familyId = memberDao.getFamilyIdByMemberUniqueHealthId(searchString);
                return this.getFamilyToBeAssignedByFamilyId(userToken.getUserId(), familyId);
            }
        }
        return null;
    }

    private Map<String, FamilyDataBean> getFamilyToBeAssignedByFamilyId(Integer userId, String familyId) {
        Map<String, FamilyDataBean> map = new HashMap<>();
        FamilyEntity familyEntity;
        try {
            familyEntity = familyDao.retrieveFamilyByFamilyId(familyId);
        } catch (Exception e) {
            map.put("Family not found", null);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            return map;
        }

        if (familyEntity == null) {
            map.put("Family not found", null);
            return map;
        }

        if (familyEntity.getLocationId().equals(-1)
                || FamilyHealthSurveyServiceConstants.FHS_ARCHIVED_CRITERIA_FAMILY_STATES.contains(familyEntity.getState())) {
            List<MemberEntity> memberEntitys = memberDao.retrieveMemberEntitiesByFamilyId(familyEntity.getFamilyId());
            List<MemberDataBean> memberDataBeans = new ArrayList<>();
            memberEntitys.forEach(memberEntity -> {
                memberDataBeans.add(MemberDataBeanMapper.convertMemberEntityToMemberDataBean(memberEntity));
            });
            FamilyDataBean familyDataBean = FamilyDataBeanMapper.convertFamilyEntityToFamilyDataBean(familyEntity, memberDataBeans);
            map.put("Family to be assigned", familyDataBean);
            return map;
        } else if (familyEntity.getAssignedTo() != null) {
            if (familyEntity.getAssignedTo().equals(userId)) {
                map.put("Family is already assigned to you", null);
            } else {
                map.put("Family is assigned to another user", null);
            }
            return map;
        } else {
            List<Integer> locationIds = userLocationDao.retrieveLocationIdsByUserId(userId);
            if (locationIds.contains(familyEntity.getLocationId())) {
                map.put("Family is already assigned to you", null);
            } else {
                map.put("Family is assigned to another user", null);
            }
            return map;
        }
    }

    @Override
    public FamilyDataBean assignFamilyToUser(String token, String locationId, String familyId) {
        UserToken userToken = userTokenDao.retrieveByUserToken(token);
        if (userToken != null) {
            UserMaster user = this.isUserTokenValid(userToken.getUserToken());
            if (user != null) {
                FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(familyId);
                familyEntity.setAssignedTo(user.getId());
                familyEntity.setLocationId(Integer.valueOf(locationId));
                if (FamilyHealthSurveyServiceConstants.FHS_ARCHIVED_CRITERIA_FAMILY_STATES.contains(familyEntity.getState())) {
                    familyHealthSurveyService.updateFamily(familyEntity, familyEntity.getState(),
                            FamilyHealthSurveyServiceConstants.FHS_FAMILY_STATE_UNVERIFIED);
                } else {
                    familyDao.update(familyEntity);
                }

                List<MemberDataBean> memberDataBeans = new ArrayList<>();
                List<MemberEntity> memberDtos = memberDao.retrieveMemberEntitiesByFamilyId(familyId);
                for (MemberEntity memberEntity : memberDtos) {
                    MemberDataBean memberDataBean = MemberDataBeanMapper.convertMemberEntityToMemberDataBean(memberEntity);
                    memberDataBeans.add(memberDataBean);
                }

                return FamilyDataBeanMapper.convertFamilyEntityToFamilyDataBean(familyEntity, memberDataBeans);
            }
        }
        return null;
    }

    @Override
    public List<UserFormAccessBean> getUserFormAccessDetail(String token) {
        UserToken userToken = userTokenDao.retrieveByUserToken(token);
        if (userToken != null) {
            if (!ConstantUtil.DROP_TYPE.equals("P")) {
                moveToProductionService.isAnyFormTrainingCompleted(userToken.getUserId());
            }
            return moveToProductionService.getUserFormAccessDetail(userToken.getUserId());
        }
        return new ArrayList<>();
    }

    @Override
    public void userReadyToMoveProduction(String token, String formCode, String userId) {
        UserToken userToken = userTokenDao.retrieveByUserToken(token);
        if (userToken != null) {
            moveToProductionService.userReadyToMoveProduction(userToken.getUserId(), formCode);
        } else if (userId != null) {
            moveToProductionService.userReadyToMoveProduction(Integer.parseInt(userId), formCode);
        }
    }

    //BELOW IS THE OLDER BACKUP IMPLEMENTATION OF GET DETAILS CALL, NO NEED TO KEEP IT IF THREAD BASED APPROACH WORKS
//    @Override
//    public LoggedInUserPrincipleDto getDetailsArchive(LogInRequestParamDetailDto paramDetailDto, Integer apkVersion) {
//
//        if (ConstantUtil.DROP_TYPE == null) {
//            Properties props = new Properties();
//            try {
//                props.load(getClass().getResourceAsStream("/build.properties"));
//                ConstantUtil.DROP_TYPE = props.getProperty("deployType").trim();
//            } catch (IOException ex) {
//                Logger.getLogger(MobileFhsServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        UserTokenDto userTokenDto = userTokenDao.retrieveDtoByUserToken(paramDetailDto.getToken());
//        if (userTokenDto != null) {
//            UserMaster user = this.isUserTokenValid(userTokenDto.getUserToken());
//            if (user != null) {
//                Integer sheetVersion = null;
//                user.setTechoPhoneNumber(paramDetailDto.getPhoneNumber());
//                user.setImeiNumber(paramDetailDto.getImeiNumber());
//                user.setSdkVersion(paramDetailDto.getSdkVersion());
//                user.setFreeSpaceMB(paramDetailDto.getFreeSpaceMB());
//                if (paramDetailDto.getLatitude() != null && paramDetailDto.getLongitude() != null) {
//                    user.setLatitude(paramDetailDto.getLatitude());
//                    user.setLongitude(paramDetailDto.getLongitude());
//                }
//                userDao.update(user);
//
//                if (paramDetailDto.getInstalledApps() != null && !paramDetailDto.getInstalledApps().isEmpty()) {
//                    this.storeUserInstalledAppsInfo(user.getId(),
//                            paramDetailDto.getImeiNumber(),
//                            paramDetailDto.getInstalledApps()
//                    );
//                }
//
//                if (paramDetailDto.getReadNotifications() != null && !paramDetailDto.getReadNotifications().isEmpty()) {
//                    techoNotificationService.markNotificationAsRead(paramDetailDto.getReadNotifications(), user.getId());
//                }
//
//                LoggedInUserPrincipleDto loggedInUserPrincipleDto = new LoggedInUserPrincipleDto();
//
//                if (ConstantUtil.DROP_TYPE.equals("P")) {
//                    loggedInUserPrincipleDto.setFhwServiceStatusDtos(this.retrieveFhwServiceDetailBean(user.getId()));
//                }
//
//                if (paramDetailDto.getLastUpdateDateForFamily() != null) {
//                    this.getUpdatedFamilyDataByDate(
//                            user,
//                            paramDetailDto.getLastUpdateDateForFamily(),
//                            loggedInUserPrincipleDto);
//                    loggedInUserPrincipleDto.setDeletedFamilies(
//                            this.retrieveDeletedFamiliesList(
//                                    user,
//                                    paramDetailDto.getLastUpdateDateForFamily()));
//                } else {
//                    this.retrieveAssignedFamiliesForFHS(user, loggedInUserPrincipleDto);
//                }
//
//                if (paramDetailDto.getUserId() != null) {
//                    loggedInUserPrincipleDto.setRetrievedVillageAndChildLocations(
//                            this.retrieveVillagesAndChildLocations(
//                                    paramDetailDto.getUserId(), user.getRoleId()));
//                    loggedInUserPrincipleDto.setCsvCoordinates(this.retrieveCoordinatesForAssignedLocations(paramDetailDto.getUserId()));
//                }
//
////                memberPregnancyStatusService.retrievePregnancyStatusForMobile(loggedInUserPrincipleDto, paramDetailDto);
//
//                if (paramDetailDto.getLastUpdateDateForNotifications() != null) {
//                    List<TechoNotificationDataBean> toBeRemovedNotificationDataBeans = new LinkedList<>();
//                    List<TechoNotificationDataBean> updatedNotificationByUser;
//                    List<Integer> deletedNotificationIds;
//                    deletedNotificationIds = techoNotificationMasterDao.getDeletedNotificationsForUserByLastModifiedOn(
//                            userTokenDto.getUserId(),
//                            new Date(paramDetailDto.getLastUpdateDateForNotifications()));
//                    if (user.getRole().getName().equals("MPHW")) {
//                        updatedNotificationByUser = techoNotificationMasterDao.retrieveNotificationsForUserByLastModifiedOn(
//                                userTokenDto.getUserId(), roleDao.retrieveByCode("FHW").getId(),
//                                new Date(paramDetailDto.getLastUpdateDateForNotifications()));
//                    } else {
//                        updatedNotificationByUser = techoNotificationMasterDao.retrieveNotificationsForUserByLastModifiedOn(
//                                userTokenDto.getUserId(), userTokenDto.getRoleId(),
//                                new Date(paramDetailDto.getLastUpdateDateForNotifications()));
//                    }
//                    for (TechoNotificationDataBean techoNotificationDataBean : updatedNotificationByUser) {
//                        if (!techoNotificationDataBean.getState().equals("PENDING")
//                                && !techoNotificationDataBean.getState().equals("RESCHEDULE")) {
//                            deletedNotificationIds.add(techoNotificationDataBean.getId());
//                            toBeRemovedNotificationDataBeans.add(techoNotificationDataBean);
//                        }
//                    }
//                    updatedNotificationByUser.removeAll(toBeRemovedNotificationDataBeans);
//                    loggedInUserPrincipleDto.setNotifications(updatedNotificationByUser);
//                    loggedInUserPrincipleDto.setDeletedNotifications(deletedNotificationIds);
//                } else {
//                    if (user.getRole().getName().equals("MPHW")) {
//                        loggedInUserPrincipleDto.setNotifications(
//                                techoNotificationMasterDao.retrieveAllNotificationsForUser(userTokenDto.getUserId(), roleDao.retrieveByCode("FHW").getId()));
//                    } else {
//                        loggedInUserPrincipleDto.setNotifications(
//                                techoNotificationMasterDao.retrieveAllNotificationsForUser(userTokenDto.getUserId(), userTokenDto.getRoleId()));
//                    }
//                }
//
//                if (paramDetailDto.getSheetNameVersionMap() != null && !paramDetailDto.getSheetNameVersionMap().isEmpty()) {
//                    Map<String, Integer> sheetNameVersionMap = paramDetailDto.getSheetNameVersionMap();
//                    Map<String, Integer> currentSheetVersionMap = new HashMap<>();
//                    sheetNameVersionMap.forEach((String sheetNameVersionKey, Integer version) -> {
//                        SystemConfiguration systemConfiguration = systemConfigurationDao.retrieveSystemConfigurationByKey(sheetNameVersionKey);
//                        Integer systemVersion = Integer.parseInt(systemConfiguration.getKeyValue());
//                        if (version.intValue() != systemVersion.intValue()) {
//                            loggedInUserPrincipleDto.setRetrievedXlsData(tenantCacheProviderForXlsDataFhw.get());
//                            currentSheetVersionMap.put(sheetNameVersionKey, systemVersion);
//                            loggedInUserPrincipleDto.setCurrentSheetVersion(currentSheetVersionMap);
//                        }
//                    });
//                }
//
//                loggedInUserPrincipleDto.setMigratedMembersDataBeans(
//                        migrationService.retrieveMigrationDetailsDataBean(user.getId()));
//
//                loggedInUserPrincipleDto.setMobileLibraryDataBeans(
//                        mobileLibraryService.retrieveMobileLibraryDataBeans(
//                                user.getRoleId(),
//                                paramDetailDto.getLastUpdateDateForLibrary())
//                );
//
//                if (user.getPrefferedLanguage() != null) {
//                    loggedInUserPrincipleDto.setRetrievedLabels(
//                            this.retrieveLabels(
//                                    paramDetailDto.getCreatedOnDateForLabel(),
//                                    user.getPrefferedLanguage()));
//                }
//
//                loggedInUserPrincipleDto.setRetrievedListValues(
//                        this.retrieveListValues(
//                                paramDetailDto.getLastUpdateDateForListValue(),
//                                user));
//
//                loggedInUserPrincipleDto.setDataQualityBeans(
//                        this.retrieveDataQualityValues(
//                                paramDetailDto.getLastUpdateDateForDataQualityValue(),
//                                user));
//
//                loggedInUserPrincipleDto.setRetrievedAnnouncements(
//                        this.retrieveAnnouncements(
//                                user,
//                                paramDetailDto.getRoleType(),
//                                paramDetailDto.getLastUpdateOfAnnouncements(),
//                                user.getPrefferedLanguage()));
//
//                loggedInUserPrincipleDto.setLocationMasterBeans(
//                        this.getLocationMasterDetails(paramDetailDto));
//
//                loggedInUserPrincipleDto.setHealthInfrastructures(
//                        this.getHealthInfrastructureDetails(paramDetailDto));
//
//                loggedInUserPrincipleDto.setSchools(this.getSchoolDetails(paramDetailDto));
//
//                loggedInUserPrincipleDto.setFeatures(
//                        mobileUtilService.retrieveFeaturesAssignedToTheAoi(user.getId()));
//
//                if (paramDetailDto.getMobileFormVersion() != null) {
//                    sheetVersion = paramDetailDto.getMobileFormVersion();
//                }
//                // make entry in user data access request table
//                this.userDataRequestInsertion(user.getId(), apkVersion, paramDetailDto.getImeiNumber(), sheetVersion);
////                System.out.println("Total time:" + (new Date().getTime() - date.getTime()));
//                return loggedInUserPrincipleDto;
//            }
//        }
//        return null;
//    }


    @Override
    public LoggedInUserPrincipleDto getDetails(LogInRequestParamDetailDto paramDetailDto, Integer apkVersion) throws Exception {
//        Date date = new Date();
//        System.out.println("Start time:" + date.toString());
        if (ConstantUtil.DROP_TYPE == null) {
            Properties props = new Properties();
            try {
                props.load(getClass().getResourceAsStream("/build.properties"));
                ConstantUtil.DROP_TYPE = props.getProperty("deployType").trim();
            } catch (IOException ex) {
                Logger.getLogger(MobileFhsServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        UserTokenDto userTokenDto = userTokenDao.retrieveDtoByUserToken(paramDetailDto.getToken());
        if (userTokenDto != null) {
            UserMaster user = this.isUserTokenValid(userTokenDto.getUserToken());
            if (user != null) {
                Integer sheetVersion = null;
                user.setTechoPhoneNumber(paramDetailDto.getPhoneNumber());
                user.setImeiNumber(paramDetailDto.getImeiNumber());
                user.setSdkVersion(paramDetailDto.getSdkVersion());
                user.setFreeSpaceMB(paramDetailDto.getFreeSpaceMB());
                if (paramDetailDto.getLatitude() != null && paramDetailDto.getLongitude() != null) {
                    user.setLatitude(paramDetailDto.getLatitude());
                    user.setLongitude(paramDetailDto.getLongitude());
                }
                userDao.update(user);


                CompletableFuture<Void> markNotificationAsReadCF = null;
                if (paramDetailDto.getReadNotifications() != null && !paramDetailDto.getReadNotifications().isEmpty()) {
                    markNotificationAsReadCF = CompletableFuture.runAsync(() ->
                                    techoNotificationService.markNotificationAsRead(paramDetailDto.getReadNotifications(), user.getId()),
                            TechoCompletableFuture.mobileGetDetailThreadPool);
                }

                LoggedInUserPrincipleDto loggedInUserPrincipleDto = new LoggedInUserPrincipleDto();

                //DISABLING FOR CORONA
                CompletableFuture<List<FhwServiceStatusDto>> fhwServiceDetailDtoCF = null;
                if (ConstantUtil.DROP_TYPE.equals("P")) {
                    fhwServiceDetailDtoCF = CompletableFuture.supplyAsync(()
                            -> this.retrieveFhwServiceDetailBean(user.getId()), TechoCompletableFuture.mobileGetDetailThreadPool);
                }

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

                CompletableFuture<Map<Integer, List<SurveyLocationMobDataBean>>> villagesAndChildLocationsCF =
                        CompletableFuture.supplyAsync(() -> this.retrieveVillagesAndChildLocations(
                                paramDetailDto.getUserId(), user.getRoleId()),
                                TechoCompletableFuture.mobileGetDetailThreadPool);



                CompletableFuture<Void> notificationsCF = CompletableFuture.runAsync(()
                                -> this.getNotificationsForMobile(loggedInUserPrincipleDto, paramDetailDto, userTokenDto, user),
                        TechoCompletableFuture.mobileGetDetailThreadPool);

//                CompletableFuture<List<MigratedMembersDataBean>> migratedMembersCF = CompletableFuture.supplyAsync(()
//                                -> migrationService.retrieveMigrationDetailsDataBean(user.getId()),
//                        TechoCompletableFuture.mobileGetDetailThreadPool);

//                CompletableFuture<List<MigratedFamilyDataBean>> migratedFamilyCF = CompletableFuture.supplyAsync(()
//                                -> familyMigrationService.retrieveMigrationByLocation(user.getId()),
//                        TechoCompletableFuture.mobileGetDetailThreadPool);

                //DISABLING FOR CORONA
//                CompletableFuture<List<MobileLibraryDataBean>> mobileLibrararyCF = CompletableFuture.supplyAsync(()
//                                -> mobileLibraryService.retrieveMobileLibraryDataBeans(user.getRoleId(),
//                        paramDetailDto.getLastUpdateDateForLibrary()),
//                        TechoCompletableFuture.mobileGetDetailThreadPool);

                CompletableFuture<QueryDto> languageLabelsCF = null;
                if (user.getPrefferedLanguage() != null) {
                    languageLabelsCF = CompletableFuture.supplyAsync(()
                                    -> this.retrieveLabels(paramDetailDto.getCreatedOnDateForLabel(),
                            user.getPrefferedLanguage()),
                            TechoCompletableFuture.mobileGetDetailThreadPool);
                }

                CompletableFuture<List<LinkedHashMap<String, Object>>> listValuesCF = CompletableFuture.supplyAsync(()
                                -> this.retrieveListValues(paramDetailDto.getLastUpdateDateForListValue(), user),
                        TechoCompletableFuture.mobileGetDetailThreadPool);

                CompletableFuture<List<LinkedHashMap<String, Object>>> dataQualityBeansCF = CompletableFuture.supplyAsync(()
                                -> this.retrieveDataQualityValues(paramDetailDto.getLastUpdateDateForDataQualityValue(), user),
                        TechoCompletableFuture.mobileGetDetailThreadPool);

                CompletableFuture<List<AnnouncementMobDataBean>> announcementsCF = CompletableFuture.supplyAsync(()
                                -> this.retrieveAnnouncements(user, paramDetailDto.getRoleType(),
                        paramDetailDto.getLastUpdateOfAnnouncements(),
                        user.getPrefferedLanguage()),
                        TechoCompletableFuture.mobileGetDetailThreadPool);

                CompletableFuture<List<LocationMasterDataBean>> locationMasterCF = CompletableFuture.supplyAsync(()
                                -> this.getLocationMasterDetails(paramDetailDto),
                        TechoCompletableFuture.mobileGetDetailThreadPool);

                CompletableFuture<List<HealthInfrastructureBean>> healthInfraCF = CompletableFuture.supplyAsync(()
                                -> this.getHealthInfrastructureDetails(paramDetailDto),
                        TechoCompletableFuture.mobileGetDetailThreadPool);

//                CompletableFuture<List<SchoolDataBean>> schoolsCF = CompletableFuture.supplyAsync(()
//                                -> this.getSchoolDetails(paramDetailDto),
//                        TechoCompletableFuture.mobileGetDetailThreadPool);

                CompletableFuture<List<String>> featuresAssignedCF = CompletableFuture.supplyAsync(()
                                -> mobileUtilService.retrieveFeaturesAssignedToTheAoi(user.getId()),
                        TechoCompletableFuture.mobileGetDetailThreadPool);

//                CompletableFuture<List<CovidTravellersInfoMobileDto>> covidTravellersInfoCF = CompletableFuture.supplyAsync(()
//                                -> covidTravellersInfoService.retrieveCovidTravellersInfoMobileDtoList(paramDetailDto.getLastUpdateDateForCovidTravellersInfo(), user),
//                        TechoCompletableFuture.mobileGetDetailThreadPool);

                CompletableFuture<List<LocationTypeMaster>> locationTypeMasterCF = CompletableFuture.supplyAsync(()
                                -> locationTypeService.retrieveLocationTypeMasterByModifiedOn(paramDetailDto.getLastUpdateDateForLocationTypeMaster()),
                        TechoCompletableFuture.mobileGetDetailThreadPool);



                CompletableFuture<List<MenuDataBean>> menuCF = CompletableFuture.supplyAsync(()
                                -> this.getMenuDetails(paramDetailDto, user.getRoleId()),
                        TechoCompletableFuture.mobileGetDetailThreadPool);

                if (markNotificationAsReadCF != null) {
                    markNotificationAsReadCF.get();
                }

//                loggedInUserPrincipleDto.setMigratedMembersDataBeans(migratedMembersCF.get());

//                loggedInUserPrincipleDto.setMigratedFamilyDataBeans(migratedFamilyCF.get());

//                loggedInUserPrincipleDto.setMobileLibraryDataBeans(mobileLibrararyCF.get());

                loggedInUserPrincipleDto.setRetrievedListValues(listValuesCF.get());

                if (languageLabelsCF != null) {
                    loggedInUserPrincipleDto.setRetrievedLabels(languageLabelsCF.get());
                }

                loggedInUserPrincipleDto.setDataQualityBeans(dataQualityBeansCF.get());

                loggedInUserPrincipleDto.setRetrievedAnnouncements(announcementsCF.get());

                loggedInUserPrincipleDto.setLocationMasterBeans(locationMasterCF.get());

                loggedInUserPrincipleDto.setHealthInfrastructures(healthInfraCF.get());

//                loggedInUserPrincipleDto.setSchools(schoolsCF.get());

                loggedInUserPrincipleDto.setFeatures(featuresAssignedCF.get());

                if (fhwServiceDetailDtoCF != null) {
                    loggedInUserPrincipleDto.setFhwServiceStatusDtos(fhwServiceDetailDtoCF.get());
                }

                loggedInUserPrincipleDto.setRetrievedVillageAndChildLocations(villagesAndChildLocationsCF.get());

//                loggedInUserPrincipleDto.setCsvCoordinates(csvCoordinatesCF.get());

//                loggedInUserPrincipleDto.setCovidTravellersInfos(covidTravellersInfoCF.get());

                notificationsCF.get();

                if (deletedFamiliesCF != null) {
                    loggedInUserPrincipleDto.setDeletedFamilies(deletedFamiliesCF.get());
                }

                if (getUpdatedFamiliesCF != null) {
                    getUpdatedFamiliesCF.get();
                }

                loggedInUserPrincipleDto.setLocationTypeMasters(locationTypeMasterCF.get());

                loggedInUserPrincipleDto.setMobileMenus(menuCF.get());


                if (paramDetailDto.getMobileFormVersion() != null) {
                    sheetVersion = paramDetailDto.getMobileFormVersion();
                }
                // make entry in user data access request table
                this.userDataRequestInsertion(user.getId(), apkVersion, paramDetailDto.getImeiNumber(), sheetVersion);
//                this.getXlsSheetDataForMobile(paramDetailDto.getSheetNameVersionMap(), loggedInUserPrincipleDto);
//                memberPregnancyStatusService.retrievePregnancyStatusForMobile(loggedInUserPrincipleDto, paramDetailDto);
//                System.out.println("Total time:" + (new Date().getTime() - date.getTime()));
                return loggedInUserPrincipleDto;
            }
        }
        return null;
    }

    private void getNotificationsForMobile(LoggedInUserPrincipleDto loggedInUserPrincipleDto,
                                           LogInRequestParamDetailDto paramDetailDto,
                                           UserTokenDto userTokenDto,
                                           UserMaster user) {
        Date d1 = new Date();
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
//        System.out.println("notification time:" + (new Date().getTime() - d1.getTime()));
    }

    @Override
    public QueryDto retrieveLabels(Long createdOnDate, String prefferedLanguage) {
        if (prefferedLanguage != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
            Date date = new Date(0);
            if (createdOnDate != null) {
                Calendar instance = getInstance();
                instance.setTime(new Date(createdOnDate));
                instance.set(HOUR, 0);
                instance.set(MINUTE, 0);
                instance.set(SECOND, 0);
                instance.set(MILLISECOND, 0);
                date = instance.getTime();
            }
            QueryDto queryDto = new QueryDto();
            queryDto.setCode("translation_mobile_label_retrival_after_date");
            LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
            parameters.put("createdOn", simpleDateFormat.format(date));
            parameters.put("preferedLanguage", prefferedLanguage);
            queryDto.setParameters(parameters);
            List<QueryDto> queryDtos = new LinkedList<>();
            queryDtos.add(queryDto);
            List<QueryDto> resultQueryDto = queryMasterService.executeQuery(queryDtos, true);
            if (resultQueryDto != null && !resultQueryDto.isEmpty()) {
                return (resultQueryDto.get(0));
            }
        }
        return null;
    }

    @Override
    public List<LinkedHashMap<String, Object>> retrieveListValues(Long updatedOnDate, UserMaster user) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        if (updatedOnDate == null) {
            updatedOnDate = 0L;
        }

        if (user != null) {
            QueryDto queryDto = new QueryDto();
            queryDto.setCode("retrival_listvalues_mobile");
            LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
            parameters.put("lastUpdatedOn", simpleDateFormat.format(new Date(updatedOnDate)));
            parameters.put("roleId", user.getRoleId());

            queryDto.setParameters(parameters);
            List<QueryDto> queryDtos = new LinkedList<>();
            queryDtos.add(queryDto);
            List<QueryDto> resultQueryDto = queryMasterService.executeQuery(queryDtos, true);
            if (resultQueryDto != null && !resultQueryDto.isEmpty()) {
                return (resultQueryDto.get(0)).getResult();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<LinkedHashMap<String, Object>> retrieveDataQualityValues(Long updatedOnDate, UserMaster user) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        if (updatedOnDate == null) {
            updatedOnDate = 0L;
        }

        if (user != null) {
            QueryDto queryDto = new QueryDto();
            queryDto.setCode("mobile_data_quality_verification_data_beans");
            LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
            parameters.put("modified_on", simpleDateFormat.format(new Date(updatedOnDate)));
            parameters.put("user_id", user.getId());

            queryDto.setParameters(parameters);
            List<QueryDto> queryDtos = new LinkedList<>();
            queryDtos.add(queryDto);
            List<QueryDto> resultQueryDto = queryMasterService.executeQuery(queryDtos, true);
            if (resultQueryDto != null && !resultQueryDto.isEmpty()) {
                return (resultQueryDto.get(0)).getResult();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<AnnouncementMobDataBean> retrieveAnnouncements(UserMaster user, String userType, Long lastUpdateOn, String languageCode) {
        if (user != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
            if (lastUpdateOn == null) {
                lastUpdateOn = 0L;
            }
            List<AnnouncementMobDataBean> announcementMobDataBeans = new ArrayList<>();

            QueryDto queryDto = new QueryDto();

            if(ConstantUtil.IMPLEMENTATION_TYPE.equals(ConstantUtil.UTTARAKHAND_IMPLEMENTATION)) {
                queryDto.setCode("retrieval_announcement_for_uttarakhand");
            }else {
                queryDto.setCode("retrieval_announcements");
            }

            LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
            parameters.put("userId", user.getId());
            parameters.put("userType", userType);
            parameters.put("lastUpdatedOn", simpleDateFormat.format(new Date(lastUpdateOn)));
            queryDto.setParameters(parameters);
            List<QueryDto> queryDtos = new LinkedList<>();
            queryDtos.add(queryDto);
            List<QueryDto> resultQueryDto = queryMasterService.executeQuery(queryDtos, true);
            List<LinkedHashMap<String, Object>> results = resultQueryDto.get(0).getResult();
            for (LinkedHashMap<String, Object> result : results) {
                try {
                    AnnouncementMobDataBean announcementMobDataBean = new AnnouncementMobDataBean();
                    announcementMobDataBean.setAnnouncementId(Integer.valueOf(result.get("id").toString()));
                    announcementMobDataBean.setSubject(result.get("subject").toString());

                    DateFormat inputFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
                    Date date = inputFormat.parse(result.get("from_date").toString());

                    announcementMobDataBean.setPublishedOn(date.getTime());
                    announcementMobDataBean.setDefaultLanguage(languageCode);
                    announcementMobDataBean.setIsActive(Boolean.parseBoolean(result.get("is_active").toString()));
                    announcementMobDataBean.setLastUpdateOfAnnouncement(new Date().getTime());

                    Boolean containsMultimedia = (Boolean) result.get("contains_multimedia");
                    if (containsMultimedia != null && containsMultimedia && result.get("media_path") != null) {
                        announcementMobDataBean.setFileName(result.get("media_path").toString());
                    }
                    announcementMobDataBeans.add(announcementMobDataBean);
                    if (result.get("modified_on").toString() != null) {
                        announcementMobDataBean.setModifiedOn(inputFormat.parse(result.get("modified_on").toString()));
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(MobileFhsServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return announcementMobDataBeans;
        }
        return new ArrayList<>();
    }

    private List<String> retrieveDeletedFamiliesList(UserMaster user, String lastUpdatedDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);

        QueryDto queryDto = new QueryDto();
        queryDto.setCode("retrieval_deleted_families");
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("user_id", user.getId());
        parameters.put("modified_on", simpleDateFormat.format(new Date(Long.parseLong(lastUpdatedDate))));
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

    @Override
    public String retrieveAndroidVersion(int oldVersion) {
        if (ConstantUtil.DROP_TYPE == null) {
            Properties props = new Properties();
            try {
                props.load(getClass().getResourceAsStream("/build.properties"));
                ConstantUtil.DROP_TYPE = props.getProperty("deployType").trim();
            } catch (IOException ex) {
                Logger.getLogger(MobileFhsServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        SystemConfiguration record = systemConfigurationDao.retrieveSystemConfigurationByKey("ANDROID_VERSION");
        if (record != null) {
            String[] split = record.getKeyValue().split("-");
            String version = split[0];
            version = version.replace("M", "");
            version = version.replace("m", "");
            if (oldVersion >= Integer.parseInt(version)) {
                return record.getKeyValue();
            }
            if (checkNumberOfResponsesInLast2Hours()) {
                return record.getKeyValue();
            }
            return "69-67";
//            return record.getKeyValue()
        }
        return null;
    }

    private boolean checkNumberOfResponsesInLast2Hours() {
        return sequenceDao.checkNumberOfResponsesInLast2Hours();
    }

    @Override
    public String retrieveFontSize(String fontType) {
        if (StringUtils.isNotBlank(fontType)) {
            SystemConfiguration record = systemConfigurationDao.retrieveSystemConfigurationByKey(fontType);
            if (record == null) {
                if (fontType.contains("LARGE")) {
                    record = new SystemConfiguration(fontType, "8.5");
                } else if (fontType.contains("NORMAL")) {
                    record = new SystemConfiguration(fontType, "6");
                } else if (fontType.contains("SMALL")) {
                    record = new SystemConfiguration(fontType, "4.5");
                } else {
                    record = new SystemConfiguration(fontType, "6");
                }
                record.setIsActive(Boolean.TRUE);
                systemConfigurationDao.update(record);
            }
            return record.getKeyValue();
        }
        return null;
    }

    @Override
    public Boolean isUserInProduction(String userName) {
        UserMaster user = userDao.retrieveByUserName(userName);
        return user != null;
    }

    @Override
    public void updateMemberDetailsFromRchForms(String mobileNumber, String aadharMapString, String aadharNumber,
                                                String accountNumber, String ifsc, MemberEntity memberEntity) {
        boolean toUpdate = false;
        if (mobileNumber != null) {
            if (memberEntity.getMobileNumber() == null || !memberEntity.getMobileNumber().equalsIgnoreCase(mobileNumber)) {
                memberEntity.setMobileNumber(mobileNumber);
                memberEntity.setIsMobileNumberVerified(false);
                FamilyEntity familyEntity = familyDao.retrieveFamilyByFamilyId(memberEntity.getFamilyId());
                if (familyEntity != null && familyEntity.getContactPersonId() == null) {
                    familyEntity.setContactPersonId(memberEntity.getId());
                    familyDao.update(familyEntity);
                }
                toUpdate = true;
            }
        }
        if (accountNumber != null) {
            memberEntity.setAccountNumber(accountNumber);
            toUpdate = true;
        }
        if (ifsc != null) {
            memberEntity.setIfsc(ifsc);
            toUpdate = true;
        }
//        if (aadharMapString != null) {
//            Map<String, String> aadharValuesMap = new HashMap<>();
//            String keyValueString = StringUtils.substringBetween(aadharMapString, "{", "}");
//            String[] keyValueSplit = keyValueString.split(",");
//            for (String keyValuePair : keyValueSplit) {
//                String[] split = keyValuePair.split("=");
//                if (split.length == 2) {
//                    aadharValuesMap.put(split[0].trim(), split[1].trim());
//                }
//            }
//            memberEntity.setAadharNumber(aadharValuesMap.get("uid"));
//            memberEntity.setNameAsPerAadhar(aadharValuesMap.get("name"));
//            memberEntity.setAadharNumberAvailable(Boolean.TRUE);
//            toUpdate = true;
//        }
//        if (aadharNumber != null) {
//            memberEntity.setAadharNumber(aadharNumber);
//            memberEntity.setAadharNumberAvailable(Boolean.TRUE);
//            toUpdate = true;
//        }

        if (toUpdate) {
            memberDao.update(memberEntity);
        }
    }

//    @Override
//    public List<RchVillageProfileDto> getRchVillageProfileDto(Integer userId) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    @Override
    public List<LocationMasterDataBean> getLocationMasterDetails(LogInRequestParamDetailDto paramDetailDto) {
        List<LocationMasterDataBean> locations = new LinkedList<>();

        if (paramDetailDto.getLastUpdateDateForLocation() == null) {
            if (!CollectionUtils.isEmpty(LocationConstants.allLocationMasterDataBeans)) {
                return LocationConstants.allLocationMasterDataBeans;
            }
        } else {
            Calendar calendar = getInstance();
            calendar.setTime(new Date(Long.parseLong(paramDetailDto.getLastUpdateDateForLocation())));
            calendar.set(MILLISECOND, 0);
            calendar.set(SECOND, 0);
            calendar.set(MINUTE, 0);
            calendar.set(HOUR_OF_DAY, 0);
            locations = locationMasterDao.retrieveAllActiveLocationsWithWorkerInfo(calendar.getTime());
        }
        return locations;
    }

    /**
     * Called whenever user getDetails method is called
     *
     * @param userId
     * @param apkVersion
     * @param imeiNumber
     */
    @Override
    public void userDataRequestInsertion(Integer userId, Integer apkVersion, String imeiNumber, Integer sheetVersion) {
        UserLoginDetailMaster loginDetailMaster = new UserLoginDetailMaster();
        loginDetailMaster.setUserId(userId);
        loginDetailMaster.setLoggingFromWeb(false);
        loginDetailMaster.setApkVersion(apkVersion);
        loginDetailMaster.setImeiNumber(imeiNumber);
        loginDetailMaster.setMobileFormVersion(sheetVersion);
        userLoginDetailMasterDao.create(loginDetailMaster);
    }

    @Override
    @Async
    public void runPatch() {
        Calendar firstDate = getInstance();
        firstDate.set(118, 5, 22, 0, 0, 0);
        Calendar secondDate = getInstance();
        secondDate.set(118, 5, 24, 23, 59, 59);
        List<SyncStatus> syncStatuses = mobileUtilService.retrieveSyncStatusBetweenActionDates(
                firstDate.getTime(),
                secondDate.getTime());
        Logger.getLogger(getClass().getName()).log(Level.INFO, "DATA RETRIEVED : {0}", syncStatuses.size());
        int count = 0;
        for (SyncStatus syncStatus : syncStatuses) {
            Map<String, String> keyAndAnswerMap = new HashMap<>();
            String answerRecord = parseRecordStringToBean(syncStatus.getRecordString()).getAnswerRecord();

            String[] keyAndAnswerSet = answerRecord.split(MobileConstantUtil.ANSWER_STRING_FIRST_SEPARATER);
            for (String aKeyAndAnswer : keyAndAnswerSet) {
                String[] keyAnswerSplit = aKeyAndAnswer.split(MobileConstantUtil.ANSWER_STRING_SECOND_SEPARATER);
                if (keyAnswerSplit.length != 2) {
                    continue;
                }
                keyAndAnswerMap.put(keyAnswerSplit[0], keyAnswerSplit[1]);
            }

            Map<Integer, Integer> mapOfScAnganwadiWithIndex = new HashMap<>();
            Map<String, Integer> mapOfPhcAnganwadiWithIndex = new HashMap<>();
            if (keyAndAnswerMap.get("101").equals("1") && keyAndAnswerMap.get("800") != null && !keyAndAnswerMap.get("800").equals("0")) {
                Logger.getLogger(getClass().getName()).log(Level.INFO, "================================================");
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Selected Subcenter Id:" + keyAndAnswerMap.get("800"));
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Selected Anganwadi Id:" + keyAndAnswerMap.get("8"));
                LocationMaster subcenter = locationMasterDao.retrieveById(Integer.valueOf(keyAndAnswerMap.get("800")));
                List<Anganwadi> subcenterAnganwadis = anganwadiDao.getAnganwadisByParentIdsList(locationHierchyCloserDetailDao.retrieveChildLocationIds(subcenter.getId()));
                List<Anganwadi> phcAnganwadis = anganwadiDao.getAnganwadisByParentIdsList(locationHierchyCloserDetailDao.retrieveChildLocationIds(subcenter.getParent()));

                int indexSc = 0;
                for (Anganwadi anganwadi : subcenterAnganwadis) {
                    mapOfScAnganwadiWithIndex.put(indexSc++, anganwadi.getId());
                }

                int indexPhc = 0;
                for (Anganwadi anganwadi : phcAnganwadis) {
                    mapOfPhcAnganwadiWithIndex.put(anganwadi.getId().toString(), indexPhc++);
                }

                Logger.getLogger(getClass().getName()).log(Level.INFO,
                        "Real Proposed Anganwadi Id:" + mapOfScAnganwadiWithIndex.get(mapOfPhcAnganwadiWithIndex.get(keyAndAnswerMap.get("8"))));
                count++;
            }
        }
        Logger.getLogger(getClass().getName()).log(Level.INFO, "TOTAL FAULTY FAMILIES : {0}", count);
    }

    private ParsedRecordBean parseRecordStringToBean(String record) {
        String checksum;
        String mobileDate;
        String answerEntity;
        String customType;
        String relativeId;
        String formFillUpTime;
        String notificationId;
        String morbidityFrame;
        String answerRecord;

        int frameSize = record.split(MobileConstantUtil.CHECKSUM_AND_ENTITY_TYPE_SEPARATER).length;

        //  Parse Checksum
        int start = 0;
        int end = record.indexOf(MobileConstantUtil.CHECKSUM_AND_ENTITY_TYPE_SEPARATER);
        checksum = record.substring(start, end);

        if (frameSize >= 9) {
            //  Mobile Date
            start = end + 1;
            end = record.indexOf(MobileConstantUtil.CHECKSUM_AND_ENTITY_TYPE_SEPARATER, start);
            mobileDate = record.substring(start, end);
        } else {
            mobileDate = String.valueOf(new Date().getTime());
        }

        //  Parse Answer Entiry (Record Type e.g. EDP, ANC etc)
        start = end + 1;
        end = record.indexOf(MobileConstantUtil.CHECKSUM_AND_ENTITY_TYPE_SEPARATER, start);
        answerEntity = record.substring(start, end);

        //  Parse customType (Record is of e.g. Mother, Child or Other etc)
        start = end + 1;
        end = record.indexOf(MobileConstantUtil.CHECKSUM_AND_ENTITY_TYPE_SEPARATER, start);
        customType = record.substring(start, end);

        //  Parse Related Instance ID
        start = end + 1;
        end = record.indexOf(MobileConstantUtil.CHECKSUM_AND_ENTITY_TYPE_SEPARATER, start);
        relativeId = record.substring(start, end);

        //  Parse Form Creation Time
        start = end + 1;
        end = record.indexOf(MobileConstantUtil.CHECKSUM_AND_ENTITY_TYPE_SEPARATER, start);
        formFillUpTime = record.substring(start, end);

        //  Notification Id
        start = end + 1;
        end = record.indexOf(MobileConstantUtil.CHECKSUM_AND_ENTITY_TYPE_SEPARATER, start);
        notificationId = record.substring(start, end);

        //  morbidityFrame
        start = end + 1;
        end = record.indexOf(MobileConstantUtil.CHECKSUM_AND_ENTITY_TYPE_SEPARATER, start);
        morbidityFrame = record.substring(start, end);

        //  Parse Actual Record data
        start = end + 1;
        end = record.length();
        answerRecord = record.substring(start, end);

        ParsedRecordBean parsedRecordBean = new ParsedRecordBean();
        parsedRecordBean.setChecksum(checksum);
        parsedRecordBean.setMobileDate(mobileDate);
        parsedRecordBean.setAnswerEntity(answerEntity);
        parsedRecordBean.setCustomType(customType);
        parsedRecordBean.setRelativeId(relativeId);
        parsedRecordBean.setFormFillTime(formFillUpTime);
        parsedRecordBean.setNotificationId(notificationId);
        parsedRecordBean.setMorbidityFrame(morbidityFrame);
        parsedRecordBean.setAnswerRecord(answerRecord);
        return parsedRecordBean;
    }

    //DO NOT STORE THIS DATA
//    @Override
//    public void storeUserInstalledAppsInfo(Integer userId, String imei, List<InstalledAppsInfoBean> appsInfoBeans) {
//        if (imei != null && !imei.isEmpty() && !imei.equals("null") && userId != null && appsInfoBeans != null && !appsInfoBeans.isEmpty()) {
//            userInstalledAppsMasterDao.deleteUserInstalledAppsByUserIdAndImei(userId, imei);
//            List<UserInstalledAppsMaster> userInstalledAppsMasters = new ArrayList<>();
//            for (InstalledAppsInfoBean appsInfoBean : appsInfoBeans) {
//                userInstalledAppsMasters.add(UserInstalledAppsMapper.convertInstalledAppsInfoToUserInstalledAppsMaster(appsInfoBean, imei, userId));
//            }
//
//            if (!userInstalledAppsMasters.isEmpty()) {
//                userInstalledAppsMasterDao.createOrUpdateAll(userInstalledAppsMasters);
//            }
//        }
//    }

    @Override
    public List<HealthInfrastructureBean> getHealthInfrastructureDetails(LogInRequestParamDetailDto paramDetailDto) {
        if (paramDetailDto.getLastUpdateDateForHealthInfrastructure() == null
                || paramDetailDto.getLastUpdateDateForHealthInfrastructure() == 0L) {
            if (!CollectionUtils.isEmpty(LocationConstants.allHealthInfrastructureForMobile)) {
                return LocationConstants.allHealthInfrastructureForMobile;
            }
        } else {
            return healthInfrastructureDao.retrieveAllHealthInfrastructureForMobile(new Date(paramDetailDto.getLastUpdateDateForHealthInfrastructure()));
        }
        return new ArrayList<>();
    }

    @Override
    public List<SchoolDataBean> getSchoolDetails(LogInRequestParamDetailDto paramDetailDto) {
        if (paramDetailDto.getLastUpdateDateForSchoolMaster() == null) {
            if (!CollectionUtils.isEmpty(LocationConstants.allSchoolsForMobile)) {
                return LocationConstants.allSchoolsForMobile;
            }
        } else {
            return schoolMasterDao.retrieveAllSchoolForMobile(new Date(paramDetailDto.getLastUpdateDateForSchoolMaster()));
        }
        return new ArrayList<>();
    }

    @Override
    public void updateAllSchoolDetailsForMobile() {
        System.out.println("Updating Schools For Mobile ");
        LocationConstants.allSchoolsForMobile = schoolMasterDao.retrieveAllSchoolForMobile(null);
    }

//    @Override
//    public Map<Integer, String> getNameBasedOnAadharForAllMembersByUserId(MobileRequestParamDto mobileRequestParamDto) {
//        if (mobileRequestParamDto.getToken() != null) {
//            UserTokenDto userTokenDto = userTokenDao.retrieveDtoByUserToken(mobileRequestParamDto.getToken());
//            if (userTokenDto != null) {
//                UserMaster user = this.isUserTokenValid(userTokenDto.getUserToken());
//                if (user != null) {
//                    boolean isAsha = user.getRoleId().equals(24);
//                    List<MemberDataBean> memberDataBeans = memberDao.getNameBasedOnAadharForAllMembersByLocation(user.getId(), isAsha);
//                    Map<Integer, String> map = new HashMap<>();
//                    if (memberDataBeans != null && !memberDataBeans.isEmpty()) {
//                        for (MemberDataBean memberDataBean : memberDataBeans) {
//                            map.put(memberDataBean.getId(), memberDataBean.getNameBasedOnAadhar());
//                        }
//                        return map;
//                    }
//                }
//            }
//        }
//        return null;
//    }

    @Override
    public void setXlsSheetsAsComponentTagsInMemory() {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Updating XLS Data In Memory");
        SystemConfiguration mobileFormVersion = systemConfigurationDao.retrieveSystemConfigurationByKey(SystemConstantUtil.MOBILE_FORM_VERSION);

        List<String> formNames = mobileFormDao.getFileNames();

        Map<String, List<ComponentTagDto>> retrievedXlsData = new HashMap<>();
        Map<String, List<ComponentTagDto>> retrievedXlsDataFhw = new HashMap<>();
        SystemConstantUtil.FHW_SHEETS.forEach((String sheetName) -> {
            List<ComponentTagDto> componentTagDtos = xlsToDtoConversion.xlsConversionMain(sheetName, mobileFormVersion.getKeyValue());
            retrievedXlsDataFhw.put(sheetName, componentTagDtos);
            // This will create or Update all the Xls form of Mobile to MobileFormMaster and store it to database
//            if (!mobileFormMastersCreated.contains(sheetName)) {
//                mobileFormMasterService.createMobileFormMaster(componentTagDtos, sheetName);
//                mobileFormMastersCreated.add(sheetName);
//            }
//            retrievedXlsDataFhw.put(sheetName, mobileFormMasterService.retrieveMobileFormBySheet(sheetName));
        });
        SystemConstantUtil.xlsDataFhw = retrievedXlsDataFhw;

        Map<String, List<ComponentTagDto>> retrievedXlsDataAsha = new HashMap<>();
        SystemConstantUtil.ASHA_SHEETS.forEach((String sheetName) -> {
            List<ComponentTagDto> componentTagDtos = xlsToDtoConversion.xlsConversionMain(sheetName, mobileFormVersion.getKeyValue());
            retrievedXlsDataAsha.put(sheetName, componentTagDtos);
            // This will create or Update all the Xls form of Mobile to MobileFormMaster and store it to database
//            if (!mobileFormMastersCreated.contains(sheetName)) {
//                mobileFormMasterService.createMobileFormMaster(componentTagDtos, sheetName);
//                mobileFormMastersCreated.add(sheetName);
//            }
//            retrievedXlsDataAsha.put(sheetName, mobileFormMasterService.retrieveMobileFormBySheet(sheetName));
        });
        SystemConstantUtil.xlsDataAsha = retrievedXlsDataAsha;

        List<String> mobileForms = systemConstraintService.getActiveMobileForms();
        formNames.forEach((String sheetName) -> {
            List<ComponentTagDto> componentTagDtos;
            if (mobileForms.contains(sheetName)) {
                componentTagDtos = SystemConstraintComponentTagDtoMapper.
                        mapToComponentTagDtoList(
                                systemConstraintService.
                                        getMobileTemplateConfig(sheetName));

            } else {
                componentTagDtos = xlsToDtoConversion.xlsConversionMain(sheetName, mobileFormVersion.getKeyValue());

            }
            retrievedXlsData.put(sheetName, componentTagDtos);
        });
        SystemConstantUtil.xlsAllData = retrievedXlsData;
    }

    @Override
    public void getXlsFormDataForMobile(LogInRequestParamDetailDto paramDetailDto, UserMaster user, LoggedInUserPrincipleDto loggedInUserPrincipleDto) {
        Integer sheetVersion = paramDetailDto.getMobileFormVersion();
        if (sheetVersion == null) {
            return;
        }
        Integer userRole = user.getRoleId();
        long lastUpdatedDateForLVB = paramDetailDto.getLastUpdateDateForListValue() == null ? 0L : paramDetailDto.getLastUpdateDateForListValue();

        SystemConfiguration systemConfiguration = systemConfigurationDao.retrieveSystemConfigurationByKey(SystemConstantUtil.MOBILE_FORM_VERSION);

        Integer systemVersion = Integer.parseInt(systemConfiguration.getKeyValue());
        if (systemVersion > sheetVersion) {
            retrieveMobileFormData(systemVersion, userRole, loggedInUserPrincipleDto);
            lastUpdatedDateForLVB = 0L;
        } else {
            List<MenuDataBean> menuDetails = getMenuDetails(paramDetailDto, userRole);
            if (menuDetails != null && !menuDetails.isEmpty()) {
                retrieveMobileFormData(systemVersion, userRole, loggedInUserPrincipleDto);
                lastUpdatedDateForLVB = 0L;
            }
        }

        loggedInUserPrincipleDto.setRetrievedListValues(retrieveListValues(lastUpdatedDateForLVB, user));
    }

    private void retrieveMobileFormData(Integer systemVersion, Integer userRole, LoggedInUserPrincipleDto loggedInUserPrincipleDto) {
        List<String> formsByRole = mobileFormDao.getFileNamesByFeature(userRole);

        if (formsByRole == null || formsByRole.isEmpty()) {
            return;
        }

        Map<String, List<ComponentTagDto>> allFormData = SystemConstantUtil.xlsAllData;
        Map<String, List<ComponentTagDto>> retrievedAllXlsData = new HashMap<>();

        formsByRole.forEach((sheetName) -> retrievedAllXlsData.put(sheetName, allFormData.get(sheetName)));
        loggedInUserPrincipleDto.setRetrievedXlsData(retrievedAllXlsData);
        loggedInUserPrincipleDto.setCurrentMobileFormVersion(systemVersion);
    }

//    @Override
//    public void getXlsSheetDataForMobile(Map<String, Integer> sheetNameVersionMap, LoggedInUserPrincipleDto loggedInUserPrincipleDto) {
//        if (sheetNameVersionMap != null && !sheetNameVersionMap.isEmpty()) {
//            Map<String, Integer> currentSheetVersionMap = new HashMap<>();
//            SystemConfiguration systemConfiguration = systemConfigurationDao.retrieveSystemConfigurationByKey(SystemConstantUtil.MOBILE_FORM_VERSION);
//            Integer systemVersion = Integer.parseInt(systemConfiguration.getKeyValue());
//
//            sheetNameVersionMap.forEach((String sheetNameVersionKey, Integer version) -> {
//                if (version.intValue() != systemVersion.intValue()) {
//                    switch (sheetNameVersionKey) {
//                        case SystemConstantUtil.FHW_SHEET_VERSION:
//                            loggedInUserPrincipleDto.setRetrievedXlsData(tenantCacheProviderForXlsDataFhw.get());
//                            break;
//                        case SystemConstantUtil.ASHA_SHEET_VERSION:
//                            loggedInUserPrincipleDto.setRetrievedXlsData(tenantCacheProviderForXlsDataAsha.get());
//                            break;
//                        case SystemConstantUtil.AWW_SHEET_VERSION:
//                            loggedInUserPrincipleDto.setRetrievedXlsData(tenantCacheProviderForXlsDataAww.get());
//                            break;
//                        default:
//                            return;
//                    }
//                    currentSheetVersionMap.put(sheetNameVersionKey, systemVersion);
//                    loggedInUserPrincipleDto.setCurrentSheetVersion(currentSheetVersionMap);
//                }
//            });
//        }
//    }

    @Override
    public Integer saveMobileNumberUpdateDetails(MobileNumberUpdationBean mobileNumberUpdationBean) {
        MemberEntity memberEntity = memberDao.retrieveById(mobileNumberUpdationBean.getMemberId());

        if (memberEntity == null) {
            throw new ImtechoMobileException("Member not found in the server.", 100);
        }

        memberEntity.setMobileNumber(mobileNumberUpdationBean.getMobileNumber());
        memberEntity.setIsMobileNumberVerified(Boolean.TRUE);

        if (mobileNumberUpdationBean.getWhatsappSmsSchems()) {
            MemberAdditionalInfo memberAdditionalInfo;
            Gson gson = new Gson();
            if (memberEntity.getAdditionalInfo() != null && !memberEntity.getAdditionalInfo().isEmpty()) {
                memberAdditionalInfo = gson.fromJson(memberEntity.getAdditionalInfo(), MemberAdditionalInfo.class);
            } else {
                memberAdditionalInfo = new MemberAdditionalInfo();
            }
            memberAdditionalInfo.setWhatsappConsentOn(new Date().getTime());
            memberEntity.setAdditionalInfo(gson.toJson(memberAdditionalInfo));
        }

        memberDao.updateMember(memberEntity);
        return memberEntity.getId();
    }

    @Override
    public void checkIfMemberDeathEntryExists(Integer memberId) {
        boolean deathEntryExist = memberDao.checkIfDeathEntryExists(memberId);
        if (deathEntryExist) {
            throw new ImtechoMobileException("Member has been marked as dead already", 100);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MenuDataBean> getMenuDetails(LogInRequestParamDetailDto paramDetailDto, int roleId) {
        if (paramDetailDto.getLastUpdateDateForMenuMaster() == null
                || paramDetailDto.getLastUpdateDateForMenuMaster() == 0L) {
            return mobileMenuMasterDao.retrieveAllMenusForMobile(null, roleId);
        } else {
            return mobileMenuMasterDao.retrieveAllMenusForMobile(new Date(paramDetailDto.getLastUpdateDateForMenuMaster()), roleId);
        }
    }
}
