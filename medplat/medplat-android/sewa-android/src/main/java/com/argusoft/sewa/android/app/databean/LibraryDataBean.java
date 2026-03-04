package com.argusoft.sewa.android.app.databean;

import com.argusoft.sewa.android.app.model.LibraryBean;

/**
 * Created by prateek on 12/2/19.
 */
public class LibraryDataBean {

    private Long actualId;
    private Long roleId;
    private String category;
    private String fileName;
    private String fileType;
    private String state;
    private String description;
    private Long modifiedOn;
    private Boolean isDownloaded;
    private String tag;

    public LibraryDataBean(LibraryBean libraryBean) {
        this.actualId = libraryBean.getActualId();
        this.category = libraryBean.getCategory();
        this.fileName = libraryBean.getFileName();
        this.fileType = libraryBean.getFileType();
        this.isDownloaded = libraryBean.getIsDownloaded();
        this.state = libraryBean.getState();
        this.description = libraryBean.getDescription();
        this.tag = libraryBean.getTag();
        if (libraryBean.getModifiedOn() != null) {
            this.modifiedOn = libraryBean.getModifiedOn().getTime();
        }
    }

    public Long getActualId() {
        return actualId;
    }

    public void setActualId(Long actualId) {
        this.actualId = actualId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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

    public Long getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Long modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Boolean getIsDownloaded() {
        return isDownloaded;
    }

    public void setIsDownloaded(Boolean isDownloaded) {
        this.isDownloaded = isDownloaded;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
