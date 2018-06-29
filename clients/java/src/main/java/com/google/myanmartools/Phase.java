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

/**
 * Class implementing a transliteration phase as an array of rules.
 */

import java.util.ArrayList;
import java.util.regex.Matcher;

class Phase {

  private final ArrayList<Rule> phaseRules;
  private String info;
  private boolean debugMode;

  public Phase() {
    // Initialize the rules for the phase.
    phaseRules = new ArrayList<>();
    info = "";
    debugMode = false;
  }

  public void setInfo(String newInfo) {
    info = newInfo;
  }

  public void setDebugMode(boolean newMode) {
    debugMode = newMode;
  }

  void addRule(Rule newRule) {
    phaseRules.add(newRule);
    // Put the rule number within the phase.
    newRule.setInfo("" + phaseRules.size());
  }

  public String printPhase() {
    StringBuilder sb = new StringBuilder();
    sb.append("  Phase ").append(info)
        .append(" has ").
        append(phaseRules.size()).
        append(" rules\n");

    for (Rule rule : phaseRules) {
      sb.append(rule.printRule());
    }
    return sb.toString();
  }

  public String runPhase(Phase phase, String inString) {
    // Run all the rules of this phase.
    String outString = "";
    String midString = inString;
    boolean startOfString = true;

    if (debugMode) {
      System.out.println("Phase " + info + ", input= " + inString + "  (" + inString.length() + ")");
    }

    while (midString.length() > 0) {
      // Move through the string, matching / applying rules .
      boolean foundRule = false;

      for (Rule rule : phaseRules) {
        // Check if the rule applies.
        if (!rule.matchOnStart || startOfString) {
          Matcher m = rule.pattern.matcher(midString);
          if (m.find()) {

            if (debugMode) {
              // Debug
              System.out.println("  Matched rule " + rule.info + " = " + rule.pattern + " --> " +
                  rule.substitution);
              System.out.println("    m.group(0):  " + m.group(0) + " (" + m.group(0).length() +")");
            }
            foundRule = true;
            int rightPartSize = midString.length() - m.group(0).length();

            midString = m.replaceFirst(rule.substitution);

            if (rule.revisitPosition < 0) {
              // Reset the new position to the end of the subsitution.
              int newStart = midString.length() - rightPartSize;
              outString = outString + midString.substring(0, newStart);
              midString = midString.substring(newStart);
              if (debugMode) {
                System.out.println("  outString:  " + outString);
              }
            }
          }
        }
      }
      // All rules applied at this position.
      if (!foundRule) {
        // Move forward by 1.
        outString = outString + midString.substring(0,1);
        midString = midString.substring(1);
      }
      startOfString = false;
    }
    if (debugMode && !outString.equals(inString)) {
      System.out.println("  Return changed result = " + outString + "  (" + outString.length() + ")");
    }
    return outString;
  }

}
