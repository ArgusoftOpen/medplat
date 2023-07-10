package com.argusoft.sewa.android.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable
public class MenuBean extends BaseEntity implements Serializable {

    @DatabaseField
    private Integer actualId;
    @DatabaseField
    private int order;
    @DatabaseField
    private String constant;
    @DatabaseField
    private String displayName;
    @DatabaseField
    private Date modifiedOn;

    public Integer getActualId() {
        return actualId;
    }

    public void setActualId(Integer actualId) {
        this.actualId = actualId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getConstant() {
        return constant;
    }

    public void setConstant(String constant) {
        this.constant = constant;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
