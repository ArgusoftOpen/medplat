package com.argusoft.sewa.android.app.databean;

import java.util.List;

public class ChardhamEmergencyDataBean {
    private ChardhamEmergencyRequestDto request;
    private ChardhamEmergencyResponseDto response;
    private List<ChardhamSupportUserBean> requestFromContactDetails;
    private List<ChardhamSupportUserBean> responseFromContactDetails;

    public ChardhamEmergencyRequestDto getRequest() {
        return request;
    }

    public void setRequest(ChardhamEmergencyRequestDto request) {
        this.request = request;
    }

    public ChardhamEmergencyResponseDto getResponse() {
        return response;
    }

    public void setResponse(ChardhamEmergencyResponseDto response) {
        this.response = response;
    }

    public List<ChardhamSupportUserBean> getRequestFromContactDetails() {
        return requestFromContactDetails;
    }

    public void setRequestFromContactDetails(List<ChardhamSupportUserBean> requestFromContactDetails) {
        this.requestFromContactDetails = requestFromContactDetails;
    }

    public List<ChardhamSupportUserBean> getResponseFromContactDetails() {
        return responseFromContactDetails;
    }

    public void setResponseFromContactDetails(List<ChardhamSupportUserBean> responseFromContactDetails) {
        this.responseFromContactDetails = responseFromContactDetails;
    }
}
