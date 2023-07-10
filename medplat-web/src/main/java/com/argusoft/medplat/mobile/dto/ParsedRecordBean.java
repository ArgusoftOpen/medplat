/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.dto;

import java.util.Comparator;

/**
 *
 * @author prateek
 */
public class ParsedRecordBean {

    private String checksum;
    private String mobileDate;
    private String answerEntity;
    private String customType;
    private String relativeId;
    private String formFillTime;
    private String notificationId;
    private String morbidityFrame;
    private String answerRecord;
    private Integer villageId;
    private String associatedId;
    private String generatedId;
    private String message;
    private Boolean isAbhaFailed;

    public Boolean getAbhaFailed() {
        return isAbhaFailed;
    }

    public void setAbhaFailed(Boolean abhaFailed) {
        isAbhaFailed = abhaFailed;
    }

    public String getAssociatedId() {
        return associatedId;
    }

    public void setAssociatedId(String associatedId) {
        this.associatedId = associatedId;
    }

    public Integer getVillageId() {
        return villageId;
    }

    public void setVillageId(Integer villageId) {
        this.villageId = villageId;
    }

    public String getMobileDate() {
        return mobileDate;
    }

    public void setMobileDate(String mobileDate) {
        this.mobileDate = mobileDate;
    }

    public String getAnswerEntity() {
        return answerEntity;
    }

    public void setAnswerEntity(String answerEntity) {
        this.answerEntity = answerEntity;
    }

    public String getAnswerRecord() {
        return answerRecord;
    }

    public void setAnswerRecord(String answerRecord) {
        this.answerRecord = answerRecord;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getCustomType() {
        return customType;
    }

    public void setCustomType(String customType) {
        this.customType = customType;
    }

    public String getFormFillTime() {
        return formFillTime;
    }

    public void setFormFillTime(String formFillTime) {
        this.formFillTime = formFillTime;
    }

    public String getMorbidityFrame() {
        return morbidityFrame;
    }

    public void setMorbidityFrame(String morbidityFrame) {
        this.morbidityFrame = morbidityFrame;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getRelativeId() {
        return relativeId;
    }

    public void setRelativeId(String relativeId) {
        this.relativeId = relativeId;
    }

    public String getGeneratedId() {
        return generatedId;
    }

    public void setGeneratedId(String generatedId) {
        this.generatedId = generatedId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public ParsedRecordBean() {
    }

    @Override
    public String toString() {
        return "ParsedRecordBean{" + "checksum=" + checksum + ", mobileDate=" + mobileDate + ", answerEntity=" + answerEntity + ", customType=" + customType + ", relativeId=" + relativeId + ", formFillTime=" + formFillTime + ", notificationId=" + notificationId + ", morbidityFrame=" + morbidityFrame + ", answerRecord=" + answerRecord + '}';
    }

    public enum ParsedRecordBeanComparator implements Comparator<ParsedRecordBean> {
        SORT_RELATIVE_ID {
            @Override
            public int compare(ParsedRecordBean o1, ParsedRecordBean o2) {
                return (o1.getRelativeId().toLowerCase()).compareTo(o2.getRelativeId().toLowerCase());
            }
        };

        public static Comparator<ParsedRecordBean> decending(final Comparator<ParsedRecordBean> other) {
            return (o1, o2) -> -1 * other.compare(o1, o2);
        }

        public static Comparator<ParsedRecordBean> getComparator(final ParsedRecordBeanComparator... multipleOptions) {
            return (o1, o2) -> {
                for (ParsedRecordBeanComparator option : multipleOptions) {
                    int result = option.compare(o1, o2);
                    if (result != 0) {
                        return result;
                    }
                }
                return 0;
            };
        }
    }
}
