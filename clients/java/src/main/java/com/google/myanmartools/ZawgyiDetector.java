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

import java.io.IOException;
import java.io.InputStream;

/**
 * Uses a machine learning model to determine whether a string of text is Zawgyi or Unicode.
 *
 * <p>For more details and documentation, see https://github.com/google/myanmar-tools
 */
public final class ZawgyiDetector {
  final ZawgyiUnicodeMarkovModel model;

  /** Loads the model from the resource and returns a ZawgyiDetector instance. */
  public ZawgyiDetector() {
    try (InputStream inStream =
        ZawgyiDetector.class.getResourceAsStream("/com/google/myanmartools/zawgyiUnicodeModel.dat")) {
      if (inStream == null) {
        throw new IOException("Model file zawgyiUnicodeModel.dat not found");
      }
      model = new ZawgyiUnicodeMarkovModel(inStream);
    } catch (IOException e) {
      throw new RuntimeException("Could not load Markov model from resource file", e);
    }
  }

  /**
   * Loads the model from the specified stream instead of the default resource.
   *
   * @throws IOException If there is trouble reading from the stream.
   */
  ZawgyiDetector(InputStream inStream) throws IOException {
    model = new ZawgyiUnicodeMarkovModel(inStream);
  }

  /**
   * Performs detection on the given string. Returns the probability that the string is Zawgyi given
   * that it is either Unicode or Zawgyi. Values approaching 1 are strong Zawgyi; values approaching
   * 0 are strong Unicode; and values close to 0.5 are toss-ups.
   *
   * <p>If the string does not contain any Myanmar range code points, -Infinity is returned.
   *
   * @param input The string on which to run detection.
   * @param verbose If true, print debugging information to standard output.
   * @return The probability that the string is Zawgyi (between 0 and 1), or -Infinity if the string
   *     contains no Myanmar range code points.
   */
  public double getZawgyiProbability(String input, boolean verbose) {
    return model.predict(input, verbose);
  }

  /**
   * Performs detection on the given string. Returns the probability that the string is Zawgyi given
   * that it is either Unicode or Zawgyi. Values approaching 1 are strong Zawgyi; values approaching
   * 0 are strong Unicode; and values close to 0.5 are toss-ups.
   *
   * <p>If the string does not contain any Myanmar range code points, -Infinity is returned.
   *
   * @param input The string on which to run detection.
   * @return The probability that the string is Zawgyi (between 0 and 1), or -Infinity if the string
   *     contains no Myanmar range code points.
   */
  public double getZawgyiProbability(String input) {
    return getZawgyiProbability(input, false);
  }
}
