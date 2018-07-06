/*
 * Copyright 2017 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.myanmartools;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LoadTest {

  static final String UNICODE_INPUT =
      "၁၉၄၈ ခုနှစ်၊ ဒီဇင်ဘာလ ၁၀ ရက်နေ့တွင် ကမ္ဘာ့ကုလသမဂ္ဂအဖွဲ့ ညီလာခံအစည်းအဝေးကြီးက လူ့အခွင့်အရေးကြေညာစာတမ်းကြီးကို အတည်ပြု၍ ကြေညာလိုက်ရာ ထိုကြေညာစာတမ်းကြီး၏ စာသားသည်နောက်စာမျက်နှာ များတွင် အပြည့်အစုံပါရှိသည်။ ဤကဲ့သို့ ရာဇဝင်တင်မည့် ကြေညာချက်ကို ပြုလုပ်ပြီးနောက် ဤညီလာခံအစည်းအဝေးကြီးက ကမ္ဘာ့ကုလသမဂ္ဂအဖွဲ့ဝင် နိုင်ငံ အားလုံးအား ထိုကြေညာစာတမ်းကြီး၏ စာသားကိုအများပြည်သူတို့ ကြားသိစေရန် ကြေညာပါမည့် အကြောင်းကိုလည်းကောင်း၊ ထို့ပြင်နိုင်ငံများ၊ သို့တည်းမဟုတ် နယ်မြေများ၏ နိုင်ငံရေး အဆင့်အတန်းကို လိုက်၍ ခွဲခြားခြင်း မပြုဘဲအဓိကအားဖြင့် စာသင်ကျောင်းများနှင့် အခြားပညာရေး အဖွဲ့အစည်းများတွင် ထိုကြေညာစာတမ်းကြီးကို ဖြန့်ချိ ဝေငှ စေရန်၊ မြင်သာအောင် ပြသထားစေရန်၊ ဖတ်ကြားစေရန်နှင့် အဓိပ္ပာယ်ရှင်းလင်း ဖော်ပြစေရန် ဆောင်ရွက်ပါမည့် အကြောင်းဖြင့် လည်းကောင်း ဆင့်ဆို လိုက်သည်။";

  static final String ZAWGYI_INPUT =
      "၁၉၄၈ ခုႏွစ္၊ ဒီဇင္ဘာလ ၁၀ ရက္ေန႔တြင္ ကမၻာ့ကုလသမဂၢအဖြဲ႕ ညီလာခံအစည္းအေဝးႀကီးက လူ႔အခြင့္အေရးေၾကညာစာတမ္းႀကီးကို အတည္ျပဳ၍ ေၾကညာလိုက္ရာ ထိုေၾကညာစာတမ္းႀကီး၏ စာသားသည္ေနာက္စာမ်က္ႏွာ မ်ားတြင္ အျပည့္အစုံပါရွိသည္။ ဤကဲ့သို႔ ရာဇဝင္တင္မည့္ ေၾကညာခ်က္ကို ျပဳလုပ္ၿပီးေနာက္ ဤညီလာခံအစည္းအေဝးႀကီးက ကမၻာ့ကုလသမဂၢအဖြဲ႕ဝင္ နိုင္ငံ အားလုံးအား ထိုေၾကညာစာတမ္းႀကီး၏ စာသားကိုအမ်ားျပည္သူတို႔ ၾကားသိေစရန္ ေၾကညာပါမည့္ အေၾကာင္းကိုလည္းေကာင္း၊ ထို႔ျပင္နိုင္ငံမ်ား၊ သို႔တည္းမဟုတ္ နယ္ေျမမ်ား၏ နိုင္ငံေရး အဆင့္အတန္းကို လိုက္၍ ခြဲျခားျခင္း မျပဳဘဲအဓိကအားျဖင့္ စာသင္ေက်ာင္းမ်ားႏွင့္ အျခားပညာေရး အဖြဲ႕အစည္းမ်ားတြင္ ထိုေၾကညာစာတမ္းႀကီးကို ျဖန္႔ခ်ိ ေဝငွ ေစရန္၊ ျမင္သာေအာင္ ျပသထားေစရန္၊ ဖတ္ၾကားေစရန္ႏွင့္ အဓိပၸာယ္ရွင္းလင္း ေဖာ္ျပေစရန္ ေဆာင္ရြက္ပါမည့္ အေၾကာင္းျဖင့္ လည္းေကာင္း ဆင့္ဆို လိုက္သည္။";

  @Test
  public void udhr_z2u() {
    TransliterateZ2U converter = new TransliterateZ2U("Z-->U");
    for (long i = 0; i < 1000; i++) {
      converter.convert(ZAWGYI_INPUT);
    }
  }

  @Test
  public void udhr_u2z() {
    TransliterateU2Z converter = new TransliterateU2Z("U-->Z");
    for (long i = 0; i < 1000; i++) {
      converter.convert(UNICODE_INPUT);
    }
  }
}
