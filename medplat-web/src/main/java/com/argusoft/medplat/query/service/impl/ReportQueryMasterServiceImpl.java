/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.query.service.impl;

import com.argusoft.medplat.query.dao.ReportQueryMasterDao;
import com.argusoft.medplat.query.dto.ReportQueryMasterDto;
import com.argusoft.medplat.query.mapper.ReportQueryMasterMapper;
import com.argusoft.medplat.query.model.ReportQueryMaster;
import com.argusoft.medplat.query.service.ReportQueryMasterService;
import com.argusoft.medplat.query.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implements methods of ReportQueryMasterService
 * @author vaishali
 * @since 02/09/2020 10:30
 */
@Transactional
@Service
public class ReportQueryMasterServiceImpl implements ReportQueryMasterService {

    @Autowired
    ReportQueryMasterDao reportQueryMasterDao;

    @Autowired
    TableService tableService;

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Integer createOrUpdate(ReportQueryMasterDto queryMasterDto) {
        if (queryMasterDto.getId() != null) {
            reportQueryMasterDao.merge(ReportQueryMasterMapper.convertDtoToMaster(queryMasterDto));
            return queryMasterDto.getId();
        } else {
            return reportQueryMasterDao.create(ReportQueryMasterMapper.convertDtoToMaster(queryMasterDto));
        }

    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public ReportQueryMasterDto retrieveById(Integer id) {
        ReportQueryMaster queryMaster = reportQueryMasterDao.retrieveById(id);
        return ReportQueryMasterMapper.convertMasterToDto(queryMaster);
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public ReportQueryMasterDto retrieveByUUID(UUID uuid) {
        ReportQueryMaster queryMaster = reportQueryMasterDao.retrieveByUUID(uuid);
        return ReportQueryMasterMapper.convertMasterToDto(queryMaster);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public List<ReportQueryMasterDto> retrieveAll(Boolean isActive) {
        return ReportQueryMasterMapper.convertMasterListToDto(reportQueryMasterDao.retrieveAll(isActive));
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void toggleActive(ReportQueryMasterDto queryMasterDto) {
        queryMasterDto.setState(queryMasterDto.getState() == ReportQueryMaster.State.ACTIVE ? ReportQueryMaster.State.INACTIVE : ReportQueryMaster.State.ACTIVE);
        reportQueryMasterDao.update(ReportQueryMasterMapper.convertDtoToMaster(queryMasterDto));
    }    


}
