package com.wdit.traffic_flutter.sdk.extra;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.wdit.traffic_flutter.sdk.Traffic;

import java.util.Collections;
import java.util.List;


public class InstallReferrerReceiver extends BroadcastReceiver {
    private static final String TAG = Traffic.tag(InstallReferrerReceiver.class);

    // Google Play
    static final String REFERRER_SOURCE_GPLAY = "com.android.vending.INSTALL_REFERRER";
    static final String ARG_KEY_GPLAY_REFERRER = "referrer";

    static final String PREF_KEY_INSTALL_REFERRER_EXTRAS = "referrer.extras";
    static final List<String> RESPONSIBILITIES = Collections.singletonList(REFERRER_SOURCE_GPLAY);

    @Override
    public void onReceive(Context context, Intent intent) {
        //Timber.tag(TAG).d(intent.toString());
        if (intent.getAction() == null || !RESPONSIBILITIES.contains(intent.getAction())) {
            //Timber.tag(TAG).w("Got called outside our responsibilities: %s", intent.getAction());
            return;
        }
        if (intent.getBooleanExtra("forwarded", false)) {
            //Timber.tag(TAG).d("Dropping forwarded intent");
            return;
        }
        SharedPreferences preferences = Traffic.getInstance(context.getApplicationContext()).getPreferences();
        if (intent.getAction().equals(REFERRER_SOURCE_GPLAY)) {
            String referrer = intent.getStringExtra(ARG_KEY_GPLAY_REFERRER);
            if (referrer != null) {
                preferences.edit().putString(PREF_KEY_INSTALL_REFERRER_EXTRAS, referrer).apply();
                //Timber.tag(TAG).d("Stored Google Play referrer extras: %s", referrer);
            }
        }
        // Forward to other possible recipients
        intent.setComponent(null);
        intent.setPackage(context.getPackageName());
        intent.putExtra("forwarded", true);
        context.sendBroadcast(intent);
    }
}
