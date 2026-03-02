package com.argusoft.sewa.android.app.model;

import com.argusoft.sewa.android.app.databean.LmsUserMetaData;
import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class LmsUserMetaDataBean extends BaseEntity implements Serializable {

    @DatabaseField
    private Integer courseId;

    @DatabaseField
    private String quizMetaData;

    @DatabaseField
    private String lessonMetaData;

    public LmsUserMetaDataBean() {
    }

    public LmsUserMetaDataBean(LmsUserMetaData metaData) {
        Gson gson = new Gson();
        this.courseId = metaData.getCourseId();
        this.quizMetaData = gson.toJson(metaData.getQuizMetaData());
        this.lessonMetaData = gson.toJson(metaData.getLessonMetaData());
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

    @Override
    public String toString() {
        return "LmsUserMetaDataBean{" +
                "courseId=" + courseId +
                ", quizMetaData='" + quizMetaData + '\'' +
                ", lessonMetaData='" + lessonMetaData + '\'' +
                '}';
    }
}
