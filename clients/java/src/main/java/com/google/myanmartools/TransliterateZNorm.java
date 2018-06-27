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
 * Transliteration initialization of phases and rules.
 */

// local imports
import com.google.myanmartools.Transliterate;
import com.google.myanmartools.Phase;
import com.google.myanmartools.Rule;

public class TransliterateZNorm extends Transliterate {
  public TransliterateZNorm() {

  // Rules for phase 0
  Phase phase0 = addPhase();
      phase0.addRule(new Rule("\u1025\u102E", "\u1026"));
      phase0.addRule(new Rule("\u102F([\u102D\u1036])", "$1\u102F"));
      phase0.addRule(new Rule("\u1039([\u1037\u1094\u1095])", "$1\u1039"));
      phase0.addRule(new Rule("\u103C([\u102E\u1032])", "$1\u103C"));
      phase0.addRule(new Rule("\u1033\u102D", "\u102D\u1033"));
      phase0.addRule(new Rule("\u103D\u102D", "\u102D\u103D"));
      phase0.addRule(new Rule("\u1089", "\u103D\u1034"));
      phase0.addRule(new Rule("\u1064\u103A", "\u103A\u1064"));
      phase0.addRule(new Rule("\u1067", "\u1066"));
      phase0.addRule(new Rule("\u1072", "\u1071"));
      phase0.addRule(new Rule("\u1074", "\u1073"));
      phase0.addRule(new Rule("\u1093", "\u107B"));
  // Rules for phase 1
  Phase phase1 = addPhase();
      phase1.addRule(new Rule("\u102D+", "\u102D"));
      phase1.addRule(new Rule("\u102E+", "\u102E"));
      phase1.addRule(new Rule("\u102F+", "\u102F"));
      phase1.addRule(new Rule("\u1030+", "\u1030"));
      phase1.addRule(new Rule("\u1032+", "\u1032"));
      phase1.addRule(new Rule("\u1033+", "\u1033"));
      phase1.addRule(new Rule("\u1034+", "\u1034"));
      phase1.addRule(new Rule("\u1036+", "\u1036"));
      phase1.addRule(new Rule("\u1037+", "\u1037"));
      phase1.addRule(new Rule("\u1039+", "\u1039"));
      phase1.addRule(new Rule("\u103A+", "\u103A"));
      phase1.addRule(new Rule("\u103B+", "\u103B"));
      phase1.addRule(new Rule("\u103C+", "\u103C"));
      phase1.addRule(new Rule("\u103D+", "\u103D"));
      phase1.addRule(new Rule("\u103E+", "\u103D"));
  // Rules for phase 2
  Phase phase2 = addPhase();
      phase2.addRule(new Rule("[\u1037\u1094\u1095]+", "\u1037"));
      phase2.addRule(new Rule("\u1005\u103A", "\u1008"));
      phase2.addRule(new Rule("\u101D", "\u1040"));
      phase2.addRule(new Rule("\u102F\u1088", "\u1088"));
      phase2.addRule(new Rule("\u103B\u103A", "\u103A\u103B"));
      phase2.addRule(new Rule("\u103D\u102F", "\u1088"));
      phase2.addRule(new Rule("\u103D\u1088", "\u1088"));
      phase2.addRule(new Rule("\u103B([\u1000-\u1021])\u103B$", "\u103B$1"));
  // Rules for phase 3
  Phase phase3 = addPhase();
      phase3.addRule(new Rule("[\u103B\u107E-\u1084]+", "\u103B"));
  // Rules for phase 4
  Phase phase4 = addPhase();
      phase4.addRule(new Rule("([\u103B\u107E-\u1084])([\u1000-\u1021])\u1036\u102F", "$1$2\u1033\u1036"));
  // Rules for phase 5
  Phase phase5 = addPhase();
      phase5.addRule(new Rule("\u1033", "\u102F"));
  // Rules for phase 6
  Phase phase6 = addPhase();
      phase6.addRule(new Rule("\u1036\u102F", "\u102F\u1036"));
      phase6.addRule(new Rule("\u1037\u1039\u1037", "\u1037\u1039"));
      phase6.addRule(new Rule("[|\u106A\u106B]", "\u100A"));
  // Rules for phase 7
  Phase phase7 = addPhase();
      phase7.addRule(new Rule("[    -‍⁠  　﻿]+([\u1000-\u109F])", "$1", 0));  // REVISIT
  }
}
// END OF TRANSLITERATION RULES
