package com.argusoft.sewa.android.app.databean;

import java.util.Date;

public class NcdMentalHealthDetailDataBean {

    private Integer memberId;

    private Date screeningDate;

    private Integer talk;

    private Integer ownDailyWork;

    private Integer socialWork;

    private Integer understanding;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Date getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(Date screeningDate) {
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
}
