package com.wdit.traffic_flutter;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * TrafficFlutterPlugin
 */
public class TrafficFlutterPlugin implements FlutterPlugin, MethodCallHandler {

    private MethodChannel channel;
    private Context applicationContext;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "traffic_flutter");
        channel.setMethodCallHandler(this);
        applicationContext = flutterPluginBinding.getApplicationContext();
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel = null;
        applicationContext = null;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("register")) {
            if (call.arguments instanceof Map) {
                Map<String, Object> map = (Map) call.arguments;
                TrafficUtils.createTracker(applicationContext, map.get("trafficUrl").toString(), Integer.parseInt(map.get("trafficId").toString()));
            }

        } else if (call.method.equals("setUUID")) {
            TrafficUtils.setUuid(call.arguments.toString());

        } else if (call.method.equals("setAppVersionName")) {
            TrafficUtils.setAppVersionName(call.arguments.toString());

        } else if (call.method.equals("setLocation")) {
            TrafficUtils.setLocation(call.arguments.toString());

        } else if (call.method.equals("screenPath")) {
            if (call.arguments instanceof Map) {
                Map<String, Object> map = (Map) call.arguments;
                TrafficUtils.screenPath(map.get("userId").toString(), (List<String>) map.get("title"), map.get("path").toString());
            }
        } else if (call.method.equals("screen")) {
            if (call.arguments instanceof Map) {
                Map<String, Object> map = (Map) call.arguments;
                TrafficUtils.screen(map.get("userId").toString(), (List<String>) map.get("title"));
            }

        } else if (call.method.equals("event")) {
            if (call.arguments instanceof Map) {
                Map<String, Object> map = (Map) call.arguments;
                TrafficUtils.event(map.get("userId").toString(), (List<String>) map.get("category"), map.get("action").toString());
            }
        } else if (call.method.equals("search")) {
            if (call.arguments instanceof Map) {
                Map<String, Object> map = (Map) call.arguments;
                TrafficUtils.search(map.get("userId").toString(), map.get("searchKey").toString(), (List<String>) map.get("category"), Integer.parseInt(map.get("count").toString()));
            }
        } else {
            result.notImplemented();
        }
    }


//    public static Map<String, Object> getObjectFormat(Object obj) throws IllegalAccessException {
//        Map<String, Object> map = new LinkedHashMap<String, Object>();
//        Class<?> clazz = obj.getClass();
//        System.out.println(clazz);
//        for (Field field : clazz.getDeclaredFields()) {
//            field.setAccessible(true);
//            String fieldName = field.getName();
//            Object value = field.get(obj);
//            if (value == null){
//                value = "";
//            }
//            map.put(fieldName, value);
//        }
//        return map;
//    }
}
