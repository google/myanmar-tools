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

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.google.myanmartools.ZawgyiDetector;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Stream;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ZawgyiDetectorTestingDataTest {

  private static ZawgyiDetector detector;
  private static List<String> unicodeTestData;
  private static List<String> zawgyiTestData;

  @BeforeClass
  public static void setup() throws Exception {
    try {
      unicodeTestData = loadStrings(false);
      zawgyiTestData = loadStrings(true);
    } catch (IllegalArgumentException e) {
      // Gracefully ignore the test if testData.tsv is not found.
      org.junit.Assume.assumeNoException(e);
    }

    URL modelResource = Resources.getResource("com/google/myanmartools/zawgyiUnicodeModel.dat");
    detector = new ZawgyiDetector(Resources.asByteSource(modelResource).openStream());
  }

  private static ImmutableList<String> loadStrings(boolean zawgyi) throws IOException {
    try (Stream<String> lines =
        Resources.asCharSource(Resources.getResource("com/google/myanmartools/testData.tsv"), UTF_8).lines()) {
      return lines
          .map(line -> line.trim())
          .filter(line -> !line.isEmpty())
          .filter(line -> line.charAt(0) == (zawgyi ? 'Z' : 'U'))
          .map(line -> line.substring(2))
          .collect(toImmutableList());
    }
  }

  /* TODO: This test cannot be run during testing because it requires the corpus.
   * Figure out a workaround.
  public static void testDataHealth() throws IOException {
    // Make sure training and test data don't overlap
    assertWithMessage("Unicode training and test data should not intersect")
        .that(BurmeseData.getUnicodeTrainingData())
        .containsNoneIn(BurmeseData.getUnicodeTestingData());
    assertWithMessage("Zawgyi training and test data should not intersect")
        .that(BurmeseData.getZawgyiTrainingData())
        .containsNoneIn(BurmeseData.getZawgyiTestingData());

    // Make sure the data functions always return the same values
    assertWithMessage("Unicode training data should always return the same contents")
        .that(BurmeseData.getUnicodeTrainingData())
        .containsExactlyElementsIn(BurmeseData.getUnicodeTrainingData());
    assertWithMessage("Unicode test data should always return the same contents")
        .that(BurmeseData.getUnicodeTestingData())
        .containsExactlyElementsIn(BurmeseData.getUnicodeTestingData());
    assertWithMessage("Zawgyi training data should always return the same contents")
        .that(BurmeseData.getZawgyiTrainingData())
        .containsExactlyElementsIn(BurmeseData.getZawgyiTrainingData());
    assertWithMessage("Zawgyi test data should always return the same contents")
        .that(BurmeseData.getZawgyiTestingData())
        .containsExactlyElementsIn(BurmeseData.getZawgyiTestingData());
  }
  */

  @Test
  public void zawgyiErrorIncreasesWithHigherThreshold() {
    List<String> data = zawgyiTestData;
    double errorLowThreshold = getErrorRate(data, 0.01, true);
    double errorMiddleThreshold = getErrorRate(data, 0.5, true);
    double errorHighThreshold = getErrorRate(data, 0.99, true);
    System.out.format(
        "Actual error rates for Zawgyi: %.4f, %.4f, %.4f%n",
        errorLowThreshold, errorMiddleThreshold, errorHighThreshold);
    // Note: "isAtMost" means "isLessThanOrEqualTo"
    assertWithMessage("0.01 to 0.5").that(errorLowThreshold).isAtMost(errorMiddleThreshold);
    assertWithMessage("0.5 to 0.99").that(errorMiddleThreshold).isAtMost(errorHighThreshold);
  }

  @Test
  public void unicodeErrorDecreasesWithHigherThreshold() {
    List<String> data = unicodeTestData;
    double errorLowThreshold = getErrorRate(data, 0.01, false);
    double errorMiddleThreshold = getErrorRate(data, 0.5, false);
    double errorHighThreshold = getErrorRate(data, 0.99, false);
    System.out.format(
        "Actual error rates for Unicode: %.4f, %.4f, %.4f%n",
        errorLowThreshold, errorMiddleThreshold, errorHighThreshold);
    // Note: "isAtLeast" means "isGreaterThanOrEqualTo"
    assertWithMessage("0.01 to 0.5").that(errorLowThreshold).isAtLeast(errorMiddleThreshold);
    assertWithMessage("0.5 to 0.99").that(errorMiddleThreshold).isAtLeast(errorHighThreshold);
  }

  @Test
  public void threshold99MeansUnicodeErrorLessThan01() {
    double actualUnicodeError = getErrorRate(unicodeTestData, 0.99, false);
    assertThat(actualUnicodeError).isLessThan(0.01);
  }

  @Test
  public void threshold01MeansZawgyiErrorLessThan01() {
    double actualZawgyiError = getErrorRate(zawgyiTestData, 0.01, true);
    assertThat(actualZawgyiError).isLessThan(0.01);
  }

  private static double getErrorRate(List<String> data, double threshold, boolean isZawgyi) {
    int failures = 0;
    for (String input : data) {
      double probability = detector.getZawgyiProbability(input);

      // Ignore if probability is -Infinity (no signal in test string)
      if (probability < 0) {
        continue;
      }

      if ((isZawgyi && probability < threshold) || (!isZawgyi && probability > threshold)) {
        failures++;
      }
    }
    return 1.0 * failures / data.size();
  }
}
