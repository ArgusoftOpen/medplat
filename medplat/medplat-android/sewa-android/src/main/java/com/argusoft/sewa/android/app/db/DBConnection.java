package com.argusoft.sewa.android.app.db;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import com.argusoft.sewa.android.app.BuildConfig;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyProcessDialog;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.exception.DataException;
import com.argusoft.sewa.android.app.model.AnnouncementBean;
import com.argusoft.sewa.android.app.model.AnswerBean;
import com.argusoft.sewa.android.app.model.BaseEntity;
import com.argusoft.sewa.android.app.model.CovidTravellersInfoBean;
import com.argusoft.sewa.android.app.model.DailyNutritionLogBean;
import com.argusoft.sewa.android.app.model.DailyPresenceReport;
import com.argusoft.sewa.android.app.model.DataQualityBean;
import com.argusoft.sewa.android.app.model.DrugInventoryBean;
import com.argusoft.sewa.android.app.model.FamilyBean;
import com.argusoft.sewa.android.app.model.FhwServiceDetailBean;
import com.argusoft.sewa.android.app.model.FileDownloadBean;
import com.argusoft.sewa.android.app.model.FormAccessibilityBean;
import com.argusoft.sewa.android.app.model.HealthInfrastructureBean;
import com.argusoft.sewa.android.app.model.LabelBean;
import com.argusoft.sewa.android.app.model.LibraryBean;
import com.argusoft.sewa.android.app.model.ListValueBean;
import com.argusoft.sewa.android.app.model.LmsBookMarkBean;
import com.argusoft.sewa.android.app.model.LmsCourseBean;
import com.argusoft.sewa.android.app.model.LmsEventBean;
import com.argusoft.sewa.android.app.model.LmsUserMetaDataBean;
import com.argusoft.sewa.android.app.model.LmsViewedMediaBean;
import com.argusoft.sewa.android.app.model.LocationBean;
import com.argusoft.sewa.android.app.model.LocationCoordinatesBean;
import com.argusoft.sewa.android.app.model.LocationMasterBean;
import com.argusoft.sewa.android.app.model.LocationTypeBean;
import com.argusoft.sewa.android.app.model.LoggerBean;
import com.argusoft.sewa.android.app.model.LoginBean;
import com.argusoft.sewa.android.app.model.MemberAvailableEveningBean;
import com.argusoft.sewa.android.app.model.MemberBean;
import com.argusoft.sewa.android.app.model.MemberCbacDetailBean;
import com.argusoft.sewa.android.app.model.MemberMoConfirmedDetailBean;
import com.argusoft.sewa.android.app.model.MemberPregnancyStatusBean;
import com.argusoft.sewa.android.app.model.MenuBean;
import com.argusoft.sewa.android.app.model.MergedFamiliesBean;
import com.argusoft.sewa.android.app.model.MigratedFamilyBean;
import com.argusoft.sewa.android.app.model.MigratedMembersBean;
import com.argusoft.sewa.android.app.model.NotificationBean;
import com.argusoft.sewa.android.app.model.QuestionBean;
import com.argusoft.sewa.android.app.model.ReadNotificationsBean;
import com.argusoft.sewa.android.app.model.SchoolBean;
import com.argusoft.sewa.android.app.model.StoreAnswerBean;
import com.argusoft.sewa.android.app.model.UncaughtExceptionBean;
import com.argusoft.sewa.android.app.model.UploadFileDataBean;
import com.argusoft.sewa.android.app.model.UserHealthInfraBean;
import com.argusoft.sewa.android.app.model.VersionBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author alpeshkyada
 */
public class DBConnection extends OrmLiteSqliteOpenHelper {

    MyProcessDialog processDialog;
    MyAlertDialog dialogForExit;

    public DBConnection(Context context) {
        super(context,
                SewaConstants.getDirectoryPath(context, SewaConstants.DIR_DATABASE) + GlobalTypes.PATH_SEPARATOR + BuildConfig.DB_NAME,
                null,
                BuildConfig.DATABASE_VERSION);
        Log.i(getClass().getSimpleName(), "DBConnection super constructor called");
    }

    private List<Class<? extends BaseEntity>> getDatabaseTableList() {
        Log.i(getClass().getSimpleName(), "Table List");
        return Arrays.asList(
                AnnouncementBean.class,
                AnswerBean.class,
                LabelBean.class,
                ListValueBean.class,
                LoggerBean.class,
                LoginBean.class,
                NotificationBean.class,
                QuestionBean.class,
                StoreAnswerBean.class,
                UploadFileDataBean.class,
                VersionBean.class,
                LocationBean.class,
                UncaughtExceptionBean.class,
                FamilyBean.class,
                MemberBean.class,
                DataQualityBean.class,
                FhwServiceDetailBean.class,
                MergedFamiliesBean.class,
                FormAccessibilityBean.class,
                LocationMasterBean.class,
                MemberCbacDetailBean.class,
                LibraryBean.class,
                MigratedMembersBean.class,
                ReadNotificationsBean.class,
                HealthInfrastructureBean.class,
                LocationCoordinatesBean.class,
                MemberPregnancyStatusBean.class,
                DataQualityBean.class,
                SchoolBean.class,
                DailyPresenceReport.class,
                CovidTravellersInfoBean.class,
                DailyNutritionLogBean.class,
                LocationTypeBean.class,
                MenuBean.class,
                MigratedFamilyBean.class,
                LmsBookMarkBean.class,
                LmsCourseBean.class,
                LmsViewedMediaBean.class,
                FileDownloadBean.class,
                LmsUserMetaDataBean.class,
                LmsEventBean.class,
                MemberMoConfirmedDetailBean.class,
                DrugInventoryBean.class,
                MemberAvailableEveningBean.class,
                UserHealthInfraBean.class
        );
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        Log.i(getClass().getSimpleName(), "OnCreate called");
        for (Class<? extends BaseEntity> clazz : getDatabaseTableList()) {
            try {
                TableUtils.createTableIfNotExists(connectionSource, clazz);
            } catch (SQLException e) {
                Log.e(getClass().getSimpleName(), null, e);
                throw new DataException(e.getMessage(), e);
            }
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        showDbDowngradeAlert(SharedStructureData.welcomeContext);
        SharedStructureData.dbDowngraded = true;
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.i(getClass().getSimpleName(), "OnUpgrade called");
        showDBUpgradeProcessDialog(SharedStructureData.welcomeContext);
        try {
            // drop The Table
            for (Class<?> clazz : getDatabaseTableDrops(database, oldVersion)) {
                TableUtils.dropTable(connectionSource, clazz, true);
            }
            // update The Table
            for (Class<?> clazz : getDatabaseTableUpdatable(oldVersion)) {
                List<ContentValues> records = new ArrayList<>();
                getPreviousData(clazz, records, database);
                TableUtils.dropTable(connectionSource, clazz, true);
                if (!records.isEmpty()) {
                    TableUtils.createTable(connectionSource, clazz);
                    for (ContentValues string : records) {
                        database.insert(clazz.getSimpleName().toLowerCase(), null, string);
                    }
                }
            }
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        } finally {
            // create Other If not Exits
            onCreate(database, connectionSource);
        }
    }

    private Iterable<Class<? extends BaseEntity>> getDatabaseTableUpdatable(int oldVersion) {
        Log.i(getClass().getSimpleName(), "Updatable Table List");
        //Add the class in the list below, whenever there is a change in the field of tables and you don't want a data loss during the upgrade
        List<Class<? extends BaseEntity>> databaseUpdatableTablesList = new ArrayList<>();

        //184 is the DB Version of 4.0.4 Release
        if (oldVersion < 184) {
            databaseUpdatableTablesList.add(QuestionBean.class);
            databaseUpdatableTablesList.add(FamilyBean.class);
            databaseUpdatableTablesList.add(UncaughtExceptionBean.class);
            databaseUpdatableTablesList.add(NotificationBean.class);
            databaseUpdatableTablesList.add(LoginBean.class);
            databaseUpdatableTablesList.add(StoreAnswerBean.class);
            databaseUpdatableTablesList.add(LoggerBean.class);
            databaseUpdatableTablesList.add(LabelBean.class);
            databaseUpdatableTablesList.add(ListValueBean.class);
            databaseUpdatableTablesList.add(FhwServiceDetailBean.class);
//            databaseUpdatableTablesList.add(LibraryBean.class);
            databaseUpdatableTablesList.add(HealthInfrastructureBean.class);
            databaseUpdatableTablesList.add(LocationBean.class);
            databaseUpdatableTablesList.add(AnnouncementBean.class);
        }

        if (oldVersion < 194) {
            databaseUpdatableTablesList.add(MemberBean.class);
        }

        if (oldVersion < 192) {
            databaseUpdatableTablesList.add(LibraryBean.class);
        }

        if (oldVersion < 193) {
            databaseUpdatableTablesList.add(FamilyBean.class);
            databaseUpdatableTablesList.add(MemberBean.class);
        }

        if (oldVersion < 195) {
            databaseUpdatableTablesList.add(FamilyBean.class);
            databaseUpdatableTablesList.add(MemberBean.class);
        }

        if (oldVersion < 196) {
            databaseUpdatableTablesList.add(MemberBean.class);
        }

        if (oldVersion < 197) {
            databaseUpdatableTablesList.add(FamilyBean.class);
        }

        if (oldVersion < 199) {
            databaseUpdatableTablesList.add(MemberMoConfirmedDetailBean.class);
        }

        if (oldVersion < 201) {
            databaseUpdatableTablesList.add(MemberBean.class);
        }

        return databaseUpdatableTablesList;
    }

    private Iterable<Class<? extends BaseEntity>> getDatabaseTableDrops(SQLiteDatabase db, int oldVersion) {
        Log.i(getClass().getSimpleName(), "Dropable Table List");
        //Add the class in the list below whenever there is a change in the field of tables, and its okay if the data is lost.
        List<Class<? extends BaseEntity>> databaseDroppableTablesList = new ArrayList<>();

        if (oldVersion == 169) {
            databaseDroppableTablesList.add(SchoolBean.class);
        }

        if (oldVersion < 192) {
            databaseDroppableTablesList.add(LmsCourseBean.class);
        }

        if (oldVersion < 184) {
            databaseDroppableTablesList.add(MigratedMembersBean.class);
        }

        if(oldVersion < 196){
            databaseDroppableTablesList.add(LmsCourseBean.class);
            databaseDroppableTablesList.add(LmsViewedMediaBean.class);
        }


        //This will add LocationMasterBean to droppable list only if modifiedOn field doesn't exists
        if (checkIfTableExistsInDB(db, LocationMasterBean.class)
                && !checkIfColumnExists(db, LocationMasterBean.class, FieldNameConstants.MODIFIED_ON)) {
            databaseDroppableTablesList.add(LocationMasterBean.class);
        }

        return databaseDroppableTablesList;
    }

    // This will check if the table already exists in the old database.
    private boolean checkIfTableExistsInDB(SQLiteDatabase db, Class<? extends BaseEntity> tableToAdd) {
        String query = "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableToAdd.getSimpleName().toLowerCase() + "'";
        try (Cursor cursor = db.rawQuery(query, null)) {
            if (cursor != null && cursor.getCount() > 0) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfColumnExists(SQLiteDatabase database, Class<? extends BaseEntity> table, String columnName) {
        String query = "SELECT * FROM " + table.getSimpleName().toLowerCase() + " LIMIT 0";
        try (Cursor cursor = database.rawQuery(query, null)) {
            // getColumnIndex() gives us the index (0 to ...) of the column - otherwise we get a -1
            return cursor.getColumnIndex(columnName) != -1;
        } catch (Exception e) {
            // Something went wrong. Missing the database? The table?
            Log.e(getClass().getSimpleName(), null, e);
            return false;
        }
    }

    private void getPreviousData(Class<?> clazz, List<ContentValues> records, SQLiteDatabase database) {
        try (Cursor cursor = database.query(clazz.getSimpleName().toLowerCase(), null, null, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ContentValues record = new ContentValues();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        try {
                            record.put(cursor.getColumnName(i), cursor.getString(i));
                        } catch (Exception e) {
                            Log.e(getClass().getSimpleName(), null, e);
                            String value = new String(cursor.getBlob(i), StandardCharsets.UTF_16);
                            record.put(cursor.getColumnName(i), value);
                        }
                    }
                    records.add(record);
                } while (cursor.moveToNext());
                Log.i(getClass().getSimpleName(), "Previous Data is :: \n" + records);
            }
        }
    }

    public void showDBUpgradeProcessDialog(Context context) {
        ((Activity) context).runOnUiThread(() -> {
            if (processDialog == null || !processDialog.isShowing()) {
                processDialog = new MyProcessDialog(context, UtilBean.getMyLabel(LabelConstants.UPGRADING_DATABASES));
                processDialog.show();
            }
        });
    }

    public void showDbDowngradeAlert(Context context) {
        ((Activity) context).runOnUiThread(() -> {
            if (processDialog != null && processDialog.isShowing()) {
                processDialog.dismiss();
            }
            View.OnClickListener listener = v -> {
                dialogForExit.dismiss();
                android.os.Process.killProcess(android.os.Process.myPid());
                Runtime.getRuntime().exit(0);
            };

            dialogForExit = new MyAlertDialog(context, false,
                    LabelConstants.DATABASE_DOWNGRADASION_IS_NOT_POSSIBLE,
                    listener, DynamicUtils.BUTTON_OK);
            dialogForExit.show();
        });
    }
}
