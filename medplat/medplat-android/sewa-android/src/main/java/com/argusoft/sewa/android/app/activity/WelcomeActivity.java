package com.argusoft.sewa.android.app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import com.argusoft.sewa.android.app.BuildConfig;
import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.core.impl.LocationMasterServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaFhsServiceImpl;
import com.argusoft.sewa.android.app.core.impl.SewaServiceImpl;
import com.argusoft.sewa.android.app.core.impl.TechoServiceImpl;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.db.DBConnection;
import com.argusoft.sewa.android.app.model.LoginBean;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.RootUtil;
import com.argusoft.sewa.android.app.util.SewaConstants;
import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@EActivity
public class WelcomeActivity extends MenuActivity {

    private static final int WELCOME_TIME_OUT = 3000;
    private static final int MY_PERMISSIONS_REQUEST = 101;
    @OrmLiteDao(helper = DBConnection.class)
    Dao<LoginBean, Integer> loginBeanDao;
    @Bean
    SewaServiceImpl sewaService;
    @Bean
    TechoServiceImpl techoService;
    @Bean
    SewaFhsServiceImpl sewaFhsService;
    @Bean
    LocationMasterServiceImpl locationMasterService;

    private MyAlertDialog dialogForExit;

    private String notificationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        SharedStructureData.welcomeContext = this;
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        checkDeviceRootAccess();
        ImageView ivLogo = findViewById(R.id.ivLogo);
        TextView tvDescription = findViewById(R.id.tvDescription);
        if (BuildConfig.FLAVOR.equals("medplat")) {
            ivLogo.setImageResource(R.drawable.medplat_argus);
            tvDescription.setText(R.string.techo_description_eng);
        }
    }

    public void checkDeviceRootAccess() {
        if (RootUtil.isDeviceRooted()) {
            showAlertAndExit(LabelConstants.NOT_ALLOW_ON_ROOTED_DEVICE_ALERT);
        } else {
            checkPermissions();
        }
    }

    @UiThread
    public void showAlertAndExit(final String msg) {
        hideProcessDialog();
        View.OnClickListener listener = v -> {
            dialogForExit.dismiss();
            android.os.Process.killProcess(android.os.Process.myPid());
            Runtime.getRuntime().exit(0);
        };
        dialogForExit = new MyAlertDialog(this, false, msg, listener, DynamicUtils.BUTTON_OK);
        dialogForExit.show();
    }

    @Background
    public void checkPermissions() {
        List<String> list = new ArrayList<>();
        String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                Manifest.permission.RECORD_AUDIO,
                //Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.VIBRATE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WAKE_LOCK,
        };
        Map<String, Integer> map = new HashMap<>();
        for (String s : permissions) {
            map.put(s, PermissionChecker.checkSelfPermission(getApplicationContext(), s));
        }

        for (Map.Entry<String, Integer> permission : map.entrySet()) {
            if (permission.getValue() != PackageManager.PERMISSION_GRANTED) {
                list.add(permission.getKey());
            }
        }

        if (!list.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    list.toArray(new String[0]),
                    MY_PERMISSIONS_REQUEST);
        } else {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    // Permission was denied!
                    View.OnClickListener listener = v -> {
                        alertDialog.dismiss();
                        System.exit(0);
                    };

                    alertDialog = new MyAlertDialog(this, false,
                            LabelConstants.ALLOW_ALL_PERMISSIONS_TO_RUN,
                            listener, DynamicUtils.BUTTON_OK);
                    alertDialog.show();
                    return;
                }
            }
        }
        init();
    }

    private void init() {
        createDirectories();
        hideProcessDialog();
        switchToLoginActivity();
    }

    private boolean isFileDirExistsInAndroidData() {
        String path = Objects.requireNonNull(getExternalFilesDir(null)).getAbsolutePath() + SewaConstants.DIR_DATABASE;
        File file = new File(path);
        return file.exists();
    }

    private void createDirectories() {
        if (isFileDirExistsInAndroidData()) {
            // Files Dir was already created - no need to do anything
            return;
        }
        createAllDirectoryInsideAndroidData();
    }

    private void createAllDirectoryInsideAndroidData() {
        createDirIfNotExists(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_IMAGE));
        createDirIfNotExists(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_AUDIO));
        createDirIfNotExists(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_VIDEO));
        createDirIfNotExists(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_DATABASE));
        createDirIfNotExists(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_DOWNLOADED));
        createDirIfNotExists(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED));
        createDirIfNotExists(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_APK_DOWNLOADED_TEMP));
        createDirIfNotExists(SewaConstants.getDirectoryPath(context, SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LIBRARY)));
        createDirIfNotExists(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LIBRARY_TEMP));
        createDirIfNotExists(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_BOOKMARK));
        createDirIfNotExists(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_LMS));
        createDirIfNotExists(SewaConstants.getDirectoryPath(context, SewaConstants.DIR_PDF));
    }

    private void createDirIfNotExists(String path) {
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.w(getClass().getSimpleName(), file.getAbsolutePath() + " is not created!");
            } else {
                Log.i(getClass().getSimpleName(), file.getAbsolutePath() + " is created!");
            }
        } else {
            Log.i(getClass().getSimpleName(), file.getAbsolutePath() + " is already created!");
        }
    }

    @UiThread
    public void switchToLoginActivity() {
        new Handler().postDelayed(() -> {
            Intent loginIntent = new Intent(WelcomeActivity.this, LoginActivity_.class);
            loginIntent.putExtra(LabelConstants.REDIRECT_TO, notificationData);
            startActivity(loginIntent);
            finish();
        }, WELCOME_TIME_OUT);
    }
}
