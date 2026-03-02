/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 * Define location_type_master entity and its fields.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "location_type_master")
public class LocationTypeMaster extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "level", nullable = false)
    private int level;
    @Column(name = "is_soh_enable", nullable = false)
    private boolean isSohEnable;
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isSohEnable() {
        return isSohEnable;
    }

    public void setSohEnable(boolean sohEnable) {
        isSohEnable = sohEnable;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    /**
     * Define fields for location_type.
     */
    public static class Fields {
        private Fields() {
        }

        public static final String TYPE = "type";
        public static final String NAME = "name";
        public static final String LEVEL = "level";
    }

}
