# Myanmar Tools PHP Documentation

This documentation is for PHP specific usage of *Myanmar Tools*. For general documentation, see [the top-level README](../../README.md).

## PHP Usage

Prerequisites: PHP >=7.0 and composer >= 1.0

Add the dependency to your project:

```bash
$ composer require googlei18n/myanmar-tools
```

To detect Zawgyi, create an instance of ZawgyiDetector, and call `getZawgyiProbability` with your string. Generally, if the score is greater than or equal to 0.95, you can generally assume the string is Zawgyi. If the score is lower or equal to 0.05, you can assume it is Unicode.

```
$autoload = __DIR__.'/vendor/autoload.php';
if (!file_exists($autoload))
{
	exit("Need Composer!");
}
require $autoload;

use Googlei18n\MyanmarTools\ZawgyiDetector;

$detector = new ZawgyiDetector();
$input1 = 'အျပည္ျပည္ဆိုင္ရာ လူ႔အခြင့္အေရး ေၾကညာစာတမ္း';
$score1 = $detector->getZawgyiProbability($input1);
// score is 1.0 (The input is definitely Zawgyi)

$input2 = 'ချယ်ရီ';
$score2 = $detector->getZawgyiProbability($input2);
// score is 0.0 (The input is definitely Unicode).
```

To convert from Zawgyi to Unicode, you can use [Rabbit Converter PHP version](https://github.com/Rabbit-Converter/Rabbit-PHP).  Install it like this:

```bash
$ composer require "rabbit-converter/rabbit-php:dev-master"
```

Then convert from Z to U like this:

```ruby
Rabbit::zg2uni("သီဟိုဠ္မွ ဉာဏ္ႀကီးရွင္သည္ အာယုဝဍ္ဎနေဆးၫႊန္းစာကို ဇလြန္ေဈးေဘးဗာဒံပင္ထက္ အဓိ႒ာန္လ်က္ ဂဃနဏဖတ္ခဲ့သည္။");
// output is now "သီဟိုဠ်မှ ဉာဏ်ကြီးရှင်သည် အာယုဝဍ်ဎနဆေးညွှန်းစာကို ဇလွန်ဈေးဘေးဗာဒံပင်ထက် အဓိဋ္ဌာန်လျက် ဂဃနဏဖတ်ခဲ့သည်။"
```

For a complete working example, see [samples/php/Demo.php](../../samples/php/Demo.php).

## Contributing

Bug reports and pull requests are welcome on GitHub at https://github.com/google/myanmar-tools . This project is intended to be a safe, welcoming space for collaboration, and contributors are expected to adhere to the [Contributor Covenant](http://contributor-covenant.org) code of conduct.

## License

The package is available as open source under the terms of the [Apache-2.0 License](http://www.apache.org/licenses/LICENSE-2.0).

## Code of Conduct

Everyone interacting in the Myanmar-Tools project’s codebases, issue trackers, chat rooms and mailing lists is expected to follow the [code of conduct](https://github.com/[USERNAME]/zawgyidetector/blob/master/CODE_OF_CONDUCT.md).
