package com.argusoft.sewa.android.app.util;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import androidx.core.content.FileProvider;

import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;

import java.io.File;

/**
 * @author maulik
 */
public class BackgroundDownloadUtils {

    private BackgroundDownloadUtils() {
        throw new IllegalStateException("Utility Class");
    }

    private static MyAlertDialog myDialog;
    private static final String TAG = "BackgroundDownloadUtils";
    private static BroadcastReceiver receiver;

    public static boolean isApkExistsOnLocal(Context context, String fileName) {
        File apkFile = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED),
                fileName);
        Log.d(TAG, "Checking file exists : " + apkFile);
        return apkFile.exists();
    }

    public static boolean isDownloadNotInProgress(final Context context, String latestApkName) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(SewaConstants.DOWNLOAD_PREFS_NAME, MODE_PRIVATE);
            long downloadId = prefs.getLong(FieldNameConstants.DOWNLOAD_ID, 0);

            if (downloadId == 0) {
                return true;
            }

            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);

            Cursor c = downloadManager.query(query);
            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    case DownloadManager.STATUS_PENDING:
                        Log.i(TAG, downloadId + " *** pending ****");
                        return false;

                    case DownloadManager.STATUS_RUNNING:
                        Log.i(TAG, downloadId + " *** running ****");
                        return false;

                    case DownloadManager.STATUS_PAUSED:
                        Log.i(TAG, downloadId + " *** paused ****");
                        return false;

                    case DownloadManager.STATUS_FAILED:
                        Log.i(TAG, downloadId + " *** failed ****");
                        deleteAllFiles(context, new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED)));
                        return true;

                    case DownloadManager.STATUS_SUCCESSFUL:
                        Log.i(TAG, "Download Completed. Success. : " + latestApkName);
                        // if download has been successful but yet to install latest application
                        File downloadedApk = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED_TEMP), latestApkName);
                        if (!downloadedApk.exists()) {
                            downloadedApk = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED), latestApkName);
                            Log.i(TAG, "Temp File not found!");
                            return !downloadedApk.exists();
                        } else {
                            Log.i(TAG, "Moving file from Temp to techo_app Directory");
                            boolean bool = downloadedApk.renameTo(new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED), latestApkName));
                            if (bool) {
                                Log.i(TAG, "File moved!");
                            }
                        }
                        return true;

                    default:
                }
            }
            c.close();
        } catch (Exception ex) {
            Log.e(TAG, null, ex);
        }
        deleteAllFiles(context, new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED)));
        return true;
    }

    public static void showAlert(final Context context,
                                 final String msg,
                                 final View.OnClickListener listener,
                                 final int buttonType) {
        ((Activity) context).runOnUiThread(() -> {
            Log.i(TAG, "Show Alert called!");
            myDialog = new MyAlertDialog(context, false, msg, listener,
                    buttonType);
            myDialog.show();
        });

    }

    public static void deleteAllFiles(Context context, File directory) {
        clearSharedPref(context);
        if (directory.exists() && directory.isDirectory()) {
            String[] childFiles = directory.list();
            for (String file : childFiles) {
                File childFile = new File(directory, file);
                if (!childFile.isDirectory()) {
                    childFile.delete();
                } else {
                    deleteAllFiles(context, childFile);
                }
            }
        }
    }

    public static void clearSharedPref(Context context) {
        SharedPreferences downloadPref = context.getSharedPreferences(SewaConstants.DOWNLOAD_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = downloadPref.edit();
        edit.clear();
        edit.apply();
    }

    public static void startAppDownloading(Context context, String apkName) {
        Log.i(TAG, "Starting to Download latest App");

        String downloadPath = "https://techotraining.gujarat.gov.in/api/mobile/" + WSConstants.ApiCalls.TECHO_DOWNLOAD_APPLICATION + "?name=" + apkName;
        final Uri downloadUri = Uri.parse(downloadPath);

        Log.i(TAG, "************Downloading started ***********");
        DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setMimeType("application/vnd.android.package-archive");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(true)
                .setDestinationUri(Uri.parse("file://" + SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED_TEMP) + apkName));

        long enqueueId = dm.enqueue(request);

        SharedPreferences downloadPref = context.getSharedPreferences(SewaConstants.DOWNLOAD_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = downloadPref.edit();
        edit.putLong(FieldNameConstants.DOWNLOAD_ID, enqueueId);
        edit.apply();
        Log.i(TAG, "Registering the Broadcast");
        registerBroadcastReceiver(context);
    }

    public static void registerBroadcastReceiver(Context context) {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context,
                                  Intent intent) {
                Log.i(TAG, "#### Received the Download Broadcast");
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    Log.i(TAG, "Broadcast Receiver : ACTION_DOWNLOAD_COMPLETE.");
                    DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                    DownloadManager.Query query = new DownloadManager.Query();
                    SharedPreferences prefs = context.getSharedPreferences(SewaConstants.DOWNLOAD_PREFS_NAME, MODE_PRIVATE);
                    long downloadId = prefs.getLong(FieldNameConstants.DOWNLOAD_ID, 0);
                    query.setFilterById(downloadId);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        String downloadedFileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
                        Log.i(TAG, "#### Downloaded file name: " + downloadedFileName);
                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                            Log.i(TAG, "#### Downloaded successful!" + downloadedFileName);
                            File downloadedApk = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED_TEMP), downloadedFileName);
                            if (downloadedApk.exists()) {
                                downloadedApk.renameTo(new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED), downloadedFileName));
                            }
                        } else if (DownloadManager.STATUS_FAILED == c.getInt(columnIndex)) {
                            Log.i(TAG, "#### Download failed, Deleting all the files");
                            deleteAllFiles(context, new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED)));
                        }
                        if (receiver != null) {
                            context.unregisterReceiver(receiver);
                        }
                    }
                    c.close();
                }
            }
        };

        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public static void showInstallAppDialog(final Context context, final String apkName) {
        Log.i(TAG, "Install App Popup called!");
        showAlert(context,
                UtilBean.getMyLabel(LabelConstants.NEW_APPLICATION_AVAILABLE),
                view -> ((Activity) context).runOnUiThread(() -> {
                    myDialog.dismiss();
                    File direct = new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED), apkName);
                    Uri uri = Uri.fromFile(direct);
                    if (Build.VERSION.SDK_INT >= 24) {
                        uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", direct);
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                    intent.setDataAndType(uri, "application/vnd.android" + ".package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }), DynamicUtils.BUTTON_OK);
    }
}
