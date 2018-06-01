// Transliteration rules to normalize Zawgyi text for comparison
function ZNormTransliterate(inString) {
  var outString = inString;

  // Rules for phase 0
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
  outString = runPhase(rules0, outString);
  // Rules for phase 1
  var rules1 = [
    {
      p: RegExp('^' + '\u102F+', 'u'),
      s: '\u102F',
    },
    {
      p: RegExp('^' + '\u103B+', 'u'),
      s: '\u103B',
    },
    {
      p: RegExp('^' + '\u1039+', 'u'),
      s: '\u1039',
    },
    {
      p: RegExp('^' + '[\u1037\u1094\u1095]+', 'u'),
      s: '\u1037',
    },
  ];
  outString = runPhase(rules1, outString);
  return outString;
}
// END OF TRANSLITERATION RULES
