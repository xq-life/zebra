import 'dart:async';

import 'package:flutter/services.dart';
import 'dart:convert';

/// Cbq Zebra Scan Plugin
class XqZebra {
  static const MethodChannel _mChannel = MethodChannel('com.cbq.zebra/command');
  static const EventChannel _sChannel = EventChannel('com.cbq.zebra/scan');

  /// 监听扫描的数据
  static listen(Function(dynamic) callback, {Function(Object) onError}) =>
      _sChannel.receiveBroadcastStream().listen(callback, onError: onError);

  static startScan() => _sendDataWedgeCommand("START_SCANNING");

  static stopScan() => _sendDataWedgeCommand("STOP_SCANNING");

  static Future<void> _sendDataWedgeCommand(String event) async =>
      await _mChannel.invokeMethod(
          'sendDataWedgeCommandStringParameter',
          jsonEncode({
            "command": "com.symbol.datawedge.api.SOFT_SCAN_TRIGGER",
            "parameter": event
          }));
}
