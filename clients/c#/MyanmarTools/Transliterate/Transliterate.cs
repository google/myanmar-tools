using System.Collections.Generic;

namespace MyanmarTools.Transliterate
{
    /// <summary>
    /// A engine for running the phases of transliteration, similar to the ICU4J transliterator.
    /// </summary>
    public abstract class Transliterate
    {

        public string Name { get; private set; }  // For identification
        private List<Phase> TransliteratePhases = new List<Phase>();


        public Transliterate(string Name)
        {
            this.Name = Name;
        }
        public Phase AddPhase()
        {
            var NewPhase = new Phase();
            NewPhase.Info = TransliteratePhases.Count.ToString();
            TransliteratePhases.Add(NewPhase);
            return NewPhase;
        }


        /// <summary>
        /// Apply the transliteration to the input string,
        /// </summary>
        /// <param name="InString">Input String</param>
        /// <returns>The converted result</returns>
        public string Convert(string InString)
        {
            return RunAllPhases(InString);
        }

        private string RunAllPhases(string InString)
        {
            var OutString = InString;

            foreach (var Phase in TransliteratePhases)
            {

                OutString = Phase.RunPhase(OutString);
            }

            return OutString;
        }

    }
}