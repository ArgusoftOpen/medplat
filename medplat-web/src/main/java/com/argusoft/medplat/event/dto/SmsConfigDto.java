package com.argusoft.medplat.event.dto;

/**
 *
 * <p>
 *     Used for sms config.
 * </p>
 * @author sneha
 * @since 26/08/20 11:00 AM
 *
 */
public class SmsConfigDto {

    private String mobileNumberFieldName;
    private String smsTypeField;
    private Boolean isSmsTypeFixed;
    private Boolean isPriority;

    public String getMobileNumberFieldName() {
        return mobileNumberFieldName;
    }

    public void setMobileNumberFieldName(String mobileNumberFieldName) {
        this.mobileNumberFieldName = mobileNumberFieldName;
    }

    public String getSmsTypeField() {
        return smsTypeField;
    }

    public void setSmsTypeField(String smsTypeField) {
        this.smsTypeField = smsTypeField;
    }

    public Boolean getIsSmsTypeFixed() {
        return isSmsTypeFixed == null ? Boolean.FALSE : isSmsTypeFixed;
    }

    public void setIsSmsTypeFixed(Boolean isSmsTypeFixed) {
        this.isSmsTypeFixed = isSmsTypeFixed;
    }

    public Boolean getIsPriority() {
        return isPriority == null ? Boolean.FALSE : isPriority;
    }

    public void setIsPriority(Boolean isPriority) {
        this.isPriority = isPriority;
    }
}
