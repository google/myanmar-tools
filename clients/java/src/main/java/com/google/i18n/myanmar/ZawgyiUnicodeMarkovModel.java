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

package com.google.i18n.myanmar;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * A Markov model to predict whether a string is more likely Unicode or Zawgyi.
 *
 * <p>Internally, this class maintains two Markov chains, one representing Unicode and the other
 * representing Zawgyi. An input string is evaluated against both chains, and the chain that returns
 * the higher probability becomes the prediction.
 *
 * <p>A string is evaluated as a sequence of transitions between states, including transitions to
 * the edges of the string. For example, the string "ABC" contains 4 transitions: NULL to A, A to B,
 * B to C, and C to NULL.
 *
 * <p>For the purposes of Unicode/Zawgyi detection, all characters are treated as the NULL state
 * except for characters in the Myanmar script or characters in the Unicode whitespace range U+2000
 * through U+200B.
 *
 * <p>This class is tested in the google-myanmar-tools-data package because this implementation does
 * not include any logic for building a new instance other than from a binary data stream.
 */
class ZawgyiUnicodeMarkovModel {

  /** Magic number used to identify this object in byte streams. (Reads in ASCII as "UZMODEL") */
  private static final long BINARY_TAG = 0x555A4D4F44454C20L;

  /** Current serial format version number, used in association with the magic number. */
  private static final int BINARY_VERSION = 1;

  // Standard Myanmar code point range before digits
  private static final int STD_CP0 = '\u1000';
  private static final int STD_CP1 = '\u103F';

  // Standard Myanmar code point range after digits
  private static final int AFT_CP0 = '\u104A';
  private static final int AFT_CP1 = '\u109F';

  // Extended Myanmar code point range A
  private static final int EXA_CP0 = '\uAA60';
  private static final int EXA_CP1 = '\uAA7F';

  // Extended Myanmar code point range B
  private static final int EXB_CP0 = '\uA9E0';
  private static final int EXB_CP1 = '\uA9FF';

  // Unicode space characters
  private static final int SPC_CP0 = '\u2000';
  private static final int SPC_CP1 = '\u200B';

  // Indices into Markov nodes
  private static final short STD_OFFSET = 1;
  private static final short AFT_OFFSET = STD_OFFSET + STD_CP1 - STD_CP0 + 1;
  private static final short EXA_OFFSET = AFT_OFFSET + AFT_CP1 - AFT_CP0 + 1;
  private static final short EXB_OFFSET = EXA_OFFSET + EXA_CP1 - EXA_CP0 + 1;
  private static final short SPC_OFFSET = EXB_OFFSET + EXB_CP1 - EXB_CP0 + 1;
  private static final short NUM_STATES = SPC_OFFSET + SPC_CP1 - SPC_CP0 + 1;

  /**
   * Returns the index of the state in the Markov chain corresponding to the given code point.
   *
   * <p>Code points in the standard Myanmar range, Myanmar Extended A, Myanmar Extended B, and
   * Unicode Whitespace each have a unique state assigned to them. All other code points are mapped
   * to state 0.
   *
   * <p>Package-private so that the builder can use this method.
   *
   * @param cp The code point to convert to a state index.
   * @return The index of the state in the Markov chain. 0 <= state < getSize()
   */
  static int getIndexForCodePoint(int cp) {
    if (STD_CP0 <= cp && cp <= STD_CP1) {
      return cp - STD_CP0 + STD_OFFSET;
    }
    if (AFT_CP0 <= cp && cp <= AFT_CP1) {
      return cp - AFT_CP0 + AFT_OFFSET;
    }
    if (EXA_CP0 <= cp && cp <= EXA_CP1) {
      return cp - EXA_CP0 + EXA_OFFSET;
    }
    if (EXB_CP0 <= cp && cp <= EXB_CP1) {
      return cp - EXB_CP0 + EXB_OFFSET;
    }
    if (SPC_CP0 <= cp && cp <= SPC_CP1) {
      return cp - SPC_CP0 + SPC_OFFSET;
    }
    return 0;
  }

  /** The number of states in the Markov chain. */
  static short getSize() {
    return NUM_STATES;
  }

  final BinaryMarkov classifier;

  /** Internal constructor used by ZawgyiUnicodeMarkovModelBuilder */
  ZawgyiUnicodeMarkovModel(BinaryMarkov classifier) {
    this.classifier = classifier;
  }

  /**
   * Creates an instance from a binary data stream.
   *
   * @throws IOException If there was a problem reading the data.
   */
  public ZawgyiUnicodeMarkovModel(InputStream stream) throws IOException {
    DataInputStream dis = new DataInputStream(stream);
    // Check magic number and serial version number
    long binaryTag = dis.readLong();
    if (binaryTag != BINARY_TAG) {
      throw new IOException(
          String.format(
              "Unexpected magic number; expected %016X but got %016X", BINARY_TAG, binaryTag));
    }
    int binaryVersion = dis.readInt();
    if (binaryVersion != BINARY_VERSION) {
      throw new IOException(
          String.format(
              "Unexpected serial version number; expected %08X but got %08X",
              BINARY_VERSION, binaryVersion));
    }
    classifier = new BinaryMarkov(stream);
  }

  /** @param verbose Whether to print the log probabilities for debugging. */
  double predict(String input, boolean verbose) {
    if (verbose) {
      System.out.format("Running detector on string: %s%n", input);
    }

    // Start at the base state
    int prevCp = 0;
    int prevState = 0;

    double totalDelta = 0.0;
    boolean seenTransition = false;
    for (int offset = 0; offset <= input.length(); ) {
      int cp;
      int currState;
      if (offset == input.length()) {
        cp = 0;
        currState = 0;
      } else {
        cp = input.codePointAt(offset);
        currState = getIndexForCodePoint(cp);
      }
      // Ignore 0-to-0 transitions
      if (prevState != 0 || currState != 0) {
        float delta = classifier.getLogProbabilityDifference(prevState, currState);
        if (verbose) {
          System.out.format("U+%04X -> U+%04X: delta=%8.4f ", prevCp, cp, delta);
          for (int i = 1; i < Math.abs(delta); i++) {
            System.out.print("!");
          }
          System.out.println();
        }
        totalDelta += delta;
        seenTransition = true;
      }
      offset += Character.charCount(cp);
      prevCp = cp;
      prevState = currState;
    }

    if (verbose) {
      System.out.format("Final: delta=%.4f%n", totalDelta);
    }

    // Special case: if there is no signal, return -Infinity,
    // which will get interpreted by users as strong Unicode.
    // This happens when the input string contains no Myanmar-range code points.
    if (!seenTransition) {
      return Double.NEGATIVE_INFINITY;
    }

    // result = Pz/(Pu+Pz)
    //        = exp(logPz)/(exp(logPu)+exp(logPz))
    //        = 1/(1+exp(logPu-logPz))
    return 1.0 / (1.0 + Math.exp(totalDelta));
  }

  /**
   * Runs the given input string on both internal Markov chains and computes the probability of the
   * string being unicode or zawgyi.
   *
   * @param input The string to evaluate.
   * @return The probability that the string is Zawgyi given that it is either Unicode or Zawgyi, or
   *     -Infinity if there are no Myanmar range code points in the string.
   */
  public double predict(String input) {
    return predict(input, false);
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof ZawgyiUnicodeMarkovModel)) {
      return false;
    }
    return Objects.equals(classifier, ((ZawgyiUnicodeMarkovModel) other).classifier);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(classifier);
  }
}
