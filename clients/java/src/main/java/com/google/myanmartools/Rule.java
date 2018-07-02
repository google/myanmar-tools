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

class Rule {

  // Fields of the rule
  Pattern pattern;
  String substitution;
  boolean matchOnStart = false;
  int revisitPosition = -1;
  String info = "";  // Id number or other information.
  String contextBefore = "";
  String contextAfter = "";

  Rule(String patternString, String substitutionString) {
    pattern = Pattern.compile("^" + patternString);
    substitution = substitutionString;
  }

  Rule setInfo(String ruleInfo) {
    info = ruleInfo;
    return this;
  }

  Rule setMatchOnStart() {
    matchOnStart = true;
    return this;
  }

  Rule setRevisitPosition(int newPos) {
    revisitPosition = newPos;
    return this;
  }

  Rule setBeforeContext(String before) {
    contextBefore = before;
    return this;
  }

  Rule setAfterContext(String after) {
    contextAfter = after;
    return this;
  }

  String printRule() {
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

    if (!"".equals(contextBefore)) {
      result += " contextAfter = " + contextBefore ;
    }

    if (!"".equals(contextAfter)) {
      result += " contextAfter = " + contextAfter ;
    }

    return result + "\n";
  }

}
