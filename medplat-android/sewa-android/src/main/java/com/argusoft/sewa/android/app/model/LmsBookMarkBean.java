package com.argusoft.sewa.android.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class LmsBookMarkBean extends BaseEntity implements Serializable {

    @DatabaseField
    private String bookmarkText;
    @DatabaseField
    private String bookmarkNote;
    @DatabaseField
    private int position;
    @DatabaseField
    private String fileName;
    @DatabaseField
    private Integer lessonId;

    public String getBookmarkText() {
        return bookmarkText;
    }

    public void setBookmarkText(String bookmarkText) {
        this.bookmarkText = bookmarkText;
    }

    public String getBookmarkNote() {
        return bookmarkNote;
    }

    public void setBookmarkNote(String bookmarkNote) {
        this.bookmarkNote = bookmarkNote;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    @Override
    public String toString() {
        return "LmsBookMarkBean{" +
                "bookmarkText='" + bookmarkText + '\'' +
                ", bookmarkNote='" + bookmarkNote + '\'' +
                ", position=" + position +
                ", fileName='" + fileName + '\'' +
                ", lessonId=" + lessonId +
                '}';
    }
}
