package com.google.i18n.myanmar;

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
