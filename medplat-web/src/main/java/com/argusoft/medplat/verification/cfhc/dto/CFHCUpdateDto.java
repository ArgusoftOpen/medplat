
package com.argusoft.medplat.verification.cfhc.dto;

import java.util.List;

/**
 * <p>
 *     Defines fields for child malnutrition treatment center update
 * </p>
 *  @author raj
 * @since 09/09/2020 12:30
 */
public class CFHCUpdateDto {

    private String familyid;

    private String comment;

    private Integer memberid;

    private Integer locationid;

    private String verificationStatus;

    private Integer familyVerificationId;

    private String verificationBody;

    private String firstNameStatus;

    private String middleNameStatus;

    private String lastNameStatus;

    private String dobStatus;

    private String pregnancyStatus;

    private String fpMethodStatus;

    private String relationshipStatus;

    private String chronicDiseaseStatus;

    private String deadStatus;

    private String migratedStatus;

    private String contactnoStatus;

    private List<String> notVerified;

    public String getFamilyid() {
        return familyid;
    }

    public void setFamilyid(String familyid) {
        this.familyid = familyid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getMemberid() {
        return memberid;
    }

    public void setMemberid(Integer memberid) {
        this.memberid = memberid;
    }

    public Integer getLocationid() {
        return locationid;
    }

    public void setLocationid(Integer locationid) {
        this.locationid = locationid;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getFpMethodStatus() {
        return fpMethodStatus;
    }

    public void setFpMethodStatus(String fpMethodStatus) {
        this.fpMethodStatus = fpMethodStatus;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public String getChronicDiseaseStatus() {
        return chronicDiseaseStatus;
    }

    public void setChronicDiseaseStatus(String chronicDiseaseStatus) {
        this.chronicDiseaseStatus = chronicDiseaseStatus;
    }

    public String getDeadStatus() {
        return deadStatus;
    }

    public void setDeadStatus(String deadStatus) {
        this.deadStatus = deadStatus;
    }

    public String getContactnoStatus() {
        return contactnoStatus;
    }

    public void setContactnoStatus(String contactnoStatus) {
        this.contactnoStatus = contactnoStatus;
    }

    public List<String> getNotVerified() {
        return notVerified;
    }

    public void setNotVerified(List<String> notVerified) {
        this.notVerified = notVerified;
    }

    public Integer getFamilyVerificationId() {
        return familyVerificationId;
    }

    public void setFamilyVerificationId(Integer familyVerificationId) {
        this.familyVerificationId = familyVerificationId;
    }

    public String getFirstNameStatus() {
        return firstNameStatus;
    }

    public void setFirstNameStatus(String firstNameStatus) {
        this.firstNameStatus = firstNameStatus;
    }

    public String getMiddleNameStatus() {
        return middleNameStatus;
    }

    public void setMiddleNameStatus(String middleNameStatus) {
        this.middleNameStatus = middleNameStatus;
    }

    public String getLastNameStatus() {
        return lastNameStatus;
    }

    public void setLastNameStatus(String lastNameStatus) {
        this.lastNameStatus = lastNameStatus;
    }

    public String getVerificationBody() {
        return verificationBody;
    }

    public void setVerificationBody(String verificationBody) {
        this.verificationBody = verificationBody;
    }

    public String getDobStatus() {
        return dobStatus;
    }

    public void setDobStatus(String dobStatus) {
        this.dobStatus = dobStatus;
    }

    public String getPregnancyStatus() {
        return pregnancyStatus;
    }

    public void setPregnancyStatus(String pregnancyStatus) {
        this.pregnancyStatus = pregnancyStatus;
    }

    public String getMigratedStatus() {
        return migratedStatus;
    }

    public void setMigratedStatus(String migratedStatus) {
        this.migratedStatus = migratedStatus;
    }
}
