/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fhs.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 * Define imt_family_state_detail entity and its fields.
 * </p>
 *
 * @author harsh
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "imt_family_state_detail")
public class FamilyStateDetailEntity extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "parent")
    private Integer parent;

    @Column(name = "to_state")
    private String toState;

    @Column(name = "from_state")
    private String fromState;

    @Column(name = "family_id")
    private String familyId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getToState() {
        return toState;
    }

    public void setToState(String toState) {
        this.toState = toState;
    }

    public String getFromState() {
        return fromState;
    }

    public void setFromState(String fromState) {
        this.fromState = fromState;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    @Override
    public String toString() {
        return "FamilyStateDetailEntity{" + "id=" + id + ", comment=" + comment + ", parent=" + parent + ", toState=" + toState + ", fromState=" + fromState + ", familyId=" + familyId + '}';
    }

    public static class Fields {

        private Fields() {
            throw new IllegalStateException("Utility Class");
        }

        public static final String ID = "id";
        public static final String FAMILY_ID = "familyId";
    }

}
