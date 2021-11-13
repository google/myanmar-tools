using System;
using System.IO;
using MyanmarTools.Utils;

namespace MyanmarTools.Markov
{
    /// <summary>
    /// <para>
    /// Internally, this class maintains two Markov chains, one representing Unicode and the other
    /// representing Zawgyi. An input string is evaluated against both chains, and the chain that returns
    /// the higher probability becomes the prediction.
    /// </para>
    /// <para>
    /// A string is evaluated as a sequence of transitions between states, including transitions to
    /// the edges of the string. For example, the string "ABC" contains 4 transitions: NULL to A, A to B,
    /// B to C, and C to NULL.
    /// </para>
    /// <para>
    /// For the purposes of Unicode/Zawgyi detection, all characters are treated as the NULL state
    /// except for characters in the Myanmar script or characters in the Unicode whitespace range U+2000
    /// through U+200B.
    /// </para>
    /// <para>
    /// This class is tested in the myanmar-tools-training package because this implementation does
    ///  not include any logic for building a new instance other than from a binary data stream.
    /// </para>
    /// </summary>
    public class ZawgyiUnicodeMarkovModel
    {
        /** Magic number used to identify this object in byte streams. (Reads in ASCII as "UZMODEL") */
        private static readonly long BINARY_TAG = 0x555A4D4F44454C20L;

        // Standard Myanmar code point range before digits
        private static readonly int STD_CP0 = '\u1000';
        private static readonly int STD_CP1 = '\u103F';

        // Standard Myanmar code point range after digits
        private static readonly int AFT_CP0 = '\u104A';
        private static readonly int AFT_CP1 = '\u109F';

        // Extended Myanmar code point range A
        private static readonly int EXA_CP0 = '\uAA60';
        private static readonly int EXA_CP1 = '\uAA7F';

        // Extended Myanmar code point range B
        private static readonly int EXB_CP0 = '\uA9E0';
        private static readonly int EXB_CP1 = '\uA9FF';

        // Unicode space characters
        private static readonly int SPC_CP0 = '\u2000';
        private static readonly int SPC_CP1 = '\u200B';

        // Indices into Markov nodes
        private static readonly short STD_OFFSET = 1;
        private static readonly short AFT_OFFSET = (short)(STD_OFFSET + STD_CP1 - STD_CP0 + 1);
        private static readonly short EXA_OFFSET = (short)(AFT_OFFSET + AFT_CP1 - AFT_CP0 + 1);
        private static readonly short EXB_OFFSET = (short)(EXA_OFFSET + EXA_CP1 - EXA_CP0 + 1);
        private static readonly short SPC_OFFSET = (short)(EXB_OFFSET + EXB_CP1 - EXB_CP0 + 1);
        private static readonly short END_OFFSET = (short)(SPC_OFFSET + SPC_CP1 - SPC_CP0 + 1);

        BinaryMarkov Classifier;
        int ssv;

        /**
       * SSV: An ID representing which Unicode code points to include in the model:
       *
       * <p>SSV_STD_EXA_EXB_SPC - include Myanmar, Extended A, Extended B, and space-like
       * <p>STD_EXA_EXB - same as above but no space-like code points
       *
       * <p>"SSV" originally stands for State Set Version.
       */
        static readonly int SSV_STD_EXA_EXB_SPC = 0;
        static readonly int SSV_COUNT = 2;


        /// <summary>
        /// Returns the index of the state in the Markov chain corresponding to the given code point.
        /// <para>
        /// Code points in the standard Myanmar range, Myanmar Extended A, Myanmar Extended B, and
        /// Unicode Whitespace each have a unique state assigned to them. All other code points are mapped
        /// to state 0.
        /// </para>
        /// <para>
        /// Package-private so that the builder can use this method.
        /// </para>
        /// </summary>
        /// <param name="cp">The code point to convert to a state index.</param>
        /// <param name="ssv">The SSV corresponding to which code points included in the model.</param>
        /// <returns>The index of the state in the Markov chain. 0 <= state < GetSize()</returns>
        static int GetIndexForCodePoint(int cp, int ssv)
        {
            if (STD_CP0 <= cp && cp <= STD_CP1)
            {
                return cp - STD_CP0 + STD_OFFSET;
            }
            if (AFT_CP0 <= cp && cp <= AFT_CP1)
            {
                return cp - AFT_CP0 + AFT_OFFSET;
            }
            if (EXA_CP0 <= cp && cp <= EXA_CP1)
            {
                return cp - EXA_CP0 + EXA_OFFSET;
            }
            if (EXB_CP0 <= cp && cp <= EXB_CP1)
            {
                return cp - EXB_CP0 + EXB_OFFSET;
            }
            if (ssv == SSV_STD_EXA_EXB_SPC && SPC_CP0 <= cp && cp <= SPC_CP1)
            {
                return cp - SPC_CP0 + SPC_OFFSET;
            }
            return 0;
        }

        /// <summary>
        /// The number of states in the Markov chain.
        /// </summary>
        /// <param name="ssv"></param>
        /// <returns></returns>
        static short GetSize(int ssv)
        {
            return ssv == SSV_STD_EXA_EXB_SPC ? END_OFFSET : SPC_OFFSET;
        }

        /// <summary>
        /// Creates an instance from a binary data stream.
        /// </summary>
        /// <param name="Stream">Model Stream</param>
        /// <exception cref="IOException">If there was a problem reading the data.</exception>
        public ZawgyiUnicodeMarkovModel(Stream Stream)
        {
            using (var Reader = new BigEndianBinaryReader(Stream))
            {
                // Check magic number and serial version number
                var BinaryTag =Reader.ReadInt64();

                if (BinaryTag != BINARY_TAG)
                {
                    throw new IOException(
                        String.Format(
                            "Unexpected magic number; expected 0x{0:X} but got 0x{1:X}", BINARY_TAG, BinaryTag));
                }

                var BinaryVersion = Reader.ReadInt32();

                if (BinaryVersion == 1)
                {
                    // Binary version 1 has no SSV field; SSV_STD_EXA_EXB_SPC is always used
                    ssv = SSV_STD_EXA_EXB_SPC;
                }
                else if (BinaryVersion == 2)
                {
                    // Binary version 2 adds the SSV field
                    ssv = Reader.ReadInt32();
                }
                else
                {
                    throw new IOException(
                        String.Format(
                            "Unexpected serial version number; expected 1 or 2 but got {0}",
                            BinaryVersion));
                }
                if (ssv < 0 || ssv >= SSV_COUNT)
                {
                    throw new IOException(
                        String.Format(
                            "Unexpected value in ssv position; expected 0 or 1 but got {0}",
                            ssv));
                }

                Classifier = new BinaryMarkov(Stream);
            }

        }

        /// <summary>
        /// Runs the given input string on both internal Markov chains and computes the probability of the
        /// string being unicode or zawgyi.
        /// </summary>
        /// <param name="Input">The string to evaluate.</param>
        /// <returns>The probability that the string is Zawgyi given that it is either Unicode or Zawgyi, or
        /// -Infinity if there are no Myanmar range code points in the string.
        /// </returns>
        public double Predict(String Input)
        {
            // Start at the base state
            int prevCp = 0;
            int prevState = 0;
            double totalDelta = 0.0;
            bool seenTransition = false;
            for (int offset = 0; offset <= Input.Length;)
            {
                int cp;
                int currState;
                if (offset == Input.Length)
                {
                    cp = 0;
                    currState = 0;
                }
                else
                {
                    cp = (int)Input[offset];
                    currState = GetIndexForCodePoint(cp, ssv);
                }
                // Ignore 0-to-0 transitions
                if (prevState != 0 || currState != 0)
                {
                    var delta = Classifier.GetLogProbabilityDifference(prevState, currState);
                    totalDelta += delta;
                    seenTransition = true;
                }

                offset += cp >= 0x10000 ? 2 : 1;
                prevCp = cp;
                prevState = currState;

            }

            // Special case: if there is no signal, return Double.NegativeInfinity,
            // which will get interpreted by users as strong Unicode.
            // This happens when the input string contains no Myanmar-range code points.
            if (!seenTransition)
            {
                return Double.NegativeInfinity;
            }

            return 1.0 / (1.0 + Math.Exp(totalDelta));

        }
    }
}