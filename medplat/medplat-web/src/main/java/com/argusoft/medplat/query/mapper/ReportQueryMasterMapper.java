/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.query.mapper;

import com.argusoft.medplat.event.util.EventFunctionUtil;
import com.argusoft.medplat.query.dto.ReportQueryMasterDto;
import com.argusoft.medplat.query.model.ReportQueryMaster;

import java.util.LinkedList;
import java.util.List;

/**
 * An util class for report query to convert modal to dto or dto to modal
 * @author vaishali
 * @since 02/09/2020 10:30
 */
public class ReportQueryMasterMapper {

    private ReportQueryMasterMapper(){
    }

    /**
     * Converts report query master to dto
     * @param queryMasterDto An instance of ReportQueryMasterDto
     * @return An instance of ReportQueryMaster
     */
    public static ReportQueryMaster convertDtoToMaster(ReportQueryMasterDto queryMasterDto) {
        ReportQueryMaster queryMaster = new ReportQueryMaster();
        queryMaster.setQuery(queryMasterDto.getQuery());
        String findParamsFromTemplate = EventFunctionUtil.findParamsFromTemplate(queryMasterDto.getQuery());
        queryMaster.setParams(findParamsFromTemplate);
        queryMaster.setId(queryMasterDto.getId());        
        queryMaster.setReturnsResultSet(queryMasterDto.getReturnsResultSet());
        queryMaster.setCreatedBy(queryMasterDto.getCreatedBy());
        queryMaster.setCreatedOn(queryMasterDto.getCreatedOn());
        queryMaster.setState(queryMasterDto.getState() == null ? ReportQueryMaster.State.ACTIVE : queryMasterDto.getState());
        queryMaster.setUuid(queryMasterDto.getUuid());

        return queryMaster;

    }

    /**
     * Converts report query dto to master
     * @param queryMaster An instance of ReportQueryMaster
     * @return An instance of ReportQueryMasterDto
     */
    public static ReportQueryMasterDto convertMasterToDto(ReportQueryMaster queryMaster) {
        ReportQueryMasterDto queryMasterDto = new ReportQueryMasterDto();
        
        if (queryMaster != null && queryMaster.getId() != null) {
            queryMasterDto.setId(queryMaster.getId());
            queryMasterDto.setQuery(queryMaster.getQuery());
            queryMasterDto.setCreatedBy(queryMaster.getCreatedBy());
            queryMasterDto.setCreatedOn(queryMaster.getCreatedOn());
            queryMasterDto.setState(queryMaster.getState());
            queryMasterDto.setReturnsResultSet(queryMaster.getReturnsResultSet());
            queryMasterDto.setParams(queryMaster.getParams());
            queryMasterDto.setUuid(queryMaster.getUuid());
        }
        return queryMasterDto;

    }
    /**
     * Converts a list of report query master to dto
     * @param queryMasters A list of ReportQueryMaster
     * @return A list of ReportQueryMasterDto
     */
    public static List<ReportQueryMasterDto> convertMasterListToDto(List<ReportQueryMaster> queryMasters) {
        List<ReportQueryMasterDto> queryMasterDtos = new LinkedList<>();
        queryMasters.stream().forEach(queryMaster ->
            queryMasterDtos.add(convertMasterToDto(queryMaster))
        );
        return queryMasterDtos;
    }
}
