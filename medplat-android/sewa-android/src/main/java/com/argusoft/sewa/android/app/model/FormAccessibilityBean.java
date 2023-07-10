package com.argusoft.sewa.android.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by prateek on 3/5/18.
 */

@DatabaseTable
public class FormAccessibilityBean extends BaseEntity implements Serializable {

    @DatabaseField
    private String formCode;

    @DatabaseField
    private String form;

    @DatabaseField
    private Boolean isTrainingReq;

    @DatabaseField
    private String state;

    @DatabaseField
    private Boolean readyToMoveProduction;

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public Boolean getTrainingReq() {
        return isTrainingReq;
    }

    public void setTrainingReq(Boolean trainingReq) {
        isTrainingReq = trainingReq;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getReadyToMoveProduction() {
        return readyToMoveProduction;
    }

    public void setReadyToMoveProduction(Boolean readyToMoveProduction) {
        this.readyToMoveProduction = readyToMoveProduction;
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
