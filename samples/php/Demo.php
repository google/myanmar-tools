<?php
// include autoload
$autoload = join(DIRECTORY_SEPARATOR, [__DIR__, "vendor", "autoload.php"]);
if (!file_exists($autoload))
    exit("Need Composer!");
require $autoload;

use Googlei18n\MyanmarTools\ZawgyiDetector;

$detector = new ZawgyiDetector();

// Unicode string
$unicodeInput = 'အပြည်ပြည်ဆိုင်ရာ လူ့အခွင့်အရေး ကြေညာစာတမ်း';

// Zawgyi string
$zawgyiInput = 'အျပည္ျပည္ဆိုင္ရာ လူ႔အခြင့္အေရး ေၾကညာစာတမ္း';

$unicodeScore = $detector->getZawgyiProbability($unicodeInput);
$zawgyiScore  = $detector->getZawgyiProbability($zawgyiInput);

assert($unicodeScore < 0.001);
assert($zawgyiScore > 0.999);

printf("Unicode Score: %.6f", $unicodeScore);
echo "<br />";
printf("Zawgyi Score: %.6f", $zawgyiScore);

echo "<br />";

// Convert the second string to Unicode:
$zawgyi2UniConverted = Rabbit::zg2uni($zawgyiInput);
assert($unicodeInput === $zawgyi2UniConverted, "Converted string is equal to unicode");
printf("Converted Text: %s\n", $zawgyi2UniConverted);
