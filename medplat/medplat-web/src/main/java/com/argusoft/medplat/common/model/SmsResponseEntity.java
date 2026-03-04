/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *<p>Defines fields related to user</p>
 * @author ashish
 * @since 26/08/2020 5:30
 */
@Entity
@Table(name = "sms_response")
public class SmsResponseEntity implements Serializable {

    @Id
    @Column(name = "a2wackid", nullable = false)
    private String a2wackid;

    @Column(name = "a2wstatus")
    private String a2wstatus;

    @Column(name = "carrierstatus")
    private String carrierstatus;

    @Column(name = "lastutime")
//    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private String lastutime;

    @Column(name = "custref")
    private String custref;

    @Column(name = "submitdt")
//    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private String submitdt;

    @Column(name = "mnumber")
    private String mnumber;
    @Column(name = "acode")
    private String acode;

    @Column(name = "senderid")
    private String senderid;

    @Column(name = "created_on")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdOn;

    public SmsResponseEntity() {
    }

    public SmsResponseEntity(String a2wackid, String a2wstatus, String carrierstatus, String lastutime, String custref, String submitdt, String mnumber, String acode, String senderid, Date createdOn) {
        this.a2wackid = a2wackid;
        this.a2wstatus = a2wstatus;
        this.carrierstatus = carrierstatus;
        this.lastutime = lastutime;
        this.custref = custref;
        this.submitdt = submitdt;
        this.mnumber = mnumber;
        this.acode = acode;
        this.senderid = senderid;
        this.createdOn = createdOn;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getA2wackid() {
        return a2wackid;
    }

    public void setA2wackid(String a2wackid) {
        this.a2wackid = a2wackid;
    }

    public String getA2wstatus() {
        return a2wstatus;
    }

    public void setA2wstatus(String a2wstatus) {
        this.a2wstatus = a2wstatus;
    }

    public String getCarrierstatus() {
        return carrierstatus;
    }

    public void setCarrierstatus(String carrierstatus) {
        this.carrierstatus = carrierstatus;
    }

    public String getCustref() {
        return custref;
    }

    public void setCustref(String custref) {
        this.custref = custref;
    }

    public String getMnumber() {
        return mnumber;
    }

    public void setMnumber(String mnumber) {
        this.mnumber = mnumber;
    }

    public String getAcode() {
        return acode;
    }

    public void setAcode(String acode) {
        this.acode = acode;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getLastutime() {
        return lastutime;
    }

    public void setLastutime(String lastutime) {
        this.lastutime = lastutime;
    }

    public String getSubmitdt() {
        return submitdt;
    }

    public void setSubmitdt(String submitdt) {
        this.submitdt = submitdt;
    }

}
