// TRANSLITERATION RULES
// Input path: genconvert/input/my_normalize_zawgyi_transliteration_rules.txt
function getAllRulesZNorm() {
  var rules0 = [
    {
      p: RegExp('^' + '\u1025\u102E', 'u'),
      s: '\u1026',
    },
    {
      p: RegExp('^' + '\u102F([\u102D\u1036])', 'u'),
      s: '$1\u102F',
    },
    {
      p: RegExp('^' + '\u1039([\u1037\u1094\u1095])', 'u'),
      s: '$1\u1039',
    },
    {
      p: RegExp('^' + '\u103C([\u102E\u1032])', 'u'),
      s: '$1\u103C',
    },
    {
      p: RegExp('^' + '\u1033\u102D', 'u'),
      s: '\u102D\u1033',
    },
    {
      p: RegExp('^' + '\u103D\u102D', 'u'),
      s: '\u102D\u103D',
    },
    {
      p: RegExp('^' + '\u1089', 'u'),
      s: '\u103D\u1034',
    },
    {
      p: RegExp('^' + '\u1064\u103A', 'u'),
      s: '\u103A\u1064',
    },
    {
      p: RegExp('^' + '\u1067', 'u'),
      s: '\u1066',
    },
    {
      p: RegExp('^' + '\u1072', 'u'),
      s: '\u1071',
    },
    {
      p: RegExp('^' + '\u1074', 'u'),
      s: '\u1073',
    },
    {
      p: RegExp('^' + '\u1093', 'u'),
      s: '\u107B',
    },
  ];
  var rules1 = [
    {
      p: RegExp('^' + '\u102D+', 'u'),
      s: '\u102D',
    },
    {
      p: RegExp('^' + '\u102E+', 'u'),
      s: '\u102E',
    },
    {
      p: RegExp('^' + '\u102F+', 'u'),
      s: '\u102F',
    },
    {
      p: RegExp('^' + '\u1030+', 'u'),
      s: '\u1030',
    },
    {
      p: RegExp('^' + '\u1032+', 'u'),
      s: '\u1032',
    },
    {
      p: RegExp('^' + '\u1033+', 'u'),
      s: '\u1033',
    },
    {
      p: RegExp('^' + '\u1034+', 'u'),
      s: '\u1034',
    },
    {
      p: RegExp('^' + '\u1036+', 'u'),
      s: '\u1036',
    },
    {
      p: RegExp('^' + '\u1037+', 'u'),
      s: '\u1037',
    },
    {
      p: RegExp('^' + '\u1039+', 'u'),
      s: '\u1039',
    },
    {
      p: RegExp('^' + '\u103A+', 'u'),
      s: '\u103A',
    },
    {
      p: RegExp('^' + '\u103B+', 'u'),
      s: '\u103B',
    },
    {
      p: RegExp('^' + '\u103C+', 'u'),
      s: '\u103C',
    },
    {
      p: RegExp('^' + '\u103D+', 'u'),
      s: '\u103D',
    },
    {
      p: RegExp('^' + '\u103E+', 'u'),
      s: '\u103D',
    },
  ];
  var rules2 = [
    {
      p: RegExp('^' + '[\u1037\u1094\u1095]+', 'u'),
      s: '\u1037',
    },
    {
      p: RegExp('^' + '\u1005\u103A', 'u'),
      s: '\u1008',
    },
    {
      p: RegExp('^' + '\u101D', 'u'),
      s: '\u1040',
    },
    {
      p: RegExp('^' + '\u102F\u1088', 'u'),
      s: '\u1088',
    },
    {
      p: RegExp('^' + '\u103B\u103A', 'u'),
      s: '\u103A\u103B',
    },
    {
      p: RegExp('^' + '\u103D\u102F', 'u'),
      s: '\u1088',
    },
    {
      p: RegExp('^' + '\u103D\u1088', 'u'),
      s: '\u1088',
    },
    {
      p: RegExp('^' + '\u1037\u1039\u1037', 'u'),
      s: '\u1037\u1039',
    },
    {
      p: RegExp('^' + '\u103B([\u1000-\u1021])\u103B$', 'u'),
      s: '\u103B$1',
    },
  ];
  var rules3 = [
    {
      p: RegExp('^' + '[\u103B\u107E-\u1084]+', 'u'),
      s: '\u103B',
    },
  ];
  var rules4 = [
    {
      p: RegExp('^' + '([\u103B\u107E-\u1084])([\u1000-\u1021])\u1036\u102F', 'u'),
      s: '$1$2\u1033\u1036',
    },
  ];
  var rules5 = [
    {
      p: RegExp('^' + '\u1033', 'u'),
      s: '\u102F',
    },
  ];
  var rules6 = [
    {
      p: RegExp('^' + '[    -‍⁠  　﻿]+([\u1000-\u109F])', 'u'),
      s: '$1',
      revisit: 0,
    },
  ];
  return [rules0, rules1, rules2, rules3, rules4, rules5, rules6];
}
// END OF TRANSLITERATION RULES
