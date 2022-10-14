package com.wdit.traffic_flutter;

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
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "traffic_flutter");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("setUUID")) {
            TrafficUtils.setUuid(call.arguments.toString());

        } else if (call.method.equals("setAppVersionName")) {
            TrafficUtils.setAppVersionName(call.arguments.toString());

        } else if (call.method.equals("setLocation")) {
            TrafficUtils.setLocation(call.arguments.toString());

        } else if (call.method.equals("screenPath")) {
            if (call.arguments instanceof Map) {
                Map<String, Object> map = (Map) call.arguments;
                TrafficUtils.screenPath(map.get("userId").toString(), (List<String>)map.get("title"), map.get("path").toString());
            }
        } else if (call.method.equals("screen")) {
            if (call.arguments instanceof Map) {
                Map<String, Object> map = (Map) call.arguments;
                TrafficUtils.screen(map.get("userId").toString(), (List<String>) map.get("title"));
            }

        } else if (call.method.equals("event")) {
            if (call.arguments instanceof Map) {
                Map<String, Object> map = (Map) call.arguments;
                TrafficUtils.event(map.get("userId").toString(), (List<String>)map.get("category"), (List<String>)map.get("action"));
            }
        } else if (call.method.equals("search")) {
            if (call.arguments instanceof Map) {
                Map<String, Object> map = (Map) call.arguments;
                TrafficUtils.search(map.get("userId").toString(), map.get("keyboard").toString(), (List<String>)map.get("category"), Integer.parseInt(map.get("count").toString()));
            }
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

}
