package com.argusoft.medplat.course.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tr_question_set_configuration")
public class QuestionSetConfiguration extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "ref_id")
    private Integer refId;

    @Column(name = "ref_type")
    private String refType;

    @Column(name = "question_set_name")
    private String questionSetName;

    @Column(name = "status")
    private String status;

    @Column(name = "minimum_marks")
    private Integer minimumMarks;

    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "question_set_type")
    private Integer questionSetType;

    @Column(name = "quiz_at_second")
    private Integer quizAtSecond;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public String getQuestionSetName() {
        return questionSetName;
    }

    public void setQuestionSetName(String questionSetName) {
        this.questionSetName = questionSetName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getMinimumMarks() {
        return minimumMarks;
    }

    public void setMinimumMarks(Integer minimumMarks) {
        this.minimumMarks = minimumMarks;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getQuestionSetType() {
        return questionSetType;
    }

    public void setQuestionSetType(Integer questionSetType) {
        this.questionSetType = questionSetType;
    }

    public Integer getQuizAtSecond() {
        return quizAtSecond;
    }

    public void setQuizAtSecond(Integer quizAtSecond) {
        this.quizAtSecond = quizAtSecond;
    }
}
