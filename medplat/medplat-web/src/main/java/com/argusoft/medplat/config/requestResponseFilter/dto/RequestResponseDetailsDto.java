/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.config.requestResponseFilter.dto;

import java.util.Date;

/**
 *
 * @author ashish
 */
public class RequestResponseDetailsDto {

    private Integer id;
    private String username;
    private String uuid;
    private String url;
    private String param;
    private String body;
    private Date startTime;
    private Date endTime;
    private String remoteIp;
    private String pageTitle;
    private Integer pageTitleId;
    private Integer urlId;

    public RequestResponseDetailsDto(String currentUserName, String uuid,
            String param, String body, Date startTime, Date endTime,
            String remoteIp, Integer pageTitleId, Integer urlId) {
        this.username = currentUserName;
        this.uuid = uuid;
        this.param = param;
        this.body = body;
        this.startTime = startTime;
        this.endTime = endTime;
        this.remoteIp = remoteIp;
        this.pageTitleId = pageTitleId;
        this.urlId = urlId;
    }

    public Integer getPageTitleId() {
        return pageTitleId;
    }

    public void setPageTitleId(Integer pageTitleId) {
        this.pageTitleId = pageTitleId;
    }

    public Integer getUrlId() {
        return urlId;
    }

    public void setUrlId(Integer urlId) {
        this.urlId = urlId;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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
