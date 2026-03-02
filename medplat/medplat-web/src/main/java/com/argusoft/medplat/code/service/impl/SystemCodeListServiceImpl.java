package com.argusoft.medplat.code.service.impl;

import com.argusoft.medplat.code.dao.SystemCodeListDao;
import com.argusoft.medplat.code.dto.SystemCodeListDto;
import com.argusoft.medplat.code.mapper.SystemCodeListMapper;
import com.argusoft.medplat.code.model.SystemCodeListMaster;
import com.argusoft.medplat.code.service.SystemCodeListService;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.query.dto.QueryDto;
import com.argusoft.medplat.query.service.QueryMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 *     Implements methods of SystemCodeListService
 * </p>
 * @author Hiren Morzariya
 * @since 16/09/2020 4:30
 */
@Service
@Transactional
public class SystemCodeListServiceImpl implements SystemCodeListService {


    private static final Logger logger = LoggerFactory.getLogger(SystemCodeListServiceImpl.class);

    @Autowired
    private SystemCodeListDao systemCodeListDao;

    @Autowired
    private ImtechoSecurityUser currentUser;

    @Autowired
    private QueryMasterService queryMasterService;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SystemCodeListDto> saveOrUpdate(List<SystemCodeListDto> dtos) {

        final String queryCode = "insert_into_system_code_master_list";

        List<QueryDto> queryDtos = dtos.stream().map((SystemCodeListDto dto) -> {
            QueryDto queryDto = new QueryDto();
            queryDto.setCode(queryCode);
            queryDto.setSequence(1);
            LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
            if (dto.getId() == null) {
                parameters.put("id", UUID.randomUUID());
            } else {
                parameters.put("id", dto.getId());
            }
            parameters.put("code_type", dto.getCodeType());
            parameters.put("codeCategory", dto.getCodeCategory());
            parameters.put("codeId", dto.getCodeId());
            parameters.put("code", dto.getCode());
            parameters.put("parentCode", dto.getParentCode());
            parameters.put("name", dto.getName());
            parameters.put("description", dto.getDescription());
            parameters.put("descTypeId", dto.getDescTypeId());
            parameters.put("effectiveDate", dto.getEffectiveDate());
            parameters.put("otherDetails", dto.getOtherDetails());
            parameters.put("publishedEdition", dto.getPublishedEdition());
            parameters.put("isActive", dto.getIsActive());
            parameters.put("createdBy", 1);
            parameters.put("modifiedBy", 1);
            queryDto.setParameters(parameters);
            return queryDto;
        }).collect(Collectors.toList());
        logger.info("SIZE = {}",queryDtos.size());
        List<List<QueryDto>> chunkList = getChunkList(queryDtos, 1000);

        int total = queryDtos.size();
        final AtomicInteger count = new AtomicInteger();
        chunkList.stream().parallel().map((List<QueryDto> dtoList)->{
            List<QueryDto> executeQuery = queryMasterService.execute(dtoList, true);
            int c = count.addAndGet(executeQuery.size());
            logger.info("SIZE processed = {} / {} ( {} %)",c, total, (c * 100 / total ));
            return executeQuery;
            
        }).collect(Collectors.toList());
        logger.info("Element Processed = {}",total);
        return dtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SystemCodeListDto saveOrUpdate(SystemCodeListDto codeDto) {
        
        final String queryCode = "insert_into_system_code_master_list";
        
        if (codeDto.getId() == null) {
            codeDto.setId(UUID.randomUUID());
        }

        List<QueryDto> queryDtos = Stream.of(codeDto).map((SystemCodeListDto dto) -> {
            QueryDto queryDto = new QueryDto();
            queryDto.setCode(queryCode);
            queryDto.setSequence(1);
            LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
            parameters.put("id", dto.getId());
            parameters.put("code_type", dto.getCodeType());
            parameters.put("codeCategory", dto.getCodeCategory());
            parameters.put("codeId", dto.getCodeId());
            parameters.put("code", dto.getCode());
            parameters.put("parentCode", dto.getParentCode());
            parameters.put("name", dto.getName());
            parameters.put("description", dto.getDescription());
            parameters.put("descTypeId", dto.getDescTypeId());
            parameters.put("effectiveDate", dto.getEffectiveDate());
            parameters.put("otherDetails", dto.getOtherDetails());
            parameters.put("publishedEdition", dto.getPublishedEdition());
            parameters.put("isActive", dto.getIsActive());
            parameters.put("createdBy", 1);
            parameters.put("modifiedBy", 1);
            queryDto.setParameters(parameters);
            return queryDto;
        }).collect(Collectors.toList());
        
        queryMasterService.execute(queryDtos, true);
        
         return this.getSystemCodesByCodeId(codeDto.getCodeId(),codeDto.getCodeType()).get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SystemCodeListDto> getSystemCodes(String searchNameString, String descTypeId, String moduleId, String codeType) {
        List<SystemCodeListMaster> list = systemCodeListDao.getSystemCodes(searchNameString, descTypeId, moduleId, codeType);
        return SystemCodeListMapper.convertMasterListToDto(list);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SystemCodeListDto> getSystemCodesByCodeId(String codeId, String codeType) {
        List<SystemCodeListMaster> list = systemCodeListDao.getSystemCodesByCodeId(codeId, codeType);
        return SystemCodeListMapper.convertMasterListToDto(list);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SystemCodeListDto> getSystemCodesByCode(String code, String codeType) {
        List<SystemCodeListMaster> list = systemCodeListDao.getSystemCodesByCode(code, codeType);
        return SystemCodeListMapper.convertMasterListToDto(list);
    }

    private List<List<QueryDto>> getChunkList(List<QueryDto> list, int chunkSize) {

        final AtomicInteger counter = new AtomicInteger();

        final Collection<List<QueryDto>> result = list.stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / chunkSize))
                .values();

        return new ArrayList<>(result);

    }

}
