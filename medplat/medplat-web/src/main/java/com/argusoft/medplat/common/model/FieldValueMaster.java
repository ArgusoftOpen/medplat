package com.argusoft.medplat.common.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 *<p>Defines fields related to user</p>
 * @author shrey
 * @since 26/08/2020 5:30
 */
@Entity
@Table(name = "field_value_master")
public class FieldValueMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "field_id", nullable = false)
    private Integer fieldId;

    @Column(name = "field_value", nullable = false)
    private String fieldValue;

    public FieldValueMaster() {
        // public constructor
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    /**
     * An util class defines string constant
     */
    public static class FieldValueMasterFields {

        private FieldValueMasterFields() {
            
        }

        public static final String ID = "id";
        public static final String FIELD_ID = "fieldId";
        public static final String FIELD_VALUE = "fieldValue";
    }

}
