/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Define facility_performance_master entity and its fields.
 * </p>
 *
 * @author vivek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "facility_performance_master")
public class FacilityPerformanceMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(name = "performance_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date performanceDate;

    @Column(name = "no_of_opd_attended")
    private Integer noOfOpdAttended;

    @Column(name = "no_of_ipd_attended")
    private Integer noOfIpdAttended;

    @Column(name = "no_of_deliveres_conducted")
    private Integer noOfDeliveresConducted;

    @Column(name = "no_of_csection_conducted")
    private Integer noOfSectionConducted;

    @Column(name = "no_of_major_operation_conducted")
    private Integer noOfMajorOperationConducted;

    @Column(name = "no_of_minor_operation_conducted")
    private Integer noOfMinorOperationConducted;

    @Column(name = "no_of_laboratory_test_conducted")
    private Integer noOfLaboratoryTestConducted;

    @Column(name = "health_infrastrucutre_id")
    private Integer healthInfrastructureId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getPerformanceDate() {
        return performanceDate;
    }

    public void setPerformanceDate(Date performanceDate) {
        this.performanceDate = performanceDate;
    }

    public Integer getNoOfOpdAttended() {
        return noOfOpdAttended;
    }

    public void setNoOfOpdAttended(Integer noOfOpdAttended) {
        this.noOfOpdAttended = noOfOpdAttended;
    }

    public Integer getNoOfIpdAttended() {
        return noOfIpdAttended;
    }

    public void setNoOfIpdAttended(Integer noOfIpdAttended) {
        this.noOfIpdAttended = noOfIpdAttended;
    }

    public Integer getNoOfDeliveresConducted() {
        return noOfDeliveresConducted;
    }

    public void setNoOfDeliveresConducted(Integer noOfDeliveresConducted) {
        this.noOfDeliveresConducted = noOfDeliveresConducted;
    }

    public Integer getNoOfSectionConducted() {
        return noOfSectionConducted;
    }

    public void setNoOfSectionConducted(Integer noOfSectionConducted) {
        this.noOfSectionConducted = noOfSectionConducted;
    }

    public Integer getNoOfMajorOperationConducted() {
        return noOfMajorOperationConducted;
    }

    public void setNoOfMajorOperationConducted(Integer noOfMajorOperationConducted) {
        this.noOfMajorOperationConducted = noOfMajorOperationConducted;
    }

    public Integer getNoOfMinorOperationConducted() {
        return noOfMinorOperationConducted;
    }

    public void setNoOfMinorOperationConducted(Integer noOfMinorOperationConducted) {
        this.noOfMinorOperationConducted = noOfMinorOperationConducted;
    }

    public Integer getNoOfLaboratoryTestConducted() {
        return noOfLaboratoryTestConducted;
    }

    public void setNoOfLaboratoryTestConducted(Integer noOfLaboratoryTestConducted) {
        this.noOfLaboratoryTestConducted = noOfLaboratoryTestConducted;
    }

    public Integer getHealthInfrastructureId() {
        return healthInfrastructureId;
    }

    public void setHealthInfrastructureId(Integer healthInfrastructureId) {
        this.healthInfrastructureId = healthInfrastructureId;
    }

    /**
     * Define fields name for facility_performance_master entity.
     */
    public static class Fields {
        private Fields() {
        }

        public static final String ID = "id";
        public static final String PERFORMANCE_DATE = "performanceDate";
        public static final String HEALTH_INFRASTRUCTURE_ID = "healthInfrastructureId";
    }
}
