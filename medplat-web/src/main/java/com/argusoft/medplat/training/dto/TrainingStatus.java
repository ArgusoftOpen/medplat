/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.dto;

import com.argusoft.medplat.training.model.Training;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 *     Used for training status.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public class TrainingStatus {
    private Date effectiveDate;
    private String location;
    private Integer attendeeCount;
    private Integer completedCount;
    private Training.State trainingState;
    private Integer totalNoOfAttendees;
    //request param
    private Integer trainingId;
    private List<TraineeCertificate> traineeCertificates;
    private Boolean isSubmit;

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getAttendeeCount() {
        return attendeeCount;
    }

    public void setAttendeeCount(Integer attendeeCount) {
        this.attendeeCount = attendeeCount;
    }

    public Integer getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(Integer completedCount) {
        this.completedCount = completedCount;
    }

    public Training.State getTrainingState() {
        return trainingState;
    }

    public void setTrainingState(Training.State trainingState) {
        this.trainingState = trainingState;
    }

    public Integer getTotalNoOfAttendees() {
        return totalNoOfAttendees;
    }

    public void setTotalNoOfAttendees(Integer totalNoOfAttendees) {
        this.totalNoOfAttendees = totalNoOfAttendees;
    }

    public Integer getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Integer trainingId) {
        this.trainingId = trainingId;
    }

    public List<TraineeCertificate> getTraineeCertificates() {
        return traineeCertificates;
    }

    public void setTraineeCertificates(List<TraineeCertificate> traineeCertificates) {
        this.traineeCertificates = traineeCertificates;
    }

    public Boolean getIsSubmit() {
        return isSubmit;
    }

    public void setIsSubmit(Boolean isSubmit) {
        this.isSubmit = isSubmit;
    }
    
    
}
