package com.argusoft.sewa.android.app.databean;

import java.util.Date;

public class NcdDiabetesDetailDataBean {

    private Integer memberId;

    private Date screeningDate;

    private Integer bloodSugar;

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

    public Integer getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(Integer bloodSugar) {
        this.bloodSugar = bloodSugar;
    }
}
