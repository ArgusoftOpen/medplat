package com.argusoft.sewa.android.app.model;

import androidx.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable
public class CovidTravellersInfoBean extends BaseEntity implements Serializable {

    @DatabaseField
    private Integer actualId;

    @DatabaseField
    private Integer memberId;

    @DatabaseField
    private Integer locationId;

    @DatabaseField
    private String name;

    @DatabaseField
    private String address;

    @DatabaseField
    private Integer pinCode;

    @DatabaseField
    private String mobileNumber;

    @DatabaseField
    private Boolean isActive;

    @DatabaseField
    private Date modifiedOn;

    @DatabaseField
    private String flightNo;

    @DatabaseField
    private Integer age;

    @DatabaseField
    private String gender;

    @DatabaseField
    private String country;

    @DatabaseField
    private Integer districtId;

    @DatabaseField
    private String districtName;

    @DatabaseField
    private Date dateOfDeparture;

    @DatabaseField
    private Date dateOfReceipt;

    @DatabaseField
    private String healthStatus;

    @DatabaseField
    private String observation;

    @DatabaseField
    private Date trackingStartDate;

    public Integer getActualId() {
        return actualId;
    }

    public void setActualId(Integer actualId) {
        this.actualId = actualId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPinCode() {
        return pinCode;
    }

    public void setPinCode(Integer pinCode) {
        this.pinCode = pinCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Date getModifiedOn() {

        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Date getDateOfDeparture() {
        return dateOfDeparture;
    }

    public void setDateOfDeparture(Date dateOfDeparture) {
        this.dateOfDeparture = dateOfDeparture;
    }

    public Date getDateOfReceipt() {
        return dateOfReceipt;
    }

    public void setDateOfReceipt(Date dateOfReceipt) {
        this.dateOfReceipt = dateOfReceipt;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Date getTrackingStartDate() {
        return trackingStartDate;
    }

    public void setTrackingStartDate(Date trackingStartedDate) {
        this.trackingStartDate = trackingStartedDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "CovidTravellersInfoBean{" +
                "actualId=" + actualId +
                ", memberId=" + memberId +
                ", locationId=" + locationId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", pinCode=" + pinCode +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", isActive=" + isActive +
                ", modifiedOn=" + modifiedOn +
                ", flightNo='" + flightNo + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", country='" + country + '\'' +
                ", districtId=" + districtId +
                ", districtName='" + districtName + '\'' +
                ", dateOfDeparture=" + dateOfDeparture +
                ", dateOfReceipt=" + dateOfReceipt +
                ", healthStatus='" + healthStatus + '\'' +
                ", observation='" + observation + '\'' +
                ", trackingStartDate=" + trackingStartDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
