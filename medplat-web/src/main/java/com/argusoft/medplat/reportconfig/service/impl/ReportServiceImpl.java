/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.reportconfig.service.impl;

import com.argusoft.medplat.common.dao.MenuConfigDao;
import com.argusoft.medplat.common.dao.MenuGroupDao;
import com.argusoft.medplat.common.dao.ServerManagementDao;
import com.argusoft.medplat.common.dao.SystemConfigSyncDao;
import com.argusoft.medplat.common.mapper.SystemConfigSyncMapper;
import com.argusoft.medplat.common.model.MenuConfig;
import com.argusoft.medplat.common.model.MenuGroup;
import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.event.util.EventFunctionUtil;
import com.argusoft.medplat.exception.ImtechoSystemException;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.query.dto.ReportQueryMasterDto;
import com.argusoft.medplat.query.service.ReportQueryMasterService;
import com.argusoft.medplat.query.service.TableService;
import com.argusoft.medplat.reportconfig.dao.ReportMasterDao;
import com.argusoft.medplat.reportconfig.dao.ReportOfflineDetailsDao;
import com.argusoft.medplat.reportconfig.dto.FilterObj;
import com.argusoft.medplat.reportconfig.dto.ReportConfigDto;
import com.argusoft.medplat.reportconfig.dto.ReprotExcelDto;
import com.argusoft.medplat.reportconfig.mapper.ReportMapper;
import com.argusoft.medplat.reportconfig.model.ReportMaster;
import com.argusoft.medplat.reportconfig.model.ReportOfflineDetails;
import com.argusoft.medplat.reportconfig.service.ReportService;
import com.argusoft.medplat.web.users.dao.UserDao;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.styledxmlparser.jsoup.Jsoup;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.argusoft.medplat.reportconfig.mapper.ReportMapper.getGroupMasterDto;

/**
 * <p>
 * Define services for report.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 11:00 AM
 */
@Transactional
@Service
public class ReportServiceImpl implements ReportService {
    public static final String TECHO_REPORT_VIEW = "techo.report.view({id:";
    public static final String ERROR_IN_PARAM = "Error in parameter query";
    public static final String QUERY_UUID = "queryUUID";
    public static final String CONTAINERS = "containers";
    public static final String FIELDS_CONTAINERS = "fieldsContainer";
    public static final String IS_QUERY = "isQuery";
    public static final String QUERY = "query";
    public static final String QUERY_PARAMS = "queryParams";
    public static final String TABLE_CONTAINER = "tableContainer";
    public static final String FIELD_NAME = "fieldName";
    public static final String FIELD_TYPE = "fieldType";
    public static final String QUERY_FOR_PARAM = "queryForParam";
    public static final String QUERY_ID_FOR_PARAM = "queryIdForParam";
    public static final String QUERY_UUID_FOR_PARAM = "queryUUIDForParam";
    public static final String OFFSET = "offset";
    public static final String LIMIT = "limit";
    public static final String CONST_LIMIT = "limit ";
    public static final String LMIT_OFFSET = "#limit_offset#";
    public static final String QUERY_ID = "queryId";

    @Autowired
    ReportMasterDao reportMasterDao;

    @Autowired
    MenuConfigDao menuConfigDao;

    @Autowired
    ImtechoSecurityUser user;

    @Autowired
    TableService tableService;
    @Autowired
    private ServerManagementDao serverManagementDao;
    @Autowired
    ReportQueryMasterService reportQueryService;


//    @Autowired
//    private GujaratiToEnglishTranslateService translate;

    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private SystemConfigSyncDao systemConfigSyncDao;
    @Autowired
    private ReportOfflineDetailsDao reportOfflineDetailsDao;

    @Autowired
    private UserDao userDao;

    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    /**
     * Save report details.
     *
     * @param reportMaster              Entity of report master.
     * @param menuConfig                Entity of menu config.
     * @param reportConfigForSysDto     Report config for sync details.
     * @param isMetodCallBySyncFunction Is method called by sync function.
     */
    public void saveReportMaster(ReportMaster reportMaster, MenuConfig menuConfig, ReportConfigDto reportConfigForSysDto, boolean isMetodCallBySyncFunction) {
        if (isMetodCallBySyncFunction) {
            if (Boolean.FALSE.equals(reportMasterDao.isCodeAvailableByUUID(reportMaster.getCode(), reportMaster.getUuid()))) {
                throw new ImtechoUserException("Report Code already exists Please enter another", 101);
            }
        } else {
            if (Boolean.FALSE.equals(reportMasterDao.isCodeAvailable(reportMaster.getCode(), reportMaster.getId()))) {
                throw new ImtechoUserException("Report Code already exists Please enter another", 101);
            }
        }

        if (reportMaster.getId() == null) {
            if (reportMaster.getMenuConfig() != null) {
                if (isMetodCallBySyncFunction) {
                    //                will check for Given Group UUID and SubGroup UUID base entries else will create new record
                    MenuGroup menuGroup = menuGroupDao.retrieveMenuGroupByGroupNameAndType(reportConfigForSysDto.getMenuType(), reportConfigForSysDto.getParentGroup().getGroupName());
                    if (menuGroup == null) {
                        menuGroup = new MenuGroup();
                        menuGroup.setGroupName(menuConfig.getMenuGroup().getGroupName());
                        menuGroup.setType(menuConfig.getMenuGroup().getType());
                        menuGroupDao.create(menuGroup);
                    }
                    menuConfig.setGroupId(menuGroup.getId());
                } else {
                    MenuGroup menuGroup = null;
                    if (menuConfig.getGroupId() != null) {
                        menuGroup = menuGroupDao.retrieveById(menuConfig.getGroupId());
                    }
                    reportConfigForSysDto.setParentGroup(getGroupMasterDto(menuGroup, menuConfig.getGroupNameUUID()));
                }
                menuConfigDao.create(menuConfig);
                reportMaster.setMenuConfig(menuConfig);
            }
            Integer reportId = reportMasterDao.create(reportMaster);
            if (reportMaster.getMenuConfig() != null) {
                menuConfig.setNavigationState(TECHO_REPORT_VIEW + reportId + "})");
                menuConfigDao.update(menuConfig);
            }

        } else {
            if (!isMetodCallBySyncFunction) {
                if (menuConfig == null && reportMaster.getMenuConfig() != null) {
                    menuConfigDao.delete(reportMaster.getMenuConfig());

                }
                if (menuConfig != null) {
                    if (menuConfig.getNavigationState() == null) {
                        menuConfig.setNavigationState(TECHO_REPORT_VIEW + reportMaster.getId() + "})");
                    }
                    MenuGroup menuGroup = null;
                    if (menuConfig.getGroupId() != null) {
                        menuGroup = menuGroupDao.retrieveById(menuConfig.getGroupId());
                    }
                    reportConfigForSysDto.setParentGroup(getGroupMasterDto(menuGroup, menuConfig.getGroupNameUUID()));
                } else {
                    reportConfigForSysDto.setParentGroup(null);
                }
                reportMaster.setMenuConfig(menuConfig);
            } else {
                if (menuConfig == null && reportMaster.getMenuConfig() != null) {
                    menuConfigDao.delete(reportMaster.getMenuConfig());
                }

                if (menuConfig != null) {
                    MenuGroup menuGroup = menuGroupDao.retrieveMenuGroupByGroupNameAndType(reportConfigForSysDto.getMenuType(), reportConfigForSysDto.getParentGroup().getGroupName());
                    MenuConfig menuConfig1 = reportMaster.getMenuConfig();
                    if (menuConfig1 == null) {
                        menuConfig1 = menuConfig;
                    }
                    if (menuGroup == null) {
                        menuGroup = new MenuGroup();
                        menuGroup.setGroupName(menuConfig.getMenuGroup().getGroupName());
                        menuGroup.setType(menuConfig.getMenuGroup().getType());
                        menuGroupDao.create(menuGroup);
                    }
                    menuConfig1.setGroupId(menuGroup.getId());
                    if (menuConfig1.getNavigationState() == null) {
                        menuConfig1.setNavigationState(TECHO_REPORT_VIEW + reportMaster.getId() + "})");
                    }
                    reportMaster.setMenuConfig(menuConfig1);
                }
            }
            reportMasterDao.merge(reportMaster);
        }

        if (!isMetodCallBySyncFunction) {
            Integer id = null;
            String jsonObject;
            UUID featureUUID = null;

            ReportConfigDto reportConfigDto = (ReportConfigDto) reportConfigForSysDto;

            try {
                jsonObject = ImtechoUtil.convertObjectToJson(reportConfigDto);
                featureUUID = reportConfigDto.getUuid();
                id = systemConfigSyncDao.create(SystemConfigSyncMapper.convertDtoToMaster(ConstantUtil.SYNC_REPORT_MASTER, reportConfigDto.getUuid(), reportConfigDto.getName(), jsonObject, reportConfigDto.getCreatedBy()));

            } catch (JsonProcessingException e) {
                throw new ImtechoUserException("JSON parsing from Report Configuration Object Exception: ", e);
            }

            List<Integer> serverIds = serverManagementDao.getActiveServerIdFromFeature(featureUUID);
            for (Integer serverId : serverIds) {
                serverManagementDao.insertSystemSyncWith(serverId, id);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReportMaster retrieveReportMasterByIdOrCode(Integer id, String code, Boolean fetchQuery) {
        ReportMaster reportMaster = reportMasterDao.getReportMasterWithParamterByIdOrCode(id, code);
        if (fetchQuery != null && fetchQuery) {
            JsonNode configJson = ImtechoUtil.convertStringToJson(reportMaster.getConfigJson());
            if (configJson.get(CONTAINERS).get(FIELDS_CONTAINERS) != null
                    && configJson.get(CONTAINERS).get(FIELDS_CONTAINERS).isArray()) {
                for (JsonNode container : configJson.get(CONTAINERS).get(FIELDS_CONTAINERS)) {
                    if (container.get(IS_QUERY) != null && container.get(IS_QUERY).asBoolean()
                            && container.get(QUERY_ID) != null) {
                        ReportQueryMasterDto queryMasterDto = reportQueryService.retrieveById(container.get(QUERY_ID).asInt());
                        if (container.get(QUERY_ID) != null) {
                            queryMasterDto.setId(container.get(QUERY_ID).asInt());
                            queryMasterDto = reportQueryService.retrieveById(container.get(QUERY_ID).asInt());
                        }
                        ((ObjectNode) container).put(QUERY, queryMasterDto.getQuery());
                        ((ObjectNode) container).put(QUERY_PARAMS, queryMasterDto.getParams());
                    }
                }
            }
            if (configJson.get(CONTAINERS).get(TABLE_CONTAINER) != null
                    && configJson.get(CONTAINERS).get(TABLE_CONTAINER).isArray()) {
                for (JsonNode table : configJson.get(CONTAINERS).get(TABLE_CONTAINER)) {
                    if (table.get(QUERY_ID) != null) {
                        ReportQueryMasterDto queryMasterDto = reportQueryService.retrieveById(table.get(QUERY_ID).asInt());
                        if (table.get(QUERY_ID) != null) {
                            queryMasterDto.setId(table.get(QUERY_ID).asInt());
                            queryMasterDto = reportQueryService.retrieveById(table.get(QUERY_ID).asInt());
                        }
                        ((ObjectNode) table).put(QUERY, queryMasterDto.getQuery());
                        ((ObjectNode) table).put(QUERY_PARAMS, queryMasterDto.getParams());
                    }
                }
            }
            reportMaster.setConfigJson(ImtechoUtil.convertJsonToString(configJson));
        }
        return reportMaster;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReportConfigDto getDynamicReportDetailByIdOrCode(Integer id, String code, Boolean fetchQueryOptions) {
        Map<String, String> parameterExceptionMap = new HashMap<>();
        ReportMaster reportMaster = this.retrieveReportMasterByIdOrCode(id, code, false);
        if (reportMaster != null) {
            ReportConfigDto reportConfigDto = ReportMapper.getReportConfigDto(reportMaster);
            if (fetchQueryOptions != null && fetchQueryOptions.equals(Boolean.TRUE) && reportConfigDto.getConfigJson() != null) {
                Map<String, JsonNode> parameterMap = new LinkedHashMap<>();
                JsonNode configJson = reportConfigDto.getConfigJson();
                if (configJson.get(CONTAINERS) != null && configJson.get(CONTAINERS).get(FIELDS_CONTAINERS) != null
                        && configJson.get(CONTAINERS).get(FIELDS_CONTAINERS).isArray()) {
                    for (JsonNode containers : configJson.get(CONTAINERS).get(FIELDS_CONTAINERS)) {
                        parameterMap.put(containers.get(FIELD_NAME).asText(), containers.get("value"));
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    for (JsonNode containers : configJson.get(CONTAINERS).get(FIELDS_CONTAINERS)) {
                        if (containers.get(IS_QUERY) != null && containers.get(IS_QUERY).asBoolean()
                                && containers.get(QUERY_ID) != null) {
                            ReportQueryMasterDto queryMaster = reportQueryService.retrieveById(containers.get(QUERY_ID).asInt());
                            String query = queryMaster.getQuery();
                            if (!CollectionUtils.isEmpty(parameterMap)) {
                                query = ImtechoUtil.replaceParameterInQuery(query, parameterMap);
                            }
                            query = ImtechoUtil.replaceImplicitParametersFromQuery(query, user.getId());
                            try {
                                ((ObjectNode) containers).set("optionsByQuery", mapper.convertValue(tableService.executeQueryForCombo(query, reportMaster.getReportName()), JsonNode.class));
                                ((ObjectNode) containers).put(QUERY_PARAMS, queryMaster.getParams());

                            } catch (Exception e) {
                                parameterExceptionMap.put(containers.get(FIELD_NAME).asText(), e.getMessage());
                            }
                        }
                    }
                }
            }

            if (!CollectionUtils.isEmpty(parameterExceptionMap)) {
                throw new ImtechoSystemException(ERROR_IN_PARAM, 0, parameterExceptionMap);
            }
            return reportConfigDto;
        } else {
            throw new ImtechoSystemException("Report Not Found.", "Report Not Found. Report Id : " + id + "Code : " + code + " User Id : " + user.getId(), null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReportConfigDto getDynamicReport(ReportConfigDto reportConfigDto) {
        ReportMaster reportMaster = this.retrieveReportMasterByIdOrCode(reportConfigDto.getId(), null, false);
        List<LinkedHashMap<String, Object>> tableData;
        if (reportMaster != null) {
            Map<String, String> tableExceptionMap = new HashMap<>();
            Map<String, String> parameterExceptionMap = new HashMap<>();
            JsonNode configJson = reportConfigDto.getConfigJson();
            if (configJson.get(CONTAINERS) != null) {
                Map<String, JsonNode> parameterMap = new LinkedHashMap<>();
                if (configJson.get(CONTAINERS).get(FIELDS_CONTAINERS) != null
                        && configJson.get(CONTAINERS).get(FIELDS_CONTAINERS).isArray()) {
                    for (JsonNode containers : configJson.get(CONTAINERS).get(FIELDS_CONTAINERS)) {
                        if (containers.get(FIELD_TYPE).asText().equals("onlyMonthFromTo") || containers.get(FIELD_TYPE).asText().equals("dateFromTo") || containers.get(FIELD_TYPE).asText().equals("dateRangePicker")) {
                            parameterMap.put("from_" + containers.get(FIELD_NAME).asText(), null);
                            parameterMap.put("to_" + containers.get(FIELD_NAME).asText(), null);
                        } else {
                            parameterMap.put(containers.get(FIELD_NAME).asText(), containers.get("value"));
                        }
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    for (JsonNode containers : configJson.get(CONTAINERS).get(FIELDS_CONTAINERS)) {
                        if (containers.get(IS_QUERY) != null && containers.get(IS_QUERY).asBoolean()
                                && containers.get(QUERY_ID) != null) {
                            ReportQueryMasterDto reportQueryMasterDto = reportQueryService.retrieveById(containers.get(QUERY_ID).asInt());
                            String query = reportQueryMasterDto.getQuery();
                            if (!CollectionUtils.isEmpty(parameterMap)) {
                                query = ImtechoUtil.replaceParameterInQuery(query, parameterMap);
                            }
                            query = ImtechoUtil.replaceImplicitParametersFromQuery(query, user.getId());
                            try {
                                ((ObjectNode) containers).set("optionsByQuery", mapper.convertValue(tableService.executeQueryForCombo(query, reportMaster.getReportName()), JsonNode.class));
                                ((ObjectNode) containers).put(QUERY_PARAMS, reportQueryMasterDto.getParams());

                            } catch (Exception e) {
                                parameterExceptionMap.put(containers.get(FIELD_NAME).asText(), e.getMessage());
                            }
                        }
                    }

                }

                if (configJson.get(CONTAINERS).get(TABLE_CONTAINER) != null
                        && configJson.get(CONTAINERS).get(TABLE_CONTAINER).isArray()) {
                    parameterMap.put(OFFSET, configJson.get(CONTAINERS).get(OFFSET));
                    ObjectMapper mapper = new ObjectMapper();
                    for (JsonNode table : configJson.get(CONTAINERS).get(TABLE_CONTAINER)) {
                        if (table.get(QUERY_ID) != null) {
                            ReportQueryMasterDto queryMasterDto = reportQueryService.retrieveById(table.get(QUERY_ID).asInt());
                            String query = queryMasterDto.getQuery();
                            query =query.replaceAll("#searchstring#","true");

                            if (!CollectionUtils.isEmpty(parameterMap)) {
                                query = ImtechoUtil.replaceParameterInQuery(query, parameterMap);
                                if (configJson.get(CONTAINERS).get(LIMIT) != null && configJson.get(CONTAINERS).get(OFFSET) != null) {
                                    query = query.replace(LMIT_OFFSET, CONST_LIMIT + configJson.get(CONTAINERS).get(LIMIT).asText() + " offset " + configJson.get(CONTAINERS).get(OFFSET).asText());
                                } else if (configJson.get(CONTAINERS).get(OFFSET) == null && configJson.get(CONTAINERS).get(LIMIT) != null) {
                                    query = query.replace(LMIT_OFFSET, CONST_LIMIT + configJson.get(CONTAINERS).get(LIMIT).asText() + " offset 0");
                                } else {
                                    query = query.replace(LMIT_OFFSET, "");
                                }
                                if (configJson.get(CONTAINERS).get(OFFSET) == null) {
                                    query = query.replaceAll("#offset#", "0");

                                } else {
                                    query = query.replaceAll("#offset#", configJson.get(CONTAINERS).get(OFFSET).asText());
                                }
                            }
                            query = ImtechoUtil.replaceImplicitParametersFromQuery(query, user.getId());
                            try {
                                tableData = tableService.executeQuery(query);
                                ((ObjectNode) table).set("tableData", mapper.convertValue(tableData, JsonNode.class));
                            } catch (Exception e) {
                                tableExceptionMap.put(table.get(FIELD_NAME).asText(), e.getMessage());
                            }
                        }
                    }
                }

            }

            if (parameterExceptionMap.size() > 0) {
                throw new ImtechoSystemException(ERROR_IN_PARAM, 0, parameterExceptionMap);
            }

            if (!CollectionUtils.isEmpty(tableExceptionMap)) {
                if (reportMaster.getReportName().equals("Report for selection")) {
                    throw new ImtechoUserException("Error while retrieving table data", 0);
                }
                String exceptionString = "Report name : " + reportMaster.getReportName() + "\n Exception data : " + tableExceptionMap.toString();
                throw new ImtechoSystemException("Error while retrieving table data", exceptionString, null);
            }

        }
        return reportConfigDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveDynamicReport(ReportConfigDto reportConfigDto, boolean isMetodCallBySyncFunction) {
        Map<String, String> parameterExceptionMap = new HashMap<>();
        Map<String, String> tableExceptionMap = new HashMap<>();
        JsonNode configJson = reportConfigDto.getConfigJson();

        if (configJson.get(CONTAINERS) != null) {
            Map<String, JsonNode> parameterMap = new LinkedHashMap<>();
            if (configJson.get(CONTAINERS).get(FIELDS_CONTAINERS) != null
                    && configJson.get(CONTAINERS).get(FIELDS_CONTAINERS).isArray()) {

//              between date parameter
                for (JsonNode containers : configJson.get(CONTAINERS).get(FIELDS_CONTAINERS)) {
                    if (containers.get(FIELD_TYPE).asText().equals("onlyMonthFromTo") || containers.get(FIELD_TYPE).asText().equals("dateFromTo") || containers.get(FIELD_TYPE).asText().equals("dateRangePicker")) {
                        parameterMap.put("from_" + containers.get(FIELD_NAME).asText(), null);
                        parameterMap.put("to_" + containers.get(FIELD_NAME).asText(), null);
                    } else {
                        parameterMap.put(containers.get(FIELD_NAME).asText(), null);
                    }
                }

//                Query for Each param (Sidebar Filter)
                for (JsonNode containers : configJson.get(CONTAINERS).get(FIELDS_CONTAINERS)) {
                    if (containers.get(QUERY_FOR_PARAM) != null) {
                        ReportQueryMasterDto queryMasterDto = new ReportQueryMasterDto();
                        String queryForParam = containers.get(QUERY_FOR_PARAM).asText();
                        if (queryForParam != null && !queryForParam.isEmpty()) {

                            if (containers.get(QUERY_ID_FOR_PARAM) != null) {
                                if (isMetodCallBySyncFunction) {
                                    queryMasterDto = reportQueryService.retrieveByUUID(UUID.fromString(containers.get(QUERY_UUID_FOR_PARAM).asText()));
                                } else {
                                    queryMasterDto.setId(containers.get(QUERY_ID_FOR_PARAM).asInt());
                                    queryMasterDto = reportQueryService.retrieveById(containers.get(QUERY_ID_FOR_PARAM).asInt());
                                }
                            }

                            if (isMetodCallBySyncFunction) {
                                queryMasterDto.setCreatedBy(-1);
                                queryMasterDto.setCreatedOn(new Date());
                            }

                            queryMasterDto.setQuery(containers.get(QUERY_FOR_PARAM).asText());
                            queryMasterDto.setParams(EventFunctionUtil.findParamsFromTemplate(containers.get(QUERY_FOR_PARAM).asText()));
                            queryMasterDto.setReturnsResultSet(Boolean.TRUE);

                            if (queryMasterDto.getUuid() == null) {
                                if (isMetodCallBySyncFunction) {
                                    queryMasterDto.setUuid(UUID.fromString(containers.get(QUERY_UUID_FOR_PARAM).asText()));
                                } else {
                                    queryMasterDto.setUuid(UUID.randomUUID());
                                }
                            }
                            Integer queryId = reportQueryService.createOrUpdate(queryMasterDto);
                            if (queryId != null) {
                                ((ObjectNode) containers).put(QUERY_ID_FOR_PARAM, queryId);
                                ((ObjectNode) containers).put(QUERY_UUID_FOR_PARAM, queryMasterDto.getUuid().toString());
                            }

                        } else {
                            ((ObjectNode) containers).put(QUERY_ID_FOR_PARAM, "");
                        }
                    }
                    if (containers.get(IS_QUERY) != null && containers.get(IS_QUERY).asBoolean()
                            && containers.get(QUERY) != null) {
                        String query = containers.get(QUERY).asText();
                        if (!CollectionUtils.isEmpty(parameterMap)) {
                            query = ImtechoUtil.replaceParameterInQuery(query, parameterMap);
                        }

                        if (isMetodCallBySyncFunction) {
                            query = ImtechoUtil.replaceImplicitParametersFromQuery(query, -1);
                        } else {
                            query = ImtechoUtil.replaceImplicitParametersFromQuery(query, user.getId());
                        }

                        try {
                            tableService.executeQueryForCombo(query, reportConfigDto.getName());
                            ReportQueryMasterDto queryMasterDto = new ReportQueryMasterDto();

                            if (containers.get(QUERY_ID) != null) {
                                if (isMetodCallBySyncFunction) {
                                    queryMasterDto = reportQueryService.retrieveByUUID(UUID.fromString(containers.get(QUERY_UUID).asText()));
                                } else {
                                    queryMasterDto.setId(containers.get(QUERY_ID).asInt());
                                    queryMasterDto = reportQueryService.retrieveById(containers.get(QUERY_ID).asInt());
                                }
                            }

                            if (isMetodCallBySyncFunction) {
                                queryMasterDto.setCreatedBy(-1);
                                queryMasterDto.setCreatedOn(new Date());
                            }

                            queryMasterDto.setQuery(containers.get(QUERY).asText());
                            queryMasterDto.setParams(EventFunctionUtil.findParamsFromTemplate(containers.get(QUERY).asText()));
                            queryMasterDto.setReturnsResultSet(Boolean.TRUE);

                            if (queryMasterDto.getUuid() == null) {
                                if (isMetodCallBySyncFunction) {
                                    queryMasterDto.setUuid(UUID.fromString(containers.get(QUERY_UUID).asText()));
                                } else {
                                    queryMasterDto.setUuid(UUID.randomUUID());
                                }
                            }

                            Integer queryId = reportQueryService.createOrUpdate(queryMasterDto);
                            if (queryId != null) {
                                ((ObjectNode) containers).put(QUERY_ID, queryId);
                                ((ObjectNode) containers).put(QUERY_PARAMS, queryMasterDto.getParams());
                                ((ObjectNode) containers).put(QUERY_UUID, queryMasterDto.getUuid().toString());
                            }
                        } catch (Exception e) {
                            parameterExceptionMap.put(containers.get(FIELD_NAME).asText(), e.getMessage());
                        }
                    }
                }
            }

//            For Main Query
            if (configJson.get(CONTAINERS).get(TABLE_CONTAINER) != null
                    && configJson.get(CONTAINERS).get(TABLE_CONTAINER).isArray()) {
                for (JsonNode table : configJson.get(CONTAINERS).get(TABLE_CONTAINER)) {
                    if (table.get(QUERY) != null) {
                        String query = table.get(QUERY).asText();
                        if (!CollectionUtils.isEmpty(parameterMap)) {
                            query = ImtechoUtil.replaceParameterInQuery(query, parameterMap);
                        }

                        if (isMetodCallBySyncFunction) {
                            query = ImtechoUtil.replaceImplicitParametersFromQuery(query, -1);
                        } else {
                            query = ImtechoUtil.replaceImplicitParametersFromQuery(query, user.getId());
                        }

                        if (query.contains(LMIT_OFFSET)) {
                            query = query.replaceAll(LMIT_OFFSET, "limit 100 offset 0");
                        }

                        try {
                            query = query.replaceAll("#searchstring#", "true");
                            tableService.executeQuery(query);
                            ReportQueryMasterDto queryMasterDto = new ReportQueryMasterDto();

                            if (table.get(QUERY_ID) != null) {
                                if (isMetodCallBySyncFunction) {
                                    queryMasterDto = reportQueryService.retrieveByUUID(UUID.fromString(table.get(QUERY_UUID).asText()));
                                } else {
                                    queryMasterDto.setId(table.get(QUERY_ID).asInt());
                                    queryMasterDto = reportQueryService.retrieveById(table.get(QUERY_ID).asInt());
                                }

                            }

                            queryMasterDto.setQuery(table.get(QUERY).asText());
                            queryMasterDto.setParams(EventFunctionUtil.findParamsFromTemplate(table.get(QUERY).asText()));
                            queryMasterDto.setReturnsResultSet(Boolean.TRUE);

                            if (queryMasterDto.getUuid() == null) {
                                if (isMetodCallBySyncFunction) {
                                    queryMasterDto.setUuid(UUID.fromString(table.get(QUERY_UUID).asText()));
                                } else {
                                    queryMasterDto.setUuid(UUID.randomUUID());
                                }
                            }

                            if (isMetodCallBySyncFunction) {
                                queryMasterDto.setCreatedBy(-1);
                                queryMasterDto.setCreatedOn(new Date());
                            }

                            Integer queryId = reportQueryService.createOrUpdate(queryMasterDto);
                            if (queryId != null) {
                                ((ObjectNode) table).put(QUERY_ID, queryId);
                                ((ObjectNode) table).put(QUERY_UUID, queryMasterDto.getUuid().toString());
                            }

                        } catch (Exception e) {
                            tableExceptionMap.put(table.get(FIELD_NAME).asText() + "Query :: " + query, e.getMessage());
                        }
                    }
                }
            }

        }
        if (parameterExceptionMap.size() > 0) {
            throw new ImtechoSystemException(ERROR_IN_PARAM, 0, parameterExceptionMap);
        }

        if (tableExceptionMap.size() > 0) {
            throw new ImtechoSystemException("Error in table query :: " + tableExceptionMap.toString(), 0, tableExceptionMap);
        }

//        check if reportMaster entry is already there or not
        ReportMaster reportMaster;
        if (!isMetodCallBySyncFunction) {
            reportMaster = ReportMapper.getReportMasterModel(reportConfigDto);
            if (reportConfigDto.getId() != null) {
                reportMaster.setId(reportConfigDto.getId());
            }
        } else {
            reportMaster = reportMasterDao.getReportMasterWithParamterByUUIDOrCode(reportConfigDto.getUuid(), reportConfigDto.getCode());
            reportMaster = ReportMapper.getReportMasterModel(reportConfigDto, reportMaster);
        }

        MenuConfig menuConfig = null;
        if (reportConfigDto.getMenuType() != null) {
            menuConfig = ReportMapper.getMenuConfigModelFromReportConfigDto(reportConfigDto);
            menuConfig.setIsDynamicReport(Boolean.TRUE);
            menuConfig.setFeatureJson("{\"canDrillDown\":true}");
        }
        this.saveReportMaster(reportMaster, menuConfig, reportConfigDto, isMetodCallBySyncFunction);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReportMaster> retrieveAllActiveReportMasters(String reportName, Integer offset, Integer limit, Boolean groupAssociated, Boolean subGroupAssociated, Integer groupdId, Integer subGroupId, String userId, String menuType, String sortBy, String sortOrder) {
        return reportMasterDao.getActiveReports(reportName, offset, limit, groupAssociated, subGroupAssociated, groupdId, subGroupId, userId, menuType, sortBy, sortOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteReportById(Integer id) {
        ReportMaster reportMaster = reportMasterDao.retrieveById(id);
        if (reportMaster != null) {
            if (reportMaster.getMenuConfig() != null) {
                MenuConfig menuConfig = menuConfigDao.retrieveById(reportMaster.getMenuConfig().getId());
                menuConfig.setIsActive(Boolean.FALSE);
                menuConfigDao.update(menuConfig);
            }
            reportMaster.setActive(Boolean.FALSE);
            reportMasterDao.update(reportMaster);
        } else {
            throw new ImtechoSystemException("No report found", 0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LinkedHashMap<String, Object>> retrieveDataByQueryUUID(UUID uuid, Map<String, String> parameterMap) {
        ReportQueryMasterDto queryMaster = reportQueryService.retrieveByUUID(uuid);
        String query = queryMaster.getQuery();
        query = ImtechoUtil.replaceImplicitParametersFromQuery(query, user.getId());
        query = ImtechoUtil.replaceParameterInQueryByParamMap(query, parameterMap);
        if (parameterMap.get(LIMIT) != null && parameterMap.get(OFFSET) != null) {
            query = query.replace(LMIT_OFFSET, CONST_LIMIT + parameterMap.get(LIMIT) + " offset " + parameterMap.get(OFFSET));
        } else if (parameterMap.get(OFFSET) == null && parameterMap.get(LIMIT) != null) {
            query = query.replace(LMIT_OFFSET, CONST_LIMIT + parameterMap.get(LIMIT) + " offset 0");
        } else {
            query = query.replace(LMIT_OFFSET, "");
        }
        if (CollectionUtils.isEmpty(parameterMap)) {
            query = query.replace(LMIT_OFFSET, "");
        }

        return tableService.executeQuery(query);

    }

    /**
     * Overloaded method with extra user id parameter.
     *
     * @param uuid           Query uuid
     * @param parameterMap A report param
     * @param userId       User id
     * @return A result of query
     */
    private List<LinkedHashMap<String, Object>> retrieveDataByQueryUUID(UUID uuid, Map<String, String> parameterMap, Integer userId) {
        ReportQueryMasterDto queryMaster = reportQueryService.retrieveByUUID(uuid);
        String query = queryMaster.getQuery();
        query = ImtechoUtil.replaceImplicitParametersFromQuery(query, userId);
        query = ImtechoUtil.replaceParameterInQueryByParamMap(query, parameterMap);
        if (parameterMap.get(LIMIT) != null && parameterMap.get(OFFSET) != null) {
            query = query.replace(LMIT_OFFSET, CONST_LIMIT + parameterMap.get(LIMIT) + " offset " + parameterMap.get(OFFSET));
        } else if (parameterMap.get(OFFSET) == null && parameterMap.get(LIMIT) != null) {
            query = query.replace(LMIT_OFFSET, CONST_LIMIT + parameterMap.get(LIMIT) + " offset 0");
        } else {
            query = query.replace(LMIT_OFFSET, "");
        }
        if (CollectionUtils.isEmpty(parameterMap)) {
            query = query.replace(LMIT_OFFSET, "");
        }

        return tableService.executeQuery(query);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Map<String, Object>> retrieveComboDataByQueryUUID(UUID uuid, Map<String, String> parameterMap) {
        ReportQueryMasterDto queryMaster = reportQueryService.retrieveByUUID(uuid);
        String query = queryMaster.getQuery();
        if (!CollectionUtils.isEmpty(parameterMap)) {
            query = ImtechoUtil.replaceParameterInQueryByParamMap(query, parameterMap);
        }
        query = ImtechoUtil.replaceImplicitParametersFromQuery(query, user.getId());
        return tableService.executeQueryForCombo(query, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LinkedHashMap<String, Object>> retrieveDataByQueryId(Integer id, Map<String, String> parameterMap) {
        ReportQueryMasterDto queryMaster = reportQueryService.retrieveById(id);
        String query = queryMaster.getQuery();
        query = ImtechoUtil.replaceImplicitParametersFromQuery(query, user.getId());
        query = ImtechoUtil.replaceParameterInQueryByParamMap(query, parameterMap);
        if (parameterMap.get(LIMIT) != null && parameterMap.get(OFFSET) != null) {
            query = query.replace(LMIT_OFFSET, CONST_LIMIT + parameterMap.get(LIMIT) + " offset " + parameterMap.get(OFFSET));
        } else if (parameterMap.get(OFFSET) == null && parameterMap.get(LIMIT) != null) {
            query = query.replace(LMIT_OFFSET, CONST_LIMIT + parameterMap.get(LIMIT) + " offset 0");
        } else {
            query = query.replace(LMIT_OFFSET, "");
        }
        if (CollectionUtils.isEmpty(parameterMap)) {
            query = query.replace(LMIT_OFFSET, "");
        }

        return tableService.executeQuery(query);

    }

    /**
     * Overloaded method with extra user id parameter.
     *
     * @param id           Report id
     * @param parameterMap A report param
     * @param userId       User id
     * @return A result of query
     */
    private List<LinkedHashMap<String, Object>> retrieveDataByQueryId(Integer id, Map<String, String> parameterMap, Integer userId) {
        ReportQueryMasterDto queryMaster = reportQueryService.retrieveById(id);
        String query = queryMaster.getQuery();
        query = ImtechoUtil.replaceImplicitParametersFromQuery(query, userId);
        query = ImtechoUtil.replaceParameterInQueryByParamMap(query, parameterMap);
        if (parameterMap.get(LIMIT) != null && parameterMap.get(OFFSET) != null) {
            query = query.replace(LMIT_OFFSET, CONST_LIMIT + parameterMap.get(LIMIT) + " offset " + parameterMap.get(OFFSET));
        } else if (parameterMap.get(OFFSET) == null && parameterMap.get(LIMIT) != null) {
            query = query.replace(LMIT_OFFSET, CONST_LIMIT + parameterMap.get(LIMIT) + " offset 0");
        } else {
            query = query.replace(LMIT_OFFSET, "");
        }
        if (CollectionUtils.isEmpty(parameterMap)) {
            query = query.replace(LMIT_OFFSET, "");
        }

        return tableService.executeQuery(query);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Map<String, Object>> retrieveComboDataByQueryId(Integer id, Map<String, String> parameterMap) {
        ReportQueryMasterDto queryMaster = reportQueryService.retrieveById(id);
        String query = queryMaster.getQuery();
        if (!CollectionUtils.isEmpty(parameterMap)) {
            query = ImtechoUtil.replaceParameterInQueryByParamMap(query, parameterMap);
        }
        query = ImtechoUtil.replaceImplicitParametersFromQuery(query, user.getId());
        return tableService.executeQueryForCombo(query, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteArrayOutputStream downLoadPdfForReport(Integer id, UUID uuid, ReprotExcelDto reportExcelDto) throws IOException, InterruptedException {
        Map<String, String> parameterMap = new HashMap<>(reportExcelDto.getParamObj());
        List<FilterObj> filterObjList = reportExcelDto.getFilterObj();
        Integer userId;
        if (Objects.nonNull(reportExcelDto.getUserId())) {
            // When user is not logged in and pdf is getting generated by scheduler
            userId = reportExcelDto.getUserId();
        } else {
            userId = user.getId();
        }

        List<LinkedHashMap<String, Object>> queryData;
        if (Objects.nonNull(uuid)) {
            queryData = this.retrieveDataByQueryUUID(uuid, parameterMap, userId);
        } else {
            queryData = this.retrieveDataByQueryId(id, parameterMap, userId);
        }

        List<String> headerName = new ArrayList<>(queryData.get(0).keySet());
        List<String> hiddenParameterList = new ArrayList<>();
        Pattern MY_PATTERN = Pattern.compile("hidden");

        for (String header : headerName) {
            Matcher hiddenParam = MY_PATTERN.matcher(header);
            if (hiddenParam.find()) {
                hiddenParameterList.add(header);
            }
        }
        if (!hiddenParameterList.isEmpty()) {
            headerName.removeAll(hiddenParameterList);
        }

        StringBuilder sb = new StringBuilder("<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no\">" +
                "</head>" +
                "<style>" +
                "body {" +
                "    padding: 0;" +
                "    margin: 0;" +
                "}" +
                "" +
                ".main-title {" +
                "    text-align: center;" +
                "    width: 100%;" +
                "}" +
                "" +
                ".w-50 {" +
                "    width: 49%;" +
                "    display: inline-block;" +
                "    vertical-align: top;" +
                "}" +
                "" +
                ".w-33 {" +
                "    width: 33%;" +
                "    display: inline-block;" +
                "    vertical-align: top;" +
                "}" +
                "" +
                ".w-100 {" +
                "    width: 100%;" +
                "    border-top: 1px solid #ebebeb;" +
                "    margin-top: 10px;" +
                "}" +
                "" +
                ".mb-15 {" +
                "    margin-bottom: 15px;" +
                "}" +
                "" +
                ".mt-15 {" +
                "    margin-top: 15px;" +
                "}" +
                "" +
                ".br-1 {" +
                "    border-right: 1px solid #ebebeb;" +
                "}" +
                "" +
                "table {" +
                "    display: table;" +
                "    width: 100%;" +
                "    border-collapse: collapse;" +
                "}" +
                "" +
                "table tr th {" +
                "    background: #ebebeb;" +
                "}" +
                "" +
                "table tr td," +
                "table tr th {" +
                "    border: 1px solid #ebebeb;" +
                "    padding: 5px;" +
                "    text-align: left;" +
                "}" +
                "/* @media print{@page {size: landscape}} */" +
                "/*Bootstrap grid system modified for print / Report */" +
                "" +
                "*," +
                "*::before," +
                "*::after {" +
                "    box-sizing: border-box;" +
                "    font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, \"Helvetica Neue\", Arial, sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\";" +
                "}" +
                "" +
                "html {" +
                "    font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, \"Helvetica Neue\", Arial, sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\";" +
                "    line-height: 1.15;" +
                "    -webkit-text-size-adjust: 100%;" +
                "    -ms-text-size-adjust: 100%;" +
                "    -ms-overflow-style: scrollbar;" +
                "    -webkit-tap-highlight-color: rgba(0, 0, 0, 0);" +
                "}" +
                "" +
                "body {" +
                "    margin: 0;" +
                "    font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, \"Helvetica Neue\", Arial, sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\";" +
                "    font-size: 1rem;" +
                "    font-weight: 400;" +
                "    line-height: 1.5;" +
                "    color: #212529;" +
                "    text-align: left;" +
                "    background-color: #fff;" +
                "    box-sizing: border-box;" +
                "}" +
                "" +
                "h1," +
                "h2," +
                "h3," +
                "h4," +
                "h5," +
                "h6," +
                ".cust-header {" +
                "    margin-top: 0;" +
                "    margin-bottom: 0.5rem;" +
                "}" +
                "" +
                "p {" +
                "    margin-top: 0;" +
                "    margin-bottom: 1rem;" +
                "}" +
                "" +
                ".cust-header {" +
                "    font-weight: bold !important;" +
                "}" +
                "" +
                ".row {" +
                "    /* display: -ms-flexbox; display: flex; -ms-flex-wrap: wrap; flex-wrap: wrap; */" +
                "    margin-right: -15px;" +
                "    margin-left: -15px;" +
                "}" +
                "" +
                ".index-title {" +
                "    text-align: center;" +
                "}" +
                "" +
                ".col-1," +
                ".col-2," +
                ".col-3," +
                ".col-4," +
                ".col-5," +
                ".col-6," +
                ".col-7," +
                ".col-8," +
                ".col-9," +
                ".col-10," +
                ".col-11," +
                ".col-12," +
                ".col," +
                ".col-auto," +
                ".col-sm-1," +
                ".col-sm-2," +
                ".col-sm-3," +
                ".col-sm-4," +
                ".col-sm-5," +
                ".col-sm-6," +
                ".col-sm-7," +
                ".col-sm-8," +
                ".col-sm-9," +
                ".col-sm-10," +
                ".col-sm-11," +
                ".col-sm-12," +
                ".col-sm," +
                ".col-sm-auto," +
                ".col-md-1," +
                ".col-md-2," +
                ".col-md-3," +
                ".col-md-4," +
                ".col-md-5," +
                ".col-md-6," +
                ".col-md-7," +
                ".col-md-8," +
                ".col-md-9," +
                ".col-md-10," +
                ".col-md-11," +
                ".col-md-12," +
                ".col-md," +
                ".col-md-auto," +
                ".col-lg-1," +
                ".col-lg-2," +
                ".col-lg-3," +
                ".col-lg-4," +
                ".col-lg-5," +
                ".col-lg-6," +
                ".col-lg-7," +
                ".col-lg-8," +
                ".col-lg-9," +
                ".col-lg-10," +
                ".col-lg-11," +
                ".col-lg-12," +
                ".col-lg," +
                ".col-lg-auto," +
                ".col-xl-1," +
                ".col-xl-2," +
                ".col-xl-3," +
                ".col-xl-4," +
                ".col-xl-5," +
                ".col-xl-6," +
                ".col-xl-7," +
                ".col-xl-8," +
                ".col-xl-9," +
                ".col-xl-10," +
                ".col-xl-11," +
                ".col-xl-12," +
                ".col-xl," +
                ".col-xl-auto {" +
                "    position: relative;" +
                "    width: 100%;" +
                "    min-height: 1px;" +
                "    padding-right: 7px;" +
                "    padding-left: 7px;" +
                "    float: left;" +
                "    box-sizing: border-box;" +
                "}" +
                "" +
                ".col {" +
                "    max-width: 100%;" +
                "}" +
                "" +
                ".col-1 {" +
                "    max-width: 8.333333%;" +
                "}" +
                "" +
                ".col-2 {" +
                "    max-width: 16.666667%;" +
                "}" +
                "" +
                ".col-3 {" +
                "    max-width: 25%;" +
                "}" +
                "" +
                ".col-4 {" +
                "    max-width: 33.333333%;" +
                "}" +
                "" +
                ".col-5 {" +
                "    max-width: 41.666667%;" +
                "}" +
                "" +
                ".col-6 {" +
                "    max-width: 50%;" +
                "}" +
                "" +
                ".col-7 {" +
                "    max-width: 58.333333%;" +
                "}" +
                "" +
                ".col-8 {" +
                "    max-width: 66.666667%;" +
                "}" +
                "" +
                ".col-9 {" +
                "    max-width: 75%;" +
                "}" +
                "" +
                ".col-10 {" +
                "    max-width: 83.333333%;" +
                "}" +
                "" +
                ".col-11 {" +
                "    max-width: 91.666667%;" +
                "}" +
                "" +
                ".col-12 {" +
                "    max-width: 100%;" +
                "}" +
                "" +
                ".clearfix {" +
                "    display: block;" +
                "    clear: both;" +
                "    content: \"\";" +
                "}" +
                "" +
                ".cst-hr," +
                ".cst-hr .light {" +
                "    display: block;" +
                "    width: 100%;" +
                "    clear: both;" +
                "}" +
                "" +
                ".cst-hr:after," +
                ".cst-hr .light:after {" +
                "    display: block;" +
                "    clear: both;" +
                "    content: \"\";" +
                "    width: 100%;" +
                "}" +
                "" +
                ".print-page {" +
                "    background: #ffffff;" +
                "    -webkit-print-color-adjust: exact;" +
                "    font-size: 11px;" +
                "    line-height: 20px;" +
                "}" +
                "" +
                ".print-wrapper {" +
                "    padding: 10px;" +
                "}" +
                "" +
                ".main-title {" +
                "    font-size: 32px;" +
                "    padding: 100px 0;" +
                "    text-align: center;" +
                "}" +
                "" +
                ".sub-title {" +
                "    font-size: 14px;" +
                "    margin: 0;" +
                "}" +
                "" +
                ".sub-title.small {" +
                "    font-size: 12px;" +
                "    margin: 0;" +
                "}" +
                "" +
                ".sub-title.border-bottom {" +
                "    display: block;" +
                "    margin-bottom: 10px;" +
                "}" +
                "" +
                ".box-header {" +
                "    background: #ebebeb !important;" +
                "    padding: 5px 10px;" +
                "    border: 1px solid #ebebeb;" +
                "    -webkit-print-color-adjust: exact;" +
                "}" +
                "" +
                ".box-body {" +
                "    border: 1px solid #ebebeb;" +
                "    border-top: 0;" +
                "    padding: 10px;" +
                "}" +
                "" +
                ".info ul {" +
                "    display: table;" +
                "    width: 100%;" +
                "    box-sizing: border-box;" +
                "}" +
                "" +
                ".info ul li {" +
                "    display: table-row;" +
                "    width: 100%;" +
                "    float: none;" +
                "    clear: both;" +
                "}" +
                "" +
                ".info label {" +
                "    font-weight: 500;" +
                "    display: block;" +
                "    margin-bottom: 5px;" +
                "}" +
                "" +
                ".info label span.info_type {" +
                "    font-size: 12px;" +
                "    width: 150px;" +
                "    display: inline-block;" +
                "    vertical-align: top;" +
                "}" +
                "" +
                ".info label span.data {" +
                "    font-weight: normal;" +
                "    width: 150px;" +
                "    display: inline-block;" +
                "    vertical-align: top;" +
                "}" +
                "" +
                ".nopadding {" +
                "    padding: 0px;" +
                "    margin: 0;" +
                "}" +
                "" +
                ".info ul {" +
                "    list-style-type: none;" +
                "}" +
                "" +
                ".info ul .data {" +
                "    font-size: 12px;" +
                "}" +
                "" +
                ".info ul .data.block {" +
                "    font-weight: bold !important;" +
                "    width: 150px;" +
                "    display: block;" +
                "}" +
                "" +
                ".row-wrapper {" +
                "    display: flex;" +
                "    flex-wrap: wrap;" +
                "}" +
                "" +
                ".row-wrapper .w-33 {}" +
                "" +
                ".row-wrapper .w-50 {}" +
                "" +
                "tbody {" +
                "    background-color: white;" +
                "}" +
                "" +
                ".cst-hr {" +
                "    box-shadow: none;" +
                "    outline: none;" +
                "    border: 0;" +
                "    border-bottom: 1px solid #e4e4e4;" +
                "    padding: 10px;" +
                "}" +
                "" +
                ".cst-hr.light {" +
                "    border-bottom: 1px solid #ebebeb;" +
                "}" +
                "" +
                "@media print {" +
                "    .cst-table table th {" +
                "        background-color: #ebebeb !important;" +
                "    }" +
                "}" +
                "" +
                ".info label span.info_type {" +
                "    display: block;" +
                "    width: 50%;" +
                "    float: left;" +
                "    padding-right: 2%;" +
                "}" +
                "" +
                ".info label span.data {" +
                "    display: block;" +
                "    width: 48%;" +
                "    float: left;" +
                "    font-weight: bold !important;" +
                "}" +
                "" +
                ".searchparam_style {" +
                "    font-size: 25px;" +
                "    padding-right: 10px;" +
                "    padding-left: 10px;" +
                "    text-align: center;" +
                "}" +
                "" +
                ".pl-10 {" +
                "    padding-left: 10px;" +
                "}" +
                "" +
                "@media print {" +
                "    @page {" +
                "        size: landscape" +
                "    }" +
                "}" +
                "" +
                ".fixed-footer {" +
                "    bottom: 0;" +
                "}" +
                "" +
                ".container {" +
                "    width: 80%;" +
                "    margin: 0 auto;" +
                "    /* Center the DIV horizontally */" +
                "}" +
                "" +
                ".h1-clone {" +
                "    font-size: 14px;" +
                "    font-weight: bold;" +
                "}" +
                "" +
                ".h1-display {" +
                "    display: inline-block;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div>");

        if (reportExcelDto.getNumberOfColumnPerPage() == null || (reportExcelDto.getNumberOfColumnPerPage() != null && headerName.size() <= reportExcelDto.getNumberOfColumnPerPage())) {
            reportExcelDto.setNumberOfColumnPerPage(headerName.size());
        }

        Integer numberOfRecordsPerPage = reportExcelDto.getNumberOfRecordsPerPage();
        if (reportExcelDto.getNumberOfRecordsPerPage() == null) {
            numberOfRecordsPerPage = Boolean.TRUE.equals(reportExcelDto.getIsLandscape()) ? 15 : 20;
        }

        List<String> referenceColumns = new ArrayList<>();
        if (!CollectionUtils.isEmpty(reportExcelDto.getReferenceColumns())) {
            referenceColumns = new ArrayList<>(new LinkedHashSet<>(reportExcelDto.getReferenceColumns()));
        }

        if (!referenceColumns.isEmpty()) {
            headerName.removeAll(referenceColumns);
        }
        List<String> headers = headerName;
        for (int i = 0; i < headerName.size(); i += reportExcelDto.getNumberOfColumnPerPage()) {
            headers.addAll(i, referenceColumns);
        }
        headerName = headers;

        if (!CollectionUtils.isEmpty(headerName)) {
            int i;
            int j;
            for (i = 0; i < queryData.size(); i += numberOfRecordsPerPage) {
                int m;
                int n;
                for (m = 0; m < headerName.size(); m += reportExcelDto.getNumberOfColumnPerPage()) {
                    sb.append("<table style=\"font-size: 14px;\">")
                            .append("<tr>");
                    for (n = m; n < (m + reportExcelDto.getNumberOfColumnPerPage()); n += 1) {
                        if (n >= headerName.size()) {
                            break;
                        }
                        if (headerName.get(n).startsWith("percent_col")) {
                            sb.append("<th>").append("%").append("</th>");
                        } else {
                            sb.append("<th>").append(headerName.get(n)).append("</th>");
                        }
                    }
                    sb.append("</tr>");

                    for (j = i; j < (i + numberOfRecordsPerPage); j += 1) {
                        if (j >= queryData.size()) {
                            break;
                        }

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        LinkedHashMap<String, Object> data = queryData.get(j);
                        sb.append("<tr>");
                        for (n = m; n < (m + reportExcelDto.getNumberOfColumnPerPage()); n += 1) {
                            if (n >= headerName.size()) {
                                break;
                            }
                            sb.append("<td>");
                            Object value = data.get(headerName.get(n));
                            if (value != null) {
                                if (value instanceof String) {
                                    String parsedValue = Jsoup.parse((String) value).text();
                                    if (headerName.get(n).equals("Member Name EN")) {
//                                        parsedValue = translate.translate(parsedValue);
                                    }
                                    sb.append(parsedValue);
                                } else if (value instanceof Date) {
                                    sb.append(dateFormat.format(value));
                                } else if (value instanceof java.sql.Date) {
                                    sb.append(dateFormat.format((java.sql.Date) value));
                                } else if (value instanceof Boolean) {
                                    sb.append(value.toString());
                                } else if (value instanceof Integer) {
                                    sb.append(value);
                                } else if (value instanceof Float) {
                                    sb.append(value);
                                } else if (value instanceof BigInteger) {
                                    sb.append(((BigInteger) value).intValue());
                                } else if (value instanceof BigDecimal) {
                                    if (value.toString().contains(".")) {
                                        sb.append(((BigDecimal) value).setScale(2, RoundingMode.CEILING).toString());
                                    } else {
                                        sb.append(((BigDecimal) value).intValue());
                                    }
                                } else if (value instanceof Double) {
                                    sb.append(value);
                                } else if (value instanceof Short) {
                                    sb.append(value);
                                }
                            }
                            sb.append("</td>");
                        }
                        sb.append("</tr>");
                    }
                    sb.append("</table>");
                    if (!(n == headerName.size() && j == queryData.size())) {
                        sb.append("<div class=\"clearfix\"></div><p style=\"page-break-before: always\"></p>");
                    }
                }
            }
        }

        sb.append("</div>" +
                "</body>" +
                "</html>");

        return this.generatePdfExtraContent(sb.toString(), reportExcelDto, filterObjList);
    }

    private ByteArrayOutputStream generatePdfExtraContent(String mainHtml, ReprotExcelDto reportExcelDto, List<FilterObj> filterObjList) throws IOException, InterruptedException {
        Pdf pdf = new Pdf();
        pdf.addPageFromString(mainHtml);
        String userName;
        if(Objects.nonNull(reportExcelDto.getUserId())){
            // When user is not logged in and pdf is getting generated by scheduler
            UserMaster userMaster = userDao.retrieveById(reportExcelDto.getUserId());
            if(Objects.nonNull(userMaster)) {
                userName = userMaster.getFirstName() + " " + userMaster.getMiddleName() + " " + userMaster.getLastName();
            }else{
                userName = "";
            }
        }else{
            userName = user.getName();
        }
        String imageDirectoryPath = ConstantUtil.REPOSITORY_PATH + ConstantUtil.IMPLEMENTATION_TYPE + File.separator + "images" + File.separator;
        String medplatImgPath = imageDirectoryPath + "medplat.png";

        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        currentTimeFormat.setTimeZone(Calendar.getInstance().getTimeZone());

        StringBuilder coverHtml = new StringBuilder("<!DOCTYPE html>\n"
                + "<html>\n"
                + "\n"
                + "<head>\n"
                + "    <meta http-equiv=Content-Type content=\"text/html; charset=UTF-8\">\n"
                + "   <h4 style=\"text-align:right;\">\n"
                + "   <img src=\"file://" + medplatImgPath + "\" height=\"50\" width=\"80\">\n"
                + "   </h4>\n"
                + "    <style>\n"
                + "        body {\n"
                + "            margin-top: 10%;\n"
                + "            text-align: center;\n"
                + "            font-family: 'Tinos', serif;\n"
                + "            font-weight: bold;\n"
                + "            font-size: xx-large;\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "\n"
                + "<body>\n"
                + "<br>\n"
                + "<h3>\n"
                + reportExcelDto.getReportName()
                + "</h3>\n"
                + "<br>\n"
                + "<br>\n"
                + "<h5 style=\"text-decoration: underline; font-weight: normal;\">"
                + "SEARCH PARAMETERS"
                + " :</h5>\n"
                + "<h5>Date : <span style=\"font-weight: normal;\">" + currentTimeFormat.format(new Date()) + "</span></h5>\n");

        if (filterObjList != null) {
            for (FilterObj filterObj : filterObjList) {
                coverHtml.append("<h5>").append(filterObj.getDisplayName()).append(" : <span style=\"font-weight: normal;\">").append(filterObj.getValue()).append("</span></h5>\n");
            }
        }

        coverHtml.append("</body></html>");
        Path coverPath = Files.createTempFile("report_cover", ".html");
        Files.write(coverPath, coverHtml.toString().getBytes());

        String headerHtml = "<!DOCTYPE html>\n"
                + "<html>\n"
                + " <head>\n"
                + "   <h4 style=\"text-align:right;\">\n"
                + "   <img src=\"file://" + medplatImgPath + "\" height=\"50\" width=\"80\">\n"
                + "   </h4>\n"
                + "  </head>\n"
                + "</html>";
        Path headerPath = Files.createTempFile("report_header", ".html");
        Files.write(headerPath, headerHtml.getBytes());
        String footerHtml = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "\n"
                + "<head>\n"
                + "  <meta http-equiv=Content-Type content=\"text/html; charset=UTF-8\">\n"
                + "  <script>\n"
                + "    function subst() {\n"
                + "      var vars = {};\n"
                + "      var x = window.location.search.substring(1).split('&');\n"
                + "      for (var i in x) { var z = x[i].split('=', 2); vars[z[0]] = unescape(z[1]); }\n"
                + "      var x = ['frompage', 'topage', 'page', 'webpage', 'section', 'subsection', 'subsubsection'];\n"
                + "      for (var i in x) {\n"
                + "        var y = document.getElementsByClassName(x[i]);\n"
                + "        for (var j = 0; j < y.length; ++j) y[j].textContent = vars[x[i]];\n"
                + "      }\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "\n"
                + "<body style=\"border:0; margin: 0;\" onload=\"subst()\">\n"
                + "  <table style=\"width: 100%\">\n"
                + "    <tr>\n"
                + "      <td>Generated by " + userName + " on\n"
                + "        <script>\n"
                + "          const current_datetime = new Date();\n"
                + "          const date = current_datetime.getDate() < 10 ? '0' + current_datetime.getDate() : current_datetime.getDate();\n"
                + "          const month = current_datetime.getMonth() < 9 ? '0' + (current_datetime.getMonth() + 1) : (current_datetime.getMonth() + 1);\n"
                + "          const year = current_datetime.getFullYear();\n"
                + "          const hours = current_datetime.getHours() < 10 ? '0' + current_datetime.getHours() : current_datetime.getHours();\n"
                + "          const minutes = current_datetime.getMinutes() < 10 ? '0' + current_datetime.getMinutes() : current_datetime.getMinutes();\n"
                + "          const seconds = current_datetime.getSeconds() < 10 ? '0' + current_datetime.getSeconds() : current_datetime.getSeconds();\n"
                + "\n"
                + "          document.write(date + \"-\" + month + \"-\" + year + \" \" + hours + \":\" + minutes + \":\" + seconds);\n"
                + "\n"
                + "        </script>\n"
                + "      </td>\n"
                + "      <td style=\"text-align:right\">\n"
                + "        Page <span class=\"page\"></span> of <span class=\"topage\"></span>\n"
                + "      </td>\n"
                + "    </tr>\n"
                + "  </table>\n"
                + "</body>\n"
                + "\n"
                + "</html>";
        Path footerPath = Files.createTempFile("report_footer", ".html");
        Files.write(footerPath, footerHtml.getBytes());
        pdf.addParam( new Param("--enable-local-file-access"),
                new Param("--orientation", Boolean.TRUE.equals(reportExcelDto.getIsLandscape()) ? "Landscape" : "Portrait"),
                new Param("--enable-javascript"), new Param("--header-html", headerPath.toString()),
                new Param("--header-spacing", "3"), new Param("--footer-html", footerPath.toString()),
                new Param("--footer-spacing", "3"), new Param("cover", coverPath.toString()),
                new Param("--exclude-from-outline"));

        Date currentDate = new Date();
        String filePath = "report_" + currentDate.getDate() + "_" + currentDate.getMonth() + "_"
                + currentDate.getYear() + "_" + currentDate.getTime() + ".pdf";
        Path tempOutputFile = Files.createTempFile(filePath, ".pdf");
        File file = pdf.saveAsDirect(tempOutputFile.toString());

        ByteArrayOutputStream outputStream;
        try (FileInputStream fis = new FileInputStream(file)) {
            outputStream = new ByteArrayOutputStream();

            byte[] buf = new byte[1024];
            try {
                for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                    outputStream.write(buf, 0, readNum); // no doubt here is 0
                }
            } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
            }
        }

        Files.deleteIfExists(headerPath);
        Files.deleteIfExists(footerPath);
        Files.deleteIfExists(coverPath);

        return outputStream;
    }

    @Override
    public ByteArrayOutputStream downLoadExcelForReport(Integer id, UUID uuid, ReprotExcelDto reportExcelDto) throws IOException {
        Map<String, String> parameterMap = new HashMap<>(reportExcelDto.getParamObj());
        List<FilterObj> filterObjList = reportExcelDto.getFilterObj();
        Integer userId;
        if (Objects.nonNull(reportExcelDto.getUserId())) {
            // When user is not logged in and pdf is getting generated by scheduler
            userId = reportExcelDto.getUserId();
        } else {
            userId = user.getId();
        }

        List<LinkedHashMap<String, Object>> queryData;
        if (Objects.nonNull(uuid)) {
            queryData = this.retrieveDataByQueryUUID(uuid, parameterMap, userId);
        } else {
            queryData = this.retrieveDataByQueryId(id, parameterMap, userId);
        }
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        int rownum = 0;
        int cellnum = 0;

        Row rowFilterName = sheet.createRow(rownum++);

        Cell currentDateLabel = rowFilterName.createCell(cellnum);
        currentDateLabel.setCellValue("Date");
        currentDateLabel.setCellStyle(style);
        cellnum = cellnum + 2;

        // for filter name
        if (filterObjList != null) {
            for (FilterObj filterObj : filterObjList) {
                Cell cellFilterName = rowFilterName.createCell(cellnum);
                cellFilterName.setCellValue(filterObj.getDisplayName());
                cellFilterName.setCellStyle(style);
                cellnum = cellnum + 2;
            }
        }

        // for current time value
        cellnum = 0;
        Row rowFilterValue = sheet.createRow(rownum++);
        Cell currentDateValue = rowFilterValue.createCell(cellnum);
        SimpleDateFormat currentTimeFormate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        currentTimeFormate.setTimeZone(Calendar.getInstance().getTimeZone());
        currentDateValue.setCellValue(currentTimeFormate.format(new Date()));
        currentDateValue.setCellStyle(style);
        cellnum = cellnum + 2;

        // for filter value
        if (filterObjList != null) {
            for (FilterObj filterObj : filterObjList) {
                Cell cellFilterName = rowFilterValue.createCell(cellnum);
                cellFilterName.setCellValue(filterObj.getValue());
                cellFilterName.setCellStyle(style);
                cellnum = cellnum + 2;
            }
        }

        rownum++;

        Set<String> headerName = queryData.get(0).keySet();
        List<String> hiddenParameterList = new ArrayList<>();
        Pattern myPattern = Pattern.compile("hidden");

        for (String header : headerName) {
            Matcher hiddenParam = myPattern.matcher(header);
            if (hiddenParam.find()) {
                hiddenParameterList.add(header);
            }
        }
        if (!hiddenParameterList.isEmpty()) {
            headerName.removeAll(hiddenParameterList);
        }

        cellnum = 0;
        Row memberHeader = sheet.createRow(rownum++);
        for (String header : headerName) {
            Cell cellMemberFiedl = memberHeader.createCell(cellnum++);
            if (header.startsWith("percent_col")) {
                cellMemberFiedl.setCellValue("%");
            } else {
                cellMemberFiedl.setCellValue(header);
            }
            cellMemberFiedl.setCellStyle(style);
        }
        SimpleDateFormat dateFomate = new SimpleDateFormat("dd-MM-yyyy");
        for (LinkedHashMap<String, Object> data1 : queryData) {
            Row row = sheet.createRow(rownum++);
            cellnum = 0;
            for (String header : headerName) {
                Cell cell = row.createCell(cellnum++);
                Object value = data1.get(header);
                if (value != null) {
                    if (value instanceof String) {
                        String parsedValue = Jsoup.parse((String) value).text();
                        if (header.equals("Member Name EN")) {
//                            parsedValue = translate.translate(parsedValue);
                        }
                        cell.setCellValue(parsedValue);
                    } else if (value instanceof Date) {
                        cell.setCellValue(dateFomate.format(value));
                    } else if (value instanceof java.sql.Date) {
                        cell.setCellValue(dateFomate.format((java.sql.Date) value));
                    } else if (value instanceof Boolean) {
                        cell.setCellValue(value.toString());
                    } else if (value instanceof Integer) {
                        cell.setCellValue(((Integer) value));
                    } else if (value instanceof Float) {
                        cell.setCellValue((Float) value);
                    } else if (value instanceof BigInteger) {
                        cell.setCellValue(((BigInteger) value).intValue());
                    } else if (value instanceof BigDecimal) {
                        if (value.toString().contains(".")) {
                            cell.setCellValue(((BigDecimal) value).setScale(2, RoundingMode.CEILING).toString());
                        } else {
                            cell.setCellValue(((BigDecimal) value).intValue());
                        }
                    } else if (value instanceof Double) {
                        cell.setCellValue(((Double) value));
                    } else if (value instanceof Short) {
                        cell.setCellValue((Short) value);
                    }
                }
            }
        }

        ByteArrayOutputStream excelByteArrayOPStream = new ByteArrayOutputStream();

        try {
            workbook.write(excelByteArrayOPStream);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return excelByteArrayOPStream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void offlineReportRequest(Integer id,
                                     ReprotExcelDto reportExcelDto) {
        ReportOfflineDetails reportOfflineDetails = new ReportOfflineDetails();
        reportOfflineDetails.setReportId(id);
        reportOfflineDetails.setReportName(reportExcelDto.getReportName());
        reportExcelDto.setUserId(user.getId()); // This will required to generated report offline
        reportOfflineDetails.setReportParameters(ImtechoUtil.convertObjectToStringUsingGson(reportExcelDto));
        reportOfflineDetails.setUserId(user.getId());
        if(reportExcelDto.getFileType().equals("PDF")){
            reportOfflineDetails.setFileType(ReportOfflineDetails.FILE_TYPE.PDF);
        }else if(reportExcelDto.getFileType().equals("EXCEL")){
            reportOfflineDetails.setFileType(ReportOfflineDetails.FILE_TYPE.EXCEL);
        }
        reportOfflineDetails.setStatus(ReportOfflineDetails.STATUS.REQUESTED);
        reportOfflineDetails.setState(ReportOfflineDetails.STATE.ACTIVE);
        reportOfflineDetailsDao.save(reportOfflineDetails);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReportOfflineDetails> retrieveOfflineReportsByUserId(Integer userId) {
        return reportOfflineDetailsDao.retrieveByUserId(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReportOfflineDetails retrieveOfflineReportById(Integer id) {
        return reportOfflineDetailsDao.retrieveById(id);
    }

}
