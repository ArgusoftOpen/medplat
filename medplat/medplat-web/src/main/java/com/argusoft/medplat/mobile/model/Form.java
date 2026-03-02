package com.argusoft.medplat.mobile.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author avani
 */
@Entity
@Table(name = "listvalue_form_master")
public class Form implements Serializable {

    private static final int serialVersionUID = 1;

    @Id
    @Basic(optional = false)
    @Column(name = "form_key", nullable = false, length = 10)
    private String formKey;

    @Basic(optional = false)
    @Column(name = "form", nullable = false, length = 50)
    private String form;

    @Basic(optional = false)
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "is_training_req")
    private Boolean isTrainingRequire;

    @Column(name = "query_for_training_completed", length = 255)
    private String query;

    public Form() {
    }

    public Form(String formKey) {
        this.formKey = formKey;
    }

    public Form(String formKey, String form, boolean isActive) {
        this.formKey = formKey;
        this.form = form;
        this.isActive = isActive;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Boolean isIsTrainingRequire() {
        return isTrainingRequire;
    }

    public void setIsTrainingRequire(Boolean isTrainingRequire) {
        this.isTrainingRequire = isTrainingRequire;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (formKey != null ? formKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Form)) {
            return false;
        }
        Form other = (Form) object;
        return (this.formKey != null || other.formKey == null) && (this.formKey == null || this.formKey.equals(other.formKey));
    }

    @Override
    public String toString() {
        return "com.argusoft.sewa.model.Form[formKey=" + formKey + "]";
    }

    public static class Fields {

        private Fields() {
            throw new IllegalStateException("Utility Class");
        }

        public static final String FORM_KEY = "formKey";
        public static final String FORM = "form";
        public static final String IS_ACTIVE = "isActive";
        public static final String IS_TRAINING_REQUIRE = "isTrainingRequire";
        public static final String QUERY = "query";
    }

}
