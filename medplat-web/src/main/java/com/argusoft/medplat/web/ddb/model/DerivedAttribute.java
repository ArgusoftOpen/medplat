package com.argusoft.medplat.web.ddb.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Defines fields for derived attributes
 * @author argusoft
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

    // equals and hashCode...
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DerivedAttribute)) {
            return false;
        }
        DerivedAttribute other = (DerivedAttribute) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    public static class DerivedAttributeFields {
        private DerivedAttributeFields() {}
        public static final String ID = "id";
        public static final String DERIVED_NAME = "derivedName";
        public static final String FORMULA = "formula";
        public static final String RESULT = "result";
    }
}
