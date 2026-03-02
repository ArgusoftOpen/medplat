package com.argusoft.medplat.common.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 Defines fields of ThirdPartyRequestLogModel
 * </p>
 *
 * @author ashish
 * @since 02/09/2020 04:44
 *
 */

@Entity
@Table(name = "tp_api_access_log")
public class ThirdPartyRequestLogModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "req_body")
    private String reqBody;

    @Column(name = "req_param")
    private String reqParam;

    @Column(name = "res_body")
    private String resBody;

    @Column(name = "req_time")
    private Date reqTime;

    @Column(name = "req_state")
    private String reqState;

    @Column(name = "req_error")
    private String reqError;

    @Column(name = "tp_type")
    private String thirdPartyType;

    @Column(name = "req_remote_ip")
    private String reqRemoteIp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReqBody() {
        return reqBody;
    }

    public void setReqBody(String reqBody) {
        this.reqBody = reqBody;
    }

    public String getReqParam() {
        return reqParam;
    }

    public void setReqParam(String reqParam) {
        this.reqParam = reqParam;
    }

    public String getResBody() {
        return resBody;
    }

    public void setResBody(String resBody) {
        this.resBody = resBody;
    }

    public Date getReqTime() {
        return reqTime;
    }

    public void setReqTime(Date reqTime) {
        this.reqTime = reqTime;
    }

    public String getReqState() {
        return reqState;
    }

    public void setReqState(String reqState) {
        this.reqState = reqState;
    }

    public String getReqError() {
        return reqError;
    }

    public void setReqError(String reqError) {
        this.reqError = reqError;
    }

    public String getThirdPartyType() {
        return thirdPartyType;
    }

    public void setThirdPartyType(String thirdPartyType) {
        this.thirdPartyType = thirdPartyType;
    }

    public String getReqRemoteIp() {
        return reqRemoteIp;
    }

    public void setReqRemoteIp(String reqRemoteIp) {
        this.reqRemoteIp = reqRemoteIp;
    }



}
