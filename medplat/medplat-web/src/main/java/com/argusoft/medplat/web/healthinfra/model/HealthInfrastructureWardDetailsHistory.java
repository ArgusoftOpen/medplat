package com.argusoft.medplat.web.healthinfra.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * <p>
 *     Define health_infrastructure_ward_details_history entity and its fields.
 * </p>
 * @author dhaval
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "health_infrastructure_ward_details_history")
public class HealthInfrastructureWardDetailsHistory extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "health_infrastructure_ward_details_id")
    private Integer healthInfrastructureWardDetailsId;

    @Column(name = "action")
    private String action;

    @Column(name = "ward_name")
    private String wardName;

    @Column(name = "ward_type")
    private Integer wardType;

    @Column(name = "number_of_beds")
    private Integer numberOfBeds;

    @Column(name = "number_of_ventilators_type1")
    private Integer numberOfVentilatorsType1;

    @Column(name = "number_of_vantilators_type2")
    private Integer numberOfVentilatorsType2;

    @Column(name = "number_of_o2")
    private Integer numberOfO2;

    @Column(name = "status")
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHealthInfrastructureWardDetailsId() {
        return healthInfrastructureWardDetailsId;
    }

    public void setHealthInfrastructureWardDetailsId(Integer healthInfrastructureWardDetailsId) {
        this.healthInfrastructureWardDetailsId = healthInfrastructureWardDetailsId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public Integer getWardType() {
        return wardType;
    }

    public void setWardType(Integer wardType) {
        this.wardType = wardType;
    }

    public Integer getNumberOfBeds() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(Integer numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public Integer getNumberOfVentilatorsType1() {
        return numberOfVentilatorsType1;
    }

    public void setNumberOfVentilatorsType1(Integer numberOfVentilatorsType1) {
        this.numberOfVentilatorsType1 = numberOfVentilatorsType1;
    }

    public Integer getNumberOfVentilatorsType2() {
        return numberOfVentilatorsType2;
    }

    public void setNumberOfVentilatorsType2(Integer numberOfVentilatorsType2) {
        this.numberOfVentilatorsType2 = numberOfVentilatorsType2;
    }

    public Integer getNumberOfO2() {
        return numberOfO2;
    }

    public void setNumberOfO2(Integer numberOfO2) {
        this.numberOfO2 = numberOfO2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
