/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.dto;


import java.util.Date;

/**
 *
 * @author avani
 */
public class UserFormAccessBean {
    
    private int userId;
    private String formCode;
    private String form;
    private String state;
    private boolean isTrainingReq;
    private Date createdOn;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    
     public boolean isIsTrainingReq() {
        return isTrainingReq;
    }

    public void setIsTrainingReq(boolean isTrainingReq) {
        this.isTrainingReq = isTrainingReq;
    }

    @Override
    public String toString() {
        return "UserFormAccessBean{" + "userId=" + userId + ", formCode=" + formCode + ", form=" + form + ", state=" + state + ", isTrainingReq=" + isTrainingReq + ", createdOn=" + createdOn + '}';
    }
}
