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
@Table(name = "system_constraint_standard_field_mapping_master")
public class SystemConstraintStandardFieldMappingMaster extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "uuid")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID uuid;

    @Column(name = "standard_master_id")
    private Integer standardMasterId;

    @Column(name = "standard_field_master_uuid")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID standardFieldMasterUuid;

    public SystemConstraintStandardFieldMappingMaster() {
    }

    public SystemConstraintStandardFieldMappingMaster(UUID uuid, Integer standardMasterId, UUID standardFieldMasterUuid) {
        this.uuid = uuid;
        this.standardMasterId = standardMasterId;
        this.standardFieldMasterUuid = standardFieldMasterUuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Integer getStandardMasterId() {
        return standardMasterId;
    }

    public void setStandardMasterId(Integer standardMasterId) {
        this.standardMasterId = standardMasterId;
    }

    public UUID getStandardFieldMasterUuid() {
        return standardFieldMasterUuid;
    }

    public void setStandardFieldMasterUuid(UUID standardFieldMasterUuid) {
        this.standardFieldMasterUuid = standardFieldMasterUuid;
    }

    public static class Fields {
        public static final String UUID = "uuid";
        public static final String STANDARD_MASTER_ID = "standardMasterId";
        public static final String STANDARD_FIELD_MASTER_UUID = "standardFieldMasterUuid";
    }
}
