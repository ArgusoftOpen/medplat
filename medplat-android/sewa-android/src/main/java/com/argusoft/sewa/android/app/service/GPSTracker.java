package com.argusoft.sewa.android.app.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;

import com.argusoft.sewa.android.app.BuildConfig;
import com.argusoft.sewa.android.app.component.MyAlertDialog;
import com.argusoft.sewa.android.app.constants.LabelConstants;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.util.DynamicUtils;
import com.argusoft.sewa.android.app.util.GlobalTypes;
import com.argusoft.sewa.android.app.util.Log;

/**
 * @author abhipsa
 */

public final class GPSTracker extends Service implements LocationListener {

    private static final String TAG = "GPSTracker";
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // frequent update
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 30L; // update within 30 minutes
    public static Double latitude = 0.0; // latitude
    public static Double longitude = 0.0; // longitude
    private static Boolean isGPSEnabled;
    private static Boolean isNetworkEnabled;
    // Declaring a Location Manager
    private LocationManager locationManager;
    private MyAlertDialog alertDialog;

    private void registerLocationManager() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (isLocationProviderEnabled()) {
            if (Boolean.TRUE.equals(isGPSEnabled)) {
                try {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                } catch (SecurityException e) {
                    Log.e(TAG, null, e);
                }
            } else if (Boolean.TRUE.equals(isNetworkEnabled)) {
                try {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                } catch (SecurityException e) {
                    Log.e(TAG, null, e);
                }
            }
        } else {
            stopUsingLocationUpdates();
        }
    }

    public void getLocation() {
        // First get location from Network Provider
        // location
        Location location = null;
        if (locationManager != null) {
            try {
                if (Boolean.TRUE.equals(isNetworkEnabled)) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                if (location == null && Boolean.TRUE.equals(isGPSEnabled)) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            } catch (SecurityException e) {
                Log.e(getClass().getSimpleName(), null, e);
            }

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                System.out.println("lat =="+latitude +"   lng =="+longitude);
            }
        }
    }

    /**
     * Stop using GPS listener [Calling this function will stop using GPS in
     * your app]
     */
    private void stopUsingLocationUpdates() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    //method for checking gps/network availability for location update.
    public boolean isLocationProviderEnabled() {
        //getting gps status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return (isGPSEnabled || isNetworkEnabled);
    }

    public void showSettingsAlert(final Context context) {
        View.OnClickListener onClickListener = v -> {
            //show LocationServices to enable Location Setting through Network Provider
            alertDialog.dismiss();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            ((Activity) context).startActivityForResult(intent, GlobalTypes.LOCATION_SERVICE_ACTIVITY); // start system activity from dynamic form activity to check result
        };

        if (BuildConfig.FLAVOR.equals(GlobalTypes.UTTARAKHAND_FLAVOR)) {
            alertDialog = new MyAlertDialog(context, false,
                    LabelConstants.GPS_SERVICE_NOT_STARTED_ALERT_CHARDHAM,
                    onClickListener, DynamicUtils.BUTTON_OK);
        } else {
            alertDialog = new MyAlertDialog(context, false,
                    LabelConstants.GPS_SERVICE_NOT_STARTED_ALERT,
                    onClickListener, DynamicUtils.BUTTON_OK);
        }

        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        //onLocationChanged
    }

    @Override
    public void onProviderDisabled(String provider) {
        //onProviderDisabled
    }

    @Override
    public void onProviderEnabled(String provider) {
        //onProviderEnabled
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //onStatusChanged
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        try {
            registerLocationManager();
        } catch (SecurityException e) {
            Log.e(getClass().getSimpleName(), null, e);
        }
        SharedStructureData.gps = this;
    }
}
