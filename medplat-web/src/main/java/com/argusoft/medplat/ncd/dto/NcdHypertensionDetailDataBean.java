package com.argusoft.medplat.ncd.dto;

import java.util.Date;

public class NcdHypertensionDetailDataBean {

    private Long screeningDate;

    private Integer systolicBp;

    private Integer diastolicBp;

    private Integer pulseRate;

    private String doneBy;

    private String status;

    public Long getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(Long screeningDate) {
        this.screeningDate = screeningDate;
    }

    public Integer getSystolicBp() {
        return systolicBp;
    }

    public void setSystolicBp(Integer systolicBp) {
        this.systolicBp = systolicBp;
    }

    public Integer getDiastolicBp() {
        return diastolicBp;
    }

    public void setDiastolicBp(Integer diastolicBp) {
        this.diastolicBp = diastolicBp;
    }

    public Integer getPulseRate() {
        return pulseRate;
    }

    public void setPulseRate(Integer pulseRate) {
        this.pulseRate = pulseRate;
    }

    public String getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(String doneBy) {
        this.doneBy = doneBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
