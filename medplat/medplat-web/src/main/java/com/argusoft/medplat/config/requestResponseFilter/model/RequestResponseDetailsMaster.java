/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.config.requestResponseFilter.model;

import javax.persistence.*;
import java.util.Date;


/**
 *
 * @author ashish
 */
@Entity
@Table(name = "request_response_details_master")
public class RequestResponseDetailsMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Integer id;

    @Column(name  = "username")
    private  String username;
        
    @Column(name = "url")
    private Integer url;
    
    @Column(name = "param")
    private String param;
    
    @Column(name = "body")
    private String body;

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "remote_ip")
    private String remoteIp;   
    

    @Column(name = "page_title")    
    private Integer pageTitle;
    

    public Integer getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(Integer pageTitle) {
        this.pageTitle = pageTitle;
    }

    public Integer getUrl() {
        return url;
    }

    public void setUrl(Integer url) {
        this.url = url;
    }      

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }   

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }
}
