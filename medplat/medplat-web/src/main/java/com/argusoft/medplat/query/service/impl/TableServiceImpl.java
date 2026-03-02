/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.query.service.impl;

import com.argusoft.medplat.event.dto.EventConfigTypeDto;
import com.argusoft.medplat.event.dto.QueryMasterParamterDto;
import com.argusoft.medplat.event.util.EventFunctionUtil;
import com.argusoft.medplat.query.dao.QueryMasterDao;
import com.argusoft.medplat.query.dao.TableDao;
import com.argusoft.medplat.query.dto.QueryMasterDto;
import com.argusoft.medplat.query.mapper.QueryMasterMapper;
import com.argusoft.medplat.query.model.QueryMaster;
import com.argusoft.medplat.query.service.QueryMasterService;
import com.argusoft.medplat.query.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements methods of TableService
 * @author hshah
 * @since 02/09/2020 10:30
 */
@Service
@Transactional
public class TableServiceImpl implements TableService {

    @Autowired
    private TableDao tableDao;
    @Autowired
    private QueryMasterDao queryMasterDao;


    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public List<Map<String, Object>> executeQueryForCombo(String query, String reportName) {
        return tableDao.executeQueryForCombo(query, reportName);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public List<LinkedHashMap<String, Object>> executeQuery(String query) {
        return tableDao.executeQuery(query);
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public List<LinkedHashMap<String, Object>> execute(String query, LinkedHashMap<String, Object> parameterValue) {
        return tableDao.execute(query, parameterValue);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void executeUpdate(String query) {
        tableDao.executeUpdate(query);
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void executeUpdateQuery(String query, LinkedHashMap<String, Object> parameterValue) {
        tableDao.executeUpdateQuery(query, parameterValue);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void queryHandler(EventConfigTypeDto notificationConfigTypeDto, List<LinkedHashMap<String, Object>> queryDataLists) {
        if (notificationConfigTypeDto.getQueryCode() == null) {
            for (LinkedHashMap<String, Object> queryDataList : queryDataLists) {
                String query = notificationConfigTypeDto.getTemplate();
                String replaceParameterWithValue = EventFunctionUtil.replaceParameterWithValue(query, notificationConfigTypeDto.getTemplateParameter(), queryDataList);
                tableDao.executeUpdate(replaceParameterWithValue);
            }
        } else {
            handleQueryWithCode(notificationConfigTypeDto,queryDataLists);
        }
    }

    private void handleQueryWithCode(EventConfigTypeDto notificationConfigTypeDto, List<LinkedHashMap<String, Object>> queryDataLists) {
        for (LinkedHashMap<String, Object> queryDataList : queryDataLists) {
            QueryMaster queryMaster = queryMasterDao.retrieveByCode(notificationConfigTypeDto.getQueryCode());
            QueryMasterDto queryMasterDto = QueryMasterMapper.convertMasterToDto(queryMaster);
            String query = queryMasterDto.getQuery();
                for (QueryMasterParamterDto queryMasterParamterDto : notificationConfigTypeDto.getQueryMasterParamJson()) {
                    if (Boolean.FALSE.equals(queryMasterParamterDto.getIsFixed())) {
                        Object value = queryDataList.get(queryMasterParamterDto.getMappingValue());
                        String val = value == null ? "null" : value.toString();
                        query = query.replace("#" + queryMasterParamterDto.getParameterName() + "#", val);
                    } else {
                        query = query.replace("#" + queryMasterParamterDto.getParameterName() + "#", queryMasterParamterDto.getMappingValue());
                    }
                }
            tableDao.executeUpdate(query);
        }
    }

    @Override
    public void executeUpdateQueryWithoutCast(String query, LinkedHashMap<String, Object> parameterValue) {
        tableDao.executeUpdateQueryWithoutCast(query, parameterValue);
    }
}
