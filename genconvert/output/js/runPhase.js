  function runPhase(rules, inString) {
    var m;
    var outString = '';
    var midString = inString;
    var startOfString = true;
    while (midString.length > 0) {
      var foundRule = false;
      for (var ruleId in rules) {
        var rule = rules[ruleId];
        var m;
        if (rule.matchOnStart == null || startOfString) {
          m = midString.match(rule.p);

          // Matching uses only unnamed groups
          if (m != null) {
            foundRule = true;
            // Just for debugging
            if (rule.after) {
              var stopHere = 0;
            }
            var rightPartSize = midString.length - m[0].length;
            midString = midString.replace(rule.p, rule.s);
            var newStart = midString.length - rightPartSize;

            if (rule.revisit == null) {
              // New location is reset unless "revisit" is set to beginning of replacement.
              outString += midString.substring(0, newStart);
              midString = midString.substring(newStart);
            }
          }
	}
      }
      if (! foundRule) {
        outString += midString[0];
        midString = midString.substring(1);
      }
      startOfString = false;
    }
    return outString;
  // End of phase
  }
