package com.argusoft.sewa.android.app.databean;

import java.util.Date;

public class ChardhamEmergencyRequestDto {
    private Integer requestId;
    private String requestState;
    private Integer healthInfraId;
    private String requestDescription;
    private String requestToType;
    private Integer userId;
    private String completedRemarks;
    private Date createdOn;
    private Date completedOn;

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getRequestState() {
        return requestState;
    }

    public void setRequestState(String requestState) {
        this.requestState = requestState;
    }

    public Integer getHealthInfraId() {
        return healthInfraId;
    }

    public void setHealthInfraId(Integer healthInfraId) {
        this.healthInfraId = healthInfraId;
    }

    public String getRequestDescription() {
        return requestDescription;
    }

    public void setRequestDescription(String requestDescription) {
        this.requestDescription = requestDescription;
    }

    public String getRequestToType() {
        return requestToType;
    }

    public void setRequestToType(String requestToType) {
        this.requestToType = requestToType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCompletedRemarks() {
        return completedRemarks;
    }

    public void setCompletedRemarks(String completedRemarks) {
        this.completedRemarks = completedRemarks;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(Date completedOn) {
        this.completedOn = completedOn;
    }
}
