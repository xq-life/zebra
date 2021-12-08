#import "ZebraPlugin.h"
#if __has_include(<zebra/zebra-Swift.h>)
#import <zebra/zebra-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "zebra-Swift.h"
#endif

@implementation ZebraPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftZebraPlugin registerWithRegistrar:registrar];
}
@end
