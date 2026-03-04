/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.dto;

import com.argusoft.medplat.training.model.Attendance;
import java.util.Date;
import java.util.Set;

/**
 *
 * <p>
 *     Used for attendance.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public class AttendanceDto {

    private Integer id;
    private String name;
    private String desc;
    private Attendance.State state;
    private Attendance.Type type;
    private Date expirationDate;
    private Date effectiveDate;
    private String remarks;
    private String reason;
    private Date completedOn;
    private Set<Integer> topicIds;
    private Integer trainingId;
    private Integer userId;
    private boolean isPresent;

    public AttendanceDto(){
    
    }
    
    public AttendanceDto(Integer id, String name, String desc,Attendance.Type type, Attendance.State state, Date expirationDate, Date effectiveDate, String remarks, String reason, Date completedOn, Set<Integer> topicIds, Integer trainingId, Integer userId, boolean isPresent) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.type = type;
        this.state = state;
        this.expirationDate = expirationDate;
        this.effectiveDate = effectiveDate;
        this.remarks = remarks;
        this.reason = reason;
        this.completedOn = completedOn;
        this.topicIds = topicIds;
        this.trainingId = trainingId;
        this.userId = userId;
        this.isPresent = isPresent;
    }

    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Attendance.State getState() {
        return state;
    }

    public void setState(Attendance.State state) {
        this.state = state;
    }

    public boolean isIsPresent() {
        return isPresent;
    }

    public void setIsPresent(boolean isPresent) {
        this.isPresent = isPresent;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(Date completedOn) {
        this.completedOn = completedOn;
    }

    public Set<Integer> getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(Set<Integer> topicIds) {
        this.topicIds = topicIds;
    }

    public Integer getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Integer trainingId) {
        this.trainingId = trainingId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean getPresent() {
        return isPresent;
    }

    public void setPresent(boolean isPresent) {
        this.isPresent = isPresent;
    }

    public Attendance.Type getType() {
        return type;
    }

    public void setType(Attendance.Type type) {
        this.type = type;
    }
    
}
