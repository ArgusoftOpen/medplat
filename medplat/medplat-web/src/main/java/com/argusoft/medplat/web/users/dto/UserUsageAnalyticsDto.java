/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.users.dto;

/**
 * <p>Defines fields related to user usage analytics</p>
 * @author ashish
 * @since 26/08/2020 5:30
 */
public class UserUsageAnalyticsDto {
    private String prevStateId;
    
    private String currStateId;
    
    private String nextStateId;
    
    private Integer userId;
    
    private Long activeTabTime;
    
    private Long totalTime;
    
    private String pageTitle;
    
    private boolean browserCloseDet;

    public boolean isBrowserCloseDet() {
        return browserCloseDet;
    }

    public void setBrowserCloseDet(boolean browserCloseDet) {
        this.browserCloseDet = browserCloseDet;
    }
    

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPrevStateId() {
        return prevStateId;
    }

    public void setPrevStateId(String prevStateId) {
        this.prevStateId = prevStateId;
    }

    public String getCurrStateId() {
        return currStateId;
    }

    public void setCurrStateId(String currStateId) {
        this.currStateId = currStateId;
    }

    public String getNextStateId() {
        return nextStateId;
    }

    public void setNextStateId(String nextStateId) {
        this.nextStateId = nextStateId;
    }    

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getActiveTabTime() {
        return activeTabTime;
    }

    public void setActiveTabTime(Long activeTabTime) {
        this.activeTabTime = activeTabTime;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    @Override
    public String toString() {
        return "UserUsageAnalyticsDto{" +
                "prevStateId='" + prevStateId + '\'' +
                ", currStateId='" + currStateId + '\'' +
                ", nextStateId='" + nextStateId + '\'' +
                ", userId=" + userId +
                ", activeTabTime=" + activeTabTime +
                ", totalTime=" + totalTime +
                ", pageTitle='" + pageTitle + '\'' +
                ", browserCloseDet=" + browserCloseDet +
                '}';
    }
}
