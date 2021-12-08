# zebra

A plugin for zebra scan.

## Usage

#### Import package:

````dart
import 'package:zebra/zebra.dart';
````

#### Add a listener in anywhere:

````dart
XqZebra().listen((code){
  // ...do something
});
````

-   You can use the physical buttons of the device to scan the barcode, or use the following methods to call the scanning device:

````dart
// turn on the scanning device
XqZebra().startScan();

// turn off the scanning device
XqZebra().stopScan();
````

