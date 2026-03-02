package com.argusoft.sewa.android.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.argusoft.sewa.android.app.service.GPSTracker;

/**
 * @author abhipsa
 */
public class BootingReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.matches("android.location.PROVIDERS_CHANGED")) {
            Intent pushIntent = new Intent(context, GPSTracker.class);
            context.startService(pushIntent);
        }
    }
}
