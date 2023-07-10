package com.argusoft.medplat.course.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.common.util.IJoinEnum;
import javax.persistence.criteria.JoinType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author akshar
 */
@Entity
@Table(name = "tr_course_master")
public class CourseMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    private Integer courseId;
    @Column(name = "course_name", nullable = false)
    private String courseName;
    @Column(name = "course_description")
    private String courseDescription;
    @Enumerated(EnumType.STRING)
    @Column(name = "course_state")
    private State courseState;
    @Enumerated(EnumType.STRING)
    @Column(name = "course_type")
    private CourseType courseType;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tr_course_topic_rel", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "topic_id")
    private Set<Integer> topicIds;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tr_course_role_rel", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "role_id")
    private Set<Integer> roleIds;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tr_course_trainer_role_rel", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "trainer_role_id")
    private Set<Integer> trainerRoleIds;

    @Column(name = "module_id")
    private Integer courseModuleId;

    @Column(name = "test_config_json")
    private String testConfigJson;

    @Column(name = "estimated_time_in_hrs")
    private Integer estimatedTimeInHrs;

    @Column(name = "course_image_json")
    private String courseImageJson;

    @Column(name = "is_allowed_to_skip_lessons")
    private Boolean isAllowedToSkipLessons;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public State getCourseState() {
        return courseState;
    }

    public void setCourseState(State courseState) {
        this.courseState = courseState;
    }

    public Set<Integer> getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(Set<Integer> topicIds) {
        this.topicIds = topicIds;
    }

    public Set<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public Set<Integer> getTrainerRoleIds() {
        return trainerRoleIds;
    }

    public void setTrainerRoleIds(Set<Integer> trainerRoleIds) {
        this.trainerRoleIds = trainerRoleIds;
    }

    public Integer getCourseModuleId() {
        return courseModuleId;
    }

    public void setCourseModuleId(Integer courseModuleId) {
        this.courseModuleId = courseModuleId;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    public String getTestConfigJson() {
        return testConfigJson;
    }

    public void setTestConfigJson(String testConfigJson) {
        this.testConfigJson = testConfigJson;
    }

    public Integer getEstimatedTimeInHrs() {
        return estimatedTimeInHrs;
    }

    public void setEstimatedTimeInHrs(Integer estimatedTimeInHrs) {
        this.estimatedTimeInHrs = estimatedTimeInHrs;
    }

    public String getCourseImageJson() {
        return courseImageJson;
    }

    public void setCourseImageJson(String courseImageJson) {
        this.courseImageJson = courseImageJson;
    }

    public Boolean getIsAllowedToSkipLessons() {
        return isAllowedToSkipLessons;
    }

    public void setIsAllowedToSkipLessons(Boolean isAllowedToSkipLessons) {
        this.isAllowedToSkipLessons = isAllowedToSkipLessons;
    }

    @Override
    public String toString() {
        return "CourseMaster{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", courseDescription='" + courseDescription + '\'' +
                ", courseState=" + courseState +
                ", courseType=" + courseType +
                ", topicIds=" + topicIds +
                ", roleIds=" + roleIds +
                ", trainerRoleIds=" + trainerRoleIds +
                ", courseModuleId=" + courseModuleId +
                ", testConfigJson='" + testConfigJson + '\'' +
                ", estimatedTimeInHrs=" + estimatedTimeInHrs +
                ", courseImageJson='" + courseImageJson + '\'' +
                ", isAllowedToSkipLessons=" + isAllowedToSkipLessons +
                '}';
    }

    public enum State {
        ACTIVE,
        INACTIVE,
        ARCHIVED
    }

    public enum CourseType {
        ONLINE,
        OFFLINE
    }

    public static class Fields {

        public static final String COURSE_ID = "courseId";
        public static final String COURSE_NAME = "courseName";
        public static final String COURSE_DESCRIPTION = "courseDescription";
        public static final String COURSE_STATE = "courseState";
        public static final String TOPIC_IDS = "topicIds";
    }

    public enum CourseMasterJoin implements IJoinEnum {

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

        private CourseMasterJoin(String value, String alias, JoinType joinType) {
            this.value = value;
            this.alias = alias;
            this.joinType = joinType;
        }
    }
}
