package com.argusoft.sewa.android.app.databean;

import java.util.List;

public class StateResponse {
    private String code;
    private String name;
    private List<StateResponseDistrict> districts;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StateResponseDistrict> getDistricts() {
        return districts;
    }

    public void setDistricts(List<StateResponseDistrict> districts) {
        this.districts = districts;
    }
}
