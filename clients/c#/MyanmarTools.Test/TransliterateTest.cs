using Xunit;
using System.Collections.Generic;
using System.IO;
using System.Text;

namespace MyanmarTools.Test
{

    public class TransliterateTest
    {
        private ZawgyiConverter converter;

        #region  TestCasesData
        public static IEnumerable<object[]> TestCases => new List<string[]> {
  new string[]{" "," "},
 new string[]{"123","123"},
 new string[]{"abc","abc"},
 new string[]{".!@|",".!@|"},
 new string[]{"\u1021\u102C\u100F\u102C\u1015\u102D\u102F\u1004\u1039\u1031" +
"\u1010\u103C","\u1021\u102C\u100F\u102C\u1015\u102D\u102F\u1004\u103A\u1010" +
"\u103D\u1031"},
 new string[]{"\u1018\u101A\u1039\u1031\u101C\u102C\u1000\u1039\u1010\u102C" +
"\u1038\u1010\u102C\u1038 ","\u1018\u101A\u103A\u101C\u1031\u102C\u1000\u103A\u1010\u102C" +
"\u1038\u1010\u102C\u1038 "},
 new string[]{"\u1011\u102D\u102F\u1004\u1039\u1038\u1000\u102D\u102F","\u1011\u102D\u102F\u1004\u103A\u1038\u1000\u102D\u102F"},
 new string[]{"\u108F\u102D\u102F\u1004\u1039\u107F\u1015\u102E\u1038","\u1014\u102D\u102F\u1004\u103A\u1015\u103C\u102E\u1038"},
 new string[]{"\u1031\u103B\u1001\u1010\u101C\u103D\u1019\u1039\u1038\u1010" +
"\u102D\u102F\u1038\u101C\u102D\u102F\u1000\u1039\u1010\u1032" +
"\u1037",
"\u1001\u103C\u1031\u1010\u101C\u103E\u1019\u103A\u1038\u1010" +
"\u102D\u102F\u1038\u101C\u102D\u102F\u1000\u103A\u1010\u1032" +
"\u1037"},
 new string[]{"\u103B\u1019\u1014\u1039\u1019\u102C ",
"\u1019\u103C\u1014\u103A\u1019\u102C "},
 new string[]{"\u1021\u1019\u103A\u102D\u1033\u1038\u101E\u1019\u102E\u1038",
"\u1021\u1019\u103B\u102D\u102F\u1038\u101E\u1019\u102E\u1038"},
 new string[]{"\u1031\u1018\u102C\u101C\u1036\u102F\u1038\u1021\u101E\u1004" +
"\u1039\u1038",
"\u1018\u1031\u102C\u101C\u102F\u1036\u1038\u1021\u101E\u1004" +
"\u103A\u1038"},
// #12
new string[]{"\u1031\u1006\u102C\u1004\u1039\u1038\u1025\u102E\u1038\u101C" +
"\u1004\u1039\u1038","\u1006\u1031\u102C\u1004\u103A\u1038\u1026\u1038\u101C" +
"\u1004\u103A\u1038"},
 new string[]{"\u103B\u1019\u1014\u1039\u1019\u102C\u1021\u101E\u1004\u1039" +
"\u1038\u101F\u102C",
"\u1019\u103C\u1014\u103A\u1019\u102C\u1021\u101E\u1004\u103A" +
"\u1038\u101F\u102C"},
 new string[]{"\u1021\u102C\u1006\u102E\u101A\u1036\u1031\u1012\u101E\u1010" +
"\u103C\u1004\u1039\u1038\u1019\u103D ",
"\u1021\u102C\u1006\u102E\u101A\u1036\u1012\u1031\u101E\u1010" +
"\u103D\u1004\u103A\u1038\u1019\u103E "},
 new string[]{" \u1010\u101E\u1004\u1039\u1038\u1010\u100A\u1039\u1038" +
"\u1031\u101E\u102C ",
" \u1010\u101E\u1004\u103A\u1038\u1010\u100A\u103A\u1038" +
"\u101E\u1031\u102C "},
 new string[]{
" \u1021\u101E\u1004\u1039\u1038\u1021\u103B\u1016\u1005\u1039 ",
" \u1021\u101E\u1004\u103A\u1038\u1021\u1016\u103C\u1005\u103A "},
 new string[]{"\u103B\u1019\u1014\u1039\u1019\u102C\u1037\u108F\u102D\u102F" +
"\u1004\u1039\u1004\u1036\u1031\u101B\u1038\u1019\u103D\u102C",
"\u1019\u103C\u1014\u103A\u1019\u102C\u1037\u1014\u102D\u102F" +
"\u1004\u103A\u1004\u1036\u101B\u1031\u1038\u1019\u103E\u102C"},
 new string[]{"\u1005\u1005\u1039\u1010\u1015\u1039\u1015\u102B\u101D\u1004" +
"\u1039\u107F\u1015\u102E\u1038",
"\u1005\u1005\u103A\u1010\u1015\u103A\u1015\u102B\u101D\u1004" +
"\u103A\u1015\u103C\u102E\u1038"},
 new string[]{"\u1006\u102D\u102F\u101B\u1004\u1039",
"\u1006\u102D\u102F\u101B\u1004\u103A"},
 new string[]{"\u103B\u1015\u1033\u103B\u1015\u1004\u1039\u1031\u103B\u1015" +
"\u102C\u1004\u1039\u1038\u101C\u1032\u1031\u101B\u1038\u1031" +
"\u1016\u102C\u1039\u1031\u1006\u102C\u1004\u1039\u1031\u1014" +
"\u1010\u1032\u1037",
"\u1015\u103C\u102F\u1015\u103C\u1004\u103A\u1015\u103C\u1031" +
"\u102C\u1004\u103A\u1038\u101C\u1032\u101B\u1031\u1038\u1016" +
"\u1031\u102C\u103A\u1006\u1031\u102C\u1004\u103A\u1014\u1031" +
"\u1010\u1032\u1037"},
 new string[]{"\u1015\u100A\u102C\u1031\u101B\u1038\u1031\u1015\u105A\u101C" +
"\u1005\u102E\u1006\u102D\u102F\u102F\u1004\u1039\u101B\u102C",
"\u1015\u100A\u102C\u101B\u1031\u1038\u1015\u1031\u102B\u103A" +
"\u101C\u1005\u102E\u1006\u102D\u102F\u1004\u103A\u101B\u102C"},
 new string[]{"\u101C\u1030\u101B\u103D\u1004\u1039\u101E\u1001\u103A\u1064" +
"\u1033\u102D\u1004\u1039\u1038\u1019\u103A\u102C\u1038\u1021" +
"\u1031\u107E\u1000\u102C\u1004\u1039\u1038",
"\u101C\u1030\u101B\u103E\u1004\u103A\u101E\u1004\u103A\u1039" +
"\u1001\u103B\u102D\u102F\u1004\u103A\u1038\u1019\u103B\u102C" +
"\u1038\u1021\u1000\u103C\u1031\u102C\u1004\u103A\u1038"},
 new string[]{"\u1001\u102F\u102F\u108F\u103D\u1005\u1039\u1021\u1010\u103C" +
"\u1000\u1039",
"\u1001\u102F\u1014\u103E\u1005\u103A\u1021\u1010\u103D\u1000" +
"\u103A"},
 new string[]{"\u1010\u1036\u1006\u102D\u1015\u1039\u1021\u1000\u103A\u108C" +
"\u1031\u1010\u103C\u1031\u101B\u102C\u1004\u1039\u1038\u1001" +
"\u103A\u103B\u1016\u1005\u1039\u1001\u1032\u1037\u1010\u1032" +
"\u1037",
"\u1010\u1036\u1006\u102D\u1015\u103A\u1021\u1004\u103A\u1039" +
"\u1000\u103B\u102E\u1010\u103D\u1031\u101B\u1031\u102C\u1004" +
"\u103A\u1038\u1001\u103B\u1016\u103C\u1005\u103A\u1001\u1032" +
"\u1037\u1010\u1032\u1037"},
 new string[]{"\u101C\u1036\u102F\u101C\u1036\u102F\u107F\u1001\u1036\u1033" +
"\u107F\u1001\u1036\u1033",
"\u101C\u102F\u1036\u101C\u102F\u1036\u1001\u103C\u102F\u1036" +
"\u1001\u103C\u102F\u1036"},
 new string[]{"\u1005\u1005\u1039\u1021\u102F\u102F\u1015\u1039\u1001\u103A" +
"\u1033\u1015\u1039\u1019\u1088",
"\u1005\u1005\u103A\u1021\u102F\u1015\u103A\u1001\u103B\u102F" +
"\u1015\u103A\u1019\u103E\u102F"},
new string[]{ // #27
"\u1031\u1021\u102C\u1039\u1018\u102F\u1014\u1039\u1038\u107E" +
"\u1000\u102E\u1038\u1019\u102E\u1038\u107F\u1019\u1087\u102D" +
"\u1000\u1039\u103B\u1015\u102E\u1038\u1019\u103D\u1000\u1014" +
"\u1039\u1031\u1010\u102C\u1037\u1010\u1032\u1037\u101C\u102C" +
"\u1038",
"\u1021\u1031\u102C\u103A\u1018\u102F\u1014\u103A\u1038\u1000" +
"\u103C\u102E\u1038\u1019\u102E\u1038\u1019\u103C\u103E\u102D" +
"\u1000\u103A\u1015\u103C\u102E\u1038\u1019\u103E\u1000\u1014" +
"\u103A\u1010\u1031\u102C\u1037\u1010\u1032\u1037\u101C\u102C" +
"\u1038"},
 new string[]{"\u1016\u102D\u102F\u102F\u101B\u1019\u1039",
"\u1016\u102D\u102F\u101B\u1019\u103A"},
 new string[]{"\u1004\u1037\u1039",
"\u1004\u1037\u103A"},
 new string[]{"\u1006\u1000\u1039\u1000\u103A\u1004\u1039\u1037\u101E\u1036" +
"\u102F\u1038\u1019\u101A\u1039",
"\u1006\u1000\u103A\u1000\u103B\u1004\u1037\u103A\u101E\u102F" +
"\u1036\u1038\u1019\u101A\u103A"},
 new string[]{"\u1010\u1000\u1039\u1040\u1004\u1039\u1031\u101B\u102C",
"\u1010\u1000\u103A\u101D\u1004\u103A\u101B\u1031\u102C"},
new string[]{ //  #32
"\u1031\u1014\u0020\u1037\u1005\u1025\u1039",
"\u1014\u1031\u1037\u0020\u1005\u1009\u103A"},
 new string[]{"\u1000\u1019\u107B\u102C\u1037\u1031\u1005\u103A\u1038\u1021",
"\u1000\u1019\u1039\u1018\u102C\u1037\u1008\u1031\u1038\u1021"},
 new string[]{"\u1019\u103D\u1088\u1001\u1004\u1039",
"\u1019\u103E\u102F\u1001\u1004\u103A"},
 new string[]{"\u1000\u0020\u102b\u0020\u1031\u1001",
"\u1000\u102b\u0020\u1001\u1031"},
 new string[]{"\u1000\u200b\u102b\u0020\u1031\u1001",
"\u1000\u102b\u0020\u1001\u1031"},
 new string[]{"\u1000\u2060\u102b\u0020\u1031\u1001",
"\u1000\u102b\u0020\u1001\u1031"},
 new string[]{"\u1000\ufeff\u102b\u0020\u1031\u1001",
"\u1000\u102b\u0020\u1001\u1031"},
 new string[]{"\u1000\u0020\u0020\u102b\u0020\u1031\u1001",
"\u1000\u102b\u0020\u1001\u1031"},
 new string[]{"\u1000\u0020\u200c\u200d\u200b\u2060\ufeff" +
"\u102b\u0020\u1031\u1001",
"\u1000\u102b\u0020\u1001\u1031"},
 new string[]{"\u1000\u0020\u103a\u0020\u1031\u1001",
"\u1000\u103B\u0020\u1001\u1031"},
 new string[]{"\u1000\u200b\u103a\u0020\u1031\u1001",
"\u1000\u103B\u0020\u1001\u1031"},
 new string[]{"\u1000\u2060\u103a\u0020\u1031\u1001",
"\u1000\u103B\u0020\u1001\u1031"},
 new string[]{"\u1000\ufeff\u103a\u0020\u1031\u1001",
"\u1000\u103B\u0020\u1001\u1031"},
new string[]{  // NOT REALLY ZAWGYI
"\u1000\u103b\u103a\u0020\u1031\u1001\u102c",
"\u1000\u103b\u103c\u0020\u1001\u1031\u102c"},
new string[]{ // #46.
"\u1000\u103a\u103b\u0020\u1031\u1001",
"\u1000\u103b\u103c\u0020\u1001\u1031"},
 new string[]{"\u1000\u0020\u103b\u103a\u0020\u1031\u1001",
"\u1000\u103b\u103c\u0020\u1001\u1031"},
new string[]{ // # 48
"\u107e\u1000\u1033 \u1031\u103b\u1001",
"\u1000\u103c\u102f\u0020\u1001\u103c\u1031"},
 new string[]{"\u1000\u200b\u103a\u2060\u103d\u0020\u1031\u1001",
"\u1000\u103b\u103e\u0020\u1001\u1031"},
new string[]{  // # 50
"\u1000\u0020\u00a0\u103a\u0020\u1031\u103b\u1001",
"\u1000\u103b\u0020\u1001\u103c\u1031"},
 new string[]{"\u1000\u200b\u103a\u0020\u103b\u0020\u1031\u1001",
"\u1000\u103b\u103c\u0020\u1001\u1031"},
 new string[]{"\u1000\u0020\u1073\u0020\u1031\u1001",
"\u1000\u1039\u1011\u0020\u1001\u1031"},
 new string[]{"\u1000\u0020\u1074\u0020\u1031\u1001",
"\u1000\u1039\u1011\u0020\u1001\u1031"},
 new string[]{"ႁခြင္းခ်က﻿္မရွိ","ခြွင်းချက်မရှိ"},
 new string[]{"ႁခ ⁠ ြင္းခ်က ​္မ ၍","ခြွင်းချက်မ ၍"},
 new string[]{"ႁခြင္းခ်က​္မရွိ","ခြွင်းချက်မရှိ"},
 new string[]{"ႁခြင္းခ်က⁠္မရွိ","ခြွင်းချက်မရှိ"},
 new string[]{"ႁခြင္းခ်က‌္မရွိ","ခြွင်းချက်မရှိ"},
 new string[]{"ႁခြင္းခ်က‍္မရွိ","ခြွင်းချက်မရှိ"},
 new string[]{"ခ ါဘယ်","ခါဘယျ"},
 new string[]{"ခ ူဘယ်","ခူဘယျ"},
 new string[]{"ခ ေဘယ်","ခ ဘေယျ"},
 new string[]{"ခ⁠ဲဘယ်","ခဲဘယျ"},
 //Duplicate
 //new string[]{"ခ⁠ဲဘယ်","ခဲဘယျ"},
 new string[]{"ခ‍ှဘယ်","ခှဘယျ"},
new string[]{  // #66
"ဃ ၠဃၠဃ ႓ဃ‍႓ဃ⁠႔\u1006","ဃ္ကဃ္ကဃ္ဘဃ္ဘဃ့\u2060\u1006"},
 new string[]{"\u1001\u0020\u1031\u1018\u101a\u1039",
"ခ ဘေယ်"},
new string[]{  // #68
"၁၉၄၈ ခုႏွစ္​၊ ဒီဇင္​သာလ ၁၀ ရက္​​ေန႔တြင္​ ကဓၻာ့ကုလသမဂအဖဲြ႔ အတည္​ျပဳ၍ ​ေၾကညာလိုက္​ရာ","၁၉၄၈ ခုနှစ်​၊ ဒီဇင်​သာလ ၁၀ ရက်​နေ့တွင်​ကဓ္ဘာ့ကုလသမဂအဖွဲ့ အတည်​ပြု၍​ကြေညာလိုက်​ရာ"},
new string[]{  // #69
"၁၉၄၈ ခုႏွစ​္၊ ဒီဇင​္သာလ ၁၀ ရက​္ေန႔တ​ြင္ ကဓၻာ​့ကုလသမဂအဖဲြ႔","၁၉၄၈ ခုနှစ်၊ ဒီဇင်သာလ ၁၀ ရက်နေ့တွင် ကဓ္ဘာ့​ကုလသမဂအဖွဲ့"},
 new string[]{"\u103b\u103b\u1000",
"\u1000\u103c"},
 new string[]{"\u103b\u103b\u107e\u1000","\u1000\u103c"},
 new string[]{"\u107f\u1084\u1082\u1000","\u1000\u103c"},
 new string[]{"\u103b\u107f\u1080\u1083\u1084\u1081\u1082\u1000","\u1000\u103c"},
 new string[]{"\u1000\u102d\u102d","\u1000\u102d"},
 new string[]{"\u1001\u102e\u102e","\u1001\u102e"},
 new string[]{"\u1002\u102f\u102f","\u1002\u102f"},
 new string[]{"\u1003\u1030\u1030","\u1003\u1030"},
 new string[]{"\u1004\u1032\u1032","\u1004\u1032"},
 new string[]{"\u1005\u1033\u1033","\u1005\u102f"},
 //Duplicate
 //new string[]{"\u1005\u1033\u1033","\u1005\u102f"},
new string[]{ // #81
"\u1005\u1030\u1030", //"\u1005\u1034\u1034",
"\u1005\u1030"},
 new string[]{"\u1006\u1036\u1036",
"\u1006\u1036"},
 new string[]{"\u1007\u1037\u1037",
"\u1007\u1037"},
 new string[]{"\u1009\u1039\u1039",
"\u1009\u103a"},
 new string[]{"\u100a\u103a\u103a",
"\u100a\u103b"},
 new string[]{"\u103b\u100b\u103b\u103b",
"\u100b\u103c"},
 new string[]{"\u100d\u103d\u103d",
"\u100d\u103e"},
 new string[]{"\u100e\u103e\u103e",
"\u100e\u103e"},
 new string[]{"\u103b\u107f\u1080\u1081\u100f",
"\u100f\u103c"},
new string[]{  // #90
"ျျမန္​မာကာမန္​မာကာ","မြန်​မာကာမန်​မာကာ"},
 new string[]{ "အခ်စ္႔႔သီေသာ pdf", "အချစ့်သီသော pdf"},
new string[]{  // #92
 "တကယ္ဆို အၿငႇိဳးေတြမဲ႔ေသာလမ္းေသာလမ္းမွာ တိုႈျပန္ဆံုျကတဲ႔အခါ ",
 "တကယ်ဆို အငြှိုးတွေမဲ့သောလမ်းသောလမ်းမှာ တှိုပြန်ဆုံကြတဲ့အခါ "},
 new string[]{ "ျမန္္မာကို", "မြန်မာကို"},
 new string[]{"တာ ့မိမိရဲ့","တာ့ မိမိရဲ့",},
 new string[]{   "မိုလေ၀သ",   // 1030 before Numeral digit 1040.
   "မိုလဝေသ",},
new string[]{  // #96
  "အား ​ေသျပီ",  // medial ra 103c should be after 1015
  "အား​သေပြီ"},
  new string[]{  // # 97
"ဆိုေတး အေ၀းေရာက္ခ်စ္သူ", "ဆိုတေး အဝေးရောက်ချစ်သူ"  },
  new string[]{ // From case 41:  #98
"myanmar ေငြေစ်း",  "myanmar ငွေဈေး"},
  new string[]{  // From case 122.  #99
"l ဆိုင္းဇီ ေ၀ခြဲမရပါလား",  "l ဆိုင်းဇီ ဝေခွဲမရပါလား"},
new string[]{  // #100 From case #211
"ေ၀သစ္လြင္ about","ဝေသစ်လွင် about"  },
new string[]{  // #101 From case #134: Other
"ျပန္လမ္းမဲ့ကြၽန္းအပိုင္း၄","ပြန်လမ်းမဲ့ကျွန်းအပိုင်း၎"  },
new string[]{  // #102 From case #173: Other
"xxx.ေေလးေလးလို း ကား","xxx.လေးလေးလိုး ကား"},
new string[]{  // #103 From case 189
 "ေမာင္ေမာင္၀", "မောင်မောင်ဝ"},
 //Duplicate
//new string[]{  // #104 From case #227
 //"မိုလေ၀သ", "မိုလဝေသ"},
 //Duplicate
//new string[]{  // # 105 From case 8: Other
 //"ဆိုေတး အေ၀းေရာက္ခ်စ္သူ", "ဆိုတေး အဝေးရောက်ချစ်သူ"},

  new string[]{ // #106 From case 114: Other
 "ေေလးေလးလို း ကား", "လေးလေးလိုး ကား"  },
  new string[]{ // #107 From case 45: Digit found C++
 "၀ိုင္း၀ိုင္း သီခ်င္းမ်ား", "ဝိုင်းဝိုင်း သီချင်းများ"  },
  new string[]{ // #108 From case 23: Other
 "ေ၀လ", "ဝေလ"  },
  new string[]{ // #109 Case 48: Other
 "ေရႊေစ်း", "ရွှေဈေး"  },
  new string[]{ // #110 From case 79: FFFD
 "ရိုးရွင္းေသာအဂၤလိပ္ဝါက်မ်ားကိုယ္တိုင္တည္ေဆာက္ျခင္း", "ရိုးရှင်းသောအင်္ဂလိပ်ဝါကျများကိုယ်တိုင်တည်ဆောက်ခြင်း"  },
  new string[]{ // #111 U+200b at beginning and end
 "\u200bေရႊေစ်း\u200b", "ရွှေဈေး"  },
  new string[]{ // #112 Multiple U+200b at beginning and end
 "\u200b\u200b\u200bေရႊေစ်း\u200b\u200b\u200b", "ရွှေဈေး"  },
  new string[]{ // #113
"အ‌ေႂကြး","အ‌ကြွေး"  },
  new string[]{ // #114
"ေကြၽးေမြး","ကျွေးမွေး"  },
  new string[]{ // #115
"ဆႏၵႀကီး ေႂကြးေၾကာ္","ဆန္ဒကြီး ကြွေးကြော်"  },
  new string[]{ // #116
"ေက်းကြၽန္","ကျေးကျွန်"  },
  new string[]{  // #117 test https://github.com/google/myanmar-tools/issues/21
"\u1000\u103c\u107d","\u1000\u103b\u103d",  },
  new string[]{  // #118 test https://github.com/google/myanmar-tools/issues/21
"\u108f\u103c\u1036\u1031\u1010\u103a\u1038","\u1014\u103d\u1036\u1010\u103b\u1031\u1038",  },
  new string[]{  // #119
"\u103b\u1019\u1087\u1004\u1039\u1037" +
"\u107f\u1019\u1087\u102d\u1000\u1039" +
"\u107f\u1004\u1087\u102d\u1033\u1038" +
"\u107f\u1001\u1036\u1033" +
"\u103b\u1019\u1087\u1034" +
"\u107f\u1019\u1087\u102e" +
"\u1083\u108f\u103c\u1032" +
"\u1031\u1081\u108f\u103c\u1033","\u1019\u103c\u103e\u1004\u1037\u103a" +
"\u1019\u103c\u103e\u102d\u1000\u103a" +
"\u1004\u103c\u103e\u102d\u102f\u1038" +
"\u1001\u103c\u102f\u1036" +
"\u1019\u103c\u103e\u1030" +
"\u1019\u103c\u103e\u102e" +
"\u1014\u103c\u103d\u1032" +
"\u1014\u103c\u103d\u1031\u102f"  },
  new string[]{ // #123 test https://github.com/google/myanmar-tools/issues/26
"\u1091","\u100f\u1039\u100d",  },
  new string[]{ // #124 test https://github.com/google/myanmar-tools/issues/26
"\u106e","\u100d\u1039\u100d",  },
};
        #endregion
        public TransliterateTest()
        {
            converter = new ZawgyiConverter();
        }


        [Fact(DisplayName = "Sanity")]
        public void Sanity()
        {
            string input = "hello world";
            Assert.Equal(converter.NormalizeZawgyi(input), input);
            Assert.Equal(converter.ZawgyiToUnicode(input), input);
            Assert.Equal(converter.UnicodeToZawgyi(input), input);
        }

        [Fact(DisplayName = "z2UT17")]
        public void Z2UT17()
        {
            var Z1 = "\u1011\u1039";
            var U1 = "\u1011\u103a";
            Assert.Equal(converter.ZawgyiToUnicode(Z1), U1);
        }

        [Fact(DisplayName = "u2ZT1")]
        public void U2ZT1()
        {
            var Z1 = "\u1011\u1039";
            var U1 = "\u1011\u103a";
            Assert.Equal(converter.UnicodeToZawgyi(U1), Z1);
        }


        [Theory(DisplayName = "zNormTests")]
        [InlineData("အကာင္ ့", "\u1021\u1000\u102c\u1004\u1039\u1037")] // "အကာင္"
        [InlineData("မၪၨဴရီ", "မၪၨဴရီ")]
        [InlineData("အာဆီယံ", "အာဆီယံ")]
        [InlineData("အမ်ားစုထိန္းခ်ဳပ္ထားတဲ့", "အမ်ားစုထိန္းခ်ုပ္ထားတဲ့")]
        public void ZNormTests(string ZIn, string Expected)
        {
            Assert.Equal(converter.NormalizeZawgyi(ZIn), Expected);
        }

        // Simple Zawgyi conversion test.
        [Fact(DisplayName = "z2UT2")]
        public void z2UT2()
        {
            var Z1 = "\u1011\u102D\u102F\u1004\u1039\u1038\u1000\u102D\u102F";
            var U1 = "\u1011\u102D\u102F\u1004\u103A\u1038\u1000\u102D\u102F";
            Assert.Equal(converter.ZawgyiToUnicode(Z1), U1);
        }

        /**
          * All Zawgyi -> Unicode tests.
          */
        [Theory(DisplayName = "z2UTests")]
        [MemberData(nameof(TestCases))]
        public void z2UTests(string ZIn, string Expected)
        {
            Assert.Equal(converter.ZawgyiToUnicode(ZIn), Expected);
        }

        /**
          * All  Unicode -> Zawgyi tests.
          */
        [Theory(DisplayName = "u2ZTests")]
        [MemberData(nameof(TestCases))]
        public void u2ZTests(string Expected, string UIn)
        {
            Assert.Equal(converter.NormalizeZawgyi(converter.UnicodeToZawgyi(UIn)), converter.NormalizeZawgyi(Expected));
        }

        /**
         * Compatibility test for Z->U conversion using golden file.
         */
        [Fact(DisplayName = "z2UCompatibilityTest")]
        public void z2UCompatibilityTest()
        {
            var SourceResourcePath = Path.Combine("resources", "mmgov_zawgyi_src.txt");
            var GoldenResourcePath = Path.Combine("resources", "mmgov_unicode_out.txt");

            using (var SourceSteam = File.Open(SourceResourcePath, FileMode.Open, FileAccess.Read, FileShare.Read))
            {
                using (var GoldenSteam = File.Open(GoldenResourcePath, FileMode.Open, FileAccess.Read, FileShare.Read))
                {
                    using (var SourceReader = new StreamReader(SourceSteam, Encoding.UTF8, true, 1024))
                    {
                        using (var GoldenReader = new StreamReader(GoldenSteam, Encoding.UTF8, true, 1024))
                        {
                            while (SourceReader.Peek() != -1 && GoldenReader.Peek() != -1)
                            {
                                var ZLine = SourceReader.ReadLine();
                                var GoldenLine = GoldenReader.ReadLine();
                                Assert.Equal(converter.ZawgyiToUnicode(ZLine), GoldenLine);
                            }


                        }
                    }
                }
            }

        }


        /**
          * Compatibility test for U->Z conversion using golden file.
          * Note that normalization of Zawgyi is needed for comparison.
          */
        [Fact(DisplayName = "u2ZCompatibilityTest")]
        public void u2ZCompatibilityTest()
        {
            var SourceResourcePath = Path.Combine("resources", "udhr_mya_unicode_src.txt");
            var GoldenResourcePath = Path.Combine("resources", "udhr_mya_zawgyi_out.txt");

            using (var SourceSteam = File.Open(SourceResourcePath, FileMode.Open, FileAccess.Read, FileShare.Read))
            {
                using (var GoldenSteam = File.Open(GoldenResourcePath, FileMode.Open, FileAccess.Read, FileShare.Read))
                {
                    using (var SourceReader = new StreamReader(SourceSteam, Encoding.UTF8, true, 1024))
                    {
                        using (var GoldenReader = new StreamReader(GoldenSteam, Encoding.UTF8, true, 1024))
                        {
                            while (SourceReader.Peek() != -1 && GoldenReader.Peek() != -1)
                            {
                                var ULine = SourceReader.ReadLine();
                                var GoldenLine = GoldenReader.ReadLine();
                                Assert.Equal(converter.NormalizeZawgyi(converter.UnicodeToZawgyi(ULine)), converter.NormalizeZawgyi(GoldenLine));
                            }


                        }
                    }
                }
            }

        }

    }
}