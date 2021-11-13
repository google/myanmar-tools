import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'markov/zawgyi_unicode_markov_model.dart';

class ZawGyiDetector{
  final ZawGyiUnicodeMarkovModel model;

  ZawGyiDetector._(this.model);

  static Future<ZawGyiDetector> create({String package='myanmar_tools'}) async {
    try {
      var data = await rootBundle.load('packages/$package/resources/zawgyiUnicodeModel.dat',);
      return ZawGyiDetector._(ZawGyiUnicodeMarkovModel(data));
    }catch(e,stacktrace){
      throw FlutterError('Could not load Markov model from resource file . \n$e\n $stacktrace');
    }
  }

  @protected
  @visibleForTesting
  //Only for Testing
  static Future<ZawGyiDetector> createForTest() async {
    try {
      var data = await rootBundle.load('resources/zawgyiUnicodeModel.dat',);
      return ZawGyiDetector._(ZawGyiUnicodeMarkovModel(data));
    }catch(e,stacktrace){
      throw FlutterError('Could not load Markov model from resource file . \n$e\n $stacktrace');
    }
  }

  /// Performs detection on the given string. Returns the probability that the string is Zawgyi given
  /// that it is either Unicode or Zawgyi. Values approaching 1 are strong Zawgyi; values approaching
  /// 0 are strong Unicode; and values close to 0.5 are toss-ups.
  ///
  /// If the string does not contain any Myanmar range code points, double.negativeInfinity is returned.
  ///
  /// * @param input The string on which to run detection.
  /// * @return The probability that the string is Zawgyi (between 0 and 1), or -Infinity if the string contains no Myanmar range code points.
  /// <p>ပေးထားသော စာသားအား ယူနီကုတ် သို့မဟုတ် ဇော်ဂျီ ဖြစ်ကြောင်းစစ်ဆေးရန်။
  /// <p>ဇော်ဂျီ စာသား ဖြစ်ပါက ၁ နှင့် အနီးပတ်ဝန်းကျင် ဂဏန်း ကိုရရှိပြီး ၊ ယူနီကုတ် စာသား ဖြစ်ပါက ၀ နှင့် အနီးပတ်ဝန်းကျင် ဂဏန်း ကိုရရှိမည် ဖြစ်ပါသည်။
  /// <p>၀.၅ အနီးပတ်ဝန်းကျင် ဂဏန်း ဖြစ်ပါက ယူနီကုတ်/ဇော်ဂျီ  နှစ်ခုစလုံး ဖြစ်နိုင်ပြီး လိုအပ်သလိုချိန်ဆ သုံးသွားနိုင်ပါသည်။
  ///  <p>ပေးထားသော စာသားတွင် ယူနီကုတ် နှင့် ဇော်ဂျီ  နှစ်ခုစလုံး မပါရှိပါက double.negativeInfinity ကိုရရှိမည် ဖြစ်ပါသည်။
  /// * @param input ယူနီကုတ် သို့မဟုတ် ဇော်ဂျီ ဖြစ်ကြောင်းစစ်ဆေးရန် စာသား
  /// * @return ဇော်ဂျီ စာသား ဖြစ်နိုင်ခြေ (၀ နှင့် ၁ ကြား) ၊ သို့မဟုတ် ယူနီကုတ် နှင့် ဇော်ဂျီ  နှစ်ခုစလုံး မပါရှိပါက double.negativeInfinity
  double getZawGyiProbability(String input) {
  return model.predict(input);
  }
}