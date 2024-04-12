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
@Table(name = "adolescent_entity")
public class AdolescentEntity extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(name = "height_of_member")
    private Integer heightOfMember;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "has_addictions")
    private Boolean hasAddictions;

    @Column(name = "addictions")
    private String addictions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHeightOfMember() {
        return heightOfMember;
    }

    public void setHeightOfMember(Integer heightOfMember) {
        this.heightOfMember = heightOfMember;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Boolean getHasAddictions() {
        return hasAddictions;
    }

    public void setHasAddictions(Boolean hasAddictions) {
        this.hasAddictions = hasAddictions;
    }

    public String getAddictions() {
        return addictions;
    }

    public void setAddictions(String addictions) {
        this.addictions = addictions;
    }
}
