package com.argusoft.medplat.systemconstraint.dto;

import java.util.Date;
import java.util.UUID;

public class SystemConstraintFormMasterDto {

    private UUID uuid;
    private String formName;
    private String formCode;
    private Integer menuConfigId;
    private String webTemplateConfig;
    private String mobileTemplateConfig;
    private String menuName;
    private String webState;
    private String mobileState;
    private Date createdOn;
    private Integer createdBy;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public Integer getMenuConfigId() {
        return menuConfigId;
    }

    public void setMenuConfigId(Integer menuConfigId) {
        this.menuConfigId = menuConfigId;
    }

    public String getWebTemplateConfig() {
        return webTemplateConfig;
    }

    public void setWebTemplateConfig(String webTemplateConfig) {
        this.webTemplateConfig = webTemplateConfig;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getWebState() {
        return webState;
    }

    public void setWebState(String webState) {
        this.webState = webState;
    }

    public String getMobileState() {
        return mobileState;
    }

    public void setMobileState(String mobileState) {
        this.mobileState = mobileState;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getMobileTemplateConfig() {
        return mobileTemplateConfig;
    }

    public void setMobileTemplateConfig(String mobileTemplateConfig) {
        this.mobileTemplateConfig = mobileTemplateConfig;
    }
}
