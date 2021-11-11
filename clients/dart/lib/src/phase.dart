import 'rule.dart';

/// Class implementing a transliteration phase as an array of rules.
class Phase {

   late final List<Rule> _phaseRules=[];
   late String _info='';


   void setInfo(String newInfo) {
      _info = newInfo;
   }


   void addRule(Rule newRule) {
      _phaseRules.add(newRule);
      // Put the rule number within the phase.
      newRule.setInfo('${_phaseRules.length}');
   }

   String printPhase() {
      var sb = StringBuffer();
      sb.write('  Phase $_info has ${_phaseRules.length} rules\n');
      for (final rule in _phaseRules) {
         sb.write(rule.printRule());
      }
      return sb.toString();
   }

   String runPhase(String inString) {
      // Run all the rules of this phase.
      var outString = StringBuffer();
      String midString = inString;
      bool startOfString = true;



      while (midString.isNotEmpty) {
         // Move through the string, matching / applying rules .
         bool foundRule = false;

         for (final rule in _phaseRules) {
            if (!rule.matchOnStart || startOfString) {
               final m = rule.pattern.firstMatch(midString);
               if (m != null) {
                  foundRule = true;

                  var substitution=rule.substitution;
                  for (var g =1; g<=m.groupCount;g++) {
                     substitution = substitution.replaceFirst('\$$g', m.group(g)!);
                  }
                  if (rule.revisitPosition < 0) {
                     // Reset the new position to the end of the substitution.
                     outString.write(substitution);
                     midString = midString.substring(m.end);

                  }else{
                     midString= '$substitution${midString.substring(m.end)}';
                  }
               }
            }
         }
         if (!foundRule) {
            // Move forward by 1.
            outString.write(midString[0]);
            midString = midString.substring(1);
         }
         startOfString = false;
      }

      return outString.toString();
   }
}