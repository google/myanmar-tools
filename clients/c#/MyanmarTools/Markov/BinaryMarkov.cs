using System;
using System.IO;
using MyanmarTools.Utils;

namespace MyanmarTools.Markov
{
    /// <summary>
    /// <para>A class that behaves as if it were two Markov chains, called Chain A and Chain B. Whereas a
    /// normal Markov chain would expose the log probability of a transition, this class exposes the
    /// difference between the log probabilities of the two Markov chains. When training, you can specify
    ///  which Markov chain a transition should be added to.
    /// </para>
    /// <para>
    /// The reasoning behind this class is that it has a smaller data footprint than two separate
    /// Markov chain objects.
    /// </para>
    /// <para>
    /// This class is tested in the myanmar-tools-training package because this implementation does
    /// not include any logic for building a new instance other than from a binary data stream.
    /// </para>
    /// </summary>
    public class BinaryMarkov
    {

        /** Magic number used to identify this object in byte streams. (Reads in ASCII as "BMARKOV ") */
        private static readonly long BINARY_TAG = 0x424D41524B4F5620L;
        /** Current serial format version number, used in association with the magic number. */
        private static readonly int BINARY_VERSION = 0;
        private float[,] LogProbabilityDifferences;

        /// <summary>
        /// Creates an instance from a binary data stream.
        /// </summary>
        /// <param name="Stream">Model Stream</param>
        /// <exception cref="IOException">
        /// If there was a problem reading the data.
        /// </exception>
        public BinaryMarkov(Stream Stream)
        {
            using (var Reader = new BigEndianBinaryReader(Stream))
            {
                // Check magic number and serial version number
                var BinaryTag = Reader.ReadInt64();

                if (BinaryTag != BINARY_TAG)
                {
                    throw new IOException(
                        String.Format(
                            "Unexpected magic number; expected 0x{0:X} but got 0x{1:X}", BINARY_TAG, BinaryTag));
                }

                var BinaryVersion = Reader.ReadInt32();
                if (BinaryVersion != BINARY_VERSION)
                {
                    throw new IOException(
                        String.Format(
                            "Unexpected serial version number; expected 0x{0:X} but got 0x{1:X}",
                            BINARY_VERSION, BinaryVersion));
                }

                short Size = Reader.ReadInt16();
                var LogProbabilityDifferences = new float[Size, Size];
                for (short i1 = 0; i1 < Size; i1++)
                {
                    short Entries = Reader.ReadInt16();
                    float Fallback = (Entries == 0) ? 0.0f : Reader.ReadSingle();
                    short Next = -1;
                    for (short i2 = 0; i2 < Size; i2++)
                    {
                        if (Entries > 0 && Next < i2)
                        {
                            Next = Reader.ReadInt16();
                            Entries--;
                        }
                        LogProbabilityDifferences[i1, i2] = (Next == i2) ? Reader.ReadSingle() : Fallback;
                    }
                }
                this.LogProbabilityDifferences = LogProbabilityDifferences;
            }


        }

        /// <summary>
        /// Gets the difference in log probabilities between chain A and chain B. This behaves as if you
        /// had two Markov chains and called
        /// <code>
        /// chainA.GetLogProbability(i1, i2) - chainB.getLogProbability(i1, i2)
        /// </code>
        /// </summary>
        /// <param name="i1">The index of the source node to transition from.</param>
        /// <param name="i2">The index of the destination node to transition to.</param>
        /// <returns>The difference between A and B in log probability of transitioning from i1 to i2.</returns>

        public float GetLogProbabilityDifference(int i1, int i2)
        {
            return LogProbabilityDifferences[i1, i2];
        }

    }
}