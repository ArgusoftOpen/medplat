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
@Table(name = "system_constraint_standard_field_value_master")
public class SystemConstraintStandardFieldValueMaster extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "uuid")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID uuid;

    @Column(name = "standard_field_mapping_master_uuid")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID standardFieldMappingMasterUuid;

    @Column(name = "value_type")
    private String valueType;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

    @Column(name = "default_value")
    private String defaultValue;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getStandardFieldMappingMasterUuid() {
        return standardFieldMappingMasterUuid;
    }

    public void setStandardFieldMappingMasterUuid(UUID standardFieldMappingMasterUuid) {
        this.standardFieldMappingMasterUuid = standardFieldMappingMasterUuid;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public static class Fields {
        public static final String UUID = "uuid";
        public static final String STANDARD_FIELD_MAPPING_MASTER_UUID = "standardFieldMappingMasterUuid";
        public static final String VALUE_TYPE = "valueType";
        public static final String KEY = "key";
        public static final String VALUE = "value";
        public static final String DEFAULT_VALUE = "defaultValue";
    }
}
