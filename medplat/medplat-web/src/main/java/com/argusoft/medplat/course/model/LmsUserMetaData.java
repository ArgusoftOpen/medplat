package com.argusoft.medplat.course.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tr_user_meta_data")
public class LmsUserMetaData extends EntityAuditInfo {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "course_id", nullable = false)
    private Integer courseId;

    @Column(name = "quiz_meta_data")
    private String quizMetaData;

    @Column(name = "lesson_meta_data")
    private String lessonMetaData;

    @Column(name = "is_course_completed")
    private Boolean isCourseCompleted;

    @Column(name = "last_accessed_lesson_on")
    private Date lastAccessedLessonOn;

    @Column(name = "last_accessed_quiz_on")
    private Date lastAccessedQuizOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getQuizMetaData() {
        return quizMetaData;
    }

    public void setQuizMetaData(String quizMetaData) {
        this.quizMetaData = quizMetaData;
    }

    public String getLessonMetaData() {
        return lessonMetaData;
    }

    public void setLessonMetaData(String lessonMetaData) {
        this.lessonMetaData = lessonMetaData;
    }

    public Boolean getCourseCompleted() {
        return isCourseCompleted;
    }

    public void setCourseCompleted(Boolean courseCompleted) {
        isCourseCompleted = courseCompleted;
    }

    public Date getLastAccessedLessonOn() {
        return lastAccessedLessonOn;
    }

    public void setLastAccessedLessonOn(Date lastAccessedLessonOn) {
        this.lastAccessedLessonOn = lastAccessedLessonOn;
    }

    public Date getLastAccessedQuizOn() {
        return lastAccessedQuizOn;
    }

    public void setLastAccessedQuizOn(Date lastAccessedQuizOn) {
        this.lastAccessedQuizOn = lastAccessedQuizOn;
    }
}
