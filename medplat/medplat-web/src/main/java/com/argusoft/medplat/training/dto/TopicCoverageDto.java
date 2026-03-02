/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.dto;

import com.argusoft.medplat.training.model.TopicCoverage;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 *     Used for topic coverage.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public class TopicCoverageDto {

    private Integer topicCoverageId;
    private Integer trainingId;
    private Integer courseId;
    private Integer topicId;
    private String topicCoverageName;
    private String topicCoverageDescription;
    private TopicCoverage.State topicCoverageState;
    private Date completedOn;
    private Date submittedOn;
    private String reason;
    private String remarks;
    private Date effectiveDate;
    private Date expirationDate;
    private List<Integer> topicCoverageIds;

    public TopicCoverageDto() {
    }

    public TopicCoverageDto(Integer topicCoverageId, Integer trainingId, Integer courseId, Integer topicId, String topicCoverageName, String topicCoverageDescription, TopicCoverage.State topicCoverageState, Date completedOn, Date submittedOn, String reason, String remarks, Date effectiveDate, Date expirationDate,List<Integer> topicCoverageIds) {
        this.topicCoverageId = topicCoverageId;
        this.trainingId = trainingId;
        this.courseId = courseId;
        this.topicId = topicId;
        this.topicCoverageName = topicCoverageName;
        this.topicCoverageDescription = topicCoverageDescription;
        this.topicCoverageState = topicCoverageState;
        this.completedOn = completedOn;
        this.submittedOn = submittedOn;
        this.reason = reason;
        this.remarks = remarks;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.topicCoverageIds = topicCoverageIds;
    }

    public TopicCoverageDto(TopicCoverageDto topicCoverage) {
        this.trainingId = topicCoverage.getTrainingId();
        this.courseId = topicCoverage.getCourseId();
        this.topicId = topicCoverage.getTopicId();
        this.completedOn = topicCoverage.getCompletedOn();
        this.submittedOn = topicCoverage.getSubmittedOn();
        this.reason = topicCoverage.getReason();
        this.remarks = topicCoverage.getRemarks();
        this.topicCoverageId = topicCoverage.getTopicCoverageId();
        this.topicCoverageName = topicCoverage.getTopicCoverageName();
        this.topicCoverageDescription = topicCoverage.getTopicCoverageDescription();
        this.topicCoverageState = topicCoverage.getTopicCoverageState();
        this.effectiveDate = topicCoverage.getEffectiveDate();
        this.expirationDate = topicCoverage.getExpirationDate();
        this.topicCoverageIds = topicCoverage.getTopicCoverageIds();
    }

    public Integer getTopicCoverageId() {
        return topicCoverageId;
    }

    public void setTopicCoverageId(Integer topicCoverageId) {
        this.topicCoverageId = topicCoverageId;
    }

    public Integer getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Integer trainingId) {
        this.trainingId = trainingId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Date getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(Date completedOn) {
        this.completedOn = completedOn;
    }

    public Date getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(Date submittedOn) {
        this.submittedOn = submittedOn;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getTopicCoverageName() {
        return topicCoverageName;
    }

    public void setTopicCoverageName(String topicCoverageName) {
        this.topicCoverageName = topicCoverageName;
    }

    public String getTopicCoverageDescription() {
        return topicCoverageDescription;
    }

    public void setTopicCoverageDescription(String topicCoverageDescription) {
        this.topicCoverageDescription = topicCoverageDescription;
    }

    public TopicCoverage.State getTopicCoverageState() {
        return topicCoverageState;
    }

    public void setTopicCoverageState(TopicCoverage.State topicCoverageState) {
        this.topicCoverageState = topicCoverageState;
    }

    public List<Integer> getTopicCoverageIds() {
        return topicCoverageIds;
    }

    public void setTopicCoverageIds(List<Integer> topicCoverageIds) {
        this.topicCoverageIds = topicCoverageIds;
    }

}
