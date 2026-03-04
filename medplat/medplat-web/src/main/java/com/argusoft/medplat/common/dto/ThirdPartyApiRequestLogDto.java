/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.dto;

import java.util.Date;

/**
 *
 * @author ashish
 */
public class ThirdPartyApiRequestLogDto {

    private String  responseString;
    private String reqParam;
    private String reqBody;
    private String reqRemoteIP;
    private String thirtPartyRequestedType;
    private String errorMsg;
    private String reqStatus;

    public Date getReqTime() {
        return reqTime;
    }

    public void setReqTime(Date reqTime) {
        this.reqTime = reqTime;
    }
    private Date reqTime;

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public String getReqParam() {
        return reqParam;
    }

    public void setReqParam(String reqParam) {
        this.reqParam = reqParam;
    }

    public String getReqBody() {
        return reqBody;
    }

    public void setReqBody(String reqBody) {
        this.reqBody = reqBody;
    }

    public String getReqRemoteIP() {
        return reqRemoteIP;
    }

    public void setReqRemoteIP(String reqRemoteIP) {
        this.reqRemoteIP = reqRemoteIP;
    }

    public String getThirtPartyRequestedType() {
        return thirtPartyRequestedType;
    }

    public void setThirtPartyRequestedType(String thirtPartyRequestedType) {
        this.thirtPartyRequestedType = thirtPartyRequestedType;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getReqStatus() {
        return reqStatus;
    }

    public void setReqStatus(String reqStatus) {
        this.reqStatus = reqStatus;
    }

}
