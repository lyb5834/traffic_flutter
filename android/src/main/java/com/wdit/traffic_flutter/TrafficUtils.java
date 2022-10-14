package com.wdit.traffic_flutter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.wdit.traffic_flutter.sdk.TrackMe;
import com.wdit.traffic_flutter.sdk.Tracker;
import com.wdit.traffic_flutter.sdk.TrackerBuilder;
import com.wdit.traffic_flutter.sdk.Traffic;
import com.wdit.traffic_flutter.sdk.extra.CustomVariables;
import com.wdit.traffic_flutter.sdk.extra.TrackHelper;

import java.util.List;

public class TrafficUtils {
    public static final String TAG = "TrafficUtils";
    private static Tracker tracker;

    private static Tracker getTracker() {
        return tracker;
    }

    public static void createTracker(Context context, String url, int siteId) {
        if (tracker == null) {
            tracker = TrackerBuilder.createDefault(url, siteId).build(Traffic.getInstance(context));
        }
    }

    /**
     * 统一打印埋点日志，方便查看，仅供测试使用
     */
    public static void trafficLog(String val) {
        Log.d(TAG, val);
    }


    public static void screenPath(String userId, List<String> title, String path) {
        try {
            Tracker tracker = TrafficUtils.getTracker();
            if (!TextUtils.isEmpty(userId)) {
                tracker.setUserId(userId);
            }
            TrackHelper.track(createCustomVariables())
                    .screen(TextUtils.isEmpty(path) ? " " : path)
                    .title(getListFormat(title))
                    .with(tracker);
            tracker.dispatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        trafficLog("screenPath -- " + title + "   " + path);
    }

    public static void screen(String userId, List<String> title) {
        try {
            Tracker tracker = TrafficUtils.getTracker();
            if (!TextUtils.isEmpty(userId)) {
                tracker.setUserId(userId);
            }
            TrackHelper.track(createCustomVariables())
                    .screen(" ")
                    .title(getListFormat(title))
                    .with(tracker);
            tracker.dispatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        trafficLog("screen -- " + getListFormat(title));
    }


    public static void event(String userId, List<String> category, String action) {
        try {
            Tracker tracker = TrafficUtils.getTracker();
            if (!TextUtils.isEmpty(userId)) {
                tracker.setUserId(userId);
            }
            TrackHelper.track(createCustomVariables())
                    .event(getListFormat(category), action)
                    .with(tracker);
            tracker.dispatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        trafficLog("event -- " + getListFormat(category) + "-" + action);
    }


    public static void search(String userId, String searchKey, List<String> category, int count) {
        try {
            Tracker tracker = TrafficUtils.getTracker();
            if (!TextUtils.isEmpty(userId)) {
                tracker.setUserId(userId);
            }
            TrackHelper.track(createCustomVariables())
                    .search(TextUtils.isEmpty(searchKey) ? " " : searchKey)
                    .category(getListFormat(category))
                    .count(count)
                    .with(tracker);
            tracker.dispatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        trafficLog("search -- searchKey " + searchKey + " category " + getListFormat(category) + " count " + count);
    }


    /**
     * 启动埋点
     * 1. 去掉启动次数埋点
     * 2. 更改经纬度 版本号 uuid的埋点方式，由原来的添加页面方式改为访问记录的自定义变量，具体如下:
     * sdk版本：matomo-sdk-android:4.1.4
     * eg:
     * <p>
     * TrackHelper.track(new CustomVariables()
     * // visit级别变量
     * .put(1, "uuid", UUID)
     * .put(2, "appversion", appVersion)
     * .toVisitVariables())
     * .screen("/custom_vars")
     * .title("Custom Vars")
     * // page级别变量
     * .variable(1, "first", "var")
     * .variable(2, "second", "long value")
     * .with(getTracker);
     * <p>
     * 3.添加顺序：1.uuid、2.版本号、3.经纬度、
     */

    public static TrackMe createCustomVariables() {
        CustomVariables customVariables = new CustomVariables();
        // visit级别变量
        customVariables.put(1, "uuid", getUuid());
        customVariables.put(2, "appversion", String.format("V%s", getAppVersionName()));
        if (!TextUtils.isEmpty(getLocation())) {
            customVariables.put(3, "经纬度", getLocation());
        }
        return customVariables.toVisitVariables();

    }

    private static String uuid;
    private static String appVersionName;
    private static String location;

    public static void setUuid(String uuid) {
        TrafficUtils.uuid = uuid;
    }

    private static String getUuid() {
        return uuid;
    }

    public static void setAppVersionName(String appVersionName) {
        TrafficUtils.appVersionName = appVersionName;
    }

    private static String getAppVersionName() {
        return appVersionName;
    }


    public static void setLocation(String location) {
        TrafficUtils.location = location;
    }

    private static String getLocation() {
        return location;
    }


    private static String getListFormat(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (list != null && list.size() > 0) {
            int size = list.size();
            if (1 == size) {
                stringBuilder.append(list.get(0));
                return stringBuilder.toString();
            }
            for (int i = 0; i < size; i++) {
                if (size - 1 == i) {
                    stringBuilder.append(list.get(i));
                } else {
                    stringBuilder.append(list.get(i)).append("/");
                }
            }
        }
        return stringBuilder.toString();
    }
}
