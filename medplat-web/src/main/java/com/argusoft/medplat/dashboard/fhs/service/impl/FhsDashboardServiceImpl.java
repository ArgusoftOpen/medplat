/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.dashboard.fhs.service.impl;

import com.argusoft.medplat.web.users.dao.UserDao;
import com.argusoft.medplat.common.service.SystemConfigurationService;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.dashboard.fhs.constants.FamilyHealthSurveyServiceConstants;
import com.argusoft.medplat.dashboard.fhs.service.FhsDashboardService;
import com.argusoft.medplat.dashboard.fhs.view.DisplayObject;
import com.argusoft.medplat.fhs.dao.FamilyDao;
import com.argusoft.medplat.fhs.dto.FhsVillagesDto;
import com.argusoft.medplat.fhs.dto.StarPerformersOfTheDayDto;
import com.argusoft.medplat.web.location.dao.LocationMasterDao;
import com.argusoft.medplat.web.location.model.LocationMaster;
import com.argusoft.medplat.web.users.dao.UserLocationDao;
import com.argusoft.medplat.web.users.model.UserLocation;
import com.argusoft.medplat.web.users.model.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>Family dashboard service implementation</p>
 * @author kunjan
 * @since 27/08/20 1:00 PM
 *
 */
@Service
@Transactional
public class  FhsDashboardServiceImpl implements FhsDashboardService {

    @Autowired
    private FamilyDao familyDao;

    @Autowired
    private LocationMasterDao locationMasterDao;
    @Autowired
    private ImtechoSecurityUser user;

    @Autowired
    private UserLocationDao userLocationDao;

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private SystemConfigurationService systemConfigurationService;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DisplayObject> familiesAndMembers(Integer locationId) {
        List<Integer> locationList = new LinkedList<>();
        locationList.add(locationId);

        List<LocationMaster> locations = locationMasterDao.retrieveLocationsByIdList(locationList);
        LocationMaster location = locations.get(0);

        return familyDao.getFamiliesAndMembersForChildLocations(locationId,
                FamilyHealthSurveyServiceConstants.LOCATION_TYPES_AND_LEVELS.get(location.getType()), user.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DisplayObject> familiesAndMembersByLocationId(Integer locationId) {

        List<Integer> locationList = new LinkedList<>();
        locationList.add(locationId);

        List<LocationMaster> locations = locationMasterDao.retrieveLocationsByIdList(locationList);
        LocationMaster location = locations.get(0);
        return familyDao.familiesAndMembersByLocationId(locationId,
                FamilyHealthSurveyServiceConstants.LOCATION_TYPES_AND_LEVELS.get(location.getType()), user.getId());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FhsVillagesDto> getFhsVillagesByUserId(Integer userId) {
        return familyDao.getFhsVillagesByUserId(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StarPerformersOfTheDayDto> starPerformersOfTheDay() {

        List<StarPerformersOfTheDayDto> views = familyDao.starPerformersOfTheDay();

        if (!views.isEmpty()) {
            StarPerformersOfTheDayDto starPerformersOfTheDayDto = views.get(0);
            Integer userId = starPerformersOfTheDayDto.getUserid();

            UserMaster userObj = userDao.retrieveById(userId);
            String userName = userObj.getFirstName() + " " + userObj.getLastName();
            starPerformersOfTheDayDto.setUserName(userName);

            List<Integer> locationIds = new ArrayList<>();
            List<String> locationNames = new ArrayList<>();

            List<UserLocation> userLocations = userLocationDao.retrieveLocationsByUserId(userId);
            userLocations.forEach(userLocation ->
                locationIds.add(userLocation.getLocationId())
            );

            List<LocationMaster> locations = locationMasterDao.retrieveLocationsByIdList(locationIds);
            locations.forEach(lm ->
                locationNames.add(lm.getName())
            );
            starPerformersOfTheDayDto.setLocationNameList(locationNames);
        }

        return views;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLastUpdateTime() {
        return systemConfigurationService
                .retrieveSystemConfigurationByKey(com.argusoft.medplat.common.util.ConstantUtil.FHS_LAST_UPDATE_TIME_SYSTEM_KEY).getKeyValue();
    }

}
