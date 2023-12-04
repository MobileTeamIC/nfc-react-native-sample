//
//  EkycNfcBridgeModule.m
//  SampleIntegrateNfcEkyc
//
//  Created by Longcon99 on 19/06/2023.
//

#import <Foundation/Foundation.h>
#import "EkycNfcBridgeModule.h"
#import "ICNFCCardReader/ICNFCCardReader.h"

@implementation EkycNfcBridgeModule

// To export a module named RCTCalendarModule
RCT_EXPORT_MODULE(EkycNfcBridge);

RCT_EXPORT_METHOD(navigateToNfcQrCode:(NSString *)json resolve:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  
  self._resolve = resolve;
  self._reject = reject;
  
  NSData *data = [json dataUsingEncoding:NSUTF8StringEncoding];
  NSDictionary *jsonResponse = [NSJSONSerialization JSONObjectWithData:data
                                                               options:kNilOptions
                                                                 error:nil];
  NSString* accessToken = [jsonResponse valueForKey:@"access_token"];
  NSString* tokenId = [jsonResponse valueForKey:@"token_id"];
  NSString* tokenKey = [jsonResponse valueForKey:@"token_key"];

  [ICNFCSaveData shared].SDTokenId = tokenId;
  [ICNFCSaveData shared].SDTokenKey = tokenKey;
  [ICNFCSaveData shared].SDAuthorization = accessToken;
  [ICNFCSaveData shared].isPrintLogRequest = YES;
  
  if (@available(iOS 13.0, *)) {
    ICMainNFCReaderViewController *objICMainNFCReader = (ICMainNFCReaderViewController *)[ICMainNFCReaderRouter createModule];
    
    // Đặt giá trị DELEGATE để nhận kết quả trả về
    objICMainNFCReader.icMainNFCDelegate = self;
    
    // Hiển thị màn hình trợ giúp
    objICMainNFCReader.isShowTutorial = true;
    
    // Bật chức năng hiển thị nút bấm "Bỏ qua hướng dẫn" tại các màn hình hướng dẫn bằng video.
    objICMainNFCReader.isEnableGotIt = true;
    
    // Thuộc tính quy định việc đọc thông tin NFC
    // - QRCode: Quét mã QR sau đó đọc thông tin thẻ Chip NFC
    // - NFCReader: Nhập thông tin cho idNumberCard, birthdayCard và expiredDateCard => sau đó đọc thông tin thẻ Chip NFC
    objICMainNFCReader.cardReaderStep = QRCode;
    
    // bật chức năng tải ảnh chân dung trong CCCD
    objICMainNFCReader.isEnableUploadAvatarImage = true;
    
    // Bật tính năng Matching Postcode.
    objICMainNFCReader.isGetPostcodeMatching = true;
    
    // bật tính năng xác thực thẻ.
    objICMainNFCReader.isEnableVerifyChip = true;
    
    // Giá trị này được truyền vào để xác định các thông tin cần để đọc. Các phần tử truyền vào là các giá trị của CardReaderValues.
    // Security Object Document (SOD, COM)
    // MRZ Code (DG1)
    // Image Base64 (DG2)
    // Security Data (DG14, DG15)
    // ** Lưu Ý: Nếu không truyền dữ liệu hoặc truyền mảng rỗng cho readingTagsNFC. SDK sẽ đọc hết các thông tin trong thẻ
    objICMainNFCReader.readingTagsNFC = @[@(VerifyDocumentInfo), @(MRZInfo), @(ImageAvatarInfo), @(SecurityDataInfo)];
    
    // Giá trị tên miền chính của SDK
    // Giá trị "" => gọi đến môi trường Product
    objICMainNFCReader.baseDomain = [NSString string];
    
    // Giá trị này xác định ngôn ngữ được sử dụng trong SDK.
    // - icnfc_vi: Tiếng Việt
    // - icnfc_en: Tiếng Anh
    objICMainNFCReader.languageSdk = @"icekyc_vi";
        
    dispatch_async(dispatch_get_main_queue(), ^{
      UIViewController *root = [[[UIApplication sharedApplication] delegate] window].rootViewController;
      BOOL modalPresent = (BOOL) (root.presentedViewController);
      objICMainNFCReader.modalTransitionStyle = UIModalTransitionStyleCoverVertical;
      
      if (modalPresent) {
        UIViewController *parent = root.presentedViewController;
        [parent setModalPresentationStyle:UIModalPresentationFullScreen];
        [parent showViewController:objICMainNFCReader sender:parent];
        
      } else {
        [objICMainNFCReader setModalPresentationStyle:UIModalPresentationFullScreen];
        [root showDetailViewController:objICMainNFCReader sender:root];
      }
      
    });
    
  } else {
    // Fallback on earlier versions
  }
  
};


RCT_EXPORT_METHOD(navigateToScanNfc:(NSString *)json resolve:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  
  self._resolve = resolve;
  self._reject = reject;
  
  NSData *data = [json dataUsingEncoding:NSUTF8StringEncoding];
  NSDictionary *jsonResponse = [NSJSONSerialization JSONObjectWithData:data
                                                               options:kNilOptions
                                                                 error:nil];
  NSString* accessToken = [jsonResponse valueForKey:@"access_token"];
  NSString* tokenId = [jsonResponse valueForKey:@"token_id"];
  NSString* tokenKey = [jsonResponse valueForKey:@"token_key"];
  NSString* cardId = [jsonResponse valueForKey:@"card_id"];
  NSString* cardDob = [jsonResponse valueForKey:@"card_dob"];
  NSString* cardExpireDate = [jsonResponse valueForKey:@"card_expire_date"];
  
  [ICNFCSaveData shared].SDTokenId = tokenId;
  [ICNFCSaveData shared].SDTokenKey = tokenKey;
  [ICNFCSaveData shared].SDAuthorization = accessToken;
  [ICNFCSaveData shared].isPrintLogRequest = YES;

  
  if (@available(iOS 13.0, *)) {
    ICMainNFCReaderViewController *objICMainNFCReader = (ICMainNFCReaderViewController *)[ICMainNFCReaderRouter createModule];
    
    // Đặt giá trị DELEGATE để nhận kết quả trả về
    objICMainNFCReader.icMainNFCDelegate = self;
    
    // Hiển thị màn hình trợ giúp
    objICMainNFCReader.isShowTutorial = true;
    
    // Bật chức năng hiển thị nút bấm "Bỏ qua hướng dẫn" tại các màn hình hướng dẫn bằng video.
    objICMainNFCReader.isEnableGotIt = true;
    
    // Thuộc tính quy định việc đọc thông tin NFC
    // - QRCode: Quét mã QR sau đó đọc thông tin thẻ Chip NFC
    // - NFCReader: Nhập thông tin cho idNumberCard, birthdayCard và expiredDateCard => sau đó đọc thông tin thẻ Chip NFC
    objICMainNFCReader.cardReaderStep = NFCReader;
    // Số giấy tờ căn cước, là dãy số gồm 12 ký tự.
    objICMainNFCReader.idNumberCard = cardId;
    // Ngày sinh của người dùng được in trên Căn cước, có định dạng YYMMDD (ví dụ 18 tháng 5 năm 1978 thì giá trị là 780518).
    objICMainNFCReader.birthdayCard = cardDob;
    // Ngày hết hạn của Căn cước, có định dạng YYMMDD (ví dụ 18 tháng 5 năm 2047 thì giá trị là 470518).
    objICMainNFCReader.expiredDateCard = cardExpireDate;
    
    
    // bật chức năng tải ảnh chân dung trong CCCD
    objICMainNFCReader.isEnableUploadAvatarImage = true;
    
    // Bật tính năng Matching Postcode.
    objICMainNFCReader.isGetPostcodeMatching = true;
    
    // bật tính năng xác thực thẻ.
    objICMainNFCReader.isEnableVerifyChip = true;
    
    // Giá trị này được truyền vào để xác định các thông tin cần để đọc. Các phần tử truyền vào là các giá trị của CardReaderValues.
    // Security Object Document (SOD, COM)
    // MRZ Code (DG1)
    // Image Base64 (DG2)
    // Security Data (DG14, DG15)
    // ** Lưu Ý: Nếu không truyền dữ liệu hoặc truyền mảng rỗng cho readingTagsNFC. SDK sẽ đọc hết các thông tin trong thẻ
    objICMainNFCReader.readingTagsNFC = @[@(VerifyDocumentInfo), @(MRZInfo), @(ImageAvatarInfo), @(SecurityDataInfo)];
    
    // Giá trị tên miền chính của SDK
    // Giá trị "" => gọi đến môi trường Product
    objICMainNFCReader.baseDomain = [NSString string];
    
    // Giá trị này xác định ngôn ngữ được sử dụng trong SDK.
    // - icnfc_vi: Tiếng Việt
    // - icnfc_en: Tiếng Anh
    objICMainNFCReader.languageSdk = @"icekyc_vi";
    
    dispatch_async(dispatch_get_main_queue(), ^{
      UIViewController *root = [[[UIApplication sharedApplication] delegate] window].rootViewController;
      BOOL modalPresent = (BOOL) (root.presentedViewController);
      objICMainNFCReader.modalTransitionStyle = UIModalTransitionStyleCoverVertical;
      
      if (modalPresent) {
        UIViewController *parent = root.presentedViewController;
        [parent setModalPresentationStyle:UIModalPresentationFullScreen];
        [parent showViewController:objICMainNFCReader sender:parent];
        
      } else {
        [objICMainNFCReader setModalPresentationStyle:UIModalPresentationFullScreen];
        [root showDetailViewController:objICMainNFCReader sender:root];
      }
      
    });
    
  } else {
    // Fallback on earlier versions
  }
  
};


#pragma mark - Delegate

- (void)icNFCCardReaderGetResult {
  NSLog(@"Finished SDK");
  
  // Hiển thị thông tin kết quả QUÉT QR
  NSLog(@"scanQRCodeResult = %@", [ICNFCSaveData shared].scanQRCodeResult);
  
  // Hiển thị thông tin đọc thẻ chip dạng chi tiết
  NSLog(@"dataNFCResult = %@", [ICNFCSaveData shared].dataNFCResult);
  
  // Hiển thị thông tin POSTCODE
  NSLog(@"postcodePlaceOfOriginResult = %@", [ICNFCSaveData shared].postcodePlaceOfOriginResult);
  NSLog(@"postcodePlaceOfResidenceResult = %@", [ICNFCSaveData shared].postcodePlaceOfResidenceResult);
  
  // Hiển thị thông tin xác thực C06
  NSLog(@"verifyNFCCardResult = %@", [ICNFCSaveData shared].verifyNFCCardResult);
  
  // Hiển thị thông tin ảnh chân dung đọc từ thẻ
  NSLog(@"imageAvatar = %@", [ICNFCSaveData shared].imageAvatar);
  NSLog(@"hashImageAvatar = %@", [ICNFCSaveData shared].hashImageAvatar);
  
  // Hiển thị thông tin Client Session
  NSLog(@"clientSessionResult = %@", [ICNFCSaveData shared].clientSessionResult);
  
  // Hiển thị thông tin đọc dữ liệu nguyên bản của thẻ CHIP: COM, DG1, DG2, … DG14, DG15
  NSLog(@"dataGroupsResult = %@", [ICNFCSaveData shared].dataGroupsResult);
  
  NSError* err;
  NSData* jsonData = [NSJSONSerialization dataWithJSONObject:[ICNFCSaveData shared].verifyNFCCardResult options:0 error:&err];
  NSString* verifyNFCCardResult = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
  
  jsonData = [NSJSONSerialization dataWithJSONObject:[ICNFCSaveData shared].dataNFCResult options:0 error:&err];
  NSString* dataNFCResult = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
  
  jsonData = [NSJSONSerialization dataWithJSONObject:[ICNFCSaveData shared].postcodePlaceOfOriginResult options:0 error:&err];
  NSString* postcodePlaceOfOriginResult = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
  
  jsonData = [NSJSONSerialization dataWithJSONObject:[ICNFCSaveData shared].postcodePlaceOfResidenceResult options:0 error:&err];
  NSString* postcodePlaceOfResidenceResult = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
  
  NSDictionary* dict = @{
    // Thông tin mã QR
    @"QR_CODE_RESULT_NFC": [ICNFCSaveData shared].scanQRCodeResult,
    // Thông tin verify C06
    @"CHECK_AUTH_CHIP_RESULT": verifyNFCCardResult,
    // Thông tin ẢNH chân dung
    @"IMAGE_AVATAR_CARD_NFC": [ICNFCSaveData shared].pathImageAvatar.absoluteString,
    @"HASH_AVATAR": [ICNFCSaveData shared].hashImageAvatar,
    // Thông tin Client Session
    @"CLIENT_SESSION_RESULT": [ICNFCSaveData shared].clientSessionResult,
    // Thông tin NFC
    @"LOG_NFC": dataNFCResult,
    // Thông tin postcode
    @"POST_CODE_ORIGINAL_LOCATION_RESULT": postcodePlaceOfOriginResult,
    @"POST_CODE_RECENT_LOCATION_RESULT": postcodePlaceOfResidenceResult
  };
  
  NSError* error;
  NSData* data= [NSJSONSerialization dataWithJSONObject:dict options:0 error:&error];
  
  NSString* resultJson = @"";
  if (error) {
    NSLog(@"Failure to serialize JSON object %@", error);
    
  } else {
    resultJson = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
  }
  
  self._resolve(resultJson);
  self._resolve = nil;
  
}

- (void)icNFCMainDismissed {
  NSLog(@"icNFCMainDismissed");

}

@end
