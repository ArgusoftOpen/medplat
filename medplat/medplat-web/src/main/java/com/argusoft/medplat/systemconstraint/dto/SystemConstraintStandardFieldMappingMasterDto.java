package com.argusoft.medplat.systemconstraint.dto;

import java.util.Date;
import java.util.UUID;

public class SystemConstraintStandardFieldMappingMasterDto {

    private UUID uuid;
    private Integer standardMasterId;
    private UUID standardFieldMasterUuid;
    private Date createdOn;
    private Integer createdBy;

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
}
