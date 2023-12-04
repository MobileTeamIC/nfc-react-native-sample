//
//  EkycNfcBridgeModule.h
//  SampleIntegrateNfcEkyc
//
//  Created by Longcon99 on 19/06/2023.
//

#ifndef EkycNfcBridgeModule_h
#define EkycNfcBridgeModule_h

#import <React/RCTBridgeModule.h>
#import "ICNFCCardReader/ICNFCCardReader.h"

@interface EkycNfcBridgeModule: NSObject <RCTBridgeModule, ICMainNFCReaderDelegate>

@property(nonatomic, copy) RCTPromiseResolveBlock _resolve;
@property(nonatomic, copy) RCTPromiseRejectBlock _reject;

@end

#endif /* EkycNfcBridgeModule_h */
