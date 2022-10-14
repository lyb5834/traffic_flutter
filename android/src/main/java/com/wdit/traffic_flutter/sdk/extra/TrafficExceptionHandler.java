package com.wdit.traffic_flutter.sdk.extra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wdit.traffic_flutter.sdk.TrackMe;
import com.wdit.traffic_flutter.sdk.Tracker;
import com.wdit.traffic_flutter.sdk.Traffic;

/**
 * An exception handler that wraps the existing exception handler and dispatches event to a {@link Tracker}.
 * <p>
 * Also see documentation for {@link TrackHelper#uncaughtExceptions()}
 */
public class TrafficExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = Traffic.tag(TrafficExceptionHandler.class);
    private final Tracker mTracker;
    private final TrackMe mTrackMe;
    private final Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    public TrafficExceptionHandler(@NonNull Tracker tracker, @Nullable TrackMe trackMe) {
        mTracker = tracker;
        mTrackMe = trackMe;
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public Tracker getTracker() {
        return mTracker;
    }

    /**
     * This will give you the previous exception handler that is now wrapped.
     */
    public Thread.UncaughtExceptionHandler getDefaultExceptionHandler() {
        return mDefaultExceptionHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            String excInfo = ex.getMessage();
            TrackHelper.track(mTrackMe).exception(ex).description(excInfo).fatal(true).with(getTracker());
            // Immediately dispatch as the app might be dying after rethrowing the exception
            getTracker().dispatch();
        } catch (Exception e) {
            //Timber.tag(TAG).e(e, "Couldn't track uncaught exception");
        } finally {
            // re-throw critical exception further to the os (important)
            if (getDefaultExceptionHandler() != null && getDefaultExceptionHandler() != this) {
                getDefaultExceptionHandler().uncaughtException(thread, ex);
            }
        }
    }
}
