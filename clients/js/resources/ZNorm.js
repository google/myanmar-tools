// TRANSLITERATION RULES
// Input path: genconvert/input/my_normalize_zawgyi_transliteration_rules.txt
function getAllRulesZNorm() {
  var rules0 = [
    {
      p: RegExp('^' + '\u1009\u1039'),
      s: '\u1025\u1039',
    },
    {
      p: RegExp('^' + '\u1025\u102E'),
      s: '\u1026',
    },
    {
      p: RegExp('^' + '\u102F([\u102D\u1036])'),
      s: '$1\u102F',
    },
    {
      p: RegExp('^' + '\u1039([\u1037\u1094\u1095])'),
      s: '$1\u1039',
    },
    {
      p: RegExp('^' + '\u103C([\u102E\u1032])'),
      s: '$1\u103C',
    },
    {
      p: RegExp('^' + '\u1033\u102D'),
      s: '\u102D\u1033',
    },
    {
      p: RegExp('^' + '\u103D\u102D'),
      s: '\u102D\u103D',
    },
    {
      p: RegExp('^' + '\u1089'),
      s: '\u103D\u1034',
    },
    {
      p: RegExp('^' + '\u1064\u103A'),
      s: '\u103A\u1064',
    },
    {
      p: RegExp('^' + '\u1067'),
      s: '\u1066',
    },
    {
      p: RegExp('^' + '\u1072'),
      s: '\u1071',
    },
    {
      p: RegExp('^' + '\u1074'),
      s: '\u1073',
    },
    {
      p: RegExp('^' + '\u1093'),
      s: '\u107B',
    },
  ];
  var rules1 = [
    {
      p: RegExp('^' + '\u102D+'),
      s: '\u102D',
    },
    {
      p: RegExp('^' + '\u102E+'),
      s: '\u102E',
    },
    {
      p: RegExp('^' + '\u102F+'),
      s: '\u102F',
    },
    {
      p: RegExp('^' + '\u1030+'),
      s: '\u1030',
    },
    {
      p: RegExp('^' + '\u1032+'),
      s: '\u1032',
    },
    {
      p: RegExp('^' + '\u1033+'),
      s: '\u1033',
    },
    {
      p: RegExp('^' + '\u1034+'),
      s: '\u1034',
    },
    {
      p: RegExp('^' + '\u1036+'),
      s: '\u1036',
    },
    {
      p: RegExp('^' + '\u1037+'),
      s: '\u1037',
    },
    {
      p: RegExp('^' + '\u1039+'),
      s: '\u1039',
    },
    {
      p: RegExp('^' + '\u103A+'),
      s: '\u103A',
    },
    {
      p: RegExp('^' + '\u103B+'),
      s: '\u103B',
    },
    {
      p: RegExp('^' + '\u103C+'),
      s: '\u103C',
    },
    {
      p: RegExp('^' + '\u103D+'),
      s: '\u103D',
    },
    {
      p: RegExp('^' + '\u103E+'),
      s: '\u103D',
    },
  ];
  var rules2 = [
    {
      p: RegExp('^' + '[\u1037\u1094\u1095]+'),
      s: '\u1037',
    },
    {
      p: RegExp('^' + '\u1005\u103A'),
      s: '\u1008',
    },
    {
      p: RegExp('^' + '\u101D'),
      s: '\u1040',
    },
    {
      p: RegExp('^' + '\u104E$'),
      s: '\u1044',
    },
    {
      p: RegExp('^' + '\u102F\u1088'),
      s: '\u1088',
    },
    {
      p: RegExp('^' + '\u103B\u103A'),
      s: '\u103A\u103B',
    },
    {
      p: RegExp('^' + '\u103D\u102F'),
      s: '\u1088',
    },
    {
      p: RegExp('^' + '\u103D\u1088'),
      s: '\u1088',
    },
    {
      p: RegExp('^' + '\u103B([\u1000-\u1021])\u103B$'),
      s: '\u103B$1',
    },
  ];
  var rules3 = [
    {
      p: RegExp('^' + '[\u103B\u107E-\u1084]+'),
      s: '\u103B',
    },
    {
      p: RegExp('^' + '\u1031\u1031+'),
      s: '\u1031',
    },
  ];
  var rules4 = [
    {
      p: RegExp('^' + '([\u103B\u107E-\u1084])([\u1000-\u1021])\u1036\u102F'),
      s: '$1$2\u1033\u1036',
    },
  ];
  var rules5 = [
    {
      p: RegExp('^' + '\u1033'),
      s: '\u102F',
    },
  ];
  var rules6 = [
    {
      p: RegExp('^' + '\u1036\u102F'),
      s: '\u102F\u1036',
    },
    {
      p: RegExp('^' + '\u1037\u1039\u1037'),
      s: '\u1037\u1039',
    },
    {
      p: RegExp('^' + '\u106B'),
      s: '\u100A',
    },
  ];
  var rules7 = [
    {
      p: RegExp('^' + '[    -‍⁠  　﻿]+([\u1000-\u109F])'),
      s: '$1',
      revisit: 0,
    },
    {
      p: RegExp('^' + '\u200B+'),
      s: '',
      matchOnStart: 'true',
    },
    {
      p: RegExp('^' + '\u200B+$'),
      s: '',
    },
  ];
  return [rules0, rules1, rules2, rules3, rules4, rules5, rules6, rules7];
}
// END OF TRANSLITERATION RULES
