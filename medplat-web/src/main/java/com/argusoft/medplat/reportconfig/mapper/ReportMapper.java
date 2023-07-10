/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.reportconfig.mapper;

import com.argusoft.medplat.common.dto.MenuConfigDto;
import com.argusoft.medplat.common.dto.MenuGroupDto;
import com.argusoft.medplat.common.model.MenuConfig;
import com.argusoft.medplat.common.model.MenuGroup;
import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.reportconfig.dto.ReportConfigDto;
import com.argusoft.medplat.reportconfig.dto.ReportParameterConfigDto;
import com.argusoft.medplat.reportconfig.model.ReportMaster;
import com.argusoft.medplat.reportconfig.model.ReportParameterMaster;
import com.argusoft.medplat.web.users.dto.UserMenuItemDto;
import com.argusoft.medplat.web.users.model.UserMenuItem;
import org.hibernate.LazyInitializationException;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * <p>
 *     Mapper for report master in order to convert dto to model or model to dto.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class ReportMapper {
    private ReportMapper(){}

    /**
     * Retrieves entity of report master with null values.
     * @param reportDto Report master details.
     * @return Returns entity of report master.
     */
    public static ReportMaster getReportMasterModel(ReportConfigDto reportDto) {
        return getReportMasterModel(reportDto, null);
    }

    /**
     * Retrieves entity of report master.
     * @param reportConfigDto Report master details.
     * @param reportMaster Entity of report master.
     * @return Returns entity of report master.
     */
    public static ReportMaster getReportMasterModel(ReportConfigDto reportConfigDto, ReportMaster reportMaster) {
        if (reportMaster == null) {
            reportMaster = new ReportMaster();
        }

        if (reportMaster.getUuid() == null) {
            reportMaster.setUuid(reportConfigDto.getUuid());
        }
        reportMaster.setReportType(reportConfigDto.getReportType());
        reportMaster.setActive(Boolean.TRUE);
        reportMaster.setFileName(reportConfigDto.getFileName());
        reportMaster.setReportName(reportConfigDto.getName());
        reportMaster.setCreatedBy(reportConfigDto.getCreatedBy());
        reportMaster.setCreatedOn(reportConfigDto.getCreatedOn());
        reportMaster.setCode(reportConfigDto.getCode());
        reportMaster.setConfigJson(ImtechoUtil.convertJsonToString(reportConfigDto.getConfigJson()));
        if (!CollectionUtils.isEmpty(reportConfigDto.getParameterDtos())) {
            reportMaster.setReportParameterMasterList(getReportParameterMasterList(reportConfigDto.getParameterDtos(), reportMaster));
        }
        return reportMaster;
    }

    /**
     * Retrieves group master dto details.
     * @param menuGroup Entity of menu group.
     * @param uuid UUID.
     * @return Returns menu group dto details.
     */
    public static MenuGroupDto getGroupMasterDto(MenuGroup menuGroup, UUID uuid) {
        MenuGroupDto groupMasterDto = new MenuGroupDto();
        if (menuGroup != null) {
            groupMasterDto.setId(menuGroup.getId());
            groupMasterDto.setGroupUUID(uuid);
            groupMasterDto.setGroupName(menuGroup.getGroupName());
            groupMasterDto.setType(menuGroup.getType());
            groupMasterDto.setIsActive(menuGroup.getIsActive());
        }
        return groupMasterDto;
    }

    /**
     * Retrieves list of report parameter entities.
     * @param parameterDtos List of report master dtos.
     * @param reportMaster Entity of report master.
     * @return Returns list of report parameter entities.
     */
    private static List<ReportParameterMaster> getReportParameterMasterList(List<ReportParameterConfigDto> parameterDtos, ReportMaster reportMaster) {
        List<ReportParameterMaster> reportParameterMasters = new ArrayList<>();
        for (ReportParameterConfigDto parameterDto : parameterDtos) {
            reportParameterMasters.add(getReportParameterMaster(parameterDto, reportMaster));
        }
        return reportParameterMasters;
    }

    /**
     * Retrieves report parameter config entity.
     * @param parameterDto Report parameter details.
     * @param reportMaster Entity of report parameter.
     * @return Returns entity of report parameter.
     */
    private static ReportParameterMaster getReportParameterMaster(ReportParameterConfigDto parameterDto, ReportMaster reportMaster) {
        ReportParameterMaster reportParameterMaster = new ReportParameterMaster();
        reportParameterMaster.setDefaultValue(parameterDto.getValue());
        reportParameterMaster.setImplicitParameter(parameterDto.getImplicitParameter());
        reportParameterMaster.setLabel(parameterDto.getLabel());
        reportParameterMaster.setName(parameterDto.getModel());
        reportParameterMaster.setOptions(parameterDto.getAvailableOptions());
        reportParameterMaster.setQuery(parameterDto.getQuery());
        reportParameterMaster.setReportMasterId(reportMaster);
        reportParameterMaster.setRptDataType(parameterDto.getRptDataType());
        reportParameterMaster.setReportName(parameterDto.getReportName());
        reportParameterMaster.setType(parameterDto.getType());
        reportParameterMaster.setId(parameterDto.getId());
        reportParameterMaster.setIsRequired(parameterDto.getIsRequired());
        reportParameterMaster.setIsQuery(parameterDto.getIsQuery());
        return reportParameterMaster;
    }

    /**
     * Retrieves menu config entities.
     * @param reportConfigDto Report config details.
     * @return Returns entity of report config.
     */
    public static MenuConfig getMenuConfigModelFromReportConfigDto(ReportConfigDto reportConfigDto) {
        MenuConfig menuConfig = new MenuConfig();
        if (reportConfigDto.getId() != null && reportConfigDto.getMenuConfig() != null) {
            menuConfig.setId(reportConfigDto.getMenuConfig().getId());
            menuConfig.setNavigationState(reportConfigDto.getMenuConfig().getNavigationState());
        }

        menuConfig.setGroupId(reportConfigDto.getParentGroupId());
        menuConfig.setType(reportConfigDto.getMenuType());
        menuConfig.setName(reportConfigDto.getName());
        menuConfig.setSubGroupId(reportConfigDto.getSubGroupId());
        menuConfig.setType(reportConfigDto.getMenuType());
        menuConfig.setIsActive(Boolean.TRUE);
        if (reportConfigDto.getMenuConfig() != null && reportConfigDto.getMenuConfig().getMenuTypeUUID() != null) {
            menuConfig.setMenuTypeUUID(reportConfigDto.getMenuConfig().getMenuTypeUUID());
        } else {
            menuConfig.setMenuTypeUUID(UUID.randomUUID());
        }
        if (reportConfigDto.getParentGroup() != null) {
            menuConfig.setGroupNameUUID(reportConfigDto.getParentGroup().getGroupUUID());
        }
        if (reportConfigDto.getSubGroup() != null) {
            menuConfig.setSubGroupUUID(reportConfigDto.getSubGroup().getGroupUUID());
        }

        return menuConfig;
    }

    /**
     * Retrieves report config details.
     * @param reportMaster Entity of report config.
     * @return Returns report config details.
     */
    public static ReportConfigDto getReportConfigDto(ReportMaster reportMaster) {
        ReportConfigDto reportDto = new ReportConfigDto();
        if (reportMaster != null) {
            reportDto.setId(reportMaster.getId());
            reportDto.setUuid(reportMaster.getUuid());
            reportDto.setName(reportMaster.getReportName());
            reportDto.setFileName(reportMaster.getFileName());
            reportDto.setReportType(reportMaster.getReportType());
            if (reportMaster.getMenuConfig() != null) {
                reportDto.setSubGroupId(reportMaster.getMenuConfig().getSubGroupId());
                reportDto.setParentGroupId(reportMaster.getMenuConfig().getGroupId());
                reportDto.setParentGroup(getGroupMasterDto(reportMaster.getMenuConfig().getMenuGroup(), reportMaster.getMenuConfig().getGroupNameUUID()));
                reportDto.setSubGroup(getGroupMasterDto(reportMaster.getMenuConfig().getSubGroup(), reportMaster.getMenuConfig().getSubGroupUUID()));
                reportDto.setMenuType(reportMaster.getMenuConfig().getType());
            }
            reportDto.setMenuConfig(getMenuConfigDtoList(reportMaster.getMenuConfig()));
            reportDto.setConfigJson(ImtechoUtil.convertStringToJson(reportMaster.getConfigJson()));
            reportDto.setCreatedBy(reportMaster.getCreatedBy());
            reportDto.setCreatedOn(reportMaster.getCreatedOn());
            reportDto.setCode(reportMaster.getCode());
            try {
                reportDto.setParameterDtos(getReportParameterConfigDtoList(reportMaster.getReportParameterMasterList()));
            } catch (LazyInitializationException lazyInitializationException) {
                //Handle exception
            }
        }
        return reportDto;
    }

    /**
     * Retrieves report parameter config details.
     * @param reportParameterMaster Entity of report parameter config.
     * @return Returns report parameter config details.
     */
    public static ReportParameterConfigDto getReportParameterConfigDto(ReportParameterMaster reportParameterMaster) {
        ReportParameterConfigDto reportParameterConfigDto = new ReportParameterConfigDto();
        if (reportParameterMaster != null) {
            reportParameterConfigDto.setId(reportParameterMaster.getId());
            reportParameterConfigDto.setIsEditable(true);
            reportParameterConfigDto.setImplicitParameter(reportParameterMaster.getImplicitParameter());
            reportParameterConfigDto.setLabel(reportParameterMaster.getLabel());
            reportParameterConfigDto.setModel(reportParameterMaster.getName());
            reportParameterConfigDto.setQuery(reportParameterMaster.getQuery());
            reportParameterConfigDto.setType(reportParameterMaster.getType());
            reportParameterConfigDto.setValue(reportParameterMaster.getDefaultValue());
            reportParameterConfigDto.setAvailableOptions(reportParameterMaster.getOptions());
            reportParameterConfigDto.setRptDataType(reportParameterMaster.getRptDataType());
            reportParameterConfigDto.setReportName(reportParameterMaster.getReportName());
            reportParameterConfigDto.setIsQuery(reportParameterMaster.getIsQuery());
            reportParameterConfigDto.setIsRequired(reportParameterMaster.getIsRequired());

        }
        return reportParameterConfigDto;
    }

    /**
     * Retrieves list of report parameter config details.
     * @param reportParameterMasters List of report parameter config entities.
     * @return Returns list of report parameter config details.
     */
    public static List<ReportParameterConfigDto> getReportParameterConfigDtoList(List<ReportParameterMaster> reportParameterMasters) {
        List<ReportParameterConfigDto> reportParameterConfigDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(reportParameterMasters)) {
            for (ReportParameterMaster reportParameterMaster : reportParameterMasters) {
                reportParameterConfigDtos.add(getReportParameterConfigDto(reportParameterMaster));
            }
        }
        return reportParameterConfigDtos;
    }

    /**
     * Retrieves menu config details.
     * @param menuConfig Entity of menu config.
     * @return Returns  menu config details.
     */
    private static MenuConfigDto getMenuConfigDtoList(MenuConfig menuConfig) {
        MenuConfigDto menuConfigDto = new MenuConfigDto();
        if (menuConfig != null) {
            menuConfigDto.setNavigationState(menuConfig.getNavigationState());
            menuConfigDto.setId(menuConfig.getId());
            menuConfigDto.setMenuTypeUUID(menuConfig.getMenuTypeUUID());
            menuConfigDto.setName(menuConfig.getName());
            menuConfigDto.setIsDynamicReport(menuConfig.getIsDynamicReport());
            try {
                if (!CollectionUtils.isEmpty(menuConfig.getUserMenuItemList())) {
                    List<UserMenuItemDto> userMenuItemDtos = new ArrayList<>();
                    for (UserMenuItem userMenuItem : menuConfig.getUserMenuItemList()) {
                        UserMenuItemDto userMenuItemDto = new UserMenuItemDto();
                        userMenuItemDto.setId(userMenuItem.getId());
                        userMenuItemDto.setMenuConfigId(userMenuItem.getMenuConfigId());
                        userMenuItemDto.setDesignationId(userMenuItem.getRoleId());
                        userMenuItemDto.setUserId(userMenuItem.getUserId());

                        userMenuItemDtos.add(userMenuItemDto);
                    }
                    menuConfigDto.setUserMenuItemDtos(userMenuItemDtos);
                }
            } catch (LazyInitializationException lazyInitializationException) {
                // Handle exception
            }

        }
        return menuConfigDto;
    }

    /**
     * Retrieves list of report config details.
     * @param reportMasters List of report config entities.
     * @return Returns list of report config details.
     */
    public static List<ReportConfigDto> getReportMasterDtoList(List<ReportMaster> reportMasters) {
        List<ReportConfigDto> reportDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(reportMasters)) {
            reportMasters.stream().forEach(reportMaster -> reportDtos.add(getReportConfigDto(reportMaster)));
        }
        return reportDtos;
    }
}
