package com.wdit.traffic_flutter.sdk.dispatcher;

import com.wdit.traffic_flutter.sdk.Tracker;

public interface DispatcherFactory {
    Dispatcher build(Tracker tracker);
}
