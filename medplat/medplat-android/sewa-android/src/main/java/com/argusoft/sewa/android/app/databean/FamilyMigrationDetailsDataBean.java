package com.argusoft.sewa.android.app.databean;

import java.util.List;

/**
 * Created by prateek on 8/20/19
 */
public class FamilyMigrationDetailsDataBean {

    private Long migrationId;
    private Long familyId;
    private String familyIdString;
    private List<String> memberDetails;
    private String locationDetails;
    private String areaDetails;
    private String fhwDetails;
    private String otherInfo;

    public Long getMigrationId() {
        return migrationId;
    }

    public void setMigrationId(Long migrationId) {
        this.migrationId = migrationId;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public String getFamilyIdString() {
        return familyIdString;
    }

    public void setFamilyIdString(String familyIdString) {
        this.familyIdString = familyIdString;
    }

    public List<String> getMemberDetails() {
        return memberDetails;
    }

    public void setMemberDetails(List<String> memberDetails) {
        this.memberDetails = memberDetails;
    }

    public String getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }

    public String getAreaDetails() {
        return areaDetails;
    }

    public void setAreaDetails(String areaDetails) {
        this.areaDetails = areaDetails;
    }

    public String getFhwDetails() {
        return fhwDetails;
    }

    public void setFhwDetails(String fhwDetails) {
        this.fhwDetails = fhwDetails;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }
}
