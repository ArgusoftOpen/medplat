/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.model;

import javax.persistence.*;

/**
 *<p>Defines fields related to user</p>
 * @author prateek
 * @since 26/08/2020 5:30
 */
@Entity
@Table(name = "sms")
public class Sms extends EntityAuditInfo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "mobile_numbers")
    private String mobileNumbers;
    
    @Column(name = "message")
    private String message;
    
    @Column(name = "response")
    private String response;
    
    @Column(name = "message_type")
    private String messageType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private STATUS  status;

    @Column(name = "exception_string")
    private String exceptionString;
    
    @Column(name = "response_id")
    private String responseId;
    
    @Column(name = "carrier_status")
    private String carrierStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobileNumbers() {
        return mobileNumbers;
    }

    public void setMobileNumbers(String mobileNumbers) {
        this.mobileNumbers = mobileNumbers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
    
    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public String getExceptionString() {
        return exceptionString;
    }

    public void setExceptionString(String exceptionString) {
        this.exceptionString = exceptionString;
    }
    
    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }
    
    public String getCarrierStatus() {
        return carrierStatus;
    }

    public void setCarrierStatus(String carrierStatus) {
        this.carrierStatus = carrierStatus;
    }

    /**
     * Defines value of states
     */
     public enum STATUS {
        SENT,
        EXCEPTION,
        BLOCKED
    }
}
