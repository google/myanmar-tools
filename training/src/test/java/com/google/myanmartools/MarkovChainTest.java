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

import static com.google.common.truth.Truth.assertWithMessage;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MarkovChainTest {

  @Test
  public void testBasicChainA() {
    double[][] expecteds = {
      {Math.log(1) - Math.log(5), Math.log(2) - Math.log(5), Math.log(2) - Math.log(5)},
      {Math.log(3) - Math.log(5), Math.log(1) - Math.log(5), Math.log(1) - Math.log(5)},
      {Math.log(1) - Math.log(4), Math.log(1) - Math.log(4), Math.log(2) - Math.log(4)}
    };
    testMarkovChain(TestObjects.BASIC_CHAIN_A, expecteds);
  }

  @Test
  public void testBasicChainB() {
    double[][] expecteds = {
      {Math.log(1) - Math.log(4), Math.log(2) - Math.log(4), Math.log(1) - Math.log(4)},
      {Math.log(2) - Math.log(5), Math.log(1) - Math.log(5), Math.log(2) - Math.log(5)},
      {Math.log(1) - Math.log(5), Math.log(1) - Math.log(5), Math.log(3) - Math.log(5)}
    };
    testMarkovChain(TestObjects.BASIC_CHAIN_B, expecteds);
  }

  private void testMarkovChain(MarkovChain chain, double[][] expectedLogProbabilities) {
    for (int i = 0; i < 3; i++) {
      double rowSum = 0.0;
      for (int j = 0; j < 3; j++) {
        float expected = (float) expectedLogProbabilities[i][j];
        float actual = chain.getLogTransitionProbability(i, j);
        assertWithMessage(i + " -> " + j).that(actual).isWithin((float) 1e-6).of(expected);
        rowSum += Math.exp(actual);
      }
      assertWithMessage("Row sum " + i).that(rowSum).isWithin(1e-6).of(1.0);
    }
  }

  @Test
  public void testEquals() {
    MarkovChain likeBasicA =
        new MarkovChain.Builder(3)
            .addEdge(0, 1)
            .addEdge(0, 2)
            .addEdge(1, 0)
            .addEdge(1, 0)
            .addEdge(2, 2)
            .build();
    MarkovChain unlikeBasic =
        new MarkovChain.Builder(3)
            .addEdge(0, 0)
            .addEdge(0, 1)
            .addEdge(1, 2)
            .addEdge(1, 2)
            .addEdge(2, 1)
            .build();

    // Test #equals() and #hashCode() using Guava EqualsTester
    new EqualsTester()
        .addEqualityGroup(TestObjects.BASIC_CHAIN_A, likeBasicA)
        .addEqualityGroup(TestObjects.BASIC_CHAIN_B)
        .addEqualityGroup(unlikeBasic)
        .testEquals();
  }
}
