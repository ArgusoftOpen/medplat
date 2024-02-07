package com.argusoft.medplat.ncd.dto;

import java.util.Date;

public class NcdMentalHealthDetailDataBean {

    private Long screeningDate;

    private Integer talk;

    private Integer ownDailyWork;

    private Integer socialWork;

    private Integer understanding;

    private String doneBy;

    private String status;

    public Long getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(Long screeningDate) {
        this.screeningDate = screeningDate;
    }

    public Integer getTalk() {
        return talk;
    }

    public void setTalk(Integer talk) {
        this.talk = talk;
    }

    public Integer getOwnDailyWork() {
        return ownDailyWork;
    }

    public void setOwnDailyWork(Integer ownDailyWork) {
        this.ownDailyWork = ownDailyWork;
    }

    public Integer getSocialWork() {
        return socialWork;
    }

    public void setSocialWork(Integer socialWork) {
        this.socialWork = socialWork;
    }

    public Integer getUnderstanding() {
        return understanding;
    }

    public void setUnderstanding(Integer understanding) {
        this.understanding = understanding;
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
