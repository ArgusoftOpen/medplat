/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.service.impl;

import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.exception.ImtechoSystemException;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.mobile.dto.LocationMasterDataBean;
import com.argusoft.medplat.web.healthinfra.dao.HealthInfrastructureWardDetailsDao;
import com.argusoft.medplat.web.healthinfra.dao.HealthInfrastructureWardDetailsHisotryDao;
import com.argusoft.medplat.web.location.constants.LocationConstants;
import com.argusoft.medplat.web.location.dao.*;
import com.argusoft.medplat.web.location.dto.LocationDetailDto;
import com.argusoft.medplat.web.location.dto.LocationHierchyDto;
import com.argusoft.medplat.web.location.dto.LocationMasterDto;
import com.argusoft.medplat.web.location.mapper.LocationDetailMapper;
import com.argusoft.medplat.web.location.mapper.LocationMasterMapper;
import com.argusoft.medplat.web.location.model.LocationMaster;
import com.argusoft.medplat.web.location.service.LocationService;
import com.argusoft.medplat.web.users.dao.UserLocationDao;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * Define services for location.
 * </p>
 *
 * @author Harshit
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class LocationServiceImpl implements LocationService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LocationServiceImpl.class);

    @Autowired
    private ImtechoSecurityUser user;
    @Autowired
    private LocationMasterDao locationMasterDao;
    @Autowired
    private UserLocationDao userLocationDao;
    @Autowired
    private LocationHierchyCloserDetailDao locationHierchyCloserDetailDao;
    @Autowired
    private LocationWiseAnalyticsDao locationWiseAnalyticsDao;
    @Autowired
    private HealthInfrastructureWardDetailsDao healthInfrastructureWardDetailsDao;
    @Autowired
    private HealthInfrastructureWardDetailsHisotryDao healthInfrastructureWardDetailsHisotryDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationHierchyDto> retrieveLocationHierarchyDetailByCriteria(Integer userId, List<Integer> locationIds, Integer level, Boolean fetchAccordingToUserAOI, String languagePreference) {
        List<LocationMaster> locationMasters = null;
        LocationHierchyDto locationHierchyDto = new LocationHierchyDto();
        List<LocationDetailDto> locationDetailDtos = new LinkedList<>();
        if (Boolean.TRUE.equals(fetchAccordingToUserAOI)) {
            if (locationIds == null) {
                if (level == null) {
                    level = userLocationDao.getUserMinLevel(userId);
                }
                locationMasters = locationMasterDao.retrieveUserLocationByLevel(userId, level);
            } else {
                locationDetailDtos = locationMasterDao.retrieveUserLocationByParentLocation(userId, locationIds, level, languagePreference);
            }
        } else {
            if (level == null) {
                level = 1;
            }
            if (locationIds == null) {
                locationMasters = locationMasterDao.retrieveLocationByLevel(level);
            } else {
                locationDetailDtos = locationMasterDao.retrieveLocationByParentLocation(locationIds, level, languagePreference);

            }
        }
        if (!CollectionUtils.isEmpty(locationMasters)) {
            List<String> locationTypes = new LinkedList<>();
            for (LocationMaster locationMaster : locationMasters) {
                if (!locationTypes.contains(locationMaster.getHierarchyType().getName())) {
                    locationTypes.add(locationMaster.getHierarchyType().getName());
                }
                LocationDetailDto locationDetailDto = new LocationDetailDto();
                locationDetailDto.setId(locationMaster.getId());
                if (ConstantUtil.LAN_EN.equalsIgnoreCase(languagePreference)) {
                    locationDetailDto.setName(locationMaster.getEnglishName());
                } else {
                    locationDetailDto.setName(locationMaster.getName());
                }
                locationDetailDto.setType(locationMaster.getType());
                locationDetailDtos.add(locationDetailDto);
            }
            locationHierchyDto.setLevel(level);
            locationHierchyDto.setLocationDetails(locationDetailDtos);
            locationHierchyDto.setLocationLabel(String.join("/", locationTypes));
            List<LocationHierchyDto> locationHierchyDtos = new LinkedList<>();
            locationHierchyDtos.add(locationHierchyDto);
            return locationHierchyDtos;
        } else if (!CollectionUtils.isEmpty(locationDetailDtos)) {
            List<String> locationTypes = new LinkedList<>();
            for (LocationDetailDto locationDetailDto : locationDetailDtos) {
                if (!locationTypes.contains(locationDetailDto.getLocType())) {
                    locationTypes.add(locationDetailDto.getLocType());
                }
            }
            locationHierchyDto.setLevel(level);
            locationHierchyDto.setLocationDetails(locationDetailDtos);
            locationHierchyDto.setLocationLabel(String.join("/", locationTypes));
            List<LocationHierchyDto> locationHierchyDtos = new LinkedList<>();
            locationHierchyDtos.add(locationHierchyDto);
            return locationHierchyDtos;
        }
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationDetailDto> getLocationsByIds(Set<Integer> locationIds) {
        return LocationDetailMapper.entityToDtoLocationDetailList(locationMasterDao.getLocationsByIds(new LinkedList<>(locationIds)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createOrUpdate(LocationMasterDto locationMasterDto, Integer userId) {

        LocationMaster locationMaster = null;
        LocationMaster parentLocation = locationMasterDao.retrieveById(locationMasterDto.getParent());

        LocationMaster sameLocationDetail = locationMasterDao.retrieveLocationByParentIdAndName(locationMasterDto.getParent(), locationMasterDto.getName());
        if (sameLocationDetail != null && (locationMasterDto.getId() == null || !locationMasterDto.getId().equals(sameLocationDetail.getId()))) {
            throw new ImtechoUserException(locationMasterDto.getName() + " location already present under parent location " + parentLocation.getName() + ".", 101);
        }

        if (Boolean.FALSE.equals(locationMasterDao.isParentLocationIsCorrect(locationMasterDto.getParent(), locationMasterDto.getType()))) {
            throw new ImtechoSystemException("Parent location is not selected properly.", "locationMaster_Id : " + locationMasterDto.getId()
                    + ",Location Type : " + locationMasterDto.getType()
                    + ",Parent location Id : " + parentLocation.getId()
                    + ",Parent location Type : " + parentLocation.getType(),
                    null);
        }

        if (locationMasterDto.getId() != null) {
            locationMaster = locationMasterDao.retrieveById(locationMasterDto.getId());
        }
        LocationMaster locationMaster1 = LocationMasterMapper.getLocationMasterEntity(locationMasterDto, locationMaster, userId);
        locationMaster1.setParentMaster(parentLocation);
        locationMasterDao.createOrUpdate(locationMaster1);
//        LocationMobileFeature retrievedLocationMobileFeature = locationMobileFeatureDao.retrieveByLocationId(locationMaster1.getId());
//        if (retrievedLocationMobileFeature != null) {
//            if (retrievedLocationMobileFeature.getFeature().equals(ConstantUtil.CEREBRAL_PALSY_SCREENING)) {
//                if (Boolean.FALSE.equals(locationMaster1.getCerebralPalsyModule())) {
//                    locationMobileFeatureDao.delete(retrievedLocationMobileFeature);
//                }
//            } else {
//                if (Boolean.FALSE.equals(locationMaster1.getGeoFencing())) {
//                    locationMobileFeatureDao.delete(retrievedLocationMobileFeature);
//                }
//            }
//        } else {
//            if (Boolean.TRUE.equals(locationMaster1.getCerebralPalsyModule())) {
//                LocationMobileFeature locationMobileFeature = new LocationMobileFeature();
//                locationMobileFeature.setFeature(ConstantUtil.CEREBRAL_PALSY_SCREENING);
//                locationMobileFeature.setLocationId(locationMaster1.getId());
//                locationMobileFeatureDao.create(locationMobileFeature);
//            }
//            if (Boolean.TRUE.equals(locationMaster1.getGeoFencing())) {
//                LocationMobileFeature locationMobileFeature = new LocationMobileFeature();
//                locationMobileFeature.setFeature(ConstantUtil.GEO_FENCING);
//                locationMobileFeature.setLocationId(locationMaster1.getId());
//                locationMobileFeatureDao.create(locationMobileFeature);
//            }
//        }

//        LocationLevelHierarchy locationLevelHierarchy = this.createOrUpdateLocationLevelHierarchy(locationMaster1);
//        updateLocationMaster(locationMasterDto, locationLevelHierarchy, locationMaster1);
    }

//    /**
//     * Update location master details.
//     *
//     * @param locationMasterDto      Location master details.
//     * @param locationLevelHierarchy Location hierarchy details.
//     * @param locationMaster1        Location master entity.
//     */
//    private void updateLocationMaster(LocationMasterDto locationMasterDto, LocationLevelHierarchy locationLevelHierarchy,
//                                      LocationMaster locationMaster1) {
//        if (locationMasterDto.getId() == null) {
//            locationMaster1.setLocationHierarchy(locationLevelHierarchy);
//            locationMasterDao.update(locationMaster1);
//        }
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationMasterDto> retrieveChildLocationsByLocationId(Integer locationId) {
        List<LocationMaster> childLocationMasters = locationMasterDao.retrieveLocationByParentLocation(locationId);
        List<LocationMasterDto> childLocationMasterDtos = LocationMasterMapper.getLocationMasterDtoList(childLocationMasters);
        String parentString;
        if (locationId == -1) {
            parentString = "Gujarat";
        } else {
            parentString = locationHierchyCloserDetailDao.getLocationHierarchyStringByLocationId(locationId);
        }
        for (LocationMasterDto locationMasterDto : childLocationMasterDtos) {
            locationMasterDto.setLocationHierarchy(parentString.concat(">").concat(locationMasterDto.getName()));
        }
        return childLocationMasterDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationMasterDto retrieveById(Integer id) {
        List<LocationMasterDto> result = LocationMasterMapper.getLocationMasterDtoList(Arrays.asList(locationMasterDao.retrieveById(id)));
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return new LocationMasterDto();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAllActiveLocationsWithWorkerInfo() {
        System.out.println("Updating Location Master Beans For Mobile");
        List<LocationMasterDataBean> locations = locationMasterDao.retrieveAllActiveLocationsWithWorkerInfo(null);
        locations.forEach((locationMasterMobile) -> {
            locationMasterMobile.setLevel(LocationConstants.getLocationLevel(locationMasterMobile.getType()));
        });
        LocationConstants.allLocationMasterDataBeans = locations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationMasterDto retrieveLocationHierarchyById(Integer locationId) {
        LocationMasterDto locationMasterDto = new LocationMasterDto();
        locationMasterDto.setName(locationMasterDao.retrieveLocationHierarchyById(locationId));
        locationMasterDto.setId(locationId);
        return locationMasterDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationMasterDto retrieveEngLocationHierarchyById(Integer locationId) {
        LocationMasterDto locationMasterDto = new LocationMasterDto();
        locationMasterDto.setName(locationMasterDao.retrieveEngLocationHierarchyById(locationId));
        locationMasterDto.setId(locationId);
        return locationMasterDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationMaster> getLocationsByLocationType(List<String> locationLevel, Boolean isActive) {
        return locationMasterDao.getLocationsByLocationType(locationLevel, isActive);
    }

    /**
     * {@inheritDoc}
     */
//    @Override
//    public Map<Integer, KpiRecord> retrieveKpiRecordForAllVillages() {
//        Map<Integer, KpiRecord> kpiRecordMap = new HashMap<>();
//        Map<Integer, KpiParams> kpiParamsMapOral = new HashMap<>();
//        Map<Integer, KpiParams> kpiParamsMapCervical = new HashMap<>();
//        Map<Integer, KpiParams> kpiParamsMapBreast = new HashMap<>();
//        Map<Integer, KpiParams> kpiParamsMapHypertension = new HashMap<>();
//        Map<Integer, KpiParams> kpiParamsMapDiabetes = new HashMap<>();
//        Map<Integer, KpiParams> aggregateKpiParamsMap = new HashMap<>();
//
//        List<KpiRecord> kpiRecords = locationMasterDao.retrieveKpiRecordForVillages(2); //Static ID For GUJARAT
//
//        List<KpiParams> kpiParamsBreast = locationMasterDao.retrieveKpiParamsForBreast(2);
//        List<KpiParams> kpiParamsOral = locationMasterDao.retrieveKpiParamsForOral(2);
//        List<KpiParams> kpiParamsCervical = locationMasterDao.retrieveKpiParamsForCervical(2);
//        List<KpiParams> kpiParamsHypertension = locationMasterDao.retrieveKpiParamsForHypertension(2);
//        List<KpiParams> kpiParamsDiabetes = locationMasterDao.retrieveKpiParamsForDiabetes(2);
//        List<KpiParams> aggregateKpiParams = locationMasterDao.retrieveAggregateKpiParams(2);
//
//        if (CollectionUtils.isEmpty(kpiRecords)) {
//            return null;
//        }
//
//        kpiParamsBreast.forEach(kpiParam ->
//                kpiParamsMapBreast.put(kpiParam.getLocationId(), kpiParam)
//        );
//
//        kpiParamsOral.forEach(kpiParam ->
//                kpiParamsMapOral.put(kpiParam.getLocationId(), kpiParam)
//        );
//
//        kpiParamsCervical.forEach(kpiParam ->
//                kpiParamsMapCervical.put(kpiParam.getLocationId(), kpiParam)
//        );
//
//        kpiParamsHypertension.forEach(kpiParam ->
//                kpiParamsMapHypertension.put(kpiParam.getLocationId(), kpiParam)
//        );
//
//        kpiParamsDiabetes.forEach(kpiParam ->
//                kpiParamsMapDiabetes.put(kpiParam.getLocationId(), kpiParam)
//        );
//
//        aggregateKpiParams.forEach(kpiParam ->
//                aggregateKpiParamsMap.put(kpiParam.getLocationId(), kpiParam)
//        );
//
//        for (KpiRecord kpiRecord : kpiRecords) {
//            List<KpiParams> kpiParamsList = new LinkedList<>();
//            kpiParamsList.add(kpiParamsMapBreast.get(kpiRecord.getLocationId()));
//            kpiParamsList.add(kpiParamsMapOral.get(kpiRecord.getLocationId()));
//            kpiParamsList.add(kpiParamsMapCervical.get(kpiRecord.getLocationId()));
//            kpiParamsList.add(kpiParamsMapHypertension.get(kpiRecord.getLocationId()));
//            kpiParamsList.add(kpiParamsMapDiabetes.get(kpiRecord.getLocationId()));
//
//            kpiRecord.setAggregateKpiParams(aggregateKpiParamsMap.get(kpiRecord.getLocationId()));
//            kpiRecord.setDiseaseWiseKpiParams(kpiParamsList);
//        }
//
//        kpiRecords.forEach(kpiRecord ->
//                kpiRecordMap.put(kpiRecord.getLocationId(), kpiRecord)
//        );
//
//        return kpiRecordMap;
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationMaster> getAllLocationsOfGujarat() {
        List<Integer> parentIds = new LinkedList<>();
        List<String> childLocationsType = new LinkedList<>();
        childLocationsType.add(LocationConstants.LocationType.STATE);
        childLocationsType.add(LocationConstants.LocationType.DISTRICT);
        childLocationsType.add(LocationConstants.LocationType.CORPORATION);
        childLocationsType.add(LocationConstants.LocationType.BLOCK);
        childLocationsType.add(LocationConstants.LocationType.ZONE);
        childLocationsType.add(LocationConstants.LocationType.PHC);
        childLocationsType.add(LocationConstants.LocationType.UHC);
        childLocationsType.add(LocationConstants.LocationType.SUBCENTER);
        childLocationsType.add(LocationConstants.LocationType.ANM_AREA);
        childLocationsType.add(LocationConstants.LocationType.VILLAGE);
        childLocationsType.add(LocationConstants.LocationType.ANGANWADI_AREA);
        parentIds.add(2);
        return locationMasterDao.retrieveLocationsByParentIdAndType(parentIds, childLocationsType);
    }

//    public void resetAggregateData(KpiParams aggregateKpiParams) {
//        aggregateKpiParams.setAnmFullyScreened(0);
//        aggregateKpiParams.setAnmFullyScreenedMale(0);
//        aggregateKpiParams.setAnmFullyScreenedFemale(0);
//        aggregateKpiParams.setAnmFullyScreenedOther(0);
//
//        aggregateKpiParams.setReferred(0);
//        aggregateKpiParams.setReferredMale(0);
//        aggregateKpiParams.setReferredFemale(0);
//        aggregateKpiParams.setReferredOther(0);
//
//        aggregateKpiParams.setPhcReferredByAnm(0);
//        aggregateKpiParams.setPhcReferredByAnmMale(0);
//        aggregateKpiParams.setPhcReferredByAnmFemale(0);
//        aggregateKpiParams.setPhcReferredByAnmOther(0);
//
//        aggregateKpiParams.setPhcReferredByAshaAnm(0);
//        aggregateKpiParams.setPhcReferredByAshaAnmMale(0);
//        aggregateKpiParams.setPhcReferredByAshaAnmFemale(0);
//        aggregateKpiParams.setPhcReferredByAshaAnmOther(0);
//
//        aggregateKpiParams.setSecondaryReferred(0);
//        aggregateKpiParams.setSecondaryReferredMale(0);
//        aggregateKpiParams.setSecondaryReferredFemale(0);
//        aggregateKpiParams.setSecondaryReferredOther(0);
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationDetailDto retrieveParentLocationDetail(Integer locationId, String languagePreference) {
        return locationMasterDao.getParentLocationDetail(locationId, languagePreference);
    }

}
