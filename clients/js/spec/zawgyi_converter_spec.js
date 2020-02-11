/* Copyright 2017 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var zawygi_unicode_convert_data = [
{
    z: " ",
    u: " "
},
{
    z: "123",
    u: "123"
},
{
    z: "abc",
    u: "abc"
},
{
    z: ".!@|",
    u: ".!@|"
},
{
  z: "\u1021\u102C\u100F\u102C\u1015\u102D\u102F\u1004\u1039\u1031" +
      "\u1010\u103C",
  u: "\u1021\u102C\u100F\u102C\u1015\u102D\u102F\u1004\u103A\u1010" +
      "\u103D\u1031"
},
{
    z: "\u1018\u101A\u1039\u1031\u101C\u102C\u1000\u1039\u1010\u102C" +
    "\u1038\u1010\u102C\u1038 ",
    u: "\u1018\u101A\u103A\u101C\u1031\u102C\u1000\u103A\u1010\u102C" +
    "\u1038\u1010\u102C\u1038 "
},
{
    z: "\u1011\u102D\u102F\u1004\u1039\u1038\u1000\u102D\u102F",
    u: "\u1011\u102D\u102F\u1004\u103A\u1038\u1000\u102D\u102F"
},
{
    z: "\u108F\u102D\u102F\u1004\u1039\u107F\u1015\u102E\u1038",
    u: "\u1014\u102D\u102F\u1004\u103A\u1015\u103C\u102E\u1038"
},
{
    z: "\u1031\u103B\u1001\u1010\u101C\u103D\u1019\u1039\u1038\u1010" +
    "\u102D\u102F\u1038\u101C\u102D\u102F\u1000\u1039\u1010\u1032" +
    "\u1037",

    u: "\u1001\u103C\u1031\u1010\u101C\u103E\u1019\u103A\u1038\u1010" +
    "\u102D\u102F\u1038\u101C\u102D\u102F\u1000\u103A\u1010\u1032" +
    "\u1037"
},
{
    z: "\u103B\u1019\u1014\u1039\u1019\u102C ",

    u: "\u1019\u103C\u1014\u103A\u1019\u102C "
},
{
    z: "\u1021\u1019\u103A\u102D\u1033\u1038\u101E\u1019\u102E\u1038",

    u: "\u1021\u1019\u103B\u102D\u102F\u1038\u101E\u1019\u102E\u1038"
},
{
    z: "\u1031\u1018\u102C\u101C\u1036\u102F\u1038\u1021\u101E\u1004" +
    "\u1039\u1038",

    u: "\u1018\u1031\u102C\u101C\u102F\u1036\u1038\u1021\u101E\u1004" +
    "\u103A\u1038"
},
{
    z: "\u1031\u1006\u102C\u1004\u1039\u1038\u1025\u102E\u1038\u101C" +
    "\u1004\u1039\u1038",

    u: "\u1006\u1031\u102C\u1004\u103A\u1038\u1026\u1038\u101C" +
    "\u1004\u103A\u1038"
},
{
    z: "\u103B\u1019\u1014\u1039\u1019\u102C\u1021\u101E\u1004\u1039" +
    "\u1038\u101F\u102C",

    u: "\u1019\u103C\u1014\u103A\u1019\u102C\u1021\u101E\u1004\u103A" +
    "\u1038\u101F\u102C"
},
{
    z: "\u1021\u102C\u1006\u102E\u101A\u1036\u1031\u1012\u101E\u1010" +
    "\u103C\u1004\u1039\u1038\u1019\u103D ",

    u: "\u1021\u102C\u1006\u102E\u101A\u1036\u1012\u1031\u101E\u1010" +
    "\u103D\u1004\u103A\u1038\u1019\u103E "
},
{
    z: " \u1010\u101E\u1004\u1039\u1038\u1010\u100A\u1039\u1038" +
    "\u1031\u101E\u102C ",

    u: " \u1010\u101E\u1004\u103A\u1038\u1010\u100A\u103A\u1038" +
    "\u101E\u1031\u102C "
},
{

    z: " \u1021\u101E\u1004\u1039\u1038\u1021\u103B\u1016\u1005\u1039 ",

    u: " \u1021\u101E\u1004\u103A\u1038\u1021\u1016\u103C\u1005\u103A "
},
{
    z: "\u103B\u1019\u1014\u1039\u1019\u102C\u1037\u108F\u102D\u102F" +
    "\u1004\u1039\u1004\u1036\u1031\u101B\u1038\u1019\u103D\u102C",

    u: "\u1019\u103C\u1014\u103A\u1019\u102C\u1037\u1014\u102D\u102F" +
    "\u1004\u103A\u1004\u1036\u101B\u1031\u1038\u1019\u103E\u102C"
},
{
    z: "\u1005\u1005\u1039\u1010\u1015\u1039\u1015\u102B\u101D\u1004" +
    "\u1039\u107F\u1015\u102E\u1038",

    u: "\u1005\u1005\u103A\u1010\u1015\u103A\u1015\u102B\u101D\u1004" +
    "\u103A\u1015\u103C\u102E\u1038"
},
{
    z: "\u1006\u102D\u102F\u101B\u1004\u1039",

    u: "\u1006\u102D\u102F\u101B\u1004\u103A"
},
{
    z: "\u103B\u1015\u1033\u103B\u1015\u1004\u1039\u1031\u103B\u1015" +
    "\u102C\u1004\u1039\u1038\u101C\u1032\u1031\u101B\u1038\u1031" +
    "\u1016\u102C\u1039\u1031\u1006\u102C\u1004\u1039\u1031\u1014" +
    "\u1010\u1032\u1037",

    u: "\u1015\u103C\u102F\u1015\u103C\u1004\u103A\u1015\u103C\u1031" +
    "\u102C\u1004\u103A\u1038\u101C\u1032\u101B\u1031\u1038\u1016" +
    "\u1031\u102C\u103A\u1006\u1031\u102C\u1004\u103A\u1014\u1031" +
    "\u1010\u1032\u1037"
},
{
    z: "\u1015\u100A\u102C\u1031\u101B\u1038\u1031\u1015\u105A\u101C" +
    "\u1005\u102E\u1006\u102D\u102F\u102F\u1004\u1039\u101B\u102C",

    u: "\u1015\u100A\u102C\u101B\u1031\u1038\u1015\u1031\u102B\u103A" +
    "\u101C\u1005\u102E\u1006\u102D\u102F\u1004\u103A\u101B\u102C"
},
{
    z: "\u101C\u1030\u101B\u103D\u1004\u1039\u101E\u1001\u103A\u1064" +
    "\u1033\u102D\u1004\u1039\u1038\u1019\u103A\u102C\u1038\u1021" +
    "\u1031\u107E\u1000\u102C\u1004\u1039\u1038",

    u: "\u101C\u1030\u101B\u103E\u1004\u103A\u101E\u1004\u103A\u1039" +
    "\u1001\u103B\u102D\u102F\u1004\u103A\u1038\u1019\u103B\u102C" +
    "\u1038\u1021\u1000\u103C\u1031\u102C\u1004\u103A\u1038"
},
{
    z: "\u1001\u102F\u102F\u108F\u103D\u1005\u1039\u1021\u1010\u103C" +
    "\u1000\u1039",

    u: "\u1001\u102F\u1014\u103E\u1005\u103A\u1021\u1010\u103D\u1000" +
    "\u103A"
},
{
    z: "\u1010\u1036\u1006\u102D\u1015\u1039\u1021\u1000\u103A\u108C" +
    "\u1031\u1010\u103C\u1031\u101B\u102C\u1004\u1039\u1038\u1001" +
    "\u103A\u103B\u1016\u1005\u1039\u1001\u1032\u1037\u1010\u1032" +
    "\u1037",

    u: "\u1010\u1036\u1006\u102D\u1015\u103A\u1021\u1004\u103A\u1039" +
    "\u1000\u103B\u102E\u1010\u103D\u1031\u101B\u1031\u102C\u1004" +
    "\u103A\u1038\u1001\u103B\u1016\u103C\u1005\u103A\u1001\u1032" +
    "\u1037\u1010\u1032\u1037"
},
{
    z: "\u101C\u1036\u102F\u101C\u1036\u102F\u107F\u1001\u1036\u1033" +
    "\u107F\u1001\u1036\u1033",

    u: "\u101C\u102F\u1036\u101C\u102F\u1036\u1001\u103C\u102F\u1036" +
    "\u1001\u103C\u102F\u1036"
},
{
    z: "\u1005\u1005\u1039\u1021\u102F\u102F\u1015\u1039\u1001\u103A" +
    "\u1033\u1015\u1039\u1019\u1088",

    u: "\u1005\u1005\u103A\u1021\u102F\u1015\u103A\u1001\u103B\u102F" +
    "\u1015\u103A\u1019\u103E\u102F"
},
{ // #27
    z: "\u1031\u1021\u102C\u1039\u1018\u102F\u1014\u1039\u1038\u107E" +
    "\u1000\u102E\u1038\u1019\u102E\u1038\u107F\u1019\u1087\u102D" +
    "\u1000\u1039\u103B\u1015\u102E\u1038\u1019\u103D\u1000\u1014" +
    "\u1039\u1031\u1010\u102C\u1037\u1010\u1032\u1037\u101C\u102C" +
    "\u1038",

    u: "\u1021\u1031\u102C\u103A\u1018\u102F\u1014\u103A\u1038\u1000" +
    "\u103C\u102E\u1038\u1019\u102E\u1038\u1019\u103C\u103E\u102D" +
    "\u1000\u103A\u1015\u103C\u102E\u1038\u1019\u103E\u1000\u1014" +
    "\u103A\u1010\u1031\u102C\u1037\u1010\u1032\u1037\u101C\u102C" +
    "\u1038"
},
{
    z: "\u1016\u102D\u102F\u102F\u101B\u1019\u1039",

    u: "\u1016\u102D\u102F\u101B\u1019\u103A"
},
{
    z: "\u1004\u1037\u1039",

    u: "\u1004\u1037\u103A"
},
{
    z: "\u1006\u1000\u1039\u1000\u103A\u1004\u1039\u1037\u101E\u1036" +
    "\u102F\u1038\u1019\u101A\u1039",

    u: "\u1006\u1000\u103A\u1000\u103B\u1004\u1037\u103A\u101E\u102F" +
    "\u1036\u1038\u1019\u101A\u103A"
},
{
    z: "\u1010\u1000\u1039\u1040\u1004\u1039\u1031\u101B\u102C",

    u: "\u1010\u1000\u103A\u101D\u1004\u103A\u101B\u1031\u102C"
},
{ //  #32
    z: "\u1031\u1014\u0020\u1037\u1005\u1025\u1039",

    u: "\u1014\u1031\u1037\u0020\u1005\u1009\u103A"
},
{
    z: "\u1000\u1019\u107B\u102C\u1037\u1031\u1005\u103A\u1038\u1021",

    u: "\u1000\u1019\u1039\u1018\u102C\u1037\u1008\u1031\u1038\u1021"
},
{
    z: "\u1019\u103D\u1088\u1001\u1004\u1039",

    u: "\u1019\u103E\u102F\u1001\u1004\u103A"
},
{
    z: "\u1000\u0020\u102b\u0020\u1031\u1001",

    u: "\u1000\u102b\u0020\u1001\u1031"
},
{
    z: "\u1000\u200b\u102b\u0020\u1031\u1001",

    u: "\u1000\u102b\u0020\u1001\u1031"
},
{
    z: "\u1000\u2060\u102b\u0020\u1031\u1001",

    u: "\u1000\u102b\u0020\u1001\u1031"
},
{
    z: "\u1000\ufeff\u102b\u0020\u1031\u1001",

    u: "\u1000\u102b\u0020\u1001\u1031"
},
{
    z: "\u1000\u0020\u0020\u102b\u0020\u1031\u1001",

    u: "\u1000\u102b\u0020\u1001\u1031"
},
{
    z: "\u1000\u0020\u200c\u200d\u200b\u2060\ufeff" +
    "\u102b\u0020\u1031\u1001",

    u: "\u1000\u102b\u0020\u1001\u1031"
},
{
    z: "\u1000\u0020\u103a\u0020\u1031\u1001",

    u: "\u1000\u103B\u0020\u1001\u1031"
},
{
    z: "\u1000\u200b\u103a\u0020\u1031\u1001",

    u: "\u1000\u103B\u0020\u1001\u1031"
},
{
    z: "\u1000\u2060\u103a\u0020\u1031\u1001",

    u: "\u1000\u103B\u0020\u1001\u1031"
},
{
    z: "\u1000\ufeff\u103a\u0020\u1031\u1001",

    u: "\u1000\u103B\u0020\u1001\u1031"
},
{  // NOT REALLY ZAWGYI
    z: "\u1000\u103b\u103a\u0020\u1031\u1001\u102c",

    u: "\u1000\u103b\u103c\u0020\u1001\u1031\u102c"
},
{ // #46.
    z: "\u1000\u103a\u103b\u0020\u1031\u1001",

    u: "\u1000\u103b\u103c\u0020\u1001\u1031"
},
{
    z: "\u1000\u0020\u103b\u103a\u0020\u1031\u1001",

    u: "\u1000\u103b\u103c\u0020\u1001\u1031"
},
{ // # 48
    z: "\u107e\u1000\u1033 \u1031\u103b\u1001",

    u: "\u1000\u103c\u102f\u0020\u1001\u103c\u1031"
},
{
    z: "\u1000\u200b\u103a\u2060\u103d\u0020\u1031\u1001",

    u: "\u1000\u103b\u103e\u0020\u1001\u1031"
},
{  // # 50
    z: "\u1000\u0020\u00a0\u103a\u0020\u1031\u103b\u1001",

    u: "\u1000\u103b\u0020\u1001\u103c\u1031"
},
{
    z: "\u1000\u200b\u103a\u0020\u103b\u0020\u1031\u1001",

    u: "\u1000\u103b\u103c\u0020\u1001\u1031"
},
{
    z: "\u1000\u0020\u1073\u0020\u1031\u1001",

    u: "\u1000\u1039\u1011\u0020\u1001\u1031"
},
{
    z: "\u1000\u0020\u1074\u0020\u1031\u1001",

    u: "\u1000\u1039\u1011\u0020\u1001\u1031"
},
{
    z: "ႁခြင္းခ်က﻿္မရွိ",
    u: "ခြွင်းချက်မရှိ"
},
{
    z: "ႁခ ⁠ ြင္းခ်က ​္မ ၍",
    u: "ခြွင်းချက်မ ၍"
},
{
    z: "ႁခြင္းခ်က​္မရွိ",
    u: "ခြွင်းချက်မရှိ"
},
{
    z: "ႁခြင္းခ်က⁠္မရွိ",
    u: "ခြွင်းချက်မရှိ"
},
{
    z: "ႁခြင္းခ်က‌္မရွိ",
    u: "ခြွင်းချက်မရှိ"
},
{
    z: "ႁခြင္းခ်က‍္မရွိ",
    u: "ခြွင်းချက်မရှိ"
},
{
    z: "ခ ါဘယ်",
    u: "ခါဘယျ"
},
{
    z: "ခ ူဘယ်",
    u: "ခူဘယျ"
},
{
    z: "ခ ေဘယ်",
    u: "ခ ဘေယျ"
},
{
    z: "ခ⁠ဲဘယ်",
    u: "ခဲဘယျ"
},
{
    z: "ခ⁠ဲဘယ်",
    u: "ခဲဘယျ"
},
{
    z: "ခ‍ှဘယ်",
    u: "ခှဘယျ"
},
{  // #66
    z: "ဃ ၠဃၠဃ ႓ဃ‍႓ဃ⁠႔\u1006",
    u: "ဃ္ကဃ္ကဃ္ဘဃ္ဘဃ့\u2060\u1006"
},
{
    z: "\u1001\u0020\u1031\u1018\u101a\u1039",

    u: "ခ ဘေယ်"
},
{  // #68
    z: "၁၉၄၈ ခုႏွစ္​၊ ဒီဇင္​သာလ ၁၀ ရက္​​ေန႔တြင္​ ကဓၻာ့ကုလသမဂအဖဲြ႔ အတည္​ျပဳ၍ ​ေၾကညာလိုက္​ရာ",
    u: "၁၉၄၈ ခုနှစ်​၊ ဒီဇင်​သာလ ၁၀ ရက်​နေ့တွင်​ကဓ္ဘာ့ကုလသမဂအဖွဲ့ အတည်​ပြု၍​ကြေညာလိုက်​ရာ"
},
{  // #69
  z: "၁၉၄၈ ခုႏွစ​္၊ ဒီဇင​္သာလ ၁၀ ရက​္ေန႔တ​ြင္ ကဓၻာ​့ကုလသမဂအဖဲြ႔",
  u: "၁၉၄၈ ခုနှစ်၊ ဒီဇင်သာလ ၁၀ ရက်နေ့တွင် ကဓ္ဘာ့​ကုလသမဂအဖွဲ့"
},
{
    z: "\u103b\u103b\u1000",

    u: "\u1000\u103c"
},
{
    z: "\u103b\u103b\u107e\u1000",

    u: "\u1000\u103c"
},
{
    z: "\u107f\u1084\u1082\u1000",

    u: "\u1000\u103c"
},
{
    z: "\u103b\u107f\u1080\u1083\u1084\u1081\u1082\u1000",

    u: "\u1000\u103c"
},
{
    z: "\u1000\u102d\u102d",

    u: "\u1000\u102d"
},
{
    z: "\u1001\u102e\u102e",

    u: "\u1001\u102e"
},
{
    z: "\u1002\u102f\u102f",

    u: "\u1002\u102f"
},
{
    z: "\u1003\u1030\u1030",

    u: "\u1003\u1030"
},
{
    z: "\u1004\u1032\u1032",

    u: "\u1004\u1032"
},
{
    z: "\u1005\u1033\u1033",

    u: "\u1005\u102f"
},
{
    z: "\u1005\u1033\u1033",

    u: "\u1005\u102f"
},
{ // #81
    z: "\u1005\u1030\u1030", //   z: "\u1005\u1034\u1034",

    u: "\u1005\u1030"
},
{
    z: "\u1006\u1036\u1036",

    u: "\u1006\u1036"
},
{
    z: "\u1007\u1037\u1037",

    u: "\u1007\u1037"
},
{
    z: "\u1009\u1039\u1039",

    u: "\u1009\u103a"
},
{
    z: "\u100a\u103a\u103a",

    u: "\u100a\u103b"
},
{
    z: "\u103b\u100b\u103b\u103b",

    u: "\u100b\u103c"
},
{
    z: "\u100d\u103d\u103d",

    u: "\u100d\u103e"
},
{
    z: "\u100e\u103e\u103e",

    u: "\u100e\u103e"
},
{
    z: "\u103b\u107f\u1080\u1081\u100f",

    u: "\u100f\u103c"
},
{  // #90
    z: "ျျမန္​မာကာမန္​မာကာ",
    u: "မြန်​မာကာမန်​မာကာ"
},
{
    z: "အခ်စ္႔႔သီေသာ pdf",
    u: "အချစ့်သီသော pdf"
},
{
    z: "တကယ္ဆို အၿငႇိဳးေတြမဲ႔ေသာလမ္းေသာလမ္းမွာ တိုႈျပန္ဆံုျကတဲ႔အခါ ",

    u: "တကယ်ဆို အငြှိုးတွေမဲ့သောလမ်းသောလမ်းမှာ တှိုပြန်ဆုံကြတဲ့အခါ "
},
{
    z: "ျမန္္မာကို",
    u: "မြန်မာကို"
},
{
   z: "တာ ့မိမိရဲ့",
   u: "တာ့ မိမိရဲ့",
},
{
  z: "မိုလေ၀သ",   // 1030 before Numeral digit 1040.
  u: "မိုလဝေသ",
},
{  // #96
  z: "အား ​ေသျပီ",  // medial ra 103c should be after 1015
  u: "အား​သေပြီ",
},
    {  // # 97
      z: "ဆိုေတး အေ၀းေရာက္ခ်စ္သူ",
      u: "ဆိုတေး အဝေးရောက်ချစ်သူ"
    },
  { // From case 41:  #98
    z: "myanmar ေငြေစ်း",
    u: "myanmar ငွေဈေး"
  },

  {  // From case 122.  #99
    z: "l ဆိုင္းဇီ ေ၀ခြဲမရပါလား",
    u: "l ဆိုင်းဇီ ဝေခွဲမရပါလား"
  },
    {  // #100 From case #211
      z: "ေ၀သစ္လြင္ about",
      u: "ဝေသစ်လွင် about"
    },
    {  // #101 From case #134: Other
      z: "ျပန္လမ္းမဲ့ကြၽန္းအပိုင္း၄",
      u: "ပြန်လမ်းမဲ့ကျွန်းအပိုင်း၎"
    },
    {  // #102 From case #173: Other
      z: "xxx.ေေလးေလးလို း ကား",
      u: "xxx.လေးလေးလိုး ကား"
    },
    {  // #103 From case 189
      z: "ေမာင္ေမာင္၀",
      u: "မောင်မောင်ဝ"
    },
    {  // #104 From case #227
      z: "မိုလေ၀သ",
      u: "မိုလဝေသ"
    },
    {  // # 105 From case 8: Other
      z: "ဆိုေတး အေ၀းေရာက္ခ်စ္သူ",
      u: "ဆိုတေး အဝေးရောက်ချစ်သူ"
    },

  { // #106 From case 114: Other
    z: "ေေလးေလးလို း ကား",
    u: "လေးလေးလိုး ကား"
  },
  { // #107 From case 45: Digit found C++
    z: "၀ိုင္း၀ိုင္း သီခ်င္းမ်ား",
    u: "ဝိုင်းဝိုင်း သီချင်းများ"
  },
  { // #108 From case 23: Other
    z: "ေ၀လ",
    u: "ဝေလ"
  },
  { // #109 Case 48: Other
    z: "ေရႊေစ်း",
    u: "ရွှေဈေး"
  },
  { // #110 From case 79: FFFD
    z: "ရိုးရွင္းေသာအဂၤလိပ္ဝါက်မ်ားကိုယ္တိုင္တည္ေဆာက္ျခင္း",
    u: "ရိုးရှင်းသောအင်္ဂလိပ်ဝါကျများကိုယ်တိုင်တည်ဆောက်ခြင်း"
  },
  { // #111 U+200b at beginning and end
    z: "\u200bေရႊေစ်း\u200b",
    u: "ရွှေဈေး"
  },
  { // #112 Multiple U+200b at beginning and end
    z: "\u200b\u200b\u200bေရႊေစ်း\u200b\u200b\u200b",
    u: "ရွှေဈေး"
  },
  {  // z+1063 and z+1091
    z: "က႑ခၮး",
    u: "\u1000\u100f\u1039\u100d\u1001\u100d\u1039\u100d\u1038"
  },
  {  // Consonant, subscripted consonant, medial
    z: "\u103b\u108f\u1075\u102c\u1082\u1000\u1075\u102c",
    u: "\u1014\u1039\u1012\u103c\u102c\u1000\u1039\u1012\u103c\u102c"
  },
  {  // Consonant, subscripted consonant, upper mark, medial
    z: "\u103b\u108f\u1075\u102e\u102c\u1082\u1000\u1075\u1039\u102c",
    u: "\u1014\u1039\u1012\u103c\u102e\u102c\u1000\u1039\u1012\u103c\u103a\u102c"
  },

  // Spacing.
  {
    z: "တာ ့မိမိရဲ့",
    u: "တာ့ မိမိရဲ့",
  },
  { // #117
    z: "အ‌ေႂကြး",
    u: "အ‌ကြွေး"
  },
  { // #118
    z: "ေကြၽးေမြး",
    u: "ကျွေးမွေး"
  },
  { // #119
    z: "ဆႏၵႀကီး ေႂကြးေၾကာ္",
    u: "ဆန္ဒကြီး ကြွေးကြော်"
  },
  { // #120
    z: "ေက်းကြၽန္",
    u: "ကျေးကျွန်"
  },
  {  // #121  test https://github.com/google/myanmar-tools/issues/21
    z: "\u1000\u103c\u107d",
    u: "\u1000\u103b\u103d"
  },
  {  // # 122 test https://github.com/google/myanmar-tools/issues/21
    z: "\u108f\u103c\u1036\u1031\u1010\u103a\u1038",
    u: "\u1014\u103d\u1036\u1010\u103b\u1031\u1038"
  },
  {  // #123
    z: "\u103b\u1019\u1087\u1004\u1039\u1037" +
    "\u107f\u1019\u1087\u102d\u1000\u1039" +
    "\u107f\u1004\u1087\u102d\u1033\u1038" +
    "\u107f\u1001\u1036\u1033" +
    "\u103b\u1019\u1087\u1034" +
    "\u107f\u1019\u1087\u102e" +
    "\u1083\u108f\u103c\u1032" +
    "\u1031\u1081\u108f\u103c\u1033",
    
    u: "\u1019\u103c\u103e\u1004\u1037\u103a" +
    "\u1019\u103c\u103e\u102d\u1000\u103a" +
    "\u1004\u103c\u103e\u102d\u102f\u1038" +
    "\u1001\u103c\u102f\u1036" +
    "\u1019\u103c\u103e\u1030" +
    "\u1019\u103c\u103e\u102e" +
    "\u1014\u103c\u103d\u1032" +
    "\u1014\u103c\u103d\u1031\u102f"
  },
  { // #123 test https://github.com/google/myanmar-tools/issues/26
    z: "\u1091",
    u: "\u100f\u1039\u100d",
  },
  { // #124 test https://github.com/google/myanmar-tools/issues/26
    z: "\u106e",
    u: "\u100d\u1039\u100d",
  },
// Odd spacing and strange combinations, for more testing later.
/*
{
    z: "ညီနဲ ့်",
    u: "ညီနျဲ့"
},
  {
    z: "ထားေပး်ခင္းအား်ဖင္ ့",
    u: "ထားပျေးခင်းအျားဖင့်"
},
{
  z: "လယၱီေ်မ",  // Found in searches
  u: "လယ္တျီမေ"
}
*/
];

// Each case contains raw zawgyi, and the output is the expected normalization.
var zawgyi_normalize_tests = [
  {
    z:  "မၪၨဴရီ",
    zn: "မၪၨဴရီ"
  },
  {
    z:  "အာဆီယံ",
    zn: "အာဆီယံ"
  },
  {
    z:  "အမ်ားစုထိန္းခ်ုပ္ထားတဲ့",
    zn: "အမ်ားစုထိန္းခ်ုပ္ထားတဲ့"
  }
];

// It would be nice to use preprocessor flags here, but that would require
// building this file before Jasmine can run it.

var ZawgyiConverter;
var compatTestZ2Usource, compatTestZ2Uoutput, compatTestU2Zsource, compatTestU2Zoutput;
if (typeof process !== "undefined") {
    // NodeJS
    ZawgyiConverter = require("../build_node/zawgyi_converter").ZawgyiConverter;

    // Get resources for compatibility testing
    compatTestZ2Usource = require("fs").readFileSync("resources/mmgov_zawgyi_src.txt", "utf-8");
    compatTestZ2Uoutput = require("fs").readFileSync("resources/mmgov_unicode_out.txt", "utf-8");
    compatTestU2Zsource = require("fs").readFileSync("resources/udhr_mya_unicode_src.txt", "utf-8");
    compatTestU2Zoutput = require("fs").readFileSync("resources/udhr_mya_zawgyi_out.txt", "utf-8");
} else {
    // Browser
    ZawgyiConverter = window.google_myanmar_tools.ZawgyiConverter;

    // Get resources for compatibility testing
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "mmgov_zawgyi_src.txt", false);
    xhr.send(null);
    compatTestZ2Usource = xhr.responseText;

    xhr.open("GET", "mmgov_unicode_out.txt", false);
    xhr.send(null);
    compatTestZ2Uoutput = xhr.responseText;

    xhr.open("GET", "udhr_mya_unicode_src.txt", false);
    xhr.send(null);
    compatTestU2Zsource = xhr.responseText;

    xhr.open("GET", "udhr_mya_zawgyi_out.txt", false);
    xhr.send(null);
    compatTestU2Zoutput = xhr.responseText;
}

describe("ZawgyiConverter Z2U", function () {
    it("should pass the data-driven test", function () {
        const converter = new ZawgyiConverter();
        for (var i = 0; i < zawygi_unicode_convert_data.length; i++) {
            var test_case = zawygi_unicode_convert_data[i];
            var expected = test_case.u;
            var actual = converter.zawgyiToUnicode(test_case.z);
            expect(i+" "+actual).toEqual(i+" "+expected);
        }
    });
});

describe("ZawgyiConverter U2Z", function () {
    // TODO: Enable this test case once fixed. (change "xit" to "it")
    it("should pass the data-driven test", function () {
        const converter = new ZawgyiConverter();
        for (var i = 0; i < zawygi_unicode_convert_data.length; i++) {
            var test_case = zawygi_unicode_convert_data[i];
            var converted = converter.unicodeToZawgyi(test_case.u);
            var expected = converter.normalizeZawgyi(test_case.z);
            var actual = converter.normalizeZawgyi(converted);
            expect(i+" "+actual).toEqual(i+" "+expected);
        }
    });
});

describe("Zawgyi to Unicode Conversion Compatibility Test", function() {
    it("should convert input Zawgyi to exact Unicode out as expected", function() {
        const expected = compatTestZ2Uoutput.split("\n");
        var index = 0;
        const converter = new ZawgyiConverter();
        compatTestZ2Usource.split("\n").forEach(function (line) {
            var actual = converter.zawgyiToUnicode(line);
            expect(actual).toEqual(expected[index]);
            index += 1;
        }, this);
    });
});

describe("Zawgyi Normalization Test", function() {
    it("should convert input Zawgyi to a normalized form", function() {
        const converter = new ZawgyiConverter();
        for (var i = 0; i < zawgyi_normalize_tests.length; i++) {
            var test_case = zawgyi_normalize_tests[i];
            var expected = test_case.zn;
            var actual = converter.normalizeZawgyi(test_case.z);
            expect(i+" "+actual).toEqual(i+" "+expected);
        }
    });
});

describe("Unicode to Zawgyi Conversion Compatibility Test", function() {
    it("should convert input Unicode to normalized Zawgyi output as expected", function() {
        const expected = compatTestU2Zoutput.split("\n");
        var index = 0;
        const converter = new ZawgyiConverter();
        // Normalization is required for exact match.
        compatTestU2Zsource.split("\n").forEach(function (line) {
            var actual = converter.normalizeZawgyi(converter.unicodeToZawgyi(line));
            expect(actual).toEqual(converter.normalizeZawgyi(expected[index]));
            index += 1;
        }, this);
    });

});
