package com.argusoft.medplat.rch.datamigration.dto;

import com.argusoft.medplat.rch.datamigration.model.RchDataMigration;

import java.util.Date;

/**
 * <p>
 * Used for rch data migration.
 * </p>
 *
 * @author shrey
 * @since 26/08/20 11:00 AM
 */
public class RchDataMigrationDto {
    private Integer id;
    private Integer locationId;
    private Date executedOn;
    private RchDataMigration.State state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Date getExecutedOn() {
        return executedOn;
    }

    public void setExecutedOn(Date executedOn) {
        this.executedOn = executedOn;
    }

    public RchDataMigration.State getState() {
        return state;
    }

    public void setState(RchDataMigration.State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "RchDataMigrationDto{" + "id=" + id + ", locationId=" + locationId + ", executedOn=" + executedOn + ", state=" + state + '}';
    }

}
