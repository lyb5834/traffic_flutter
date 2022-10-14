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

  Future<bool> register(
      {required String trafficUrl,
      required int trafficId,
      required String uuid,
      required String appVersion,
      bool? debug,
      String? userId,
      String? longitude,
      String? latitude}) async {
    Map<String, dynamic> map = {
      "trafficUrl": trafficUrl,
      "trafficId": trafficId,
    };
    methodChannel.invokeMethod<String>('register', map);
    methodChannel.invokeMethod<String>('setUUID', uuid);
    methodChannel.invokeMethod<String>('setAppVersionName', appVersion);
    _init(userId: userId, longitude: longitude, latitude: latitude);
    SDKDataCache().debug = debug ?? false;

    _trafficLog('register trafficUrl: $trafficUrl '
        'trafficId: $trafficId '
        'uuid: $uuid '
        'appVersion: $appVersion '
        'userId: $userId '
        'longitude: $longitude '
        'latitude: $latitude '
        'debug: $debug '
    );
    return true;
  }

  _init({String? userId, String? longitude, String? latitude}) {
    if (userId != null && userId.isNotEmpty) {
      setUserId(userId: userId);
    }
    if (longitude != null &&
        longitude.isNotEmpty &&
        latitude != null &&
        latitude.isNotEmpty) {
      setLocation(longitude: longitude, latitude: latitude);
    }
  }

  _trafficLog(String msg) {
    if (SDKDataCache().debug) {
      print(msg);
    }
  }

  setUserId({required String userId}) {
    SDKDataCache().userId = userId;
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
    _trafficLog('screenPath '
        'userId: ${SDKDataCache().userId} '
        'title: $title '
        'path: $path '
        'path: ${UrlAnalysis.analysis(path)} '
    );
    return await methodChannel.invokeMethod<String>('screenPath', map);
  }

  Future<String?> screen({required List<String> title}) async {
    Map<String, dynamic> map = {
      "userId": SDKDataCache().userId,
      "title": title,
    };
    _trafficLog('screen '
        'userId: ${SDKDataCache().userId} '
        'title: $title '
    );

    return await methodChannel.invokeMethod<String>('screen', map);
  }

  Future<String?> event(
      {required List<String> category, required List<String> action}) async {
    Map<String, dynamic> map = {
      "userId": SDKDataCache().userId,
      "category": category,
      "action": action,
    };
    _trafficLog('event '
        'userId: ${SDKDataCache().userId} '
        'category: $category '
        'action: $action '
    );

    return await methodChannel.invokeMethod<String>('event', map);
  }

  Future<String?> search(
      {required String searchKey,
      required List<String> category,
      required int count}) async {
    Map<String, dynamic> map = {
      'userId: ${SDKDataCache().userId} '
      "searchKey": searchKey,
      "category": category,
      "count": count.toString(),
    };

    _trafficLog('event '
        'userId: ${SDKDataCache().userId} '
        'searchKey: $searchKey '
        'category: $category '
        'count: $count '
    );

    return await methodChannel.invokeMethod<String>('search', map);
  }
}
