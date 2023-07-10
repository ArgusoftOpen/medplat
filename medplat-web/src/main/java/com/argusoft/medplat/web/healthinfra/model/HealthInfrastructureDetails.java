/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.healthinfra.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * <p>
 *     Define health_infrastructure_details entity and its fields.
 * </p>
 * @author vaishali
 * @since 26/08/20 11:00 AM
 *
 */
@Table(name = "health_infrastructure_details")
@Entity
public class HealthInfrastructureDetails implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type")
    private Integer type;

    @Column(name = "sub_type")
    private Integer subType;

    @Column(name = "address")
    private String address;

    @Column(name = "name")
    private String name;

    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "state")
    private String state;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "nin")
    private String nin;

    @Column(name = "emamta_id")
    private Long emamtaId;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "landline_number")
    private String landlineNumber;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "name_in_english")
    private String nameInEnglish;

    @Column(name = "no_of_beds")
    private Integer noOfBeds;

    @Column(name = "is_nrc")
    private Boolean isNrc;

    @Column(name = "is_cmtc")
    private Boolean isCmtc;

    @Column(name = "is_sncu")
    private Boolean isSncu;

    @Column(name = "is_chiranjeevi_scheme")
    private Boolean isChiranjeeviScheme;

    @Column(name = "is_balsaka")
    private Boolean isBalsaka;

    @Column(name = "is_pmjy")
    private Boolean isPmjy;

    @Column(name = "is_fru")
    private Boolean isFru;

    @Column(name = "for_ncd")
    private Boolean forNcd;

    @Column(name = "is_blood_bank")
    private Boolean isBloodBank;

    @Column(name = "is_gynaec")
    private Boolean isGynaec;

    @Column(name = "is_pediatrician")
    private Boolean isPediatrician;

    @Column(name = "is_cpconfirmationcenter")
    private Boolean isCpConfirmationCenter;

    @Column(name = "is_balsakha1")
    private Boolean isBalsakha1;

    @Column(name = "is_balsakha3")
    private Boolean isBalsakha3;

    @Column(name = "is_usg_facility")
    private Boolean isUsgFacility;

    @Column(name = "is_referral_facility")
    private Boolean isReferralFacility;

    @Column(name = "is_ma_yojna")
    private Boolean isMaYojna;

    @Column(name = "is_npcb")
    private Boolean isNpcb;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name="created_by")
    private Integer createdBy;

    @Column(name = "modified_on")
    private Date modifiedOn;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "registration_number")
    private String registrationNumber;
    
    @Column(name  = "is_no_reporting_unit")
    private Boolean isNoReportingUnit;

    @Column(name  = "hfr_facility_id")
    private String hfrFacilityId;

    @Column(name = "other_details")
    private String otherDetails;

    @Column(name = "speciality_type")
    private String specialityType;

    @Column(name = "type_of_services")
    private String typeOfServices;

    @Column(name = "ownership_code")
    private String ownershipCode;

    @Column(name = "ownership_sub_type_code")
    private String ownershipSubTypeCode;

    @Column(name = "ownership_sub_type_code2")
    private String ownershipSubTypeCode2;

    @Column(name = "system_of_medicine_code")
    private String systemOfMedicineCode;

    @Column(name="facility_type_code")
    private String facilityTypeCode;

    @Column(name="facility_sub_type_code")
    private String facilitySubTypeCode;

    @Column(name="facility_region")
    private String facilityRegion;

    @Column(name  = "is_link_to_bridge_id")
    private Boolean isLinkToBridgeId;

    public Boolean getIsNoReportingUnit() {
        return isNoReportingUnit;
    }

    public void setIsNoReportingUnit(Boolean isNoReportingUnit) {
        this.isNoReportingUnit = isNoReportingUnit;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public enum State {
        ACTIVE
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public Long getEmamtaId() {
         return emamtaId;
    }

    public void setEmamtaId(Long emamtaId) {
        this.emamtaId = emamtaId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLandlineNumber() {
        return landlineNumber;
    }

    public void setLandlineNumber(String landlineNumber) {
        this.landlineNumber = landlineNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNameInEnglish() {
        return nameInEnglish;
    }

    public void setNameInEnglish(String nameInEnglish) {
        this.nameInEnglish = nameInEnglish;
    }

    public Integer getNoOfBeds() {
        return noOfBeds;
    }

    public void setNoOfBeds(Integer noOfBeds) {
        this.noOfBeds = noOfBeds;
    }

    public Boolean getIsNrc() {
        return isNrc;
    }

    public void setIsNrc(Boolean isNrc) {
        this.isNrc = isNrc;
    }

    public Boolean getIsCmtc() {
        return isCmtc;
    }

    public void setIsCmtc(Boolean isCmtc) {
        this.isCmtc = isCmtc;
    }

    public Boolean getIsSncu() {
        return isSncu;
    }

    public void setIsSncu(Boolean isSncu) {
        this.isSncu = isSncu;
    }

    public Boolean getIsChiranjeeviScheme() {
        return isChiranjeeviScheme;
    }

    public void setIsChiranjeeviScheme(Boolean isChiranjeeviScheme) {
        this.isChiranjeeviScheme = isChiranjeeviScheme;
    }

    public Boolean getIsBalsaka() {
        return isBalsaka;
    }

    public void setIsBalsaka(Boolean isBalsaka) {
        this.isBalsaka = isBalsaka;
    }

    public Boolean getIsPmjy() {
        return isPmjy;
    }

    public void setIsPmjy(Boolean isPmjy) {
        this.isPmjy = isPmjy;
    }

    public Boolean getIsFru() {
        return isFru;
    }

    public void setIsFru(Boolean isFru) {
        this.isFru = isFru;
    }

    public Boolean getForNcd() {
        return forNcd;
    }

    public void setForNcd(Boolean forNcd) {
        this.forNcd = forNcd;
    }

    public Boolean getIsBloodBank() {
        return isBloodBank;
    }

    public void setIsBloodBank(Boolean isBloodBank) {
        this.isBloodBank = isBloodBank;
    }

    public Boolean getIsGynaec() {
        return isGynaec;
    }

    public void setIsGynaec(Boolean isGynaec) {
        this.isGynaec = isGynaec;
    }

    public Boolean getIsPediatrician() {
        return isPediatrician;
    }

    public void setIsPediatrician(Boolean isPediatrician) {
        this.isPediatrician = isPediatrician;
    }

    public Boolean getIsCpConfirmationCenter() {
        return isCpConfirmationCenter;
    }

    public void setIsCpConfirmationCenter(Boolean isCpConfirmationCenter) {
        this.isCpConfirmationCenter = isCpConfirmationCenter;
    }

    public Boolean getIsBalsakha1() {
        return isBalsakha1;
    }

    public void setIsBalsakha1(Boolean isBalsakha1) {
        this.isBalsakha1 = isBalsakha1;
    }

    public Boolean getIsBalsakha3() {
        return isBalsakha3;
    }

    public void setIsBalsakha3(Boolean isBalsakha3) {
        this.isBalsakha3 = isBalsakha3;
    }

    public Boolean getIsUsgFacility() {
        return isUsgFacility;
    }

    public void setIsUsgFacility(Boolean isUsgFacility) {
        this.isUsgFacility = isUsgFacility;
    }

    public Boolean getIsReferralFacility() {
        return isReferralFacility;
    }

    public void setIsReferralFacility(Boolean isReferralFacility) {
        this.isReferralFacility = isReferralFacility;
    }

    public Boolean getIsMaYojna() {
        return isMaYojna;
    }

    public void setIsMaYojna(Boolean isMaYojna) {
        this.isMaYojna = isMaYojna;
    }

    public Boolean getIsNpcb() {
        return isNpcb;
    }

    public void setIsNpcb(Boolean isNpcb) {
        this.isNpcb = isNpcb;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    public String getSpecialityType() {
        return specialityType;
    }

    public void setSpecialityType(String specialityType) {
        this.specialityType = specialityType;
    }

    public String getTypeOfServices() {
        return typeOfServices;
    }

    public void setTypeOfServices(String typeOfServices) {
        this.typeOfServices = typeOfServices;
    }

    public String getOwnershipCode() {
        return ownershipCode;
    }

    public void setOwnershipCode(String ownershipCode) {
        this.ownershipCode = ownershipCode;
    }

    public String getOwnershipSubTypeCode() {
        return ownershipSubTypeCode;
    }

    public void setOwnershipSubTypeCode(String ownershipSubTypeCode) {
        this.ownershipSubTypeCode = ownershipSubTypeCode;
    }

    public String getOwnershipSubTypeCode2() {
        return ownershipSubTypeCode2;
    }

    public void setOwnershipSubTypeCode2(String ownershipSubTypeCode2) {
        this.ownershipSubTypeCode2 = ownershipSubTypeCode2;
    }

    public String getSystemOfMedicineCode() {
        return systemOfMedicineCode;
    }

    public void setSystemOfMedicineCode(String systemOfMedicineCode) {
        this.systemOfMedicineCode = systemOfMedicineCode;
    }

    public String getFacilityTypeCode() {
        return facilityTypeCode;
    }

    public void setFacilityTypeCode(String facilityTypeCode) {
        this.facilityTypeCode = facilityTypeCode;
    }

    public String getFacilitySubTypeCode() {
        return facilitySubTypeCode;
    }

    public void setFacilitySubTypeCode(String facilitySubTypeCode) {
        this.facilitySubTypeCode = facilitySubTypeCode;
    }

    public String getHfrFacilityId() {
        return hfrFacilityId;
    }

    public void setHfrFacilityId(String hfrFacilityId) {
        this.hfrFacilityId = hfrFacilityId;
    }

    public String getFacilityRegion() {
        return facilityRegion;
    }

    public void setFacilityRegion(String facilityRegion) {
        this.facilityRegion = facilityRegion;
    }

    public Boolean getLinkToBridgeId() {
        return isLinkToBridgeId;
    }

    public void setLinkToBridgeId(Boolean linkToBridgeId) {
        isLinkToBridgeId = linkToBridgeId;
    }


    public static class Fields {
        private Fields() {

        }
        public static final String HFR_FACILITY_ID = "hfrFacilityId";
    }
}
