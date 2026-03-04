package com.argusoft.sewa.android.app.rdservice;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class to help capture fingerprint data by using RDService drivers for fingerprint-devices.
 * @author https://github.com/manustays
 */
public class RDServiceManager {

    private static final String TAG = "RDServiceManager";
    private RDServiceEvents mRDEvent;

    private static final int RC_RDSERVICE_DISCOVER_START_INDEX = 8500;
    private static final int RC_RDSERVICE_CAPTURE_START_INDEX = 8300;

    private static final Map<String, Integer> mapRDDriverRCIndex = new HashMap<String, Integer>();
    private static final Map<Integer, String> mapRDDiscoverRC = new HashMap<Integer, String>();
    private static final Map<Integer, String> mapRDCaptureRC = new HashMap<Integer, String>();

    private static final Map<String, String> mapRDDriverWhitelist = new HashMap<String, String>() {
        {
            put("com.secugen.rdservice", "Secugen");
            put("com.scl.rdservice", "Morpho");
            put("com.mantra.rdservice", "Mantra");
            put("com.acpl.registersdk", "Startek FM220");
            put("com.rd.gemalto.com.rdserviceapp", "Gemalto 3M Cogent CSD200");

            put("com.tatvik.bio.tmf20", "Tatvik TMF20");
            put("com.evolute.rdservice", "Evolute");
            put("com.precision.pb510.rdservice", "PB510");
            put("com.mantra.mis100v2.rdservice", "MIS100V2 by Mantra");
            put("com.nextbiometrics.rdservice", "NEXT Biometrics NB-3023");
            put("com.iritech.rdservice", "IriTech IriShield");
        }
    };

    private static final Map<String, String> mapRDDriverBlacklist = new HashMap<String, String>();


    private RDServiceManager(@NonNull final RDServiceManager.Builder builder) {
        mRDEvent = builder._rdevent;
        mapRDDriverWhitelist.putAll(builder.mapNewWhitelistedRDDrivers);
        mapRDDriverBlacklist.putAll(builder.mapBlacklistedRDDrivers);
    }


    /**
     * Builder class to create a configured instance of RDServiceManager.
     */
    public static class Builder {
        private RDServiceEvents _rdevent = null;
        private Map<String, String> mapNewWhitelistedRDDrivers = new HashMap<String, String>();
        private Map<String, String> mapBlacklistedRDDrivers = new HashMap<String, String>();

        /**
         * Constructor for the builder.
         *
         * @param eventActivity Reference to Activity that has implemented RDServiceEvents interface so that it can receive callback when RDService drivers are discovered or when fingerprint is captured.
         */
        public Builder(@NonNull final RDServiceEvents eventActivity) {
            _rdevent = eventActivity;
        }

        /**
         * Whitelist one or more RDService drivers. The response after driver discovery will contain a flag whether the discovered driver is whitelisted or not. However, it does not stop you from using a driver that is not already whitelisted. Note that a few popular drivers (eg: Morpho, Mantra, Secugen, Startek, 3M) are already whitelisted.
         * @param mapNewWhitelistedRDDrivers A Map of new whitelisted drivers containing PlayStore package names and an optional label for each driver.
         * @return A reference to this builder to easily chain other builder methods.
         */
        public RDServiceManager.Builder whitelistRDDrivers(@NonNull final Map<String, String> mapNewWhitelistedRDDrivers) {
            this.mapNewWhitelistedRDDrivers = mapNewWhitelistedRDDrivers;
            return this;
        }

        /**
         * Blacklist one or more RDService drivers. These drivers will be ignored during driver discovery and will not be allowed to capture from.
         * @param mapBlacklistedRDDrivers A Map of blacklisted drivers containing PlayStore package names and an optional label for each driver.
         * @return A reference to this builder to easily chain other builder methods.
         */
        public RDServiceManager.Builder blacklistRDDrivers(@NonNull final Map<String, String> mapBlacklistedRDDrivers) {
            this.mapBlacklistedRDDrivers = mapBlacklistedRDDrivers;
            return this;
        }

        /**
         * Create and return a configured instance of RDServiceManager.
         * @return An instance of RDServiceManager.
         */
        public RDServiceManager create() {
            if (_rdevent == null) {
                throw new IllegalStateException("First set your Activity that implements RDServiceEvent by calling setRDServiceEventActivity()");
            }
            return new RDServiceManager(this);
        }
    }


    /**
     * Dispatch onActivityResult here from the implementing Activity.
     */
    public void onActivityResult(@NonNull int requestCode, @NonNull int resultCode, @NonNull Intent data) throws JSONException {

        Log.i(TAG, "onActivityResult: " + requestCode + ", " + resultCode + " ~~ " + mapRDDiscoverRC.toString());

        if (mapRDDiscoverRC.containsKey(requestCode)) {
            String rdservice_pkg_name = mapRDDiscoverRC.get(requestCode);
            if (resultCode == RESULT_OK) {
                onRDServiceInfoResponse(data, rdservice_pkg_name);  // RDService Info Received
            } else {
                mRDEvent.onRDServiceDriverDiscoveryFailed(resultCode, data, rdservice_pkg_name, "");    // RDService Info Failed
            }
        } else if (mapRDCaptureRC.containsKey(requestCode)) {
            String rdservice_pkg_name = mapRDCaptureRC.get(requestCode);
            if (resultCode == RESULT_OK) {
                onRDServiceCaptureIntentResponse(data, rdservice_pkg_name);  // Fingerprint Captured
            } else {
                mRDEvent.onRDServiceCaptureFailed(resultCode, data, rdservice_pkg_name);    // Fingerprint Capture Failed
            }
        }
    }


    /**
     * Initiate discovery of installed RDService drivers on current device. For every discovered driver, the onRDServiceDriverDiscovery() will be called.
     */
    public void discoverRdService() {
        Intent intentServiceList = new Intent("in.gov.uidai.rdservice.fp.INFO");
        List<ResolveInfo> resolveInfoList = ((Activity) mRDEvent).getPackageManager().queryIntentActivities(intentServiceList, 0);

        // String packageNamesStr = "";
        if (resolveInfoList.isEmpty()) {
            mRDEvent.onRDServiceDriverNotFound();
            return;
        }

        int iInfo = 0;
        for (ResolveInfo resolveInfo : resolveInfoList) {
            String _pkg = resolveInfo.activityInfo.packageName;

            if (!mapRDDriverBlacklist.containsKey(_pkg)) {
                try {
                    // Assign an index to current RDService driver
                    int next_rdservice_index = mapRDDriverRCIndex.size() + 1;
                    mapRDDriverRCIndex.put(_pkg, next_rdservice_index);

                    // Calculate and map request-code for the current RDService GetInfo Intent
                    int next_discover_rc_index = getRDServiceDiscoverRC(next_rdservice_index);
                    mapRDDiscoverRC.put(next_discover_rc_index, _pkg);

                    // Calculate and map request-code for the current RDService Capture Intent
                    int next_capture_rc_index = getRDServiceCaptureRC(next_rdservice_index);
                    mapRDCaptureRC.put(next_capture_rc_index, _pkg);

                    // Get RD Service Info..
                    Intent intentInfo = new Intent("in.gov.uidai.rdservice.fp.INFO");
                    intentInfo.setPackage(_pkg);
                    ((Activity) mRDEvent).startActivityForResult(intentInfo, next_discover_rc_index);

                    Log.e(TAG, "RD SERVICE Package Found: (" + next_rdservice_index + ") " + next_discover_rc_index + " ~ " + _pkg + " ~~ " + mapRDDriverRCIndex + " ~ " + mapRDDiscoverRC);
                } catch (Exception e) {
                    e.printStackTrace();
                    mRDEvent.onRDServiceDriverDiscoveryFailed(0, null, _pkg, e.getMessage());
                }
            } else {
                mRDEvent.onRDServiceDriverDiscoveryFailed(0, null, _pkg, "Package not whitelisted");
            }


            ++iInfo;

            // Limit max installed driver discovery count
            if (iInfo > 10) {
                break;
            }
        }
    }


    /**
     * Initiate fingerprint capture.
     * @param rd_service_package The package name of the active RDService driver to be used for capture.
     * @param pid_options The PID-Options XML string to configure the RDService driver as per Aadhaar Registered Devices Specification v2.0 by UIDAI (https://uidai.gov.in/images/resource/Aadhaar_Registered_Devices_2_0_4.pdf).
     */
    public void captureRdService(@NonNull String rd_service_package, @NonNull String pid_options) {

        if (mapRDDriverRCIndex.containsKey(rd_service_package)) {
            int capture_rc_index = mapRDDriverRCIndex.get(rd_service_package);
            int capture_rc = getRDServiceCaptureRC(capture_rc_index);

            Log.d(TAG, "RDSERVICE BEFORE CAPTURE: pid_options: (" + capture_rc_index + ") " + capture_rc + " ~ " + pid_options);

            // Capture fingerprint using RD Service
            Intent intentCapture = new Intent("in.gov.uidai.rdservice.fp.CAPTURE");
            intentCapture.setPackage(rd_service_package);
            intentCapture.putExtra("PID_OPTIONS", pid_options);
            ((Activity) mRDEvent).startActivityForResult(intentCapture, capture_rc);
        } else {
            mRDEvent.onRDServiceDriverDiscoveryFailed(0, null, rd_service_package, "Package not found or not whitelisted");
            Log.d(TAG, "RDSERVICE CAPTURE ERROR: package not found or not whitelisted: " + rd_service_package);
        }
    }


    /**
     * Process response RDService driver status info.
     * TODO: return parsed data
     * @param data Intent response returned from RDService driver activity
     * @param rd_service_package The package name of the RDService driver
     */
    private void onRDServiceInfoResponse(@NonNull Intent data, @NonNull String rd_service_package) throws JSONException {

        Bundle b = data.getExtras();

        if (b != null) {
            // sendWebViewResponse("rdservice_info", b.getString("RD_SERVICE_INFO", "") + "<RD_SERVICE_ANDROID_PACKAGE=\"" + rd_service_package + "\" />");

            Log.d(TAG, "onRDServiceInfoResponse: " + b.getString("RD_SERVICE_INFO", "") + " //// " + rd_service_package);

            mRDEvent.onRDServiceDriverDiscovery(b.getString("RD_SERVICE_INFO", ""), rd_service_package, mapRDDriverWhitelist.containsKey(rd_service_package));

            Log.i(TAG, "onRDServiceInfoResponse: Device Info: \n\n Device = " + b.getString("DEVICE_INFO", "") + "    \n\nRDService = " + b.getString("RD_SERVICE_INFO", ""));
        }
    }


    /**
     * Process response RDService driver status info.
     * @param data Intent response returned from RDService driver activity
     * @param rd_service_package The package name of the RDService driver
     */
    private void onRDServiceCaptureIntentResponse(@NonNull Intent data, @NonNull String rd_service_package) throws JSONException {

        Bundle b = data.getExtras();

        if (b != null) {
            // sendWebViewResponse("rdservice_resp", b.getString("PID_DATA", ""));
            mRDEvent.onRDServiceCaptureResponse(b.getString("PID_DATA", ""), rd_service_package);

            Log.i(TAG, "onRDServiceCaptureIntentResponse: Capture Info: \n\n PID-DATA = " + b.getString("PID_DATA", "") + "    \n\nDeviceNotConnected = " + b.getString("DNC", ""));
        }
    }


    /**
     * Generate and return the next result-code for RDService driver discovery
     * @param index Index of the RDService driver. Usually last used index + 1.
     * @return The result-code
     */
    private int getRDServiceDiscoverRC(@NonNull int index) {
        return RC_RDSERVICE_DISCOVER_START_INDEX + index;
    }

    /**
     * Generate and return the next result-code for RDService fingerprint capture
     * @param index Index of the RDService driver. Usually last used index + 1.
     * @return The result-code
     */
    private int getRDServiceCaptureRC(@NonNull int index) {
        return RC_RDSERVICE_CAPTURE_START_INDEX + index;
    }

}

