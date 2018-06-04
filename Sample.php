<?php

$autoload = __DIR__.'/vendor/autoload.php';

if ( ! file_exists($autoload))
{
    exit("Need Composer!");
}

require $autoload;

$detector = new \Googlei18n\MyanmarTools\ZawgyiDetector();
$input1 = 'အျပည္ျပည္ဆိုင္ရာ လူ႔အခြင့္အေရး ေၾကညာစာတမ္း';
$score1 = $detector->getZawgyiProbability($input1, false);
printf("Zawgyi Score: %.6f", $score1);
echo "<br />";


$input2 = 'ချယ်ရီ';
$score2 = $detector->getZawgyiProbability($input2);
printf("Unicode Score: %.6f", $score2);

//function hexTo32Float($strHex) {
//    $v = hexdec($strHex);
//    $x = ($v & ((1 << 23) - 1)) + (1 << 23) * ($v >> 31 | 1);
//    $exp = ($v >> 23 & 0xFF) - 127;
//    return $x * pow(2, $exp - 23);
//}
//
//function hex2float($strHex) {
//    $hex = sscanf($strHex, "%02x%02x%02x%02x");
//    $hex = array_reverse($hex);
//    $bin = implode('', array_map('chr', $hex));
//    $array = unpack("gnum", $bin);
//    return $array['num'];
//}
//
//echo hex2float("3ee30553");

//echo ord_utf8("abc");
//bin2hex(iconv('UTF-8', 'UCS-2', 'က'));