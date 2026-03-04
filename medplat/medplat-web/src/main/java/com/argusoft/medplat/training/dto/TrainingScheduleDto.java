/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.dto;

import com.argusoft.medplat.training.model.Training;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * <p>
 *     Used for training schedule.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public class TrainingScheduleDto {

    private Integer trainingId;

    private String trainingName;

    private String trainingDescription;

    private Training.State trainingState;

    private Long effectiveDate;

    private Long expirationDate;

    private String location;

    private Set<Integer> primaryTrainers = new HashSet<>();

    private Set<Integer> optionalTrainers = new HashSet<>();

    private Set<Integer> organizationUnits = new HashSet<>();

    private Set<Integer> courses = new HashSet<>();

    private Set<Integer> attendees = new HashSet<>();

    private Set<Integer> additionalAttendees = new HashSet<>();

    private Set<Integer> primaryTrainerRole = new HashSet<>();

    private Set<Integer> primaryTargetRole = new HashSet<>();
    
    private Set<Integer> excludedAttendees = new HashSet<>();

    private String courseType;

    
    public TrainingScheduleDto() {
    }

    public TrainingScheduleDto(Integer trainingId, String trainingName, String trainingDescription, Training.State trainingState,Long effectiveDate, Long expirationDate, String location, Set<Integer> primaryTrainers, Set<Integer> optionalTrainers, Set<Integer> organizationUnits, Set<Integer> courses, Set<Integer> attendees, Set<Integer> additionalAttendees, Set<Integer> primaryTrainerRole, Set<Integer> primaryTargetRole,Set<Integer> excludedAttendees) {
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
        this.excludedAttendees = excludedAttendees;
    }

    public TrainingScheduleDto(TrainingScheduleDto trainingScheduleDto) {
        this.trainingId = trainingScheduleDto.getTrainingId();
        this.trainingName = trainingScheduleDto.getTrainingName();
        this.trainingDescription = trainingScheduleDto.getTrainingDescription();
        this.trainingState = trainingScheduleDto.getTrainingState();
        this.effectiveDate = trainingScheduleDto.getEffectiveDate();
        this.expirationDate = trainingScheduleDto.getExpirationDate();
        this.location = trainingScheduleDto.getLocation();
        this.primaryTrainers = trainingScheduleDto.getPrimaryTrainers();
        this.optionalTrainers = trainingScheduleDto.getOptionalTrainers();
        this.organizationUnits = trainingScheduleDto.getOrganizationUnits();
        this.courses = trainingScheduleDto.getCourses();
        this.attendees = trainingScheduleDto.getAttendees();
        this.additionalAttendees = trainingScheduleDto.getAdditionalAttendees();
        this.primaryTrainerRole = trainingScheduleDto.getPrimaryTrainerRole();
        this.primaryTargetRole = trainingScheduleDto.getPrimaryTargetRole();
        this.excludedAttendees = trainingScheduleDto.getExcludedAttendees();
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

    public Long getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Long effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<Integer> getPrimaryTrainers() {
        return primaryTrainers;
    }

    public void setPrimaryTrainers(Set<Integer> primaryTrainers) {
        this.primaryTrainers = primaryTrainers;
    }

    public Set<Integer> getOptionalTrainers() {
        return optionalTrainers;
    }

    public void setOptionalTrainers(Set<Integer> optionalTrainers) {
        this.optionalTrainers = optionalTrainers;
    }

    public Set<Integer> getOrganizationUnits() {
        return organizationUnits;
    }

    public void setOrganizationUnits(Set<Integer> organizationUnits) {
        this.organizationUnits = organizationUnits;
    }

    public Set<Integer> getCourses() {
        return courses;
    }

    public void setCourses(Set<Integer> courses) {
        this.courses = courses;
    }

    public Set<Integer> getAttendees() {
        return attendees;
    }

    public void setAttendees(Set<Integer> attendees) {
        this.attendees = attendees;
    }

    public Set<Integer> getAdditionalAttendees() {
        return additionalAttendees;
    }

    public void setAdditionalAttendees(Set<Integer> additionalAttendees) {
        this.additionalAttendees = additionalAttendees;
    }

    public Set<Integer> getPrimaryTrainerRole() {
        return primaryTrainerRole;
    }

    public void setPrimaryTrainerRole(Set<Integer> primaryTrainerRole) {
        this.primaryTrainerRole = primaryTrainerRole;
    }

    public Set<Integer> getPrimaryTargetRole() {
        return primaryTargetRole;
    }

    public void setPrimaryTargetRole(Set<Integer> primaryTargetRole) {
        this.primaryTargetRole = primaryTargetRole;
    }

    public Set<Integer> getExcludedAttendees() {
        return excludedAttendees;
    }

    public void setExcludedAttendees(Set<Integer> excludedAttendees) {
        this.excludedAttendees = excludedAttendees;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }
}
