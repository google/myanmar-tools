<?php
/**
 * Created by PhpStorm.
 * User: Dell
 * Date: 5/25/2018
 * Time: 4:15 PM
 */

function uniord($u) {
    $k = mb_convert_encoding($u, 'UCS-2LE', 'UTF-8');
    $k1 = ord(substr($k, 0, 1));
    $k2 = ord(substr($k, 1, 1));
    return $k2 * 256 + $k1;
}

// https://stackoverflow.com/a/395936/3229851
function utf8_to_unicode( $str ) {

    $unicode = array();
    $values = array();
    $lookingFor = 1;

    for ($i = 0; $i < strlen( $str ); $i++ ) {
        $thisValue = ord( $str[ $i ] );
        if ( $thisValue < ord('A') ) {
            // exclude 0-9
            if ($thisValue >= ord('0') && $thisValue <= ord('9')) {
                // number
                $unicode[] = chr($thisValue);
            }
            else {
                $unicode[] = '%'.dechex($thisValue);
            }
        } else {
            if ( $thisValue < 128)
                $unicode[] = $str[ $i ];
            else {
                if ( count( $values ) == 0 ) $lookingFor = ( $thisValue < 224 ) ? 2 : 3;
                $values[] = $thisValue;
                if ( count( $values ) == $lookingFor ) {
                    $number = ( $lookingFor == 3 ) ?
                        ( ( $values[0] % 16 ) * 4096 ) + ( ( $values[1] % 64 ) * 64 ) + ( $values[2] % 64 ):
                        ( ( $values[0] % 32 ) * 64 ) + ( $values[1] % 64 );
                    $number = dechex($number);
                    $unicode[] = (strlen($number)==3)?"%u0".$number:"%u".$number;
                    $values = array();
                    $lookingFor = 1;
                } // if
            } // if
        }
    } // for

    return implode("",$unicode);

} // utf8_to_unicode


echo str_replace('%', '\\', utf8_to_unicode("ကခဂ"));
echo "Hello World for Wamp!!!";