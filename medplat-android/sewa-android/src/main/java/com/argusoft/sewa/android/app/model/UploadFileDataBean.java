/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.model;

import androidx.annotation.NonNull;

import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author kelvin
 */
@DatabaseTable
public class UploadFileDataBean extends BaseEntity {

    @DatabaseField
    private String checkSum;
    @DatabaseField
    private String formType;
    @DatabaseField
    private String fileType;
    @DatabaseField
    private String fileName;
    @DatabaseField
    private String userName;
    @DatabaseField
    private String parentStatus;
    @DatabaseField
    private String status;
    @DatabaseField
    private int noOfAttemp;
    @DatabaseField
    private String recordUrl;

    public String getRecordUrl() {
        return recordUrl;
    }

    public void setRecordUrl(String recordUrl) {
        this.recordUrl = recordUrl;
    }

    public int getNoOfAttemp() {
        return noOfAttemp;
    }

    public void setNoOfAttemp(int noOfAttemp) {
        this.noOfAttemp = noOfAttemp;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getParentStatus() {
        return parentStatus;
    }

    public void setParentStatus(String parentStatus) {
        this.parentStatus = parentStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return "UploadFileDataBean{" + "checkSum=" + checkSum + ", formType=" + formType + ", fileType=" + fileType + ", fileName=" + fileName + ", userName=" + userName + ", parentStatus=" + parentStatus + ", status=" + status + '}';
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String pack() {
        return checkSum + GlobalTypes.BEAN_SEPARATOR +
                formType +
                GlobalTypes.BEAN_SEPARATOR +
                fileType +
                GlobalTypes.BEAN_SEPARATOR +
                fileName +
                GlobalTypes.BEAN_SEPARATOR +
                userName +
                GlobalTypes.BEAN_SEPARATOR +
                parentStatus +
                GlobalTypes.BEAN_SEPARATOR +
                status;
    }

    public void unpack(String data) {
        String[] split = UtilBean.split(data, GlobalTypes.BEAN_SEPARATOR);

        if (split.length > 6) {
            checkSum = split[0];
            formType = split[1];
            fileType = split[2];
            fileName = split[3];
            userName = split[4];
            parentStatus = split[5];
            status = split[6];
        }
    }
}
