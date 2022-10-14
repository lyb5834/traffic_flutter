import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'package:traffic_flutter/sdk_data_cache.dart';
import 'package:traffic_flutter/url_analysis.dart';

class TrafficFlutter {

  static final TrafficFlutter _instance = TrafficFlutter._internal();

  factory TrafficFlutter() => _instance;

  TrafficFlutter._internal();


  @visibleForTesting
  final methodChannel = const MethodChannel('traffic_flutter');

   Future<bool> initSdk({
    required String uuid,
    required String appVersion,
    String? userId='',
    String? location='',
  }) async {
    SDKDataCache().uuid = uuid;
    SDKDataCache().appVersion = appVersion;
    SDKDataCache().userId = userId;
    SDKDataCache().location = location;
    setUUID();
    setAppVersionName();
    return true;
  }

  setUserId({required String userId}) {
    SDKDataCache().userId = userId;
  }

  setUUID() {
    methodChannel.invokeMethod<String>('setUUID', SDKDataCache().uuid);
  }

  setAppVersionName() {
    methodChannel.invokeMethod<String>(
        'setAppVersionName', SDKDataCache().appVersion);
  }

  setLocation({required String longitude, required String latitude}) {
    SDKDataCache().location = '$longitude , $latitude';
    methodChannel.invokeMethod<String>('setLocation', SDKDataCache().location);
  }

  Future<String?> screenPath(
      {required List<String> title, required String path}) async {
    Map<String, dynamic> map = {
      "userId": SDKDataCache().userId,
      "title": title,
      "path": UrlAnalysis.analysis(path),
    };
    return await methodChannel.invokeMethod<String>('screenPath', map);
  }

  Future<String?> screen({required List<String> title}) async {
    Map<String, dynamic> map = {
      "userId": SDKDataCache().userId,
      "title": title,
    };
    return await methodChannel.invokeMethod<String>('screen', map);
  }

  Future<String?> event(
      {required List<String> category, required List<String> action}) async {
    Map<String, dynamic> map = {
      "userId": SDKDataCache().userId,
      "category": category,
      "action": action,
    };
    return await methodChannel.invokeMethod<String>('event', map);
  }

  Future<String?> search(
      {required String keyboard,
      required List<String> category,
      required int count}) async {
    Map<String, dynamic> map = {
      "userId": SDKDataCache().userId,
      "keyboard": keyboard,
      "category": category,
      "count": count.toString(),
    };
    return await methodChannel.invokeMethod<String>('search', map);
  }
}
