package com.argusoft.medplat.fhs.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.common.util.IJoinEnum;

import javax.persistence.*;
import javax.persistence.criteria.JoinType;
import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * Define imt_family entity and its fields.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "imt_family")
public class FamilyEntity extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(name = "family_id")
    private String familyId;

    @Column(name = "house_number")
    private String houseNumber;

    @Column(name = "location_id")
    private Integer locationId;

    @Column
    private String religion;

    @Column
    private String caste;

    @Column(name = "bpl_flag")
    private Boolean bplFlag;

    @Column(name = "anganwadi_id")
    private Integer anganwadiId;

    @Column(name = "toilet_available_flag")
    private Boolean toiletAvailableFlag;

    @Column(name = "is_verified_flag")
    private Boolean isVerifiedFlag;

    @Column
    private String state;

    @Column(name = "assigned_to")
    private Integer assignedTo;

    @Column
    private String address1;

    @Column
    private String address2;

    @Column(name = "maa_vatsalya_number")
    private String maaVatsalyaNumber;

    @Column(name = "area_id")
    private Integer areaId;

    @Column(name = "vulnerable_flag")
    private Boolean vulnerableFlag;

    @Column(name = "migratory_flag")
    private Boolean migratoryFlag;

    @Column
    private String latitude;

    @Column
    private String longitude;

    @Column(name = "rsby_card_number")
    private String rsbyCardNumber;

    @Column(name = "comment", length = 4000)
    private String comment;

    @Column(name = "current_state")
    private Integer currentState;

    @Column(name = "is_report")
    private Boolean isReport;

    @Column(name = "contact_person_id")
    private Integer contactPersonId;

    @Column(name = "hof_id")
    private Integer headOfFamily;

    @Column(name = "additional_info")
    private String additionalInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_state", referencedColumnName = "id", insertable = false, updatable = false)
    private FamilyStateDetailEntity currentStateDetail;

    @Column(name = "merged_with")
    private String mergedWith;

    @Column(name = "anganwadi_update_flag")
    private Boolean anganwadiUpdateFlag;

    @Column(name = "any_member_cbac_done")
    private Boolean anyMemberCbacDone;

    @Column(name = "type_of_house")
    private String typeOfHouse;

    @Column(name = "type_of_toilet")
    private String typeOfToilet;

    @Column(name = "other_toilet")
    private String otherToilet;

    @Column(name = "electricity_availability")
    private String electricityAvailability;

    @Column(name = "drinking_water_source")
    private String drinkingWaterSource;

    @Column(name = "other_water_source")
    private String otherWaterSource;

    @Column(name = "fuel_for_cooking")
    private String fuelForCooking;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "imt_family_vehicle_detail_rel", joinColumns = @JoinColumn(name = "family_id"))
    @Column(name = "vehicle_details")
    private Set<String> vehicleDetails;

    @Column(name = "other_motorized_vehicle")
    private String otherMotorizedVehicle;

    @Column(name = "house_ownership_status")
    private String houseOwnershipStatus;

    @Column(name = "ration_card_number")
    private String rationCardNumber;

    @Column(name = "pmjay_card_number")
    private String pmjayCardNumber;

    @Column(name = "bpl_card_number")
    private String bplCardNumber;

    @Column(name = "annual_income")
    private String annualIncome;

    @Column(name = "remarks")
    private String remarks;


    @Column(name = "split_from")
    private Integer splitFrom;

    @Column(name = "is_providing_consent")
    private Boolean isProvidingConsent;

    @Column(name = "vulnerability_criteria")
    private String vulnerabilityCriteria;

    @Column(name = "eligible_for_pmjay")
    private Boolean eligibleForPmjay;

    @Column(name = "other_type_of_house")
    private String otherTypeOfHouse;

    @Column(name = "ration_card_color")
    private String rationCardColor;

    @Column(name = "residence_status")
    private String residenceStatus;

    @Column(name = "native_state")
    private String nativeState;

    @Column(name = "vehicle_availability_flag")
    private Boolean vehicleAvailabilityFlag;

    @Column(name = "pmjay_or_health_insurance")
    private String pmjayOrHealthInsurance;

    @Column(name = "anyone_travelled_foreign")
    private Boolean anyoneTravelledForeign;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public Boolean getBplFlag() {
        return bplFlag;
    }

    public void setBplFlag(Boolean bplFlag) {
        this.bplFlag = bplFlag;
    }

    public Integer getAnganwadiId() {
        return anganwadiId;
    }

    public void setAnganwadiId(Integer anganwadiId) {
        this.anganwadiId = anganwadiId;
    }

    public Boolean getToiletAvailableFlag() {
        return toiletAvailableFlag;
    }

    public void setToiletAvailableFlag(Boolean toiletAvailableFlag) {
        this.toiletAvailableFlag = toiletAvailableFlag;
    }

    public Boolean getIsVerifiedFlag() {
        return isVerifiedFlag;
    }

    public void setIsVerifiedFlag(Boolean isVerifiedFlag) {
        this.isVerifiedFlag = isVerifiedFlag;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getMaaVatsalyaNumber() {
        return maaVatsalyaNumber;
    }

    public void setMaaVatsalyaNumber(String maaVatsalyaNumber) {
        this.maaVatsalyaNumber = maaVatsalyaNumber;
    }

    public String getPmjayCardNumber() {
        return pmjayCardNumber;
    }

    public void setPmjayCardNumber(String pmjayCardNumber) {
        this.pmjayCardNumber = pmjayCardNumber;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Boolean getVulnerableFlag() {
        return vulnerableFlag;
    }

    public void setVulnerableFlag(Boolean vulnerableFlag) {
        this.vulnerableFlag = vulnerableFlag;
    }

    public Boolean getMigratoryFlag() {
        return migratoryFlag;
    }

    public void setMigratoryFlag(Boolean migratoryFlag) {
        this.migratoryFlag = migratoryFlag;
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

    public String getRsbyCardNumber() {
        return rsbyCardNumber;
    }

    public void setRsbyCardNumber(String rsbyCardNumber) {
        this.rsbyCardNumber = rsbyCardNumber;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getCurrentState() {
        return currentState;
    }

    public FamilyStateDetailEntity getCurrentStateDetail() {
        return currentStateDetail;
    }

    public void setCurrentStateDetail(FamilyStateDetailEntity currentStateDetail) {
        this.currentStateDetail = currentStateDetail;
    }

    public Boolean getIsReport() {
        return isReport;
    }

    public void setIsReport(Boolean isReport) {
        this.isReport = isReport;
    }

    public Integer getContactPersonId() {
        return contactPersonId;
    }

    public void setContactPersonId(Integer contactPersonId) {
        this.contactPersonId = contactPersonId;
    }

    public String getMergedWith() {
        return mergedWith;
    }

    public void setMergedWith(String mergedWith) {
        this.mergedWith = mergedWith;
    }

    public Integer getHeadOfFamily() {
        return headOfFamily;
    }

    public void setHeadOfFamily(Integer headOfFamily) {
        this.headOfFamily = headOfFamily;
    }

    public Boolean getAnganwadiUpdateFlag() {
        return anganwadiUpdateFlag;
    }

    public void setAnganwadiUpdateFlag(Boolean anganwadiUpdateFlag) {
        this.anganwadiUpdateFlag = anganwadiUpdateFlag;
    }

    public Boolean getAnyMemberCbacDone() {
        return anyMemberCbacDone;
    }

    public void setAnyMemberCbacDone(Boolean anyMemberCbacDone) {
        this.anyMemberCbacDone = anyMemberCbacDone;
    }

    public String getTypeOfHouse() {
        return typeOfHouse;
    }

    public void setTypeOfHouse(String typeOfHouse) {
        this.typeOfHouse = typeOfHouse;
    }

    public String getTypeOfToilet() {
        return typeOfToilet;
    }

    public void setTypeOfToilet(String typeOfToilet) {
        this.typeOfToilet = typeOfToilet;
    }

    public String getOtherToilet() {
        return otherToilet;
    }

    public void setOtherToilet(String otherToilet) {
        this.otherToilet = otherToilet;
    }

    public String getElectricityAvailability() {
        return electricityAvailability;
    }

    public void setElectricityAvailability(String electricityAvailability) {
        this.electricityAvailability = electricityAvailability;
    }

    public String getDrinkingWaterSource() {
        return drinkingWaterSource;
    }

    public void setDrinkingWaterSource(String drinkingWaterSource) {
        this.drinkingWaterSource = drinkingWaterSource;
    }

    public String getOtherWaterSource() {
        return otherWaterSource;
    }

    public void setOtherWaterSource(String otherWaterSource) {
        this.otherWaterSource = otherWaterSource;
    }

    public String getFuelForCooking() {
        return fuelForCooking;
    }

    public void setFuelForCooking(String fuelForCooking) {
        this.fuelForCooking = fuelForCooking;
    }

    public Set<String> getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(Set<String> vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    public String getHouseOwnershipStatus() {
        return houseOwnershipStatus;
    }

    public void setHouseOwnershipStatus(String houseOwnershipStatus) {
        this.houseOwnershipStatus = houseOwnershipStatus;
    }

    public String getRationCardNumber() {
        return rationCardNumber;
    }

    public void setRationCardNumber(String rationCardNumber) {
        this.rationCardNumber = rationCardNumber;
    }

    public String getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(String annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Integer getSplitFrom() {
        return splitFrom;
    }

    public void setSplitFrom(Integer splitFrom) {
        this.splitFrom = splitFrom;
    }

    public String getBplCardNumber() {
        return bplCardNumber;
    }

    public void setBplCardNumber(String bplCardNumber) {
        this.bplCardNumber = bplCardNumber;
    }

    public Boolean getProvidingConsent() {
        return isProvidingConsent;
    }

    public void setProvidingConsent(Boolean providingConsent) {
        isProvidingConsent = providingConsent;
    }

    public String getVulnerabilityCriteria() {
        return vulnerabilityCriteria;
    }

    public void setVulnerabilityCriteria(String vulnerabilityCriteria) {
        this.vulnerabilityCriteria = vulnerabilityCriteria;
    }

    public Boolean getEligibleForPmjay() {
        return eligibleForPmjay;
    }

    public void setEligibleForPmjay(Boolean eligibleForPmjay) {
        this.eligibleForPmjay = eligibleForPmjay;
    }

    public String getOtherTypeOfHouse() {
        return otherTypeOfHouse;
    }

    public void setOtherTypeOfHouse(String otherTypeOfHouse) {
        this.otherTypeOfHouse = otherTypeOfHouse;
    }

    public String getRationCardColor() {
        return rationCardColor;
    }

    public void setRationCardColor(String rationCardColor) {
        this.rationCardColor = rationCardColor;
    }

    public String getResidenceStatus() {
        return residenceStatus;
    }

    public void setResidenceStatus(String residenceStatus) {
        this.residenceStatus = residenceStatus;
    }

    public String getNativeState() {
        return nativeState;
    }

    public void setNativeState(String nativeState) {
        this.nativeState = nativeState;
    }

    public Boolean getVehicleAvailabilityFlag() {
        return vehicleAvailabilityFlag;
    }

    public void setVehicleAvailabilityFlag(Boolean vehicleAvailabilityFlag) {
        this.vehicleAvailabilityFlag = vehicleAvailabilityFlag;
    }

    public String getPmjayOrHealthInsurance() {
        return pmjayOrHealthInsurance;
    }

    public void setPmjayOrHealthInsurance(String pmjayOrHealthInsurance) {
        this.pmjayOrHealthInsurance = pmjayOrHealthInsurance;
    }

    public String getOtherMotorizedVehicle() {
        return otherMotorizedVehicle;
    }

    public void setOtherMotorizedVehicle(String otherMotorizedVehicle) {
        this.otherMotorizedVehicle = otherMotorizedVehicle;
    }

    public Boolean getAnyoneTravelledForeign() {
        return anyoneTravelledForeign;
    }

    public void setAnyoneTravelledForeign(Boolean anyoneTravelledForeign) {
        this.anyoneTravelledForeign = anyoneTravelledForeign;
    }

    public static class Fields {

        private Fields() {
            throw new IllegalStateException("Utility Class");
        }

        public static final String ID = "id";
        public static final String FAMILY_ID = "familyId";
        public static final String LOCATION_ID = "locationId";
        public static final String IS_VERIFIED_FLAG = "isVerifiedFlag";
        public static final String STATE = "state";
        public static final String ASSIGNED_TO = "assignedTo";
        public static final String AREA_ID = "areaId";
        public static final String CURRENT_STATE_DETAIL = "currentStateDetail";
        public static final String MEMBER_DETAILS = "memberDetails";
        public static final String REMARKS = "remarks";
        public static final String USER_ID = "userId";
        public static final String STATES = "states";
        public static final String MEMBER_VERIFIED = "memberVerified";
    }

    public enum FamilyEntityJoin implements IJoinEnum {

        MEMBER_DETAILS(Fields.MEMBER_DETAILS, Fields.MEMBER_DETAILS, JoinType.INNER),
        CURRENT_STATE_DETAIL(Fields.CURRENT_STATE_DETAIL, Fields.CURRENT_STATE_DETAIL, JoinType.INNER),
        CHRONIC_DISEASE_DETAILS(Fields.MEMBER_DETAILS + "." + MemberEntity.Fields.CHRONIC_DISEASE_DETAILS, MemberEntity.Fields.CHRONIC_DISEASE_DETAILS, JoinType.LEFT),
        CONGENITAL_ANOMALY_DETAILS(Fields.MEMBER_DETAILS + "." + MemberEntity.Fields.CONGENITAL_ANOMALY_DETAILS, MemberEntity.Fields.CONGENITAL_ANOMALY_DETAILS, JoinType.LEFT),
        CURRENT_DISEASE_DETAILS(Fields.MEMBER_DETAILS + "." + MemberEntity.Fields.CURRENT_DISEASE_DETAILS, MemberEntity.Fields.CURRENT_DISEASE_DETAILS, JoinType.LEFT),
        EYE_ISSUE_DETAILS(Fields.MEMBER_DETAILS + "." + MemberEntity.Fields.EYE_ISSUE_DETAILS, MemberEntity.Fields.EYE_ISSUE_DETAILS, JoinType.LEFT),
        CURRENT_MEMBER_STATE_DETAIL(Fields.MEMBER_DETAILS + "." + MemberEntity.Fields.CURRENT_STATE_DETAIL, "member" + MemberEntity.Fields.CURRENT_STATE_DETAIL, JoinType.INNER);

        private String value;
        private String alias;
        private JoinType joinType;

        public String getValue() {
            return value;
        }

        public String getAlias() {
            return alias;
        }

        public JoinType getJoinType() {
            return joinType;
        }

        FamilyEntityJoin(String value, String alias, JoinType joinType) {
            this.value = value;
            this.alias = alias;
            this.joinType = joinType;
        }
    }

    @Override
    public String toString() {
        return "FamilyEntity{" + "id=" + id + ", familyId=" + familyId + ", houseNumber=" + houseNumber + ", locationId=" + locationId + ", religion=" + religion + ", caste=" + caste + ", bplFlag=" + bplFlag + ", anganwadiId=" + anganwadiId + ", toiletAvailableFlag=" + toiletAvailableFlag + ", isVerifiedFlag=" + isVerifiedFlag + ", state=" + state + ", assignedTo=" + assignedTo + ", address1=" + address1 + ", address2=" + address2 + ", maaVatsalyaNumber=" + maaVatsalyaNumber + ", areaId=" + areaId + ", vulnerableFlag=" + vulnerableFlag + ", migratoryFlag=" + migratoryFlag + ", latitude=" + latitude + ", longitude=" + longitude + ", rsbyCardNumber=" + rsbyCardNumber + ", comment=" + comment + ", currentState=" + currentState + ", isReport=" + isReport + ", contactPersonId=" + contactPersonId + ", currentStateDetail=" + currentStateDetail + '}';
    }

}
