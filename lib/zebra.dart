import 'dart:async';

import 'package:flutter/services.dart';
import 'dart:convert';

/// Cbq Zebra Scan Plugin
class XqZebra {
  static XqZebra _instance;

  factory XqZebra() {
    if (_instance == null) {
      _instance = XqZebra._();
    }
    return _instance;
  }

  XqZebra._();

  MethodChannel _mChannel = MethodChannel('com.cbq.zebra/command');
  EventChannel _sChannel = EventChannel('com.cbq.zebra/scan');

  /// 监听扫描的数据
  listen(Function(dynamic) callback, {Function(Object) onError}) =>
      _sChannel.receiveBroadcastStream().listen(callback, onError: onError);

  startScan() => _sendDataWedgeCommand("START_SCANNING");

  stopScan() => _sendDataWedgeCommand("STOP_SCANNING");

  Future<void> _sendDataWedgeCommand(String event) async =>
      await _mChannel.invokeMethod(
          'sendDataWedgeCommandStringParameter',
          jsonEncode({
            "command": "com.symbol.datawedge.api.SOFT_SCAN_TRIGGER",
            "parameter": event
          }));
}
