package com.argusoft.sewa.android.app.model;

import com.argusoft.sewa.android.app.databean.LibraryDataBean;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by prateek on 12/2/19.
 */
@DatabaseTable
public class LibraryBean extends BaseEntity implements Serializable {

    @DatabaseField
    private Long actualId;
    @DatabaseField
    private String category;
    @DatabaseField
    private String fileName;
    @DatabaseField
    private String fileType;
    @DatabaseField
    private String state;
    @DatabaseField
    private String description;
    @DatabaseField
    private Date modifiedOn;
    @DatabaseField
    private boolean isDownloaded;
    @DatabaseField
    private String tag;

    public LibraryBean() {
    }

    public LibraryBean(LibraryDataBean libraryDataBean) {
        this.actualId = libraryDataBean.getActualId();
        this.category = libraryDataBean.getCategory();
        this.fileName = libraryDataBean.getFileName();
        this.fileType = libraryDataBean.getFileType();
        this.state = libraryDataBean.getState();
        this.description = libraryDataBean.getDescription();
        this.isDownloaded = libraryDataBean.getIsDownloaded() != null && libraryDataBean.getIsDownloaded();
        this.tag = libraryDataBean.getTag();
        if (libraryDataBean.getModifiedOn() != null) {
            this.modifiedOn = new Date(libraryDataBean.getModifiedOn());
        }
    }

    public Long getActualId() {
        return actualId;
    }

    public void setActualId(Long actualId) {
        this.actualId = actualId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public boolean getIsDownloaded() {
        return isDownloaded;
    }

    public void setIsDownloaded(boolean isDownloaded) {
        this.isDownloaded = isDownloaded;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
