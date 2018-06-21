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

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.io.Resources;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Generates compatibility_test_data.tsv. Should be run whenever training data is updated. See
 * README.md for details.
 */
public class GenerateCompatibilityTSV {

  private static InputStream getResourceAsStream(String path) throws IOException {
    // Near-equivalent: GenerateCompatibilityTSV.class.getResourceAsStream(path);
    // Guava is better because it throws an exception if the resource does not exist.
    return Resources.asByteSource(Resources.getResource(path)).openStream();
  }

  public static void main(String[] args) throws IOException {
    BufferedReader tsvReader =
        new BufferedReader(new InputStreamReader(getResourceAsStream("com/google/myanmartools/compatibility.tsv"), UTF_8));
    ZawgyiDetector detector = new ZawgyiDetector(getResourceAsStream("com/google/myanmartools/zawgyiUnicodeModel.dat"));
    String line;
    while ((line = tsvReader.readLine()) != null) {
      String input = line.split("\t", -1)[1].trim();
      double newScore = detector.getZawgyiProbability(input);
      System.out.print(Double.toString(newScore));
      System.out.print("\t");
      System.out.println(input);
    }
    tsvReader.close();
  }
}
