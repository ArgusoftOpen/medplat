package com.argusoft.medplat.ncd.dto;

public class MemberEcgTokenDto {

    private String token;
    private int testFailurePoint;
    private String generatedDataPoints;
    private String ecgPosition;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getTestFailurePoint() {
        return testFailurePoint;
    }

    public void setTestFailurePoint(int testFailurePoint) {
        this.testFailurePoint = testFailurePoint;
    }

    public String getGeneratedDataPoints() {
        return generatedDataPoints;
    }

    public void setGeneratedDataPoints(String generatedDataPoints) {
        this.generatedDataPoints = generatedDataPoints;
    }

    public String getEcgPosition() {
        return ecgPosition;
    }

    public void setEcgPosition(String ecgPosition) {
        this.ecgPosition = ecgPosition;
    }
}
