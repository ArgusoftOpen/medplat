/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.query.mapper;

import com.argusoft.medplat.event.util.EventFunctionUtil;
import com.argusoft.medplat.query.dto.QueryMasterDto;
import com.argusoft.medplat.query.model.QueryMaster;

import java.util.LinkedList;
import java.util.List;

/**
 * An util class for query to convert modal to dto or dto to modal
 * @author vaishali
 * @since 02/09/2020 10:30
 */
public class QueryMasterMapper {

    private QueryMasterMapper(){
    }

    /**
     * Converts query dto to master
     * @param queryMasterDto An instance of QueryMasterDto
     * @return An instance of QueryMaster
     */
    public static QueryMaster convertDtoToMaster(QueryMasterDto queryMasterDto) {
        QueryMaster queryMaster = new QueryMaster();
        queryMaster.setCode(queryMasterDto.getCode());
        queryMaster.setQuery(queryMasterDto.getQuery());
        String findParamsFromTemplate = EventFunctionUtil.findParamsFromTemplate(queryMasterDto.getQuery());
        queryMaster.setParams(findParamsFromTemplate);
        queryMaster.setReturnsResultSet(queryMasterDto.getReturnsResultSet());
        queryMaster.setCreatedBy(queryMasterDto.getCreatedBy());
        queryMaster.setCreatedOn(queryMasterDto.getCreatedOn());
        queryMaster.setDescription(queryMasterDto.getDescription());
        queryMaster.setState(queryMasterDto.getState() == null ? QueryMaster.State.ACTIVE : queryMasterDto.getState());
        queryMaster.setIsPublic(queryMasterDto.getIsPublic());
        queryMaster.setUuid(queryMasterDto.getUuid());

        return queryMaster;
    }

    /**
     * Converts query master to dto
     * @param queryMaster An instance of QueryMaster
     * @return An instance of QueryMasterDto
     */
    public static QueryMasterDto convertMasterToDto(QueryMaster queryMaster) {
        QueryMasterDto queryMasterDto = new QueryMasterDto();
        queryMasterDto.setCode(queryMaster.getCode());
        queryMasterDto.setQuery(queryMaster.getQuery());
        queryMasterDto.setCreatedBy(queryMaster.getCreatedBy());
        queryMasterDto.setCreatedOn(queryMaster.getCreatedOn());
        queryMasterDto.setState(queryMaster.getState());
        queryMasterDto.setReturnsResultSet(queryMaster.getReturnsResultSet());
        queryMasterDto.setParams(queryMaster.getParams());
        queryMasterDto.setDescription(queryMaster.getDescription());
        queryMasterDto.setIsPublic(queryMaster.getIsPublic());
        queryMasterDto.setUuid(queryMaster.getUuid());

        return queryMasterDto;
    }

    /**
     * Converts a list of query master to dto
     * @param queryMasters A list of QueryMaster
     * @return A list of QueryMasterDto
     */
    public static List<QueryMasterDto> convertMasterListToDto(List<QueryMaster> queryMasters) {
        List<QueryMasterDto> queryMasterDtos = new LinkedList<>();
        queryMasters.stream().forEach(queryMaster ->
            queryMasterDtos.add(convertMasterToDto(queryMaster))
        );

        return queryMasterDtos;
    }
}
