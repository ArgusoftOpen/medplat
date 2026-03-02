/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.restclient;

import androidx.annotation.NonNull;

/**
 * @author alpesh
 */
public class RestFileMultipart {

    private byte[] data;
    private FILE_TYPE type;
    private String fileName;
    private String description;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public FILE_TYPE getType() {
        return type;
    }

    public void setType(FILE_TYPE type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return "RestFileMultipart{" + "type=" + type + ", fileName=" + fileName + ", description=" + description + '}';
    }

    public enum FILE_TYPE {

        IMAGE("image"),
        AUDIO("audio"),
        VIDEO("video"),
        DOCUMENT("document");
        private final String value;

        FILE_TYPE(String a) {
            this.value = a;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "FILE_TYPE{" + "value=" + value + '}';
        }

    }

}
