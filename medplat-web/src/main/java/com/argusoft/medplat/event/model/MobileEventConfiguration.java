/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.event.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * <p>
 *     Define event_mobile_configuration entity and its fields.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "event_mobile_configuration")
public class MobileEventConfiguration implements Serializable {

    @Id
    @Basic(optional = false)
    private String id;
    @Column(name = "notification_code")
    private String notificationCode;
    @Column(name = "number_of_days_added_for_on_date")
    private Integer numberOfDaysAddedForOnDate;
    @Column(name = "number_of_days_added_for_due_date")
    private Integer numberOfDaysAddedForDueDate;
    @Column(name = "number_of_days_added_for_expiry_date")
    private Integer numberOfDaysAddedForExpiryDate;
    @Column(name="notification_type_config_id")
    private String notificationTypeConfigId;

    public String getNotificationCode() {
        return notificationCode;
    }

    public void setNotificationCode(String notificationCode) {
        this.notificationCode = notificationCode;
    }

    public Integer getNumberOfDaysAddedForOnDate() {
        return numberOfDaysAddedForOnDate;
    }

    public void setNumberOfDaysAddedForOnDate(Integer numberOfDaysAddedForOnDate) {
        this.numberOfDaysAddedForOnDate = numberOfDaysAddedForOnDate;
    }

    public Integer getNumberOfDaysAddedForDueDate() {
        return numberOfDaysAddedForDueDate;
    }

    public void setNumberOfDaysAddedForDueDate(Integer numberOfDaysAddedForDueDate) {
        this.numberOfDaysAddedForDueDate = numberOfDaysAddedForDueDate;
    }

    public Integer getNumberOfDaysAddedForExpiryDate() {
        return numberOfDaysAddedForExpiryDate;
    }

    public void setNumberOfDaysAddedForExpiryDate(Integer numberOfDaysAddedForExpiryDate) {
        this.numberOfDaysAddedForExpiryDate = numberOfDaysAddedForExpiryDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotificationTypeConfigId() {
        return notificationTypeConfigId;
    }

    public void setNotificationTypeConfigId(String notificationTypeConfigId) {
        this.notificationTypeConfigId = notificationTypeConfigId;
    }
    
    

}
