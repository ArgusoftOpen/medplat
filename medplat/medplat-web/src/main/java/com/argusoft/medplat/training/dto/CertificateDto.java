/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.dto;

import com.argusoft.medplat.training.model.Certificate;
import java.util.Date;

/**
 *
 * <p>
 *     Used for certificate.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public class CertificateDto {

    private Integer certificateId;
    private String certificateName;
    private String certificateDescription;
    private Certificate.State certificateState;
    private String certificateRemarks;
    private Integer trainingId;
    private Integer courseId;
    private Integer userId;
    private Date certificationDate;
    private Certificate.GradType gradeType;
    private Certificate.Type certificateType;

    public CertificateDto(){
    }
    
    public CertificateDto(Integer certificateId, String certificateName, String certificateDescription, Certificate.State certificateState, String certificateRemarks, Integer trainingId, Integer courseId, Integer userId, Date certificationDate, Certificate.GradType gradeType , Certificate.Type certificateType) {
        this.certificateId = certificateId;
        this.certificateName = certificateName;
        this.certificateDescription = certificateDescription;
        this.certificateState = certificateState;
        this.certificateRemarks = certificateRemarks;
        this.trainingId = trainingId;
        this.courseId = courseId;
        this.userId = userId;
        this.certificationDate = certificationDate;
        this.gradeType = gradeType;
        this.certificateType = certificateType;
    }

    public Certificate.Type getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(Certificate.Type certificateType) {
        this.certificateType = certificateType;
    }

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

    public Certificate.State getCertificateState() {
        return certificateState;
    }

    public void setCertificateState(Certificate.State certificateState) {
        this.certificateState = certificateState;
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

    public Certificate.GradType getGradeType() {
        return gradeType;
    }

    public void setGradeType(Certificate.GradType gradeType) {
        this.gradeType = gradeType;
    }
    
    
}
