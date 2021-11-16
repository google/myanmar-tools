using System.Collections.Generic;
using System.Text;
namespace MyanmarTools
{
    public class Phase
    {
        private List<Rule> PhaseRules = new List<Rule>();
        public string Info;

        public void AddRule(Rule NewRule)
        {
            PhaseRules.Add(NewRule);
            // Put the rule number within the phase.
            NewRule.SetInfo(PhaseRules.Count.ToString());
        }

        public string RunPhase(string InString)
        {
            // Run all the rules of this phase.
            var OutString = new StringBuilder();
            var MidString = InString;
            var StartOfString = true;



            while (!string.IsNullOrEmpty(MidString))
            {
                // Move through the string, matching / applying rules .
                bool FoundRule = false;

                foreach (var Rule in PhaseRules)
                {
                    if (!Rule.MatchOnStart || StartOfString)
                    {

                        var Match = Rule.Pattern.Match(MidString);
                        if (Match.Success)
                        {
                             FoundRule = true;
                             var Substitution = Match.Result(Rule.Substitution);
                             if (Rule.RevisitPosition < 0)
                             {
                                 // Reset the new position to the end of the substitution.
                                 OutString.Append(Substitution);
                                 MidString = MidString.Substring(Match.Length);
                             }
                             else
                             {
                                 MidString = string.Concat(Substitution, MidString.Substring(Match.Length));
                             }

                            
                        }
                    }
                }
                if (!FoundRule)
                {
                    // Move forward by 1.
                    OutString.Append(MidString[0]);
                    MidString = MidString.Substring(1);
                }
                StartOfString = false;

            }
            return OutString.ToString();
        }
    }
}