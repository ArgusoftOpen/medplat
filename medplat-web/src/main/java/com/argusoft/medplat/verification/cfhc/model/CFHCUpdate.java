
package com.argusoft.medplat.verification.cfhc.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 *     Defines fields for child malnutrition treatment center
 * </p>
 * @author raj
 * @since 09/09/2020 12:30
 */
@Entity
@Table(name = "cfhc_mo_verification_status")
public class CFHCUpdate extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberid;

    @Column(name = "family_id")
    private String familyid;

    @Column(name = "location_id")
    private Integer locationid;

    @Column(name = "comment")
    private String comment;

    @Column(name = "state")
    private String verificationState;

    @Column(name = "family_verification_id")
    private Integer familyVerificationId;

    @Column(name = "verification_body")
    private String verificationBody;

    @Column(name = "first_name_status")
    private String firstNameStatus;

    @Column(name = "middle_name_status")
    private String middleNameStatus;

    @Column(name = "last_name_status")
    private String lastNameStatus;

    @Column(name = "aadhar_name_status")
    private String aadharNameStatus;

    @Column(name = "dob_status")
    private String dobStatus;

    @Column(name = "pregnancy_status")
    private String pregnancyStatus;

    @Column(name = "relationship_with_hof_status")
    private String relationshipStatus;

    @Column(name = "fp_method_status")
    private String fpMethodStatus;

    @Column(name = "chronic_disease_status")
    private String chronicDiseaseStatus;

    @Column(name = "contact_status")
    private String contactnoStatus;

    @Column(name = "dead_status")
    private String deadStatus;

    @Column(name = "migrated_status")
    private String migratedStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberid() {
        return memberid;
    }

    public void setMemberid(Integer memberid) {
        this.memberid = memberid;
    }

    public String getFamilyid() {
        return familyid;
    }

    public void setFamilyid(String familyid) {
        this.familyid = familyid;
    }

    public Integer getLocationid() {
        return locationid;
    }

    public void setLocationid(Integer locationid) {
        this.locationid = locationid;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public String getFpMethodStatus() {
        return fpMethodStatus;
    }

    public void setFpMethodStatus(String fpMethodStatus) {
        this.fpMethodStatus = fpMethodStatus;
    }

    public String getChronicDiseaseStatus() {
        return chronicDiseaseStatus;
    }

    public void setChronicDiseaseStatus(String chronicDiseaseStatus) {
        this.chronicDiseaseStatus = chronicDiseaseStatus;
    }

    public String getContactnoStatus() {
        return contactnoStatus;
    }

    public void setContactnoStatus(String contactnoStatus) {
        this.contactnoStatus = contactnoStatus;
    }

    public String getDeadStatus() {
        return deadStatus;
    }

    public void setDeadStatus(String deadStatus) {
        this.deadStatus = deadStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVerificationState() {
        return verificationState;
    }

    public void setVerificationState(String verificationState) {
        this.verificationState = verificationState;
    }

    public Integer getFamilyVerificationId() {
        return familyVerificationId;
    }

    public void setFamilyVerificationId(Integer familyVerificationId) {
        this.familyVerificationId = familyVerificationId;
    }

    public String getVerificationBody() {
        return verificationBody;
    }

    public void setVerificationBody(String verificationBody) {
        this.verificationBody = verificationBody;
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

    public String getDobStatus() {
        return dobStatus;
    }

    public String getAadharNameStatus() {
        return aadharNameStatus;
    }

    public void setAadharNameStatus(String aadharNameStatus) {
        this.aadharNameStatus = aadharNameStatus;
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
