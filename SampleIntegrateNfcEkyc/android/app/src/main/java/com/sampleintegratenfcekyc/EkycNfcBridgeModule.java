package com.sampleintegratenfcekyc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vnptit.nfc.activity.VnptScanNFCActivity;
import com.vnptit.nfc.utils.KeyIntentConstantsNFC;
import com.vnptit.nfc.utils.KeyResultConstantsNFC;
import com.vnptit.nfc.utils.SDKEnumNFC;

public class EkycNfcBridgeModule extends ReactContextBaseJavaModule {

   private static final int EKYC_REQUEST_CODE = 100;
   private static final String EKYC_REJECT_CODE = "69";
   private Promise mEkycPromise;

   public EkycNfcBridgeModule(ReactApplicationContext reactContext) {
      super(reactContext);

      final ActivityEventListener activityEventListener = new BaseActivityEventListener() {
         @SuppressWarnings("DanglingJavadoc")
         @Override
         public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            if (requestCode == EKYC_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
               if (data != null && mEkycPromise != null) {
                  /**
                   * đường dẫn ảnh mặt trước trong thẻ chip lưu trong cache
                   * {@link KeyResultConstantsNFC#IMAGE_AVATAR_CARD_NFC}
                   */
                  final String avatarPath = data.getStringExtra(KeyResultConstantsNFC.IMAGE_AVATAR_CARD_NFC);

                  /**
                   * chuỗi thông tin cua SDK
                   * {@link KeyResultConstantsNFC#CLIENT_SESSION_RESULT}
                   */
                  final String clientSession = data.getStringExtra(KeyResultConstantsNFC.CLIENT_SESSION_RESULT);

                  /**
                   * kết quả NFC
                   * {@link KeyResultConstantsNFC#LOG_NFC}
                   */
                  final String logNFC = data.getStringExtra(KeyResultConstantsNFC.LOG_NFC);

                  /**
                   * mã hash avatar
                   * {@link KeyResultConstantsNFC#HASH_AVATAR}
                   */
                  final String hashAvatar = data.getStringExtra(KeyResultConstantsNFC.HASH_AVATAR);

                  /**
                   * chuỗi json string chứa thông tin post code của quê quán
                   * {@link KeyResultConstantsNFC#POST_CODE_ORIGINAL_LOCATION_RESULT}
                   */
                  final String postCodeOriginalLocation = data.getStringExtra(KeyResultConstantsNFC.POST_CODE_ORIGINAL_LOCATION_RESULT);

                  /**
                   * chuỗi json string chứa thông tin post code của nơi thường trú
                   * {@link KeyResultConstantsNFC#POST_CODE_RECENT_LOCATION_RESULT}
                   */
                  final String postCodeRecentLocation = data.getStringExtra(KeyResultConstantsNFC.POST_CODE_RECENT_LOCATION_RESULT);

                  /**
                   * time scan nfc
                   * {@link KeyResultConstantsNFC#TIME_SCAN_NFC}
                   */
                  final String timeScanNfc = data.getStringExtra(KeyResultConstantsNFC.TIME_SCAN_NFC);

                  /**
                   * kết quả check chip căn cước công dân
                   * {@link KeyResultConstantsNFC#CHECK_AUTH_CHIP_RESULT}
                   */
                  final String checkAuthChipResult = data.getStringExtra(KeyResultConstantsNFC.CHECK_AUTH_CHIP_RESULT);

                  /**
                   * kết quả quét QRCode căn cước công dân
                   * {@link KeyResultConstantsNFC#QR_CODE_RESULT_NFC}
                   */
                  final String qrCodeResult = data.getStringExtra(KeyResultConstantsNFC.QR_CODE_RESULT_NFC);

                  final JsonObject json = new JsonObject();
                  putSafe(json, KeyResultConstantsNFC.IMAGE_AVATAR_CARD_NFC, avatarPath);
                  putSafe(json, KeyResultConstantsNFC.CLIENT_SESSION_RESULT, clientSession);
                  putSafe(json, KeyResultConstantsNFC.LOG_NFC, logNFC);
                  putSafe(json, KeyResultConstantsNFC.HASH_AVATAR, hashAvatar);
                  putSafe(
                       json,
                       KeyResultConstantsNFC.POST_CODE_ORIGINAL_LOCATION_RESULT,
                       postCodeOriginalLocation
                  );
                  putSafe(
                       json,
                       KeyResultConstantsNFC.POST_CODE_RECENT_LOCATION_RESULT,
                       postCodeRecentLocation
                  );
                  putSafe(json, KeyResultConstantsNFC.TIME_SCAN_NFC, timeScanNfc);
                  putSafe(json, KeyResultConstantsNFC.CHECK_AUTH_CHIP_RESULT, checkAuthChipResult);
                  putSafe(json, KeyResultConstantsNFC.QR_CODE_RESULT_NFC, qrCodeResult);
                  mEkycPromise.resolve(json.toString());
               }

               mEkycPromise = null;
            }
         }
      };
      reactContext.addActivityEventListener(activityEventListener);
   }

   private void putSafe(final JsonObject json, final String key, final String value) {
      if (!TextUtils.isEmpty(value)) {
         json.addProperty(key, value);
      }
   }

   @NonNull
   @Override
   public String getName() {
      return "EkycNfcBridge";
   }

   private boolean isDeviceSupportedNfc(Activity activity) {
      final NfcAdapter adapter = ((NfcManager) activity.getSystemService(Context.NFC_SERVICE)).getDefaultAdapter();
      return adapter != null && adapter.isEnabled();
   }

   @ReactMethod
   private void navigateToNfcQrCode(final String args, final Promise promise) {
      final Activity activity = getCurrentActivity();
      if (activity == null) return;

      mEkycPromise = promise;
      if (!isDeviceSupportedNfc(activity)) {
         mEkycPromise.reject(EKYC_REJECT_CODE, "Thiết bị không hỗ trợ NFC");
         return;
      }

      final JsonObject json = JsonParser.parseString(args).getAsJsonObject();

      final Intent intent = new Intent(activity, VnptScanNFCActivity.class);
      /*
       * Truyền access token chứa bearer
       */
      intent.putExtra(KeyIntentConstantsNFC.ACCESS_TOKEN, json.get("access_token").getAsString());
      /*
       * Truyền token id
       */
      intent.putExtra(KeyIntentConstantsNFC.TOKEN_ID, json.get("token_id").getAsString());
      /*
       * Truyền token key
       */
      intent.putExtra(KeyIntentConstantsNFC.TOKEN_KEY, json.get("token_key").getAsString());
      /*
       * điều chỉnh ngôn ngữ tiếng việt
       *    - vi: tiếng việt
       *    - en: tiếng anh
       */
      intent.putExtra(KeyIntentConstantsNFC.LANGUAGE_NFC, SDKEnumNFC.LanguageEnum.VIETNAMESE.getValue());
      /*
       * hiển thị màn hình hướng dẫn + hiển thị nút bỏ qua hướng dẫn
       * - mặc định luôn luôn hiển thị màn hình hướng dẫn
       *    - true: hiển thị nút bỏ qua
       *    - false: ko hiển thị nút bỏ qua
       */
      intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_GOT_IT, true);
      /*
       * bật tính năng upload ảnh
       *    - true: bật tính năng
       *    - false: tắt tính năng
       */
      intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_UPLOAD_IMAGE, true);
      /*
       * bật tính năng get Postcode
       *    - true: bật tính năng
       *    - false: tắt tính năng
       */
      intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_MAPPING_ADDRESS, true);
      /*
       * bật tính năng xác thực chip
       *    - true: bật tính năng
       *    - false: tắt tính năng
       */
      intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_VERIFY_CHIP, true);
      /*
       * truyền các giá trị đọc thẻ
       *    - nếu không truyền gì mặc định sẽ đọc tất cả (MRZ,Verify Document,Image Avatar)
       *    - giá trị truyền vào là 1 mảng int: nếu muốn đọc giá trị nào sẽ truyền
       *      giá trị đó vào mảng
       * eg: chỉ đọc thông tin MRZ
       *    new int[]{SDKEnumNFC.ReadingNFCTags.MRZInfo.getValue()}
       */
      intent.putExtra(
           KeyIntentConstantsNFC.READING_TAG_NFC,
           new int[]{
                SDKEnumNFC.ReadingNFCTags.MRZInfo.getValue(),
                SDKEnumNFC.ReadingNFCTags.VerifyDocumentInfo.getValue(),
                SDKEnumNFC.ReadingNFCTags.ImageAvatarInfo.getValue()
           }
      );
      /*
       * truyền giá trị bật quét QRCode
       *    - true: tắt quét QRCode
       *    - false: bật quét QRCode
       */
      intent.putExtra(KeyIntentConstantsNFC.IS_TURN_OFF_QR_CODE, false);
      // set baseDomain="" => sử dụng mặc định là Product
      intent.putExtra(KeyIntentConstantsNFC.CHANGE_BASE_URL_NFC, "");

      activity.startActivityForResult(intent, EKYC_REQUEST_CODE);
   }

   @ReactMethod
   private void navigateToScanNfc(final String args, final Promise promise) {
      final Activity activity = getCurrentActivity();
      if (activity == null) return;

      mEkycPromise = promise;
      if (!isDeviceSupportedNfc(activity)) {
         mEkycPromise.reject(EKYC_REJECT_CODE, "Thiết bị không hỗ trợ NFC");
         return;
      }

      final JsonObject json = JsonParser.parseString(args).getAsJsonObject();

      final Intent intent = new Intent(activity, VnptScanNFCActivity.class);
      /*
       * Truyền access token chứa bearer
       */
      intent.putExtra(KeyIntentConstantsNFC.ACCESS_TOKEN, json.get("access_token").getAsString());
      /*
       * Truyền token id
       */
      intent.putExtra(KeyIntentConstantsNFC.TOKEN_ID, json.get("token_id").getAsString());
      /*
       * Truyền token key
       */
      intent.putExtra(KeyIntentConstantsNFC.TOKEN_KEY, json.get("token_key").getAsString());
      /*
       * điều chỉnh ngôn ngữ tiếng việt
       *    - vi: tiếng việt
       *    - en: tiếng anh
       */
      intent.putExtra(KeyIntentConstantsNFC.LANGUAGE_NFC, SDKEnumNFC.LanguageEnum.VIETNAMESE.getValue());
      /*
       * hiển thị màn hình hướng dẫn + hiển thị nút bỏ qua hướng dẫn
       * - mặc định luôn luôn hiển thị màn hình hướng dẫn
       *    - true: hiển thị nút bỏ qua
       *    - false: ko hiển thị nút bỏ qua
       */
      intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_GOT_IT, true);
      /*
       * bật tính năng upload ảnh
       *    - true: bật tính năng
       *    - false: tắt tính năng
       */
      intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_UPLOAD_IMAGE, true);
      /*
       * bật tính năng get Postcode
       *    - true: bật tính năng
       *    - false: tắt tính năng
       */
      intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_MAPPING_ADDRESS, true);
      /*
       * bật tính năng xác thực chip
       *    - true: bật tính năng
       *    - false: tắt tính năng
       */
      intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_VERIFY_CHIP, true);
      /*
       * truyền các giá trị đọc thẻ
       *    - nếu không truyền gì mặc định sẽ đọc tất cả (MRZ,Verify Document,Image Avatar)
       *    - giá trị truyền vào là 1 mảng int: nếu muốn đọc giá trị nào sẽ truyền
       *      giá trị đó vào mảng
       * eg: chỉ đọc thông tin MRZ
       *    new int[]{SDKEnumNFC.ReadingNFCTags.MRZInfo.getValue()}
       */
      intent.putExtra(
           KeyIntentConstantsNFC.READING_TAG_NFC,
           new int[]{
                SDKEnumNFC.ReadingNFCTags.MRZInfo.getValue(),
                SDKEnumNFC.ReadingNFCTags.VerifyDocumentInfo.getValue(),
                SDKEnumNFC.ReadingNFCTags.ImageAvatarInfo.getValue()
           }
      );
      /*
       * truyền giá trị bật quét QRCode
       *    - true: tắt quét QRCode
       *    - false: bật quét QRCode
       */
      intent.putExtra(KeyIntentConstantsNFC.IS_TURN_OFF_QR_CODE, true);
      // set baseDomain="" => sử dụng mặc định là Product
      intent.putExtra(KeyIntentConstantsNFC.CHANGE_BASE_URL_NFC, "");
      // truyền id định danh căn cước công dân
      intent.putExtra(KeyIntentConstantsNFC.ID_NUMBER_CARD, json.get("card_id").getAsString());
      // truyền ngày sinh ghi trên căn cước công dân
      intent.putExtra(KeyIntentConstantsNFC.BIRTHDAY_CARD, json.get("card_dob").getAsString());
      // truyền ngày hết hạn căn cước công dân
      intent.putExtra(KeyIntentConstantsNFC.EXPIRED_CARD, json.get("card_expire_date").getAsString());

      activity.startActivityForResult(intent, EKYC_REQUEST_CODE);
   }
}
