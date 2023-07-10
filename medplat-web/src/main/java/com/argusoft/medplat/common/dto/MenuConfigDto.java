package com.argusoft.medplat.common.dto;

import com.argusoft.medplat.web.users.dto.UserMenuItemDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.UUID;

/**
 * <p>Defines fields related to menu configuration</p>
 * @author charmi
 * @since 26/08/2020 5:30
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuConfigDto {

    private Integer id;
    private UUID menuTypeUUID;
    private String name;
    private String navigationState;
    private Boolean isGroup;
    private Boolean isSubGroup;
    private List<MenuConfigDto> subGroups;
    private List<UserMenuItemDto> userMenuItemDtos;
    private String featureJson;
    private String groupName;
    private String subGroupName;
    private Boolean isDynamicReport;
    private Integer displayOrder;
    private String description;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getMenuTypeUUID() {
        return menuTypeUUID;
    }

    public void setMenuTypeUUID(UUID menuTypeUUID) {
        this.menuTypeUUID = menuTypeUUID;
    } 
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNavigationState() {
        return navigationState;
    }

    public void setNavigationState(String navigationState) {
        this.navigationState = navigationState;
    }

    public Boolean getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(Boolean isGroup) {
        this.isGroup = isGroup;
    }

    public Boolean getIsSubGroup() {
        return isSubGroup;
    }

    public void setIsSubGroup(Boolean isSubGroup) {
        this.isSubGroup = isSubGroup;
    }

    public List<MenuConfigDto> getSubGroups() {
        return subGroups;
    }

    public void setSubGroups(List<MenuConfigDto> subGroups) {
        this.subGroups = subGroups;
    }

    public List<UserMenuItemDto> getUserMenuItemDtos() {
        return userMenuItemDtos;
    }

    public void setUserMenuItemDtos(List<UserMenuItemDto> userMenuItemDtos) {
        this.userMenuItemDtos = userMenuItemDtos;
    }

    public String getFeatureJson() {
        return featureJson;
    }

    public void setFeatureJson(String featureJson) {
        this.featureJson = featureJson;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSubGroupName() {
        return subGroupName;
    }

    public void setSubGroupName(String subGroupName) {
        this.subGroupName = subGroupName;
    }

    public Boolean getIsDynamicReport() {
        return isDynamicReport;
    }

    public void setIsDynamicReport(Boolean isDynamicReport) {
        this.isDynamicReport = isDynamicReport;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
