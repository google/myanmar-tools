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

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * A general implementation of a Markov chain. This class is used for testing purposes only to
 * ensure that BinaryMarkovClassifier behaves identically to two Markov chains.
 *
 * <p>The {@link Builder#addEdge} method is used to increase the probability of a certain transition
 * during training. All edge weights start at 1 to prevent division by zero errors.
 *
 * <p>The {@link #getLogTransitionProbability} method returns the natural logarithm of the
 * probability of making the stated transition based on training.
 *
 * <p>For more information on Markov chains, read here: https://en.wikipedia.org/wiki/Markov_chain
 */
public final class MarkovChain {

  /** A Builder used to train an instance of MarkovChain. */
  public static final class Builder {
    final int size;
    final int[][] transitionCounts;

    public Builder(int size) {
      this.size = size;
      this.transitionCounts = new int[size][size];
    }

    /**
     * Adds an edge to the Markov chain, increasing the probability of transitioning between the
     * given nodes.
     *
     * @param i1 The index of the source node to transition from.
     * @param i2 The index of the destination node to transition to.
     * @return The builder, for chaining.
     */
    public Builder addEdge(int i1, int i2) {
      // transitionCounts[i1][i2]++ with an exception upon integer overflow
      transitionCounts[i1][i2] = Math.incrementExact(transitionCounts[i1][i2]);
      return this;
    }

    public MarkovChain build() {
      float[][] logTransitionProbabilities = new float[size][size];
      for (int i = 0; i < size; i++) {
        // A "base weight" of 1 is added to every transition to prevent divisions by zero.
        int sum = IntStream.of(transitionCounts[i]).reduce(0, (s, x) -> Math.addExact(s, x));
        sum = Math.addExact(sum, size); // Base weight
        for (int j = 0; j < size; j++) {
          int count = transitionCounts[i][j];
          count = Math.addExact(count, 1); // Base weight
          // probability = count / sum
          // log(probability) = log(count / sum) = log(count) - log(sum)
          // Downcast to a float to reduce memory footprint.
          float result = (float) (Math.log(count) - Math.log(sum));
          logTransitionProbabilities[i][j] = result;
        }
      }
      return new MarkovChain(logTransitionProbabilities);
    }
  }

  final float[][] logTransitionProbabilities;

  private MarkovChain(float[][] logTransitionProbabilities) {
    this.logTransitionProbabilities = logTransitionProbabilities;
  }

  /**
   * Returns the log probability of the transition between the given nodes. The logarithm is base
   * <em>e</em> (natural logarithm) in accordance with {@link Math#log}.
   *
   * @param i1 The index of the source node to transition from.
   * @param i2 The index of the destination node to transition to.
   * @return The log probability of transitioning from i1 to i2.
   */
  public float getLogTransitionProbability(int i1, int i2) {
    return logTransitionProbabilities[i1][i2];
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof MarkovChain)) {
      return false;
    }
    return Arrays.deepEquals(
        logTransitionProbabilities, ((MarkovChain) other).logTransitionProbabilities);
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(logTransitionProbabilities);
  }
}
