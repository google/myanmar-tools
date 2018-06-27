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

import java.lang.StringBuilder;

import java.util.regex.Pattern;

/**
 * Class implementing a rule as part of a transliteration phase.
 */

public class Rule {

  // Fields of the rule
  public Pattern pattern;
  public String substitution;
  public Boolean matchOnStart = false;
  public int revisitPosition = -1;
  public String info = "";  // Id number or other information.

  private void setPatternSubst(String patternString, String substitutionString) {
    // Set up to Match only at the beginning of a string.
    pattern = Pattern.compile("^" + patternString);
    substitution = substitutionString;
  }

  public Rule(String patternString, String substitutionString) {
    setPatternSubst(patternString, substitutionString);
  }

  public Rule(String patternString, String substitutionString, Boolean matchStart) {
    setPatternSubst(patternString, substitutionString);
    matchOnStart = matchStart;
  }

  public Rule(String patternString, String substitutionString, int revisitPos) {
    setPatternSubst(patternString, substitutionString);
    revisitPosition = revisitPos;
  }

  public Rule(String patternString, String substitutionString, Boolean matchStart, int revisitPos) {
    setPatternSubst(patternString, substitutionString);
    matchOnStart = matchStart;
    revisitPosition = revisitPos;
  }

  private void setPattern(String patternString, String substitutionString, String ruleInfo) {
    setPatternSubst(patternString, substitutionString);
    info = ruleInfo;
  }

  public void setInfo(String ruleInfo) {
    info = ruleInfo;
  }

 public String printRule() {
    String result = "    R " + info + " p: " + pattern + " s: " + substitution;
    if (matchOnStart) {
      result += " matchOnStart=True ";
    }
    if (revisitPosition >= 0) {
      result += " revisitPosition= " + revisitPosition;
    }
    return result + "\n";
  }

  // Apply the rule here rather than in Phase?

  // TODO: Other functions to set some of the fields?
}
