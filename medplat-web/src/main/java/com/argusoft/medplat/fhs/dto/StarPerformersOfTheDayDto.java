/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fhs.dto;

import java.util.List;

/**
 *
 * <p>
 *     Used for star performers of the day.
 * </p>
 * @author prateek
 * @since 26/08/20 11:00 AM
 *
 */
public class StarPerformersOfTheDayDto {
    
    Integer userid;
    String userName;
    List<String> locationNameList;
    Integer recordupdated;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getLocationNameList() {
        return locationNameList;
    }

    public void setLocationNameList(List<String> locationNameList) {
        this.locationNameList = locationNameList;
    }   

    public Integer getRecordupdated() {
        return recordupdated;
    }

    public void setRecordupdated(Integer recordupdated) {
        this.recordupdated = recordupdated;
    }
    
    
    
}
