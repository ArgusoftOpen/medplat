package com.argusoft.medplat.systemconstraint.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "system_constraint_form_master")
public class SystemConstraintFormMaster extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "uuid")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID uuid;

    @Column(name = "form_name")
    private String formName;

    @Column(name = "form_code")
    private String formCode;

    @Column(name = "menu_config_id")
    private Integer menuConfigId;

    @Column(name = "web_template_config")
    private String webTemplateConfig;

    @Column(name = "web_state")
    private String webState;

    @Column(name = "mobile_state")
    private String mobileState;

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

    public static class Fields {
        public static final String UUID = "uuid";
        public static final String FORM_NAME = "formName";
        public static final String FORM_CODE = "formCode";
        public static final String MENU_CONFIG_ID = "menuConfigId";
        public static final String WEB_TEMPLATE_CONFIG = "webTemplateConfig";
        public static final String STATE = "state";
    }
}
