/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.document.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;

/**
 *
 * @author jay
 */
@Entity
@Table(name = "document_master")
public class DocumentMaster extends EntityAuditInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_name_th")
    private String fileNameTh;

    @Column(name = "extension")
    private String extension;

    @Column(name = "actual_file_name")
    private String actualFileName;

    @Column(name = "module_id")
    private Integer moduleId;

    @Column(name = "is_temporary", columnDefinition = "boolean default false")
    private boolean isTemporary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNameTh() {
        return fileNameTh;
    }

    public void setFileNameTh(String fileNameTh) {
        this.fileNameTh = fileNameTh;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getActualFileName() {
        return actualFileName;
    }

    public void setActualFileName(String actualFileName) {
        this.actualFileName = actualFileName;
    }

    public boolean getIsTemporary() {
        return isTemporary;
    }

    public void setIsTemporary(boolean isTemporary) {
        this.isTemporary = isTemporary;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }
}
