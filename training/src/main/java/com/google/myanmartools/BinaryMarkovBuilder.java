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

import com.google.myanmartools.BinaryMarkov;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/** A builder for {@link BinaryMarkov}. */
public class BinaryMarkovBuilder {

  /** Magic number used to identify this object in byte streams. (Reads in ASCII as "BMARKOV ") */
  private static final long BINARY_TAG = 0x424D41524B4F5620L;

  /** Current serial format version number, used in association with the magic number. */
  private static final int BINARY_VERSION = 0;

  final short size;
  final int[][] transitionCountsA;
  final int[][] transitionCountsB;

  public BinaryMarkovBuilder(short size) {
    this.size = size;
    transitionCountsA = new int[size][size];
    transitionCountsB = new int[size][size];
  }

  /**
   * Adds an edge to the Markov chain of the specified class, increasing the probability of
   * transitioning between the given nodes of the given class.
   *
   * @param i1 The index of the source node to transition from.
   * @param i2 The index of the destination node to transition to.
   * @param classA true if this transition should be added to class A's chain; false to add this
   *     transition to class B's chain.
   * @return The builder, for chaining.
   */
  public BinaryMarkovBuilder addEdge(int i1, int i2, boolean classA) {
    int[][] transitionCounts = classA ? transitionCountsA : transitionCountsB;
    // transitionCounts[i1][i2]++ with an exception upon integer overflow
    transitionCounts[i1][i2] = Math.incrementExact(transitionCounts[i1][i2]);
    return this;
  }

  /**
   * Exports this instance to a binary data stream.
   *
   * @throws IOException If there was a problem writing the data.
   */
  public void buildToStream(OutputStream stream) throws IOException {
    // Compute the log probability difference matrix.
    float[][] logProbabilityDifferences = computeLogProbabilityDifferences();

    // Write magic number and serial version number.
    DataOutputStream dos = new DataOutputStream(stream);
    dos.writeLong(BINARY_TAG);
    dos.writeInt(BINARY_VERSION);
    dos.writeShort(size);

    for (short i1 = 0; i1 < size; i1++) {
      // Compute the fallback (the most common) and how many values are different than the fallback.
      // Can't use Java 8 streams to construct the counter because the raw data is of type float,
      // and there is no Stream primitive for float.
      Map<Float, Long> counter = new HashMap<>();
      for (float f : logProbabilityDifferences[i1]) {
        counter.put(f, counter.getOrDefault(f, 0L) + 1);
      }
      Map.Entry<Float, Long> mostCommon =
          counter.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get();
      float fallback = mostCommon.getKey();
      short entries = (short) (size - mostCommon.getValue().longValue());
      dos.writeShort(entries);
      if (entries == 0) {
        // Do not write fallback if entries == 0 because it is guaranteed to be zero
        assert fallback == 0.0f;
        continue;
      }
      dos.writeFloat(fallback);

      // Write out the rest of the values in the array.
      for (short i2 = 0; i2 < size; i2++) {
        float curr = logProbabilityDifferences[i1][i2];
        if (curr != fallback) {
          dos.writeShort(i2);
          dos.writeFloat(curr);
          entries--;
        }
      }
      assert entries == 0;
    }
  }

  /** Convenience method to obtain a BinaryMarkov instance directly. */
  public BinaryMarkov buildObject() {
    float[][] logProbabilityDifferences = computeLogProbabilityDifferences();
    return new BinaryMarkov(logProbabilityDifferences);
  }

  private float[][] computeLogProbabilityDifferences() {
    float[][] logProbabilityDifferences = new float[size][size];
    for (int i1 = 0; i1 < size; i1++) {
      int sumA = sumTransitionCountsRow(transitionCountsA[i1]);
      int sumB = sumTransitionCountsRow(transitionCountsB[i1]);
      for (int i2 = 0; i2 < size; i2++) {
        float logProbabilityA = computeLogTransitionProbability(transitionCountsA[i1][i2], sumA);
        float logProbabilityB = computeLogTransitionProbability(transitionCountsB[i1][i2], sumB);
        logProbabilityDifferences[i1][i2] = logProbabilityA - logProbabilityB;
      }
    }
    return logProbabilityDifferences;
  }

  private static int sumTransitionCountsRow(int[] row) {
    // A "base weight" of 1 is added to every transition to prevent divisions by zero.
    return IntStream.of(row).reduce(row.length, (s, x) -> Math.addExact(s, x));
  }

  private static float computeLogTransitionProbability(int rawCount, int sum) {
    int count = Math.addExact(rawCount, 1); // Base weight
    // probability = count / sum
    // log(probability) = log(count / sum) = log(count) - log(sum)
    // Downcast to a float to reduce memory footprint.
    return (float) (Math.log(count) - Math.log(sum));
  }
}
