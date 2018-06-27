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

public class TransliterateU2Z extends Transliterate {
  public TransliterateU2Z() {

  // Rules for phase 0
  Phase phase0 = addPhase();
      phase0.addRule(new Rule("[\u1004\u101B\u105A]\u103A\u1039([\u1000-\u1021])\u103B", "$1\u103A\u1064"));
      phase0.addRule(new Rule("[\u1004\u101B\u105A]\u103A\u1039([\u1000-\u1021])\u102D\u1036", "$1\u108E"));
      phase0.addRule(new Rule("[\u1004\u101B\u105A]\u103A\u1039([\u1000-\u1021])\u102D", "$1\u108B"));
      phase0.addRule(new Rule("[\u1004\u101B\u105A]\u103A\u1039([\u1000-\u1021])\u102E", "$1\u108C"));
      phase0.addRule(new Rule("[\u1004\u101B\u105A]\u103A\u1039([\u1000-\u1021])\u1036", "$1\u108D"));
      phase0.addRule(new Rule("[\u1004\u101B\u105A]\u103A\u1039([\u1000-\u1021])\u1031", "$1\u1031\u1064"));
      phase0.addRule(new Rule("[\u1004\u101B\u105A]\u103A\u1039([\u1000-\u1021])\u103B\u102D\u102F", "$1\u103A\u1033\u108B"));
      phase0.addRule(new Rule("[\u1004\u101B\u105A]\u103A\u1039([\u1000-\u1021])\u103B\u102D", "$1\u103A\u108B"));
      phase0.addRule(new Rule("[\u1004\u101B\u105A]\u103A\u1039([\u1000-\u1021])\u103B\u102E\u102F", "$1\u103A\u108C\u1033"));
      phase0.addRule(new Rule("[\u1004\u101B\u105A]\u103A\u1039([\u1000-\u1021])\u103B\u102E", "$1\u103A\u108C"));
      phase0.addRule(new Rule("[\u1004\u101B\u105A]\u103A\u1039([\u1000-\u1021])\u103B\u1036", "$1\u103A\u108D"));
      phase0.addRule(new Rule("[\u1004\u101B\u105A]\u103A\u1039([\u1000-\u1021])\u103C", "$1\u103B\u1064"));
      phase0.addRule(new Rule("[\u1004\u101B\u105A]\u103A\u1039\u102D", "\u108B"));
      phase0.addRule(new Rule("[\u1004\u101B\u105A]\u103A\u1039\u102E", "\u108C"));
      phase0.addRule(new Rule("[\u1004\u101B\u105A]\u103A\u1039\u1036", "\u108D"));
      phase0.addRule(new Rule("[\u1004\u101B\u105A]\u103A\u1039([\u1000-\u1021])", "$1\u1064"));
      phase0.addRule(new Rule("\u1025([\u102B-\u1030\u1032])\u1038", "\u106A$1\u1038"));
      phase0.addRule(new Rule("\u1025\u102F\u1036", "\u1025\u1036\u1033"));
      phase0.addRule(new Rule("\u102D\u1036", "\u108E"));
      phase0.addRule(new Rule("\u103D\u103E", "\u108A"));
      phase0.addRule(new Rule("\u103E\u102F", "\u1088"));
      phase0.addRule(new Rule("\u103E\u1030", "\u1089"));
      phase0.addRule(new Rule("\u103A", "\u1039"));
      phase0.addRule(new Rule("\u103B", "\u103A"));
      phase0.addRule(new Rule("\u103C", "\u103B"));
      phase0.addRule(new Rule("\u103D", "\u103C"));
      phase0.addRule(new Rule("\u103E", "\u103D"));
      phase0.addRule(new Rule("\u103F", "\u1086"));
      phase0.addRule(new Rule("([\u1019])\u103E\u1030", "$1\u103D\u1034"));
      phase0.addRule(new Rule("\u102B\u103A", "\u105A"));
      phase0.addRule(new Rule("\u1039\u1010\u103D", "\u1096"));
      phase0.addRule(new Rule("\u1039\u1000", "\u1060"));
      phase0.addRule(new Rule("\u1039\u1001", "\u1061"));
      phase0.addRule(new Rule("\u1039\u1002", "\u1062"));
      phase0.addRule(new Rule("\u1039\u1003", "\u1063"));
      phase0.addRule(new Rule("\u1039\u1005", "\u1065"));
      phase0.addRule(new Rule("\u1039\u1006", "\u1067"));
      phase0.addRule(new Rule("\u1039\u1007", "\u1068"));
      phase0.addRule(new Rule("\u1039\u1008", "\u1069"));
      phase0.addRule(new Rule("\u1039\u100B", "\u106C"));
      phase0.addRule(new Rule("\u1039\u100C", "\u106D"));
      phase0.addRule(new Rule("\u1039\u100D", "\u106E"));
      phase0.addRule(new Rule("\u100D\u1039\u100E", "\u106F"));
      phase0.addRule(new Rule("\u1039\u100E", "\u106F"));
      phase0.addRule(new Rule("\u1039\u100F", "\u1070"));
      phase0.addRule(new Rule("\u1039\u1010", "\u1072"));
      phase0.addRule(new Rule("\u1039\u1011", "\u1074"));
      phase0.addRule(new Rule("\u1039\u1012", "\u1075"));
      phase0.addRule(new Rule("\u1039\u1013", "\u1076"));
      phase0.addRule(new Rule("\u1039\u1014", "\u1077"));
      phase0.addRule(new Rule("\u1039\u1015", "\u1078"));
      phase0.addRule(new Rule("\u1039\u1016", "\u1079"));
      phase0.addRule(new Rule("\u1039\u1017", "\u107A"));
      phase0.addRule(new Rule("\u1039\u1018", "\u1093"));
      phase0.addRule(new Rule("\u1039\u1019", "\u107C"));
      phase0.addRule(new Rule("\u1039\u101C", "\u1085"));
      phase0.addRule(new Rule("\u100F\u1039\u100D", "\u1091"));
      phase0.addRule(new Rule("\u100B\u1039\u100C", "\u1092"));
      phase0.addRule(new Rule("\u100B\u1039\u100B", "\u1097"));
      phase0.addRule(new Rule("\u104E\u1004\u103A\u1038", "\u104E"));
  // Rules for phase 1
  Phase phase1 = addPhase();
      phase1.addRule(new Rule("([\u1000-\u1021])\u103B\u1031", "\u1031\u103B$1"));
      phase1.addRule(new Rule("([\u1000-\u1021])\u103B", "\u103B$1"));
      phase1.addRule(new Rule("([\u1000-\u1021])\u103D\u1031\u1037", "\u1031$1\u1094\u103D"));
      phase1.addRule(new Rule("([\u1000-\u1021])\u1064\u103B", "\u103B$1\u1064"));
      phase1.addRule(new Rule("([\u1000-\u1021])([\u103A\u103C\u103D])\u1031", "\u1031$1$2"));
      phase1.addRule(new Rule("([\u1000-\u102A])\u1031", "\u1031$1"));
      phase1.addRule(new Rule("\u1014([\u1060-\u1068\u106C\u106D\u1070-\u107C\u1085\u1093\u1096])", "\u108F$1"));
      phase1.addRule(new Rule("\u1014([\u102F\u1030\u1037\u103A\u103C\u103D\u1087-\u108A])([\u102D\u102E\u1032\u1036\u1039\u103D\u103E\u1064])\u1037", "\u108F$1$2\u1094"));
      phase1.addRule(new Rule("\u1014([\u102D\u102E\u1032\u1036\u1039\u103D\u103E\u1064])([\u102F\u1030\u1037\u103A\u103C\u103D\u1087-\u108A])\u1037", "\u108F$1$2\u1094"));
      phase1.addRule(new Rule("\u1014([\u102D\u102E\u1032\u1036\u1039\u103D\u103E\u1064])\u1037", "\u1014$1\u1094"));
      phase1.addRule(new Rule("\u1014\u1032\u1037", "\u1014\u1032\u1094"));
      phase1.addRule(new Rule("\u1014\u1037", "\u1014\u1094"));
      phase1.addRule(new Rule("\u1014\u1032([\u102F\u1030\u1037\u103A\u103C\u103D\u1087-\u108A])\u1037", "\u108F$1\u1032\u1094"));
      phase1.addRule(new Rule("\u1014([\u102D\u102E\u1032\u1036\u1039\u103D\u103E\u1064])([\u102F\u1030\u1037\u103A\u103C\u103D\u1087-\u108A])", "\u108F$1$2"));
      phase1.addRule(new Rule("\u1014([\u102F\u1030\u1037\u103A\u103C\u103D\u1087-\u108A])([\u102D\u102E\u1032\u1036\u1039\u103D\u103E\u1064])", "\u108F$1$2"));
      phase1.addRule(new Rule("\u1014([\u102F\u1030\u1037\u103A\u103C\u103D\u1087-\u108A])\u1037", "\u108F$1\u1094"));
      phase1.addRule(new Rule("\u1014([\u102F\u1030\u1037\u103A\u103C\u103D\u1087-\u108A])", "\u108F$1"));
      phase1.addRule(new Rule("([\u102F\u1030\u1037\u103A\u103C\u103D\u1087-\u108A])([\u102D\u102E\u1032\u1036\u1039\u103D\u103E\u1064]*)\u1037", "$1$2\u1094"));
      phase1.addRule(new Rule("([^\u1040-\u1049])\u1040([\u102B-\u103F])", "$1\u101D$2"));
      phase1.addRule(new Rule("\u1031\u1040([^\u1040-\u1049])", "\u1031\u101D$1"));
      phase1.addRule(new Rule("\u1025\u102E", "\u1026"));
      phase1.addRule(new Rule("\u1037\u103A", "\u103A\u1037"));
      phase1.addRule(new Rule("([\u102B\u102C\u102F\u1030])([\u102D\u102E\u1032])", "$2$1"));
      phase1.addRule(new Rule("([\u103B\u107E-\u1084])([\u1000-\u1021])\u102F", "$1$2\u1033"));
  // Rules for phase 2
  Phase phase2 = addPhase();
      phase2.addRule(new Rule("\u103C\u1094", "\u103C\u1095"));
      phase2.addRule(new Rule("[\u103B\u107E-\u1084]([\u1001\u1002\u1004\u1005\u1007\u100B-\u100E\u1012\u1013\u1015-\u1017\u1019\u101D\u1020\u1025\u1026])\u102D\u103D\u102F", "\u107F$1\u102D\u1087\u1083"));
      phase2.addRule(new Rule("[\u103B\u107E-\u1084]([\u1000\u1003\u1006\u1009\u100A\u100F-\u1011\u1018\u101C\u101E\u101F\u1021])\u102D\u103D\u102F", "\u1080$1\u102D\u1087\u1083"));
      phase2.addRule(new Rule("[\u103B\u107E-\u1084]([\u1001\u1002\u1004\u1005\u1007\u100B-\u100E\u1012\u1013\u1015-\u1017\u1019\u101D\u1020\u1025\u1026])([\u102F\u1030\u1037\u103A\u103C\u103D\u1087-\u108A])([\u102D\u102E\u1032\u1036\u1039\u103D\u103E\u1064])", "\u1083$1$2$3"));
      phase2.addRule(new Rule("[\u103B\u107E-\u1084]([\u1000\u1003\u1006\u1009\u100A\u100F-\u1011\u1018\u101C\u101E\u101F\u1021])([\u102F\u1030\u1037\u103A\u103C\u103D\u1087-\u108A])([\u102D\u102E\u1032\u1036\u1039\u103D\u103E\u1064])", "\u1084$1$2$3"));
      phase2.addRule(new Rule("[\u103B\u107E-\u1084]([\u1001\u1002\u1004\u1005\u1007\u100B-\u100E\u1012\u1013\u1015-\u1017\u1019\u101D\u1020\u1025\u1026])([\u102D\u102E\u1032\u1036\u1039\u103D\u103E\u1064])", "\u107F$1$2"));
      phase2.addRule(new Rule("[\u103B\u107E-\u1084]([\u1000\u1003\u1006\u1009\u100A\u100F-\u1011\u1018\u101C\u101E\u101F\u1021])([\u102D\u102E\u1032\u1036\u1039\u103D\u103E\u1064])", "\u1080$1$2"));
      phase2.addRule(new Rule("[\u103B\u107E-\u1084]([\u1001\u1002\u1004\u1005\u1007\u100B-\u100E\u1012\u1013\u1015-\u1017\u1019\u101D\u1020\u1025\u1026])\u1030", "\u103B$1\u1034"));
      phase2.addRule(new Rule("[\u103B\u107E-\u1084]([\u1000\u1003\u1006\u1009\u100A\u100F-\u1011\u1018\u101C\u101E\u101F\u1021])\u1030", "\u107E$1\u1034"));
      phase2.addRule(new Rule("[\u103B\u107E-\u1084]([\u1001\u1002\u1004\u1005\u1007\u100B-\u100E\u1012\u1013\u1015-\u1017\u1019\u101D\u1020\u1025\u1026])(\u102F)", "\u103B$1\u1033"));
      phase2.addRule(new Rule("[\u103B\u107E-\u1084]([\u1000\u1003\u1006\u1009\u100A\u100F-\u1011\u1018\u101C\u101E\u101F\u1021])(\u102F)", "\u107E$1\u1033"));
      phase2.addRule(new Rule("[\u103B\u107E-\u1084]([\u1001\u1002\u1004\u1005\u1007\u100B-\u100E\u1012\u1013\u1015-\u1017\u1019\u101D\u1020\u1025\u1026])([\u102F\u1030\u1037\u103A\u103C\u103D\u1087-\u108A])", "\u1081$1$2"));
      phase2.addRule(new Rule("[\u103B\u107E-\u1084]([\u1000\u1003\u1006\u1009\u100A\u100F-\u1011\u1018\u101C\u101E\u101F\u1021])([\u102F\u1030\u1037\u103A\u103C\u103D\u1087-\u108A])", "\u1082$1$2"));
      phase2.addRule(new Rule("[\u103B\u107E-\u1084]([\u100A\u106B])", "\u1082$1"));
      phase2.addRule(new Rule("[\u103B\u107E-\u1084]([\u1009\u106A])", "\u103B\u106A"));
      phase2.addRule(new Rule("[\u103B\u107E-\u1084]([\u1001\u1002\u1004\u1005\u1007\u100B-\u100E\u1012\u1013\u1015-\u1017\u1019\u101D\u1020\u1025\u1026])", "\u103B$1"));
      phase2.addRule(new Rule("[\u103B\u107E-\u1084]([\u1000\u1003\u1006\u1009\u100A\u100F-\u1011\u1018\u101C\u101E\u101F\u1021])", "\u107E$1"));
      phase2.addRule(new Rule("\u1009([\u102F\u1030\u1037\u103A\u103C\u103D\u1087-\u108A])", "\u106A$1"));
      phase2.addRule(new Rule("\u100A([\u102F\u1030\u1037\u103A\u103C\u103D\u1087-\u108A])", "\u106B$1"));
      phase2.addRule(new Rule("\u103D\u102D", "\u102D\u103D"));
      phase2.addRule(new Rule("\u103A([\u102D\u102E\u1032\u1036\u1039\u103D\u103E\u1064])\u102F[\u1037\u1094\u1095]", "\u103A$1\u1033\u1095"));
      phase2.addRule(new Rule("\u103A\u102F[\u1037\u1094\u1095]", "\u103A\u1033\u1095"));
      phase2.addRule(new Rule("\u103A\u102F", "\u103A\u1033"));
      phase2.addRule(new Rule("\u1064\u102E", "\u108C"));
  // Rules for phase 3
  Phase phase3 = addPhase();
      phase3.addRule(new Rule("([\u103C-\u103E]+)\u103B", "\u103B$1"));
      phase3.addRule(new Rule("([\u103D\u103E]+)\u103C", "\u103C$1"));
      phase3.addRule(new Rule("\u103E\u103D", "\u103D\u103E"));
      phase3.addRule(new Rule("\u1037([\u102D-\u1030\u1032\u1036])", "$1\u1037"));
      phase3.addRule(new Rule("([\u1000-\u1021])([\u102B-\u1032\u1036\u103B-\u103E])\u103A([\u1000-\u1021])", "$1\u103A$2$3"));
      phase3.addRule(new Rule("\u103D\u102F", "\u1088"));
      phase3.addRule(new Rule("\u1033\u1094", "\u1033\u1095"));
      phase3.addRule(new Rule("([\u103B\u107E-\u1084])([\u1000-\u1021])([\u102D\u102E\u1032\u1036\u1039\u103D\u103E\u1064])\u102F", "$1$2$3\u1033"));
  // Rules for phase 4
  Phase phase4 = addPhase();
      phase4.addRule(new Rule("([\u103D\u103E])\u103C", "\u103C$1"));
      phase4.addRule(new Rule("\u103E\u103D", "\u103D\u103E"));
      phase4.addRule(new Rule("\u1038([\u102B-\u1030\u1032\u103C-\u103F])", "$1\u1038"));
      phase4.addRule(new Rule("\u1038([\u1036\u1037\u103A])", "$1\u1038"));
      phase4.addRule(new Rule("\u1036\u102F", "\u102F\u1036"));
      phase4.addRule(new Rule("\u103A([\u1064\u108B-\u108E])\u102D\u102F", "\u103A$1\u102D\u1033"));
      phase4.addRule(new Rule("\u103A\u102D\u102F", "\u103A\u102D\u1033"));
  // Rules for phase 5
  Phase phase5 = addPhase();
      phase5.addRule(new Rule("([\u1000-\u1021])\u103B\u103A", "$1\u103A\u103B"));
      phase5.addRule(new Rule("([\u103C-\u103E])\u103B", "\u103B$1"));
      phase5.addRule(new Rule("([\u103D\u103E])\u103C", "\u103C$1"));
      phase5.addRule(new Rule("\u103E\u103D", "\u103D\u103E"));
      phase5.addRule(new Rule("([\u102D-\u1030\u1032])\u103A([\u1000-\u1021])\u103A", "$1$2\u103A"));
      phase5.addRule(new Rule("\u102D\u103A", "\u102D"));
      phase5.addRule(new Rule("\u102E\u103A", "\u102E"));
      phase5.addRule(new Rule("\u102F\u103A", "\u102F"));
      phase5.addRule(new Rule("\u102D\u102E", "\u102E"));
      phase5.addRule(new Rule("\u102F\u1030", "\u102F"));
      phase5.addRule(new Rule("\u102B\u102B+", "\u102B"));
      phase5.addRule(new Rule("\u102C\u102C+", "\u102C"));
      phase5.addRule(new Rule("\u102D\u102D+", "\u102D"));
      phase5.addRule(new Rule("\u102E\u102E+", "\u102E"));
      phase5.addRule(new Rule("\u102F\u102F+", "\u102F"));
      phase5.addRule(new Rule("\u1030\u1030+", "\u1030"));
      phase5.addRule(new Rule("\u1031\u1031+", "\u1031"));
      phase5.addRule(new Rule("\u1032\u1032+", "\u1032"));
      phase5.addRule(new Rule("\u1036\u1036+", "\u1036"));
      phase5.addRule(new Rule("\u103A\u103A+", "\u103A"));
      phase5.addRule(new Rule("\u103B\u103B+", "\u103B"));
      phase5.addRule(new Rule("\u103C\u103C+", "\u103C"));
      phase5.addRule(new Rule("\u103D\u103D+", "\u103D"));
      phase5.addRule(new Rule("\u103E\u103E+", "\u103E"));
      phase5.addRule(new Rule("\u102F\u102D", "\u102D\u102F"));
      phase5.addRule(new Rule("\u102F\u1036", "\u1036\u102F"));
      phase5.addRule(new Rule("\u1039\u1037", "\u1037\u1039"));
      phase5.addRule(new Rule("\u103C\u1032", "\u1032\u103C"));
      phase5.addRule(new Rule("\u103C\u102E", "\u102E\u103C"));
      phase5.addRule(new Rule("\u103D\u1088", "\u1088"));
  }
}
// END OF TRANSLITERATION RULES
