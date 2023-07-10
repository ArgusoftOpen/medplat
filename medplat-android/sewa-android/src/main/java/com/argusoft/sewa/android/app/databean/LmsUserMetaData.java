package com.argusoft.sewa.android.app.databean;

import com.argusoft.sewa.android.app.model.LmsUserMetaDataBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class LmsUserMetaData {

    Integer courseId;

    List<LmsUserQuizMetaData> quizMetaData;

    List<LmsUserLessonMetaData> lessonMetaData;

    public LmsUserMetaData() {
    }

    public LmsUserMetaData(LmsUserMetaDataBean metaDataBean) {
        if (metaDataBean == null) {
            return;
        }
        this.courseId = metaDataBean.getCourseId();
        Gson gson = new Gson();
        if (metaDataBean.getQuizMetaData() != null) {
            this.quizMetaData = gson.fromJson(metaDataBean.getQuizMetaData(), new TypeToken<List<LmsUserQuizMetaData>>() {
            }.getType());
        }
        if (metaDataBean.getLessonMetaData() != null) {
            this.lessonMetaData = gson.fromJson(metaDataBean.getLessonMetaData(), new TypeToken<List<LmsUserLessonMetaData>>() {
            }.getType());
        }
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public List<LmsUserQuizMetaData> getQuizMetaData() {
        return quizMetaData;
    }

    public void setQuizMetaData(List<LmsUserQuizMetaData> quizMetaData) {
        this.quizMetaData = quizMetaData;
    }

    public List<LmsUserLessonMetaData> getLessonMetaData() {
        return lessonMetaData;
    }

    public void setLessonMetaData(List<LmsUserLessonMetaData> lessonMetaData) {
        this.lessonMetaData = lessonMetaData;
    }

    @Override
    public String toString() {
        return "LmsUserMetaData{" +
                "courseId=" + courseId +
                ", quizMetaData=" + quizMetaData +
                ", lessonMetaData=" + lessonMetaData +
                '}';
    }
}
