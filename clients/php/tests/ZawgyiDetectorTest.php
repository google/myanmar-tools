<?php
/**
 * Created by SteveNay.
 * User: NayLinAung
 * Date: 5/28/2018
 * Time: 8:46 PM
 */

namespace test;

use Googlei18n\MyanmarTools\ZawgyiDetector;
use PHPUnit\Framework\TestCase;

class ZawgyiDetectorTest extends TestCase
{
    private $detector;

    public function setUp()
    {
        // set multibyte encoding.
        mb_internal_encoding('utf-8');

        $this->detector = new ZawgyiDetector();
    }

    /** @tests */
    public function ignore_non_myanmar_code_points()
    {
        $allAscii     = 'blah blah blah blah blah';
        $mixedUnicode = "<span>blah blah ဒဂုန်ဦးစန်းငွေ </span> blah blah blah blah";
        $mixedZawgyi  = 'blah blah blah blah blah သို႔သြားပါ။ blah blah blah';

        $this->assertEquals(INF, $this->detector->getZawgyiProbability($allAscii), "All ASCII");
        $this->assertLessThan(0.01, $this->detector->getZawgyiProbability($mixedUnicode), "Mixed Unicode");
        $this->assertGreaterThan(0.99, $this->detector->getZawgyiProbability($mixedZawgyi), "Mixed Zawgyi");
    }

    /** @tests */
    public function returns_low_score_for_strong_unicode()
    {
        $strongUnicode = 'အပြည်ပြည်ဆိုင်ရာ လူ့အခွင့်အရေး ကြေညာစာတမ်း';
        $this->assertLessThan(0.001, $this->detector->getZawgyiProbability($strongUnicode), "Strong Unicode");
    }

    /** @tests */
    public function returns_hgih_score_for_strong_zawgyi()
    {
        $strongZawgyi = "အျပည္ျပည္ဆိုင္ရာ လူ႔အခြင့္အေရး ေၾကညာစာတမ္း";
        $this->assertGreaterThan(0.999, $this->detector->getZawgyiProbability($strongZawgyi), "Strong Zawgyi");
    }

    /** @tests */
    public function ignore_numerals()
    {
        // Digits/numerals are ignored and treated like non-Myanmar code points.
        $this->assertEquals(INF, $this->detector->getZawgyiProbability("၉၆.၀ kHz"));
        $this->assertEquals($this->detector->getZawgyiProbability("ဒဂုန်"), $this->detector->getZawgyiProbability("၂၄၀၉ ဒဂုန်"));
    }

    /** @tests */
    public function difficult_code_points()
    {
        /* These are strings that the detector has gotten wrong. They mostly render in both Zawgyi and
         * Unicode, but the words they spell make sense in one encoding but not in the other. As the
         * detector improves, change this tests case with scores to match the new output.
         */
        $cases = [
            // STRINGS IDENTICAL IN UNICODE/ZAWGYI
            // TODO: These should return scores in the middle range.
            ["အသံကို အစားထိုးလိုပါသလား။", 0.995], // Truth: Identical in U/Z
            ["နမူနာ", 0.26], // Truth: Identical in U/Z
            [" ဦး", 0.35], // Truth: Identical in U/Z

            // UNICODE STRINGS WITH HIGH ZAWGYI SCORES
            // TODO: Fix detection so that these get low Zawgyi scores.
            ["အစားထိုး အထူးအက္ခရာ", 0.995], // Truth: Unicode (confirmed by yinmay@)
            ["ယခု မိုးရွာနေပါသလား။", 0.995], // Truth: Unicode (confirmed by yinmay@)
            ["အခြား", 0.74], // Truth: Unicode (confirmed by yinmay@)

            // DIFFICULT STRINGS THAT DETECT CORRECTLY
            // Changes to the detector should not significantly change these scores.
            ["ကာမစာအုပ္မ်ား(ေစာက္ပတ္စာအုပ္မ်ား)", 1.0], // Truth: Zawgyi (confirmed by yinmay@)
            ["ညႇပ္စရာမလို", 0.82], // Truth: Zawgyi (confirmed by yinmay@)
        ];

        foreach ($cases as $case) {
            $input    = $case[0];
            $expected = $case[1];
            $actual   = $this->detector->getZawgyiProbability($input);
            $delta    = 0.005;
            $this->assertEquals($expected, $actual, $input, $delta);
        }
    }

    /** @tests */
    public function compatibility()
    {
        $fileName = join(DIRECTORY_SEPARATOR, [__DIR__, "..", "resources", "compatibility.tsv"]);
        $row = 1;
        if (($handle = fopen($fileName, "r")) !== FALSE) {
            // omitting the length parameter by setting zero
            while (($data = fgetcsv($handle, 0, "\t")) !== FALSE) {
                $expected = $data[0] === "-Infinity" ? INF : (double) $data[0];
                $actual = $this->detector->getZawgyiProbability($data[1]);
                $this->assertEquals($expected, $actual, implode(" expected from ", $data));
            }
        }

    }
}