class SDKDataCache {
  static final SDKDataCache _instance = SDKDataCache._internal();

  factory SDKDataCache() => _instance;

  SDKDataCache._internal();

  String? uuid;
  String? userId;
  String? appVersion;
  String? location;
}
