/* Copyright 2018 Google LLC
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

package com.google.myanmartools;

import static com.google.common.truth.Truth.assertWithMessage;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TransliterateTest {

  private static final TransliterateU2Z u2Z = new TransliterateU2Z("Unicode to Zawgyi");
  private static final TransliterateZNorm zNorm = new TransliterateZNorm("Normalize Zawgyi");
  private static final TransliterateZ2U z2U = new TransliterateZ2U("Zawgyi to Unicode");

  // Test cases of {Zawgyi, Unicode} pairs, expected to interconvert.
  private final String[][] testCases = {
    {
      " ",
      " "
    },
    {
      "123",
      "123"
    },
    {
      "abc",
      "abc"
    },
    {
      ".!@",
      ".!@"
    },
    {
      "\u1021\u102C\u100F\u102C\u1015\u102D\u102F\u1004\u1039\u1031" +
      "\u1010\u103C",
      "\u1021\u102C\u100F\u102C\u1015\u102D\u102F\u1004\u103A\u1010" +
      "\u103D\u1031"
    },
    {
      "\u1018\u101A\u1039\u1031\u101C\u102C\u1000\u1039\u1010\u102C" +
      "\u1038\u1010\u102C\u1038 ",
      "\u1018\u101A\u103A\u101C\u1031\u102C\u1000\u103A\u1010\u102C" +
      "\u1038\u1010\u102C\u1038 "
    },
    {
      "\u1011\u102D\u102F\u1004\u1039\u1038\u1000\u102D\u102F",
      "\u1011\u102D\u102F\u1004\u103A\u1038\u1000\u102D\u102F"
    },
    {
      "\u108F\u102D\u102F\u1004\u1039\u107F\u1015\u102E\u1038",
      "\u1014\u102D\u102F\u1004\u103A\u1015\u103C\u102E\u1038"
    },
    {
      "\u1031\u103B\u1001\u1010\u101C\u103D\u1019\u1039\u1038\u1010" +
      "\u102D\u102F\u1038\u101C\u102D\u102F\u1000\u1039\u1010\u1032" +
      "\u1037",

      "\u1001\u103C\u1031\u1010\u101C\u103E\u1019\u103A\u1038\u1010" +
      "\u102D\u102F\u1038\u101C\u102D\u102F\u1000\u103A\u1010\u1032" +
      "\u1037"
    },
    {
      "\u103B\u1019\u1014\u1039\u1019\u102C ",

      "\u1019\u103C\u1014\u103A\u1019\u102C "
    },
    {
      "\u1021\u1019\u103A\u102D\u1033\u1038\u101E\u1019\u102E\u1038",

      "\u1021\u1019\u103B\u102D\u102F\u1038\u101E\u1019\u102E\u1038"
    },
    {
      "\u1031\u1018\u102C\u101C\u1036\u102F\u1038\u1021\u101E\u1004" +
      "\u1039\u1038",

      "\u1018\u1031\u102C\u101C\u102F\u1036\u1038\u1021\u101E\u1004" +
      "\u103A\u1038"
    },
    {  // #12
      "\u1031\u1006\u102C\u1004\u1039\u1038\u1025\u102E\u1038\u101C" +
      "\u1004\u1039\u1038",

      "\u1006\u1031\u102C\u1004\u103A\u1038\u1026\u1038\u101C" +
      "\u1004\u103A\u1038"
    },
    {
      "\u103B\u1019\u1014\u1039\u1019\u102C\u1021\u101E\u1004\u1039" +
      "\u1038\u101F\u102C",

      "\u1019\u103C\u1014\u103A\u1019\u102C\u1021\u101E\u1004\u103A" +
      "\u1038\u101F\u102C"
    },
    {
      "\u1021\u102C\u1006\u102E\u101A\u1036\u1031\u1012\u101E\u1010" +
      "\u103C\u1004\u1039\u1038\u1019\u103D ",

      "\u1021\u102C\u1006\u102E\u101A\u1036\u1012\u1031\u101E\u1010" +
      "\u103D\u1004\u103A\u1038\u1019\u103E "
    },
    {
      " \u1010\u101E\u1004\u1039\u1038\u1010\u100A\u1039\u1038" +
      "\u1031\u101E\u102C ",

      " \u1010\u101E\u1004\u103A\u1038\u1010\u100A\u103A\u1038" +
      "\u101E\u1031\u102C "
    },
    {

      " \u1021\u101E\u1004\u1039\u1038\u1021\u103B\u1016\u1005\u1039 ",

      " \u1021\u101E\u1004\u103A\u1038\u1021\u1016\u103C\u1005\u103A "
    },
    {
      "\u103B\u1019\u1014\u1039\u1019\u102C\u1037\u108F\u102D\u102F" +
      "\u1004\u1039\u1004\u1036\u1031\u101B\u1038\u1019\u103D\u102C",

      "\u1019\u103C\u1014\u103A\u1019\u102C\u1037\u1014\u102D\u102F" +
      "\u1004\u103A\u1004\u1036\u101B\u1031\u1038\u1019\u103E\u102C"
    },
    {
      "\u1005\u1005\u1039\u1010\u1015\u1039\u1015\u102B\u101D\u1004" +
      "\u1039\u107F\u1015\u102E\u1038",

      "\u1005\u1005\u103A\u1010\u1015\u103A\u1015\u102B\u101D\u1004" +
      "\u103A\u1015\u103C\u102E\u1038"
    },
    {
      "\u1006\u102D\u102F\u101B\u1004\u1039",

      "\u1006\u102D\u102F\u101B\u1004\u103A"
    },
    {
      "\u103B\u1015\u1033\u103B\u1015\u1004\u1039\u1031\u103B\u1015" +
      "\u102C\u1004\u1039\u1038\u101C\u1032\u1031\u101B\u1038\u1031" +
      "\u1016\u102C\u1039\u1031\u1006\u102C\u1004\u1039\u1031\u1014" +
      "\u1010\u1032\u1037",

      "\u1015\u103C\u102F\u1015\u103C\u1004\u103A\u1015\u103C\u1031" +
      "\u102C\u1004\u103A\u1038\u101C\u1032\u101B\u1031\u1038\u1016" +
      "\u1031\u102C\u103A\u1006\u1031\u102C\u1004\u103A\u1014\u1031" +
      "\u1010\u1032\u1037"
    },
    {
      "\u1015\u100A\u102C\u1031\u101B\u1038\u1031\u1015\u105A\u101C" +
      "\u1005\u102E\u1006\u102D\u102F\u102F\u1004\u1039\u101B\u102C",

      "\u1015\u100A\u102C\u101B\u1031\u1038\u1015\u1031\u102B\u103A" +
      "\u101C\u1005\u102E\u1006\u102D\u102F\u1004\u103A\u101B\u102C"
    },
    {
      "\u101C\u1030\u101B\u103D\u1004\u1039\u101E\u1001\u103A\u1064" +
      "\u1033\u102D\u1004\u1039\u1038\u1019\u103A\u102C\u1038\u1021" +
      "\u1031\u107E\u1000\u102C\u1004\u1039\u1038",

      "\u101C\u1030\u101B\u103E\u1004\u103A\u101E\u1004\u103A\u1039" +
      "\u1001\u103B\u102D\u102F\u1004\u103A\u1038\u1019\u103B\u102C" +
      "\u1038\u1021\u1000\u103C\u1031\u102C\u1004\u103A\u1038"
    },
    {
      "\u1001\u102F\u102F\u108F\u103D\u1005\u1039\u1021\u1010\u103C" +
      "\u1000\u1039",

      "\u1001\u102F\u1014\u103E\u1005\u103A\u1021\u1010\u103D\u1000" +
      "\u103A"
    },
    {
      "\u1010\u1036\u1006\u102D\u1015\u1039\u1021\u1000\u103A\u108C" +
      "\u1031\u1010\u103C\u1031\u101B\u102C\u1004\u1039\u1038\u1001" +
      "\u103A\u103B\u1016\u1005\u1039\u1001\u1032\u1037\u1010\u1032" +
      "\u1037",

      "\u1010\u1036\u1006\u102D\u1015\u103A\u1021\u1004\u103A\u1039" +
      "\u1000\u103B\u102E\u1010\u103D\u1031\u101B\u1031\u102C\u1004" +
      "\u103A\u1038\u1001\u103B\u1016\u103C\u1005\u103A\u1001\u1032" +
      "\u1037\u1010\u1032\u1037"
    },
    {
      "\u101C\u1036\u102F\u101C\u1036\u102F\u107F\u1001\u1036\u1033" +
      "\u107F\u1001\u1036\u1033",

      "\u101C\u102F\u1036\u101C\u102F\u1036\u1001\u103C\u102F\u1036" +
      "\u1001\u103C\u102F\u1036"
    },
    {
      "\u1005\u1005\u1039\u1021\u102F\u102F\u1015\u1039\u1001\u103A" +
      "\u1033\u1015\u1039\u1019\u1088",

      "\u1005\u1005\u103A\u1021\u102F\u1015\u103A\u1001\u103B\u102F" +
      "\u1015\u103A\u1019\u103E\u102F"
    },
    {
      "\u1031\u1021\u102C\u1039\u1018\u102F\u1014\u1039\u1038\u107E" +
      "\u1000\u102E\u1038\u1019\u102E\u1038\u103B\u1019\u102D\u103D" +
      "\u1000\u1039\u103B\u1015\u102E\u1038\u1019\u103D\u1000\u1014" +
      "\u1039\u1031\u1010\u102C\u1037\u1010\u1032\u1037\u101C\u102C" +
      "\u1038",

      "\u1021\u1031\u102C\u103A\u1018\u102F\u1014\u103A\u1038\u1000" +
      "\u103C\u102E\u1038\u1019\u102E\u1038\u1019\u103C\u103E\u102D" +
      "\u1000\u103A\u1015\u103C\u102E\u1038\u1019\u103E\u1000\u1014" +
      "\u103A\u1010\u1031\u102C\u1037\u1010\u1032\u1037\u101C\u102C" +
      "\u1038"
    },
    {
      "\u1016\u102D\u102F\u102F\u101B\u1019\u1039",

      "\u1016\u102D\u102F\u101B\u1019\u103A"
    },
    {
      "\u1004\u1037\u1039",

      "\u1004\u1037\u103A"
    },
    {
      "\u1006\u1000\u1039\u1000\u103A\u1004\u1039\u1037\u101E\u1036" +
      "\u102F\u1038\u1019\u101A\u1039",

      "\u1006\u1000\u103A\u1000\u103B\u1004\u1037\u103A\u101E\u102F" +
      "\u1036\u1038\u1019\u101A\u103A"
    },
    {
      "\u1010\u1000\u1039\u1040\u1004\u1039\u1031\u101B\u102C",

      "\u1010\u1000\u103A\u101D\u1004\u103A\u101B\u1031\u102C"
    },
    { //  #32
      "\u1031\u1014\u0020\u1037\u1005\u1025\u1039",

      "\u1014\u1031\u1037\u0020\u1005\u1025\u103A"
    },
    {
      "\u1000\u1019\u107B\u102C\u1037\u1031\u1005\u103A\u1038\u1021",

      "\u1000\u1019\u1039\u1018\u102C\u1037\u1008\u1031\u1038\u1021"
    },
    {
      "\u1019\u103D\u1088\u1001\u1004\u1039",

      "\u1019\u103E\u102F\u1001\u1004\u103A"
    },
    {
      "\u1000\u0020\u102b\u0020\u1031\u1001",

      "\u1000\u102b\u0020\u1001\u1031"
    },
    {
      "\u1000\u200b\u102b\u0020\u1031\u1001",

      "\u1000\u102b\u0020\u1001\u1031"
    },
    {
      "\u1000\u2060\u102b\u0020\u1031\u1001",

      "\u1000\u102b\u0020\u1001\u1031"
    },
    {
      "\u1000\ufeff\u102b\u0020\u1031\u1001",

      "\u1000\u102b\u0020\u1001\u1031"
    },
    {
      "\u1000\u0020\u0020\u102b\u0020\u1031\u1001",

      "\u1000\u102b\u0020\u1001\u1031"
    },
    {
      "\u1000\u0020\u200c\u200d\u200b\u2060\ufeff" +
      "\u102b\u0020\u1031\u1001",

      "\u1000\u102b\u0020\u1001\u1031"
    },
    {
      "\u1000\u0020\u103a\u0020\u1031\u1001",

      "\u1000\u103B\u0020\u1001\u1031"
    },
    {
      "\u1000\u200b\u103a\u0020\u1031\u1001",

      "\u1000\u103B\u0020\u1001\u1031"
    },
    {
      "\u1000\u2060\u103a\u0020\u1031\u1001",

      "\u1000\u103B\u0020\u1001\u1031"
    },
    {
      "\u1000\ufeff\u103a\u0020\u1031\u1001",

      "\u1000\u103B\u0020\u1001\u1031"
    },
    {  // NOT REALLY ZAWGYI
      "\u1000\u103b\u103a\u0020\u1031\u1001\u102c",

      "\u1000\u103b\u103c\u0020\u1001\u1031\u102c"
    },
    { // #46.
      "\u1000\u103a\u103b\u0020\u1031\u1001",

      "\u1000\u103b\u103c\u0020\u1001\u1031"
    },
    {
      "\u1000\u0020\u103b\u103a\u0020\u1031\u1001",

      "\u1000\u103b\u103c\u0020\u1001\u1031"
    },
    { // # 48
      "\u107e\u1000\u1033 \u1031\u103b\u1001",

      "\u1000\u103c\u102f\u0020\u1001\u103c\u1031"
    },
    {
      "\u1000\u200b\u103a\u2060\u103d\u0020\u1031\u1001",

      "\u1000\u103b\u103e\u0020\u1001\u1031"
    },
    {  // # 50
      "\u1000\u0020\u00a0\u103a\u0020\u1031\u103b\u1001",

      "\u1000\u103b\u0020\u1001\u103c\u1031"
    },
    {
      "\u1000\u200b\u103a\u0020\u103b\u0020\u1031\u1001",

      "\u1000\u103b\u103c\u0020\u1001\u1031"
    },
    {
      "\u1000\u0020\u1073\u0020\u1031\u1001",

      "\u1000\u1039\u1011\u0020\u1001\u1031"
    },
    {
      "\u1000\u0020\u1074\u0020\u1031\u1001",

      "\u1000\u1039\u1011\u0020\u1001\u1031"
    },
    {
      "ႁခြင္းခ်က﻿္မရွိ",
      "ခြွင်းချက်မရှိ"
    },
    {
      "ႁခ ⁠ ြင္းခ်က ​္မ ၍",
      "ခြွင်းချက်မ ၍"
    },
    {
      "ႁခြင္းခ်က​္မရွိ",
      "ခြွင်းချက်မရှိ"
    },
    {
      "ႁခြင္းခ်က⁠္မရွိ",
      "ခြွင်းချက်မရှိ"
    },
    {
      "ႁခြင္းခ်က‌္မရွိ",
      "ခြွင်းချက်မရှိ"
    },
    {
      "ႁခြင္းခ်က‍္မရွိ",
      "ခြွင်းချက်မရှိ"
    },
    {
      "ခ ါဘယ်",
      "ခါဘယျ"
    },
    {
      "ခ ူဘယ်",
      "ခူဘယျ"
    },
    {
      "ခ ေဘယ်",
      "ခ ဘေယျ"
    },
    {
      "ခ⁠ဲဘယ်",
      "ခဲဘယျ"
    },
    {
      "ခ⁠ဲဘယ်",
      "ခဲဘယျ"
    },
    {
      "ခ‍ှဘယ်",
      "ခှဘယျ"
    },
    {  // #66
      "ဃ ၠဃၠဃ ႓ဃ‍႓ဃ⁠႔\u1006",
      "ဃ္ကဃ္ကဃ္ဘဃ္ဘဃ့\u2060\u1006"
    },
    {
      "\u1001\u0020\u1031\u1018\u101a\u1039",

      "ခ ဘေယ်"
    },
    {  // #68
      "၁၉၄၈ ခုႏွစ္​၊ ဒီဇင္​သာလ ၁၀ ရက္​​ေန႔တြင္​ ကဓၻာ့ကုလသမဂအဖဲြ႔ အတည္​ျပဳ၍ ​ေၾကညာလိုက္​ရာ",
     "၁၉၄၈ ခုနှစ်​၊ ဒီဇင်​သာလ ၁၀ ရက်​​နေ့တွင်​ ကဓ္ဘာ့ကုလသမဂအဖွဲ့ အတည်​ပြု၍ ​ကြေညာလိုက်​ရာ"
    },
    {  // #69
      "၁၉၄၈ ခုႏွစ​္၊ ဒီဇင​္သာလ ၁၀ ရက​္ေန႔တ​ြင္ ကဓၻာ​့ကုလသမဂအဖဲြ႔",

      "၁၉၄၈ ခုနှစ်၊ ဒီဇင်သာလ ၁၀ ရက်နေ့တွင် ကဓ္ဘာ့​ကုလသမဂအဖွဲ့"
    },
    {
      "\u103b\u103b\u1000",

      "\u1000\u103c"
    },
    {
      "\u103b\u103b\u107e\u1000",

      "\u1000\u103c"
    },
    {
      "\u107f\u1084\u1082\u1000",

      "\u1000\u103c"
    },
    {
      "\u103b\u107f\u1080\u1083\u1084\u1081\u1082\u1000",

      "\u1000\u103c"
    },
    {
      "\u1000\u102d\u102d",

      "\u1000\u102d"
    },
    {
      "\u1001\u102e\u102e",

      "\u1001\u102e"
    },
    {
      "\u1002\u102f\u102f",

      "\u1002\u102f"
    },
    {
      "\u1003\u1030\u1030",

      "\u1003\u1030"
    },
    {
      "\u1004\u1032\u1032",

      "\u1004\u1032"
    },
    {
      "\u1005\u1033\u1033",

      "\u1005\u102f"
    },
    {
      "\u1005\u1033\u1033",

      "\u1005\u102f"
    },
    { // #81
      "\u1005\u1030\u1030", //    "\u1005\u1034\u1034",

      "\u1005\u1030"
    },
    {
      "\u1006\u1036\u1036",

      "\u1006\u1036"
    },
    {
      "\u1007\u1037\u1037",

      "\u1007\u1037"
    },
    {
      "\u1009\u1039\u1039",

      "\u1009\u103a"
    },
    {
      "\u100a\u103a\u103a",

      "\u100a\u103b"
    },
    {
      "\u103b\u100b\u103b\u103b",

      "\u100b\u103c"
    },
    {
      "\u100d\u103d\u103d",

      "\u100d\u103e"
    },
    {
      "\u100e\u103e\u103e",

      "\u100e\u103e"
    },
    {
      "\u103b\u107f\u1080\u1081\u100f",

      "\u100f\u103c"
    },
    {
      "ျျမန္​မာကာမန္​မာကာ",
      "မြန်​မာကာမန်​မာကာ"
    },
    {
      "အခ်စ္႔႔သီေသာ pdf",
      "အချစ့်သီသော pdf"
    },
    {
      "တကယ္ဆို အျငိႈးေတြမဲ႔ေသာလမ္းေသာလမ္းမွာ တိုႈျပန္ဆံုျကတဲ႔အခါ ",

      "တကယ်ဆို အငြှိုးတွေမဲ့သောလမ်းသောလမ်းမှာ တှိုပြန်ဆုံကြတဲ့အခါ "
    },
    {
      "ျမန္္မာကို",
      "မြန်မာကို"
    },
    {
      "တာ ့မိမိရဲ့",
      "တာ့ မိမိရဲ့",
    },
    // Odd spacing and strange combinations, for more testing later.
    /*
      {
      "ညီနဲ ့်",
      "ညီနျဲ့"
      },
      {
      "တာ ့မိမိရဲ့",
      "တာ့ မိမိရဲ့",
      },
      {
      "ထားေပး်ခင္းအား်ဖင္ ့",
      "ထားပျေးခင်းအျားဖင့်"
      },
      {
      "လယၱီေ်မ",  // Found in searches
      "လယ္တျီမေ"
      }
    */

  };


  @BeforeClass
  public static void setup() {
    /*
       System.out.println("zNorm: " + zNorm.printAll());
       System.out.println("Z-->U: " + z2U.printAll());
       System.out.println("U-->Z: " + u2Z.printAll());
    */
  }

  // Service function to get hex values.
  public static String unicodeToHex(String s) {
    String output = "";
    char[] chars = s.toCharArray();

    for (int i = 0; i < chars.length; i++) {
      output += " " + Long.toHexString((long)chars[i]);
    }
    return output;
  }


  @Test
  public void sanity() {
    String input = "hello world";
    String actual = zNorm.convert(input);
    assertWithMessage("sanity zNorm").that(actual).isEqualTo("hello world");

    actual = z2U.convert(input);
    assertWithMessage("sanity Z2U").that(actual).isEqualTo("hello world");

    actual = u2Z.convert(input);
    assertWithMessage("sanity U2Z").that(actual).isEqualTo("hello world");
  }

  @Test
  public void z2UT17() {
    String z1 = "\u1011\u1039";
    String u1 = "\u1011\u103a";
    String r1 = z2U.convert(z1);
    assertWithMessage("Simple Z->U").that(r1).isEqualTo(u1);
  }

  @Test
  public void u2ZT1() {
    String z1 = "\u1011\u1039";
    String u1 = "\u1011\u103a";

    String r1 = u2Z.convert(u1);
    String line = "test U2Z\n" +
                  "  input  = " + unicodeToHex(u1) + "\n" +
                  "  output = " + unicodeToHex(r1);

    assertWithMessage("line").that(r1).isEqualTo(z1);
  }

  @Test
  public void zNormTests() {
    String z1 = "အကာင္ ့";
    String z1NormExpected = "အကာင့္";
    String result = zNorm.convert(z1);

    String line = " !!!! zNorm: z1\n" +
                  "  input  = " + unicodeToHex(z1) + "\n" +
                  "  output = " + unicodeToHex(result);
    assertWithMessage(line).that(result).isEqualTo(z1NormExpected);
  }

  @Test
  // Simple Zawgyi conversion test.
  public void z2UT2() {
    String z1 = "\u1011\u102D\u102F\u1004\u1039\u1038\u1000\u102D\u102F";
    String u1 = "\u1011\u102D\u102F\u1004\u103A\u1038\u1000\u102D\u102F";
    String r1 = z2U.convert(z1);
    String line = "    input  = " + unicodeToHex(z1) + "\n" +
                  "    output = " + unicodeToHex(r1) + "\n" +
                  "    expect = " + unicodeToHex(u1) + "\n";
    assertWithMessage("!!! Simple Z->U" + line).that(u1).isEqualTo(r1);
  }

  @Test
  /**
   * All Zawgyi -> Unicode tests.
   */
  public void z2UTests() {
    int i = 0;
    for (String[] testCase : testCases) {
      String zIn = testCase[0];
      String expected = testCase[1];
      String actual = z2U.convert(zIn);

      String line = " !!!! Z2U Test " + i + "\n" +
                  "  input  = " + unicodeToHex(zIn) + "\n" +
                  "  expect = " + unicodeToHex(expected) + "\n" +
                  "  actual = " + unicodeToHex(actual) + "\n";

      assertWithMessage(line).that(actual).isEqualTo(expected);
      i += 1;
    }
  }

  @Test
  /**
   * All Zawgyi -> Unicode tests.
   */
  public void u2ZTests() {
    int i = 0;
    for (String[] testCase : testCases) {
      String uIn = testCase[1];
      String expected = testCase[0];
      String actual = u2Z.convert(uIn);

      String line = " !!!! U2Z Test " + i + "\n" +
                  "  input  = " + unicodeToHex(uIn) + "\n" +
                  "  expect = " + unicodeToHex(expected) + "\n" +
                  "  actual = " + unicodeToHex(actual);

      assertWithMessage(line).that(zNorm.convert(actual)).isEqualTo(zNorm.convert(expected));
      i += 1;
    }
  }

}
