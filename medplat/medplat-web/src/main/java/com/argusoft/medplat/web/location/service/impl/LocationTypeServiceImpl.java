/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.service.impl;

import com.argusoft.medplat.web.location.dao.LocationTypeMasterDao;
import com.argusoft.medplat.web.location.dto.LocationTypeMasterDto;
import com.argusoft.medplat.web.location.mapper.LocationTypeMasterMapper;
import com.argusoft.medplat.web.location.model.LocationTypeMaster;
import com.argusoft.medplat.web.location.service.LocationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 *     Define services for location.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
@Transactional
@Service
public class LocationTypeServiceImpl implements LocationTypeService {

    @Autowired
    LocationTypeMasterDao locationTypeMasterDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationTypeMasterDto> retrieveAll() {
        return LocationTypeMasterMapper.convertMasterListToDtoList(locationTypeMasterDao.retrieveAll());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationTypeMasterDto retrieveLocationType(String type) {
        return LocationTypeMasterMapper.convertMasterToDto(locationTypeMasterDao.retrieveLocationType(type));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocationTypeMaster> retrieveLocationTypeMasterByModifiedOn(Long modifiedOn) {
        if (modifiedOn != null && modifiedOn != 0L) {
            return locationTypeMasterDao.retrieveLocationTypeMasterByModifiedOn(new Date(modifiedOn));
        } else {
            return locationTypeMasterDao.retrieveLocationTypeMasterByModifiedOn(null);
        }
    }


}
