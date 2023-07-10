package com.argusoft.medplat.familyqrcode.dto;

/**
 * @author kripansh
 * @since 07/04/23 3:45 pm
 */
public class FamilyQRCodeDto {
    private String familyId;
    private String location;
    private String address;
    private String familyHead;
    private String contactNumber;
    private String ashaName;
    private String qrCode;
    private String houseNumber;
    private String qrLocation;

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFamilyHead() {
        return familyHead;
    }

    public void setFamilyHead(String familyHead) {
        this.familyHead = familyHead;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAshaName() {
        return ashaName;
    }

    public void setAshaName(String ashaName) {
        this.ashaName = ashaName;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getQrLocation() {
        return qrLocation;
    }

    public void setQrLocation(String qrLocation) {
        this.qrLocation = qrLocation;
    }
}
