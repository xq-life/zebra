import 'package:flutter/material.dart';
import 'package:zebra/zebra.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _code = '';

  @override
  void initState() {
    super.initState();
    XqZebra().listen((code) => setState(() => this._code = '$code'));
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: const Text('CBQ ZEBRA PLUGIN')),
        body: Column(
          children: [
            Padding(
              padding: EdgeInsets.all(20),
              child: Text('scan code: $_code\n'),
            ),
            GestureDetector(
              onTapDown: (_) => XqZebra().startScan(),
              onTapUp: (_) => XqZebra().stopScan(),
              child: Container(
                padding: EdgeInsets.all(20),
                color: Colors.lightBlueAccent,
                child: Center(child: Text('Tap To Scan')),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
