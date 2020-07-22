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

package com.google.myanmartools;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import com.google.myanmartools.ZawgyiDetector;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ZawgyiDetectorTest {

  private static ZawgyiDetector detector;

  @BeforeClass
  public static void setup() {
    detector = new ZawgyiDetector();
  }

  @Test
  public void sanity() {
    double actual = detector.getZawgyiProbability("hello world");
    assertThat(actual).isNegativeInfinity();
  }

  @Test
  public void testIgnoreNonMyanmarCodePoints() {
    String allASCII = "blah blah blah blah blah";
    String mixedUnicode = "<span>blah blah ဒဂုန်ဦးစန်းငွေ </span> blah blah blah blah";
    String mixedZawgyi = "blah blah blah blah blah သို႔သြားပါ။ blah blah blah";
    assertWithMessage("All ASCII")
        .that(detector.getZawgyiProbability(allASCII))
        .isEqualTo(Double.NEGATIVE_INFINITY);
    assertWithMessage("Mixed Unicode")
        .that(detector.getZawgyiProbability(mixedUnicode))
        .isLessThan(0.01);
    assertWithMessage("Mixed Zawgyi")
        .that(detector.getZawgyiProbability(mixedZawgyi))
        .isGreaterThan(0.99);
  }

  @Test
  public void strongUnicodeReturnsLowScore() {
    String strongUnicode = "အပြည်ပြည်ဆိုင်ရာ လူ့အခွင့်အရေး ကြေညာစာတမ်း";
    assertWithMessage("Strong Unicode")
        .that(detector.getZawgyiProbability(strongUnicode))
        .isLessThan(0.001);
  }

  @Test
  public void strongZawgyiReturnsHighScore() {
    String strongZawgyi = "အျပည္ျပည္ဆိုင္ရာ လူ႔အခြင့္အေရး ေၾကညာစာတမ္း";
    assertWithMessage("Strong Zawgyi")
        .that(detector.getZawgyiProbability(strongZawgyi))
        .isGreaterThan(0.999);
  }

  @Test
  public void testDifficult() {
    /* These are strings that the detector has gotten wrong. They mostly render in both Zawgyi and
     * Unicode, but the words they spell make sense in one encoding but not in the other. As the
     * detector improves, change this test case with scores to match the new output.
     */
    Object[][] cases = {
      // STRINGS IDENTICAL IN UNICODE/ZAWGYI
      // TODO: These should return scores in the middle range.
      {"အသံကို အစားထိုးလိုပါသလား။", 0.995}, // Truth: Identical in U/Z
      {"နမူနာ", 0.26}, // Truth: Identical in U/Z
      {" ဦး", 0.35}, // Truth: Identical in U/Z

      // UNICODE STRINGS WITH HIGH ZAWGYI SCORES
      // TODO: Fix detection so that these get low Zawgyi scores.
      {"အစားထိုး အထူးအက္ခရာ", 0.995}, // Truth: Unicode (confirmed by yinmay@)
      {"ယခု မိုးရွာနေပါသလား။", 0.995}, // Truth: Unicode (confirmed by yinmay@)
      {"အခြား", 0.74}, // Truth: Unicode (confirmed by yinmay@)

      // DIFFICULT STRINGS THAT DETECT CORRECTLY
      // Changes to the detector should not significantly change these scores.
      {"ကာမစာအုပ္မ်ား(ေစာက္ပတ္စာအုပ္မ်ား)", 1.0}, // Truth: Zawgyi (confirmed by yinmay@)
      {"ညႇပ္စရာမလို", 0.82}, // Truth: Zawgyi (confirmed by yinmay@)
    };
    for (Object[] cas : cases) {
      String input = (String) cas[0];
      double expected = (double) cas[1];
      double actual = detector.getZawgyiProbability(input);
      assertWithMessage(input).that(actual).isWithin(0.005).of(expected);
    }
  }

  @Test
  public void testIgnoreNumerals() {
    // Digits/numerals are ignored and treated like non-Myanmar code points.
    assertThat(detector.getZawgyiProbability("၉၆.၀ kHz")).isNegativeInfinity();
    assertThat(detector.getZawgyiProbability("၂၄၀၉ ဒဂုန်"))
        .isEqualTo(detector.getZawgyiProbability("ဒဂုန်"));
  }

  @Test
  public void testCompatibility() throws IOException {
    InputStream inputStream = ZawgyiDetector.class.getResourceAsStream("/com/google/myanmartools/compatibility.tsv");
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
    String line = null;
    while ((line = reader.readLine()) != null) {
      int tabIdx = line.indexOf('\t');
      double expected = Double.parseDouble(line.substring(0, tabIdx));
      String input = line.substring(tabIdx + 1);
      double actual = detector.getZawgyiProbability(input);
      try {
        // Java Truth has very limited support for double equality testing. Prior to JDK 11, this
        // test passed for both infinite and non-infinite with .isEqualTo(), but now it fails with
        // a small delta.
        if (Double.isInfinite(expected)) {
          assertWithMessage(line).that(actual).isEqualTo(expected);
        } else {
          assertWithMessage(line).that(actual).isWithin(1e-6).of(expected);
        }
      } catch (AssertionError e) {
        // Print verbose output for the failure
        detector.getZawgyiProbability(input, true);
        throw e;
      }
    }
    reader.close();
  }
}
