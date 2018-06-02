// Transliteration rules to normalize Zawgyi text for comparison
function getAllRulesZNorm() {
  // Rules for phase 0
  var rules0 = [
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
  // Rules for phase 1
  var rules1 = [
    {
      p: RegExp('^' + '\u102F+'),
      s: '\u102F',
    },
    {
      p: RegExp('^' + '\u103B+'),
      s: '\u103B',
    },
    {
      p: RegExp('^' + '\u1039+'),
      s: '\u1039',
    },
    {
      p: RegExp('^' + '[\u1037\u1094\u1095]+'),
      s: '\u1037',
    },
  ];
  return [rules0, rules1];
}
// END OF TRANSLITERATION RULES
