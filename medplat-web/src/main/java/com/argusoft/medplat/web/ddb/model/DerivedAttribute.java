package com.argusoft.medplat.web.ddb.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Defines fields for derived attributes
 * @author ashwin
 * @since 23/08/2025 15:30
 */
@Entity
@Table(name = "derived_attributes")
@Getter
@Setter
public class DerivedAttribute extends EntityAuditInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "derived_name", columnDefinition = "TEXT")
    private String derivedName;

    @Column(name = "formula", columnDefinition = "TEXT")
    private String formula;

    @Column(name = "result", columnDefinition = "DOUBLE PRECISION")
    private Double result;

    /**
     * An util class for string constants of derived attributes
     */
    public static class DerivedAttributeFields {
        private DerivedAttributeFields() {}

        public static final String ID = "id";
        public static final String DERIVED_NAME = "derivedName";
        public static final String FORMULA = "formula";
        public static final String RESULT = "result";
    }
}
