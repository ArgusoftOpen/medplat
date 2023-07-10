/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.model;

import androidx.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author kelvin
 */
@DatabaseTable
public class AnswerBean extends BaseEntity {

    @DatabaseField
    private int queId;
    @DatabaseField
    private String key;
    @DatabaseField
    private String value;
    @DatabaseField
    private int next;
    @DatabaseField
    private String subform;
    @DatabaseField
    private String relatedpropertyname;
    @DatabaseField
    private String entity;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getQueId() {
        return queId;
    }

    public void setQueId(int queId) {
        this.queId = queId;
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

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public String getSubform() {
        return subform;
    }

    public void setSubform(String subform) {
        this.subform = subform;
    }

    public String getRelatedpropertyname() {
        return relatedpropertyname;
    }

    public void setRelatedpropertyname(String relatedpropertyname) {
        this.relatedpropertyname = relatedpropertyname;
    }

    @NonNull
    @Override
    public String toString() {
        return "AnswerBean{" + "queId=" + queId + ", key=" + key + ", value=" + value + ", next=" + next + ", subform=" + subform + ", relatedpropertyname=" + relatedpropertyname + '}';
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
