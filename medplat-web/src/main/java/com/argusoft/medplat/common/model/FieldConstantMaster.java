package com.argusoft.medplat.common.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 *<p>Defines fields related to user</p>
 * @author shrey
 * @since 26/08/2020 5:30
 */
@Entity
@Table(name = "field_constant_master")
public class FieldConstantMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    
    @Column(name = "field_name", nullable = false, unique = true)
    private String fieldName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    
}
