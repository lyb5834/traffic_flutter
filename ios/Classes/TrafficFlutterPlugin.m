#import "TrafficFlutterPlugin.h"
#import <TrafficClassSDK/TrafficClassSDK.h>

@interface TrafficFlutterPlugin ()
@property (nonatomic, copy) NSString * uuid;
@property (nonatomic, copy) NSString * version;
@property (nonatomic, copy) NSString * location;
@end

@implementation TrafficFlutterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"traffic_flutter"
            binaryMessenger:[registrar messenger]];
  TrafficFlutterPlugin* instance = [[TrafficFlutterPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"register" isEqualToString:call.method]) {
      NSDictionary * dic = call.arguments;
      NSString * trafficId = dic[@"trafficId"];
      [TrackerTool setSitId:trafficId];
      NSLog(@"register success : trafficId = %@",trafficId);
      
  } else if ([@"setUUID" isEqualToString:call.method]) {
      self.uuid = call.arguments;
      NSLog(@"setUUID = %@",call.arguments);
      
  } else if ([@"setAppVersionName" isEqualToString:call.method]) {
      self.version = call.arguments;
      NSLog(@"setAppVersionName = %@",call.arguments);
      
  } else if ([@"setLocation" isEqualToString:call.method]) {
      self.location = call.arguments;
      NSLog(@"setLocation = %@",call.arguments);
      
  } else if ([@"screenPath" isEqualToString:call.method]) {
      NSDictionary * dic = call.arguments;
      NSArray <NSString *>* titles = dic[@"title"];
      NSString * url = dic[@"path"];
      [TrackerTool addTrackView:titles variables:[self getTracks] url:[NSURL URLWithString:url]];
      NSLog(@"页面埋点 => %@-%@",[titles componentsJoinedByString:@"/"],url);
      
  } else if ([@"screen" isEqualToString:call.method]) {
      NSDictionary * dic = call.arguments;
      NSArray <NSString *>* titles = dic[@"title"];
      [TrackerTool addTrackView:titles variables:[self getTracks]];
      NSLog(@"页面埋点 => %@",[titles componentsJoinedByString:@"/"]);
      
  } else if ([@"event" isEqualToString:call.method]) {
      NSDictionary * dic = call.arguments;
      NSArray <NSString *>* categorys = dic[@"category"];
      NSString * action = dic[@"action"];
      [TrackerTool trackerEventCategory:[categorys componentsJoinedByString:@"/"] action:action variables:[self getTracks]];
      NSLog(@"事件埋点 => %@-%@",[categorys componentsJoinedByString:@"/"],action);
      
  } else if ([@"search" isEqualToString:call.method]) {
      NSDictionary * dic = call.arguments;
      NSArray <NSString *>* categorys = dic[@"category"];
      NSString * query = dic[@"searchKey"];
      NSInteger count = [dic[@"count"] integerValue];
      [TrackerTool trackSearchWithQuery:query category:[categorys componentsJoinedByString:@"/"] resultCount:count variables:[self getTracks] url:nil];
      NSLog(@"搜索埋点 => %@-%@-%ld",query,[categorys componentsJoinedByString:@"/"],count);
      
  }
  else {
    result(FlutterMethodNotImplemented);
  }
}

- (NSArray<Variable *> *)getTracks {
    NSMutableArray *variables = @[].mutableCopy;
    
    NSString *uuidStr = [NSString stringWithFormat:@"%@", self.uuid];
    [variables addObject:[[Variable alloc] initWithIndex:1 name:@"uuid" value:uuidStr]];
    
    [variables addObject:[[Variable alloc] initWithIndex:2 name:@"appversion" value:[NSString stringWithFormat:@"V%@",self.version]]];
    
    if (self.location.length > 0) {
        [variables addObject:[[Variable alloc] initWithIndex:3 name:@"经纬度" value:self.location]];
    }
    
    return variables.copy;
}

@end
