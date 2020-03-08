import 'dart:async';

import 'package:flutter/services.dart';

class ScreenRecorder {
  static const MethodChannel _channel =
      const MethodChannel('screen_recorder');

  static Future<String> get startScreenRecording async {
    final String status = await _channel.invokeMethod('startScreenRecording');
    return status;
  }
  static Future<String> get stopScreenRecording async {
    final String status = await _channel.invokeMethod('stopScreenRecording');
    return status;
  }
}
