/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.course.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author akshar
 */
@Entity
@Table(name = "tr_topic_master")
public class TopicMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id", nullable = false)
    private Integer topicId;
    @Column(name = "topic_name", nullable = false)
    private String topicName;
    @Column(name = "topic_description")
    private String topicDescription;
    @Column(name = "day")
    private int day;
    @Column(name = "topic_order")
    private String topicOrder;
    @Enumerated(EnumType.STRING)
    @Column(name = "topic_state")
    private State topicState;

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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTopicOrder() {
        return topicOrder;
    }

    public void setTopicOrder(String topicOrder) {
        this.topicOrder = topicOrder;
    }

    public State getTopicState() {
        return topicState;
    }

    public void setTopicState(State topicState) {
        this.topicState = topicState;
    }

    public enum State {
        ACTIVE,
        INACTIVE,
        ARCHIVED
    }

    public static class Fields {

        public static final String TOPIC_ID = "topicId";
        public static final String TOPIC_NAME = "topicName";
        public static final String TOPIC_DESCRIPTION = "topicDescription";
        public static final String DAY = "day";
        public static final String TOPIC_ORDER = "topicOrder";
        public static final String TOPIC_STATE = "topicState";
    }
   
}
