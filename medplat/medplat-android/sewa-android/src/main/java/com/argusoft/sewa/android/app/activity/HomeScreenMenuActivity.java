package com.argusoft.sewa.android.app.activity;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.argusoft.sewa.android.app.BuildConfig;
import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.component.MyProcessDialog;
import com.argusoft.sewa.android.app.constants.IdConstants;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.LmsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.MoveToProductionServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceRestClientImpl;
import com.argusoft.sewa.android.app.core.impl.TechoServiceImpl;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.AnnouncementBean;
import com.argusoft.sewa.android.app.model.FormAccessibilityBean;
import com.argusoft.sewa.android.app.model.LabelBean;
import com.argusoft.sewa.android.app.model.LoginBean;
import com.argusoft.sewa.android.app.model.UserHealthInfraBean;
import com.argusoft.sewa.android.app.model.VersionBean;
import com.argusoft.sewa.android.app.restclient.RestHttpException;
import com.argusoft.sewa.android.app.transformer.SewaTransformer;
import com.argusoft.sewa.android.app.util.BackgroundDownloadUtils;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.argusoft.sewa.android.app.util.SewaUtil;
import com.argusoft.sewa.android.app.util.UtilBean;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.io.File;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by prateek on 11/18/19
 */
@EActivity
public class HomeScreenMenuActivity extends AppCompatActivity {

    @OrmLiteDao(helper = DBConnection.class)
    Dao<LoginBean, Integer> loginBeanDao;

    @OrmLiteDao(helper = DBConnection.class)
    Dao<AnnouncementBean, Integer> announcementBeanDao;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<UserHealthInfraBean, Integer> userHealthInfraBeanDao;

    @Bean
    MoveToProductionServiceImpl moveToProductionService;
    @Bean
    TechoServiceImpl techoService;
    @Bean
    LmsServiceImpl lmsService;
    @Bean
    SewaServiceRestClientImpl sewaServiceRestClient;

    Context context = this;
    MyAlertDialog alertDialog;
    MyProcessDialog processDialog;
    boolean alertDialogShown = false;
    TextView textCartItemCount;

    private static final String TAG = "HomeScreenMenuActivity";

    @Override
    public void setTheme(int resid) {
        super.setTheme(SewaUtil.CURRENT_THEME);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.menu_announcement);

        View actionView = menuItem.getActionView();
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_home).setVisible(false);
        menu.findItem(R.id.menu_refresh).setVisible(true);
        menu.findItem(R.id.menu_about).setVisible(true);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
            return true;
        } else if (item.getItemId() == R.id.menu_announcement) {
            Intent intent;
            intent = new Intent(this, AnnouncementActivity_.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menu_refresh) {
            processDialog = new MyProcessDialog(this, GlobalTypes.PLEASE_WAIT);
            processDialog.show();
            doUpdate(false);
            if (SewaUtil.isUserInTraining) {
                showTrainingCompletedForm();
            }
            return true;
        } else if (item.getItemId() == R.id.menu_about) {
            Intent intent = new Intent(this, AboutActivity_.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showTrainingCompletedForm() {

        List<FormAccessibilityBean> formAccessibilityBeans = moveToProductionService.isAnyFormTrainingCompleted();
        if (formAccessibilityBeans != null && !formAccessibilityBeans.isEmpty()) {
            Intent intent = new Intent(this, MoveToProductionActivity_.class);
            startActivity(intent);
        }
    }


//    public void checkIfFormIsAccessibleToUser(String formType) {
//        FormAccessibilityBean formAccessibilityBean = moveToProductionService.retrieveFormAccessibilityBeanByFormType(formType);
//        final View.OnClickListener myListener = v -> {
//            alertDialogShown = false;
//            alertDialog.dismiss();
//        };
//
//        if (!SewaUtil.isUserInTraining) {
//            if (formAccessibilityBean != null) {
//                if (formAccessibilityBean.getTrainingReq() != null && formAccessibilityBean.getTrainingReq()) {
//                    if (formAccessibilityBean.getState() == null || !formAccessibilityBean.getState().equals("MOVE_TO_PRODUCTION")) {
//                        if (!alertDialogShown) {
//                            alertDialogShown = true;
//                            runOnUiThread(() -> {
//                                alertDialog = new MyAlertDialog(context, UtilBean.getMyLabel(LabelConstants.TRAINING_REQUIRED_TO_ACCESS_FORM_ALERT), myListener, DynamicUtils.BUTTON_OK);
//                                alertDialog.show();
//                            });
//                        }
//                    } else {
//                        alertDialogShown = false;
//                    }
//                } else {
//                    alertDialogShown = false;
//                }
//            } else {
//                if (!alertDialogShown) {
//                    alertDialogShown = true;
//                    runOnUiThread(() -> {
//                        alertDialog = new MyAlertDialog(context, UtilBean.getMyLabel(LabelConstants.TRAINING_REQUIRED_TO_ACCESS_FORM_ALERT), myListener, DynamicUtils.BUTTON_OK);
//                        alertDialog.show();
//                    });
//                }
//            }
//        }
//    }


    @UiThread
    public void showProcessDialog() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (processDialog == null || !processDialog.isShowing()) {
            processDialog = new MyProcessDialog(context, UtilBean.getMyLabel(LabelConstants.PROCESSING));
            processDialog.show();
        }
    }

    //no use of this (recycler view implemented)
//    void setCardView(Context context, String[] items, int[] images, AdapterView.OnItemClickListener onItemClickListener) {
//        LinearLayout layout = findViewById(R.id.rv_home_screen_icons);
//        adapter = new MyMenuAdapter(context, images, items, true);
//        GridView gridView = new GridView(context);
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            gridView.setNumColumns(6);
//        } else {
//            gridView.setNumColumns(3);
//        }
//        gridView.setVerticalScrollBarEnabled(false);
//        gridView.setGravity(Gravity.CENTER_VERTICAL);
//        gridView.setAdapter(adapter);
//        gridView.setOnItemClickListener(onItemClickListener);
//        layout.addView(gridView);
//    }


    public void logoutAlertDialogClick(View v) {
        if (v.getId() == BUTTON_POSITIVE) {
            if (SewaTransformer.loginBean != null) {
                try {
                    SewaTransformer.loginBean.setLoggedIn(false);
                    SewaTransformer.loginBean.setHidden(false);
                    loginBeanDao.update(SewaTransformer.loginBean);
                } catch (SQLException ex) {
                    Logger.getLogger(HomeScreenMenuActivity.class.getName()).log(Level.SEVERE, null, ex);
                }
                alertDialog.dismiss();
            }
            Intent intent = new Intent(context, LoginActivity_.class);
            context.startActivity(intent);
            finish();
        } else if (v.getId() == BUTTON_NEGATIVE) {
            try {
                SewaTransformer.loginBean.setLoggedIn(true);
                SewaTransformer.loginBean.setHidden(true);
                loginBeanDao.update(SewaTransformer.loginBean);
            } catch (SQLException ex) {
                Logger.getLogger(HomeScreenMenuActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
            alertDialog.dismiss();
            finish();
        }
    }

    protected void setActionBarDesign() {
        MaterialTextView textView = findViewById(R.id.toolbar_title);
        MaterialTextView subTitle = findViewById(R.id.toolbar_subtitle);
        if (BuildConfig.FLAVOR.equals(GlobalTypes.UTTARAKHAND_FLAVOR)) {
            textView.setText(LabelConstants.CHARDHAM);
        } else {
            textView.setText(LabelConstants.IHRPTM);
        }
        textView.setTextAppearance(context, R.style.ToolbarTitle);
        subTitle.setVisibility(View.GONE);
        ActionBar supportActionBar = getSupportActionBar();

        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowCustomEnabled(false); //show custom title
            supportActionBar.setDisplayShowTitleEnabled(false); //hide the default title
            supportActionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
    }

    protected void setNavigationView() {

        NavigationView navigationView = findViewById(R.id.nav_view);
        LinearLayout headerLayout = (LinearLayout) navigationView.getHeaderView(0);

        if (SewaTransformer.loginBean != null) {
            MaterialTextView nameView = (MaterialTextView) headerLayout.getChildAt(1);
            nameView.setText(SewaTransformer.loginBean.getFirstName());

            MaterialTextView roleView = (MaterialTextView) headerLayout.getChildAt(2);
            roleView.setText(SewaTransformer.loginBean.getUserRole());

            MaterialTextView languageView = (MaterialTextView) headerLayout.getChildAt(3);
            languageView.setText(UtilBean.getMyLabel(LabelConstants.CHOOSE_LANGUAGE));
            languageView.setOnClickListener(getLanguageMenuClickListener());

            if (BuildConfig.FLAVOR.equals(GlobalTypes.UTTARAKHAND_FLAVOR)) {
                languageView.setVisibility(View.GONE);
            }
        }
        LinearLayout linearLayout = findViewById(R.id.nav_footer);
        linearLayout.setOnClickListener(v -> startLoginActivity());
    }

    private View.OnClickListener getLanguageMenuClickListener() {
        int[] checkedItem = {-1};
        return view -> {
            //Open popup to set language
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setIcon(R.drawable.ic_language);
            alertDialog.setTitle("Choose a language");
            final String[] listItems = new String[]{UtilBean.getMyLabel(LabelConstants.ENGLISH), UtilBean.getMyLabel(LabelConstants.GUJARATI)};

            if (SewaTransformer.loginBean.getLanguageCode().equals("EN")) {
                checkedItem[0] = 0;
            } else {
                checkedItem[0] = 1;
            }

            alertDialog.setSingleChoiceItems(listItems, checkedItem[0], (dialog, which) -> {
                checkedItem[0] = which;
                dialog.dismiss();

                if (which == 0) {
                    //ENGLISH
                    if (!SewaTransformer.loginBean.getLanguageCode().equals("EN")) {
                        showProcessDialog();
                        networkCallsForLanguageChange("EN");
                    }
                } else {
                    //HINDI
                    if (!SewaTransformer.loginBean.getLanguageCode().equals("GU")) {
                        showProcessDialog();
                        networkCallsForLanguageChange("GU");
                    }
                }
            });

            alertDialog.setNegativeButton("Cancel", (dialog, which) -> {
                //do nothing
            });

            AlertDialog customAlertDialog = alertDialog.create();
            customAlertDialog.show();
        };
    }

    @Background
    public void networkCallsForLanguageChange(String preferredLanguage) {
        //Language updated
        try {
            if (SharedStructureData.sewaService.isOnline()) {
                sewaServiceRestClient.updateLanguagePreference(preferredLanguage);
            } else {
                showAlert(LabelConstants.NETWORK, LabelConstants.INTERNET_CONNECTION_REQUIRED_FOR_LANGUAGE_CHANGE, null, DynamicUtils.BUTTON_OK);
            }
        } catch (RestHttpException e) {
            e.printStackTrace();
            //Network call failed for updating user language
            runOnUiThread(() -> SewaUtil.generateToast(context, LabelConstants.SOME_ERROR_OCCURRED_PLEASE_TRY_AGAIN));
            hideProcessDialog();
            //Return
            return;
        }
        if (SharedStructureData.sewaService.isOnline()) {
            //update language locally
            try {
                SewaTransformer.loginBean.setLanguageCode(preferredLanguage);
                loginBeanDao.update(SewaTransformer.loginBean);
                //Table cleared for Label Bean
                TableUtils.clearTable(loginBeanDao.getConnectionSource(), LabelBean.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //Refresh now.. SO that language can be synced from server
            doUpdate(true);
        }
    }

    private void startLoginActivity() {
        if (SewaTransformer.loginBean != null) {
            try {
                SewaTransformer.loginBean.setLoggedIn(false);
                SewaTransformer.loginBean.setHidden(false);
                loginBeanDao.update(SewaTransformer.loginBean);
            } catch (SQLException ex) {
                Log.e(getClass().getName(), null, ex);
            }
        }
        Intent intent = new Intent(context, LoginActivity_.class);
        startActivity(intent);
        finish();
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
                    this.runOnUiThread(() -> showAlert(LabelConstants.NETWORK, LabelConstants.APP_DATA_ERROR, v -> {
                        techoService.deleteDatabaseFileFromLocal(context);
                        UtilBean.restartApplication(context);
                    }, DynamicUtils.BUTTON_OK));
                } else {
                    this.runOnUiThread(() -> showAlert(LabelConstants.NETWORK, SharedStructureData.NETWORK_MESSAGE, null, DynamicUtils.BUTTON_OK));
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
        Log.i(getClass().getSimpleName(), "#### onCheckUpdateComplete called from HomeScreenMenuActivity");

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
                    showAlert(UtilBean.getMyLabel(LabelConstants.APP_UPDATE_STATUS), UtilBean.getMyLabel(LabelConstants.NEW_APPLICATION_AVAILABLE), view -> {
                        alertDialog.dismiss();
                        BackgroundDownloadUtils.deleteAllFiles(context, new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED)));
                        BackgroundDownloadUtils.startAppDownloading(context, apkName);
                        Intent downloadProgress = new Intent(context, DownloadProgressActivity.class);
                        startActivity(downloadProgress);
                        finish();
                    }, DynamicUtils.BUTTON_OK);
                } else {
                    BackgroundDownloadUtils.deleteAllFiles(context, new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED)));
                    BackgroundDownloadUtils.startAppDownloading(context, apkName);
                }
            }
        } else {
            Log.i(getClass().getName(), "#### Latest version.... So, Deleting all the files");
            BackgroundDownloadUtils.deleteAllFiles(this, new File(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED)));
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
            showAlert(UtilBean.getMyLabel(LabelConstants.APP_UPDATE_STATUS), UtilBean.getMyLabel(LabelConstants.NEW_APPLICATION_AVAILABLE), v -> {
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

    @UiThread
    public void hideProcessDialog() {
        if (processDialog != null && processDialog.isShowing()) {
            processDialog.dismiss();
        }
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
}
