package com.argusoft.sewa.android.app.databean;

public class BookmarkDataBean {

    private int position;
    private String bookmarkText;
    private String bookmarkNote;
    private String fileName;

    public BookmarkDataBean(int position, String bookmarkText, String bookmarkNote, String fileName) {
        this.position = position;
        this.bookmarkText = bookmarkText;
        this.bookmarkNote = bookmarkNote;
        this.fileName = fileName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "BookmarkDataBean{" +
                "position=" + position +
                ", bookmarkText='" + bookmarkText + '\'' +
                ", bookmarkNote='" + bookmarkNote + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
