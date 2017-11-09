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
import com.google.myanmartools.BinaryMarkov;
import com.google.myanmartools.BinaryMarkovBuilder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BinaryMarkovClassifierTest {

  @Test
  public void testBinaryMarkovClassifier() {
    // The return values from getLogProbabilityDifference() should be the same as if we constructed
    // two MarkovChains and took the difference of calls to getLogTransitionProbability().
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        float logProbabilityA = TestObjects.BASIC_CHAIN_A.getLogTransitionProbability(i, j);
        float logProbabilityB = TestObjects.BASIC_CHAIN_B.getLogTransitionProbability(i, j);
        float expected = logProbabilityA - logProbabilityB;
        float actual = TestObjects.A_MINUS_B.getLogProbabilityDifference(i, j);
        assertWithMessage(i + " -> " + j).that(actual).isWithin((float) 1e-6).of(expected);
      }
    }
  }

  @Test
  public void testEquals() {
    BinaryMarkov likeAMinusB =
        new BinaryMarkovBuilder((short) 3)
            .addEdge(0, 1, true)
            .addEdge(0, 2, true)
            .addEdge(1, 0, true)
            .addEdge(1, 0, true)
            .addEdge(2, 2, true)
            .addEdge(0, 1, false)
            .addEdge(1, 0, false)
            .addEdge(1, 2, false)
            .addEdge(2, 2, false)
            .addEdge(2, 2, false)
            .buildObject();
    BinaryMarkov unlikeAMinusB =
        new BinaryMarkovBuilder((short) 3).addEdge(0, 1, true).addEdge(1, 0, true).buildObject();

    // Test #equals() and #hashCode() using Guava EqualsTester
    new EqualsTester()
        .addEqualityGroup(TestObjects.A_MINUS_B, likeAMinusB)
        .addEqualityGroup(unlikeAMinusB)
        .testEquals();
  }

  @Test
  public void testBinaryFormat() throws IOException {
    BinaryMarkovBuilder builder =
        new BinaryMarkovBuilder((short) 3)
            .addEdge(0, 1, true)
            .addEdge(0, 2, false)
            .addEdge(1, 0, true)
            .addEdge(1, 2, false)
            .addEdge(2, 2, true)
            .addEdge(2, 2, false);
    BinaryMarkov object = builder.buildObject();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    builder.buildToStream(outputStream);
    byte[] bytes = outputStream.toByteArray();
    assertWithMessage("Serialized binary Markov classifier should be 52 bytes long")
        .that(bytes.length)
        .isEqualTo(52);
    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
    BinaryMarkov copy = new BinaryMarkov(inputStream);
    assertWithMessage("Deserialized binary Markov classifier should equal the original")
        .that(copy)
        .isEqualTo(object);
  }
}
