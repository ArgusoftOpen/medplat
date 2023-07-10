/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.databean;

import androidx.annotation.NonNull;

/**
 * @author alpeshkyada
 */
public class OptionDataBean {

    private String key;
    private String value;
    private String next;
    private String relatedProperty;

    public OptionDataBean() {
    }

    public OptionDataBean(String key, String value, String next) {
        this.key = key;
        this.value = value;
        this.next = next;
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

    public String getRelatedProperty() {
        return relatedProperty;
    }

    public void setRelatedProperty(String relatedProperty) {
        this.relatedProperty = relatedProperty;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (this.key != null ? this.key.hashCode() : 0);
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
        final OptionDataBean other = (OptionDataBean) obj;
        return (this.key == null) ? (other.key == null) : this.key.toLowerCase().contains(other.key.toLowerCase());
    }

    @NonNull
    @Override
    public String toString() {
        return "OptionDataBean{" + "key=" + key + ", value=" + value + ", next=" + next + ", relatedProperty=" + relatedProperty + "}\n";
    }
}
