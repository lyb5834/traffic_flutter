package com.wdit.traffic_flutter.sdk.dispatcher;

import com.wdit.traffic_flutter.sdk.Tracker;
import com.wdit.traffic_flutter.sdk.tools.Connectivity;

public class DefaultDispatcherFactory implements DispatcherFactory {
    public Dispatcher build(Tracker tracker) {
        return new DefaultDispatcher(
                new EventCache(new EventDiskCache(tracker)),
                new Connectivity(tracker.getTraffic().getContext()),
                new PacketFactory(tracker.getAPIUrl()),
                new DefaultPacketSender()
        );
    }
}
