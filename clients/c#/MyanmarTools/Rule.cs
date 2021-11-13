using System.Text.RegularExpressions;

namespace MyanmarTools
{
    /// Class implementing a rule as part of a transliteration phase.
    public class Rule
    {
        public string Substitution { get; private set; }
        public bool MatchOnStart { get; private set; }
        public int RevisitPosition { get; private set; }
        public Regex Pattern { get; private set; }
        public string Info { get; private set; }
        public string ContextBefore { get; private set; }
        public string ContextAfter { get; private set; }
        public Rule(string PatternString, string Substitution,bool MatchOnStart=false,int RevisitPosition=-1)
        {

            this.Pattern = new Regex($"^{PatternString}");
            this.Substitution = Substitution;
            this.MatchOnStart = MatchOnStart;
            this.RevisitPosition = RevisitPosition;

        }

        public Rule SetInfo(string RuleInfo)
        {
            Info = RuleInfo;
            return this;
        }
        public Rule SetMatchOnStart()
        {
            MatchOnStart = true;
            return this;
        }

        public Rule SetRevisitPosition(int NewPos)
        {
            RevisitPosition = NewPos;
            return this;
        }

        public Rule SetBeforeContext(string Before)
        {
            ContextBefore = Before;
            return this;
        }

        public Rule SetAfterContext(string After)
        {
            ContextAfter = After;
            return this;
        }

    }
}
