// Transliteration rules for converting Zawygi encoded Burmese to Unicode
function Z2Utransliterate(inString) {
  var outString = inString;

  // Rules for phase 0
  var rules0 = [
    {
      p: RegExp('^' + '([\u1000-\u1021])\u103A\u1064', 'u'),
      s: '\u1004\u103A\u1039$1\u103B',
    },
    {
      p: RegExp('^' + '([\u1000-\u1021])\u1064', 'u'),
      s: '\u1004\u103A\u1039$1',
    },
    {
      p: RegExp('^' + '\u1064', 'u'),
      s: '\u1004\u103A\u1039',
    },
    {
      p: RegExp('^' + '([\u1000-\u1021])\u108B', 'u'),
      s: '\u1004\u103A\u1039$1\u102D',
    },
    {
      p: RegExp('^' + '([\u1000-\u1021])\u108C', 'u'),
      s: '\u1004\u103A\u1039$1\u102E',
    },
    {
      p: RegExp('^' + '([\u1000-\u1021])\u108D', 'u'),
      s: '\u1004\u103A\u1039$1\u1036',
    },
    {
      p: RegExp('^' + '([\u1000-\u1021])\u103A\u1033\u108B', 'u'),
      s: '\u1004\u103A\u1039$1\u103B\u102D\u102F',
    },
    {
      p: RegExp('^' + '([\u1000-\u1021])\u103A\u108B', 'u'),
      s: '\u1004\u103A\u1039$1\u103B\u102D',
    },
    {
      p: RegExp('^' + '([\u1000-\u1021])\u103A\u108C\u1033', 'u'),
      s: '\u1004\u103A\u1039$1\u103B\u102E\u102F',
    },
    {
      p: RegExp('^' + '([\u1000-\u1021])\u103A\u108C', 'u'),
      s: '\u1004\u103A\u1039$1\u103B\u102E',
    },
    {
      p: RegExp('^' + '([\u1000-\u1021])\u103A\u108D', 'u'),
      s: '\u1004\u103A\u1039$1\u103B\u1036',
    },
    {
      p: RegExp('^' + '([\u1000-\u1021])\u103A\u108E', 'u'),
      s: '$1\u103B\u102D\u1036',
    },
    {
      p: RegExp('^' + '\u108B', 'u'),
      s: '\u1004\u103A\u1039\u102D',
    },
    {
      p: RegExp('^' + '\u108C', 'u'),
      s: '\u1004\u103A\u1039\u102E',
    },
    {
      p: RegExp('^' + '\u108D', 'u'),
      s: '\u1004\u103A\u1039\u1036',
    },
    {
      p: RegExp('^' + '\u106A([\u102B-\u1030\u1032])\u1038', 'u'),
      s: '\u1025$1\u1038',
    },
    {
      p: RegExp('^' + '\u106A', 'u'),
      s: '\u1009',
    },
    {
      p: RegExp('^' + '\u106B', 'u'),
      s: '\u100A',
    },
    {
      p: RegExp('^' + '\u108F', 'u'),
      s: '\u1014',
    },
    {
      p: RegExp('^' + '\u1090', 'u'),
      s: '\u101B',
    },
    {
      p: RegExp('^' + '\u1086', 'u'),
      s: '\u103F',
    },
    {
      p: RegExp('^' + '\u103A', 'u'),
      s: '\u103B',
    },
    {
      p: RegExp('^' + '\u107D', 'u'),
      s: '\u103B',
    },
    {
      p: RegExp('^' + '\u103C\u108A', 'u'),
      s: '\u103D\u103E',
    },
    {
      p: RegExp('^' + '\u103C', 'u'),
      s: '\u103D',
    },
    {
      p: RegExp('^' + '\u108A', 'u'),
      s: '\u103D\u103E',
    },
    {
      p: RegExp('^' + '\u103D', 'u'),
      s: '\u103E',
    },
    {
      p: RegExp('^' + '\u1087', 'u'),
      s: '\u103E',
    },
    {
      p: RegExp('^' + '\u1088', 'u'),
      s: '\u103E\u102F',
    },
    {
      p: RegExp('^' + '\u1089', 'u'),
      s: '\u103E\u1030',
    },
    {
      p: RegExp('^' + '\u1039', 'u'),
      s: '\u103A',
    },
    {
      p: RegExp('^' + '\u1033', 'u'),
      s: '\u102F',
    },
    {
      p: RegExp('^' + '\u1034', 'u'),
      s: '\u1030',
    },
    {
      p: RegExp('^' + '\u105A', 'u'),
      s: '\u102B\u103A',
    },
    {
      p: RegExp('^' + '\u108E', 'u'),
      s: '\u102D\u1036',
    },
    {
      p: RegExp('^' + '\u1031\u1094([\u1000-\u1021])\u103D', 'u'),
      s: '$1\u103E\u1031\u1037',
    },
    {
      p: RegExp('^' + '\u1094', 'u'),
      s: '\u1037',
    },
    {
      p: RegExp('^' + '\u1095', 'u'),
      s: '\u1037',
    },
    {
      p: RegExp('^' + '\u1025\u1061', 'u'),
      s: '\u1009\u1039\u1001',
    },
    {
      p: RegExp('^' + '\u1025\u1062', 'u'),
      s: '\u1009\u1039\u1002',
    },
    {
      p: RegExp('^' + '\u1025\u1065', 'u'),
      s: '\u1009\u1039\u1005',
    },
    {
      p: RegExp('^' + '\u1025\u1068', 'u'),
      s: '\u1009\u1039\u1007',
    },
    {
      p: RegExp('^' + '\u1025\u1076', 'u'),
      s: '\u1009\u1039\u1013',
    },
    {
      p: RegExp('^' + '\u1025\u1078', 'u'),
      s: '\u1009\u1039\u1015',
    },
    {
      p: RegExp('^' + '\u1025\u107A', 'u'),
      s: '\u1009\u1039\u1017',
    },
    {
      p: RegExp('^' + '\u1025\u1079', 'u'),
      s: '\u1009\u1039\u1016',
    },
    {
      p: RegExp('^' + '([\u1000-\u1021])\u103A\u1039', 'u'),
      s: '$1\u103A\u103B',
    },
    {
      p: RegExp('^' + '\u1060', 'u'),
      s: '\u1039\u1000',
    },
    {
      p: RegExp('^' + '\u1061', 'u'),
      s: '\u1039\u1001',
    },
    {
      p: RegExp('^' + '\u1062', 'u'),
      s: '\u1039\u1002',
    },
    {
      p: RegExp('^' + '\u1063', 'u'),
      s: '\u1039\u1003',
    },
    {
      p: RegExp('^' + '\u1065', 'u'),
      s: '\u1039\u1005',
    },
    {
      p: RegExp('^' + '\u1066', 'u'),
      s: '\u1039\u1006',
    },
    {
      p: RegExp('^' + '\u1067', 'u'),
      s: '\u1039\u1006',
    },
    {
      p: RegExp('^' + '\u1068', 'u'),
      s: '\u1039\u1007',
    },
    {
      p: RegExp('^' + '\u1069', 'u'),
      s: '\u1039\u1008',
    },
    {
      p: RegExp('^' + '\u106C', 'u'),
      s: '\u1039\u100B',
    },
    {
      p: RegExp('^' + '\u106D', 'u'),
      s: '\u1039\u100C',
    },
    {
      p: RegExp('^' + '\u1070', 'u'),
      s: '\u1039\u100F',
    },
    {
      p: RegExp('^' + '\u1071', 'u'),
      s: '\u1039\u1010',
    },
    {
      p: RegExp('^' + '\u1072', 'u'),
      s: '\u1039\u1010',
    },
    {
      p: RegExp('^' + '\u1096', 'u'),
      s: '\u1039\u1010\u103D',
    },
    {
      p: RegExp('^' + '\u1073', 'u'),
      s: '\u1039\u1011',
    },
    {
      p: RegExp('^' + '\u1074', 'u'),
      s: '\u1039\u1011',
    },
    {
      p: RegExp('^' + '\u1075', 'u'),
      s: '\u1039\u1012',
    },
    {
      p: RegExp('^' + '\u1076', 'u'),
      s: '\u1039\u1013',
    },
    {
      p: RegExp('^' + '\u1077', 'u'),
      s: '\u1039\u1014',
    },
    {
      p: RegExp('^' + '\u1078', 'u'),
      s: '\u1039\u1015',
    },
    {
      p: RegExp('^' + '\u1079', 'u'),
      s: '\u1039\u1016',
    },
    {
      p: RegExp('^' + '\u107A', 'u'),
      s: '\u1039\u1017',
    },
    {
      p: RegExp('^' + '\u107B', 'u'),
      s: '\u1039\u1018',
    },
    {
      p: RegExp('^' + '\u1093', 'u'),
      s: '\u1039\u1018',
    },
    {
      p: RegExp('^' + '\u107C', 'u'),
      s: '\u1039\u1019',
    },
    {
      p: RegExp('^' + '\u1085', 'u'),
      s: '\u1039\u101C',
    },
    {
      p: RegExp('^' + '\u106E', 'u'),
      s: '\u100D\u1039\u100D',
    },
    {
      p: RegExp('^' + '\u106F', 'u'),
      s: '\u100D\u1039\u100E',
    },
    {
      p: RegExp('^' + '\u1091', 'u'),
      s: '\u100F\u1039\u100D',
    },
    {
      p: RegExp('^' + '\u1092', 'u'),
      s: '\u100B\u1039\u100C',
    },
    {
      p: RegExp('^' + '\u1097', 'u'),
      s: '\u100B\u1039\u100B',
    },
    {
      p: RegExp('^' + '\u104E', 'u'),
      s: '\u104E\u1004\u103A\u1038',
    },
    {
      p: RegExp('^' + '([\u103B\u107E-\u1084])+', 'u'),
      s: '\u103C',
    },
  ];
  outString = runPhase(rules0, outString);
  // Rules for phase 1
  var rules1 = [
    {
      p: RegExp('^' + '([\ \u00A0\u2002\u2006\u2008\u200B-\u200D\u2060\uFEFF])+([\u102B-\u1030\u1032-\u103B\u103D\u103E])', 'u'),
      s: '$2',
    },
    {
      p: RegExp('^' + '\u1037\u1037+', 'u'),
      s: '\u1037',
    },
    {
      p: RegExp('^' + '\u1031\u1040([^\u1040-\u1049])', 'u'),
      s: '\u1031\u101D$1',
    },
    {
      p: RegExp('^' + '([\u1031]+)\u1004\u103A\u1039([\u1000-\u1021])', 'u'),
      s: '\u1004\u103A\u1039$2\u1031',
    },
    {
      p: RegExp('^' + '\u1031\u103C([\u1000-\u1021])', 'u'),
      s: '$1\u103C\u1031',
    },
    {
      p: RegExp('^' + '\u1031([\u1000-\u1021])([\u103B-\u103E]+)', 'u'),
      s: '$1$2\u1031',
    },
    {
      p: RegExp('^' + '\u1031([\u1000-\u102A])', 'u'),
      s: '$1\u1031',
    },
    {
      p: RegExp('^' + '\u1031(\u1037+)([\u1000-\u1021])', 'u'),
      s: '$2\u1031\u1037',
    },
    {
      p: RegExp('^' + '\u1031([\u1000-\u1021])\u1004\u103A\u1039', 'u'),
      s: '\u1004\u103A\u1039\u1031',
    },
  ];
  outString = runPhase(rules1, outString);
  // Rules for phase 2
  var rules2 = [
    {
      p: RegExp('^' + '\u1040([^\u1040-\u1049])', 'u'),
      s: '\u101D$1',
      matchOnStart: 'true',
    },
    {
      p: RegExp('^' + '([\u102B-\u103F])\u1040([^\u1040-\u1049])', 'u'),
      s: '$1\u101D$2',
    },
    {
      p: RegExp('^' + '\u1044([^\u1040-\u1049])', 'u'),
      s: '\u104E$1',
      matchOnStart: 'true',
      revisit: 0,
    },
    {
      p: RegExp('^' + '([\u102B-\u103F])\u1044([^\u1040-\u1049])', 'u'),
      s: '$1\u104E$2',
    },
    {
      p: RegExp('^' + '\u1025\u103A', 'u'),
      s: '\u1009\u103A',
    },
    {
      p: RegExp('^' + '\u1025\u102E', 'u'),
      s: '\u1026',
    },
    {
      p: RegExp('^' + '\u103A\u1037', 'u'),
      s: '\u1037\u103A',
    },
    {
      p: RegExp('^' + '\u1036([\u103B-\u103E]*)([\u102B-\u1030\u1032]+)', 'u'),
      s: '$1$2\u1036',
    },
    {
      p: RegExp('^' + '([\u102B\u102C\u102F\u1030])([\u102D\u102E\u1032])', 'u'),
      s: '$2$1',
    },
    {
      p: RegExp('^' + '\u103C([\u1000-\u1021])', 'u'),
      s: '$1\u103C',
    },
    {
      p: RegExp('^' + '\u1005\u103B', 'u'),
      s: '\u1008',
    },
  ];
  outString = runPhase(rules2, outString);
  // Rules for phase 3
  var rules3 = [
    {
      p: RegExp('^' + '([\u103B-\u103E])\u1039([\u1000-\u1021])', 'u'),
      s: '\u1039$2$1',
    },
    {
      p: RegExp('^' + '\u103C\u103A\u1039([\u1000-\u1021])', 'u'),
      s: '\u103A\u1039$1\u103C',
    },
    {
      p: RegExp('^' + '\u1036([\u103B-\u103E]+)', 'u'),
      s: '$1\u1036',
    },
  ];
  outString = runPhase(rules3, outString);
  // Rules for phase 4
  var rules4 = [
    {
      p: RegExp('^' + '([\u103C-\u103E]+)\u103B', 'u'),
      s: '\u103B$1',
    },
    {
      p: RegExp('^' + '([\u103D\u103E]+)\u103C', 'u'),
      s: '\u103C$1',
    },
    {
      p: RegExp('^' + '\u103E\u103D', 'u'),
      s: '\u103D\u103E',
    },
    {
      p: RegExp('^' + '([\u1031]+)([\u102B-\u1030\u1032]*)\u1039([\u1000-\u1021])', 'u'),
      s: '\u1039$3$1$2',
    },
    {
      p: RegExp('^' + '([\u102B-\u1030\u1032]+)\u1039([\u1000-\u1021])', 'u'),
      s: '\u1039$2$1',
    },
    {
      p: RegExp('^' + '([\u103B-\u103E]*)([\u1031]+)([\u103B-\u103E]*)', 'u'),
      s: '$1$3$2',
    },
    {
      p: RegExp('^' + '\u1037([\u102D-\u1030\u1032\u1036])', 'u'),
      s: '$1\u1037',
    },
    {
      p: RegExp('^' + '\u1037([\u103B-\u103E]+)', 'u'),
      s: '$1\u1037',
    },
    {
      p: RegExp('^' + '([\u102B-\u1030\u1032]+)([\u103B-\u103E]+)', 'u'),
      s: '$2$1',
    },
    {
      p: RegExp('^' + '([\u1000-\u1021])([\u102B-\u1032\u1036\u103B-\u103E])\u103A([\u1000-\u1021])', 'u'),
      s: '$1\u103A$2$3',
    },
  ];
  outString = runPhase(rules4, outString);
  // Rules for phase 5
  var rules5 = [
    {
      p: RegExp('^' + '([\u1031]+)([\u103B-\u103E]+)', 'u'),
      s: '$2$1',
    },
    {
      p: RegExp('^' + '([\u102B-\u1030\u1032])([\u103B-\u103E])', 'u'),
      s: '$2$1',
    },
    {
      p: RegExp('^' + '([\u103C-\u103E])\u103B', 'u'),
      s: '\u103B$1',
    },
    {
      p: RegExp('^' + '([\u103D\u103E])\u103C', 'u'),
      s: '\u103C$1',
    },
    {
      p: RegExp('^' + '\u103E\u103D', 'u'),
      s: '\u103D\u103E',
    },
    {
      p: RegExp('^' + '\u1038([\u102B-\u1030\u1032\u103B-\u103F])', 'u'),
      s: '$1\u1038',
    },
    {
      p: RegExp('^' + '\u1038([\u1036\u1037\u103A])', 'u'),
      s: '$1\u1038',
    },
    {
      p: RegExp('^' + '\u1036\u102F', 'u'),
      s: '\u102F\u1036',
    },
  ];
  outString = runPhase(rules5, outString);
  // Rules for phase 6
  var rules6 = [
    {
      p: RegExp('^' + '\u102D\u102D+', 'u'),
      s: '\u102D',
    },
    {
      p: RegExp('^' + '\u102E\u102E+', 'u'),
      s: '\u102E',
    },
    {
      p: RegExp('^' + '\u102F\u102F+', 'u'),
      s: '\u102F',
    },
    {
      p: RegExp('^' + '\u1030\u1030+', 'u'),
      s: '\u1030',
    },
    {
      p: RegExp('^' + '\u1032\u1032+', 'u'),
      s: '\u1032',
    },
    {
      p: RegExp('^' + '\u1033\u1033+', 'u'),
      s: '\u1033',
    },
    {
      p: RegExp('^' + '\u1035\u1035+', 'u'),
      s: '\u1035',
    },
    {
      p: RegExp('^' + '\u1036\u1036+', 'u'),
      s: '\u1036',
    },
    {
      p: RegExp('^' + '\u1037\u1037+', 'u'),
      s: '\u1037',
    },
    {
      p: RegExp('^' + '\u1039\u1039+', 'u'),
      s: '\u1039',
    },
    {
      p: RegExp('^' + '\u103A\u103A+', 'u'),
      s: '\u103A',
    },
    {
      p: RegExp('^' + '\u103B\u103B+', 'u'),
      s: '\u103B',
    },
    {
      p: RegExp('^' + '\u103C\u103C+', 'u'),
      s: '\u103C',
    },
    {
      p: RegExp('^' + '\u103D\u103D+', 'u'),
      s: '\u103D',
    },
    {
      p: RegExp('^' + '\u103E\u103E+', 'u'),
      s: '\u103E',
    },
    {
      p: RegExp('^' + '\u102F\u1030', 'u'),
      s: '\u102F',
    },
    {
      p: RegExp('^' + '\u102F\u103A', 'u'),
      s: '\u102F',
    },
    {
      p: RegExp('^' + '\u102D\u102E', 'u'),
      s: '\u102E',
    },
    {
      p: RegExp('^' + '[    -   　]([\u102B-\u1032\u1036-\u103E])', 'u'),
      s: '$1',
    },
    {
      p: RegExp('^' + '([\u1000-\u1021])\u103B\u103A', 'u'),
      s: '$1\u103A\u103B',
    },
  ];
  outString = runPhase(rules6, outString);
  return outString;
}
// END OF TRANSLITERATION RULES
