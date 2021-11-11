import 'transliterate/transliterate.dart';
import 'transliterate/transliterate_u2z.dart';
import 'transliterate/transliterate_z2u.dart';
import 'transliterate/transliterate_znorm.dart';

class ZawGyiConverter{

  Transliterate? _transliterateU2Z;
  Transliterate? _transliterateZ2U;
  Transliterate? _transliterateZNorm;

  ///Convert Zawgyi to Unicode
  /// * @param inString Zawgyi string.
  /// * @return Unicode string.
  /// <p> ဇော်ဂျီ မှ ယူနီကုတ် သို့ပြောင်းရန်။
  /// * @param inString ဇော်ဂျီ အချက်အလက်စာ။
  /// * @return ယူနီကုတ် သို့‌ပြောင်းထားသော အချက်အလက်စာ။
  String zawGyiToUnicode(String inString){
    _transliterateZ2U??=TransliterateZ2U('Z2U');
    return _transliterateZ2U!.convert(inString);
  }

  ///Convert Unicode To Zawgyi
  /// * @param inString Unicode string.
  /// * @return Zawgyi string.
  /// <p>ယူနီကုတ် မှ ဇော်ဂျီ သို့ပြောင်းရန်။
  /// * @param inString ယူနီကုတ် အချက်အလက်စာ။
 /// * @return ဇော်ဂျီ သို့‌ပြောင်းထားသော အချက်အလက်စာ။
  String unicodeToZawGyi(String inString){
    _transliterateU2Z??=TransliterateU2Z('U2Z');
    return _transliterateU2Z!.convert(inString);
  }

  ///Convert normalize Zawgyi
  /// * @param inString Zawgyi string.
  /// * @return Normalized Zawgyi string.
  /// <p>ရှုပ်ထွေးသော ဇော်ဂျီ အချက်အလက်စာကြမ်းများ ကို ရှင်းလင်းခြင်း။
  /// * @param  ရှုပ်ထွေးသော ဇော်ဂျီ အချက်အလက်စာကြမ်း။
  /// * @return ရှင်းလင်း ထားသော ဇော်ဂျီ အချက်အလက်စာ။
  String normalizeZawGyi(String inString){
    _transliterateZNorm??=TransliterateZNorm('ZNorm');
    return _transliterateZNorm!.convert(inString);
  }
}