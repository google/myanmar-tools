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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * A class that behaves as if it were two Markov chains, called Chain A and Chain B. Whereas a
 * normal Markov chain would expose the log probability of a transition, this class exposes the
 * difference between the log probabilities of the two Markov chains. When training, you can specify
 * which Markov chain a transition should be added to.
 *
 * <p>The reasoning behind this class is that it has a smaller data footprint than two separate
 * Markov chain objects.
 *
 * <p>This class is tested in the myanmar-tools-training package because this implementation does
 * not include any logic for building a new instance other than from a binary data stream.
 */
class BinaryMarkov {

  /** Magic number used to identify this object in byte streams. (Reads in ASCII as "BMARKOV ") */
  private static final long BINARY_TAG = 0x424D41524B4F5620L;

  /** Current serial format version number, used in association with the magic number. */
  private static final int BINARY_VERSION = 0;

  final float[][] logProbabilityDifferences;

  /** Internal constructor used by BinaryMarkovBuilder */
  BinaryMarkov(float[][] logProbabilityDifferences) {
    this.logProbabilityDifferences = logProbabilityDifferences;
  }

  /**
   * Creates an instance from a binary data stream.
   *
   * @throws IOException If there was a problem reading the data.
   */
  public BinaryMarkov(InputStream stream) throws IOException {
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
    short size = dis.readShort();
    float[][] logProbabilityDifferences = new float[size][size];
    for (short i1 = 0; i1 < size; i1++) {
      short entries = dis.readShort();
      float fallback = (entries == 0) ? 0.0f : dis.readFloat();
      short next = -1;
      for (short i2 = 0; i2 < size; i2++) {
        if (entries > 0 && next < i2) {
          next = dis.readShort();
          entries--;
        }
        logProbabilityDifferences[i1][i2] = (next == i2) ? dis.readFloat() : fallback;
      }
    }
    this.logProbabilityDifferences = logProbabilityDifferences;
  }

  /**
   * Gets the difference in log probabilities between chain A and chain B. This behaves as if you
   * had two Markov chains and called <code>
   * chainA.getLogProbability(i1, i2) - chainB.getLogProbability(i1, i2)</code>.
   *
   * @param i1 The index of the source node to transition from.
   * @param i2 The index of the destination node to transition to.
   * @return The difference between A and B in log probability of transitioning from i1 to i2.
   */
  public float getLogProbabilityDifference(int i1, int i2) {
    return logProbabilityDifferences[i1][i2];
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof BinaryMarkov)) {
      return false;
    }
    return Arrays.deepEquals(
        logProbabilityDifferences, ((BinaryMarkov) other).logProbabilityDifferences);
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(logProbabilityDifferences);
  }
}
