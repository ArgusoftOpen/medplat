package com.argusoft.medplat.course.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tr_question_set_answer")
public class QuestionSetAnswerMaster extends EntityAuditInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "question_set_id", nullable = false)
    private Integer questionSetId;

    @Column(name = "marks_scored", nullable = false)
    private Integer marksScored;

    @Column(name = "passing_marks", nullable = false)
    private Integer passingMarks;

    @Column(name = "is_passed", nullable = false)
    private Boolean isPassed;

    @Column(name = "answer_json", nullable = false)
    private String answerJson;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "is_locked")
    private Boolean isLocked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestionSetId() {
        return questionSetId;
    }

    public void setQuestionSetId(Integer questionSetId) {
        this.questionSetId = questionSetId;
    }

    public Integer getMarksScored() {
        return marksScored;
    }

    public void setMarksScored(Integer marksScored) {
        this.marksScored = marksScored;
    }

    public Integer getPassingMarks() {
        return passingMarks;
    }

    public void setPassingMarks(Integer passingMarks) {
        this.passingMarks = passingMarks;
    }

    public Boolean getPassed() {
        return isPassed;
    }

    public void setPassed(Boolean passed) {
        isPassed = passed;
    }

    public String getAnswerJson() {
        return answerJson;
    }

    public void setAnswerJson(String answerJson) {
        this.answerJson = answerJson;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }
}
