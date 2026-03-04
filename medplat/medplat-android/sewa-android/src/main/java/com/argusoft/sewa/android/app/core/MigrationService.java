package com.argusoft.sewa.android.app.core;

import com.argusoft.sewa.android.app.databean.FamilyDataBean;
import com.argusoft.sewa.android.app.databean.FamilyMigrationDetailsDataBean;
import com.argusoft.sewa.android.app.databean.FamilyMigrationInConfirmationDataBean;
import com.argusoft.sewa.android.app.databean.FamilyMigrationOutDataBean;
import com.argusoft.sewa.android.app.databean.MemberDataBean;
import com.argusoft.sewa.android.app.databean.MigratedFamilyDataBean;
import com.argusoft.sewa.android.app.databean.MigratedMembersDataBean;
import com.argusoft.sewa.android.app.databean.MigrationDetailsDataBean;
import com.argusoft.sewa.android.app.databean.MigrationInConfirmationDataBean;
import com.argusoft.sewa.android.app.databean.MigrationInDataBean;
import com.argusoft.sewa.android.app.databean.MigrationOutConfirmationDataBean;
import com.argusoft.sewa.android.app.databean.MigrationOutDataBean;
import com.argusoft.sewa.android.app.databean.NotificationMobDataBean;
import com.argusoft.sewa.android.app.model.LocationMasterBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.model.MigratedFamilyBean;
import com.argusoft.sewa.android.app.model.MigratedMembersBean;
import com.argusoft.sewa.android.app.model.NotificationBean;

import java.util.List;
import java.util.Map;

/**
 * Created by prateek on 18/3/19.
 */

public interface MigrationService {

    List<LocationMasterBean> retrieveLocationMasterBeansBySearch(CharSequence s, Integer level);

    Map<Integer, String> retrieveHierarchyOfVillage(LocationMasterBean locationMasterBean);

    List<MemberBean> retrieveChildrenUnder5YearsByMotherId(Long motherId);

    List<FamilyDataBean> retrieveFamilyListBySearchForMigration(String search);

    List<FamilyDataBean> searchFamilyByLocation(String search, Long locationId, long limit, long offset);

    void createMigrationOut(MigrationOutDataBean migration, MemberDataBean memberDataBean, NotificationBean notificationBean);

    void createMigrationIn(MigrationInDataBean migration);

    void createMigrationInConfirmation(MigrationInConfirmationDataBean migrationInConfirmation, MigrationDetailsDataBean migrationDetailsDataBean);

    void createMigrationOutConfirmation(MigrationOutConfirmationDataBean migrationOutConfirmation, MigrationDetailsDataBean migrationDetailsDataBean);

    List<MigratedMembersBean> retrieveMigrationDetailsForMigratedMembers(Integer locationId, List<Integer> selectedAshaAreas, boolean isOut, long limit, long offset);

    void storeRevertedMigrationBean(MigratedMembersDataBean migratedMembersBean);

    void createFamilyMigrationOut(FamilyMigrationOutDataBean migration, FamilyDataBean family, NotificationMobDataBean notificationMobDataBean);

    void createFamilyMigrationInConfirmation(FamilyMigrationInConfirmationDataBean confirmationDataBean, FamilyMigrationDetailsDataBean detailsDataBean);

    void deleteMigratedMembersBean(MigratedMembersBean migratedMembersBean);

    List<MigratedFamilyBean> retrieveMigrationDetailsForMigratedFamily(Integer locationId, List<Integer> selectedAshaAreas, boolean isOut, long limit, long offset);

    void storeRevertedFamilyMigrationBean(MigratedFamilyDataBean migratedFamilyDataBean);

    void deleteMigratedFamilyBean(MigratedFamilyBean migratedFamilyBean);
}
