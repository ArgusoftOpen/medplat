package com.argusoft.medplat.systemconstraint.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "system_constraint_form_version")
public class SystemConstraintFormVersion extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "form_master_uuid")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID formMasterUuid;

    @Column(name = "template_config")
    private String templateConfig;

    @Column(name = "version")
    private Integer version;

    @Column(name = "type")
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getFormMasterUuid() {
        return formMasterUuid;
    }

    public void setFormMasterUuid(UUID formMasterUuid) {
        this.formMasterUuid = formMasterUuid;
    }

    public String getTemplateConfig() {
        return templateConfig;
    }

    public void setTemplateConfig(String templateConfig) {
        this.templateConfig = templateConfig;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
