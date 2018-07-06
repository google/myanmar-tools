/* Copyright 2018 Google LLC
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

import java.util.ArrayList;

/**
 * A Java engine for running the phases of transliteration, similar to the ICU4J
 * transliterator.
 * This class is thread-safe and has immutable behavior."
 */


public abstract class Transliterate {

  private final ArrayList<Phase> translitPhases;
  private final String name;  // For identification

  private boolean debugMode;  // Print details of execution.

  Transliterate(String id) {
    translitPhases = new ArrayList<>();
    name = id;
  }

  Phase addPhase() {
    Phase newPhase = new Phase();
    newPhase.setInfo(" " + translitPhases.size());
    translitPhases.add(newPhase);
    newPhase.setDebugMode(debugMode);
    return newPhase;
  }

  void setDebugMode(boolean newMode) {
    debugMode = newMode;
    for (Phase phase :translitPhases) {
      phase.setDebugMode(debugMode);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Transliterator name = ").append(name).append("\n");
    sb.append("  Phase count: ").append(translitPhases.size()).append("\n");
    for (Phase p : translitPhases) {
      sb.append(p.printPhase());
    }
    return sb.toString();
  }

  /**
   * Apply the transliteration to the input string,
   * returning the converted result.
   */
  public String convert(CharSequence inString) {
    return runAllPhases(inString.toString());
  }

  private String runAllPhases(String inString) {
    String outString = inString;
    // TODO: catch exceptions
    for (Phase phase : translitPhases) {
      outString = phase.runPhase(phase, outString);
    }
    return outString;
  }

}
