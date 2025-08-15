package com.argusoft.medplat.web.BackendController.entity;
import com.argusoft.medplat.common.model.EntityAuditInfo;
import javax.persistence.*;

@Entity
@Table(name = "derived_attributes")
public class DerivedAttributes extends EntityAuditInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "derived_name", nullable = false)
    private String derivedName;

    @Column(name = "formula", nullable = false)
    private String formula;

    @Column(name = "result")
    private Double result;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDerivedName() {
        return derivedName;
    }

    public void setDerivedName(String derivedName) {
        this.derivedName = derivedName;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }
}