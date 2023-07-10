/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.course.dto;

import com.argusoft.medplat.common.dto.FieldValueMasterDto;
import com.argusoft.medplat.course.model.CourseMaster;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author akshar
 */
public class CourseMasterDto {

    private Integer courseId;
    private String courseName;
    private String courseDescription;
    private Set<TopicMasterDto> topicMasterDtos = new HashSet<>();
    private CourseMaster.State courseState;
    private CourseMaster.CourseType courseType;
    private Set<Integer> roleIds;
    private Set<Integer> trainerRoleIds;
    private Integer courseModuleId;
    private Map<String, List<FieldValueMasterDto>> dropDown;
    private String targetRole;
    private String trainerRole;
    private String moduleName;
    private Integer duration;
    private String createdByUserName;
    private String testConfigJson;
    private Integer estimatedTimeInHrs;
    private String courseImageJson;
    private Boolean isAllowedToSkipLessons;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public CourseMaster.State getCourseState() {
        return courseState;
    }

    public void setCourseState(CourseMaster.State courseState) {
        this.courseState = courseState;
    }

    public Set<TopicMasterDto> getTopicMasterDtos() {
        return topicMasterDtos;
    }

    public void setTopicMasterDtos(Set<TopicMasterDto> topicMasterDtos) {
        this.topicMasterDtos = topicMasterDtos;
    }

    public Set<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public Set<Integer> getTrainerRoleIds() {
        return trainerRoleIds;
    }

    public void setTrainerRoleIds(Set<Integer> trainerRoleIds) {
        this.trainerRoleIds = trainerRoleIds;
    }

    public Integer getCourseModuleId() {
        return courseModuleId;
    }

    public void setCourseModuleId(Integer courseModuleId) {
        this.courseModuleId = courseModuleId;
    }

    public Map<String, List<FieldValueMasterDto>> getDropDown() {
        return dropDown;
    }

    public void setDropDown(Map<String, List<FieldValueMasterDto>> dropDown) {
        this.dropDown = dropDown;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public String getTrainerRole() {
        return trainerRole;
    }

    public void setTrainerRole(String trainerRole) {
        this.trainerRole = trainerRole;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public CourseMaster.CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseMaster.CourseType courseType) {
        this.courseType = courseType;
    }

    public String getCreatedByUserName() {
        return createdByUserName;
    }

    public void setCreatedByUserName(String createdByUserName) {
        this.createdByUserName = createdByUserName;
    }

    public String getTestConfigJson() {
        return testConfigJson;
    }

    public void setTestConfigJson(String testConfigJson) {
        this.testConfigJson = testConfigJson;
    }

    public Integer getEstimatedTimeInHrs() {
        return estimatedTimeInHrs;
    }

    public void setEstimatedTimeInHrs(Integer estimatedTimeInHrs) {
        this.estimatedTimeInHrs = estimatedTimeInHrs;
    }

    public String getCourseImageJson() {
        return courseImageJson;
    }

    public void setCourseImageJson(String courseImageJson) {
        this.courseImageJson = courseImageJson;
    }


    public Boolean getIsAllowedToSkipLessons() {
        return isAllowedToSkipLessons;
    }

    public void setIsAllowedToSkipLessons(Boolean isAllowedToSkipLessons) {
        this.isAllowedToSkipLessons = isAllowedToSkipLessons;
    }
}
