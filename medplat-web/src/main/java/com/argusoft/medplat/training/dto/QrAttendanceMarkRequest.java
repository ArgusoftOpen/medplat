package com.argusoft.medplat.training.dto;

/**
 * Prototype request DTO used when a learner scans a QR code to mark attendance.
 */
public class QrAttendanceMarkRequest {

    private String token;
    private Double latitude;
    private Double longitude;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

