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

// local imports
import com.google.myanmartools.Phase;
import com.google.myanmartools.Rule;

import java.lang.StringBuilder;

import java.util.ArrayList;


/**
 * A Java engine for running the phases of transliteration, similar to the ICU4J
 * transliterator.
 */

public class Transliterate {

  private ArrayList<Phase> translitPhases;
  private String name;  // For identification

  private Boolean debugMode;  // Print details of execution.

  // Creates a new transliterator.
  public Transliterate() {
    translitPhases = new ArrayList<Phase>();
    name = "";
    debugMode = false;
  }

  public Transliterate(String id) {
    translitPhases = new ArrayList<Phase>();
    name = id;
  }

  public Phase addPhase() {
    Phase newPhase = new Phase();
    newPhase.setInfo(" " + translitPhases.size());
    translitPhases.add(newPhase);
    newPhase.setDebugMode(debugMode);
    return newPhase;
  }

  public void setDebugMode(Boolean newMode) {
    debugMode = newMode;
    for (Phase phase :translitPhases) {
      phase.setDebugMode(debugMode);
    }
  }

  public Boolean getDebugMode() {
    return debugMode;
  }

  public String printAll() {
    StringBuilder sb = new StringBuilder();
    sb.append("Transliterator name = " + name + "\n");
    sb.append("  Phase count: " + translitPhases.size() + "\n");
    for (Phase p : translitPhases) {
      sb.append(p.printPhase());
    }
    return sb.toString();
  }

  /**
   * Apply the transliteration to the input string,
   * returning the converted result.
   */
  public String convert(String inString) {
    return runAllPhases(inString);
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
