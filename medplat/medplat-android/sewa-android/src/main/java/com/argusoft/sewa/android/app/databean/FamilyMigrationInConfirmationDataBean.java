package com.argusoft.sewa.android.app.databean;

/**
 * Created by prateek on 8/21/19
 */
public class FamilyMigrationInConfirmationDataBean {

    private Long notificationId;
    private Long migrationId;
    private Long familyId;
    private Boolean hasMigrationHappened;
    private Long locationMigratedTo;
    private Long areaMigratedTo;

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

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

    public Boolean getHasMigrationHappened() {
        return hasMigrationHappened;
    }

    public void setHasMigrationHappened(Boolean hasMigrationHappened) {
        this.hasMigrationHappened = hasMigrationHappened;
    }

    public Long getLocationMigratedTo() {
        return locationMigratedTo;
    }

    public void setLocationMigratedTo(Long locationMigratedTo) {
        this.locationMigratedTo = locationMigratedTo;
    }

    public Long getAreaMigratedTo() {
        return areaMigratedTo;
    }

    public void setAreaMigratedTo(Long areaMigratedTo) {
        this.areaMigratedTo = areaMigratedTo;
    }
}
