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
@Table(name = "system_constraint_field_master")
public class SystemConstraintFieldMaster extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "uuid")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID uuid;

    @Column(name = "form_master_uuid")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID formMasterUuid;

    @Column(name = "field_key")
    private String fieldKey;

    @Column(name = "field_name")
    private String fieldName;

    @Column(name = "field_type")
    private String fieldType;

    @Column(name = "ng_model")
    private String ngModel;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "standard_field_master_uuid")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID standardFieldMasterUuid;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getFormMasterUuid() {
        return formMasterUuid;
    }

    public void setFormMasterUuid(UUID formMasterUuid) {
        this.formMasterUuid = formMasterUuid;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public UUID getStandardFieldMasterUuid() {
        return standardFieldMasterUuid;
    }

    public void setStandardFieldMasterUuid(UUID standardFieldMasterUuid) {
        this.standardFieldMasterUuid = standardFieldMasterUuid;
    }

    public String getNgModel() {
        return ngModel;
    }

    public void setNgModel(String ngModel) {
        this.ngModel = ngModel;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public static class Fields {
        public static final String UUID = "uuid";
        public static final String FORM_MASTER_UUID = "formMasterUuid";
        public static final String FIELD_KEY = "fieldKey";
        public static final String FIELD_NAME = "fieldName";
        public static final String FIELD_TYPE = "fieldType";
        public static final String STANDARD_FIELD_MASTER_UUID = "standardFieldMasterUuid";
        public static final String NG_MODEL = "ngModel";
        public static final String APP_NAME = "appName";
    }
}
