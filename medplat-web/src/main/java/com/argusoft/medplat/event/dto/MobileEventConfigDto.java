/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.event.dto;

/**
 *
 * <p>
 *     Used for mobile event.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
public class MobileEventConfigDto {
    private String id;
    private String notificationCode;
    private Integer numberOfDaysAddedForOnDate;
    private Integer numberOfDaysAddedForDueDate;
    private Integer numberOfDaysAddedForExpiryDate;
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
