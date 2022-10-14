package com.wdit.traffic_flutter.sdk;

import android.app.Application;
import android.content.Context;

public class ApplicationHolder {

    private static Application application = null;


    public static Application getApplication() {
        return application;
    }

    public static void setApplication(Application application) {
        ApplicationHolder.application = application;

    }

    public static Context getContext() {
        return application;
    }



}
