/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.argusoft.sewa.android.app.BuildConfig;
import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyProcessDialog;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.MoveToProductionServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.core.impl.TechoServiceImpl;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.model.FormAccessibilityBean;
import com.argusoft.sewa.android.app.model.VersionBean;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.BackgroundDownloadUtils;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.textview.MaterialTextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.io.File;
import java.util.Calendar;
import java.util.List;

/**
 * @author alpeshkyada
 */
@EActivity
public class MenuActivity extends AppCompatActivity {

    @Bean
    public MoveToProductionServiceImpl moveToProductionService;
    @Bean
    public TechoServiceImpl techoService;

    public Context context = this;
    MyAlertDialog alertDialog;
    MyProcessDialog processDialog;

    @Override
    public void setTheme(int resid) {
        super.setTheme(SewaUtil.CURRENT_THEME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_home) {
            onHomePressed();
            return true;
        } else if (item.getItemId() == R.id.menu_refresh) {
            processDialog = new MyProcessDialog(this, GlobalTypes.PLEASE_WAIT);
            processDialog.show();
            doUpdate(false);
            if (SewaUtil.isUserInTraining) {
                showTrainingCompletedForm();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void onHomePressed() {
        View.OnClickListener myListener = v -> {
            if (v.getId() == BUTTON_POSITIVE) {
                alertDialog.dismiss();
                navigateToHomeScreen(false);
                finish();
            } else {
                alertDialog.dismiss();
            }
        };

        alertDialog = new MyAlertDialog(this,
                LabelConstants.WANT_TO_GO_BACK_TO_HOME_SCREEN,
                myListener, DynamicUtils.BUTTON_YES_NO);
        alertDialog.show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_refresh).setVisible(true);
        menu.findItem(R.id.menu_about).setVisible(false);
        menu.findItem(R.id.menu_announcement).setVisible(false);
        menu.findItem(R.id.menu_home).setVisible(true);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    public void navigateToHomeScreen(boolean isLogout) {
        Intent intent = null;
        if (SewaTransformer.loginBean != null && SewaTransformer.loginBean.getUserRole() != null && !SewaTransformer.loginBean.getUserRole().trim().isEmpty()) {
            intent = new Intent(this, GenericHomeScreenActivity_.class);
        }

        if (intent != null) {
            if (isLogout) {
                intent.putExtra("From", "logout");
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    public void showProcessDialog() {
        if (processDialog == null || !processDialog.isShowing()) {
            runOnUiThread(() -> {
                processDialog = new MyProcessDialog(MenuActivity.this, UtilBean.getMyLabel(LabelConstants.PROCESSING));
                if(!isFinishing()) {
                    processDialog.show();
                }
            });
        }
    }

    protected void hideProcessDialog() {
        if (processDialog != null && processDialog.isShowing()) {
            runOnUiThread(() -> processDialog.dismiss());
        }
    }

    @SuppressLint("RestrictedApi")
    protected void setTitle(String title) {
        LinearLayout layout = findViewById(R.id.toolbar_layout);
        if (layout != null) {
            layout.setPadding(0, 0, 15, 0);
        }

        MaterialTextView textView = findViewById(R.id.toolbar_title);
        if (textView != null) {
            textView.setText(UtilBean.getMyLabel(title));
            textView.setTextAppearance(context, R.style.ToolbarTitle);
            textView.setAutoSizeTextTypeUniformWithConfiguration(8, 15, 1, TypedValue.COMPLEX_UNIT_SP);
        }

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowCustomEnabled(false); //show custom title
            supportActionBar.setDisplayShowTitleEnabled(false); //hide the default title
        }
        setSubTitle(null);
    }

    protected void setSubTitle(String sub) {
        if (sub == null && SewaTransformer.loginBean != null) {
            sub = SewaTransformer.loginBean.getUsername();
        }
        final String subtitle = sub;
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

    public void showAlertAndFinish(final String msg) {
        runOnUiThread(() -> {
            alertDialog = new MyAlertDialog(MenuActivity.this,
                    false,
                    UtilBean.getMyLabel(msg),
                    v -> {
                        alertDialog.dismiss();
                        finish();
                    },
                    DynamicUtils.BUTTON_OK);
            alertDialog.show();
        });
    }

    public MaterialTextView lastUpdateLabelView(SewaServiceImpl sewaService, LinearLayout linearLayout) {
        long lastUpdateTime = 0L;
        VersionBean versionBean = new VersionBean();
        versionBean.setKey(SewaConstants.TIME_STAMP_LAST_REFRESH);
        versionBean = sewaService.getSheetStatusByFiletr(versionBean);
        if (versionBean != null) {
            lastUpdateTime = Long.parseLong(versionBean.getValue());
        }
        MaterialTextView updateLabel = new MaterialTextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = linearLayout.getPaddingBottom() * -1;
        layoutParams.setMargins(margin, margin, margin, 0);
        updateLabel.setLayoutParams(layoutParams);
        updateLabel.setTextAppearance(context, R.style.LastUpdateLabel);
        updateLabel.setText(UtilBean.getLastUpdatedTime(lastUpdateTime));
        updateLabel.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        updateLabel.setBackgroundColor(ContextCompat.getColor(context, R.color.lastUpdateLabelBackground));
        updateLabel.setPadding(10, 10, 10, 10);
        return updateLabel;
    }

    public void addDataNotSyncedMsg(final LinearLayout bodyLayoutContainer, final Button nextButton) {
        runOnUiThread(() -> {
            bodyLayoutContainer.addView(MyStaticComponents.generateInstructionView(context, UtilBean.getMyLabel("Data not synced properly. Please refresh.")));
            View.OnClickListener listener = v -> navigateToHomeScreen(false);
            nextButton.setText(UtilBean.getMyLabel(GlobalTypes.EVENT_OKAY));
            nextButton.setOnClickListener(listener);
            hideProcessDialog();
        });
    }

    private void showTrainingCompletedForm() {

        List<FormAccessibilityBean> formAccessibilityBeans = moveToProductionService.isAnyFormTrainingCompleted();
        if (formAccessibilityBeans != null && !formAccessibilityBeans.isEmpty()) {
            Intent intent = new Intent(this, MoveToProductionActivity_.class);
            startActivity(intent);
        }
    }

    @Background
    public void doUpdate(Boolean restartHomeScreen) {
        if (SharedStructureData.sewaService != null && SharedStructureData.sewaService.isOnline()) {
            this.runOnUiThread(() -> {
                if (processDialog != null) {
                    TextView message = processDialog.findViewById(IdConstants.PROGRESS_DIALOG_MESSAGE_ID); // that is id given to message text box
                    if (message != null) {
                        message.setText(UtilBean.getMyLabel(GlobalTypes.MSG_LOAD_DATA_FROM_SERVER));
                    }
                }
            });

            try {
                SharedStructureData.sewaService.revalidateUserTokenForUser();
                SharedStructureData.sewaService.doAfterSuccessfulLogin(false);
            } catch (Exception e) {
                SharedStructureData.NETWORK_MESSAGE = SewaConstants.NETWORK_NOT_PROPER;
                Log.e(getClass().getName(), "Exception", e);
            } finally {
                hideProcessDialog();
            }

            if (!SharedStructureData.NETWORK_MESSAGE.equalsIgnoreCase(SewaConstants.NETWORK_AVAILABLE)) {
                if (SharedStructureData.NETWORK_MESSAGE.equalsIgnoreCase(SewaConstants.SQL_EXCEPTION)) {
                    this.runOnUiThread(() -> showAlert(LabelConstants.NETWORK,
                            LabelConstants.APP_DATA_ERROR,
                            v -> {
                                techoService.deleteDatabaseFileFromLocal(context);
                                UtilBean.restartApplication(context);
                            },
                            DynamicUtils.BUTTON_OK));
                } else {
                    this.runOnUiThread(() -> showAlert(LabelConstants.NETWORK,
                            SharedStructureData.NETWORK_MESSAGE,
                            null,
                            DynamicUtils.BUTTON_OK));
                }
            } else {
                VersionBean bean = new VersionBean();
                bean.setKey(SewaConstants.TIME_STAMP_LAST_REFRESH);
                bean = SharedStructureData.sewaService.getSheetStatusByFiletr(bean);
                if (bean == null) {
                    bean = new VersionBean();
                    bean.setKey(SewaConstants.TIME_STAMP_LAST_REFRESH);
                }
                bean.setValue(Calendar.getInstance().getTimeInMillis() + "");
                SharedStructureData.sewaService.updateVersionBean(bean);
            }
            onCheckUpdateComplete();
            if (Boolean.TRUE.equals(restartHomeScreen)) {
                finish();
                startActivity(getIntent());
            }
        } else {
            showAlert(LabelConstants.NETWORK, LabelConstants.INTERNET_CONNECTION_REQUIRED_TO_REFRESH, null, DynamicUtils.BUTTON_OK);
        }
    }


    private void onCheckUpdateComplete() {
        Log.i(getClass().getSimpleName(), "#### onCheckUpdateComplete called from MenuActivity");

        final String minMaxVersions = SharedStructureData.sewaService.getAndroidVersion();
        if (minMaxVersions == null) {
            return;
        }

        String currentVersion = minMaxVersions.split("-")[0];
        if (currentVersion == null) {
            return;
        }

        boolean isMajor = false;
        Log.i(getClass().getSimpleName(), "#### DB SEWA APP VERSION :" + currentVersion);
        if (currentVersion.contains("M") || currentVersion.contains("m")) {
            isMajor = true;
            currentVersion = currentVersion.replace("M", "").replace("m", "");
        }

        final String apkName = BuildConfig.APK_NAME + "_" + currentVersion + ".apk";

        if (!checkAuthenticityOfCurrentVersion(minMaxVersions, apkName)) {
            return;
        }

        if (Integer.parseInt(currentVersion) > BuildConfig.VERSION_CODE) {
            Log.i(getClass().getName(), "#### APP NAME (apk name): " + apkName);
            Log.i(getClass().getName(), "#### Current Version :" + currentVersion + " & Config Local Version : " + BuildConfig.VERSION_CODE);

            if (BackgroundDownloadUtils.isApkExistsOnLocal(context, apkName)) {
                hideProcessDialog();
                BackgroundDownloadUtils.showInstallAppDialog(this, apkName);
            } else if (BackgroundDownloadUtils.isDownloadNotInProgress(this, apkName)) {
                if (isMajor) {
                    showAlert(UtilBean.getMyLabel(LabelConstants.APP_UPDATE_STATUS),
                            UtilBean.getMyLabel(LabelConstants.NEW_APPLICATION_AVAILABLE),
                            view -> {
                                alertDialog.dismiss();
                                BackgroundDownloadUtils.deleteAllFiles(context, new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED)));
                                BackgroundDownloadUtils.startAppDownloading(context, apkName);
                                Intent downloadProgress = new Intent(context, DownloadProgressActivity.class);
                                startActivity(downloadProgress);
                                finish();
                            },
                            DynamicUtils.BUTTON_OK);
                } else {
                    BackgroundDownloadUtils.deleteAllFiles(context, new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED)));
                    BackgroundDownloadUtils.startAppDownloading(context, apkName);
                }
            }
        } else {
            Log.i(getClass().getName(), "#### Latest version.... So, Deleting all the files");
            BackgroundDownloadUtils.deleteAllFiles(this, new File(
                    SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED)));
        }
    }

    private boolean checkAuthenticityOfCurrentVersion(String minMaxVersion, final String apkName) {
        String[] versions = minMaxVersion.split("-");
        if (versions.length == 1) {
            return true;
        }

        int installedAppVersion = BuildConfig.VERSION_CODE;
        int minSupportedVersion = Integer.parseInt(versions[1]);

        if (BackgroundDownloadUtils.isApkExistsOnLocal(context, apkName)) {
            hideProcessDialog();
            BackgroundDownloadUtils.showInstallAppDialog(context, apkName);
        } else if (installedAppVersion < minSupportedVersion) {
            showAlert(UtilBean.getMyLabel(LabelConstants.APP_UPDATE_STATUS), UtilBean.getMyLabel(
                    LabelConstants.NEW_APPLICATION_AVAILABLE),
                    v -> {
                        alertDialog.dismiss();
                        BackgroundDownloadUtils.deleteAllFiles(context, new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED)));
                        BackgroundDownloadUtils.startAppDownloading(context, apkName);
                        Intent downloadProgress = new Intent(context, DownloadProgressActivity.class);
                        startActivity(downloadProgress);
                        finish();
                    }, DynamicUtils.BUTTON_OK);
            return false;
        }
        return true;
    }

    @UiThread
    public void showAlert(String title, String msg, View.OnClickListener listener, int buttonType) {
        hideProcessDialog();
        alertDialog = new MyAlertDialog(this, false, msg, listener, buttonType);
        alertDialog.show();
    }
}
