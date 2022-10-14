package com.wdit.traffic_flutter_example;

import com.wdit.traffic_flutter.sdk.ApplicationHolder;
import com.wdit.traffic_flutter.sdk.TrackerBuilder;
import com.wdit.traffic_flutter.sdk.extra.TrafficApplication;

public class App extends TrafficApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationHolder.setApplication(this);
    }

    @Override
    public TrackerBuilder onCreateTrackerConfig() {
        return TrackerBuilder.createDefault("https://gl.ewdcloud.com/traffic.php",9527);
    }
}
