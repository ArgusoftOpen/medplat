/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.common.util.IJoinEnum;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.criteria.JoinType;

/**
 *
 * <p>
 *     Define tr_attendance_master entity and its fields.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "tr_attendance_master")
public class Attendance extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "descr")
    private String desc;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Column(name = "expiration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;

    @Column(name = "effective_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDate;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "reason")
    private String reason;

    @Column(name = "completed_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedOn;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tr_attendance_topic_rel", joinColumns = @JoinColumn(name = "attendance_id"))
    @Column(name = "topic_id")
    private Set<Integer> topicIds;

    @Column(name = "training_id")
    private Integer trainingId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "is_present")
    private boolean isPresent;

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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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

    public void setIsPresent(boolean isPresent) {
        this.isPresent = isPresent;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum State {
        ACTIVE,
        INACTIVE
    }

    public enum Type {
        TRAINER,
        TRAINEE
    }

    /**
     * Define fields name for tr_attendance_master entity.
     */
    public static class Fields {
        private Fields(){}

        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String DESC = "desc";
        public static final String STATE = "state";
        public static final String TYPE = "type";
        public static final String EXPIRATION_DATE = "expirationDate";
        public static final String EFFECTIVE_DATE = "effectiveDate";
        public static final String REMARKS = "remarks";
        public static final String REASON = "reason";
        public static final String COMPLETED_ON = "completedOn";
        public static final String TOPIC_IDS = "topicIds";
        public static final String TRAINING_ID = "trainingId";
        public static final String USER_ID = "userId";
        public static final String IS_PRESENT = "isPresent";
    }
    
    public enum AttendanceJoin implements IJoinEnum {

        TOPIC_IDS(Fields.TOPIC_IDS, Fields.TOPIC_IDS, JoinType.LEFT);

        private String value;
        private String alias;
        private JoinType joinType;

        public String getValue() {
            return value;
        }

        public String getAlias() {
            return alias;
        }

        public JoinType getJoinType() {
            return joinType;
        }

        AttendanceJoin(String value, String alias, JoinType joinType) {
            this.value = value;
            this.alias = alias;
            this.joinType = joinType;
        }
    }
}
