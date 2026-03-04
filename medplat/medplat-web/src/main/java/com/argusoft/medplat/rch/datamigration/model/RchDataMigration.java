package com.argusoft.medplat.rch.datamigration.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Define rch_data_migration entity and its fields.
 * </p>
 *
 * @author shrey
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_data_migration")
public class RchDataMigration extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "location_id")
    private Integer locationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private RchDataMigration.State state;

    @Column(name = "executed_on")
    @Temporal(TemporalType.DATE)
    private Date executedOn;

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

    public RchDataMigration.State getState() {
        return state;
    }

    public void setState(RchDataMigration.State state) {
        this.state = state;
    }

    public Date getExecutedOn() {
        return executedOn;
    }

    public void setExecutedOn(Date executedOn) {
        this.executedOn = executedOn;
    }

    @Override
    public String toString() {
        return "RchDataMigration{" + "id=" + id + ", locationId=" + locationId + ", state=" + state + ", executedOn=" + executedOn + '}';
    }

    /**
     * Define state.
     */
    public enum State {
        PENDING,
        EXECUTED
    }
}
