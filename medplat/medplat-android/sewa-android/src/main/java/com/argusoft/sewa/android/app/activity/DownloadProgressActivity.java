package com.argusoft.sewa.android.app.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.argusoft.sewa.android.app.BuildConfig;
import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.BackgroundDownloadUtils;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author maulik
 */
public class DownloadProgressActivity extends AppCompatActivity {

    private Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_download_progress);

        final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        SharedPreferences prefs = getSharedPreferences(SewaConstants.DOWNLOAD_PREFS_NAME, MODE_PRIVATE);
        final long downloadId = prefs.getLong("downloadId", 0);

        if (downloadId == 0) {
            return;
        }

        final ProgressBar mProgressBar = findViewById(R.id.progressbar);
        final ProgressBar roundProgressBar = findViewById(R.id.roundProgressBar);

        final TextView percent = findViewById(R.id.percent);
        percent.setText(LabelConstants.HELLO);

        new Thread() {

            @Override
            public void run() {
                boolean downloading = true;
                while (downloading) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor cursor = manager.query(query);
                    cursor.moveToFirst();

                    int bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    final int dl_progress = (int) ((bytesDownloaded * 100L) / bytesTotal);

                    runOnUiThread(() -> {
                        mProgressBar.setProgress(dl_progress);
                        roundProgressBar.setProgress(dl_progress);
                        String text = UtilBean.getMyLabel("Please wait, download in progress") + ": " + dl_progress + "%";
                        percent.setText(text);
                    });

                    try {
                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                            downloading = false;
                            runOnUiThread(() -> {
                                mProgressBar.setProgress(dl_progress);
                                roundProgressBar.setProgress(dl_progress);
                                String text = UtilBean.getMyLabel("Download completed") + ": " + dl_progress + "% ";
                                percent.setText(text);
                            });
                            Logger.getLogger(DownloadProgressActivity.class.getName()).log(Level.INFO, "#### Download Completed. Showing App Install Dialog. (DPA)");
                            showAppInstallDialog();
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(DownloadProgressActivity.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    cursor.close();

                    try {
                        sleep(1000L);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(DownloadProgressActivity.class.getName()).log(Level.SEVERE, null, ex);
                        Thread.currentThread().interrupt();
                    }
                }
            }

        }.start();

    }

    private void showAppInstallDialog() {
        String currentVersion = SharedStructureData.sewaService.getAndroidVersion();
        currentVersion = currentVersion.split("-")[0];
        if (currentVersion.contains("M") || currentVersion.contains("m")) {
            currentVersion = currentVersion.replace("M", "").replace("m", "");
        }
        String apkName = BuildConfig.APK_NAME + "_" + currentVersion + LabelConstants.APK_EXTENSTION;

        if (BackgroundDownloadUtils.isDownloadNotInProgress(this, apkName)) {
            BackgroundDownloadUtils.showInstallAppDialog(this, apkName);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(UtilBean.getMyLabel(LabelConstants.NEW_APLLICATION_DOWNLOADER));
    }

    private void setTitle(String title) {
        MaterialTextView textView = findViewById(R.id.toolbar_title);
        if (textView != null) {
            textView.setText(UtilBean.getMyLabel(title));
            textView.setTextAppearance(context, R.style.ToolbarTitle);
        }

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(false);
            supportActionBar.setDisplayShowCustomEnabled(false); //show custom title
            supportActionBar.setDisplayShowTitleEnabled(false); //hide the default title
        }
        setSubTitle();
    }

    private void setSubTitle() {
        final String subtitle = SewaTransformer.loginBean.getUsername();
        runOnUiThread(() -> {
            MaterialTextView subTitleTextView = findViewById(R.id.toolbar_subtitle);
            if (subTitleTextView != null) {
                if (subtitle == null || subtitle.trim().isEmpty()) {
                    subTitleTextView.setVisibility(View.GONE);
                } else {
                    subTitleTextView.setVisibility(View.VISIBLE);
                    subTitleTextView.setText(UtilBean.getMyLabel(subtitle));
                    subTitleTextView.setTextAppearance(context, R.style.ToolbarSubtitle);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Do nothing
    }
}
