package com.argusoft.sewa.android.app.databean;

/**
 * Created by prateek on 18/3/19.
 */

public class MigrationInConfirmationDataBean {

    private Long notificationId;
    private Long migrationId;
    private Long memberId;
    private Boolean hasMigrationHappened;
    private Long familyMigratedTo;

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

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Boolean getHasMigrationHappened() {
        return hasMigrationHappened;
    }

    public void setHasMigrationHappened(Boolean hasMigrationHappened) {
        this.hasMigrationHappened = hasMigrationHappened;
    }

    public Long getFamilyMigratedTo() {
        return familyMigratedTo;
    }

    public void setFamilyMigratedTo(Long familyMigratedTo) {
        this.familyMigratedTo = familyMigratedTo;
    }
}
