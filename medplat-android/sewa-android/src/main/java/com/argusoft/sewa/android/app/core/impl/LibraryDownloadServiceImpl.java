package com.argusoft.sewa.android.app.core.impl;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import com.argusoft.sewa.android.app.util.Log;

import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.core.LibraryDownloadService;
import com.argusoft.sewa.android.app.databean.LibraryDataBean;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.LibraryBean;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.argusoft.sewa.android.app.util.WSConstants;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by prateek on 12/2/19.
 */
@EBean(scope = EBean.Scope.Singleton)
public class LibraryDownloadServiceImpl implements LibraryDownloadService {

    @RootContext
    Context context;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<LibraryBean, Integer> libraryBeanDao;

    public static final String INACTIVE = "INACTIVE";

    private boolean isFileExistsOnLocal(String fileName, Long actualId) {
        File downloadedFile = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LIBRARY_TEMP), fileName);
        if (downloadedFile.exists()) {
            moveFile(downloadedFile, fileName, actualId);
        }
        File file = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LIBRARY), fileName);
        return file.exists();
    }

    private static void clearSharedPref(Context context) {
        SharedPreferences downloadPref = context.getSharedPreferences(
                SewaConstants.LIBRARY_DOWNLOAD_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = downloadPref.edit();
        edit.clear();
        edit.apply();
    }

    @Override
    public void deleteFile(String fileName, Long actualId) {
        Log.e("LibraryDownloadService", "deleteFile called for File : " + fileName);
        File file = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LIBRARY), fileName);
        if (file.exists()) {
            file.delete();
        }

        try {
            LibraryBean libraryBean = libraryBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_ID, actualId).queryForFirst();
            if (libraryBean != null) {
                libraryBean.setIsDownloaded(false);
                libraryBeanDao.update(libraryBean);
            }
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }

        clearSharedPref(context);
    }

    private void moveFile(File downloadedFile, String fileName, Long actualId) {
        downloadedFile.renameTo(new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LIBRARY), fileName));
        Log.i(getClass().getSimpleName(), "#### File moved to " + SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LIBRARY));
        try {
            LibraryBean libraryBean = libraryBeanDao.queryBuilder().where().eq(FieldNameConstants.ACTUAL_ID, actualId).queryForFirst();
            libraryBean.setIsDownloaded(true);
            libraryBeanDao.update(libraryBean);
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }

        clearSharedPref(context);
    }

    private boolean isDownloadInProgress(final Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SewaConstants.LIBRARY_DOWNLOAD_PREFS_NAME, MODE_PRIVATE);
        long downloadId = prefs.getLong(FieldNameConstants.DOWNLOAD_ID, 0);
        long actualId = prefs.getLong(FieldNameConstants.ACTUAL_ID, 0);
        String fileName = prefs.getString(FieldNameConstants.FILE_NAME, null);

        if (downloadId == 0 || actualId == 0 || fileName == null) {
            return false;
        }

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);

        Cursor c = downloadManager.query(query);
        if (c != null && c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

            switch (status) {
                case DownloadManager.STATUS_PENDING:
                    Log.i(getClass().getSimpleName(), downloadId + " Download is PENDING");
                    clearSharedPref(context);
                    return false;

                case DownloadManager.STATUS_RUNNING:
                    Log.i(getClass().getSimpleName(), downloadId + " Download is RUNNING");
                    return true;

                case DownloadManager.STATUS_PAUSED:
                    Log.i(getClass().getSimpleName(), downloadId + " Download is PAUSED");
                    clearSharedPref(context);
                    return false;

                case DownloadManager.STATUS_FAILED:
                    Log.i(getClass().getSimpleName(), downloadId + " Download FAILED");
                    deleteFile(fileName, actualId);
                    return false;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Log.i(getClass().getSimpleName(), "#### Download Completed. Success. : " + fileName);
                    // if download has been successful
                    File downloadedFile = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LIBRARY_TEMP), fileName);
                    if (!downloadedFile.exists()) {
                        downloadedFile = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LIBRARY), fileName);
                        Log.i(getClass().getSimpleName(), "#### Temp File not found!");
                        return downloadedFile.exists();
                    } else {
                        moveFile(downloadedFile, fileName, actualId);
                    }
                    return true;
                default:
            }
        }
        if (c != null) {
            c.close();
        }
        return false;
    }

    private void startFileDownloading(Context context, LibraryBean libraryBean) {
        String downloadPath = WSConstants.REST_TECHO_SERVICE_URL + "downloadlibraryfile?fileId="
                + libraryBean.getActualId().toString().replace(" ", "%20");
        Log.i(getClass().getSimpleName(), "##### Download URL : " + downloadPath);
        final Uri downloadUri = Uri.parse(downloadPath);
        Uri fileUri = Uri.parse("file://" + SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LIBRARY_TEMP) + libraryBean.getActualId() + "." + libraryBean.getFileType());

        Log.i(getClass().getSimpleName(), "************Downloading started ***********");
        DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setMimeType("application/vnd.android.package-archive");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(true)
                .setDestinationUri(fileUri);
        request.setTitle(UtilBean.getMyLabel("TeCHO+") + " - " + UtilBean.getMyLabel("Downloading Library files"));
        request.setDescription(libraryBean.getActualId() + "." + libraryBean.getFileType());
        long enqueueId = dm.enqueue(request);

        SharedPreferences downloadPref = context.getSharedPreferences(SewaConstants.LIBRARY_DOWNLOAD_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = downloadPref.edit();
        edit.putLong(FieldNameConstants.DOWNLOAD_ID, enqueueId);
        edit.putLong(FieldNameConstants.ACTUAL_ID, libraryBean.getActualId());
        edit.putString(FieldNameConstants.FILE_NAME, libraryBean.getActualId().toString() + "." + libraryBean.getFileType());

        edit.apply();
        Log.i(getClass().getSimpleName(), "#### Registering the Broadcast");
        registerBroadcastReceiver(context);
    }

    private void registerBroadcastReceiver(Context context) {
        BroadcastReceiver receiver;
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context,
                                  Intent intent) {
                Log.i(getClass().getSimpleName(), "#### Received the Download Broadcast");
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    Log.i(getClass().getSimpleName(), "#### RB: download completed.");

                    DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                    DownloadManager.Query query = new DownloadManager.Query();

                    SharedPreferences prefs = context.getSharedPreferences(SewaConstants.LIBRARY_DOWNLOAD_PREFS_NAME, MODE_PRIVATE);
                    long downloadId = prefs.getLong(FieldNameConstants.DOWNLOAD_ID, 0);
                    long actualId = prefs.getLong(FieldNameConstants.ACTUAL_ID, 0);
                    String fileName = prefs.getString(FieldNameConstants.FILE_NAME, null);

                    query.setFilterById(downloadId);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        String downloadedFileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION));
                        Log.i(getClass().getSimpleName(), "#### Downloaded file name: " + downloadedFileName);
                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                            Log.i(getClass().getSimpleName(), "#### Downloaded successful ! " + downloadedFileName);
                            File downloadedFile = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LIBRARY_TEMP), downloadedFileName);
                            if (downloadedFile.exists()) {
                                moveFile(downloadedFile, downloadedFileName, actualId);
                                retrieveNotDownloadedAndStartDownload();
                            }
                        } else if (DownloadManager.STATUS_FAILED == c.getInt(columnIndex)) {
                            Log.i(getClass().getSimpleName(), "#### Download failed, Deleting all the files");
                            deleteFile(fileName, actualId);
                            retrieveNotDownloadedAndStartDownload();
                        }
                    }
                    c.close();
                }
            }
        };

        context.registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void retrieveNotDownloadedAndStartDownload() {
        try {
            List<LibraryBean> libraryBeans = libraryBeanDao.queryBuilder().where().eq(FieldNameConstants.IS_DOWNLOADED, false).query();
            if (libraryBeans != null && !libraryBeans.isEmpty()) {
                LibraryBean libraryBean = libraryBeans.get(0);
                if (!isFileExistsOnLocal(libraryBean.getActualId() + "." + libraryBean.getFileType(), libraryBean.getActualId())) {
                    if (!isDownloadInProgress(context)) {
                        startFileDownloading(context, libraryBean);
                    } else {
                        libraryBean.setIsDownloaded(true);
                        libraryBeanDao.update(libraryBean);
                        this.retrieveNotDownloadedAndStartDownload();
                    }
                } else {
                    libraryBean.setIsDownloaded(true);
                    libraryBeanDao.update(libraryBean);
                    this.retrieveNotDownloadedAndStartDownload();
                }
            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
    }

    @Override
    public List<LibraryDataBean> retrieveLibraryBeansByCategory(String category, String search) {
        List<LibraryDataBean> libraryDataBeans = new ArrayList<>();
        try {
            List<LibraryBean> libraryBeans;
            if (category != null && !category.isEmpty()) {
                libraryBeans = libraryBeanDao.queryBuilder().where()
                        .ne(FieldNameConstants.STATE, INACTIVE)
                        .and().eq(FieldNameConstants.IS_DOWNLOADED, true)
                        .and().like(FieldNameConstants.CATEGORY, category + "%").query();
            } else {
                if (search != null && !search.isEmpty()) {
                    libraryBeans = libraryBeanDao.queryBuilder().where()
                            .ne(FieldNameConstants.STATE, INACTIVE)
                            .and().eq(FieldNameConstants.IS_DOWNLOADED, true)
                            .and().like(FieldNameConstants.TAG, search + "%")
                            .query();
                } else {
                    libraryBeans = libraryBeanDao.queryBuilder().where()
                            .ne(FieldNameConstants.STATE, INACTIVE)
                            .and().eq(FieldNameConstants.IS_DOWNLOADED, true)
                            .query();
                }
            }

            if (libraryBeans != null && !libraryBeans.isEmpty()) {
                for (LibraryBean bean : libraryBeans) {
                    libraryDataBeans.add(new LibraryDataBean(bean));
                }
            }

        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        return libraryDataBeans;
    }
}
