/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.model;

import androidx.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * @author kelvin
 */
@DatabaseTable
public class LabelBean extends BaseEntity {

    @DatabaseField
    private String labelKey;
    @DatabaseField
    private String labelValue;
    @DatabaseField
    private String language;
    @DatabaseField
    private Date modifiedOn;

    public String getLabelKey() {
        return labelKey;
    }

    public void setLabelKey(String labelKey) {
        this.labelKey = labelKey;
    }

    public String getLabelValue() {
        return labelValue;
    }

    public void setLabelValue(String labelValue) {
        this.labelValue = labelValue;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @NonNull
    @Override
    public String toString() {
        return "LabelBean{" + "labelKey=" + labelKey + ", labelValue=" + labelValue + ", language=" + language + '}';
    }

    public String getMapIndex() {
        if (labelKey != null) {
            if (language != null) {
                return (labelKey + language).toLowerCase().trim();
            } else {
                return labelKey.toLowerCase().trim();
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        return 7;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LabelBean other = (LabelBean) obj;

        if (this.labelKey != null
                && other.labelKey != null
                && this.labelKey.equalsIgnoreCase(other.labelKey)) {

            if (this.language == null || other.language == null) {
                return true;
            } else return this.language.equalsIgnoreCase(other.language);
        }
        return false;
    }
}
