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
 * <strong>Java Zawgyi Detector (go/zawgyi)</strong>
 *
 * <p>Uses a machine learning Markov model to predict whether a string is encoded in the Zawgyi font
 * encoding for the Myanmar script.
 *
 * <p>A typical use case should look like this:
 *
 * <p><code><pre>
 * private static final ZawgyiDetector zawgyiDetector = new ZawgyiDetector();
 *
 * if (zawgyiDetector.getZawgyiProbability(input) > THRESHOLD) {
 *   // do stuff
 * }
 * </pre></code>
 *
 * <p>The method {@link #getZawgyiProbability(String)} returns a value between 0 and 1 for the
 * probability that the string is Zawgyi-encoded given that the string is either Unicode or Zawgyi.
 * With this in mind, use the following heuristics to set <code>THRESHOLD</code>:
 *
 * <ul>
 *   <li>If <em>under</em>-predicting Zawgyi is bad (e.g., when a human gets to evaluate the
 *       result), set a low threshold like <code>0.01</code>. This threshold guarantees that fewer
 *       than 1% of Zawgyi strings will go undetected.
 *   <li>If <em>over</em>-predicting Zawgyi is bad (e.g., when conversion will take place
 *       automatically), set a high threshold like <code>0.99</code>. This threshold guarantees that
 *       fewer than 1% of Unicode strings will be wrongly flagged.
 * </ul>
 *
 * <p>You should use this class in the form of a "private static final" singleton instance for
 * optimal performance.
 */
public final class ZawgyiDetector {
  final ZawgyiUnicodeMarkovModel model;

  /** Loads the model from the resource and returns a ZawgyiDetector instance. */
  public ZawgyiDetector() {
    try (InputStream inStream =
        ZawgyiDetector.class.getResourceAsStream("/zawgyiUnicodeModel.dat")) {
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
