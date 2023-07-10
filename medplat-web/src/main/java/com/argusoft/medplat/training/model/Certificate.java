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
 *     Define tr_certificate_master entity and its fields.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "tr_certificate_master")
public class Certificate extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id")
    private Integer certificateId;

    @Column(name = "certificate_name")
    private String certificateName;

    @Column(name = "certificate_descr")
    private String certificateDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "certificate_state")
    private State state;

    @Enumerated(EnumType.STRING)
    @Column(name = "certificate_type")
    private Type type;

    @Column(name = "remarks")
    private String certificateRemarks;

    @Column(name = "training_id")
    private Integer trainingId;

    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "certification_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date certificationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade_type")
    private GradType gradeType;

    public Integer getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(Integer certificateId) {
        this.certificateId = certificateId;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getCertificateDescription() {
        return certificateDescription;
    }

    public void setCertificateDescription(String certificateDescription) {
        this.certificateDescription = certificateDescription;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getCertificateRemarks() {
        return certificateRemarks;
    }

    public void setCertificateRemarks(String certificateRemarks) {
        this.certificateRemarks = certificateRemarks;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCertificationDate() {
        return certificationDate;
    }

    public void setCertificationDate(Date certificationDate) {
        this.certificationDate = certificationDate;
    }

    public GradType getGradeType() {
        return gradeType;
    }

    public void setGradeType(GradType gradeType) {
        this.gradeType = gradeType;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum State {
        ACTIVE,
        INACTIVE
    }

    public enum Type {
        COURSECOMPLETION,
        COURSECOMPLETION_FAILED
    }

    public enum GradType {
        FAILED,
        TRAINED
    }

    /**
     * Define fields name for tr_certificate_master entity.
     */
    public static class Fields {
        private Fields(){}

        public static final String CERTIFICATE_ID = "certificateId";
        public static final String CERTIFICATE_NAME = "certificateName";
        public static final String CERTIFICATE_DESCRIPTION = "certificateDescription";
        public static final String STATE = "state";
        public static final String TYPE = "type";
        public static final String CERTIFICATE_REMARKS = "certificateRemarks";
        public static final String TRAINING_ID = "trainingId";
        public static final String COURSE_ID = "courseId";
        public static final String USER_ID = "userId";
        public static final String CERTIFICATION_DATE = "certificationDate";
        public static final String GRADE_TYPE = "gradeType";
    }

}
