package com.argusoft.sewa.android.app.databean;

/**
 * <p>
 * Defined fields for health id creation using aadhar biometric details
 * </p>
 *
 */
public class CreateHidAadharBioRequest {
    private String aadhaar;
    private String bioType;
    private String mobileNumber;
    private String pid;

    public String getAadhaar() {
        return aadhaar;
    }

    public void setAadhaar(String aadhaar) {
        this.aadhaar = aadhaar;
    }

    public String getBioType() {
        return bioType;
    }

    public void setBioType(String bioType) {
        this.bioType = bioType;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
