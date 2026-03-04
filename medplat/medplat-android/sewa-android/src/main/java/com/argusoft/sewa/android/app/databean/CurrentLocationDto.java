package com.argusoft.sewa.android.app.databean;

import java.util.Date;

public class CurrentLocationDto {
    private Date entryDate;
    private String latitude;
    private String longitude;

    public CurrentLocationDto(Date entryDate, String latitude, String longitude) {
        this.entryDate = entryDate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
