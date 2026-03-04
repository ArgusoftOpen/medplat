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
 * Define health_infrastructure_monthly_volunteers_details entity and its fields.
 * </p>
 *
 * @author smeet
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "health_infrastructure_monthly_volunteers_details")
public class VolunteersMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(name = "health_infrastructure_id", nullable = false)
    private Integer healthInfrastructureId;

    @Column(name = "no_of_volunteers", nullable = false)
    private Integer noOfVolunteers;

    @Column(name = "month_year", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date monthYear;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHealthInfrastructureId() {
        return healthInfrastructureId;
    }

    public void setHealthInfrastructureId(Integer healthInfrastructureId) {
        this.healthInfrastructureId = healthInfrastructureId;
    }

    public Integer getNoOfVolunteers() {
        return noOfVolunteers;
    }

    public void setNoOfVolunteers(Integer noOfVolunteers) {
        this.noOfVolunteers = noOfVolunteers;
    }

    public Date getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(Date monthYear) {
        this.monthYear = monthYear;
    }

    /**
     * Define fields name for health_infrastructure_monthly_volunteers_details entity.
     */
    public static class Fields {
        private Fields() {
        }

        public static final String ID = "id";
        public static final String HEALTH_INFRASTRUCTURE_ID = "healthInfrastructureId";
        public static final String MONTH_YEAR = "monthYear";
    }

}
