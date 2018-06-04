<?php
/**
 * Created by SteveNay.
 * User: Nay Lin Aung
 * Date: 5/26/2018
 * Time: 4:10 PM
 */

namespace Googlei18n\MyanmarTools;

use Exception;

final class ZawgyiDetector
{
    private $model = 0;

    public function __construct()
    {
        try {
            $fileName = join(DIRECTORY_SEPARATOR, [__DIR__, "..", "resources", "zawgyiUnicodeModel.dat"]);
            // file open with read permission with binary format
            $inStream = fopen($fileName, "rb");

            if ($inStream == null) {
                // IO Exception
                throw new Exception("Model file zawgyiUnicodeModel.dat not found");
            }

            $this->model = new ZawgyiUnicodeMarkovModel($inStream);
            fclose($inStream);

        } catch (Exception $exception) {
            throw new Exception("Could not load Markov model from resource file", 0, $exception);
        }
    }

    /**
     * Performs detection on the given string. Returns the probability that the string is Zawgyi given
     * that it is either Unicode or Zawgyi. Values approaching 1 are strong Zawgyi; values approaching
     * 0 are strong Unicode; and values close to 0.5 are toss-ups.
     *
     * <p>If the string does not contain any Myanmar range code points, -Infinity is returned.
     *
     * @param string $input The string on which to run detection.
     * @param bool $verbose If true, print debugging information to standard output.
     * @return float The probability that the string is Zawgyi (between 0 and 1), or -Infinity if the string
     *     contains no Myanmar range code points.
     */
    public function getZawgyiProbability(string $input, bool $verbose = false): float
    {
        return $this->model->predict($input, $verbose);
    }
}