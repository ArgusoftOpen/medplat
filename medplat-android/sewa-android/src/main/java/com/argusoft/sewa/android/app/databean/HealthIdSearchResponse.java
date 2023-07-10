package com.argusoft.sewa.android.app.databean;

import java.util.List;

public class HealthIdSearchResponse {

    private List<String> authMethods;
    private String healthId;
    private String healthIdNumber;
    private String name;
    private HealthIdTags tags;

    public List<String> getAuthMethods() {
        return authMethods;
    }

    public void setAuthMethods(List<String> authMethods) {
        this.authMethods = authMethods;
    }

    public String getHealthId() {
        return healthId;
    }

    public void setHealthId(String healthId) {
        this.healthId = healthId;
    }

    public String getHealthIdNumber() {
        return healthIdNumber;
    }

    public void setHealthIdNumber(String healthIdNumber) {
        this.healthIdNumber = healthIdNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HealthIdTags getTags() {
        return tags;
    }

    public void setTags(HealthIdTags tags) {
        this.tags = tags;
    }
}
