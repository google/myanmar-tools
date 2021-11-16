namespace MyanmarTools.Transliterate
{

    /// <summary>
    /// Transliteration initialization of Phases and rules.
    /// Note: this is generated by compiling ICU transliteration rule format.
    /// Source rule file = genconvert/input/my-t-my-s0-zawgyi.txt
    /// </summary>
    public class TransliterateZ2U : Transliterate
    {
        public TransliterateZ2U(string name) : base(name)
        {
            // Rules for Phase 0
            Phase Phase0 = AddPhase();
            Phase0.AddRule(new Rule("([\u1000-\u1021])\u103A\u1064", "\u1004\u103A\u1039$1\u103B"));
            Phase0.AddRule(new Rule("([\u1000-\u1021])\u1064", "\u1004\u103A\u1039$1"));
            Phase0.AddRule(new Rule("\u1064", "\u1004\u103A\u1039"));
            Phase0.AddRule(new Rule("([\u1000-\u1021])\u108B", "\u1004\u103A\u1039$1\u102D"));
            Phase0.AddRule(new Rule("([\u1000-\u1021])\u108C", "\u1004\u103A\u1039$1\u102E"));
            Phase0.AddRule(new Rule("([\u1000-\u1021])\u108D", "\u1004\u103A\u1039$1\u1036"));
            Phase0.AddRule(new Rule("([\u1000-\u1021])\u103A\u1033\u108B", "\u1004\u103A\u1039$1\u103B\u102D\u102F"));
            Phase0.AddRule(new Rule("([\u1000-\u1021])\u103A\u108B", "\u1004\u103A\u1039$1\u103B\u102D"));
            Phase0.AddRule(new Rule("([\u1000-\u1021])\u103A\u108C", "\u1004\u103A\u1039$1\u103B\u102E"));
            Phase0.AddRule(new Rule("([\u1000-\u1021])\u103A\u108D", "\u1004\u103A\u1039$1\u103B\u1036"));
            Phase0.AddRule(new Rule("([\u1000-\u1021])\u103A\u108E", "$1\u103B\u102D\u1036"));
            Phase0.AddRule(new Rule("\u108B", "\u1004\u103A\u1039\u102D"));
            Phase0.AddRule(new Rule("\u108C", "\u1004\u103A\u1039\u102E"));
            Phase0.AddRule(new Rule("\u108D", "\u1004\u103A\u1039\u1036"));
            Phase0.AddRule(new Rule("\u106A", "\u1009"));
            Phase0.AddRule(new Rule("\u106B", "\u100A"));
            Phase0.AddRule(new Rule("\u108F", "\u1014"));
            Phase0.AddRule(new Rule("\u1090", "\u101B"));
            Phase0.AddRule(new Rule("\u1086", "\u103F"));
            Phase0.AddRule(new Rule("[\u103A\u107D]", "\u103B"));
            Phase0.AddRule(new Rule("([\u103B\u107E-\u1084])+", "\u103C"));
            Phase0.AddRule(new Rule("\u103C*\u108A", "\u103D\u103E"));
            Phase0.AddRule(new Rule("\u103C", "\u103D"));
            Phase0.AddRule(new Rule("[\u103D\u1087]", "\u103E"));
            Phase0.AddRule(new Rule("\u1088", "\u103E\u102F"));
            Phase0.AddRule(new Rule("\u1089", "\u103E\u1030"));
            Phase0.AddRule(new Rule("\u1033", "\u102F"));
            Phase0.AddRule(new Rule("\u1034", "\u1030"));
            Phase0.AddRule(new Rule("\u1039", "\u103A"));
            Phase0.AddRule(new Rule("[\u1094\u1095]", "\u1037"));
            Phase0.AddRule(new Rule("\u1025\u1039", "\u1009\u103A"));
            Phase0.AddRule(new Rule("\u1025\u1061", "\u1009\u1039\u1001"));
            Phase0.AddRule(new Rule("\u1025\u1062", "\u1009\u1039\u1002"));
            Phase0.AddRule(new Rule("\u1025\u1065", "\u1009\u1039\u1005"));
            Phase0.AddRule(new Rule("\u1025\u1068", "\u1009\u1039\u1007"));
            Phase0.AddRule(new Rule("\u1025\u1076", "\u1009\u1039\u1013"));
            Phase0.AddRule(new Rule("\u1025\u1078", "\u1009\u1039\u1015"));
            Phase0.AddRule(new Rule("\u1025\u107A", "\u1009\u1039\u1017"));
            Phase0.AddRule(new Rule("\u1025\u1079", "\u1009\u1039\u1016"));
            Phase0.AddRule(new Rule("\u105A", "\u102B\u103A"));
            Phase0.AddRule(new Rule("\u1060", "\u1039\u1000"));
            Phase0.AddRule(new Rule("\u1061", "\u1039\u1001"));
            Phase0.AddRule(new Rule("\u1062", "\u1039\u1002"));
            Phase0.AddRule(new Rule("\u1063", "\u1039\u1003"));
            Phase0.AddRule(new Rule("\u1065", "\u1039\u1005"));
            Phase0.AddRule(new Rule("[\u1066\u1067]", "\u1039\u1006"));
            Phase0.AddRule(new Rule("\u1068", "\u1039\u1007"));
            Phase0.AddRule(new Rule("\u1069", "\u1039\u1008"));
            Phase0.AddRule(new Rule("\u106C", "\u1039\u100B"));
            Phase0.AddRule(new Rule("\u106D", "\u1039\u100C"));
            Phase0.AddRule(new Rule("\u1070", "\u1039\u100F"));
            Phase0.AddRule(new Rule("[\u1071\u1072]", "\u1039\u1010"));
            Phase0.AddRule(new Rule("\u1096", "\u1039\u1010\u103D"));
            Phase0.AddRule(new Rule("[\u1073\u1074]", "\u1039\u1011"));
            Phase0.AddRule(new Rule("\u1075", "\u1039\u1012"));
            Phase0.AddRule(new Rule("\u1076", "\u1039\u1013"));
            Phase0.AddRule(new Rule("\u1077", "\u1039\u1014"));
            Phase0.AddRule(new Rule("\u1078", "\u1039\u1015"));
            Phase0.AddRule(new Rule("\u1079", "\u1039\u1016"));
            Phase0.AddRule(new Rule("\u107A", "\u1039\u1017"));
            Phase0.AddRule(new Rule("[\u107B\u1093]", "\u1039\u1018"));
            Phase0.AddRule(new Rule("\u107C", "\u1039\u1019"));
            Phase0.AddRule(new Rule("\u1085", "\u1039\u101C"));
            Phase0.AddRule(new Rule("\u108E", "\u102D\u1036"));
            Phase0.AddRule(new Rule("\u106E", "\u100D\u1039\u100D"));
            Phase0.AddRule(new Rule("\u106F", "\u100D\u1039\u100E"));
            Phase0.AddRule(new Rule("\u1091", "\u100F\u1039\u100D"));
            Phase0.AddRule(new Rule("\u1092", "\u100B\u1039\u100C"));
            Phase0.AddRule(new Rule("\u1097", "\u100B\u1039\u100B"));
            Phase0.AddRule(new Rule("\u104E", "\u104E\u1004\u103A\u1038"));

            // Rules for Phase 1
            Phase Phase1 = AddPhase();
            Phase1.AddRule(new Rule("\u1040([^\u1040-\u1049])", "\u101D$1")
                  .SetMatchOnStart());
            Phase1.AddRule(new Rule("\u1044([^\u1040-\u1049])", "\u104E$1")
                  .SetMatchOnStart()
                  .SetRevisitPosition(0));
            Phase1.AddRule(new Rule("([^\u1040-\u1049])\u1040$", "$1\u101D"));
            Phase1.AddRule(new Rule("([^\u1040-\u1049])\u1044$", "$1\u104E"));
            Phase1.AddRule(new Rule("([\u102B-\u103F])\u1040([^\u1040-\u1049])", "$1\u101D$2"));
            Phase1.AddRule(new Rule("([\u102B-\u103F])\u1044([^\u1040-\u1049])", "$1\u104E$2"));

            // Rules for Phase 2
            Phase Phase2 = AddPhase();
            Phase2.AddRule(new Rule("([ \u00A0\u1680\u2000-\u200D\u202F\u205F\u2060\u3000\uFEFF])\u1037", "\u1037$1"));
            Phase2.AddRule(new Rule("([ \u00A0\u1680\u2000-\u200D\u202F\u205F\u2060\u3000\uFEFF]+)([\u102B-\u1030\u1032-\u103B\u103D\u103E])", "$2"));
            Phase2.AddRule(new Rule("\u1037+", "\u1037"));
            Phase2.AddRule(new Rule("\u1031+\u1004\u103A\u1039([\u1000-\u1021])", "\u1004\u103A\u1039$1\u1031"));
            Phase2.AddRule(new Rule("\u1031+\u1037+([\u1000-\u1021])", "$1\u1031\u1037"));
            Phase2.AddRule(new Rule("\u1031+\u103C([\u1000-\u1021])", "$1\u103C\u1031"));
            Phase2.AddRule(new Rule("\u1031+([\u1000-\u1021])([\u103B\u103D\u103E]+)", "$1$2\u1031"));
            Phase2.AddRule(new Rule("\u1031+([\u1000-\u102A])", "$1\u1031"));

            // Rules for Phase 3
            Phase Phase3 = AddPhase();
            Phase3.AddRule(new Rule("\u103B\u103A", "\u103A\u103B"));
            Phase3.AddRule(new Rule("\u1025\u102E", "\u1026"));
            Phase3.AddRule(new Rule("\u103A\u1037", "\u1037\u103A"));
            Phase3.AddRule(new Rule("\u1036([\u103B-\u103E]*)([\u102B-\u1030\u1032]+)", "$1$2\u1036"));
            Phase3.AddRule(new Rule("([\u102B\u102C\u102F\u1030])([\u102D\u102E\u1032])", "$2$1"));
            Phase3.AddRule(new Rule("\u103C([\u1000-\u1021])", "$1\u103C"));

            // Rules for Phase 4
            Phase Phase4 = AddPhase();
            Phase4.AddRule(new Rule("([\u103B-\u103E])\u1039([\u1000-\u1021])", "\u1039$2$1"));
            Phase4.AddRule(new Rule("\u103C\u103A\u1039([\u1000-\u1021])", "\u103A\u1039$1\u103C"));
            Phase4.AddRule(new Rule("\u1036([\u103B-\u103E]+)", "$1\u1036"));

            // Rules for Phase 5
            Phase Phase5 = AddPhase();
            Phase5.AddRule(new Rule("([\u103C-\u103E]+)\u103B", "\u103B$1"));
            Phase5.AddRule(new Rule("([\u103D\u103E]+)\u103C", "\u103C$1"));
            Phase5.AddRule(new Rule("\u103E\u103D", "\u103D\u103E"));
            Phase5.AddRule(new Rule("([\u1031]+)([\u102B-\u1030\u1032]*)\u1039([\u1000-\u1021])", "\u1039$3$1$2"));
            Phase5.AddRule(new Rule("([\u102B-\u1030\u1032]+)\u1039([\u1000-\u1021])", "\u1039$2$1"));
            Phase5.AddRule(new Rule("([\u103B-\u103E]*)([\u1031]+)([\u103B-\u103E]*)", "$1$3$2"));
            Phase5.AddRule(new Rule("\u1037([\u102D-\u1030\u1032\u1036\u103B-\u103E]+)", "$1\u1037"));
            Phase5.AddRule(new Rule("([\u102B-\u1030\u1032]+)([\u103B-\u103E]+)", "$2$1"));
            Phase5.AddRule(new Rule("([\u1000-\u1021])([\u102B-\u1032\u1036\u103B-\u103E])\u103A([\u1000-\u1021])", "$1\u103A$2$3"));

            // Rules for Phase 6
            Phase Phase6 = AddPhase();
            Phase6.AddRule(new Rule("\u1005\u103B", "\u1008"));
            Phase6.AddRule(new Rule("([\u102B-\u1032])([\u103B-\u103E])", "$2$1"));
            Phase6.AddRule(new Rule("([\u103C-\u103E])\u103B", "\u103B$1"));
            Phase6.AddRule(new Rule("([\u103D\u103E])\u103C", "\u103C$1"));
            Phase6.AddRule(new Rule("\u103E\u103D", "\u103D\u103E"));
            Phase6.AddRule(new Rule("\u1038([\u000136u\u102B-\u1030\u1032\u1037\u103A-\u103F])", "$1\u1038"));
            Phase6.AddRule(new Rule("\u1036\u102F", "\u102F\u1036"));

            // Rules for Phase 7
            Phase Phase7 = AddPhase();
            Phase7.AddRule(new Rule("\u102D\u102D+", "\u102D"));
            Phase7.AddRule(new Rule("\u102E\u102E+", "\u102E"));
            Phase7.AddRule(new Rule("\u102F\u102F+", "\u102F"));
            Phase7.AddRule(new Rule("\u1030\u1030+", "\u1030"));
            Phase7.AddRule(new Rule("\u1032\u1032+", "\u1032"));
            Phase7.AddRule(new Rule("\u1036\u1036+", "\u1036"));
            Phase7.AddRule(new Rule("\u1037\u1037+", "\u1037"));
            Phase7.AddRule(new Rule("\u1039\u1039+", "\u1039"));
            Phase7.AddRule(new Rule("\u103A\u103A+", "\u103A"));
            Phase7.AddRule(new Rule("\u103B\u103B+", "\u103B"));
            Phase7.AddRule(new Rule("\u103C\u103C+", "\u103C"));
            Phase7.AddRule(new Rule("\u103D\u103D+", "\u103D"));
            Phase7.AddRule(new Rule("\u103E\u103E+", "\u103E"));
            Phase7.AddRule(new Rule("\u102F[\u1030\u103A]", "\u102F"));
            Phase7.AddRule(new Rule("\u102D\u102E", "\u102E"));
            Phase7.AddRule(new Rule("([ \u00A0\u1680\u2000-\u200D\u202F\u205F\u2060\u3000\uFEFF])+([\u102B-\u1032\u1036-\u103E])", "$2"));
            Phase7.AddRule(new Rule("\u200B+", "")
                  .SetMatchOnStart());
            Phase7.AddRule(new Rule("\u200B+$", ""));
            Phase7.AddRule(new Rule("[ \u00A0\u1680\u2000-\u200D\u202F\u205F\u2060\u3000\uFEFF]*\u200B[ \u00A0\u1680\u2000-\u200D\u202F\u205F\u2060\u3000\uFEFF]*", "\u200B"));
        }

    }
}