package com.argusoft.medplat.ncd.dto;

import java.util.Date;

public class NcdDiabetesDetailDataBean {

    private Long screeningDate;

    private Integer bloodSugar;

    private String doneBy;

    private String status;

    public Long getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(Long screeningDate) {
        this.screeningDate = screeningDate;
    }

    public Integer getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(Integer bloodSugar) {
        this.bloodSugar = bloodSugar;
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
