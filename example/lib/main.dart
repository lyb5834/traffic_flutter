import 'package:flutter/material.dart';
import 'package:traffic_flutter/traffic_flutter.dart';

void main() {
  runApp(const MyApp());
  TrafficFlutter().register(
      trafficUrl: 'https://www.baidu.com',
      trafficId: 9527,
      uuid: '00000000000000000000',
      latitude: '000.00',
      longitude: '000.00',
      appVersion: '3.0.0',
      debug: true);
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              ElevatedButton(
                onPressed: () {
                  TrafficFlutter().setUserId(userId: '1111111');
                  TrafficFlutter()
                      .setLocation(longitude: '222.222', latitude: '333.333');
                },
                child: const Text(
                    'setUserId(1111111) & setLocation(222.222,333.333)'),
              ),
              ElevatedButton(
                onPressed: () {
                  TrafficFlutter().setUserId(userId: '4444444');
                  TrafficFlutter()
                      .setLocation(longitude: '555.555', latitude: '666.666');
                },
                child: const Text(
                    'setUserId(4444444) & setLocation(555.555,666.666)'),
              ),
              ElevatedButton(
                onPressed: () {
                  TrafficFlutter().screenPath(
                      title: ['screenPath1', "screenPath2"],
                      path:
                          'rmt://link?target=http%3a%2f%2fwww.mhedu.sh.cn%2fapp%2findex');
                  TrafficFlutter().screenPath(
                      title: ['screenPath1', "screenPath2"],
                      path: 'https://www.baidu.com/');
                },
                child: const Text('screenPath'),
              ),
              ElevatedButton(
                onPressed: () {
                  TrafficFlutter().screen(title: ['screen1', "screen2"]);
                },
                child: const Text('screen'),
              ),
              ElevatedButton(
                onPressed: () {
                  TrafficFlutter().event(
                      category: ['category1', "category2"],
                      action: 'action');
                },
                child: const Text('event'),
              ),
              ElevatedButton(
                onPressed: () {
                  TrafficFlutter().search(
                      searchKey: 'key',
                      category: ['category1', "category2"],
                      count: 100);
                },
                child: const Text('search'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
