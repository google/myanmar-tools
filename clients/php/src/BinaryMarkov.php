<?php
/**
 * Created by SteveNay.
 * User: Nay Lin Aung
 * Date: 5/24/2018
 * Time: 3:24 PM
 */

namespace Googlei18n\MyanmarTools;
use Exception;

/**
 * A class that behaves as if it were two Markov chains, called Chain A and Chain B. Whereas a
 * normal Markov chain would expose the log probability of a transition, this class exposes the
 * difference between the log probabilities of the two Markov chains. When training, you can specify
 * which Markov chain a transition should be added to.
 * <p>
 * <p>The reasoning behind this class is that it has a smaller data footprint than two separate
 * Markov chain objects.
 * <p>
 * <p>This class is tested in the google-myanmar-tools-data package because this implementation does
 * not include any logic for building a new instance other than from a binary data stream.
 */
class BinaryMarkov
{
    /**
     * Magic number used to identify this object in byte streams. (Reads in ASCII as "BMARKOV ")
     */
    const BINARY_TAG = "424D41524B4F5620";

    /**
     * Current serial format version number, used in association with the magic number.
     */
    const BINARY_VERSION = 0;
    private $logProbabilityDifferences;

    /**
     * Creates an instance from a binary data stream.
     *
     * @param $stream
     * @throws Exception If there was a problem reading the data.
     */
    public function __construct($stream)
    {
        // Check magic number and serial version number
        $binaryTag = strtoupper(unpack("H*", fread($stream, 8))[1]);
        if ($binaryTag !== self::BINARY_TAG) {
            throw new Exception(
                sprintf(
                    "Unexpected magic number; expected %016X but got %016X", self::BINARY_TAG, $binaryTag));
        }

        $binaryVersion = (int) unpack("H*", fread($stream, 4))[1];
        if ($binaryVersion != self::BINARY_VERSION) {
            throw new Exception(
                sprintf(
                    "Unexpected serial version number; expected %08X but got %08X",
                    self::BINARY_VERSION, $binaryVersion));
        }

        $this->generateProbabilityDifferences($stream);
    }

    public function generateProbabilityDifferences($stream)
    {
        $size = unpack('n*', fread($stream, 2))[1];
        $logProbabilityDifferences = array();

        for ($i1 = 0; $i1 < $size; $i1++) {
            $entries  = unpack('n*', fread($stream, 2))[1];
            $fallback = ($entries == 0) ? 0.0 : unpack("G*", fread($stream, 4))[1];
            $next     = -1;
            for ($i2 = 0; $i2 < $size; $i2++) {
                if ($entries > 0 && $next < $i2) {
                    $next = unpack("n*", fread($stream, 2))[1];
                    $entries--;
                }

                if ($next == $i2) {
                    $readData = fread($stream, 4);
                    $logProbabilityDifferences[$i1][$i2] = unpack("G*", $readData)[1];
                } else {
                    $logProbabilityDifferences[$i1][$i2] = $fallback;
                }

            }
        }

        $this->logProbabilityDifferences = $logProbabilityDifferences;
    }

    /**
     * Gets the difference in log probabilities between chain A and chain B. This behaves as if you
     * had two Markov chains and called <code>
     * chainA.getLogProbability(i1, i2) - chainB.getLogProbability(i1, i2)</code>.
     *
     * @param i1 The index of the source node to transition from.
     * @param i2 The index of the destination node to transition to.
     * @return The difference between A and B in log probability of transitioning from i1 to i2.
     */
    public function getLogProbabilityDifference(int $i1, int $i2): float
    {
        return $this->logProbabilityDifferences[$i1][$i2];
    }
}