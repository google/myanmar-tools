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
import com.google.myanmartools.BinaryMarkovBuilder;

/**
 * Shared immutable objects used in unit tests.
 */
public class TestObjects {

  static final MarkovChain BASIC_CHAIN_A =
      new MarkovChain.Builder(3)
          .addEdge(0, 1)
          .addEdge(0, 2)
          .addEdge(1, 0)
          .addEdge(1, 0)
          .addEdge(2, 2)
          .build();

  static final MarkovChain BASIC_CHAIN_B =
      new MarkovChain.Builder(3)
          .addEdge(0, 1)
          .addEdge(1, 0)
          .addEdge(1, 2)
          .addEdge(2, 2)
          .addEdge(2, 2)
          .build();

  static final BinaryMarkov A_MINUS_B =
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
}
