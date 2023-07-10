/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.service.impl;

import com.argusoft.medplat.rch.dao.FacilityPerformanceMasterDao;
import com.argusoft.medplat.rch.dto.FacilityPerformanceMasterDto;
import com.argusoft.medplat.rch.mapper.FacilityPerformanceMapper;
import com.argusoft.medplat.rch.model.FacilityPerformanceMaster;
import com.argusoft.medplat.rch.service.FacilityPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * Define services for facility performance.
 * </p>
 *
 * @author vivek
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class FacilityPerformanceServiceImpl implements FacilityPerformanceService {

    @Autowired
    FacilityPerformanceMasterDao facilityPerformanceMasterDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createOrUpdate(FacilityPerformanceMasterDto facilityPerformanceMasterDto) {
        if (facilityPerformanceMasterDto.getId() != null) {
            FacilityPerformanceMaster facilityPerformanceMaster = facilityPerformanceMasterDao.retrieveById(facilityPerformanceMasterDto.getId());
            facilityPerformanceMaster.setNoOfOpdAttended(facilityPerformanceMasterDto.getNoOfOpdAttended());
            facilityPerformanceMaster.setNoOfIpdAttended(facilityPerformanceMasterDto.getNoOfIpdAttended());
            facilityPerformanceMaster.setNoOfDeliveresConducted(facilityPerformanceMasterDto.getNoOfDeliveresConducted());
            facilityPerformanceMaster.setNoOfSectionConducted(facilityPerformanceMasterDto.getNoOfSectionConducted());
            facilityPerformanceMaster.setNoOfMajorOperationConducted(facilityPerformanceMasterDto.getNoOfMajorOperationConducted());
            facilityPerformanceMaster.setNoOfMinorOperationConducted(facilityPerformanceMasterDto.getNoOfMinorOperationConducted());
            facilityPerformanceMaster.setNoOfLaboratoryTestConducted(facilityPerformanceMasterDto.getNoOfLaboratoryTestConducted());
            facilityPerformanceMasterDao.update(facilityPerformanceMaster);
        } else {
            FacilityPerformanceMaster facilityPerformanceMaster = FacilityPerformanceMapper.convertDtoToEntity(facilityPerformanceMasterDto);
            facilityPerformanceMasterDao.create(facilityPerformanceMaster);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FacilityPerformanceMasterDto getFacilityPerformaceByHidAndDate(Integer hid, Date perfromanceDate) {
        FacilityPerformanceMaster facilityPerformanceMaster = facilityPerformanceMasterDao.getFacilityPerformaceByHidAndDate(hid, perfromanceDate);
        if (facilityPerformanceMaster != null) {
            return FacilityPerformanceMapper.convertEntityToDto(facilityPerformanceMaster);
        } else {
            return null;
        }
    }
}
