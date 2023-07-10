/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.databean;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * @author kelvin
 */
public class OptionTagBean {

    private String key;
    private String value;
    private String next;
    private String subform;
    private String relatedpropertyname;

    public String getRelatedpropertyname() {
        return relatedpropertyname;
    }

    public void setRelatedpropertyname(String relatedpropertyname) {
        this.relatedpropertyname = relatedpropertyname;
    }

    public String getSubform() {
        return subform;
    }

    public void setSubform(String subform) {
        this.subform = subform;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.key != null ? this.key.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OptionTagBean other = (OptionTagBean) obj;
        return Objects.equals(this.key, other.key);
    }

    @NonNull
    @Override
    public String toString() {
        return "OptionTagBean{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", next='" + next + '\'' +
                ", subform='" + subform + '\'' +
                ", relatedpropertyname='" + relatedpropertyname + '\'' +
                '}';
    }
}
