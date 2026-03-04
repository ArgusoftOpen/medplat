/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Used for wpd master.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 11:00 AM
 */
public class WpdMasterDto {

    private Integer id;
    private Integer memberId;
    private String contactNumber;
    private String uniqueHealthId;
    private String name;
    private Integer familyId;
    private String latitude;
    private String longitude;
    private Date startDate;
    private Date endDate;
    private Integer locationId;
    private Date deliveryDate;
    private String memberStatus;
    private String deliveryPlace;
    private Integer typeOfHospital;
    private String deliveryDoneBy;
    private Boolean isMotherAlive;
    private String typeOfDelivery;
    private String pregnancyOutcome;
    private Integer motherReferralPlace;
    private Boolean isDischarged;
    private Date dischargeDate;
    private Boolean breastFeeding;
    private Date deathDate;
    private String deathReason;
    private String deathPlace;
    private Boolean isCorticoSteroidGiven;
    private Integer mtpPlace;
    private String mtpPerformedBy;
    private Boolean isDeliveryDone;
    private String motherOtherDangerSign;
    private Set<Integer> motherDangerSigns;
    private Set<Integer> highRiskSymptomsDuringDelivery;
    private Set<Integer> treatmentsDuringDelivery;
    private List<WpdChildDto> childDetails;
    private Boolean wasMisoProstolGiven;
    private String freeDropDelivery;
    private Boolean isHighRiskCase;
    private String accountNumber;
    private String ifsc;
    private Integer deliveryPerson;
    private String deliveryPersonName;
    private Integer healthInfrastructureId;
    private Integer pregRegDetId;
    private Boolean isFromWeb;
    private Boolean freeMedicines;
    private Boolean freeDiet;
    private Boolean freeLabTest;
    private Boolean freeBloodTransfusion;
    private Boolean freeDropTransport;
    private String familyPlanningMethod;
    private Boolean fbmdsr;
    private Integer motherReferralInfraId;
    private String childName;
    private Integer childId;
    private Integer childMemberId;
    private String givenChildImmuns;
    private Integer deathInfrastructureId;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public String getDeliveryPlace() {
        return deliveryPlace;
    }

    public void setDeliveryPlace(String deliveryPlace) {
        this.deliveryPlace = deliveryPlace;
    }

    public Integer getTypeOfHospital() {
        return typeOfHospital;
    }

    public void setTypeOfHospital(Integer typeOfHospital) {
        this.typeOfHospital = typeOfHospital;
    }

    public String getDeliveryDoneBy() {
        return deliveryDoneBy;
    }

    public void setDeliveryDoneBy(String deliveryDoneBy) {
        this.deliveryDoneBy = deliveryDoneBy;
    }

    public Boolean getIsMotherAlive() {
        return isMotherAlive;
    }

    public void setIsMotherAlive(Boolean isMotherAlive) {
        this.isMotherAlive = isMotherAlive;
    }

    public String getTypeOfDelivery() {
        return typeOfDelivery;
    }

    public void setTypeOfDelivery(String typeOfDelivery) {
        this.typeOfDelivery = typeOfDelivery;
    }

    public Integer getMotherReferralPlace() {
        return motherReferralPlace;
    }

    public void setMotherReferralPlace(Integer motherReferralPlace) {
        this.motherReferralPlace = motherReferralPlace;
    }

    public Date getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(Date dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public Boolean getBreastFeeding() {
        return breastFeeding;
    }

    public void setBreastFeeding(Boolean breastFeeding) {
        this.breastFeeding = breastFeeding;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public String getDeathReason() {
        return deathReason;
    }

    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }

    public String getDeathPlace() {
        return deathPlace;
    }

    public void setDeathPlace(String deathPlace) {
        this.deathPlace = deathPlace;
    }

    public Boolean getIsCorticoSteroidGiven() {
        return isCorticoSteroidGiven;
    }

    public void setIsCorticoSteroidGiven(Boolean isCorticoSteroidGiven) {
        this.isCorticoSteroidGiven = isCorticoSteroidGiven;
    }

    public Integer getMtpPlace() {
        return mtpPlace;
    }

    public void setMtpPlace(Integer mtpPlace) {
        this.mtpPlace = mtpPlace;
    }

    public String getMtpPerformedBy() {
        return mtpPerformedBy;
    }

    public void setMtpPerformedBy(String mtpPerformedBy) {
        this.mtpPerformedBy = mtpPerformedBy;
    }

    public Boolean getIsDeliveryDone() {
        return isDeliveryDone;
    }

    public void setIsDeliveryDone(Boolean isDeliveryDone) {
        this.isDeliveryDone = isDeliveryDone;
    }

    public String getMotherOtherDangerSign() {
        return motherOtherDangerSign;
    }

    public void setMotherOtherDangerSign(String motherOtherDangerSign) {
        this.motherOtherDangerSign = motherOtherDangerSign;
    }

    public Set<Integer> getMotherDangerSigns() {
        return motherDangerSigns;
    }

    public void setMotherDangerSigns(Set<Integer> motherDangerSigns) {
        this.motherDangerSigns = motherDangerSigns;
    }

    public Set<Integer> getHighRiskSymptomsDuringDelivery() {
        return highRiskSymptomsDuringDelivery;
    }

    public void setHighRiskSymptomsDuringDelivery(Set<Integer> highRiskSymptomsDuringDelivery) {
        this.highRiskSymptomsDuringDelivery = highRiskSymptomsDuringDelivery;
    }

    public Set<Integer> getTreatmentsDuringDelivery() {
        return treatmentsDuringDelivery;
    }

    public void setTreatmentsDuringDelivery(Set<Integer> treatmentsDuringDelivery) {
        this.treatmentsDuringDelivery = treatmentsDuringDelivery;
    }

    public List<WpdChildDto> getChildDetails() {
        return childDetails;
    }

    public void setChildDetails(List<WpdChildDto> childDetails) {
        this.childDetails = childDetails;
    }

    public Boolean getWasMisoProstolGiven() {
        return wasMisoProstolGiven;
    }

    public void setWasMisoProstolGiven(Boolean wasMisoProstolGiven) {
        this.wasMisoProstolGiven = wasMisoProstolGiven;
    }

    public String getFreeDropDelivery() {
        return freeDropDelivery;
    }

    public void setFreeDropDelivery(String freeDropDelivery) {
        this.freeDropDelivery = freeDropDelivery;
    }

    public Boolean getIsHighRiskCase() {
        return isHighRiskCase;
    }

    public void setIsHighRiskCase(Boolean isHighRiskCase) {
        this.isHighRiskCase = isHighRiskCase;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public Integer getDeliveryPerson() {
        return deliveryPerson;
    }

    public void setDeliveryPerson(Integer deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }

    public Integer getHealthInfrastructureId() {
        return healthInfrastructureId;
    }

    public void setHealthInfrastructureId(Integer healthInfrastructureId) {
        this.healthInfrastructureId = healthInfrastructureId;
    }

    public Boolean getIsDischarged() {
        return isDischarged;
    }

    public void setIsDischarged(Boolean isDischarged) {
        this.isDischarged = isDischarged;
    }

    public String getUniqueHealthId() {
        return uniqueHealthId;
    }

    public void setUniqueHealthId(String uniqueHealthId) {
        this.uniqueHealthId = uniqueHealthId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeliveryPersonName() {
        return deliveryPersonName;
    }

    public void setDeliveryPersonName(String deliveryPersonName) {
        this.deliveryPersonName = deliveryPersonName.replace("\0", "");
    }

    public Integer getPregRegDetId() {
        return pregRegDetId;
    }

    public void setPregRegDetId(Integer pregRegDetId) {
        this.pregRegDetId = pregRegDetId;
    }

    public Boolean getIsFromWeb() {
        return isFromWeb;
    }

    public void setIsFromWeb(Boolean isFromWeb) {
        this.isFromWeb = isFromWeb;
    }

    public Boolean getFreeMedicines() {
        return freeMedicines;
    }

    public void setFreeMedicines(Boolean freeMedicines) {
        this.freeMedicines = freeMedicines;
    }

    public Boolean getFreeDiet() {
        return freeDiet;
    }

    public void setFreeDiet(Boolean freeDiet) {
        this.freeDiet = freeDiet;
    }

    public Boolean getFreeLabTest() {
        return freeLabTest;
    }

    public void setFreeLabTest(Boolean freeLabTest) {
        this.freeLabTest = freeLabTest;
    }

    public Boolean getFreeBloodTransfusion() {
        return freeBloodTransfusion;
    }

    public void setFreeBloodTransfusion(Boolean freeBloodTransfusion) {
        this.freeBloodTransfusion = freeBloodTransfusion;
    }

    public Boolean getFreeDropTransport() {
        return freeDropTransport;
    }

    public void setFreeDropTransport(Boolean freeDropTransport) {
        this.freeDropTransport = freeDropTransport;
    }

    public String getFamilyPlanningMethod() {
        return familyPlanningMethod;
    }

    public void setFamilyPlanningMethod(String familyPlanningMethod) {
        this.familyPlanningMethod = familyPlanningMethod;
    }

    public Boolean getFbmdsr() {
        return fbmdsr;
    }

    public void setFbmdsr(Boolean fbmdsr) {
        this.fbmdsr = fbmdsr;
    }

    public Integer getMotherReferralInfraId() {
        return motherReferralInfraId;
    }

    public void setMotherReferralInfraId(Integer motherReferralInfraId) {
        this.motherReferralInfraId = motherReferralInfraId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public Integer getChildMemberId() {
        return childMemberId;
    }

    public void setChildMemberId(Integer childMemberId) {
        this.childMemberId = childMemberId;
    }

    public String getGivenChildImmuns() {
        return givenChildImmuns;
    }

    public void setGivenChildImmuns(String givenChildImmuns) {
        this.givenChildImmuns = givenChildImmuns;
    }

    public Integer getDeathInfrastructureId() {
        return deathInfrastructureId;
    }

    public void setDeathInfrastructureId(Integer deathInfrastructureId) {
        this.deathInfrastructureId = deathInfrastructureId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPregnancyOutcome() {
        return pregnancyOutcome;
    }

    public void setPregnancyOutcome(String pregnancyOutcome) {
        this.pregnancyOutcome = pregnancyOutcome;
    }

    @Override
    public String toString() {
        return "WpdMasterDto{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", contactNumber='" + contactNumber + '\'' +
                ", uniqueHealthId='" + uniqueHealthId + '\'' +
                ", name='" + name + '\'' +
                ", familyId=" + familyId +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", locationId=" + locationId +
                ", deliveryDate=" + deliveryDate +
                ", memberStatus='" + memberStatus + '\'' +
                ", deliveryPlace='" + deliveryPlace + '\'' +
                ", typeOfHospital=" + typeOfHospital +
                ", deliveryDoneBy='" + deliveryDoneBy + '\'' +
                ", isMotherAlive=" + isMotherAlive +
                ", typeOfDelivery='" + typeOfDelivery + '\'' +
                ", pregnancyOutcome='" + pregnancyOutcome + '\'' +
                ", motherReferralPlace=" + motherReferralPlace +
                ", isDischarged=" + isDischarged +
                ", dischargeDate=" + dischargeDate +
                ", breastFeeding=" + breastFeeding +
                ", deathDate=" + deathDate +
                ", deathReason='" + deathReason + '\'' +
                ", deathPlace='" + deathPlace + '\'' +
                ", isCorticoSteroidGiven=" + isCorticoSteroidGiven +
                ", mtpPlace=" + mtpPlace +
                ", mtpPerformedBy='" + mtpPerformedBy + '\'' +
                ", isDeliveryDone=" + isDeliveryDone +
                ", motherOtherDangerSign='" + motherOtherDangerSign + '\'' +
                ", motherDangerSigns=" + motherDangerSigns +
                ", highRiskSymptomsDuringDelivery=" + highRiskSymptomsDuringDelivery +
                ", treatmentsDuringDelivery=" + treatmentsDuringDelivery +
                ", childDetails=" + childDetails +
                ", wasMisoProstolGiven=" + wasMisoProstolGiven +
                ", freeDropDelivery='" + freeDropDelivery + '\'' +
                ", isHighRiskCase=" + isHighRiskCase +
                ", accountNumber='" + accountNumber + '\'' +
                ", ifsc='" + ifsc + '\'' +
                ", deliveryPerson=" + deliveryPerson +
                ", deliveryPersonName='" + deliveryPersonName + '\'' +
                ", healthInfrastructureId=" + healthInfrastructureId +
                ", pregRegDetId=" + pregRegDetId +
                ", isFromWeb=" + isFromWeb +
                ", freeMedicines=" + freeMedicines +
                ", freeDiet=" + freeDiet +
                ", freeLabTest=" + freeLabTest +
                ", freeBloodTransfusion=" + freeBloodTransfusion +
                ", freeDropTransport=" + freeDropTransport +
                ", familyPlanningMethod='" + familyPlanningMethod + '\'' +
                ", fbmdsr=" + fbmdsr +
                ", motherReferralInfraId=" + motherReferralInfraId +
                ", childName='" + childName + '\'' +
                ", childId=" + childId +
                ", childMemberId=" + childMemberId +
                ", givenChildImmuns='" + givenChildImmuns + '\'' +
                ", deathInfrastructureId=" + deathInfrastructureId +
                '}';
    }
}
