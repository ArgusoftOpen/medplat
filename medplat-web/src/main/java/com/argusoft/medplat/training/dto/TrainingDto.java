/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.dto;

import com.argusoft.medplat.training.model.Training;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * <p>
 *     Used for training.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public class TrainingDto {

    private Integer trainingId;

    private String trainingName;

    private String trainingDescription;

    private Training.State trainingState;

    private Date effectiveDate;

    private Date expirationDate;

    private String location;

    private Map<Integer,String> primaryTrainers = new HashMap<>();

    private Map<Integer,String> optionalTrainers = new HashMap<>();

    private Map<Integer,String> organizationUnits = new HashMap<>();

    private Map<Integer,String> courses = new HashMap<>();

    private Map<Integer,String> attendees = new HashMap<>();

    private Map<Integer,String> additionalAttendees = new HashMap<>();

    private Map<Integer,String> primaryTrainerRole = new HashMap<>();
    
    private Map<Integer,String> primaryTargetRole = new HashMap<>();

    public TrainingDto() {

    }

    public TrainingDto(Integer trainingId, String trainingName, String trainingDescription, Training.State trainingState, Date effectiveDate, Date expirationDate, String location, Map<Integer,String> primaryTrainers, Map<Integer,String> optionalTrainers, Map<Integer,String> organizationUnits, Map<Integer,String> courses, Map<Integer,String> attendees, Map<Integer,String> additionalAttendees, Map<Integer,String> primaryTrainerRole,Map<Integer,String> primaryTargetRole) {
        this.trainingId = trainingId;
        this.trainingName = trainingName;
        this.trainingDescription = trainingDescription;
        this.trainingState = trainingState;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.location = location;
        this.primaryTrainers = primaryTrainers;
        this.optionalTrainers = optionalTrainers;
        this.organizationUnits = organizationUnits;
        this.courses = courses;
        this.attendees = attendees;
        this.additionalAttendees = additionalAttendees;
        this.primaryTrainerRole = primaryTrainerRole;
        this.primaryTargetRole = primaryTargetRole;
    }
    
    public TrainingDto(TrainingDto trainingDto){
        this.trainingId = trainingDto.getTrainingId();
        this.trainingName = trainingDto.getTrainingName();
        this.trainingDescription = trainingDto.getTrainingDescription();
        this.trainingState = trainingDto.getTrainingState();
        this.effectiveDate = trainingDto.getEffectiveDate();
        this.expirationDate = trainingDto.getExpirationDate();
        this.location = trainingDto.getLocation();
        this.primaryTrainers = trainingDto.getPrimaryTrainers();
        this.optionalTrainers = trainingDto.getOptionalTrainers();
        this.organizationUnits = trainingDto.getOrganizationUnits();
        this.courses = trainingDto.getCourses();
        this.attendees = trainingDto.getAttendees();
        this.additionalAttendees = trainingDto.getAdditionalAttendees();
        this.primaryTrainerRole = trainingDto.getPrimaryTrainerRole();
        this.primaryTargetRole = trainingDto.getPrimaryTargetRole();
    }
    
    

    public Integer getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Integer trainingId) {
        this.trainingId = trainingId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getTrainingDescription() {
        return trainingDescription;
    }

    public void setTrainingDescription(String trainingDescription) {
        this.trainingDescription = trainingDescription;
    }

    public Training.State getTrainingState() {
        return trainingState;
    }

    public void setTrainingState(Training.State trainingState) {
        this.trainingState = trainingState;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Map<Integer,String> getPrimaryTrainers() {
        return primaryTrainers;
    }

    public void setPrimaryTrainers(Map<Integer,String> primaryTrainers) {
        this.primaryTrainers = primaryTrainers;
    }

    public Map<Integer,String> getOptionalTrainers() {
        return optionalTrainers;
    }

    public void setOptionalTrainers(Map<Integer,String> optionalTrainers) {
        this.optionalTrainers = optionalTrainers;
    }

    public Map<Integer,String> getOrganizationUnits() {
        return organizationUnits;
    }

    public void setOrganizationUnits(Map<Integer,String> organizationUnits) {
        this.organizationUnits = organizationUnits;
    }

    public Map<Integer,String> getCourses() {
        return courses;
    }

    public void setCourses(Map<Integer,String> courses) {
        this.courses = courses;
    }

    public Map<Integer,String> getAttendees() {
        return attendees;
    }

    public void setAttendees(Map<Integer,String> attendees) {
        this.attendees = attendees;
    }

    public Map<Integer,String> getAdditionalAttendees() {
        return additionalAttendees;
    }

    public void setAdditionalAttendees(Map<Integer,String> additionalAttendees) {
        this.additionalAttendees = additionalAttendees;
    }

    public Map<Integer, String> getPrimaryTrainerRole() {
        return primaryTrainerRole;
    }

    public void setPrimaryTrainerRole(Map<Integer, String> primaryTrainerRole) {
        this.primaryTrainerRole = primaryTrainerRole;
    }

    public Map<Integer, String> getPrimaryTargetRole() {
        return primaryTargetRole;
    }

    public void setPrimaryTargetRole(Map<Integer, String> primaryTargetRole) {
        this.primaryTargetRole = primaryTargetRole;
    }

    
}
