/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app;

import android.app.Application;
import android.os.PowerManager;

import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;
import com.argusoft.sewa.android.app.util.WSConstants;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import org.androidannotations.annotations.EApplication;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * @author kelvin
 */
@EApplication
public class SewaApplication extends Application {

    private UncaughtExceptionHandler defaultUEH;

    PowerManager.WakeLock screenOnWakeLock;

    public SewaApplication() {
        Log.d("APPS_INSTALLED", "Configurations Called");
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        UncaughtExceptionHandler uncaughtExceptionHandler = (thread, ex) -> {
            SharedStructureData.sewaService.storeException(ex, GlobalTypes.EXCEPTION_TYPE_UNHANDLED);
            OpenHelperManager.releaseHelper();
            defaultUEH.uncaughtException(thread, ex);
        };
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("APPS_INSTALLED", "onCreate of Main");
        WSConstants.setLiveContextUrl();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        screenOnWakeLock =  powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,"SewaApp:ScreenlockTag");
        screenOnWakeLock.acquire();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        OpenHelperManager.releaseHelper();
    }
}
