/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.course.dto;
import com.argusoft.medplat.course.model.TopicMaster;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author akshar
 */
public class TopicMasterDto {

    private Integer topicId;
    private String topicName;
    private String topicDescription;
    private int topicDay;
    private String topicOrder;
    private TopicMaster.State topicState;
    private List<TopicMediaMasterDto> topicMediaList = new ArrayList<>();

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }

    public int getTopicDay() {
        return topicDay;
    }

    public void setTopicDay(int topicDay) {
        this.topicDay = topicDay;
    } 

    public String getTopicOrder() {
        return topicOrder;
    }

    public void setTopicOrder(String topicOrder) {
        this.topicOrder = topicOrder;
    }

    public TopicMaster.State getTopicState() {
        return topicState;
    }

    public void setTopicState(TopicMaster.State topicState) {
        this.topicState = topicState;
    }

    public List<TopicMediaMasterDto> getTopicMediaList() {
        return topicMediaList;
    }

    public void setTopicMediaList(List<TopicMediaMasterDto> topicMediaList) {
        this.topicMediaList = topicMediaList;
    }
}
