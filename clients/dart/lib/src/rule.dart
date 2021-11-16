/// Class implementing a rule as part of a transliteration phase.
class Rule {
  final String substitution;
  late bool matchOnStart ;
  final String patternString;
  int revisitPosition;
  late RegExp _pattern;
  String info = '';  // Id number or other information.
  String contextBefore = '';
  String contextAfter = '';
  RegExp get pattern=>_pattern;
  Rule(this.patternString,this.substitution, {this.matchOnStart=false,this.revisitPosition=-1}){
    _pattern=RegExp(patternString,unicode: true);
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
    String result = '    R $info p: $patternString s: $substitution';
    if (matchOnStart) {
      result += ' matchOnStart=True ';
    }
    if (revisitPosition >= 0) {
      result += ' revisitPosition= $revisitPosition!' ;
    }

    if (matchOnStart) {
      result += ' matchOnStart = true' ;
    }

    if (contextBefore.isNotEmpty) {
      result += ' contextAfter = $contextBefore';
    }

    if ( contextAfter.isNotEmpty) {
      result += ' contextAfter = $contextAfter' ;
    }

    return result + '\n';
  }

}

