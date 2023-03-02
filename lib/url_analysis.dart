class UrlAnalysis {
  static String analysis(String urlString) {
    final encodeUrlString = Uri.encodeFull(urlString);
    final uri = Uri.parse(encodeUrlString);
    Map<String, dynamic> params = {};

    for (var key in uri.queryParameters.keys) {
      if (uri.queryParameters[key] != null) {
        String? result = uri.queryParameters[key];
        if (UrlAnalysis.isContainChinese(result ?? '') == true) {
          params[key] = result;
        } else {
          String? decodeString = Uri.decodeFull(result ?? '');
          if (decodeString == 'true') {
            params[key] = true;
          } else if (decodeString == 'false') {
            params[key] = false;
          } else {
            params[key] = decodeString;
          }
        }
      }
    }
    print('actionUrl = $urlString \n host = ${uri.host} \n params = $params');

    return params['target'] ?? urlString;
  }

  // 是否包含中文
  static bool isContainChinese(String input) {
    if (input.isEmpty) return false;
    return RegExp('[\u4e00-\u9fa5]').hasMatch(input);
  }
}

class UriInfo {
  late String host;
  late Map<String, dynamic>? params;

  UriInfo({this.host = '', this.params});
}
