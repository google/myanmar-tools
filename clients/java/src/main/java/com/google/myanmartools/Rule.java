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
  public String contextBefore = "";
  public String contextAfter = "";

  public Rule(String patternString, String substitutionString) {
    pattern = Pattern.compile("^" + patternString);
    substitution = substitutionString;
  }

  public Rule setInfo(String ruleInfo) {
    info = ruleInfo;
    return this;
  }

  public Rule setMatchOnStart() {
    matchOnStart = true;
    return this;
  }

  public Rule setRevisitPosition(int newPos) {
    revisitPosition = newPos;
    return this;
  }

  public Rule setBeforeContext(String before) {
    contextBefore = before;
    return this;
  }

  public Rule setAfterContext(String after) {
    contextAfter = after;
    return this;
  }

  public String printRule() {
    String result = "    R " + info + " p: " + pattern + " s: " + substitution;
    if (matchOnStart) {
      result += " matchOnStart=True ";
    }
    if (revisitPosition >= 0) {
      result += " revisitPosition= " + revisitPosition;
    }

    if (matchOnStart) {
      result += " matchOnStart = true" ;
    }

    if (contextBefore != "") {
      result += " contextAfter = " + contextBefore ;
    }

    if (contextAfter != "") {
      result += " contextAfter = " + contextAfter ;
    }

    return result + "\n";
  }

}
