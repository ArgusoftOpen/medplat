package com.argusoft.medplat.course.dto;

import java.util.List;

public class LmsUserMetaDataJson {

    private List<LmsUserQuizMetaData> attempts;

    private List<LmsUserLessonMetaData> lessonsMetaData;

    public List<LmsUserQuizMetaData> getAttempts() {
        return attempts;
    }

    public void setAttempts(List<LmsUserQuizMetaData> attempts) {
        this.attempts = attempts;
    }

    public List<LmsUserLessonMetaData> getLessonsMetaData() {
        return lessonsMetaData;
    }

    public void setLessonsMetaData(List<LmsUserLessonMetaData> lessonsMetaData) {
        this.lessonsMetaData = lessonsMetaData;
    }
}
