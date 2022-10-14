package com.wdit.traffic_flutter.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.wdit.traffic_flutter.sdk.dispatcher.DefaultDispatcherFactory;
import com.wdit.traffic_flutter.sdk.dispatcher.DispatcherFactory;
import com.wdit.traffic_flutter.sdk.tools.BuildInfo;
import com.wdit.traffic_flutter.sdk.tools.Checksum;
import com.wdit.traffic_flutter.sdk.tools.DeviceHelper;
import com.wdit.traffic_flutter.sdk.tools.PropertySource;

import java.util.HashMap;
import java.util.Map;


public class Traffic {
    public static final String LOGGER_PREFIX = "MATOMO:";
    private static final String TAG = Traffic.tag(Traffic.class);
    private static final String BASE_PREFERENCE_FILE = "org.traffic.sdk";

    @SuppressLint("StaticFieldLeak") private static Traffic sInstance;

    private final Map<Tracker, SharedPreferences> mPreferenceMap = new HashMap<>();
    private final Context mContext;
    private final SharedPreferences mBasePreferences;
    private DispatcherFactory mDispatcherFactory = new DefaultDispatcherFactory();

    public static synchronized Traffic getInstance(Context context) {
        if (sInstance == null) {
            synchronized (Traffic.class) {
                if (sInstance == null) sInstance = new Traffic(context);
            }
        }
        return sInstance;
    }

    private Traffic(Context context) {
        mContext = context.getApplicationContext();
        mBasePreferences = context.getSharedPreferences(BASE_PREFERENCE_FILE, Context.MODE_PRIVATE);
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * Base preferences, tracker idenpendent.
     */
    public SharedPreferences getPreferences() {
        return mBasePreferences;
    }

    /**
     * @return Tracker specific settings object
     */
    public SharedPreferences getTrackerPreferences(@NonNull Tracker tracker) {
        synchronized (mPreferenceMap) {
            SharedPreferences newPrefs = mPreferenceMap.get(tracker);
            if (newPrefs == null) {
                String prefName;
                try {
                    prefName = "org.traffic.sdk_" + Checksum.getMD5Checksum(tracker.getName());
                } catch (Exception e) {
                    //Timber.tag(TAG).e(e);
                    prefName = "org.traffic.sdk_" + tracker.getName();
                }
                newPrefs = getContext().getSharedPreferences(prefName, Context.MODE_PRIVATE);
                mPreferenceMap.put(tracker, newPrefs);
            }
            return newPrefs;
        }
    }

    /**
     * If you want to use your own {@link com.wdit.traffic.sdk.dispatcher.Dispatcher}
     */
    public void setDispatcherFactory(DispatcherFactory dispatcherFactory) {
        this.mDispatcherFactory = dispatcherFactory;
    }

    public DispatcherFactory getDispatcherFactory() {
        return mDispatcherFactory;
    }

    DeviceHelper getDeviceHelper() {
        return new DeviceHelper(mContext, new PropertySource(), new BuildInfo());
    }

    public static String tag(Class... classes) {
        String[] tags = new String[classes.length];
        for (int i = 0; i < classes.length; i++) {
            tags[i] = classes[i].getSimpleName();
        }
        return tag(tags);
    }

    public static String tag(String... tags) {
        StringBuilder sb = new StringBuilder(LOGGER_PREFIX);
        for (int i = 0; i < tags.length; i++) {
            sb.append(tags[i]);
            if (i < tags.length - 1) sb.append(":");
        }
        return sb.toString();
    }
}
