package com.argusoft.medplat.mobile.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author prateek on 12 Feb, 2019
 */
@Entity
@Table(name = "blocked_devices_master")
public class BlockedDevicesMaster implements Serializable {

    @Id
    @Column(name = "imei")
    private String imei;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    
    @Column(name = "is_block_device")
    private Boolean isBlockDevice;
    
    @Column(name = "is_delete_database")
    private Boolean isDeleteDatabase;
    
    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Boolean getIsBlockDevice() {
        return isBlockDevice;
    }

    public void setIsBlockDevice(Boolean isBlockDevice) {
        this.isBlockDevice = isBlockDevice;
    }

    public Boolean getIsDeleteDatabase() {
        return isDeleteDatabase;
    }

    public void setIsDeleteDatabase(Boolean isDeleteDatabase) {
        this.isDeleteDatabase = isDeleteDatabase;
    }
}
