import 'dart:io';


import 'package:flutter_test/flutter_test.dart';
import 'package:myanmar_tools/myanmar_tools.dart';


void main() async{

  late ZawGyiDetector detector;
  setUpAll(() async {
    TestWidgetsFlutterBinding.ensureInitialized();
    detector=await ZawGyiDetector.createForTest();

  });



  test('sanity', () {

    double actual = detector.getZawGyiProbability("hello world");
    expect(actual, double.negativeInfinity);
  });



  test('testIgnoreNonMyanmarCodePoints', () {
    String allASCII = "blah blah blah blah blah";
    String mixedUnicode = "<span>blah blah ဒဂုန်ဦးစန်းငွေ </span> blah blah blah blah";
    String mixedZawgyi = "blah blah blah blah blah သို႔သြားပါ။ blah blah blah";


    expect(detector.getZawGyiProbability(allASCII), double.negativeInfinity);
    expect(detector.getZawGyiProbability(mixedUnicode),lessThan(0.01));
    expect(detector.getZawGyiProbability(mixedZawgyi),greaterThan(0.99));

  });


  test('strongUnicodeReturnsLowScore', () {
    String strongUnicode = "အပြည်ပြည်ဆိုင်ရာ လူ့အခွင့်အရေး ကြေညာစာတမ်း";
    expect(detector.getZawGyiProbability(strongUnicode),lessThan(0.001));
  });

  test('strongZawgyiReturnsHighScore', () {
    String strongZawgyi = "အျပည္ျပည္ဆိုင္ရာ လူ႔အခြင့္အေရး ေၾကညာစာတမ္း";
    expect(detector.getZawGyiProbability(strongZawgyi),greaterThan(0.999));
  });


  test('testDifficult', () {

    /* These are strings that the detector has gotten wrong. They mostly render in both Zawgyi and
     * Unicode, but the words they spell make sense in one encoding but not in the other. As the
     * detector improves, change this test case with scores to match the new output.
     */
      Map<String,double>  cases =<String,double> {
      // STRINGS IDENTICAL IN UNICODE/ZAWGYI
      "အသံကို အစားထိုးလိုပါသလား။": 0.995, // Truth: Identical in U/Z
      "နမူနာ": 0.26, // Truth: Identical in U/Z
      " ဦး": 0.35, // Truth: Identical in U/Z

      // UNICODE STRINGS WITH HIGH ZAWGYI SCORES
      "အစားထိုး အထူးအက္ခရာ": 0.995,
      "ယခု မိုးရွာနေပါသလား။": 0.995,
      "အခြား": 0.74,

      // DIFFICULT STRINGS THAT DETECT CORRECTLY
      // Changes to the detector should not significantly change these scores.
      "ကာမစာအုပ္မ်ား(ေစာက္ပတ္စာအုပ္မ်ား)": 1.0,
      "ညႇပ္စရာမလို": 0.82,
    };

      for (final key in cases.keys) {
        expect(detector.getZawGyiProbability(key),closeTo(cases[key]!,0.005));
      }



  });



  test('testIgnoreNumerals', () {

    expect(detector.getZawGyiProbability('၉၆.၀ kHz'),double.negativeInfinity);
    expect(detector.getZawGyiProbability('၂၄၀၉ ဒဂုန်'),detector.getZawGyiProbability('ဒဂုန်'));
  });


  test('testCompatibility', () async {
      var data=await File('resources/compatibility.tsv').readAsLines();
      for (final line in data) {
        int tabIdx = line.indexOf('\t');
        double expected = double.parse(line.substring(0, tabIdx));
        String input = line.substring(tabIdx + 1);
        double actual = detector.getZawGyiProbability(input);
        if(expected==double.negativeInfinity){
          expect(actual,expected);
        }else{
          expect(actual,closeTo(expected,1e-6));
        }
        }
      });


}
