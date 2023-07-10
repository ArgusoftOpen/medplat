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
@Table(name = "system_constraint_standard_field_master")
public class SystemConstraintStandardFieldMaster extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "uuid")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID uuid;

    @Column(name = "field_key")
    private String fieldKey;

    @Column(name = "field_name")
    private String fieldName;

    @Column(name = "field_type")
    private String fieldType;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "state")
    private String state;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public static class Fields {
        public static final String UUID = "uuid";
        public static final String FIELD_KEY = "fieldKey";
        public static final String FIELD_NAME = "fieldName";
        public static final String FIELD_TYPE = "fieldType";
        public static final String APP_NAME = "appName";
        public static final String CATEGORY_ID = "categoryId";
        public static final String STATE = "state";
    }
}
