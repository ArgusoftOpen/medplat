/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.query.service.impl;

import com.argusoft.medplat.common.service.SystemConfigSyncService;
import com.argusoft.medplat.common.util.AESEncryption;
import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.event.util.EventFunctionUtil;
import com.argusoft.medplat.exception.ImtechoSystemException;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.query.dao.QueryMasterDao;
import com.argusoft.medplat.query.dto.QueryDto;
import com.argusoft.medplat.query.dto.QueryMasterDto;
import com.argusoft.medplat.query.mapper.QueryMasterMapper;
import com.argusoft.medplat.query.model.QueryMaster;
import com.argusoft.medplat.query.service.QueryMasterService;
import com.argusoft.medplat.query.service.TableService;
import com.argusoft.medplat.web.users.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Implements methods of QueryMasterService
 *
 * @author vaishali
 * @since 02/09/2020 10:30
 */
@Transactional
@Service
public class QueryMasterServiceImpl implements QueryMasterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryMasterServiceImpl.class);

    @Autowired
    QueryMasterDao queryMasterDao;

    @Autowired
    private SystemConfigSyncService systemConfigSyncService;

    @Autowired
    TableService tableService;

    @Autowired
    private ImtechoSecurityUser user;

    @Autowired
    UserService userService;

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID createOrUpdate(QueryMasterDto queryMasterDto, boolean isMetodCallBySyncFunction) {
        if (Boolean.TRUE.equals(queryMasterDao.isCodeAvailable(queryMasterDto.getCode(), queryMasterDto.getUuid()))) {

            if (!isMetodCallBySyncFunction) {
                queryMasterDto.setCreatedBy(user.getId());
            } else {
                queryMasterDto.setCreatedBy(-1);
            }

            if (queryMasterDto.getUuid() != null) {
                queryMasterDao.merge(QueryMasterMapper.convertDtoToMaster(queryMasterDto));

            } else {
                queryMasterDto.setUuid(UUID.randomUUID());
                queryMasterDao.create(QueryMasterMapper.convertDtoToMaster(queryMasterDto));

            }
            if (!isMetodCallBySyncFunction) {
                systemConfigSyncService.createOrUpdate(queryMasterDto, ConstantUtil.SYNC_QUERY_BUILDER);

            }
            return queryMasterDto.getUuid();
        } else {
            throw new ImtechoUserException("This code is already registered. Please enter another", 101);
        }
    }

//    This API endpoint isn't used anywhere in web.If somewhere it is using then need to pass UUID instead of ID

    /**
     * {@inheritDoc}
     */
    @Override
    public QueryMasterDto retrieveById(Integer id) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QueryMasterDto retrieveByCode(String code) {
        QueryMaster queryMaster = queryMasterDao.retrieveByCode(code);
        return QueryMasterMapper.convertMasterToDto(queryMaster);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QueryMasterDto retrieveByUUID(UUID uuid) {
        QueryMaster queryMaster = queryMasterDao.retrieveByUUID(uuid);
        if (queryMaster != null) {
            return QueryMasterMapper.convertMasterToDto(queryMaster);
        }
        return null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<QueryMasterDto> retrieveAll(Boolean isActive) {
        return QueryMasterMapper.convertMasterListToDto(queryMasterDao.retrieveAll(isActive));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toggleActive(QueryMasterDto queryMasterDto) {
        queryMasterDto.setState(queryMasterDto.getState() == QueryMaster.State.ACTIVE ? QueryMaster.State.INACTIVE : QueryMaster.State.ACTIVE);
        queryMasterDao.update(QueryMasterMapper.convertDtoToMaster(queryMasterDto));
        systemConfigSyncService.createOrUpdate(queryMasterDto, ConstantUtil.SYNC_QUERY_BUILDER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<QueryDto> executeQuery(List<QueryDto> queryDtos, boolean isSecure) {
        queryDtos.sort(Comparator.comparing(QueryDto::getSequence));
        List<QueryDto> resultQueryDtos = new LinkedList<>();
        queryDtos.stream().map(queryDto -> {
            QueryMaster queryMaster = queryMasterDao.retrieveByCode(queryDto.getCode());

            checkIsSecure(isSecure, queryMaster, queryDto);
            if (queryMaster.getCode().equals("dr_techo_retrieve_by_aadhar_number")
                    && queryDto.getParameters().get(QueryMaster.Fields.AADHAR_NUMBER) != null) {
                String encryptedAadharNumber = AESEncryption.getInstance().encrypt(queryDto.getParameters().get(QueryMaster.Fields.AADHAR_NUMBER).toString());
                queryDto.getParameters().put(QueryMaster.Fields.AADHAR_NUMBER, encryptedAadharNumber);
            }
            QueryDto queryResultDto = new QueryDto();
            queryResultDto.setCode(queryDto.getCode());
            queryResultDto.setSequence(queryDto.getSequence());
            String replacedQuery = queryMaster.getQuery();
            setDefaultParams(queryDto.getParameters());
            if (queryDto.getParameters() != null && !queryDto.getParameters().isEmpty()) {
                replacedQuery = EventFunctionUtil.replaceParameterWithValue(queryMaster.getQuery(), queryMaster.getParams(), queryDto.getParameters());
            }
            try {
                if (queryMaster.getReturnsResultSet() != null && queryMaster.getReturnsResultSet()) {
                    List<LinkedHashMap<String, Object>> executeQuery = tableService.executeQuery(replacedQuery);
                    queryResultDto.setResult(executeQuery);
                } else {
                    tableService.executeUpdate(replacedQuery);
                }
            } catch (Exception e) {
                LOGGER.error("Exception In Query");
                String queryWithCode = "Query Code : " + queryDto.getCode() + "\nQuery : " + replacedQuery + " \n ";
                LOGGER.error(queryWithCode);
                throw new ImtechoSystemException(queryWithCode, e);
            }
            return queryResultDto;
        }).forEach(resultQueryDtos::add
        );
        return resultQueryDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<QueryDto> execute(List<QueryDto> queryDtos, boolean isSecure) {
        queryDtos.sort(Comparator.comparing(QueryDto::getSequence));
        List<QueryDto> resultQueryDtos = new LinkedList<>();
        queryDtos.stream().map(queryDto -> {
            QueryMaster queryMaster = queryMasterDao.retrieveByCode(queryDto.getCode());

            checkIsSecure(isSecure, queryMaster, queryDto);
            if (queryMaster.getCode().equals("dr_techo_retrieve_by_aadhar_number")
                    && (queryDto.getParameters().get(QueryMaster.Fields.AADHAR_NUMBER) != null)) {
                String encryptedAadharNumber = AESEncryption.getInstance().encrypt(queryDto.getParameters().get(QueryMaster.Fields.AADHAR_NUMBER).toString());
                queryDto.getParameters().put(QueryMaster.Fields.AADHAR_NUMBER, encryptedAadharNumber);
            }
            QueryDto queryResultDto = new QueryDto();
            queryResultDto.setCode(queryDto.getCode());
            queryResultDto.setSequence(queryDto.getSequence());
            String replacedQuery;
            // For SOH user id not available for some API
            Integer userId = !userService.isAnonymous() ? user.getId() : 0;
            replacedQuery = EventFunctionUtil.replaceParamWithValue(queryMaster.getQuery(), queryMaster.getParams(), queryDto.getParameters(), userId);
            try {
                if (queryMaster.getReturnsResultSet() != null && queryMaster.getReturnsResultSet()) {
                    List<LinkedHashMap<String, Object>> executeQuery = tableService.execute(replacedQuery, queryDto.getParameters());
                    queryResultDto.setResult(executeQuery);
                } else {
                    if (queryMaster.getCode().equals("system_config_update")){
                        tableService.executeUpdateQueryWithoutCast(replacedQuery, queryDto.getParameters());
                    }
                    else {
                        tableService.executeUpdateQuery(replacedQuery, queryDto.getParameters());
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Exception In Query");
                String queryWithCode = "Query Code : " + queryDto.getCode() + "\nQuery : " + replacedQuery + " \n ";
                LOGGER.error(queryWithCode);
                throw new ImtechoSystemException(queryWithCode, e);
            }
            return queryResultDto;
        }).forEach(resultQueryDtos::add
        );
        return resultQueryDtos;
    }

    void setDefaultParams(LinkedHashMap<String, Object> defaultParams) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (user != null && defaultParams != null &&
                (authentication != null && authentication.getPrincipal() != null && !authentication.getPrincipal().equals("anonymousUser"))) {
            defaultParams.put("loggedInUserId", user.getId());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runUpdateQuery(String query) {
        tableService.executeUpdate(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LinkedHashMap<String, Object>> getQueryResult(QueryDto queryDto) {
        List<QueryDto> queryDtos = new LinkedList<>();
        queryDtos.add(queryDto);
        List<QueryDto> resultQueryDto = executeQuery(queryDtos, true);
        if (!resultQueryDto.isEmpty()) {
            return resultQueryDto.get(0).getResult();
        }
        return Collections.emptyList();
    }

    private void checkIsSecure(Boolean isSecure, QueryMaster queryMaster, QueryDto queryDto) {
        if (queryMaster == null) {
            throw new ImtechoSystemException("The resource with given code not found : " + queryDto.getCode(), 101);
        }

        if (Boolean.FALSE.equals(isSecure) && (queryMaster.getIsPublic() == null || !queryMaster.getIsPublic())) {
            throw new ImtechoSystemException("You are not authorised to access this resource", 101);
        }
    }

}
