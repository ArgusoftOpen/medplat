/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * <p>
 *     Define tr_topic_coverage_master entity and its fields.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "tr_topic_coverage_master")
public class TopicCoverage extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer topicCoverageId;

    @Column(name = "name")
    private String topicCoverageName;

    @Column(name = "descr")
    private String topicCoverageDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State topicCoverageState;

    @Column(name = "expiration_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date topicCoverageExpirationDate;

    @Column(name = "effective_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date topicCoverageEffectiveDate;

    @Column(name = "remarks")
    private String topicCoverageRemarks;

    @Column(name = "reason")
    private String topicCoverageReason;

    @Column(name = "submitted_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date topicCoverageSubmittedOn;

    @Column(name = "completed_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date topicCoverageCompletedOn;

    @Column(name = "topic_id")
    private Integer topicId;

    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "training_id")
    private Integer trainingId;

    public Integer getTopicCoverageId() {
        return topicCoverageId;
    }

    public void setTopicCoverageId(Integer topicCoverageId) {
        this.topicCoverageId = topicCoverageId;
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

    public State getTopicCoverageState() {
        return topicCoverageState;
    }

    public void setTopicCoverageState(State topicCoverageState) {
        this.topicCoverageState = topicCoverageState;
    }

    public Date getTopicCoverageExpirationDate() {
        return topicCoverageExpirationDate;
    }

    public void setTopicCoverageExpirationDate(Date topicCoverageExpirationDate) {
        this.topicCoverageExpirationDate = topicCoverageExpirationDate;
    }

    public Date getTopicCoverageEffectiveDate() {
        return topicCoverageEffectiveDate;
    }

    public void setTopicCoverageEffectiveDate(Date topicCoverageEffectiveDate) {
        this.topicCoverageEffectiveDate = topicCoverageEffectiveDate;
    }

    public String getTopicCoverageRemarks() {
        return topicCoverageRemarks;
    }

    public void setTopicCoverageRemarks(String topicCoverageRemarks) {
        this.topicCoverageRemarks = topicCoverageRemarks;
    }

    public String getTopicCoverageReason() {
        return topicCoverageReason;
    }

    public void setTopicCoverageReason(String topicCoverageReason) {
        this.topicCoverageReason = topicCoverageReason;
    }

    public Date getTopicCoverageSubmittedOn() {
        return topicCoverageSubmittedOn;
    }

    public void setTopicCoverageSubmittedOn(Date topicCoverageSubmittedOn) {
        this.topicCoverageSubmittedOn = topicCoverageSubmittedOn;
    }

    public Date getTopicCoverageCompletedOn() {
        return topicCoverageCompletedOn;
    }

    public void setTopicCoverageCompletedOn(Date topicCoverageCompletedOn) {
        this.topicCoverageCompletedOn = topicCoverageCompletedOn;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Integer trainingId) {
        this.trainingId = trainingId;
    }

    public enum State {
        PENDING,
        SUBMITTED,
        COMPLETED,
        LATE_COMPLETED,
    }

    /**
     * Define fields name for tr_topic_coverage_master entity.
     */
    public static class Fields {
        private Fields(){}

        public static final String TOPIC_COVERAGE_ID = "topicCoverageId";
        public static final String TOPIC_COVERAGE_EFFECTIVE_DATE = "topicCoverageEffectiveDate";
        public static final String TOPIC_COVERAGE_COMPLETED_ON = "topicCoverageCompletedOn";
        public static final String TRAINING_ID = "trainingId";
    }

}
