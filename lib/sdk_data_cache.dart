class SDKDataCache {
  static final SDKDataCache _instance = SDKDataCache._internal();

  factory SDKDataCache() => _instance;

  SDKDataCache._internal();

  String? userId;
  String? location;
  bool debug = false;
}
