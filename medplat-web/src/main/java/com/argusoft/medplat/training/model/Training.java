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
 *     Define tr_training_master entity and its fields.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "tr_training_master")
public class Training extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "training_id", nullable = false)
    private Integer trainingId;
    @Column(name = "training_name", nullable = false)
    private String trainingName;
    @Column(name = "training_descr")
    private String trainingDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "training_state")
    private State trainingState;

    @Column(name = "location_name")
    private String trainingLocationName;

    @Column(name = "expiration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date trainingExpirationDate;

    @Column(name = "effective_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date trainingEffectiveDate;

    @ElementCollection
    @CollectionTable(name = "tr_training_primary_trainer_rel", joinColumns = @JoinColumn(name = "training_id"))
    @Column(name = "primary_trainer_id", nullable = false)
    private Set<Integer> trainingPrimaryTrainerIds;

    @ElementCollection
    @CollectionTable(name = "tr_training_optional_trainer_rel", joinColumns = @JoinColumn(name = "training_id"))
    @Column(name = "optional_trainer_id")
    private Set<Integer> trainingOptionalTrainerIds;

    @ElementCollection
    @CollectionTable(name = "tr_training_org_unit_rel", joinColumns = @JoinColumn(name = "training_id"))
    @Column(name = "org_unit_id", nullable = false)
    private Set<Integer> trainingOrganizationUnitIds;

    @ElementCollection
    @CollectionTable(name = "tr_training_course_rel", joinColumns = @JoinColumn(name = "training_id"))
    @Column(name = "course_id", nullable = false)
    private Set<Integer> trainingCourseIds;

    @ElementCollection
    @CollectionTable(name = "tr_training_attendee_rel", joinColumns = @JoinColumn(name = "training_id"))
    @Column(name = "attendee_id")
    private Set<Integer> trainingAttendeeIds;

    @ElementCollection
    @CollectionTable(name = "tr_training_additional_attendee_rel", joinColumns = @JoinColumn(name = "training_id"))
    @Column(name = "additional_attendee_id")
    private Set<Integer> trainingAdditionalAttendeeIds;

    @ElementCollection
    @CollectionTable(name = "tr_training_trainer_role_rel", joinColumns = @JoinColumn(name = "training_id"))
    @Column(name = "role_id", nullable = false)
    private Set<Integer> trainerRoleIds;

    @ElementCollection
    @CollectionTable(name = "tr_training_target_role_rel", joinColumns = @JoinColumn(name = "training_id"))
    @Column(name = "target_role_id", nullable = false)
    private Set<Integer> targetRoleIds;

    public Training() {
    }

    public Training(Integer trainingId, String trainingName, String trainingDescription, State trainingState, String trainingLocationName, Date trainingExpirationDate, Date trainingEffectiveDate, Set<Integer> trainingPrimaryTrainerIds, Set<Integer> trainingOptionalTrainerIds, Set<Integer> trainingOrganizationUnitIds, Set<Integer> trainingCourseIds, Set<Integer> trainingAttendeeIds, Set<Integer> trainingAdditionalAttendeeIds, Set<Integer> trainerRoleIds, Set<Integer> targetRoleIds) {
        this.trainingId = trainingId;
        this.trainingName = trainingName;
        this.trainingDescription = trainingDescription;
        this.trainingState = trainingState;
        this.trainingLocationName = trainingLocationName;
        this.trainingExpirationDate = trainingExpirationDate;
        this.trainingEffectiveDate = trainingEffectiveDate;
        this.trainingPrimaryTrainerIds = trainingPrimaryTrainerIds;
        this.trainingOptionalTrainerIds = trainingOptionalTrainerIds;
        this.trainingOrganizationUnitIds = trainingOrganizationUnitIds;
        this.trainingCourseIds = trainingCourseIds;
        this.trainingAttendeeIds = trainingAttendeeIds;
        this.trainingAdditionalAttendeeIds = trainingAdditionalAttendeeIds;
        this.trainerRoleIds = trainerRoleIds;
        this.targetRoleIds = targetRoleIds;
    }

    public Training(Training training) {
        this.trainingId = training.getTrainingId();
        this.trainingName = training.getTrainingName();
        this.trainingDescription = training.getTrainingDescription();
        this.trainingState = training.getTrainingState();
        this.trainingLocationName = training.getTrainingLocationName();
        this.trainingExpirationDate = training.getTrainingExpirationDate();
        this.trainingEffectiveDate = training.getTrainingEffectiveDate();
        this.trainingPrimaryTrainerIds = training.getTrainingPrimaryTrainerIds();
        this.trainingOptionalTrainerIds = training.getTrainingOptionalTrainerIds();
        this.trainingOrganizationUnitIds = training.getTrainingOrganizationUnitIds();
        this.trainingCourseIds = training.getTrainingCourseIds();
        this.trainingAttendeeIds = training.getTrainingAttendeeIds();
        this.trainingAdditionalAttendeeIds = training.getTrainingAdditionalAttendeeIds();
        this.trainerRoleIds = training.getTrainerRoleIds();
        this.targetRoleIds = training.getTargetRoleIds();
    }

    public Integer getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Integer trainingId) {
        this.trainingId = trainingId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getTrainingDescription() {
        return trainingDescription;
    }

    public void setTrainingDescription(String trainingDescription) {
        this.trainingDescription = trainingDescription;
    }

    public State getTrainingState() {
        return trainingState;
    }

    public void setTrainingState(State trainingState) {
        this.trainingState = trainingState;
    }

    public String getTrainingLocationName() {
        return trainingLocationName;
    }

    public void setTrainingLocationName(String trainingLocationName) {
        this.trainingLocationName = trainingLocationName;
    }

    public Date getTrainingExpirationDate() {
        return trainingExpirationDate;
    }

    public void setTrainingExpirationDate(Date trainingExpirationDate) {
        this.trainingExpirationDate = trainingExpirationDate;
    }

    public Date getTrainingEffectiveDate() {
        return trainingEffectiveDate;
    }

    public void setTrainingEffectiveDate(Date trainingEffectiveDate) {
        this.trainingEffectiveDate = trainingEffectiveDate;
    }

    public Set<Integer> getTrainingPrimaryTrainerIds() {
        return trainingPrimaryTrainerIds;
    }

    public void setTrainingPrimaryTrainerIds(Set<Integer> trainingPrimaryTrainerIds) {
        this.trainingPrimaryTrainerIds = trainingPrimaryTrainerIds;
    }

    public Set<Integer> getTrainingOptionalTrainerIds() {
        return trainingOptionalTrainerIds;
    }

    public void setTrainingOptionalTrainerIds(Set<Integer> trainingOptionalTrainerIds) {
        this.trainingOptionalTrainerIds = trainingOptionalTrainerIds;
    }

    public Set<Integer> getTrainingOrganizationUnitIds() {
        return trainingOrganizationUnitIds;
    }

    public void setTrainingOrganizationUnitIds(Set<Integer> trainingOrganizationUnitIds) {
        this.trainingOrganizationUnitIds = trainingOrganizationUnitIds;
    }

    public Set<Integer> getTrainingCourseIds() {
        return trainingCourseIds;
    }

    public void setTrainingCourseIds(Set<Integer> trainingCourseIds) {
        this.trainingCourseIds = trainingCourseIds;
    }

    public Set<Integer> getTrainingAttendeeIds() {
        return trainingAttendeeIds;
    }

    public void setTrainingAttendeeIds(Set<Integer> trainingAttendeeIds) {
        this.trainingAttendeeIds = trainingAttendeeIds;
    }

    public Set<Integer> getTrainingAdditionalAttendeeIds() {
        return trainingAdditionalAttendeeIds;
    }

    public void setTrainingAdditionalAttendeeIds(Set<Integer> trainingAdditionalAttendeeIds) {
        this.trainingAdditionalAttendeeIds = trainingAdditionalAttendeeIds;
    }

    public Set<Integer> getTrainerRoleIds() {
        return trainerRoleIds;
    }

    public void setTrainerRoleIds(Set<Integer> trainerRoleIds) {
        this.trainerRoleIds = trainerRoleIds;
    }

    public Set<Integer> getTargetRoleIds() {
        return targetRoleIds;
    }

    public void setTargetRoleIds(Set<Integer> targetRoleIds) {
        this.targetRoleIds = targetRoleIds;
    }

    public enum State {
        DRAFT,
        SUBMITTED,
        SAVED,
        COMPLETED,
        ARCHIVED
    }

    /**
     * Define fields name for tr_training_master entity.
     */
    public static class Fields {
        private Fields(){}

        public static final String TRAINING_ID = "trainingId";
        public static final String TRAINER_ID = "trainerId";
        public static final String TRAINING_STATE = "trainingState";
        public static final String TRAINING_EXPIRATION_DATE = "trainingExpirationDate";
        public static final String TRAINING_EFFECTIVE_DATE = "trainingEffectiveDate";
        public static final String TRAINING_PRIMARY_TRAINER_IDS = "trainingPrimaryTrainerIds";
        public static final String TRAINING_OPTIONAL_TRAINER_IDS = "trainingOptionalTrainerIds";
        public static final String TRAINING_ORGANIZATION_UNIT_IDS = "trainingOrganizationUnitIds";
        public static final String TRAINING_COURSE_IDS = "trainingCourseIds";
        public static final String TRAINING_ATTENDEE_IDS = "trainingAttendeeIds";
        public static final String TRAINING_ADDITIONAL_ATTENDEE_IDS = "trainingAdditionalAttendeeIds";
        public static final String TRAINER_ROLE_IDS = "trainerRoleIds";
        public static final String TARGET_ROLE_IDS = "targetRoleIds";
    }
    
    public enum TrainingJoin implements IJoinEnum {

        TRAINING_PRIMARY_TRAINER(Fields.TRAINING_PRIMARY_TRAINER_IDS, Fields.TRAINING_PRIMARY_TRAINER_IDS, JoinType.LEFT),
        TRAINING_OPTIONAL_TRAINER(Fields.TRAINING_OPTIONAL_TRAINER_IDS, Fields.TRAINING_OPTIONAL_TRAINER_IDS, JoinType.LEFT),
        TRAINING_ORGANIZATION_UNIT(Fields.TRAINING_ORGANIZATION_UNIT_IDS, Fields.TRAINING_ORGANIZATION_UNIT_IDS, JoinType.LEFT),
        TRAINING_COURSE(Fields.TRAINING_COURSE_IDS, Fields.TRAINING_COURSE_IDS, JoinType.LEFT),
        TRAINING_ATTENDEE(Fields.TRAINING_ATTENDEE_IDS, Fields.TRAINING_ATTENDEE_IDS, JoinType.LEFT),
        TRAINING_ADDITIONAL_ATTENDEE(Fields.TRAINING_ADDITIONAL_ATTENDEE_IDS, Fields.TRAINING_ADDITIONAL_ATTENDEE_IDS, JoinType.LEFT),
        TARGET_ROLE(Fields.TARGET_ROLE_IDS, Fields.TARGET_ROLE_IDS, JoinType.LEFT),
        TRAINER_ROLE(Fields.TRAINER_ROLE_IDS, Fields.TRAINER_ROLE_IDS, JoinType.LEFT);

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

        TrainingJoin(String value, String alias, JoinType joinType) {
            this.value = value;
            this.alias = alias;
            this.joinType = joinType;
        }
    }
}
