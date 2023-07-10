package com.argusoft.medplat.rch.model;

import javax.persistence.*;

/**
 * <p>
 * Define health_infrastructure_lab_test_mapping entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "health_infrastructure_lab_test_mapping")
public class RchHealthInfraLabTestMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "health_infra_id")
    private Integer healthInfraId;

    @Column(name = "health_infra_type")
    private Integer healthInfraType;

    @Column(name = "ref_id")
    private Integer refId;

    @Column(name = "permission_type")
    private String permissionType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHealthInfraId() {
        return healthInfraId;
    }

    public void setHealthInfraId(Integer healthInfraId) {
        this.healthInfraId = healthInfraId;
    }

    public Integer getHealthInfraType() {
        return healthInfraType;
    }

    public void setHealthInfraType(Integer healthInfraType) {
        this.healthInfraType = healthInfraType;
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }
}
