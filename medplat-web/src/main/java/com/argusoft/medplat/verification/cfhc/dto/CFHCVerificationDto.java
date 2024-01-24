
package com.argusoft.medplat.verification.cfhc.dto;

import java.util.List;

/**
 * <p>
 *     Defines fields for child malnutrition treatment center verification
 * </p>
 * @author raj
 * @since 09/09/2020 12:30
 */
public class CFHCVerificationDto {

    private String familyid;

    private String familyheadmembername;

    private String contactno;

    private String comment;

    private String membername;

    private Integer memberid;

    private Boolean smsVerified;

    private String relationship;

    private Integer locationid;

    private String chronicDisease;

    private String fpMethod;

    private Boolean marriedWomen;

    private String fulllocation;

    private Boolean familyVerified;

    private String uniqueHealthid;

    private List<String> states;

    private Boolean isMobileNumberVerified;
    
    private Integer familyVerificationId;
    
    private String verificationBody;
    
    public List<String> getStates() {
        return states;
    }

    public void setStates(List<String> states) {
        this.states = states;
    }

    public String getUniqueHealthid() {
        return uniqueHealthid;
    }

    public void setUniqueHealthid(String uniqueHealthid) {
        this.uniqueHealthid = uniqueHealthid;
    }

    public Boolean getFamilyVerified() {
        return familyVerified;
    }

    public void setFamilyVerified(Boolean familyVerified) {
        this.familyVerified = familyVerified;
    }

    public String getFulllocation() {
        return fulllocation;
    }

    public void setFulllocation(String fulllocation) {
        this.fulllocation = fulllocation;
    }

    public Boolean getMarriedWomen() {
        return marriedWomen;
    }

    public void setMarriedWomen(Boolean marriedWomen) {
        this.marriedWomen = marriedWomen;
    }

    public String getFpMethod() {
        return fpMethod;
    }

    public void setFpMethod(String fpMethod) {
        this.fpMethod = fpMethod;
    }

    public String getChronicDisease() {
        return chronicDisease;
    }

    public void setChronicDisease(String chronicDisease) {
        this.chronicDisease = chronicDisease;
    }

    public String getFamilyid() {
        return familyid;
    }

    public void setFamilyid(String familyid) {
        this.familyid = familyid;
    }

    public String getFamilyheadmembername() {
        return familyheadmembername;
    }

    public void setFamilyheadmembername(String familyheadmembername) {
        this.familyheadmembername = familyheadmembername;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public Integer getMemberid() {
        return memberid;
    }

    public void setMemberid(Integer memberid) {
        this.memberid = memberid;
    }

    public Boolean getSmsVerified() {
        return smsVerified;
    }

    public void setSmsVerified(Boolean smsVerified) {
        this.smsVerified = smsVerified;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public Integer getLocationid() {
        return locationid;
    }

    public void setLocationid(Integer locationid) {
        this.locationid = locationid;
    }
    
    public Boolean getIsMobileNumberVerified() {
        return isMobileNumberVerified;
    }

    public void setIsMobileNumberVerified(Boolean isMobileNumberVerified) {
        this.isMobileNumberVerified = isMobileNumberVerified;
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

}
