<?php
/**
 * Created by SteveNay.
 * User: NayLinAung
 * Date: 5/24/2018
 * Time: 3:39 PM
 */

namespace Googlei18n\MyanmarTools;

use Exception;

/**
 * A Markov model to predict whether a string is more likely Unicode or Zawgyi.
 * <p>
 * <p>Internally, this class maintains two Markov chains, one representing Unicode and the other
 * representing Zawgyi. An input string is evaluated against both chains, and the chain that returns
 * the higher probability becomes the prediction.
 * <p>
 * <p>A string is evaluated as a sequence of transitions between states, including transitions to
 * the edges of the string. For example, the string "ABC" contains 4 transitions: NULL to A, A to B,
 * B to C, and C to NULL.
 * <p>
 * <p>For the purposes of Unicode/Zawgyi detection, all characters are treated as the NULL state
 * except for characters in the Myanmar script or characters in the Unicode whitespace range U+2000
 * through U+200B.
 * <p>
 * <p>This class is tested in the google-myanmar-tools-data package because this implementation does
 * not include any logic for building a new instance other than from a binary data stream.
 */
class ZawgyiUnicodeMarkovModel
{
    /** Magic number used to identify this object in byte streams. (Reads in ASCII as "BMARKOV ") */
    const BINARY_TAG = "555a4d4f44454c20";

    // Standard Myanmar code point range before digits
    const STD_CP0 = 0x1000;
    const STD_CP1 = 0x103F;

    // Standard Myanmar code point range after digits
    const AFT_CP0 = 0x104A;
    const AFT_CP1 = 0x109F;

    // Extended Myanmar code point range A
    const EXA_CP0 = 0xAA60;
    const EXA_CP1 = 0xAA7F;

    // Extended Myanmar code point range B
    const EXB_CP0 = 0xA9E0;
    const EXB_CP1 = 0xA9FF;

    // Unicode space characters
    const SPC_CP0 = 0x2000;
    const SPC_CP1 = 0x200B;

    // Indices into Markov nodes
    const STD_OFFSET = 1;
    const AFT_OFFSET = self::STD_OFFSET + self::STD_CP1 - self::STD_CP0 + 1;
    const EXA_OFFSET = self::AFT_OFFSET + self::AFT_CP1 - self::AFT_CP0 + 1;
    const EXB_OFFSET = self::EXA_OFFSET + self::EXA_CP1 - self::EXA_CP0 + 1;
    const SPC_OFFSET = self::EXB_OFFSET + self::EXB_CP1 - self::EXB_CP0 + 1;
    const NUM_STATES = self::SPC_OFFSET + self::SPC_CP1 - self::SPC_CP0 + 1;

    // BinaryMarkov
    private $classifier;

    // TODO: Not currently used
    private $ssv;

    /**
     * Creates an instance from a binary data stream.
     *
     * @throws Exception If there was a problem reading the data.
     */
    public function __construct($stream)
    {
        // Check magic number and serial version number
        $binaryTag = unpack("H*", fread($stream, 8))[1];

        if ($binaryTag !== self::BINARY_TAG) {
            throw new Exception(
                sprintf(
                    "Unexpected magic number; expected %016X but got %016X", self::BINARY_TAG, $binaryTag));
        }

        $binaryVersion = (int) unpack("H*", fread($stream, 4))[1];

        if ($binaryVersion === 1) {
            $this->ssv = 0;
        } else if ($binaryVersion == 2) {
            // TODO: Support nonzero SSV if needed in the future
            $this->ssv = (int) unpack("H*", fread($stream, 4))[1];
            if ($this->ssv != 0) {
                throw new Exception(sprintf("Unsupported ssv: %d", $this->ssv));
            }
        } else {
            throw new Exception(
                sprintf(
                    "Unexpected serial version number; expected 1 or 2 but got %08X",
                    $binaryVersion));
        }

        // check for utf-8 encoding.
        if (mb_internal_encoding() != "UTF-8") {
            $message = sprintf("Encoding must be UTF-8; got %s. Please call mb_internal_encoding('UTF-8')", mb_internal_encoding());
            throw new Exception($message);
        }

        $this->classifier = new BinaryMarkov($stream);
    }

    /**
     * Returns the index of the state in the Markov chain corresponding to the given code point.
     * <p>
     * <p>Code points in the standard Myanmar range, Myanmar Extended A, Myanmar Extended B, and
     * Unicode Whitespace each have a unique state assigned to them. All other code points are mapped
     * to state 0.
     * <p>
     * <p>Package-private so that the builder can use this method.
     *
     * @param int $cp The code point to convert to a state index.
     * @return The index of the state in the Markov chain. 0 <= state < getSize()
     */
    public static function getIndexForCodePoint(int $cp)
    {
        if (self::STD_CP0 <= $cp && $cp <= self::STD_CP1) {
            return $cp - self::STD_CP0 + self::STD_OFFSET;
        }

        if (self::AFT_CP0 <= $cp && $cp <= self::AFT_CP1) {
            return $cp - self::AFT_CP0 + self::AFT_OFFSET;
        }

        if (self::EXA_CP0 <= $cp && $cp <= self::EXA_CP1) {
            return $cp - self::EXA_CP0 + self::EXA_OFFSET;
        }

        if (self::EXB_CP0 <= $cp && $cp <= self::EXB_CP1) {
            return $cp - self::EXB_CP0 + self::EXB_OFFSET;
        }

        if (self::SPC_CP0 <= $cp && $cp <= self::SPC_CP1) {
            return $cp - self::SPC_CP0 + self::SPC_OFFSET;
        }

        return 0;
    }

    /**
     * The number of states in the Markov chain.
     */
    public static function getSize()
    {
        return self::NUM_STATES;
    }

    /**
     * @param string $input
     * @param bool $verbose
     * @return float
     */
    public function predict(string $input, bool $verbose = false): float
    {
        if ($verbose) {
            printf("Running detector on string: %s\n", $input);
        }

        // Start at the base state
        $prevCp    = 0;
        $prevState = 0;

        $totalDelta     = 0.0;
        $seenTransition = false;

        $inputBreakIterator = \IntlBreakIterator::createCodePointInstance();
        $inputBreakIterator->setText($input);

        do {
            $idx = $inputBreakIterator->next();
            $cp = ($idx === \IntlBreakIterator::DONE) ? 0 : $inputBreakIterator->getLastCodePoint();
            $currState = self::getIndexForCodePoint($cp);
            // Ignore 0-to-0 transitions
            if ($prevState != 0 || $currState != 0) {
                $delta = $this->classifier->getLogProbabilityDifference($prevState, $currState);
                if ($verbose) {
                    printf("U+%04X -> U+%04X: delta=%8.4f ", $prevCp, $cp, $delta);
                    for ($i = 1; $i < abs($delta); $i++) {
                        printf("!");
                    }
                    print("\n");
                }
                $totalDelta     += $delta;
                $seenTransition = true;
            }

            $prevCp    = $cp;
            $prevState = $currState;
        } while ($cp !== 0);

        if ($verbose) {
            printf("Final: delta=%.4f\n", $totalDelta);
        }

        // Special case: if there is no signal, return -Infinity,
        // which will get interpreted by users as strong Unicode.
        // This happens when the input string contains no Myanmar-range code points.
        if (!$seenTransition)
            return INF;

        // result = Pz/(Pu+Pz)
        //        = exp(logPz)/(exp(logPu)+exp(logPz))
        //        = 1/(1+exp(logPu-logPz))
        return 1.0 / (1.0 + exp($totalDelta));
    }
}